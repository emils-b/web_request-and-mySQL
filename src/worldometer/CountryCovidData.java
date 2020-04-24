package worldometer;

import java.util.HashMap;
import java.util.Map;

public class CountryCovidData {
	String countryName;
	int totalCases;
	int totalDeath;
	int totalRecovered;
	int totalTests;
	String countryHref; // karoga lejuplādēšanai izmantot var
	double totalCasesOnOneMil;
	CountryData countryData;
	static Map<String, CountryCovidData> countryCovList = new HashMap<String, CountryCovidData>();
	//DB db;
	
	public CountryCovidData(String countryName, int totalCases, int totalDeath, int totalRecovered, int totalTests,
			double totalCasesOnOneMil, String countryHref) {
		this.countryName = countryName.trim();
		this.totalCases = totalCases;
		this.totalDeath = totalDeath;
		this.totalRecovered = totalRecovered;
		this.totalTests = totalTests;
		this.totalCasesOnOneMil = totalCasesOnOneMil;
		this.countryHref = countryHref;
		changeCountryName();
		Main.countryList.add(this.countryName);
		countryCovList.put(this.countryName, this);
		//this.db = new DB();
		//this.db.saveCovidData(countryCovList);
		
		// this.printCountry();
	}
	
	
	
	// vai šo var kā veiksmīgāk???
	public void changeCountryName() {
		/*
		 * String[] nameList = {"USA", "UK", "S. Korea", "UAE", "Czechia",
		 * "Ivory Coast", "Palestine", "DRC", "CAR", "Saint Kitts and Nevis",
		 * "St. Vincent Grenadines", "Vatican City", "St. Barth",
		 * "Sao Tome and Principe", "Saint Pierre Miquelon"};
		 */
		if (this.countryName.equals("USA"))
			this.countryName = "United States";
		if (this.countryName.equals("UK"))
			this.countryName = "United Kingdom";
		if (this.countryName.equals("S. Korea"))
			this.countryName = "South Korea";
		if (this.countryName.equals("UAE"))
			this.countryName = "United Arab Emirates";
		if (this.countryName.equals("Czechia"))
			this.countryName = "Czech Republic (Czechia)";
		if (this.countryName.equals("Ivory Coast"))
			this.countryName = "Côte d'Ivoire";
		if (this.countryName.equals("Palestine"))
			this.countryName = "State of Palestine";
		if (this.countryName.equals("DRC"))
			this.countryName = "DR Congo";
		if (this.countryName.equals("CAR"))
			this.countryName = "Central African Republic";
		if (this.countryName.equals("Saint Kitts and Nevis"))
			this.countryName = "Saint Kitts & Nevis";
		if (this.countryName.equals("St. Vincent Grenadines"))
			this.countryName = "St. Vincent & Grenadines";
		if (this.countryName.equals("St. Barth"))
			this.countryName = "Saint Barthelemy";
		if (this.countryName.equals("Sao Tome and Principe"))
			this.countryName = "Sao Tome & Principe";
		if (this.countryName.equals("Saint Pierre Miquelon"))
			this.countryName = "Saint Pierre & Miquelon";
	}

	/*
	 * void printCountry() { System.out.println(countryCount +" "+ this.countryName
	 * +
	 * " has "+this.totalCases+" total cases "+this.totalDeath+" total death"+this.
	 * totalRecovered+" total recovered "+this.totalTests+
	 * " total tests done. "+this.countryHref); }
	 */

}
