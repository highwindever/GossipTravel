package edu.columbia.fengzhou;

import java.util.ArrayList;
import java.util.HashMap;

public class GossipGraph {
	private HashMap<String,GossipVertex> gossipVerticies;
	private ArrayList<GossipEdge> gossipEdges;
	
	public GossipGraph(){
		gossipVerticies = new HashMap<String,GossipVertex>();
		gossipEdges = new ArrayList<GossipEdge>();
	}
	
	public boolean addVertex(GossipVertex v){
		if(gossipVerticies.values().contains(v)==false){
			gossipVerticies.put(v.getName(),v);
			return true;
		}
		return false;
	}
	
	public GossipVertex getVertex(String name){
		return this.gossipVerticies.get(name);
	}
	
	public HashMap<String,GossipVertex> getVertices(){
		return this.gossipVerticies;
	}
	
	public ArrayList<GossipEdge> getEdges(){
		return this.gossipEdges;
	}
	
	
	public boolean addEdgeAndVertex(String talkerName,String listenerName, int chatTime){
		GossipVertex talker;
		if(gossipVerticies.keySet().contains(talkerName)==false) {
			talker= new GossipVertex(talkerName);
			gossipVerticies.put(talkerName,talker);
		}else {
			talker = gossipVerticies.get(talkerName);
		}
		
		GossipVertex listener;
		if(gossipVerticies.keySet().contains(listenerName)==false) {
			listener = new GossipVertex(listenerName);
			gossipVerticies.put(listenerName,listener);
		}else {
			listener = gossipVerticies.get(listenerName);
		}
		
		GossipEdge e = new GossipEdge(talker, listener,chatTime);
		if(talker.findEdge(listener)!=null)
			return false;
		else{
			talker.addEge(e);
			listener.addEge(e);
			gossipEdges.add(e);
			return true;
		}
		
		
	}
	
	  public String toString() {
		    StringBuffer tmp = new StringBuffer("Graph{\n");
		    for (GossipVertex v : gossipVerticies.values())
		      tmp.append(v);
		    tmp.append('}');
		    return tmp.toString();
	  }

		
}
