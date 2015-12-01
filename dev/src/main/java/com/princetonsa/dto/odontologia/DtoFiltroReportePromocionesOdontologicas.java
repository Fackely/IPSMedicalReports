package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;


/**
 * Esta clase se encarga de recibir los datos ingresados en la forma de reporte promociones odontológicas
 * 
 * @author Fabian Becerra
 *
 */
public class DtoFiltroReportePromocionesOdontologicas implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena la razón social
	 * de la institución.
	 */
	private String razonSocial;
	
	
	/**
	 * Atributo que almacena la ruta del logo de la institución.
	 */
	private String rutaLogo;
	
	
	/**
	 * Atributo que indica la posición en donde 
	 * debe ser mostrado el logo.
	 */
	private String ubicacionLogo;
	
	
	/**
	 * Atributo que almacena el nombre del usuario activo
	 */
	private String nombreUsuario;
	
	
	/**
	 * Atributo que almacena el código de
	 * la especialidad odontológica.
	 */
	private int codigoEspecialidad;
	
	
	/**
	 * Atributo que almacena el nombre de la especialidad.
	 */
	private String nombreEspecialidad;
	
	
	/**
	 * Atributo donde se almacena la fecha de generación inicial de la promoción
	 */
	private Date fechaGenInicial;
	
	
	/**
	 * Atributo donde se almacena la fecha de generación inicial de la promoción 
	 * con formato dd/MM/yyyy en String
	 */
	private String fechaGenInicialF;
	
	
	/**
	 * Atributo donde se almacena la fecha de generación final de la promoción
	 */
	private Date fechaGenFinal;
	
	
	/**
	 * Atributo donde se almacena la fecha de generación final de la promoción 
	 * con formato dd/MM/yyyy en String
	 */
	private String fechaGenFinalF;
	
	
	/**
	 * Atributo donde se almacena la fecha de vigencia inicial de la promoción
	 */
	private Date fechaVigInicial;
	
	
	/**
	 * Atributo donde se almacena la fecha de vigencia final de la promoción
	 */
	private Date fechaVigFinal;
	
	
	/**
	 * Atributo donde se almacena el estado civil del paciente
	 */
	private String estadoCivilPaciente;
	
	
	/**
	 * Atributo donde se almacena el codigo de la ocupación del paciente
	 */
	private String codigoOcupacion;
	
	
	/**
	 * Atributo donde se almacena el codigo del convenio
	 */
	private String codigoConvenio;
	
	
	/**
	 * Atributo donde se almacena el codigo de la institución
	 */
	private int codigoInstitucion;
	
	
	/**
	 * Atributo que almacena el código del
	 * Programa odontológico.
	 */
	private long codigoPrograma;
	
	
	/**
	 * Atributo que almacena el código de
	 * un país.
	 */
	private String codigoPaisSeleccionado;
	
	/**
	 * Atributo que almacena el código que identifica a 
	 * una determinada ciudad.
	 */
	private String ciudadDeptoPais;
	
	
	/**
	 * Atributo que almacena el código de
	 * una región.
	 */
	private long codigoRegionSeleccionada;
	
	
	/**
	 * Atributo que almacena el código de la empresa-institucion.
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
	 * Atributo que almacena la edad a partir de la cual 
	 * se buscaran los pacientes asociados a solicitudes de cambio de 
	 * servicio de citas Odontologicas
	 */
	private Integer rangoEdadInicialPaciente;
	
	
	/**
	 * Atributo donde se almacena la edad hasta la cual se 
	 * buscaran los pacientes asociados a solicitudes de cambio de 
	 * servicio de citas Odontologicas
	 */
	private Integer rangoEdadFinalPaciente;
	
	
	/**
	 * Atributo que almacena el sexo del paciente.
	 */
	private String sexoPaciente;
	
	
	/**
	 * Atributo que almacena el codigo del sexo del paciente.
	 */
	private String codigoSexoPaciente;
	
	
	/**
	 * Atributo que almacena el estado de la promocion.
	 */
	private String estadoPromocion;
	
	
	/**
	 * Atributo que almacena si esta activa o no la promoción.
	 */
	private String estadoActivoPromocion;
	
	
	/**
	 * Atributo que almacena el código de un país de residencia.
	 */
	private String codigoPaisResidencia;
	
	
	/**
	 * Atributo que almacena el código de
	 * un centro de atención.
	 */
	private int consecutivoCentroAtencionSeleccionado;
	
	
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String institucionMultiempresa;
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaGenInicial
 	 * 
 	 * @param fechaGenInicial valor para el atributo fechaGenInicial
 	 */
	public void setFechaGenInicial(Date fechaGenInicial) {
		this.fechaGenInicial = fechaGenInicial;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaGenInicial
	 * 
	 * @return Retorna la variable fechaGenInicial
	 */
	public Date getFechaGenInicial() {
		return fechaGenInicial;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaGenFinal
 	 * 
 	 * @param fechaGenFinal valor para el atributo fechaGenFinal
 	 */
	public void setFechaGenFinal(Date fechaGenFinal) {
		this.fechaGenFinal = fechaGenFinal;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaGenFinal
	 * 
	 * @return  Retorna la variable fechaGenFinal
	 */
	public Date getFechaGenFinal() {
		return fechaGenFinal;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaVigFinal
 	 * 
 	 * @param fechaVigFinal valor para el atributo fechaVigFinal
 	 */
	public void setFechaVigFinal(Date fechaVigFinal) {
		this.fechaVigFinal = fechaVigFinal;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaVigFinal
	 * 
	 * @return  Retorna la variable fechaVigFinal
	 */
	public Date getFechaVigFinal() {
		return fechaVigFinal;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaVigInicial
 	 * 
 	 * @param fechaVigInicial valor para el atributo fechaVigInicial
 	 */
	public void setFechaVigInicial(Date fechaVigInicial) {
		this.fechaVigInicial = fechaVigInicial;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaVigInicial
	 * 
	 * @return  Retorna la variable fechaVigInicial
	 */
	public Date getFechaVigInicial() {
		return fechaVigInicial;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoPaisSeleccionado
 	 * 
 	 * @param codigoPaisSeleccionado valor para el atributo codigoPaisSeleccionado
 	 */
	public void setCodigoPaisSeleccionado(String codigoPaisSeleccionado) {
		this.codigoPaisSeleccionado = codigoPaisSeleccionado;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoPaisSeleccionado
	 * 
	 * @return  Retorna la variable codigoPaisSeleccionado
	 */
	public String getCodigoPaisSeleccionado() {
		return codigoPaisSeleccionado;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ciudadDeptoPais
 	 * 
 	 * @param ciudadDeptoPais valor para el atributo ciudadDeptoPais
 	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ciudadDeptoPais
	 * 
	 * @return  Retorna la variable ciudadDeptoPais
	 */
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoRegionSeleccionada
 	 * 
 	 * @param codigoRegionSeleccionada valor para el atributo codigoRegionSeleccionada
 	 */
	public void setCodigoRegionSeleccionada(long codigoRegionSeleccionada) {
		this.codigoRegionSeleccionada = codigoRegionSeleccionada;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoRegionSeleccionada
	 * 
	 * @return  Retorna la variable codigoRegionSeleccionada
	 */
	public long getCodigoRegionSeleccionada() {
		return codigoRegionSeleccionada;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoEmpresaInstitucion
 	 * 
 	 * @param codigoEmpresaInstitucion valor para el atributo codigoEmpresaInstitucion
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
 	 *  del atributo institucionMultiempresa
 	 * 
 	 * @param institucionMultiempresa valor para el atributo institucionMultiempresa
 	 */
	public void setInstitucionMultiempresa(String institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo institucionMultiempresa
	 * 
	 * @return  Retorna la variable institucionMultiempresa
	 */
	public String getInstitucionMultiempresa() {
		return institucionMultiempresa;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo consecutivoCentroAtencionSeleccionado
 	 * 
 	 * @param consecutivoCentroAtencionSeleccionado valor para el atributo consecutivoCentroAtencionSeleccionado
 	 */
	public void setConsecutivoCentroAtencionSeleccionado(
			int consecutivoCentroAtencionSeleccionado) {
		this.consecutivoCentroAtencionSeleccionado = consecutivoCentroAtencionSeleccionado;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencionSeleccionado
	 */
	public int getConsecutivoCentroAtencionSeleccionado() {
		return consecutivoCentroAtencionSeleccionado;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoPaisResidencia
 	 * 
 	 * @param codigoPaisResidencia valor para el atributo codigoPaisResidencia
 	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoPaisResidencia
	 * 
	 * @return  Retorna la variable codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoCiudad
 	 * 
 	 * @param codigoCiudad valor para el atributo codigoCiudad
 	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoCiudad
	 * 
	 * @return  Retorna la variable codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoPais
 	 * 
 	 * @param codigoPais valor para el atributo codigoPais
 	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoPais
	 * 
	 * @return  Retorna la variable codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoDpto
 	 * 
 	 * @param codigoDpto valor para el atributo codigoDpto
 	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoDpto
	 * 
	 * @return  Retorna la variable codigoDpto
	 */
	public String getCodigoDpto() {
		return codigoDpto;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo rangoEdadInicialPaciente
 	 * 
 	 * @param rangoEdadInicialPaciente valor para el atributo rangoEdadInicialPaciente
 	 */
	public void setRangoEdadInicialPaciente(Integer rangoEdadInicialPaciente) {
		this.rangoEdadInicialPaciente = rangoEdadInicialPaciente;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo rangoEdadInicialPaciente
	 * 
	 * @return  Retorna la variable rangoEdadInicialPaciente
	 */
	public Integer getRangoEdadInicialPaciente() {
		return rangoEdadInicialPaciente;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo rangoEdadFinalPaciente
 	 * 
 	 * @param rangoEdadFinalPaciente valor para el atributo rangoEdadFinalPaciente
 	 */
	public void setRangoEdadFinalPaciente(Integer rangoEdadFinalPaciente) {
		this.rangoEdadFinalPaciente = rangoEdadFinalPaciente;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo rangoEdadFinalPaciente
	 * 
	 * @return  Retorna la variable rangoEdadFinalPaciente
	 */
	public Integer getRangoEdadFinalPaciente() {
		return rangoEdadFinalPaciente;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo sexoPaciente, dependiendo del acronimo del sexo
 	 *  asigna el codigo
 	 * 
 	 * @param sexoPaciente valor para el atributo sexoPaciente
 	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente=sexoPaciente;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @return  Retorna la variable sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo estadoCivilPaciente
 	 * 
 	 * @param estadoCivilPaciente valor para el atributo estadoCivilPaciente
 	 */
	public void setEstadoCivilPaciente(String estadoCivilPaciente) {
		this.estadoCivilPaciente = estadoCivilPaciente;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estadoCivilPaciente
	 * 
	 * @return  Retorna la variable estadoCivilPaciente
	 */
	public String getEstadoCivilPaciente() {
		return estadoCivilPaciente;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoOcupacion
 	 * 
 	 * @param codigoOcupacion valor para el atributo codigoOcupacion
 	 */
	public void setCodigoOcupacion(String codigoOcupacion) {
		this.codigoOcupacion = codigoOcupacion;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoOcupacion
	 * 
	 * @return  Retorna la variable codigoOcupacion
	 */
	public String getCodigoOcupacion() {
		return codigoOcupacion;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoEspecialidad
 	 * 
 	 * @param codigoEspecialidad valor para el atributo codigoEspecialidad
 	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoEspecialidad
	 * 
	 * @return  Retorna la variable codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nombreEspecialidad
 	 * 
 	 * @param nombreEspecialidad valor para el atributo nombreEspecialidad
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
 	 *  del atributo estadoPromocion
 	 * 
 	 * @param estadoPromocion valor para el atributo estadoPromocion
 	 */
	public void setEstadoPromocion(String estadoPromocion) {
		this.estadoPromocion = estadoPromocion;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estadoPromocion
	 * 
	 * @return  Retorna la variable estadoPromocion
	 */
	public String getEstadoPromocion() {
		return estadoPromocion;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoConvenio
 	 * 
 	 * @param codigoConvenio valor para el atributo codigoConvenio
 	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoConvenio
	 * 
	 * @return  Retorna la variable codigoConvenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nombreUsuario
 	 * 
 	 * @param nombreUsuario valor para el atributo nombreUsuario
 	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombreUsuario
	 * 
	 * @return  Retorna la variable nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaGenInicialF
 	 * 
 	 * @param fechaGenInicialF valor para el atributo fechaGenInicialF
 	 */
	public void setFechaGenInicialF(String fechaGenInicialF) {
		this.fechaGenInicialF = fechaGenInicialF;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaGenInicialF
	 * 
	 * @return  Retorna la variable fechaGenInicialF
	 */
	public String getFechaGenInicialF() {
		this.fechaGenInicialF=UtilidadFecha.conversionFormatoFechaAAp(this.fechaGenInicial);
		return fechaGenInicialF;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaGenFinalF
 	 * 
 	 * @param fechaGenFinalF valor para el atributo fechaGenFinalF
 	 */
	public void setFechaGenFinalF(String fechaGenFinalF) {
		this.fechaGenFinalF = fechaGenFinalF;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaGenFinalF
	 * 
	 * @return  Retorna la variable fechaGenFinalF
	 */
	public String getFechaGenFinalF() {
		this.fechaGenFinalF=UtilidadFecha.conversionFormatoFechaAAp(this.fechaGenFinal);
		return fechaGenFinalF;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo rutaLogo
 	 * 
 	 * @param rutaLogo valor para el atributo rutaLogo
 	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo rutaLogo
	 * 
	 * @return  Retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ubicacionLogo
 	 * 
 	 * @param ubicacionLogo valor para el atributo ubicacionLogo
 	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo razonSocial
 	 * 
 	 * @param razonSocial valor para el atributo razonSocial
 	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoInstitucion
 	 * 
 	 * @param codigoInstitucion valor para el atributo codigoInstitucion
 	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoInstitucion
	 * 
	 * @return  Retorna la variable codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoPrograma
 	 * 
 	 * @param codigoPrograma valor para el atributo codigoPrograma
 	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoPrograma
	 * 
	 * @return  Retorna la variable codigoPrograma
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}
	
	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo estadoActivoPromocion
 	 * 
 	 * @param estadoActivoPromocion valor para el atributo estadoActivoPromocion
 	 */
	public void setEstadoActivoPromocion(String estadoActivoPromocion) {
		this.estadoActivoPromocion = estadoActivoPromocion;
	}
	
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estadoActivoPromocion dependiendo
	 *  del atributo estadoPromocion, si este ultimo esta en activo
	 *  retorna S, si esta inactivo retorna N
	 * 
	 * @return  Retorna la variable estadoActivoPromocion
	 */
	public String getEstadoActivoPromocion() {
		if(this.estadoPromocion.equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
			this.estadoActivoPromocion=ConstantesBD.acronimoSi;
		else
			if(this.estadoPromocion.equals(ConstantesIntegridadDominio.acronimoEstadoInactivo))
				this.estadoActivoPromocion=ConstantesBD.acronimoNo;
			else
				this.estadoActivoPromocion=ConstantesBD.codigoNuncaValido+"";
		return estadoActivoPromocion;
	}


	public void setCodigoSexoPaciente(String codigoSexoPaciente) {
		this.codigoSexoPaciente = codigoSexoPaciente;
	}


	public String getCodigoSexoPaciente() {
		if(sexoPaciente.equals(ConstantesIntegridadDominio.acronimoMasculino)){
			this.codigoSexoPaciente = ConstantesBD.codigoSexoMasculino +"";
		}else
		if(sexoPaciente.equals(ConstantesIntegridadDominio.acronimoFemenino)){
			this.codigoSexoPaciente=ConstantesBD.codigoSexoFemenino+"";
		}else{
			this.codigoSexoPaciente = ConstantesBD.codigoSexoAmbos+"";
		}
		return codigoSexoPaciente;
	}
	

	

	
	

}
