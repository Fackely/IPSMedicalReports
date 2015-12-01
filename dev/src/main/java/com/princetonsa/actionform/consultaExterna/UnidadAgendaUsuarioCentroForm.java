package com.princetonsa.actionform.consultaExterna;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.Utilidades;

@SuppressWarnings("serial")
public class UnidadAgendaUsuarioCentroForm extends ValidatorForm
{
	/**
	 * estado del formulario
	 */
	private String estado;
	
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
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Mapa para la Consulta de Registros Unidad de Agenda
	 */
	private HashMap unidadAgendaMap;
	
	/**
	 * Mapa para la Consulta de las Actividades x Unidad
	 */
	private HashMap unidadAgenda2Map;
	
	/**
	 * Index para controlar las Posiciones de los Registros en el Mapa
	 */
	private String indexMap;
	
	/**
	 * Codigo del centro de atencion
	 */
	private String centroAtencion;
	
	/**
	 * Codigo del Centro de Atencion para Busqueda Principal
	 */
	private String centroAtencionP;
	
	/**
	 * 
	 * */
	private String codigoUsuarioP;
	
	/**
	 * Mapa de los centros de atencion
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * Codigo de la Unidad
	 */
	private String unidad;
	
	/**
	 * Tipo de atencion de la unidad
	 */
	private String tipoAtencion;
	
	/**
	 * Mapa de las Unidades
	 */
	private HashMap unidadesMap;
	
	/**
	 * Login del Usuario Autorizado
	 */
	private String usuario;
	
	/**
	 * Mapa Usuarios
	 */
	private HashMap usuariosMap;
	
	/**
	 * Mapa Actividades;
	 */
	private HashMap actividadesMap;
	
	/**
	 * Actividad
	 */
	private String [] actividad;
	
	/**
	 * HashMap con los nombres generados de las actividades adicionadas 
	 */
	private HashMap actividadesGenerados; 
	
	/**
	 * Número de documentos adjuntos
	 */
	private int numActividades = 1;
	
	/**
	 * codigo de resopuesta
	 */
	private String codigoRespuesta;
	
	
	
	
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
        errores=super.validate(mapping,request);
        int nume=0;
        if(this.estado.equals("insertarUnidad"))
        {
        	if(this.unidad.equals("-1"))
        		errores.add("Campo Unidad de Agenda requerido",new ActionMessage("errors.required", "La Unidad de Agenda "));
        	
        	if(this.usuario.equals("-1"))
        		errores.add("Campo Usuario Autorizado requerido",new ActionMessage("errors.required", "El Usuario Autorizado "));
        	
        	if(Integer.parseInt(this.getActividadesGenerados("numRegistros").toString())>0)
        	{
        		for(int i=1;i<=Integer.parseInt(this.getActividadesGenerados("numRegistros").toString());i++){
        			if(this.getActividadesGenerados("checkac_"+i).equals("1"))
        				nume=1;
        		}
        		if(nume==0)
        			errores.add("Campo Actividad Autorizada requerido",new ActionMessage("errors.required", "La Actividad Autorizada "));
        	}
        	else
            {
            	errores.add("Campo Actividad Autorizada requerido",new ActionMessage("errors.required", "La Actividad Autorizada "));
            }
        	if(!errores.isEmpty())
        		this.setEstado("nuevoUnidad");
        }
        
        if(this.estado.equals("guardarUnidad"))
        {
        	if(this.unidad.equals("-1"))
        		errores.add("Campo Unidad de Agenda requerido",new ActionMessage("errors.required", "La Unidad de Agenda "));
        	
        	if(this.usuario.equals("-1"))
        		errores.add("Campo Usuario Autorizado requerido",new ActionMessage("errors.required", "El Usuario Autorizado "));
        	
        	if(Integer.parseInt(this.getActividadesGenerados("numRegistros").toString())>0)
        	{
        		for(int i=1;i<=Integer.parseInt(this.getActividadesGenerados("numRegistros").toString());i++){
        			if(this.getActividadesGenerados("checkac_"+i).equals("1"))
        				nume=1;
        		}
        		if(nume==0)
        			errores.add("Campo Actividad Autorizada requerido",new ActionMessage("errors.required", "La Actividad Autorizada "));
        	}
        	else
            {
            	errores.add("Campo Actividad Autorizada requerido",new ActionMessage("errors.required", "La Actividad Autorizada "));
            }
        	if(!errores.isEmpty())
        		this.setEstado("modificarUnidad");
        }        
        return errores;
    }
	
	
	/**
	 * Metodo para resetaer los valores de la forma 
	 */
	public void reset( int codigoInstitucion, int centroAtencion)
	{
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.centroAtencion=centroAtencion+"";
		this.centroAtencionP=centroAtencion+"";
		this.codigoUsuarioP = "";
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.unidadAgendaMap=new HashMap();
		unidadAgendaMap.put("numRegistros", 0);
		this.unidadAgenda2Map=new HashMap();
		unidadAgenda2Map.put("numRegistros", 0);
		this.indexMap="";
		this.unidad="";
		this.unidadesMap=new HashMap();
		unidadesMap.put("numRegistros", 0);
		this.usuariosMap=new HashMap();
		usuariosMap.put("numRegistros", 0);
		this.usuario="";
		this.actividadesMap=new HashMap();
		actividadesMap.put("numRegistros", 0);
		this.actividad= new String []{""};
		this.actividadesGenerados=new HashMap();
		actividadesGenerados.put("numRegistros", 0);
		this.numActividades = 0;
		this.codigoRespuesta="";
		this.tipoAtencion="";
		
	}
	
	public String getTipoAtencion() {
		return tipoAtencion;
	}


	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}


	/**
	 * Metodo para resetear el Mapa de las Actividades Escogidas
	 * @return
	 */
	public void resetAct()
	{
		this.actividadesGenerados=new HashMap();
		actividadesGenerados.put("numRegistros", 0);
		this.numActividades = 0;
	}
	
	
	
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public HashMap getUnidadAgendaMap() {
		return unidadAgendaMap;
	}

	public void setUnidadAgendaMap(HashMap unidadAgendaMap) {
		this.unidadAgendaMap = unidadAgendaMap;
	}
	
	public Object getUnidadAgendaMap(String key) {
		return unidadAgendaMap.get(key);
	}

	public void setUnidadAgendaMap(String key, Object value) {
		this.unidadAgendaMap.put(key, value);
	}

	public String getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}

	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public HashMap getUnidadesMap() {
		return unidadesMap;
	}

	public void setUnidadesMap(HashMap unidadesMap) {
		this.unidadesMap = unidadesMap;
	}
	
	public Object getUnidadesMap(String key) {
		return unidadesMap.get(key);
	}

	public void setUnidadesMap(String key, Object value) {
		this.unidadesMap.put(key, value);
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public HashMap getUsuariosMap() {
		return usuariosMap;
	}

	public void setUsuariosMap(HashMap usuariosMap) {
		this.usuariosMap = usuariosMap;
	}

	public HashMap getActividadesMap() {
		return actividadesMap;
	}

	public void setActividadesMap(HashMap actividadesMap) {
		this.actividadesMap = actividadesMap;
	}
	
	public Object getActividadesMap(String key) {
		return actividadesMap.get(key);
	}

	public void setActividadesMap(String key, Object value) {
		this.actividadesMap.put(key, value);
	}
	
	public HashMap getActividadesGenerados() {
		return actividadesGenerados;
	}

	public void setActividadesGenerados(HashMap actividadesGenerados) {
		this.actividadesGenerados = actividadesGenerados;
	}
	
	public Object getActividadesGenerados(String key) {
		return actividadesGenerados.get(key);
	}

	public void setActividadesGenerados(String key, Object value) {
		this.actividadesGenerados.put(key, value);
	}

	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}
	
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public int getNumActividades() {
		return numActividades;
	}

	public void setNumActividades(int numActividades) {
		this.numActividades = numActividades;
	}

	public HashMap getUnidadAgenda2Map() {
		return unidadAgenda2Map;
	}

	public void setUnidadAgenda2Map(HashMap unidadAgenda2Map) {
		this.unidadAgenda2Map = unidadAgenda2Map;
	}
	
	public Object getUnidadAgenda2Map(String key) {
		return unidadAgenda2Map.get(key);
	}

	public void setUnidadAgenda2Map(String key, Object value) {
		this.unidadAgenda2Map.put(key, value);
	}

	public String getCentroAtencionP() {
		return centroAtencionP;
	}

	public void setCentroAtencionP(String centroAtencionP) {
		this.centroAtencionP = centroAtencionP;
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


	/**
	 * @return the codigoUsuarioP
	 */
	public String getCodigoUsuarioP() {
		return codigoUsuarioP;
	}


	/**
	 * @param codigoUsuarioP the codigoUsuarioP to set
	 */
	public void setCodigoUsuarioP(String codigoUsuarioP) {
		this.codigoUsuarioP = codigoUsuarioP;
	}


	/**
	 * @param actividad the actividad to set
	 */
	public void setActividad(String[] actividad) {
		this.actividad = actividad;
	}


	/**
	 * @return the actividad
	 */
	public String[] getActividad() {
		return actividad;
	}
}