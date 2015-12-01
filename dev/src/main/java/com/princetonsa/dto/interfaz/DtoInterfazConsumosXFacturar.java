package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;

/**
 * 
 * @author wilson
 *
 */
public class DtoInterfazConsumosXFacturar implements Serializable 
{
	/**
	 * 
	 */
	private double valorXFacturar;
	
	/**
	 * 
	 */
	private String numeroIdentificacion;
	
	/**
	 * 
	 */
	private String codigoInterfazConvenio;
	
	/**
	 * 
	 */
	private String descripcionConvenio;
	
	/**
	 * 
	 */
	private int numeroIngreso;
	
	/**
	 * 
	 */
	private int codigoViaIngreso;
	
	/**
	 * 
	 */
	private String tipoPaciente;
	
	/**
	 * 
	 */
	private String fechaIngresoBD;
	
	/**
	 * 
	 */
	private String fechaEgresoBD;
	
	/**
	 * 
	 */
	private String primerNombrePaciente;
	
	/**
	 * 
	 */
	private String primerApellidoPaciente;
	
	/**
	 * 
	 */
	private String numeroCama;
	
	/**
	 * 
	 */
	private String loginUsuarioAdmision;
	
	/**
	 * 
	 */
	private String estadoIngreso;
	
	/**
	 * 
	 */
	private String estadoRegistro;
	
	/**
	 * 
	 */
	private String fechaRegistroBD;
	
	/**
	 * 
	 */
	private String horaRegistro;
	
	/**
	 * 
	 */
	private String fechaProcesadosBD;

	/**
	 * 
	 */
	private int institucion;
	
	
	/**
	 * 
	 */
	private String codInterfazViaIngresoTipoPac;
	
	/**
	 * 
	 */
	private String estadoIngresoShaio;
	
	private String consecutivo;
	
	
	/**
	 * 
	 */
	public DtoInterfazConsumosXFacturar() 
	{
		super();
		this.codigoInterfazConvenio = "";
		this.codigoViaIngreso = ConstantesBD.codigoNuncaValido;
		this.descripcionConvenio = "";
		this.estadoIngreso = "";
		this.estadoRegistro = "";
		this.fechaEgresoBD = "";
		this.fechaIngresoBD = "";
		this.fechaProcesadosBD = "";
		this.fechaRegistroBD = "";
		this.horaRegistro = "";
		this.loginUsuarioAdmision = "";
		this.numeroCama = "";
		this.numeroIdentificacion = "";
		this.numeroIngreso = ConstantesBD.codigoNuncaValido;
		this.primerApellidoPaciente = "";
		this.primerNombrePaciente = "";
		this.tipoPaciente="";
		this.valorXFacturar = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.codInterfazViaIngresoTipoPac="";
		this.estadoIngresoShaio="";
		this.consecutivo="";
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * 
	 * @return
	 */
	public String obtenerViaIngresoShaio()
	{
		String retorna="";
		if(!UtilidadTexto.isEmpty(this.getCodigoViaIngreso()+""))
		{
			if(this.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				retorna="01";
			else if(this.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
				retorna="02";
			else if((this.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
				&& (this.getTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado)))
				retorna="05";
			else if((this.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
				&& (this.getTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria)))
				retorna="06";
			else if((this.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios))
				retorna="08";
		}
		return retorna;
	}
	
	/**
	 * @return the estadoIngresoShaio
	 */
	public String getEstadoIngresoShaio() {
		return estadoIngresoShaio;
	}

	/**
	 * @param estadoIngresoShaio the estadoIngresoShaio to set
	 */
	public void setEstadoIngresoShaio(String estadoIngresoShaio) {
		this.estadoIngresoShaio = estadoIngresoShaio;
	}

	/**
	 * 
	 * @return
	 */
	public String obtenerEstadoIngresoShaio()
	{
		if(!UtilidadTexto.isEmpty(this.getEstadoIngreso()))
		{
			if(this.getEstadoIngreso().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
				this.setEstadoIngresoShaio("2");
			else if(this.getEstadoIngreso().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
				this.setEstadoIngresoShaio("3");
		}
		return this.getEstadoIngresoShaio();
	}
	
	
	
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the valorXFacturar
	 */
	public double getValorXFacturar() {
		return valorXFacturar;
	}

	/**
	 * @param valorXFacturar the valorXFacturar to set
	 */
	public void setValorXFacturar(double valorXFacturar) {
		this.valorXFacturar = valorXFacturar;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the codigoInterfazConvenio
	 */
	public String getCodigoInterfazConvenio() {
		return codigoInterfazConvenio;
	}

	/**
	 * @param codigoInterfazConvenio the codigoInterfazConvenio to set
	 */
	public void setCodigoInterfazConvenio(String codigoInterfazConvenio) {
		this.codigoInterfazConvenio = codigoInterfazConvenio;
	}

	/**
	 * @return the descripcionConvenio
	 */
	public String getDescripcionConvenio() {
		return descripcionConvenio;
	}

	/**
	 * @param descripcionConvenio the descripcionConvenio to set
	 */
	public void setDescripcionConvenio(String descripcionConvenio) {
		this.descripcionConvenio = descripcionConvenio;
	}

	/**
	 * @return the numeroIngreso
	 */
	public int getNumeroIngreso() {
		return numeroIngreso;
	}

	/**
	 * @param numeroIngreso the numeroIngreso to set
	 */
	public void setNumeroIngreso(int numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the fechaIngreso
	 */
	public String getFechaIngresoBD() {
		return fechaIngresoBD;
	}

	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngresoBD(String fechaIngresoBD) {
		this.fechaIngresoBD = fechaIngresoBD;
	}

	/**
	 * @return the fechaEgreso
	 */
	public String getFechaEgresoBD() {
		return fechaEgresoBD;
	}

	/**
	 * @param fechaEgreso the fechaEgreso to set
	 */
	public void setFechaEgresoBD(String fechaEgresoBD) {
		this.fechaEgresoBD = fechaEgresoBD;
	}

	/**
	 * @return the primerNombrePaciente
	 */
	public String getPrimerNombrePaciente() {
		return primerNombrePaciente;
	}

	/**
	 * @param primerNombrePaciente the primerNombrePaciente to set
	 */
	public void setPrimerNombrePaciente(String primerNombrePaciente) {
		this.primerNombrePaciente = primerNombrePaciente;
	}

	/**
	 * @return the primerApellidoPaciente
	 */
	public String getPrimerApellidoPaciente() {
		return primerApellidoPaciente;
	}

	/**
	 * @param primerApellidoPaciente the primerApellidoPaciente to set
	 */
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}

	/**
	 * @return the numeroCama
	 */
	public String getNumeroCama() {
		return numeroCama;
	}

	/**
	 * @param numeroCama the numeroCama to set
	 */
	public void setNumeroCama(String numeroCama) {
		this.numeroCama = numeroCama;
	}

	/**
	 * @return the loginUsuarioAdmision
	 */
	public String getLoginUsuarioAdmision() {
		return loginUsuarioAdmision;
	}

	/**
	 * @param loginUsuarioAdmision the loginUsuarioAdmision to set
	 */
	public void setLoginUsuarioAdmision(String loginUsuarioAdmision) {
		this.loginUsuarioAdmision = loginUsuarioAdmision;
	}

	/**
	 * @return the estadoIngreso
	 */
	public String getEstadoIngreso() {
		return estadoIngreso;
	}

	/**
	 * @param estadoIngreso the estadoIngreso to set
	 */
	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}

	/**
	 * @return the estadoRegistro
	 */
	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	/**
	 * @param estadoRegistro the estadoRegistro to set
	 */
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	/**
	 * @return the fechaRegistro
	 */
	public String getFechaRegistroBD() {
		return fechaRegistroBD;
	}

	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistroBD(String fechaRegistroBD) {
		this.fechaRegistroBD = fechaRegistroBD;
	}

	/**
	 * @return the horaRegistro
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}

	/**
	 * @param horaRegistro the horaRegistro to set
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	/**
	 * @return the fechaProcesados
	 */
	public String getFechaProcesadosBD() {
		return fechaProcesadosBD;
	}

	/**
	 * @param fechaProcesados the fechaProcesados to set
	 */
	public void setFechaProcesadosBD(String fechaProcesadosBD) {
		this.fechaProcesadosBD = fechaProcesadosBD;
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
	 * @return the codInterfazViaIngresoTipoPac
	 */
	public String getCodInterfazViaIngresoTipoPac() {
		return codInterfazViaIngresoTipoPac;
	}

	/**
	 * @param codInterfazViaIngresoTipoPac the codInterfazViaIngresoTipoPac to set
	 */
	public void setCodInterfazViaIngresoTipoPac(String codInterfazViaIngresoTipoPac) {
		this.codInterfazViaIngresoTipoPac = codInterfazViaIngresoTipoPac;
	}

	
}
