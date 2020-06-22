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
	
	public void saveOrUpdate(Funcao obj) {
		//testando se Id é igual a zero
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}

}
