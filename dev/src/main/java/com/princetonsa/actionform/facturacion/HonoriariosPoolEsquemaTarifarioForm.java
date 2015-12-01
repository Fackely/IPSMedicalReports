package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.facturacion.InfoTarifaVigente;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoHonorariosPool;
import com.princetonsa.mundo.facturacion.HonoriariosPoolEsquemaTarifario;

/**
 * 
 * @author axioma
 * 
 */
public class HonoriariosPoolEsquemaTarifarioForm extends ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String estado;
	private String estadoAnterior;
	private DtoHonorariosPool dtoHonorarios;
	private ArrayList<DtoHonorariosPool> listaHonorarios = new ArrayList<DtoHonorariosPool>();
	private int postArrayAgrupacion;
	private int postArrayServicios;
	private int posArrayEncabezado;
	private String patronOrdenar;
	
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();
	
	/**
	 *LISTA PARA CARGAR LOS GRUPO DE SERVICIOS
	 */
	private ArrayList<HashMap<String, Object>> listaGruposDeServicios = new ArrayList<HashMap<String, Object>>();
	/**
	 * LISTA DE TIPO DE SERVICIOS
	 */
	private ArrayList<HashMap<String, Object>> listaTiposDeServicios = new ArrayList<HashMap<String, Object>>();
	/**
	 * LISTA POOLES
	 */
	private ArrayList<InfoDatosStr> listaPooles = new ArrayList<InfoDatosStr>();

	/**
	 * LISTA CARGAR ESQUEMAS TARIFARIOS
	 */
	private ArrayList<HashMap<String, Object>> listaEsquemasTarifarios = new ArrayList<HashMap<String, Object>>();

	/**
	 * LISTA CONVENIOS
	 */
	private ArrayList<HashMap<String, Object>> listConvenios = new ArrayList<HashMap<String, Object>>();

	/**
	 * ATRIBUTO PARA SABER SI ES CONVENIO
	 */
	private boolean esConvenio;

	/**
	 * 
	 */
	private InfoTarifaVigente infoTarifa = new InfoTarifaVigente();

	/**
	 * 
	 */
	private boolean mensajeExitoso;
	
	/**
	 * 
	 */
	public void reset() {
		this.estado = "";
		this.estadoAnterior = "";
		this.dtoHonorarios = new DtoHonorariosPool();
		this.listaHonorarios = new ArrayList<DtoHonorariosPool>();
		this.listaPooles = new ArrayList<InfoDatosStr>();
		this.listaEsquemasTarifarios = new ArrayList<HashMap<String, Object>>();
		this.listConvenios = new ArrayList<HashMap<String, Object>>();
		this.esConvenio = false;
		this.listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();
		this.patronOrdenar="";
		this.posArrayEncabezado= ConstantesBD.codigoNuncaValido;
		this.mensajeExitoso= false;
		resetDetalle();
	}

	
	public void resetDetalle()
	{
		this.postArrayAgrupacion = ConstantesBD.codigoNuncaValido;
		this.postArrayServicios = ConstantesBD.codigoNuncaValido;
		this.listaGruposDeServicios = new ArrayList<HashMap<String, Object>>();
		this.listaTiposDeServicios = new ArrayList<HashMap<String, Object>>();
		this.infoTarifa = new InfoTarifaVigente();
	}
	
	
	/**
	 * FORMATEA LA LISTA Y LOS OBJETOS SERVICIOS Y GRUPOS
	 */
	public void reset2() {
		this.dtoHonorarios.reset2();
	}

	/**
	 * FORMATEA LOS OBJETOS
	 */
	public void reset3() {
		this.dtoHonorarios.reset3();
		this.infoTarifa= new InfoTarifaVigente(); //LIMPIAR INFO TARIFA
	}

	public ArrayList<HashMap<String, Object>> getListaGruposDeServicios() {
		return listaGruposDeServicios;
	}

	public void setListaGruposDeServicios(
			ArrayList<HashMap<String, Object>> listaGruposDeServicios) {
		this.listaGruposDeServicios = listaGruposDeServicios;
	}

	public ArrayList<HashMap<String, Object>> getListaTiposDeServicios() {
		return listaTiposDeServicios;
	}

	public void setListaTiposDeServicios(
			ArrayList<HashMap<String, Object>> listaTiposDeServicios) {
		this.listaTiposDeServicios = listaTiposDeServicios;
	}

	/**
	 *VALIDACION DE ERRORES
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();

		if(this.getEstado().equals("guardarModificar") || this.getEstado().equals("guardar"))
		{	
			this.validar(errores);
			if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardarModificar"))
				{	
					this.setEstado("mostrarErroresGuardarModificar");
				}
				else if(this.getEstado().equals("guardar"))
				{
					this.setEstado("mostrarErroresGuardar");
				}
			}
		}
		else if(this.getEstado().equals("eliminar"))
		{
			if(HonoriariosPoolEsquemaTarifario.existenDetalles(this.listaHonorarios.get(this.posArrayEncabezado).getCodigoPk()))
			{
				errores.add("", new ActionMessage("errors.existeDetalle", "Honorario Pool"));
			}
		}
		if(!errores.isEmpty())
		{
			this.setMensajeExitoso(false);
		}
		
		return errores;
	}

	/***
	 * 
	 * @param errores
	 */
	private void validar(ActionErrors errores) 
	{
		if (this.dtoHonorarios.getPool().getCodigo() <= 0) 
		{
			errores.add("", new ActionMessage("errors.required", " El Pool"));
		}
		if (this.esConvenio) 
		{
			if (this.dtoHonorarios.getConvenio().getCodigo() == 0) 
			{
				errores.add("", new ActionMessage("errors.required"," El Convenio"));
			}
		} 
		else 
		{
			if (this.getDtoHonorarios().getEsquemaTarifario().getCodigo() == 0) 
			{
				errores.add("", new ActionMessage("errors.required","El Esquema Tarifario"));
			}
		}
		if(this.getDtoHonorarios().getCentroAtencion().getCodigo()==0)
		{
			errores.add("", new ActionMessage("errors.required","El Centro Atencion"));
		}
		if(errores.isEmpty())
		{
			if(HonoriariosPoolEsquemaTarifario.existeHonorarioPool(	this.getDtoHonorarios().getPool().getCodigo(), 
																	this.getDtoHonorarios().getConvenio().getCodigo(), 
																	this.getDtoHonorarios().getEsquemaTarifario().getCodigo(), 
																	this.getDtoHonorarios().getCentroAtencion().getCodigo(), 
																	this.getDtoHonorarios().getCodigoPk()))
			{
				errores.add("", new ActionMessage("errors.yaExiste","El pool,"+(this.isEsConvenio()?"Convenio":"Esquema Tarifario")+", Centro de Atención"));
			}
		}
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DtoHonorariosPool getDtoHonorarios() {
		return dtoHonorarios;
	}

	public void setDtoHonorarios(DtoHonorariosPool dtoHonorarios) {
		this.dtoHonorarios = dtoHonorarios;
	}

	public ArrayList<DtoHonorariosPool> getListaHonorarios() {
		return listaHonorarios;
	}

	public void setListaHonorarios(ArrayList<DtoHonorariosPool> listaHonorarios) {
		this.listaHonorarios = listaHonorarios;
	}

	public void setListaPooles(ArrayList<InfoDatosStr> listaPooles) {
		this.listaPooles = listaPooles;
	}

	public ArrayList<InfoDatosStr> getListaPooles() {
		return listaPooles;
	}

	public void setListaEsquemasTarifarios(
			ArrayList<HashMap<String, Object>> listaEsquemasTarifarios) {
		this.listaEsquemasTarifarios = listaEsquemasTarifarios;
	}

	public ArrayList<HashMap<String, Object>> getListaEsquemasTarifarios() {
		return listaEsquemasTarifarios;
	}

	public void setPostArrayAgrupacion(int postArrayAgrupacion) {
		this.postArrayAgrupacion = postArrayAgrupacion;
	}

	public int getPostArrayAgrupacion() {
		return postArrayAgrupacion;
	}

	public void setPostArrayServicios(int postArrayServicios) {
		this.postArrayServicios = postArrayServicios;
	}

	public int getPostArrayServicios() {
		return postArrayServicios;
	}

	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setListConvenios(
			ArrayList<HashMap<String, Object>> listConvenios) {
		this.listConvenios = listConvenios;
	}

	public ArrayList<HashMap<String, Object>> getListConvenios() {
		return listConvenios;
	}

	public void setEsConvenio(boolean esConvenio) {
		this.esConvenio = esConvenio;
	}

	public boolean isEsConvenio() {
		return esConvenio;
	}

	public boolean getEsConvenio() {
		return esConvenio;
	}

	public void setInfoTarifa(InfoTarifaVigente infoTarifa) {
		this.infoTarifa = infoTarifa;
	}

	public InfoTarifaVigente getInfoTarifa() {
		return infoTarifa;
	}

	/**
	 * @return the listaCentrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	/**
	 * @param listaCentrosAtencion
	 *            the listaCentrosAtencion to set
	 */
	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
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
	 * @return the posArrayEncabezado
	 */
	public int getPosArrayEncabezado() {
		return posArrayEncabezado;
	}

	/**
	 * @param posArrayEncabezado the posArrayEncabezado to set
	 */
	public void setPosArrayEncabezado(int posArrayEncabezado) {
		this.posArrayEncabezado = posArrayEncabezado;
	}

	/**
	 * @return the mensajeExitoso
	 */
	public boolean isMensajeExitoso() {
		return mensajeExitoso;
	}

	/**
	 * @return the mensajeExitoso
	 */
	public boolean getMensajeExitoso() {
		return mensajeExitoso;
	}
	
	/**
	 * @param mensajeExitoso the mensajeExitoso to set
	 */
	public void setMensajeExitoso(boolean mensajeExitoso) {
		this.mensajeExitoso = mensajeExitoso;
	}

}