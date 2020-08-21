package com.ssafy.jara.common.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssafy.jara.dao.WeatherDao;
import com.ssafy.jara.dto.Location;

@Component
public class WeatherService {

	protected static Log log = LogFactory.getLog(WeatherService.class);
	
	@Autowired
	WeatherDao weatherDao;
	
	// 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
	private static String[] PTYCode = new String[] {"없음", "비", "비/눈", "눈", "소나기", "빗방울", "빗방울/눈날림", "눈날림"};
	
	// 하늘상태(SKY) 코드 : 맑음(1), 구름많음(3), 흐림(4) 
	private static String[] SKYCode = new String[] {"", "맑음", "", "구름많음", "흐림"};
	
	// 2시간마다 실행
	@Scheduled(cron = "0 0 0/2 * * *")
	public void WeatherScheduled() throws IOException, ParseException {
		Calendar cal = Calendar.getInstance(Locale.KOREA);
        
        String year = String.valueOf(cal.get(cal.YEAR));
        String month = String.valueOf(cal.get(cal.MONTH) + 1);
        String day = String.valueOf(cal.get(cal.DATE));
        String time = String.valueOf(cal.get(cal.HOUR_OF_DAY) - 1); // 해당 시간 예보 검색을 위한 (현재 시간 - 1)
        String originTime = String.valueOf(cal.get(cal.HOUR_OF_DAY)); // 현재 시간
        
        String base_date = year + ((month.length() < 2)? "0" + month : month) + day; // (200811 201225 형태)
        String base_time = (time.length() < 2 ? "0" + time : time) + "00"; // 현재 시간 - 1 기준 (0900 1200 형태) 
        String orgTime = (originTime.length() < 2 ? "0" + originTime : originTime) + "00"; // 현재 시간 기준
		
		List<Location> locationList = (List<Location>) weatherDao.selectLocation(); // Location에 저장되어 있는 구 정보 받아옴
		
		for (int i = 0; i < locationList.size(); i++) {
			Location location = locationList.get(i);
			
			// 받아온 기상 정보를 JSON parsing을 통해 원하는 시간의 원하는 정보(강수형태, 하늘상태, 기온)만 추출
			jsonParse(apiConnection(base_date, base_time, location.getNx(), location.getNy()), location.getName(), orgTime);
		}
	}

	// 발표날짜, 발표시각, 격자위도, 격자경도로 기상청 api와 통신하여 해당 위치의 기상 정보를 JSON으로 받아옴
	public String apiConnection(String base_date, String base_time, String nx, String ny) throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + "DysdYWgqnU55g9Q7PoDso1H3%2BQJFLffwk4YkWWCS4cSluMp9qKnSyq0J0u1PXFITKErf1yqWK%2FrwEUKlHTVePw%3D%3D"); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(base_date, "UTF-8")); /*현재날짜(20200810) 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(base_time, "UTF-8")); /*현재시각 발표(정시단위)*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점 Y 좌표*/
       
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        
        BufferedReader rd;
        
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        StringBuilder sb = new StringBuilder();
        
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
		return sb.toString();
	}
	
	
	// JSON에서 원하는 시간의 원하는 정보(강수형태, 하늘상태, 기온)을 추출하여 해당 구(name) 테이블의 기상 정보(컬럼 PTY, SKY, T1H) 수정
	public void jsonParse(String jsonData, String name, String orgTime) throws ParseException {
		
		Location location = new Location();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonData);
		JSONObject parse_response = (JSONObject)jsonObject.get("response");
		JSONObject parse_body = (JSONObject)parse_response.get("body");
		JSONObject parse_items = (JSONObject)parse_body.get("items");
		JSONArray parse_item = (JSONArray)parse_items.get("item");
		
		JSONObject weather;
		
		for (int i = 0; i < parse_item.size(); i++) {
			weather = (JSONObject)parse_item.get(i);
			
			if(weather.get("category").equals("PTY") && weather.get("fcstTime").equals(orgTime)) {
				location.setPTY(PTYCode[Integer.parseInt((String) weather.get("fcstValue"))]); // PTY 정보 추출
			}
			
			if(weather.get("category").equals("SKY") && weather.get("fcstTime").equals(orgTime)) {
				location.setSKY(SKYCode[Integer.parseInt((String)weather.get("fcstValue"))]); // SKY 정보 추출
			}
			
			if(weather.get("category").equals("T1H") && weather.get("fcstTime").equals(orgTime)) {
				location.setT1H((String) weather.get("fcstValue")); // T1H 정보 추출
			}
		}
		
		location.setName(name);
		
		weatherDao.updateLocationWeather(location);
	}
	
	
	public String selectPTY(String name) {
		return weatherDao.selectPTY(name);
	}

	public String selectSKY(String name) {
		return weatherDao.selectSKY(name);
	}

	public String selectT1H(String name) {
		return weatherDao.selectT1H(name);
	}
}
