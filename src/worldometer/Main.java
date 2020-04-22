package worldometer;

public class Main {

	public static void main(String[] args) {
		String urlString = "https://www.worldometers.info/coronavirus/";
		WebReader.getStatTable(urlString);

	}

}
