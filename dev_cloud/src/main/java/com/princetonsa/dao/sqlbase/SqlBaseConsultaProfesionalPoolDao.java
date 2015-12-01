
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * Calse para las transacciones de Consulta de
 * Profesionlaes por Pool
 *
 * @version 1.0, 17 /Mar/ 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class SqlBaseConsultaProfesionalPoolDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaProfesionalPoolDao.class);
	
	
	/**
	 * Cadena con el statement necesario para consultar los datos de un medico en un pool
	 * segun el codigo del medico
	 */
	private static final String consultaProfesionalPoolStr=" SELECT pp.pool as codigopool, " +
														   " p.descripcion as nombrepool, " +
														   " p.activo as estadopool, " +
														   " to_char(pp.fecha_ingreso,'"+ConstantesBD.formatoFechaAp +"') as fechaingreso, " +
														   " to_char(pp.fecha_retiro,'"+ConstantesBD.formatoFechaAp +"') as fecharetiro, " +
														   " pp.porcentaje_participacion as participacion " +
														   " FROM participaciones_pooles pp " +
														   " INNER JOIN pooles p ON (pp.pool=p.codigo) " +
														   " WHERE pp.medico=? " +
														   " ORDER BY nombrepool ";
	
	
	/**
	 * Metodo para consultar los datos de los pooles de un medico dado 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public static HashMap consultaProfesionalPool(Connection con,int codigoMedico) throws SQLException
    {
        try
        {
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();                
            }
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaProfesionalPoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoMedico);
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
    		ps.close();
    		return mapaRetorno;
        }
        catch(SQLException e)
        {
            logger.warn(e+"Error en la consultaProfesionalPool [SqlBaseConsultaProfesionalPoolDao] : "+e.toString() );
            return null;
        }        
    }
}