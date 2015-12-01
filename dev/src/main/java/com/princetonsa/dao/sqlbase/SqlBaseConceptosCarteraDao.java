/*
 * Creado   24/05/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;


import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Clase para manejar la comunicacion con la fuente de datos,
 * para los conceptos cartera.
 *
 * @version 1.0, 24/05/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseConceptosCarteraDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConceptosCarteraDao.class);
	
	
	
//CADENAS PARA LA FUNCIONALIDAD CONCEPTO GLOSAS//
	/**
	 * para consultar conceptos de pago cartera.
	 */	
	private static final String consultaConceptosGlosasStr = "SELECT distinct cg.codigo AS codigoConcepto, " +
																"cg.descripcion AS descripcion, " +
																"cg.cuenta_debito AS cuentadebito, " +
																"cg.cuenta_credito AS cuentacredito, " +
																"cg.tipo_concepto AS tipoConcepto, " +
																"(SELECT cg1.descripcion FROM glosas.conceptos_generales cg1 WHERE cg1.codigo = cg.concepto_general AND cg1.institucion = cg.institucion ) AS conceptoGeneral, " +
																"(SELECT cg1.descripcion FROM glosas.conceptos_especificos cg1 WHERE cg1.codigo = cg.concepto_especifico AND cg1.institucion = cg.institucion ) AS conceptoEspecifico, " +
																"cg.concepto_general AS codConceptGral,  " +
																"cg.concepto_especifico AS codConceptEspe, " +
																"coalesce(cc.cuenta_contable, '') AS  numeroCuentaContable,  " +
																"coalesce(cc.anio_vigencia, '') AS anioCuenta, " +
																"coalesce(cc1.cuenta_contable,'') AS  numeroCuentaContableCredito,  " +
																"coalesce(cc1.anio_vigencia, '') AS anioCuentaCredito " +
																"FROM glosas.concepto_glosas cg  " +
																"LEFT OUTER JOIN cuentas_contables cc ON (cg.cuenta_debito=cc.codigo) " +
																"LEFT OUTER JOIN cuentas_contables cc1 ON (cg.cuenta_credito=cc1.codigo) " +
																"WHERE cg.institucion=? ";
																
														

	/**
	 * para consultar si un concepto de Glosa, tiene
	 * relacion con Glosas.
	 */
	private static final String existeRelacionConGlosas = "SELECT COUNT(*) AS numFilas FROM glosas WHERE concep_glosa = ? and institucion=?";
	
	/**
	 * Cadena para ingresar los registro a la tabla concepto_glosas
	 */
	private static final String ingresarConceptoGlosa="INSERT INTO glosas.concepto_glosas (" +
			"codigo," +					//1
			"institucion," +			//2
			"descripcion," +			//3
			"cuenta_debito," +			//4
			"cuenta_credito," +			//5
			"tipo_concepto," +			//6
			"concepto_general," +		//7
			"concepto_especifico," +	//8
			"fecha_modifica," +			//9
			"hora_modifica," +			//10
			"usuario_modifica) " +		//11
			"VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	private static final String modificarConceptoGlosa="UPDATE glosas.concepto_glosas SET " +
			"codigo= ?, " +
			"descripcion=?, " +
			"cuenta_debito=?, " +
			"cuenta_credito=?, " +
			"tipo_concepto=?, " +
			"concepto_general=?, " +
			"concepto_especifico=?, " +
			"fecha_modifica=?," +
			"hora_modifica=?," +
			"usuario_modifica=? " +
			"where codigo=? " +
			"and  institucion=?";
	
	private static final String eliminarConceptoGlosa="DELETE FROM glosas.concepto_glosas where codigo=? and  institucion=?";
//FIN CADENAS PARA LA FUNCIONALIDAD CONCEPTO GLOSAS//
//	CADENAS PARA LA FUNCIONALIDAD CONCEPTO RESPUESTAS GLOSAS//
	/**
	 * para consultar conceptos de pago cartera.
	 */	
	private static final String consultaConceptosRespuestaGlosasStr = "SELECT" +
																" codigo AS codigoRespuestaConcepto," +
																" descripcion AS descripcion" +
																" FROM concepto_resp_glosas" +
																" WHERE institucion=?";
	/**
	 * para consultar si un concepto de Glosa, tiene
	 * relacion con Glosas.
	 */
	private static final String existeRelacionRespuestaConGlosas = "SELECT COUNT(*) AS numFilas FROM glosas WHERE concep_resp_glosa = ? and institucion=?";
	
	/**
	 * Cadena para ingresar los registro a la tabla concepto_glosas
	 */
	private static final String ingresarConceptoRespuestaGlosa="INSERT INTO concepto_resp_glosas (codigo,institucion,descripcion) VALUES (?,?,?)";

	private static final String modificarConceptoRespuestaGlosa="UPDATE concepto_resp_glosas SET codigo= ?, descripcion=? where codigo=? and  institucion=?";
	
	private static final String eliminarConceptoRespuestaGlosa="DELETE FROM concepto_resp_glosas where codigo=? and  institucion=?";
//FIN CADENAS PARA LA FUNCIONALIDAD CONCEPTO RESPUESTAS GLOSAS//	
	
	
	
	//CONCEPTOS PAGO CARTERA *********************************************************
	/**
	 * para insertar un concepto de pago de cartera
	 */
	private static final String insertarConceptoPagoCarteraStr = "  INSERT INTO concepto_pagos " +
																									"(codigo," +
																									"institucion," +
																									"tipo," +
																									"cuenta_contable," +
																									"porcentaje," +
																									"descripcion) " +
																									"VALUES (?,?,?,?,?,?)";
	
	/**
	 * para modificar un concepto de pago de cartera
	 */
	private static final String modificarConceptoPagoCarteraStr = "UPDATE concepto_pagos SET ";
	
	/**
	 * para eliminar un concepto de pago de cartera.	 
	 */
	private static final String eliminarConceptoPagoCarteraStr = "DELETE FROM concepto_pagos WHERE codigo=? AND institucion=?";
	
	/**
	 * para consultar conceptos de pago cartera.
	 */	
	private static final String consultaConceptosPagoCarteraStr = "SELECT " +
																				"codigo AS codigoConcepto," +
																				"tipo AS codigoTipo," +
																				"cuenta_contable AS codigoCuenta," +
																				"porcentaje AS porcentaje," +
																				"descripcion AS descripcion," +
																				"getdesctipoconceptopago(tipo) AS descripcionTipo " +
																				"FROM concepto_pagos " +
																				"WHERE institucion = ? ";

	/**
	 * filtro de la consulta
	 */
	public static final String consultaUno=" ORDER BY descripcion asc";
	
	/**
	 * para consultar si un concepto de pago cartera, tiene
	 * relacion con aplicacion de pagos.
	 */
	private static final String existeRelacionAplicacionPagosStr = "SELECT COUNT(*) AS numFilas FROM conceptos_apli_pagos WHERE concepto_pagos = ? AND institucion = ?";
	
	//FIN CONCEPTOS PAGO CARTERA ***************************************************************
	
	//CONCEPTOS AJUSTES ****************************************************
	
	/**
	 * para consultar conceptos de ajustes cartera.
	 */	
	private static final String consultaConceptosAjustesCarteraStr = "SELECT cac.codigo AS codigoConcepto,cac.naturaleza AS codigoNaturaleza,cac.cuenta_contable AS codigoCuenta,coalesce(cc.descripcion,'') as desccuentacontable,cac.descripcion AS descripcion,tipo_cartera AS tipo_cartera,tc.nombre as nomtipocartera FROM concepto_ajustes_cartera  cac  left outer join cuentas_contables cc on(cac.cuenta_contable=cc.codigo) inner join tipo_cartera tc on(cac.tipo_cartera=tc.codigo) WHERE cac.institucion =  ? ";
	
	/**
	 * para insertar un concepto de ajustes de cartera
	 */
	private static final String insertarConceptoAjustesCarteraStr = "  INSERT INTO concepto_ajustes_cartera " +
																									"(codigo," +
																									"institucion," +
																									"naturaleza," +
																									"descripcion,"+
																									"cuenta_contable," +
																									"tipo_cartera) " +
																									"VALUES (?,?,?,?,?,?)";
	/**
	 * para modificar un concepto de ajustes de cartera
	 */
	private static final String modificarConceptoAjustesCarteraStr = "UPDATE concepto_ajustes_cartera SET ";
	
	/**
	 * para eliminar un concepto de ajustes de cartera.	 
	 */
	private static final String eliminarConceptoAjustesCarteraStr = "DELETE FROM concepto_ajustes_cartera WHERE codigo=? AND institucion = ?";
	
	/**
	 * para consultar si un concepto de ajustes cartera, tiene
	 * relacion con ajustes y/o conceptos castigo cartera.
	 */
	private static final String existeRelacionAplicacionAjustes01Str = " SELECT count(1) as num_filas from ajustes_empresa where concepto_ajuste=? AND institucion = ?" ;
	private static final String existeRelacionAplicacionAjustes02Str = " select count(1) as num_filas from concepto_castigo_cartera where ajuste_credito=? AND institucion = ?" ;
	
	
	//FIN CONCEPTOS AJUSTES ************************************************
	
	//	CONCEPTO CASTIGO//
	/**
	 * para consultar conceptos de castigo cartera.
	 */	
	private static final String consultaConceptoCastigoStr = "SELECT" +
																" codigo AS codigoConcepto," +
																" descripcion AS descripcion," +
																" ajuste_credito AS ajustecredito" +
																" FROM concepto_castigo_cartera" +
																" WHERE institucion=? "; 
															
	/**
	 * Consultar si un concepto de castigo, tiene relacion con castigo.
	 */
	private static final String existeRelacionConCastigo = "SELECT COUNT(*) AS numFilas FROM ajustes_empresa WHERE concepto_castigo_cartera= ? and institucion=?";
	
	/**
	 * Insertar los registro a la tabla concepto_castigo_cartera
	 */
	private static final String ingresarConceptoCastigo="INSERT INTO concepto_castigo_cartera (codigo,institucion,descripcion,ajuste_credito) VALUES (?,?,?,?)";

	/**
	 * Modificar un concepto de castigo.
	 */
	private static final String modificarConceptoCastigo="UPDATE concepto_castigo_cartera SET ";
	
	/**
	 * Eliminar un concepto de castigo de cartera.	 
	 */
	private static final String eliminarConceptoCastigoStr = "DELETE FROM concepto_castigo_cartera WHERE codigo=?";
	
	//FIN CONCEPTO CASTIGO//

	
	/**
	 * Indices de la consulta GLosas
	 */
	private static String [] indicesGlosas ={"codigoglosa_","glosasis_","fechareg_","conveniog_","contratog_","glosaent_","fechanot_","valorg_","obser_","codigoconvenio_"};
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla conceptos_audi_glosas o no para validar si se puede o no eliminar 
	 */
	private static String consultaConceptosAudiGlosas ="SELECT count(codigo) AS codigo FROM conceptos_audi_glosas where concepto_glosa = ?";
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla conceptos_det_audi_glosas o no para validar si se puede o no eliminar 
	 */
	private static String consultaConceptosDetAudiGlosas="SELECT count(codigo) AS codigo FROM conceptos_det_audi_glosas where concepto_glosa = ?";
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla conceptos_aso_audi_glosas o no para validar si se puede o no eliminar 
	 */
	private static String consultaConceptosAsoAudiGlosas="SELECT count(codigo) AS codigo FROM conceptos_aso_audi_glosas where concepto_glosa = ?";
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla det_fact_ext_resp_glosa o no para validar si se puede o no eliminar 
	 */
	private static String consultaDetFactExtRespGlosa="SELECT count(codigo) AS codigo FROM det_fact_ext_resp_glosa where concepto_glosa = ?";
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla det_fact_resp_glosa o no para validar si se puede o no eliminar 
	 */
	private static String consultaDetFactRespGlosa="SELECT count(codigo) AS codigo FROM det_fact_resp_glosa where concepto_glosa = ?";
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla asocios_resp_glosa o no para validar si se puede o no eliminar 
	 */
	private static String consultaAsociosRespGlosa="SELECT count(codigo) AS codigo FROM asocios_resp_glosa where concepto_glosa = ?";	
	
	
	/**
	 * Metodo que consulta si un concepto tipo glosa ha sido utilizado o no para validar si se puede o no eliminar
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public static boolean consultarConceptosGlosa (Connection con, String codConcepto)
	{
		PreparedStatementDecorator ps;
		int resultado= 0;
		ResultSetDecorator rs;
		
		try {	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConceptosAudiGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, codConcepto);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				resultado = rs.getInt("codigo");
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e){
			logger.info("\n\nERROR. CONCEPTOS AUDITORIA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		
		if(resultado == 0){
			try {	
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConceptosDetAudiGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, codConcepto);
				
				rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next()){
					resultado = rs.getInt("codigo");
				}
				ps.close();
				rs.close();
			}
			catch (SQLException e){
				logger.info("\n\nERROR. CONCEPTOS DETTALLE AUDITORIA GLOSA------>>>>>>"+e);
				e.printStackTrace();
			}
			if(resultado == 0){
				try {	
					ps =  new PreparedStatementDecorator(con.prepareStatement(consultaConceptosAsoAudiGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					ps.setString(1, codConcepto);
					
					rs=new ResultSetDecorator(ps.executeQuery());
					
					if(rs.next()){
						resultado = rs.getInt("codigo");
					}
					ps.close();
					rs.close();
				}
				catch (SQLException e){
					logger.info("\n\nERROR. CONCEPTOS ASOCIOS AUDITORIA GLOSA------>>>>>>"+e);
					e.printStackTrace();
				}		
				if(resultado == 0){
					try {	
						ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetFactExtRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setString(1, codConcepto);
						
						rs=new ResultSetDecorator(ps.executeQuery());
						
						if(rs.next()){
							resultado = rs.getInt("codigo");
						}
						
						ps.close();
						rs.close();
					}
					catch (SQLException e){
						logger.info("\n\nERROR. CONCEPTOS DETALLE FACTURA EXTERNA RESPUESTA GLOSA------>>>>>>"+e);
						e.printStackTrace();
					}
					if(resultado == 0){
						try {	
							ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetFactRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
							ps.setString(1, codConcepto);
							
							rs=new ResultSetDecorator(ps.executeQuery());
							
							if(rs.next()){
								resultado = rs.getInt("codigo");
							}
							
							ps.close();
							rs.close();
						}
						catch (SQLException e){
							logger.info("\n\nERROR. CONCEPTOS DETALLE FACTURA RESPUESTA GLOSA------>>>>>>"+e);
							e.printStackTrace();
						}
						if(resultado == 0){
							try {	
								ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAsociosRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								
								ps.setString(1, codConcepto);
								
								rs=new ResultSetDecorator(ps.executeQuery());
								
								if(rs.next()){
									resultado = rs.getInt("codigo");
								}
								
								ps.close();
								rs.close();
							}
							catch (SQLException e){
								logger.info("\n\nERROR. CONCEPTOS ASOCIOS RESPUESTA GLOSA------>>>>>>"+e);
								e.printStackTrace();
							}
						}
					}
				}				
			}
		}	
		
		if(resultado > 0)
			return false;
			
		return true;
	}
	
	
//	METODOS DE PAGOS 
	
	/**
	 * metodo para insertar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripción del concepto
	 * @param codTipo int, código del tipo de concepto
	 * @param codCuenta double, código de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#insertarConcepto(Connection, String,String,int,int,double)
	 */
	public static boolean insertarConceptosPago (Connection con, 
															String codigo, 
															int institucion,
															String descripcion, 
															int codTipo, 
															double codCuenta, 
															double porcentaje)
	{
		int resp = 0;
		
		try
		  {
			
			  if (con == null || con.isClosed()) 
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
			  PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarConceptoPagoCarteraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			  ps.setString(1,codigo);
			  ps.setInt(2,institucion);
			  ps.setInt(3,codTipo);
			  if(codCuenta<=0)
				  ps.setObject(4,null);
			  else
				  ps.setDouble(4,codCuenta);
			  ps.setDouble(5,porcentaje);
			  ps.setString(6,descripcion);
			  resp=ps.executeUpdate();
			  ps.close();
			  if(resp < 0)
				  return  false;
			  else
				  return  true;
			  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosCarteraDao "+e.toString() );
			return false;
		}		
	}
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @return ResultSet
	 * @see com.princetonsa.dao.ConceptosCarteraDao#consultaConceptos(Connection, String,String,int,int,double)
	 */
	public static ResultSetDecorator consultaConceptosPago (Connection con,int codInstitucion)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
			ps=  new PreparedStatementDecorator(con.prepareStatement(consultaConceptosPagoCarteraStr+consultaUno,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs; 
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}
	
	/**
	 * metodo para modificar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto modificado
	 * @param codigoOld String, código original.
	 * @param descripcion String, descripción del concepto
	 * @param codTipo int, código del tipo de concepto
	 * @param codCuenta double, código de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#modicarConceptos(Connection, String,String,int,int,double)
	 */
	public static boolean modicarConceptosPago (Connection con,
															String codigo,
															String codigoOld,
															String descripcion, 
															int codTipo, 
															double codCuenta, 
															double porcentaje,
															int institucion)
	{
		int resp = 0;
		String modificarStr = modificarConceptoPagoCarteraStr;
		boolean esPrimero=true;
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();				
			}
					
			if(!descripcion.equals(""))
			{
				modificarStr+=" descripcion = '"+descripcion+"'";
				esPrimero=false;
			}
			if(!codigo.equals(""))
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;				
				modificarStr+=" codigo = '"+codigo+"'";	  
			}
			if(codTipo != -1)
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;				
				modificarStr+=" tipo = '"+codTipo+"'";				
			}
			if(codCuenta != -1)
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;
				if(codCuenta<=0)
					modificarStr+=" cuenta_contable = null";
				else
					modificarStr+=" cuenta_contable = '"+codCuenta+"'";				
			}
			if(porcentaje != -1)
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;
				modificarStr+=" porcentaje = '"+porcentaje+"'";			   
			}			
			
			modificarStr+=" WHERE codigo = '"+codigoOld+"' AND institucion = "+institucion+"";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			resp=ps.executeUpdate();
			ps.close();
			if(resp>0)
				return true;
			else
				return false;
		 }
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return false;
		}		
	}
	
	/**
	 * metodo para eliminar conceptos de pago cartera
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, código del conpto
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#eliminarConceptos(Connection, String)
	 */
	public static boolean eliminarConceptosPago (Connection con, String codigo, int institucion)
	{
		int resp=0;
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoPagoCarteraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);	   
			ps.setInt(2,institucion);
			resp = ps.executeUpdate();
			ps.close();
			if(resp>0)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la eliminacion: SqlBaseConceptosCarteraDao "+e.toString() );
			return false;
		}		
	}
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de pago cartera que tienen relación con
	 * aplicación de pagos.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigoConcepto String, código del concepto
	 * @param institucion int, código de la institución
	 * @return ResultSet
	 * @see com.princetonsa.dao.ConceptosCarteraDao#consultaConceptos(Connection,String)
	 */
	public static ResultSetDecorator consultaRelacionConceptosPago (Connection con, String codigoConcepto,int institucion)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionAplicacionPagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setInt(2,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaRelacionConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}
	
	/**
	 * metodo para modificar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripción del concepto
	 * @param codTipo int, código del tipo de concepto
	 * @param codCuenta double, código de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#modicarConceptos(Connection, String,String,int,int,double)
	 */
	public static ResultSetDecorator busquedaAvanzadaPago (Connection con,
																String codigo, 
																String descripcion, 
																int codTipo, 
																double codCuenta, 
																double porcentaje,
																int codInstitucion)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
			
			String avanzadaStr = "";
						
			if(!codigo.equals(""))
			{
				avanzadaStr+=" AND codigo = '"+codigo+"'";				
			}
			if(!descripcion.equals(""))
			{	
			   avanzadaStr+=" AND upper(descripcion) like upper('%"+descripcion+"%')";					
			}
			if(codTipo != -1)
			{	
			   avanzadaStr+=" AND tipo = "+codTipo+"";					
			}
			if(codCuenta >0)
			{	
				avanzadaStr+=" AND cuenta_contable = "+codCuenta+"";					
			}
			if(porcentaje != 0)
			{	
			   avanzadaStr+=" AND porcentaje = "+porcentaje+"";					
			}
			String consulta = consultaConceptosPagoCarteraStr + avanzadaStr + consultaUno;
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
		   logger.warn(e+"Error en busquedaAvanzadaPago : SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}		
	}

	
//	FIN METODOS DE PAGOS

//	METODOS AJUSTES
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de ajustes cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @return ResultSet
	 * @see com.princetonsa.dao.ConceptosCarteraDao#consultaConceptosAjustes(Connection, int)
	 */
	public static ResultSetDecorator consultaConceptosAjustes (Connection con,int codInstitucion)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
			String consulta = consultaConceptosAjustesCarteraStr + " ORDER BY cac.descripcion asc";
			logger.info("query consulta conceptos certera: "+consulta);
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement sin recorrer resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaConceptosAjustes: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}
	
	/**
	 * metodo para insertar conceptos de ajustes cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripción del concepto	 
	 * @param codCuenta double, código de la cuenta
	 * @param naturaleza int, código de la naturaleza.
	 * @param tipoCartera @todo
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#insertarConceptosAjustes(Connection, String,int,String,double,int, int)
	 */
	public static boolean insertarConceptosAjustes (Connection con, 
															String codigo, 
															int institucion,
															String descripcion,															 
															double codCuenta, 
															int naturaleza,
															int tipoCartera)
	{
		int resp = 0;
		
		try
		  {
			  if (con == null || con.isClosed()) 
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
			  PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarConceptoAjustesCarteraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			  
			  //logger.info("CODIGO--->"+codigo+" lengght-->"+codigo.length());
			  
			  ps.setString(1,codigo);
			  ps.setInt(2,institucion);
			  ps.setInt(3,naturaleza);
			  ps.setString(4,descripcion);
			  if(codCuenta<=0)
				  ps.setObject(5,null);
			  else
				  ps.setDouble(5,codCuenta);

			  ps.setInt(6,tipoCartera);
			  resp=ps.executeUpdate();
			  ps.close();
			  if(resp < 0)
				  return  false;
			  else
				  return  true;
			  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosCarteraDao "+e.toString() );
			e.printStackTrace();
			return false;
		}		
	}
	
	/**
	 * metodo para modificar conceptos de ajustes cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto modificado
	 * @param codigoOld String, código original.
	 * @param descripcion String, descripción del concepto	 
	 * @param codCuenta double, código de la cuenta
	 * @param naturaleza int, código de la naturaleza
	 * @param institucion int, códigop de la institución
	 * @param tipoCartera @todo
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#modicarConceptosAjustes(Connection, String,String,double,int)
	 */
	public static boolean modicarConceptosAjustes(Connection con,
															String codigo,
															String codigoOld,
															String descripcion,															
															double codCuenta, 
															int naturaleza,
															int institucion, int tipoCartera)
	{
		int resp = 0;
		String modificarStr = modificarConceptoAjustesCarteraStr;
		boolean esPrimero=true;
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();				
			}
					
			if(!descripcion.equals(""))
			{
				modificarStr+=" descripcion = '"+descripcion+"'";
				esPrimero=false;
			}
			if(!codigo.equals(""))
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;				
				modificarStr+=" codigo = '"+codigo+"'";	  
			}
			if(codCuenta != -1)
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;
				if(codCuenta<=0)
					modificarStr+=" cuenta_contable = null";
				else
					modificarStr+=" cuenta_contable = '"+codCuenta+"'";				
			}
			if(naturaleza != -1)
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;
				modificarStr+=" naturaleza = '"+naturaleza+"'";			   
			}
			if(tipoCartera!=0)
			{
				if(!esPrimero)
					modificarStr+=" , ";
				esPrimero=false;
				modificarStr+=" tipo_cartera = '"+tipoCartera+"'";			   
			}
			
			modificarStr+=" WHERE codigo = '"+codigoOld+"' AND institucion = "+institucion+"";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			resp=ps.executeUpdate();
			ps.close();
			if(resp>0)
				return true;
			else
				return false;
		 }
		catch(SQLException e)
		{
			logger.warn(e+"Error en la modicarConceptosAjustes: SqlBaseConceptosCarteraDao "+e.toString() );
		   return false;
		}		
	}
	
	/**
	 * metodo para eliminar conceptos de ajustes cartera
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, código del conpto
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#eliminarConceptosAjustes(Connection, String)
	 */
	public static boolean eliminarConceptosAjustes (Connection con, String codigo, int institucion)
	{
		int resp=0;
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoAjustesCarteraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);	
			ps.setInt(2,institucion);
			resp = ps.executeUpdate();
			ps.close();
			if(resp>0)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la eliminacion Ajustes: SqlBaseConceptosCarteraDao "+e.toString() );
			return false;
		}		
	}
	
	/**
	 * metodo para modificar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripción del concepto	 	
	 * @param codCuenta double, código de la cuenta
	 * @param naturaleza int, código de la naturaleza
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.ConceptosCarteraDao#busquedaAvanzadaAjustes(Connection, String,String,double,int)
	 */
	public static ResultSetDecorator busquedaAvanzadaAjustes (Connection con,
																String codigo, 
																String descripcion,																
																double codCuenta,																
																int naturaleza,
																int codInstitucion,int tipoCartera)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
			
			String avanzadaStr = "";
						
			if(!codigo.equals(""))
			{
				avanzadaStr+=" AND cac.codigo = '"+codigo+"'";				
			}
			if(!descripcion.equals(""))
			{	
			   avanzadaStr+=" AND upper(cac.descripcion) like upper('%"+descripcion+"%')";					
			}			
			if(codCuenta > 0)
			{	
				avanzadaStr+=" AND cac.cuenta_contable = "+codCuenta+"";					
			}
			if(naturaleza != -1)
			{	
			   avanzadaStr+=" AND cac.naturaleza = "+naturaleza+"";					
			}
			if(tipoCartera != -2)
			{
				avanzadaStr+=" AND cac.tipo_cartera="+tipoCartera;
			}
			String consulta = consultaConceptosAjustesCarteraStr + avanzadaStr + " ORDER BY cac.descripcion asc" ;
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
		   logger.warn(e+"Error en busquedaAvanzadaPago : SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}		
	}
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de ajustes cartera que tienen relación con
	 * ajustes y/o conceptos castigo cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigoConcepto String, código del concepto
	 * @param institucion int, código de la institución
	 * @return ResultSet
	 * @see com.princetonsa.dao.ConceptosCarteraDao#consultaRelacionConceptosAjustes(Connection,String,int)
	 */
	public static int consultaRelacionConceptosAjustes (Connection con, String codigoConcepto,int institucion)
	{
		int numFilas = 0;
		try
		{
			PreparedStatementDecorator ps = null;
			
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionAplicacionAjustes01Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setInt(2,institucion);
			
			
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				numFilas = rs.getInt("num_filas");
			}
			ps.close();
			rs.close();
			
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionAplicacionAjustes02Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setInt(2,institucion);
			
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				numFilas = rs.getInt("num_filas");
			}
			ps.close();
			rs.close();
			
			return numFilas;
		}
		catch(SQLException e)
		{
		   logger.warn(e+"Error en la consultaRelacionConceptosAjustes: SqlBaseConceptosCarteraDao "+e.toString() );
		   numFilas = 0;
		   return numFilas;
		}
	}

	
//	FIN METODOS AJUSTES
		
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ResultSetDecorator consultaConceptosGlosas(Connection con, int codigoInstitucionInt) 
	{
		String query=consultaConceptosGlosasStr+" ORDER BY cg.descripcion asc";
		
		try
		{
		
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucionInt);
			//ps.setInt(2,codigoInstitucionInt);
			//ps.setInt(3,codigoInstitucionInt);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close(); no se puede cerrar hasta que se haya recorrido el resultset
			
			return rs;
		}		
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
			return null;
		}
		
	}

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ResultSetDecorator consultaRelacionConceptosGlosas(Connection con, String codigo, int codigoInstitucionInt) 
	{
		try
		{
			PreparedStatementDecorator ps = null;
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionConGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,codigoInstitucionInt);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			//ps.close(); no se puede cerrar hasta que se devuelva el resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaRelacionConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}

	/**
	 * Método encargado de insertar un concepto glosa
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param cuentaDebito
	 * @param cuentaCredito
	 * @param conceptoGeneral
	 * @param conceptoEspecifico
	 * @param usuarioModifica
	 * @return
	 */
	public static int insertarConceptoGlosa(
			Connection con, 
			String codigoConcepto, 
			int codigoInstitucionInt, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral, 
			String conceptoEspecifico,
			String usuarioModifica)
	{
		 
		try
		  {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarConceptoGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("===> Aquí está la cadena de inserción: "+ingresarConceptoGlosa);


			logger.info("===> codigoConcepto: "+codigoConcepto);
			logger.info("===> codigoInstitucionInt: "+codigoInstitucionInt);
			logger.info("===> descripcion: "+descripcion);
			logger.info("===> cuentaDebito: "+cuentaDebito);
			logger.info("===> cuentaDebito: "+cuentaDebito);
			logger.info("===> cuentaCredito: "+cuentaCredito);
			logger.info("===> cuentaCredito: "+cuentaCredito);
			logger.info("===> tipoConcepto: "+tipoConcepto);
			logger.info("===> conceptoGeneral: "+conceptoGeneral);
			logger.info("===> conceptoEspecifico: "+conceptoEspecifico);
			logger.info("===> fecha: "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		logger.info("===> hora: "+UtilidadFecha.getHoraActual(con));
	  		logger.info("===> usuarioModifica: "+usuarioModifica);
			
			ps.setString(1,codigoConcepto);
			ps.setInt(2,codigoInstitucionInt);
			ps.setString(3,descripcion);

			if(cuentaDebito==ConstantesBD.codigoNuncaValido)
				ps.setObject(4,null);
			else 
				ps.setDouble(4,cuentaDebito);

				
			if(cuentaCredito==ConstantesBD.codigoNuncaValido) 
				ps.setObject(5,null);
			else 
				ps.setDouble(5,cuentaCredito);
			
			ps.setString(6, tipoConcepto);


			if((Utilidades.convertirAEntero(conceptoGeneral) == ConstantesBD.codigoNuncaValido) || (conceptoGeneral == null) ) 
				ps.setObject(7,null);
			else 
				ps.setInt(7, Utilidades.convertirAEntero(conceptoGeneral));
			

			if((Utilidades.convertirAEntero(conceptoEspecifico) == ConstantesBD.codigoNuncaValido) || (conceptoEspecifico == null) ) 
				ps.setObject(8,null);
			else 
				ps.setInt(8, Utilidades.convertirAEntero(conceptoEspecifico));
			
			
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(10, UtilidadFecha.getHoraActual(con));
	  		ps.setString(11, usuarioModifica);
	  		int resp = ps.executeUpdate();
	  		ps.close();
			return resp;
		  }

		catch (SQLException e) {
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosPagoCarteraDao "+e.toString() );
			return -1;
		}				
	}

	/**
	 * Método encargado de modificar un concepto glosa
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param cuentaDebito
	 * @param cuentaCredito
	 * @param tipoConcepto
	 * @param conceptoGeneral
	 * @param conceptoEspecifico
	 * @param usuarioModifica
	 * @return
	 */
	public static int modificarConceptoGlosas(
			Connection con, 
			String codigoConceptoAntiguo, 
			int codigoInstitucionInt, 
			String codigoConcepto, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral,
			String conceptoEspecifico,
			String usuarioModifica) 
	{
		 try
		  {
			logger.info("===> y Ahora Vamos a ver si modifica !!!");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarConceptoGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setString(2,descripcion);
			if(cuentaDebito==ConstantesBD.codigoNuncaValido)
				ps.setObject(3,null);
			else
				ps.setDouble(3,cuentaDebito);
			logger.info("===> cuentaDebito: "+cuentaDebito);
			if(cuentaCredito==ConstantesBD.codigoNuncaValido)
				ps.setObject(4,null);
			else			
				ps.setDouble(4,cuentaCredito);
			logger.info("===> cuentaCredito: "+cuentaCredito);
			ps.setString(5, tipoConcepto);
			logger.info("===> tipoConcepto: "+tipoConcepto);
			ps.setString(6, conceptoGeneral);
			logger.info("===> conceptoGeneral: "+conceptoGeneral);
			ps.setString(7, conceptoEspecifico);
			logger.info("===> conceptoEspecifico: "+conceptoEspecifico);
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			logger.info("===> fecha: "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(9, UtilidadFecha.getHoraActual(con));
	  		logger.info("===> hora: "+UtilidadFecha.getHoraActual(con));
	  		ps.setString(10, usuarioModifica);
	  		logger.info("===> usuarioModifica: "+usuarioModifica);
			ps.setString(11,codigoConceptoAntiguo);
	  		logger.info("===> codigo concepto antiguo: "+codigoConceptoAntiguo);
			ps.setInt(12,codigoInstitucionInt);
			int resp = ps.executeUpdate();
			ps.close();
			return resp;
			  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosPagoCarteraDao "+e.toString() );
			return -1;
		}			
	}
	
	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static int eliminarConceptoGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt) 
	{
		try
		  {
			logger.info("===> Voy a eliminar por éste codigo: "+codigoConceptoAntiguo);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConceptoAntiguo);
			ps.setInt(2,codigoInstitucionInt);
			int temp =ps.executeUpdate();
			ps.close();
			return temp;
			  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosPagoCarteraDao "+e.toString() );
			return -1;
		}			
	}

	/**
	 * Metodo encargado de realizar la búsqueda avanzada a la tabla de concepto_glosas
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param cuentaDebito
	 * @param cuentaCredito
	 * @param tipoConcepto
	 * @param conceptoGeneral
	 * @param conceptoEspecifico
	 * @return
	 */
	public static ResultSetDecorator busquedaAvanzadaGlosas(
			Connection con, 
			String codigo, 
			int codigoInstitucionInt, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral,
			String conceptoEspecifico) 
	{
		try
		{
			PreparedStatementDecorator ps = null;
			
			String avanzadaStr = "";
			if(!codigo.equals(""))
			{
				avanzadaStr+=" AND cg.codigo = '"+codigo+"'";				
			}
			if(!descripcion.equals(""))
			{	
			   avanzadaStr+=" AND upper(cg.descripcion) like upper('%"+descripcion+"%')";					
			}
			if(cuentaDebito != -1)
			{	
			   avanzadaStr+=" AND cg.cuenta_debito = "+cuentaDebito+"";					
			}
			if(cuentaCredito != -1)
			{	
				avanzadaStr+=" AND cg.cuenta_credito = "+cuentaCredito+"";
			}
			if(!tipoConcepto.equals(""))
			{
				avanzadaStr+=" AND cg.tipo_concepto = '"+tipoConcepto+"'";
			}
			if(!conceptoGeneral.equals(""))
			{
				avanzadaStr+=" AND cg.concepto_general = '"+conceptoGeneral+"'";
			}
			if(!conceptoEspecifico.equals(""))
			{
				avanzadaStr+=" AND cg.concepto_especifico = '"+conceptoEspecifico+"'";
			}

			String consulta = consultaConceptosGlosasStr + avanzadaStr + " ORDER BY descripcion asc";
			logger.info("\n\n\n CONCEPTO-->"+consulta+"\n\n\n");
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucionInt);
			//ps.setInt(2,codigoInstitucionInt);
			//ps.setInt(3,codigoInstitucionInt);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consultaConceptos: "+e.toString() );
			return null;
		}
	}
//	 CONCEPTO CASTIGO
	/**
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param ajusteCredito
	 * @return
	 */
	public static boolean insertarConceptoCastigo(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion, String ajusteCredito)
	{
	  int resp=0;		 
		try
		  {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarConceptoCastigo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setInt(2,codigoInstitucionInt);
			ps.setString(3,descripcion);
			ps.setString(4,ajusteCredito);
			resp=ps.executeUpdate();
			ps.close();
			  if(resp < 0)
				  return  false;
			  else
				  return  true;  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosCarteraDao "+e.toString() );
			return false;
		}				
	}

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ResultSetDecorator consultaConceptoCastigo(Connection con, int codigoInstitucionInt) 
	{
		String consulta=consultaConceptoCastigoStr+"ORDER BY descripcion asc";
		try
		{
			PreparedStatementDecorator ps = null;
			
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucionInt);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consulta Conceptos Castigo: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ResultSetDecorator consultaRelacionConceptoCastigo(Connection con, String codigo, int codigoInstitucionInt) 
	{
		try
		{
			if(con.isClosed())
				con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = null;
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionConCastigo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,codigoInstitucionInt);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaRelacionConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}

	
	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoConcepto
	 * @param descripcion
	 * @param ajusteCredito
	 * @return
	 */
	public static boolean modificarConceptoCastigo(Connection con, String codigoConceptoAntiguo, String codigoConcepto, String descripcion, String ajusteCredito) 
	{
		int resp = 0;
		String modificarStr=modificarConceptoCastigo;
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();				
			}
					
			if(!codigoConcepto.equals(""))
			{
				modificarStr+="codigo = '"+codigoConcepto+"',";	  
			}			
		
			if(!descripcion.equals(""))
			{
				modificarStr+="descripcion = '"+descripcion+"',";
			}
			if(!ajusteCredito.equals(""))
			{
				modificarStr+="ajuste_credito = '"+ajusteCredito+"'";	  
			}			
			
			modificarStr+=" WHERE codigo = '"+codigoConceptoAntiguo+"'";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			resp=ps.executeUpdate();
			ps.close();
			 if(resp < 0)
				  return  false;
			  else
				  return  true;
			  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosCarteraDao "+e.toString() );
			return false;
		}			
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param ajusteCredito
	 * @return
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busquedaAvanzadaCastigo(Connection con,String codigo, String descripcion, String ajusteCredito)
	{
		ResultSetDecorator respuesta=null;
		String consultaArmada="";
		try
		{
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
			consultaArmada=armarConsulta(codigo, descripcion, ajusteCredito);
			Statement ps= con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			respuesta=new ResultSetDecorator(ps.executeQuery(consultaArmada));
			//ps.close();no cerrar prepared statement hasta resultset
				
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada del concepto castigo " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * 
	 * @param codigo
	 * @param descripcion
	 * @param ajusteCredito
	 * @return
	 */
	
	private static String armarConsulta(String codigo, String descripcion, String ajusteCredito)
	{
		String consulta="SELECT " +
		"cas.codigo AS codigoConcepto," +
		"cas.descripcion AS descripcion," +
		"cas.ajuste_credito AS ajusteCredito " +
		"FROM concepto_castigo_cartera cas "+
		"WHERE 1=1 ";
	   
		 if(codigo!= null && !codigo.equals(""))
		 {
		 consulta = consulta + "AND cas.codigo= '" +codigo+ "' ";
		 }
		
		if(descripcion != null && !descripcion.equals(""))
		 {
		 consulta= consulta + "AND UPPER(cas.descripcion) LIKE UPPER('%" +descripcion+ "%') ";
		 }
				   
		 if(ajusteCredito != null && !ajusteCredito.equals("-1"))
		 {
		 consulta = consulta + "AND UPPER(cas.ajuste_credito) LIKE  UPPER('%"+ajusteCredito+"%') ";
		 }
		 
		 consulta= consulta + " ORDER BY cas.descripcion";
		 
		logger.info("\n Conceto castigo-->"+consulta); 
		return consulta;
	}

	/**
	 * @param con
	 * @param codigoConcepto
	 * @return
	 */
	public static boolean eliminarConceptoCastigo(Connection con, String codigoConcepto)
	{
		int resp=0;
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoCastigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);			
			resp = ps.executeUpdate();
			ps.close();
			if(resp>0)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la eliminacion Concepto Castigo: SqlBaseConceptosCarteraDao "+e.toString() );
			return false;
		}
	}

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ResultSetDecorator consultaConceptosRespuestasGlosas(Connection con, int codigoInstitucionInt) 
	{
		try
		{
			PreparedStatementDecorator ps = null;
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaConceptosRespuestaGlosasStr+" ORDER BY descripcion asc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucionInt);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaRespuestaConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ResultSetDecorator consultaRelacionConceptosRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt) 
	{
		try
		{
			PreparedStatementDecorator ps = null;
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionRespuestaConGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,codigoInstitucionInt);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaRelacionConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}

	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static int eliminarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt) 
	{
		try
		  {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoRespuestaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConceptoAntiguo);
			ps.setInt(2,codigoInstitucionInt);
			int temp =ps.executeUpdate();
			ps.close();
			return temp;
			  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosPagoCarteraDao "+e.toString() );
			return -1;
		}			
	}

	/**
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @return
	 */
	public static int insertarConceptoRespuestaGlosas(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion) 
	{
		try
		  {
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarConceptoRespuestaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setInt(2,codigoInstitucionInt);
			ps.setString(3,descripcion);
			int resp = ps.executeUpdate();
			ps.close();
			return resp;
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosPagoCarteraDao "+e.toString() );
			return -1;
		}				}

	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoInstitucionInt
	 * @param codigoConcepto
	 * @param descripcion
	 * @return
	 */
	public static int modificarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt, String codigoConcepto, String descripcion) 
	{
		try
		  {
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarConceptoRespuestaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setString(2,descripcion);
			ps.setString(3,codigoConceptoAntiguo);
			ps.setInt(4,codigoInstitucionInt);
			int resp = ps.executeUpdate();
			ps.close();
			return resp;
			  
		  }
		catch (SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosPagoCarteraDao "+e.toString() );
			return -1;
		}				}

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @return
	 */
	public static ResultSetDecorator busquedaAvanzadaRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt, String descripcion) 
	{
		try
		{
			PreparedStatementDecorator ps = null;
			
			String avanzadaStr = "";
			if(!codigo.equals(""))
			{
				avanzadaStr+=" AND codigo = '"+codigo+"'";				
			}
			if(!descripcion.equals(""))
			{	
			   avanzadaStr+=" AND upper(descripcion) like upper('%"+descripcion+"%')";					
			}


			String consulta = consultaConceptosRespuestaGlosasStr + avanzadaStr + " ORDER BY descripcion asc";
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucionInt);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			//ps.close();no cerrar prepared statement hasta resultset
			return rs;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consultaConceptos: SqlBaseConceptosCarteraDao "+e.toString() );
		   return null;
		}
	}
}

;