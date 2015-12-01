package com.princetonsa.actionform.cargos;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

/**
 * Form que contiene todos los datos específicos para generar 
 * los cargos pendientes
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class GeneracionCargosPendientesForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;

	/**
	 * 
	 */
	private double codigoSubCuentaResponsable;
	
	
	/**
	 * Número de la solicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 */
	private int codigoTipoSolicitud;
	
	/**
	 * codigo del detalle del cargo
	 */
	private double codigoDetalleCargo;
	
	/**
	 * 
	 */
	private Vector erroresCargo;
	
	/**
	 * 
	 */
	private HashMap subCuentasResponsablesTagMap;
	
	/**
	 * 
	 */
	private HashMap listadoSolicitudesMap;
	
	/**
	 * mapa con la informacion de la valoracion pendiente
	 */
	private HashMap valoracionPendienteMap;
	
	/**
	 * mapa con la informacion de una solicitud de 
	 * interconsulta - procedimientos -evolucion - cargos directos
	 * que quedo pendiente
	 */
	private HashMap solicitudServicioPendienteMap;
	
	/**
	 * especialidad + servicio
	 */
	private String codigoAxioma;
	
	/**
	 * desc servicio cups
	 */
	private String descripcionServicioCups;
	
	/**
	 * Boolean que indica si es cobrable o es excenta
	 */
	private boolean esCobrable;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private double valorTotal;
	
	/**
	 * 
	 */
	private String fechaAdministracion;
	
	/**
	 * 
	 */
	//private String numeroAutorizacion;
	
	/**
	 * Datos propios de la administración
	 */
	private HashMap administracion;
	
	/**
	 * 
	 */
	private String esPortatil;
	
	/**
	 * 
	 */
	private boolean esProcesoAutorizacionCapita;
	
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
		if(estado.equals("guardarDetalleSolicitudes") && this.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudEvolucion)
		{
			if(this.codigoAxioma==null || this.codigoAxioma.equals(""))
			{
				errores.add("Servicio vacio", new ActionMessage("errors.required","El campo Servicio"));
			}
		}
		if(estado.equals("guardarDetalleSolValoracion"))
		{				
			errores=super.validate(mapping,request);
			
			if(this.codigoAxioma==null || this.codigoAxioma.equals(""))
			{
				errores.add("Servicio vacio", new ActionMessage("errors.required","El campo Servicio"));
			}
			if(!errores.isEmpty())
			{
				this.setEstado("detalle");
			}		
		}		
		return errores;
	}
	
	/**
	 * reset
	 *
	 */
	public void reset()
	{
		this.codigoSubCuentaResponsable= ConstantesBD.codigoNuncaValido;
		this.listadoSolicitudesMap= new HashMap();
		this.listadoSolicitudesMap.put("numRegistros", "0");
		this.subCuentasResponsablesTagMap= new HashMap();
		this.subCuentasResponsablesTagMap.put("numRegistros", "0");
		this.numeroSolicitud= ConstantesBD.codigoNuncaValido;
		this.codigoTipoSolicitud=ConstantesBD.codigoNuncaValido;
		this.valoracionPendienteMap= new HashMap();
		this.valoracionPendienteMap.put("numRegistros", "0");
		this.solicitudServicioPendienteMap= new HashMap();
		this.solicitudServicioPendienteMap.put("numRegistros", "0");
		
		this.codigoAxioma="";
		this.descripcionServicioCups="";
		this.esCobrable=true;
		this.observaciones="";
		this.valorTotal=0;
		
		this.codigoDetalleCargo= ConstantesBD.codigoNuncaValidoDouble;
		this.erroresCargo= new Vector();
		
		this.fechaAdministracion="";
		//this.numeroAutorizacion="";
		
		this.administracion= new HashMap();
		this.administracion.put("numRegistros", "0");
		
		this.esPortatil="";
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
	 * @return the listadoSolicitudesMap
	 */
	public HashMap getListadoSolicitudesMap() {
		return listadoSolicitudesMap;
	}

	/**
	 * @param listadoSolicitudesMap the listadoSolicitudesMap to set
	 */
	public void setListadoSolicitudesMap(HashMap listadoSolicitudesMap) {
		this.listadoSolicitudesMap = listadoSolicitudesMap;
	}
	
	/**
	 * @return the listadoSolicitudesMap
	 */
	public Object getListadoSolicitudesMap(Object key) {
		return listadoSolicitudesMap.get(key);
	}

	/**
	 * @param listadoSolicitudesMap the listadoSolicitudesMap to set
	 */
	public void setListadoSolicitudesMap(Object key, Object value) {
		this.listadoSolicitudesMap.put(key, value);
	}

	/**
	 * @return the codigoTipoSolicitud
	 */
	public int getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}

	/**
	 * @param codigoTipoSolicitud the codigoTipoSolicitud to set
	 */
	public void setCodigoTipoSolicitud(int codigoTipoSolicitud) {
		this.codigoTipoSolicitud = codigoTipoSolicitud;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the valoracionPendienteMap
	 */
	public HashMap getValoracionPendienteMap() {
		return valoracionPendienteMap;
	}

	/**
	 * @param valoracionPendienteMap the valoracionPendienteMap to set
	 */
	public void setValoracionPendienteMap(HashMap valoracionPendienteMap) {
		this.valoracionPendienteMap = valoracionPendienteMap;
	}

	/**
	 * @return the valoracionPendienteMap
	 */
	public Object getValoracionPendienteMap(Object key) {
		return valoracionPendienteMap.get(key);
	}

	/**
	 * @param valoracionPendienteMap the valoracionPendienteMap to set
	 */
	public void setValoracionPendienteMap(Object key, Object value) {
		this.valoracionPendienteMap.put(key, value);
	}

	/**
	 * @return the codigoAxioma
	 */
	public String getCodigoAxioma() {
		return codigoAxioma;
	}

	/**
	 * @param codigoAxioma the codigoAxioma to set
	 */
	public void setCodigoAxioma(String codigoAxioma) {
		this.codigoAxioma = codigoAxioma;
	}
	
	/**
	 * @return Returns the esCobrable.
	 */
	public boolean getEsCobrable() {
		return esCobrable;
	}
	/**
	 * @param esCobrable The esCobrable to set.
	 */
	public void setEsCobrable(boolean esCobrable) {
		this.esCobrable = esCobrable;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the descripcionServicioCups
	 */
	public String getDescripcionServicioCups() {
		return descripcionServicioCups;
	}

	/**
	 * @param descripcionServicioCups the descripcionServicioCups to set
	 */
	public void setDescripcionServicioCups(String descripcionServicioCups) {
		this.descripcionServicioCups = descripcionServicioCups;
	}

	/**
	 * @return the codigoDetalleCargo
	 */
	public double getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	/**
	 * @param codigoDetalleCargo the codigoDetalleCargo to set
	 */
	public void setCodigoDetalleCargo(double codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	/**
	 * @return the valorTotal
	 */
	public double getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the erroresCargo
	 */
	public Vector getErroresCargo() {
		return erroresCargo;
	}

	/**
	 * @param erroresCargo the erroresCargo to set
	 */
	public void setErroresCargo(Vector erroresCargo) {
		this.erroresCargo = erroresCargo;
	}

	/**
	 * @param erroresCargo the erroresCargo to set
	 */
	public void setErrorCargo(String error) {
		this.erroresCargo.add(error);
	}
	
	/**
	 * @return the solicitudServicioPendienteMap
	 */
	public HashMap getSolicitudServicioPendienteMap() {
		return solicitudServicioPendienteMap;
	}

	/**
	 * @param solicitudServicioPendienteMap the solicitudServicioPendienteMap to set
	 */
	public void setSolicitudServicioPendienteMap(
			HashMap solicitudServicioPendienteMap) {
		this.solicitudServicioPendienteMap = solicitudServicioPendienteMap;
	}
	
	/**
	 * @return the solicitudServicioPendienteMap
	 */
	public Object getSolicitudServicioPendienteMap(Object key) {
		return solicitudServicioPendienteMap.get(key);
	}

	/**
	 * @param solicitudServicioPendienteMap the solicitudServicioPendienteMap to set
	 */
	public void setSolicitudServicioPendienteMap(
			Object key, Object value) {
		this.solicitudServicioPendienteMap.put(key, value);
	}

	/**
	 * @return the fechaAdministracion
	 */
	public String getFechaAdministracion() {
		return fechaAdministracion;
	}

	/**
	 * @param fechaAdministracion the fechaAdministracion to set
	 */
	public void setFechaAdministracion(String fechaAdministracion) {
		this.fechaAdministracion = fechaAdministracion;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	/*
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	/*
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	*/
	/**
	 * @return the administracion
	 */
	public HashMap getAdministracion() {
		return administracion;
	}

	/**
	 * @param administracion the administracion to set
	 */
	public void setAdministracion(HashMap administracion) {
		this.administracion = administracion;
	}

	/**
	 * @return the administracion
	 */
	public Object getAdministracion(Object key) {
		return administracion.get(key);
	}

	/**
	 * @param administracion the administracion to set
	 */
	public void setAdministracion(Object key, Object value) {
		this.administracion.put(key, value);
	}

	/**
	 * @return the codigoSubCuentaResponsable
	 */
	public double getCodigoSubCuentaResponsable() {
		return codigoSubCuentaResponsable;
	}

	/**
	 * @param codigoSubCuentaResponsable the codigoSubCuentaResponsable to set
	 */
	public void setCodigoSubCuentaResponsable(double codigoSubCuentaResponsable) {
		this.codigoSubCuentaResponsable = codigoSubCuentaResponsable;
	}

	/**
	 * @return the subCuentasResponsablesTagMap
	 */
	public HashMap getSubCuentasResponsablesTagMap() {
		return subCuentasResponsablesTagMap;
	}

	/**
	 * @param subCuentasResponsablesTagMap the subCuentasResponsablesTagMap to set
	 */
	public void setSubCuentasResponsablesTagMap(HashMap subCuentasResponsablesTagMap) {
		this.subCuentasResponsablesTagMap = subCuentasResponsablesTagMap;
	}

	/**
	 * @return the subCuentasResponsablesTagMap
	 */
	public Object getSubCuentasResponsablesTagMap(Object key) {
		return subCuentasResponsablesTagMap.get(key);
	}

	/**
	 * @param subCuentasResponsablesTagMap the subCuentasResponsablesTagMap to set
	 */
	public void setSubCuentasResponsablesTagMap(Object key, Object value) {
		this.subCuentasResponsablesTagMap.put(key,value);
	}

	/**
	 * @return the esPortatil
	 */
	public String getEsPortatil() {
		return esPortatil;
	}

	/**
	 * @param esPortatil the esPortatil to set
	 */
	public void setEsPortatil(String esPortatil) {
		this.esPortatil = esPortatil;
	}

	public boolean isEsProcesoAutorizacionCapita() {
		return esProcesoAutorizacionCapita;
	}

	public void setEsProcesoAutorizacionCapita(boolean esProcesoAutorizacionCapita) {
		this.esProcesoAutorizacionCapita = esProcesoAutorizacionCapita;
	}
}
