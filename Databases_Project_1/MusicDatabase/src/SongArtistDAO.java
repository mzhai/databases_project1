import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class SongArtistDAO {
	static String song = "song";
	static String artist = "artist";
	
	
	static String tableName = Constants.songArtistTableName;
	DAO dao;
	
	public static void main(String [] args){

//		ArtistDAO dao = new ArtistDAO();
//		dao.deleteTable();
//		dao.createTable();	
//
//		ArtistParser parser = new ArtistParser();
//		parser.readFile();
//		Object [] keys = parser.artists.keySet().toArray();
//		for(int i = 0; i<keys.length;i++){
//			Artist temp = parser.artists.get(keys[i]);
//			dao.insert(temp);
//		}
//		
	}
	
	SongArtistDAO(){
		try {
			Class.forName("org.sqlite.JDBC");
			dao = new DAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	       
	}
	public void createTable(){
	    Statement stat;
	    try {
	    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
		    stat.executeUpdate("create table if not exists " + this.tableName +  
		    		" (" + 
		    		this.song + " TEXT," + 
		    		this.artist  + " TEXT" +  
		    		");"
		    		);
		    conn.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void parseFromCSV(){
		SongArtistDAO dao = new SongArtistDAO();
		dao.createTable();
		Parser parser = new Parser();
		parser.readFile();
		
		ArrayList<SongArtist> artists = parser.songArtists;
		for(int i = 0; i<artists.size();i++){
			dao.insert(artists.get(i));
		}
		
	}
	public void insert(SongArtist sa){
		insert(sa.song, sa.artist);
	}
	public void insert(String song,String artist){
	    PreparedStatement prep;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			prep = conn.prepareStatement(
				      "insert into " + this.tableName + "  values (?, ?);");

		    prep.setString(1, song);
		    prep.setString(2, artist);
		    prep.execute();
		    conn.close();
		    prep.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteTable(){
	    dao.deleteTable(this.tableName);
	}
	public ArrayList<SongArtist> parseFromDatabase(){
		ArrayList<SongArtist> temp = new ArrayList<SongArtist>();
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){

		    	String song = rs.getString(this.song);
		    	String artist = rs.getString(this.artist);
		    	temp.add(new SongArtist(song,artist));
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return temp;
	}
	public HashMap<String,String> mapFromDatabase(){
		HashMap<String,String> map = new HashMap<String,String>();
		
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){
		    	String song = rs.getString(this.song);
		    	String artist = rs.getString(this.artist);

		    	map.put(song, artist);
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return map;
	}

}


