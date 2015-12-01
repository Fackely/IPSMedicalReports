/*
 * @(#)PostgresqlDevolucionAFarmaciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;

import util.ValoresPorDefecto;

import com.princetonsa.dao.DevolucionAFarmaciaDao;
import com.princetonsa.dao.sqlbase.SqlBaseDevolucionAFarmaciaDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para un la devolución de medicmentos
 *
 * @version 1.0, Sept. 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */

public class PostgresqlDevolucionAFarmaciaDao implements DevolucionAFarmaciaDao 
{
	/**
	 *  Inserción del DETALLE de la  devolución del medicamento
	 */
	private final static String insertarDetalleDevolucionMedicamentoStr= 	"INSERT INTO " +
																											"detalle_devol_med " +
																											"(codigo,devolucion,numero_solicitud,articulo,cantidad, lote, fecha_vencimiento)" +
																											"VALUES " +
																											"(nextval('seq_detalle_devol_med'), " +
																											"?, ?, ?, ?, ?, ? )"; 
	
	/**
	 *  Inserción de la devolución de medicamentos
	 */
	private final static String insertarDevolucionMedicamentosStr= 	"INSERT INTO " +
																									"devolucion_med " +
																									"(codigo,observaciones,fecha,hora,fecha_grabacion,hora_grabacion,usuario,estado,centro_costo_devuelve,farmacia,tipo_devolucion, motivo, institucion)" +
																									"VALUES " +
																									"(nextval('seq_devolucion_med'),?,?,?," +
																									"CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?, ?, ?)";
	
	/**
	 * Inserta una devolución de medicamentos
	 * @param con, Connection
	 * @param motivo, String
	 * @param fecha, String
	 * @param hora, String
	 * @param usuario, String
	 * @param estadoDevolucion, int
 	 * @param tipoDevolucion, (Automática - Manual)
	 * @return int (0 -ultimoCodigoSequence)
	 */
	public int insertarDevolucionMedicamentos		(	Connection con, String observaciones,  
	        																String fecha, String hora, 
																			String usuario, int estadoDevolucion,
																			int centroCostoDevuelve, int farmacia, int tipoDevolucion, String motivo, int institucion)
	{
		return SqlBaseDevolucionAFarmaciaDao.insertarDevolucionMedicamentos(con,observaciones,fecha,hora,usuario,estadoDevolucion,centroCostoDevuelve,farmacia,tipoDevolucion, insertarDevolucionMedicamentosStr, motivo, institucion);
	}
	
	/**
	 * Carga la ultima devolucion  insertada   (table= devolucion_med))
	 * @param con
	 * @return
	 */
	public  int cargarUltimoCodigoSequence(Connection con)
	{
		return SqlBaseDevolucionAFarmaciaDao.cargarUltimoCodigoSequence(con);
	}

	/**
	 * Inserta el DETALLE de la devolución del medicamento
	 * @param con, Connection
	 * @param devolucion, Código de la tabla devolucion_med
	 * @param numeroSolicitud, Número de la solicitud
	 * @param articulo, código del artículo a devolver
	 * @param cantidad, cantidad
	 * @return
	 */
	public  int insertarDetalleDevolucionMedicamentos		(	Connection con, int  devolucion,  
			   																			int numeroSolicitud, int articulo, 
																						int cantidad,
																						String lote,
																						String fechaVencimientoLote)
	{
		return SqlBaseDevolucionAFarmaciaDao.insertarDetalleDevolucionMedicamentos(con,devolucion,numeroSolicitud,articulo,cantidad, insertarDetalleDevolucionMedicamentoStr, lote, fechaVencimientoLote);
	}
	
	/**
	 * Indica si existe o no devolucuion para un numero de solicutd -Articulo especificos
	 * @param con
	 * @param numeroSolicitudStr
	 * @param codigoArticuloStr
	 * @return
	 */
	public boolean existeDevolucionParaNumeroSolicitudArticulo(Connection con, String numeroSolicitudStr, String codigoArticuloStr)
	{
		return SqlBaseDevolucionAFarmaciaDao.existeDevolucionParaNumeroSolicitudArticulo(con, numeroSolicitudStr, codigoArticuloStr);
	}
	
}
