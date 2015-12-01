/*
 * @(#)SqlBaseEvolucionDao.java
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
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesValoresPorDefecto;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Evolucion
 *
 *	@version 1.0, Apr 5, 2004
 */
public class SqlBaseEvolucionDao 
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseEvolucionDao.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una entrada en la tabla de evoluciones.
	 */
	private static final String insertarEvolucionStr = "INSERT INTO evoluciones (diagnostico_complicacion, diagnostico_complicacion_cie, fecha_grabacion, hora_grabacion, fecha_evolucion, hora_evolucion, informacion_dada_paciente, desc_complicacion, tratamiento, resultados_tratamiento, cambios_manejo, hallazgos_importantes, proced_quirurgicos_obst, resultado_examenes_diag, pronostico, observaciones, codigo_medico, orden_salida, tipo_evolucion, valoracion, cobrable, recargo, tipo_diagnostico_principal, datos_medico, codigo, centro_costo, conducta_seguir, tipo_referencia) VALUES (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar los datos de las evoluciones asociadas a un mismo numero de cuenta
	 */	
	public static final String consultarDatosBasicosEvolucionesStr = "" +
	"SELECT e.codigo as idEvolucion, to_char(fecha_evolucion,'YYYY-MM-DD')  as fechaEvolucion, to_char(hora_evolucion,'24:mi') as horaEvolucion, cc.codigo as codigoCentroCosto, cc.nombre as nombreCentroCosto, primer_nombre || ' ' || segundo_nombre || ' ' || primer_apellido || ' ' || segundo_apellido as nombreResponsable FROM evoluciones e INNER JOIN  solicitudes s ON (s.numero_solicitud = e.valoracion) INNER JOIN centros_costo cc ON (s.centro_costo_solicitado = cc.codigo ) INNER JOIN personas per  ON (e.codigo_medico = per.codigo) WHERE s.cuenta = ?   order by cc.codigo, e.codigo ASC";
//	"SELECT e.numero_solicitud as idEvolucion, text(fecha_evolucion) as fechaEvolucion, substring(text(hora_evolucion),0,9) as horaEvolucion, cc.codigo as codigoCentroCosto, cc.nombre as nombreCentroCosto " +
//	"FROM evoluciones e, solicitudes_evolucion se, centros_costo cc  " +
//	"WHERE se.id_cuenta = ? AND se.numero_solicitud = e.numero_solicitud AND se.centro_costo = cc.codigo order by cc.codigo, e.numero_solicitud ASC";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los datos base de una evolución, de las tablas solicitudes_evolucion y evoluciones
	 */
//	private static final String cargarEvolucionBaseStr = "SELECT e.codigo, sol.cuenta AS idCuenta, sol.centro_costo_solicitado AS idCentroCosto, e.cobrable AS cobrable, e.diagnostico_complicacion AS acronimoDiagnosticoComplicacin, e.diagnostico_complicacion_cie AS tipoCIEDiagnosticoComplicacion, d.nombre AS nombreDiagnosticoComplicacion, e.fecha_grabacion AS fechaGrabacion, e.hora_grabacion AS horaGrabacion, e.fecha_evolucion AS fechaEvolucion, e.hora_evolucion AS horaEvolucion, e.informacion_dada_paciente AS informacionDadaPaciente, e.desc_complicacion AS descripcionComplicacion, e.tratamiento AS tratamiento, e.resultados_tratamiento AS resultadosTratamiento, e.cambios_manejo AS cambiosManejo, e.hallazgos_importantes AS hallazgosImportantes, e.proced_quirurgicos_obst AS procedimientosQuirurgicosObste, e.resultado_examenes_diag AS fechaYResultadoExamenesDiagnos, e.pronostico AS pronostico, e.observaciones AS observaciones, per.tipo_identificacion AS tipoIdentificacionMedico, per.numero_identificacion AS numeroIdentificacionMedico, e.orden_salida AS ordenSalida, e.tipo_evolucion AS tipoEvolucion, e.valoracion AS codigoValoracion, tr.codigo as codigoRecargo, tr.nombre as nombreRecargo, e.tipo_diagnostico_principal AS tipoDiagnosticoPrincipal, tdp.nombre AS nombreTipoDiagnosticoPrincipal FROM solicitudes sol INNER JOIN evoluciones e ON (sol.numero_solicitud=e.valoracion) INNER JOIN diagnosticos d ON ( e.diagnostico_complicacion = d.acronimo AND e.diagnostico_complicacion_cie = d.tipo_cie ) INNER JOIN personas per ON (e.codigo_medico=per.codigo  ) INNER JOIN tipos_recargo tr ON (e.recargo=tr.codigo) INNER JOIN tipos_diagnostico tdp ON(e.tipo_diagnostico_principal=tdp.codigo) WHERE  e.codigo = ?";
	private static final String cargarEvolucionBaseStr = "SELECT e.codigo, sol.cuenta AS idCuenta, sol.centro_costo_solicitado AS idCentroCosto, e.cobrable AS cobrable, e.diagnostico_complicacion AS acronimoDiagnosticoComplicacin, e.diagnostico_complicacion_cie AS tipoCIEDiagnosticoComplicacion, d.nombre AS nombreDiagnosticoComplicacion, e.fecha_grabacion AS fechaGrabacion, e.hora_grabacion AS horaGrabacion, e.fecha_evolucion AS fechaEvolucion, e.hora_evolucion AS horaEvolucion, e.informacion_dada_paciente AS informacionDadaPaciente, e.desc_complicacion AS descripcionComplicacion, e.tratamiento AS tratamiento, e.resultados_tratamiento AS resultadosTratamiento, e.cambios_manejo AS cambiosManejo, e.hallazgos_importantes AS hallazgosImportantes, getTipoMonitoreo(e.proced_quirurgicos_obst) AS procedimientosQuirurgicosObste, e.resultado_examenes_diag AS fechaYResultadoExamenesDiagnos, e.pronostico AS pronostico, e.observaciones AS observaciones, per.tipo_identificacion AS tipoIdentificacionMedico, per.numero_identificacion AS numeroIdentificacionMedico, e.orden_salida AS ordenSalida, e.tipo_evolucion AS tipoEvolucion, e.valoracion AS codigoValoracion, tr.codigo as codigoRecargo, tr.nombre as nombreRecargo, e.tipo_diagnostico_principal AS tipoDiagnosticoPrincipal, tdp.nombre AS nombreTipoDiagnosticoPrincipal FROM solicitudes sol INNER JOIN evoluciones e ON (sol.numero_solicitud=e.valoracion) INNER JOIN diagnosticos d ON ( e.diagnostico_complicacion = d.acronimo AND e.diagnostico_complicacion_cie = d.tipo_cie ) INNER JOIN personas per ON (e.codigo_medico=per.codigo  ) INNER JOIN tipos_recargo tr ON (e.recargo=tr.codigo) INNER JOIN tipos_diagnostico tdp ON(e.tipo_diagnostico_principal=tdp.codigo) WHERE  e.codigo = ?";
//	private static final String cargarEvolucionBaseStr = "SELECT e.codigo, sol.cuenta AS idCuenta, sol.centro_costo_solicitado AS idCentroCosto, e.cobrable AS cobrable, e.diagnostico_complicacion AS acronimoDiagnosticoComplicacin, e.diagnostico_complicacion_cie AS tipoCIEDiagnosticoComplicacion, d.nombre AS nombreDiagnosticoComplicacion, e.fecha_grabacion AS fechaGrabacion, e.hora_grabacion AS horaGrabacion, e.fecha_evolucion AS fechaEvolucion, e.hora_evolucion AS horaEvolucion, e.informacion_dada_paciente AS informacionDadaPaciente, e.desc_complicacion AS descripcionComplicacion, e.tratamiento AS tratamiento, e.resultados_tratamiento AS resultadosTratamiento, e.cambios_manejo AS cambiosManejo, e.hallazgos_importantes AS hallazgosImportantes, getTipoMonitoreo(CAST (CASE WHEN e.proced_quirurgicos_obst = '' THEN NULL ELSE e.proced_quirurgicos_obst || '' END AS INTEGER ) )  AS procedimientosQuirurgicosObste, e.resultado_examenes_diag AS fechaYResultadoExamenesDiagnos, e.pronostico AS pronostico, e.observaciones AS observaciones, per.tipo_identificacion AS tipoIdentificacionMedico, per.numero_identificacion AS numeroIdentificacionMedico, e.orden_salida AS ordenSalida, e.tipo_evolucion AS tipoEvolucion, e.valoracion AS codigoValoracion, tr.codigo as codigoRecargo, tr.nombre as nombreRecargo, e.tipo_diagnostico_principal AS tipoDiagnosticoPrincipal, tdp.nombre AS nombreTipoDiagnosticoPrincipal FROM solicitudes sol INNER JOIN evoluciones e ON (sol.numero_solicitud=e.valoracion) INNER JOIN diagnosticos d ON ( e.diagnostico_complicacion = d.acronimo AND e.diagnostico_complicacion_cie = d.tipo_cie ) INNER JOIN personas per ON (e.codigo_medico=per.codigo  ) INNER JOIN tipos_recargo tr ON (e.recargo=tr.codigo) INNER JOIN tipos_diagnostico tdp ON(e.tipo_diagnostico_principal=tdp.codigo) WHERE  e.codigo = ?";
	/*
	 * Armando hizo esto para hacer un castin de VARCHAR a entero, pero ahora le cambió el tipo de dato a entero, entonces el casting yano es necesario
	 */
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los diagnósticos de una evolución, de la tabla evol_diagnosticos.
	 */
	private static final String cargarEvolucionDiagnosticosStr = "SELECT acronimo_diagnostico AS acronimo, tipo_cie_diagnostico AS tipoCIE, numero, nombre, principal, definitivo FROM evol_diagnosticos, diagnosticos WHERE acronimo = acronimo_diagnostico AND tipo_cie = tipo_cie_diagnostico AND evolucion = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los signos vitales de una evolución, de la tabla evol_signos_vitales.
	 */
	private static final String cargarSignoVitalStr = "SELECT signo_vital AS codigo, descripcion, unidad_medida AS unidadMedida, valor AS valorSignoVital, nombre FROM evol_signos_vitales, signos_vitales WHERE signo_vital = codigo AND evolucion = ?";
	
	/**
	 * Sentencia para cargar el Balance de liquidos
	 */
	private static final String cargarBalanceLiquidosStr="SELECT b.nombre AS nombre, e.balance_liq_evol_cc_ins AS codigo, e.valor AS valor FROM balance_liquidos_evol e INNER JOIN balance_liq_evol_cc_ins i ON(i.codigo=e.balance_liq_evol_cc_ins) INNER JOIN tipo_balance_liq_evol b ON(b.codigo=i.tipo_balance_liq_evol)WHERE evolucion=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar numero de la cuenta asociada a un numero de solicitud dado.
	 */	
	private static final String consultarIdCuentaStr ="" +
	"SELECT sol.cuenta as idCuenta FROM solicitudes sol, evoluciones evol WHERE evol.valoracion=sol.numero_solicitud and evol.codigo = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar la fecha y hora de la evolución que dio la orden de salida.
	 */	
	private static final String consultarFechaEvolucionSalidaStr = "" +
	"SELECT fecha_evolucion, hora_evolucion FROM evoluciones e, solicitudes sol  WHERE e.valoracion = sol.numero_solicitud AND sol.cuenta = ? AND e.orden_salida = " + ValoresPorDefecto.getValorTrueParaConsultas() + "";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una entrada en la tabla de evol_diagnosticos.
	 */
	private static final String insertarEvolucionDiagnosticoStr = "INSERT INTO evol_diagnosticos (evolucion, acronimo_diagnostico, tipo_cie_diagnostico, numero, principal, definitivo) VALUES (?, ?, ?, ?, ?, ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una entrada en la tabla de evol_signos_vitales.
	 */
	private static final String insertarEvolucionSignoVitalStr = "INSERT INTO evol_signos_vitales (evolucion, signo_vital, descripcion, valor) VALUES (?, ?, ?, ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * si se debe decir que esta evolución fué la primera después que
	 * se canceló la solicitud de médico tratante al centro de costo
	 * del médico. Porqué no se necesita el centro de costo? debido 
	 * a que buscamos por la valoración asociada, y esta solo puede
	 * asociarse a una del mismo centro de costo 
	 */
	private static final String consultaCancelacionTratanteStr=" select ev.codigo from sol_cambio_tratante sct INNER JOIN cancelacion_tratantes cant ON (sct.codigo=cant.codigo) INNER JOIN evoluciones ev ON (sct.solicitud=ev.valoracion) INNER JOIN evoluciones ev2 ON (ev.valoracion=ev2.valoracion) where activa=" + ValoresPorDefecto.getValorFalseParaConsultas() + " and ((cant.fecha=ev.fecha_grabacion and substr(cant.hora,0,6)<ev.hora_grabacion) or (cant.fecha<ev.fecha_grabacion) ) and ev2.codigo=? order by ev.fecha_grabacion ASC, ev.hora_grabacion ASC";
	
	/**
	 * Sentencia para ingresar el balance de liquidos
	 */
	private static final String insertarBalanceLiquidos="INSERT INTO balance_liquidos_evol (evolucion, balance_liq_evol_cc_ins, valor) VALUES(?,?,?)";
	
	/**
	 * Método privado, carga lo que se le indique en el parámetro which.
	 * @param con conexión abierta con una base de datos Genérica
	 * @param codigo número de solicitud de la evolución o parte de la evolución que se desea cargar
	 * @param which indica qué parte de la evolución se va a cargar. Posibles valores son : </br>
	 * <ul>
	 *   <li>cargarEvolucionBaseStr : se cargan los datos de solicitudes_evolucion y evoluciones</li> 
	 *   <li>cargarEvolucionDiagnosticosStr : se cargan los datos de evol_diagnosticos</li>
	 *   <li>cargarSignoVitalStr : evol_signos_vitales</li>
	 * </ul>
	 * @return <code>ResultSet</code> con los resultados de la consulta
	 */
	private static ResultSetDecorator cargar(Connection con, int codigo, String which) throws SQLException {
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(which,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setInt(1, codigo);
		return new ResultSetDecorator(cargar.executeQuery());
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionBase(Connection, int)
	 */
	public static ResultSetDecorator cargarEvolucionBase(Connection con, int codigo) throws SQLException 
	{
		return cargar(con, codigo, cargarEvolucionBaseStr);
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionDiagnosticos(Connection, int)
	 */
	public static ResultSetDecorator cargarEvolucionDiagnosticos(Connection con, int codigo) throws SQLException {
		return cargar(con, codigo, cargarEvolucionDiagnosticosStr);
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionSignosVitales(Connection, int)
	 */
	public static ResultSetDecorator cargarEvolucionSignosVitales(Connection con, int codigo) throws SQLException {
		return cargar(con, codigo, cargarSignoVitalStr);
	}


	/**
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionSignosVitales(Connection, int)
	 */
	public static ResultSetDecorator cargarEvolucionBalanceLiquidos(Connection con, int codigo) throws SQLException {
		return cargar(con, codigo, cargarBalanceLiquidosStr);
	}

	/**
	 * Implementación de la consulta del numero de cuenta asociado a la evolucion con numero de solicitud dado.
	 * @see com.princetonsa.dao.EvolucionDao#getIdCuenta (con, int ) throws SQLException
	 * @version Sept. 24 de 2003
	 */	
	public static int getIdCuenta(Connection con, int codigo) throws SQLException
	{
		ResultSetDecorator rs;
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		
		try
		{
			PreparedStatementDecorator consultarIdCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(consultarIdCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarIdCuentaStatement.setInt(1, codigo);
			rs = new ResultSetDecorator(consultarIdCuentaStatement.executeQuery());
			if(rs.next()) 
				return rs.getInt("idCuenta");
					
			return -1;
		} 
		catch (SQLException sql) 
		{
			logger.warn("Error consultando la cuenta asociada a la evolucion con numero de solicitud "+codigo+" en la tabla 'solicitudes_evolucion'.\n Se lanzó la siguiente excepción:\n"+sql);
			throw sql;
		}	
	}	

	/**
	 * @see com.princetonsa.dao.EvolucionDao#consultarFechaEvolucionSalida(Connection, int)
	 */	
	public static ResultSetDecorator consultarFechaEvolucionSalida(Connection con, int numeroCuenta) throws SQLException {
		PreparedStatementDecorator consultarFechaEvolucionSalidaStatement =  new PreparedStatementDecorator(con.prepareStatement(consultarFechaEvolucionSalidaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultarFechaEvolucionSalidaStatement.setInt(1, numeroCuenta);
		return new ResultSetDecorator(consultarFechaEvolucionSalidaStatement.executeQuery());				
	}

	/**
	 * @param datosMedico @todo
	 * @see com.princetonsa.dao.EvolucionDao#int insertar(Connection , int , int , boolean , String , int , String , String , String , String , String , String , String , String , String , String , String , String , String , String , boolean , Collection , Collection , Collection , int , int ) throws SQLException
	 */
	public static int insertar(Connection con, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, int tipoDiagnosticoPrincipal, String datosMedico, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException 
	{
		if (horaEvolucion.length()<=5)
		{
			horaEvolucion=horaEvolucion+ ":00";
		}

		int resp = 0, codigo = -1;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean resp0 = false, resp2 = false,
				resp3 = false, resp4 = false, resp5 = false;  // Estos valores son true si todo salió bien

		/* Iniciamos la transacción */
		resp0 = myFactory.beginTransaction(con);

		/* Consultamos el número con el que fue insertada esta evolución */
		
		codigo=myFactory.incrementarValorSecuencia(con, "seq_evolucion");

		/* Inserción de datos en evoluciones */

		if (diagnosticoComplicacion == null || diagnosticoComplicacionCIE == -1) {
			diagnosticoComplicacion = "1";
			diagnosticoComplicacionCIE = 0;
		}


		PreparedStatementDecorator insertarEvolucion =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		insertarEvolucion.setString(1, diagnosticoComplicacion);
		insertarEvolucion.setInt(2, diagnosticoComplicacionCIE);
		insertarEvolucion.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaEvolucion));
		insertarEvolucion.setString(4, horaEvolucion);
		insertarEvolucion.setString(5, informacionDadaPaciente);
		insertarEvolucion.setString(6, descripcionComplicacion);
		insertarEvolucion.setString(7, tratamiento);
		insertarEvolucion.setString(8, resultadosTratamiento);
		insertarEvolucion.setString(9, cambiosManejo);
		insertarEvolucion.setString(10, hallazgosImportantes);
		insertarEvolucion.setString(11, procedimientosQuirurgicosObstetricos);
		insertarEvolucion.setString(12, fechaYResultadoExamenesDiagnostico);
		insertarEvolucion.setString(13, pronostico);
		insertarEvolucion.setString(14, observaciones);
		insertarEvolucion.setInt(15, codigoMedico);
		insertarEvolucion.setBoolean(16, ordenSalida);
		insertarEvolucion.setInt(17, tipoEvolucion);
		insertarEvolucion.setInt(18, codigoValoracion);
		insertarEvolucion.setBoolean(19, cobrable);
		insertarEvolucion.setInt(20, recargo);
		insertarEvolucion.setInt(21, tipoDiagnosticoPrincipal);		
		insertarEvolucion.setInt(21, tipoDiagnosticoPrincipal);
		insertarEvolucion.setString(22, datosMedico);
		insertarEvolucion.setInt(23, codigo);
		insertarEvolucion.setInt(24, centroCostoAreaPaciente);
		
		if(codigoConductaASeguir==ConstantesBD.codigoNuncaValido)
		{	
			insertarEvolucion.setObject(25, null);
			insertarEvolucion.setObject(26, null);
		}	
		else
		{	
			insertarEvolucion.setInt(25, codigoConductaASeguir);
			if(codigoConductaASeguir==ConstantesBD.codigoConductaASeguirRemitirEvolucion)
				insertarEvolucion.setString(26, acronimoTipoReferencia);
			else
				insertarEvolucion.setObject(26, null);
		}	
		
		try {
			resp3 = (insertarEvolucion.executeUpdate() > 0);
		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		insertarEvolucion.close();

		/* Inserción de diagnósticos presuntivos y definitivos en evol_diagnosticos */
		PreparedStatementDecorator insertarEvolucionDiagnostico =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		Iterator iter = diagnosticosPresuntivos.iterator();
		Diagnostico diagnostico = null;
		int numeroDiagnostico = 1;

		while (iter.hasNext()) {

			diagnostico = (Diagnostico) iter.next();
			insertarEvolucionDiagnostico.clearParameters();

			insertarEvolucionDiagnostico.setInt(1, codigo);
			insertarEvolucionDiagnostico.setString(2, diagnostico.getAcronimo());
			insertarEvolucionDiagnostico.setInt(3, diagnostico.getTipoCIE());

			if (!diagnostico.isPrincipal()) {
				insertarEvolucionDiagnostico.setInt(4, numeroDiagnostico++);
			}
			else {
				insertarEvolucionDiagnostico.setInt(4, -1);
			}

			insertarEvolucionDiagnostico.setBoolean(5, diagnostico.isPrincipal());
			insertarEvolucionDiagnostico.setBoolean(6, false);

			insertarEvolucionDiagnostico.addBatch();

		}

		iter = diagnosticosDefinitivos.iterator();
		numeroDiagnostico = 1;

		while (iter.hasNext()) {

			diagnostico = (Diagnostico) iter.next();
			insertarEvolucionDiagnostico.clearParameters();

			insertarEvolucionDiagnostico.setInt(1, codigo);
			insertarEvolucionDiagnostico.setString(2, diagnostico.getAcronimo());
			insertarEvolucionDiagnostico.setInt(3, diagnostico.getTipoCIE());

			if (!diagnostico.isPrincipal()) {
				insertarEvolucionDiagnostico.setInt(4, numeroDiagnostico++);
			}
			else {
				insertarEvolucionDiagnostico.setInt(4, -1);
			}

			insertarEvolucionDiagnostico.setBoolean(5, diagnostico.isPrincipal());
			insertarEvolucionDiagnostico.setBoolean(6, true);

			insertarEvolucionDiagnostico.addBatch();

		}

		try {

			int [] batchResult = insertarEvolucionDiagnostico.executeBatch();
			resp4 = true;

			if (batchResult.length != 0) {
				for (int i=0, size=batchResult.length; i<size; i++) {
					resp4 = (batchResult[i] > 0);
					if (!resp4) break;
				}
			}

		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		insertarEvolucionDiagnostico.close();

		/* Inserción de datos en evol_signos_vitales */
		PreparedStatementDecorator insertarEvolucionSignoVital =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionSignoVitalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		iter = signosVitales.iterator();
		SignoVital signoVital = null;

		while (iter.hasNext()) {

			signoVital = (SignoVital) iter.next();
			insertarEvolucionSignoVital.clearParameters();

			insertarEvolucionSignoVital.setInt(1, codigo);
			
			insertarEvolucionSignoVital.setInt(2, signoVital.getCodigo());
			insertarEvolucionSignoVital.setString(3, signoVital.getDescripcion());
			insertarEvolucionSignoVital.setString(4, signoVital.getValorSignoVital());

			insertarEvolucionSignoVital.addBatch();

		}

		try {

			int [] batchResult = insertarEvolucionSignoVital.executeBatch();
			resp5 = true;

			if (batchResult.length != 0) {
				for (int i=0, size=batchResult.length; i<size; i++) {
					resp5 = (batchResult[i] > 0);
					if (!resp5) break;
				}
			}

		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		insertarEvolucionSignoVital.close();

		/* Terminamos la transacción, bien sea con un commit o un rollback */
		if (resp0 && resp2 && resp3 && resp4 && resp5) {
			myFactory.endTransaction(con);
			resp = codigo;
		}

		else {
			myFactory.abortTransaction(con);
			resp = 0;
		}				
		
		/* Retornamos el número de la solicitud de evolución, o 0 si se hizo rollback */
		return resp;

	}

	/**
	 * @param balanceLiquidos @todo
	 * @see com.princetonsa.dao.EvolucionDao#insertarTransaccional(Connection , String , int , int , boolean , String , int , String , String , String , String , String , String , String , String , String , String , String , String , String , String , boolean , Collection , Collection , Collection , int , String, int) throws SQLException 
	 */
	public static  int insertarTransaccional(Connection con, String estado, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, String nombreSecuencia, int tipoDiagnosticoPrincipal, String datosMedico,Vector balanceLiquidos, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException 
	{
		int resp = 0, codigo = -1;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean resp0 = false, 
				resp3 = false, resp4 = false, resp5 = false;   // Estos valores son true si todo salió bien

		/* Iniciamos la transacción */
		if(estado.equals("empezar")) {
			resp0 = myFactory.beginTransaction(con);
		}
		else {
			resp0 = true;
		}

		
		/* Consultamos el número con el que fue insertada esta evolución */
		
		codigo=myFactory.incrementarValorSecuencia(con, "seq_evolucion");


		/* Inserción de datos en evoluciones */

		if (diagnosticoComplicacion == null || diagnosticoComplicacionCIE == -1) {
			diagnosticoComplicacion = "1";
			diagnosticoComplicacionCIE = 0;
		}

		PreparedStatementDecorator insertarEvolucion =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		insertarEvolucion.setString(1, diagnosticoComplicacion);
		insertarEvolucion.setInt(2, diagnosticoComplicacionCIE);
		insertarEvolucion.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaEvolucion));
		insertarEvolucion.setString(4, horaEvolucion);
		insertarEvolucion.setString(5, informacionDadaPaciente);
		insertarEvolucion.setString(6, descripcionComplicacion);
		insertarEvolucion.setString(7, tratamiento);
		insertarEvolucion.setString(8, resultadosTratamiento);
		insertarEvolucion.setString(9, cambiosManejo);
		insertarEvolucion.setString(10, hallazgosImportantes);
		insertarEvolucion.setString(11, procedimientosQuirurgicosObstetricos);
		insertarEvolucion.setString(12, fechaYResultadoExamenesDiagnostico);
		insertarEvolucion.setString(13, pronostico);
		insertarEvolucion.setString(14, observaciones);
		insertarEvolucion.setInt(15, codigoMedico);
		insertarEvolucion.setBoolean(16, ordenSalida);
		insertarEvolucion.setInt(17, tipoEvolucion);
		insertarEvolucion.setInt(18, codigoValoracion);
		insertarEvolucion.setBoolean(19, cobrable);
		insertarEvolucion.setInt(20, recargo);
		insertarEvolucion.setInt(21, tipoDiagnosticoPrincipal);
		insertarEvolucion.setString(22, datosMedico);
		insertarEvolucion.setInt(23, codigo);
		insertarEvolucion.setInt(24, centroCostoAreaPaciente);
		
		if(codigoConductaASeguir==ConstantesBD.codigoNuncaValido)
		{	
			insertarEvolucion.setObject(25, null);
			insertarEvolucion.setObject(26, null);
		}	
		else
		{	
			insertarEvolucion.setInt(25, codigoConductaASeguir);
			if(codigoConductaASeguir==ConstantesBD.codigoConductaASeguirRemitirEvolucion)
				insertarEvolucion.setString(26, acronimoTipoReferencia);
			else
				insertarEvolucion.setObject(26, null);
		}	
		
		try {
			resp3 = (insertarEvolucion.executeUpdate() > 0);
		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		insertarEvolucion.close();

		/* Inserción de diagnósticos presuntivos y definitivos en evol_diagnosticos */
		PreparedStatementDecorator insertarEvolucionDiagnostico =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		Iterator iter = diagnosticosPresuntivos.iterator();
		Diagnostico diagnostico = null;
		int numeroDiagnostico = 1;
		
		resp4=true;

		//los presuntivos no se deben grabar, ya no deben existir
		/*while (iter.hasNext()) {

			diagnostico = (Diagnostico) iter.next();
			insertarEvolucionDiagnostico.clearParameters();

			insertarEvolucionDiagnostico.setInt(1, codigo);
			insertarEvolucionDiagnostico.setString(2, diagnostico.getAcronimo());
			insertarEvolucionDiagnostico.setInt(3, diagnostico.getTipoCIE());

			if (!diagnostico.isPrincipal()) {
				insertarEvolucionDiagnostico.setInt(4, numeroDiagnostico++);
			}
			else {
				insertarEvolucionDiagnostico.setInt(4, -1);
			}

			insertarEvolucionDiagnostico.setBoolean(5, diagnostico.isPrincipal());
			insertarEvolucionDiagnostico.setBoolean(6, false);

			try
			{
				if(insertarEvolucionDiagnostico.executeUpdate()<0)
				{
					resp4=false;
				}
			}
			catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
			}

		}*/

		iter = diagnosticosDefinitivos.iterator();
		numeroDiagnostico = 1;

		while (iter.hasNext()) {
			
			diagnostico = (Diagnostico) iter.next();
			insertarEvolucionDiagnostico.clearParameters();

			insertarEvolucionDiagnostico.setInt(1, codigo);
			insertarEvolucionDiagnostico.setString(2, diagnostico.getAcronimo());
			insertarEvolucionDiagnostico.setInt(3, diagnostico.getTipoCIE());

			if (!diagnostico.isPrincipal()) {
				insertarEvolucionDiagnostico.setInt(4, numeroDiagnostico++);
			}
			else {
				insertarEvolucionDiagnostico.setInt(4, -1);
			}

			insertarEvolucionDiagnostico.setBoolean(5, diagnostico.isPrincipal());
			insertarEvolucionDiagnostico.setBoolean(6, true);

			try
			{
				if(insertarEvolucionDiagnostico.executeUpdate()<0)
				{
					resp4=false;
				}
			}
			catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
			}
		}


		insertarEvolucionDiagnostico.close();

		/* Inserción de datos en evol_signos_vitales */
		PreparedStatementDecorator insertarEvolucionSignoVital =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionSignoVitalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		iter = signosVitales.iterator();
		SignoVital signoVital = null;

		while (iter.hasNext()) {

			signoVital = (SignoVital) iter.next();
			insertarEvolucionSignoVital.clearParameters();

			insertarEvolucionSignoVital.setInt(1, codigo);
			insertarEvolucionSignoVital.setInt(2, signoVital.getCodigo());
			insertarEvolucionSignoVital.setString(3, signoVital.getDescripcion());
			insertarEvolucionSignoVital.setString(4, signoVital.getValorSignoVital());

			insertarEvolucionSignoVital.addBatch();

		}

		try {

			int [] batchResult = insertarEvolucionSignoVital.executeBatch();
			resp5 = true;

			if (batchResult.length != 0) {
				for (int i=0, size=batchResult.length; i<size; i++) {
					resp5 = (batchResult[i] > 0);
					if (!resp5) break;
				}
			}

		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		insertarEvolucionSignoVital.close();
		
		PreparedStatementDecorator insertarEvolucionBalance= new PreparedStatementDecorator(con.prepareStatement(insertarBalanceLiquidos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarEvolucionBalance.setInt(1, codigo);
		for(int i=0; i<balanceLiquidos.size();i++)
		{
			Vector fila=(Vector)balanceLiquidos.elementAt(i);
			insertarEvolucionBalance.setInt(2, Integer.parseInt(fila.elementAt(0)+""));
			insertarEvolucionBalance.setString(3, fila.elementAt(1)+"");
			int numBalanceInsertado=insertarEvolucionBalance.executeUpdate();
		}

		
		/* Si debemos finalizar la transacción, salimos bien sea con un commit o un rollback */

		if (resp0 && resp3 && resp4 && resp5) 
		{
			if (estado.equals("finalizar")) 
			{
				myFactory.endTransaction(con);
			}
			resp = codigo;
		}

		else 
		{
			myFactory.abortTransaction(con);
			resp = 0;
		}

		/* Retornamos el número de la solicitud de evolución, o 0 si se hizo rollback */
		return resp;

	}

	/**
	 * Implementación de la consulta de datos basicos de las evoluciones que estan asociadas al mismo numero de cuenta
	 * @see com.princetonsa.dao.EvolucionDao#getBasicoEvolucionesRespondidas (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	 
	public static ResultSetDecorator getBasicoEvolucionesRespondidas(Connection con, int numeroCuenta) throws SQLException
	{		                                             
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		
		try
		{			
			PreparedStatementDecorator consultarDatosBasicosEvolucionesStatement= new PreparedStatementDecorator(con.prepareStatement(consultarDatosBasicosEvolucionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarDatosBasicosEvolucionesStatement.setInt(1, numeroCuenta);
			return new ResultSetDecorator(consultarDatosBasicosEvolucionesStatement.executeQuery());
		} 
		catch (SQLException sql) 
		{
			logger.warn("Error consultando evoluciones asociadas al numero de cuenta "+numeroCuenta+" en la tabla 'evoluciones'.\n Se lanzó la siguiente excepción:\n"+sql);
			throw sql;
		}
	}
	
	/**
	 * Implementación del método que revisa si esta es la 
	 * primera evolución que llenó el usuario después que
	 * se cancelo una solicitud de cambio de tratante para
	 * una BD Genérica
	 *
	 * @see com.princetonsa.dao.EvolucionDao#deboMostrarCancelacionTratante (Connection , int ) throws SQLException
	 */
	public static boolean deboMostrarCancelacionTratante (Connection con, int idEvolucion) throws SQLException
	{
	    PreparedStatementDecorator consultaCancelacionTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(consultaCancelacionTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    consultaCancelacionTratanteStatement.setInt(1, idEvolucion);
	    ResultSetDecorator rs=new ResultSetDecorator(consultaCancelacionTratanteStatement.executeQuery());
	    if (rs.next())
	    {
	        int respuesta=rs.getInt("codigo");
	        rs.close();
	        if (respuesta==idEvolucion)
	        {
	            return true;
	        }
	        else
	        {
	            return false;
	        }
	    }
	    else
	    {
	        rs.close();
	        return false;
	    }
	    
	}
	
	
	/**
	 * mapa que carga la informacion de conducta a seguir de la utltinma evolucion insertada,
	 * en caso de no encontrar informacion retorna null
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap conductaASeguirUltimaInsertada(Connection con, int idCuenta) 
	{
		try
		{
			String fechaHoraUltimaEvol[]={"",""};
			fechaHoraUltimaEvol=UtilidadValidacion.obtenerMaximaFechaYHoraEvolucion(con, idCuenta);
			if(!UtilidadTexto.isEmpty(fechaHoraUltimaEvol[0]) || !UtilidadTexto.isEmpty(fechaHoraUltimaEvol[1]))
			{	
				String obtenerConductaStr=	"select " +
												"case when e.conducta_seguir is not null then e.conducta_seguir else "+ConstantesBD.codigoNuncaValido+" end as codigoconductaseguir, " +
												"c.descripcion as descripcionconductaseguir, " +
												"case when e.tipo_referencia is null then '' else e.tipo_referencia end as acronimotiporeferencia, " +
												"case when e.tipo_referencia is null then '' else getintegridaddominio(e.tipo_referencia) end as tiporeferencia " +
											"from " +
												"evoluciones e " +
												"inner join solicitudes sol on (sol.numero_solicitud=e.valoracion) " +
												"inner join conductas_seguir_evolucion c on(c.codigo=e.conducta_seguir) " +
											"where " +
												"sol.cuenta=? " +
												"and e.fecha_evolucion= '"+fechaHoraUltimaEvol[0]+"' " +
												"and hora_evolucion='"+fechaHoraUltimaEvol[1]+"' ";
				
				logger.info("CONDUCTA ->"+obtenerConductaStr+" cuenta->"+idCuenta);
				
				PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(obtenerConductaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, idCuenta);
				ResultSetDecorator rs=new ResultSetDecorator(statement.executeQuery());
				return UtilidadBD.cargarValueObject(rs, false, false);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en conductaASeguirUltimaInsertada de SqlBaseUtilidadValidacionDao:"+e);
		}
		catch (Exception e) 
		{
			logger.warn("no existe fecha ultima evolucion.");
		}
		return null;
	}
	
	/**
	 * mapa que contiene las conductas a seguir
	 * @param con
	 * @return
	 */
	public static HashMap conductasASeguirMap(Connection con) 
	{
		HashMap mapaConductas= new HashMap();
		mapaConductas.put("numRegistros", "0");
		String consulta="SELECT codigo, descripcion from conductas_seguir_evolucion order by descripcion";
		PreparedStatementDecorator statement;
		try 
		{
			statement =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapaConductas=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapaConductas;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static HashMap conductaASeguirDadaEvol(Connection con, int codigoEvolucion) 
	{
		try
		{
			String obtenerConductaStr=	"select " +
											"case when e.conducta_seguir is not null then e.conducta_seguir else "+ConstantesBD.codigoNuncaValido+" end as codigoconductaseguir, " +
											"c.descripcion as descripcionconductaseguir, " +
											"case when e.tipo_referencia is null then '' else e.tipo_referencia end as acronimotiporeferencia, " +
											"case when e.tipo_referencia is null then '' else getintegridaddominio(e.tipo_referencia) end as tiporeferencia " +
										"from " +
											"evoluciones e " +
											"inner join solicitudes sol on (sol.numero_solicitud=e.valoracion) " +
											"inner join conductas_seguir_evolucion c on(c.codigo=e.conducta_seguir) " +
										"where " +
											"e.codigo=? ";
												
				
				logger.info("CONDUCTA ->"+obtenerConductaStr+" evol->"+codigoEvolucion);
				
				PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(obtenerConductaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, codigoEvolucion);
				ResultSetDecorator rs=new ResultSetDecorator(statement.executeQuery());
				return UtilidadBD.cargarValueObject(rs, false, false);
		}
		catch(SQLException e)
		{
			logger.error("Error en conductaASeguir");
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static String consultarParametroInterpretacion(Connection con) 
	{
		String consultarParametroGeneral = "SELECT valor from valores_por_defecto where parametro= '"+ConstantesValoresPorDefecto.nombreControlaInterpretacionProcedimientosParaEvolucionar+"' ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarParametroGeneral, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{	
				logger.info("VALOR PARAMETRO en SQLBASE->"+rs.getString("valor"));
				return rs.getString("valor");	
			}			
		}
		catch (SQLException e) 
		{
			logger.error("Error en consultar Parametro General");
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 * Metodo para Consultar las Especialidades de la Cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static HashMap consultarEspecialidadesCuenta(Connection con, int codigoCuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");

		String consultarEspecialidades = "SELECT sol.especialidad_solicitante AS especialidad, serv.requiere_interpretacion AS interpretar, " +
											" sol.codigo_medico AS codMedSolicito " +
											" FROM solicitudes sol " +
											" INNER JOIN sol_procedimientos solp ON(solp.numero_solicitud=sol.numero_solicitud) " + 
											" INNER JOIN servicios serv ON (solp.codigo_servicio_solicitado=serv.codigo)" + 
											" WHERE " + 
											" sol.cuenta=? " + 
											" AND sol.tipo=? " +
											" AND sol.estado_historia_clinica=? " +
											" AND serv.requiere_interpretacion='"+ConstantesBD.acronimoSi + "'" ;

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarEspecialidades, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCuenta);
			ps.setInt(2, ConstantesBD.codigoTipoSolicitudProcedimiento);
			ps.setInt(3, ConstantesBD.codigoEstadoHCRespondida);
			//ps.setString(4, ConstantesBD.acronimoSi);

			logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("SQL: " + consultarEspecialidades);
			logger.info("Codigo Cuenta: " + codigoCuenta);
			logger.info("Tipo Solicitud de Procedimiento: " + ConstantesBD.codigoTipoSolicitudProcedimiento);
			logger.info("Estado Historia Clinica: " + ConstantesBD.codigoEstadoHCRespondida);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * Metodo que evalua las especialidades del medico que realizo la solicitud y el que va a evolucionar
	 * @param con
	 * @param codigoMedicoEvo -> codigo del medico que va a realizar la evolucion
	 * @param codigoMedicoSol -> codigo del medico que realizo la solicitud 
	 * @return
	 */
	public static HashMap validarEspecialidadesMedico(Connection con, int codigoMedicoEvo, int codigoMedicoSol) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");

		String consultarEspecialidades = "SELECT em.codigo_medico AS numEspecialidades, " +
											"em.codigo_especialidad AS codEspecialidad, " +
											"getnombreespecialidad(em.codigo_especialidad) AS nomEspecialidad " +
											" FROM especialidades_medicos em " +
											" INNER JOIN especialidades_medicos em2 ON (em.codigo_medico=? AND em2.codigo_medico=?) " +
											" WHERE em2.codigo_especialidad=em.codigo_especialidad";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarEspecialidades, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMedicoSol); //codigo del medico que realizo la solicitud
			ps.setInt(2, codigoMedicoEvo); //codigo del medico que va a realizar la evolucion
			
			logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("SQL: " + consultarEspecialidades);
			logger.info("Codigo Med Sol : " + codigoMedicoSol);
			logger.info("Codigo Med Evo : " + codigoMedicoEvo);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 

		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mapa;
	}
	
}