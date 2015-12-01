package com.servinte.axioma.servicio.fabrica;

import com.servinte.axioma.orm.ConceptosIngTesoreria;
import com.servinte.axioma.servicio.impl.tesoreria.AceptacionTrasladoCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.AdjuntoMovimientosCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.CajasCajerosServicio;
import com.servinte.axioma.servicio.impl.tesoreria.CajasServicio;
import com.servinte.axioma.servicio.impl.tesoreria.ConceptosIngTesoreriaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.ConsultaArqueoCierreCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.CuentasBancariasServicio;
import com.servinte.axioma.servicio.impl.tesoreria.DetFaltanteSobranteServicio;
import com.servinte.axioma.servicio.impl.tesoreria.EntidadesFinancierasServicio;
import com.servinte.axioma.servicio.impl.tesoreria.EntregaTransportadoraValoresServicio;
import com.servinte.axioma.servicio.impl.tesoreria.FaltanteSobranteServicio;
import com.servinte.axioma.servicio.impl.tesoreria.FormasPagoServicio;
import com.servinte.axioma.servicio.impl.tesoreria.HistoCambioResponsableServicio;
import com.servinte.axioma.servicio.impl.tesoreria.LogTarjetasFinancieraServicio;
import com.servinte.axioma.servicio.impl.tesoreria.MovimientosCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.RecibosCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.SolicitudTrasladoCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.TarjetaFinancieraServicio;
import com.servinte.axioma.servicio.impl.tesoreria.TipoCuentaBancariaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.TiposMovimientoCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.TransportadoraValoresServicio;
import com.servinte.axioma.servicio.impl.tesoreria.TurnoDeCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IAceptacionTrasladoCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IAdjuntoMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConceptosIngTesoreriaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICuentasBancariasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IDetFaltanteSobranteServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IEntidadesFinancierasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IEntregaTransportadoraValoresServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFaltanteSobranteServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFormasPagoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IHistoCambioResponsableServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILogTarjetasFinancieraServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ISolicitudTrasladoCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITarjetaFinancieraServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITipoCuentaBancariaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposMovimientoCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITransportadoraValoresServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITurnoDeCajaServicio;

/**
 * F&aacute;brica para objetos de la capa de servicios
 * relacionados con las funcionalidades de 
 * Tesorer&iacute;a
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio
 */

public abstract class TesoreriaFabricaServicio {

	
	/**
	 * Fábrica para los objetos MovimientosCaja
	 * @autor Jorge Armando Agudelo - Luis Alejandro Echandia
	 * @return
	 */
	public static IMovimientosCajaServicio crearMovimientosCajaServicio()
	{
		return new MovimientosCajaServicio();
	}
	
	/**
	 * F&aacute;brica para los objetos CajasCajero
	 * @autor Jorge Armando Agudelo - Luis Alejandro Echandia
	 * @return
	 */
	public static ICajasCajerosServicio crearCajasCajerosServicio()
	{
		return new CajasCajerosServicio();
	}

	/**
	 * F&aacute;brica para los objetos Cajas
	 * @return
	 */
	public static ICajasServicio crearCajasServicio()
	{
		return new CajasServicio();
	}
	
	
	
	/**
	 * F&aacute;brica para EntidadesFinancierasServicio
	 * @return
	 */
	public static IEntidadesFinancierasServicio crearEntidadesFinancierasServicio()
	{
		return new EntidadesFinancierasServicio();
	}
	
	
	/**
	 * F&aacute;brica para los objetos TransportadoraValores
	 * @return
	 */
	public static ITransportadoraValoresServicio crearTransportadoraValoresServicio()
	{
		return new TransportadoraValoresServicio();
	}

	
	/**
	 * F&aacute;brica para el servicio de Faltante Sobrante
	 * @return
	 */
	public static IFaltanteSobranteServicio crearFaltanteSobranteServicio()
	{
		return new FaltanteSobranteServicio(); 
	}
	
	/**
	 * F&aacute;brica para el servicio de Tipos Movimiento Caja
	 * @return ITiposMovimientoCajaServicio
	 */
	public static ITiposMovimientoCajaServicio crearTiposMovimentoCajaServicio()
	{
		return new TiposMovimientoCajaServicio(); 
	}
	
	
	/**
	 * F&aacute;brica para CuentasBancariasServicio
	 * @return
	 */
	public static ICuentasBancariasServicio crearCuentasBancariasServicio()
	{
		return new CuentasBancariasServicio();
	}
	
	/**
	 * Se encarga de crear una instancia de la clase
	 * IHistoCambioResponsableServicio
	 * 
	 *  @return IHistoCambioResponsableServicio
	 *  
	 */
	public static IHistoCambioResponsableServicio crearHistoCambioResponsableServicio(){
		return new HistoCambioResponsableServicio();
	}
	
	/**
	 * Se encarga de crear una instancia de la clase
	 * IHistoCambioResponsableServicio
	 * 
	 * @return IDetFaltanteSobranteServicio
	 *  
	 */
	public static IDetFaltanteSobranteServicio crearDetFaltanteSobranteServicio(){
		return new DetFaltanteSobranteServicio();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear una Instancia de la clase 
	 * IEntregaTransportadoraValoresServicio
	 * @author, Diana Carolina G
	 *
	 */
	public static IEntregaTransportadoraValoresServicio crearEntregaTransportadoraValoresServicio(){
		return new EntregaTransportadoraValoresServicio();
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de crear una Instancia de la clase
	 * IAdjuntoMovimientosCajaServicio
	 * @author, Diana Carolina G
	 */
	public static final  IAdjuntoMovimientosCajaServicio crearAdjuntoMovimiento(){
		return new AdjuntoMovimientosCajaServicio();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una Instancia de la clase 
	 * ISolicitudTrasladoCajaServicio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static ISolicitudTrasladoCajaServicio crearSolicitudTrasladoCaja(){
		return new SolicitudTrasladoCajaServicio();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una Instancia de la clase 
	 * ITipoCuentaBancariaServicio
	 * @author, Diana Carolina G
	 *
	 */
	
	public static ITipoCuentaBancariaServicio crearTipoCuentaBancariaServicio(){
		return new TipoCuentaBancariaServicio();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IConsultaArqueoCierreCajaServicio}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IConsultaArqueoCierreCajaServicio}.
	 */
	public static IConsultaArqueoCierreCajaServicio crearConsultaArqueoCierreCajaServicio()
	{
		return new ConsultaArqueoCierreCajaServicio();
	}

	/**
	 * Crea servicio de tarjetas financieras
	 * @return Instancia concreta de {@link ITarjetaFinancieraServicio}
	 */
	public static ITarjetaFinancieraServicio crearTarjetasFinancieras() {
		return new TarjetaFinancieraServicio();
	}
	
	/**
	 * Crea servicio de turno de caja servicio
	 * @return Instancia concreta de {@link ITurnoDeCajaServicio}
	 */
	public static ITurnoDeCajaServicio crearTurnoDeCajaServicio()
	{
		return new TurnoDeCajaServicio();
	}
	
	
	/**
	 * Crea servicio de Recibos de Caja
	 * @return Instancia concreta de {@link IRecibosCajaServicio}
	 */
	public static IRecibosCajaServicio crearRecibosCajaServicio()
	{
		return new RecibosCajaServicio();
	}
	
	/**
	 * Crea servicio de Formas de Pago
	 * @return Instancia concreta de {@link IFormasPagoServicio}
	 */
	public static IFormasPagoServicio crearFormasPagoServicio()
	{
		return new FormasPagoServicio();
	}
	
	
	/**
	 * Crea servicio de Aceptación de Solicitudes de Traslado a Caja de Recaudo
	 * @return Instancia concreta de {@link IAceptacionTrasladoCajaServicio}
	 */
	public static IAceptacionTrasladoCajaServicio crearAceptacionTrasladoCajaServicio()
	{
		return new AceptacionTrasladoCajaServicio();
	}
	
	
	/**
	 * Retorna la instancia
	 * @return Instancia concreta de {@link ILogTarjetasFinancieraServicio}
	 */
	public static ILogTarjetasFinancieraServicio crearLogTarjetasFinancieraServicio()
	{
		return new LogTarjetasFinancieraServicio();
	}
	
	/**
	 * Este M&eacute;todo se encarga de crear una Instancia de la clase 
	 *IConceptosIngTesoreriaServicio
	 * @return
	 */
	public static IConceptosIngTesoreriaServicio crearConceptosIngTesoreriaServicio(){
		return new ConceptosIngTesoreriaServicio();
	}
	
}
