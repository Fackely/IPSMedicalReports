package com.princetonsa.dao.sqlbase.enfermeria;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.enfermeria.ProgramacionAreaPaciente;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;


/**
 * @version 1.0
 * @fecha 06/01/09
 * @author Jhony Alexander Duque A. y Diego Fernando Bedoya Castaño
 *
 */
public class SqlBaseProgramacionAreaPacienteDao 
{
	private static Logger logger = Logger.getLogger(SqlBaseProgramacionAreaPacienteDao.class);
	
/**
 * ###############################################################################################################################
 * ########################################## SECCION DE PROGRAMACION POR PACIENTE ###############################################
 * ###############################################################################################################################
 */
	//indices del listado de ordenes de medicamentos
	static String [] indicesListadoOrdenesMedicas =ProgramacionAreaPaciente.indicesListadoOrdenesMedicas;
	//indices para los medicamentos a programar
	static String [] indicesProgramacionAdmin =ProgramacionAreaPaciente.indicesProgramacionAdmin;
	
		
		/**
		 * Cadena Encargada deinsertar datos en la tabla 
		 * programacion_admin, la cual es el encabezado
		 * de la programacion.								   	
		 */
		private static String strInsertarDatosProgramacionAdmin=" INSERT INTO programacion_admin (codigo,numero_solicitud,articulo,fecha_inicio,hora_inicio," +
																" usuario_programacion,institucion,observaciones) VALUES (?,?,?,?,?,?,?,?)";
		
		
		/**
		 * Cadena encargada de insertar los datos del 
		 * detalle de la progrmacion en la tabla 
		 */
		private static String strInsertarDatosDetProgAdmin=" INSERT INTO det_prog_admin (codigo,codigo_programacion,fecha,hora,activo) VALUES (?,?,?,?,?)";
		
		
		
		/**
		 * Cadena encargada de consultar la progracion 
		 * de un medicamento
		 */
		private static String strConsultarDatosProgramacionAdmin =" SELECT 	to_char(dpa.fecha, 'DD/MM/YYYY')  As fecha0," +
																		  " dpa.hora  As hora1," +
																		  " getnombreusuario(pa.usuario_programacion) As usuario_prog2," +
																		  " pa.codigo As codigo_prog_admin3, " +
																		  " dpa.codigo As codigo_det_prog_admin4 " +
																  " FROM programacion_admin pa " +
																  " INNER JOIN det_prog_admin dpa ON (dpa.codigo_programacion=pa.codigo) " +
																  " WHERE dpa.codigo_programacion=? AND dpa.activo='"+ConstantesBD.acronimoSi+"'";
		/**
		 * Cadena encargado de actualizar el estado del detalle de la programacion
		 */
		private static String strActualizarEstadoProg = "UPDATE det_prog_admin SET activo=? WHERE codigo=?";
		
		
		/**
		 * cadena para imprimir reporte por paciente	
		 */
		public static String cadenaG;
		
		
		
		/**
		 * Metodo encargado de actualizar el estado del
		 * detalle de la programacion
		 * @param connection
		 * @param activo
		 * @param codigo
		 * @return
		 */
		public static boolean actualizarEstadoProgramacionAdmin (Connection connection,String activo,int codigo)
		{
			logger.info("\n entro a actualizarEstadoProgramacionAdmin    activo -->"+activo+"    codigo -->"+codigo);
			
			String cadena=strActualizarEstadoProg;
			
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet ,ConstantesBD.concurrencyResultSet ));
				//activo
				ps.setString(1, activo);
				//codigo
				ps.setInt(2, codigo);
				
				if (ps.executeUpdate()>0)
					return true;
				
				
			}
			catch (SQLException e) 
			{
				logger.info("\n problema en actualizarEstadoProgramacionAdmin "+e);
			}
			
			return false;
		}
		
		
		/**
		 * Metodo encargado de consultar los datos de la
		 * programacion de administracion de medicamentos.
		 * @param connection
		 * @param codigoAdmin
		 * @return
		 */
		public static HashMap consultarProgramacionAdmin (Connection connection,int codigoAdmin,String validaFecha)
		{
			logger.info("\n entre a consultarProgramacionAdmin codigoAdmin -->"+codigoAdmin+"  validaFecha -->"+validaFecha);
			String cadena=strConsultarDatosProgramacionAdmin; 
			
			if (UtilidadTexto.getBoolean(validaFecha))
				cadena+=" AND dpa.fecha>= CURRENT_DATE AND dpa.hora >= to_char(CURRENT_TIMESTAMP,'HH24:MI') ";
			
			
			HashMap result = new HashMap ();
			result.put("numRegistros",0);
			logger.info("\n cadena -->"+cadena);
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoAdmin);
				
				result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
			} 
			catch (SQLException e)
			{
				logger.info("\n problema en consultarProgramacionAdmin "+e);
			}
			
			return result;
		}
		
		
		
		
		/**
		 * Metodo encargado de insertar los datos del detalle
		 * de la programacion
		 * @param connection
		 * @param datos
		 * ---------------------------
		 * KEY'S DEL MAPA DATOS
		 * ---------------------------
		 * codigoProg13 --> Requerido
		 * fechaProg14 --> Requerido
		 * horaProg15 --> Requerido
		 * activo16 --> Requerido
		 * @return
		 */
		public static int insertarDetalleProgramacion (Connection connection,HashMap datos)
		{
			logger.info("\n entre a insertarDetalleProgramacion -->"+datos);
			String cadena=strInsertarDatosDetProgAdmin;
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_det_prog_admin");
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				//codigo
				ps.setInt(1, codigo);
				//codigoProg13
				ps.setInt(2, Utilidades.convertirAEntero(datos.get(indicesProgramacionAdmin[13])+""));
				//fechaProg14
				ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get(indicesProgramacionAdmin[14])+"")));
				//horaProg15
				ps.setString(4, datos.get(indicesProgramacionAdmin[15])+"");
				//activo16
				ps.setString(5, datos.get(indicesProgramacionAdmin[16])+"");
				
				if (ps.executeUpdate()>0)
					return codigo;
				
			} 
			catch (SQLException e)
			{
				 logger.info("\n problema en insertarDetalleProgramacion "+e);
			}
			
			return ConstantesBD.codigoNuncaValido;
		}
		
		        
		
	/**
	 * Metodo encargado de insertar el encabezado de
	 * la programacion
	 * @param connection
	 * @param datos
	 * -----------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------------
	 * numeroSolicitud10_ --> Requerido
	 * articulo11_ --> Requerido
	 * fechaIni6_ --> Requerido
	 * horaIni7_ --> Requerido
	 * usuarioProg9_ --> Requerido
	 * institucion12 --> Requerido
	 * observ8_ --> Requerido
	 * @return
	 */
	public static int insertarEncabezadoProgramacion (Connection connection,HashMap datos)
	{
		logger.info("\n entre a  insertarEncabezadoProgramacion -->"+datos);
		String cadena=strInsertarDatosProgramacionAdmin;
		int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_programacion_admin");
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo
			ps.setInt(1, codigo);
			//numeroSolicitud10_
			ps.setInt(2, Utilidades.convertirAEntero(datos.get(indicesProgramacionAdmin[10])+""));
			//articulo11_
			ps.setInt(3, Utilidades.convertirAEntero(datos.get(indicesProgramacionAdmin[11])+""));
			//fechaIni6_
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get(indicesProgramacionAdmin[6])+"")));
			//horaIni7_
			ps.setString(5, datos.get(indicesProgramacionAdmin[7])+"");
			//usuarioProg9_
			ps.setString(6, datos.get(indicesProgramacionAdmin[9])+"");
			//institucion12
			ps.setInt(7, Utilidades.convertirAEntero(datos.get(indicesProgramacionAdmin[12])+""));
			//observ8_
			if (UtilidadCadena.noEsVacio(datos.get(indicesProgramacionAdmin[8])+""))
				ps.setString(8, datos.get(indicesProgramacionAdmin[8])+"");
			else
				ps.setNull(8, Types.VARCHAR);
			
			if(ps.executeUpdate()>0)
				return codigo;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema insertando los datos en la tabla programacion_admin "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
		
	/**
	 * Metodo encargado de Consultar el listado de ordenes medicas
	 * de un paciente.
	 * @param connection
	 * @param criterios
	 * ---------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------------
	 *  cuenta6 --> Requerido
	 * idPaciente7 --> Requerido
	 * @return HashMap
	 * ---------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------------------
	 * numeroOrden0_,medicamento1_,presentacion2_,
	 * dosis3_,via4_,frecuencia5_
	 * 
	 */
	public static HashMap consultarListadoOrdenesMedicas (Connection connection,HashMap criterios, String remite)
	{
		logger.info("\n consultarListadoOrdenesMedicas--> criterios "+criterios);
		String consulta =" SELECT getconsecutivosolicitud (ds.numero_solicitud) As numero_orden0," +
		   " getCodArticuloAxiomaInterfaz(ds.articulo,'"+ConstantesIntegridadDominio.acronimoAmbos+"')||' '||getdescripcionarticulo(ds.articulo) As medicamento1," +
		   " getformafarmaceuticaarticulo (ds.articulo) As presentacion2," +
		   " ds.dosis As dosis3, " +
		   " ds.via As via4, " +
		   " ds.frecuencia As frecuencia5," +
		   " ds.numero_solicitud As numero_solicitud10," +
		   " ds.articulo As articulo11," +
		   " getnumeroprogramaciones(ds.numero_solicitud,ds.articulo) As tiene_prog12," +
		   " getnrodosispendientesadmin(ds.numero_solicitud,ds.articulo)  As nro_dosis_pend_admon13," +
		   " ds.tipo_frecuencia As tipo_frecuencia14," +
		   " inventarios.getunimedidaunidosisxunidad(ds.unidosis_Articulo) AS unidadDosis16";
		if(remite.equals("consulta"))
			consulta+=", CASE WHEN getPrimeraHoraProgramacion(ds.numero_solicitud,ds.articulo) IS NULL THEN 'Ver' ELSE getPrimeraHoraProgramacion(ds.numero_solicitud,ds.articulo) END AS hora_p15";
		else
			consulta+=", '' AS hora_p15";
	   consulta+=" FROM detalle_solicitudes ds " +
	   " INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud=ds.numero_solicitud)" +
	   " INNER JOIN solicitudes s ON (s.numero_solicitud=ds.numero_solicitud)" +
	   " WHERE  s.cuenta=?   " +
	   		//con esta funcion se verifica que el ingreso esta abierto, 
	   		//y que la cuenta este en los estados indicados (que sea cuenta valida)	
	   		" AND getcuentavalidaxpaci(?)>=1 " +
	   		//se verifica que el estado de la solicitud sea solicitada o despachada
	   		" AND (" +
	   				"getcodigoestadohcsol(ds.numero_solicitud)="+ConstantesBD.codigoEstadoHCDespachada+" " +
	   				"OR " +
	   				"getcodigoestadohcsol(ds.numero_solicitud)="+ConstantesBD.codigoEstadoHCSolicitada+" " +
	   			 ")" +
	   		" AND getnrodosispendientesadmin(ds.numero_solicitud,ds.articulo)>0 ";
		if(remite.equals("consulta"))
			consulta+=" AND getnumeroprogramaciones(ds.numero_solicitud,ds.articulo)>0";
	   	consulta+=" ORDER BY ds.numero_solicitud " ;
		
		HashMap resultado= new HashMap ();
		resultado.put("numRegistros", 0);
		try 
		{
			logger.info("\n consulta -->"+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//cuenta6
			logger.info("---->>"+criterios.get(indicesListadoOrdenesMedicas[6])+"------"+criterios.get(indicesListadoOrdenesMedicas[7]));
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indicesListadoOrdenesMedicas[6])+""));
			//idPaciente7
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get(indicesListadoOrdenesMedicas[7])+""));
			
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema en consultarListadoOrdenesMedicas "+e);
		}
		
		return resultado;
	}
		
		
		
		
/**
 * ###############################################################################################################################
 * ########################################## FIN SECCION DE PROGRAMACION POR PACIENTE ###############################################
 * ###############################################################################################################################
 */
		

		
		
		
		
		

	/**
	 * ###############################################################################################################################
	 * ########################################## SECCION DE PROGRAMACION POR AREA  ###############################################
	 * ###############################################################################################################################
	 */
	
	/**
	 * Consulta las Areas por Via de Ingreso segun centro de Atencion
	 */
	private static String strListarAreas ="SELECT distinct (cc.codigo), cc.nombre " +
									"FROM centros_costo cc " +
											"INNER JOIN centro_costo_via_ingreso cv ON (cc.codigo=cv.centro_costo) " +
									"WHERE cc.centro_atencion=? ORDER BY cc.nombre";
	
	private static String [] indicesAreas = {"codigo_","nombre_"};
	
	/**
	 * Consulta los pisos
	 */
	private static String strListarPisos="SELECT codigo, nombre FROM pisos WHERE centro_atencion=?";
	
	/**
	 * Consulta las Habitaciones segun Piso
	 */
	private static String strListarHabitaciones="SELECT codigo, nombre, piso FROM habitaciones WHERE piso=?";
	
	private static String [] indicesHabitaciones = {"codigo_","nombre_","piso_"};
	
	/**
	 * Consulta los pacientes para la Programacion
	 */
	public static String cadenaConsultaPaciente;
		
	/**
	 * Consulta las Areas por Via de Ingreso segun Centro de Atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> listaAreas (Connection con, int centroAtencion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strListarAreas, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroAtencion);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultados.put("INDICES",indicesAreas);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Areas");
		}
		return resultados;
	}
	
	/**
	 * Consulta los Pisos segun Centro de Atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> listaPisos (Connection con, int centroAtencion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strListarPisos, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroAtencion);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultados.put("INDICES",indicesAreas);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Pisos");
		}
		return resultados;
	}
	
	/**
	 * Consulta las Habitaciones segun Piso
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> listaHabitaciones (Connection con, int piso)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strListarHabitaciones, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, piso);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultados.put("INDICES",indicesHabitaciones);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Habitaciones");
		}
		return resultados;
	}
	
	/**
	 * Consulta Pacientes General
	 * @param con
	 * @param fechaIniOrden
	 * @param fechaFinOrden
	 * @param area
	 * @param piso
	 * @param habitacion
	 * @param checkProg
	 * @return
	 */
	public static HashMap<String, Object> ConsultaPacientes (Connection con, String fechaIniProg, String fechaFinProg, String area, String piso, String habitacion, int checkProg, String horaIniProg, String horaFinProg, int remite)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		cadenaConsultaPaciente="SELECT distinct getnombrepersona(p.codigo) AS nombrep, " +
				"getconsecutivoingreso (c.id_ingreso) AS sol, " +
//				"getCodArticuloAxiomaInterfaz(ds.articulo,'"+ConstantesIntegridadDominio.acronimoAxioma+"')||' '||getdescripcionarticulo(ds.articulo) As medicamento, " +
//				" ds.dosis || ' ' || getunimedidaunidosisxunidad(ds.unidosis_articulo) As dosis, " +
//				" ds.via As via, " +
//				" ds.frecuencia || ' ' || ds.tipo_frecuencia As frecuencia," +
				"getfechaingreso(c.id,c.via_ingreso) AS fechaing, " +
				"getnumeroytipoid(c.codigo_paciente) AS tiponumid, " +				
				"CASE WHEN getescuentaasocio(c.id)='S' AND escuentacerradaasocio(c.id)='N' AND getcuentafinalasocioint(c.id_ingreso,c.id) IS NOT NULL THEN getcuentafinalasocioint(c.id_ingreso,c.id) ELSE c.id END AS cuentap, " +
				"getnumhistocli(c.codigo_paciente) AS his_clin, " +
				"getnombreviaingreso(getviaingresocuenta(CASE WHEN getescuentaasocio(c.id)='S' AND escuentacerradaasocio(c.id)='N' AND getcuentafinalasocioint(c.id_ingreso,c.id) IS NOT NULL THEN getcuentafinalasocioint(c.id_ingreso,c.id) ELSE c.id END)) AS viai, " +
				"getnombreconvenio(getconvenioxingreso(c.id_ingreso)) AS conv, " +
				"getdiagnosticopaciente(c.id,vi.codigo) AS diag, " +
				"CASE WHEN hcc.codigocama IS NULL THEN '' ELSE hcc.nombrehabitacion || ' - ' || hcc.nombrecama END AS camap, " +
				"p.codigo AS codp " +
		"FROM solicitudes s " +
				"INNER JOIN detalle_solicitudes ds ON (ds.numero_solicitud=s.numero_solicitud) " +
				"INNER JOIN his_camas_cuentas hcc ON (s.cuenta=hcc.cuenta) " +
				"INNER JOIN cuentas c ON (s.cuenta=c.id) " +
				"INNER JOIN personas p ON (c.codigo_paciente=p.codigo) " +
				"INNER JOIN vias_ingreso vi ON (c.via_ingreso=vi.codigo) ";
		if(remite==1)
			cadenaConsultaPaciente+="INNER JOIN programacion_admin pa ON (s.numero_solicitud=pa.numero_solicitud AND ds.articulo=pa.articulo) " +
					"INNER JOIN det_prog_admin dpa ON (pa.codigo=dpa.codigo_programacion) ";
		cadenaConsultaPaciente+="WHERE getcuentavalidaxpaci(c.codigo_paciente)>=1 " +
				"AND ((getcodigoestadohcsol(s.numero_solicitud)="+ ConstantesBD.codigoEstadoHCDespachada +") " +
				"OR (getcodigoestadohcsol(s.numero_solicitud)="+ ConstantesBD.codigoEstadoHCSolicitada +")) ";
		
		String [] indicesConsultaP={"nombrep_","tiponumid_","sol_","cuentap_","his_clin_","viai_"};
		
		if(!area.equals("-1"))
			cadenaConsultaPaciente+="AND c.area="+ area +" ";
		
		if(!piso.equals("-1"))
			cadenaConsultaPaciente+="AND hcc.codigopkpiso="+ piso +" ";
		
		if(!habitacion.equals("-1"))
			cadenaConsultaPaciente+="AND hcc.codigopkhabitacion="+ habitacion +" ";
		
		if(checkProg == 1)
			cadenaConsultaPaciente+="AND getNumeroProgramaciones(s.numero_solicitud,ds.articulo)<>-1 ";
		
		if(remite==2){
			cadenaConsultaPaciente+="AND to_char(s.fecha_solicitud,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaIniProg)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinProg)+"' ";
		}
		
		if(remite==1)
			cadenaConsultaPaciente+="AND getnumeroprogramaciones(ds.numero_solicitud,ds.articulo)>0 " +
					" AND TO_CHAR(dpa.fecha,'YYYY-MM-DD')||' '|| dpa.hora BETWEEN ('"+UtilidadFecha.conversionFormatoFechaABD(fechaIniProg)+" "+horaIniProg+"') AND ('"+UtilidadFecha.conversionFormatoFechaABD(fechaIniProg)+" "+horaIniProg+"') ";
		
		cadenaConsultaPaciente+=" GROUP BY getnombrepersona(p.codigo),c.codigo_paciente,c.id_ingreso,c.id,vi.codigo,hcc.codigocama,hcc.nombrehabitacion,hcc.nombrecama,p.codigo,ds.articulo" +
//				",ds.dosis" +
				",ds.unidosis_articulo" +
//				",ds.via,ds.frecuencia,ds.tipo_frecuencia" +
				",c.via_ingreso";
		
		logger.info("\n\nCADENA CONSULTA P>>>>>>>>>>>>>>>>>>"+cadenaConsultaPaciente);
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaPaciente, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultados.put("INDICES",indicesConsultaP);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta por Area de Pacientes");
		}
		logger.info("\n\nMAPAAAA PPPPP >>>>>>>>>>>>>>>"+resultados);
		return resultados;
	}

	/**
	 * ###############################################################################################################################
	 * ########################################## FIN SECCION DE PROGRAMACION POR AREA ###############################################
	 * ###############################################################################################################################
	 */
	
	
	
}