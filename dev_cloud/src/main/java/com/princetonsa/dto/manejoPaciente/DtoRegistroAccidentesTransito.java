/*
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * DTO para la funcionalidad Registro Accidentes Transito.
 * 
 * 
 * @author Jorge Armando Osorio Velasquez.
 * 
 *
 */
public class DtoRegistroAccidentesTransito implements Serializable
{
	
	/**
	 * Codigo del registro de accidentes de transito
	 */
	private String codigo;
	
	/**
	 * codigo del ingreso relacionado al registro.
	 */
	private String ingreso;
	
	/**
	 * Empresa donde trabaja la persona accidentada
	 */
	private String empresaTrabaja;
	
	/**
	 * Direccion de la empresa;
	 */
	private String direccionEmpresa;
	
	/**
	 * Telefono de la empresa.
	 */
	private String telefonoEmpresa;
	
	/**
	 * Ciudad Empresa
	 */
	private String ciudadEmpresa;
	
	/**
	 * Departamento Empresa.
	 */
	private String departamentoEmpresa;
	
	/**
	 * Indica si la persona es ocupante o peaton Valores posibres S - N
	 */
	private String ocupante;
	
	/**
	 * Hace referencia al la codiccion accidentado referida al documento, valores posibles (AVEH - AMOT - APEA - ACIC).
	 */
	private String codicionAccidentado;
	
	/**
	 * Descripcion de como resulta lesionado.
	 */
	private String resultaLesionadoAl;
	
	/**
	 * En caso de que el accidente sea ocacionado por otro vehiculo, este
	 * atributo contiene la placa.
	 * Este atributo solo tiene valor si el atributo resultaLesionadoAl es AECV (Estrillarse con Vehiculo).
	 */
	private String placaVehiculoOcaciona;
	
	/**
	 * Descripcion del lugar del accidente;
	 */
	private String lugarAccidente;
	
	/**
	 * Fecha del accidente
	 */
	private String fechaAccidente;
	
	/**
	 * Hora del Accidente;
	 */
	private String horaAccidente;
	
	/**
	 * Ciudad del Accidente.
	 */
	private String ciudadAccidente;
	
	/**
	 * Departamento del Accidente
	 */
	private String departamentoAccidente;
	
	/**
	 * Zona del Accidente;
	 */
	private String zonaAccidente;
	
	/**
	 * Informacion del accidente (Descripcion de los Hechos).
	 */
	private String informacionAccidente;
	
	/**
	 * Marca del vehiculo del accidente.
	 */
	private String marcaVehiculo;
	
	/**
	 * Placa del vehiculo del accidente
	 */
	private String placa;
	
	/**
	 * Tipo de vehiculo.
	 */
	private String tipo;
	
	/**
	 * Empresa aseguradora del vehiculo, hace referencia a convenios.
	 */
	private String aseguradora;
	
	
	//////////////////////INFORMACION QUE SE RELACIONAD EN EL ANEX 383 - CAMBIOS EN OTRAS FUNCIONALIDES X FOSYGA
    //datos del propietario
	private String primerApellidoProp;
	private String segundoApellidoProp;
	private String primerNombreProp;
	private String segundoNombreProp;
	private String tipoIdProp;
	private String numeroIdProp;
	private String ciudadExpedicionIdProp;
	private String deptoExpedicionIdProp;
	private String direccionProp;
	private String telefonoProp;
	private String ciudadProp;
	private String deptoProp;
	
	
	private String apellidoNombreTransporta;
	private String tipoIdTransporta;
	private String numeroIdTransporta;
	private String ciudadExpedicionIdTransporta;
	private String deptoExpedicionIdTransporta;
	private String telefonoTransporta;
	private String direccionTransporta;
	private String ciudadTransporta;
	private String departamentoTransporta;
	private String transportaVictimaDesde;
	private String transportaVictimaHasta; 
	private String tipoTransporte;
	private String placaVehiculoTransporta;
	
	
	/**
	 * Nombre de la sucursal que expidio el nombre de la sucursal.
	 */
	private String agencia;
	
	/**
	 * Numero de poliza SOAT.
	 */
	private String numeroPoliza;
	
	/**
	 * Campo que indica si el vehiculo esta asegurado. valores posibles (S - N - FAN);
	 */
	private String asegurado;
	
	
	/**
	 * Campo para indicar la poliza. valores posibles(Vigente - Falsa - Vencida)
	 */
	private String poliza;
	
	/**
	 * Fecha inicial de vigencia de la poliza en caso de tenerla
	 */
	private String fechaInicialPoliza;
	
	/**
	 * Fecha final de la poliza en caso de tenerla.
	 */
	private String fechaFinalPoliza;
	
	/**
	 * 
	 */
	private String primerApellidoConductor;
	
	/**
	 * 
	 */
	private String segundoApellidoConductor;
	

	/**
	 * 
	 */
	private String primerNombreConductor;
	
	/**
	 * 
	 */
	private String segundoNombreConductor;
	
	/**
	 * Tipo de Identificacion del conductor
	 */
	private String tipoIdConductor;
	
	/**
	 * Numero Identificacion del Conductor.
	 */
	private String numeroIdConductor;
	
	/**
	 * Ciudad de expedicion de identificacion del conductor
	 */
	private String ciuExpedicionIdConductor;
	
	/**
	 * Departamento de expedicion de identificacion del conductor
	 */
	private String depExpedicionIdConductor;
	
	/**
	 * Dirreccion de residencia del conductor
	 */
	private String direccionConductor;
	
	/**
	 * Cidad de residencia del conductor.
	 */
	private String ciudadConductor;
	
	/**
	 * Departamento de residencia del conductor.
	 */
	private String departamentoConductor;
	
	/**
	 * Telefono del conductor
	 */
	private String telefonoConductor;
	
	/**
	 * Fecha de grabacion del registro..
	 */
	private String fechaGrabacion;
	
	/**
	 * Hora Grabacion del Registro.
	 */
	private String horaGrabacion;
	
	/**
	 * Usuario que graba el registro.
	 */
	private String usuarioGrabacion;
	
	/**
	 * Estado del registro de accidente de transito, valores posibles (PRO - PEN )
	 */
	private String estadoRegistro;
	
	
	/**
	 * 
	 */
	private String apellidoNombreDeclarante;
	
	/**
	 * 
	 */
	private String tipoIdDeclarante;
	
	private String numeroIdDeclarante;
	
	/**
	 * 
	 */
	private String ciuExpedicionIdDeclarante;
	
	/**
	 * 
	 */
	private String depExpedicionIdDeclarante;
	
	/**
	 * Fecha de anulacion
	 */
	private String fechaAnulacion;
	
	/**
	 * Hora de anulacion
	 */
	private String horaAnulacion;
	
	/**
	 * Usuario Anulacion
	 */
	private String usuarioAnulacion;
	
	/**
	 * 
	 */
	private String paisEmpresa;
	
	/**
	 * 
	 */
	private String paisAccidente;
	
	/**
	 * 
	 */
	private String paisExpedicionIdConductor;
	
	/**
	 * 
	 */
	private String paisConductor;
	
	/**
	 * 
	 */
	private String paisExpedicionIdDeclarante;
	
	/**
	 * 
	 */
	private String paisExpedicionIdProp;
	
	/**
	 * 
	 */
	private String paisExpedicionIdTransporta;
	
	/**
	 * 
	 */
	private String paisProp;
	
	/**
	 * 
	 */
	private String paisTransporta;
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//cambios por anexo 722 cambios por resolucion 1915/08
private String descripcionBreveOcurrencia="";

private String otroTipoServicioVehiculo="";

private String intervencionAutoridad="";

private String cobroExcedentePoliza="";

private int cantidadOtrosVehiAcc=0;

private String existenOtrosVehiAcc=ConstantesBD.acronimoNo;


private String placa2Vehiculo="";

private String tipoId2Vehiculo="";

private String nroId2Vehiculo="";

private String placa3Vehiculo="";

private String tipoId3Vehiculo="";

private String nroId3Vehiculo="";



private String tipoReferenciaRem="";
private String fechaRem="";
private String horaRem="";
private String prestadorRem="";

private String profesionalRem="";
private String cargoProfesionalRem="";
private String fechaAceptacion="";
private String horaAceptacion="";
private String prestadorRecibe="";

private String profesionalRecibe="";
private String cargoProfesionalRecibe="";

private String otroTipoTrans="";

private String zonaTransporte="";

private int institucion=ConstantesBD.codigoNuncaValido;

private double totalFacAmpQx=ConstantesBD.codigoNuncaValidoDouble;
private double totalReclamoAmpQx=ConstantesBD.codigoNuncaValidoDouble;
private double totalFacAmpTx=ConstantesBD.codigoNuncaValidoDouble;
private double totalReclamoAmpTx=ConstantesBD.codigoNuncaValidoDouble;

private String esReclamacion="";
private String furips="";
private String furtran="";
private String respGlosa="";
private String nroRadicadoAnterior="";
private long nroConsReclamacion=0;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


public String getEsReclamacion() {
	return esReclamacion;
}


public void setEsReclamacion(String esReclamacion) {
	this.esReclamacion = esReclamacion;
}


public String getFurips() {
	return furips;
}


public void setFurips(String furips) {
	this.furips = furips;
}


public String getFurtran() {
	return furtran;
}


public void setFurtran(String furtran) {
	this.furtran = furtran;
}


public String getRespGlosa() {
	return respGlosa;
}


public void setRespGlosa(String respGlosa) {
	this.respGlosa = respGlosa;
}


public String getNroRadicadoAnterior() {
	return nroRadicadoAnterior;
}


public void setNroRadicadoAnterior(String nroRadicadoAnterior) {
	this.nroRadicadoAnterior = nroRadicadoAnterior;
}


public double getTotalFacAmpQx() {
	return totalFacAmpQx;
}


public long getNroConsReclamacion() {
	return nroConsReclamacion;
}


public void setNroConsReclamacion(long nroConsReclamacion) {
	this.nroConsReclamacion = nroConsReclamacion;
}


public void setTotalFacAmpQx(double totalFacAmpQx) {
	this.totalFacAmpQx = totalFacAmpQx;
}


public double getTotalReclamoAmpQx() {
	return totalReclamoAmpQx;
}


public void setTotalReclamoAmpQx(double totalReclamoAmpQx) {
	this.totalReclamoAmpQx = totalReclamoAmpQx;
}


public double getTotalFacAmpTx() {
	return totalFacAmpTx;
}


public void setTotalFacAmpTx(double totalFacAmpTx) {
	this.totalFacAmpTx = totalFacAmpTx;
}


public double getTotalReclamoAmpTx() {
	return totalReclamoAmpTx;
}


public void setTotalReclamoAmpTx(double totalReclamoAmpTx) {
	this.totalReclamoAmpTx = totalReclamoAmpTx;
}


public String getOtroTipoTrans() {
	return otroTipoTrans;
}


public void setOtroTipoTrans(String otroTipoTrans) {
	this.otroTipoTrans = otroTipoTrans;
}


public String getZonaTransporte() {
	return zonaTransporte;
}


public void setZonaTransporte(String zonaTransporte) {
	this.zonaTransporte = zonaTransporte;
}

	public String getTipoReferenciaRem() {
	return tipoReferenciaRem;
}


public void setTipoReferenciaRem(String tipoReferenciaRem) {
	this.tipoReferenciaRem = tipoReferenciaRem;
}


public String getFechaRem() {
	return fechaRem;
}


public void setFechaRem(String fechaRem) {
	this.fechaRem = fechaRem;
}


public String getHoraRem() {
	return horaRem;
}


public void setHoraRem(String horaRem) {
	this.horaRem = horaRem;
}


public String getPrestadorRem() {
	return prestadorRem;
}


public void setPrestadorRem(String prestadorRem) {
	this.prestadorRem = prestadorRem;
}


public String getProfesionalRem() {
	return profesionalRem;
}


public void setProfesionalRem(String profesionalRem) {
	this.profesionalRem = profesionalRem;
}


public String getCargoProfesionalRem() {
	return cargoProfesionalRem;
}


public void setCargoProfesionalRem(String cargoProfesionalRem) {
	this.cargoProfesionalRem = cargoProfesionalRem;
}


public String getFechaAceptacion() {
	return fechaAceptacion;
}


public void setFechaAceptacion(String fechaAceptacion) {
	this.fechaAceptacion = fechaAceptacion;
}


public String getHoraAceptacion() {
	return horaAceptacion;
}


public void setHoraAceptacion(String horaAceptacion) {
	this.horaAceptacion = horaAceptacion;
}


public String getPrestadorRecibe() {
	return prestadorRecibe;
}


public void setPrestadorRecibe(String prestadorRecibe) {
	this.prestadorRecibe = prestadorRecibe;
}

public String getProfesionalRecibe() {
	return profesionalRecibe;
}


public void setProfesionalRecibe(String profesionalRecibe) {
	this.profesionalRecibe = profesionalRecibe;
}


public String getCargoProfesionalRecibe() {
	return cargoProfesionalRecibe;
}


public void setCargoProfesionalRecibe(String cargoProfesionalRecibe) {
	this.cargoProfesionalRecibe = cargoProfesionalRecibe;
}


	public String getIntervencionAutoridad() {
	return intervencionAutoridad;
}


public void setIntervencionAutoridad(String intervencionAutoridad) {
	this.intervencionAutoridad = intervencionAutoridad;
}


public String getCobroExcedentePoliza() {
	return cobroExcedentePoliza;
}


public void setCobroExcedentePoliza(String cobroExcedentePoliza) {
	this.cobroExcedentePoliza = cobroExcedentePoliza;
}


	/**
	 * Constructor del DTO que llama al metodo reset de la clase.
	 *
	 */
	public DtoRegistroAccidentesTransito()
	{
		this.reset();
	}

	
	/**
	 * Metodo que resetea los atributos del DTO
	 *
	 */
	private void reset() 
	{
		this.codigo="";
		this.ingreso="";
		this.empresaTrabaja="";
		this.direccionEmpresa="";
		this.telefonoEmpresa="";
		this.ciudadEmpresa="";
		this.departamentoEmpresa="";
		this.ocupante="";
		this.codicionAccidentado="";
		this.resultaLesionadoAl="";
		this.placaVehiculoOcaciona="";
		this.lugarAccidente="";
		this.fechaAccidente="";
		this.horaAccidente="";
		this.ciudadAccidente="";
		this.departamentoAccidente="";
		this.zonaAccidente="";
		this.informacionAccidente="";
		this.marcaVehiculo="";
		this.placa="";
		this.tipo="";
		this.aseguradora="";
		this.poliza="";
		this.agencia="";
		this.numeroPoliza="";
		this.asegurado="";
		this.fechaInicialPoliza="";
		this.fechaFinalPoliza="";
		this.primerApellidoConductor="";
		this.segundoNombreConductor="";
		this.primerApellidoConductor="";
		this.segundoNombreConductor="";
		this.tipoIdConductor="";
		this.numeroIdConductor="";
		this.ciuExpedicionIdConductor="";
		this.depExpedicionIdConductor="";
		this.direccionConductor="";
		this.ciudadConductor="";
		this.departamentoConductor="";
		this.telefonoConductor="";
		this.fechaGrabacion="";
		this.horaGrabacion="";
		this.usuarioGrabacion="";
		this.estadoRegistro="";
		this.apellidoNombreDeclarante="";
		this.tipoIdDeclarante="";
		this.numeroIdDeclarante="";
		this.ciuExpedicionIdDeclarante="";
		this.depExpedicionIdDeclarante="";
		this.fechaAnulacion  = "";
		this.horaAnulacion = "";
		this.usuarioAnulacion = "";
    	this.primerApellidoProp="";
    	this.segundoApellidoProp="";
    	this.primerNombreProp="";
    	this.segundoNombreProp="";
    	this.tipoIdProp="";
    	this.numeroIdProp="";
    	this.ciudadExpedicionIdProp="";
    	this.deptoExpedicionIdProp="";
    	this.direccionProp="";
    	this.telefonoProp="";
    	this.ciudadProp="";
    	this.deptoProp="";
    	this.apellidoNombreTransporta="";
    	this.tipoIdTransporta="";
    	this.numeroIdTransporta="";
    	this.ciudadExpedicionIdTransporta="";
    	this.deptoExpedicionIdTransporta="";
    	this.telefonoTransporta="";
    	this.direccionTransporta="";
    	this.ciudadTransporta="";
    	this.departamentoTransporta="";
    	this.transportaVictimaDesde="";
    	this.transportaVictimaHasta=""; 
    	this.tipoTransporte="";
    	this.placaVehiculoTransporta="";
    	this.paisEmpresa="";
    	this.paisAccidente="";
    	this.paisExpedicionIdConductor="";
    	this.paisConductor="";
    	this.paisExpedicionIdDeclarante="";
    	this.paisExpedicionIdProp="";
    	this.paisExpedicionIdTransporta="";
    	this.paisProp="";
    	this.paisTransporta="";
    	this.descripcionBreveOcurrencia="";
    	this.otroTipoServicioVehiculo="";
    	this.intervencionAutoridad="";
    	this.cobroExcedentePoliza="";
    	this.cantidadOtrosVehiAcc=0;
    	this.existenOtrosVehiAcc=ConstantesBD.acronimoNo;
    	this.placa2Vehiculo="";
    	this.tipoId2Vehiculo="";
    	this.nroId2Vehiculo="";
    	this.placa3Vehiculo="";
    	this.tipoId3Vehiculo="";
    	this.nroId3Vehiculo="";
    	this.tipoReferenciaRem="";
    	this.fechaRem="";
    	this.horaRem="";
    	this.prestadorRem="";
    	this.profesionalRem="";
    	this.cargoProfesionalRem="";
    	this.fechaAceptacion="";
    	this.horaAceptacion="";
    	this.prestadorRecibe="";
    	this.profesionalRecibe="";
    	this.cargoProfesionalRecibe="";
    	this.otroTipoTrans="";
    	this.zonaTransporte="";
    	this.institucion=ConstantesBD.codigoNuncaValido;
    	this.totalFacAmpQx=ConstantesBD.codigoNuncaValidoDouble;
    	this.totalFacAmpTx=ConstantesBD.codigoNuncaValidoDouble;
    	this.totalReclamoAmpQx=ConstantesBD.codigoNuncaValidoDouble;
    	this.totalReclamoAmpTx=ConstantesBD.codigoNuncaValidoDouble;
    	this.esReclamacion="";
    	this.furips="";
    	this.furtran="";
    	this.respGlosa="";
    	this.nroRadicadoAnterior="";
    	this.nroConsReclamacion=0;
	}

	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	public String getPlaca2Vehiculo() {
		return placa2Vehiculo;
	}


	public void setPlaca2Vehiculo(String placa2Vehiculo) {
		this.placa2Vehiculo = placa2Vehiculo;
	}


	public String getTipoId2Vehiculo() {
		return tipoId2Vehiculo;
	}


	public void setTipoId2Vehiculo(String tipoId2Vehiculo) {
		this.tipoId2Vehiculo = tipoId2Vehiculo;
	}


	public String getNroId2Vehiculo() {
		return nroId2Vehiculo;
	}


	public void setNroId2Vehiculo(String nroId2Vehiculo) {
		this.nroId2Vehiculo = nroId2Vehiculo;
	}


	public String getPlaca3Vehiculo() {
		return placa3Vehiculo;
	}


	public void setPlaca3Vehiculo(String placa3Vehiculo) {
		this.placa3Vehiculo = placa3Vehiculo;
	}


	public String getTipoId3Vehiculo() {
		return tipoId3Vehiculo;
	}


	public void setTipoId3Vehiculo(String tipoId3Vehiculo) {
		this.tipoId3Vehiculo = tipoId3Vehiculo;
	}


	public String getNroId3Vehiculo() {
		return nroId3Vehiculo;
	}


	public void setNroId3Vehiculo(String nroId3Vehiculo) {
		this.nroId3Vehiculo = nroId3Vehiculo;
	}


	public String getExistenOtrosVehiAcc() {
		return existenOtrosVehiAcc;
	}


	public void setExistenOtrosVehiAcc(String existenOtrosVehiAcc) {
		this.existenOtrosVehiAcc = existenOtrosVehiAcc;
	}


	public int getCantidadOtrosVehiAcc() {
		return cantidadOtrosVehiAcc;
	}


	public void setCantidadOtrosVehiAcc(int cantidadOtrosVehiAcc) {
		this.cantidadOtrosVehiAcc = cantidadOtrosVehiAcc;
	}


	public String getAgencia()
	{
		return agencia;
	}

	public void setAgencia(String agencia)
	{
		this.agencia = agencia;
	}

	public String getAsegurado()
	{
		return asegurado;
	}

	public void setAsegurado(String asegurado)
	{
		this.asegurado = asegurado;
	}

	public String getAseguradora()
	{
		return aseguradora;
	}

	public void setAseguradora(String aseguradora)
	{
		this.aseguradora = aseguradora;
	}

	public String getCiudadAccidente()
	{
		return ciudadAccidente;
	}

	public void setCiudadAccidente(String ciudadAccidente)
	{
		this.ciudadAccidente = ciudadAccidente;
	}

	public String getCiudadEmpresa()
	{
		return ciudadEmpresa;
	}

	public void setCiudadEmpresa(String ciudadEmpresa)
	{
		this.ciudadEmpresa = ciudadEmpresa;
	}

	public String getCiuExpedicionIdConductor()
	{
		return ciuExpedicionIdConductor;
	}

	public void setCiuExpedicionIdConductor(String ciuExpedicionIdConductor)
	{
		this.ciuExpedicionIdConductor = ciuExpedicionIdConductor;
	}

	public String getCodigo()
	{
		return codigo;
	}

	public void setCodigo(String codigo)
	{
		this.codigo = codigo;
	}

	public String getDepartamentoAccidente()
	{
		return departamentoAccidente;
	}

	public void setDepartamentoAccidente(String departamentoAccidente)
	{
		this.departamentoAccidente = departamentoAccidente;
	}

	public String getDepartamentoEmpresa()
	{
		return departamentoEmpresa;
	}

	public void setDepartamentoEmpresa(String departamentoEmpresa)
	{
		this.departamentoEmpresa = departamentoEmpresa;
	}

	public String getDepExpedicionIdConductor()
	{
		return depExpedicionIdConductor;
	}

	public void setDepExpedicionIdConductor(String depExpedicionIdConductor)
	{
		this.depExpedicionIdConductor = depExpedicionIdConductor;
	}

	public String getDireccionConductor()
	{
		return direccionConductor;
	}

	public void setDireccionConductor(String direccionConductor)
	{
		this.direccionConductor = direccionConductor;
	}

	public String getDireccionEmpresa()
	{
		return direccionEmpresa;
	}

	public void setDireccionEmpresa(String direccionEmpresa)
	{
		this.direccionEmpresa = direccionEmpresa;
	}

	public String getEmpresaTrabaja()
	{
		return empresaTrabaja;
	}

	public void setEmpresaTrabaja(String empresaTrabaja)
	{
		this.empresaTrabaja = empresaTrabaja;
	}

	public String getEstadoRegistro()
	{
		return estadoRegistro;
	}

	public void setEstadoRegistro(String estado)
	{
		this.estadoRegistro = estado;
	}

	public String getFechaAccidente()
	{
		return fechaAccidente;
	}

	public void setFechaAccidente(String fechaAccidente)
	{
		this.fechaAccidente = fechaAccidente;
	}

	public String getFechaFinalPoliza()
	{
		return fechaFinalPoliza;
	}

	public void setFechaFinalPoliza(String fechaFinalPoliza)
	{
		this.fechaFinalPoliza = fechaFinalPoliza;
	}

	public String getFechaGrabacion()
	{
		return fechaGrabacion;
	}

	public void setFechaGrabacion(String fechaGrabacion)
	{
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getFechaInicialPoliza()
	{
		return fechaInicialPoliza;
	}

	public void setFechaInicialPoliza(String fechaInicialPoliza)
	{
		this.fechaInicialPoliza = fechaInicialPoliza;
	}

	public String getHoraAccidente()
	{
		return horaAccidente;
	}

	public void setHoraAccidente(String horaAccidente)
	{
		this.horaAccidente = horaAccidente;
	}

	public String getHoraGrabacion()
	{
		return horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion)
	{
		this.horaGrabacion = horaGrabacion;
	}

	public String getInformacionAccidente()
	{
		return informacionAccidente;
	}

	public void setInformacionAccidente(String informacionAccidente)
	{
		this.informacionAccidente = informacionAccidente;
	}

	public String getIngreso()
	{
		return ingreso;
	}

	public void setIngreso(String ingreso)
	{
		this.ingreso = ingreso;
	}

	public String getLugarAccidente()
	{
		return lugarAccidente;
	}

	public void setLugarAccidente(String lugarAccidente)
	{
		this.lugarAccidente = lugarAccidente;
	}

	public String getMarcaVehiculo()
	{
		return marcaVehiculo;
	}

	public void setMarcaVehiculo(String marcaVehiculo)
	{
		this.marcaVehiculo = marcaVehiculo;
	}

	public String getNumeroIdConductor()
	{
		return numeroIdConductor;
	}

	public void setNumeroIdConductor(String numeroIdConductor)
	{
		this.numeroIdConductor = numeroIdConductor;
	}

	public String getNumeroPoliza()
	{
		return numeroPoliza;
	}

	public void setNumeroPoliza(String numeroPoliza)
	{
		this.numeroPoliza = numeroPoliza;
	}

	public String getOcupante()
	{
		return ocupante;
	}

	public void setOcupante(String ocupante)
	{
		this.ocupante = ocupante;
	}

	public String getPlaca()
	{
		return placa;
	}

	public void setPlaca(String placa)
	{
		this.placa = placa;
	}

	public String getPlacaVehiculoOcaciona()
	{
		return placaVehiculoOcaciona;
	}

	public void setPlacaVehiculoOcaciona(String placaVehiculoOcaciona)
	{
		this.placaVehiculoOcaciona = placaVehiculoOcaciona;
	}

	public String getResultaLesionadoAl()
	{
		return resultaLesionadoAl;
	}

	public void setResultaLesionadoAl(String resultaLesionadoAl)
	{
		this.resultaLesionadoAl = resultaLesionadoAl;
	}

	public String getTelefonoConductor()
	{
		return telefonoConductor;
	}

	public void setTelefonoConductor(String telefonoConductor)
	{
		this.telefonoConductor = telefonoConductor;
	}

	public String getTelefonoEmpresa()
	{
		return telefonoEmpresa;
	}

	public void setTelefonoEmpresa(String telefonoEmpresa)
	{
		this.telefonoEmpresa = telefonoEmpresa;
	}

	public String getTipo()
	{
		return tipo;
	}

	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}

	public String getTipoIdConductor()
	{
		return tipoIdConductor;
	}

	public void setTipoIdConductor(String tipoIdConductor)
	{
		this.tipoIdConductor = tipoIdConductor;
	}

	public String getUsuarioGrabacion()
	{
		return usuarioGrabacion;
	}

	public void setUsuarioGrabacion(String usuarioGrabacion)
	{
		this.usuarioGrabacion = usuarioGrabacion;
	}

	public String getZonaAccidente()
	{
		return zonaAccidente;
	}

	public void setZonaAccidente(String zonaAccidente)
	{
		this.zonaAccidente = zonaAccidente;
	}

	public String getCiudadConductor()
	{
		return ciudadConductor;
	}

	public void setCiudadConductor(String ciudadConductor)
	{
		this.ciudadConductor = ciudadConductor;
	}

	public String getDepartamentoConductor()
	{
		return departamentoConductor;
	}

	public void setDepartamentoConductor(String departamentoConductor)
	{
		this.departamentoConductor = departamentoConductor;
	}

	public String getCodicionAccidentado()
	{
		return codicionAccidentado;
	}

	public void setCodicionAccidentado(String codicionAccidentado)
	{
		this.codicionAccidentado = codicionAccidentado;
	}

	public String getApellidoNombreDeclarante()
	{
		return apellidoNombreDeclarante;
	}

	public void setApellidoNombreDeclarante(String apellidoNombreDeclarante)
	{
		this.apellidoNombreDeclarante = apellidoNombreDeclarante;
	}

	public String getCiuExpedicionIdDeclarante()
	{
		return ciuExpedicionIdDeclarante;
	}

	public void setCiuExpedicionIdDeclarante(String ciuExpedicionIdDeclarante)
	{
		this.ciuExpedicionIdDeclarante = ciuExpedicionIdDeclarante;
	}

	public String getDepExpedicionIdDeclarante()
	{
		return depExpedicionIdDeclarante;
	}

	public void setDepExpedicionIdDeclarante(String depExpedicionIdDeclarante)
	{
		this.depExpedicionIdDeclarante = depExpedicionIdDeclarante;
	}

	public String getTipoIdDeclarante()
	{
		return tipoIdDeclarante;
	}

	public void setTipoIdDeclarante(String tipoIdDeclarante)
	{
		this.tipoIdDeclarante = tipoIdDeclarante;
	}

	public String getNumeroIdDeclarante()
	{
		return numeroIdDeclarante;
	}

	public void setNumeroIdDeclarante(String numeroIdDeclarante)
	{
		this.numeroIdDeclarante = numeroIdDeclarante;
	}

	/**
	 * @return Returns the fechaAnulacion.
	 */
	public String getFechaAnulacion() {
		return fechaAnulacion;
	}

	/**
	 * @param fechaAnulacion The fechaAnulacion to set.
	 */
	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	/**
	 * @return Returns the horaAnulacion.
	 */
	public String getHoraAnulacion() {
		return horaAnulacion;
	}

	/**
	 * @param horaAnulacion The horaAnulacion to set.
	 */
	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}

	/**
	 * @return Returns the usuarioAnulacion.
	 */
	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}

	/**
	 * @param usuarioAnulacion The usuarioAnulacion to set.
	 */
	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}

	public String getPoliza()
	{
		return poliza;
	}

	public void setPoliza(String poliza)
	{
		this.poliza = poliza;
	}

	public String getCiudadProp()
	{
		return ciudadProp;
	}

	public void setCiudadProp(String ciudadProp)
	{
		this.ciudadProp = ciudadProp;
	}

	public String getDeptoProp()
	{
		return deptoProp;
	}

	public void setDeptoProp(String deptoProp)
	{
		this.deptoProp = deptoProp;
	}

	public String getDireccionProp()
	{
		return direccionProp;
	}

	public void setDireccionProp(String direccionProp)
	{
		this.direccionProp = direccionProp;
	}

	public String getNumeroIdProp()
	{
		return numeroIdProp;
	}

	public void setNumeroIdProp(String numeroIdProp)
	{
		this.numeroIdProp = numeroIdProp;
	}

	public String getPrimerApellidoConductor()
	{
		return primerApellidoConductor;
	}

	public void setPrimerApellidoConductor(String primerApellidoConductor)
	{
		this.primerApellidoConductor = primerApellidoConductor;
	}

	public String getPrimerApellidoProp()
	{
		return primerApellidoProp;
	}

	public void setPrimerApellidoProp(String primerApellidoProp)
	{
		this.primerApellidoProp = primerApellidoProp;
	}

	public String getPrimerNombreConductor()
	{
		return primerNombreConductor;
	}

	public void setPrimerNombreConductor(String primerNombreConductor)
	{
		this.primerNombreConductor = primerNombreConductor;
	}

	public String getPrimerNombreProp()
	{
		return primerNombreProp;
	}

	public void setPrimerNombreProp(String primerNombreProp)
	{
		this.primerNombreProp = primerNombreProp;
	}

	public String getSegundoApellidoConductor()
	{
		return segundoApellidoConductor;
	}

	public void setSegundoApellidoConductor(String segundoApellidoConductor)
	{
		this.segundoApellidoConductor = segundoApellidoConductor;
	}

	public String getSegundoApellidoProp()
	{
		return segundoApellidoProp;
	}

	public void setSegundoApellidoProp(String segundoApellidoProp)
	{
		this.segundoApellidoProp = segundoApellidoProp;
	}

	public String getSegundoNombreConductor()
	{
		return segundoNombreConductor;
	}

	public void setSegundoNombreConductor(String segundoNombreConductor)
	{
		this.segundoNombreConductor = segundoNombreConductor;
	}

	public String getSegundoNombreProp()
	{
		return segundoNombreProp;
	}

	public void setSegundoNombreProp(String segundoNombreProp)
	{
		this.segundoNombreProp = segundoNombreProp;
	}

	public String getTelefonoProp()
	{
		return telefonoProp;
	}

	public void setTelefonoProp(String telefonoProp)
	{
		this.telefonoProp = telefonoProp;
	}

	public String getTipoIdProp()
	{
		return tipoIdProp;
	}

	public void setTipoIdProp(String tipoIdProp)
	{
		this.tipoIdProp = tipoIdProp;
	}

	public String getApellidoNombreTransporta()
	{
		return apellidoNombreTransporta;
	}

	public void setApellidoNombreTransporta(String apellidoNombreTransporta)
	{
		this.apellidoNombreTransporta = apellidoNombreTransporta;
	}

	public String getCiudadTransporta()
	{
		return ciudadTransporta;
	}

	public void setCiudadTransporta(String ciudadTransporta)
	{
		this.ciudadTransporta = ciudadTransporta;
	}

	public String getDepartamentoTransporta()
	{
		return departamentoTransporta;
	}

	public void setDepartamentoTransporta(String departamentoTransporta)
	{
		this.departamentoTransporta = departamentoTransporta;
	}

	public String getDireccionTransporta()
	{
		return direccionTransporta;
	}

	public void setDireccionTransporta(String direccionTransporta)
	{
		this.direccionTransporta = direccionTransporta;
	}

	public String getNumeroIdTransporta()
	{
		return numeroIdTransporta;
	}

	public void setNumeroIdTransporta(String numeroIdTransporta)
	{
		this.numeroIdTransporta = numeroIdTransporta;
	}

	public String getPlacaVehiculoTransporta()
	{
		return placaVehiculoTransporta;
	}

	public void setPlacaVehiculoTransporta(String placaVehiculoTransporta)
	{
		this.placaVehiculoTransporta = placaVehiculoTransporta;
	}

	public String getTelefonoTransporta()
	{
		return telefonoTransporta;
	}

	public void setTelefonoTransporta(String telefonoTransporta)
	{
		this.telefonoTransporta = telefonoTransporta;
	}

	public String getTipoIdTransporta()
	{
		return tipoIdTransporta;
	}

	public void setTipoIdTransporta(String tipoIdTransporta)
	{
		this.tipoIdTransporta = tipoIdTransporta;
	}

	public String getTipoTransporte()
	{
		return tipoTransporte;
	}

	public void setTipoTransporte(String tipoTransporte)
	{
		this.tipoTransporte = tipoTransporte;
	}

	public String getTransportaVictimaDesde()
	{
		return transportaVictimaDesde;
	}

	public void setTransportaVictimaDesde(String transportaVictimaDesde)
	{
		this.transportaVictimaDesde = transportaVictimaDesde;
	}

	public String getTransportaVictimaHasta()
	{
		return transportaVictimaHasta;
	}

	public void setTransportaVictimaHasta(String transportaVictimaHasta)
	{
		this.transportaVictimaHasta = transportaVictimaHasta;
	}

	public String getCiudadExpedicionIdProp()
	{
		return ciudadExpedicionIdProp;
	}

	public void setCiudadExpedicionIdProp(String ciudadExpedicionIdProp)
	{
		this.ciudadExpedicionIdProp = ciudadExpedicionIdProp;
	}

	public String getCiudadExpedicionIdTransporta()
	{
		return ciudadExpedicionIdTransporta;
	}

	public void setCiudadExpedicionIdTransporta(String ciudadExpedicionIdTransporta)
	{
		this.ciudadExpedicionIdTransporta = ciudadExpedicionIdTransporta;
	}

	public String getDeptoExpedicionIdProp()
	{
		return deptoExpedicionIdProp;
	}

	public void setDeptoExpedicionIdProp(String deptoExpedicionIdProp)
	{
		this.deptoExpedicionIdProp = deptoExpedicionIdProp;
	}

	public String getDeptoExpedicionIdTransporta()
	{
		return deptoExpedicionIdTransporta;
	}

	public void setDeptoExpedicionIdTransporta(String deptoExpedicionIdTransporta)
	{
		this.deptoExpedicionIdTransporta = deptoExpedicionIdTransporta;
	}

	public String getPaisAccidente() {
		return paisAccidente;
	}

	public void setPaisAccidente(String paisAccidente) {
		this.paisAccidente = paisAccidente;
	}

	public String getPaisConductor() {
		return paisConductor;
	}

	public void setPaisConductor(String paisConductor) {
		this.paisConductor = paisConductor;
	}

	public String getPaisEmpresa() {
		return paisEmpresa;
	}

	public void setPaisEmpresa(String paisEmpresa) {
		this.paisEmpresa = paisEmpresa;
	}

	public String getPaisExpedicionIdConductor() {
		return paisExpedicionIdConductor;
	}

	public void setPaisExpedicionIdConductor(String paisExpedicionIdConductor) {
		this.paisExpedicionIdConductor = paisExpedicionIdConductor;
	}

	public String getPaisExpedicionIdDeclarante() {
		return paisExpedicionIdDeclarante;
	}

	public void setPaisExpedicionIdDeclarante(String paisExpedicionIdDeclarante) {
		this.paisExpedicionIdDeclarante = paisExpedicionIdDeclarante;
	}

	public String getPaisExpedicionIdProp() {
		return paisExpedicionIdProp;
	}

	public void setPaisExpedicionIdProp(String paisExpedicionIdProp) {
		this.paisExpedicionIdProp = paisExpedicionIdProp;
	}

	public String getPaisExpedicionIdTransporta() {
		return paisExpedicionIdTransporta;
	}

	public void setPaisExpedicionIdTransporta(String paisExpedicionIdTransporta) {
		this.paisExpedicionIdTransporta = paisExpedicionIdTransporta;
	}

	public String getPaisProp() {
		return paisProp;
	}

	public void setPaisProp(String paisProp) {
		this.paisProp = paisProp;
	}

	public String getPaisTransporta() {
		return paisTransporta;
	}

	public void setPaisTransporta(String paisTransporta) {
		this.paisTransporta = paisTransporta;
	}
	
	public String getDescripcionBreveOcurrencia() {
		return descripcionBreveOcurrencia;
	}

	public void setDescripcionBreveOcurrencia(String descripcionBreveOcurrencia) {
		this.descripcionBreveOcurrencia = descripcionBreveOcurrencia;
	}

	
	public String getOtroTipoServicioVehiculo() {
	return otroTipoServicioVehiculo;
	}


	public void setOtroTipoServicioVehiculo(String otroTipoServicioVehiculo) {
		this.otroTipoServicioVehiculo = otroTipoServicioVehiculo;
	}
	
	}
