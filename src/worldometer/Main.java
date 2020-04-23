package worldometer;

import java.util.ArrayList;

public class Main {
	
	static ArrayList<String> countryList = new ArrayList<String>();

	public static void main(String[] args) {
		String covUrlString = "https://www.worldometers.info/coronavirus/";
		String countryUrlString = "https://www.worldometers.info/world-population/population-by-country/";
		WebReader.getCovStatTable(covUrlString);
		WebReader.getCountryStatTable(countryUrlString);
		
		System.out.println(CountryCovidData.countryCount + "____"+CountryData.countryCount);

	}

}
