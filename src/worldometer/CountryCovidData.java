package worldometer;

public class CountryCovidData {
	String countryName;
	int totalCases;
	int totalDeath;
	int totalRecovered;
	int totalTests;
	String countryHref; //karoga lejuplādēšanai izmantot var
	static int countryCount;

	public CountryCovidData(String countryName, int totalCases, int totalDeath, int totalRecovered, int totalTests,
			String countryHref) {
		this.countryName = countryName.trim();
		this.totalCases = totalCases;
		this.totalDeath = totalDeath;
		this.totalRecovered = totalRecovered;
		this.totalTests = totalTests;
		this.countryHref = countryHref;
		Main.countryList.add(this.countryName);
		countryCount++;
		//this.printCountry();
	}
	
	/*void printCountry() {
		System.out.println(countryCount +" "+ this.countryName + " has "+this.totalCases+" total cases "+this.totalDeath+" total death"+this.totalRecovered+" total recovered "+this.totalTests+
				" total tests done. "+this.countryHref);
	}*/

}
