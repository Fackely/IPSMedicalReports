/*
 * Creado en Febreo del 2008
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.parametrizacion.ConstantesSeccionesParametrizables;

import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;

/**
 * @author Jose Eduardo Arias Doncel
 */
@SuppressWarnings("unchecked")
public class HojaAnestesiaForm extends ValidatorForm
{
	//***********************************************************
	//Atributos**************************************************
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Funcionalidad Dummy
	/**
     * encargado de indicar si se oculta o no el cabezote superior
     */
    private boolean  ocultarEncabezado = false;
    /**
     * numero de la peticion
     */
    private String peticion = "";
    /**
     * nombre de la funcionalidad que esta llamando la hqx
     */
    private String funcionalidad ="";
    /**
     * inidca si es llamado de una funcionalidad dummy o no
     */
    private boolean esDummy = false;
    /**
     * Indica el parametro pasado por llamado para la modificacion de la hoja de anestesia
     * */
    private boolean esModificableDummy = false;    
    /**
     * Indica si se debe ocultar el menu
     * */
    private boolean ocultarMenu = false;
    /**
     * codigo de la cita, es utilizado cuando es llamado desde la funcionalidad de citas
     */
    private String codigoCita=""; 
	
	
	//Atributos para el pager
	/**
	 * String link Siguiente
	 * */
	private String linkSiguiente;
	
	/**
	 * String patron ordenar
	 * */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron ordenar
	 * */
	private String ultimoPatronOrdenar;	
	
	
	//Atributos para el Listado de Peticiones
	
	 /**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Indicador de las secciones parametrizable
	 * */
	private String seccion;
	
	/**
	 * Indicador del listado de peticion
	 * */
	private String indexPeticion;
	
	/**
	 * Maneja el listado de Peticiones
	 * */
	private HashMap listadoPeticionesMap;
	
	/**
	 * HashMap centros de Costo
	 * */
	private HashMap centroCostosMap;	
		
	/**
	 * HashMap Especialidades
	 * */
	private HashMap especialidadesMap;
	
	/**
	 * HashMap SolicitudMap
	 * */
	private HashMap solicitudMap;
	
	/**
	 * 
	 * */
	private String codigoPaciente;
	
	
	
	//Atributos para la Hoja de Anestesia
	
	/**
	 * Maneja las validaciones de la funcionalidad
	 * */
	private HashMap validacionesMap;
		
	/**
	 * HashMap utilitario para almacenar datos de una 
	 * seccion o subseccion 1
	 * */
	private HashMap mapaUtilitario_1_Map = new HashMap();
	
	
	/**
	 * ArrayList utilitario para almacenar datos 
	 * */
	private ArrayList arrayListUtilitario_1;
	
	/**
	 * ArrayList utilitario para almacenar datos 
	 * */
	private ArrayList arrayListUtilitario_2;
	
	/**
	 * ArrayList utilitario para almacenar datos 
	 * */
	private ArrayList arrayListUtilitario_3;
	
	/**
	 * HashMap utilitario para almacenar datos de una 
	 * seccion o subseccion 2
	 * */
	private HashMap mapaUtilitario_2_Map;
	
	/**
	 * HashMap utilitario para almacenar datos de una 
	 * seccion o subseccion 3
	 * */
	private HashMap mapaUtilitario_3_Map;
	
	/**
	 * HashMap utilitario para almacenar datos de una 
	 * seccion o subseccion 4
	 * */
	private HashMap mapaUtilitario_4_Map;
	
	/**
	 * HashMap utilitario para almacenar datos de un listado
	 * */
	private HashMap mapaUtilitarioBusquedas_1_Map;	
	
	/**
	 * String indicador utilitario
	 * */
	private String indicadorUtilitario;
	
	//*****************************************************************************************
	//***** CAMBIOS JUSTIFICACION NO POS **************
	
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos
	 */
	private HashMap justificacionMap=new HashMap();
	
	/**
	 * Mapa medicamento pos
	 */
	private HashMap medicamentosPosMap=new HashMap();
	
	/**
	 * Mapa medicamento no pos
	 */
	private HashMap medicamentosNoPosMap=new HashMap();
	
	/**
	 * Mapa medicamento sustituto no pos
	 */
	private HashMap sustitutosNoPosMap=new HashMap();
	
	/**
	 * Mapa diagnosticos definitivos
	 */
	private HashMap diagnosticosDefinitivos=new HashMap();
	
	/**
	 * Mapa diagnosticos presuntivos
	 */
	private HashMap diagnosticosPresuntivos=new HashMap();
	
	
	/**
	 * numero de justificacion
	 */
	private int numjus=0;
	
	/**
	 * numero solicitud
	 */
	private int solicitud=0;
	
	
	/**
	 * mapa de alertas justificacion pendiente
	 */
	private HashMap justificacionNoPosMap=new HashMap();
	
	/**
	 * Strign hiddens resultadoi de la generacin de hiddens
	 */
	private String hiddens="";
	
    /**
	 * Dto de articulos incluidos solicitud de procedimiento
	 * */
	private ArrayList <DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto;	
	
    private ArrayList<String> mensajes = new ArrayList<String>();
	
	
    
    
    // Anexo 179 - Cambio 1.50
	private boolean sinAutorizacionEntidadsubcontratada = false;
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	//********************************************************************************************************
	private boolean validacionCapitacion;
		
	
	
	//***********************************************************
	//Metodos****************************************************
	
	public ArrayList<String> getMensajes() {
		return mensajes;
	}


	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}


	/**
	 * Inicializa los atributos de la forma
	 * @param int seccion
	 * */
	public void reset(int seccion)
	{			
		if(seccion == ConstantesSeccionesParametrizables.subSeccionEspecialidadesIntervienenCiru)
		{
			this.mapaUtilitario_3_Map = new HashMap();
			this.mapaUtilitario_3_Map.put("numRegistros","0");			
		}
		else if(seccion == ConstantesSeccionesParametrizables.seccionAnestesicosMedicaAdminis)
		{
			this.mapaUtilitario_3_Map = new HashMap();
			this.mapaUtilitario_3_Map.put("numRegistros","0");
			
			this.mapaUtilitario_1_Map = new HashMap();
			this.mapaUtilitario_2_Map = new HashMap();
			
			this.arrayListUtilitario_1 = new ArrayList();
			
			//***** formato justificacion no pos
			this.justificacionMap=new HashMap();
			this.justificacionMap.put("numRegistros", 0);
			this.medicamentosNoPosMap=new HashMap();
			this.medicamentosNoPosMap.put("numRegistros", 0);
			this.medicamentosPosMap=new HashMap();
			this.medicamentosPosMap.put("numRegistros", 0);
			this.sustitutosNoPosMap=new HashMap();
			this.sustitutosNoPosMap.put("numRegistros", 0);
			this.numjus=0;
			this.diagnosticosDefinitivos=new HashMap();
			this.diagnosticosDefinitivos.put("numRegistros", 0);
			this.diagnosticosPresuntivos=new HashMap();
			this.diagnosticosPresuntivos.put("numRegistros", 0);
			this.justificacionNoPosMap=new HashMap();
			this.justificacionNoPosMap.put("numRegistros", 0);
			this.hiddens="";
			//*****
		}
		else if(seccion == ConstantesSeccionesParametrizables.seccionSalidaPaciente)
		{
			this.mapaUtilitario_1_Map = new HashMap();
			this.mapaUtilitario_2_Map = new HashMap();
			this.mapaUtilitario_3_Map = new HashMap();
			this.mapaUtilitario_3_Map.put("numRegistros","0");			
			this.arrayListUtilitario_1 = new ArrayList();
			this.arrayListUtilitario_2 = new ArrayList();
			this.arrayListUtilitario_3 = new ArrayList();
			this.arrayArticuloIncluidoDto = new ArrayList<DtoArticuloIncluidoSolProc>();
		}		
		else if(seccion == ConstantesBD.codigoNuncaValido)
		{
			this.listadoPeticionesMap = new HashMap();
			
			this.validacionesMap = new HashMap();
			this.linkSiguiente  = "";
			this.patronOrdenar = "";
			this.ultimoPatronOrdenar = "";		
			this.indexPeticion = "";
			this.centroCostosMap = new HashMap();
			this.especialidadesMap = new HashMap();
			this.solicitudMap = new HashMap();			
			
			this.mapaUtilitario_1_Map = new HashMap();
			this.mapaUtilitario_2_Map = new HashMap();
			this.mapaUtilitario_4_Map = new HashMap();
			this.indicadorUtilitario = "";
			this.mapaUtilitarioBusquedas_1_Map = new HashMap();
			
			this.listadoPeticionesMap.put("numRegistros",0);
			this.centroCostosMap.put("numRegistros",0);
			this.especialidadesMap.put("numRegistros",0);
			this.mapaUtilitario_4_Map.put("numRegistros","0");
			this.arrayListUtilitario_1 = new ArrayList();
			
			this.esDummy = false;
			this.peticion = "";
			this.funcionalidad = "";
			this.codigoCita = "";			
			this.ocultarEncabezado = false;
			this.esModificableDummy = true;
			
			
			//***** formato justificacion no pos
			this.justificacionMap=new HashMap();
			this.justificacionMap.put("numRegistros", 0);
			this.medicamentosNoPosMap=new HashMap();
			this.medicamentosNoPosMap.put("numRegistros", 0);
			this.medicamentosPosMap=new HashMap();
			this.medicamentosPosMap.put("numRegistros", 0);
			this.sustitutosNoPosMap=new HashMap();
			this.sustitutosNoPosMap.put("numRegistros", 0);
			this.numjus=0;
			this.diagnosticosDefinitivos=new HashMap();
			this.diagnosticosDefinitivos.put("numRegistros", 0);
			this.diagnosticosPresuntivos=new HashMap();
			this.diagnosticosPresuntivos.put("numRegistros", 0);
			this.justificacionNoPosMap=new HashMap();
			this.justificacionNoPosMap.put("numRegistros", 0);
			this.hiddens="";
			this.codigoPaciente = "";
			//*****
		}
			
		//this.sinAutorizacionEntidadsubcontratada = false;
		this.listaAdvertencias = new ArrayList<String>();
	}
	

	//	-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	

	/**
	 * @return the listadoPeticionesMap
	 */
	public HashMap getListadoPeticionesMap() {
		return listadoPeticionesMap;
	}

	/**
	 * @param listadoPeticionesMap the listadoPeticionesMap to set
	 */
	public void setListadoPeticionesMap(HashMap listadoPeticionesMap) {
		this.listadoPeticionesMap = listadoPeticionesMap;
	}
	
	
	/**
	 * @return the listadoPeticionesMap
	 */
	public Object getListadoPeticionesMap(String key) {
		return listadoPeticionesMap.get(key);
	}

	/**
	 * @param listadoPeticionesMap the listadoPeticionesMap to set
	 */
	public void setListadoPeticionesMap(String key, Object value) {
		this.listadoPeticionesMap.put(key, value);
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	
	/**
	 * @return the indexPeticion
	 */
	public String getIndexPeticion() {
		return indexPeticion;
	}

	/**
	 * @param indexPeticion the indexPeticion to set
	 */
	public void setIndexPeticion(String indexPeticion) {
		this.indexPeticion = indexPeticion;
	}

	/**
	 * @return the validacionesMap
	 */
	public HashMap getValidacionesMap() {
		return validacionesMap;
	}

	/**
	 * @param validacionesMap the validacionesMap to set
	 */
	public void setValidacionesMap(HashMap validacionesMap) {
		this.validacionesMap = validacionesMap;
	}
	
	/**
	 * @return the validacionesMap
	 */
	public Object getValidacionesMap(String key) {
		return validacionesMap.get(key);
	}

	/**
	 * @param validacionesMap the validacionesMap to set
	 */
	public void setValidacionesMap(String key, Object value) {
		this.validacionesMap.put(key, value);
	}

	/**
	 * @return the ultimoPatronOrdenar
	 */
	public String getUltimoPatronOrdenar() {
		return ultimoPatronOrdenar;
	}

	/**
	 * @param ultimoPatronOrdenar the ultimoPatronOrdenar to set
	 */
	public void setUltimoPatronOrdenar(String ultimoPatronOrdenar) {
		this.ultimoPatronOrdenar = ultimoPatronOrdenar;
	}
	
	/**
	 * @return the centroCostosMap
	 */
	public HashMap getCentroCostosMap() {
		return centroCostosMap;
	}

	/**
	 * @param centroCostosMap the centroCostosMap to set
	 */
	public void setCentroCostosMap(HashMap centroCostosMap) {
		this.centroCostosMap = centroCostosMap;
	}
	
	/**
	 * @return the centroCostosMap
	 */
	public Object getCentroCostosMap(String key) {
		return centroCostosMap.get(key);
	}

	/**
	 * @param centroCostosMap the centroCostosMap to set
	 */
	public void setCentroCostosMap(String key, Object value) {
		this.centroCostosMap.put(key, value);
	}

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
	 * @return the solicitudMap
	 */
	public HashMap getSolicitudMap() {
		return solicitudMap;
	}

	/**
	 * @param solicitudMap the solicitudMap to set
	 */
	public void setSolicitudMap(HashMap solicitudMap) {
		this.solicitudMap = solicitudMap;
	}
	
	/**
	 * @return the solicitudMap
	 */
	public Object getSolicitudMap(String key) {
		return solicitudMap.get(key);
	}

	/**
	 * @param solicitudMap the solicitudMap to set
	 */
	public void setSolicitudMap(String key, Object value) {
		this.solicitudMap.put(key, value);
	}

	/**
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the mapaUtilitario_1_Map
	 */
	public HashMap getMapaUtilitario_1_Map() {
		return mapaUtilitario_1_Map;
	}

	/**
	 * @param mapaUtilitario_1_Map the mapaUtilitario_1_Map to set
	 */
	public void setMapaUtilitario_1_Map(HashMap mapaUtilitario_1_Map) {
		this.mapaUtilitario_1_Map = mapaUtilitario_1_Map;
	}
	
	/**
	 * @return the mapaUtilitario_1_Map
	 */
	public Object getMapaUtilitario_1_Map(String key) {
		return mapaUtilitario_1_Map.get(key);
	}

	/**
	 * @param mapaUtilitario_1_Map the mapaUtilitario_1_Map to set
	 */
	public void setMapaUtilitario_1_Map(String key, Object value) {
		this.mapaUtilitario_1_Map.put(key, value);
	}

	/**
	 * @return the mapaUtilitario_2_Map
	 */
	public HashMap getMapaUtilitario_2_Map() {
		return mapaUtilitario_2_Map;
	}

	/**
	 * @param mapaUtilitario_2_Map the mapaUtilitario_2_Map to set
	 */
	public void setMapaUtilitario_2_Map(HashMap mapaUtilitario_2_Map) {
		this.mapaUtilitario_2_Map = mapaUtilitario_2_Map;
	}
	
	/**
	 * @return the mapaUtilitario_2_Map
	 */
	public Object getMapaUtilitario_2_Map(String key) {
		return mapaUtilitario_2_Map.get(key);
	}

	/**
	 * @param mapaUtilitario_2_Map the mapaUtilitario_2_Map to set
	 */
	public void setMapaUtilitario_2_Map(String key, Object value) {
		this.mapaUtilitario_2_Map.put(key, value);
	}

	/**
	 * @return the indicadorUtilitario
	 */
	public String getIndicadorUtilitario() {
		return indicadorUtilitario;
	}

	/**
	 * @param indicadorUtilitario the indicadorUtilitario to set
	 */
	public void setIndicadorUtilitario(String indicadorUtilitario) {
		this.indicadorUtilitario = indicadorUtilitario;
	}

	/**
	 * @return the mapaUtilitarioBusquedas_1_Map
	 */
	public HashMap getMapaUtilitarioBusquedas_1_Map() {
		return mapaUtilitarioBusquedas_1_Map;
	}

	/**
	 * @param mapaUtilitarioBusquedas_1_Map the mapaUtilitarioBusquedas_1_Map to set
	 */
	public void setMapaUtilitarioBusquedas_1_Map(
			HashMap mapaUtilitarioBusquedas_1_Map) {
		this.mapaUtilitarioBusquedas_1_Map = mapaUtilitarioBusquedas_1_Map;
	}

	/**
	 * @return the mapaUtilitario_3_Map
	 */
	public HashMap getMapaUtilitario_3_Map() {
		return mapaUtilitario_3_Map;
	}

	/**
	 * @param mapaUtilitario_3_Map the mapaUtilitario_3_Map to set
	 */
	public void setMapaUtilitario_3_Map(HashMap mapaUtilitario_3_Map) {
		this.mapaUtilitario_3_Map = mapaUtilitario_3_Map;
	}
	
	/**
	 * @return the mapaUtilitario_3_Map
	 */
	public Object getMapaUtilitario_3_Map(String key) {
		return mapaUtilitario_3_Map.get(key);
	}

	/**
	 * @param mapaUtilitario_3_Map the mapaUtilitario_3_Map to set
	 */
	public void setMapaUtilitario_3_Map(String key, Object value) {
		this.mapaUtilitario_3_Map.put(key, value);
	}

	/**
	 * @return the mapaUtilitario_4_Map
	 */
	public HashMap getMapaUtilitario_4_Map() {
		return mapaUtilitario_4_Map;
	}

	/**
	 * @param mapaUtilitario_4_Map the mapaUtilitario_4_Map to set
	 */
	public void setMapaUtilitario_4_Map(HashMap mapaUtilitario_4_Map) {
		this.mapaUtilitario_4_Map = mapaUtilitario_4_Map;
	}
	
	/**
	 * @return the mapaUtilitario_4_Map
	 */
	public Object getMapaUtilitario_4_Map(String key) {
		return mapaUtilitario_4_Map.get(key);
	}

	/**
	 * @param mapaUtilitario_4_Map the mapaUtilitario_4_Map to set
	 */
	public void setMapaUtilitario_4_Map(String key, Object value) {
		this.mapaUtilitario_4_Map.put(key, value);
	}

	/**
	 * @return the arrayListUtilitario_1_Map
	 */
	public ArrayList getArrayListUtilitario_1() {
		return arrayListUtilitario_1;
	}

	/**
	 * @param arrayListUtilitario_1_Map the arrayListUtilitario_1_Map to set
	 */
	public void setArrayListUtilitario_1(ArrayList arrayListUtilitario_1) {
		this.arrayListUtilitario_1 = arrayListUtilitario_1;
	}
	
	/**
	 * @return the arrayListUtilitario_1_Map
	 */
	public int getSizeArrayListUtilitario_1() {
		if(arrayListUtilitario_1 == null)
			return 0;
		else			
			return arrayListUtilitario_1.size();
	}
	
	/**
	 * @return the arrayListUtilitario_2_Map
	 */
	public int getSizeArrayListUtilitario_2() {
		if(arrayListUtilitario_2 == null)
			return 0;
		else			
			return arrayListUtilitario_2.size();
	}
	
	/**
	 * @return the arrayListUtilitario_3_Map
	 */
	public int getSizeArrayListUtilitario_3() {
		if(arrayListUtilitario_3 == null)
			return 0;
		else			
			return arrayListUtilitario_3.size();
	}
		
		
	
	public void setCodigosArticulosInsertados(String valor)
	{
		this.mapaUtilitario_1_Map.put("codigosArticulosInsertados",valor);
	}
	
	public String getCodigosArticulosInsertados()
	{
		return this.mapaUtilitario_1_Map.get("codigosArticulosInsertados").toString();
	}

	
	public void setCriterioBusqueda(String valor)
	{
		this.mapaUtilitario_1_Map.put("criterioBusqueda",valor);
	}
	
	public String getCriterioBusqueda()
	{
		return this.mapaUtilitario_1_Map.get("criterioBusqueda").toString();
	}
	
	public void setEsBusquedaPorNombre(String valor)
	{
		this.mapaUtilitario_1_Map.put("esBusquedaPorNombre",valor);
	}
	
	public String getEsBusquedaPorNombre()
	{
		return this.mapaUtilitario_1_Map.get("esBusquedaPorNombre").toString();
	}


	/**
	 * @return the arrayListUtilitario_2
	 */
	public ArrayList getArrayListUtilitario_2() {
		return arrayListUtilitario_2;
	}


	/**
	 * @param arrayListUtilitario_2 the arrayListUtilitario_2 to set
	 */
	public void setArrayListUtilitario_2(ArrayList arrayListUtilitario_2) {
		this.arrayListUtilitario_2 = arrayListUtilitario_2;
	}


	/**
	 * @return the arrayListUtilitario_3
	 */
	public ArrayList getArrayListUtilitario_3() {
		return arrayListUtilitario_3;
	}


	/**
	 * @param arrayListUtilitario_3 the arrayListUtilitario_3 to set
	 */
	public void setArrayListUtilitario_3(ArrayList arrayListUtilitario_3) {
		this.arrayListUtilitario_3 = arrayListUtilitario_3;
	}
	
	public int getNumeroSolicitud()
	{		
		if(this.solicitudMap.containsKey("numeroSolicitud8_0") && 
				!this.solicitudMap.get("numeroSolicitud8_0").equals(""))
			return Integer.parseInt(this.solicitudMap.get("numeroSolicitud8_0").toString());
		else
			return ConstantesBD.codigoNuncaValido;
	}
	
	public void setNumeroSolicitud(int valor)
	{
		this.solicitudMap.put("numeroSolicitud8_0",valor);		
	}
	
	public int getCodigoPeticion()
	{		
		if(this.solicitudMap.containsKey("codigoPeticion18_0") && 
				!this.solicitudMap.get("codigoPeticion18_0").equals(""))
			return Integer.parseInt(this.solicitudMap.get("codigoPeticion18_0").toString());
		else
			return ConstantesBD.codigoNuncaValido;
	}
	
	public void setCodigoPeticion(int valor)
	{
		this.solicitudMap.put("codigoPeticion18_0",valor);		
	}


	/**
	 * @return the codigoCita
	 */
	public String getCodigoCita() {
		return codigoCita;
	}


	/**
	 * @param codigoCita the codigoCita to set
	 */
	public void setCodigoCita(String codigoCita) {
		this.codigoCita = codigoCita;
	}


	/**
	 * @return the esDummy
	 */
	public boolean isEsDummy() {
		return esDummy;
	}


	/**
	 * @param esDummy the esDummy to set
	 */
	public void setEsDummy(boolean esDummy) {
		this.esDummy = esDummy;
	}


	/**
	 * @return the funcionalidad
	 */
	public String getFuncionalidad() {
		return funcionalidad;
	}


	/**
	 * @param funcionalidad the funcionalidad to set
	 */
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}


	/**
	 * @return the ocultarEncabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}


	/**
	 * @param ocultarEncabezado the ocultarEncabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}


	/**
	 * @return the peticion
	 */
	public String getPeticion() {
		return peticion;
	}


	/**
	 * @param peticion the peticion to set
	 */
	public void setPeticion(String peticion) {
		this.peticion = peticion;
	}


	public HashMap getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}


	public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}


	public HashMap getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}


	public void setDiagnosticosPresuntivos(HashMap diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}


	public String getHiddens() {
		return hiddens;
	}


	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}


	public HashMap getJustificacionMap() {
		return justificacionMap;
	}


	public void setJustificacionMap(HashMap justificacionMap) {
		this.justificacionMap = justificacionMap;
	}


	public HashMap getJustificacionNoPosMap() {
		return justificacionNoPosMap;
	}


	public void setJustificacionNoPosMap(HashMap justificacionNoPosMap) {
		this.justificacionNoPosMap = justificacionNoPosMap;
	}


	public HashMap getMedicamentosNoPosMap() {
		return medicamentosNoPosMap;
	}


	public void setMedicamentosNoPosMap(HashMap medicamentosNoPosMap) {
		this.medicamentosNoPosMap = medicamentosNoPosMap;
	}


	public HashMap getMedicamentosPosMap() {
		return medicamentosPosMap;
	}


	public void setMedicamentosPosMap(HashMap medicamentosPosMap) {
		this.medicamentosPosMap = medicamentosPosMap;
	}


	public int getNumjus() {
		return numjus;
	}


	public void setNumjus(int numjus) {
		this.numjus = numjus;
	}


	public int getSolicitud() {
		return solicitud;
	}


	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}


	public HashMap getSustitutosNoPosMap() {
		return sustitutosNoPosMap;
	}

	public void setSustitutosNoPosMap(HashMap sustitutosNoPosMap) {
		this.sustitutosNoPosMap = sustitutosNoPosMap;
	}
	
	public void setCcSolicitado0(String value)
	{
		this.solicitudMap.put("ccSolicitado0",value);
	}
	
	public void setNumAutorizacion1(String value)
	{
		this.solicitudMap.put("numAutorizacion1",value);
	}
	
	public void setFechaSolicitud2(String value)
	{
		this.solicitudMap.put("fechaSolicitud2",value);
	}
	
	public void setHoraSolicitud3(String value)
	{
		this.solicitudMap.put("horaSolicitud3",value);
	}
	
	public void setEspecialidad4(String value)
	{
		this.solicitudMap.put("especialidad4",value);
	}
	
	public void setUrgente5(String value)
	{
		this.solicitudMap.put("urgente5",value);
	}


	/**
	 * @return the esModificableDummy
	 */
	public boolean isEsModificableDummy() {
		return esModificableDummy;
	}


	/**
	 * @param esModificableDummy the esModificableDummy to set
	 */
	public void setEsModificableDummy(boolean esModificableDummy) {
		this.esModificableDummy = esModificableDummy;
	}


	/**
	 * @return the ocultarMenu
	 */
	public boolean isOcultarMenu() {
		return ocultarMenu;
	}


	/**
	 * @param ocultarMenu the ocultarMenu to set
	 */
	public void setOcultarMenu(boolean ocultarMenu) {
		this.ocultarMenu = ocultarMenu;
	}	

	
	public String getAcronimoDiagFallece()
	{
		if(!this.mapaUtilitario_1_Map.get("diagFallece10").toString().equals(""))
		{
			String [] tmp = this.mapaUtilitario_1_Map.get("diagFallece10").toString().split(ConstantesBD.separadorSplit);
			
			if(tmp.length >0)
			{
				return tmp[0]; 						
			}	
		}
		
		return "";
	}
	
	
	public String getTipoCieDiagFallece()
	{
		if(!this.mapaUtilitario_1_Map.get("diagFallece10").toString().equals(""))
		{
			String [] tmp = this.mapaUtilitario_1_Map.get("diagFallece10").toString().split(ConstantesBD.separadorSplit);
			
			if(tmp.length > 1)
			{
				return tmp[1]; 						
			}	
		}
		
		return "";
	}


	/**
	 * @return the arrayArticuloIncluidoDto
	 */
	public ArrayList<DtoArticuloIncluidoSolProc> getArrayArticuloIncluidoDto() {
		return arrayArticuloIncluidoDto;
	}


	/**
	 * @param arrayArticuloIncluidoDto the arrayArticuloIncluidoDto to set
	 */
	public void setArrayArticuloIncluidoDto(
			ArrayList<DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto) {
		this.arrayArticuloIncluidoDto = arrayArticuloIncluidoDto;
	}


	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}


	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	
	public int getSizeMensajes() {
		return mensajes.size();
	}


	public boolean isSinAutorizacionEntidadsubcontratada() {
		return sinAutorizacionEntidadsubcontratada;
	}


	public void setSinAutorizacionEntidadsubcontratada(
			boolean sinAutorizacionEntidadsubcontratada) {
		this.sinAutorizacionEntidadsubcontratada = sinAutorizacionEntidadsubcontratada;
	}


	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}


	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}


	public boolean isValidacionCapitacion() {
		return validacionCapitacion;
	}


	public void setValidacionCapitacion(boolean validacionCapitacion) {
		this.validacionCapitacion = validacionCapitacion;
	}

}