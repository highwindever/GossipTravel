/*
 * The GossipCalculateThread is used to calculate the shortest travel time
 * for a rumor to travel from the gossiper to the victim.
 * 
 */



package edu.columbia.fengzhou;

import org.json.JSONException;
import org.json.JSONObject;

public class GossipCalculateThread extends Thread{
	int rumorID;
	GossipVertex gossiper;
	GossipVertex victim;
	GossipGraph gg;
	JSONObject outputJson;
	
	public GossipCalculateThread(int rumorID,GossipVertex gossiper, GossipVertex victim, GossipGraph gg, JSONObject outputJson){
		this.rumorID = rumorID;
		this.gossiper = gossiper;
		this.victim = victim;
		this.gg = gg;
		this.outputJson = outputJson;
	}
	
	public void run(){
		//calculate rumor travel time and calculation time
		long startTime = System.nanoTime();
		GossipTravelTimeCalculation gttc = new GossipTravelTimeCalculation(gg);
		int rumorTravelTime = gttc.calculateGossipTravelTime(gossiper, victim);
		long stopTime = System.nanoTime();
	    long calculationTime = (stopTime - startTime)/1000000;
	    
	    try {
	    	//create the result json
	    	JSONObject json = new JSONObject();
			json.put("Rumor ID", rumorID);
			json.put("Rumor Travel Time", rumorTravelTime);
		    json.put("Calculation Time", calculationTime);
		    //add the result json to an output json array
		    synchronized(outputJson){
		    	outputJson.accumulate("Result", json);
		    }
			
			//System.out.print("Thread "+rumorID +" :" +json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
	}
	
}
