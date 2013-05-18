package progin5socket;

import java.io.*;
import java.net.*;

public class Client2 {
    public static void main(String[] args) throws IOException {
        
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String userInput;
        
        String serverHostname = "127.0.0.1";

        if (args.length > 0) { 
            serverHostname = args[0];
        }
        
        System.out.println ("Attemping to connect to host " + serverHostname + " on port 2222.");

        try {
            echoSocket = new Socket(serverHostname, 2222);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host : " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + serverHostname);
            System.exit(1);
        }

	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.println ("*** Type Message (\"logout;\" to quit)");
        System.out.print("User input : ");
	while ((userInput = stdIn.readLine()) != null) 
        {
            //Send message to server
	    out.println(userInput);
            System.out.println("Response : " + in.readLine());
            
            String[] req_parts = userInput.split(";");
            
            if (req_parts[0].equals("logout")) {
                break;
            }
            else 
            {
                System.out.print("User input : ");
            }
	}

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
    }
}
