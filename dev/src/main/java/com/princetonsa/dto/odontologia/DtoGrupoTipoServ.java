package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.InfoDatosInt;
import util.InfoDatosStr;

public class DtoGrupoTipoServ implements Serializable
{
	private InfoDatosInt  grupoServicio;
	private InfoDatosStr tipoServicio;
	
	
	/**
	 * 
	 */
	public DtoGrupoTipoServ() {
		super();
		this.grupoServicio = new InfoDatosInt();
		this.tipoServicio = new InfoDatosStr();
	}
	
	/**
	 * 
	 * @param grupoServicio
	 * @param tipoServicio
	 */
	public DtoGrupoTipoServ(InfoDatosInt grupoServicio,
			InfoDatosStr tipoServicio) {
		super();
		this.grupoServicio = grupoServicio;
		this.tipoServicio = tipoServicio;
	}
	/**
	 * @return the grupoServicio
	 */
	public InfoDatosInt getGrupoServicio() {
		return grupoServicio;
	}
	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(InfoDatosInt grupoServicio) {
		this.grupoServicio = grupoServicio;
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
	
	
}
