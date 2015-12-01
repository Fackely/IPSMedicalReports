/**
 * 
 */
package com.princetonsa.dto.facturacion;

import java.util.Date;

import util.facturacion.InfoResponsableCobertura;

import com.servinte.axioma.orm.Convenios;

/**
 * @author armando
 */
public class DtoServicios 
{
	private int codigoServicio;
	
	private String codigoPropietarioServicio;
	
	private String descripcionServicio;
	
	private String descripcionPropietarioServicio;
	
	private String acronimoTipoServicio;
	
	private int codigoGrupoServicio;
	
	private int codigoEspecialidad;
	
	private double valorTarifa;
	
	private Date fechaVigenciaTarifa;

	private Integer codigoEsquemaTarifario;
	
	private String nombreEsquemaTarifario; 
	
	private Integer codigoTipoLiquidacion;
	
	private String nombreTipoLoquidacion;
	
	private String acronimoTipoLoquidacion;
	
	private String tipoServicio;
	
	private Integer codigoTipoMonto;
	
	private int cantidad;

	private int centroCosto;
	
	/**Almacena la info de la cobertura del servicio**/
	private InfoResponsableCobertura responsableCoberturaServicio;
	/**Almacena el convenio responsable del serivicio*/
	private Convenios convenioResponsable;
	
	/**
	 * Atributo que permite indicar si el convenio responsable de la orden cubre el servicios.
	 */
	public String servicioCubierto;
	
	/**
	 * Atributo que permite almacenar el tipo de monto
	 */
	public int tipoMonto;
	
	public boolean urgente;
	
	/**
	 * Constructor de la clase
	 */
	public DtoServicios() 
	{
		this.setCodigoTipoMonto(null);
		this.responsableCoberturaServicio= new InfoResponsableCobertura();
		this.convenioResponsable= new Convenios();
	}
	
	//Mt 6029
	public DtoServicios(String acronimoTipoServicio, int codigoGrupoServicio, int tipoMonto) 
	{
		this.acronimoTipoServicio=acronimoTipoServicio;
		this.codigoGrupoServicio=codigoGrupoServicio;
		this.tipoMonto=tipoMonto;
	}
	//Fin MT
	
	public int getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getCodigoPropietarioServicio() {
		return codigoPropietarioServicio;
	}

	public void setCodigoPropietarioServicio(String codigoPropietarioServicio) {
		this.codigoPropietarioServicio = codigoPropietarioServicio;
	}

	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	public String getDescripcionPropietarioServicio() {
		return descripcionPropietarioServicio;
	}

	public void setDescripcionPropietarioServicio(
			String descripcionPropietarioServicio) {
		this.descripcionPropietarioServicio = descripcionPropietarioServicio;
	}

	public void setAcronimoTipoServicio(String acronimoTipoServicio) {
		this.acronimoTipoServicio = acronimoTipoServicio;
	}

	public String getAcronimoTipoServicio() {
		return acronimoTipoServicio;
	}

	public void setCodigoGrupoServicio(int codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	public int getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	public void setValorTarifa(double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	public double getValorTarifa() {
		return valorTarifa;
	}

	public void setFechaVigenciaTarifa(Date fechaVigenciaTarifa) {
		this.fechaVigenciaTarifa = fechaVigenciaTarifa;
	}

	public Date getFechaVigenciaTarifa() {
		return fechaVigenciaTarifa;
	}

	/**
	 * @return valor de codigoEsquemaTarifario
	 */
	public Integer getCodigoEsquemaTarifario() {
		return codigoEsquemaTarifario;
	}

	/**
	 * @param codigoEsquemaTarifario el codigoEsquemaTarifario para asignar
	 */
	public void setCodigoEsquemaTarifario(Integer codigoEsquemaTarifario) {
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}

	/**
	 * @return valor de codigoTipoLiquidacion
	 */
	public Integer getCodigoTipoLiquidacion() {
		return codigoTipoLiquidacion;
	}

	/**
	 * @param codigoTipoLiquidacion el codigoTipoLiquidacion para asignar
	 */
	public void setCodigoTipoLiquidacion(Integer codigoTipoLiquidacion) {
		this.codigoTipoLiquidacion = codigoTipoLiquidacion;
	}

	/**
	 * @return valor de nombreTipoLoquidacion
	 */
	public String getNombreTipoLoquidacion() {
		return nombreTipoLoquidacion;
	}

	/**
	 * @param nombreTipoLoquidacion el nombreTipoLoquidacion para asignar
	 */
	public void setNombreTipoLoquidacion(String nombreTipoLoquidacion) {
		this.nombreTipoLoquidacion = nombreTipoLoquidacion;
	}

	/**
	 * @return valor de acronimoTipoLoquidacion
	 */
	public String getAcronimoTipoLoquidacion() {
		return acronimoTipoLoquidacion;
	}

	/**
	 * @param acronimoTipoLoquidacion el acronimoTipoLoquidacion para asignar
	 */
	public void setAcronimoTipoLoquidacion(String acronimoTipoLoquidacion) {
		this.acronimoTipoLoquidacion = acronimoTipoLoquidacion;
	}

	/**
	 * @return valor de nombreEsquemaTarifario
	 */
	public String getNombreEsquemaTarifario() {
		return nombreEsquemaTarifario;
	}

	/**
	 * @param nombreEsquemaTarifario el nombreEsquemaTarifario para asignar
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario) {
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}

	/**
	 * @return valor de tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio el tipoServicio para asignar
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}


	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}


	public int getCentroCosto() {
		return centroCosto;
	}


	public void setCodigoTipoMonto(Integer codigoTipoMonto) {
		this.codigoTipoMonto = codigoTipoMonto;
	}


	public Integer getCodigoTipoMonto() {
		return codigoTipoMonto;
	}


	public InfoResponsableCobertura getResponsableCoberturaServicio() {
		return responsableCoberturaServicio;
	}


	public void setResponsableCoberturaServicio(
			InfoResponsableCobertura responsableCoberturaServicio) {
		this.responsableCoberturaServicio = responsableCoberturaServicio;
	}


	public Convenios getConvenioResponsable() {
		return convenioResponsable;
	}


	public void setConvenioResponsable(Convenios convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
	}
	
	public String getServicioCubierto() {
		return servicioCubierto;
	}

	public void setServicioCubierto(String servicioCubierto) {
		this.servicioCubierto = servicioCubierto;
	}
	
	public void setTipoMonto(int tipoMonto) {
		this.tipoMonto = tipoMonto;
	}


	public int getTipoMonto() {
		return tipoMonto;
	}

	
	public boolean isUrgente() {
		return urgente;
	}
	
	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	

}
