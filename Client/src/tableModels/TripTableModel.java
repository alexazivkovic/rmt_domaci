package tableModels;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TripTableModel implements TableModel{
	private java.util.List<domain.Trip> trips;
	
	public TripTableModel(java.util.List<domain.Trip> trips){
		this.trips = trips;
	}
	
	@Override
	public int getRowCount(){
		return trips.size();
	}
	
	@Override
	public int getColumnCount(){
		return 10;
	}
	
	@Override
	public String getColumnName(int columnIndex){
		switch(columnIndex){
			case 0:
				return "ID";
			case 1:
				return "Ime";
			case 2:
				return "Prezime";
			case 3:
				return "Zemlje";
			case 4:
				return "JMBG";
			case 5:
				return "Broj pasoša";
			case 6:
				return "Datum ulaska";
			case 7:
				return "Datum izlaska";
			case 8:
				return "Način prevoza";
			case 9:
				return "Status";
			default:
				return "";
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		return String.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex){
		domain.Trip trip = trips.get(rowIndex);
		switch(columnIndex){
			case 0:
				return trip.getId();
			case 1:
				return trip.getIme();
			case 2:
				return trip.getPrezime();
			case 3:
				return trip.getZemlje();
			case 4:
				return trip.getJmbg();
			case 5:
				return trip.getBrPasosa();
			case 6:
				return trip.getDatumUlaska();
			case 7:
				return trip.getDatumIzlaska();
			case 8:
				return trip.getNacinPrevoza();
			case 9:
				return trip.getStatus();
			default:
				return "";
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex){
		// Not implemented since table is read-only
	}
	
	@Override
	public void addTableModelListener(TableModelListener l){
	
	}
	
	@Override
	public void removeTableModelListener(TableModelListener l){
	
	}
}
