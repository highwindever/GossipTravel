/*
 * A GossipServerThread is used to process a client request by calculating
 * the result for a set of Json strings.
 * 
 */



package edu.columbia.fengzhou;

import static org.junit.Assert.assertTrue;

import java.io.*; 
import java.net.*; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GossipServerThread extends Thread { 
	Socket clientRequest; 

	BufferedReader input; 
	PrintWriter output; 
	GossipGraph gg;
	
	public GossipServerThread(Socket s) {
		this.clientRequest = s;
		InputStreamReader reader;
		OutputStreamWriter writer;
		gg = new GossipGraph();
		String fileName = "Programming Challenge.xlsx";
		new GossipChainBuilder(fileName, gg);// build the gossip graph in a server thread
		
		//Test the graph
		ArrayList<GossipEdge> testEdges = gg.getEdges();
		for(GossipEdge e:testEdges){
			assertTrue(gg.getVertices().keySet().contains(e.getTalker().getName()));//the talker in an edge should be a vertex of the graph 
			assertTrue(gg.getVertices().keySet().contains(e.getListener().getName()));//the listener in an edge should be a vertex of the graph
			assertTrue(e.getChatTime()>=8);//the minimal chat time is 8 seconds according to background of this programming challenge
		}
		
		HashMap<String,GossipVertex> testVertices = gg.getVertices();
		for( Map.Entry<String,GossipVertex> entry:testVertices.entrySet()){
			int size =entry.getValue().getAsListenerEdges().size();
			assertTrue(size>=1&&size<=10);//talkers can have anywhere between 1 and 10 trusted Listeners
		}
		
		
		//prepare for input and output
		try {
			reader = new InputStreamReader(clientRequest.getInputStream());
			writer = new OutputStreamWriter(clientRequest.getOutputStream());
			input = new BufferedReader(reader);
			output = new PrintWriter(writer, true);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

	public void run() {
		
		String str;

		try {
			//Parse the input
			
			str = input.readLine();
			System.out.println(str);
			if(str.startsWith("{\"Rumor\"")==false){
				output.println("Please send data in Correct format:Put the set of json Strings in a field named \"Rumor\".");
			}
			else{
				//Process the input and get the output result on multiple threads
				JSONObject outputJson = new JSONObject();
				calculateWithMultipleThreads(str,outputJson);
				output.println(outputJson);
			}
			output.close();
			input.close();
			clientRequest.close();

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void calculateWithMultipleThreads(String str, JSONObject outputJson){
		try {
			JSONObject inputJson = new JSONObject(str);
			JSONArray rumors = inputJson.getJSONArray("Rumor");
			int size = rumors.length();
			GossipCalculateThread[] gct = new GossipCalculateThread[size];
			//calculate for each rumor on concurrent threads 
			for (int i = 0; i < size; i++) {
				int rumorID = rumors.getJSONObject(i).getInt("Rumor ID");
				GossipVertex gossiper = gg.getVertex(rumors.getJSONObject(i)
						.getString("Gossiper"));
				GossipVertex victim = gg.getVertex(rumors.getJSONObject(i)
						.getString("Victim"));

				gct[i] = new GossipCalculateThread(rumorID, gossiper, victim,
						gg, outputJson);
				gct[i].start();
			}
			//wait for each thread to finish
			for (int i = 0; i < size; i++) {
				gct[i].join();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

