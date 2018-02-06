package br.com.pedrogoncalves.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;

import br.com.pedrogoncalves.strava.AtividadeStrava;
import br.com.pedrogoncalves.strava.StravaUtils;
import br.com.pedrogoncalves.utils.Utils;
import javastrava.api.v3.model.reference.StravaActivityType;

@ManagedBean(name="dashboardBean")
public class DashboardBean {
		
    private StravaActivityType tiposAtividadesValidas[] = {StravaActivityType.RUN, StravaActivityType.RIDE, StravaActivityType.SWIM, StravaActivityType.WALK};
	
	private LineChartModel lineModel;
	private MeterGaugeChartModel model;
	
    private List<AtividadeStrava> listaAtividades;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int anoSelecionado = Calendar.getInstance().get(Calendar.YEAR);
    
    private double metaCorrida = 1500;
	
	public String getNomeUsuarioLogado() {
		return StravaUtils.getInstance().getNomeUsuarioLogado();
	}
	
	public String getEmailUsuarioLogado() {
		return StravaUtils.getInstance().getEmailUsuario();
	}
	
	@SuppressWarnings("serial")
	public MeterGaugeChartModel getMeterGaugeModel() {
		
		if ( listaAtividades==null ) getListaAtividades();
		
		double total = getTotalCorrida();
		
		List<Number> intervals = new ArrayList<Number>(){{ 
        }};
         
        model = new MeterGaugeChartModel(total, intervals);
        model.setExtender("skinMeterGauge");
        model.setMin(0);
        model.setMax(metaCorrida);
        model.setTitle( ((total/metaCorrida)*100)+"%" );
        model.setLegendPosition("bottom");
        
        return model;
        
	}

	public double getTotalCorrida() {
		if ( listaAtividades==null ) getListaAtividades();
		
		double total = 0;
		for (AtividadeStrava atividade: listaAtividades) {
        	if ( atividade.getTipo().equals(StravaActivityType.RUN) ) {
        		total += atividade.getDistancia();
        	}
        }
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
	
	public List<AtividadeStrava> getListaAtividades() {				
		listaAtividades = StravaUtils.getInstance().buscarAtividades( anoSelecionado );		
		return listaAtividades;
		
	}

}
