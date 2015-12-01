/**
 * 
 */
package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Jorge Armando Osorio Velasquez.
 * 
 * DTO para el manejo de los datos de abonos, que se comparte con la interfaz.
 *
 */
public class DtoInterfazAbonos implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Codigo del paciente.
	 */
	private String codigoPaciente;
	
	/**
	 * Numero del documento (ReciboCaja-Factura)
	 */
	private String numeroDocumento;
	
	/**
	 * Tipo Movimiento (1-Generacion recibo caja, 2-generacion factura,3-anulacion factura,-anulacion recibo caja.)
	 */
	private String tipoMov;
	
	/**
	 * Define si el tipo de movimiento suma o resta para el calculo de salods (+ Generacion recibo caja, - generacion factura,+ anulacion factura,- anulacion recibo caja.)
	 */
	private String signo;
	
	/**
	 * Valor
	 */
	private String valor;
	
	/**
	 * Estado del registro (0-No  Procesado, 1-Procesado , N- (10,199) errores de incosistencias)
	 * @see util.ConstantesIncosistenciasInterfas.
	 */
	private String estadoRegistro;
	
	/**
	 * Institucion con la que se esta trabajando
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;

	/**
	 * Numero Identificacion del Paciente
	 */
	private String numIdentificacion;
	
	/**
	 * Tipo de Identificacion del Paciente
	 */
	private String tipoIdentificacion;
	

	/**
	 * @param codigoPaciente
	 * @param numeroDocumento
	 * @param tipoMov
	 * @param signo
	 * @param valor
	 * @param estadoRegistro
	 * @param institucion
	 */
	public DtoInterfazAbonos(String codigoPaciente, String numeroDocumento, String tipoMov, String signo, String valor, String estadoRegistro, int institucion,String fecha,String hora, String numIdentificacion, String tipoIdentificacion) 
	{
		this.codigoPaciente = codigoPaciente;
		this.numeroDocumento = numeroDocumento;
		this.tipoMov = tipoMov;
		this.signo = signo;
		this.valor = valor;
		this.estadoRegistro = estadoRegistro;
		this.institucion = institucion;
		this.fecha=fecha;
		this.hora=hora;
		this.numIdentificacion=numIdentificacion;
		this.tipoIdentificacion=tipoIdentificacion;
	}
	
	/**
	 * @param codigoPaciente
	 * @param numeroDocumento
	 * @param tipoMov
	 * @param signo
	 * @param valor
	 * @param estadoRegistro
	 */
	public DtoInterfazAbonos() 
	{
		this.codigoPaciente ="";
		this.numeroDocumento = "";
		this.tipoMov = "";
		this.signo = "";
		this.valor = "";
		this.estadoRegistro = "";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.fecha="";
		this.hora="";
		this.numIdentificacion="";
		this.tipoIdentificacion="";
	}

	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getSigno() {
		return signo;
	}

	public void setSigno(String signo) {
		this.signo = signo;
	}

	public String getTipoMov() {
		return tipoMov;
	}

	public void setTipoMov(String tipoMov) {
		this.tipoMov = tipoMov;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getNumIdentificacion() {
		return numIdentificacion;
	}

	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	
	
}
	

