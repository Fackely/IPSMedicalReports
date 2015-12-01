package com.princetonsa.actionform.administracion;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;
import com.princetonsa.action.administracion.DetConceptosRetencionAction;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoDetConceptosRetencion;
import com.princetonsa.dto.administracion.DtoDetVigConRet;
import com.princetonsa.dto.administracion.DtoDetVigConRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;

import com.princetonsa.mundo.UsuarioBasico;

public class DetConceptosRetencionForm extends ValidatorForm
{
	//	--------------------Atributos
	String estado;
	String descripcion;
	String mostrarDiv;
	DtoDetConceptosRetencion dtoDetConceptosRetencion;
	DtoDetVigConRetencion dtoDetVigConRetencion;
	DtoDetVigConRetencion dtoLog;
	DtoDetVigConRet dtoDetVigConRetGrupo;
	DtoDetVigConRet dtoDetVigConRetClase;
	DtoDetVigConRet dtoDetVigConRetCfv;
	ArrayList <DtoDetConceptosRetencion> listaDetConceptosRetencion;
	ArrayList <DtoDetVigConRetencion> listaDetVigConceptosRetencion;
	ArrayList <DtoConceptosRetencion> listaConceptosRetencion;
	ArrayList <DtoDetVigConRet> listaDetConRetGrupos;
	ArrayList <DtoDetVigConRet> listaDetConRetClases;
	ArrayList <DtoDetVigConRet> listaDetConRetConceptos;
	ArrayList<HashMap<String, Object>> gruposServicio;
	ArrayList<HashMap<String, Object>> clasesInventario;
	ArrayList<HashMap<String, Object>> cfv;
	int indice;
	int numRegistrosDetVig;
	int numRegistrosListaDetConRet;
	int numRegistrosGrupos;
	int indiceDetConceptosVig;
	int indiceGrupos;
	int numRegistrosClases;
	int indiceClase=0;
	int numRegistrosConceptos=0;
	int indiceConceptos=0;
	boolean esConsulta;
	boolean modificable;
	boolean detalleModificable;
	
	
	//	--------------- Fin Atributos
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request);
    	
    	if (this.estado.equals("guardarConceptoRetencion"))
    	{
    		if (UtilidadTexto.isEmpty(this.dtoDetConceptosRetencion.getFechaVigenciaInicial()))
				errores.add(this.dtoDetConceptosRetencion.getFechaVigenciaInicial(), new ActionMessage("errors.required","La Fecha de Vigencia Incial "));
    		
    		if (UtilidadTexto.isEmpty(this.dtoDetConceptosRetencion.getTipoRetencion()))
				errores.add(this.dtoDetConceptosRetencion.getTipoRetencion(), new ActionMessage("errors.required","El Tipo de Retención "));
    		
    		if (!UtilidadTexto.isEmpty(this.dtoDetConceptosRetencion.getFechaVigenciaInicial()))
    		{
    			if(!UtilidadFecha.esFechaValidaSegunAp(this.dtoDetConceptosRetencion.getFechaVigenciaInicial()))			
    				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "de Vigencia Inicial "+this.dtoDetConceptosRetencion.getFechaVigenciaInicial()));
    		}
    	}   	
    	return errores;
	}
	
	public void reset()
	{
		this.estado="";
		this.descripcion="";
		this.mostrarDiv="hidden";
		this.dtoDetConceptosRetencion=new DtoDetConceptosRetencion();
		this.dtoDetVigConRetencion=new DtoDetVigConRetencion();
		this.dtoLog=new DtoDetVigConRetencion();
		this.dtoDetVigConRetGrupo=new DtoDetVigConRet();
		this.dtoDetVigConRetClase=new DtoDetVigConRet();
		this.dtoDetVigConRetCfv=new DtoDetVigConRet();
		this.listaDetConceptosRetencion=new ArrayList<DtoDetConceptosRetencion>();
		this.listaDetVigConceptosRetencion=new ArrayList<DtoDetVigConRetencion>();
		this.listaConceptosRetencion=new ArrayList<DtoConceptosRetencion>();
		this.listaDetConRetGrupos= new ArrayList<DtoDetVigConRet>();
		this.listaDetConRetClases=new ArrayList<DtoDetVigConRet>();
		this.listaDetConRetConceptos=new ArrayList<DtoDetVigConRet>();
		this.gruposServicio=new ArrayList<HashMap<String,Object>>();
		this.clasesInventario= new ArrayList<HashMap<String,Object>>();
		this.cfv=new ArrayList<HashMap<String,Object>>();
		this.numRegistrosListaDetConRet=0;
		this.numRegistrosDetVig=0;
		this.numRegistrosGrupos=0;
		this.indice=0;	
		this.indiceDetConceptosVig=0;
		this.indiceGrupos=0;
		this.numRegistrosClases=0;
		this.indiceClase=0;
		this.indiceConceptos=0;
		this.numRegistrosConceptos=0;
		this.esConsulta=false;
		this.modificable=false;
		this.detalleModificable=false;
	}
	
	public void resetDetConceptosretencion()
	{
		this.dtoDetConceptosRetencion=new DtoDetConceptosRetencion();
		this.descripcion="";
	}
	
	public void resetDetVigConRetencion()
	{
		this.dtoDetVigConRetencion=new DtoDetVigConRetencion();
		this.dtoLog=new DtoDetVigConRetencion();
		this.modificable=false;
	}
	
	public void resetGrupo()
	{
		this.dtoDetVigConRetGrupo=new DtoDetVigConRet();
		this.detalleModificable=false;
	}
	
	public void resetClase()
	{
		this.dtoDetVigConRetClase=new DtoDetVigConRet();
		this.detalleModificable=false;
	}
	
	public void resetConceptos()
	{
		this.dtoDetVigConRetCfv=new DtoDetVigConRet();
		this.detalleModificable=false;
	}
	
	public void empezarConsulta()
	{
		this.esConsulta=true;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public DtoDetConceptosRetencion getDtoDetConceptosRetencion() {
		return dtoDetConceptosRetencion;
	}

	public void setDtoDetConceptosRetencion(
			DtoDetConceptosRetencion dtoDetConceptosRetencion) {
		this.dtoDetConceptosRetencion = dtoDetConceptosRetencion;
	}

	public ArrayList<DtoDetConceptosRetencion> getListaDetConceptosRetencion() {
		return listaDetConceptosRetencion;
	}

	public void setListaDetConceptosRetencion(
			ArrayList<DtoDetConceptosRetencion> listaDetConceptosRetencion) {
		this.listaDetConceptosRetencion = listaDetConceptosRetencion;
	}

	public int getNumRegistrosListaDetConRet() {
		return numRegistrosListaDetConRet;
	}

	public void setNumRegistrosListaDetConRet(int numRegistrosListaDetConRet) {
		this.numRegistrosListaDetConRet = numRegistrosListaDetConRet;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public ArrayList<DtoDetVigConRetencion> getListaDetVigConceptosRetencion() {
		return listaDetVigConceptosRetencion;
	}

	public void setListaDetVigConceptosRetencion(
			ArrayList<DtoDetVigConRetencion> listaDetVigConceptosRetencion) {
		this.listaDetVigConceptosRetencion = listaDetVigConceptosRetencion;
	}

	public int getNumRegistrosDetVig() {
		return numRegistrosDetVig;
	}

	public void setNumRegistrosDetVig(int numRegistrosDetVig) {
		this.numRegistrosDetVig = numRegistrosDetVig;
	}

	public DtoDetVigConRetencion getDtoDetVigConRetencion() {
		return dtoDetVigConRetencion;
	}

	public void setDtoDetVigConRetencion(DtoDetVigConRetencion dtoDetVigConRetencion) {
		this.dtoDetVigConRetencion = dtoDetVigConRetencion;
	}

	public ArrayList<DtoConceptosRetencion> getListaConceptosRetencion() {
		return listaConceptosRetencion;
	}

	public void setListaConceptosRetencion(
			ArrayList<DtoConceptosRetencion> listaConceptosRetencion) {
		this.listaConceptosRetencion = listaConceptosRetencion;
	}

	public String getMostrarDiv() {
		return mostrarDiv;
	}

	public void setMostrarDiv(String mostrarDiv) {
		this.mostrarDiv = mostrarDiv;
	}

	public int getIndiceDetConceptosVig() {
		return indiceDetConceptosVig;
	}

	public void setIndiceDetConceptosVig(int indiceDetConceptosVig) {
		this.indiceDetConceptosVig = indiceDetConceptosVig;
	}

	public ArrayList<DtoDetVigConRet> getListaDetConRetGrupos() {
		return listaDetConRetGrupos;
	}

	public void setListaDetConRetGrupos(
			ArrayList<DtoDetVigConRet> listaDetConRetGrupos) {
		this.listaDetConRetGrupos = listaDetConRetGrupos;
	}

	public ArrayList<HashMap<String, Object>> getGruposServicio() {
		return gruposServicio;
	}

	public void setGruposServicio(ArrayList<HashMap<String, Object>> gruposServicio) {
		this.gruposServicio = gruposServicio;
	}

	public int getNumRegistrosGrupos() {
		return numRegistrosGrupos;
	}

	public void setNumRegistrosGrupos(int numRegistrosGrupos) {
		this.numRegistrosGrupos = numRegistrosGrupos;
	}

	public DtoDetVigConRet getDtoDetVigConRetGrupo() {
		return dtoDetVigConRetGrupo;
	}

	public void setDtoDetVigConRetGrupo(DtoDetVigConRet dtoDetVigConRetGrupo) {
		this.dtoDetVigConRetGrupo = dtoDetVigConRetGrupo;
	}

	public DtoDetVigConRet getDtoDetVigConRetClase() {
		return dtoDetVigConRetClase;
	}

	public void setDtoDetVigConRetClase(DtoDetVigConRet dtoDetVigConRetClase) {
		this.dtoDetVigConRetClase = dtoDetVigConRetClase;
	}

	public DtoDetVigConRet getDtoDetVigConRetCfv() {
		return dtoDetVigConRetCfv;
	}

	public void setDtoDetVigConRetCfv(DtoDetVigConRet dtoDetVigConRetCfv) {
		this.dtoDetVigConRetCfv = dtoDetVigConRetCfv;
	}

	public int getIndiceGrupos() {
		return indiceGrupos;
	}

	public void setIndiceGrupos(int indiceGrupos) {
		this.indiceGrupos = indiceGrupos;
	}

	public ArrayList<DtoDetVigConRet> getListaDetConRetClases() {
		return listaDetConRetClases;
	}

	public void setListaDetConRetClases(
			ArrayList<DtoDetVigConRet> listaDetConRetClases) {
		this.listaDetConRetClases = listaDetConRetClases;
	}

	public int getNumRegistrosClases() {
		return numRegistrosClases;
	}

	public void setNumRegistrosClases(int numRegistrosClases) {
		this.numRegistrosClases = numRegistrosClases;
	}

	public ArrayList<HashMap<String, Object>> getClasesInventario() {
		return clasesInventario;
	}

	public void setClasesInventario(
			ArrayList<HashMap<String, Object>> clasesInventario) {
		this.clasesInventario = clasesInventario;
	}

	public int getIndiceClase() {
		return indiceClase;
	}

	public void setIndiceClase(int indiceClase) {
		this.indiceClase = indiceClase;
	}

	public ArrayList<HashMap<String, Object>> getCfv() {
		return cfv;
	}

	public void setCfv(ArrayList<HashMap<String, Object>> cfv) {
		this.cfv = cfv;
	}

	public int getNumRegistrosConceptos() {
		return numRegistrosConceptos;
	}

	public void setNumRegistrosConceptos(int numRegistrosConceptos) {
		this.numRegistrosConceptos = numRegistrosConceptos;
	}

	public int getIndiceConceptos() {
		return indiceConceptos;
	}

	public void setIndiceConceptos(int indiceConceptos) {
		this.indiceConceptos = indiceConceptos;
	}

	public ArrayList<DtoDetVigConRet> getListaDetConRetConceptos() {
		return listaDetConRetConceptos;
	}

	public void setListaDetConRetConceptos(
			ArrayList<DtoDetVigConRet> listaDetConRetConceptos) {
		this.listaDetConRetConceptos = listaDetConRetConceptos;
	}

	public DtoDetVigConRetencion getDtoLog() {
		return dtoLog;
	}

	public void setDtoLog(DtoDetVigConRetencion dtoLog) {
		this.dtoLog = dtoLog;
	}

	public boolean isEsConsulta() {
		return esConsulta;
	}

	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	public boolean isModificable() {
		return modificable;
	}

	public void setModificable(boolean modificable) {
		this.modificable = modificable;
	}

	public boolean isDetalleModificable() {
		return detalleModificable;
	}

	public void setDetalleModificable(boolean detalleModificable) {
		this.detalleModificable = detalleModificable;
	}
	
	
}