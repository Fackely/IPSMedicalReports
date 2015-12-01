package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Encaps&uacute;la la respuesta a la informaci&oacute;n de una traslado de abonos entre pacientes
 * 
 * @author Cristhian Murillo
 */
public class DtoConsultaTrasladoAbonoPAciente implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	// Paciente Origen
	private String tipoIdOri;
	private String numeroIdOri;
	private String primerNombreOri;
	private String segundoNombreOri;
	private String primerApellidoOri;
	private String segundoApellidoOri;
	int centroAtencionOri;
	private Object centroAtencionOriObj;
	String nombreCentroAtencionOri;
	double abonoTrasladadoOri;
	private int idIngresoOri;
	private Object idIngresoOriObj;
	private String estadoPlanTramiendoOri;
	
	// Paciente Destino
	private String tipoIdDes;
	private String numeroIdDes;
	private String primerNombreDes;
	private String segundoNombreDes;
	private String primerApellidoDes;
	private String segundoApellidoDes;
	int centroAtencionDes;
	String nombreCentroAtencionDes;
	double abonoTrasladadoDes;
	private int idIngresoDes;
	private String estadoPlanTramiendoDes;
	
	// Datos proceso
	private long idTrasladoAbonos;
	private int codCentroAtenProceso;
	private String nomCentroAtenProceso;
	private String loginUsuarioProceso;
	private Date fechaProceso;
	private String horaProceso; 
	
	
	
	public DtoConsultaTrasladoAbonoPAciente()
	{
		this.tipoIdOri				= "";
		this.numeroIdOri			= "";
		this.primerNombreOri		= "";
		this.segundoNombreOri		= "";
		this.primerApellidoOri	  	= "";
		this.segundoApellidoOri		= "";
		this.centroAtencionOri		= ConstantesBD.codigoNuncaValido;
		this.centroAtencionOriObj	= null;
		this.nombreCentroAtencionOri= "";
		this.abonoTrasladadoOri		= ConstantesBD.codigoNuncaValidoDouble;
		this.idIngresoOri			= ConstantesBD.codigoNuncaValido;
		this.idIngresoOriObj		= null;
		this.estadoPlanTramiendoOri	= "";
		
		this.tipoIdDes				= "";
		this.numeroIdDes			= "";
		this.primerNombreDes		= "";
		this.segundoNombreDes		= "";
		this.primerApellidoDes		= "";
		this.segundoApellidoDes		= "";
		this.centroAtencionDes		= ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencionDes= "";
		this.abonoTrasladadoDes		= ConstantesBD.codigoNuncaValidoDouble;
		this.idIngresoDes			= ConstantesBD.codigoNuncaValido;
		this.estadoPlanTramiendoDes = "";
		
		this.idTrasladoAbonos		= ConstantesBD.codigoNuncaValidoLong;
		this.codCentroAtenProceso	= ConstantesBD.codigoNuncaValido;
		this.nomCentroAtenProceso	= "";
		this.loginUsuarioProceso	= "";
		this.fechaProceso			= new Date();
		this.horaProceso			= ""; 
	}

	public String getTipoIdOri() {
		return tipoIdOri;
	}

	public void setTipoIdOri(String tipoIdOri) {
		this.tipoIdOri = tipoIdOri;
	}

	public String getNumeroIdOri() {
		return numeroIdOri;
	}

	public void setNumeroIdOri(String numeroIdOri) {
		this.numeroIdOri = numeroIdOri;
	}

	public String getPrimerNombreOri() {
		return primerNombreOri;
	}

	public void setPrimerNombreOri(String primerNombreOri) {
		this.primerNombreOri = primerNombreOri;
	}

	public String getSegundoNombreOri() {
		return segundoNombreOri;
	}

	public void setSegundoNombreOri(String segundoNombreOri) {
		this.segundoNombreOri = segundoNombreOri;
	}

	public String getPrimerApellidoOri() {
		return primerApellidoOri;
	}

	public void setPrimerApellidoOri(String primerApellidoOri) {
		this.primerApellidoOri = primerApellidoOri;
	}

	public String getSegundoApellidoOri() {
		return segundoApellidoOri;
	}

	public void setSegundoApellidoOri(String segundoApellidoOri) {
		this.segundoApellidoOri = segundoApellidoOri;
	}

	public int getIdIngresoOri() {
		return idIngresoOri;
	}

	public void setIdIngresoOri(int idIngresoOri) {
		this.idIngresoOri = idIngresoOri;
	}

	public int getCentroAtencionOri() {
		return centroAtencionOri;
	}

	public void setCentroAtencionOri(int centroAtencionOri) {
		this.centroAtencionOri = centroAtencionOri;
	}

	public String getNombreCentroAtencionOri() {
		return nombreCentroAtencionOri;
	}

	public void setNombreCentroAtencionOri(String nombreCentroAtencionOri) {
		this.nombreCentroAtencionOri = nombreCentroAtencionOri;
	}

	public double getAbonoTrasladadoOri() {
		return abonoTrasladadoOri;
	}

	public void setAbonoTrasladadoOri(double abonoTrasladadoOri) {
		this.abonoTrasladadoOri = abonoTrasladadoOri;
	}

	public String getEstadoPlanTramiendoOri() {
		return estadoPlanTramiendoOri;
	}

	public void setEstadoPlanTramiendoOri(String estadoPlanTramiendoOri) {
		this.estadoPlanTramiendoOri = estadoPlanTramiendoOri;
	}

	public String getTipoIdDes() {
		return tipoIdDes;
	}

	public void setTipoIdDes(String tipoIdDes) {
		this.tipoIdDes = tipoIdDes;
	}

	public String getNumeroIdDes() {
		return numeroIdDes;
	}

	public void setNumeroIdDes(String numeroIdDes) {
		this.numeroIdDes = numeroIdDes;
	}

	public String getPrimerNombreDes() {
		return primerNombreDes;
	}

	public void setPrimerNombreDes(String primerNombreDes) {
		this.primerNombreDes = primerNombreDes;
	}

	public String getSegundoNombreDes() {
		return segundoNombreDes;
	}

	public void setSegundoNombreDes(String segundoNombreDes) {
		this.segundoNombreDes = segundoNombreDes;
	}

	public String getPrimerApellidoDes() {
		return primerApellidoDes;
	}

	public void setPrimerApellidoDes(String primerApellidoDes) {
		this.primerApellidoDes = primerApellidoDes;
	}

	public String getSegundoApellidoDes() {
		return segundoApellidoDes;
	}

	public void setSegundoApellidoDes(String segundoApellidoDes) {
		this.segundoApellidoDes = segundoApellidoDes;
	}

	public int getCentroAtencionDes() {
		return centroAtencionDes;
	}

	public void setCentroAtencionDes(int centroAtencionDes) {
		this.centroAtencionDes = centroAtencionDes;
	}


	public double getAbonoTrasladadoDes() {
		return abonoTrasladadoDes;
	}

	public void setAbonoTrasladadoDes(double abonoTrasladadoDes) {
		this.abonoTrasladadoDes = abonoTrasladadoDes;
	}

	public String getEstadoPlanTramiendoDes() {
		return estadoPlanTramiendoDes;
	}

	public void setEstadoPlanTramiendoDes(String estadoPlanTramiendoDes) {
		this.estadoPlanTramiendoDes = estadoPlanTramiendoDes;
	}

	public long getIdTrasladoAbonos() {
		return idTrasladoAbonos;
	}

	public void setIdTrasladoAbonos(long idTrasladoAbonos) {
		this.idTrasladoAbonos = idTrasladoAbonos;
	}

	public int getCodCentroAtenProceso() {
		return codCentroAtenProceso;
	}

	public void setCodCentroAtenProceso(int codCentroAtenProceso) {
		this.codCentroAtenProceso = codCentroAtenProceso;
	}

	public String getNomCentroAtenProceso() {
		return nomCentroAtenProceso;
	}

	public void setNomCentroAtenProceso(String nomCentroAtenProceso) {
		this.nomCentroAtenProceso = nomCentroAtenProceso;
	}

	public String getLoginUsuarioProceso() {
		return loginUsuarioProceso;
	}

	public void setLoginUsuarioProceso(String loginUsuarioProceso) {
		this.loginUsuarioProceso = loginUsuarioProceso;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getHoraProceso() {
		return horaProceso;
	}

	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}

	public int getIdIngresoDes() {
		return idIngresoDes;
	}

	public void setIdIngresoDes(int idIngresoDes) {
		this.idIngresoDes = idIngresoDes;
	}

	public String getNombreCentroAtencionDes() {
		return nombreCentroAtencionDes;
	}

	public void setNombreCentroAtencionDes(String nombreCentroAtencionDes) {
		this.nombreCentroAtencionDes = nombreCentroAtencionDes;
	}

	public Object getCentroAtencionOriObj() {
		return centroAtencionOriObj;
	}

	public void setCentroAtencionOriObj(Object centroAtencionOriObj) {
		this.centroAtencionOriObj = centroAtencionOriObj;
		if(centroAtencionOriObj instanceof Integer){
			setCentroAtencionOri(Integer.parseInt(centroAtencionOriObj.toString()));
		}
	}

	public Object getIdIngresoOriObj() {
		return idIngresoOriObj;
	}

	public void setIdIngresoOriObj(Object idIngresoOriObj) {
		this.idIngresoOriObj = idIngresoOriObj;
		if(idIngresoOriObj instanceof Integer){
			setIdIngresoOri(Integer.parseInt(idIngresoOriObj.toString()));
		}
	}

	
}
