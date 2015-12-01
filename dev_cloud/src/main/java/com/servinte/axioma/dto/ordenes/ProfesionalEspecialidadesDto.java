package com.servinte.axioma.dto.ordenes;

import java.io.Serializable;
import java.util.ArrayList;

import com.servinte.axioma.dto.administracion.EspecialidadDto;

/**
 * @author hermorhu
 * Dto con los datos del profesional y las especialidades 
 */
public class ProfesionalEspecialidadesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int codigoProfesional;
	private String nombreProfesional;
	private ArrayList<EspecialidadDto> especialidades;
	
	/**
	 * @return the codigoProfesional
	 */
	public int getCodigoProfesional() {
		return codigoProfesional;
	}
	
	/**
	 * @param codigoProfesional the codigoProfesional to set
	 */
	public void setCodigoProfesional(int codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}
	
	/**
	 * @return the especialidades
	 */
	public ArrayList<EspecialidadDto> getEspecialidades() {
		return especialidades;
	}
	
	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList<EspecialidadDto> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * @return the nombreProfesional
	 */
	public String getNombreProfesional() {
		return nombreProfesional;
	}

	/**
	 * @param nombreProfesional the nombreProfesional to set
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}
	
}
