package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;



/**
 * Clase 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */
public class SqlBaseConsultarActivarCamasReservadasDao
{
	
	/**
	 * Maneja los log's de la clase
	 */
	static Logger logger = Logger.getLogger(SqlBaseCensoCamasDao.class);
	
	
	
	/*----------------------------------------------------------------------------------------
	 *                 ATRIBUTOS DE CONSULTAR Y ACTIVAR CAMAS RESERVADAS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Cadena que Consulta  las Camas Reservadas
	 */
	private static final String strConsultaCamasReservadas= "SELECT " +
			"rc.centro_atencion AS centroatencion," +
			"getnomcentroatencion(rc.centro_atencion) AS nombrecentroatencion," +
			"rc.codigo_paciente AS codigopacinte," +
			"rc.codigo_cama AS codigocama," +
			"rc.fecha_ocupacion AS fechareservada," +
			"rc.estado AS estadoreserva," +
			"rc.codigo AS codigoreserva," +
			"rc.fecha_modifica AS fechacreacion," +
			"getnombrepersona(rc.codigo_paciente) AS nombrepac," +
			"p.numero_identificacion AS identificacionpac," +
			"p.tipo_identificacion AS tipoidentificacionpac," +
			"c.numero_cama AS nombrecama," +
			"getnomhabitacioncama(rc.codigo_cama) AS nombrehabitacion," +
			"getnomcentrocosto(c.centro_costo) AS nombrecentrocosto," +
			"getnombrepisocama(c.codigo) AS nombrepiso," +
			"getcodigopisoxcama(c.codigo) AS codigopiso," +
			"getintcodigopisoxcama(c.codigo) AS codigo,"+
			"h.nombre AS nombretipohabitacion," +
			"h.codigo_habitac AS codigohabitacion " +
			"FROM reservar_cama rc " +
			"INNER JOIN personas p ON (p.codigo=rc.codigo_paciente) " +
			"INNER JOIN camas1 c ON (c.codigo=rc.codigo_cama) " +
			"INNER JOIN habitaciones h ON (h.codigo=c.habitacion)";
	
	
		
	/**
	 * cadena que consulta la informacion de una cama
	 */
	private static final String strConsultaCama="SELECT " +
			"c.codigo AS codigocama," +
			"c.habitacion AS codigohabitacion," +
			"c.numero_cama AS numerocama," +
			"c.tipo_usuario_cama AS tipousuario," +
			"getnombretipousuario(c.tipo_usuario_cama) AS nombretipousuario," +
			"c.centro_costo AS centrocosto, " +
			"coalesce(getnomhabitacioncama(c.codigo),'') AS habitacion," +
			"cc.nombre AS nombrecentrocosto, "+
			"coalesce(getnombrepisocama(c.codigo),'') as piso," +
			"coalesce(getnomtipohabitacioncama(c.codigo),'') as tipohabitacion," +
			"getnomcentroatencion(cc.centro_atencion) AS nombrecentroatencioncama," +
			"getintcodigopisoxcama(c.codigo)AS codigopisoint, " +
			"getCodigoPisoXCama(c.codigo) AS codigopiso " +
			"FROM camas1 c " +
			"INNER JOIN centros_costo cc ON (cc.codigo=c.centro_costo) ";
	
	
	/**
	 * Cadena que consulta la informacion de una reserva
	 */
	private static final String strConsultaReserva = "SELECT " +
			"getnomcentroatencion(rc.centro_atencion) AS nombrecentroatencionreserva," +
			"rc.centro_atencion AS codigocentroatencion," +
			"rc.fecha_ocupacion AS fechaocupacion," +
			"rc.fecha_modifica AS fechareserva," +
			"rc.usuario_modifica AS usuario," +
			"rc.estado AS estadoreserva," +
			"rc.codigo_cama AS codigocama," +
			"rc.codigo_paciente AS codigopaciente," +
			"getnombrepersona(rc.codigo_paciente) AS nombrepaciente, " +
			"rc.codigo AS codigoreserva " +
			"FROM reservar_cama rc ";
			
	
	/**
	 * Cadena de Actualizacion Reserva
	 */
	private static final String strActualizacionReserva ="UPDATE  reservar_cama SET estado='CAN', motivo_cancelacion=? where institucion=? AND codigo=? ";
	
	/**
	 * Cadena de Actualizacion Cama
	 */
	private static final String strActualizacionCama ="UPDATE camas1 SET estado=0 where institucion=? AND codigo=?";
	
	/**
	 * Cadena para Gusrdar el Log de la activacion de camas
	 */
	private static final String strIngresarLog = "INSERT INTO logs_activar_camas_reservadas (consecutivo, codigo_paciente, codigo_centro_atencion, " +
			"codigo_centro_costo, codigo_piso, codigo_habitacion, codigo_cama, codigo_reserva_cancelada, fecha_cancelacion, hora_cancelacion, " +
			"usuario_cancela) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/*----------------------------------------------------------------------------------------
	 *                FIN ATRIBUTOS DE CONSULTAR Y ACTIVAR CAMAS RESERVADAS
	 -----------------------------------------------------------------------------------------*/
	
	
	/*----------------------------------------------------------------------------------------
	 *             DEFINICION DE INDICES DE CONSULTAR Y ACTIVAR CAMAS RESERVADAS
	 -----------------------------------------------------------------------------------------*/
	private static String [] indicesConsultarActivarCamasReservadas= {"centroatencion_","nombrecentroatencion_","codigopacinte_","codigocama_","fechareservada_","estadoreserva_",
																	  "codigoreserva_","fechacreacion_","nombrepac_","identificacionpac_","tipoidentificacionpac_","nombrecama_",
																	  "nombrehabitacion_","nombrecentrocosto_","nombrepiso_","codigopiso_","codigo_","nombretipohabitacion_","codigohabitacion_"};
	
	/*----------------------------------------------------------------------------------------
	 *            FIN  DEFINICION DE INDICES DE CONSULTAR Y ACTIVAR CAMAS RESERVADAS
	 -----------------------------------------------------------------------------------------*/
	
	
	
	/*----------------------------------------------------------------------------------------
	 *                              METODOS DE CONSULTAR Y ACTIVAR CAMAS RESERVADAS
	 -----------------------------------------------------------------------------------------*/
	
	
	
	/**
	 * Metodo que se encarga de ingresar la informacion a la BD
	 * El Hasmap Parametros contiene los siguientes Key's:
	 * --------------------------------------------------------
	 * 					KEY'S DE PARAMETROS
	 * --------------------------------------------------------
	 * --codigopaciente --> Requerido
	 * --codigocentroatencion --> Requerido
	 * --codigocentrocosto --> Requerido
	 * --codigopiso --> Requerido
	 * --codigohabitacion --> Requerido
	 * --codigocama --> Requerido
	 * --codigoreservacancelada --> Requerido
	 * --codigousuariocancela --> Requerido
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean logActivacionCamas(Connection connection, HashMap parametros)
	{
		
		logger.info("\n entro a logActivacionCamas --> "+parametros);
		String cadena=strIngresarLog;
		
		  try
		  {
			  PreparedStatementDecorator ps1=  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			  
			  /**
			   * INSERT INTO logs_activar_camas_reservadas (consecutivo, codigo_paciente, codigo_centro_atencion, " +
			"codigo_centro_costo, codigo_piso, codigo_habitacion, codigo_cama, codigo_reserva_cancelada, fecha_cancelacion, hora_cancelacion, " +
			"usuario_cancela) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			   */
			  
			  
			  ps1.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_logs_act_camas_reser"));
			  ps1.setInt(2,Utilidades.convertirAEntero(parametros.get("codigopaciente").toString()));
			  ps1.setInt(3,Utilidades.convertirAEntero(parametros.get("codigocentroatencion").toString()));
			  ps1.setInt(4,Utilidades.convertirAEntero(parametros.get("codigocentrocosto").toString()));
			  ps1.setInt(5,Utilidades.convertirAEntero(parametros.get("codigopiso").toString()));
			  ps1.setInt(6,Utilidades.convertirAEntero(parametros.get("codigohabitacion").toString()));
			  ps1.setInt(7,Utilidades.convertirAEntero(parametros.get("codigocama").toString()));
			  ps1.setDouble(8,Utilidades.convertirADouble(parametros.get("codigoreservacancelada").toString()));
			  ps1.setDate(9,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			  ps1.setString(10,UtilidadFecha.getHoraActual());
			  ps1.setString(11,parametros.get("codigousuariocancela").toString());
			   
			  
			  if(ps1.executeUpdate() >0)
					return true;
		  }
			catch(SQLException e)
			{
				logger.info("Error en al hacer el log de Activacion de Camas: "+e);
			}
			return false;
	}
	
	
	
	
	
	/**
	 * Metodo que se encarga de canselar una Reserva.
	 * El HashMap parametros contiene los siguientes key's:
	 * ---------------------------------------------------
	 * 					KEYS DE PARAMETROS
	 * ---------------------------------------------------
	 * --institucion --> Requerido
	 * --motivocancelacion --> Requerido
	 * --codigoreserva --> Requerido
	 * --codigocama --> Requerido
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static boolean cancelarReserva (Connection connection,HashMap parametros)
	{
		String consulta=strActualizacionReserva;
		try 
		{
			PreparedStatementDecorator psr =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psr.setString(1, parametros.get("motivocancelacion").toString());
			psr.setInt(2, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			psr.setInt(3, Utilidades.convertirAEntero(parametros.get("codigoreserva").toString()));
			
			
			
			if(psr.executeUpdate() >0)
			{
				consulta=strActualizacionCama;
				PreparedStatementDecorator psc =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psc.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
				psc.setInt(2, Utilidades.convertirAEntero(parametros.get("codigocama").toString()));
				
				if(psc.executeUpdate() >0)
					return true;
					
			}
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		return false;
	}
	
	
	
	
	/**
	 * Metodo encargado de buscar los datos de una reserva.
	 * El HashMap Parametros lleva los key's.
	 * ----------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * ----------------------------------------------
	 * --institucion --> Requerido
	 * --reservaCamaId --> Opcional
	 * --codigoCama  -->Opcional
	 * ----------------------------------------------
	 * 			KEY'S DEL MAPA QUE DEVUELVE
	 * ----------------------------------------------
	 * nombrecentroatencionreserva, fechaocupacion,fechareserva,
	 * usuario, estadoreserva, codigocama, codigopaciente, codigoreserva.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static HashMap consultaDatosReserva (Connection connection,HashMap parametros)
	{
		HashMap mapa = new HashMap ();
		
		logger.info("el vlaor de parametros en consultaDatosReserva "+parametros);
		String consulta = strConsultaReserva,where=" WHERE rc.institucion=? AND rc.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
		if (parametros.containsKey("reservaCamaId"))
			if (!parametros.get("reservaCamaId").equals(""))
				where+=" AND rc.codigo="+Utilidades.convertirAEntero(parametros.get("reservaCamaId")+"");
		if (parametros.containsKey("codigoCama"))	
			if (!parametros.get("codigoCama").equals(""))
				where+=" AND rc.codigo_cama="+Utilidades.convertirAEntero(parametros.get("codigoCama")+"");
				
		consulta+=where;
		logger.info("consulta datos de la reserva ==> "+consulta);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		logger.info("el vlaor del mapa en consultaDatosReserva "+mapa);	
		
		
		return mapa;
	}
	
	
	
	
	/**
	 * Metodo encargado de consultar los datos de una cama
	 * El HashMap parametros puede llevar los siguiente Key's:
	 * -----------------------------------------------------
	 * 					KEY'S DE PARAMETROS
	 * -----------------------------------------------------
	 * --institucion --> Requerido
	 * --codigocama --> Opcional 
	 * ---------------------------------------------------
	 * 			KEY'S DEL MAPA QUE DEVUELVE
	 * ---------------------------------------------------
	 * codigocama, codigohabitacion, numerocama, tipousuario,
	 * centrocosto, habitacion, nombrecentrocosto, piso,
	 * tipohabitacion, nombrecentroatencioncama,nombretipousuario.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static HashMap consultaDatoscama (Connection connection,HashMap parametros)
	{
		HashMap mapa = new HashMap ();
		String consulta = strConsultaCama,where=" WHERE c.institucion=? ";
		logger.info("el vlaor de parametros en consultaDatoscama "+parametros);
		if (parametros.containsKey("codigocama"))
			if (!parametros.get("codigocama").equals(""))
				where+=" AND c.codigo="+Utilidades.convertirAEntero(parametros.get("codigocama").toString());
		
		
		
		consulta+=where;
		logger.info("consulta datos de la cama ==> "+consulta);
		
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		logger.info("el vlaor del mapa en consultaDatoscama "+mapa);
		return mapa;
	}
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de consultar las camas reservadas por diferentes 
	 * criterios de busuqeda; el HashMap parametros puede estar compuesto
	 * por los siguientes Key's:
	 * ---------------------------------------------------------------
	 * 			KEY'S QUE PUEDE CONTENER EL MAPA PARAMETROS
	 * --------------------------------------------------------------
	 * institucion --> Requerido
	 * centroatencion --> Opcional
	 * codigocama --> Opcional
	 * estadoreserva --> Opcional
	 * centrocosto --> Opcional
	 * piso --> Opcional
	 * tipohabitacion --> Opcional
	 * tipoidentificacion --> Opcional
	 * numeroidentificacion --> Opcional
	 * primernombre --> Opcional
	 * segundonombre --> Opcional
	 * primerapellido --> Opcional
	 * segundoapellido --> Opcional
	 * Y al momento de devolver la consulta, el metodo devuele el mapa 
	 * con los siguientes Key's:
	 * ----------------------------------------------------------------
	 * 				KEY'S QUE DEVUELVE LA CONSULTA
	 * ---------------------------------------------------------------
	 * centroatencion, codigopacinte, codigocama, fechareservada, 
	 * estadoreserva, codigoreserva, fechacreacion, nombrepac, 
	 * identificacionpac, tipoidentificacionpac, nombrecama, 
	 * nombrehabitacion, nombrecentrocosto, nombrepiso, nombretipohabitacion.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static HashMap consultarCamasReservadas (Connection connection,HashMap parametros)
	{
		logger.info("\n entre a consultarCamasReservadas parametros --> "+parametros);
		
		HashMap mapa = new HashMap();
		String consulta = strConsultaCamasReservadas,where=" WHERE rc.institucion=? ";
		/*----------------------------------------------------
		 * 				FILTROS POR RESERVA DE CAMAS
		 -------------------------------------------------------*/
		//codigo del centro de atencion
		if (parametros.containsKey("centroatencion"))
			if (!parametros.get("centroatencion").equals(""))
				where+=" AND rc.centro_atencion="+Utilidades.convertirAEntero(parametros.get("centroatencion")+"");
		
		//codigo de la cama
		if (parametros.containsKey("codigoCama"))
			if (!parametros.get("codigoCama").equals(""))
				where+=" AND rc.codigo_cama="+Utilidades.convertirAEntero(parametros.get("codigoCama")+"");
		
		//estado de la reserva
		if (parametros.containsKey("estadoreserva"))
			if (!parametros.get("estadoreserva").equals(""))
				where+=" AND rc.estado='"+parametros.get("estadoreserva")+"'";
		
		//cosigo del centro de costo
		if (parametros.containsKey("centrocosto"))
			if (!parametros.get("centrocosto").equals(""))
				where+=" AND c.centro_costo="+Utilidades.convertirAEntero(parametros.get("centrocosto")+"");
		// Codigo del piso
		if (parametros.containsKey("codigopiso"))
			if (!parametros.get("codigopiso").equals(""))
				where+=" AND h.piso="+Utilidades.convertirAEntero(parametros.get("codigopiso")+"");
		
		if (parametros.containsKey("tipohabitacion"))
			if (!parametros.get("tipohabitacion").equals(""))
				where+=" AND h.tipo_habitacion='"+parametros.get("tipohabitacion")+"'";
		
		
		/*------------------------------------------------------
		 * 			FILTROS POR PERSONA
		 ------------------------------------------------------*/
		
		if (parametros.containsKey("tipoidentificacion"))
			if (!parametros.get("tipoidentificacion").equals(""))
				where+=" AND p.tipo_identificacion='"+parametros.get("tipoidentificacion").toString()+"'";
		
		if (parametros.containsKey("numeroidentificacion"))
			if (!parametros.get("numeroidentificacion").equals(""))
				where+=" AND p.numero_identificacion='"+parametros.get("numeroidentificacion").toString()+"'";
		
		if (parametros.containsKey("primernombre") && parametros.containsKey("segundonombre"))
			if (!parametros.get("primernombre").equals("") && !parametros.get("segundonombre").equals(""))
				where+=" AND ( UPPER(p.primer_nombre) like UPPER('%"+parametros.get("primernombre").toString()+"%') OR UPPER(p.primer_nombre) like UPPER('%"+parametros.get("segundonombre").toString()+"%')" +
				   		"OR UPPER(p.segundo_nombre) like UPPER('%"+parametros.get("primernombre").toString()+"%') OR UPPER(p.segundo_nombre) like UPPER('%"+parametros.get("segundonombre").toString()+"%') )";
		else
			if (parametros.containsKey("primernombre"))
				if (!parametros.get("primernombre").equals(""))
					where+=" AND UPPER(p.primer_nombre) like UPPER('%"+parametros.get("primernombre").toString()+"%')";
			
		if (parametros.containsKey("primerapellido") && parametros.containsKey("segundoapellido"))
			if (!parametros.get("primerapellido").equals("") && !parametros.get("segundoapellido").equals(""))
				where+=" AND UPPER(p.primer_apellido) like UPPER('%"+parametros.get("primerapellido").toString()+"%') OR UPPER(p.primer_apellido) like UPPER('%" +parametros.get("segundoapellido").toString()+"%')"+
						"OR UPPER(p.segundo_apellido) like UPPER('%"+parametros.get("primerapellido").toString()+"%') OR UPPER(p.primer_apellido) like UPPER('%" +parametros.get("segundoapellido").toString()+"')";
			
		if (parametros.containsKey("primerapellido"))
			if (!parametros.get("primerapellido").equals(""))
				where+=" AND UPPER(p.primer_apellido) like UPPER('%"+parametros.get("primerapellido").toString()+"%')";
		
			
		
											
		
		consulta+=where;
		consulta+=" ORDER BY rc.centro_atencion";
		logger.info("consulta camas reservadas ==> "+consulta);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA", indicesConsultarActivarCamasReservadas);
		return mapa;
	}
	
	
	/*----------------------------------------------------------------------------------------
	 *                             FIN METODOS DE CONSULTAR Y ACTIVAR CAMAS RESERVADAS
	 -----------------------------------------------------------------------------------------*/
	

	
}