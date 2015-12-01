package util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author axioma
 *
 */
public class RangosConsecutivos implements Serializable 
{
	/**
	 * 
	 */
	private BigDecimal rangoInicial;
	
	/**
	 * 
	 */
	private BigDecimal rangoFinal;

	/**
	 * 
	 * @param rangoInicial
	 * @param rangoFinal
	 */
	public RangosConsecutivos(BigDecimal rangoInicial, BigDecimal rangoFinal) {
		super();
		this.rangoInicial = rangoInicial;
		this.rangoFinal = rangoFinal;
	}

	/**
	 * 
	 * @return
	 */
	public boolean estaEnRango(BigDecimal valorEvaluar)
	{
		return (valorEvaluar.doubleValue()>=rangoInicial.doubleValue() && valorEvaluar.doubleValue()<=rangoFinal.doubleValue());
	}
	
	
	/**
	 * @return the rangoInicial
	 */
	public BigDecimal getRangoInicial() {
		return rangoInicial;
	}

	/**
	 * @param rangoInicial the rangoInicial to set
	 */
	public void setRangoInicial(BigDecimal rangoInicial) {
		this.rangoInicial = rangoInicial;
	}

	/**
	 * @return the rangoFinal
	 */
	public BigDecimal getRangoFinal() {
		return rangoFinal;
	}

	/**
	 * @param rangoFinal the rangoFinal to set
	 */
	public void setRangoFinal(BigDecimal rangoFinal) {
		this.rangoFinal = rangoFinal;
	}

	@Override
	public String toString() {
		return rangoInicial+" - "+rangoFinal;
	}
	
	
}
