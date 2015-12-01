/*
 * Creado en 04-oct-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

import com.sysmedica.dao.sqlbase.SqlBaseNotificacionDao;
import com.sysmedica.dao.NotificacionDao;

/**
 * @author santiago
 *
 */
public class OracleNotificacionDao implements NotificacionDao {

    /**
     * String con el statement para obtener el valor de la secuencia de las notificaciones (para el codigo)
     */
    private String secuenciaStr = "SELECT seq_notificaciones.nextval FROM dual";
    
    
    /**
     * Metodo para insertar una notificacion
     */
    public int insertarNotificacion(Connection con,
										Vector codigosFichas,
										int codigoUsuario,
										String tipo,
										String nombreDiagnostico
									)
    {
        return SqlBaseNotificacionDao.insertarNotificacion(con,
                											codigosFichas,
                											codigoUsuario,
                											tipo,
                											nombreDiagnostico,
                											secuenciaStr
                											);
    }

    
    
    /**
     * Metodo para consultar una notificacion
     */
    public ResultSet consultarTodoNotificacion(Connection con, int codigo)
    {
        return SqlBaseNotificacionDao.consultarTodoNotificacion(con,codigo);
    }
}
