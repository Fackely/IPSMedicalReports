package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;

/**
 * Esta clase se encarga de recibir los datos que devuelve la consulta de solicitud
 * de cambios de servicios de citas odontologicas
 * @author Fabian Becerra
 *
 */
public class DtoResultadoConsultaCambioServiciosOdontologicos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String constante;
	
	public String getConstante() {
		constante="-";
		return constante;
	}

	public void setConstante(String constante) {
		this.constante = constante;
	}

	/**
	 * Atributo que almacena el codigopk de la solicitud
	 */
	 private int codigoPkSolicitud;
	 
	 /**
	  * Atributo que almacena el codigopk de la cita odontologica
	  */
	 private long codigoPkCita;
	 
	 /**
	  * Atributo que almacena la descripcion de la institucion o empresa institucion
	  */
	 private String descripcionEmpresaInstitucion;
	 
	 /**
	  * Atributo que almacena la descripcion del centro de atencion
	  */
	 private String descripcionCentroAtencion;
	 
	 /**
	  * Atributo que almacena el consecutivo del centro de atencion
	  */
	 private int consecutivoCentroAtencion;
	 
	 /**
	  * Atributo que almacena una cadena con el acronimo y la identificacion del paciente
	  */
	 private String idPaciente;
	 
	 /**
	  * Atributo que almacena la identificacion del paciente
	  */
	 private String identificacionPaciente;
	 
	 /**
	  * Atributo que almacena el acronimo de la identificacion del paciente
	  */
	 private String acronimoIdentificacion;
	 
	 /**
	  * Atributo que almacena el primer nombre del profesional de la salud
	  */
	 private String primerNombre;
	 
	 /**
	  * Atributo que almacena el segundo nombre del profesional de la salud
	  */
	 private String segundoNombre;
	 
	 /**
	  * Atributo que almacena el primer apellido del profesional de la salud
	  */
	 private String primerApellido;
	 
	 /**
	  * Atributo que almacena el segundo apellido del profesional de la salud
	  */
	 private String segundoApellido;
	 
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
	  * Atributo que almacena el codigo del profesional
	  */
	 private int codigoProfesional;
	 
	 /**
	  * Atributo que almacena el login del profesional
	  */
	 private String loginProfesional;
	 
	 /**
	  * Atributo que almacena el estado de la cita
	  */
	 private String estadoCita;
	 
	 /**
	  * Atributo que almacena la descripcion de la ciudad
	  */
	 private String descripcionCiudad;
	 
	 /**
	  * Atributo que almacena la descripcion del pais
	  */
	 private String descripcionPais;
	 
	 /**
	  * Atributo que almacena la descripcion de la region
	  */
	 private String descripcionRegionCobertura;
	 
	 /**
	  * Atributo que almacena la la descripcion del centro de 
	  * atencion, la ciudad y la region a la que pertenece
	  */
	 private String centroAtencionCompleto;
	 
	 /**
	  * Atributo que almacena el codigo de la institucion
	  */
	 private Integer codigoInstitucion;
	 
	 /**
	  * Atributo que almacena si la institucion es multiempresa
	  */
	 private String esMultiempresa;
	 
	 /**
	  * Atributo que almacena el genero
	  */
	 private String sexo;
	 
	 /**
	  * Atributo que almacena el rango edad inicial
	  */
	 private String rangoEdadInicial;
	 
	 /**
	  * Atributo que almacena el rango edad final
	  */
	 private String rangoEdadFinal;
	 
	 /**
	  * Atributo que almacena el nombre completo del profesional
	  */
     private String profesional;
     
     /**
	  * Atributo que almacena el nombre de la especialidad del profesional
	  */
     private String especialidad;
     
     /**
	  * Atributo que almacena la fecha de la solicitud
	  */
     private String fechaSol;
     
     /**
	  * Atributo que almacena el estado de la solicitud
	  */
     private String estadoSol;
     
     /**
	  * Atributo que almacena el codigo de la empresa institucion
	  */
     private long codigoEmpresaInstitucion;

     /**
	  * Atributo que almacena el valor inicial de los servicios
	  */
     private BigDecimal valorInicial;
     
     /**
	  * Atributo que almacena el valor final de los servicios
	  */
     private BigDecimal valorFinal;
     
     /**
	  * Atributo que almacena el valor inicial de la cita para ser mostrado en el reporte
	  */
     private String valorInicialCita;
     
     /**
	  * Atributo que almacena el valor final de la cita para ser mostrado en el reporte
	  */
     private String valorFinalCita;
     
     /**
	  * Atributo que almacena el usuario activo en el sistema
	  */
     private String usuario;
     
     /**
	  * Atributo que almacena los servicios iniciales de la solicitud
	  */
     private String servicioInicial;
     
     /**
	  * Atributo que almacena los servicios finales de la solicitud
	  */
     private String servicioFinal;
     
     /**
	  * Atributo que almacena el nombre del paciente
	  */
     private String nombrePaciente;
     
     /**
	  * Atributo que almacena la fecha de nacimiento del paciente
	  */
     private Date fechaNacimientoPaciente;
     
     /**
	  * Atributo que almacena la edad del paciente
	  */
     private String edadPaciente;
     
     /**
	  * Atributo que almacena el codigo del sexo del paciente
	  */
     private int ayudanteCodigoSexoPaciente;
     
     /**
	  * Atributo que almacena el acronimo sexo del paciente
	  */
     private String sexoPaciente;     
     
     /**
	  * Atributo que almacena la primera especialidad del profesional
	  * utilizada en el archivo plano
	  */
     private String especialidad1;
     
     
     /**
	  * Atributo que almacena la segunda especialidad del profesional
	  * utilizada en el archivo plano
	  */
     private String especialidad2;
     
     
     /**
	  * Atributo que almacena la tercera especialidad del profesional
	  * utilizada en el archivo plano
	  */
     private String especialidad3;
     
     /**
	  * Atributo que almacena la especialidad de la solicitud
	  */
     private String nombreEspecialidad;
     
     /**
	  * Atributo que almacena el programa de la solicitud
	  */
     private String nombrePrograma;
  
     
     /** Objeto jasper para el subreporte de cambio servicio */
     private JRDataSource dsResultadoProfesional;
  
     /**
	  * Atributo que almacena los registros asociados al profesional de la salud
	  */
     private ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaCambioServicioProfesional;
     
     /**
	  * Atributo que almacena las especialidades del profesional utilizado en el reporte plano
	  */
 	 private ArrayList<String> especialidadesProfesional;
 	 
 	/**
	  * Atributo que almacena la hora de la solicitud de cambio de servicio
	  * utilizada en el ordenamiento
	  */
 	 private String horaSol;

     /**
 	 * M&eacute;todo que se encarga de obtener el valor 
 	 *  del atributo servicioInicial
 	 * 
 	 * @return  Retorna la variable servicioInicial
 	 */
	public String getServicioInicial() {
		
		return servicioInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo servicioInicial
	 * 
	 * @param  valor para el atributo servicioInicial
	 */
	public void setServicioInicial(String servicioInicial) {
		this.servicioInicial = servicioInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo servicioFinal
	 * 
	 * @return  Retorna la variable servicioFinal
	 */
	public String getServicioFinal() {
		return servicioFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo servicioFinal
	 * 
	 * @param  valor para el atributo servicioFinal
	 */
	public void setServicioFinal(String servicioFinal) {
		this.servicioFinal = servicioFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo primerNombrePaciente
	 * 
	 * @return  Retorna la variable primerNombrePaciente
	 */
	public String getPrimerNombrePaciente() {
		
		return primerNombrePaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo primerNombrePaciente
	 * 
	 * @param  valor para el atributo primerNombrePaciente
	 */
	public void setPrimerNombrePaciente(String primerNombrePaciente) {
		this.primerNombrePaciente = primerNombrePaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo segundoNombrePaciente
	 * 
	 * @return  Retorna la variable segundoNombrePaciente
	 */
	public String getSegundoNombrePaciente() {
		return segundoNombrePaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo segundoNombrePaciente
	 * 
	 * @param  valor para el atributo segundoNombrePaciente
	 */
	public void setSegundoNombrePaciente(String segundoNombrePaciente) {
		this.segundoNombrePaciente = segundoNombrePaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo primerApellidoPaciente
	 * 
	 * @return  Retorna la variable primerApellidoPaciente
	 */
	public String getPrimerApellidoPaciente() {
		return primerApellidoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo primerApellidoPaciente
	 * 
	 * @param  valor para el atributo primerApellidoPaciente
	 */
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo segundoApellidoPaciente
	 * 
	 * @return  Retorna la variable segundoApellidoPaciente
	 */
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo segundoApellidoPaciente
	 * 
	 * @param  valor para el atributo segundoApellidoPaciente
	 */
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionPais
	 * 
	 * @return  Retorna la variable descripcionPais
	 */	
	public String getDescripcionPais() {
		return descripcionPais;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo descripcionPais
	 * 
	 * @param  valor para el atributo descripcionPais
	 */
	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionRegionCobertura
	 * 
	 * @return  Retorna la variable descripcionRegionCobertura
	 */
	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo descripcionRegionCobertura
	 * 
	 * @param  valor para el atributo descripcionRegionCobertura
	 */
	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoInstitucion
	 * 
	 * @return  Retorna la variable codigoInstitucion
	 */
	public Integer getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoInstitucion
	 * 
	 * @param  valor para el atributo codigoInstitucion
	 */
	public void setCodigoInstitucion(Integer codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo esMultiempresa
	 * 
	 * @return  Retorna la variable esMultiempresa
	 */
	public String getEsMultiempresa() {
		return esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo esMultiempresa
	 * 
	 * @param  valor para el atributo esMultiempresa
	 */
	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo profesional que es concatenado mediante
	 *  los nombres y apellidos del profesional
	 * 
	 * @return  Retorna la variable profesional
	 */
	public String getProfesional() {
		profesional=primerApellido+" "+segundoApellido+" "+primerNombre+" "+segundoNombre;
		return profesional;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo profesional
	 * 
	 * @param  valor para el atributo profesional
	 */
	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo especialidad
	 * 
	 * @return  Retorna la variable especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo especialidad
	 * 
	 * @param  valor para el atributo especialidad
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaSol
	 * 
	 * @return  Retorna la variable fechaSol
	 */
	public String getFechaSol() {
		this.fechaSol=UtilidadFecha.conversionFormatoFechaAAp(fechaSol);
		return fechaSol;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo fechaSol
	 * 
	 * @param  valor para el atributo fechaSol
	 */
	public void setFechaSol(String fechaSol) {
		this.fechaSol = fechaSol;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estadoSol
	 * 
	 * @return  Retorna la variable estadoSol
	 */
	public String getEstadoSol() {
		return estadoSol;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo estadoSol
	 * 
	 * @param  valor para el atributo estadoSol
	 */
	public void setEstadoSol(String estadoSol) {
		this.estadoSol = estadoSol;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombrePaciente
	 * 
	 * @return  Retorna la variable nombrePaciente
	 */
	public String getNombrePaciente() {
		nombrePaciente=primerNombrePaciente+" "+segundoNombrePaciente+" "+primerApellidoPaciente+" "+segundoApellidoPaciente;
		return nombrePaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo nombrePaciente
	 * 
	 * @param  valor para el atributo nombrePaciente
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo usuario
	 * 
	 * @return  Retorna la variable usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo usuario
	 * 
	 * @param  valor para el atributo usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaCambioServicioProfesional
	 * 
	 * @return  Retorna la variable listaCambioServicioProfesional
	 */
    public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> getListaCambioServicioProfesional() {
		return listaCambioServicioProfesional;
	}

    /**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo listaCambioServicioProfesional
	 * 
	 * @param  valor para el atributo listaCambioServicioProfesional
	 */
	public void setListaCambioServicioProfesional(
			ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaCambioServicioProfesional) {
		this.listaCambioServicioProfesional = listaCambioServicioProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dsResultadoProfesional
	 * 
	 * @return  Retorna la variable dsResultadoProfesional
	 */
	public JRDataSource getDsResultadoProfesional() {
		return dsResultadoProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo dsResultadoProfesional
	 * 
	 * @param  valor para el atributo dsResultadoProfesional
	 */
	public void setDsResultadoProfesional(JRDataSource dsResultadoProfesional) {
		this.dsResultadoProfesional = dsResultadoProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionCentroAtencion
	 * 
	 * @return  Retorna la variable descripcionCentroAtencion
	 */
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo descripcionCentroAtencion
	 * 
	 * @param  valor para el atributo descripcionCentroAtencion
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estadoCita
	 * 
	 * @return  Retorna la variable estadoCita
	 */
	public String getEstadoCita() {
		return estadoCita;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo estadoCita
	 * 
	 * @param  valor para el atributo estadoCita
	 */
	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo primerNombre
	 * 
	 * @return  Retorna la variable primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo primerNombre
	 * 
	 * @param  valor para el atributo primerNombre
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo segundoNombre
	 * 
	 * @return  Retorna la variable segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo segundoNombre
	 * 
	 * @param  valor para el atributo segundoNombre
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo primerApellido
	 * 
	 * @return  Retorna la variable primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo primerApellido
	 * 
	 * @param  valor para el atributo primerApellido
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo segundoApellido
	 * 
	 * @return  Retorna la variable segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo segundoApellido
	 * 
	 * @param  valor para el atributo segundoApellido
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo idPaciente
	 * 
	 * @return  Retorna la variable idPaciente
	 */
	public String getIdPaciente() {
		idPaciente=acronimoIdentificacion+"-"+identificacionPaciente;
		return idPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo idPaciente
	 * 
	 * @param  valor para el atributo idPaciente
	 */
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexo
	 * 
	 * @return  Retorna la variable sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo sexo
	 * 
	 * @param  valor para el atributo sexo
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadInicial
	 * 
	 * @return  Retorna la variable rangoEdadInicial
	 */
	public String getRangoEdadInicial() {
		return rangoEdadInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo rangoEdadInicial
	 * 
	 * @param  valor para el atributo rangoEdadInicial
	 */
	public void setRangoEdadInicial(String rangoEdadInicial) {
		this.rangoEdadInicial = rangoEdadInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadFinal
	 * 
	 * @return  Retorna la variable rangoEdadFinal
	 */
	public String getRangoEdadFinal() {
		return rangoEdadFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo rangoEdadFinal
	 * 
	 * @param  valor para el atributo rangoEdadFinal
	 */
	public void setRangoEdadFinal(String rangoEdadFinal) {
		this.rangoEdadFinal = rangoEdadFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo descripcionEmpresaInstitucion
	 * 
	 * @param  valor para el atributo descripcionEmpresaInstitucion
	 */
	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionEmpresaInstitucion
	 * 
	 * @return  Retorna la variable descripcionEmpresaInstitucion
	 */
	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo descripcionCiudad
	 * 
	 * @param  valor para el atributo descripcionCiudad
	 */
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionCiudad
	 * 
	 * @return  Retorna la variable descripcionCiudad
	 */
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoProfesional
	 * 
	 * @param  valor para el atributo codigoProfesional
	 */
	public void setCodigoProfesional(int codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoProfesional
	 * 
	 * @return  Retorna la variable codigoProfesional
	 */
	public int getCodigoProfesional() {
		return codigoProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo acronimoIdentificacion
	 * 
	 * @param  valor para el atributo acronimoIdentificacion
	 */
	public void setAcronimoIdentificacion(String acronimoIdentificacion) {
		this.acronimoIdentificacion = acronimoIdentificacion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo acronimoIdentificacion
	 * 
	 * @return  Retorna la variable acronimoIdentificacion
	 */
	public String getAcronimoIdentificacion() {
		return acronimoIdentificacion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo identificacionPaciente
	 * 
	 * @param  valor para el atributo identificacionPaciente
	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo identificacionPaciente
	 * 
	 * @return  Retorna la variable identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo loginProfesional
	 * 
	 * @param  valor para el atributo loginProfesional
	 */
	public void setLoginProfesional(String loginProfesional) {
		this.loginProfesional = loginProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo loginProfesional
	 * 
	 * @return  Retorna la variable loginProfesional
	 */
	public String getLoginProfesional() {
		return loginProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencion
	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencion
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo centroAtencionCompleto
	 * 
	 * @param  valor para el atributo centroAtencionCompleto
	 */
	public void setCentroAtencionCompleto(String centroAtencionCompleto) {
		this.centroAtencionCompleto = centroAtencionCompleto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo centroAtencionCompleto con la descripcion del 
	 *  centro de atencion, la ciudad y el pais 
	 * 
	 * @return  Retorna la variable centroAtencionCompleto
	 */
	public String getCentroAtencionCompleto() {
		centroAtencionCompleto=descripcionCentroAtencion+" - "+descripcionCiudad+"("+descripcionPais+")";
		return centroAtencionCompleto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoPkSolicitud
	 * 
	 * @param  valor para el atributo codigoPkSolicitud
	 */
	public void setCodigoPkSolicitud(int codigoPkSolicitud) {
		this.codigoPkSolicitud = codigoPkSolicitud;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPkSolicitud
	 * 
	 * @return  Retorna la variable codigoPkSolicitud
	 */
	public int getCodigoPkSolicitud() {
		return codigoPkSolicitud;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoPkCita
	 * 
	 * @param  valor para el atributo codigoPkCita
	 */
	public void setCodigoPkCita(long codigoPkCita) {
		this.codigoPkCita = codigoPkCita;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPkCita
	 * 
	 * @return  Retorna la variable codigoPkCita
	 */
	public long getCodigoPkCita() {
		return codigoPkCita;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo valorFinalCita
	 * 
	 * @param  valor para el atributo valorFinalCita
	 */
	public void setValorFinalCita(String valorFinalCita) {
		this.valorFinalCita = valorFinalCita;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo valorFinalCita, si es 0.00 devuelve "-",
	 *  el valor se vuelve string y se le da un patron
	 *  para mostrarlo en el reporte
	 * 
	 * @return  Retorna la variable valorFinalCita
	 */
	public String getValorFinalCita() {
		BigDecimal m= new BigDecimal(valorFinal.doubleValue());
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			valorFinalCita="-";
		}
		else{
			valorFinalCita=number;
		}
		return valorFinalCita;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo valorInicialCita
	 * 
	 * @param  valor para el atributo valorInicialCita
	 */
	public void setValorInicialCita(String valorInicialCita) {
		this.valorInicialCita = valorInicialCita;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo valorInicialCita, si es 0.00 devuelve "-"
	 *  el valor se vuelve string y se le da un patron
	 *  para mostrarlo en el reporte
	 * @return  Retorna la variable valorInicialCita
	 */
	public String getValorInicialCita() {
		
		BigDecimal m= new BigDecimal(valorInicial.doubleValue());
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			valorInicialCita="-";
		}
		else{
			valorInicialCita=number;
		}
		return valorInicialCita;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo valorFinal
	 * 
	 * @return  Retorna la variable valorFinal
	 */
	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo valorInicial
	 * 
	 * @param  valor para el atributo valorInicial
	 */
	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo valorInicial
	 * 
	 * @return  Retorna la variable valorInicial
	 */
	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @param  valor para el atributo codigoEmpresaInstitucion
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @return  Retorna la variable codigoEmpresaInstitucion
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo fechaNacimientoPaciente
	 * 
	 * @param  valor para el atributo fechaNacimientoPaciente
	 */
	public void setFechaNacimientoPaciente(Date fechaNacimientoPaciente) {
		this.fechaNacimientoPaciente = fechaNacimientoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaNacimientoPaciente
	 * 
	 * @return  Retorna la variable fechaNacimientoPaciente
	 */	
	public Date getFechaNacimientoPaciente() {
		
		return fechaNacimientoPaciente;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @param  valor para el atributo sexoPaciente
	 */
	public void setSexoPaciente(String sexoPaciente) {
		
		this.sexoPaciente = sexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @return  Retorna la variable sexoPaciente
	 */	
	public String getSexoPaciente() {
		if(ayudanteCodigoSexoPaciente==ConstantesBD.codigoSexoMasculino){
		    sexoPaciente=ConstantesIntegridadDominio.acronimoMasculino;	
		}else
			if(ayudanteCodigoSexoPaciente==ConstantesBD.codigoSexoMasculino){
				sexoPaciente=ConstantesIntegridadDominio.acronimoFemenino;
			}else
			{
				sexoPaciente="";
			}
		return sexoPaciente;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo ayudanteCodigoSexoPaciente
	 * 
	 * @param  valor para el atributo ayudanteCodigoSexoPaciente
	 */
	public void setAyudanteCodigoSexoPaciente(int ayudanteCodigoSexoPaciente) {
		this.ayudanteCodigoSexoPaciente = ayudanteCodigoSexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @return  Retorna la variable sexoPaciente
	 */	
	public int getAyudanteCodigoSexoPaciente() {
		
		return ayudanteCodigoSexoPaciente;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo edadPaciente
	 * 
	 * @param  valor para el atributo edadPaciente
	 */
	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo edadPaciente
	 * 
	 * @return  Retorna la variable edadPaciente
	 */	
	public String getEdadPaciente() {
		
		String fecha=UtilidadFecha.conversionFormatoFechaAAp(this.fechaNacimientoPaciente);
		if(!UtilidadTexto.isEmpty(fecha))
			this.edadPaciente=Integer.toString(UtilidadFecha.calcularEdad(fecha));
		
		return edadPaciente;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo especialidadesProfesional
	 * 
	 * @param  valor para el atributo especialidadesProfesional
	 */
	public void setEspecialidadesProfesional(
			ArrayList<String> especialidadesProfesional) {
		this.especialidadesProfesional = especialidadesProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo especialidadesProfesional
	 * 
	 * @return  Retorna la variable especialidadesProfesional
	 */	
	public ArrayList<String> getEspecialidadesProfesional() {
		return especialidadesProfesional;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo especialidad1
	 * 
	 * @param  valor para el atributo especialidad1
	 */
	public void setEspecialidad1(String especialidad1) {
		this.especialidad1 = especialidad1;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo especialidad1
	 * 
	 * @return  Retorna la variable especialidad1
	 */	
	public String getEspecialidad1() {
		if(!Utilidades.isEmpty(especialidadesProfesional)){
			if(especialidadesProfesional.size()>=0){
				especialidad1=especialidadesProfesional.get(0);
			}else
				especialidad1="";
		}
		return especialidad1;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo especialidad2
	 * 
	 * @param  valor para el atributo especialidad2
	 */
	public void setEspecialidad2(String especialidad2) {
		this.especialidad2 = especialidad2;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo especialidad2
	 * 
	 * @return  Retorna la variable especialidad2
	 */	
	public String getEspecialidad2() {
		if(!Utilidades.isEmpty(especialidadesProfesional)){
			if(especialidadesProfesional.size()>=2){
				especialidad2=especialidadesProfesional.get(1);
			}else
				especialidad2="";
		}
		return especialidad2;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo especialidad3
	 * 
	 * @param  valor para el atributo especialidad3
	 */
	public void setEspecialidad3(String especialidad3) {
		this.especialidad3 = especialidad3;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo especialidad3
	 * 
	 * @return  Retorna la variable especialidad3
	 */	
	public String getEspecialidad3() {
		if(!Utilidades.isEmpty(especialidadesProfesional)){
			if(especialidadesProfesional.size()>=3){
				especialidad3=especialidadesProfesional.get(2);
			}else
				especialidad3="";
		}
		return especialidad3;
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
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */	
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo nombrePrograma
	 * 
	 * @param  valor para el atributo nombrePrograma
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombrePrograma
	 * 
	 * @return  Retorna la variable nombrePrograma
	 */	
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo horaSol
	 * 
	 * @param  valor para el atributo horaSol
	 */
	public void setHoraSol(String horaSol) {
		this.horaSol = horaSol;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo horaSol
	 * 
	 * @return  Retorna la variable horaSol
	 */	
	public String getHoraSol() {
		return horaSol;
	}

	

	
	
	
	
}

