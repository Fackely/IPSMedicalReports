package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.tesoreria.DtoInformacionFormaPago;
import com.princetonsa.mundo.odontologia.TarjetaCliente;
import com.servinte.axioma.orm.Cajas;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
@SuppressWarnings("serial")
public class DtoVentaTarjetasCliente implements Serializable{

	/**
	 * codigo pk de venta tarjeta
	 */
	private	double 	codigoPk;
	/**
	 * Atributo para almacenar el tipo de venta
	 */
	private	String tipoVenta;
	
	/**
	 * Atributo para almacenar el deudor que genera la venta
	 */
	private DtoDeudor deudor;
	
	/**
	 * Tipo de tarjeta que se vende 
	 */
	private double tipoTarjeta ;
	
	/**
	 * Atributo para almacernar el codigo del Servicio 
	 */
	private int codigoServicio;
	
	/**
	 * Valor unitario Tarjeta
	 */
	private double valorUnitarioTarjeta;
	
	/**
	 * Atributo para almacenar la cantidad de tarjeta vendidas 
	 */
	private int cantidad ;
	
	/**
	 * Atributo para mostra los totales de las tarjetas
	 */
	private double valorTotalTarjetas ;
		
	/**
	 * Atributo para almacenar la observacione de la venta
	 */
	private String observaciones ;
	
	/**
	 * Atributo para almacenar el usuario vendedor 
	 */
	private String usuarioVendedor;
	
	/**
	 * Atributo para mostrar el estado de la venta 
	 * TODO FALTA LA ENUMERACION
	 */
	private String estadoVenta;
	
	/**
	 * Atributo para mostrar el consecutivo de la factura
	 */
	private DtoFacturaVaria facturaVaria;
	
	/**
	 * Atributo para almacenar el codigo del la factura
	 */
	private int codigoPkFacturaVaria;
	
	/**
	 * Fecha modifica
	 */
	private String fechaModifica ;
	
	/**
	 * Hora modificar
	 */
	private String horaModifica ;
	
	/**
	 * Usuario que genero la venta
	 */
	private String usuarioModifica ;
	
	/**
	 * 
	 */
	private String numeroVenta;
	
	/**
	 *Atrituto para almacenar el institucion basica 
	 */
	private int institucion;
	
	/**
	 * Atributo tmp para mostrar el valor total de la factura
	 */
	private String valorFactura;
	
	/**
	 * Este atributo hace referencia al consecutivo de la venta
	 */
	private double consecutivoVenta;
	
	/**
	 * Atributo Tmp Para mostrar en presentacion el nombre comprador
	 */
	private String comprador;
	
	/**
	 * Dto Tarjeta Cliente. hace referencia al tipo de tarjeta cliente
	 */
	private TarjetaCliente dtoTarjetaCliente;

	/**
	 * Atributos temporares para hacer busquedas de ventas de tarjetas
	 * hacer referencia a la fecha inicial 
	 */
	private String fechaInicialConsulta;
	
	/**
	 * Atributos temporales para hacer búsquedas de ventas de tarjetas
	 * hacer referencia a la fecha Final
	 */
	private String fechaFinalConsulta;
	
	/**
	 * Lista para, para mostrar en presentación los beneficiarios
	 */
	private ArrayList<DtoBeneficiarioCliente> listaBeneficiarios = new ArrayList<DtoBeneficiarioCliente>();
	
	/**
	 * Dto Venta Empresarial 
	 */
	private  DTOVentaTarjetaEmpresarial dtoVentaEmpresarial;
	
	/**
	 * Atributo para guardar el contrato del convenio seleccionado tarjeta cliente
	 */
	private DtoContrato contratoTarjeta;

	/**
	 * Convenio de la tarjeta seleccionada
	 */
	private int convenioTipoTarjeta;
	
	/**
	 * Caja del usuario
	 */
	private Cajas caja;
	
	/**
	 * Lista con la información de las formas de pago
	 */
	private ArrayList<DtoInformacionFormaPago> listaInformacionFormasPago;
	
	/**
	 * Indice de la forma de pago seleccionada
	 */
	private int indiceFormaPago;
	
	/**
	 * numero del recibo de caja generado
	 */
	private int numeroReciboCaja;
	
	/**
	 * Constructor vacío DtoVenta Tarjeta Cliente
	 */
	public DtoVentaTarjetasCliente()
	{
		reset();
	}
	
	/**
	 * Reset 
	 */
	void reset(){
		
		this.codigoPk =0;
		this.tipoVenta="";
		this.tipoTarjeta=0;
		this.codigoServicio =0;
		this.valorUnitarioTarjeta =0;
		this.cantidad =0;
		this.valorTotalTarjetas =0;
		this.observaciones ="";
		this.usuarioVendedor="";
		
		this.estadoVenta="";
		this.facturaVaria =new DtoFacturaVaria();
		this.fechaModifica ="";
		this.horaModifica ="";
		this.usuarioModifica ="";
		
		this.setListaBeneficiarios(new ArrayList<DtoBeneficiarioCliente>());
		this.numeroVenta="";
		this.institucion=0;
		this.comprador="";
		this.setCodigoPkFacturaVaria(0);
		this.setDeudor(new DtoDeudor());
		this.dtoTarjetaCliente= new TarjetaCliente();
		this.dtoVentaEmpresarial=new DTOVentaTarjetaEmpresarial();
		
		this.contratoTarjeta = new DtoContrato();
		this.caja=null;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica)?UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica): "";
	}

	/**
	 * @return the codigoPk
	 */
	public double getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the tipoVenta
	 */
	public String getTipoVenta() {
		return tipoVenta;
	}

	/**
	 * @param tipoVenta the tipoVenta to set
	 */
	public void setTipoVenta(String tipoVenta) {
		this.tipoVenta = tipoVenta;
	}

	/**
	 * @return the tipoTarjeta
	 */
	public double getTipoTarjeta() {
		return tipoTarjeta;
	}

	/**
	 * @param tipoTarjeta the tipoTarjeta to set
	 */
	public void setTipoTarjeta(double tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the valorUnitarioTarjeta
	 */
	public double getValorUnitarioTarjeta() {
		return valorUnitarioTarjeta;
	}

	/**
	 * @param valorUnitarioTarjeta the valorUnitarioTarjeta to set
	 */
	public void setValorUnitarioTarjeta(double valorUnitarioTarjeta) {
		this.valorUnitarioTarjeta = valorUnitarioTarjeta;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the valorTotalTarjetas
	 */
	public double getValorTotalTarjetas() {
		return valorTotalTarjetas;
	}

	/**
	 * @param valorTotalTarjetas the valorTotalTarjetas to set
	 */
	public void setValorTotalTarjetas(double valorTotalTarjetas) {
		this.valorTotalTarjetas = valorTotalTarjetas;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the usuarioVendedor
	 */
	public String getUsuarioVendedor() {
		return usuarioVendedor;
	}

	/**
	 * @param usuarioVendedor the usuarioVendedor to set
	 */
	public void setUsuarioVendedor(String usuarioVendedor) {
		this.usuarioVendedor = usuarioVendedor;
	}

	/**
	 * @return the estadoVenta
	 */
	public String getEstadoVenta() {
		return estadoVenta;
	}
	
	/**
	 * @return the estadoVenta
	 */
	public String getEstadoVentaBD() {
		return UtilidadFecha.conversionFormatoFechaABD(estadoVenta);
	}

	/**
	 * @param estadoVenta the estadoVenta to set
	 */
	public void setEstadoVenta(String estadoVenta) {
		this.estadoVenta = estadoVenta;
	}

	/**
	 * @return the facturaVaria
	 */
	public DtoFacturaVaria getFacturaVaria() {
		return facturaVaria;
	}

	/**
	 * @param facturaVaria the facturaVaria to set
	 */
	public void setFacturaVaria(DtoFacturaVaria facturaVaria) {
		this.facturaVaria = facturaVaria;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * 
	 * @return
	 */

	public String getFechaModificaBD() {
		return  UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica);
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the numeroVenta
	 */
	public String getNumeroVenta() {
		return numeroVenta;
	}

	/**
	 * @param numeroVenta the numeroVenta to set
	 */
	public void setNumeroVenta(String numeroVenta) {
		this.numeroVenta = numeroVenta;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	/**
	 * @return the listaBeneficiarios
	 */
	public ArrayList<DtoBeneficiarioCliente> getListaBeneficiarios() {
		return listaBeneficiarios;
	}

	/**
	 * @param listaBeneficiarios the listaBeneficiarios to set
	 */
	public void setListaBeneficiarios(
			ArrayList<DtoBeneficiarioCliente> listaBeneficiarios) {
		this.listaBeneficiarios = listaBeneficiarios;
	}

	/**
	 * Obtener el número de beneficiarios
	 * @return entero con la cantidad de beneficiarios existentes
	 */
	public int getCantidadBeneficiarios() {
		if(listaBeneficiarios==null)
		{
			return 0;
		}
		return listaBeneficiarios.size();
	}

	public void setFechaInicialConsulta(String fechaInicialConsulta) {
		this.fechaInicialConsulta = fechaInicialConsulta;
	}

	public String getFechaInicialConsulta() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialConsulta);
	}

	public void setFechaFinalConsulta(String fechaFinalConsulta) {
		this.fechaFinalConsulta = fechaFinalConsulta;
		
	}

	public String getFechaFinalConsulta() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaFinalConsulta);	
	}

	/**
	 * Obtiene el valor del atributo deudor
	 *
	 * @return Retorna atributo deudor
	 */
	public DtoDeudor getDeudor()
	{
		return deudor;
	}

	/**
	 * Establece el valor del atributo deudor
	 *
	 * @param valor para el atributo deudor
	 */
	public void setDeudor(DtoDeudor deudor)
	{
		this.deudor = deudor;
	}

	/**
	 * Obtiene el valor del atributo codigoPkFacturaVaria
	 *
	 * @return Retorna atributo codigoPkFacturaVaria
	 */
	public int getCodigoPkFacturaVaria()
	{
		return codigoPkFacturaVaria;
	}

	/**
	 * Establece el valor del atributo codigoPkFacturaVaria
	 *
	 * @param valor para el atributo codigoPkFacturaVaria
	 */
	public void setCodigoPkFacturaVaria(int codigoPkFacturaVaria)
	{
		this.codigoPkFacturaVaria = codigoPkFacturaVaria;
	}

	/**
	 * Obtiene el valor del atributo valorFactura
	 *
	 * @return Retorna atributo valorFactura
	 */
	public String getValorFactura()
	{
		return valorFactura;
	}

	/**
	 * Establece el valor del atributo valorFactura
	 *
	 * @param valor para el atributo valorFactura
	 */
	public void setValorFactura(String valorFactura)
	{
		this.valorFactura = valorFactura;
	}

	/**
	 * Obtiene el valor del atributo consecutivoVenta
	 *
	 * @return Retorna atributo consecutivoVenta
	 */
	public double getConsecutivoVenta()
	{
		return consecutivoVenta;
	}

	/**
	 * Establece el valor del atributo consecutivoVenta
	 *
	 * @param valor para el atributo consecutivoVenta
	 */
	public void setConsecutivoVenta(double consecutivoVenta)
	{
		this.consecutivoVenta = consecutivoVenta;
	}

	/**
	 * Obtiene el valor del atributo comprador
	 *
	 * @return Retorna atributo comprador
	 */
	public String getComprador()
	{
		return comprador;
	}

	/**
	 * Establece el valor del atributo comprador
	 *
	 * @param valor para el atributo comprador
	 */
	public void setComprador(String comprador)
	{
		this.comprador = comprador;
	}

	/**
	 * Obtiene el valor del atributo dtoTarjetaCliente
	 *
	 * @return Retorna atributo dtoTarjetaCliente
	 */
	public TarjetaCliente getDtoTarjetaCliente()
	{
		return dtoTarjetaCliente;
	}

	/**
	 * Establece el valor del atributo dtoTarjetaCliente
	 *
	 * @param valor para el atributo dtoTarjetaCliente
	 */
	public void setDtoTarjetaCliente(TarjetaCliente dtoTarjetaCliente)
	{
		this.dtoTarjetaCliente = dtoTarjetaCliente;
	}

	/**
	 * Obtiene el valor del atributo dtoVentaEmpresarial
	 *
	 * @return Retorna atributo dtoVentaEmpresarial
	 */
	public DTOVentaTarjetaEmpresarial getDtoVentaEmpresarial()
	{
		return dtoVentaEmpresarial;
	}

	/**
	 * Establece el valor del atributo dtoVentaEmpresarial
	 *
	 * @param valor para el atributo dtoVentaEmpresarial
	 */
	public void setDtoVentaEmpresarial(
			DTOVentaTarjetaEmpresarial dtoVentaEmpresarial)
	{
		this.dtoVentaEmpresarial = dtoVentaEmpresarial;
	}

	/**
	 * Obtiene el valor del atributo contratoTarjeta
	 *
	 * @return Retorna atributo contratoTarjeta
	 */
	public DtoContrato getContratoTarjeta()
	{
		return contratoTarjeta;
	}

	/**
	 * Establece el valor del atributo contratoTarjeta
	 *
	 * @param valor para el atributo contratoTarjeta
	 */
	public void setContratoTarjeta(DtoContrato contratoTarjeta)
	{
		this.contratoTarjeta = contratoTarjeta;
	}

	public Cajas getCaja() {
		return caja;
	}

	public void setCaja(Cajas caja) {
		this.caja = caja;
	}

	public ArrayList<DtoInformacionFormaPago> getListaInformacionFormasPago() {
		return listaInformacionFormasPago;
	}

	public void setListaInformacionFormasPago(
			ArrayList<DtoInformacionFormaPago> listaInformacionFormasPago) {
		this.listaInformacionFormasPago = listaInformacionFormasPago;
	}

	public int getIndiceFormaPago() {
		return indiceFormaPago;
	}

	public void setIndiceFormaPago(int indiceFormaPago) {
		this.indiceFormaPago = indiceFormaPago;
	}

	/**
	 * Retorna la cantidad de formas de pago ingresadas por el usuario
	 * @return int Cantidad de formas de pago
	 */
	public int getCantidadFormasPago()
	{
		if(listaInformacionFormasPago==null)
		{
			return 0;
		}
		return listaInformacionFormasPago.size();
	}
	
	/**
	 * Calcular el valor de la sumatoria de todas las formas de pago
	 * ingresadas por el usuario
	 * @return double con el valor tota lde las formas de pago
	 */
	public double getTotalFormasPago()
	{
		double valor=0;
		int tamanio=listaInformacionFormasPago.size();
		for(int i=0; i<tamanio-1; i++)
		{
			DtoInformacionFormaPago formaPago=listaInformacionFormasPago.get(i);
			valor+=formaPago.getValor();
		}
		return valor;
	}

	/**
	 * Calcular el valor de la sumatoria de todas las formas de pago
	 * ingresadas por el usuario y le da formato
	 * @return String con el valor formateado, listo para mostrar al usuario
	 */
	public String getTotalFormasPagoFormateado()
	{
		return UtilidadTexto.formatearValores(getTotalFormasPago());
	}

	public int getNumeroReciboCaja() {
		return numeroReciboCaja;
	}

	public void setNumeroReciboCaja(int numeroReciboCaja) {
		this.numeroReciboCaja = numeroReciboCaja;
	}

	/**
	 * Obtiene el valor del atributo convenioTipoTarjeta
	 *
	 * @return Retorna atributo convenioTipoTarjeta
	 */
	public int getConvenioTipoTarjeta()
	{
		return convenioTipoTarjeta;
	}

	/**
	 * Establece el valor del atributo convenioTipoTarjeta
	 *
	 * @param valor para el atributo convenioTipoTarjeta
	 */
	public void setConvenioTipoTarjeta(int convenioTipoTarjeta)
	{
		this.convenioTipoTarjeta = convenioTipoTarjeta;
	}


}
