package edu.columbia.fengzhou;

public class GossipEdge {
	private GossipVertex talker, listener;
	private int chatTime;
	
	public GossipEdge(GossipVertex talker, GossipVertex listener, int chatTime){
		this.talker = talker;
		this.listener = listener;
		this.chatTime = chatTime;
	}
	
	public GossipVertex getTalker(){
		return this.talker;
	}
	
	public GossipVertex getListener(){
		return this.listener;
	}
	
	public int getChatTime(){
		return this.chatTime;
	}
	
	public String toString() {
	    StringBuffer tmp = new StringBuffer("Edge[from: ");
	    tmp.append(talker.getName());
	    tmp.append(",to: ");
	    tmp.append(listener.getName());
	    tmp.append(", chatTime: ");
	    tmp.append(chatTime);
	    tmp.append("]");
	    return tmp.toString();
	  }
}
