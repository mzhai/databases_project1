import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SongDAO {
	static String title = "title";
	static String billboardYear = "billboardYear";
	static String rank = "rank";
	static String features = "features";
	static String tableName = Constants.songTableName;
	
	DAO dao;
	public static void main(String [] args){
//		SongDAO dao = new SongDAO();
//		dao.deleteTable();
//		dao.createTable();
//
//		SongParser parser = new SongParser();
//		parser.readFile();
//		ArrayList<Song> songs = parser.songs;
//		for(int i = 0; i <songs.size();i++){
//			dao.insert(songs.get(i));
//		}
//		

	}
	
	SongDAO(){
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
		    		this.title + " TEXT," + 
		    		this.billboardYear + " INTEGER," + 
		    		this.rank + " INTEGER," + 
		    		this.features  + " BOOLEAN" +
		    		");"
		    		);
		    conn.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void deleteTable(){
	    dao.deleteTable(this.tableName);
	}
	public void insert(Song song){
		insert(song.songName, song.year, song.rank,song.features);
	}
	public void insert(String name, int year, int rank, boolean features){
	    PreparedStatement prep;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			prep = conn.prepareStatement(
				      "insert into " + this.tableName + "  values (?, ?, ?, ?);");

		    prep.setString(1, name);
		    prep.setString(2, ((Integer)(year)).toString());
		    prep.setString(3, ((Integer)(rank)).toString());
		    prep.setString(4, ((Boolean)(features)).toString());
		    
		    prep.execute();
//		    conn.setAutoCommit(true);
		    conn.close();
		    prep.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void parseFromCSV(){
		SongDAO dao = new SongDAO();
		dao.createTable();
		Parser parser = new Parser();
		parser.readFile();
		ArrayList<Song> songs = parser.songs;
		for(int i = 0; i<songs.size();i++){
			dao.insert(songs.get(i));
		}
	}
	public ArrayList<Song> parseFromDatabase(){
		ArrayList<Song> temp = new ArrayList<Song>();
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){
		    	

		    	int rank = rs.getInt(this.rank);
		    	int year = rs.getInt(this.billboardYear);
		    	String song = rs.getString(this.title);
		    	String features = rs.getString(this.features);
		    	temp.add(new Song(rank, year, song, Boolean.parseBoolean(features)));
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return temp;
	}

}