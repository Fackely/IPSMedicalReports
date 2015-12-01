package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class DtoReporteIngresosOdontologicos implements Serializable{

	private static final Long serialVersionUID = 1L;

	/**
	 * Atributo que almacena la fecha inicial desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes.
	 */
	private Date fechaInicial;
	
	/**
	 * Atributo que almacena la fecha final desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes
	 */
	private Date fechaFinal;
	
	/**
	 * Atributo que almacena el c&oacute;digo de un pa&iacute;s de residencia.
	 */
	private String codigoPaisResidencia;
	
	/**
	 * Atributo que almacena el c&oacute;digo que identifica a 
	 * una determinada ciudad.
	 */
	private String ciudadDeptoPais;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la regi&oacute;n de cobertura.
	 */
	private long codigoRegion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la empresa-institucion.
	 */
	private long codigoEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el primer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoCiudad;
	
	/**
	 * Atributo que almacena el segundo valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoPais;
	
	/**
	 * Atributo que almacena el tercer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoDpto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del centro de 
	 * atenci&oacute;n.
	 */
	private Integer consecutivoCentroAtencion;
	
	/**
	 * Edad inicial del paciente desde la cual se desea 
	 * realizar la b&uacute;squeda.
	 */
	private Integer edadInicial;
	
	/**
	 * Edad final del paciente desde la cual se desea 
	 * realizar la b&uacute;squeda.
	 */
	private Integer edadFinal;
	
	/**
	 * Atributo que almacena el sexo del paciente.
	 */
	private String sexoPaciente;
	
	/**
	 * Atributo que indica si la b%uacute;squeda de pacientes se har&acute;
	 * con registro de valoraci&oacute;n inicial, sin registro o ambos.
	 */
	private String conValoracion;
	
	/**
	 * Atributo que almacena la fecha de nacimiento desde la cual se
	 * desea realizar la b&uacute;squeda del ingreso de un paciente.
	 */
	private String rangoEdadInicial;
	
	/**
	 * Atributo que almacena la fecha de nacimiento hasta la cual se
	 * desea realizar la b&uacute;squeda del ingreso de un paciente.
	 */
	private String rangoEdadFinal;
	
	/**
	 * Atributo que almacena el codigo del profesional
	 * que valor&oacute;.
	 */
	private Integer codigoMedico;
	
	/**
	 * Atributo que indica si la b%uacute;squeda de pacientes se har&acute;
	 * con presupuesto, sin presupuesto o ambos.
	 */
	private String conPresupuesto;
	
	/**
	 * Atributo que almacena el login del profesional que 
	 * valor&oacute;
	 */
	private String login;
	
	/**
	 * Atributo que almacena el estado del presupuesto
	 * odontol&oacute;gico.
	 */
	private String estadoPresupuesto;
	
	/**
	 * Atributo que almacena el indicativo de contrato
	 * de un presupuesto en estado contratado.
	 */
	private String indicativoContrato;
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * una especialidad odontol&oacute;gica.
	 */
	private int codigoEspecialidad;
	
	/**
	 * Atributo que almacena el c&oacute;digo del paquete
	 * odontol&oacute;gico.
	 */
	private int codigoPaqueteOdonto;
	
	/**
	 * Atributo que indica si el resultado de la b&uacute;squeda 
	 * incluir&aacute; presupuestos con solicitudes de descuento o no.
	 */
	private String conSolicitudDcto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del programa seleccionado.
	 */
	private long codigoPrograma;
	
	
	/**
	 * Atributo que permite obtener el servicio por medio de
	 *  la b&uacute;squeda gen&eacute;rica. 
	 */
	private InfoDatosStr servicio;
	
	/**
	 * Atributo que almacena el nombre del profesional 
	 * que valor&oacute;.
	 */
	private String nombreProfesional;
	
	/**
	 * Atributo que almacena el nombre de la especialidad.
	 */
	private String nombreEspecialidad;
	
	/**
	 * Atributo que almacena el nombre del indicativo 
	 * de contrato.
	 */
	private String nombreIndicativo;
	
	/**
	 * Atributo que almacena el nombre del paquete seleccionado.
	 */
	private String nombrePaquete;
	
	/**
	 * Atributo que almacena el nombre del programa seleccionado.
	 */
	private String nombrePrograma;
	
	/**
	 * Atributo que almacena el nombre del estado del presupuesto.
	 */
	private String nombreEstadoPresupuesto;
	
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
	/**
	 * Atributo que almacena la fecha inicial de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaInicialFormateado;
	
	/**
	 * Atributo que almacena la fecha final de los criterios de
	 * b&uacute;squeda con el formato de impresi&oacute;n correcto.
	 */
	private String fechaFinalFormateado;
	
	/**
	 * Atributo que almacena la ruta del logo a mostrar en el reporte.
	 */
	private String rutaLogo;
	
	/**
	 * Atributo que indica la posici&oacute;n en donde 
	 * debe ser mostrado el logo.
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena el nombre completo del usuario activo en la sesion.
	 */
	private String nombreUsuario;
	
	/**
	 * Atributo que almacena el rango de edades por el cual se realiza la consulta.
	 */
	private String rangoEdadConsultada;
	
	/**
	 * Permite almacenar el valor para ser mostrado en pantalla cuando no se
	 * ha seleccionado ningún sexo.
	 */
	private String ayudanteSexoPaciente;
	
	
	public  DtoReporteIngresosOdontologicos(){
		
		this.setServicio(new InfoDatosStr());
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;		
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPaisResidencia
	 * 
	 * @return  Retorna la variable codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPaisResidencia
	 * 
	 * @param  valor para el atributo codigoPaisResidencia 
	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ciudadDeptoPais
	 * 
	 * @return  Retorna la variable ciudadDeptoPais
	 */
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ciudadDeptoPais
	 * 
	 * @param  valor para el atributo ciudadDeptoPais 
	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoRegion
	 * 
	 * @return  Retorna la variable codigoRegion
	 */
	public long getCodigoRegion() {
		return codigoRegion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoRegion
	 * 
	 * @param  valor para el atributo codigoRegion 
	 */
	public void setCodigoRegion(long codigoRegion) {
		this.codigoRegion = codigoRegion;
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
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoEmpresaInstitucion
	 * 
	 * @param  valor para el atributo codigoEmpresaInstitucion 
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoCiudad
	 * 
	 * @return  Retorna la variable codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPais
	 * 
	 * @return  Retorna la variable codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoDpto
	 * 
	 * @return  Retorna la variable codigoDpto
	 */
	public String getCodigoDpto() {
		return codigoDpto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoCiudad
	 * 
	 * @param  valor para el atributo codigoCiudad 
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPais
	 * 
	 * @param  valor para el atributo codigoPais 
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoDpto
	 * 
	 * @param  valor para el atributo codigoDpto 
	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo edadInicial
	 * 
	 * @return  Retorna la variable edadInicial
	 */
	public Integer getEdadInicial() {
		return edadInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo edadFinal
	 * 
	 * @return  Retorna la variable edadFinal
	 */
	public Integer getEdadFinal() {
		return edadFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo edadInicial
	 * 
	 * @param  valor para el atributo edadInicial 
	 */
	public void setEdadInicial(Integer edadInicial) {
		this.edadInicial = edadInicial;
		
		if (edadInicial!=null) {
			rangoEdadInicial = UtilidadFecha.calcularFechaNacimiento(1, edadInicial);
		}else{
			rangoEdadInicial ="";
		}
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo edadFinal
	 * 
	 * @param  valor para el atributo edadFinal 
	 */
	public void setEdadFinal(Integer edadFinal) {
		this.edadFinal = edadFinal;
		
		if (edadFinal!=null) {
			rangoEdadFinal = UtilidadFecha.calcularFechaNacimiento(1, edadFinal);
		}else{
			rangoEdadFinal = "";
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @return  Retorna la variable sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo sexoPaciente
	 * 
	 * @param  valor para el atributo sexoPaciente 
	 */
	public void setSexoPaciente(String sexoPaciente) {
		
		this.sexoPaciente = sexoPaciente;
		this.ayudanteSexoPaciente = this.sexoPaciente;
		
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
	 * del atributo rangoEdadInicial
	 * 
	 * @param  valor para el atributo rangoEdadInicial 
	 */
	public void setRangoEdadInicial(String rangoEdadInicial) {
		this.rangoEdadInicial = rangoEdadInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rangoEdadFinal
	 * 
	 * @param  valor para el atributo rangoEdadFinal 
	 */
	public void setRangoEdadFinal(String rangoEdadFinal) {
		this.rangoEdadFinal = rangoEdadFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoMedico
	 * 
	 * @return  Retorna la variable codigoMedico
	 */
	public Integer getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoMedico
	 * 
	 * @param  valor para el atributo codigoMedico 
	 */
	public void setCodigoMedico(Integer codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencion
	 */
	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoCentroAtencion
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencion 
	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo login
	 * 
	 * @return  Retorna la variable login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo login
	 * 
	 * @param  valor para el atributo login 
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo conValoracion
	 * 
	 * @return  Retorna la variable conValoracion
	 */
	public String getConValoracion() {
		return conValoracion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo conPresupuesto
	 * 
	 * @return  Retorna la variable conPresupuesto
	 */
	public String getConPresupuesto() {
		return conPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo conValoracion
	 * 
	 * @param  valor para el atributo conValoracion 
	 */
	public void setConValoracion(String conValoracion) {
		this.conValoracion = conValoracion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo conPresupuesto
	 * 
	 * @param  valor para el atributo conPresupuesto 
	 */
	public void setConPresupuesto(String conPresupuesto) {
		this.conPresupuesto = conPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estadoPresupuesto
	 * 
	 * @return  Retorna la variable estadoPresupuesto
	 */
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estadoPresupuesto
	 * 
	 * @param  valor para el atributo estadoPresupuesto 
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
		
		if (!UtilidadTexto.isEmpty(estadoPresupuesto)) {
			this.nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(estadoPresupuesto).toString();
		}else{
			this.nombreEstadoPresupuesto = "Todos";
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo indicativoContrato
	 * 
	 * @return  Retorna la variable indicativoContrato
	 */
	public String getIndicativoContrato() {
		return indicativoContrato;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo indicativoContrato
	 * 
	 * @param  valor para el atributo indicativoContrato 
	 */
	public void setIndicativoContrato(String indicativoContrato) {
		this.indicativoContrato = indicativoContrato;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoEspecialidad
	 * 
	 * @return  Retorna la variable codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPaqueteOdonto
	 * 
	 * @return  Retorna la variable codigoPaqueteOdonto
	 */
	public int getCodigoPaqueteOdonto() {
		return codigoPaqueteOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoEspecialidad
	 * 
	 * @param  valor para el atributo codigoEspecialidad 
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPaqueteOdonto
	 * 
	 * @param  valor para el atributo codigoPaqueteOdonto 
	 */
	public void setCodigoPaqueteOdonto(int codigoPaqueteOdonto) {
		this.codigoPaqueteOdonto = codigoPaqueteOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo conSolicitudDcto
	 * 
	 * @return  Retorna la variable conSolicitudDcto
	 */
	public String getConSolicitudDcto() {
		return conSolicitudDcto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo conSolicitudDcto
	 * 
	 * @param  valor para el atributo conSolicitudDcto 
	 */
	public void setConSolicitudDcto(String conSolicitudDcto) {
		this.conSolicitudDcto = conSolicitudDcto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPrograma
	 * 
	 * @return  Retorna la variable codigoPrograma
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPrograma
	 * 
	 * @param  valor para el atributo codigoPrograma 
	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	public void setServicio(InfoDatosStr servicio) {
		this.servicio = servicio;
	}

	public InfoDatosStr getServicio() {
		return servicio;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreProfesional
	 * 
	 * @return  Retorna la variable nombreProfesional
	 */
	public String getNombreProfesional() {
		
		if (UtilidadTexto.isEmpty(login) || login.trim().equals("-1")) {
			nombreProfesional = "Todos";
		}else{
			nombreProfesional = Utilidades.obtenerNombreUsuarioXLogin(login, false);
		}
		
		return nombreProfesional;
	}




	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreProfesional
	 * 
	 * @param  valor para el atributo nombreProfesional 
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		
		if (UtilidadTexto.isEmpty(nombreEspecialidad) || 
				nombreEspecialidad.trim().equals("-1") ) {
			
			nombreEspecialidad = "Todos";
		}
		
		return nombreEspecialidad;
	}




	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreEspecialidad
	 * 
	 * @param  valor para el atributo nombreEspecialidad 
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreIndicativo
	 * 
	 * @return  Retorna la variable nombreIndicativo
	 */
	public String getNombreIndicativo() {
		
		if (UtilidadTexto.isEmpty(nombreIndicativo) || 
				nombreIndicativo.trim().equals("-1") ) {
			
			nombreIndicativo = "Todos";
		}
		
		return nombreIndicativo;
	}




	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreIndicativo
	 * 
	 * @param  valor para el atributo nombreIndicativo 
	 */
	public void setNombreIndicativo(String nombreIndicativo) {
		this.nombreIndicativo = nombreIndicativo;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombrePaquete
	 * 
	 * @return  Retorna la variable nombrePaquete
	 */
	public String getNombrePaquete() {
		
		if (UtilidadTexto.isEmpty(nombrePaquete) || 
				nombrePaquete.trim().equals("-1") ) {
			
			nombrePaquete = "Todos";
		}
		
		return nombrePaquete;
	}




	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombrePaquete
	 * 
	 * @param  valor para el atributo nombrePaquete 
	 */
	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombrePrograma
	 * 
	 * @return  Retorna la variable nombrePrograma
	 */
	public String getNombrePrograma() {
		
		if (UtilidadTexto.isEmpty(nombrePrograma) || 
				nombrePrograma.trim().equals("-1") ) {
			
			nombrePrograma = "Todos";
		}
		
		return nombrePrograma;
	}



	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombrePrograma
	 * 
	 * @param  valor para el atributo nombrePrograma 
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEstadoPresupuesto
	 * 
	 * @return  Retorna la variable nombreEstadoPresupuesto
	 */
	public String getNombreEstadoPresupuesto() {
		
		this.nombreEstadoPresupuesto = this.estadoPresupuesto;
		
		if (UtilidadTexto.isEmpty(estadoPresupuesto) || 
				estadoPresupuesto.trim().equals("-1") ) {
			
			nombreEstadoPresupuesto = "Todos";
		}
		
		
		if (nombreEstadoPresupuesto != null && 
				!nombreEstadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoContratado) &&
				!nombreEstadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente) &&
				!nombreEstadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoTerminado) &&
				!nombreEstadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoCancelado) &&
				!nombreEstadoPresupuesto.trim().equals("Todos")) {
			
			nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(nombreEstadoPresupuesto).toString();
		}
			
		if (nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoContratado) ||
				nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratado)) {
			this.nombreEstadoPresupuesto = "Contratado";
		}
		
		if (nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente)) {
			this.nombreEstadoPresupuesto = "Suspendido Temporalmente";
		}
		
		if (nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoTerminado)) {
			this.nombreEstadoPresupuesto = "Terminado";
		}
		
		if (nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoCancelado)) {
			this.nombreEstadoPresupuesto = "Cancelado";
		}
		
		return nombreEstadoPresupuesto;
	}




	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreEstadoPresupuesto
	 * 
	 * @param  valor para el atributo nombreEstadoPresupuesto 
	 */
	public void setNombreEstadoPresupuesto(String nombreEstadoPresupuesto) {
		this.nombreEstadoPresupuesto = nombreEstadoPresupuesto;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicialFormateado
	 * 
	 * @return  Retorna la variable fechaInicialFormateado
	 */
	public String getFechaInicialFormateado() {
		fechaInicialFormateado = UtilidadFecha.conversionFormatoFechaAAp(fechaInicial);
		
		return fechaInicialFormateado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaInicialFormateado
	 * 
	 * @param  valor para el atributo fechaInicialFormateado 
	 */
	public void setFechaInicialFormateado(String fechaInicialFormateado) {
		this.fechaInicialFormateado = fechaInicialFormateado;
	}




	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinalFormateado
	 * 
	 * @return  Retorna la variable fechaFinalFormateado
	 */
	public String getFechaFinalFormateado() {
		fechaFinalFormateado = UtilidadFecha.conversionFormatoFechaAAp(fechaFinal);
		
		return fechaFinalFormateado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaFinalFormateado
	 * 
	 * @param  valor para el atributo fechaFinalFormateado 
	 */
	public void setFechaFinalFormateado(String fechaFinalFormateado) {
		this.fechaFinalFormateado = fechaFinalFormateado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo logo
	 * 
	 * @return  Retorna la variable logo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo logo
	 * 
	 * @param  valor para el atributo logo 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	 * 
	 * @param  valor para el atributo ubicacionLogo 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreUsuario
	 * 
	 * @return  Retorna la variable nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreUsuario
	 * 
	 * @param  valor para el atributo nombreUsuario 
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadConsultada
	 * 
	 * @return  Retorna la variable rangoEdadConsultada
	 */
	public String getRangoEdadConsultada() {
		
		if (!UtilidadTexto.isEmpty(rangoEdadInicial)) {
			this.rangoEdadConsultada = edadInicial + " - "+ edadFinal;
		}else{
			this.rangoEdadConsultada= "Todas";
		}
		
		return rangoEdadConsultada;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rangoEdadConsultada
	 * 
	 * @param  valor para el atributo rangoEdadConsultada 
	 */
	public void setRangoEdadConsultada(String rangoEdadConsultada) {
		this.rangoEdadConsultada = rangoEdadConsultada;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ayudanteSexoPaciente
	 * 
	 * @return  Retorna la variable ayudanteSexoPaciente
	 */
	public String getAyudanteSexoPaciente() {
		
		if (sexoPaciente.trim().equals("-1")) {
			
			this.ayudanteSexoPaciente = "Femenimo-Masculino";
			
		}else{
			
			try {
				
				if (this.ayudanteSexoPaciente.trim().equals(ConstantesBD.codigoSexoMasculino +"")) {
					this.ayudanteSexoPaciente= ConstantesIntegridadDominio.acronimoMasculino;
				}
				if (this.ayudanteSexoPaciente.trim().equals(ConstantesBD.codigoSexoFemenino+"")) {
					this.ayudanteSexoPaciente = ConstantesIntegridadDominio.acronimoFemenino;
				}
				
				if (this.ayudanteSexoPaciente.trim().equals(ConstantesBD.codigoSexoAmbos+"")) {
					this.ayudanteSexoPaciente = ConstantesIntegridadDominio.acronimoAmbos;
				}
				
				if(ValoresPorDefecto.getIntegridadDominio(sexoPaciente)!=null)
				{
					this.ayudanteSexoPaciente = ValoresPorDefecto.getIntegridadDominio(ayudanteSexoPaciente).toString();
				}
			} catch (Exception e) {
				
					Log4JManager.info(e);
			}
		}
		
		return ayudanteSexoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ayudanteSexoPaciente
	 * 
	 * @param  valor para el atributo ayudanteSexoPaciente 
	 */
	public void setAyudanteSexoPaciente(String ayudanteSexoPaciente) {
		this.ayudanteSexoPaciente = ayudanteSexoPaciente;
	}
}
