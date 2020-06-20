package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Funcao;

public class FuncaoService {
	
	public List<Funcao> findAll(){
		List<Funcao> list = new ArrayList<>();
		list.add(new Funcao(1, "Sup. Mergulho"));
		list.add(new Funcao(2, "Inspetor de Mergulho"));
		return list;
	}

}
