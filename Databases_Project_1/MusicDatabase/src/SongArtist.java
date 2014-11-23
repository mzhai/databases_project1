public class SongArtist {
	public String song = "";
	public String artist = "";
	
	SongArtist(){
		
	}
	SongArtist(String song, String artist){
		this.song = song;
		this.artist = artist;
	}
	public void create(String song, String artist){
		this.song = song;
		this.artist = artist;
	}
}