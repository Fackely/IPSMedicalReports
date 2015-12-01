package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoParametros;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoSolicitudTrasladoPendiente;
import com.princetonsa.dto.tesoreria.DtoTurnoDeCajaApta;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITiposMovimientoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITurnoDeCajaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;

/**
 * @author Cristhian Murillo
 *
 */

public class MovimientosCajaServicio implements IMovimientosCajaServicio {
	
	
	private IMovimientosCajaMundo movimientosCajaMundo;
	private ITiposMovimientoCajaMundo tiposMovimientoCajaMundo;
	private ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo;
	private ITurnoDeCajaMundo turnoDeCajaMundo;
	private IDocSopMovimCajasMundo docSopMovimCajasMundo;
	private IUsuariosMundo usuariosMundo;
	
	
	public MovimientosCajaServicio(){
		movimientosCajaMundo 		= TesoreriaFabricaMundo.crearMovimientosCajaMundo(); 
		tiposMovimientoCajaMundo 	= TesoreriaFabricaMundo.crearTiposMovimientoCajaMundo();
		solicitudTrasladoCajaMundo	= TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
		turnoDeCajaMundo			= TesoreriaFabricaMundo.crearTurnoDeCajaMundo();
		docSopMovimCajasMundo		= TesoreriaFabricaMundo.crearDocSopMovimCajasMundo();
		usuariosMundo				= TesoreriaFabricaMundo.crearUsuariosMundo();
	}


	/* Tipos Movimiento Caja Mundo */
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerListadoTiposArqueo()
	 */
	public List<TiposMovimientoCaja> obtenerListadoTiposArqueo(){
		return tiposMovimientoCajaMundo.obtenerListadoTiposArqueo();
	}
	
	
	/* Solicitud Traslado CajaMundo */
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerSolicitudesPendientes(com.servinte.axioma.orm.Cajas)
	 */
	@Override
	public ArrayList<DtoSolicitudTrasladoPendiente> obtenerSolicitudesPendientes(Cajas caja) {
		return solicitudTrasladoCajaMundo.obtenerSolicitudesPendientes(caja);
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#tieneSolicitudDeTraslado(com.servinte.axioma.orm.Cajas)
	 */
	@Override
	public boolean tieneSolicitudDeTraslado(Cajas caja) {
		return solicitudTrasladoCajaMundo.tieneSolicitudDeTraslado(caja);
	}
	

	/* Movimientos Caja Mundo */
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerMovimientoCaja(long)
	 */
	@Override
	public MovimientosCaja obtenerMovimientoCaja(long pk) {
		return movimientosCajaMundo.obtenerMovimientoCaja(pk);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerConsolidadoMovimiento(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimiento(MovimientosCaja movimientosCaja) {
		
		return movimientosCajaMundo.obtenerConsolidadoMovimiento(movimientosCaja);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerConsolidadoMovimientoArqueoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimientoArqueoCaja(MovimientosCaja movimientosCaja) {
		
		return movimientosCajaMundo.obtenerConsolidadoMovimientoArqueoCaja(movimientosCaja);
	}
	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerUltimoTurnoCierre(com.servinte.axioma.orm.Cajas, com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override public MovimientosCaja obtenerUltimoTurnoCierre(Cajas caja, MovimientosCaja movimientoCaja)
	{
		return movimientosCajaMundo.obtenerUltimoTurnoCierre(caja, movimientoCaja);
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#guardarMovimientoCaja(com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento)
	 */
	@Override
	public boolean guardarMovimientoCaja(DtoConsolidadoMovimiento dtoConsolidadoMovimiento) {
		
		return movimientosCajaMundo.guardarMovimientoCaja(dtoConsolidadoMovimiento);
	}
	
	
	/* Turno de Caja Mundo */
	
	@Override
	public DtoTurnoDeCajaApta esCajaAptaParaApertura(UsuarioBasico usuario, Cajas caja) {
		return turnoDeCajaMundo.esCajaAptaParaApertura(usuario, caja);
	}
		
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#validarTurnosCajeroCerrados(com.princetonsa.mundo.UsuarioBasico)
	 */
	@Override
	public boolean validarTurnosCajeroCerrados(UsuarioBasico usuario) {
		return turnoDeCajaMundo.validarTurnosCajeroCerrados(usuario);
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#guardarTurno(com.servinte.axioma.orm.TurnoDeCaja, java.util.ArrayList)
	 */
	@Override
	public boolean guardarTurno(TurnoDeCaja turno, ArrayList<DtoInformacionEntrega>  listaDtoInformacionEntrega) {
		return turnoDeCajaMundo.guardarTurno(turno, listaDtoInformacionEntrega);
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerTurnoCajaAbiertoPorCajaCajero(com.servinte.axioma.orm.Usuarios, int, int)
	 */
	@Override
	public TurnoDeCaja obtenerTurnoCajaAbiertoPorCajaCajero(DtoUsuarioPersona usuario, int consecutivoCaja,int consecutivoCentroAtencion) {
		
		return turnoDeCajaMundo.obtenerTurnoCajaAbiertoPorCajaCajero(usuario, consecutivoCaja, consecutivoCentroAtencion);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerTurnoPorMovimiento(long)
	 */
	@Override
	public TurnoDeCaja obtenerTurnoPorMovimiento(long idMovimientoCaja) {
		return turnoDeCajaMundo.obtenerTurnoPorMovimiento(idMovimientoCaja);
	}


	/* Doc Sop Movim Cajas Mundo */

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#consolidarInformacionAceptacion(long, int)
	 */
	@Override
	public DtoInformacionEntrega consolidarInformacionAceptacion(long idMovimiento, int codigoInstitucion) {
		return docSopMovimCajasMundo.consolidarInformacionAceptacion(idMovimiento, codigoInstitucion);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#consolidadoInformacionEntrega(com.servinte.axioma.orm.MovimientosCaja, com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento)
	 */
	@Override
	public DtoInformacionEntrega consolidadoInformacionEntrega(MovimientosCaja movimientosCaja,  ETipoMovimiento eTipoMovimiento) {
		
		return movimientosCajaMundo.consolidadoInformacionEntrega(movimientosCaja, eTipoMovimiento);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#completarConsolidadoInformacionAceptacion(com.princetonsa.dto.tesoreria.DtoInformacionEntrega, com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento)
	 */
	@Override
	public DtoInformacionEntrega completarConsolidadoInformacionAceptacion(DtoInformacionEntrega informacionEntregaDTO, ETipoMovimiento eTipoMovimiento) {
		return movimientosCajaMundo.completarConsolidadoInformacionAceptacion(informacionEntregaDTO, eTipoMovimiento);
	}

	
	/* Usuarios Mundo */
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerUsuariosActivosDiferenteDe(int, java.lang.String)
	 */
	@Override
	public List<DtoUsuarioPersona> obtenerUsuariosActivosDiferenteDe(int codInstitucion, String loginUsuario, boolean incluirInactivos) {
		return usuariosMundo.obtenerUsuariosActivosDiferenteDe(codInstitucion, loginUsuario, incluirInactivos);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#consultaEntregaTransportadora(com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora)
	 */
	@Override
	public List<DtoInformacionEntrega> consultaEntregaTransportadora(DtoConsultaEntregaTransportadora dtoConsultaEntregaTransportadora) {

		return movimientosCajaMundo.consultaEntregaTransportadora(dtoConsultaEntregaTransportadora);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#validarTurnoCajaAbierta(com.princetonsa.mundo.UsuarioBasico, int)
	 */
	@Override
	public boolean validarTurnoCajaAbierta(UsuarioBasico usuario, int codigoCaja)
	{
		IMovimientosCajaServicio movimientosCajaServicio=TesoreriaFabricaServicio.crearMovimientosCajaServicio();
		UtilidadTransaccion.getTransaccion().begin();
		DtoTurnoDeCajaApta dtoApto = movimientosCajaServicio.esCajaAptaParaApertura(usuario, new CajasDelegate().findById(codigoCaja));
		UtilidadTransaccion.getTransaccion().commit();
		return !dtoApto.isTieneTurnoCajaCerrado() && !dtoApto.isTieneTurnoCajeroCerrado();

	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#validarDefinicionesParametrosAperturaTurno(com.princetonsa.mundo.UsuarioBasico)
	 */
	@Override
	public DtoParametros validarDefinicionesParametrosAperturaTurno(UsuarioBasico usuario) 
	{
		return movimientosCajaMundo.validarDefinicionesParametrosAperturaTurno(usuario);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#obtenerUltimaFechaMovimientoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public long obtenerUltimaFechaMovimientoCaja(TurnoDeCaja turnoDeCaja) {
		
		return movimientosCajaMundo.obtenerUltimaFechaMovimientoCaja(turnoDeCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#calcularValoresParaArqueos(com.princetonsa.dto.tesoreria.DtoInformacionEntrega, com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento)
	 */
	@Override
	public DtoInformacionEntrega calcularValoresParaArqueos(DtoInformacionEntrega informacionEntregaDTO, ETipoMovimiento eTipoMovimiento) {
		
		return movimientosCajaMundo.calcularValoresParaArqueos(informacionEntregaDTO, eTipoMovimiento);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio#consolidarInformacionSolicitudTrasCajaRealizada(int, long)
	 */
	@Override
	public DtoInformacionEntrega consolidarInformacionSolicitudTrasCajaRealizada(int codigoInstitucion, long codigoSolicitud) {
		
		return movimientosCajaMundo.consolidarInformacionSolicitudTrasCajaRealizada(codigoInstitucion, codigoSolicitud);
	}

}
