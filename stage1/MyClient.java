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
        send = "HELO";
        out.println(send);
        System.out.println("Client: " + send);
        receive = in.readLine(); // receive OK
        System.out.println("Server: " + receive);

        // send AUTH <user>
        send = "AUTH garry";
        out.println(send);
        System.out.println("Client: " + send);
        receive = in.readLine(); // receive OK
        System.out.println("Server: " + receive);

        // send REDY
        send = "REDY";
        out.println(send);
        System.out.println("Client: " + send);
        receive = in.readLine();
        String job = new String(receive); // storing response to REDY so that jobs can be scheduled later on
        System.out.println("Server: " + receive);

        // DETERMINING LARGEST SERVER TYPE AND QUANTITY ----------------------------------------------------
        // send GETS to query server states
        send = "GETS All";
        out.println(send);
        System.out.println("Client: " + send);
        receive = in.readLine(); // receive DATA nRecs recLen
        System.out.println("Server: " + receive);

        String[] dataArr = receive.split(" ");
        int nRecs = Integer.parseInt(dataArr[1]);

        // send OK for DATA
        send = "OK";
        out.println(send);
        System.out.println("Client: " + send);
        receive = in.readLine(); // receive record about first server
        System.out.println("Server: " + receive);

        // extracting number of cores and server type, and starting count
        String[] servSpecs = receive.split(" ");
        serverList = new String[nRecs][servSpecs.length];
        serverList[0] = servSpecs;
        largestServCores = Integer.parseInt(servSpecs[4]);
        largestServType = servSpecs[0]; // storing server information -> not used in stage 1
        largestServNum++;

        // retrieving records for each server
        // linear search since records are retrieved one-by-one anyway
        for(int i = 1; i < nRecs; i++){
            receive = in.readLine();
            System.out.println("Server: " + receive);
            servSpecs = receive.split(" ");
            serverList[i] = servSpecs; // storing serving information -> not used in stage 1

            currServCores = Integer.parseInt(servSpecs[4]);
            currServType = servSpecs[0];

            // increasing count if number of cores in current server match largestServCores and it is of the same type
            if(currServCores == largestServCores && currServType.equals(largestServType)){
                largestServNum++;
            }

            // resetting count if a larger server type is encountered
            if(currServCores > largestServCores){
                largestServCores = currServCores;
                largestServType = currServType;
                largestServNum = 1;
            }
        }

        // send OK after receiving last record
        send = "OK";
        out.println(send);
        System.out.println("Client: " + send);
        receive = in.readLine(); // receive .
        System.out.println("Server: " + receive);
        //System.out.println("Largest Server Info: " + largestServCores + " " + largestServNum + " " + largestServType);

        // SCHEDULING JOBS USING LRR --------------------------------------------------------------------

        while(!job.equals("NONE")){
            String[] jobDetails = job.split(" ");
            
            // cycling back to first server when last has been assigned a job
            if(servID >= largestServNum){
                servID = 0;
            }

            // schedule job if received JOBN after REDY
            if(jobDetails[0].equals("JOBN")){
                send = "SCHD " + jobDetails[2] + " " + largestServType + " " + servID;
                servID++; // next server of the largest type
                out.println(send);
                System.out.println("Client: " + send);
                receive = in.readLine();
                System.out.println("Server: " + receive);
            }

            // send REDY for next job
            send = "REDY";
            out.println(send);
            System.out.println("Client: " + send);
            receive = in.readLine(); // receive OK
            job = new String(receive);
            System.out.println("Server: " + receive + "\n---------------------------------------");
        }

        // TERMINATING CLIENT-SERVER INTERACTION ----------------------------------------------------------
        // send QUIT
        send = "QUIT";
        out.println(send);
        System.out.println("Client: " + send);
        receive = in.readLine(); // receive QUIT
        System.out.println("Server: " + receive);

        // closing input/output streams and socket
        in.close();
        out.close();
        clientSocket.close();
    }
}
