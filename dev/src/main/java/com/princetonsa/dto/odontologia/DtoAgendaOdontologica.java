package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;

import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

@SuppressWarnings("serial")
public class DtoAgendaOdontologica implements Serializable
{
	/**
	 * Versión serial
	 */
	private int codigoPk;
	private CentroAtencion centroAtencion;
	private int unidadAgenda;
	private String descripcionUniAgen;
	private String colorUniAgen;
	private int especialidadUniAgen;
	private String nombreEspecialidadUniAgen;
	private int consultorio;
	private String nombreConsultorio;
	private String descripcionConsultorio;
	private int dia;
	private String fecha;
	private String fechaInicio;
	private String fechaFin;
	private String horaInicio;
	private String horaFin;
	private int codigoMedico;
	private String nombreMedico;
	private int convencion;
	private InfoDatosInt especialidadMedico;
	private String usuarioModifica;
	private String fechaModifica;
	private String horaModifica;
	private int cupos;
	private int codigoHorarioAtencion;
	private String tipoCita;
	private DtoExcepcionCentroAten excepcionesCenAten;
	private ArrayList<InfoDatosString> msgErrorAgenOdon;
	private ArrayList<DtoCitaOdontologica> citasOdontologicas;
	
	private String observaciones;
	
	//Atributos adicionales de operación
	private String fechaBD;
	private String registroMedico;
	
	private HashMap<String, Integer> cuposEspacioTiempo;
	
	/**
	 * Lista de consultorios por cada agenda que no tenía seleccionado
	 * consultorio a la hora de generarla 
	 */
	private ArrayList<DtoConsultorios> listConsultoriosXGenerar;
	
	/**
	 * Atributo para llevar el objeto usuario del medico asociado a la agenda
	 */
	private UsuarioBasico usuarioMedico;
	
	public DtoAgendaOdontologica()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = null;
		this.unidadAgenda = ConstantesBD.codigoNuncaValido;
		this.colorUniAgen = "";
		this.descripcionUniAgen = "";
		this.especialidadUniAgen = ConstantesBD.codigoNuncaValido;
		this.nombreEspecialidadUniAgen = "";
		this.consultorio = ConstantesBD.codigoNuncaValido;
		this.descripcionConsultorio = "";
		this.dia = ConstantesBD.codigoNuncaValido;
		this.fecha = "" ;
		this.horaInicio = "" ;
		this.horaFin = "" ;
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.nombreMedico = "";
		this.convencion=ConstantesBD.codigoNuncaValido;
		this.especialidadMedico = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.usuarioModifica = "" ;
		this.fechaModifica = "" ;
		this.horaModifica = "" ;
		this.cupos = ConstantesBD.codigoNuncaValido;
		this.tipoCita="";
		this.codigoHorarioAtencion = ConstantesBD.codigoNuncaValido;
		this.excepcionesCenAten = new DtoExcepcionCentroAten();
		this.msgErrorAgenOdon = new ArrayList<InfoDatosString>();
		this.citasOdontologicas = new ArrayList<DtoCitaOdontologica>();
		this.fechaInicio="";
		this.fechaFin="";
		
		//Atributos adiconales de operacion
		this.fechaBD = "";
		this.registroMedico = "";
		
		this.usuarioMedico = new UsuarioBasico();
		this.observaciones="";
		this.nombreConsultorio="";
	}

	public String getTipoCita() {
		return tipoCita;
	}

	public void setTipoCita(String tipoCita) {
		this.tipoCita = tipoCita;
	}

	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		if(centroAtencion!=null)
		{
			return centroAtencion.getConsecutivo();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		if(this.centroAtencion==null)
		{
			this.centroAtencion=new CentroAtencion();
		}
		this.centroAtencion.setConsecutivo(centroAtencion);
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
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
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
	 * @return the excepcionesCenAten
	 */
	public DtoExcepcionCentroAten getExcepcionesCenAten() {
		return excepcionesCenAten;
	}

	/**
	 * @param excepcionesCenAten the excepcionesCenAten to set
	 */
	public void setExcepcionesCenAten(DtoExcepcionCentroAten excepcionesCenAten) {
		this.excepcionesCenAten = excepcionesCenAten;
	}

	/**
	 * @return the msgErrorAgenOdon
	 */
	public ArrayList<InfoDatosString> getMsgErrorAgenOdon() {
		return msgErrorAgenOdon;
	}

	/**
	 * @param msgErrorAgenOdon the msgErrorAgenOdon to set
	 */
	public void setMsgErrorAgenOdon(ArrayList<InfoDatosString> msgErrorAgenOdon) {
		this.msgErrorAgenOdon = msgErrorAgenOdon;
	}

	/**
	 * @return the descripcionCentAten
	 */
	public String getDescripcionCentAten() {
		if(this.centroAtencion!=null)
		{
			return this.centroAtencion.getDescripcion();
		}
		return "";
	}

	/**
	 * @param descripcionCentAten the descripcionCentAten to set
	 */
	public void setDescripcionCentAten(String descripcionCentAten) {
		if(this.centroAtencion==null)
		{
			this.centroAtencion=new CentroAtencion();
		}
		this.centroAtencion.setDescripcion(descripcionCentAten);
	}

	/**
	 * @return the descripcionUniAgen
	 */
	public String getDescripcionUniAgen() {
		return descripcionUniAgen;
	}

	/**
	 * @param descripcionUniAgen the descripcionUniAgen to set
	 */
	public void setDescripcionUniAgen(String descripcionUniAgen) {
		this.descripcionUniAgen = descripcionUniAgen;
	}

	/**
	 * @return the descripcionConsultorio
	 */
	public String getDescripcionConsultorio() {
		return descripcionConsultorio;
	}

	/**
	 * @param descripcionConsultorio the descripcionConsultorio to set
	 */
	public void setDescripcionConsultorio(String descripcionConsultorio) {
		this.descripcionConsultorio = descripcionConsultorio;
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
	 * @return the codigoHorarioAtencion
	 */
	public int getCodigoHorarioAtencion() {
		return codigoHorarioAtencion;
	}

	/**
	 * @param codigoHorarioAtencion the codigoHorarioAtencion to set
	 */
	public void setCodigoHorarioAtencion(int codigoHorarioAtencion) {
		this.codigoHorarioAtencion = codigoHorarioAtencion;
	}

	/**
	 * @return the colorUniAgen
	 */
	public String getColorUniAgen() {
		return colorUniAgen;
	}

	/**
	 * @param colorUniAgen the colorUniAgen to set
	 */
	public void setColorUniAgen(String colorUniAgen) {
		this.colorUniAgen = colorUniAgen;
	}

	/**
	 * @return the citasOdontologicas
	 */
	public ArrayList<DtoCitaOdontologica> getCitasOdontologicas() {
		return citasOdontologicas;
	}

	/**
	 * @param citasOdontologicas the citasOdontologicas to set
	 */
	public void setCitasOdontologicas(
			ArrayList<DtoCitaOdontologica> citasOdontologicas) {
		this.citasOdontologicas = citasOdontologicas;
	}

	/**
	 * @return the especialidadMedico
	 */
	public InfoDatosInt getEspecialidadMedico() {
		return especialidadMedico;
	}

	/**
	 * @param especialidadMedico the especialidadMedico to set
	 */
	public void setEspecialidadMedico(InfoDatosInt especialidadMedico) {
		this.especialidadMedico = especialidadMedico;
	}

	/**
	 * @return the fechaBD
	 */
	public String getFechaBD() {
		return fechaBD;
	}

	/**
	 * @param fechaBD the fechaBD to set
	 */
	public void setFechaBD(String fechaBD) {
		this.fechaBD = fechaBD;
	}

	/**
	 * @return the especialidadUniAgen
	 */
	public int getEspecialidadUniAgen() {
		return especialidadUniAgen;
	}

	/**
	 * @param especialidadUniAgen the especialidadUniAgen to set
	 */
	public void setEspecialidadUniAgen(int especialidadUniAgen) {
		this.especialidadUniAgen = especialidadUniAgen;
	}

	/**
	 * @return the nombreEspecialidadUniAgen
	 */
	public String getNombreEspecialidadUniAgen() {
		return nombreEspecialidadUniAgen;
	}

	/**
	 * @param nombreEspecialidadUniAgen the nombreEspecialidadUniAgen to set
	 */
	public void setNombreEspecialidadUniAgen(String nombreEspecialidadUniAgen) {
		this.nombreEspecialidadUniAgen = nombreEspecialidadUniAgen;
	}

	/**
	 * @return the usuarioMedico
	 */
	public UsuarioBasico getUsuarioMedico() {
		return usuarioMedico;
	}

	/**
	 * @param usuarioMedico the usuarioMedico to set
	 */
	public void setUsuarioMedico(UsuarioBasico usuarioMedico) {
		this.usuarioMedico = usuarioMedico;
	}

	/**
	 * @return the registroMedico
	 */
	public String getRegistroMedico() {
		return registroMedico;
	}

	/**
	 * @param registroMedico the registroMedico to set
	 */
	public void setRegistroMedico(String registroMedico) {
		this.registroMedico = registroMedico;
	}

	/**
	 * @return the cuposEspacioTiempo
	 */
	public HashMap<String, Integer> getCuposEspacioTiempo() {
		return cuposEspacioTiempo;
	}

	/**
	 * @param cuposEspacioTiempo the cuposEspacioTiempo to set
	 */
	public void setCuposEspacioTiempo(HashMap<String, Integer> cuposEspacioTiempo) {
		this.cuposEspacioTiempo = cuposEspacioTiempo;
	}

	public void setNombreConsultorio(String nombreConsultorio) {
		this.nombreConsultorio = nombreConsultorio;
	}

	public String getNombreConsultorio() {
		return nombreConsultorio;
	}

	/**
	 * Establece el valor del atributo centroAtencion
	 *
	 * @param valor para el atributo centroAtencion
	 */
	public void setCentroAtencion(CentroAtencion centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}
	
	/**
	 * Obtiene el centro de atención asociado a la agenda odontológica
	 * @return
	 */
	public CentroAtencion getDtoCentroAtencion()
	{
		return this.centroAtencion;
	}

	/**
	 * Obtiene el valor del atributo listConsultoriosXGenerar
	 *
	 * @return Retorna atributo listConsultoriosXGenerar
	 */
	public ArrayList<DtoConsultorios> getListConsultoriosXGenerar()
	{
		return listConsultoriosXGenerar;
	}

	/**
	 * Establece el valor del atributo listConsultoriosXGenerar
	 *
	 * @param valor para el atributo listConsultoriosXGenerar
	 */
	public void setListConsultoriosXGenerar(ArrayList<DtoConsultorios> listConsultoriosXGenerar)
	{
		this.listConsultoriosXGenerar = listConsultoriosXGenerar;
	}
	
}
