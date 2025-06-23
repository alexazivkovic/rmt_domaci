package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBCon{
	private static DBCon instance;
	private Connection con;
	
	private DBCon(){
		try{
			InputStream input = getClass().getClassLoader().getResourceAsStream("db/db.properties");
			Properties props = new Properties();
			props.load(input);
			con = DriverManager.getConnection(
					props.getProperty("url"),
					props.getProperty("username"),
					props.getProperty("password")
			);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static DBCon getInstance(){
		if(instance == null)
			instance = new DBCon();
		return instance;
	}
	
	public Connection getConnection(){
		return con;
	}
}
