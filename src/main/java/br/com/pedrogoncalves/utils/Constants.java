package br.com.pedrogoncalves.utils;

import java.text.DateFormatSymbols;
import java.util.Locale;

import org.primefaces.model.chart.LegendPlacement;

public class Constants {

	public static final Locale LOCALIZACAO = new Locale("pt","BR");
	public static final DateFormatSymbols DATE_FORMAT_SYMBOLS = new DateFormatSymbols(LOCALIZACAO);	
	public static final String LEGEND_POSITION = "s";
	public static final LegendPlacement LEGEND_PLACEMENT = LegendPlacement.OUTSIDEGRID;
	public static final String LABEL_DISTANCIA_KM = "Distância (KM)";
	public static final String LABEL_KM_MEDIO = "KM Médio";
	public static final String LABEL_QUANTIDADE = "Quantidade";
	public static final String LABEL_CALORIAS = "Calorias";
	public static final String LABEL_RITMO = "Ritmo Médio";
	public static final String LABEL_TEMPO = "Tempo (Horas)";
	public static final String LABEL_TEMPO_MEDIO = "Tempo Médio";
	public static final String CHART_SKIN_PADRAO = "skinChartDefault";
	public static final String CHART_SKIN_STEP= "skinChartStep";
	public static final String CHART_SKIN_BAR = "skinChartBar";
	public static final String CHART_SKIN_BUBBLE = "skinChartBubble";
	
}
