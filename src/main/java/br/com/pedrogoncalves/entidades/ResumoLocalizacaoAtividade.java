package br.com.pedrogoncalves.entidades;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javastrava.api.v3.model.reference.StravaActivityType;

public class ResumoLocalizacaoAtividade {
	
	private StravaActivityType tipo;
	private Map<String, Integer> locais;
	
	public ResumoLocalizacaoAtividade(StravaActivityType tipo) {		
		this.tipo = tipo;
		this.locais = new Hashtable<String, Integer>();
	}

	public StravaActivityType getTipo() {
		return tipo;
	}
	
	public void addQuantidade(String local, Integer q) {
		if (local==null) return;
		if ( !locais.containsKey(local) ) {
			locais.put(local, q);
		}
		else {
			Integer quantidade = locais.get(local);
			quantidade += q;
			locais.put(local, quantidade);
		}		
	}
	
	public Integer getQuantidade(String local) {
		if ( !locais.containsKey(local) ) {
			return 0;
		}
		else {
			return locais.get(local);
		}
	}
	
	public Integer getTotalQuantidade() {
		Iterator<String> keys = locais.keySet().iterator();
		Integer total = 0;
		while (keys.hasNext()) {
			total += locais.get(keys.next());
		}
		return total;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		Iterator<String> keys = locais.keySet().iterator();		
		while (keys.hasNext()) {
			String k = keys.next();
			sb.append("["+k+","+locais.get(k)+"],");
		}
		return sb.toString();
	}
	
}
