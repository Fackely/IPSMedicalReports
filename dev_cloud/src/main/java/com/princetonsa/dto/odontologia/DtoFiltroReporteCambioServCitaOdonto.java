package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoServicios;

public class DtoFiltroReporteCambioServCitaOdonto implements Serializable {

	private static final long serialVersionUID = 1L;

	
	
	public DtoFiltroReporteCambioServCitaOdonto() {
		
		this.codigoPaisSeleccionado = "";
		this.ciudadDeptoPais = "";
		this.codigoRegionSeleccionada = 0;
		this.codigoEmpresaInstitucionSeleccionada = 0;
		this.consecutivoCentroAtencionSeleccionado = 0;
		this.rangoEdadInicialPaciente = 0;
		this.rangoEdadFinalPaciente = 0;
		this.tiposEstadoSolicitud = new String[]{};
		this.loginProfesional = "";
		this.loginUsuario = "";
		this.codigoEspecialidad = 0;
		this.codigoPrograma = 0;
		this.codigoServicio = 0;
		this.codigoPaisResidencia = "";
		this.codigoCiudad = "";
		this.codigoPais = "";
		this.codigoDpto = "";
		this.razonSocial = "";
		this.sexo = "";
		this.nombreEspecialidad = "";
		this.nombrePrograma = "";
	}
	
	/**
	 * Atributo que almacena si la institucion es multiempresa.
	 */
	private String esInstitucionMultiempresa;

	
	/**
	 * Atributo que almacena la fecha a partir de la cual se 
	 * buscarán las solicitudes de cambio de servicios generadas.
	 */
	private String fechaInicial;
	
	
	/**
	 * Atributo que almacena la fecha hasta la cual se buscarán 
	 * las solicitudes de cambio de servicios generadas.
	 */
	private String fechaFinal;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * un pa&iacute;s.
	 */
	private String codigoPaisSeleccionado;
	
	
	/**
	 * Atributo que almacena el tercer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoDpto;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * una regi&oacute;n.
	 */
	private long codigoRegionSeleccionada;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de la empresa-institucion que 
	 * fue seleccionada en la forma.
	 */
	private long codigoEmpresaInstitucionSeleccionada;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de la institucion si la institucion
	 * no es multiempresa
	 */
	private int codigoInstitucion;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * un centro de atenci&oacute;n.
	 */
	private int consecutivoCentroAtencionSeleccionado;
	
	
	/**
	 * Atributo que almacena los c&oacute;digos de
	 * los estados de solicitud.
	 */
	private String[] tiposEstadoSolicitud;
	
	
	/**
	 * Atributo que almacena la edad en años a partir de la cual 
	 * se buscarán los pacientes asociados a solicitudes de cambio de 
	 * servicio de citas Odontológicas
	 */
	private Integer rangoEdadInicialPaciente;
	
	
	/**
	 * Atributo donde se almacena la edad en años  hasta la cual se 
	 * buscarán los pacientes asociados a solicitudes de cambio de 
	 * servicio de citas Odontológicas
	 */
	private Integer rangoEdadFinalPaciente;
	

	/**
	 * Atributo que almacena el login de
	 * usuario que confirm&oacute; o anul&oacute; 
	 * la solicitud de cambio de servicio.
	 */
	private String loginUsuario;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * la especialidad odontol&oacute;gica.
	 */
	private int codigoEspecialidad;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo del
	 * Programa odontol&oacute;gico.
	 */
	private long codigoPrograma;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo del
	 * servicio odontol&oacute;gico.
	 */
	private int codigoServicio;
	
	
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
	
	/**
	 * Atributo que almacena el sexo del paciente.
	 */
	private String sexo;
	
	/**
	 * Atributo que indica la posici&oacute;n en donde 
	 * debe ser mostrado el logo.
	 */
	private String ubicacionLogo;
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
	 * Atributo que almacena el c&oacute;digo de un pa&iacute;s de residencia.
	 */
	private String codigoPaisResidencia;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo que identifica a 
	 * una determinada ciudad.
	 */
	private String ciudadDeptoPais;
	
	
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
	 * Atributo que almacena el login de
	 * un profesional de la salud.
	 */
	private String loginProfesional;
	
	
	/**
	 * Atributo que almacena el sexo del paciente.
	 */
	private String sexoPaciente;
	
	
	/**
	 * Atributo que almacena el rango de edades por el cual se realiza la consulta.
	 */
	private String rangoEdadConsultada;
	
	
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
	 * Atributo que permite almacenar el nombre del sexo para imprimirlo
	 * en el reporte generado
	 */
	private String ayudanteSexo;
	
	
	/**
	 * Atributo donde se almacenan los Servicios odontol&oacute;gicos existentes en el sistema.
	 */
	private ArrayList<DtoServicios> servicios;
	
	/**
	 * Atributo donde se almacena el nombre de la especialidad.
	 */
	private String nombreEspecialidad;
	
	/**
	 * Atributo donde se almacena el nombre del programa.
	 */
	private String nombrePrograma;
		
	/**
	 * Atributo que almacena la ruta del logo de la institución.
	 */
	private String rutaLogo;
	
	
	/**
	 * Atributo que almacena el nombre completo del usuario activo en la sesion.
	 */
	private String usuarioProcesa;
	
	
	/**
	 * Atributo donde se almacena la fecha desde donde se debe 
	 * comenzar a buscar las solicitudes de cambio de servicio 
	 * dependiendo de la edad inicial digitada.
	 */
	private String rangoEdadInicialFecha;
	
	
	/**
	 * Atributo donde se almacena la fecha hasta donde se debe 
	 * buscar las solicitudes de cambio de servicio 
	 * dependiendo de la edad final digitada.
	 */
	private String rangoEdadFinalFecha;
	
	/**
	 * Atributo que permite almacenar el codigo de los servicios
	 */
	private ArrayList<Integer> codigosServicios;
	
	/**
	 * Atributo que permite almacenar el codigo estandar para la busqueda de servicios
	 */
	private int codigoManualEstandarBusquedaServicios;
	
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
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoEspecialidad
	 * 
	 * @param  valor para el atributo codigoEspecialidad
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoServicio
	 * 
	 * @return  Retorna la variable codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo codigoServicio
	 * 
	 * @param  valor para el atributo codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
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
	 *  del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @return  Retorna la variable sexoPaciente
	 */
	public String getSexo() {
		return sexo;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @param  valor para el atributo sexoPaciente
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad, si no se ha seleccionado
	 *  ninguna especialidad el metodo devuelve la cadena Todos
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
	 *  del atributo nombreEspecialidad
	 * 
	 * @param  valor para el atributo nombreEspecialidad
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombrePrograma, si no se ha seleccionado
	 *  ningun programa el metodo devuelve la cadena Todos
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
	 *  del atributo nombrePrograma
	 * 
	 * @param  valor para el atributo nombrePrograma
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rutaLogo
	 * 
	 * @return  Retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rutaLogo
	 * 
	 * @param  valor para el atributo rutaLogo 
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
	 *  del atributo fechaInicialFormateado convirtiendo
	 *  la fechaInicial de tipo yyyy-mm-dd
	 * 
	 * @return  Retorna la variable fechaInicialFormateado
	 */
	public String getFechaInicialFormateado() {
		fechaInicialFormateado = UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
		this.fechaInicialFormateado=UtilidadFecha.conversionFormatoFechaAAp(fechaInicialFormateado);
		return fechaInicialFormateado;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo fechaInicialFormateado
	 * 
	 * @param  valor para el atributo fechaInicialFormateado
	 */
	public void setFechaInicialFormateado(String fechaInicialFormateado) {
		this.fechaInicialFormateado = fechaInicialFormateado;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinalFormateado convirtiendo
	 *  la fechaFinal de tipo yyyy-mm-dd
	 * 
	 * @return  Retorna la variable fechaFinalFormateado
	 */
	public String getFechaFinalFormateado() {
		fechaFinalFormateado = UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
		this.fechaFinalFormateado=UtilidadFecha.conversionFormatoFechaAAp(fechaFinalFormateado);
		return fechaFinalFormateado;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 *  del atributo fechaFinalFormateado
	 * 
	 * @param  valor para el atributo fechaFinalFormateado
	 */
	public void setFechaFinalFormateado(String fechaFinalFormateado) {
		this.fechaFinalFormateado = fechaFinalFormateado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el listado de
	 * servicios 
	 * 
	 * @return  Retorna la variable servicios
	 */
	public ArrayList<DtoServicios> getServicios() {
		return servicios;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el listado de
	 *  servicios
	 * 
	 * @param  valor para el atributo servicios
	 */
	public void setServicios(ArrayList<DtoServicios> servicios) {
		this.servicios = servicios;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el listado de
	 *  codigosServicios
	 * 
	 * @return  Retorna la variable codigosServicios
	 */
	public ArrayList<Integer> getCodigosServicios() {
		
		return codigosServicios;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el listado de
	 *  codigosServicios
	 * 
	 * @param  valor para el atributo codigosServicios
	 */
	public void setCodigosServicios(ArrayList<Integer> codigosServicios) {
		this.codigosServicios = codigosServicios;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del
	 * atributo rangoEdadInicialFecha
	 * 
	 * @return  Retorna la variable rangoEdadInicialFecha
	 */
	public String getRangoEdadInicialFecha() {
		return rangoEdadInicialFecha;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo rangoEdadInicialFecha
	 * 
	 * @param  valor para el atributo rangoEdadInicialFecha
	 */
	public void setRangoEdadInicialFecha(String rangoEdadInicialFecha) {
		this.rangoEdadInicialFecha = rangoEdadInicialFecha;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo rangoEdadFinalFecha
	 * 
	 * @return  Retorna la variable rangoEdadFinalFecha
	 */
	public String getRangoEdadFinalFecha() {
		return rangoEdadFinalFecha;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo rangoEdadFinalFecha
	 * 
	 * @param  valor para el atributo rangoEdadFinalFecha
	 */
	public void setRangoEdadFinalFecha(String rangoEdadFinalFecha) {
		this.rangoEdadFinalFecha = rangoEdadFinalFecha;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo loginProfesional
	 * 
	 * @return  Retorna la variable loginProfesional
	 */
	public String getLoginProfesional() {
		return loginProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo loginProfesional
	 * 
	 * @param  valor para el atributo loginProfesional
	 */
	public void setLoginProfesional(String loginProfesional) {
		this.loginProfesional = loginProfesional;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoCiudad
	 * 
	 * @return  Retorna la variable codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoPais
	 * 
	 * @return  Retorna la variable codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoDpto
	 * 
	 * @return  Retorna la variable codigoDpto
	 */
	public String getCodigoDpto() {
		return codigoDpto;
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoPaisResidencia
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo ciudadDeptoPais
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo rangoEdadInicial
	 * 
	 * @return  Retorna la variable rangoEdadInicial
	 */
	public String getRangoEdadInicial() {
		return rangoEdadInicial;
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo rangoEdadFinal
	 * 
	 * @return  Retorna la variable rangoEdadFinal
	 */
	public String getRangoEdadFinal() {
		return rangoEdadFinal;
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo rangoEdadConsultada que permite concatenar la
	 * edad inicial y final para mostrarla en el reporte, sino 
	 * se ha seleccionado devuelve el valor todos
	 * 
	 * @return  Retorna la variable rangoEdadConsultada
	 */
	public String getRangoEdadConsultada() {
		if (!UtilidadTexto.isEmpty(rangoEdadInicial)) {
			this.rangoEdadConsultada = rangoEdadInicial + " - "+ rangoEdadFinal+ " años";
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
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoPaisSeleccionado
	 * 
	 * @return  Retorna la variable codigoPaisSeleccionado
	 */
	public String getCodigoPaisSeleccionado() {
		return codigoPaisSeleccionado;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoPaisSeleccionado
	 * 
	 * @param  valor para el atributo codigoPaisSeleccionado
	 */
	public void setCodigoPaisSeleccionado(String codigoPaisSeleccionado) {
		this.codigoPaisSeleccionado = codigoPaisSeleccionado;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoRegionSeleccionada
	 * 
	 * @return  Retorna la variable codigoRegionSeleccionada
	 */
	public long getCodigoRegionSeleccionada() {
		return codigoRegionSeleccionada;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoRegionSeleccionada
	 * 
	 * @param  valor para el atributo codigoRegionSeleccionada
	 */
	public void setCodigoRegionSeleccionada(long codigoRegionSeleccionada) {
		this.codigoRegionSeleccionada = codigoRegionSeleccionada;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoEmpresaInstitucionSeleccionada
	 * 
	 * @return  Retorna la variable codigoEmpresaInstitucionSeleccionada
	 */
	public long getCodigoEmpresaInstitucionSeleccionada() {
		return codigoEmpresaInstitucionSeleccionada;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoEmpresaInstitucionSeleccionada
	 * 
	 * @param  valor para el atributo codigoEmpresaInstitucionSeleccionada
	 */
	public void setCodigoEmpresaInstitucionSeleccionada(
			long codigoEmpresaInstitucionSeleccionada) {
		this.codigoEmpresaInstitucionSeleccionada = codigoEmpresaInstitucionSeleccionada;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencionSeleccionado
	 */
	public int getConsecutivoCentroAtencionSeleccionado() {
		return consecutivoCentroAtencionSeleccionado;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencionSeleccionado
	 */
	public void setConsecutivoCentroAtencionSeleccionado(
			int consecutivoCentroAtencionSeleccionado) {
		this.consecutivoCentroAtencionSeleccionado = consecutivoCentroAtencionSeleccionado;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo rangoEdadInicialPaciente
	 * 
	 * @return  Retorna la variable rangoEdadInicialPaciente
	 */
	public Integer getRangoEdadInicialPaciente() {
		return rangoEdadInicialPaciente;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo rangoEdadInicialPaciente
	 * 
	 * @param  valor para el atributo rangoEdadInicialPaciente
	 */
	public void setRangoEdadInicialPaciente(Integer rangoEdadInicialPaciente) {
		this.rangoEdadInicialPaciente = rangoEdadInicialPaciente;

		if (rangoEdadInicialPaciente!=null) {
			rangoEdadInicial = UtilidadFecha.calcularFechaNacimiento(1, rangoEdadInicialPaciente);
		}
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo rangoEdadFinalPaciente
	 * 
	 * @return  Retorna la variable rangoEdadFinalPaciente
	 */
	public Integer getRangoEdadFinalPaciente() {
		return rangoEdadFinalPaciente;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo rangoEdadFinalPaciente
	 * 
	 * @param  valor para el atributo rangoEdadFinalPaciente
	 */
	public void setRangoEdadFinalPaciente(Integer rangoEdadFinalPaciente) {
		this.rangoEdadFinalPaciente = rangoEdadFinalPaciente;
		
		if (rangoEdadFinalPaciente !=null) {
			rangoEdadFinal = UtilidadFecha.calcularFechaNacimiento(1, rangoEdadFinalPaciente );
		}
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo tiposEstadoSolicitud
	 * 
	 * @return  Retorna la variable tiposEstadoSolicitud
	 */
	public String[] getTiposEstadoSolicitud() {
		return tiposEstadoSolicitud;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo tiposEstadoSolicitud
	 * 
	 * @param  valor para el atributo tiposEstadoSolicitud
	 */
	public void setTiposEstadoSolicitud(String[] tiposEstadoSolicitud) {
		this.tiposEstadoSolicitud = tiposEstadoSolicitud;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo loginUsuario
	 * 
	 * @return  Retorna la variable loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo loginUsuario
	 * 
	 * @param  valor para el atributo loginUsuario
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoEspecialidad
	 * 
	 * @return  Retorna la variable codigoEspecialidad
	 */
	public int getcodigoEspecialidad() {
		return codigoEspecialidad;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoEspecialidad
	 * 
	 * @param  valor para el atributo codigoEspecialidad
	 */
	public void setcodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoPrograma
	 * 
	 * @return  Retorna la variable codigoPrograma
	 */
	public long getcodigoPrograma() {
		return codigoPrograma;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoPrograma
	 * 
	 * @param  valor para el atributo codigoPrograma
	 */
	public void setcodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoServicio
	 * 
	 * @return  Retorna la variable codigoServicio
	 */
	public int getcodigoServicio() {
		return codigoServicio;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoServicio
	 * 
	 * @param  valor para el atributo codigoServicio
	 */
	public void setcodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo sexoPaciente
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
		
		
		if(sexoPaciente.equals(ConstantesIntegridadDominio.acronimoMasculino)){
			this.sexoPaciente = ConstantesBD.codigoSexoMasculino +"";
		}else
			if(sexoPaciente.equals(ConstantesIntegridadDominio.acronimoFemenino)){
				this.sexoPaciente=ConstantesBD.codigoSexoFemenino+"";
			}else{
				this.sexoPaciente = ConstantesBD.codigoSexoAmbos+"";
			}
		
	}
		
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo ayudanteSexo
	 * 
	 * @return  Retorna la variable ayudanteSexo
	 */
	public String getAyudanteSexo() {
		if(sexoPaciente.equals(ConstantesBD.codigoSexoMasculino +"")){
			ayudanteSexo="Masculino";
		}else
			if(sexoPaciente.equals(ConstantesBD.codigoSexoFemenino+"")){
				ayudanteSexo="Femenino";
			}else{
				ayudanteSexo="Femenino-Masculino";
			}
		return ayudanteSexo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo ayudanteSexo
	 * 
	 * @param  valor para el atributo ayudanteSexo
	 */
	public void setAyudanteSexo(String ayudanteSexo) {
		this.ayudanteSexo = ayudanteSexo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo usuarioProcesa
	 * 
	 * @param  valor para el atributo usuarioProcesa
	 */
	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo usuarioProcesa
	 * 
	 * @return  Retorna la variable usuarioProcesa
	 */
	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo esInstitucionMultiempresa
	 * 
	 * @param  valor para el atributo esInstitucionMultiempresa
	 */
	public void setEsInstitucionMultiempresa(String esInstitucionMultiempresa) {
		this.esInstitucionMultiempresa = esInstitucionMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo esInstitucionMultiempresa
	 * 
	 * @return  Retorna la variable esInstitucionMultiempresa
	 */
	public String getEsInstitucionMultiempresa() {
		return esInstitucionMultiempresa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoInstitucion
	 * 
	 * @param  valor para el atributo codigoInstitucion
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoInstitucion
	 * 
	 * @return  Retorna la variable codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	public void setCodigoManualEstandarBusquedaServicios(
			int codigoManualEstandarBusquedaServicios) {
		this.codigoManualEstandarBusquedaServicios = codigoManualEstandarBusquedaServicios;
	}

	public int getCodigoManualEstandarBusquedaServicios() {
		return codigoManualEstandarBusquedaServicios;
	}
	
	
	
	
		
}

