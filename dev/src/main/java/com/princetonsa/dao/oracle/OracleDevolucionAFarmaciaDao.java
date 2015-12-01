/*
 * @(#)OracleDevolucionAFarmaciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;

import util.ValoresPorDefecto;

import com.princetonsa.dao.DevolucionAFarmaciaDao;
import com.princetonsa.dao.sqlbase.SqlBaseDevolucionAFarmaciaDao;

/**
 * Implementaci�n Oracle de las funciones de acceso a la fuente de datos
 * para un la devoluci�n de medicmentos
 *
 * @version 1.0, Sept. 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */

public class OracleDevolucionAFarmaciaDao implements DevolucionAFarmaciaDao
{//SIN PROBAR FUNC. SECUENCIA
	
	/**
	 *  Inserci�n del DETALLE de la  devoluci�n del medicamento
	 */
	private final static String insertarDetalleDevolucionMedicamentoStr= 	"INSERT INTO " +
																											"detalle_devol_med " +
																											"(codigo,devolucion,numero_solicitud,articulo,cantidad, lote,fecha_vencimiento)"+
																											"VALUES (seq_detalle_devol_med.nextval, " +
																											"?, ?, ?, ?, ?, ?)"; 
	
	/**
	 *  Inserci�n de la devoluci�n de medicamentos
	 */
	private final static String insertarDevolucionMedicamentosStr= 	"INSERT INTO " +
																									"devolucion_med " +
																									"(codigo,observaciones,fecha,hora,fecha_grabacion,hora_grabacion,usuario,estado,centro_costo_devuelve,farmacia,tipo_devolucion, motivo, institucion)" +
																									"VALUES " +
																									"(seq_devolucion_med.nextval,?,?,?," +
																									"CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?, ?, ?)";
	
	/**
	 * Inserta una devoluci�n de medicamentos
	 * @param con, Connection
	 * @param motivo, String
	 * @param fecha, String
	 * @param hora, String
	 * @param usuario, String
	 * @param estadoDevolucion, int
 	 * @param tipoDevolucion, (Autom�tica - Manual)
	 * @return int (0 -ultimoCodigoSequence) 
	 */
	public int insertarDevolucionMedicamentos		(	Connection con, String observaciones,  
	        																String fecha, String hora, 
																			String usuario, int estadoDevolucion,
																			int centroCostoDevuelve, int farmacia, int tipoDevolucion, String motivo, int codigoInstitucion)
	{
		return SqlBaseDevolucionAFarmaciaDao.insertarDevolucionMedicamentos(con,observaciones,fecha,hora,usuario,estadoDevolucion,centroCostoDevuelve,farmacia,tipoDevolucion, insertarDevolucionMedicamentosStr, motivo, codigoInstitucion);
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
	 * Inserta el DETALLE de la devoluci�n del medicamento
	 * @param con, Connection
	 * @param devolucion, C�digo de la tabla devolucion_med
	 * @param numeroSolicitud, N�mero de la solicitud
	 * @param articulo, c�digo del art�culo a devolver
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
