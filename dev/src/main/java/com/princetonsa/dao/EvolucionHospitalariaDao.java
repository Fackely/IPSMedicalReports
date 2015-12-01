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
	 * para una evolución . Este método ES transaccional (por dentro).
	 * 
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigoEvolucion Código de la evolución a la que se va a
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
	 * para una evolución . Este método ES transaccional 
	 * (permitiendo definir el estado por medio del 
	 * parámetro del mismo nombre).
	 * 
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigoEvolucion Código de la evolución a la que se va a
	 * ingresar su parte Hospitalaria
	 * @param estado sus posibles valores son "empezar", "continuar", "finalizar". Indican
	 * si la transacción empieza pero no termina con esta inserción, si continúa dentro
	 * de una transacción iniciada con anterioridad, o si debe finalizar una transacción
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
	 * @return el número de la solicitud de evolución, o 0 si hubo algún error
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, int codigoEvolucion, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException;
	
	/**
	 * Carga los datos propios de una evolución hospitalaria (de la tabla evolu_hospitalarias)
	 * desde la fuente de datos.
	 * @param con una conexión abierta con la fuente de datos
	 * @param numeroSolicitud número de solicitud de la información que se desea cargar
	 * @return <code>ResultSet</code> con los resultados de esta consulta
	 */
	public ResultSetDecorator cargar(Connection con, int numeroSolicitud) throws SQLException;

	/**
	 * Modifica los datos propios de una evolución hospitalaria (de la tabla evolu_hospitalarias)
	 * en la fuente de datos. Este método ES transaccional.
	 * @param con una conexión abierta con la fuente de datos
	 * @param numeroSolicitud número de solicitud de la información que se desea modificar
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
	 * @return <b>true</b> si la modificación fue exitosa, <b>false</b> si no se pudo
	 * modificar (posiblemente, el número de solicitud no existía)
	 */
	public boolean modificar(Connection con, int numeroSolicitud, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException;

	/**
	 * Modifica los datos propios de una evolución hospitalaria (de la tabla evolu_hospitalarias)
	 * en la fuente de datos. Mediante el parámetro 'estado', se le indica a este método cómo debe manejar la transaccionalidad.
	 * @param con una conexión abierta con la fuente de datos
	 * @param numeroSolicitud número de solicitud de la información que se desea modificar
	 * @param estado sus posibles valores son "empezar", "continuar", "finalizar". Indican
	 * si la transacción empieza pero no termina con esta inserción, si continúa dentro
	 * de una transacción iniciada con anterioridad, o si debe finalizar una transacción
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
	 * @return <b>true</b> si la modificación fue exitosa, <b>false</b> si no se pudo
	 * modificar (posiblemente, el número de solicitud no existía)
	 */
	public boolean modificarTransaccional(Connection con, int numeroSolicitud, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException;

}