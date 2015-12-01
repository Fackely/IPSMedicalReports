package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * 
 * @author Edgar Carvajal Ruiz
 * DTO Historcios para cargar los ingreso de un paciente y su presupuesto asociado
 *
 */
public class DtoHistoricoIngresoPresupuesto implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String noIngreso;
	private String consecutivoIngreso;
	private String estadoIngreso;
	private String ayudanteEstadoIngreso;
	private String fechaIngreso;
	private String centroAtencion; //nombre Centro Atencion
	private String estadoCuenta;
	private String ayudanteEstadoCuenta;
	private BigDecimal codigoPresupuesto;
	
	
	
	





	/**
	 * Contrutor Historico Ingreso Presupuesto 
	 */
	public DtoHistoricoIngresoPresupuesto (){
		this.noIngreso="";
		this.estadoIngreso="";
		this.fechaIngreso="";
		this.centroAtencion="";
		this.estadoCuenta="";
		this.setConsecutivoIngreso("");
		this.codigoPresupuesto =BigDecimal.ZERO;
		this.ayudanteEstadoCuenta="";
		this.ayudanteEstadoIngreso="";
		
	}




	/**
	 * @return the noIngreso
	 */
	public String getNoIngreso() {
		return noIngreso;
	}




	/**
	 * @return the estadoIngreso
	 */
	public String getEstadoIngreso() {
		return estadoIngreso;
	}




	/**
	 * Cargar la fecha ingreso en formato de presentacion 
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		if(!UtilidadTexto.isEmpty(this.fechaIngreso) )
		{
		  this.fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(this.fechaIngreso);	
		}
		return fechaIngreso;
	}




	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}




	/**
	 * @return the estadoCuenta
	 */
	public String getEstadoCuenta() {
		return estadoCuenta;
	}




	/**
	 * @param noIngreso the noIngreso to set
	 */
	public void setNoIngreso(String noIngreso) {
		this.noIngreso = noIngreso;
	}




	/**
	 * @param estadoIngreso the estadoIngreso to set
	 */
	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}




	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}




	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}




	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}




	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}




	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}




	public void setCodigoPresupuesto(BigDecimal codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}




	public BigDecimal getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	/**
	 * @return the ayudanteEstadoIngreso
	 */
	public String getAyudanteEstadoIngreso() {
		
		try {
			
			if( !UtilidadTexto.isEmpty(this.estadoIngreso) )
			{
				ayudanteEstadoIngreso=ValoresPorDefecto.getIntegridadDominio(this.estadoIngreso).toString();
			}
			
		} catch (Exception e) {
			Log4JManager.info(e);
		}
		return ayudanteEstadoIngreso;
	}




	/**
	 * @return the ayudanteEstadoCuenta
	 */
	public String getAyudanteEstadoCuenta() {
		
	
		
		return ayudanteEstadoCuenta;
	}




	/**
	 * @param ayudanteEstadoIngreso the ayudanteEstadoIngreso to set
	 */
	public void setAyudanteEstadoIngreso(String ayudanteEstadoIngreso) {
		this.ayudanteEstadoIngreso = ayudanteEstadoIngreso;
	}




	/**
	 * @param ayudanteEstadoCuenta the ayudanteEstadoCuenta to set
	 */
	public void setAyudanteEstadoCuenta(String ayudanteEstadoCuenta) {
		this.ayudanteEstadoCuenta = ayudanteEstadoCuenta;
	}
	
	
	
	
	
	



}
