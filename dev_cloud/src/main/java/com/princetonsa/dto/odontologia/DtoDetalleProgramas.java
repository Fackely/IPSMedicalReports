package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoDetalleProgramas implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double codigoPk;
	
	private double programas;
	
	private int orden;
	
	private int servicio;
	
	private String activo;
	
	private String fecha;
	
	private String hora;
	
	private String usuario;
	
	private String descripcionServicio;
	
	private String codigoCUPS;
	
	//Elemetno que aplica para el log
	private String eliminado="";
	
	private boolean enBD; 
	
	private String acronimoTarifario;
	
	public void clean()
	{	
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.programas=ConstantesBD.codigoNuncaValido;;
		this.orden=ConstantesBD.codigoNuncaValido;
		this.servicio=ConstantesBD.codigoNuncaValido;
		this.activo=ConstantesBD.acronimoSi;
		this.fecha="";
		this.hora="";
		this.usuario="";
		this.eliminado=ConstantesBD.acronimoNo;
		this.descripcionServicio="";
		this.codigoCUPS="";
		this.enBD=true;
		this.acronimoTarifario="";
	}
	
	public DtoDetalleProgramas()
	{
		this.clean();
	}

	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public double getProgramas() {
		return programas;
	}

	public void setProgramas(double programas) {
		this.programas = programas;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public int getServicio() {
		return servicio;
	}

	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getEliminado() {
		return eliminado;
	}

	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}

	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	public String getCodigoCUPS() {
		return codigoCUPS;
	}

	public void setCodigoCUPS(String codigoCUPS) {
		this.codigoCUPS = codigoCUPS;
	}

	public boolean isEnBD() {
		return enBD;
	}

	public void setEnBD(boolean enBD) {
		this.enBD = enBD;
	}

	/**
	 * @return the acronimoTarifario
	 */
	public String getAcronimoTarifario() {
		return acronimoTarifario;
	}

	/**
	 * @param acronimoTarifario the acronimoTarifario to set
	 */
	public void setAcronimoTarifario(String acronimoTarifario) {
		this.acronimoTarifario = acronimoTarifario;
	}
	
	
}