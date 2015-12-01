/*
 * @(#)SqlBaseTerceroDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Cargos.InfoDeudorTerceroDto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoConceptosRetencionTercero;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para un tercero
 *
 * @version 1.0, Junio 19 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseTerceroDao 
{

	/** Objeto para realizar acciones de registro */
	private static Logger logger = Logger.getLogger(SqlBaseTerceroDao.class);

	/**
	 * Carga los datos para mostrarlos en el resumen
	 */
	private final static String cargarDatosTerceroStr = "SELECT ter.codigo AS codigo, " +
																			"ter.numero_identificacion AS numeroIdentificacion, "+
																			"coalesce(ter.digito_verificacion||'','') AS digito_verificacion," +
																			"ter.descripcion AS descripcion, " +
																			"ter.tipo_tercero AS tipo_tercero, ter.activo AS activo, " +
																			"ter.direccion, ter.telefono " +
																			"FROM  " +
																			"terceros ter " +
																			"INNER JOIN tipo_tercero tipt ON(tipt.codigo=ter.tipo_tercero) " +
																			"WHERE ter.codigo = ? "; 
		

	private final static String cargarCodigoUltimaInsercionStr = "SELECT MAX(codigo) AS codigo from terceros";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar
	 * un tercero en una BD Genérica.
	 */
	private static final String modificarTerceroStr=	"UPDATE terceros SET " +
																					"numero_identificacion = ?, " +
																					"descripcion = ?, " +
																					"tipo_tercero = ?, " +
																					"digito_verificacion = ?," +
																					"activo = ?," +
																					"direccion=?, telefono=? " +
																					"WHERE codigo = ?";
	
	/**
	 * consulta todos los terceros para mostrarlos en el listado 
	 */
	private static final String listarTerceroStr = 	"SELECT DISTINCT " +
																"ter.codigo AS codigo, " +
																"ter.numero_identificacion AS numeroIdentificacion, " +
																"coalesce(ter.digito_verificacion||'','') AS digito_verificacion, " +
																"ter.descripcion AS descripcion, " +
																"tipt.descripcion AS tipo_tercero, " +
																"ter.activo AS activo " +
																"FROM  " +
																"facturacion.terceros ter INNER JOIN facturacion.tipo_tercero tipt ON(tipt.codigo=ter.tipo_tercero) " +
															"WHERE ter.institucion = ? ";	
	
	/**
	 * Busca un tercero dado el numero de identificacion y el tipo
	 */
	private static final String busquedaExistenciaTerceroStr="SELECT codigo from terceros where numero_identificacion=? ";

	private static String modificarConceptoRetTercero="UPDATE administracion.conceptos_retencion_tercero SET " +
															"concepto_retencion=?," +
															"tipo_aplicacion=?," +
															"ind_agente_retenedor=?," +
															"fecha_modifica=?," +
															"hora_modifica=?," +
															"usuario_modifica=?," +
															"activo=?," +
															"fecha_inactivacion=?," +
															"hora_inactivacion=?," +
															"usuario_inactivacion=?" +
														" WHERE consecutivo=?";
	
	private static String insertarConceptoRetTercero="INSERT INTO administracion.conceptos_retencion_tercero " +
														"(consecutivo,tercero,concepto_retencion,tipo_aplicacion," +
														"ind_agente_retenedor,fecha_modifica,hora_modifica," +
														"usuario_modifica,activo,fecha_inactivacion,hora_inactivacion," +
														"usuario_inactivacion) VALUES " +
														"(?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	private static String consultarConceptosRetTercero="SELECT crt.consecutivo, " +
														"crt.tercero AS tercero, " +
														"crt.concepto_retencion AS conRet, " +
														"crt.tipo_aplicacion AS tipoApli, " +
														"crt.ind_agente_retenedor AS indAgente, " +
														"crt.activo AS activo, " +
														"cr.tipo_retencion AS tipo_retencion, " +
														"cr.descripcion_concepto AS desConceptoRet, " +
														"tr.codigo AS tipoRetCodigo, " +
														"tr.descripcion AS tipoRetDesc, " +
														"tr.consecutivo AS consecutivo_tipo_retencion, " +
														"tr.sigla AS tipoRetSigla " +
														"FROM administracion.conceptos_retencion_tercero crt " +
														"INNER JOIN administracion.conceptos_retencion cr ON(cr.consecutivo= crt.concepto_retencion) " +
														"INNER JOIN administracion.tipos_retencion tr ON(cr.tipo_retencion= tr.consecutivo) " +
														"WHERE crt.tercero=? AND crt.activo= '"+ConstantesBD.acronimoSi+"' ";
	 
	
	public static ArrayList<DtoConceptosRetencionTercero> consultarConceptosRetTercero(int tercero) 
	{
		ArrayList<DtoConceptosRetencionTercero> listaDtoConceptosRetencionTercero= new ArrayList<DtoConceptosRetencionTercero>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarConceptosRetTercero,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tercero);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			logger.info("\n\nconsulta::::::: "+consultarConceptosRetTercero+"  tercero::::::. "+tercero);
			
			while(rs.next())
			{
				DtoConceptosRetencionTercero dto= new DtoConceptosRetencionTercero();
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setTercero(rs.getInt("tercero")+"");
				dto.getConceptosRet().setConsecutivo(rs.getInt("conRet")+"");
				dto.setTipoAplicacion(rs.getInt("tipoApli")+"");
				dto.setIndAgenteRetenedor(rs.getString("indAgente"));
				dto.setActivo(rs.getString("activo"));
				dto.getConceptosRet().setDescripcion(rs.getString("desConceptoRet"));
				dto.getConceptosRet().setTipoRetencion(rs.getString("tipoRetCodigo"));
				dto.getConceptosRet().setTipoRetencionDesc(rs.getString("tipoRetDesc"));
				dto.getConceptosRet().setTipoRetencionSigla(rs.getString("tipoRetSigla"));
				dto.getConceptosRet().setTipoRetencionConsecutivo(rs.getInt("consecutivo_tipo_retencion"));
				dto.getConceptosRet().getTipoRet().setConsecutivo(rs.getInt("consecutivo_tipo_retencion"));
				dto.setEnBD(true);
				listaDtoConceptosRetencionTercero.add(dto);
				
			}
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO CONCEPTOS.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoConceptosRetencionTercero;
	}
	
	public static boolean insertarConceptoRetencion(DtoConceptosRetencionTercero dto, int tercero)
	{
	
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarConceptoRetTercero, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_conceptos_ret_tercero"));
			ps.setInt(2,tercero);
			ps.setInt(3,Utilidades.convertirAEntero(dto.getConceptosRet().getConsecutivo()));
			ps.setInt(4,Utilidades.convertirAEntero(dto.getTipoAplicacion()));
			ps.setString(5,dto.getIndAgenteRetenedor());
			ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModificacion()));	
			ps.setString(7, dto.getHoraModificacion());					
			ps.setString(8, dto.getUsuarioModificacion());
			ps.setString(9, dto.getActivo());
			if(dto.getFechaInactivacion().equals(""))
				ps.setNull(10, Types.VARCHAR);
			else
				ps.setString(10, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			if(dto.getHoraInactivacion().equals(""))
				ps.setNull(11, Types.VARCHAR);
			else
				ps.setString(11, dto.getHoraInactivacion());
			if(dto.getUsuarioInactivacion().equals(""))
				ps.setNull(12, Types.VARCHAR);
			else
				ps.setString(12, dto.getUsuarioInactivacion());			
		
			if(ps.executeUpdate()>0)
			{	
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();
							
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR INGRESAR CONCEPTO---> "+e);
		}		
		return false;
	}
	
	
	/**
	 * inserta un  tercero (nit)
	 * @param con, Connection, conexión abierta con una fuente de datos 
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del terceroarg1
	 * @param institucion, institución según dado el usuarioBasico
	 * @return el código ingresado si lo inserta, 0 si no se produjo la adición
	 */
	public static int insertar(Connection con,
								String numeroIdentificacion,
								String descripcion,
								boolean activa,
								int institucion,
								String insertarTerceroStr,
								int codigoTipoTercero,
								String digitoVerificacion,
								String direccion, String telefono)
	{
		int resp = 0;
		int valorSecuencia = 0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarTerceroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					valorSecuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_terceros");
					ps.setInt(1, valorSecuencia);
					ps.setString(2, numeroIdentificacion);
					ps.setString(3, descripcion);
					ps.setInt(4, institucion);
					ps.setBoolean(5, activa);
					ps.setInt(6, codigoTipoTercero);
					if(UtilidadTexto.isEmpty(digitoVerificacion))
						ps.setNull(7, Types.VARCHAR);
					else
						ps.setString(7, digitoVerificacion);
					if(UtilidadTexto.isEmpty(direccion))
						ps.setNull(8, Types.VARCHAR);
					else
						ps.setString(8, direccion);
					if(UtilidadTexto.isEmpty(telefono))
						ps.setNull(9, Types.VARCHAR);
					else
						ps.setString(9, telefono);
					
					if(ps.executeUpdate()>0)
						resp = valorSecuencia;
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos: SqlBaseTerceroDao "+e.toString() );
					resp = 0;
			}
			return resp;
	}
	
	/**
	 * inserta un  tercero (nit) dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos 
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, institución según dado el usuarioBasico
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public static ResultadoBoolean insertarTransaccional(	Connection con,
																								String numeroIdentificacion,
																								String descripcion,
																								boolean activa,
																								int institucion,
																								String estado,
																								String insertarTerceroStr) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resp=new ResultadoBoolean(false);
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean(false, "Error iniciando transacción");
		    }
		}
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			else
			{	
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarTerceroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,numeroIdentificacion);
				ps.setString(2,descripcion);
				ps.setInt(3,institucion);
				ps.setBoolean(4,activa);
				
				int insert=ps.executeUpdate();	
				
				if( insert == 0 )
				{
				    myFactory.abortTransaction(con);	
					resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseTerceroDao: ");
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseTerceroDao");
			myFactory.abortTransaction(con);

			resp=new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseTerceroDao: "+e);
			return resp;		
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return resp =new ResultadoBoolean(true);		
	}
	
	/**
	 * Método que  carga  los datos de un tercero según los datos
	 * que lleguen del codigo de tercero para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public static ResultSetDecorator cargarResumen(Connection con, int codigo) 
	{
		try{
			PreparedStatementDecorator cargarResumenStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosTerceroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarResumenStatement.setInt(1, codigo);
			return new ResultSetDecorator(cargarResumenStatement.executeQuery());
		}
		catch 	(SQLException e)
		{
			logger.warn(e+" Error en la consulta del resumen del tercero: SqlBaseTerceroDao "+e.toString());	
			return null;
		}
	}

	/**Carga el último tercero insertado**/
	public static ResultSetDecorator cargarUltimoCodigo(Connection con)
	{
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoUltimaInsercionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(cargarUltimoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último código del tercero: SqlBaseTerceroDao "+e.toString());
			return null;
		}
	}

	/**
	 * modifica un  tercero (nit)
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, codigo asignado por el sistema al tercero
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, institución según dado el usuarioBasico
	 * @return  1 si encuentra, 0 de lo contrario
	 */
	public static int modificar(	Connection con,
													int codigo,
													String numeroIdentificacion,
													String descripcion,
													int codigoTipoTercero,
													String digitoVerificacion,
													boolean activa, 
													String direccion, String telefono)
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarTerceroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,numeroIdentificacion);
				ps.setString(2,descripcion);
				ps.setInt(3, codigoTipoTercero);
				if(digitoVerificacion.equals(""))
					ps.setString(4, null);
				else
					ps.setString(4, digitoVerificacion);
				ps.setBoolean(5,activa);
				if(UtilidadTexto.isEmpty(direccion))
					ps.setNull(6, Types.VARCHAR);
				else
					ps.setString(6,direccion);
				if(UtilidadTexto.isEmpty(telefono))
					ps.setNull(7, Types.VARCHAR);
				else
					ps.setString(7,telefono);
				ps.setInt(8,codigo);
				
				if(ps.executeUpdate()>0)
					resp=1;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseTerceroDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * modifica un  tercero (nit) dado su código con los paramétros dados  dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, codigo asignado por el sistema al tercero
	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param activa, boolean, estado activo/ inactivo del tercero
	 * @param institucion, institución según dado el usuarioBasico
	 * @param estado. String, estado dentro de la transacción 
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public static ResultadoBoolean modificarTransaccional(	Connection con,
																									int codigo,
																									String numeroIdentificacion,
																									String descripcion,
																									int codigoTipoTercero,
																									String digitoVerificacion,
																									boolean activa,
																									String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
		    if (!myFactory.beginTransaction(con))
		    {
		        return new ResultadoBoolean(false, "Error iniciando transacción");
		    }
		}	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
		
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarTerceroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1,numeroIdentificacion);
			ps.setString(2,descripcion);
			ps.setInt(3, codigoTipoTercero);
			ps.setString(4, digitoVerificacion);
			ps.setBoolean(5,activa);
			ps.setInt(6,codigo);
			

			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseTerceroDao");
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(false," Error en la inserción de datos: SqlBaseTerceroDao: "+e);
		}
		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
		    myFactory.endTransaction(con);
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * Busca un tercero dado el tipo y numero de identificacion
	 * @param con
	 * @param numeroIdentificacion
	 * @return
	 * @throws SQLException
	 */
	public static int busquedaExistenciaTercero(Connection con, String numeroIdentificacion) throws SQLException
	{
	    int codigoTercero= ConstantesBD.codigoNuncaValido;
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
				return codigoTercero;
			}
		}
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(busquedaExistenciaTerceroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroIdentificacion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
			    codigoTercero= rs.getInt("codigo");
			}
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado terceros " +e.toString());
			return codigoTercero;
		}
		
		return codigoTercero;
	}
	
	/**
	 * Método que contiene el Resulset de todos los terceros
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoInstitucion
	 * @return Resultset con todos los datos de la tabla terceros
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
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			logger.info("\n\nconsulta lista tercero:::::. "+listarTerceroStr);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(listarTerceroStr+" ORDER BY ter.descripcion",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			respuesta=new ResultSetDecorator(ps.executeQuery());	
			
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado terceros " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * Método que contiene el Resulset de todas los terceros buscados
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoInstitucion
	 * @param numeroIdentificacion,
	 * @param descripcion
	 * @param activaAux
	 * @return Resultset con todos los datos de la tabla terceros
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busqueda(		Connection con,
	        												int codigoInstitucion,
																String numeroIdentificacion,
																String descripcion,
																int codigoTipoTercero,
																String digitoVerificacion, 
																int activaAux) throws SQLException
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
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta = null;
			}
		}
		try
		{
			consultaArmada=armarConsulta(numeroIdentificacion,descripcion, codigoTipoTercero, digitoVerificacion, activaAux);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de terceros " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 */
	private static String armarConsulta(	String numeroIdentificacion,
																	String descripcion,
																	int codigoTipoTercero,
																	String digitoVerificacion, 
																	int activaAux)
	{
		logger.info("--------------ARMAR CONSULTA ------------");
		logger.info("BUSQUEDA TIPO TERCERO 		  ->"+codigoTipoTercero);
		logger.info("BUSQUEDA DIGITO VERIFICACION ->"+digitoVerificacion);
		
		String consulta=	listarTerceroStr;
		if(numeroIdentificacion != null && !numeroIdentificacion.equals(""))
			consulta+= " AND UPPER(ter.numero_identificacion||'') LIKE UPPER('%"+numeroIdentificacion+"%') "; 
		if(descripcion != null && !descripcion.equals(""))
			consulta+=" AND UPPER(ter.descripcion||'') LIKE UPPER('%"+descripcion+"%') ";
		if(activaAux==1)
			consulta += " AND ter.activo=  "+ValoresPorDefecto.getValorTrueParaConsultas()	;	
		if(activaAux==2)
			consulta += " AND ter.activo=  "+ValoresPorDefecto.getValorFalseParaConsultas()	;	
		
		if(digitoVerificacion != null && !digitoVerificacion.equals(""))
			consulta+=" AND UPPER(ter.digito_verificacion||'') LIKE UPPER('%"+digitoVerificacion+"%') ";
		
		if(codigoTipoTercero== ConstantesBD.codigoTipoTerceroNoAplica)
				consulta += "AND ter.tipo_tercero =9 ";
		if(codigoTipoTercero==ConstantesBD.codigoTipoTerceroPersonaJuridica)
			consulta += "AND ter.tipo_tercero =1 ";
		if(codigoTipoTercero==ConstantesBD.codigoTipoTerceroPersonaNatural)
			consulta += "AND ter.tipo_tercero =0 ";
		
		consulta+=" ORDER BY ";
		
		if((numeroIdentificacion == null || numeroIdentificacion.equals("")))
			consulta+= "ter.descripcion ";
		else
			consulta+= "ter.numero_identificacion";
		logger.info("COnsulta TERCEROS  ************"+consulta);
		
		
		return consulta;
	}

	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */

	public static boolean modificarConceptoRetencion(DtoConceptosRetencionTercero dto)
	{

		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarConceptoRetTercero, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));

			ps.setInt(1,Utilidades.convertirAEntero(dto.getConceptosRet().getConsecutivo()));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getTipoAplicacion()));
			ps.setString(3,dto.getIndAgenteRetenedor());
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModificacion()));	
			ps.setString(5, dto.getHoraModificacion());			
			ps.setString(6, dto.getUsuarioModificacion());
			ps.setString(7, dto.getActivo());
			if(dto.getFechaInactivacion().equals(""))
				ps.setNull(8, Types.VARCHAR);
			else
				ps.setString(8, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			if(dto.getHoraInactivacion().equals(""))
				ps.setNull(9, Types.VARCHAR);
			else
				ps.setString(9, dto.getHoraInactivacion());
			if(dto.getUsuarioInactivacion().equals(""))
				ps.setNull(10, Types.VARCHAR);
			else
				ps.setString(10, dto.getUsuarioInactivacion());			
		
			ps.setInt(11, Utilidades.convertirAEntero(dto.getConsecutivo()));
			if(ps.executeUpdate()>0)
			{	
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();

		}catch (SQLException e) {
			logger.error("error modificando el concepto "+e);
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<InfoDeudorTerceroDto> cargarTerceroArray(InfoDeudorTerceroDto dto)
	{
		ArrayList<InfoDeudorTerceroDto> lista = new ArrayList<InfoDeudorTerceroDto>();
		
		String consulta= 
		"SELECT ter.codigo AS codigo, " +
			"ter.numero_identificacion AS numeroIdentificacion, "+
			"coalesce(ter.digito_verificacion||'','') AS digito_verificacion," +
			"ter.descripcion AS descripcion, " +
			"ter.tipo_tercero AS tipo_tercero, " +
			"ter.activo AS activo, " +
			"ter.direccion as direccion," +
			"ter.telefono as telefono,  " +
			"de.codigo as codigoPKDeudor " +
		"FROM  " +
			"terceros ter " +
		"INNER JOIN " +
			"tipo_tercero tipt " +
				"ON(tipt.codigo=ter.tipo_tercero) " +
		"LEFT OUTER JOIN " +
			"facturasvarias.deudores de " +
				"ON (ter.codigo=de.codigo_tercero)" +
		"WHERE ";
		
		boolean ponerAnd=false;
	
		if(dto.getDtoTercero().getCodigo().intValue()>0)
		{
			ponerAnd=true;
			consulta+="ter.codigo=?";
		}

		if(!UtilidadTexto.isEmpty(dto.getDtoTercero().getNumeroIdentificacion()))
		{
			if(ponerAnd)
			{
				consulta+=" AND ";
			}
			ponerAnd=true;
			consulta+="ter.numero_identificacion=?";
		}

		if(dto.getDtoTercero().getDtoTipoTercero().getCodigo()>ConstantesBD.codigoNuncaValido)
		{
			if(ponerAnd)
			{
				consulta+=" AND ";
			}
			ponerAnd=true;
			consulta+="tipt.codigo=?";
		}
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			
			int indice=1;
			
			if(dto.getDtoTercero().getCodigo().intValue()>0)
			{
				ps.setInt(indice, dto.getDtoTercero().getCodigo().intValue());
				indice++;
			}
			if(!UtilidadTexto.isEmpty(dto.getDtoTercero().getNumeroIdentificacion()))
			{
				ps.setString(indice, dto.getDtoTercero().getNumeroIdentificacion());
				indice++;
			}
			if(dto.getDtoTercero().getDtoTipoTercero().getCodigo()>ConstantesBD.codigoNuncaValido)
			{
				ps.setInt(indice, dto.getDtoTercero().getDtoTipoTercero().getCodigo());
				indice++;
			}
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoDeudorTerceroDto tdto = new InfoDeudorTerceroDto();
				tdto.getDtoTercero().setCodigo( new  BigDecimal(rs.getInt("codigo")));
				tdto.getDtoTercero().setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
				//tdto.getDtoTercero().setDigitoVerificacion((int) rs.getDouble("digito_verificacion"));
				tdto.getDtoTercero().setDescripcion(rs.getString("descripcion"));
				tdto.getDtoTercero().setActivo(rs.getString("activo"));
				tdto.getDtoTercero().setTipoTercero(rs.getInt("tipo_tercero"));
				tdto.getDtoTercero().setDireccion(rs.getString("direccion"));
				tdto.getDtoTercero().setTelefono(rs.getString("telefono"));
				tdto.getDtoDeudor().setCodigo(String.valueOf(rs.getInt("codigoPKDeudor")));
				lista.add(tdto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> ",e);
		}
		return lista; 
	}
	
	
	
	
	
}
