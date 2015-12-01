/*
 * Creado en 04-oct-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao;

import java.sql.Connection;
import java.util.Vector;
import java.sql.ResultSet;

/**
 * @author santiago
 *
 */
public interface NotificacionDao {

    /**
     * Metodo para insertar una notificacion
     * @param con
     * @param codigosFichas
     * @param codigoUsuario
     * @param tipo
     * @param nombreDiagnostico
     * @return
     */
    public int insertarNotificacion(Connection con,
										Vector codigosFichas,
										int codigoUsuario,
										String tipo,
										String nombreDiagnostico
									);
    
    /**
     * Metodo para consultar todos los datos de una notificacion
     * @param con
     * @param codigo
     * @return
     */
    public ResultSet consultarTodoNotificacion(Connection con, int codigo);
}
