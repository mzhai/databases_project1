import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class FileDAO {
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
		FileDAO dao = new FileDAO();
		dao.createTable();
		dao.parseFromCSV();
	}
	
	FileDAO(){
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			dao = new DAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void parseFromCSV(){
		FileDAO dao = new FileDAO();
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
			prep = conn.prepareStatement(
				      "insert into " + this.tableName + "  values (?, ?, ?);");

		    prep.setString(1, ((Integer)(size)).toString());
		    prep.setString(2, ((Integer)(length)).toString());
		    prep.setString(3, description);
		    
		    prep.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
