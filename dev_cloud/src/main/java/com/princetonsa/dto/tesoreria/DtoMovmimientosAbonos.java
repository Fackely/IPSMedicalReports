package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoPaciente;


/**
 * Manejo de los abonos de los pacientes
 * @author Juan David Ram&iacute;rez
 * @since 2010-07-19
 * @version 1.0.1
 */
@SuppressWarnings("serial")
public class DtoMovmimientosAbonos implements Serializable, Comparable<DtoMovmimientosAbonos>
{
	/**
	 * C&oacute;digo del movimiento (Primary Key)
	 */
	private int codigo;

	/**
	 * Paciente al cual se le realiz&oacute; el movimiento del abono
	 */
	private int paciente;
	
	/**
	 * C&oacute;digo del movimiento asociado
	 */
	private BigDecimal codigoDocumento;
	
	/**
	 * Consecutivo que se debe mostrar en pantalla
	 * relacionado al c&oacute;digo del movimiento seg&uacute;n
	 * el tipo de movimiento
	 */
	private String consecutivoDocumento;
	
	/**
	 * Tipo de movimiento
	 * Constantes a utilizar:<br>
	 * <code>ConstantesBD.tipoMovimientoAbonoIngresoReciboCaja</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoFacturacion</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoAnulacionFactura</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoAnulacionDevolucionReciboCaja</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoSalidaReservaAbono</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoEntradaReservaAbono</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoAnulacionReservaAbono</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoIngresoPorTraslado</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoSalidaPorTraslado</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoSaldoInicialPositivo</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoSaldoInicialNegativo</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoAjusteSaldoInicialPositivo</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoAjusteSaldoInicialNegativo</code><br>
	 * <code>ConstantesBD.tipoMovimientoAbonoDevolucionAbono</code><br>
	 */
	private int tipo;

	/**
	 * Nombre del tipo de movimiento de abono
	 */
	private String nombreTipo;

	/**
	 * Valor por el cual se realiz&oacute; el movimiento
	 */
	private double valor;
	
	/**
	 * Fecha del movimiento
	 */
	private Date fecha;
	
	/**
	 * Hora del movimiento
	 */
	private String hora;
	
	/**
	 * Ingreso asociado al paciente
	 */
	private Integer ingreso;
	
	/**
	 * Estado del documento asociado, solo se asigna 
	 * en los casos que aplique
	 */
	private String estado;
	
	/**
	 * Operacion seg&oacute;n el tipo de movimiento
	 */
	private String operacion;
	
	/**
	 * Centro de atenci6oacute;n donde se gener&oacute; el movimiento
	 */
	private String centroAtencion;
	
	/**
	 * Centro de atenci6oacute;n due&ntilde;o del paciente
	 */
	private String centroAtencionDuenio;
	
	/**
	 * Almacena el valor del movimiento formateado.
	 */
	private String valorFormateado;
	
	/**
	 * Almacena el consecutivo del recibo de caja que fue
	 * anulado o devuelto.
	 */
	private String consecutivoReciboCaja;
	
	/**
	 * Almacena el consecutivo de la factura que ha sido 
	 * anulada o devuelta.
	 */
	private String consecutivoFactura;
	
	/**
	 * Almacena el número del documento (factura o recibo de caja)
	 * que fue devuelto o anulado.
	 */
	private String dctoDevueltoAnulado;
	
	/**
	 * Almacena el simbolo de la operación a realizar
	 */
	private String ayudanteOperacion;
	
	/**
	 * Almacena la prioridad en la que deben ser mostrados los
	 * tipos de movimientos de abono en el consolidado por paciente.
	 */
	private int orden;
	
	/**
	 * Almacena el listado con el consolidado de los tipos de movimientos abonos
	 * del paciente.
	 */
	private ArrayList<DtoMovmimientosAbonos> listadoConsolidadoMovimientos;
	
	/**
	 * Almacena la ruta del logo a ser mostrado en el reporte.
	 */
	private String rutaLogo;
	
	/**
	 * Almacena la ubicación del logo a ser mostrado en el reporte.
	 */
	private String ubicacionLogo;
	
	/**
	 * Almacena el nombre del usuario que genera el reporte
	 */
	private String usuarioGeneraReporte;
	
	/**
	 * Almacena la fecha y hora en la cual se ha generado el reporte.
	 */
	private String fechaHoraGeneraReporte;
	
	/**
	 * Razón social de la institución en donde se genera el reporte.
	 */
	private String razonSocial;
	
	/**
	 * Nombre de la institución en donde se genera el reporte.
	 */
	private String nombreInstitucion;
	
	/**
	 * Actividad económica de la institución en la cual se genera el reporte.
	 */
	private String actividadEconomica;
	
	/**
	 * Dirección y teléfono de la institución en donde se genera el reporte.
	 */
	private String direccionTelefono;
	
	/**
	 * Centro de atención en donde se genera el reporte.
	 */
	private String centroAtencionReporte;
	
	/**
	 * Nit de la institución en donde se genera el reporte.
	 */
	private String nit;
	
	/**
	 * Almacena la ubicación del logo si debe ser posicionado a la derecha
	 * del reporte.
	 */
	private String logoDerecha;
	
	/**
	 * Almacena la ubicación del logo si debe ser posicionado a la izquierda
	 * del reporte.
	 */
	private String logoIzquierda;
	
	/**
	 * Almacena la información del paciente al cual 
	 * se le está generando el reporte.
	 */
	private DtoPaciente infoPaciente;
	
	/**
	 * Almacena el saldo disponible que tiene el paciente.
	 */
	private String totalSaldoPaciente;
	
	/** Objeto jasper para el subreporte con la información del paciente
	 * al cual se le está generando el reporte.
	 */
   private JRDataSource dsInfoPaciente;
   
	/**
	 * Objeto jasper para el subreporte con la información del paciente al cual
	 * se le está generando el reporte.
	 */
   private JRDataSource dsConsolidadoMovimientos;
  
	/**
	 * Almacena el tipo y operación del movimiento de abonos.
	 */
  private String tipoMovimiento;
  
	/**
	 * Almacena el listado con el detalle de los tipos de movimientos abonos del
	 * paciente.
	 */
	private ArrayList<DtoMovmimientosAbonos> listadoDetalleMovimientos;
	
	/**
	 * Almacena la fecha formateada del detalle de los movimientos de abonos.
	 */
	private String fechaFormateada;
	
	/**
	 * Objeto jasper para el subreporte con la información del paciente al cual
	 * se le está generando el reporte.
	 */
  private JRDataSource dsDetalleMovimientos;
  
  /**
   * Atributo que indica si el reporte que se está generando es un consolidado
   * o un detalle de los movimientos de abonos.
   */
  private boolean esConsolidado;
  
  /**
   * Atributo que almacena un valor booleano que indica si el tipo
   * de archivo a generar es plano.
   */
  private boolean esArchivoPlano;
  
	
	
	/**
	 * Método constructor de la clase
	 * @author Yennifer Guerrero
	 */
	public DtoMovmimientosAbonos() {
		this.valorFormateado = "";
		this.consecutivoReciboCaja = "";
		this.consecutivoFactura = "";
		this.dctoDevueltoAnulado = "";
		this.ayudanteOperacion = "";
		this.orden = ConstantesBD.codigoNuncaValido;
		this.listadoConsolidadoMovimientos = new ArrayList<DtoMovmimientosAbonos>();
		this.rutaLogo = "";
		this.ubicacionLogo = "";
		this.usuarioGeneraReporte = "";
		this.fechaHoraGeneraReporte = "";
		this.razonSocial = "";
		this.nombreInstitucion = "";
		this.actividadEconomica = "";
		this.direccionTelefono = "";
		this.centroAtencionReporte = "";
		this.nit = "";
		this.logoDerecha = "";
		this.logoIzquierda = "";
		this.infoPaciente = new DtoPaciente();
		this.totalSaldoPaciente = "";
		this.tipoMovimiento = "";
		this.listadoDetalleMovimientos = new ArrayList<DtoMovmimientosAbonos>();
		this.fechaFormateada = "";
		this.esConsolidado = false;
		this.esArchivoPlano = false;
		this.centroAtencion = "";
		this.nombreTipo = "";
		this.codigoDocumento = BigDecimal.ZERO;
		this.ingreso = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return Retorna atributo codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna atributo codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Retorna atributo paciente
	 */
	public int getPaciente()
	{
		return paciente;
	}

	/**
	 * @param paciente Asigna atributo paciente
	 */
	public void setPaciente(int paciente)
	{
		this.paciente = paciente;
	}

	/**
	 * @return Retorna atributo codigoDocumento
	 */
	public BigDecimal getCodigoDocumento()
	{
		return codigoDocumento;
	}

	/**
	 * @param codigoDocumento Asigna atributo codigoDocumento
	 */
	public void setCodigoDocumento(BigDecimal codigoDocumento)
	{
		this.codigoDocumento = codigoDocumento;
	}

	/**
	 * @return Retorna atributo consecutivoDocumento
	 */
	public String getConsecutivoDocumento()
	{
		return consecutivoDocumento;
	}

	/**
	 * @param consecutivoDocumento Asigna atributo consecutivoDocumento
	 */
	public void setConsecutivoDocumento(String consecutivoDocumento)
	{
		this.consecutivoDocumento = consecutivoDocumento;
	}

	/**
	 * @return Retorna atributo tipo
	 */
	public int getTipo()
	{
		return tipo;
	}

	/**
	 * @param tipo Asigna atributo tipo
	 */
	public void setTipo(int tipo)
	{
		this.tipo = tipo;
	}

	/**
	 * @return Retorna atributo valor
	 */
	public double getValor()
	{
		return valor;
	}

	/**
	 * @param valor Asigna atributo valor
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}

	/**
	 * @return Retorna atributo fecha
	 */
	public Date getFecha()
	{
		return fecha;
	}

	/**
	 * @param fecha Asigna atributo fecha
	 */
	public void setFecha(Date fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * @return Retorna atributo hora
	 */
	public String getHora()
	{
		return UtilidadFecha.conversionFormatoHoraABD(this.hora);
	}
	
	/**
	 * @param hora Asigna atributo hora
	 */
	public void setHora(String hora)
	{
		this.hora = hora;
	}

	/**
	 * @return Retorna atributo ingreso
	 */
	public Integer getIngreso()
	{
		return ingreso;
	}

	/**
	 * @param ingreso Asigna atributo ingreso
	 */
	public void setIngreso(Integer ingreso)
	{
		this.ingreso = ingreso;
	}

	/**
	 * @return Retorna atributo estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna atributo estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna atributo nombreTipo
	 */
	public String getNombreTipo()
	{
		return nombreTipo;
	}

	/**
	 * @param nombreTipo Asigna atributo nombreTipo
	 */
	public void setNombreTipo(String nombreTipo)
	{
		this.nombreTipo = nombreTipo;
	}

	/**
	 * @return Retorna atributo operacion
	 */
	public String getOperacion()
	{
		return operacion;
	}

	/**
	 * @param operacion Asigna atributo operacion
	 */
	public void setOperacion(String operacion)
	{
		this.operacion = operacion;
	}

	/**
	 * @return Retorna atributo centroAtencion
	 */
	public String getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion Asigna atributo centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return Retorna atributo centroAtencionDuenio
	 */
	public String getCentroAtencionDuenio()
	{
		return centroAtencionDuenio;
	}

	/**
	 * @param centroAtencionDuenio Asigna atributo centroAtencionDuenio
	 */
	public void setCentroAtencionDuenio(String centroAtencionDuenio)
	{
		this.centroAtencionDuenio = centroAtencionDuenio;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorFormateado
	
	 * @return retorna la variable valorFormateado 
	 * @author Yennifer Guerrero 
	 */
	public String getValorFormateado() {
		
		this.valorFormateado = UtilidadTexto.formatearValores(this.valor);
		
		return valorFormateado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorFormateado
	
	 * @param valor para el atributo valorFormateado 
	 * @author Yennifer Guerrero
	 */
	public void setValorFormateado(String valorFormateado) {
		this.valorFormateado = valorFormateado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivoReciboCaja
	
	 * @return retorna la variable consecutivoReciboCaja 
	 * @author Yennifer Guerrero 
	 */
	public String getConsecutivoReciboCaja() {
		return consecutivoReciboCaja;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoReciboCaja
	
	 * @param valor para el atributo consecutivoReciboCaja 
	 * @author Yennifer Guerrero
	 */
	public void setConsecutivoReciboCaja(String consecutivoReciboCaja) {
		this.consecutivoReciboCaja = consecutivoReciboCaja;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivoFactura
	
	 * @return retorna la variable consecutivoFactura 
	 * @author Yennifer Guerrero 
	 */
	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoFactura
	
	 * @param valor para el atributo consecutivoFactura 
	 * @author Yennifer Guerrero
	 */
	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dctoDevueltoAnulado
	
	 * @return retorna la variable dctoDevueltoAnulado 
	 * @author Yennifer Guerrero 
	 */
	public String getDctoDevueltoAnulado() {
		
		if (!this.consecutivoFactura.isEmpty()) {
			this.dctoDevueltoAnulado = this.consecutivoFactura;
		}else if (!this.consecutivoReciboCaja.isEmpty()) {
			this.dctoDevueltoAnulado = this.consecutivoReciboCaja;
		}
		return dctoDevueltoAnulado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dctoDevueltoAnulado
	
	 * @param valor para el atributo dctoDevueltoAnulado 
	 * @author Yennifer Guerrero
	 */
	public void setDctoDevueltoAnulado(String dctoDevueltoAnulado) {
		this.dctoDevueltoAnulado = dctoDevueltoAnulado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ayudanteOperacion
	
	 * @return retorna la variable ayudanteOperacion 
	 * @author Yennifer Guerrero 
	 */
	public String getAyudanteOperacion() {
		
		if (this.operacion.trim().equalsIgnoreCase("suma")) {
			this.ayudanteOperacion = "(+)";
		}else if (this.operacion.trim().equalsIgnoreCase("resta")) {
			this.ayudanteOperacion = "(-)";
		}
		return ayudanteOperacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ayudanteOperacion
	
	 * @param valor para el atributo ayudanteOperacion 
	 * @author Yennifer Guerrero
	 */
	public void setAyudanteOperacion(String ayudanteOperacion) {
		this.ayudanteOperacion = ayudanteOperacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo orden
	
	 * @return retorna la variable orden 
	 * @author Yennifer Guerrero 
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo orden
	
	 * @param valor para el atributo orden 
	 * @author Yennifer Guerrero
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoConsolidadoMovimientos
	
	 * @return retorna la variable listadoConsolidadoMovimientos 
	 * @author Yennifer Guerrero 
	 */
	public ArrayList<DtoMovmimientosAbonos> getListadoConsolidadoMovimientos() {
		return listadoConsolidadoMovimientos;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoConsolidadoMovimientos
	
	 * @param valor para el atributo listadoConsolidadoMovimientos 
	 * @author Yennifer Guerrero
	 */
	public void setListadoConsolidadoMovimientos(
			ArrayList<DtoMovmimientosAbonos> listadoConsolidadoMovimientos) {
		this.listadoConsolidadoMovimientos = listadoConsolidadoMovimientos;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo rutaLogo
	
	 * @return retorna la variable rutaLogo 
	 * @author Yennifer Guerrero 
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo rutaLogo
	
	 * @param valor para el atributo rutaLogo 
	 * @author Yennifer Guerrero
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ubicacionLogo
	
	 * @return retorna la variable ubicacionLogo 
	 * @author Yennifer Guerrero 
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	
	 * @param valor para el atributo ubicacionLogo 
	 * @author Yennifer Guerrero
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo usuarioGeneraReporte
	
	 * @return retorna la variable usuarioGeneraReporte 
	 * @author Yennifer Guerrero 
	 */
	public String getUsuarioGeneraReporte() {
		return usuarioGeneraReporte;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo usuarioGeneraReporte
	
	 * @param valor para el atributo usuarioGeneraReporte 
	 * @author Yennifer Guerrero
	 */
	public void setUsuarioGeneraReporte(String usuarioGeneraReporte) {
		this.usuarioGeneraReporte = usuarioGeneraReporte;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaHoraGeneraReporte
	
	 * @return retorna la variable fechaHoraGeneraReporte 
	 * @author Yennifer Guerrero 
	 */
	public String getFechaHoraGeneraReporte() {
		return fechaHoraGeneraReporte;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaHoraGeneraReporte
	
	 * @param valor para el atributo fechaHoraGeneraReporte 
	 * @author Yennifer Guerrero
	 */
	public void setFechaHoraGeneraReporte(String fechaHoraGeneraReporte) {
		this.fechaHoraGeneraReporte = fechaHoraGeneraReporte;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo razonSocial
	
	 * @return retorna la variable razonSocial 
	 * @author Yennifer Guerrero 
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo razonSocial
	
	 * @param valor para el atributo razonSocial 
	 * @author Yennifer Guerrero
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreInstitucion
	
	 * @return retorna la variable nombreInstitucion 
	 * @author Yennifer Guerrero 
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreInstitucion
	
	 * @param valor para el atributo nombreInstitucion 
	 * @author Yennifer Guerrero
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo actividadEconomica
	
	 * @return retorna la variable actividadEconomica 
	 * @author Yennifer Guerrero 
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo actividadEconomica
	
	 * @param valor para el atributo actividadEconomica 
	 * @author Yennifer Guerrero
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo direccionTelefono
	
	 * @return retorna la variable direccionTelefono 
	 * @author Yennifer Guerrero 
	 */
	public String getDireccionTelefono() {
		return direccionTelefono;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo direccionTelefono
	
	 * @param valor para el atributo direccionTelefono 
	 * @author Yennifer Guerrero
	 */
	public void setDireccionTelefono(String direccionTelefono) {
		this.direccionTelefono = direccionTelefono;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo centroAtencionReporte
	
	 * @return retorna la variable centroAtencionReporte 
	 * @author Yennifer Guerrero 
	 */
	public String getCentroAtencionReporte() {
		return centroAtencionReporte;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo centroAtencionReporte
	
	 * @param valor para el atributo centroAtencionReporte 
	 * @author Yennifer Guerrero
	 */
	public void setCentroAtencionReporte(String centroAtencionReporte) {
		this.centroAtencionReporte = centroAtencionReporte;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nit
	
	 * @return retorna la variable nit 
	 * @author Yennifer Guerrero 
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nit
	
	 * @param valor para el atributo nit 
	 * @author Yennifer Guerrero
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo logoDerecha
	
	 * @return retorna la variable logoDerecha 
	 * @author Yennifer Guerrero 
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo logoDerecha
	
	 * @param valor para el atributo logoDerecha 
	 * @author Yennifer Guerrero
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo logoIzquierda
	
	 * @return retorna la variable logoIzquierda 
	 * @author Yennifer Guerrero 
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo logoIzquierda
	
	 * @param valor para el atributo logoIzquierda 
	 * @author Yennifer Guerrero
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo infoPaciente
	
	 * @return retorna la variable infoPaciente 
	 * @author Yennifer Guerrero 
	 */
	public DtoPaciente getInfoPaciente() {
		return infoPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo infoPaciente
	
	 * @param valor para el atributo infoPaciente 
	 * @author Yennifer Guerrero
	 */
	public void setInfoPaciente(DtoPaciente infoPaciente) {
		this.infoPaciente = infoPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo totalSaldoPaciente
	
	 * @return retorna la variable totalSaldoPaciente 
	 * @author Yennifer Guerrero 
	 */
	public String getTotalSaldoPaciente() {
		return totalSaldoPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo totalSaldoPaciente
	
	 * @param valor para el atributo totalSaldoPaciente 
	 * @author Yennifer Guerrero
	 */
	public void setTotalSaldoPaciente(String totalSaldoPaciente) {
		this.totalSaldoPaciente = totalSaldoPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsInfoPaciente
	
	 * @return retorna la variable dsInfoPaciente 
	 * @author Yennifer Guerrero 
	 */
	public JRDataSource getDsInfoPaciente() {
		return dsInfoPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsInfoPaciente
	
	 * @param valor para el atributo dsInfoPaciente 
	 * @author Yennifer Guerrero
	 */
	public void setDsInfoPaciente(JRDataSource dsInfoPaciente) {
		this.dsInfoPaciente = dsInfoPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsConsolidadoMovimientos
	
	 * @return retorna la variable dsConsolidadoMovimientos 
	 * @author Yennifer Guerrero 
	 */
	public JRDataSource getDsConsolidadoMovimientos() {
		return dsConsolidadoMovimientos;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsConsolidadoMovimientos
	
	 * @param valor para el atributo dsConsolidadoMovimientos 
	 * @author Yennifer Guerrero
	 */
	public void setDsConsolidadoMovimientos(JRDataSource dsConsolidadoMovimientos) {
		this.dsConsolidadoMovimientos = dsConsolidadoMovimientos;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoMovimiento
	
	 * @return retorna la variable tipoMovimiento 
	 * @author Yennifer Guerrero 
	 */
	public String getTipoMovimiento() {
		
		if (this.operacion.trim().equalsIgnoreCase("suma")) {
			this.ayudanteOperacion = "(+)";
		}else if (this.operacion.trim().equalsIgnoreCase("resta")) {
			this.ayudanteOperacion = "(-)";
		}
		
		this.tipoMovimiento = nombreTipo + " " + this.ayudanteOperacion;
		
		return tipoMovimiento;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoMovimiento
	
	 * @param valor para el atributo tipoMovimiento 
	 * @author Yennifer Guerrero
	 */
	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listadoDetalleMovimientos
	
	 * @return retorna la variable listadoDetalleMovimientos 
	 * @author Yennifer Guerrero 
	 */
	public ArrayList<DtoMovmimientosAbonos> getListadoDetalleMovimientos() {
		return listadoDetalleMovimientos;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listadoDetalleMovimientos
	
	 * @param valor para el atributo listadoDetalleMovimientos 
	 * @author Yennifer Guerrero
	 */
	public void setListadoDetalleMovimientos(
			ArrayList<DtoMovmimientosAbonos> listadoDetalleMovimientos) {
		this.listadoDetalleMovimientos = listadoDetalleMovimientos;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaFormateada
	
	 * @return retorna la variable fechaFormateada 
	 * @author Yennifer Guerrero 
	 */
	public String getFechaFormateada() {
		this.fechaFormateada = UtilidadFecha.conversionFormatoFechaAAp(this.fecha);
		return fechaFormateada;
	}
	
	/**
	 * Este Método se encarga de obtener la fecha y hora 
	 * del movimiento de abono.
	 * 
	 * @return retorna la variable fechaFormateada 
	 * @author Yennifer Guerrero 
	 */
	public String getFechaHoraFormateada() {
		String fechaHora = UtilidadFecha.conversionFormatoFechaAAp(this.fecha) +
		"  " + UtilidadFecha.conversionFormatoHoraABD(this.hora);
		
		return fechaHora;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaFormateada
	
	 * @param valor para el atributo fechaFormateada 
	 * @author Yennifer Guerrero
	 */
	public void setFechaFormateada(String fechaFormateada) {
		this.fechaFormateada = fechaFormateada;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsDetalleMovimientos
	
	 * @return retorna la variable dsDetalleMovimientos 
	 * @author Yennifer Guerrero 
	 */
	public JRDataSource getDsDetalleMovimientos() {
		return dsDetalleMovimientos;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsDetalleMovimientos
	
	 * @param valor para el atributo dsDetalleMovimientos 
	 * @author Yennifer Guerrero
	 */
	public void setDsDetalleMovimientos(JRDataSource dsDetalleMovimientos) {
		this.dsDetalleMovimientos = dsDetalleMovimientos;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esConsolidado
	
	 * @return retorna la variable esConsolidado 
	 * @author Yennifer Guerrero 
	 */
	public boolean isEsConsolidado() {
		return esConsolidado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esConsolidado
	
	 * @param valor para el atributo esConsolidado 
	 * @author Yennifer Guerrero
	 */
	public void setEsConsolidado(boolean esConsolidado) {
		this.esConsolidado = esConsolidado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esArchivoPlano
	
	 * @return retorna la variable esArchivoPlano 
	 * @author Yennifer Guerrero 
	 */
	public boolean isEsArchivoPlano() {
		return esArchivoPlano;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esArchivoPlano
	
	 * @param valor para el atributo esArchivoPlano 
	 * @author Yennifer Guerrero
	 */
	public void setEsArchivoPlano(boolean esArchivoPlano) {
		this.esArchivoPlano = esArchivoPlano;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DtoMovmimientosAbonos dtoMovmimientosAbonosComparado) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
		String fechaThis=sdf.format(this.fecha)+" "+this.getHora();
		String fechaComparado=sdf.format(dtoMovmimientosAbonosComparado.getFecha())+" "+dtoMovmimientosAbonosComparado.getHora();
		
		return fechaThis.compareTo(fechaComparado);
	}
	
}
