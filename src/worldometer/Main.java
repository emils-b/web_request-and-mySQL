package worldometer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
	
	static ArrayList<String> countryList = new ArrayList<String>();

	public static void main(String[] args) {
		String covUrlString = "https://www.worldometers.info/coronavirus/";
		String countryUrlString = "https://www.worldometers.info/world-population/population-by-country/";
		
		WebReader.getCovStatTable(covUrlString);
		WebReader.getCountryStatTable(countryUrlString);
		
		
		DB db = new DB();
		
		db.saveCovidData(CountryCovidData.countryCovList);
		db.saveCountryData(CountryData.countryDataList);
		db.saveCountryDataWithForeignKey(CountryData.countryDataList);

		
		/*for (String c:countryList) {
			System.out.println(CountryCovidData.countryCovList.get(c).countryName +"______"+CountryData.countryDataList.get(c).countryName);
		}*/
	}

}
