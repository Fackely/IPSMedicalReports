package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * Clase para consultar e imprimir las glosas
 * Date: 2009-02-27
 * @author jfhernandez@princetonsa.com
 */
public class ConsultarImprimirGlosasForm extends ValidatorForm 
{
	private Logger logger = Logger.getLogger(ConsultarImprimirGlosasForm.class);
	
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 *Variable para guardar el listado de glosas resultantes de la busqueda 
	 */
	private HashMap<String, Object> listadoGlosasMap;
	
	/**
	 * Variable para guardar  los convenios
	 */
	private ArrayList<HashMap> convenios;
	
	/**
	 * Variable para guardar las Glosas 
	 */
	private HashMap<String, Object> filtrosMap;
	
	/**
	 * 
	 */
	private HashMap<String, Object> detalleFacturasGlosaMap;
	
	/**
	 * 
	 */
	private HashMap<String, Object> detalleSolicitudesGlosaMap;
	
	/**
	 * 
	 */
	private HashMap<String, Object> glosaMap;
	
	/**
	 * Variable para almacenar el convenio seleccionado
	 */
	private String convenio;
	
	/**
	 * Variable para almacenar los contratos
	 */
	private ArrayList<HashMap> contratos;
	
	/**
	 * Variable para almacenar el patron para ordenar el mapa de Glsoas
	 */
	private String patronOrdenar;
	
	/**
	 * Variable para almacenar el ultimo Patron con el que se ordeno el mapa de Glosas
	 */
	private String ultimoPatron;
	
	/**
	 * Variable para saber que tipo de salida desea el usuario; reporte en pdf ó archivo plano
	 */
	private String tipoSalida;
	
	/**
	 * 
	 */
	private String urlArchivoPlano;
	
		
	/**
	 * 
	 */
	private String pathArchivoPlano;
	
	/**
	 * 
	 */
	private int posMap;
	
	/**
	 * 
	 */
	private String msj; 
	
	/**
	 * Reset General
	 */
	public void reset()
	{
		this.listadoGlosasMap = new HashMap<String, Object>();
		this.listadoGlosasMap.put("numRegistros", "0");
		this.filtrosMap = new HashMap<String, Object>();
		this.filtrosMap.put("numRegistros", "0");
		this.glosaMap = new HashMap<String, Object>();
		this.convenios = new ArrayList<HashMap>();
		this.contratos=new ArrayList<HashMap>();
		this.convenio="";
		this.detalleFacturasGlosaMap = new HashMap<String, Object>();
		this.detalleFacturasGlosaMap.put("numRegistros", "0");
		this.detalleSolicitudesGlosaMap = new HashMap<String, Object>();
		this.detalleSolicitudesGlosaMap.put("numRegistros", "0");
		this.tipoSalida="";
		this.urlArchivoPlano="";
		this.pathArchivoPlano="";
		this.posMap = ConstantesBD.codigoNuncaValido;
		this.msj = "";
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
		if(this.estado.equals("cargarListadoGlosas"))
        {
			
			// Se validan los campos de fecha de Registro Inicial y Final
			logger.info("\n\nmapa: "+this.filtrosMap);
			if((this.filtrosMap.get("glosa")+"").equals(""))
			{
				if (UtilidadTexto.isEmpty(this.filtrosMap.get("fechaRegIni").toString()) && (UtilidadTexto.isEmpty((this.filtrosMap.get("fechaRegFin").toString()))))
				{
					errores.add(this.filtrosMap.get("fechaRegFin").toString(), new ActionMessage("errors.required","La Fecha de Registro Final "));
					errores.add(this.filtrosMap.get("fechaRegIni").toString(), new ActionMessage("errors.required","La Fecha de Registro Inicial "));
				}
						
				else if (!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaRegIni").toString ())|| !UtilidadTexto.isEmpty(this.filtrosMap.get("fechaRegFin").toString()))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosMap.get("fechaRegIni").toString()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.filtrosMap.get("fechaRegIni").toString()));
						centinelaErrorFechas=true;
					}
					if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosMap.get("fechaRegFin").toString()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.filtrosMap.get("fechaRegFin").toString()));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaRegIni").toString(), this.filtrosMap.get("fechaRegFin").toString()))
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.filtrosMap.get("fechaRegIni").toString(), "Final "+this.filtrosMap.get("fechaRegFin").toString()));
						
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaRegFin").toString(), UtilidadFecha.getFechaActual().toString()))
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.filtrosMap.get("fechaRegFin").toString(), "Actual "+UtilidadFecha.getFechaActual()));
						
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaRegIni").toString(), UtilidadFecha.getFechaActual().toString()))
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.filtrosMap.get("fechaRegIni").toString(), "Actual "+UtilidadFecha.getFechaActual()));
						
						if(UtilidadFecha.numeroMesesEntreFechasExacta(this.filtrosMap.get("fechaRegIni").toString(), this.filtrosMap.get("fechaRegFin").toString()) >= 6)
							errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar las Glosas", "6", "180"));
					}
				}
			}
			
			//Validacion de los campos Glosa, Glosa Gntidad y Convenio
			/*if (!UtilidadTexto.isEmpty(this.filtrosMap.get("glosa").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("glosaE").toString()))
				errores.add(this.filtrosMap.get("glosaE").toString(), new ActionMessage("errors.required","La Glosa Entidad "));
			*/
		
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("glosaE").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("convenio").toString()))
				errores.add(this.filtrosMap.get("convenio").toString(), new ActionMessage("errors.required","El Convenio "));
			
			if (UtilidadTexto.isEmpty(this.filtrosMap.get("glosaE").toString()) && !UtilidadTexto.isEmpty(this.filtrosMap.get("convenio").toString()))
				errores.add(this.filtrosMap.get("glosaE").toString(), new ActionMessage("errors.required","La Glosa Entidad "));
			
			//Validación del campo de fecha Notificacion
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaNoti").toString()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaNoti").toString(), UtilidadFecha.getFechaActual().toString()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "de Notificación "+this.filtrosMap.get("fechaNoti").toString(), "Actual "+UtilidadFecha.getFechaActual()));
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
	 * @return the listadoGlosasMap
	 */
	public HashMap<String, Object> getListadoGlosasMap() {
		return listadoGlosasMap;
	}

	/**
	 * @param listadoGlosasMap the listadoGlosasMap to set
	 */
	public void setListadoGlosasMap(HashMap<String, Object> listadoGlosasMap) {
		this.listadoGlosasMap = listadoGlosasMap;
	}

	/**
	 * @return the listadoGlosasMap
	 */
	public Object getListadoGlosasMap(String llave) {
		return listadoGlosasMap.get(llave);
	}

	/**
	 * @param listadoGlosasMap the listadoGlosasMap to set
	 */
	public void setListadoGlosasMap(String llave, Object obj) {
		this.listadoGlosasMap.put(llave, obj);
	}

	/**
	 * @return the conveniosMap
	 */
	public ArrayList<HashMap>  getConvenios() {
		return convenios;
	}

	/**
	 * @param conveniosMap the conveniosMap to set
	 */
	public void setConvenios(ArrayList<HashMap> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the contratos
	 */
	public ArrayList<HashMap> getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList<HashMap> contratos) {
		this.contratos = contratos;
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
	 * @return the detalleFacturasGlosaMap
	 */
	public HashMap<String, Object> getDetalleFacturasGlosaMap() {
		return detalleFacturasGlosaMap;
	}
	
	/**
	 * @param detalleFacturasGlosaMap the detalleFacturasGlosaMap to set
	 */
	public void setDetalleFacturasGlosaMap(
			HashMap<String, Object> detalleFacturasGlosaMap) {
		this.detalleFacturasGlosaMap = detalleFacturasGlosaMap;
	}

	/**
	 * @return the detalleSolicitudesGlosaMap
	 */
	public HashMap<String, Object> getDetalleSolicitudesGlosaMap() {
		return detalleSolicitudesGlosaMap;
	}

	/**
	 * @param detalleSolicitudesGlosaMap the detalleSolicitudesGlosaMap to set
	 */
	public void setDetalleSolicitudesGlosaMap(
			HashMap<String, Object> detalleSolicitudesGlosaMap) {
		this.detalleSolicitudesGlosaMap = detalleSolicitudesGlosaMap;
	}

	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * @param tipoSalida the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * @return the urlArchivoPlano
	 */
	public String getUrlArchivoPlano() {
		return urlArchivoPlano;
	}

	/**
	 * @param urlArchivoPlano the urlArchivoPlano to set
	 */
	public void setUrlArchivoPlano(String urlArchivoPlano) {
		this.urlArchivoPlano = urlArchivoPlano;
	}

	/**
	 * @return the pathArchivoPlano
	 */
	public String getPathArchivoPlano() {
		return pathArchivoPlano;
	}

	/**
	 * @param pathArchivoPlano the pathArchivoPlano to set
	 */
	public void setPathArchivoPlano(String pathArchivoPlano) {
		this.pathArchivoPlano = pathArchivoPlano;
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
	 * @return the glosaMap
	 */
	public HashMap<String, Object> getGlosaMap() {
		return glosaMap;
	}

	/**
	 * @param glosaMap the glosaMap to set
	 */
	public void setGlosaMap(HashMap<String, Object> glosaMap) {
		this.glosaMap = glosaMap;
	}
	
	/**
	 * @return the glosaMap
	 */
	public Object getGlosaMap(String llave) {
		return glosaMap.get(llave);
	}

	/**
	 * @param glosaMap the glosaMap to set
	 */
	public void setGlosaMap(String llave, Object obj) {
		this.glosaMap.put(llave, obj);
	}

	/**
	 * @return the msj
	 */
	public String getMsj() {
		return msj;
	}

	/**
	 * @param msj the msj to set
	 */
	public void setMsj(String msj) {
		this.msj = msj;
	}
	
	/**
	 * @return the listadoGlosasMap
	 */
	public Object getDetalleFacturasGlosaMap(String llave) {
		return detalleFacturasGlosaMap.get(llave);
	}
	
	/**
	 * @return the listadoGlosasMap
	 */
	public Object getDetalleSolicitudesGlosaMap(String llave) {
		return detalleSolicitudesGlosaMap.get(llave);
	}
}
