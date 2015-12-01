package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.Camas1;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a 
 * <code>AdmisionUrgenciasDao</code>, proporcionando los servicios
 * de acceso a una base de datos requeridos
 * por la clase <code>AdmisionUrgencias</code>
 *
 *	@version 1.0, Mar 24, 2004
 */
public class SqlBaseAdmisionUrgenciasDao {
	
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAdmisionUrgenciasDao.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para liberar
	 * la cama ocupada por una Admision de Urgencias tras un egreso usando
	 * una BD Genérica .
	 */
	private static final String pasarCamaADesinfeccionStr="UPDATE camas1 set estado =? where codigo =(select cama_observacion from admisiones_urgencias where codigo=? and anio=?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para saber si una admisión de urgencias tiene o no cama de observación.
	 */
	private static final String tieneCamaAdmisionUrgenciasStr="select count(1) as numResultados from admisiones_urgencias where codigo=? and anio=? and cama_observacion is not null";
	
	/**
	 * Sección selectpara la consulta de admision Urgencias
	 */
	// "au.numero_autorizacion AS numero_autorizacion, " +
	private static final String consultaAdmisionUrgenciasSELECT_Str = "SELECT " +
			"CASE " +
			"WHEN au.fecha_egreso_observacion is not null " +
			"THEN to_char(au.fecha_egreso_observacion,'"+ConstantesBD.formatoFechaBD+"') " +
			"ELSE NULL " +
			"END AS fecha_egreso_observacion, " +
	"au.hora_egreso_observacion AS hora_egreso_observacion, " +
	"to_char(au.fecha_ingreso_observacion,'"+ConstantesBD.formatoFechaBD+"') AS fecha_ingreso_observacion, " +
	"au.hora_ingreso_observacion, " +
	"au.cama_observacion AS cama_observacion, " +
	"au.codigo_medico, " +
	"ce.nombre AS nombre_causa_externa, " +
	"oau.nombre AS nom_origen_admision_urgencias, " +
	"au.origen_admision_urgencias AS origen_admision_urgencias, " +
	"au.codigo AS codigo, " +
	"au.anio AS anio, " +
	"to_char(au.fecha_admision, '"+ConstantesBD.formatoFechaBD+"') AS fecha_admision, " +
	"au.hora_admision AS hora_admision, " +	
	"au.causa_externa AS causa_externa, " +
	"au.login_usuario AS login_usuario, " +
	"au.cuenta AS cuenta " +
	"FROM " +
	"admisiones_urgencias au, " +
	"causas_externas ce, " +
	"ori_admision_hospi oau ";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar una
	 * admision de urgencias
	 */
	private static final String consultarAdmisionUrgenciasStr = consultaAdmisionUrgenciasSELECT_Str +
																							"WHERE  au.codigo = ? and au.anio = ? and ce.codigo=au.causa_externa and oau.codigo=au.origen_admision_urgencias";
	
	private static final String consultarAdmisionUrgencias2Str = consultaAdmisionUrgenciasSELECT_Str + 
		"WHERE  au.cuenta = ? and ce.codigo=au.causa_externa and oau.codigo=au.origen_admision_urgencias";
	

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar una
	 * admision de urgencias
	 */
	// "numero_autorizacion = ?, " +
	private static final String modificarAdmisionUrgenciasStr = "UPDATE admisiones_urgencias SET " +
			"origen_admision_urgencias = ?, " +
			"fecha_admision = ?, " +
			"hora_admision = ?, " +
			"codigo_medico = ?, " +
			"causa_externa = ?, " +
			"fecha_ingreso_observacion = ?, " +
			"hora_ingreso_observacion = ?, " +
			"cama_observacion = ?, " +
			"fecha_egreso_observacion = ?, " +
			"hora_egreso_observacion = ?, " +
			"login_usuario = ? " +
			"WHERE codigo = ? and anio = ?";

	/**
	 * Consulta la cuenta de una admision que tuvo asignada una cama en particular
	 * */
	private static final String consultarUltimaAdmisionAsignadaAUnaCama="SELECT " +
				"au.CUENTA, " +
				"au.fecha_ingreso_observacion, " +
				"au.hora_ingreso_observacion " +			
			"FROM manejopaciente.admisiones_urgencias au " +
			"WHERE au.cama_observacion      = ? ";
	
	/**
 	 * Cadena para consultar el número de una solicitud 
 	 */
	private static final String consultarNumeroSolicitudStr = ""+
		"SELECT MIN(s.numero_solicitud) AS numeroSolicitud "+
		"FROM solicitudes s, admisiones_urgencias au "+
		"WHERE s.cuenta = au.cuenta AND " +
		"au.codigo = ?";
		
	private static final String consultarConductaASeguirStr = ""+
		"SELECT codigo_conducta_valoracion AS codigoConductaASeguir "+
		"FROM valoraciones_urgencias "+
		"WHERE numero_solicitud = ?";	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar el estado de la cama asignada en la
	 * admision a 'definfeccion'
	 */
	private static final String modificarCamaADesinfeccionStr = "" +
	"UPDATE camas1 " +
	"SET estado =? " +
	"WHERE codigo= (SELECT cama_observacion FROM admisiones_urgencias WHERE cuenta = ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para borrar los datos de observacion que
	 * se tienen en la admisión de urgencias.
	 */
	private static final String borrarDatosCamaObservacionStr = "" +
	"UPDATE admisiones_urgencias " +
	"SET fecha_ingreso_observacion = null, hora_ingreso_observacion = null, cama_observacion = null " +
	"WHERE cuenta = ?";		
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar los datos de ingreso a observacion de una admision .
	 */
	private static final String consultarDatosObservacionAdmisionStr = "" +
	"SELECT fecha_ingreso_observacion, hora_ingreso_observacion, cama_observacion, fecha_egreso_observacion, hora_egreso_observacion " +
	"FROM admisiones_urgencias " +
	"WHERE cuenta = ?";
	 
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar el nombre de la cama asociada a esta admision en caso de haberse asignado
	 */
	private static final String consultarCamaStr ="" +
	"SELECT " +
	" ' P.' || substr(pis.nombre,0,16) || '  H.' || substr(hab.nombre,0,16) || '  C.' || c.numero_cama as cama, " +
	"c.descripcion, " +
	"c.codigo " +
	"FROM admisiones_urgencias au " +
	"INNER JOIN camas1 c ON(c.codigo = au.cama_observacion) " +
	"INNER JOIN habitaciones hab ON(hab.codigo = c.habitacion) " +
	"INNER JOIN pisos pis ON(pis.codigo = hab.piso) " +
	"WHERE au.codigo = ? " +
	"AND au.fecha_egreso_observacion is null " +
	"AND au.hora_egreso_observacion is null";

	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar
	 * el estado de una Admision Hospitalaria a egresado tras una reversión de
	 * egreso usando una BD Genérica.
	 */
	
	
	private static final String actualizarAdmisionViaReversionEgresoStr="UPDATE admisiones_urgencias SET cama_observacion=?,fecha_egreso_observacion = null,hora_egreso_observacion = null WHERE cuenta=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para asignar
	 * la hora de salida de observación en una Admision de Urgencias usando
	 * una BD Genérica .
	 */
	private static final String asignarFechaObservacionCamaStr="UPDATE admisiones_urgencias set fecha_egreso_observacion =?, hora_egreso_observacion=? where codigo=? and anio=?";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para ocupar
	* una cama de una admisión a observación de urgencias tras una
	* reversión de egreso usando una BD Genérica.
	*/
	 private static final String ocuparCamaStr="UPDATE camas1 SET estado=" + ConstantesBD.codigoEstadoCamaOcupada + " WHERE codigo=? AND(estado=" + ConstantesBD.codigoEstadoCamaDisponible +" OR estado="+ConstantesBD.codigoEstadoCamaDesinfeccion+" OR estado = "+ConstantesBD.codigoEstadoCamaConSalida+")";
	
	 
	private static final String insertarNumeroAdmisionTriageStr="UPDATE triage SET numero_admision=?, anio_admision=? WHERE consecutivo=? AND consecutivo_fecha=? "; 
	 
	/**
	 * Este método implementa cargarAdmisionUrgencias para Genérica o Hsqldb.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#cargarAdmisionUrgencias
	 * (Connection con, int codigoAdmisionUrgencias)
	 */
	public static ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoAdmisionUrgencias, int anio) throws SQLException
	{
		try
		{
			PreparedStatementDecorator consultarAdmisionUrgencias= new PreparedStatementDecorator(con.prepareStatement(consultarAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarAdmisionUrgencias.setInt(1, codigoAdmisionUrgencias);
			consultarAdmisionUrgencias.setInt(2, anio);
			logger.info("-->"+consultarAdmisionUrgenciasStr);
			logger.info("codigoAdmisionUrgencias-->"+codigoAdmisionUrgencias);
			logger.info("anio-->"+anio);
			return new ResultSetDecorator(consultarAdmisionUrgencias.executeQuery());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Este método implementa cargarAdmisionUrgencias para Genérica o Hsqldb.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#cargarAdmisionUrgencias
	 * (Connection con, int codigoAdmisionUrgencias)
	 */
	public static ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoCuenta) 
	{
		try
		{
			
			logger.info("SQL / cargarAdmisionUrgencias / "+consultarAdmisionUrgencias2Str);
			
			PreparedStatementDecorator consultarAdmisionUrgencias= new PreparedStatementDecorator(con.prepareStatement(consultarAdmisionUrgencias2Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarAdmisionUrgencias.setInt(1, codigoCuenta);
			
			
			
			return new ResultSetDecorator(consultarAdmisionUrgencias.executeQuery());
		}
		catch (Exception e)
		{
			//Por alguna razón no salió bien
			return null;
		}
	}
	
	/**
	 * Este método implementa modificarAdmisionUrgencias para Genérica o Hsqldb.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#modificarAdmisionUrgencias
	 * (Connection con,  int codigoOrigen, String fecha, String hora, String
	 * numeroIdentificacionMedico, String codigoTipoIdentificacionMedico, String
	 * numeroAutorizacion, int codigoCausaExterna, String fechaObservacion,
	 * String horaObservacion, int codigoCama, String loginUsuario)
	 */
	public static int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio,
																		 int codigoOrigen, String fecha, String hora, int codigoMedico,
																		 /*String numeroAutorizacion, */int codigoCausaExterna, 
																		 String fechaObservacion, String horaObservacion, 
																		 int codigoCama, String fechaEgresoObservacion, 
																		 String horaEgresoObservacion, String loginUsuario, String estado, DaoFactory myFactory) 
																		 throws SQLException
	 {
			logger.info("\n entro a modificarAdmisionUrgencias ");
			int resp = 0, returnedVal = 0;
			
			boolean resp0 = false, resp1 = false; // Estos valores son true si todo salió bien
			
			/* Iniciamos la transacción */
			if(estado.equals("empezar"))
				resp0 = myFactory.beginTransaction(con);
			else resp0 = true;
			
			/*
			 * Los datos opcionales que vengan como "", los convertimos a null en los siguientes casos :
			 * Si son FK's a otra tabla, si son fechas o si son horas.
			 */
			if (fechaObservacion != null && fechaObservacion.equals(""))
			{
					fechaObservacion = null;
			}
			if (horaObservacion != null && horaObservacion.equals("")) 
			{
					horaObservacion = null;
			}
			if (fechaEgresoObservacion != null && fechaEgresoObservacion.equals(""))
			{
					fechaEgresoObservacion = null;
			}
			if (horaEgresoObservacion != null && horaEgresoObservacion.equals("")) 
			{
					horaEgresoObservacion = null;
			}

			/* Inserción de datos de admisión por urgencias */
			PreparedStatementDecorator modificarAdmisionUrgencias =  new PreparedStatementDecorator(con.prepareStatement(modificarAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			modificarAdmisionUrgencias.setInt   ( 1, codigoOrigen);
			modificarAdmisionUrgencias.setString( 2, fecha);
			modificarAdmisionUrgencias.setString( 3, hora);

			if (codigoMedico>0)
			{
					modificarAdmisionUrgencias.setInt( 4, codigoMedico);
			}
			else
			{
					modificarAdmisionUrgencias.setString( 4, null);
			}

			//modificarAdmisionUrgencias.setString( 5, numeroAutorizacion);
			modificarAdmisionUrgencias.setInt   ( 5, codigoCausaExterna);
			modificarAdmisionUrgencias.setString( 6, fechaObservacion);
			modificarAdmisionUrgencias.setString(7, horaObservacion);

			/* Si llega código cama == -1, es pq no debemos insertar una cama, en su lugar, insertamos un null */

			if (codigoCama != -1)
			{
				int estadoCama = ConstantesBD.codigoEstadoCamaOcupada;
				
				/////////////////////////////////////////////////////////////////////////////////////////////////
				//modificacion por tarea 1347
				//se pregunta si tiene evolucion con orden de salida, de ser asi el estado de la
				//cama debe de ser "con salida".
			//	logger.info("\n el tiene evolucion -->"+Utilidades.tieneEvolucionConSalidaXAdminUrg(con, codigoAdmisionUrgencias+"", anio+""));
				if(Utilidades.tieneEvolucionConSalidaXAdminUrg(con, codigoAdmisionUrgencias+"", anio+""))
					estadoCama=ConstantesBD.codigoEstadoCamaConSalida;
				///////////////////////////////////////////////////////////////////////////////////////////	
				
				
					modificarAdmisionUrgencias.setInt (8, codigoCama);
					/*Cama cama = new Cama();
					cama.init(System.getProperty("TIPOBD"));
					cama.cargarCama(con, codigoCama+"");
					cama.setEstado(1);
					cama.modificarCama(con, codigoCama+"");*/
					Camas1 camas1 = new Camas1();
				    camas1.setCodigo(codigoCama);
				    camas1.detalleCama1(con);
				    camas1.setEstadoCama(new InfoDatosInt(estadoCama));
				    camas1.modificarCamas1Transaccional(con, ConstantesBD.continuarTransaccion);
			}
			else 
			{
					modificarAdmisionUrgencias.setString (8, null);
			}

			modificarAdmisionUrgencias.setString(9, fechaEgresoObservacion);
			modificarAdmisionUrgencias.setString(10, horaEgresoObservacion);
			modificarAdmisionUrgencias.setString(11, loginUsuario);
			modificarAdmisionUrgencias.setInt   (12, codigoAdmisionUrgencias);
			modificarAdmisionUrgencias.setInt   (13, anio);

			try 
			{
					returnedVal = modificarAdmisionUrgencias.executeUpdate();
			}	
			catch (SQLException sqle) 
			{
					myFactory.abortTransaction(con);
					throw sqle;
			}

			resp1 = (returnedVal > 0);
			modificarAdmisionUrgencias.close();
			
			if(estado.equals("finalizar"))
			{
					/* Terminamos la transacción, bien sea con un commit o un rollback */
					if (resp0 && resp1) 
					{
							myFactory.endTransaction(con);
							resp = returnedVal;
					}
					else 
					{
							myFactory.abortTransaction(con);
							resp = 0;
				}
			}
			else resp = returnedVal;
			/* Retornamos el numero de registros modificados, o 0 si se hizo rollback */
			return resp;
	}
	
	/**
	 * Actualiza el numero de admision en el triage 
	 * @param con
	 * @param codigoAdmision
	 * @param anioAdmision
	 * @param consecutivoTtriage
	 * @param consecutivoFechaTriage
	 * @return
	 */
	private static boolean actualizarNumeroAdmisionEnTriage(Connection con, String codigoAdmision, String anioAdmision, String consecutivoTtriage, String consecutivoFechaTriage)
	{
		boolean resultado=false;
		PreparedStatement pst=null;
		try
		{
			pst= con.prepareStatement(insertarNumeroAdmisionTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, codigoAdmision);
			pst.setString(2, anioAdmision);
			pst.setString(3, consecutivoTtriage);
			pst.setString(4, consecutivoFechaTriage);
			if(pst.executeUpdate()>0)
				resultado=true;
		}
		catch(Exception e){
			Log4JManager.error("ERROR actualizarNumeroAdmisionEnTriage", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	
	/**
	 * Este método implementa modificarAdmisionUrgencias para Genérica o Hsqldb
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#modificarAdmisionUrgencias
	 * (Connection con,  int codigoOrigen, String fecha, String hora, String
	 * numeroIdentificacionMedico, String codigoTipoIdentificacionMedico, String
	 * numeroAutorizacion, int codigoCausaExterna, String fechaObservacion,
	 * String horaObservacion, int codigoCama, String loginUsuario)
	 */
	public static int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio, 
																		int codigoOrigen, String fecha, String hora, int codigoMedico,
																		/*String numeroAutorizacion, */int codigoCausaExterna, 
																		String fechaObservacion, String horaObservacion, int codigoCama,
																		String fechaEgresoObservacion, String horaEgresoObservacion, 
																		String loginUsuario, DaoFactory myFactory) throws SQLException 
	{
			int resp = 0, returnedVal = 0;
			
			boolean resp0 = false, resp1 = false; // Estos valores son true si todo salió bien
			/* Iniciamos la transacción */
			resp0 = myFactory.beginTransaction(con);
	
			/*
			 * Los datos opcionales que vengan como "", los convertimos a null en los siguientes casos :
			 * Si son FK's a otra tabla, si son fechas o si son horas.
			 */
			if (fechaObservacion != null && fechaObservacion.equals(""))
			{
					fechaObservacion = null;
			}
			if (horaObservacion != null && horaObservacion.equals("")) 
			{
					horaObservacion = null;
			}
			if (fechaEgresoObservacion != null && fechaEgresoObservacion.equals("")) 
			{
					fechaEgresoObservacion = null;
			}
			if (horaEgresoObservacion != null && horaEgresoObservacion.equals("")) 
			{
					horaEgresoObservacion = null;
			}
	
			/* Inserción de datos de admisión por urgencias */
			PreparedStatementDecorator modificarAdmisionUrgencias =  new PreparedStatementDecorator(con.prepareStatement(modificarAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			modificarAdmisionUrgencias.setInt   ( 1, codigoOrigen);
			modificarAdmisionUrgencias.setString( 2, fecha);
			modificarAdmisionUrgencias.setString( 3, hora);
	
			if (codigoMedico>0)
			{
					modificarAdmisionUrgencias.setInt( 4, codigoMedico);
			}
			else
			{
					modificarAdmisionUrgencias.setString( 4, null);
			}
	
			//modificarAdmisionUrgencias.setString( 5, numeroAutorizacion);
			modificarAdmisionUrgencias.setInt   ( 5, codigoCausaExterna);
			modificarAdmisionUrgencias.setString( 6, fechaObservacion);
			modificarAdmisionUrgencias.setString(7, horaObservacion);
	
			/* Si llega código cama == -1, es pq no debemos insertar una cama, en su lugar, insertamos un null */
	
			if (codigoCama != -1) 
			{
					modificarAdmisionUrgencias.setInt (8, codigoCama);
					Cama cama = new Cama();
					cama.init(System.getProperty("TIPOBD"));
					cama.cargarCama(con, codigoCama+"");
					cama.setEstado(1);
					cama.modificarCama(con, codigoCama+"");
			}
			else 
			{
					modificarAdmisionUrgencias.setString (8, null);
			}
	
			modificarAdmisionUrgencias.setString(9, fechaEgresoObservacion);
			modificarAdmisionUrgencias.setString(10, horaEgresoObservacion);
			modificarAdmisionUrgencias.setString(11, loginUsuario);
			modificarAdmisionUrgencias.setInt   (12, codigoAdmisionUrgencias);
			modificarAdmisionUrgencias.setInt   (13, anio);
	
			try
			{
					returnedVal = modificarAdmisionUrgencias.executeUpdate();
			}	
			catch (SQLException sqle) 
			{
					myFactory.abortTransaction(con);
					throw sqle;
			}
	
			resp1 = (returnedVal > 0);
			modificarAdmisionUrgencias.close();
	
			/* Terminamos la transacción, bien sea con un commit o un rollback */
			if (resp0 && resp1) 
			{
					myFactory.endTransaction(con);
					resp = returnedVal;
			}
	
			else 
			{
					myFactory.abortTransaction(con);
					resp = 0;
			}
	
			/* Retornamos el numero de registros modificados, o 0 si se hizo rollback */
			return resp;
	}
	
	/**
	 * Si encuentra un egreso asociado a esta admision-cuenta con medico no nulo, significa que no se puede modificar los
	 * datos de ingreso a observacion. De lo contrario si.
	 * Nota Raul: esta funcionalidad queda por defecto en true, ya que la existencia del egreso se esta validando directamente en
	 * desicionTipoAdmision.jsp y para cualquier caso desvia a una pagina de error, ya que estos datos de ingreso se pueden modificar
	 * en los casos restantes.
	 */
	public static  boolean isModificacionCamaHabilitada() throws SQLException
	{
		return true;
	}

	public static boolean mostrarDatosCama(Connection con, int codigoAdmisionUrgencias) throws SQLException 
	{
		PreparedStatementDecorator consultarNumeroSolicitud =  new PreparedStatementDecorator(con.prepareStatement(consultarNumeroSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		PreparedStatementDecorator consultarConductaASeguir =  new PreparedStatementDecorator(con.prepareStatement(consultarConductaASeguirStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator conductaASeguir_rs;
		//Desde la admision de urgencias consultamos la valoracion asociada a la admision, en dos pasos:
		//primero obtenemos el numero de solicitud que identifica la valoracion, en caso de que exista

		consultarNumeroSolicitud.setInt(1,codigoAdmisionUrgencias);
		ResultSetDecorator numeroSolicitud_rs = new ResultSetDecorator(consultarNumeroSolicitud.executeQuery());

		if(numeroSolicitud_rs.next())
		{
				//Con el numero de solicitud obtenido se obtiene la conducta a seguir en la valoracion
				consultarConductaASeguir.setInt(1,numeroSolicitud_rs.getInt("numeroSolicitud"));
				conductaASeguir_rs = new ResultSetDecorator(consultarConductaASeguir.executeQuery());
				//Si el codigo_conducta_valoracion es 3, quiere decir que se le puede asignar cama de observacion
				if (conductaASeguir_rs.next())
				{
						if( conductaASeguir_rs.getInt("codigoConductaASeguir") == ConstantesBD.codigoConductaSeguirCamaObservacion) 
						return true;
				}
		}
		return false;
	}

	/**
	 * Implementación del borrado de los datos de cama de observacion y de la liberacion de la cama,
	 * dejandola en estado 2 de desinfeccion
	 *
	 * @see com.princetonsa.dao.ValoracionUrgenciasDao#borrarDatosObservacion(Connection, int)
	 */
	public static int borrarDatosObservacion(Connection con, int numeroCuenta, int institucion) throws SQLException
	{
			int resp0, resp1;
			PreparedStatement pst=null;
			PreparedStatement pst2=null;
			try
			{
					if (con == null || con.isClosed())
					{
							//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
							throw new SQLException ("Error SQL: Conexión cerrada");
					}

					pst= con.prepareStatement(modificarCamaADesinfeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst.setInt(1, Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(institucion)));
					pst.setInt(2, numeroCuenta);
					resp0 = pst.executeUpdate();

					pst2= con.prepareStatement(borrarDatosCamaObservacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst2.setInt(1, numeroCuenta);
					resp1 = pst2.executeUpdate();
					return ((resp0 == 1) && (resp1 == 1))? 1:0;
			}
			catch(Exception e){
				Log4JManager.error("ERROR borrarDatosObservacion", e);
			}
			finally{
				try{
					if(pst2 != null){
						pst2.close();
					}
					if(pst != null){
						pst.close();
					}
				}
				catch (SQLException sql) {
					Log4JManager.error("ERROR cerrando objetos persistentes", sql);
				}
			}
			return 0;
	}

	public static boolean estaAsignadaCamaObservacion(Connection con, int numeroCuenta) 
	{
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
			try
			{
				if (con == null || con.isClosed())
				{
					//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				
				pst= new PreparedStatementDecorator(con.prepareStatement(consultarDatosObservacionAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, numeroCuenta);

				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next()){
					if(UtilidadTexto.isEmpty(rs.getString("cama_observacion")))
					{
						if(UtilidadTexto.isEmpty(rs.getString("fecha_ingreso_observacion"))
						&& UtilidadTexto.isEmpty(rs.getString("hora_ingreso_observacion")))
						{
							if(UtilidadTexto.isEmpty(rs.getString("hora_egreso_observacion"))
							&& UtilidadTexto.isEmpty(rs.getString("fecha_egreso_observacion")))
								return false;
							else throw new SQLException("DATOS INCONSISTENTES: No se tiene una cama de observación asignada, ni fecha y hora de ingreso a observación, pero si se tiene fecha y hora de egreso a observación. Número de cuenta del paciente: "+numeroCuenta);
						}
						else throw new SQLException("DATOS INCONSISTENTES: No se tiene una cama de observación asignada, pero si fecha u hora de ingreso a observación asignada. Número de cuenta del paciente: "+numeroCuenta);
					}
					return true;
				}
				else throw new SQLException("ERROR SQL: No se encontró una admisión de urgencias con número de cuenta "+numeroCuenta);
			}
			catch(Exception e){
				Log4JManager.error("ERROR estaAsignadaCamaObservacion", e);
			}
			finally{
				try{
					if(rs != null){
						rs.close();
					}
					if(pst != null){
						pst.close();
					}
				}
				catch (SQLException sql) {
					Log4JManager.error("ERROR cerrando objetos persistentes", sql);
				}
			}
			return false;

	}
					
					
	/**
	 * Para consultar la cama asociada a esta admision.
	 * Metodo usado para cargar resumen de los datos del paciente
	 */
	public static String[] getCama(Connection con, int codigoAdmision) throws SQLException
	{
			ResultSetDecorator rs=null;
			PreparedStatementDecorator pst=null;
			final Logger logger = Logger.getLogger(SqlBaseAdmisionUrgenciasDao.class);
			if (con == null || con.isClosed())
			{
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			String resp[]=new String[3];
			try
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultarCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoAdmision);
				rs = new ResultSetDecorator(pst.executeQuery());
			   
				if(rs.next())
				{
				    resp[0]=rs.getString("cama");
				    resp[1]=rs.getString("descripcion");
				    resp[2]=rs.getString("codigo");
				}
				else
				{
				    resp[0]="";
				    resp[1]="No se ha asignado cama de urgencias";
				    resp[2]="0";
				}
			}
			catch(Exception e){
				Log4JManager.error("ERROR getCama", e);
			}
			finally{
				try{
					if(rs != null){
						rs.close();
					}
					if(pst != null){
						pst.close();
					}
				}
				catch (SQLException sql) {
					Log4JManager.error("ERROR cerrando objetos persistentes", sql);
				}
			}
			return resp;
	}

	public static String[] getUltimaCama(Connection con, String consulta,  int codigoAdmision) throws SQLException{
		ResultSetDecorator rs=null;
		PreparedStatementDecorator pst=null;
		final Logger logger = Logger.getLogger(SqlBaseAdmisionUrgenciasDao.class);
		if (con == null || con.isClosed())
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		String resp[]=new String[5];
		try
		{
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoAdmision);
			rs = new ResultSetDecorator(pst.executeQuery());
		   
			if(rs.next())
			{
			    resp[0]=rs.getString("cama");
			    resp[1]=rs.getString("descripcion");
			    resp[2]=rs.getString("codigo");
			    resp[3]=rs.getString("fecha_egreso_observacion");
			    resp[4]=rs.getString("hora_egreso_observacion");
			}
			else
			{
			    resp[0]="";
			    resp[1]="No se ha asignado cama de urgencias";
			    resp[2]="0";
			    resp[2]=null;
			    resp[2]=null;
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR getCama", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}

	

	/**
	 * Este método implementa insertarAdmisionUrgencias para Genérica.
	 * Manejando la transaccion dependiendo del estado que le llega en el
	 * parametro
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#insertarAdmisionUrgencias
	 * (Connection, int, String, String, String, String, String, int, String,
	 * String, int, String, int, String)
	 */
	public static int insertarAdmisionUrgencias(Connection con, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String loginUsuario, int idCuenta, String estado, String insertarAdmisionUrgenciasStr, String consecutivoTriage, String consecutivoFechaTriage,String fechaEgresoObservacion,String horaEgresoObservacion,int institucion) throws SQLException {
		int resp = 0, returnedVal = 0;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
//		int anio = (fecha != null) ? Integer.parseInt(UtilidadTexto.separarNombresDeCodigos(fecha, 2)[0]) : 0;
		//El anio es el anio del sistema, no depende de la fecha de la admision
		GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));
		int anio = calendar.get(Calendar.YEAR);
		boolean resp0 = false, resp1 = false, resp2 = false, resp4= false; // Estos valores son true si todo salió bien

		/* Iniciamos la transacción */
		if(estado.equals("empezar"))
			resp0 = myFactory.beginTransaction(con);
		else resp0 = true;

		/*
		 * Los datos opcionales que vengan como "", los convertimos a null en los siguientes casos :
		 * Si son FK's a otra tabla, si son fechas o si son horas.
		 */

		if (fechaObservacion != null && fechaObservacion.equals("")) {
			fechaObservacion = null;
		}
		if (horaObservacion != null && horaObservacion.equals("")) {
			horaObservacion = null;
		}

		int codigo=UtilidadBD.obtenerSiguienteValorSecuencia("seq_admisiones_urgencias");
		/* Inserción de datos de admisión por urgencias */
		PreparedStatementDecorator insertarAdmisionUrgencias =  new PreparedStatementDecorator(con.prepareStatement(insertarAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		String consecutivo =  UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAdmisionUrgencias, institucion);
		
		if(Utilidades.validarExisteConsecutivo(consecutivo)){
			
		insertarAdmisionUrgencias.setInt   ( 1, codigo);
		insertarAdmisionUrgencias.setInt   ( 2, Utilidades.convertirAEntero(consecutivo));
		insertarAdmisionUrgencias.setInt   ( 3, anio);
		insertarAdmisionUrgencias.setInt   ( 4, codigoOrigen);
		insertarAdmisionUrgencias.setString( 5, fecha);
		insertarAdmisionUrgencias.setString( 6, hora);
		if (codigoMedico>0)
		{
			insertarAdmisionUrgencias.setInt( 7, codigoMedico);
		}
		else
		{
			insertarAdmisionUrgencias.setString( 7, null);
		}

		//insertarAdmisionUrgencias.setString( 6, numeroAutorizacion);
		insertarAdmisionUrgencias.setInt   ( 8, codigoCausaExterna);
		insertarAdmisionUrgencias.setString( 9, fechaObservacion);
		insertarAdmisionUrgencias.setString(10, horaObservacion);

		/* Si llega código cama == -1, es pq no debemos insertar una cama, en su lugar, insertamos un null */

		if (codigoCama != -1) {
			insertarAdmisionUrgencias.setInt (11, codigoCama);
			Cama cama = new Cama();
			cama.init(System.getProperty("TIPOBD"));
			cama.cargarCama(con,codigoCama+"");
			cama.setEstado(1);
			cama.modificarCama(con, codigoCama+"");
		}
		else {
			insertarAdmisionUrgencias.setString (11, null);
		}

		insertarAdmisionUrgencias.setString(12, loginUsuario);
		insertarAdmisionUrgencias.setInt   (13, idCuenta);

		if(consecutivoTriage.trim().equals(""))
			insertarAdmisionUrgencias.setObject(14, null);
		else
			insertarAdmisionUrgencias.setString(14, consecutivoTriage);
		
		if(consecutivoFechaTriage.trim().equals(""))
			insertarAdmisionUrgencias.setObject(15, null);
		else
			insertarAdmisionUrgencias.setString(15, consecutivoFechaTriage);
		
		if(fechaEgresoObservacion.equals(""))
			insertarAdmisionUrgencias.setNull(16,Types.DATE);
		else
			insertarAdmisionUrgencias.setDate(16,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaEgresoObservacion)));
		
		if(horaEgresoObservacion.equals(""))
			insertarAdmisionUrgencias.setNull(17,Types.TIME);
		else
			insertarAdmisionUrgencias.setString(17,horaEgresoObservacion);
		
		try {
			returnedVal = insertarAdmisionUrgencias.executeUpdate();
		}	catch (SQLException sqle) {
				logger.error("Error insertando admision de urgencias: "+sqle);
				myFactory.abortTransaction(con);
				throw sqle;
		}
		resp1 = (returnedVal > 0);
		insertarAdmisionUrgencias.close();
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAdmisionUrgencias,institucion, consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		}else{
			
			myFactory.abortTransaction(con);
			resp = 0;
		}
		
		
		int lastValue = codigo;
		//en caso de que se inserte la informacion del triage en la admision entonces toca
		// actualizar el triage con el numero de admision creado
		if(!consecutivoTriage.trim().equals(""))
			resp4=actualizarNumeroAdmisionEnTriage(con, lastValue+"",  anio+"", consecutivoTriage, consecutivoFechaTriage);
		else
			resp4=true;
		
		
		if (estado.equals("finalizar"))
		{
			/* Terminamos la transacción, bien sea con un commit o un rollback */
			if (resp0 && resp1 && resp2 && resp4) {
				myFactory.endTransaction(con);
				resp = lastValue;
			}

			else {
				myFactory.abortTransaction(con);
				resp = 0;
			}

		}
		else resp = lastValue;
		/* Retornamos el código de la inserción realizada, o 0 si se hizo rollback */
		return resp;
	}

	public static int pasarCamaADesinfeccionTransaccional (Connection con, int numeroAdmision, int anioAdmision, String estado, int institucion) throws SQLException
	{
		int resp0=0, resp1=0;
		try
		{
		    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if (con == null || con.isClosed())
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar"))
			{
			    if (myFactory.beginTransaction(con))
			    {
			        resp0=1;
			    }
			    else
			    {
			        resp0=0;
			    }
			}
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}

			//Si no tiene cama asociada no corremos la actualización sino de
			//una vez decimos que resp1 es 1
			
			if (tieneCamaAdmisionUrgencias(con, numeroAdmision, anioAdmision))
			{
				PreparedStatementDecorator pasarCamaADesinfeccionStatement= new PreparedStatementDecorator(con.prepareStatement(pasarCamaADesinfeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				pasarCamaADesinfeccionStatement.setInt(1, Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(institucion)+""));
				pasarCamaADesinfeccionStatement.setInt(2, numeroAdmision);
				pasarCamaADesinfeccionStatement.setInt(3, anioAdmision);
				resp1=pasarCamaADesinfeccionStatement.executeUpdate();
				pasarCamaADesinfeccionStatement.close();
			}
			else
			{
				resp1=1;
			}

			if (resp0<1||resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
			    myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
				    myFactory.endTransaction(con);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
				final Logger logger = Logger.getLogger(SqlBaseAdmisionUrgenciasDao.class);
				logger.warn("Excepción en pasarCamaADesinfeccionTransaccional " + e);
				throw e;
		}
	}

	/**
	 * Método privado que permite averiguar si una admisión de urgencias tiene
	 * o no asociada una cama de observación
	 * @param con Conexión con una BD Genérica
	 * @param numeroAdmision Número de la admisión de urgencias
	 * @return
	 * @throws SQLException
	 */
	private static boolean tieneCamaAdmisionUrgencias (Connection con, int numeroAdmision, int anioAdmision) throws SQLException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean result=true;
		try{
			pst= new PreparedStatementDecorator(con.prepareStatement(tieneCamaAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroAdmision);
			pst.setInt(2, anioAdmision);
			rs=new ResultSetDecorator(pst.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("numResultados")<1)
				{
					result=false;
				}
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR tieneCamaAdmisionUrgencias", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	public  static int asignarFechaObservacionTransaccional (Connection con, int numeroAdmision, int anioAdmision, String fechaObservacion, String horaObservacion, String estado) throws SQLException
	{
		logger.info("\n numeroAdmision -->"+numeroAdmision+"   anioAdmision-->"+anioAdmision+"  fechaObservacion -->"+fechaObservacion+"  horaObservacion--> "+horaObservacion+"   estado--> "+estado);
		
		int resp0=0, resp1=0;
		try
		{
		    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if (con == null || con.isClosed())
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar"))
			{
			    if (myFactory.beginTransaction(con))
			    {
			        resp0=1;
			    }
			    else
			    {
			        resp0=0;
			    }
			}
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}

			PreparedStatement asignarFechaObservacionCamaStatement= con.prepareStatement(asignarFechaObservacionCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			asignarFechaObservacionCamaStatement.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaObservacion)));
			asignarFechaObservacionCamaStatement.setString(2, horaObservacion);
			asignarFechaObservacionCamaStatement.setInt(3, numeroAdmision);
			asignarFechaObservacionCamaStatement.setInt(4, anioAdmision);
			resp1=asignarFechaObservacionCamaStatement.executeUpdate();
			asignarFechaObservacionCamaStatement.close();
			if (resp0<1||resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
			    myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
				    myFactory.endTransaction(con);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			final Logger logger = Logger.getLogger(SqlBaseAdmisionUrgenciasDao.class);
			logger.warn("Excepción en asignarFechaObservacionCamaTransaccional " + e);
			throw e;
		}
	}

	/**
	* Método que implementa el efecto de una reversión de egreso en una
	* admisión de urgencias en una BD Genérica 
	*
	* @see com.princetonsa.dao.AdmisionUrgenciasDao#reversarEgresoYAdmisionTransaccional(Connection , int , int , String) throws SQLException
	*/
	public static int reversarEgresoYAdmisionTransaccional(Connection con, int idCuenta, int codigoCama, String estado) throws SQLException
	{
		int resp0;
		int resp1;
		int resp2;
		resp0 = resp1 = resp2 = 0;

		try
		{
		    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if(con == null || con.isClosed() )
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}

			if(estado == null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if(estado.equals("empezar") )
			{
			    if (myFactory.beginTransaction(con))
			    {
			        resp0=1;
			    }
			    else
			    {
			        resp0=0;
			    }
			}
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0 = 1;
			}
			if (codigoCama>0)
			{
				PreparedStatementDecorator ocuparCamaStatement =  new PreparedStatementDecorator(con.prepareStatement(ocuparCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ocuparCamaStatement.setInt(1, codigoCama);
				resp1 = ocuparCamaStatement.executeUpdate();
				ocuparCamaStatement.close();
			}
			else
				resp1=1;
			
			PreparedStatementDecorator actualizarAdmisionViaReversionEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarAdmisionViaReversionEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**MT 3189-4132-4319 - Diana Ruiz*/
			
			if(codigoCama>0)
				actualizarAdmisionViaReversionEgresoStatement.setInt(1, codigoCama);
			else
				actualizarAdmisionViaReversionEgresoStatement.setNull(1, Types.INTEGER);
			
			actualizarAdmisionViaReversionEgresoStatement.setInt(2, idCuenta);
			resp2 = actualizarAdmisionViaReversionEgresoStatement.executeUpdate();
			actualizarAdmisionViaReversionEgresoStatement.close();
			logger.info("resp0 => "+resp0);
			logger.info("resp1 => "+resp1);
			logger.info("resp2 => "+resp2);

			if(resp0 < 1 || resp1 < 1 || resp2 < 1)
			{
			    myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar") )
				{
				    myFactory.endTransaction(con);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
				final Logger logger = Logger.getLogger(SqlBaseAdmisionUrgenciasDao.class);
				logger.warn("Excepción en SqlBaseAdmisionUrgenciasDao.reversarEgresoYAdmisionTransaccional " + e);
				throw e;
		}
	}

	/**
	 * Implementación de la consulta de datos basicos de una admision urgencias
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#getBasicoAdmision (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	public static ResultSetDecorator getBasicoAdmision(Connection con, int numeroCuenta, String consultarBasicoAdmisionStr) throws SQLException
	{
		if (con == null || con.isClosed())
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}

		try
		{
			PreparedStatementDecorator consultarBasicoAdmisionStatement= new PreparedStatementDecorator(con.prepareStatement(consultarBasicoAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarBasicoAdmisionStatement.setInt(1, numeroCuenta);
			return new ResultSetDecorator(consultarBasicoAdmisionStatement.executeQuery());
		}
		catch (SQLException sql)
		{
			logger.warn("Error consultando admisión urgencias asociada al numero de cuenta "+numeroCuenta+" en la tabla 'admisiones_urgencias'.\n Se lanzó la siguiente excepción:\n"+sql);
			throw sql;
		}
	}
	
	/**
	 * Método para actualizar la fecha/hora ingreso a observacion de la admision de urgencias
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean actualizarFechaHoraIngresoObservacion(Connection con,HashMap campos)
	{
		boolean resultado = false;
		PreparedStatement pst=null; 
		try
		{
			//***************SE TOMAN LOS PARÁMETROS*******************************************
			String fechaIngresoObservacion = campos.get("fechaIngresoObservacion").toString();
			String horaIngresoObservacion = campos.get("horaIngresoObservacion").toString();
			int idCuenta = Utilidades.convertirAEntero(campos.get("idCuenta").toString());
			//***********************************************************************************
			
			String consulta = " UPDATE admisiones_urgencias SET fecha_ingreso_observacion = ?, hora_ingreso_observacion = ? WHERE cuenta = ?";
			pst =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			if(!fechaIngresoObservacion.equals(""))
				pst.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaIngresoObservacion)));
			else
				pst.setNull(1,Types.DATE);
			if(!horaIngresoObservacion.equals(""))
				pst.setString(2,horaIngresoObservacion);
			else
				pst.setNull(2,Types.TIME);
			pst.setInt(3,idCuenta);
			resultado = pst.executeUpdate()>0?true:false;
		}
		catch(Exception e){
			Log4JManager.error("ERROR actualizarFechaHoraIngresoObservacion", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	/**
	 * Consulta la cuenta de una admision que tuvo asignada una cama en particular
	 * 
	 * @param con conexion 
	 * @param codigoCama
	 * @return Codigo de la cuenta
	 */
	public static String obtenerCuentaUltimaAdmisionXCama(Connection con,int codigoCama) {
		String cuenta=null;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		try {
			boolean isOracle=System.getProperty("TIPOBD").equals("ORACLE");
			boolean isPostgres=System.getProperty("TIPOBD").equals("POSTGRESQL");
			String consulta="";
			if (isOracle) {
				consulta+="SELECT RS.CUENTA as CUENTA FROM ("+consultarUltimaAdmisionAsignadaAUnaCama+
				" AND au.fecha_egreso_observacion is null " +
				" AND au.hora_egreso_observacion is null " +
				"ORDER BY au.fecha_ingreso_observacion DESC,au.hora_ingreso_observacion DESC "+
				") RS WHERE ROWNUM <=1 ";
			}else {
				if (isPostgres) {
					consulta+=consultarUltimaAdmisionAsignadaAUnaCama+
					" AND au.fecha_egreso_observacion is null " +
					" AND au.hora_egreso_observacion is null " +
					" ORDER BY au.fecha_ingreso_observacion DESC,au.hora_ingreso_observacion DESC "+
					" LIMIT 1 ";
				}
			}
			
			pst= con.prepareStatement(consulta);
			pst.setInt(1,codigoCama);
			rs=pst.executeQuery();
			if(rs.next()){
				cuenta=rs.getString("CUENTA");
			}else{
				consulta="";
				if (isOracle) {
					consulta+="SELECT RS.CUENTA as CUENTA FROM ("+consultarUltimaAdmisionAsignadaAUnaCama+
					" ORDER BY au.fecha_ingreso_observacion DESC,au.hora_ingreso_observacion DESC "+
					") RS WHERE ROWNUM <=1 ";
				}else {
					if (isPostgres) {
						consulta+=consultarUltimaAdmisionAsignadaAUnaCama+
						" ORDER BY au.fecha_ingreso_observacion DESC,au.hora_ingreso_observacion DESC "+
						" LIMIT 1 ";
					}
				}
				pst2= con.prepareStatement(consulta);
				pst2.setInt(1,codigoCama);
				rs2=pst2.executeQuery();
				if(rs2.next()){
					cuenta=rs.getString("CUENTA");
				}
			}
		}catch(Exception e){
			Log4JManager.error("ERROR obtenerCuentaUltimaAdmisionXCama", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return cuenta;
	}
}
