/*
 * Creado en 14-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.NotificacionDao;
import java.util.Vector;
import java.sql.Connection;

/**
 * @author santiago
 *
 */
public class Notificacion {
    
    private int codigo;
    private int codigoUsuario;
    private String fecha;
    private boolean tipo;
    
    private NotificacionDao notificacionDao;
    
    /**
     * Constructor
     *
     */
    public Notificacion() {
        reset();
        init(System.getProperty("TIPOBD"));
    }
    
    //******************************************************************
    
    /**
     * Metodo que resetea los campos
     */
    public void reset() {
        
        codigo = 0;
        fecha = "";
        tipo = false;
        codigoUsuario = 0;
    }
    
    /**
     * Metodo para inicializar el acceso a base de datos
     * @param tipoBD
     * @return
     */
    public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			if (myFactory != null)
			{
				notificacionDao = myFactory.getNotificacionDao();
				wasInited = (notificacionDao != null);
			}
			return wasInited;
	}
    
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
            							String nombreDiagnostico)
    {
        int resultado = notificacionDao.insertarNotificacion(con,codigosFichas,codigoUsuario,tipo,nombreDiagnostico);        
        return resultado;
    }
    
    /**
     * @return Returns the codigo.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Returns the codigoUsuario.
     */
    public int getCodigoUsuario() {
        return codigoUsuario;
    }
    /**
     * @param codigoUsuario The codigoUsuario to set.
     */
    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }
    /**
     * @return Returns the fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @return Returns the tipo.
     */
    public boolean isTipo() {
        return tipo;
    }
    /**
     * @param tipo The tipo to set.
     */
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }
}
