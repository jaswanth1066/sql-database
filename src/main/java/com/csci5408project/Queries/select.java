package com.csci5408project.Queries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.csci5408project.log_management.LogWriterService;

//Author
//Kandarp Parikh
//B00873863

public class select {
	
	 Map<String, String> informationMap = new HashMap<>();
	//select * from StudentTable
	//select * from StudentTable where StudentName=KandarpModified
	//select StudentName,StudentID from StudentTable where StudentName=KandarpModified
	//select StudentID from StudentTable where StudentName=KandarpModified
	//select StudentName from StudentTable where StudentID=123
	//select StudentName,StudentID from StudentTable where StudentID=123
	public  void selectquery(String query, String databaseName , String userName) throws IOException {

		int exitFlag = 0;
		informationMap.put(LogWriterService.QUERY_LOG_EXECUTED_QUERY_KEY, query);
		
	    if(parseSelectQuery(query,databaseName) == true)
	    {
	    	exitFlag = 1;
	    }
	    LogWriterService.getInstance().write(informationMap);
	}
	public  boolean parseSelectQuery(String query,String databaseName) throws IOException {
		//Select * from xyz where col="";
		String[] queryArray = query.split(" ");
		String[] columnHeaders = queryArray[1].split(",");
		String tableName = queryArray[3];
		String TableLocation = "bin/Databases/"+databaseName+"/"+tableName+".txt";
		System.out.println(TableLocation);
		try {
		    BufferedReader br = new BufferedReader(new FileReader(TableLocation));
			if(queryArray.length > 4)
			{
				String condition = queryArray[5];
				String conditionColumn = queryArray[5].split("=")[0];
				String conditionValue = queryArray[5].split("=")[1];
				executeSelectWhereQuery(query , TableLocation ,conditionColumn , conditionValue);
			    return false;
			}
			else {
				executeSelectQuery(query , TableLocation);
			    return false;
			}

		} catch (FileNotFoundException e) {
			System.out.println("Error: table "+tableName+" does not exists");
			System.out.println("Enter \"1\" to exit the program , any other key to continue");
			Scanner sc = new Scanner(System.in);
			String s = sc.next();
			if(s.equals("1"))
			{
				System.out.println("exiting");
				return true;
			}
			else {
				return false;	
			}
			
		}
		
	}
	public  void executeSelectQuery(String query , String TableLocation) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(TableLocation));
	    String line;
		String[] queryArray = query.split(" ");
		String[] columnHeaders = queryArray[1].split(",");
		int databaseRecords = 0;
		long startTime = System.nanoTime();
	    if(columnHeaders[0].equalsIgnoreCase("*"))
	    {
		    while ((line = br.readLine()) != null) 
		    {
		    	databaseRecords +=1;
			      if(line.startsWith("<~colheader~>") || line.startsWith("<~row~>"))
			      {
			    	  String splitter = "";
			    	  if(line.startsWith("<~colheader~>"))
			    	  {
			    		  splitter = "<~colheader~>";
			    	  }
			    	  else 
			    	  {
			    		  splitter = "<~row~>";
			    	  }
			    	  //line.trim();
			    	  String[] contents = line.split(splitter);
			    	  for(String data : contents)
			    	  {
			    		  java.util.Formatter formatter = new java.util.Formatter();
			    		  formatter.format("%12s ||",data);
			    		  System.out.print(formatter);
			    	  }
			    	  System.out.println();
			      }
			 }
		    informationMap.put(LogWriterService.GENRAL_LOG_DATABASE_STATE_KEY, "Current records :"+databaseRecords);
		 }
	      else
	      {
	    	  List<Integer> indexOfColumns = new ArrayList<Integer>();
	    	  String[] rows = {};
	    	  while ((line = br.readLine()) != null) {
	    		  databaseRecords +=1;
			      if(line.startsWith("<~colheader~>"))
			      {
			    	  String[] Columns = line.split("<~colheader~>");
			    	  List<String> collist = Arrays.asList(Columns);
			    	  for(String s : columnHeaders)
			    	  {
			    		  if(!collist.contains(s))
			    		  {
			    			  informationMap.put(LogWriterService.EVENT_LOG_DATABASE_CRASH_KEY, "Table does not contain column: "+s);
			    			  System.out.println("Table does not contain column: "+s);	
			    			  break;
			    		  }
			    		  indexOfColumns.add(collist.indexOf(s));
			    		  java.util.Formatter formatter = new java.util.Formatter();
				    	  formatter.format("%12s ||",s);
			    		  System.out.print(formatter);
			    	  }
			    	  System.out.println();
			      }
			      if(line.startsWith("<~row~>"))
			      {
			    	  java.util.Formatter formatter = new java.util.Formatter();
			    	  String[] row = line.split("<~row~>");
			    	  for(int i =0 ; i<indexOfColumns.size();i++)
			    	  {
			    		  formatter.format("%12s ||",row[indexOfColumns.get(i)]);
			    	  }
			    	  System.out.print(formatter);
			      }
			      System.out.println();
			    	  
			      } 
	      }
	    long stopTime = System.nanoTime();
	    informationMap.put(LogWriterService.GENRAL_LOG_QUERY_EXECUTION_TIME_KEY , ""+(startTime-stopTime));
	    informationMap.put(LogWriterService.GENRAL_LOG_DATABASE_STATE_KEY, "Current records :"+databaseRecords);
	}
	
	public  void executeSelectWhereQuery(String query , String TableLocation,String conditionColumn ,String conditionValue) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(TableLocation));
	    String line;
		String[] queryArray = query.split(" ");
		String[] columnHeaders = queryArray[1].split(",");
		int databaseRecords = 0;
		long startTime = System.nanoTime();
	    if(columnHeaders[0].equalsIgnoreCase("*"))
	    {	
	    	List<String> columnList = new ArrayList<>();
		    while ((line = br.readLine()) != null) 
		    {	
		    	  databaseRecords +=1;
			      if(line.startsWith("<~colheader~>") || line.startsWith("<~row~>"))
			      {
			    	  String splitter = "";
			    	  if(line.startsWith("<~colheader~>"))
			    	  {
			    		  splitter = "<~colheader~>";
			    	  }
			    	  else 
			    	  {
			    		  splitter = "<~row~>";
			    	  }
			    	  //line.trim();
			    	  String[] contents = line.split(splitter);
			    	  List<String> list = Arrays.asList(contents);
			    	  
			    	  if(splitter == "<~colheader~>")
			    	  {
			    		  columnList = Arrays.asList(contents);
			    		  System.out.println(columnList);
			    	  for(String data : contents)
				    	  {
				    		  java.util.Formatter formatter = new java.util.Formatter();
				    		  formatter.format("%12s ||",data);
				    		  System.out.print(formatter);
				    	  }
			    	  System.out.println();
			    	  }
			    	  else {
//			    		  //System.out.println(columnList);
//			    		  //System.out.println(columnList.indexOf(conditionColumn));
//			    		  System.out.println(conditionValue);
//			    		  System.out.println(list.get(columnList.indexOf(conditionColumn)));
				    	  if(list.get(columnList.indexOf(conditionColumn)).equals(conditionValue))
				    	  {
			    			  for(int j=0;j<list.size();j++)
			    			  {
			    				  java.util.Formatter formatter = new java.util.Formatter();
			    				  formatter.format("%12s ||",list.get(j));
			    				  System.out.print(formatter);				  
			    			  }
			    			  System.out.println();
			    		  }
				    	  
			    	  }
			      }
			 }
		    informationMap.put(LogWriterService.GENRAL_LOG_DATABASE_STATE_KEY, "Current records :"+databaseRecords);
		 }
	      else
	      {
	    	  List<Integer> indexOfColumns = new ArrayList<Integer>();
	    	  String[] rows = {};
	    	  while ((line = br.readLine()) != null) {
	    		  databaseRecords +=1;
			      if(line.startsWith("<~colheader~>"))
			      {
			    	  String[] Columns = line.split("<~colheader~>");
			    	  List<String> collist = Arrays.asList(Columns);
			    	  for(String str : columnHeaders)
			    	  {
			    		  if(!collist.contains(str))
			    		  {
			    			  System.out.println("Table does not contain column: "+str);	
			    			  break;
			    		  }
			    		  else
			    		  {
				    		  indexOfColumns.add(collist.indexOf(str));
				    		  java.util.Formatter formatter = new java.util.Formatter();
					    	  formatter.format("%12s ||",str);
				    		  System.out.print(formatter);
			    		  }
			    	  }
			    	  System.out.println();
			      }
			      if(line.startsWith("<~row~>"))
			      {
			    	  java.util.Formatter formatterrow = new java.util.Formatter();
			    	  String[] row = line.split("<~row~>");
			    	  List<String> tmp = Arrays.asList(row);
//			    	  System.out.println(row);
//			    	  System.out.println(indexOfColumns);
			    	  List<String> TempRow = new ArrayList<>();
			    	  for(int i =0 ; i<indexOfColumns.size();i++)
			    	  {
			    		  TempRow.add(row[indexOfColumns.get(i)]);
			    	  }
//			    	  System.out.println(TempRow);
			    	  if(tmp.contains(conditionValue))
			    	  {
			    		  //System.out.println(TempRow.get(0) + TempRow.get(1));
		    			  for(int j=0;j<TempRow.size();j++)
		    			  {
		    				  formatterrow.format("%12s ||",TempRow.get(j));
		    			  }
		    			  System.out.print(formatterrow);
		    			  System.out.println();
		    		  }
  
			      } 	  
			      } 
	      }
	    long stopTime = System.nanoTime();
	    informationMap.put(LogWriterService.GENRAL_LOG_QUERY_EXECUTION_TIME_KEY , ""+(startTime-stopTime));
	    informationMap.put(LogWriterService.GENRAL_LOG_DATABASE_STATE_KEY, "Current records :"+databaseRecords);
	}

}
