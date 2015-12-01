package com.servinte.axioma.generadorReporte.tesoreria.dtoReportes;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDataSource;

import util.ConstantesBD;

public class DtoAnticiposRecibidosConvenioPlano implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fechaInicial;
	private String fechaFinal;
	private long codigoInstitucion;
	private String descripcionInstitucion;
	private int consecutivoCentroAtencion;
	private String descripcionCentroAtencion;
	private String descripcionCiudad;
	private String descripcionRegion;
	private String numeroIdConvenio;
	private String nombreConvenio;
	private String fechaRC;
	private String conceptoRC;
	private String nroRC;
	private String estadoRC;
	private String usuario;
	private String formaPago;
	private String valorFormateado;
	transient private JRDataSource dsListadoResultado;
	
	public DtoAnticiposRecibidosConvenioPlano() {
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValidoLong;
		this.descripcionInstitucion = "";
		this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.descripcionCentroAtencion = "";
		this.descripcionCiudad = "";
		this.descripcionRegion = "";
		this.numeroIdConvenio = "";
		this.nombreConvenio = "";
		this.fechaRC = "";
		this.conceptoRC = "";
		this.nroRC = "";
		this.estadoRC = "";
		this.usuario = "";
		this.formaPago = "";
		this.valorFormateado = "";
		
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaInicial
	 *
	 * @return retorna la variable fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaInicial
	 * @param fechaInicial es el valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaFinal
	 *
	 * @return retorna la variable fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaFinal
	 * @param fechaFinal es el valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionInstitucion
	 *
	 * @return retorna la variable descripcionInstitucion
	 */
	public String getDescripcionInstitucion() {
		return descripcionInstitucion;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionInstitucion
	 * @param descripcionInstitucion es el valor para el atributo descripcionInstitucion 
	 */
	public void setDescripcionInstitucion(String descripcionInstitucion) {
		this.descripcionInstitucion = descripcionInstitucion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  consecutivoCentroAtencion
	 *
	 * @return retorna la variable consecutivoCentroAtencion
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo consecutivoCentroAtencion
	 * @param consecutivoCentroAtencion es el valor para el atributo consecutivoCentroAtencion 
	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionCentroAtencion
	 *
	 * @return retorna la variable descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionCentroAtencion
	 * @param descripcionCentroAtencion es el valor para el atributo descripcionCentroAtencion 
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionCiudad
	 *
	 * @return retorna la variable descripcionCiudad
	 */
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionCiudad
	 * @param descripcionCiudad es el valor para el atributo descripcionCiudad 
	 */
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  descripcionRegion
	 *
	 * @return retorna la variable descripcionRegion
	 */
	public String getDescripcionRegion() {
		return descripcionRegion;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo descripcionRegion
	 * @param descripcionRegion es el valor para el atributo descripcionRegion 
	 */
	public void setDescripcionRegion(String descripcionRegion) {
		this.descripcionRegion = descripcionRegion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  nombreConvenio
	 *
	 * @return retorna la variable nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo nombreConvenio
	 * @param nombreConvenio es el valor para el atributo nombreConvenio 
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaRC
	 *
	 * @return retorna la variable fechaRC
	 */
	public String getFechaRC() {
		return fechaRC;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaRC
	 * @param fechaRC es el valor para el atributo fechaRC 
	 */
	public void setFechaRC(String fechaRC) {
		this.fechaRC = fechaRC;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  conceptoRC
	 *
	 * @return retorna la variable conceptoRC
	 */
	public String getConceptoRC() {
		return conceptoRC;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo conceptoRC
	 * @param conceptoRC es el valor para el atributo conceptoRC 
	 */
	public void setConceptoRC(String conceptoRC) {
		this.conceptoRC = conceptoRC;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  nroRC
	 *
	 * @return retorna la variable nroRC
	 */
	public String getNroRC() {
		return nroRC;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo nroRC
	 * @param nroRC es el valor para el atributo nroRC 
	 */
	public void setNroRC(String nroRC) {
		this.nroRC = nroRC;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  estadoRC
	 *
	 * @return retorna la variable estadoRC
	 */
	public String getEstadoRC() {
		return estadoRC;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo estadoRC
	 * @param estadoRC es el valor para el atributo estadoRC 
	 */
	public void setEstadoRC(String estadoRC) {
		this.estadoRC = estadoRC;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  usuario
	 *
	 * @return retorna la variable usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo usuario
	 * @param usuario es el valor para el atributo usuario 
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  formaPago
	 *
	 * @return retorna la variable formaPago
	 */
	public String getFormaPago() {
		return formaPago;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo formaPago
	 * @param formaPago es el valor para el atributo formaPago 
	 */
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  dsListadoResultado
	 *
	 * @return retorna la variable dsListadoResultado
	 */
	public JRDataSource getDsListadoResultado() {
		return dsListadoResultado;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo dsListadoResultado
	 * @param dsListadoResultado es el valor para el atributo dsListadoResultado 
	 */
	public void setDsListadoResultado(JRDataSource dsListadoResultado) {
		this.dsListadoResultado = dsListadoResultado;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  valorFormateado
	 *
	 * @return retorna la variable valorFormateado
	 */
	public String getValorFormateado() {
		return valorFormateado;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo valorFormateado
	 * @param valorFormateado es el valor para el atributo valorFormateado 
	 */
	public void setValorFormateado(String valorFormateado) {
		this.valorFormateado = valorFormateado;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  codigoInstitucion
	 *
	 * @return retorna la variable codigoInstitucion
	 */
	public long getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo codigoInstitucion
	 * @param codigoInstitucion es el valor para el atributo codigoInstitucion 
	 */
	public void setCodigoInstitucion(long codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  numeroIdConvenio
	 *
	 * @return retorna la variable numeroIdConvenio
	 */
	public String getNumeroIdConvenio() {
		return numeroIdConvenio;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo numeroIdConvenio
	 * @param numeroIdConvenio es el valor para el atributo numeroIdConvenio 
	 */
	public void setNumeroIdConvenio(String numeroIdConvenio) {
		this.numeroIdConvenio = numeroIdConvenio;
	}
}
