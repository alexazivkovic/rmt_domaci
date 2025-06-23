package domain;

public class Client implements java.io.Serializable{
	private String username;
	private String password;
	private String email;
	private String ime;
	private String prezime;
	private String jmbg;
	private String brPasosa;
	
	public Client(String username, String password, String email, String firstName, String lastName, String jmbg, String brPasosa) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.ime = firstName;
		this.prezime = lastName;
		this.jmbg = jmbg;
		this.brPasosa = brPasosa;
	}
	
	public Client(){
	}
	
	public Client(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getIme(){
		return ime;
	}
	
	public void setIme(String ime){
		this.ime = ime;
	}
	
	public String getPrezime(){
		return prezime;
	}
	
	public void setPrezime(String prezime){
		this.prezime = prezime;
	}
	
	public String getJmbg(){
		return jmbg;
	}
	
	public void setJmbg(String jmbg){
		this.jmbg = jmbg;
	}
	
	public String getBrPasosa(){
		return brPasosa;
	}
	
	public void setBrPasosa(String brPasosa){
		this.brPasosa = brPasosa;
	}
}
