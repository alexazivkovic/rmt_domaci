package domain;

public class Trip implements java.io.Serializable{
	private int id;
	private String ime;
	private String prezime;
	private String zemlje;
	private String jmbg;
	private String brPasosa;
	private String datumUlaska;
	private String datumIzlaska;
	private String nacinPrevoza;
	private String status;
	
	public Trip(String ime, String prezime, String zemlje, String jmbg, String brPasosa, String datumUlaska, String datumIzlaska, String nacinPrevoza) {
		this.ime = ime;
		this.prezime = prezime;
		this.zemlje = zemlje;
		this.jmbg = jmbg;
		this.brPasosa = brPasosa;
		this.datumUlaska = datumUlaska;
		this.datumIzlaska = datumIzlaska;
		this.nacinPrevoza = nacinPrevoza;
	}
	
	public Trip(String jmbg, String brPasosa){
		this.jmbg = jmbg;
		this.brPasosa = brPasosa;
	}
	
	public Trip(){
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
	
	public String getZemlje(){
		return zemlje;
	}
	
	public void setZemlje(String zemlje){
		this.zemlje = zemlje;
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
	
	public String getDatumUlaska(){
		return datumUlaska;
	}
	
	public void setDatumUlaska(String datumUlaska){
		this.datumUlaska = datumUlaska;
	}
	
	public String getDatumIzlaska(){
		return datumIzlaska;
	}
	
	public void setDatumIzlaska(String datumIzlaska){
		this.datumIzlaska = datumIzlaska;
	}
	
	public String getNacinPrevoza(){
		return nacinPrevoza;
	}
	
	public void setNacinPrevoza(String nacinPrevoza){
		this.nacinPrevoza = nacinPrevoza;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
}
