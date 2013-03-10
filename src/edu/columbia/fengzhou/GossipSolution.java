package edu.columbia.fengzhou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

public class GossipSolution {
	static GossipGraph gg;
	public static void main(String[] args){

		try {
			gg = new GossipGraph();
			String fileName = "Programming Challenge.xlsx";

			new GossipChainBuilder(fileName, gg);// build the gossip chain as a
													// graph
			
			// System.out.println(gg);
			JSONObject rumorJson = new JSONObject();
			JSONObject resultJson = new JSONObject();
			FileInputStream file;
			file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook;
			workbook = new XSSFWorkbook(file);
			XSSFSheet gossipChainSheet = workbook.getSheet("Inputs & Outputs");
			Iterator<Row> rowIterator = gossipChainSheet.iterator();
			while(rowIterator.hasNext()) {
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
				JSONObject inputJson = new JSONObject();
				
				inputJson.put("Rumor ID", rumorID);
				inputJson.put("Gossiper", gossiperName);
				inputJson.put("Victim",victimName);
				rumorJson.accumulate("Rumor", inputJson);
				Cell inputCell = row.createCell(4);
				inputCell.setCellValue(inputJson.toString());
				
				long startTime = System.currentTimeMillis();
				GossipTravelTimeCalculation gttc = new GossipTravelTimeCalculation(gg);
				GossipVertex gossiper = gg.getVertex(gossiperName);
				GossipVertex victim = gg.getVertex(victimName);
				int rumorTravelTime = gttc.execute(gossiper, victim);
				long stopTime = System.currentTimeMillis();
			    long calculationTime = stopTime - startTime;
			    
			    //for test
			    GossipVertex target = gg.getVertex(victimName);
			    LinkedList<GossipVertex> path = gttc.getPath(target);
			    System.out.println(rumorID+":"+path.toString());
			    
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
			System.out.println(resultJson);
		} catch (JSONException e) {			
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	
	
}
