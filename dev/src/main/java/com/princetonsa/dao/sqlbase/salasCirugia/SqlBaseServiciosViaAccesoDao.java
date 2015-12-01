package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;


/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SqlBaseServiciosViaAccesoDao {
	
	/**
	 * Objeto para manejar los logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseServiciosViaAccesoDao.class);
	
	/**
	 * Cadena para a consulta de los servicios de via de acceso
	 */
	private static final String consultarServiciosViaAccesoStr="SELECT codigo, getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups+") as descripcion_servicio,servicio as codigo_servicio,'"+ConstantesBD.acronimoSi+"' as estabd FROM servicios_via_acceso ORDER BY descripcion_servicio";
	
	/**
	 * Cadena para la eliminacion de los servicios de via de acceso
	 */
	private static final String eliminarServiciosViaAccesoStr="DELETE FROM servicios_via_acceso WHERE servicio=?";
	
	/**
	 * Metodo para la insercion de los servicios de via de acceso
	 * @param con
	 * @param servicio
	 * @param institucion
	 * @param cadenaInsecion
	 * @return
	 */
	public static boolean insertarServiciosViaAcceso(Connection con,HashMap vo,String cadenaInsercion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO servicios_via_acceso (codigo,servicio,fecha_modifica,hora_modifica,usuario_modifica,institucion)" +
										" VALUES ('SEQ_SERVICIOS_VIA_ACCESO'),?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoservicio")+""));
			ps.setString(2, vo.get("usuario")+"");
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("institucion")+""));
			if(ps.executeUpdate()>0)
				return true;
			logger.info("INSERTANDO");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo para la consulta de los servicios de via de acceso
	 * @param con
	 * @return
	 */
	public static HashMap consultarServiciosViaAcceso(Connection con)
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarServiciosViaAccesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Metodo para eliminar los servicios de via de acceso
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarServiciosViaAcceso(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarServiciosViaAccesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			logger.info("ELIMINANDO");
			return ps.executeUpdate()>0;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

}
