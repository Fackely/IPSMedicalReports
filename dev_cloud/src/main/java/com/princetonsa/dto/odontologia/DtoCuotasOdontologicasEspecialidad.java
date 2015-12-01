package com.princetonsa.dto.odontologia;


import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class DtoCuotasOdontologicasEspecialidad implements Serializable

{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int codigoPk;
	private String loginUsuario;
	private int codigoEspecialidad ;
	private String nombreEspecialidad;
	private int codigoInstitucion;
	private String nombreInstitucion;
	private String tipoValor;
	private String fechaModifica;
	private String horaModifica;
	
	
	
	private ArrayList<DtoDetalleCuotasOdontoEspClon> listaDetallesEspecialidad;
	
	
	
	
	
	
	/**
	 * CONSTRUTOR
	 */
	public DtoCuotasOdontologicasEspecialidad(){
		

		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.loginUsuario="";
		this.codigoEspecialidad=ConstantesBD.codigoNuncaValido;
		this.nombreEspecialidad="";
		this.codigoInstitucion=ConstantesBD.codigoNuncaValido;
		this.nombreInstitucion="";
		this.tipoValor="";
		this.fechaModifica="";
		this.horaModifica="";
		this.setListaDetallesEspecialidad(new ArrayList<DtoDetalleCuotasOdontoEspClon>());
		
	}



	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}



	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}



	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}



	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}



	/**
	 * @return the codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}



	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}



	/**
	 * @return the nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}



	/**
	 * @param nombreEspecialidad the nombreEspecialidad to set
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}



	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}



	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}



	/**
	 * @return the nombreInstitucion
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}



	/**
	 * @param nombreInstitucion the nombreInstitucion to set
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}



	/**
	 * @return the tipoValor
	 */
	public String getTipoValor() {
		return tipoValor;
	}



	/**
	 * @param tipoValor the tipoValor to set
	 */
	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
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



	public void setListaDetallesEspecialidad(
			ArrayList<DtoDetalleCuotasOdontoEspClon> listaDetallesEspecialidad) {
		this.listaDetallesEspecialidad = listaDetallesEspecialidad;
	}



	public ArrayList<DtoDetalleCuotasOdontoEspClon> getListaDetallesEspecialidad() {
		return listaDetallesEspecialidad;
	}



	
	
	
	
	

}
