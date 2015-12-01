package com.princetonsa.actionform.interfaz;
import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.actionform.interfaz.ParamInterfazSistema1EForm;
import com.princetonsa.dto.interfaz.DtoDocumentoDesmarcar;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;
import com.princetonsa.mundo.UsuarioBasico;

public class ConsultaInterfazSistema1EForm extends ValidatorForm
{
	//	--------------------Atributos
	String estado;
	DtoLogInterfaz1E dtoLogInterfaz;
	HashMap filtros;
	HashMap Docs;
	ArrayList <DtoDocumentoDesmarcar> arrayDocs;
	ArrayList <DtoLogInterfaz1E> arrayLog;
	HashMap contenidoArchivo;
	String rutaInd;
	String nombreInd;
	boolean selectActivo;
	//	--------------- Fin Atributos
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request);
    	if (this.estado.equals("buscar"))
    	{
    		if (filtros.get("fechaProceso").toString().isEmpty())
    			errores.add(filtros.get("fechaProceso").toString(), new ActionMessage("errors.required","La Fecha de Inicio de Proceso "));
    		
    		if (filtros.get("fechaFinProceso").toString().isEmpty())
    			errores.add(filtros.get("fechaFinProceso").toString(), new ActionMessage("errors.required","La Fecha Final de Proceso "));
    		
    		if (!filtros.get("tipoMovimiento").toString().isEmpty()
    				&&filtros.get("tipoDocumento").toString().isEmpty()
    					&&filtros.get("tipoProceso").toString().equals(ConstantesIntegridadDominio.acronimoTipoProcesoDesmarcar))
	    		{
	    			errores.add(filtros.get("fechaFinProceso").toString(), new ActionMessage("errors.required","El tipo de documento  "));
	    		}
    		
    		if (!UtilidadTexto.isEmpty(filtros.get("fechaProceso").toString()))
	    	{
    			if(!UtilidadFecha.esFechaValidaSegunAp(filtros.get("fechaProceso").toString()))			
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "de Inicio de Proceso "+filtros.get("fechaProceso").toString()));
    			if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), filtros.get("fechaProceso").toString()))
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "La Fecha de Inicio de Proceso "+filtros.get("fechaProceso").toString(), "la fecha actual "+UtilidadFecha.getFechaActual()));
	    	}
    		
    		if (!UtilidadTexto.isEmpty(filtros.get("fechaFinProceso").toString()))
	    	{
    			if(!UtilidadFecha.esFechaValidaSegunAp(filtros.get("fechaFinProceso").toString()))			
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", " Fin de Proceso "+filtros.get("fechaFinProceso").toString()));
	    	}
    		
    		if (!UtilidadTexto.isEmpty(filtros.get("fechaProceso").toString())&&!UtilidadTexto.isEmpty(filtros.get("fechaFinProceso").toString()))
    		{
    			if(UtilidadFecha.esFechaMenorQueOtraReferencia(filtros.get("fechaFinProceso").toString(), filtros.get("fechaProceso").toString()))
					errores.add("", new ActionMessage("errors.debeSerNumeroMayorIgual", "La fecha final de proceso "+filtros.get("fechaFinProceso").toString(), "la fecha de inicio de proceso "+filtros.get("fechaProceso").toString()));
    		}
    	}
    	
    	return errores;
	}
	
	public void reset()
	{
		this.estado="";
		this.dtoLogInterfaz=new DtoLogInterfaz1E();
		this.filtros=new HashMap();
		this.Docs=new HashMap();
		this.arrayDocs=new ArrayList<DtoDocumentoDesmarcar>();
		this.arrayLog=new ArrayList<DtoLogInterfaz1E>();
		this.contenidoArchivo=new HashMap();
		this.rutaInd="";
		this.nombreInd="";
		this.selectActivo=false;
		
	}
	
	public void resetTipoDoc()
	{
		this.arrayDocs=new ArrayList<DtoDocumentoDesmarcar>();
	}

	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DtoLogInterfaz1E getDtoLogInterfaz() {
		return dtoLogInterfaz;
	}

	public void setDtoLogInterfaz(DtoLogInterfaz1E dtoLogInterfaz) {
		this.dtoLogInterfaz = dtoLogInterfaz;
	}

	public HashMap getFiltros() {
		return filtros;
	}

	public void setFiltros(HashMap filtros) {
		this.filtros = filtros;
	}

	public HashMap getDocs() {
		return Docs;
	}

	public void setDocs(HashMap docs) {
		Docs = docs;
	}

	public ArrayList<DtoDocumentoDesmarcar> getArrayDocs() {
		return arrayDocs;
	}

	public void setArrayDocs(ArrayList<DtoDocumentoDesmarcar> arrayDocs) {
		this.arrayDocs = arrayDocs;
	}
	
	public String getFiltros(String llave) {
		return filtros.get(llave).toString();
	}

	public ArrayList<DtoLogInterfaz1E> getArrayLog() {
		return arrayLog;
	}

	public void setArrayLog(ArrayList<DtoLogInterfaz1E> arrayLog) {
		this.arrayLog = arrayLog;
	}

	public HashMap getContenidoArchivo() {
		return contenidoArchivo;
	}

	public void setContenidoArchivo(HashMap contenidoArchivo) {
		this.contenidoArchivo = contenidoArchivo;
	}

	public String getRutaInd() {
		return rutaInd;
	}

	public void setRutaInd(String rutaInd) {
		this.rutaInd = rutaInd;
	}

	public String getNombreInd() {
		return nombreInd;
	}

	public void setNombreInd(String nombreInd) {
		this.nombreInd = nombreInd;
	}

	public boolean isSelectActivo() {
		return selectActivo;
	}

	public void setSelectActivo(boolean selectActivo) {
		this.selectActivo = selectActivo;
	}
}