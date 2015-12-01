package com.servinte.axioma.dto.ordenes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.ContratoDto;

import util.ConstantesBD;

/**
 * Dto para guardar las ordenes pendientes por autorizar
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class OrdenAutorizacionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2212420178757352960L;

	/**
	 * Atributo que representa el codigoPk de la orden
	 */
	private Long codigoOrden;
	
	/**
	 * Atributo que representa el consecutivo de la orden
	 */
	private String consecutivoOrden;
	
	/**
	 * Atributo que representa el consecutivo de la orden
	 */
	private Long consecutivoOrdenLong;
	
	/**
	 * Atributo que representa el tipo de la orden
	 */
	private int tipoOrden;
	
	/**
	 * Atributo que representa la clase de la orden
	 */
	private int claseOrden;
	
	/**
	 * Atributo que representa la fecha de la orden
	 */
	private Date fechaOrden;
	
	/**
	 * Atributo que representa la urgencia de la orden
	 */
	private boolean esUrgente;
	
	/**
	 * Atributo que representa el codigoPk del centro de costo que ejecuta la orden
	 */
	private int codigoCentroCostoEjecuta;
	
	/**
	 * Atributo que representa el nombre del centro de costo que ejecuta la orden
	 */
	private String nombreCentroCostoEjecuta;
	
	/**
	 * Atributo que representa el codigoPk del ingreso asociado a la orden
	 */
	private int codigoIngreso;
	
	/**
	 * Atributo que representa el consecutivo del ingreso asociado a la orden
	 */
	private String consecutivoIngreso;
	
	/**
	 * Atributo que representa la fecha a posponer la orden
	 */
	private Date fechaPosponer;
	
	/**
	 * Atributo que representa los servicios pendientes por autorizar de la orden
	 */
	private List<ServicioAutorizacionOrdenDto> serviciosPorAutorizar= new ArrayList<ServicioAutorizacionOrdenDto>();
	
	/**
	 * Atributo que representa los Medicamentos/Insumos pendientes por autorizar de la orden
	 */
	private List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosPorAutorizar= new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
	
	/**
	 * Atributo que representa si la orden tiene o no registro de posponer para mostrar la convención
	 */
	private boolean posponer;
	
	/**
	 * Atributo que representa el código de la via de ingreso
	 */
	private int codigoViaIngreso;
	
	/**
	 * Atributo que representa si a la orden se le activa o no el link de detalle
	 */
	private boolean mostrarLink;
	
	/**
	 * Atributo que representa si a la orden se le activa o no el check de posponer
	 */
	private boolean mostrarPosponer;
	
	/**
	 * Atributo que representa si a la orden se le activa o no el check de Autorizar Múltiple
	 */
	private boolean mostrarAutoMultiple;

	/**
	 * Atributo que representa si la orden es pyp
	 */
	private boolean esPyp;
	
	/**
	 * Atributo que representa el nombre de los diferentes niveles de atención asociados
	 * a los Medicamentos/Insumos ó Servicios de la orden
	 */
	private String nombreNivelesAtencion;
	
	/**
	 * Atributo que almacena el tipo de entidad que ejecuta correspondiente al centro de costo que responde la orden
	 */
	private String tipoEntidadEjecuta;
		
	/**
	 * Atributo que representa el contrato/convenio asociado a la orden
	 */
	private ContratoDto contrato= new ContratoDto();
	
	/**
	 * Atributo que permite identificar si existe una autorizacion previa para la solicitud a autorizar 
	 */
	private char migrado;
	
	/**
	 * Atributo para almacenar si la orden esta checkeada para posponer
	 */
	private boolean checkeadoPosponer;
	
	/**
	 * Atributo para almacenar si la orden esta checkeada para Autorizar Múltiple
	 */
	private boolean checkeadoAutoMultiple;
		
	/**
	 * Atributo que representa el codigoPk del paciente asociado a la orden
	 */
	private int codigoPaciente;
	/**
	 * Atributo que representa el TIPO y NUMERO de documento del paciente asociado a la orden
	 */
	private String identificacionPaciente;
	
	/**
	 * Atributo que representa los nombres y apellidos del paciente asociado a la orden
	 */
	private String nombresApellidosPaciente;
	
	/**
	 * 
	 */
	private boolean puedeAutorizar;
	
	/**
	 * Atributo que representa si la orden esta asociada a una ordenAmbulatoria o Peticion.  
	 */
	private Long otroCodigoOrden;
	
	
	public OrdenAutorizacionDto(){
		serviciosPorAutorizar = new ArrayList<ServicioAutorizacionOrdenDto>();
		medicamentosInsumosPorAutorizar = new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();

	}
	
	
	
	/**
	 * Constructor general del DTO
	 * @param codigoOrden
	 * @param consecutivoOrden
	 * @param tipoOrden
	 * @param claseOrden
	 * @param fechaOrden
	 * @param esUrgente
	 * @param codigoCentroCostoEjecuta
	 * @param nombreCentroCostoEjecuta
	 * @param codigoIngreso
	 * @param consecutivoIngreso
	 * @param fechaPosponer
	 * @param serviciosPorAutorizar
	 * @param medicamentosInsumosPorAutorizar
	 * @param posponer
	 * @param codigoViaIngreso
	 * @param mostrarLink
	 * @param mostrarPosponer
	 * @param mostrarAutoMultiple
	 * @param esPyp
	 * @param nombreNivelesAtencion
	 * @param tipoEntidadEjecuta
	 * @param contrato
	 * @param migrado
	 * @param checkeadoPosponer
	 * @param checkeadoAutoMultiple
	 * @param codigoPaciente
	 * @param identificacionPaciente
	 * @param nombresApellidosPaciente
	 * @param puedeAutorizar
	 * @param otroCodigoOrden
	 */
	public OrdenAutorizacionDto(
			Long codigoOrden,
			String consecutivoOrden,
			int tipoOrden,
			int claseOrden,
			Date fechaOrden,
			boolean esUrgente,
			int codigoCentroCostoEjecuta,
			String nombreCentroCostoEjecuta,
			int codigoIngreso,
			String consecutivoIngreso,
			Date fechaPosponer,
			List<ServicioAutorizacionOrdenDto> serviciosPorAutorizar,
			List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosPorAutorizar,
			boolean posponer, int codigoViaIngreso, boolean mostrarLink,
			boolean mostrarPosponer, boolean mostrarAutoMultiple,
			boolean esPyp, String nombreNivelesAtencion,
			String tipoEntidadEjecuta, ContratoDto contrato, char migrado,
			boolean checkeadoPosponer, boolean checkeadoAutoMultiple,
			int codigoPaciente, String identificacionPaciente,
			String nombresApellidosPaciente, boolean puedeAutorizar,
			Long otroCodigoOrden) {
		this.codigoOrden = codigoOrden;
		this.consecutivoOrden = consecutivoOrden;
		this.consecutivoOrdenLong = Long.valueOf(consecutivoOrden);
		this.tipoOrden = tipoOrden;
		this.claseOrden = claseOrden;
		this.fechaOrden = fechaOrden;
		this.esUrgente = esUrgente;
		this.codigoCentroCostoEjecuta = codigoCentroCostoEjecuta;
		this.nombreCentroCostoEjecuta = nombreCentroCostoEjecuta;
		this.codigoIngreso = codigoIngreso;
		this.consecutivoIngreso = consecutivoIngreso;
		this.fechaPosponer = fechaPosponer;
		this.serviciosPorAutorizar = serviciosPorAutorizar;
		this.medicamentosInsumosPorAutorizar = medicamentosInsumosPorAutorizar;
		this.posponer = posponer;
		this.codigoViaIngreso = codigoViaIngreso;
		this.mostrarLink = mostrarLink;
		this.mostrarPosponer = mostrarPosponer;
		this.mostrarAutoMultiple = mostrarAutoMultiple;
		this.esPyp = esPyp;
		this.nombreNivelesAtencion = nombreNivelesAtencion;
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
		this.contrato = contrato;
		this.migrado = migrado;
		this.checkeadoPosponer = checkeadoPosponer;
		this.checkeadoAutoMultiple = checkeadoAutoMultiple;
		this.codigoPaciente = codigoPaciente;
		this.identificacionPaciente = identificacionPaciente;
		this.nombresApellidosPaciente = nombresApellidosPaciente;
		this.puedeAutorizar = puedeAutorizar;
		this.otroCodigoOrden = otroCodigoOrden;
	}



	/**
	 * Constructor de la clase para cargar los datos obtenidos
	 * de la consulta de ordenes pendientes por autorizar del paciente
	 * 
	 * @param codigoOrden
	 * @param consecutivoOrden
	 * @param tipoOrden
	 * @param claseOrden
	 * @param fechaOrden
	 * @param urgente
	 * @param codigoCentroCostoEjecuta
	 * @param nombreCentroCostoEjecuta
	 * @param consecutivoIngreso
	 * @param fechaPosponer
	 */
	public OrdenAutorizacionDto(Long codigoOrden, String consecutivoOrden,
			int tipoOrden, int claseOrden, Date fechaOrden, String urgente,
			int codigoCentroCostoEjecuta, String nombreCentroCostoEjecuta,
			String tipoEntidadCentroCostoEjecuta,
			int codigoIngreso, String consecutivoIngreso, Date fechaPosponer,
			String pyp) {
		this.codigoOrden = codigoOrden;
		this.consecutivoOrden = consecutivoOrden;
		this.consecutivoOrdenLong = Long.valueOf(consecutivoOrden);
		this.tipoOrden = tipoOrden;
		this.claseOrden = claseOrden;
		this.fechaOrden = fechaOrden;
		if(urgente != null && urgente.equals(ConstantesBD.acronimoSi)){
			this.esUrgente=true;
		}
		else{
			this.esUrgente=false;
		}
		this.codigoCentroCostoEjecuta = codigoCentroCostoEjecuta;
		this.nombreCentroCostoEjecuta = nombreCentroCostoEjecuta;
		this.tipoEntidadEjecuta = tipoEntidadCentroCostoEjecuta;
		this.codigoIngreso = codigoIngreso;
		this.consecutivoIngreso = consecutivoIngreso;
		this.fechaPosponer = fechaPosponer;
		if(this.fechaPosponer != null){
			this.posponer=true;
		}
		else{
			this.posponer=false;
		}
		if(pyp != null && pyp.equals(ConstantesBD.acronimoSi)){
			this.esPyp=true;
		}
		else{
			this.esPyp=false;
		}
	}
	
	
	/**
	 * Constructor de la clase para cargar los datos obtenidos
	 * de la consulta de ordenes pendientes por autorizar por rangos
	 * 
	 * @param codigoOrden
	 * @param consecutivoOrden
	 * @param tipoOrden
	 * @param claseOrden
	 * @param fechaOrden
	 * @param urgente
	 * @param codigoCentroCostoEjecuta
	 * @param nombreCentroCostoEjecuta
	 * @param consecutivoIngreso
	 * @param fechaPosponer
	 * @param tipoIdPaciente
	 * @param numeroIdPaciente
	 * @param primerNombrePaciente
	 * @param segundoNombrePaciente
	 * @param primerApellidoPaciente
	 * @param segundoApellidoPaciente
	 */
	public OrdenAutorizacionDto(Long codigoOrden, String consecutivoOrden,
			int tipoOrden, int claseOrden, Date fechaOrden, String urgente,
			int codigoCentroCostoEjecuta, String nombreCentroCostoEjecuta,
			int codigoIngreso, String consecutivoIngreso, Date fechaPosponer,
			String pyp, int codigoPaciente, String tipoIdPaciente, String numeroIdPaciente,
			String primerNombrePaciente, String segundoNombrePaciente, 
			String primerApellidoPaciente, String segundoApellidoPaciente) {
		this.codigoOrden = codigoOrden;
		this.consecutivoOrden = consecutivoOrden;
		this.consecutivoOrdenLong = Long.valueOf(consecutivoOrden);
		this.tipoOrden = tipoOrden;
		this.claseOrden = claseOrden;
		this.fechaOrden = fechaOrden;
		if(urgente != null && urgente.equals(ConstantesBD.acronimoSi)){
			this.esUrgente=true;
		}
		else{
			this.esUrgente=false;
		}
		this.codigoCentroCostoEjecuta = codigoCentroCostoEjecuta;
		this.nombreCentroCostoEjecuta = nombreCentroCostoEjecuta;
		this.codigoIngreso = codigoIngreso;
		this.consecutivoIngreso = consecutivoIngreso;
		this.fechaPosponer = fechaPosponer;
		if(this.fechaPosponer != null){
			this.posponer=true;
		}
		else{
			this.posponer=false;
		}
		if(pyp != null && pyp.equals(ConstantesBD.acronimoSi)){
			this.esPyp=true;
		}
		else{
			this.esPyp=false;
		}
		this.codigoPaciente = codigoPaciente;
		this.identificacionPaciente=tipoIdPaciente+" "+numeroIdPaciente;
		String nombres=primerNombrePaciente+" ";
		if(segundoNombrePaciente != null){
			nombres=nombres+segundoNombrePaciente+" ";
		}
		nombres=nombres+primerApellidoPaciente+" ";
		if(segundoApellidoPaciente != null){
			nombres+=segundoApellidoPaciente;
		}
		this.nombresApellidosPaciente=nombres;
	}
	
	/**
	 * Contructor de la clase para cargar los datos consultados en el detalle de la 
	 * autorización de capitación subcontratada para las solicitudes de procedimientos
	 * @param numero_solicitud
	 * @param urgente
	 * @param valorTarifa
	 * @param codServicio
	 * @param nivelAutorizacion
	 * @param codigoContrato
	 * @param codFinalidad
	 * @param centro_costo
	 * @param viaIngreso
	 * @param tipoEntidadAutoriza
	 */
	public OrdenAutorizacionDto (int numero_solicitud, Character urgente, 
			BigDecimal valorTarifa, int codServicio, int nivelAutorizacion,
			int codigoContrato, Integer codFinalidad, int centro_costo,
			int viaIngreso, String tipoEntidadAutoriza, int codConvenio,
			int consecutivoOrden){

		this.codigoCentroCostoEjecuta = (int) centro_costo;
		this.codigoOrden = (long) numero_solicitud;
		this.consecutivoOrden = consecutivoOrden+"";
		this.codigoViaIngreso = viaIngreso;
		this.contrato.setCodigo(codigoContrato);
		this.contrato.getConvenio().setCodigo(codConvenio);
		this.puedeAutorizar = true;
		this.tipoEntidadEjecuta = tipoEntidadAutoriza;
		ServicioAutorizacionOrdenDto servicio = new ServicioAutorizacionOrdenDto();
		long cantidad=1;
		servicio.setCantidad(cantidad);
		servicio.setAutorizado(true);
		servicio.setAutorizar(true);
		servicio.setCodigo(codServicio);
		if (codFinalidad != null){
			servicio.setFinalidad(codFinalidad);
		}
		NivelAutorizacionDto nivelAutoriza = new NivelAutorizacionDto();
		nivelAutoriza.setCodigo(nivelAutorizacion);
		servicio.setNivelAutorizacion(nivelAutoriza);
		servicio.setPuedeAutorizar(true);
		servicio.setValorTarifa(valorTarifa);
		this.serviciosPorAutorizar.add(servicio);
	}
	
	/**
	 * Contructor de la clase para cargar los datos consultados en el detalle de la 
	 * autorización de capitación subcontratada para las ordenes ambulatorias
	 * @param numero_solicitud
	 * @param urgente
	 * @param valorTarifa
	 * @param codServicio
	 * @param nivelAutorizacion
	 * @param codigoContrato
	 * @param codFinalidad
	 * @param centro_costo
	 * @param viaIngreso
	 * @param tipoEntidadAutoriza
	 */
	public OrdenAutorizacionDto (long numero_solicitud, 
			BigDecimal valorTarifa, int codServicio, int nivelAutorizacion,
			int codigoContrato, Integer codFinalidad, int centro_costo,
			int viaIngreso, String tipoEntidadAutoriza, int codConvenio,
			String consecutivoOrden){

		this.codigoCentroCostoEjecuta = (int) centro_costo;
		this.codigoOrden = numero_solicitud;
		this.consecutivoOrden = consecutivoOrden;
		this.codigoViaIngreso = viaIngreso;
		this.contrato.setCodigo(codigoContrato);
		this.contrato.getConvenio().setCodigo(codConvenio);
		this.puedeAutorizar = true;
		this.tipoEntidadEjecuta = tipoEntidadAutoriza;
		ServicioAutorizacionOrdenDto servicio = new ServicioAutorizacionOrdenDto();
		long cantidad=1;
		servicio.setCantidad(cantidad);
		servicio.setAutorizado(true);
		servicio.setAutorizar(true);
		servicio.setCodigo(codServicio);
		if (codFinalidad != null){
			servicio.setFinalidad(codFinalidad);
		}
		NivelAutorizacionDto nivelAutoriza = new NivelAutorizacionDto();
		nivelAutoriza.setCodigo(nivelAutorizacion);
		servicio.setNivelAutorizacion(nivelAutoriza);
		servicio.setPuedeAutorizar(true);
		servicio.setValorTarifa(valorTarifa);
		this.serviciosPorAutorizar.add(servicio);
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
	 * @return the consecutivoOrden
	 */
	public String getConsecutivoOrden() {
		return consecutivoOrden;
	}

	/**
	 * @param consecutivoOrden the consecutivoOrden to set
	 */
	public void setConsecutivoOrden(String consecutivoOrden) {
		this.consecutivoOrden = consecutivoOrden;
	}

	/**
	 * @return the tipoOrden
	 */
	public int getTipoOrden() {
		return tipoOrden;
	}

	/**
	 * @param tipoOrden the tipoOrden to set
	 */
	public void setTipoOrden(int tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	/**
	 * @return the claseOrden
	 */
	public int getClaseOrden() {
		return claseOrden;
	}

	/**
	 * @param claseOrden the claseOrden to set
	 */
	public void setClaseOrden(int claseOrden) {
		this.claseOrden = claseOrden;
	}

	/**
	 * @return the fechaOrden
	 */
	public Date getFechaOrden() {
		return fechaOrden;
	}

	/**
	 * @param fechaOrden the fechaOrden to set
	 */
	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	/**
	 * @return the urgente
	 */
	public boolean getEsUrgente() {
		return esUrgente;
	}

	/**
	 * @param urgente the urgente to set
	 */
	public void setEsUrgente(boolean esUrgente) {
		this.esUrgente = esUrgente;
	}

	/**
	 * @return the codigoCentroCostoEjecuta
	 */
	public int getCodigoCentroCostoEjecuta() {
		return codigoCentroCostoEjecuta;
	}

	/**
	 * @param codigoCentroCostoEjecuta the codigoCentroCostoEjecuta to set
	 */
	public void setCodigoCentroCostoEjecuta(int codigoCentroCostoEjecuta) {
		this.codigoCentroCostoEjecuta = codigoCentroCostoEjecuta;
	}

	/**
	 * @return the nombreCentroCostoEjecuta
	 */
	public String getNombreCentroCostoEjecuta() {
		return nombreCentroCostoEjecuta;
	}

	/**
	 * @param nombreCentroCostoEjecuta the nombreCentroCostoEjecuta to set
	 */
	public void setNombreCentroCostoEjecuta(String nombreCentroCostoEjecuta) {
		this.nombreCentroCostoEjecuta = nombreCentroCostoEjecuta;
	}

	/**
	 * @return the consecutivoIngreso
	 */
	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}

	/**
	 * @param consecutivoIngreso the consecutivoIngreso to set
	 */
	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
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
	 * @return the serviciosPorAutorizar
	 */
	public List<ServicioAutorizacionOrdenDto> getServiciosPorAutorizar() {
		return serviciosPorAutorizar;
	}

	/**
	 * @param serviciosPorAutorizar the serviciosPorAutorizar to set
	 */
	public void setServiciosPorAutorizar(
			List<ServicioAutorizacionOrdenDto> serviciosPorAutorizar) {
		this.serviciosPorAutorizar = serviciosPorAutorizar;
	}

	/**
	 * @return the medicamentosInusmosPorAutorizar
	 */
	public List<MedicamentoInsumoAutorizacionOrdenDto> getMedicamentosInsumosPorAutorizar() {
		return medicamentosInsumosPorAutorizar;
	}

	/**
	 * @param medicamentosInusmosPorAutorizar the medicamentosInusmosPorAutorizar to set
	 */
	public void setMedicamentosInsumosPorAutorizar(
			List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosPorAutorizar) {
		this.medicamentosInsumosPorAutorizar = medicamentosInsumosPorAutorizar;
	}

	/**
	 * @return the posponer
	 */
	public boolean isPosponer() {
		return posponer;
	}

	/**
	 * @param posponer the posponer to set
	 */
	public void setPosponer(boolean posponer) {
		this.posponer = posponer;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the codigoIngreso
	 */
	public int getCodigoIngreso() {
		return codigoIngreso;
	}

	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * @return the mostrarLink
	 */
	public boolean isMostrarLink() {
		return mostrarLink;
	}

	/**
	 * @param mostrarLink the mostrarLink to set
	 */
	public void setMostrarLink(boolean mostrarLink) {
		this.mostrarLink = mostrarLink;
	}

	/**
	 * @return the mostrarPosponer
	 */
	public boolean isMostrarPosponer() {
		return mostrarPosponer;
	}

	/**
	 * @param mostrarPosponer the mostrarPosponer to set
	 */
	public void setMostrarPosponer(boolean mostrarPosponer) {
		this.mostrarPosponer = mostrarPosponer;
	}

	/**
	 * @return the mostrarAutoMultiple
	 */
	public boolean isMostrarAutoMultiple() {
		return mostrarAutoMultiple;
	}

	/**
	 * @param mostrarAutoMultiple the mostrarAutoMultiple to set
	 */
	public void setMostrarAutoMultiple(boolean mostrarAutoMultiple) {
		this.mostrarAutoMultiple = mostrarAutoMultiple;
	}

	/**
	 * @return the esPyp
	 */
	public boolean isEsPyp() {
		return esPyp;
	}

	/**
	 * @param esPyp the esPyp to set
	 */
	public void setEsPyp(boolean esPyp) {
		this.esPyp = esPyp;
	}

	/**
	 * @return the nombreNivelesAtencion
	 */
	public String getNombreNivelesAtencion() {
		return nombreNivelesAtencion;
	}

	/**
	 * @param nombreNivelesAtencion the nombreNivelesAtencion to set
	 */
	public void setNombreNivelesAtencion(String nombreNivelesAtencion) {
		this.nombreNivelesAtencion = nombreNivelesAtencion;
	}
	
	/**
	 * @return tipoEntidadEjecuta
	 */
	public String getTipoEntidadEjecuta() {
		return tipoEntidadEjecuta;
	}
	
	/**
	 * @param tipoEntidadEjecuta
	 */
	public void setTipoEntidadEjecuta(String tipoEntidadEjecuta) {
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}
	
		
	/**
	 * @return migrado 
	 */
	public char getMigrado() {
		return migrado;
	}
	
	/**
	 * @param migrado
	 */
	public void setMigrado(char migrado) {
		this.migrado = migrado;
	}

	/**
	 * @return the checkeadoPosponer
	 */
	public boolean isCheckeadoPosponer() {
		return checkeadoPosponer;
	}

	/**
	 * @param checkeadoPosponer the checkeadoPosponer to set
	 */
	public void setCheckeadoPosponer(boolean checkeadoPosponer) {
		this.checkeadoPosponer = checkeadoPosponer;
	}

	/**
	 * @return the checkeadoAutoMultiple
	 */
	public boolean isCheckeadoAutoMultiple() {
		return checkeadoAutoMultiple;
	}

	/**
	 * @param checkeadoAutoMultiple the checkeadoAutoMultiple to set
	 */
	public void setCheckeadoAutoMultiple(boolean checkeadoAutoMultiple) {
		this.checkeadoAutoMultiple = checkeadoAutoMultiple;
	}

	/**
	 * @return the identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}

	/**
	 * @param identificacionPaciente the identificacionPaciente to set
	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}

	/**
	 * @return the nombresApellidosPaciente
	 */
	public String getNombresApellidosPaciente() {
		return nombresApellidosPaciente;
	}

	/**
	 * @param nombresApellidosPaciente the nombresApellidosPaciente to set
	 */
	public void setNombresApellidosPaciente(String nombresApellidosPaciente) {
		this.nombresApellidosPaciente = nombresApellidosPaciente;
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
	 * @return the contratos
	 */
	public ContratoDto getContrato() {
		return contrato;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContrato(ContratoDto contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the puedeAutorizar
	 */
	public boolean isPuedeAutorizar() {
		return puedeAutorizar;
	}

	/**
	 * @param puedeAutorizar the puedeAutorizar to set
	 */
	public void setPuedeAutorizar(boolean puedeAutorizar) {
		this.puedeAutorizar = puedeAutorizar;
	}

	/**
	 * @return otroCodigoOrden
	 */
	public Long getOtroCodigoOrden() {
		return otroCodigoOrden;
	}

	/**
	 * @param otroCodigoOrden
	 */
	public void setOtroCodigoOrden(Long otroCodigoOrden) {
		this.otroCodigoOrden = otroCodigoOrden;
	}



	/**
	 * @return the consecutivoOrdenLong
	 */
	public Long getConsecutivoOrdenLong() {
		return consecutivoOrdenLong;
	}



	/**
	 * @param consecutivoOrdenLong the consecutivoOrdenLong to set
	 */
	public void setConsecutivoOrdenLong(Long consecutivoOrdenLong) {
		this.consecutivoOrdenLong = consecutivoOrdenLong;
	}

}