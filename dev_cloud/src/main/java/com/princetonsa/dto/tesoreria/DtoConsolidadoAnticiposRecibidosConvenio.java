package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoConsolidadoAnticiposRecibidosConvenio implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private String nombreUsuarioProceso;
	private String logoDerecha;
	private String logoIzquierda;
	/*private String descripcionEmpresaInstitucion;
	private String descripcionCentroAtencion;
	private String descripcionCiudad;
	private String descripcionPais;
	private String descripcionRegionCobertura;
	private String nombreInstitucion;
	private ArrayList<DtoAnticiposRecibidosConvenio> listaConsolidadoRecibosCaja; */
	
	/** Objeto jasper para el subreporte del consolidado */
    transient private JRDataSource dsConsolidadoConsulta;
    
  
    
    
    
    public DtoConsolidadoAnticiposRecibidosConvenio(){
    	
    	this.razonSocial="";
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.nombreUsuarioProceso="";
    	this.logoDerecha="";
    	this.logoIzquierda="";
    /*	this.descripcionEmpresaInstitucion="";
    	this.descripcionCentroAtencion="";
    	this.descripcionCiudad="";
    	this.descripcionPais="";
    	this.descripcionRegionCobertura="";
    	this.setNombreInstitucion("");
    	this.listaConsolidadoRecibosCaja= new ArrayList<DtoAnticiposRecibidosConvenio>();*/
    }


	public String getRazonSocial() {
		return razonSocial;
	}


	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	public String getFechaInicial() {
		return fechaInicial;
	}


	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	public String getFechaFinal() {
		return fechaFinal;
	}


	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public String getNombreUsuarioProceso() {
		return nombreUsuarioProceso;
	}


	public void setNombreUsuarioProceso(String nombreUsuarioProceso) {
		this.nombreUsuarioProceso = nombreUsuarioProceso;
	}


	public String getLogoDerecha() {
		return logoDerecha;
	}


	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}


	public String getLogoIzquierda() {
		return logoIzquierda;
	}


	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

/*
	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}


	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}


	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}


	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}


	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}


	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}


	public String getDescripcionPais() {
		return descripcionPais;
	}


	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}


	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}


	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}


	public ArrayList<DtoAnticiposRecibidosConvenio> getListaConsolidadoRecibosCaja() {
		return listaConsolidadoRecibosCaja;
	}


	public void setListaConsolidadoRecibosCaja(
			ArrayList<DtoAnticiposRecibidosConvenio> listaConsolidadoRecibosCaja) {
		this.listaConsolidadoRecibosCaja = listaConsolidadoRecibosCaja;
	}
	
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}


	public String getNombreInstitucion() {
		return nombreInstitucion;
	} 
	 
	 
	
	 */


	public JRDataSource getDsConsolidadoConsulta() {
		return dsConsolidadoConsulta;
	}


	public void setDsConsolidadoConsulta(JRDataSource dsConsolidadoConsulta) {
		this.dsConsolidadoConsulta = dsConsolidadoConsulta;
	}

	

    
}
