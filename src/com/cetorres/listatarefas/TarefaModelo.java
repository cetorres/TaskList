package com.cetorres.listatarefas;

public class TarefaModelo {
	public Integer TarefaId;
	public String TarefaDescricao;
	
	/** A lista (ListView) irá mostrar o valor retornado pelo método toString() */
	public String toString() {
		return TarefaDescricao;
	}

	public boolean equals(Object o) {
		return o instanceof TarefaModelo && ((TarefaModelo) o).TarefaDescricao.compareTo(TarefaDescricao) == 0;
	} 
}
