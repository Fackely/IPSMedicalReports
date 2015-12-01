package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.historiaClinica.ServiciosXTipoTratamientoOdontologico;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;



/**
 * Clase SqlBaseServiciosXTipoTratamientoOdontologicoDao
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseServiciosXTipoTratamientoOdontologicoDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseServiciosXTipoTratamientoOdontologicoDao.class);

	
	
	/*----------------------------------------------------------------------------------------
	 *                      ATRIBUTOS DE SERVICIOS X TIPO TRATAMIENTO ODONTOLOGICO
	 -----------------------------------------------------------------------------------------*/
	
	private static final String strCadenaConsultaServiciosXTipoTratamientoOdontologico= " SELECT " +
																								 " sxtto.consecutivo As consecutivo0, " +
																								 " sxtto.tipo_tratamiento As codigo_tipo_tratamiento1, " +
																								 " getNombTipoTratamientoOdoInst(sxtto.tipo_tratamiento) As nombre_tipo_tratamiento2, " +
																								 " getobtenercodigocupsserv(sxtto.servicio,"+ConstantesBD.codigoTarifarioCups+") As codigo_cups_servicio3, " +
																								 " getnombreservicio(sxtto.servicio,"+ConstantesBD.codigoTarifarioCups+") As nombre_servicio4, " +
																								 " sxtto.servicio As codigo_axioma_servicio5, " +
																								 " sxtto.uet As uet6, " +
																								 " '"+ConstantesBD.acronimoSi+"' As esta_bd7 "+
																							" FROM serv_x_tipo_trat_odont sxtto " +
																							" WHERE sxtto.institucion=? " +
																							" AND  sxtto.tipo_tratamiento=? " +
																							" ORDER BY codigo_axioma_servicio5, nombre_servicio4 ";
	
	private static final String strCadenaIngresarServiciosXTipoTratamientoOdontologico =" INSERT INTO serv_x_tipo_trat_odont (consecutivo, tipo_tratamiento, servicio, uet, institucion, fecha_modifica, hora_modifica, usuario_modifica) " +
																								 "VALUES (?,?,?,?,?,?,?,?) "; 
	
	private static final String strEliminarRegistros = " DELETE FROM serv_x_tipo_trat_odont WHERE  consecutivo=? ";
	
	
	private static final String strActualizarServiciosXTipoTratamientoOdontologico =" UPDATE serv_x_tipo_trat_odont SET servicio=?, uet=?, fecha_modifica=?, hora_modifica=?, usuario_modifica=?  WHERE consecutivo=? ";
	
	/*----------------------------------------------------
	 * INDICES
	 ----------------------------------------------------*/
	private static final String [] indicesServicioXTipoTratamientoOdontologico=ServiciosXTipoTratamientoOdontologico.indicesServicioXTipoTratamientoOdontologico;
	
	/*----------------------------------------------------------------------------------------
	 *                    FIN  ATRIBUTOS DE SERVICIOS X TIPO TRATAMIENTO ODONTOLOGICO
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Metodo encargado de actualizar los datos de servicio por tipo de tratamiento
	 * odontologico en la tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * ---------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------
	 * -- codigoAxiomaServicio5_ --> Requerido
	 * -- uet6_ --> Opional
	 * -- usuarioModifica9_ --> Requerido
	 * -- consecutivo0_ --> Requerido
	 * @return false/true
	 */
	public static boolean actualizarServicioXTipoTratamientoOdontologico (Connection connection, HashMap datos)
	{
		logger.info("\n entro a  actualizarServicioXTipoTratamientoOdontologico datos -->"+datos);
		String cadena = strActualizarServiciosXTipoTratamientoOdontologico;
		PreparedStatementDecorator ps = null;
		try 
		{
			 ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			
			/**
			 *  UPDATE serv_x_tipo_trat_odont SET servicio=?, uet=?, fecha_modifica=?, hora_modifica=?, usuario_modifica=?  WHERE consecutivo=? 
			 */
			
			//servicio
			ps.setInt(1, Utilidades.convertirAEntero(datos.get(indicesServicioXTipoTratamientoOdontologico[5])+""));
			//uet
			if (UtilidadCadena.noEsVacio(datos.get(indicesServicioXTipoTratamientoOdontologico[6])+""))
				ps.setDouble(2, Utilidades.convertirADouble(datos.get(indicesServicioXTipoTratamientoOdontologico[6])+""));
			else
				ps.setNull(2, Types.NUMERIC );
			//fecha modifica
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			//hora modifica
			ps.setString(4, UtilidadFecha.getHoraActual());
			//usuario modifica
			ps.setString(5, datos.get(indicesServicioXTipoTratamientoOdontologico[9])+"");
			//consecutivo
			ps.setDouble(6, Utilidades.convertirADouble(datos.get(indicesServicioXTipoTratamientoOdontologico[0])+""));
			
			if (ps.executeUpdate()>0)
				return true;
			else
				return false;
			
		}
		catch (Exception e)
		{
			logger.info("\n problema actualizando los servicios por tipo de tratamiento odontologico "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 *Metodo encargado de eliminar un registro de 
	 *servicios por tipo tratamiento odontologico en 
	 *la tabla  serv_x_tipo_trat_odont
	 *@author Jhony Alexander Duque A.
	 *@param connection
	 *@param consecutivo
	 *@return false/true 
	 */
	public static boolean eliminarServicosXTipoTratamiento (Connection connection, String consecutivo)
	{
		logger.info("\n entro a eliminarServicosXTipoTratamiento datos-->"+consecutivo);
		PreparedStatementDecorator ps = null;
		String cadena=strEliminarRegistros;
		
		try 
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			//consecutivo
			ps.setDouble(1, Utilidades.convertirADouble(consecutivo));
			
			if (ps.executeUpdate()>0)
				return true;
			else
				return false;
			
		}
		catch (Exception e) 
		{
			logger.info("\n problema eliminando los datos de servicios por tipo de tratamiento odontologico en la tabla serv_x_tipo_trat_odont "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
		return false;
	}
	
	
	/**
	 * Metodo encargado de ingresar los datos de servicios
	 * por tipo de tratamiento odontologico en la 
	 * tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------------
	 * codigoTipoTratamiento1_,codigoAxiomaServicio5_,
	 * uet6_,institucion8_,usuarioModifica9_
	 * @return false/true
	 */
	public static boolean insertarServicosXTipoTratamiento (Connection connection, HashMap datos)
	{
		logger.info("\n entro a insertarServicosXTipoTratamiento datos-->"+datos);
		String cadena=strCadenaIngresarServiciosXTipoTratamientoOdontologico;
		PreparedStatementDecorator ps = null;
		try 
		{
			 ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			
			/**
			 *  INSERT INTO serv_x_tipo_trat_odont (
			 *  consecutivo, tipo_tratamiento, servicio, uet, institucion, fecha_modifica, hora_modifica, usuario_modifica) 
			 */
			
			//consecutivo
			int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_serv_x_tipo_trat_odont");
			ps.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
			//tipo tratamiento
			ps.setInt(2, Utilidades.convertirAEntero(datos.get(indicesServicioXTipoTratamientoOdontologico[1])+""));
			//servicio
			ps.setInt(3, Utilidades.convertirAEntero(datos.get(indicesServicioXTipoTratamientoOdontologico[5])+""));
			//uet
			if (UtilidadCadena.noEsVacio(datos.get(indicesServicioXTipoTratamientoOdontologico[6])+""))
				ps.setDouble(4, Utilidades.convertirADouble(datos.get(indicesServicioXTipoTratamientoOdontologico[6])+""));
			else
				ps.setNull(4, Types.NUMERIC);
			//institucion
			ps.setInt(5, Utilidades.convertirAEntero(datos.get(indicesServicioXTipoTratamientoOdontologico[8])+""));
			//fecha modifica
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			//hora modifica
			ps.setString(7, UtilidadFecha.getHoraActual());
			//usuario modifica
			ps.setString(8, datos.get(indicesServicioXTipoTratamientoOdontologico[9])+"");
			
			if (ps.executeUpdate()>0)
				return true;
			else
				return false;
			
			
		}
		catch (Exception e) 
		{
			logger.info("\n problema insertando los datos de servicios por tipo de tratamiento odontologico en la tabla serv_x_tipo_trat_odont "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
		
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de 
	 * servicios por tipo tratamiento odontologico 
	 * en la tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- institucion --> Requerido
	 * -- tipoTratamientoOdontologico --> Requerido
	 * @return Mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * consecutivo0_,codigoTipoTratamiento1_,
	 * nombreTipoTratamiento2_,codigoCupsServicio3_,
	 * nombreServicio4_,codigoAxiomaServicio5_,
	 * uet6_,estaBd7_
	 */
	public static HashMap consultarServiciosXTratamientoOdontologico (Connection connection,HashMap criterios)
	{
		logger.info("\n entre a consultarServiciosXTratamientoOdontologico criterios -->"+criterios);
		String cadena = strCadenaConsultaServiciosXTipoTratamientoOdontologico;
		PreparedStatement ps = null;
		try 
		{
			 logger.info("-->"+cadena);
			 logger.info("-->"+Utilidades.convertirAEntero(criterios.get("institucion")+""));
			 logger.info("-->"+Utilidades.convertirAEntero(criterios.get("tipoTratamientoOdontologico")+""));
			 //PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.typeResultSet));
			 ps=connection.prepareStatement(cadena);
			 //institucion
			 int temp=  Utilidades.convertirAEntero(criterios.get("institucion")+"");
			 ps.setInt(1,temp);
			 //tipo de tratamiento odontologico
			 temp=  Utilidades.convertirAEntero(criterios.get("tipoTratamientoOdontologico")+"");
			 ps.setInt(2,temp);
			 HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
				ps.close();
				return mapaRetorno;
			 
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando consultarServiciosXTratamientoOdontologico "+e);
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
		
		return null;
	}
	
	
}