package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author davgommo
 * @version 1.0
 * @created 20-jun-2012 02:24:02 p.m.
 */

public class PacienteConsumidorPresupuestoDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3120534142136391571L;
	
	
	private String nombrePaciente;
	private String nombreTipoIdentificacion;
	private String numeroIdentificacion;
	private int codigoPaciente;
	private JRDataSource listaValoresGrupoServicio;
	private JRDataSource listaValoresClaseInventario;
	private long cantidadAutorizada =0;;
	private long cantidadIngresos=0;;
		
	public PacienteConsumidorPresupuestoDto(){
		
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
	 * @return the cantidadIngresos
	 */
	public long getCantidadIngresos() {
		return cantidadIngresos;
	}

	/**
	 * @param cantidadIngresos the cantidadIngresos to set
	 */
	public void setCantidadIngresos(long cantidadIngresos) {
		this.cantidadIngresos = cantidadIngresos;
	}

	/**
	 * @return the listaValoresGrupoServicio
	 */
	public JRDataSource getListaValoresGrupoServicio() {
		return listaValoresGrupoServicio;
	}

	/**
	 * @param listaValoresGrupoServicio the listaValoresGrupoServicio to set
	 */
	public void setListaValoresGrupoServicio(JRDataSource listaValoresGrupoServicio) {
		this.listaValoresGrupoServicio = listaValoresGrupoServicio;
	}

	/**
	 * @return the listaValoresClaseInventario
	 */
	public JRDataSource getListaValoresClaseInventario() {
		return listaValoresClaseInventario;
	}

	/**
	 * @param listaValoresClaseInventario the listaValoresClaseInventario to set
	 */
	public void setListaValoresClaseInventario(
			JRDataSource listaValoresClaseInventario) {
		this.listaValoresClaseInventario = listaValoresClaseInventario;
	}

	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}



	
}