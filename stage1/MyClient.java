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

        while(!response.equals("NONE")){
            message = "REDY";
            out.println(message);
            System.out.println("Client: " + message);
            response = in.readLine();
            System.out.println("Server: " + response);

            String job = new String(response);
            System.out.println(job);

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

            int nCores = 0;
            String sType = "";
            String[] servSpecs;

            for(int i = 0; i < nRecs; ++i) {
                servSpecs = response.split(" ");
                if(Integer.parseInt(servSpecs[4]) >= nCores){
                    nCores = Integer.parseInt(servSpecs[4]);
                    sType = servSpecs[0];
                }
                message = "OK";
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);
            }

            System.out.println("Checkpoint: " + nCores + ", " + sType);

            message = "OK";
            out.println(message);
            System.out.println("Client: " + message);
            response = in.readLine();
            System.out.println("Server: " + response);

            String[] jobArr = job.split(" ");
            System.out.println(job);
            if(jobArr[0].equals("JOBN")){
                message = "SCHD " + jobArr[2] + " " + sType + " 0";
                out.println(message);
                System.out.println("Client: " + message);
                response = in.readLine();
                System.out.println("Server: " + response);
            }
        }

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
