package worldometer;

public class CountryData {
	String countryName;
	int population;
	int density; //P/km2
	int landArea;
	int medianAge;
	int urbanPop; //%
	static int countryCount;
	
	public CountryData (String countryName,	int population, int density, int landArea, int medianAge, int urbanPop) {
		this.countryName = countryName;
		this.population = population;
		this.density = density;
		this.landArea = landArea;
		this.medianAge = medianAge;
		this.urbanPop = urbanPop;
		countryCount++;
	}

}
