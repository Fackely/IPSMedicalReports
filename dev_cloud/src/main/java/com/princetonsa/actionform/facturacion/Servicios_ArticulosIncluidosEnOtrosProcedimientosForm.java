package com.princetonsa.actionform.facturacion;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.ResultadoBoolean;
//import util.Utilidades;
//import org.apache.struts.action.ActionMessage;

/**
 * @author Juan Alejandro Cardona L.
 * @date Septiembre de 2008 */

public class Servicios_ArticulosIncluidosEnOtrosProcedimientosForm extends ValidatorForm
{
	/** * VARIABLES GENERALES  */
	private String estado;		// Variable para identificar el estado de la accion 
	
    private ResultadoBoolean mensaje = new ResultadoBoolean(false); // Mensaje que informa sobre la generacion de la aplicacion 
    																// de Servicios_ArticulosIncluidosEnOtrosProcedimientos 

    private boolean esUtilizado;		// Boolean que indica si el registro a modificar esta siendo utilizado
     									// con el fin de validarlo en la vista y solo habilitar los campos correpondientes 

    private HashMap hasMapaServiArtiIncluOtroProc; 	// HashMap para almacenar los resultados arrojados por la consulta de 
     												// Servicios_ArticulosIncluidosEnOtrosProcedimientos
     												// private HashMap servicios_ArticulosIncluidosEnOtrosProcedimientos; 
    
    private HashMap hasMapaServiIncluServiPpal; 	// HashMap para almacenar los resultados arrojados por la consulta de 
     												// los Servicios Incluidos en un Servicio Principal 

    private HashMap hasMapaArtServiPpal = new HashMap<String, Object>(); 	// HashMap para almacenar los resultados arrojados por la consulta de 
     																		// los Articulos Incluidos en un Servicio Principal 
    
    private HashMap hasMapaArtServiInclu; 	// HashMap para almacenar los resultados arrojados por la consulta de 
     										// los Articulos Incluidos en un Servicio Incluido
    
    /**     * VARIABLES PARA PAGER     */
    private int currentPageNumber;	// Atributo para el manejo de la paginacion con memoria
    private int offset;				// Para controlar la página actual del pager.
    private String linkSiguiente;	// Para controlar el link siguiente del pager 
 
    /**     * VARIABLES PARA EL ORDENAMIENTO     */
    private String patronOrdenar;	// String para ordenar por un nuevo patron
    private String ultimoPatron;	// String que almacena el ordenamiento del ultimo patron ordenado 

    private int posicion;			    // Posición del Servicio-Articulo Incluido seleccionado en el listado principal para manejar el detalle
    private int posServicioSeleccionado; // Posición del Servicio Incluido Seleccionado en el segundo listado para saber a que servicio incluido se le ingresan articulos incluidos
    
    /**     * DATOS PARA SERVICIOS PPALES     */
    private String codigoSevicioTarifario;		// Código del servicio según el tipo tarifario
    private String codigoServicio;				// Código del servicio
    private String descripcionServicio;			// Descripción del Servicio
    private String activoServicioPrincipal;     // Activo Servicio Incluidos en otros Procedimientos
    private String especialidadServicio;
    private String esPosServicio;
        
    /**     * DATOS PARA ARTICULOS INCLUIDOS EN SERVICIOS PPALES     */
    private String codartincluppal;				// Código del ARTICULO
    private String descripartservppal;			// Descripción del Articulo del Servicio
    private String unidadArtPpal;
    private String esPosArtPpal;
    
    /**     * CODE GIOVANI     */
	private String codigosArticulos;  		// Cadena con codigos de articulos separado por comas
	private String seccion;					// Codigo de la seccion
	private HashMap seccionesElegidasMap;	// Mapa de las secciones elegidas por el usuario
	private int subseccion;					// Codigo de la subseccion
	private String indexMap;				// indica una posicion en el mapa de articulos
	private String indexMapInclu;			// indica una posicion en el mapa de servicios incluidos
	
	/**	 * Variables Servicios Incluidos	 */
	private String codServInclu;			//codigo del servicio ke se selecciono
	private String descripServInclu;		//descripcion del servicio incluido
	private String especialidadServInclu;	//especialidad del servicio
	private String esPosServInclu;			//si es pos o no
	
    /**     * DATOS PARA ARTICULOS INCLUIDOS EN SERVICIOS INCLUIDOS     */
    private String codartservinclu;				// Código del ARTICULO
    private String descripartservinclu;			// Descripción del Articulo del Servicio
    private String unidadArtServinclu;
    private String esPosArtServinclu;
    
    /**     * Boolean para indicar si se llama de nuevo o si se llama de modificación     */
    private String esModificado;
    
    private boolean exitoso;

	private String esPpal;					// bandera para saber al momento de agregar a donde apunto la buskeda generica de serv. 
	private String esArtPpal;				// bandera para saber al momento de agregar a donde apunto la buskeda generica de serv. 
    
    /**     * para mostrar las farmacias en el agregar     * */
    private HashMap farmaciasMap;

    
    private HashMap hasMapaCodigosTmp;		//mapa temporal para almacenar los codigos utilizados en el sistema
    
    
    //Agregado por anexo 951
    private String esServicioOdontologico;  //Variable para determinar si el servicio cargado es de atencion odontologica y hacer las correspondientes validaciones
    
    
    
	/**	 * Método reset de la forma. Inicializa las variables	 */
	public void reset() {

        this.hasMapaCodigosTmp = new HashMap<String, Object>();
		this.hasMapaCodigosTmp.put("numRegistros", "0");
//		this.hasMapaCodigosTmp.put("codigosUtilizados", "");
		
		
		this.codServInclu = "";
		this.descripServInclu = "";
		this.especialidadServInclu = "";
		this.esPosServInclu = "";
		
		this.esPpal = "";
		this.esArtPpal = "";
		
	    this.codartservinclu="";
	    this.descripartservinclu="";
	    this.unidadArtServinclu="";
	    this.esPosArtServinclu="";
		
		this.codigosArticulos = "";
		this.seccion = "";
		this.seccionesElegidasMap = new HashMap();
		this.seccionesElegidasMap.put("numRegistros", "0");
		this.subseccion = ConstantesBD.codigoNuncaValido;
		this.indexMap ="";
		this.indexMapInclu ="";
		
		this.codigoServicio = "";
		this.codigoSevicioTarifario = "";
		this.descripcionServicio = "";
		this.activoServicioPrincipal = ConstantesBD.acronimoSi;
		this.especialidadServicio = "";
		this.esPosServicio = "";
		
	    this.codartincluppal = "";
	    this.descripartservppal = "";
	    this.unidadArtPpal = "";
	    this.esPosArtPpal = "";
		
		this.esUtilizado = true;
		this.posicion = ConstantesBD.codigoNuncaValido;
		this.posServicioSeleccionado = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.ultimoPatron = "";
        this.currentPageNumber = 1;  //0
        this.linkSiguiente = "";
        this.offset = 0;

        this.esModificado = "";
        
        this.hasMapaServiArtiIncluOtroProc = new HashMap<String, Object>();
		this.hasMapaServiArtiIncluOtroProc.put("numRegistros", "0");
		this.hasMapaServiArtiIncluOtroProc.put("codServiUtilizados", "");

		this.hasMapaArtServiPpal = new HashMap<String, Object>();
		this.hasMapaArtServiPpal.put("numRegistros", "0");
		this.hasMapaArtServiPpal.put("codigosArticulos", "");
		
		this.hasMapaServiIncluServiPpal = new HashMap<String, Object>();
		this.hasMapaServiIncluServiPpal.put("numRegistros", "0");
		this.hasMapaServiIncluServiPpal.put("codigosServicios", "");

		this.hasMapaArtServiInclu = new HashMap<String, Object>();
		this.hasMapaArtServiInclu.put("numRegistros", "0");
		this.hasMapaArtServiInclu.put("codigosArticulos", "");		//codigos de los articulos incluidos de los servinclu ya utilizados 
		
		this.exitoso=false;
		this.farmaciasMap =new HashMap();
		
		this.esServicioOdontologico="";
	}

	/**	 * Reset al mapa en donde se almacenan temporalmente los codigos utilizados  */
	public void resetCodigosTmp() {
		this.hasMapaCodigosTmp = new HashMap<String, Object>();
		this.hasMapaCodigosTmp.put("numRegistros", "0");
		this.hasMapaCodigosTmp.put("codigosServicios", "");
	}
	
	/**
	 * Método para hacer reset a las variables del servicio
	 */
	public void resetServiciosIncluidos()
	{
		this.codServInclu = "";
		this.descripServInclu = "";
		this.especialidadServInclu = "";
		this.esPosServInclu = "";
	}
	
	/**
	 * Método para hacer reset a las variables del articulo de un serv ppal
	 */
	public void resetArticulosIncluidos()
	{
		this.codartincluppal = "";
		this.descripartservppal = "";
		this.unidadArtPpal = "";
		this.esPosArtPpal = "";
	}

	/** Metodo para hacer reset a las variables del articulo de un servinclu 
	 *	 */
	public void resetArtincluServinclu()
	{
	    this.codartservinclu = "";
	    this.descripartservinclu = "";
	    this.unidadArtServinclu = "";
	    this.esPosArtServinclu = "";
	}
	
	
	/**	 *	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		return errores;
	}

	/**	 * @return the estado	 */
	public String getEstado() {	return estado;	}
	/**	 * @param estado the estado to set	 */
	public void setEstado(String estado) {		this.estado = estado;	}


	/**	 * @return the mensaje	 */
	public ResultadoBoolean getMensaje() {		return mensaje;	}
	/**	 * @param mensaje the mensaje to set	 */
	public void setMensaje(ResultadoBoolean mensaje) {		this.mensaje = mensaje;	}


	/**	 * @return the esUtilizado	 */
	public boolean isEsUtilizado() {		return esUtilizado;	}
	/**	 * @param esUtilizado the esUtilizado to set	 */
	public void setEsUtilizado(boolean esUtilizado) {		this.esUtilizado = esUtilizado;	}


	/**	 * @return the offset	 */
	public int getOffset() {		return offset;	}
	/**	 * @param offset the offset to set	 */
	public void setOffset(int offset) {		this.offset = offset;	}


	/**	 * @return the currentPageNumber	 */
	public int getCurrentPageNumber() {		return currentPageNumber;	}
	/**	 * @param currentPageNumber the currentPageNumber to set	 */
	public void setCurrentPageNumber(int currentPageNumber) {	this.currentPageNumber = currentPageNumber; }


	/**	 * @return the linkSiguiente	 */
	public String getLinkSiguiente() {	return linkSiguiente;	}
	/**	 * @param linkSiguiente the linkSiguiente to set	 */
	public void setLinkSiguiente(String linkSiguiente) { this.linkSiguiente = linkSiguiente; }


	/**	 * @return the patronOrdenar	 */
	public String getPatronOrdenar() {		return patronOrdenar;	}
	/**	 * @param patronOrdenar the patronOrdenar to set */
	public void setPatronOrdenar(String patronOrdenar) { this.patronOrdenar = patronOrdenar; }


	/**  * @return the ultimoPatron	 */
	public String getUltimoPatron() {	return ultimoPatron; }
	/**  * @param ultimoPatron the ultimoPatron to set  */
	public void setUltimoPatron(String ultimoPatron) {	this.ultimoPatron = ultimoPatron; }

	
	/**	 * @return the posicion	 */
	public int getPosicion() {	return posicion; }
	/**	 * @param posicion the posicion to set  */
	public void setPosicion(int posicion) {	this.posicion = posicion; }
	
	
	//---------------Inicio getHasMapaServiArtiIncluOtroProc-------------
	/**	 * @return the hasMapaServiArtiIncluOtroProc	 */
	public HashMap getHasMapaServiArtiIncluOtroProc() {		return hasMapaServiArtiIncluOtroProc;	}
	/**	 * @param hasMapaServiArtiIncluOtroProc the hasMapaServiArtiIncluOtroProc to set	 */
	public void setHasMapaServiArtiIncluOtroProc(HashMap hasMapaServiArtiIncluOtroProc) {	this.hasMapaServiArtiIncluOtroProc = hasMapaServiArtiIncluOtroProc; }
	/**	 * @param key	 * @return */	 
	public Object getHasMapaServiArtiIncluOtroProc(String key) {      return hasMapaServiArtiIncluOtroProc.get(key);   }
	/** * @param key	 * @param value */
	public void setHasMapaServiArtiIncluOtroProc(String key,Object value) { this.hasMapaServiArtiIncluOtroProc.put(key, value);  }
	//---------------Fin getHasMapaServiArtiIncluOtroProc-------------

	//---------------Inicio getHasMapaServiIncluServiPpal -------------
	/**	 * @return the hasMapaServiIncluServiPpal	 */
	public HashMap getHasMapaServiIncluServiPpal() {	return hasMapaServiIncluServiPpal;	}
	/**	 * @param hasMapaServiIncluServiPpal the hasMapaServiIncluServiPpal to set	 */
	public void setHasMapaServiIncluServiPpal(HashMap hasMapaServiIncluServiPpal) {	this.hasMapaServiIncluServiPpal = hasMapaServiIncluServiPpal; }
	/**	 * @param key	 * @return */	 
	public Object getHasMapaServiIncluServiPpal(String key) {      return hasMapaServiIncluServiPpal.get(key);   }
	/** * @param key	 * @param value */
	public void setHasMapaServiIncluServiPpal(String key,Object value) { this.hasMapaServiIncluServiPpal.put(key, value);  }
	//---------------Fin getHasMapaServiArtiIncluOtroProc-------------
	
	//---------------Inicio getHasMapaArtServiPpal-------------
	/**	 * @return the hasMapaArtServiPpal	 */
	public HashMap getHasMapaArtServiPpal() {	return hasMapaArtServiPpal;	}
	/**	 * @param hasMapaArtServiPpal the hasMapaArtServiPpal to set	 */
	public void setHasMapaArtServiPpal(HashMap hasMapaArtServiPpal) {	this.hasMapaArtServiPpal = hasMapaArtServiPpal;	}
	/**	 * @param key	 * @return */	 
	public Object getHasMapaArtServiPpal(String key) {      return hasMapaArtServiPpal.get(key);   }
	/** * @param key	 * @param value */
	public void setHasMapaArtServiPpal(String key,Object value) { this.hasMapaArtServiPpal.put(key, value);  }
	//---------------Fin getHasMapaServiArtiIncluOtroProc-------------

	//---------------Inicio getHasMapaArtServiInclu-------------
	/**	 * @return the hasMapaArtServiInclu */
	public HashMap getHasMapaArtServiInclu() {	return hasMapaArtServiInclu;	}
	/**	 * @param hasMapaArtServiInclu the hasMapaArtServiInclu to set	 */
	public void setHasMapaArtServiInclu(HashMap hasMapaArtServiInclu) {	this.hasMapaArtServiInclu = hasMapaArtServiInclu;	}
	/**	 * @param key	 * @return */	 
	public Object getHasMapaArtServiInclu(String key) {      return hasMapaArtServiInclu.get(key);   }
	/** * @param key	 * @param value */
	public void setHasMapaArtServiInclu(String key,Object value) { this.hasMapaArtServiInclu.put(key, value);  }
	//---------------Fin getHasMapaArtiserviInclu-------------

	/**	 * @return the codigoSevicioTarifario	 */
	public String getCodigoSevicioTarifario() {		return codigoSevicioTarifario;	}
	/**	 * @param codigoSevicioTarifario the codigoSevicioTarifario to set	 */
	public void setCodigoSevicioTarifario(String codigoSevicioTarifario) {	this.codigoSevicioTarifario = codigoSevicioTarifario;	}

	/**	 * @return the codigoServicio	 */
	public String getCodigoServicio() {		return codigoServicio;	}
	/**	 * @param codigoServicio the codigoServicio to set	 */
	public void setCodigoServicio(String codigoServicio) {		this.codigoServicio = codigoServicio;	}

	/**	 * @return the descripcionServicio	 */
	public String getDescripcionServicio() {		return descripcionServicio;	}
	/**	 * @param descripcionServicio the descripcionServicio to set	 */
	public void setDescripcionServicio(String descripcionServicio) {	this.descripcionServicio = descripcionServicio;	}

	/**	 * @return the codartincluppal	 */
	public String getCodartincluppal() {		return codartincluppal;	}
	/**	 * @param codartincluppal the codartincluppal to set	 */
	public void setCodartincluppal(String codartincluppal) {	this.codartincluppal = codartincluppal;	}

	/**	 * @return the descripartservppal	 */
	public String getDescripartservppal() {		return descripartservppal;	}
	/**	 * @param descripartservppal the descripartservppal to set	 */
	public void setDescripartservppal(String descripartservppal) {	this.descripartservppal = descripartservppal;	}

	/**	 * @return the codigosArticulos	 */
	public String getCodigosArticulos() {	return codigosArticulos;	}
	/**	 * @param codigosArticulos the codigosArticulos to set	 */
	public void setCodigosArticulos(String codigosArticulos) {	this.codigosArticulos = codigosArticulos;	}

	/**	 * @return the seccion	 */
	public String getSeccion() {		return seccion;	}
	/**	 * @param seccion the seccion to set	 */
	public void setSeccion(String seccion) {		this.seccion = seccion;	}

	/**	 * @return the seccionesElegidasMap	 */	
	public HashMap getSeccionesElegidasMap() {		return seccionesElegidasMap;	}
	/**	 * @param seccionesElegidasMap the seccionesElegidasMap to set	 */
	public void setSeccionesElegidasMap(HashMap seccionesElegidasMap) {		this.seccionesElegidasMap = seccionesElegidasMap;	}

	/**	 * @return the indexMap	 */
	public String getIndexMap() {		return indexMap;	}
	/**	 * @param indexMap the indexMap to set	 */
	public void setIndexMap(String indexMap) {		this.indexMap = indexMap;	}

	/**	 * @return the subseccion	 */
	public int getSubseccion() {	return subseccion;	}
	/**	 * @param subseccion the subseccion to set	 */
	public void setSubseccion(int subseccion) {		this.subseccion = subseccion;	}
	
	/**	 * @return the activoServicioPrincipal	 */
	public String getActivoServicioPrincipal() {		return activoServicioPrincipal;	}
	/**	 * @param activoServicioPrincipal the activoServicioPrincipal to set	 */
	public void setActivoServicioPrincipal(String activoServicioPrincipal) {	this.activoServicioPrincipal = activoServicioPrincipal;	}

	/**	 * @return the esPpal	 */
	public String getEsPpal() {	return esPpal;	}
	/**	 * @param esPpal the esPpal to set	 */
	public void setEsPpal(String esPpal) {	this.esPpal = esPpal;	}

	/**	 * @return the codServInclu	 */
	public String getCodServInclu() {		return codServInclu;	}
	/**	 * @param codServInclu the codServInclu to set	 */
	public void setCodServInclu(String codServInclu) {	this.codServInclu = codServInclu;	}

	/**	 * @return the descripServInclu	 */
	public String getDescripServInclu() {	return descripServInclu;	}
	/**	 * @param descripServInclu the descripServInclu to set	 */
	public void setDescripServInclu(String descripServInclu) {	this.descripServInclu = descripServInclu;	}

	/**	 * @return the indexMapInclu	 */
	public String getIndexMapInclu() {	return indexMapInclu;	}
	/**	 * @param indexMapInclu the indexMapInclu to set	 */
	public void setIndexMapInclu(String indexMapInclu) {	this.indexMapInclu = indexMapInclu;	}

	
	/**	 * @return the especialidadServInclu	 */
	public String getEspecialidadServInclu() {	return especialidadServInclu;}
	/**	 * @param especialidadServInclu the especialidadServInclu to set */
	public void setEspecialidadServInclu(String especialidadServInclu) {this.especialidadServInclu = especialidadServInclu;	}

	
	/** * @return the esPosServInclu */
	public String getEsPosServInclu() {	return esPosServInclu;	}
	/** * @param esPosServInclu the esPosServInclu to set */
	public void setEsPosServInclu(String esPosServInclu) {	this.esPosServInclu = esPosServInclu; }
	
	
	/**	 * @return the unidadArtPpal	 */
	public String getUnidadArtPpal() {	return unidadArtPpal;}
	/**	 * @param unidadArtPpal the unidadArtPpal to set	 */
	public void setUnidadArtPpal(String unidadArtPpal) {	this.unidadArtPpal = unidadArtPpal;	}

	
	/**	 * @return the esPosArtPpal	 */
	public String getEsPosArtPpal() {	return esPosArtPpal; }
	/** * @param esPosArtPpal the esPosArtPpal to set	 */
	public void setEsPosArtPpal(String esPosArtPpal) {	this.esPosArtPpal = esPosArtPpal; }

	
	/**	 * @return the especialidadServicio	 */
	public String getEspecialidadServicio() {	return especialidadServicio;	}
	/**	 * @param especialidadServicio the especialidadServicio to set	 */
	public void setEspecialidadServicio(String especialidadServicio) {	this.especialidadServicio = especialidadServicio;	}

	/**	 * @return the esPosServicio	 */
	public String getEsPosServicio() {	return esPosServicio;	}
	/**	 * @param esPosServicio the esPosServicio to set	 */
	public void setEsPosServicio(String esPosServicio) {	this.esPosServicio = esPosServicio;	}

	
	/**	 * @return the codartservinclu	 */
	public String getCodartservinclu() {	return codartservinclu;	}
	/**	 * @param codartservinclu the codartservinclu to set	 */
	public void setCodartservinclu(String codartservinclu) {	this.codartservinclu = codartservinclu;	}

	
	/**	 * @return the descripartservinclu	 */
	public String getDescripartservinclu() {	return descripartservinclu;	}
	/**	 * @param descripartservinclu the descripartservinclu to set	 */
	public void setDescripartservinclu(String descripartservinclu) {	this.descripartservinclu = descripartservinclu;	}

	
	/**	 * @return the unidadArtServinclu	 */
	public String getUnidadArtServinclu() {	return unidadArtServinclu;	}
	/**	 * @param unidadArtServinclu the unidadArtServinclu to set	 */
	public void setUnidadArtServinclu(String unidadArtServinclu) {	this.unidadArtServinclu = unidadArtServinclu;	}

	/**	 * @return the esPosArtServinclu	 */
	public String getEsPosArtServinclu() {	return esPosArtServinclu;	}
	/**	 * @param esPosArtServinclu the esPosArtServinclu to set	 */
	public void setEsPosArtServinclu(String esPosArtServinclu) {	this.esPosArtServinclu = esPosArtServinclu;	}

	
	/**	 * @return the posServicioSeleccionado	 */
	public int getPosServicioSeleccionado() {	return posServicioSeleccionado;	}
	/**	 * @param posServicioSeleccionado the posServicioSeleccionado to set	 */
	public void setPosServicioSeleccionado(int posServicioSeleccionado) {	this.posServicioSeleccionado = posServicioSeleccionado;	}

	
	/** * @return the esModificado	 */
	public String getEsModificado() {	return esModificado;	}
	/**	 * @param esModificado the esModificado to set	 */
	public void setEsModificado(String esModificado) {	this.esModificado = esModificado;	}

	
	public boolean isExitoso() {	return exitoso;	}
	public void setExitoso(boolean exitoso) {	this.exitoso = exitoso;	}

	
	/**	 * @return the farmaciasMap	 */
	public HashMap getFarmaciasMap() {	return farmaciasMap;	}
	/**	 * @param farmaciasMap the farmaciasMap to set	 */
	public void setFarmaciasMap(HashMap farmaciasMap) {	this.farmaciasMap = farmaciasMap;	}

	
	/**	 * @return the esArtPpal	 */
	public String getEsArtPpal() {	return esArtPpal;	}
	/**	 * @param esArtPpal the esArtPpal to set	 */
	public void setEsArtPpal(String esArtPpal) {	this.esArtPpal = esArtPpal; }
	
	
	//---------------Inicio getHasMapaCodigosTmp-------------
	/**	 * @return the hasMapaCodigosTmp	 */
	public HashMap getHasMapaCodigosTmp() {		return hasMapaCodigosTmp;	}
	/**	 * @param hasMapaCodigosTmp the hasMapaCodigosTmp to set	 */
	public void setHasMapaCodigosTmp(HashMap hasMapaCodigosTmp) {	this.hasMapaCodigosTmp = hasMapaCodigosTmp; }
	/**	 * @param key	 * @return */	 
	public Object getHasMapaCodigosTmp(String key) {      return hasMapaCodigosTmp.get(key);   }
	/** * @param key	 * @param value */
	public void setHasMapaCodigosTmp(String key,Object value) { this.hasMapaCodigosTmp.put(key, value);  }
	//---------------Fin getHasMapaCodigosTmp-------------

	public String getEsServicioOdontologico() {
		return esServicioOdontologico;
	}

	public void setEsServicioOdontologico(String esServicioOdontologico) {
		this.esServicioOdontologico = esServicioOdontologico;
	}
}