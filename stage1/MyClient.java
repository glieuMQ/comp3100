import java.net.*;
import java.io.*;

class MyClient {
    public static void main(String[] args) throws IOException {
        // creating a socket to connect to server on localhost:50000
        Socket clientSocket = new Socket("localhost", 50000);

        // initialising input and output streams for socket
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String message, response;

        // send HELO
        message = "HELO";
        out.println(message);
        System.out.println("Client: " + message);
        response = in.readLine(); // receive OK
        System.out.println("Server: " + response);

        // send AUTH <user>
        message = "AUTH garry";
        out.println(message);
        System.out.println("Client: " + message);
        response = in.readLine(); // receive OK
        System.out.println("Server: " + response);

        //String job = "JOBN";
        message = "REDY";
        out.println(message);
        System.out.println("Client: " + message);
        response = in.readLine();
        String job = new String(response);
        System.out.println("Server: " + response);


        int largestServCores = 0;
        int largestServNum = 0;
        String largestServType = "";
        int servID = 0;
        String currServType = "";
        int currServCores = 0;

        while(!job.equals("NONE")){
            if(largestServType.isEmpty()){
                message = "GETS All";
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);

                String[] dataArr = response.split(" ");
                int nRecs = Integer.parseInt(dataArr[1]);

                message = "OK";
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);

                String[] servSpecs = response.split(" ");
                largestServCores = Integer.parseInt(servSpecs[4]);
                largestServType = servSpecs[0];
                largestServNum++;

                for(int i = 0; i < nRecs-1; i++){
                    response = in.readLine();
                    System.out.println("Server: " + response);
                    servSpecs = response.split(" ");
                    
                    currServCores = Integer.parseInt(servSpecs[4]);
                    currServType = servSpecs[0];

                    if(currServCores == largestServCores){
                        largestServNum++;
                    }

                    if(currServCores > largestServCores){
                        largestServCores = currServCores;
                        largestServType = currServType;
                        largestServNum = 1;
                    }
                }

                message = "OK";
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);

                System.out.println("Largest Server Info: " + largestServCores + " " + largestServNum + " " + largestServType);
            }

            String[] jobDetails = job.split(" ");
            
            if(servID >= largestServNum){
                servID = 0;
            }

            if(jobDetails[0].equals("JOBN")){
                message = "SCHD " + jobDetails[2] + " " + largestServType + " " + servID;
                servID++;
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);
            }

            message = "REDY";
            out.println(message);
            System.out.println("Client: " + message);
            response = in.readLine();
            job = new String(response);
            System.out.println("Server: " + response + "\n---------------------------------------");
        }

/*
        int nCores = 0;
        int nServs = 0;
        String largestServType = "";
        String[] servSpecs;
        int counter = 0; // counter used for round-robin job scheduling

        while(!job.equals("NONE")){
            // determine largest server type and number available
            if (largestServType.isEmpty()){
                message = "GETS All";
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);

                String[] servInfo = response.split(" ");
                int nRecs = Integer.parseInt(servInfo[1]);

                message = "OK";
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);

                for(int i = 0; i < nRecs-1; i++) {
                    servSpecs = response.split(" ");

                    if(Integer.parseInt(servSpecs[4]) > nCores){
                        nCores = Integer.parseInt(servSpecs[4]);
                        largestServType = servSpecs[0];
                        nServs = 1;
                    }

                    if(Integer.parseInt(servSpecs[4]) == nCores){
                        nServs++;
                    }

                    message = "OK";
                    out.println(message);
                    System.out.println("Client: " + message);
                    response = in.readLine();
                    System.out.println("Server: " + response);
                }
            }

            String[] jobDetails = job.split(" ");
            
            if(counter >= nServs){
                counter = 0;
            }

            if(jobDetails[0].equals("JOBN")){
                message = "SCHD " + jobDetails[2] + " " + largestServType + " " + counter;
                counter++;
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);
            }

            message = "REDY";
            out.println(message);
            System.out.println("Client: " + message);
            response = in.readLine();
            job = new String(response);
            System.out.println("Server: " + response + "\n---------------------------");

            
            //System.out.println(job);
        }
*/

        message = "QUIT";
        out.println(message);
        System.out.println("Client: " + message);
        response = in.readLine();
        System.out.println("Server: " + response);

        in.close();
        out.close();
        clientSocket.close();
    }
}
