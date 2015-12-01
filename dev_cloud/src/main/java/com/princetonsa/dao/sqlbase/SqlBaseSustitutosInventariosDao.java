/*
 * Created on 31-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

/**
 * @author armando
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Registro
 * de Diagnosticos
 
 * Princeton 31-ago-2004
 */
public class SqlBaseSustitutosInventariosDao {
    
    /**
     * Variable para manejar los errores loger de la funcionalidad
     */
    private static Logger logger = Logger.getLogger(SqlBaseSustitutosInventariosDao.class);
    /**
     * Variable para manejar la insercion en la tabla sustitutos_inventarios
     */
    private static String insertarSustitutosInventarios="insert into sustitutos_inventario values (?,?)";
    /**
     * Variable para manejar la Modificacion en la tabla sustitutos_inventarios 
     */
    private static String modificarSustitutosInventarios="update sustitutos_inventario set sustituto=? where principal=? and sustituto=?";
    /**
     * Variable para manejar la Modificacion en la tabla sustitutos_inventarios 
     */
    private static String modificarSustitutosInventariosInverso="update sustitutos_inventario set principal=? where principal=? and sustituto=?";

    /**
     * Variable para manejar la Eliminacion en la tabla sustitutos_inventarios 
     */    
    private static String eliminarSustitutosInventarios="delete from sustitutos_inventario where principal=? and sustituto=?";
    /**
     * Variable para realizar la consulta avanzada de la tabla sustitutos_inventarios.
     */
    private static String consutaAvanzada="SELECT codigo,descripcion,getNomNaturalezaArticulo(naturaleza) AS naturaleza,minsalud,getNomFormaFarmaceutica(forma_farmaceutica) as forma_farmaceutica,concentracion,getNomUnidadMedida(unidad_medida) AS unidad_medida from view_articulos where codigo in (select sustituto from sustitutos_inventario where principal=?) order by descripcion";
    /**
     * Variable para realizar la consulta de un registro en la tabla sustitutos_inventarios.
     */
    private static String consuta="select principal,sustituto from sustitutos_inventario where principal=? and sustituto=?";

    
    /*Consulta avanzada
    *
    *SELECT si.principal as codigoPrincipal,si.sustituto as codigoSustituto,a.naturaleza as naturalez,a.minsalud as minsalud,a.forma_farmaceutica as formaFarmaceutica,a.concentracion as concentracion from sustitutos_inventario si inner join articulo a on (si.principal=a.codigo) inner join subgrupo_inventario s on (a.subgrupo=s.codigo) inner join clase_inventario c on (s.clase=c.codigo AND institucion='2') where 1=1
    *
    *
    **/
    /**
     * Metodo que inserta un registro en la tabla Sustitutos Inventario
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @param codigoSustituto,<b>int</b>, Codigo del articulo Sustituto.
     * @return -1 si se produjo un error.
     */
    public static int insertarSustitutosInventarios(Connection con, int codigoPrincipal, int codigoSustituto) 
    {
        PreparedStatementDecorator ps;
        try 
        {   
            ps =  new PreparedStatementDecorator(con.prepareStatement(insertarSustitutosInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoPrincipal);
            ps.setInt(2,codigoSustituto);
            ps.executeUpdate();
            ps.clearParameters();
            ps =  new PreparedStatementDecorator(con.prepareStatement(insertarSustitutosInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoSustituto);
            ps.setInt(2,codigoPrincipal);
            return ps.executeUpdate();
            
        } catch (SQLException e) 
        {
            logger.warn("No se pudo realizar la insercion "+e.toString());
        }
        return -1;
    }
    /**
     * Metodo que modifica un registro en la tabla Sustitutos Inventario,
     * solo se pueden modificar los articulos sustitutos de un determinado
     * aticulo principal.
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal a modificar.
     * @param codigoSustituto,<b>int</b>, Codigo del nuevo articulo Sustituto.
     * @return -1 si se produjo un error.
     */
    public static int modificarSustitutosInventarios(Connection con,int codigoPrincipal,int codigoSustituto,int codigoSustitutoOld)
    {
        PreparedStatementDecorator ps;
        try 
        {   
            ps =  new PreparedStatementDecorator(con.prepareStatement(modificarSustitutosInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoSustituto);
            ps.setInt(2,codigoPrincipal);
            ps.setInt(3,codigoSustitutoOld);
            ps.executeUpdate();
            ps.clearParameters();
            ps =  new PreparedStatementDecorator(con.prepareStatement(modificarSustitutosInventariosInverso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoSustituto);
            ps.setInt(2,codigoSustitutoOld);
            ps.setInt(3,codigoPrincipal);
            return ps.executeUpdate();
        } catch (SQLException e) 
        {
            logger.warn("No se pudo realizar la Modificacion "+e.toString());
        }
        return -1;    
    }
    
    /**
     * Metodo que elimina un registro en la tabla Sustitutos Inventario,
     * solo se pueden eliminar los articulos sustitutos de un determinado
     * aticulo principal.
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @param codigoSustituto,<b>int</b>, Codigo del articulo Sustituto que sera eliminado.
     * @return -1 si se produjo un error.
     */
    public static int eliminarSustitutosInventarios(Connection con,int codigoPrincipal,int codigoSustituto)
    {
        PreparedStatementDecorator ps;
        try 
        {   
            ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarSustitutosInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoPrincipal);
            ps.setInt(2,codigoSustituto);
            ps.executeUpdate();
            ps.clearParameters();
            ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarSustitutosInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoSustituto);
            ps.setInt(2,codigoPrincipal);
            return ps.executeUpdate();
        } catch (SQLException e) 
        {
            logger.warn("No se pudo realizar la Eliminacion "+e.toString());
        }
        return -1;    
    }
    /**
     * Metodo que realiza la consulta de un registro en la tabla Sustitutos Inventario,
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @return Collection con los datos
     */
    public static Collection consultarSustitutosInventarios(Connection con,int codigoPrincipal)
    {
        PreparedStatementDecorator ps;
        ResultSetDecorator rs;
        Collection coleccion=null;
        try 
        {   
            ps =  new PreparedStatementDecorator(con.prepareStatement(consutaAvanzada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoPrincipal);
            rs=new ResultSetDecorator(ps.executeQuery());
            coleccion=UtilidadBD.resultSet2Collection(rs);
            return coleccion;
        } catch (SQLException e) 
        {
            logger.warn("No se pudo realizar la consulta "+e.toString());
        }
        return coleccion;    
    }
    /**
     * Metodo que me retorna un registro especifico. se debe dar rs.next() para ver los datos.
     * @param con, Conexion
     * @param codigoPrincipal, Codigo principal del articulo
     * @param codigoSustituto, Codigo sustituto del articulo
     * @return Resultset, resultset con los datos.
     */
    public static ResultSetDecorator consultarSustitutoInventarioEspecifico(Connection con, int codigoPrincipal, int codigoSustituto) {
        
        PreparedStatementDecorator ps;
        ResultSetDecorator rs;
        try 
        {   
            ps =  new PreparedStatementDecorator(con.prepareStatement(consuta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoPrincipal);
            ps.setInt(2,codigoSustituto);
            rs=new ResultSetDecorator(ps.executeQuery());
            return rs;
        } catch (SQLException e) 
        {
            logger.warn("No se pudo realizar la consulta "+e.toString());
        }
        return null;  
    }

}
