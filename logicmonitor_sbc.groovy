import com.santaba.agent.groovyapi.expect.Expect;

class BaseTable{
    /*
       base table 
    */
    public headers = []; 
    public data = null;
    public debug = true;

    public void RawTableInput(rawInput){
      this.data = rawInput

      
    }

    public GetValues(){
        
        def x = "";
        def new_data = this.data.split("\n")
        int i;
        int y;
    
        for(i=0; i < new_data.size(); i++){
            try{
            def line = new_data[i].replaceAll("(?=.*?[^\r\n])[\\s]{2,}", " ");
            line = line.split(" ")
            x = x + line[0] + ","+line[value_location[line[0]][0]]+","+line[value_location[line[0]][1]]+"\n"
            }catch(Exception e){
                println e
            }

        }            
          return x;
    }
         
         
         
    public GetJson(){   
        def values = this.GetValues();        
        def json = '{ "status":{'
        values.eachLine{ line ->
          line = line.split(",")
          json = json + '"'+line[0]+'" : {"server":"'+line[1]+'","client": "'+line[2]+'"},'
         
        }
        json = json + "}}"
       return json
    }

      
    }
    

class SipTable extends BaseTable{
  /*
       class based on "show sip reg" command
  */

    def value_location = ["REGISTER":[2,5], 
                      "Retransmissions":[1,4],
                      "100":[2,5],
                      "180":[2,5],
                      "182":[2,5],
                      "183":[2,5],
                      "1xx":[2,5],  
                      "200":[2,5], 
                      "30x":[2,5],
                      "400":[3,6],
                      "401":[2,5],
                      "403":[2,5],
                      "404":[3,6],
                      "405":[3,6],
                      "406":[3,6],
                      "407":[4,7],
                      "408":[3,6],
                      "415":[4,7],
                      "420":[3,6],
                      "480":[2,5],
                      "481":[4,7],
                      "482":[3,6],
                      "483":[4,7],
                      "486":[3,6],
                      "487":[2,5],
                      "488":[3,6],
                      "491":[3,6],
                      "4xx":[3,6],
                      "500":[3,6],
                      "501":[3,6],
                      "502":[3,6],
                      "503":[3,6],
                      "504":[3,6],
                      "513":[4,7],
                      "600":[3,6],
                      "603":[2,5],
                      "606":[3,6],
                      ]
  
}


command = null // << sbc command goes here 
hostname = hostProps.get("system.hostname");
host = hostProps.get("ssh.host");
userid = hostProps.get("ssh.user");
passwd = hostProps.get("ssh.pass");
prompt = host+"> "
SipTable table = new SipTable();
ssh_connection = Expect.open(hostname, userid, passwd,timeout=10000);
ssh_connection.expect(prompt);
ssh_connection.send(command+"\r");
ssh_connection.expect(prompt);
cmd_output = ssh_connection.before();
table.RawTableInput(cmd_output);
println table.GetJson();
ssh_connection.send("exit\n");
ssh_connection.expectClose();
return (0);
