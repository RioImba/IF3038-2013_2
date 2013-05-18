package progin5socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicholas Rio
 */
public class RepositoryConnection {

    private final String authURL = "http://nicholasrio.ap01.aws.af.cm/rest/authentication";
    private final String taskStatusURL = "http://nicholasrio.ap01.aws.af.cm/sync/taskStatusService.php";
    private final String showTaskURL = "http://nicholasrio.ap01.aws.af.cm/rest/showTaskdong";
    
    public RepositoryConnection() {
    }

    public String getResponseText(String url) {
        try {
            // TODO code application logic here
            StringWriter content = new StringWriter();
            URL repo = new URL(url);
            //URL repo = new URL("http://nicholasrio.ap01.aws.af.cm/sync/taskStatusService.php?idtask=20&checked=0&timestamp=130518090900");
            URLConnection con = repo.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(repo.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                content.write(line);
            }
            in.close();
            content.close();
            String plaintext = content.toString();
            return plaintext;
        } catch (IOException ex) {
            //Logger.getLogger(progin5socket.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date current = new Date();
        String timestamp = sdf.format(current);
        return timestamp;
    }
    
    public boolean login(String username, String password){
        String completeURL = authURL + "?usr=" + username + "&psw=" + password;
        String result = getResponseText(completeURL);
        System.out.println(result);
        if(result.compareToIgnoreCase("1") == 0){
            System.out.println("Login success");
            return true;
        }else{
            System.out.println("Login failed");
            return false;
        }
    }

    public boolean changeStatus(String idTask, int checked) {
        String completeURL = taskStatusURL + "?idtask=" + idTask + "&checked=" + checked + "&timestamp=" + getTimestamp();
        String result = getResponseText(completeURL);
        if (result.equals("1")) {
            System.out.println("Change task status succesfull");
        } else {
            System.out.println("Change task status failed");
        }
        return true;
    }

    public String getList(String username) throws IOException {
        String completeURL = showTaskURL + "?username=" + username + "&q=0";
        String response = getResponseText(completeURL);
        StringWriter content = new StringWriter();
        //list - taskid - taskname - assignee - tag - category - status (seharusnya)
        //System.out.println(response);
        String[] data = response.split("<br>");
        for (String datasplit : data) {
            String[] list = datasplit.split(",");
            System.out.println(list.length);
            if (list.length == 7) {
                content.write("list;");
                content.write(list[3] + ";");//taskid
                content.write(list[0] + ";");//taskname
                content.write(list[6].replaceAll(";", "#") + ";");//assignee
                content.write(list[5].replaceAll(";", "#") + ";");//tags
                content.write("dummy" + ";");//category
                content.write(list[2]);//status
                content.write("@");//splitter
            }
        }
        content.close();
        String result = content.toString();
        System.out.println(result);
        return result;
    }
}
