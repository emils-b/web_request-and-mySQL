package worldometer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	//UPDATE `corona`.`tbl_countrycoviddata` SET `totalCases`='111111111' WHERE  `countryName`='United States';
	/*
	 * String query = "UPDATE `corona`.`tbl_countrycoviddata` SET `totalCases`='" + countryOb.totalCases + "', `totalDeath`='"
					+ countryOb.totalDeath + "', `totalRecovered`='" + countryOb.totalRecovered + "', `totalTests`='" + countryOb.totalTests + "', `countryHref`='"
					+ countryOb.countryHref.replace("'", "\\\'") + "', `totalCasesOnOneMil`='" + countryOb.totalCasesOnOneMil + "'
	 *  WHERE `countryName` = '" + countryOb.countryName.replace("'", "\\\'") + "'";
	 * 
	 */
	
	//šis atjauno jau esošo tabulu
	public void updateCovidData(Map<String, CountryCovidData> data) {
		for (String country : Main.countryList) {
			String query = "UPDATE `corona`.`tbl_countrycoviddata` SET `totalCases`=";
			CountryCovidData countryOb = data.get(country);
			String part = "'" + countryOb.totalCases + "', `totalDeath`='"
					+ countryOb.totalDeath + "', `totalRecovered`='" + countryOb.totalRecovered + "', `totalTests`='" + countryOb.totalTests + "', `countryHref`='"
					+ countryOb.countryHref.replace("'", "\\\'") + "', `totalCasesOnOneMil`='" + countryOb.totalCasesOnOneMil + "' WHERE `countryName` = '" + countryOb.countryName.replace("'", "\\\'") + "'";
			query += part;
			insert(query);
		}
	}
		
	public void updateCountryData(Map<String, CountryData> data) {
		for (String country : Main.countryList) {
			String query = "UPDATE `corona`.`tbl_CountryData` SET `population`=";
			CountryData countryOb = data.get(country);
			String part = "'" + countryOb.population + "', `density`='"
					+ countryOb.density + "', `landArea`='" + countryOb.landArea + "', `medianAge`='" + countryOb.medianAge + "', `urbanPop`='"
					+ countryOb.urbanPop + "' WHERE `countryName` = '" + countryOb.countryName.replace("'", "\\\'") + "'";
			query += part;
			insert(query);
		}
	}
	
	
	public void updateCountryDataWithForeignKey(Map<String, CountryData> data) {
		for (String country : Main.countryList) {
			String query = "UPDATE `corona`.`tbl_countrydata_foreign_key` SET `population`=";
			CountryData countryOb = data.get(country);
			String part = "'" + countryOb.population + "', `density`='"
					+ countryOb.density + "', `landArea`='" + countryOb.landArea + "', `medianAge`='" + countryOb.medianAge + "', `urbanPop`='"
					+ countryOb.urbanPop + "' WHERE `countryName` = '" + countryOb.countryName.replace("'", "\\\'") + "'";
			query += part;
			insert(query);
		}
	}
	
	//dataRelationQuery makes no sense here, its only for concept. Will use foreign keys instead
	public void saveCovidData(Map<String, CountryCovidData> data) {
		String query = "INSERT IGNORE INTO tbl_CountryCovidData (`countryName`, `totalCases`, `totalDeath`, `totalRecovered`, `totalTests`, `countryHref`, `totalCasesOnOneMil`) values ";
		String dataRelationQuery = "INSERT IGNORE INTO rel_countrydata_coviddata (`country_data_name`, `covid_data_name`) values ";
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
			stat.executeUpdate(insertQuery);
		} catch (Exception e) {
			System.err.println("problems with insert: " + insertQuery);
			e.printStackTrace();
		}
	}
	
	public void readCovidData() {
		try {
			String query = "select * from tbl_countrycoviddata";
			this.rs = stat.executeQuery(query);
			while (rs.next()) {
				//int id = rs.getInt("id");
				String name = rs.getString("countryName");
				System.out.println(name);
			}
		} catch (Exception e) {
			System.err.println("Problem with select");
			e.printStackTrace();
		}
	}
	
	public void readCountryData() {
		try {
			String query = "select * from tbl_countrydata";
			this.rs = stat.executeQuery(query);
			while (rs.next()) {
				//int id = rs.getInt("id");
				String name = rs.getString("countryName");
				System.out.println(name);
			}
		} catch (Exception e) {
			System.err.println("Problem with select");
			e.printStackTrace();
		}
	}
	
	public void readCountryDataWithForeignKey() {
		try {
			String query = "select * from tbl_countrydata_foreign_key";
			this.rs = stat.executeQuery(query);
			while (rs.next()) {
				String name = rs.getString("countryName");
				System.out.println(name);
			}
		} catch (Exception e) {
			System.err.println("Problem with select");
			e.printStackTrace();
		}
	}
	
	//just for concept. gets each object from its name in DB
	public void readCovidCountryRelations(Map<String, CountryCovidData> countryCovList, Map<String, CountryData> countryDataList) {
		try {
			String query = "select * from rel_countrydata_coviddata";
			this.rs = stat.executeQuery(query);
			while (rs.next()) {
				//int id = rs.getInt("id");
				String covidCountryName = rs.getString("covid_data_name");
				String countryName = rs.getString("country_data_name");
				CountryCovidData covDat = countryCovList.get(covidCountryName);
				CountryData countryDat = countryDataList.get(countryName);
				if (covDat==null || countryDat==null) {
					System.err.println("Problem with country realltion on "+covidCountryName+" and "+countryName);
				}
				System.out.println(covDat.countryName+ " and "+countryDat.countryName);
				//System.out.println(name);
			}
		} catch (Exception e) {
			System.err.println("Problem with select");
			e.printStackTrace();
		}
	}
	
	
	public String checkIfTablesExist(String tableName) {
		String countryName1 = null;
		try {
			//String query = "SELECT * FROM information_schema.tables WHERE table_schema = 'corona' AND table_name = 'tbl_CountryDat' LIMIT 1;"; //com.mysql.jdbc.JDBC42ResultSet@179ece50
			String query = "SHOW TABLES LIKE '" +tableName+"';";
			this.rs = stat.executeQuery(query);
			//System.out.println(rs);
			while(rs.next()){
				countryName1 =  rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("Problem with testing if tables exist");
			e.printStackTrace();
		}
		return countryName1;
	}
	/*
	 * db.saveCovidData(CountryCovidData.countryCovList);
		db.saveCountryData(CountryData.countryDataList);
		db.saveCountryDataWithForeignKey(CountryData.countryDataList);
	 */

	public void createTables() {
		String tableNameCountryData = "tbl_countrydata";
		String tableNameCountryCovData = "tbl_countrycoviddata";
		String tableNameRelCountryCovData = "rel_countrydata_coviddata";
		if (checkIfTablesExist(tableNameCountryData)==null) {
			String countryDataQuery = "CREATE TABLE IF NOT EXISTS `"+tableNameCountryData+"` (  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,  `countryName` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `population` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `density` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `landArea` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `medianAge` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `urbanPop` INT(10) UNSIGNED NOT NULL DEFAULT '0',  PRIMARY KEY (`id`) USING BTREE,  UNIQUE INDEX `countryName` (`countryName`) USING BTREE)COMMENT='Contains data about each country'COLLATE='utf8mb4_general_ci'ENGINE=InnoDB;";
			this.insert(countryDataQuery);
			saveCountryData(CountryData.countryDataList);
			System.out.println("creating table "+tableNameCountryData);
		}
		else updateCountryData(CountryData.countryDataList);
		if (checkIfTablesExist(tableNameCountryCovData)==null) {
			String countryCovidDataQuery = "CREATE TABLE IF NOT EXISTS `"+tableNameCountryCovData+"` (  `countryName` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `totalCases` INT(11) UNSIGNED NOT NULL DEFAULT '0',  `totalDeath` INT(11) UNSIGNED NOT NULL DEFAULT '0',  `totalRecovered` INT(11) UNSIGNED NOT NULL DEFAULT '0',  `totalTests` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `countryHref` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `totalCasesOnOneMil` INT(10) UNSIGNED NOT NULL DEFAULT '0',  PRIMARY KEY (`countryName`),  UNIQUE INDEX `countryName` (`countryName`),  UNIQUE INDEX `countryHref` (`countryHref`))COMMENT='Corona data for each country'COLLATE='utf8mb4_general_ci'ENGINE=InnoDB AUTO_INCREMENT=430;";
			this.insert(countryCovidDataQuery);
			saveCovidData(CountryCovidData.countryCovList);
			System.out.println("creating table "+tableNameCountryCovData);
		}
		else updateCovidData(CountryCovidData.countryCovList);
		if (checkIfTablesExist(tableNameRelCountryCovData)==null) {
			String countryAndCovidDataRelations = "CREATE TABLE IF NOT EXISTS `"+tableNameRelCountryCovData+"` (   `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,  `country_data_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',  `covid_data_name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',  PRIMARY KEY (`id`) USING BTREE)COLLATE='utf8mb4_general_ci'ENGINE=InnoDB;";
			this.insert(countryAndCovidDataRelations);
			System.out.println("creating table "+tableNameRelCountryCovData);
		}

	}
	
	public void createTableWithForeignKey() {
		String tableNameCountryDataWithForeignKey = "tbl_countrydata_foreign_key";
		if (checkIfTablesExist(tableNameCountryDataWithForeignKey)==null) {
			String countryDataQueryWithForeignKey = "CREATE TABLE IF NOT EXISTS `"+tableNameCountryDataWithForeignKey+"` (  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,  `countryName` VARCHAR(100) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',  `population` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `density` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `landArea` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `medianAge` INT(10) UNSIGNED NOT NULL DEFAULT '0',  `urbanPop` INT(10) UNSIGNED NOT NULL DEFAULT '0',  PRIMARY KEY (`id`) USING BTREE,  UNIQUE INDEX `countryName` (`countryName`) USING BTREE,  CONSTRAINT `countryName` FOREIGN KEY (`countryName`) REFERENCES `corona`.`tbl_countrycoviddata` (`countryName`) ON UPDATE RESTRICT ON DELETE RESTRICT)COMMENT='Contains data about each country'COLLATE='utf8mb4_general_ci'ENGINE=InnoDB AUTO_INCREMENT=628;";
			this.insert(countryDataQueryWithForeignKey);
			saveCountryDataWithForeignKey(CountryData.countryDataList);
			System.out.println("creating table "+tableNameCountryDataWithForeignKey);
		}
		else updateCountryDataWithForeignKey(CountryData.countryDataList);
	}
}
