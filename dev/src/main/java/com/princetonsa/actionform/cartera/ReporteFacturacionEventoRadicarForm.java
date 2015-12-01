package com.princetonsa.actionform.cartera;

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
import util.Utilidades;


public class ReporteFacturacionEventoRadicarForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ReporteFacturacionEventoRadicarForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
		
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Arreglos para almacenar los valores del select 
	 */
	private ArrayList<HashMap<String, Object>> listaConvenio;
	private ArrayList<HashMap<String, Object>> listaCentrosAtencion;	
	private ArrayList<HashMap<String, Object>> listaViaIngreso;
	
	/**
	 * Atributos que manejan los criterios de generaci�n para el reporte 
	 */
	private String centroAtencion;
	private String convenio;
	private String fechaElabIni;
	private String fechaElabFin;
	private String factIni;
	private String factFin;
	private String viaIngreso;
	private String tipoReporte;
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
	
	private int numReg;
	
	
	/**
	 * Metodo que resetea las variables de la Forma
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";		
		this.convenio="-1";
		this.listaConvenio= new ArrayList<HashMap<String, Object>>();
		this.centroAtencion="";
		this.listaCentrosAtencion= new ArrayList<HashMap<String, Object>>();
		this.fechaElabIni="";
		this.fechaElabFin="";
		this.factIni="";
		this.factFin="";
		this.viaIngreso="";
		this.tipoReporte="";
		this.tipoSalida="";
		this.ruta="";
		this.urlArchivo="";
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.criteriosConsulta="";
		this.numReg=ConstantesBD.codigoNuncaValido;
	}
	
	public int getNumReg() {
		return numReg;
	}

	public void setNumReg(int numReg) {
		this.numReg = numReg;
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
	
	public ArrayList<HashMap<String, Object>> getListaConvenio() {
		return listaConvenio;
	}

	public void setListaConvenio(ArrayList<HashMap<String, Object>> listaConvenio) {
		this.listaConvenio = listaConvenio;
	}

	public ArrayList<HashMap<String, Object>> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	public void setListaCentrosAtencion(
			ArrayList<HashMap<String, Object>> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	public ArrayList<HashMap<String, Object>> getListaViaIngreso() {
		return listaViaIngreso;
	}

	public void setListaViaIngreso(
			ArrayList<HashMap<String, Object>> listaViaIngreso) {
		this.listaViaIngreso = listaViaIngreso;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getFechaElabIni() {
		return fechaElabIni;
	}

	public void setFechaElabIni(String fechaElabIni) {
		this.fechaElabIni = fechaElabIni;
	}

	public String getFechaElabFin() {
		return fechaElabFin;
	}

	public void setFechaElabFin(String fechaElabFin) {
		this.fechaElabFin = fechaElabFin;
	}

	public String getFactIni() {
		return factIni;
	}

	public void setFactIni(String factIni) {
		this.factIni = factIni;
	}

	public String getFactFin() {
		return factFin;
	}

	public void setFactFin(String factFin) {
		this.factFin = factFin;
	}

	public String getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
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
        String fecha=UtilidadFecha.getFechaActual(), fechaaux="";
                
        if(this.estado.equals("Imprimir"))
        {          	
        	if(this.tipoReporte.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Reporte "));
        	if(this.tipoSalida.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Salida "));
        	if(!this.centroAtencion.equals("-1") || !this.convenio.equals("-1") || !this.fechaElabIni.equals("") || !this.fechaElabFin.equals("") || 
        		!this.factIni.equals("") || !this.factFin.equals("") || !this.viaIngreso.equals("-1"))
        	{
        		if(!this.fechaElabIni.equals(""))
        		{	
            		if(!UtilidadFecha.compararFechas(fecha, "00:00", this.fechaElabIni.toString(), "00:00").isTrue())
    					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.fechaElabIni .toString(),fecha+""));
        		}
        		if(!this.fechaElabIni.equals("") && !this.fechaElabFin.equals(""))
        		{
        			if(!UtilidadFecha.compararFechas(this.fechaElabFin+"", "00:00", this.fechaElabIni+"", "00:00").isTrue())
    					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.fechaElabIni .toString(),this.fechaElabFin+""));
        			
        			fechaaux=UtilidadFecha.incrementarDiasAFecha(this.fechaElabIni.toString(), 180, false);
					
					if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.fechaElabFin.toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha inicial y Fecha final supera los 180 d�as por lo tanto el rango elegido"));
        		}   
        		if(!this.factIni.equals("") && !this.factFin.equals(""))
        		{
        			if(Utilidades.convertirAEntero(this.fechaElabFin) < Utilidades.convertirAEntero(this.fechaElabIni))
    					errores.add("descripcion",new ActionMessage("promt.generico","La Factura Inicial debe ser Menor a la Final"));
        		}
        	}
        	else
        		errores.add("descripcion",new ActionMessage("prompt.generico","Es Requerido al Menos un Criterio para Generar el Reporte"));
        }        
        return errores;
    }   
}