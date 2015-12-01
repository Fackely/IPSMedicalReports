package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.administracion.CentroCostoDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;

/**
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 03:15:55 p.m.
 */
public class AutorizacionCapitacionDto implements Serializable{
	
	private static final long serialVersionUID = -8651884991055781291L;
	
	/**Atributo que almacena el tipo de autorización */
	private String tipoAutorizacion;
	
	/**Atributo que almacena el código de la institución que esta generando la autorización */
	private int codigoInstitucion;
	
	/**Atributo para almacenar la fecha de vencimiento de la autorización */
	private Date fechaVencimientoAutorizacion;
	
	/**Atributo para almacenar las observaciones generales de la autorización */
	private String observacionesGenerales;
	
	/**Atributo para almacenar el login del usuario que genera la autorización */
	private String loginUsuario;
	
	/**Atributo para almacenar el codigo persona-usuario que genera la autorización */
	private int codigoPersonaUsuario;
	
	/**Atributo para almacenar el codigo del centro de atención que genera la autorización */
	private int centroAtencion;
	
	/**Atributo que indica si el proceso de autorización fue exitoso o no */
	private boolean procesoExitoso;
	
	/**Atributo que indica si para alguno o todos los Servicios/MedicamentosInsumos no se puedo generar autorización */
	private boolean verificarDetalleError;
	
	/**Atributo	para almacenar el listado de ordenes a autorizar */
	private List<OrdenAutorizacionDto> ordenesAutorizar;
	
	/**Atributo para almacenar la entidad subcontratada a autorizar */
	private EntidadSubContratadaDto entidadSubAutorizarCapitacion = new EntidadSubContratadaDto();
	
	/**Atributo para almacenar los datos del paciente al cual se le realizará la autorización de capitación subcontratad */
	private DatosPacienteAutorizacionDto datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
	
	/**Atibuto para almacenar el monto de cobro para la autorización de capitación subcontratada */
	private MontoCobroDto montoCobroAutorizacion = new MontoCobroDto();
	
	/**Atributo que almacena los diferentes centros de costo que responden o ejecutan la autorización de capitación subcontratada
	 * para las ordenes ambulatorias de medicamentos */
	private List<DtoCentroCosto> listaCentroCostoEjecOrdenAmbArtic;
	
	/**Atributo para almacenar el indicativo de la autorización de Servicios/MedicamentosInsumos de ingreso estancia */
	private boolean autoServArtIngresoEstancia;
	
	/**Atributo para almacenar el mensaje de error por el cual no se puede generar autorización */
	private ErrorMessage mensajeErrorGeneral;
	
	/**Atributo para almacenar si tiene o no convenio recobro */
	private boolean convenioRecobro;
	
	/**Atributo para almacenar el codigo del convenio recobro */
	private Integer codConvenioRecobro;
	
	/**Atributo para almacenar la descripción del convenio recobro  */
	private String descripcionConvenioRecobro;
	
	/**
	 * Atributo que representa el centro de costo seleccionado o de la orden
	 */
	private CentroCostoDto centroCosto= new CentroCostoDto();
	/**
	 * Atributo que representa el consecutivo de la autorización de entidad subcontratada
	 */
	private String consecutivoAutorizacion;
	
	/**
	 * Atributo que representa el consecutivo de la autorización de capitación subcontratada
	 */
	private String consecutivoAutorizacionCapita;
	
	/**Atributo para almacenar la fecha de la autorización */
	private Date fechaAutorizacion;
	
	/**
	 * Atributo que almacena la hora en la que se genera la autorizacion
	 */
	private String horaAutorizacion;
	
	/**Atributo para almacenar la fecha de la autorización */
	private String estadoAutorizacion;
	
	/**
	 * Atributo para almacenar el indicativo temporal
	 */
	private char indicativoTemporal;
	
	/**
	 * Atributo que indica si se realiza actualizacion de Cierre
	 */
	private boolean actualizarCierre;
	
	/**
	 * Atributo que permite almacenar el codido de la autorización de ingreso estancia
	 */
	private long codAutorIngresoEstancia;
	
	/**
	 * Atributo que almacena los datos de la entrega de la autorizacion
	 */
	private AutorizacionEntregaDto autorizacionEntrega;
	
	
	/**
	 * Atributo que almacena el valor de la autorización
	 */
	private BigDecimal valorAutorizacion;
	
	public AutorizacionCapitacionDto(){
		ordenesAutorizar = new ArrayList<OrdenAutorizacionDto>();
		listaCentroCostoEjecOrdenAmbArtic = new ArrayList<DtoCentroCosto>();
	}
	
	/**
	 * @param tipoAutorizacion
	 * @param codigoInstitucion
	 * @param fechaVencimientoAutorizacion
	 * @param observacionesGenerales
	 * @param loginUsuario
	 * @param codigoPersonaUsuario
	 * @param centroAtencion
	 * @param procesoExitoso
	 * @param verificarDetalleError
	 * @param tipoOrden
	 * @param ordenesAutorizar
	 * @param entidadSubAutorizarCapitacion
	 * @param datosPacienteAutorizar
	 * @param montoCobroAutorizacion
	 * @param listaCentroCostoEjecOrdenAmbArtic
	 * @param autoServArtIngresoEstancia
	 * @param mensajeErrorGeneral
	 * @param codConvenioRecobro
	 * @param descripcionConvenioRecobro
	 */
	public AutorizacionCapitacionDto(String tipoAutorizacion,
			int codigoInstitucion, Date fechaVencimientoAutorizacion,
			String observacionesGenerales, String loginUsuario,
			int codigoPersonaUsuario, int centroAtencion,
			boolean procesoExitoso, boolean verificarDetalleError,
			List<OrdenAutorizacionDto> ordenesAutorizar,
			EntidadSubContratadaDto entidadSubAutorizarCapitacion,
			DatosPacienteAutorizacionDto datosPacienteAutorizar,
			MontoCobroDto montoCobroAutorizacion,
			List<DtoCentroCosto> listaCentroCostoEjecOrdenAmbArtic,
			boolean autoServArtIngresoEstancia, ErrorMessage mensajeErrorGeneral,
			Integer codConvenioRecobro, String descripcionConvenioRecobro) {
		this.tipoAutorizacion = tipoAutorizacion;
		this.codigoInstitucion = codigoInstitucion;
		this.fechaVencimientoAutorizacion = fechaVencimientoAutorizacion;
		this.observacionesGenerales = observacionesGenerales;
		this.loginUsuario = loginUsuario;
		this.codigoPersonaUsuario = codigoPersonaUsuario;
		this.centroAtencion = centroAtencion;
		this.procesoExitoso = procesoExitoso;
		this.verificarDetalleError = verificarDetalleError;
		this.ordenesAutorizar = ordenesAutorizar;
		this.entidadSubAutorizarCapitacion = entidadSubAutorizarCapitacion;
		this.datosPacienteAutorizar = datosPacienteAutorizar;
		this.montoCobroAutorizacion = montoCobroAutorizacion;
		this.listaCentroCostoEjecOrdenAmbArtic = listaCentroCostoEjecOrdenAmbArtic;
		this.autoServArtIngresoEstancia = autoServArtIngresoEstancia;
		this.mensajeErrorGeneral = mensajeErrorGeneral;
		this.codConvenioRecobro = codConvenioRecobro;
		this.descripcionConvenioRecobro = descripcionConvenioRecobro;
	}
	
	
	/**
	 * Constructor de la clase para cargar los datos obtenidos en la consulta obtener 
	 * autorización de entidad subcontratada y capitación. 
	 * 
	 * @param loginAutoEntSub
	 * @param codigoIngresoPaciente
	 * @param codEntSub
	 * @param codInstitucion
	 * @param fechaAutorizacion
	 * @param horaAutorizacion
	 * @param fechaVencimiento
	 * @param observaciones
	 * @param tipoEntEjecuta
	 * @param estado
	 * @param codCentroAtencion
	 * @param convenioRecobro
	 * @param loguinUsuarioAutorizo
	 * @param tipoAfiliado
	 * @param clasificacionSocioeconomica
	 * @param tipoAutorizacion
	 * @param descripcionConvenioRecobro
	 * @param indicativoTemporal
	 * @param descripcionEntidadOtra
	 * @param indicadorPrioridad
	 * @param direccionEntidadOtra
	 * @param telefonoEntidadOtra
	 * @param viaIngreso
	 * @param valormontoCalculado
	 * @param porcentajemonto
	 * @param tipomonto
	 * @param tipodetallemonto
	 */
	
	public AutorizacionCapitacionDto (String loginAutoEntSub, int codigoPaciente, 
			long codEntSub, int codInstitucion, Date fechaAutorizacion, String horaAutorizacion, 
			Date fechaVencimiento, String observaciones, String estado,	Integer codCentroAtencion, 
			Integer convenioRecobro, Character tipoAfiliado, Integer clasificacionSocioeconomica, 
			String tipoAutorizacion, String descripcionConvenioRecobro,	char indicativoTemporal, 
			String descripcionEntidadOtra, String direccionEntidadOtra, 
			String telefonoEntidadOtra, Double valorMontoCalculado, 
			Double porcentajeMonto, Integer tipoMonto, String tipoDetalleMonto){
		
		this.loginUsuario = loginAutoEntSub;
		this.codigoInstitucion = codInstitucion;
		this.fechaAutorizacion = fechaAutorizacion;
		this.horaAutorizacion = horaAutorizacion;
		this.fechaVencimientoAutorizacion = fechaVencimiento;
		this.observacionesGenerales = observaciones;
		this.estadoAutorizacion = estado;
		if (codCentroAtencion!=null){
			this.centroAtencion = codCentroAtencion;
		}
		if (convenioRecobro!= null){
			this.codConvenioRecobro = convenioRecobro;
			this.convenioRecobro = true;
		}
		this.tipoAutorizacion = tipoAutorizacion;
		this.descripcionConvenioRecobro = descripcionConvenioRecobro;
		this.indicativoTemporal = indicativoTemporal;
		this.entidadSubAutorizarCapitacion.setCodEntidadSubcontratada(codEntSub);
		this.entidadSubAutorizarCapitacion.setRazonSocial(descripcionEntidadOtra);
		this.entidadSubAutorizarCapitacion.setDireccionEntidad(direccionEntidadOtra);
		this.entidadSubAutorizarCapitacion.setTelefonoEntidad(telefonoEntidadOtra);
		this.datosPacienteAutorizar.setCodigoPaciente(codigoPaciente);
		if (tipoAfiliado!=null){
			this.datosPacienteAutorizar.setTipoAfiliado(tipoAfiliado.toString());
		}
		if (clasificacionSocioeconomica!=null){
			this.datosPacienteAutorizar.setClasificacionSocieconomica(clasificacionSocioeconomica);
		}
		if (valorMontoCalculado!=null){
			this.montoCobroAutorizacion.setValorMontoCalculado(valorMontoCalculado);
		}
		this.montoCobroAutorizacion.setPorcentajeMonto(porcentajeMonto);
		this.montoCobroAutorizacion.setTipoMonto(tipoMonto);
		this.montoCobroAutorizacion.setTipoDetalleMonto(tipoDetalleMonto);
	}

	/**
	 * @return tipoAutorizacion
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}
	
	/**
	 * @param tipoAutorizacion
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
	
	/**
	 * @return codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	
	/**
	 * @param codigoInstitucion
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	
	/**
	 * @return fechaVencimientoAutorizacion
	 */
	public Date getFechaVencimientoAutorizacion() {
		return fechaVencimientoAutorizacion;
	}
	
	/**
	 * @param fechaVencimientoAutorizacion
	 */
	public void setFechaVencimientoAutorizacion(
			Date fechaVencimientoAutorizacion) {
		this.fechaVencimientoAutorizacion = fechaVencimientoAutorizacion;
	}
	
	/**
	 * @return observacionesGenerales
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	
	/**
	 * @param observacionesGenerales
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	
	/**
	 * @return loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}
	
	/**
	 * @param loginUsuario
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	
	

	/**
	 * @return the codigoPersonaUsuario
	 */
	public int getCodigoPersonaUsuario() {
		return codigoPersonaUsuario;
	}

	/**
	 * @param codigoPersonaUsuario the codigoPersonaUsuario to set
	 */
	public void setCodigoPersonaUsuario(int codigoPersonaUsuario) {
		this.codigoPersonaUsuario = codigoPersonaUsuario;
	}

	/**
	 * @return centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	/**
	 * @return procesoExitoso
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}

	/**
	 * @param procesoExitoso
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}
	
	/**
	 * @return verificarDetalleError
	 */
	public boolean isVerificarDetalleError() {
		return verificarDetalleError;
	}
	
	/**
	 * @param verificarDetalleError
	 */
	public void setVerificarDetalleError(boolean verificarDetalleError) {
		this.verificarDetalleError = verificarDetalleError;
	}
	
	/**
	 * @return ordenesAutorizar
	 */
	public List<OrdenAutorizacionDto> getOrdenesAutorizar() {
		return ordenesAutorizar;
	}
	
	/**
	 * @param ordenesAutorizar
	 */
	public void setOrdenesAutorizar(List<OrdenAutorizacionDto> ordenesAutorizar) {
		this.ordenesAutorizar = ordenesAutorizar;
	}
	
	/**
	 * @return entidadSubAutorizarCapitacion
	 */
	public EntidadSubContratadaDto getEntidadSubAutorizarCapitacion() {
		return entidadSubAutorizarCapitacion;
	}
	
	/**
	 * @param entidadSubAutorizarCapitacion
	 */
	public void setEntidadSubAutorizarCapitacion(EntidadSubContratadaDto entidadSubAutorizarCapitacion) {
		this.entidadSubAutorizarCapitacion = entidadSubAutorizarCapitacion;
	}
	
	/**
	 * @return datosPacienteAutorizar
	 */
	public DatosPacienteAutorizacionDto getDatosPacienteAutorizar() {
		return datosPacienteAutorizar;
	}
	
	/**
	 * @param datosPacienteAutorizar
	 */
	public void setDatosPacienteAutorizar(DatosPacienteAutorizacionDto datosPacienteAutorizar) {
		this.datosPacienteAutorizar = datosPacienteAutorizar;
	}
	
	/**
	 * @return montoCobroAutorizacion
	 */
	public MontoCobroDto getMontoCobroAutorizacion() {
		return montoCobroAutorizacion;
	}
	
	/**
	 * @param montoCobroAutorizacion
	 */
	public void setMontoCobroAutorizacion(MontoCobroDto montoCobroAutorizacion) {
		this.montoCobroAutorizacion = montoCobroAutorizacion;
	}
	
	/**
	 * @return listaCentroCostoEjecOrdenAmbArtic
	 */
	public List<DtoCentroCosto> getListaCentroCostoEjecOrdenAmbArtic() {
		return listaCentroCostoEjecOrdenAmbArtic;
	}
	
	/**
	 * @param listaCentroCostoEjecOrdenAmbArtic
	 */
	public void setListaCentroCostoEjecOrdenAmbArtic(
			List<DtoCentroCosto> listaCentroCostoEjecOrdenAmbArtic) {
		this.listaCentroCostoEjecOrdenAmbArtic = listaCentroCostoEjecOrdenAmbArtic;
	}
	
	/**
	 * @return autoServArtIngresoEstancia
	 */
	public boolean isAutoServArtIngresoEstancia() {
		return autoServArtIngresoEstancia;
	}
	
	/**
	 * @param autoServArtIngresoEstancia
	 */
	public void setAutoServArtIngresoEstancia(boolean autoServArtIngresoEstancia) {
		this.autoServArtIngresoEstancia = autoServArtIngresoEstancia;
	}
	
	/**
	 * @return mensajeErrorGeneral
	 */
	public ErrorMessage getMensajeErrorGeneral() {
		return mensajeErrorGeneral;
	}
	
	/**
	 * @param mensajeErrorGeneral
	 */
	public void setMensajeErrorGeneral(ErrorMessage mensajeErrorGeneral) {
		this.mensajeErrorGeneral = mensajeErrorGeneral;
	}
	
	/**
	 * @return codConvenioRecobro
	 */
	public Integer getCodConvenioRecobro() {
		return codConvenioRecobro;
	}
	
	/**
	 * @param codConvenioRecobro
	 */
	public void setCodConvenioRecobro(Integer codConvenioRecobro) {
		this.codConvenioRecobro = codConvenioRecobro;
	}
	
	/**
	 * @return descripcionConvenioRecobro
	 */
	public String getDescripcionConvenioRecobro() {
		return descripcionConvenioRecobro;
	}
	
	/**
	 * @param descripcionConvenioRecobro
	 */
	public void setDescripcionConvenioRecobro(String descripcionConvenioRecobro) {
		this.descripcionConvenioRecobro = descripcionConvenioRecobro;
	}
	
	/**
	 * @return the centroCosto
	 */
	public CentroCostoDto getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(CentroCostoDto centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the convenioRecobro
	 */
	public boolean isConvenioRecobro() {
		return convenioRecobro;
	}

	/**
	 * @param convenioRecobro the convenioRecobro to set
	 */
	public void setConvenioRecobro(boolean convenioRecobro) {
		this.convenioRecobro = convenioRecobro;
	}

	/**
	 * @return the consecutivoAutorizacion
	 */
	public String getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}

	/**
	 * @param consecutivoAutorizacion the consecutivoAutorizacion to set
	 */
	public void setConsecutivoAutorizacion(String consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}

	/**
	 * @return the fechaAutorizacion
	 */
	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	/**
	 * @param fechaAutorizacion the fechaAutorizacion to set
	 */
	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	/**
	 * @return the estadoAutorizacion
	 */
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}

	/**
	 * @param estadoAutorizacion the estadoAutorizacion to set
	 */
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public String getHoraAutorizacion() {
		return horaAutorizacion;
	}

	public void setHoraAutorizacion(String horaAutorizacion) {
		this.horaAutorizacion = horaAutorizacion;
	}

	public char getIndicativoTemporal() {
		return indicativoTemporal;
	}

	public void setIndicativoTemporal(char indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}

	public String getConsecutivoAutorizacionCapita() {
		return consecutivoAutorizacionCapita;
	}

	public void setConsecutivoAutorizacionCapita(
			String consecutivoAutorizacionCapita) {
		this.consecutivoAutorizacionCapita = consecutivoAutorizacionCapita;
	}
	
	public boolean isActualizarCierre() {
		return actualizarCierre;
	}

	public void setActualizarCierre(boolean actualizarCierre) {
		this.actualizarCierre = actualizarCierre;
	}

	public long getCodAutorIngresoEstancia() {
		return codAutorIngresoEstancia;
	}

	public void setCodAutorIngresoEstancia(long codAutorIngresoEstancia) {
		this.codAutorIngresoEstancia = codAutorIngresoEstancia;
	}

	/**
	 * @return the autorizacionEntrega
	 */
	public AutorizacionEntregaDto getAutorizacionEntrega() {
		return autorizacionEntrega;
	}

	/**
	 * @param autorizacionEntrega the autorizacionEntrega to set
	 */
	public void setAutorizacionEntrega(AutorizacionEntregaDto autorizacionEntrega) {
		this.autorizacionEntrega = autorizacionEntrega;
	}

	public BigDecimal getValorAutorizacion() {
		return valorAutorizacion;
	}

	public void setValorAutorizacion(BigDecimal valorAutorizacion) {
		this.valorAutorizacion = valorAutorizacion;
	}

}
	
	