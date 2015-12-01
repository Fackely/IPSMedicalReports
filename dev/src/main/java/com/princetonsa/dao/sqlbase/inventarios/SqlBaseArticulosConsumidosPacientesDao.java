package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.ArticulosConsumidosPacientes;

/**
 * @author Mauricio Jaramillo
 */

public class SqlBaseArticulosConsumidosPacientesDao 
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseArticulosConsumidosPacientesDao.class);

	/**
	 * Método que consulta los Articulos Consumidos y devuelve un mapa
	 * para mostrar los resultados en el Archivo Plano
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap<String, Object> consultarArticulosConsumidos(Connection con, HashMap<String, Object> criterios)
	{
		HashMap mapaResultados = new HashMap();
		HashMap mapaConsulta = new HashMap();
		mapaConsulta = ArticulosConsumidosPacientes.consultarCondicionesArticulosConsumidos(con, criterios);
		try
		{
			logger.info("===>Consulta Articulos Consumidos: "+mapaConsulta.get("consulta")+" ===>Tipo Codigo Articulo: "+criterios.get("tipoCodigoArticulo"));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(mapaConsulta.get("consulta").toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, criterios.get("tipoCodigoArticulo")+"");
			pst.setString(2, criterios.get("tipoCodigoArticulo")+"");
			pst.setString(3, criterios.get("tipoCodigoArticulo")+"");
			mapaResultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarArticulosConsumidos: "+e);
		}
		return mapaResultados;
	}
	
}