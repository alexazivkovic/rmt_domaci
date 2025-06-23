package forms;

import domain.Client;
import domain.Trip;
import network.Packet;
import network.PacketType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UlogovaniIzmena extends UlogovaniPrijava{
	private Trip trip;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private JFrame instance;
	
	public UlogovaniIzmena(JFrame parent, ObjectOutputStream out, ObjectInputStream in, Client client, Trip trip){
		super(parent, out, in, client);
		this.trip = trip;
		this.out = out;
		this.in = in;
		instance = this;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		prijaviPutovanjeButton.setText("Izmeni putovanje sa ID-jem: "+trip.getId());
		
		prijaviPutovanjeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Packet packet = new Packet(PacketType.REQUEST_DELETE_TRIP, trip);
				try{
					out.writeObject(packet);
					instance.dispose();
				} catch(Exception ex){
					System.out.println(ex.getMessage());
				}
				
			}
		});
	}
}
