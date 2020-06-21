package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.FuncaoDao;
import model.entities.Funcao;

public class FuncaoService {
	
	private FuncaoDao dao = DaoFactory.createFuncaoDao();
	
	public List<Funcao> findAll(){
		return dao.findAll();
	}

}
