import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JTable;


public class Parser {
	ArrayList <Artist> artists = new ArrayList <Artist>();
	ArrayList <Song> songs = new ArrayList <Song>();
	ArrayList <MusicFile> files = new ArrayList<MusicFile>();
	ArrayList <SongFile> songFiles = new ArrayList<SongFile>();
	ArrayList <SongArtist> songArtists = new ArrayList<SongArtist>();
	 
	public static void main(String [] args){
//		Parser p = new Parser();
//		p.readFile();
//		System.out.println(p.artists.size());
//		Parser.populateDatabase();
		DAO dao = new DAO();
		Parser p = new Parser();
		p.writeThisFile("testing2.csv");
//		dao.populateDatabase();
	}

	public void readFile(){
		if(!(artists.size()==0 && songs.size()==0 && files.size()==0
				&& songFiles.size()==0 && songArtists.size()==0)){
			return;
		}
		readThisFile("input.csv");
	}
	public void readThisFile(String filename){
		File file = new File(filename);
		Artist a = null;
		Song s = null;
		MusicFile f = null;
		SongArtist sa = null;
		SongFile sf = null;
		
		String line = null;
		Object [] data;
		
		try{
			BufferedReader buf = new BufferedReader(new FileReader(file));
			buf.readLine();
			while((line = buf.readLine()) != null){
				a = new Artist();
				s = new Song();
				f = new MusicFile();
				sf = new SongFile();
				sa = new SongArtist();
				data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				
				//Artist(String name,boolean isGroup, int debutYear, String state, String country)
				//Song: Song(int rank, int year, String songName, boolean feature)
				//MusicFile(int size, int length, String description)
				//Rank, Year, Name, Artist, Features, Size, Time, Group, Debut Year, State, Country
				//1,2006,Bad Day,Daniel Powter,false,6463488,230,false,2000,,Canada
				
				//SongFile(String song, String file)
				//SongArtist(String song, String artist);
				if((String)data[9] == null || ((String)data[9]).compareTo("")==0){
					data[9] = "n/a";
				}
				
				a.create((String)data[3],Boolean.parseBoolean((String)data[7]), Integer.parseInt(((String)data[8])), (String)data[9], (String)data[10]);
				s.create(Integer.parseInt((String)data[0]), Integer.parseInt((String)data[1]), (String)data[2], Boolean.parseBoolean((String)data[4]));
				f.create(Integer.parseInt((String)data[5]), Integer.parseInt((String)data[6]), 
						(String)data[3]+"-"+(String)data[2]+"-"+(String)data[1]);
				
				sa.create((String)data[1] + "-" +  (String)data[0],(String)data[3]);
				sf.create((String)data[1] + "-" +  (String)data[0],(String)data[3]+"-"+(String)data[2]+"-"+(String)data[1]);
				
				
				
				//System.out.println(Arrays.toString(data));
				if(!artists.contains(a)){
					artists.add(a);					
				}

				songs.add(s);
				files.add(f);
				songArtists.add(sa);
				songFiles.add(sf);
			}
			buf.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void writeThisFile(String filename){
		ArtistDAO dao = new ArtistDAO();
		MusicFileDAO dao1 = new MusicFileDAO();
		SongDAO dao2 = new SongDAO();
		SongFileDAO dao3 = new SongFileDAO();
		SongArtistDAO dao4 = new SongArtistDAO();
		
		ArrayList<Song> songs =  dao2.parseFromDatabase();
		HashMap<String,MusicFile> files = dao1.mapFromDatabase();//dao1.parseFromDatabase();
		HashMap<String,Artist> artists = dao.mapFromDatabase();//dao.parseFromDatabase();
		HashMap<String,String> songFile = dao3.mapFromDatabase();
		HashMap<String,String> songArtist = dao4.mapFromDatabase();
		
		try{
			// Create file 
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Rank,Year,Name,Artist,Features,Size,Time,Group,Debut Year,State,Country\n");
			
			for(int i = 0; i<songs.size();i++){
				Song tempsong = songs.get(i);
				String key = tempsong.year + "-" + tempsong.rank;
				MusicFile tempfile = files.get(songFile.get(key));
				Artist tempartist = artists.get(songArtist.get(key));
				
				if(tempsong.songName.contains(",") || tempsong.songName.contains("\"") || tempsong.songName.contains("'")){
					tempsong.songName = tempsong.songName.replaceAll("'", " ");
					tempsong.songName = tempsong.songName.replaceAll(",", "");
					tempsong.songName = tempsong.songName.replaceAll("\"", "");
				}
				out.write(tempsong.rank+"," + tempsong.year+"," +  tempsong.songName+"," +  
						songArtist.get(key)+"," +  tempsong.features+"," +  
						tempfile.size+"," + 
						tempfile.length+"," +  
						tempartist.isGroup+"," +  
						tempartist.debutYear+"," +  tempartist.state+"," +  tempartist.country + "\n");
			}
			
			out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}	
}
