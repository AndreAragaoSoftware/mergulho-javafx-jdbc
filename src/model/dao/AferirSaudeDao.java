package model.dao;

import java.util.List;

import model.entities.AferirSaude;
import model.entities.Mergulhador;

public interface AferirSaudeDao {
	
		void insert(AferirSaude obj);
		void update(AferirSaude obj);
		void deleteById(Integer id);
		AferirSaude findById(Integer id);
		List<AferirSaude> findAll();
		List<AferirSaude> findByMergulhador(Mergulhador mergulhador);
}

