package edu.columbia.fengzhou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class GossipTravelTimeCalculation {

  private final HashMap<String,GossipVertex> nodes;
  private final ArrayList<GossipEdge> edges;
  private Set<GossipVertex> settledNodes;
  private Set<GossipVertex> unSettledNodes;
  private Map<GossipVertex, GossipVertex> predecessors;
  private Map<GossipVertex, Integer> distance;

  public GossipTravelTimeCalculation(GossipGraph gg) {
    // Create a copy of the array so that we can operate on this array
    this.nodes = new HashMap<String,GossipVertex>(gg.getVertices());
    this.edges = new ArrayList<GossipEdge>(gg.getEdges());
  }

  public int calculateGossipTravelTime(GossipVertex source, GossipVertex victim) {
    settledNodes = new HashSet<GossipVertex>();
    unSettledNodes = new HashSet<GossipVertex>();
    distance = new HashMap<GossipVertex, Integer>();
    predecessors = new HashMap<GossipVertex, GossipVertex>();
    distance.put(source, 0);
    unSettledNodes.add(source);
    while (unSettledNodes.size() > 0 && settledNodes.contains(victim)==false) {
      GossipVertex node = getMinimum(unSettledNodes);
      settledNodes.add(node);
      unSettledNodes.remove(node);
      findMinimalDistances(node);
    }
    if(distance.containsKey(victim)==false) return -1;
    return distance.get(victim);
  }

  private void findMinimalDistances(GossipVertex node) {
    List<GossipVertex> adjacentNodes = getNeighbors(node);
    for (GossipVertex target : adjacentNodes) {
      if (getShortestDistance(target) > getShortestDistance(node)
          + getDistance(node, target)) {
        distance.put(target, getShortestDistance(node)
            + getDistance(node, target));
        predecessors.put(target, node);
        unSettledNodes.add(target);
      }
    }

  }

  private int getDistance(GossipVertex node, GossipVertex target) {
    for (GossipEdge edge : edges) {
      if (edge.getTalker().equals(node)
          && edge.getListener().equals(target)) {
        return edge.getChatTime();
      }
    }
    throw new RuntimeException("Should not happen");
  }

  private List<GossipVertex> getNeighbors(GossipVertex node) {
    List<GossipVertex> neighbors = new ArrayList<GossipVertex>();
    for (GossipEdge edge : edges) {
      if (edge.getTalker().equals(node)
          && !isSettled(edge.getListener())) {
        neighbors.add(edge.getListener());
      }
    }
    return neighbors;
  }

  private GossipVertex getMinimum(Set<GossipVertex> vertexes) {
    GossipVertex minimum = null;
    for (GossipVertex vertex : vertexes) {
      if (minimum == null) {
        minimum = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
          minimum = vertex;
        }
      }
    }
    return minimum;
  }
    
  private boolean isSettled(GossipVertex vertex) {
    return settledNodes.contains(vertex);
  }

  private int getShortestDistance(GossipVertex destination) {
    Integer d = distance.get(destination);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }

  /*
   * This method returns the path from the source to the selected target and
   * NULL if no path exists
   */
  public LinkedList<GossipVertex> getPath(GossipVertex target) {
    LinkedList<GossipVertex> path = new LinkedList<GossipVertex>();
    GossipVertex step = target;
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
