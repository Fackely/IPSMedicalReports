
/*
 * Creado   29/11/2004
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

import util.ConstantesBD;
import util.ValoresPorDefecto;


import com.princetonsa.dao.DaoFactory;

/**
 * Implementaci�n sql gen�rico de todas las funciones 
 * de acceso a la fuente de datos
 *
 * @version 1.0, 29/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseMedicosXPoolDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseMedicosXPoolDao.class);
	
	/**
	 * almacena la consulta de los medicos que pertenecen a un pool
	 */
	private static String consultaMedicosPoolesStr = "SELECT " +
																"pp.codigo as codigo, " +
																"pp.pool as pool, " +
																"pp.medico as medico, " +
																"to_char(pp.fecha_ingreso, 'yyyy-mm-dd') as fechaIngreso, " +
																"pp.hora_ingreso as horaIngreso, "+
																"to_char(pp.fecha_retiro,'yyyy-mm-dd') AS fechaRetiro, " +
																"pp.hora_retiro as horaRetiro, "+
																"pp.porcentaje_participacion as porcentaje, " +
																"per.primer_nombre as primerNombre, " +
																"per.segundo_nombre as segundoNombre, " +
																"per.primer_apellido as primerApellido, " +
																"per.segundo_apellido as segundoApellido " +
																"FROM participaciones_pooles  pp "+
																"INNER JOIN personas per ON (per.codigo = pp.medico) ";
																
	
	/**
	 * almacena el filtro para consultar por un tipo de pool
	 */
	private static String consultaMedicoXPoolXCodigoStr = "where pp.pool = ? ORDER BY per.primer_apellido ASC";
	
	
	
	/**
	 * Metodo que realiza la consulta de uno � varios MedicosXPool.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPool, Codigo del tipo de pool.
	 * @param consultaUno, Boolean indica que tipo de consulta se realiza.
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.MedicosXPoolDao#consultaPooles(java.sql.Connection,int,boolean)
	 */
	public static ResultSetDecorator consultaPooles (Connection con, int codigoPool,boolean consultaUno)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        if(consultaUno)
	        {
	            ps=  new PreparedStatementDecorator(con.prepareStatement(consultaMedicosPoolesStr + consultaMedicoXPoolXCodigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ps.setInt(1,codigoPool);	                  
	        }
	       	       	       
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta de MedicosXPool, con el codigo->"+codigoPool+" "+": SqlBaseMedicosXPoolDao "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * Consulta Avanzada, segun los parametros recibidos
	 * @param con, Connection con la fuente de datos
	 * @param fechaIngreso, fecha de ingreso
	 * @param fechaRetiro, fecha de retiro
	 * @param medico, codigo del medico
	 * @param pool, codigo del pool
	 * @param porcentaje, porcentaje de participaci�n
	 * @return
	 * @see com.princetonsa.dao.MedicosXPoolDao#consultaPoolesAvanzada(java.sql.Connection,String,String,int,int,double)
	 */
	public static ResultSetDecorator consultaPoolesAvanzada (Connection con, 
			        												String fechaIngreso,
															        String fechaRetiro,
															        int medico, 
															        int pool, 
															        double porcentaje)
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
	        String avanzadaStr = "";
	        String filtro = " WHERE ";
	        String filtro2 = " pool = '"+pool+"'";
	        
	        if(!fechaIngreso.equals(""))
			{	
			    avanzadaStr+=" fecha_ingreso = '"+fechaIngreso+"'";
			    esPrimero=false;
			}	
		    if(!fechaRetiro.equals(""))
			{	
			    if(!esPrimero)
			        avanzadaStr+=" AND ";
			    
			    esPrimero=false;
			    avanzadaStr+=" fecha_retiro = '"+fechaRetiro+"'";
			}
		    if(porcentaje != 0.0)
			{	
			    if(!esPrimero)
			        avanzadaStr+=" AND ";
			    
			    esPrimero=false;
			    avanzadaStr+=" porcentaje_participacion = '"+porcentaje+"'";
			}	
		    if(medico != 0 && medico != ConstantesBD.codigoNuncaValido)
			{	
			    if(!esPrimero)
			        avanzadaStr+=" AND ";
			    
			    esPrimero=false;
			    avanzadaStr+=" medico = '"+medico+"'";
			}
		    if(!esPrimero)
		    	avanzadaStr+="AND "+filtro2;
		    else
		    	avanzadaStr+=filtro2;
		    
		    ps=  new PreparedStatementDecorator(con.prepareStatement(consultaMedicosPoolesStr + filtro + avanzadaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta Avanzada de MedicosXPool, con el codigo->"+pool+" "+": SqlBaseMedicosXPoolDao "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 *  Inserta los datos de uno � varios m�dicos por pool
	 * @param con, Connection con la fuente de datos
	 * @param fechaIngreso, fecha de ingreso del medico al pool
	 * @param fechaRetiro, fecha de retiro del medico al pool
	 * @param horaIngreso, hora de ingreso del medico al pool
	 * @param horaRetiro, hora de retiro del medico al pool
	 * @param medico, c�digo del m�dico
	 * @param pool, c�digo del pool
	 * @param porcentaje, porcentaje de participaci�n
	 * @see com.princetonsa.dao.MedicosXPoolDao#insertarMedicoPool(java.sql.Connection,String,String,String,String,int,int,double)
	 * @return siInserto, boolean true efectivo, false de lo contrario
	 */
	public static boolean insertarMedicoPool (Connection con,
													        String fechaIngreso,
													        String fechaRetiro,
													        String horaIngreso,
													        String horaRetiro,
													        int medico, 
													        int pool, 
													        double porcentaje,
													        String insertarMedicosXPoolStr)
	{
	    
	   boolean siInserto = false;
	   int resp = 0;
	   
	   horaIngreso = ""+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+"";//no es requerida aun
	   horaRetiro = "0:00";//no es requerida aun
	   
	   try
		  {
		      if (con == null || con.isClosed()) 
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
		      
		      PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarMedicosXPoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		      logger.info("------------------------->>>>>>>"+insertarMedicosXPoolStr);
		      
		      ps.setString(1,fechaIngreso);
		      
		      if(fechaRetiro.equals(""))
		          ps.setObject(2,null);
		      else
		          ps.setString(2,fechaRetiro);
		      if(horaRetiro.equals(""))
		    	  ps.setObject(3,null);
		      else
		    	  ps.setString(3,horaRetiro);
		      ps.setInt(4,medico);
		      ps.setInt(5,pool);
		      ps.setDouble(6,porcentaje);
		      
		      resp=ps.executeUpdate();
		      
		      if(resp < 0)
		          siInserto = false;
		      else
		          siInserto = true;
		      
		  }
	    catch (SQLException e)
	    {
	        logger.warn(e+" Error en la inserci�n de datos: SqlBaseParamInstitucionDao "+e.toString() );
		    resp=0;
	    }

	    return siInserto;
	}
	
	/**
	 * Modifica uno � varios registros de m�dicos x pool
	 * @param con, Connection con la fuente de datos
	 * @param medico, c�digo del m�dico
	 * @param pool, c�digo del pool
	 * @param fechaIngreso, fecha de ingreso del medico al pool
	 * @param fechaRetiro, fecha de retiro del medico al pool
	 * @param porcentaje, porcentaje de participaci�n
	 * @see com.princetonsa.dao.MedicosXPoolDao#insertarMedicoPool(java.sql.Connection,int,int,String,String,double)
	 * @return boolean, true si modifico, false de lo contrario
	 */
	public static boolean modificar (Connection con,int medico,int pool,String fechaIngreso,String fechaRetiro,double porcentaje)
	{
	    int resp = 0;
	    boolean esPrimero=true;
	    
	    String modificarStr="UPDATE participaciones_pooles SET ";
	    
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en modificar SqlBaseMedicosXPoolDao : Conexi�n cerrada");
			}
			
		    if(!fechaIngreso.equals(""))
			{	
			    modificarStr+=" fecha_ingreso = '"+fechaIngreso+"'";
			    esPrimero=false;
			}	
		    if(!fechaRetiro.equals("") || fechaRetiro.equals(""))
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    
			    if(fechaRetiro.equals(""))
			        modificarStr+=" fecha_retiro = null";
			    else
			        modificarStr+=" fecha_retiro = '"+fechaRetiro+"'";					
			}
		    // Tarea oid=3112
		    //if(porcentaje != 0.0)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" porcentaje_participacion = "+porcentaje+"";					
			}	
		    
		    modificarStr+=" WHERE medico = '"+medico+"' AND pool = '"+pool+"'";
		    PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
		    logger.info("-------------"+modificarStr);
			resp=ps.executeUpdate();
			
			if(resp>0)
				return true;
			else
				return false;
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en la modificaci�n de datos: SqlBaseMedicosXPoolDao "+e.toString());
			return false;			
		}	
	}
}
