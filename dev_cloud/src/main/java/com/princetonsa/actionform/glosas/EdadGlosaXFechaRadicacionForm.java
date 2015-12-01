package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;

import com.princetonsa.dto.glosas.DtoGlosa;

public class EdadGlosaXFechaRadicacionForm extends ValidatorForm 
{
	private String estado;
	
	private String mensaje;
	
	private DtoGlosa glosaBusqueda;
	
	private String tipoSalida;
	
	private String tipoReporte;
	
	private ArrayList<HashMap> tiposConvenios;
	
	private ArrayList<HashMap> convenios;
	
	private int tipoConvenio;
	
	private ArrayList<DtoGlosa> listadoGlosas;
	
	/**
	 * informacion reporte
	 * */
	private InfoDatosString  infoCvsArchivo; 
	
	/**
	 * Cadena que almacena los criterios de consulta para el archivo plano
	 */
	private String criteriosConsulta;	
	
	/**
	 * para la impresion de los reportes
	 */
	private String ruta="";
	private String urlArchivo="";
	private boolean existeArchivo=false;
	private boolean operacionTrue = false;
	
	public void reset()
	{
		this.estado="";
		this.mensaje="";
		this.glosaBusqueda=new DtoGlosa();
		this.tipoReporte="";
		this.tipoSalida="";
		this.tiposConvenios=new ArrayList<HashMap>();
		this.tipoConvenio=ConstantesBD.codigoNuncaValido;
		this.convenios=new ArrayList<HashMap>();
		this.listadoGlosas=new ArrayList<DtoGlosa>();
		this.ruta="";
		this.urlArchivo="";
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.infoCvsArchivo = new InfoDatosString();
		this.criteriosConsulta="";
	}
	
	public String getCriteriosConsulta() {
		return criteriosConsulta;
	}

	public void setCriteriosConsulta(String criteriosConsulta) {
		this.criteriosConsulta = criteriosConsulta;
	}

	public InfoDatosString getInfoCvsArchivo() {
		return infoCvsArchivo;
	}

	public void setInfoCvsArchivo(InfoDatosString infoCvsArchivo) {
		this.infoCvsArchivo = infoCvsArchivo;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public DtoGlosa getGlosaBusqueda() {
		return glosaBusqueda;
	}

	public void setGlosaBusqueda(DtoGlosa glosaBusqueda) {
		this.glosaBusqueda = glosaBusqueda;
	}

	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public ArrayList<HashMap> getTiposConvenios() {
		return tiposConvenios;
	}

	public void setTiposConvenios(ArrayList<HashMap> tiposConvenios) {
		this.tiposConvenios = tiposConvenios;
	}

	public int getTipoConvenio() {
		return tipoConvenio;
	}

	public void setTipoConvenio(int tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	public ArrayList<HashMap> getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList<HashMap> convenios) {
		this.convenios = convenios;
	}

	public ArrayList<DtoGlosa> getListadoGlosas() {
		return listadoGlosas;
	}

	public void setListadoGlosas(ArrayList<DtoGlosa> listadoGlosas) {
		this.listadoGlosas = listadoGlosas;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        String fecha=UtilidadFecha.getFechaActual();
        
        if(this.estado.equals("generarReporte"))
        {        	
        	if(this.tipoReporte.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Reporte "));
        	if(this.tipoSalida.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Salida "));
        	if(this.glosaBusqueda.getFechaRegistroGlosa().equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Corte "));
        	else{
        		if(!UtilidadFecha.compararFechas(fecha, "00:00", this.glosaBusqueda.getFechaRegistroGlosa().toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Corte "+this.glosaBusqueda.getFechaRegistroGlosa().toString(),fecha+""));
        	}        	
        }        
        return errores;
    }   
}