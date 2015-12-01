package com.servinte.axioma.generadorReporte.facturasVarias.dtoFacturasVarias;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoConsultaMovimientosFacturaVariaGen {
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private String logoIzquierda;
	private String logoDerecha;
	private String usuario;
	private String usuarioProcesa;
	/** Objeto jasper para el subreporte del consolidado */
    private JRDataSource dsResultado;
    
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
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}
	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}
	public void setDsResultado(JRDataSource dsResultado) {
		this.dsResultado = dsResultado;
	}
	public JRDataSource getDsResultado() {
		return dsResultado;
	}
	
}
