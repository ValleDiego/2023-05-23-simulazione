package it.polito.tdp.baseball.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.baseball.model.Appearances;
import it.polito.tdp.baseball.model.Arco;
import it.polito.tdp.baseball.model.People;
import it.polito.tdp.baseball.model.Team;


public class BaseballDAO {
	
	public List<People> readAllPlayers(){
		String sql = "SELECT * "
				+ "FROM people";
		List<People> result = new ArrayList<People>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new People(rs.getString("playerID"), 
						rs.getString("birthCountry"), 
						rs.getString("birthCity"), 
						rs.getString("deathCountry"), 
						rs.getString("deathCity"),
						rs.getString("nameFirst"), 
						rs.getString("nameLast"), 
						rs.getInt("weight"), 
						rs.getInt("height"), 
						rs.getString("bats"), 
						rs.getString("throws"),
						getBirthDate(rs), 
						getDebutDate(rs), 
						getFinalGameDate(rs), 
						getDeathDate(rs)) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	public List<Team> readAllTeams(){
		String sql = "SELECT * "
				+ "FROM  teams";
		List<Team> result = new ArrayList<Team>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Team( rs.getInt("iD"),
						rs.getInt("year"), 
						rs.getString("teamCode"), 
						rs.getString("divID"), 
						rs.getInt("div_ID"), 
						rs.getInt("teamRank"),
						rs.getInt("games"), 
						rs.getInt("gamesHome"), 
						rs.getInt("wins"), 
						rs.getInt("losses"), 
						rs.getString("divisionWinnner"), 
						rs.getString("leagueWinner"),
						rs.getString("worldSeriesWinnner"), 
						rs.getInt("runs"), 
						rs.getInt("hits"), 
						rs.getInt("homeruns"), 
						rs.getInt("stolenBases"),
						rs.getInt("hitsAllowed"), 
						rs.getInt("homerunsAllowed"), 
						rs.getString("name"), 
						rs.getString("park")  ) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	
	//=================================================================
	//==================== HELPER FUNCTIONS   =========================
	//=================================================================
	
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getBirthDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("birth_date") != null) {
				return rs.getTimestamp("birth_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getDebutDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("debut_date") != null) {
				return rs.getTimestamp("debut_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getFinalGameDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("finalgame_date") != null) {
				return rs.getTimestamp("finalgame_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getDeathDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("death_date") != null) {
				return rs.getTimestamp("death_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Metodo che legge dal database i vertici
	 * @param anno
	 * @param salario
	 * @return
	 */
	public List<People> getVertices(int anno, double salario){
		String sql = "SELECT p.*, SUM(s.salary) as salaryTot "
				+ "FROM people p, salaries s "
				+ "WHERE p.playerID = s.playerID AND s.year=? "
				+ "Group By p.playerID " 
				+ "HAVING salaryTot > ?";
		List<People> result = new ArrayList<People>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setDouble(2, salario);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new People(rs.getString("playerID"), 
						rs.getString("birthCountry"), 
						rs.getString("birthCity"), 
						rs.getString("deathCountry"), 
						rs.getString("deathCity"),
						rs.getString("nameFirst"), 
						rs.getString("nameLast"), 
						rs.getInt("weight"), 
						rs.getInt("height"), 
						rs.getString("bats"), 
						rs.getString("throws"),
						getBirthDate(rs), 
						getDebutDate(rs), 
						getFinalGameDate(rs), 
						getDeathDate(rs) ) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	// NOTA BENE:
	// Qui sotto facciamo 2 diverse versioni della query per leggere gli archi
	/**
	 * Metodo che legge dal database gli archi, restituiendo già una lista di coppie di vertici.
	 * @param anno
	 * @param salario
	 * @param playerIDMap
	 * @return
	 */
	public List<Arco> getEdges_v1(int anno, double salario, Map<String, People> playerIDMap) {		
		String sql = "SELECT tmp1.playerID as pid1, tmp2.playerID as pid2, tmp1.teamID "
				+ "FROM "
				+ "(SELECT a.playerID, a.teamID "
				+ "FROM appearances a, "
				+ "(SELECT p.*, s.year, SUM(s.salary) as salaryTot "
				+ "FROM people p, salaries s "
				+ "WHERE p.playerID = s.playerID AND s.year=? "
				+ "Group By p.playerID, s.year "
				+ "HAVING salaryTot > ?) ps "
				+ "WHERE a.year=? AND a.playerID = ps.playerID) tmp1 "
				+ "LEFT JOIN "
				+ "(SELECT a.playerID, a.teamID "
				+ "FROM appearances a, "
				+ "(SELECT p.*, s.year, SUM(s.salary) as salaryTot "
				+ "FROM people p, salaries s "
				+ "WHERE p.playerID = s.playerID AND s.year=? "
				+ "Group By p.playerID, s.year "
				+ "HAVING salaryTot > ?) ps "
				+ "WHERE a.year=? AND a.playerID = ps.playerID) tmp2 "
				+ "ON tmp1.playerID<>tmp2.playerID "
				+ "WHERE tmp1.playerID > tmp2.playerID and tmp1.teamID = tmp2.teamID";
		
		List<Arco> result = new ArrayList<Arco>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setDouble(2, salario);
			st.setInt(3, anno);
			st.setInt(4, anno);
			st.setDouble(5, salario);
			st.setInt(6, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				People player1 = playerIDMap.get(rs.getString("pid1"));
				People player2 = playerIDMap.get(rs.getString("pid2"));
				result.add(new Arco(player1, 
						player2) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	public List<Arco> getEdges_v2(int anno, double salario, Map<String, People> playerIDMap) {
		String sql = "SELECT a1.playerID as pid1, a2.playerID as pid2, a1.teamID "
				+ "FROM appearances a1, appearances a2 "
				+ "WHERE a1.playerID < a2.playerID AND a1.teamID = a2.teamID AND a1.year = a2.year AND a1.year = ? "
				+ "AND a1.playerID IN (SELECT p.PlayerID "
				+ "						FROM people p, salaries s "
				+ "						WHERE p.playerID = s.playerID AND s.year=? "
				+ "						Group By p.playerID "
				+ "						HAVING SUM(s.salary) > ?) "
				+ "AND a2.playerID IN (SELECT p.PlayerID "
				+ "						FROM people p, salaries s "
				+ "						WHERE p.playerID = s.playerID AND s.year=? "
				+ "						Group By p.playerID "
				+ "						HAVING SUM(s.salary) > ?)";
		List<Arco> result = new ArrayList<Arco>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setDouble(3, salario);
			st.setInt(4, anno);
			st.setDouble(5, salario);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				People player1 = playerIDMap.get(rs.getString("pid1"));
				People player2 = playerIDMap.get(rs.getString("pid2"));
				result.add(new Arco(player1, 
						player2) );
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	/**
	 * Metodo che restituisce una lista di apparizioni fatte dai giocatori nell'anno selezionato
	 * @param anno
	 * @return
	 */
	
	/**
	 * Metodo che restituisce una lista di apparizioni fatte dai giocatori nell'anno selezionato
	 * @param anno
	 * @return
	 */
	public List<Appearances> getAppearancesInYear(int anno){
		String sql = "SELECT * "
				+ "FROM appearances a "
				+ "WHERE a.year = ?";
		List<Appearances> result = new ArrayList<Appearances>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Appearances(rs.getInt("iD"),
						rs.getInt("year"),
						rs.getString("teamCode"),
						rs.getInt("teamID"),
						rs.getString("playerID"),
						rs.getInt("games"),
						rs.getInt("gamesStarted"), 
						rs.getInt("gamesBatting"), 
						rs.getInt("gamesDefense"), 
						rs.getInt("gamesPitcher"),
						rs.getInt("gamesCatcher")  ) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	/**
	 * Metodo per leggere il salario totale di un giocatore in un anno
	 * @param year
	 * @param player
	 * @return
	 */
	public double getPlayerSalaryInYear(int year, People player) {
		String sql = "SELECT s.playerID, SUM(s.salary) as totSalary "
				+ "FROM  salaries s "
				+ "WHERE s.playerID = ? AND year = ? "
				+ "GROUP BY s.playerID";
		double salario = 0.0;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, player.getPlayerID());
			st.setInt(2, year);
			ResultSet rs = st.executeQuery();

			if (rs.first()) {
				salario = rs.getDouble("totSalary")/1000000;
			}

			conn.close();
			return salario;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	/**
	 * Metodo che restituisce la lista di squadre per le quali un giocatore ha giocato in un dato anno
	 * @param year
	 * @param player
	 * @param teamsIDMap
	 * @return
	 */
	public List<Team> getPlayerTeamsInYear(int year, People player, Map<Integer, Team> teamsIDMap) {
		String sql = "SELECT a.playerID, a.teamID "
				+ "FROM appearances a "
				+ "WHERE a.year=? AND a.playerID = ?";
		List<Team> result = new ArrayList<Team>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			st.setObject(2, player.getPlayerID());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Team squadra = teamsIDMap.get(rs.getInt("teamID"));
				result.add(squadra);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}
