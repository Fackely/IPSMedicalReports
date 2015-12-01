package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

import com.servinte.axioma.orm.BonosConvIngPac;
import com.servinte.axioma.orm.ValidacionesBdConvIngPac;


/**
 * Contiene la informacion de la seccion de convenios de ingreso del paciente
 * @author Cristhian Murillo
 */
public class DtoSeccionConvenioPaciente implements Serializable
{
	private static final long serialVersionUID = 1L;

	private long codigoConveniosIngresoPaciente;
	
	private String codigoConvenio;
	private int codigoConvenioInt;
	private String descripcionConvenio;
	
	private String codigoContrato;
	private String numeroContrato;
	
	private String activo;
	
	private ValidacionesBdConvIngPac validacionesBdConvIngPac;
	
	private BonosConvIngPac bonosConvIngPac;
	private ArrayList<BonosConvIngPac> listaBonosConvIngPac;
	
	private DtoAutorizacionConvIngPac dtoAutorizacionConvIngPac;
	private ArrayList<DtoAutorizacionConvIngPac> listaDtoAutorizacionConvIngPac;
	
	/** Indica si este registro se puede eliminar o no */
	private boolean esEliminable;
	
	/** Indica si este registro se puede modificar o no */
	private boolean esModificable;
	
	/** Indica si este convenio fue cargado por defecto */
	private Character porDefecto;
	
	/** Contiene la lista de los codigos de ConveniosIngresoPaciente a eliminar */
	private ArrayList<Long> listaConveniosIngresoEliminar;
	
	/** Contiene la lista de los codigos de bonos a eliminar */
	private ArrayList<Long> listaBonosConveniosIngresoEliminar;
	
	
	/**
	 * Constructor
	 * DtoSeccionConvenioPaciente 
	 */
	public DtoSeccionConvenioPaciente()
	{
		this.codigoConveniosIngresoPaciente	= ConstantesBD.codigoNuncaValidoLong;
		this.codigoContrato					= "";
		this.numeroContrato					= "";
		this.codigoConvenio					= "";
		this.descripcionConvenio			= "";
		this.validacionesBdConvIngPac		= new ValidacionesBdConvIngPac();
		this.dtoAutorizacionConvIngPac		= new DtoAutorizacionConvIngPac();
		this.bonosConvIngPac				= new BonosConvIngPac();
		this.listaDtoAutorizacionConvIngPac	= new ArrayList<DtoAutorizacionConvIngPac>();
		this.listaBonosConvIngPac			= new ArrayList<BonosConvIngPac>();
		this.esEliminable					= false;
		this.activo							= "";
		this.esModificable					= false;
		this.porDefecto						= null;
		this.listaConveniosIngresoEliminar	= new ArrayList<Long>();
		this.listaBonosConveniosIngresoEliminar = new ArrayList<Long>();
		this.codigoConvenioInt				= ConstantesBD.codigoNuncaValido;
	}

	
	
	/**
	 * Metodo que resetea las los registros referenciados que tiene el dto
	 */
	public void limpiarRegistrosNoBasicos()
	{
		this.validacionesBdConvIngPac		= new ValidacionesBdConvIngPac();
		this.dtoAutorizacionConvIngPac		= new DtoAutorizacionConvIngPac();
		this.bonosConvIngPac 				= new BonosConvIngPac();
		this.listaDtoAutorizacionConvIngPac = new ArrayList<DtoAutorizacionConvIngPac>();
		this.listaBonosConvIngPac			= new ArrayList<BonosConvIngPac>();
		this.listaConveniosIngresoEliminar	= new ArrayList<Long>();
		this.listaBonosConveniosIngresoEliminar = new ArrayList<Long>();
	}
	
	
	/**
	 * Metodo que resetea las autorizaciones del dto
	 */
	public void limpiarAutorizaciones()
	{
		this.dtoAutorizacionConvIngPac			= new DtoAutorizacionConvIngPac();
		this.listaDtoAutorizacionConvIngPac 	= new ArrayList<DtoAutorizacionConvIngPac>();
	}
	
	
	/**
	 * Metodo que resetea los bonos del dto
	 */
	public void limpiarBonos()
	{
		this.bonosConvIngPac 				= new BonosConvIngPac();
		this.listaDtoAutorizacionConvIngPac 	= new ArrayList<DtoAutorizacionConvIngPac>();
	}
	

	public String getCodigoContrato() {
		return codigoContrato;
	}



	public void setCodigoContrato(String codigoContrato) {
		this.codigoContrato = codigoContrato;
	}



	public String getNumeroContrato() {
		return numeroContrato;
	}



	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}



	public String getCodigoConvenio() {
		return codigoConvenio;
	}



	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}



	public String getDescripcionConvenio() {
		return descripcionConvenio;
	}



	public void setDescripcionConvenio(String descripcionConvenio) {
		this.descripcionConvenio = descripcionConvenio;
	}



	public ValidacionesBdConvIngPac getValidacionesBdConvIngPac() {
		return validacionesBdConvIngPac;
	}



	public void setValidacionesBdConvIngPac(
			ValidacionesBdConvIngPac validacionesBdConvIngPac) {
		this.validacionesBdConvIngPac = validacionesBdConvIngPac;
	}


	public boolean isEsEliminable() {
		return esEliminable;
	}



	public boolean isEsModificable() {
		return esModificable;
	}



	public void setEsModificable(boolean esModificable) {
		this.esModificable = esModificable;
	}



	public String getActivo() {
		return activo;
	}



	public void setActivo(String activo) {
		this.activo = activo;
	}



	public void setEsEliminable(boolean esEliminable) {
		this.esEliminable = esEliminable;
	}



	public BonosConvIngPac getBonosConvIngPac() {
		return bonosConvIngPac;
	}



	public void setBonosConvIngPac(BonosConvIngPac bonosConvIngPac) {
		this.bonosConvIngPac = bonosConvIngPac;
	}



	public ArrayList<BonosConvIngPac> getListaBonosConvIngPac() {
		return listaBonosConvIngPac;
	}



	public ArrayList<DtoAutorizacionConvIngPac> getListaDtoAutorizacionConvIngPac() {
		return listaDtoAutorizacionConvIngPac;
	}



	public void setListaDtoAutorizacionConvIngPac(
			ArrayList<DtoAutorizacionConvIngPac> listaDtoAutorizacionConvIngPac) {
		this.listaDtoAutorizacionConvIngPac = listaDtoAutorizacionConvIngPac;
	}



	public DtoAutorizacionConvIngPac getDtoAutorizacionConvIngPac() {
		return dtoAutorizacionConvIngPac;
	}



	public void setDtoAutorizacionConvIngPac(
			DtoAutorizacionConvIngPac dtoAutorizacionConvIngPac) {
		this.dtoAutorizacionConvIngPac = dtoAutorizacionConvIngPac;
	}



	public void setListaBonosConvIngPac(
			ArrayList<BonosConvIngPac> listaBonosConvIngPac) {
		this.listaBonosConvIngPac = listaBonosConvIngPac;
	}



	public long getCodigoConveniosIngresoPaciente() {
		return codigoConveniosIngresoPaciente;
	}



	public void setCodigoConveniosIngresoPaciente(
			long codigoConveniosIngresoPaciente) {
		this.codigoConveniosIngresoPaciente = codigoConveniosIngresoPaciente;
	}


	public Character getPorDefecto() {
		return porDefecto;
	}



	public void setPorDefecto(Character porDefecto) {
		this.porDefecto = porDefecto;
	}



	public ArrayList<Long> getListaConveniosIngresoEliminar() {
		return listaConveniosIngresoEliminar;
	}



	public int getCodigoConvenioInt() {
		return codigoConvenioInt;
	}



	public void setCodigoConvenioInt(int codigoConvenioInt) {
		this.codigoConvenioInt = codigoConvenioInt;
	}



	public void setListaConveniosIngresoEliminar(
			ArrayList<Long> listaConveniosIngresoEliminar) {
		this.listaConveniosIngresoEliminar = listaConveniosIngresoEliminar;
	}



	public ArrayList<Long> getListaBonosConveniosIngresoEliminar() {
		return listaBonosConveniosIngresoEliminar;
	}



	public void setListaBonosConveniosIngresoEliminar(
			ArrayList<Long> listaBonosConveniosIngresoEliminar) {
		this.listaBonosConveniosIngresoEliminar = listaBonosConveniosIngresoEliminar;
	}



}
