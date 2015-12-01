/*
 * @(#)Xconnection.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.5.0_06
 *
 */
package com.princetonsa.webCreateXml;

import java.sql.Connection;
import java.sql.DriverManager;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

/**
 * Clase que genera toda la comunicacion con la BD que tenga el usuario,
 * las consultas se hicieron en sql generico para no tener problemas con
 * los diferentes motores de BD, se hizo aparte de la arquitectura DAO de 
 * axioma porque esta funcionalidad debe correr independientemente
 *  
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 *
 */
public class XconnectionDB 
{	
	/**
	 * lista de funcionalidades x roles
	 */
	private static final String listadoFuncionalidadesXRolesStr="SELECT DISTINCT f.codigo_func AS codigoFuncionalidad, f.nombre_func AS nombreFuncionalidad, f.archivo_func AS pathFuncionalidad, rf.isssl AS isSSL FROM funcionalidades f, roles r, roles_funcionalidades rf WHERE rf.codigo_func=f.codigo_func AND r.nombre_rol=rf.nombre_rol ORDER BY nombreFuncionalidad ";
	
	/**
	 * detalle de los roles que contienen una funcionalidad especifica
	 */
	private static final String detalleRolXFuncionalidadStr= "SELECT nombre_rol AS nombreRol FROM roles_funcionalidades WHERE codigo_func=? ORDER BY nombreRol";
	
	/**
	 * carga el listado de roles existentes en el sistema ordenados alfabeticamente por el nombre
	 */
	private static final String listadoRolesSistemaStr= "SELECT nombre_rol AS nombreRol FROM roles ORDER BY nombre_rol";
	
	/**
	 * vector de strings que contienen unos posibles drivers de conexion, si ha futuro se tienen mas se deben adicionar aca para que se reflejen en la
	 * aplicacion swing
	 */
	public static final String[] listadoDriversVector={"org.postgresql.Driver", "oracle.jdbc.driver.OracleDriver", "oracle.jdbc.OracleDriver"};

	
	/**
	 * metodo que devuelve una conexion con la base de datos, recuerde cerrar la conexion
	 * @param xJDBCDriver
	 * @param xDatabaseURL
	 * @param xUser
	 * @param xPassword
	 * @return
	 */
	public static Connection getConnectionDB(String xJDBCDriver, String xDatabaseURL, String xUser, String xPassword)
    {
		Connection con=null;
		try
		{
			Class.forName(xJDBCDriver);
		}
		catch(ClassNotFoundException e )
		{
			Log4JManager.error("No pudo cargar el driver");
			return null;
		}
		try
		{
			con= DriverManager.getConnection(xDatabaseURL,xUser,xPassword);
		}
		catch(SQLException e)
		{
			Log4JManager.error("No se pudo conectar");
			return null;
        }
		return con;
    }
	
	/**
	 * cierra la conexion
	 * @param con
	 */
	public static void closeConnectionDB(Connection con)
	{
		try 
		{
			con.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * metodo que retorna el listado de roles del sistema
	 * @param con
	 * @return
	 */
    public static Vector listadoRolesSistema(Connection con)
    {
    	Vector rolesVector= new Vector();
    	int index=0;
    	try
		{
    		ResultSet rs =  new PreparedStatementDecorator(con.prepareStatement(listadoRolesSistemaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery();
    		while(rs.next())
    		{
    			rolesVector.add(index, Xutilidades.flattenString(rs.getString("nombreRol")));
    			index++;
    		}
    		rs.close();
    		return rolesVector;
    	}
    	catch(SQLException e)
		{
    		Log4JManager.error("Error en listadoRolesSistema: "+e);
    		return null;
		}
    }
	
    
    /**
     * Metodo que carga toda la informacion de roles funcionalidades para poder hacer el 
     * <security-constraint> del web.xml, este mapa tiene el codigo - nombre -path funcionalidad
     * y el detalle_ que es otro mapa encapsulado con la información de los roles que tienen esa func 
     * @param con
     * @return
     */
	public static HashMap cargarInformacionRolesFuncionalidades(Connection con)
    {
        HashMap map= new HashMap();
        try
        {
            String[] colums={   "codigoFuncionalidad",
                                "nombreFuncionalidad",
                                "pathFuncionalidad",
                                "isSSL"
                            };
            
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listadoFuncionalidadesXRolesStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            map=Xutilidades.resultSet2HashMap(colums,cargarStatement.executeQuery(), false, true);
            for(int i=0; i<Integer.parseInt(map.get("numRegistros").toString()) ; i++)
            {
                String codigoFuncionalidadStr= map.get("codigoFuncionalidad_"+i).toString();
                map.put("detalle_"+i,  detalleRolXFuncionalidad(con, codigoFuncionalidadStr));
            }
            cargarStatement.close();
            return map;
        }
        catch(SQLException e)
        {
        	Log4JManager.error(" Error en cargarInformacionRolesFuncionalidades: "+e.toString());
            return map;
        }
    }
    
    
    /**
     * metodo que carga un mapa con los roles que contienen una funcionalidad especifica
     * @param con
     * @param codigoFuncionalidad
     * @return
     */
    private static HashMap detalleRolXFuncionalidad(Connection con, String codigoFuncionalidad)
    {
        HashMap map= new HashMap();
        try
        {
            String[] colums={ "nombreRol"};          
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(detalleRolXFuncionalidadStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cargarStatement.setString(1,codigoFuncionalidad);
            map=Xutilidades.resultSet2HashMap(colums,cargarStatement.executeQuery(), false, true);
            map.put("numColumnas", colums.length+"");
            cargarStatement.close();
            return map;
        }
        catch(SQLException e)
        {
        	Log4JManager.error(e+" Error en detalleRolXFuncionalidad de : SqlBaseRolesFuncsDao "+e.toString());
            return map;
        }
    }
	
}