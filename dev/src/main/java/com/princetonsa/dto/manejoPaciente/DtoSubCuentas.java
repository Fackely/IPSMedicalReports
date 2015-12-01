/*
 * Jun 15, 2007
 * Projecto axioma
 * Paquete com.princetonsa.dto.manejoPaciente
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.Utilidades;

import com.princetonsa.dto.facturacion.DtoConvenio;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoSubCuentas implements Serializable 
{
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * codigo de la subcuenta
	 */
	private String subCuenta;
	
	/**
	 * Ingreso
	 */
	private int ingreso;
	
	/**
	 * Infodatos que contiene el codigo-nombre del convenio
	 */
	private InfoDatosInt  convenio;
	
	/**
	 * Código del tipo de régimen
	 */
	private String codigoTipoRegimen;
	
	/**
	 * Tipo de cobertura
	 */
	private InfoDatosInt tipoCobertura;
	
	/**
	 * Tipo afiliado al responsable
	 */
	private String tipoAfiliado;
	private String descripcionTipoAfiliado;
	
	/**
	 * clasificacion socio-economica al paciente
	 */
	private int clasificacionSocioEconomica;
	private String descripcionClasificacionSocioEconomica;
	
	/**
	 * numero autorizacion del paciente con el responsable
	 */
	private String nroAutorizacion;
	
	/**
	 * monto cobro.
	 */
	private int montoCobro;
	private String descripcionMontoCobro;
	
	/**
	 * naturaleza paciente
	 */
	private int naturalezaPaciente;
	private String descripcionNaturalezaPaciente;
	
	/**
	 * nro carnet
	 */
	private String nroCarnet;
	
	/**
	 * nro poliza
	 */
	private String nroPoliza;
	
	/**
	 * fecha de afiliacion al responsable
	 */
	private String fechaAfiliacion;
	
	/**
	 * semanas de cotizacion del paciente,
	 */
	private int semanasCotizacion;
	
	/**
	 * Meses de cotizacion del paciente
	 */
	private int mesesCotizacion;
	
	/**
	 * Codigo del paciente
	 */
	private int codigoPaciente;
	
	/**
	 * contrato asociado al responsable
	 */
	private int contrato;
	private String numeroContrato;
	
	/**
	 * valor utilizado soat para el responsable
	 */
	private String valorUtilizadoSoat;
	
	/**
	 * Nro prioridad de responsable
	 */
	private int nroPrioridad;
	
	/**
	 * porcentaje autorizado para el responsable
	 */
	private String porcentajeAutorizado;
	
	/**
	 * monto autorizado para el responsable
	 */
	private String montoAutorizado;
	
	/**
	 * observaciones de los parametro de distribucion del responsable
	 */
	private String obsParametrosDistribucion;
	
	/**
	 * indicativo de si el responsable es facturado o no
	 */
	private String facturado;	
	
	/**
	 * Login del usuario
	 */
	private String loginUsuario;
	
	/**
	 * Variable para saber si la cuenta maneja poliza
	 */
	private boolean subCuentaPoliza; 
	
	/**
	 * Variable para saber si la cuenta maneja verificacion derechos
	 */
	private boolean subCuentaVerificacionDerechos;
	
	/**
	 * Objeto que maneja el filtro de la distribucion.
	 */
	private DtoFiltroDistribucion filtroDistribucion;
	
	/**
	 * Objeto que maneja los requisitos del paciente
	 */
	private ArrayList<DtoRequsitosPaciente> requisitosPaciente;
	
	/**
	 * Objeto que maneja la verificacion de derechos de un paciente.
	 */
	private DtoVerifcacionDerechos verificacionDerechos;
	
	/**
	 * Coleccion de objetos de las solicitudes asociadas al responsable
	 */
	private ArrayList<DtoSolicitudesSubCuenta> solicitudesSubcuenta;
	
	/**
	 * Objeto que maneja la informacion de la poliza 
	 */
	private DtoTitularPoliza titularPoliza;
	
	private String numeroSolicitudVolante;
	
	/**
	 * codigo MultiEmpresa
	 * */
	private String empresasInstitucion;
	
	private BigDecimal bono;
	
	private Double valorAutorizacion;
	
	private String medioAutorizacion;
	
	private ArrayList<String> documentosAdjuntos;
	
	//*******Atributo usado para almacenar valores adicionales del convenio de la subcuenta**************
	private DtoConvenio datosConvenio;
	
	//********Atributos de control*************************************************
	private boolean modificacion;
	
	private boolean eliminar;
	
	
	private String tipoCobroPaciente="";
	private String tipoMontoCobro="";
	private Long codigoRegistroAccidenteTransito;
	
	/**
	 * valor del Convenio y el paciente
	 */
	private HashMap<String,Object> valoresSubCuenta;
	
	/**
	 * Atributo para almacenar el porcentaje del monto cuando la cuenta no maneja montos 
	 */
	private Double porcentajeMontoCobro;
	
	/**
	 * Atributo para almacenar el tipo de paciente ingresado en la cuenta
	 */
	private String tipoPaciente;
	
	/**
	 * Atributo para almacenar el tipo del detalle del monto
	 */
	private String  tipoDetalleMonto;
	
	/**
	 * Atributo que almacena el valor del monto general
	 */
	private Double valorMontoGeneral;
	
	/**
	 * Atributo que almacena la cantidad de monto general
	 */
	private Integer cantidadMontoGeneral;
	
	/**
	 * Atributo que almacena el porcentaje de monto general
	 */
	private Double porcentajeMontoGeneral;
	
	/**
	 * Atributo que almacena el tipo deo cobro para el monto
	 */
	private Integer tipoMonto;

	
	public DtoSubCuentas() 
	{
		this.subCuenta="";
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.convenio=new InfoDatosInt();
		this.codigoTipoRegimen = "";
		this.tipoCobertura = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.tipoAfiliado="";
		this.descripcionTipoAfiliado = "";
		this.clasificacionSocioEconomica=ConstantesBD.codigoNuncaValido;
		this.descripcionClasificacionSocioEconomica = "";
		this.nroAutorizacion="";
		this.montoCobro=ConstantesBD.codigoNuncaValido;
		this.descripcionMontoCobro = "";
		this.naturalezaPaciente=ConstantesBD.codigoNuncaValido;
		this.descripcionNaturalezaPaciente = "";
		this.nroCarnet="";
		this.nroPoliza="";
		this.fechaAfiliacion="";
		this.semanasCotizacion=ConstantesBD.codigoNuncaValido;
		this.mesesCotizacion=ConstantesBD.codigoNuncaValido;
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.numeroContrato = "";
		this.valorUtilizadoSoat="";
		this.nroPrioridad=ConstantesBD.codigoNuncaValido;
		this.porcentajeAutorizado="";
		this.montoAutorizado="";
		this.obsParametrosDistribucion="";
		this.facturado="";
		this.loginUsuario = "";
		this.subCuentaPoliza = false;
		this.subCuentaVerificacionDerechos = false;
		this.empresasInstitucion = "";
		
		
		
		this.filtroDistribucion=new DtoFiltroDistribucion();
		this.requisitosPaciente=new ArrayList<DtoRequsitosPaciente>();
		this.verificacionDerechos=new DtoVerifcacionDerechos();
		this.solicitudesSubcuenta=new ArrayList<DtoSolicitudesSubCuenta>();
		this.titularPoliza = new DtoTitularPoliza();
		
		this.numeroSolicitudVolante = "";
		
		this.bono= BigDecimal.ZERO;
		this.valorAutorizacion=ConstantesBD.codigoNuncaValidoDouble;
		this.medioAutorizacion="";
		this.documentosAdjuntos=new ArrayList<String>();
		
		//*******Atributo usado para almacenar valores adicionales del convenio de la subcuenta**************
		this.datosConvenio = new DtoConvenio();
		
		//Atributos de control
		this.modificacion = false; //indica si el objeto se está usando para una modificacion
		this.eliminar = false; //indica si el objeto se va a eliminar de una cuenta general
		
		this.tipoCobroPaciente="";
		this.tipoMontoCobro="";
		this.porcentajeMontoCobro=ConstantesBD.codigoNuncaValidoDouble;
		
		this.valoresSubCuenta=new HashMap<String,Object>();
	}

	public int getNumDocAdjuntos()
	{
		return this.documentosAdjuntos.size();
	}
	/**
	 * 
	 * @return
	 */
	public DtoFiltroDistribucion getFiltroDistribucion() 
	{
		return filtroDistribucion;
	}

	/**
	 * 
	 * @param filtroDistribucion
	 */
	public void setFiltroDistribucion(DtoFiltroDistribucion filtroDistribucion) 
	{
		this.filtroDistribucion = filtroDistribucion;
	}

	

	/**
	 * @return the requisitosPaciente
	 */
	public ArrayList<DtoRequsitosPaciente> getRequisitosPaciente() {
		return requisitosPaciente;
	}

	/**
	 * @param requisitosPaciente the requisitosPaciente to set
	 */
	public void setRequisitosPaciente(
			ArrayList<DtoRequsitosPaciente> requisitosPaciente) {
		this.requisitosPaciente = requisitosPaciente;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoSolicitudesSubCuenta> getSolicitudesSubcuenta() 
	{
		return solicitudesSubcuenta;
	}

	
	/**
	 * 
	 * @param solicitudesSubcuenta
	 */
	public void setSolicitudesSubcuenta(ArrayList<DtoSolicitudesSubCuenta> solicitudesSubcuenta) 
	{
		this.solicitudesSubcuenta = solicitudesSubcuenta;
	}

	/**
	 * 
	 * @return
	 */
	public DtoVerifcacionDerechos getVerificacionDerechos() 
	{
		return verificacionDerechos;
	}

	/**
	 * 
	 * @param verificacionDerechos
	 */
	public void setVerificacionDerechos(DtoVerifcacionDerechos verificacionDerechos) 
	{
		this.verificacionDerechos = verificacionDerechos;
	}

	/**
	 * @return the clasificacionSocioEconomica
	 */
	public int getClasificacionSocioEconomica() {
		return clasificacionSocioEconomica;
	}

	/**
	 * @param clasificacionSocioEconomica the clasificacionSocioEconomica to set
	 */
	public void setClasificacionSocioEconomica(int clasificacionSocioEconomica) {
		this.clasificacionSocioEconomica = clasificacionSocioEconomica;
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
	 * @return the contrato
	 */
	public int getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the convenio
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the facturado
	 */
	public String getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}

	/**
	 * @return the fechaAfiliacion
	 */
	public String getFechaAfiliacion() {
		return fechaAfiliacion;
	}

	/**
	 * @param fechaAfiliacion the fechaAfiliacion to set
	 */
	public void setFechaAfiliacion(String fechaAfiliacion) {
		this.fechaAfiliacion = fechaAfiliacion;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the montoAutorizado
	 */
	public String getMontoAutorizado() {
		return montoAutorizado;
	}

	/**
	 * @param montoAutorizado the montoAutorizado to set
	 */
	public void setMontoAutorizado(String montoAutorizado) {
		this.montoAutorizado = montoAutorizado;
	}

	/**
	 * @return the montoCobro
	 */
	public int getMontoCobro() {
		return montoCobro;
	}

	/**
	 * @param montoCobro the montoCobro to set
	 */
	public void setMontoCobro(int montoCobro) {
		this.montoCobro = montoCobro;
	}

	/**
	 * @return the naturalezaPaciente
	 */
	public int getNaturalezaPaciente() {
		return naturalezaPaciente;
	}

	/**
	 * @param naturalezaPaciente the naturalezaPaciente to set
	 */
	public void setNaturalezaPaciente(int naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}

	/**
	 * @return the nroAutorizacion
	 */
	public String getNroAutorizacion() {
		return nroAutorizacion;
	}

	/**
	 * @param nroAutorizacion the nroAutorizacion to set
	 */
	public void setNroAutorizacion(String nroAutorizacion) {
		this.nroAutorizacion = nroAutorizacion;
	}

	/**
	 * @return the nroCarnet
	 */
	public String getNroCarnet() {
		return nroCarnet;
	}

	/**
	 * @param nroCarnet the nroCarnet to set
	 */
	public void setNroCarnet(String nroCarnet) {
		this.nroCarnet = nroCarnet;
	}

	/**
	 * @return the nroPoliza
	 */
	public String getNroPoliza() {
		return nroPoliza;
	}

	/**
	 * @param nroPoliza the nroPoliza to set
	 */
	public void setNroPoliza(String nroPoliza) {
		this.nroPoliza = nroPoliza;
	}

	/**
	 * @return the nroPrioridad
	 */
	public int getNroPrioridad() {
		return nroPrioridad;
	}

	/**
	 * @param nroPrioridad the nroPrioridad to set
	 */
	public void setNroPrioridad(int nroPrioridad) {
		this.nroPrioridad = nroPrioridad;
	}

	/**
	 * @return the obsParametrosDistribucion
	 */
	public String getObsParametrosDistribucion() {
		return obsParametrosDistribucion;
	}

	/**
	 * @param obsParametrosDistribucion the obsParametrosDistribucion to set
	 */
	public void setObsParametrosDistribucion(String obsParametrosDistribucion) {
		this.obsParametrosDistribucion = obsParametrosDistribucion;
	}

	/**
	 * @return the porcentajeAutorizado
	 */
	public String getPorcentajeAutorizado() {
		return porcentajeAutorizado;
	}

	/**
	 * @param porcentajeAutorizado the porcentajeAutorizado to set
	 */
	public void setPorcentajeAutorizado(String porcentajeAutorizado) {
		this.porcentajeAutorizado = porcentajeAutorizado;
	}

	/**
	 * @return the semanasCotizacion
	 */
	public int getSemanasCotizacion() {
		return semanasCotizacion;
	}

	/**
	 * @param semanasCotizacion the semanasCotizacion to set
	 */
	public void setSemanasCotizacion(int semanasCotizacion) {
		this.semanasCotizacion = semanasCotizacion;
	}

	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @return the subCuenta
	 */
	public double getSubCuentaDouble() {
		return Double.parseDouble(subCuenta);
	}
	
	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the tipoAfiliado
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * @param tipoAfiliado the tipoAfiliado to set
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	

	/**
	 * @return the valorUtilizadoSoat
	 */
	public String getValorUtilizadoSoat() {
		return valorUtilizadoSoat;
	}

	/**
	 * @param valorUtilizadoSoat the valorUtilizadoSoat to set
	 */
	public void setValorUtilizadoSoat(String valorUtilizadoSoat) {
		this.valorUtilizadoSoat = valorUtilizadoSoat;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the descripcionNaturalezaPaciente
	 */
	public String getDescripcionNaturalezaPaciente() {
		return descripcionNaturalezaPaciente;
	}

	/**
	 * @param descripcionNaturalezaPaciente the descripcionNaturalezaPaciente to set
	 */
	public void setDescripcionNaturalezaPaciente(
			String descripcionNaturalezaPaciente) {
		this.descripcionNaturalezaPaciente = descripcionNaturalezaPaciente;
	}

	/**
	 * @return the numeroContrato
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}

	/**
	 * @param numeroContrato the numeroContrato to set
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	/**
	 * @return the descripcionTipoAfiliado
	 */
	public String getDescripcionTipoAfiliado() {
		return descripcionTipoAfiliado;
	}

	/**
	 * @param descripcionTipoAfiliado the descripcionTipoAfiliado to set
	 */
	public void setDescripcionTipoAfiliado(String descripcionTipoAfiliado) {
		this.descripcionTipoAfiliado = descripcionTipoAfiliado;
	}

	/**
	 * @return the descripcionClasificacionSocioEconomica
	 */
	public String getDescripcionClasificacionSocioEconomica() {
		return descripcionClasificacionSocioEconomica;
	}

	/**
	 * @param descripcionClasificacionSocioEconomica the descripcionClasificacionSocioEconomica to set
	 */
	public void setDescripcionClasificacionSocioEconomica(
			String descripcionClasificacionSocioEconomica) {
		this.descripcionClasificacionSocioEconomica = descripcionClasificacionSocioEconomica;
	}

	/**
	 * @return the descripcionMontoCobro
	 */
	public String getDescripcionMontoCobro() {
		return descripcionMontoCobro;
	}

	/**
	 * @param descripcionMontoCobro the descripcionMontoCobro to set
	 */
	public void setDescripcionMontoCobro(String descripcionMontoCobro) {
		this.descripcionMontoCobro = descripcionMontoCobro;
	}

	/**
	 * @return the titularPoliza
	 */
	public DtoTitularPoliza getTitularPoliza() {
		return titularPoliza;
	}

	/**
	 * @param titularPoliza the titularPoliza to set
	 */
	public void setTitularPoliza(DtoTitularPoliza titularPoliza) {
		this.titularPoliza = titularPoliza;
	}

	/**
	 * @return the subCuentaPoliza
	 */
	public boolean isSubCuentaPoliza() {
		return subCuentaPoliza;
	}

	/**
	 * @param subCuentaPoliza the subCuentaPoliza to set
	 */
	public void setSubCuentaPoliza(boolean subCuentaPoliza) {
		this.subCuentaPoliza = subCuentaPoliza;
	}

	/**
	 * @return the subCuentaVerificacionDerechos
	 */
	public boolean isSubCuentaVerificacionDerechos() {
		return subCuentaVerificacionDerechos;
	}

	/**
	 * @param subCuentaVerificacionDerechos the subCuentaVerificacionDerechos to set
	 */
	public void setSubCuentaVerificacionDerechos(
			boolean subCuentaVerificacionDerechos) {
		this.subCuentaVerificacionDerechos = subCuentaVerificacionDerechos;
	}

	
	/**
	 * 
	 * @return
	 */
	public String loggerSubcuenta()
	{
		return "subcuenta->"+this.subCuenta+" cod pac->"+codigoPaciente+" cod contrato->"+this.contrato;
	}

	/**
	 * @return the codigoTipoRegimen
	 */
	public String getCodigoTipoRegimen() {
		return codigoTipoRegimen;
	}

	/**
	 * @param codigoTipoRegimen the codigoTipoRegimen to set
	 */
	public void setCodigoTipoRegimen(String codigoTipoRegimen) {
		this.codigoTipoRegimen = codigoTipoRegimen;
	}

	/**
	 * @return the numeroSolicitudVolante
	 */
	public String getNumeroSolicitudVolante() {
		return numeroSolicitudVolante;
	}

	/**
	 * @param numeroSolicitudVolante the numeroSolicitudVolante to set
	 */
	public void setNumeroSolicitudVolante(String numeroSolicitudVolante) {
		this.numeroSolicitudVolante = numeroSolicitudVolante;
	}

	/**
	 * @return the empresasInstitucion
	 */
	public String getEmpresasInstitucion() {
		if(this.empresasInstitucion == null)
			return "";
		else
			return empresasInstitucion;
	}

	/**
	 * @param empresasInstitucion the empresasInstitucion to set
	 */
	public void setEmpresasInstitucion(String empresasInstitucion) {
		this.empresasInstitucion = empresasInstitucion;
	}
	
	
	/**
	 * Campo de NIT o numero de identificacion del tercero que se carga para su insercion en la tabla ax_pacien de la interfaz.
	 */
	private String nit;

	/**
	 * @return the nit
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit the nit to set
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * 
	 * @param servicio
	 * @return
	 */
	public int getEsquemaTarifarioServiciosPpalOoriginal(Connection con,String subCuenta,int contrato,int servicio,String fechaVigencia, int centroAtencion) throws IPSException 
	{
		return Utilidades.obtenerEsquemasTarifarioServicioArticulo(con,subCuenta,contrato,servicio,true,fechaVigencia, centroAtencion);
	}

	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static int getEsquemaTarifarioArticuloPpalOoriginal(Connection con,String subCuenta,int contrato,int articulo,String fechaVigencia, int centroAtencion) throws IPSException 
	{
		return Utilidades.obtenerEsquemasTarifarioServicioArticulo(con,subCuenta,contrato,articulo,false,fechaVigencia, centroAtencion);
	}

	/**
	 * @return the mesesCotizacion
	 */
	public int getMesesCotizacion() {
		return mesesCotizacion;
	}

	/**
	 * @param mesesCotizacion the mesesCotizacion to set
	 */
	public void setMesesCotizacion(int mesesCotizacion) {
		this.mesesCotizacion = mesesCotizacion;
	}

	/**
	 * @return the tipoCobertura
	 */
	public InfoDatosInt getTipoCobertura() {
		return tipoCobertura;
	}

	/**
	 * @param tipoCobertura the tipoCobertura to set
	 */
	public void setTipoCobertura(InfoDatosInt tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}
	
	/**
	 * @return the tipoCobertura
	 */
	public int getCodigoTipoCobertura() {
		return tipoCobertura.getCodigo();
	}

	/**
	 * @param tipoCobertura the tipoCobertura to set
	 */
	public void setCodigoTipoCobertura(int tipoCobertura) {
		this.tipoCobertura.setCodigo(tipoCobertura);
	}
	
	/**
	 * @return the tipoCobertura
	 */
	public String getNombreTipoCobertura() {
		return tipoCobertura.getNombre();
	}

	/**
	 * @param tipoCobertura the tipoCobertura to set
	 */
	public void setNombreTipoCobertura(String tipoCobertura) {
		this.tipoCobertura.setNombre(tipoCobertura);
	}

	

	/**
	 * @return the valorAutorizacion
	 */
	public Double getValorAutorizacion() {
		return valorAutorizacion;
	}

	/**
	 * @param valorAutorizacion the valorAutorizacion to set
	 */
	public void setValorAutorizacion(Double valorAutorizacion) {
		this.valorAutorizacion = valorAutorizacion;
	}

	/**
	 * @return the medioAutorizacion
	 */
	public String getMedioAutorizacion() {
		return medioAutorizacion;
	}

	/**
	 * @param medioAutorizacion the medioAutorizacion to set
	 */
	public void setMedioAutorizacion(String medioAutorizacion) {
		this.medioAutorizacion = medioAutorizacion;
	}

	/**
	 * @return the documentosAdjuntos
	 */
	public ArrayList<String> getDocumentosAdjuntos() {
		return documentosAdjuntos;
	}

	/**
	 * @param documentosAdjuntos the documentosAdjuntos to set
	 */
	public void setDocumentosAdjuntos(ArrayList<String> documentosAdjuntos) {
		this.documentosAdjuntos = documentosAdjuntos;
	}

	/**
	 * @return the documentosAdjuntos
	 */
	public String getDocumentoAdjunto(int indice) {
		return documentosAdjuntos.get(indice);
	}

	/**
	 * @param documentosAdjuntos the documentosAdjuntos to set
	 */
	public void setDocumentoAdjunto(int indice, String documentosAdjuntos) {
		if(indice<this.documentosAdjuntos.size())
		{
			this.documentosAdjuntos.set(indice, documentosAdjuntos);
		}
		else
		{
			this.documentosAdjuntos.add(documentosAdjuntos);
		}
	}

	/**
	 * @return the datosConvenio
	 */
	public DtoConvenio getDatosConvenio() {
		return datosConvenio;
	}

	/**
	 * @param datosConvenio the datosConvenio to set
	 */
	public void setDatosConvenio(DtoConvenio datosConvenio) {
		this.datosConvenio = datosConvenio;
	}

	/**
	 * @return the modificacion
	 */
	public boolean isModificacion() {
		return modificacion;
	}

	/**
	 * @param modificacion the modificacion to set
	 */
	public void setModificacion(boolean modificacion) {
		this.modificacion = modificacion;
	}

	/**
	 * @return the eliminar
	 */
	public boolean getEliminar()
	{
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(boolean eliminar)
	{
		this.eliminar = eliminar;
	}

	/**
	 * @return the bono
	 */
	public BigDecimal getBono() {
		return bono;
	}

	/**
	 * @param bono the bono to set
	 */
	public void setBono(BigDecimal bono) {
		this.bono = bono;
	}

	public String getTipoCobroPaciente() {
		return tipoCobroPaciente;
	}

	public void setTipoCobroPaciente(String tipoCobroPaciente) {
		this.tipoCobroPaciente = tipoCobroPaciente;
	}

	public String getTipoMontoCobro() {
		return tipoMontoCobro;
	}

	public void setTipoMontoCobro(String tipoMontoCobro) {
		this.tipoMontoCobro = tipoMontoCobro;
	}

	public double getPorcentajeMontoCobro() {
		return porcentajeMontoCobro;
	}

	public void setPorcentajeMontoCobro(double porcentajeMontoCobro) {
		this.porcentajeMontoCobro = porcentajeMontoCobro;
	}

	/**
	 * @return Retorna codigoRegistroAccidenteTransito
	 */
	public Long getCodigoRegistroAccidenteTransito() {
		return codigoRegistroAccidenteTransito;
	}

	/**
	 * @param codigoRegistroAccidenteTransito Asigna el atributo codigoRegistroAccidenteTransito
	 */
	public void setCodigoRegistroAccidenteTransito(
			Long codigoRegistroAccidenteTransito) {
		this.codigoRegistroAccidenteTransito = codigoRegistroAccidenteTransito;
	}
	
	/**
	 * @return Retorna valoresSubcuenta
	 */
	public HashMap<String, Object> getValoresSubCuenta() {
		return valoresSubCuenta;
	}

	/**
	 * @param valoresSubcuenta
	 */
	public void setValoresSubCuenta(HashMap<String, Object> valoresSubCuenta) {
		this.valoresSubCuenta = valoresSubCuenta;
	}
	
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	public String getTipoDetalleMonto() {
		return tipoDetalleMonto;
	}

	public void setTipoDetalleMonto(String tipoDetalleMonto) {
		this.tipoDetalleMonto = tipoDetalleMonto;
	}

	public Double getValorMontoGeneral() {
		return valorMontoGeneral;
	}

	public void setValorMontoGeneral(Double valorMontoGeneral) {
		this.valorMontoGeneral = valorMontoGeneral;
	}

	public Integer getCantidadMontoGeneral() {
		return cantidadMontoGeneral;
	}

	public void setCantidadMontoGeneral(Integer cantidadMontoGeneral) {
		this.cantidadMontoGeneral = cantidadMontoGeneral;
	}

	public Double getPorcentajeMontoGeneral() {
		return porcentajeMontoGeneral;
	}

	public void setPorcentajeMontoGeneral(Double porcentajeMontoGeneral) {
		this.porcentajeMontoGeneral = porcentajeMontoGeneral;
	}

	public Integer getTipoMonto() {
		return tipoMonto;
	}

	public void setTipoMonto(Integer tipoMonto) {
		this.tipoMonto = tipoMonto;
	}

}