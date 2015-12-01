package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.interfaz.DtoCuentaContable;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class DtoDetalleAgrupacionHonorarios implements Serializable, Cloneable {
	
	private double codigo;
	private double codigoHonorario; 
	private InfoDatosInt  especialidad;
	private String fechaModificada;
	private InfoDatosInt  grupoServicio;
	private String horaModificada;
	private double porcentajeParticipacion;
	private InfoDatosStr tipoServicio;
	private String usuarioModifica;
	private double valorParticipacion;
	
	private ArrayList<DtoCuentaContable> arrayCuentasContables = new  ArrayList<DtoCuentaContable>(); 
     
	public DtoDetalleAgrupacionHonorarios() {
     this.reset();
	}

	
	public void reset() {
     this.codigo = ConstantesBD.codigoNuncaValidoDouble;
     this.codigoHonorario = ConstantesBD.codigoNuncaValidoDouble;
     this.especialidad=  new InfoDatosInt();
     this.fechaModificada = "";
     this.grupoServicio = new InfoDatosInt();
     this.horaModificada = "";
     this.porcentajeParticipacion = ConstantesBD.codigoNuncaValido;
     this.tipoServicio = new InfoDatosStr();
     this.valorParticipacion = ConstantesBD.codigoNuncaValido;
     this.usuarioModifica = "";
     this.arrayCuentasContables = new ArrayList<DtoCuentaContable>();
     this.arrayCuentasContables.add(new DtoCuentaContable());
     this.arrayCuentasContables.add(new DtoCuentaContable());
     this.arrayCuentasContables.add(new DtoCuentaContable());
	}
	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return this.codigo;
	}

	/**
	 * @return the codigoHonorario
	 */
	public double getCodigoHonorario() {
		return this.codigoHonorario;
	}

	

	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}

	public InfoDatosInt getGrupoServicio() {
		return grupoServicio;
	}

	public void setGrupoServicio(InfoDatosInt grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	/**
	 * @return the fechaModificada
	 */
	public String getFechaModificada() {
		return this.fechaModificada;
	}
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModificada) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModificada) : "";
	}

	

	/**
	 * @return the horaModificada
	 */
	public String getHoraModificada() {
		return this.horaModificada;
	}

	/**
	 * @return the porcentajeParticipacion
	 */
	public double getPorcentajeParticipacion() {
		return this.porcentajeParticipacion;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	/**
	 * @return the valorParticipacion
	 */
	public double getValorParticipacion() {
		return this.valorParticipacion;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	/**
	 * @param codigoHonorario
	 *            the codigoHonorario to set
	 */
	public void setCodigoHonorario(double codigoHonorario) {
		this.codigoHonorario = codigoHonorario;
	}

	

	/**
	 * @param fechaModificada
	 *            the fechaModificada to set
	 */
	public void setFechaModificada(String fechaModificada) {
		this.fechaModificada = fechaModificada;
	}

	

	/**
	 * @param horaModificada
	 *            the horaModificada to set
	 */
	public void setHoraModificada(String horaModificada) {
		this.horaModificada = horaModificada;
	}

	/**
	 * @param porcentajeParticipacion
	 *            the porcentajeParticipacion to set
	 */
	public void setPorcentajeParticipacion(double porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	/**
	 * @param usuarioModifica
	 *            the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @param valorParticipacion
	 *            the valorParticipacion to set
	 */
	public void setValorParticipacion(double valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
	}
	
	/**
	 * @return the tipoServicio
	 */
	public InfoDatosStr getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(InfoDatosStr tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the arrayCuentasContables
	 */
	public ArrayList<DtoCuentaContable> getArrayCuentasContables() {
		return arrayCuentasContables;
	}


	/**
	 * @param arrayCuentasContables the arrayCuentasContables to set
	 */
	public void setArrayCuentasContables(
			ArrayList<DtoCuentaContable> arrayCuentasContables) {
		this.arrayCuentasContables = arrayCuentasContables;
	}
	
	/**
	 * 
	 * 
	 */
	public String getValorParticipacionFormateado() {
		return UtilidadTexto.formatearValores(valorParticipacion);
	}
	
	/***
	 * 
	 *  
	 */
	
	public DtoDetalleAgrupacionHonorarios clone(){
		DtoDetalleAgrupacionHonorarios obj=null;
        try{
            obj= (DtoDetalleAgrupacionHonorarios)super.clone();	            
           
            obj.setArrayCuentasContables((ArrayList<DtoCuentaContable>)this.arrayCuentasContables.clone());
            obj.setTipoServicio((InfoDatosStr)this.tipoServicio.clone());
            obj.setGrupoServicio((InfoDatosInt)this.grupoServicio.clone());
            obj.setEspecialidad((InfoDatosInt)this.especialidad.clone());
            
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
	


}

	
	/**
	 * 
	 * 
	 * 
	 */
	
