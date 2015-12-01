
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0 11 /May/ 2006
 * Clase para las transacciones de la Consulta de los Centros de Costo
 */
public class SqlBaseConsultarCentrosCostoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultarCentrosCostoDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar los centros de costo
	 */
	private final static String consultarCentrosCostoStr=" SELECT ca.consecutivo as codigocentroatencion, " +
														 " ca.descripcion as nombrecentroatencion, " +
														 " cc.codigo as codigocentrocosto, " +
														 " cc.identificador as identificador, " +
														 " cc.nombre as descripcion, " +
														 " cc.tipo_area as codigotipoarea, " +
														 " ta.nombre as nombretipoarea, " +
														 " case when cc.manejo_camas="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as manejocamas, " +
														 " cc.unidad_funcional as unidadfuncional, " +
														 " cc.codigo_interfaz as codigo_interfaz, " +
														 " cc.tipo_entidad_ejecuta as tipoentidadejecuta, " +
														 " uf.descripcion as nombreunidadfuncional, " +
														 " case when cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as activo" +
														 " FROM centros_costo cc " +
														 " INNER JOIN centro_atencion ca ON(cc.centro_atencion=ca.consecutivo) " +
														 " INNER JOIN tipos_area ta ON(cc.tipo_area=ta.codigo) " +
														 " INNER JOIN unidades_funcionales uf ON(cc.unidad_funcional=uf.acronimo) " +
														 " WHERE cc.codigo<>"+ConstantesBD.codigoNuncaValido +
														 " AND cc.codigo<>"+ConstantesBD.codigoCentroCostoTodos  +
														 " AND cc.codigo<>"+ConstantesBD.codigoCentroCostoExternos ;
	
	
	
	/**
	 * Método para realizar la busqueda avanzada de los centros de costo segun los
	 * parametros ingresados
	 * @param con
	 * @param codCentroAtencion
	 * @param identificador
	 * @param descripcion
	 * @param codigoTipoArea
	 * @param manejoCamas
	 * @param acronimoUnidadFuncional
	 * @param activo
	 * @param tipoEntidad 
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCentrosCosto(Connection con, int codCentroAtencion, String identificador, String descripcion, int codigoTipoArea, String manejoCamas, String acronimoUnidadFuncional, String codigo_interfaz, String activo, String tipoEntidad) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps = null;
		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
		
			String avanzadaStr = "";
			
			
			if(codCentroAtencion != -1)
			{
				avanzadaStr+=" AND cc.centro_atencion="+codCentroAtencion;
			}
			if(!identificador.trim().equals(""))
			{
				avanzadaStr+=" AND UPPER(cc.identificador) LIKE UPPER('%"+identificador+"%') ";
			}
			if(!descripcion.trim().equals(""))
			{
				avanzadaStr+=" AND UPPER(cc.nombre) LIKE UPPER('%"+descripcion+"%') ";
			}
			if(codigoTipoArea != -1)
			{
				avanzadaStr+=" AND cc.tipo_area="+codigoTipoArea;
			}
			if(!manejoCamas.trim().equals(""))
			{
				if(manejoCamas.equals("true"))
				{
					avanzadaStr+=" AND cc.manejo_camas="+ValoresPorDefecto.getValorTrueParaConsultas();
				}
				else
				{
					avanzadaStr+=" AND cc.manejo_camas="+ValoresPorDefecto.getValorFalseParaConsultas();
				}
			}
			if(!acronimoUnidadFuncional.trim().equals("-1"))
			{
				avanzadaStr+=" AND cc.unidad_funcional='"+acronimoUnidadFuncional+"'";
			}
			if(!activo.trim().equals(""))
			{
				if(activo.equals("true"))
				{
					avanzadaStr+=" AND cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas();
				}
				else
				{
					avanzadaStr+=" AND cc.es_activo="+ValoresPorDefecto.getValorFalseParaConsultas();
				}
			}
			
			String consulta= consultarCentrosCostoStr + avanzadaStr+" ORDER BY ca.descripcion asc, cc.nombre asc ";
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarCentrosCosto : [SqlBaseConsultarCentrosCostoDao] "+e.toString() );
			return null;
		}	    
	}
}