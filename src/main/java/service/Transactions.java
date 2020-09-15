package service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import api.Api;
import support.Current;
import support.Daily;
import support.Hourly;

@Service
public class Transactions {
	
	String lat="12.9716";
	String lon="77.5946";
	CloseableHttpClient client=null;
	HttpGet get=null;
	CloseableHttpResponse resp=null;
	String locURL=null;
		
	public void setPlace(String place) throws ClientProtocolException, IOException
	{
		this.locURL="https://api.mapbox.com/geocoding/v5/mapbox.places/" + place +".json?access_token=pk.eyJ1IjoiaGltYW5zaCIsImEiOiJjazM3NGI2NDUwNW83M2duejdhYWYzcXgxIn0.Mnlssw3UsIv91IYvNX11ww";
		client = HttpClients.createDefault();
		
		get = new HttpGet(locURL);
		resp = client.execute(get);
		HttpEntity entity = resp.getEntity();
		System.out.println(entity);
		String s=EntityUtils.toString(entity);
		JSONObject j = new JSONObject(s);
		System.out.println(s);
		JSONArray j1 = j.getJSONArray("features");
		JSONObject j2 = j1.getJSONObject(0);
		JSONObject j3 =(JSONObject) j2.get("geometry");
		JSONArray arr =j3.getJSONArray("coordinates");
		this.lat=String.valueOf(arr.getDouble(1));
		this.lon=String.valueOf(arr.getDouble(0));
		client.close();
	}
	
	public Current currentlyCall() throws IOException
	{
		String url = "https://api.darksky.net/forecast/"+ Api.ApiKey()+"/" + lat + "," + lon + "?exclude=hourly,daily,flags";
		
		client = HttpClients.createDefault();
		
			get = new HttpGet(url);
			resp = client.execute(get);
			HttpEntity entity = resp.getEntity();
			
			System.out.println("Currently JSON resp");
			String s = EntityUtils.toString(entity);
			System.out.println(s);
			JSONObject j = new JSONObject(s);
			JSONObject j1 = j.getJSONObject("currently");
			
			Current c = new Current();
			c.setSummary(j1.getString("summary"));
			c.setIcon(j1.getString("icon"));
            c.setPrecipProbability(j1.getDouble("precipProbability"));
            c.setTemperature(j1.get("temperature") + "\u00B0");
            c.setWindSpeed(j1.getDouble("windSpeed"));
            
            long seconds = j1.getLong("time");
            
            Date date = new Date(seconds *1000);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a");
            System.out.println(sdf.format(date));
            
            c.setTime(sdf.format(date));
            
            client.close();
            return c;
		
	}
	
	public Hourly[] hourlyCall() throws IOException
	{
		
		String url = "https://api.darksky.net/forecast/"+ Api.ApiKey() +"/" + lat + "," + lon + "?exclude=currently,daily,flags";
		
		client = HttpClients.createDefault();
		
			get = new HttpGet(url);
			resp = client.execute(get);
			HttpEntity entity = resp.getEntity();
			
			System.out.println("hourly JSON resp");
			String s = EntityUtils.toString(entity);
			System.out.println(s);
			JSONObject j = new JSONObject(s);
			JSONObject j1 = (JSONObject) j.get("hourly");
			
			JSONArray arr = j1.getJSONArray("data");
			
			Hourly[] h = new Hourly[7];
			JSONObject j2=null;
			Date date=null;
			SimpleDateFormat sdf=new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a");
			long seconds;
			
			for(int i=0;i<7;i++)
			{
				h[i] = new Hourly();
				j2=arr.getJSONObject(i);
				h[i].setIcon(j2.getString("icon"));
				h[i].setPrecipProbability(j2.getDouble("precipProbability"));
				h[i].setSummary(j2.getString("summary"));
				h[i].setTemperature(j2.get("temperature")+ "\u00B0");
				
				seconds =j2.getLong("time");
				date=new Date(seconds*1000);
				
				h[i].setTime(sdf.format(date));
				
				h[i].setWindSpeed(j2.getDouble("windSpeed"));
			}
			return h;
	}
	
	public Daily dailyCall(int day) throws IOException
	{
		String url = "https://api.darksky.net/forecast/"+ Api.ApiKey() +"/" + lat + "," + lon + "?exclude=currently,hourly,flags";
		
		client = HttpClients.createDefault();
		
			get = new HttpGet(url);
			resp = client.execute(get);
			HttpEntity entity = resp.getEntity();
			
			Daily d = new Daily();
			
			System.out.println("Daily JSON resp");
			String s = EntityUtils.toString(entity);
			System.out.println(s);
			JSONObject j = new JSONObject(s);
	        JSONObject result1 = (JSONObject) j.get("daily");
	        
	        d.setWeeklySummary(result1.getString("summary"));
	        
	        JSONArray arr= result1.getJSONArray("data");
	        JSONObject j1 = arr.getJSONObject(day);
	        
	        long seconds = j1.getLong("time");
	        Date date=new Date(seconds*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a");
            
            d.setIcon(j1.getString("icon"));	
            d.setPrecipProbability(j1.getDouble("precipProbability"));
            d.setSummary(j1.getString("summary"));
            
            seconds=j1.getLong("sunriseTime");
            date=new Date(seconds*1000);
     
            d.setSunriseTime(sdf.format(date));
            
            seconds=j1.getLong("sunsetTime");
            date=new Date(seconds*1000);
            
            d.setSunsetTime(sdf.format(date));
            
            d.setTemperatureHigh(j1.get("temperatureHigh") + "\u00B0");
            d.setTemperatureLow(j1.get("temperatureLow") + "\u00B0");
            d.setTime(sdf.format(date));	
            
            return d;

	}
	
}
