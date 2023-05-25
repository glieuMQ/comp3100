/*
 * AUTHOR: Garry Lieu
 * SID: 46629297
 * DATE: 1/4/2023
 */

import java.net.*;
import java.io.*;

class MyClient {
    public static void main(String[] args) throws IOException {
        // creating a socket to connect to server on localhost:50000
        Socket clientSocket = new Socket("localhost", 50000);

        // initialising input and output streams for socket
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // VARIABLES ---------------------------------------------------------------------------------------
        String send, receive; // stores messages to and responses from the client and server
        int largestServCores = 0; // number of cores in largest server
        int largestServNum = 0; // quantity of the largest server type
        String largestServType = ""; // stores largest server type
        int servID = 0; // counter variable used to perform the round robin
        String currServType = ""; // variables used for searching
        int currServCores = 0;
        String[][] serverList; // holds specs of each server -> not used here. More useful for stage 2

        // INITIAL HANDSHAKE --------------------------------------------------------------------------------
        // send HELO
         // receive OK
        receive = sendMessage("HELO", in , out);

        // send AUTH <user>
        // receive OK
        receive = sendMessage("AUTH garry", in , out);

        // send REDY
        receive = sendMessage("REDY", in , out);
        String job = new String(receive); // storing response to REDY so that jobs can be scheduled later on

        while(!job.equals("NONE")){
            String[] jobDetails = job.split(" ");

            // schedule job if received JOBN after REDY
            if(jobDetails[0].equals("JOBN")){
                // receive DATA nRecs recLen
                receive = sendMessage("GETS Available " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6], in , out);
                
                String[] dataArr = receive.split(" ");
                int nRecs = Integer.parseInt(dataArr[1]);

                if(nRecs == 0){
                    receive = sendMessage("OK", in , out);
                    receive = sendMessage("GETS Capable " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6], in , out);
                    dataArr = receive.split(" ");
                    nRecs = Integer.parseInt(dataArr[1]);
                }

                // send OK for DATA
                // receive record about first server
                receive = sendMessage("OK", in , out);

                System.out.println("Server: " + receive);
                String servCap = new String(receive);
                String[] servDetails = servCap.split(" ");

                // retrieving records for each server
                for(int i = 1; i < nRecs; i++){
                    receive = in.readLine();
                    //System.out.println("Server: " + receive);
                }

                //send OK after receiving last record
                // receive .
                receive = sendMessage("OK", in , out);

                // scheduling job
                receive = sendMessage("SCHD " + jobDetails[2] + " " + servDetails[0] + " " + servDetails[1], in , out);
            }

            // send REDY for next job
            // receive next event
            receive = sendMessage("REDY", in , out);
            job = new String(receive);
        }

        // TERMINATING CLIENT-SERVER INTERACTION ----------------------------------------------------------
        // send QUIT
        receive = sendMessage("QUIT", in , out);

        // closing input/output streams and socket
        in.close();
        out.close();
        clientSocket.close();
    }

    public static String sendMessage(String message, BufferedReader inputStream, PrintWriter outputStream){
        String response = "";
        outputStream.println(message);
        try{
            response = inputStream.readLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
