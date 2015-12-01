package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo donde se almacena el codigo pk de la cita
	 */
	private long codigoPkCita;
	
	
	/**
	 * Atributo donde se almacena la fecha de inicio de atención de la cita
	 */
	private Date fechaInicioAtencion;
	
	/**
	 * Atributo donde se almacena la hora de inicio de atención de la cita
	 */
	private String horaInicioAtencion;
	
	/**
	 * Atributo donde se almacena la fecha y hora de inicio de atención de la cita
	 */
	private String fechaHoraInicioAtencion;
	
	/**
	 * Atributo donde se almacena la fecha de la cita
	 */
	private Date fechaCita;
	
	/**
	 * Atributo que permite obtener la fecha en formato dd/mm/aaaa como string
	 */
	private String fechaCitaPlano;
	
	/**
	 * Atributo que permite obtener la fecha en formato dd/mm/aaaa como string
	 */
	private String fechaInicioAtencionPlano;
	
	/**
	 * Atributo que permite almacenar la hora de la cita
	 */
	private String horaCita;
	
	/**
	 * Atributo que almacena la fecha y hora de la cita
	 */
	private String fechaHoraCita;
	
	/**
	 * Atributo que almacena la fecha de asignación de la cita
	 */
	private Date fechaAsignacion;
	
	/**
	 * Atributo que almacena la fecha de asignación de la cita en formato dd/mm/aaaa
	 * como string
	 */
	private String fechaAsignacionPlano;
	
	/**
	 * Atributo que almacena la hora de asignación de la cita 
	 */
	private String horaAsignacion;
	
	/**
	 * Atributo que almacena la fecha y hora de asignación de la cita 
	 */
	private String fechaHoraAsignacion;
	
	/**
	 * Atributo que almacena la fecha de confirmación de la cita 
	 */
	private Date fechaConfirmacion;
	
	/**
	 * Atributo que almacena la fecha de confirmación de la cita en formato
	 * dd/mm/aaaa como string
	 */
	private String fechaConfirmacionPlano;
	
	/**
	 * Atributo que almacena la hora de confirmación de la cita 
	 */
	private String horaConfirmacion;
	
	/**
	 * Atributo que almacena la fecha y hora de confirmación de la cita 
	 */
	private String fechaHoraConfirmacion;
	
	/**
	 * Atributo que almacena el tipo de la cita 
	 */
	private String tipoCita;
	
	/**
	 * Atributo que almacena el login del usuario que asigno la cita
	 */
	private String loginUsuarioAsigno;
	
	/**
	 * Atributo que almacena la hora de inicio de la cita
	 */
	private String horaInicio;
	
	/**
	 * Atributo que almacena la razón social de la institucion o empresa-institucion
	 */
	private String descripcionEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el nombre del centro de atención
	 */
	private String descripcionCentroAtencion;
	
	/**
	 * Atributo que almacena el nombre de la unidad de agenda
	 */
	private String nombreUnidadAgenda;
	
	/**
	 * Atributo que almacena el nombre de la especialidad
	 */
	private String nombreEspecialidad;
	
	/**
	 * Atributo que almacena el codigo de la unidad de agenda
	 */
	private int codigoUnidadAgenda;
	
	/**
	 * Atributo que almacena el nombre de la ciudad
	 */
	private String descripcionCiudad;
	
	/**
	 * Atributo que almacena el nombre del país
	 */
	private String descripcionPais;
	
	/**
	 * Atributo que almacena el primer nombre del profesional
	 */
	private String primerNombreProfesional;
	
	/**
	 * Atributo que almacena el primer apellido del profesional
	 */
	private String primerApellidoProfesional;
	
	/**
	 * Atributo que almacena la diferencia en minutos desde el tiempo de
	 * asignación de la cita hasta el tiempo de confirmación de la cita
	 */
	private String tiempoAsigConf;
	
	/**
	 * Atributo que almacena la diferencia en minutos desde el tiempo de
	 * asignación de la cita hasta el tiempo de inicio de atención la cita
	 */
	private String tiempoAsigIni;
	
	/**
	 * Atributo que almacena la diferencia en minutos desde el tiempo de
	 * inicio de atención de la cita hasta el tiempo de confirmación de la cita
	 */
	private String tiempoIniConf;
	
	
	 /** Objeto jasper para el subreporte de tiempos de espera */
    private JRDataSource dsResultadoEspecialidades;
    
    /**
	  * Atributo que almacena los registros asociados por unidad de agenda
	  */
    private ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listaTiempoEsperaEspecialidad;
	
	/**
	  * Atributo que almacena la identificacion del paciente
	  */
	 private String identificacionPaciente;

	 /**
	  * Atributo que almacena el primer nombre del paciente
	  */
	 private String primerNombrePaciente;
	 
	 /**
	  * Atributo que almacena el segundo nombre del paciente
	  */
	 private String segundoNombrePaciente;
	 
	 /**
	  * Atributo que almacena el primer apellido del paciente
	  */
	 private String primerApellidoPaciente;
	 
	 /**
	  * Atributo que almacena el segundo apellido del paciente
	  */
	 private String segundoApellidoPaciente;
	 
	 /**
	  * Atributo que almacena el nombre completo del paciente
	  */
	 private String nombrePaciente;
	 
	 /**
	  * Atributo que almacena el acronimo de la identificacion del paciente
	  */
	 private String acronimoIdentificacion;
	 
	 /**
	  * Atributo que almacena el acronimo y el numero de identificacion del paciente
	  */
	 private String idPaciente;
	 
	 /**
	  * Atributo que almacena el primer nombre y primer apellido del profesional
	  */
	 private String profesional;
	 
	 /**
	  * Atributo que almacena la la descripcion del centro de 
	  * atencion, la ciudad y la region a la que pertenece
	  */
	 private String centroAtencionCompleto;
	 
	 /**
	  * Atributo que almacena la descripcion de la region
	  */
	 private String descripcionRegionCobertura;
	 
	 /**
	  * Atributo que almacena el consecutivo del centro de atencion
	  */
	 private int consecutivoCentroAtencion;
	 
	 /**
	  * Atributo que almacena el codigo de la empresa institucion
	  */
     private long codigoEmpresaInstitucion;

     /**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaCita
 	 * 
 	 * @param  valor para el atributo horaCita
 	 */
	public void setHoraCita(String horaCita) {
		this.horaCita = horaCita;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaCita
	 * 
	 * @return  Retorna la variable horaCita
	 */
	public String getHoraCita() {
		return horaCita;
	}

	 /**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo tipoCita
 	 * 
 	 * @param  valor para el atributo tipoCita
 	 */
	public void setTipoCita(String tipoCita) {
		this.tipoCita = tipoCita;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo tipoCita
	 * 
	 * @return  Retorna la variable tipoCita
	 */
	public String getTipoCita() {
		if(tipoCita!=null){
			if(tipoCita.equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)){
				this.tipoCita="VAL";
			}else
				if(tipoCita.equals(ConstantesIntegridadDominio.acronimoPrioritaria)){
					this.tipoCita="PRIOR";
				}
				else
					if(tipoCita.equals(ConstantesIntegridadDominio.acronimoControlCitaOdon)){
						this.tipoCita="CONT";
					}
					else
						if(tipoCita.equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)){
							this.tipoCita="INT";
						}
						else
							if(tipoCita.equals(ConstantesIntegridadDominio.acronimoAuditoria)){
								this.tipoCita="AUD";
							}
							else
								if(tipoCita.equals(ConstantesIntegridadDominio.acronimoRevaloracion)){
									this.tipoCita="REV";
								}
			
			
		}
		return tipoCita;
	}

	 /**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo identificacionPaciente
 	 * 
 	 * @param  valor para el atributo identificacionPaciente
 	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo identificacionPaciente
	 * 
	 * @return  Retorna la variable identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}

	 /**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo primerNombrePaciente
 	 * 
 	 * @param  valor para el atributo primerNombrePaciente
 	 */
	public void setPrimerNombrePaciente(String primerNombrePaciente) {
		this.primerNombrePaciente = primerNombrePaciente;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo primerNombrePaciente
	 * 
	 * @return  Retorna la variable primerNombrePaciente
	 */
	public String getPrimerNombrePaciente() {
		return primerNombrePaciente;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo segundoNombrePaciente
 	 * 
 	 * @param  valor para el atributo segundoNombrePaciente
 	 */
	public void setSegundoNombrePaciente(String segundoNombrePaciente) {
		this.segundoNombrePaciente = segundoNombrePaciente;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo segundoNombrePaciente
	 * 
	 * @return  Retorna la variable segundoNombrePaciente
	 */
	public String getSegundoNombrePaciente() {
		return segundoNombrePaciente;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo primerApellidoPaciente
 	 * 
 	 * @param  valor para el atributo primerApellidoPaciente
 	 */
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo primerApellidoPaciente
	 * 
	 * @return  Retorna la variable primerApellidoPaciente
	 */
	public String getPrimerApellidoPaciente() {
		return primerApellidoPaciente;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo segundoApellidoPaciente
 	 * 
 	 * @param  valor para el atributo segundoApellidoPaciente
 	 */
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo segundoApellidoPaciente
	 * 
	 * @return  Retorna la variable segundoApellidoPaciente
	 */
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo acronimoIdentificacion
 	 * 
 	 * @param  valor para el atributo acronimoIdentificacion
 	 */
	public void setAcronimoIdentificacion(String acronimoIdentificacion) {
		this.acronimoIdentificacion = acronimoIdentificacion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo acronimoIdentificacion
	 * 
	 * @return  Retorna la variable acronimoIdentificacion
	 */
	public String getAcronimoIdentificacion() {
		return acronimoIdentificacion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaAsignacion
 	 * 
 	 * @param  valor para el atributo fechaAsignacion
 	 */
	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaAsignacion
	 * 
	 * @return  Retorna la variable fechaAsignacion
	 */
	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo loginUsuarioAsigno
 	 * 
 	 * @param  valor para el atributo loginUsuarioAsigno
 	 */
	public void setLoginUsuarioAsigno(String loginUsuarioAsigno) {
		this.loginUsuarioAsigno = loginUsuarioAsigno;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo loginUsuarioAsigno
	 * 
	 * @return  Retorna la variable loginUsuarioAsigno
	 */
	public String getLoginUsuarioAsigno() {
		return loginUsuarioAsigno;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaConfirmacion
 	 * 
 	 * @param  valor para el atributo fechaConfirmacion
 	 */
	public void setFechaConfirmacion(Date fechaConfirmacion) {
		this.fechaConfirmacion = fechaConfirmacion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaConfirmacion
	 * 
	 * @return  Retorna la variable fechaConfirmacion
	 */
	public Date getFechaConfirmacion() {
		return fechaConfirmacion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaCita
 	 * 
 	 * @param  valor para el atributo fechaCita
 	 */
	public void setFechaCita(Date fechaCita) {
		this.fechaCita = fechaCita;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaCita
	 * 
	 * @return  Retorna la variable fechaCita
	 */
	public Date getFechaCita() {
		return fechaCita;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaInicio
 	 * 
 	 * @param  valor para el atributo horaInicio
 	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaInicio
	 * 
	 * @return  Retorna la variable horaInicio
	 */
	public String getHoraInicio() {
		return horaInicio;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo descripcionEmpresaInstitucion
 	 * 
 	 * @param  valor para el atributo descripcionEmpresaInstitucion
 	 */
	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo descripcionEmpresaInstitucion
	 * 
	 * @return  Retorna la variable descripcionEmpresaInstitucion
	 */
	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo descripcionCentroAtencion
 	 * 
 	 * @param  valor para el atributo descripcionCentroAtencion
 	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo descripcionCentroAtencion
	 * 
	 * @return  Retorna la variable descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo profesional
 	 * 
 	 * @param  valor para el atributo profesional
 	 */
	public void setProfesional(String profesional) {
		
		this.profesional = profesional;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo profesional
	 * 
	 * @return  Retorna la variable profesional
	 */
	public String getProfesional() {
		if(!UtilidadTexto.isEmpty(primerApellidoProfesional)&&!UtilidadTexto.isEmpty(primerNombreProfesional)){
			this.profesional=this.primerApellidoProfesional+" "+primerNombreProfesional;
		}
		else
			this.profesional="";
		
		return profesional;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaConfirmacion
 	 * 
 	 * @param  valor para el atributo horaConfirmacion
 	 */
	public void setHoraConfirmacion(String horaConfirmacion) {
		this.horaConfirmacion = horaConfirmacion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaConfirmacion
	 * 
	 * @return  Retorna la variable horaConfirmacion
	 */
	public String getHoraConfirmacion() {
		return horaConfirmacion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaHoraConfirmacion
 	 * 
 	 * @param  valor para el atributo fechaHoraConfirmacion
 	 */
	public void setFechaHoraConfirmacion(String fechaHoraConfirmacion) {
		this.fechaHoraConfirmacion = fechaHoraConfirmacion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaHoraConfirmacion
	 * 
	 * @return  Retorna la variable fechaHoraConfirmacion
	 */
	public String getFechaHoraConfirmacion() {
		
		if(fechaConfirmacion!=null&&!UtilidadTexto.isEmpty(horaConfirmacion)){
			String fechaConfirmacionCadena=UtilidadFecha.conversionFormatoFechaAAp(this.fechaConfirmacion);
			this.fechaHoraConfirmacion=fechaConfirmacionCadena+" "+horaConfirmacion;
		}
		else 
			this.fechaHoraConfirmacion="";
		return fechaHoraConfirmacion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaAsignacion
 	 * 
 	 * @param  valor para el atributo horaAsignacion
 	 */
	public void setHoraAsignacion(String horaAsignacion) {
		this.horaAsignacion = horaAsignacion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaAsignacion
	 * 
	 * @return  Retorna la variable horaAsignacion
	 */
	public String getHoraAsignacion() {
		return horaAsignacion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaHoraAsignacion
 	 * 
 	 * @param  valor para el atributo fechaHoraAsignacion
 	 */
	public void setFechaHoraAsignacion(String fechaHoraAsignacion) {
		this.fechaHoraAsignacion = fechaHoraAsignacion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaHoraAsignacion
	 * 
	 * @return  Retorna la variable fechaHoraAsignacion
	 */
	public String getFechaHoraAsignacion() {
		if(fechaAsignacion!=null&&!UtilidadTexto.isEmpty(horaAsignacion)){
			String fechaAsignacionCadena=UtilidadFecha.conversionFormatoFechaAAp(this.fechaAsignacion);
			this.fechaHoraAsignacion=fechaAsignacionCadena+" "+horaAsignacion;
		}
		else 
			this.fechaHoraAsignacion="";
		return fechaHoraAsignacion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nombrePaciente
 	 * 
 	 * @param  valor para el atributo nombrePaciente
 	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombrePaciente
	 * 
	 * @return  Retorna la variable nombrePaciente
	 */
	public String getNombrePaciente() {
		this.nombrePaciente=primerApellidoPaciente+" "+segundoApellidoPaciente+" "+primerNombrePaciente+" "+segundoNombrePaciente;
		return nombrePaciente;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo idPaciente
 	 * 
 	 * @param  valor para el atributo idPaciente
 	 */
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo idPaciente
	 * 
	 * @return  Retorna la variable idPaciente
	 */
	public String getIdPaciente() {
		this.idPaciente=acronimoIdentificacion+" "+identificacionPaciente;
		return idPaciente;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaHoraCita
 	 * 
 	 * @param  valor para el atributo fechaHoraCita
 	 */
	public void setFechaHoraCita(String fechaHoraCita) {
		this.fechaHoraCita = fechaHoraCita;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaHoraCita
	 * 
	 * @return  Retorna la variable fechaHoraCita
	 */
	public String getFechaHoraCita() {
		
		if(fechaCita!=null&&!UtilidadTexto.isEmpty(horaCita)){
			String fechaCitaCadena=UtilidadFecha.conversionFormatoFechaAAp(this.fechaCita);
			this.fechaHoraCita=fechaCitaCadena+" "+horaCita;
		}
		else 
			this.fechaHoraCita="";
	
		return fechaHoraCita;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo dsResultadoEspecialidades
 	 * 
 	 * @param  valor para el atributo dsResultadoEspecialidades
 	 */
	public void setDsResultadoEspecialidades(JRDataSource dsResultadoEspecialidades) {
		this.dsResultadoEspecialidades = dsResultadoEspecialidades;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo dsResultadoEspecialidades
	 * 
	 * @return  Retorna la variable dsResultadoEspecialidades
	 */
	public JRDataSource getDsResultadoEspecialidades() {
		return dsResultadoEspecialidades;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo listaTiempoEsperaEspecialidad
 	 * 
 	 * @param  valor para el atributo listaTiempoEsperaEspecialidad
 	 */
	public void setListaTiempoEsperaEspecialidad(
			ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listaTiempoEsperaEspecialidad) {
		this.listaTiempoEsperaEspecialidad = listaTiempoEsperaEspecialidad;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo listaTiempoEsperaEspecialidad
	 * 
	 * @return  Retorna la variable listaTiempoEsperaEspecialidad
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> getListaTiempoEsperaEspecialidad() {
		return listaTiempoEsperaEspecialidad;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo centroAtencionCompleto
 	 * 
 	 * @param  valor para el atributo centroAtencionCompleto
 	 */
	public void setCentroAtencionCompleto(String centroAtencionCompleto) {
		this.centroAtencionCompleto = centroAtencionCompleto;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo centroAtencionCompleto
	 * 
	 * @return  Retorna la variable centroAtencionCompleto
	 */
	public String getCentroAtencionCompleto() {
		centroAtencionCompleto=descripcionCentroAtencion+" - "+descripcionCiudad+"("+descripcionPais+")";
		return centroAtencionCompleto;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo descripcionRegionCobertura
 	 * 
 	 * @param  valor para el atributo descripcionRegionCobertura
 	 */
	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo descripcionRegionCobertura
	 * 
	 * @return  Retorna la variable descripcionRegionCobertura
	 */
	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo consecutivoCentroAtencion
 	 * 
 	 * @param  valor para el atributo consecutivoCentroAtencion
 	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencion
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoEmpresaInstitucion
 	 * 
 	 * @param  valor para el atributo codigoEmpresaInstitucion
 	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @return  Retorna la variable codigoEmpresaInstitucion
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nombreUnidadAgenda
 	 * 
 	 * @param  valor para el atributo nombreUnidadAgenda
 	 */
	public void setNombreUnidadAgenda(String nombreUnidadAgenda) {
		this.nombreUnidadAgenda = nombreUnidadAgenda;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombreUnidadAgenda
	 * 
	 * @return  Retorna la variable nombreUnidadAgenda
	 */
	public String getNombreUnidadAgenda() {
		return nombreUnidadAgenda;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nombreEspecialidad
 	 * 
 	 * @param  valor para el atributo nombreEspecialidad
 	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoUnidadAgenda
 	 * 
 	 * @param  valor para el atributo codigoUnidadAgenda
 	 */
	public void setCodigoUnidadAgenda(int codigoUnidadAgenda) {
		this.codigoUnidadAgenda = codigoUnidadAgenda;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoUnidadAgenda
	 * 
	 * @return  Retorna la variable codigoUnidadAgenda
	 */
	public int getCodigoUnidadAgenda() {
		return codigoUnidadAgenda;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaCitaPlano
 	 * 
 	 * @param  valor para el atributo fechaCitaPlano
 	 */
	public void setFechaCitaPlano(String fechaCitaPlano) {
		this.fechaCitaPlano = fechaCitaPlano;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaCitaPlano
	 * 
	 * @return  Retorna la variable fechaCitaPlano
	 */
	public String getFechaCitaPlano() {
		fechaCitaPlano=UtilidadFecha.conversionFormatoFechaAAp(this.fechaCita);
		return fechaCitaPlano;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaAsignacionPlano
 	 * 
 	 * @param  valor para el atributo fechaAsignacionPlano
 	 */
	public void setFechaAsignacionPlano(String fechaAsignacionPlano) {
		this.fechaAsignacionPlano = fechaAsignacionPlano;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaAsignacionPlano
	 * 
	 * @return  Retorna la variable fechaAsignacionPlano
	 */
	public String getFechaAsignacionPlano() {
		fechaAsignacionPlano=UtilidadFecha.conversionFormatoFechaAAp(this.fechaAsignacion);
		return fechaAsignacionPlano;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaConfirmacionPlano
 	 * 
 	 * @param  valor para el atributo fechaConfirmacionPlano
 	 */
	public void setFechaConfirmacionPlano(String fechaConfirmacionPlano) {
		this.fechaConfirmacionPlano = fechaConfirmacionPlano;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaConfirmacionPlano
	 * 
	 * @return  Retorna la variable fechaConfirmacionPlano
	 */
	public String getFechaConfirmacionPlano() {
		fechaConfirmacionPlano=UtilidadFecha.conversionFormatoFechaAAp(this.fechaConfirmacion);
		return fechaConfirmacionPlano;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo descripcionCiudad
 	 * 
 	 * @param  valor para el atributo descripcionCiudad
 	 */
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo descripcionCiudad
	 * 
	 * @return  Retorna la variable descripcionCiudad
	 */
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo descripcionPais
 	 * 
 	 * @param  valor para el atributo descripcionPais
 	 */
	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo descripcionPais
	 * 
	 * @return  Retorna la variable descripcionPais
	 */
	public String getDescripcionPais() {
		return descripcionPais;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo primerNombreProfesional
 	 * 
 	 * @param  valor para el atributo primerNombreProfesional
 	 */
	public void setPrimerNombreProfesional(String primerNombreProfesional) {
		this.primerNombreProfesional = primerNombreProfesional;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo primerNombreProfesional
	 * 
	 * @return  Retorna la variable primerNombreProfesional
	 */
	public String getPrimerNombreProfesional() {
		return primerNombreProfesional;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo primerApellidoProfesional
 	 * 
 	 * @param  valor para el atributo primerApellidoProfesional
 	 */
	public void setPrimerApellidoProfesional(String primerApellidoProfesional) {
		this.primerApellidoProfesional = primerApellidoProfesional;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo primerApellidoProfesional
	 * 
	 * @return  Retorna la variable primerApellidoProfesional
	 */
	public String getPrimerApellidoProfesional() {
		return primerApellidoProfesional;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo tiempoAsigConf
	 * 
	 * @return  Retorna la variable tiempoAsigConf
	 */
	public String getTiempoAsigConf() {
		
		if(fechaAsignacion!=null&&fechaConfirmacion!=null){
			String fechaA=UtilidadFecha.conversionFormatoFechaAAp(fechaAsignacion);
			String fechaC=UtilidadFecha.conversionFormatoFechaAAp(fechaConfirmacion);
		
			
			int minutos=UtilidadFecha.numeroMinutosEntreFechas(fechaA, horaAsignacion, fechaC, horaConfirmacion);
		
			this.tiempoAsigConf=Integer.toString(minutos);
		}
		else{
			this.tiempoAsigConf="";
		}
		
		
		return tiempoAsigConf;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo tiempoAsigConf
 	 * 
 	 * @param  valor para el atributo tiempoAsigConf
 	 */
	public void setTiempoAsigConf(String tiempoAsigConf) {
		this.tiempoAsigConf = tiempoAsigConf;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoPkCita
 	 * 
 	 * @param  valor para el atributo codigoPkCita
 	 */
	public void setCodigoPkCita(long codigoPkCita) {
		this.codigoPkCita = codigoPkCita;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoPkCita
	 * 
	 * @return  Retorna la variable codigoPkCita
	 */
	public long getCodigoPkCita() {
		return codigoPkCita;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo tiempoAsigIni
 	 * 
 	 * @param  valor para el atributo tiempoAsigIni
 	 */
	public void setTiempoAsigIni(String tiempoAsigIni) {
		this.tiempoAsigIni = tiempoAsigIni;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo tiempoAsigIni
	 * 
	 * @return  Retorna la variable tiempoAsigIni
	 */
	public String getTiempoAsigIni() {
		if(fechaAsignacion!=null&&fechaInicioAtencion!=null){
			String fechaA=UtilidadFecha.conversionFormatoFechaAAp(fechaAsignacion);
			String fechaI=UtilidadFecha.conversionFormatoFechaAAp(fechaInicioAtencion);
		
			
			int minutos=UtilidadFecha.numeroMinutosEntreFechas(fechaA, horaAsignacion, fechaI, horaInicioAtencion);
		
			this.tiempoAsigIni=Integer.toString(minutos);
		}
		else{
			this.tiempoAsigIni="";
		}
		return tiempoAsigIni;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo tiempoIniConf
 	 * 
 	 * @param  valor para el atributo tiempoIniConf
 	 */
	public void setTiempoIniConf(String tiempoIniConf) {
		this.tiempoIniConf = tiempoIniConf;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo tiempoIniConf
	 * 
	 * @return  Retorna la variable tiempoIniConf
	 */
	public String getTiempoIniConf() {
		if(fechaInicioAtencion!=null&&fechaConfirmacion!=null){
			String fechaI=UtilidadFecha.conversionFormatoFechaAAp(fechaInicioAtencion);
			String fechaC=UtilidadFecha.conversionFormatoFechaAAp(fechaConfirmacion);
		
			
			int minutos=UtilidadFecha.numeroMinutosEntreFechas(fechaI, horaInicioAtencion, fechaC, horaConfirmacion);
		
			this.tiempoIniConf=Integer.toString(minutos);
		}
		else{
			this.tiempoIniConf="";
		}
		return tiempoIniConf;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaInicioAtencion
 	 * 
 	 * @param  valor para el atributo fechaInicioAtencion
 	 */
	public void setFechaInicioAtencion(Date fechaInicioAtencion) {
		this.fechaInicioAtencion = fechaInicioAtencion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaInicioAtencion
	 * 
	 * @return  Retorna la variable fechaInicioAtencion
	 */
	public Date getFechaInicioAtencion() {
		return fechaInicioAtencion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaInicioAtencion
 	 * 
 	 * @param  valor para el atributo horaInicioAtencion
 	 */
	public void setHoraInicioAtencion(String horaInicioAtencion) {
		this.horaInicioAtencion = horaInicioAtencion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaInicioAtencion
	 * 
	 * @return  Retorna la variable horaInicioAtencion
	 */
	public String getHoraInicioAtencion() {
		return horaInicioAtencion;
	}

	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaHoraInicioAtencion
 	 * 
 	 * @param  valor para el atributo fechaHoraInicioAtencion
 	 */
	public void setFechaHoraInicioAtencion(String fechaHoraInicioAtencion) {
		this.fechaHoraInicioAtencion = fechaHoraInicioAtencion;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaHoraInicioAtencion
	 * 
	 * @return  Retorna la variable fechaHoraInicioAtencion
	 */
	public String getFechaHoraInicioAtencion() {
		if(fechaInicioAtencion!=null&&horaInicioAtencion!=null){
			String fechaInicioAtencionCadena=UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicioAtencion);
			this.fechaHoraInicioAtencion=fechaInicioAtencionCadena+" "+horaInicioAtencion;
		}
		else
			this.fechaHoraInicioAtencion="";
		return fechaHoraInicioAtencion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaInicioAtencionPlano
 	 * 
 	 * @param  valor para el atributo fechaInicioAtencionPlano
 	 */
	public void setFechaInicioAtencionPlano(String fechaInicioAtencionPlano) {
		this.fechaInicioAtencionPlano = fechaInicioAtencionPlano;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaInicioAtencionPlano
	 * 
	 * @return  Retorna la variable fechaInicioAtencionPlano
	 */
	public String getFechaInicioAtencionPlano() {
		fechaInicioAtencionPlano=UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicioAtencion);
		return fechaInicioAtencionPlano;
	}


	


	
}
