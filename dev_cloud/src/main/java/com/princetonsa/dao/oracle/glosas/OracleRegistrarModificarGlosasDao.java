package com.princetonsa.dao.oracle.glosas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.actionform.glosas.RegistrarModificarGlosasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.RegistrarModificarGlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRegistrarModificarGlosasDao;
import com.princetonsa.dto.glosas.DtoObsFacturaGlosas;

/**
 * 
 * @author Angela María Angel amangel@princetonsa.com
 *
 */

public class OracleRegistrarModificarGlosasDao implements RegistrarModificarGlosasDao
{
	/**
	 * Metodo que actualiza la glosa modificada
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int guardar(Connection con, HashMap criterios, String bandera)
	{
		return SqlBaseRegistrarModificarGlosasDao.guardar(con, criterios, bandera);
	}

	/**
	 * Metodo que consulta el detalle de la glosa
	 */
	public HashMap consultarDetalleGlosa(Connection con, int codGlosa, int institucion) {
		return SqlBaseRegistrarModificarGlosasDao.consultarDetalleGlosa(con, codGlosa, institucion);
	}	
	
	/**
	 *Metodo que consulta el historico de factura en todas las glosas 
	 */
	public HashMap accionHistoricoGlosa(Connection con, String codFactura){
		return SqlBaseRegistrarModificarGlosasDao.accionHistoricoGlosa(con, codFactura);
	}
	
	/**
	 * Metodo que consulta Unica Factura por consecutivo
	 */
	public HashMap consultarUnicaFactura(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultarUnicaFactura(con, criterios);
	}
	
	/**
	 * Metodo que guarda las faturas de una Glosa
	 */
	public int guardarFacturas(Connection con, HashMap criterios, String operacion)
	{
		return SqlBaseRegistrarModificarGlosasDao.guardarFacturas(con, criterios, operacion);
	}
	
	/**
	 * Metodo que consulta todos los contratos por detalle glosa factura
	 */
	public HashMap consultaContratosFactura(Connection con, String codigoGlosa)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultaContratosFactura(con, codigoGlosa);
	}
	
	/**
	 * Metodo que consulta los detalles factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultarDetalleFacturaGlosa(Connection con, String codAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultarDetalleFacturaGlosa(con, codAudi);
	}
	
	/**
	 * Metodo que consulta los asocios detalles factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultarAsociosDetalleFacturaGlosa(Connection con, String codDetAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultarAsociosDetalleFacturaGlosa(con, codDetAudi);
	}
	
	/**
	 * Metodo que elimina todos los conceptos del detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarConceptosDetalleFacturaGlosa(Connection con, String codDetAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.eliminarConceptosDetalleFacturaGlosa(con, codDetAudi);
	}
	
	/**
	 * Metodo que elimina todos los conceptos de los asocios detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarConceptosAsociosDetalleFacturaGlosa(Connection con, String codAsoDetAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.eliminarConceptosAsociosDetalleFacturaGlosa(con, codAsoDetAudi);
	}
	
	/**
	 * Metodo que elimina todos los asocios detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarAsociosDetalleFacturaGlosa(Connection con, String codDetAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.eliminarAsociosDetalleFacturaGlosa(con, codDetAudi);
	}
	
	/**
	 * Metodo que elimina todos los detalle factura glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarDetalleFacturaGlosa(Connection con, String codAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.eliminarDetalleFacturaGlosa(con, codAudi);
	}
	
	/**
	 * Metodo que elimina los conceptos de la Auditoria Glosa
	 */
	public boolean eliminarConceptosAudiGlosa(Connection con, String codAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.eliminarConceptosAudiGlosa(con, codAudi);
	}
	
	/**
	 * Metodo que elimina la Audi Glosa
	 * @param con
	 * @param codDetAudi
	 * @return
	 */
	public boolean eliminarAuditoriaGlosa(Connection con, String codAudi)
	{
		return SqlBaseRegistrarModificarGlosasDao.eliminarAuditoriaGlosa(con, codAudi);
	}
	
	/**
	 * Metodo que consulta los conceptos de la Glosa
	 * @param con
	 * @param estado
	 * @param tipoConcepto 
	 * @return
	 */
	public HashMap consultarConceptos(Connection con, String estado, String tipoConcepto)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultarConceptos(con, estado, tipoConcepto);
	}
	
	/**
	 * Metodo que consulta todos los conceptos de todas las
	 * Facturas asociadas a una glosa desde sus solicitudes
	 * hasta los asocios de las mismas
	 * @param con
	 * @param codigoGlosa
	 */
	public HashMap consultarTodosConceptosFacturas(Connection con, String codigoGlosa)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultarTodosConceptosFacturas(con, codigoGlosa);
	}
	
	/**
	 * Metodo que guarda los conceptos de la auditoria Glosa
	 */
	public boolean guardarConceptoFactura(Connection con, HashMap criterios)
	{
		return SqlBaseRegistrarModificarGlosasDao.guardarConceptoFactura(con, criterios);
	} 
	
	/**
	 * Metodo que consulta las fechas de Radicacion de las Glosas asociadas a una Factura
	 */
	public HashMap consultaFechaRadicacion(Connection con, String codFactura)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultaFechaRadicacion(con, codFactura, DaoFactory.ORACLE);
	}
	
	/**
	 * 
	 */
	public HashMap consultaFechaRadiCC(Connection con, String codFactura, String codGlosa)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultaFechaRadiCC(con, codFactura, codGlosa, DaoFactory.ORACLE);
	}
	
	/**
	 * Metodo que consulta las Glosas en estado Respondida o Conciliada asociadas a una factura especifica
	 * @param con
	 * @param codFatura
	 * @return
	 */
	public int consultarNumeroGlosasPorFactura(Connection con, String codFactura)
	{
		return SqlBaseRegistrarModificarGlosasDao.consultarNumeroGlosasPorFactura(con, codFactura);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public String obtenerFechaAprobacionGlosa(String codigo)
	{
		return SqlBaseRegistrarModificarGlosasDao.obtenerFechaAprobacionGlosa(codigo);
	}

	@Override
	public ArrayList<DtoObsFacturaGlosas> consultarObsAuditoriaGlosa(
			Connection con, int codigoFactura) {
		return SqlBaseRegistrarModificarGlosasDao.consultarObsAuditoriaGlosa(con,codigoFactura);

	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	@Override
	public boolean insertarObservacion(Connection con,DtoObsFacturaGlosas dto)
	{
		return SqlBaseRegistrarModificarGlosasDao.insertarObservacion(con,dto);
	}
	
	@Override
	public String consultarEstadoGlosa(Connection con, String codigoGlosa) 
	{
		return SqlBaseRegistrarModificarGlosasDao.consultarEstadoGlosa(con,codigoGlosa);
	}
}