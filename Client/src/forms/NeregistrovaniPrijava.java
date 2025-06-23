package forms;

import domain.Trip;
import network.Packet;
import network.PacketType;

import java.io.*;
import java.awt.Desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class NeregistrovaniPrijava extends JFrame{
	
	protected JTextField textField1;
	private JPanel panel1;
	protected JTextField textField2;
	protected JTextField textField3;
	protected JTextField textField4;
	private JSpinner spinner1;
	private JSpinner spinner2;
	protected JButton prijaviPutovanjeButton;
	private JPanel checkBoxPanel;
	private JComboBox comboBox1;
	private JButton nazadButton;
	
	private JFrame instance;
	private JFrame parent;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private static final String[] EU_COUNTRIES = {
			"Austrija", "Belgija", "Bugarska", "Hrvatska", "Kipar", "Češka",
			"Danska", "Estonija", "Finska", "Francuska", "Nemačka", "Grčka",
			"Mađarska", "Irska", "Italija", "Letonija", "Litvanija", "Luksemburg",
			"Malta", "Holandija", "Poljska", "Portugalija", "Rumunija",
			"Slovačka", "Slovenija", "Španija", "Švedska"
	};
	
	public NeregistrovaniPrijava(JFrame parent, ObjectOutputStream out, ObjectInputStream in){
		this.parent = parent;
		setContentPane(panel1);
		setTitle("Prijava putovanja");
		setSize(800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		instance = this;
		this.parent = parent;
		this.out = out;
		this.in = in;
		
		// Panel to hold checkboxes
		checkBoxPanel.setLayout(new GridLayout(0, 2)); // 2 columns
		
		// Store checkboxes in array
		JCheckBox[] countryCheckboxes = new JCheckBox[EU_COUNTRIES.length];
		
		for (int i = 0; i < EU_COUNTRIES.length; i++) {
			countryCheckboxes[i] = new JCheckBox(EU_COUNTRIES[i]);
			checkBoxPanel.add(countryCheckboxes[i]);
		}
		
		String[] naciniPrevoza = {"Putnicki automobil", "Motocikl", "Autobus", "Avio prevoz"};
		//comboBox1.setModel(new DefaultComboBoxModel(naciniPrevoza));
		for(String n : naciniPrevoza)
			comboBox1.addItem(n);
		
		prijaviPutovanjeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try{
					if(validInput()){
						String ime = textField1.getText();
						String prezime = textField2.getText();
						String jmbg = textField3.getText();
						String brPasosa = textField4.getText();
						String datumUlaska = formatDate((Date) spinner1.getValue());
						String datumIzlaska = formatDate((Date) spinner2.getValue());
						String[] odabraneZemlje = new String[countryCheckboxes.length];
						for (int i = 0; i < countryCheckboxes.length; i++) {
							if (countryCheckboxes[i].isSelected()) {
								odabraneZemlje[i] = countryCheckboxes[i].getText();
							}
						}
						String zemlje = "";
						for(String z : odabraneZemlje)
							if(z!=null)
								zemlje += z + " ";
						String nacinPrevoza = naciniPrevoza[comboBox1.getSelectedIndex()];
						Trip trip = new Trip(ime, prezime, zemlje, jmbg, brPasosa, datumUlaska, datumIzlaska, nacinPrevoza);
						Packet packet = new Packet(PacketType.REQUEST_ADD_TRIP, trip);
						out.writeObject(packet);
						packet = (Packet) in.readObject();
						if(packet.getType().equals(PacketType.RESPONSE_CODE_OK)){
							JOptionPane.showMessageDialog(instance, "Uspesno ste uneli izmene!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
							String filename = "putovanje_" + System.currentTimeMillis() + ".txt";
							FileWriter writer = new FileWriter(filename);
							writer.write(packet.getData().toString());
							writer.close();
							Desktop.getDesktop().open(new File(filename));
						}
						else{
							JOptionPane.showMessageDialog(instance, "Lose uneti licni podaci ili je dato putovanje vec prijavljeno", "Greska", JOptionPane.ERROR_MESSAGE);
						}
					}
					else{
						JOptionPane.showMessageDialog(instance, "Unesite ispravno sve podatke", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(instance, "Greska prilikom komunikacije sa serverom", "Greska", JOptionPane.ERROR_MESSAGE);
					System.out.println(ex.getMessage());
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
	
	private void createUIComponents(){
		// Date models
		SpinnerDateModel entryModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
		SpinnerDateModel exitModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
		
		// Spinners
		spinner1 = new JSpinner(entryModel);
		spinner1.setEditor(new JSpinner.DateEditor(spinner1, "dd.MM.yyyy"));
		
		spinner2 = new JSpinner(exitModel);
		spinner2.setEditor(new JSpinner.DateEditor(spinner2, "dd.MM.yyyy"));
	}
	
	private String formatDate(Date date){
		return String.format("%1$td.%1$tm.%1$tY", date);
	}
	
	private boolean validInput(){
		if(textField1.getText().isEmpty() || textField2.getText().isEmpty() || textField3.getText().isEmpty() || textField4.getText().isEmpty()){
			return false;
		}
		if(!textField3.getText().matches("\\d{13}") || !textField4.getText().matches("\\d{9}")){
			return false;
		}
		if(spinner1.getValue() == null || spinner2.getValue() == null){
			return false;
		}
		if(comboBox1.getSelectedIndex() == -1){
			return false;
		}
		if(checkBoxPanel.getComponentCount() == 0){
			return false;
		}
		if(checkBoxPanel.getComponentCount() > EU_COUNTRIES.length){
			return false;
		}
		Date now = new Date();
		Date entryDate = (Date) spinner1.getValue();
		Date exitDate = (Date) spinner2.getValue();
		if(entryDate.before(now)){
			return false;
		}
		if(exitDate.before(entryDate)){
			return false;
		}
		long diffInMillies = exitDate.getTime() - entryDate.getTime();
		long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
		if(diffInDays > 90){
			return false;
		}
		return true;
	}
}
