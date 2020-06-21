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
import model.dao.MergulhadorDao;
import model.entities.Funcao;
import model.entities.Mergulhador;

public class MergulhadorDaoJDBC implements MergulhadorDao {
	
	private Connection conn;

	public MergulhadorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Mergulhador obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO mergulhador "
					+ "(Name, FuncaoId) "
					+ "VALUES "
					+ "(?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setInt(2, obj.getFuncao().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Mergulhador obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE mergulhador "
					+ "SET Name = ?, FuncaoId = ? "
					+ "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getFuncao().getId());
			st.setInt(3, obj.getId());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM mergulhador WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Mergulhador findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT mergulhador.*,funcao.Name as DepName "
					+ "FROM mergulhador INNER JOIN funcao "
					+ "ON mergulhador.FuncaoId = funcao.Id "
					+ "WHERE mergulhador.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Funcao dep = instantiateFuncao(rs);
				Mergulhador obj = instantiateMergulhador(rs, dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Mergulhador instantiateMergulhador(ResultSet rs, Funcao dep) throws SQLException {
		Mergulhador obj = new Mergulhador();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setFuncao(dep);
		return obj;
	}

	private Funcao instantiateFuncao(ResultSet rs) throws SQLException {
		Funcao dep = new Funcao();
		dep.setId(rs.getInt("FuncaoId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Mergulhador> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT mergulhador.*,funcao.Name as DepName "
					+ "FROM mergulhador INNER JOIN funcao "
					+ "ON mergulhador.FuncaoId = funcao.Id "
					+ "ORDER BY Name");

			rs = st.executeQuery();

			List<Mergulhador> list = new ArrayList<>();
			Map<Integer, Funcao> map = new HashMap<>();

			while (rs.next()) {

				Funcao dep = map.get(rs.getInt("FuncaoId"));

				if (dep == null) {
					dep = instantiateFuncao(rs);
					map.put(rs.getInt("FuncaoId"), dep);
				}

				Mergulhador obj = instantiateMergulhador(rs, dep);
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
	public List<Mergulhador> findByFuncao(Funcao funcao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT mergulhador.*,funcao.Name as DepName "
					+ "FROM mergulhador INNER JOIN funcao "
					+ "ON mergulhador.FuncaoId = funcao.Id "
					+ "WHERE FuncaoId = ? "
					+ "ORDER BY Name");

			st.setInt(1, funcao.getId());

			rs = st.executeQuery();

			List<Mergulhador> list = new ArrayList<>();
			Map<Integer, Funcao> map = new HashMap<>();

			while (rs.next()) {

				Funcao dep = map.get(rs.getInt("FuncaoId"));

				if (dep == null) {
					dep = instantiateFuncao(rs);
					map.put(rs.getInt("FuncaoId"), dep);
				}

				Mergulhador obj = instantiateMergulhador(rs, dep);
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
}
