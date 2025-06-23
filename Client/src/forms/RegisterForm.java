package forms;

import domain.Client;
import network.Packet;
import network.PacketType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegisterForm extends JFrame{
	private JTextField textField1;
	private JPanel panel1;
	private JPasswordField passwordField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JTextField textField5;
	private JTextField textField6;
	private JButton registrujSeButton;
	private JButton nazadButton;
	
	private JFrame instance;
	private JFrame parent;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public RegisterForm(JFrame parent, ObjectOutputStream out, ObjectInputStream in){
		setContentPane(panel1);
		setTitle("Registracija");
		setSize(800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		instance = this;
		this.parent = parent;
		this.out = out;
		this.in = in;
		
		registrujSeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Client client = new Client();
				client.setUsername(textField1.getText());
				client.setPassword(new String(passwordField1.getPassword()));
				client.setIme(textField2.getText());
				client.setPrezime(textField3.getText());
				client.setEmail(textField4.getText());
				client.setJmbg(textField5.getText());
				client.setBrPasosa(textField6.getText());
				try{
					if(!checkInput()){
						JOptionPane.showMessageDialog(instance, "Unesite ispravne podatke", "Greska", JOptionPane.ERROR_MESSAGE);
						return;
					}
					Packet p = new Packet(PacketType.REQUEST_REGISTER, client);
					out.writeObject(p);
					p = (Packet) in.readObject();
					if(p.getType().equals(PacketType.RESPONSE_CODE_ERROR))
						JOptionPane.showMessageDialog(instance, "Korisnik ili username vec postoji", "Greska", JOptionPane.ERROR_MESSAGE);
					else
						JOptionPane.showMessageDialog(instance, "Uspesna registracija!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
				}catch(Exception ex){
					JOptionPane.showMessageDialog(instance, "Greska pri registraciji", "Greska", JOptionPane.ERROR_MESSAGE);
					//throw new RuntimeException(ex);
				}
			}
		});
		
		nazadButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				parent.setVisible(true);
				instance.dispose();
			}
		});
	}
	
	private boolean checkInput(){
		if(textField1.getText().isEmpty() || textField2.getText().isEmpty() || textField3.getText().isEmpty() || textField4.getText().isEmpty() || textField5.getText().isEmpty() || textField6.getText().isEmpty() || passwordField1.getText().isEmpty()){
			return false;
		}
		if(!textField5.getText().matches("\\d{13}") || !textField6.getText().matches("\\d{9}")){
			return false;
		}
		return true;
	}
}
