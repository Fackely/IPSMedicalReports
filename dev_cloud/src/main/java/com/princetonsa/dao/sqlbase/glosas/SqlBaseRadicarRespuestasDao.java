package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class SqlBaseRadicarRespuestasDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRadicarRespuestasDao.class);
	
	/**
	 * Cadena que actualiza la Respuesta 
	 */
	private static String actualizarRespuesta ="UPDATE respuesta_glosa " +
												"SET estado=?, " +
												"fecha_radicacion=?, " +
												"numero_radicacion=?, " +
												"observaciones=?, " +
												"fecha_modifica=CURRENT_DATE, " +
												"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
												"usuario_radicacion=?, " +
												"usuario_modifica=? " +
												"WHERE codigo=? ";
	
	/**
	 * Metodo que la Repuesta  Radicada 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean guardar(Connection con, HashMap criterios)
	{
		PreparedStatementDecorator ps;
		try
		{			
			ps= new PreparedStatementDecorator(con.prepareStatement(actualizarRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRadicada);
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(criterios.get("fecha")+""));
			ps.setString(3, criterios.get("numero")+"");
			ps.setString(4, criterios.get("observ")+"");
			ps.setString(5, criterios.get("usuario")+"");			
			ps.setString(6, criterios.get("usuario")+"");
			ps.setString(7, criterios.get("codrespuesta")+"");
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO RESPUESTA SQL------>>>>>>"+e);
			e.printStackTrace();
		}		
		return false;
	}
}