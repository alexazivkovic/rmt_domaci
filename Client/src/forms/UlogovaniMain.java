package forms;

import domain.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UlogovaniMain extends JFrame{
	private JFrame instance;
	private JFrame parent;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Client client;
	
	private JButton prijaviNovoPutovanjeButton;
	private JPanel panel1;
	private JButton pregledIIzmenaPutovanjaButton;
	private JButton nazadButton;
	private JTextField textField1;
	
	public UlogovaniMain(JFrame parent, ObjectOutputStream out, ObjectInputStream in, Client client){
		setContentPane(panel1);
		setTitle("Dobrodosli");
		setSize(800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		instance = this;
		this.parent = parent;
		this.out = out;
		this.in = in;
		this.client = client;
		
		textField1.setText("Dobro dosli, "+client.getIme()+" "+client.getPrezime());
		
		prijaviNovoPutovanjeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				instance.setVisible(false);
				new UlogovaniPrijava(instance, out, in, client).setVisible(true);
			}
		});
		
		pregledIIzmenaPutovanjaButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				instance.setVisible(false);
				new UlogovaniPregled(instance, out, in, client).setVisible(true);
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
}
