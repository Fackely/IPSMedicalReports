package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;




/**
 * 
 * @author Edgar Carvajal
 *
 */
public class DtoDetalleCuotasOdontoEsp implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int codigoPk;
	private String tipoCuota;
	private int nroCuotas;
	private String ayudanteNroCuotas;
	private BigDecimal porcentaje;
	private String ayudantePorcentaje;
	private BigDecimal valor;
	private String ayudanteValor;
	private boolean activo;
	
	
	
	/**
	 * CONSTRUTOR
	 */
	public DtoDetalleCuotasOdontoEsp()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.tipoCuota="";
		this.nroCuotas=ConstantesBD.codigoNuncaValido;
		this.porcentaje=BigDecimal.ZERO;
		this.ayudantePorcentaje="";
		this.valor=BigDecimal.ZERO;
		this.ayudanteValor="";
		this.activo=Boolean.TRUE;
		this.ayudanteNroCuotas="";
	}


	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}


	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	/**
	 * @return the tipoCuota
	 */
	public String getTipoCuota() {
		return tipoCuota;
	}


	/**
	 * @param tipoCuota the tipoCuota to set
	 */
	public void setTipoCuota(String tipoCuota) {
		this.tipoCuota = tipoCuota;
	}


	/**
	 * @return the nroCuotas
	 */
	public int getNroCuotas() 
	{
		
		if(UtilidadTexto.isEmpty(this.ayudanteNroCuotas)  )
		{
			nroCuotas=ConstantesBD.codigoNuncaValido;
		}
		else
		{
			nroCuotas=Utilidades.convertirAEntero(this.ayudanteNroCuotas);
		}
		
		return nroCuotas;
	}


	/**
	 * @param nroCuotas the nroCuotas to set
	 */
	public void setNroCuotas(int nroCuotas) {
		this.nroCuotas = nroCuotas;
	}


	/**
	 * @return the porcentaje
	 */
	public BigDecimal getPorcentaje() 
	{
		if (!UtilidadTexto.isEmpty(this.ayudantePorcentaje) )
		{
			this.porcentaje = new BigDecimal(this.ayudantePorcentaje);
		}
		else 
		{
			this.porcentaje= new BigDecimal(ConstantesBD.codigoNuncaValido);
		}
	
		return porcentaje;
	}


	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(BigDecimal porcentaje) 
	{
		this.porcentaje = porcentaje;
	}


	/**
	 * @return the ayudantePorcentaje
	 */
	public String getAyudantePorcentaje() 
	{
		
		if( this.porcentaje!=null && this.porcentaje.floatValue()>0)
		{
			this.ayudantePorcentaje=this.porcentaje.floatValue()+"";
		}
		
		return ayudantePorcentaje;
	}


	/**
	 * @param ayudantePorcentaje the ayudantePorcentaje to set
	 */
	public void setAyudantePorcentaje(String ayudantePorcentaje) 
	{
		this.ayudantePorcentaje = ayudantePorcentaje;
	}


	/**
	 * @return the valor
	 */
	public BigDecimal getValor() 
	{
		
		if(!UtilidadTexto.isEmpty(this.ayudanteValor))
		{
			this.valor = new BigDecimal(this.ayudanteValor);
		}
		else 
		{
			this.valor=new BigDecimal(ConstantesBD.codigoNuncaValido);
		}
		
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}


	/**
	 * @return the ayudanteValor
	 */
	public String getAyudanteValor() 
	
	{
		
		if(this.valor!=null && this.valor.floatValue()>0)
		{
			this.ayudanteValor=this.valor.floatValue()+"";
		}
		
		return ayudanteValor;
	}


	/**
	 * @param ayudanteValor the ayudanteValor to set
	 */
	public void setAyudanteValor(String ayudanteValor) {
		this.ayudanteValor = ayudanteValor;
	}


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param activo
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public boolean getActivo() 
	{
		return activo;
	}


	public void setAyudanteNroCuotas(String ayudanteNroCuotas) 
	{
		this.ayudanteNroCuotas = ayudanteNroCuotas;
	}


	public String getAyudanteNroCuotas() 
	{
		if( this.nroCuotas>0 )
		{
			this.ayudanteNroCuotas=this.nroCuotas+"";
		}
		
		return ayudanteNroCuotas;
	}
	
	
	

}
