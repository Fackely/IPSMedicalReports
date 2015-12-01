/*
 * Abr 16, 2007
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * @author Sebasti�n G�mez
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad FORMAS FARMAC�UTICAS
 *
 */
public class SqlBaseFormaFarmaceuticaDao
{

	/**
	 * Cadena para consultar una forma Farmaceutica
	 */
	private static final String consultarFormaFarmaceutica="SELECT " +
			"ct.acronimo as consecutivo," + 
			"ct.institucion as institucion," +
			"ct.nombre as descripcion " + 
			"FROM forma_farmaceutica ct " +
			"WHERE ct.acronimo=? and institucion = ?";
	
	
	 /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger( SqlBaseFormaFarmaceuticaDao.class);
	
	
	/**
	 * Cadena para consultar las cajas de la BD
	 */
	private static final String consultarFormasFarmaceuticas="SELECT " +
		"ct.acronimo as consecutivo, " +
		"ct.institucion as institucion, " +
		"ct.nombre as descripcion " +
		"from forma_farmaceutica ct " +
		"where ct.institucion=? order by ct.nombre "; 
	
	/**
	 * Cadena para eliminar un registro.
	 */
	private static final String eliminarRegistro="DELETE from forma_farmaceutica where acronimo = ? and institucion = ?";
	
	/**
	 * Cadena para consultar si un registro fue modificado o no.
	 */
	private static final String cadenaExisteModificacion="select acronimo as consecutivo from forma_farmaceutica where acronimo=? AND institucion=? AND nombre=?";//??? //and codigo=? and nombre=? and color=?";
	
	
	/**
	 * Cadena para modificar una forma farmaceutica
	 */
	private static final String modificarRegistro="UPDATE forma_farmaceutica " +
			"SET acronimo=?, " + 
			"nombre=?" +
			"WHERE acronimo=? "+
			" AND institucion = ?"; 

 
	/**
	 * 
	 * M�todo que carga todas las formas farmaceuticas parametrizadas por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarInformacion(Connection con, int institucion) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultarFormasFarmaceuticas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en cargarInformacion de Formas Farmac�uticas "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * 
	 * M�todo implementado para eliminar una forma farmac�utica junto con sus vias de administracion relacionadas 
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, String consecutivo,int institucion) 
	{
		try
		{
			PreparedStatementDecorator ps = null;
			eliminarRelacionesFormasFarmViasAdmin(con,consecutivo,institucion);
			ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * DELETE from forma_farmaceutica where acronimo = ? and institucion = ?
			 */
			
			ps.setString(1,consecutivo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error eliminarRegistro de forma farmac�utica "+e.toString() );
			return false;
		}
	}

	/**
	 * M�todo que elimina las vias de administracion relacionadas a una forma farmaceutica
	 * @param con
	 * @param codigo
	 * @param institucion 
	 * @return
	 */
	public static boolean eliminarRelacionesFormasFarmViasAdmin(Connection con,String codigo, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			ps= new PreparedStatementDecorator(con.prepareStatement(
				"delete from vias_admin_forma_farmac where forma_farmaceutica='"+codigo + "' and institucion = "+institucion,
				ConstantesBD.typeResultSet,
				ConstantesBD.concurrencyResultSet ));
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en eliminarRelacionesFormasFarmViasAdmin "+e.toString() );
			return false;
		}
		
	}
	
	/**
	 * M�todo que verifica si una forma farmac�utica fue modificada
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 * @param descripcion
	 * @return
	 */
	public static boolean existeModificacion(Connection con, String consecutivo, int institucion,String descripcion) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaExisteModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        /**
	         * select acronimo as consecutivo from forma_farmaceutica where acronimo=? AND institucion=? AND nombre=?
	         */
	        
	        ps.setString(1,consecutivo);
	        ps.setInt(2,institucion);
	        ps.setString(3,descripcion); 
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error revisando la existencia de modificaciones: "+e.toString() );
	        return false;
	    }
	}

	/**
	 * M�todo implementado para consultar la informacion de una forma farmaceutica
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarFormaFarmaceutica(Connection con, String consecutivo,int institucion)
	{
		try
	    {
			
			PreparedStatementDecorator ps = null;
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultarFormaFarmaceutica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1,consecutivo);
	        ps.setInt(2,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en cargarFormaFarmaceutica:  "+e.toString() );
		   return null;
	    }
	}

	/**
	 * M�todo implementado para modificar la informacion de formas farmac�uticas
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param institucion
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, String consecutivo, String descripcion, int institucion) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(modificarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1,consecutivo);
	        ps.setString(2,descripcion);
	        ps.setString(3,consecutivo);
	        ps.setInt(4,institucion);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en modificarRegistro: "+e.toString() );
		   return false;
	    }
	}

	/**
	 * M�todo que inserta una nueva forma farmaceutica
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion) 
	{
		try
	    {
			String cadena = "insert into forma_farmaceutica (acronimo,nombre, institucion) values (?,?,?)";
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1,codigo);
	        ps.setString(2,descripcion);
	        ps.setInt(3,institucion);	        

	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en insertarRegistro: "+e.toString() );
		   return false;
	    }
	}

	/**
	 * M�todo que actualiza las relaciones de vias de adminstracion con su respectiva forma farmac�utica
	 * @param con
	 * @param formaFarma
	 * @param institucion
	 * @param viasAdmin(HashMap)
	 * @return
	 */
	public static boolean actualizarRelacionesFormasFarmaViasAdmin(Connection con, String formaFarma,int institucion, HashMap viasAdmin) 
	{
		String dest="";
		//Se toma el n�mero de vias de adminsnitracion de la forma farmac�utica
		int numRegistros = Utilidades.convertirAEntero(viasAdmin.get("numRegistros").toString());
		
		//Se verifica si no hay vias de administracion
		if(numRegistros==0)
		{
			return  eliminarRelacionesFormasFarmViasAdmin(con,formaFarma,institucion);
		}
		else
		{
		
			//Se listan por coma las vias de administracion
			for(int i=0;i<numRegistros;i++)
			{
				if(i>0)
					dest=dest+",";
				dest=dest+viasAdmin.get("viaAdmin_"+i).toString();
			}
			try
			{
				if(!dest.trim().equals(""))
				{
					String consulta = "delete from vias_admin_forma_farmac where forma_farmaceutica='"+formaFarma +"' and institucion = "+institucion+" and via_admin not in ("+dest+")";
					PreparedStatementDecorator ps = null;
					ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.executeUpdate();
				}
			}
			catch(SQLException e)
			{
				logger.warn("Error en actualizarRelacionesFormasFarmaViasAdmin: "+e.toString() );
			}
			return insertarRelacionesFormaFarmaViasAdmin(con,formaFarma,institucion,viasAdmin);
		}
	}

	/**
	 * 
	 * M�todo que inserta las vias de adminsitracion de una forma farmac�utica
	 * @param con
	 * @param formaFarma
	 * @param institucion 
	 * @param viasAdmin
	 * @return
	 */
	public static boolean insertarRelacionesFormaFarmaViasAdmin(Connection con, String formaFarma, int institucion, HashMap viasAdmin) 
	{
		for(int i=0;i<Utilidades.convertirAEntero(viasAdmin.get("numRegistros").toString());i++)
		{
			try
		    {
		        if(!existeRelacionFormaFarmaViaAdmin(con,formaFarma,institucion,Utilidades.convertirAEntero(viasAdmin.get("viaAdmin_"+i).toString())))
		        {
		        	PreparedStatementDecorator ps = null;
		        	ps=  new PreparedStatementDecorator(con.prepareStatement("insert into vias_admin_forma_farmac (forma_farmaceutica ,via_admin ,institucion)  values(?,?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			        ps.setString(1, formaFarma); 
			        ps.setInt(2,Utilidades.convertirAEntero(viasAdmin.get("viaAdmin_"+i).toString()));
			        ps.setInt(3, institucion);
			        ps.executeUpdate();
		        }
		        
		    }
		    catch(SQLException e)
		    {
		        logger.warn("Error en insertarRelacionesFormaFarmaViasAdmin: "+e.toString() );
			   return false;
		    }
		}
        return true;
	}
	
	

	/**
	 * M�todo que verifica si una via de adminsitracion y una forma Farmac�utica est�n relacionadas
	 * @param con
	 * @param formaFarma
	 * @param institucion
	 * @param viaAdmin 
	 * @return
	 */
	private static boolean existeRelacionFormaFarmaViaAdmin(Connection con, String formaFarma, int institucion, int viaAdmin) 
	{
		try
		{
			PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement(
				"SELECT count(1) as resultado from vias_admin_forma_farmac where forma_farmaceutica='"+formaFarma+"' and institucion = "+institucion+" and via_admin="+viaAdmin,
				ConstantesBD.typeResultSet,
				ConstantesBD.concurrencyResultSet ));
			ResultSetDecorator rs=new ResultSetDecorator(stm.executeQuery());
			if(rs.next())
			{
				return rs.getInt("resultado")>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en existeRelacionFormaFarmaViaAdmin: "+e);
		}
		return false;
	}

	

	/**
	 * M�todo que carga las vias de administracion de una forma farmac�utica
	 * @param con
	 * @param consecutivo
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarRelacionesViasAdminFormaFarma(Connection con, String consecutivo,int institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
	    {
	        PreparedStatementDecorator ps = null;
	        String consulta="select DISTINCT " +
	        	"cd.via_admin as via_Admin," +
	        	"case  when t.via_admin is null then 'false' else 'true' end  as usado " +
	        	"from vias_admin_forma_farmac cd " +
	        	"left outer join inventarios.vias_admin_articulo t on (cd.via_admin=t.via_admin and cd.forma_farmaceutica=t.forma_farmaceutica)  " +
	        	"where " +
	        	"cd.forma_farmaceutica='"+consecutivo+"'  and  " +
	        	"cd.institucion = " +institucion +" ";
	        	//"group by 1,2,3 ";
	        
	        logger.info("consulta-->"+consulta);
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        mapa=(HashMap)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true).clone();
	    }
	    catch(SQLException e)
	    {
	    	logger.warn("Error en cargarRelacionesViasAdminFormaFarma: "+e.toString() );
	    }	
        return mapa;
	}

}
