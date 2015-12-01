/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoParametros;
import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoTotalesDocumento;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.orm.TrasladoCajaPrincipalMayor;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con los Movimientos de Caja -
 * Apertura de caja - Cierre de caja - Arqueo de caja
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see com.servinte.axioma.mundo.impl.tesoreria.MovimientosCajaMundo
 */

public interface IMovimientosCajaMundo {

	
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
	 * Valida que la caja tenga mas turnos abiertos
	 * 
	 * @param usuario
	 * @return
	 */
	

	/**
	 * Retorna el movimiento de caja respectivo
	 * 
	 * @param caja
	 * @return
	 */
	public MovimientosCaja obtenerMovimientoCaja(long pk);

	
	/**
	 * Encargado de retornar el consolidado del movimiento espec&iacute;fico seg&uacute;n
	 * la informaci&oacute;n contenida en {@link MovimientosCaja} y en el {@link UsuarioBasico}
	 * 
	 * @param movimientosCaja
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimiento(MovimientosCaja movimientosCaja);
	
	/**
	 * Encargado de retornar el consolidado del movimiento espec&iacute;fico seg&uacute;n
	 * la informaci&oacute;n contenida en {@link MovimientosCaja} y en el {@link UsuarioBasico}
	 * 
	 * @param movimientosCaja
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimientoArqueoCaja(MovimientosCaja movimientosCaja);

	
	/**
	 * Guardar un Movimiento de caja.
	 *  
	 * @param dtoConsolidadoArqueo
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public boolean guardarMovimientoCaja(DtoConsolidadoMovimiento dtoConsolidadoMovimiento);

	
	 
	/**
	 * Retorna un DTO con la informaci&oacute;n disponible para realizar una Entrega a Caja Mayor / Principal o a una Transportadora
	 * de Valores
	 *
	 * @param movimientosCaja
	 * @param eTipoMovimiento
	 * @return listado con los detalles de documentos por las formas de pago registradas en el sistema disponibles para realizar una entrega
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
	 * Determina cual es el tipo de diferencia entre el valor entregados/cargados desde el sistama 
	 * y el valor recibido que se encuentra actualmente en la caja. Retorna nulo si no existe diferencia alguna
	 * 
	 * @param valorEntregaSistema
	 * @param valorActualCaja
	 * @return
	 */
	public String obtenerTipoDiferencia(double valorEntregaSistema, double valorActualCaja);
	
	
	
	/**
	 * Guardar el Movimiento de caja.
	 *  
	 * @param listaDtoConsolidadoMovimiento
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public boolean guardarMovimientoCaja(ArrayList<DtoInformacionEntrega>  listaDtoInformacionEntrega);
	
	
	/**
	 * Retorna una solicitud de traslado relacionada al movimiento de caja enviado
	 * @param idMovimiento
	 * @return solicitudTrasladoCaja
	 */
	public SolicitudTrasladoCaja obtenerSolicitudTraslado(long idMovimiento);
	
	/**
	 * Retorna una lista de movimientos entregados a una transportadora
	 * @param dtoConsultaEntregaTransportadora
	 * @return
	 */
	
	public List<DtoInformacionEntrega> consultaEntregaTransportadora(DtoConsultaEntregaTransportadora dtoConsultaEntregaTransportadora);


	/**
	 * M&eacute;todo que se encarga de totalizar los documentos relacionados en el arqueo seg&uacute;n el estado del documento
	 * 
	 * @param consolidadoMovimientoDTO
	 * @return ArrayList<{@link DtoTotalesDocumento}>
	 */
	public ArrayList<DtoTotalesDocumento> totalizarDocumentosArqueo(DtoConsolidadoMovimiento consolidadoMovimientoDTO);
	
	/**
	 * M&eacute;todo que se encarga de definir si existen documentos relacionados en el arqueo
	 * 
	 * @param consolidadoMovimientoDTO
	 * @return ArrayList<{@link DtoTotalesDocumento}>
	 */
	public int numeroTotalDocumentosArqueo(ArrayList<DtoTotalesDocumento> dtoTotalesDocumento);
	
	/**
	 * M&eacute;todo que totaliza los valores parciales por forma de pago de los Traslados de caja recibidos y las 
	 * Entregas a transportadora de valores y las Entregas a Caja Mayor principal realizadas.
	 * 
	 * @param consolidadoMovimientoDTO
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public DtoConsolidadoMovimiento totalesParcialesTrasladosEntregas (DtoConsolidadoMovimiento consolidadoMovimientoDTO);
		

	/**
	 * Consolida la informaci&oacute;n de cada una de las consultas parciales relacionadas con Recibos de Caja, 
	 * Devoluciones, Traslados, Entregas a Transportadora o a Caja mayor.
	 * 
	 * La informaci&oacute;n son los totales de cada uno de los registros anteriores por forma de pago 
	 * registrada. Al final obtenemos un consolidado seg&uacute;n la forma de pago para poder realizar 
	 * el cuadre de caja.
	 * 
	 * A la forma de pago definida en los par&aacute;metros generales, m&oacute;dulo Tesoreria - Forma Pago Efectivo, se le suma 
	 * la base en caja.
	 * 
	 * @param consolidadoMovimientoDTO
	 * @param institucion
	 * @param valorBase
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public DtoConsolidadoMovimiento consolidarInformacionPorFormaPago(DtoConsolidadoMovimiento consolidadoMovimientoDTO, double valorBase, int institucion);
	
	
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

	
	/**
	 * Método que se encarga de realizar la consulta de los movimientos de caja de tipo Arqueo, Arqueo Entrega Parcial
	 * y Cierre Turno de caja que cumplan con los parámetros de búsqueda.
	 * 
	 * @param dtoBusquedaCierreArqueo
	 * @return
	 */
	public List<MovimientosCaja> consultarCierreArqueo (DtoBusquedaCierreArqueo dtoBusquedaCierreArqueo);
	
	
	/**
	 * Método que se encarga de realizar la consulta de los movimientos de caja de tipo 
	 * Entrega Caja Mayor o Entrega Transportadora de Valores asociado a un Arqueo Entrega Parcial
	 * 
	 * @param dtoBusquedaCierreArqueo
	 * @return
	 */
	public MovimientosCaja consultarEntregaAsociadaArqueoParcial (long codigoMovimientoArqueo);
	
	
	/**
	 * metodo que se encarga de consultar los cierres pro centros de atencion
	 * @param fecha
	 * @param centroAtencion
	 * @param empresaInstitucion
	 * @param formasPago
	 * @return lista con los consolidados de cierre
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierre(String fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formasPago);
	
	/**
	 * Consultar los cierres por caja cajero 
	 * @param fecha
	 * @param centroAtencion
	 * @param empresaInstitucion
	 * @param formasPago
	 * @return lista con cierres caja / cajero 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierreCajaCajero(String fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formasPago);
	
	/**
	 * Metodo que consulta los traslado a caja mayor principal
	 * @param parametros
	 * @return ArrayList<DtoConsolidadoCierreReporte>
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoCajaMayorPrincipal(DtoConsolidadoCierreReporte parametros);
	
	
	/**
	 * Metodo que consulta los traslado a caja mayor principal
	 * @param parametros
	 * @return ArrayList<DtoConsolidadoCierreReporte>
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoHaciaCajaMayor(DtoConsolidadoCierreReporte parametros);
	
	
	/**
	 *  Guarda la entidad en la base de datos.
	 *  @param transientInstance
	 *  @author Cristhian Murillo
	 *  
	 *  @return boolean
	 */
	public boolean persistTrasladoCajaPrincipalMayor(TrasladoCajaPrincipalMayor transientInstance) ;
	
	
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
//	
//
//	/**
//	 * M&eacute;todo que se encarga de consultar un movimiento de tipo
//	 * Arqueo de Caja realizado previamente.
//	 * 
//	 * @param arqueoCaja
//	 * @return DtoConsolidadoMovimiento con la informaci&oacute;n del movimiento Arqueo Caja realizado
//	 */
//	public DtoConsolidadoMovimiento consultarArqueoCaja (MovimientosCaja arqueoCaja);
//	
	
	
//	/**
//	 * Este m&eacute;todo se encarga de adicionar a la forma de pago efectivo para realizar la entrega (o en el proceso de 
//	 * aceptaci&oacute;n de las solicitudes ), las aceptaciones realizadas de efectivo que no han sido entregadas previamente.
//	 * 
//	 * @param dtoInformacionEntrega
//	 * @param listadoAceptaciones
//	 * @param codigosTipoMovimiento
//	 * @return
//	 */
//	public DtoInformacionEntrega agregarValorEfectivoAceptacion (DtoInformacionEntrega dtoInformacionEntrega, ArrayList<DtoDetalleDocSopor> listadoAceptaciones, ArrayList<Integer> codigosTipoMovimiento, long codigoMovimiento);

	
//	
//	/**
//	 * Retorna un DTO con la informaci&oacute;n que ya fue entregada a Transportadora de valores o a Caja Mayor Principal.
//	 *  
//	 * @param movimientosCaja
//	 * @param eTipoMovimiento
//	 */
//	public DtoInformacionEntrega consolidadoInformacionEntregada (MovimientosCaja movimientosCaja, ETipoMovimiento eTipoMovimiento);
//	
	
	
//	
//	/**
//	 * Retorna un {@link DtoInformacionEntrega} con la información necesaria para realizar las entregas a 
//	 * Caja Mayor / Principal, Entregas a Transportadora y Aceptaciones
//	 *
//	 * @param listadoDefinitivoDocSop
//	 * @param codigoInstitucion
//	 * @param restriccion (este atributo es utilizado para poder recorrer el listado disponible de 
//	 * documentos sin tener en cuenta la parametrización Consignación Banco)
//	 * 
//	 * @return
//	 */
//	public DtoInformacionEntrega obtenerDtoInformacionEntrega(ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop, int codigoInstitucion, boolean restriccion);
//	
}
