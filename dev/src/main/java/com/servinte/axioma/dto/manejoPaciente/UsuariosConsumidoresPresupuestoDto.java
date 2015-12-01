package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.util.List;

/**
 * @author davgommo
 * @version 1.0
 * @created 20-jun-2012 02:24:02 p.m.
 */

public class UsuariosConsumidoresPresupuestoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1390570435372867730L;
	
	private String primerApellidoPersona;
	private String segundoApellidoPersona;
	private String primerNombrePersona;
	private String segundoNombrePersona;
	private String nombreTipoIdentificacion;
	private String numeroIdentificacion;
	private int codigoPaciente;
	private List<ValorGruposServicioPacienteDto> listaValoresGrupoServicio;
	private List<ValorClaseInventariosPacienteDto> listaValoresClaseInventario;
	private long cantidadAutorizada;
	private long cantidadIngreso;
		
	public UsuariosConsumidoresPresupuestoDto(){
		
	}

	/**
	 * @return the primerApellidoPersona
	 */
	public String getPrimerApellidoPersona() {
		return primerApellidoPersona;
	}
	 /**
	 * @param primerApellidoPersona the primerApellidoPersona to set
	 */
	public void setPrimerApellidoPersona(String primerApellidoPersona) {
		this.primerApellidoPersona = primerApellidoPersona;
	}
	/**
	 * @return the segundoApellidoPersona
	 */
	public String getSegundoApellidoPersona() {
		return segundoApellidoPersona;
	}
	 /**
	 * @param segundoApellidoPersona the segundoApellidoPersona to set
	 */
	public void setSegundoApellidoPersona(String segundoApellidoPersona) {
		this.segundoApellidoPersona = segundoApellidoPersona;
	}
	/**
	 * @return the primerNombrePersona
	 */
	public String getPrimerNombrePersona() {
		return primerNombrePersona;
	}
	 /**
	 * @param primerNombrePersona the primerNombrePersona to set
	 */
	public void setPrimerNombrePersona(String primerNombrePersona) {
		this.primerNombrePersona = primerNombrePersona;
	}
	/**
	 * @return the segundoNombrePersona
	 */
	public String getSegundoNombrePersona() {
		return segundoNombrePersona;
	}
	 /**
	 * @param segundoNombrePersona the segundoNombrePersona to set
	 */
	public void setSegundoNombrePersona(String segundoNombrePersona) {
		this.segundoNombrePersona = segundoNombrePersona;
	}
	/**
	 * @return the nombreTipoIdentificacion
	 */
	public String getNombreTipoIdentificacion() {
		return nombreTipoIdentificacion;
	}
	 /**
	 * @param nombreTipoIdentificacion the nombreTipoIdentificacion to set
	 */
	public void setNombreTipoIdentificacion(String nombreTipoIdentificacion) {
		this.nombreTipoIdentificacion = nombreTipoIdentificacion;
	}
	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	 /**
		 * @param numeroIdentificacion the numeroIdentificacion to set
		 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	/**
	 * @return the listaValoresGrupoServicio
	 */
	public List<ValorGruposServicioPacienteDto> getListaValoresGrupoServicio() {
		return listaValoresGrupoServicio;
	}
	 /**
	 * @param listaValoresGrupoServicio the listaValoresGrupoServicio to set
	 */
	public void setListaValoresGrupoServicio(
			List<ValorGruposServicioPacienteDto> listaValoresGrupoServicio) {
		this.listaValoresGrupoServicio = listaValoresGrupoServicio;
	}
	/**
	 * @return the listaValoresClaseInventario
	 */
	public List<ValorClaseInventariosPacienteDto> getListaValoresClaseInventario() {
		return listaValoresClaseInventario;
	}
	 /**
	 * @param listaValoresClaseInventario the listaValoresClaseInventario to set
	 */
	public void setListaValoresClaseInventario(
			List<ValorClaseInventariosPacienteDto> listaValoresClaseInventario) {
		this.listaValoresClaseInventario = listaValoresClaseInventario;
	}
	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	 /**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the cantidadAutorizada
	 */
	public long getCantidadAutorizada() {
		return cantidadAutorizada;
	}

	/**
	 * @param cantidadAutorizada the cantidadAutorizada to set
	 */
	public void setCantidadAutorizada(long cantidadAutorizada) {
		this.cantidadAutorizada = cantidadAutorizada;
	}

	/**
	 * @return the cantidadIngreso
	 */
	public long getCantidadIngreso() {
		return cantidadIngreso;
	}

	/**
	 * @param cantidadIngreso the cantidadIngreso to set
	 */
	public void setCantidadIngreso(long cantidadIngreso) {
		this.cantidadIngreso = cantidadIngreso;
	}



	
}