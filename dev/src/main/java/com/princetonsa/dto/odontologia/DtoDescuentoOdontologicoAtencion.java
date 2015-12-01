package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadFecha;

public class  DtoDescuentoOdontologicoAtencion implements Serializable , Cloneable {

	private double consecutivo;	
	private InfoDatosInt centroAtencion;
	private int diasVigencia;
	private InfoDatosDouble nivelAutorizacion;
	private double porcentajeDescuento;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	private boolean puedoEliminar;
	
	public DtoDescuentoOdontologicoAtencion(){
	  this.reset();	
	}
	
	public void reset(){
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = new InfoDatosInt();
		this.nivelAutorizacion =new InfoDatosDouble();
		this.diasVigencia = ConstantesBD.codigoNuncaValido;
		this.porcentajeDescuento = ConstantesBD.codigoNuncaValido;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
		this.puedoEliminar= false;
	}
	
	public void reset2()
	{
		this.nivelAutorizacion = new InfoDatosDouble();
		this.diasVigencia = ConstantesBD.codigoNuncaValido;
		this.porcentajeDescuento = ConstantesBD.codigoNuncaValido;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	/***
	 * 
	 *  
	 */
	/***
	 * 
	 *  
	 */
	public DtoDescuentoOdontologicoAtencion clone(){
	        DtoDescuentoOdontologicoAtencion obj=null;
	        try{
	            obj= (DtoDescuentoOdontologicoAtencion)super.clone();	            
	           
	            obj.setNivelAutorizacion((InfoDatosDouble) this.nivelAutorizacion.clone());
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }

	/**
	 * @return the diasVigencia
	 */
	public int getDiasVigencia() {
		return diasVigencia;
	}

	/**
	 * @param diasVigencia the diasVigencia to set
	 */
	public void setDiasVigencia(int diasVigencia) {
		this.diasVigencia = diasVigencia;
	}

	

	/**
	 * @return the porcentajeDescuento
	 */
	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	/**
	 * @param porcentajeDescuento the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	/**
	 * @return the nivelAutorizacion
	 */
	public InfoDatosDouble getNivelAutorizacion() {
		return nivelAutorizacion;
	}

	/**
	 * @param nivelAutorizacion the nivelAutorizacion to set
	 */
	public void setNivelAutorizacion(InfoDatosDouble nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}

	/**
	 * @return the puedoEliminar
	 */
	public boolean isPuedoEliminar() {
		return puedoEliminar;
	}

	/**
	 * @param puedoEliminar the puedoEliminar to set
	 */
	public void setPuedoEliminar(boolean puedoEliminar) {
		this.puedoEliminar = puedoEliminar;
	}

	
}
