package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:02 p.m.
 */
/**
 * @author ricruico
 *
 */
public class ValorGruposServicioPacienteDto implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7064288412056222682L;
	
	private String nombreGrupoServicio;
	private double cantidadAutorizada;
	private double valorAutorizado;
	private double valorFacturado;
	

	
	public ValorGruposServicioPacienteDto(){
    
		this.reset();
	}
	public void reset()
	{
		this.setNombreGrupoServicio("");
		this.setCantidadAutorizada(0);
		this.setValorAutorizado(0);
		this.setValorFacturado(0);
		
	}
	/**
	 * @param nombreGrupoServicio
	 * @param cantidadAutorizada
	 * @param valorAutorizado
	 */
	public ValorGruposServicioPacienteDto(String nombreGrupoServicio,
			BigDecimal cantidadAutorizada, BigDecimal valorAutorizado) {
		this.nombreGrupoServicio = nombreGrupoServicio;
		this.cantidadAutorizada = cantidadAutorizada.doubleValue();
		this.valorAutorizado = valorAutorizado.doubleValue();
		this.valorFacturado = new Double(0) ;
	}
	
	public ValorGruposServicioPacienteDto(String nombreGrupoServicio,
			double cantidadAutorizada, BigDecimal valorAutorizado) {
		this.nombreGrupoServicio = nombreGrupoServicio;
		this.cantidadAutorizada = cantidadAutorizada;
		this.valorAutorizado = valorAutorizado.doubleValue();
		this.valorFacturado = new Double(0) ;
	}
	
	public ValorGruposServicioPacienteDto(String nombreGrupoServicio,
			Integer cantidadAutorizada, BigDecimal valorAutorizado) {
		this.nombreGrupoServicio = nombreGrupoServicio;
		this.cantidadAutorizada = cantidadAutorizada.intValue();
		this.valorAutorizado = valorAutorizado.doubleValue();
		this.valorFacturado = new Double(0) ;
	}
	public ValorGruposServicioPacienteDto(String nombreGrupoServicio,
			Number cantidadAutorizada, BigDecimal valorAutorizado) {
		this.nombreGrupoServicio = nombreGrupoServicio;
		this.cantidadAutorizada = cantidadAutorizada.doubleValue();
		this.valorAutorizado = valorAutorizado.doubleValue();
		this.valorFacturado = new Double(0) ;
	}

	/**
	 * @return the nombreGrupoServicio
	 */
	public String getNombreGrupoServicio() {
		return nombreGrupoServicio;
	}
	/**
	 * @param nombreGrupoServicio the nombreGrupoServicio to set
	 */
	public void setNombreGrupoServicio(String nombreGrupoServicio) {
		this.nombreGrupoServicio = nombreGrupoServicio;
	}
	/**
	 * @return the cantidadAutorizada
	 */
	public double getCantidadAutorizada() {
		return cantidadAutorizada;
	}
	/**
	 * @param cantidadAutorizada the cantidadAutorizada to set
	 */
	public void setCantidadAutorizada(double cantidadAutorizada) {
		this.cantidadAutorizada = cantidadAutorizada;
	}
	/**
	 * @return the valorAutorizado
	 */
	public double getValorAutorizado() {
		return valorAutorizado;
	}
	/**
	 * @param valorAutorizado the valorAutorizado to set
	 */
	public void setValorAutorizado(double valorAutorizado) {
		this.valorAutorizado = valorAutorizado;
	}
	/**
	 * @return the valorFacturado
	 */
	public double getValorFacturado() {
		return valorFacturado;
	}
	/**
	 * @param valorFacturado the valorFacturado to set
	 */
	public void setValorFacturado(double valorFacturado) {
		this.valorFacturado = valorFacturado;
	}

	
}