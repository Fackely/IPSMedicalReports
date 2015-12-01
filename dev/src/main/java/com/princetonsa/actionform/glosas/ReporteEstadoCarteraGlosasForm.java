package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;


public class ReporteEstadoCarteraGlosasForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ReporteEstadoCarteraGlosasForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
		
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Arreglo que almacena los tipos de convenio
	 */
	private ArrayList listaTiposConvenio;
	
	/**
	 * Arreglo que almacna los convenios
	 */
	private ArrayList<HashMap<String, Object>> convenios;
	
	/**
	 * Atributos que manejan los criterios de busqueda para el reporte 
	 */
	private String fechaCorte;
	private String tipoReporte;
	private String tipoConvenio;
	private String convenio;
	private String tipoSalida;
	
	
	/**
	 * para la impresion de los reportes
	 */
	private String ruta="";
	private String urlArchivo="";
	private boolean existeArchivo=false;
	private boolean operacionTrue = false;
	
	/**
	 * Cadena que almacena los criterios de consulta para el archivo plano
	 */
	private String criteriosConsulta;	
	
	
	/**
	 * Metodo que resetea las variables de la Forma
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";		
		this.fechaCorte="";
		this.tipoReporte="";
		this.tipoConvenio="-1";
		this.convenio="-1";
		this.tipoSalida="";
		this.listaTiposConvenio= new ArrayList();
		this.convenios= new ArrayList<HashMap<String, Object>>();
		this.ruta="";
		this.urlArchivo="";
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.criteriosConsulta="";
	}
	
	public String getCriteriosConsulta() {
		return criteriosConsulta;
	}

	public void setCriteriosConsulta(String criteriosConsulta) {
		this.criteriosConsulta = criteriosConsulta;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getUrlArchivo() {
		return urlArchivo;
	}

	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}

	public boolean isExisteArchivo() {
		return existeArchivo;
	}

	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}

	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getTipoConvenio() {
		return tipoConvenio;
	}

	public void setTipoConvenio(String tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	public ArrayList getListaTiposConvenio() {
		return listaTiposConvenio;
	}

	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	public void setListaTiposConvenio(ArrayList listaTiposConvenio) {
		this.listaTiposConvenio = listaTiposConvenio;
	}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public String getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
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
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        String fecha=UtilidadFecha.getFechaActual();
        
        if(this.estado.equals("Imprimir"))
        {
        	if(this.fechaCorte.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Corte "));
        	else
        	{
        		if(!UtilidadFecha.compararFechas(fecha, "00:00", this.fechaCorte.toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Corte "+this.fechaCorte.toString(),fecha+""));
        	}
        	if(this.tipoReporte.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Reporte "));
        	if(this.tipoSalida.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Salida "));
        }        
        return errores;
    }   
}