/*
 * @author armando
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

/**
 * 
 * @author artotor
 *
 */
public class SqlBaseCategoriasTriageDao 
{

	/**
	 * Cadena para consultar las cajas de la BD
	 */
	private static final String consultarCategoriaTriage="SELECT ct.consecutivo as consecutivo,ct.codigo as codigo,ct.institucion as institucion,ct.nombre as descripcion,ct.color as color from categorias_triage ct where ct.consecutivo=?";
	
	
	 /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger( SqlBaseCategoriasTriageDao.class);
	
	
	/**
	 * Cadena para consultar las cajas de la BD
	 */
	private static final String consultarCategoriasTriage="SELECT " +
		"ct.consecutivo as consecutivo," +
		"ct.codigo as codigo," +
		"ct.institucion as institucion," +
		"ct.nombre as descripcion," +
		"ct.color as color, " +
		"c.nombre as nombrecolor " +
		"from categorias_triage ct " +
		"inner join colores_triage c ON(c.codigo=ct.color) " +
		"where ct.institucion=? order by ct.codigo";
	
	/**
	 * Cadena para eliminar un registro.
	 */
	private static final String eliminarRegistro="DELETE from categorias_triage where consecutivo = ?";
	
	/**
	 * Cadena para consultar si un registro fue modificado o no.
	 */
	private static final String cadenaExisteModificacion="select consecutivo from categorias_triage where consecutivo=? and codigo=? and nombre=? and color=?";
	
	/**
	 * Cadena para modificar los registros de la tabla caja
	 */
	private static final String modificarRegistro="UPDATE categorias_triage SET codigo = ?,nombre=?,color=? where consecutivo=?";


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarInformacion(Connection con, int institucion) 
	{
		PreparedStatementDecorator ps = null;
		try
	    {
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultarCategoriasTriage));
	        ps.setInt(1,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseCategoriasTriageDao "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int consecutivo) 
	{
		PreparedStatementDecorator ps = null;
		try
		{
			eliminarRelacionesCategoriasDestinos(con,consecutivo);
			ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistro));
			ps.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error SqlBaseCategoriasTriageDao "+e.toString() );
			return false;
		}finally{
			if(ps!=null){
				try{
					ps.close();
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
					
				}
			}
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param categoria
	 * @return
	 */
	public static boolean eliminarRelacionesCategoriasDestinos(Connection con,int categoria)
	{
		PreparedStatementDecorator ps = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement("delete from categorias_destinos where categoria="+categoria));
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error SqlBaseCategoriasTriageDao "+e.toString() );
			return false;
		}finally{
			if(ps!=null){
				try{
					ps.close();
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
					
				}
			}
		}
		
	}
	public static boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int color) 
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try
	    {
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaExisteModificacion));
	       
	        /**
	         * select consecutivo from categorias_triage where consecutivo=? and codigo=? and nombre=? and color=?
	         */
	        
	        ps.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
	        ps.setDouble(2,Utilidades.convertirADouble(codigo+""));
	        ps.setString(3,descripcion);
	        ps.setDouble(4,Utilidades.convertirADouble(color+""));
	        rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error revisando la existencia de modificaciones [SqlBaseCategoriasTriageDao] "+e.toString() );
	        return false;
	    }finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
					
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
					
				}
			}
		}
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static ResultSetDecorator cargarCategoriaTriage(Connection con, int consecutivo)
	{
		PreparedStatementDecorator ps = null;
		try
	    {
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultarCategoriaTriage));
	        ps.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta SqlBaseCategoriasTriageDao:  "+e.toString() );
		   return null;
	    }
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, int consecutivo, String codigo, String descripcion, int color) 
	{
		PreparedStatementDecorator ps = null;
		try
	    {
	        ps=  new PreparedStatementDecorator(con.prepareStatement(modificarRegistro));
	        
	        /**
	         * UPDATE categorias_triage SET codigo = ?,nombre=?,color=? where consecutivo=?
	         */
	        
	        ps.setDouble(1,Utilidades.convertirADouble(codigo));
	        ps.setString(2,descripcion);
	        ps.setDouble(3,Utilidades.convertirADouble(color+""));
	        ps.setDouble(4,Utilidades.convertirADouble(consecutivo+""));
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseCategoriasTriageDao "+e.toString() );
		   return false;
	    }finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @param cadena
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, int color, String cadena) 
	{
		PreparedStatementDecorator ps = null;
		try
	    {

	        logger.info(cadena);
	        logger.info(Utilidades.convertirADouble(codigo));
	        logger.info(institucion);
	        logger.info(descripcion);
	        logger.info(Utilidades.convertirADouble(color+""));

	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadena));
	        /**
	         * insert into categorias_triage (consecutivo,codigo,institucion,nombre,color) values ('seq_categorias_triage'),?,?,?,?)
	         */
	        
	        ps.setDouble(1,Utilidades.convertirADouble(codigo));
	        ps.setInt(2,institucion);
	        ps.setString(3,descripcion);
	        ps.setDouble(4,Utilidades.convertirADouble(color+""));
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la insercion cajas: SqlBaseCategoriasTriageDao "+e.toString() );
	        e.printStackTrace();
		   return false;
	    }finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param categoria
	 * @param destinos
	 * @return
	 */
	public static boolean actualizarRelacionesCategoriasDestinos(Connection con, int categoria, HashMap destinos) 
	{
		//eliminarRelacionesCategoriasDestinos(con,categoria);
		String dest="";
		for(int i=0;i<Integer.parseInt(destinos.get("numRegistros").toString());i++)
		{
			if(i>0)
				dest=dest+",";
			dest=dest+destinos.get("destino_"+i).toString();
		}
		PreparedStatementDecorator ps = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement("delete from categorias_destinos where categoria="+categoria +" and destino not in ("+dest+")"));
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error SqlBaseCategoriasTriageDao "+e.toString() );
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}
			
		}
		return insertarRelacionesCategoriasDestinos(con,categoria,destinos);
	}

	/**
	 * 
	 * @param con
	 * @param categoria
	 * @param destinos
	 * @return
	 */
	public static boolean insertarRelacionesCategoriasDestinos(Connection con, int categoria, HashMap destinos) 
	{
		PreparedStatementDecorator ps = null;
		for(int i=0;i<Integer.parseInt(destinos.get("numRegistros").toString());i++)
		{
			try
		    {
		        if(!existeRelacionCategoriaDestino(con,categoria,Integer.parseInt(destinos.get("destino_"+i).toString())))
		        {
		        	ps=  new PreparedStatementDecorator(con.prepareStatement("insert into categorias_destinos values(?,?)"));
			        ps.setDouble(1,Utilidades.convertirADouble(categoria+""));
			        ps.setDouble(2,Utilidades.convertirADouble(destinos.get("destino_"+i).toString()));
			        ps.executeUpdate();
		        }
		        
		    }
		    catch(SQLException e)
		    {
		        logger.warn(e+"Error en la insercion cajas: SqlBaseCategoriasTriageDao "+e.toString() );
			   return false;
		    }finally{
				if(ps!=null){
					try{
						ps.close();
						
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
					}
				}
				
			}
		}
        return true;
	}
	
	

	
	private static boolean existeRelacionCategoriaDestino(Connection con, int categoria, int destino) 
	{
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs=null;
		try
		{
			 stm =  new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) as resultado from categorias_destinos where categoria="+categoria+" and destino="+destino));
			 rs=new ResultSetDecorator(stm.executeQuery());
			if(rs.next())
			{
				return rs.getInt("resultado")>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si la categoria triage es utilizada en categoriaTriageUtilizadaEnTriage "+e);
		}finally{
			if(stm!=null){
				try{
					stm.close();
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}
				if(rs!=null){
					try{
						rs.close();
						
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
					}
				}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public static int obtenerConsecutivoCategoriaTriage(Connection con, String codigo, int institucion, String descripcion, int color) 
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try
	    {
	        ps=  new PreparedStatementDecorator(con.prepareStatement("select consecutivo  as consecutivo from categorias_triage where codigo=? and nombre=? and color=? and institucion=?"));
	        ps.setDouble(1,Utilidades.convertirADouble(codigo));
	        ps.setString(2,descripcion);
	        ps.setDouble(3,Utilidades.convertirADouble(color+""));
	        ps.setInt(4,institucion);
	        rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	        {
	        	return rs.getInt("consecutivo");
	        }
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error revisando la existencia de modificaciones [SqlBaseCategoriasTriageDao] "+e.toString() );
	    }	finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}
				if(rs!=null){
					try{
						rs.close();
						
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
					}
				}
		}
        return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap cargarRelacionesDestionsCategorias(Connection con, String consecutivo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps = null;
		try
	    {
	        String consulta="select cd.destino,case  when t.destino is null then 'false' else 'true' end  as usado from categorias_destinos cd left outer join triage t on (cd.categoria=t.categoria_triage and cd.destino=t.destino)  where categoria="+consecutivo+" group by cd.categoria,cd.destino,t.destino,2";//en donde 2 hace referencia a usado.
	        logger.info(consulta);
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
	        mapa=(HashMap)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error revisando la existencia de modificaciones [SqlBaseCategoriasTriageDao] "+e.toString() );
	    	e.printStackTrace();
	    }	finally{
			if(ps!=null){
				try{
					ps.close();
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}
				
		}
        return mapa;
	}
}
