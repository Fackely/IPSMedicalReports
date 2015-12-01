package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.ProgramasHallazgoPieza;

import util.ConstantesBD;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * May 20, 2010 - 10:42:55 AM
 */
public class DtoInclusionesPresupuesto implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -624963711367846806L;
	/**
	 * 
	 */
	private BigDecimal codigoPk ;
	
	
	/**
	 * 
	 */
	private BigDecimal valor;

	/**
	 * Código del registro de {@link IncluPresuEncabezado}
	 */
	private long codigoIncluPresuEncabezado;
	
	
	/**
	 * Código del registro de {@link ProgramasHallazgoPieza}
	 */
	private long codigoProgramaHallazgoPieza;
	
	/**
	 * 
	 */
	public DtoInclusionesPresupuesto (BigDecimal valor, long codigoIncluPresuEncabezado, long codigoProgramaHallazgoPieza) {
		
		super();
		reset();
		this.valor= valor;
		this.codigoIncluPresuEncabezado = codigoIncluPresuEncabezado;
		this.codigoProgramaHallazgoPieza = codigoProgramaHallazgoPieza;
	}

	/**
	 * 
	 */
	public DtoInclusionesPresupuesto() {
		super();
		reset();
	}

	/**
	 * 
	 */
	public void reset()
	{
		this.codigoPk = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.codigoIncluPresuEncabezado = ConstantesBD.codigoNuncaValidoLong;
	}

	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor()
	{
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor)
	{
		this.valor = valor;
	}

	/**
	 * @param codigoIncluPresuEncabezado the codigoIncluPresuEncabezado to set
	 */
	public void setCodigoIncluPresuEncabezado(long codigoIncluPresuEncabezado) {
		this.codigoIncluPresuEncabezado = codigoIncluPresuEncabezado;
	}

	/**
	 * @return the codigoIncluPresuEncabezado
	 */
	public long getCodigoIncluPresuEncabezado() {
		return codigoIncluPresuEncabezado;
	}

	/**
	 * @param codigoProgramaHallazgoPieza the codigoProgramaHallazgoPieza to set
	 */
	public void setCodigoProgramaHallazgoPieza(long codigoProgramaHallazgoPieza) {
		this.codigoProgramaHallazgoPieza = codigoProgramaHallazgoPieza;
	}

	/**
	 * @return the codigoProgramaHallazgoPieza
	 */
	public long getCodigoProgramaHallazgoPieza() {
		return codigoProgramaHallazgoPieza;
	}
}
