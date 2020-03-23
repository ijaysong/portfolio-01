package com.corona.domain;

public class WorldDailyReport {
	private int num;
	private String country;
	private int confirmed;
	private int deaths;
	private int recovered;
	private String updatedDate;
	
	public WorldDailyReport() {}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getRecovered() {
		return recovered;
	}

	public void setRecovered(int recovered) {
		this.recovered = recovered;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "WorldDailyReport [num=" + num + ", country=" + country + ", confirmed=" + confirmed + ", deaths=" + deaths
				+ ", recovered=" + recovered + ", updatedDate=" + updatedDate + "]";
	}
	
}
