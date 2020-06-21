package model.dao;

import java.util.List;

import model.entities.Funcao;

public interface FuncaoDao {

	void insert(Funcao obj);
	void update(Funcao obj);
	void deleteById(Integer id);
	Funcao findById(Integer id);
	List<Funcao> findAll();
}
