/**
 * 
 */
package com.princetonsa.dto.cargos;

import java.io.Serializable;
import java.math.BigDecimal;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.servinte.axioma.dto.facturacion.DtoInfoMontoCobroDetallado;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoDetalleCargo implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7134195087860068988L;

	/**
	 * 
	 */
	private double codigoDetalleCargo;
	
	/**
	 * 
	 */
	private double codigoSubcuenta;
	
	/**
	 * 
	 */
	private int codigoConvenio;
	
	/**
	 * 
	 */
	private int codigoContrato;
	
	/**
	 * 
	 */
	private int codigoEsquemaTarifario;
	
	/**
	 * 
	 */
	private int cantidadCargada;
	
	/**
	 * 
	 */
	private double valorUnitarioTarifa;
	
	/**
	 * 
	 */
	private double valorUnitarioCargado;
	
	/**
	 * 
	 */
	private double valorTotalCargado;
	
	/**
	 * 
	 */
	private double porcentajeCargado;
	
	/**
	 * 
	 */
	private double porcentajeRecargo;
	
	/**
	 * 
	 */
	private double valorUnitarioRecargo;
	
	/**
	 * 
	 */
	private double porcentajeDescuento;
	
	/**
	 * 
	 */
	private double valorUnitarioDescuento;
	
	/**
	 * 
	 */
	private double valorUnitarioIva;
	
	/**
	 * 
	 */
	private String requiereAutorizacion;
	
	/**
	 * 
	 */
	private String numeroAutorizacion;
	
	/**
	 * 
	 */
	private int estado;
	
	/**
	 * 
	 */
	private String cubierto;
	
	/**
	 * 
	 */
	private String tipoDistribucion;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 */
	private int codigoServicio;
	
	/**
	 * 
	 */
	private String nombreServicio;
	
	/**
	 * 
	 */
	private int codigoArticulo;
	
	/**
	 * 
	 */
	private int codigoServicioCx;
	
	/**
	 * 
	 */
	private int codigoTipoAsocio;
	
	/**
	 * 
	 */
	private String facturado;
	
	/**
	 * 
	 */
	private int codigoTipoSolicitud;
	
	/**
	 * 
	 */
	private String paquetizado;
	
	/**
	 * 
	 */
	private double cargoPadre;
	
	/**
	 * 
	 */
	private double codigoSolicitudSubCuenta;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private boolean filtrarSoloCantidadesMayoresCero;
	
	/**
	 * 
	 */	
	private int detCxHonorarios;
	
	/**
	 * 
	 */
	private int detAsocioCxSalasMat;
	
	/**
	 * 
	 */
	private String esPortatil;
	
	/**
	 * 
	 */
	private String dejarExcento;
	
	/**
	 * 
	 */
	private double porcentajeDctoPromocionServicio;
	
	/**
	 * 
	 */
	private BigDecimal valorDescuentoPromocionServicio;
	
	/**
	 * 
	 */
	private double porcentajeHonorarioPromocionServicio;
	
	/**
	 * 
	 */
	private BigDecimal valorHonorarioPromocionServicio;
	
	
	/**
	 * 
	 */
	private double programa;
	
	
	/**
	 * 
	 */
	private double porcentajeDctoBono;
	
	/**
	 * 
	 */
	private BigDecimal valorDescuentoBono;
	
	/**
	 * 
	 */
	private double porcentajeDctoOdontologico;
	
	/**
	 * 
	 */
	private BigDecimal valorDescuentoOdontologico;
	
	/**
	 * 
	 */
	private DtoInfoFechaUsuario FHU;
	
	/**
	 * 
	 */
	private int detallePaqueteOdontoConvenio;
	

	
	/**
	 * Constructor 
	 */
	public DtoDetalleCargo()
	{	
		this.codigoDetalleCargo= ConstantesBD.codigoNuncaValidoDouble;
		this.codigoSubcuenta = ConstantesBD.codigoNuncaValidoDouble;
		this.codigoContrato = ConstantesBD.codigoNuncaValido;
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.cantidadCargada = ConstantesBD.codigoNuncaValido;
		this.codigoEsquemaTarifario = ConstantesBD.codigoNuncaValido;
		this.valorUnitarioTarifa = ConstantesBD.codigoNuncaValidoDouble;
		this.valorUnitarioCargado = ConstantesBD.codigoNuncaValidoDouble;
		this.valorTotalCargado = ConstantesBD.codigoNuncaValidoDouble;
		this.porcentajeCargado = ConstantesBD.codigoNuncaValido;
		this.porcentajeRecargo = ConstantesBD.codigoNuncaValido;
		this.valorUnitarioRecargo = ConstantesBD.codigoNuncaValidoDouble;
		this.porcentajeDescuento = ConstantesBD.codigoNuncaValido;
		this.valorUnitarioDescuento = ConstantesBD.codigoNuncaValidoDouble;
		this.valorUnitarioIva = ConstantesBD.codigoNuncaValidoDouble;
		this.requiereAutorizacion="";
		this.numeroAutorizacion = "";
		this.estado = ConstantesBD.codigoNuncaValido;
		this.cubierto = "";
		this.tipoDistribucion = "";
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
		this.nombreServicio="";
		this.codigoArticulo = ConstantesBD.codigoNuncaValido;
		this.codigoServicioCx = ConstantesBD.codigoNuncaValido;
		this.facturado = "";
		this.codigoTipoAsocio=ConstantesBD.codigoNuncaValido;
		this.paquetizado="";
		this.cargoPadre=ConstantesBD.codigoNuncaValidoDouble;
		this.codigoSolicitudSubCuenta=ConstantesBD.codigoNuncaValidoDouble;
		this.observaciones="";
		this.filtrarSoloCantidadesMayoresCero=false;
		this.detCxHonorarios=ConstantesBD.codigoNuncaValido;
		this.detAsocioCxSalasMat=ConstantesBD.codigoNuncaValido;
		this.esPortatil="";	
		this.dejarExcento="";
		
		this.porcentajeDctoPromocionServicio=0;
		this.valorDescuentoPromocionServicio=BigDecimal.ZERO;
		this.porcentajeHonorarioPromocionServicio=0;
		this.valorHonorarioPromocionServicio=BigDecimal.ZERO;
		
		this.programa=0;
		this.valorDescuentoBono= BigDecimal.ZERO;
		this.porcentajeDctoBono= 0;
		this.valorDescuentoOdontologico= BigDecimal.ZERO;
		this.porcentajeDctoOdontologico= 0;
		this.FHU= new DtoInfoFechaUsuario();
		this.detallePaqueteOdontoConvenio=0;
	}

	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @param codigoCargo
	 * @param codigoSubcuenta
	 * @param codigoConvenio
	 * @param codigoEsquemaTarifario
	 * @param cantidadCargada
	 * @param valorUnitarioTarifa
	 * @param valorUnitarioCargado
	 * @param valorTotalCargado
	 * @param porcentajeCargado
	 * @param porcentajeRecargo
	 * @param valorUnitarioRecargo
	 * @param porcentajeDescuento
	 * @param valorUnitarioDescuento
	 * @param valorUnitarioIva
	 * @param numeroAutorizacion
	 * @param estado
	 * @param cubierto
	 * @param tipoDistribucion
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @param codigoArticulo
	 * @param codigoServicioCx
	 * @param codigoTipoAsocio
	 * @param facturado
	 * @param codigoTipoSolicitud
	 * @param paquetizado
	 * @param cargoPadre
	 * @param codigoSolicitudSubCuenta
	 * @param observaciones
	 * @param detallePaqueteOdontoConvenio
	 */
	public DtoDetalleCargo(double codigoDetalleCargo, double codigoSubcuenta, int codigoConvenio, int codigoEsquemaTarifario, int cantidadCargada, double valorUnitarioTarifa, double valorUnitarioCargado, double valorTotalCargado, double porcentajeCargado, double porcentajeRecargo, double valorUnitarioRecargo, double porcentajeDescuento, double valorUnitarioDescuento, double valorUnitarioIva, String requiereAutorizacion, String numeroAutorizacion, int estado, String cubierto, String tipoDistribucion, int numeroSolicitud, int codigoServicio, int codigoArticulo, int codigoServicioCx, int codigoTipoAsocio, String facturado, int codigoTipoSolicitud, String paquetizado, double cargoPadre, double codigoSolicitudSubCuenta, String observaciones, int codigoContrato, boolean filtrarSoloCantidadesMayoresCero, int detCxHonorarios, int detAsocioCxSalasMat, String esPortatil, String dejarExcento, double porcentajeDctoPromocionServicio, BigDecimal valorDescuentoPromocionServicio, double porcentajeHonorarioPromocionServicio, BigDecimal valorHonorarioPromocionServicio, double programa,double porcentajeDctoBono,BigDecimal valorDescuentoBono, double porcentajeDctoOdontologico, BigDecimal	valorDescuentoOdontologico, int detallePaqueteOdontoConvenio) 
	{
		super();
		this.codigoDetalleCargo = codigoDetalleCargo;
		this.codigoSubcuenta = codigoSubcuenta;
		this.codigoConvenio = codigoConvenio;
		this.codigoContrato= codigoContrato;
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
		this.cantidadCargada = cantidadCargada;
		this.valorUnitarioTarifa = valorUnitarioTarifa;
		this.valorUnitarioCargado = valorUnitarioCargado;
		this.valorTotalCargado = valorTotalCargado;
		this.porcentajeCargado = porcentajeCargado;
		this.porcentajeRecargo = porcentajeRecargo;
		this.valorUnitarioRecargo = valorUnitarioRecargo;
		this.porcentajeDescuento = porcentajeDescuento;
		this.valorUnitarioDescuento = valorUnitarioDescuento;
		this.valorUnitarioIva = valorUnitarioIva;
		this.requiereAutorizacion= requiereAutorizacion;
		this.numeroAutorizacion = numeroAutorizacion;
		this.estado = estado;
		this.cubierto = cubierto;
		this.tipoDistribucion = tipoDistribucion;
		this.numeroSolicitud = numeroSolicitud;
		this.codigoServicio = codigoServicio;
		this.nombreServicio="";
		this.codigoArticulo = codigoArticulo;
		this.codigoServicioCx = codigoServicioCx;
		this.codigoTipoAsocio = codigoTipoAsocio;
		this.facturado = facturado;
		this.codigoTipoSolicitud = codigoTipoSolicitud;
		this.paquetizado = paquetizado;
		this.cargoPadre = cargoPadre;
		this.codigoSolicitudSubCuenta = codigoSolicitudSubCuenta;
		this.observaciones = observaciones;
		this.filtrarSoloCantidadesMayoresCero=filtrarSoloCantidadesMayoresCero;
		this.detCxHonorarios=detCxHonorarios;
		this.detAsocioCxSalasMat=detAsocioCxSalasMat;
		this.esPortatil=esPortatil;
		this.dejarExcento= dejarExcento;
		
		this.porcentajeDctoPromocionServicio=porcentajeDctoPromocionServicio;
		this.valorDescuentoPromocionServicio=valorDescuentoPromocionServicio;
		this.porcentajeHonorarioPromocionServicio=porcentajeHonorarioPromocionServicio;
		this.valorHonorarioPromocionServicio=valorHonorarioPromocionServicio;
		
		this.programa= programa;
		this.valorDescuentoBono= valorDescuentoBono;
		this.porcentajeDctoBono= porcentajeDctoBono;
		this.valorDescuentoOdontologico= valorDescuentoOdontologico;
		this.porcentajeDctoOdontologico= porcentajeDctoOdontologico;
		this.detallePaqueteOdontoConvenio=detallePaqueteOdontoConvenio;
	}



	/**
	 * @return the cantidadCargada
	 */
	public int getCantidadCargada() {
		return cantidadCargada;
	}

	/**
	 * @param cantidadCargada the cantidadCargada to set
	 */
	public void setCantidadCargada(int cantidadCargada) {
		this.cantidadCargada = cantidadCargada;
	}

	/**
	 * @return the codigoArticulo
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return the codigoEsquemaTarifario
	 */
	public int getCodigoEsquemaTarifario() {
		return codigoEsquemaTarifario;
	}

	/**
	 * @param codigoEsquemaTarifario the codigoEsquemaTarifario to set
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario) {
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the codigoSubcuenta
	 */
	public double getCodigoSubcuenta() {
		return codigoSubcuenta;
	}

	/**
	 * @param codigoSubcuenta the codigoSubcuenta to set
	 */
	public void setCodigoSubcuenta(double codigoSubcuenta) {
		this.codigoSubcuenta = codigoSubcuenta;
	}

	/**
	 * @return the cubierto
	 */
	public String getCubierto() {
		return cubierto;
	}

	/**
	 * @param cubierto the cubierto to set
	 */
	public void setCubierto(String cubierto) {
		this.cubierto = cubierto;
	}

	/**
	 * @return the estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}

	/**
	 * @return the facturado
	 */
	public String getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	
	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	
	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the porcentajeCargado
	 */
	public double getPorcentajeCargado() {
		return porcentajeCargado;
	}

	/**
	 * @param porcentajeCargado the porcentajeCargado to set
	 */
	public void setPorcentajeCargado(double porcentajeCargado) {
		this.porcentajeCargado = porcentajeCargado;
	}

	/**
	 * @return the porcentajeDescuento
	 */
	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	/**
	 * @param porcentajeDescuento the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	/**
	 * @return the porcentajeRecargo
	 */
	public double getPorcentajeRecargo() {
		return porcentajeRecargo;
	}

	/**
	 * @param porcentajeRecargo the porcentajeRecargo to set
	 */
	public void setPorcentajeRecargo(double porcentajeRecargo) {
		this.porcentajeRecargo = porcentajeRecargo;
	}

	

	/**
	 * @return the codigoServicioCx
	 */
	public int getCodigoServicioCx() {
		return codigoServicioCx;
	}

	/**
	 * @param codigoServicioCx the codigoServicioCx to set
	 */
	public void setCodigoServicioCx(int codigoServicioCx) {
		this.codigoServicioCx = codigoServicioCx;
	}

	/**
	 * @return the tipoDistribucion
	 */
	public String getTipoDistribucion() {
		return tipoDistribucion;
	}

	/**
	 * @param tipoDistribucion the tipoDistribucion to set
	 */
	public void setTipoDistribucion(String tipoDistribucion) {
		this.tipoDistribucion = tipoDistribucion;
	}

	/**
	 * @return the valorTotalCargado
	 */
	public double getValorTotalCargado() {
		return valorTotalCargado;
	}

	/**
	 * @param valorTotalCargado the valorTotalCargado to set
	 */
	public void setValorTotalCargado(double valorTotalCargado) {
		this.valorTotalCargado = valorTotalCargado;
	}

	/**
	 * @return the valorUnitarioCargado
	 */
	public double getValorUnitarioCargado() {
		return valorUnitarioCargado;
	}

	/**
	 * @return the valorUnitarioCargado
	 */
	public BigDecimal getValorUnitarioCargadoBigDecimal() {
		return (valorUnitarioCargado>0)?new BigDecimal(valorUnitarioCargado): BigDecimal.ZERO; 
	}
	
	
	/**
	 * @param valorUnitarioCargado the valorUnitarioCargado to set
	 */
	public void setValorUnitarioCargado(double valorUnitarioCargado) {
		this.valorUnitarioCargado = valorUnitarioCargado;
	}

	/**
	 * @return the valorUnitarioDescuento
	 */
	public double getValorUnitarioDescuento() {
		return valorUnitarioDescuento;
	}

	/**
	 * @return the valorUnitarioDescuento
	 */
	public BigDecimal getValorUnitarioDescuentoBigDecimal() {
		return (valorUnitarioDescuento>0)?new BigDecimal(valorUnitarioDescuento):BigDecimal.ZERO;
	}
	
	/**
	 * @param valorUnitarioDescuento the valorUnitarioDescuento to set
	 */
	public void setValorUnitarioDescuento(double valorUnitarioDescuento) {
		this.valorUnitarioDescuento = valorUnitarioDescuento;
	}

	/**
	 * @return the valorUnitarioIva
	 */
	public double getValorUnitarioIva() {
		return valorUnitarioIva;
	}

	/**
	 * @param valorUnitarioIva the valorUnitarioIva to set
	 */
	public void setValorUnitarioIva(double valorUnitarioIva) {
		this.valorUnitarioIva = valorUnitarioIva;
	}

	/**
	 * @return the valorUnitarioRecargo
	 */
	public double getValorUnitarioRecargo() {
		return valorUnitarioRecargo;
	}

	/**
	 * @return the valorUnitarioRecargo
	 */
	public BigDecimal getValorUnitarioRecargoBigDecimal() {
		return (valorUnitarioRecargo>0)?new BigDecimal(valorUnitarioRecargo):BigDecimal.ZERO;
	}
	
	/**
	 * @param valorUnitarioRecargo the valorUnitarioRecargo to set
	 */
	public void setValorUnitarioRecargo(double valorUnitarioRecargo) {
		this.valorUnitarioRecargo = valorUnitarioRecargo;
	}

	/**
	 * @return the valorUnitarioTarifa
	 */
	public double getValorUnitarioTarifa() {
		return valorUnitarioTarifa;
	}

	/**
	 * @param valorUnitarioTarifa the valorUnitarioTarifa to set
	 */
	public void setValorUnitarioTarifa(double valorUnitarioTarifa) {
		this.valorUnitarioTarifa = valorUnitarioTarifa;
	}

	/**
	 * @return the codigoDetalleCargo
	 */
	public double getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	/**
	 * @param codigoDetalleCargo the codigoDetalleCargo to set
	 */
	public void setCodigoDetalleCargo(double codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	/**
	 * @return the codigoTipoAsocio
	 */
	public int getCodigoTipoAsocio() {
		return codigoTipoAsocio;
	}

	/**
	 * @param codigoTipoAsocio the codigoTipoAsocio to set
	 */
	public void setCodigoTipoAsocio(int codigoTipoAsocio) {
		this.codigoTipoAsocio = codigoTipoAsocio;
	}

	/**
	 * @return the cargoPadre
	 */
	public double getCargoPadre() {
		return cargoPadre;
	}

	/**
	 * @param cargoPadre the cargoPadre to set
	 */
	public void setCargoPadre(double cargoPadre) {
		this.cargoPadre = cargoPadre;
	}

	/**
	 * @return the codigoTipoSolicitud
	 */
	public int getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}

	/**
	 * @param codigoTipoSolicitud the codigoTipoSolicitud to set
	 */
	public void setCodigoTipoSolicitud(int codigoTipoSolicitud) {
		this.codigoTipoSolicitud = codigoTipoSolicitud;
	}

	/**
	 * @return the paquetizado
	 */
	public String getPaquetizado() {
		return paquetizado;
	}

	/**
	 * @param paquetizado the paquetizado to set
	 */
	public void setPaquetizado(String paquetizado) {
		this.paquetizado = paquetizado;
	}

	/**
	 * @return the codigoSolicitudSubCuenta
	 */
	public double getCodigoSolicitudSubCuenta() {
		return codigoSolicitudSubCuenta;
	}

	/**
	 * @param codigoSolicitudSubCuenta the codigoSolicitudSubCuenta to set
	 */
	public void setCodigoSolicitudSubCuenta(double codigoSolicitudSubCuenta) {
		this.codigoSolicitudSubCuenta = codigoSolicitudSubCuenta;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	/**
	 * @return the requiereAutorizacion
	 */
	public String getRequiereAutorizacion() {
		return requiereAutorizacion;
	}


	/**
	 * @param requiereAutorizacion the requiereAutorizacion to set
	 */
	public void setRequiereAutorizacion(String requiereAutorizacion) {
		this.requiereAutorizacion = requiereAutorizacion;
	}


	/**
	 * @return the nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}


	/**
	 * @param nombreServicio the nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}


	/**
	 * @return the codigoContrato
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}


	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}


	/**
	 * @return the filtrarSoloCantidadesMayoresCero
	 */
	public boolean getFiltrarSoloCantidadesMayoresCero() {
		return filtrarSoloCantidadesMayoresCero;
	}


	/**
	 * @param filtrarSoloCantidadesMayoresCero the filtrarSoloCantidadesMayoresCero to set
	 */
	public void setFiltrarSoloCantidadesMayoresCero(
			boolean filtrarSoloCantidadesMayoresCero) {
		this.filtrarSoloCantidadesMayoresCero = filtrarSoloCantidadesMayoresCero;
	}


	/**
	 * @return the detAsocioCxSalasMat
	 */
	public int getDetAsocioCxSalasMat() {
		return detAsocioCxSalasMat;
	}


	/**
	 * @param detAsocioCxSalasMat the detAsocioCxSalasMat to set
	 */
	public void setDetAsocioCxSalasMat(int detAsocioCxSalasMat) {
		this.detAsocioCxSalasMat = detAsocioCxSalasMat;
	}


	/**
	 * @return the detCxHonorarios
	 */
	public int getDetCxHonorarios() {
		return detCxHonorarios;
	}


	/**
	 * @param detCxHonorarios the detCxHonorarios to set
	 */
	public void setDetCxHonorarios(int detCxHonorarios) {
		this.detCxHonorarios = detCxHonorarios;
	}


	/**
	 * @return the esPortatil
	 */
	public String getEsPortatil() {
		return esPortatil;
	}


	/**
	 * @param esPortatil the esPortatil to set
	 */
	public void setEsPortatil(String esPortatil) {
		this.esPortatil = esPortatil;
	}


	/**
	 * @return the dejarExcento
	 */
	public String getDejarExcento() {
		return dejarExcento;
	}


	/**
	 * @param dejarExcento the dejarExcento to set
	 */
	public void setDejarExcento(String dejarExcento) {
		this.dejarExcento = dejarExcento;
	}


	/**
	 * @return the porcentajeDctoPromocionServicio
	 */
	public double getPorcentajeDctoPromocionServicio()
	{
		return porcentajeDctoPromocionServicio;
	}


	/**
	 * @return the valorDescuentoPromocionServicio
	 */
	public BigDecimal getValorDescuentoPromocionServicio()
	{
		return valorDescuentoPromocionServicio;
	}


	/**
	 * @return the porcentajeHonorarioPromocionServicio
	 */
	public double getPorcentajeHonorarioPromocionServicio()
	{
		return porcentajeHonorarioPromocionServicio;
	}


	/**
	 * @return the valorHonorarioPromocionServicio
	 */
	public BigDecimal getValorHonorarioPromocionServicio()
	{
		return valorHonorarioPromocionServicio;
	}


	/**
	 * @param porcentajeDctoPromocionServicio the porcentajeDctoPromocionServicio to set
	 */
	public void setPorcentajeDctoPromocionServicio(
			double porcentajeDctoPromocionServicio)
	{
		this.porcentajeDctoPromocionServicio = porcentajeDctoPromocionServicio;
	}


	/**
	 * @param valorDescuentoPromocionServicio the valorDescuentoPromocionServicio to set
	 */
	public void setValorDescuentoPromocionServicio(
			BigDecimal valorDescuentoPromocionServicio)
	{
		this.valorDescuentoPromocionServicio = valorDescuentoPromocionServicio;
	}


	/**
	 * @param porcentajeHonorarioPromocionServicio the porcentajeHonorarioPromocionServicio to set
	 */
	public void setPorcentajeHonorarioPromocionServicio(
			double porcentajeHonorarioPromocionServicio)
	{
		this.porcentajeHonorarioPromocionServicio = porcentajeHonorarioPromocionServicio;
	}


	/**
	 * @param valorHonorarioPromocionServicio the valorHonorarioPromocionServicio to set
	 */
	public void setValorHonorarioPromocionServicio(
			BigDecimal valorHonorarioPromocionServicio)
	{
		this.valorHonorarioPromocionServicio = valorHonorarioPromocionServicio;
	}


	/**
	 * @return the programa
	 */
	public double getPrograma() {
		return programa;
	}


	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(double programa) {
		this.programa = programa;
	}


	/**
	 * @return the porcentajeDctoBono
	 */
	public double getPorcentajeDctoBono() {
		return porcentajeDctoBono;
	}


	/**
	 * @param porcentajeDctoBono the porcentajeDctoBono to set
	 */
	public void setPorcentajeDctoBono(double porcentajeDctoBono) {
		this.porcentajeDctoBono = porcentajeDctoBono;
	}


	/**
	 * @return the valorDescuentoBono
	 */
	public BigDecimal getValorDescuentoBono() {
		return valorDescuentoBono;
	}


	/**
	 * @param valorDescuentoBono the valorDescuentoBono to set
	 */
	public void setValorDescuentoBono(BigDecimal valorDescuentoBono) {
		this.valorDescuentoBono = valorDescuentoBono;
	}


	/**
	 * @return the porcentajeDctoOdontologico
	 */
	public double getPorcentajeDctoOdontologico() {
		return porcentajeDctoOdontologico;
	}


	/**
	 * @param porcentajeDctoOdontologico the porcentajeDctoOdontologico to set
	 */
	public void setPorcentajeDctoOdontologico(double porcentajeDctoOdontologico) {
		this.porcentajeDctoOdontologico = porcentajeDctoOdontologico;
	}


	/**
	 * @return the valorDescuentoOdontologico
	 */
	public BigDecimal getValorDescuentoOdontologico() {
		return valorDescuentoOdontologico;
	}


	/**
	 * @param valorDescuentoOdontologico the valorDescuentoOdontologico to set
	 */
	public void setValorDescuentoOdontologico(BigDecimal valorDescuentoOdontologico) {
		this.valorDescuentoOdontologico = valorDescuentoOdontologico;
	}
	
	/**
	 * sumatoria del valor unitario neto cargado con descuentos y recargos
	 * @return
	 */
	public BigDecimal getValorUnitarioNeto()
	{
		BigDecimal valor= BigDecimal.ZERO;
		valor= this.getValorUnitarioCargadoBigDecimal()
						 .add(this.getValorUnitarioRecargoBigDecimal())	
						 .subtract(this.getValorUnitarioDctosUnitarioNetoTodos());
		return valor;
	}
	
	/**
	 * segun documentacion es sumandole los recargos
	 * @return
	 */
	public BigDecimal getValorTotalBrutoMasRecargos() 
	{
		return this.getValorUnitarioBrutoMasRecargos().multiply(new BigDecimal(this.getCantidadCargada()));
	}
	
	/**
	 * segun documentacion es sumandole los recargos
	 * @return
	 */
	public BigDecimal getValorUnitarioBrutoMasRecargos() 
	{
		BigDecimal valor= BigDecimal.ZERO;
		valor= (this.getValorUnitarioCargadoBigDecimal()
				.add(this.getValorUnitarioRecargoBigDecimal()));
		return valor;
	}
	
	
	/**
	 * Valor total neto del cargo con descuentos y recargos
	 * @return
	 */
	public BigDecimal getValorTotalNeto()
	{
		return this.getValorUnitarioNeto().multiply(new BigDecimal(this.getCantidadCargada()));
	}

	/**
	 * Valor total neto del cargo con descuentos y recargos
	 * @return
	 */
	public BigDecimal getValorTotalDctoTodos()
	{
		return this.getValorUnitarioDctosUnitarioNetoTodos().multiply(new BigDecimal(this.getCantidadCargada()));
	}

	
	/**
	 * Valor total neto de los dctos del cargo 
	 * @return
	 */
	public BigDecimal getValorUnitarioDctosUnitarioNetoTodos()
	{
		BigDecimal valor= BigDecimal.ZERO;
		valor= 	this.getValorDescuentoBono()
		 		.add(this.getValorDescuentoOdontologico())
		 		.add(this.getValorDescuentoPromocionServicio())
		 		.add(this.getValorUnitarioDescuentoBigDecimal());
		return valor;
	}


	/**
	 * @return the fHU
	 */
	public DtoInfoFechaUsuario getFHU() {
		return FHU;
	}


	/**
	 * @param fHU the fHU to set
	 */
	public void setFHU(DtoInfoFechaUsuario fHU) {
		FHU = fHU;
	}


	/**
	 * @return the detallePaqueteOdontoConvenio
	 */
	public int getDetallePaqueteOdontoConvenio() {
		return detallePaqueteOdontoConvenio;
	}


	/**
	 * @param detallePaqueteOdontoConvenio the detallePaqueteOdontoConvenio to set
	 */
	public void setDetallePaqueteOdontoConvenio(int detallePaqueteOdontoConvenio) {
		this.detallePaqueteOdontoConvenio = detallePaqueteOdontoConvenio;
	}




	
}