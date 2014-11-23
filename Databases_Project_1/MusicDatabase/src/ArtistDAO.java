import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class ArtistDAO {
	static String name = "Artist";
	static String group = "isGroup";
	static String debutYear = "debutYear";
	static String state = "state";
	static String country = "country";
	String tableName = Constants.artistTableName;
	DAO dao;
	
	public static void main(String [] args){

		ArtistDAO dao = new ArtistDAO();
		ArrayList<Artist> list = dao.parseFromDatabase();
		for(int i = 0; i <list.size();i++){
			System.out.println(list.get(i));
		}
	}
	
	ArtistDAO(){
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
		    		this.name + " TEXT," + 
		    		this.debutYear + " INTEGER," + 
		    		this.group  + " BOOLEAN," +  
		    		this.state + " TEXT," +
		    		this.country + " TEXT" +
		    		");"
		    		);
		    conn.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void parseFromCSV(){
		ArtistDAO dao = new ArtistDAO();
		dao.createTable();
		Parser parser = new Parser();
		parser.readFile();
		ArrayList<Artist> artists = parser.artists;
		for(int i = 0; i<artists.size();i++){
			dao.insert(artists.get(i));
		}
	}
	public void insert(Artist artist){
//		System.out.println(artist.name);
//		System.out.println(artist.debutYear);
//		System.out.println(artist.isGroup);
		insert(artist.name,artist.debutYear, artist.isGroup, artist.state, artist.country);
	}
	public void insert(String name, int year, boolean group , String state, String country){
	    PreparedStatement prep;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			prep = conn.prepareStatement(
				      "insert into " + this.tableName + "  values (?, ?, ?, ?, ?);");

		    prep.setString(1, name);
		    prep.setString(2, ((Integer)(year)).toString());
		    prep.setString(3, ((Boolean)(group)).toString());
		    prep.setString(4, state);
		    prep.setString(5, country);
		    prep.execute();
//		    conn.setAutoCommit(true);
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
	public ArrayList<Artist> parseFromDatabase(){
		ArrayList<Artist> temp = new ArrayList<Artist>();
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){
		    	String name = rs.getString(this.name);
		    	String isGroup = rs.getString(this.group);
		    	int debutYear = rs.getInt(this.debutYear);
		    	String state = rs.getString(this.state);
		    	String country = rs.getString(this.country);
		    	temp.add(new Artist(name,Boolean.parseBoolean(isGroup),debutYear,state,country));
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return temp;
	}
	public HashMap<String,Artist> mapFromDatabase(){
		HashMap<String,Artist> temp = new HashMap<String,Artist>();
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){

		    	String name = rs.getString(this.name);
		    	String isGroup = rs.getString(this.group);
		    	int debutYear = rs.getInt(this.debutYear);
		    	String state = rs.getString(this.state);
		    	String country = rs.getString(this.country);
		    	temp.put(name,new Artist(name,Boolean.parseBoolean(isGroup),debutYear,state,country));
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return temp;
	}
}
