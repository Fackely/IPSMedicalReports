package com.princetonsa.dto.ordenes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import util.ConstantesBD;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;


/**
 * Dto creado para el manejo Ordenes/Solicitudes
 * @author Cristhian Murillo
*/
public class DtoSolicitud implements Serializable{
	
	/** Serial * */
	private static final long serialVersionUID = 1L;
	
	/** código de la Institución */
	private Integer institucion;
	
	/** Número solicitud * */
	private int numeroSolicitud;
	private Long numeroSolicitudLong; 
	
	/** Tipos Solicitud **/
	private int tipoSolicitud;
	
	/** Número del consecutivo de la orden mèdica * */
	private int consecutivoOrdenesMedicas;
	/** Número del consecutivo de la orden mèdica en String * */
	private String consecutivoOrdenesMedicasStr;
		
	/** Fecha Solicitud / Fecha inicial de la solicitud * */
	private Date fechaSolicitud;
	
	/** Fecha final de la solicitud (Parametro de búsqueda) * */
	private Date fechaFinalSolicitud;
	
	/** Hora  Solicitud * */
	private String horaSolicitud;
	
	/** Indica si la solicitud es urgente * */
	private Boolean urgente;
	
	/** Centro Costo solicitante * */
	private int codigoCentroCostoSolicitante;
	private String nombreCentroCostoSolicitante;
	
	/** Centro Costo solicitado * */
	private int codigoCentroCostoSolicitado;
	private String nombreCentroCostoSolicitado;
	
	/** Se puede utilizar para indicar si está checkeado o no * */
	private boolean flag;
	
	/** * Servicios solicitados */
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
	
	/** * lista de artículos a autorizar */
	private ArrayList<DtoArticulosAutorizaciones> listaArticulos;
	
	/** * Dado el caso almacena la llave primaria del registro de posponer solicitud para la autorización (solicitudes_posponer) */
	private Long codigoPosponer;
	
	/** * Dado el caso almacena la fecha posponer de la solicitud para la autorización (solicitudes_posponer) */
	private Date fechaPosponer;
	
	/** * Datos de la persona asociada a la solicitud */
	private String tipoId;
	private String numeroId;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private Date fechaNacimiento;
	private Integer codPersona;
	private Integer codPaciente;
	
	/** * Datos del convenio y contrato cargado a la orden/solicitud */
	private Integer codigoConvenio;
	private String nombreConvenio;
	private String nombreTipoContrato; 
	private Integer contratoSubcuenta;
	
	/** * Datos de la cuenta de la persona asociada a la solicitud */
	private Integer codigoEstratoSocial;
	private String descripcionEstratoSocial;
	private String nombreTipoAfiliado;
	private Character acronimoTipoAfiliado;
	private Integer semanasCotizacion;
	private String tipoMontoCobro;
	private BigDecimal porcentajeAutorizado;
	private BigDecimal montoAutorizado;
	private Integer nroPrioridad;
	private Integer codViaIngreso;
	private String nombreViaIngreso;
	private String tipoPaciente;
	private Integer codigoNaturalezaPaciente;
	
	/** Indica si se maneja recobro o no * */
	private boolean manejaRecobro;
	/** Descripcion Entidad a Recobrar*/
	private String descripcionOtraEntidadRecobrar;
	
	/** Observaciones de la Solicitud de Autorización*/
	private String observaciones;
	
	/** * Datos Entidad Subcontratada = Otra */
	private String descripcionEntidadSubOtra;
	private String telefonoEntidadSubOtra;
	private String direccionEntidadSubOtra;
	
	/** Fecha de vencimiento para todos los Servicios seleccionados * */
	private Date fechaVencimientoServicios;
	/** Fecha de vencimiento para todos los Articulos seleccionados * */
	private Date fechaVencimientoArticulos;
	
	/** * Datos de la Autorización de Entidad Subcontratada en caso de tener */
	private Long consecutivoPkAutorizacionAsociada;
	private String estadoAutorizacionAsociada;
	
	/** * Datos para diferenciar tipos de Ordenes */
	private String tipoOrden;
	
	/** * Atributo que almacena S o N para mostrar el detalle*/
	private boolean mostrarLink;
	
	/** * Centro de Atención de la cuenta asociada */
	private Integer centroAtencionCuenta;
	
	/** * Centro de Atención asignado */
	private Integer centroAtencionAsignado;
	
	/** * Ingreso Asociado */
	private Integer idIngreso;
	
	/** * lista Centros Costo validos para responder */
	private ArrayList<DtoCentroCosto> listaCentrosCosto;
	
	/*-------------------------------------------------------*/
	/* Parametros de Búsqueda
	/*-------------------------------------------------------*/
	/** * Codigo de la cuenta de la solicitud */
	private Integer codigoCuenta;
	/** * Indica si una solicitud es de cargo directo en estado cargado */
	private boolean cargoDirectoEstadoCargado;
	/** Para usar el filtro de busqueda indicando que el estado de la historia clínica debe ser igual al valor seteado */
	private Integer[] estadoHistoriaClinicaIgual;
	/** * Buscar las que estén asociadas a Convenios Capitados que Manejen Capitación Subcontratada y tengan contratos Vigentes. */
	private boolean conveniosCapitacionSubcontratadaVigentes;
	/** * Indica si una solicitud es de cargo directo en estado pendiente */
	private boolean cargoDirectoEstadoPendiente;
	/** * Indica si una solicitud no tiene cargo */
	private boolean noTieneCargo;
	/** * Indica si la solicitud es de cirugía para saber en donde se tiene que buscar el convenio y el contrato */
	private boolean solicitudDeCirugia;
	/** * Estado de la orden */
	private Integer estadoOrden;
	
	/** * Montos de cobro */
	private Double valorMonto 		= null;
	private Double porcentajeMonto 	= null;
	private Integer tipoMonto 		= null;
	
	
	/** * lista de artículos ya autorizados */
	private ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizados;
	
	
	/** * Reset de la forma  */
	public void reset()
	{		
		this.numeroSolicitud				= ConstantesBD.codigoNuncaValido;
		this.numeroSolicitudLong			= null;
		this.consecutivoOrdenesMedicas		= ConstantesBD.codigoNuncaValido;
		this.consecutivoOrdenesMedicasStr	= null;
		this.fechaSolicitud					= null;
		this.fechaFinalSolicitud			= null;
		this.horaSolicitud					= "";
		this.urgente						= false;
		this.codigoCentroCostoSolicitante	= ConstantesBD.codigoNuncaValido;
		this.nombreCentroCostoSolicitante	= "";
		this.codigoCentroCostoSolicitado	= ConstantesBD.codigoNuncaValido;
		this.nombreCentroCostoSolicitado	= "";
		
		this.codigoCuenta					= null;
		this.cargoDirectoEstadoCargado		= false;
		this.setCargoDirectoEstadoPendiente(false);
		this.noTieneCargo					= false;
		this.setEstadoHistoriaClinicaIgual(null);
		this.flag							= false;
		this.listaServicios					= new ArrayList<DtoServiciosAutorizaciones>();
		this.listaArticulos					= new ArrayList<DtoArticulosAutorizaciones>();
		this.institucion					= null;
		this.codigoPosponer					= null;
		
		this.tipoId							= "";
		this.numeroId						= "";
		this.primerNombre					= "";
		this.segundoNombre					= "";
		this.primerApellido					= "";
		this.segundoApellido				= "";
		this.fechaNacimiento				= null;
		this.codPersona						= null;
		this.codPaciente					= null;
		
		this.codigoConvenio					= null;
		this.nombreConvenio					= "";
		this.nombreTipoContrato				= "";
		this.contratoSubcuenta				= null;
		
		this.conveniosCapitacionSubcontratadaVigentes	= false;
		
		this.codigoEstratoSocial			= null;
		this.descripcionEstratoSocial		= "";
		this.nombreTipoAfiliado				= "";
		this.acronimoTipoAfiliado			= null;
		this.semanasCotizacion				= null;
		this.tipoMontoCobro					= "";
		this.porcentajeAutorizado			= null;	
		this.montoAutorizado				= null;
		this.nroPrioridad 					= null;
		this.codViaIngreso 					= null;
		this.nombreViaIngreso				= null;
		this.tipoPaciente					= "";
		this.codigoNaturalezaPaciente		= null;
		
		this.manejaRecobro					= false;
		this.descripcionOtraEntidadRecobrar	= "";
		
		this.observaciones					= "";
		
		this.descripcionEntidadSubOtra		= "";
		this.telefonoEntidadSubOtra			= "";
		this.direccionEntidadSubOtra		= "";
		
		this.fechaVencimientoArticulos		= null;
		this.fechaVencimientoServicios		= null;

		this.consecutivoPkAutorizacionAsociada 	= null;
		this.estadoAutorizacionAsociada 		= null;
		this.solicitudDeCirugia				= false;
		this.tipoOrden						= null;
		this.estadoOrden					= null;
		this.mostrarLink					= false;
		this.centroAtencionCuenta			= null;
		this.centroAtencionAsignado			= null;
		this.idIngreso						= null;
		this.listaCentrosCosto				= new ArrayList<DtoCentroCosto>();
		
		this.valorMonto 					= null;
		this.porcentajeMonto 				= null;
		this.tipoMonto 						= null;
		this.listaArticulosAutorizados = new ArrayList<DtoArticulosAutorizaciones>();
		
		this.fechaPosponer					= null;
		
		this.tipoSolicitud					= ConstantesBD.codigoNuncaValido;
	}

	
	
	
	
	
	/** * Reset de la forma  */
	public void resetEntidadSubOtra()
	{		
		resetDatosEntidad();
		this.observaciones	= "";
	}

	
	/** * Reset de la forma  */
	public void resetDatosEntidad()
	{		
		this.descripcionEntidadSubOtra		= "";
		this.telefonoEntidadSubOtra			= "";
		this.direccionEntidadSubOtra		= "";
	}
	
	
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}



	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}



	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}



	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}



	public String getHoraSolicitud() {
		return horaSolicitud;
	}



	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}



	public Boolean isUrgente() {
		return urgente;
	}



	public void setUrgente(Boolean urgente) {
		this.urgente = urgente;
	}



	public int getCodigoCentroCostoSolicitante() {
		return codigoCentroCostoSolicitante;
	}



	public void setCodigoCentroCostoSolicitante(int codigoCentroCostoSolicitante) {
		this.codigoCentroCostoSolicitante = codigoCentroCostoSolicitante;
	}



	public String getNombreCentroCostoSolicitante() {
		return nombreCentroCostoSolicitante;
	}



	public void setNombreCentroCostoSolicitante(String nombreCentroCostoSolicitante) {
		this.nombreCentroCostoSolicitante = nombreCentroCostoSolicitante;
	}



	public int getCodigoCentroCostoSolicitado() {
		return codigoCentroCostoSolicitado;
	}



	public void setCodigoCentroCostoSolicitado(int codigoCentroCostoSolicitado) {
		this.codigoCentroCostoSolicitado = codigoCentroCostoSolicitado;
	}



	public String getNombreCentroCostoSolicitado() {
		return nombreCentroCostoSolicitado;
	}



	public void setNombreCentroCostoSolicitado(String nombreCentroCostoSolicitado) {
		this.nombreCentroCostoSolicitado = nombreCentroCostoSolicitado;
	}



	public Integer getCodigoCuenta() {
		return codigoCuenta;
	}



	public void setCodigoCuenta(Integer codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}




	public boolean isCargoDirectoEstadoCargado() {
		return cargoDirectoEstadoCargado;
	}




	public void setCargoDirectoEstadoCargado(boolean cargoDirectoEstadoCargado) {
		this.cargoDirectoEstadoCargado = cargoDirectoEstadoCargado;
	}




	public String getDescripcionEstratoSocial() {
		return descripcionEstratoSocial;
	}




	public void setDescripcionEstratoSocial(String descripcionEstratoSocial) {
		this.descripcionEstratoSocial = descripcionEstratoSocial;
	}




	public String getNombreTipoAfiliado() {
		return nombreTipoAfiliado;
	}




	public void setNombreTipoAfiliado(String nombreTipoAfiliado) {
		this.nombreTipoAfiliado = nombreTipoAfiliado;
	}




	public Integer getSemanasCotizacion() {
		return semanasCotizacion;
	}




	public void setSemanasCotizacion(Integer semanasCotizacion) {
		this.semanasCotizacion = semanasCotizacion;
	}




	public String getTipoMontoCobro() {
		return tipoMontoCobro;
	}




	public void setTipoMontoCobro(String tipoMontoCobro) {
		this.tipoMontoCobro = tipoMontoCobro;
	}




	public BigDecimal getPorcentajeAutorizado() {
		return porcentajeAutorizado;
	}




	public void setPorcentajeAutorizado(BigDecimal porcentajeAutorizado) {
		this.porcentajeAutorizado = porcentajeAutorizado;
	}




	public BigDecimal getMontoAutorizado() {
		return montoAutorizado;
	}




	public void setMontoAutorizado(BigDecimal montoAutorizado) {
		this.montoAutorizado = montoAutorizado;
	}




	public Integer getNroPrioridad() {
		return nroPrioridad;
	}




	public void setNroPrioridad(Integer nroPrioridad) {
		this.nroPrioridad = nroPrioridad;
	}




	public boolean isFlag() {
		return flag;
	}



	public void setFlag(boolean flag) {
		this.flag = flag;
	}




	public ArrayList<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}




	public void setListaServicios(
			ArrayList<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}




	public ArrayList<DtoArticulosAutorizaciones> getListaArticulos() {
		return listaArticulos;
	}




	public void setListaArticulos(
			ArrayList<DtoArticulosAutorizaciones> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}




	public Integer getInstitucion() {
		return institucion;
	}




	public void setInstitucion(Integer institucion) {
		this.institucion = institucion;
	}




	public Long getCodigoPosponer() {
		return codigoPosponer;
	}




	public void setCodigoPosponer(Long codigoPosponer) {
		this.codigoPosponer = codigoPosponer;
	}




	public Date getFechaFinalSolicitud() {
		return fechaFinalSolicitud;
	}




	public void setFechaFinalSolicitud(Date fechaFinalSolicitud) {
		this.fechaFinalSolicitud = fechaFinalSolicitud;
	}




	public String getTipoId() {
		return tipoId;
	}




	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}




	public String getNumeroId() {
		return numeroId;
	}

	
	
	/**
	 * Utilizado para organizar por tipo y número Id
	 * @return
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	public String getTipoIdNumeroId() {
		return tipoId+numeroId;
	}
	


	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
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




	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}




	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}




	public String getNombreConvenio() {
		return nombreConvenio;
	}




	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}




	public boolean isConveniosCapitacionSubcontratadaVigentes() {
		return conveniosCapitacionSubcontratadaVigentes;
	}




	public void setConveniosCapitacionSubcontratadaVigentes(
			boolean conveniosCapitacionSubcontratadaVigentes) {
		this.conveniosCapitacionSubcontratadaVigentes = conveniosCapitacionSubcontratadaVigentes;
	}




	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}




	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}




	public String getNombreTipoContrato() {
		return nombreTipoContrato;
	}




	public Integer getCodViaIngreso() {
		return codViaIngreso;
	}



	public void setCodViaIngreso(Integer codViaIngreso) {
		this.codViaIngreso = codViaIngreso;
	}



	public void setNombreTipoContrato(String nombreTipoContrato) {
		this.nombreTipoContrato = nombreTipoContrato;
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



	public String getObservaciones() {
		return observaciones;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}



	public Date getFechaVencimientoServicios() {
		return fechaVencimientoServicios;
	}



	public void setFechaVencimientoServicios(Date fechaVencimientoServicios) {
		this.fechaVencimientoServicios = fechaVencimientoServicios;
	}



	public Date getFechaVencimientoArticulos() {
		return fechaVencimientoArticulos;
	}



	public void setFechaVencimientoArticulos(Date fechaVencimientoArticulos) {
		this.fechaVencimientoArticulos = fechaVencimientoArticulos;
	}



	public String getTipoPaciente() {
		return tipoPaciente;
	}



	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}



	public Integer getCodigoNaturalezaPaciente() {
		return codigoNaturalezaPaciente;
	}



	public void setCodigoNaturalezaPaciente(Integer codigoNaturalezaPaciente) {
		this.codigoNaturalezaPaciente = codigoNaturalezaPaciente;
	}



	public Integer getCodPersona() {
		return codPersona;
	}



	public void setCodPersona(Integer codPersona) {
		this.codPersona = codPersona;
	}


	public boolean isNoTieneCargo() {
		return noTieneCargo;
	}



	public void setNoTieneCargo(boolean noTieneCargo) {
		this.noTieneCargo = noTieneCargo;
	}



	public void setCargoDirectoEstadoPendiente(boolean cargoDirectoEstadoPendiente) {
		this.cargoDirectoEstadoPendiente = cargoDirectoEstadoPendiente;
	}



	public boolean isCargoDirectoEstadoPendiente() {
		return cargoDirectoEstadoPendiente;
	}



	public void setEstadoHistoriaClinicaIgual(
			Integer[] estadoHistoriaClinicaIgual) {
		this.estadoHistoriaClinicaIgual = estadoHistoriaClinicaIgual;
	}



	public Integer[] getEstadoHistoriaClinicaIgual() {
		return estadoHistoriaClinicaIgual;
	}



	public Long getConsecutivoPkAutorizacionAsociada() {
		return consecutivoPkAutorizacionAsociada;
	}



	public void setConsecutivoPkAutorizacionAsociada(
			Long consecutivoPkAutorizacionAsociada) {
		this.consecutivoPkAutorizacionAsociada = consecutivoPkAutorizacionAsociada;
	}



	public String getEstadoAutorizacionAsociada() {
		return estadoAutorizacionAsociada;
	}



	public void setEstadoAutorizacionAsociada(String estadoAutorizacionAsociada) {
		this.estadoAutorizacionAsociada = estadoAutorizacionAsociada;
	}


	public boolean isSolicitudDeCirugia() {
		return solicitudDeCirugia;
	}


	public void setSolicitudDeCirugia(boolean solicitudDeCirugia) {
		this.solicitudDeCirugia = solicitudDeCirugia;
	}



	public void setConsecutivoOrdenesMedicas(int consecutivoOrdenesMedicas) {
		this.consecutivoOrdenesMedicas = consecutivoOrdenesMedicas;
	}


	public int getConsecutivoOrdenesMedicas() {
		return consecutivoOrdenesMedicas;
	}

	/**
	 * @return valor de tipoOrden
	 */
	public String getTipoOrden() {
		return tipoOrden;
	}


	/**
	 * @param tipoOrden el tipoOrden para asignar
	 */
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}


	/**
	 * @return valor de estadoOrden
	 */
	public Integer getEstadoOrden() {
		return estadoOrden;
	}


	/**
	 * @param estadoOrden el estadoOrden para asignar
	 */
	public void setEstadoOrden(Integer estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	/**
	 * @return valor de numeroSolicitudLong
	 */
	public Long getNumeroSolicitudLong() {
		return numeroSolicitudLong;
	}

	/**
	 * @param numeroSolicitudLong el numeroSolicitudLong para asignar
	 */
	public void setNumeroSolicitudLong(Long numeroSolicitudLong) {
		this.numeroSolicitudLong = numeroSolicitudLong;
		this.numeroSolicitud = this.numeroSolicitudLong.intValue();
	}


	/**
	 * @return valor de consecutivoOrdenesMedicasStr
	 */
	public String getConsecutivoOrdenesMedicasStr() {
		return consecutivoOrdenesMedicasStr;
	}


	/**
	 * @param consecutivoOrdenesMedicasStr el consecutivoOrdenesMedicasStr para asignar
	 */
	public void setConsecutivoOrdenesMedicasStr(String consecutivoOrdenesMedicasStr) {
		this.consecutivoOrdenesMedicasStr = consecutivoOrdenesMedicasStr;
		this.consecutivoOrdenesMedicas = Integer.parseInt(this.consecutivoOrdenesMedicasStr);
	}



	/**
	 * @return valor de mostrarLink
	 */
	public boolean getMostrarLink() {
		return mostrarLink;
	}


	/**
	 * @param mostrarLink el mostrarLink para asignar
	 */
	public void setMostrarLink(boolean mostrarLink) {
		this.mostrarLink = mostrarLink;
	}


	/**
	 * @return valor de urgente
	 */
	public Boolean getUrgente() {
		return urgente;
	}


	/**
	 * @return valor de centroAtencionCuenta
	 */
	public Integer getCentroAtencionCuenta() {
		return centroAtencionCuenta;
	}



	/**
	 * @param centroAtencionCuenta el centroAtencionCuenta para asignar
	 */
	public void setCentroAtencionCuenta(Integer centroAtencionCuenta) {
		this.centroAtencionCuenta = centroAtencionCuenta;
	}






	/**
	 * @return valor de nombreViaIngreso
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}






	/**
	 * @param nombreViaIngreso el nombreViaIngreso para asignar
	 */
	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}






	/**
	 * @return valor de contratoSubcuenta
	 */
	public Integer getContratoSubcuenta() {
		return contratoSubcuenta;
	}






	/**
	 * @param contratoSubcuenta el contratoSubcuenta para asignar
	 */
	public void setContratoSubcuenta(Integer contratoSubcuenta) {
		this.contratoSubcuenta = contratoSubcuenta;
	}






	/**
	 * @return valor de acronimoTipoAfiliado
	 */
	public Character getAcronimoTipoAfiliado() {
		return acronimoTipoAfiliado;
	}






	/**
	 * @param acronimoTipoAfiliado el acronimoTipoAfiliado para asignar
	 */
	public void setAcronimoTipoAfiliado(Character acronimoTipoAfiliado) {
		this.acronimoTipoAfiliado = acronimoTipoAfiliado;
	}






	/**
	 * @return valor de codigoEstratoSocial
	 */
	public Integer getCodigoEstratoSocial() {
		return codigoEstratoSocial;
	}






	/**
	 * @param codigoEstratoSocial el codigoEstratoSocial para asignar
	 */
	public void setCodigoEstratoSocial(Integer codigoEstratoSocial) {
		this.codigoEstratoSocial = codigoEstratoSocial;
	}






	/**
	 * @return valor de idIngreso
	 */
	public Integer getIdIngreso() {
		return idIngreso;
	}






	/**
	 * @param idIngreso el idIngreso para asignar
	 */
	public void setIdIngreso(Integer idIngreso) {
		this.idIngreso = idIngreso;
	}






	/**
	 * @return valor de listaCentrosCosto
	 */
	public ArrayList<DtoCentroCosto> getListaCentrosCosto() {
		return listaCentrosCosto;
	}






	/**
	 * @param listaCentrosCosto el listaCentrosCosto para asignar
	 */
	public void setListaCentrosCosto(ArrayList<DtoCentroCosto> listaCentrosCosto) {
		this.listaCentrosCosto = listaCentrosCosto;
	}






	/**
	 * @return valor de valorMonto
	 */
	public Double getValorMonto() {
		return valorMonto;
	}






	/**
	 * @param valorMonto el valorMonto para asignar
	 */
	public void setValorMonto(Double valorMonto) {
		this.valorMonto = valorMonto;
	}






	/**
	 * @return valor de porcentajeMonto
	 */
	public Double getPorcentajeMonto() {
		return porcentajeMonto;
	}






	/**
	 * @param porcentajeMonto el porcentajeMonto para asignar
	 */
	public void setPorcentajeMonto(Double porcentajeMonto) {
		this.porcentajeMonto = porcentajeMonto;
	}






	/**
	 * @return valor de tipoMonto
	 */
	public Integer getTipoMonto() {
		return tipoMonto;
	}






	/**
	 * @param tipoMonto el tipoMonto para asignar
	 */
	public void setTipoMonto(Integer tipoMonto) {
		this.tipoMonto = tipoMonto;
	}






	/**
	 * @return valor de centroAtencionAsignado
	 */
	public Integer getCentroAtencionAsignado() {
		return centroAtencionAsignado;
	}






	/**
	 * @param centroAtencionAsignado el centroAtencionAsignado para asignar
	 */
	public void setCentroAtencionAsignado(Integer centroAtencionAsignado) {
		this.centroAtencionAsignado = centroAtencionAsignado;
	}






	/**
	 * @return the listaArticulosAutorizados
	 */
	public ArrayList<DtoArticulosAutorizaciones> getListaArticulosAutorizados() {
		return listaArticulosAutorizados;
	}






	/**
	 * @param listaArticulosAutorizados the listaArticulosAutorizados to set
	 */
	public void setListaArticulosAutorizados(
			ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizados) {
		this.listaArticulosAutorizados = listaArticulosAutorizados;
	}






	public void setCodPaciente(Integer codPaciente) {
		this.codPaciente = codPaciente;
	}






	public Integer getCodPaciente() {
		return codPaciente;
	}






	public void setFechaPosponer(Date fechaPosponer) {
		this.fechaPosponer = fechaPosponer;
	}






	public Date getFechaPosponer() {
		return fechaPosponer;
	}






	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}






	public int getTipoSolicitud() {
		return tipoSolicitud;
	}


}