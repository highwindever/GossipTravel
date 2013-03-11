package edu.columbia.fengzhou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;



public class GossipTravelTimeCalculationWithPriorityQueue {

  private final HashMap<String,GossipVertex> vertices;
  private final ArrayList<GossipEdge> edges;
  private Set<GossipVertex> settledVertices;
  private PriorityQueue<GossipVertex> unSettledVertices;
  private Map<GossipVertex, GossipVertex> predecessors;
  private Map<GossipVertex, Integer> gossipTravelTime;

  public GossipTravelTimeCalculationWithPriorityQueue(GossipGraph gg) {
    // Create a copy of the array so that we can operate on this array
    this.vertices = new HashMap<String,GossipVertex>(gg.getVertices());
    this.edges = new ArrayList<GossipEdge>(gg.getEdges());
  }

  public int calculateGossipTravelTime(GossipVertex gossiper, GossipVertex victim) {
    settledVertices = new HashSet<GossipVertex>();
    unSettledVertices = new PriorityQueue<GossipVertex>(1000,new shortestDistanceComparator());
    gossipTravelTime = new HashMap<GossipVertex, Integer>();
    predecessors = new HashMap<GossipVertex, GossipVertex>();
    gossipTravelTime.put(gossiper, 0);
    unSettledVertices.add(gossiper);
    while (unSettledVertices.size() > 0 && !isSettled(victim)) {
      GossipVertex vertex = unSettledVertices.poll();
      settledVertices.add(vertex);
      unSettledVertices.remove(vertex);
      findMinimalGossipTravelTime(vertex);
    }
    if(gossipTravelTime.containsKey(victim)==false) return -1;//gossip can't be reached to the victim, return -1;
    return gossipTravelTime.get(victim);
  }

  private void findMinimalGossipTravelTime(GossipVertex talker) {
    List<GossipVertex> listeners = getListeners(talker);
    for (GossipVertex listener : listeners) {
    	
      int newGossipTravelTime = getMinimalGossipTravelTime(talker)+ getChatTime(talker, listener);
      if (getMinimalGossipTravelTime(listener) > newGossipTravelTime) {
    	gossipTravelTime.put(listener, newGossipTravelTime);
        predecessors.put(listener, talker);
        unSettledVertices.add(listener);
      }
    }

  }

  private int getChatTime(GossipVertex talker, GossipVertex listener) {
    for (GossipEdge edge : talker.getAsTalkerEdges()) {
      if (edge.getListener().equals(listener)) {
        return edge.getChatTime();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private List<GossipVertex> getListeners(GossipVertex talker) {
    List<GossipVertex> listeners = new ArrayList<GossipVertex>();
    for (GossipEdge edge : talker.getAsTalkerEdges()) {
      if (!isSettled(edge.getListener())) {
    	  listeners.add(edge.getListener());
      }
    }
    return listeners;
  }
  
  private class shortestDistanceComparator implements Comparator<GossipVertex>{
	  public int compare(GossipVertex v1, GossipVertex v2){
		 int v1Dist = getMinimalGossipTravelTime(v1);
		 int v2Dist = getMinimalGossipTravelTime(v2);
		 if(v1Dist>v2Dist) return 1;
		 else if(v1Dist<v2Dist) return -1;
		 else return 0;
		 
	  }
  }
  
  private boolean isSettled(GossipVertex vertex) {
    return settledVertices.contains(vertex);
  }

  private int getMinimalGossipTravelTime(GossipVertex v) {
    Integer d = gossipTravelTime.get(v);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }

  /*
   * This method returns the path from the gossiper to the selected target and
   * NULL if no path exists
   */
  public LinkedList<GossipVertex> getPath(GossipVertex v) {
    LinkedList<GossipVertex> path = new LinkedList<GossipVertex>();
    GossipVertex step = v;
    // Check if a path exists
    if (predecessors.get(step) == null) {
      return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    return path;
  }

} 
