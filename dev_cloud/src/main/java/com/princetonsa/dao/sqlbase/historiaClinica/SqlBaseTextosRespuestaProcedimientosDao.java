package com.princetonsa.dao.sqlbase.historiaClinica;

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
 * Anexo 714
 * Creado el 17 de Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class SqlBaseTextosRespuestaProcedimientosDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseTextosRespuestaProcedimientosDao.class);


	/*----------------------------------------------------------------------------------------
	 *                         ATRIBUTOS DE TEXTOS RESPUESTA PROCEDIMIENTOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Cadena utilizada para consultar los datos
	 * de Textos Respuesta Procedimientos
	 * Alias trp = textos_resp_proc= Textos Respuesta Procedimientos
	 */
	private static final String strCadenaConsulta = " SELECT" + 
		" trp.servicio AS servicio," +
		" getcodigoservicio(trp.servicio, "+ConstantesBD.codigoTarifarioCups+") AS codigocupsservicio," +
		" getnombreservicio(trp.servicio, "+ConstantesBD.codigoTarifarioCups+") AS nombrecupsservicio" +
	" FROM" +
		" textos_resp_proc trp" +
	" WHERE" +
		" trp.institucion = ?" +
	" GROUP BY" +
		" trp.servicio" +
	" ORDER BY" +
		" codigocupsservicio";
	
	/**
	 * Cadena encargada de realizar la insercion de datos
	 * de Textos Respuesta Procedimientos.
	 */
	private static final String strCadenaInsertar = "INSERT INTO " +
														"textos_resp_proc " +
													"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
	
	/**
	 * 
	 */
	private static final String strCadenaConsultaTextosServicio = "SELECT " +
		"trp.codigo AS codigo, " +
		"trp.servicio AS servicio, " +
		"trp.descripcion_texto AS descripciontexto, " +
		"trp.texto_predeterminado AS textopredeterminado, " +
		"trp.activo AS activo, " +
		"to_char(trp.fecha_modifica, 'DD/MM/YYYY') AS fechamodifica " +
	"FROM " +
		"textos_resp_proc trp " +
	"WHERE " +
		"servicio = ? " +
		"AND institucion = ? ";
	
	/**
	 * Cadena para actualizar los datos de Textos Respuesta Procedimientos
	 */
	private static final String strCadenaActualizar = "UPDATE " +
		"textos_resp_proc " +
	"SET " +
		"servicio = ?, " +//1
		"descripcion_texto = ?, " +//2
		"texto_predeterminado = ?, "+//3
		"activo = ?, " +//4
		"usuario_modifica = ?, " +//5
		"fecha_modifica = ?, " +//6
		"hora_modifica = ? " +//7
	"WHERE " +
		"institucion = ? " +//8
		"AND codigo = ? ";//9

	

	/**
	 * Cadena encargada de elimiar los servicios de textos procedimientos
	 */
	private static final String strCadenaEliminarServicio = "DELETE FROM textos_resp_proc WHERE servicio = ? AND institucion = ?";

	/**
	 * Cadena encargada de eliminar datos
	 * de Textos Respuesta Procedimientos.
	 */
	private static final String strCadenaEliminarTextosProcedimientos = "DELETE FROM textos_resp_proc WHERE codigo = ? AND institucion = ?";
	
	
	/*----------------------------------------------------------------------------------------
	 *                         FIN ATRIBUTOS DE TEXTOS RESPUESTA PROCEDIMIENTOS
	 -----------------------------------------------------------------------------------------*/
	
	/*---------------------------------------------------------------------
	 *  		METODOS PARA EL MANEJO DE TEXTOS RESPUESTA PROCEDIMIENTOS
	 ---------------------------------------------------------------------*/
	
	/**
	 * Metodo encargado de consultar la informacion de
	 * Textos Respuesta Procedimientos - Historia Clinica, en la tabla "textos_resp_proc".
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- institucion 	--> Requerido
	 * -- action 		--> Opcional
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo_,
	 * servicio_,
	 * descripcionTexto_,
	 * textoPredeterminado_,
	 * activo_,
	 * estaBd_
	 */
	public static HashMap consultaTextosRespuestaProcedimientos (Connection connection, int institucion)
	{
		HashMap mapa = new HashMap();
		mapa.put("0", "numRegistros");
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(connection.prepareStatement(strCadenaConsulta));
			logger.info("====>Consulta Textos Procedimientos: "+strCadenaConsulta);
			ps.setInt(1, institucion);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			return mapa; 
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR CONSULTANDO LOS TEXTOS DE RESPUESTA DE PROCEDIMIENTOS"+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return mapa;
	}
	
	/**
	 * Metodo encargado de insertar
	 * los datos de Textos Respuesta Procedimientos.
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- codigo 				--> Requerido
	 * -- descripcionTexto	 	--> Requerido
	 * -- textoPredeterminado 	--> Requerido
	 * -- activo				--> Requerido
	 * -- institucion 			--> Requerido
	 * @return false/true
	 */
	public static boolean insertarTextosRespuestaProcedimientos (Connection connection, HashMap datos)
	{
		boolean enTransaccion = false;
		PreparedStatementDecorator ps= null;
		try
		{
	  		ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaInsertar));
	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_textos_resp_proc"));
	  		ps.setInt(2, Utilidades.convertirAEntero(datos.get("codigoServicio")+""));
	  		ps.setString(3, datos.get("descripcionTexto")+"");
	  		ps.setString(4, datos.get("textoPredeterminado")+"");
	  		ps.setString(5, datos.get("activo")+"");
	  		ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(connection))));
	  		ps.setString(7, UtilidadFecha.getHoraActual(connection));
	  		ps.setString(8, datos.get("usuario")+"");
	  		ps.setInt(9, Utilidades.convertirAEntero(datos.get("institucion")+""));
	  		enTransaccion = (ps.executeUpdate() > 0);
	  		
	    	if(enTransaccion)
	    	{
	    		logger.info("SE INSERTO CORRECTAMENTE EL TEXTO DE RESPUESTA DE PROCEDIMIENTO");
	    		return true;
	    	}
	    	
	    	else
	    		return false;
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		
		return false;
	}
	
	/**
	 * Método encargado de cargar los textos respuesta procedimientos
	 * @param connection
	 * @param codigoServicio
	 * @return mapa
	 */
	public static HashMap cargarTextosRespuestaProcedimientos(Connection connection, int codigoServicio, int institucion)
	{
		HashMap mapa = new HashMap();
		mapa.put("0", "numRegistros");
		PreparedStatementDecorator ps= null;
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaConsultaTextosServicio, ConstantesBD.typeResultSet, 
			ConstantesBD.concurrencyResultSet ));	
			logger.info("====>Consulta Textos Respuesta Procedimientos Servicio: "+strCadenaConsultaTextosServicio);
			ps.setInt(1, codigoServicio);
			ps.setInt(2, institucion);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			return mapa; 
		} 
		
		catch (SQLException e) 
		{
			logger.error("ERROR CONSULTANDO LOS TEXTOS DE RESPUESTA DE PROCEDIMIENTOS"+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		
		return mapa;
	}
	
	/**
	 * Método encargado de actualizar la tabla de "textos_resp_proc".
	 * 
	 * 
	 * @author Felipe Pérez Granda
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo				--> Requerido
	 * -- servicio				--> Requerido
	 * -- descripcionTexto		--> Requerido
	 * -- textoPredetermiando 	--> Requerido
	 * -- activo				--> Requerido
	 * -- institucion			--> Requerido
	 * -- usuarioModifica		--> Requerido
	 * @return false/true
	 */
	public static boolean actualizaTextosRespuestaProcedimientos (Connection connection, HashMap datos)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			 ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaActualizar, ConstantesBD.typeResultSet, 
			ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, Utilidades.convertirAEntero(datos.get("codigoServicio")+""));
			ps.setString(2, datos.get("descripcionTexto")+"");
			ps.setString(3, datos.get("textoPredeterminado")+"");
			ps.setString(4, datos.get("activo")+"");
			ps.setString(5, datos.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setInt(8, Utilidades.convertirAEntero(datos.get("institucion")+""));
			ps.setInt(9, Utilidades.convertirAEntero(datos.get("codigo")+""));
			
			logger.info("===> La consulta de Actualizar es: "+strCadenaActualizar); 
			logger.info("===> codigoServicio: "+Utilidades.convertirAEntero(datos.get("codigoServicio")+""));
			logger.info("===> descripcionTexto: "+datos.get("descripcionTexto")+"");
			logger.info("===> textoPredeterminado: "+datos.get("textoPredeterminado")+"");
			logger.info("===> activo: "+datos.get("activo")+"");
			logger.info("===> usuario: "+datos.get("usuario")+"");
			logger.info("===> Fecha Actual: "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			logger.info("===> Hora Actual: "+UtilidadFecha.getHoraActual());
			logger.info("===> institucion: "+Utilidades.convertirAEntero(datos.get("institucion")+""));
			logger.info("===> codigo: "+Utilidades.convertirAEntero(datos.get("codigo")+""));
			
			if (ps.executeUpdate()>0)
			{
				logger.info("SE ACTUALIZO CORRECTAMENTE EL TEXTO DE RESPUESTA DE PROCEDIMIENTO");
				return true;
			}
		} 
		
		catch (SQLException e) 
		{
			logger.error("ERROR ACTUALIZANDO EL TEXTO DE RESPUESTA DE PROCEDIMIENTOS"+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		
		return false;
	}
	
	/**
	 * Método encargado de eliminar los textos respuesta procedimientos
	 * @param connection
	 * @param codigoServicio
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarTextosRespuestaProcedimientos(Connection connection, int codigoServicio, int institucion)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaEliminarServicio, ConstantesBD.typeResultSet, 
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, codigoServicio);
			ps.setInt(2, institucion);
			
			if (ps.executeUpdate()>0)
			{
				logger.info("SE ELIMINO CORRECTAMENTE EL TEXTO DE RESPUESTA DE PROCEDIMIENTO SEGÚN EL SERVICIO");
				return true;
			}
		}
		
		catch (SQLException e) 
		{
			logger.error("ERROR ELIMINANDO EL TEXTO DE RESPUESTA DE PROCEDIMIENTOS SEGÚN EL SERVICIO"+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		
		return false;
	}
	
	/**
	 * Método encargado de eliminar un texto prodecimiento
	 * @param connection
	 * @param codServicio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static boolean eliminarTextoProcedimiento(Connection connection, int codigo, int codigoInstitucionInt)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaEliminarTextosProcedimientos, ConstantesBD.typeResultSet, 
					ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, codigo);
			ps.setInt(2, codigoInstitucionInt);
			
			if (ps.executeUpdate()>0)
			{
				logger.info("SE ELIMINO CORRECTAMENTE EL TEXTO DE RESPUESTA DE PROCEDIMIENTO");
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR ELIMINANDO EL TEXTO DE RESPUESTA DE PROCEDIMIENTOS"+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return false;
	}

	/**
	 * Metodo encargardo de consultar los codigos de textos asociados a un servicio
	 * @param connection
	 * @param servicio
	 * @param institucion
	 * @return
	 */
	
	public static HashMap consultarCodigosTextos(Connection connection, String servicio, int institucion)
	{
		UtilidadBD.iniciarTransaccionSinMensaje(connection);
		HashMap mapaCodigos = new HashMap();
		String consulta = "SELECT codigo AS codigo FROM textos_resp_proc " +
				" WHERE servicio = "+servicio+" AND institucion = "+institucion;
		PreparedStatementDecorator ps= null;
		try 
		{
			 ps= new PreparedStatementDecorator(connection.prepareStatement(consulta));
			
			logger.info("===> Se está ejecutando esta consulta... \n"+consulta);
			
			mapaCodigos = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			return mapaCodigos;

		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando codigos asociados a un servicio : "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return null;
	}

	
	/*---------------------------------------------------------------------
	 *  		FIN METODOS PARA EL MANEJO DE TEXTOS RESPUESTA PROCEDIMEINTOS
	 ---------------------------------------------------------------------*/
}