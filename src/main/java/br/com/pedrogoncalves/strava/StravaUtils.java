package br.com.pedrogoncalves.strava;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

import br.com.pedrogoncalves.IntegracaoStrava;
import br.com.pedrogoncalves.entidades.AtividadeStrava;
import javastrava.api.v3.model.StravaActivity;
import javastrava.api.v3.model.StravaAthlete;

public class StravaUtils {

	private static StravaUtils stravaUtils;
	
	private IntegracaoStrava strava;
	private StravaAthlete atleta;	
	
	private StravaUtils() {
		Authenticator.setDefault(new ProxyAuthenticator("p.goncalves", "Dataprev.0"));
		System.getProperties().put("https.proxyHost", "10.31.220.23");
		System.getProperties().put("https.proxyPort", "3128");	
		strava = IntegracaoStrava.getInstance();
	}	

	public static StravaUtils getInstance() {
		if (stravaUtils != null) {
			return stravaUtils;
		}
		else {
			stravaUtils = new StravaUtils();			
		}
		return stravaUtils;		
	}
	
	public String getNomeUsuarioLogado() {
		if (atleta == null) {
			atleta = strava.buscarAtleta();
		}
		return atleta.getFirstname();
	}
	
	public String getEmailUsuario() {
		if (atleta == null) {
			atleta = strava.buscarAtleta();
		}
		return atleta.getEmail();
	}
	
	/**
	 * Busca todas as atividades do usuário logado
	 * Pode demorar caso o usuário possua muitas atividades
	 * 
	 * UTILZAR COM CUIDADO
	 * @return Lista de Atividades no Strava do Usuário Logado
	 */
	public List<AtividadeStrava> buscarTodasAtividades() {		
		return converterListaAtividade(strava.buscarTodasAtividades());
	}
	

	/**
	 * Busca todas as atividades do usuário logado
	 * Pode demorar caso o usuário possua muitas atividades
	 * 
	 * UTILZAR COM CUIDADO
	 * @return Lista de Atividades no Strava do Usuário Logado
	 */
	public List<AtividadeStrava> buscarAtividades(int ano) {		
		return converterListaAtividade(strava.buscarAtividades(ano));
	}
	
	private List<AtividadeStrava> converterListaAtividade(List<StravaActivity> list) {
		List<AtividadeStrava> result = new ArrayList<AtividadeStrava>();
		for(StravaActivity activity:list) {
			result.add( new AtividadeStrava(activity) );
		}		
		return result;
	}
	
	class ProxyAuthenticator extends Authenticator {

	    private String user, password;

	    public ProxyAuthenticator(String user, String password) {
	        this.user = user;
	        this.password = password;
	    }

	    protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication(user, password.toCharArray());
	    }
	}
}
