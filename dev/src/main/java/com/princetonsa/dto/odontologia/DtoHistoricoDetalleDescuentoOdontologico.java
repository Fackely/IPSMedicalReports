package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;

public class DtoHistoricoDetalleDescuentoOdontologico implements Serializable , Cloneable {

	private double codigo;
	private double consecutivoDescuento;
	private double valorMinimoPresupuesto;
	private double valorMinimoPresupuestoMod;
	private double valorMaximoPresupuesto;
	private double valorMaximoPresupuestoMod;
	private double porcentajeDescuento;
	private double porcentajeDescuentoMod;
	private double diasVigencia;
	private double diasVigenciaMod;

	private double detalle;
	private InfoDatosStr tipoUsuarioAutoriza;
	private InfoDatosStr tipoUsuarioAutorizaMod;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	private String eliminado;
	
	
	
	public DtoHistoricoDetalleDescuentoOdontologico() {
		
		this.reset();
		
		
	}
	
	void reset(){

		this.codigo = ConstantesBD.codigoNuncaValido;
		this.consecutivoDescuento = ConstantesBD.codigoNuncaValido;
		this.valorMinimoPresupuesto= ConstantesBD.codigoNuncaValido;
		this.valorMaximoPresupuestoMod = ConstantesBD.codigoNuncaValido;
		this.valorMaximoPresupuesto = ConstantesBD.codigoNuncaValido;
		this.valorMaximoPresupuestoMod = ConstantesBD.codigoNuncaValido;
		this.porcentajeDescuento = ConstantesBD.codigoNuncaValido;
		this.porcentajeDescuentoMod = ConstantesBD.codigoNuncaValido;
		this.diasVigencia= ConstantesBD.codigoNuncaValido;
		this.diasVigenciaMod = ConstantesBD.codigoNuncaValido;
		this.tipoUsuarioAutoriza = new InfoDatosStr();
		this.tipoUsuarioAutorizaMod = new InfoDatosStr();
		
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
	    this.eliminado = "";
	    this.detalle = ConstantesBD.codigoNuncaValido;
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
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }

	/**
	 * @return the valorMinimoPresupuestoMod
	 */
	public double getValorMinimoPresupuestoMod() {
		return valorMinimoPresupuestoMod;
	}

	/**
	 * @param valorMinimoPresupuestoMod the valorMinimoPresupuestoMod to set
	 */
	public void setValorMinimoPresupuestoMod(double valorMinimoPresupuestoMod) {
		this.valorMinimoPresupuestoMod = valorMinimoPresupuestoMod;
	}

	/**
	 * @return the valorMaximoPresupuestoMod
	 */
	public double getValorMaximoPresupuestoMod() {
		return valorMaximoPresupuestoMod;
	}

	/**
	 * @param valorMaximoPresupuestoMod the valorMaximoPresupuestoMod to set
	 */
	public void setValorMaximoPresupuestoMod(double valorMaximoPresupuestoMod) {
		this.valorMaximoPresupuestoMod = valorMaximoPresupuestoMod;
	}

	/**
	 * @return the porcentajeDescuentoMod
	 */
	public double getPorcentajeDescuentoMod() {
		return porcentajeDescuentoMod;
	}

	/**
	 * @param porcentajeDescuentoMod the porcentajeDescuentoMod to set
	 */
	public void setPorcentajeDescuentoMod(double porcentajeDescuentoMod) {
		this.porcentajeDescuentoMod = porcentajeDescuentoMod;
	}

	/**
	 * @return the diasVigenciaMod
	 */
	public double getDiasVigenciaMod() {
		return diasVigenciaMod;
	}

	/**
	 * @param diasVigenciaMod the diasVigenciaMod to set
	 */
	public void setDiasVigenciaMod(double diasVigenciaMod) {
		this.diasVigenciaMod = diasVigenciaMod;
	}

	
	/**
	 * @return the eliminado
	 */
	public String getEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}

	/**
	 * @return the tipoUsuarioAutorizaMod
	 */
	public InfoDatosStr getTipoUsuarioAutorizaMod() {
		return tipoUsuarioAutorizaMod;
	}

	/**
	 * @param tipoUsuarioAutorizaMod the tipoUsuarioAutorizaMod to set
	 */
	public void setTipoUsuarioAutorizaMod(InfoDatosStr tipoUsuarioAutorizaMod) {
		this.tipoUsuarioAutorizaMod = tipoUsuarioAutorizaMod;
	}

	/**
	 * @return the detalle
	 */
	public double getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(double detalle) {
		this.detalle = detalle;
	}


}
