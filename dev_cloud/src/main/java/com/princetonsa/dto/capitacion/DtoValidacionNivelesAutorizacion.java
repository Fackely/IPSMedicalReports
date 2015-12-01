/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author armando
 *
 */
public class DtoValidacionNivelesAutorizacion 
{

	
	/**
	 * 
	 */
	private int codigoNivelAutorizacion;
	
	/**
	 * 
	 */
	private String descripcionNivelAutorizacion;
	
	/**
	 * 
	 */
	private int viaIngreso;
	
	/**
	 * 
	 */
	private String tipoAutorizacion;
	
	/**
	 * 
	 */
	private String usuarioAutorizacion;
	
	/**
	 * 
	 */
	private int ocupacionMedicaAutorizacion;
	
	/**
	 * 
	 */
	private int codigoNivelAutorizacionUsuario;
	
	/**
	 * 
	 */
	private int codigoNivelAutorizacionOcupacionMedica;
	
	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 */
	private ArrayList<Integer> prioridades;

	/**
	 * 
	 */
	public DtoValidacionNivelesAutorizacion()
	{
		this.codigoNivelAutorizacion=ConstantesBD.codigoNuncaValido;
		this.descripcionNivelAutorizacion="";
		this.viaIngreso=ConstantesBD.codigoNuncaValido;
		this.tipoAutorizacion="";
		this.usuarioAutorizacion="";
		this.ocupacionMedicaAutorizacion=ConstantesBD.codigoNuncaValido;
		this.codigoNivelAutorizacionUsuario=ConstantesBD.codigoNuncaValido;
		this.codigoNivelAutorizacionOcupacionMedica=ConstantesBD.codigoNuncaValido;
		this.activo=false;
		this.prioridades=new ArrayList<Integer>();
	}
	

	public int getCodigoNivelAutorizacion() {
		return codigoNivelAutorizacion;
	}

	public void setCodigoNivelAutorizacion(int codigoNivelAutorizacion) {
		this.codigoNivelAutorizacion = codigoNivelAutorizacion;
	}

	public String getDescripcionNivelAutorizacion() {
		return descripcionNivelAutorizacion;
	}

	public void setDescripcionNivelAutorizacion(String descripcionNivelAutorizacion) {
		this.descripcionNivelAutorizacion = descripcionNivelAutorizacion;
	}

	public int getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}

	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}

	public String getUsuarioAutorizacion() {
		return usuarioAutorizacion;
	}

	public void setUsuarioAutorizacion(String usuarioAutorizacion) {
		this.usuarioAutorizacion = usuarioAutorizacion;
	}

	public int getOcupacionMedicaAutorizacion() {
		return ocupacionMedicaAutorizacion;
	}

	public void setOcupacionMedicaAutorizacion(int ocupacionMedicaAutorizacion) {
		this.ocupacionMedicaAutorizacion = ocupacionMedicaAutorizacion;
	}

	public int getCodigoNivelAutorizacionUsuario() {
		return codigoNivelAutorizacionUsuario;
	}

	public void setCodigoNivelAutorizacionUsuario(int codigoNivelAutorizacionUsuario) {
		this.codigoNivelAutorizacionUsuario = codigoNivelAutorizacionUsuario;
	}

	public int getCodigoNivelAutorizacionOcupacionMedica() {
		return codigoNivelAutorizacionOcupacionMedica;
	}

	public void setCodigoNivelAutorizacionOcupacionMedica(
			int codigoNivelAutorizacionOcupacionMedica) {
		this.codigoNivelAutorizacionOcupacionMedica = codigoNivelAutorizacionOcupacionMedica;
	}


	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}


	public ArrayList<Integer> getPrioridades() {
		return prioridades;
	}


	public void setPrioridades(ArrayList<Integer> prioridades) {
		this.prioridades = prioridades;
	}
	
	
}
