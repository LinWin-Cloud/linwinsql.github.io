
var _md5 = require("./Md5");
function setRemote(remote) {
    /**
     * Remote Value = Url + Port || Or only use url;
     */
    this.remote = remote;
}
function setLoginUser(user) {
    this.user = user;
}
function setLoginPasswd(passwd){
    this.passwd = _md5._md5(passwd);
}
function run(command) {
    //console.log(this.user+" "+this.passwd+" "+this.command);
    var http = require('http');
    try{
        http.get(this.remote+"/?Logon="+this.user+"?Passwd="+this.passwd+"?Command="+command, function(res) {
            var data = '';
            var mes = "";
            res.setEncoding("utf8");
            res.on('data', function(chunk) {
                data += chunk;
            }).on('end', function() {
                var s = data.split("\n");
                for (var i = 0 ; i < s.length ; i++) {
                    if (s[i] == "") {
                        break;
                    }
                    console.log(" -- "+s[i]);
                }
            }).on('err', function () {
                return false;
            });
        });
        return true;
    }catch (err) {
        console.log(err);
        return false;
    }
}
function createData(name,value,note,database) {
    run("create data '"+name+"' setting('"+value+"','"+note+"') in "+database);
}
function createDatabase(name) {
    run("create database '"+name+"'");
}
function deleteData(name,database) {
    run("");
}