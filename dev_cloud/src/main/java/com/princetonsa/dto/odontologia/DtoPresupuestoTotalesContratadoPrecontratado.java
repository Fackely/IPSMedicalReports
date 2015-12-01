package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;

import util.UtilidadTexto;

public class DtoPresupuestoTotalesContratadoPrecontratado implements Serializable , Cloneable {

	private BigDecimal totalPresupuesto ;
	private BigDecimal totalPresupuestoParaDescuento;
	private BigDecimal descuento;
	private String mensaje;
	private String estadoDescuento;
	private BigDecimal descuentoNoAutorizadoNoContratado;
	private BigDecimal codigoPkDcto;
	private String estadoDescuentoNuevo;
	private boolean existeCambioTotalPresupuestoParaDcto;
	private String generarNuevaSolicitudDescuento;
	
	public DtoPresupuestoTotalesContratadoPrecontratado()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.totalPresupuesto = new BigDecimal(0);
		this.totalPresupuestoParaDescuento = new BigDecimal(0);
		this.descuento = new BigDecimal(0);
		this.mensaje="";
		this.estadoDescuento="";
		this.descuentoNoAutorizadoNoContratado= new BigDecimal(0);
		this.codigoPkDcto= new BigDecimal(0);
		this.estadoDescuentoNuevo= "";
		this.existeCambioTotalPresupuestoParaDcto= false;
		this.generarNuevaSolicitudDescuento="";
	}

	/**
	 * @return the totalPresupuesto
	 */
	public BigDecimal getTotalPresupuesto() {
		return totalPresupuesto;
	}
	
	/**
	 * @param totalPresupuesto the totalPresupuesto to set
	 */
	public void setTotalPresupuesto(BigDecimal totalPresupuesto) {
		this.totalPresupuesto = totalPresupuesto;
	}

	/**
	 * @return the totalPresupuestoParaDescuento
	 */
	public BigDecimal getTotalPresupuestoParaDescuento() {
		return totalPresupuestoParaDescuento;
	}
	
	/**
	 * @param totalPresupuestoParaDescuento the totalPresupuestoParaDescuento to set
	 */
	public void setTotalPresupuestoParaDescuento(
			BigDecimal totalPresupuestoParaDescuento) {
		this.totalPresupuestoParaDescuento = totalPresupuestoParaDescuento;
	}
	
	
	
	
	/**
	 * @return the descuento
	 */
	public BigDecimal getDescuento() {
		return descuento;
	}
	
	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public String getTotalAContratar()
	{
		String tmp="";
		try {
			tmp= UtilidadTexto.formatearValores((this.getTotalPresupuesto().subtract(this.getDescuento())).doubleValue());
		} catch (Exception e) {
			Log4JManager.info("Errores Total a Contratar "+e.getMessage());
		}
		if( UtilidadTexto.isEmpty(tmp))
		{
			tmp="0.0";
		}
		
		return 	tmp;
	}

	
	public String getDescuentoFormateado()
	{
		
		return UtilidadTexto.formatearValores(this.getDescuento().doubleValue());
	}
	
	public String getTotalPresupuestoParaDescuentoFormateado()
	{
		
		return UtilidadTexto.formatearValores(this.getTotalPresupuestoParaDescuento().doubleValue());
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getTotalPresupuestoFormateado()
	{
		String tmp="";
	
		/*
		 * TODO OJO es 0.0 o vacio?
		 */
		try{
			tmp=UtilidadTexto.formatearValores(this.getTotalPresupuesto().doubleValue());
		}
		catch (Exception e) {
		
			Log4JManager.info("error formato de Total presupuseto por "+e.getMessage());
		}
		
		
		if(UtilidadTexto.isEmpty(tmp))
		{
			tmp="0.0";
		}
		
		return tmp ;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * @return the estadoDescuento
	 */
	public String getEstadoDescuento()
	{
		return estadoDescuento;
	}

	/**
	 * @return the descuentoNoAutorizadoNoContratado
	 */
	public BigDecimal getDescuentoNoAutorizadoNoContratado()
	{
		return descuentoNoAutorizadoNoContratado;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}

	/**
	 * @param estadoDescuento the estadoDescuento to set
	 */
	public void setEstadoDescuento(String estadoDescuento)
	{
		this.estadoDescuento = estadoDescuento;
	}

	/**
	 * @param descuentoNoAutorizadoNoContratado the descuentoNoAutorizadoNoContratado to set
	 */
	public void setDescuentoNoAutorizadoNoContratado(
			BigDecimal descuentoNoAutorizadoNoContratado)
	{
		this.descuentoNoAutorizadoNoContratado = descuentoNoAutorizadoNoContratado;
	}

	/**
	 * @return the codigoPkDcto
	 */
	public BigDecimal getCodigoPkDcto()
	{
		return codigoPkDcto;
	}

	/**
	 * @param codigoPkDcto the codigoPkDcto to set
	 */
	public void setCodigoPkDcto(BigDecimal codigoPkDcto)
	{
		this.codigoPkDcto = codigoPkDcto;
	}

	/**
	 * @return the estadoDescuentoNuevo
	 */
	public String getEstadoDescuentoNuevo()
	{
		return estadoDescuentoNuevo;
	}

	/**
	 * @param estadoDescuentoNuevo the estadoDescuentoNuevo to set
	 */
	public void setEstadoDescuentoNuevo(String estadoDescuentoNuevo)
	{
		this.estadoDescuentoNuevo = estadoDescuentoNuevo;
	}

	/**
	 * @return the existeCambioTotalPresupuestoParaDcto
	 */
	public boolean isExisteCambioTotalPresupuestoParaDcto()
	{
		return existeCambioTotalPresupuestoParaDcto;
	}

	/**
	 * @param existeCambioTotalPresupuestoParaDcto the existeCambioTotalPresupuestoParaDcto to set
	 */
	public void setExisteCambioTotalPresupuestoParaDcto(
			boolean existeCambioTotalPresupuestoParaDcto)
	{
		this.existeCambioTotalPresupuestoParaDcto = existeCambioTotalPresupuestoParaDcto;
	}

	/**
	 * @param generarNuevaSolicitudDescuento the generarNuevaSolicitudDescuento to set
	 */
	public void setGenerarNuevaSolicitudDescuento(
			String generarNuevaSolicitudDescuento)
	{
		this.generarNuevaSolicitudDescuento = generarNuevaSolicitudDescuento;
	}
	
	/**
	 * @return the existeCambioTotalPresupuestoParaDcto
	 */
	public boolean getExisteCambioTotalPresupuestoParaDcto()
	{
		return existeCambioTotalPresupuestoParaDcto;
	}

	/**
	 * @return the generarNuevaSolicitudDescuento
	 */
	public String getGenerarNuevaSolicitudDescuento()
	{
		return generarNuevaSolicitudDescuento;
	}
	
	
	
}
