package br.com.pedrogoncalves.entidades;

import javastrava.api.v3.model.reference.StravaActivityType;

public class ResumoAtividade {

	private StravaActivityType tipo;
	private float distanciaTotal;
	private Integer tempoTotal;
	private Integer quantidadeTotal;
	
	public ResumoAtividade(StravaActivityType t) {
		this.tipo=t;
		this.distanciaTotal=0;
		this.tempoTotal=0;
		this.quantidadeTotal=0;
	}
	
	public StravaActivityType getTipo() {
		return tipo;
	}
	
	public void setTipo(StravaActivityType tipo) {
		this.tipo = tipo;
	}
	
	public float getDistanciaTotal() {
		return distanciaTotal;
	}
	
	public void setDistanciaTotal(float distanciaTotal) {
		this.distanciaTotal = distanciaTotal;
	}
	
	public void addDistancia(float d) {
		this.distanciaTotal += d;
	}
	
	public Integer getTempoTotal() {
		return tempoTotal;
	}
	
	public void setTempoTotal(Integer tempoTotal) {
		this.tempoTotal = tempoTotal;
	}
	
	public void addTempo(Integer t) {
		this.tempoTotal += t;
	}
	
	public Integer getQuantidadeTotal() {
		return quantidadeTotal;
	}
	
	public void setQuantidadeTotal(Integer quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}
	
	public void addQuantidade(Integer q) {
		this.quantidadeTotal += q;
	}
	
}
