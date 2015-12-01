package com.princetonsa.actionform.consultaExterna;
 
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;

/**
 * @author Jose Eduardo Arias Doncel
 */
public class ReporteEstadisticoConsultaExForm extends ValidatorForm
{
	//*****************************************
	//Atributos Generales 
	
	/**
	 * Estado del Action 
	 * */
	private String estado;
	
	/**
	 * Tipo de Reporte
	 * */
	private int constanteReporte;
	
	/**
	 * Mapa de Busqueda
	 * */
	private HashMap mapaBusqueda;
	
	//********************************************
	
	/**
	 * Mapa de listado de Especialidades
	 * */
	private HashMap especialidadesMap;
	
	/**
	 * Mapa de especialiades insertadas
	 * */
	private HashMap estructuraEspecialidadInfo;
	
	/**
	 * String nombre nueva especialidad
	 * */
	private String nombreElementoAdd;
	
	/**
	 * String codigo nuevo especialidad
	 * */
	private String codigoElementoAdd;
	
	//*******************************************
	
	/**
	 * Mapa de listado de Motivo Cancelacion
	 * */
	private HashMap motivoCancelacionMap;
	
	/**
	 * Mapa de Motivos Cancelacion insertadas
	 * */
	private HashMap estructuraMotivosCancelacionInfo;
	
	//*******************************************

	/**
	 * Mapa de listado de Profesionales de la Salud
	 * */
	private ArrayList<HashMap<String,Object>> profesionalMap;
	
	/**
	 * Mapa de Motivos Cancelacion insertadas
	 * */
	private HashMap estructuraProfesionalInfo;
	
	//*******************************************
	
	public void resetAtributosGenerales()
	{
		constanteReporte = ConstantesBD.codigoNuncaValido;
		mapaBusqueda = new HashMap();
		especialidadesMap = new HashMap();
		motivoCancelacionMap = new HashMap();
		estructuraEspecialidadInfo = new HashMap();
		estructuraEspecialidadInfo.put("numRegistros","0");
		estructuraMotivosCancelacionInfo = new HashMap();
		estructuraMotivosCancelacionInfo.put("numRegistros","0");
		estructuraProfesionalInfo = new HashMap();
		estructuraProfesionalInfo.put("numRegistros","0");
	}
	
	public HashMap getEspecialidadesMap() {
		return especialidadesMap;
	}


	public void setEspecialidadesMap(HashMap especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}
	
	public HashMap getMapaBusqueda() {
		return mapaBusqueda;
	}


	public void setMapaBusqueda(HashMap mapaBusqueda) {
		this.mapaBusqueda = mapaBusqueda;
	}
	
	public Object getMapaBusqueda(String key) {
		return mapaBusqueda.get(key);
	}


	public void setMapaBusqueda(String key, Object value) {
		this.mapaBusqueda .put(key, value);
	}
	
	
	public int getConstanteReporte() {
		return constanteReporte;
	}

	public void setConstanteReporte(int constanteReporte) {
		this.constanteReporte = constanteReporte;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public HashMap getEstructuraEspecialidadInfo() {
		return estructuraEspecialidadInfo;
	}

	public void setEstructuraEspecialidadInfo(HashMap estructuraEspecialidadInfo) {
		this.estructuraEspecialidadInfo = estructuraEspecialidadInfo;
	}
	
	public Object getEstructuraEspecialidadInfo(String key) {
		return estructuraEspecialidadInfo.get(key);
	}

	public void setEstructuraEspecialidadInfo(String key, Object value) {		
		this.estructuraEspecialidadInfo.put(key, value);
	}
	
	public Object getEstructuraMotivosCancelacionInfo(String key) {
		return estructuraMotivosCancelacionInfo.get(key);
	}

	public void setEstructuraMotivosCancelacionInfo(String key, Object value) {		
		this.estructuraMotivosCancelacionInfo.put(key, value);
	}

	public HashMap getMotivoCancelacionMap() {
		return motivoCancelacionMap;
	}

	public void setMotivoCancelacionMap(HashMap motivoCancelacionMap) {
		this.motivoCancelacionMap = motivoCancelacionMap;
	}

	public HashMap getEstructuraMotivosCancelacionInfo() {
		return estructuraMotivosCancelacionInfo;
	}

	public void setEstructuraMotivosCancelacionInfo(
			HashMap estructuraMotivosCancelacionInfo) {
		this.estructuraMotivosCancelacionInfo = estructuraMotivosCancelacionInfo;
	}

	public String getNombreElementoAdd() {
		return nombreElementoAdd;
	}

	public void setNombreElementoAdd(String nombreElementoAdd) {
		this.nombreElementoAdd = nombreElementoAdd;
	}

	public String getCodigoElementoAdd() {
		return codigoElementoAdd;
	}

	public void setCodigoElementoAdd(String codigoElementoAdd) {
		this.codigoElementoAdd = codigoElementoAdd;
	}

	/**
	 * @return the estructuraProfesionalInfo
	 */
	public HashMap getEstructuraProfesionalInfo() {
		return estructuraProfesionalInfo;
	}

	/**
	 * @param estructuraProfesionalInfo the estructuraProfesionalInfo to set
	 */
	public void setEstructuraProfesionalInfo(HashMap estructuraProfesionalInfo) {
		this.estructuraProfesionalInfo = estructuraProfesionalInfo;
	}

	/**
	 * @param profesionalMap the profesionalMap to set
	 */
	public void setProfesionalMap(ArrayList<HashMap<String, Object>> profesionalMap) {
		this.profesionalMap = profesionalMap;
	}

	/**
	 * @return the profesionalMap
	 */
	public ArrayList<HashMap<String, Object>> getProfesionalMap() {
		return profesionalMap;
	}
}