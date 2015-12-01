package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoReportePresupuestosOdontologicosContratadosConPromocionGen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private String logoIzquierda;
	private String logoDerecha;
	private String usuarioProcesa;
	//private BigDecimal totalPorPromocion;
	private String totalPromociones;
	
	/** Objeto jasper para el subreporte del consolidado */
    private JRDataSource dsResultadoPresupuPromo;

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

	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}

	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}

	public JRDataSource getDsResultadoPresupuPromo() {
		return dsResultadoPresupuPromo;
	}

	public void setDsResultadoPresupuPromo(JRDataSource dsResultadoPresupuPromo) {
		this.dsResultadoPresupuPromo = dsResultadoPresupuPromo;
	}
	
	public void setTotalPromociones(String totalPromociones) {
		this.totalPromociones = totalPromociones;
	}

	public String getTotalPromociones() {
		return totalPromociones;
	}
	
    
    
}
