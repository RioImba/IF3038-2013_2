package progin5socket;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client2 {

    String username;
    Socket echoSocket = null;

    public Client2() {
        username = "";
    }

    public void connectToServer(String serverHostname) {
        try {
            this.echoSocket = new Socket(serverHostname, 2222);
            System.out.println("Attemping to connect to host " + serverHostname + " on port 2222.");
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date current = new Date();
        String timestamp = sdf.format(current);
        return timestamp;
    }

    public static void main(String[] args) throws IOException {
        Client2 client = new Client2();

        PrintWriter out = null;
        BufferedReader in = null;
        String userInput;
        String serverHostname = "127.0.0.1";

        while (true) {

            try {

                client.connectToServer(serverHostname);

                out = new PrintWriter(client.echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.echoSocket.getInputStream()));

                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("*** Type Message (\"logout;\" to quit)");
                System.out.print(client.username + " query : ");
                while ((userInput = stdIn.readLine()) != null) {
                    //Add Timestamp
                    String[] temp_part = userInput.split(";");
                    if ((temp_part[0].equals("check")) || (temp_part[0].equals("uncheck"))) {
                        userInput = userInput + ";" + client.getTimestamp();
                    }

                    //Send message to server
                    out.println(userInput);
                    String response = in.readLine();

                    System.out.println("Response : " + response);

                    String[] req_parts = userInput.split(";");

                    if ((req_parts[0].equals("logout")) && (req_parts.length == 2)) {
                        break;
                    } else if ((req_parts[0].equals("login")) && (req_parts.length == 3)) {
                        //login;username;password
                        switch (response) {
                            case "success":
                                client.username = req_parts[1];
                                System.out.println("You have been successfully logged in");
                                break;
                            case "failed":
                                System.out.println("Username or password is wrong");
                                break;
                        }
                    } else if ((req_parts[0].equals("check")) && (req_parts.length == 3)) {
                        //check;id_task;timestamp                
                        //check if response = success / failed
                    } else if ((req_parts[0].equals("uncheck")) && (req_parts.length == 3)) {
                        //uncheck;id_task;timestamp                
                        //check if response = success / failed
                    } else if ((req_parts[0].equals("getlist")) && (req_parts.length == 1)) {
                        //getlist
                        String[] task_list = response.split("@");

                        for (int j = 0; j < task_list.length; j++) {
                            System.out.println(task_list[j]);
                            String[] task = task_list[j].split(";");

                            System.out.println("Task id : " + task[1]);
                            System.out.println("Task name : " + task[2]);
                            String[] assignee = task[3].split("#");
                            System.out.println("Assignee : ");
                            for (int i = 0; i < assignee.length; i++) {
                                System.out.println("  - " + assignee[i]);
                            }

                            String[] tag = task[4].split("#");
                            System.out.println("Tag : ");
                            for (int i = 0; i < tag.length; i++) {
                                System.out.println("  - " + tag[i]);
                            }

                            System.out.println("Category name : " + task[5]);
                            System.out.println("Status : " + task[6]);
                            System.out.println("");
                        }
                    }

                    System.out.print(client.username + " query : ");
                }
                out.close();
                in.close();
                stdIn.close();

            } catch (UnknownHostException e) {
                System.err.println("Unknown host : " + serverHostname);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: " + serverHostname);
            } finally {
                client.echoSocket.close();
            }
        }
    }
}
