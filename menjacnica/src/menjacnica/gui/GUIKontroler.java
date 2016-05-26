package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import menjacnica.Menjacnica;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	private static MenjacnicaGUI glavniProzor;
	private static DodajKursGUI dodajKursProzor;
	private static ObrisiKursGUI obrisiKursProzor;
	private static IzvrsiZamenuGUI izvrsiZamenuProzor;
	protected static Menjacnica sistem;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					glavniProzor = new MenjacnicaGUI();
					glavniProzor.setVisible(true);
					sistem = new Menjacnica();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(glavniProzor.getContentPane(),
				"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}


	public static void prikaziDodajKursGUI() {
		dodajKursProzor = new DodajKursGUI();
		dodajKursProzor.setLocationRelativeTo(glavniProzor.getContentPane());
		dodajKursProzor.setVisible(true);
	}


	public static void prikaziObrisiKursGUI(JTable table) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(table.getModel());
			obrisiKursProzor = new ObrisiKursGUI(model.vratiValutu(table.getSelectedRow()));
			obrisiKursProzor.setLocationRelativeTo(glavniProzor.getContentPane());
			obrisiKursProzor.setVisible(true);
		}
	}


	public static void prikaziIzvrsiZamenuGUI(JTable table) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel)(table.getModel());
			izvrsiZamenuProzor = new IzvrsiZamenuGUI(model.vratiValutu(table.getSelectedRow()));
			izvrsiZamenuProzor.setLocationRelativeTo(glavniProzor.getContentPane());
			izvrsiZamenuProzor.setVisible(true);
		}
	}


	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(glavniProzor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sistem.ucitajIzFajla(file.getAbsolutePath());
				glavniProzor.prikaziSveValute();
			}	
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}


	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(glavniProzor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}


	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(glavniProzor.getContentPane(),
				"Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}
	

	public static void unesiKurs(String naziv, String skraceniNaziv, int sifra, double prodajni,double kupovni, double srednji) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(prodajni);
			valuta.setKupovni(kupovni);
			valuta.setSrednji(srednji);
			
			// Dodavanje valute u kursnu listu
			sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			glavniProzor.prikaziSveValute();
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(dodajKursProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void prikaziSveValute(JTable table){
		MenjacnicaTableModel model = (MenjacnicaTableModel)(table.getModel());
		model.staviSveValuteUModel(vratiKursnuListu());
	}
	
	public static List<Valuta> vratiKursnuListu() {
		return sistem.vratiKursnuListu();
	}
	
	public static void zatvoriProzorDodajKurs(){
		dodajKursProzor.dispose();
	}


	public static void prikaziValutuZaBrisanje(Valuta valuta, JTextField textFieldNaziv, JTextField textFieldSkraceniNaziv,
			JTextField textFieldSifra, JTextField textFieldKupovniKurs, JTextField textFieldProdajniKurs,
			JTextField textFieldSrednjiKurs) {
		textFieldNaziv.setText(valuta.getNaziv());
		textFieldSkraceniNaziv.setText(valuta.getSkraceniNaziv());
		textFieldSifra.setText(""+valuta.getSifra());
		textFieldProdajniKurs.setText(""+valuta.getProdajni());
		textFieldKupovniKurs.setText(""+valuta.getKupovni());
		textFieldSrednjiKurs.setText(""+valuta.getSrednji());
	}


	public static void obrisiValutu(Valuta valuta) {
		try{
			sistem.obrisiValutu(valuta);
			
			glavniProzor.prikaziSveValute();
			obrisiKursProzor.dispose();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(obrisiKursProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}


	public static void prikaziValutuZaZamenu(Valuta valuta, JTextField textFieldProdajniKurs,
			JTextField textFieldKupovniKurs, JTextField textFieldValuta) {
		textFieldProdajniKurs.setText(""+valuta.getProdajni());
		textFieldKupovniKurs.setText(""+valuta.getKupovni());
		textFieldValuta.setText(valuta.getSkraceniNaziv());
	}


	public static void izvrsiZamenu(Valuta valuta, JRadioButton rdbtnProdaja, JTextField textFieldIznos, JTextField textFieldKonacniIznos) {
		try{
			double konacniIznos = 
					sistem.izvrsiTransakciju(valuta,rdbtnProdaja.isSelected(), 
							Double.parseDouble(textFieldIznos.getText()));
		
			textFieldKonacniIznos.setText(""+konacniIznos);
		} catch (Exception e1) {
		JOptionPane.showMessageDialog(izvrsiZamenuProzor.getContentPane(), e1.getMessage(),
				"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
}
