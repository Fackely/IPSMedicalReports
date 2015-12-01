/*
 * Abr 29, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import com.princetonsa.mundo.atencion.Diagnostico;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * Data Transfer Object: Valoración Hospitalizacion (extensión del DtoValoracion)
 * @author Sebastián Gómez R.
 *
 */
public class DtoValoracionHospitalizacion extends DtoValoracion implements Serializable
{
	/**
	 * Campo que se llena cuando el origen de la admision fue diferente a urgencias
	 */
	private String textoOrigenNoUrgencias;
	private InfoDatosInt origenAdmision;
	private Diagnostico diagnosticoIngreso;
	private String especialidadProfResponde;
	private String nomEspecialidadProfResponde;
	
	/**
	 * Constructor de la valoración de hospitalización
	 *
	 */
	public DtoValoracionHospitalizacion()
	{
		this.clean();
	}
	
	/**
	 * Método que resetea los datos 
	 */
	public void clean()
	{
		super.clean();
		this.textoOrigenNoUrgencias = "";
		this.origenAdmision = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.diagnosticoIngreso = new Diagnostico();
		this.especialidadProfResponde="";
		this.nomEspecialidadProfResponde="";
	}

	/**
	 * @return the diagnosticoIngreso
	 */
	public Diagnostico getDiagnosticoIngreso() {
		return diagnosticoIngreso;
	}

	/**
	 * @param diagnosticoIngreso the diagnosticoIngreso to set
	 */
	public void setDiagnosticoIngreso(Diagnostico diagnosticoIngreso) {
		this.diagnosticoIngreso = diagnosticoIngreso;
	}

	/**
	 * @return the origenAdmision
	 */
	public int getCodigoOrigenAdmision() {
		return origenAdmision.getCodigo();
	}

	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setCodigoOrigenAdmision(int origenAdmision) {
		this.origenAdmision.setCodigo(origenAdmision);
	}
	
	/**
	 * @return the origenAdmision
	 */
	public String getNombreOrigenAdmision() {
		return origenAdmision.getNombre();
	}

	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setNombreOrigenAdmision(String origenAdmision) {
		this.origenAdmision.setNombre(origenAdmision);
	}

	/**
	 * @return the textoOrigenNoUrgencias
	 */
	public String getTextoOrigenNoUrgencias() {
		return textoOrigenNoUrgencias;
	}

	/**
	 * @param textoOrigenNoUrgencias the textoOrigenNoUrgencias to set
	 */
	public void setTextoOrigenNoUrgencias(String textoOrigenNoUrgencias) {
		this.textoOrigenNoUrgencias = textoOrigenNoUrgencias;
	}

	/**
	 * @return the origenAdmision
	 */
	public InfoDatosInt getOrigenAdmision() {
		return origenAdmision;
	}

	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setOrigenAdmision(InfoDatosInt origenAdmision) {
		this.origenAdmision = origenAdmision;
	}

	public String getEspecialidadProfResponde() {
		return especialidadProfResponde;
	}

	public void setEspecialidadProfResponde(String especialidadProfResponde) {
		this.especialidadProfResponde = especialidadProfResponde;
	}

	/**
	 * @return the nomEspecialidadProfResponde
	 */
	public String getNomEspecialidadProfResponde() {
		return nomEspecialidadProfResponde;
	}

	/**
	 * @param nomEspecialidadProfResponde the nomEspecialidadProfResponde to set
	 */
	public void setNomEspecialidadProfResponde(String nomEspecialidadProfResponde) {
		this.nomEspecialidadProfResponde = nomEspecialidadProfResponde;
	}

	
	
	
}
