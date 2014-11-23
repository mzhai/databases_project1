
public class Artist{
	public String name = "";
	public boolean isGroup = false;
	public int debutYear;
	public String state = "";
	public String country = "";
	

	public Artist(){}
	public Artist(String artist){
		name = artist;
	}
	public Artist(String name,boolean isGroup, int debutYear, String state, String country){
		this.name = name;
		this.isGroup = isGroup;
		this.debutYear = debutYear;
		this.state = state;
		this.country = country;
	}
	public void create(String name,boolean isGroup, int debutYear, String state, String country){
		this.name = name;
		this.isGroup = isGroup;
		this.debutYear = debutYear;
		this.state = state;
		this.country = country;
	}
	public boolean equals(Object b){
		return this.name.equals(((Artist)b).name);
	}
	public String toString(){
		return name + "," + isGroup + "," +debutYear + "," +state + "," +country;
	}
	
}
