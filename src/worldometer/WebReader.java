package worldometer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebReader {

	static Document getPage(String urlString) {
		Document doc = null;
		pause(350);
		try {
			System.out.println("Calling web request");
			doc = Jsoup.connect(urlString).get();
		} catch (Exception e) {
			System.out.println("ERROR with web request");
			e.printStackTrace();
		}
		return doc;
	}

	/*
	 * NAV LABS RISINĀJUMS ĪSTI
	 */
	static void getCovStatTable(String urlString) {
		Document doc = getPage(urlString);
		Element allTable = doc.getElementById("main_table_countries_today");
		Elements tableRows = allTable.getElementsByTag("tr");
		int countryNameIndex = 0;
		int totalCovCasesIndex = 1;
		int totalCovDeathIndex = 3;
		int totalCovRecoveredIndex = 5;
		int totalCovTestsMadeIndex = 10;
		for (Element row : tableRows) {
			Elements rowsCells = row.getElementsByTag("td");
			if (rowsCells.size() < 10 || getStringData(rowsCells, countryNameIndex).contentEquals("")
					|| getCountryHref(rowsCells).contentEquals("")) {
				continue;
			}
			CountryCovidData countryCovDat = new CountryCovidData(getStringData(rowsCells, countryNameIndex),
					getIntData(rowsCells, totalCovCasesIndex), getIntData(rowsCells, totalCovDeathIndex),
					getIntData(rowsCells, totalCovRecoveredIndex), getIntData(rowsCells, totalCovTestsMadeIndex),
					getCountryHref(rowsCells));
		}
	}

	static void getCountryStatTable(String urlString) {
		Document doc = getPage(urlString);
		Element allTable = doc.getElementById("example2");
		Elements tableRows = allTable.getElementsByTag("tr");
		int countryNameIndex = 1;
		int populationIndex = 2;
		int densityIndex = 5;
		int landAreaIndex = 6;
		int medianAgeIndex = 9;
		int urbanPopIndex = 10;
		for (Element row : tableRows) {
			Elements rowsCells = row.getElementsByTag("td");
			if (rowsCells.size() < 10) {
				continue;
			}
			String name = getStringData(rowsCells, countryNameIndex);
			if (Main.countryList.contains(name)) {
				CountryData country = new CountryData(name, getIntData(rowsCells, populationIndex),
						getIntData(rowsCells, densityIndex), getIntData(rowsCells, landAreaIndex),
						getIntData(rowsCells, medianAgeIndex), getIntData(rowsCells, urbanPopIndex));
			}
		}
	}

	static String getStringData(Elements rowsCells, int index) {
		String countryName;
		if (!isValidString(rowsCells, index))
			countryName = "";
		else {
			countryName = rowsCells.get(index).text();
		}
		return countryName;
	}

	static String getCountryHref(Elements rowsCells) {
		String countryHref;
		int index = 0;
		if (!isValidString(rowsCells, index))
			countryHref = "";
		else {
			countryHref = rowsCells.get(index).getElementsByTag("a").attr("href");
		}
		return countryHref;
	}

	static int getIntData(Elements rowsCells, int index) {
		int totalCases;
		if (!isValidString(rowsCells, index))
			totalCases = 0;
		else if (rowsCells.get(index).text().contains("%")) {
			totalCases = getIntFromCellWithPercent(rowsCells, index);
		} else {
			totalCases = getIntFromCell(rowsCells, index);
		}
		return totalCases;
	}

	static int getIntFromCell(Elements rowsCells, int index) {
		return Integer.parseInt(rowsCells.get(index).text().replace(",", ""));
	}

	static int getIntFromCellWithPercent(Elements rowsCells, int index) {
		return Integer.parseInt(rowsCells.get(index).text().replace(" %", ""));
	}

	static boolean isValidString(Elements rowsCells, int index) {
		boolean isValidString = true;
		String row = rowsCells.get(index).text();
		if (row.contentEquals("N/A") || row.contentEquals("N.A.") || row.contentEquals(""))
			isValidString = false;
		return isValidString;
	}

	static void pause(int millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {

		}
	}

}
