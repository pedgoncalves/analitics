package br.com.pedrogoncalves.utils;

import java.math.BigDecimal;

public class Utils {

	public static double arrendarPercentual(double valor) {
		if ( valor <0 ) valor = 0;
		if ( valor >100 ) valor = 100;
       
        return arredondar(valor);
	}

	public static double arredondar(double valor) {
		BigDecimal bd = new BigDecimal(Double.toString(valor));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        
		return bd.doubleValue();
	}
	
	public static String converterHoraFormatada(Integer tempoSegundos) {		
		int h = (int) (tempoSegundos/3600);
		int m = (int) ((tempoSegundos-(h*3600))/60);
		int s = (int) ((tempoSegundos-(h*3600))-m*60);
		
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
		return sBuilder.toString();
	}
}
