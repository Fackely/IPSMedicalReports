package com.princetonsa.dao.postgresql.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.glosas.GlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseGlosasDao;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Anexo 686 Registro Auditoría
 * Creado el 6 Enero de 2009
 * @author Felipe Pérez Granda - Sebastián Gómez
 * @mail lfperez@princetonsa.com - sgomez@princetonsa.com
 */

public class PostgresqlGlosasDao implements GlosasDao
{
	/**
	 * Método encargado de consultar las facturas
	 * @author Felipe Pérez Granda - Sebastián Gómez
	 * @param connection
	 * @param criterios
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> consultarFacturas(Connection connection, HashMap<String, Object> criterios) 
	{
		return SqlBaseGlosasDao.consultarFacturas(connection, criterios);
	}
	
	/**
	 * Método que realiza la inserción de la glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public ResultadoBoolean guardarGlosa(Connection con,DtoGlosa glosa, String remite)
	{
		return SqlBaseGlosasDao.guardarGlosa(con, glosa, remite);
	}
	
	/**
	 * Método implementado para cargar los datos de una glosa
	 * @author Sebastián Gómez R. - Felipe Pérez Granda
	 * @param connection
	 * @param criterios
	 * @return DtoGlosa
	 */
	public DtoGlosa cargarGlosa (Connection connection, HashMap<String, Object> criterios, UsuarioBasico usuario)
	{
		return SqlBaseGlosasDao.cargarGlosa(connection, criterios, usuario);
	}
	
	/**
	 * Método para precargar la glosa cuando se piensa hacer una devolucion
	 * @param con
	 * @param glosa
	 * @return
	 */
	public DtoGlosa cargarGlosaDevolucion(Connection con,DtoGlosa glosa, String forma)
	{
		return SqlBaseGlosasDao.cargarGlosaDevolucion(con, glosa, forma);
	}
	
	/**
	 * Método para cargar las solicitudes de la factura
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap<String, Object> cargarSolicitudesFactura(Connection con,HashMap<String, Object> criterios)
	{
		return SqlBaseGlosasDao.cargarSolicitudesFactura(con, criterios);
	}
	
	/**
	 * Método para cargar los asocios del detalle de la factura
	 * @param con
	 * @param codigoDetalleFactura
	 * @param codigoTarifarioOficial
	 * @return
	 */
	public ArrayList<DtoDetalleAsociosGlosa> cargarAsociosDetalleFactura(Connection con,String codigoDetalleFactura,int codigoTarifarioOficial)
	{
		return SqlBaseGlosasDao.cargarAsociosDetalleFactura(con, codigoDetalleFactura, codigoTarifarioOficial);
	}
	
	/**
	 * Método para consultar las facturas auditadas
	 */
	public HashMap<String, Object> consultarFacturasAuditadas(Connection con,HashMap<String, Object> criterios, ArrayList<Integer> listaConvenios)
	{
		return SqlBaseGlosasDao.consultarFacturasAuditadas(con, criterios, listaConvenios);
	}
	
	/**
	 * 
	 * @param codigoAuditoria
	 * @param observaciones
	 * @return
	 */
	public boolean actualizarObservacionAuditoria(String codigoAuditoria,String observaciones)
	{
		return SqlBaseGlosasDao.actualizarObservacionAuditoria(codigoAuditoria, observaciones);
	}
}