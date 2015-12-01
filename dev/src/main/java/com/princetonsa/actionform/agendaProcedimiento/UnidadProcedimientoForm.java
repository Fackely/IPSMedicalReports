package com.princetonsa.actionform.agendaProcedimiento;

import java.util.HashMap; 
import util.ConstantesBD;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import org.apache.struts.validator.ValidatorForm;

import util.Utilidades;

public class UnidadProcedimientoForm extends ValidatorForm 
{
	//--Atributos
	
	private Logger logger = Logger.getLogger(UnidadProcedimientoForm.class);
			
	
	//***** Atributos para Unidad de Procedimiento
	/**
	 * Mapa unidadProcedimiento 
	 * */
	private  HashMap unidadProcMap;
	
	/**
	 * Mapa de especialidades 
	 * */	
	private HashMap especialidadesMap;
	
	/**
	 * Mapa de eliminados unidadProcedimiento
	 * */
	private HashMap unidadProcEliminadoMap;
	
	/**
	 * Indica el ultimo patron de ordenamiento
	 * */
	private String ultimoPatron;	
	
	/**
	 * Indica el patron de ordenamiento
	 * */
	private String patronOrdenar;
	
	
	//**** Atributos para Servicios por Unidad de Procedimiento
	
	/**
	 * Mapa servicio por Unidad de Procedimiento
	 * */
	private HashMap servicioUndProcMap;
	
	/**
	 * Mapa servicios eliminados
	 * */
	private HashMap servicioEliminadoUndProcMap;
	
	/**
	 * Indica el ultimo patron de ordenamiento
	 * */
	private String ultimoPatronServUndProc;	
	
	/**
	 * Indica el patron de ordenamiento
	 * */
	private String patronOrdenarServUndProc;
	
	
	//**** Atributos para Detalle del Servio por Unidad de Procediento
	
	/**
	 * Mapa de servicios por Servicios
	 * */
	private HashMap servicioDetalleMap;
	
	/**
	 * Mapa de Condiciones de Toma de Examen por Servicios
	 * */
	private HashMap examenDetalleMap;
	
	/**
	 * Mapa de Articulos por Servicios 
	 * */
	private HashMap articulosDetalleMap;
	
	/**
	 * Indica el ultimo patron de ordenamiento
	 * */
	private String ultimoPatronServDetalle;	
	
	/**
	 * Indica el patron de ordenamiento
	 * */
	private String patronOrdenarServDetalle;
	
	/**
	 * HashMap Almacena los detalles eliminados (servicios, condiciones de toma de examen, articulos)
	 * */
	private HashMap detalleEliminadoMap;
	
	//***** Otros Atributos
	
	/**
	 * Estado que indica quien posee la busqueda de servicios activa
	 * */
	private String whoConsultaServicios;
	
	/**
	 * Estado de la aplicacion 
	 * */
	private String estado; 
	
	/**
	 * Indica el item eliminado del Mapa
	 * */
	private int indexEliminado;
	
	/**
	 * 
	 * */
	private String linkSiguiente;
	
	/**
	 * Index o Codigo de la Unidad de Procedimiento actual
	 * */
	private int indexUndProc;
	
	/**
	 * Index o Codigo del Servicio actual por Unidad de Procedimiento este valor es el codigo mas no el consecutivo
	 * */
	private int indexServUndProc;
	
	/**
	 * Index del Consecutivo del Servicio actual por Unidad de Procedimiento
	 * */
	private int indexServUndProcConsecutivo;
	
	
	/**
	 * Index o Posicion donde se encuentra el Page de Detalle de Servicios por Unidad de Procedimiento
	 * */
	private int indexServicioDetalle;
	
	/**
	 * Variables donde se encuentra los codigos de los servicios ingresados en la forma
	 * */
	private int codigocondicionInsert;
	
	/**
	 * Variables donde se encuentra los codigos de los servicios ingresados en la forma
	 * */
	private String codigosServiciosInsertados;
	
	/**
	 * Variables donde se encuentra los codigos de los servicio ingresados en el detalle
	 * */
	private String codServInsert;	
	
	/**
	 * Variable almacena el valor del codigo servicio padre
	 * */
	private String codigoServicioActual;
	
	/**
	 * Variable almacena la ubicacion desde donde se configura Unidad Procedimiento, Servicios, Detalle Servicios 
	 * */
	private String ubicacionConfig;
	
	//--Fin Atributos
	
	
	
		
	/**
	 * inicializa la forma
	 * */
	public void reset()
	{
		this.unidadProcMap = new HashMap();
		this.unidadProcEliminadoMap = new HashMap();
		this.especialidadesMap = new HashMap();
				
		this.servicioUndProcMap = new HashMap();
		this.servicioEliminadoUndProcMap = new HashMap();
		
		this.servicioDetalleMap = new HashMap();
		this.examenDetalleMap = new HashMap();
		this.articulosDetalleMap = new HashMap();		
		this.detalleEliminadoMap = new HashMap();
		
		
		this.initEspecialidades();
		this.whoConsultaServicios="padre";
		this.indexEliminado=ConstantesBD.codigoNuncaValido;
		this.estado="";
		this.ultimoPatron="";
		this.patronOrdenar="";
		this.linkSiguiente="";		
	}
	
	
	/**
	 * Inicializar las especialidades 
	 * */
	public void initEspecialidades()
	{
		this.especialidadesMap = Utilidades.obtenerEspecialidades();		
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
		errores = super.validate(mapping, request);
		
		if(estado.equals("guardarUnidadProc"))
		{
			int numReg = Integer.parseInt(this.unidadProcMap.get("numRegistros").toString());
			for(int i=0; i<numReg; i++)
			{
				if(this.unidadProcMap.get("descripcion_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Descripcion del registro "+(i+1)));
				}
				
				if(Integer.parseInt(this.unidadProcMap.get("especialidad_"+i).toString())==ConstantesBD.codigoNuncaValido)
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Especialidad del registro "+(i+1)));
				}
			}			
		}
		
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
	 * @return the indexEliminado
	 */
	public int getIndexEliminado() {
		return indexEliminado;
	}



	/**
	 * @param indexEliminado the indexEliminado to set
	 */
	public void setIndexEliminado(int indexEliminado) {
		this.indexEliminado = indexEliminado;
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
	 * @return the unidadProcEliminadoMap
	 */
	public HashMap getUnidadProcEliminadoMap() {
		return unidadProcEliminadoMap;
	}



	/**
	 * @param unidadProcEliminadoMap the unidadProcEliminadoMap to set
	 */
	public void setUnidadProcEliminadoMap(HashMap unidadProcEliminadoMap) {
		this.unidadProcEliminadoMap = unidadProcEliminadoMap;
	}
	
	/**
	 * @param String key
	 * @param Object value
	 * */
	public void setUnidadProcEliminadoMap(String key, Object value)
	{
		this.unidadProcEliminadoMap.put(key, value);
	}
	
	/**
	 * @return Object value
	 * */
	public Object getUnidadProcEliminadoMap(String key)	
	{
		return unidadProcEliminadoMap.get(key); 
	}



	/**
	 * @return the unidadProcMap
	 */
	public HashMap getUnidadProcMap() {
		return unidadProcMap;
	}



	/**
	 * @param unidadProcMap the unidadProcMap to set
	 */
	public void setUnidadProcMap(HashMap unidadProcMap) {
		this.unidadProcMap = unidadProcMap;
	}
	
	
	/**
	 * @param String key
	 * @param Object value
	 * */
	public void setUnidadProcMap(String key, Object value)
	{
		this.unidadProcMap.put(key, value);
	}
	
	
	/**
	 * @return Object value
	 * */
	public Object getUnidadProcMap(String key)	
	{
		return unidadProcMap.get(key); 
	}
	
	/**
	 * @return Object value
	 * */
	public Object getEspecialidadesMap(String key)
	{
		return especialidadesMap.get(key);		
	}
	
	
	
	/**
	 * @param String key
	 * @param Object value
	 * */
	public void setEspecialidadesMap(String key, Object value)
	{
		this.especialidadesMap.put(key, value);		
	}
	
	
	
	/**	 
	 * @return HashMap
	 * */
	public HashMap getEspecialidadesMap()
	{
		return especialidadesMap; 		
	}

	
	
	/**
	 * @param HashMap especialidadesMap 
	 * */
	public void setEspecialidadesMap(HashMap especialidadesMap)
	{
		this.especialidadesMap=especialidadesMap;		
	}


	/**
	 * @return the indexUndProc
	 */
	public int getIndexUndProc() {
		return indexUndProc;
	}


	/**
	 * @param indexUndProc the indexUndProc to set
	 */
	public void setIndexUndProc(int indexUndProc) {
		this.indexUndProc = indexUndProc;
	}


	/**
	 * @return the servicioEliminadoUndProcMap
	 */
	public HashMap getServicioEliminadoUndProcMap() {
		return servicioEliminadoUndProcMap;
	}


	/**
	 * @param servicioEliminadoUndProcMap the servicioEliminadoUndProcMap to set
	 */
	public void setServicioEliminadoUndProcMap(HashMap servicioEliminadoUndProcMap) {
		this.servicioEliminadoUndProcMap = servicioEliminadoUndProcMap;
	}
	
	/**
	 * @param String key
	 * @param Object value
	 * */
	public void setServicioEliminadoUndProcMap(String key, Object value)
	{
		this.servicioEliminadoUndProcMap.put(key, value);		
	}
	
	/**
	 * @param String key
	 * */
	public Object getServicioEliminadoUndProcMap(String key)
	{
		return servicioEliminadoUndProcMap.get(key);
	}	


	/**
	 * @return the servicioUndProcMap
	 */
	public HashMap getServicioUndProcMap() {
		return servicioUndProcMap;
	}


	/**
	 * @param servicioUndProcMap the servicioUndProcMap to set
	 */
	public void setServicioUndProcMap(HashMap servicioUndProcMap) {
		this.servicioUndProcMap = servicioUndProcMap;
	}
	
	
	/**
	 * Retorna el objeto basado en la llave
	 * @param String key
	 * @return the Object servicioUndProcMap
	 */
	public Object getServicioUndProcMap(String key) {
		return servicioUndProcMap.get(key);
	}


	/**
	 * Actualiza el valor de la llave 
	 * @param String key
	 * @param Object value
	 */
	public void setServicioUndProcMap(String key, Object value) {
		this.servicioUndProcMap.put(key, value);
	}


	/**
	 * @return the patronOrdenarServUndProc
	 */
	public String getPatronOrdenarServUndProc() {
		return patronOrdenarServUndProc;
	}


	/**
	 * @param patronOrdenarServUndProc the patronOrdenarServUndProc to set
	 */
	public void setPatronOrdenarServUndProc(String patronOrdenarServUndProc) {
		this.patronOrdenarServUndProc = patronOrdenarServUndProc;
	}


	/**
	 * @return the ultimoPatronServUndProc
	 */
	public String getUltimoPatronServUndProc() {
		return ultimoPatronServUndProc;
	}


	/**
	 * @param ultimoPatronServUndProc the ultimoPatronServUndProc to set
	 */
	public void setUltimoPatronServUndProc(String ultimoPatronServUndProc) {
		this.ultimoPatronServUndProc = ultimoPatronServUndProc;
	}


	/**
	 * @return the codigosServiciosInsertados
	 */
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}


	/**
	 * @param codigosServiciosInsertados the codigoServiciosInsertados to set
	 */
	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}


	/**
	 * @return the patronOrdenarServDetalle
	 */
	public String getPatronOrdenarServDetalle() {
		return patronOrdenarServDetalle;
	}


	/**
	 * @param patronOrdenarServDetalle the patronOrdenarServDetalle to set
	 */
	public void setPatronOrdenarServDetalle(String patronOrdenarServDetalle) {
		this.patronOrdenarServDetalle = patronOrdenarServDetalle;
	}


	/**
	 * @return the servicioDetalle
	 */
	public HashMap getServicioDetalleMap() {
		return servicioDetalleMap;
	}

	/**
	 * Retorna el valor de relacionado a la llave
	 * @param String key
	 * @return Object  
	 * */
	public Object getServicioDetalleMap(String key) {
		return servicioDetalleMap.get(key);
	}
	
	/**
	 * @param servicioDetalle the servicioDetalle to set
	 */
	public void setServicioDetalleMap(HashMap servicioDetalleMap) {
		this.servicioDetalleMap = servicioDetalleMap;
	}
	
	/**
	 * Actualiza el valor de la llave 
	 * @param String key
	 * @param Object value
	 */
	public void setServicioDetalleMap(String key, Object value) {
		this.servicioDetalleMap.put(key, value);
	}

	
	/**
	 * @return the ultimoPatronServDetalle
	 */
	public String getUltimoPatronServDetalle() {
		return ultimoPatronServDetalle;
	}


	/**
	 * @param ultimoPatronServDetalle the ultimoPatronServDetalle to set
	 */
	public void setUltimoPatronServDetalle(String ultimoPatronServDetalle) {
		this.ultimoPatronServDetalle = ultimoPatronServDetalle;
	}


	/**
	 * @return the indexServicioDetalle
	 */
	public int getIndexServicioDetalle() {
		return indexServicioDetalle;
	}


	/**
	 * @param indexServicioDetalle the indexServicioDetalle to set
	 */
	public void setIndexServicioDetalle(int indexServicioDetalle) {
		this.indexServicioDetalle = indexServicioDetalle;
	}


	/**
	 * @return the indexServUndProc
	 */
	public int getIndexServUndProc() {
		return indexServUndProc;
	}


	/**
	 * @param indexServUndProc the indexServUndProc to set
	 */
	public void setIndexServUndProc(int indexServUndProc) {
		this.indexServUndProc = indexServUndProc;
	}


	/**
	 * @return the codServInsert
	 */
	public String getCodServInsert() {
		return codServInsert;
	}


	/**
	 * @param codServInsert the codServInsert to set
	 */
	public void setCodServInsert(String codServInsert) {
		this.codServInsert = codServInsert;
	}
	
	
	/**
	 * @return the examenDetalleMap
	 */
	public HashMap getExamenDetalleMap() {
		return examenDetalleMap;
	}

	/**
	 * Retorna el valor de relacionado a la llave
	 * @param String key
	 * @return Object  
	 * */
	public Object getExamenDetalleMap(String key) {
		return examenDetalleMap.get(key);
	}
	
	/**
	 * @param servicioDetalle the servicioDetalle to set
	 */
	public void setExamenDetalleMap(HashMap ExamenDetalleMap) {
		this.examenDetalleMap = ExamenDetalleMap;
	}
	
	/**
	 * Actualiza el valor de la llave 
	 * @param String key
	 * @param Object value
	 */
	public void setExamenDetalleMap(String key, Object value) {
		this.examenDetalleMap.put(key, value);
	}

	
	/**
	 * @return the articuloDetalleMap
	 */
	public HashMap getArticulosDetalleMap() {
		return articulosDetalleMap;
	}

	/**
	 * Retorna el valor de relacionado a la llave
	 * @param String key
	 * @return Object  
	 * */
	public Object getArticulosDetalleMap(String key) {
		return articulosDetalleMap.get(key);
	}
	
	/**
	 * @param servicioDetalle the servicioDetalle to set
	 */
	public void setArticulosDetalleMap(HashMap ArticulosDetalleMap) {
		this.articulosDetalleMap = ArticulosDetalleMap;
	}
	
	/**
	 * Actualiza el valor de la llave 
	 * @param String key
	 * @param Object value
	 */
	public void setArticulosDetalleMap(String key, Object value) {
		this.articulosDetalleMap.put(key, value);
	}


	/**
	 * @return the whoConsultaServicios
	 */
	public String getWhoConsultaServicios() {
		return whoConsultaServicios;
	}


	/**
	 * @param whoConsultaServicios the whoConsultaServicios to set
	 */
	public void setWhoConsultaServicios(String whoConsultaServicios) {
		this.whoConsultaServicios = whoConsultaServicios;
	}


	/**
	 * @return the codigoServicioActual
	 */
	public String getCodigoServicioActual() {
		return codigoServicioActual;
	}


	/**
	 * @param codigoServicioActual the codigoServicioActual to set
	 */
	public void setCodigoServicioActual(String codigoServicioActual) {
		this.codigoServicioActual = codigoServicioActual;
	}


	/**
	 * @return the codigocondicionInsert
	 */
	public int getCodigocondicionInsert() {
		return codigocondicionInsert;
	}


	/**
	 * @param codigocondicionInsert the codigocondicionInsert to set
	 */
	public void setCodigocondicionInsert(int codigocondicionInsert) {
		this.codigocondicionInsert = codigocondicionInsert;
	}


	/**
	 * @return the setDetalleEliminadoMap
	 */
	public HashMap getDetalleEliminadoMap() {
		return detalleEliminadoMap;
	}


	/**
	 * @param setDetalleEliminadoMap the setDetalleEliminadoMap to set
	 */
	public void setDetalleEliminadoMap(HashMap setDetalleEliminadoMap) {
		this.detalleEliminadoMap = setDetalleEliminadoMap;
	}		
	
	
	/**
	 * @return the Object setDetalleEliminadoMap
	 */
	public Object getDetalleEliminadoMap(String key) {
		return detalleEliminadoMap.get(key);
	}


	/**
	 * @param setDetalleEliminadoMap the setDetalleEliminadoMap to set
	 */
	public void setDetalleEliminadoMap(String key, Object value) {
		this.detalleEliminadoMap.put(key, value) ;
	}
	//--Metodos	


	/**
	 * @return the indexServUndProcConsecutivo
	 */
	public int getIndexServUndProcConsecutivo() {
		return indexServUndProcConsecutivo;
	}


	/**
	 * @param indexServUndProcConsecutivo the indexServUndProcConsecutivo to set
	 */
	public void setIndexServUndProcConsecutivo(int indexServUndProcConsecutivo) {
		this.indexServUndProcConsecutivo = indexServUndProcConsecutivo;
	}


	/**
	 * @return the ubicacionConfig
	 */
	public String getUbicacionConfig() {
		return ubicacionConfig;
	}


	/**
	 * @param ubicacionConfig the ubicacionConfig to set
	 */
	public void setUbicacionConfig(String ubicacionConfig) {
		this.ubicacionConfig = ubicacionConfig;
	}
}