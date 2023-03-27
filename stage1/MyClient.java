import java.net.*;  
import java.io.*;  
/*class MyClient{  
	public static void main(String args[])throws Exception{  
		Socket s = new Socket("localhost", 50000);  
		DataInputStream din = new DataInputStream(s.getInputStream());  
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());  
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
		
		String str = "", str2 = "";  
		while(!str.equals("stop")){  
			str = br.readLine();  
			dout.write((str + "\n").getBytes());  
			dout.flush();  
			str2 = din.readLine();  
			System.out.println("Server says: "+str2);  
		}
		
		dout.close();  
		s.close();  
	}
}*/

class MyClient {
    public static void main(String[] args) throws IOException {
    	// creating a socket to connect to server on localhost:50000
        Socket clientSocket = new Socket("localhost", 50000);
        
        // initialising input and output streams for socket
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

	// send HELO
        out.println("HELO");
        String response = in.readLine(); // receive OK
        System.out.println("Server: " + response);

	// send AUTH <user>
        out.println("AUTH garry");
        response = in.readLine(); // receive OK
        System.out.println("Server: " + response);
        
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
        
        out.println("QUIT");
        response = in.readLine();
        System.out.println("Server: " + response);
        
        in.close();
        out.close();
        socket.close();
    }
}
