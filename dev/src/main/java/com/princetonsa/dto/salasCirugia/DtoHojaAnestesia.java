package com.princetonsa.dto.salasCirugia;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import util.InfoDatosString;

/**
 * @author Jose Eduardo Arias Doncel
 * */
public class DtoHojaAnestesia implements Serializable
{
	
	/**
	 * Indicador cargo Dto
	 * */
	private String indicadorCargoDto;
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Atributos para Información general de la Hoja de Anestesia*****************************	
	/*************************************************************************************************/
	
	/**
	 * HashMao infoHojaAnestesiaMap
	 * */
	private HashMap infoHojaAnestesiaMap;
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Atributos para la SubSeccion Especialidades y Cirujanos Principales*****************************
	//Seccion Informacion General
	/*************************************************************************************************/
	
	/**
	 * Indicador existe información especialidadesMap
	 * */
	private String indicadorInfoEspInterCirPrin;
	
	/**
	 * HasMap especialidadesMap
	 * */
	private HashMap especialidadesMap;
	
	/**
	 * HashMap cirujanosMap
	 * */
	private HashMap cirujanosMap;	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Atributos para la SubSeccion Anestesiologos*****************************************************
	//Seccion Informacion General
	/*************************************************************************************************/
	
	/**
	 * Indicador existe información Anestesiologos
	 * */
	private String indicadorInfoAnestesiologos;
	
	/**
	 * HashMap anestesiologos
	 * */
	private HashMap anestesiologosMap;
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Atributos para la SubSeccion Fecha y Hora de Ingreso Sala***************************************
	//Seccion Informacion General
	/*************************************************************************************************/
	
	/**
	 * HashMap datos solicitudCX
	 * */
	private HashMap solicitudCxMap;	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Seccion Anestesicos y Medicamentos Administrados
	/*************************************************************************************************/	
	/**
	 * Indicador existe informació Anestesicos y Medicamentos Administrados
	 * */
	private String indicadorInfoAnestesicosMedAdminis;
	
	/**
	 * HashMap datos medicamentos administrados
	 * */
	private HashMap anestesicosMedAdmMap1;		
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Seccion Liquidos
	/*************************************************************************************************/
		
	/**
	 * Indicador existe información de Liquidos
	 * */
	private String indicadorInfoliquidos;
	
	/**
	 * HashMap datos liquidos administrados
	 * */
	private HashMap liquidosMap1;
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Seccion Hemoderivados
	/*************************************************************************************************/
		
	/**
	 * Indicador existe información de Hemoderivados
	 * */
	private String indicadorInfoHemoderivados;
	
	/**
	 * HashMap datos Hemoderivados administrados
	 * */
	private HashMap hemoderivadosMap1;
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Seccion Infusiones
	/*************************************************************************************************/
		
	/**
	 * Indicador existe información de Infusiones
	 * */
	private String indicadorInfoInfusiones;
	
	/**
	 * HashMap datos Hemoderivados administrados
	 * */
	private HashMap infusionesMap1;
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Seccion Balance de Liquidos
	/*************************************************************************************************/
	
	/**
	 * Indicador existe información Anestesiologos
	 * */
	private String indicadorInfoBalanceLiquidos;
	
	
	/**
	 * HashMap datos Balance de Liquidos 1
	 * */
	private HashMap balanceLiquidosMap1;
	
	/**
	 * HashMap datos Balance de liquidos 2
	 * */
	private HashMap balanceLiquidosMap2;
	
	
	/*************************************************************************************************/
	//************************************************************************************************
	//Seccion Observaciones Generales
	/*************************************************************************************************/
	
	/**
	 * Indicador existe información de Observaciones
	 * */
	private String indicadorInfoObservaciones;
	
	/**
	 * HashMap observacionesMap
	 * */
	private HashMap observacionesMap;
	
	/*************************************************************************************************/
	//************************************************************************************************

	/**
	 * @return the especialidadesMap
	 */
	public HashMap getEspecialidadesMap() {
		return especialidadesMap;
	}

	/**
	 * @param especialidadesMap the especialidadesMap to set
	 */
	public void setEspecialidadesMap(HashMap especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}
	
	/**
	 * @return the especialidadesMap
	 */
	public Object getEspecialidadesMap(String key) {
		return especialidadesMap.get(key);
	}

	/**
	 * @param especialidadesMap the especialidadesMap to set
	 */
	public void setEspecialidadesMap(String key, Object value) {
		this.especialidadesMap.put(key, value);
	}

	/**
	 * @return the cirujanosMap
	 */
	public HashMap getCirujanosMap() {
		return cirujanosMap;
	}

	/**
	 * @param cirujanosMap the cirujanosMap to set
	 */
	public void setCirujanosMap(HashMap cirujanosMap) {
		this.cirujanosMap = cirujanosMap;
	}
	
	/**
	 * @return the cirujanosMap
	 */
	public Object getCirujanosMap(String key) {
		return cirujanosMap.get(key);
	}

	/**
	 * @param cirujanosMap the cirujanosMap to set
	 */
	public void setCirujanosMap(String key, Object value) {
		this.cirujanosMap.put(key, value);
	}
	

	/**
	 * @return the indicadorCargoDto
	 */
	public String getIndicadorCargoDto() {
		return indicadorCargoDto;
	}

	/**
	 * @param indicadorCargoDto the indicadorCargoDto to set
	 */
	public void setIndicadorCargoDto(String indicadorCargoDto) {
		this.indicadorCargoDto = indicadorCargoDto;
	}

	/**
	 * @return the indicadorInfoEspInterCirPrin
	 */
	public String getIndicadorInfoEspInterCirPrin() {
		return indicadorInfoEspInterCirPrin;
	}

	/**
	 * @param indicadorInfoEspInterCirPrin the indicadorInfoEspInterCirPrin to set
	 */
	public void setIndicadorInfoEspInterCirPrin(String indicadorInfoEspInterCirPrin) {
		this.indicadorInfoEspInterCirPrin = indicadorInfoEspInterCirPrin;
	}

	/**
	 * @return the anestesiologosMap
	 */
	public HashMap getAnestesiologosMap() {
		return anestesiologosMap;
	}

	/**
	 * @param anestesiologosMap the anestesiologosMap to set
	 */
	public void setAnestesiologosMap(HashMap anestesiologosMap) {
		this.anestesiologosMap = anestesiologosMap;
	}
	
	/**
	 * @return the anestesiologosMap
	 */
	public Object getAnestesiologosMap(String key) {
		return anestesiologosMap.get(key);
	}

	/**
	 * @param anestesiologosMap the anestesiologosMap to set
	 */
	public void setAnestesiologosMap(String key, Object value) {
		this.anestesiologosMap.put(key, value);
	}

	/**
	 * @return the indicadorInfoAnestesiologos
	 */
	public String getIndicadorInfoAnestesiologos() {
		return indicadorInfoAnestesiologos;
	}

	/**
	 * @param indicadorInfoAnestesiologos the indicadorInfoAnestesiologos to set
	 */
	public void setIndicadorInfoAnestesiologos(String indicadorInfoAnestesiologos) {
		this.indicadorInfoAnestesiologos = indicadorInfoAnestesiologos;
	}

	/**
	 * @return the infoHojaAnestesiaMap
	 */
	public HashMap getInfoHojaAnestesiaMap() {
		return infoHojaAnestesiaMap;
	}

	/**
	 * @param infoHojaAnestesiaMap the infoHojaAnestesiaMap to set
	 */
	public void setInfoHojaAnestesiaMap(HashMap infoHojaAnestesiaMap) {
		this.infoHojaAnestesiaMap = infoHojaAnestesiaMap;
	}	

	/**
	 * @return the infoHojaAnestesiaMap
	 */
	public Object getInfoHojaAnestesiaMap(String key) {
		return infoHojaAnestesiaMap.get(key);
	}

	/**
	 * @param infoHojaAnestesiaMap the infoHojaAnestesiaMap to set
	 */
	public void setInfoHojaAnestesiaMap(String key, Object value) {
		this.infoHojaAnestesiaMap.put(key, value);
	}

	/**
	 * @return the solicitudCxMap
	 */
	public HashMap getSolicitudCxMap() {
		return solicitudCxMap;
	}

	/**
	 * @param solicitudCxMap the solicitudCxMap to set
	 */
	public void setSolicitudCxMap(HashMap solicitudCxMap) {
		this.solicitudCxMap = solicitudCxMap;
	}
	
	/**
	 * @return the solicitudCxMap
	 */
	public Object getSolicitudCxMap(String key) {
		return solicitudCxMap.get(key);
	}

	/**
	 * @param solicitudCxMap the solicitudCxMap to set
	 */
	public void setSolicitudCxMap(String key, Object value) {
		this.solicitudCxMap.put(key, value);
	}

	/**
	 * @return the observacionesMap
	 */
	public HashMap getObservacionesMap() {
		return observacionesMap;
	}

	/**
	 * @param observacionesMap the observacionesMap to set
	 */
	public void setObservacionesMap(HashMap observacionesMap) {
		this.observacionesMap = observacionesMap;
	}
	
	/**
	 * @return the observacionesMap
	 */
	public Object getObservacionesMap(String key) {
		return observacionesMap.get(key);
	}

	/**
	 * @param observacionesMap the observacionesMap to set
	 */
	public void setObservacionesMap(String key, Object value) {
		this.observacionesMap.put(key, value);
	}

	/**
	 * @return the indicadorInfoObservaciones
	 */
	public String getIndicadorInfoObservaciones() {
		return indicadorInfoObservaciones;
	}

	/**
	 * @param indicadorInfoObservaciones the indicadorInfoObservaciones to set
	 */
	public void setIndicadorInfoObservaciones(String indicadorInfoObservaciones) {
		this.indicadorInfoObservaciones = indicadorInfoObservaciones;
	}

	/**
	 * @return the balanceLiquidosMap1
	 */
	public HashMap getBalanceLiquidosMap1() {
		return balanceLiquidosMap1;
	}

	/**
	 * @param balanceLiquidosMap1 the balanceLiquidosMap1 to set
	 */
	public void setBalanceLiquidosMap1(HashMap balanceLiquidosMap1) {
		this.balanceLiquidosMap1 = balanceLiquidosMap1;
	}
	
	/**
	 * @return the balanceLiquidosMap1
	 */
	public Object getBalanceLiquidosMap1(String key) {
		return balanceLiquidosMap1.get(key);
	}

	/**
	 * @param balanceLiquidosMap1 the balanceLiquidosMap1 to set
	 */
	public void setBalanceLiquidosMap1(String key, Object value) {
		this.balanceLiquidosMap1.put(key, value);
	}

	/**
	 * @return the balanceLiquidosMap2
	 */
	public HashMap getBalanceLiquidosMap2() {
		return balanceLiquidosMap2;
	}

	/**
	 * @param balanceLiquidosMap2 the balanceLiquidosMap2 to set
	 */
	public void setBalanceLiquidosMap2(HashMap balanceLiquidosMap2) {
		this.balanceLiquidosMap2 = balanceLiquidosMap2;
	}
	
	/**
	 * @return the balanceLiquidosMap2
	 */
	public Object getBalanceLiquidosMap2(String key) {
		return balanceLiquidosMap2.get(key);
	}

	/**
	 * @param balanceLiquidosMap2 the balanceLiquidosMap2 to set
	 */
	public void setBalanceLiquidosMap2(String key, Object value) {
		this.balanceLiquidosMap2.put(key, value);
	}

	/**
	 * @return the indicadorInfoBalanceLiquidos
	 */
	public String getIndicadorInfoBalanceLiquidos() {
		return indicadorInfoBalanceLiquidos;
	}

	/**
	 * @param indicadorInfoBalanceLiquidos the indicadorInfoBalanceLiquidos to set
	 */
	public void setIndicadorInfoBalanceLiquidos(String indicadorInfoBalanceLiquidos) {
		this.indicadorInfoBalanceLiquidos = indicadorInfoBalanceLiquidos;
	}

	/**
	 * @return the indicadorInfoAnestesicosMedAdminis
	 */
	public String getIndicadorInfoAnestesicosMedAdminis() {
		return indicadorInfoAnestesicosMedAdminis;
	}

	/**
	 * @param indicadorInfoAnestesicosMedAdminis the indicadorInfoAnestesicosMedAdminis to set
	 */
	public void setIndicadorInfoAnestesicosMedAdminis(
			String indicadorInfoAnestesicosMedAdminis) {
		this.indicadorInfoAnestesicosMedAdminis = indicadorInfoAnestesicosMedAdminis;
	}

	/**
	 * @return the anestesicosMedAdmMap1
	 */
	public HashMap getAnestesicosMedAdmMap1() {
		return anestesicosMedAdmMap1;
	}

	/**
	 * @param anestesicosMedAdmMap1 the anestesicosMedAdmMap1 to set
	 */
	public void setAnestesicosMedAdmMap1(HashMap anestesicosMedAdmMap1) {
		this.anestesicosMedAdmMap1 = anestesicosMedAdmMap1;
	}
		
	/**
	 * @return the anestesicosMedAdmMap1
	 */
	public Object getAnestesicosMedAdmMap1(String key) {
		return anestesicosMedAdmMap1.get(key);
	}

	/**
	 * @param anestesicosMedAdmMap1 the anestesicosMedAdmMap1 to set
	 */
	public void setAnestesicosMedAdmMap1(String key, Object value) {
		this.anestesicosMedAdmMap1.put(key, value);
	}
		
	/**
	 * @return the indicadorInfoliquidos
	 */
	public String getIndicadorInfoliquidos() {
		return indicadorInfoliquidos;
	}

	/**
	 * @param indicadorInfoliquidos the indicadorInfoliquidos to set
	 */
	public void setIndicadorInfoliquidos(String indicadorInfoliquidos) {
		this.indicadorInfoliquidos = indicadorInfoliquidos;
	}

	/**
	 * @return the liquidosMap1
	 */
	public HashMap getLiquidosMap1() {
		return liquidosMap1;
	}

	/**
	 * @param liquidosMap1 the liquidosMap1 to set
	 */
	public void setLiquidosMap1(HashMap liquidosMap1) {
		this.liquidosMap1 = liquidosMap1;
	}
	
	/**
	 * @return the liquidosMap1
	 */
	public Object getLiquidosMap1(String key) {
		return liquidosMap1.get(key);
	}

	/**
	 * @param liquidosMap1 the liquidosMap1 to set
	 */
	public void setLiquidosMap1(String key, Object value) {
		this.liquidosMap1.put(key, value);
	}

	/**
	 * @return the hemoderivadosMap1
	 */
	public HashMap getHemoderivadosMap1() {
		return hemoderivadosMap1;
	}

	/**
	 * @param hemoderivadosMap1 the hemoderivadosMap1 to set
	 */
	public void setHemoderivadosMap1(HashMap hemoderivadosMap1) {
		this.hemoderivadosMap1 = hemoderivadosMap1;
	}
	
	/**
	 * @return the hemoderivadosMap1
	 */
	public Object getHemoderivadosMap1(String key) {
		return hemoderivadosMap1.get(key);
	}

	/**
	 * @param hemoderivadosMap1 the hemoderivadosMap1 to set
	 */
	public void setHemoderivadosMap1(String key, Object value) {
		this.hemoderivadosMap1.put(key, value);
	}

	/**
	 * @return the indicadorInfoHemoderivados
	 */
	public String getIndicadorInfoHemoderivados() {
		return indicadorInfoHemoderivados;
	}

	/**
	 * @param indicadorInfoHemoderivados the indicadorInfoHemoderivados to set
	 */
	public void setIndicadorInfoHemoderivados(String indicadorInfoHemoderivados) {
		this.indicadorInfoHemoderivados = indicadorInfoHemoderivados;
	}

	/**
	 * @return the indicadorInfoInfusiones
	 */
	public String getIndicadorInfoInfusiones() {
		return indicadorInfoInfusiones;
	}

	/**
	 * @param indicadorInfoInfusiones the indicadorInfoInfusiones to set
	 */
	public void setIndicadorInfoInfusiones(String indicadorInfoInfusiones) {
		this.indicadorInfoInfusiones = indicadorInfoInfusiones;
	}

	/**
	 * @return the infusionesMap1
	 */
	public HashMap getInfusionesMap1() {
		return infusionesMap1;
	}

	/**
	 * @param infusionesMap1 the infusionesMap1 to set
	 */
	public void setInfusionesMap1(HashMap infusionesMap1) {
		this.infusionesMap1 = infusionesMap1;
	}
	
	/**
	 * @return the infusionesMap1
	 */
	public Object getInfusionesMap1(String key) {
		return infusionesMap1.get(key);
	}

	/**
	 * @param infusionesMap1 the infusionesMap1 to set
	 */
	public void setInfusionesMap1(String key, Object value) {
		this.infusionesMap1.put(key, value);
	}
}