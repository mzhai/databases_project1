import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class SongFileDAO {
	static String song = "song";
	static String file = "file";
	DAO dao;
	
	String tableName = Constants.songFileTableName;
	
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
	
	SongFileDAO(){
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
		    		this.file  + " TEXT" +  
		    		");"
		    		);
		    conn.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void parseFromCSV(){
		SongFileDAO dao = new SongFileDAO();
		dao.createTable();
		Parser parser = new Parser();
		parser.readFile();
		
		ArrayList<SongFile> artists = parser.songFiles;
		for(int i = 0; i<artists.size();i++){
			dao.insert(artists.get(i));
		}
		
	}
	public void insert(SongFile sa){
		insert(sa.song, sa.file);
	}
	public void insert(String song,String file){
	    PreparedStatement prep;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			prep = conn.prepareStatement(
				      "insert into " + this.tableName + "  values (?, ?);");

		    prep.setString(1, song);
		    prep.setString(2, file);
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
	public ArrayList<SongFile> parseFromDatabase(){
		ArrayList<SongFile> temp = new ArrayList<SongFile>();
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){
		    	String song = rs.getString(this.song);
		    	String file = rs.getString(this.file);
		    	temp.add(new SongFile(song,file));
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
		    	String file = rs.getString(this.file);

		    	map.put(song, file);
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return map;
	}
}
