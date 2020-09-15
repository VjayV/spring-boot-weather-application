package support;

public class Current {
	
	String time,summary,icon,temperature;
	double precipProbability,windSpeed;
	
	public void setTime(String time) {
		this.time = time;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setPrecipProbability(double precipProbability) {
		this.precipProbability = precipProbability;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getTime() {
		return time;
	}
	public String getSummary() {
		return summary;
	}
	public String getIcon() {
		return icon;
	}
	public double getPrecipProbability() {
		return precipProbability;
	}
	public String getTemperature() {
		return temperature;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	
}
