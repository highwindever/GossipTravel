package edu.columbia.fengzhou;

import org.json.JSONObject;

public class GossipTravelTimeCalculation {
	public static void main(String[] args){
		GossipGraph gg = new GossipGraph();
		String fileName ="Programming Challenge.xlsx";
		new GossipChainBuilder(fileName, gg);
		//System.out.println(gg);
		JSONObject json = new JSONObject();
		new GossipInputFormatter(fileName,json);
		//System.out.println(json);
	}
	


	
	
}
