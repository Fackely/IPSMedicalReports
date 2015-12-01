/*
 * Abril 11, 2008
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;


/**
 * @author Sebastián Gómez R
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Registro Rips Cargos Directos 
 */
public class RegistroRipsCargosDirectosForm extends ValidatorForm 
{
	Logger logger = Logger.getLogger(RegistroRipsCargosDirectosForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Listado de las cuentas
	 */
	private HashMap<String, Object> listadoCuentas = new HashMap<String, Object>();
	
	private HashMap<String, Object> listadoSolicitudes = new HashMap<String, Object>();
	
	/**
	 * Mapa para almacenar los datos del detalle de la solicitud
	 */
	private HashMap<String, Object> datos = new HashMap<String, Object>();
	
	/**
	 * Mapa para almacernar la información de los recién nacidos
	 */
	private HashMap<String, Object> infoRecienNacidos = new HashMap<String, Object>();
	
	/**
	 * Id de la cuenta
	 */
	private String idCuenta;
	
	/**
	 * Para saber si el flujo es por periodo o por paciente
	 */
	private boolean periodo;
	/**
	 * Atributos usados para la ordenación y el indexado de registros
	 */
	private String indice;
	private String ultimoIndice;
	private int indexCuenta;
	private int indexSolicitud;
	
	private String tipoServicio;
	private String numeroSolicitud;
	private int tipoSolicitud;
	
	
	//***********Arreglos**********************************************************
	ArrayList<HashMap<String, Object>> causasExternas = new ArrayList<HashMap<String,Object>>();
	ArrayList<HashMap<String, Object>> finalidades = new ArrayList<HashMap<String,Object>>();
	ArrayList<HashMap<String, Object>> sexos = new ArrayList<HashMap<String,Object>>();
	
	//*******************ATRIBUTOS PARA LA OPCION POR PERIODO***************************
	private HashMap<String,Object> parametros = new HashMap<String, Object>();
	//***********Arreglos de la opcion por periodo*************************************
	ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
	ArrayList<HashMap<String, Object>> estadosCuenta = new ArrayList<HashMap<String,Object>>();
	ArrayList<HashMap<String, Object>> viasIngreso = new ArrayList<HashMap<String,Object>>();
	ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	//*********************************************************************************
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.listadoCuentas = new HashMap<String, Object>();
		this.listadoSolicitudes = new HashMap<String, Object>();
		this.periodo = false;
		
		
		
		this.indice = "";
		this.ultimoIndice = "";
		this.indexCuenta = ConstantesBD.codigoNuncaValido;
		this.indexSolicitud = ConstantesBD.codigoNuncaValido;
		
		this.idCuenta = "";
		this.tipoServicio = "";
		this.numeroSolicitud = "";
		this.tipoSolicitud = 0;
		
		//Arreglos**************************************+
		this.causasExternas = new ArrayList<HashMap<String,Object>>();
		this.finalidades = new ArrayList<HashMap<String,Object>>();
		this.sexos = new ArrayList<HashMap<String,Object>>();
		
		this.parametros = new HashMap<String, Object>();
		//Arreglos propios de la opcion por periodo************
		this.centrosAtencion = new ArrayList<HashMap<String,Object>>();
		this.estadosCuenta = new ArrayList<HashMap<String,Object>>();
		this.viasIngreso = new ArrayList<HashMap<String,Object>>();
		this.convenios = new ArrayList<HashMap<String,Object>>();
		
		this.resetDetalle();
	}
	
	/**
	 * Reset para el detalle de la solicitud
	 *
	 */
	public void resetDetalle()
	{
		this.datos = new HashMap<String, Object>();
		this.infoRecienNacidos = new HashMap<String, Object>();
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		return errores;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the listadoCuentas
	 */
	public HashMap<String, Object> getListadoCuentas() {
		return listadoCuentas;
	}

	/**
	 * @param listadoCuentas the listadoCuentas to set
	 */
	public void setListadoCuentas(HashMap<String, Object> listadoCuentas) {
		this.listadoCuentas = listadoCuentas;
	}
	
	/**
	 * @return the listadoCuentas
	 */
	public Object getListadoCuentas(String key) {
		return listadoCuentas.get(key);
	}

	/**
	 * @param listadoCuentas the listadoCuentas to set
	 */
	public void setListadoCuentas(String key,Object obj) {
		this.listadoCuentas.put(key,obj);
	}

	/**
	 * @return the listadoSolicitudes
	 */
	public HashMap<String, Object> getListadoSolicitudes() {
		return listadoSolicitudes;
	}

	/**
	 * @param listadoSolicitudes the listadoSolicitudes to set
	 */
	public void setListadoSolicitudes(HashMap<String, Object> listadoSolicitudes) {
		this.listadoSolicitudes = listadoSolicitudes;
	}
	
	/**
	 * @return the listadoSolicitudes
	 */
	public Object getListadoSolicitudes(String key) {
		return listadoSolicitudes.get(key);
	}

	/**
	 * @param listadoSolicitudes the listadoSolicitudes to set
	 */
	public void setListadoSolicitudes(String key,Object obj) {
		this.listadoSolicitudes.put(key,obj);
	}
	
	/**
	 * Método para obtener el tamaño del listado de las cuentas
	 * @return
	 */
	public int getNumListadoCuentas()
	{
		return Utilidades.convertirAEntero(this.getListadoCuentas("numRegistros")+"", true);
	}
	
	/**
	 * Método para obtener el tamaño del listado de las solicitudes
	 * @return
	 */
	public int getNumListadoSolicitudes()
	{
		return Utilidades.convertirAEntero(this.getListadoSolicitudes("numRegistros")+"", true);
	}

	/**
	 * @return the indexCuenta
	 */
	public int getIndexCuenta() {
		return indexCuenta;
	}

	/**
	 * @param indexCuenta the indexCuenta to set
	 */
	public void setIndexCuenta(int indexCuenta) {
		this.indexCuenta = indexCuenta;
	}

	/**
	 * @return the indexSolicitud
	 */
	public int getIndexSolicitud() {
		return indexSolicitud;
	}

	/**
	 * @param indexSolicitud the indexSolicitud to set
	 */
	public void setIndexSolicitud(int indexSolicitud) {
		this.indexSolicitud = indexSolicitud;
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return the idCuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta the idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the datos
	 */
	public HashMap<String, Object> getDatos() {
		return datos;
	}

	/**
	 * @param datos the datos to set
	 */
	public void setDatos(HashMap<String, Object> datos) {
		this.datos = datos;
	}
	
	/**
	 * @return the datos
	 */
	public Object getDatos(String key) {
		return datos.get(key);
	}

	/**
	 * @param datos the datos to set
	 */
	public void setDatos(String key,Object obj) {
		this.datos.put(key, obj);
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the causasExternas
	 */
	public ArrayList<HashMap<String, Object>> getCausasExternas() {
		return causasExternas;
	}

	/**
	 * @param causasExternas the causasExternas to set
	 */
	public void setCausasExternas(ArrayList<HashMap<String, Object>> causasExternas) {
		this.causasExternas = causasExternas;
	}

	/**
	 * @return the finalidades
	 */
	public ArrayList<HashMap<String, Object>> getFinalidades() {
		return finalidades;
	}

	/**
	 * @param finalidades the finalidades to set
	 */
	public void setFinalidades(ArrayList<HashMap<String, Object>> finalidades) {
		this.finalidades = finalidades;
	}

	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	/**
	 * @return the infoRecienNacidos
	 */
	public HashMap<String, Object> getInfoRecienNacidos() {
		return infoRecienNacidos;
	}

	/**
	 * @param infoRecienNacidos the infoRecienNacidos to set
	 */
	public void setInfoRecienNacidos(HashMap<String, Object> infoRecienNacidos) {
		this.infoRecienNacidos = infoRecienNacidos;
	}
	
	/**
	 * @return the infoRecienNacidos
	 */
	public Object getInfoRecienNacidos(String key) {
		return infoRecienNacidos.get(key);
	}

	/**
	 * @param infoRecienNacidos the infoRecienNacidos to set
	 */
	public void setInfoRecienNacidos(String key, Object obj) {
		this.infoRecienNacidos.put(key,obj);
	}

	/**
	 * @return the sexos
	 */
	public ArrayList<HashMap<String, Object>> getSexos() {
		return sexos;
	}

	/**
	 * @param sexos the sexos to set
	 */
	public void setSexos(ArrayList<HashMap<String, Object>> sexos) {
		this.sexos = sexos;
	}

	/**
	 * @return the centrosAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the estadosCuenta
	 */
	public ArrayList<HashMap<String, Object>> getEstadosCuenta() {
		return estadosCuenta;
	}

	/**
	 * @param estadosCuenta the estadosCuenta to set
	 */
	public void setEstadosCuenta(ArrayList<HashMap<String, Object>> estadosCuenta) {
		this.estadosCuenta = estadosCuenta;
	}

	/**
	 * @return the parametros
	 */
	public HashMap<String, Object> getParametros() {
		return parametros;
	}

	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(HashMap<String, Object> parametros) {
		this.parametros = parametros;
	}
	
	/**
	 * @return the parametros
	 */
	public Object getParametros(String key) {
		return parametros.get(key);
	}

	/**
	 * @param parametros the parametros to set
	 */
	public void setParametros(String key,Object obj) {
		this.parametros.put(key,obj);
	}

	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the viasIngreso
	 */
	public ArrayList<HashMap<String, Object>> getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(ArrayList<HashMap<String, Object>> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	/**
	 * @return the periodo
	 */
	public boolean isPeriodo() {
		return periodo;
	}

	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(boolean periodo) {
		this.periodo = periodo;
	}
	
}
