/*
 * Abr 29, 2008
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;

import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;


/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * VALORACIONES
 */
public interface ValoracionesDao 
{
	/**
	 * M�todo que realiza la inserci�n de una valoraci�n base
	 * @param con
	 * @param valoracionBase
	 * @return
	 */
	public ResultadoBoolean insertarBase(Connection con,DtoValoracion valoracionBase, String observacionCapitacion);
	
	/**
	 * M�todo implementado para insertar una valoraci�n de urgencias
	 * @param con
	 * @param valoracionUrgencias
	 * @return
	 */
	public ResultadoBoolean insertarUrgencias(Connection con,DtoValoracionUrgencias valoracionUrgencias);
	
	/**
	 * M�todo que inserta una nueva valoraci�n
	 * @param con
	 * @param valoracionHospitalizacion
	 * @return
	 */
	public ResultadoBoolean insertarHospitalizacion(Connection con,DtoValoracionHospitalizacion valoracionHospitalizacion);
	
	/**
	 * M�todo para cargar la valoracion de hospitalizacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoValoracionHospitalizacion cargarHospitalizacion(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo para cargar la valoraci�n de urgencias
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoValoracionUrgencias cargarUrgencias(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo implementado para cargar la valoracion base
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoValoracion cargarBase(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo para consultar la informaci�n triage
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	
	public DtoValoracionUrgencias cargarDiagnosticosValoracion (Connection con,String numeroSolicitud);
	
	public InfoDatosString consultarInformacionTriageUrgencias(Connection con,String idCuenta);
	
	/**
	 * M�todo implementado para insertar la primera versi�n de epicrisis
	 * @param con
	 * @param codigoIngreso
	 * @param codigoProfesional
	 * @param isUrgencias
	 * @return
	 */
	public int insertarPrimeraVersionEpicrisis(Connection con, String codigoIngreso,int codigoProfesional,boolean isUrgencias);
	
	/**
	 * M�todo implementado para modificar la valroaci�n de urgencias
	 * @param con
	 * @param valoracionUrgencias
	 * @return
	 */
	public ResultadoBoolean modificarUrgencias(Connection con,DtoValoracionUrgencias valoracionUrgencias);
	
	/**
	 * M�todo implementado para insertar observaciones
	 * @param con
	 * @param observaciones
	 * @param numeroSolicitud
	 * @param fechaActual
	 * @param horaActual
	 * @return
	 */
	public ResultadoBoolean insertarObservaciones(Connection con, ArrayList<DtoValoracionObservaciones> observaciones, String numeroSolicitud, String fechaActual, String horaActual);
	
	/**
	 * M�todo implementado para cargar los hist�ricos de valoraciones y/o evoluciones en caso de asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap<String, Object> cargarHistorico(Connection con,String idIngreso);
	
	/**
	 * M�todo implementado para actualizar la valoracion en el registro cuidado especial
	 * @param con
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @return
	 */
	public ResultadoBoolean actualizarValoracionRegistroCuidadoEspecial(Connection con,String codigoIngreso,String numeroSolicitud,String loginUsuario,boolean obligatorio);
	
	/**
	 * M�todo para obtener la causa externa de la primera valoraci�n
	 * @param con
	 * @param idCuenta
	 * @param seccionOrderBy
	 * @return
	 */
	public InfoDatosInt obtenerCausaExternaValoracion(Connection con,String idCuenta);
	
	/**
	 * M�todo para obtener la finalidad de la consulta de la valoracion
	 * @param con
	 * @param idCuenta
	 * @param tipoBD
	 * @return
	 */
	public InfoDatosString obtenerFinalidadConsultaValoracion(Connection con,String idCuenta);
	
	/**
	 * M�todo implementado para obtener la fecha/hora grabacion de la valoraci�n de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String[] obtenerFechahoraGrabacionValoracionUrgencias(Connection con,String idCuenta);

	/**
	 * M�todo para poner estado en valoraci�n a un paciente
	 * @param con
	 * @param paciente
	 * @param estaEnValoracion
	 * @param sessionId 
	 * @param usuario 
	 */
	public void ponerPacienteEnEvaloracion(Connection con, int paciente, boolean estaEnValoracion, String usuario, String sessionId);

	public int cancelarPacientesEnEvaloracion(Connection con);
	
	
	/**
	 * 
	 */
	public boolean actualizarPoolSolicitudXAgenda(Connection con, int solicitud, int pool);
	
	
	
	/**
	 * @param con
	 * @param numeroSolcitud
	 * @return si tiene o no una valoracion asociada
	 */
	public  Boolean tieneValoraciones(Connection con,String numeroSolcitud);
	
	/**
	 * M�todo para cargar las observaciones
	 * @param con
	 * @param numeroSolicitud
	 */
	public List<DtoValoracionObservaciones> cargarObservaciones(Connection con, String numeroSolicitud);
	
	/**
	 * Valida si la solicitud es de hospitalizacion cuidados especiales
	 * @param con
	 * @param idSolicitud
	 * @return boolean
	 */
	public boolean esSolicitudDeCuidadosEspeciales(Connection con, int idSolicitud);
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para obtener las observaciones
	 * @param con
	 * @param numeroSolicitud
	 * @return List
	 */
	public List<DtoValoracion> cargarObservacionesVistaValoracion(Connection con,String numeroSolicitud);
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para obtener la cadena Registro ConductaValoracion
	 * @param con
	 * @param numeroSolicitud
	 * @return String
	 */
	public String selecionarCadenaRegistroConductaValoracion(Connection con,String numeroSolicitud);
	
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para modificar las obsevaciones de urgencias
	 * @param con
	 * @param dtoValoracionObservaciones
	 * @param dtoValoracionUrgencias
	 * @param fechaActual
	 * @param horaActual
	 */
	public void modificarObservaciones(Connection con,DtoValoracionObservaciones dtoValoracionObservaciones,DtoValoracionUrgencias dtoValoracionUrgencias,String fechaActual,String horaActual);
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * metodo para modificar las obsevaciones de hospitalizacion
	 * @param con
	 * @param dtoValoracionObservaciones
	 * @param dtoValoracion
	 * @param fechaActual
	 * @param horaActual
	 */
	public void modificarObsevacionesHospitalizacion(Connection con,DtoValoracionObservaciones dtoValoracionObservaciones,DtoValoracionHospitalizacion dtoValoracionHospitalizacion,String fechaActual,String horaActual);
	
	
	
}


