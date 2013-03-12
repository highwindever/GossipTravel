/*
 * GossipInputFormatter is used to process the input from the xlsx
 * file and get the required input json fromat.
 * 
 */

package edu.columbia.fengzhou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.*;


public class GossipInputFormatter {
	public GossipInputFormatter(String fileName, JSONObject json){
		try {
			//open the required sheet of the xlsx file
			FileInputStream file;
			file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook;
			workbook = new XSSFWorkbook(file);
			XSSFSheet gossipChainSheet = workbook.getSheet("Inputs & Outputs");
			Iterator<Row> rowIterator = gossipChainSheet.iterator();
			while(rowIterator.hasNext()) {
				//parse each row to get the input info
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
				
				//create the required input json
				JSONObject rumorJson = new JSONObject();				
				rumorJson.put("Rumor ID", rumorID);
				rumorJson.put("Gossiper", gossiperName);
				rumorJson.put("Victim",victimName);
				json.accumulate("Rumor", rumorJson);
			}

			file.close();
			//System.out.println(json.toString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

}
