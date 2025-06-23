package forms;

import domain.Client;
import domain.Trip;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UlogovaniPregled extends NeregistrovaniPregled{
	private Client client;
	private UlogovaniPregled instance;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public UlogovaniPregled(JFrame parent, ObjectOutputStream out, ObjectInputStream in, Client client){
		super(parent, out, in);
		this.client = client;
		instance = this;
		this.out = out;
		this.in = in;
		
		textField1.setText(client.getJmbg());
		textField1.setEditable(false);
		textField2.setText(client.getBrPasosa());
		textField2.setEditable(false);
		textField3.setVisible(true);
		izmeniPutovanjeZaUnetiButton.setVisible(true);
		
		izmeniPutovanjeZaUnetiButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try{
					int id = Integer.parseInt(textField3.getText());
					Trip trip = null;
					for(Trip t : trips){
						if(t.getId() == id)
							trip = t;
					}
						if(trip==null){
							JOptionPane.showMessageDialog(instance, "Putovanje sa zadanim ID-jem ne postoji", "Greska", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(!trip.getStatus().equals("U obradi")){
							JOptionPane.showMessageDialog(instance, "Putovanje sa zadanim ID-jem vise nije moguce izmeniti", "Greska", JOptionPane.ERROR_MESSAGE);
							return;
						}
						new UlogovaniIzmena(instance, out, in, client, trip).setVisible(true);
					
				}catch(Exception ex){
					JOptionPane.showMessageDialog(UlogovaniPregled.this, "Unesite ispravan broj putovanja", "Greska", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
