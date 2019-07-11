package it.polito.tdp.poweroutages.db;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutagesDAO {
	
	public List<Nerc> loadAllNercs() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public boolean getArchi(Nerc n1, Nerc n2) {
		
		String sql = "SELECT n.nerc_one, n.nerc_two " + 
				"FROM nercrelations AS n";
		
		boolean trovato = false;

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				if((n1.getId()==res.getInt("n.nerc_one")) && (n2.getId()==res.getInt("n.nerc_two"))) {
					trovato = true;
				}		
	
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return trovato;
	}

	public int getPeso(Nerc sorgente, Nerc destinazione) {
		
		String sql = "SELECT COUNT(DISTINCT MONTH(p1.date_event_began), YEAR(p1.date_event_began)) AS cnt " + 
				"FROM poweroutages AS p1, poweroutages AS p2 " + 
				"WHERE ((YEAR(p1.date_event_began) = YEAR(p2.date_event_began) AND MONTH(p1.date_event_began) = MONTH(p2.date_event_began)) " + 
				"OR (YEAR(p1.date_event_began) = YEAR(p2.date_event_finished) AND MONTH(p1.date_event_began) = MONTH(p2.date_event_finished)) " + 
				"OR (YEAR(p1.date_event_finished) = YEAR(p2.date_event_began) AND MONTH(p1.date_event_finished) = MONTH(p2.date_event_began))) " + 
				"AND p1.nerc_id = ? AND p2.nerc_id = ?";
		
		int peso = 0;
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, sorgente.getId());
			st.setInt(2, destinazione.getId());
			ResultSet res = st.executeQuery();

			if (res.next()) {
				peso = res.getInt("cnt");
			}
			
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return peso;
	}
	
}
