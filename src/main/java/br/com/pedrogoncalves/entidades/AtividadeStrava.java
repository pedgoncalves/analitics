package br.com.pedrogoncalves.entidades;

import java.util.Date;

import br.com.pedrogoncalves.utils.Utils;
import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.model.reference.StravaActivityType;

public class AtividadeStrava {

	private Integer id;
	private Date data;
	private Float distancia;
	private Integer tempoSegundos;
	private String tempo;
	private StravaActivityType tipo;
	private Float calorias;
	private Float lat;
	private Float lng;
	private Float cadencia;
	
	public AtividadeStrava(StravaActivity activity) {
		this.id = activity.getId();
		this.data = Date.from(activity.getStartDate().toInstant());
		this.distancia = activity.getDistance();
		
		this.tempoSegundos = activity.getMovingTime();
		this.tempo = Utils.converterHoraFormatada(tempoSegundos);
		this.tipo = activity.getType();
		
		this.calorias = activity.getCalories();		
		this.cadencia = activity.getAverageCadence();
		
		this.lat = activity.getStartLatitude();
		this.lng = activity.getStartLongitude();
		//buscarLocalizacao(activity);			
	}	
	
	/*private void buscarLocalizacao(StravaActivity activity) {
		
		if ( activity==null || activity.getStartLatlng()==null ) return;
		try {
			GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyDI_ihzuUkvgZvyA26dGk7S39qRnbKfvFg").build();
						
			LatLng location = new LatLng(activity.getStartLatitude(), activity.getStartLongitude());			
			GeocodingResult[] results = GeocodingApi.reverseGeocode(context, location).language("en-US").await();
						
			AddressComponent enderecos[] = results[0].addressComponents;
			for (int j = 0; j < enderecos.length; j++) {
				AddressComponentType tipos[] = results[0].addressComponents[j].types;
				for (int i = 0; i < tipos.length; i++) {
					if ( tipos[i].equals(AddressComponentType.COUNTRY) ) {
						this.pais = results[0].addressComponents[j].longName;						
					}	
					if ( tipos[i].equals(AddressComponentType.LOCALITY) ) {
						this.cidade = results[0].addressComponents[j].longName;						
					}	
					if ( tipos[i].equals(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1) ) {
						this.estado = results[0].addressComponents[j].shortName;						
					}
				}
			}			
			
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public Integer getTempoSegundos() {
		return tempoSegundos;
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

	public Float getCalorias() {
		return calorias;
	}

	public void setCalorias(float calorias) {
		this.calorias = calorias;
	}
	
	public String geLatLng() {
		if ( lat==null || lng==null) return null;
		return Utils.arredondar(lat)+","+Utils.arredondar(lng);
	}

	public Float getCadencia() {
		return cadencia;
	}

	public void setCadencia(Float cadencia) {
		this.cadencia = cadencia;
	}

	public void setCalorias(Float calorias) {
		this.calorias = calorias;
	}	
}
