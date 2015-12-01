
/*
 * Creado   14/10/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Vector;

import com.princetonsa.dao.RecargoTarifasDao;
import com.princetonsa.dao.sqlbase.SqlBaseRecargoTarifasDao;

/**
 * 
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class OracleRecargoTarifasDao implements RecargoTarifasDao {//SIN PROBAR FUNC. SECUENCIA

	/**
	 * Insertar un recargo
	 */
	public static String insertarRecargoStr="INSERT INTO recargos_tarifas " +
																			"(codigo, " +
																			"porcentaje, " +
																			"valor, " +
																			"via_ingreso, " +
																			"tipo_paciente, " +
																			"especialidad, " +
																			"contrato, " +
																			"servicio, " +
																			"tipo_recargo) " +
																			"VALUES(seq_recargos_tarifas.nextval,?,?,?,?,?,?,?,?)";
	
    
    /**
	 * Inserta un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @return int, 0 no efectivo, >0 efectivo.
	 * @see com.princetonsa.dao.RecargoTarifasDao#insertar(java.sql.Connection, double, double, int, int, int, int, int)
	 */
	public int insertarRecargoTarifa (Connection con, double porcentaje,
															 double valor,
															 int codigoViaIngreso,
															 String tipoPaciente,
															 int codigoEspecialidad,
															 int codigoContrato,
															 int codigoServicio,
															 int codigoTipoRecargo )
	{
	    return SqlBaseRecargoTarifasDao.insertarRecargoTarifa(con,
	            												porcentaje,
	            												valor,
	            												codigoViaIngreso,
	            												tipoPaciente,
	            												codigoEspecialidad,
	            												codigoContrato,
	            												codigoServicio,
	            												codigoTipoRecargo,
	            												insertarRecargoStr);
	}
	
	
	/**
	 * Metodo que realiza la consulta de un registro de tarifa especifico.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo del recargo de tarifa.
	 * @return ResultSet.
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultaRecargoTarifa(java.sql.Connection,int)
	 */
	public ResultSetDecorator consultaRecargoTarifa (Connection con, int codigo)
	{
	    return SqlBaseRecargoTarifasDao.consultaRecargoTarifa(con,codigo);
	}
	
	/**
	 * Carga el siguiente codigo recargo   (table= recargo_tarifas))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 * @see com.princetonsa.dao.RecargoTarifasDao#cargarUltimoCodigoSequence(java.sql.Connection)
	 */
	public int cargarUltimoCodigoSequence(Connection con)
	{
	    return SqlBaseRecargoTarifasDao.cargarUltimoCodigoSequence(con);
	}
	
	/**
	 * Metodo para realizar la consulta avanzada filtrando por los parametros elegidos.
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @param nombreEspecialidad. String, nombre de la especialidad asociado esta tarifa
	 * @param nombreServicio. String, nombre del servicio asociado esta tarifa
	 * @return ResultSet, lista con los datos obtnidos de la consulta.
	 * @throws SQLException
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultaAvanzada(java.sql.Connection, double, double, int, int, int, int, int, String, String)
	 */
	public ResultSetDecorator consultaAvanzada(Connection con,
	        
															        double porcentaje,
																	double valor,
																	int codigoViaIngreso,
																	String tipoPaciente,
																	int codigoEspecialidad,
																	int codigoContrato,
																	int codigoServicio,
																	int codigoTipoRecargo,
																	String nombreEspecialidad,
																	String nombreServicio) throws SQLException 
	{
	    return SqlBaseRecargoTarifasDao.consultaAvanzada(con,
													            porcentaje,
													            valor,
													            codigoViaIngreso,
													            tipoPaciente,
													            codigoEspecialidad,
													            codigoContrato,
													            codigoServicio,
													            codigoTipoRecargo,
													            nombreEspecialidad,
													            nombreServicio);
	}
	
	/**
	 * Metodo para consultar todos los registros existentes de recargos.
	 * @param con, Connection con la fuente de datos.
	 * @return ResultSet, con todos los registros de recargos.
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultaTodos(java.sql.Connection)
	 */
	public  ResultSetDecorator consultaTodos (Connection con)
	{
	    return SqlBaseRecargoTarifasDao.consultaTodos(con);	    
	}

	/**
	 * Método que elimina un recargo de tarifa dado su código  
	 * @param con, Connection con la fuente de datos.
	 * @param codigoRecargo, Codigo del recargo de tarifa por el cual se elimina.
	 * @return true si es efectivo de lo contrario false.
	 * @see com.princetonsa.dao.RecargoTarifasDao#eliminar(java.sql.Connection, int)
	 */
	public boolean eliminar(Connection con, int codigoRecargo)
	{
	    return SqlBaseRecargoTarifasDao.eliminar(con, codigoRecargo);
	}
	
	/**
	 * Modifica un Recargo dado su código con los paramétros que fueron modificados.
	 * @param con Connection con la fuente de datos.
	 * @param codigoRecargo, codigo del Recargo
	 * @param porcentaje Porcentaje de recargo.
	 * @param valor Valor de recargo.
	 * @param codigoViaIngreso Codigo de la via de ingreso.
	 * @param codigoEspecialidad Codigo de la especialidad
	 * @param codigoServicio Codigo del servicio
	 * @param codigoTipoRecargo Codigo del tipo de recargo
	 * @return true modificó de lo contrario false.
	 * @see com.princetonsa.dao.RecargoTarifasDao#modificar(java.sql.Connection, int, double, double, int, int ,int, int)
	 */
	public boolean modificar (Connection con,
	        										 int codigoRecargo,
	        										 double porcentaje,
	        										 double valor,
	        										 int codigoViaIngreso,
	        										 String tipoPaciente,
	        										 int codigoEspecialidad,
	        										 int codigoServicio,
	        										 int codigoTipoRecargo)
	{
	    return SqlBaseRecargoTarifasDao.modificar(con,
											            codigoRecargo,
											            porcentaje,
											            valor,
											            codigoViaIngreso,
											            tipoPaciente,
											            codigoEspecialidad,
											            codigoServicio,
											            codigoTipoRecargo);
	}
	

	/**
	 * Método que  carga  el resumen de la modificación de N recargos 
	 * en una BD PostgresSQL.
	 * @param con Connection con la fuente de datos.
	 * @param codigosModificados Codigos de los recargos que fueron modificados.
	 * @return ResultSetDecorator con lo registros.
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultarRegistrosModificados(java.sql.Connection, int)
	 */
	public ResultSetDecorator consultarRegistrosModificados (Connection con, Vector codigosModificados)
	{
	    return SqlBaseRecargoTarifasDao.consultarRegistrosModificados(con, codigosModificados);
	}
	
}
