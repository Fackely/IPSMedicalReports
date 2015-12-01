package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Clase para el manejo de la parametrizacion 
 * de la impresion de soportes de facturas
 * Date: 2009-02-09
 * @author jfhernandez@princetonsa.com
 */
public class ImpresionSoportesFacturasForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa en el que se almacenan los filtros seleccionados
	 */
	private HashMap<String, Object> filtrosMap;
	
	/**
	 * Mapa en el que se almacenan los tipos de soportes para un convenio
	 */
	private HashMap<String, Object> tiposSoportesMap;
	
	/**
	 * Mapa en el que se almacenan las vias de ingreso
	 */
	private ArrayList<HashMap> viasIngreso;
	
	/**
	 * Mapa en el que se almacenan los convenios
	 */
	private ArrayList<HashMap> convenios;
	
	/**
	 * Mapa en el que se almacenan los filtros seleccionados
	 */
	private HashMap<String, Object> listadoFacturasMap;
	
	/**
	 * Mapa en el que se almacenan las solicitudes asociadas a una factura
	 */
	private HashMap<String, Object> solicitudesFacturaMap;
	
	/**
	 * Posicion mapa
	 */
	private int posMap;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Resetear las variables de la funcionalidad
	 */
	public void reset()
	{
		this.filtrosMap = new HashMap<String, Object>();
		this.filtrosMap.put("numRegistros", 0);
		this.tiposSoportesMap = new HashMap<String, Object>();
		this.tiposSoportesMap.put("numRegistros", 0);
		this.listadoFacturasMap = new HashMap<String, Object>();
		this.listadoFacturasMap.put("numRegistros", 0);
		this.viasIngreso= new ArrayList<HashMap>();
		this.posMap = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.solicitudesFacturaMap = new HashMap<String, Object>();
		this.solicitudesFacturaMap.put("numRegistros", 0);
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
        
        if(this.estado.equals("listarFacturas"))
        {
        	//Se validan el ingreso de las facturas   
        	
        	//Validamos que la factura final es requerida siempre y cuando la inicial no este vacia
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("facturaInicial").toString()) && (UtilidadTexto.isEmpty((this.filtrosMap.get("facturaFinal").toString()))))
				errores.add(this.filtrosMap.get("facturaFinal").toString(), new ActionMessage("errors.required","La Factura Final "));
			
			//Validamos que la factura inicial es requerida siempre y cuando la final no este vacia
			if(!UtilidadTexto.isEmpty(this.filtrosMap.get("facturaFinal").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("facturaInicial").toString()))
				errores.add(this.filtrosMap.get("facturaInicial").toString(), new ActionMessage("errors.required","La Factura Inicial "));
			
			//Hacemos la validacion que los campos factura inicial y final sean mayores a cero y que el rango inicial sea menos al rango final
			if(!UtilidadTexto.isEmpty(this.filtrosMap.get("facturaInicial").toString()) && !UtilidadTexto.isEmpty(this.filtrosMap.get("facturaFinal").toString()))
			{
				if(Utilidades.convertirAEntero(this.filtrosMap.get("facturaInicial").toString()) <= 0)
					errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial de Facturas"));
				
				if(Utilidades.convertirAEntero(this.filtrosMap.get("facturaFinal").toString()) <= 0)
					errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final de Facturas"));
				
				if(Utilidades.convertirAEntero(this.filtrosMap.get("facturaFinal").toString()) < Utilidades.convertirAEntero(this.filtrosMap.get("facturaInicial").toString()))
					errores.add("",new ActionMessage("errors.integerMenorQue", "Factura Inicial "+this.filtrosMap.get("facturaInicial").toString(), "Factura Final "+this.filtrosMap.get("facturaFinal").toString()));
			}
			
			//Hacemos las validaciones de los campos fecha
			
			if (UtilidadTexto.isEmpty(this.filtrosMap.get("facturaInicial").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("facturaFinal").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("fechaInicial").toString ()) && UtilidadTexto.isEmpty(this.filtrosMap.get("fechaFinal").toString ()))
				errores.add("", new ActionMessage("error.facturacion.NoListarSinFechas"));
						
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaInicial").toString ())|| !UtilidadTexto.isEmpty(this.filtrosMap.get("fechaFinal").toString()))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosMap.get("fechaInicial").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.filtrosMap.get("fechaInicial").toString()));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosMap.get("fechaFinal").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.filtrosMap.get("fechaFinal").toString()));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaInicial").toString(), this.filtrosMap.get("fechaFinal").toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.filtrosMap.get("fechaInicial").toString(), "Final "+this.filtrosMap.get("fechaFinal").toString()));
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaFinal").toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.filtrosMap.get("fechaFinal").toString(), "Actual "+UtilidadFecha.getFechaActual()));
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaInicial").toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.filtrosMap.get("fechaInicial").toString(), "Actual "+UtilidadFecha.getFechaActual()));
					
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.filtrosMap.get("fechaInicial").toString(), this.filtrosMap.get("fechaFinal").toString()) >= 3)
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Consolidado de Facturación", "3", "90"));
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
	 * @return the filtrosMap
	 */
	public HashMap<String, Object> getFiltrosMap() {
		return filtrosMap;
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(HashMap<String, Object> filtrosMap) {
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
	 * @return the viasIngreso
	 */
	public ArrayList<HashMap> getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(ArrayList<HashMap> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}
	
	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap> getConvenios() {
		return convenios;
	}

	/**
	 * @return the tiposSoportesMap
	 */
	public HashMap<String, Object> getTiposSoportesMap() {
		return tiposSoportesMap;
	}

	/**
	 * @param tiposSoportesMap the tiposSoportesMap to set
	 */
	public void setTiposSoportesMap(HashMap<String, Object> tiposSoportesMap) {
		this.tiposSoportesMap = tiposSoportesMap;
	}
	
	/**
	 * @return the tiposSoportesMap
	 */
	public Object getTiposSoportesMap(String llave) {
		return tiposSoportesMap.get(llave);
	}

	/**
	 * @param tiposSoportesMap the tiposSoportesMap to set
	 */
	public void setTiposSoportesMap(String llave, Object obj) {
		this.tiposSoportesMap.put(llave, obj);
	}

	/**
	 * @return the listadoFacturasMap
	 */
	public HashMap<String, Object> getListadoFacturasMap() {
		return listadoFacturasMap;
	}

	/**
	 * @param listadoFacturasMap the listadoFacturasMap to set
	 */
	public void setListadoFacturasMap(HashMap<String, Object> listadoFacturasMap) {
		this.listadoFacturasMap = listadoFacturasMap;
	}
	
	/**
	 * @return the listadoFacturasMap
	 */
	public Object getListadoFacturasMap(String llave) {
		return listadoFacturasMap.get(llave);
	}

	/**
	 * @param listadoFacturasMap the listadoFacturasMap to set
	 */
	public void setListadoFacturasMap(String llave, Object obj) {
		this.listadoFacturasMap.put(llave, obj);
	}
	
	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap> convenios) {
		this.convenios = convenios;
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
	 * @return the solicitudesFacturaMap
	 */
	public HashMap<String, Object> getSolicitudesFacturaMap() {
		return solicitudesFacturaMap;
	}

	/**
	 * @param solicitudesFacturaMap the solicitudesFacturaMap to set
	 */
	public void setSolicitudesFacturaMap(
			HashMap<String, Object> solicitudesFacturaMap) {
		this.solicitudesFacturaMap = solicitudesFacturaMap;
	}
	
	
}