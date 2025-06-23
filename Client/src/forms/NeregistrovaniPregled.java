package forms;

import domain.Trip;
import network.Packet;
import network.PacketType;
import tableModels.TripTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NeregistrovaniPregled extends JFrame{
	protected JTextField textField1;
	private JPanel panel1;
	protected JTextField textField2;
	private JButton pretraziButton;
	private JTable table1;
	private JButton nazadButton;
	protected JButton izmeniPutovanjeZaUnetiButton;
	protected JTextField textField3;
	
	private JFrame instance;
	private JFrame parent;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	protected ArrayList<Trip> trips;
	
	public NeregistrovaniPregled(JFrame parent, ObjectOutputStream out, ObjectInputStream in){
		setContentPane(panel1);
		setTitle("Pregled putovanja");
		setSize(800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		textField3.setVisible(false);
		izmeniPutovanjeZaUnetiButton.setVisible(false);
		
		instance = this;
		this.parent = parent;
		this.out = out;
		this.in = in;
		
		pretraziButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try{
					if(!checkInput()){
						JOptionPane.showMessageDialog(instance, "Unesite ispravne podatke", "Greska", JOptionPane.ERROR_MESSAGE);
						return;
					}
					String jmbg = textField1.getText();
					String brPasosa = textField2.getText();
					Trip t = new Trip(jmbg, brPasosa);
					Packet p = new Packet(PacketType.REQUEST_GET_TRIPS, t);
					out.writeObject(p);
					p = (Packet) in.readObject();
					if(p.getType().equals(PacketType.RESPONSE_CODE_OK)){
						trips = (ArrayList<Trip>) p.getData();
						table1.setModel(new TripTableModel(trips));
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(instance, "Greška pri učitavanju podataka", "Greška", JOptionPane.ERROR_MESSAGE);
					throw new RuntimeException(ex);
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
		if(textField1.getText().isEmpty() || textField2.getText().isEmpty()){
			return false;
		}
		if(!textField1.getText().matches("\\d{13}") || !textField2.getText().matches("\\d{9}")){
			return false;
		}
		return true;
	}
}
