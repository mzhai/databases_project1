
public class MusicFile {
	public int size;
	public int length;
	public String description;
	
	public MusicFile(){}
	public MusicFile(int size, int length, String description){
		this.size = size;
		this.length = length;
		this.description = description;
	}
	public void create(int size, int length, String description){
		this.size = size;
		this.length = length;
		this.description = description;
	}
	
}
