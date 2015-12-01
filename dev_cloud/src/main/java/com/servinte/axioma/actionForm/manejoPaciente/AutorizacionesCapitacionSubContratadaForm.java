package com.servinte.axioma.actionForm.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.servinte.axioma.dto.administracion.CentroAtencionDto;
import com.servinte.axioma.dto.administracion.CentroCostoDto;
import com.servinte.axioma.dto.capitacion.NivelAtencionDto;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;
import com.servinte.axioma.dto.ordenes.ClaseOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;

/**
 * Clase que soporta las propiedades de las JSP de la funcionalidad de 
 * autorizaciones manuales de capitación subcontratada
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class AutorizacionesCapitacionSubContratadaForm extends ValidatorForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3354210058354883705L;

	/**
	 * Atributo utilizado para verificar si se muestra o no mensaje informativo
	 * por niveles de atención del usuario
	 */
	private boolean mensajeNivelesAutorizacionUsuario;
	
	/**
	 * Atributo utilizado para verificar si se muestra o no mensaje informativo
	 * para parámetro general de Via de Ingreso de la orden ambulatoria
	 */
	private boolean mensajeParametroViaIngresoOrden;
	
	/**
	 * Atributo utilizado para verificar si se muestra o no mensaje informativo
	 * para parámetro general de Via de Ingreso de la orden ambulatoria
	 */
	private boolean mensajeParametroViaIngresoPeticion;
	
	/**
	 * Atributo utilizado para verificar si se muestra o no mensaje informativo
	 * para parámetro general de Consecutivo Autorización Capitación
	 */
	private boolean mensajeConsecutivoAutorizacionCapitacion;
	
	/**
	 * Atributo utilizado para verificar si se muestra o no mensaje informativo
	 * para parámetro general de Consecutivo Autorización Entidad Subcontratada
	 */
	private boolean mensajeConsecutivoAutorizacionEntidad;
	
	/**
	 * Atributo utilizado para verificar si existen o no ordenes pendientes por
	 * autorizar
	 */
	private boolean mensajeNoOrdenesPorAutorizar;
	
	/**
	 * Atributo utilizado para verificar si se muestra o no mensaje informativo
	 * para centro de atención asignado al paciente
	 */
	private boolean mensajeCentroAtencionPaciente;
	
	/**
	 * Atributo que representa si al ingresar a la busqueda de rangos
	 * viene de paciente
	 */
	private boolean fromPacienteORango;
	
	/**
	 * Atributo que representa los niveles de autorización del usuario logueado 
	 */
	private List<NivelAutorizacionDto> nivelesAutorizacionUsuario = new ArrayList<NivelAutorizacionDto>();
	
	/**
	 * Atributo que almacena el valor del parámetro Vía de Ingreso para Validaciones de Ordenes Ambulatorias
	 */
	private String parametroViaIngresoOrdenAmbulatoria;
	
	/**
	 * Atributo que almacena el valor del parámetro Vía de Ingreso para Validaciones de Peticiondes
	 */
	private String parametroViaIngresoPeticion;
	
	/**
	 * Atributo que representa la lista de ordenes pendientes por autorizar del paciente
	 */
	private List<OrdenAutorizacionDto> ordenesPorAutorizarPaciente = new ArrayList<OrdenAutorizacionDto>();
	
	/**
	 * Atributo que representa las filtros de busqueda seleccionados
	 * para realizar la busqueda de ordenes por rango
	 */
	private ParametroBusquedaOrdenAutorizacionDto filtrosBusquedaRangos = new ParametroBusquedaOrdenAutorizacionDto(); 
	
	/**
	 * Atributo que representa si la búsqueda se esta realizando por paciente 
	 */
	private boolean porPaciente;
	
	/**
	 * Atributo que representa si la búsqueda se esta realizando por rango
	 */
	private boolean porRango;
	
	/**
	 * Atributo que representa la lista de ordenes pendientes por autorizar de acuerdo a los filtros
	 * de búsqueda seleccionados para la funcionalidad por rangos
	 */
	private List<OrdenAutorizacionDto> ordenesPorAutorizarRangos = new ArrayList<OrdenAutorizacionDto>();
	
	
	/**
	 * Atributo que representa la lista de convenios del filtro de busqueda de ordenes
	 * pendientes por autorizar por rangos
	 */
	private List<ConvenioDto> listaConvenios = new ArrayList<ConvenioDto>();
	
	/**
	 * Atributo que representa la lista de contratos asociados al convenio seleccionado
	 * del filtro de busqueda de ordenes pendientes por autorizar por rangos
	 */
	private List<ContratoDto> listaContratos = new ArrayList<ContratoDto>();
	
	/**
	 * Atributo que representa la lista de vias de ingreso del filtro de busqueda de ordenes
	 * pendientes por autorizar por rangos
	 */
	private List<ViaIngresoDto> listaViasIngreso = new ArrayList<ViaIngresoDto>();
	
	/**
	 * Atributo que representa la lista de niveles de atención del filtro de busqueda de ordenes
	 * pendientes por autorizar por rangos
	 */
	private List<NivelAtencionDto> listaNivelesAtencion = new ArrayList<NivelAtencionDto>();
	
	/**
	 * Atributo que representa la lista de clases de prdem del filtro de busqueda de ordenes
	 * pendientes por autorizar por rangos
	 */
	private List<ClaseOrdenDto> listaClasesOrden = new ArrayList<ClaseOrdenDto>();
	
	/**
	 * Atributo que representa el valor del parametro general para verificar si se muestra o no
	 * el nombre de la JSP
	 */
	private boolean mostrarRutaJsp;
	
	/**
	 * Atributo que representa el número de ordenes por autorizar
	 */
	private Integer numeroOrdenesPorAutorizar;
	
	/**
	 * Atributo que representa el valor del parámetro correspondiente al número de registros por
	 * página
	 */
	private Integer numeroRegistrosPagina;
	
	/**
	 * Atributo que representa el codigoPk del paciente asociado a la 
	 * orden seleccionada en la funcionalidad Ordenes por Rangos
	 */
	private int codigoPaciente;
	
	/**
	 * Atributo que representa el codigoPk de la 
	 * orden seleccionada en la funcionalidad Ordenes por Paciente
	 */
	private Long codigoOrden;
	
	/**
	 * Atributo que indica si la funcionalidad de Ordenes por Rango es llamada
	 * desde el menú 
	 */
	private boolean fromURL;
	
	/**
	 * Atributo para almacenar el nombre de la acción a realizar
	 */
	private String estado;
	
	/**
	 * Atributo que representa si se debe mostrar el popUp de posponer
	 * luego de realizadas las respectivas validaciones
	 */
	private boolean mostrarPopUpPosponer;
	
	/**
	 * Atributo que representa la fecha a posponer las ordenes
	 */
	private Date fechaPosponer;
	
	/**
	 * Atributo que representa las observaciones ingresadas al momento
	 * de realizar el registrod e posponer
	 */
	private String observacionesPosponer;
	
	/**
	 * Atributo que representa la lista de ordenes a posponer
	 */
	private List<OrdenAutorizacionDto> ordenesPorPosponer = new ArrayList<OrdenAutorizacionDto>();
	
	/**
	 * Atributo que representa la lista de ordenes a Autorizar
	 */
	private List<OrdenAutorizacionDto> ordenesPorAutorizar = new ArrayList<OrdenAutorizacionDto>();
	
	
	/**
	 * Atributo que representa el centro de atención asignado al paciente para las
	 * ordenes ambulatorias y peticiones
	 */
	private CentroAtencionDto centroAtencionPaciente;
	
	/**
	 * Atributo que representa el dto con la información de la autorización de capitación
	 */
	private AutorizacionCapitacionDto autorizacionCapitacion = new AutorizacionCapitacionDto();
	
	/**
	 * Atributo que representa los centros de costo a seleccionar para las ordenes ambulatorias/peticiones
	 */
	private List<CentroCostoDto> listaCentrosCosto= new ArrayList<CentroCostoDto>();
		
	/**
	 * Atributo que indica si se debe o no mostrar la lista de centros de costo
	 */
	private boolean mostrarListaCentrosCosto;
	
	/**
	 * Atributo que indica si la autorización es de servicios o de articulos
	 */
	private boolean autorizacionServicios;
	
	/**
	 * Atributo que indica si la autorización es de Medicamentos o de Insumos
	 */
	private boolean autorizacionMedicamentos;
	
	/**
	 * Atributo que indica si la autorización es de Insumos
	 */
	private boolean autorizacionInsumos;
	
	/**
	 * Atributo para verificar si se activa o no el botón Autorizar
	 */
	private boolean permiteAutorizar;
	
	
	/**
	 * Atributo que representa el tipo de entidad que ejecuta
	 */
	private String tipoEntidadEjecuta;
	
	/**
	 * Atributo que representa los centros de costo a seleccionar para las ordenes ambulatorias/peticiones
	 */
	private List<DtoCheckBox> listaTipoEntidades= new ArrayList<DtoCheckBox>();
	
	/**
	 * Atributo que representa la lista de entidades subcontratadas parametrizadas para la autorizacion
	 */
	private List<EntidadSubContratadaDto> listaEntidadesSubcontratadas= new ArrayList<EntidadSubContratadaDto>();
	
	/**
	 * Atributo que representa si se quiere o no realizar una autorización para múltiples ordenes
	 */
	private boolean autorizacionMultiple;
	
	/**
	 * Atributo que representa si se muestra o no la sección autorización
	 */
	private boolean mostrarSeccionAutorizacion;
	
	
	/**
	 * Atributo que representa la lista de montos de cobro para la autorizacion
	 */
	private List<MontoCobroDto> listaMontosCobro = new ArrayList<MontoCobroDto>();
	
	/**
	 * Atributo que representa el codigo del monto de cobro
	 */
	private int codigoMontoCobro;
	
	/**
	 * Atributo que representa el codigo del centro de costo seleccionado
	 */
	private int codigoCentroCosto;
	
	/**
	 * Atributo que representa el codigo de la entidad subcontratada seleccionada
	 */
	private long codigoEntidadSubcontratada;
	
	/**
	 * Atributo que representa la lista de reportes generados
	 */
	private String nombreReporte;
	
	/**
	 * Atributo que representa si se pudo generar el reporte PDF
	 */
	private boolean mostrarReporte;
	
	
	/**
	 * Atributo que representa el patron de ordenamiento
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo que representa si el ordenamiento es descendente
	 */
	private String esDescendente;
		
	/**
	 * Atributo que almacena la bandera que permite mostrar popup cuando es la primera impresion de la autorizacion 
	 */
	private boolean mostrarPopupPrimeraImpresion;
	
	/**
	 * Atributo que almacena la bandera que permite identificar en la impresion si es (true: original, false:copia, null:nunguna de las anteriores)
	 */
	private Boolean esImpresionOriginal;
	
	/**
	 * Atributo que almacena el nombre de la persona quien recibe la autorizacion
	 */
	private String personaRecibeAutorizacion;
	
	/**
	 * Atributo que almacena las observaciones en la entrega de la autorizacion
	 */
	private String observacionesEntregaAutorizacion;
		
	/**
	 * Método utilizado para volver a los valores iniciales las propiedades de
	 * la forma menos la de los filtros de búsqueda por rangos
	 */
	public void reset(){
		this.mensajeConsecutivoAutorizacionCapitacion=false;
		this.mensajeConsecutivoAutorizacionEntidad=false;
		this.mensajeNivelesAutorizacionUsuario=false;
		this.mensajeNoOrdenesPorAutorizar=false;
		this.mensajeParametroViaIngresoOrden=false;
		this.mensajeParametroViaIngresoPeticion=false;
		this.mensajeCentroAtencionPaciente=false;
		this.nivelesAutorizacionUsuario = new ArrayList<NivelAutorizacionDto>();
		this.parametroViaIngresoOrdenAmbulatoria = null;
		this.parametroViaIngresoPeticion = null;
		this.ordenesPorAutorizarPaciente = new ArrayList<OrdenAutorizacionDto>();
		this.porPaciente=false;
		this.porRango=false;
		this.ordenesPorAutorizarRangos = new ArrayList<OrdenAutorizacionDto>();
		this.listaConvenios = new ArrayList<ConvenioDto>();
		this.listaContratos = new ArrayList<ContratoDto>();
		this.listaViasIngreso = new ArrayList<ViaIngresoDto>();
		this.listaNivelesAtencion = new ArrayList<NivelAtencionDto>();
		this.listaClasesOrden = new ArrayList<ClaseOrdenDto>();
		this.mostrarRutaJsp = false;
		this.numeroOrdenesPorAutorizar = ConstantesBD.codigoNuncaValido;
		this.numeroRegistrosPagina = ConstantesBD.codigoNuncaValido;
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.codigoOrden = ConstantesBD.codigoNuncaValidoLong;
		this.mostrarPopUpPosponer=false;
		this.fechaPosponer=null;
		this.observacionesPosponer=null;
		this.ordenesPorPosponer = new ArrayList<OrdenAutorizacionDto>();
		this.ordenesPorAutorizar = new ArrayList<OrdenAutorizacionDto>();
		this.centroAtencionPaciente = null;
		this.autorizacionCapitacion = new AutorizacionCapitacionDto();
		this.listaCentrosCosto = new ArrayList<CentroCostoDto>();
		this.mostrarListaCentrosCosto= false;
		this.autorizacionServicios=false;
		this.permiteAutorizar = true;
		this.tipoEntidadEjecuta = null;
		this.listaTipoEntidades = new ArrayList<DtoCheckBox>();
		this.autorizacionMedicamentos=false;
		this.autorizacionInsumos=false;
		this.listaEntidadesSubcontratadas= new ArrayList<EntidadSubContratadaDto>();
		this.autorizacionMultiple=false;
		this.mostrarSeccionAutorizacion=false;
		this.listaMontosCobro= new ArrayList<MontoCobroDto>();
		this.codigoMontoCobro= ConstantesBD.codigoNuncaValido;
		this.codigoCentroCosto= ConstantesBD.codigoNuncaValido;
		this.codigoEntidadSubcontratada= ConstantesBD.codigoNuncaValidoLong;
		this.nombreReporte= null;
		this.mostrarReporte=false;
		this.esDescendente="";
		this.patronOrdenar="";
		
		this.mostrarPopupPrimeraImpresion = false;
		this.esImpresionOriginal = null;
		this.personaRecibeAutorizacion  = "";
		this.observacionesEntregaAutorizacion = "";
	}
	
	/**
	 * Método utilizado para volver a los valores iniciales las propiedades de
	 * la forma incluidos los filtros de búsqueda por rangos
	 */
	public void resetAll(){
		this.filtrosBusquedaRangos= new ParametroBusquedaOrdenAutorizacionDto();
		this.fromPacienteORango=false;
		this.reset();
	}

	/**
	 * @return the mensajeNivelesAutorizacionUsuario
	 */
	public boolean isMensajeNivelesAutorizacionUsuario() {
		return mensajeNivelesAutorizacionUsuario;
	}

	/**
	 * @param mensajeNivelesAutorizacionUsuario the mensajeNivelesAutorizacionUsuario to set
	 */
	public void setMensajeNivelesAutorizacionUsuario(
			boolean mensajeNivelesAutorizacionUsuario) {
		this.mensajeNivelesAutorizacionUsuario = mensajeNivelesAutorizacionUsuario;
	}

	/**
	 * @return the mensajeParametroViaIngresoOrden
	 */
	public boolean isMensajeParametroViaIngresoOrden() {
		return mensajeParametroViaIngresoOrden;
	}

	/**
	 * @param mensajeParametroViaIngresoOrden the mensajeParametroViaIngresoOrden to set
	 */
	public void setMensajeParametroViaIngresoOrden(
			boolean mensajeParametroViaIngresoOrden) {
		this.mensajeParametroViaIngresoOrden = mensajeParametroViaIngresoOrden;
	}

	/**
	 * @return the mensajeParametroViaIngresoPeticion
	 */
	public boolean isMensajeParametroViaIngresoPeticion() {
		return mensajeParametroViaIngresoPeticion;
	}

	/**
	 * @param mensajeParametroViaIngresoPeticion the mensajeParametroViaIngresoPeticion to set
	 */
	public void setMensajeParametroViaIngresoPeticion(
			boolean mensajeParametroViaIngresoPeticion) {
		this.mensajeParametroViaIngresoPeticion = mensajeParametroViaIngresoPeticion;
	}

	/**
	 * @return the mensajeConsecutivoAutorizacionCapitacion
	 */
	public boolean isMensajeConsecutivoAutorizacionCapitacion() {
		return mensajeConsecutivoAutorizacionCapitacion;
	}

	/**
	 * @param mensajeConsecutivoAutorizacionCapitacion the mensajeConsecutivoAutorizacionCapitacion to set
	 */
	public void setMensajeConsecutivoAutorizacionCapitacion(
			boolean mensajeConsecutivoAutorizacionCapitacion) {
		this.mensajeConsecutivoAutorizacionCapitacion = mensajeConsecutivoAutorizacionCapitacion;
	}

	/**
	 * @return the mensajeConsecutivoAutorizacionEntidad
	 */
	public boolean isMensajeConsecutivoAutorizacionEntidad() {
		return mensajeConsecutivoAutorizacionEntidad;
	}

	/**
	 * @param mensajeConsecutivoAutorizacionEntidad the mensajeConsecutivoAutorizacionEntidad to set
	 */
	public void setMensajeConsecutivoAutorizacionEntidad(
			boolean mensajeConsecutivoAutorizacionEntidad) {
		this.mensajeConsecutivoAutorizacionEntidad = mensajeConsecutivoAutorizacionEntidad;
	}

	/**
	 * @return the mensajeNoOrdenesPorAutorizar
	 */
	public boolean isMensajeNoOrdenesPorAutorizar() {
		return mensajeNoOrdenesPorAutorizar;
	}

	/**
	 * @param mensajeNoOrdenesPorAutorizar the mensajeNoOrdenesPorAutorizar to set
	 */
	public void setMensajeNoOrdenesPorAutorizar(
			boolean mensajeNoOrdenesPorAutorizar) {
		this.mensajeNoOrdenesPorAutorizar = mensajeNoOrdenesPorAutorizar;
	}

	/**
	 * @return the nivelesAutorizacionUsuario
	 */
	public List<NivelAutorizacionDto> getNivelesAutorizacionUsuario() {
		return nivelesAutorizacionUsuario;
	}

	/**
	 * @param nivelesAutorizacionUsuario the nivelesAutorizacionUsuario to set
	 */
	public void setNivelesAutorizacionUsuario(
			List<NivelAutorizacionDto> nivelesAutorizacionUsuario) {
		this.nivelesAutorizacionUsuario = nivelesAutorizacionUsuario;
	}

	/**
	 * @return the parametroViaIngresoOrdenAmbulatoria
	 */
	public String getParametroViaIngresoOrdenAmbulatoria() {
		return parametroViaIngresoOrdenAmbulatoria;
	}

	/**
	 * @param parametroViaIngresoOrdenAmbulatoria the parametroViaIngresoOrdenAmbulatoria to set
	 */
	public void setParametroViaIngresoOrdenAmbulatoria(
			String parametroViaIngresoOrdenAmbulatoria) {
		this.parametroViaIngresoOrdenAmbulatoria = parametroViaIngresoOrdenAmbulatoria;
	}

	/**
	 * @return the parametroViaIngresoPeticion
	 */
	public String getParametroViaIngresoPeticion() {
		return parametroViaIngresoPeticion;
	}

	/**
	 * @param parametroViaIngresoPeticion the parametroViaIngresoPeticion to set
	 */
	public void setParametroViaIngresoPeticion(String parametroViaIngresoPeticion) {
		this.parametroViaIngresoPeticion = parametroViaIngresoPeticion;
	}

	/**
	 * @return the ordenesPorAutorizarPaciente
	 */
	public List<OrdenAutorizacionDto> getOrdenesPorAutorizarPaciente() {
		return ordenesPorAutorizarPaciente;
	}

	/**
	 * @param ordenesPorAutorizarPaciente the ordenesPorAutorizarPaciente to set
	 */
	public void setOrdenesPorAutorizarPaciente(
			List<OrdenAutorizacionDto> ordenesPorAutorizarPaciente) {
		this.ordenesPorAutorizarPaciente = ordenesPorAutorizarPaciente;
	}

	/**
	 * @return the filtrosBusquedaRangos
	 */
	public ParametroBusquedaOrdenAutorizacionDto getFiltrosBusquedaRangos() {
		return filtrosBusquedaRangos;
	}

	/**
	 * @param filtrosBusquedaRangos the filtrosBusquedaRangos to set
	 */
	public void setFiltrosBusquedaRangos(
			ParametroBusquedaOrdenAutorizacionDto filtrosBusquedaRangos) {
		this.filtrosBusquedaRangos = filtrosBusquedaRangos;
	}

	/**
	 * @return the fromPacienteORango
	 */
	public boolean isFromPacienteORango() {
		return fromPacienteORango;
	}

	/**
	 * @param fromPacienteORango the fromPacienteORango to set
	 */
	public void setFromPacienteORango(boolean fromPacienteORango) {
		this.fromPacienteORango = fromPacienteORango;
	}

	/**
	 * @return the porPaciente
	 */
	public boolean isPorPaciente() {
		return porPaciente;
	}

	/**
	 * @param porPaciente the porPaciente to set
	 */
	public void setPorPaciente(boolean porPaciente) {
		this.porPaciente = porPaciente;
	}

	/**
	 * @return the porRango
	 */
	public boolean isPorRango() {
		return porRango;
	}

	/**
	 * @param porRango the porRango to set
	 */
	public void setPorRango(boolean porRango) {
		this.porRango = porRango;
	}

	/**
	 * @return the ordenesPorAutorizarRangos
	 */
	public List<OrdenAutorizacionDto> getOrdenesPorAutorizarRangos() {
		return ordenesPorAutorizarRangos;
	}

	/**
	 * @param ordenesPorAutorizarRangos the ordenesPorAutorizarRangos to set
	 */
	public void setOrdenesPorAutorizarRangos(
			List<OrdenAutorizacionDto> ordenesPorAutorizarRangos) {
		this.ordenesPorAutorizarRangos = ordenesPorAutorizarRangos;
	}

	/**
	 * @return the listaConvenios
	 */
	public List<ConvenioDto> getListaConvenios() {
		return listaConvenios;
	}

	/**
	 * @param listaConvenios the listaConvenios to set
	 */
	public void setListaConvenios(List<ConvenioDto> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	/**
	 * @return the listaContratos
	 */
	public List<ContratoDto> getListaContratos() {
		return listaContratos;
	}

	/**
	 * @param listaContratos the listaContratos to set
	 */
	public void setListaContratos(List<ContratoDto> listaContratos) {
		this.listaContratos = listaContratos;
	}

	/**
	 * @return the listasViasIngreso
	 */
	public List<ViaIngresoDto> getListaViasIngreso() {
		return listaViasIngreso;
	}

	/**
	 * @param listaViasIngreso the listasViasIngreso to set
	 */
	public void setListaViasIngreso(List<ViaIngresoDto> listaViasIngreso) {
		this.listaViasIngreso = listaViasIngreso;
	}

	/**
	 * @return the listaNivelesAtencion
	 */
	public List<NivelAtencionDto> getListaNivelesAtencion() {
		return listaNivelesAtencion;
	}

	/**
	 * @param listaNivelesAtencion the listaNivelesAtencion to set
	 */
	public void setListaNivelesAtencion(List<NivelAtencionDto> listaNivelesAtencion) {
		this.listaNivelesAtencion = listaNivelesAtencion;
	}

	/**
	 * @return the listaClasesOrden
	 */
	public List<ClaseOrdenDto> getListaClasesOrden() {
		return listaClasesOrden;
	}

	/**
	 * @param listaClasesOrden the listaClasesOrden to set
	 */
	public void setListaClasesOrden(List<ClaseOrdenDto> listaClasesOrden) {
		this.listaClasesOrden = listaClasesOrden;
	}

	/**
	 * @return the mostrarRutaJsp
	 */
	public boolean isMostrarRutaJsp() {
		return mostrarRutaJsp;
	}

	/**
	 * @param mostrarRutaJsp the mostrarRutaJsp to set
	 */
	public void setMostrarRutaJsp(boolean mostrarRutaJsp) {
		this.mostrarRutaJsp = mostrarRutaJsp;
	}

	/**
	 * @return the numeroOrdenesPorAutorizar
	 */
	public Integer getNumeroOrdenesPorAutorizar() {
		return numeroOrdenesPorAutorizar;
	}

	/**
	 * @param numeroOrdenesPorAutorizar the numeroOrdenesPorAutorizar to set
	 */
	public void setNumeroOrdenesPorAutorizar(Integer numeroOrdenesPorAutorizar) {
		this.numeroOrdenesPorAutorizar = numeroOrdenesPorAutorizar;
	}

	/**
	 * @return the numeroRegistrosPagina
	 */
	public Integer getNumeroRegistrosPagina() {
		return numeroRegistrosPagina;
	}

	/**
	 * @param numeroRegistrosPagina the numeroRegistrosPagina to set
	 */
	public void setNumeroRegistrosPagina(Integer numeroRegistrosPagina) {
		this.numeroRegistrosPagina = numeroRegistrosPagina;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the codigoOrden
	 */
	public Long getCodigoOrden() {
		return codigoOrden;
	}

	/**
	 * @param codigoOrden the codigoOrden to set
	 */
	public void setCodigoOrden(Long codigoOrden) {
		this.codigoOrden = codigoOrden;
	}

	/**
	 * @return the fromURL
	 */
	public boolean isFromURL() {
		return fromURL;
	}

	/**
	 * @param fromURL the fromURL to set
	 */
	public void setFromURL(boolean fromURL) {
		this.fromURL = fromURL;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the mostrarPopUpPosponer
	 */
	public boolean isMostrarPopUpPosponer() {
		return mostrarPopUpPosponer;
	}

	/**
	 * @param mostrarPopUpPosponer the mostrarPopUpPosponer to set
	 */
	public void setMostrarPopUpPosponer(boolean mostrarPopUpPosponer) {
		this.mostrarPopUpPosponer = mostrarPopUpPosponer;
	}

	/**
	 * @return the fechaPosponer
	 */
	public Date getFechaPosponer() {
		return fechaPosponer;
	}

	/**
	 * @param fechaPosponer the fechaPosponer to set
	 */
	public void setFechaPosponer(Date fechaPosponer) {
		this.fechaPosponer = fechaPosponer;
	}

	/**
	 * @return the observacionesPosponer
	 */
	public String getObservacionesPosponer() {
		return observacionesPosponer;
	}

	/**
	 * @param observacionesPosponer the observacionesPosponer to set
	 */
	public void setObservacionesPosponer(String observacionesPosponer) {
		this.observacionesPosponer = observacionesPosponer;
	}

	/**
	 * @return the ordenesPorPosponer
	 */
	public List<OrdenAutorizacionDto> getOrdenesPorPosponer() {
		return ordenesPorPosponer;
	}

	/**
	 * @param ordenesPorPosponer the ordenesPorPosponer to set
	 */
	public void setOrdenesPorPosponer(List<OrdenAutorizacionDto> ordenesPorPosponer) {
		this.ordenesPorPosponer = ordenesPorPosponer;
	}

	/**
	 * @return the ordenesPorAutorizar
	 */
	public List<OrdenAutorizacionDto> getOrdenesPorAutorizar() {
		return ordenesPorAutorizar;
	}

	/**
	 * @param ordenesPorAutorizar the ordenesPorAutorizar to set
	 */
	public void setOrdenesPorAutorizar(
			List<OrdenAutorizacionDto> ordenesPorAutorizar) {
		this.ordenesPorAutorizar = ordenesPorAutorizar;
	}

	/**
	 * @return the centroAtencionPaciente
	 */
	public CentroAtencionDto getCentroAtencionPaciente() {
		return centroAtencionPaciente;
	}

	/**
	 * @param centroAtencionPaciente the centroAtencionPaciente to set
	 */
	public void setCentroAtencionPaciente(CentroAtencionDto centroAtencionPaciente) {
		this.centroAtencionPaciente = centroAtencionPaciente;
	}

	/**
	 * @return the mensajeCentroAtencionPaciente
	 */
	public boolean isMensajeCentroAtencionPaciente() {
		return mensajeCentroAtencionPaciente;
	}

	/**
	 * @param mensajeCentroAtencionPaciente the mensajeCentroAtencionPaciente to set
	 */
	public void setMensajeCentroAtencionPaciente(
			boolean mensajeCentroAtencionPaciente) {
		this.mensajeCentroAtencionPaciente = mensajeCentroAtencionPaciente;
	}

	/**
	 * @return the autorizacionCapitacion
	 */
	public AutorizacionCapitacionDto getAutorizacionCapitacion() {
		return autorizacionCapitacion;
	}

	/**
	 * @param autorizacionCapitacion the autorizacionCapitacion to set
	 */
	public void setAutorizacionCapitacion(
			AutorizacionCapitacionDto autorizacionCapitacion) {
		this.autorizacionCapitacion = autorizacionCapitacion;
	}

	/**
	 * @return the listaCentrosCosto
	 */
	public List<CentroCostoDto> getListaCentrosCosto() {
		return listaCentrosCosto;
	}

	/**
	 * @param listaCentrosCosto the listaCentrosCosto to set
	 */
	public void setListaCentrosCosto(List<CentroCostoDto> listaCentrosCosto) {
		this.listaCentrosCosto = listaCentrosCosto;
	}

	/**
	 * @return the mostrarListaCentrosCosto
	 */
	public boolean isMostrarListaCentrosCosto() {
		return mostrarListaCentrosCosto;
	}

	/**
	 * @param mostrarListaCentrosCosto the mostrarListaCentrosCosto to set
	 */
	public void setMostrarListaCentrosCosto(boolean mostrarListaCentrosCosto) {
		this.mostrarListaCentrosCosto = mostrarListaCentrosCosto;
	}

	/**
	 * @return the autorizacionServicios
	 */
	public boolean isAutorizacionServicios() {
		return autorizacionServicios;
	}

	/**
	 * @param autorizacionServicios the autorizacionServicios to set
	 */
	public void setAutorizacionServicios(boolean autorizacionServicios) {
		this.autorizacionServicios = autorizacionServicios;
	}

	/**
	 * @return the permiteAutorizar
	 */
	public boolean isPermiteAutorizar() {
		return permiteAutorizar;
	}

	/**
	 * @param permiteAutorizar the permiteAutorizar to set
	 */
	public void setPermiteAutorizar(boolean permiteAutorizar) {
		this.permiteAutorizar = permiteAutorizar;
	}

	/**
	 * @return the tipoEntidadEjecuta
	 */
	public String getTipoEntidadEjecuta() {
		return tipoEntidadEjecuta;
	}

	/**
	 * @param tipoEntidadEjecuta the tipoEntidadEjecuta to set
	 */
	public void setTipoEntidadEjecuta(String tipoEntidadEjecuta) {
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}

	/**
	 * @return the listaTipoEntidades
	 */
	public List<DtoCheckBox> getListaTipoEntidades() {
		return listaTipoEntidades;
	}

	/**
	 * @param listaTipoEntidades the listaTipoEntidades to set
	 */
	public void setListaTipoEntidades(List<DtoCheckBox> listaTipoEntidades) {
		this.listaTipoEntidades = listaTipoEntidades;
	}

	/**
	 * @return the autorizacionMedicamentos
	 */
	public boolean isAutorizacionMedicamentos() {
		return autorizacionMedicamentos;
	}

	/**
	 * @param autorizacionMedicamentos the autorizacionMedicamentos to set
	 */
	public void setAutorizacionMedicamentos(boolean autorizacionMedicamentos) {
		this.autorizacionMedicamentos = autorizacionMedicamentos;
	}

	/**
	 * @return the autorizacionInsumos
	 */
	public boolean isAutorizacionInsumos() {
		return autorizacionInsumos;
	}

	/**
	 * @param autorizacionInsumos the autorizacionInsumos to set
	 */
	public void setAutorizacionInsumos(boolean autorizacionInsumos) {
		this.autorizacionInsumos = autorizacionInsumos;
	}

	/**
	 * @return the listaEntidadesSubcontratadas
	 */
	public List<EntidadSubContratadaDto> getListaEntidadesSubcontratadas() {
		return listaEntidadesSubcontratadas;
	}

	/**
	 * @param listaEntidadesSubcontratadas the listaEntidadesSubcontratadas to set
	 */
	public void setListaEntidadesSubcontratadas(
			List<EntidadSubContratadaDto> listaEntidadesSubcontratadas) {
		this.listaEntidadesSubcontratadas = listaEntidadesSubcontratadas;
	}

	/**
	 * @return the autorizacionMultiple
	 */
	public boolean isAutorizacionMultiple() {
		return autorizacionMultiple;
	}

	/**
	 * @param autorizacionMultiple the autorizacionMultiple to set
	 */
	public void setAutorizacionMultiple(boolean autorizacionMultiple) {
		this.autorizacionMultiple = autorizacionMultiple;
	}

	/**
	 * @return the mostrarSeccionAutorizacion
	 */
	public boolean isMostrarSeccionAutorizacion() {
		return mostrarSeccionAutorizacion;
	}

	/**
	 * @param mostrarSeccionAutorizacion the mostrarSeccionAutorizacion to set
	 */
	public void setMostrarSeccionAutorizacion(boolean mostrarSeccionAutorizacion) {
		this.mostrarSeccionAutorizacion = mostrarSeccionAutorizacion;
	}

	/**
	 * @return the listaMontosCobro
	 */
	public List<MontoCobroDto> getListaMontosCobro() {
		return listaMontosCobro;
	}

	/**
	 * @param listaMontosCobro the listaMontosCobro to set
	 */
	public void setListaMontosCobro(List<MontoCobroDto> listaMontosCobro) {
		this.listaMontosCobro = listaMontosCobro;
	}

	/**
	 * @return the codigoCentroCosto
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto the codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return the codigoEntidadSubcontratada
	 */
	public long getCodigoEntidadSubcontratada() {
		return codigoEntidadSubcontratada;
	}

	/**
	 * @param codigoEntidadSubcontratada the codigoEntidadSubcontratada to set
	 */
	public void setCodigoEntidadSubcontratada(long codigoEntidadSubcontratada) {
		this.codigoEntidadSubcontratada = codigoEntidadSubcontratada;
	}

	/**
	 * @param codigoMontoCobro the codigoMontoCobro to set
	 */
	public void setCodigoMontoCobro(int codigoMontoCobro) {
		this.codigoMontoCobro = codigoMontoCobro;
	}

	/**
	 * @return the codigoMontoCobro
	 */
	public int getCodigoMontoCobro() {
		return codigoMontoCobro;
	}

	/**
	 * @return the mostrarReporte
	 */
	public boolean isMostrarReporte() {
		return mostrarReporte;
	}

	/**
	 * @param mostrarReporte the mostrarReporte to set
	 */
	public void setMostrarReporte(boolean mostrarReporte) {
		this.mostrarReporte = mostrarReporte;
	}

	/**
	 * @return the nombreReporte
	 */
	public String getNombreReporte() {
		return nombreReporte;
	}

	/**
	 * @param nombreReporte the nombreReporte to set
	 */
	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the mostrarPopupPrimeraImpresion
	 */
	public boolean isMostrarPopupPrimeraImpresion() {
		return mostrarPopupPrimeraImpresion;
	}

	/**
	 * @param mostrarPopupPrimeraImpresion the mostrarPopupPrimeraImpresion to set
	 */
	public void setMostrarPopupPrimeraImpresion(boolean mostrarPopupPrimeraImpresion) {
		this.mostrarPopupPrimeraImpresion = mostrarPopupPrimeraImpresion;
	}

	/**
	 * @return the esImpresionOriginal
	 */
	public Boolean getEsImpresionOriginal() {
		return esImpresionOriginal;
	}

	/**
	 * @param esImpresionOriginal the esImpresionOriginal to set
	 */
	public void setEsImpresionOriginal(Boolean esImpresionOriginal) {
		this.esImpresionOriginal = esImpresionOriginal;
	}

	/**
	 * @return the personaRecibeAutorizacion
	 */
	public String getPersonaRecibeAutorizacion() {
		return personaRecibeAutorizacion;
	}

	/**
	 * @param personaRecibeAutorizacion the personaRecibeAutorizacion to set
	 */
	public void setPersonaRecibeAutorizacion(String personaRecibeAutorizacion) {
		this.personaRecibeAutorizacion = personaRecibeAutorizacion;
	}

	/**
	 * @return the observacionesEntregaAutorizacion
	 */
	public String getObservacionesEntregaAutorizacion() {
		return observacionesEntregaAutorizacion;
	}

	/**
	 * @param observacionesEntregaAutorizacion the observacionesEntregaAutorizacion to set
	 */
	public void setObservacionesEntregaAutorizacion(
			String observacionesEntregaAutorizacion) {
		this.observacionesEntregaAutorizacion = observacionesEntregaAutorizacion;
	}

}