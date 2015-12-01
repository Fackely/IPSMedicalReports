package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SqlBaseTiposMonedaDao {
	
	/**
	 * Objeto para manejar los logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTiposMonedaDao.class);
	
	/**
	 * Cadena para la insercion de los tipos de monedas
	 */
	private static final String insertarTiposMonedaStr="INSERT INTO administracion.tipos_moneda (codigo,codigo_tipo_moneda,descripcion,simbolo,institucion,fecha_modificacion,hora_modificacion,usuario_modificacion) VALUES (?,?,?,?,?";
	
	private static final String insertarTiposMonedaFechaOracle=",(select TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') from dual),substr((select to_char(sysdate, 'hh:mi:ss') from dual), 1,5)";
	
	private static final String insertarTiposMonedaFechaPostgres=",CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+"";
		
	private static final String insertarTiposMonedaStr1=",?)";
	
	/**
	 * Cadena para la consulta de los tipod de monedas
	 */
	private static final String consultarTiposMonedaStr="SELECT codigo as codigo, codigo_tipo_moneda as codigomoneda,"+
														"descripcion as descripcion,simbolo as simbolo,"+
														"'"+ConstantesBD.acronimoSi+"' AS estabd "+
														"FROM administracion.tipos_moneda";
	
	/**
	 * Cadena para la modificacion de los tipos de monedas
	 */
	private static final String modificarTiposMonedaStrOra="UPDATE administracion.tipos_moneda SET codigo_tipo_moneda=?,descripcion=?,simbolo=?,institucion=?,fecha_modificacion=(select TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') from dual),hora_modificacion=substr((select to_char(sysdate, 'hh24:mi:ss') from dual), 1,5),usuario_modificacion=? WHERE codigo=?";
	
	private static final String modificarTiposMonedaStrPos="UPDATE administracion.tipos_moneda SET codigo_tipo_moneda=?,descripcion=?,simbolo=?,institucion=?,fecha_modificacion=CURRENT_DATE,hora_modificacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_modificacion=? WHERE codigo=?";
	
	/**
	 * Cadena para la eliminacion de los tipos de monedas
	 */
	private static final String eliminarTiposMondeaStr="DELETE FROM administracion.tipos_moneda WHERE codigo=?";
	
	/**
	 * Insertar los tipos de monedas
	 * @param con
	 * @param vo
	 * @param Tipo_BD 
	 * @return
	 */
	public static boolean insertarTiposMoneda(Connection con,HashMap vo, int Tipo_BD)
	{
		try
		{
			String cadenainsertarTiposMoneda="";
			
			switch (Tipo_BD)
			{
			
			case DaoFactory.POSTGRESQL:
				cadenainsertarTiposMoneda+=insertarTiposMonedaStr+insertarTiposMonedaFechaPostgres+insertarTiposMonedaStr1;
							break;
			case DaoFactory.ORACLE:
				cadenainsertarTiposMoneda+=insertarTiposMonedaStr+insertarTiposMonedaFechaOracle+insertarTiposMonedaStr1;
				break;
				default:
					break;
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenainsertarTiposMoneda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cadena=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_tipos_moneda");
			ps.setInt(1, cadena);			
			logger.info("posicion 1 ------->"+cadena);
			ps.setString(2, vo.get("codigomoneda")+"");
			
			ps.setString(3, vo.get("descripcion")+"");
			
			ps.setString(4, vo.get("simbolo")+"");
			
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(6, vo.get("usuario")+"");
			logger.info("HOLA MUNDO2----------------------->"+insertarTiposMonedaStr);
			logger.info("INSERTANDO-.......************-> "+vo);
			if(ps.executeUpdate()>0)
				return true;
			logger.info("INSERTANDO--> "+vo);
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Consultar los tipos de monedas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarTiposMoneda(Connection con)
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposMonedaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Modificar los tipos de monedas
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarTiposMoneda(Connection con, HashMap vo, int tipoBD)
	{
		try
		{
			String modificarTiposMonedaStr="";
			switch(tipoBD)
			{
			case DaoFactory.ORACLE:
				modificarTiposMonedaStr = modificarTiposMonedaStrOra;
				break;
			case DaoFactory.POSTGRESQL:
				modificarTiposMonedaStr = modificarTiposMonedaStrPos;
				break;
			default:
				break;
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarTiposMonedaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigomoneda")+"");
			ps.setString(2, vo.get("descripcion")+"");
			ps.setString(3, vo.get("simbolo")+"");
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(5, vo.get("usuario")+"");
			ps.setString(6, vo.get("codigo")+"");
			if(ps.executeUpdate()>0)
				return true;
			logger.info("MODFICANDO-->> "+vo);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Eliminar los tipos de monedas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarTiposMoneda(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarTiposMondeaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

}
