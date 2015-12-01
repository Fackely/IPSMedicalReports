/*
 * @(#)EpicrisisDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.util.ArrayList;
import java.util.Collection;

import com.princetonsa.mundo.atencion.Evolucion;
import com.princetonsa.mundo.atencion.EvolucionHospitalaria;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar un objeto
 * que presta servicio de acceso a fuentes de datos para una <code>Epicrisis</code>.
 * N�tese que ninguno de los m�todos de esta <i>interface</i> recibe como par�metro una
 * conexi�n; internamente, todos deben tomar una del pool y liberarla cuando ya no la necesiten.
 *
 * @version Jul 4, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public interface EpicrisisDao 
{

	/**
	 * Carga los datos del m�dico responsable de la epicrisis y el boolean indicando si va la informaci�n
	 * de Urgenicas, en caso de que esta exista.
	 * @param codigoIngreso c�digo de ingreso de la epicrisis de la cual vamos a cargar los datos b�sicos.
	 * @return una <code>Collection</code> con el resultado de cargar los datos b�sicos de la epicrisis.
	 * Si codigoIngreso no existe, la Collection ser� vacia.
	 */
	public Collection cargarEpicrisisBasica(int codigoIngreso) throws Exception;

	
	
	
	/**
	 * Carga la �ltima evoluci�n para una epicrisis, si la hay.
	 * @param codigoIngreso c�digo de ingreso de la epicrisis de la cual vamos a cargar la �ltima evoluci�n
	 * @param esEpicrisisHospitalizacion boolean que dice si la evoluci�n que se debe cambiar es de urgencias 
	 * u hospitalizados
	 * @return la <code>Evolucion</code> con el c�digo de ingreso solicitado, o null si no existe.
	 */
	public Evolucion cargarUltimaEvolucion(int codigoIngreso, boolean esEpicrisisHospitalizacion) throws Exception;

	/**
	 * Carga la primera evoluci�n para una epicrisis, si la hay.
	 * @param codigoIngreso c�digo de ingreso de la epicrisis de la cual vamos a cargar la primera evoluci�n
	 * @param esEpicrisisHospitalizacion boolean que dice si la evoluci�n que se debe cambiar es de urgencias 
	 * u hospitalizados
	 * @return la <code>Evolucion</code> con el c�digo de ingreso solicitado, o null si no existe.
	 */
	public EvolucionHospitalaria cargarPrimeraEvolucion(int codigoIngreso, boolean esEpicrisisHospitalizacion) throws Exception;

	/**
	 * Carga las notas aclaratorias de una epicrisis.
	 * @param codigoIngreso c�digo de ingreso de la epicrisis de la cual vamos a cargar las notas aclaratorias
	 * @return una <code>Collection</code> con las notas aclaratorias de la epicrisis, o una Collection vac�a
	 *  si no hab�a.
	 */
	public Collection cargarNotasAclaratorias(int codigoIngreso) throws Exception;

	/**
	 * Carga los c�digos de las valoraciones generales asociadas a esta epicrisis cuya
	 * cuenta tiene al menos una valoraci�n de hospitalizacion.
	 * @param codigoIngreso c�digo de ingreso de las valoraciones que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las valoraciones, de tama�o 0 si no hubo valoraciones.
	 */
	public ArrayList cargarCodigosValoracionesInterconsultaHospitalizacion(int codigoIngreso) throws Exception;

	/**
	 * Carga los c�digos de las solicitudes de procedimientos asociadas 
	 * a esta epicrisis en la cuenta de hospitalizaci�n
	 * 
	 * @param codigoIngreso c�digo de ingreso de las solicitudes de procedimiento que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las solicitudes de procedimientos, de tama�o 0 si no 
	 * hubo solicitudes de procedimientos en la cuenta de hospitalizaci�n.
	 */
	public ArrayList cargarCodigosSolicitudesProcedimientosHospitalizacion (int codigoIngreso) throws Exception;
	
	/**
	 * Carga los c�digos de las solicitudes de procedimientos asociadas 
	 * a esta epicrisis 
	 * 
	 * @param codigoIngreso c�digo de ingreso de las solicitudes de procedimiento que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las solicitudes de procedimientos, de tama�o 0 si no 
	 * hubo solicitudes de procedimientos en la cuenta de urgencias.
	 */
	public ArrayList cargarCodigosSolicitudesProcedimientosUrgencias (int codigoIngreso) throws Exception;

	/**
	 * Carga los c�digos de las solicitudes de interconsulta asociadas 
	 * a esta epicrisis en la cuenta de hospitalizaci�n
	 * 
	 * @param codigoIngreso c�digo de ingreso de las solicitudes de interconsulta que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las solicitudes de interconsulta, de tama�o 0 si no 
	 * hubo solicitudes de Interconsulta en la cuenta de hospitalizaci�n.
	 */
	public ArrayList cargarCodigosSolicitudesInterconsultaHospitalizacion (int codigoIngreso) throws Exception;
	
	/**
	 * Carga los c�digos de las solicitudes de interconsulta asociadas 
	 * a esta epicrisis en su cuenta de urgencias
	 * 
	 * @param codigoIngreso c�digo de ingreso de las solicitudes de interconsulta que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las solicitudes de interconsulta, de tama�o 0 si no 
	 * hubo solicitudes de procedimientos en la cuenta de urgencias.
	 */
	public ArrayList cargarCodigosSolicitudesInterconsultaUrgencias (int codigoIngreso) throws Exception;

	/**
	 * Carga los c�digos de las valoraciones pediatricas asociadas a esta epicrisis cuya
	 * cuenta tiene al menos una valoraci�n de hospitalizacion.
	 * @param codigoIngreso c�digo de ingreso de las valoraciones pediatricas que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las valoraciones pediatricas, de tama�o 0 si no hubo valoraciones.
	 */
	public ArrayList cargarCodigosValoracionesInterconsultaHospitalizacionPediatricas (int codigoIngreso) throws Exception;
	
	/**
	 * Carga los c�digos de las valoraciones generales asociadas a esta epicrisis cuya
	 * cuenta tiene al menos una valoraci�n de urgencias.
	 * @param codigoIngreso c�digo de ingreso de las valoraciones que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las valoraciones, de tama�o 0 si no hubo valoraciones pediatricas .
	 */
	public ArrayList cargarCodigosValoracionesInterconsultaUrgencias(int codigoIngreso) throws Exception;

	/**
	 * Carga los c�digos de las valoraciones pediatricas asociadas a esta epicrisis cuya
	 * cuenta tiene al menos una valoraci�n de urgencias.
	 * @param codigoIngreso c�digo de ingreso de las valoraciones pediatricas que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las valoraciones pediatricas, de tama�o 0 si no hubo valoraciones pediatricas.
	 */
	public ArrayList cargarCodigosValoracionesInterconsultaUrgenciasPediatricas(int codigoIngreso) throws Exception;
	
	
	/**
	 * Carga los c�digos de las evoluciones generales asociadas a esta epicrisis.
	 * @param codigoIngreso c�digo de ingreso de las evoluciones que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las evoluciones, de tama�o 0 si no hubo evoluciones generales.
	 */
	public ArrayList cargarCodigosEvoluciones(int codigoIngreso) throws Exception;

	/**
	 * Carga los c�digos de las evoluciones hospitalarias asociadas a esta epicrisis.
	 * @param codigoIngreso c�digo de ingreso de las evoluciones hospitalarias que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las evoluciones hospitalarias, de tama�o 0 si no hubo evoluciones hospitalarias.
	 */
	public ArrayList cargarCodigosEvolucionesHospitalarias(int codigoIngreso) throws Exception;
	
	/**
	 * Carga los c�digos de las evoluciones hospitalarias asociadas a esta epicrisis,
	 * recibe un par�metro extra con un c�digo de evoluci�n que no se quiera 
	 * mostrar. Ejemplo: La primera de urgencias.
	 * 
	 * @param codigoIngreso c�digo de ingreso de las evoluciones de urgencias que se desean cargar
	 * @return un <code>ArrayList</code> con los c�digos de las evoluciones de urgencias, de tama�o 0 si no hubo evoluciones hospitalarias.
	 */
	public ArrayList cargarCodigosEvolucionesUrgencias(int codigoIngreso, int codigoEvolucionANoCargar, boolean incluirEnBusquedaEvolEgreso) throws Exception;

	/**
	 * M�todo que inserta una nota aclaratoria en una epicrisis
	 * 
	 * @param codigoEpicrisis C�digo de la Epicrisis a la cual se va a a�adir la nota aclaratoria
	 * @param notaAclaratoria Texto de la nota aclaratoria
	 * @param codigoMedico C�digo del m�dico que agrega esta nota aclaratoria
	 * @return
	 * @throws Exception
	 */
	public int insertarNotaAclaratoria (int codigoEpicrisis, String notaAclaratoria, int codigoMedico) throws Exception;
	
	/**
	 * M�todo que busca si para esta epicrisis existe un egreso de urgencias.
	 * Retorna el c�digo de este egreso. Debe retornar el egreso as� solo tenga
	 * orden de salida (M�s no lo debe mostrar en caso de reversi�n de egreso)
	 * 
	 * @param codigoIngreso C�digo de ingreso o de epicrisis
	 * @return
	 * @throws Exception
	 */
	public int buscarCodigoEgresoUrgencias (int codigoIngreso) throws Exception;


	/**
	 * M�todo que busca si para esta epicrisis existe un egreso de hospitalizaci�n.
	 * Retorna el c�digo de este egreso. Debe retornar el egreso as� solo tenga
	 * orden de salida (M�s no lo debe mostrar en caso de reversi�n de egreso)
	 * 
	 * @param codigoIngreso C�digo de ingreso o de epicrisis
	 * @return
	 * @throws Exception
	 */
	public int buscarCodigoEgresoHospitalizacion (int codigoIngreso) throws Exception;
	
	/**
	 * M�todo que busca los c�digos de las respuestas 
	 * de procedimiento para la cuenta de urgencias,
	 * en una epicrisis
	 * 
	 * @param codigoIngreso C�digo de ingreso o de epicrisis
	 * @return
	 * @throws Exception
	 */
	public ArrayList cargarCodigosRespuestaProcedimientosUrgencias (int codigoIngreso) throws Exception;

	/**
	 * M�todo que busca los c�digos de las respuestas 
	 * de procedimiento para la cuenta de hospitalizaci�n,
	 * en una epicrisis
	 * 
	 * @param codigoIngreso C�digo de ingreso o de epicrisis
	 * @return
	 * @throws Exception
	 */
	public ArrayList cargarCodigosRespuestaProcedimientosHospitalizacion (int codigoIngreso) throws Exception;
}