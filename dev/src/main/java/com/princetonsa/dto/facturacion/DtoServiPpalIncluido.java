/*
 * Ene 06, 2010
 */
package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * 
 * DTO que representa la tabla servi_ppalincluidos
 * @author Sebastián Gómez R.
 *
 */
public class DtoServiPpalIncluido implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal codigo;
	private InfoDatosInt servicio;
	private boolean activo;
	private int institucion;
	private UsuarioBasico usuarioModifica;
	private String fechaModifica;
	private String horaModifica;
	
	private ArrayList<DtoServiIncluidoServiPpal> serviciosIncluidos;
	private ArrayList<DtoArtIncluidoServiPpal> articulosIncluidos;
	
	//Atributos adicionales************************************
	/**
	 * Atributo usado para saber si el servicio incluido ppal es de atencion odontologica
	 */
	private boolean atencionOdontologica;
	
	public void reset()
	{
		this.codigo = new BigDecimal(0);
		this.servicio = new InfoDatosInt(ConstantesBD.codigoNuncaValido);
		this.activo = false;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = new UsuarioBasico();
		this.fechaModifica = "";
		this.horaModifica = "";
		
		this.serviciosIncluidos = new ArrayList<DtoServiIncluidoServiPpal>();
		this.articulosIncluidos = new ArrayList<DtoArtIncluidoServiPpal>();
		
		this.atencionOdontologica = false;
		
	}
	
	/**
	 * Constructor
	 */
	public DtoServiPpalIncluido()
	{
		this.reset();
	}

	/**
	 * @return the codigo
	 */
	public BigDecimal getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
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
	public UsuarioBasico getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(UsuarioBasico usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
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
	 * @return the serviciosIncluidos
	 */
	public ArrayList<DtoServiIncluidoServiPpal> getServiciosIncluidos() {
		return serviciosIncluidos;
	}

	/**
	 * @param serviciosIncluidos the serviciosIncluidos to set
	 */
	public void setServiciosIncluidos(
			ArrayList<DtoServiIncluidoServiPpal> serviciosIncluidos) {
		this.serviciosIncluidos = serviciosIncluidos;
	}

	/**
	 * @return the articulosIncluidos
	 */
	public ArrayList<DtoArtIncluidoServiPpal> getArticulosIncluidos() {
		return articulosIncluidos;
	}

	/**
	 * @param articulosIncluidos the articulosIncluidos to set
	 */
	public void setArticulosIncluidos(
			ArrayList<DtoArtIncluidoServiPpal> articulosIncluidos) {
		this.articulosIncluidos = articulosIncluidos;
	}

	/**
	 * @return the atencionOdontologica
	 */
	public boolean isAtencionOdontologica() {
		return atencionOdontologica;
	}

	/**
	 * @param atencionOdontologica the atencionOdontologica to set
	 */
	public void setAtencionOdontologica(boolean atencionOdontologica) {
		this.atencionOdontologica = atencionOdontologica;
	}
	
	
}
