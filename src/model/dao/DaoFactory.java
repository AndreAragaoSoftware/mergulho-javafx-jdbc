package model.dao;

import db.DB;
import model.dao.impl.FuncaoDaoJDBC;
import model.dao.impl.MergulhadorDaoJDBC;

public class DaoFactory {
	
	public static FuncaoDao createFuncaoDao() {
		return new FuncaoDaoJDBC(DB.getConnection());
	}
	
	public static MergulhadorDao createMergulhadorDao() {
		return new MergulhadorDaoJDBC(DB.getConnection());
	}
}
