package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;

public class DtoDescuentosOdontologicos implements Serializable , Cloneable {

	private double consecutivo;	
	private InfoDatosInt centroAtencion;
	private String fechaInicioVigencia;
	private String fechaFinVigencia;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	
	
	public DtoDescuentosOdontologicos(){
	  this.reset();	
	}
	
	void reset(){
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = new InfoDatosInt();
		this.fechaInicioVigencia = "";
		this.fechaFinVigencia = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
	}

	/**
	 * @return the consecutivo
	 */
	public double getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(double consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the fechaInicioVigencia
	 */
	public String getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	/**
	 * @param fechaInicioVigencia the fechaInicioVigencia to set
	 */
	public void setFechaInicioVigencia(String fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	/**
	 * @return the fechaFinVigencia
	 */
	public String getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia the fechaFinVigencia to set
	 */
	public void setFechaFinVigencia(String fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}
	
	
	
	
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaInicioVigenciaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaInicioVigencia) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaInicioVigencia) : "";
	}
	
	
	
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaFinVigenciaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaFinVigencia) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaFinVigencia) : "";
	}
	
	
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }

	
}
