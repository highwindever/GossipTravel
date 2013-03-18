/*
 * This class is used to fill in the JSON Request String and JSON Response String 
 * part in the xlsx file. It will print the shortest path for each rumor to travel 
 * from gossiper to victim.
 * 
 * I didn't use multiple threads for the process in this class since there would 
 * be multiple write operations on the same xlsx file. 
 * 
 */



package edu.columbia.fengzhou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Test;
import static org.junit.Assert.*;
public class GossipSolution {
	static GossipGraph gg;
	@Test public static void main(String[] args){

		try {
			// build the gossip chain as a graph
			gg = new GossipGraph();
			String fileName = "Programming Challenge.xlsx";
			new GossipChainBuilder(fileName, gg);
			
			//Test the graph
			List<GossipEdge> testEdges = gg.getEdges();
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
			
			//Calculate the travel time and calculation time for each rumor and 
			//fill in the required input and output Json Strings in the xlsx file
			JSONObject rumorJson = new JSONObject();
			JSONObject resultJson = new JSONObject();
			
			FileInputStream file;
			file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook;
			workbook = new XSSFWorkbook(file);
			XSSFSheet gossipChainSheet = workbook.getSheet("Inputs & Outputs");
			Iterator<Row> rowIterator = gossipChainSheet.iterator();
			
			long calTimeSum =0;
			int count =0;
			System.out.println("Path for each rumor:");
			
			while(rowIterator.hasNext()) {
				//parse each row
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				
				Cell rumorIDCell = cellIterator.next();
				if (rumorIDCell.getCellType()==Cell.CELL_TYPE_STRING)
					continue;
				int rumorID = (int)rumorIDCell.getNumericCellValue();

				Cell GossiperCell = cellIterator.next();
				String gossiperName = GossiperCell.getStringCellValue();

				Cell victimCell = cellIterator.next();
				String victimName = victimCell.getStringCellValue();
				
				//fill in the JSON Request String cells
				JSONObject inputJson = new JSONObject();
				inputJson.put("Rumor ID", rumorID);
				inputJson.put("Gossiper", gossiperName);
				inputJson.put("Victim",victimName);
				rumorJson.accumulate("Rumor", inputJson);
				Cell inputCell = row.createCell(4);
				inputCell.setCellValue(inputJson.toString());
				
				//calculate travel time and calculation time
				long startTime = System.nanoTime();
				GossipTravelTimeCalculation gttc = new GossipTravelTimeCalculation(gg);
				GossipVertex gossiper = gg.getVertex(gossiperName);
				GossipVertex victim = gg.getVertex(victimName);
				int rumorTravelTime = gttc.calculateGossipTravelTime(gossiper, victim);
				long stopTime = System.nanoTime();
			    long calculationTime = (long)((stopTime - startTime)/1000000.0);
			    calTimeSum += calculationTime;
			    count++;
			    //print the shortest path for the rumor which travels from gossiper to victim
			    GossipVertex target = gg.getVertex(victimName);
			    LinkedList<GossipVertex> path = gttc.getPath(target);			    
			    System.out.print(rumorID+": [");
			    int size =path.size();
			    for(int i=0;i<size;i++){
			    	System.out.print(path.get(i).getName());
			    	if(i==size-1) System.out.println("]");
			    	else System.out.print(" -> ");
			    }
			    
			    //test the gossip travel path
	    		assertTrue(path.get(0).getName().equals(gossiperName));//the first vertex in the path should be the talker
	    		assertTrue(path.get(size-1).getName().equals(victimName));//the last vertex in the path should be the listener
	    		
			    int travelTimeForPath =0;
			    Iterator<GossipVertex> itr = path.iterator();
			    GossipVertex talker = itr.next();
			    GossipVertex listener;
			    while(itr.hasNext()){
			    	listener = itr.next();
			    	travelTimeForPath += talker.findEdge(listener).getChatTime();
			    	talker = listener;
			    }
			    
			    assertTrue(travelTimeForPath==rumorTravelTime);//the gossip travel time should equal to the sum of the chat time in the path

			    //fill in the JSON Response String 
			    JSONObject outputJson = new JSONObject();
			    outputJson.put("Rumor ID", rumorID);
			    outputJson.put("Rumor Travel Time", rumorTravelTime);
			    outputJson.put("Calculation Time", calculationTime);
				resultJson.accumulate("Result", outputJson);
				Cell outputCell = row.createCell(5);
				outputCell.setCellValue(outputJson.toString());
			}

			file.close();
			FileOutputStream outFile =new FileOutputStream(new File("Programming Challenge.xlsx"));
		    workbook.write(outFile);
		    outFile.close();
		    //System.out.println();
		    //System.out.println("The output Json:");
			//System.out.println(resultJson);
			System.out.println();
			System.out.println("Calculation Performance:");
			System.out.println("total time:"+calTimeSum+"ms");
			System.out.println("avg time:"+1.0*calTimeSum/count+"ms");
		} catch (JSONException e) {			
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
