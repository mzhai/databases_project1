public class SongFile {
	public String song = "";
	public String file = "";
	
	SongFile(){
		
	}
	SongFile(String song, String file){
		this.song = song;
		this.file = file;
	}
	public void create(String song, String file){
		this.song = song;
		this.file = file;
	}
}
