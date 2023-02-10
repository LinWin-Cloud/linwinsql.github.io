package LinwinVOS.Mirror;

import LinwinVOS.LinwinVOS;
import LinwinVOS.data.Json;

import javax.naming.ldap.SortKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class LoadMirror
{
    public void load()
    {
        File file = new File("../../config/Distributed/");
        if (file.isDirectory() && file.exists())
        {
            for (File target : file.listFiles())
            {
                if (target.isDirectory())
                {
                    continue;
                }
                String getRemoteHost = Json.readJson(target.getAbsolutePath(),"Remote");
                String getKey = Json.readJson(target.getAbsolutePath(),"Key");

                if (getRemoteHost == null || getKey == null)
                {
                    System.out.println("CONFIG FILE ERROR: "+target.getName());
                    continue;
                }
                else
                {
                    LinwinVOS.executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            LoadMirror.loadReal_Mirror(
                                    getRemoteHost,getKey,LoadMirror.getFrontName(
                                            target.getName()));
                            return 0;
                        }
                    });
                }
            }
        }
        else
        {
            System.out.println("CAN NOT FIND CONFIG DIRECTORY: Distributed");
            System.exit(0);
        }
    }
    public static String getFrontName(String str) {
        try
        {
            return str.substring(0,str.lastIndexOf("."));
        }
        catch (Exception exception){
            return str;
        }
    }
    private static void loadReal_Mirror(String remote,String key,String name) {
        /**
         * First, we must Confirm the remote host and key to connect.
         * Then, the Software can connect and share the data.
         */
        try
        {
            MirrorHost mirrorHost = new MirrorHost();
            mirrorHost.setKey(key);
            mirrorHost.setName(name);
            mirrorHost.setRemote(remote);

            URL url = new URL(remote+"/?Key="+mirrorHost.getMd5Key()+"?Command=test connection");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(3000);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String getMessage = bufferedReader.readLine();

            if (getMessage.equals("Successful Connection"))
            {
                LinwinVOS.mirrorHostHashSet.add(mirrorHost);
            }
            else
            {
                System.out.println("[!] ERROR CONNECT TO TARGET: "+remote);    
            }
        }
        catch (Exception exception)
        {
            System.out.println("Load Mirror Host Error: "+exception.getMessage());
            return;
        }
    }
}
