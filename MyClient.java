/*
 * AUTHOR: Garry Lieu
 * SID: 46629297
 * DATE: 1/4/2023
 */

import java.net.*;
import java.util.*;
import java.io.*;

class MyClient {
    static BufferedReader in;
    static PrintWriter out;
    static String receive;
    static ArrayList<String[]> serverList;
    static Iterator<String[]> iter;
    static String[] currServer;
    static int j = 0;
    static String[][] serverList2;

    public static void main(String[] args) throws IOException {
        // creating a socket to connect to server on localhost:50000
        Socket clientSocket = new Socket("localhost", 50000);

        // initialising input and output streams for socket
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        // INITIAL HANDSHAKE --------------------------------------------------------------------------------
        receive = sendMessage("HELO"); // send HELO. receive OK
        receive = sendMessage("AUTH garry"); // send AUTH <user>. receive OK


        receive = sendMessage("REDY"); // send REDY. receive first event.
        String event = new String(receive); // storing response to REDY so that jobs can be scheduled later on

        while(!event.equals("NONE")){
            // schedule job following first fit algorithm.
            ffsched(event);
            // send REDY for next event. receive next event and store it
            receive = sendMessage("REDY");
            event = new String(receive);
        }

        // TERMINATING CLIENT-SERVER INTERACTION ----------------------------------------------------------
        // send QUIT
        receive = sendMessage("QUIT");

        // closing input/output streams and socket
        in.close();
        out.close();
        clientSocket.close();
    }

    public static String sendMessage(String message){
        String response = "";
        out.println(message);
        try{
            response = in.readLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void ffsched(String job){
        String[] jobDetails = job.split(" ");
        // schedule job if received JOBN after REDY
        if(jobDetails[0].equals("JOBN")){
            // receive DATA nRecs recLen
            receive = sendMessage("GETS Available " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6]);
                
            String[] dataArr = receive.split(" ");
            int nRecs = Integer.parseInt(dataArr[1]);
            
            // if no servers are available, schedule job to first capable one instead
            if(nRecs == 0){
                receive = sendMessage("OK");
                receive = sendMessage("GETS Capable " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6]);
                dataArr = receive.split(" ");
                nRecs = Integer.parseInt(dataArr[1]);
            }

            // send OK for DATA. receive record about first server
            receive = sendMessage("OK");

            String servCap = new String(receive);
            String[] servDetails = servCap.split(" ");

            // retrieving records for each server
            for(int i = 1; i < nRecs; i++){
                try{
                    receive = in.readLine();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }

            //send OK after receiving last record. receive .
            receive = sendMessage("OK");

            // scheduling job
            receive = sendMessage("SCHD " + jobDetails[2] + " " + servDetails[0] + " " + servDetails[1]);
        }
    }
}