package com.yrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/*
 * Class that formats the data into desired file type
 * 
 */

public class FormatData {
	
	public static <T> void jsonFileFormatter(List<T> list, String filePath) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		
		String fileOutput = gson.toJson(list);
		
		File f = new File(filePath);
		PrintWriter pw;
		try {
			pw = new PrintWriter(f);
			pw.println("{");
			pw.print("\"" + list.get(0).getClass().getSimpleName().toLowerCase() + "s\": ");
			pw.println(fileOutput);
			pw.println("}");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		
		
	}
}
	
}
