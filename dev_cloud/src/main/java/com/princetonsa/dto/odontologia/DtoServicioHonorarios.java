package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
/**
 * 
 * @author axioma
 *
 */
public class DtoServicioHonorarios implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double codigo;
	private InfoDatosInt esquemaTarifario;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String usuarioModifica;
	private InfoDatosInt centroAtencion;
	
	
	public DtoServicioHonorarios() 
	{
		this.reset();
	}

	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return this.codigo;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public InfoDatosInt getEsquemaTarifario() {
		return this.esquemaTarifario;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return this.fechaModifica;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return this.horaModifica;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return this.institucion;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	/**
	 * 
	 */
	public void reset() {
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.esquemaTarifario = new InfoDatosInt();
		this.fechaModifica = "";
		this.horaModifica = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = "";
		this.centroAtencion= new InfoDatosInt();
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	/**
	 * @param esquemaTarifario
	 *            the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(InfoDatosInt esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * @param fechaModifica
	 *            the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @param horaModifica
	 *            the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @param institucion
	 *            the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @param usuarioModifica
	 *            the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        DtoServicioHonorarios obj=null;
	        try{
	            obj= (DtoServicioHonorarios)super.clone();
	            obj.setEsquemaTarifario((InfoDatosInt) this.esquemaTarifario.clone());
	        }catch(CloneNotSupportedException ex){
	            System.out.println(" no se puede duplicar");
	        }
	        return obj;
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

}
