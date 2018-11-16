package com.example.demo;

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
import com.example.demo.vo.Recipe;
import com.google.gson.Gson;

public class ObjectToJson {

	private  String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public  JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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
	
	// json 데이터 가져오기
	public List<Row> getJsonData() throws JSONException, IOException {
	    JSONObject json = this.readJsonFromUrl("http://openapi.foodsafetykorea.go.kr/api/575b452f24504f6f983e/COOKRCP01/json/5/10");
	    System.out.println(json.get("COOKRCP01"));
	    
        Gson gson = new Gson();
        Cookrcp01 cookrcp = gson.fromJson(json.get("COOKRCP01").toString(), Cookrcp01.class);
        
        return cookrcp.getRow();
	}
	
	// 요리 방법
	public int parseFoodType(String cookingType) {
		int result = 1;

		if(cookingType.equals("밥")) {
			result = 1;
		}else if(cookingType.equals("국&찌개")) {
			result = 2;
		}else if(cookingType.equals("반찬")) {
			result = 3;
		}else if(cookingType.equals("일품")) {
			result = 4;
		}else if(cookingType.equals("후식")) {
			result = 5;
		}else {
			result = 1;
		}
		
		return result;
	}
	
	// 요리 방법
	public int parseCookingType(String cookingType) {
			int result = 1;

		
		if(cookingType.equals("끊이기")) {
			result = 1;
		}else if(cookingType.equals("굽기")) {
			result = 2;
		}else if(cookingType.equals("찌기")) {
			result = 3;
		}else if(cookingType.equals("기타")) {
			result = 4;
		}else if(cookingType.equals("튀기기")) {
			result = 5;
		}else if(cookingType.equals("볶기")){
			result = 6;
		}else {
			result = 1;
		}
		  
		  
		return result;
	}
	
	
	public List<Food> getFoodList(List<Row> rows) {
	    List<Food> foodList = new ArrayList<Food>(); // food list
	    
        for(int i=0; i< rows.size(); i++) {
        	
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
        	
        	food.setCookingTypeId(this.parseFoodType(rows.get(i).getRCP_WAY2().trim())); // 요리 방법
        	food.setFoodTypeId(this.parseCookingType(rows.get(i).getRCP_PAT2().trim())); // 요리 종류
        	
        	// 재료 낱개 
        	String[] incredients = rows.get(i).getRCP_PARTS_DTLS().split(",");
       	
        	List<Incredient> incredientList = this.getIncredientsList(incredients);
        	food.setIncredientList(incredientList); // 재료 붙이기 
        	
        	//incredientList.forEach(v -> System.out.println(v.getName()));       	
        	
        	List<Recipe> recipeList = this.getRecipeList(rows.get(i));
        	food.setRecipes(recipeList);
        	
        	foodList.add(food); // food list에 food 넣기 
        	//System.out.println(food.toString());
        	
        }
        return foodList;
	}
	
	
	// 재료 리스트 
	public List<Incredient> getIncredientsList(String[] incredients) {
		
		List<Incredient> incredientList = new ArrayList<Incredient>(); // 재료 리스트
		for (String indi : incredients) {
			Incredient incredient = new Incredient();
			incredient.setName(indi.trim());
			incredientList.add(incredient); // 재료리스트에 재료 낱개로 넣기
		}
		
		
		
		return incredientList;
	}
	
	// 레시피 
	public List<Recipe> getRecipeList(Row row) {
		List<Recipe> recipeList = new ArrayList<Recipe>();
		
    	if(!"".equals(row.getMANUAL01())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL01());//  만드는법 설명
    		
    		if(!"".equals(row.getMANUAL_IMG01())) {
        		recipe.setImageLocation(row.getMANUAL_IMG01());// 만드는법 이미지 
    		}
    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL02())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL02());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG02())) {
        		recipe.setImageLocation(row.getMANUAL_IMG02());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL03())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL03());
    		
    		if(!"".equals(row.getMANUAL_IMG03())) {
        		recipe.setImageLocation(row.getMANUAL_IMG03());// 만드는법 이미지 
    		}

    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL04())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL04());// 만드는 법 설명
    		
    		if(!"".equals(row.getMANUAL_IMG04())) {
        		recipe.setImageLocation(row.getMANUAL_IMG04());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	
    	if(!"".equals(row.getMANUAL05())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL05());// 만드는 법 설명
    		
    		if(!"".equals(row.getMANUAL_IMG05())) {
        		recipe.setImageLocation(row.getMANUAL_IMG05());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	
    	if(!"".equals(row.getMANUAL06())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL06());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG06())) {
        		recipe.setImageLocation(row.getMANUAL_IMG06());// 만드는법 이미지 
    		}

    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL07())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL07());// 만드는 법 설명
    		
    		if(!"".equals(row.getMANUAL_IMG07())) {
        		recipe.setImageLocation(row.getMANUAL_IMG07());// 만드는법 설명 
    		}

    		recipeList.add(recipe);
    	}
    	
    	
    	if(!"".equals(row.getMANUAL08())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL08());// 만드는 법 설명
    		
    		if(!"".equals(row.getMANUAL_IMG08())) {
        		recipe.setImageLocation(row.getMANUAL_IMG08());// 만드는법 이미지 
    		}	
    		recipeList.add(recipe);
    	}
    	
    	
    	
    	if(!"".equals(row.getMANUAL09())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL09());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG09())) {
        		recipe.setImageLocation(row.getMANUAL_IMG09());// 만드는법 이미지 
    		}	
    		
    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL10())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL10());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG10())) {
        		recipe.setImageLocation(row.getMANUAL_IMG10());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	
    	if(!"".equals(row.getMANUAL11())) {
    		Recipe recipe = new Recipe();

    		recipe.setDesc(row.getMANUAL11());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG11())) {
        		recipe.setImageLocation(row.getMANUAL_IMG11());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL12())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL12());// 만드는 법 이미지 
    		
    		if(!"".equals(row.getMANUAL_IMG12())) {
        		recipe.setImageLocation(row.getMANUAL_IMG12());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	
    	if(!"".equals(row.getMANUAL13())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL13());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG13())) {
        		recipe.setImageLocation(row.getMANUAL_IMG13());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL14())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL14());// 만드는 법 이미지 
    		
    		if(!"".equals(row.getMANUAL_IMG14())) {
        		recipe.setImageLocation(row.getMANUAL_IMG14());// 만드는법 이미지 
    		}

    		recipeList.add(recipe);
    	}
    	if(!"".equals(row.getMANUAL15())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL15());// 만드는 법 이미지 
    		
    		if(!"".equals(row.getMANUAL_IMG15())) {
        		recipe.setImageLocation(row.getMANUAL_IMG15());// 만드는법 이미지 
    		}

    		recipeList.add(recipe);
    	}

    	if(!"".equals(row.getMANUAL16())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL16());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG16())) {
        		recipe.setImageLocation(row.getMANUAL_IMG16());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	
    	if(!"".equals(row.getMANUAL17())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL17());// 만드는법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG17())) {
        		recipe.setImageLocation(row.getMANUAL_IMG17());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
       	if(!"".equals(row.getMANUAL18())) {
    		Recipe recipe = new Recipe();
      		recipe.setDesc(row.getMANUAL18());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG18())) {
        		recipe.setImageLocation(row.getMANUAL_IMG18());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
       	
    	if(!"".equals(row.getMANUAL19())) {
    		Recipe recipe = new Recipe();
    		recipe.setDesc(row.getMANUAL19());// 만드는 법 설명 
    		
    		if(!"".equals(row.getMANUAL_IMG19())) {
        		recipe.setImageLocation(row.getMANUAL_IMG19());// 만드는법 이미지 
    		}
    		
    		recipeList.add(recipe);
    	}
    	
    	if(!"".equals(row.getMANUAL20())) {
    		Recipe recipe = new Recipe();
 
    		recipe.setDesc(row.getMANUAL20());// 만드는 법 설명 
    		    		
    		if(!"".equals(row.getMANUAL_IMG20())) {
        		recipe.setImageLocation(row.getMANUAL_IMG20());// 만드는법 이미지 
    		}
    		recipeList.add(recipe);
    	}
    	
    	return recipeList;
    	
	}

	public static void main(String args[]) throws IOException, JSONException{
		ObjectToJson objectToJson = new ObjectToJson();
		List<Row> rows = objectToJson.getJsonData();
		List<Food> list = objectToJson.getFoodList(rows);
		

		
		System.out.println(list.size());
		
	}

}
