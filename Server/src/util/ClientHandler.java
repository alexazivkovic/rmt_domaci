package util;

import db.DBCon;
import domain.Client;
import domain.Trip;
import network.Packet;
import network.PacketType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Connection con;
    private ResultSet resultSet;
    private PreparedStatement ps;
    private Packet p;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            con = DBCon.getInstance().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
	    while(!socket.isClosed()){
            try{
                Packet packet = (Packet) in.readObject();
                switch(packet.getType()){
                    case REQUEST_ADD_TRIP:
                        try{
                            Trip trip = (Trip) packet.getData();
                            ps = con.prepareStatement("SELECT * FROM putovanje WHERE jmbg=? AND zemlje=? AND datum_ulaska=? AND datum_izlaska=?");
                            ps.setString(1, trip.getJmbg());
                            ps.setString(2, trip.getZemlje());
                            ps.setString(3, trip.getDatumUlaska());
                            ps.setString(4, trip.getDatumIzlaska());
                            resultSet = ps.executeQuery();
                            if(resultSet.next()){
                                p = new Packet(PacketType.RESPONSE_CODE_ERROR, "This trip already exists!");
                                out.writeObject(p);
                                break;
                            }
                            
                            ps = con.prepareStatement("SELECT * FROM validacija WHERE jmbg=? AND br_pasosa=? AND ime=? AND prezime=?");
                            ps.setString(1, trip.getJmbg());
                            ps.setString(2, trip.getBrPasosa());
                            ps.setString(3, trip.getIme());
                            ps.setString(4, trip.getPrezime());
                            resultSet = ps.executeQuery();
                            if(!resultSet.next()){
                                p = new Packet(PacketType.RESPONSE_CODE_ERROR, "Personal data is invalid");
                                out.writeObject(p);
                                break;
                            }
                            
                            ps = con.prepareStatement("INSERT INTO putovanje(jmbg,broj_pasosa,ime,prezime,zemlje,datum_ulaska,datum_izlaska,nacin_prevoza) VALUES (?,?,?,?,?,?,?,?)");
                            ps.setString(1, trip.getJmbg());
                            ps.setString(2, trip.getBrPasosa());
                            ps.setString(3, trip.getIme());
                            ps.setString(4, trip.getPrezime());
                            ps.setString(5, trip.getZemlje());
                            ps.setString(6, trip.getDatumUlaska());
                            ps.setString(7, trip.getDatumIzlaska());
                            ps.setString(8, trip.getNacinPrevoza());
                            ps.executeUpdate();
                            
                            String responseFile = "Ime: " + trip.getIme() + "\nPrezime: " + trip.getPrezime() + "\nJMBG: " + trip.getJmbg() + "\nBroj pasosa: " + trip.getBrPasosa() + "\nPrijavljenje zemlje: " + trip.getZemlje() + "\nDatum ulaska u EU: " + trip.getDatumUlaska() + "\nDatum izlaska iz EU: " + trip.getDatumIzlaska() + "\nNacin prevoza: " + trip.getNacinPrevoza();
                            if(DateUtils.isAgeBetween18And70(trip.getJmbg()))
                                responseFile += "\nNeophodna uplata: DA";
                            else
                                responseFile += "\nNeophodna uplata: NE";
                            
                            p = new Packet(PacketType.RESPONSE_CODE_OK, responseFile);
                            out.writeObject(p);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                        
                    case REQUEST_GET_TRIPS:
                        Trip trip = (Trip) packet.getData();
                        ps = con.prepareStatement("SELECT * FROM putovanje WHERE jmbg=? AND broj_pasosa=?");
                        ps.setString(1, trip.getJmbg());
                        ps.setString(2, trip.getBrPasosa());
                        resultSet = ps.executeQuery();
                        ArrayList<Trip> trips = new ArrayList<Trip>();
                        while(resultSet.next()){
                            Trip t = new Trip();
                            t.setId(resultSet.getInt("id"));
                            t.setIme(resultSet.getString("ime"));
                            t.setPrezime(resultSet.getString("prezime"));
                            t.setJmbg(resultSet.getString("jmbg"));
                            t.setBrPasosa(resultSet.getString("broj_pasosa"));
                            t.setZemlje(resultSet.getString("zemlje"));
                            t.setDatumUlaska(resultSet.getString("datum_ulaska"));
                            t.setDatumIzlaska(resultSet.getString("datum_izlaska"));
                            t.setNacinPrevoza(resultSet.getString("nacin_prevoza"));
                            
                            Date now = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                            Date entryDate = sdf.parse(resultSet.getString("datum_ulaska"));
                            Date exitDate = sdf.parse(resultSet.getString("datum_izlaska"));
                            
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(now);
                            calendar.add(Calendar.HOUR, 48);
                            Date in48Hours = calendar.getTime();
                            
                            if(exitDate.before(now)){
                                t.setStatus("Zavrsen");
                            } else if(entryDate.before(in48Hours) || now.after(entryDate)){
                                t.setStatus("Zakljucan");
                            } else{
                                t.setStatus("U obradi");
                            }
                            
                            trips.add(t);
                        }
                        p = new Packet(PacketType.RESPONSE_CODE_OK, trips);
                        out.writeObject(p);
                        break;
                        
                    case REQUEST_REGISTER:
                        try{
                            Client client = (Client) packet.getData();
                            ps = con.prepareStatement("SELECT * FROM korisnik WHERE username=?");
                            ps.setString(1, client.getUsername());
                            resultSet = ps.executeQuery();
                            if(resultSet.next()){
                                p = new Packet(PacketType.RESPONSE_CODE_ERROR, "Username already in use!");
                                out.writeObject(p);
                                break;
                            }
                            
                            ps = con.prepareStatement("SELECT * FROM korisnik WHERE jmbg=?");
                            ps.setString(1, client.getJmbg());
                            resultSet = ps.executeQuery();
                            if(resultSet.next()){
                                p = new Packet(PacketType.RESPONSE_CODE_ERROR, "User already exists!");
                                out.writeObject(p);
                                break;
                            }
                            
                            ps = con.prepareStatement("SELECT * FROM validacija WHERE jmbg=? AND br_pasosa=? AND ime=? AND prezime=?");
                            ps.setString(1, client.getJmbg());
                            ps.setString(2, client.getBrPasosa());
                            ps.setString(3, client.getIme());
                            ps.setString(4, client.getPrezime());
                            resultSet = ps.executeQuery();
                            if(!resultSet.next()){
                                p = new Packet(PacketType.RESPONSE_CODE_ERROR, "Personal data is invalid");
                                out.writeObject(p);
                                break;
                            }
                            
                            ps = con.prepareStatement("INSERT INTO korisnik(username,password,ime,prezime,jmbg,broj_pasosa,email) VALUES (?,?,?,?,?,?,?)");
                            ps.setString(1, client.getUsername());
                            ps.setString(2, client.getPassword());
                            ps.setString(3, client.getIme());
                            ps.setString(4, client.getPrezime());
                            ps.setString(5, client.getJmbg());
                            ps.setString(6, client.getBrPasosa());
                            ps.setString(7, client.getEmail());
                            ps.executeUpdate();
                            
                            p = new Packet(PacketType.RESPONSE_CODE_OK, "Registration successful!");
                            out.writeObject(p);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                        
                    case REQUEST_LOGIN:
                        try{
                            Client client = (Client) packet.getData();
                            ps = con.prepareStatement("SELECT * FROM korisnik WHERE username=? AND password=?");
                            ps.setString(1, client.getUsername());
                            ps.setString(2, client.getPassword());
                            resultSet = ps.executeQuery();
                            if(resultSet.next()){
                                client.setIme(resultSet.getString("ime"));
                                client.setPrezime(resultSet.getString("prezime"));
                                client.setJmbg(resultSet.getString("jmbg"));
                                client.setBrPasosa(resultSet.getString("broj_pasosa"));
                                client.setEmail(resultSet.getString("email"));
                                p = new Packet(PacketType.RESPONSE_CODE_OK, client);
                                out.writeObject(p);
                            }else{
                                p = new Packet(PacketType.RESPONSE_CODE_ERROR, "Invalid username or password!");
                                out.writeObject(p);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                        
                    case REQUEST_DELETE_TRIP:
                        try{
                            Trip trip1 = (Trip) packet.getData();
                            ps = con.prepareStatement("DELETE FROM putovanje WHERE id=?");
                            ps.setInt(1, trip1.getId());
                            ps.executeUpdate();
                            
                            /*p = new Packet(PacketType.RESPONSE_CODE_OK, "Trip deleted successfully!");
                            out.writeObject(p);*/
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
            } catch(Exception e){
                System.out.println("Konekcija zatvorena");
                System.out.println(e.getMessage());
                break;
            }
	    }
    }
}
