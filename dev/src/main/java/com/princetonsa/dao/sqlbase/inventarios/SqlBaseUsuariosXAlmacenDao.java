
/*
 * Creado   23/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 *
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseUsuariosXAlmacenDao 
{
    /**
     * manejador de errores de la clase
     */
    public static Logger logger= Logger.getLogger(SqlBaseUsuariosXAlmacenDao.class);    
    /**
     * query para consultar los usuarios por almacen
     */
    public static final String consultaUsuariosXAlmacenStr="SELECT " +
															    		"u.codigo as codigo," +
															    		"u.centros_costo as cod_centros_costo," +
															    		"u.usuario as login_usuario," +
															    		"p.primer_apellido as  primer_apellido, " +
															    		"p.segundo_apellido as segundo_apellido, " +
															    		"p.primer_nombre as  primer_nombre, " +
															    		"p.segundo_nombre as segundo_nombre " +
															    		"from inventarios.usuarios_x_almacen_inv u " +
															    		"inner join administracion.usuarios us on (u.usuario=us.login) " +
															    		"inner join administracion.personas p on(p.codigo=us.codigo_persona) " ;
															    		
    /**
     * query para modificar usuarios por almacen
     */
    public static final String updateUsuariosXAlmacenStr="UPDATE usuarios_x_almacen_inv SET";
    /**
     * query para eliminar usuarios por almacen
     */
    public static final String deleteUsuariosXAlmacenStr="DELETE FROM inventarios.usuarios_x_almacen_inv WHERE codigo=? AND institucion=?";
        
    /**
     * metodo para generar la consulta de
     * usuarios por almacen
     * @param con Connection
     * @param institucion int
     * @param codigo int     
     * @return HashMap
     */
    public static HashMap generarConsulta(Connection con,int institucion,int codigo)
    {
    	// logger.info("\n entre a generarConsulta  -->"+codigo);
        try
        { 
            String query=consultaUsuariosXAlmacenStr+" where u.institucion=? AND u.centros_costo=? ORDER BY primer_apellido,segundo_apellido,primer_nombre,segundo_nombre ";          
            //logger.info("\ncadena -->"+query);
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            ps.setInt(1,institucion);  
            ps.setInt(2,codigo);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());           
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsulta"+e.toString() );		   
	    }
        return null;   
    }
    /**
     * metodo para consultar un solo registro
     * @param con onnection
     * @param institucion int
     * @param codigo int
     * @return HashMap
     */
    public static HashMap generarConsultaRegistro(Connection con,int institucion,int codigo)
    {
        try
        { 
            String query=consultaUsuariosXAlmacenStr+" where u.institucion=? AND u.codigo=? ";               
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            ps.setInt(1,institucion);  
            ps.setInt(2,codigo);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());           
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsulta"+e.toString() );		   
	    }
        return null; 
    }
    /**
     * metodo para modificar usuarios
     * por almacen
     * @param con Connection
     * @param codigo int
     * @param login String
     * @param institucion int
     * @return boolean
     */
    public static boolean gerarUpdate(Connection con,int codigo,String login,int institucion)
    {
        try
        {                       
            String query=updateUsuariosXAlmacenStr;             
            if(!login.equals(""))
            {
                query+=" usuario='"+login+"'";			    
            }                      
            query+=" WHERE codigo="+codigo+" AND institucion="+institucion;
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
	        int r=ps.executeUpdate();
	        if(r>0)
	            return true;
	        else
	            return false;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en gerarUpdate"+e.toString() );
		   return false;
	    }
    }
    /**
     * metodo para eliminar usurios
     * por almacen
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return boolean
     */
    public static boolean generarDelete(Connection con, int codigo, int institucion)
    {
        try
	    {
            PreparedStatementDecorator ps = null;            
            ps= new PreparedStatementDecorator(con.prepareStatement(deleteUsuariosXAlmacenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigo);
            ps.setInt(2, institucion);            
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarDelete"+e.toString() );
		   return false;
	    }
    }
    /**
     * metodo para generar los inserts
     * de usuarios por almacen
     * @param con Connection
     * @param almacen int
     * @param login String
     * @param institucion int
     * @param query String
     * @return boolean
     */
    public static boolean generarInsert(Connection con,int almacen,String login,int institucion,String query)
    {
        try
	    {
            PreparedStatementDecorator ps = null;            
            ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, almacen);
            ps.setString(2, login);            
            ps.setInt(3, institucion);            
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarInsert"+e.toString() );
		   return false;
	    }        
    }
}
