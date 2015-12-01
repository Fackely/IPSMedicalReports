/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.mundo.fabrica;

import com.servinte.axioma.mundo.impl.administracion.UsuariosMundo;
import com.servinte.axioma.mundo.impl.tesoreria.AceptacionTrasladoCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.AdjuntoMovimientosCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.AnulacionRecibosCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CajasCajerosMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CajasMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CierreCajaAcepTrasCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CierreCajaTransportadoraMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXAnulaReciboCMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXDevolReciboMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXEntregaCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CierreCajaXReciboCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ConceptoNotaPacCuentaContMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ConceptoNotasPacientesMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ConceptosIngTesoreriaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ConsultaConsolidadoCierresMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CuadreCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.CuentasBancariasMundo;
import com.servinte.axioma.mundo.impl.tesoreria.DetCargosMundo;
import com.servinte.axioma.mundo.impl.tesoreria.DetFaltanteSobranteMundo;
import com.servinte.axioma.mundo.impl.tesoreria.DetalleNotaPacienteMundo;
import com.servinte.axioma.mundo.impl.tesoreria.DetallePagosRcMundo;
import com.servinte.axioma.mundo.impl.tesoreria.DevolRecibosCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.DocSopMovimCajasMundo;
import com.servinte.axioma.mundo.impl.tesoreria.EntidadesFinancierasMundo;
import com.servinte.axioma.mundo.impl.tesoreria.EntregaCajaMayorMundo;
import com.servinte.axioma.mundo.impl.tesoreria.EntregaTransportadoraValoresMundo;
import com.servinte.axioma.mundo.impl.tesoreria.FaltanteSobranteMundo;
import com.servinte.axioma.mundo.impl.tesoreria.FormasPagoMundo;
import com.servinte.axioma.mundo.impl.tesoreria.HistoCambioResponsableMundo;
import com.servinte.axioma.mundo.impl.tesoreria.LogTarjetasFinancieraMundo;
import com.servinte.axioma.mundo.impl.tesoreria.MovimientosAbonosMundo;
import com.servinte.axioma.mundo.impl.tesoreria.MovimientosCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.NotaPacienteMundo;
import com.servinte.axioma.mundo.impl.tesoreria.RecibosCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.SolicitudTrasladoCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.TarjetaFinancieraMundo;
import com.servinte.axioma.mundo.impl.tesoreria.TipoCuentaBancariaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.TiposMovimientoCajaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.TransportadoraValoresMundo;
import com.servinte.axioma.mundo.impl.tesoreria.TrasladosAbonoMundo;
import com.servinte.axioma.mundo.impl.tesoreria.TurnoDeCajaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAdjuntoMovimientosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAnulacionRecibosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasCajerosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaAcepTrasCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaTransportadoraMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXAnulaReciboCMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXDevolReciboMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXEntregaCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXReciboCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptosIngTesoreriaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICuadreCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICuentasBancariasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetCargosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetFaltanteSobranteMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetalleNotaPacienteMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetallePagosRcMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDevolRecibosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntidadesFinancierasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFaltanteSobranteMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IHistoCambioResponsableMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ILogTarjetasFinancieraMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosAbonosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITarjetaFinancieraMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITipoCuentaBancariaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITiposMovimientoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITransportadoraValoresMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITrasladosAbonoMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITurnoDeCajaMundo;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITarjetaFinancieraServicio;

/**
 * F&aacute;brica para contruir objetos para el m&oacute;dulo de tesorer&iacute;a
 * @author Cristhian Murillo
 *
 */
public abstract class TesoreriaFabricaMundo {
	
	private TesoreriaFabricaMundo(){}
	
	
	/**
	 * F&aacute;brica para los temas de Movimientos de Caja
	 * @return
	 */
	public static IMovimientosCajaMundo crearMovimientosCajaMundo()
	{
		return new MovimientosCajaMundo();
	}
	
	/**
	 * F&aacute;brica para los Tipos de Movimiento
	 * @return
	 */
	public static ITiposMovimientoCajaMundo crearTiposMovimientoCajaMundo()
	{
		return new TiposMovimientoCajaMundo();
	}
	
	
	/**
	 * F&aacute;brica para Caja
	 * @return
	 */
	public static ICajasMundo crearCajasMundo()
	{
		return new CajasMundo();
	}
	
	/**
	 * F&aacute;brica para CajasCajeros
	 * @return
	 */
	public static ICajasCajerosMundo crearCajasCajerosMundo()
	{
		return new CajasCajerosMundo();
	}
	
	
	/**
	 * F&aacute;brica para Recibos de Caja
	 * @return
	 */
	public static IRecibosCajaMundo crearRecibosCajaMundo()
	{
		return new RecibosCajaMundo();
	}

	
	/**
	 * F&aacute;brica para Recibos de Caja
	 * @return 
	 */
	public static IDevolRecibosCajaMundo crearDevolRecibosCajaMundo()
	{
		return new DevolRecibosCajaMundo();
	}
	
	/**
	 * F&aacute;brica para SolicitudeTrasladoCaja
	 * @return
	 */
	public static ISolicitudTrasladoCajaMundo crearSolicitudTrasladoCajaMundo()
	{
		return new SolicitudTrasladoCajaMundo();
	}
	
	/**
	 * F&aacute;brica para la creaci&oacute;n de objetos del mundo
	 * que contienen l&oacute;gica referente a Entregas a transportadora de valores.
	 * @return
	 */
	public static IEntregaTransportadoraValoresMundo crearEntregaTransportadoraValoresMundo()
	{
		return new EntregaTransportadoraValoresMundo();
	}
	
	/**
	 * F&aacute;brica para la creaci&oacute;n de objetos del mundo
	 * que contienen l&oacute;gica referente a Entregas a caja Mayor Principal
	 * @return
	 */
	public static IEntregaCajaMayorMundo crearEntregaCajaMayorMundo()
	{
		return new EntregaCajaMayorMundo();
	}
	
	
	/**
	 * F&aacute;brica para la creaci&oacute;n de objetos del mundo
	 * que contienen l&oacute;gica referente a las Formas de Pago
	 * @return
	 */
	public static IFormasPagoMundo crearFormasPagoMundo()
	{
		return new FormasPagoMundo();
	}
	
	
	/**
	 * F&aacute;brica para la creaci&oacute;n de objetos del mundo
	 * que contienen l&oacute;gica referente a los 
	 * documentos de soporte de los movimientos de caja
	 * @return
	 */
	public static IDocSopMovimCajasMundo crearDocSopMovimCajasMundo()
	{
		return new DocSopMovimCajasMundo();
	}
	
	
	/**
	 * F&aacute;brica para TurnoDeCajaMundo
	 * @return
	 */
	public static ITurnoDeCajaMundo crearTurnoDeCajaMundo()
	{
		return new TurnoDeCajaMundo();
	}
	
	
	
	/**
	 * F&aacute;brica para FaltanteSobranteMundo
	 * @return
	 */
	public static IFaltanteSobranteMundo crearFaltanteSobranteMundo()
	{
		return new FaltanteSobranteMundo();
	}
	
	

	/**
	 * Fabrica para EntidadesFinancierasMundo
	 * @return
	 */
	public static IEntidadesFinancierasMundo crearEntidadesFinancierasMundo()
	{
		return new EntidadesFinancierasMundo();
	}
	
	
	
	/**
	 * F&aacute;brica para Usuarios
	 * @return
	 */
	public static IUsuariosMundo crearUsuariosMundo()
	{
		return new UsuariosMundo();
	}
	
	/**
	 * F&aacute;brica para TransportadoraValoresMundo
	 * @return
	 */
	public static ITransportadoraValoresMundo crearTransportadoraValoresMundo()
	{
		return new TransportadoraValoresMundo();
	}
	
	
	
	
	/**
	 * F&aacute;brica para Traslados de abono
	 * @return
	 */
	public static ITrasladosAbonoMundo crearTrasladosAbonoMundo()
	{
		return new TrasladosAbonoMundo();
	}


	/**
	 * F&aacute;brica para CuentasBancariasMundo
	 * @return
	 */
	public static ICuentasBancariasMundo crearCuentasBancariasMundo()
	{
		return new CuentasBancariasMundo();
	}
	
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear una  instancia del 
	 * objeto IHistoCambioResponsableMundo
	 * 
	 * @return IHistoCambioResponsableMundo
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoCambioResponsableMundo crearHistorialCambioResponsableMundo(){
		return new HistoCambioResponsableMundo();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear una  instancia del 
	 * objeto IHistoCambioResponsableMundo
	 * 
	 * @return IHistoCambioResponsableMundo
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IDetFaltanteSobranteMundo crearDetFaltanteSobranteMundo(){
		return new DetFaltanteSobranteMundo();
	}
	

	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IDetallePagosRcMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IDetallePagosRcMundo}.
	 */
	public static IDetallePagosRcMundo crearDetallePagosRcMundo()
	{
		return new DetallePagosRcMundo();
	}
	
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de
	 * 
	 *
	 */
	public static final IAdjuntoMovimientosCajaMundo crearAdjuntoMovimientosCaja(){
		return new  AdjuntoMovimientosCajaMundo();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXReciboCajaMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXReciboCajaMundo}.
	 */
	public static ICierreCajaXReciboCajaMundo crearCierreCajaXReciboCajaMundo()
	{
		return new CierreCajaXReciboCajaMundo();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXDevolReciboMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXDevolReciboMundo}.
	 */
	public static ICierreCajaXDevolReciboMundo crearCierreCajaXDevolReciboMundo()
	{
		return new CierreCajaXDevolReciboMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXAnulaReciboCMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXAnulaReciboCMundo}.
	 */
	public static ICierreCajaXAnulaReciboCMundo crearCierreCajaXAnulaReciboCMundo()
	{
		return new CierreCajaXAnulaReciboCMundo();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaTransportadoraMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXAnulaReciboCMundo}.
	 */
	public static ICierreCajaTransportadoraMundo crearCierreCajaTransportadoraMundo()
	{
		return new CierreCajaTransportadoraMundo();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IAceptacionTrasladoCajaMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IAceptacionTrasladoCajaMundo}.
	 */
	public static IAceptacionTrasladoCajaMundo crearAceptacionTrasladoCajaMundo()
	{
		return new AceptacionTrasladoCajaMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaAcepTrasCajaMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaAcepTrasCajaMundo}.
	 */
	public static ICierreCajaAcepTrasCajaMundo crearCierreCajaAcepTrasCajaMundo()
	{
		return new CierreCajaAcepTrasCajaMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IAnulacionRecibosCajaMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IAnulacionRecibosCajaMundo}.
	 */
	public static IAnulacionRecibosCajaMundo crearAnulacionRecibosCajaMundo()
	{
		return new AnulacionRecibosCajaMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXEntregaCajaMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXEntregaCajaMundo}.
	 */
	public static ICierreCajaXEntregaCajaMundo crearCierreCajaXEntregaCajaMundo()
	{
		return new CierreCajaXEntregaCajaMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IConsultaArqueoCierreCajaMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IConsultaArqueoCierreCajaMundo}.
	 */
	public static IConsultaArqueoCierreCajaMundo crearConsultaArqueoCierreCajaMundo()
	{
		return new ConsultaArqueoCierreCajaMundo();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de 
	 * {@link ITipoCuentaBancariaMundo}.
	 * @return objeto que es implementaci&oacute;n de {@link ITipoCuentaBancariaMundo}.
	 */
	public static ITipoCuentaBancariaMundo crearTipoCuentaBancariaMundo ()
	{
		return new TipoCuentaBancariaMundo();
	}

	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de 
	 * {@link ICuadreCajaMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICuadreCajaMundo}.
	 */
	public static ICuadreCajaMundo crearCuadreCajaMundo ()
	{
		return new CuadreCajaMundo();
	}

	/**
	 * Crea servicio de tarjetas financieras
	 * @return Instancia concreta de {@link ITarjetaFinancieraServicio}
	 */
	public static ITarjetaFinancieraMundo crearTarjetasFinancieras() {
		return new TarjetaFinancieraMundo();
	}
	
	
	/**
	 * F&aacute;brica para los temas de Tarjeta Financiera
	 * @return
	 */
	public static ILogTarjetasFinancieraMundo crearLogTarjetasFinancieraMundo()
	{
		return new LogTarjetasFinancieraMundo();
	}

	public static IConceptosIngTesoreriaMundo crearConceptosIngTesoreriaMundo(){
		return new ConceptosIngTesoreriaMundo();
	}
	
	/**
	 * Metodo encargado de crear una instacia del mundo
	 * @return instancia del mundo de cierre
	 */
	public static IConsultaConsolidadoCierresMundo crearConsolidadoCierreMundo(){
		return new ConsultaConsolidadoCierresMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IDetCargosMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IDetCargosMundo}.
	 * 
	 * @author Camilo Gómez
	 */
	public static IDetCargosMundo crearDetCargosMundo(){
		return new DetCargosMundo();
	}

	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IConceptoNotasPacientesMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IConceptoNotasPacientesMundo}.
	 * 
	 * @author diecorqu
	 */
	public static IConceptoNotasPacientesMundo crearConceptoNotasPacientes(){
		return new ConceptoNotasPacientesMundo();
	}

	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IConceptoNotaPacCuentaContMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IConceptoNotaPacCuentaContMundo}.
	 * 
	 * @author diecorqu
	 */
	public static IConceptoNotaPacCuentaContMundo crearConceptoNotaPacCuentaCont(){
		return new ConceptoNotaPacCuentaContMundo();
	}

	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link INotaPacienteMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link INotaPacienteMundo}.
	 * 
	 * @author diecorqu
	 */
	public static INotaPacienteMundo crearNotaPacienteMundo(){
		return new NotaPacienteMundo();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IDetalleNotaPacienteMundo}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IDetalleNotaPacienteMundo}.
	 * 
	 * @author diecorqu
	 */
	public static IDetalleNotaPacienteMundo crearDetalleNotaPacienteMundo(){
		return new DetalleNotaPacienteMundo();
	}
	
	/**
	 * Este Método se encarga de crear una Instancia de la clase
	 * IMovimientosAbonosMundo
	 * 
	 * @return Instancia concreta de {@link IMovimientosAbonosMundo}
	 */
	public static IMovimientosAbonosMundo crearMovimientosAbonosMundo() {
		return new MovimientosAbonosMundo();
	}
	
	
}
