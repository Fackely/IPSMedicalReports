package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import util.InfoDatosStr;
import util.UtilidadTexto;

public class DtoReporteConsultaPacienteEstadoPresupuesto implements Serializable{

	private static final long serialVersionUID = 1L;
	private static String SEXO_PACIENTE = "Femenino - Masculino";
	
	/**
	 * Atributo que almacena la fecha inicial desde la cual se realiza la 
	 * búsqueda de las solicitudes.
	 */
	private Date fechaInicial;
	
	/**
	 * Atributo que almacena la fecha final desde la cual se realiza la 
	 * búsqueda de las solicitudes
	 */
	private Date fechaFinal;
	
	/**
	 * Atributo que almacena el código que identifica a 
	 * una determinada ciudad.
	 */
	private String ciudadDeptoPais;
	
	/**
	 * Atributo que almacena el código de la región de cobertura.
	 */
	private long codigoRegion;
	
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
	 * Atributo que almacena el código del centro de 
	 * atención.
	 */
	private Integer consecutivoCentroAtencion;
	
	/**
	 * Edad inicial del paciente desde la cual se desea 
	 * realizar la búsqueda.
	 */
	private Integer edadInicial;
	
	/**
	 * Edad final del paciente desde la cual se desea 
	 * realizar la búsqueda.
	 */
	private Integer edadFinal;
	
	/**
	 * Atributo que almacena el sexo del paciente.
	 */
	private String sexoPaciente;	
	
	/**
	 * Atributo que almacena el estado del presupuesto
	 * odontológico.
	 */
	private String estadoPresupuesto;
	
	/**
	 * Atributo que almacena el indicativo de contrato
	 * de un presupuesto en estado contratado.
	 */
	private String indicativoContrato;
	
	/**
	 * Atributo que almacena el c&oacute;digo de
	 * una especialidad odontológica.
	 */
	private int codigoEspecialidad;
	
	/**
	 * Atributo que almacena el código del paquete
	 * odontológico.
	 */
	private int codigoPaqueteOdonto;	
	
	/**
	 * Atributo que almacena el código del programa seleccionado.
	 */
	private long codigoPrograma;	
	
	/**
	 * Atributo que permite obtener el servicio por medio de
	 *  la búsqueda genérica. 
	 */
	private InfoDatosStr servicio;
	
	/**
	 * Atributo que indica si el resultado de la b&uacute;squeda 
	 * incluir&aacute; presupuestos con solicitudes de descuento o no.
	 */
	private String conSolicitudDcto;
	
	private String nombreSexo;
	private String nombreInstitucion;
	private String nombrePrograma;
	private String nombrePaquete;
	private String nombreCentroAtencion;
	private String nombreCiudad;
	private String nombrePais;
	private String nombreRegion;
	private String nombreEstadoPresupuesto;
	private long codigoPresupuesto;
	private int codigoCentroAtencion;
	private long codigoInstitucion;
	private long codigoSolicitudDescuento;
	private String etiquetaEdadPaciente;
	private String usuarioProceso;
	private String razonSocial;
	private String rutaLogo;
	private String ubicacionLogo;
	private boolean esMultiempresa;
	
	/**
	 * Atributo que almacena el total del presupuesto
	 * por estado.
	 */
	private Integer totalPresupuestoPorEstado;
	

	public Integer getTotalPresupuestoPorEstado() {
		return totalPresupuestoPorEstado;
	}

	public void setTotalPresupuestoPorEstado(Integer totalPresupuestoPorEstado) {
		this.totalPresupuestoPorEstado = totalPresupuestoPorEstado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaInicial
	
	 * @return retorna la variable fechaInicial 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaInicial
	
	 * @param valor para el atributo fechaInicial 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaFinal
	
	 * @return retorna la variable fechaFinal 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaFinal
	
	 * @param valor para el atributo fechaFinal 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ciudadDeptoPais
	
	 * @return retorna la variable ciudadDeptoPais 
	 * @author Angela Maria Aguirre 
	 */
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ciudadDeptoPais
	
	 * @param valor para el atributo ciudadDeptoPais 
	 * @author Angela Maria Aguirre 
	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoRegion
	
	 * @return retorna la variable codigoRegion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoRegion() {
		return codigoRegion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoRegion
	
	 * @param valor para el atributo codigoRegion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoRegion(long codigoRegion) {
		this.codigoRegion = codigoRegion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoEmpresaInstitucion
	
	 * @return retorna la variable codigoEmpresaInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoEmpresaInstitucion
	
	 * @param valor para el atributo codigoEmpresaInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoCiudad
	
	 * @return retorna la variable codigoCiudad 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoCiudad
	
	 * @param valor para el atributo codigoCiudad 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPais
	
	 * @return retorna la variable codigoPais 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPais
	
	 * @param valor para el atributo codigoPais 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoDpto
	
	 * @return retorna la variable codigoDpto 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigoDpto() {
		return codigoDpto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoDpto
	
	 * @param valor para el atributo codigoDpto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivoCentroAtencion
	
	 * @return retorna la variable consecutivoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoCentroAtencion
	
	 * @param valor para el atributo consecutivoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo edadInicial
	
	 * @return retorna la variable edadInicial 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getEdadInicial() {
		return edadInicial;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo edadInicial
	
	 * @param valor para el atributo edadInicial 
	 * @author Angela Maria Aguirre 
	 */
	public void setEdadInicial(Integer edadInicial) {
		this.edadInicial = edadInicial;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo edadFinal
	
	 * @return retorna la variable edadFinal 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getEdadFinal() {
		return edadFinal;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo edadFinal
	
	 * @param valor para el atributo edadFinal 
	 * @author Angela Maria Aguirre 
	 */
	public void setEdadFinal(Integer edadFinal) {
		this.edadFinal = edadFinal;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo sexoPaciente
	
	 * @return retorna la variable sexoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo sexoPaciente
	
	 * @param valor para el atributo sexoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setSexoPaciente(String sexoPaciente) {				
		this.sexoPaciente = sexoPaciente;
		if(UtilidadTexto.isEmpty(sexoPaciente)){
			this.nombreSexo=SEXO_PACIENTE;
		}
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estadoPresupuesto
	
	 * @return retorna la variable estadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estadoPresupuesto
	
	 * @param valor para el atributo estadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indicativoContrato
	
	 * @return retorna la variable indicativoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public String getIndicativoContrato() {
		return indicativoContrato;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indicativoContrato
	
	 * @param valor para el atributo indicativoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndicativoContrato(String indicativoContrato) {
		this.indicativoContrato = indicativoContrato;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoEspecialidad
	
	 * @return retorna la variable codigoEspecialidad 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoEspecialidad
	
	 * @param valor para el atributo codigoEspecialidad 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPaqueteOdonto
	
	 * @return retorna la variable codigoPaqueteOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoPaqueteOdonto() {
		return codigoPaqueteOdonto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPaqueteOdonto
	
	 * @param valor para el atributo codigoPaqueteOdonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPaqueteOdonto(int codigoPaqueteOdonto) {
		this.codigoPaqueteOdonto = codigoPaqueteOdonto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPrograma
	
	 * @return retorna la variable codigoPrograma 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPrograma
	
	 * @param valor para el atributo codigoPrograma 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo servicio
	
	 * @return retorna la variable servicio 
	 * @author Angela Maria Aguirre 
	 */
	public InfoDatosStr getServicio() {
		return servicio;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo servicio
	
	 * @param valor para el atributo servicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setServicio(InfoDatosStr servicio) {
		this.servicio = servicio;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo conSolicitudDcto
	
	 * @return retorna la variable conSolicitudDcto 
	 * @author Angela Maria Aguirre 
	 */
	public String getConSolicitudDcto() {
		return conSolicitudDcto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo conSolicitudDcto
	
	 * @param valor para el atributo conSolicitudDcto 
	 * @author Angela Maria Aguirre 
	 */
	public void setConSolicitudDcto(String conSolicitudDcto) {
		this.conSolicitudDcto = conSolicitudDcto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreSexo
	
	 * @return retorna la variable nombreSexo 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreSexo() {
		return nombreSexo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreSexo
	
	 * @param valor para el atributo nombreSexo 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreSexo(String nombreSexo) {
		this.nombreSexo = nombreSexo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreInstitucion
	
	 * @return retorna la variable nombreInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreInstitucion
	
	 * @param valor para el atributo nombreInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombrePrograma
	
	 * @return retorna la variable nombrePrograma 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombrePrograma
	
	 * @param valor para el atributo nombrePrograma 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombrePaquete
	
	 * @return retorna la variable nombrePaquete 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombrePaquete() {
		return nombrePaquete;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombrePaquete
	
	 * @param valor para el atributo nombrePaquete 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreCentroAtencion
	
	 * @return retorna la variable nombreCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCentroAtencion
	
	 * @param valor para el atributo nombreCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreCiudad
	
	 * @return retorna la variable nombreCiudad 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreCiudad() {
		return nombreCiudad;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCiudad
	
	 * @param valor para el atributo nombreCiudad 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreCiudad(String nombreCiudad) {
		this.nombreCiudad = nombreCiudad;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombrePais
	
	 * @return retorna la variable nombrePais 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombrePais() {
		return nombrePais;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombrePais
	
	 * @param valor para el atributo nombrePais 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreRegion
	
	 * @return retorna la variable nombreRegion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreRegion() {
		return nombreRegion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreRegion
	
	 * @param valor para el atributo nombreRegion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreRegion(String nombreRegion) {
		this.nombreRegion = nombreRegion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreEstadoPresupuesto
	
	 * @return retorna la variable nombreEstadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreEstadoPresupuesto() {
		return nombreEstadoPresupuesto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreEstadoPresupuesto
	
	 * @param valor para el atributo nombreEstadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreEstadoPresupuesto(String nombreEstadoPresupuesto) {
		this.nombreEstadoPresupuesto = nombreEstadoPresupuesto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPresupuesto
	
	 * @return retorna la variable codigoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPresupuesto
	
	 * @param valor para el atributo codigoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPresupuesto(long codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoCentroAtencion
	
	 * @return retorna la variable codigoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoCentroAtencion
	
	 * @param valor para el atributo codigoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoInstitucion
	
	 * @return retorna la variable codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	
	 * @param valor para el atributo codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoInstitucion(long codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoSolicitudDescuento
	
	 * @return retorna la variable codigoSolicitudDescuento 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoSolicitudDescuento() {
		return codigoSolicitudDescuento;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoSolicitudDescuento
	
	 * @param valor para el atributo codigoSolicitudDescuento 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoSolicitudDescuento(long codigoSolicitudDescuento) {
		this.codigoSolicitudDescuento = codigoSolicitudDescuento;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo etiquetaEdadPaciente
	
	 * @return retorna la variable etiquetaEdadPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getEtiquetaEdadPaciente() {
		return etiquetaEdadPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo etiquetaEdadPaciente
	
	 * @param valor para el atributo etiquetaEdadPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setEtiquetaEdadPaciente(String etiquetaEdadPaciente) {
		this.etiquetaEdadPaciente = etiquetaEdadPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo usuarioProceso
	
	 * @return retorna la variable usuarioProceso 
	 * @author Angela Maria Aguirre 
	 */
	public String getUsuarioProceso() {
		return usuarioProceso;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo usuarioProceso
	
	 * @param valor para el atributo usuarioProceso 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo razonSocial
	
	 * @return retorna la variable razonSocial 
	 * @author Angela Maria Aguirre 
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo razonSocial
	
	 * @param valor para el atributo razonSocial 
	 * @author Angela Maria Aguirre 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo rutaLogo
	
	 * @return retorna la variable rutaLogo 
	 * @author Angela Maria Aguirre 
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo rutaLogo
	
	 * @param valor para el atributo rutaLogo 
	 * @author Angela Maria Aguirre 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ubicacionLogo
	
	 * @return retorna la variable ubicacionLogo 
	 * @author Angela Maria Aguirre 
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	
	 * @param valor para el atributo ubicacionLogo 
	 * @author Angela Maria Aguirre 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esMultiempresa
	
	 * @return retorna la variable esMultiempresa 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isEsMultiempresa() {
		return esMultiempresa;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esMultiempresa
	
	 * @param valor para el atributo esMultiempresa 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsMultiempresa(boolean esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}	
	
}
