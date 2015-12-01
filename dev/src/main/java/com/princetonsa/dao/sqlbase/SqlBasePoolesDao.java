/*
 * Creado   16/12/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Types;

import com.ibm.icu.util.ULocale.Type;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementaci�n sql gen�rico de todas las funciones 
 * de acceso a la fuente de datos
 *
 * @version 1.0, 16/12/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBasePoolesDao 
{
	
    /**
	* Objeto para manejar los logs de esta clase
	*/
	public static Logger logger = Logger.getLogger(SqlBasePoolesDao.class);
	
	/**
	 * almacena la consulta de pooles 
	 */
	private static final String consultaPooles = "SELECT " +
															"pol.codigo as codigo, " +
															"pol.descripcion as descripcion, " +
															"pol.tercero_responsable as tercero, " +
															"pol.activo as activo, " +
															"ter.numero_identificacion as nit, " +
															"ter.descripcion as nombreResponsable, " +
															"coalesce (pol.dias_vencimiento_fact||'','') as diasVencimiento, " +
															"coalesce (pol.cuenta_x_pagar||'','') as cuentaXPagar, " +
															"coalesce (cc.anio_vigencia||' - '||cc.cuenta_contable,'') as descCuentaXPagar " +
															"FROM pooles pol " +
															"INNER JOIN terceros ter ON (ter.codigo = pol.tercero_responsable AND ter.institucion = ?) "+
															"LEFT OUTER JOIN interfaz.cuentas_contables cc ON (cc.codigo = pol.cuenta_x_pagar) " +
															"ORDER BY pol.descripcion ASC ";
	
	
	private static final String consultaPool = "SELECT " +
													"pol.codigo as codigo, " +
													"pol.descripcion as descripcion, " +
													"pol.tercero_responsable as tercero, " +
													"pol.activo as activo, " +
													"ter.numero_identificacion as nit, " +
													"ter.descripcion as nombreResponsable, " +
													"coalesce (pol.dias_vencimiento_fact||'','') as diasVencimiento, " +
													"coalesce (pol.cuenta_x_pagar||'','') as cuentaXPagar, " +
													"coalesce (cc.anio_vigencia||' - '||cc.cuenta_contable,'') as descCuentaXPagar " +
													"FROM pooles pol " +
													"INNER JOIN terceros ter ON (ter.codigo = pol.tercero_responsable AND ter.institucion = ?) "+
													"LEFT OUTER JOIN interfaz.cuentas_contables cc ON (cc.codigo = pol.cuenta_x_pagar) " +
													"where pol.activo=? ORDER BY pol.descripcion ASC ";
													
	/**
	 * consulta de terceros
	 */
	private static final String consultaTerceros = "SELECT " +
																	"codigo as codigo, " +
																	"numero_identificacion as numeroIdentificacion, " +
																	"'' as tipoIdentificacion, " +
																	"descripcion as descripcion, " +
																	"0 as tipoRetencion, "+
																	"institucion as institucion, "+
																	"activo as activo "+
																	"FROM terceros";
	
	
	/**
	 * almacena la consulta para verificar la relacion del pool con la tabla
	 * de MedicosXPool
	 */
	private static final String existeRelacionMedicosXPoolStr = "SELECT " +
																			"codigo " +
																			"FROM participaciones_pooles " +
																			"WHERE pool = ?";
	/**
	 * almacena la consulta de los codigo que fueron modificados,
	 * insertados o eliminados
	 */
	public static final String consultaResumenStr = "SELECT " +
																"codigo as codigo, " +
																"descripcion as descripcion, " +
																"tercero_responsable as tercero, " +
																"activo as activo " +
																"FROM pooles ";		
	
	/**
	 * Query eliminar pool
	 */
	public static final String deleteQuery = "DELETE FROM pooles WHERE codigo = ?"; 
	
	/**
	 * Almacena la consulta Avanzada para pooles
	 */
	public static final String consultaAvanzadaStr = "SELECT " +
																"pol.codigo as codigo, " +
																"pol.descripcion as descripcion, " +
																"pol.tercero_responsable as tercero, " +
																"pol.activo as activo, " +
																"coalesce (pol.dias_vencimiento_fact||'','') as diasVencimiento, " +
																"coalesce (pol.cuenta_x_pagar||'','') as cuentaXPagar, " +
																"ter.numero_identificacion as nit, " +
																"ter.descripcion as nombreResponsable " +																
																"FROM pooles pol ";
	
	
	/**
	 * Metodo que realiza la consulta de uno � varios pooles.
	 * @param con, Connection con la fuente de datos.
	 * @param institucion, Codigo de la instituci�n.	 
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.PoolesDao#consultaPooles(java.sql.Connection,int)
	 */
	public static ResultSetDecorator consultaPooles (Connection con, int institucion)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consultaPooles,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);	                  
	        
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en consultaPooles, con el codigo institucion->"+institucion+" "+": [clase: SqlBasePoolesDao] "+e.toString() );
		   return null;
	    }
	}
	
	
	
	/**
	 * Metodo que realiza la consulta de uno � varios pooles.
	 * @param con, Connection con la fuente de datos.
	 * @param codigos,int[] con los c�digos a consultar	 
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.PoolesDao#consultaResumen(java.sql.Connection,int)
	 */
	public static ResultSetDecorator consultaResumen (Connection con, int[] codigos)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        String consulta =consultaResumenStr + " WHERE pol.codigo IN ( ";
	        String codigosStr= "",coma=" , ";
	        	        
	        boolean esPrimero = true; 
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        for(int i=0;i<codigos.length;i++)
	        {
	            if(!esPrimero)
	              codigosStr+=coma;
	            
	            codigosStr+=codigos[i];
	            esPrimero = false;
	        }
	        
	        consulta+=codigosStr + " ) ";
	        
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	               	       	       	       
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en consultaResumen : [clase: SqlBasePoolesDao] "+e.toString() );
		   return null;
	    }
	}
	
	
	/**
	 * Metodo que realiza la consulta de terceros
	 * @param con, Connection con la fuente de datos.
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.PoolesDao#consultaTerceros(java.sql.Connection)
	 */
	public static ResultSetDecorator consultaTerceros (Connection con)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consultaTerceros,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en consultaPooles [clase: SqlBasePoolesDao] "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * Metodo que realiza la consulta para verificar si el pool
	 * se encuentra relacionado con la funcionalidad de MedicosXPool 
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPool, int codigo del Pool
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.PoolesDao#existePoolToMedicosXPool(java.sql.Connection)
	 */
	public static ResultSetDecorator existePoolToMedicosXPool (Connection con, int codigoPool)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionMedicosXPoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigoPool);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en existePoolToMedicosXPool [clase: SqlBasePoolesDao] "+e.toString() );
		   return null;
	    }
	}

	/**
	 * Metodo para insertar un Pool
	 * @param diasVencFactura 
	 * @param cuentaXpagar 
	 * @param con, Connection con la fuente de datos
	 * @param descripcion, String descripci�n del pool
	 * @param responsable, int c�digo del responsable (tabla terceros)
	 * @param activo, int 1 activo, 0 de lo contrario
	 * @return boolean, true efectivo, false de lo contrario
	 * @see com.princetonsa.dao.PoolesDao#insertarPool(java.sql.Connection,String,int,int)
	 */
	public static boolean insertarPool (Connection con,String descripcion,int responsable,int activo, int diasVencFactura, int cuentaXpagar, String insertQuery)
	{
	    boolean siInserto = false;
	    int resp = 0;
	    
	    try
		  {
		      if (con == null || con.isClosed()) 
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
		      logger.info("*****************************************"+insertQuery);
		      
		      PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		      
		      ps.setString(1,descripcion);
		      ps.setInt(2,responsable);
		      ps.setInt(3,activo);
		      if (diasVencFactura == ConstantesBD.codigoNuncaValido)
		    	  ps.setNull(4,Types.INTEGER);
		      else
		    	  ps.setInt(4, diasVencFactura);
		      
		      if (cuentaXpagar == ConstantesBD.codigoNuncaValido)
		    	  ps.setNull(5,Types.INTEGER);
		      else
		    	  ps.setInt(5, cuentaXpagar);
		      
		      resp=ps.executeUpdate();
		      
		      if(resp < 0)
		          siInserto = false;
		      else
		          siInserto = true;
		  }
		    catch (SQLException e)
		    {
		        logger.warn(e+" Error en la insercion de datos: SqlBasePoolesDao "+e.toString() );
			    resp=0;
		    }
	    
	    return siInserto;
	}
	
	/**
	 * Eliminar pooles
	 * @param con, Connection con la fuente de datos
	 * @param codigoPool, c�digo del pool a eliminar	 
	 * @return boolean, true si es efectivo, false de lo contrario
	 * @see com.princetonsa.dao.PoolesDao#eliminar(java.sql.Connection,int)
	 */
	public static boolean eliminar(	Connection con, 
	        								int codigoPool) 
	{
		int resp=0;	
		boolean siElimino = false;
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(deleteQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1, codigoPool);
					
			resp=ps.executeUpdate();
			

		      if(resp < 0)
		          siElimino = false;
		      else
		          siElimino = true;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en eliminar de datos: SqlBasePoolesDao "+e.toString());
			siElimino = false;			
		}	
		return siElimino;	
	}
	
	/**
	 * Modifica uno � varios registros de pooles
	 * @param diasVencFactura 
	 * @param cuentaXpagar 
	 * @param descripcion, String nombre del pool
	 * @param activo, int estado del registro 1 activo, 0 de lo contrario
	 * @param responsable, int codigo del tercero
	 * @param codigoPool, int codigo del pool
	 * @see com.princetonsa.dao.PoolesDao#modificar(java.sql.Connection,String,int,int)
	 * @return boolean, true si modifico, false de lo contrario
	 */
	public static boolean modificar (Connection con,String descripcion,int activo,int tercero,int codigoPool, int diasVencFactura, int cuentaXpagar)
	{
	    int resp = 0;
	    boolean esPrimero=true;
	    
	    String modificarStr="UPDATE pooles SET ";
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en modificar SqlBasePoolesDao : Conexi�n cerrada");
			}
			
		    if(!descripcion.equals(""))
			{	
			    modificarStr+=" descripcion = '"+descripcion+"'";
			    esPrimero=false;
			}	
		     if(activo != -1)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" activo = '"+activo+"'";					
			}
		     
		    if(tercero != -1)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" tercero_responsable = '"+tercero+"'";					
			}
		    
		    if(diasVencFactura != -1)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" dias_vencimiento_fact = "+diasVencFactura+"";					
			}
		    else
		    {
		    	if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
		    	modificarStr+=" dias_vencimiento_fact = NULL";
		    }
		    
		    if(cuentaXpagar != -1)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    
			    modificarStr+=" cuenta_x_pagar = "+cuentaXpagar+"";					
			}
		    else
		    {
		    	if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
		    	modificarStr+=" cuenta_x_pagar = NULL";
		    }
		    
		    modificarStr+=" WHERE codigo = '"+codigoPool+"' ";
		    PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
			resp=ps.executeUpdate();
			
			if(resp>0)
				return true;
			else
				return false;
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en la modificaci�n de datos: SqlBasePoolesDao "+e.toString());
			return false;			
		}	
	}
	
	/**
	 * Consulta Avanzada, segun los parametros recibidos
	 * @param con, Connection con la fuente de datos
	 * @param descripcion, String con el nombre del pool
	 * @param nombreResponsable, String con el nombre del responsable
	 * @param nit, String con el n�mero de identificaci�n
	 * @param activo, int 1 si es activo, 0 de lo contrario
	 * @return ResultSet, con el resultado de la consulta
	 * @see com.princetonsa.dao.PoolesDao#consultaPoolesAvanzada(java.sql.Connection,String,String,int,int,double)
	 */
	public static ResultSetDecorator consultaPoolesAvanzada (Connection con, 
			        												String descripcion,
															        String nombreResponsable,
															        String nit, 
															        int activo )
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        boolean esPrimero=true;
	        boolean esFiltro = false;
	        String avanzadaStr = "";
	        String filtro = " WHERE ";
	        String inner = " INNER JOIN terceros ter ON ( pol.tercero_responsable = ter.codigo ";
	        String cerrarInner = " )";
	        String order = " ORDER BY pol.descripcion ASC ";
	        
	        if(!descripcion.equals(""))
			{	
			    avanzadaStr+=" UPPER(pol.descripcion) LIKE UPPER ('%"+descripcion+"%')";
			    esPrimero=false;
			    esFiltro = true;
			}	
		    if(!nombreResponsable.equals("") && !nombreResponsable.equals("-1"))
			{	
		        inner+= " AND UPPER(ter.descripcion) LIKE UPPER('%"+nombreResponsable+"%') ";		        
			}
		    if(!nit.equals(""))
			{	
		        inner+= " AND ter.numero_identificacion LIKE '%"+nit+"%' ";		        		
			}	
		    if(activo != -1)
			{	
			    if(!esPrimero)
			        avanzadaStr+=" AND ";
			    
			    esPrimero=false;
			    esFiltro = true;
			    avanzadaStr+=" pol.activo = '"+activo+"'";					
			}
		    
		    if(esFiltro)
		        ps=  new PreparedStatementDecorator(con.prepareStatement(consultaAvanzadaStr + inner + cerrarInner + filtro + avanzadaStr + order,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    else
		        ps=  new PreparedStatementDecorator(con.prepareStatement(consultaAvanzadaStr + inner + cerrarInner + order,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
	       return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consultaPoolesAvanzada, [SqlBasePoolesDao] "+e.toString() );
		   return null;
	    }
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public static ArrayList<InfoDatosStr> cargarPooles(final int institucion, boolean activo){
	
		int activoN=ConstantesBD.codigoNuncaValido;
		
		if(activo)
		{
			activoN=1;
		}
		else
		{
			activoN=0;
		}
		
		ArrayList<InfoDatosStr> listaPooles = new ArrayList<InfoDatosStr>();
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaPool);
		    ps.setInt(1,institucion);
		    ps.setInt(2,activoN);
		   
	    	logger.info("\n\n\n\n\n SQL Cargar POOLES   --/ "+ps);
		
	    	ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				InfoDatosStr newInfoPool = new InfoDatosStr();
				newInfoPool.setCodigo(rs.getString("codigo"));
				newInfoPool.setNombre(rs.getString("descripcion"));
				listaPooles.add(newInfoPool);
			 }
		 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		return listaPooles;
	}
	
	
}
