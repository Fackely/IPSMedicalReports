package com.servinte.axioma.dao.fabrica;

import com.servinte.axioma.dao.impl.tesoreria.AceptacionTrasladoCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.AdjuntoMovimientosCajaDAO;
import com.servinte.axioma.dao.impl.tesoreria.AnulacionRecibosCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CajasCajerosHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CajasHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CierreCajaAcepTrasCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CierreCajaTransportadoraHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CierreCajaXAnulaReciboCHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CierreCajaXDevolReciboHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CierreCajaXEntregaCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CierreCajaXReciboCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.ConceptoNotaPacCuentaContHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.ConceptoNotasPacientesHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.ConceptosIngTesoreriaDAO;
import com.servinte.axioma.dao.impl.tesoreria.ConsultaConsolidadoCierreDAO;
import com.servinte.axioma.dao.impl.tesoreria.CuadreCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.CuentasBancariasHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.DetCargosDAO;
import com.servinte.axioma.dao.impl.tesoreria.DetFaltanteSobranteHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.DetalleNotaPacienteHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.DetallePagosRcHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.DevolRecibosCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.DocSopMovimCajasHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.EntidadesFinancierasHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.EntregaCajaMayorHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.EntregaTransportadoraValoresHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.FaltanteSobrantHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.FormasPagoHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.HistoCambioResponsableHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.MovimientosAbonosHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.MovimientosCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.NotaPacienteHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.RecibosCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.SolicitudTrasladoCajasHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.TarjetasFinancierasDAO;
import com.servinte.axioma.dao.impl.tesoreria.TipoCuentaBancariaDAO;
import com.servinte.axioma.dao.impl.tesoreria.TiposMovimientoCajaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.TransportadoraValoresHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.TrasladosAbonosHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.TurnoDeCajasHibernateDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IAceptacionTrasladoCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IAdjuntoMovimientosCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IAnulacionRecibosCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasCajerosDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaAcepTrasCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaTransportadoraDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXAnulaReciboCDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXDevolReciboDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXEntregaCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXReciboCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotaPacCuentaContDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConceptoNotasPacientesDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConceptosIngTesoreriaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConsultaConsolidadoCierreDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICuadreCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICuentasBancariasDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetCargosDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetFaltanteSobranteDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetalleNotaPacienteDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetallePagosRcDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDevolRecibosCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntidadesFinancierasDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IFaltanteSobranteDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IFormasPagoDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IHistoCambioResponsableDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosAbonosDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITarjetasFinancierasDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITipoCuentaBancariaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITiposMovimientoCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITransportadoraValoresDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITrasladosAbonosDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITurnoDeCajaDAO;

/**
 * F&aacute;brica para construir objetos DAO para la l&oacute;gica
 * de Tesorer&iacute;a.
 * 
 * @author Jorge Armando Agudelo Quintero
 */

public  abstract class TesoreriaFabricaDAO {

	private TesoreriaFabricaDAO() {
	
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ITiposMovimientoCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ITiposMovimientoCajaDAO}.
	 */
	public static ITiposMovimientoCajaDAO crearTiposMovimientoCajaDAO() {
		return new TiposMovimientoCajaHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICajasCajerosDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICajasCajerosDAO}.
	 */
	public static ICajasCajerosDAO crearCajasCajerosDAO() {
		return new CajasCajerosHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICajasDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICajasDAO}.
	 */
	public static ICajasDAO crearCajasDAO() {
		return new CajasHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IRecibosCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IRecibosCajaDAO}.
	 */
	public static IRecibosCajaDAO crearRecibosCajaDAO() {
		return new RecibosCajaHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IDevolRecibosCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IDevolRecibosCajaDAO}.
	 */
	public static IDevolRecibosCajaDAO crearDevolRecibosCajaDAO() {
		return new DevolRecibosCajaHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ISolicitudTrasladoCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ISolicitudTrasladoCajaDAO}.
	 */
	public static ISolicitudTrasladoCajaDAO crearSolicitudTrasladoCajaDAO() {
		return new SolicitudTrasladoCajasHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ITurnoDeCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ITurnoDeCajaDAO}.
	 */
	public static ITurnoDeCajaDAO crearTurnoDeCajaDAO() {
		return new TurnoDeCajasHibernateDAO();
	}


	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IEntregaTransportadoraValoresDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IEntregaTransportadoraValoresDAO}.
	 */
	public static IEntregaTransportadoraValoresDAO crearEntregaTransportadoraValoresDAO() {
		
		return new EntregaTransportadoraValoresHibernateDAO();
	}


	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IEntregaCajaMayorDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IEntregaCajaMayorDAO}.
	 */
	public static IEntregaCajaMayorDAO crearEntregaCajaMayorDAO() {
		
		return new EntregaCajaMayorHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IFormasPagoDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IFormasPagoDAO}.
	 */
	public static IFormasPagoDAO crearFormasPagoDAO() {
		
		return new FormasPagoHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IDocSopMovimCajasDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IDocSopMovimCajasDAO}.
	 */
	public static IDocSopMovimCajasDAO crearDocSopMovimCajasDAO() {
		
		return new DocSopMovimCajasHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IMovimientosCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IMovimientosCajaDAO}.
	 */
	public static IMovimientosCajaDAO crearMovimientosCajaDAO() {
		return new MovimientosCajaHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IFaltanteSobranteDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IFaltanteSobranteDAO}.
	 */
	public static IFaltanteSobranteDAO crearFaltanteSobranteDAO() {
		return new FaltanteSobrantHibernateDAO();
	}
	
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IEntidadesFinancierasDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IEntidadesFinancierasDAO}.
	 */
	public static IEntidadesFinancierasDAO crearEntidadFinancieraDAO() {
		return new EntidadesFinancierasHibernateDAO();
	}
	
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IEntidadesFinancierasDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IEntidadesFinancierasDAO}.
	 */
	public static ITransportadoraValoresDAO crearTransportadoraValoresDAO() {
		return new TransportadoraValoresHibernateDAO();
	}
	
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ITrasladosAbonosDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ITrasladosAbonosDAO}.
	 */
	public static ITrasladosAbonosDAO crearTrasladosAbonosDAO() {
		return new TrasladosAbonosHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICuentasBancariasDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IEntidadesFinancierasDAO}.
	 */
	public static ICuentasBancariasDAO crearCuentasBancariasDAO()
	{
		return new CuentasBancariasHibernateDAO();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear una instancia de IHistoCambioResponsableDAO
	 * 
	 * @return IHistoCambioResponsableDAO
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IHistoCambioResponsableDAO crearHistorialCambioResponsableDAO(){
		return new HistoCambioResponsableHibernateDAO();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear una instancia de la entidad
	 * detalle faltante sobrante
	 * 
	 * @return IDetFaltanteSobranteDAO
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static IDetFaltanteSobranteDAO crearDetFaltanteSobranteDAO(){
		return new DetFaltanteSobranteHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IDetallePagosRcDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IDetallePagosRcDAO}.
	 */
	public static IDetallePagosRcDAO crearDetallePagosRcDAO()
	{
		return new DetallePagosRcHibernateDAO();
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de
	 *
	 */
	public static final IAdjuntoMovimientosCajaDAO crearAdjuntosMovimientos(){
		return new AdjuntoMovimientosCajaDAO();
	} 
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXReciboCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXReciboCajaDAO}.
	 */
	public static ICierreCajaXReciboCajaDAO crearCierreCajaXReciboCajaDAO()
	{
		return new CierreCajaXReciboCajaHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXDevolReciboDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXDevolReciboDAO}.
	 */
	public static ICierreCajaXDevolReciboDAO crearCierreCajaXDevolReciboDAO()
	{
		return new CierreCajaXDevolReciboHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXAnulaReciboCDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXAnulaReciboCDAO}.
	 */
	public static ICierreCajaXAnulaReciboCDAO crearCierreCajaXAnulaReciboCDAO()
	{
		return new CierreCajaXAnulaReciboCHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaTransportadoraDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaTransportadoraDAO}.
	 */
	public static ICierreCajaTransportadoraDAO crearCierreCajaTransportadoraDAO()
	{
		return new CierreCajaTransportadoraHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IAceptacionTrasladoCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IAceptacionTrasladoCajaDAO}.
	 */
	public static IAceptacionTrasladoCajaDAO crearAceptacionTrasladoCajaDAO()
	{
		return new AceptacionTrasladoCajaHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaAcepTrasCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaAcepTrasCajaDAO}.
	 */
	public static ICierreCajaAcepTrasCajaDAO crearCierreCajaAcepTrasCajaDAO()
	{
		return new CierreCajaAcepTrasCajaHibernateDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IAnulacionRecibosCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IAnulacionRecibosCajaDAO}.
	 */
	public static IAnulacionRecibosCajaDAO crearAnulacionRecibosCajaDAO()
	{
		return new AnulacionRecibosCajaHibernateDAO();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ICierreCajaXEntregaCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICierreCajaXEntregaCajaDAO}.
	 */
	public static ICierreCajaXEntregaCajaDAO crearCierreCajaXEntregaCajaDAO()
	{
		return new CierreCajaXEntregaCajaHibernateDAO();
		
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link ITipoCuentaBancariaDAO}
	 * @return objeto que es implementaci&oacute;n de {@link ITipoCuentaBancariaDAO}.
	 */
	
	public final static ITipoCuentaBancariaDAO crearTipoCuentaBancariaDAO()
	{
		return new TipoCuentaBancariaDAO();
	}

	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de 
	 * {@link ICuadreCajaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link ICuadreCajaDAO}.
	 */
	public static ICuadreCajaDAO crearCuadreCajaDAO()
	{
		return new CuadreCajaHibernateDAO();
	}

	/**
	 * Crea una instancia concreta de tarjetas financieras
	 * @return {@link ITarjetasFinancierasDAO} Instancia concreta
	 */
	public static ITarjetasFinancierasDAO crearTarjetasFinancieras() {
		return new TarjetasFinancierasDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IConceptosIngTesoreriaDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IConceptosIngTesoreriaDAO}.
	 * 
	 */
	public static IConceptosIngTesoreriaDAO crearConceptosIngTesoreria(){
		return new ConceptosIngTesoreriaDAO();
	}
	
	/**
	 *  Crea y retorna un objeto que es implementaci&oacute;n de
	 *  {@link IConsultaConsolidadoCierreDAO}.
	 * @return @return objeto que es implementaci&oacute;n de  {@link IConsultaConsolidadoCierreDAO}.
	 */
	public static IConsultaConsolidadoCierreDAO crearConsolidacionDAO(){
		return new ConsultaConsolidadoCierreDAO();
	}
	
	/**
	 * Crea y retorna un objeto que es implementaci&oacute;n de
	 * {@link IDetCargosDAO}.
	 * 
	 * @return objeto que es implementaci&oacute;n de {@link IDetCargosDAO}.
	 * 
	 * @author Camilo Gómez
	 */
	public static IDetCargosDAO crearDetCargos(){
		return new DetCargosDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IConceptoNotasPacientesDAO
	 * 
	 * @return IConceptoNotasPacientesDAO
	 * 
	 * @author diecorqu
	 */
	public static IConceptoNotasPacientesDAO crearConceptoNotasPacientesDAO() {
		return new ConceptoNotasPacientesHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IConceptoNotaPacCuentaContDAO
	 * 
	 * @return IConceptoNotaPacCuentaContDAO
	 * 
	 * @author diecorqu
	 */
	public static IConceptoNotaPacCuentaContDAO crearConceptoNotaPacCuentaContDAO() {
		return new ConceptoNotaPacCuentaContHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * INotaPacienteDAO
	 * 
	 * @return INotaPacienteDAO
	 * 
	 * @author diecorqu
	 */
	public static INotaPacienteDAO crearNotaPacienteDAO() {
		return new NotaPacienteHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IDetalleNotaPacienteDAO
	 * 
	 * @return IDetalleNotaPacienteDAO
	 * 
	 * @author diecorqu
	 */
	public static IDetalleNotaPacienteDAO crearDetalleNotaPacienteDAO() {
		return new DetalleNotaPacienteHibernateDAO();
	}
	
	/**
	 * Este Método se encarga de crear una instancia para la entidad
	 * IMovimientosAbonosDAO
	 * 
	 * @return Instancia concreta de {@link IMovimientosAbonosDAO}
	 */
	public static IMovimientosAbonosDAO crearMovimientosAbonosDAO() {
		return new MovimientosAbonosHibernateDAO();
	}
}
