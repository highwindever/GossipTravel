/*
 * This class is used to build the gossip graph based on the information
 * from the xlsx file.
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

public class GossipChainBuilder {

	public GossipChainBuilder(String fileName, GossipGraph gg)  {
		
		try {
			//get the xlsx file and "Gossip Chain" sheet
			FileInputStream file;
			file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook;
			workbook = new XSSFWorkbook(file);
			XSSFSheet gossipChainSheet = workbook.getSheet("Gossip Chain");
			Iterator<Row> rowIterator = gossipChainSheet.iterator();
			//parse each row and cell to add edges and vertices to the graph
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				
				Cell talkerCell = cellIterator.next();
				if (talkerCell.getStringCellValue().equalsIgnoreCase("Talker"))
					continue;
				String talkerName = talkerCell.getStringCellValue();

				Cell listenerCell = cellIterator.next();
				String listenerName = listenerCell.getStringCellValue();

				Cell chatTimeCell = cellIterator.next();
				int chatTime = (int) chatTimeCell.getNumericCellValue();

				gg.addEdgeAndVertex(talkerName, listenerName, chatTime);	
				
			}

			file.close();
			//System.out.println(gg);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

}
