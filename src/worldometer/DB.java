package worldometer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class DB {
	private Connection con;
	private Statement stat;
	private ResultSet rs;

	public DB() {
		try {
			this.con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/corona", "root", "");
			this.stat = con.createStatement();
			createTables();
		} catch (Exception e) {
			System.err.println("Problems creating DB connection");
			e.printStackTrace();
		}
	}

	//dataRelationQuery makes no sense here, its only for concept. Will use foreign keys instead
	public void saveCovidData(Map<String, CountryCovidData> data) {
		String query = "INSERT IGNORE INTO tbl_CountryCovidData (`countryName`, `totalCases`, `totalDeath`, `totalRecovered`, `totalTests`, `countryHref`, `totalCasesOnOneMil`) values ";
		String dataRelationQuery = "INSERT INTO rel_countrydata_coviddata (`country_data_name`, `covid_data_name`) values ";
		for (String country : Main.countryList) {
			CountryCovidData countryOb = data.get(country);
			String part = "('" + countryOb.countryName.replace("'", "\\\'") + "'," + countryOb.totalCases + ", "
					+ countryOb.totalDeath + " , " + countryOb.totalRecovered + " , " + countryOb.totalTests + " , '"
					+ countryOb.countryHref.replace("'", "\\\'") + "' , " + countryOb.totalCasesOnOneMil + "),";
			query += part;
			String relationPart = "('"+ countryOb.countryName.replace("'", "\\\'") +"' , '"+ countryOb.countryData.countryName.replace("'", "\\\'") +"'),";
			dataRelationQuery += relationPart;
		}
		query = query.substring(0, query.length() - 1);
		dataRelationQuery = dataRelationQuery.substring(0, dataRelationQuery.length() - 1);
		insert(query);
		insert(dataRelationQuery);
	}

	public void saveCountryData(Map<String, CountryData> data) {
		String query = "INSERT IGNORE INTO tbl_CountryData(`countryName`, `population`, `density`, `landArea`, `medianAge`, `urbanPop`) values ";
		for (String country : Main.countryList) {
			CountryData countryOb = data.get(country);
			String part = "('" + countryOb.countryName.replace("'", "\\\'") + "'," + countryOb.population + ", "
					+ countryOb.density + " , " + countryOb.landArea + " , " + countryOb.medianAge + " , "
					+ countryOb.urbanPop + "),";
			query += part;
		}
		query = query.substring(0, query.length() - 1);
		insert(query);
		createTableWithForeignKey();
	}
	
	//does the same as saveCountryData, only creates foreign key
	public void saveCountryDataWithForeignKey(Map<String, CountryData> data) {
		String query = "INSERT IGNORE INTO tbl_countrydata_foreign_key(`countryName`, `population`, `density`, `landArea`, `medianAge`, `urbanPop`) values ";
		for (String country : Main.countryList) {
			CountryData countryOb = data.get(country);
			String part = "('" + countryOb.countryName.replace("'", "\\\'") + "'," + countryOb.population + ", "
					+ countryOb.density + " , " + countryOb.landArea + " , " + countryOb.medianAge + " , "
					+ countryOb.urbanPop + "),";
			query += part;
		}
		query = query.substring(0, query.length() - 1);
		insert(query);
	}

	public void select() {
		try {
			String query = "select * from tbl_CountryCovidData";
			this.rs = stat.executeQuery(query);
			while (rs.next()) {
				int id = rs.getInt("id");
				String countryName = rs.getString("countryName");
				System.out.println(id + ": " + countryName);
			}
		} catch (Exception e) {
			System.err.println("problems with select");
			e.printStackTrace();
		}
	}

	public void insert(String insertQuery) {
		try {
			// String query = "INSERT IGNORE INTO tbl_CountryCovidData(`countryName`,
			// `totalCases`, `totalDeath`, `totalRecovered`, `totalTests`, `countryHref`,
			// `totalCasesOnOneMil`) values ('test',1,2,3,4,'test2',5)";
			stat.executeUpdate(insertQuery);
		} catch (Exception e) {
			System.err.println("problems with insert: " + insertQuery);
			e.printStackTrace();
		}
	}

	public void createTables() {
		String countryDataQuery = "CREATE TABLE IF NOT EXISTS `tbl_countrydata` (  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,  `countryName` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `population` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `density` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `landArea` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `medianAge` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `urbanPop` INT(10) UNSIGNED NOT NULL DEFAULT '0',  PRIMARY KEY (`id`) USING BTREE,  UNIQUE INDEX `countryName` (`countryName`) USING BTREE)COMMENT='Contains data about each country'COLLATE='utf8mb4_general_ci'ENGINE=InnoDB;";
		this.insert(countryDataQuery);
		String countryCovidDataQuery = "CREATE TABLE IF NOT EXISTS `tbl_countrycoviddata` (  `countryName` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `totalCases` INT(11) UNSIGNED NOT NULL DEFAULT '0',  `totalDeath` INT(11) UNSIGNED NOT NULL DEFAULT '0',  `totalRecovered` INT(11) UNSIGNED NOT NULL DEFAULT '0',  `totalTests` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `countryHref` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `totalCasesOnOneMil` INT(10) UNSIGNED NOT NULL DEFAULT '0',  PRIMARY KEY (`countryName`),  UNIQUE INDEX `countryName` (`countryName`),  UNIQUE INDEX `countryHref` (`countryHref`))COMMENT='Corona data for each country'COLLATE='utf8mb4_general_ci'ENGINE=InnoDB AUTO_INCREMENT=430;";
		this.insert(countryCovidDataQuery);
		String countryAndCovidDataRelations = "CREATE TABLE IF NOT EXISTS `rel_countrydata_coviddata` (   `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,  `country_data_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',  `covid_data_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',  PRIMARY KEY (`id`) USING BTREE)COLLATE='utf8mb4_general_ci'ENGINE=InnoDB;";
		this.insert(countryAndCovidDataRelations);

	}
	
	public void createTableWithForeignKey() {
		String countryDataQueryWithForeignKey = "CREATE TABLE IF NOT EXISTS `tbl_countrydata_foreign_key` (  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,  `countryName` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `population` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `density` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `landArea` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `medianAge` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `urbanPop` INT(10) UNSIGNED NOT NULL DEFAULT '0',  PRIMARY KEY (`id`) USING BTREE,  UNIQUE INDEX `countryName` (`countryName`) USING BTREE,  CONSTRAINT `countryName` FOREIGN KEY (`countryName`) REFERENCES `corona`.`tbl_countrycoviddata` (`countryName`) ON UPDATE RESTRICT ON DELETE RESTRICT)COMMENT='Contains data about each country'COLLATE='utf8mb4_general_ci'ENGINE=InnoDB AUTO_INCREMENT=628;";
		this.insert(countryDataQueryWithForeignKey);
	}
}
