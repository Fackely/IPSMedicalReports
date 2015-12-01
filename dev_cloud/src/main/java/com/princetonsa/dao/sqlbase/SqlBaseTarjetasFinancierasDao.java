
/*
 * Creado   20/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;


/** 
 * Objeto usado para el acceso común a la fuente de datos
 * de la funcionalidad tarjetas financieras 
 *
 * @version 1.0, 20/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseTarjetasFinancierasDao 
{
    /**
     * Manejador de los Logs de la clase
     */
    private static Logger logger=Logger.getLogger(SqlBaseTarjetasFinancierasDao.class);
    
    
    /**
     * query para consultar los datos de un solo registro
     */
    private static String consultarTarjetaStr = "SELECT " +
    												"tf.consecutivo AS consecutivo, " + 
    												"tf.codigo AS codigo_tarjeta,"+
    												"tf.entidad_financiera AS codigo_entidad," +
    												"tf.descripcion AS descripcion_tarjeta," +
    												"tf.tipo_tarjeta_financiera AS tipo_tarjeta_financiera," +
    												"tf.base_rete AS base_rete," +
    												"tf.retefte AS retefte," +
    												"tf.reteica AS reteica," +
    												"tf.comision AS comision," +
    												"tf.directo_banco AS directo_banco," +
    												"tf.activo AS activo, " +
    												"'BD' AS tiporegistro, " +
    												"tf.consecutivo AS consecutivo, " +
    												"tesoreria.tieneTarjetaFinancieraMovimi(tf.consecutivo) AS tiene_movimientos, " +
    												"ef.activo AS activo_entidad " +
	                                             "FROM " +
	                                             	"tarjetas_financieras tf " +
	                                             	"INNER JOIN entidades_financieras ef ON (tf.entidad_financiera = ef.consecutivo) " +
	                                             "WHERE " +
	                                             	"1 = 1 ";
 
    private static String cadenaModificarTarjeta = " UPDATE tarjetas_financieras set entidad_financiera = ?,descripcion = ?,tipo_tarjeta_financiera = ?,base_rete = ?,retefte = ?,reteica = ?,comision = ?,directo_banco = ?,activo = ? WHERE consecutivo = ?";


    /**
     * string para eliminar un registro
     */
    private static String eliminarTarjetaStr="DELETE FROM tarjetas_financieras WHERE consecutivo=?" ; // AND institucion=?";

    /**
     * String para consulta de Entidades Financieras - Terceros
     */
    private static String consultarEntidadFinacieranTerceroStr = "SELECT " +
    																"ef.consecutivo AS consecutivo, " +
    																"tf.numero_identificacion AS numero_identificacion, " + 
    																"tf.descripcion AS descripcion, " +
    																"ef.activo AS activo_entidad " +
															     "FROM " +
															     	"entidades_financieras ef, " +
															     	"terceros tf " +
															     "WHERE " +
															     	"ef.tercero = tf.codigo ";
    /**
     * metodo para consultar los datos de un solo registro
     * @param con Connection
     * @param consecutivo int
     * @return HashMap
     */
    public static HashMap consultarInfoTarjetaFinanciera(Connection con, HashMap vo)
    {
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena = consultarTarjetaStr;
		if(vo.containsKey("institucion"))
			cadena+=" AND tf.institucion = "+vo.get("institucion")+" ORDER BY tf.codigo";
		else if(vo.containsKey("consecutivo"))
			cadena+=" AND tf.consecutivo = "+vo.get("consecutivo");
		try
		{
			logger.info("====>Consulta Tarjetas Financieras: "+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;    

    }
    
    /**
     * metodo para eliminar un registro de una
     * tarjeta financiera
     * @param con
     * @param codEntidad
     * @param descripcion
     * @param tipoTarjeta
     * @param baseRete
     * @param retefte
     * @param reteica
     * @param comision
     * @param directoBanco
     * @param activo
     * @return boolean
     */
    public static boolean modificarTarjetas(Connection con, HashMap vo )
    {
		try
		{
			//cadenaModificarTarjeta = " UPDATE tarjetas_financieras set entidad_financiera = ?,descripcion = ?,tipo_tarjeta_financiera = ?,
			//"base_rete = ?,retefte = ?,reteica = ?,comision = ?,directo_banco = ?,activo = ? WHERE codigo = ?";			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarTarjeta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigo_entidad")+"");
			ps.setString(2, vo.get("descripcion_tarjeta")+"");
			ps.setString(3, vo.get("tipo_tarjeta_financiera")+"");
			ps.setString(4, vo.get("base_rete")+"");
			ps.setString(5, vo.get("retefte")+"");
			ps.setString(6, vo.get("reteica")+"");
			ps.setString(7, vo.get("comision")+"");
			ps.setBoolean(8, UtilidadTexto.getBoolean(vo.get("directo_banco")+""));
			ps.setBoolean(9, UtilidadTexto.getBoolean(vo.get("activo")+""));
			ps.setString(10, vo.get("consecutivo")+"");
			///CODIGO????
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;    	
    }    
    
    /**
     * metodo para insertar los registros
     * de tarjetas financieras
     * @param con Connection
     */
    public static boolean insertarTarjetas(Connection con,HashMap vo,String cadena)
    //String query,String codTarjeta,int codEntidad,String descripcion,String tipoTarjeta,double baseRete,
	//double retefte,double reteica,double comision, boolean directoBanco,boolean activo, int institucion)
    {

		try
		{

			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigo_tarjeta")+"");
			ps.setString(2, vo.get("codigo_entidad")+"");
			ps.setString(3, vo.get("descripcion_tarjeta")+"");
			ps.setString(4, vo.get("tipo_tarjeta_financiera")+"");
			if(UtilidadTexto.isEmpty(vo.get("base_rete")+""))
				ps.setObject(5, "0");
			else
				ps.setString(5, vo.get("base_rete")+"");
			if(UtilidadTexto.isEmpty(vo.get("retefte")+""))
				ps.setObject(6, "0");
			else
				ps.setString(6, vo.get("retefte")+"");
			if(UtilidadTexto.isEmpty(vo.get("reteica")+""))
				ps.setObject(7, "0");
			else
				ps.setString(7, vo.get("reteica")+"");
			if(UtilidadTexto.isEmpty(vo.get("comision")+""))
				ps.setObject(8, "0");
			else
				ps.setString(8, vo.get("comision")+"");
			ps.setBoolean(9, UtilidadTexto.getBoolean(vo.get("directo_banco")+""));
			ps.setBoolean(10, UtilidadTexto.getBoolean(vo.get("activo")+""));
			ps.setString(11, vo.get("institucion")+"");
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
		
    }    

    /**
     * metodo para eliminar un registro de una
     * tarjeta financiera
     * @param con
     * @param codTarjeta
     * @param institucion
     * @return boolean
     */
    public static boolean eliminarTarjetas(Connection con, String consecutivo)
    {
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarTarjetaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, consecutivo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
		
    }

    /**
     * Llenar el HasMap para el combo en el JSP de Entidades Financieras
     * @param con
     * @param vo
     * @return
     */
    public static HashMap consultarInfoEntidadFinacieranTercero(Connection con)
    {
		HashMap mapa = new HashMap();
		String cadena = consultarEntidadFinacieranTerceroStr;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("===>Tarjetas Financieras: "+consultarEntidadFinacieranTerceroStr);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;    

    }
    
}
