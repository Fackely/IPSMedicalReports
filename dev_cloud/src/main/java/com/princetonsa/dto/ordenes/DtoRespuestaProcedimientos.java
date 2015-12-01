package com.princetonsa.dto.ordenes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;

public class DtoRespuestaProcedimientos implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * */
	private int codigoPkRespuestaProce;
	
	/**
	 * 
	 * */
	private String esMuerto;
	
	/**
	 * 
	 * */
	private String diagnosticoMuerteCadenaCompleta;
	
	/**
	 * 
	 * */
	private String nombreDiagnosticoMuerte;
	
	/**
	 * 
	 * */
	private String codigoDiagnosticoMuerte;
	
	/**
	 * 
	 * */
	private String codigoTipoCieDiagnosticoMuerte;
	
	/**
	 * 
	 * */
	private String fechaMuerte;
	
	/**
	 * 
	 * */
	private String horaMuerte;
	
	/**
	 * 
	 * */
	private String certificadoDefuncion;
	
	/**
	 * 
	 * */
	private ArrayList<InfoDatosString> otrosComentariosArray;
	
	
	/**
	 * Estructura Medicos Otros Comentarios
	 * */
	private ArrayList<HashMap<String, Object>> medicosOtrosComentarios;
	
	/**
	 * Estructura Medicos para el Caso centro de Costo Respuesta Procedimiento por Terceros
	 * */
	private ArrayList<HashMap<String, Object>> medicosCentroCosto;
	
	/**
	 * 
	 * */
	private String codigoMedicoCentroCostoTercero;
	
	/**
	 * 
	 */
	private String nombreyRMProfesional;
	
	/**
	 * 
	 * */
	private String indexOtrosComentarios;
	
			
	/**
	 *  numero de respuestas anteriores almacenado en el momento de guardar la respuesta
	 * */
	private int numeroResSolProcHistorico;
	
	/**
	 * 
	 * */
	private String mostrarSeccionMuerto; 
	
	/**
	 * 
	 * */
	private String interpretacion;
	
	/**
	 * Listado de especialidades asociadas a un Profesional
	 */
	private InfoDatosInt especialidadesProfResponde[];
	
	/**
	 * Especialidad Profesional que responde
	 */
	private String codEspecialidadProfResponde;
	
	/**
	 * Descripcion Especialidad Profesional que Responde
	 */
	private String nombreEspecialidadProfResponde;
	
	
	/**
	 * 
	 */
	private int codigoFinalidadServicio;
	
	
	/**
	 * 
	 */
	private String nombreFinalidadServicio;
	
	
	
	public DtoRespuestaProcedimientos()
	{
		this.clean();
	}
	
	public void clean()
	{
		this.codigoPkRespuestaProce = ConstantesBD.codigoNuncaValido;
		this.esMuerto = ConstantesBD.acronimoNo;
		this.fechaMuerte = "";
		this.horaMuerte = "";
		this.certificadoDefuncion = "";
		this.numeroResSolProcHistorico = 0;
		this.diagnosticoMuerteCadenaCompleta = "";
		this.otrosComentariosArray = new ArrayList<InfoDatosString>();
		this.medicosOtrosComentarios = new ArrayList<HashMap<String,Object>>();
		this.indexOtrosComentarios = "";
		this.mostrarSeccionMuerto = ConstantesBD.acronimoNo;
		this.medicosCentroCosto = new ArrayList<HashMap<String,Object>>();
		this.codigoMedicoCentroCostoTercero = "";
		this.interpretacion = "";
		this.codEspecialidadProfResponde= "";
		this.nombreEspecialidadProfResponde="";
		this.especialidadesProfResponde = new InfoDatosInt[0];
		this.nombreyRMProfesional= "";
		this.codigoFinalidadServicio=ConstantesBD.codigoNuncaValido;
		this.nombreFinalidadServicio="";
	}
		
	/**
	 * @return the codigoPkRespuestaProce
	 */
	public int getCodigoPkRespuestaProce() {
		return codigoPkRespuestaProce;
	}

	/**
	 * @param codigoPkRespuestaProce the codigoPkRespuestaProce to set
	 */
	public void setCodigoPkRespuestaProce(int codigoPkRespuestaProce) {
		this.codigoPkRespuestaProce = codigoPkRespuestaProce;
	}

	/**
	 * @return the esMuerto
	 */
	public String getEsMuerto() {
		return esMuerto;
	}

	/**
	 * @param esMuerto the esMuerto to set
	 */
	public void setEsMuerto(String esMuerto) {
		this.esMuerto = esMuerto;
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
	 * @return the certificadoDefuncion
	 */
	public String getCertificadoDefuncion() {
		return certificadoDefuncion;
	}

	/**
	 * @param certificadoDefuncion the certificadoDefuncion to set
	 */
	public void setCertificadoDefuncion(String certificadoDefuncion) {
		this.certificadoDefuncion = certificadoDefuncion;
	}

	/**
	 * @return the numeroResSolProcHistorico
	 */
	public int getNumeroResSolProcHistorico() {
		return numeroResSolProcHistorico;
	}

	/**
	 * @param numeroResSolProcHistorico the numeroResSolProcHistorico to set
	 */
	public void setNumeroResSolProcHistorico(int numeroResSolProcHistorico) {
		this.numeroResSolProcHistorico = numeroResSolProcHistorico;
	}

	/**
	 * @return the diagnosticoMuerteCadenaCompleta
	 */
	public String getDiagnosticoMuerteCadenaCompleta() {
		return diagnosticoMuerteCadenaCompleta;
	}

	/**
	 * @param diagnosticoMuerteCadenaCompleta the diagnosticoMuerteCadenaCompleta to set
	 */
	public void setDiagnosticoMuerteCadenaCompleta(
			String diagnosticoMuerteCadenaCompleta) {
		this.diagnosticoMuerteCadenaCompleta = diagnosticoMuerteCadenaCompleta;
	}

	/**
	 * @return the nombreDiagnosticoMuerte
	 */
	public String getNombreDiagnosticoMuerte() {
		
		this.nombreDiagnosticoMuerte = "";
		
		if(!this.getDiagnosticoMuerteCadenaCompleta().equals(""))
		{
			if(this.getDiagnosticoMuerteCadenaCompleta().split(ConstantesBD.separadorSplit).length > 1)
			{
				this.nombreDiagnosticoMuerte = this.getDiagnosticoMuerteCadenaCompleta().split(ConstantesBD.separadorSplit)[2];
			}
		}	
		
		return nombreDiagnosticoMuerte;
	}

	/**
	 * @param nombreDiagnosticoMuerte the nombreDiagnosticoMuerte to set
	 */
	public void setNombreDiagnosticoMuerte(String nombreDiagnosticoMuerte) {
		this.nombreDiagnosticoMuerte = nombreDiagnosticoMuerte;
	}

	/**
	 * @return the codigoDiagnosticoMuerte
	 */
	public String getCodigoDiagnosticoMuerte() 
	{
		this.codigoDiagnosticoMuerte = "";
		
		if(!this.getDiagnosticoMuerteCadenaCompleta().equals(""))
		{
			if(this.getDiagnosticoMuerteCadenaCompleta().split(ConstantesBD.separadorSplit).length > 1)
			{
				this.codigoDiagnosticoMuerte = this.getDiagnosticoMuerteCadenaCompleta().split(ConstantesBD.separadorSplit)[0];
			}
		}	
			
		return codigoDiagnosticoMuerte;
	}

	/**
	 * @param codigoDiagnosticoMuerte the codigoDiagnosticoMuerte to set
	 */
	public void setCodigoDiagnosticoMuerte(String codigoDiagnosticoMuerte) {
		this.codigoDiagnosticoMuerte = codigoDiagnosticoMuerte;
	}

	/**
	 * @return the codigoTipoCieDiagnosticoMuerte
	 */
	public String getCodigoTipoCieDiagnosticoMuerte() 
	{	
		this.codigoTipoCieDiagnosticoMuerte = "";
		
		if(!this.getDiagnosticoMuerteCadenaCompleta().equals(""))
		{
			if(this.getDiagnosticoMuerteCadenaCompleta().split(ConstantesBD.separadorSplit).length > 1)
			{
				this.codigoTipoCieDiagnosticoMuerte = this.getDiagnosticoMuerteCadenaCompleta().split(ConstantesBD.separadorSplit)[1];
			}
		}	
			
		return codigoTipoCieDiagnosticoMuerte;
	}

	/**
	 * @param codigoTipoCieDiagnosticoMuerte the codigoTipoCieDiagnosticoMuerte to set
	 */
	public void setCodigoTipoCieDiagnosticoMuerte(
			String codigoTipoCieDiagnosticoMuerte) {
		this.codigoTipoCieDiagnosticoMuerte = codigoTipoCieDiagnosticoMuerte;
	}

	/**
	 * @return the otrosComentariosArray
	 */
	public ArrayList<InfoDatosString> getOtrosComentariosArray() {
		return otrosComentariosArray;
	}

	/**
	 * @param otrosComentariosArray the otrosComentariosArray to set
	 */
	public void setOtrosComentariosArray(
			ArrayList<InfoDatosString> otrosComentariosArray) {
		this.otrosComentariosArray = otrosComentariosArray;
	}

	/**
	 * @return the medicosOtrosComentarios
	 */
	public ArrayList<HashMap<String, Object>> getMedicosOtrosComentarios() {
		return medicosOtrosComentarios;
	}

	/**
	 * @param medicosOtrosComentarios the medicosOtrosComentarios to set
	 */
	public void setMedicosOtrosComentarios(
			ArrayList<HashMap<String, Object>> medicosOtrosComentarios) {
		this.medicosOtrosComentarios = medicosOtrosComentarios;
	}

	/**
	 * @return the indexOtrosComentarios
	 */
	public String getIndexOtrosComentarios() {
		return indexOtrosComentarios;
	}

	/**
	 * @param indexOtrosComentarios the indexOtrosComentarios to set
	 */
	public void setIndexOtrosComentarios(String indexOtrosComentarios) {
		this.indexOtrosComentarios = indexOtrosComentarios;
	}

	/**
	 * @return the mostrarSeccionMuerto
	 */
	public String getMostrarSeccionMuerto() {
		return mostrarSeccionMuerto;
	}

	/**
	 * @param mostrarSeccionMuerto the mostrarSeccionMuerto to set
	 */
	public void setMostrarSeccionMuerto(String mostrarSeccionMuerto) {
		this.mostrarSeccionMuerto = mostrarSeccionMuerto;
	}

	public ArrayList<HashMap<String, Object>> getMedicosCentroCosto() {
		return medicosCentroCosto;
	}

	public void setMedicosCentroCosto(
			ArrayList<HashMap<String, Object>> medicosCentroCosto) {
		this.medicosCentroCosto = medicosCentroCosto;
	}

	public String getCodigoMedicoCentroCostoTercero() {
		return codigoMedicoCentroCostoTercero;
	}

	public void setCodigoMedicoCentroCostoTercero(
			String codigoMedicoCentroCostoTercero) {
		this.codigoMedicoCentroCostoTercero = codigoMedicoCentroCostoTercero;
	}

	/**
	 * @return the codEspecialidadProfResponde
	 */
	public String getCodEspecialidadProfResponde() {
		return codEspecialidadProfResponde;
	}

	/**
	 * @param codEspecialidadProfResponde the codEspecialidadProfResponde to set
	 */
	public void setCodEspecialidadProfResponde(String codEspecialidadProfResponde) {
		this.codEspecialidadProfResponde = codEspecialidadProfResponde;
	}

	/**
	 * @return the especialidadesProfResponde
	 */
	public InfoDatosInt[] getEspecialidadesProfResponde() {
		return especialidadesProfResponde;
	}

	/**
	 * @param especialidadesProfResponde the especialidadesProfResponde to set
	 */
	public void setEspecialidadesProfResponde(
			InfoDatosInt[] especialidadesProfResponde) {
		this.especialidadesProfResponde = especialidadesProfResponde;
	}

	/**
	 * @return the nombreyRMProfesional
	 */
	public String getNombreyRMProfesional() {
		return nombreyRMProfesional;
	}

	/**
	 * @param nombreyRMProfesional the nombreyRMProfesional to set
	 */
	public void setNombreyRMProfesional(String nombreyRMProfesional) {
		this.nombreyRMProfesional = nombreyRMProfesional;
	}

	/**
	 * @return the nombreEspecialidadProfResponde
	 */
	public String getNombreEspecialidadProfResponde() {
		return nombreEspecialidadProfResponde;
	}

	/**
	 * @param nombreEspecialidadProfResponde the nombreEspecialidadProfResponde to set
	 */
	public void setNombreEspecialidadProfResponde(
			String nombreEspecialidadProfResponde) {
		this.nombreEspecialidadProfResponde = nombreEspecialidadProfResponde;
	}

	/**
	 * @return the interpretacion
	 */
	public String getInterpretacion() {
		return interpretacion;
	}

	/**
	 * @param interpretacion the interpretacion to set
	 */
	public void setInterpretacion(String interpretacion) {
		this.interpretacion = interpretacion;
	}

	public int getCodigoFinalidadServicio() {
		return codigoFinalidadServicio;
	}

	public void setCodigoFinalidadServicio(int codigoFinalidadServicio) {
		this.codigoFinalidadServicio = codigoFinalidadServicio;
	}

	public String getNombreFinalidadServicio() {
		return nombreFinalidadServicio;
	}

	public void setNombreFinalidadServicio(String nombreFinalidadServicio) {
		this.nombreFinalidadServicio = nombreFinalidadServicio;
	}

	/**
	 * @return the especialidadesProfResponde
	 */
	
	
}