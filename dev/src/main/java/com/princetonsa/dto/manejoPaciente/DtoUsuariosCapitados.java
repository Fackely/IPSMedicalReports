package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.servinte.axioma.dto.capitacion.CargueDto;

/**
 * Esta clase se encarga de contener los datos de los usuarios Capitados
 * 
 * @author Cristhian Murillo
 */
public class DtoUsuariosCapitados implements Serializable 
{  
	
	/** Serial**/
	private static final long serialVersionUID = 1L;
	
	
	// ---------- SECCION PACIENTE ---------- //
	/** Código del convenio **/
	private String convenio;
	/** Código  del convenio en formato int **/
	private Integer convenioInt;
	/** nombre del convenio **/
	private String nombreConvenio;
	/** Tipo de Afiliado o Tipo de Usuario **/
	private Character tipoAfiliado;
	/** Tipo de identificación **/
	private String tipoIdentificacion;
	/** número identificación **/
	private String numeroIdentificacion;
	/** primer nombre **/
	private String primerNombre;
	/** segundo nombre **/
	private String segundoNombre;
	/** primer apellido **/
	private String primerApellido;
	/** segundo apellido **/
	private String segundoApellido;
	/** Estado Ingreso **/
	private String estadoIngreso;
	/** Fecha Nacimiento **/
	private Date fechaNacimiento;
	/** Dirección **/
	private String direccion;
	/** Código del paciente/persona **/
	private Integer codigoPaciente;
	/** Nombre tipo afiliado **/
	private String nombreTipoAfiliado;
	/** Lista de las Autorizaciones de Ingreso Estancia del usuario  */
	private ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstancia;
	/** Nombre del tipo de contrato */
	private String nombreTipoContrato; 
	/** Nombre/Descripcion del estrato social **/
	private String descripcionEstratoSocial;
	/** Código del estrato social **/
	private Integer codigoEstratoSocial;
	/** Código  del contrato**/
	private Integer codigoContrato;
	/** Id  de la cuenta **/
	private Integer idCuenta;  
	/** Tipo de cobro del paciente (subcuenta) **/
	private String tipoCobroPaciente;
	/** Porcentaje del Monto (subcuenta) **/
	private BigDecimal porcentajeMontoCobro;
	/** Código del detalle del monto (detalleMonto) **/
	private Integer detalleCodigo;
	/** Tipo de monto de cobro (subcuenta) **/
	private String tipoMontoCobro;
	/** Fecha vigencia Cargue **/
	private Date fechaVigenciaCargue;
	
	// ---------- DATOS CARGUE ---------- //
	private Date fechaInicial;
	private Date fechaFinal;
	private Long codigoTipoCargue;
	private Long codigoUsuarioCapitado;
	
	
	// ---------- SECCION ADMISIÓN ---------- //
	/** Para el manejo d ela busqueda de diagnosticos  */
	private DtoDiagnosticoBusqueda dtoDiagnosticoBusqueda;
	/** Datos para registrar la Admisión  */
	private DtoIngresoEstancia datosAdmision;
	
	
	// ---------- SECCION AUTORIZACIÓN ---------- //
	/** * Código de la Entidad subcontratada seleccionada  */
	private Long selectEntidadSubcontratada;
	/** * Datos Entidad Subcontratada = Otra */
	private String descripcionEntidadSubOtra;
	private String telefonoEntidadSubOtra;
	private String direccionEntidadSubOtra;
	/** Autorizacion Ingreso Estancia Seleccionada/Principal  */
	private DTOAdministracionAutorizacion autorizacionIngresoEstancia;
	/** Indica si se maneja recobro o no * */
	private boolean manejaRecobro;
	/** Descripcion Entidad a Recobrar*/
	private String descripcionOtraEntidadRecobrar;
	/** * Código del convenio (Entidad de recobro)  seleccionado  */
	private Integer selectConvenioEntidadRecobro;
	/**Atributo que permite indicar si el convenio maneja presupuesto capitación */
	private Character manejaPresupuestoCapit;
	
	// -- PARAMETROS DE BUSQUEDA -----
	/** *  Define si se debe buscar por coincidencia exacta o con un like*/
	private boolean buscarPorLike;
	
	//---------------SECCION SUBIR PACIENTE--------------------------//
	private String barrio;
	private Integer codigoBarrio;
	private String localidad; 
	private String ciudad;
	private String departamento;
	private String pais;
	private String centroAtencion;
	private Integer centroAtencionConsecutivo;
	private String tipoIdEmpleador;
	private String numIdEmpleador;
	private String razonSociEmpleador;
	private String tipoIdCotizante;
	private String numIdCotizante;
	private String nombresCotizante;
	private String apellidosCotizante;
	private String parentesco;
	private String excepcionMonto;
	private String sexo;
	private Integer codigoSexo;
	private String telefono;	
	private String email;
	private String numeroFicha;
	private Integer naturaleza;
	
	/**
	 * Grupo familiar que tiene el usuario
	 */
	private List<DtoUsuariosCapitados>grupoFamiliar=new ArrayList<DtoUsuariosCapitados>(0);

	/**
	 * Cargue actual que tiene el usuario
	 */
	private CargueDto cargueActualDto= new CargueDto();
	
	/**
	 *  Historico de cargues en el que el usuario ha estado involucrado 
	 */
	private List<CargueDto>historicoCargues= new ArrayList<CargueDto>(0);
	
	
	/**
	 * Fecha en que se genera el cargue
	 */
	private Date fechaCargue;
	
	/**
	 * Indica si el cargue se encuentra activo o inactivo en el sistema.
	 */
	private String estadoCargue;
	
	/**
	 * 
	 * */
	private boolean esIngresado;
	/**
	 * constructor
	 */
	public DtoUsuariosCapitados() {
		this.convenio							= null;
		this.convenioInt						= null;
		this.nombreConvenio						= "";
		this.tipoAfiliado						= null;
		this.tipoIdentificacion					= "";;
		this.numeroIdentificacion				= "";
		this.primerNombre						= "";
		this.segundoNombre						= "";
		this.primerApellido						= "";
		this.segundoApellido					= "";
		this.estadoIngreso						= "";
		this.fechaNacimiento					= null;
		this.direccion							= "";
		this.codigoPaciente						= null;
		this.nombreTipoAfiliado					= "";
		this.listaAutorizacionesIngresoEstancia = new ArrayList<DTOAdministracionAutorizacion>();
		this.nombreTipoContrato					= "";
		this.descripcionEstratoSocial 			= "";
		this.codigoEstratoSocial				= null;
		this.codigoContrato						= null;
		this.idCuenta							= null;
		this.tipoCobroPaciente					= null;
		this.porcentajeMontoCobro				= null;
		this.detalleCodigo						= null;
		this.tipoMontoCobro						= null;
		this.autorizacionIngresoEstancia		= new DTOAdministracionAutorizacion();
		this.dtoDiagnosticoBusqueda				= new DtoDiagnosticoBusqueda();
		this.fechaInicial						= null;
		this.fechaFinal							= null;
		this.codigoTipoCargue					= null;
		this.codigoUsuarioCapitado				= null;
		this.datosAdmision						= new DtoIngresoEstancia();
		this.manejaRecobro						= false;
		this.descripcionOtraEntidadRecobrar		= "";
		this.selectConvenioEntidadRecobro		= null;
		this.buscarPorLike						= true;
		
		this.barrio="";
		this.codigoBarrio=null;
		this.localidad=""; 
		this.ciudad="";
		this.departamento="";
		this.pais="";
		this.centroAtencion="";
		this.centroAtencionConsecutivo=null;
		this.tipoIdEmpleador="";
		this.numIdEmpleador="";
		this.razonSociEmpleador="";
		this.tipoIdCotizante="";
		this.numIdCotizante="";
		this.nombresCotizante="";
		this.apellidosCotizante="";
		this.parentesco="";
		this.excepcionMonto="";
		this.sexo="";	
		this.codigoSexo=null;
		this.telefono="";
		this.setEmail("");
		this.numeroFicha="";
		this.fechaVigenciaCargue = null;
		this.setNaturaleza(null);
		this.manejaPresupuestoCapit = ConstantesBD.acronimoNoChar;
	}
	
	/** * Reset de la forma  */
	public void resetEntidadSubOtra()
	{		
		this.descripcionEntidadSubOtra		= "";
		this.telefonoEntidadSubOtra			= "";
		this.direccionEntidadSubOtra		= "";
	}
	
	

	public String getConvenio() {
		return convenio;
	}


	public void setConvenio(String convenio) {
		this.convenio = convenio;
		if(!UtilidadTexto.isEmpty(convenio)){
			this.convenioInt = Integer.parseInt(convenio);
		}
	}


	public Character getTipoAfiliado() {
		return tipoAfiliado;
	}


	public void setTipoAfiliado(Character tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}


	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}


	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}


	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}


	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public String getSegundoApellido() {
		return segundoApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	public String getEstadoIngreso() {
		return estadoIngreso;
	}


	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}


	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public Integer getConvenioInt() {
		return convenioInt;
	}


	public void setConvenioInt(Integer convenioInt) {
		this.convenioInt = convenioInt;
		this.convenio = convenioInt+"";
	}


	public Integer getCodigoPaciente() {
		return codigoPaciente;
	}


	public void setCodigoPaciente(Integer codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	public ArrayList<DTOAdministracionAutorizacion> getListaAutorizacionesIngresoEstancia() {
		return listaAutorizacionesIngresoEstancia;
	}


	public void setListaAutorizacionesIngresoEstancia(
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstancia) {
		this.listaAutorizacionesIngresoEstancia = listaAutorizacionesIngresoEstancia;
	}


	public String getNombreConvenio() {
		return nombreConvenio;
	}


	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}


	public String getNombreTipoContrato() {
		return nombreTipoContrato;
	}


	public void setNombreTipoContrato(String nombreTipoContrato) {
		this.nombreTipoContrato = nombreTipoContrato;
	}


	public String getNombreTipoAfiliado() {
		return nombreTipoAfiliado;
	}


	public void setNombreTipoAfiliado(String nombreTipoAfiliado) {
		this.nombreTipoAfiliado = nombreTipoAfiliado;
	}


	public String getDescripcionEstratoSocial() {
		return descripcionEstratoSocial;
	}


	public void setDescripcionEstratoSocial(String descripcionEstratoSocial) {
		this.descripcionEstratoSocial = descripcionEstratoSocial;
	}


	public DTOAdministracionAutorizacion getAutorizacionIngresoEstancia() {
		return autorizacionIngresoEstancia;
	}


	public void setAutorizacionIngresoEstancia(
			DTOAdministracionAutorizacion autorizacionIngresoEstancia) {
		this.autorizacionIngresoEstancia = autorizacionIngresoEstancia;
	}


	public DtoDiagnosticoBusqueda getDtoDiagnosticoBusqueda() {
		return dtoDiagnosticoBusqueda;
	}


	public void setDtoDiagnosticoBusqueda(
			DtoDiagnosticoBusqueda dtoDiagnosticoBusqueda) {
		this.dtoDiagnosticoBusqueda = dtoDiagnosticoBusqueda;
	}


	public DtoIngresoEstancia getDatosAdmision() {
		return datosAdmision;
	}


	public void setDatosAdmision(DtoIngresoEstancia datosAdmision) {
		this.datosAdmision = datosAdmision;
	}


	public Long getSelectEntidadSubcontratada() {
		return selectEntidadSubcontratada;
	}


	public void setSelectEntidadSubcontratada(Long selectEntidadSubcontratada) {
		this.selectEntidadSubcontratada = selectEntidadSubcontratada;
	}


	public String getDescripcionEntidadSubOtra() {
		return descripcionEntidadSubOtra;
	}


	public void setDescripcionEntidadSubOtra(String descripcionEntidadSubOtra) {
		this.descripcionEntidadSubOtra = descripcionEntidadSubOtra;
	}


	public String getTelefonoEntidadSubOtra() {
		return telefonoEntidadSubOtra;
	}


	public void setTelefonoEntidadSubOtra(String telefonoEntidadSubOtra) {
		this.telefonoEntidadSubOtra = telefonoEntidadSubOtra;
	}


	public String getDireccionEntidadSubOtra() {
		return direccionEntidadSubOtra;
	}


	public void setDireccionEntidadSubOtra(String direccionEntidadSubOtra) {
		this.direccionEntidadSubOtra = direccionEntidadSubOtra;
	}


	public boolean isManejaRecobro() {
		return manejaRecobro;
	}


	public void setManejaRecobro(boolean manejaRecobro) {
		this.manejaRecobro = manejaRecobro;
	}


	public String getDescripcionOtraEntidadRecobrar() {
		return descripcionOtraEntidadRecobrar;
	}


	public void setDescripcionOtraEntidadRecobrar(
			String descripcionOtraEntidadRecobrar) {
		this.descripcionOtraEntidadRecobrar = descripcionOtraEntidadRecobrar;
	}


	public Integer getSelectConvenioEntidadRecobro() {
		return selectConvenioEntidadRecobro;
	}


	public void setSelectConvenioEntidadRecobro(Integer selectConvenioEntidadRecobro) {
		this.selectConvenioEntidadRecobro = selectConvenioEntidadRecobro;
	}


	public Integer getCodigoEstratoSocial() {
		return codigoEstratoSocial;
	}


	public void setCodigoEstratoSocial(Integer codigoEstratoSocial) {
		this.codigoEstratoSocial = codigoEstratoSocial;
	}


	public Date getFechaInicial() {
		return fechaInicial;
	}


	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	public Date getFechaFinal() {
		return fechaFinal;
	}


	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public Long getCodigoTipoCargue() {
		return codigoTipoCargue;
	}


	public void setCodigoTipoCargue(Long codigoTipoCargue) {
		this.codigoTipoCargue = codigoTipoCargue;
	}


	public Long getCodigoUsuarioCapitado() {
		return codigoUsuarioCapitado;
	}


	public void setCodigoUsuarioCapitado(Long codigoUsuarioCapitado) {
		this.codigoUsuarioCapitado = codigoUsuarioCapitado;
	}


	public Integer getCodigoContrato() {
		return codigoContrato;
	}


	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}


	public void setBuscarPorLike(boolean buscarPorLike) {
		this.buscarPorLike = buscarPorLike;
	}


	public boolean isBuscarPorLike() {
		return buscarPorLike;
	}


	/**
	 * @return the barrio
	 */
	public String getBarrio() {
		return barrio;
	}


	/**
	 * @param barrio the barrio to set
	 */
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}


	/**
	 * @return the localidad
	 */
	public String getLocalidad() {
		return localidad;
	}


	/**
	 * @param localidad the localidad to set
	 */
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}


	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}


	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


	/**
	 * @return the departamento
	 */
	public String getDepartamento() {
		return departamento;
	}


	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}


	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}


	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}


	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the tipoIdEmpleador
	 */
	public String getTipoIdEmpleador() {
		return tipoIdEmpleador;
	}


	/**
	 * @param tipoIdEmpleador the tipoIdEmpleador to set
	 */
	public void setTipoIdEmpleador(String tipoIdEmpleador) {
		this.tipoIdEmpleador = tipoIdEmpleador;
	}


	/**
	 * @return the numIdEmpleador
	 */
	public String getNumIdEmpleador() {
		return numIdEmpleador;
	}


	/**
	 * @param numIdEmpleador the numIdEmpleador to set
	 */
	public void setNumIdEmpleador(String numIdEmpleador) {
		this.numIdEmpleador = numIdEmpleador;
	}


	/**
	 * @return the razonSociEmpleador
	 */
	public String getRazonSociEmpleador() {
		return razonSociEmpleador;
	}


	/**
	 * @param razonSociEmpleador the razonSociEmpleador to set
	 */
	public void setRazonSociEmpleador(String razonSociEmpleador) {
		this.razonSociEmpleador = razonSociEmpleador;
	}


	/**
	 * @return the tipoIdCotizante
	 */
	public String getTipoIdCotizante() {
		return tipoIdCotizante;
	}


	/**
	 * @param tipoIdCotizante the tipoIdCotizante to set
	 */
	public void setTipoIdCotizante(String tipoIdCotizante) {
		this.tipoIdCotizante = tipoIdCotizante;
	}


	/**
	 * @return the numIdCotizante
	 */
	public String getNumIdCotizante() {
		return numIdCotizante;
	}


	/**
	 * @param numIdCotizante the numIdCotizante to set
	 */
	public void setNumIdCotizante(String numIdCotizante) {
		this.numIdCotizante = numIdCotizante;
	}


	/**
	 * @return the nombresCotizante
	 */
	public String getNombresCotizante() {
		return nombresCotizante;
	}


	/**
	 * @param nombresCotizante the nombresCotizante to set
	 */
	public void setNombresCotizante(String nombresCotizante) {
		this.nombresCotizante = nombresCotizante;
	}


	/**
	 * @return the apellidosCotizante
	 */
	public String getApellidosCotizante() {
		return apellidosCotizante;
	}


	/**
	 * @param apellidosCotizante the apellidosCotizante to set
	 */
	public void setApellidosCotizante(String apellidosCotizante) {
		this.apellidosCotizante = apellidosCotizante;
	}


	/**
	 * @return the parentesco
	 */
	public String getParentesco() {
		return parentesco;
	}


	/**
	 * @param parentesco the parentesco to set
	 */
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}


	/**
	 * @return the excepcionMonto
	 */
	public String getExcepcionMonto() {
		return excepcionMonto;
	}


	/**
	 * @param excepcionMonto the excepcionMonto to set
	 */
	public void setExcepcionMonto(String excepcionMonto) {
		this.excepcionMonto = excepcionMonto;
	}


	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	public String getSexo() {
		return sexo;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEmail() {
		return email;
	}


	public void setNumeroFicha(String numeroFicha) {
		this.numeroFicha = numeroFicha;
	}


	public String getNumeroFicha() {
		return numeroFicha;
	}


	public void setCodigoBarrio(Integer codigoBarrio) {
		this.codigoBarrio = codigoBarrio;
	}


	public Integer getCodigoBarrio() {
		return codigoBarrio;
	}


	/**
	 * @return valor de idCuenta
	 */
	public Integer getIdCuenta() {
		return idCuenta;
	}


	/**
	 * @param idCuenta el idCuenta para asignar
	 */
	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}


	/**
	 * @return valor de tipoCobroPaciente
	 */
	public String getTipoCobroPaciente() {
		return tipoCobroPaciente;
	}


	/**
	 * @param tipoCobroPaciente el tipoCobroPaciente para asignar
	 */
	public void setTipoCobroPaciente(String tipoCobroPaciente) {
		this.tipoCobroPaciente = tipoCobroPaciente;
	}


	/**
	 * @return valor de porcentajeMontoCobro
	 */
	public BigDecimal getPorcentajeMontoCobro() {
		return porcentajeMontoCobro;
	}


	/**
	 * @param porcentajeMontoCobro el porcentajeMontoCobro para asignar
	 */
	public void setPorcentajeMontoCobro(BigDecimal porcentajeMontoCobro) {
		this.porcentajeMontoCobro = porcentajeMontoCobro;
	}


	/**
	 * @return valor de detalleCodigo
	 */
	public Integer getDetalleCodigo() {
		return detalleCodigo;
	}


	/**
	 * @param detalleCodigo el detalleCodigo para asignar
	 */
	public void setDetalleCodigo(Integer detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
	}


	/**
	 * @return valor de tipoMontoCobro
	 */
	public String getTipoMontoCobro() {
		return tipoMontoCobro;
	}


	/**
	 * @param tipoMontoCobro el tipoMontoCobro para asignar
	 */
	public void setTipoMontoCobro(String tipoMontoCobro) {
		this.tipoMontoCobro = tipoMontoCobro;
	}


	public void setFechaVigenciaCargue(Date fechaVigenciaCargue) {
		this.fechaVigenciaCargue = fechaVigenciaCargue;
	}


	public Date getFechaVigenciaCargue() {
		return fechaVigenciaCargue;
	}


	public void setNaturaleza(Integer naturaleza) {
		this.naturaleza = naturaleza;
	}


	public Integer getNaturaleza() {
		return naturaleza;
	}

	public Character getManejaPresupuestoCapit() {
		return manejaPresupuestoCapit;
	}


	public void setManejaPresupuestoCapit(Character manejaPresupuestoCapit) {
		this.manejaPresupuestoCapit = manejaPresupuestoCapit;
	}

	/**
	 * @return the grupoFamiliar
	 */
	public List<DtoUsuariosCapitados> getGrupoFamiliar() {
		return grupoFamiliar;
	}

	/**
	 * @param grupoFamiliar the grupoFamiliar to set
	 */
	public void setGrupoFamiliar(List<DtoUsuariosCapitados> grupoFamiliar) {
		this.grupoFamiliar = grupoFamiliar;
	}

	/**
	 * @return the cargueActualDto
	 */
	public CargueDto getCargueActualDto() {
		return cargueActualDto;
	}

	/**
	 * @param cargueActualDto the cargueActualDto to set
	 */
	public void setCargueActualDto(CargueDto cargueActualDto) {
		this.cargueActualDto = cargueActualDto;
	}

	/**
	 * @return the historicoCargues
	 */
	public List<CargueDto> getHistoricoCargues() {
		return historicoCargues;
	}

	/**
	 * @param historicoCargues the historicoCargues to set
	 */
	public void setHistoricoCargues(List<CargueDto> historicoCargues) {
		this.historicoCargues = historicoCargues;
	}

	/**
	 * @return the fechaCargue
	 */
	public Date getFechaCargue() {
		return fechaCargue;
	}

	/**
	 * @param fechaCargue the fechaCargue to set
	 */
	public void setFechaCargue(Date fechaCargue) {
		this.fechaCargue = fechaCargue;
	}

	/**
	 * @return the estadoCargue
	 */
	public String getEstadoCargue() {
		return estadoCargue;
	}

	/**
	 * @param estadoCargue the estadoCargue to set
	 */
	public void setEstadoCargue(String estadoCargue) {
		this.estadoCargue = estadoCargue;
	}

	/**
	 * @return the esIngresado
	 */
	public boolean isEsIngresado() {
		return esIngresado;
	}

	/**
	 * @param esIngresado the esIngresado to set
	 */
	public void setEsIngresado(boolean esIngresado) {
		this.esIngresado = esIngresado;
	}

	public Integer getCodigoSexo() {
		return codigoSexo;
	}

	public void setCodigoSexo(Integer codigoSexo) {
		this.codigoSexo = codigoSexo;
	}

	public Integer getCentroAtencionConsecutivo() {
		return centroAtencionConsecutivo;
	}

	public void setCentroAtencionConsecutivo(Integer centroAtencionConsecutivo) {
		this.centroAtencionConsecutivo = centroAtencionConsecutivo;
	}

	



}
