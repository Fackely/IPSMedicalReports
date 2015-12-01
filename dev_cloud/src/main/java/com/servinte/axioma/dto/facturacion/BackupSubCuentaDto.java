package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hermorhu
 * @created 24-Nov-2012 
 */
public class BackupSubCuentaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1718530652665027927L;
	private long logDistribucionCuenta;
	private int idSubCuenta;
	private int convenio;
	private Integer naturalezaPaciente;
	private Integer montoCobro;
	private String nroPoliza;
	private String nroCarnet;
	private Date fechaModifica;
	private String usuarioModifica;
	private int contrato;
	private int ingreso;
	private Character tipoAfiliado;
	private Integer clasificacionSocioeconomica;
	private String nroAutorizacion;
	private Date fechaAfiliacion;
	private Integer semanasCotizacion;
	private int codigoPaciente;
	private BigDecimal valorUtilizadoSoat;
	private int nroPrioridad;
	private BigDecimal porcentajeAutorizado;
	private BigDecimal montoAutorizado;
	private String obsParametrosDistribucion;
	private char facturado;
	private String horaModifica;
	private Long empresasInstitucion;
	private Long numeroSolicitudVolante;
	private Integer mesesCotizacion;
	private Integer tipoCobertura;
	private BigDecimal valorAutorizacion;
	private String medioAutorizacion;
	private Long bono;
	private String tipoCobroPaciente;
	private String tipoMontoCobro;
	private BigDecimal porcentajeMontoCobro;
	private String migrado;
	
	/**
	 * 
	 */
	public BackupSubCuentaDto() {
		super();
	}

	/**
	 * @param logDistribucionCuenta
	 * @param idSubCuenta
	 * @param convenio
	 * @param naturalezaPaciente
	 * @param montoCobro
	 * @param nroPoliza
	 * @param nroCarnet
	 * @param fechaModifica
	 * @param usuarioModifica
	 * @param contrato
	 * @param ingreso
	 * @param tipoAfiliado
	 * @param clasificacionSocioeconomica
	 * @param nroAutorizacion
	 * @param fechaAfiliacion
	 * @param semanasCotizacion
	 * @param codigoPaciente
	 * @param valorUtilizadoSoat
	 * @param nroPrioridad
	 * @param porcentajeAutorizado
	 * @param montoAutorizado
	 * @param obsParametrosDistribucion
	 * @param facturado
	 * @param horaModifica
	 * @param empresasInstitucion
	 * @param numeroSolicitudVolante
	 * @param mesesCotizacion
	 * @param tipoCobertura
	 * @param valorAutorizacion
	 * @param medioAutorizacion
	 * @param bono
	 * @param tipoCobroPaciente
	 * @param tipoMontoCobro
	 * @param porcentajeMontoCobro
	 * @param migrado
	 */
	public BackupSubCuentaDto(int idSubCuenta, int ingreso, Integer naturalezaPaciente,
			Integer montoCobro, int contrato, int codigoPaciente, Integer clasificacionSocioeconomica,
			String usuarioModifica, Character tipoAfiliado, Integer tipoCobertura,
			int convenio, Long empresasInstitucion, String nroPoliza, String nroCarnet,
			Date fechaModifica, String nroAutorizacion, Date fechaAfiliacion, Integer semanasCotizacion,
			BigDecimal valorUtilizadoSoat, int nroPrioridad, BigDecimal porcentajeAutorizado,
			BigDecimal montoAutorizado, String obsParametrosDistribucion, char facturado, 
			String horaModifica, Long numeroSolicitudVolante, Integer mesesCotizacion,
			BigDecimal valorAutorizacion, String medioAutorizacion, Long bono, String tipoCobroPaciente,
			String tipoMontoCobro, BigDecimal porcentajeMontoCobro,	String migrado) {
		super();
		this.idSubCuenta = idSubCuenta;
		this.ingreso = ingreso;
		this.naturalezaPaciente = naturalezaPaciente;
		this.montoCobro = montoCobro;
		this.contrato = contrato;
		this.codigoPaciente = codigoPaciente;
		this.clasificacionSocioeconomica = clasificacionSocioeconomica;
		this.usuarioModifica = usuarioModifica;
		this.tipoAfiliado = tipoAfiliado;
		this.tipoCobertura = tipoCobertura;
		this.convenio = convenio;
		this.empresasInstitucion = empresasInstitucion;
		this.nroPoliza = nroPoliza;
		this.nroCarnet = nroCarnet;
		this.fechaModifica = fechaModifica;
		this.nroAutorizacion = nroAutorizacion;
		this.fechaAfiliacion = fechaAfiliacion;
		this.semanasCotizacion = semanasCotizacion;
		this.valorUtilizadoSoat = valorUtilizadoSoat;
		this.nroPrioridad = nroPrioridad;
		this.porcentajeAutorizado = porcentajeAutorizado;
		this.montoAutorizado = montoAutorizado;
		this.obsParametrosDistribucion = obsParametrosDistribucion;
		this.facturado = facturado;
		this.horaModifica = horaModifica;
		this.numeroSolicitudVolante = numeroSolicitudVolante;
		this.mesesCotizacion = mesesCotizacion;
		this.valorAutorizacion = valorAutorizacion;
		this.medioAutorizacion = medioAutorizacion;
		this.bono = bono;
		this.tipoCobroPaciente = tipoCobroPaciente;
		this.tipoMontoCobro = tipoMontoCobro;
		this.porcentajeMontoCobro = porcentajeMontoCobro;
		this.migrado = migrado;
	}

	/**
	 * @return the logDistribucionCuenta
	 */
	public long getLogDistribucionCuenta() {
		return logDistribucionCuenta;
	}

	/**
	 * @param logDistribucionCuenta the logDistribucionCuenta to set
	 */
	public void setLogDistribucionCuenta(long logDistribucionCuenta) {
		this.logDistribucionCuenta = logDistribucionCuenta;
	}

	/**
	 * @return the idSubCuenta
	 */
	public int getIdSubCuenta() {
		return idSubCuenta;
	}

	/**
	 * @param idSubCuenta the idSubCuenta to set
	 */
	public void setIdSubCuenta(int idSubCuenta) {
		this.idSubCuenta = idSubCuenta;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the naturalezaPaciente
	 */
	public Integer getNaturalezaPaciente() {
		return naturalezaPaciente;
	}

	/**
	 * @param naturalezaPaciente the naturalezaPaciente to set
	 */
	public void setNaturalezaPaciente(Integer naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}

	/**
	 * @return the montoCobro
	 */
	public Integer getMontoCobro() {
		return montoCobro;
	}

	/**
	 * @param montoCobro the montoCobro to set
	 */
	public void setMontoCobro(Integer montoCobro) {
		this.montoCobro = montoCobro;
	}

	/**
	 * @return the nroPoliza
	 */
	public String getNroPoliza() {
		return nroPoliza;
	}

	/**
	 * @param nroPoliza the nroPoliza to set
	 */
	public void setNroPoliza(String nroPoliza) {
		this.nroPoliza = nroPoliza;
	}

	/**
	 * @return the nroCarnet
	 */
	public String getNroCarnet() {
		return nroCarnet;
	}

	/**
	 * @param nroCarnet the nroCarnet to set
	 */
	public void setNroCarnet(String nroCarnet) {
		this.nroCarnet = nroCarnet;
	}

	/**
	 * @return the fechaModifica
	 */
	public Date getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the tipoAfiliado
	 */
	public Character getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * @param tipoAfiliado the tipoAfiliado to set
	 */
	public void setTipoAfiliado(Character tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	/**
	 * @return the clasificacionSocioeconomica
	 */
	public Integer getClasificacionSocioeconomica() {
		return clasificacionSocioeconomica;
	}

	/**
	 * @param clasificacionSocioeconomica the clasificacionSocioeconomica to set
	 */
	public void setClasificacionSocioeconomica(Integer clasificacionSocioeconomica) {
		this.clasificacionSocioeconomica = clasificacionSocioeconomica;
	}

	/**
	 * @return the nroAutorizacion
	 */
	public String getNroAutorizacion() {
		return nroAutorizacion;
	}

	/**
	 * @param nroAutorizacion the nroAutorizacion to set
	 */
	public void setNroAutorizacion(String nroAutorizacion) {
		this.nroAutorizacion = nroAutorizacion;
	}

	/**
	 * @return the fechaAfiliacion
	 */
	public Date getFechaAfiliacion() {
		return fechaAfiliacion;
	}

	/**
	 * @param fechaAfiliacion the fechaAfiliacion to set
	 */
	public void setFechaAfiliacion(Date fechaAfiliacion) {
		this.fechaAfiliacion = fechaAfiliacion;
	}

	/**
	 * @return the semanasCotizacion
	 */
	public Integer getSemanasCotizacion() {
		return semanasCotizacion;
	}

	/**
	 * @param semanasCotizacion the semanasCotizacion to set
	 */
	public void setSemanasCotizacion(Integer semanasCotizacion) {
		this.semanasCotizacion = semanasCotizacion;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the valorUtilizadoSoat
	 */
	public BigDecimal getValorUtilizadoSoat() {
		return valorUtilizadoSoat;
	}

	/**
	 * @param valorUtilizadoSoat the valorUtilizadoSoat to set
	 */
	public void setValorUtilizadoSoat(BigDecimal valorUtilizadoSoat) {
		this.valorUtilizadoSoat = valorUtilizadoSoat;
	}

	/**
	 * @return the nroPrioridad
	 */
	public int getNroPrioridad() {
		return nroPrioridad;
	}

	/**
	 * @param nroPrioridad the nroPrioridad to set
	 */
	public void setNroPrioridad(int nroPrioridad) {
		this.nroPrioridad = nroPrioridad;
	}

	/**
	 * @return the porcentajeAutorizado
	 */
	public BigDecimal getPorcentajeAutorizado() {
		return porcentajeAutorizado;
	}

	/**
	 * @param porcentajeAutorizado the porcentajeAutorizado to set
	 */
	public void setPorcentajeAutorizado(BigDecimal porcentajeAutorizado) {
		this.porcentajeAutorizado = porcentajeAutorizado;
	}

	/**
	 * @return the montoAutorizado
	 */
	public BigDecimal getMontoAutorizado() {
		return montoAutorizado;
	}

	/**
	 * @param montoAutorizado the montoAutorizado to set
	 */
	public void setMontoAutorizado(BigDecimal montoAutorizado) {
		this.montoAutorizado = montoAutorizado;
	}

	/**
	 * @return the obsParametrosDistribucion
	 */
	public String getObsParametrosDistribucion() {
		return obsParametrosDistribucion;
	}

	/**
	 * @param obsParametrosDistribucion the obsParametrosDistribucion to set
	 */
	public void setObsParametrosDistribucion(String obsParametrosDistribucion) {
		this.obsParametrosDistribucion = obsParametrosDistribucion;
	}

	/**
	 * @return the facturado
	 */
	public char getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(char facturado) {
		this.facturado = facturado;
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
	 * @return the empresasInstitucion
	 */
	public Long getEmpresasInstitucion() {
		return empresasInstitucion;
	}

	/**
	 * @param empresasInstitucion the empresasInstitucion to set
	 */
	public void setEmpresasInstitucion(Long empresasInstitucion) {
		this.empresasInstitucion = empresasInstitucion;
	}

	/**
	 * @return the numeroSolicitudVolante
	 */
	public Long getNumeroSolicitudVolante() {
		return numeroSolicitudVolante;
	}

	/**
	 * @param numeroSolicitudVolante the numeroSolicitudVolante to set
	 */
	public void setNumeroSolicitudVolante(Long numeroSolicitudVolante) {
		this.numeroSolicitudVolante = numeroSolicitudVolante;
	}

	/**
	 * @return the mesesCotizacion
	 */
	public Integer getMesesCotizacion() {
		return mesesCotizacion;
	}

	/**
	 * @param mesesCotizacion the mesesCotizacion to set
	 */
	public void setMesesCotizacion(Integer mesesCotizacion) {
		this.mesesCotizacion = mesesCotizacion;
	}

	/**
	 * @return the tipoCobertura
	 */
	public Integer getTipoCobertura() {
		return tipoCobertura;
	}

	/**
	 * @param tipoCobertura the tipoCobertura to set
	 */
	public void setTipoCobertura(Integer tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}

	/**
	 * @return the valorAutorizacion
	 */
	public BigDecimal getValorAutorizacion() {
		return valorAutorizacion;
	}

	/**
	 * @param valorAutorizacion the valorAutorizacion to set
	 */
	public void setValorAutorizacion(BigDecimal valorAutorizacion) {
		this.valorAutorizacion = valorAutorizacion;
	}

	/**
	 * @return the medioAutorizacion
	 */
	public String getMedioAutorizacion() {
		return medioAutorizacion;
	}

	/**
	 * @param medioAutorizacion the medioAutorizacion to set
	 */
	public void setMedioAutorizacion(String medioAutorizacion) {
		this.medioAutorizacion = medioAutorizacion;
	}

	/**
	 * @return the bono
	 */
	public Long getBono() {
		return bono;
	}

	/**
	 * @param bono the bono to set
	 */
	public void setBono(Long bono) {
		this.bono = bono;
	}

	/**
	 * @return the tipoCobroPaciente
	 */
	public String getTipoCobroPaciente() {
		return tipoCobroPaciente;
	}

	/**
	 * @param tipoCobroPaciente the tipoCobroPaciente to set
	 */
	public void setTipoCobroPaciente(String tipoCobroPaciente) {
		this.tipoCobroPaciente = tipoCobroPaciente;
	}

	/**
	 * @return the tipoMontoCobro
	 */
	public String getTipoMontoCobro() {
		return tipoMontoCobro;
	}

	/**
	 * @param tipoMontoCobro the tipoMontoCobro to set
	 */
	public void setTipoMontoCobro(String tipoMontoCobro) {
		this.tipoMontoCobro = tipoMontoCobro;
	}

	/**
	 * @return the porcentajeMontoCobro
	 */
	public BigDecimal getPorcentajeMontoCobro() {
		return porcentajeMontoCobro;
	}

	/**
	 * @param porcentajeMontoCobro the porcentajeMontoCobro to set
	 */
	public void setPorcentajeMontoCobro(BigDecimal porcentajeMontoCobro) {
		this.porcentajeMontoCobro = porcentajeMontoCobro;
	}

	/**
	 * @return the migrado
	 */
	public String getMigrado() {
		return migrado;
	}

	/**
	 * @param migrado the migrado to set
	 */
	public void setMigrado(String migrado) {
		this.migrado = migrado;
	}
	
}
