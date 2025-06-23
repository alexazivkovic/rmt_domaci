package forms;

import domain.Client;
import network.Packet;
import network.PacketType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginForm extends JFrame{
	private JTextField textField1;
	private JPanel panel1;
	private JPasswordField passwordField1;
	private JButton ulogujSeButton;
	private JButton vidiSvojaDosadasnjaPutovanjaButton;
	private JButton registrujSeButton;
	private JButton prijaviPutovanjeBezNalogaButton;
	
	private LoginForm instance;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	
	public LoginForm() throws IOException{
		socket = new Socket("localhost", 9000);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		setContentPane(panel1);
		setTitle("Login Form");
		setSize(800, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		instance = this;
		prijaviPutovanjeBezNalogaButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				instance.setVisible(false);
				new NeregistrovaniPrijava(instance, out, in).setVisible(true);
			}
		});
		vidiSvojaDosadasnjaPutovanjaButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				instance.setVisible(false);
				new NeregistrovaniPregled(instance, out, in).setVisible(true);
			}
		});
		registrujSeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				instance.setVisible(false);
				new RegisterForm(instance, out, in).setVisible(true);
			}
		});
		ulogujSeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String username = textField1.getText();
				String password = new String(passwordField1.getPassword());
				if(username.isEmpty() || password.isEmpty()){
					JOptionPane.showMessageDialog(instance, "Unesite sve podatke", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Client client = new Client(username, password);
				Packet packet = new Packet(PacketType.REQUEST_LOGIN, client);
				try{
					out.writeObject(packet);
					packet = (Packet) in.readObject();
					if(packet.getType().equals(PacketType.RESPONSE_CODE_ERROR)){
						JOptionPane.showMessageDialog(instance, "Korisnik ili password je pogresan", "Greska", JOptionPane.ERROR_MESSAGE);
						return;
					}
					instance.setVisible(false);
					new UlogovaniMain(instance, out, in, (Client) packet.getData()).setVisible(true);
				} catch(Exception ex){
					System.out.println(ex.getMessage());				}
			}
		});
		
	}
}
