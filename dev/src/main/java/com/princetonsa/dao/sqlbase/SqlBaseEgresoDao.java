/*
 * @(#)SqlBaseEgresoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ResultadoInteger;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Egreso;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Egreso
 *
 *	@version 1.0, Apr 1, 2004
 */
public class SqlBaseEgresoDao 
{

	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseEgresoDao.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar todos los datos del egreso con excepción del código de admisión y la cama  
	 */
	/*private static final String cargarEgresoGeneralStr="SELECT eg.usuario_responsable as usuarioResponsable, ev.codigo as codigoEvolucion, ev.fecha_grabacion as fechaGrabacion, ev.hora_grabacion as horaGrabacion, ev.fecha_evolucion as fechaEvolucion, ev.hora_evolucion as horaEvolucion, autorizacion.numero_autorizacion as numeroAutorizacion, eg.estado_salida as estadoSalida, eg.destino_salida as codigoDestinoSalida, destsal.nombre as destinoSalida, eg.otro_destino_salida as otroDestinoSalida, eg.diagnostico_muerte as acroDiagM, eg.diagnostico_muerte_cie as tipoCieDiagM, diagm.nombre as diagM, eg.causa_externa as codigoCausaExterna, cauext.nombre as causaExterna, eg.diagnostico_principal as acroDiagP, eg.diagnostico_principal_cie as tipoCieDiagP, diagp.nombre as diagP, eg.diagnostico_relacionado1 as acroDiagR1, eg.diagnostico_relacionado1_cie as tipoCieDiagR1, diagr1.nombre as diagR1, eg.diagnostico_relacionado2 as acroDiagR2, eg.diagnostico_relacionado2_cie as tipoCieDiagR2, diagr2.nombre as diagR2, eg.diagnostico_relacionado3 as acroDiagR3, eg.diagnostico_relacionado3_cie as tipoCieDiagR3, diagr3.nombre as diagR3, per.tipo_identificacion as tipoIdMedico, per.numero_identificacion as numIdMedico from egresos eg, destinos_salida destsal, diagnosticos diagm, causas_externas cauext, diagnosticos diagp, diagnosticos diagr1, diagnosticos diagr2, diagnosticos diagr3, personas per, evoluciones ev, " +
		"("+
			"select numero_autorizacion from admisiones_hospi where cuenta=?" +
		" UNION " +
			"select numero_autorizacion from admisiones_urgencias where cuenta=?" +
		") autorizacion " +
		"where eg.destino_salida=destsal.codigo and eg.diagnostico_muerte=diagm.acronimo and eg.diagnostico_muerte_cie=diagm.tipo_cie and per.codigo=eg.codigo_medico and eg.causa_externa=cauext.codigo and eg.diagnostico_principal=diagp.acronimo and eg.diagnostico_principal_cie=diagp.tipo_cie and diagr1.acronimo=eg.diagnostico_relacionado1 and diagr1.tipo_cie=eg.diagnostico_relacionado1_cie and diagr1.tipo_cie=eg.diagnostico_relacionado1_cie and diagr2.acronimo=eg.diagnostico_relacionado2 and diagr2.tipo_cie=eg.diagnostico_relacionado2_cie and diagr3.acronimo=eg.diagnostico_relacionado3 and diagr3.tipo_cie=eg.diagnostico_relacionado3_cie and eg.evolucion=ev.codigo and eg.cuenta=?";

*/
	
	private static final String cargarEgresoGeneralStr="" +
	   " SELECT eg.usuario_responsable as usuarioResponsable, " +
	   "       coalesce(ev.codigo,"+ConstantesBD.codigoNuncaValido+") as codigoEvolucion, " +
	   "	   to_char(coalesce(ev.fecha_grabacion,eg.fecha_grabacion),'YYYY-MM-DD') as fechaGrabacion, " +
	   "       coalesce(ev.hora_grabacion,eg.hora_grabacion) as horaGrabacion, " +
	   "	   to_char(coalesce(ev.fecha_evolucion,eg.fecha_egreso),'YYYY-MM-DD') as fechaEvolucion, " +
	   "       coalesce(ev.hora_evolucion,eg.hora_egreso) as horaEvolucion, " +
	   "	   autorizacion.numero_autorizacion as numeroAutorizacion, eg.estado_salida as estadoSalida, " +
	   "	   coalesce(eg.destino_salida,"+ConstantesBD.codigoNuncaValido+") as codigoDestinoSalida," +
	   "	   CASE WHEN eg.destino_salida IS NULL THEN '' ELSE destsal.nombre END as destinoSalida, " +
	   "	   eg.otro_destino_salida as otroDestinoSalida, eg.diagnostico_muerte as acroDiagM, " +
	   "	   eg.diagnostico_muerte_cie as tipoCieDiagM, diagm.nombre as diagM, eg.causa_externa as codigoCausaExterna, " +
	   "	   cauext.nombre as causaExterna, eg.diagnostico_principal as acroDiagP, eg.diagnostico_principal_cie as tipoCieDiagP, " +
	   "	   diagp.nombre as diagP, eg.diagnostico_relacionado1 as acroDiagR1, eg.diagnostico_relacionado1_cie as tipoCieDiagR1, " +
	   "	   diagr1.nombre as diagR1, eg.diagnostico_relacionado2 as acroDiagR2, eg.diagnostico_relacionado2_cie as tipoCieDiagR2, " +
	   "	   diagr2.nombre as diagR2, eg.diagnostico_relacionado3 as acroDiagR3, eg.diagnostico_relacionado3_cie as tipoCieDiagR3, " +
	   "	   diagr3.nombre as diagR3, per.tipo_identificacion as tipoIdMedico, per.numero_identificacion as numIdMedico " +
	   "	FROM egresos eg" +
	   "		INNER JOIN diagnosticos diagm ON (eg.diagnostico_muerte=diagm.acronimo AND eg.diagnostico_muerte_cie=diagm.tipo_cie)" +
	   "		INNER JOIN causas_externas cauext ON (eg.causa_externa=cauext.codigo)" +
	   "		INNER JOIN diagnosticos diagp ON (eg.diagnostico_principal=diagp.acronimo AND eg.diagnostico_principal_cie=diagp.tipo_cie)" +
	   "		INNER JOIN diagnosticos diagr1 ON (diagr1.acronimo=eg.diagnostico_relacionado1 AND diagr1.tipo_cie=eg.diagnostico_relacionado1_cie)" +
	   "		INNER JOIN diagnosticos diagr2 ON (diagr2.acronimo=eg.diagnostico_relacionado2 and diagr2.tipo_cie=eg.diagnostico_relacionado2_cie)" +
	   "		INNER JOIN diagnosticos diagr3 ON (diagr3.acronimo=eg.diagnostico_relacionado3 and diagr3.tipo_cie=eg.diagnostico_relacionado3_cie)" +
	   "		INNER JOIN personas per ON (per.codigo=eg.codigo_medico)" +
	   "		INNER JOIN " +
	   "		("+
	   "		select numero_autorizacion,cuenta " +
	   "			from admisiones_hospi " +
	   "				where cuenta=?" +
	   " 		UNION " +
	   "		select numero_autorizacion,cuenta " +
	   "			from admisiones_urgencias " +
	   "				where cuenta=?" +
	   "		) autorizacion ON (eg.cuenta=autorizacion.cuenta)" +
	   "		LEFT OUTER JOIN destinos_salida destsal ON (eg.destino_salida=destsal.codigo)" +
	   "		LEFT OUTER JOIN evoluciones ev ON (ev.codigo=eg.evolucion) " +
		"			WHERE eg.cuenta=?";	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para eliminar un egreso que se genero automaticamente.
	 */
	private static final String eliminarEgresoAutomaticoStr = "DELETE FROM egresos WHERE cuenta = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para revisar si existe un egreso efectivo
	 */	
	private static final String existeEgresoEfectivoStr = "" +
	"SELECT cuenta FROM egresos WHERE cuenta = ? and usuario_responsable IS NOT NULL";

	/**
	 * Cadena constante con el <i>statement</i> necesario para eliminar un semi-egreso que se generó automaticamente.
	 */
	private static final String eliminarPreEgresoStr ="DELETE FROM egresos WHERE cuenta = ?";	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar la causa externa de una valoración de hospitalización
	 */
	private static final String cargarCausaExternaUltimaValoracionHospitalizacionStr="SELECT valgen.causa_externa as codigoCausaExterna, cext.nombre as causaExterna from valoraciones valgen, val_hospitalizacion valh, solicitudes sol, causas_externas cext where valgen.numero_solicitud=valh.numero_solicitud and sol.numero_solicitud=valgen.numero_solicitud and valgen.causa_externa=cext.codigo and sol.cuenta=? order by valgen.numero_solicitud desc";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar la causa externa de una valoración de urgencias
	 */
	private static final String  cargarCausaExternaUltimaValoracionUrgenciasStr="SELECT valgen.causa_externa as codigoCausaExterna, cext.nombre as causaExterna from valoraciones valgen, valoraciones_urgencias valur, solicitudes sol, causas_externas cext where valgen.numero_solicitud=valur.numero_solicitud and sol.numero_solicitud=valgen.numero_solicitud and valgen.causa_externa=cext.codigo and sol.cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> para saber si una cuenta ya tiene egreso o no.
	 */
	private static final String existeEgresoStr="SELECT count(1) as numResultados from egresos where cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un egreso que se genero desde la evolución.
	 */
	private static final String insertarEgresoDesdeEvolucionStr="INSERT INTO egresos (cuenta, evolucion, estado_salida, destino_salida, otro_destino_salida, causa_externa, diagnostico_muerte, diagnostico_muerte_cie, diagnostico_principal, diagnostico_principal_cie, diagnostico_relacionado1, diagnostico_relacionado1_cie, diagnostico_relacionado2, diagnostico_relacionado2_cie, diagnostico_relacionado3, diagnostico_relacionado3_cie, codigo_medico, es_automatico,diagnostico_complicacion,diagnostico_complicacion_cie, fecha_grabacion, hora_grabacion) values (?,?,? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + ValoresPorDefecto.getValorFalseParaConsultas() + ",?,?, current_date, "+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar el número de autorización de una admisión de urgencias
	 */
	private static final String actualizarNumeroAutorizacionAdmisionUrgenciasStr="UPDATE admisiones_urgencias set numero_autorizacion=? where cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar el número de autorización de una admisión de hospitalización
	 */
	private static final String actualizarNumeroAutorizacionAdmisionHospitalizacionStr="UPDATE admisiones_hospi set numero_autorizacion=? where cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar un egreso que se genero desde la evolución.
	 */
	private static final String actualizarEgresoDesdeEvolucionStr="update egresos set evolucion=?, estado_salida=?, destino_salida=?, otro_destino_salida=?, causa_externa=?, diagnostico_muerte=?, diagnostico_muerte_cie=?, diagnostico_principal=?, diagnostico_principal_cie=?, diagnostico_relacionado1=?, diagnostico_relacionado1_cie=?, diagnostico_relacionado2=?, diagnostico_relacionado2_cie=?, diagnostico_relacionado3=?, diagnostico_relacionado3_cie=?, codigo_medico=?, diagnostico_complicacion = ?, diagnostico_complicacion_cie = ?, fecha_grabacion=CURRENT_DATE, hora_grabacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para completar un semi-egreso que se generó automaticamente.
	 */
	private static final String completarSemiEgresoStr ="UPDATE egresos set usuario_responsable=?,fecha_egreso=CURRENT_DATE, hora_egreso="+ValoresPorDefecto.getSentenciaHoraActualBD()+", fecha_grabacion=CURRENT_DATE, hora_grabacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+",diagnostico_principal=?,diagnostico_principal_cie=?,tipo_monitoreo = ? where cuenta=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar la informacion sobre motivo de reversion de egreso y este aparecera en la epicrisis   
	 */
	private static final String actualizarInformacionMotivoReversionStr="UPDATE egresos set mostrar_motivo_rev_epicrisis=?, evolucion_genero_motivo_rev=? where cuenta=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar el boolean de motivo de reversion de egreso    
	 */
	private static final String actualizarInformacionMotivoReversionSoloBooleanStr="UPDATE egresos set mostrar_motivo_rev_epicrisis=? where evolucion_genero_motivo_rev=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar el número de autorización de una admisión de urgencias
	 */
	private static final String cargarNumeroAutorizacionAdmisionHospitalizacionStr="SELECT numero_autorizacion as numeroAutorizacion from admisiones_hospi where codigo=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar el número de autorización de una admisión de urgencias
	 */
	private static final String cargarNumeroAutorizacionAdmisionUrgenciasStr="SELECT numero_autorizacion as numeroAutorizacion from admisiones_urgencias where codigo=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un semi-egreso que se generó automaticamente.
	 */
	private static final String insertarPreEgresoStr ="" +
	"INSERT INTO EGRESOS " +
	"(cuenta, diagnostico_muerte, diagnostico_muerte_cie, diagnostico_principal, diagnostico_principal_cie, diagnostico_relacionado1, diagnostico_relacionado1_cie, diagnostico_relacionado2, diagnostico_relacionado2_cie, diagnostico_relacionado3, diagnostico_relacionado3_cie, fecha_grabacion, hora_grabacion, codigo_medico, es_automatico) " +
	"VALUES (?, '1', 0, '1', 0, '1', 0, '1', 0, '1', 0, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, " + ValoresPorDefecto.getValorFalseParaConsultas() + ")";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un egreso automatico.
	 */	
	private static final String insertarEgresoAutomaticoStr = "" +
	"INSERT INTO EGRESOS " +
	"(cuenta, fecha_egreso, hora_egreso, fecha_grabacion, hora_grabacion, usuario_responsable, diagnostico_muerte, diagnostico_muerte_cie, diagnostico_principal, diagnostico_principal_cie, diagnostico_relacionado1, diagnostico_relacionado1_cie, diagnostico_relacionado2, diagnostico_relacionado2_cie, diagnostico_relacionado3, diagnostico_relacionado3_cie, es_automatico, codigo_medico, estado_salida,causa_externa) " +
	"VALUES (?, CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + ValoresPorDefecto.getValorTrueParaConsultas() + ",?,?,?)";
		
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un egreso automatico cuando se hace una valoración 
	 * y se selecciona conducta a seguir hospitalizar en piso
	 */	
	private static final String insertarEgresoAutomaticoValoracionHospitalizarEnPisoStr = "" +
	"INSERT INTO EGRESOS " +
	"(cuenta, destino_salida, fecha_egreso, hora_egreso, fecha_grabacion, hora_grabacion, usuario_responsable, diagnostico_muerte, diagnostico_muerte_cie, diagnostico_principal, diagnostico_principal_cie, diagnostico_relacionado1, diagnostico_relacionado1_cie, diagnostico_relacionado2, diagnostico_relacionado2_cie, diagnostico_relacionado3, diagnostico_relacionado3_cie, es_automatico,codigo_medico,tipo_monitoreo) " +
	"VALUES (?, ?, ?,?,CURRENT_DATE,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + ValoresPorDefecto.getValorTrueParaConsultas() + ",?, ?)";
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar los datos basicos de un egreso dado el numero de cuenta
	 */
	private static final String consultarBasicoEgresoStr = "" +
	"SELECT diagnostico_principal as codigoDiagnosticoPrincipal, diagnostico_principal_cie as codigoDiagnosticoPrincipalCie, nombre as nombreDiagnosticoPrincipal, fecha_egreso || '' as fechaEgreso, substring(hora_egreso||'' ,0,6) as horaEgreso " +
	"FROM egresos, diagnosticos" +
	" WHERE cuenta = ? and acronimo = diagnostico_principal and tipo_cie = diagnostico_principal_cie and usuario_responsable IS NOT NULL";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar todos los datos del egreso necesarios en la reversión del egreso  
	 */
	private static final String cargarEgresoReversionEgresoStr =
		"SELECT c.codigo AS codigoCama,"				+
						"c.numero_cama AS numeroCama, " +
						"c.descripcion AS descripcionCama, " +
						"getnombrepisocama(c.codigo) AS piso, "				+
						"getnomhabitacioncama(c.codigo) AS habitacion, " +
						"getnomtipohabitacioncama(c.codigo) AS tipoHabitacion, "+
						"getnomcentrocosto(c.centro_costo) AS centroCostoCama,"			+
						"c.centro_costo AS codigoCentroCostoCama,"	+
						"tuc.nombre AS tipoUsuarioCama,"			+
						"c.estado AS estadoCama,"				+
						"ec.nombre AS nombreEstadoCama,"				+
						"tuc.codigo AS codigoTipoUsuarioCama,"	+
						"to_char(e.fecha_egreso,'"+ConstantesBD.formatoFechaBD+"') AS fechaEgreso,"				+
						"e.hora_egreso AS horaEgreso,"				+
						"per.tipo_identificacion AS tipoIdMedico,"			+
						"per.numero_identificacion AS numIdMedico "				+
		"FROM egresos e,"		+
						"camas1 c,"		+
						"personas per,"		+
						"tipos_usuario_cama tuc," +
						"estados_cama ec "	+
		"WHERE e.cuenta= ?"
						+" AND per.codigo=e.codigo_medico "
						+ " AND"	+
						"("																+
							"c.codigo in "											+
							"("															+
								"SELECT ah.codigo_nueva_cama FROM traslado_cama ah WHERE ah.cuenta=? and ah.fecha_finalizacion is null"								+
							") OR "		+
							"c.codigo in "											+
							"("															+
								"SELECT au.cama_observacion "					+
								"FROM admisiones_urgencias au "				+
								"WHERE au.cuenta = ?"							+
							")"															+
						") AND "	+
						"ec.codigo = c.estado AND tuc.codigo=c.tipo_usuario_cama";
	
	private static final String cargarSemiEgresoStr="SELECT to_char(fecha_egreso, 'YYYY-MM-DD') as fecha_egreso, hora_egreso as hora_egreso, usuario_responsable as usuario_responsable, diagnostico_principal as diagnostico_principal, codigo_medico AS codigo_medico FROM egresos where cuenta=?";
	/**
	 * cadena que carga los datos de una reversión de egreso
	 */
	private static final String cargarReversionEgresoStr="SELECT " +
			"fecha_reversion_egreso AS fecha_reversion," +
			"hora_reversion_egreso AS hora_reversion " +
			"FROM egresos WHERE cuenta=?";
	
	/**
	 * carga la fecha y hora de egreso
	 */
	private static final String cargarFechaHoraEgresoStr="select to_char(fecha_egreso,'"+ConstantesBD.formatoFechaBD+"') AS fecha , hora_grabacion AS hora  from egresos where cuenta= ?";
	
	
	
	/**********************************************************************************************
	 * Modificado por anexo 747
	 */
		private static final String [] indicesFactura=Egreso.indicesFactura;
		
		// cadena para consultar la informacion restante para la boleta de salida
		private static final String strconsultaDatosFactura=" SELECT  coalesce(to_char(i.fecha_egreso,'"+ConstantesBD.formatoFechaAp+"'),TO_CHAR(ci.fecha_cierre,'"+ConstantesBD.formatoFechaAp+"'),'') As fecha_cierre_ingreso0, " +
																	" coalesce(substr(i.hora_egreso,0,6),substr(ci.hora_cierre,0,6),'') As hora_cierre_ingreso1, " +
																	" coalesce(f.consecutivo_factura,0) AS consecutivo_factura," +
																	" coalesce(to_char(f.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') As fecha_factura, " +
																	" getcamacuentasalida(getcuentaxestado(i.id),getviaingresocuenta(getcuentaxestado(i.id))) As cama3, " +
																	" manejopaciente.getcodigocamaxcuentasalida(getcuentaxestado(i.id),getviaingresocuenta(getcuentaxestado(i.id))) As codigo_cama4," +
																	" getcuentaxestado(i.id) As codigo_cuenta12 " +
															" FROM ingresos i  " +
															" LEFT OUTER JOIN facturas f ON (f.cod_paciente=i.codigo_paciente AND f.estado_facturacion!="+ConstantesBD.codigoEstadoFacturacionAnulada+") " +
															" LEFT OUTER JOIN cierre_ingresos ci ON (ci.id_ingreso = i.id) "+
															" WHERE i.id=?" ;
	
		
		private static final String strActualizacionDatosNuevosEgreso =" UPDATE egresos SET acompanado_por=?, remitido_a=?, placa_nro=?," +
																		" conductor_ambulancia=?, quien_recibe=?, observaciones=? where cuenta=? ";
		
		
		
		private static final String strConsultaDatosNuevos=" SELECT " +
																	" e.acompanado_por AS acompanado_por6, " +
																	" e.remitido_a As remitido_a7, " +
																	" e.placa_nro As placa_nro8," +
																	" e.conductor_ambulancia As conductor_ambulancia9," +
																	" e.quien_recibe As quien_recibe10, " +
																	" e.observaciones As observaciones11 " +
																" FROM egresos e " +
																	" WHERE cuenta=?";
		
		private static final String strConsultaExisteBoletaSalida="SELECT "+
																		"fecha_egreso, "+
																		"hora_egreso, "+
																		"usuario_responsable "+
																	"FROM egresos e "+
																	"WHERE cuenta=? ";
		/**
		 * Metodo encargado de consultar los nuevos datos del egreso
		 * @param connection
		 * @param cuenta
		 *  @return
		 *  -------------------------
		 *  KEY'S DEL MAPA RESULT
		 *  -------------------------
		 *  acompanadoPor6, remitidoA7,
		 *  placa8,conductor9,quienRecibe10,
		 *  observaciones11,codigoCuenta12 
		 */
		public static HashMap consultarNuevosDatosEgreso (Connection connection,String cuenta)
		{
			
			String cadena=strConsultaDatosNuevos;
			HashMap result = new HashMap ();
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//cuenta
				ps.setInt(1, Utilidades.convertirAEntero(cuenta));
				
				result= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false, true);
				
			}
			catch (SQLException e) 
			{
				logger.info("\n problema consultando los datos del egreso "+e);
			}
			
			return result;
		}
		
		
		
		
		/**
		 * Metodo encargado de actualizar los nuevos datos del egreso
		 * @param connection
		 * @param datos
		 * ----------------------------
		 * KEY'S DEL MAPA DATOS
		 * ----------------------------
		 * acompanadoPor6 --> Opcional
		 * remitidoA7 --> Opcional
		 * placa8 --> Opcional
		 * conductor9 --> Opcional
		 * quienRecibe10 --> Opcional
		 * observaciones11 --> Opcional
		 * codigoCuenta12 --> Requerido
		 * @return false/true
		 */
		public static boolean actualizarNuevosDatosEgreso (Connection connection,HashMap datos)
		{
			logger.info("\n entre a actualizarNuevosDatosEgreso datos --> "+datos);
					
			String cadena=strActualizacionDatosNuevosEgreso;
			
			try 
			{
				logger.info("\n consulta --> "+cadena);
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				//acompanado_por
				if (UtilidadCadena.noEsVacio(datos.get(indicesFactura[6])+""))
					ps.setString(1, datos.get(indicesFactura[6])+"");
				else
					ps.setNull(1, Types.VARCHAR);
				
				//remitido_a
				if (UtilidadCadena.noEsVacio(datos.get(indicesFactura[7])+""))
					ps.setString(2, datos.get(indicesFactura[7])+"");
				else
					ps.setNull(2, Types.VARCHAR);
				
				//placa_nro
				if (UtilidadCadena.noEsVacio(datos.get(indicesFactura[8])+""))
					ps.setString(3, datos.get(indicesFactura[8])+"");
				else
					ps.setNull(3, Types.VARCHAR);
				
				//conductor_ambulancia
				if (UtilidadCadena.noEsVacio(datos.get(indicesFactura[9])+""))
					ps.setString(4, datos.get(indicesFactura[9])+"");
				else
					ps.setNull(4, Types.VARCHAR);
				
				//quien_recibe
				if (UtilidadCadena.noEsVacio(datos.get(indicesFactura[10])+""))
					ps.setString(5, datos.get(indicesFactura[10])+"");
				else
					ps.setNull(5, Types.VARCHAR);
				
				//observaciones
				if (UtilidadCadena.noEsVacio(datos.get(indicesFactura[11])+""))
					ps.setString(6, datos.get(indicesFactura[11])+"");
				else
					ps.setNull(6, Types.VARCHAR);
				//cuenta
				ps.setInt(7, Utilidades.convertirAEntero(datos.get(indicesFactura[12])+""));
				
				if (ps.executeUpdate()>0)
					return true;
				
			}
			catch (SQLException e)
			{
				logger.info("\n problema actualizando datos de egresos "+e);
			}
			
			return false;
		}
		
		
		
		
		
		
		
		
		/**
		 * Metodo encargado de consultar los datos de la factura
		 * para la boleta de salida
		 * @param connection
		 * @param datos
		 * ------------------------------
		 * KEY'S DEL MAPA DATOS
		 * ------------------------------
		 * --ingreso6
		 * @return  HashMap
		 * -------------------------------
		 * KEY'S DEL MAPA RESULTADO
		 * -------------------------------
		 * fechaCierreIngreso0,horaCierreIngreso1
		 * facturasFecha2,cama3,codigoCama4,ingreso5
		 */ 
		public static HashMap consultaDatosFactura (Connection connection,HashMap datos)
		{
			logger.info("\n entre a consultaDatosFactura datos --> "+datos);
			HashMap result = new HashMap ();
			result.put("numRegistros", 0);
			
			HashMap mapa = new HashMap ();
			mapa.put("numRegistros", 0);
			
			String cadena=strconsultaDatosFactura;
			
			
			try 
			{
				logger.info("\n consulta --> "+cadena);
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				//ingreso5
				ps.setInt(1, Utilidades.convertirAEntero(datos.get(indicesFactura[5])+""));
				
				
				
				result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
				
				//Se toman las facturas-----------------------------------------------------------------
				String facturas = "";
				for(int i=0;i<Utilidades.convertirAEntero(result.get("numRegistros")+"");i++)
				{
					if(Utilidades.convertirAEntero(result.get("consecutivoFactura_"+i)+"")>0)
					{
						facturas += (facturas.equals("")?"":", ") + result.get("consecutivoFactura_"+i) + (UtilidadTexto.isEmpty(result.get("fechaFactura_"+i)+"")?"":"-"+result.get("fechaFactura_"+i));
					}
				}
				if(Utilidades.convertirAEntero(result.get("numRegistros")+"")>0)
				{
					mapa.put("numRegistros", 1);
					mapa.put(indicesFactura[2], facturas);
					mapa.put(indicesFactura[0], result.get(indicesFactura[0]+"_0"));
					mapa.put(indicesFactura[1], result.get(indicesFactura[1]+"_0"));
					mapa.put(indicesFactura[3], result.get(indicesFactura[3]+"_0"));
					mapa.put(indicesFactura[4], result.get(indicesFactura[4]+"_0"));
					mapa.put(indicesFactura[12], result.get(indicesFactura[12]+"_0"));
				}
				//-----------------------------------------------------------
			}
			catch (SQLException e)
			{
				logger.info("\n problema en consultaDatosFactura "+e);
			}
			
			return mapa;
		}
		
		
		
		
		
		
		
		
		
	/**
	 * 
	 ***********************************************************************************************/
	
	/**
	 * Implementación del borrado de un egreso que se habia generado automaticamente.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#borrarEgresoAutomatico(Connection, int) 
	 */
	public static int borrarEgresoAutomatico(Connection con, int numeroCuenta) 
	{
		try
		{		
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
		
			PreparedStatementDecorator eliminarEgresoAutomaticoStatement= new PreparedStatementDecorator(con.prepareStatement(eliminarEgresoAutomaticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminarEgresoAutomaticoStatement.setInt(1, numeroCuenta);
			return eliminarEgresoAutomaticoStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error en borrarEgresoAutomatico: "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Revisa que existe un egreso asociado al numero de cuenta dado y que ademas tenga lo datos
	 * de medico, usuario y fecha y hora de egreso llenos.
	 */
	public static boolean existeEgresoEfectivo(Connection con, int numeroCuenta) 
	{
		ResultSetDecorator rs;
		try
		{		
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				logger.error ("Error SQL: Conexión cerrada");
				return false;
			}
		
			PreparedStatementDecorator existeEgresoEfectivoStatement= new PreparedStatementDecorator(con.prepareStatement(existeEgresoEfectivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeEgresoEfectivoStatement.setInt(1, numeroCuenta);
			rs = new ResultSetDecorator(existeEgresoEfectivoStatement.executeQuery());
			if(rs.next()) return true;
			//if(rs.getFetchSize() == 1) return true;
			return false;
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.error("Error en existeEgresoEfectivo: "+e);
			
			return false;
		}
	}
	
	/**
	 * Implementación del método que elimina un preegreso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#borrarPreEgreso(Connection , int ) throws SQLException 
	 */
	public static int borrarPreEgreso(Connection con, int numeroCuenta) 
	{
		try
		{		
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
		
			PreparedStatementDecorator eliminarPreEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(eliminarPreEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminarPreEgresoStatement.setInt(1, numeroCuenta);
			eliminarPreEgresoStatement.executeUpdate();
			return 1;
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.error("Error al tratar de eliminar el pre-egreso de la cuenta "+numeroCuenta+" "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * Implementación de la búsqueda de la causa externa
	 * desde cualquiera de las admisiones.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarCausaExternaUltimaValoracionUH (Connection , int , char ) throws SQLException
	 */
	public static ResultSetDecorator cargarCausaExternaUltimaValoracionUH (Connection con, int idCuenta, char tipoAdmision) throws SQLException
	{
		if (tipoAdmision=='h')
		{
			PreparedStatementDecorator cargarCausaExternaUltimaValoracionHospitalizacionStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCausaExternaUltimaValoracionHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarCausaExternaUltimaValoracionHospitalizacionStatement.setInt(1, idCuenta);
			return new ResultSetDecorator(cargarCausaExternaUltimaValoracionHospitalizacionStatement.executeQuery());
		}
		else
		{
			PreparedStatementDecorator cargarCausaExternaUltimaValoracionUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCausaExternaUltimaValoracionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarCausaExternaUltimaValoracionUrgenciasStatement.setInt(1, idCuenta);
			return new ResultSetDecorator(cargarCausaExternaUltimaValoracionUrgenciasStatement.executeQuery());
		}
	}

	/**
	 * Implementación del método que inserta un preegreso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarPreEgreso(Connection , int , String , String ) throws SQLException 
	 */
	public static int insertarPreEgreso(Connection con, int numeroCuenta, int codigoMedico) 
	{
		try
		{		
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}		
			PreparedStatementDecorator insertarPreEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarPreEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarPreEgresoStatement.setInt(1, numeroCuenta);
			insertarPreEgresoStatement.setInt(2, codigoMedico);
			return insertarPreEgresoStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.error("Error en insertarPreEgreso: "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Método privado que dice, dado el código de la cuenta, si existe un egreso
	 * 
	 * @param con Conexión con la base de datos Genérica
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	private static boolean existeEgreso (Connection con, int idCuenta) throws SQLException
	{
		PreparedStatementDecorator existeEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(existeEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeEgresoStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(existeEgresoStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			throw new SQLException ("La búsqueda de un egreso no dio ningún resultado (egreso)");
		}
	}

	/**
	 * Implementación de creación de egreso desde evolución.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#crearEgresoDesdeEvolucion (Connection , int , int , boolean , int , String , String , int , String , int , String , int , String , int , String , int , String , int , String , String , String ) 
	 */
	public static int crearEgresoDesdeEvolucion (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico,String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		// tipo_identificacion_medico, numero_identificacion_medico) values (1,3,false,1,'otro dest', 'n aut. 2549', 1, 'A000', 10, 'A000', 10, 'A000', 10,'A000', 10,'A000', 10,'CC',1954564);
		int resp0=0, resp1=0, resp2=0;
		 		
		try
		{
			myFactory.beginTransaction(con);
			resp0 = 1;
	
			PreparedStatementDecorator insertarEgresoDesdeEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(insertarEgresoDesdeEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarEgresoDesdeEvolucionStatement.setInt(1, idCuenta);
			insertarEgresoDesdeEvolucionStatement.setInt(2, idEvolucion);
			insertarEgresoDesdeEvolucionStatement.setBoolean(3, estadoSalida);
			insertarEgresoDesdeEvolucionStatement.setInt(4, destinoSalida);
			insertarEgresoDesdeEvolucionStatement.setString(5, otroDestinoSalida);
			if(causaExterna==ConstantesBD.codigoNuncaValido)
				insertarEgresoDesdeEvolucionStatement.setObject(6, null);
			else
				insertarEgresoDesdeEvolucionStatement.setInt(6, causaExterna);
			insertarEgresoDesdeEvolucionStatement.setString(7, acronimoDiagnosticoMuerte);
			insertarEgresoDesdeEvolucionStatement.setInt(8, diagnosticoMuerteCie);
			insertarEgresoDesdeEvolucionStatement.setString(9, acronimoDiagnosticoPrincipal);
			insertarEgresoDesdeEvolucionStatement.setInt(10, diagnosticoPrincipalCie);
			insertarEgresoDesdeEvolucionStatement.setString(11, acronimoDiagnosticoRelacionado1);
			insertarEgresoDesdeEvolucionStatement.setInt(12, diagnosticoRelacionado1Cie);
			insertarEgresoDesdeEvolucionStatement.setString(13, acronimoDiagnosticoRelacionado2);
			insertarEgresoDesdeEvolucionStatement.setInt(14, diagnosticoRelacionado2Cie);
			insertarEgresoDesdeEvolucionStatement.setString(15, acronimoDiagnosticoRelacionado3);
			insertarEgresoDesdeEvolucionStatement.setInt(16, diagnosticoRelacionado3Cie);
			insertarEgresoDesdeEvolucionStatement.setInt(17, codigoMedico);
			if(!acronimoDiagnosticoComplicacion.equals(""))
				insertarEgresoDesdeEvolucionStatement.setString(18,acronimoDiagnosticoComplicacion);
			else
				insertarEgresoDesdeEvolucionStatement.setNull(18,Types.VARCHAR);
			if(diagnosticoComplicacionCie!=ConstantesBD.codigoNuncaValido)
				insertarEgresoDesdeEvolucionStatement.setInt(19,diagnosticoComplicacionCie);
			else
				insertarEgresoDesdeEvolucionStatement.setNull(19,Types.INTEGER);
			
			resp1=insertarEgresoDesdeEvolucionStatement.executeUpdate();
			
			//Primero intentamos actualizar el número de autorización de
			//urgencias, si no sale probamos hospitalizacion
			PreparedStatementDecorator actualizarNumeroAutorizacionAdmision= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarNumeroAutorizacionAdmision.setString(1, numeroAutorizacion);
			actualizarNumeroAutorizacionAdmision.setInt(2, idCuenta);
			
			if (actualizarNumeroAutorizacionAdmision.executeUpdate()<1)
			{
				actualizarNumeroAutorizacionAdmision.close();
				actualizarNumeroAutorizacionAdmision= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				actualizarNumeroAutorizacionAdmision.setString(1, numeroAutorizacion);
				actualizarNumeroAutorizacionAdmision.setInt(2, idCuenta);
				resp2=actualizarNumeroAutorizacionAdmision.executeUpdate();
			}
			else
			{
				//Se hizo la actualizacion de urgencias continuamos sin problema
				resp2=1;
			}
			
			if (resp0 == 0|| resp1 == 0||resp2==0) 
			{
				myFactory.abortTransaction(con);
				resp1=0;
			}
		
		}
		catch (SQLException e)
		{
			myFactory.abortTransaction(con);
			logger.warn("Error ing automatico egreso en evolución. Excepción: " + e.toString());
			throw e;
		}

		return resp1;
	}

	/**
	 * Implementación de la creación de un egreso desde una evolucion,
	 * soportando hacer parte de una transacción.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#crearEgresoDesdeEvolucionTransaccional (Connection , int , int , boolean , int , String , String , int , String , int , String , int , String , int , String , int , String , int , String , String , String ) throws SQLException
	 */
	public static int crearEgresoDesdeEvolucionTransaccional (Connection con, int idCuenta, int idEvolucion, boolean estadoSalida, int destinoSalida, String otroDestinoSalida, String numeroAutorizacion, int causaExterna, String acronimoDiagnosticoMuerte, int diagnosticoMuerteCie, String acronimoDiagnosticoPrincipal, int diagnosticoPrincipalCie, String acronimoDiagnosticoRelacionado1, int diagnosticoRelacionado1Cie, String acronimoDiagnosticoRelacionado2, int diagnosticoRelacionado2Cie, String acronimoDiagnosticoRelacionado3, int diagnosticoRelacionado3Cie, int codigoMedico, String acronimoDiagnosticoComplicacion,int diagnosticoComplicacionCie,String estado) throws SQLException
	{
		int resp0=0, resp1=0, resp=0, resp2=0;

		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{

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
				con.setAutoCommit(false);
			}

			if (estado.equals("empezar"))
			{
				myFactory.beginTransaction(con);
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
		
			logger.info("EXISTE EGRESO????????????????-->"+existeEgreso(con, idCuenta));
			
			if (!existeEgreso(con, idCuenta))
			{
				PreparedStatementDecorator insertarEgresoDesdeEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(insertarEgresoDesdeEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				logger.info("\n\n insertarEgresoDesdeEvolucionStr-->"+insertarEgresoDesdeEvolucionStr+"");
				
				insertarEgresoDesdeEvolucionStatement.setInt(1, idCuenta);
				if(idEvolucion>0)
					insertarEgresoDesdeEvolucionStatement.setInt(2, idEvolucion);
				else
					insertarEgresoDesdeEvolucionStatement.setNull(2, Types.INTEGER);
				insertarEgresoDesdeEvolucionStatement.setBoolean(3, estadoSalida);
				insertarEgresoDesdeEvolucionStatement.setInt(4, destinoSalida);
				insertarEgresoDesdeEvolucionStatement.setString(5, otroDestinoSalida);
				insertarEgresoDesdeEvolucionStatement.setInt(6, causaExterna);
				insertarEgresoDesdeEvolucionStatement.setString(7, acronimoDiagnosticoMuerte);
				insertarEgresoDesdeEvolucionStatement.setInt(8, diagnosticoMuerteCie);
				insertarEgresoDesdeEvolucionStatement.setString(9, acronimoDiagnosticoPrincipal);
				insertarEgresoDesdeEvolucionStatement.setInt(10, diagnosticoPrincipalCie);
				insertarEgresoDesdeEvolucionStatement.setString(11, acronimoDiagnosticoRelacionado1);
				insertarEgresoDesdeEvolucionStatement.setInt(12, diagnosticoRelacionado1Cie);
				insertarEgresoDesdeEvolucionStatement.setString(13, acronimoDiagnosticoRelacionado2);
				insertarEgresoDesdeEvolucionStatement.setInt(14, diagnosticoRelacionado2Cie);
				insertarEgresoDesdeEvolucionStatement.setString(15, acronimoDiagnosticoRelacionado3);
				insertarEgresoDesdeEvolucionStatement.setInt(16, diagnosticoRelacionado3Cie);
				insertarEgresoDesdeEvolucionStatement.setInt(17, codigoMedico);
				if(!acronimoDiagnosticoComplicacion.equals(""))
					insertarEgresoDesdeEvolucionStatement.setString(18,acronimoDiagnosticoComplicacion);
				else
					insertarEgresoDesdeEvolucionStatement.setNull(18,Types.VARCHAR);
				if(diagnosticoComplicacionCie!=ConstantesBD.codigoNuncaValido)
					insertarEgresoDesdeEvolucionStatement.setInt(19,diagnosticoComplicacionCie);
				else
					insertarEgresoDesdeEvolucionStatement.setNull(19,Types.INTEGER);
				
				
				resp1= insertarEgresoDesdeEvolucionStatement.executeUpdate();
				
				logger.info("actualizo?????->"+resp1);
			}
			else
			{
				//Existe el egreso luego debemos actualizar
				PreparedStatementDecorator actualizarEgresoDesdeEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarEgresoDesdeEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("actualizarEgresoDesdeEvolucionStr->"+actualizarEgresoDesdeEvolucionStr);
				
				actualizarEgresoDesdeEvolucionStatement.setInt(1, idEvolucion);
				actualizarEgresoDesdeEvolucionStatement.setBoolean(2, estadoSalida);
				actualizarEgresoDesdeEvolucionStatement.setInt(3, destinoSalida);
				actualizarEgresoDesdeEvolucionStatement.setString(4, otroDestinoSalida);
				actualizarEgresoDesdeEvolucionStatement.setInt(5, causaExterna);
				actualizarEgresoDesdeEvolucionStatement.setString(6, acronimoDiagnosticoMuerte);
				actualizarEgresoDesdeEvolucionStatement.setInt(7, diagnosticoMuerteCie);
				actualizarEgresoDesdeEvolucionStatement.setString(8, acronimoDiagnosticoPrincipal);
				actualizarEgresoDesdeEvolucionStatement.setInt(9, diagnosticoPrincipalCie);
				actualizarEgresoDesdeEvolucionStatement.setString(10, acronimoDiagnosticoRelacionado1);
				actualizarEgresoDesdeEvolucionStatement.setInt(11, diagnosticoRelacionado1Cie);
				actualizarEgresoDesdeEvolucionStatement.setString(12, acronimoDiagnosticoRelacionado2);
				actualizarEgresoDesdeEvolucionStatement.setInt(13, diagnosticoRelacionado2Cie);
				actualizarEgresoDesdeEvolucionStatement.setString(14, acronimoDiagnosticoRelacionado3);
				actualizarEgresoDesdeEvolucionStatement.setInt(15, diagnosticoRelacionado3Cie);
				actualizarEgresoDesdeEvolucionStatement.setInt(16, codigoMedico);
				
				if(!acronimoDiagnosticoComplicacion.equals(""))
					actualizarEgresoDesdeEvolucionStatement.setString(17,acronimoDiagnosticoComplicacion);
				else
					actualizarEgresoDesdeEvolucionStatement.setNull(17,Types.VARCHAR);
				if(diagnosticoComplicacionCie!=ConstantesBD.codigoNuncaValido)
					actualizarEgresoDesdeEvolucionStatement.setInt(18,diagnosticoComplicacionCie);
				else
					actualizarEgresoDesdeEvolucionStatement.setNull(18,Types.INTEGER);
				
				actualizarEgresoDesdeEvolucionStatement.setInt(19, idCuenta);
				
				resp1= actualizarEgresoDesdeEvolucionStatement.executeUpdate();
				
				logger.info("hizo update?????->"+resp1);
				
			}
			
			//Solo en el caso en que se haya ingresado una evolución se actualiza el número de autorizacion
			if(idEvolucion!=ConstantesBD.codigoNuncaValido)
			{
			
				//Primero intentamos actualizar el número de autorización de
				//urgencias, si no sale probamos hospitalizacion
				PreparedStatementDecorator actualizarNumeroAutorizacionAdmision= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				actualizarNumeroAutorizacionAdmision.setString(1, numeroAutorizacion);
				actualizarNumeroAutorizacionAdmision.setInt(2, idCuenta);
				
				if (actualizarNumeroAutorizacionAdmision.executeUpdate()<1)
				{
					actualizarNumeroAutorizacionAdmision.close();
					actualizarNumeroAutorizacionAdmision= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					actualizarNumeroAutorizacionAdmision.setString(1, numeroAutorizacion);
					actualizarNumeroAutorizacionAdmision.setInt(2, idCuenta);
					resp2=actualizarNumeroAutorizacionAdmision.executeUpdate();
				}
				else
				{
					//Se hizo la actualizacion de urgencias continuamos sin problema
					resp2=1;
				}
			}
			else
				resp2 = 1;

			// Terminamos la transaccion, sea con un rollback o un commit.
			if (resp0 == 0|| resp1 == 0 || resp2==0) 
			{
				resp = 0;
				myFactory.abortTransaction(con);
			}
			else
				resp = 1;

			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
				con.setAutoCommit(true);
			}
		}
		catch (SQLException e)
		{
			myFactory.abortTransaction(con);
			logger.warn("Error ing automatico egreso en evolución. Excepción: " + e.toString());
			throw e;
		}
		
		logger.info("inserto al final->"+resp);
		
		return resp;
	}

	/**
	 * Implementación de la modificación del egreso cuando un usuario esta a
	 * punto de finalizarlo (El egreso).
	 * 
	 * @see com.princetonsa.dao.EgresoDao#modificarEgresoUsuarioFinalizarTransaccional (Connection , int , String , String , String , String , String, String , String) throws SQLException 
	 */
	public static int modificarEgresoUsuarioFinalizarTransaccional (Connection con, int idCuenta, String fechaEgreso, String horaEgreso, String fechaGrabacion, String horaGrabacion, String numeroAutorizacion, String loginUsuario, String estado) throws SQLException
	{
		
		int resp0=0, resp1=0, resp2=0;
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		try
		{
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
				con.setAutoCommit(false);
			}

			if (estado.equals("empezar"))
			{
				myFactory.beginTransaction(con);
				resp0=1;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			//statements 
			/**
			 * Cadena constante con el <i>statement</i> necesario para modificar las fechas, el número de autorización y el login de usuario de un egreso  
			 */
			String modificarEgresoUsuarioFinalizarStr="UPDATE egresos set " +
																	" fecha_egreso=to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaEgreso)+"','yyyy-mm-dd'), " +
																	" hora_egreso='"+horaEgreso+"', " +
																	" fecha_grabacion=to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaGrabacion)+"','yyyy-mm-dd'), " +
																	" hora_grabacion='"+horaGrabacion+"', " +
																	" usuario_responsable='"+loginUsuario+"' " +
																	" where cuenta="+idCuenta;

			logger.info("\n\n\n\n\n\n\n\n\nSQL / modificarEgresoUsuarioFinalizarStr / "+modificarEgresoUsuarioFinalizarStr+"\n\n\n\n\n\n\n");
			
			PreparedStatementDecorator modificarEgresoUsuarioFinalizarStatement= new PreparedStatementDecorator(con.prepareStatement(modificarEgresoUsuarioFinalizarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resp1=modificarEgresoUsuarioFinalizarStatement.executeUpdate();
			
			//Primero intentamos actualizar el número de autorización de
			//urgencias, si no sale probamos hospitalizacion
			PreparedStatementDecorator actualizarNumeroAutorizacionAdmision= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarNumeroAutorizacionAdmision.setString(1, numeroAutorizacion);
			actualizarNumeroAutorizacionAdmision.setInt(2, idCuenta);
			
			if (actualizarNumeroAutorizacionAdmision.executeUpdate()<1)
			{
				actualizarNumeroAutorizacionAdmision.close();
				actualizarNumeroAutorizacionAdmision= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				actualizarNumeroAutorizacionAdmision.setString(1, numeroAutorizacion);
				actualizarNumeroAutorizacionAdmision.setInt(2, idCuenta);
				resp2=actualizarNumeroAutorizacionAdmision.executeUpdate();
			}
			else
			{
				//Se hizo la actualizacion de urgencias continuamos sin problema
				resp2=1;
			}


			if (resp0<1||resp1<1||resp2<1)
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
					con.setAutoCommit(true);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
			logger.warn("Excepción en modificarEgresoUsuarioFinalizarTransaccional ",e);
			throw e;
		}
	}

	/**
	 * Implementación del método que completa un semi-egreso en una
	 * Base de datos Genérica.
	 * @param codigoTipoMonitoreo 
	 * 
	 * @see com.princetonsa.dao.EgresoDao#completarSemiEgreso (Connection con, int , String ) throws SQLException 
	 */
	public static int completarSemiEgreso (Connection con, int numeroCuenta, String loginUsuario,String acronimo,int tipocie, int codigoTipoMonitoreo)
	{
		try{
		PreparedStatementDecorator completarSemiEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(completarSemiEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		completarSemiEgresoStatement.setString(1, loginUsuario);
		completarSemiEgresoStatement.setString(2, acronimo);
		completarSemiEgresoStatement.setInt(3, tipocie);
		if(codigoTipoMonitoreo>0)
			completarSemiEgresoStatement.setInt(4, codigoTipoMonitoreo);
		else
			completarSemiEgresoStatement.setNull(4,Types.INTEGER);
		completarSemiEgresoStatement.setInt(5, numeroCuenta);
		
		return completarSemiEgresoStatement.executeUpdate();
		}
		catch(SQLException e){
			logger.warn("Excepción en completarSemiEgreso --- SqlBaseEgresoDao " + e);
			return -1;
		}
	}

	/**
	 * Implementación del método que reversa un semi-egreso en una
	 * Base de datos Genérica.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#reversarSemiEgreso (Connection , int ) throws SQLException
	 */
	public static int reversarSemiEgreso (Connection con, int numeroCuenta) throws SQLException
	{
		String reversarSemiEgresoStr ="UPDATE egresos set usuario_responsable=null, fecha_reversion_egreso=CURRENT_DATE, hora_reversion_egreso="+ValoresPorDefecto.getSentenciaHoraActualBD()+", tipo_monitoreo = null,estado_salida = null where cuenta=?";
		PreparedStatementDecorator reversarSemiEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(reversarSemiEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		reversarSemiEgresoStatement.setInt(1, numeroCuenta);
		return reversarSemiEgresoStatement.executeUpdate();
	}

	/**
	 * Implementación de la actualización de la informacion del motivo
	 * de reversion en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.EgresoDao#actualizarInformacionMotivoReversionTransaccional (con, idCuenta, deboMostrarMotivoReversionEpicrisis, idEvolucion, estado) throws SQLException 
	 */
	public static int actualizarInformacionMotivoReversionTransaccional (Connection con, int idCuenta, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException
	{
		int resp0=0, resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		try
		{
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
				con.setAutoCommit(false);
			}

			if (estado.equals("empezar"))
			{
				myFactory.beginTransaction(con);
				resp0=1;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			//statements 

			PreparedStatementDecorator actualizarInformacionMotivoReversionStatement =  new PreparedStatementDecorator(con.prepareStatement(actualizarInformacionMotivoReversionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarInformacionMotivoReversionStatement.setBoolean(1, deboMostrarMotivoReversionEpicrisis);
			actualizarInformacionMotivoReversionStatement.setInt(2, idEvolucion);
			actualizarInformacionMotivoReversionStatement.setInt(3, idCuenta);
			resp1= actualizarInformacionMotivoReversionStatement.executeUpdate();

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
					con.setAutoCommit(true);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
			logger.warn("Excepción en actualizarInformacionMotivoReversionTransaccional" + e);
			throw e;
		}
	}

	/**
	 * Implementación de la actualización en el egreso, una vez este se 
	 * reverse.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#actualizarPorReversionEgresoTransaccional (Connection , int , int , String , String ) throws SQLException 
	 */
	public static int actualizarPorReversionEgresoTransaccional (Connection con, int idCuenta, int idPersonaRealizaReversion, String motivoReversionEgreso, String estado) throws SQLException
	{
		int resp0=0, resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
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
				con.setAutoCommit(false);
			}

			if (estado.equals("empezar"))
			{
				myFactory.beginTransaction(con);
				resp0=1;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			String actualizarPorReversionEgresoStr="UPDATE egresos SET motivo_reversion=?,codigo_medico=NULL, usuario_responsable=NULL,fecha_reversion_egreso=CURRENT_DATE, hora_reversion_egreso="+ValoresPorDefecto.getSentenciaHoraActualBD()+" ,destino_salida=NULL,otro_destino_salida=null,diagnostico_muerte=1,diagnostico_muerte_cie=0, codigo_persona_reversion=? WHERE cuenta=?";

			PreparedStatementDecorator actualizarPorReversionEgresoStatement =  new PreparedStatementDecorator(con.prepareStatement(actualizarPorReversionEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarPorReversionEgresoStatement.setString(1, motivoReversionEgreso);
			actualizarPorReversionEgresoStatement.setInt(2, idPersonaRealizaReversion);
			actualizarPorReversionEgresoStatement.setInt(3, idCuenta);
			resp1= actualizarPorReversionEgresoStatement.executeUpdate();

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
					con.setAutoCommit(true);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.warn("Excepción en actualizarPorReversionEgresoTransaccional " + e);
			throw e;
		}
	}

	/**
	 * Implementación de la actualización del boolean del motivo
	 * de reversion en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.EgresoDao#actualizarInformacionMotivoReversionSoloBooleanTransaccional (Connection , boolean , int , String ) throws SQLException 
	 */
	public static int actualizarInformacionMotivoReversionSoloBooleanTransaccional (Connection con, boolean deboMostrarMotivoReversionEpicrisis, int idEvolucion, String estado) throws SQLException
	{
		int resp0=0, resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		try
		{
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
				con.setAutoCommit(false);
			}

			if (estado.equals("empezar"))
			{
				myFactory.beginTransaction(con);
				resp0 = 1;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			//statements 

			PreparedStatementDecorator actualizarInformacionMotivoReversionSoloBooleanStatement =  new PreparedStatementDecorator(con.prepareStatement(actualizarInformacionMotivoReversionSoloBooleanStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarInformacionMotivoReversionSoloBooleanStatement.setBoolean(1, deboMostrarMotivoReversionEpicrisis);
			actualizarInformacionMotivoReversionSoloBooleanStatement.setInt(2, idEvolucion);
			resp1= actualizarInformacionMotivoReversionSoloBooleanStatement.executeUpdate();

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
					con.setAutoCommit(true);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.warn("Excepción en actualizarInformacionMotivoReversionSoloBooleanTransaccional " + e);
			throw e;
		}
	}

	/**
	 * Implementación de la búsqueda de número de autorización
	 * desde cualquiera de las admisiones.
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarNumeroAutorizacionAdmision(Connection , int , char ) throws SQLException
	 */
	public static String cargarNumeroAutorizacionAdmision(Connection con, int codigoAdmision, char tipoAdmision) throws SQLException
	{
		if (tipoAdmision=='h')
		{
			PreparedStatementDecorator cargarNumeroAutorizacionAdmisionHospitalizacionStatement= new PreparedStatementDecorator(con.prepareStatement(cargarNumeroAutorizacionAdmisionHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarNumeroAutorizacionAdmisionHospitalizacionStatement.setInt(1, codigoAdmision);
			ResultSetDecorator rs=new ResultSetDecorator(cargarNumeroAutorizacionAdmisionHospitalizacionStatement.executeQuery());
			if (rs.next())
			{
				return rs.getString("numeroAutorizacion");
			}
			else
			{
				throw new SQLException ("La búsqueda del número de autorización en una admisión hospitalaria no dio ningún resultado a pesar que el usuario especifico que debería existir");
			}

		}
		else
		{
			PreparedStatementDecorator cargarNumeroAutorizacionAdmisionUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(cargarNumeroAutorizacionAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarNumeroAutorizacionAdmisionUrgenciasStatement.setInt(1, codigoAdmision);
			ResultSetDecorator rs=new ResultSetDecorator(cargarNumeroAutorizacionAdmisionUrgenciasStatement.executeQuery());
			if (rs.next())
			{
				return rs.getString("numeroAutorizacion");
			}
			else
			{
				throw new SQLException ("La búsqueda del número de autorización en una admisión de urgencias no dio ningún resultado a pesar que el usuario especifico que debería existir");
			}
		}
	}

	/**
	 * Implementación de la inserción automática de un egreso.
	 * @param cieDiagnosticoRelacionado3
	 * @param diagnosticoRelacionado3
	 * @param cieDiagnosticoRelacionado2
	 * @param diagnosticoRelacionado2
	 * @param cieDiagnosticoRelacionado1
	 * @param diagnosticoRelacionado1
	 * @param cieDiagnosticoPrincipal
	 * @param diagnosticoPrincipal
	 * @param cieDiagnosticoMuerte
	 * @param diagnosticoMuerte
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarEgresoAutomatico(Connection, int, String) 
	 */
	public static int insertarEgresoAutomatico(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,int causaExterna) throws SQLException
	{
		logger.info("\n entre a insertarEgresoAutomatico !!!!!!!!!!!!!!!");
		try
		{		
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}	
			UsuarioBasico usuario = new UsuarioBasico();
			usuario.cargarUsuarioBasico(con,loginUsuario);
			
			PreparedStatementDecorator insertarEgresoAutomaticoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarEgresoAutomaticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarEgresoAutomaticoStatement.setInt(1, numeroCuenta);
			insertarEgresoAutomaticoStatement.setString(2, loginUsuario);
			insertarEgresoAutomaticoStatement.setString(3, diagnosticoMuerte);
			insertarEgresoAutomaticoStatement.setInt(4, cieDiagnosticoMuerte);
			insertarEgresoAutomaticoStatement.setString(5, diagnosticoPrincipal);
			insertarEgresoAutomaticoStatement.setInt(6, cieDiagnosticoPrincipal);
			insertarEgresoAutomaticoStatement.setString(7, diagnosticoRelacionado1);
			insertarEgresoAutomaticoStatement.setInt(8, cieDiagnosticoRelacionado1);
			insertarEgresoAutomaticoStatement.setString(9, diagnosticoRelacionado2);
			insertarEgresoAutomaticoStatement.setInt(10, cieDiagnosticoRelacionado2);
			insertarEgresoAutomaticoStatement.setString(11, diagnosticoRelacionado3);
			insertarEgresoAutomaticoStatement.setInt(12, cieDiagnosticoRelacionado3);
			insertarEgresoAutomaticoStatement.setInt(13, usuario.getCodigoPersona());
			insertarEgresoAutomaticoStatement.setBoolean(14, diagnosticoMuerte.equals(ConstantesBD.acronimoDiagnosticoNoSeleccionado)?false:true);
			//Modificacion por tarea 79181 causa externa
			insertarEgresoAutomaticoStatement.setInt(15, causaExterna);
			return insertarEgresoAutomaticoStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.warn(e);
			throw e;
		}
	}
	
	/**
	 * Implementación de la inserción automática de un egreso cuando en una valoración se 
	 * selecciona conducta a seguir hospitalizar en piso
	 * @param cieDiagnosticoRelacionado3
	 * @param diagnosticoRelacionado3
	 * @param cieDiagnosticoRelacionado2
	 * @param diagnosticoRelacionado2
	 * @param cieDiagnosticoRelacionado1
	 * @param diagnosticoRelacionado1
	 * @param cieDiagnosticoPrincipal
	 * @param diagnosticoPrincipal
	 * @param cieDiagnosticoMuerte
	 * @param diagnosticoMuerte
	 * 
	 * @see com.princetonsa.dao.EgresoDao#insertarEgresoAutomatico(Connection, int, String) 
	 */
	public static int insertarEgresoAutomaticoValoracionHospitalizarEnPiso(Connection con, int numeroCuenta, String loginUsuario, String diagnosticoMuerte, int cieDiagnosticoMuerte, String diagnosticoPrincipal, int cieDiagnosticoPrincipal, String diagnosticoRelacionado1, int cieDiagnosticoRelacionado1, String diagnosticoRelacionado2, int cieDiagnosticoRelacionado2, String diagnosticoRelacionado3, int cieDiagnosticoRelacionado3,String fechaEgreso,String horaEgreso, int tipoMonitoreo) throws SQLException
	{
		
		try
		{		
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}		
			
			UsuarioBasico usuario = new UsuarioBasico();
			usuario.cargarUsuarioBasico(con,loginUsuario);
			
			logger.info("sentencia -->"+insertarEgresoAutomaticoValoracionHospitalizarEnPisoStr);
			logger.info("-->"+numeroCuenta+"<--");
			logger.info("-->"+ConstantesBD.codigoDestinoSalidaHospitalizacion+"<--");
			logger.info("-->"+UtilidadFecha.conversionFormatoFechaABD(fechaEgreso)+"<--");
			logger.info("-->"+horaEgreso+"<--");
			logger.info("-->"+UtilidadFecha.getHoraActual(con)+"<--");
			logger.info("-->"+loginUsuario+"<--");
			logger.info("-->"+diagnosticoMuerte+"<--");
			logger.info("-->"+cieDiagnosticoMuerte+"<--");
			logger.info("-->"+diagnosticoPrincipal+"<--");
			logger.info("-->"+cieDiagnosticoPrincipal+"<--");
			logger.info("-->"+diagnosticoRelacionado1+"<--");
			logger.info("-->"+cieDiagnosticoRelacionado1+"<--");
			logger.info("-->"+diagnosticoRelacionado2+"<--");
			logger.info("-->"+cieDiagnosticoRelacionado2+"<--");
			logger.info("-->"+diagnosticoRelacionado3+"<--");
			logger.info("-->"+cieDiagnosticoRelacionado3+"<--");
			logger.info("-->"+usuario.getCodigoPersona()+"<--");
			logger.info("-->"+tipoMonitoreo+"<--");
			PreparedStatementDecorator insertarEgresoAutomaticoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarEgresoAutomaticoValoracionHospitalizarEnPisoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarEgresoAutomaticoStatement.setInt(1, numeroCuenta);
			insertarEgresoAutomaticoStatement.setInt(2, ConstantesBD.codigoDestinoSalidaHospitalizacion);
			insertarEgresoAutomaticoStatement.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaEgreso)));
			insertarEgresoAutomaticoStatement.setString(4, horaEgreso);
			insertarEgresoAutomaticoStatement.setString(5, UtilidadFecha.getHoraActual(con));
			insertarEgresoAutomaticoStatement.setString(6, loginUsuario);			
			insertarEgresoAutomaticoStatement.setString(7, diagnosticoMuerte);
			insertarEgresoAutomaticoStatement.setInt(8, cieDiagnosticoMuerte);
			insertarEgresoAutomaticoStatement.setString(9, diagnosticoPrincipal);
			insertarEgresoAutomaticoStatement.setInt(10, cieDiagnosticoPrincipal);
			insertarEgresoAutomaticoStatement.setString(11, diagnosticoRelacionado1);
			insertarEgresoAutomaticoStatement.setInt(12, cieDiagnosticoRelacionado1);
			insertarEgresoAutomaticoStatement.setString(13, diagnosticoRelacionado2);
			insertarEgresoAutomaticoStatement.setInt(14, cieDiagnosticoRelacionado2);
			insertarEgresoAutomaticoStatement.setString(15, diagnosticoRelacionado3);
			insertarEgresoAutomaticoStatement.setInt(16, cieDiagnosticoRelacionado3);
			insertarEgresoAutomaticoStatement.setInt(17, usuario.getCodigoPersona());
			if(tipoMonitoreo>0)
				insertarEgresoAutomaticoStatement.setInt(18, tipoMonitoreo);
			else
				insertarEgresoAutomaticoStatement.setNull(18,Types.INTEGER);
			
			int valor= insertarEgresoAutomaticoStatement.executeUpdate();
			insertarEgresoAutomaticoStatement.close();
			return valor;
		}
		catch (SQLException e)
		{
			logger.error("error al ingresar",e);
			throw e;
		}
	}

	/**
	 * Implementación de la consulta de datos basicos de un egreso asociado a una cuenta.
	 * @see com.princetonsa.dao.EgresoDao#getBasicoEgreso (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	public static ResultSetDecorator getBasicoEgreso(Connection con, int numeroCuenta) throws SQLException
	{		
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		
		try
		{			
			PreparedStatementDecorator consultarBasicoEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(consultarBasicoEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarBasicoEgresoStatement.setInt(1, numeroCuenta);

			return new ResultSetDecorator(consultarBasicoEgresoStatement.executeQuery());
		} 
		catch (SQLException sql) 
		{
			logger.warn("Error consultando egreso asociado al numero de cuenta "+numeroCuenta+" en la tabla 'egresos'.\n Se lanzó la siguiente excepción:\n"+sql);
			throw sql;
		}
	}

	/**
	 * Implementación de cargar un egreso de modo general
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarEgresoGeneral (Connection , int ) throws SQLException 
	 */
	public static ResultSetDecorator cargarEgresoGeneral (Connection con, int idCuenta) throws SQLException
	{
		PreparedStatementDecorator cargarEgresoGeneralStatement= new PreparedStatementDecorator(con.prepareStatement(cargarEgresoGeneralStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		cargarEgresoGeneralStatement.setInt(1, idCuenta);
		cargarEgresoGeneralStatement.setInt(2, idCuenta);
		cargarEgresoGeneralStatement.setInt(3, idCuenta);
		logger.info("se carga el egreso=> "+cargarEgresoGeneralStr.replace("?", idCuenta+"")+" cuenta=> "+idCuenta);
		return new ResultSetDecorator(cargarEgresoGeneralStatement.executeQuery());
	}
	
	/**
	 * Implementación de cargar un egreso para reversión de egreso
	 * 
	 * @see com.princetonsa.dao.EgresoDao#cargarEgresoReversionEgreso (Connection , int ) throws SQLException 
	 */
	public static ResultSetDecorator cargarEgresoReversionEgreso (Connection con, int idCuenta) throws SQLException
	{
		logger.info("\n entre a cargarEgresoReversionEgreso cuenta -->"+idCuenta);
		logger.info("\n cadena --> "+cargarEgresoReversionEgresoStr.replace("?", idCuenta+""));
		PreparedStatementDecorator cargarEgresoReversionEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarEgresoReversionEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarEgresoReversionEgresoStatement.setInt(1, idCuenta);
		cargarEgresoReversionEgresoStatement.setInt(2, idCuenta);
		cargarEgresoReversionEgresoStatement.setInt(3, idCuenta);
		return new ResultSetDecorator(cargarEgresoReversionEgresoStatement.executeQuery());
	}
	
	/**
	 * Adición de Sebastián
	 * Método que pemrite consultar un semiEgreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static Collection cargarSemiEgreso (Connection con,int idCuenta){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarSemiEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e){
			logger.error("Error Consultando SemiEgreso en SqlBaseEgresoDao: "+e);
			return null;
		}
	}
	
	/**
	 * Adición sebastián
	 * Método que carga los datos básicos de una reversión de un egreso
	 * @param con
	 * @param idCuenta
	 * @return Colección con la fecha y hora del egreso (apenas tiene esos campos)
	 */
	public static Collection cargarReversionEgreso(Connection con,int idCuenta){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarReversionEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e){
			logger.error("Error cargndo reversión Egreso en SqlBaseEgresoDao "+e);
			return null;
		}
	}
	
	/**
	 * Metodo que carga la fecha - hora de egreso
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static ResultSetDecorator cargarFechaHoraEgreso(Connection con, int idCuenta)
	{
	    try
	    {
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarFechaHoraEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e)
		{
			logger.error("Error cargndo fecha - hora Egreso en SqlBaseEgresoDao "+e);
			return null;
		}
	}
	
	/**
	 * Metodo encargado de consultar si existe boleta de salida
	 * @param connection
	 * @param cuenta
	 *  @return false/true
	 */
	public static boolean consultarExisteBoletaSalida(Connection con, int idCuenta){
		
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		
		try{
			pst= new PreparedStatementDecorator(con.prepareStatement(strConsultaExisteBoletaSalida,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getDate("fecha_egreso")!=null && rs.getString("hora_egreso")!=null && rs.getString("usuario_responsable")!=null)
				{					
					return true;
				}else
					return false;
				
			}
			
		}catch(SQLException sqe){
			logger.error("############## SQLException ERROR consultarExisteBoletaSalida",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR consultarExisteBoletaSalida", e);
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
			catch (SQLException se) {
				logger.error("###########  Error close ResultSetDecorator - PreparedStatementDecorator", se);
			}
		}
		return false;
	}
	
}
