package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.carteraPaciente.DtoAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDocsAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoMotivosAutSaldoMora;

import util.ConstantesBD;
import util.ResultadoBoolean;

public class AutorizacionIngresoPacienteSaldoMoraForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AutorizacionIngresoPacienteSaldoMoraForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	public String mensaje;
	
	public ArrayList<DtoDocumentosGarantia> listadoDocs;
	
	public ArrayList<DtoCuotasDatosFinanciacion> listadoCuotas;
	
	public boolean pacienteEnMora;
	
	public String seccionDetalleCuotas;
	
	public int indiceDocs;
	
	public DtoAutorizacionSaldoMora dtoAutorizacion;
	
	public ArrayList<HashMap> viasIngreso;
	
	public HashMap usuarios;
	
	public ArrayList<DtoMotivosAutSaldoMora> listadoMotivos;
	
	public ArrayList<HashMap<String,Object>> tiposPaciente;
	
	public ArrayList<DtoAutorizacionSaldoMora> listadoExistente;
	
	ArrayList<HashMap<String, Object>> listaTiposId;
	
	public ArrayList<DtoAutorizacionSaldoMora> listadoBusqueda;
	
	public int numRegistros;
	
	public DtoDocsAutorizacionSaldoMora dtoDocsSaldoMora;
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
    	ActionErrors errores = new ActionErrors();
    	    	
    	return errores;
	}
	
	
	public void reset()
	{
		this.estado="";
		this.mensaje="";
		this.listadoDocs=new ArrayList<DtoDocumentosGarantia>();
		this.listadoCuotas=new ArrayList<DtoCuotasDatosFinanciacion>();
		this.pacienteEnMora=false;
		this.seccionDetalleCuotas=ConstantesBD.acronimoNo;
		this.indiceDocs=ConstantesBD.codigoNuncaValido;
		this.dtoAutorizacion=new DtoAutorizacionSaldoMora();
		this.viasIngreso=new ArrayList<HashMap>();
		this.usuarios=new HashMap();
		this.listadoMotivos=new ArrayList<DtoMotivosAutSaldoMora>();
		this.tiposPaciente=new ArrayList<HashMap<String,Object>>();
		this.listadoExistente=new ArrayList<DtoAutorizacionSaldoMora>();
		this.listaTiposId=new ArrayList<HashMap<String,Object>>();
		this.listadoBusqueda=new ArrayList<DtoAutorizacionSaldoMora>();
		this.numRegistros=ConstantesBD.codigoNuncaValido;
		this.dtoDocsSaldoMora=new DtoDocsAutorizacionSaldoMora();
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


	public ArrayList<DtoDocumentosGarantia> getListadoDocs() {
		return listadoDocs;
	}


	public void setListadoDocs(ArrayList<DtoDocumentosGarantia> listadoDocs) {
		this.listadoDocs = listadoDocs;
	}


	public ArrayList<DtoCuotasDatosFinanciacion> getListadoCuotas() {
		return listadoCuotas;
	}


	public void setListadoCuotas(ArrayList<DtoCuotasDatosFinanciacion> listadoCuotas) {
		this.listadoCuotas = listadoCuotas;
	}


	public boolean isPacienteEnMora() {
		return pacienteEnMora;
	}


	public void setPacienteEnMora(boolean pacienteEnMora) {
		this.pacienteEnMora = pacienteEnMora;
	}


	public String getSeccionDetalleCuotas() {
		return seccionDetalleCuotas;
	}


	public void setSeccionDetalleCuotas(String seccionDetalleCuotas) {
		this.seccionDetalleCuotas = seccionDetalleCuotas;
	}


	public int getIndiceDocs() {
		return indiceDocs;
	}


	public void setIndiceDocs(int indiceDocs) {
		this.indiceDocs = indiceDocs;
	}


	public DtoAutorizacionSaldoMora getDtoAutorizacion() {
		return dtoAutorizacion;
	}


	public void setDtoAutorizacion(DtoAutorizacionSaldoMora dtoAutorizacion) {
		this.dtoAutorizacion = dtoAutorizacion;
	}


	public ArrayList<HashMap> getViasIngreso() {
		return viasIngreso;
	}


	public void setViasIngreso(ArrayList<HashMap> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}


	public HashMap getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(HashMap usuarios) {
		this.usuarios = usuarios;
	}


	public ArrayList<DtoMotivosAutSaldoMora> getListadoMotivos() {
		return listadoMotivos;
	}


	public void setListadoMotivos(ArrayList<DtoMotivosAutSaldoMora> listadoMotivos) {
		this.listadoMotivos = listadoMotivos;
	}


	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}


	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}


	public ArrayList<DtoAutorizacionSaldoMora> getListadoExistente() {
		return listadoExistente;
	}


	public void setListadoExistente(
			ArrayList<DtoAutorizacionSaldoMora> listadoExistente) {
		this.listadoExistente = listadoExistente;
	}


	public ArrayList<HashMap<String, Object>> getListaTiposId() {
		return listaTiposId;
	}


	public void setListaTiposId(ArrayList<HashMap<String, Object>> listaTiposId) {
		this.listaTiposId = listaTiposId;
	}


	public ArrayList<DtoAutorizacionSaldoMora> getListadoBusqueda() {
		return listadoBusqueda;
	}


	public void setListadoBusqueda(
			ArrayList<DtoAutorizacionSaldoMora> listadoBusqueda) {
		this.listadoBusqueda = listadoBusqueda;
	}


	public int getNumRegistros() {
		return numRegistros;
	}


	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}


	public DtoDocsAutorizacionSaldoMora getDtoDocsSaldoMora() {
		return dtoDocsSaldoMora;
	}


	public void setDtoDocsSaldoMora(DtoDocsAutorizacionSaldoMora dtoDocsSaldoMora) {
		this.dtoDocsSaldoMora = dtoDocsSaldoMora;
	}
}