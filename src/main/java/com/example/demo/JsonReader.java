package com.example.demo;

import java.io.Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JsonReader {

	 private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }

		  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONObject json = new JSONObject(jsonText);
		      return json;
		    } finally {
		      is.close();
		    }
		  }

		  public static void main(String[] args) throws IOException, JSONException {
		    JSONObject json = readJsonFromUrl("http://openapi.foodsafetykorea.go.kr/api/575b452f24504f6f983e/COOKRCP01/json/1/10");
		    System.out.println(json.get("COOKRCP01"));

	        Gson gson = new Gson();
	        Cookrcp01 food = gson.fromJson(json.get("COOKRCP01").toString(), Cookrcp01.class);
	        
	        List<Row> rows = food.getRow();
	        
	        System.out.println("row size: "+rows.size());
	        
	        for(int i=0; i< rows.size(); i++) {
	        	String[] incredients = rows.get(i).getRCP_PARTS_DTLS().split(",");
	        	
//	        	for(String incredient : incredients){
//	        		System.out.println(incredient.trim());
//	        	}
	        	
	        }
	        
	        //System.out.println(food.toString());
	        //만드는법 

		  }

}
