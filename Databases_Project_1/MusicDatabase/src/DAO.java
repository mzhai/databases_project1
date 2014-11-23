import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DAO {
	public static void main(String [] args){
		DAO dao = new DAO();
//		dao.addSong("songName1", 1990, 6, true, "Akon",1919, true, "md", "mda", 12, 123);
//		dao.addSong("songName", 1990, 1, true, "bill",1919, true, "md", "mda", 12, 123);
		
		dao.deleteSong(new Song(2006,2));
//		dao.deleteSong(new Song(2007,4));
//		dao.populateDatabase();
//		System.out.println(dao.artistExists(new Artist("Akon")));
//		dao.deleteArtist("Akon");
//		dao.deleteArtist("Beyonce");
//		dao.deleteArtist("Blue October");
//		dao.deleteArtist("Brad Paisley");
//		dao.deleteArtist("Alicia Keys");
//		dao.deleteArtist("Carrie Underwood");
//		dao.deleteArtist("Britney Spears");
//		dao.deleteArtist("Chris Brown");
//		System.out.println(dao.songExists(new Song(2006,1)));
//		dao.deleteSong(new Song(2006,1));
	}
	
	DAO(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
	public boolean artistExists(Artist artist){
		String name = artist.name;
		Statement stat;
	    try {
	    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			//count the total number of artists in the table
	    	stat = conn.createStatement();
		    ResultSet res;
		    
		    res = stat.executeQuery("SELECT count(*) FROM " + Constants.artistTableName + " WHERE " + ArtistDAO.name + " = '" + name + "';");
		    int count = res.getInt(1);
		    
		    if(count >=1){
			    conn.close();
			    res.close();
			    stat.close();

		    	return true;
		    }
		    conn.close();
		    res.close();
		    stat.close();
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}
	public boolean songExists(Song song){
		int rank = song.rank;
		int year = song.year;
		
		Statement stat;
	    try {
	    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			//count the total number of artists in the table
	    	stat = conn.createStatement();
		    ResultSet res;
		    
		    res = stat.executeQuery("SELECT count(*) FROM " + Constants.songTableName + " WHERE " + SongDAO.rank + " =" + rank + " and " + SongDAO.billboardYear + " =" + year + ";");
		    int count = res.getInt(1);
		    
		    if(count >=1){
		    	return true;
		    }
		    conn.close();
		    
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}
	
	//check for repeating artist
	public boolean addSong(String songName, int billboardYear, int rank, boolean features,String artist, int debutYear, boolean isGroup, String state, String country, int size, int length){
		if(songName==null || songName=="" || billboardYear<=0 || rank <=0 
				|| artist ==null  || artist==""|| debutYear<=0 
				|| state==null || state=="" || country==null || country==""|| size<=0 || length<=0
				|| billboardYear<=0 || rank >100){
			return false;
		}
		
		//also check database for rank/year pre-existing
		if(songExists(new Song(billboardYear,rank))){
			return false;
		}
		try{
			Artist a = new Artist();
			Song s = new Song();
			MusicFile f = new MusicFile();
			
			SongArtist sa = new SongArtist();
			SongFile sf = new SongFile();
			
			a.create(artist, isGroup, debutYear, state, country);
			s.create(rank, billboardYear, songName, features);
			f.create(size, length, artist + "-" + songName +"-" + billboardYear);
			sa.create(billboardYear + "-" + rank, artist);
			sf.create(billboardYear + "-" + rank, artist + "-" + songName +"-" + billboardYear);
			
			ArtistDAO dao = new ArtistDAO();
			SongDAO dao1 = new SongDAO();
			MusicFileDAO dao2 = new MusicFileDAO();
			SongArtistDAO dao3 = new SongArtistDAO();
			SongFileDAO dao4 = new SongFileDAO();
			
			//check if artist already exists
			if(!artistExists(new Artist(artist))){
				dao.insert(a);
			}else{
			}
			dao1.insert(s);
			dao2.insert(f);
			dao3.insert(sa);
			dao4.insert(sf);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public boolean deleteSong(Song song){		
		int rank = song.rank;
		int year = song.year;
		
		Statement stat;
		try {
			//delete from song table
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			ResultSet rs;
	
			//gets song description
			rs = stat.executeQuery("select " + SongFileDAO.file + " from " + Constants.songFileTableName + " where " + SongFileDAO.song + "='" +song.year + "-" + song.rank+ "';");
			String filedescription = rs.getString(SongFileDAO.file);
			
			
			//			System.out.println(filedescription);
			
			//have to delete from artist table
			//must fetch artist name from relation table

			
			rs = stat.executeQuery("select " + SongFileDAO.file + " from " + Constants.songFileTableName + " where " + SongFileDAO.song + "='" +song.year + "-" + song.rank+ "';");
			filedescription = rs.getString(SongFileDAO.file);

			rs = stat.executeQuery("select " + SongArtistDAO.artist + " from " + Constants.songArtistTableName + " where " + SongArtistDAO.song + "='" +song.year + "-" + song.rank+ "';");
			String artist = rs.getString(SongArtistDAO.artist);
			
			rs =stat.executeQuery("select count(*) from " + Constants.songArtistTableName +" where " + SongArtistDAO.artist + "='"+ artist+  "';"); 
			int count = rs.getInt(1);			
			
			stat.executeUpdate("delete from " + Constants.songTableName + " where " + SongDAO.billboardYear + "=" + year + " and " + SongDAO.rank + " =" + rank + ";");
			
//			System.out.println("delete from " + Constants.fileTableName + " where " + MusicFileDAO.description + "='" +filedescription+ "';");
			stat.executeUpdate("delete from " + Constants.fileTableName + " where " + MusicFileDAO.description + "='" +filedescription+ "';");
			
			//now delete from song-artist table
//			System.out.println("delete from " + Constants.songArtistTableName + " where " + SongArtistDAO.song + "='" +song.year + "-" + song.rank+ "';");
			stat.executeUpdate("delete from " + Constants.songArtistTableName + " where " + SongArtistDAO.song + "='" +song.year + "-" + song.rank+ "';");
			
			//now delete from song-file table
			stat.executeUpdate("delete from " + Constants.songFileTableName + " where " + SongFileDAO.file + "='"+ filedescription + "';");
				
			//now delete from artist table only if it's the only song left
			
			if(count==1){
				stat.executeUpdate("delete from " + Constants.artistTableName + " where " + SongArtistDAO.artist + "='" +artist+"';");				
			}

			
			conn.close();
			rs.close();
			stat.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public boolean deleteArtist(String artist){
		return deleteArtist(new Artist(artist));
	}
	public boolean deleteArtist(Artist artist){
		String artistname = artist.name;
		
		Statement stat;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			//fetch all the songs under this artist from the songartist table
			ResultSet rs;
			rs = stat.executeQuery("select " + SongArtistDAO.song + " from " + Constants.songArtistTableName+ " where " + SongArtistDAO.artist + "='"+ artistname+ "';");
//			System.out.println("select " + SongArtistDAO.song + " from " + Constants.songArtistTableName+ " where " + SongArtistDAO.artist + "='"+ artistname+ "';");
			
			//			conn.close();			
			
			ArrayList<Song> songs = new ArrayList<Song>();
		
			while(rs.next()){
				String song = rs.getString(SongArtistDAO.song);
				String [] songInfo= song.split("-");
				
				songs.add(new Song(Integer.parseInt(songInfo[0]), Integer.parseInt(songInfo[1])));
		
			}

			
			for(int i = 0; i<songs.size();i++){
//				System.out.println(songs.get(i).toString());
				deleteSong(songs.get(i));
			}
			conn.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public boolean deleteFile(MusicFile file){
		//delete the song and artist if only 1 file
		//get the rank and year of the song and then call the delete song method
		
		Statement stat;
		try {
			String filedescription =file.description;
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			//fetch all the songs under this artist from the songartist table
			ResultSet rs;
			rs = stat.executeQuery("select " + SongFileDAO.song + " from " + Constants.songFileTableName+ " where " + SongFileDAO.file + "='"+ filedescription+ "';");
			String [] data = rs.getString(SongFileDAO.song).split("-");
			conn.close();
			rs.close();
			stat.close();
			
			deleteSong(new Song(Integer.parseInt(data[0]),Integer.parseInt(data[1])));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	public void deleteDatabase(){
		Statement stat;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
		    stat.executeUpdate("drop table " + Constants.artistTableName  + ";");
		    stat.executeUpdate("drop table " + Constants.fileTableName  + ";");
		    stat.executeUpdate("drop table " + Constants.songTableName  + ";");
		    stat.executeUpdate("drop table " + Constants.songArtistTableName  + ";");
		    stat.executeUpdate("drop table " + Constants.songFileTableName  + ";");
		    conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		File back = new File("library.db");
//		back.delete();
	}
	public void deleteTable(String tableName){
		Statement stat;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
		    stat.executeUpdate("drop table if exists " + tableName  + ";");
		    conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList getAllRecords(String tableName){
	    ArrayList array = new ArrayList();
		Statement stat;
	    ResultSet rs = null;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();

		    rs = stat.executeQuery("select * from " + tableName + ";");
	    	ResultSetMetaData data = rs.getMetaData();
	    	data.getColumnCount();
		    while (rs.next()) {
		    	Object [] temp = new Object[data.getColumnCount()];
		    	for(int i = 0; i<data.getColumnCount();i++){
		    		temp[i] =rs.getObject(i+1); 
		    	}
		    	array.add(temp);
		    }
		    rs.close();
		    conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return array;
	}
	public void populateDatabase(){
//		System.out.println("p");
	  	File libraryFile = new File("library.db");
		if(libraryFile.exists()){
			System.out.println("poo");
    		deleteDatabase();
		}
		
		MusicFileDAO dao = new MusicFileDAO();
		dao.createTable();
		dao.parseFromCSV();
		
		ArtistDAO dao1 = new ArtistDAO();
		dao1.createTable();
		dao1.parseFromCSV();
		
		SongDAO dao2 = new SongDAO();
		dao2.createTable();
		dao2.parseFromCSV();
		
		SongFileDAO dao3 = new SongFileDAO();
		dao3.createTable();
		dao3.parseFromCSV();
		
		SongArtistDAO dao4 = new SongArtistDAO();
		dao4.createTable();
		dao4.parseFromCSV();
		
	}
	public Object [][] readDatabaseTable(String tableName){
		Object [][] data = null;
		
		ArrayList temp = getAllRecords(tableName);
		int rows = temp.size();
		int cols = ((Object [])(temp.get(0))).length;
		data = new Object[rows][cols];
		
		for(int i = 0; i<temp.size();i++){
			data[i] = (Object[]) temp.get(i); 
		}
		return data;
	}
}
