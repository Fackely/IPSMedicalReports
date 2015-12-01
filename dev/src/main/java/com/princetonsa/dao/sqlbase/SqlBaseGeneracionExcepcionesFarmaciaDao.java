
/*
 * Creado   7/12/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones 
 * de acceso a la fuente de datos.
 *
 * @version 1.0, 7/12/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class SqlBaseGeneracionExcepcionesFarmaciaDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseGeneracionExcepcionesFarmaciaDao.class);
    
    /**
     * Carga el Listado de las solicitudes  de la cuenta cuando son de
	 * farmacia y != de centro_costo_externo, estado_med = Despachada - Admin,
	 * estado_fact= pendiente - cargada 
     */

	private final static String obtenerPorcentajeStr=
														"CASE WHEN " +
															"(SELECT porcentaje FROM excepciones_farmacia_gen efg WHERE s.numero_solicitud=efg.numero_solicitud AND det.articulo=efg.articulo) IS NULL " +
														"THEN " +
															"(CASE WHEN e.no_cubre IS NULL THEN 0 ELSE e.no_cubre END) " +
														"ELSE " +
															"(SELECT porcentaje FROM excepciones_farmacia_gen efg WHERE s.numero_solicitud=efg.numero_solicitud AND det.articulo=efg.articulo) " +
														"END ";
 
	
	private final static String listadoSolicitudesFarmaciaStr= "SELECT " +
																	"s.numero_solicitud AS numeroSolicitud, " +
																	"s.consecutivo_ordenes_medicas AS orden, " +
																	"cc.codigo AS codigoArea, " +
																	"cc.nombre AS nombreArea, " +
																	"det.articulo AS codigoArticulo, " +
																	"getDescArticulo(det.articulo) AS descripcionArticulo, " +
																	obtenerPorcentajeStr+
																	"AS porcentajeNoCubiertoGenerado, "+
																	"CASE WHEN " +
																		"(SELECT porcentaje FROM excepciones_farmacia_gen efg WHERE s.numero_solicitud=efg.numero_solicitud AND det.articulo=efg.articulo) IS NULL " +
																		"THEN " +
																			ValoresPorDefecto.getValorTrueParaConsultas()+" " +
																		"ELSE " +
																			ValoresPorDefecto.getValorFalseParaConsultas()+" " +
																		"END "+
																	"AS esNuloEnBD " +
																	"FROM solicitudes s " +
																	"INNER JOIN detalle_solicitudes det " +
																		"on(det.numero_solicitud=s.numero_solicitud) " +
																	"INNER JOIN cuentas c " +
																		"ON(c.id=s.cuenta) " +
																	"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+
																	"INNER JOIN centros_costo cc " +
																		"ON(cc.codigo=s.centro_costo_solicitante) " +
																	"LEFT OUTER JOIN excepciones_farmacia e " +
																		"ON(s.centro_costo_solicitante=e.centro_costo AND sc.convenio=e.convenio AND det.articulo=e.articulo) " +
																	"WHERE " +
																		"(s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+" OR s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCAdministrada+" OR s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCCargoDirecto+") " +
																		"AND esSolicitudCargadaoPendiente(s.numero_solicitud)='"+ConstantesBD.acronimoSi+"'"+
																		"AND s.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos;

	
	private final static String listadoSolicitudesFarmaciaGeneradasStr= "SELECT " +
																	"s.numero_solicitud AS numeroSolicitud, " +
																	"s.consecutivo_ordenes_medicas AS orden, " +
																	"cc.codigo AS codigoArea, " +
																	"cc.nombre AS nombreArea, " +
																	"efg.articulo AS codigoArticulo, " +
																	"getDescArticulo(efg.articulo) AS descripcionArticulo, " +
																	"efg.porcentaje AS porcentajeNoCubiertoGenerado, "+
																	"to_char(efg.fecha, 'dd/mm/yyyy')||'-'||to_char(efg.hora,'24:MI') AS fecha, "+
																	"getnombreusuario(efg.usuario) AS usuario "+
																	"FROM solicitudes s " +
																	"INNER JOIN excepciones_farmacia_gen efg " +
																		"on(efg.numero_solicitud=s.numero_solicitud) " +
																	"INNER JOIN cuentas c " +
																		"ON(c.id=s.cuenta) " +
																	"INNER JOIN centros_costo cc ON(cc.codigo=s.centro_costo_solicitado) " +
																	"WHERE " +
																		"(s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+" OR s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCAdministrada+" OR s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCCargoDirecto+") " +
																		"AND esSolicitudCargadaoPendiente(s.numero_solicitud)='"+ConstantesBD.acronimoSi+"'"+
																	"AND efg.porcentaje <> 0";

    
    /**
     * Inserta una generación de excepciones de farmacia
     */
    private final static String insertarGeneracionExcepcionesFarmaciaStr="INSERT INTO excepciones_farmacia_gen (numero_solicitud,articulo,porcentaje, usuario, fecha, hora) VALUES( ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") ";
    
    /**
     * modifica una generación de excepciones de farmacia
     */
    private final static String modificarGeneracionExcepcionesFarmaciaStr="UPDATE excepciones_farmacia_gen SET porcentaje=?, usuario = ?  WHERE numero_solicitud=? AND articulo=? ";
    
    /**
     * Links con las cuentas que tienen una entrada en la generación de excepciones de farmacia
     */
    private final static String linksConsultaGeneracionExcepcionesFarmaciaStr=	"SELECT" +
    																													"  c.via_ingreso AS codigoViaIngreso" +
    																													", getnombreviaingreso(c.via_ingreso) AS nombreViaIngreso" +
    																													", c.id AS numeroCuenta" +
    																													", TO_CHAR(c.fecha_apertura,'dd/mm/yyyy') AS fechaApertura" +
    																													", c.estado_cuenta AS codigoEstadoCuenta" +
    																													", es.nombre AS nombreEstadoCuenta" +
    																													"  FROM cuentas c " +
    																													"  INNER JOIN estados_cuenta es ON (c.estado_cuenta=es.codigo) " +
    																													"  INNER JOIN solicitudes s ON(s.cuenta = c.id) " +
    																													"  INNER JOIN excepciones_farmacia_gen efg ON (efg.numero_solicitud=s.numero_solicitud) " +
    																													"  WHERE c.codigo_paciente = ? ";
    
    
    /**
     * Almacena la consulta de la tabla de excepciones de farmacia
     */
    private final static String datosExcepcionesFarmaciaStr="SELECT " +
															    		"ef.codigo as codigoEF, " +
															    		"ef.convenio as convenioEF, " +
															    		"ef.centro_costo as centroCostoEF, " +
															    		"ef.articulo as articuloEF, " +
															    		"ef.no_cubre as no_cubreEF " +
															    		"FROM excepciones_farmacia ef " +
															    		"WHERE ef.convenio = ? AND ef.centro_costo = ? AND ef.articulo = ?";
    
    /**
     * Almacena la consulta, de verificación de parametrizacion de
     * Excepcion de farmacia por convenio.
     */
    private final static String existeExcpFarmaciaParaElConvenio = "SELECT centro_costo FROM excepciones_farmacia WHERE convenio = ?";
  
    /**
     * Eliminar Gen Excepciones de farmacia
     */
    private final static String eliminarGenExcepcionesFarmaciaStr="DELETE FROM excepciones_farmacia_gen WHERE numero_solicitud=? and articulo=? ";
    
	/**
	 * Carga el Listado de las solicitudes  de la cuenta cuando son de
	 * farmacia y != de centro_costo_externo, estado_med = Despachada - Admin,
	 * estado_fact= pendiente - cargada 
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param idCuenta, int, id de la cuenta
	 * @param tipoConsulta, boolean true realiza la consulta con todos los porcentajes
	 * 								false realiza la consulta solamente con los porcentajes generados 
	 * @return ResulSet list
	 * @see com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao#listadoSolicitudesFarmacia(java.sql.Connection,int,boolean)
	 */
	public static ResultSetDecorator listadoSolicitudesFarmacia(Connection con, int idCuenta, boolean tipoConsulta)
	{
		try
		{
		    PreparedStatementDecorator listadoStatement= null;
		    if(tipoConsulta)
		    {
				String orden=" AND c.id= ? ORDER BY getDescripcionArticulo(det.articulo)";
				listadoStatement= new PreparedStatementDecorator(con.prepareStatement(listadoSolicitudesFarmaciaStr+orden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    }
		    if(!tipoConsulta)
		    {
				String orden=" AND c.id= ? ORDER BY getDescripcionArticulo(efg.articulo)";
		        listadoStatement= new PreparedStatementDecorator(con.prepareStatement(listadoSolicitudesFarmaciaGeneradasStr+orden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    }
			listadoStatement.setInt(1,idCuenta);
		    
		    return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del listado de las solicitudes de farmacia: SqlBaseGeneracionExcepcionesFarmaciaDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * consulta de las excepciones de farmacia, segun los códigos de
	 * convenio, centroCosto y articulo
	 * @param con, Connection con la fuente de datos
	 * @param codigoConvenio, código del convenio
	 * @param codigoCentroCosto, código del centro de costo
	 * @param codigoArticulo, código del articulo
	 * @return ResultSetDecorator con los datos de la consulta.
	 * @return existeExcepcion, boolean true para realizar la consulta solo por código del convenio
	 * @return consultaExcepcion, boolean true para la consulta por código convenio, centro de costo y articulo
	 * @see com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao#consultaExcepcionesFarmacia(java.sql.Connection,int,int,int,boolean,boolean)
	 */
	public static ResultSetDecorator consultaExcepcionesFarmacia(Connection con, 
																        int codigoConvenio,
																        int codigoCentroCosto,
																        int codigoArticulo,
																        boolean existeExcepcion,
																        boolean consultaExcepcion)
	{
		try
		{
		    PreparedStatementDecorator consultaStatement= null;
		   
		    if(consultaExcepcion)
		    {
			    consultaStatement= new PreparedStatementDecorator(con.prepareStatement(datosExcepcionesFarmaciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    consultaStatement.setInt(1,codigoConvenio);
			    consultaStatement.setInt(2,codigoCentroCosto);
			    consultaStatement.setInt(3,codigoArticulo);
		    }
		    
		    if(existeExcepcion)
		    {
		        consultaStatement= new PreparedStatementDecorator(con.prepareStatement(existeExcpFarmaciaParaElConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    consultaStatement.setInt(1,codigoConvenio);
		    }
		    
		    
		    return new ResultSetDecorator(consultaStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consultaExcepcionesFarmacia: SqlBaseGeneracionExcepcionesFarmaciaDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Inserta una generación de excepciones de farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param porcentajeNoCubierto
	 * @param usuario
	 * @return int, 0 no inserta, 1 si inserta
	 * @see com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao#insertar(java.sql.Connection,int,double,String)
	 */
	public static int  insertar(	Connection con, int numeroSolicitud, int codigoArticulo,  double porcentajeNoCubierto, String usuario )
	{
		int resp=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarGeneracionExcepcionesFarmaciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoArticulo);
			ps.setDouble(3,porcentajeNoCubierto);
			ps.setString(4,usuario);
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseGeneracionExcepcionesFarmaciaDao "+e.toString() );
			resp=0;
		}
		return resp;
	}

	/**
	 * modificar una generación de excepciones de farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param porcentajeNoCubierto
	 * @param usuario
	 * @return int, 0  No modifica, 1 si modifica
	 * @see com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao#modificar(java.sql.Connection,int,int,double,String)
	 */
	public static int modificar(	Connection con, int numeroSolicitud, int codigoArticulo,  double porcentajeNoCubierto, String usuario )
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarGeneracionExcepcionesFarmaciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setDouble(1,porcentajeNoCubierto);
				ps.setString(2, usuario);
				ps.setInt(3, numeroSolicitud);
				ps.setInt(4, codigoArticulo);
				
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos: SqlBaseGeneracionExcepcionesFarmaciaDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Método que contiene el Resulset de la búsqueda avanzada 
	 * de la generación de Excepciones farmacia
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param idCuenta
	 * @param codigoArea
	 * @param descripcionArticulo
	 * @param esBusquedaArticuloPorCodigo
	 * @param porcentajeNoCubierto
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 * @see com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao#busqueda(java.sql.Connection,int,int,String,boolean,double)
	 */
	public static  ResultSetDecorator busqueda(		Connection con,
	        												int idCuenta,
															int codigoArea,
															String descripcionArticulo,
															boolean esBusquedaArticuloPorCodigo,
															double porcentajeNoCubierto	) throws SQLException
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
				respuesta= null;
			}
		}
		try
		{
			consultaArmada=armarConsulta(idCuenta, codigoArea, descripcionArticulo, esBusquedaArticuloPorCodigo,  porcentajeNoCubierto);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada SqlBaseGeneracionExcepcionesFarmaciaDao " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 *
	 * @param idCuenta
	 * @param codigoArea
	 * @param descripcionArticulo
	 * @param esBusquedaArticuloPorCodigo
	 * @param porcentajeNoCubierto
	 * @return
	 * @see com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao#armarConsulta(java.sql.Connection,int,String,boolean,double)
	 */
	private static String armarConsulta  (	int idCuenta,
	        												int codigoArea,
															String descripcionArticulo,
															boolean esBusquedaArticuloPorCodigo,
															double porcentajeNoCubierto)
	{
		String orden=" ORDER BY a.descripcion";
	    String consulta= listadoSolicitudesFarmaciaStr;
		if(idCuenta>0)
		    consulta+=" AND s.cuenta =  "+idCuenta; 
		if(codigoArea>0)
		    consulta+=" AND s.centro_costo_solicitante = "+codigoArea;
		//en caso de que se busque por la descripción del articulo
		if(!esBusquedaArticuloPorCodigo)
		{
		    if(descripcionArticulo !=null && !descripcionArticulo.equals(""))
		        consulta+=" AND UPPER(a.descripcion) LIKE UPPER('%"+descripcionArticulo+"%')";
		}
		//en caso de que se busque por el código exacto del artículo
		else
		{
		    if(descripcionArticulo !=null && !descripcionArticulo.equals(""))
		        consulta+=" AND a.codigo = "+descripcionArticulo;
		}
		if(porcentajeNoCubierto>0)
		{
		    consulta+=" AND "+obtenerPorcentajeStr+" = "+porcentajeNoCubierto;
		}
				
		
		return consulta+orden;
	}
	
	  /**
     * Carga el listado de links  con las cuentas que tienen una entrada en la 
	 * generación de excepciones de farmacia
     * @param con
	 * @param codigoPaciente
	 * @return
     * @see com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao#linksConsultaGeneracionExcepcionesFarmacia(java.sql.Connection,int)
     */
	public static ResultSetDecorator linksConsultaGeneracionExcepcionesFarmacia(Connection con, 
																				        int codigoPaciente)
	{
		try
		{
		    String consulta= linksConsultaGeneracionExcepcionesFarmaciaStr;
		    
		    consulta+=  " GROUP BY c.via_ingreso, c.id, c.fecha_apertura, c.estado_cuenta, es.nombre ORDER BY fechaApertura DESC  ";
		    
			PreparedStatementDecorator listadoStatement= new PreparedStatementDecorator(con.prepareStatement(consulta ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listadoStatement.setInt(1, codigoPaciente);
			return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los links de las solicitudes de farmacia: SqlBaseGeneracionExcepcionesFarmaciaDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Eliminar Gen Excepciones de farmacia
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public static int eliminar(	Connection con, 
	        								int numeroSolicitud,
											int codigoArticulo) 
	{
		int resp=0;	
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarGenExcepcionesFarmaciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoArticulo);
			
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en eliminar de datos: SqlBaseGeneracionExcepcionesFarmaciaDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
}
