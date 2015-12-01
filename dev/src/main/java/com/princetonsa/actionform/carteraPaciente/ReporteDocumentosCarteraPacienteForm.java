package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoReporteDocumentosCarteraPaciente;

public class ReporteDocumentosCarteraPacienteForm extends ValidatorForm
{
	public String estado;

	public String mensaje;
	
	public DtoDocumentosGarantia dtoDocumentos;
	
	public HashMap centrosAtencion;
	
	public String tipoSalida;
	
	private String urlArchivoPlano;
	
	private String pathArchivoPlano;
	
	public ArrayList<DtoReporteDocumentosCarteraPaciente> listadoDocs;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request);
    	
    	if (this.estado.equals("consultarDocs"))
    	{
    		if (!this.dtoDocumentos.getFechaGen().equals("")&&!UtilidadFecha.esFechaValidaSegunAp(this.dtoDocumentos.getFechaGen()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial Generación "+this.dtoDocumentos.getFechaGen() ));
				
			if (!this.dtoDocumentos.getFechaGenFinal().equals("")&&!UtilidadFecha.esFechaValidaSegunAp(this.dtoDocumentos.getFechaGenFinal()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final Generación "+this.dtoDocumentos.getFechaGenFinal()));
			
			if (!this.dtoDocumentos.getFechaGen().equals("")&&this.dtoDocumentos.getFechaGenFinal().equals(""))
				errores.add("",new ActionMessage("errors.required", "La fecha final "));
			
			if (this.dtoDocumentos.getFechaGen().equals("")&&!this.dtoDocumentos.getFechaGenFinal().equals(""))
				errores.add("",new ActionMessage("errors.required", "La fecha inicial "));
			
			if ((!this.dtoDocumentos.getFechaGen().equals("")&&UtilidadFecha.esFechaValidaSegunAp(this.dtoDocumentos.getFechaGen()))
					&&!this.dtoDocumentos.getFechaGenFinal().equals("")&&UtilidadFecha.esFechaValidaSegunAp(this.dtoDocumentos.getFechaGenFinal()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.dtoDocumentos.getFechaGen(), this.dtoDocumentos.getFechaGenFinal()))
					errores.add("", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final "+this.dtoDocumentos.getFechaGenFinal(),"Inicial  "+this.dtoDocumentos.getFechaGen()));
			}
			
			if (this.tipoSalida.equals(""))
				errores.add("",new ActionMessage("errors.required", "El tipo de salida "));
    	}
    
    	
    	return errores;
	}
	
	public void clean()
	{
		this.estado="";
		this.mensaje="";
		this.dtoDocumentos=new DtoDocumentosGarantia();
		this.centrosAtencion=new HashMap();
		this.tipoSalida="";
		this.listadoDocs=new ArrayList<DtoReporteDocumentosCarteraPaciente>();
		this.urlArchivoPlano="";
		this.pathArchivoPlano="";
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

	public DtoDocumentosGarantia getDtoDocumentos() {
		return dtoDocumentos;
	}

	public void setDtoDocumentos(DtoDocumentosGarantia dtoDocumentos) {
		this.dtoDocumentos = dtoDocumentos;
	}

	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}

	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public ArrayList<DtoReporteDocumentosCarteraPaciente> getListadoDocs() {
		return listadoDocs;
	}

	public void setListadoDocs(
			ArrayList<DtoReporteDocumentosCarteraPaciente> listadoDocs) {
		this.listadoDocs = listadoDocs;
	}

	public String getUrlArchivoPlano() {
		return urlArchivoPlano;
	}

	public void setUrlArchivoPlano(String urlArchivoPlano) {
		this.urlArchivoPlano = urlArchivoPlano;
	}

	public String getPathArchivoPlano() {
		return pathArchivoPlano;
	}

	public void setPathArchivoPlano(String pathArchivoPlano) {
		this.pathArchivoPlano = pathArchivoPlano;
	}
}