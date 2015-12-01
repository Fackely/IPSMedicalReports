package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoReportePromocionesOdontologicasGen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo donde se almacena la razón social de la institución
	 */
	private String razonSocial;
	
	/**
	 * Atributo donde se almacena la fecha inicial de la generación del reporte
	 */
	private String fechaGeneracionInicial;
	
	/**
	 * Atributo donde se almacena la fecha final de la generación del reporte
	 */
	private String fechaGeneracionFinal;
	
	/**
	 * Atributo donde se almacena la fecha final de la generación del reporte
	 */
	private String estadoPromocion;
	
	/**
	 * Atributo donde se almacena la ruta para generar el logo a la izquiera
	 */
	private String logoIzquierda;
	
	/**
	 * Atributo donde se almacena la ruta para generar el logo a la derecha
	 */
	private String logoDerecha;
	
	/**
	 * Atributo donde se almacena el usuario de la session
	 */
	private String usuarioProcesa;
	
	/** Objeto jasper para el subreporte del consolidado */
    private JRDataSource dsResultadoPromociones;

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	

	public String getFechaGeneracionInicial() {
		return fechaGeneracionInicial;
	}

	public void setFechaGeneracionInicial(String fechaGeneracionInicial) {
		this.fechaGeneracionInicial = fechaGeneracionInicial;
	}

	public String getFechaGeneracionFinal() {
		return fechaGeneracionFinal;
	}

	public void setFechaGeneracionFinal(String fechaGeneracionFinal) {
		this.fechaGeneracionFinal = fechaGeneracionFinal;
	}

	public String getEstadoPromocion() {
		return estadoPromocion;
	}

	public void setEstadoPromocion(String estadoPromocion) {
		this.estadoPromocion = estadoPromocion;
	}

	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	public String getLogoDerecha() {
		return logoDerecha;
	}

	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}

	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}

	public void setDsResultadoPromociones(JRDataSource dsResultadoPromociones) {
		this.dsResultadoPromociones = dsResultadoPromociones;
	}

	public JRDataSource getDsResultadoPromociones() {
		return dsResultadoPromociones;
	}
	
	

}
