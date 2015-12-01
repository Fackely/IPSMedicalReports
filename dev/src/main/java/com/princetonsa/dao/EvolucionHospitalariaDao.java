/*
 * @(#)EvolucionHospitalariaDao.java
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

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>EvolucionHospitalaria</code>.
 *
 * @version May 29, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */
public interface EvolucionHospitalariaDao {

	/**
	 * Inserta los datos particulares para ev. hospitalaria 
	 * para una evoluci�n . Este m�todo ES transaccional (por dentro).
	 * 
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param codigoEvolucion C�digo de la evoluci�n a la que se va a
	 * ingresar su parte Hospitalaria
	 * @param cambiosManejoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param complicacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosDefinitivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosPresuntivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param fechaYResultadoExamenesDiagnosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param hallazgosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param infoDadaPacienteEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param observacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param procedimientosQuirurgicosObstetricosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param pronosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param resultadosTratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param signosVitalesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param tratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @return
	 * @throws SQLException
	 */	
	public int insertar(Connection con, int codigoEvolucion, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException;

	/**
	 * Inserta los datos particulares para ev. hospitalaria 
	 * para una evoluci�n . Este m�todo ES transaccional 
	 * (permitiendo definir el estado por medio del 
	 * par�metro del mismo nombre).
	 * 
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param codigoEvolucion C�digo de la evoluci�n a la que se va a
	 * ingresar su parte Hospitalaria
	 * @param estado sus posibles valores son "empezar", "continuar", "finalizar". Indican
	 * si la transacci�n empieza pero no termina con esta inserci�n, si contin�a dentro
	 * de una transacci�n iniciada con anterioridad, o si debe finalizar una transacci�n
	 * ya iniciada
	 * @param cambiosManejoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param complicacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosDefinitivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosPresuntivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param fechaYResultadoExamenesDiagnosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param hallazgosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param infoDadaPacienteEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param observacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param procedimientosQuirurgicosObstetricosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param pronosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param resultadosTratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param signosVitalesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param tratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @return el n�mero de la solicitud de evoluci�n, o 0 si hubo alg�n error
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, int codigoEvolucion, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException;
	
	/**
	 * Carga los datos propios de una evoluci�n hospitalaria (de la tabla evolu_hospitalarias)
	 * desde la fuente de datos.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param numeroSolicitud n�mero de solicitud de la informaci�n que se desea cargar
	 * @return <code>ResultSet</code> con los resultados de esta consulta
	 */
	public ResultSetDecorator cargar(Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * Modifica los datos propios de una evoluci�n hospitalaria (de la tabla evolu_hospitalarias)
	 * en la fuente de datos. Este m�todo ES transaccional.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param numeroSolicitud n�mero de solicitud de la informaci�n que se desea modificar
	 * @param cambiosManejoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param complicacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosDefinitivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosPresuntivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param fechaYResultadoExamenesDiagnosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param hallazgosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param infoDadaPacienteEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param observacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param procedimientosQuirurgicosObstetricosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param pronosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param resultadosTratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param signosVitalesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param tratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @return <b>true</b> si la modificaci�n fue exitosa, <b>false</b> si no se pudo
	 * modificar (posiblemente, el n�mero de solicitud no exist�a)
	 */
	public boolean modificar(Connection con, int numeroSolicitud, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException;

	/**
	 * Modifica los datos propios de una evoluci�n hospitalaria (de la tabla evolu_hospitalarias)
	 * en la fuente de datos. Mediante el par�metro 'estado', se le indica a este m�todo c�mo debe manejar la transaccionalidad.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param numeroSolicitud n�mero de solicitud de la informaci�n que se desea modificar
	 * @param estado sus posibles valores son "empezar", "continuar", "finalizar". Indican
	 * si la transacci�n empieza pero no termina con esta inserci�n, si contin�a dentro
	 * de una transacci�n iniciada con anterioridad, o si debe finalizar una transacci�n
	 * ya iniciada
	 * @param cambiosManejoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param complicacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosDefinitivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param diagnosticosPresuntivosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param fechaYResultadoExamenesDiagnosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param hallazgosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param infoDadaPacienteEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param observacionesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param procedimientosQuirurgicosObstetricosEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param pronosticoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param resultadosTratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param signosVitalesEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @param tratamientoEpicrisis indica si este apartado se debe o no mostrar en la epicrisis
	 * @return <b>true</b> si la modificaci�n fue exitosa, <b>false</b> si no se pudo
	 * modificar (posiblemente, el n�mero de solicitud no exist�a)
	 */
	public boolean modificarTransaccional(Connection con, int numeroSolicitud, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException;

}