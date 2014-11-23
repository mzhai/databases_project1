import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Views {
	//for the purpose of running the functions on the main screen
	
	public static void main(String [] args){
		Views view = new Views();
//		view.showSongsbyArtist(new Artist("Akon"));
		view.showSongsbyArtistJoin(new Artist("Akon"));
	}
	public Views(){
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
	//show songs by artist
	public ArrayList<String> showSongsbyArtist(String artist){
		return showSongsbyArtistJoin(new Artist(artist));
	}
	public ArrayList<String> showSongsbyArtist(Artist artist){
		//select * from songs join songArtist on songs.billBoardYear || '-' || songs.rank = songArtist.song;
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("[Song Title], [Billboard Year], [Rank], [featured Artists]");		
		String artistname = artist.name;
		
		Statement stat;
		ArrayList<Song> songs = new ArrayList<Song>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			//fetch all the songs under this artist from the songartist table
			ResultSet rs;
			rs = stat.executeQuery("select " + SongArtistDAO.song + " from " + Constants.songArtistTableName+ " where " + SongArtistDAO.artist + "='"+ artistname+ "';");
		
			while(rs.next()){
				String song = rs.getString(SongArtistDAO.song);
				String [] songInfo= song.split("-");
				songs.add(new Song(Integer.parseInt(songInfo[0]),Integer.parseInt(songInfo[1])));
			}
			
			for(int i = 0; i<songs.size();i++){
				Song tempsong = songs.get(i);
				
				rs = stat.executeQuery("select * from " + Constants.songTableName+ " where " + SongDAO.rank + "="+ tempsong.rank+ " and " + SongDAO.billboardYear+ " =" + tempsong.year + ";");
				String title = rs.getString(SongDAO.title);
				String year = rs.getString(SongDAO.billboardYear);
				String rank =rs.getString(SongDAO.rank);
				String features = rs.getString(SongDAO.features);
				
				String songinfo = title + "," + year + "," + rank + "," + features;
				temp.add(songinfo);
			}
			conn.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	public ArrayList<String> showSongsbyArtistJoin(Artist artist){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("[Song Title], [Billboard Year], [Rank], [featured Artists]");
		String artistname = artist.name;
		
		Statement stat;
		ArrayList<Song> songs = new ArrayList<Song>();
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			//fetch all the songs under this artist from the songartist table
			ResultSet rs;
//			System.out.println("select * from (select * from " + Constants.songTableName + " join " + Constants.songArtistTableName + " on " + Constants.songTableName + "." + SongDAO.billboardYear + " || '-' || "+  Constants.songTableName + "." +  SongDAO.rank + "=" + Constants.songArtistTableName + "." + SongArtistDAO.song + ") where " + SongArtistDAO.artist + "='" + artistname +"';");
			rs = stat.executeQuery("select * from (select * from " + Constants.songTableName + " join " + Constants.songArtistTableName + " on " + Constants.songTableName + "." + SongDAO.billboardYear + " || '-' || "+  Constants.songTableName + "." +  SongDAO.rank + "=" + Constants.songArtistTableName + "." + SongArtistDAO.song + ") where " + SongArtistDAO.artist + "='" + artistname +"';");
		
			while(rs.next()){
				String title = rs.getString(SongDAO.title);
				String year = rs.getString(SongDAO.billboardYear);
				String rank =rs.getString(SongDAO.rank);
				String features = rs.getString(SongDAO.features);
				
				String songinfo = title + "," + year + "," + rank + "," + features;
				temp.add(songinfo);
			}
			conn.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}

	//show artist by debut Year
	public ArrayList<String> showArtistbyYear(int year){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("[Artist Name], [Debut Year], [isGroup], [State], [Country]");
		Statement stat;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			//fetch all the songs under this artist from the songartist table
			ResultSet rs;
			rs = stat.executeQuery("select * from " + Constants.artistTableName+ " where " + ArtistDAO.debutYear + "="+ year+ ";");
		
			while(rs.next()){
				String artist = rs.getString(ArtistDAO.name);
				String country = rs.getString(ArtistDAO.country);
				String state = rs.getString(ArtistDAO.state);
				String isGroup = rs.getString(ArtistDAO.group);
				
				String artistinfo = artist + "," +year + "," + isGroup + "," + state + "," + country;
				temp.add(artistinfo);
			}
			
			conn.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	//show artist by country
	public ArrayList<String> showArtistbyCountry(String country){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("[Artist Name], [Debut Year], [isGroup], [State], [Country]");
		Statement stat;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			//fetch all the songs under this artist from the songartist table
			ResultSet rs;
			rs = stat.executeQuery("select * from " + Constants.artistTableName+ " where " + ArtistDAO.country + "='"+ country+ "';");
		
			while(rs.next()){
				String artist = rs.getString(ArtistDAO.name);
				String debutYear = rs.getString(ArtistDAO.debutYear);
				String state = rs.getString(ArtistDAO.state);
				String isGroup = rs.getString(ArtistDAO.group);
				
				String artistinfo = artist + "," +debutYear + "," + isGroup + "," + state + "," + country;
				temp.add(artistinfo);
			}
			
			conn.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	//show artist by state
	public ArrayList<String> showArtistbyState(String state){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("[Artist Name], [Debut Year], [isGroup], [State], [Country]");
		Statement stat;
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.databaseName + ".db");
			stat = conn.createStatement();
			//fetch all the songs under this artist from the songartist table
			ResultSet rs;
			rs = stat.executeQuery("select * from " + Constants.artistTableName+ " where " + ArtistDAO.state + "='"+ state+ "';");
		
			while(rs.next()){
				String artist = rs.getString(ArtistDAO.name);
				String debutYear = rs.getString(ArtistDAO.debutYear);
				String country = rs.getString(ArtistDAO.country);
				String isGroup = rs.getString(ArtistDAO.group);
				
				String artistinfo = artist + "," +debutYear + "," + isGroup + "," + state + "," + country;
				temp.add(artistinfo);
			}
			
			conn.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;	
	}
}
