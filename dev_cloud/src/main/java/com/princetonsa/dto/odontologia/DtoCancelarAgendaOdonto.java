/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoCancelarAgendaOdonto implements Serializable{
	
	private int codigoPk;
	private String horaInicio;
	private String horaFin;
	
//	Atributos odontologia.cancelacion_agenda_odo
	private int centroAtencion;
	private int unidadAgenda;
	private int consultorio;
	private int dia;
	private String fecha;
	private int codigoMedico;
	private DtoInfoFechaUsuario fhuModifica;
	private int cupos;
	
//	Atributos odontologia.det_cancelacion_agen_odo
	private int citaOdontologica;
	private int codigoPaciente;
	private ArrayList<DtoDetCancelAgenOdo> detCancelacionAgendaOdo;
	
//	Atributos Busqueda
	private String fechaInicial;
	private String fechaFinal;
	
//	Atributos resultado Listar
	private String nombreCentroAtencion;
	private String nombreMedico;
	private String nombreUnidadAgenda;
	private String nombreConsultorio;
	
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	
	public DtoCancelarAgendaOdonto(){
		this.reset();
	}
	
	/**
	 * m�todo que reinicia los atributos del dto
	 */
	public void reset(){
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.horaInicio = "";
		this.horaFin = "";
		
//		Atributos odontologia.cancelacion_agenda_odo
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.unidadAgenda = ConstantesBD.codigoNuncaValido;
		this.consultorio = ConstantesBD.codigoNuncaValido;
		this.dia = ConstantesBD.codigoNuncaValido;
		this.fecha = "";
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.fhuModifica =  null;
		this.cupos = ConstantesBD.codigoNuncaValido;
		
//		Atributos odontologia.det_cancelacion_agen_odo
		this.citaOdontologica = ConstantesBD.codigoNuncaValido;
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		
//		Atributos Busqueda
		this.fechaInicial = "";
		this.fechaFinal = "";
		
//		Atributos resultado Listar
		this.nombreCentroAtencion = "";
		this.nombreMedico = "";
		this.nombreUnidadAgenda = "";
		this.nombreConsultorio = "";
		
		this.fechaModificacion="";
		this.horaModificacion="";
		this.usuarioModificacion="";
		
		this.detCancelacionAgendaOdo= new ArrayList<DtoDetCancelAgenOdo>();
	}

	public ArrayList<DtoDetCancelAgenOdo> getDetCancelacionAgendaOdo() {
		return detCancelacionAgendaOdo;
	}

	public void setDetCancelacionAgendaOdo(
			ArrayList<DtoDetCancelAgenOdo> detCancelacionAgendaOdo) {
		this.detCancelacionAgendaOdo = detCancelacionAgendaOdo;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getHoraModificacion() {
		return horaModificacion;
	}

	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio the horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return the horaFin
	 */
	public String getHoraFin() {
		return horaFin;
	}

	/**
	 * @param horaFin the horaFin to set
	 */
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the unidadAgenda
	 */
	public int getUnidadAgenda() {
		return unidadAgenda;
	}

	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(int unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the consultorio
	 */
	public int getConsultorio() {
		return consultorio;
	}

	/**
	 * @param consultorio the consultorio to set
	 */
	public void setConsultorio(int consultorio) {
		this.consultorio = consultorio;
	}

	/**
	 * @return the dia
	 */
	public int getDia() {
		return dia;
	}

	/**
	 * @param dia the dia to set
	 */
	public void setDia(int dia) {
		this.dia = dia;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the codigoMedico
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return the fhuModifica
	 */
	public DtoInfoFechaUsuario getFhuModifica() {
		return fhuModifica;
	}

	/**
	 * @param fhuModifica the fhuModifica to set
	 */
	public void setFhuModifica(DtoInfoFechaUsuario fhuModifica) {
		this.fhuModifica = fhuModifica;
	}

	/**
	 * @return the cupos
	 */
	public int getCupos() {
		return cupos;
	}

	/**
	 * @param cupos the cupos to set
	 */
	public void setCupos(int cupos) {
		this.cupos = cupos;
	}

	/**
	 * @return the citaOdontologica
	 */
	public int getCitaOdontologica() {
		return citaOdontologica;
	}

	/**
	 * @param citaOdontologica the citaOdontologica to set
	 */
	public void setCitaOdontologica(int citaOdontologica) {
		this.citaOdontologica = citaOdontologica;
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
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the nombreMedico
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}

	/**
	 * @param nombreMedico the nombreMedico to set
	 */
	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}

	/**
	 * @return the nombreUnidadAgenda
	 */
	public String getNombreUnidadAgenda() {
		return nombreUnidadAgenda;
	}

	/**
	 * @param nombreUnidadAgenda the nombreUnidadAgenda to set
	 */
	public void setNombreUnidadAgenda(String nombreUnidadAgenda) {
		this.nombreUnidadAgenda = nombreUnidadAgenda;
	}

	/**
	 * @return the nombreConsultorio
	 */
	public String getNombreConsultorio() {
		return nombreConsultorio;
	}

	/**
	 * @param nombreConsultorio the nombreConsultorio to set
	 */
	public void setNombreConsultorio(String nombreConsultorio) {
		this.nombreConsultorio = nombreConsultorio;
	}
}
