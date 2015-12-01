package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.CitaOdontologica;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

@SuppressWarnings("serial")
public class DtoCitaOdontologica implements Serializable,Cloneable
{
	private int codigoPk;
	private String estado;
	private String estadoAnterior; //usado en la atención de citas odontológicas
	private String tipo;
	private String ayudanteNombreTipoCita;
	private String nombreTipo;
	private int agenda; // atributo para el listado de citas de la agenda
	private DtoAgendaOdontologica agendaOdon; // atributo para el record de citas del paciente
	private int duracion;
	private String horaInicio;
	private String horaFinal;
	private String fechaReserva;
	private String horaReserva;
	private String usuarioReserva;
	private int motivoCancelacion;
	private String descripcionMotCancelacion;
	private String porConfirmar;
	private String usuarioModifica;
	private int codigoPaciente;
	private String nombrePaciente;
	
	private String nombreCompletoPaciente;//
	
	private String numeroIdentificacionPac;
	private String tipoIdentificacionPac; 
	private String tipoyNumIdentificacionPac;
	private String motivoNoAtencion;
	private int codigoCentroCosto;
	private int codigoCentroAtencion;
	private String ayudanteNombreCentroAtencion;
	private String fechaProgramacion;
	private ArrayList<DtoServicioCitaOdontologica> serviciosCitaOdon;

	private String codpais;
  	private String codciudad;
	private String descripcionpais;
	private String descripcionCiudad;	 
	
	// atributos de validación gráfica
	private String permisoUsuario;
	private String habilitarLink;
	
//	atributos consultar citas --> consulta externa
	private String solicitud;
	private String cuenta;
	private String ingreso;
	private String historiaClinica;
	private String telefonoPaciente;
	
	// atributos de validaciones atención cita odontológica
	boolean todasSolicitudesPendientes; //para saber si todas las solicitudes de la cita están pendientes
	
	private UsuarioBasico usuarioRegistra;
	private String fechaRegistra;
	private String horaRegistra;
	private UsuarioBasico usuarioConfirma;
	private String fechaConfirma;
	private String horaConfirma;
	
	private String confirmacion;
	private String observacionesConfirmacion;
	private InfoDatosInt motivoNoConfirmacion;
	private UsuarioBasico usuarioConfirmacion;
	private String fechaConfirmacion;
	private String horaConfirmacion;
	
	private String fechaModifica;
	private String horaModifica;
	
	/**
	 * Indica si existen servicios asignados a otras citas con &oacute;rden menor
	 */
	private boolean existenServiciosDeOrdenMenorAsignados;
	
	private boolean migrado;
	
	
	//***********Atributos usados para la validación de actividades***************************++
	private String codigoActividad;
	private ArrayList<InfoDatosString> arregloActividades;
	//*********************************************************************************************
	
	//*******Atributo para Validar que TODOS Servicios hallan sido Asignados de la Cita Programada
	private String todosServiciosAsigCitaProgramada;
	
	
	private String estadoPlanTratamiento;
	
	
	private int codigoEvolucion;
	
	/**
	 * atributo en el que se carga el codigo de la solicitud de cambio de servicio en caso de tener
	 */
	private int codigoSolicitudCambioServicio;
	
	/**
	 * atributo en el que se carga el estado de la solicitud de cambio de servicio en caso de tener
	 */
	private String estadoSolicitudCambioServicio;
	
	
	/**
	 * Nombre de la especialidad de la unidad de consulta
	 */
	private String nomEspeUconsulta;


	/**
	 * Indicativo del cambio de estado
	 * Manual: MANU --> ConstantesIntegridadDominio.acronimoManual
	 * Automático: AUTO --> ConstantesIntegridadDominio.acronimoAutomatica
	 */
	private String indicativoCambioEstado;

	
	/**
	 * Almacena el tipo de cancelación de la cita 
	 * odontológica, es decir, si fue realizada por paciente o por institucion.
	 */
	private String tipoCancelacion;
	
	/**
	 * Almacena el nombre del profesional asignado a la cita odontológica
	 */
	private String nombreProfesional;

	public DtoCitaOdontologica()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.estado = "";
		this.estadoAnterior = "";
		this.tipo = "";
		this.agenda = ConstantesBD.codigoNuncaValido;
		this.agendaOdon = new DtoAgendaOdontologica(); 
		this.duracion = ConstantesBD.codigoNuncaValido;
		this.horaInicio = "";
		this.horaFinal = "";
		this.fechaReserva = ""; 
		this.horaReserva = "";
		this.usuarioReserva = "";
		this.motivoCancelacion = ConstantesBD.codigoNuncaValido;
		this.descripcionMotCancelacion = "";
		this.porConfirmar = "";
		this.usuarioModifica = "";
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.nombrePaciente = "";
		this.numeroIdentificacionPac = "";
		this.tipoIdentificacionPac = "";
		this.motivoNoAtencion = "";
		this.codigoCentroCosto = ConstantesBD.codigoNuncaValido;
		this.fechaProgramacion = "";
		this.serviciosCitaOdon = new ArrayList<DtoServicioCitaOdontologica>();
		
		this.setCodigoCentroAtencion(ConstantesBD.codigoNuncaValido);
		
		// atributos de validacion gráfica
		this.permisoUsuario = ConstantesBD.acronimoNo;
		this.habilitarLink = ConstantesBD.acronimoNo;
		
//		atributos consultar citas --> consulta externa
		this.solicitud = "";
		this.cuenta = "";
		this.ingreso = "";
		this.historiaClinica = "";
		this.telefonoPaciente = "";
		
		
		
		//atributos de validacion atenciójn cita odontologica
		this.todasSolicitudesPendientes = false;
		
		this.usuarioRegistra = new UsuarioBasico();
		this.fechaRegistra = "";
		this.horaRegistra = "";
		this.usuarioConfirma = new UsuarioBasico();
		this.fechaConfirma = "";
		this.horaConfirma = "";
		
		this.codciudad="";
		this.codpais= "";
		this.descripcionpais="";
		this.descripcionCiudad="";
		this.tipoyNumIdentificacionPac="";
		
		//***********Atributos usados para la validación de actividades***************************++
		this.codigoActividad = "";
		this.arregloActividades = new ArrayList<InfoDatosString>();
		//*******************************************************************************************
		
		this.confirmacion = "";
		this.observacionesConfirmacion = "";
		this.motivoNoConfirmacion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.usuarioConfirmacion = new UsuarioBasico();
		this.fechaConfirmacion = "";
		this.horaConfirmacion = "";
		
		this.migrado = false;
		
		this.estadoPlanTratamiento = "";
		
		this.todosServiciosAsigCitaProgramada=ConstantesBD.acronimoNo;
		this.codigoEvolucion=ConstantesBD.codigoNuncaValido;
		
		this.codigoSolicitudCambioServicio=ConstantesBD.codigoNuncaValido;
		this.estadoSolicitudCambioServicio="";
		this.ayudanteNombreCentroAtencion="";
		
		this.nomEspeUconsulta = "";
		this.tipoCancelacion = "";
		
		this.indicativoCambioEstado=null;
		this.nombreProfesional= "";
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the agenda
	 */
	public int getAgenda() {
		return agenda;
	}

	/**
	 * @param agenda the agenda to set
	 */
	public void setAgenda(int agenda) {
		this.agenda = agenda;
	}

	/**
	 * @return the duracion
	 */
	public int getDuracion() {
		return duracion;
	}

	/**
	 * @param duracion the duracion to set
	 */
	public void setDuracion(int duracion) {
		this.duracion = duracion;
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
	 * @return the horaFinal
	 */
	public String getHoraFinal() {
		return horaFinal;
	}

	/**
	 * @param horaFinal the horaFinal to set
	 */
	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	/**
	 * @return the fechaReserva
	 */
	public String getFechaReserva() {
		return fechaReserva;
	}

	/**
	 * @param fechaReserva the fechaReserva to set
	 */
	public void setFechaReserva(String fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	/**
	 * @return the horaReserva
	 */
	public String getHoraReserva() {
		return horaReserva;
	}

	/**
	 * @param horaReserva the horaReserva to set
	 */
	public void setHoraReserva(String horaReserva) {
		this.horaReserva = horaReserva;
	}

	/**
	 * @return the usuarioReserva
	 */
	public String getUsuarioReserva() {
		return usuarioReserva;
	}

	/**
	 * @param usuarioReserva the usuarioReserva to set
	 */
	public void setUsuarioReserva(String usuarioReserva) {
		this.usuarioReserva = usuarioReserva;
	}

	/**
	 * @return the motivoCancelacion
	 */
	public int getMotivoCancelacion() {
		return motivoCancelacion;
	}

	/**
	 * @param motivoCancelacion the motivoCancelacion to set
	 */
	public void setMotivoCancelacion(int motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}

	/**
	 * @return the porConfirmar
	 */
	public String getPorConfirmar() {
		return porConfirmar;
	}

	/**
	 * @param porConfirmar the porConfirmar to set
	 */
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
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
	 * @return the motivoNoAtencion
	 */
	public String getMotivoNoAtencion() {
		return motivoNoAtencion;
	}

	/**
	 * @param motivoNoAtencion the motivoNoAtencion to set
	 */
	public void setMotivoNoAtencion(String motivoNoAtencion) {
		this.motivoNoAtencion = motivoNoAtencion;
	}

	/**
	 * @return the serviciosCitaOdon
	 */
	public ArrayList<DtoServicioCitaOdontologica> getServiciosCitaOdon() {
		return serviciosCitaOdon;
	}

	/**
	 * @param serviciosCitaOdon the serviciosCitaOdon to set
	 */
	public void setServiciosCitaOdon(
			ArrayList<DtoServicioCitaOdontologica> serviciosCitaOdon) {
		this.serviciosCitaOdon = serviciosCitaOdon;
	}

	/**
	 * @return the descripcionMotCancelacion
	 */
	public String getDescripcionMotCancelacion() {
		return descripcionMotCancelacion;
	}

	/**
	 * @param descripcionMotCancelacion the descripcionMotCancelacion to set
	 */
	public void setDescripcionMotCancelacion(String descripcionMotCancelacion) {
		this.descripcionMotCancelacion = descripcionMotCancelacion;
	}

	/**
	 * @return the numeroIdentificacionPac
	 */
	public String getNumeroIdentificacionPac() {
		return numeroIdentificacionPac;
	}

	/**
	 * @param numeroIdentificacionPac the numeroIdentificacionPac to set
	 */
	public void setNumeroIdentificacionPac(String numeroIdentificacionPac) {
		this.numeroIdentificacionPac = numeroIdentificacionPac;
	}

	/**
	 * @return the tipoIdentificacionPac
	 */
	public String getTipoIdentificacionPac() {
		return tipoIdentificacionPac;
	}

	/**
	 * @param tipoIdentificacionPac the tipoIdentificacionPac to set
	 */
	public void setTipoIdentificacionPac(String tipoIdentificacionPac) {
		this.tipoIdentificacionPac = tipoIdentificacionPac;
	}

	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * @return the agendaOdon
	 */
	public DtoAgendaOdontologica getAgendaOdon() {
		return agendaOdon;
	}

	/**
	 * @param agendaOdon the agendaOdon to set
	 */
	public void setAgendaOdon(DtoAgendaOdontologica agendaOdon) {
		this.agendaOdon = agendaOdon;
	}

	/**
	 * @return the permisoUsuario
	 */
	public String getPermisoUsuario() {
		return permisoUsuario;
	}

	/**
	 * @param permisoUsuario the permisoUsuario to set
	 */
	public void setPermisoUsuario(String permisoUsuario) {
		this.permisoUsuario = permisoUsuario;
	}

	/**
	 * @return the habilitarLink
	 */
	public String getHabilitarLink() {
		return habilitarLink;
	}

	/**
	 * @param habilitarLink the habilitarLink to set
	 */
	public void setHabilitarLink(String habilitarLink) {
		this.habilitarLink = habilitarLink;
	}

	/**
	 * @return the codigoCentroCosto
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto the codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return the fechaProgramacion
	 */
	public String getFechaProgramacion() {
		return fechaProgramacion;
	}

	/**
	 * @param fechaProgramacion the fechaProgramacion to set
	 */
	public void setFechaProgramacion(String fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}

	/**
	 * @return the solicitud
	 */
	public String getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the estadoAnterior
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	/**
	 * @param estadoAnterior the estadoAnterior to set
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	/**
	 * @return the todasSolicitudesPendientes
	 */
	public boolean isTodasSolicitudesPendientes() {
		return todasSolicitudesPendientes;
	}

	/**
	 * @param todasSolicitudesPendientes the todasSolicitudesPendientes to set
	 */
	public void setTodasSolicitudesPendientes(boolean todasSolicitudesPendientes) {
		this.todasSolicitudesPendientes = todasSolicitudesPendientes;
	}

	/**
	 * @return the usuarioRegistra
	 */
	public UsuarioBasico getUsuarioRegistra() {
		return usuarioRegistra;
	}

	/**
	 * @param usuarioRegistra the usuarioRegistra to set
	 */
	public void setUsuarioRegistra(UsuarioBasico usuarioRegistra) {
		this.usuarioRegistra = usuarioRegistra;
	}

	/**
	 * @return the fechaRegistra
	 */
	public String getFechaRegistra() {
		return fechaRegistra;
	}

	/**
	 * @param fechaRegistra the fechaRegistra to set
	 */
	public void setFechaRegistra(String fechaRegistra) {
		this.fechaRegistra = fechaRegistra;
	}

	/**
	 * @return the horaRegistra
	 */
	public String getHoraRegistra() {
		return horaRegistra;
	}

	/**
	 * @param horaRegistra the horaRegistra to set
	 */
	public void setHoraRegistra(String horaRegistra) {
		this.horaRegistra = horaRegistra;
	}

	/**
	 * @return the usuarioConfirma
	 */
	public UsuarioBasico getUsuarioConfirma() {
		return usuarioConfirma;
	}

	/**
	 * @param usuarioConfirma the usuarioConfirma to set
	 */
	public void setUsuarioConfirma(UsuarioBasico usuarioConfirma) {
		this.usuarioConfirma = usuarioConfirma;
	}

	/**
	 * @return the fechaConfirma
	 */
	public String getFechaConfirma() {
		return fechaConfirma;
	}

	/**
	 * @param fechaConfirma the fechaConfirma to set
	 */
	public void setFechaConfirma(String fechaConfirma) {
		this.fechaConfirma = fechaConfirma;
	}

	/**
	 * @return the horaConfirma
	 */
	public String getHoraConfirma() {
		return horaConfirma;
	}

	/**
	 * @param horaConfirma the horaConfirma to set
	 */
	public void setHoraConfirma(String horaConfirma) {
		this.horaConfirma = horaConfirma;
	}
	
	
	/**
	 * Método para saber si la cita tiene servicios de tipo consulta
	 * @return
	 */
	public boolean tieneServiciosConsulta()
	{
		boolean tiene = false;
		
		for(DtoServicioCitaOdontologica servicio:this.serviciosCitaOdon)
		{
			if(servicio.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
			{
				tiene = true;
			}
		}
		
		return tiene;
	}
	
	/**
	 * Método para saber si la cita tiene servicios de tipo procedimiento
	 * @return
	 */
	public boolean tieneServiciosProcedimiento()
	{
		boolean tiene = false;
		
		for(DtoServicioCitaOdontologica servicio:this.serviciosCitaOdon)
		{
			if(servicio.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
			{
				tiene = true;
			}
		}
		
		return tiene;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the historiaClinica
	 */
	public String getHistoriaClinica() {
		return historiaClinica;
	}

	/**
	 * @param historiaClinica the historiaClinica to set
	 */
	public void setHistoriaClinica(String historiaClinica) {
		this.historiaClinica = historiaClinica;
	}

	/**
	 * @return the telefonoPaciente
	 */
	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}

	/**
	 * @param telefonoPaciente the telefonoPaciente to set
	 */
	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}
	
	
	/**
	 * Método implementado para marcar como anulada una solicitud de una ctia 
	 * @param numeroSolicitud
	 */
	public void marcarAnulacionSolicitudCita(BigDecimal numeroSolicitud)
	{
		for(DtoServicioCitaOdontologica servicio:this.serviciosCitaOdon)
		{
			if(servicio.getNumeroSolicitud()==numeroSolicitud.intValue())
			{
				servicio.getEstadoHistoriaClinicaSolicitud().setCodigo(ConstantesBD.codigoEstadoHCAnulada);
			}
		}
	}
	
	/**
	 * Método implementado para marcar como evolucionado el servicio de una cita 
	 * por hacer parte del plan de tratamiento
	 * @param codigoPkProgServ
	 */
	public void marcarEvolucionadoServicioXProgramaHallazgoPieza(int codigoProgramaHallazgoPieza)
	{
		for(DtoServicioCitaOdontologica servicio:this.serviciosCitaOdon)
		{
			if(servicio.getProgramaHallazgoPieza().getCodigoPk()==codigoProgramaHallazgoPieza||servicio.getCodigoProgramaHallazgoPieza()==codigoProgramaHallazgoPieza)
			{
				servicio.getEstadoHistoriaClinicaSolicitud().setCodigo(ConstantesBD.codigoEstadoHCSolicitada);
				servicio.setEvolucionado(true);
			}
		}
	}
	
	/**
	 * Método para verificar si la cita contiene el servicio del plan de tratamiento
	 * @param codigoPkProgServ
	 * @return
	 */
	public boolean tieneCitaServicioXProgramaHallazgoPieza(int codigoProgramaHallazgoPieza)
	{
		boolean tiene = false;
		
		for(DtoServicioCitaOdontologica servicio:this.serviciosCitaOdon)
		{
			if(servicio.getProgramaHallazgoPieza().getCodigoPk()==codigoProgramaHallazgoPieza||servicio.getCodigoProgramaHallazgoPieza()==codigoProgramaHallazgoPieza)
			{
				tiene = true;
			}
		}
		
		return tiene;
	}
	
	/**
	 * Método para obtener el DtoServicioCitaOdontologica que concuerde con el codigo del detalle programas servicio plan de tratamiento
	 * 
	 * @param codigoPkProgServ
	 * @return
	 */
	public DtoServicioCitaOdontologica obtenerServicioXProgramaHallazgoPieza(int codigoProgramaHallazgoPieza)
	{
		DtoServicioCitaOdontologica servicioEncontrado = new DtoServicioCitaOdontologica();
		
		for(DtoServicioCitaOdontologica servicio:this.serviciosCitaOdon)
		{
			if(servicio.getProgramaHallazgoPieza().getCodigoPk()==codigoProgramaHallazgoPieza||servicio.getCodigoProgramaHallazgoPieza()==codigoProgramaHallazgoPieza)
			{
				return (DtoServicioCitaOdontologica)servicio.clone();
			}
		}
		return servicioEncontrado;
	}
	
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	           
	        }
	        return obj;
	    }

	/**
	 * @return the codigoActividad
	 */
	public String getCodigoActividad() {
		return codigoActividad;
	}

	/**
	 * @param codigoActividad the codigoActividad to set
	 */
	public void setCodigoActividad(String codigoActividad) {
		this.codigoActividad = codigoActividad;
	}

	/**
	 * @return the arregloActividades
	 */
	public ArrayList<InfoDatosString> getArregloActividades() {
		return arregloActividades;
	}

	/**
	 * @param arregloActividades the arregloActividades to set
	 */
	public void setArregloActividades(ArrayList<InfoDatosString> arregloActividades) {
		this.arregloActividades = arregloActividades;
	}
	
	/**
	 *Método para cargar el arreglo de actividades según el estado de la cita 
	 * @param con 
	 * @param unidadAgenda 
	 * @param loginUsuario 
	 * @param codigoCentroAtencion 
	 * @param codigoCita Código de la cita únicamente para las citas en estado "programado"
	 * @param string 
	 * @param i 
	 */
	public void cargarArregloActividades(Connection con, int unidadAgenda, String loginUsuario, int codigoCentroAtencion, int codigoCita, int i, String string)
	{
		if(this.estado.equals(ConstantesIntegridadDominio.acronimoProgramado))
		{
			InfoDatosString actividadRese = new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar)+"");
			InfoDatosString actividadAsig = new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar)+"");
			
			if(UtilidadesConsultaExterna.esActividadAurtorizadaProgramacionCitaOdo(con, codigoCita, ConstantesBD.codigoActividadAutorizadaAsignarCitas, loginUsuario, codigoCentroAtencion))
			{
				this.arregloActividades.add(actividadAsig);
			}
			if(UtilidadesConsultaExterna.esActividadAurtorizadaProgramacionCitaOdo(con, codigoCita, ConstantesBD.codigoActividadAutorizadaReservarCitas, loginUsuario, codigoCentroAtencion))
			{
				this.arregloActividades.add(actividadRese);
			}
		}
		
		else if(this.estado.equals(ConstantesIntegridadDominio.acronimoReservado))
		{
			InfoDatosString actividadAsig = new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar)+"");
			InfoDatosString actividadCan = new InfoDatosString(ConstantesIntegridadDominio.acronimoCancelar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoCancelar)+"");
			
			InfoDatosString actividadRep = new InfoDatosString(ConstantesIntegridadDominio.acronimoReprogramar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoReprogramar)+"");
			InfoDatosString actividadCam = new InfoDatosString(ConstantesIntegridadDominio.acronimoCambiarServicio,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoCambiarServicio)+"");
			
			
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaAsignarCitas, loginUsuario, codigoCentroAtencion, true))
			{
				this.arregloActividades.add(actividadAsig);
			}
			
			/*
			 * RQF-1-33: 
			 * En la actividad confirmar
			 * debe traer todos los datos de la cita y el campo observación no debe ser requerido al seleccionar en la ventana la 
			 * opcion confirmar
			 */
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaConfirmar, loginUsuario, codigoCentroAtencion, true))
			{
				boolean escitaConfirmada=CitaOdontologica.esCitaConfirmada(codigoCita) ;
				if(!escitaConfirmada)
				{
					InfoDatosString actividadCon = new InfoDatosString(ConstantesIntegridadDominio.acronimoConfirmar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoConfirmar)+"");
					this.arregloActividades.add(actividadCon);
				}	
			}
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaCancelarCitas, loginUsuario, codigoCentroAtencion, true))
			{
				this.arregloActividades.add(actividadCan);
			}
			
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaReprogramarCitas, loginUsuario, codigoCentroAtencion, true))
			{
				this.arregloActividades.add(actividadRep);
			}		
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaCambiarServicion, loginUsuario, codigoCentroAtencion, true))
			{
				this.arregloActividades.add(actividadCam);
			}
		}
		else if(this.estado.equals(ConstantesIntegridadDominio.acronimoAsignado))
		{
			
			if(this.codigoSolicitudCambioServicio<=0)
			{
				InfoDatosString actividadCan = new InfoDatosString(ConstantesIntegridadDominio.acronimoCancelar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoCancelar)+"");
				if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaCancelarCitas, loginUsuario, codigoCentroAtencion, true))
				{
					this.arregloActividades.add(actividadCan);
				}

				InfoDatosString actividadCam = new InfoDatosString(ConstantesIntegridadDominio.acronimoCambiarServicio,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoCambiarServicio)+"");
				if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaCambiarServicion, loginUsuario, codigoCentroAtencion, true))
				{
					this.arregloActividades.add(actividadCam);
				}
						
			}
			else if(this.codigoSolicitudCambioServicio>0&&this.estadoSolicitudCambioServicio.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
			{
				InfoDatosString actividadConfCamSer = new InfoDatosString(ConstantesIntegridadDominio.acronimoConfirmarSolicitudCambiarServicio,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoConfirmarSolicitudCambiarServicio)+"");
				InfoDatosString actividadAnulCamSer = new InfoDatosString(ConstantesIntegridadDominio.acronimoAnularSolicitudCambiarServicio,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAnularSolicitudCambiarServicio)+"");
				if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaConfirmarSolicitudCambiarServicio, loginUsuario, codigoCentroAtencion, true))
				{
					this.arregloActividades.add(actividadConfCamSer);
				}
				if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaAnularSolicitudCambiarServicio, loginUsuario, codigoCentroAtencion, true))
				{
					this.arregloActividades.add(actividadAnulCamSer);
				}
			}
			
			
			//this.arregloActividades.add(actividadRep);
		}
		else if(this.estado.equals(ConstantesIntegridadDominio.acronimoNoAtencion)||
				this.estado.equals(ConstantesIntegridadDominio.acronimoNoAsistio))
		{
			InfoDatosString actividadRep = new InfoDatosString(ConstantesIntegridadDominio.acronimoReprogramar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoReprogramar)+"");
			
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaReprogramarCitas, loginUsuario, codigoCentroAtencion, true))
			{
				this.arregloActividades.add(actividadRep);
			}
		}
		else if(this.estado.equals(ConstantesIntegridadDominio.acronimoAreprogramar))
		{
			
			InfoDatosString actividadCan = new InfoDatosString(ConstantesIntegridadDominio.acronimoCancelar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoCancelar)+"");
			InfoDatosString actividadRep = new InfoDatosString(ConstantesIntegridadDominio.acronimoReprogramar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoReprogramar)+"");
			
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaCancelarCitas, loginUsuario, codigoCentroAtencion, true))
			{
				this.arregloActividades.add(actividadCan);
			}
			if(UtilidadesConsultaExterna.esActividadAurtorizadaOServAddProf(con,codigoCita, unidadAgenda, ConstantesBD.codigoActividadAutorizadaReprogramarCitas, loginUsuario, codigoCentroAtencion, true))
			{
				this.arregloActividades.add(actividadRep);
			}
			
		}else if(this.estado.equals(ConstantesIntegridadDominio.acronimoCancelada)){
			
			this.arregloActividades.clear();
		}
	}
	
	/**
	 * Método para obtener el número de actividades
	 * @return
	 */
	public int getNumActividades()
	{
		return this.arregloActividades.size();
	}

	/**
	 * @return the confirmacion
	 */
	public String getConfirmacion() {
		return confirmacion;
	}

	/**
	 * @param confirmacion the confirmacion to set
	 */
	public void setConfirmacion(String confirmacion) {
		this.confirmacion = confirmacion;
	}

	/**
	 * @return the observacionesConfirmacion
	 */
	public String getObservacionesConfirmacion() {
		return observacionesConfirmacion;
	}

	/**
	 * @param observacionesConfirmacion the observacionesConfirmacion to set
	 */
	public void setObservacionesConfirmacion(String observacionesConfirmacion) {
		this.observacionesConfirmacion = observacionesConfirmacion;
	}

	/**
	 * @return the motivoNoConfirmacion
	 */
	public InfoDatosInt getMotivoNoConfirmacion() {
		return motivoNoConfirmacion;
	}

	/**
	 * @param motivoNoConfirmacion the motivoNoConfirmacion to set
	 */
	public void setMotivoNoConfirmacion(InfoDatosInt motivoNoConfirmacion) {
		this.motivoNoConfirmacion = motivoNoConfirmacion;
	}

	/**
	 * @return the usuarioConfirmacion
	 */
	public UsuarioBasico getUsuarioConfirmacion() {
		return usuarioConfirmacion;
	}

	/**
	 * @param usuarioConfirmacion the usuarioConfirmacion to set
	 */
	public void setUsuarioConfirmacion(UsuarioBasico usuarioConfirmacion) {
		this.usuarioConfirmacion = usuarioConfirmacion;
	}

	/**
	 * @return the fechaConfirmacion
	 */
	public String getFechaConfirmacion() {
		return fechaConfirmacion;
	}

	/**
	 * @param fechaConfirmacion the fechaConfirmacion to set
	 */
	public void setFechaConfirmacion(String fechaConfirmacion) {
		this.fechaConfirmacion = fechaConfirmacion;
	}

	/**
	 * @return the horaConfirmacion
	 */
	public String getHoraConfirmacion() {
		return horaConfirmacion;
	}

	/**
	 * @param horaConfirmacion the horaConfirmacion to set
	 */
	public void setHoraConfirmacion(String horaConfirmacion) {
		this.horaConfirmacion = horaConfirmacion;
	}

	/**
	 * @return the migrado
	 */
	public boolean isMigrado() {
		return migrado;
	}

	/**
	 * @param migrado the migrado to set
	 */
	public void setMigrado(boolean migrado) {
		this.migrado = migrado;
	}

	/**
	 * @return the estadoPlanTratamiento
	 */
	public String getEstadoPlanTratamiento() {
		return estadoPlanTratamiento;
	}

	/**
	 * @param estadoPlanTratamiento the estadoPlanTratamiento to set
	 */
	public void setEstadoPlanTratamiento(String estadoPlanTratamiento) {
		this.estadoPlanTratamiento = estadoPlanTratamiento;
	}

	/**
	 * @return the tipoyNumIdentificacionPac
	 */
	public String getTipoyNumIdentificacionPac() {
		return tipoyNumIdentificacionPac;
	}

	/**
	 * @param tipoyNumIdentificacionPac the tipoyNumIdentificacionPac to set
	 */
	public void setTipoyNumIdentificacionPac(String tipoyNumIdentificacionPac) {
		this.tipoyNumIdentificacionPac = tipoyNumIdentificacionPac;
	}

	/**
	 * @return the codpais
	 */
	public String getCodpais() {
		return codpais;
	}

	/**
	 * @param codpais the codpais to set
	 */
	public void setCodpais(String codpais) {
		this.codpais = codpais;
	}

	/**
	 * @return the codciudad
	 */
	public String getCodciudad() {
		return codciudad;
	}

	/**
	 * @param codciudad the codciudad to set
	 */
	public void setCodciudad(String codciudad) {
		this.codciudad = codciudad;
	}

	/**
	 * @return the descripcionpais
	 */
	public String getDescripcionpais() {
		return descripcionpais;
	}

	/**
	 * @param descripcionpais the descripcionpais to set
	 */
	public void setDescripcionpais(String descripcionpais) {
		this.descripcionpais = descripcionpais;
	}

	/**
	 * @return the descrpcionciudad
	 */
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	/**
	 * @param descrpcionciudad the descripci&oacute;n ciudad to set
	 */
	public void setDescripcionCiudad(String descrpcionciudad) {
		this.descripcionCiudad = descrpcionciudad;
	}

	/**
	 * Retorna existenServiciosDeOrdenMenorAsignados
	 * @return
	 */
	public boolean getExistenServiciosDeOrdenMenorAsignados() {
		return existenServiciosDeOrdenMenorAsignados;
	}

	/**
	 * Asigna el par&aacute;metro existenServiciosDeOrdenMenorAsignados al atributo existenServiciosDeOrdenMenorAsignados
	 * @param existenServiciosDeOrdenMenorAsignados
	 */
	public void setExistenServiciosDeOrdenMenorAsignados(
			boolean existenServiciosDeOrdenMenorAsignados) {
		this.existenServiciosDeOrdenMenorAsignados = existenServiciosDeOrdenMenorAsignados;
	}

	/**
	 * @return the serviciosAsigCitaProgramada
	 */
	public String getTodosServiciosAsigCitaProgramada() {
		return todosServiciosAsigCitaProgramada;
	}

	/**
	 * @param serviciosAsigCitaProgramada the serviciosAsigCitaProgramada to set
	 */
	public void setTodosServiciosAsigCitaProgramada(String serviciosAsigCitaProgramada) {
		this.todosServiciosAsigCitaProgramada = serviciosAsigCitaProgramada;
	}

	public void setCodigoEvolucion(int codigoEvolucion) {
		this.codigoEvolucion = codigoEvolucion;
	}

	public int getCodigoEvolucion() {
		return codigoEvolucion;
	}

	public int getCodigoSolicitudCambioServicio() {
		return codigoSolicitudCambioServicio;
	}

	public void setCodigoSolicitudCambioServicio(int codigoSolicitudCambioServicio) {
		this.codigoSolicitudCambioServicio = codigoSolicitudCambioServicio;
	}

	public String getEstadoSolicitudCambioServicio() {
		return estadoSolicitudCambioServicio;
	}

	public void setEstadoSolicitudCambioServicio(
			String estadoSolicitudCambioServicio) {
		this.estadoSolicitudCambioServicio = estadoSolicitudCambioServicio;
	}

	/**
	 * @return Retorna atributo nombreTipo
	 */
	public String getNombreTipo()
	{
		return nombreTipo;
	}

	/**
	 * @param nombreTipo Asigna atributo nombreTipo
	 */
	public void setNombreTipo(String nombreTipo)
	{
		this.nombreTipo = nombreTipo;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setAyudanteNombreTipoCita(String ayudanteNombreTipoCita) {
		this.ayudanteNombreTipoCita = ayudanteNombreTipoCita;
	}

	/**
	 * 
	 * @return
	 */
	public String getAyudanteNombreTipoCita() {
		
		try{
			
			if(!UtilidadTexto.isEmpty(this.tipo) ){
				
				ayudanteNombreTipoCita=ValoresPorDefecto.getIntegridadDominio(this.tipo).toString();
			}
			else
			{
				ayudanteNombreTipoCita="";
			}
			
		}
		catch (Exception e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return ayudanteNombreTipoCita;
	}

	public void setAyudanteNombreCentroAtencion(
			String ayudanteNombreCentroAtencion) {
		this.ayudanteNombreCentroAtencion = ayudanteNombreCentroAtencion;
	}

	public String getAyudanteNombreCentroAtencion() {
		return ayudanteNombreCentroAtencion;
	}

	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}

	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}

	public String getNomEspeUconsulta() {
		return nomEspeUconsulta;
	}

	public void setNomEspeUconsulta(String nomEspeUconsulta) {
		this.nomEspeUconsulta = nomEspeUconsulta;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  tipoCancelacion
	 *
	 * @return retorna la variable tipoCancelacion
	 */
	public String getTipoCancelacion() {
		return tipoCancelacion;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo tipoCancelacion
	 * @param tipoCancelacion es el valor para el atributo tipoCancelacion 
	 */
	public void setTipoCancelacion(String tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}

	/**
	 * Obtiene el valor del atributo indicativoCambioEstado
	 *
	 * @return Retorna atributo indicativoCambioEstado
	 */
	public String getIndicativoCambioEstado()
	{
		return indicativoCambioEstado;
	}

	/**
	 * Establece el valor del atributo indicativoCambioEstado
	 *
	 * @param valor para el atributo indicativoCambioEstado
	 */
	public void setIndicativoCambioEstado(String indicativoCambioEstado)
	{
		this.indicativoCambioEstado = indicativoCambioEstado;
	}

	/**
	 * @return the nombreProfesional
	 */
	public String getNombreProfesional() {
		return nombreProfesional;
	}

	/**
	 * @param nombreProfesional the nombreProfesional to set
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}
	
}
