/**
 * Juan David Ramírez 11/05/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * @author Juan David Ramírez
 *
 */
public class SqlBaseCentrosCostoXUnidadConsultaDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCentrosCostoXUnidadConsultaDao.class);
	
	/**
	 * Sentencia para cargar los centros de costo por unidad de
	 * consulta para un centro de atención específico
	 */
	private static String consultarRegistrosStr="SELECT cc.identificador as id, centro_costo AS centro_costo, cc.nombre AS nombre_centro_costo, unidad_consulta AS unidad_consulta, uc.descripcion AS nombre_unidad_consulta,'"+ConstantesBD.acronimoNo+"' as modificar_activado FROM cen_costo_x_un_consulta cen INNER JOIN centros_costo cc ON(cc.codigo=cen.centro_costo) INNER JOIN unidades_consulta uc ON(uc.codigo=cen.unidad_consulta) WHERE cc.institucion=? AND cc.centro_atencion=? ORDER BY uc.descripcion";
	
	/**
	 * Sentencia para eliminar los registros de un centro de atención específico
	 */
	private static String eliminarRegistrosStr="DELETE FROM cen_costo_x_un_consulta WHERE centro_costo IN(SELECT codigo FROM centros_costo WHERE centro_atencion=?)";
	
	/**
	 * Sentencia para ingresar los registros en la BD
	 */
	private static String insertarRegistrosStr="INSERT INTO cen_costo_x_un_consulta (centro_costo, unidad_consulta) VALUES (?,?)";
	
	/**
	 * Método para consultar las
	 * opciones desplegadas en los select
	 * @param con
	 * @param tipoConsulta
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static Collection consultarListados(Connection con, int tipoConsulta, int institucion, int centroAtencion)
	{
		String consultaStr;
		try
		{
			PreparedStatementDecorator stm;
			switch(tipoConsulta)
			{
				case 1:
					/*
					 * En este caso utilizo el centro de atención
					 */
					if(centroAtencion == 0)
					{
						return null;
					}
					
					consultaStr="SELECT codigo AS codigo, nombre AS nombre, identificador as id,  identificador || ' - ' || nombre AS codigo_nombre FROM centros_costo WHERE institucion=? AND centro_atencion=? AND tipo_area="+ConstantesBD.codigoTipoAreaDirecto+" AND codigo NOT IN("+ConstantesBD.codigoCentroCostoTodos+", "+ConstantesBD.codigoCentroCostoExternos+", -1) AND tipo_entidad_ejecuta IN ('"+ConstantesIntegridadDominio.acronimoInterna+"'"+/*", '"+ConstantesIntegridadDominio.acronimoAmbos+"'"+*/")";
					stm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setInt(1, institucion);
					stm.setInt(2, centroAtencion);
					logger.info("consultarListados 1: "+consultaStr);
				break;
				case 2:
					consultaStr="SELECT codigo AS codigo, descripcion AS nombre FROM unidades_consulta WHERE activa="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY descripcion ";
					stm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("consultarListados 2: "+consultaStr);
				break;
				case 3:
					consultaStr="SELECT codigo AS codigo, consecutivo AS consecutivo, descripcion AS nombre FROM centro_atencion WHERE cod_institucion=? ORDER BY descripcion";
					stm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setInt(1, institucion);
					logger.info("consultarListados 3: "+consultaStr);
				break;
				default:
					return null;
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los listados tipo "+tipoConsulta+": "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta los centros de costo por unidad de consulta
	 * de acuerdo con el centro de atencion entregado
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return Mapa con los registros
	 */
	public static HashMap consultarRegistros(Connection con, int institucion, int centroAtencion)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarRegistrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, institucion);
			stm.setInt(2, centroAtencion);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los centros de costo por unidad de consulta para el centro de atencion "+centroAtencion+" "+e);
			return null;
		}
	}

	/**
	 * Método para guardar los datos en la BD
	 * @param con
	 * @param elementos
	 * @param centroAtencion @todo
	 * @return true si se insertó bien
	 */
	public static boolean guardar(Connection con, Vector<Vector> elementos, int centroAtencion)
	{
		PreparedStatementDecorator stm;
		try
		{
			stm =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, centroAtencion);
			stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando los registros para el centro de atencion "+centroAtencion+": "+e);
			return false;
		}
		try
		{
			stm =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int registrosInsertados=0;
			for(int i=0; i<elementos.size(); i++)
			{
				Vector<Integer> elemento=elementos.elementAt(i);
				stm.setInt(1, elemento.elementAt(0));
				stm.setInt(2, elemento.elementAt(1));
				registrosInsertados+=stm.executeUpdate();
			}
			return true;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando los registros para el centro de atencion "+centroAtencion+": "+e);
			return false;
		}
	}
}