/*
 * @(#)SqlBaseArqueosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase.tesoreria;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la estructura de los arqueos
 *
 * @version 1.0, Abr 25 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseArqueosDao 
{
   /**
    * Objeto para manejar los logs de esta clase
    */
	private static Logger logger = Logger.getLogger(SqlBaseArqueosDao.class);

	/**
	 * cadena para hacer la insercion del arqueo definitivo
	 */
	private static String insertarArqueoDefinitivoStr=	"insert into arqueos_definitivos" +
														" (" +
															"consecutivo, " +
															"institucion, " +
															"usuario, " +
															"fecha_arqueo, " +
															"hora_arqueo, " +
															"caja, " +
															"cajero, " +
															"fecha_arqueada," +
															"tipo_arqueo" +
														"  ) " +
														"values(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	/**
	 * cadena para hacer la insercion del cierre caja
	 */
	private static String insertarCierreCajaStr=	"insert into cierres_cajas" +
														" (" +
															"consecutivo, " +
															"institucion, " +
															"usuario, " +
															"fecha_cierre, " +
															"hora_cierre, " +
															"caja, " +
															"cajero, " +
															"fecha_cerrada," +
															"tipo_arqueo," +
															"caja_ppal " +
														"  ) " +
														"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	/**
	 * cadena para consultar el resumen de los cierres de caja
	 */
	private static String resumenCierreCajaStr= "SELECT " +
												"c.usuario AS loginusuariogenera, " +
												"getnombrepersona(u.codigo_persona) ||' [' ||u.login||']' AS usuariogenera, " +
												"to_char(c.fecha_cierre, 'DD/MM/YYYY') AS fechacierre, " +
												"substr(c.hora_cierre, 1, 5) AS horacierre, " +
												"c.caja AS consecutivocaja, " +
												"c.cajero AS logincajero, " +
												"getnombrepersona(u1.codigo_persona) ||' [' ||u1.login||']' AS cajero, " +
												"to_char(c.fecha_cerrada, 'DD/MM/YYYY')  AS fechacerrada, " +
												"c.caja_ppal AS consecutivocajappal " +
												"FROM cierres_cajas c " +
												"INNER JOIN usuarios u ON (u.login=c.usuario) " +
												"INNER JOIN usuarios u1 ON (u1.login=c.cajero) " +
												"WHERE c.institucion=? AND c.consecutivo=?";
	
	/**
	 * cadena para consultar el resumen de los arqueos definitivos
	 */
	private static String resumenArqueoDefinitivoStr= "SELECT " +
													"a.usuario AS loginusuariogenera, " +
													"getnombrepersona(u.codigo_persona) ||' [' ||u.login||']' AS usuariogenera, " +
													"to_char(a.fecha_arqueo, 'DD/MM/YYYY') AS fechaarqueo, " +
													"substr(a.hora_arqueo, 1, 5) AS horaarqueo, " +
													"a.caja AS consecutivocaja, " +
													"a.cajero AS logincajero, " +
													"getnombrepersona(u1.codigo_persona) ||' [' ||u1.login||']' AS cajero, " +
													"to_char(a.fecha_arqueada, 'DD/MM/YYYY') AS fechaarqueada " +
													"FROM arqueos_definitivos a " +
													"INNER JOIN usuarios u ON (u.login=a.usuario) " +
													"INNER JOIN usuarios u1 ON (u1.login=a.cajero) " +
													"WHERE a.institucion=? AND a.consecutivo=?";
	
	
	/**
	 * cadena para la actualizacion el campo arqueo definitivo de la tabla recibos caja
	 */
	private static String actualizarRecibosCajaCampoArqueoStr="update recibos_caja set arqueo_definitivo=? where numero_recibo_caja=? and institucion=?";
	
	/**
	 * cadena para la actualizacion del campo cierre caja de la tabla recibos caja
	 */
	private static String actualizarRecibosCajaCampoCierreCajaStr="update recibos_caja set cierre_caja=? where numero_recibo_caja=? and institucion=?";
	
	 /**
     * cadena para consultar los recibos caja que no cumplen con el arqueo definitivo para un
     * cajero - caja y fecha dada
     */
    private static String estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha="SELECT " +
    						"rc.numero_recibo_caja as numerorecibo " +
    						"from " +
    						"recibos_caja rc " +
    						"where " +
    						"rc.institucion=? " +
    						"and rc.fecha= ? " +
    						"and rc.usuario=? " +
    						"and rc.caja =? " +
    						"and rc.estado NOT IN ("+ConstantesBD.codigoEstadoReciboCajaEnArqueo+","+ConstantesBD.codigoEstadoReciboCajaAnulado+","+ConstantesBD.codigoEstadoReciboCajaEnCierre+") " +
    						"order by numerorecibo" ;
	
    
    /**
     * cadena para consultar los recibos caja que no cumplen con el arqueo definitivo para un
     * cajero - caja y fecha dada
     */
    private static String estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha="SELECT " +
    						"d.consecutivo as numerodevolucion " +
    						"from " +
    						"devol_recibos_caja d " +
    						"where " +
    						"d.institucion=? " +
    						"and d.fecha_devolucion= ? " +
    						"and d.usuario_devolucion=? " +
    						"and d.caja_devolucion =? " +
    						"and d.estado= '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' " +
    						"and d.arqueo_definitivo is null " +
    						"and d.cierre_caja is null " +
    						"order by numerodevolucion" ;
	
    
    /**
     * Cadena para realizar la evaluacion de la existencia del cierre caja 
     * para un cajero - caja - fecha determinados
     */
    private static String existeCierreCajaStr= 	"SELECT " +
    											"consecutivo " +
    											"FROM " +
    											"cierres_cajas c " +
    											"where " +
    											"c.institucion=? " +
    											"and c.fecha_cerrada=? " +
    											"and c.usuario=? " +
    											"and c.caja=?";
    /**
     * Insercion del arqueo definitivo
     * @param con
     * @param consecutivoArqueoDefinitivo
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @param fechaArqueoDDMMYYYY
     * @param horaArqueo
     * @param consecutivoCaja
     * @param loginUsuarioCajero
     * @param fechaArqueadaDDMMYYYY
     * @return
     */
    public static boolean  insertarArqueoDefinitivo(  Connection con,
                                                      String consecutivoArqueoDefinitivo,
                                                      int codigoInstitucion,
                                                      String loginUsuarioGenera,
                                                      String fechaArqueoDDMMYYYY,
                                                      String horaArqueo,
                                                      String consecutivoCaja,
                                                      String loginUsuarioCajero,
                                                      String fechaArqueadaDDMMYYYY
                                                    )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarArqueoDefinitivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, consecutivoArqueoDefinitivo);
            ps.setInt(2, codigoInstitucion);
            ps.setString(3, loginUsuarioGenera);
            ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaArqueoDDMMYYYY));
            ps.setString(5, horaArqueo);
            ps.setString(6, consecutivoCaja);
            ps.setString(7, loginUsuarioCajero);
            ps.setString(8, UtilidadFecha.conversionFormatoFechaABD(fechaArqueadaDDMMYYYY));
            ps.setInt(9,ConstantesBD.codigoTipoArqueoDefinitivo);
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos del arqueo definitivo: SqlBaseArqueosDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * actualiza el campo arqueo definitivo de la tabla recibos caja
     * @param con
     * @param consecutivoArqueoDefinitivo
     * @param numeroReciboCaja
     * @param codigoInstitucion
     * @return
     */
    public static boolean  actualizarRecibosCajaCampoArqueoDefinitivo(  	Connection con,
		                                                      				String consecutivoArqueoDefinitivo,
		                                                      				String numeroReciboCaja,
		                                                      				int codigoInstitucion
		                                                    		)  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(actualizarRecibosCajaCampoArqueoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            if(consecutivoArqueoDefinitivo.trim().equals(""))
            	ps.setObject(1, null);
            else
            	ps.setString(1, consecutivoArqueoDefinitivo);
            ps.setString(2, numeroReciboCaja);
            ps.setInt(3, codigoInstitucion);
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la actualizacion de datos del arqueo definitivo de la tabla recibos caja: SqlBaseArqueosDao "+e.toString() );
            return false;
        }
    }
	
    
    /**
     * Insercion del cierre caja
     * @param con
     * @param consecutivoCierreCaja
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @param fechaCierreDDMMYYYY
     * @param horaCierre
     * @param consecutivoCaja
     * @param loginUsuarioCajero
     * @param fechaCerradaDDMMYYYY
     * @return
     */
    public static boolean  insertarCierreCaja(  Connection con,
                                                String consecutivoCierreCaja,
                                                int codigoInstitucion,
                                                String loginUsuarioGenera,
                                                String fechaCierreDDMMYYYY,
                                                String horaCierre,
                                                String consecutivoCaja,
                                                String loginUsuarioCajero,
                                                String fechaCerradaDDMMYYYY,
                                                String consecutivoCajaPpal
                                              )  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarCierreCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, consecutivoCierreCaja);
            ps.setInt(2, codigoInstitucion);
            ps.setString(3, loginUsuarioGenera);
            ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaCierreDDMMYYYY));
            ps.setString(5, horaCierre);
            ps.setString(6, consecutivoCaja);
            ps.setString(7, loginUsuarioCajero);
            ps.setString(8, UtilidadFecha.conversionFormatoFechaABD(fechaCerradaDDMMYYYY));
            ps.setInt(9,ConstantesBD.codigoTipoArqueoCierreCaja);
            ps.setString(10, consecutivoCajaPpal);
            
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la inserción de datos del cierre caja: SqlBaseArqueosDao "+e.toString() );
            return false;
        }
    }
    
    /**
     * actualiza el campo cierre caja de la tabla recibos caja
     * @param con
     * @param consecutivoCierreCaja
     * @param numeroReciboCaja
     * @param codigoInstitucion
     * @return
     */
    public static boolean  actualizarRecibosCajaCampoCierreCaja(  	Connection con,
                                                      				String consecutivoCierreCaja,
                                                      				String numeroReciboCaja,
                                                      				int codigoInstitucion
                                                    			)  
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(actualizarRecibosCajaCampoCierreCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            if(consecutivoCierreCaja.trim().equals(""))
            	ps.setObject(1, null);
            else
            	ps.setString(1, consecutivoCierreCaja);
            ps.setString(2, numeroReciboCaja);
            ps.setInt(3, codigoInstitucion);
            if(ps.executeUpdate()>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la actualizacion de datos del cierre caja de la tabla recibos caja: SqlBaseArqueosDao "+e.toString() );
            return false;
        }
    }
    
    
    /**
	 * medodo para consultar los recibos caja que no cumplen con el arqueo definitivo para un
     * cajero - caja y fecha dada, retorna String con los numero Recibos caja separados por comas, 
	 * o "" si todos ya tienen el arqueo definitivo o "ERROR" en caso de exception
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaArqueoDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @return String con los numero Recibos caja separados por comas, 
	 * 			o "" si todos ya tienen el arqueo definitivo
	 * 			o "ERROR" en caso de exception
	 */
	public static String estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha(	Connection con,
																					int codigoInstitucion,
																					String fechaArqueoDDMMYYYY,
																					String loginUsuarioCajero,
																					String consecutivoCaja
																				)
	{
		String respuesta="";
		try
		{
			if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoInstitucion);
            ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaArqueoDDMMYYYY));
            ps.setString(3, loginUsuarioCajero);
            ps.setString(4, consecutivoCaja);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            while(rs.next())
            	respuesta+= rs.getString("numerorecibo")+", ";
            return respuesta;
		}
		catch (SQLException sqle) 
		{
			logger.error("Error en estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha de SqlBaseConsultarecibosCaja");
			return "ERROR";
		}
	}
    
	
	/**
	 * metodo que evalua la existencia del cierre caja 
     * para un cajero - caja - fecha determinados
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaArqueoDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @return
	 */
	public static boolean existeCierreCaja(	Connection con,
											int codigoInstitucion,
											String fechaArqueoDDMMYYYY,
											String loginUsuarioCajero,
											String consecutivoCaja
										 )
	{
		try
		{
			if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(existeCierreCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoInstitucion);
            ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaArqueoDDMMYYYY));
            ps.setString(3, loginUsuarioCajero);
            ps.setString(4, consecutivoCaja);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	return true;
            return false;
		}
		catch (SQLException sqle) 
		{
			logger.error("Error en estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha de SqlBaseConsultarecibosCaja");
			return false;
		}
	}
	
	/**
	 * metodo que realiza la busqueda de los arqueos y de los cierres caja
	 * @param con
	 * @param fechaInicialConsultaArqueosCierres
	 * @param fechaFinalConsultaArqueosCierres
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @param codigoTipoArqueoStr
	 * @param codigoInstitucion
	 * @return
	 */
	public static Collection busquedaArqueosCierres(	Connection con,
														String fechaInicialConsultaArqueosCierres,
														String fechaFinalConsultaArqueosCierres,
														String loginUsuarioCajero,
														String consecutivoCaja,
														String codigoTipoArqueoStr,
														int codigoInstitucion,
														int codigoCentroAtencion,
														String consecutivoCajaPpal)
	{
		Collection coleccion=new ArrayList();
		String consultaArqueosDefinitivos=	"SELECT " +
											"c.codigo ||' '|| c.descripcion AS descripcioncaja, " +
											"ad.cajero AS loginusuariocajero, " +
											"getnombrepersona(u.codigo_persona) ||' ['|| ad.cajero ||']' AS descripcioncajero, " +
											"ta.codigo AS codigotipoarqueo, " +
											"ta.descripcion AS descripciontipoarqueo, " +
											"ad.consecutivo AS consecutivoasociado, " +
											"to_char(ad.fecha_arqueada, 'DD/MM/YYYY') as fechaarqueadaocerrada," +
											"ad.fecha_arqueada as fechaarqueadaocerrada_bd, " +
											"'"+ConstantesBD.codigoFuncionalidadTipoArqueoDefinitivo+"' AS codigofuncionalidad," +
											"ad.caja AS consecutivocaja, " +
											"'' as consecutivocajappal, " +
											"'' AS trasladocaja " +
											"FROM " +
											"arqueos_definitivos ad " +
											"INNER JOIN usuarios u ON (ad.cajero=u.login) " +
											"INNER JOIN cajas c ON (c.consecutivo=ad.caja) " +
											"INNER JOIN tipos_arqueos ta ON (ta.codigo=ad.tipo_arqueo) " +
											"WHERE ad.institucion= "+codigoInstitucion+" ";
											//no se cargan los que ya estan cerrados
											//no aplica segun margarita
											//" and ad.consecutivo NOT IN (select rc.arqueo_definitivo from recibos_caja rc where rc.arqueo_definitivo=ad.consecutivo and cierre_caja is not null ) ";
		
		String consultaCierreCajas= "SELECT " +
									"c.codigo ||' '|| c.descripcion AS descripcioncaja, " +
									"cc.cajero AS loginusuariocajero, " +
									"getnombrepersona(u.codigo_persona) ||' ['|| cc.cajero ||']' AS descripcioncajero, " +
									"ta.codigo AS codigotipoarqueo, " +
									"ta.descripcion AS descripciontipoarqueo, " +
									"cc.consecutivo AS consecutivoasociado, " +
									"to_char(cc.fecha_cerrada, 'DD/MM/YYYY') as fechaarqueadaocerrada, " +
									"cc.fecha_cerrada as fechaarqueadaocerrada_bd, " +
									"'"+ConstantesBD.codigoFuncionalidadTipoArqueoCierre+"' AS codigofuncionalidad," +
									"cc.caja as consecutivocaja, " +
									"CASE WHEN cc.caja_ppal IS NULL THEN '' ELSE cc.caja_ppal||'' END as consecutivocajappal, " +
									"CASE WHEN cc.traslado_caja IS NULL THEN '' ELSE cc.traslado_caja||'' END AS trasladocaja " +
									"FROM " +
									"cierres_cajas cc " +
									"INNER JOIN usuarios u ON (cc.cajero=u.login) " +
									"INNER JOIN cajas c ON (c.consecutivo=cc.caja) " +
									"INNER JOIN tipos_arqueos ta ON (ta.codigo=cc.tipo_arqueo) " +
									"WHERE cc.institucion="+codigoInstitucion+" ";
		
		String consultaDefinitiva="";
		String orderBy=" ORDER BY descripcioncaja, descripcioncajero, descripciontipoarqueo, consecutivoasociado ";
		boolean esBusquedaXArqueoDefinitivo=false, esBusquedaXCierreCaja=false;
		
		//primero se evalua que tipo de arqueo es ambos-> ambos 2->arqueo definitivo 3->cierre caja  
		if(codigoTipoArqueoStr.equals("ambos"))
		{
			esBusquedaXArqueoDefinitivo=true;
			esBusquedaXCierreCaja=true;
		}
		else if(codigoTipoArqueoStr.equals(ConstantesBD.codigoTipoArqueoDefinitivo+""))
			esBusquedaXArqueoDefinitivo=true;
		else if(codigoTipoArqueoStr.equals(ConstantesBD.codigoTipoArqueoCierreCaja+""))
			esBusquedaXCierreCaja=true;
		
		if(esBusquedaXArqueoDefinitivo)
		{
			consultaArqueosDefinitivos+=" AND (ad.fecha_arqueada >= '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicialConsultaArqueosCierres)+"' " +
										" AND ad.fecha_arqueada <= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinalConsultaArqueosCierres)+"') ";
			
			if(!loginUsuarioCajero.equals(""))
				consultaArqueosDefinitivos+=" AND ad.cajero = '"+loginUsuarioCajero+"' ";
			if(!consecutivoCaja.equals(""))
				consultaArqueosDefinitivos+=" AND ad.caja= '"+consecutivoCaja+"' ";
			
			if(codigoCentroAtencion>0)
				consultaArqueosDefinitivos+=" AND c.centro_atencion="+codigoCentroAtencion+" ";
			
			consultaDefinitiva+=" ("+consultaArqueosDefinitivos+") ";
			
			if(esBusquedaXCierreCaja)
				consultaDefinitiva+=" UNION ";
		}
		if(esBusquedaXCierreCaja)
		{
			consultaCierreCajas+=	" AND (cc.fecha_cerrada >= '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicialConsultaArqueosCierres)+"' " +
									" AND cc.fecha_cerrada <= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinalConsultaArqueosCierres)+"') ";
			
			if(!loginUsuarioCajero.equals(""))
				consultaCierreCajas+=" AND cc.cajero = '"+loginUsuarioCajero+"' ";
			if(!consecutivoCaja.equals(""))
				consultaCierreCajas+=" AND cc.caja= '"+consecutivoCaja+"' ";
			
			if(codigoCentroAtencion>0)
				consultaCierreCajas+=" AND c.centro_atencion="+codigoCentroAtencion+" ";
			
			if(!consecutivoCajaPpal.equals(""))
				consultaCierreCajas+=" AND cc.caja_ppal = '"+consecutivoCajaPpal+"' ";
			
			consultaDefinitiva+=" ("+consultaCierreCajas+") ";
		}
		
		consultaDefinitiva+=orderBy;
		
		logger.info("consulta-->"+consultaDefinitiva);
		
		try
		{
			if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            }
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaDefinitiva,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            coleccion=UtilidadBD.resultSet2Collection(rs);
        }
		catch (SQLException sqle) 
		{
			logger.error("Error en busquedaArqueosCierres de SqlBaseArqueosDao ");
			sqle.printStackTrace();
		}
		return coleccion;
	}
	
	/**
	 * Metodo que consulta el resumen de un cierre de caja
	 * @param con
	 * @param codigoInstitucion
	 * @param consecutivo
	 * @return
	 */
	public static ResultSetDecorator resumenCierreCaja(Connection con, int codigoInstitucion, String consecutivo)
	{
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(resumenCierreCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			ps.setString(2, consecutivo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			return rs;
		}
		catch (SQLException e) 
		{
			logger.error("Error en el resumenCierreCaja "+e.toString());
			return null;
		}
	}
	
	/**
	 * Metodo que consulta el resumen de un arqueo definitivo
	 * @param con
	 * @param codigoInstitucion
	 * @param consecutivo
	 * @return
	 */
	public static ResultSetDecorator resumenArqueoDefinitivo(Connection con, int codigoInstitucion, String consecutivo)
	{
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(resumenArqueoDefinitivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			ps.setString(2, consecutivo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			return rs;
		}
		catch (SQLException e) 
		{
			logger.error("Error en el resumenArqueoDefinitivo "+e.toString());
			return null;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaArqueoDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @return
	 */
	public static ArrayList<Integer> estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha(
			Connection con, int codigoInstitucion, String fechaArqueoDDMMYYYY,
			String loginUsuarioCajero, String consecutivoCaja)
	{
		ArrayList<Integer> array= new ArrayList<Integer>();
		
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoInstitucion);
            ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaArqueoDDMMYYYY));
            ps.setString(3, loginUsuarioCajero);
            ps.setString(4, consecutivoCaja);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            while(rs.next())
            {	
            	array.add(rs.getInt(1));
            }
            rs.close();
            ps.close();
       }
		catch (SQLException sqle) 
		{
			logger.error("Error en estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha de SqlBaseConsultarecibosCaja", sqle);
			sqle.printStackTrace();
			return new ArrayList<Integer>();
		}
		return array;
	}
	
}