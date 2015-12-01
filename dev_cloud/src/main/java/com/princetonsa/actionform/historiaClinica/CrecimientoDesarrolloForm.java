/* Princeton S.A (Parquesoft-Manizales)
*  Andrés Mauricio Ruiz Vélez
*  Creado 14-nov-2006 11:05:38
*/


package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import util.UtilidadCadena;

public class CrecimientoDesarrolloForm extends ActionForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CrecimientoDesarrolloForm.class);
	
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa que contiene la información de los exámenes físicos de todas
	 * las valoraciones para la realización de la curva de crecimiento y desarrollo
	 */
	private HashMap mapaCrecimientoDesarrollo;
	
	/**
	 * Mapa para tener los datos de la curva de EDAD X PESO de TODOS LOS PERCENTILES 
	 */
	private HashMap mapaEdadPeso;

	/**
	 * Mapa para tener los datos de la curva de EDAD X ESTATURA de TODOS LOS PERCENTILES 
	 */
	private HashMap mapaEdadEstatura;

	/**
	 * Mapa para tener los datos de la curva de PESO X ESTATURA de TODOS LOS PERCENTILES 
	 */
	private HashMap mapaPesoEstatura;
	
	/**
	 * Mapa para tener los datos de la curva de Perimetro cefalico 
	 */
	private HashMap mapaEdadPerCefalico;

	/**
	 * Mapa para tener los datos de la curva del Indice de Masa Corporal. 
	 */
	private HashMap mapaEdadImc;
	
	/**
	 * para saber las Graficas que se van a mostrar . 
	 */
	private int tipoGrafica;
	
	private String tallaActual="";
	private String pesoActual="";
	private String imcActual="";
	private String perCefalicoActual="";
	
	/**
	 * Función para resetear los valores de la funcionalidad.
	 *
	 */
	public void reset()
	{
		this.tipoGrafica = 0;
		this.mapaCrecimientoDesarrollo = new HashMap();
		this.mapaEdadPeso = new HashMap();
		this.mapaEdadEstatura = new HashMap();
		this.mapaPesoEstatura = new HashMap();
		this.mapaEdadPerCefalico = new HashMap();
		this.mapaEdadImc = new HashMap();
	}
	
	
//------------------------------------- SETS Y GETS ---------------------------------------------------//

	/**
	 * return estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * setea estado
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return mapaCrecimientoDesarrollo
	 */
	public HashMap getMapaCrecimientoDesarrollo() {
		return mapaCrecimientoDesarrollo;
	}

	/**
	 * Setea mapaCrecimientoDesarrollo
	 * @param mapaCrecimientoDesarrollo
	 */
	public void setMapaCrecimientoDesarrollo(HashMap mapaCrecimientoDesarrollo) {
		this.mapaCrecimientoDesarrollo = mapaCrecimientoDesarrollo;
	}

	/**
	 */
	public Object getMapaCrecimientoDesarrollo(String key) {
		return this.mapaCrecimientoDesarrollo.get(key);
	}

	/**
	 */
	public void setMapaCrecimientoDesarrollo(String key, String valor) {
		this.mapaCrecimientoDesarrollo.put(key,valor);
	}
	//------------------EDADPESO
	/**
	 */
	public Object getMapaEdadPeso(String key) {
		return this.mapaEdadPeso.get(key);
	}

	/**
	 */
	public void setMapaEdadPeso(String key, String valor) {
		this.mapaEdadPeso.put(key,valor);
	}

	/**
	 */
	public HashMap getMapaEdadPeso() {
		return mapaEdadPeso;
	}

	/**
	 */
	public void setMapaEdadPeso(HashMap mapa) {
		this.mapaEdadPeso = mapa;
	}

	//------------------EDADESTATURA
	/**
	 */
	public Object getMapaEdadEstatura(String key) {
		return this.mapaEdadEstatura.get(key);
	}

	/**
	 */
	public void setMapaEdadEstatura(String key, String valor) {
		this.mapaEdadEstatura.put(key,valor);
	}

	/**
	 */
	public HashMap getMapaEdadEstatura() {
		return mapaEdadEstatura;
	}

	/**
	 */
	public void setMapaEdadEstatura(HashMap mapa) {
		this.mapaEdadEstatura = mapa;
	}

	//------------------PESOESTATURA
	/**
	 */
	public Object getMapaPesoEstatura(String key) {
		return this.mapaPesoEstatura.get(key);
	}

	/**
	 */
	public void setMapaPesoEstatura(String key, String valor) {
		this.mapaPesoEstatura.put(key,valor);
	}

	/**
	 */
	public HashMap getMapaPesoEstatura() {
		return mapaPesoEstatura;
	}

	/**
	 */
	public void setMapaPesoEstatura(HashMap mapa) {
		this.mapaPesoEstatura = mapa;
	}
	
	//------------------PERIMETRO CEFALICO
	/**
	 */
	public Object getMapaEdadPerCefalico(String key) {
		return this.mapaEdadPerCefalico.get(key);
	}

	/**
	 */
	public void setMapaEdadPerCefalico(String key, String valor) {
		this.mapaEdadPerCefalico.put(key,valor);
	}

	/**
	 */
	public HashMap getMapaEdadPerCefalico() {
		return mapaEdadPerCefalico;
	}

	/**
	 */
	public void setMapaEdadPerCefalico(HashMap mapa) {
		this.mapaEdadPerCefalico = mapa;
	}

	//-------------------EDAD IMC

	/**
	 */
	public Object getMapaEdadImc(String key) {
		return this.mapaEdadImc.get(key);
	}

	/**
	 */
	public void setMapaEdadImc(String key, String valor) {
		this.mapaEdadImc.put(key,valor);
	}

	/**
	 */
	public HashMap getMapaEdadImc() {
		return mapaEdadImc;
	}

	/**
	 */
	public void setMapaEdadImc(HashMap mapa) {
		this.mapaEdadImc = mapa;
	}

	public int getTipoGrafica() {
		return tipoGrafica;
	}

	public void setTipoGrafica(int tipoGrafica) {
		this.tipoGrafica = tipoGrafica;
	}

	public String getImcActual()
	{
		return imcActual;
	}

	public void setImcActual(String imcActual)
	{
		this.imcActual = imcActual;
	}

	public String getPerCefalicoActual()
	{
		return perCefalicoActual;
	}

	public void setPerCefalicoActual(String perCefalicoActual)
	{
		this.perCefalicoActual = perCefalicoActual;
	}

	public String getPesoActual()
	{
		return pesoActual;
	}

	public void setPesoActual(String pesoActual)
	{
		this.pesoActual = pesoActual;
	}

	public String getTallaActual()
	{
		return tallaActual;
	}

	public void setTallaActual(String tallaActual)
	{
		this.tallaActual = tallaActual;
	}
	

}
