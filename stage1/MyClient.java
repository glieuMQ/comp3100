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

        while(!response.equals(NONE)){
            message = "REDY";
            out.println(message);
            System.out.println("Client: " + message);
            response = in.readLine();
            System.out.println("Server: " + response);

            message = "GETS All";
            out.printLn(message);
            System.out.println("Client: " + message);
            response = in.readLine();
            System.out.println("Server: " + response);

            String[] servInfo = response.split(" ");
            int nRecs = servInfo[1];

            message = "OK";
            out.printLn(message);
            System.out.println("Client: " + message);
            response = in.readLine();
            System.out.println("Server: " + response);

            int nCores = 0;
            String[] servSpecs;

            for(int i = 0; i < nRecs; ++i) {
                servSpecs = response.split(" ");

            }
            /*
            if(){

            }
            */
        }


  /*
        out.println("REDY");
	response = in.readLine();
	System.out.println("Server: " + response);

	out.println("GETS All");
	response = in.readLine();
	System.out.println("Server: " + response);

        out.println("OK");
        response = in.readLine();
        System.out.println("Server: " + response);

        String[] strArr = response.split(" ");
        String nCore = strArr[4];
        String sType = strArr[0];

        while(!response.equals(".")){
        	strArr = response.split(" ");
        	if(strArr[4].compareTo(nCore) >= 0){
        		nCore = strArr[4];
        		sType = strArr[0];
        	}
        	out.println("OK");
        	System.out.println(nCore);
        	response = in.readLine();
        	System.out.println("Server: " + response);
        }

        System.out.println(sType);

        for(int i = 0; i < 10; i++) {
        	out.println("REDY");
		response = in.readLine();
		System.out.println("Server: " + response);

		out.println("GETS All");
		response = in.readLine();
		System.out.println("Server: " + response);

        	out.println("OK");
        	response = in.readLine();
        	System.out.println("Server: " + response);

        	out.println("OK");
        	response = in.readLine();
        	System.out.println("Server: " + response);

        	out.println("SCHD "+ i + " " + sType + " 0");
		response = in.readLine();
		System.out.println("Server: " + response);
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
