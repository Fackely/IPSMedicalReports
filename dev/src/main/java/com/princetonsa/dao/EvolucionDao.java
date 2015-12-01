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
	 * Inserta una evoluci�n en sus tablas respectivas. Este m�todo ES transaccional.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param cobrable indica si esta evoluci�n se debe o no cobrar
	 * @param diagnosticoComplicacion c�digo del diagn�stico de la complicaci�n, si la hubo
	 * @param diagnosticoComplicacionCIE c�digo del tipo CIE del diagn�stico de la complicaci�n, si la hubo
	 * @param fechaEvolucion fecha de realizaci�n de la evoluci�n
	 * @param horaEvolucion hora de realizaci�n de la evoluci�n
	 * @param informacionDadaPaciente informaci�n dada por el paciente
	 * @param descripcionComplicacion descripci�n de la complicaci�n, si la hubo
	 * @param tratamiento tratamiento en esta evoluci�n
	 * @param resultadosTratamiento resultados del tratamiento
	 * @param cambiosManejo cambios en el manejo del paciente
	 * @param hallazgosImportantes hallazgos m�s importantes durante esta evoluci�n
	 * @param procedimientosQuirurgicosObstetricos procedimientos quir�rgicos u obst�tricos
	 * @param fechaYResultadoExamenesDiagnostico fecha y resultados de los ex�menes de diagn�stico
	 * @param pronostico pron�stico del paciente
	 * @param observaciones observaciones generales
	 * @param codigoMedico c�digo del m�dico
	 * @param ordenSalida indica si se dio o no una orden de salida del paciente en esta evoluci�n
	 * @param diagnosticosPresuntivos Collection con los diagn�sticos presuntivos presentes en una evoluci�n
	 * @param diagnosticosDefinitivos Collection con los diagn�sticos definitivos presentes en una evoluci�n
	 * @param signosVitales Collection con los signos vitales del paciente
	 * @param tipoEvolucion el tipo de esta evoluci�n
	 * @param codigoValoracion el c�digo de la valoraci�n a la que esta evoluci�n est� asociada
	 * @param recargo el c�digo del recargo de esta evoluci�n
	 * @param datosMedico @todo
	 * @return el n�mero de la solicitud de evoluci�n, o 0 si hubo alg�n error.
	 */
	public int insertar(Connection con, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, int tipoDiagnosticoPrincipal, String datosMedico, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException;

	/**
	 * Inserta una evoluci�n en sus tablas respectivas. Mediante el par�metro 'estado',
	 * se le indica a este m�todo c�mo debe manejar la transaccionalidad.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param estado sus posibles valores son "empezar", "continuar", "finalizar". Indican
	 * si la transacci�n empieza pero no termina con esta inserci�n, si contin�a dentro
	 * de una transacci�n iniciada con anterioridad, o si debe finalizar una transacci�n
	 * ya iniciada
	 * @param cobrable indica si esta evoluci�n se debe o no cobrar
	 * @param diagnosticoComplicacion c�digo del diagn�stico de la complicaci�n, si la hubo
	 * @param diagnosticoComplicacionCIE c�digo del tipo CIE del diagn�stico de la complicaci�n, si la hubo
	 * @param fechaEvolucion fecha de realizaci�n de la evoluci�n
	 * @param horaEvolucion hora de realizaci�n de la evoluci�n
	 * @param informacionDadaPaciente informaci�n dada por el paciente
	 * @param descripcionComplicacion descripci�n de la complicaci�n, si la hubo
	 * @param tratamiento tratamiento en esta evoluci�n
	 * @param resultadosTratamiento resultados del tratamiento
	 * @param cambiosManejo cambios en el manejo del paciente
	 * @param hallazgosImportantes hallazgos m�s importantes durante esta evoluci�n
	 * @param procedimientosQuirurgicosObstetricos procedimientos quir�rgicos u obst�tricos
	 * @param fechaYResultadoExamenesDiagnostico fecha y resultados de los ex�menes de diagn�stico
	 * @param pronostico pron�stico del paciente
	 * @param observaciones observaciones generales
	 * @param codigoMedico c�digo del m�dico
	 * @param ordenSalida indica si se dio o no una orden de salida del paciente en esta evoluci�n
	 * @param diagnosticosPresuntivos Collection con los diagn�sticos presuntivos presentes en una evoluci�n
	 * @param diagnosticosDefinitivos Collection con los diagn�sticos definitivos presentes en una evoluci�n
	 * @param signosVitales Collection con los signos vitales del paciente
	 * @param tipoEvolucion el tipo de esta evoluci�n
	 * @param codigoValoracion el c�digo de la valoraci�n a la que esta evoluci�n est� asociada
	 * @param recargo el c�digo del recargo de esta evoluci�n
	 * @param balanceLiquidos @todo
	 * @param idCuenta c�digo de la cuenta asociada a esta evoluci�n
	 * @param idCentroCosto c�digo del centro de costo al que pertenece el m�dico que realiza esta evoluci�n
	 * @return el n�mero de la solicitud de evoluci�n, o 0 si hubo alg�n error.
	 */
	public int insertarTransaccional(Connection con, String estado, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, int tipoDiagnosticoPrincipal, String datosMedico, Vector balanceLiquidos, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException;

	/**
	 * Carga una evoluci�n base (de las tablas solicitudes_evolucion y evoluciones) desde la fuente de datos.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param codigo c�digo de la evoluci�n que se desea cargar
	 * @return <code>ResultSet</code> con los resultados de esta consulta
	 */
	public ResultSetDecorator cargarEvolucionBase(Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * Carga los diagn�sticos de una evoluci�n (de la tabla evol_diagnosticos) desde la fuente de datos.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param codigo c�digo de la evoluci�n que se desea cargar
	 * @return <code>ResultSet</code> con los resultados de esta consulta
	 */
	public ResultSetDecorator cargarEvolucionDiagnosticos(Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * Implementaci�n del m�todo que revisa si esta es la 
	 * primera evoluci�n que llen� el usuario despu�s que
	 * se cancelo una solicitud de cambio de tratante 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idEvolucion C�digo de la evoluci�n sobre la que se desea
	 * buscar
	 * @return
	 * @throws SQLException
	 */
	public boolean deboMostrarCancelacionTratante (Connection con, int idEvolucion) throws SQLException;
	
	/**
	 * Carga los signos vitales de una evoluci�n (de la tabla evol_signos_vitales) desde la fuente de datos.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param codigo c�digo de la evoluci�n que se desea cargar
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
	 * M�todo para cargar el balance de liquidos de la evoluci�n
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