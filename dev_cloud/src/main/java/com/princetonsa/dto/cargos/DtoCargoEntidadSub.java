/*
 * Junio 24 , 2009
 */
package com.princetonsa.dto.cargos;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * DTO que carga la informacion del cargo de la entidad subcontratada   
 * @author Sebastián Gómez R.
 *
 */
public class DtoCargoEntidadSub implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoDetalleCargo;
	private DtoEntidadSubcontratada entidad;
	private DtoContratoEntidadSub contrato;
	private String fecha;
	private String hora;
	private String numeroSolicitud;
	private int codigoArticulo;
	private String numeroPedido;
	private String valorUnitario;
	private int codigoArticuloPrincipal;
	private boolean vieneDespacho;
	private InfoDatosInt estado;
	private InfoDatosInt esquemaTarifario;
	private String consecutivoAutorizacion;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	private String observaciones;
	
	public void clean()
	{
		this.codigoDetalleCargo = "";
		this.entidad = new DtoEntidadSubcontratada();
		this.contrato = new DtoContratoEntidadSub();
		this.fecha = "";
		this.hora = "";
		this.numeroSolicitud = "";
		this.codigoArticulo = ConstantesBD.codigoNuncaValido;
		this.numeroPedido = "";
		this.valorUnitario = "";
		this.codigoArticuloPrincipal = ConstantesBD.codigoNuncaValido;
		this.vieneDespacho = false;
		this.estado = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.esquemaTarifario = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.consecutivoAutorizacion = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
		this.observaciones= "";
	}
	
	/*
	 * Constructor
	 */
	public DtoCargoEntidadSub()
	{
		this.clean();
	}

	/**
	 * @return the codigoDetalleCargo
	 */
	public String getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	/**
	 * @param codigoDetalleCargo the codigoDetalleCargo to set
	 */
	public void setCodigoDetalleCargo(String codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	/**
	 * @return the entidad
	 */
	public DtoEntidadSubcontratada getEntidad() {
		return entidad;
	}

	/**
	 * @param entidad the entidad to set
	 */
	public void setEntidad(DtoEntidadSubcontratada entidad) {
		this.entidad = entidad;
	}

	/**
	 * @return the contrato
	 */
	public DtoContratoEntidadSub getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(DtoContratoEntidadSub contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
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
	 * @return the numeroPedido
	 */
	public String getNumeroPedido() {
		return numeroPedido;
	}

	/**
	 * @param numeroPedido the numeroPedido to set
	 */
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	/**
	 * @return the valorUnitario
	 */
	public String getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the codigoArticuloPrincipal
	 */
	public int getCodigoArticuloPrincipal() {
		return codigoArticuloPrincipal;
	}

	/**
	 * @param codigoArticuloPrincipal the codigoArticuloPrincipal to set
	 */
	public void setCodigoArticuloPrincipal(int codigoArticuloPrincipal) {
		this.codigoArticuloPrincipal = codigoArticuloPrincipal;
	}

	/**
	 * @return the vieneDespacho
	 */
	public boolean isVieneDespacho() {
		return vieneDespacho;
	}

	/**
	 * @param vieneDespacho the vieneDespacho to set
	 */
	public void setVieneDespacho(boolean vieneDespacho) {
		this.vieneDespacho = vieneDespacho;
	}

	/**
	 * @return the estado
	 */
	public InfoDatosInt getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(InfoDatosInt estado) {
		this.estado = estado;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public InfoDatosInt getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(InfoDatosInt esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public UsuarioBasico getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(UsuarioBasico usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the consecutivoAutorizacion
	 */
	public String getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}

	/**
	 * @param consecutivoAutorizacion the consecutivoAutorizacion to set
	 */
	public void setConsecutivoAutorizacion(String consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
