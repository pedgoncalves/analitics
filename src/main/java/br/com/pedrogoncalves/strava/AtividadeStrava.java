package br.com.pedrogoncalves.strava;

import java.util.Date;

import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.model.reference.StravaActivityType;

public class AtividadeStrava {

	private Integer id;
	private Date data;
	private Float distancia;
	private String tempo;
	private StravaActivityType tipo;
	
	public AtividadeStrava(StravaActivity activity) {
		this.id = activity.getId();
		this.data = Date.from(activity.getStartDate().toInstant());
		this.distancia = activity.getDistance();
		
		Integer time = activity.getMovingTime();
		int h = (int) (time/3600);
		int m = (int) ((time-(h*3600))/60);
		int s = (int) ((time-(h*3600))-m*60);
		
		StringBuilder sBuilder = new StringBuilder();
		if ( h<10 ) {
			sBuilder.append("0");			
		}
		sBuilder.append(h);
		sBuilder.append(":");
		if ( m<10 ) {
			sBuilder.append("0");			
		}
		sBuilder.append(m);
		sBuilder.append(":");
		if ( s<10 ) {
			sBuilder.append("0");			
		}
		sBuilder.append(s);		
		this.tempo = sBuilder.toString();
					
		this.tipo = activity.getType();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Float getDistancia() {
		return distancia;
	}
	
	public void setDistancia(Float distancia) {
		this.distancia = distancia;
	}
	
	public String getTempo() {
		return tempo;
	}
	
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public StravaActivityType getTipo() {
		return tipo;
	}

	public void setTipo(StravaActivityType tipo) {
		this.tipo = tipo;
	}
	
}
