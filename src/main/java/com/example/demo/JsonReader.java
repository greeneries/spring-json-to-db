package com.example.demo;

import java.io.Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.vo.Food;
import com.example.demo.vo.Incredient;
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
		JSONObject json = readJsonFromUrl(
				"http://openapi.foodsafetykorea.go.kr/api/575b452f24504f6f983e/COOKRCP01/json/1/10");
		System.out.println(json.get("COOKRCP01"));

		Gson gson = new Gson();
		Cookrcp01 cookrcp = gson.fromJson(json.get("COOKRCP01").toString(), Cookrcp01.class);

		List<Row> rows = cookrcp.getRow();

		System.out.println("row size: " + rows.size());

		List<Food> foodList = new ArrayList<Food>(); // food list

		for (int i = 0; i < rows.size(); i++) {

			Food food = new Food(); // food 하나

			food.setHashTag(rows.get(i).getHASHTAG()); // hashtag
			food.setFat(Float.parseFloat(rows.get(i).getINFO_FAT())); // fat
			food.setCarbohydrate(Float.parseFloat(rows.get(i).getINFO_CAR())); // 탄수화물
			food.setName(rows.get(i).getRCP_NM()); // 메뉴명
			food.setProtein(Float.parseFloat(rows.get(i).getINFO_PRO())); // 단백질
			food.setCalorie(Float.parseFloat(rows.get(i).getINFO_ENG())); // 열량
			food.setIngredients(rows.get(i).getRCP_PARTS_DTLS()); // 재료정보

			food.setSmallImageLocation(rows.get(i).getATT_FILE_NO_MAIN()); // 이미지 소
			food.setBigImageLocation(rows.get(i).getATT_FILE_NO_MK()); // 이미지 대

			// 재료 낱개
			String[] incredients = rows.get(i).getRCP_PARTS_DTLS().split(",");
			
			List<Incredient> incredientList = new ArrayList<Incredient>(); // 재료 리스트
			for (String indi : incredients) {
				Incredient incredient = new Incredient();
				incredient.setName(indi.trim());
				incredientList.add(incredient); // 재료리스트에 재료 낱개로 넣기
			}

			rows.get(i).getMANUAL01(); // 만드는법 설명
			rows.get(i).getMANUAL_IMG01(); // 만드는 법 이미지

			foodList.add(food); // food list에 food 넣기
		}
		

	}
	
	
}
