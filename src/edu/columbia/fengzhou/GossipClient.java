/*
 * GossipClient is used to send a set of input json strings
 * to the GossipServer and receive the output from the server.
 */

package edu.columbia.fengzhou;


/*
 * This class calls method in GossipInputFormatter to turn the data from the 
 * xlsx file to required request JSON string. Then send it to server and receives
 * the result from the server
 */


import java.io.*;
import java.net.*;

import org.json.JSONObject;

public class GossipClient {
	public static void main(String[] args ) throws UnknownHostException, IOException  {
		//specify the address and port of the srver
		Socket s = new Socket("127.0.0.1", 6666);
		//format the data in the xlsx file as a set of json strings
		JSONObject inputJson = new JSONObject();
		String fileName = "Programming Challenge.xlsx";
		new GossipInputFormatter(fileName, inputJson);

		BufferedReader in = new BufferedReader (new InputStreamReader(s.getInputStream()));
		PrintWriter os = new PrintWriter(s.getOutputStream(),true);

		try {
			//send the request string
			os.println(inputJson);
			//get the response string
			System.out.println("Response from Server:");
			System.out.println(in.readLine());
			os.close();
			in.close();
			s.close();

		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
}

