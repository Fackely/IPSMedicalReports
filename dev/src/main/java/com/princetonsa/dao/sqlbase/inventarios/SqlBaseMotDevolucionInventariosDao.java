/*
 * Created on 23/11/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
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
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class SqlBaseMotDevolucionInventariosDao
{
    /**
     * Variable para manejar los errores de la clase.
     */
    private static Logger logger=Logger.getLogger(SqlBaseMotDevolucionInventariosDao.class);
    
    /**
     * Cadena que realiza la consulta de los motivods devoulicon de inventario.
     */
    private static String cadenaConsultaMotDevInv="SELECT codigo as codigo,lower(descripcion) as descripcion, activo as activo,'false' as eliminado,'true' as bd from mot_devolucion_inventario where institucion=? order by descripcion";
    
    /**
     * Cadena para eliminar un registro.
     */
    private static String cadenaEliminarMotDevInv="delete from mot_devolucion_inventario  where codigo=? and institucion=?";
    
    /**
     * Cadena para actualizar un registro.
     */
    private static String cadenaUpadateRegistro="UPDATE mot_devolucion_inventario SET descripcion = ?, activo = ? where codigo = ? and institucion = ?";
    
    /**
     * Cadena para insertar un nuevo registro.
     */
    private static String insertarRegistro="INSERT INTO mot_devolucion_inventario(codigo,descripcion,activo,institucion) values(?,?,?,?)";
    
    /**
     * Cadena para consultar un registro por la llave.
     */
    private static String consultarRegistroLLave="SELECT codigo as codigo,lower(descripcion) as descripcion, activo as activo,'false' as eliminado,'true' as bd from mot_devolucion_inventario where codigo=? and institucion=?";

    
    /**
     * @param con
     * @param institucion
     * @return
     */
    public static HashMap consultarMotivosDevolucionInventarios(Connection con, int institucion)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaMotDevInv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR CONSULTANDO LOS MOTIVO DEVOLUCION INVENTARIOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
       
    }


    
    /**
     * @param con
     * @param codigoRegistro
     * @param institucion
     * @return
     */
    public static boolean eliminarRegistro(Connection con, String codigoRegistro, int institucion)
    {
        try
        {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarMotDevInv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setString(1, codigoRegistro);
           ps.setInt(2, institucion);
           return (ps.executeUpdate()>0);
        }
        catch (SQLException e)
        {
            logger.error("ERROR ELIMINANDO MOTIVO DEVOLUCION INVENTARIOS CODIGO "+codigoRegistro);
            e.printStackTrace();
        }
        return false;
    }



    
    /**
     * @param con
     * @param descripcion
     * @param activo
     * @param codigo
     * @param institucion
     * @return
     */
    public static boolean actualizarRegistrio(Connection con, String descripcion, boolean activo, String codigo, int institucion)
    {
        try
        {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpadateRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setString(1, descripcion);
           ps.setBoolean(2, activo);
           ps.setString(3, codigo);
           ps.setInt(4, institucion);
           return (ps.executeUpdate()>0);
        }
        catch (SQLException e)
        {
            logger.error("ERROR ACTUALIZANDO MOTIVO DEVOLUCION INVENTARIOS CODIGO ");
            e.printStackTrace();
        }
        return false;
    }



    
    /**
     * @param con
     * @param descripcion
     * @param activo
     * @param codigo
     * @param institucion
     * @return
     */
    public static boolean insertarRegistro(Connection con, String descripcion, boolean activo, String codigo, int institucion)
    {
        try
        {
           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           ps.setString(1, codigo);
           ps.setString(2, descripcion);
           ps.setBoolean(3, activo);
           ps.setInt(4, institucion);
           return (ps.executeUpdate()>0);
        }
        catch (SQLException e)
        {
            logger.error("ERROR INSERTANDO MOTIVO DEVOLUCION INVENTARIOS CODIGO ");
            e.printStackTrace();
        }
        return false;
    }



    
    /**
     * @param con
     * @param codigo
     * @param institucion
     * @return
     */
    public static HashMap consultarMotivoDevolucionInvLLave(Connection con, String codigo, int institucion)
    {
        HashMap mapa=new HashMap();
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarRegistroLLave,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, codigo);
            ps.setInt(2, institucion);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR CONSULTANDO LOS MOTIVO DEVOLUCION INVENTARIOS POR LLAVE");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }



}
