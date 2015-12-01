/*
 * @(#)EvolucionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Evolucion</code>.
 *
 * @version May 26, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public interface EvolucionDao {

	/**
	 * Inserta una evolución en sus tablas respectivas. Este método ES transaccional.
	 * @param con una conexión abierta con la fuente de datos
	 * @param cobrable indica si esta evolución se debe o no cobrar
	 * @param diagnosticoComplicacion código del diagnóstico de la complicación, si la hubo
	 * @param diagnosticoComplicacionCIE código del tipo CIE del diagnóstico de la complicación, si la hubo
	 * @param fechaEvolucion fecha de realización de la evolución
	 * @param horaEvolucion hora de realización de la evolución
	 * @param informacionDadaPaciente información dada por el paciente
	 * @param descripcionComplicacion descripción de la complicación, si la hubo
	 * @param tratamiento tratamiento en esta evolución
	 * @param resultadosTratamiento resultados del tratamiento
	 * @param cambiosManejo cambios en el manejo del paciente
	 * @param hallazgosImportantes hallazgos más importantes durante esta evolución
	 * @param procedimientosQuirurgicosObstetricos procedimientos quirúrgicos u obstétricos
	 * @param fechaYResultadoExamenesDiagnostico fecha y resultados de los exámenes de diagnóstico
	 * @param pronostico pronóstico del paciente
	 * @param observaciones observaciones generales
	 * @param codigoMedico código del médico
	 * @param ordenSalida indica si se dio o no una orden de salida del paciente en esta evolución
	 * @param diagnosticosPresuntivos Collection con los diagnósticos presuntivos presentes en una evolución
	 * @param diagnosticosDefinitivos Collection con los diagnósticos definitivos presentes en una evolución
	 * @param signosVitales Collection con los signos vitales del paciente
	 * @param tipoEvolucion el tipo de esta evolución
	 * @param codigoValoracion el código de la valoración a la que esta evolución está asociada
	 * @param recargo el código del recargo de esta evolución
	 * @param datosMedico @todo
	 * @return el número de la solicitud de evolución, o 0 si hubo algún error.
	 */
	public int insertar(Connection con, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, int tipoDiagnosticoPrincipal, String datosMedico, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException;

	/**
	 * Inserta una evolución en sus tablas respectivas. Mediante el parámetro 'estado',
	 * se le indica a este método cómo debe manejar la transaccionalidad.
	 * @param con una conexión abierta con la fuente de datos
	 * @param estado sus posibles valores son "empezar", "continuar", "finalizar". Indican
	 * si la transacción empieza pero no termina con esta inserción, si continúa dentro
	 * de una transacción iniciada con anterioridad, o si debe finalizar una transacción
	 * ya iniciada
	 * @param cobrable indica si esta evolución se debe o no cobrar
	 * @param diagnosticoComplicacion código del diagnóstico de la complicación, si la hubo
	 * @param diagnosticoComplicacionCIE código del tipo CIE del diagnóstico de la complicación, si la hubo
	 * @param fechaEvolucion fecha de realización de la evolución
	 * @param horaEvolucion hora de realización de la evolución
	 * @param informacionDadaPaciente información dada por el paciente
	 * @param descripcionComplicacion descripción de la complicación, si la hubo
	 * @param tratamiento tratamiento en esta evolución
	 * @param resultadosTratamiento resultados del tratamiento
	 * @param cambiosManejo cambios en el manejo del paciente
	 * @param hallazgosImportantes hallazgos más importantes durante esta evolución
	 * @param procedimientosQuirurgicosObstetricos procedimientos quirúrgicos u obstétricos
	 * @param fechaYResultadoExamenesDiagnostico fecha y resultados de los exámenes de diagnóstico
	 * @param pronostico pronóstico del paciente
	 * @param observaciones observaciones generales
	 * @param codigoMedico código del médico
	 * @param ordenSalida indica si se dio o no una orden de salida del paciente en esta evolución
	 * @param diagnosticosPresuntivos Collection con los diagnósticos presuntivos presentes en una evolución
	 * @param diagnosticosDefinitivos Collection con los diagnósticos definitivos presentes en una evolución
	 * @param signosVitales Collection con los signos vitales del paciente
	 * @param tipoEvolucion el tipo de esta evolución
	 * @param codigoValoracion el código de la valoración a la que esta evolución está asociada
	 * @param recargo el código del recargo de esta evolución
	 * @param balanceLiquidos @todo
	 * @param idCuenta código de la cuenta asociada a esta evolución
	 * @param idCentroCosto código del centro de costo al que pertenece el médico que realiza esta evolución
	 * @return el número de la solicitud de evolución, o 0 si hubo algún error.
	 */
	public int insertarTransaccional(Connection con, String estado, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, int tipoDiagnosticoPrincipal, String datosMedico, Vector balanceLiquidos, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException;

	/**
	 * Carga una evolución base (de las tablas solicitudes_evolucion y evoluciones) desde la fuente de datos.
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigo código de la evolución que se desea cargar
	 * @return <code>ResultSet</code> con los resultados de esta consulta
	 */
	public ResultSetDecorator cargarEvolucionBase(Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * Carga los diagnósticos de una evolución (de la tabla evol_diagnosticos) desde la fuente de datos.
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigo código de la evolución que se desea cargar
	 * @return <code>ResultSet</code> con los resultados de esta consulta
	 */
	public ResultSetDecorator cargarEvolucionDiagnosticos(Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * Implementación del método que revisa si esta es la 
	 * primera evolución que llenó el usuario después que
	 * se cancelo una solicitud de cambio de tratante 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idEvolucion Código de la evolución sobre la que se desea
	 * buscar
	 * @return
	 * @throws SQLException
	 */
	public boolean deboMostrarCancelacionTratante (Connection con, int idEvolucion) throws SQLException;
	
	/**
	 * Carga los signos vitales de una evolución (de la tabla evol_signos_vitales) desde la fuente de datos.
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigo código de la evolución que se desea cargar
	 * @return <code>ResultSet</code> con los resultados de esta consulta
	 */
	public ResultSetDecorator cargarEvolucionSignosVitales(Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * 
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws SQLEXception
	 */
	public ResultSetDecorator consultarFechaEvolucionSalida(Connection con, int numeroCuenta) throws SQLException;
	//Deben estar ordenadas por Centro de Costo y por numero de solicitud
//	* idEvolucion int
//	* FechaEvolucion String
//	* HoraEvolucion String 
//	* codigoCentroCosto int 
//	* nombreCentroCosto String 
	
	public ResultSetDecorator getBasicoEvolucionesRespondidas(Connection con, int numeroCuenta) throws SQLException;
	public int getIdCuenta(Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método para cargar el balance de liquidos de la evolución
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEvolucionBalanceLiquidos(Connection con, int codigo) throws SQLException;
	
	/**
	 * mapa que carga la informacion de conducta a seguir de la utltinma evolucion insertada,
	 * en caso de no encontrar informacion retorna null
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap conductaASeguirUltimaInsertada(Connection con, int idCuenta); 
	
	/**
	 * mapa que contiene las conductas a seguir
	 * @param con
	 * @return
	 */
	public HashMap conductasASeguirMap(Connection con);

	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public HashMap conductaASeguirDadaEvol(Connection con, int codigoEvolucion);

	/**
	 * Metodo para Consultar el Parametro General de Controla Interpretacion
	 * @param con
	 * @return
	 */
	public String consultarParametroControlaInterpretacion(Connection con);

	/**
	 * Metodo para Consultar las Especialidades de la Cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap consultarEspecialidadesCuenta(Connection con, int codigoCuenta);
	
	
	/**
	 * Metodo para validar las especialidades del medico que solicita al que evoluciona
	 * @param con
	 * @param codigoMedicoEvo
	 * @param codigoMedicoSol
	 * @return
	 */
	public HashMap validarEspecialidadesMedico(Connection con, int codigoMedicoEvo, int codigoMedicoSol); 
	
	
	

}