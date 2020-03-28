package com.corona.domain;

public class WorldDailyReport {
	
	private int countryId;       // 국가ID
	private String countryName;  // 국가명
	private int confirmed;       // 확진환자
	private int deaths;          // 사망자
	private int recovered;       // 격리해제
	private String updatedDate;  // 갱신일자
	
	
	public WorldDailyReport() {}

	
	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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
		return "WorldDailyReport [countryId=" + countryId + ", countryName=" + countryName
				+ ", confirmed=" + confirmed + ", deaths=" + deaths + ", recovered=" + recovered + ", updatedDate="
				+ updatedDate + "]";
	}

}
