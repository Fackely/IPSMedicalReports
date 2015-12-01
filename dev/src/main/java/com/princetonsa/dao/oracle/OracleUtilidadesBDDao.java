/*
 * Created on 6/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;

/**
 * @version 1.0, 6/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class OracleUtilidadesBDDao implements UtilidadesBDDao
{

    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(OracleUtilidadesBDDao.class);
	        
    /**
     * Metodo que abre una conexion
     * @see com.princetonsa.dao.UtilidadesBDDao#abrirConexion()
     */
    public static Connection abrirConexion()
    {
        Connection con=null; 
        try
        {
            con=OracleDaoFactory.getInstance().getConnection();
            if(con==null)
            {
                logger.error("Imposible abrir la conexion");
            }
        }
        catch (SQLException e)
        {
            logger.error("Error Abrindo la conexion"+e);
        }
        return con;
    }

    /**
     * metodo que cierra una conexion
     * @param con
     */
    public static boolean cerrarConexion(Connection con)
    {
        try
        {
        	UtilidadBD.closeConnection(con);
            return con.isClosed();
        }
        catch (SQLException e)
        {
            logger.error("Error cerrando la conexion"+e);
        }
        return false;
    }
    
    /**
     * metodo que inicia una transaccion
     * @param con
     */
    public static boolean iniciarTransaccion(Connection con)
    {
        try
        {
            return OracleDaoFactory.getInstance().beginTransaction(con);
        }
        catch (SQLException e)
        {
            logger.error("Error cerrando la conexion"+e);
        }
        return false;
    }
    
    /**
     * metodo que aborta una transaccion
     * @param con
     */
    public static void abortarTransaccion(Connection con)
    {
        try
        {
            OracleDaoFactory.getInstance().abortTransaction(con);
        }
        catch (SQLException e)
        {
            logger.error("Error cerrando la conexion"+e);
        }
    }
    
    /**
     * metodo que aborta una transaccion
     * @param con
     */
    public static void finalizarTransaccion(Connection con)
    {
        try
        {
            OracleDaoFactory.getInstance().endTransaction(con);
        }
        catch (SQLException e)
        {
            logger.error("Error cerrando la conexion"+e);
        }
    }
    
    
    
	/**
	 * Metodo que indica si una consulta esta o no bloqueada (select for update) en caso de que sea verdadero entonces retorna true,
	 * POR FAVOR SOLO USAR ESTE METODO A NIVEL DEL DAO
	 * @param consulta
	 * @return
	 */
	public boolean estaConsultaBloqueada(String consulta)
	{
		return SqlBaseUtilidadesBDDao.estaConsultaBloqueada(consulta);
	}
	
	/**
	 * Metodo que devuelve la cantidad de veces
	 * que es utilizado un key en una tabla
	 * @param connection
	 * @param tabla
	 * @param nombreKey
	 * @param valuekey
	 * @return
	 */
	public int estaUtilizadoEnTabla (Connection connection,String tabla,String nombreKey, int valuekey)
	{
		return SqlBaseUtilidadesBDDao.estaUtilizadoEnTabla(connection, tabla, nombreKey, valuekey);
	}

	@Override
	public boolean actualizarNumeroProcesosBD() 
	{
		boolean retorna=false;
		String consulta= "ALTER SYSTEM SET PROCESSES=150 SCOPE=SPFILE";
		logger.info("\n\n actualizarNumeroProcesosBD->"+consulta+"\n\n");
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//ps.executeUpdate();
			ps.close();
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
            logger.info("Se presento un error en  actualizarNumeroProcesosBD: "+e);
            e.printStackTrace();
		}
		
		return retorna;
	}
	
	/**
	 * Método implementado para cerrar objetos de persistencia
	 * @param ps
	 * @param rs
	 * @param con
	 */
	public void cerrarObjetosPersistencia(Statement ps,ResultSet rs,Connection con)
	{
		SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
	}
    
}