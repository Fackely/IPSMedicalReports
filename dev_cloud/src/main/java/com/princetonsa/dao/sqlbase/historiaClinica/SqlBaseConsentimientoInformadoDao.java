package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoConsentimientoInformadoOdonto;
import com.sies.mundo.UtilidadLogs;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadClonar;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author jhony Alexander Duque A
 * jduque@princetonsa.com  
 * */

public class SqlBaseConsentimientoInformadoDao
{
	/**
	 * objeto para manejar el log de la clase
	 */
	
	private static Logger logger =Logger.getLogger(SqlBaseConsentimientoInformadoDao.class);
	
	/**
	 * 	Cadena de insercion de detalle consentimiento informado
	 */	
	private static final String cadenaInsertardetalleStr = "INSERT INTO historiaclinica.detalleconsentimientoinf (codigo_pk,codigoconsentimiento, institucion, codigogruposservicios,programa_odontologico, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena de modificacion de consentimiento informado
	 * */ 
	private static final String cadenaModificarStr = "UPDATE historiaclinica.consentimientoinformado SET descripcion=?, nombre_archivo=?, nombre_original=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo=? AND institucion=? ";
 
	/**
	 * Cadena de modificacion de consentimiento informado
	 * */ 
	private static final String cadenaModificarDetalleStr = "UPDATE historiaclinica.detalleconsentimientoinf SET codigogruposservicios=?,programa_odontologico=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE codigo_pk=?";

	
	/**
	 * Cadena de eliminacion de consentimiento informado
	 * */ 
	private static final String cadenaEliminarStr = "DELETE FROM historiaclinica.consentimientoinformado WHERE codigo=? AND institucion=? ";
	
	/**
	 * Cadena de eliminacion de detalle consentimiento informado
	 * */ 
	private static final String cadenaEliminardetalleStr = "DELETE FROM historiaclinica.detalleconsentimientoinf  WHERE codigoconsentimiento=? AND institucion=?";
	
		
	/**
	 * vector sting de indice del mapa de consentimiento informado
	 * */
	private static final String[] indicesMapa = {"codigo_","institucion_","descripcion_","nombrearchivo_","nombreoriginal_","estabd_"};
 
	private static final String [] indicesMapaDetalle = {"codigopk_","codigoconsent_","institucion_","codigogrupser_","estabd_","codigogrupserold_","programaodontologico_","programaodontologicoold_"};
	
	/**
	 * indices para la consulta de consentimiento informado por ingreso
	 */
	private static final String [] indicesMapaIngreso = {"servicio_","nombreservicio_","gruposervicio_","nombregruposervicio_","nombreoriginaltmp_","nombrearchivo_","existe_","ingreso_","codigoconsentimiento_","descripcion_"};
	
	/**
	 * indices para la consulta de los formatos.
	 */
	private static final String [] indicesFormatos = {"codigo_","nombrearchivo_","nombreoriginal_","descripcion_","existe_"};
	
	
	/**
	 * Cadena de consulta de consentimiento informado
	 * */
	private static String cadenaConsultaStr = "SELECT "+
	 													 "codigo AS codigo, "+
	 													 "institucion AS institucion, " +
	 													 "descripcion AS descripcion, "+
	 													 "nombre_archivo AS nombrearchivo, "+
	 													 "nombre_original AS nombreoriginal, "+
	 													 "usuario_modifica AS usuariomodifica, "+	 													 
	 													 "'"+ConstantesBD.acronimoSi+"' AS estabd " +
	 											   "FROM historiaclinica.consentimientoinformado ";
	
	/**
	 * Cadena de consulta de detalle consentimiento informado
	 * */
	private static String cadenaConsultadetalleStr = "SELECT "+
														 "codigo_pk as codigopk," +
														 "codigoconsentimiento AS codigoconsent, "+
														 "institucion AS institucion, " +
														 "codigogruposservicios AS codigogrupser," +
														 "codigogruposservicios AS codigogrupserold, "+
														 "programa_odontologico as programaodontologico, " +
														 "programa_odontologico as programaodontologicoold, "+
														 "usuario_modifica AS usuariomodifica, "+														 
														 "'"+ConstantesBD.acronimoSi+"' AS estabd " +
												"FROM  historiaclinica.detalleconsentimientoinf";
	
	
	/**
	 * Cadena de consulta de consentimientos informados apartir del codigo del servicio 
	 * */
	private static String strConsultaConsentimientoInf = "SELECT " +
			"ci.codigo AS codigo," +
			"ci.institucion AS institucion," +
			"ci.descripcion AS descripcion," +			
			"ci.nombre_archivo AS nombrearchivo," +
			"ci.nombre_original AS nombreoriginal " +
			"FROM consentimientoinformado ci " +
			"WHERE institucion = ? AND " +
			"(ci.codigo =  (SELECT de.codigoconsentimiento " +
			"					FROM detalleconsentimientoinf de " +
			"					WHERE de.codigoconsentimiento = ci.codigo " +
			"					AND de.codigogruposservicios = getcodigogruposervicio(?) AND de.institucion = ci.institucion) " +
			"OR "+ 
			"ci.codigo NOT IN(SELECT de.codigoconsentimiento FROM detalleconsentimientoinf de) )"+	
			"GROUP BY ci.codigo,ci.institucion,ci.descripcion,ci.nombre_archivo,ci.nombre_original " +
			"ORDER BY ci.descripcion ASC ";	
	
	/**
	 * Cadena de consulta de consentimientos informados apartir del codigo del programa 
	 * */
	private static String strConsultaConsentimientoGrupoPrograma = 
		
		"SELECT ci.codigo     AS codigo, "+
			"ci.institucion     AS institucion, "+
			"ci.descripcion     AS descripcion, "+
			"ci.nombre_archivo  AS nombrearchivo, "+
			"ci.nombre_original AS nombreoriginal "+
		"FROM consentimientoinformado ci "+
		"INNER JOIN "+
			"detalleconsentimientoinf de "+
				"ON(ci.codigo=de.codigoconsentimiento AND de.institucion = ci.institucion) "+
		"WHERE " +
			"programa_odontologico     = ? "+
		"AND " +
			"ci.institucion            = ? "+
		"GROUP BY ci.codigo, "+
			"ci.institucion, "+
			"ci.descripcion, "+
			"ci.nombre_archivo, "+
			"ci.nombre_original "+
		"ORDER BY " +
			"ci.descripcion ASC ";
	
	
	/**
	 * cadena de inserccion para el historial de impresiones de consentimiento informado	 *  
	 * */
	private static String strInsertarHistorialConsentimiento = "INSERT INTO " +
			"historialconsentimientoinf (ingreso," +
			"							 servicio," +
			"							 codigoconsentimiento," +
			"							 institucion," +
			"							 descripcion," +
			"							 nombre_archivo," +
			"							 usuario_imprime," +
			"							 fecha_imprime," +
			"							 hora_imprime,plan_tratamiento,codigo_programa) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?) ";	
	
	
	
	/**
	 * Cadena de consulta de Consentmiento Informado
	 */
	private static String strCadenaConsultaconsentimientoInf = "SELECT DISTINCT *  FROM "+ 
		"( "+
			"( "+
			"SELECT "+ 
			"scxs.servicio AS servicio, "+ 
			"getnombreservicio(scxs.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio, "+ 
			"serv.grupo_servicio AS gruposervicio, "+ 
			"c.id_ingreso AS ingreso, "+
			"getnombregruposervicio(serv.grupo_servicio) AS nombregruposervicio "+ 
			"FROM cuentas c "+ 
			"INNER JOIN solicitudes s ON(s.cuenta = c.id) "+ 
			"INNER JOIN sol_cirugia_por_servicio scxs ON(scxs.numero_solicitud = s.numero_solicitud) "+ 
			"INNER JOIN servicios serv on (serv.codigo = scxs.servicio) "+
			"WHERE "+ 
			"c.id_ingreso=? "+
			") UNION ( "+ 
			"SELECT ssc.servicio AS servicio, "+ 
			"getnombreservicio(ssc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio, "+ 
			"ser.grupo_servicio AS gruposervicio, "+ 
			"sc.ingreso AS ingreso, "+
			"getnombregruposervicio(ser.grupo_servicio) AS nombregruposervicio "+ 
			"FROM sub_cuentas sc "+  
			"INNER JOIN solicitudes_subcuenta ssc ON(ssc.sub_cuenta = sc.sub_cuenta) "+
			"INNER JOIN servicios ser ON (ser.codigo=ssc.servicio) "+ 
			"WHERE "+ 
			"sc.ingreso=? And "+ 
			"ssc.eliminado='"+ConstantesBD.acronimoNo+"' AND "+ 
			"ssc.tipo_solicitud NOT IN ("+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") AND "+ 
			"ser.tipo_servicio !='"+ConstantesBD.codigoServicioPaquetes+"' "+
			") "+ 
		") t ORDER BY t.nombreservicio";
	
	
	
	
	
	/**
	 * cadena de consulta de los formatos de consentimiento informado de los servicios.
	 */
	public static String strCadenaConsultaFormatos = "SELECT " +
			"ci.codigo AS codigo," +
			"ci.nombre_archivo AS nombrearchivo," +
			"ci.nombre_original AS nombreoriginal," +
			"ci.descripcion AS descripcion " +
			"FROM consentimientoinformado ci " +
				"INNER JOIN detalleconsentimientoinf dci ON (dci.codigoconsentimiento=ci.codigo) " +
			"WHERE dci.codigogruposservicios=? ";
	
	
	/**
	 * Consulta de los formatos del servicio
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static HashMap impresionCosentimientoinformadoXIngreso (Connection connection,HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = strCadenaConsultaFormatos;
		
		PreparedStatementDecorator ps = null;
			try
			{
				ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
				ps.setInt(1, Utilidades.convertirAEntero(parametros.get("gruposervicio").toString()));
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));	
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}finally{
				if(ps!=null){
					try{
						ps.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
					}
				}
					
			}
			
		
			mapa.put("INDICES_MAPA",indicesFormatos);
		return mapa;
	}
	
	/**
	 * Insertar un registro de consentimiento informado	
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean insertarConsentimientoInformado (Connection connection, HashMap consentimientoInformado, String cadenaInsertarStr)
	{
		PreparedStatementDecorator ps = null;
		try
		{   
			//logger.info("\n\n entro a insertarConsentimientoInformado "+consentimientoInformado);
			 ps =  new PreparedStatementDecorator(connection.prepareStatement(cadenaInsertarStr));
			// cargamos los datos en el PreparedStatementDecorator para subirlos a la BD, como el codigo es auto incremental entonces
			//no va ir en el PreparedStatement.
		
			/**
			 * INSERT INTO historiaclinica.consentimientoinformado (
			 * codigo,
			 * institucion,
			 * descripcion,
			 * nombre_archivo,
			 * nombre_original,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) VALUES('historiaclinica.seq_consentimientoinformado'),?,?,?,?,?,?,?) 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(consentimientoInformado.get("institucion").toString()));
			ps.setString(2, consentimientoInformado.get("descripcion").toString());
			ps.setString(3, consentimientoInformado.get("nombrearchivo").toString());
			ps.setString(4, consentimientoInformado.get("nombreoriginal").toString());			
			ps.setString(5, consentimientoInformado.get("usuariomodifica").toString());
			ps.setDate(6, Date.valueOf(consentimientoInformado.get("fechamodifica").toString()));
			ps.setString(7, consentimientoInformado.get("horamodifica").toString());
			

			if (ps.executeUpdate()>0)
				return true;
				
		}catch (SQLException e)
			{
				e.printStackTrace();
			}finally{
				if(ps!=null){
					try{
						ps.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
					}
				}
					
			}
		
		return false;
		
	}
	
	/**
	 * Insertar un registro de detalle consentimiento informado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	
	public static boolean insertarDetalleConsentimientoInformado (Connection connection, HashMap detalleConsentimientoInformado)
	{
		PreparedStatementDecorator ps =null;
		try
		{
			//logger.info("valor del hashmap en sql insertar detalle >> "+detalleConsentimientoInformado);
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadenaInsertardetalleStr));
			
			ps.setInt(1, Utilidades.getSiguienteValorSecuencia(connection, "seq_detconsentimientoinfo"));
			ps.setDouble(2, Utilidades.convertirADouble(detalleConsentimientoInformado.get("codigoconsent").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("institucion").toString()));
			if(detalleConsentimientoInformado.containsKey("codigogrupser")&&!UtilidadTexto.isEmpty(detalleConsentimientoInformado.get("codigogrupser")+""))
			{
				ps.setInt(4, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("codigogrupser").toString()));
			}
			else
			{
				ps.setObject(4, null);
			}
			if(detalleConsentimientoInformado.containsKey("programaodontologico")&&!UtilidadTexto.isEmpty(detalleConsentimientoInformado.get("programaodontologico")+""))
			{
				ps.setInt(5, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("programaodontologico").toString()));
			}
			else
			{
				ps.setObject(5, null);
			}
			
			ps.setString(6, detalleConsentimientoInformado.get("usuariomodifica").toString());
			ps.setDate(7, Date.valueOf(detalleConsentimientoInformado.get("fechamodifica").toString()));
			ps.setString(8, detalleConsentimientoInformado.get("horamodifica").toString());
			

			if (ps.executeUpdate()>0)
				return true;
				
		}catch (SQLException e)
			{
				e.printStackTrace();
			}finally{
				if(ps!=null){
					try{
						ps.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
					}
				}
					
			}
		
		return false;
		
	}
	
				
	/**
	 * Modifica  consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean modificarConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		PreparedStatementDecorator ps = null;
		try		
		{
			//logger.info("Entro a modificarConsentimientoInformado:::::::"+ consentimientoInformado);
			 ps =  new PreparedStatementDecorator(connection.prepareStatement(cadenaModificarStr));
			
			
			ps.setString(1, consentimientoInformado.get("descripcion").toString());
			ps.setString(2, consentimientoInformado.get("nombrearchivo").toString());
			ps.setString(3, consentimientoInformado.get("nombreoriginal").toString());
			ps.setString(4, consentimientoInformado.get("usuariomodifica").toString());
			ps.setDate(5, Date.valueOf(consentimientoInformado.get("fechamodifica").toString()));
			ps.setString(6, consentimientoInformado.get("horamodifica").toString());
			ps.setDouble(7, Utilidades.convertirADouble(consentimientoInformado.get("codigo").toString()));
			ps.setInt(8, Utilidades.convertirAEntero(consentimientoInformado.get("institucion").toString()));
			
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();						
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}				
		}
		return false;
	}
	
	
	/**
	 * Elimina consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean eliminarConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadenaEliminarStr));
			ps.setDouble(1, Utilidades.convertirADouble(consentimientoInformado.get("codigo").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(consentimientoInformado.get("institucion").toString()));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}				
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}
				
		}
		
		return false;
	}
	
	/**
	 * Elimina detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static boolean eliminardetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado)
	{
		//logger.info("Entro a eliminardetalleConsentimientoInformado:::::::::::::::::::"+detalleConsentimientoInformado);
		
		String cadena = cadenaEliminardetalleStr;
		if (detalleConsentimientoInformado.containsKey("codigopk"))
			cadena+="AND codigo_pk= "+detalleConsentimientoInformado.get("codigopk");
		
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			ps.setDouble(1, Utilidades.convertirADouble(detalleConsentimientoInformado.get("codigoconsent").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("institucion").toString()));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}				
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}
				
		}
		
		return false;
	}
	
	
	
	/**
	 * Consulta basica de consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static HashMap consultaConsentimientoInformado(Connection connection, HashMap consentimientoInformado)
	{
		//logger.info("Entro a consultaConsentimientoInformado::::::::::::::::::::::::::::::"+consentimientoInformado);
		
		HashMap mapa = new HashMap();
		String cadena = cadenaConsultaStr+" WHERE institucion=?";
		
							
		/*if(consentimientoInformado.containsKey("codigo"))
			cadena+=" AND codigo = "+consentimientoInformado.get("codigo");*/
		if((consentimientoInformado.get("tipoConsentimiento")+"").equals("SERVICIOS"))
		{
			cadena=cadena + " AND codigo IN (SELECT DISTINCT ci.codigo FROM consentimientoinformado ci" +
							" LEFT OUTER JOIN detalleconsentimientoinf dci ON (dci.codigoconsentimiento=ci.codigo)" +
							" WHERE dci.programa_odontologico IS NULL)";
		}
		else if((consentimientoInformado.get("tipoConsentimiento")+"").equals("PROGRAMAS"))
		{
			cadena=cadena + " AND codigo IN (SELECT DISTINCT ci.codigo FROM consentimientoinformado ci" +
							" LEFT OUTER JOIN detalleconsentimientoinf dci ON (dci.codigoconsentimiento=ci.codigo)" +
							" WHERE dci.codigogruposservicios IS NULL) ";
		}
		cadena = cadena + " ORDER BY codigo ";
		PreparedStatementDecorator ps = null;
		try
		{
			logger.info("consultaré concentimiento informado: "+cadena+"******************************************************");
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));			
			ps.setInt(1,Utilidades.convertirAEntero(consentimientoInformado.get("institucion").toString()));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));		 	 
			
		}
		catch(SQLException e)
		{
			e.printStackTrace(); 	
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}
				
		}		
		
		mapa.put("INDICES_MAPA",indicesMapa);
		return mapa;
	}
	
	
	
	/**
	 * Consulta basica de consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	consentimientoInformado
	 * */
	
	public static HashMap consultaConsentimientoInformadoXingreso(Connection connection, HashMap consentimientoInformado)
	{
		//logger.info("Entro a consultaConsentimientoInformado::::::::::::::::::::::::::::::"+consentimientoInformado);
		
		HashMap mapa = new HashMap();
		String cadena = strCadenaConsultaconsentimientoInf;
							
		PreparedStatementDecorator ps = null;
						
		try
		{
			logger.info("INICIO CONSULTA CONSETIMUIENTO: "+cadena.replace("?", consentimientoInformado.get("ingreso").toString()));
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));			
			ps.setInt(1,Utilidades.convertirAEntero( consentimientoInformado.get("ingreso").toString()));
			ps.setInt(2,Utilidades.convertirAEntero( consentimientoInformado.get("ingreso").toString()));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));		 	 
			
		}
		catch(SQLException e)
		{
			e.printStackTrace(); 	
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}
				
		}		
		
		mapa.put("INDICES_MAPA",indicesMapaIngreso);
		return mapa;
	}
	
	
	
	/**
	 * Consulta basica de detalle consentimiento informado por keys
	 * @param Connection connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	
	public static HashMap consultaDetalleConsentimientoInformado(Connection connection, HashMap detalleConsentimientoInformado)
	{
		//logger.info("Detalle:::::::"+detalleConsentimientoInformado);
		
		HashMap mapa = new HashMap();
		String cadena = cadenaConsultadetalleStr+" WHERE institucion=? AND codigoconsentimiento=?" ;
		if((detalleConsentimientoInformado.get("tipoConsentimiento")+"").equals("SERVICIOS"))
		{
			cadena=cadena+" and programa_odontologico is null";
			if (detalleConsentimientoInformado.containsKey("codigogrupser"))
				cadena+=" AND codigogruposservicios="+detalleConsentimientoInformado.get("codigogrupser");
		}
		else if((detalleConsentimientoInformado.get("tipoConsentimiento")+"").equals("PROGRAMAS"))
		{
			cadena=cadena+" and codigogruposservicios is null";
			if (detalleConsentimientoInformado.containsKey("programaodontologico"))
				cadena+=" AND programa_odontologico="+detalleConsentimientoInformado.get("programaodontologico");
		}
		PreparedStatementDecorator ps = null;			
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));			
			ps.setInt(1,Utilidades.convertirAEntero( detalleConsentimientoInformado.get("institucion").toString()));
			ps.setDouble(2, Utilidades.convertirADouble(detalleConsentimientoInformado.get("codigoconsent").toString()));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));		 	 
		}
		catch(SQLException e)
		{
			e.printStackTrace(); 	
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}				
		}			
		
		mapa.put("INDICES_MAPA",indicesMapaDetalle);
	//	logger.info("MAPA para mirar que sale de la consulta:::::::"+mapa);
		return mapa;
	}
	
	/**
	 * Modifica el detalle consentimiento informado registrado
	 * @param Connection   connection
	 * @param HashMap 	detalleConsentimientoInformado
	 * */
	public static boolean modificarDetalleConsentimientoInf(Connection connection, HashMap detalleConsentimientoInformado)
	{
		PreparedStatementDecorator ps = null;
		try		
		{
			
			//logger.info("Entro a modificarDetalleConsentimientoInformado:::::::"+ detalleConsentimientoInformado);
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadenaModificarDetalleStr));
			
			if(detalleConsentimientoInformado.containsKey("codigogrupser")&&!UtilidadTexto.isEmpty(detalleConsentimientoInformado.get("codigogrupser")+""))
			{
				ps.setInt(1, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("codigogrupser").toString()));
			}
			else
			{
				ps.setObject(1, null);
			}
			if(detalleConsentimientoInformado.containsKey("programaodontologico")&&!UtilidadTexto.isEmpty(detalleConsentimientoInformado.get("programaodontologico")+""))
			{
				ps.setInt(2, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("programaodontologico").toString()));
			}
			else
			{
				ps.setObject(2, null);
			}
			
			ps.setString(3, detalleConsentimientoInformado.get("usuariomodifica").toString());
			ps.setDate(4, Date.valueOf(detalleConsentimientoInformado.get("fechamodifica").toString()));
			ps.setString(5, detalleConsentimientoInformado.get("horamodifica").toString());
			/*
			ps.setDouble(5, Utilidades.convertirADouble(detalleConsentimientoInformado.get("codigoconsent").toString()));
			ps.setInt(6, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("institucion").toString()));
			ps.setInt(7, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("codigogrupserold").toString()));
			*/
			ps.setInt(6, Utilidades.convertirAEntero(detalleConsentimientoInformado.get("codigopk").toString()));
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();						
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}				
		}	
		
		return false;
	}
	
	
	/**
	 * Consulta los consentimientos informados de un grupo de servicios a partir del servicio asociado 
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public static HashMap buscarConsentimientoInfServicio(Connection connection, HashMap parametros)
	{
		
		HashMap mapa = new HashMap();	
						
		PreparedStatementDecorator ps = null;
		try
		{
			if(parametros.containsKey("servicio")&&!UtilidadTexto.isEmpty(parametros.get("servicio")+""))
			{
				ps=new PreparedStatementDecorator(connection.prepareStatement(strConsultaConsentimientoInf));
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("servicio").toString()));
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));		 	
			}
			else if(parametros.containsKey("codigoPrograma")&&!UtilidadTexto.isEmpty(parametros.get("codigoPrograma")+""))
			{
				ps=new PreparedStatementDecorator(connection.prepareStatement(strConsultaConsentimientoGrupoPrograma));
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPrograma").toString()));
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));		 	
			}
			
			 
		}
		catch(SQLException e)
		{
			e.printStackTrace(); 	
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}				
		}		
		
		return mapa;
	}
	
	
	/**
	 * Inserta el registro que indica que sea imprimido un consentimiento informado
	 * @param Connection connection 
	 * @param HashMap parametros
	 * */
	public static boolean insertarHistorialConsentimientoInf(Connection connection, HashMap parametros)
	{
		PreparedStatementDecorator ps = null;
		try
		{
			Utilidades.imprimirMapa(parametros);
			
			ps =  new PreparedStatementDecorator(connection.prepareStatement(strInsertarHistorialConsentimiento));			
			
			/**
			 * NSERT INTO " +
			"historialconsentimientoinf (ingreso," +
			"							 servicio," +
			"							 codigoconsentimiento," +
			"							 institucion," +
			"							 descripcion," +
			"							 nombre_archivo," +
			"							 usuario_imprime," +
			"							 fecha_imprime," +
			"							 hora_imprime) " +
			"VALUES(?,?,?,?,?,?,?,?,?) 
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			if(parametros.containsKey("servicio")&&!UtilidadTexto.isEmpty(parametros.get("servicio")+""))
			{
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("servicio").toString()));
			}
			else
			{
				ps.setObject(2, null);
			}
			ps.setDouble(3,Utilidades.convertirADouble(parametros.get("codigoconsentimiento").toString()));
			ps.setInt(4,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setString(5,parametros.get("descripcion").toString());
			ps.setString(6,parametros.get("nombrearchivo").toString());
			ps.setString(7,parametros.get("usuarioimprime").toString());
			ps.setDate(8,Date.valueOf(parametros.get("fechaimprime").toString()));
			ps.setString(9,parametros.get("horaimprime").toString());
			
			if(parametros.containsKey("codigoPrograma")&&!UtilidadTexto.isEmpty(parametros.get("codigoPrograma")+""))
			{
				ps.setInt(10,Utilidades.convertirAEntero(parametros.get("planTratamiento").toString()));
				ps.setInt(11,Utilidades.convertirAEntero(parametros.get("codigoPrograma").toString()));
			}
			else
			{
				ps.setObject(10, null);
				ps.setObject(11, null);
			}
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}				
		}		
		
		
		return false;
	}

	/**
	 * 
	 * @param codigoPrograma
	 * @return
	 */
	public static boolean programaTieneConsentimientosInformados(int codigoPrograma) 
	{
		String cadena="SELECT count(1) from detalleconsentimientoinf where programa_odontologico ="+codigoPrograma;
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				if(rs.getInt(1)>0)
					resultado=true;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}				
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param codigophp
	 * @return
	 */
	public static boolean programaHallazgoPiezaTienConsentimientoInfo(int codigophp) 
	{
		String cadena="SELECT count(1) from odontologia.consentimiento_info_odonto where programa_hallazgo_pieza="+codigophp;
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps = null;
		ResultSet rs= null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			rs=ps.executeQuery();
			if(rs.next())
			{
				if(rs.getInt(1)>0)
					resultado=true;
			}
	
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCategoriasTriageDao "+sqlException.toString() );
				}
			}	
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param codigosPHP
	 * @param todos
	 * @return
	 */
	public static ArrayList<DtoConsentimientoInformadoOdonto> consultarInfoConsentimientoOdonto(String codigosPHP, String planTratamiento, boolean todos) 
	{
		ArrayList<DtoConsentimientoInformadoOdonto> array=new ArrayList<DtoConsentimientoInformadoOdonto>();
		String cadena="SELECT DISTINCT " +
								" coalesce(cio.codigo_pk,-1) as codigo," +
								" php.plan_tratamiento as plantratamiento," +
								" pt.ingreso as ingreso," +
								//" php.codigo_pk as codigophp," +
								" coalesce(cio.recibio_consentimiento,'') as recibioconsentimiento," +
								" coalesce(cio.motivo_no_consentimiento,-1) as motivonoconsentimiento," +
								" mncs.descripcion as descmotivonoconsentimiento," +
								" php.programa as codigoprograma," +
								" pro.nombre as nombreprograma," +
								" coalesce(cio.usuario_modifica,'') as usuariomodifica," +
								" coalesce(cio.fecha_modifica,'') as fechamodifica," +
								" coalesce(cio.hora_modifica,'') as horamodifica " +
						" from odontologia.programas_hallazgo_pieza php " +
						" inner join plan_tratamiento pt on(pt.codigo_pk=php.plan_tratamiento) " +
						" inner join odontologia.programas pro on(php.programa=pro.codigo) " +
						" left outer join odontologia.consentimiento_info_odonto cio on(cio.programa_hallazgo_pieza=php.codigo_pk) " +
						" left outer join odontologia.motivos_cambios_servicios mncs on(mncs.codigo_pk=cio.motivo_no_consentimiento) " +
						" where php.programa in("+codigosPHP+") and php.plan_tratamiento="+planTratamiento;
		if(!todos)
			cadena=cadena+" and (cio.codigo_pk is null or cio.recibio_consentimiento='"+ConstantesBD.acronimoNo+"')";
		
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps  = null;
		PreparedStatementDecorator psInterna  = null;
		ResultSet rs = null;
		ResultSet rsInterna = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			logger.info(cadena);
			rs=ps.executeQuery();
			while(rs.next())
			{
				DtoConsentimientoInformadoOdonto dto=new DtoConsentimientoInformadoOdonto();
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setPlanTratamiento(rs.getInt("plantratamiento"));
				dto.setIngreso(rs.getInt("ingreso"));
				//dto.setCodigoProgramaHallazgoPieza(rs.getInt("codigophp"));
				dto.setRecibioConsentimiento(rs.getString("recibioconsentimiento"));
				dto.setMotivoNoConsentimiento(rs.getInt("motivonoconsentimiento"));
				dto.setDescripcionMotivoNoConsentimiento(rs.getString("descmotivonoconsentimiento"));
				dto.setCodigoPrograma(rs.getInt("codigoprograma"));
				dto.setDescripcionPrograma(rs.getString("nombreprograma"));
				dto.setUsuarioModifica(rs.getString("usuariomodifica"));
				dto.setFechaModifica(rs.getString("fechamodifica"));
				dto.setHoraModifica(rs.getString("horamodifica"));
				if(dto.getCodigoPk()>0)
				{
					try{
						dto.setExisteBd(true);
						String subConsulta="SELECT to_char(fecha_imprime,'dd/mm/yyyy') as fecha,hora_imprime as hora,usuario_imprime as usuario from historialconsentimientoinf  where plan_tratamiento="+dto.getPlanTratamiento()+" and codigo_programa="+dto.getCodigoPrograma()+" order by fecha_imprime desc,hora_imprime desc";
						psInterna =  new PreparedStatementDecorator(con.prepareStatement(subConsulta));
						rsInterna=psInterna.executeQuery();
						if(rsInterna.next())
						{
							dto.setUsuarioImpresionConsentimiento(rsInterna.getString("usuario"));
							dto.setFechaImpresionConsentimiento(rsInterna.getString("fecha"));
							dto.setHoraImpresionConsentimiento(rsInterna.getString("hora"));
						}
						else
						{
							dto.setUsuarioImpresionConsentimiento("");
							dto.setFechaImpresionConsentimiento("");
							dto.setHoraImpresionConsentimiento("");
						}
						
					}catch(SQLException e)
					{
						e.printStackTrace();
					}finally{
						if(rsInterna!=null){
							try{
								rsInterna.close();					
							}catch(SQLException sqlException){
								logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
							}
						}	
						if(psInterna!=null){
							try{
								psInterna.close();					
							}catch(SQLException sqlException){
								logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
							}
						}	
					}
					
				
				}
				else
				{
					dto.setUsuarioImpresionConsentimiento("");
					dto.setFechaImpresionConsentimiento("");
					dto.setHoraImpresionConsentimiento("");
					dto.setExisteBd(false);
				}
				array.add(dto);
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally {
			
				if(ps!=null){
					try{
						ps.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
					}
				}	
				
				if(rs!=null){
					try{
						rs.close();					
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
					}
				}	
		}
		UtilidadBD.closeConnection(con);
		
		return array;
	}

	
	
	/**
	 * 
	 * @param con
	 * @param codigoCita 
	 * @param consentimientoOdonto
	 * @return
	 */
	public static boolean guardarConsentimientoOdontologia(Connection con,ArrayList<DtoConsentimientoInformadoOdonto> consentimiento, String codigoCita) 
	{
		String cadenaInsertar="insert into odontologia.consentimiento_info_odonto(" +
								" codigo_pk," +
								" ingreso," +
								" plan_tratamiento," +
								" programa_hallazgo_pieza," +
								" recibio_consentimiento," +
								" motivo_no_consentimiento," +
								" usuario_modifica," +
								" fecha_modifica," +
								" hora_modifica" +
						") values(?,?,?,?,?,?,?,?,?) ";
		
		String cadenaUpdate="update odontologia.consentimiento_info_odonto set " +
								" recibio_consentimiento=?," +
								" motivo_no_consentimiento=?," +
								" usuario_modifica=?," +
								" fecha_modifica=?," +
								" hora_modifica=?" +
							" where codigo_pk=?";
		boolean resultado=false;
		PreparedStatementDecorator ps = null;
		try
		{
			for(DtoConsentimientoInformadoOdonto dto:consentimiento)
			{
				logger.info("************ dat - ");
				UtilidadLog.obtenerStringHerencia(dto, true);
				if(dto.getCodigoPk()<=0)
				{
					for(Integer codPHP:dto.getCodigosPHPRelacionados())
					{
						logger.info("** IF - codPHP: " + codPHP);
						ps=new PreparedStatementDecorator(con,cadenaInsertar);
						ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_consinfoodo"));
						ps.setInt(2, dto.getIngreso());
						ps.setInt(3, dto.getPlanTratamiento());
						ps.setInt(4, codPHP.intValue());
						ps.setString(5, dto.getRecibioConsentimiento());
						if(dto.getMotivoNoConsentimiento()>0)
							ps.setInt(6, dto.getMotivoNoConsentimiento());
						else
							ps.setObject(6, null);
						ps.setString(7, dto.getUsuarioModifica());
						ps.setString(8, dto.getFechaModifica());
						ps.setString(9, dto.getHoraModifica());
						if(ps.executeUpdate()<=0)
							return false;
						resultado=true;
					}
				}
				else
				{
					logger.info("** ELSE" );
					 ps=new PreparedStatementDecorator(con,cadenaUpdate);
					ps.setString(1,dto.getRecibioConsentimiento());
					if(dto.getMotivoNoConsentimiento()>0)
						ps.setInt(2, dto.getMotivoNoConsentimiento());
					else
						ps.setObject(2, null);
					ps.setString(3, dto.getUsuarioModifica());
					ps.setString(4, dto.getFechaModifica());
					ps.setString(5, dto.getHoraModifica());
					ps.setInt(6, dto.getCodigoPk());
					if(ps.executeUpdate()<=0)
						return false;
					resultado=true;
				}
			}
		}
		catch(Exception e)
		{
			logger.error("error",e);
			resultado=false;
		}finally {
			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}	
			
		
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param planTratamiento
	 * @param codigoPrograma
	 * @param codigoCita 
	 * @return
	 */
	public static ArrayList<Integer> obtenerProgramasHallazgoPiezasParaConsentimiento(Connection con, int planTratamiento, int codigoPrograma, String codigoCita, boolean validaPresupuestoContratado) 
	{
		ArrayList<Integer> resultado=new ArrayList<Integer>();
		String cadena="";
		if(validaPresupuestoContratado)
		{
			cadena="SELECT DISTINCT php.codigo_pk " +
						" from odontologia.programas_hallazgo_pieza php " +
						" inner JOIN odontologia.superficies_x_programa sxp ON (php.codigo_pk=sxp.prog_hallazgo_pieza) " +
						" inner JOIN odontologia.det_plan_tratamiento dpt ON (sxp.det_plan_trata=dpt.codigo_pk) " +
						" inner join programas_servicios_plan_t pspt on (dpt.codigo_pk=pspt.det_plan_tratamiento) " +
						" left outer join odontologia.consentimiento_info_odonto cio on(cio.programa_hallazgo_pieza=php.codigo_pk)  " +
						" where " +
								" php.plan_tratamiento = ? and " +
								" php.programa=? and (cio.codigo_pk is null or cio.recibio_consentimiento='"+ConstantesBD.acronimoNo+"') and " +
								" pspt.estado_programa in('COT','ENP')";
		}
		else
		{
			cadena="SELECT DISTINCT php.codigo_pk " +
				" from odontologia.programas_hallazgo_pieza php " +
				" inner join odontologia.servicios_cita_odontologica sco on (sco.programa_hallazgo_pieza=php.codigo_pk and sco.cita_odontologica="+codigoCita+")" +
				" left outer join odontologia.consentimiento_info_odonto cio on(cio.programa_hallazgo_pieza=php.codigo_pk)  " +
				" where " +
						" php.plan_tratamiento = ? and " +
						" php.programa=? and (cio.codigo_pk is null or cio.recibio_consentimiento='"+ConstantesBD.acronimoNo+"') ";
		}
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, planTratamiento);
			ps.setInt(2, codigoPrograma);
			logger.info("**************************************");
			logger.info("sQL: " + cadena);
			logger.info("Plan Tratamiento: " + planTratamiento + "  codigoPrograma: " + codigoPrograma);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				resultado.add(rs.getInt(1));
			}
		}
		catch(Exception e)
		{
			logger.error("error",e);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsentimientoInformadoDao "+sqlException.toString() );
				}
			}	
			
		
		}
		return resultado;
	}
}