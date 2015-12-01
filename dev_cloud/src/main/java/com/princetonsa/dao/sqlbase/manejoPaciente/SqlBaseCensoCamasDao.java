package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.TreeMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IDiagnosticosMundo;
import com.servinte.axioma.persistencia.UtilidadTransaccion;



/**
 * Clase Censo De Camas
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseCensoCamasDao
{
	/**
	 *Mensajes de error 
	 */
		
	/*----------------------------------------------------------------------------------------
	 *                              ATRIBUTOS CENSO DE CAMAS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Cadena de Consulta todas las camas
	 */
	private static final String strCadenaConsultaCensoCamas = "SELECT DISTINCT "+
			"getnomhabitacioncama(c.codigo) AS nombrehabitacion," +
			"c.numero_cama As cama,"+
			"c.codigo AS codigocama,"+
			"c.estado AS estadocama," +
			"getcolorestadocama(c.estado) AS color,"+
			"getnombreestadocama(c.estado) AS nombreestadocama,"+
			"getnombretipohabitacion(c.habitacion) AS nombretipohabitacion," +
			"c.habitacion AS habitacioncama," +
			"tuc.nombre AS tipousuariocama," +
			"tuc.sexo AS sexocama," +
			"tuc.ind_sexo_restrictivo AS indicativorestrictivo," +
			"h.piso AS codigopiso," +
			"h.codigo_habitac AS codhabitmostar," +
			"getcodigopiso(h.piso) AS codpisomostrar,"+
			"getnombrepiso(h.piso) AS nombrepiso," +
			"c.centro_costo AS codigocentrocosto," +
			"getnomcentrocosto(c.centro_costo) AS nombrecentrocosto," +
			"CASE WHEN tuc.sexo = '1' THEN 'M' ELSE coalesce('F','')  END AS restriccioncama " +
			"FROM camas1 c INNER JOIN tipos_usuario_cama tuc ON (tuc.codigo=c.tipo_usuario_cama) " +
			" INNER JOIN  habitaciones h ON (h.codigo=c.habitacion) ";
						
	/**
	 * Cadena de consulta de Convenio por Cuenta
	 */
	private static final String strCadenaConsultaConvenioXCuenta = "SELECT "+
			" CASE WHEN getesconvenioparticular(getconvenioxingreso(c.id_ingreso))='"+ConstantesBD.acronimoSi+"' THEN getnomdeudoringreso(c.id_ingreso) ELSE getnombreconvenioxingreso(c.id_ingreso) END AS convenio," +
			"c.via_ingreso AS viaingreso," +
			"getconsecutivoingreso(c.id_ingreso) AS consecutivoingreso "+
			"FROM cuentas c "+
			"WHERE id=? ";
	
	/**
	 * Cadena que consulta la informacion del paciente en la cama 
	 */
	private static final String strCadenaConsultaPaciente = "SELECT " +
			"getnombrepersona(p.codigo) AS nombrepac," +
			"p.numero_identificacion AS identificacionpac," +
			"p.tipo_identificacion AS tipoidentificacionpac," +
			"getnombretipoidentificacion(p.tipo_identificacion) AS nombretipoidentificacionpac," +
			"p.sexo AS sexopac," +
			"getdescripcionsexo(p.sexo) AS nombresexopac," +
			"p.fecha_nacimiento AS fechanacimientopac," +
			"getnumhistocli(p.codigo) AS historiaclinica," +
			"p.codigo AS codigopersona " +
			"FROM personas p " +
			"INNER JOIN cuentas c ON (c.codigo_paciente=p.codigo)" +
			"WHERE c.id=?";
	
	/**
	 * Cadena de consulta de la informacion del paciente si el estado de la cama es Reservada.
	 */
	private static final String strCadenaConsultaPacienteReserva = "SELECT " +
			"getnombrepersona(p.codigo) AS nombrepac," +
			"p.numero_identificacion AS identificacionpac," +
			"p.tipo_identificacion AS tipoidentificacionpac," +
			"getnombretipoidentificacion(p.tipo_identificacion) AS nombretipoidentificacionpac," +
			"p.sexo AS sexopac," +
			"getdescripcionsexo(p.sexo) AS nombresexopac," +
			"p.fecha_nacimiento AS fechanacimientopac," +
			"getnumhistocli(p.codigo) AS historiaclinica," +
			"p.codigo AS codigopersona," +
			"rc.observaciones As observaciones " +
			"FROM personas p " +
			"INNER JOIN reservar_cama rc ON (rc.codigo_paciente=p.codigo) " +
			"WHERE rc.codigo_cama=? AND rc.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
	
	/**
	 * Cadena de consulta de la fecha de ocupacion de la cama en estado reservada
	 */
	@SuppressWarnings("unused")
	private static final String srtCadenaConsultaFechaOcupacionReserva = "SELECT " +
			" CASE WHEN rc.fecha_ocupacion IS NULL THEN to_char(rc.fecha_modifica,'DD/MM/YYYY') ||" +
					"'@@@@@'||rc.hora_modifica ELSE to_char(rc.fecha_ocupacion,'DD/MM/YYYY') ||'@@@@@00:00'END " +
			"FROM reservar_cama rc " +
			"WHERE rc.codigo_cama=? AND rc.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
	
	
	
	private static final String consultaTodasLasCamasUltimoIngresoPostgres =
			"SELECT * FROM "+
			"( "+
				"SELECT "+ 
				  "cuentas.id || '@@@@@' || "+
				  "admisiones_urgencias.fecha_admision || '@@@@@' || "+
				  "admisiones_urgencias.hora_admision || '@@@@@' || "+
				  "cuentas.id_ingreso AS cuentafechahora, "+
				  "admisiones_urgencias.fecha_admision AS fecha, "+
				  "admisiones_urgencias.hora_admision AS hora "+
				"FROM "+
				  "manejopaciente.admisiones_urgencias, "+ 
				  "manejopaciente.cuentas "+
				"WHERE "+
				  "admisiones_urgencias.cuenta = cuentas.id AND "+
				  "admisiones_urgencias.cama_observacion = ? "+

				"UNION ALL "+

				"SELECT "+
				  "cuentas.id || '@@@@@' || "+
				  "admisiones_hospi.fecha_admision || '@@@@@' || "+
				  "admisiones_hospi.hora_admision || '@@@@@' || "+
				  "cuentas.id_ingreso AS cuentafechahora, "+
				  "admisiones_hospi.fecha_admision AS fecha, "+
				  "admisiones_hospi.hora_admision AS hora "+
				"FROM "+
				  "manejopaciente.admisiones_hospi, "+ 
				  "manejopaciente.cuentas "+
				"WHERE "+
				  "admisiones_hospi.cuenta = cuentas.id AND "+
				  "admisiones_hospi.cama = ? "+

				"UNION ALL "+

				"SELECT "+
				  "cuentas.id || '@@@@@' || "+
				  "traslado_cama.fecha_asignacion || '@@@@@' || "+
				  "traslado_cama.hora_asignacion || '@@@@@' || "+
				  "cuentas.id_ingreso AS cuentafechahora, "+
				  "traslado_cama.fecha_asignacion AS fecha, "+
				  "traslado_cama.hora_asignacion AS hora "+
				"FROM "+
				  "manejopaciente.traslado_cama, "+ 
				  "manejopaciente.cuentas "+
				"WHERE "+
				  "traslado_cama.cuenta = cuentas.id AND "+
				  "traslado_cama.codigo_nueva_cama = ? "+
			") AS subconsulta "+
			"ORDER BY to_timestamp(to_char(fecha, 'DD/MM/YYYY') || ' ' || to_char(hora, 'HH24:MI'), 'DD/MM/YYYY HH24:MI') DESC "+
			"LIMIT 1 ";
	
	private static final String consultaTodasLasCamasUltimoIngresoOracle =
			"SELECT * FROM "+
			"( "+
			 "SELECT * FROM "+
			  "( "+
			    "SELECT cuentas.id "+
			      "|| '@@@@@' "+
			      "|| admisiones_urgencias.fecha_admision "+
			      "|| '@@@@@' "+
			      "|| admisiones_urgencias.hora_admision "+
			      "|| '@@@@@' "+
			      "|| cuentas.id_ingreso               AS cuentafechahora, "+
			      "admisiones_urgencias.fecha_admision AS fecha, "+
			      "admisiones_urgencias.hora_admision  AS hora "+
			    "FROM manejopaciente.admisiones_urgencias, "+
			      "manejopaciente.cuentas "+
			    "WHERE admisiones_urgencias.cuenta         = cuentas.id "+
			    "AND admisiones_urgencias.cama_observacion = ? "+
			    
			    "UNION ALL "+
			    
			    "SELECT cuentas.id "+
			      "|| '@@@@@' "+
			      "|| admisiones_hospi.fecha_admision "+
			      "|| '@@@@@' "+
			      "|| admisiones_hospi.hora_admision "+
			      "|| '@@@@@' "+
			      "|| cuentas.id_ingreso           AS cuentafechahora, "+
			      "admisiones_hospi.fecha_admision AS fecha, "+
			      "admisiones_hospi.hora_admision  AS hora "+
			    "FROM manejopaciente.admisiones_hospi, "+
			      "manejopaciente.cuentas "+
			    "WHERE admisiones_hospi.cuenta = cuentas.id "+
			    "AND admisiones_hospi.cama     = ? "+
			    
			    "UNION ALL "+
				
				"SELECT cuentas.id "+
				"|| '@@@@@' "+
				"|| traslado_cama.fecha_asignacion "+
				"|| '@@@@@' "+
				"|| traslado_cama.hora_asignacion "+
				"|| '@@@@@' "+
				"|| cuentas.id_ingreso          AS cuentafechahora, "+
				"traslado_cama.fecha_asignacion AS fecha, "+
				"traslado_cama.hora_asignacion  AS hora "+
				"FROM manejopaciente.traslado_cama, "+
				"manejopaciente.cuentas "+
				"WHERE traslado_cama.cuenta          = cuentas.id "+
				"AND traslado_cama.codigo_nueva_cama = ? "+
			  ") "+
			  "ORDER BY to_date((to_char(fecha, 'DD/MM/YYYY') || ' ' || hora) , 'DD/MM/YYYY HH24:MI') DESC "+
			") "+
			"WHERE ROWNUM = 1 ";

			
			
	/*******************************************************************************************************
	 *SE PASO PARA EL POSTGRES Y EL ORACLE 
	 * 
	 
	private static final String strCadenaProcesoAutomatico = " INSERT INTO proceso_automatico_censo" +
				" (estadocama,cantidad,piso,centro_atencion,institucion, codigo) SELECT c.estado,count(*) ," +
				" h.piso, h.centro_atencion, c.institucion" +
			" FROM camas1 c " +
			" INNER JOIN habitaciones h ON (h.codigo = c.habitacion) group by c.institucion, h.centro_atencion, h.piso,c.estado";
	*************************************************************************************************************************/	
	/*----------------------------------------------------------------------------------------
	 *                              FIN DE ATRIBUTOS CENSO DE CAMAS
	 -----------------------------------------------------------------------------------------*/
	
	
	/*----------------------------------------------------------------------------------------
	 *                         DEFINICION DE INDICES DEL CENSO DE CAMAS
	 -----------------------------------------------------------------------------------------*/
	
	
	/**
	 * Vector String indices Mapa Censocamas
	 */
	private static String [] indicesCensoCamas = {"nombrehabitacion_","codigocama_","cama_","nombrepac_","identificacionpac_","diagnostico_","edadpac_",
												  "nombresexopac_","fecha_","diasestancia_","convenio_","nombretipohabitacion_","tipousuariocama_",
												  "sexocama_","restriccioncama_","tipoidentificacionpac_","hora_","codigocentrocosto_","estadocama_",
												  "codigopiso_","nombrepiso_","nombrecentrocosto_","nombreestadocama_","codigopersona_","color_","habitacioncama_",
												  "nombretipoidentificacionpac_","consecutivoingreso_","historiaclinica_","cuenta_","alerta_","viaingreso_",
												  "codpisomostrar_","codhabitmostar_"};
	
	
	
	/*----------------------------------------------------------------------------------------
	 *                      FIN DE DEFINICION DE INDICES DEL CENSO DE CAMAS
	 -----------------------------------------------------------------------------------------*/
	
	
	
	/*----------------------------------------------------------------------------------------
	 *                              METODOS CENSO DE CAMAS
	 -----------------------------------------------------------------------------------------*/
	
	
	/**
	 *Metodo que consulta informacion estadistica y lo inserta en una tabla para
	 *luego ser consultada por otra funcionalidad. 
	 */
	public static boolean corrrerProcesoAutomatico (String cadena)
	{
		Log4JManager.error("\n\n ************************************* PROCESO AUTOMATICO CENSO CAMAS ");
		Log4JManager.error("\n\n *** Cadena: " + cadena);

		Connection connection = null;
		PreparedStatementDecorator ps = null;
			
		boolean operacionTrue=false;
		try {
			connection = UtilidadBD.abrirConexion();
			ps = new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeUpdate() > 0)
				operacionTrue = true;
		} catch (SQLException e) {
			Log4JManager.error("\n\n *** problema ejecutando el proceso automatico de censo de camas"+e);
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
				if(connection != null) {
					UtilidadBD.closeConnection(connection);
				}
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - Connection: " + e);
			}
		}
		
		return operacionTrue;
	}
	
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas en
	 * estados diferentes a resercama. 
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public static String ConsultaFechaOcupacionOtrosEst(Connection connection, int cuenta, String sentenciaSQL)
	{
		String result="";
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		Log4JManager.info("\n \n *** entro a ConsultaFechaOcupacionOtrosEst con el valor de la cuenta"+ cuenta);
		
		try {
			Log4JManager.info("la cadena d econsulta es ==> "+sentenciaSQL);
		 	ps = new PreparedStatementDecorator(connection.prepareStatement(sentenciaSQL, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, cuenta);
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				result = rs.getString(1);
		} catch (SQLException e) {
			Log4JManager.error("\n\n *** problema consultando la fecha de ocupacion de la cama en otros estados "+e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResulsetDecorator: " + e);
			}
		}
		return result;
	}
	
	
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas reservadas
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public static String ConsultaFechaOcupacionReserva (Connection connection, int codigoCama, String sentenciaSQL)
	{
		String result="";
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try {
		 	ps =  new PreparedStatementDecorator(connection, sentenciaSQL);
			ps.setInt(1, codigoCama);
			Log4JManager.info(ps);
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				result = rs.getString(1);
		} catch (SQLException e) {
			Log4JManager.error("\n\n *** problema consultando la fecha de ocupacion de la cama reservada "+e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResulsetDecorator: " + e);
			}
		}	
		
		return result;
	}
	
	/**
	 * Metodo encargado de realizar el censo de camas
	 * @param connection
	 * @param parametros
	 * @param sqlCenso
	 * @return
	 */
	public static HashMap<?,?> consultaCenso (Connection connection, HashMap<?,?> parametros, String sqlCenso)
	{
		String restriccionesUrg = "";
		String restriccionesHos = "";
		String restriccionesNuncaUsadas = "";
		//Se agregan los parametros a la consulta
		if (parametros.containsKey("convenio"))
 		{
			restriccionesHos += "AND convenios.codigo = " + parametros.get("convenio")+" ";
			restriccionesHos += "AND camas1.estado in(1,2,3,5,6,7,8) ";
			
			restriccionesUrg += "AND convenios.codigo = " + parametros.get("convenio")+" ";
			restriccionesUrg += "AND camas1.estado in(1,2,3,5,6,7,8) ";
			
			if(!parametros.containsKey("estado")||!parametros.get("estado").equals("'"+ConstantesBD.codigoEstadoCamaOcupada+"'")){
				restriccionesHos += "AND ingresos.estado = 'ABI' ";
				restriccionesUrg += "AND ingresos.estado = 'ABI' ";
			}
			
			restriccionesNuncaUsadas += "AND camas1.estado NOT IN(0,2,3,4) "; 
		}
		if(parametros.containsKey("estado")&&parametros.get("estado").equals("'"+ConstantesBD.codigoEstadoCamaOcupada+"'")){
			restriccionesHos += "AND ingresos.estado = 'ABI' ";
			restriccionesHos += "AND traslado_cama.FECHA_FINALIZACION is null ";
			restriccionesHos += "AND traslado_cama.FECHA_FINALIZACION is null ";
			
			restriccionesUrg += "AND ingresos.estado = 'ABI' ";
			restriccionesUrg += "AND admisiones_urgencias.FECHA_EGRESO_OBSERVACION is null ";
			restriccionesUrg += "AND admisiones_urgencias.HORA_EGRESO_OBSERVACION is null ";
		}
		
		if (parametros.containsKey("centroatencion"))
		{
			restriccionesHos += "AND centros_costo.centro_atencion = " + parametros.get("centroatencion")+" ";
			restriccionesUrg += "AND centros_costo.centro_atencion = " + parametros.get("centroatencion")+" ";
			restriccionesNuncaUsadas += "AND centros_costo.centro_atencion = " + parametros.get("centroatencion")+" ";
		}
		if (parametros.containsKey("centrocosto"))
		{
			restriccionesHos += "AND camas1.centro_costo = " + parametros.get("centrocosto") + " ";
			restriccionesUrg += "AND camas1.centro_costo = " + parametros.get("centrocosto") + " ";
			restriccionesNuncaUsadas += "AND camas1.centro_costo = " + parametros.get("centrocosto") + " ";
		}
		if (parametros.containsKey("piso"))
		{
			restriccionesHos += "AND habitaciones.piso = " + parametros.get("piso") + " ";
			restriccionesUrg += "AND habitaciones.piso = " + parametros.get("piso") + " ";
			restriccionesNuncaUsadas += "AND habitaciones.piso = " + parametros.get("piso") + " ";
		}
		if (parametros.containsKey("estado"))
		{
			restriccionesHos += "AND camas1.estado IN("+parametros.get("estado").toString().replaceAll("'", "")+") ";
			restriccionesUrg += "AND camas1.estado IN("+parametros.get("estado").toString().replaceAll("'", "")+") ";
			restriccionesNuncaUsadas += "AND camas1.estado IN("+parametros.get("estado").toString().replaceAll("'", "")+") ";
		}
		if (UtilidadCadena.noEsVacio(parametros.get("incluirAsignableAdmin")+""))
		{
			if((parametros.get("incluirAsignableAdmin")+"").equals(ConstantesBD.acronimoNo))
			{
				restriccionesHos += "AND camas1.asignable_admision<>'"+ConstantesBD.acronimoSi+"' ";
				restriccionesUrg += "AND camas1.asignable_admision<>'"+ConstantesBD.acronimoSi+"' ";
				restriccionesNuncaUsadas += "AND camas1.asignable_admision<>'"+ConstantesBD.acronimoSi+"' ";
			}
		}
		
		//se agregan las restricciones a las sub-consultas de hospitalizacion y urgencias, respectivamente 
		String sql = sqlCenso.replaceAll("reemplasoRestriccionesHos", restriccionesHos).replaceAll("reemplasoRestriccionesUrg", restriccionesUrg).replaceAll("reemplasoRestriccionesNuncaUsadas", restriccionesNuncaUsadas);
		Log4JManager.info("consulta CensoCamas ==> " + sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String, Object> mapaRetorno = null;
		HashMap<String, Object> nuevoMapa = null;
		try
		{
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			TreeMap<String, String[]> mapa = new TreeMap<String, String[]>();
			HashMap<String, Boolean>haSidoAgregadoPaciente=new HashMap<String, Boolean>();
			
			//Se agrega la cama al mapa solo si esta no ha sido agregada antes ya que la consulta esta ordenada por fecha
			while(rs.next())
			{
				if(!mapa.containsKey(rs.getString("codigoCama"))&&!haSidoAgregadoPaciente.containsKey(rs.getString("idPaciente")))
				{
					mapa.put(rs.getString("codigoCama"), new String[]	{
																			rs.getString("piso"),
																			rs.getString("cama"),
																			rs.getString("codigoHabitacion"),
																			rs.getString("estado"),
																			rs.getString("nombrePaciente"),
																			rs.getString("idPaciente"),
																			rs.getString("ingreso"),
																			rs.getString("historiClinica"),
																			rs.getString("fechaNacimiento"),
																			rs.getString("sexo"),
																			rs.getString("fechaIngreso"),
																			rs.getString("convenio"),
																			rs.getString("colorCama"),
																			rs.getString("centroCosto"),
																			rs.getString("codigoEstado"),
																			rs.getString("idCuenta"),
																			rs.getString("codigopersona"),
																			rs.getString("viaIngreso"),
																			rs.getString("tipoIdentificacion"),
																			rs.getString("codigocentrocosto"),
																			rs.getString("codigoPiso")
																		}
							);
					if(!UtilidadTexto.isEmpty(rs.getString("idPaciente"))){
						haSidoAgregadoPaciente.put(rs.getString("idPaciente"), true);
					}
				}
			}
			Object[] keys = mapa.keySet().toArray();
			mapaRetorno = new HashMap<String, Object>();
			int disponible = 0;
			int ocupada = 0;
			int desinfeccion = 0;
			int mantenimiento = 0;
			int fueradeservicio = 0;
			int reservada = 0;
			int remitir = 0;
			int trasladar = 0;
			int consalida = 0;
			int total = 0;
			LinkedHashSet<String> arregloRompimiento = new LinkedHashSet<String>();
			String fechaActual = UtilidadFecha.getFechaActual();
			//Se recorre el mapa para construir el mapaRetorno
			for (int i = 0; i < keys.length; i++)
			{
				//Solo NO se agregan datos de pacientes cuando el estado es disponible, desinfeccion, mantenimiento y fuera de servicio
				if(!mapa.get(keys[i])[14].equals("0") && !mapa.get(keys[i])[14].equals("2") && !mapa.get(keys[i])[14].equals("3") && !mapa.get(keys[i])[14].equals("4"))
				{
					mapaRetorno.put("codigocama_"+i,        	keys[i]+"");
					mapaRetorno.put("nombrepiso_"+i, 			mapa.get(keys[i])[0]);
					mapaRetorno.put("cama_"+i, 					mapa.get(keys[i])[1]);
					mapaRetorno.put("codhabitmostar_"+i,		mapa.get(keys[i])[2]);
					mapaRetorno.put("nombreestadocama_"+i, 		mapa.get(keys[i])[3]);
					mapaRetorno.put("nombrepac_"+i, 			mapa.get(keys[i])[4]);
					mapaRetorno.put("identificacionpac_"+i, 	mapa.get(keys[i])[5]);
					mapaRetorno.put("consecutivoingreso_"+i,	mapa.get(keys[i])[6]);
					mapaRetorno.put("historiaclinica_"+i, 		mapa.get(keys[i])[7]);
					mapaRetorno.put("edadpac_"+i, 				UtilidadFecha.calcularEdadDetallada(UtilidadFecha.conversionFormatoFechaAAp(mapa.get(keys[i])[8]), UtilidadFecha.conversionFormatoFechaAAp(fechaActual)));
					mapaRetorno.put("nombresexopac_"+i, 		mapa.get(keys[i])[9]);
					mapaRetorno.put("fecha_"+i, 				mapa.get(keys[i])[10].split(" ")[0]);
					mapaRetorno.put("hora_"+i,	 				mapa.get(keys[i])[10].split(" ")[1]);
					mapaRetorno.put("diasestancia_"+i,      	UtilidadFecha.numeroDiasEntreFechas(mapa.get(keys[i])[10].split(" ")[0], UtilidadFecha.conversionFormatoFechaAAp(fechaActual)));
					mapaRetorno.put("convenio_"+i, 				mapa.get(keys[i])[11]);
					mapaRetorno.put("color_"+i,	 				mapa.get(keys[i])[12]);
					mapaRetorno.put("nombrecentrocosto_"+i,		mapa.get(keys[i])[13]);
					mapaRetorno.put("estadocama_"+i,			mapa.get(keys[i])[14]);
					
					IDiagnosticosMundo  diagnosticosMundo = ManejoPacienteFabricaMundo.crearDiagnosticosMundo();
					DtoDiagnostico dtoDiagnostico = new DtoDiagnostico();
					dtoDiagnostico.setIdCuenta(Integer.valueOf(mapa.get(keys[i])[15]+""));
					dtoDiagnostico = diagnosticosMundo.ultimoDiagnostico(dtoDiagnostico);
					mapaRetorno.put("diagnostico_"+i,			dtoDiagnostico.getNombreCompletoDiagnostico());
					
					mapaRetorno.put("cuenta_"+i,				mapa.get(keys[i])[15]);
					mapaRetorno.put("codigopersona_"+i,			mapa.get(keys[i])[16]);
					mapaRetorno.put("viaingreso_"+i,			mapa.get(keys[i])[17]);
					mapaRetorno.put("tipoidentificacionpac_"+i,	mapa.get(keys[i])[18]);
					mapaRetorno.put("codigocentrocosto_"+i,		mapa.get(keys[i])[19]);
					mapaRetorno.put("codigopiso_"+i,			mapa.get(keys[i])[20]);
				}
				else
				{
					mapaRetorno.put("codigocama_"+i,        	keys[i]+"");
					mapaRetorno.put("nombrepiso_"+i, 			mapa.get(keys[i])[0]);
					mapaRetorno.put("cama_"+i, 					mapa.get(keys[i])[1]);
					mapaRetorno.put("codhabitmostar_"+i,		mapa.get(keys[i])[2]);
					mapaRetorno.put("nombreestadocama_"+i, 		mapa.get(keys[i])[3]);
					mapaRetorno.put("color_"+i,	 				mapa.get(keys[i])[12]);
					mapaRetorno.put("nombrecentrocosto_"+i,		mapa.get(keys[i])[13]);
					mapaRetorno.put("estadocama_"+i,			mapa.get(keys[i])[14]);
					mapaRetorno.put("codigocentrocosto_"+i,		mapa.get(keys[i])[19]);
					mapaRetorno.put("codigopiso_"+i,			mapa.get(keys[i])[20]);
				}
				
				//Se agrega el tipo de rompimiento para despues ordenar el mapa
				if (parametros.containsKey("tiporompimiento") && !parametros.get("tiporompimiento").equals(""))
				{
					if (parametros.get("tiporompimiento").equals("codigocentrocosto"))
					{
						arregloRompimiento.add(mapaRetorno.get("codigocentrocosto_"+i)+"");
					}
					else if (parametros.get("tiporompimiento").equals("codigopiso"))
					{
						arregloRompimiento.add(mapaRetorno.get("codigopiso_"+i)+"");
					}
					else if (parametros.get("tiporompimiento").equals("estadocama"))
					{
						arregloRompimiento.add(mapaRetorno.get("nombreestadocama_"+i)+"");
					}
				}
				
				//Se realiza el conteo
				if(mapa.get(keys[i])[14].equals("0")){
					disponible++;
				}
				if(mapa.get(keys[i])[14].equals("1")){
					ocupada++;
				}
				if(mapa.get(keys[i])[14].equals("2")){
					desinfeccion++;
				}
				if(mapa.get(keys[i])[14].equals("3")){
					mantenimiento++;
				}
				if(mapa.get(keys[i])[14].equals("4")){
					fueradeservicio++;
				}
				if(mapa.get(keys[i])[14].equals("5")){
					reservada++;
				}
				if(mapa.get(keys[i])[14].equals("6")){
					trasladar++;
				}
				if(mapa.get(keys[i])[14].equals("7")){
					remitir++;
				}
				if(mapa.get(keys[i])[14].equals("8")){
					consalida++;
				}
			}
			
			//Se ordena el mapa segun el tipo de rompimiento seleccionado
			Object[] arregloRompimientoOrdenar = arregloRompimiento.toArray();
			Arrays.sort(arregloRompimientoOrdenar);
			nuevoMapa = new HashMap<String, Object>();
			nuevoMapa.put("INDICES_MAPA", indicesCensoCamas);
			int indiceActual = 0;
			if (parametros.containsKey("tiporompimiento") && !parametros.get("tiporompimiento").equals(""))
			{
				if (parametros.get("tiporompimiento").equals("codigocentrocosto"))
				{
					for (int j = 0; j < arregloRompimientoOrdenar.length; j++)
					{	
						for (int k = 0; k < keys.length; k++)
						{
							if(mapaRetorno.get("codigocentrocosto_"+k)!=null && mapaRetorno.get("codigocentrocosto_"+k).toString().equals(arregloRompimientoOrdenar[j].toString()))
							{
								indiceActual = agregarPorIndiceYborrar(mapaRetorno, nuevoMapa, indiceActual, k);
							}
						}
					}
				}
				else if (parametros.get("tiporompimiento").equals("codigopiso"))
				{
					for (int j = 0; j < arregloRompimientoOrdenar.length; j++)
					{	
						for (int k = 0; k < keys.length; k++)
						{
							if(mapaRetorno.get("codigopiso_"+k)!=null && mapaRetorno.get("codigopiso_"+k).toString().equals(arregloRompimientoOrdenar[j].toString()))
							{
								indiceActual = agregarPorIndiceYborrar(mapaRetorno, nuevoMapa, indiceActual, k);
							}
						}
					}
				}
				else if (parametros.get("tiporompimiento").equals("estadocama"))
				{
					for (int j = 0; j < arregloRompimientoOrdenar.length; j++)
					{	
						for (int k = 0; k < keys.length; k++)
						{
							if(mapaRetorno.get("nombreestadocama_"+k)!=null && mapaRetorno.get("nombreestadocama_"+k).toString().equals(arregloRompimientoOrdenar[j].toString()))
							{
								indiceActual = agregarPorIndiceYborrar(mapaRetorno, nuevoMapa, indiceActual, k);
							}
						}
					}
				}
			}
			
			//Se ingresan las cantidades resultantes
			nuevoMapa.put("disponible",disponible + "");
			nuevoMapa.put("ocupada",ocupada + "");
			nuevoMapa.put("desinfeccion",desinfeccion + "");
			nuevoMapa.put("mantenimiento", mantenimiento + "");
			nuevoMapa.put("fueradeservicio", fueradeservicio + "");
			nuevoMapa.put("reservada", reservada + "");
			nuevoMapa.put("remitir", remitir + "");
			nuevoMapa.put("trasladar", trasladar + "");
			nuevoMapa.put("consalida", consalida + "");
			nuevoMapa.put("total", total + "");
			nuevoMapa.put("numRegistros", keys.length + "");
		}
		catch (Exception e)
		{
			Log4JManager.error("Error consultando el censo de camas: ", e);
		}
		finally
		{
			try
			{
				if(ps != null)
				{
					ps.close();
				}
				if(rs != null)
				{
					rs.close();
				}
			}
			catch (Exception e2)
			{
				Log4JManager.error(e2);
			}
		}
		parametros = null;
		return nuevoMapa;
	}
	
	private static int agregarPorIndiceYborrar(HashMap<String, Object> mapaRetorno, HashMap<String, Object> nuevoMapa, int i, int j)
	{			
		for (int k = 0; k < indicesCensoCamas.length; k++)
		{
			if(mapaRetorno.get(indicesCensoCamas[k]+j)!=null)
			{
				nuevoMapa.put(indicesCensoCamas[k]+i, mapaRetorno.get(indicesCensoCamas[k]+j));
				mapaRetorno.remove(indicesCensoCamas[k]+j);
			}
		}
		return ++i;
	}

	/**
	 * Metodo Principal encargado de consultar el censo de camas
	 * el HashMap parametros puede contener los siguentes parametros
	 * institucion --> Requerido
	 * convenio --> Opcional
	 * centroatencion --> Opcional
	 * centrocosto --> Opcional
	 * piso --> Opcional
	 * estado --> Opcional
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @param strCadenaConsultaCuentaXCama 
	 * @param strCadenaConsultaDiagnosticoXCuenta 
	 * @return mapa
	 * 
	 * -------------------------------------------------------
	 * 			KEY'S QUE CONTIENE EL HASHMAP CENSOCAMAS
	 * --------------------------------------------------------
	 * El mapa de salida contiene los siguientes key's
	 * nombrehabitacion, cama, estadocama, nombretipohabitacion,
	 * habitacioncama, tipousuariocama, sexocama, restriccioncama
	 * cuenta, fecha, diasestancia, hora, diagnostico, convenio,
	 * nombrepac, identificacionpac, tipoidentificacionpac, 
	 * sexopac, fechanacimientopac, edadpac, restriccioncama,viaingreso
	 *  
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCensoBorrar (Connection connection,HashMap parametros, String strCadenaConsultaCuentaXCama, String strCadenaConsultaDiagnosticoXCuenta)
	{
		HashMap mapa = new HashMap();
		HashMap tmp = new HashMap ();
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		Log4JManager.info("\n\n***********************ENTRO A CENSAR CAMAS********************");
		Log4JManager.info("\n ****PARAMETROS ******");
		Log4JManager.info(parametros);
		Log4JManager.info("\n ****FIN PARAMETROS ******\n\n");
		String consulta = strCadenaConsultaCensoCamas,	where=" WHERE c.institucion=? ";
		
		try {

			if (parametros.containsKey("convenio"))
     		{
				where+=" AND manejopaciente.getconveniocamallena(c.codigo,c.estado)="+Utilidades.convertirAEntero(parametros.get("convenio").toString());
//				consulta += " LEFT JOIN admisiones_urgencias au ON (c.codigo= au.cama_observacion)" +
//						" LEFT JOIN traslado_cama tc ON (c.codigo=tc.codigo_nueva_cama)" +
//						" LEFT JOIN cuentas cue ON (au.cuenta= cue.id)" +
//						" LEFT JOIN sub_cuentas sub ON (cue.codigo_paciente= sub.codigo_paciente)";
//				
//				where += " AND ( (tc.convenio="+Utilidades.convertirAEntero(parametros.get("convenio").toString())+"AND tc.fecha_finalizacion IS NULL AND tc.hora_finalizacion  IS NULL)"+" OR (sub.convenio="+Utilidades.convertirAEntero(parametros.get("convenio").toString())+
//						" AND au.fecha_egreso_observacion IS NULL AND au.hora_egreso_observacion  IS NULL ))";
				
			}
			if (parametros.containsKey("centroatencion")){
				consulta+=" INNER JOIN centros_costo cc ON (cc.codigo=c.centro_costo) ";
				where+=" AND cc.centro_atencion="+Utilidades.convertirAEntero(parametros.get("centroatencion").toString());
			}
			if (parametros.containsKey("centrocosto"))
				where+=" AND c.centro_costo="+Utilidades.convertirAEntero(parametros.get("centrocosto").toString());
			if (parametros.containsKey("piso"))
				where+=" AND h.piso="+Utilidades.convertirAEntero(parametros.get("piso").toString());
			if (parametros.containsKey("estado"))
				where+=" AND c.estado IN("+parametros.get("estado")+")";

			if (UtilidadCadena.noEsVacio(parametros.get("incluirAsignableAdmin")+""))
				if((parametros.get("incluirAsignableAdmin")+"").equals(ConstantesBD.acronimoNo))
					where+=" AND c.asignable_admision<>'"+ConstantesBD.acronimoSi+"'";

			consulta +=where;
			if (parametros.containsKey("tiporompimiento"))
				if (!parametros.get("tiporompimiento").equals(""))
					if (parametros.get("tiporompimiento").equals("codigocentrocosto")){
						/**
						 * Modificación de codigo según incidencia 3124
						 * Autor: Alejandro Aguirre Luna
						 * Usuario: aleagulu
						 * Fecha: 15/01/2013
						 * Descripción: La consulta debe ordenar por numero 
						 * 				de cama y no por centro costo   
						 */
						consulta +=" ORDER BY c.numero_cama";
						//consulta +=" ORDER BY c.centro_costo";
					}
					else
						if (parametros.get("tiporompimiento").equals("codigopiso"))
							consulta +=" ORDER BY h.piso";
						else
							if (parametros.get("tiporompimiento").equals("estadocama"))
								consulta +=" ORDER BY c.estado";

			Log4JManager.info("consulta CensoCamas ==> "+consulta);

			ps = new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			rs = new ResultSetDecorator(ps.executeQuery());
			mapa=UtilidadBD.cargarValueObject(rs,true,true);
			
			//Log4JManager.info("\n\n respuesta de la consulta de camas "+mapa);
			//se consulta las Cuentas,Fechas de Ingreso,Horas de Ingreso,Diagnostico y Convenio de las camas en estado ocupado
			tmp=consulta_CuentaXCama_DiagnosticoXCuenta_ConvenioXCuenta_PacienteXCuenta(connection, mapa, strCadenaConsultaCuentaXCama, strCadenaConsultaDiagnosticoXCuenta);	
			//	Log4JManager.info("el valor del tmp es"+tmp);
			//se itera el hashmap mapa para cargar las cuentas, fecha y hora en el.
			for (int i=0;Utilidades.convertirAEntero(mapa.get("numRegistros").toString())>i;i++)
			{
				//Log4JManager.info("el valor del tmp es"+tmp);
				/*en las siguientes lineas se carga el mapa original de las camas con
				 *la informacion siguiente informacion.
				 *--Cuenta
				 *--Fecha
				 *--Hora
				 *--Diagnostico
				 *--Convenio
				 *--Alerta
				 */
				//	Log4JManager.info("el valor del i es "+i);
				
				if(tmp.containsKey("alerta_"+i))
					mapa.put("alerta_"+i, 1);
				
				if (tmp.containsKey("cuenta_"+i))
					mapa.put("cuenta_"+i, tmp.get("cuenta_"+i));
				else
					mapa.put("cuenta_"+i, "");
				if (tmp.containsKey("fecha_"+i))
				{
					mapa.put("fecha_"+i, tmp.get("fecha_"+i));

					//se calcula los dias de estancia del paciente 
					if (tmp.get("fecha_"+i) == null )
						mapa.put("diasestancia_"+i,"");
					else
						mapa.put("diasestancia_"+i, UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(tmp.get("fecha_"+i).toString()), UtilidadFecha.getFechaActual()));

				}
				else
				{
					mapa.put("fecha_"+i, "");
					mapa.put("diasestancia_"+i,"");
				}
				if (tmp.containsKey("hora_"+i))
					mapa.put("hora_"+i, tmp.get("hora_"+i));
				else
					mapa.put("hora_"+i, "");
				if (tmp.containsKey("diagnostico_"+i))
					mapa.put("diagnostico_"+i, tmp.get("diagnostico_"+i));
				else
					mapa.put("diagnostico_"+i, "");

				if (tmp.containsKey("convenio_"+i))
					mapa.put("convenio_"+i, tmp.get("convenio_"+i));
				else
					mapa.put("convenio_"+i, "");

				if (tmp.containsKey("consecutivoingreso_"+i))
					mapa.put("consecutivoingreso_"+i, tmp.get("consecutivoingreso_"+i));
				else
					mapa.put("consecutivoingreso_"+i, "");

				if (tmp.containsKey("viaingreso_"+i))
					mapa.put("viaingreso_"+i, tmp.get("viaingreso_"+i));
				else
					mapa.put("viaingreso_"+i, "");

				if (tmp.containsKey("nombrepac_"+i))
				{
					if (tmp.get("nombrepac_"+i) == null)
						mapa.put("nombrepac_"+i, "");
					else
						mapa.put("nombrepac_"+i, tmp.get("nombrepac_"+i));

				}
				else
					mapa.put("nombrepac_"+i, "");
				if (tmp.containsKey("identificacionpac_"+i))
				{	
					if (tmp.get("identificacionpac_"+i) == null)
						mapa.put("identificacionpac_"+i, "");
					else
						mapa.put("identificacionpac_"+i, tmp.get("identificacionpac_"+i));
				}
				else
					mapa.put("identificacionpac_"+i, "");
				if (tmp.containsKey("tipoidentificacionpac_"+i))
				{
					if(tmp.get("tipoidentificacionpac_"+i) == null)
						mapa.put("tipoidentificacionpac_"+i, "");
					else
						mapa.put("tipoidentificacionpac_"+i, tmp.get("tipoidentificacionpac_"+i));

					if (tmp.get("nombretipoidentificacionpac_"+i) == null)
						mapa.put("nombretipoidentificacionpac_"+i, "");
					else
						mapa.put("nombretipoidentificacionpac_"+i, tmp.get("nombretipoidentificacionpac_"+i));

				}
				else
					mapa.put("tipoidentificacionpac_"+i, "");
				if (tmp.containsKey("nombresexopac_"+i))
				{	

					if(tmp.get("nombresexopac_"+i) == null)
						mapa.put("nombresexopac_"+i, "");
					else
						mapa.put("nombresexopac_"+i,tmp.get("nombresexopac_"+i));
				}	
				else
					mapa.put("nombresexopac_"+i,"");
				if (tmp.containsKey("sexopac_"+i))
				{	
					if(tmp.get("sexopac_"+i) == null)
						mapa.put("sexopac_"+i, "");
					else
						mapa.put("sexopac_"+i, tmp.get("sexopac_"+i));
				}
				else
					mapa.put("sexopac_"+i, "");
				if (tmp.containsKey("fechanacimientopac_"+i))
				{
					mapa.put("fechanacimientopac_"+i, tmp.get("fechanacimientopac_"+i));

					//se calcula la edad del paciente
					if (tmp.get("fechanacimientopac_"+i) == null)
						mapa.put("edadpac_"+i,"");
					else
						mapa.put("edadpac_"+i, UtilidadFecha.calcularEdadDetallada(UtilidadFecha.conversionFormatoFechaAAp(tmp.get("fechanacimientopac_"+i).toString()), UtilidadFecha.getFechaActual().toString()));

				}
				else
				{
					mapa.put("fechanacimientopac_"+i, "");
					mapa.put("edadpac_"+i,"");
				}
				if (tmp.containsKey("codigopersona_"+i))
					mapa.put("codigopersona_"+i, tmp.get("codigopersona_"+i));
				else
					mapa.put("codigopersona_"+i, "");

				if (tmp.containsKey("historiaclinica_"+i))
					mapa.put("historiaclinica_"+i, tmp.get("historiaclinica_"+i));
				else
					mapa.put("historiaclinica_"+i, "");

				if (tmp.containsKey("observaciones_"+i))
					mapa.put("observaciones_"+i, tmp.get("observaciones_"+i));
				else
					mapa.put("observaciones_"+i, "");

				//aqui se carga la cantidad de camas en cada estado.
				if (tmp.containsKey("disponible"))
					mapa.put("disponible", tmp.get("disponible"));
				else
					mapa.put("disponible", 0);
				if (tmp.containsKey("ocupada"))
					mapa.put("ocupada", tmp.get("ocupada"));
				else
					mapa.put("ocupada", 0);
				if (tmp.containsKey("desinfeccion"))
					mapa.put("desinfeccion", tmp.get("desinfeccion"));
				else 
					mapa.put("desinfeccion", 0);
				if (tmp.containsKey("mantenimiento"))
					mapa.put("mantenimiento", tmp.get("mantenimiento"));
				else
					mapa.put("mantenimiento", 0);
				if (tmp.containsKey("fueradeservicio"))
					mapa.put("fueradeservicio", tmp.get("fueradeservicio"));
				else
					mapa.put("fueradeservicio",0);
				if (tmp.containsKey("reservada"))
					mapa.put("reservada", tmp.get("reservada"));
				else
					mapa.put("reservada", 0);
				if (tmp.containsKey("trasladar"))
					mapa.put("trasladar", tmp.get("trasladar"));
				else
					mapa.put("trasladar", 0);
				if (tmp.containsKey("remitir"))
					mapa.put("remitir", tmp.get("remitir"));
				else
					mapa.put("remitir", 0);
				if (tmp.containsKey("consalida"))
					mapa.put("consalida", tmp.get("consalida"));
				else
					mapa.put("consalida", 0);

				if (tmp.containsKey("total"))
					mapa.put("total", tmp.get("total"));
				else
					mapa.put("total", 0);
			}
			//	Log4JManager.info("El valor antes de restriccion"+mapa);
			mapa=ConsultaRestriccion(mapa);
			//Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nEl valor del censo final es"+mapa);	

			mapa.put("INDICES_MAPA", indicesCensoCamas);
		} catch (SQLException e) {
			Log4JManager.error("Error consultando censo: " + e.getMessage() + " " + e.getCause());
		} catch (Exception e) {
			Log4JManager.error("Error consultando censo: " + e.getMessage() + " " + e.getCause());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResulsetDecorator: " + e);
			}
		}	

		return mapa;
	}
	
	
	
	
	/**
	 * Metodo encargado de consultar la Cuenta, Diagnostico, 
	 * Convenio y el paciete de la cama que se encuentra ocupada;
	 * ademas si la cama se encuentra en estado reservada, se consultan
	 * los datos del paciente que tiene la cama reservada.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @param strCadenaConsultaCuentaXCama 
	 * @param strCadenaConsultaDiagnosticoXCuenta 
	 * @return mapa
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public static HashMap consulta_CuentaXCama_DiagnosticoXCuenta_ConvenioXCuenta_PacienteXCuenta (Connection connection, HashMap parametros, String strCadenaConsultaCuentaXCama, String strCadenaConsultaDiagnosticoXCuenta)
	{
		//Log4JManager.info("entre a consulta_CuentaXCama_DiagnosticoXCuenta_ConvenioXCuenta_PacienteXCuenta");
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		HashMap mapa = new HashMap ();
		HashMap tmp = new HashMap ();
		mapa.put("numRegistros", 0);
		int ocu=0,dis=0,des=0,man=0,fdc=0,res=0, ppr=0,ppt=0, cs=0,total=0;
		
		int numRegistros=Utilidades.convertirAEntero(parametros.get("numRegistros").toString());
		total=numRegistros;

		try {

			String consultaCuenta=strCadenaConsultaCuentaXCama,consultaDiagnostico=strCadenaConsultaDiagnosticoXCuenta,
			consultaConvenio=strCadenaConsultaConvenioXCuenta,consultaPaciente=strCadenaConsultaPaciente,
			consultaPacienteReserva=strCadenaConsultaPacienteReserva,datos;

			/* Busqueda de diagnosticos por cuenta para el censo de camas. MT 391 */
			IDiagnosticosMundo  diagnosticosMundo = ManejoPacienteFabricaMundo.crearDiagnosticosMundo();
			DtoDiagnostico dtoDiagnostico = new DtoDiagnostico();

			String [] datostmp;
			//se recorre el Hashmap de camas
			for (int i=0;numRegistros>i;i++)
			{
				//se pregunta solamente por las camas con el estado de ocupado
				//portal motivo se pregunta si es igual a uno.

				/*------------------------------------------------------------
				 * 						TABLA DE ESTADOS
				 * -----------------------------------------------------------
				 *  codigo |      nombre
			 --------+-------------------
			       0 | Disponible
			       1 | Ocupada
			       2 | Desinfección
			       3 | Mantenimiento
			       4 | Fuera de Servicio
			       5 | Reservada
				 */
				if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i)+"")==ConstantesBD.codigoEstadoCamaOcupada || Utilidades.convertirAEntero(parametros.get("estadocama_"+i)+"")==ConstantesBD.codigoEstadoCamaPendientePorRemitir || Utilidades.convertirAEntero(parametros.get("estadocama_"+i)+"")==ConstantesBD.codigoEstadoCamaPendientePorTrasladar || Utilidades.convertirAEntero(parametros.get("estadocama_"+i)+"")==ConstantesBD.codigoEstadoCamaConSalida)
				{
					// para identificar la cantidad de camas en estos estados. 
					if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaOcupada)
						ocu++;
					else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaPendientePorRemitir)
						ppr++;
					else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaPendientePorTrasladar)
						ppt++;
					else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaConSalida)
						cs++;
					/*---------------------------------------------------------------------------------------
					 * 			EN ESTE BLOQUE DE TRY-CATCH SE CONSULTA LA CUENTA DE LA CAMA OCUPADA
				 ---------------------------------------------------------------------------------------*/

//					try
//					{
//						ps =  new PreparedStatementDecorator(connection.prepareStatement(consultaCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
//						ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigocama_"+i).toString()));
//						rs = new ResultSetDecorator(ps.executeQuery());
//						tmp = UtilidadBD.cargarValueObject(rs,false,true);
//					}
//					catch (Exception e)
//					{
//						Log4JManager.error(e);
//					}
					
					// MT-4734 y MT-3189
					try
					{
//						if (tmp.get("cuentafechahora")==null || (tmp.get("cuentafechahora")+"").equals(""))
//						{
							if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
								ps = new PreparedStatementDecorator(connection.prepareStatement(consultaTodasLasCamasUltimoIngresoPostgres,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							else
								ps = new PreparedStatementDecorator(connection.prepareStatement(consultaTodasLasCamasUltimoIngresoOracle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigocama_"+i).toString()));
							ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigocama_"+i).toString()));
							ps.setInt(3, Utilidades.convertirAEntero(parametros.get("codigocama_"+i).toString()));
							rs = new ResultSetDecorator(ps.executeQuery());
							while(rs.next())
							{
								tmp.put("cuentafechahora", rs.getString(1));
							}
//							mapa.put("alerta_"+i, 1);
//						}						
					}
					catch (Exception e)
					{
						Log4JManager.error(e);
					}
					// FIN MT-4734 y MT-3189

					if (tmp.get("cuentafechahora")!=null && !(tmp.get("cuentafechahora")+"").equals(""))
					{
						//Log4JManager.info("\n\ncuentas para las camas ocupadas"+tmp+" \n\n");
						//en este punto la variable datos contiene la siguiente forma numero_de_cuenta@@@@@fecha@@@@@hora
						//Log4JManager.info("Estos es para la cama"+Utilidades.convertirAEntero(parametros.get("codigocama_"+i).toString()));
						datos=tmp.get("cuentafechahora").toString();
						// Log4JManager.info("estoy en el String []"+datos);
						//se separa lavariable datos en:=> cuenta,fecha,hora


						datostmp=datos.split(ConstantesBD.separadorSplit);
						//se almacena en el HashMap la cuenta,fecha y hora.
						mapa.put("cuenta_"+i, datostmp[0]);
						mapa.put("fecha_"+i, datostmp[1]);
						mapa.put("hora_"+i, datostmp[2]);
						mapa.put("ingreso_"+i, datostmp[3]);


						// MT 391 - Cristhian Murillo
						dtoDiagnostico = new DtoDiagnostico();
						try 
						{
							dtoDiagnostico.setIdCuenta(Integer.parseInt(mapa.get("cuenta_"+i).toString()));

							UtilidadTransaccion.getTransaccion().begin();
							dtoDiagnostico = diagnosticosMundo.ultimoDiagnostico(dtoDiagnostico);
							UtilidadTransaccion.getTransaccion().commit();

						} catch (Exception e) {
							Log4JManager.info("No se pudo consultar el último diagnostico");
						}

						mapa.put("diagnostico_"+i, dtoDiagnostico.getNombreCompletoDiagnostico());
						/*-------------------------------------------------------------------------------------------------
						 * 		EN ESTE BLOQUE DE TRY-CATCH SE CONSULTA EL CONVENIO DE LA CAMA EN UNO DE ESTOS 4 ESTADOS
						 * --OCUPADO
						 * --PENDIENTE POR TRANSLADAR
						 * --PENDIENTE POR REMITIR
						 * --CON SALIDA
							  -------------------------------------------------------------------------------------------------*/


						//Log4JManager.info("\n consulta del convenio --> "+consultaConvenio+"  cuenta -->"+mapa.get("cuenta_"+i));
						ps =  new PreparedStatementDecorator(connection.prepareStatement(consultaConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, Utilidades.convertirAEntero(mapa.get("cuenta_"+i).toString()));
						rs = new ResultSetDecorator(ps.executeQuery());
						tmp = UtilidadBD.cargarValueObject(rs,false,false);

						//Log4JManager.info("\n  *************> convenio --> "+tmp.get("convenio"));
						mapa.put("convenio_"+i, tmp.get("convenio"));
						mapa.put("consecutivoingreso_"+i, tmp.get("consecutivoingreso"));
						mapa.put("viaingreso_"+i, tmp.get("viaingreso"));

						//Log4JManager.info("mapa despues de consulta Convenio ==> "+mapa);
						/*--------------------------------------------------------------------------------------------------------------------
						 *EN ESTE BLOQUE DE TRY-CATCH SE CONSULTA LOS DATOS DEL PACIENTE QUE SE ENCUENTRA EN LA CAMA EN UNO DE ESTOS 4 ESTADOS
						 * --OCUPADO
						 * --PENDIENTE POR TRANSLADAR
						 * --PENDIENTE POR REMITIR
						 * --CON SALIDA
										 ---------------------------------------------------------------------------------------------------------------------*/
						// Log4JManager.info("consulta ==> "+consultaPaciente);


						ps =  new PreparedStatementDecorator(connection.prepareStatement(consultaPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, Utilidades.convertirAEntero(mapa.get("cuenta_"+i).toString()));
						rs = new ResultSetDecorator(ps.executeQuery());
						tmp = UtilidadBD.cargarValueObject(rs,false,false);

						mapa.put("nombrepac_"+i, tmp.get("nombrepac"));
						mapa.put("identificacionpac_"+i, tmp.get("identificacionpac"));
						mapa.put("tipoidentificacionpac_"+i, tmp.get("tipoidentificacionpac"));
						mapa.put("sexopac_"+i, tmp.get("sexopac"));
						mapa.put("nombresexopac_"+i, tmp.get("nombresexopac"));
						mapa.put("fechanacimientopac_"+i, tmp.get("fechanacimientopac"));
						mapa.put("codigopersona_"+i, tmp.get("codigopersona"));
						mapa.put("nombretipoidentificacionpac_"+i, tmp.get("nombretipoidentificacionpac"));
						mapa.put("historiaclinica_"+i, tmp.get("historiaclinica"));


						// Log4JManager.info("mapa despues de consulta paciente ==> "+mapa);

						//se aumenta numRegistros en 1.
						mapa.put("numRegistros", Utilidades.convertirAEntero(mapa.get("numRegistros").toString())+1);

					}
					else
					{
						mapa.put("alerta_"+i, 1);
						Log4JManager.info("\n \n Problema con la cama ---- "+parametros.get("codigocama_"+i)+" ---- Esta ocupada, pero tiene cuenta cerrada.");
					}

				}
				//aqui se verifica si el estado de la cama es reservada.
				else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaReservada)
				{
					res++;
					/*----------------------------------------------------------------------------------------------------
					 *EN ESTE BLOQUE DE TRY-CATCH SE CONSULTA LOS DATOS DEL PACIENTE QUE SE ENCUENTRA EN LA CAMA RESERVADA
				 -----------------------------------------------------------------------------------------------------*/
					//	Log4JManager.info("consulta Paciente Reserva ==> "+consultaPacienteReserva);
					ps =  new PreparedStatementDecorator(connection.prepareStatement(consultaPacienteReserva,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigocama_"+i).toString()));
					rs = new ResultSetDecorator(ps.executeQuery());
					tmp = UtilidadBD.cargarValueObject(rs,false,false);

					mapa.put("nombrepac_"+i, tmp.get("nombrepac"));
					mapa.put("identificacionpac_"+i, tmp.get("identificacionpac"));
					mapa.put("tipoidentificacionpac_"+i, tmp.get("tipoidentificacionpac"));
					mapa.put("sexopac_"+i, tmp.get("sexopac"));
					mapa.put("nombresexopac_"+i, tmp.get("nombresexopac"));
					mapa.put("fechanacimientopac_"+i, tmp.get("fechanacimientopac"));
					mapa.put("codigopersona_"+i, tmp.get("codigopersona"));
					mapa.put("nombretipoidentificacionpac_"+i, tmp.get("nombretipoidentificacionpac"));
					mapa.put("historiaclinica_"+i, tmp.get("historiaclinica"));
					mapa.put("observaciones_"+i, tmp.get("observaciones"));

					//Log4JManager.info("mapa despues de consulta paciente reservada ==> "+mapa);
					//se aumenta numRegistros en 1. 
					mapa.put("numRegistros", Utilidades.convertirAEntero(mapa.get("numRegistros").toString())+1);

				} else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaDisponible)
					dis++;
				else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaDesinfeccion)
					des++;
				else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaMantenimiento)
					man++;
				else if (Utilidades.convertirAEntero(parametros.get("estadocama_"+i).toString())==ConstantesBD.codigoEstadoCamaFueraServicio)
					fdc++;
			}

			//Log4JManager.info("el mapa datos paciente convenio y otros"+mapa);

			//aqui se ingresan la cantidad de camas que hay en cada estado	
			mapa.put("disponible",dis );
			mapa.put("ocupada",ocu);
			mapa.put("desinfeccion",des);
			mapa.put("mantenimiento", man);
			mapa.put("fueradeservicio", fdc);
			mapa.put("reservada", res);
			mapa.put("remitir", ppr);
			mapa.put("trasladar", ppt);
			mapa.put("consalida", cs);
			mapa.put("total", total);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log4JManager.error("\n \n Problema con la cama ---- ");
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResulsetDecorator: " + e);
			}
		}
		return mapa;
	}
	
	/**
	 * Metodo encargado de hacer todas las validaciones 
	 * para el campo Restriccion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @return mapa
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap ConsultaRestriccion (HashMap parametros)
	{
		HashMap mapa = new HashMap ();
		mapa = parametros;
	//	Log4JManager.info("entre a ConsultaRestriccion 1");
		int numRegistros=Utilidades.convertirAEntero(parametros.get("numRegistros").toString());
		
		for (int i=0;numRegistros>i;i++)
		{
			//se verifica si el codigo es 1 --> Masculino ó 2 --> Femenino
			
			if (parametros.get("sexocama_"+i).toString().equals(ConstantesBD.codigoSexoMasculino+""))
			{
				//Log4JManager.info("entre a hombre*************************");
				mapa.put("sexocama_"+i, "M");
				mapa.put("restriccioncama_"+i, "M");
			}
			else 
				if (parametros.get("sexocama_"+i).toString().equals(ConstantesBD.codigoSexoFemenino+""))
				{	
					mapa.put("sexocama_"+i, "F");
					mapa.put("restriccioncama_"+i, "F");
				//	Log4JManager.info("entre a ConsultaRestriccion 2");
				}
				else
					if (parametros.get("sexocama_"+i).toString().equals(""))
						if (parametros.get("indicativorestrictivo_"+i).toString().equals(ConstantesBD.acronimoSi))//se verifica si el estado de la cama es Ocupada
						{
							if(parametros.get("estadocama_"+i).toString().equals(ConstantesBD.codigoEstadoCamaOcupada+"")||parametros.get("estadocama_"+i).toString().equals(ConstantesBD.codigoEstadoCamaReservada+"")
									||parametros.get("estadocama_"+i).toString().equals(ConstantesBD.codigoEstadoCamaPendientePorRemitir+"")||parametros.get("estadocama_"+i).toString().equals(ConstantesBD.codigoEstadoCamaPendientePorTrasladar+"")
									||parametros.get("estadocama_"+i).toString().equals(ConstantesBD.codigoEstadoCamaConSalida+""))
							{
//								sexo de la cama = a sexo del paciente ó
								 //sexo de la cama = a sexo del paciente que la reserva
								if (parametros.get("sexopac_"+i).toString().equals(ConstantesBD.codigoSexoFemenino+""))
									mapa.put("restriccioncama_"+i, "F");
								else
									if (parametros.get("sexopac_"+i).toString().equals(ConstantesBD.codigoSexoMasculino+""))
										mapa.put("restriccioncama_"+i, "M");
								
							}														
							else
								//si el estado de la cama es disponible entonces se debe buscar las camas ocupadas de esa misma habitacion.
								if (parametros.get("estadocama_"+i).toString().equals(ConstantesBD.codigoEstadoCamaDisponible+""))
								{
									//se recorre el HashMap para buscar las demas camas de esa habitacion
								//	Log4JManager.info("\n\n************* entro -1");
									for (int j=0;j<numRegistros;j++)
									{
										//si se encuentra una cama de la misma habitacion y se encuentra en estado coupado entonces el
										//sexo de la cama sera el sexo del paciente de la cama ocupada de la misma habitacion.
									//	Log4JManager.info("\n\n************* entro 0");
										if (j!=i)
										{
										//	Log4JManager.info("\n\n************* entro a la diferencia");
											if (parametros.get("habitacioncama_"+j).toString().equals(parametros.get("habitacioncama_"+i).toString()) && (parametros.get("estadocama_"+j).toString().equals(ConstantesBD.codigoEstadoCamaOcupada+"") || parametros.get("estadocama_"+j).toString().equals(ConstantesBD.codigoEstadoCamaConSalida+"")
													|| parametros.get("estadocama_"+j).toString().equals(ConstantesBD.codigoEstadoCamaPendientePorRemitir+"") || parametros.get("estadocama_"+j).toString().equals(ConstantesBD.codigoEstadoCamaPendientePorTrasladar+"")))
											{
												//se ingresa en el HashMap el sexo de la cama;asi
												//Sexocama=SexoPaciente de la cama de la misma habitacion.
												//Log4JManager.info("\n\n************* entro 1");
												if (parametros.get("sexopac_"+j).toString().equals(ConstantesBD.codigoSexoFemenino+""))
												{
													//Log4JManager.info("\n\n************* entro 2");
													mapa.put("restriccioncama_"+i,"F");
												}
												else
													if (parametros.get("sexopac_"+j).toString().equals(ConstantesBD.codigoSexoMasculino+""))
													{
														//Log4JManager.info("\n\n************* entro 3");
														mapa.put("restriccioncama_"+i, "M");
													}
												//se iguala j a numRegistros para que no siga iterando el for.
												j=numRegistros;
											}
											else 
												mapa.put("restriccioncama_"+i, "Sin Restricción");
										}
									}
									
								}
								else
									mapa.put("restriccioncama_"+i, "");		
						}
						else
							mapa.put("restriccioncama_"+i, "Sin Restricción");
		
		
		}
		//Log4JManager.info("mapa con las restricciones de la cama ***************"+mapa);
		return mapa;
	}

	
	
	/*----------------------------------------------------------------------------------------
	 *                              FIN DE METODOS CENSO DE CAMAS
	 -----------------------------------------------------------------------------------------*/
	
	
}