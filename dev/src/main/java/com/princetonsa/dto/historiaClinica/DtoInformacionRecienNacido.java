/*
 * Enero 16, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import com.princetonsa.mundo.atencion.Diagnostico;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;

/**
 * Data Transfer Object: información recién nacido
 * @author Sebastián Gómez R.
 *
 */
public class DtoInformacionRecienNacido implements Serializable
{
	/**
	 * Consecutivo
	 */
	private String consecutivo;
	
	/**
	 * Consecutivo Hijo
	 */
	private int consecutivoHijo;
	
	/**
	 * Fecha de nacimiento
	 */
	private String fechaNacimiento;
	
	/**
	 * Hora de nacimiento
	 */
	private String horaNacimiento;
	
	/**
	 * Sexo
	 */
	private InfoDatosInt sexo;
	
	/**
	 * Vivo
	 */
	private boolean vivo;
	
	/**
	 * Diagnostico recién nacido
	 */
	private Diagnostico diagnosticoRecienNacido;
	
	/**
	 * Fecha de muerte
	 */
	private String fechaMuerte;
	
	/**
	 * Hora de muerte
	 */
	private String horaMuerte;
	
	/**
	 * Diagnostico de muerte
	 */
	private Diagnostico diagnosticoMuerte;
	
	/**
	 * Codigo de la cirugia
	 */
	private String codigoCirugia;
	
	/**
	 * Código del ingreso
	 */
	private String codigoIngreso;
	
	/**
	 * Número de la solicitud
	 */
	private String numeroSolicitud;
	
	/**
	 * Peso al egreso
	 */
	private int pesoEgreso;
	
	/**
	 * Fecha del proceso
	 */
	private String fechaProceso;
	
	/**
	 * Hora del proceso
	 */
	private String horaProceso;
	
	/**
	 * Usuario que ejecuta el proceso
	 */
	private InfoDatos usuarioProceso;
	
	/**
	 * Indica si ya esta finalizada la información
	 */
	private boolean finalizada;
	
	/**
	 * Para verificar si el registro ya existe en la base de datos
	 */
	private boolean existeBaseDatos;
	
	/**
	 * Resetear datos del objeto
	 *
	 */
	public void clean()
	{
		this.consecutivo = "";
		this.consecutivoHijo = 0;
		this.fechaNacimiento = "";
		this.horaNacimiento = "";
		this.sexo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.vivo = false;
		this.diagnosticoRecienNacido = new Diagnostico("",ConstantesBD.codigoNuncaValido);
		this.fechaMuerte = "";
		this.horaMuerte = "";
		this.diagnosticoMuerte = new Diagnostico("",ConstantesBD.codigoNuncaValido);
		this.codigoCirugia = "";
		this.codigoIngreso = "";
		this.numeroSolicitud = "";
		this.pesoEgreso = ConstantesBD.codigoNuncaValido;
		this.fechaProceso = "";
		this.horaProceso = "";
		this.usuarioProceso = new InfoDatos("","");
		this.finalizada = false;
		this.existeBaseDatos = false;
	}
	
	/**
	 * Constructor
	 *
	 */
	public DtoInformacionRecienNacido()
	{
		this.clean();
	}

	/**
	 * @return the codigoCirugia
	 */
	public String getCodigoCirugia() {
		return codigoCirugia;
	}

	/**
	 * @param codigoCirugia the codigoCirugia to set
	 */
	public void setCodigoCirugia(String codigoCirugia) {
		this.codigoCirugia = codigoCirugia;
	}

	/**
	 * @return the codigoIngreso
	 */
	public String getCodigoIngreso() {
		return codigoIngreso;
	}

	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(String codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
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
	 * @return the consecutivoHijo
	 */
	public int getConsecutivoHijo() {
		return consecutivoHijo;
	}

	/**
	 * @param consecutivoHijo the consecutivoHijo to set
	 */
	public void setConsecutivoHijo(int consecutivoHijo) {
		this.consecutivoHijo = consecutivoHijo;
	}

	/**
	 * @return the diagnosticoMuerte
	 */
	public Diagnostico getDiagnosticoMuerte() {
		return diagnosticoMuerte;
	}

	/**
	 * @param diagnosticoMuerte the diagnosticoMuerte to set
	 */
	public void setDiagnosticoMuerte(Diagnostico diagnosticoMuerte) {
		this.diagnosticoMuerte = diagnosticoMuerte;
	}
	
	/**
	 * @return the diagnosticoMuerte
	 */
	public String getAcronimoDiagnosticoMuerte() {
		return diagnosticoMuerte.getAcronimo();
	}

	/**
	 * @param diagnosticoMuerte the diagnosticoMuerte to set
	 */
	public void setAcronimoDiagnosticoMuerte(String diagnosticoMuerte) {
		this.diagnosticoMuerte.setAcronimo(diagnosticoMuerte);
	}
	
	/**
	 * @return the diagnosticoMuerte
	 */
	public int getCieDiagnosticoMuerte() {
		return diagnosticoMuerte.getTipoCIE();
	}

	/**
	 * @param diagnosticoMuerte the diagnosticoMuerte to set
	 */
	public void setCieDiagnosticoMuerte(int diagnosticoMuerte) {
		this.diagnosticoMuerte.setTipoCIE(diagnosticoMuerte);
	}

	/**
	 * @return the diagnosticoRecienNacido
	 */
	public Diagnostico getDiagnosticoRecienNacido() {
		return diagnosticoRecienNacido;
	}

	/**
	 * @param diagnosticoRecienNacido the diagnosticoRecienNacido to set
	 */
	public void setDiagnosticoRecienNacido(Diagnostico diagnosticoRecienNacido) {
		this.diagnosticoRecienNacido = diagnosticoRecienNacido;
	}
	
	/**
	 * @return the diagnosticoRecienNacido
	 */
	public String getAcronimoDiagnosticoRecienNacido() {
		return diagnosticoRecienNacido.getAcronimo();
	}

	/**
	 * @param diagnosticoRecienNacido the diagnosticoRecienNacido to set
	 */
	public void setAcronimoDiagnosticoRecienNacido(String diagnosticoRecienNacido) {
		this.diagnosticoRecienNacido.setAcronimo(diagnosticoRecienNacido);
	}
	
	/**
	 * @return the diagnosticoRecienNacido
	 */
	public int getCieDiagnosticoRecienNacido() {
		return diagnosticoRecienNacido.getTipoCIE();
	}

	/**
	 * @param diagnosticoRecienNacido the diagnosticoRecienNacido to set
	 */
	public void setCieDiagnosticoRecienNacido(int diagnosticoRecienNacido) {
		this.diagnosticoRecienNacido.setTipoCIE(diagnosticoRecienNacido);
	}

	/**
	 * @return the fechaMuerte
	 */
	public String getFechaMuerte() {
		return fechaMuerte;
	}

	/**
	 * @param fechaMuerte the fechaMuerte to set
	 */
	public void setFechaMuerte(String fechaMuerte) {
		this.fechaMuerte = fechaMuerte;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the fechaProceso
	 */
	public String getFechaProceso() {
		return fechaProceso;
	}

	/**
	 * @param fechaProceso the fechaProceso to set
	 */
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	/**
	 * @return the finalizada
	 */
	public boolean isFinalizada() {
		return finalizada;
	}

	/**
	 * @param finalizada the finalizada to set
	 */
	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}

	/**
	 * @return the horaMuerte
	 */
	public String getHoraMuerte() {
		return horaMuerte;
	}

	/**
	 * @param horaMuerte the horaMuerte to set
	 */
	public void setHoraMuerte(String horaMuerte) {
		this.horaMuerte = horaMuerte;
	}

	/**
	 * @return the horaNacimiento
	 */
	public String getHoraNacimiento() {
		return horaNacimiento;
	}

	/**
	 * @param horaNacimiento the horaNacimiento to set
	 */
	public void setHoraNacimiento(String horaNacimiento) {
		this.horaNacimiento = horaNacimiento;
	}

	/**
	 * @return the horaProceso
	 */
	public String getHoraProceso() {
		return horaProceso;
	}

	/**
	 * @param horaProceso the horaProceso to set
	 */
	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}

	/**
	 * @return the pesoEgreso
	 */
	public int getPesoEgreso() {
		return pesoEgreso;
	}

	/**
	 * @param pesoEgreso the pesoEgreso to set
	 */
	public void setPesoEgreso(int pesoEgreso) {
		this.pesoEgreso = pesoEgreso;
	}

	/**
	 * @return the sexo
	 */
	public InfoDatosInt getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(InfoDatosInt sexo) {
		this.sexo = sexo;
	}
	
	/**
	 * @return the sexo
	 */
	public int getCodigoSexo() {
		return sexo.getCodigo();
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setCodigoSexo(int sexo) {
		this.sexo.setCodigo(sexo);
	}
	
	/**
	 * @return the sexo
	 */
	public String getNombreSexo() {
		return sexo.getNombre();
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setNombreSexo(String sexo) {
		this.sexo.setNombre(sexo);
	}

	/**
	 * @return the usuarioProceso
	 */
	public InfoDatos getUsuarioProceso() {
		return usuarioProceso;
	}

	/**
	 * @param usuarioProceso the usuarioProceso to set
	 */
	public void setUsuarioProceso(InfoDatos usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}
	
	/**
	 * @return the usuarioProceso
	 */
	public String getLoginUsuarioProceso() {
		return usuarioProceso.getId();
	}

	/**
	 * @param usuarioProceso the usuarioProceso to set
	 */
	public void setLoginUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso.setId(usuarioProceso);
	}
	
	/**
	 * @return the usuarioProceso
	 */
	public String getNombreUsuarioProceso() {
		return usuarioProceso.getValue();
	}

	/**
	 * @param usuarioProceso the usuarioProceso to set
	 */
	public void setNombreUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso.setValue(usuarioProceso);
	}

	/**
	 * @return the vivo
	 */
	public boolean isVivo() {
		return vivo;
	}

	/**
	 * @param vivo the vivo to set
	 */
	public void setVivo(boolean vivo) {
		this.vivo = vivo;
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
	 * @return the existeBaseDatos
	 */
	public boolean isExisteBaseDatos() {
		return existeBaseDatos;
	}

	/**
	 * @param existeBaseDatos the existeBaseDatos to set
	 */
	public void setExisteBaseDatos(boolean existeBaseDatos) {
		this.existeBaseDatos = existeBaseDatos;
	}
	
}
