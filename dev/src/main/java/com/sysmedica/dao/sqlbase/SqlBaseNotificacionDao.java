/*
 * Creado en 04-oct-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

/**
 * @author santiago
 *
 */
public class SqlBaseNotificacionDao {

    private static Logger logger = Logger.getLogger(SqlBaseNotificacionDao.class);
    public static int codigoNotificacion=0;
    
    
    /**
     * String con el statement para insertar una notificacion
     */
    private static final String insertarNotificacionStr = "INSERT INTO vigiNotificacion "+
    															"("+
    															"codigoNotificacion,"+
    															"codigoUsuario,"+
    															"fecha,"+
    															"hora,"+
    															"tipo,"+
    															"nombreDiagnostico"+
    															") "+
    														"VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?)";
    
    
    /**
     * String con el statement para consultar una notificacion
     */
    private static final String consultarTodoNotificacionStr = "SELECT "+
    															"codigoUsuario,"+
    															"fecha,"+
    															"hora,"+
    															"tipo "+
    															"FROM vigiNotificacion "+
    														"WHERE codigoNotificacion=?";
    
    /**
     * Metodo para insertar una notificacion
     * @param con
     * @param codigoUsuario
     * @param tipo
     * @param secuencia
     * @return
     */
    public static int insertarNotificacion(Connection con,
            								Vector codigosFichas,
            								int codigoUsuario,
            								String tipo,
            								String nombreDiagnostico,
            								String secuencia)
    {
        int resultado=0;
        int codigo;
        
        try {
            
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                codigo = rs.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
            
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator insertarNotificacion =  new PreparedStatementDecorator(con.prepareStatement(insertarNotificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
            // Inserción de los datos de la notificacion
            
            insertarNotificacion.setInt(1,codigo);
            insertarNotificacion.setInt(2,codigoUsuario);
            insertarNotificacion.setString(3,tipo);
            insertarNotificacion.setString(4,nombreDiagnostico);
            
            resultado = insertarNotificacion.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            // Insercion de la(s) ficha(s) notificada(s)
            /*
            PreparedStatementDecorator insertarFichaNotificada =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaNotificadaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            for (int i=0;i<codigosFichas.size();i++) {
                
                int codigoFicha = Integer.parseInt(codigosFichas.get(i).toString());
                
                insertarFichaNotificada.setInt(1,codigoFicha);
                insertarFichaNotificada.setInt(2,codigo);
                
                resultado = insertarFichaNotificada.executeUpdate();
                
                if (resultado<1) {
                    
                    daoFactory.abortTransaction(con);
                    return -1;
                }
            }
            */
            
            daoFactory.endTransaction(con);
        }
        catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseNotificacionDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    /**
     * Metodo para consultar una notificacion
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarTodoNotificacion(Connection con, int codigo)
    {
        try {
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarTodoNotificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            logger.error("Error consultando la notificacion "+sqle);
			return null;
        }
    }
}
