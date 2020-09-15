package org.example.weather;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import service.Transactions;
import support.Current;
import support.Daily;
import support.Hourly;

@RestController
public class weatherController {
	
	@Autowired
	Transactions t; 
	
	@GetMapping("/getCurrent")
	public Current getCurrent() throws IOException
	{
		Current c = t.currentlyCall();
		return c;
	}
	@GetMapping("/getHourly")
	public Hourly[] getHourly() throws IOException
	{
		Hourly[] c = t.hourlyCall();
		return c;
	}
	@GetMapping("/getDaily")
	public Daily getDaily(@Param("day") int day) throws IOException
	{
		Daily d= t.dailyCall(day);
		return d;
	}
	@GetMapping("/setLoc")
	public void setLoc(@Param("loc") String loc)
	{
		try {
			t.setPlace(loc);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@GetMapping("/w")
	public String s()
	{
		return "example mapping";
	}
}
