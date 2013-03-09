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
				JSONObject rumorJson = new JSONObject();
				
				rumorJson.put("rumorID", rumorID);
				rumorJson.put("gossiper", gossiperName);
				rumorJson.put("victim",victimName);
				json.accumulate("rumor", rumorJson);
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
