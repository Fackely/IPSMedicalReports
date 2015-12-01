package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.orm.TrasladoCajaPrincipalMayor;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Movimientos de Caja.
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia - Cristhian Murillo
 *
 */

public interface IMovimientosCajaDAO {

	
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
	 * 
	 * @param pk
	 * @return
	 */
	public MovimientosCaja findById(long pk);
	
	/**
	 * 
	 * @param movimientosCaja
	 * @return {@link MovimientosCaja}
	 */

	public boolean guardarMovimientoCaja (MovimientosCaja movimientosCaja);


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
	public List<MovimientosCaja>  consultaEntregaTransportadora (DtoConsultaEntregaTransportadora dtoConsultaEntregaTransportadora);
	
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
	 * Metodo que se encarga de consultar los consoldiados de cierre
	 * @param fecha
	 * @param centroAtencion
	 * @param empresaInstitucion
	 * @param formaspago
	 * @return consulta consoldiado cierre
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierre(Date fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formaspago);
	
	/**
	 * Metodo que se encarga de consultar los cierres por caja cajero
	 * @param fecha
	 * @param centroAtencion
	 * @param empresaInstitucion
	 * @param formaspago
	 * @return lista con los cierres de caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierreCajaCajero(Date fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formaspago);
	
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
}
