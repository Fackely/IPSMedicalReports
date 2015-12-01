package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author wilson
 *
 */
public class ReliquidacionTarifasForm extends ValidatorForm 
{
	/**
     * estado de la accion
     */
    private String estado;
    
    /**
     * 
     */
    private HashMap listadoCuentasMap;
    
    /**
     * 
     */
    private int indexMapa;
    
    /**
     * 
     */
    private boolean reliquidarServicios;
    
    /**
     * 
     */
    private boolean reliquidarArticulos;
    
    
    /**
     * 
     */
    private String motivo;
    
    
    /**
     * 
     */
    private HashMap esquemasInventario;
    
    /**
     * 
     */
    private HashMap esquemasProcedimientos;
    
    /**
	 * 
	 */
	private Vector erroresCargo;
	
	
	/**
	 * 
	 */
	private int posEliminar;

	/**
	 * resetea los valores de la forma
	 */
	public void reset()
	{
		this.listadoCuentasMap= new HashMap();
		this.listadoCuentasMap.put("numRegistros", "0");
		this.indexMapa=ConstantesBD.codigoNuncaValido;
		this.reliquidarServicios=false;
		this.reliquidarArticulos=false;
		this.esquemasInventario=new HashMap();
		this.esquemasInventario.put("numRegistros", "0");
		this.esquemasProcedimientos=new HashMap();
		this.esquemasProcedimientos.put("numRegistros", "0");
		this.motivo="";
		this.erroresCargo= new Vector();
		this.posEliminar=ConstantesBD.codigoNuncaValido;
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
		ActionErrors errores= new ActionErrors();
		errores=super.validate(mapping,request);
		if(estado.equals("reliquidar"))
		{	
			if(!this.reliquidarServicios && !this.reliquidarArticulos)
			{	
		    	errores.add("Campo vacio", new ActionMessage("errors.required","El tipo de liquidacion (Servicios - Articulos)"));
			}
		   
		    if(UtilidadTexto.isEmpty(this.getMotivo().trim()))
		    {
		    	errores.add("Campo vacio", new ActionMessage("errors.required","El motivo reliquidacion"));
		    }
		    if(!errores.isEmpty())
		    	this.setEstado("refrescarEsquemas");
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
	 * @return the listadoCuentasMap
	 */
	public HashMap getListadoCuentasMap() {
		return listadoCuentasMap;
	}

	/**
	 * @param listadoCuentasMap the listadoCuentasMap to set
	 */
	public void setListadoCuentasMap(HashMap listadoCuentasMap) {
		this.listadoCuentasMap = listadoCuentasMap;
	}
	
	/**
	 * @return the listadoCuentasMap
	 */
	public Object getListadoCuentasMap(Object key) {
		return listadoCuentasMap.get(key);
	}

	/**
	 * @param listadoCuentasMap the listadoCuentasMap to set
	 */
	public void setListadoCuentasMap(Object key, Object value) {
		this.listadoCuentasMap.put(key, value);
	}

	/**
	 * @return the indexMapa
	 */
	public int getIndexMapa() {
		return indexMapa;
	}

	/**
	 * @param indexMapa the indexMapa to set
	 */
	public void setIndexMapa(int indexMapa) {
		this.indexMapa = indexMapa;
	}

	/**
	 * @return the reliquidarArticulos
	 */
	public boolean getReliquidarArticulos() {
		return reliquidarArticulos;
	}

	/**
	 * @param reliquidarArticulos the reliquidarArticulos to set
	 */
	public void setReliquidarArticulos(boolean reliquidarArticulos) {
		this.reliquidarArticulos = reliquidarArticulos;
	}

	/**
	 * @return the reliquidarServicios
	 */
	public boolean getReliquidarServicios() {
		return reliquidarServicios;
	}

	/**
	 * @param reliquidarServicios the reliquidarServicios to set
	 */
	public void setReliquidarServicios(boolean reliquidarServicios) {
		this.reliquidarServicios = reliquidarServicios;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
	 * @return the erroresCargo
	 */
	public Object getErrorCargo(int index) {
		return erroresCargo.get(index);
	}

	/**
	 * @param erroresCargo the erroresCargo to set
	 */
	public void setErroresCargo(Object value) {
		this.erroresCargo.add(value);
	}

	public HashMap getEsquemasInventario() {
		return esquemasInventario;
	}

	public void setEsquemasInventario(HashMap esquemasInventario) {
		this.esquemasInventario = esquemasInventario;
	}

	public HashMap getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}

	public void setEsquemasProcedimientos(HashMap esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}

	public int getPosEliminar() {
		return posEliminar;
	}

	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}

}
