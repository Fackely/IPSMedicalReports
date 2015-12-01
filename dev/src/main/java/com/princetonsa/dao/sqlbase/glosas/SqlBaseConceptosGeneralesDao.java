package com.princetonsa.dao.sqlbase.glosas;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author Mauricio Jaramillo
 * Fecha: Septiembre de 2008
 */

public class SqlBaseConceptosGeneralesDao
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConceptosGeneralesDao.class);
	
	/**
	 * Cadena SELECT para consultar los conceptos generales de glosas
	 */
	private static String strConSelConceptosGenerales = "SELECT " +
															"codigo AS codigo, " +
															"consecutivo AS consecutivo, " +
															"descripcion AS descripcion, " +
															"tipo_glosa AS tipoglosa, " +
															"activo AS activo " +
														"FROM " +
															"glosas.conceptos_generales " +
														"ORDER BY "+
															"consecutivo ";
		
	/**
	 * Cadena INSERT para ingresar el Concepto General de Glosas 
	 */
	private static String strInsConceptoGlosas = "INSERT INTO glosas.conceptos_generales " +
			"(codigo, consecutivo, descripcion, tipo_glosa, activo, institucion, usuario_modifica, fecha_modifica, hora_modifica)" +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; 

	/**
	 * Cadena DELETE para Eliminar el Concepto General de Glosas 
	 */
	private static String strDelConceptoGlosas = "DELETE FROM glosas.conceptos_generales WHERE codigo= ? "; 
	
	/**
	 * Cadena Para Modificar los datos de un Concepto General de Glosas 
	 */
	private static String strUpdConceptoGlosas = "UPDATE glosas.conceptos_generales SET consecutivo = ?, " +
																	"descripcion = ?, " +
																	"tipo_glosa = ?, " +
																	"activo = ?, " +
																	"institucion = ?, " +
																	"usuario_modifica = ?, " +
																	"fecha_modifica = ?, "+
																	"hora_modifica = ? "+
																"WHERE codigo= ? "; 
		
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 */
	public static HashMap consultarConceptosGenerales(Connection con, int codigoInstitucionInt)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
	    try
	    {
	    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSelConceptosGenerales, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	    	logger.info("====>Consulta de Conceptos Generales Glosas: "+strConSelConceptosGenerales);
	    	mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	    }
	    catch (SQLException e)
	    {
	        logger.error("ERROR EJECUTANDO LA CONSULTA DE CONCEPTOS GENERALES GLOSAS");
	        e.printStackTrace();
	    }
	    return mapa;
	}

	/**
	 * Método que genera la inserción del nuevo concepto general de glosas, 
	 * especificando el código, la descripción, el tipo y si esta activo o no
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean insertarConceptoGeneral(Connection con, HashMap criterios)
	{
		boolean enTransaccion = false;
		try
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsConceptoGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "glosas.seq_conceptos_generales"));
	  		ps.setDouble(2, Utilidades.convertirADouble(criterios.get("codigoConcepto")+""));
	  		ps.setString(3, criterios.get("descripcionConcepto")+"");
	  		ps.setString(4, criterios.get("tipoConcepto")+"");
	  		ps.setString(5, criterios.get("activoConcepto")+"");
	  		ps.setInt(6, Utilidades.convertirAEntero(criterios.get("institucionConcepto")+""));
	  		ps.setString(7, criterios.get("usuarioConcepto")+"");
	  		ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(9, UtilidadFecha.getHoraActual(con));
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion)
        	{
        		logger.info("SE INSERTO CORRECTAMENTE EL CONCEPTO GENERAL DE GLOSA");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * @param con
	 * @param codigoConcepto
	 * @return
	 */
	public static boolean eliminarConceptoGeneral(Connection con, int codigoConcepto)
	{
		boolean enTransaccion = false;
		try
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strDelConceptoGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setInt(1, codigoConcepto);
	  		
	  		enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion)
        	{
        		logger.info("SE ELIMINO CORRECTAMENTE EL CONCEPTO GENERAL DE GLOSA");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean modificarConceptoGeneral(Connection con, HashMap criterios)
	{
		boolean enTransaccion = false;
		try
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdConceptoGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setDouble(1, Utilidades.convertirADouble(criterios.get("consecutivoConcepto")+""));
	  		ps.setString(2, criterios.get("descripcionConcepto")+"");
	  		ps.setString(3, criterios.get("tipoConcepto")+"");
	  		ps.setString(4, criterios.get("activoConcepto")+"");
	  		ps.setInt(5, Utilidades.convertirAEntero(criterios.get("institucionConcepto")+""));
	  		ps.setString(6, criterios.get("usuarioConcepto")+"");
	  		ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(8, UtilidadFecha.getHoraActual(con));	  		
	  		ps.setInt(9, Utilidades.convertirAEntero(criterios.get("codigoConcepto")+""));

        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion)
        	{
        		logger.info("SE MODIFCO CORRECTAMENTE EL CONCEPTO GENERAL DE GLOSA");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

}