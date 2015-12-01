/*
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * @author Juan David Ramírez
 *
 */
public class SqlBasePagosPacienteDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBasePagosPacienteDao.class);
	
	/**
	 * statement para insertar pago
	 */
	private static final String insertarPagoStr="INSERT INTO pagos_paciente" +
												" (" +
													" codigo," +
													" entidad," +
													" tipo_monto," +
													" documento," +
													" fecha," +
													" diagnostico," +
													" tipo_cie," +
													" descripcion," +
													" valor_pago," +
													" origen_pago," +
													" fecha_grabacion," +
													" hora_grabacion," +
													" usuario," +
													" institucion," +
													" paciente," +
													" tipo_regimen)" +
												" VALUES(?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?)";

	/**
	 * Stetement para la busqueda avanzada de los pagos
	 */
	private static String busqueda="SELECT pp.codigo AS codigo," +
										" pp.entidad AS entidad," +
										" tm.nombre AS tipoMonto," +
										" pp.documento AS documento," +
										" pp.fecha AS fecha," +
										" CASE" +
											" WHEN" +
												" pp.diagnostico IS NULL" +
											" THEN ''"+
											" ELSE" +
												" pp.diagnostico" +
											" END AS acronimo, " +
										" CASE" +
											" WHEN" +
												" dia.nombre IS NULL" +
											" THEN ''"+
											" ELSE" +
												" dia.nombre" +
											" END AS diagnostico," +
										" tc.codigo_real AS tipoCie," +
										" pp.descripcion AS descripcion," +
										" CASE WHEN pp.anulado IS NULL AND pp.fecha_anulacion IS NULL AND pp.usuario_anula IS NULL" +
											" THEN " +
												" pp.valor_pago " +
											" ELSE" +
												" 0 " +
										    "END AS valor," +
										" op.nombre AS origen," +
										" pp.usuario AS usuario," +
										" i.razon_social AS institucion," +
										" tr.nombre AS tipoRegimen," +
										" CASE WHEN pp.anulado IS NULL THEN '' ELSE pp.anulado END AS anulado," +
										" CASE WHEN pp.fecha_anulacion IS NULL THEN '' ELSE to_char(pp.fecha_anulacion,'yyyy-mm-dd') END AS fecha_anulacion," +
										" CASE WHEN pp.usuario_anula IS NULL THEN '' ELSE pp.usuario_anula END AS usuario_anula" +
											" from pagos_paciente pp" +
											" INNER JOIN tipos_monto tm" +
												" ON(tm.codigo=pp.tipo_monto)" +
											" LEFT OUTER JOIN diagnosticos dia" +
												" ON(pp.diagnostico=dia.acronimo)" +
											" INNER JOIN tipos_cie tc" +
												" ON(tc.codigo=pp.tipo_cie)" +
											" INNER JOIN origenes_pago op" +
												" ON(op.codigo=pp.origen_pago)" +
											" INNER JOIN instituciones i" +
												" ON(pp.institucion=i.codigo)" +
											" INNER JOIN usuarios u" +
												" ON(pp.usuario=u.login)" +
											" INNER JOIN tipos_regimen tr" +
												" ON(pp.tipo_regimen=tr.acronimo)" +
											" where pp.paciente=?"; 
	
	/**
	 * Cadena para consultar un pago especifico
	 */
	private static final String consultaPagoStr="SELECT entidad AS entidad," +
													" tipo_monto AS tipomonto," +
													" documento AS documento," +
													" fecha AS fecha," +
													" CASE" +
														" WHEN" +
															" diagnostico IS NULL" +
														" THEN ''" +
														" ELSE " +
															" diagnostico" +
														" END AS diagnostico," +
													" tipo_cie AS tipocie," +
													" descripcion AS descripcion," +
													" valor_pago AS valor," +
													" origen_pago AS origen," +
													" usuario AS usuario," +
													" institucion AS institucion," +
													" tipo_regimen AS tipoRegimen" +
														" from pagos_paciente" +
														" WHERE codigo = ?";
	
	/**
	 * Cadena para modificar un pago
	 */
	private static String modificarPagoStr="UPDATE pagos_paciente" +
											" SET" +
												" entidad=?," +
												" tipo_monto=?," +
												" documento=?," +
												" fecha=?," +
												" diagnostico=?," +
												" tipo_cie=?," +
												" descripcion=?," +
												" valor_pago=?," +
												" institucion=?," +
												" tipo_regimen=?" +
											" where" +
												" codigo=?";
	
	/**
	 * Cadena para eliminar un pago
	 */
	private static String borrarPagoStr="DELETE" +
										" FROM" +
											" pagos_paciente" +
										" WHERE" +
											" codigo = ?";
	
	/**
	 * Cadena para la selección de la vista que contiene los pagos acumulados
	 */
	private static String consultaPagosStr="SELECT" +
											" vp.tipomonto AS tipomonto," +
											" vp.codigotipomonto AS codigotipomonto," +
											" vp.anio AS anio," +
											" vp.codigodiagnostico || ' ' || vp.diagnostico AS diagnostico," +
											" vp.tipoCie," +
											" vp.acumulado AS acumulado," +
											" tr.acronimo AS codigoTipoRegimen," +
											" tr.nombre AS tipoRegimen" +
										" FROM" +
											" vista_pagos_paciente vp" +
										" INNER JOIN" +
											" tipos_regimen tr" +
											" ON(tr.acronimo=vp.tipoRegimen)" +
										" WHERE vp.anulado="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND vp.paciente=?";

	
	
	private static String insertarPagosTemporalesStr="INSERT INTO pagos_paciente_tempo" +
													" (" +
														" codigo," +
														" tipo_monto," +
														" fecha," +
														" diagnostico," +
														" tipo_cie," +
														" valor_pago," +
														" institucion," +
														" paciente," +
														"tipo_regimen, " +
														"numero_transaccion)" +
													" VALUES(?,?,CURRENT_DATE,?,?,?,?,?,?,?)";

	/**
	 * Borrar pagos del paciente temporales ingresados en al facturación
	 */
	private static String borrarPagosPacienteTempoStr="DELETE " +
														"FROM " +
															"pagos_paciente_tempo " +
														"WHERE " +
															"numero_transaccion=?";
	
	/**
	 * Consulta para obtener es sugiente numero de Transacción
	 */
	private static final String consultarSiguienteNumeroTransaccionStr="SELECT CASE WHEN max(numero_transaccion) IS NULL THEN 1 ELSE max(numero_transaccion)+1 END AS numeroTransaccion FROM pagos_paciente_tempo";
	
	/**
	 * Cadena para actualizar la info de anulación de pagos de paciente
	 */
	private static final String  actuallizarAnulacionPagosPacienteDadoFacturaStr="UPDATE pagos_paciente SET anulado=?, fecha_anulacion= CURRENT_DATE, usuario_anula=? WHERE codigo IN (SELECT codigo FROM pagos_paciente WHERE institucion=? AND origen_pago=? AND documento =?)";
	
	/**
	 * Cadena para actualizar el diagnóstico de pagos de paciente, cuando la cuenta del paciente está facturada 
	 */
	private static final String actualizarDiagnosticoCuentaFacturadaStr = "UPDATE pagos_paciente SET diagnostico= ? , tipo_cie=? " +
																																			"WHERE codigo=" +
																																				"(SELECT pp.codigo AS codigo FROM pagos_paciente pp " +
																																					"INNER JOIN facturas f ON (f.consecutivo_factura=pp.documento) " +
																																					"INNER JOIN cuentas cue ON (f.cuenta=cue.id) " +
																																					"INNER JOIN solicitudes sol ON (sol.cuenta=cue.id) " +
																																						"WHERE sol.numero_solicitud=? AND pp. origen_pago="+ConstantesBD.codigoOrigenPagoInterno+" AND f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+")";
	
	
	/**
	 * Método para insertar un pago de un paciente
	 * @param con
	 * @param entidad
	 * @param tipoMonto
	 * @param documento
	 * @param fecha
	 * @param diagnostico
	 * @param tipoCie
	 * @param descripcion
	 * @param valorPago
	 * @param origenPago
	 * @param usuario
	 * @param institucion
	 * @param secuencia
	 * @param codigoPaciente
	 * @param tipoRegimen
	 * @return Código del pago insertado
	 */
	public static int insertar(Connection con, String entidad, int tipoMonto, String documento, String fecha, String diagnostico, int tipoCie, String descripcion, double valorPago, int origenPago, String usuario, int institucion, String secuencia, int codigoPaciente, String tipoRegimen)
	{
		/*
		 * No se debe ingresar pagos con valor 0
		 */
		if(valorPago==0)
		{
			logger.warn("Trató de insertar un pago en valor 0");
			//tiene que retornar un valor >0 para que no aque error en la transaccion de la generacion de factura
			return 1;
		}
		/*
		 * Obtener el código del pago del paciente
		 */
		int codigo;
		try
		{
			PreparedStatementDecorator codigoPago= new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(codigoPago.executeQuery());
			if(resultado.next())
				codigo=resultado.getInt(1);
			else
			{
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error obteniendo el código de la secuencia "+e);
			return 0;
		}
		
		PreparedStatementDecorator insertarPago;
		try
		{
			
			logger.info("insertar pago pac-->"+valorPago);
			
			insertarPago =  new PreparedStatementDecorator(con.prepareStatement(insertarPagoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarPago.setInt(1, codigo);
			insertarPago.setString(2, entidad);
			insertarPago.setInt(3, tipoMonto);
			insertarPago.setString(4, documento);
			insertarPago.setString(5, fecha);
			insertarPago.setString(6,diagnostico);
			insertarPago.setInt(7, tipoCie);
			insertarPago.setString(8, descripcion);
			insertarPago.setDouble(9, valorPago);
			insertarPago.setInt(10, origenPago);
			insertarPago.setString(11, usuario);
			insertarPago.setInt(12, institucion);
			insertarPago.setInt(13, codigoPaciente);
			insertarPago.setString(14, tipoRegimen);
			if(!(insertarPago.executeUpdate()>0))
			{
				codigo=0;
				logger.error("Eror insertando el pago");
			}
		
			return codigo;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando el pago "+e);
			return 0;
		}
	}

	/**
	 * Método que devuelve el siguiente numero de transacción
	 * para los pagos temporales del paciente
	 * @param con
	 * @return siguiente numero de transacción
	 */
	public static int siguienteNumeroTransaccion(Connection con)
	{
		try
		{
			PreparedStatementDecorator numTransaccion= new PreparedStatementDecorator(con.prepareStatement(consultarSiguienteNumeroTransaccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(numTransaccion.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numeroTransaccion");
			}
			else
			{
				return 0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el siguiente numero de transacción "+e);
			return 0;
		}
	}

	/**
	 * Método para insertar un pago temporal de un paciente
	 * @param con
	 * @param tipoMonto
	 * @param diagnostico
	 * @param tipoCie
	 * @param valorPago
	 * @param institucion
	 * @param secuencia
	 * @param codigoPaciente
	 * @param tipoRegimen
	 * @param numeroTransaccion
	 * @return Código del pago insertado
	 */
	public static int insertarPagoTemporal(Connection con, int tipoMonto, String diagnostico, int tipoCie, double valorPago, int institucion, String secuencia, int codigoPaciente, String tipoRegimen, int numeroTransaccion)
	{
		/*
		 * No se debe ingresar pagos con valor 0
		 */
		if(valorPago==0)
		{
			logger.warn("Trató de insertar un pago en valor 0");
			return 0;
		}
		
		/*
		 * Obtener el código del pago del paciente
		 */
		int codigo;
		try
		{
			PreparedStatementDecorator codigoPago= new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(codigoPago.executeQuery());
			if(resultado.next())
				codigo=resultado.getInt(1);
			else
			{
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error obteniendo el código de la secuencia "+e);
			return 0;
		}
		
		PreparedStatementDecorator insertarPago;
		try
		{
			if(diagnostico.equals(""))
			{
				diagnostico=ConstantesBD.acronimoDiagnosticoNoSeleccionado;
			}
			insertarPago =  new PreparedStatementDecorator(con.prepareStatement(insertarPagosTemporalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarPago.setInt(1, codigo);
			insertarPago.setInt(2, tipoMonto);
			insertarPago.setString(3,diagnostico);
			insertarPago.setInt(4, tipoCie);
			insertarPago.setDouble(5, valorPago);
			insertarPago.setInt(6, institucion);
			insertarPago.setInt(7, codigoPaciente);
			insertarPago.setString(8, tipoRegimen);
			insertarPago.setInt(9, numeroTransaccion);
			if(!(insertarPago.executeUpdate()>0))
			{
				codigo=0;
				logger.error("Error insertando el pago");
			}
		
			return codigo;
		}
		catch (SQLException e)
		{
			logger.error("Eror insertando el pago "+e);
			return 0;
		}
	}

	/**
	 * Búsqueda avanzada de pagos por paciente
	 * @param con
	 * @param entidad
	 * @param tipoMonto
	 * @param documento
	 * @param fecha
	 * @param diagnostico
	 * @param tipoCie
	 * @param descripcion
	 * @param valor
	 * @param origen
	 * @param usuario
	 * @param institucion
	 * @param tipoRegimen
	 * @return Collection con todos los resultados
	 */
	public static Collection consultaModificar(Connection con, String entidad, int tipoMonto, String documento, String fecha, String diagnostico, int tipoCie, String descripcion, double valor, int origen, String usuario, int institucion, String tipoRegimen, int paciente)
	{
		String consulta=busqueda;
		String orden=" ORDER BY pp.diagnostico, pp.codigo";
		if(!entidad.equals(""))
		{
			consulta+=" AND UPPER(pp.entidad) LIKE UPPER('%"+entidad+"%')";
		}
		if(tipoMonto!=0)
		{
			consulta+=" AND pp.tipo_monto = "+tipoMonto;
		}
		if(!documento.equals(""))
		{
			consulta+=" AND UPPER(pp.documento) LIKE UPPER('%"+documento+"%')";
		}
		if(!fecha.equals(""))
		{
			consulta+=" AND pp.fecha || '' LIKE '%"+fecha+"%'";
		}
		if(!diagnostico.equals(""))
		{
			consulta+=" AND UPPER(pp.diagnostico) LIKE UPPER('%"+diagnostico+"%')";
		}
		if(tipoCie!=0)
		{
			consulta+=" AND pp.tipo_cie="+tipoCie;
		}
		if(!descripcion.equals(""))
		{
			consulta+=" AND UPPER(pp.descripcion) LIKE UPPER('%"+descripcion+"%')";
		}
		if(valor!=0)
		{
			String val=valor+"";
			int punto=val.indexOf(".");
			if(val.substring(punto+1,val.length()).equals("0"))
			{
				val=val.substring(0,punto);
			}
			consulta+=" AND pp.valor_pago LIKE '%"+val+"%'";
		}
		if(origen!=0)
		{
			consulta+=" AND pp.origen_pago="+origen;
		}
		if(!usuario.equals(""))
		{
			consulta+=" AND UPPER(u.login) LIKE UPPER('%"+usuario+"%')";
		}
		if(institucion!=0)
		{
			consulta+=" AND pp.institucion="+institucion;
		}
		if(!tipoRegimen.equals(""))
		{
			consulta+=" AND UPPER(pp.tipo_regimen) LIKE UPPER('%"+tipoRegimen+"%')";
		}
		
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta+orden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, paciente);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(statement.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error al consultar los pagos por paciente "+e);
			return null;
		}
	}
	
	/**
	 * Método el cual hace la consulta de un solo pago dado el codigo del mismo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static Collection consultarPago(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator consultaPago= new PreparedStatementDecorator(con.prepareStatement(consultaPagoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaPago.setInt(1, codigo);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultaPago.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el pago con codigo "+codigo+": "+e);
			return null;
		}
	}
	
	/**
	 * Eliminar un pago especifico dado su código
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean borrar(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator prepared= new PreparedStatementDecorator(con.prepareStatement(borrarPagoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			prepared.setInt(1, codigo);
			if(prepared.executeUpdate()>0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando el pago con código "+codigo+": "+e);
			return false;
		}
	}
	
	/**
	 * Método para modificar un pago de un paciente
	 * @param con
	 * @param entidad
	 * @param tipoMonto
	 * @param documento
	 * @param fecha
	 * @param diagnostico
	 * @param tipoCie
	 * @param descripcion
	 * @param valorPago
	 * @param institucion
	 * @param tipoRegimen
	 * @return Número de elementos modificados
	 */
	public static boolean modificar(Connection con, int codigo, String entidad, int tipoMonto, String documento, String fecha, String diagnostico, int tipoCie, String descripcion, double valorPago, int institucion, String tipoRegimen)
	{
		PreparedStatementDecorator modificarPago;
		try
		{
			modificarPago =  new PreparedStatementDecorator(con.prepareStatement(modificarPagoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificarPago.setString(1, entidad);
			modificarPago.setInt(2, tipoMonto);
			modificarPago.setString(3, documento);
			modificarPago.setString(4, fecha);
			modificarPago.setString(5,diagnostico);
			modificarPago.setInt(6, tipoCie);
			modificarPago.setString(7, descripcion);
			modificarPago.setDouble(8, valorPago);
			modificarPago.setInt(9, institucion);
			modificarPago.setString(10, tipoRegimen);
			modificarPago.setInt(11, codigo);
			if(modificarPago.executeUpdate()>0)
			{
				return true;
			}
			else
			{
				logger.error("Eror modificando el pago");
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Eror modificando el pago "+e);
			return false;
		}
	}
	
	/**
	 * Método para consultar los pagos acumulados de los pacientes 
	 * @param con
	 * @return Colección con el resultado de los pagos acumulados
	 */
	public static Collection consultarPagosAcumulados(Connection con, int codigoPaciente)
	{
		try
		{
			PreparedStatementDecorator consulta= new PreparedStatementDecorator(con.prepareStatement(consultaPagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1, codigoPaciente);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los pagos acumulados de los pacientes "+e);
			return null;
		}
	}

	/**
	 * Método para consultar los pagos acumulados de un paciente
	 * para el año actual (Comparado con la fecha del sistema) 
	 * @param con
	 * @return double con el valor de los pagos acumulados del paciente
	 */
	public static double consultarPagoAcumuladoAnioActual(Connection con, int codigoPaciente, int tipoMonto, String tipoRegimen)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consultaPagosAnioActualStr= "SELECT " +
													"SUM(valor_pago) as acumulado FROM pagos_paciente " +
												"WHERE " +
													"paciente=? " +
													"AND to_char(fecha, 'yyyy')=to_char(current_date, 'yyyy') " +
													"AND tipo_monto=? " +
													"AND tipo_regimen=? " +
													"AND fecha_anulacion is null ";
				
			pst= con.prepareStatement(consultaPagosAnioActualStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, tipoMonto);
			pst.setString(3, tipoRegimen);
			rs=pst.executeQuery();
			if(rs.next())
			{
				return rs.getDouble("acumulado");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR consultarPagoAcumuladoAnioActual",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarPagoAcumuladoAnioActual", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return 0;
	}

	/**
	 * Método para consultar los pagos acumulados de un paciente
	 * para el año actual (Comparado con la fecha del sistema) 
	 * @param con
	 * @param diagnostico
	 * @param tipoCie Tipo de cie del diagnóstico
	 * @param tipoMonto tipo de monto ligado al pago
	 * @param tipoRegimen tipo de regimen ligado al pago
	 * @return double con el valor de los pagos acumulados del paciente
	 */
	public static double consultarPagoAcumuladoXDiagnostico(Connection con, int codigoPaciente, String diagnostico, int tipoCie, int tipoMonto, String tipoRegimen)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consultaPagosXDiagnosticoStr="SELECT " +
													"SUM(valor_pago) as acumulado FROM pagos_paciente " +
												"WHERE " +
													"paciente=? " +
													"AND diagnostico=? " +
													"AND to_char(fecha, 'yyyy')=to_char(current_date, 'yyyy') " +
													"AND tipo_cie=? " +
													"AND tipo_monto=? " +
													"AND tipo_regimen=? " +
													"AND fecha_anulacion is null ";
			
			logger.info("consultarPagoAcumuladoXDiagnostico->"+consultaPagosXDiagnosticoStr+" codigoPaciente->"+codigoPaciente+" DX->"+diagnostico+" CIE->"+tipoCie+" TIPmONTO->"+tipoMonto+" REG->"+tipoRegimen);
			pst= con.prepareStatement(consultaPagosXDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
			pst.setString(2, diagnostico);
			pst.setInt(3, tipoCie);
			pst.setInt(4, tipoMonto);
			pst.setString(5, tipoRegimen);
			
			
			rs =pst.executeQuery();
			if(rs.next())
			{
				return rs.getDouble("acumulado");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR consultarPagoAcumuladoXDiagnostico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarPagoAcumuladoXDiagnostico", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return 0;
	}

	/**
	 * Ver el detalle de los pagos por paciente
	 * @param con
	 * @param tipoMonto
	 * @param anio
	 * @param diagnostico
	 * @param tipoCie
	 * @param tipoRegimen
	 * @return
	 */
	public static Collection detalle(Connection con, int tipoMonto, String anio, String diagnostico, int codigoPaciente, int tipoCie, String tipoRegimen)
	{
		String detalle=busqueda+
						" AND pp.tipo_monto=?" +
						" AND to_char(pp.fecha,'yyyy')=to_char(to_date(?,'yyyy'),'yyyy')" +
						" AND (pp.diagnostico IS NULL OR pp.diagnostico = UPPER(?))" +
						" AND pp.tipo_cie=?" +
						" AND pp.paciente=" + codigoPaciente +
						" AND pp.tipo_regimen=?" +
						" ORDER BY fecha DESC";
		
		try
		{
			PreparedStatementDecorator detalleSt= new PreparedStatementDecorator(con.prepareStatement(detalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			detalleSt.setInt(1, codigoPaciente);
			detalleSt.setInt(2,tipoMonto);
			detalleSt.setString(3, anio);
			detalleSt.setString(4, diagnostico);
			detalleSt.setInt(5, tipoCie);
			detalleSt.setString(6, tipoRegimen);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(detalleSt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el detalle de los pagos por paciente "+e);
			return null;
		}
	}
	
	/**
	 * Método para borrar los pagos de los pacientes temporales insertados
	 * durante el proceso de facturación
	 * @param con
	 * @param numeroTransaccion
	 * @return numero de elemntos eliminados
	 */
	public static int borrarPagosPacienteTempo(Connection con, int numeroTransaccion)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(borrarPagosPacienteTempoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, numeroTransaccion);
			return statement.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("error eliminando los pagos temporales de los pacientes "+e);
			return 0;
		}
	}
	
	/**
	 * Método que actualiza la información de anulación de los pagos de una paciente dada la factura
	 * @param con
	 * @param anulado
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param origenPago
	 * @param codigoDIANFactura
	 * @return
	 */
	public static boolean  actuallizarAnulacionPagosPacienteDadoFactura(Connection con, String anulado, String loginUsuario, int codigoInstitucion,  int origenPago, String codigoDIANFactura)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(actuallizarAnulacionPagosPacienteDadoFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\n actuallizarAnulacionPagosPacienteDadoFactura-->"+actuallizarAnulacionPagosPacienteDadoFacturaStr+" ->anul->"+anulado+" usuario->"+loginUsuario+" inst->"+codigoInstitucion+" orgen pago->"+origenPago+" consefactura->"+codigoDIANFactura);
			
			statement.setString(1, anulado);
			statement.setString(2, loginUsuario);
			statement.setInt(3, codigoInstitucion);
			statement.setInt(4, origenPago);
			statement.setString(5, codigoDIANFactura);
			
			if(statement.executeUpdate()>0)
			    return true;
			else
			    return false;
			    
		}
		catch (SQLException e)
		{
			logger.error("error actuallizarAnulacionPagosPacienteDadoFactura "+e);
			return false;
		}
	}
	
	/**
	 * Método que actualiza el diagnóstico de pagos paciente, si cuando se va ha realizar la atención de la cita, la cuenta
	 * del paciente ya se encuentra facturada y el origen del pago es interno
	 * @param con
	 * @param numeroSolicitud
	 * @param diagnostico
	 * @param tipoCie
	 * @return
	 */
	public static boolean actualizarDiagnosticoCuentaFacturada(Connection con, int numeroSolicitud, String diagnostico, int tipoCie)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(actualizarDiagnosticoCuentaFacturadaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setString(1, diagnostico);
			statement.setInt(2, tipoCie);
			statement.setInt(3, numeroSolicitud);
			
		//	if(statement.executeUpdate()>0)
			    return true;
			//else
			   // return false;
			    
		}
		catch (SQLException e)
		{
			logger.error("error actualizarDiagnosticoCuentaFacturada "+e);
			return false;
		}
	}
	
	
}
