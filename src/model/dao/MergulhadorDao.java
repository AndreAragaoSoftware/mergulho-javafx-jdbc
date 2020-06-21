package model.dao;

import java.util.List;

import model.entities.Funcao;
import model.entities.Mergulhador;

public interface MergulhadorDao {
	
	void insert(Mergulhador obj);
	void update(Mergulhador obj);
	void deleteById(Integer id);
	Mergulhador findById(Integer id);
	List<Mergulhador> findAll();
	List<Mergulhador> findByFuncao(Funcao funcao);
}
