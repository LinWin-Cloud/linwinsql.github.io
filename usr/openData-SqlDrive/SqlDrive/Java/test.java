import sqlDrive.SqlDo;
import sqlDrive.SqlDrive;

public class test {
    public static void main(String[] args) {
        SqlDrive sqlDrive = new SqlDrive("http://127.0.0.1:8888");
        sqlDrive.setPasswd("123456");
        sqlDrive.setUserName("root");
        SqlDo sqlDo = new SqlDo(sqlDrive);
        long s = System.currentTimeMillis();
        System.out.println(sqlDo.findData("d").length);
        long e = System.currentTimeMillis();
        System.out.println(e-s);
    }
}
