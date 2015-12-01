package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;


/**
 * Esta clase se encarga de recibir los datos ingresados en la forma de reporte promociones odontol�gicas
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
	 * Atributo que almacena la raz�n social
	 * de la instituci�n.
	 */
	private String razonSocial;
	
	
	/**
	 * Atributo que almacena la ruta del logo de la instituci�n.
	 */
	private String rutaLogo;
	
	
	/**
	 * Atributo que indica la posici�n en donde 
	 * debe ser mostrado el logo.
	 */
	private String ubicacionLogo;
	
	
	/**
	 * Atributo que almacena el nombre del usuario activo
	 */
	private String nombreUsuario;
	
	
	/**
	 * Atributo que almacena el c�digo de
	 * la especialidad odontol�gica.
	 */
	private int codigoEspecialidad;
	
	
	/**
	 * Atributo que almacena el nombre de la especialidad.
	 */
	private String nombreEspecialidad;
	
	
	/**
	 * Atributo donde se almacena la fecha de generaci�n inicial de la promoci�n
	 */
	private Date fechaGenInicial;
	
	
	/**
	 * Atributo donde se almacena la fecha de generaci�n inicial de la promoci�n 
	 * con formato dd/MM/yyyy en String
	 */
	private String fechaGenInicialF;
	
	
	/**
	 * Atributo donde se almacena la fecha de generaci�n final de la promoci�n
	 */
	private Date fechaGenFinal;
	
	
	/**
	 * Atributo donde se almacena la fecha de generaci�n final de la promoci�n 
	 * con formato dd/MM/yyyy en String
	 */
	private String fechaGenFinalF;
	
	
	/**
	 * Atributo donde se almacena la fecha de vigencia inicial de la promoci�n
	 */
	private Date fechaVigInicial;
	
	
	/**
	 * Atributo donde se almacena la fecha de vigencia final de la promoci�n
	 */
	private Date fechaVigFinal;
	
	
	/**
	 * Atributo donde se almacena el estado civil del paciente
	 */
	private String estadoCivilPaciente;
	
	
	/**
	 * Atributo donde se almacena el codigo de la ocupaci�n del paciente
	 */
	private String codigoOcupacion;
	
	
	/**
	 * Atributo donde se almacena el codigo del convenio
	 */
	private String codigoConvenio;
	
	
	/**
	 * Atributo donde se almacena el codigo de la instituci�n
	 */
	private int codigoInstitucion;
	
	
	/**
	 * Atributo que almacena el c�digo del
	 * Programa odontol�gico.
	 */
	private long codigoPrograma;
	
	
	/**
	 * Atributo que almacena el c�digo de
	 * un pa�s.
	 */
	private String codigoPaisSeleccionado;
	
	/**
	 * Atributo que almacena el c�digo que identifica a 
	 * una determinada ciudad.
	 */
	private String ciudadDeptoPais;
	
	
	/**
	 * Atributo que almacena el c�digo de
	 * una regi�n.
	 */
	private long codigoRegionSeleccionada;
	
	
	/**
	 * Atributo que almacena el c�digo de la empresa-institucion.
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
	 * Atributo que almacena si esta activa o no la promoci�n.
	 */
	private String estadoActivoPromocion;
	
	
	/**
	 * Atributo que almacena el c�digo de un pa�s de residencia.
	 */
	private String codigoPaisResidencia;
	
	
	/**
	 * Atributo que almacena el c�digo de
	 * un centro de atenci�n.
	 */
	private int consecutivoCentroAtencionSeleccionado;
	
	
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String institucionMultiempresa;
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo fechaGenInicial
 	 * 
 	 * @param fechaGenInicial valor para el atributo fechaGenInicial
 	 */
	public void setFechaGenInicial(Date fechaGenInicial) {
		this.fechaGenInicial = fechaGenInicial;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaGenInicial
	 * 
	 * @return Retorna la variable fechaGenInicial
	 */
	public Date getFechaGenInicial() {
		return fechaGenInicial;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo fechaGenFinal
 	 * 
 	 * @param fechaGenFinal valor para el atributo fechaGenFinal
 	 */
	public void setFechaGenFinal(Date fechaGenFinal) {
		this.fechaGenFinal = fechaGenFinal;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaGenFinal
	 * 
	 * @return  Retorna la variable fechaGenFinal
	 */
	public Date getFechaGenFinal() {
		return fechaGenFinal;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo fechaVigFinal
 	 * 
 	 * @param fechaVigFinal valor para el atributo fechaVigFinal
 	 */
	public void setFechaVigFinal(Date fechaVigFinal) {
		this.fechaVigFinal = fechaVigFinal;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaVigFinal
	 * 
	 * @return  Retorna la variable fechaVigFinal
	 */
	public Date getFechaVigFinal() {
		return fechaVigFinal;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo fechaVigInicial
 	 * 
 	 * @param fechaVigInicial valor para el atributo fechaVigInicial
 	 */
	public void setFechaVigInicial(Date fechaVigInicial) {
		this.fechaVigInicial = fechaVigInicial;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaVigInicial
	 * 
	 * @return  Retorna la variable fechaVigInicial
	 */
	public Date getFechaVigInicial() {
		return fechaVigInicial;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoPaisSeleccionado
 	 * 
 	 * @param codigoPaisSeleccionado valor para el atributo codigoPaisSeleccionado
 	 */
	public void setCodigoPaisSeleccionado(String codigoPaisSeleccionado) {
		this.codigoPaisSeleccionado = codigoPaisSeleccionado;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoPaisSeleccionado
	 * 
	 * @return  Retorna la variable codigoPaisSeleccionado
	 */
	public String getCodigoPaisSeleccionado() {
		return codigoPaisSeleccionado;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo ciudadDeptoPais
 	 * 
 	 * @param ciudadDeptoPais valor para el atributo ciudadDeptoPais
 	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo ciudadDeptoPais
	 * 
	 * @return  Retorna la variable ciudadDeptoPais
	 */
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoRegionSeleccionada
 	 * 
 	 * @param codigoRegionSeleccionada valor para el atributo codigoRegionSeleccionada
 	 */
	public void setCodigoRegionSeleccionada(long codigoRegionSeleccionada) {
		this.codigoRegionSeleccionada = codigoRegionSeleccionada;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoRegionSeleccionada
	 * 
	 * @return  Retorna la variable codigoRegionSeleccionada
	 */
	public long getCodigoRegionSeleccionada() {
		return codigoRegionSeleccionada;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoEmpresaInstitucion
 	 * 
 	 * @param codigoEmpresaInstitucion valor para el atributo codigoEmpresaInstitucion
 	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoEmpresaInstitucion
	 * 
	 * @return  Retorna la variable codigoEmpresaInstitucion
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo institucionMultiempresa
 	 * 
 	 * @param institucionMultiempresa valor para el atributo institucionMultiempresa
 	 */
	public void setInstitucionMultiempresa(String institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo institucionMultiempresa
	 * 
	 * @return  Retorna la variable institucionMultiempresa
	 */
	public String getInstitucionMultiempresa() {
		return institucionMultiempresa;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo consecutivoCentroAtencionSeleccionado
 	 * 
 	 * @param consecutivoCentroAtencionSeleccionado valor para el atributo consecutivoCentroAtencionSeleccionado
 	 */
	public void setConsecutivoCentroAtencionSeleccionado(
			int consecutivoCentroAtencionSeleccionado) {
		this.consecutivoCentroAtencionSeleccionado = consecutivoCentroAtencionSeleccionado;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencionSeleccionado
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencionSeleccionado
	 */
	public int getConsecutivoCentroAtencionSeleccionado() {
		return consecutivoCentroAtencionSeleccionado;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoPaisResidencia
 	 * 
 	 * @param codigoPaisResidencia valor para el atributo codigoPaisResidencia
 	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoPaisResidencia
	 * 
	 * @return  Retorna la variable codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoCiudad
 	 * 
 	 * @param codigoCiudad valor para el atributo codigoCiudad
 	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoCiudad
	 * 
	 * @return  Retorna la variable codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoPais
 	 * 
 	 * @param codigoPais valor para el atributo codigoPais
 	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoPais
	 * 
	 * @return  Retorna la variable codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoDpto
 	 * 
 	 * @param codigoDpto valor para el atributo codigoDpto
 	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoDpto
	 * 
	 * @return  Retorna la variable codigoDpto
	 */
	public String getCodigoDpto() {
		return codigoDpto;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo rangoEdadInicialPaciente
 	 * 
 	 * @param rangoEdadInicialPaciente valor para el atributo rangoEdadInicialPaciente
 	 */
	public void setRangoEdadInicialPaciente(Integer rangoEdadInicialPaciente) {
		this.rangoEdadInicialPaciente = rangoEdadInicialPaciente;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadInicialPaciente
	 * 
	 * @return  Retorna la variable rangoEdadInicialPaciente
	 */
	public Integer getRangoEdadInicialPaciente() {
		return rangoEdadInicialPaciente;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo rangoEdadFinalPaciente
 	 * 
 	 * @param rangoEdadFinalPaciente valor para el atributo rangoEdadFinalPaciente
 	 */
	public void setRangoEdadFinalPaciente(Integer rangoEdadFinalPaciente) {
		this.rangoEdadFinalPaciente = rangoEdadFinalPaciente;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadFinalPaciente
	 * 
	 * @return  Retorna la variable rangoEdadFinalPaciente
	 */
	public Integer getRangoEdadFinalPaciente() {
		return rangoEdadFinalPaciente;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo sexoPaciente, dependiendo del acronimo del sexo
 	 *  asigna el codigo
 	 * 
 	 * @param sexoPaciente valor para el atributo sexoPaciente
 	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente=sexoPaciente;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo sexoPaciente
	 * 
	 * @return  Retorna la variable sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo estadoCivilPaciente
 	 * 
 	 * @param estadoCivilPaciente valor para el atributo estadoCivilPaciente
 	 */
	public void setEstadoCivilPaciente(String estadoCivilPaciente) {
		this.estadoCivilPaciente = estadoCivilPaciente;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo estadoCivilPaciente
	 * 
	 * @return  Retorna la variable estadoCivilPaciente
	 */
	public String getEstadoCivilPaciente() {
		return estadoCivilPaciente;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoOcupacion
 	 * 
 	 * @param codigoOcupacion valor para el atributo codigoOcupacion
 	 */
	public void setCodigoOcupacion(String codigoOcupacion) {
		this.codigoOcupacion = codigoOcupacion;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoOcupacion
	 * 
	 * @return  Retorna la variable codigoOcupacion
	 */
	public String getCodigoOcupacion() {
		return codigoOcupacion;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoEspecialidad
 	 * 
 	 * @param codigoEspecialidad valor para el atributo codigoEspecialidad
 	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoEspecialidad
	 * 
	 * @return  Retorna la variable codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo nombreEspecialidad
 	 * 
 	 * @param nombreEspecialidad valor para el atributo nombreEspecialidad
 	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo estadoPromocion
 	 * 
 	 * @param estadoPromocion valor para el atributo estadoPromocion
 	 */
	public void setEstadoPromocion(String estadoPromocion) {
		this.estadoPromocion = estadoPromocion;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo estadoPromocion
	 * 
	 * @return  Retorna la variable estadoPromocion
	 */
	public String getEstadoPromocion() {
		return estadoPromocion;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoConvenio
 	 * 
 	 * @param codigoConvenio valor para el atributo codigoConvenio
 	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoConvenio
	 * 
	 * @return  Retorna la variable codigoConvenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo nombreUsuario
 	 * 
 	 * @param nombreUsuario valor para el atributo nombreUsuario
 	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo nombreUsuario
	 * 
	 * @return  Retorna la variable nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo fechaGenInicialF
 	 * 
 	 * @param fechaGenInicialF valor para el atributo fechaGenInicialF
 	 */
	public void setFechaGenInicialF(String fechaGenInicialF) {
		this.fechaGenInicialF = fechaGenInicialF;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaGenInicialF
	 * 
	 * @return  Retorna la variable fechaGenInicialF
	 */
	public String getFechaGenInicialF() {
		this.fechaGenInicialF=UtilidadFecha.conversionFormatoFechaAAp(this.fechaGenInicial);
		return fechaGenInicialF;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo fechaGenFinalF
 	 * 
 	 * @param fechaGenFinalF valor para el atributo fechaGenFinalF
 	 */
	public void setFechaGenFinalF(String fechaGenFinalF) {
		this.fechaGenFinalF = fechaGenFinalF;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo fechaGenFinalF
	 * 
	 * @return  Retorna la variable fechaGenFinalF
	 */
	public String getFechaGenFinalF() {
		this.fechaGenFinalF=UtilidadFecha.conversionFormatoFechaAAp(this.fechaGenFinal);
		return fechaGenFinalF;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo rutaLogo
 	 * 
 	 * @param rutaLogo valor para el atributo rutaLogo
 	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo rutaLogo
	 * 
	 * @return  Retorna la variable rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo ubicacionLogo
 	 * 
 	 * @param ubicacionLogo valor para el atributo ubicacionLogo
 	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo razonSocial
 	 * 
 	 * @param razonSocial valor para el atributo razonSocial
 	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoInstitucion
 	 * 
 	 * @param codigoInstitucion valor para el atributo codigoInstitucion
 	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoInstitucion
	 * 
	 * @return  Retorna la variable codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo codigoPrograma
 	 * 
 	 * @param codigoPrograma valor para el atributo codigoPrograma
 	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
	 *  del atributo codigoPrograma
	 * 
	 * @return  Retorna la variable codigoPrograma
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}
	
	
	/**
 	 * M�todo que se encarga de establecer el valor 
 	 *  del atributo estadoActivoPromocion
 	 * 
 	 * @param estadoActivoPromocion valor para el atributo estadoActivoPromocion
 	 */
	public void setEstadoActivoPromocion(String estadoActivoPromocion) {
		this.estadoActivoPromocion = estadoActivoPromocion;
	}
	
	
	/**
	 * M�todo que se encarga de obtener el valor 
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
