package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


/**
 * Clase para el manejo de los reportes de ventas por facturar por centro de costo
 * Date: 2008-06-11
 * @author garias@princetonsa.com
 */
public class VentasXFacturarXCentroCostoForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa con la información de los motivox
	 */
	private HashMap filtrosMap;
	
	/**
	 * Listado de todos los centros de atención
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencion;
	
	/**
	 * Mapa con la información de los centros de costo
	 */
	private HashMap centrosCosto;
	
	/**
	 *
	 */
	public void reset()
	{
		this.filtrosMap=new HashMap();
		this.centrosAtencion=new ArrayList();
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", "0");
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
	 * @return the filtrosMap
	 */
	public HashMap getFiltrosMap() {
		return filtrosMap;
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(HashMap filtrosMap) {
		this.filtrosMap = filtrosMap;
	}

	/**
	 * @return the filtrosMap
	 */
	public Object getFiltrosMap(String llave) {
		return filtrosMap.get(llave);
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(String llave, Object obj) {
		this.filtrosMap.put(llave, obj);
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
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	
	
}	