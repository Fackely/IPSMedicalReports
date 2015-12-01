/*
 * Abril 21, 2009
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * DTO que modela la tabla det_autorizaciones
 * @author Sebastián Gómez R.
 *
 */
public class DtoDetAutorizacion implements Serializable
{
	private String codigoPK;
	private String codigoPkAnterior;
	private String autorizacion;
	private String numeroSolicitud;
	private String tipoSolicitud;
	private String ordenAmbulatoria;
	private String detCargo;
	private InfoDatosInt tipoAsocio;
	private InfoDatosInt serviciocx;
	private String estadoHCSerArt;
	private String estadoSolDetAuto;
	private int esServicio;
	private String activo;
	private InfoDatosInt servicioArticulo;
	private int cantidad;
	private String justificacionSolicitud;
	private String fechaSolicitaOrden;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	private DtoRespAutorizacion respuestaDto;
	private String tipoServicio;
	private int cantidadAutorizacion;
	
	private ArrayList<DtoEnvioAutorizacion> envios;
	 
	//atributos para evaluar
	private String codigoEvaluar;
	private String tipoOrden;
	
	// atributos para informe tecnico
	private String codigoCUPS;
	
	//Indica si la orden es urgente
	private String urgente;
	
	
	/**
	 * Constructor
	 */
	public DtoDetAutorizacion()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPK = "";
		this.codigoPkAnterior = "";
		this.autorizacion = "";
		this.numeroSolicitud = "";
		this.ordenAmbulatoria = "";
		this.detCargo = "";
		this.servicioArticulo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.tipoAsocio = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.serviciocx = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.cantidad = 0;
		this.esServicio = 0;
		this.estadoHCSerArt = "";
		this.estadoSolDetAuto = "";
		this.activo = ConstantesBD.acronimoNo; 
		this.justificacionSolicitud = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
		this.codigoEvaluar = "";
		this.tipoOrden = ""; 
		this.respuestaDto = new DtoRespAutorizacion();
		this.tipoServicio = "";
		this.fechaSolicitaOrden = "";		
		this.tipoSolicitud = "";
		this.envios = new ArrayList<DtoEnvioAutorizacion>();
		this.cantidadAutorizacion = 0;
		this.urgente = "";
		// atributo informe tecnico
		this.codigoCUPS = "";
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the autorizacion
	 */
	public String getAutorizacion() {
		return autorizacion;
	}

	/**
	 * @param autorizacion the autorizacion to set
	 */
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
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
	 * @return the ordenAmbulatoria
	 */
	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	/**
	 * @param ordenAmbulatoria the ordenAmbulatoria to set
	 */
	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	/**
	 * @return the detCargo
	 */
	public String getDetCargo() {
		return detCargo;
	}

	/**
	 * @param detCargo the detCargo to set
	 */
	public void setDetCargo(String detCargo) {
		this.detCargo = detCargo;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicioArticulo() {
		return servicioArticulo;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicioArticulo(InfoDatosInt servicioArticulo) {
		this.servicioArticulo = servicioArticulo;
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

	/**
	 * @return the justificacionSolicitud
	 */
	public String getJustificacionSolicitud() {
		return justificacionSolicitud;
	}

	/**
	 * @param justificacionSolicitud the justificacionSolicitud to set
	 */
	public void setJustificacionSolicitud(String justificacionSolicitud) {
		this.justificacionSolicitud = justificacionSolicitud;
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
	
	// 1 -> servicio_cx o asocios
	// 2 -> servicio interconsulta o procedimiento
	// 3 -> articulo
	/**
	 * @return the esServicio
	 */
	public int getEsServicio() {
		return esServicio;
	}
	/**
	 * @param esServicio the esServicio to set
	 */
	public void setEsServicio(int esServicio) {
		this.esServicio = esServicio;
	}

	
	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(InfoDatosInt tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}
	/**
	 * @return the tipoAsocio
	 */
	public InfoDatosInt getTipoAsocio() {
		return tipoAsocio;
	}
	/**
	 * @param TipoAsocio the TipoAsocio to set
	 */
	public void setCodigoTipoAsocio(int tipoAsocio) {
		this.tipoAsocio.setCodigo(tipoAsocio);
	}
	/**
	 * @return the TipoAsocio
	 */
	public int getCodigoTipoAsocio() {
		return tipoAsocio.getCodigo();
	}
	/**
	 * @param TipoAsocio the TipoAsocio to set
	 */
	public void setNombreTipoAsocio(String tipoAsocio) {
		this.tipoAsocio.setNombre(tipoAsocio);
	}
	/**
	 * @return the TipoAsocio
	 */
	public String getNombreTipoAsocio() {
		return tipoAsocio.getNombre();
	}
	
	/**
	 * @param serviciocx the serviciocx to set
	 */
	public void setServicioCx(InfoDatosInt serviciocx) {
		this.serviciocx = serviciocx;
	}
	/**
	 * @return the serviciocx
	 */
	public InfoDatosInt getServicioCx() {
		return serviciocx;
	}
	/**
	 * @param serviciocx the serviciocx to set
	 */
	public void setCodigoServicioCx(int serviciocx) {
		this.serviciocx.setCodigo(serviciocx);
	}
	/**
	 * @return the serviciocx
	 */
	public int getCodigoServicioCx() {
		return serviciocx.getCodigo();
	}
	/**
	 * @param serviciocx the serviciocx to set
	 */
	public void setNombreServicioCx(String serviciocx) {
		this.serviciocx.setNombre(serviciocx);
	}
	/**
	 * @return the serviciocx
	 */
	public String getNombreServicioCx() {
		return serviciocx.getNombre();
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	public InfoDatosInt getServiciocx() {
		return serviciocx;
	}

	public void setServiciocx(InfoDatosInt serviciocx) {
		this.serviciocx = serviciocx;
	}

	public String getCodigoEvaluar() {
		return codigoEvaluar;
	}

	public void setCodigoEvaluar(String codigoEvaluar) {
		this.codigoEvaluar = codigoEvaluar;
	}	
	
	public DtoRespAutorizacion getRespuestaDto() {
		return respuestaDto;
	}

	public void setRespuestaDto(DtoRespAutorizacion respuestaDto) {
		this.respuestaDto = respuestaDto;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public String getFechaSolicitaOrden() {
		return fechaSolicitaOrden;
	}

	public void setFechaSolicitaOrden(String fechaSolicitaOrden) {
		this.fechaSolicitaOrden = fechaSolicitaOrden;
	}

	public String getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public ArrayList<DtoEnvioAutorizacion> getEnvios() {
		return envios;
	}

	public void setEnvios(ArrayList<DtoEnvioAutorizacion> envios) {
		this.envios = envios;
	}

	/**
	 * @return the cantidadAutorizacion
	 */
	public int getCantidadAutorizacion() {
		return cantidadAutorizacion;
	}

	/**
	 * @param cantidadAutorizacion the cantidadAutorizacion to set
	 */
	public void setCantidadAutorizacion(int cantidadAutorizacion) {
		this.cantidadAutorizacion = cantidadAutorizacion;
	}
	
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	public String getTipoOrden() {
		return tipoOrden;
	}

	/**
	 * @return the codigoCUPS
	 */
	public String getCodigoCUPS() {
		return codigoCUPS;
	}

	/**
	 * @param codigoCUPS the codigoCUPS to set
	 */
	public void setCodigoCUPS(String codigoCUPS) {
		this.codigoCUPS = codigoCUPS;
	}

	public String getCodigoPkAnterior() {
		return codigoPkAnterior;
	}

	public void setCodigoPkAnterior(String codigoPkAnterior) {
		this.codigoPkAnterior = codigoPkAnterior;
	}

	public String getEstadoSolDetAuto() {
		return estadoSolDetAuto;
	}

	public void setEstadoSolDetAuto(String estadoSolDetAuto) {
		this.estadoSolDetAuto = estadoSolDetAuto;
	}

	public String getEstadoHCSerArt() {
		return estadoHCSerArt;
	}

	public void setEstadoHCSerArt(String estadoHCSerArt) {
		this.estadoHCSerArt = estadoHCSerArt;
	}

	/**
	 * @return the urgente
	 */
	public String getUrgente() {
		return urgente;
	}

	/**
	 * @param urgente the urgente to set
	 */
	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}
	
	
}
