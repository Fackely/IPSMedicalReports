package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
/**
 * ANEXO 961
 * @author axioma
 *
 */
public class DtoHonorariosPool  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	private BigDecimal codigoPk ;
	private InfoDatosInt pool;
	private InfoDatosInt convenio ;
	private InfoDatosInt esquemaTarifario;
	private DtoInfoFechaUsuario usuarioModifica;
	private InfoDatosInt centroAtencion;
	private int institucion;
	/**
	 * boolean para indicar si es o no convenio
	 */
	private boolean esConvenio;
	
	/**
	 * DETALLE AGRUPACION HONORARIOS POOL
	 */
	private DtoAgrupHonorariosPool dtoAgrupacionPool ;
	private ArrayList<DtoAgrupHonorariosPool> listaAgrupacionHonorarios = new ArrayList<DtoAgrupHonorariosPool>();
	
	/**
	 * DETALLE HONORARIOS POOL SERVICIO
	 */
	private ArrayList<DtoHonorarioPoolServicio> listaHonorarioPoolServ = new  ArrayList<DtoHonorarioPoolServicio>();
	private DtoHonorarioPoolServicio  dtoHonorarioPoolServicio;
	/**
	 * 
	 */
	public DtoHonorariosPool() {
		this.reset();
	}

	/**
	 * 
	 */
	public void reset(){
		this.codigoPk = BigDecimal.ZERO;
		this.pool = new  InfoDatosInt();
		this.convenio = new InfoDatosInt();
		this.esquemaTarifario = new InfoDatosInt();
		this.centroAtencion= new InfoDatosInt();
		this.usuarioModifica =  new DtoInfoFechaUsuario();
		this.listaAgrupacionHonorarios = new ArrayList<DtoAgrupHonorariosPool>();
		this.listaHonorarioPoolServ = new ArrayList<DtoHonorarioPoolServicio>();
		this.dtoAgrupacionPool= new DtoAgrupHonorariosPool();
		this.dtoHonorarioPoolServicio= new DtoHonorarioPoolServicio();
		this.institucion= ConstantesBD.codigoNuncaValido;
		this.esConvenio= false;
	}
	
	/**
	 * FORMATEA LAS LISTA Y LOS OBJETOS
	 */
	public void reset2(){
		this.codigoPk=BigDecimal.ZERO;
		this.dtoAgrupacionPool = new DtoAgrupHonorariosPool();
		this.dtoHonorarioPoolServicio = new DtoHonorarioPoolServicio();
		this.listaAgrupacionHonorarios = new ArrayList<DtoAgrupHonorariosPool>();
		this.listaHonorarioPoolServ = new ArrayList<DtoHonorarioPoolServicio>();
	}
	
	
	/**
	 * FORMATEA LOS OBJETOS
	 */
	public void reset3(){
		this.dtoAgrupacionPool = new DtoAgrupHonorariosPool();
		this.dtoHonorarioPoolServicio = new DtoHonorarioPoolServicio();
	}
	

	public BigDecimal getCodigoPk() {
		return codigoPk;
	}



	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}



	public InfoDatosInt getPool() {
		return pool;
	}

	public String getNombrePool() {
		return pool.getNombre();
	}


	public void setPool(InfoDatosInt pool) {
		this.pool = pool;
	}



	public InfoDatosInt getConvenio() {
		return convenio;
	}

	public String getNombreConvenio() {
		return convenio.getNombre();
	}


	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}

	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}



	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}


	public InfoDatosInt getEsquemaTarifario() {
		return esquemaTarifario;
	}

	public String getNombreEsquemaTarifario() {
		return esquemaTarifario.getNombre();
	}

	public void setEsquemaTarifario(InfoDatosInt esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}



	public ArrayList<DtoAgrupHonorariosPool> getListaAgrupacionHonorarios() {
		return listaAgrupacionHonorarios;
	}



	public void setListaAgrupacionHonorarios(
			ArrayList<DtoAgrupHonorariosPool> listaAgrupacionHonorarios) {
		this.listaAgrupacionHonorarios = listaAgrupacionHonorarios;
	}



	public ArrayList<DtoHonorarioPoolServicio> getListaHonorarioPoolServ() {
		return listaHonorarioPoolServ;
	}



	public void setListaHonorarioPoolServ(
			ArrayList<DtoHonorarioPoolServicio> listaHonorarioPoolServ) {
		this.listaHonorarioPoolServ = listaHonorarioPoolServ;
	}



	public void setDtoAgrupacionPool(DtoAgrupHonorariosPool dtoAgrupacionPool) {
		this.dtoAgrupacionPool = dtoAgrupacionPool;
	}



	public DtoAgrupHonorariosPool getDtoAgrupacionPool() {
		return dtoAgrupacionPool;
	}



	public void setDtoHonorarioPoolServicio(DtoHonorarioPoolServicio dtoHonorarioPoolServicio) {
		this.dtoHonorarioPoolServicio = dtoHonorarioPoolServicio;
	}



	public DtoHonorarioPoolServicio getDtoHonorarioPoolServicio() {
		return dtoHonorarioPoolServicio;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getNombreCentroAtencion() {
		return centroAtencion.getNombre();
	}
	
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the esConvenio
	 */
	public boolean isEsConvenio() {
		return esConvenio;
	}

	/**
	 * @param esConvenio the esConvenio to set
	 */
	public void setEsConvenio(boolean esConvenio) {
		this.esConvenio = esConvenio;
	}

}
