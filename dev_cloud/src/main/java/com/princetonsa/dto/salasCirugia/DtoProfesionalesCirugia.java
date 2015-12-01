/*
 * Feb 15, 2008
 */
package com.princetonsa.dto.salasCirugia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatos;

/**
 * DATA TRANSFER OBJECT usado para representar los profesionales
 * de una cirugía
 * @author Sebastián Gómez R.
 *
 */
public class DtoProfesionalesCirugia implements Serializable
{
	private String codigo;
	
	/**
	 * Código del registro de la cirugia asociada
	 */
	private String codSolCxServ;
	
	/**
	 * Asocio del profesional
	 */
	private InfoDatos asocio;
	
	private InfoDatos profesional;
	
	private InfoDatos especialidad;
	
	private String cobrable;
	
	private InfoDatos pool;
	
	private String historiaClinica;
	
	/**
	 * Verifica si el registro ya existe en la base de datos
	 */
	private boolean existeBD;
	
	/**
	 * Verifica si el registro se debe eliminar
	 */
	private boolean eliminar;
	
	/**
	 * Indicativo de tipo de especialista
	 */
	private String tipoEspecialista;
	
	/**
	 * Nombre del profesional
	 * */
	private String nombre;
	/**
	 * Nombre de la especialidad del profesional
	 * */
	private String nombreEspecialidad2;
	
	
	/**
	 * Constructor
	 *
	 */
	public DtoProfesionalesCirugia()
	{
		this.codigo = "";
		this.codSolCxServ = "";
		this.asocio = new InfoDatos(ConstantesBD.codigoNuncaValido,"");
		this.profesional = new InfoDatos(ConstantesBD.codigoNuncaValido,"");
		this.especialidad = new InfoDatos(ConstantesBD.codigoNuncaValido,"");
		this.cobrable = "";
		this.pool = new InfoDatos(ConstantesBD.codigoNuncaValido,"");
		this.tipoEspecialista = "";
		this.existeBD = false;
		this.eliminar = false;
		this.historiaClinica = ConstantesBD.acronimoSi;
	}

	/**
	 * @return the asocio
	 */
	public InfoDatos getAsocio() {
		return asocio;
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setAsocio(InfoDatos asocio) {
		this.asocio = asocio;
	}
	
	/**
	 * @return the asocio
	 */
	public int getCodigoAsocio() {
		return asocio.getCodigo();
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setCodigoAsocio(int asocio) {
		this.asocio.setCodigo(asocio);
	}
	
	/**
	 * @return the asocio
	 */
	public String getNombreAsocio() {
		return asocio.getNombre();
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setNombreAsocio(String asocio) {
		this.asocio.setNombre(asocio);
	}

	/**
	 * @return the cobrable
	 */
	public String getCobrable() {
		return cobrable;
	}

	/**
	 * @param cobrable the cobrable to set
	 */
	public void setCobrable(String cobrable) {
		this.cobrable = cobrable;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codSolCxServ
	 */
	public String getCodSolCxServ() {
		return codSolCxServ;
	}

	/**
	 * @param codSolCxServ the codSolCxServ to set
	 */
	public void setCodSolCxServ(String codSolCxServ) {
		this.codSolCxServ = codSolCxServ;
	}

	/**
	 * @return the pool
	 */
	public InfoDatos getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(InfoDatos pool) {
		this.pool = pool;
	}
	
	/**
	 * @return the pool
	 */
	public int getCodigoPool() {
		return pool.getCodigo();
	}

	/**
	 * @param pool the pool to set
	 */
	public void setCodigoPool(int pool) {
		this.pool.setCodigo(pool);
	}
	
	/**
	 * @return the pool
	 */
	public String getNombrePool() {
		return pool.getNombre();
	}

	/**
	 * @param pool the pool to set
	 */
	public void setNombrePool(String pool) {
		this.pool.setNombre(pool);
	}

	/**
	 * @return the profesional
	 */
	public InfoDatos getProfesional() {
		return profesional;
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(InfoDatos profesional) {
		this.profesional = profesional;
	}
	
	/**
	 * @return the profesional
	 */
	public int getCodigoProfesional() {
		return profesional.getCodigo();
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setCodigoProfesional(int profesional) {
		this.profesional.setCodigo(profesional);
	}
	
	/**
	 * @return the profesional
	 */
	public String getNombreProfesional() {
		return profesional.getNombre();
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setNombreProfesional(String profesional) {
		this.profesional.setNombre(profesional);
	}

	/**
	 * @return the tipoEspecialista
	 */
	public String getTipoEspecialista() {
		return tipoEspecialista;
	}

	/**
	 * @param tipoEspecialista the tipoEspecialista to set
	 */
	public void setTipoEspecialista(String tipoEspecialista) {
		this.tipoEspecialista = tipoEspecialista;
	}

	/**
	 * @return the especialidad
	 */
	public InfoDatos getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(InfoDatos especialidad) {
		this.especialidad = especialidad;
	}
	
	/**
	 * @return the especialidad
	 */
	public int getCodigoEspecialidad() {
		return especialidad.getCodigo();
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setCodigoEspecialidad(int especialidad) {
		this.especialidad.setCodigo(especialidad);
	}
	
	/**
	 * @return the especialidad
	 */
	public String getNombreEspecialidad() {
		return especialidad.getNombre();
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setNombreEspecialidad(String especialidad) {
		this.especialidad.setNombre(especialidad);
	}

	/**
	 * @return the eliminar
	 */
	public boolean isEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(boolean eliminar) {
		this.eliminar = eliminar;
	}

	/**
	 * @return the existeBD
	 */
	public boolean isExisteBD() {
		return existeBD;
	}

	/**
	 * @param existeBD the existeBD to set
	 */
	public void setExisteBD(boolean existeBD) {
		this.existeBD = existeBD;
	}

	/**
	 * @return the historiaClinica
	 */
	public String getHistoriaClinica() {
		return historiaClinica;
	}

	/**
	 * @param historiaClinica the historiaClinica to set
	 */
	public void setHistoriaClinica(String historiaClinica) {
		this.historiaClinica = historiaClinica;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreEspecialidad2() {
		return nombreEspecialidad2;
	}

	public void setNombreEspecialidad2(String nombreEspecialidad2) {
		this.nombreEspecialidad2 = nombreEspecialidad2;
	}
}
