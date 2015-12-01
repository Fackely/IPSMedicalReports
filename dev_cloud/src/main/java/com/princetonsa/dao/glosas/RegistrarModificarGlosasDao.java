package com.princetonsa.dao.glosas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.actionform.glosas.RegistrarModificarGlosasForm;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRegistrarModificarGlosasDao;
import com.princetonsa.dto.glosas.DtoObsFacturaGlosas;

/**
 * 
 * @author Angela María Angel amangel@princetonsa.com
 *
 */

public interface RegistrarModificarGlosasDao
{
	/**
	 * Metodo que actualiza la glosa modificada
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int guardar(Connection con, HashMap criterios, String bandera);

	/**
	 * Metodo que consulta el detalle de la glosa
	 * @param con
	 * @param usuario
	 * @param criteriosDetalle
	 * @return
	 */
	public HashMap consultarDetalleGlosa(Connection con, int codGlosa, int institucion);	
	
	/**
	 * Metodo que consulta el historico en factura de todas las glosas
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public HashMap accionHistoricoGlosa(Connection con, String codFactura);
	
	/**
	 * Metodo que consulta Unica Factura por consecutivo
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarUnicaFactura(Connection con, HashMap criterios);
	
	/**
	 * Metodo que guarda las facturas de una Glosa
	 * @param con
	 * @param criterios
	 * @param operacion
	 * @return
	 */
	public int guardarFacturas(Connection con, HashMap criterios, String operacion);
	
	/**
	 * Metodo que consulta todos los contratos por detalle glosa factura
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public HashMap consultaContratosFactura(Connection con, String codigoGlosa);
	
	/**
	 * Metodo que consulta los detalles factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultarDetalleFacturaGlosa(Connection con, String codAudi);
	
	/**
	 * Metodo que consulta los asocios detalles factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultarAsociosDetalleFacturaGlosa(Connection con, String codDetAudi);
	
	/**
	 * Metodo que elimina todos los conceptos del detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarConceptosDetalleFacturaGlosa(Connection con, String codDetAudi);
	
	/**
	 * Metodo que elimina todos los conceptos de los asocios detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarConceptosAsociosDetalleFacturaGlosa(Connection con, String codAsoDetAudi);
	
	/**
	 * Metodo que elimina todos los asocios detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarAsociosDetalleFacturaGlosa(Connection con, String codDetAudi);
	
	/**
	 * Metodo que elimina todos los detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarDetalleFacturaGlosa(Connection con, String codAudi);
	
	/**
	 * Metodo que elimina los conceptos de la Auditoria Glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public boolean eliminarConceptosAudiGlosa(Connection con, String codAudi);
	
	/**
	 * Metodo que elimina la Audi Glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarAuditoriaGlosa(Connection con, String codAudi);
	
	/**
	 * Metodo que consulta los conceptos de la Glosa
	 * @param con
	 * @param estado
	 * @param tipoConcepto
	 * @return
	 */
	public HashMap consultarConceptos(Connection con, String estado, String tipoConcepto);
	
	/**
	 * Metodo que consulta todos los conceptos de todas las
	 * Facturas asociadas a una glosa desde sus solicitudes
	 * hasta los asocios de las mismas
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public HashMap consultarTodosConceptosFacturas(Connection con, String codigoGlosa);
	
	/**
	 * Metodo que guarda los conceptos de la Auditoria Glosa
	 * @param con
	 * @param criterios
	 * @param operacion
	 * @return
	 */
	public boolean guardarConceptoFactura(Connection con, HashMap criterios);
	
	/**
	 * Metodo que consulta las fechas de Radicacion de las Glosas asociadas a una Factura
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public HashMap consultaFechaRadicacion(Connection con, String codFactura);
	
	
	/**
	 * Metodo que consulta las fechas de Radicacion de las cuentas de cobro asociadas a una Factura
	 * @param con
	 * @param codFactura
	 * @param codGlosa
	 */
	public HashMap consultaFechaRadiCC(Connection con, String codFactura, String codGlosa);
	
	/**
	 * Metodo que consulta las Glosas en estado Respondida o Conciliada asociadas a una factura especifica
	 * @param con
	 * @param codFatura
	 * @return
	 */
	public int consultarNumeroGlosasPorFactura(Connection con, String codFactura);
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public String obtenerFechaAprobacionGlosa(String codigo);

	/**
	 * 
	 * @param con
	 * @param codigoAuditoria
	 * @return
	 */
	public ArrayList<DtoObsFacturaGlosas> consultarObsAuditoriaGlosa(Connection con, int codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarObservacion(Connection con,DtoObsFacturaGlosas dto);

	/**
	 * 
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public String consultarEstadoGlosa(Connection con, String codigoGlosa);
	
}