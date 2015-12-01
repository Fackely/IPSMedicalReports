package com.servinte.axioma.servicio.interfaz.tesoreria;

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
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.Usuarios;

/**
 * Servicio que le delega al negocio las operaciones de turno de caja
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.servicio.impl.tesoreria.MovimientosCajaServicio
 */
public interface IMovimientosCajaServicio {
	

	/**
	 * Valida que el usuario tiene mas turnos abiertos
	 * @param usuario
	 * @return
	 */
	public boolean validarTurnosCajeroCerrados(UsuarioBasico usuario);

	
	/**
	 * Retorna true si la caja tiene solicitudes de traslado en proceso
	 * @param caja
	 * @return
	 */
	public boolean tieneSolicitudDeTraslado(Cajas caja);
	
	
	/**
	 * Valida que el cajero tenga turnos cerrados y que la caja no tenga solicitud
	 * de traslado
	 * @param caja
	 * @param usuario
	 * @return
	 */
	public DtoTurnoDeCajaApta esCajaAptaParaApertura(UsuarioBasico usuario, Cajas caja);
	
	/**
	 * 
	 * Retorna un {@link TurnoDeCaja} si el usuario enviado tiene uno en estado abierto y asociado a esa caja,
	 * a el centro de atenci&oacute;n espec&iacute;fico.
	 * 	
	 * @param usuario
	 * @param consecutivoCaja
	 * @param consecutivoCentroAtencion
	 * @return TurnoDeCaja para ese usuario/cajero en estado abierto.
	 */
	public TurnoDeCaja obtenerTurnoCajaAbiertoPorCajaCajero(DtoUsuarioPersona usuario, int consecutivoCaja, int consecutivoCentroAtencion);
	
	
	
	/**
	 * Retorna las solicitudes de traslado para la caja dada
	 * @param caja
	 * @return
	 */
	public ArrayList<DtoSolicitudTrasladoPendiente> obtenerSolicitudesPendientes(Cajas caja);
	
	
	
	/**
	 * Retorna una lista si el usuario enviado tiene turnos de caja abiertos.
	 * Si el movimiento de caja enviado es nulo, solo se tendra en cuenta al caja para buscar su ultimo cierre
	 * De lo contrario el turno de cierre debe corresponder al movimiento enviado.
	 * 
	 * @param caja
	 * @param movCaja
	 * @return
	 */
	public MovimientosCaja obtenerUltimoTurnoCierre(Cajas caja, MovimientosCaja movimientoCaja);
	
	
	
	/**
	 * Retorna listado con los tipos de arqueo de la entidad tipos_movimientos_caja
	 * 
	 * @autor Jorge Armando Agudelo - Luis Alejandro Echandia
	 * @return List<{@link TiposMovimientoCaja}>
	 */
	public List<TiposMovimientoCaja> obtenerListadoTiposArqueo();
	
	
	
	/**
	 * Retorna el movimiento de caja respectivo
	 * @param caja
	 * @return
	 */
	public MovimientosCaja obtenerMovimientoCaja(long pk);

	

	/**
	 * Encargado de retornar el consolidado del Movimiento espec&iacute;fico seg&uacute;n la informaci&oacute;n
	 * contenida en {@link MovimientosCaja}
	 * 
	 * @param movimientosCaja
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimiento(MovimientosCaja movimientosCaja);
	
	/**
	 * Encargado de retornar el consolidado del Movimiento espec&iacute;fico seg&uacute;n la informaci&oacute;n
	 * contenida en {@link MovimientosCaja}
	 * 
	 * @param movimientosCaja
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimientoArqueoCaja(MovimientosCaja movimientosCaja);
	
	
	
	/**
	 * Retorna el movimiento de caja respectivo
	 * @param caja
	 * @param dtoInformacionEntrega
	 * @return
	 */
	public boolean guardarTurno(TurnoDeCaja turno, ArrayList<DtoInformacionEntrega> listaDtoInformacionEntrega);
	
	/**
	 * 
	 * 
	 * @param dtoConsolidadoArqueo
	 * @param eTipoMovimiento 
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public boolean guardarMovimientoCaja(DtoConsolidadoMovimiento dtoConsolidadoMovimiento);
	
	
	
	/**
	 * Listado de los detalles de documentos/movimientos para la aceptacion
	 * @param idMovimiento
	 * @return 
	 */
	public DtoInformacionEntrega consolidarInformacionAceptacion(long idMovimiento, int codigoInstitucion);
	
	
	/**
	 * Retorna un consolidado con los detalles por forma de pago y su relacion con las entidades financieras 
	 * cuando la forma de pago este relacionada a esta.
	 * 
	 * @param movimientosCaja
	 * @param eTipoMovimiento
	 * @return listado con los detalles de documentos por las formas de pago registradas en el sistema
	 * @see DtoInformacionEntrega
	 */
	public DtoInformacionEntrega consolidadoInformacionEntrega (MovimientosCaja movimientosCaja,  ETipoMovimiento eTipoMovimiento);
	
	
	
	/**
	 * Retorna la informacion de la entrega con los valores correspondientes a las diferencias y sus totales
	 * 
	 * @param informacionEntregaDTO
	 * @return
	 */
	public DtoInformacionEntrega completarConsolidadoInformacionAceptacion(DtoInformacionEntrega informacionEntregaDTO, ETipoMovimiento eTipoMovimiento);
	
	
	
	/**
	 * Retorna Los usuarios activos relacionados con el centro de atencion y diferentes del usuario enviado
	 * @param usuario
	 * @return List<{@link Usuarios}>
	 */
	public List<DtoUsuarioPersona> obtenerUsuariosActivosDiferenteDe(int codInstitucion, String loginUsuario, boolean incluirInactivos);
	
	
	/**
	 * Retorna el turno de caja del movimiento dado
	 * @param idMovimientoCaja
	 * @return TurnoDeCaja
	 */
	public TurnoDeCaja obtenerTurnoPorMovimiento(long idMovimientoCaja);
	
	
	/** Retorna una lista de movimientos entregados a una transportadora
	 * @param dtoConsultaEntregaTransportadora
	 * @return
	 */
		
	public List<DtoInformacionEntrega> consultaEntregaTransportadora(DtoConsultaEntregaTransportadora dtoConsultaEntregaTransportadora);
	

	/**
	 * 
	 * @param usuario
	 * @param codigoCaja
	 * @return true en caso de tener turno abierto, false de lo contrario
	 */
	public boolean validarTurnoCajaAbierta(UsuarioBasico usuario, int codigoCaja);
	
	/**
	 * Retorna un DTO con la informaci&oacute;n de una Solicitud de Traslado a Caja de Recaudo
	 * realizada y que se encuentra asociada a un movimiento de Cierre de Turno de caja.
	 *  
	 * @param codigoInstitucion
	 * @param codigoSolicitud
	 * @return DtoInformacionEntrega
	 * 
	 */
	public DtoInformacionEntrega consolidarInformacionSolicitudTrasCajaRealizada (int codigoInstitucion, long codigoSolicitud);
	
	
	/**
	 * Valida el estado de los parametros necesarios para la apertura de turno
	 * @param usuario
	 * @return DtoParametros
	 */
	public DtoParametros validarDefinicionesParametrosAperturaTurno(UsuarioBasico usuario);
	
	
	/**
	 * M&eacute;todo que se encarga de obtener la fecha m&aacute;xima registrada
	 * para un movimiento de caja. Teniendo en cuenta que los movimientos de caja son:
	 * 
	 * Recibos de caja
	 * Anulaciones de recibos de caja
	 * Devoluciones de Recibos de caja
	 * Egresos
	 * Solicitudes de Traslados de caja realizadas y aceptadas.
	 * Entregas a transportadora de valores o cajas mayor/principal.
	 * 
	 * @param turnoDeCaja
	 * @return
	 */
	public long obtenerUltimaFechaMovimientoCaja (TurnoDeCaja turnoDeCaja);
	
	
	/**
	 * M&eacute;todo utilizado para calcular los valores de diferencia, valores recaudados en caja y registro correcto
	 * de informaci&oacute;n, necesario para continuar con las operaciones de un movimiento espec&iacute;fico.
	 * 
	 * @param informacionEntregaDTO
	 * @param eTipoMovimiento
	 * @return
	 */
	public DtoInformacionEntrega calcularValoresParaArqueos(DtoInformacionEntrega informacionEntregaDTO, ETipoMovimiento eTipoMovimiento);


//	/**
//	 * Retorna un DTO con la informaci&oacute;n relacionada a una Entrega a Transportadora de valores o a una Caja Mayor Principal
//	 * que ya fue realizada. Se pasa como par&aacute;metro el codigo de la entrega realizada junto con el tipo de movimiento de 
//	 * caja asociado.
//	 *
//	 * @param codigoEntrega
//	 * @param eTipoMovimiento
//	 * @param codigoInstitucion
//	 * @return
//	 */
//	public DtoInformacionEntrega obtenerInformacionEntregaPorMovimiento (long codigoEntrega, ETipoMovimiento eTipoMovimiento, int codigoInstitucion);

//	/**
//	 * M&eacute;todo que se encarga de consultar un movimiento de tipo
//	 * Arqueo de Caja realizado previamente.
//	 * 
//	 * @param arqueoCaja
//	 * @return DtoConsolidadoMovimiento con la informaci&oacute;n del movimiento Arqueo Caja realizado
//	 */
//	public DtoConsolidadoMovimiento consultarArqueoCaja (MovimientosCaja arqueoCaja);

//	/**
//	 * Retorna un DTO con la informaci&oacute;n que ya fue entregada a Transportadora de valores o a Caja Mayor Principal.
//	 *  
//	 * @param movimientosCaja
//	 * @param eTipoMovimiento
//	 */
//	public DtoInformacionEntrega consolidadoInformacionEntregada (MovimientosCaja movimientosCaja, ETipoMovimiento eTipoMovimiento);
//	
	
//	/**
//	 * 
//	 * Retorna un {@link DtoInformacionEntrega} con la informaci&oacute;n necesaria para realizar las entregas a 
//	 * Caja Mayor / Principal, Entregas a Transportadora y Aceptaciones
//	 *
//	 * @param listadoDefinitivoDocSop
//	 * @param codigoInstitucion
//	 * @param restriccion (este atributo es utilizado para poder recorrer el listado disponible de 
//	 * documentos sin tener en cuenta la parametrización Consignación Banco)
//	 * @return
//	 *
//	 */
//	public DtoInformacionEntrega obtenerDtoInformacionEntrega(ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop, int codigoInstitucion, boolean restriccion);
//	
}
