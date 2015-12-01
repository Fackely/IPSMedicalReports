package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoServicioIncluidoSolProc;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Juan Alejandro Cardona
 * @date Septiembre de 2008
 */


public class SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao {

	/**	* Objeto para manejar los logs de esta clase	*/
	private static Logger logger = Logger.getLogger(SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.class);

	
	private static String strSearchServArtiIncludOtroProced = "SELECT " +
																"spi.codigo AS codigosys, " +
																"cod_servi_ppal AS codservippal, " +
																"getcodigoservicio(cod_servi_ppal, ?) AS codigoserviciotarifa, " +
																"getnombreservicio(cod_servi_ppal, ?) AS nameservi, " +
																"getespecialidadservicio(cod_servi_ppal) AS nameespecialidad, " +
																"spi.activo AS activo, " +
																"getEsPosServicioPpal(cod_servi_ppal) AS espos, " +
																"spi.institucion AS institucion, " +
																"spi.usuario_modifica AS usuario, " +
																"spi.fecha_modifica AS fecha, " +
																"spi.hora_modifica AS hora," +
																"s.atencion_odontologica AS atencionodontologica " +
															"FROM " +
																"servi_ppalincluidos spi " +
															"INNER JOIN " +
																"facturacion.servicios s ON (s.codigo=spi.cod_servi_ppal) ";

	private static String strSearchServIncluServPpal= "SELECT " +
																"codigo AS codconservinclu, " +
																"cod_servippal AS codservppal, " +
																"cod_servicio AS codservinclu, " +
																"getcodigoservicio(cod_servicio, ?) AS codservinclutarifario, " +
																"getnombreservicio(cod_servicio, ?) AS descripservinclu, " +
																"getespecialidadservicio(cod_servicio) AS especialidadservinclu, " +
																"CASE WHEN getEsPosServicioPpal(cod_servicio) = 'N' THEN 'NO POS' ELSE 'POS' END AS posservinclu, " +
																"centro_costo_grupo_serv AS centrocostoseleccionado, " +
																"getnomcentrocosto(centro_costo_grupo_serv) AS nomcentrocostoseleccionado, " +
																"cantidad AS cantservinclu, " +
																"'"+ConstantesBD.acronimoSi+"' AS basedatos, "+
																"getEsPosServicioPpal(cod_servicio) AS esposervinclu, " +
																"getcodigoservicio(cod_servicio, ?) as codtarifarioservincluido " +
															"FROM " +
																"servi_incluido_servippal sis " +
															"WHERE " +
																"sis.cod_servippal = ? ORDER BY sis.codigo";

	private static String strSearchArtIncluServPpal = "SELECT " +	
															"ais.codigo AS codconartinclu, " +
															"ais.articulo_incluido AS codartinclu, " +
															"ais.cod_servi_ppal AS codservppal, " +
															"getdescripcionarticulo(articulo_incluido) AS descripartinclu, " +
															"getDescArticulo(articulo_incluido) as descripcompletaartinclu, " +
															"ais.cantidad AS cantartinclu, " +
															"ais.farmacia AS farmaciaartinclu, " +
															"getunidadmedidaarticulo(articulo_incluido) AS unidadartinclu, " +
															"CASE WHEN getposnaturalezaarticulo(a.naturaleza) ='"+(ValoresPorDefecto.getValorFalseParaConsultas().equals("false")?ValoresPorDefecto.getValorFalseCortoParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas())+"' THEN 'NO POS' ELSE 'POS' END AS posartinclu, "+
															"getobtenercodigointerfaz(ais.articulo_incluido) as codigointerfazartinclu, " +
															"getnomcentrocosto(farmacia) AS nombrefarmacia, "+
															"'"+ConstantesBD.acronimoSi+"' AS basedatos "+
														"FROM " +
															"art_incluidos_servippal ais " +
															"INNER JOIN articulo a ON (ais.articulo_incluido = a.codigo) " +
														"WHERE " +
															"ais.cod_servi_ppal = ? " +
														"ORDER BY " +
															"ais.codigo";

	private static String strSearchArtIncluServInclu = "SELECT " +	
															"asi.codigo AS codconartincluser, " +
															"asi.cod_art_incluido AS codartincluserv, " +
															"asi.cod_servi_incluido AS codartservinclu, " +
															"getdescripcionarticulo(cod_art_incluido) AS descripartincluserv, " +
															"asi.cantidad AS cantartincluserv, " +
															"asi.farmacia AS farmaciaartincluserv, " +
															"getunidadmedidaarticulo(cod_art_incluido) AS unidadartincluserv, " +
															"CASE WHEN getposnaturalezaarticulo(a.naturaleza) ="+(ValoresPorDefecto.getValorFalseParaConsultas().equals("false")?ValoresPorDefecto.getValorFalseCortoParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas())+" THEN 'NO POS' ELSE 'POS' END AS posartincluserv, "+
															"getobtenercodigointerfaz(cod_art_incluido) as codintartincluserv, " +
															"getnomcentrocosto(farmacia) AS nombrefarmacia, "+
															"'"+ConstantesBD.acronimoSi+"' AS basedatos "+
														"FROM " +
															"art_servi_incluidos asi " +
															"INNER JOIN articulo a ON (asi.cod_art_incluido = a.codigo) " +
														"WHERE " +
															"asi.cod_servi_incluido = ? " +
														"ORDER BY " +
															"asi.codigo";

	
	/**	 * Cadena INSERT para ingresar el Servicio Principal	 */
	private static String strInsertServArtiIncludOtroProced = "INSERT INTO " +
																"facturacion.servi_ppalincluidos " +
																	"(" +
																		"codigo, " +
																		"cod_servi_ppal, " +
																		"activo, " +
																		"institucion, " +
																		"usuario_modifica, " +
																		"fecha_modifica, " +
																		"hora_modifica" +
																	")" +
																"VALUES (?, ?, ?, ?, ?, ?, ?)"; 
	
	/**
	 * Cadena INSERT para ingresar los Servicios Incluidos en el Servicio Principal
	 */
	private static String strInsertServIncludServPrincipal = "INSERT INTO " +
																"facturacion.servi_incluido_servippal " +
																	"(" +
																		"codigo, " +
																		"cod_servippal, " +
																		"cod_servicio, " +
																		"centro_costo_grupo_serv, " +
																		"cantidad " +
																	")" +
																"VALUES (?, ?, ?, ?, ?)";
	
	/**	 * Cadena INSERT para ingresar los Articulos Incluidos en el Servicio Principal	 */
	private static String strInsertArtIncludServPrincipal = "INSERT INTO " +
																"facturacion.art_incluidos_servippal " +
																	"(" +
																		"codigo, " +
																		"cod_servi_ppal, " +
																		"articulo_incluido, " +
																		"cantidad, " +
																		"farmacia " +
																	")" +
																"VALUES (?, ?, ?, ?, ?)";	

	
	
	/**	 * Cadena INSERT para ingresar los Articulos Incluidos en el Servicio Incluido	 */
	private static String strInsertArtIncludServIncluido = "INSERT INTO " +
																"facturacion.art_servi_incluidos " +
																	"(" +
																		"codigo, " +
																		"cod_servi_incluido, " +
																		"cod_art_incluido, " +
																		"cantidad, " +
																		"farmacia " +
																	")" +
																"VALUES (?, ?, ?, ?, ?)";	
	

	/**
	 * Cadena UPDATE para actualizar el Servicio Principal
	 */
	private static String strUpdateServArtiIncludOtroProced = "UPDATE " +
															  	  "facturacion.servi_ppalincluidos " +
															  "SET " +
																  "activo = ?, " +
																  "institucion = ?, " +
																  "usuario_modifica = ?, " +
																  "fecha_modifica = ?, " +
																  "hora_modifica = ? " +
															  "WHERE " +
															  	  "codigo = ?";
	
	/**
	 * Cadena UPDATE para actualizar los servicios incluidos en un servicio principal
	 */
	private static String strUpdateServIncludServPrincipal = "UPDATE " +
															  	  "facturacion.servi_incluido_servippal " +
															  "SET " +
																  "centro_costo_grupo_serv = ?, " +
																  "cantidad = ? " +
															  "WHERE " +
															  	  "codigo = ?";
	
	/**
	 * Cadena UPDATE para actualizar los articulos incluidos en un servicio principal
	 */
	private static String strUpdateArtIncludServPrincipal = "UPDATE " +
															  	  "facturacion.art_incluidos_servippal " +
															  "SET " +
																  "cantidad = ?, " +
																  "farmacia = ? " +
															  "WHERE " +
															  	  "codigo = ?";
	
	/**	 * Cadena UPDATE para actualizar los articulos incluidos en un servicio incluido	 */
	private static String strUpdateArtIncludServIncluido = "UPDATE " +
															  	  "facturacion.art_servi_incluidos " +
															  "SET " +
																  "cantidad = ?, " +
																  "farmacia = ? " +
															  "WHERE " +
															  	  "codigo = ?";


	
	/** Cadena de busqueda para listar todos los codigos de los servinclus que estan siendo utilizados en el sistema
	 * y que no pueden ser tomados en cuenta al momento de crear o modificar un ppal */
	private static String strSearchServIncluUsados = "SELECT " +
														"codigo AS codconservinclu, " +
														"cod_servippal AS codservppal, " +
														"cod_servicio AS codservinclu, " +
														"'"+ConstantesBD.acronimoSi+"' AS basedatos "+
													"FROM " +
														"servi_incluido_servippal sis ";

	
	
	/**	 * Metodo para consultar los Servicios-Articulos Incluidos En Otros Procedimientos
	 * @param (con, codigoInstitucionInt)	 */
	public static HashMap consultarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, int codigoInstitucionInt, String tarifarioServicio) {
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		
		try {
	    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strSearchServArtiIncludOtroProced, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1, Utilidades.convertirAEntero(tarifarioServicio));
	    	ps.setInt(2, Utilidades.convertirAEntero(tarifarioServicio));
	    	logger.info("====>Consulta de Servicios-Artículos Incluidos Otros Procedimientos: "+strSearchServArtiIncludOtroProced);
	    	mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	    }

		catch (SQLException e) {
	        logger.error("ERROR EJECUTANDO LA CONSULTA DE SERVICIOS-ARTÍCULOS INCLUIDOS EN OTROS PROCEDIMIENTOS");
	        e.printStackTrace();
	    }
	    return mapa;
	}

	
	
	/**	 * Metodo para consultar los Servicios Incluidos en el Servicio Principal
	 * @param (con, codigoInstitucionInt)	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap cargarServiIncluServiPpal(Connection con, int criterios, int tarifario) {
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numServiInclu", "0");
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try {
	    	ps =  new PreparedStatementDecorator(con.prepareStatement(strSearchServIncluServPpal, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1, tarifario);
	    	ps.setInt(2, tarifario);
	    	ps.setInt(3, tarifario);
	    	ps.setInt(4, criterios);
	    	rs=new ResultSetDecorator(ps.executeQuery());
	    	logger.info("====>Consulta de Servicios Incluidos en Servicios Principales: "+strSearchServIncluServPpal+ " tarifario->"+tarifario+" codigo->"+criterios);
	    	mapa = UtilidadBD.cargarValueObject(rs, true, true);
	    }

		catch(Exception e){
			logger.error("############## ERROR cargarServiIncluServiPpal", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
	    }
	    return mapa;
	}

	
	/**	 * Metodo para consultar los Articulos Incluidos en el Servicio Principal
	 * @param (con, codigoInstitucionInt)	 */
	public static HashMap cargarArtIncluServiPpal(Connection con, int criterios) {
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numServiInclu", "0");
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try {
	    	ps =  new PreparedStatementDecorator(con.prepareStatement(strSearchArtIncluServPpal, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1, criterios);
	    	logger.info("===>Consulta de Articulos Incluidos en Servicios Principales: "+strSearchArtIncluServPpal+" ===>Código Servicio Principal: "+criterios);
	    	rs=new ResultSetDecorator(ps.executeQuery());
	    	mapa = UtilidadBD.cargarValueObject(rs, true, true);
	    }

		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
	    }
	    return mapa;
	}

	
	/**	 * Metodo para consultar los Articulos Incluidos en un Servicio Incluido
	 * @param (con, codigoInstitucionInt)	 */
	public static HashMap cargarArtIncluServiInclu(Connection con, int criterios) {
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");

		try {
	    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strSearchArtIncluServInclu, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1, criterios);
	    	logger.info("====>Consulta de Articulos Incluidos en Servicios Incluidos: "+strSearchArtIncluServInclu);
	    	logger.info("====>Codigo Servicio Incluido: " + criterios);

	    	mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	    }

		catch (SQLException e) {
	        logger.error("ERROR EJECUTANDO LA CONSULTA DE ARTICULOS INCLUIDOS EN SERVICIOS INCLUIDOS");
	        e.printStackTrace();
	    }
	    return mapa;
	}

	
	/** * Método que genera la inserción de un Nuevo Servicio Principal Incluido especificando el código, el servicio, si esta activo o no
	 * @param (con, criterios)
	 * @return	 */
	public static boolean insertarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, HashMap criterios)
	{
		boolean enTransaccion = false;

		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertServArtiIncludOtroProced,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_servi_ppalincluidos"));
	  		ps.setInt(2, Utilidades.convertirAEntero(criterios.get("codigoServicioPrincipal")+""));
	  		ps.setString(3, criterios.get("activo")+"");
	  		ps.setInt(4, Utilidades.convertirAEntero(criterios.get("institucion")+""));
	  		ps.setString(5, criterios.get("usuario")+"");
	  		ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(7, UtilidadFecha.getHoraActual(con));
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE INSERTO CORRECTAMENTE EL SERVICIO PRINCIPAL INCLUIDO");
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
	
	/**
	 * Método que inserta los servicios incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal
	 * @param con	 * @param codigoServicioPrincipal	 * @param codigoServicioIncluido
	 * @param centroCosto	 * @param cantidad
	 * @return	 */
	public static boolean insertarServiciosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoServicioIncluido, String centroCosto, String cantidad)
	{
		boolean enTransaccion = false;
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertServIncludServPrincipal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_servi_incluido_servippal"));
	  		ps.setInt(2, Utilidades.convertirAEntero(codigoServicioPrincipal));
	  		ps.setInt(3, Utilidades.convertirAEntero(codigoServicioIncluido));
	  		ps.setInt(4, Utilidades.convertirAEntero(centroCosto));
	  		ps.setInt(5, Utilidades.convertirAEntero(cantidad));
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE INSERTO CORRECTAMENTE EL SERVICIO INCLUIDO");
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
	
	/**
	 * Método que inserta los articulos incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoArticuloIncluido
	 * @param farmacia
	 * @param cantidad
	 * @return
	 */
	public static boolean insertarArticulosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoArticuloIncluido, String farmacia, String cantidad)
	{
		boolean enTransaccion = false;
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertArtIncludServPrincipal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_art_incluidos_servippal"));
	  		ps.setInt(2, Utilidades.convertirAEntero(codigoServicioPrincipal));
	  		ps.setInt(3, Utilidades.convertirAEntero(codigoArticuloIncluido));
	  		ps.setInt(4, Utilidades.convertirAEntero(cantidad));
	  		ps.setInt(5, Utilidades.convertirAEntero(farmacia));
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE INSERTO CORRECTAMENTE EL ARTICULO INCLUIDO");
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
	
	/**
	 * Método que actualiza los servicios incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal 
	 * @param con	 * @param codigoServicioPrincipal
	 * @param codigoServicioIncluido	 * @param centroCosto	 * @param cantidad
	 * @return	 */
	public static boolean actualizarServiciosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int centroCosto, int cantidad)
	{
		boolean enTransaccion = false;
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdateServIncludServPrincipal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		
	  		ps.setInt(1, centroCosto);
	  		ps.setInt(2, cantidad);
	  		ps.setInt(3, codigoServicioIncluido);
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ACTUALIZO CORRECTAMENTE EL SERVICIO INCLUIDO EN EL PRINCIPAL");
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

	/**
	 * Método que actualiza los articulos incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio principal 
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoArticuloIncluido
	 * @param farmacia
	 * @param cantidad
	 * @return
	 */
	public static boolean actualizarArticulosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int farmacia, int cantidad)
	{
		boolean enTransaccion = false;
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdateArtIncludServPrincipal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		
	  		ps.setInt(1, cantidad);
	  		ps.setInt(2, farmacia);
	  		ps.setInt(3, codigoServicioIncluido);
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ACTUALIZO CORRECTAMENTE EL ARTICULO INCLUIDO EN EL PRINCIPAL");
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
	
	/**
	 * Método que actualiza la información del servicio 
	 * principal según lo parametrizado por el usuario
	 * @param con
	 * @param activoServicioPrincipal
	 * @return
	 */
	public static boolean actualizarServicioPrincipal(Connection con, HashMap<String, Object> criterios)
	{
		boolean enTransaccion = false;
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdateServArtiIncludOtroProced,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		
	  		ps.setString(1, criterios.get("activoServicioPrincipal")+"");
	  		ps.setInt(2, Utilidades.convertirAEntero(criterios.get("institucion")+""));
	  		ps.setString(3, criterios.get("usuario")+"");
	  		ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(5, UtilidadFecha.getHoraActual(con));
	  		ps.setInt(6, Utilidades.convertirAEntero(criterios.get("codigoSistemaServiPpal")+""));
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ACTUALIZO CORRECTAMENTE EL SERVICIO PRINCIPAL");
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
	
	/**
	 * Método para eliminar un servicio incluido
	 * @param con
	 * @param codigoServicioIncluido
	 * @return
	 */
	public static boolean eliminarServicioIncluido(Connection con, int codigoServicioIncluido)
	{
		boolean enTransaccion = false;
		String cadena = "DELETE FROM servi_incluido_servippal WHERE codigo = "+codigoServicioIncluido+" ";
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ELIMINO CORRECTAMENTE EL SERVICIO INCLUIDO");
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

	/**
	 * Método para eliminar un articulo incluido en un ppal
	 * @param con
	 * @param codigoServicioIncluido
	 * @return
	 */
	public static boolean eliminarArticuloIncluido(Connection con, int codigoServicioIncluido)
	{
		boolean enTransaccion = false;
		String cadena = "DELETE FROM art_incluidos_servippal WHERE codigo = "+codigoServicioIncluido+" ";
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ELIMINO CORRECTAMENTE EL ARTICULO INCLUIDO");
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
	
	/**
	 * @param con
	 * @param codigoServicio
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int obtenerCodigoPkServicioIncluye(Connection con, int codigoServicio, boolean activo, int institucion)
	{
		String consulta="SELECT codigo FROM servi_ppalincluidos WHERE cod_servi_ppal=? and activo=? and institucion=? ";
		ResultSetDecorator rs=null;
		PreparedStatementDecorator ps=null;
		try 
		{
			logger.info("\n obtenerCodigoPkServicioIncluye->"+consulta+" serv->"+codigoServicio+" activo->"+activo+" inst->"+institucion+" \n");
		    ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    ps.setInt(1, codigoServicio);
		    ps.setString(2, UtilidadTexto.convertirSN(activo+""));
		    ps.setInt(3, institucion);
		    rs=new ResultSetDecorator(ps.executeQuery());
		    	
			if (rs.next())
				return rs.getInt("codigo");
		} 
		catch(Exception e){
			logger.error("############## ERROR obtenerCodigoPkServicioIncluye", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean solicitudIncluyeServiciosArticulos(Connection con, int numeroSolicitud,String consulta)
	{
		
		try 
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setInt(1, numeroSolicitud);
	  		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	  		if(rs.next())
	  			return rs.getInt(1)>0;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarServiciosIncluidosSolicitudProcedimientos(Connection con, DtoServicioIncluidoSolProc dto)
	{
		String consulta="INSERT INTO serv_inclu_sol_proc 	(" +
																"solicitud_ppal , " +			//1
																"solicitud_incluida , " +		//2
																"servicio_ppal , " +			//3
																"servicio_incluido , " +		//4
																"centro_costo_ejecuta , " +		//5
																"fecha_modifica , " +			
																"hora_modifica , " +
																"usuario_modifica" +			//6
															")" +
															"VALUES " +
															"( ?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
		
		try 
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		ps.setInt(1, dto.getSolicitudPpal());
	  		ps.setInt(2, dto.getSolicitudIncluida());
	  		ps.setInt(3, dto.getServicioPpal());
	  		ps.setInt(4, dto.getServicioIncluido());
	  		ps.setInt(5, dto.getCentroCostoEjecuta());
	  		ps.setString(6, dto.getUsuarioModifica());
	  		
	  		if(ps.executeUpdate() > 0)
	  		{	
        		logger.info("SE INSERTO CORRECTAMENTE EL SERVICIO PRINCIPAL INCLUIDO");
        		return true;
        	}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarArticulosIncluidosSolicitudProcedimientos(Connection con, DtoArticuloIncluidoSolProc dto)
	{
		String consulta="INSERT INTO art_inclu_sol_proc 	(" +
																"solicitud_ppal , " +		//1
																"articulo , " +				//2
																"servicio_ppal , " +		//3
																"cantidad , " +				//4
																"cantidad_maxima , " +		//5
																"farmacia , " +				//6
																"es_servicio_incluido , " +	//7
																"solicitud_incluida , " +	//8
																"fecha_modifica , " +		
																"hora_modifica , " +		
																"usuario_modifica" +		//9
															")" +
															"VALUES " +
															"( ?,?,?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
		
		try 
		{
			
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		ps.setInt(1, dto.getSolicitudPpal());
	  		ps.setInt(2, dto.getArticuloIncluido());
	  		ps.setInt(3, dto.getServicioPpal());
	  		ps.setInt(4, dto.getCantidad());
	  		ps.setInt(5, dto.getCantidadMaxima());
	  		ps.setInt(6, dto.getFarmacia());
	  		ps.setString(7, UtilidadTexto.convertirSN(dto.getEsServicioIncluido()+""));
	  		
	  		if(dto.getSolicitudIncluida()>0)
	  			ps.setInt(8, dto.getSolicitudIncluida());
	  		else
	  			ps.setNull(8, Types.INTEGER);
	  		
	  		ps.setString(9, dto.getUsuarioModifica());
	  		
	  		if(ps.executeUpdate() > 0)
	  		{	
        		logger.info("SE INSERTO CORRECTAMENTE EL ARTÍCULO PRINCIPAL INCLUIDO");
        		return true;
        	}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param solicitudPpal
	 * @param articulo
	 * @param cantidad
	 * @param usuario
	 * @return
	 */
	public static boolean modificarCantidadArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int cantidad, String usuario)
	{
		String consulta="UPDATE art_inclu_sol_proc SET cantidad=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica=? WHERE solicitud_ppal=? and articulo=? ";
		
		logger.info("#############################modificarCantidadArticulosIncluidosSolicitudProcedimientos->"+consulta+"  cantidad->"+cantidad+" usu->"+usuario+" solppal->"+solicitudPpal+" art->"+articulo);
		try 
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		ps.setInt(1, cantidad);
	  		ps.setString(2, usuario);
	  		ps.setInt(3, solicitudPpal);
	  		ps.setInt(4, articulo);
	  		
	  		if(ps.executeUpdate() > 0)
	  		{	
        		logger.info("SE MODIFICO LA CANTIDAD DEL ARTICULO INCLUIDO");
        		return true;
        	}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param solicitudPpal
	 * @param articulo
	 * @param solicitudIncluida
	 * @param usuario
	 * @return
	 */
	public static boolean modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int solicitudIncluida, String usuario)
	{
		String consulta="UPDATE art_inclu_sol_proc SET solicitud_incluida=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica=? WHERE solicitud_ppal=? and articulo=? ";
		
		logger.info("#############################modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos->"+consulta+"  solicitudIncluida ->"+solicitudIncluida+" usu->"+usuario+" solppal->"+solicitudPpal+" art->"+articulo);
		try 
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		if(solicitudIncluida <= 0 )
	  			ps.setNull(1,Types.INTEGER);
	  		else	  			
	  			ps.setInt(1, solicitudIncluida);
	  		
	  		ps.setString(2, usuario);
	  		ps.setInt(3, solicitudPpal);
	  		ps.setInt(4, articulo);
	  		
	  		if(ps.executeUpdate() > 0)
	  		{	
        		logger.info("SE MODIFICO LA SOLICITUD INCLUIDA DEL ARTICULO INCLUIDO");
        		return true;
        	}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	
	
	
	
	/**	 * Método que inserta los articulos incluidos dentro
	 * de un servicio incluido ya almacenado
	 * @param con, codigoServicioIncluido, codigoArticuloIncluido, farmacia, cantidad
	 * @return	 */
	public static boolean insertarArticulosIncluidosEnServicioIncluido(Connection con, String codigoServicioIncluido, String codigoArticuloIncluido, String farmacia, String cantidad)
	{
		boolean enTransaccion = false;
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertArtIncludServIncluido,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_art_servi_incluidos"));
	  		ps.setInt(2, Utilidades.convertirAEntero(codigoServicioIncluido));
	  		ps.setInt(3, Utilidades.convertirAEntero(codigoArticuloIncluido));
	  		ps.setInt(4, Utilidades.convertirAEntero(cantidad));
	  		ps.setInt(5, Utilidades.convertirAEntero(farmacia));
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE INSERTO CORRECTAMENTE EL ARTICULO INCLUIDO");
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

	/**
	 * Método que actualiza los articulos incluidos dentro
	 * de un procedimiento que tiene ya establecido un 
	 * servicio incluido 
	 * @param con
	 * @param codigoServicioIncluido
	 * @param codigoArticuloIncluido
	 * @param farmacia
	 * @param cantidad
	 * @return */
	public static boolean actualizarArticulosIncluidosEnServicioIncluido(Connection con, int codigoServicioIncluido, int farmacia, int cantidad) {
		boolean enTransaccion = false;
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdateArtIncludServIncluido,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		
	  		ps.setInt(1, cantidad);
	  		ps.setInt(2, farmacia);
	  		ps.setInt(3, codigoServicioIncluido);

	  		logger.info("===========");
	  		logger.info("Cadena Actualizar Art en Servinclu: " + strUpdateArtIncludServPrincipal);	  		
	  		logger.info("===========");
	  		logger.info(" " + cantidad + " , " + farmacia + " , " + codigoServicioIncluido);
	  		
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ACTUALIZO CORRECTAMENTE EL ARTICULO INCLUIDO EN EL SERVICIO INCLUIDO");
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



	/**	 * Método para eliminar un articulo incluido en un serv incluido
	 * @param (con, codigoServicioIncluido)
	 * @return	 */
	public static boolean eliminarArticuloIncluidoServinclu(Connection con, int codigoServicioIncluido) {
		boolean enTransaccion = false;
		String cadena = "DELETE FROM art_servi_incluidos WHERE codigo = "+codigoServicioIncluido+" ";
		
		try {
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion) {
        		logger.info("SE ELIMINO CORRECTAMENTE EL ARTICULO INCLUIDO EN EL SERVINCLU");
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

	

	/**	 * Metodo para consultar los Servicios-Articulos Incluidos En Otros Procedimientos
	 * @param (con, especifico, codpertenece)
	 * @return */
	public static HashMap consultarServIncluUsados(Connection con, int especifico, int codpertenece) {

		String consulta = strSearchServIncluUsados;
		
		// la bandera especifico indica si es para uno nuevo o si es para cargar los valores de uno ya creado 
		
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		
		//se busca para todos los servinclus almacenados en el sistema
		if(especifico == 0){
			consulta += " GROUP BY sis.cod_servippal, sis.cod_servicio, sis.codigo, 4 "/*el cuatro es basedatos, toca asi pa q funcione oracle*/;
		}
		//es para uno especifico
		else {
			consulta +=	"WHERE "+
							"sis.cod_servippal = "+ codpertenece + " " +
							" GROUP BY sis.cod_servippal, sis.cod_servicio, sis.codigo, 4 "/*el cuatro es basedatos, toca asi pa q funcione oracle*/;
		}			
			
		
		try {
	    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	    	logger.info("====>Consulta de Servicios Incluidos Otros Procedimientos: "+consulta);
	    	mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	    }

		catch (SQLException e) {
	        logger.error("ERROR EJECUTANDO LA CONSULTA DE TODOS LOS SERVICIOS INCLUIDOS EN OTROS PROCEDIMIENTOS");
	        e.printStackTrace();
	    }
	    return mapa;
	}
	
	
	
}