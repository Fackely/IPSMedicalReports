package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;


@SuppressWarnings("serial")
public class DtoDetalleEmisionesTarjetaCliente implements Serializable, Cloneable {

	private double codigo;
	private double codigoEmisiontargeta;
	private String fechaModifica;
	private String horaModifica;
	private InfoDatosInt centroAtencion;
	private BigDecimal serialInicial;
	private BigDecimal serialFinal;
	private String usuarioModifica;
	private String usuarioResponsable;
	private int institucion;
	
	/**
	 * 
	 */
	public DtoDetalleEmisionesTarjetaCliente(){
		
      this.reset();
	}
	
	/**
	 * 
	 * 
	 */
	
	public void reset(){
		
		 this.codigo = ConstantesBD.codigoNuncaValido;
			this.codigoEmisiontargeta = ConstantesBD.codigoNuncaValido;
			this.centroAtencion= new InfoDatosInt();
			
			this.fechaModifica = "";
			this.horaModifica = "";		
			this.usuarioModifica = "";
			this.usuarioResponsable = "";
			this.setSerialInicial(BigDecimal.ZERO);
			this.setSerialFinal(BigDecimal.ZERO);
			this.institucion = ConstantesBD.codigoNuncaValido;
	}
	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the codigoEmisiontargeta
	 */
	public double getCodigoEmisiontargeta() {
		return codigoEmisiontargeta;
	}
	/**
	 * @param codigoEmisiontargeta the codigoEmisiontargeta to set
	 */
	public void setCodigoEmisiontargeta(double codigoEmisiontargeta) {
		this.codigoEmisiontargeta = codigoEmisiontargeta;
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
	 * @return the usuarioResponsable
	 */
	public String getUsuarioResponsable() {
		return usuarioResponsable;
	}
	/**
	 * @param usuarioResponsable the usuarioResponsable to set
	 */
	public void setUsuarioResponsable(String usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
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
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
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

	public void setSerialInicial(BigDecimal serialInicial) {
		this.serialInicial = serialInicial;
	}

	public BigDecimal getSerialInicial() {
		return serialInicial;
	}

	public void setSerialFinal(BigDecimal serialFinal) {
		this.serialFinal = serialFinal;
	}

	public BigDecimal getSerialFinal() {
		return serialFinal;
	}

}
