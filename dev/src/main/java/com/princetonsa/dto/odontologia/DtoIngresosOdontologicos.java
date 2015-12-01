package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class DtoIngresosOdontologicos implements Serializable, Comparable<DtoIngresosOdontologicos> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que identifica el ingreso odontol&oacute;gico.
	 */
	private int id;
	
	/**
	 * Atributo que almacena el nombre de un pa&iacute;s
	 */
	private String descripcionPais;
	
	/**
	 * Atributo que almacena el nombre de una ciudad
	 */
	private String descripcionCiudad;
	
	/**
	 * atributo que almacena la descripci&oacute;n de 
	 * una regi&oacute;n de cobertura.
	 */
	private String descripcionRegionCobertura;
	
	/**
	 * Atributo que almacena el nombre de un centro de atenci&oacute;n
	 */
	private String descripcionCentroAtencion;
	
	/**
	 * Atributo que almacena el nombre de la empresa
	 * instituci&oacute;n.
	 */
	private String descripcionEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de un paciente.
	 */
	private int codigoPaciente;

	/**
	 * Atributo que almacena el tipo de identificaci&oacute;n 
	 * de la persona.
	 */
	private String tipoId;
	
	/**
	 * Atributo que almacena el n&uacute;mero de identificaci&oacute;n 
	 * de la persona.
	 */ 
	private String numeroId;
	
	/**
	 * Atributo que almacena el primer nombre de la persona.
	 */
	private String primerNombre;
	
	/**
	 * Atributo que almacena el segundo nombre de la persona.
	 */
	private String segundoNombre;
	
	/**
	 * Atributo que almacena el primer apellido de la persona.
	 */
	private String primerApellido;
	
	/**
	 * Atributo que almacena el segundo apellido de la persona.
	 */
	private String segundoApellido;
	
	/**
	 * Atributo que almacena el estado en el que tiene la cita
	 * una persona de tipo paciente.
	 */
	private String estadoCitaOdonto;
	
	/**
	 * Atributo que almacena el n&uacute;mero de tel&eacute;fono fijo
	 * de la persona.
	 */
	private Integer telefonoFijo;
	
	/**
	 * Atributo que almacena el n&uacute;mero de tel&eacute;fono celular
	 * de la persona.
	 */
	private Long telefonoCelular;
	
	/**
	 * Atributo que almacena  los n&uacute;meros de otros tel&eacute;fonos del paciente.
	 */
	private String telefono;
	
	/**
	 * Atributo que almacena los datos introducidos por el usuario
	 * para realizar el reporte de ingresos odontol&oacute;gicos.
	 */
	private DtoReporteIngresosOdontologicos filtros;
	
	/**
	 * Atributo que almacena el consecutivo del centro de atenci&oacute;n
	 */
	private int consecutivoCentroAtencion;
	
	/**
	 * Atributo que permite mostrar un resultado diferente si el tel&eacute;fono
	 * celular es nulo.
	 */
	private String telefonoCelularString;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la instituci&oacute;n a la que
	 * pertenece el centro de atenci&oacute;n.
	 */
	private Integer codigoInstitucion;
	
	/**
	 * permite definir si la instituci&oacute;n a la que pertenece
	 * un centro de atenci&oacute;n es multiempresa.
	 */
	private String esMultiempresa;
	
	/**
	 * Atributo que almacena el consecutivo del presupuesto
	 * odontol&oacute;gico.
	 */
	private Long consecutivoPresupuesto;
	
	/**
	 * Atributo que almacena la fecha en la cual fue
	 * generado el presupuesto odontol&oacute;gico.
	 */
	private Date fechaGeneracionPresupuesto;
	
	/**
	 * Atributo que almacena el estado del presupuesto odontol&oacute;gico.
	 */
	private String estadoPresupuesto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del presupuesto.
	 */
	private Long codigoPkPresupuesto;
	
	/**
	 * Atributo que almacena el n&uacute;mero de contrato del
	 * presupuesto contratado.
	 */
	private String numeroContrato;
	
	/**
	 * Atributo que almacena el valor del preuspuesto contratado.
	 */
	private String valorPresupuesto;
	
	/**
	 * Atributo que almacena la fecha de generaci&oacute;n 
	 * del presupuesto.
	 */
	private String fechaGeneracionPresup;
	
	/**
	 * Atributo que almacena el n&uacute;mero del presupuesto.
	 */
	private String numeroPresupuesto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del ingreso odontol&oacute;gico.
	 */
	private int idIngreso;
	/**
	 * Atributo que almacena el nombre del estado de la cita odontol&oacute;gica.
	 */
	private String nombreEstadoCita;
	
	/**
	 * Atributo que almacena el tipo y número de identificación
	 * de un paciente. 
	 */
	private String ayudanteNumeroId;
	
	/**
	 * Atributo que almacena el nombre completo del
	 * paciente.
	 */
	private String nombreCompletoPaciente;
	
	/**
	 * Almacena el nombre del estado del presupuesto.
	 */
	private String nombreEstadoPresupuesto;
	
	/**
	 * Almacena si el reporte se genera para pacientes con o sin valoración inicial.
	 */
	private String conValoracion;
	
	/**
	 * Almacena la fecha de nacimiento del paciente.
	 */
	private Date fechaNacimiento;
	
	/**
	 * Almacena la edad del paciente.
	 */
	private String edadPaciente;
	
	/**
	 * Almacena el sexo del paciente.
	 */
	private String sexoPaciente;
	
	/**
	 * Almacena el login del profesional que valoró.
	 */
	private String loginProfesional;
	
	/**
	 * Almacena el nombre del profesional que valoró.
	 */
	private String nombreProfesional;
	
	/**
	 * Indica si los ingresos con presupuesto tienen o no solicitud de dcto.
	 */
	private String conSolicitudDcto;
	
	/**
	 * Atributo que almacena la fecha y hora en la cual ha sido modificada la 
	 * cita odontológica.
	 */
	private String  fechaHoraModifica;
	
	/**
	 * Almacena el número de citas existentes por cada estado.
	 */
	private int numeroCitas;
	
	private int citasAtendidas;
	private int citasReservadas;
	private int citasAsignadas;
	private int citasAreprog;
	private int citasNoasis;
	private int citasNoaten;
	private int canceladas;
	
	private String fechaAtendida;
	private String fechaReservada;
	private String fechaAsignada;
	private String fechaAreprog;
	private String fechaNoasis;
	private String fechaNoaten;
	private String fechaCancelada;
	
	/**
	 * Mètodo constructor de la clase
	 *
	 * @author Yennifer Guerrero
	 */
	public DtoIngresosOdontologicos() {
		
		this.id = ConstantesBD.codigoNuncaValido ;
		this.descripcionPais = "";
		this.descripcionCiudad = "";
		this.descripcionRegionCobertura = "";
		this.descripcionCentroAtencion = "";
		this.descripcionEmpresaInstitucion = "";
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.tipoId = "";
		this.numeroId = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.primerApellido = "";
		this.segundoApellido = "";
		this.estadoCitaOdonto = "";
		this.telefonoFijo = ConstantesBD.codigoNuncaValido;
		this.telefonoCelular = ConstantesBD.codigoNuncaValidoLong;
		this.telefono = "";
		this.filtros = new DtoReporteIngresosOdontologicos();
		this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.telefonoCelularString = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.esMultiempresa = "";
		this.consecutivoPresupuesto = ConstantesBD.codigoNuncaValidoLong;
		this.estadoPresupuesto = "";
		this.codigoPkPresupuesto = ConstantesBD.codigoNuncaValidoLong;
		this.numeroContrato = "";
		this.valorPresupuesto = "";
		this.fechaGeneracionPresup = "";
		this.numeroPresupuesto = "";
		this.idIngreso = ConstantesBD.codigoNuncaValido;
		this.nombreEstadoCita = "";
		this.ayudanteNumeroId = "";
		this.nombreCompletoPaciente = "";
		this.nombreEstadoPresupuesto = "";
		this.conValoracion = "";
		this.edadPaciente = "";
		this.sexoPaciente = "";
		this.loginProfesional = "";
		this.nombreProfesional = "";
		this.conSolicitudDcto = "";
		this.numeroCitas = ConstantesBD.codigoNuncaValido;
		this.fechaHoraModifica = "";
		this.citasAtendidas = 0; 
		this.citasAtendidas = 0;
		this.citasReservadas = 0;
		this.citasAsignadas = 0;
		this.citasAreprog = 0;
		this.citasNoasis = 0;
		this.citasNoaten = 0;
		this.canceladas = 0;
		this.fechaAtendida = "";
		this.fechaReservada = "";
		this.fechaAsignada = "";
		this.fechaAreprog = "";
		this.fechaNoasis = "";
		this.fechaNoaten = "";
		this.fechaCancelada = "";
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo id
	 * 
	 * @return  Retorna la variable id
	 */
	public int getId() {
		return id;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo id
	 * 
	 * @param  valor para el atributo id 
	 */
	public void setId(int id) {
		this.id = id;
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
	 * del atributo descripcionPais
	 * 
	 * @param  valor para el atributo descripcionPais 
	 */
	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionCiudad
	 * 
	 * @param  valor para el atributo descripcionCiudad 
	 */
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
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
	 * del atributo descripcionCentroAtencion
	 * 
	 * @param  valor para el atributo descripcionCentroAtencion 
	 */
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
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
	 * del atributo descripcionEmpresaInstitucion
	 * 
	 * @param  valor para el atributo descripcionEmpresaInstitucion 
	 */
	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPaciente
	 * 
	 * @return  Retorna la variable codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPaciente
	 * 
	 * @param  valor para el atributo codigoPaciente 
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo tipoId
	 * 
	 * @return  Retorna la variable tipoId
	 */
	public String getTipoId() {
		return tipoId;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo numeroId
	 * 
	 * @return  Retorna la variable numeroId
	 */
	public String getNumeroId() {
		return numeroId;
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
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo segundoNombre
	 * 
	 * @return  Retorna la variable segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
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
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo segundoApellido
	 * 
	 * @return  Retorna la variable segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estadoCitaOdonto
	 * 
	 * @return  Retorna la variable estadoCitaOdonto
	 */
	public String getEstadoCitaOdonto() {
		return estadoCitaOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo telefonoFijo
	 * 
	 * @return  Retorna la variable telefonoFijo
	 */
	public Integer getTelefonoFijo() {
		return telefonoFijo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo telefonoCelular
	 * 
	 * @return  Retorna la variable telefonoCelular
	 */
	public Long getTelefonoCelular() {
		return telefonoCelular;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo telefono
	 * 
	 * @return  Retorna la variable telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo filtros
	 * 
	 * @return  Retorna la variable filtros
	 */
	public DtoReporteIngresosOdontologicos getFiltros() {
		return filtros;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo tipoId
	 * 
	 * @param  valor para el atributo tipoId 
	 */
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo numeroId
	 * 
	 * @param  valor para el atributo numeroId 
	 */
	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo primerNombre
	 * 
	 * @param  valor para el atributo primerNombre 
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo segundoNombre
	 * 
	 * @param  valor para el atributo segundoNombre 
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo primerApellido
	 * 
	 * @param  valor para el atributo primerApellido 
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo segundoApellido
	 * 
	 * @param  valor para el atributo segundoApellido 
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estadoCitaOdonto
	 * 
	 * @param  valor para el atributo estadoCitaOdonto 
	 */
	public void setEstadoCitaOdonto(String estadoCitaOdonto) {
		this.estadoCitaOdonto = estadoCitaOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo telefonoFijo
	 * 
	 * @param  valor para el atributo telefonoFijo 
	 */
	public void setTelefonoFijo(Integer telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo telefonoCelular
	 * 
	 * @param  valor para el atributo telefonoCelular 
	 */
	public void setTelefonoCelular(Long telefonoCelular) {
		
		if (telefonoCelular != null && telefonoCelular > 0) {
			this.telefonoCelularString = String.valueOf(telefonoCelular);
		}else{
			this.telefonoCelularString = " - ";
		}
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo telefono
	 * 
	 * @param  valor para el atributo telefono 
	 */
	public void setTelefono(String telefono) {
		
		if (telefonoFijo != null && telefonoFijo > 0 ) {
			this.telefono= String.valueOf(telefonoFijo);
		} else if (!UtilidadTexto.isEmpty(telefono)) {
			this.telefono = telefono;
		} else{
			this.telefono = " - ";
		}
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo filtros
	 * 
	 * @param  valor para el atributo filtros 
	 */
	public void setFiltros(DtoReporteIngresosOdontologicos filtros) {
		this.filtros = filtros;
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
	 * del atributo consecutivoCentroAtencion
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencion 
	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
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
	 * del atributo descripcionRegionCobertura
	 * 
	 * @param  valor para el atributo descripcionRegionCobertura 
	 */
	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}

	@Override
	public int compareTo(DtoIngresosOdontologicos ingresoAComparar) {
		if(this.getConsecutivoCentroAtencion()>ingresoAComparar.getConsecutivoCentroAtencion())
		{
			return 1;
		}else if(this.getConsecutivoCentroAtencion()<ingresoAComparar.getConsecutivoCentroAtencion())
		{
			return -1;
		}
		else return 0;
	}

	public String getTelefonoCelularString() {
		return telefonoCelularString;
	}

	public void setTelefonoCelularString(String telefonoCelularString) {
			this.telefonoCelularString = telefonoCelularString;
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
	 * del atributo codigoInstitucion
	 * 
	 * @param  valor para el atributo codigoInstitucion 
	 */
	public void setCodigoInstitucion(Integer codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
		
		esMultiempresa= ValoresPorDefecto.getInstitucionMultiempresa(this.codigoInstitucion);
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
	 * del atributo esMultiempresa
	 * 
	 * @param  valor para el atributo esMultiempresa 
	 */
	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
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
			nombreEstadoPresupuesto = this.estadoPresupuesto;
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoPresupuesto
	 * 
	 * @return  Retorna la variable consecutivoPresupuesto
	 */
	public Long getConsecutivoPresupuesto() {
		return consecutivoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoPresupuesto
	 * 
	 * @param  valor para el atributo consecutivoPresupuesto 
	 */
	public void setConsecutivoPresupuesto(Long consecutivoPresupuesto) {
		this.consecutivoPresupuesto = consecutivoPresupuesto;
		
		if (consecutivoPresupuesto == null) {
			numeroPresupuesto = " - ";
		} else {
			numeroPresupuesto = String.valueOf(consecutivoPresupuesto);
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaGeneracionPresupuesto
	 * 
	 * @return  Retorna la variable fechaGeneracionPresupuesto
	 */
	public Date getFechaGeneracionPresupuesto() {
		return fechaGeneracionPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaGeneracionPresupuesto
	 * 
	 * @param  valor para el atributo fechaGeneracionPresupuesto 
	 */
	public void setFechaGeneracionPresupuesto(Date fechaGeneracionPresupuesto) {
		this.fechaGeneracionPresupuesto = fechaGeneracionPresupuesto;
		
		if (fechaGeneracionPresupuesto == null) {
			fechaGeneracionPresup = " - ";
		} else {
			fechaGeneracionPresup = String.valueOf(fechaGeneracionPresupuesto);
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPkPresupuesto
	 * 
	 * @return  Retorna la variable codigoPkPresupuesto
	 */
	public Long getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPkPresupuesto
	 * 
	 * @param  valor para el atributo codigoPkPresupuesto 
	 */
	public void setCodigoPkPresupuesto(Long codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo valorPresupuesto
	 * 
	 * @return  Retorna la variable valorPresupuesto
	 */
	public String getValorPresupuesto() {
		return valorPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo valorPresupuesto
	 * 
	 * @param  valor para el atributo valorPresupuesto 
	 */
	public void setValorPresupuesto(String valorPresupuesto) {
		
		
		if (UtilidadTexto.isEmpty(valorPresupuesto )
				|| valorPresupuesto.trim().equals("-")) {
			
			if (estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoEstadoActivo)) {
				this.valorPresupuesto = "0.00";
			}else if ( estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoInactivo)) {
				this.valorPresupuesto = "  ";
			}else{
				this.valorPresupuesto = "  -  ";
			}
			
		} else if ( estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoInactivo)) {
			this.valorPresupuesto = "  ";
		} else {
			this.valorPresupuesto = valorPresupuesto;
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo numeroContrato
	 * 
	 * @return  Retorna la variable numeroContrato
	 */
	public String getNumeroContrato() {
		
		if (UtilidadTexto.isEmpty(numeroContrato)) {
			this.numeroContrato = " - ";
		} else {
			this.numeroContrato =numeroContrato;
		}
		
		return numeroContrato;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo numeroContrato
	 * 
	 * @param  valor para el atributo numeroContrato 
	 */
	public void setNumeroContrato(String numeroContrato) {
		
		if (numeroContrato == null || UtilidadTexto.isEmpty(numeroContrato) ||
				numeroContrato.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
			this.numeroContrato = " - ";
		} else {
			this.numeroContrato =numeroContrato;
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaGeneracionPresup
	 * 
	 * @return  Retorna la variable fechaGeneracionPresup
	 */
	public String getFechaGeneracionPresup() {
		
		this.fechaGeneracionPresup = UtilidadFecha.conversionFormatoFechaAAp(fechaGeneracionPresup);
		
		return fechaGeneracionPresup;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaGeneracionPresup
	 * 
	 * @param  valor para el atributo fechaGeneracionPresup 
	 */
	public void setFechaGeneracionPresup(String fechaGeneracionPresup) {
		this.fechaGeneracionPresup = fechaGeneracionPresup;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo numeroPresupuesto
	 * 
	 * @return  Retorna la variable numeroPresupuesto
	 */
	public String getNumeroPresupuesto() {
		return numeroPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo numeroPresupuesto
	 * 
	 * @param  valor para el atributo numeroPresupuesto 
	 */
	public void setNumeroPresupuesto(String numeroPresupuesto) {
		this.numeroPresupuesto = numeroPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo idIngreso
	 * 
	 * @return  Retorna la variable idIngreso
	 */
	public int getIdIngreso() {
		return idIngreso;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo idIngreso
	 * 
	 * @param  valor para el atributo idIngreso 
	 */
	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEstadoCita
	 * 
	 * @return  Retorna la variable nombreEstadoCita
	 */
	public String getNombreEstadoCita() {
		
		if (!UtilidadTexto.isEmpty(estadoCitaOdonto) && !estadoCitaOdonto.equals("Sin cita")) {
			nombreEstadoCita = ValoresPorDefecto.getIntegridadDominio(estadoCitaOdonto).toString();
		}else {
			
			nombreEstadoCita = estadoCitaOdonto;
		}
		
		return nombreEstadoCita;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreEstadoCita
	 * 
	 * @param  valor para el atributo nombreEstadoCita 
	 */
	public void setNombreEstadoCita(String nombreEstadoCita) {
		this.nombreEstadoCita = nombreEstadoCita;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ayudanteNumeroId
	 * 
	 * @return  Retorna la variable ayudanteNumeroId
	 */
	public String getAyudanteNumeroId() {
		
		this.ayudanteNumeroId = tipoId + " " + numeroId;
		return ayudanteNumeroId;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ayudanteNumeroId
	 * 
	 * @param  valor para el atributo ayudanteNumeroId 
	 */
	public void setAyudanteNumeroId(String ayudanteNumeroId) {
		this.ayudanteNumeroId = ayudanteNumeroId;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreCompletoPaciente
	 * 
	 * @return  Retorna la variable nombreCompletoPaciente
	 */
	public String getNombreCompletoPaciente() {
		
		if (segundoNombre == null) {
			segundoNombre = "";
		}
		if (segundoApellido == null) {
			segundoApellido = "";
		}
		
		this.nombreCompletoPaciente = primerNombre + " " + segundoNombre + " " +
			primerApellido + " " + segundoApellido; 
		
		return nombreCompletoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreCompletoPaciente
	 * 
	 * @param  valor para el atributo nombreCompletoPaciente 
	 */
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEstadoPresupuesto
	 * 
	 * @return  Retorna la variable nombreEstadoPresupuesto
	 */
	public String getNombreEstadoPresupuesto() {
		
		this.nombreEstadoPresupuesto = this.estadoPresupuesto;
		
		if (nombreEstadoPresupuesto != null && 
				!nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoContratado) &&
				!nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente) &&
				!nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoTerminado) &&
				!nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoContratadoCancelado) && 
				!nombreEstadoPresupuesto.trim().equals(ConstantesIntegridadDominio.acronimoPrecontratado) &&
				!nombreEstadoPresupuesto.trim().equals(ConstantesBD.codigoNuncaValido + "")) {
			
			nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(nombreEstadoPresupuesto).toString();
			
		}else if (nombreEstadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoPrecontratado)) {
			if (this.conSolicitudDcto.equals(ConstantesBD.acronimoNo)) {
				this.nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(nombreEstadoPresupuesto).toString();
				this.nombreEstadoPresupuesto += " " + "Sin Solicitud Descuento";
			}else if (this.conSolicitudDcto.equals(ConstantesBD.acronimoSi)) {
				this.nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(nombreEstadoPresupuesto).toString();
				this.nombreEstadoPresupuesto += " " + "Con Solicitud Descuento";
			}else{
				nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(nombreEstadoPresupuesto).toString();
			}
			
		}else if (UtilidadTexto.isEmpty(nombreEstadoPresupuesto) || nombreEstadoPresupuesto.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
			this.nombreEstadoPresupuesto = "Sin Presupuesto";
		}else{
			nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(nombreEstadoPresupuesto).toString();
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
	 * Método que se encarga de obtener el 
	 * valor del atributo  conValoracion
	 *
	 * @return retorna la variable conValoracion
	 */
	public String getConValoracion() {
		return conValoracion;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo conValoracion
	 * @param conValoracion es el valor para el atributo conValoracion 
	 */
	public void setConValoracion(String conValoracion) {
		this.conValoracion = conValoracion;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaNacimiento
	 *
	 * @return retorna la variable fechaNacimiento
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaNacimiento
	 * @param fechaNacimiento es el valor para el atributo fechaNacimiento 
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		
		String fechaNcto= this.fechaNacimiento.toString();
		
		String [] fecha = fechaNcto.split("-");
		fechaNcto= fecha [2] + "/" + fecha [1] + "/" + fecha [0];
		
		
		if (fechaNacimiento != null) {
			this.edadPaciente = String.valueOf(UtilidadFecha.calcularEdad(fechaNcto));
		}
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  edadPaciente
	 *
	 * @return retorna la variable edadPaciente
	 */
	public String getEdadPaciente() {
		return edadPaciente;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo edadPaciente
	 * @param edadPaciente es el valor para el atributo edadPaciente 
	 */
	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  sexoPaciente
	 *
	 * @return retorna la variable sexoPaciente
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo sexoPaciente
	 * @param sexoPaciente es el valor para el atributo sexoPaciente 
	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente = sexoPaciente;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  loginProfesional
	 *
	 * @return retorna la variable loginProfesional
	 */
	public String getLoginProfesional() {
		return loginProfesional;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo loginProfesional
	 * @param loginProfesional es el valor para el atributo loginProfesional 
	 */
	public void setLoginProfesional(String loginProfesional) {
		this.loginProfesional = loginProfesional;
		if (!UtilidadTexto.isEmpty(this.loginProfesional)) {
			this.nombreProfesional = Utilidades.obtenerNombreUsuarioXLogin(loginProfesional, false);
		}
	
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  nombreProfesional
	 *
	 * @return retorna la variable nombreProfesional
	 */
	public String getNombreProfesional() {
		return nombreProfesional;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo nombreProfesional
	 * @param nombreProfesional es el valor para el atributo nombreProfesional 
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  conSolicitudDcto
	 *
	 * @return retorna la variable conSolicitudDcto
	 */
	public String getConSolicitudDcto() {
		return conSolicitudDcto;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo conSolicitudDcto
	 * @param conSolicitudDcto es el valor para el atributo conSolicitudDcto 
	 */
	public void setConSolicitudDcto(String conSolicitudDcto) {
		this.conSolicitudDcto = conSolicitudDcto;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  numeroCitas
	 *
	 * @return retorna la variable numeroCitas
	 */
	public int getNumeroCitas() {
		return numeroCitas;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo numeroCitas
	 * @param numeroCitas es el valor para el atributo numeroCitas 
	 */
	public void setNumeroCitas(int numeroCitas) {
		this.numeroCitas = numeroCitas;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaHoraModifica
	 *
	 * @return retorna la variable fechaHoraModifica
	 */
	public String getFechaHoraModifica() {
		return fechaHoraModifica;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaHoraModifica
	 * @param fechaHoraModifica es el valor para el atributo fechaHoraModifica 
	 */
	public void setFechaHoraModifica(String fechaHoraModifica) {
		this.fechaHoraModifica = fechaHoraModifica;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  citasAtendidas
	 *
	 * @return retorna la variable citasAtendidas
	 */
	public int getCitasAtendidas() {
		return citasAtendidas;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo citasAtendidas
	 * @param citasAtendidas es el valor para el atributo citasAtendidas 
	 */
	public void setCitasAtendidas(int citasAtendidas) {
		this.citasAtendidas = citasAtendidas;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  citasReservadas
	 *
	 * @return retorna la variable citasReservadas
	 */
	public int getCitasReservadas() {
		return citasReservadas;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo citasReservadas
	 * @param citasReservadas es el valor para el atributo citasReservadas 
	 */
	public void setCitasReservadas(int citasReservadas) {
		this.citasReservadas = citasReservadas;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  citasAsignadas
	 *
	 * @return retorna la variable citasAsignadas
	 */
	public int getCitasAsignadas() {
		return citasAsignadas;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo citasAsignadas
	 * @param citasAsignadas es el valor para el atributo citasAsignadas 
	 */
	public void setCitasAsignadas(int citasAsignadas) {
		this.citasAsignadas = citasAsignadas;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  citasAreprog
	 *
	 * @return retorna la variable citasAreprog
	 */
	public int getCitasAreprog() {
		return citasAreprog;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo citasAreprog
	 * @param citasAreprog es el valor para el atributo citasAreprog 
	 */
	public void setCitasAreprog(int citasAreprog) {
		this.citasAreprog = citasAreprog;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  citasNoasis
	 *
	 * @return retorna la variable citasNoasis
	 */
	public int getCitasNoasis() {
		return citasNoasis;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo citasNoasis
	 * @param citasNoasis es el valor para el atributo citasNoasis 
	 */
	public void setCitasNoasis(int citasNoasis) {
		this.citasNoasis = citasNoasis;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  citasNoaten
	 *
	 * @return retorna la variable citasNoaten
	 */
	public int getCitasNoaten() {
		return citasNoaten;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo citasNoaten
	 * @param citasNoaten es el valor para el atributo citasNoaten 
	 */
	public void setCitasNoaten(int citasNoaten) {
		this.citasNoaten = citasNoaten;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  canceladas
	 *
	 * @return retorna la variable canceladas
	 */
	public int getCanceladas() {
		return canceladas;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo canceladas
	 * @param canceladas es el valor para el atributo canceladas 
	 */
	public void setCanceladas(int canceladas) {
		this.canceladas = canceladas;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaAtendida
	 *
	 * @return retorna la variable fechaAtendida
	 */
	public String getFechaAtendida() {
		return fechaAtendida;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaAtendida
	 * @param fechaAtendida es el valor para el atributo fechaAtendida 
	 */
	public void setFechaAtendida(String fechaAtendida) {
		this.fechaAtendida = fechaAtendida;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaReservada
	 *
	 * @return retorna la variable fechaReservada
	 */
	public String getFechaReservada() {
		return fechaReservada;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaReservada
	 * @param fechaReservada es el valor para el atributo fechaReservada 
	 */
	public void setFechaReservada(String fechaReservada) {
		this.fechaReservada = fechaReservada;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaAsignada
	 *
	 * @return retorna la variable fechaAsignada
	 */
	public String getFechaAsignada() {
		return fechaAsignada;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaAsignada
	 * @param fechaAsignada es el valor para el atributo fechaAsignada 
	 */
	public void setFechaAsignada(String fechaAsignada) {
		this.fechaAsignada = fechaAsignada;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaAreprog
	 *
	 * @return retorna la variable fechaAreprog
	 */
	public String getFechaAreprog() {
		return fechaAreprog;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaAreprog
	 * @param fechaAreprog es el valor para el atributo fechaAreprog 
	 */
	public void setFechaAreprog(String fechaAreprog) {
		this.fechaAreprog = fechaAreprog;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaNoasis
	 *
	 * @return retorna la variable fechaNoasis
	 */
	public String getFechaNoasis() {
		return fechaNoasis;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaNoasis
	 * @param fechaNoasis es el valor para el atributo fechaNoasis 
	 */
	public void setFechaNoasis(String fechaNoasis) {
		this.fechaNoasis = fechaNoasis;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaNoaten
	 *
	 * @return retorna la variable fechaNoaten
	 */
	public String getFechaNoaten() {
		return fechaNoaten;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaNoaten
	 * @param fechaNoaten es el valor para el atributo fechaNoaten 
	 */
	public void setFechaNoaten(String fechaNoaten) {
		this.fechaNoaten = fechaNoaten;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaCancelada
	 *
	 * @return retorna la variable fechaCancelada
	 */
	public String getFechaCancelada() {
		return fechaCancelada;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaCancelada
	 * @param fechaCancelada es el valor para el atributo fechaCancelada 
	 */
	public void setFechaCancelada(String fechaCancelada) {
		this.fechaCancelada = fechaCancelada;
	}
}
