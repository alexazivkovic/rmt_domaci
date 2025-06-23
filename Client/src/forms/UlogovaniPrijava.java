package forms;

import domain.Client;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UlogovaniPrijava extends NeregistrovaniPrijava{
	private Client client;
	
	public UlogovaniPrijava(JFrame parent, ObjectOutputStream out, ObjectInputStream in, Client client){
		super(parent, out, in);
		this.client = client;
		
		textField1.setText(client.getIme());
		textField1.setEditable(false);
		textField2.setText(client.getPrezime());
		textField2.setEditable(false);
		textField3.setText(client.getJmbg());
		textField3.setEditable(false);
		textField4.setText(client.getBrPasosa());
		textField4.setEditable(false);
	}
}
