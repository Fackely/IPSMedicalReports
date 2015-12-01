/**
 * Juan David Ramírez 31/05/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Juan David Ramírez
 *
 */
public class SqlBaseArticulosXMezclaDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseArticulosXMezclaDao.class);

	/**
	 * Sentencia para ingresar artículos por mezcla
	 */
	private static String ingresarAtriculoXMezclaStr="INSERT INTO articulos_por_mezcla(articulo, mezcla) VALUES (?,?)";
	
	/**
	 * Sentencia para eliminar artículos por mezcla
	 */
	private static String eliminarAtriculoXMezclaStr="DELETE FROM articulos_por_mezcla WHERE articulo=? AND mezcla=?";

	/**
	 * Sentencia para consultar los artículos por mezcla
	 */
	private static String consultarAtriculosXMezclaStr=	"SELECT " +
															"am.articulo AS articulo, " +
															"getdescarticulo(am.articulo) AS nombre " +
														"FROM " +
															"articulos_por_mezcla am " +
															"INNER JOIN mezcla m ON(am.mezcla=m.consecutivo) " +
														"WHERE " +
															"m.cod_institucion=? " +
															"AND am.mezcla=? " +
														"ORDER BY am.articulo";

	/**
	 * Método para consultar lkas opciones de los selectores de la funcionalidad
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoConsulta
	 * @return
	 */
	public static Collection consultarTipos(Connection con, int codigoInstitucion, int tipoConsulta)
	{
		PreparedStatementDecorator pst;
		String consulta="";
		switch (tipoConsulta)
		{
			case 1:
			{
				consulta="SELECT m.consecutivo AS consecutivo, m.codigo AS codigo, m.nombre AS nombre, tm.nombre AS tipo_mezcla FROM mezcla m INNER JOIN tipo_mezcla tm ON(tm.codigo=m.cod_tipo_mezcla) WHERE activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND cod_institucion=?";
			}
			break;
			case 2:
			{
				consulta="SELECT a.codigo AS codigo, getdescarticulo(a.codigo) AS nombre " +
						"		 FROM articulo a " +
									" inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and na.institucion=a.institucion) " +
						"			  WHERE a.institucion=?	" +
						"				AND na.es_medicamento='"+ConstantesBD.acronimoSi+"' ";

				//consulta="SELECT codigo AS codigo, getdescarticulo(codigo) AS nombre FROM view_articulos WHERE institucion=?";
			}	
			break;
			default:
				logger.error("No se especificó un tipo de consulta válido");
			return null;
		}
		try
		{
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoInstitucion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando tipos "+tipoConsulta+" "+e);
			return null;
		}
	}
	
	/**
	 * Método para ingresar o modificar los datos de
	 * los artículos por mezcla
	 * @param con
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public static int ingresarModificar(Connection con, Vector <Integer>articulosIngresados, Vector <Integer>articulosEliminados, int mezcla)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(ingresarAtriculoXMezclaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int numRegistrosModificados=0;
			stm.setInt(2, mezcla);
			for(int i=0; i<articulosIngresados.size(); i++)
			{
				int codigoArticulo=articulosIngresados.elementAt(i);
				stm.setInt(1, codigoArticulo);
				numRegistrosModificados+=stm.executeUpdate();
			}
			stm= new PreparedStatementDecorator(con.prepareStatement(eliminarAtriculoXMezclaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(2, mezcla);
			for(int i=0; i<articulosEliminados.size(); i++)
			{
				int codigoArticulo=articulosEliminados.elementAt(i);
				stm.setInt(1, codigoArticulo);
				numRegistrosModificados+=stm.executeUpdate();
			}
			return numRegistrosModificados;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando los articulos por mezcla "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * Método para ingresar o modificar los datos de
	 * los artículos por mezcla
	 * @param con
	 * @param codigoMezcla @todo
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public static HashMap consultar(Connection con, int codigoInstitucion, int codigoMezcla)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarAtriculosXMezclaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoInstitucion);
			stm.setInt(2, codigoMezcla);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los articulos por mezcla "+e);
			return null;
		}
	}
	
}
