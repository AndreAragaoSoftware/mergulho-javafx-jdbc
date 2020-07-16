package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.AferirSaudeDao;
import model.entities.AferirSaude;
import model.entities.Mergulhador;

public class AferirSaudeDaoJDBC implements AferirSaudeDao {

	private Connection conn;

	public AferirSaudeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(AferirSaude obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO aferirsaude "
					+ "(PressaoArterialSistolica, PressaoArterialDiastolica, Pulsacao, TemperaturaCorporal, Imc, DataAfericao, Sintomas, MergulhadorId) "
					+ "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setDouble(1, obj.getPressaoArterialSistolica());
			;
			st.setDouble(2, obj.getPressaoArterialDiastolica());
			;
			st.setDouble(3, obj.getPulsacao());
			;
			st.setDouble(4, obj.getTemperaturaCorporal());
			;
			st.setDouble(5, obj.getImc());
			;
			st.setDate(6, new java.sql.Date(obj.getDataAfericao().getTime()));
			st.setString(7, obj.getSintomas());
			st.setInt(8, obj.getMergulhador().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(AferirSaude obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE aferirsaude "
					+ "SET PressaoArterialSistolica = ?, PressaoArterialDiastolica = ?, Pulsacao = ?, TemperaturaCorporal = ?, Imc = ?, DataAfericao = ?, Sintomas = ?, MergulhadorId = ? "
					+ "WHERE Id = ?");

			st.setDouble(1, obj.getPressaoArterialSistolica());
			;
			st.setDouble(2, obj.getPressaoArterialDiastolica());
			;
			st.setDouble(3, obj.getPulsacao());
			;
			st.setDouble(4, obj.getTemperaturaCorporal());
			;
			st.setDouble(5, obj.getImc());
			;
			st.setDate(6, new java.sql.Date(obj.getDataAfericao().getTime()));
			st.setString(7, obj.getSintomas());
			st.setInt(8, obj.getMergulhador().getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM aferirsaude WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public AferirSaude findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT aferirsaude.*,mergulhador.Name as DepName " 
			+ "FROM aferirsaude INNER JOIN mergulhador "
			+ "ON aferirsaude.MergulhadorId = mergulhador.Id " 
			+ "WHERE aferirsaude.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Mergulhador dep = instantiateMergulhador(rs);
				AferirSaude obj = instantiateAferirSaude(rs, dep);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private AferirSaude instantiateAferirSaude(ResultSet rs, Mergulhador dep) throws SQLException {
		AferirSaude obj = new AferirSaude();
		obj.setId(rs.getInt("Id"));
		obj.setPressaoArterialSistolica(rs.getDouble("PressaoArterialSistolica"));
		obj.setPressaoArterialDiastolica(rs.getDouble("PressaoArterialDiastolica"));
		obj.setPulsacao(rs.getDouble("Pulsacao"));
		obj.setTemperaturaCorporal(rs.getDouble("TemperaturaCorporal"));
		obj.setImc(rs.getDouble("Imc"));
		obj.setDataAfericao(new java.util.Date(rs.getTimestamp("DataAfericao").getTime()));
		obj.setSintomas(rs.getString("Sintomas"));
		obj.setMergulhador(dep);
		return obj;
	}

	private Mergulhador instantiateMergulhador(ResultSet rs) throws SQLException {
		Mergulhador dep = new Mergulhador();
		dep.setId(rs.getInt("MergulhadorId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<AferirSaude> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT aferirsaude.*,mergulhador.Name as DepName "
							+ "FROM aferirsaude INNER JOIN mergulhador "
							+ "ON aferirsaude.MergulhadorId = mergulhador.Id "
							+ "ORDER BY DataAfericao");

			rs = st.executeQuery();

			List<AferirSaude> list = new ArrayList<>();
			Map<Integer, Mergulhador> map = new HashMap<>();

			while (rs.next()) {

				Mergulhador dep = map.get(rs.getInt("MergulhadorId"));

				if (dep == null) {
					dep = instantiateMergulhador(rs);
					map.put(rs.getInt("MergulhadorId"), dep);
				}

				AferirSaude obj = instantiateAferirSaude(rs, dep);
				list.add(obj);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<AferirSaude> findByMergulhador(Mergulhador mergulhador) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT aferirsuade.*,mergulhador.Name as DepName "
					+ "FROM aferirsuade INNER JOIN mergulhador " 
					+ "ON aferirsuade.MergulhadorId = mergulhador.Id "
					+ "WHERE MergulhadorId = ? " 
					+ "ORDER BY Name");

			st.setInt(1, mergulhador.getId());

			rs = st.executeQuery();

			List<AferirSaude> list = new ArrayList<>();
			Map<Integer, Mergulhador> map = new HashMap<>();

			while (rs.next()) {

				Mergulhador dep = map.get(rs.getInt("MergulhadorId"));

				if (dep == null) {
					dep = instantiateMergulhador(rs);
					map.put(rs.getInt("MergulhadorId"), dep);
				}

				AferirSaude obj = instantiateAferirSaude(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
