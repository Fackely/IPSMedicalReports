package com.servinte.axioma.dto.tesoreria;

import java.sql.Date;
import java.util.ArrayList;

public class DtoBusquedaNotasPacientePorRango {

	private long codigoEmpresaInstitucion;
	private ArrayList<Integer> codigosCentrosAtencion;
	private Date fechaInicialGeneracion;
	private Date fechaFinalGeneracion;
	private long numeroInicialNota;
	private long numeroFinalNota;
	private String naturalezaNota;
	private long codigoPkConceptoNotasPaciente;
	private String usuarioGeneraNotas;
	
	
	public DtoBusquedaNotasPacientePorRango(){}
	
	
	public DtoBusquedaNotasPacientePorRango(long codigoEmpresaInstitucion, 
			ArrayList<Integer> codigosCentrosAtencion,
			Date fechaInicialGeneracion, Date fechaFinalGeneracion,
			long numeroInicialNota, long numeroFinalNota,
			String naturalezaNota, long codigoPkConceptoNotasPaciente,
			String usuarioGeneraNotas) {
		super();
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
		this.codigosCentrosAtencion = codigosCentrosAtencion;
		this.fechaInicialGeneracion = fechaInicialGeneracion;
		this.fechaFinalGeneracion = fechaFinalGeneracion;
		this.numeroInicialNota = numeroInicialNota;
		this.numeroFinalNota = numeroFinalNota;
		this.naturalezaNota = naturalezaNota;
		this.codigoPkConceptoNotasPaciente = codigoPkConceptoNotasPaciente;
		this.usuarioGeneraNotas = usuarioGeneraNotas;
	}
	
	
	

	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}


	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}


	public ArrayList<Integer> getCodigosCentrosAtencion() {
		return codigosCentrosAtencion;
	}


	public void setCodigosCentrosAtencion(ArrayList<Integer> codigosCentrosAtencion) {
		this.codigosCentrosAtencion = codigosCentrosAtencion;
	}


	public Date getFechaInicialGeneracion() {
		return fechaInicialGeneracion;
	}
	public void setFechaInicialGeneracion(Date fechaInicialGeneracion) {
		this.fechaInicialGeneracion = fechaInicialGeneracion;
	}
	public Date getFechaFinalGeneracion() {
		return fechaFinalGeneracion;
	}
	public void setFechaFinalGeneracion(Date fechaFinalGeneracion) {
		this.fechaFinalGeneracion = fechaFinalGeneracion;
	}
	public long getNumeroInicialNota() {
		return numeroInicialNota;
	}
	public void setNumeroInicialNota(long numeroInicialNota) {
		this.numeroInicialNota = numeroInicialNota;
	}
	public long getNumeroFinalNota() {
		return numeroFinalNota;
	}
	public void setNumeroFinalNota(long numeroFinalNota) {
		this.numeroFinalNota = numeroFinalNota;
	}
	public String getNaturalezaNota() {
		return naturalezaNota;
	}
	public void setNaturalezaNota(String naturalezaNota) {
		this.naturalezaNota = naturalezaNota;
	}


	public long getCodigoPkConceptoNotasPaciente() {
		return codigoPkConceptoNotasPaciente;
	}


	public void setCodigoPkConceptoNotasPaciente(long codigoPkConceptoNotasPaciente) {
		this.codigoPkConceptoNotasPaciente = codigoPkConceptoNotasPaciente;
	}


	public String getUsuarioGeneraNotas() {
		return usuarioGeneraNotas;
	}


	public void setUsuarioGeneraNotas(String usuarioGeneraNotas) {
		this.usuarioGeneraNotas = usuarioGeneraNotas;
	}

}
