/*
 * @(#)EvolucionHospitalaria.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Vector;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EvolucionHospitalariaDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Esta clase extiende la evoluci�n b�sica, a�adi�ndole los m�todos necesarios para volverla
 * una evoluci�n hospitalaria. En este caso, esto implica incorporar varios valores boolean
 * que indican qu� partes de la evoluci�n se deben mostrar en la epicrisis.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class EvolucionHospitalaria extends Evolucion {

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n
	 * dada por el paciente de esta evoluci�n en particular.
	 */
	private boolean infoDadaPacienteEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * signos vitales de esta evoluci�n en particular.
	 */
	private boolean signosVitalesEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * hallazgos de esta evoluci�n en particular.
	 */
	private boolean hallazgosEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * complicaciones de esta evoluci�n en particular.
	 */
	private boolean complicacionesEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * diagn�sticos presuntivos de esta evoluci�n en particular.
	 */
	private boolean diagnosticosPresuntivosEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * diagn�sticos definitivos de esta evoluci�n en particular.
	 */
	private boolean diagnosticosDefinitivosEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * tratamiento de esta evoluci�n en particular.
	 */
	private boolean tratamientoEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * resultados del tratamiento de esta evoluci�n en particular.
	 */
	private boolean resultadosTratamientoEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * cambios en el manejo de esta evoluci�n en particular.
	 */
	private boolean cambiosManejoEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * observaciones de esta evoluci�n en particular.
	 */
	private boolean observacionesEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * Procedimientos Quir�rgicos y Obst�tricos de esta evoluci�n en particular.
	 */
	private boolean procedimientosQuirurgicosObstetricosEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * Fecha y Resultado de Ex�menes de Diagnostico de esta evoluci�n en particular.
	 * 
	 */
	private boolean fechaYResultadoExamenesDiagnosticoEpicrisis;

	/**
	 * Indica si se debe o no mostrar en la epicrisis la informaci�n de
	 * pron�stico de esta evoluci�n en particular.
	 * 
	 */
	private boolean pronosticoEpicrisis;

	/**
	 * El DAO usado por el objeto <code>EvolucionHospitalaria</code> para acceder a la fuente de datos.
	 */
	private static EvolucionHospitalariaDao evolucionHospitalariaDao;

	/**
	 * Crea una nueva evoluci�n hospitalaria.
	 */
	public EvolucionHospitalaria() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * Tambi�n inicializa el acceso a BD de su padre, <code>Evolucion</code>.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) {

		boolean wasInited = false;
		boolean superWasInited = super.init(tipoBD);
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			evolucionHospitalariaDao = myFactory.getEvolucionHospitalariaDao();
			wasInited = (evolucionHospitalariaDao != null);
		}

		return (superWasInited && wasInited);

	}

	/**
	 * Retorna si se deben o no incluir los 'cambios en el manejo' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isCambiosManejoEpicrisis() 
	{
		return cambiosManejoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'cambios en el manejo' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getCambiosManejoEpicrisis() 
	{
		return cambiosManejoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir las 'complicaciones' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isComplicacionesEpicrisis() {
		return complicacionesEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir las 'complicaciones' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getComplicacionesEpicrisis() 
	{
		return complicacionesEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'diagn�sticos definitivos' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isDiagnosticosDefinitivosEpicrisis() 
	{
		return diagnosticosDefinitivosEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'diagn�sticos definitivos' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getDiagnosticosDefinitivosEpicrisis() 
	{
		return diagnosticosDefinitivosEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'diagn�sticos presuntivos' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isDiagnosticosPresuntivosEpicrisis() 
	{
		return diagnosticosPresuntivosEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'diagn�sticos presuntivos' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * 
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getDiagnosticosPresuntivosEpicrisis() 
	{
		return diagnosticosPresuntivosEpicrisis;
	}


	/**
	 * Retorna si se deben o no incluir la 'fecha y resultado de ex�menes de diagn�stico' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isFechaYResultadoExamenesDiagnosticoEpicrisis() 
	{
		return fechaYResultadoExamenesDiagnosticoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir la 'fecha y resultado de ex�menes de diagn�stico' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getFechaYResultadoExamenesDiagnosticoEpicrisis() 
	{
		return fechaYResultadoExamenesDiagnosticoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'hallazgos' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isHallazgosEpicrisis() 
	{
		return hallazgosEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'hallazgos' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getHallazgosEpicrisis() 
	{
		return hallazgosEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir la 'informaci�n dada por el paciente' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isInfoDadaPacienteEpicrisis() 
	{
		return infoDadaPacienteEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir la 'informaci�n dada por el paciente' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getInfoDadaPacienteEpicrisis() 
	{
		return infoDadaPacienteEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir las 'observaciones' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isObservacionesEpicrisis() 
	{
		return observacionesEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir las 'observaciones' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getObservacionesEpicrisis() 
	{
		return observacionesEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'procedimientos quir�rgicos u obst�tricos' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isProcedimientosQuirurgicosObstetricosEpicrisis() 
	{
		return procedimientosQuirurgicosObstetricosEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'procedimientos quir�rgicos u obst�tricos' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getProcedimientosQuirurgicosObstetricosEpicrisis() 
	{
		return procedimientosQuirurgicosObstetricosEpicrisis;
	}

	/**
	 * Retorna si se debe o no incluir el 'pron�stico' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isPronosticoEpicrisis() 
	{
		return pronosticoEpicrisis;
	}

	/**
	 * Retorna si se debe o no incluir el 'pron�stico' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getPronosticoEpicrisis() 
	{
		return pronosticoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'resultados de tratamiento' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isResultadosTratamientoEpicrisis() 
	{
		return resultadosTratamientoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'resultados de tratamiento' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getResultadosTratamientoEpicrisis() 
	{
		return resultadosTratamientoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'signos vitales' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isSignosVitalesEpicrisis() 
	{
		return signosVitalesEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'signos vitales' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * 
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getSignosVitalesEpicrisis() {
		return signosVitalesEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'tratamientos' de esta evoluci�n en la epicrisis.
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean isTratamientoEpicrisis()
	{
		return tratamientoEpicrisis;
	}

	/**
	 * Retorna si se deben o no incluir los 'tratamientos' de esta evoluci�n en la epicrisis.
	 * Es necesario porque struts necesita un get para los boolean o no los reconoce en
	 * el JSP
	 * @return <b>true</b> si se deben incluir, <b>false</b> si no
	 */
	public boolean getTratamientoEpicrisis() 
	{
		return tratamientoEpicrisis;
	}

	/**
	 * Establece si los 'cambios de manejo' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param cambiosManejoEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setCambiosManejoEpicrisis(boolean cambiosManejoEpicrisis) 
	{
		this.cambiosManejoEpicrisis = cambiosManejoEpicrisis;
	}

	/**
	 * Establece si las 'complicaciones' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param complicacionesEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setComplicacionesEpicrisis(boolean complicacionesEpicrisis) {
		this.complicacionesEpicrisis = complicacionesEpicrisis;
	}

	/**
	 * Establece si los 'diagn�sticos definitivos' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param diagnosticosDefinitivosEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setDiagnosticosDefinitivosEpicrisis(boolean diagnosticosDefinitivosEpicrisis) {
		this.diagnosticosDefinitivosEpicrisis = diagnosticosDefinitivosEpicrisis;
	}

	/**
	 * Establece si los 'diagn�sticos presuntivos' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param diagnosticosPresuntivosEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setDiagnosticosPresuntivosEpicrisis(boolean diagnosticosPresuntivosEpicrisis) {
		this.diagnosticosPresuntivosEpicrisis = diagnosticosPresuntivosEpicrisis;
	}

	/**
	 * Establece si la 'fecha y resultado de ex�menes de diagn�stico' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param fechaYResultadoExamenesDiagnosticoEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setFechaYResultadoExamenesDiagnosticoEpicrisis(boolean fechaYResultadoExamenesDiagnosticoEpicrisis) {
		this.fechaYResultadoExamenesDiagnosticoEpicrisis = fechaYResultadoExamenesDiagnosticoEpicrisis;
	}

	/**
	 * Establece si los 'hallazgos' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param hallazgosEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setHallazgosEpicrisis(boolean hallazgosEpicrisis) {
		this.hallazgosEpicrisis = hallazgosEpicrisis;
	}

	/**
	 * Establece si la 'informaci�n dada por el paciente' de esta evoluci�n se debe o no incluir en la epicrisis.
	 * @param infoDadaPacienteEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setInfoDadaPacienteEpicrisis(boolean infoDadaPacienteEpicrisis) {
		this.infoDadaPacienteEpicrisis = infoDadaPacienteEpicrisis;
	}

	/**
	 * Establece si las 'observaciones' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param observacionesEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setObservacionesEpicrisis(boolean observacionesEpicrisis) {
		this.observacionesEpicrisis = observacionesEpicrisis;
	}

	/**
	 * Establece si los 'procedimientos quir�rgicos u obst�tricos' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param procedimientosQuirurgicosObstetricosEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setProcedimientosQuirurgicosObstetricosEpicrisis(boolean procedimientosQuirurgicosObstetricosEpicrisis) {
		this.procedimientosQuirurgicosObstetricosEpicrisis = procedimientosQuirurgicosObstetricosEpicrisis;
	}

	/**
	 * Establece si el 'pron�stico' de esta evoluci�n se debe o no incluir en la epicrisis.
	 * @param pronosticoEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setPronosticoEpicrisis(boolean pronosticoEpicrisis) {
		this.pronosticoEpicrisis = pronosticoEpicrisis;
	}

	/**
	 * Establece si los 'resultados del tratamiento' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param resultadosTratamientoEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setResultadosTratamientoEpicrisis(boolean resultadosTratamientoEpicrisis) {
		this.resultadosTratamientoEpicrisis = resultadosTratamientoEpicrisis;
	}

	/**
	 * Establece si los 'signos vitales' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param signosVitalesEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setSignosVitalesEpicrisis(boolean signosVitalesEpicrisis) {
		this.signosVitalesEpicrisis = signosVitalesEpicrisis;
	}

	/**
	 * Establece si los 'tratamientos' de esta evoluci�n se deben o no incluir en la epicrisis.
	 * @param tratamientoEpicrisis <b>true</b> si esta secci�n de la evoluci�n se debe
	 * incluir en la epicrisis, <b>false</b> si no
	 */
	public void setTratamientoEpicrisis(boolean tratamientoEpicrisis) {
		this.tratamientoEpicrisis = tratamientoEpicrisis;
	}

	/**
	 * Este m�todo inicializa en valores vac�os, -mas no nulos- los atributos de este objeto.
	 */
	public void clean() {

		super.clean();
		this.cambiosManejoEpicrisis = false;
		this.complicacionesEpicrisis = false;
		this.diagnosticosDefinitivosEpicrisis = false;
		this.diagnosticosPresuntivosEpicrisis = false;
		this.fechaYResultadoExamenesDiagnosticoEpicrisis = false;
		this.hallazgosEpicrisis = false;
		this.infoDadaPacienteEpicrisis = false;
		this.observacionesEpicrisis = false;
		this.procedimientosQuirurgicosObstetricosEpicrisis = false;
		this.pronosticoEpicrisis = false;
		this.resultadosTratamientoEpicrisis = false;
		this.signosVitalesEpicrisis = false;
		this.tratamientoEpicrisis = false;
		setTipoEvolucion(Evolucion.HOSPITALARIA);

	}

	/**
	 * Inserta esta evoluci�n hospitalaria en sus tablas respectivas, y establece el valor
	 * de n�mero de solicitud de este objeto en su valor adecuado. Este m�todo maneja su
	 * transaccionalidad internamente.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @return El n�mero de la solicitud de evoluci�n, o 0 si hubo alg�n error
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#insertar(Connection, int, int, boolean, String, int, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, boolean, Collection, Collection, Collection, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, int)
	 */
	public int insertar(Connection con, PersonaBasica paciente, UsuarioBasico medico, Vector balanceLiquidos, int codigoConductaASeguir, String acronimoTipoReferencia) throws Exception 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int codigo=this.insertarTransaccional(con, paciente, medico, ConstantesBD.inicioTransaccion, balanceLiquidos, codigoConductaASeguir, acronimoTipoReferencia);
		myFactory.endTransaction(con);
		return codigo;
	}

	/**
	 * Inserta esta evoluci�n hospitalaria en sus tablas respectivas, y establece el valor
	 * de n�mero de solicitud de este objeto en su valor adecuado. Este m�todo
	 * maneja su transaccionalidad internamente basado en el valor del 'estado'.
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param estado de la transacci�n. posibles valores son "empezar", "continuar", "finalizar".
	 * @return El n�mero de la solicitud de evoluci�n, o 0 si hubo alg�n error
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#insertarTransaccional(Connection, String, int, int, boolean, String, int, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, boolean, Collection, Collection, Collection, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, int)
	 */
	public int insertarTransaccional(Connection con, PersonaBasica paciente, UsuarioBasico medico,String estado, Vector balanceLiquidos, int codigoConductaASeguir, String acronimoTipoReferencia) throws Exception 
	{
		
		if (!estado.equals(ConstantesBD.finTransaccion))
		{
			super.insertarTransaccional(con, paciente, medico, estado, balanceLiquidos, codigoConductaASeguir, acronimoTipoReferencia); 
		}
		else
		{
		    super.insertarTransaccional(con, paciente, medico, ConstantesBD.continuarTransaccion, balanceLiquidos, codigoConductaASeguir, acronimoTipoReferencia);
		}
		if (!estado.equals(ConstantesBD.inicioTransaccion))
		{
		    evolucionHospitalariaDao.insertarTransaccional (con, this.getCodigo(), estado, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis, diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis, infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis, pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis) ;
		}
		else
		{
		    evolucionHospitalariaDao.insertarTransaccional (con, this.getCodigo(), ConstantesBD.continuarTransaccion, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis, diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis, infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis, pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis) ;
		}
		return this.getCodigo();
	}

	/**
	 * Carga una evoluci�n desde la fuente de datos, y pone todos los datos asociados a ella
	 * en un objeto <code>Evolucion</code>.
	 * @param con conexi�n abierta con la fuente de datos
	 * @param numeroSolicitud n�mero de solicitud de la evoluci�n que se desea cargar
	 * @return <b>true</b> si se pudo cargar una evoluci�n con el n�mero de
	 * solicitud dado, <b>false</b> si no se pudo (posiblemente, pq no exist�a una
	 * evoluci�n hospitalaria con ese n�mero de solicitud)
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#cargar(Connection, int)
	 */
	public boolean cargar(Connection con, int numeroSolicitud) throws SQLException 
	{

		// Limpiamos los datos viejos antes de cargar lo nuevo
		clean();

		// Cargamos los datos del padre
		if (!super.cargar(con, numeroSolicitud)) {
			return false;
		}

		ResultSetDecorator rs = evolucionHospitalariaDao.cargar(con, numeroSolicitud);

		if (rs.next()) {

			this.infoDadaPacienteEpicrisis = rs.getBoolean("info_dada_pac_epicrisis");
			this.signosVitalesEpicrisis = rs.getBoolean("signos_vitales_epicrisis");
			this.hallazgosEpicrisis = rs.getBoolean("hallazgos_epicrisis");
			this.complicacionesEpicrisis = rs.getBoolean("complicaciones_epicrisis");
			this.diagnosticosPresuntivosEpicrisis = rs.getBoolean("diag_presuntivos_epicrisis");
			this.diagnosticosDefinitivosEpicrisis = rs.getBoolean("diag_definitivos_epicrisis");
			this.tratamientoEpicrisis = rs.getBoolean("tratamiento_epicrisis");
			this.resultadosTratamientoEpicrisis = rs.getBoolean("resultados_trat_epicrisis");
			this.cambiosManejoEpicrisis = rs.getBoolean("cambios_manejo_epicrisis");
			this.procedimientosQuirurgicosObstetricosEpicrisis = rs.getBoolean("proced_quirurgicos_obst_ep");
			this.fechaYResultadoExamenesDiagnosticoEpicrisis = rs.getBoolean("resultado_examenes_diag_ep");
			this.pronosticoEpicrisis = rs.getBoolean("pronostico_epicrisis");
			this.observacionesEpicrisis = rs.getBoolean("observaciones_epicrisis");

		}

		else {
			return false;
		}

		return true;

	}

	/**
	 * Modifica esta evoluci�n hospitalaria en su tabla respectiva. Este m�todo maneja su
	 * transaccionalidad internamente.
	 * @param con conexi�n abierta con la fuente de datos
	 * @param numeroSolicitud n�mero de solicitud de la evoluci�n que se desea modificar
	 * @return <b>true</b> si la modificaci�n fue exitosa, <b>false</b> si no se pudo
	 * modificar (posiblemente, el n�mero de solicitud no exist�a)
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#modificar(Connection, int, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	public boolean modificar(Connection con, int numeroSolicitud) throws SQLException {

		return evolucionHospitalariaDao.modificar(
			con, numeroSolicitud, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis,
			diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis,
			infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis,
			pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis
		);

	}

	/**
	 * Modifica esta evoluci�n hospitalaria en su tabla respectiva. Este m�todo
	 * maneja su transaccionalidad internamente basado en el valor del 'estado'.
	 * @param con conexi�n abierta con la fuente de datos
	 * @param numeroSolicitud n�mero de solicitud de la evoluci�n que se desea modificar
	 * @param estado de la transacci�n. posibles valores son "empezar", "continuar", "finalizar".
	 * @return <b>true</b> si la modificaci�n fue exitosa, <b>false</b> si no se pudo
	 * modificar (posiblemente, el n�mero de solicitud no exist�a)
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#modificarTransaccional(Connection, int, String, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	public boolean modificarTransaccional(Connection con, int numeroSolicitud, String estado) throws SQLException {

		return evolucionHospitalariaDao.modificarTransaccional(
			con, numeroSolicitud, estado, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis,
			diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis,
			infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis,
			pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis
		);

	}

	/**
	 * Este m�todo dice si para esta evoluci�n hay datos objetivos
	 * @return
	 */
	public boolean getHayDatosObjetivos ()
	{
		if (this.hallazgosEpicrisis||this.procedimientosQuirurgicosObstetricosEpicrisis||this.fechaYResultadoExamenesDiagnosticoEpicrisis||(this.signosVitalesEpicrisis&&super.getNumSignosVitales()>0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Este m�todo dice si para esta evoluci�n hay an�lisis
	 * @return
	 */
	public boolean getHayAnalisis()
	{
		if (this.resultadosTratamientoEpicrisis||this.complicacionesEpicrisis)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Este m�todo dice si para esta evoluci�n hay plan
	 * @return
	 */
	public boolean getHayPlan()
	{
		if (this.tratamientoEpicrisis||this.cambiosManejoEpicrisis||this.pronosticoEpicrisis||this.observacionesEpicrisis)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Este m�todo dice si para esta evoluci�n hay diagnosticos
	 * @return
	 */
	public boolean getHayDiagnosticos()
	{
		if (this.diagnosticosDefinitivosEpicrisis||this.diagnosticosPresuntivosEpicrisis||!this.getDiagnosticoComplicacion().getAcronimo().equals("1"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}