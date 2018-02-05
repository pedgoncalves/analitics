package br.com.pedrogoncalves.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.pedrogoncalves.strava.AtividadeStrava;
import br.com.pedrogoncalves.strava.StravaUtils;
import javastrava.api.v3.model.reference.StravaActivityType;

@ManagedBean(name="dashboardBean")
public class DashboardBean {
		
    private StravaActivityType tiposAtividadesValidas[] = {StravaActivityType.RUN, StravaActivityType.RIDE, StravaActivityType.SWIM, StravaActivityType.WALK};
	
	private LineChartModel lineModel;
    private List<AtividadeStrava> listaAtividades;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int anoSelecionado = Calendar.getInstance().get(Calendar.YEAR);
	
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
