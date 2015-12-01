package com.servinte.axioma.dto.manejoPaciente;

import java.util.Date;

/**
 * @author jeilones
 *
 */
public class ServicioAutorizadoCapitacionDto {
	
	private long tipoAutorizacion;
	private Long codigo;
	private Long consecutivo;
	private Date fechaGeneracion;
	private long idServicio;
	private int especialidad;
	private String tipoServicio;
	private int grupoServicio;
	private String codServ;
	private String nomServ;
	private String nivelAtencion;
	private long cantidad;
	private String acronimoDiag;
	private Integer tipoCieDiag;
	private String diagDescripcion;
	private Double valorTarifa;
	private String indicativoTemporal;
	private int viaIngreso;
	
	private long idIngreso;
	private long codigoCuenta;
	private String tipoPaciente;
	private int tipoSolicitud;
	private String pyp;
	
	/**
	 * @param tipoAutorizacion
	 * @param codigo
	 * @param consecutivo
	 * @param fechaGeneracion
	 * @param idServicio
	 * @param especialidad
	 * @param tipoServicio
	 * @param grupoServicio
	 * @param codServ
	 * @param nomServ
	 * @param nivelAtencion
	 * @param cantidad
	 * @param acronimoDiag
	 * @param tipoCieDiag
	 * @param diagDescripcion
	 * @param valorTarifa
	 * @param indicativoTemporal
	 * @param viaIngreso
	 * @author jeilones
	 * @param idIngreso 
	 * @param codigoCuenta 
	 * @param tipoPaciente 
	 * @param pyp 
	 * @created 08/08/2012
	 */
	public ServicioAutorizadoCapitacionDto(long tipoAutorizacion, Long codigo,
			Long consecutivo, Date fechaGeneracion, long idServicio, 
			int especialidad,String tipoServicio,int grupoServicio,String codServ,
			String nomServ, String nivelAtencion, long cantidad,
			String acronimoDiag, Integer tipoCieDiag, String diagDescripcion,
			Double valorTarifa, String indicativoTemporal, int viaIngreso, long idIngreso, 
			long codigoCuenta, String tipoPaciente, int tipoSolicitud, String pyp) {
		super();
		this.tipoAutorizacion = tipoAutorizacion;
		this.codigo = codigo;
		this.consecutivo = consecutivo;
		this.fechaGeneracion = fechaGeneracion;
		this.idServicio=idServicio;
		this.especialidad=especialidad;
		this.tipoServicio=tipoServicio;
		this.grupoServicio=grupoServicio;
		this.codServ = codServ;
		this.nomServ = nomServ;
		this.nivelAtencion = nivelAtencion;
		this.cantidad = cantidad;
		this.acronimoDiag = acronimoDiag;
		this.tipoCieDiag = tipoCieDiag;
		this.diagDescripcion = diagDescripcion;
		this.valorTarifa = valorTarifa;
		this.indicativoTemporal = indicativoTemporal;
		this.viaIngreso=viaIngreso;
		this.idIngreso=idIngreso;
		this.codigoCuenta=codigoCuenta;
		this.tipoPaciente=tipoPaciente;
		this.tipoSolicitud=tipoSolicitud;
		this.pyp=pyp;
	}
	/**
	 * @return the tipoAutorizacion
	 */
	public long getTipoAutorizacion() {
		return tipoAutorizacion;
	}
	/**
	 * @param tipoAutorizacion the tipoAutorizacion to set
	 */
	public void setTipoAutorizacion(long tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the consecutivo
	 */
	public Long getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return the fechaGeneracion
	 */
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	/**
	 * @param fechaGeneracion the fechaGeneracion to set
	 */
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	/**
	 * @return the codServ
	 */
	public String getCodServ() {
		return codServ;
	}
	/**
	 * @param codServ the codServ to set
	 */
	public void setCodServ(String codServ) {
		this.codServ = codServ;
	}
	/**
	 * @return the nomServ
	 */
	public String getNomServ() {
		return nomServ;
	}
	/**
	 * @param nomServ the nomServ to set
	 */
	public void setNomServ(String nomServ) {
		this.nomServ = nomServ;
	}
	/**
	 * @return the nivelAtencion
	 */
	public String getNivelAtencion() {
		return nivelAtencion;
	}
	/**
	 * @param nivelAtencion the nivelAtencion to set
	 */
	public void setNivelAtencion(String nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}
	/**
	 * @return the cantidad
	 */
	public long getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return the acronimoDiag
	 */
	public String getAcronimoDiag() {
		return acronimoDiag;
	}
	/**
	 * @param acronimoDiag the acronimoDiag to set
	 */
	public void setAcronimoDiag(String acronimoDiag) {
		this.acronimoDiag = acronimoDiag;
	}
	/**
	 * @return the tipoCieDiag
	 */
	public Integer getTipoCieDiag() {
		return tipoCieDiag;
	}
	/**
	 * @param tipoCieDiag the tipoCieDiag to set
	 */
	public void setTipoCieDiag(Integer tipoCieDiag) {
		this.tipoCieDiag = tipoCieDiag;
	}
	/**
	 * @return the diagDescripcion
	 */
	public String getDiagDescripcion() {
		return diagDescripcion;
	}
	/**
	 * @param diagDescripcion the diagDescripcion to set
	 */
	public void setDiagDescripcion(String diagDescripcion) {
		this.diagDescripcion = diagDescripcion;
	}
	/**
	 * @return the valorTarifa
	 */
	public Double getValorTarifa() {
		return valorTarifa;
	}
	/**
	 * @param valorTarifa the valorTarifa to set
	 */
	public void setValorTarifa(Double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}
	/**
	 * @return the indicativoTemporal
	 */
	public String getIndicativoTemporal() {
		return indicativoTemporal;
	}
	/**
	 * @param indicativoTemporal the indicativoTemporal to set
	 */
	public void setIndicativoTemporal(String indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}
	/**
	 * @return the idServicio
	 */
	public long getIdServicio() {
		return idServicio;
	}
	/**
	 * @param idServicio the idServicio to set
	 */
	public void setIdServicio(long idServicio) {
		this.idServicio = idServicio;
	}
	/**
	 * @return the especialidad
	 */
	public int getEspecialidad() {
		return especialidad;
	}
	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}
	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}
	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	/**
	 * @return the grupoServicio
	 */
	public int getGrupoServicio() {
		return grupoServicio;
	}
	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(int grupoServicio) {
		this.grupoServicio = grupoServicio;
	}
	/**
	 * @return the viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}
	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
	/**
	 * @return the idIngreso
	 */
	public long getIdIngreso() {
		return idIngreso;
	}
	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(long idIngreso) {
		this.idIngreso = idIngreso;
	}
	/**
	 * @return the codigoCuenta
	 */
	public long getCodigoCuenta() {
		return codigoCuenta;
	}
	/**
	 * @param codigoCuenta the codigoCuenta to set
	 */
	public void setCodigoCuenta(long codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}
	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}
	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}
	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	/**
	 * @return the pyp
	 */
	public String getPyp() {
		return pyp;
	}
	/**
	 * @param pyp the pyp to set
	 */
	public void setPyp(String pyp) {
		this.pyp = pyp;
	}
	
	
}
