package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.List;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class ProfesionalHQxDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6804347279723864863L;

	private int idMedico;
	private String nombreCompleto;
	
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	
	private boolean esOtroProfesionalEspecialidad;
	private boolean esProfesionalEspecialidad;
	private TipoProfesionalDto tipoProfesional;
	private EspecialidadDto especialidad;
	private List<EspecialidadDto> especialidades;

	private boolean cobrable;
	private int idProfesionalXServicio;
	private int idProfesionalXEspecialidad;
	private int idProfesionalInfoQx;
	
	public ProfesionalHQxDto() {
	}
	
	public ProfesionalHQxDto(int idMedico, String nombreCompleto) {
		this.idMedico = idMedico;
		this.nombreCompleto = nombreCompleto;
	}

	public ProfesionalHQxDto(int idMedico, String nombreCompleto,
			String primerNombre, String segundoNombre, String primerApellido,
			String segundoApellido, boolean esOtroProfesionalEspecialidad,
			boolean esProfesionalEspecialidad,
			TipoProfesionalDto tipoProfesional, EspecialidadDto especialidad) {
		this.idMedico = idMedico;
		this.nombreCompleto = nombreCompleto;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.esOtroProfesionalEspecialidad = esOtroProfesionalEspecialidad;
		this.esProfesionalEspecialidad = esProfesionalEspecialidad;
		this.tipoProfesional = tipoProfesional;
		this.especialidad = especialidad;
	}
	/**
	 * @return the idProfesionalXEspecialidad
	 */
	public int getIdProfesionalXEspecialidad() {
		return idProfesionalXEspecialidad;
	}
	/**
	 * @param idProfesionalXEspecialidad the idProfesionalXEspecialidad to set
	 */
	public void setIdProfesionalXEspecialidad(int idProfesionalXEspecialidad) {
		this.idProfesionalXEspecialidad = idProfesionalXEspecialidad;
	}
	/**
	 * @return the idProfesionalInfoQx
	 */
	public int getIdProfesionalInfoQx() {
		return idProfesionalInfoQx;
	}
	/**
	 * @param idProfesionalInfoQx the idProfesionalInfoQx to set
	 */
	public void setIdProfesionalInfoQx(int idProfesionalInfoQx) {
		this.idProfesionalInfoQx = idProfesionalInfoQx;
	}
	/**
	 * @return the idProfesionalXServicio
	 */
	public int getIdProfesionalXServicio() {
		return idProfesionalXServicio;
	}
	/**
	 * @param idProfesionalXServicio the idProfesionalXServicio to set
	 */
	public void setIdProfesionalXServicio(int idProfesionalXServicio) {
		this.idProfesionalXServicio = idProfesionalXServicio;
	}
	/**
	 * @return the cobrable
	 */
	public boolean isCobrable() {
		return cobrable;
	}
	/**
	 * @param cobrable the cobrable to set
	 */
	public void setCobrable(boolean cobrable) {
		this.cobrable = cobrable;
	}
	public boolean isEsOtroProfesionalEspecialidad() {
		return esOtroProfesionalEspecialidad;
	}
	public void setEsOtroProfesionalEspecialidad(
			boolean esOtroProfesionalEspecialidad) {
		this.esOtroProfesionalEspecialidad = esOtroProfesionalEspecialidad;
	}
	public boolean isEsProfesionalEspecialidad() {
		return esProfesionalEspecialidad;
	}
	public void setEsProfesionalEspecialidad(boolean esProfesionalEspecialidad) {
		this.esProfesionalEspecialidad = esProfesionalEspecialidad;
	}
	public TipoProfesionalDto getTipoProfesional() {
		return tipoProfesional;
	}
	public void setTipoProfesional(TipoProfesionalDto tipoProfesional) {
		this.tipoProfesional = tipoProfesional;
	}
	public EspecialidadDto getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(EspecialidadDto especialidad) {
		this.especialidad = especialidad;
	}
	public int getIdMedico() {
		return idMedico;
	}
	public void setIdMedico(int idMedico) {
		this.idMedico = idMedico;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public List<EspecialidadDto> getEspecialidades() {
		return especialidades;
	}
	public void setEspecialidades(List<EspecialidadDto> especialidades) {
		this.especialidades = especialidades;
	}

	
	
}
