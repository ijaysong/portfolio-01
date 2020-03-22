package com.corona.domain;

public class WorldDailyReport {
	private int id;
	private String country;
	private int confirmed;
	private int deaths;
	private int recovered;
	private String updatedDate;
	
	public WorldDailyReport() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return "WorldDailyReport [id=" + id + ", country=" + country + ", confirmed=" + confirmed + ", deaths=" + deaths
				+ ", recovered=" + recovered + ", updatedDate=" + updatedDate + "]";
	}
	
}
