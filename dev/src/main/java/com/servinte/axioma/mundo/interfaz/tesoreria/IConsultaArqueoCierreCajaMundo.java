
package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con las consultas de
 * los movimientos de caja de tipo Arqueo
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.ConsultaArqueoCierreCajaMundo
 */

public interface IConsultaArqueoCierreCajaMundo {

	
	/**
	 * M&eacute;todo que se encarga de consultar toda la informaci&oacute;n asociada a un moviento de caja
	 * de tipo Cierre Turno de Caja.
	 * 
	 * @param movimientoCierreTurno
	 * @return DtoInformacionEntrega con la informaci&oacute;n consultada para el movimiento
	 * de caja de tipo Cierre Turno de Caja
	 */
	public DtoInformacionEntrega consultarCierreTurnoCaja(MovimientosCaja movimientoCierreTurno);
	
	
	/**
	 * M&eacute;todo que se encarga de consultar un movimiento de tipo
	 * Arqueo de Caja realizado previamente.
	 * 
	 * @param arqueoCaja
	 * @return DtoConsolidadoMovimiento con la informaci&oacute;n del movimiento Arqueo Caja realizado
	 */
	public DtoConsolidadoMovimiento consultarArqueoCaja (MovimientosCaja arqueoCaja);
	
	
	/**
	 * Retorna un DTO con la informaci&oacute;n relacionada a una Entrega a Transportadora de valores o a una Entrega
	 * a Caja Mayor Principal que ya fue realizada. Se pasa como par&aacute;metro el c&oacute;digo de la entrega realizada 
	 * junto con el tipo de movimiento de caja asociado.
	 *
	 * @param codigoEntrega
	 * @param eTipoMovimiento
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoInformacionEntrega consultarArqueoParcialPorEntrega(long codigoEntrega, ETipoMovimiento eTipoMovimiento, int codigoInstitucion);
	
	/**
	 * Retorna un {@link DtoInformacionEntrega} con la informaci&oacute;n asociada a un Arqueo Entrega Parcial,
	 * ya sea una Entrega a Transportadora de Valores o una Entrega a Caja Mayor Principal.
	 *
	 * @param listadoDefinitivoDocSop
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoInformacionEntrega obtenerDtoInformacionEntregaImpresion (ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop, int codigoInstitucion);
	
	
	
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
}
