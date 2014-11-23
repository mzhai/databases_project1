
public class Song {
	public int rank = 0;
	public int year= 0;
	public String songName= "";
	public boolean features = false;
	
	public Song(){}
	public Song(int year, int rank){
		this.year = year;
		this.rank = rank;
		
	}
	public Song(int rank, int year, String songName, boolean feature){
		
		this.rank = rank;
		this.year = year;
		this.songName= songName;
		this.features = feature;
	}
	public void create(int rank, int year, String songName, boolean feature){
	
		this.rank = rank;
		this.year = year;
		this.songName= songName;
		this.features = feature;
	}
	public String toString(){
		return rank+" "+year+" "+songName+" "+features;
	}

}
