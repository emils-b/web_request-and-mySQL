package worldometer;

import java.util.HashMap;
import java.util.Map;

public class CountryData {
	String countryName;
	int population;
	int density; //P/km2
	int landArea;
	int medianAge;
	int urbanPop; //%
	//static int countryCount;
	static Map<String, CountryData> countryDataList = new HashMap<String, CountryData>();
	//DB db;
	
	public CountryData (String countryName,	int population, int density, int landArea, int medianAge, int urbanPop) {
		this.countryName = countryName;
		this.population = population;
		this.density = density;
		this.landArea = landArea;
		this.medianAge = medianAge;
		this.urbanPop = urbanPop;
		//countryCount++;
		countryDataList.put(this.countryName, this);
		//this.db = new DB();
		//this.db.saveCountryData(countryDataList);
	}

}

