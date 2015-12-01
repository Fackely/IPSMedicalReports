package com.princetonsa.actionform.glosas;

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

import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;

public class ConsultarImprimirRespuestasForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Variable para guardar las Glosas 
	 */
	private HashMap<String, Object> filtrosMap;
	
	/**
	 * Variable para guardar  los convenios
	 */
	private ArrayList<HashMap> convenios;
	
	/**
	 * Variable para almacenar los contratos
	 */
	private ArrayList<HashMap> contratos;
	
	/**
	 * Variable para guardar las Glosas 
	 */
	private ArrayList<DtoGlosa> arrayListGlosas;
	
	/**
	 *
	 */
	private DtoGlosa glosa;
	
	/**
	 * 
	 */
	private DtoRespuestaFacturaGlosa respuestaFacturaGlosa;
	
	/**
	 * Variable para saber que tipo de salida desea el usuario; reporte en pdf ó archivo plano
	 */
	private String tipoSalida;
	
	/**
	 * Variable para guardar la URL del archivo plano 
	 */
	private String urlArchivoPlano;
	
	/**
	 * Variable para guardar el path del archivo plano
	 */
	private String pathArchivoPlano;
	
	/**
	 *  Posicion del array
	 */
	private int posArray;
	
	private String esDescendente;
	
	private String propiedadOrdenar;
	
	public void reset()
	{
		this.filtrosMap = new HashMap<String, Object>();
		this.filtrosMap.put("numRegistros", "0");
		this.convenios = new ArrayList<HashMap>();
		this.contratos= new ArrayList<HashMap>();
		this.arrayListGlosas = new ArrayList<DtoGlosa>();
		this.glosa = new DtoGlosa();
		this.tipoSalida="";
		this.urlArchivoPlano="";
		this.pathArchivoPlano="";
		this.posArray = ConstantesBD.codigoNuncaValido;
		this.respuestaFacturaGlosa = new DtoRespuestaFacturaGlosa();
		this.esDescendente="";
		this.propiedadOrdenar="";
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
			//Validadción de los campos de fecha de respuesta inicial y final
			/*if (!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResIni").toString ())&&UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResFin").toString()))
				errores.add(this.filtrosMap.get("convenio").toString(), new ActionMessage("errors.required","La Fecha de Respuesta Final"));
			
			if (UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResIni").toString ())&&!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResFin").toString()))
				errores.add(this.filtrosMap.get("convenio").toString(), new ActionMessage("errors.required","La Fecha de Respuesta Inicial"));
			*/
			if (UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResIni").toString ()) || UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResFin").toString()))
			{
				if (!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResIni").toString ())&&UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResFin").toString()))
					errores.add(this.filtrosMap.get("convenio").toString(), new ActionMessage("errors.required","La Fecha de Respuesta Inicial "));
				if (UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResIni").toString ())&&!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResFin").toString()))
					errores.add(this.filtrosMap.get("convenio").toString(), new ActionMessage("errors.required","La Fecha de Respuesta Final  "));
			}
			
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResIni").toString ())&& !UtilidadTexto.isEmpty(this.filtrosMap.get("fechaResFin").toString()))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosMap.get("fechaResIni").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.filtrosMap.get("fechaResIni").toString()));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.filtrosMap.get("fechaResFin").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.filtrosMap.get("fechaResFin").toString()));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaResIni").toString(), this.filtrosMap.get("fechaResFin").toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.filtrosMap.get("fechaResIni").toString(), "Final "+this.filtrosMap.get("fechaResFin").toString()));
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaResFin").toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.filtrosMap.get("fechaResFin").toString(), "Actual "+UtilidadFecha.getFechaActual()));
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaResIni").toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.filtrosMap.get("fechaResIni").toString(), "Actual "+UtilidadFecha.getFechaActual()));
					
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.filtrosMap.get("fechaResIni").toString(), this.filtrosMap.get("fechaResFin").toString()) >= 6)
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar las Glosas", "6", "180"));
				}
			}
			
			//Validacion de los campos Glosa, Glosa Entidad y Convenio
			/*
			 * Validación comentada por tarea 125801
			 * 
			 * if (!UtilidadTexto.isEmpty(this.filtrosMap.get("glosa").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("glosaE").toString()))
				errores.add(this.filtrosMap.get("glosaE").toString(), new ActionMessage("errors.required","La Glosa Entidad "));*/
		
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("glosaE").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("convenio").toString()))
				errores.add(this.filtrosMap.get("convenio").toString(), new ActionMessage("errors.required","El Convenio "));
			
			//Validación del campo de fecha Notificacion
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("fechaNoti").toString()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtrosMap.get("fechaNoti").toString(), UtilidadFecha.getFechaActual().toString()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "de Notificación "+this.filtrosMap.get("fechaNoti").toString(), "Actual "+UtilidadFecha.getFechaActual()));
			}
			
			//Validación de los campos de respuesta
			if (((Utilidades.convertirAEntero(this.filtrosMap.get("resIni").toString()))>(Utilidades.convertirAEntero(this.filtrosMap.get("resFin").toString())))) 
				errores.add(this.filtrosMap.get("resFin").toString(), new ActionMessage("errors.integerMenorIgualQue","El Número de Respuesta Final ","el Número de Respuesta Inicial"));
			
			if (!UtilidadTexto.isEmpty(this.filtrosMap.get("resIni").toString()) && UtilidadTexto.isEmpty(this.filtrosMap.get("resFin").toString()))
				errores.add(this.filtrosMap.get("resIni").toString(), new ActionMessage("errors.required","El Número de Respuesta Inicial "));
			
			if (UtilidadTexto.isEmpty(this.filtrosMap.get("resIni").toString()) && !UtilidadTexto.isEmpty(this.filtrosMap.get("resFin").toString()))
				errores.add(this.filtrosMap.get("resFin").toString(), new ActionMessage("errors.required","El Número de Respuesta Final "));
       }
        return errores;
        
    }
	
    
    
	public String getEsDescendente() {
		return esDescendente;
	}



	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}



	public String getPropiedadOrdenar() {
		return propiedadOrdenar;
	}



	public void setPropiedadOrdenar(String propiedadOrdenar) {
		this.propiedadOrdenar = propiedadOrdenar;
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
	 * @return the arrayListGlosas
	 */
	public ArrayList<DtoGlosa> getArrayListGlosas() {
		return arrayListGlosas;
	}
	
	/**
	 * @return the arrayListGlosas
	 */
	public DtoGlosa getArrayListGlosas(int posArray) {
		return arrayListGlosas.get(posArray);
	}

	/**
	 * @param arrayListGlosas the arrayListGlosas to set
	 */
	public void setArrayListGlosas(ArrayList<DtoGlosa> arrayListGlosas) {
		this.arrayListGlosas = arrayListGlosas;
	}

	/**
	 * @return the glosa
	 */
	public DtoGlosa getGlosa() {
		return glosa;
	}

	/**
	 * @param glosa the glosa to set
	 */
	public void setGlosa(DtoGlosa glosa) {
		this.glosa = glosa;
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
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the respuestaFacturaGlosa
	 */
	public DtoRespuestaFacturaGlosa getRespuestaFacturaGlosa() {
		return respuestaFacturaGlosa;
	}

	/**
	 * @param respuestaFacturaGlosa the respuestaFacturaGlosa to set
	 */
	public void setRespuestaFacturaGlosa(
			DtoRespuestaFacturaGlosa respuestaFacturaGlosa) {
		this.respuestaFacturaGlosa = respuestaFacturaGlosa;
	}

	

}
