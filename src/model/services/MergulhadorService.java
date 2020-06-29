package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MergulhadorDao;
import model.entities.Mergulhador;

public class MergulhadorService {
	
	private MergulhadorDao dao = DaoFactory.createMergulhadorDao();
	
	public List<Mergulhador> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(Mergulhador obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Mergulhador obj) {
		dao.deleteById(obj.getId());
	}
}
