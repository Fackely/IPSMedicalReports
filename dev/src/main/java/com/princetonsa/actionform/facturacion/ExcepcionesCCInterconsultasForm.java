package com.princetonsa.actionform.facturacion;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import util.UtilidadTexto;
import util.Utilidades;

public class ExcepcionesCCInterconsultasForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Centros de Atencion a consultar y/o ingresar excepciones
	 */
	private HashMap centrosAtencion;
	
	/**
	 * Variable para guardar el centro de atencion seleccionado
	 */
	private String centroAtencionSeleccionado;
	
	/**
	 * Variable para guardar los parametros de búsqueda
	 */
	private HashMap<String, Object> listadoXCentroAtencion;
	
	/**
	 * Variable para guardar los parametros de búsqueda
	 */
	private HashMap<String, Object> datosIngresoExcepcion;
	
	/**
	 * Variable cargar los centros de costo que solicitan
	 */
	private HashMap<String, Object> centrosCosto;
	
	/**
	 * Variable cargar los servicios
	 */
	private HashMap<String, Object> servicios;
	
	/**
	 * Variable cargar los médicos
	 */
	private HashMap<String, Object>  medicos;
	
	/**
	 * Variable para almacenar el patron para ordenar el mapa de Excepciones
	 */
	private String patronOrdenar;
	
	/**
	 * Variable para almacenar el ultimo Patron con el que se ordeno el mapa de Excepciones
	 */
	private String ultimoPatron;
	
	/**
	 * Variable para almacenar el indice de la Excepcion a Eliminar o Modificar
	 */
	private int indice;
	
	/**
	 * Variable para enviar mensajes de error
	 */
	
	private String mensaje;
	/**
	 * 
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * variable para guardar los datos a modificar
	 */
	private HashMap<String, Object> datosAModificarMap;
	
	/**
	 * indicativo para indicar que elementos mostrar en la vista
	 */
	private boolean indicativo;
	
	/**
	 * Variable cargar los centros de costo que ejecutan
	 */
	private HashMap<String, Object> centrosCostoEjecutan;
	
	/**
	 *Inicializa las variables
	 */
	public void reset()
	{
		this.centrosAtencion= new HashMap();
		this.listadoXCentroAtencion = new HashMap<String, Object>();
		this.listadoXCentroAtencion.put("numRegistros", "0");
		this.centrosCosto=new HashMap<String, Object>();
		this.centrosCosto.put("numRegistros", "0");
		this.servicios=new HashMap<String, Object>();
		this.medicos = new HashMap<String, Object>();
		this.datosIngresoExcepcion = new HashMap<String, Object>();
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.indice=0;
		this.centroAtencionSeleccionado="";
		this.mensaje="";
		this.codigosServiciosInsertados="";
		this.datosAModificarMap = new HashMap<String, Object>();
		this.indicativo=false;
		this.centrosCostoEjecutan=new HashMap<String, Object>();
		this.centrosCostoEjecutan.put("numRegistros", "0");
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
    
        // Validar Campos Requeridos
        
        if(this.estado.equals("guardarExcepcion") || this.estado.equals("editarExcepcionConfirmar") ){
        	// Se validan los campos de centros_costo_solicita, servicio, medico ejecuta y centro_costo_ejecuta
			if (UtilidadTexto.isEmpty(this.datosIngresoExcepcion.get("centroCostoSolicita").toString()) )
			{
				errores.add(this.datosIngresoExcepcion.get("centroCostoSolicita").toString(), new ActionMessage("errors.required","El Centro de Costo Solicita "));
			}
			
			if (UtilidadTexto.isEmpty(this.datosIngresoExcepcion.get("servicioDesc").toString()))
			{
				errores.add(this.datosIngresoExcepcion.get("servicioDesc").toString(), new ActionMessage("errors.required","El Servicio "));
			}
			
			if (UtilidadTexto.isEmpty(this.datosIngresoExcepcion.get("medico").toString()))
			{
				errores.add(this.datosIngresoExcepcion.get("medico").toString(), new ActionMessage("errors.required","El Medico que Ejecuta  "));
			}
			
			if (UtilidadTexto.isEmpty(this.datosIngresoExcepcion.get("centroCostoEjecuta").toString()))
			{
				errores.add(this.datosIngresoExcepcion.get("centroCostoEjecuta").toString(), new ActionMessage("errors.required","El Centro de Costo Ejecuta "));
			}
			
			if (UtilidadTexto.isEmpty(this.centroAtencionSeleccionado.toString()))
			{
				errores.add(this.centroAtencionSeleccionado.toString(), new ActionMessage("error.NoCargar","El Centro de Costo Ejecuta "));
			}
				
			if (getEstado().equals("guardarExcepcion"))
			{
				for(int i=0;i<Utilidades.convertirAEntero(getListadoXCentroAtencion("numRegistros").toString());i++)
				{
					if(getListadoXCentroAtencion("centroatencion_"+i).toString().equals(getCentroAtencionSeleccionado())
						&&getListadoXCentroAtencion("centrocostosolicita_"+i).toString().equals(getDatosIngresoExcepcion("centroCostoSolicita").toString())
						&&getListadoXCentroAtencion("servicio_"+i).toString().equals(getDatosIngresoExcepcion("codServicio").toString())
						&&getListadoXCentroAtencion("medico_"+i).toString().equals(getDatosIngresoExcepcion("medico").toString()))
						{
							errores.add("", new ActionMessage("error.facturacion.combinacionCAtencionCCSolServMedico"));
						}
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
	 * @return the Centros de Atención
	 */
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param Centros de Atención the Centros de Atención to set
	 */
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
     
	/**
	 * @return the filtrosMap
	 */
	public HashMap<String, Object> getListadoXCentroAtencion() {
		return listadoXCentroAtencion;
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setListadoXCentroAtencion(HashMap<String, Object> listadoXCentroAtencion) {
		this.listadoXCentroAtencion = listadoXCentroAtencion;
	}
	
	/**
	 * @return the listadoXCentroAtencion
	 */
	public Object getListadoXCentroAtencion(String llave) {
		return listadoXCentroAtencion.get(llave);
	}

	/**
	 * @param listadoXCentroAtencion the listadoXCentroAtencion to set
	 */
	public void setListadoXCentroAtencion(String llave, Object obj) {
		this.listadoXCentroAtencion.put(llave, obj);
	}

	/**
	 * @return the centrosCosto
	 */
	public HashMap<String, Object> getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap<String, Object> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return the servicios
	 */
	public HashMap<String, Object> getServicios() {
		return servicios;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(HashMap<String, Object> servicios) {
		this.servicios = servicios;
	}

	/**
	 * @return the medicos
	 */
	public HashMap<String, Object> getMedicos() {
		return medicos;
	}

	/**
	 * @param medicos the medicos to set
	 */
	public void setMedicos(HashMap<String, Object> medicos) {
		this.medicos = medicos;
	}
	
	/**
	 * @param medicos the medicos to get
	 */
	public Object getMedicos(String llave) {
		return medicos.get(llave);
	}

	/**
	 * @param medicos the medicos to set
	 */
	public void setMedicos(String llave, Object obj) {
		this.medicos.put(llave, obj);
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
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}

	/**
	 * @return the centroAtencionSeleccionado
	 */
	public String getCentroAtencionSeleccionado() {
		return centroAtencionSeleccionado;
	}

	/**
	 * @param centroAtencionSeleccionado the centroAtencionSeleccionado to set
	 */
	public void setCentroAtencionSeleccionado(String centroAtencionSeleccionado) {
		this.centroAtencionSeleccionado = centroAtencionSeleccionado;
	}

	/**
	 * @return the datosIngresoExcepcion
	 */
	public HashMap<String, Object> getDatosIngresoExcepcion() {
		return datosIngresoExcepcion;
	}

	/**
	 * @param datosIngresoExcepcion the datosIngresoExcepcion to set
	 */
	public void setDatosIngresoExcepcion(
			HashMap<String, Object> datosIngresoExcepcion) {
		this.datosIngresoExcepcion = datosIngresoExcepcion;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
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
	 * @return the datosAModificar
	 */
	public Object getDatosIngresoExcepcion(String llave) {
		return datosIngresoExcepcion.get(llave);
	}

	/**
	 * @param datosAModificar the datosAModificar to set
	 */
	public void setDatosIngresoExcepcion(String llave, Object obj) {
		this.datosIngresoExcepcion.put(llave, obj);
	}

	/**
	 * @return the indicativo
	 */
	public boolean isIndicativo() {
		return indicativo;
	}

	/**
	 * @param indicativo the indicativo to set
	 */
	public void setIndicativo(boolean indicativo) {
		this.indicativo = indicativo;
	}

	/**
	 * @return the centrosCostoEjecutan
	 */
	public HashMap<String, Object> getCentrosCostoEjecutan() {
		return centrosCostoEjecutan;
	}

	/**
	 * @param centrosCostoEjecutan the centrosCostoEjecutan to set
	 */
	public void setCentrosCostoEjecutan(HashMap<String, Object> centrosCostoEjecutan) {
		this.centrosCostoEjecutan = centrosCostoEjecutan;
	}
}