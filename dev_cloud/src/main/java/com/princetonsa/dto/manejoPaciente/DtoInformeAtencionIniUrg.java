package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.model.parser.CompatibleOnRowPropertyState;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;

/**
 * 
 * */
public class DtoInformeAtencionIniUrg implements Serializable
{
	/**
	 * 
	 * */
	private int codigoPk;
	
	/**
	 * 
	 * */
	private String nombrePersona;
	
	/**
	 * 
	 * */
	private String idPersona;
	
	/**
	 * 
	 * */
	private String codigoPersona;
	
	/**
	 * 
	 * */
	private int idIngreso;
	
	/**
	 * 
	 * */
	private String consecutivoIngreso;
	
	
	/**
	 * 
	 * */
	private String anioConsecutivoIngreso;
	
	/**
	 * 
	 * */
	private int codigoCuenta;
	
	/**
	 * 
	 * */
	private String descripcionEstadoCuenta;
	
	/**
	 * 
	 * */
	private String origenAdmision;
	
	/**
	 * 
	 * */
	private boolean poseeReferencia;
	
	/**
	 * 
	 * */
	private String fechaValoracion;
	
	/**
	 * 
	 * */
	private String horaValoracion;
	
	/**
	 * 
	 * */
	private String fechaIngreso;
	
	/**
	 * 
	 * */
	private String horaIngreso;
	
	/**
	 * 
	 * */
	private String fechaEgreso;
	
	/**
	 * 
	 * */
	private String horaEgreso;
	
	/**
	 * 
	 * */
	private String estadoInforme;
	
	/**
	 * 
	 * */
	private ArrayList<DtoEnvioInfoAtenIniUrg> historialEnvios;
	
	/**
	 * 
	 * */
	private int codigoConvenio;
	
	/**
	 * indica si el convenio tiene activa la opcion de COnvenio Informe Inicial de Urgencias
	 * */
	private boolean indConvInfIniUrg;
	
	/**
	 * 
	 * */
	private String descripcionConvenio;
	
	/**
	 * 
	 * */
	private String nombreCentroAtencion;
	
	/**
	 * 
	 * */
	private ArrayList<InfoDatosString> conveniosPaciente;
	
	/**
	 * 
	 * */
	private String descripcionViaIngreso;
	
	
	//**************************************************************************************************
	/**
	 * 
	 * */
	private ArrayList<DtoDiagInfoIniUrg> diagInfoAtenIniUrg;
	
	/**
	 * 
	 * */
	private String fechaGeneracion;
	
	/**
	 * 
	 * */
	private String horaGeneracion;
	
	/**
	 * 
	 * */
	private InfoDatosInt coberturaSalud;
	
	/**
	 * 
	 * */
	private InfoDatosInt causaExterna;
	
	/**
	 * 
	 * */
	private InfoDatosInt colorTriage;
	
	/**
	 * 
	 * */
	private String fechaIngUrgencias;
	
	/**
	 * 
	 * */
	private String horaIngUrgencias;
	
	/**
	 * 
	 * */
	private String pacVieneRemitido;
	
	/**
	 * 
	 * */
	private String motivoConsulta;
	
	/**
	 * 
	 * */
	private InfoDatosInt destinoPaciente;
	
	/**
	 * 
	 * */
	private String nombreUsuarioGenera;
	
	/**
	 * 
	 * */
	private String telefono;
	
	/**
	 * 
	 * */
	private String telefonoCelular;
	
	/**
	 * 
	 * */
	private String cargo;
	
	/**
	 * 
	 * */
	private HashMap<String,Object> institucionRemite;
	/**
	 * 
	 */
	private String archivoInconGenerado;
	/**
	 * 
	 */
	private String pathArchivoIncoXml;
	
	//**************************************************************************************************
	
	/**
	 * 
	 * */
	public DtoInformeAtencionIniUrg()
	{
		reset();		
	}
	
	/**
	 * 
	 * */
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.nombrePersona = "";
		this.idPersona = "";
		this.codigoPersona = "";
		this.idIngreso = ConstantesBD.codigoNuncaValido;
		this.codigoCuenta = ConstantesBD.codigoNuncaValido;
		this.fechaValoracion = "";
		this.horaValoracion = "";
		this.estadoInforme = "";
		this.origenAdmision = "";
		this.historialEnvios = new ArrayList<DtoEnvioInfoAtenIniUrg>();
		this.poseeReferencia = false;
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.descripcionConvenio = "";
		this.consecutivoIngreso = "";
		this.anioConsecutivoIngreso = "";
		this.fechaIngreso = "";
		this.horaIngreso = "";
		this.fechaEgreso = "";
		this.horaEgreso = "";
		this.descripcionEstadoCuenta = "";
		this.nombreCentroAtencion = "";
		this.conveniosPaciente = new ArrayList<InfoDatosString>();
		this.descripcionViaIngreso = "";
		this.indConvInfIniUrg = false;
		//************************************
		this.diagInfoAtenIniUrg = new ArrayList<DtoDiagInfoIniUrg>();
		this.fechaGeneracion = "";
		this.horaGeneracion = "";
		this.coberturaSalud = new InfoDatosInt();
		this.causaExterna = new InfoDatosInt();
		this.colorTriage = new InfoDatosInt();
		this.fechaIngUrgencias = "";
		this.horaIngUrgencias = "";
		this.pacVieneRemitido = "";
		this.motivoConsulta = "";
		this.destinoPaciente = new InfoDatosInt();
		this.nombreUsuarioGenera = "";
		this.cargo = "";
		this.institucionRemite = new HashMap<String,Object>();
		//*******************************
		
		this.archivoInconGenerado = "";
		this.pathArchivoIncoXml = "";
		
	}

	public String getNombrePersona() {
		return nombrePersona;
	}

	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public int getIdIngreso() {
		return idIngreso;
	}

	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}

	public int getCodigoCuenta() {
		return codigoCuenta;
	}

	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}

	public String getFechaValoracion() {
		return fechaValoracion;
	}

	public void setFechaValoracion(String fechaValoracion) {
		this.fechaValoracion = fechaValoracion;
	}

	public String getHoraValoracion() {
		return horaValoracion;
	}

	public void setHoraValoracion(String horaValoracion) {
		this.horaValoracion = horaValoracion;
	}

	public String getEstadoInforme() {
		return estadoInforme;
	}

	public void setEstadoInforme(String estadoInforme) {
		this.estadoInforme = estadoInforme;
	}
	
	public String getDescripcionEstadoInforme()
	{
		if(this.estadoInforme.equals(ConstantesIntegridadDominio.acronimoEstadoEnviado))
			return "Enviado";
		else if(this.estadoInforme.equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
			return "Generado";
		else
			return "";
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getOrigenAdmision() {
		return origenAdmision;
	}

	public void setOrigenAdmision(String origenAdmision) {
		this.origenAdmision = origenAdmision;
	}

	public ArrayList<DtoEnvioInfoAtenIniUrg> getHistorialEnvios() {
		return historialEnvios;
	}

	public void setHistorialEnvios(ArrayList<DtoEnvioInfoAtenIniUrg> historialEnvios) {
		this.historialEnvios = historialEnvios;
	}

	public boolean isPoseeReferencia() {
		return poseeReferencia;
	}

	public void setPoseeReferencia(boolean poseeReferencia) {
		this.poseeReferencia = poseeReferencia;
	}

	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public String getDescripcionConvenio() {
		return descripcionConvenio;
	}

	public void setDescripcionConvenio(String descripcionConvenio) {
		this.descripcionConvenio = descripcionConvenio;
	}

	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}

	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}

	public String getAnioConsecutivoIngreso() {
		return anioConsecutivoIngreso;
	}

	public void setAnioConsecutivoIngreso(String anioConsecutivoIngreso) {
		this.anioConsecutivoIngreso = anioConsecutivoIngreso;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getHoraIngreso() {
		return horaIngreso;
	}

	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	public String getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	public String getHoraEgreso() {
		return horaEgreso;
	}

	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}

	public String getDescripcionEstadoCuenta() {
		return descripcionEstadoCuenta;
	}

	public void setDescripcionEstadoCuenta(String descripcionEstadoCuenta) {
		this.descripcionEstadoCuenta = descripcionEstadoCuenta;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public ArrayList<InfoDatosString> getConveniosPaciente() {
		return conveniosPaciente;
	}

	public void setConveniosPaciente(ArrayList<InfoDatosString> conveniosPaciente) {
		this.conveniosPaciente = conveniosPaciente;
	}

	public String getDescripcionViaIngreso() {
		return descripcionViaIngreso;
	}

	public void setDescripcionViaIngreso(String descripcionViaIngreso) {
		this.descripcionViaIngreso = descripcionViaIngreso;
	}
	
	//**************************************************************************************
	
	public ArrayList<DtoDiagInfoIniUrg> getDiagInfIniUrg() {
		return diagInfoAtenIniUrg;
	}

	public void setDiagInfIniUrg(ArrayList<DtoDiagInfoIniUrg> diagInfoAtenIniUrg) {
		this.diagInfoAtenIniUrg = diagInfoAtenIniUrg;
	}

	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getHoraGeneracion() {
		return horaGeneracion;
	}

	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	public InfoDatosInt getCoberturaSalud() {
		return coberturaSalud;
	}

	public void setCoberturaSalud(InfoDatosInt coberturaSalud) {
		this.coberturaSalud = coberturaSalud;
	}

	public InfoDatosInt getCausaExterna() {
		return causaExterna;
	}

	public void setCausaExterna(InfoDatosInt causaExterna) {
		this.causaExterna = causaExterna;
	}

	public InfoDatosInt getColorTriage() {
		return colorTriage;
	}

	public void setColorTriage(InfoDatosInt colorTriage) {
		this.colorTriage = colorTriage;
	}

	public String getFechaIngUrgencias() {
		return fechaIngUrgencias;
	}

	public void setFechaIngUrgencias(String fechaIngUrgencias) {
		this.fechaIngUrgencias = fechaIngUrgencias;
	}

	public String getHoraIngUrgencias() {
		return horaIngUrgencias;
	}

	public void setHoraIngUrgencias(String horaIngUrgencias) {
		this.horaIngUrgencias = horaIngUrgencias;
	}

	public String getPacVieneRemitido() {
		return pacVieneRemitido;
	}

	public void setPacVieneRemitido(String pacVieneRemitido) {
		this.pacVieneRemitido = pacVieneRemitido;
	}

	public String getMotivoConsulta() {
		return motivoConsulta;
	}

	public void setMotivoConsulta(String motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
	}

	public InfoDatosInt getDestinoPaciente() {
		return destinoPaciente;
	}

	public void setDestinoPaciente(InfoDatosInt destinoPaciente) {
		this.destinoPaciente = destinoPaciente;
	}

	public String getNombreUsuarioGenera() {
		return nombreUsuarioGenera;
	}

	public void setNombreUsuarioGenera(String nombreUsuarioGenera) {
		this.nombreUsuarioGenera = nombreUsuarioGenera;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefonoCelular() {
		return telefonoCelular;
	}

	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public HashMap<String, Object> getInstitucionRemite() {
		return institucionRemite;
	}

	public void setInstitucionRemite(HashMap<String, Object> institucionRemite) {
		this.institucionRemite = institucionRemite;
	}

	public String getCodigoPersona() {
		return codigoPersona;
	}

	public void setCodigoPersona(String codigoPersona) {
		this.codigoPersona = codigoPersona;
	}

	public ArrayList<DtoDiagInfoIniUrg> getDiagInfoAtenIniUrg() {
		return diagInfoAtenIniUrg;
	}

	public void setDiagInfoAtenIniUrg(
			ArrayList<DtoDiagInfoIniUrg> diagInfoAtenIniUrg) {
		this.diagInfoAtenIniUrg = diagInfoAtenIniUrg;
	}

	/**
	 * @return the archivoInconGenerado
	 */
	public String getArchivoInconGenerado() {
		return archivoInconGenerado;
	}

	/**
	 * @param archivoInconGenerado the archivoInconGenerado to set
	 */
	public void setArchivoInconGenerado(String archivoInconGenerado) {
		this.archivoInconGenerado = archivoInconGenerado;
	}

	/**
	 * @return the pathArchivoIncoXml
	 */
	public String getPathArchivoIncoXml() {
		return pathArchivoIncoXml;
	}

	/**
	 * @param pathArchivoIncoXml the pathArchivoIncoXml to set
	 */
	public void setPathArchivoIncoXml(String pathArchivoIncoXml) {
		this.pathArchivoIncoXml = pathArchivoIncoXml;
	}

	public boolean isIndConvInfIniUrg() {
		return indConvInfIniUrg;
	}

	public void setIndConvInfIniUrg(boolean indConvInfIniUrg) {
		this.indConvInfIniUrg = indConvInfIniUrg;
	}
	
	//**************************************************************************************
}