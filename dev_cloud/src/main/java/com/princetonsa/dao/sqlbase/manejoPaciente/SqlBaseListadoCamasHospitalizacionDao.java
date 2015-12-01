package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.ListadoCamasHospitalizacion;

/**
 * 
 * @author Mauricio Jaramillo
 *
 */

public class SqlBaseListadoCamasHospitalizacionDao 
{

	/**
	 * Indices
	 */
	private static String[] indices = {"convenio_", "ocupacion_", "porcentaje_"}; 
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseListadoCamasHospitalizacionDao.class);
	
	/**
	 * 
	 * @param mundo
	 * @param oldQuery
	 * @param usuario
	 * @return
	 */
	public static HashMap listadoCamasHospitalizacion(Connection con, ListadoCamasHospitalizacion mundo, String condiciones)
	{
		HashMap mapa = new HashMap();
		String listadoCamasHospitalizacionStr = 
												"SELECT "+
													"getnombreconvenio(tc.convenio) as convenio, "+
													"count(tc.convenio) as ocupacion, "+
													"(count(tc.convenio)*100 / (SELECT sum(1) "+
														"FROM "+
														"traslado_cama tc "+
														"INNER JOIN camas1 c on(tc.codigo_nueva_cama=c.codigo) "+
														"INNER JOIN centros_costo cc on(c.centro_costo=cc.codigo) "+
													"WHERE "+
														"tc.fecha_finalizacion is null "+
														"AND tc.hora_finalizacion is null "+
														"AND c.estado in("+condiciones+") "+
														"AND centro_atencion=?)) as porcentaje "+
												"FROM "+
													"traslado_cama tc "+
													"INNER JOIN camas1 c on(tc.codigo_nueva_cama=c.codigo) "+
													"INNER JOIN estados_cama ec on(c.estado=ec.codigo) "+
													"INNER JOIN centros_costo cc on(c.centro_costo=cc.codigo) "+
												"WHERE "+
													"tc.fecha_finalizacion is null "+
													"AND tc.hora_finalizacion is null "+
													"AND c.estado in("+condiciones+") "+
													"AND centro_atencion=? "+
												"GROUP BY getnombreconvenio(tc.convenio)"+
												"ORDER BY getnombreconvenio(tc.convenio)";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(listadoCamasHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, mundo.getCentroAtencion());
			ps.setString(2, mundo.getCentroAtencion());
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("indices", indices);

		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;	
	}
}