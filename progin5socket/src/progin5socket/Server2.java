package progin5socket;

import java.io.*; 
import java.net.*;

public class Server2 extends Thread
{ 
    protected Socket clientSocket;
    
    private Server2 (Socket clientSoc)
    {
        clientSocket = clientSoc;
        start(); //start new thread
    }
    
    public static void main(String[] args) throws IOException 
    { 
        ServerSocket serverSocket = null; 

        try { 
            serverSocket = new ServerSocket(2222); 
            System.out.println ("Connection Socket Created on port 2222");
            try { 
                while (true)
                {
                    System.out.println ("Waiting for Connection");
                    new Server2 (serverSocket.accept()); 
                }
            } 
            catch (IOException e) 
            { 
                 System.err.println("Accept failed."); 
                 System.exit(1); 
            } 
        } 
        catch (IOException e) 
        { 
            System.err.println("Could not listen on port: 2222."); 
            System.exit(1); 
        } 
        finally
        {
            try {
                 serverSocket.close(); 
                }
            catch (IOException e)
                { 
                 System.err.println("Could not close port: 2222."); 
                 System.exit(1); 
                } 
        }
    }

    public void run()
    {
        String username = "";
        RepositoryConnection RC = new RepositoryConnection();
        System.out.println ("New Communication Thread Started");
        
        try { 
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true); 
            BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream())); 

            String inputLine; 
            
            while ((inputLine = in.readLine()) != null) 
            { 
                 System.out.println ("Query from " + username + " : " + inputLine); 
                 
                 String[] req_parts = inputLine.split(";");
                 
                 if ( (req_parts[0].equals("logout")) && (req_parts.length == 2) ) {                    
                    out.println(req_parts[1] + " has been logged out.");
                    username = "";
                    break; 
                 } 
                 else if ( (req_parts[0].equals("login")) && (req_parts.length == 3) ) {
                     //login;username;password
                     boolean flag = RC.login(req_parts[1], req_parts[2]);
                     if (flag) {
                         username = req_parts[1];
                         out.println("success");
                     }
                     else {                         
                         out.println("failed");
                     }
                 }
                 else if ( (req_parts[0].equals("check")) && (req_parts.length == 3) ) {
                    //check;id_task;timestamp
                    RC.changeStatus(req_parts[1], 1);
                    out.println("success");
                 }
                 else if ( (req_parts[0].equals("uncheck"))  && (req_parts.length == 3) ){
                    //uncheck;id_task;timestamp
                    RC.changeStatus(req_parts[1], 0);
                    out.println("success");
                 }
                 else if ( (req_parts[0].equals("getlist"))  && (req_parts.length == 2) ) {
                     if (!"".equals(req_parts[1])) {
                         out.println(RC.getList(req_parts[1]));
                     }
                     else {
                         out.println("Please log in first");
                     }
                 }
                 else {
                     out.println("Command not found or the parameters are wrong");
                 }
            } 

            out.close(); 
            in.close(); 
            clientSocket.close(); 
        } 
        catch (IOException e) 
        { 
            System.err.println("Problem with Communication Server");
        } 
    }
} 