package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.AlmacenParametros;

public interface AlmacenParametrosDao{
	
	/**
	 * Consulta registros en base al codigo del centro de costos (subalmacen)
	 * @param Connection con
	 * @param int codigo del centro de costos (subalmacen)
	 * @param int centro de atencion
	 * @param int institucion (*)
	 * @return HashMap
	 * */
	public HashMap consultarAlmacenParametros(Connection con, int codigo, int centroAtencion, int institucion);
	
	/**
	 * Consulta centro de Costos
	 * @param Connection con
	 * @param int institucion
	 * @return HashMap
	 * */
	public HashMap consultarCentroCostos(Connection con, int institucion, int centroAtencion);
	
	
	/**
	 * Inserta un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public boolean insertarAlmacenParametros(Connection con, AlmacenParametros almacenParametros);
	
	/**
	 * Elimina un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public boolean eliminarAlmacenParametros(Connection con, int codigo, int institucion );
	
	/**
	 * Modifica un registro de la base de datos
	 * @param Connection con
	 * @param AlmacenParametros almacenParametros 
	 * */
	public boolean modificarAlmacenParametros(Connection con, AlmacenParametros almacenParametros);
	
	/**
	 * Inicializa la tabla almacen parametros si esta se encuentra vacia
	 * @param Connection con
	 * @param int institucion
	 * @param int opc 1 iniciar tabla, 2 completar tabla
	 * */
	public void iniciarTableAlmacenParametros(Connection con, int institucion, int opc);
	
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean manejaExistenciasNegativas(Connection con, int codigoAlmacen, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */	
	public boolean manejaExistenciasNegativasCentroAten(Connection con, int centroCosto, int codigoInstitucion);
	
	/**
	 * Consulta registro Afecta Costo Promedio
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean afectaCostoPromedio(Connection con, int codigoAlmacen, int codigoInstitucion);
	
}
