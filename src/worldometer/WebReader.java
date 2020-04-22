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
	
	
	static void getStatTable(String urlString) {
		Document doc = getPage(urlString);
		Element allTable = doc.getElementById("main_table_countries_today");
		Elements tableRows = allTable.getElementsByTag("tr");
		int i = 0;
		for (Element row : tableRows) {
			if (i <= 8) { 
			i++;
			continue;
			}
			Elements rowsCells = row.getElementsByTag("td");
			getCountryName(rowsCells, 0);
			getCountryHref(rowsCells, 0);
			getTotalCases(rowsCells, 1);
			getTotalDeath(rowsCells, 3);
			getTotalRecovered(rowsCells, 5);
			getTotalTests(rowsCells, 10);
			//CountryData country = new CountryData();
			i++;
		}
	}
	
	static String getCountryName(Elements rowsCells, int index) {
		String countryName;
		if (!isValidString(rowsCells, index)) countryName = "";
		else {
			countryName = rowsCells.get(index).text();
		}
		System.out.println(countryName);
		return countryName;
	}
	
	static String getCountryHref(Elements rowsCells, int index) {
		String countryHref = rowsCells.get(index).getElementsByTag("a").attr("href");
		System.out.println(countryHref);
		return countryHref;
	}
	
	static int getTotalCases(Elements rowsCells, int index) {
		int totalCases = Integer.parseInt(rowsCells.get(index).text().replace(",", ""));
		System.out.println("cases "+totalCases);
		return totalCases;
	}
	
	static int getTotalDeath(Elements rowsCells, int index) {
		int totalDeath = Integer.parseInt(rowsCells.get(index).text().replace(",", ""));
		System.out.println("dead "+totalDeath);
		return totalDeath;
	}
	
	static int getTotalRecovered(Elements rowsCells, int index) {
		int totalRecovered = Integer.parseInt(rowsCells.get(index).text().replace(",", ""));
		System.out.println("recovered "+totalRecovered);
		return totalRecovered;
	}
	
	static int getTotalTests(Elements rowsCells, int index) {
		int totalTests = Integer.parseInt(rowsCells.get(index).text().replace(",", ""));
		System.out.println("tests "+totalTests);
		return totalTests;
	}
	
	static boolean isValidString(Elements rowsCells, int index) {
		boolean isValidString = true;
		if (rowsCells.get(index).text().contentEquals("N//N")) isValidString=false;
		return isValidString;
	}
	
	static void pause(int millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			
		}
	}

}
