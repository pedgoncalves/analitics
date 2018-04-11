package br.com.pedrogoncalves.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.pedrogoncalves.entidades.AtividadeStrava;
import br.com.pedrogoncalves.entidades.ResumoAtividade;
import br.com.pedrogoncalves.entidades.ResumoLocalizacaoAtividade;
import br.com.pedrogoncalves.strava.StravaUtils;
import br.com.pedrogoncalves.utils.Constants;
import br.com.pedrogoncalves.utils.Utils;
import javastrava.api.v3.model.reference.StravaActivityType;


@ManagedBean(name="dashboardBean")
public class DashboardBean {
		
    //Filtro
    private int anoSelecionado = Calendar.getInstance().get(Calendar.YEAR);
    private StravaActivityType tipoAtividadeSelecionado = StravaActivityType.RUN;
    
    private List<AtividadeStrava> listaAtividades;
    private Map<StravaActivityType, ResumoAtividade> resumosAtividades;
    private Map<StravaActivityType, ResumoLocalizacaoAtividade> resumoLocalizacaoAtividade;
	
	private StravaActivityType tiposAtividadesValidas[] = {StravaActivityType.RUN};
    private StravaActivityType tiposStrava[] = {StravaActivityType.RUN, StravaActivityType.SWIM, StravaActivityType.RIDE};	
	
    private double metaCorrida = 1500;
    
	private LineChartModel acumuladoModel;
	private LineChartModel atividadeDiaModel;
	private BubbleChartModel bubbleModel;
	private LineChartModel caloriasMesModel;
	private BarChartModel quantidadeMesChartModel;
	private LineChartModel ritmoChartModel;
	private BarChartModel atividadeMesModel;
	private BarChartModel atividadeTempoModel;
	private LineChartModel atividadeSemanaModel;
	
	private String dataAtividadesFormatado;
	
	@PostConstruct
    public void init() {
		popularListaAtividades();
		
		createAtividadeDiaModel();	
        createAcumuladoModel();
        createBubbleModel();
        createCaloriasMesModel();
        createQuantidadeMesModel();
        createRitmoMesModel();
        createAtividadeMesModel();
        createAtividadeTempoModel();	
        createAtividadeSemanaModel();
        
        createCalendarioAtividade();
	}

	private void createCalendarioAtividade() {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd");
		
		//[ new Date(2012, 3, 13), 37032 ],
		for (AtividadeStrava atividade:listaAtividades) {
			if ( atividade.getTipo().equals(tipoAtividadeSelecionado) ) {
				sb.append("[ new Date(");
				sb.append(sdf.format(atividade.getData()));			
				sb.append("),");
				sb.append(atividade.getDistancia()/1000);
				sb.append("], ");			
			}
		}
		
		/*for (AtividadeStrava atividade:listaAtividades) {
			if ( atividade.getTipo().equals(tipoAtividadeSelecionado) ) {
				sb.append("['");
				sb.append(sdf.format(atividade.getData()));			
				sb.append("',");
				sb.append(atividade.getDistancia()/1000);
				sb.append("], ");			
			}
		}*/					
		dataAtividadesFormatado = sb.toString();
	}
	
	public String getNomeUsuarioLogado() {
		return StravaUtils.getInstance().getNomeUsuarioLogado();
	}
	
	public String getEmailUsuarioLogado() {
		return StravaUtils.getInstance().getEmailUsuario();
	}		
	
	public List<AtividadeStrava> getListaAtividades() {
		return listaAtividades;
	}
	
	public LineChartModel getAcumuladoModel() { 
		return acumuladoModel;
	}

	public LineChartModel getLineModel() {	
		return atividadeDiaModel;
	}
	
	public BubbleChartModel getQuantidadeMesBubble() {
		return bubbleModel;
	}
	
	public LineChartModel getCaloriasMesGraph() {	
		return caloriasMesModel;
	}
	
	public BarChartModel getQuantidadeMesGraph() {	
		return quantidadeMesChartModel;
	}
	
	public LineChartModel getRitmoMesGraph() {		
		return ritmoChartModel;
	}	
	
	public BarChartModel getMonthGraph() {		
		return atividadeMesModel;	
	}
	
	public BarChartModel getTempoGraph() {		
		return 	atividadeTempoModel;
	}
	
	public LineChartModel getSemanaGraph() {	
		return atividadeSemanaModel;		
	}
		
	public String getDatasAtividades() {
		return dataAtividadesFormatado;
	}
	
	public String getLocalizacaoCorrida() {
		StravaActivityType stravaActivityType = StravaActivityType.RUN;			
		ResumoLocalizacaoAtividade resumo = resumoLocalizacaoAtividade.get(stravaActivityType);
		
		return resumo.toString();
	}
	
	public Integer getQuantidadeTotalGeralAtividades() {
		Integer total = 0;
		for (int i = 0; i < tiposAtividadesValidas.length; i++) {
			StravaActivityType stravaActivityType = tiposAtividadesValidas[i];
			
			ResumoAtividade resumo = resumosAtividades.get(stravaActivityType);
			total += resumo.getQuantidadeTotal();
		}
		
		return total;
	}
	
	public Integer getQuantidadeTotalGeralCorrida() {
		Integer total = 0;
		
		ResumoAtividade resumo = resumosAtividades.get(StravaActivityType.RUN);		
		if ( resumo!=null ) total = resumo.getQuantidadeTotal();
		
		return total;
	}
	
	public Integer getQuantidadeTotalGeralNatacao() {
		Integer total = 0;
		
		ResumoAtividade resumo = resumosAtividades.get(StravaActivityType.SWIM);		
		if ( resumo!=null ) total = resumo.getQuantidadeTotal();		
		
		return total;
	}
	
	public Integer getQuantidadeTotalGeralCiclismo() {
		Integer total = 0;
		
		ResumoAtividade resumo = resumosAtividades.get(StravaActivityType.RIDE);
		if ( resumo!=null ) total = resumo.getQuantidadeTotal();
		
		return total;
	}
	
	public double getTotalCorrida() {
		float total = 0;
		
		ResumoAtividade resumo = resumosAtividades.get(StravaActivityType.RUN);		
		if ( resumo!=null ) total = resumo.getDistanciaTotal();
		
		return Utils.arredondar(total/1000);
	
	}
	
	public List<AtividadeStrava> popularListaAtividades() {				
		listaAtividades = StravaUtils.getInstance().buscarAtividades( anoSelecionado );
		//listaAtividades = StravaUtils.getInstance().buscarTodasAtividades();
		
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
	
	public void filtrar() {
		init();		
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

	public int getAnoSelecionado() {
		return anoSelecionado;
	}

	public void setAnoSelecionado(int anoSelecionado) {
		this.anoSelecionado = anoSelecionado;
	}

	public StravaActivityType getTipoSelecionado() {
		return tipoAtividadeSelecionado;
	}

	public void setTipoSelecionado(StravaActivityType tipoSelecionado) {
		this.tipoAtividadeSelecionado = tipoSelecionado;
		this.tiposAtividadesValidas = new StravaActivityType[]{tipoSelecionado}; 
	}

	public StravaActivityType[] getTiposStrava() {
		return tiposStrava;
	}

	public void setTiposStrava(StravaActivityType[] tiposStrava) {
		this.tiposStrava = tiposStrava;
	}	
	
	
	// ---------------------- Criação dos modelos de gráficos	
	private LineChartModel createAtividadeDiaModel() {
		atividadeDiaModel = new LineChartModel();	
        atividadeDiaModel.setExtender(Constants.CHART_SKIN_PADRAO);
        atividadeDiaModel.setAnimate(true);
        
        Axis yAxis = atividadeDiaModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        
        DateAxis xAxis = new DateAxis();
        xAxis.setTickAngle(-50);        
        xAxis.setTickFormat("%b %#d, %y");
                
        atividadeDiaModel.getAxes().put(AxisType.X, xAxis);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Constants.LOCALIZACAO);
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	StravaActivityType tipo = tiposAtividadesValidas[i];
        	
        	LineChartSeries series1 = new LineChartSeries();
            series1.setLabel(tipo.getValue());
            for (AtividadeStrava atividade: listaAtividades) {
            	if ( atividade.getTipo().equals(tipo) ) {
            		series1.set(sdf.format(atividade.getData()),atividade.getDistancia()/1000);
            	}
            }
            
            if ( !series1.getData().isEmpty() ) atividadeDiaModel.addSeries(series1); 
		}        		
		
		return atividadeDiaModel;
	}	
	
	@SuppressWarnings("deprecation")
	private LineChartModel createAcumuladoModel() {	
		acumuladoModel = new LineChartModel();
        acumuladoModel.setExtender(Constants.CHART_SKIN_STEP);
        acumuladoModel.setAnimate(true);
        acumuladoModel.setLegendPosition(Constants.LEGEND_POSITION);
        acumuladoModel.setLegendPlacement(Constants.LEGEND_PLACEMENT);
        acumuladoModel.setLegendRows(1);   
        acumuladoModel.setSeriesColors("3E98D3,EF3F61,2BB673,F15732");


        acumuladoModel.getAxis(AxisType.Y).setMin(0);
        acumuladoModel.getAxis(AxisType.X).setTickCount(31);
        acumuladoModel.getAxis(AxisType.X).setMin(1);
        acumuladoModel.getAxis(AxisType.X).setMax(31);
        
        SimpleDateFormat sdfCompet = new SimpleDateFormat("MM/yyyy",Constants.LOCALIZACAO);        
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        
        	StravaActivityType tipo = tiposAtividadesValidas[i];
        	SortedMap<String, List<AtividadeStrava>> htCompetencia = new TreeMap<String, List<AtividadeStrava>>();
	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                		
            		String data = sdfCompet.format(atividade.getData());           		
            		
            		List<AtividadeStrava> lista = htCompetencia.get(data);
            		if (lista==null) lista = new ArrayList<AtividadeStrava>();
            		
            		lista.add(atividade);
            		
            		htCompetencia.put(data, lista);
            	}
            }
            
            if ( htCompetencia.entrySet().size()>0 ) {
        		
                
                Iterator<String> e = htCompetencia.keySet().iterator();
        		while(e.hasNext()) {        			
        			String data = e.next();
        			List<AtividadeStrava> lista = htCompetencia.get(data);
        			
        			LineChartSeries serie = new LineChartSeries(data);        			
                    Float acumulado = 0f;;
                    for(int dia=1; dia<31; dia++) {
                    	for (AtividadeStrava atividadeStrava: lista) {
                        	if (atividadeStrava.getData().getDate()==dia) {
                        		acumulado += atividadeStrava.getDistancia();
                        		serie.set(dia, acumulado/1000);  
                        	}
                        }
                    }                    
            		
            		if ( !serie.getData().isEmpty() ) acumuladoModel.addSeries(serie);   
        		}        		       		
        	}  
		}     

		return acumuladoModel;		
	}

	private LineChartModel createCaloriasMesModel() {
		caloriasMesModel = new LineChartModel();
		caloriasMesModel.setExtender(Constants.CHART_SKIN_PADRAO);
		caloriasMesModel.setLegendPosition(Constants.LEGEND_POSITION);
		caloriasMesModel.setLegendPlacement(Constants.LEGEND_PLACEMENT);
		caloriasMesModel.setLegendRows(1);
		caloriasMesModel.setAnimate(true); 
		caloriasMesModel.setShowPointLabels(true);
		caloriasMesModel.setMouseoverHighlight(false);
        
		caloriasMesModel.getAxis(AxisType.Y).setMin(0);               
		caloriasMesModel.getAxes().put(AxisType.X, new CategoryAxis());
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy",Constants.LOCALIZACAO);
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	
        	StravaActivityType tipo = tiposAtividadesValidas[i];        	
        	SortedMap<String, Float> htCaloria = new TreeMap<String, Float>();
        	        	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                		
            		String data = sdf.format(atividade.getData());            		
            		
            		Float calorias = htCaloria.get(data);            		
            		if ( calorias==null ) calorias = 0f;              		
            		if ( atividade.getCalorias()==null ) continue;
            		calorias += atividade.getCalorias();            		
            		htCaloria.put(data, calorias);
            	}
            }
            
            if ( htCaloria.entrySet().size()>0 ) {
        		
        		ChartSeries serie = new ChartSeries();
                serie.setLabel(Constants.LABEL_CALORIAS);
                
                Iterator<String> e = htCaloria.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			serie.set(data, htCaloria.get(data));
        		}        		
        		caloriasMesModel.addSeries(serie);        		
        	}
		}
		
		return caloriasMesModel;
	}
			
	private BarChartModel createQuantidadeMesModel() {
		quantidadeMesChartModel = new BarChartModel();
		quantidadeMesChartModel.setExtender(Constants.CHART_SKIN_BAR);
		quantidadeMesChartModel.setLegendPosition(Constants.LEGEND_POSITION);
		quantidadeMesChartModel.setLegendPlacement(Constants.LEGEND_PLACEMENT);
		quantidadeMesChartModel.setLegendRows(1);
		quantidadeMesChartModel.setAnimate(true); 
		quantidadeMesChartModel.setShowPointLabels(true);
		quantidadeMesChartModel.setMouseoverHighlight(false);
        
        Axis yAxis = quantidadeMesChartModel.getAxis(AxisType.Y);
        yAxis.setMin(0); 
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy",Constants.LOCALIZACAO);
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	
        	StravaActivityType tipo = tiposAtividadesValidas[i];        	
        	SortedMap<String, Integer> htQuantidade = new TreeMap<String, Integer>();
        	        	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                		
            		String data = sdf.format(atividade.getData());            		
            		
            		Integer quantidade = htQuantidade.get(data);            		
            		if ( quantidade==null ) quantidade = 0;            		
            		quantidade += 1;            		
            		htQuantidade.put(data, quantidade);
            	}
            }
            
            if ( htQuantidade.entrySet().size()>0 ) {
        		
        		ChartSeries serie = new ChartSeries();
                serie.setLabel(Constants.LABEL_QUANTIDADE);
                
                Iterator<String> e = htQuantidade.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			serie.set(data, htQuantidade.get(data));
        		}        		
        		quantidadeMesChartModel.addSeries(serie);        		
        	}
		}
		
		return quantidadeMesChartModel;
	}
		
	private BubbleChartModel createBubbleModel() {
		bubbleModel = new BubbleChartModel();		
		bubbleModel.setExtender(Constants.CHART_SKIN_BUBBLE);
        bubbleModel.setAnimate(true);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy",Constants.LOCALIZACAO);
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	
        	StravaActivityType tipo = tiposAtividadesValidas[i];        	
        	SortedMap<String, Integer> htQuantidade = new TreeMap<String, Integer>();
        	        	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                		
            		String data = sdf.format(atividade.getData());            		
            		
            		Integer quantidade = htQuantidade.get(data);            		
            		if ( quantidade==null ) quantidade = 0;            		
            		quantidade += 1;            		
            		htQuantidade.put(data, quantidade);
            	}
            }
            
            if ( htQuantidade.entrySet().size()>0 ) {
	               
                Iterator<String> e = htQuantidade.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			Integer qtd = htQuantidade.get(data);
        			Integer mes = Integer.parseInt(data.substring(0, data.indexOf("/")));
            		bubbleModel.add(new BubbleChartSeries(data+"("+qtd+")",mes,qtd,qtd));       
        		}        		 		
        	}
		}        
		
		return bubbleModel;
	}
	
	private LineChartModel createRitmoMesModel() {
		ritmoChartModel = new LineChartModel();
        ritmoChartModel.setExtender(Constants.CHART_SKIN_PADRAO);
                
        ritmoChartModel.getAxis(AxisType.Y).setMin(0);               
        ritmoChartModel.getAxes().put(AxisType.X, new CategoryAxis()); 
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy",Constants.LOCALIZACAO);        
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	
        	StravaActivityType tipo = tiposAtividadesValidas[i];        	
        	SortedMap<String, Float> htRitmo = new TreeMap<String, Float>();        	
        	SortedMap<String, Integer> htQuantidade = new TreeMap<String, Integer>();
        	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                		
            		String data = sdf.format(atividade.getData());            		
            		
            		Float ritmo = htRitmo.get(data);            		
            		if ( ritmo==null ) ritmo = 0f;            		
            		ritmo += atividade.getCadencia();            		
            		htRitmo.put(data, ritmo);
            		
            		Integer quantidade = htQuantidade.get(data);            		
            		if ( quantidade==null ) quantidade = 0;            		
            		quantidade += 1;            		
            		htQuantidade.put(data, quantidade);
            	}
            }

            if ( htRitmo.entrySet().size()>0 ) {
            	
        		LineChartSeries linha = new LineChartSeries();
                linha.setLabel(Constants.LABEL_RITMO);
                linha.setShowLine(false);
                 
                Iterator<String> e = htRitmo.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			linha.set(data, htRitmo.get(data)/htQuantidade.get(data));
        		}        		
        		ritmoChartModel.addSeries(linha);
        	}
		}        		
       
		return ritmoChartModel;
	}
	
	private BarChartModel createAtividadeMesModel() {
		atividadeMesModel = new BarChartModel();
        atividadeMesModel.setExtender(Constants.CHART_SKIN_BAR);
        atividadeMesModel.setLegendPosition(Constants.LEGEND_POSITION);
        atividadeMesModel.setLegendPlacement(Constants.LEGEND_PLACEMENT);
        atividadeMesModel.setLegendRows(1);
        atividadeMesModel.setAnimate(true); 
        atividadeMesModel.setShowPointLabels(true);
        atividadeMesModel.setMouseoverHighlight(false);
        
        Axis yAxis = atividadeMesModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy",Constants.LOCALIZACAO);        
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	
        	StravaActivityType tipo = tiposAtividadesValidas[i];        	
        	SortedMap<String, Double> htDistancia = new TreeMap<String, Double>();        	
        	SortedMap<String, Integer> htQuantidade = new TreeMap<String, Integer>();
        	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                		
            		String data = sdf.format(atividade.getData());            		
            		
            		Double distancia = htDistancia.get(data);            		
            		if ( distancia==null ) distancia = 0d;            		
            		distancia += atividade.getDistancia()/1000;            		
            		htDistancia.put(data, distancia);
            		
            		Integer quantidade = htQuantidade.get(data);            		
            		if ( quantidade==null ) quantidade = 0;            		
            		quantidade += 1;            		
            		htQuantidade.put(data, quantidade);
            	}
            }

            if ( htDistancia.entrySet().size()>0 ) {
        		
        		ChartSeries serie = new ChartSeries();
                serie.setLabel(Constants.LABEL_DISTANCIA_KM);                
                
                Iterator<String> e = htDistancia.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			serie.set(data, htDistancia.get(data));
        		}
        		atividadeMesModel.addSeries(serie);        		
        		
        		LineChartSeries linha = new LineChartSeries();
                linha.setLabel(Constants.LABEL_KM_MEDIO);         
                 
                e = htDistancia.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			Double media = htDistancia.get(data)/htQuantidade.get(data);
        			linha.set(data, media);
        		}        		
        		atividadeMesModel.addSeries(linha);
        	}
		}        		
       
		return atividadeMesModel;
	}
	
	private BarChartModel createAtividadeTempoModel() {
		atividadeTempoModel = new BarChartModel();
        atividadeTempoModel.setExtender(Constants.CHART_SKIN_BAR);
        atividadeTempoModel.setLegendPosition(Constants.LEGEND_POSITION);
        atividadeTempoModel.setLegendPlacement(Constants.LEGEND_PLACEMENT);
        atividadeTempoModel.setLegendRows(1);
        atividadeTempoModel.setAnimate(true); 
        atividadeTempoModel.setShowPointLabels(true);
        atividadeTempoModel.setMouseoverHighlight(false);
        atividadeTempoModel.setShowDatatip(false);
        
        Axis yAxis = atividadeTempoModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy",Constants.LOCALIZACAO);        
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        	
        	StravaActivityType tipo = tiposAtividadesValidas[i];        	
        	SortedMap<String, Double> htTempo = new TreeMap<String, Double>();        	
        	SortedMap<String, Integer> htQuantidade = new TreeMap<String, Integer>();
        	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                		
            		String data = sdf.format(atividade.getData());            		
            		
            		Double tempo = htTempo.get(data);            		
            		if ( tempo==null ) tempo = 0d;            		
            		tempo += atividade.getTempoSegundos();            		
            		htTempo.put(data, tempo);
            		
            		Integer quantidade = htQuantidade.get(data);            		
            		if ( quantidade==null ) quantidade = 0;            		
            		quantidade += 1;            		
            		htQuantidade.put(data, quantidade);
            	}
            }

            if ( htTempo.entrySet().size()>0 ) {
        		
        		ChartSeries serie = new ChartSeries();
                serie.setLabel(Constants.LABEL_TEMPO);                
                
                Iterator<String> e = htTempo.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			serie.set(data, htTempo.get(data)/3600d);
        		}
        		atividadeTempoModel.addSeries(serie);        		
        		
        		LineChartSeries linha = new LineChartSeries();
                linha.setLabel(Constants.LABEL_TEMPO_MEDIO);         
                 
                e = htTempo.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			linha.set(data, (htTempo.get(data)/3600)/htQuantidade.get(data));
        		}        		
        		atividadeTempoModel.addSeries(linha);
        	}
		}        		
       
		return atividadeTempoModel;
	}
	
	private LineChartModel createAtividadeSemanaModel() {
		atividadeSemanaModel = new LineChartModel();	
        atividadeSemanaModel.setLegendPosition(Constants.LEGEND_POSITION);
        atividadeSemanaModel.setLegendPlacement(Constants.LEGEND_PLACEMENT);
        atividadeSemanaModel.setLegendRows(1);
        atividadeSemanaModel.setAnimate(true); 
        atividadeSemanaModel.setShowPointLabels(true);
        atividadeSemanaModel.setMouseoverHighlight(false);
        atividadeSemanaModel.setExtender(Constants.CHART_SKIN_PADRAO);
        
        Axis yAxis = atividadeSemanaModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        
        atividadeSemanaModel.getAxes().put(AxisType.X, new CategoryAxis());        
              
        SimpleDateFormat sdf = new SimpleDateFormat("EEE",Constants.LOCALIZACAO);        
        for (int i = 0; i < tiposAtividadesValidas.length; i++) {
        
        	StravaActivityType tipo = tiposAtividadesValidas[i];
        	Map<String, Double> htDistancia = new LinkedHashMap<String, Double>();
        	Map<String, Integer> htQuantidade = new LinkedHashMap<String, Integer>();
        	
        	String dias[] = Constants.DATE_FORMAT_SYMBOLS.getShortWeekdays();
        	for (int j = 0; j < dias.length; j++) {
        		if (dias[j].equals("")) continue;
				htDistancia.put(dias[j], 0d);
				htQuantidade.put(dias[j], 0);
			}
        	        	       	
            for (AtividadeStrava atividade: listaAtividades) {
            	
            	if ( atividade.getTipo().equals(tipo) ) {                
            		String diaDaSemana = sdf.format(atividade.getData());
            		
            		Double distancia = htDistancia.get(diaDaSemana);
            		distancia += atividade.getDistancia()/1000;            		
            		htDistancia.put(diaDaSemana, distancia);
            		
            		Integer quantidade = htQuantidade.get(diaDaSemana);		
            		quantidade += 1;            		
            		htQuantidade.put(diaDaSemana, quantidade);
            	}
            }
            
            if ( htDistancia.entrySet().size()>0 ) {
        		
        		BarChartSeries serie = new BarChartSeries();
                serie.setLabel(Constants.LABEL_DISTANCIA_KM);                
                
                Iterator<String> e = htDistancia.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			serie.set(data, htDistancia.get(data));
        		}
        		atividadeSemanaModel.addSeries(serie);        		
        		
        		LineChartSeries linha = new LineChartSeries();
                linha.setLabel(Constants.LABEL_QUANTIDADE);         
                 
                e = htQuantidade.keySet().iterator();
        		while(e.hasNext()) {
        			String data = e.next();
        			linha.set(data, htQuantidade.get(data));
        		}        		
        		atividadeSemanaModel.addSeries(linha);
        	}
		}        		    
		
		return atividadeSemanaModel;
	}	
}
