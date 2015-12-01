/*
 * Creado el 17/11/2005
 * Juan David Ramírez López
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;

/**
 * @author Jhony Alexander Duque
 * 
 * CopyRight Princeton S.A.
 * 17/11/2005
 */
@SuppressWarnings("unchecked")
public class HojaQuirurgicaForm extends ValidatorForm
{
	/**
	 * Manejo de versiones
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejador de logs de la clase
	 */
	@SuppressWarnings("unused")
	private transient static Logger logger=Logger.getLogger(HojaQuirurgicaForm.class);
	

	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * ATRIBUTOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	/*---------------------------------------------------------
	 * Atributos para el manejo del pager y el ordenamiento
	 ---------------------------------------------------------*/
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	
	/*---------------------------------------------------------
	 * Fin Atributos para el manejo del pager y el ordenamiento
	 ---------------------------------------------------------*/
	
	/**
	 * encargado de almacenar los estados que le indican
	 * al controlador (Action) que hacer
	 */
	private String estado;
	
	/**
	 * Mapa de las justificaciones NO POS para los servicios
	 */
	private HashMap justificacionesServicios = new HashMap();
	
	
	/**
	 * Almacena el listado de las peticiones sin solicitud asociada
	 * y las peticiones asociadas a una solicitud y que corresponda
	 * a la cuenta cargada y que ademas no se encuentre en estado
	 * atendido ni canselada; cada solicitud o peticion lleva sociada
	 * un ocnjunto de servivios los cuales tambien van dentro del hashmap.
	 */
	private HashMap listadoPeticionesMap = new HashMap();
	
	/**
	 * Almacena los datos faltantes de la solicitud para ser
	 * generada por debajo cuando no existe unas solicitud
	 * asociada a la peticion
	 */
	private HashMap solicitudMap = new HashMap ();
	
	/**
	 * Almacena los datos de la seccion General
	 * de la hoja Quirurgica.
	 */
	private HashMap diagnosticos = new HashMap ();
	
	/**
	 * Almacena la informacion inicial, para luego 
	 * comparar los cambios. 
	 */
	private HashMap diagnosticosClone = new HashMap ();
	

	/**
	 * indica el indice del listado de peticiones y solicitudes
	 * al cual se quiere entrar.
	 */
	private String indexPeticion;
	
	/**
	 * Almacena los centros de costo a ser mostrados en el select
	 * de la generacion de la solicitud automatica.
	 */
	private HashMap centroCostosMap = new HashMap ();
	
	/**
	 * Almacena las especialidades que se muestra en el select
	 * de la generacion de la solicitud automatica
	 */
	private HashMap especialidadesMap = new HashMap ();
	
	/**
	 * Almacena las validaciones del sistema
	 */
	private HashMap validacionesMap = new HashMap ();
	
	/**
	 * inidica si la solicitud ya estas en ingresada en la hoja Qx
	 */
	private boolean existeHojaQx = false;
	
	/**
	 * almacena el numero de autorizacion inicial para compararlo
	 * para identificar si se hay modificacion
	 */
	private String numAutorizacionOld ="";
	
	/**
	 * indices que maneja el hashmap solicitud
	 */
	private String [] indicesSolicitud = HojaQuirurgica.indicesSolicitud;
	
	/**
	 * almacena los diferentes servicios de la solicitud
	 */
	private HashMap servicios = new HashMap ();
	
	private HashMap serviciosOld = new HashMap ();
	/**
	 * indice que servicio es
	 */
	private String indexServicio = ""; 
	/**
	 * indica que profecional es
	 */
	private String indexprofesional = "";
	
	/**
	 * almacena datos de parametros generales.
	 */
	private HashMap paramGenerSecServicos = new HashMap();
	
	/**
	 * datos de la seccion  de la informacion Qx
	 */
	private HashMap secInfoQx = new HashMap ();
	
	private HashMap secInfoQxOld = new HashMap ();
	
	/**
	 * datos de la peticion
	 */	
	private HashMap datosPeticion = new HashMap ();
	/**
	 * datos de la peticion
	 */	
	private HashMap datosPeticionOld = new HashMap ();

	/**
	 * fechas para l ainformacion quirurgica
	 */
	private HashMap fechasCx = new HashMap ();
	
	private HashMap fechasCxOld = new HashMap ();
	
	private ArrayList<HashMap<String, Object>> tiposSalas = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * indicador del tipo de sala
	 */
	private String idTipoSala = "";
	
	/**
	 * indicador de sala
	 */
	private String idSala = "";
	
	
	private ArrayList<HashMap<String, Object>> salas = new ArrayList<HashMap<String,Object>>();	
	
	private ArrayList<HashMap<String, Object>> tipoAnestesia = new ArrayList<HashMap<String,Object>>();	
	/**
	 * Los prefesionales de l ainforamcion quirurgica que estan en la BD
	 */
	private HashMap profesionales = new HashMap (); 
	
	/**
	 * copia de los profecionales cuando empieza
	 */
	private HashMap profesionalesOld = new HashMap ();
	
	private ArrayList<HashMap<String, Object>> profesionalesList = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> medicos = new ArrayList<HashMap<String,Object>>(); 
	
	private ArrayList<HashMap<String, Object>> asocios = new ArrayList<HashMap<String,Object>>();
	
    private ArrayList<String> mensajes = new ArrayList<String>();
    
    private HashMap patologia = new HashMap ();
    
    /**
     * profesionales de la seccion otros profesionales
     */
    private HashMap otrosProf = new HashMap ();
    /***
     * copia de los profesionales cuando entra
     */
    private HashMap otrosProfOld = new HashMap ();
    
    private HashMap notasAcla = new HashMap ();
    
    private HashMap obsGenerales = new HashMap ();
    
    private ArrayList<HashMap<String, Object>> salidaPaciente = new ArrayList<HashMap<String,Object>>();
    
    private HashMap salPac = new HashMap ();
    private HashMap salPacOld = new HashMap (); 
    
    private ArrayList<HashMap<String, Object>> espeMedico = new ArrayList<HashMap<String,Object>>();
	
    //***************************************************************************************************************
    //*************ATRIBUTOS PARA EL MANEJO DE LA FUNCIONALIDAD DUMMY************************************************
    /**
     * encargado de indicar si se oculta o no el cabezote superior
     */
    private boolean  ocultarEncabezado = false;
    /**
     * numero de la peticion 
     */
    private String peticion = "";
    
    private String solicitud="";
	/**
	 * nombre de la funcionalidad que esta llamando la hqx
	 */
	private String funcionalidad ="";
	/**
	 * inidca si es llamado de una funcionalidad dummy o no
	 */
    private boolean esDummy = false;
    	
    /**
     * codigo de la cita, es utilizado cuando es llamado desde la funcionalidad de citas 
     */
    private String codigoCita="";
    
    private boolean esModificable= true;
    
    
    private boolean operacionTrue = false;
    
    /**
     * Parámetro usado en el flujo de consulta externa para saber si se debe mostrar el popup de otros servicios cita
     */
    private boolean deboMostrarOtrosServiciosCita = false;
    
    /**
	 * Dto de articulos incluidos solicitud de procedimiento
	 * */
	private ArrayList <DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto;
	
	/**
	 * HashMap de Justificacion de los articulos Incluidos
	 * */
	private HashMap justificacionMap;
	
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
	 * 
	 * */
	private String hiddens;
	
	/**
	 * 
	 * */
	private String codigoPaciente;
	
	private PersonaBasica paciente= new PersonaBasica();
	
	
	
	// Anexo 179 - Cambio 1.50
	private boolean sinAutorizacionEntidadsubcontratada = false;; 
	/** 
	 * Mensajes de Advertencia
	 */
	private ArrayList<String> advertencia;

	/**
	 * 
	 */
    private HashMap anestesiologos=new HashMap();
    
    
    /**
     * 
     */
    private int anestesiologo=ConstantesBD.codigoNuncaValido;
    
    /**
     * 
     */
    private String fechaInicioAnestesia="";
    
    /**
     * 
     */
    private String horaInicioAnestesia="";
    
    /**
     * 
     */
    private String fechaFinAnestesia="";
    
    /**
     * 
     */
    private String horaFinAnestesia="";
    //****************************************************************************************************************
    
    /**
     * Lista de posibles mensajes después de finalizar la atención
     */
    private ArrayList<String> listaMensajes;
    
    /**
     * Código usuario cargado en sesión
     */
    private String codigoUsuario;
    /**
     * Código Cirujano para paprametrizar el tipo de honorario en caso de que sea de tipo cirujano
     */
    private String codigoParamAsocioCirujano;
   
    /**
     * Indice usado para cargar el index del servicio en la postulación 
     * automática de los diagnosticos
     */
    private String indexPostularDiagnostico;
   
    /**
     * atributo que identifica el elemento al cual se debe hacer focus 
     * al recargar la forma
     */
    private String idElementoFocus;
    
    private int codSolicitud;
    
    private String fechaHy =  UtilidadFecha.getFechaActual();
    
    private boolean validacionCapitacion;
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN ATRIBUTOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS SETTERS AND GETTERS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	public HashMap getListadoPeticionesMap() {
		return listadoPeticionesMap;
	}

	public void setListadoPeticionesMap(HashMap listadoPeticionesMap) {
		this.listadoPeticionesMap = listadoPeticionesMap;
	}
	
	public Object getListadoPeticionesMap(String key) {
		return listadoPeticionesMap.get(key);
	}

	public void setListadoPeticionesMap(String key,Object value) {
		this.listadoPeticionesMap.put(key, value);
	}
	
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	public String getIndexPeticion() {
		return indexPeticion;
	}

	public void setIndexPeticion(String indexPeticion) {
		this.indexPeticion = indexPeticion;
	}
	
	public HashMap getSolicitudMap() {
		return solicitudMap;
	}

	public void setSolicitudMap(HashMap solicitudMap) {
		this.solicitudMap = solicitudMap;
	}
	
	public Object getSolicitudMap(String key) {
		return solicitudMap.get(key);
	}

	public void setSolicitudMap(String key, Object value) {
		this.solicitudMap.put(key, value);
	}
	
	public HashMap getCentroCostosMap() {
		return centroCostosMap;
	}

	public void setCentroCostosMap(HashMap centroCostosMap) {
		this.centroCostosMap = centroCostosMap;
	}

	public HashMap getEspecialidadesMap() {
		return especialidadesMap;
	}

	public void setEspecialidadesMap(HashMap especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}
	
	
	public Object getCentroCostosMap(String key) {
		return centroCostosMap.get(key);
	}

	public void setCentroCostosMap(String key, Object value) {
		this.centroCostosMap.put(key, value);
	}

	public Object getEspecialidadesMap(String key) {
		return especialidadesMap.get(key);
	}

	public void setEspecialidadesMap(String key,Object value) {
		this.especialidadesMap.put(key, value);
	}
	
	public HashMap getDiagnosticos() {
		return diagnosticos;
	}

	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	
	public Object getDiagnosticos(String key) {
		return diagnosticos.get(key);
	}

	public void setDiagnosticos(String key,Object value) {
		this.diagnosticos.put(key, value);
	}

	public HashMap getValidacionesMap() {
		return validacionesMap;
	}

	public void setValidacionesMap(HashMap validacionesMap) {
		this.validacionesMap = validacionesMap;
	}
	
	public Object getValidacionesMap(String key) {
		return validacionesMap.get(key);
	}

	public void setValidacionesMap(String key, Object value) {
		this.validacionesMap.put(key, value);
	}
	
	public HashMap getDiagnosticosClone() {
		return diagnosticosClone;
	}

	public void setDiagnosticosClone(HashMap diagnosticosClone) {
		this.diagnosticosClone = diagnosticosClone;
	}
	
	public Object getDiagnosticosClone(String key) {
		return diagnosticosClone.get(key);
	}

	public void setDiagnosticosClone(String key, Object value) {
		this.diagnosticosClone.put(key, value);
	}
	
	public boolean isExisteHojaQx() {
		return existeHojaQx;
	}

	public void setExisteHojaQx(boolean existeHojaQx) {
		this.existeHojaQx = existeHojaQx;
	}
	
	public String getNumAutorizacionOld() {
		return numAutorizacionOld;
	}

	public void setNumAutorizacionOld(String numAutorizacionOld) {
		this.numAutorizacionOld = numAutorizacionOld;
	}
	
	
	public HashMap getServicios()
	{
		
		return servicios;
	}

	public void setServicios(HashMap servicios) 
	{
		
		this.servicios = servicios;
	}
	
	
	public Object getServicios(String key)
	{
		
		return servicios.get(key);
	}

	public void setServicios(String key,Object value) 
	{
		
		this.servicios.put(key, value);
	}

	public String getIndexServicio() {
		return indexServicio;
	}

	public void setIndexServicio(String indexServicio) {
		this.indexServicio = indexServicio;
	}

	public String getIndexprofesional() {
		return indexprofesional;
	}

	public void setIndexprofesional(String indexprofesional) {
		this.indexprofesional = indexprofesional;
	}
	
	
	
	public HashMap getParamGenerSecServicos() {
		return paramGenerSecServicos;
	}

	public void setParamGenerSecServicos(HashMap paramGenerSecServicos) {
		this.paramGenerSecServicos = paramGenerSecServicos;
	}

	public Object getParamGenerSecServicos(String key) {
		return paramGenerSecServicos.get(key);
	}

	public void setParamGenerSecServicos(String key, Object value) {
		this.paramGenerSecServicos.put(key, value);
	}

	public HashMap getServiciosOld() {
		return serviciosOld;
	}

	public void setServiciosOld(HashMap serviciosOld) {
		this.serviciosOld = serviciosOld;
	}
	
	
	public HashMap getSecInfoQx() {
		return secInfoQx;
	}

	public void setSecInfoQx(HashMap secInfoQx) {
		this.secInfoQx = secInfoQx;
	}

	
	public Object getSecInfoQx(String key) {
		return secInfoQx.get(key);
	}

	public void setSecInfoQx(String key,Object value) {
		this.secInfoQx.put(key, value);
	}
	


	
	public HashMap getDatosPeticion() {
		return datosPeticion;
	}

	public void setDatosPeticion(HashMap datosPeticion) {
		this.datosPeticion = datosPeticion;
	}
	


	
	public Object getDatosPeticion(String key) {
		return datosPeticion.get(key);
	}

	public void setDatosPeticion(String key,Object value) {
		this.datosPeticion.put(key, value);
	}
	
	public HashMap getFechasCx() {
		return fechasCx;
	}

	public void setFechasCx(HashMap fechasCx) {
		this.fechasCx = fechasCx;
	}
	
	public Object getFechasCx(String key) {
		return fechasCx.get(key);
	}

	public void setFechasCx(String key,Object value) {
		this.fechasCx.put(key, value);
	}
	
	public ArrayList<HashMap<String, Object>> getSalas() {
		return salas;
	}

	public void setSalas(ArrayList<HashMap<String, Object>> salas) {
		this.salas = salas;
	}

	public ArrayList<HashMap<String, Object>> getTiposSalas() {
		return tiposSalas;
	}

	public void setTiposSalas(ArrayList<HashMap<String, Object>> tiposSalas) {
		this.tiposSalas = tiposSalas;
	}

	public String getIdTipoSala() {
		return idTipoSala;
	}

	public void setIdTipoSala(String idTipoSala) {
		this.idTipoSala = idTipoSala;
	}

	public ArrayList<HashMap<String, Object>> getTipoAnestesia() {
		return tipoAnestesia;
	}

	public void setTipoAnestesia(ArrayList<HashMap<String, Object>> tipoAnestesia) {
		this.tipoAnestesia = tipoAnestesia;
	}

	public HashMap getProfesionales() {
		return profesionales;
	}

	public void setProfesionales(HashMap profesionales) {
		this.profesionales = profesionales;
	}

	public Object getProfesionales(String key) {
		return profesionales.get(key);
	}

	public void setProfesionales(String key,Object value) {
		this.profesionales.put(key, value);
	}

	
	public ArrayList<HashMap<String, Object>> getProfesionalesList() {
		return profesionalesList;
	}

	public void setProfesionalesList(
			ArrayList<HashMap<String, Object>> profesionalesList) {
		this.profesionalesList = profesionalesList;
	}
	

	public ArrayList<HashMap<String, Object>> getAsocios() {
		return asocios;
	}

	public void setAsocios(ArrayList<HashMap<String, Object>> asocios) {
		this.asocios = asocios;
	}
	

	public ArrayList<String> getMensajes() {
		return mensajes;
	}

	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}

	public int getSizeMensajes() {
		return mensajes.size();
	}

	
	public HashMap getNotasAcla() {
		return notasAcla;
	}

	public void setNotasAcla(HashMap notasAcla) {
		this.notasAcla = notasAcla;
	}

	public Object getNotasAcla(String key) {
		return notasAcla.get(key);
	}

	public void setNotasAcla(String key,Object value ) {
		this.notasAcla.put(key, value);
	}
	
	public HashMap getObsGenerales() {
		return obsGenerales;
	}

	public void setObsGenerales(HashMap obsGenerales) {
		this.obsGenerales = obsGenerales;
	}

	public Object getObsGenerales(String key) {
		return obsGenerales.get(key);
	}

	public void setObsGenerales(String key,Object value) {
		this.obsGenerales.put(key, value);
	}
	
	public HashMap getOtrosProf() {
		return otrosProf;
	}

	public void setOtrosProf(HashMap otrosProf) {
		this.otrosProf = otrosProf;
	}

	public Object getOtrosProf(String key) {
		return otrosProf.get(key);
	}

	public void setOtrosProf(String key,Object value) {
		this.otrosProf.put(key, value);
	}
	
	public HashMap getPatologia() {
		return patologia;
	}

	/**
	 * @return the idSala
	 */
	public String getIdSala() {
		return idSala;
	}

	/**
	 * @param idSala the idSala to set
	 */
	public void setIdSala(String idSala) {
		this.idSala = idSala;
	}

	public void setPatologia(HashMap patologia) {
		this.patologia = patologia;
	}


	public Object getPatologia(String key) {
		return patologia.get(key);
	}

	public void setPatologia(String key,Object value) {
		this.patologia.put(key, value);
	}
	
	
	public ArrayList<HashMap<String, Object>> getMedicos() {
		return medicos;
	}

	public void setMedicos(ArrayList<HashMap<String, Object>> medicos) {
		this.medicos = medicos;
	}

	public HashMap getDatosPeticionOld() {
		return datosPeticionOld;
	}

	public void setDatosPeticionOld(HashMap datosPeticionOld) {
		this.datosPeticionOld = datosPeticionOld;
	}
	
	public Object getDatosPeticionOld(String key) {
		return datosPeticionOld.get(key);
	}

	public void setDatosPeticionOld(String key,Object value) {
		this.datosPeticionOld.put(key, value);
	}
	
	
	public HashMap getOtrosProfOld() {
		return otrosProfOld;
	}

	public void setOtrosProfOld(HashMap otrosProfOld) {
		this.otrosProfOld = otrosProfOld;
	}

	
	public Object getOtrosProfOld(String key) {
		return otrosProfOld.get(key);
	}

	public void setOtrosProfOld(String key,Object value) {
		this.otrosProfOld.put(key, value);
	}
	
	public HashMap getProfesionalesOld() {
		return profesionalesOld;
	}

	public void setProfesionalesOld(HashMap profesionalesOld) {
		this.profesionalesOld = profesionalesOld;
	}
	
	public Object getProfesionalesOld(String key) {
		return profesionalesOld.get(key);
	}

	public void setProfesionalesOld(String key,Object value) {
		this.profesionalesOld.put(key, value);
	}
	

	public ArrayList<HashMap<String, Object>> getSalidaPaciente() {
		return this.salidaPaciente;
	}

	public void setSalidaPaciente(ArrayList<HashMap<String, Object>> salidaPaciente) {
		this.salidaPaciente = salidaPaciente;
	}

	public HashMap getSalPac() {
		return salPac;
	}

	public void setSalPac(HashMap salPac) {
		this.salPac = salPac;
	}


	public Object getSalPac(String key) {
		return salPac.get(key);
	}

	public void setSalPac(String key,Object value) {
		this.salPac.put(key, value);
	}
	
	
	public ArrayList<HashMap<String, Object>> getEspeMedico() {
		return espeMedico;
	}

	public void setEspeMedico(ArrayList<HashMap<String, Object>> espeMedico) {
		this.espeMedico = espeMedico;
	}
	

	public HashMap getSalPacOld() {
		return salPacOld;
	}

	public void setSalPacOld(HashMap salPacOld) {
		this.salPacOld = salPacOld;
	}


	public Object getSalPacOld(String key) {
		return salPacOld.get(key);
	}

	public void setSalPacOld(String key, Object value) {
		this.salPacOld.put(key, value);
	}
	

	public HashMap getFechasCxOld() {
		return fechasCxOld;
	}

	public void setFechasCxOld(HashMap fechasCxOld) {
		this.fechasCxOld = fechasCxOld;
	}
	

	public Object getFechasCxOld(String key) {
		return fechasCxOld.get(key);
	}

	public void setFechasCxOld(String key,Object value) {
		this.fechasCxOld.put(key, value);
	}
	
	public HashMap getSecInfoQxOld() {
		return secInfoQxOld;
	}

	public void setSecInfoQxOld(HashMap secInfoQxOld) {
		this.secInfoQxOld = secInfoQxOld;
	}
	
	public Object getSecInfoQxOld(String key) {
		return secInfoQxOld.get(key);
	}

	public void setSecInfoQxOld(String key,Object value) {
		this.secInfoQxOld.put(key, value);
	}
	

	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}

	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}


	public String getFuncionalidad() {
			return funcionalidad;
		}

		public void setFuncionalidad(String funcionalidad) {
			this.funcionalidad = funcionalidad;
		}

		public String getPeticion() {
			return peticion;
		}

		public void setPeticion(String peticion) {
			this.peticion = peticion;
		}
	
		
	public boolean isEsDummy() {
			return esDummy;
		}

	public void setEsDummy(boolean esDummy) {
			this.esDummy = esDummy;
		}
	

	public String getCodigoCita() {
		return codigoCita;
	}

	public void setCodigoCita(String codigoCita) {
		this.codigoCita = codigoCita;
	}
	
	public boolean isEsModificable() {
		return esModificable;
	}

	public void setEsModificable(boolean esModificable) {
		this.esModificable = esModificable;
	}
 	
	
	public PersonaBasica getPaciente() {
		return paciente;
	}

	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}

	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN DE LOS METODOS SETTERS AND GETTERS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS ADICIONALES DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	public void resetDummy()
	{
		this.codigoPaciente = "";
		this.esModificable=true;
		this.peticion="";
		//this.funcionalidad="";
		this.codigoCita="";
		this.ocultarEncabezado=false;
		this.hiddens="";
		//this.sinAutorizacionEntidadsubcontratada	= false;
		
	}
	
	
	/**
	 * metodo encargado de inicializar todos los
	 * atrubutos de la clase.
	 */
	public void reset_hqx ()
	{
		this.listadoPeticionesMap = new HashMap ();
		this.setListadoPeticionesMap("numRegistros", 0);
		this.solicitudMap = new HashMap ();
		this.diagnosticos = new HashMap ();
		this.setDiagnosticos("numRegistros", 0);
		this.indexPeticion = "";
		this.centroCostosMap = new HashMap ();
		this.setCentroCostosMap("numRegistros", 0);
		this.especialidadesMap = new HashMap ();
		this.setEspecialidadesMap("numRegistros", 0);
		this.diagnosticosClone = new HashMap ();
		this.setDiagnosticosClone("numRegistros", 0);
		this.existeHojaQx =  false;
		this.numAutorizacionOld = "";
		this.servicios = new HashMap ();
		this.setServicios("numRegistros", 0);
		this.indexServicio ="";
		this.indexprofesional ="";
		this.paramGenerSecServicos = new HashMap ();
		this.secInfoQx = new  HashMap ();
		this.setSecInfoQx("numRegistros", 0);

		this.profesionalesList = new  ArrayList<HashMap<String,Object>>();
		this.asocios = new ArrayList<HashMap<String,Object>>();
		this.salas = new ArrayList<HashMap<String,Object>>();
		this.profesionales = new HashMap ();
		this.setProfesionales("numRegistros", 0);
		this.tiposSalas = new ArrayList<HashMap<String,Object>>();
		this.fechasCx = new HashMap ();
		this.datosPeticion = new HashMap ();
		this.salidaPaciente = new ArrayList<HashMap<String,Object>>();
		this.espeMedico = new ArrayList<HashMap<String,Object>>();
		this.salPac = new HashMap ();
		this.salPacOld = new HashMap ();
		this.ocultarEncabezado=false;
		this.peticion="";
		this.funcionalidad="";
		//this.esDummy=false;
		this.codigoCita="";
		this.esModificable=true;
		this.operacionTrue=false;
		this.solicitud="";
		this.codigoPaciente = "";
		this.mensajes = new ArrayList<String>();
		this.paciente=new PersonaBasica();
		
		this.advertencia = new ArrayList<String>();
		//this.sinAutorizacionEntidadsubcontratada	= false;
		this.listaMensajes=new ArrayList<String>();
		this.setCodigoUsuario("");
		this.fechaHy=UtilidadFecha.getFechaActual();
	}


	public void resetPager()
	{
		this.patronOrdenar= "";
		this.ultimoPatron = "";
		this.linkSiguiente = "";
	}
	
	public void reset_secGeneral (boolean formatearOpera)
	{
		this.solicitudMap = new HashMap ();
		this.diagnosticos =  new HashMap ();
		this.setDiagnosticos("numRegistros", 0);
		this.diagnosticosClone = new HashMap ();
		this.setDiagnosticosClone("numRegistros", 0);
		this.numAutorizacionOld="";
		this.paramGenerSecServicos = new HashMap ();
		//this.ocultarEncabezado=false;
		if (formatearOpera)
			this.operacionTrue=false;
	}
	
	public void reset_secServicios (boolean formatearSecDesp)
	{
		this.servicios = new HashMap ();
		this.setServicios("numRegistros", 0);
		this.indexServicio="";
		this.indexprofesional ="";
		if (formatearSecDesp)
		{
			this.paramGenerSecServicos = new HashMap ();
			this.operacionTrue=false;
		}
		this.justificacionesServicios = new HashMap();
		
	}
	
	public void reset_secInfoQx (boolean formatearOpera)
	{
		this.secInfoQx = new HashMap ();
		this.datosPeticion = new HashMap ();
		this.fechasCx = new HashMap ();
		this.tiposSalas = new ArrayList<HashMap<String,Object>>();
		this.salas = new ArrayList<HashMap<String,Object>>();
		this.profesionales = new HashMap ();
		this.setProfesionales("numRegistros", 0);
		this.profesionalesList = new  ArrayList<HashMap<String,Object>>();
		this.asocios = new ArrayList<HashMap<String,Object>>();
		this.indexprofesional = "";
		this.mensajes = new ArrayList<String>();
		this.salidaPaciente = new ArrayList<HashMap<String,Object>>();
		this.salPac = new HashMap ();
		this.salPacOld = new HashMap ();
		if (formatearOpera)
			this.operacionTrue=false;
		this.setCodigoUsuario("");
		this.fechaHy=UtilidadFecha.getFechaActual();

		
	}
	
	public void reset_secPato (boolean formatearOpera)
	{
		this.patologia = new HashMap ();
		if (formatearOpera)
			this.operacionTrue=false;

	}
	
	public void reset_secNotasAcla (boolean formatearOpera)
	{
		this.notasAcla = new HashMap ();
		if (formatearOpera)
			this.operacionTrue=false;

	}
	
	public void reset_secOtrosProf (boolean formatearOpera)
	{
		this.otrosProf = new HashMap ();
		this.profesionalesList = new  ArrayList<HashMap<String,Object>>();
		this.asocios = new ArrayList<HashMap<String,Object>>();
		this.espeMedico = new ArrayList<HashMap<String,Object>>();
		if (formatearOpera)
			this.operacionTrue=false;

		
	}
	
	public void reset_secObsGenerales (boolean formatearOpera)
	{
		this.obsGenerales = new HashMap ();
		if (formatearOpera)
			this.operacionTrue=false;

	}
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN DE LOS METODOS ADICIONALES DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	
	
	
	/**
	 * Método para reiniciar todos los atributos de la clase
	 *
	 */
		
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		//logger.info("\n ENTRE AL VALIDATE diagnosticos --> "+this.getDiagnosticos() +" solicitud "+this.getSolicitudMap() );
/*--------------------------------------------------------------------------------------------------------------------------------------
 * 								VALIDACIONES DE LA NUEVA HOJA QUIRURGICA
 --------------------------------------------------------------------------------------------------------------------------------------*/
/******************************************************************************************************************************************/
			if(estado.equals("guardarSecGeneral"))
			{
				if ((this.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia))
					if ((this.getDiagnosticos("principal")+"").equals(""))
						errores.add("diagnostico", new ActionMessage("errors.required", "El Diagnostico Principal"));	
						
			}
		
/******************************************************************************************************************************************/
/*--------------------------------------------------------------------------------------------------------------------------------------
 * 								FIN VALIDACIONES DE LA NUEVA HOJA QUIRURGICA
 --------------------------------------------------------------------------------------------------------------------------------------*/
/******************************************************************************************************************************************/
		
	
		
		return errores;
	}
	
	/**
	 * @return the justificacionesServicios
	 */
	public HashMap getJustificacionesServicios() {
		return justificacionesServicios;
	}

	/**
	 * @param justificacionesServicios the justificacionesServicios to set
	 */
	public void setJustificacionesServicios(HashMap justificacionesServicios) {
		this.justificacionesServicios = justificacionesServicios;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getJustificacionesServicios(String key) {
		return justificacionesServicios.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setJustificacionesServicios(String key, Object obj) {
		this.justificacionesServicios.put(key, obj);
	}

	/**
	 * @return the deboMostrarOtrosServiciosCita
	 */
	public boolean isDeboMostrarOtrosServiciosCita() {
		return deboMostrarOtrosServiciosCita;
	}

	/**
	 * @param deboMostrarOtrosServiciosCita the deboMostrarOtrosServiciosCita to set
	 */
	public void setDeboMostrarOtrosServiciosCita(
			boolean deboMostrarOtrosServiciosCita) {
		this.deboMostrarOtrosServiciosCita = deboMostrarOtrosServiciosCita;
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

	public String getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
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
	 * @return the justificacionMap
	 */
	public HashMap getJustificacionMap() {
		return justificacionMap;
	}


	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	public void setJustificacionMap(HashMap justificacionMap) {
		this.justificacionMap = justificacionMap;
	}


	/**
	 * @return the hiddens
	 */
	public String getHiddens() {
		if(this.hiddens==null)this.hiddens="";;
		return hiddens;
	}


	/**
	 * @param hiddens the hiddens to set
	 */
	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}
	
	/**
	 * @return the justificacionInclMap
	 */
	public Object getJustificacionMap(String key) {
		return justificacionMap.get(key);
	}


	/**
	 * @param justificacionInclMap the justificacionInclMap to set
	 */
	public void setJustificacionMap(String key, Object value) {
		this.justificacionMap.put(key, value);
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

	/**
	 * @return the medicamentosPosMap
	 */
	public HashMap getMedicamentosPosMap() {
		return medicamentosPosMap;
	}

	/**
	 * @param medicamentosPosMap the medicamentosPosMap to set
	 */
	public void setMedicamentosPosMap(HashMap medicamentosPosMap) {
		this.medicamentosPosMap = medicamentosPosMap;
	}

	/**
	 * @return the medicamentosNoPosMap
	 */
	public HashMap getMedicamentosNoPosMap() {
		return medicamentosNoPosMap;
	}

	/**
	 * @param medicamentosNoPosMap the medicamentosNoPosMap to set
	 */
	public void setMedicamentosNoPosMap(HashMap medicamentosNoPosMap) {
		this.medicamentosNoPosMap = medicamentosNoPosMap;
	}

	/**
	 * @return the sustitutosNoPosMap
	 */
	public HashMap getSustitutosNoPosMap() {
		return sustitutosNoPosMap;
	}

	/**
	 * @param sustitutosNoPosMap the sustitutosNoPosMap to set
	 */
	public void setSustitutosNoPosMap(HashMap sustitutosNoPosMap) {
		this.sustitutosNoPosMap = sustitutosNoPosMap;
	}

	/**
	 * @return the diagnosticosDefinitivos
	 */
	public HashMap getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * @return the diagnosticosPresuntivos
	 */
	public HashMap getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * @param diagnosticosPresuntivos the diagnosticosPresuntivos to set
	 */
	public void setDiagnosticosPresuntivos(HashMap diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}

	/**
	 * @return the advertencia
	 */
	public ArrayList<String> getAdvertencia() {
		return advertencia;
	}

	/**
	 * @param advertencia the advertencia to set
	 */
	public void setAdvertencia(ArrayList<String> advertencia) {
		this.advertencia = advertencia;
	}

	public boolean isSinAutorizacionEntidadsubcontratada() {
		return sinAutorizacionEntidadsubcontratada;
	}

	public void setSinAutorizacionEntidadsubcontratada(
			boolean sinAutorizacionEntidadsubcontratada) {
		this.sinAutorizacionEntidadsubcontratada = sinAutorizacionEntidadsubcontratada;
	}

	public HashMap getAnestesiologos() {
		return anestesiologos;
	}

	public void setAnestesiologos(HashMap anestesiologos) {
		this.anestesiologos = anestesiologos;
	}

	public int getAnestesiologo() {
		return anestesiologo;
	}

	public void setAnestesiologo(int anestesiologo) {
		this.anestesiologo = anestesiologo;
	}

	public String getFechaInicioAnestesia() {
		return fechaInicioAnestesia;
	}

	public void setFechaInicioAnestesia(String fechaInicioAnestesia) {
		this.fechaInicioAnestesia = fechaInicioAnestesia;
	}

	public String getHoraInicioAnestesia() {
		return horaInicioAnestesia;
	}

	public void setHoraInicioAnestesia(String horaInicioAnestesia) {
		this.horaInicioAnestesia = horaInicioAnestesia;
	}

	public String getFechaFinAnestesia() {
		return fechaFinAnestesia;
	}

	public void setFechaFinAnestesia(String fechaFinAnestesia) {
		this.fechaFinAnestesia = fechaFinAnestesia;
	}

	public String getHoraFinAnestesia() {
		return horaFinAnestesia;
	}

	public void setHoraFinAnestesia(String horaFinAnestesia) {
		this.horaFinAnestesia = horaFinAnestesia;
	}

	/**
	 * @return Retorna listaMensajes
	 */
	public ArrayList<String> getListaMensajes() {
		return listaMensajes;
	}

	/**
	 * @param listaMensajes Asigna el atributo listaMensajes
	 */
	public void setListaMensajes(ArrayList<String> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}
	
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	public String getCodigoParamAsocioCirujano() {
		return codigoParamAsocioCirujano;
	}

	public void setCodigoParamAsocioCirujano(String codigoParamAsocioCirujano) {
		this.codigoParamAsocioCirujano = codigoParamAsocioCirujano;
	}

	public void setIndexPostularDiagnostico(String indexPostularDiagnostico) {
		this.indexPostularDiagnostico = indexPostularDiagnostico;
	}

	public String getIndexPostularDiagnostico() {
		return indexPostularDiagnostico;
	}

	public void setIdElementoFocus(String idElementoFocus) {
		this.idElementoFocus = idElementoFocus;
	}

	public String getIdElementoFocus() {
		return idElementoFocus;
	}

	public int getCodSolicitud() {
		return codSolicitud;
	}

	public void setCodSolicitud(int codSolicitud) {
		this.codSolicitud = codSolicitud;
	}

	/**
	 * @return the fechaHy
	 */
	public String getFechaHy() {
		return fechaHy;
	}

	/**
	 * @param fechaHy the fechaHy to set
	 */
	public void setFechaHy(String fechaHy) {
		this.fechaHy = fechaHy;
	}
	
	public boolean isValidacionCapitacion() {
		return validacionCapitacion;
	}

	public void setValidacionCapitacion(boolean validacionCapitacion) {
		this.validacionCapitacion = validacionCapitacion;
	}

	private Integer numeroDeRegistros=0;

	public Integer getNumeroDeRegistros() {
		return numeroDeRegistros;
	}
	
	public void setNumeroDeRegistros(Integer numeroDeRegistros) {
		this.numeroDeRegistros = numeroDeRegistros;
	}
}