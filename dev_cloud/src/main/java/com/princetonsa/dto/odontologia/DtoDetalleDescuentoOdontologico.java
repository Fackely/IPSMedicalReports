package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadFecha;

public class DtoDetalleDescuentoOdontologico implements Serializable , Cloneable {

	private double codigo;
	private double consecutivoDescuento;
	private double valorMinimoPresupuesto;
	private double valorMaximoPresupuesto;
	private double porcentajeDescuento;
	private double diasVigencia;

	
	private InfoDatosStr tipoUsuarioAutoriza;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	
	private boolean puedoEliminar;
	
	
	public DtoDetalleDescuentoOdontologico() {
		
		this.reset();
		
		
	}
	
	void reset(){

		this.codigo = ConstantesBD.codigoNuncaValido;
		this.consecutivoDescuento = ConstantesBD.codigoNuncaValido;
		this.valorMinimoPresupuesto= ConstantesBD.codigoNuncaValido;
		this.valorMaximoPresupuesto = ConstantesBD.codigoNuncaValido;
		this.porcentajeDescuento = ConstantesBD.codigoNuncaValido;
		this.diasVigencia= ConstantesBD.codigoNuncaValido;
		this.tipoUsuarioAutoriza = new InfoDatosStr();
		
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
		this.puedoEliminar= false;
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
	 * @return the consecutivoDescuento
	 */
	public double getConsecutivoDescuento() {
		return consecutivoDescuento;
	}

	/**
	 * @param consecutivoDescuento the consecutivoDescuento to set
	 */
	public void setConsecutivoDescuento(double consecutivoDescuento) {
		this.consecutivoDescuento = consecutivoDescuento;
	}

	/**
	 * @return the valorMinimoPresupuesto
	 */
	public double getValorMinimoPresupuesto() {
		return valorMinimoPresupuesto;
	}

	/**
	 * @param valorMinimoPresupuesto the valorMinimoPresupuesto to set
	 */
	public void setValorMinimoPresupuesto(double valorMinimoPresupuesto) {
		this.valorMinimoPresupuesto = valorMinimoPresupuesto;
	}

	/**
	 * @return the valorMaximoPresupuesto
	 */
	public double getValorMaximoPresupuesto() {
		return valorMaximoPresupuesto;
	}

	/**
	 * @param valorMaximoPresupuesto the valorMaximoPresupuesto to set
	 */
	public void setValorMaximoPresupuesto(double valorMaximoPresupuesto) {
		this.valorMaximoPresupuesto = valorMaximoPresupuesto;
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
	 * @return the diasVigencia
	 */
	public double getDiasVigencia() {
		return diasVigencia;
	}

	/**
	 * @param diasVigencia the diasVigencia to set
	 */
	public void setDiasVigencia(double diasVigencia) {
		this.diasVigencia = diasVigencia;
	}

	

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaBD() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica);
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
	 * @return the tipoUsuarioAutoriza
	 */
	public InfoDatosStr getTipoUsuarioAutoriza() {
		return tipoUsuarioAutoriza;
	}

	/**
	 * @param tipoUsuarioAutoriza the tipoUsuarioAutoriza to set
	 */
	public void setTipoUsuarioAutoriza(InfoDatosStr tipoUsuarioAutoriza) {
		this.tipoUsuarioAutoriza = tipoUsuarioAutoriza;
	}
	
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
		 DtoDetalleDescuentoOdontologico obj=null;
	        try{
	            obj= (DtoDetalleDescuentoOdontologico)super.clone();
	            obj.setTipoUsuarioAutoriza((InfoDatosStr)this.tipoUsuarioAutoriza.clone());
	        }catch(CloneNotSupportedException ex){
	            System.out.println(" no se puede duplicar");
	        }
	        return obj;
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
