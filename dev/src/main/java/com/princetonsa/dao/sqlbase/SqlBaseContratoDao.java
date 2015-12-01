/*
 * @(#)SqlBaseContratoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Implementacion sql generico de todas las funciones de acceso a la fuente de datos
 * para un  contrato
 *
 * @version 1.0, Abril 30 / 2004
 */
public class SqlBaseContratoDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseContratoDao.class);
	
 	/**
	 * Carga los datos para mostrarlos en el resumen
	 */
	private final static String cargarDatosContrato= 	"SELECT " +
			"c.codigo ||'' AS codigo, " +	
			"c.convenio AS convenio, " +
			"to_char(c.fecha_inicial,'yyyy-mm-dd') AS fechaInicial, " +
			"to_char(c.fecha_final,'yyyy-mm-dd') AS fechaFinal, " +
			"c.numero_contrato AS numeroContrato, " +
			"c.valor AS valorContrato, " +
			"c.acumulado AS valorAcumulado, " +
			"to_char(c.fecha_firma,'yyyy-mm-dd') AS fechaFirmaContrato, " +
			"c.limite_radicacion AS diaLimiteRadicacion, " +
			"c.dias_radicacion AS diasRadicacion, " +
			"c.controla_anticipos AS controlaAnticipos, " +
			"c.maneja_cobertura AS manejaCobertura, " +
			"CASE WHEN c.tipo_pago IS NULL THEN '' ELSE c.tipo_pago ||'' END AS codigoTipoPago, " +
			"CASE WHEN c.upc IS NULL THEN '' ELSE c.upc ||'' END AS upc," +
			"CASE WHEN c.porcentaje_pyp IS NULL THEN '' ELSE c.porcentaje_pyp ||'' END AS porcentaje_pyp, " +
			"CASE WHEN c.contrato_secretaria IS NULL THEN '' ELSE c.contrato_secretaria END AS contrato_secretaria, " +
			"co.tipo_contrato AS codigoTipoContrato, " +
			"CASE WHEN c.porcentaje_upc IS NULL THEN '' ELSE c.porcentaje_upc ||'' END AS porcentaje_upc, " +
			"CASE WHEN c.base IS NULL THEN '' ELSE c.base ||'' END AS codigoBase, " +
			"CASE WHEN c.base IS NULL THEN '' ELSE getNombreTipoContrato(c.base) end as nombreBase, " +
			"c.req_auto_no_cobertura as requiereautonocobertura, " +
			"c.sin_contrato as sinContrato, " +
			"c.observaciones as frmObservaciones, " +
			"c.paciente_paga_atencion, " +
			"c.validar_abono_atencion_odo," +
			"co.tipo_atencion AS tipoatencionconvenio, " +
			"c.maneja_tarifas_x_ca as maneja_tarifas_x_ca " +
			"FROM " +
			"facturacion.contratos c, facturacion.convenios co WHERE co.codigo=c.convenio AND c.codigo= ? ";
	
	/**
	 * cargar los nivelse
	 */
	private final static String cargarNivelesContratos= "SELECT nivel_servicio AS consecutivo, "+ValoresPorDefecto.getValorTrueParaConsultas()+" AS estaBD, "+ValoresPorDefecto.getValorFalseParaConsultas()+" AS fueEliminado FROM capitacion.niveles_contratos WHERE contrato=?";
	
	/**
	 * Hace la modificacion de los datos del contrato
	 */
	private final static String modificarContrato =		"UPDATE " +
														"contratos SET " +
														"numero_contrato= ?, " +
														"fecha_inicial= ?, " +
														"fecha_final= ?, " +
														//"tipo_contrato= ?, " +
														"valor=?, " +
														"acumulado=?, " +
														"fecha_firma=?, " +
														"limite_radicacion=?, " +
														"dias_radicacion=?, " +
														"controla_anticipos=?, " +
														"maneja_cobertura=?, " +
														"tipo_pago=?, " +
														"upc=?, " +
														"porcentaje_pyp=?," +
														"contrato_secretaria=?," +
														"porcentaje_upc=?, " +
														"base = ?, " +
														"req_auto_no_cobertura=?,  " +
														"sin_contrato=?,  " +
														"observaciones=?,  " +
														"paciente_paga_atencion=?, " +
														"validar_abono_atencion_odo=?, " +
														"maneja_tarifas_x_ca=? " +
														"WHERE " +
														"codigo = ?";	
																					
	private final static String eliminarContrato = " DELETE FROM contratos WHERE codigo=?";																																								 
	
	/**
	 * Seleccionar todos los datos de la tabla  contratos para mostrarlos en el listado
	 */
	// MT6009 se elimina ||'' de la linea c.valor AS valorContrato
	private final static String consultarContratos= 	"SELECT " +
															"c.codigo AS codigo,  " +
															"con.nombre AS convenio, " +
															"con.tipo_atencion AS tipoAtencion ," +
															"coalesce(' - '||facturacion.getdescempresainstitucion(con.empresa_institucion),'') AS empresaInstitucion," +
															"c.numero_contrato AS numeroContrato, " +
															"to_char(c.fecha_inicial,'yyyy-mm-dd') AS fechaInicial,  " +
															"to_char(c.fecha_final,'yyyy-mm-dd') AS fechaFinal,  " +
															"c.valor AS valorContrato, " +
															"c.acumulado||'' AS valorAcumulado, " +
															"to_char(c.fecha_firma,'yyyy-mm-dd') AS fechaFirmaContrato, " +
															"c.limite_radicacion AS diaLimiteRadicacion, " +
															"c.dias_radicacion AS diasRadicacion, " +
															"c.controla_anticipos AS controlaAnticipos, " +
															"c.maneja_cobertura AS manejaCobertura, " +
															"CASE WHEN c.tipo_pago IS NULL THEN '' ELSE c.tipo_pago ||'' END AS codigoTipoPago, " +
															"CASE WHEN c.tipo_pago IS NULL THEN '' ELSE tp.nombre ||'' END AS nombreTipoPago, " +
															"CASE WHEN c.upc IS NULL THEN '' ELSE c.upc ||'' END AS upc," +
															"CASE WHEN c.porcentaje_pyp IS NULL THEN '' ELSE c.porcentaje_pyp ||'' END AS porcentaje_pyp, " +
															"CASE WHEN c.contrato_secretaria IS NULL THEN '' ELSE c.contrato_secretaria END AS contrato_secretaria, " +
															"con.tipo_contrato AS codigoTipoContrato, " +
															"facturacion.getNivelesAtencionContrato(c.codigo , ' - ') AS niveles_atencion, "+
															"tc.nombre AS nombreTipoContrato, " +
															"CASE WHEN c.porcentaje_upc IS NULL THEN '' ELSE c.porcentaje_upc ||'' END AS porcentaje_upc, " +
															"CASE WHEN c.base IS NULL THEN '' ELSE c.base ||'' END AS codigoBase, " +
															"CASE WHEN c.base IS NULL THEN '' ELSE facturacion.getNombreTipoContrato(c.base) end as nombreBase, " +
															"c.req_auto_no_cobertura as requiereautonocobertura, " +
															"c.sin_contrato as sinContrato, " +
															"coalesce(c.observaciones, '') as frmobservaciones, " +
															"c.paciente_paga_atencion, " +
															"c.validar_abono_atencion_odo, " +
															"c.maneja_tarifas_x_ca as maneja_tarifas_x_ca " +
														"FROM " +
															"facturacion.contratos c " +
															"INNER JOIN facturacion.convenios con ON (c.convenio=con.codigo) " +
															"INNER JOIN facturacion.empresas emp ON (emp.codigo=con.empresa) " +
															"INNER JOIN facturacion.terceros ter ON (ter.codigo=emp.tercero) " +
															"INNER JOIN facturacion.tipos_contrato tc ON (tc.codigo=con.tipo_contrato) " +
															"LEFT OUTER JOIN facturacion.tipos_pagos tp ON (tp.codigo=c.tipo_pago) " +
														"WHERE " +
															"ter.institucion = ? " +
														"ORDER BY " +
															"con.nombre, c.numero_contrato"; 
	
	/**
	 * Selecciona el codigo del contrato por el codigo de la cuenta
	 */			
	private final static String cargarCodigoContratoCuentaStr="SELECT "+ 
		"sc.contrato AS codigo "+ 
		"FROM cuentas cue "+ 
		"INNER JOIN sub_cuentas sc ON(sc.ingreso = cue.id_ingreso AND sc.nro_prioridad = 1) "+ 
		"INNER JOIN contratos cont ON(cont.codigo=sc.contrato) "+ 
		"WHERE cue.id=? AND "+ 
		"cue.fecha_apertura BETWEEN cont.fecha_inicial AND cont.fecha_final";
	/**
	 * Selecciona el codigo del contrato por el codigo de la subcuenta
	 */
	private final static String cargarCodigoContratoSubCuentaStr="select cont.codigo AS codigo from contratos cont INNER JOIN convenios conv ON(conv.codigo=cont.convenio) INNER JOIN sub_cuentas cue ON(conv.codigo=cue.convenio) WHERE cue.sub_cuenta=?";
	
	/**
	 * Actualiza unicamente el valor acumulado del contrato, dado su codigo, este valor se actualiza en la generacion
	 * de las facturas con el VrContrato 
	 */
	private final static String actualizarValorAcumuladoStr="UPDATE contratos SET acumulado=acumulado + ?  WHERE codigo = ?  ";
	
	/**
	 * Selecciona los datos del contrato de la cuenta
	 */
	private final static String cargarContratoPorSubCuentaStr="SELECT "+ 
		"con.codigo AS codigo,"+
		"'false' AS esta_vencido,"+
		"con.numero_contrato AS numero_contrato "+ 
		"FROM "+ 
		"sub_cuentas sc, contratos con "+ 
		"WHERE "+ 
		"sc.contrato=con.codigo AND "+ 
		"sc.sub_cuenta=?";
	
	/**
	 * nivels de los contratos
	 */
	private final static String insertarNivelesContratos= "INSERT INTO niveles_contratos (contrato, nivel_servicio) VALUES (?, ?)";
	
	/**
	 * nivels de los contratos
	 */
	private final static String eliminarNivelesContratos= "DELETE FROM niveles_contratos WHERE contrato=? and nivel_servicio=? ";
	
	/**
	 * ultimo codigoInsertado
	 */
	private final static String cargarCodigoUltimaInsercion = "SELECT MAX(codigo) AS codigo from contratos";
	
	/**
	 * obtiene el contrato de una cuenta dada
	 */
	private final static String getContratoCuentaStr=" select getContratoCuenta(?) as codigoContrato ";
	
	
	/**
	 * inserta un contrato 
	 * @param con, Connection, conexion abierta con una fuente de datos
	 * @param codigoConvenio, int, captura el convenio al cual corresponde el contrato
	 * @param numeroContrato, String, numero del contrato
	 * @param fechaInicial, String, fecha inicial del contrato
	 * @param fechaFinal, String, fecha final del contrato
	 * @param valorContrato
	 * @param valorAcumulado
	 * @param validarAbonoAtencionOdo 
	 * @param pacientePagaAtencion 
	 * @return  int, 0 si  no inserta, 1 si inserta
	 */
	@SuppressWarnings("unchecked")
	public static int insertar(	Connection con, 
												int codigoConvenio, 
												String numeroContrato, 
												String fechaInicial, 
												String  fechaFinal, 
												//int tipoContrato,
												double valorContrato,
												double valorAcumulado,
												String fechaFirmaContrato,
												String diaLimiteRadicacion,
												String diasRadicacion,
												String controlaAnticipos,
												String manejaCobertura,
												String codigoTipoPago,
												String upc,
												String pyp,
												String contratoSecretaria,
												String insertarContratoStr,
												HashMap nivelAtencionMap,
												String porcentajeUpc,
												String base,
												String requiereautonocobertura,
												String sinContrato,
												String frmObservaciones, 
												String pacientePagaAtencion, 
												String validarAbonoAtencionOdo,
												boolean manejaTarifasXCA
												) 
	{
		int resp=0;

		try
		{		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}

			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con , insertarContratoStr);
			ps.setInt(1,codigoConvenio);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));

			ps.setString(4,numeroContrato);
			ps.setDouble(5,valorContrato);
			ps.setDouble(6,valorAcumulado);
			if(UtilidadTexto.isEmpty(fechaFirmaContrato))
				ps.setObject(7, null);
			else
				ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(fechaFirmaContrato));
			ps.setString(8, diaLimiteRadicacion);
			ps.setString(9, diasRadicacion);

			if(UtilidadTexto.isEmpty(controlaAnticipos))
				ps.setString(10, ConstantesBD.acronimoNo);
			else
				ps.setString(10, controlaAnticipos);

			if(UtilidadTexto.isEmpty(manejaCobertura))
				ps.setString(11, ConstantesBD.acronimoNo);
			else
				ps.setString(11, manejaCobertura);

			if(!codigoTipoPago.trim().equals(""))
				ps.setString(12, codigoTipoPago);
			else
				ps.setObject(12, null);
			if(!upc.trim().equals(""))
				ps.setString(13, upc);
			else
				ps.setObject(13, null);
			if(!pyp.trim().equals(""))
				ps.setString(14, pyp);
			else
				ps.setObject(14, null);
			if(!contratoSecretaria.trim().equals(""))
				ps.setString(15, contratoSecretaria);
			else
				ps.setObject(15, null);

			if(!porcentajeUpc.equals(""))
				ps.setString(16, porcentajeUpc);
			else
				ps.setObject(16, null);
			if(!base.equals(""))
				ps.setString(17, base);
			else
				ps.setObject(17, null);	

			ps.setString(18, requiereautonocobertura);

			if(UtilidadTexto.isEmpty(sinContrato))
				ps.setString(19, ConstantesBD.acronimoNo);
			else
				ps.setString(19, sinContrato);

			if(UtilidadTexto.isEmpty(frmObservaciones))
				ps.setString(20, "");
			else
				ps.setString(20, frmObservaciones);

			if(UtilidadTexto.isEmpty(pacientePagaAtencion))
				ps.setString(21, "");
			else
				ps.setString(21, pacientePagaAtencion);

			if(UtilidadTexto.isEmpty(validarAbonoAtencionOdo))
				ps.setString(22, "");
			else
				ps.setString(22, validarAbonoAtencionOdo);

			ps.setString(23, UtilidadTexto.convertirSN(manejaTarifasXCA+""));

			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.info(" INSERCAR CONTRATO STR ->"+insertarContratoStr);
			logger.info("***********************************************************************");
			logger.info("-- INSERCAR CONTRATO STR ------------>>>>"+ps+"\n\n");

			resp=ps.executeUpdate();
			//se carga el codigoConvenio insertado
			if(resp>0)
			{	
				resp=cargarUltimoCodigo(con);
				//se insertan lo niveles de contrato para el caso capitado
				boolean actualizoNivel=actualizarNivelesContrato(con, resp+"",nivelAtencionMap);
				if(!actualizoNivel)
					resp=-1;
			}	

		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la insercion de datos: SqlBaseContratoDao "+e.toString() );
			resp=0;
		}
		return resp;
	}
	
	/**Carga el ultimo contrato insertado**/
	private static int cargarUltimoCodigo(Connection con)
	{
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoUltimaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(cargarUltimoStatement.executeQuery());
			if(rs.next())
				return rs.getInt("codigo");
		}
		catch(SQLException e)
		{
			logger.warn(" Error en la consulta del ultimo codigo del contrato: SqlBaseContratoDao ",e);
		}
		return -1;
	}
	
	/**
	 * metodo para insertar los niveles contrato
	 * @param con
	 * @param codigoContrato
	 * @param nivelAtencionMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean actualizarNivelesContrato(Connection con, String codigoContrato, HashMap nivelAtencionMap)
	{
		if(nivelAtencionMap.containsKey("numRegistros"))
		{
			
			for(int w=0; w<Integer.parseInt(nivelAtencionMap.get("numRegistros").toString()); w++)
			{
				//primera parte se eliminan los que estan en BD
				if(UtilidadTexto.getBoolean((String)nivelAtencionMap.get("fueEliminado_"+w)) 
						&& UtilidadTexto.getBoolean((String)nivelAtencionMap.get("estaBD_"+w)))
				{	
					try
					{
						PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarNivelesContratos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setString(1, codigoContrato);
						ps.setString(2, nivelAtencionMap.get("consecutivo_"+w).toString());
						if(ps.executeUpdate()<0)
						{	
							logger.warn(" Error en el eliminar datos niveles para el contrato: "+codigoContrato+" y nivel: "+nivelAtencionMap.get("consecutivo_"+w)+" SqlBaseContratoDao ");
							return false;
						}
					}
					catch (SQLException e)
					{
						logger.warn(" Error en el eliminar de datos niveles para el contrato: "+codigoContrato+" y nivel: "+nivelAtencionMap.get("consecutivo_"+w)+" SqlBaseContratoDao "+e.toString() );
						return false;
					}
				}	
				
				//segunda parte para la insercion de los nuevos
				if(!(UtilidadTexto.getBoolean((String)nivelAtencionMap.get("fueEliminado_"+w))) 
						&& !(UtilidadTexto.getBoolean((String)nivelAtencionMap.get("estaBD_"+w))))
				{	
					try
					{
						PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarNivelesContratos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setString(1, codigoContrato);
						ps.setString(2, nivelAtencionMap.get("consecutivo_"+w).toString());
						if(ps.executeUpdate()<0)
						{	
							logger.warn(" Error en la insercion de datos niveles para el contrato: "+codigoContrato+" y nivel: "+nivelAtencionMap.get("consecutivo_"+w)+" SqlBaseContratoDao ");
							return false;
						}	
					}
					catch (SQLException e)
					{
						logger.warn(" Error en la insercion de datos niveles para el contrato: "+codigoContrato+" y nivel: "+nivelAtencionMap.get("consecutivo_"+w)+" SqlBaseContratoDao "+e.toString() );
						return false;
					}
				}	
			}
		}
		return true;
	}
	
	
	/** Metodo que indica si el contrato puede 
	 *  ser insertado o no en un rango de fechas
	 *  que pertenezcan al mismo convenio
	*/
	public static boolean puedoInsertarContrato(	Connection con, 
													int convenio, 
													String fechaInicial, 
													String fechaFinal,
													String estadoInsertaOModifica,
													String fechaInicialAntigua,
													String fechaFinalAntigua
													) 
	{
		//solo aplica para los contratos que no son capitados

		String consultaRangosFechaStr=	"SELECT  " +
											"c.codigo " +
										"FROM contratos c  " +
											"inner join convenios co ON (co.codigo=c.convenio)  " +
										"WHERE  " +
											"c.convenio = ? " +
											"AND co.tipo_contrato<> "+ConstantesBD.codigoTipoContratoCapitado +" "+
											"AND ((? >= c.fecha_inicial " +
											"AND ? <= c.fecha_final) " +
											"OR (? >= c.fecha_inicial " +
											"AND ? <= c.fecha_final))";

		try
		{
			if(estadoInsertaOModifica.equals("modificar"))
				consultaRangosFechaStr = consultaRangosFechaStr + " AND (c.fecha_inicial<> ? AND c.fecha_final<>?) ";

			fechaInicial= UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
			fechaFinal= UtilidadFecha.conversionFormatoFechaABD(fechaFinal);

			PreparedStatementDecorator cargarRStatement= new PreparedStatementDecorator(con.prepareStatement(consultaRangosFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarRStatement.setInt(1, convenio);
			cargarRStatement.setDate(2,Date.valueOf(fechaInicial));
			cargarRStatement.setDate(3,Date.valueOf(fechaInicial));
			cargarRStatement.setDate(4,Date.valueOf(fechaFinal));
			cargarRStatement.setDate(5,Date.valueOf(fechaFinal));

			logger.info("Consulta ------>"+consultaRangosFechaStr);
			logger.info("convenio ------>"+convenio);
			logger.info("fechainicial -->"+fechaInicial);
			logger.info("fechaFinal-- -->"+fechaFinal);

			if(estadoInsertaOModifica.equals("modificar"))
			{
				cargarRStatement.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaInicialAntigua));
				cargarRStatement.setString(7, UtilidadFecha.conversionFormatoFechaABD(fechaFinalAntigua));
			}
			ResultSetDecorator rs=new ResultSetDecorator(cargarRStatement.executeQuery());
			if(rs.next())
			{
				logger.info("encontre contrato cruzado");
				return false;
			}
			else
			{
				logger.info("NOOO encontre contrato cruzado");
				return true;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		return false;	
	}

	/**
	 * Metodo que  carga  los datos de un contrato para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator cargar(Connection con, String codigoContrato) throws SQLException
	{
		PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosContrato,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarResumenStatement.setInt(1, Integer.parseInt(codigoContrato));
		return new ResultSetDecorator(cargarResumenStatement.executeQuery());
	}
	
	/**
	 * Metodo que elimina un contrato segun su codigo, solo se puede eliminar 
	 * si el contrato tiene  fechas >= al sistema 
	 */
	public static int eliminarContrato(Connection con, int codigo)
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexion cerrada");
			}

			PreparedStatementDecorator psE= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM esq_tar_procedimiento_contrato WHERE contrato=?"));
			psE.setInt(1, codigo);
			psE.executeUpdate();
			psE.close();

			psE= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM esq_tar_inventarios_contrato WHERE contrato=?"));
			psE.setInt(1, codigo);
			psE.executeUpdate();
			psE.close();


			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarContrato,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1,codigo);

			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la insercion de datos: SqlBaseContratoDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	/**
	 * Modifica un contrato dado su codigo con los param�tros dados.
	 * @param validarAbonoAtencionOdo 
	 * @param pacientePagaAtencion 
	 * @param con, Connection, conexion abierta con una fuente de datos
	 * @param codigo, int, codigo del contrato
	 * @param numeroContrato, String, numero del contrato
	 * @param fechaInicial, String, fecha inicial del contrato
	 * @param fechaFinal, String, fecha final del contrato
	 * @return  int, 0 si  no inserta, 1 si inserta
	 */																				
	@SuppressWarnings("unchecked")
	public static int modificar(	Connection con, 
													int codigo, 
													String numeroContrato,
													String fechaInicial, 
													String  fechaFinal, 
													double valorContrato,
													double valorAcumulado,
													String fechaFirmaContrato,
													String diaLimiteRadicacion,
													String diasRadicacion,
													String controlaAnticipos,
													String manejaCobertura,
													String codigoTipoPago,
													String upc,
													String pyp,
													String contratoSecretaria,
													HashMap nivelAtencionMap,
													String porcentajeUpc,
													String base,
													String requiereautonocobertura, 
													String sinContrato, 
													String frmObservaciones, 
													String pacientePagaAtencion, 
													String validarAbonoAtencionOdo,
													boolean manejaTarifasXCA
													)
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexion cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarContrato,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			logger.info("modificarContrato-->"+modificarContrato);

			ps.setString(1,numeroContrato);
			ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			ps.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			ps.setDouble(4,valorContrato);
			logger.info("valor contrato -->"+valorContrato);
			ps.setDouble(5,valorAcumulado);
			logger.info("valor acumulado -->"+valorAcumulado);
			if(UtilidadTexto.isEmpty(fechaFirmaContrato))
				ps.setObject(6, null);
			else
				ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaFirmaContrato));
			ps.setString(7, diaLimiteRadicacion);
			ps.setString(8, diasRadicacion);
			ps.setString(9, controlaAnticipos);
			ps.setString(10, manejaCobertura);

			//parte capitacion
			if(!codigoTipoPago.trim().equals(""))
				ps.setString(11, codigoTipoPago);
			else
				ps.setObject(11, null);
			if(!upc.trim().equals(""))
				ps.setString(12, upc);
			else
				ps.setObject(12, null);
			if(!pyp.trim().equals(""))
				ps.setString(13, pyp);
			else
				ps.setObject(13, null);
			if(!contratoSecretaria.trim().equals(""))
				ps.setString(14, contratoSecretaria);
			else
				ps.setObject(14, null);

			if(!porcentajeUpc.equals(""))
				ps.setString(15, porcentajeUpc);
			else
				ps.setObject(15, null);

			if(!base.equals(""))
				ps.setString(16, base);
			else
				ps.setObject(16, null);

			ps.setString(17, requiereautonocobertura);

			ps.setString(18, sinContrato);

			ps.setString(19, frmObservaciones);

			if(!pacientePagaAtencion.equals(""))
				ps.setString(20, pacientePagaAtencion);
			else
				ps.setObject(20, null);

			if(!validarAbonoAtencionOdo.equals(""))
				ps.setString(21, validarAbonoAtencionOdo);
			else
				ps.setObject(21, null);

			ps.setString(22,UtilidadTexto.convertirSN(manejaTarifasXCA+""));
			
			ps.setInt(23,codigo);

			resp=ps.executeUpdate();

			if(resp>0)
			{	
				//se insertan lo niveles de contrato para el caso capitado
				boolean actualizoNivel=actualizarNivelesContrato(con, codigo+"",nivelAtencionMap);
				if(!actualizoNivel)
				{	
					resp=-1;
				}	
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la insercion de datos: SqlBaseContratoDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Metodo que contiene el Resulset de los datos de la tabla contratos
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexion abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla contratos
	 * @throws SQLException
	 */
	public static  ResultSetDecorator listado(Connection con, int codigoInstitucion) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexion "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarContratos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			respuesta=new ResultSetDecorator(ps.executeQuery());
			logger.info("\n\n\n\n");
			logger.info(" "+consultarContratos);
			logger.info("\n\n\n\n\n");
		}
		catch(SQLException e)
		{
			logger.error("Error en el listado contratos ", e);
			respuesta=null;
		}
		return respuesta;
	}
	
	
	/**
	 * 	
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean pacientePagaAtencion( int codigoContrato )
	{
		boolean resultado = false;

		String consultaStr=	"SELECT " +
								"ct.paciente_paga_atencion as paciente_paga_atencion " +
							"from " +
								"facturacion.contratos ct " +
							"WHERE  " +
								"ct.codigo = "+codigoContrato +" ";


		logger.info("CONSULTA paciente paga at"+consultaStr);

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= UtilidadTexto.getBoolean(rs.getString(1));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);

		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean pacienteValidaBonoAtenOdo( int codigoContrato )
	{
		boolean resultado = false;

		String consultaStr=	"SELECT " +
								"ct.validar_abono_atencion_odo as validar_abono_atencion_odo " +
							"from " +
								"facturacion.contratos ct " +
							"WHERE  " +
								"ct.codigo = "+codigoContrato +" ";
	
		logger.info("CONSULTA paciente paga at"+consultaStr);

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= UtilidadTexto.getBoolean(rs.getString(1));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);

		}
		return resultado;
	}
	
	/**
	 * 	
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean controlaAnticipos( int codigoContrato )
	{
		boolean resultado = false;

		String consultaStr=	"SELECT " +
								"ct.controla_anticipos as controla_anticipos " +
							"from " +
								"facturacion.contratos ct " +
							"WHERE  " +
								"ct.codigo = "+codigoContrato +" ";
		
		logger.info("CONSULTA paciente paga at"+consultaStr);

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= UtilidadTexto.getBoolean(rs.getString(1));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);

		}
		return resultado;
	}
	
	
	/**
	 * Metodo que contiene el Resulset de todas las empresas buscadas
	 * @param con, Connection, conexion abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busqueda(	Connection con, 
												String nombreConvenio, 
												String numeroContrato,
												String eleccionFechaBusqueda,
												String fechaInicial, 
												String  fechaFinal, 
												//String nombreTipoContrato,
												int codigoInstitucion,
												double valorContrato,
												double valorAcumulado,
												String nombreTipoPago,
												String upc,
												String pyp,
												String contratoSecretaria,
												String busquedaNivelAtencion,
												String porcentajeUpc,
												String base,
												String requiereautonocobertura, 
												String sinContrato, String esquemaTarInventario, String esquemaTarProcedimiento,
												String frmObservaciones,
												String manejaTarifasXCA
												) throws SQLException
												{
		ResultSetDecorator respuesta=null;
		String consultaArmada="";
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexion "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			consultaArmada=armarConsulta(	nombreConvenio, 
					numeroContrato,
					eleccionFechaBusqueda,
					fechaInicial, 
					fechaFinal, 
					//nombreTipoContrato,
					codigoInstitucion,
					valorContrato, valorAcumulado,
					nombreTipoPago,
					upc,
					pyp,
					contratoSecretaria,
					busquedaNivelAtencion,
					porcentajeUpc,
					base,
					requiereautonocobertura,
					sinContrato,esquemaTarInventario,esquemaTarProcedimiento,
					frmObservaciones,
					manejaTarifasXCA
			);

			logger.info("--->"+consultaArmada);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la busqueda avanzada del contrato" +e.toString());
			respuesta=null;
		}
		return respuesta;
												}

	/**
	 * Metodo que arma la consulta segun los datos dados por el usuarios en 
	 * la busqueda avanzada. 
	 * @param esquemaTarProcedimiento 
	 * @param esquemaTarInventario 
	 */
	private static String armarConsulta  (		String nombreConvenio, 
												String numeroContrato,
												String eleccionFechaBusqueda,
												String fechaInicial, 
												String  fechaFinal, 
												//String nombreTipoContrato,
												int codigoInstitucion,
												double valorContrato,
												double valorAcumulado,
												String nombreTipoPago,
												String upc,
												String pyp,
												String contratoSecretaria,
												String busquedaNivelAtencion,
												String porcentajeUpc,
												String base,
												String requiereautonocobertura,
												String sinContrato, String esquemaTarInventario, String esquemaTarProcedimiento,
												String frmObservaciones,
												String manejaTarifasXCA
										)
	{
			String consulta=	"SELECT " +
										"c.codigo AS codigo,  " +
										"con.nombre AS convenio, " +
										"con.tipo_atencion AS tipoAtencion ," +
										"coalesce(' - '||getdescempresainstitucion(con.empresa_institucion),'') AS empresaInstitucion," +
										"c.numero_contrato AS numeroContrato, " +
										"to_char(c.fecha_inicial,'yyyy-mm-dd') AS fechaInicial, " +
										"to_char(c.fecha_final,'yyyy-mm-dd') AS fechaFinal, " +
										"c.valor AS valorContrato, " +
										"c.acumulado AS valorAcumulado,  " +
										"CASE WHEN c.tipo_pago IS NULL THEN '' ELSE c.tipo_pago ||'' END AS codigoTipoPago, " +
										"CASE WHEN c.tipo_pago IS NULL THEN '' ELSE tp.nombre ||'' END AS nombreTipoPago, " +
										"CASE WHEN c.upc IS NULL THEN '' ELSE c.upc ||'' END AS upc," +
										"CASE WHEN c.porcentaje_pyp IS NULL THEN '' ELSE c.porcentaje_pyp ||'' END AS porcentaje_pyp, " +
										"CASE WHEN c.contrato_secretaria IS NULL THEN '' ELSE c.contrato_secretaria END AS contrato_secretaria, " +
										"con.tipo_contrato AS codigoTipoContrato, " +
										"getNivelesAtencionContrato(c.codigo , ' - ') AS niveles_atencion, " +
										"tc.nombre AS nombreTipoContrato, " +
										"CASE WHEN c.porcentaje_upc IS NULL THEN '' ELSE c.porcentaje_upc ||'' END AS porcentaje_upc, " +
										"CASE WHEN c.base IS NULL THEN '' ELSE c.base ||'' END AS codigoBase, " +
										"CASE WHEN c.base IS NULL THEN '' ELSE getNombreTipoContrato(c.base) end as nombreBase, " +
										"c.req_auto_no_cobertura as requiereautonocobertura, " +
										"c.sin_contrato as sinContrato, " +
										"c.observaciones as frmObservaciones, " +
										"c.paciente_paga_atencion, " +
										"c.validar_abono_atencion_odo, " +
										"c.maneja_tarifas_x_ca " +
										"FROM contratos c " +
										"INNER JOIN convenios con ON (c.convenio=con.codigo ";
		
		if(nombreConvenio != null && !nombreConvenio.equals(""))
		{
			consulta = consulta + "AND UPPER(con.nombre) LIKE  UPPER('%"+nombreConvenio+"%')  ";
		}
		if(numeroContrato != null && !numeroContrato.equals(""))
		{
			consulta = consulta + "AND UPPER(c.numero_contrato) LIKE  UPPER('%"+numeroContrato+"%')  ";
		}
		if(valorContrato != ConstantesBD.codigoNuncaValido)
		{
			consulta = consulta + "AND valor = "+valorContrato+" ";
		}
		if(valorAcumulado != ConstantesBD.codigoNuncaValido)
		{
			consulta = consulta + "AND acumulado = "+valorAcumulado+" ";
		}
		if((fechaInicial!=null && !fechaInicial.equals(""))&&(fechaFinal!=null && !fechaFinal.equals("")) )
		{
			fechaInicial= UtilidadFecha.conversionFormatoFechaABD(fechaInicial);
			fechaFinal= UtilidadFecha.conversionFormatoFechaABD(fechaFinal);
			if(eleccionFechaBusqueda.equals("inicial"))
				consulta=consulta+ "AND to_char(c.fecha_inicial,'yyyy-mm-dd') >= '"+fechaInicial+"' AND to_char(c.fecha_inicial,'yyyy-mm-dd') <= '"+fechaFinal+"'";
			else if(eleccionFechaBusqueda.equals("final"))
				consulta=consulta+ "AND to_char(c.fecha_final,'yyyy-mm-dd') >= '"+fechaInicial+"' AND to_char(c.fecha_final,'yyyy-mm-dd') <= '"+fechaFinal+"'";	
			//consulta = consulta + "AND (('"+fechaInicial+"' >= c.fecha_inicial AND '"+fechaInicial+"' <= c.fecha_final) OR ('"+fechaFinal+"' >= c.fecha_inicial AND '"+fechaFinal+"' <= c.fecha_final))";
		}
		
		/*consulta= consulta + ") INNER JOIN tipos_contrato t ON(c.tipo_contrato=t.codigo ";
		
		if(nombreTipoContrato!=null && !nombreTipoContrato.equals(""))
		{
			consulta= consulta + "AND UPPER(t.nombre) LIKE UPPER('%"+nombreTipoContrato+"%') ";
		}*/
		
		
		
		consulta+= 	") INNER JOIN empresas emp ON (emp.codigo=con.empresa) " +
							" INNER JOIN terceros ter ON (ter.codigo=emp.tercero) " +
							"INNER JOIN tipos_contrato tc ON (tc.codigo=con.tipo_contrato) " +
							"LEFT OUTER JOIN tipos_pagos tp ON (tp.codigo=c.tipo_pago) ";
		
		if(!UtilidadTexto.isEmpty(esquemaTarInventario))
		{
			consulta=consulta+"" +
					"INNER JOIN esq_tar_inventarios_contrato etic ON (etic.contrato=c.codigo) ";
		}
		if(!UtilidadTexto.isEmpty(esquemaTarProcedimiento))
		{
			consulta=consulta+"" +
					"INNER JOIN esq_tar_procedimiento_contrato etpc ON (etpc.contrato=c.codigo) ";
		}
		
		
		consulta+=" WHERE ter.institucion = "+codigoInstitucion; 
		
		if(!nombreTipoPago.trim().equals(""))
			consulta+=  " AND UPPER(tp.nombre) LIKE UPPER('%"+nombreTipoPago+"%') ";
		
		if(!upc.trim().equals(""))
			consulta+=" AND c.upc = "+upc;
		
		if(!pyp.trim().equals(""))
			consulta+=" AND c.porcentaje_pyp = "+pyp;
		
		if(!contratoSecretaria.trim().equals(""))
			consulta+=" AND UPPER(c.contrato_secretaria) LIKE UPPER ('%"+contratoSecretaria+"%') ";
		
		if(!busquedaNivelAtencion.trim().equals(""))
			consulta+="AND UPPER(getNivelesAtencionContrato(c.codigo , ' - ')) like UPPER('%"+busquedaNivelAtencion+"%') ";
		
		if(!porcentajeUpc.trim().equals(""))
			consulta+=" AND c.porcentaje_upc = "+porcentajeUpc+" ";
		
		if(!base.trim().equals(""))
			consulta+=" AND c.base =  "+base+" ";
		
		if(!UtilidadTexto.isEmpty(requiereautonocobertura))
			consulta+=" AND c.req_auto_no_cobertura ='"+requiereautonocobertura+"' ";
		
		if(!UtilidadTexto.isEmpty(sinContrato))
			consulta+=" AND c.sin_contrato ='"+sinContrato+"' ";
		
		if(!UtilidadTexto.isEmpty(esquemaTarInventario))
		{
			consulta=consulta+"" +
					" AND etic.esquema_tarifario="+esquemaTarInventario;
		}
		if(!UtilidadTexto.isEmpty(esquemaTarProcedimiento))
		{
			consulta=consulta+"" +
					" AND etpc.esquema_tarifario="+esquemaTarProcedimiento;
		}
		
		if(!UtilidadTexto.isEmpty(manejaTarifasXCA))
		{
			consulta+=" AND c.maneja_tarifas_x_ca = '"+manejaTarifasXCA+"' ";
		}
		
		consulta= consulta + " ORDER BY con.nombre, c.numero_contrato";
		
		return consulta;	
	}	
	
	/**
	 * Adiciones Sebastian
	 * Metodo que carga el codigo del contrato de una subcuenta o cuenta
	 * @param con
	 * @param id
	 * @param opcion si es true es cuenta y si es false es subcuenta
	 * @return
	 */
	public static int cargarCodigoContrato(Connection con,int id,boolean opcion)
	{
		ResultSetDecorator rs;
		try
		{
			PreparedStatementDecorator pst;
			if(opcion)
				pst= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoContratoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			else
				pst= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoContratoSubCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,id);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				rs.close();
				pst.close();
				return rs.getInt("codigo");
			}	
			else
			{	
				rs.close();
				pst.close();
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn("Error al cargar codigo de contrato SqlBaseContratoDao" +e.toString());
			return -1;
		}
	}
	
	/**
	 * Actualiza unicamente el valor acumulado del contrato, dado su codigo, este valor se actualiza en la generacion
	 * de las facturas con el VrContrato
	 * @param con
	 * @param codigo
	 * @param valorAcumASumar
	 * @return
	 */
	public static int actualizarValorAcumulado(		Connection con, 
													int codigo, 
													double valorAcumASumar)
	{
	    int resp=0;	
	    try
	    {
	        if (con == null || con.isClosed()) 
	        {
	            throw new SQLException ("Error SQL: conexion cerrada");
	        }
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarValorAcumuladoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,valorAcumASumar);
	        ps.setInt(2,codigo);
			
			logger.info("\n\n Se hace commit de este valor \n\n actualizarValorAcumuladoStr-->"+actualizarValorAcumuladoStr+" -->cod-->"+codigo+" valorAsumar-->"+valorAcumASumar);
			
			resp=ps.executeUpdate();
			con.commit();
	    }
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualizacion Vr Acumulado de datos: SqlBaseContratoDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	/**
	 * Adicion Sebastian
	 * Metodo que carga los datos del contrato a partir del id de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap cargarContratoPorSubCuenta(Connection con,double subCuenta)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"esta_vencido",
				"numero_contrato"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarContratoPorSubCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cargarContratoPorSubCuentaStr->"+cargarContratoPorSubCuentaStr);
			pst.setLong(1,(long)subCuenta);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarContratoPorSubCuenta en SqlBaseContratoDao: "+e);
			return null;
		}
	}

	/**
	 * carga los niveles
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap cargarNivelesContratos(Connection con, String codigoContrato)
	{
		String[] columnas={
				"consecutivo",
				"estaBD",
				"fueEliminado",
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarNivelesContratos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoContrato);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());			
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,rs,false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarNivelesContratos en SqlBaseContratoDao: "+e);
			return null;
		}
	}
	
	/**
	 * obtiene el contrato de una cuenta dada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int getContratoCuenta(Connection con, String idCuenta)
	{
		int codigoContrato=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(getContratoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoContrato=rs.getInt("codigoContrato");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getContratoCuenta en SqlBaseContratoDao: "+e);
		}
		return codigoContrato;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static boolean estaContratoVencido(Connection con, int codigoContrato)
	{
		String consulta="SELECT codigo from contratos cont where cont.codigo=? and cont.fecha_inicial<=CURRENT_DATE and cont.fecha_final>=CURRENT_DATE";
		try
		{
			ResultSetDecorator rs=UtilidadBD.ejecucionGenericaResultSetDecorator(con, codigoContrato, 1, consulta);
			if(rs.next())
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en estaContratoVencido SqlBaseContratoDao: "+e);
		}
		return false;
	}
	
	/**
	 * metodo que valida que no exista el mismo numero de Contrato para el convenio cuando las fechas se traslapen
	 * @param con
	 * @param convenioAInsertar
	 * @param numeroContratoAInsertar
	 * @param fechaInicialAInsertar
	 * @param fechaFinalAInsertar
	 * @param codigoContratoCuandoEsModificacion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap validacionNumeroContrato(Connection con, String convenioAInsertar, String numeroContratoAInsertar, String fechaInicialAInsertar, String fechaFinalAInsertar, String codigoContratoCuandoEsModificacion)
	{
		fechaInicialAInsertar= UtilidadFecha.conversionFormatoFechaABD(fechaInicialAInsertar);
		fechaFinalAInsertar= UtilidadFecha.conversionFormatoFechaABD(fechaFinalAInsertar);
		
		String consulta="select  codigo, to_char(fecha_inicial, 'DD/MM/YYYY') as fecha_inicial, to_char(fecha_final, 'DD/MM/YYYY') as fecha_final, getnombreconvenio(convenio) AS nombre_convenio from contratos " +
						"where convenio= "+convenioAInsertar+" and numero_contrato='"+numeroContratoAInsertar+"' " +
						"and ((to_char(fecha_inicial,'yyyy-mm-dd') <= '"+fechaInicialAInsertar+"' AND to_char(fecha_final,'yyyy-mm-dd') >='"+fechaFinalAInsertar+"') " +
								"or (to_char(fecha_inicial,'yyyy-mm-dd') >= '"+fechaInicialAInsertar+"' AND to_char(fecha_inicial,'yyyy-mm-dd') <= '"+fechaFinalAInsertar+"') " +
								"or (to_char(fecha_inicial,'yyyy-mm-dd') <= '"+fechaInicialAInsertar+"' and  to_char(fecha_final,'yyyy-mm-dd') >='"+fechaInicialAInsertar+"' and to_char(fecha_final,'yyyy-mm-dd')<='"+fechaFinalAInsertar+"'))";
		
		if(!codigoContratoCuandoEsModificacion.equals(""))
		{
			consulta+=" and codigo <> "+codigoContratoCuandoEsModificacion;
		}
		
		String[] columnas={
				"codigo",
				"fecha_inicial",
				"fecha_final",
				"nombre_convenio"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionNumeroContrato en SqlBaseContratoDao: "+e);
			return null;
		}
	}

	/**
	 * metodo que indica si un conterato maneja o no cobertura
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static boolean manejaCobertura(Connection con, int codigoContrato) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado=false;
		
		try {
			Log4JManager.info("############## Inicio manejaCobertura");
			String consulta="SELECT maneja_cobertura AS maneja FROM facturacion.contratos WHERE codigo="+codigoContrato;
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			
			if(rs.next())
			{	
				if(rs.getString("maneja") != null && rs.getString("maneja").toString().equals(ConstantesBD.acronimoSi)){
					resultado=true;
				}
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
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
				Log4JManager.error("Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin manejaCobertura");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static boolean requiereAutorizacionXNoCobertura(Connection con, int codigoContrato)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT coalesce(req_auto_no_cobertura,'') AS requiere FROM contratos WHERE codigo="+codigoContrato;
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next())
			{	
				if(rs.getString("requiere").equals(ConstantesBD.acronimoSi))
					return true;
				else
					return false;
			}	
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR requiereAutorizacionXNoCobertura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR requiereAutorizacionXNoCobertura", e);
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
		return false;
	}
	
	/**
	 * metodo que obiene los cpntratos vencidos dado la cuenta y sus contratos
	 * @param con
	 * @param cuenta
	 * @param convenios
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap obtenerContratosVencidosXCuenta (Connection con, String cuenta, Vector contratos)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=	"SELECT " +
					"conv.codigo as codigoconvenio, " +
					"conv.nombre AS nombreconvenio, " +
					"to_char(fecha_inicial,'dd/mm/yyyy') AS fechainicial, " +
					"to_char(fecha_final,'dd/mm/yyyy') AS fechafinal " +
				"FROM " +
					"contratos cont " +
					"INNER JOIN convenios conv ON(cont.convenio=conv.codigo) " +
				"WHERE " +
					"cont.codigo IN ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(contratos, false)+") "+
					"AND ((SELECT fecha_apertura FROM cuentas where id="+cuenta+")<fecha_inicial " +
					"OR (SELECT fecha_apertura FROM cuentas where id="+cuenta+")>fecha_final) " +
					"AND conv.tipo_regimen <> '"+ConstantesBD.codigoTipoRegimenParticular+"' " ;

			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerTarifaBaseServicio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerTarifaBaseServicio", e);
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
		return mapa;
	}
	
	/**
	 * metodo que obiene los cpntratos vencidos dado la cuenta y sus contratos
	 * @param con
	 * @param cuenta
	 * @param convenios
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerContratosVencidos (Connection con, Vector contratos)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String consulta=	"SELECT " +
								"conv.codigo as codigoconvenio, " +
								"conv.nombre AS nombreconvenio, " +
								"to_char(fecha_inicial,'dd/mm/yyyy') AS fechainicial, " +
								"to_char(fecha_final,'dd/mm/yyyy') AS fechafinal " +
							"FROM " +
								"contratos cont " +
								"INNER JOIN convenios conv ON(cont.convenio=conv.codigo) " +
							"WHERE " +
								"cont.codigo IN ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(contratos, false)+") "+
								"AND (current_date<fecha_inicial OR current_date>fecha_final) " +
								"AND conv.tipo_regimen <> '"+ConstantesBD.codigoTipoRegimenParticular+"' " ;
		
		try
		{
			logger.info("consulta contratos vencidos->"+consulta);
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
				e.printStackTrace();
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap obtenerContratosTopesCompletos (Connection con, Vector contratos, String cuenta)
	{
		HashMap mapa= new HashMap();
	    PreparedStatement pst=null;
	    ResultSet rs=null;
		try 
		{
			String consulta=	"SELECT " +
						"conv.nombre as nombreconvenio, " +
						"cont.valor AS valor, " +
						"cont.acumulado AS acumulado " +
					"FROM " +
						"contratos cont " +
					"INNER JOIN " +
						"convenios conv ON (cont.convenio=conv.codigo) ";
			
			if(!cuenta.equals("")){  
				consulta +=		"INNER JOIN " +
							"cuentas c ON (c.id="+cuenta+") " +
						"INNER JOIN " +
							"manejopaciente.sub_cuentas sc ON (sc.ingreso=c.id_ingreso AND sc.convenio=conv.codigo AND sc.nro_prioridad=1) ";
			}
			consulta +=			"WHERE " +
						"cont.codigo in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(contratos, false)+") " +
						"AND cont.valor>0 " +
						"AND cont.acumulado>=cont.valor "+
						"AND conv.tipo_regimen<>'" + ConstantesBD.codigoTipoRegimenParticular +"' ";
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerContratosTopesCompletos",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerContratosTopesCompletos", e);
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
	    return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @return
	 */
	public static boolean acumuladoMenorValorContrato(Connection con, int contrato) 
	{
		String cadena=" SELECT case when acumulado<valor then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from contratos where codigo="+contrato;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		} 
		catch (SQLException e) 
		{
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static String obtenerNumeroContrato(Connection con, int codigoContrato)
	{
		String cadena=" SELECT numero_contrato from contratos where codigo="+codigoContrato;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public static HashMap cargarEsquemasTarifarioInventarios(Connection con, String codigoContrato) throws BDException
	{
		HashMap mapa=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio cargarEsquemasTarifarioInventarios");
			String cadena="SELECT " +
				"codigo," +
				"contrato," +
				"coalesce(clase_inventario||'','') as claseinventario," +
				"case when clase_inventario is null then 'Todos' else getnomclaseinventario(clase_inventario) end as nombreclaseinventario," +
				"esquema_tarifario as esquematarifario," +
				"getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario," +
				"to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia," +
				"to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal," +
				"'BD' as tipoRegistro, " +
				"coalesce(centro_atencion, "+ConstantesBD.codigoNuncaValido+") as codcentroatencion, " +
				"case when centro_atencion is null then 'Todos' else getnomcentroatencion(centro_atencion) end as nombrecentroatencion " +
			"from " +
				"inventarios.esq_tar_inventarios_contrato " +
			"where " +
				"contrato=?";
			
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setObject(1, codigoContrato);
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next()){
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
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
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin cargarEsquemasTarifarioInventarios");
		return mapa;
	}

	/** 
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap cargarEsquemasTarifarioProcedimientos(Connection con, String codigoContrato) throws BDException {
		HashMap mapa=new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio cargarEsquemasTarifarioProcedimientos");
			String cadena="SELECT " +
				"codigo," +
				"contrato," +
				"coalesce(grupo_servicio||'','') as gruposervicio," +
				"case when grupo_servicio is null then 'Todos' else getnombregruposervicio(grupo_servicio) end as nombregruposervicio," +
				"esquema_tarifario as esquematarifario," +
				"getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario," +
				"to_char(fecha_vigencia,'dd/mm/yyyy')  as fechavigencia," +
				"to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal," +
				"'BD' as tipoRegistro, " +
				"coalesce(centro_atencion, "+ConstantesBD.codigoNuncaValido+") as codcentroatencion, " +
				"case when centro_atencion is null then 'Todos' else getnomcentroatencion(centro_atencion) end as nombrecentroatencion " +
			"from " +
				"esq_tar_procedimiento_contrato " +
			"where " +
				"contrato=?";

			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setObject(1, codigoContrato);
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
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
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin cargarEsquemasTarifarioProcedimientos");
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema) 
	{
		String cadena="SELECT " +
							"codigo," +
							"contrato," +
							"coalesce(clase_inventario||'','') as claseinventario," +
							"case when clase_inventario is null then 'Todos' else getnomclaseinventario(clase_inventario) end as nombreclaseinventario," +
							"esquema_tarifario as esquematarifario," +
							"getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario," +
							"to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia," +
							"to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal," +
							"'BD' as tipoRegistro, " +
							"coalesce(centro_atencion, "+ConstantesBD.codigoNuncaValido+") as codcentroatencion, " +
							"case when centro_atencion is null then 'Todos' else getnomcentroatencion(centro_atencion) end as nombrecentroatencion " +
						"from " +
							"esq_tar_inventarios_contrato " +
						"where " +
							"codigo=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoEsquema);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema) 
	{
		String cadena="SELECT " +
						"codigo," +
						"contrato," +
						"coalesce(grupo_servicio||'','') as gruposervicio," +
						"case when grupo_servicio is null then 'Todos' else getnombregruposervicio(grupo_servicio) end as nombregruposervicio," +
						"esquema_tarifario as esquematarifario," +
						"getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario," +
						"to_char(fecha_vigencia,'dd/mm/yyyy')  as fechavigencia," +
						"to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal," +
						"'BD' as tipoRegistro, " +
						"coalesce(centro_atencion, "+ConstantesBD.codigoNuncaValido+") as codcentroatencion, " +
						"case when centro_atencion is null then 'Todos' else getnomcentroatencion(centro_atencion) end as nombrecentroatencion " +
						"from " +
						"esq_tar_procedimiento_contrato " +
						"where " +
						"codigo=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoEsquema);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @param esInventario
	 * @return
	 */
	public static boolean eliminarEsquema(Connection con, String codigoEsquema, boolean esInventario) 
	{
		String cadena="";
		if(esInventario)
			cadena="DELETE FROM esq_tar_inventarios_contrato where codigo=?";
		else 
			cadena="DELETE FROM esq_tar_procedimiento_contrato where codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoEsquema);
			ps.executeUpdate();
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean insertarEsquemasInventario(Connection con, HashMap vo) 
	{
		String cadena="INSERT INTO esq_tar_inventarios_contrato(codigo,contrato,clase_inventario,esquema_tarifario,fecha_vigencia,usuario_modifica,fecha_modifica,hora_modifica, centro_atencion) values (?,?,?,?,?,?,?,?,?)";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setObject(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_esqtarinvcont")+"");
			ps.setObject(2, vo.get("contrato"));
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(3, null);
			else
				ps.setObject(3, vo.get("clase"));
			ps.setObject(4, vo.get("esquematarifario"));
			ps.setObject(5, UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+""));
			ps.setObject(6, vo.get("usuario"));
			ps.setObject(7, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
			ps.setObject(8, UtilidadFecha.getHoraActual(con));
			if(Utilidades.convertirAEntero(vo.get("codcentroatencion")+"")<=0)
			{
				ps.setNull(9, Types.INTEGER);
			}
			else
			{
				ps.setInt(9, Utilidades.convertirAEntero(vo.get("codcentroatencion")+""));
			}
			return ps.executeUpdate()>0;

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean insertarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		String cadena="INSERT INTO esq_tar_procedimiento_contrato(codigo,contrato,grupo_servicio,esquema_tarifario,fecha_vigencia,usuario_modifica,fecha_modifica,hora_modifica, centro_atencion) values (?,?,?,?,?,?,?,?,?)";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setObject(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_esqtarprocont")+"");
			ps.setObject(2, vo.get("contrato"));
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
			{	
				ps.setObject(3, null);
			}	
			else
			{	
				ps.setObject(3, vo.get("grupo"));
			}	
			ps.setObject(4, vo.get("esquematarifario"));
			ps.setObject(5, UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+""));
			ps.setObject(6, vo.get("usuario"));
			ps.setObject(7, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
			ps.setObject(8, UtilidadFecha.getHoraActual(con));
			
			if(Utilidades.convertirAEntero(vo.get("codcentroatencion")+"")<=0)
			{
				ps.setNull(9, Types.INTEGER);
			}
			else
			{
				ps.setInt(9, Utilidades.convertirAEntero(vo.get("codcentroatencion")+""));
			}
			
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean modificarEsquemasInventario(Connection con, HashMap vo) 
	{
		String cadena="UPDATE esq_tar_inventarios_contrato SET clase_inventario=?,esquema_tarifario=?,fecha_vigencia=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?, centro_atencion=? WHERE codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(1, null);
			else
				ps.setObject(1, vo.get("clase"));
			ps.setObject(2, vo.get("esquematarifario"));
			ps.setObject(3, UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+""));
			ps.setObject(4, vo.get("usuario"));
			ps.setObject(5, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
			ps.setObject(6, UtilidadFecha.getHoraActual(con));
			
			if(Utilidades.convertirAEntero(vo.get("codcentroatencion")+"")<=0)
			{
				ps.setNull(7, Types.INTEGER);
			}
			else
			{
				ps.setInt(7, Utilidades.convertirAEntero(vo.get("codcentroatencion")+""));
			}
			
			ps.setObject(8,vo.get("codigo")+"");
			
			return ps.executeUpdate()>0;

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return false;

	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean modificarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		String cadena="UPDATE esq_tar_procedimiento_contrato SET grupo_servicio=?,esquema_tarifario=?,fecha_vigencia=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?, centro_atencion=? WHERE codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(1, null);
			else
				ps.setObject(1, vo.get("grupo"));
			ps.setObject(2, vo.get("esquematarifario"));
			ps.setObject(3, UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+""));
			ps.setObject(4, vo.get("usuario"));
			ps.setObject(5, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
			ps.setObject(6, UtilidadFecha.getHoraActual(con));
			
			if(Utilidades.convertirAEntero(vo.get("codcentroatencion")+"")<=0)
			{
				ps.setNull(7, Types.INTEGER);
			}
			else
			{
				ps.setInt(7, Utilidades.convertirAEntero(vo.get("codcentroatencion")+""));
			}
			
			ps.setObject(8,vo.get("codigo")+"");
			return ps.executeUpdate()>0;

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerEsquemasTarifariosInventariosVigentes(Connection con, int ingreso,String fromSTR) 
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			String consulta="SELECT codigo,contrato,coalesce(clase_inventario||'','') as claseinventario,case when clase_inventario is null then 'Todos' else getnomclaseinventario(clase_inventario) end as nombreclaseinventario,esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal  "+fromSTR+" where contrato in (select contrato from sub_cuentas where ingreso=? and nro_prioridad=1) group by clase_inventario,contrato,codigo,esquema_tarifario,fecha_vigencia";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, ingreso);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerEsquemasTarifariosProcedimientosVigentes(Connection con, int ingreso,String fromSTR) 
	{
		String consulta="SELECT codigo,contrato,coalesce(grupo_servicio||'','') as gruposervicio,case when grupo_servicio is null then 'Todos' else getnombregruposervicio(grupo_servicio) end as nombregruposervicio,esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,to_char(fecha_vigencia,'dd/mm/yyyy')  as fechavigencia,to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal,'BD' as tipoRegistro  "+fromSTR+" where contrato in (select contrato from sub_cuentas where ingreso=? and nro_prioridad=1) group by grupo_servicio,contrato,codigo,esquema_tarifario,fecha_vigencia";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, ingreso);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerEsquemasTarifariosInventariosVigentesFecha(Connection con, String contrato, String fromSTR) 
	{
		String consulta="SELECT codigo,contrato,coalesce(clase_inventario||'','') as claseinventario,case when clase_inventario is null then 'Todos' else getnomclaseinventario(clase_inventario) end as nombreclaseinventario,esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal "+fromSTR+" where contrato=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			logger.info("-->"+consulta+"\n"+"\n"+contrato+"\n-->");

			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, contrato);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerEsquemasTarifariosProcedimientosVigentesFecha(Connection con, String contrato, String fromSTR) 
	{
		String consulta="SELECT codigo,contrato,coalesce(grupo_servicio||'','') as gruposervicio,case when grupo_servicio is null then 'Todos' else getnombregruposervicio(grupo_servicio) end as nombregruposervicio,esquema_tarifario as esquematarifario,getnombreesquematarifario(esquema_tarifario) as nombreesquematarifario,to_char(fecha_vigencia,'dd/mm/yyyy')  as fechavigencia,to_char(fecha_vigencia,'yyyy-mm-dd') as fechavigenciaoriginal,'BD' as tipoRegistro  "+fromSTR+" where contrato=?";
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			logger.info("-->"+consulta+"\n"+"\n"+contrato);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, contrato);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

}
