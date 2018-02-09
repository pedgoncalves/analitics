package br.com.pedrogoncalves.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.pedrogoncalves.entidades.AtividadeStrava;
import br.com.pedrogoncalves.entidades.ResumoAtividade;
import br.com.pedrogoncalves.entidades.ResumoLocalizacaoAtividade;
import br.com.pedrogoncalves.strava.StravaUtils;
import br.com.pedrogoncalves.utils.Utils;
import javastrava.api.v3.model.reference.StravaActivityType;

@ManagedBean(name="dashboardBean")
public class DashboardBean {
		
    private StravaActivityType tiposAtividadesValidas[] = {StravaActivityType.RUN, StravaActivityType.SWIM, StravaActivityType.RIDE};
	
	private LineChartModel lineModel;
	
    private List<AtividadeStrava> listaAtividades;
    private int anoSelecionado = Calendar.getInstance().get(Calendar.YEAR);
    
    private double metaCorrida = 1500;
    
    private Map<StravaActivityType, ResumoAtividade> resumosAtividades;
    private Map<StravaActivityType, ResumoLocalizacaoAtividade> resumoLocalizacaoAtividade;
    
	
	public String getNomeUsuarioLogado() {
		return StravaUtils.getInstance().getNomeUsuarioLogado();
	}
	
	public String getEmailUsuarioLogado() {
		return StravaUtils.getInstance().getEmailUsuario();
	}
			
	public LineChartModel getLineModel() {
		lineModel = new LineChartModel();		
		if ( listaAtividades==null ) getListaAtividades();
		
		lineModel.setTitle("Km Percorridos");
        lineModel.setLegendPosition("e");
        lineModel.setExtender("skinChart");
        lineModel.setAnimate(true);
        
        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        
        DateAxis xAxis = new DateAxis();
        xAxis.setTickAngle(-50);
        xAxis.setTickFormat("%b %#d, %y");
                
        lineModel.getAxes().put(AxisType.X, xAxis);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	StravaActivityType tipo = tiposAtividadesValidas[i];
        	
        	LineChartSeries series1 = new LineChartSeries();
            series1.setLabel(tipo.getValue());
            for (AtividadeStrava atividade: listaAtividades) {
            	if ( atividade.getTipo().equals(tipo) ) {
            		series1.set(sdf.format(atividade.getData()),atividade.getDistancia()/1000);
            	}
            }
            
            if ( !series1.getData().isEmpty() ) lineModel.addSeries(series1); 
		}        		
		
		return lineModel;
		
	}
		
	@SuppressWarnings("deprecation")
	public String getDatasAtividades() {
		if ( listaAtividades==null ) getListaAtividades();
		
		StringBuffer sb = new StringBuffer();
		//[ new Date(2013, 9, 4), 38177 ],
		for (AtividadeStrava atividade:listaAtividades) {
			sb.append("[ new Date(");
			sb.append(atividade.getData().getYear()+1900);
			sb.append(",");
			sb.append(atividade.getData().getMonth());
			sb.append(", ");
			sb.append(atividade.getData().getDate());
			sb.append("), ");
			sb.append(atividade.getDistancia()/1000);
			sb.append("], ");			
		}		
		
		return sb.toString();
	}
	
	public String getLocalizacaoCorrida() {
		if ( listaAtividades==null ) getListaAtividades();
		
		StravaActivityType stravaActivityType = StravaActivityType.RUN;			
		ResumoLocalizacaoAtividade resumo = resumoLocalizacaoAtividade.get(stravaActivityType);
		
		return resumo.toString();
	}
	
	public Integer getQuantidadeTotalGeralAtividades() {
		if ( listaAtividades==null ) getListaAtividades();
		
		Integer total = 0;
		for (int i = 0; i < tiposAtividadesValidas.length; i++) {
			StravaActivityType stravaActivityType = tiposAtividadesValidas[i];
			
			ResumoAtividade resumo = resumosAtividades.get(stravaActivityType);
			total += resumo.getQuantidadeTotal();
		}
		
		return total;
	}
	
	public Integer getQuantidadeTotalGeralCorrida() {
		if ( listaAtividades==null ) getListaAtividades();
		
		Integer total = 0;
		
		StravaActivityType stravaActivityType = StravaActivityType.RUN;			
		ResumoAtividade resumo = resumosAtividades.get(stravaActivityType);
		total = resumo.getQuantidadeTotal();		
		
		return total;
	}
	
	public Integer getQuantidadeTotalGeralNatacao() {
		if ( listaAtividades==null ) getListaAtividades();
		
		Integer total = 0;
		
		StravaActivityType stravaActivityType = StravaActivityType.SWIM;			
		ResumoAtividade resumo = resumosAtividades.get(stravaActivityType);
		total = resumo.getQuantidadeTotal();		
		
		return total;
	}
	
	public Integer getQuantidadeTotalGeralCiclismo() {
		if ( listaAtividades==null ) getListaAtividades();
		
		Integer total = 0;
		
		StravaActivityType stravaActivityType = StravaActivityType.RIDE;			
		ResumoAtividade resumo = resumosAtividades.get(stravaActivityType);
		total = resumo.getQuantidadeTotal();		
		
		return total;
	}
	
	public double getTotalCorrida() {
		if ( listaAtividades==null ) getListaAtividades();
		
		ResumoAtividade resumo = resumosAtividades.get(StravaActivityType.RUN);		
		float total = resumo.getDistanciaTotal();
		return Utils.arredondar(total/1000);
	
	}
	
	public double getMetaCorrida() {
		return Utils.arredondar(metaCorrida);
	}
	
	public double getMetaCorridaAlcancada() {
		double valor = (getTotalCorrida()/getMetaCorrida())*100;		
		return Utils.arrendarPercentual(valor);
	}
	
	public double getMetaCorridaRestante() {
		double valor = 100-((getTotalCorrida()/getMetaCorrida())*100);		
		return Utils.arrendarPercentual(valor);
	}	
	
	public List<AtividadeStrava> getListaAtividades() {				
		//listaAtividades = StravaUtils.getInstance().buscarAtividades( anoSelecionado );
		listaAtividades = StravaUtils.getInstance().buscarTodasAtividades();
		
		resumosAtividades = new Hashtable<StravaActivityType, ResumoAtividade>();
		resumoLocalizacaoAtividade = new Hashtable<StravaActivityType, ResumoLocalizacaoAtividade>();
		
		for (Iterator<AtividadeStrava> iterator = listaAtividades.iterator(); iterator.hasNext();) {
			AtividadeStrava atividadeStrava = iterator.next();
			StravaActivityType tipo = atividadeStrava.getTipo();
			
			//Preenche resumo de dados de atividades
			ResumoAtividade resumo = resumosAtividades.get(tipo);
			if ( resumo==null ) resumo = new ResumoAtividade(tipo);
			
			resumo.addDistancia(atividadeStrava.getDistancia());
			resumo.addTempo(atividadeStrava.getTempoSegundos());
			resumo.addQuantidade(1);
			
			resumosAtividades.put(tipo, resumo);
			
			//Preenche resumo de local das atividades
			if ( atividadeStrava.geLatLng()!=null ) {
				ResumoLocalizacaoAtividade resumoLocal = resumoLocalizacaoAtividade.get(tipo);
				if ( resumoLocal==null ) resumoLocal = new ResumoLocalizacaoAtividade(tipo);			
				resumoLocal.addQuantidade(atividadeStrava.geLatLng(), 1);			
				resumoLocalizacaoAtividade.put(tipo, resumoLocal);
			}
		}
		
		//Criar os resumos zerados
		for (int i = 0; i < tiposAtividadesValidas.length; i++) {
			StravaActivityType tipo = tiposAtividadesValidas[i];
			
			ResumoAtividade resumo = resumosAtividades.get(tipo);
			if ( resumo==null ) {
				resumo = new ResumoAtividade(tipo);	
				resumosAtividades.put(tipo, resumo);		
			}
		}
		
		return listaAtividades;
		
	}

}
