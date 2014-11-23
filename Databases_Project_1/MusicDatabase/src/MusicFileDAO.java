import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class MusicFileDAO {
	static String size = "size";
	static String length = "length";
	static String description = "description";
	String tableName = Constants.fileTableName;
	
	Connection conn;
	DAO dao;
	public static void main(String [] args){
//		SongDAO dao = new SongDAO();
//		dao.deleteTable();
//		dao.createTable();
		MusicFileDAO dao = new MusicFileDAO();
		dao.createTable();
		dao.parseFromCSV();
	}
	
	MusicFileDAO(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void parseFromCSV(){
		MusicFileDAO dao = new MusicFileDAO();
		dao.createTable();
		Parser parser = new Parser();
		parser.readFile();
		ArrayList<MusicFile> files = parser.files;
		for(int i = 0; i<files.size();i++){
			dao.insert(files.get(i));
		}
	}
	public void createTable(){
	    Statement stat;
	    try {
	    	conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
		    stat.executeUpdate("create table if not exists " + this.tableName +  
		    		" (" + 
		    		this.size + " INTEGER," + 
		    		this.length + " INTEGER," + 
		    		this.description + " TEXT" + 
		    		");"
		    		);
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void deleteTable(){
	    dao.deleteTable(this.tableName);
	}
	public void insert(MusicFile file){
		insert(file.size, file.length, file.description);
	}
	public void insert(int size, int length, String description){
	    PreparedStatement prep;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			prep = conn.prepareStatement(
				      "insert into " + this.tableName + "  values (?, ?, ?);");

		    prep.setString(1, ((Integer)(size)).toString());
		    prep.setString(2, ((Integer)(length)).toString());
		    prep.setString(3, description);
		    
		    prep.execute();
		    conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<MusicFile> parseFromDatabase(){
		ArrayList<MusicFile> temp = new ArrayList<MusicFile>();
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){

		    	String size = rs.getString(this.size);
		    	String length = rs.getString(this.length);
		    	String description = rs.getString(this.description);
		    	temp.add(new MusicFile(Integer.parseInt(size),Integer.parseInt(length),description));
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return temp;
	}
	public HashMap<String,MusicFile> mapFromDatabase(){
		HashMap<String,MusicFile> temp = new HashMap<String,MusicFile>();
	    Statement stat=null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    ResultSet rs = stat.executeQuery("select * from " + this.tableName + ";");
		    while(rs.next()){

		    	String size = rs.getString(this.size);
		    	String length = rs.getString(this.length);
		    	String description = rs.getString(this.description);
		    	temp.put(description, new MusicFile(Integer.parseInt(size),Integer.parseInt(length),description));
		    }			
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return temp;
	}
}
