package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;

/**
 * Clase para el manejo de la justificación de servicios no pos
 * Date: 2008-04-23
 * @author garias@princetonsa.com
 */
public class JustificacionNoPosServForm extends ValidatorForm 
{
	/**
	 * Estado del formulario
	 */
	private String estado;
	
	/**
	 * Estado anterior del formulario, requerido para saber cuando se esta haciendo una consulta de ingresos 
	 */
	private String estadoAnterior="";

	/**
	 * Indicador de posición en un mapa Cualkiera
	 */
	private int posMap; 
	
	/**
	 * Mapa con la información de las justificaciones
	 */
	private HashMap justificacionesServicios;
	
	/**
	 * Mapa con los ingresos por paciente;
	 */
	private HashMap ingresosPorPacienteMap;
	
	/**
	 * Mapa con los ingresos por paciente;
	 */
	private HashMap centrosAtencionMap;
	
	
	/**
	 * Mapa con las solicitudes de justificaciones No POS;
	 */
	private HashMap solicitudesJustificacionesMap;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String,Object>> centroAtencion;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String,Object>> centrosCosto;
	
	/**
	 * 
	 */
	private ArrayList<HashMap> viaIngresoPaciente;
	
	/**
	 * 
	 */
	private ArrayList<HashMap> convenios;
	
	/**
	 * 
	 */
	private String fechaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	/**
	 * 
	 */
	private HashMap filtros;
	
	/**
	 * 
	 */
	private String codigoCentroAtencion;
	
	/**
	 * 
	 */
	private String codigoViaIngreso;
	
	/**
	 * 
	 */
	private String centroCosto="";
	
	/**
	 * 
	 */
	private HashMap serviciosMap;
	
	/**
	 * 
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * 
	 */
	private String criterioBusqueda;
	
	/**
	 * 
	 */
	private String funcionalidad="";
	
	/**
	 * 
	 */
	private int cargarsi;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String,Object>> pisos;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String,Object>> profesionales;
	
	private int contPagina;
	
	/**
	 * 
	 *
	 */
	public void resetIngresoXRango(){
		this.cargarsi=ConstantesBD.codigoNuncaValido;
		this.justificacionesServicios=new HashMap();
		this.justificacionesServicios.put("numRegistros", "0");
		this.pisos= new ArrayList();
		this.profesionales= new ArrayList();
		this.solicitudesJustificacionesMap=new HashMap();
		this.solicitudesJustificacionesMap.put("numRegistros", "0");
		this.filtros=new HashMap();
		this.centroAtencion=new ArrayList();
		this.centrosCosto=new ArrayList();
		this.centroCosto="";
		this.criterioBusqueda="";
		this.serviciosMap=new HashMap();
		this.serviciosMap.put("numeroFilasMapaServicios", "0");
		this.codigosServiciosInsertados="";
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.fechaInicial="";
		this.fechaFinal="";
		this.contPagina=0;
	}
	
	/**
	 * Reset
	 */
	public void reset( int codigoInstitucion, int centroAtencion)
	{
		this.centrosAtencionMap = Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.ingresosPorPacienteMap=new HashMap();
		this.ingresosPorPacienteMap.put("numRegistros", "0");
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.justificacionesServicios=new HashMap();
		this.justificacionesServicios.put("numRegistros", "0");
		this.solicitudesJustificacionesMap=new HashMap();
		this.solicitudesJustificacionesMap.put("numRegistros", "0");
		this.filtros=new HashMap();
		this.centroAtencion=new ArrayList();
		this.centrosCosto=new ArrayList();
		this.viaIngresoPaciente=new ArrayList();
		this.convenios= new ArrayList();
		this.fechaFinal="";
		this.fechaInicial="";
		this.codigoCentroAtencion="";
		this.codigoViaIngreso="";
		this.centroCosto="";
		this.serviciosMap=new HashMap();
		this.serviciosMap.put("numeroFilasMapaServicios", "0");
		this.codigosServiciosInsertados="";
		this.criterioBusqueda="";
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
        ActionErrors errores = new ActionErrors();
        errores = super.validate(mapping,request);
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
	 * @return the centrosAtencionMap
	 */
	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}

	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}

	/**
	 * @return the ingresosPorPacienteMap
	 */
	public HashMap getIngresosPorPacienteMap() {
		return ingresosPorPacienteMap;
	}

	/**
	 * @param ingresosPorPacienteMap the ingresosPorPacienteMap to set
	 */
	public void setIngresosPorPacienteMap(HashMap ingresosPorPacienteMap) {
		this.ingresosPorPacienteMap = ingresosPorPacienteMap;
	}
	
	/**
	 * @return the ingresosPorPacienteMap
	 */
	public Object getIngresosPorPacienteMap(String llave) {
		return ingresosPorPacienteMap.get(llave);
	}

	/**
	 * @param ingresosPorPacienteMap the ingresosPorPacienteMap to set
	 */
	public void setIngresosPorPacienteMap(String llave, Object obj) {
		this.ingresosPorPacienteMap.put(llave, obj);
	}

	/**
	 * @return the posMap
	 */
	public int getPosMap() {
		return posMap;
	}

	/**
	 * @param posMap the posMap to set
	 */
	public void setPosMap(int posMap) {
		this.posMap = posMap;
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
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the solicitudesJustificaciones
	 */
	public HashMap getSolicitudesJustificacionesMap() {
		return solicitudesJustificacionesMap;
	}

	/**
	 * @param solicitudesJustificaciones the solicitudesJustificaciones to set
	 */
	public void setSolicitudesJustificacionesMap(HashMap solicitudesJustificacionesMap) {
		this.solicitudesJustificacionesMap = solicitudesJustificacionesMap;
	}
	
	/**
	 * @return the solicitudesJustificaciones
	 */
	public Object getSolicitudesJustificacionesMap(String llave) {
		return solicitudesJustificacionesMap.get(llave);
	}

	/**
	 * @param solicitudesJustificaciones the solicitudesJustificaciones to set
	 */
	public void setSolicitudesJustificacionesMap(String llave, Object obj) {
		this.solicitudesJustificacionesMap.put(llave, obj);
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
	 * @return the justificacionesServicios
	 */
	public Object getJustificacionesServicios(String llave) {
		return justificacionesServicios.get(llave);
	}

	/**
	 * @param justificacionesServicios the justificacionesServicios to set
	 */
	public void setJustificacionesServicios(String llave, Object obj) {
		this.justificacionesServicios.put(llave, obj);
	}
	
	/**
	 * @return the centroAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(ArrayList<HashMap<String, Object>> centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centrosCosto
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto() {
		return centrosCosto;
	}
	
	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(ArrayList<HashMap<String, Object>> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap> getConvenios() {
		return convenios;
	}
	
	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the viaIngresoPaciente
	 */
	public ArrayList<HashMap> getViaIngresoPaciente() {
		return viaIngresoPaciente;
	}
	
	/**
	 * @param viaIngresoPaciente the viaIngresoPaciente to set
	 */
	public void setViaIngresoPaciente(ArrayList<HashMap> viaIngresoPaciente) {
		this.viaIngresoPaciente = viaIngresoPaciente;
	}
	
	/**
	 * @return the filtros
	 */
	public HashMap getFiltros() {
		return filtros;
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(HashMap filtros) {
		this.filtros = filtros;
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(String key, Object o) {
		this.filtros.put(key, o);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getFiltros(String llave) {
		return filtros.get(llave);
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the centroCosto
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the serviciosMap
	 */
	public HashMap getServiciosMap() {
		return serviciosMap;
	}

	/**
	 * @param serviciosMap the serviciosMap to set
	 */
	public void setServiciosMap(HashMap serviciosMap) {
		this.serviciosMap = serviciosMap;
	}
	
	/**
	 * @return the serviciosMap
	 */
	public Object getServiciosMap(String llave) {
		return serviciosMap.get(llave);
	}

	/**
	 * @param serviciosMap the serviciosMap to set
	 */
	public void setServiciosMap(String llave, Object obj) {
		this.serviciosMap.put(llave, obj);
	}

	/**
	 * @return the codigosServiciosInsertados
	 */
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	/**
	 * @param codigosServiciosInsertados the codigosServiciosInsertados to set
	 */
	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
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
	 * @return the pisos
	 */
	public ArrayList<HashMap<String, Object>> getPisos() {
		return pisos;
	}



	/**
	 * @param pisos the pisos to set
	 */
	public void setPisos(ArrayList<HashMap<String, Object>> pisos) {
		this.pisos = pisos;
	}



	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}



	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}



	/**
	 * @return the cargarsi
	 */
	public int getCargarsi() {
		return cargarsi;
	}



	/**
	 * @param cargarsi the cargarsi to set
	 */
	public void setCargarsi(int cargarsi) {
		this.cargarsi = cargarsi;
	}

	/**
	 * @return
	 */
	public int getContPagina() {
		return contPagina;
	}

	/**
	 * @param contPagina
	 */
	public void setContPagina(int contPagina) {
		this.contPagina = contPagina;
	}
	
	/**
	 * @return
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	/**
	 * @param estadoAnterior
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}	
}