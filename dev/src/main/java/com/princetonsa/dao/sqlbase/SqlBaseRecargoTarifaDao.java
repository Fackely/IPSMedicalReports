/*
 * @(#)SqlBaseRecargoTarifaDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.ConstantesBD;


/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para un recargo de tarifa 
 
 * 
 * @version 1.0, 03-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class SqlBaseRecargoTarifaDao
{
	/**
	 * Buscar un conjunto de recargos
	 */
	public static String buscarRecargosStr="SELECT rt.codigo AS codigo,"+
																		"rt.porcentaje AS porcentaje,"+
																		"rt.valor AS valor,"+
																		"rt.via_ingreso AS viaIngreso,"+
																		"vi.nombre AS nombreViaIngreso,"+
																		"rs.descripcion AS descripcion,"+
																		"rt.especialidad AS codigoEspecialidad,"+
																		"rt.contrato AS codigoContrato,"+
																		"c.numero_contrato AS numeroContrato,"+
																		"c.convenio AS codigoConvenio,"+
																		"co.nombre AS nombreConvenio,"+
																		"rt.servicio AS codigoServicio,"+
																		"rt.tipo_recargo AS codigoTipoRecargo, "+
																		"tr.nombre AS nombreTipoRecargo "+																		
																		"FROM convenios co,tipos_recargo tr,recargos_tarifas rt "+
																		" LEFT OUTER JOIN vias_ingreso vi  ON (vi.codigo=via_ingreso ) "+
																		"LEFT OUTER JOIN especialidades e ON (rt.especialidad = e.codigo) " +
																		"LEFT OUTER JOIN referencias_servicio rs ON (rt.servicio = rs.servicio AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups +" ) " +
																		"LEFT OUTER JOIN contratos c ON (rt.contrato = c.codigo " ;
	/**
	 * consultar un recargo
	 */
	public static String  consultarRecargoStr="SELECT rt.codigo AS codigo,"+
	"rt.porcentaje AS porcentaje,"+
	" rt.valor AS valor,"+
	"rt.via_ingreso AS viaIngreso,"+
	"vi.nombre AS nombreViaIngreso,"+
	"rs.descripcion AS descripcion,"+
	"rt.especialidad AS codigoEspecialidad,"+
	"e.nombre AS nombreEspecialidad,"+
	"rt.contrato AS codigoContrato,"+
	"c.numero_contrato AS numeroContrato,"+
	"c.convenio AS codigoConvenio,"+
	"co.nombre AS nombreConvenio,"+
	"rt.servicio AS codigoServicio,"+
	"rt.tipo_recargo AS codigoTipoRecargo, "+
	"tr.nombre AS nombreTipoRecargo "+																		
	"FROM convenios co,tipos_recargo tr,recargos_tarifas rt "+
	" LEFT OUTER JOIN vias_ingreso vi  ON (vi.codigo=via_ingreso ) "+
	"LEFT OUTER JOIN especialidades e ON (rt.especialidad = e.codigo) " +
	"LEFT OUTER JOIN referencias_servicio rs ON (rt.servicio = rs.servicio AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups +" ) " +
	"LEFT OUTER JOIN contratos c ON (rt.contrato = c.codigo) " +
	
	"WHERE rt.codigo=?  AND co.codigo=c.convenio" ;
	
	public static String  consultarRecargoSinCodigoStr="SELECT rt.codigo AS codigo,"+
		"rt.porcentaje AS porcentaje,"+
		" rt.valor AS valor,"+
		"rt.via_ingreso AS viaIngreso,"+
		"vi.nombre AS nombreViaIngreso,"+
		"rs.descripcion AS descripcion,"+
		"rt.especialidad AS codigoEspecialidad,"+
		"e.nombre AS nombreEspecialidad,"+
		"rt.contrato AS codigoContrato,"+
		"c.numero_contrato AS numeroContrato,"+
		"c.convenio AS codigoConvenio,"+
		"co.nombre AS nombreConvenio,"+
		"rt.servicio AS codigoServicio,"+
		"rt.tipo_recargo AS codigoTipoRecargo, "+
		"tr.nombre AS nombreTipoRecargo "+																		
		"FROM convenios co,tipos_recargo tr,recargos_tarifas rt "+
		" LEFT OUTER JOIN vias_ingreso vi  ON (vi.codigo=via_ingreso ) "+
		"LEFT OUTER JOIN especialidades e ON (rt.especialidad = e.codigo) " +
		"LEFT OUTER JOIN referencias_servicio rs ON (rt.servicio = rs.servicio AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups +" ) " +
		"LEFT OUTER JOIN contratos c ON (rt.contrato = c.codigo) " +
	
		"WHERE rt.via_ingreso=?  AND rt. especialidad=? AND rt.servicio=? AND rt.contrato=? AND co.codigo=c.convenio" ;
	
	
	/*"SELECT rt.codigo AS codigo,"+
																		"porcentaje AS porcentaje,"+
																		" valor AS valor,"+
																		"via_ingreso AS viaIngreso,"+
																		"vi.nombre AS nombreViaIngreso,"+
																		"rs.descripcion AS descripcion,"+
																		"especialidad AS codigoEspecialidad,"+
																		"contrato AS codigoContrato,"+
																		"rt.servicio AS codigoServicio,"+
																		"tipo_recargo AS codigoTipoRecargo, "+
																		"tr.nombre AS nombreTipoRecargo "+
																		"FROM recargos_tarifas rt, vias_ingreso vi, tipos_recargo tr,referencias_servicio rs "+
																		"WHERE rt.codigo=? AND rt.via_ingreso=vi.codigo AND tr.codigo=rt.tipo_recargo AND rt.servicio=rs.servicio AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups ;*/
	/**
	 * eliminar un recargo
	 */
	public static String eliminarRecargoStr="DELETE FROM recargos_tarifas " +
																		"WHERE codigo=?";
	/**
	 * modificar un recargo
	 */
	public static String modificarRecargoStr="UPDATE recargos_tarifas " +
																		"SET ";
	/**
	 * Almacena la consulta de Recargo de tarifas
	 */
	public static String consultarRecargoTarifaStr="select " +
																"rt.codigo as codigo, " +
																"rt.porcentaje as porcentaje, " +
																"rt.valor as valor, " +
																"rt.via_ingreso as via_ingreso, " +
																"rt.especialidad as especialidad, " +
																"rt.contrato as contrato, " +
																"rt.servicio as servicio, " +
																"rt.tipo_recargo as tipo_recargo, " +
																"ct.numero_contrato as numero_contrato, " +
																"co.nombre as nombre_convenio, " +
																"CASE WHEN rt.via_ingreso IS NULL THEN 'TODAS' ELSE getNombreViaIngreso(rt.via_ingreso) END as nombre_via, " +
																"getNombreTipoRecargo(rt.tipo_recargo) as nombre_recargo, " +
																"CASE WHEN rt.especialidad IS NULL THEN 'TODAS' ELSE getNombreEspecialidad(rt.especialidad) END as nombre_especialidad, " +
																"CASE WHEN rt.servicio IS NULL THEN 'TODAS' ELSE getNombreServicio(rt.servicio,"+ConstantesBD.codigoTarifarioCups+") END as nombre_servicio " +
																"from recargos_tarifas rt " +
																"inner join contratos ct on (ct.codigo = rt.contrato) " +
																"inner join convenios co on (co.codigo = ct.convenio) " +
																"where rt.codigo = ? ";
																		
	/**
	 * Obtiene el último cod de la sequence para Pedido 
	 */
	private final static String ultimaSequenciaStr= "SELECT MAX(codigo) AS seq_recargos_tarifas FROM recargos_tarifas ";
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRecargoTarifaDao.class);

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
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#insertar(java.sql.Connection, double, double, int, int, int, int, int)
	 */
	public static ResultadoBoolean insertar(	Connection con,
																	double porcentaje,
																	double valor,
																	int codigoTipoRecargo,
																	int codigoServicio,
																	int codigoEspecialidad,
																	int codigoViaIngreso,
																	int codigoContrato,
																	String insertarRecargoStr) throws SQLException
	{
		PreparedStatementDecorator insertarRecargoStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		insertarRecargoStatement.setDouble(1,porcentaje);
		insertarRecargoStatement.setDouble(2,valor);
		
		if(codigoViaIngreso!=0){
			insertarRecargoStatement.setInt(3,codigoViaIngreso);
		}else{
			insertarRecargoStatement.setNull(3,Types.INTEGER);
		}	
		if(codigoEspecialidad!=0){
			insertarRecargoStatement.setInt(4,codigoEspecialidad);
		}else{
			insertarRecargoStatement.setNull(4,Types.INTEGER);
		}
		insertarRecargoStatement.setInt(5,codigoContrato);
		if(codigoServicio!=0){
			
			insertarRecargoStatement.setInt(6,codigoServicio);
		}else{
			insertarRecargoStatement.setNull(6,Types.INTEGER);
		}
		insertarRecargoStatement.setInt(7,codigoTipoRecargo);
		
		if(insertarRecargoStatement.executeUpdate()<1){
			return new ResultadoBoolean(false);
		}else{
			return new ResultadoBoolean(true);
		}
		
		
	}

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
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#insertarTransaccional(java.sql.Connection, double, double, int, int, int, int, int, java.lang.String)
	 */
	public static ResultadoBoolean insertarTransaccional(	Connection con,
																							double porcentaje,
																							double valor,
																							int codigoTipoRecargo,
																							int codigoServicio,
																							int codigoEspecialidad,
																							int codigoViaIngreso,
																							int codigoContrato,
																							String estado,
																							String insertarRecargoStr) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		/* El estado de la transacción no puede ser nulo */
		if(estado == null)
		{
			myFactory.abortTransaction(con);

			if(logger.isDebugEnabled() )
				logger.debug(
					"Estado de transaccion inválida al insertar recargo de tarifa transaccional"
				);

			throw new SQLException(
				"El estado de la transacción (insertar Recargo SqlBase Transaccional) " +
				"no esta especificado"
			);
		}
		/* Se desea iniciar una transaccion */
		if(estado.equals(ConstantesBD.inicioTransaccion) )
		{
			/* Iniciar la transacción */
			if (!myFactory.beginTransaction(con))
			{
				myFactory.abortTransaction(con);
				throw new SQLException(
					"No se pudo inicializar bien la transacción en (Insertar Recargo SqlBase Transaccional)");
			}
		}
		ResultadoBoolean resp=insertar(con, porcentaje, valor, codigoTipoRecargo, codigoServicio, codigoEspecialidad, codigoViaIngreso, codigoContrato, insertarRecargoStr);
		if (!resp.isTrue())
		{
			myFactory.abortTransaction(con);
			throw new SQLException(
				"No se pudo ejecutar inserción en (Insertar Recargo SqlBase Transaccional)");
		}
				
		if(estado.equals(ConstantesBD.finTransaccion) )
		{
			/* Finalizar la transacción */
			myFactory.endTransaction(con);
		}

		return resp;
	}

	/**
	 * Modifica una recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo, int, código del recargo a modificar
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @return ResultadoBoolean, true si la modificacion fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#modificar(java.sql.Connection, int, double, double, int, int, int, int, int)
	 */
	public static ResultadoBoolean modificar(	Connection con,
																		int codigo,
																		double porcentaje,
																		double valor,
																		int codigoTipoRecargo,
																		int codigoServicio,
																		int codigoEspecialidad,
																		int codigoViaIngreso,
																		int codigoContrato) throws SQLException
	{
		
		String condiciones="";
		String consulta="";
		boolean primeraCondicion=true;
		if(porcentaje>=0){
			if(primeraCondicion){
				condiciones+=" porcentaje="+porcentaje;
				primeraCondicion=false;
			}else{
				condiciones+=", porcentaje="+porcentaje;
			}
		}
		if(valor>=0){
			if(primeraCondicion){
				condiciones+=" valor="+valor;
				primeraCondicion=false;
			}else{
				condiciones+=",valor="+valor;
			}
		}
		if(codigoTipoRecargo!=-1){
			if(primeraCondicion){
				condiciones+=" tipo_recargo="+codigoTipoRecargo;
				primeraCondicion=false;
			}else{
				condiciones+=", tipo_recargo="+codigoTipoRecargo;
			}
		}
		if(codigoServicio>0)
		{
			if(primeraCondicion){
				condiciones+=" servicio="+codigoServicio;
				primeraCondicion=false;
			}else{
				condiciones+=", servicio="+codigoServicio;
			}
		}
		else if(codigoServicio==0)
		{
			if(primeraCondicion){
				condiciones+=" servicio= null ";
				primeraCondicion=false;
			}else{
				condiciones+=", servicio= null ";
			}
		}	
		if(codigoEspecialidad>0){
			if(primeraCondicion){
				condiciones+=" especialidad="+codigoEspecialidad;
				primeraCondicion=false;
			}else{
				condiciones+=", especialidad="+codigoEspecialidad;
			}
		}
		else 	if(codigoEspecialidad==0){
			if(primeraCondicion){
				condiciones+=" especialidad=null ";
				primeraCondicion=false;
			}else{
				condiciones+=", especialidad=null ";
			}
		}
		if(codigoViaIngreso>0){
			if(primeraCondicion){
				condiciones+=" via_ingreso="+codigoViaIngreso;
				primeraCondicion=false;	
			}else{
				condiciones+=",via_ingreso="+codigoViaIngreso;
			}
		}
		else if(codigoViaIngreso==0){
			if(primeraCondicion){
				condiciones+=" via_ingreso=null ";
				primeraCondicion=false;	
			}else{
				condiciones+=",via_ingreso=null ";
			}
		}
		if(codigoContrato!=-1){
			if(primeraCondicion){
				condiciones+=" contrato="+codigoContrato;
				primeraCondicion=false;
			}else{
				condiciones+=", contrato="+codigoContrato;
			}
			
		}
		if(!primeraCondicion){
			condiciones+=" ";
			
			consulta=modificarRecargoStr+condiciones+"WHERE codigo="+codigo;
			
			
			PreparedStatementDecorator modificarRecargoStatement =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//modificarRecargoStatement.setInt(1,codigo);
	
			if(modificarRecargoStatement.executeUpdate()<1){
				return new ResultadoBoolean(false);
			}else{
				return new ResultadoBoolean(true);
			}
		}else{
			return new ResultadoBoolean(true);
		}
				
	}

	/**
	 * Modifica una recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo, int, código del recargo a modificar
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#modificarTransaccional(java.sql.Connection, int, double, double, int, int, int, int, int, java.lang.String)
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con,
																								int codigo,
																								double porcentaje,
																								double valor,
																								int codigoTipoRecargo,
																								int codigoServicio,
																								int codigoEspecialidad,
																								int codigoViaIngreso,
																								int codigoContrato,
																								String estado) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		/* El estado de la transacción no puede ser nulo */
		if(estado == null)
		{
			myFactory.abortTransaction(con);

			if(logger.isDebugEnabled() )
				logger.debug(
					"Estado de transaccion inválida al modificar recargo de tarifa transaccional"
				);

			throw new SQLException(
				"El estado de la transacción (modificar Recargo SqlBase Transaccional) " +
				"no esta especificado"
			);
		}
		/* Se desea iniciar una transaccion */
		if(estado.equals(ConstantesBD.inicioTransaccion) )
		{
			/* Iniciar la transacción */
			if (!myFactory.beginTransaction(con))
			{
				myFactory.abortTransaction(con);
				throw new SQLException(
					"No se pudo inicializar bien la transacción en (modificar Recargo SqlBase Transaccional)");
			}
		}
		ResultadoBoolean resp=modificar(con, codigo, porcentaje, valor, codigoTipoRecargo, codigoServicio, codigoEspecialidad, codigoViaIngreso, codigoContrato);
		
		if (!resp.isTrue())
		{
			myFactory.abortTransaction(con);
			throw new SQLException(
				"No se pudo ejecutar inserción en (modificar Recargo SqlBase Transaccional)");
		}
				
		if(estado.equals(ConstantesBD.finTransaccion) )
		{
			/* Finalizar la transacción */
			myFactory.endTransaction(con);
		}

		return resp;
	}

	/**
	 * Elimina un recargo dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo. int, código del recargo a eliminar
	 * @return ResultadoBoolean, true si la eliminación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#eliminar(java.sql.Connection, int)
	 */
	public static ResultadoBoolean eliminar(Connection con, int codigo) throws SQLException
	{
		PreparedStatementDecorator eliminarRecargoStatement =  new PreparedStatementDecorator(con.prepareStatement(eliminarRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		eliminarRecargoStatement.setInt(1,codigo);
		if(eliminarRecargoStatement.executeUpdate()<1){
			return new ResultadoBoolean(false);
		}else{
			return new ResultadoBoolean(true);
		}

	}

	/**
	 * Consulta un recargo dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo. int, código del recargo a consultar
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#consultar(java.sql.Connection, int)
	 */
	public static ResultadoCollectionDB consultar(Connection con, int codigo, boolean conCodigo,int codigoViaIngreso,int codigoEspecialidad,int codigoServicio, int codigoContrato) throws SQLException
	{
		if(conCodigo){
		
			PreparedStatementDecorator consultarRecargoStatement =  new PreparedStatementDecorator(con.prepareStatement(consultarRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarRecargoStatement.setInt(1,codigo);
			ResultSetDecorator rs=new ResultSetDecorator(consultarRecargoStatement.executeQuery());
			
			Collection listadoCollection = UtilidadBD.resultSet2Collection(rs);
			return new ResultadoCollectionDB(true, "", listadoCollection);	
		}else{
			PreparedStatementDecorator consultarRecargoStatement =  new PreparedStatementDecorator(con.prepareStatement(consultarRecargoSinCodigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarRecargoStatement.setInt(1,codigoViaIngreso);
			consultarRecargoStatement.setInt(2,codigoEspecialidad);
			consultarRecargoStatement.setInt(3,codigoServicio);
			consultarRecargoStatement.setInt(4,codigoContrato);
			
			ResultSetDecorator rs=new ResultSetDecorator(consultarRecargoStatement.executeQuery());
	
			Collection listadoCollection = UtilidadBD.resultSet2Collection(rs);
			return new ResultadoCollectionDB(true, "", listadoCollection);	
		}
	}
	
	
	
	
	
	
	public static ResultadoCollectionDB consultar(Connection con,
																	int codigo,
																	boolean buscarPorCodigo,
																	double porcentaje,
																	boolean buscarPorPorcentaje,
																	double valor,
																	boolean buscarPorValor,
																	int codigoTipoRecargo,
																	boolean buscarPorCodigoTipoRecargo,
																	int codigoServicio,
																	boolean buscarPorCodigoServicio,
																	int codigoEspecialidad,
																	boolean buscarPorCodigoEspecialidad,
																	int codigoViaIngreso,
																	boolean buscarPorCodigoViaIngreso,
																	int codigoContrato,
																	boolean buscarPorCodigoContrato,
																	int codigoConvenio) throws SQLException
	{
		boolean existeAsignacion=false;
		String consulta=new String(buscarRecargosStr);
		
		consulta+= " AND c.convenio = "+codigoConvenio +" ) WHERE "; 
		String condiciones="";


		if(buscarPorCodigo){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
			}
			condiciones=condiciones.concat("rt.codigo="+codigo+" ");
			existeAsignacion=true;
		}
		if(buscarPorPorcentaje){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
			}
			condiciones=condiciones.concat("rt.porcentaje="+porcentaje+" ");
			existeAsignacion=true;
		}
		if(buscarPorValor){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
			}
			condiciones=condiciones.concat("rt.valor="+valor+" ");
			existeAsignacion=true;
		}
		if(buscarPorCodigoTipoRecargo){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
			}
			condiciones=condiciones.concat("rt.tipo_recargo="+codigoTipoRecargo+" ");
			existeAsignacion=true;
		}
		if(buscarPorCodigoServicio){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
			}
			condiciones=condiciones.concat("rt.servicio="+codigoServicio+" ");
			existeAsignacion=true;
		}
		if(buscarPorCodigoEspecialidad){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
			}
			condiciones=condiciones.concat("rt.especialidad="+codigoEspecialidad+" ");
			existeAsignacion=true;
		}
		if(buscarPorCodigoViaIngreso){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
			}
			condiciones=condiciones.concat("rt.via_ingreso="+codigoViaIngreso+" ");
			existeAsignacion=true;
		}
		if(buscarPorCodigoContrato){
			if(existeAsignacion){
				condiciones=condiciones.concat(" AND ");
				
			}
			condiciones=condiciones.concat("rt.contrato="+codigoContrato+" ");
			existeAsignacion=true;
		}
		//aqui poner condiciones adicionales
		
		if(existeAsignacion){
			condiciones=condiciones.concat(" AND ");
		}
		condiciones=condiciones.concat("  tr.codigo=tipo_recargo AND co.codigo=c.convenio ORDER BY nombreViaIngreso,rt.servicio ASC");
		consulta=consulta+condiciones;
		
		PreparedStatementDecorator buscarRecargosStmnt =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		Collection listadoCollection = UtilidadBD.resultSet2Collection(new ResultSetDecorator(buscarRecargosStmnt.executeQuery()));
		
		return new ResultadoCollectionDB(true, "", listadoCollection);	
	
	}
	
	public static boolean existeRecargo(Connection con,
																int codigoTipoRecargo,
																int codigoServicio,
																int codigoEspecialidad,
																int codigoViaIngreso,
																int codigoContrato) throws SQLException{
		
		String existeRecargoStr=	"SELECT COUNT(*) "+
												"FROM recargos_tarifas rt "+
												"WHERE ";
		
		if(codigoViaIngreso==0)
			existeRecargoStr+= " via_ingreso is null "; 
		else
			existeRecargoStr+= " via_ingreso = "+codigoViaIngreso;
		
		existeRecargoStr+=" AND contrato = "+codigoContrato;
		
		if(codigoEspecialidad==0)
			existeRecargoStr+=" AND especialidad is null";
		else
			existeRecargoStr+=" AND especialidad = "+codigoEspecialidad;
		
		if(codigoServicio==0)
			existeRecargoStr+=" AND servicio is null ";
		else
			existeRecargoStr+=" AND servicio = "+codigoServicio;
		
		existeRecargoStr+=" AND tipo_recargo = "+codigoTipoRecargo;
		
		
		PreparedStatementDecorator existeRecargosStmnt =  new PreparedStatementDecorator(con.prepareStatement(existeRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		ResultSetDecorator rs=new ResultSetDecorator(existeRecargosStmnt.executeQuery());
		rs.next();
		if(rs.getInt("count")>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Metodo que realiza la consulta de un registro de tarifa especifico.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo del recargo de tarifa.
	 * @return ResultSet.
	 */
	public static ResultSetDecorator consultaRecargoTarifa (Connection con, int codigo)
	{
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarRecargoTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigo);
	        return new ResultSetDecorator(ps.executeQuery());
	        
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta de recargo tarifa, con el codigo->"+codigo+" "+": SqlBaseRecargoTarifaDao "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * Carga el siguiente codigo recargo   (table= recargo_tarifas))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public static int cargarUltimoCodigoSequence(Connection con)
	{
		int ultimoCodigoSequence=0;
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(ultimaSequenciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(cargarUltimoStatement.executeQuery());
			if(rs.next())
			{
				ultimoCodigoSequence=rs.getInt(1);
				return ultimoCodigoSequence;
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último codigo recargo: SqlBaseRecargoTarifaDao "+e.toString());
			return 0;
		}
	}

}
