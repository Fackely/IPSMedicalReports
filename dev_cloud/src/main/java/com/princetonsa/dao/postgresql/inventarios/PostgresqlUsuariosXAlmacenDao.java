
/*
 * Creado   23/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.UsuariosXAlmacenDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseUsuariosXAlmacenDao;

/**
 * 
 *
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class PostgresqlUsuariosXAlmacenDao implements UsuariosXAlmacenDao 
{
    /**
     * metodo para generar la consulta de
     * usuarios por almacen
     * @param con Connection
     * @param institucion int
     * @param codigo int     
     * @return HashMap
     */
    public HashMap generarConsulta(Connection con,int institucion,int codigo)
    {
        return SqlBaseUsuariosXAlmacenDao.generarConsulta(con, institucion, codigo);
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
    public boolean gerarUpdate(Connection con,int codigo,String login,int institucion)
    {
        return SqlBaseUsuariosXAlmacenDao.gerarUpdate(con, codigo, login, institucion);
    }
    /**
     * metodo para eliminar usurios
     * por almacen
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return boolean
     */
    public boolean generarDelete(Connection con, int codigo, int institucion)
    {
        return SqlBaseUsuariosXAlmacenDao.generarDelete(con, codigo, institucion);
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
    public boolean generarInsert(Connection con,int almacen,String login,int institucion)
    {
        
        /**
         * query para insertar usuarios por almacen
         */
        final String insertUsuariosXAlmacenStr="INSERT INTO " +
													    		"usuarios_x_almacen_inv " +
													    		"(codigo,centros_costo,usuario,institucion) " +
													    		"VALUES(NEXTVAL('seq_usuarios_x_almacen_inv'),?,?,?)";
        return SqlBaseUsuariosXAlmacenDao.generarInsert(con, almacen, login, institucion, insertUsuariosXAlmacenStr);
    }
    /**
     * metodo para consultar un solo registro
     * @param con onnection
     * @param institucion int
     * @param codigo int
     * @return HashMap
     */
    public HashMap generarConsultaRegistro(Connection con,int institucion,int codigo)
    {
        return SqlBaseUsuariosXAlmacenDao.generarConsultaRegistro(con, institucion, codigo);
    }
}
