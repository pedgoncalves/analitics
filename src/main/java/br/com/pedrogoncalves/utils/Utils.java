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
	
}
