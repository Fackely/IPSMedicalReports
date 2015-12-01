/**
 * Juan David Ramírez 30/06/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Juan David Ramírez
 *
 */
public class SqlBaseAjusteCxCDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseAjusteCxCDao.class);
	
	/**
	 * Sentencia para guardar el ajuste
	 */
	private static String guardarStr="" +
				"INSERT INTO " +
				"	capitacion.ajustes_cxc " +
				"	(" +
				"		codigo, " +
				"		tipo_ajuste, " +
				"		consecutivo, " +
				"		estado, " +
				"		fecha, " +
				"		fecha_grabacion, " +
				"		hora_grabacion, " +
				"		cuenta_cobro, " +
				"		concepto, " +
				"		valor, " +
				"		observaciones, " +
				"		usuario, " +
				"		institucion," +
				"       contabilizado" +
				"	) " +
				"	VALUES " +
				"	(" +
				"		?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?, ?, ?, ?, ?, ?, '"+ConstantesBD.acronimoNo+"'" +
				"	)";
	
	/**
	 * Consultar ajuste existente
	 */
	private static String cargarAjusteStr="" +
						"SELECT " +
						"	acxc.codigo AS codigo, " +
						"	acxc.tipo_ajuste AS tipo_ajuste, " +
						"	acxc.consecutivo AS consecutivo, " +
						"	acxc.estado AS estado, " +
						"	ea.nombre AS nombre_estado, " +
						"	acxc.fecha AS fecha, " +
						"	acxc.concepto AS concepto, " +
						"	acxc.observaciones AS observaciones, " +
						"	cc.numero_cuenta_cobro AS cuenta_cobro, " +
						"	cc.convenio AS convenio, " +
						"	acxc.valor AS valor, " +
						"	cc.saldo_cuenta AS saldo " +
						"FROM " +
						"	capitacion.ajustes_cxc acxc " +
						"INNER JOIN capitacion.cuentas_cobro_capitacion cc " +
						"		ON(cc.numero_cuenta_cobro=acxc.cuenta_cobro AND cc.institucion=acxc.institucion) " +
						"INNER JOIN capitacion.estados_ajuste ea " +
						"		ON(ea.codigo=acxc.estado) " +
						"WHERE acxc.consecutivo=? AND acxc.tipo_ajuste=?";

	/**
	 * Consultar ajuste existente
	 */
	private static String cargarAjusteXCodigoStr="" +
						"SELECT " +
						"	acxc.codigo AS codigo, " +
						"	acxc.tipo_ajuste AS tipo_ajuste, " +
						"	acxc.consecutivo AS consecutivo, " +
						"	acxc.estado AS estado, " +
						"	ea.nombre AS nombre_estado, " +
						"	acxc.fecha AS fecha, " +
						"	acxc.concepto AS concepto, " +
						"	acxc.observaciones AS observaciones, " +
						"	cc.numero_cuenta_cobro AS cuenta_cobro, " +
						"	cc.convenio AS convenio, " +
						"	acxc.valor AS valor, " +
						"	cc.saldo_cuenta AS saldo, " +
						"	anu.motivo AS motivo_anulacion " +
						"FROM " +
						"	capitacion.ajustes_cxc acxc " +
						"INNER JOIN capitacion.cuentas_cobro_capitacion cc " +
						"		ON(cc.numero_cuenta_cobro=acxc.cuenta_cobro AND cc.institucion=acxc.institucion) " +
						"INNER JOIN capitacion.estados_ajuste ea " +
						"		ON(ea.codigo=acxc.estado)" +
						"LEFT OUTER JOIN anulacion_ajuste_cxc anu" +
						"		ON(anu.ajuste=acxc.codigo) " +
						"WHERE acxc.codigo=?";

	private static String cargarCuentaCobroStr="" +
						"SELECT " +
						"	ccc.numero_cuenta_cobro AS codigo, " +
						"	ccc.convenio AS convenio, " +
						"	ccc.saldo_cuenta AS saldo " +
						"FROM " +
						"	capitacion.cuentas_cobro_capitacion ccc " +
						"WHERE " +
						"		ccc.institucion=? " +
						"	AND " +
						"		ccc.numero_cuenta_cobro=? " +
						"	AND " +
						"		ccc.estado IN("+ConstantesBD.codigoEstadoCarteraGenerado+","+ConstantesBD.codigoEstadoCarteraRadicada+")";
	
	private static String modificarAjusteStr="" +
						"UPDATE " +
						"	capitacion.ajustes_cxc " +
						"SET " +
						"	fecha=?, " +
						"	concepto=?, " +
						"	valor=?, " +
						"	observaciones=? " +
						"WHERE " +
						"	codigo=?";
	
	private static String anularStr="" +
						"UPDATE " +
						"	ajustes_cxc " +
						"SET " +
						"	estado = "+ ConstantesBD.codigoEstadoAjusteCxCAnulado+" "+
						"WHERE " +
						"	codigo=?";
	
	private static String ingresoAnulacionStr="" +
						"INSERT INTO " +
						"	anulacion_ajuste_cxc" +
						"	(" +
						"		ajuste, " +
						"		motivo, " +
						"		fecha_grabacion, " +
						"		hora_grabacion, " +
						"		usuario" +
						"	) " +
						"VALUES " +
						"	(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
	
	private static String cargarCarguesStr="" +
						"SELECT " +
						"	cc.codigo AS codigo, " +
						"	con.numero_contrato AS contrato, " +
						"	to_char(cc.fecha_inicial, 'dd/mm/yyyy') || ' - ' || to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_cargue, " +
						"	cc.valor_total-cc.ajustes_credito+cc.ajustes_debito AS saldo " +
						"FROM " +
						"		capitacion.contrato_cargue cc " +
						"	INNER JOIN contratos con " +
						"		ON(con.codigo=cc.contrato) " +
						"	INNER JOIN capitacion.cuentas_cobro_capitacion cob " +
						"		ON(cob.numero_cuenta_cobro=cc.cuenta_cobro AND cob.institucion=cc.institucion) " +
						"	LEFT OUTER JOIN capitacion.ajustes_cargue ac " +
						"		ON(ac.cargue=cc.codigo AND ac.ajuste = ? ) " +
						"WHERE " +
						"		cob.numero_cuenta_cobro=? " +
						"	AND cob.institucion=?";
	
	/**
	 * Consultar el detalle de los ajustes
	 */
	private static String consultarDetalleAjustesStr="" +
													"SELECT " +
													"	cc.codigo AS codigo, " +
													"	con.numero_contrato AS contrato, " +
													"	to_char(cc.fecha_inicial, 'dd/mm/yyyy') || ' - ' || to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_cargue, " +
													"	cc.valor_total-cc.ajustes_credito+cc.ajustes_debito AS saldo, " +
													"	ac.valor AS valor, " +
													"	conc.descripcion AS nombre_concepto, " +
													"	conc.codigo AS concepto " +
													"FROM " +
													"		capitacion.contrato_cargue cc " +
													"	INNER JOIN contratos con " +
													"		ON(con.codigo=cc.contrato) " +
													"	INNER JOIN capitacion.cuentas_cobro_capitacion cob " +
													"		ON(cob.numero_cuenta_cobro=cc.cuenta_cobro AND cob.institucion=cc.institucion) " +
													"	INNER JOIN capitacion.ajustes_cargue ac " +
													"		ON(ac.cargue=cc.codigo) " +
													"	INNER JOIN cartera.concepto_ajustes_cartera conc " +
													"		ON (conc.codigo=ac.concepto AND conc.institucion=ac.institucion) " +
													"	WHERE " +
													"		ac.ajuste=?";

	/**
	 * Consultar el detalle de los ajustes
	 */
	private static String consultarDetalleAjustesTodosStr="" +
													"SELECT " +
													"	cc.codigo AS codigo, " +
													"	con.numero_contrato AS contrato, " +
													"	to_char(cc.fecha_inicial, 'dd/mm/yyyy') || ' - ' || to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_cargue, " +
													"	cc.valor_total-cc.ajustes_credito+cc.ajustes_debito AS saldo, " +
													"	ac.valor AS valor, " +
													"	conc.descripcion AS nombre_concepto," +
													"	conc.codigo AS concepto " +
													"FROM " +
													"		capitacion.contrato_cargue cc " +
													"	INNER JOIN contratos con " +
													"		ON(con.codigo=cc.contrato) " +
													"	INNER JOIN capitacion.ajustes_cxc acxc " +
													"		ON(acxc.cuenta_cobro=cc.cuenta_cobro AND acxc.institucion=cc.institucion) " +
													"	LEFT OUTER JOIN capitacion.ajustes_cargue ac " +
													"		ON(ac.cargue=cc.codigo AND ac.ajuste = ?) " +
													"	LEFT OUTER JOIN cartera.concepto_ajustes_cartera conc " +
													"		ON (conc.codigo=ac.concepto AND conc.institucion=ac.institucion) " +
													"	WHERE acxc.codigo=? ";
													//"		acxc.codigo=? "; --No esta Filtrando los Cargues para la cuenta de cobro Exclusiva.

	/**
	 * Ingresar el detalle de los ajustes
	 */
	private static String detalleAjusteStr="INSERT INTO capitacion.ajustes_cargue (cargue, ajuste, valor, concepto, institucion) VALUES(?,?,?,?,?)";
	
	/**
	 * Borrar detalle de los ajustes caso modificacion
	 */
	private static String borrarDetalleAjustesStr="DELETE FROM capitacion.ajustes_cargue WHERE ajuste=?";

	/**
	 * Método para consultar las opciones de selección de la funcionalidad
	 * @param con
	 * @param tipoConsulta
	 * @param codigoAjuste
	 * @return
	 */
	public static Collection consultarTipos(Connection con, int tipoConsulta, int codigoAjuste)
	{
		String consulta="";
		switch(tipoConsulta)
		{
			case 1:
				consulta="SELECT codigo AS codigo, descripcion AS descripcion FROM tipos_ajuste WHERE codigo IN("+ConstantesBD.codigoConceptosCarteraDebito+", "+ConstantesBD.codigoConceptosCarteraCredito+")";
			break;
			case 2:
				consulta="SELECT codigo AS codigo, nombre AS nombre FROM convenios WHERE activo="+ValoresPorDefecto.getValorTrueParaConsultas();
			break;
			case 3:
				consulta="SELECT " +
							"codigo AS codigo, " +
							"descripcion AS nombre, " +
							"naturaleza AS naturaleza," +
							"tipo_cartera AS tipo_cartera " +
							"FROM cartera.concepto_ajustes_cartera " +
							"WHERE naturaleza " +
							"IN("+ConstantesBD.codigoConceptosCarteraDebito+", "+ConstantesBD.codigoConceptosCarteraCredito+") " +
							"AND tipo_cartera IN("+ConstantesBD.codigoTipoCarteraTodos+", "+ConstantesBD.codigoTipoCarteraCapitacion+") " +
							"ORDER BY descripcion";
			break;
			case 4:
				consulta="SELECT cargue, valor, concepto FROM ajustes_cargue WHERE ajuste="+codigoAjuste;
			break;
			default:
				logger.error("Tipo consulta no definido: "+tipoConsulta);
				return null;
		}
		PreparedStatementDecorator stm= null;
		try
		{
			logger.info(" \n\n  La Consulta [" + consulta + "] \n\n "); 
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error cionsultando tipos "+tipoConsulta+": "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param tipoAjuste
	 * @param consecutivo
	 * @return
	 */
	public static HashMap cargarAjuste(Connection con, int tipoAjuste, int consecutivo)
	{
		PreparedStatementDecorator stm=  null;
		try
		{
			logger.info("\n\n La Consulta (cargarAjusteStr) [" +cargarAjusteStr +"] [" + consecutivo +"] [" + tipoAjuste +"]  \n\n");
			stm= new PreparedStatementDecorator(con.prepareStatement(cargarAjusteStr));
			stm.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
			stm.setInt(2, tipoAjuste);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()), false, false);
	
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error cargando ajuste existente "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Cargar ajuste existente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarAjuste(Connection con, int codigo)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(cargarAjusteXCodigoStr));
			stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()), false, false);
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error cargando ajuste existente (Código: "+codigo+"): "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método para cargar los datos de la cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarCuentaCobro(Connection con, int cuentaCobro, int institucion)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(cargarCuentaCobroStr));
			stm.setInt(1, institucion);
			stm.setDouble(2, Utilidades.convertirADouble(cuentaCobro+""));
			
			//logger.info("\n\n cargarCuentaCobroStr [" + cargarCuentaCobroStr + "][" +cuentaCobro +"] [" +institucion +"] \n\n");
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()), false, false);
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando cuenta de cobro capitación "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	
	/**
	 * Método para cargar los datos de la cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param institucion2 
	 * @return
	 */
	public static HashMap verificarCuentaCobro(Connection con, int tipoConsulta, int cuentaCobro, int institucion)
	{
		String consulta = "";
		PreparedStatementDecorator stm=   null;
		try
		{
			if (tipoConsulta == 1) 
			{
			  consulta = " SELECT ccc.numero_cuenta_cobro AS cuenta_cobro, ac.consecutivo as codigo_ajuste, ta.descripcion as tipo_ajuste									 													 " +
						 "		 FROM capitacion.cuentas_cobro_capitacion ccc 																					 " +
						 "			  INNER JOIN capitacion.ajustes_cxc ac ON ( ac.cuenta_cobro = ccc.numero_cuenta_cobro AND ac.institucion = ccc.institucion ) " +
						 "			  INNER JOIN capitacion.estados_ajuste ea ON ( ea.codigo = ac.estado ) " +
						 "			  INNER JOIN cartera.tipos_ajuste ta ON ( ta.codigo = ac.tipo_ajuste ) " +
						 "			  	   WHERE ccc.institucion = ? 																									 " +
						 "					 AND ccc.numero_cuenta_cobro = ?																							 " +
						 "				     AND ccc.estado IN("+ConstantesBD.codigoEstadoCarteraGenerado+","+ConstantesBD.codigoEstadoCarteraRadicada+")" +
						 "					 AND ac.estado = " +  ConstantesBD.codigoEstadoAjusteCxCPendiente;
			}
			
			if (tipoConsulta == 2) 
			{
			  consulta = " SELECT CASE WHEN fecha_radicacion IS NULL THEN '' ELSE to_char(fecha_radicacion, 'DD/MM/YYYY') END as fecha 																						 " +
						 "		  FROM capitacion.cuentas_cobro_capitacion ccc 																					 " +
						 "			  	    WHERE ccc.institucion = ? 																				 " +
						 "					  AND ccc.numero_cuenta_cobro = ?																		 " +
						 "				      AND ccc.estado = " + ConstantesBD.codigoEstadoCarteraRadicada;
			}
			

			logger.info("\n\n *** -LA CONSULTA cargarCuentaCobroStr [" + consulta + "] cuentaCobro [" +cuentaCobro +"] \n\n");
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta));
			stm.setInt(1, institucion);
			stm.setDouble(2, Utilidades.convertirADouble(cuentaCobro+""));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()), false, false);
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando cuenta de cobro capitación "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método para guardar el nuevo ajuste
	 * @param con
	 * @param tipoAjuste
	 * @param fecha
	 * @param cuentaCobro
	 * @param concepto
	 * @param valorAjuste
	 * @param observaciones
	 * @param usuario
	 * @param institucion
	 * @param consecutivo2 
	 * @return
	 */
	public static int guardar(Connection con, int tipoAjuste, String fecha, int cuentaCobro, String concepto, double valorAjuste, String observaciones, String usuario, int institucion, int consecutivo)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(guardarStr));
			/*
			 *  "1		codigo, " +
				"2		tipo_ajuste, " +
				"3		consecutivo, " +
				"4		estado, " +
				"5		fecha, " +
				"6		cuenta_cobro, " +
				"7		concepto, " +
				"8		valor, " +
				"9		observaciones, " +
				"10		usuario, " +
				"11		institucion" +
			 */
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_ajustes_cxc");
			stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
			stm.setInt(2, tipoAjuste);
			stm.setDouble(3, consecutivo);
			stm.setDouble(4, Utilidades.convertirADouble(ConstantesBD.codigoEstadoAjusteCxCPendiente+""));
			stm.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			stm.setDouble(6, Utilidades.convertirADouble(cuentaCobro+""));
			stm.setString(7, concepto);
			stm.setDouble(8, valorAjuste);
			stm.setString(9, observaciones);
			stm.setString(10, usuario);
			stm.setInt(11, institucion);
			if(stm.executeUpdate()>0)
			{
				return codigo;
			}
			else
			{
				logger.error("Error ingresando el ajuste cxc: (Ne si ingresó ningún valor)");
				return -1;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el ajuste cxc: "+e);
			return -1;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método para modificar el ajuste
	 * @param con
	 * @param codigo
	 * @param fecha
	 * @param concepto
	 * @param valorAjuste
	 * @param observaciones
	 * @return
	 */
	public static int modificar(Connection con, int codigo, String fecha, String concepto, double valorAjuste, String observaciones)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(modificarAjusteStr));

			/*
			 *			"	fecha=?, " +
						"	concepto=?, " +
						"	valor=?, " +
						"	observaciones=? " +
						"WHERE " +
						"	codigo=?";

			 */
			stm.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			stm.setString(2, concepto);
			stm.setDouble(3, valorAjuste);
			stm.setString(4, observaciones);
			stm.setDouble(5, Utilidades.convertirADouble(codigo+""));
			//logger.info("Codigo "+codigo);
			int numRes=stm.executeUpdate();
			stm= new PreparedStatementDecorator(con.prepareStatement(borrarDetalleAjustesStr));
			stm.setInt(1, codigo);
			return stm.executeUpdate()+numRes;
		}
		catch (SQLException e)
		{
			logger.error("Error modificando el ajuste "+e);
			return -1;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método para anular el ajuste
	 * @param con
	 * @param codigo
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @return
	 */
	public static int anular(Connection con, int codigo, String motivoAnulacion, String loginUsuario)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(anularStr));
			stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
			if( stm.executeUpdate()>0)
			{
				stm= new PreparedStatementDecorator(con.prepareStatement(ingresoAnulacionStr));
				stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
				stm.setString(2, motivoAnulacion);
				stm.setString(3, loginUsuario);
				return stm.executeUpdate();
			}
			else
			{
				return -1;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error anulando el ajuste "+e);
			return -1;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método para cargar los cargues del ajuste
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarCargues(Connection con, int cuentaCobro, int ajuste, int institucion)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			logger.info("\n\n Los CARGUES AL ENTRAR AL DETALLE  [" + cargarCarguesStr + "] ajuste [" + ajuste + "]  cuentaCobro [" + cuentaCobro + "]  \n\n");
			stm= new PreparedStatementDecorator(con.prepareStatement(cargarCarguesStr));
			stm.setDouble(1, Utilidades.convertirADouble(ajuste+""));
			stm.setDouble(2, Utilidades.convertirADouble(cuentaCobro+""));
			stm.setInt(3, institucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error cargando los cargues "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método para guardar un detalle de un ajuste
	 * para un cargue
	 * @param con
	 * @param codigoCargue
	 * @param valor
	 * @param ajuste
	 * @param concepto
	 * @param institucion
	 * @param esModificacion
	 * @return
	 */
	public static int detalleAjuste(Connection con, int codigoCargue, double valor, int ajuste, String concepto, int institucion, boolean esModificacion)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(detalleAjusteStr));
			stm.setDouble(1, Utilidades.convertirADouble(codigoCargue+""));
			stm.setDouble(2, Utilidades.convertirADouble(ajuste+""));
			stm.setDouble(3, valor);
			stm.setString(4, concepto);
			stm.setInt(5, institucion);
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando los cargues "+e);
			return -1;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que retorna el detalle de los ajustes
	 * @param con
	 * @param codigoAjuste
	 * @param mostrarTodos
	 * @return
	 */
	public static HashMap consultarDetalleAjustes(Connection con, int codigoAjuste, boolean mostrarTodos)
	{
		PreparedStatementDecorator stm = null;
		try
		{
			if(mostrarTodos)
			{
				logger.info("\n\n EN (consultarDetalleAjustes) consulta  [" + mostrarTodos +"] codigoAjuste [" + codigoAjuste +"] SI [" + consultarDetalleAjustesTodosStr +"] \n\n");
				stm= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleAjustesTodosStr));
			}
			else
			{
				logger.info("\n\n EN (consultarDetalleAjustes) consulta  [" + mostrarTodos +"] codigoAjuste [" + codigoAjuste +"] ELSE [" + consultarDetalleAjustesStr +"] \n\n");
				stm= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleAjustesStr));
			}
			//logger.info("ajuste "+ajuste);
			if (mostrarTodos)
			{
				stm.setDouble(1, Utilidades.convertirADouble(codigoAjuste+""));
				stm.setDouble(2, Utilidades.convertirADouble(codigoAjuste+""));
			}
			else
			{
				stm.setDouble(1, Utilidades.convertirADouble(codigoAjuste+""));
			}
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
		
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando los cargues "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método para eliminar el detalle de los cargues existentes
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarDetalleAjustes(Connection con, int codigo)
	{
		PreparedStatementDecorator stm=   null;
		try
		{
			stm= new PreparedStatementDecorator(con.prepareStatement(borrarDetalleAjustesStr));
			stm.setDouble(1, Utilidades.convertirADouble(codigo+""));
			stm.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando el detalle de los ajustes "+e);
			return false;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

	public static HashMap verificarFechaCierre(Connection con, int tipoFecha, String fechaGeneracionAjuste, int institucion)
	{
		String cad = ""; 
		PreparedStatementDecorator stm=   null;
		try
		{
			//-- Verificar si hay cierre de fecha inicial Cartera Capitacion.
			if (tipoFecha == 0)
			{
				cad =  "	SELECT cs.anio_cierre ||'-'|| cs.mes_cierre	as fecha_cierre						" +
					   "		   FROM cierres_saldos_capitacion cs													" +
					   "				WHERE cs.institucion = " +  institucion +
					   "				  AND cs.tipo_cierre = '" + ConstantesBD.codigoTipoCierreSaldoInicialStr + "'";
			}
				
			//-- Verificar si hay cierre ANUAL Cartera Capitacion.
			if (tipoFecha == 1)
			{
				cad =  "	SELECT max (cs.anio_cierre) as anio_cierre						" +
					   "		   FROM cierres_saldos_capitacion cs						" +
					   "				WHERE cs.institucion = " +  institucion +
					   "				  AND cs.tipo_cierre = '" + ConstantesBD.codigoTipoCierreAnualStr + "'";
			}
				


			//logger.info("\n\n LA CONSULTA cargarCuentaCobroStr [" + cad + "][" +tipoFecha +"] [" +fechaGeneracionAjuste +"] \n\n");
			stm= new PreparedStatementDecorator(con.prepareStatement(cad));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()), false, false);

			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando (verificarFechaCierre) "+e);
			return null;
		}finally{
			try{
				if(stm!=null){
					stm.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
		}
	}

}
