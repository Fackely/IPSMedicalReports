package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;



public class DtoPresupuestoPiezas implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5780651892698159179L;
	/**
	 * 
	 */
	private BigDecimal codigoPk ;
	private BigDecimal pieza;
	private BigDecimal hallazgo;
	private BigDecimal superficie;
	private DtoInfoFechaUsuario usuarioModifica;
	private BigDecimal presupuestoOdoProgServ;
	private String seccion;
	private boolean activo;
	private int numSuperficies;
	
	
	
	private BigDecimal codigoDetallePlanTratamiento;
	
	/**
	 * contiene la lista de convenio+"-"+contrato que lo contiene paquetizado
	 */
	private ArrayList<String> listaConvenioContratoPaquetiza;
	
	/**
	 * 
	 */
	public DtoPresupuestoPiezas(){
		this.reset();
	}
	
	/**
	 * 
	 */
	void reset (){
		this.codigoPk = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.pieza = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.hallazgo = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.superficie = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.usuarioModifica = new DtoInfoFechaUsuario();
		this.presupuestoOdoProgServ = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.seccion = "";
		this.activo=true;
		this.numSuperficies=0;
		this.listaConvenioContratoPaquetiza= new ArrayList<String>();
		
		this.codigoDetallePlanTratamiento = new BigDecimal(ConstantesBD.codigoNuncaValido);
	}


	public BigDecimal getCodigoPk() {
		return codigoPk;
	}


	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}


	public BigDecimal getPieza() {
		return pieza;
	}


	public void setPieza(BigDecimal pieza) {
		this.pieza = pieza;
	}


	public BigDecimal getHallazgo() {
		return hallazgo;
	}


	public void setHallazgo(BigDecimal hallazgo) {
		this.hallazgo = hallazgo;
	}


	public BigDecimal getSuperficie() {
		return superficie;
	}


	public void setSuperficie(BigDecimal superficie) {
		this.superficie = superficie;
	}


	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}


	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public void setPresupuestoOdoProgServ(BigDecimal presupuestoOdoProgServ) {
		this.presupuestoOdoProgServ = presupuestoOdoProgServ;
	}

	public BigDecimal getPresupuestoOdoProgServ() {
		return presupuestoOdoProgServ;
	}

	/**
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo()
	{
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo)
	{
		this.activo = activo;
	}
	
	/**
	 * @return the activo
	 */
	public boolean getActivo()
	{
		return activo;
	}

	/**
	 * @return the numSuperficies
	 */
	public int getNumSuperficies() {
		return numSuperficies;
	}

	/**
	 * @param numSuperficies the numSuperficies to set
	 */
	public void setNumSuperficies(int numSuperficies) {
		this.numSuperficies = numSuperficies;
	}

	/**
	 * @return the listaConvenioContratoPaquetiza
	 */
	public ArrayList<String> getListaConvenioContratoPaquetiza() {
		return listaConvenioContratoPaquetiza;
	}

	/**
	 * @param listaConvenioContratoPaquetiza the listaConvenioContratoPaquetiza to set
	 */
	public void setListaConvenioContratoPaquetiza(
			ArrayList<String> listaConvenioContratoPaquetiza) {
		this.listaConvenioContratoPaquetiza = listaConvenioContratoPaquetiza;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DtoPresupuestoPiezas [activo=" + activo + ", codigoPk="
				+ codigoPk + ", hallazgo=" + hallazgo + ", numSuperficies="
				+ numSuperficies + ", pieza=" + pieza
				+ ", presupuestoOdoProgServ=" + presupuestoOdoProgServ
				+ ", seccion=" + seccion + ", superficie=" + superficie + "]";
	}

	/**
	 * @param codigoDetallePlanTratamiento the codigoDetallePlanTratamiento to set
	 */
	public void setCodigoDetallePlanTratamiento(
			BigDecimal codigoDetallePlanTratamiento) {
		this.codigoDetallePlanTratamiento = codigoDetallePlanTratamiento;
	}

	/**
	 * @return the codigoDetallePlanTratamiento
	 */
	public BigDecimal getCodigoDetallePlanTratamiento() {
		return codigoDetallePlanTratamiento;
	}


}
