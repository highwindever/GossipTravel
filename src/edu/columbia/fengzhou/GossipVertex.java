/*
 * Each student is modeled as a GossipVertex, it contains the information
 * of the name of the student and the list of GossipEdges when this students 
 * serves as a talker and the list of GossipEdges when the student serves as 
 * a listener.
 */
package edu.columbia.fengzhou;

import java.util.ArrayList;

public class GossipVertex {
	private String name;
	private ArrayList<GossipEdge> asListenerEdges;
	private ArrayList<GossipEdge> asTalkerEdges;
	 
	public GossipVertex(String name){
		this.name = name;
		this.asListenerEdges = new ArrayList<GossipEdge>();
		this.asTalkerEdges = new ArrayList<GossipEdge>();
	}
	
	public String getName(){
		return this.name;
	}
	
	public ArrayList<GossipEdge> getAsListenerEdges(){
		return this.asListenerEdges;
	}
	
	public ArrayList<GossipEdge> getAsTalkerEdges(){
		return this.asTalkerEdges;
	}
	
	// add an egde to either asTalkerEdges or asListenerEdges
	public boolean addEge(GossipEdge e){
		if(e.getTalker()==this)
			asTalkerEdges.add(e);
		else if(e.getListener()==this)
			asListenerEdges.add(e);
		else
			return false;
		return true;
	}
	
	//find an edge with this GossipVertex as a talker and the specified GossipVertex as a listener
	public GossipEdge findEdge(GossipVertex listener){
		for(GossipEdge e:asTalkerEdges){
			if(e.getListener()==listener){
				return e;
			}
		}
		return null;
	}
	
	public String toString() {
	    StringBuffer tmp = new StringBuffer("Vertex(");
	    tmp.append(name);
	    tmp.append("), in:[");
	    for (int i = 0; i < asListenerEdges.size(); i++) {
	      GossipEdge e = asListenerEdges.get(i);
	      if (i > 0)
	        tmp.append(',');
	      tmp.append('(');
	      tmp.append(e.getTalker().name);
	      tmp.append(',');
	      tmp.append(e.getChatTime());
	      tmp.append(')');
	    }
	    tmp.append("], out:[");
	    for (int i = 0; i < asTalkerEdges.size(); i++) {
	      GossipEdge e = asTalkerEdges.get(i);
	      if (i > 0)
	        tmp.append(',');
	      tmp.append('(');
	      tmp.append(e.getListener().name);
	      tmp.append(',');
	      tmp.append(e.getChatTime());
	      tmp.append(')');
	    }
	    tmp.append(']');
	    tmp.append("\n");
	    return tmp.toString();

	  }
	
}
