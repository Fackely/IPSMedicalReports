package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

/**
 * Clase que almacena los valores que han sido ingresados por el usuario
 * en la funcionalidad Reporte Estadisticos - Egresos y Estancias para generar la consulta
 * y reportes  
 * @author Fabi�n Becerra
 *
 */
public class DtoFiltroReporteIndicadoresHospitalizacion implements Serializable{

	
	private int consecutivoCentroAtencion;
	private String fechaInicial;
	private String fechaFinal;
	private int codigoViaIngreso;
	private String codigoTipoPaciente;
	private int codigoSexo;
	private int codigoConvenio;
	
	private String rutaLogo;
	private String razonSocial;
	private String nit;
	private String actividadEconomica;
	private String direccion;
	private String telefono;
	private String nombreUsuario;
	
	private String descripcionCentroAtencion;
	private String descripcionViaIngreso;
	private String descripcionTipoPaciente;
	private String descripcionSexo;
	private String descripcionConvenio;
	private String descripcionDiagnosticosEgreso;
	
	/**
	 * M�todo constructor de la clase
	 */
	public DtoFiltroReporteIndicadoresHospitalizacion() {
	}
	
	/**
	 * M�todo que retorna el valor del atributo descripcionDiagnosticosEgreso
	 * @return descripcionDiagnosticosEgreso
	 */
	public String getDescripcionDiagnosticosEgreso() {
		return descripcionDiagnosticosEgreso;
	}


	/**
	 * M�todo que almacena el valor del atributo descripcionDiagnosticosEgreso
	 * @param descripcionDiagnosticosEgreso
	 */
	public void setDescripcionDiagnosticosEgreso(
			String descripcionDiagnosticosEgreso) {
		this.descripcionDiagnosticosEgreso = descripcionDiagnosticosEgreso;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionConvenio
	 * @return descripcionConvenio
	 */
	public String getDescripcionConvenio() {
		return descripcionConvenio;
	}


	/**
	 * M�todo que almacena el valor del atributo descripcionConvenio
	 * @param descripcionConvenio
	 */
	public void setDescripcionConvenio(String descripcionConvenio) {
		this.descripcionConvenio = descripcionConvenio;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionSexo
	 * @return descripcionSexo
	 */
	public String getDescripcionSexo() {
		return descripcionSexo;
	}


	/**
	 * M�todo que almacena el valor del atributo descripcionSexo
	 * @param descripcionSexo
	 */
	public void setDescripcionSexo(String descripcionSexo) {
		this.descripcionSexo = descripcionSexo;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionTipoPaciente
	 * @return descripcionTipoPaciente
	 */
	public String getDescripcionTipoPaciente() {
		return descripcionTipoPaciente;
	}


	/**
	 * M�todo que almacena el valor del atributo descripcionTipoPaciente
	 * @param descripcionTipoPaciente
	 */
	public void setDescripcionTipoPaciente(String descripcionTipoPaciente) {
		this.descripcionTipoPaciente = descripcionTipoPaciente;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionViaIngreso
	 * @return descripcionViaIngreso
	 */
	public String getDescripcionViaIngreso() {
		return descripcionViaIngreso;
	}


	/**
	 * M�todo que almacena el valor del atributo descripcionViaIngreso
	 * @param descripcionViaIngreso
	 */
	public void setDescripcionViaIngreso(String descripcionViaIngreso) {
		this.descripcionViaIngreso = descripcionViaIngreso;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionCentroAtencion
	 * @return descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}


	/**
	 * M�todo que almacena el valor del atributo descripcionCentroAtencion
	 * @param descripcionCentroAtencion
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}


	/**
	 * M�todo que retorna el valor del atributo rutaLogo
	 * @return rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}


	/**
	 * M�todo que almacena el valor del atributo rutaLogo
	 * @param rutaLogo
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	/**
	 * M�todo que retorna el valor del atributo razonSocial
	 * @return razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}


	/**
	 * M�todo que almacena el valor del atributo razonSocial
	 * @param razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	/**
	 * M�todo que retorna el valor del atributo nit
	 * @return nit
	 */
	public String getNit() {
		return nit;
	}


	/**
	 * M�todo que almacena el valor del atributo nit
	 * @param nit
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}


	/**
	 * M�todo que retorna el valor del atributo actividadEconomica
	 * @return actividadEconomica
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}


	/**
	 * M�todo que almacena el valor del atributo actividadEconomica
	 * @param actividadEconomica
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}


	/**
	 * M�todo que retorna el valor del atributo direccion
	 * @return direccion
	 */
	public String getDireccion() {
		return direccion;
	}


	/**
	 * M�todo que almacena el valor del atributo direccion
	 * @param direccion
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	/**
	 * M�todo que retorna el valor del atributo telefono
	 * @return telefono
	 */
	public String getTelefono() {
		return telefono;
	}


	/**
	 * M�todo que almacena el valor del atributo telefono
	 * @param telefono
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	/**
	 * M�todo que retorna el valor del atributo nombreUsuario
	 * @return nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}


	/**
	 * M�todo que almacena el valor del atributo nombreUsuario
	 * @param nombreUsuario
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * M�todo que retorna el valor del atributo consecutivoCentroAtencion
	 * @return consecutivoCentroAtencion
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}


	/**
	 * M�todo que almacena el valor del atributo consecutivoCentroAtencion
	 * @param consecutivoCentroAtencion
	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}


	/**
	 * M�todo que retorna el valor del atributo fechaInicial
	 * @return fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * M�todo que almacena el valor del atributo fechaInicial
	 * @param fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * M�todo que retorna el valor del atributo fechaFinal
	 * @return fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * M�todo que almacena el valor del atributo fechaFinal
	 * @param fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * M�todo que retorna el valor del atributo codigoViaIngreso
	 * @return codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}


	/**
	 * M�todo que almacena el valor del atributo codigoViaIngreso
	 * @param codigoViaIngreso
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}


	/**
	 * M�todo que retorna el valor del atributo codigoTipoPaciente
	 * @return codigoTipoPaciente
	 */
	public String getCodigoTipoPaciente() {
		return codigoTipoPaciente;
	}


	/**
	 * M�todo que almacena el valor del atributo codigoTipoPaciente
	 * @param codigoTipoPaciente
	 */
	public void setCodigoTipoPaciente(String codigoTipoPaciente) {
		this.codigoTipoPaciente = codigoTipoPaciente;
	}


	/**
	 * M�todo que retorna el valor del atributo codigoSexo
	 * @return codigoSexo
	 */
	public int getCodigoSexo() {
		return codigoSexo;
	}


	/**
	 * M�todo que almacena el valor del atributo codigoSexo
	 * @param codigoSexo
	 */
	public void setCodigoSexo(int codigoSexo) {
		this.codigoSexo = codigoSexo;
	}


	/**
	 * M�todo que retorna el valor del atributo codigoConvenio
	 * @return codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	/**
	 * M�todo que almacena el valor del atributo codigoConvenio
	 * @param codigoConvenio
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	
	
	
}
