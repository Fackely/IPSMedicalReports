package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.actionform.inventarios.SustitutosNoPosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.SustitutosNoPosDao;

import util.ConstantesBD;

public class SustitutosNoPos
{
	private static Logger logger = Logger.getLogger(SustitutosNoPos.class);
	
	/**
	 * 
	 */
	private String articuloSustituto;
	
	/**
	 * 
	 */
	private String articuloPrincipal;
	
	/**
	 * 
	 */
	private String nomModSus;
	
	/**
	 * 
	 */
	private String tiempoTratamiento;
	
	/**
	 * 
	 */
	private String dosificacion;
	
	/**
	 * 
	 */
	private String dosisDiaria;
	
	/**
	 * 
	 */
	private String numDosisEquivalentes;
	
	/**
	 * 
	 */
	private String indexMap;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private HashMap resultadosMap;
	
	/**
	 * 
	 */
	private HashMap resultadosCGMap;
	
	/**
	 * 
	 */
	private String codNueArtSus;

	/**
	 * 
	 * */
	private static SustitutosNoPosDao getSustitutosNoPosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSustitutosNoPosDao();		
	}
	
	/**
	 * Metodo de consulta de los articulos sustitutos de un articulo seleccionado
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */	
	public static HashMap<String, Object> consultaSus (Connection con, int codigoArtPpal)
	{
		return getSustitutosNoPosDao().consultaSus(con, codigoArtPpal);
	}
	
	/**
	 * Metodo de consulta de la clase y el grupo de articulo principal seleccionado
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */	
	public static HashMap<String, Object> consultaCG (Connection con, int codigoArtPpal)
	{
		return getSustitutosNoPosDao().consultaCG(con, codigoArtPpal);
	}
	
	/**
	 * Metodo de validacion de los articulos sustitutos de un articulo seleccionado
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public static boolean validarSus (HashMap datos, String codigo)
	{	
		int numRegistros = Integer.parseInt(datos.get("numRegistros").toString());
		
		for(int i=0;i<numRegistros;i++){
		 	if(datos.get("articuloSustituto_"+i).toString().equals(codigo))
		 		return false;
		}
		return true;
	}
	
	/**
	 * Metodo de la modificacion de los articulos sustitutos de un articulo seleccionado
	 * @param con
	 * @param sustitutoNoPos
	 * @return
	 */
	public static boolean modificar(Connection con, SustitutosNoPos sustitutosNoPos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSustitutosNoPosDao().modificarSustitutosNoPos(con, sustitutosNoPos);
	}
	
	/**
	 * 
	 */
	public static boolean eliminar(Connection con, String sustitutosNoPos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSustitutosNoPosDao().eliminarSustitutosNoPos(con, sustitutosNoPos);
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean insertar(Connection con, SustitutosNoPos sustitutosNoPos){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSustitutosNoPosDao().insertarSustitutosNoPos(con, sustitutosNoPos);
	}

	public String getArticuloSustituto() {
		return articuloSustituto;
	}

	public void setArticuloSustituto(String articuloSustituto) {
		this.articuloSustituto = articuloSustituto;
	}

	public String getDosificacion() {
		return dosificacion;
	}

	public void setDosificacion(String dosificacion) {
		this.dosificacion = dosificacion;
	}	

	public String getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}

	public String getNomModSus() {
		return nomModSus;
	}

	public void setNomModSus(String nomModSus) {
		this.nomModSus = nomModSus;
	}		

	public String getArticuloPrincipal() {
		return articuloPrincipal;
	}

	public void setArticuloPrincipal(String articuloPrincipal) {
		this.articuloPrincipal = articuloPrincipal;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	public HashMap getResultadosMap() {
		return resultadosMap;
	}


	public void setResultadosMap(HashMap resultados) {
		this.resultadosMap = resultados;
	}
	
	public Object getResultadosMap(String key) {
		return resultadosMap.get(key);
	}


	public void setResultadosMap(String key, Object value) {
		this.resultadosMap.put(key, value);
	}

	public String getDosisDiaria() {
		return dosisDiaria;
	}

	public void setDosisDiaria(String dosisDiaria) {
		this.dosisDiaria = dosisDiaria;
	}

	public String getNumDosisEquivalentes() {
		return numDosisEquivalentes;
	}

	public void setNumDosisEquivalentes(String numDosisEquivalentes) {
		this.numDosisEquivalentes = numDosisEquivalentes;
	}

	public String getTiempoTratamiento() {
		return tiempoTratamiento;
	}

	public void setTiempoTratamiento(String tiempoTratamiento) {
		this.tiempoTratamiento = tiempoTratamiento;
	}
	
	public String getCodNueArtSus() {
		return codNueArtSus;
	}

	public void setCodNueArtSus(String codNueArtSus) {
		this.codNueArtSus = codNueArtSus;
	}
	
	public HashMap getResultadosCGMap() {
		return resultadosCGMap;
	}



	public void setResultadosCGMap(HashMap resultadosCGMap) {
		this.resultadosCGMap = resultadosCGMap;
	}
	
	public Object getResultadosCGMap(String key) {
		return resultadosCGMap.get(key);
	}


	public void setResultadosCGMap(String key, Object value) {
		this.resultadosCGMap.put(key, value);
	}
}