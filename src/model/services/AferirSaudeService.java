package model.services;

import java.util.List;

import model.dao.AferirSaudeDao;
import model.dao.DaoFactory;
import model.entities.AferirSaude;

public class AferirSaudeService {
	
	private AferirSaudeDao dao = DaoFactory.createAferirSaudeDao();
	
	
	public List<AferirSaude> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate(AferirSaude obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(AferirSaude obj) {
		dao.deleteById(obj.getId());
	}
}
