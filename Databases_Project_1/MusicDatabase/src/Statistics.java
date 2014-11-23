import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Statistics{


	DAO dao;
	public double percentSongsFeaturing = 0;
	public double percentArtistsGroup = 0;
	public double averageFileSize = 0;
	public double averageLengthFiles = 0;

	public static void main(String [] args){
		Statistics stat = new Statistics();
//		Statistics stat1 = new Statistics();
		System.out.println(stat.averageFileLength());
//		System.out.println(stat.percentSongsFeaturing);
//		System.out.println(stat1.percentArtistsGroup);
//		System.out.println(stat.averageFileSize);
//		System.out.println(stat.averageLengthFiles);

	}
	public Statistics(){
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			updateStatistics();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	       
	}
	
	//percent of songs featuring mult artists
	public double multArtistPercentage(){
		double percent = 0;
		double totalartists = 0;
		double features = 0;
		Statement stat;
	    try {
			//count the total number of artists in the table
	    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
	    	stat = conn.createStatement();
		    ResultSet res = stat.executeQuery("SELECT COUNT(*) FROM " + Constants.songTableName + ";");
		    totalartists = res.getInt(1);
		    
		    res = stat.executeQuery("SELECT count(*) FROM " + Constants.songTableName + " WHERE " + SongDAO.features + " ='true';");
		    features = res.getInt(1);
		    conn.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    System.out.println("totalartists:" + totalartists);
//	    System.out.println(features);
	    //count the number of artists that feature multiple artists
		
		//divide the two numbers
		
	    percent = 100*(features/totalartists);
		return percent;
	}
	
	//percent of artists that are a group
	public double artistGroupPercentage(){
		double percent = 0;
		double totalartists = 0;
		double features = 0;
		Statement stat;
	    try {
	    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			//count the total number of artists in the table
	    	stat = conn.createStatement();
		    ResultSet res = stat.executeQuery("SELECT COUNT(*) FROM " + Constants.artistTableName + ";");
		    totalartists = res.getInt(1);
		    
		    res = stat.executeQuery("SELECT count(*) FROM " + Constants.artistTableName + " WHERE " + ArtistDAO.group + " ='true'");
		    features = res.getInt(1);
		    conn.close();
		    
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    System.out.println("totalartists:" + totalartists);
//	    System.out.println(features);
	    //count the number of artists that are in a group
		
		//divide the two numbers
		
	    percent = 100*(features/totalartists);
		return percent;
	}
	//average size of files in bytes
	public double averageFileSize(){
		double avg = 0;
		
		Statement stat;
	    try {
	    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			//count the total number of artists in the table
	    	stat = conn.createStatement();
		    ResultSet res = stat.executeQuery("SELECT AVG(" +MusicFileDAO.size+ " ) FROM " + Constants.fileTableName);
		    avg = res.getInt(1);
		    conn.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avg;
	}
	//average length of files in seconds
	public double averageFileLength(){
		double length = 0;
		
		Statement stat;
	    try {
	    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			//count the total number of artists in the table
	    	stat = conn.createStatement();
		    ResultSet res = stat.executeQuery("SELECT AVG(" +MusicFileDAO.length+ " ) FROM " + Constants.fileTableName);
		    length = res.getDouble(1);
		    conn.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return length;
	}
	
	
	public void updateStatistics(){
		percentSongsFeaturing = multArtistPercentage();
		percentArtistsGroup = artistGroupPercentage();
		averageFileSize = averageFileSize();
		averageLengthFiles = averageFileLength();
	}
}
