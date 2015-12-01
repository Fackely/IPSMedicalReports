package com.princetonsa.dao.sqlbase.glosas;

/**
 * @author Juan Alejandro Cardona
 * @date: Octubre de 2008
 */


import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseParametrosFirmasImpresionRespDao;
import com.princetonsa.decorator.ResultSetDecorator;


public class SqlBaseParametrosFirmasImpresionRespDao {
	
	 // Manejador de logs de la clase
	private static Logger logger = Logger.getLogger(SqlBaseParametrosFirmasImpresionRespDao.class); 
	
	 // Cadena SELECT que consulta las Firmas en Impresion Respuesta de Glosa 
	private static String sqlSearchParametrosFirmasImpresionResp = ""+
		 "SELECT " +
		 	"frg.codigo AS codsecuencia, " +
		 	"frg.usuario AS usuario, " +
		 	"frg.cargo AS cargo " +
		"FROM " +
			"firmas_resp_glosas frg " +
		"WHERE frg.usuario <> '' AND frg.cargo <> '' " +
		"ORDER BY " +
			"codsecuencia ";

	 // Cadena INSERT para guardar los datos de nueva Firma en Impresion Respuesta de Glosa 
	private static String sqlInsertFirmasImpresionResp = "INSERT INTO firmas_resp_glosas " +
			"(codigo, usuario, cargo, institucion, usuario_modifica, fecha_modifica, hora_modifica) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?)"; 
	

	 // Cadena DELETE para Eliminar una firma 
	private static String strDelFirmasImpresionResp  = "DELETE FROM firmas_resp_glosas WHERE codigo = ? "; 
	

	 // Cadena DELETE para Eliminar TODAS las firmas 
	private static String strDelTodaFirmasImpresionResp  = "DELETE FROM firmas_resp_glosas "; 
	
	
	 // Cadena Para Modificar los datos de una firma 
	private static String strUpdFirmasImpresionResp = "UPDATE firmas_resp_glosas" +
														" SET " +
															"usuario = ?, "+
															"cargo = ? "+
														"WHERE codigo= ? "; 


	public static boolean eliminarFirmaImpresion(Connection con, int codeSecuenFirma) {
		boolean enTransaccion = false;
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strDelFirmasImpresionResp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setInt(1, codeSecuenFirma);
	  		
	  		enTransaccion = (ps.executeUpdate() > 0);
	  		if(enTransaccion)     	{
        		logger.info("SE ELIMINO CORRECTAMENTE LA FIRMA EN IMPRESION RESPUESTA DE GLOSA");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	public static boolean eliminarTodaFirmaImpresion(Connection con) {
		boolean enTransaccion = false;
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strDelTodaFirmasImpresionResp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		
	  		enTransaccion = (ps.executeUpdate() > 0);
	  		if(enTransaccion)     	{
        		logger.info("SE ELIMINARON CORRECTAMENTE TODAS LAS FIRMAS EN IMPRESION RESPUESTA DE GLOSA");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public static boolean modificarFirmaImpresion(Connection con, HashMap criterios) {
		boolean enTransaccion = false;
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdFirmasImpresionResp, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	  		ps.setString(1, criterios.get("usuario")+"");
	  		ps.setString(2, criterios.get("cargo")+"");
	  		ps.setInt(3, Utilidades.convertirAEntero(criterios.get("codigo")+""));

        	enTransaccion = (ps.executeUpdate() > 0);
        	
        	if(enTransaccion) {
        		logger.info("SE MODIFICO CORRECTAMENTE LA FIRMA EN IMPRESION RESPUESTA DE GLOSA");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
		return false;
	}

	
	
	/** Método para la inserción de una Nueva Firma en Impresion Respuesta de Glosa  
	 * @param con, criterios
	 * @return	 */
	public static boolean insertarFirmaImpresion(Connection con, HashMap criterios) {
		boolean enTransaccion = false;

		logger.info("===========================");
		logger.info("ENTRO AL INSERTAR FIRMA");
		
		Utilidades.imprimirMapa(criterios);		
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sqlInsertFirmasImpresionResp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_codi_firmas_resp_glosas"));
	  		ps.setString(2, criterios.get("tmpUsuario")+"");
	  		ps.setString(3, criterios.get("tmpCargo")+"");

	  		//revisar estos valores
	  		ps.setInt(4, Utilidades.convertirAEntero(criterios.get("institucionFirma")+""));
	  		ps.setString(5, criterios.get("usuarioFirma")+"");

	  		ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(7, UtilidadFecha.getHoraActual(con));
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ALMACENO CORRECTAMENTE LA FIRMA EN IMPRESION RESPUESTA DE GLOSA.");
        		return true;
        	}
        	else {
        		logger.info("ERROR NO SE ALMACENO CORRECTAMENTE EL DATO: " + criterios.get("tmpUsuario"));
        		return false;
        	}
  		}
		catch (SQLException e) {
			
			logger.info("ERROR NO SE ALMACENO CORRECTAMENTE EL DATO: " + criterios.get("tmpUsuario")+", DEBIDO A QUE YA EXISTE.");
		}
		return false;
	}
	
	
	/** Metodo para listar los datos almacenados de  los resultados y mandarlos a un archivo plano posteriormente o a la pantalla del usuario
	 * @param (con, criterios, tipoBD
	 * @return */
	public static HashMap listadoFirmas(Connection con, String consulta) {
		sqlSearchParametrosFirmasImpresionResp = consulta;
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");

		try {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(" SELECT frg.codigo AS codsecuencia, frg.usuario AS usuario, frg.cargo AS cargo FROM firmas_resp_glosas frg ORDER BY codsecuencia ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	logger.info("====>Consulta Firmas Impresion Respuesta de Glosa : " + sqlSearchParametrosFirmasImpresionResp);
        	ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
        	mapa = UtilidadBD.cargarValueObject(rs, true, true);
        }
        catch (SQLException e) { 
            logger.error("ERROR EJECUTANDO LA CONSULTA DE FIRMAS EN IMPRESION RESPUESTA DE GLOSA.");
            e.printStackTrace();
        }
        logger.info("MAPA::::"+mapa);
        return mapa;
	}
}