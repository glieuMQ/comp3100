/*
 * AUTHOR: Garry Lieu
 * SID: 46629297
 * DATE: 29/05/2023
 */

import java.net.*;
import java.util.*;
import java.io.*;

class MyClient {
    static BufferedReader in;
    static PrintWriter out;
    static String receive;

    public static void main(String[] args) throws IOException {
        // creating a socket to connect to server on localhost:50000
        Socket clientSocket = new Socket("localhost", 50000);

        // initialising input and output streams for socket
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        // INITIAL HANDSHAKE --------------------------------------------------------------------------------
        receive = sendMessage("HELO"); // send HELO and receive OK
        receive = sendMessage("AUTH garry"); // send AUTH <user> and receive OK

        // SCHEDULING JOBS ----------------------------------------------------------------------------------
        receive = sendMessage("REDY"); // send REDY and receive first event
        String event = new String(receive); // storing response to REDY so that jobs can be scheduled later on

        while(!event.equals("NONE")){
            // schedule normal jobs following first fit algorithm
            ffsched(event);
            // send REDY, receive next event and store it
            receive = sendMessage("REDY");
            event = new String(receive);
        }

        // TERMINATING CLIENT-SERVER INTERACTION ----------------------------------------------------------
        receive = sendMessage("QUIT");

        // closing input/output streams and socket
        in.close();
        out.close();
        clientSocket.close();
    }

    /*
     * function: send and receive messages to and from ds-sim
     * input: message to ds-sim
     * result: receive response from ds-sim
     */
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

    /*
     * function: schedule job using the first fit algorithm
     * input: response to READY
     * result: if response is of type JOBN, the job is scheduled to the first available and sufficiently resourced server found
     */
    public static void ffsched(String job){
        String[] jobDetails = job.split(" ");

        // checking that a normal job was passed to scheduler
        if(jobDetails[0].equals("JOBN")){
            // determining number of available and sufficiently resourced servers
            receive = sendMessage("GETS Available " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6]);
            String[] dataArr = receive.split(" ");
            int nRecs = Integer.parseInt(dataArr[1]);
            
            // if no servers are available, find first capable one instead
            if(nRecs == 0){
                receive = sendMessage("OK");
                receive = sendMessage("GETS Capable " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6]);
                dataArr = receive.split(" ");
                nRecs = Integer.parseInt(dataArr[1]);
            }

            // send OK and receive record about first server
            receive = sendMessage("OK");

            String servFirst = new String(receive);
            String[] servDetails = servFirst.split(" ");

            // loop through remaining records since we're only interested in first
            for(int i = 1; i < nRecs; i++){
                try{
                    receive = in.readLine();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }

            //send OK after receiving last record and receive "."
            receive = sendMessage("OK");

            // schedule job to selected server
            receive = sendMessage("SCHD " + jobDetails[2] + " " + servDetails[0] + " " + servDetails[1]);
        }
    }
}