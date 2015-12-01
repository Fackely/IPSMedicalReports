/*
 * Created on Aug 29, 2005
 */
package com.princetonsa.actionform.parametrizacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TiposMonitoreoForm extends ValidatorForm {
	/**
	 * Variable para manejar el estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Objeto donde se almacenan los tipos de monitoreo
	 */
	private HashMap tiposMonitoreo= new HashMap();
	
	/**
	 * Mapa de los centros de costo por cada tipo de Monitoreo
	 */
	private HashMap centrosPorTipo;
	
	/**
	 * Variable que maneja el número de registros del Mapa
	 */
	private int numRegistros;
	
	/**
	 * Variable que almacena el código del registro que se desea
	 * eliminar
	 */
	private int codigoRegistro;
	
	/**
	 * Mapa para los centros de Costo
	 */
	private HashMap centrosCostoMap;
	
	/**
	 * Mapa para los centros seleccionados
	 */
	private HashMap centrosGeneradosMap;
	
	/**
	 * Numero de centros Generados
	 */
	private int numCentrosGenerados;
	
	/**
	 * Control para solo ingresar un registro
	 */
	private int control;
	
	/**
	 * Centro de Costo seleccionado
	 */
	private String centroCosto;
	
	/**
	 * Descripcion del nuevo tipo de monitoreo
	 */
	private String descripcion;
	
	/**
	 * Prioridad del nuevo tipo de monitoreo
	 */
	private String prioridad;
	
	/**
	 * Check de requiere valoracion del tipo de monitoreo
	 */
	private String check;
	
	/**
	 * Servicio para el tipo de monitoreo
	 */
	private String servicio;
	
	/**
	 * Index del Mapa
	 */
	private String indexMap="";
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
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
		
		if(estado.equals("insertar"))
		{
			int contador=0;
			int aux=0;
			int aux1=0;
			int aux2=0;
			boolean bandera=false;
			
			if(this.descripcion.equals(""))
				errores.add("Campo Descripcion requerido",new ActionMessage("errors.required", "La Descripción del Tipo de Monitoreo "));
			
			if(this.prioridad.equals(""))
				errores.add("Campo Prioridad requerido",new ActionMessage("errors.required", "La Prioridad del Tipo de Monitoreo "));
			
			if(this.servicio.equals("-1"))
				errores.add("Campo Servicio requerido",new ActionMessage("errors.required", "El Servicio del Tipo de Monitoreo "));
			
			if(!errores.isEmpty())
				this.estado="empezar";
			
			if(Integer.parseInt(this.getCentrosGeneradosMap("numRegistros").toString())>0)
        	{
        		for(int i=1;i<=Integer.parseInt(this.getCentrosGeneradosMap("numRegistros").toString());i++){
        			if(this.getCentrosGeneradosMap("checkcc_"+i).equals("1"))
        				aux=1;
        		}
        		if(aux==0)
        			errores.add("Campo Centro de Costo requerido",new ActionMessage("errors.required", "El Centro de Costo "));
        	}
        	else
            {
            	errores.add("Campo Centro de Costo requerido",new ActionMessage("errors.required", "El Centro de Costo "));
            }
			
			for(int j=0;j<Integer.parseInt(this.getTiposMonitoreo("numRegistros").toString());j++)
			{
				if(this.descripcion.equals(this.getTiposMonitoreo("nombre_"+j).toString()))
					aux1=1;
			}
			if(aux1==1)
				errores.add("Campo Descripcion Repetido requerido",new ActionMessage("errors.notEspecific", "La Descripción del Tipo Monitoreo ya se encuentra Parametrizada. Por favor Cambiela."));
			
			for(int j=0;j<Integer.parseInt(this.getTiposMonitoreo("numRegistros").toString());j++)
			{
				if(this.prioridad.equals(this.getTiposMonitoreo("prioridad_"+j).toString()))
					aux2=1;
			}
			if(aux2==1)
				errores.add("Campo Prioridad Repetido requerido",new ActionMessage("errors.notEspecific", "La Prioridad del Tipo Monitoreo ya se encuentra Parametrizada. Por favor Cambiela."));
			if(!errores.isEmpty())
        		this.setEstado("nuevo");
		}
		if(estado.equals("guardar"))
		{
			int contador=0;
			int aux=0;
			int aux1=0;
			int aux2=0;
			boolean bandera=false;
			
			if(this.descripcion.equals(""))
				errores.add("Campo Descripcion requerido",new ActionMessage("errors.required", "La Descripción del Tipo de Monitoreo "));
			
			if(this.prioridad.equals(""))
				errores.add("Campo Prioridad requerido",new ActionMessage("errors.required", "La Prioridad del Tipo de Monitoreo "));
			
			if(this.servicio.equals("-1"))
				errores.add("Campo Servicio requerido",new ActionMessage("errors.required", "El Servicio del Tipo de Monitoreo "));
			
			if(!errores.isEmpty())
				this.estado="empezar";
			
			if(Integer.parseInt(this.getCentrosGeneradosMap("numRegistros").toString())>0)
        	{
        		for(int i=1;i<=Integer.parseInt(this.getCentrosGeneradosMap("numRegistros").toString());i++){
        			if(this.getCentrosGeneradosMap("checkcc_"+i).equals("1"))
        				aux=1;
        		}
        		if(aux==0)
        			errores.add("Campo Centro de Costo requerido",new ActionMessage("errors.required", "El Centro de Costo "));
        	}
        	else
            {
            	errores.add("Campo Centro de Costo requerido",new ActionMessage("errors.required", "El Centro de Costo "));
            }
			
			for(int j=0;j<Integer.parseInt(this.getTiposMonitoreo("numRegistros").toString());j++)
			{
				if(this.descripcion.equals(this.getTiposMonitoreo("nombre_"+j).toString()) && Utilidades.convertirAEntero(this.indexMap) != j)
					aux1=1;
			}
			if(aux1==1)
				errores.add("Campo Descripcion Repetido requerido",new ActionMessage("errors.notEspecific", "La Descripción del Tipo Monitoreo ya se encuentra Parametrizada. Por favor Cambiela."));
			
			for(int j=0;j<Integer.parseInt(this.getTiposMonitoreo("numRegistros").toString());j++)
			{
				if(this.prioridad.equals(this.getTiposMonitoreo("prioridad_"+j).toString()) && Utilidades.convertirAEntero(this.indexMap) != j)
					aux2=1;
			}
			if(aux2==1)
				errores.add("Campo Prioridad Repetido requerido",new ActionMessage("errors.notEspecific", "La Prioridad del Tipo Monitoreo ya se encuentra Parametrizada. Por favor Cambiela."));
			
			if(!errores.isEmpty())
        		this.setEstado("modificar");
		}
		return errores;
	}
	
	/**
	 * método que verifica secuencialidad en los tipos de monitoreo
	 * @param errores
	 * @param contador
	 * @return
	 */
	private ActionErrors verificarPrioridadesDuplicadas(ActionErrors errores, int contador) {
		String prioridades[]=new String[contador];
		String aux="";
		int tamano=0;
		//se asignan las prioridades a un vector temporal
		for(int i=0;i<numRegistros;i++)
		{
			aux=tiposMonitoreo.get("prioridad_"+i)+"";
			if(!aux.equals("")&&!aux.equals("null"))
			{
				prioridades[tamano]=aux;
				tamano++;
			}
		}
		
		//se recorre ciclo de comparación
		for(int i=0;i<(tamano-1);i++)
		{
			for(int j=(tamano-1);j>i;j--)
			{
				//se revisa si hay prioridades iguales
				if(Integer.parseInt(prioridades[i])==Integer.parseInt(prioridades[j]))
				{
					errores.add("prioridades sin orden secuencial", new ActionMessage("error.tipoMonitoreo.prioridadesDuplicadas"));
					//se rompe ciclo
					j=i;
					i=tamano;
				}
			}
		}
		
		return errores;
	}

	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
	    this.estado="";
	    this.tiposMonitoreo=new HashMap();
	    this.numRegistros=0;
	    this.codigoRegistro=0;
	    this.centrosCostoMap=new HashMap();
	    centrosCostoMap.put("numRegistros", 0);
	    this.centrosGeneradosMap=new HashMap();
	    centrosGeneradosMap.put("numRegistros", 0);
	    this.control=0;
	    this.numCentrosGenerados=0;
	    this.centroCosto="";
	    this.centrosPorTipo=new HashMap();
	    centrosPorTipo.put("numRegistros", 0);
	    this.descripcion="";
	    this.prioridad="";
	    this.check="";
	    this.servicio="-1";
	    this.indexMap="";
	}
	
	/**
	 * reset de un Mapa de la forma
	 *
	 */
	public void resetCG()
	{
		this.centrosGeneradosMap=new HashMap();
	    centrosGeneradosMap.put("numRegistros", 0);
	    this.numCentrosGenerados=0;
	}
	
	//************MÉTODOS GETTERS & SETTERS*****************************************
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the tiposMonitoreo.
	 */
	public HashMap getTiposMonitoreo() {
		return tiposMonitoreo;
	}
	/**
	 * @param tiposMonitoreo The tiposMonitoreo to set.
	 */
	public void setTiposMonitoreo(HashMap tiposMonitoreo) {
		this.tiposMonitoreo = tiposMonitoreo;
	}
	/**
	 * @return Retorna un registro del mapa tiposMonitoreo.
	 */
	public Object getTiposMonitoreo(String key) {
		return tiposMonitoreo.get(key);
	}
	/**
	 * @param tiposMonitoreo The tiposMonitoreo to set.
	 */
	public void setTiposMonitoreo(String key,Object obj) {
		this.tiposMonitoreo.put(key,obj);
	}
	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	/**
	 * @return Returns the codigoRegistro.
	 */
	public int getCodigoRegistro() {
		return codigoRegistro;
	}
	/**
	 * @param codigoRegistro The codigoRegistro to set.
	 */
	public void setCodigoRegistro(int codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}

	public HashMap getCentrosCostoMap() {
		return centrosCostoMap;
	}

	public void setCentrosCostoMap(HashMap centrosCostoMap) {
		this.centrosCostoMap = centrosCostoMap;
	}
	
	public Object getCentrosCostoMap(String key) {
		return centrosCostoMap.get(key);
	}
	
	public void setCentrosCostoMap(String key,Object obj) {
		this.centrosCostoMap.put(key,obj);
	}

	public int getControl() {
		return control;
	}

	public void setControl(int control) {
		this.control = control;
	}

	public HashMap getCentrosGeneradosMap() {
		return centrosGeneradosMap;
	}

	public void setCentrosGeneradosMap(HashMap centrosGeneradosMap) {
		this.centrosGeneradosMap = centrosGeneradosMap;
	}
	
	public Object getCentrosGeneradosMap(String key) {
		return centrosGeneradosMap.get(key);
	}
	
	public void setCentrosGeneradosMap(String key,Object obj) {
		this.centrosGeneradosMap.put(key,obj);
	}

	public int getNumCentrosGenerados() {
		return numCentrosGenerados;
	}

	public void setNumCentrosGenerados(int numCentrosGenerados) {
		this.numCentrosGenerados = numCentrosGenerados;
	}

	public String getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	public HashMap getCentrosPorTipo() {
		return centrosPorTipo;
	}

	public void setCentrosPorTipo(HashMap centrosPorTipo) {
		this.centrosPorTipo = centrosPorTipo;
	}
	
	public Object getCentrosPorTipo(String key) {
		return centrosPorTipo.get(key);
	}

	public void setCentrosPorTipo(String key, Object value) {
		this.centrosPorTipo.put(key, value);
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}
	
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
}