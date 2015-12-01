/*
 * Junio 3, 2008
 */
package util.ordenesMedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ordenesmedicas.UtilidadesOrdenesMedicasDao;

/**
 * 
 * @author sgomez
 * Clase que contiene las utilidades del módulo de ORDENES MEDICAS
 */
public class UtilidadesOrdenesMedicas 
{
	private static Logger logger =Logger.getLogger(UtilidadesOrdenesMedicas.class);
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesOrdenesMedicasDao utilidadesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesOrdenesMedicasDao();
	}
	
	/**
	 * Método para obtener la especialidad solicitada de una interconsulta
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatosInt obtenerEspecialidadSolicitadaInterconsulta(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().obtenerEspecialidadSolicitadaInterconsulta(con, numeroSolicitud);
	}
	
	/**
	 * Método para obtener la fecha valoracion inicial de la solicitud
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static String consultarFechaValoracionInicial(Connection con, int codigoCuenta) 
	{
		return utilidadesDao().consultarFechaValoracionInicial(con, codigoCuenta);
	}
	
	/**
	 * Método para obtener la fecha / hora de la solicitud
	 * [0] fecha
	 * [1] hora
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * 
	 */
	public static String[] obtenerFechaHoraSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().obtenerFechaHoraSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para obtener el servicio solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerServicioSolicitud(Connection con, String numeroSolicitud)
	{
		return utilidadesDao().obtenerServicioSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para obtener el servicio solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerServicioSolicitud(String numeroSolicitud)
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		Connection con=UtilidadBD.abrirConexion();
		resultado= utilidadesDao().obtenerServicioSolicitud(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Método para obtener la subcuenta de una solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerSubCuentaSolicitudCirugia(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().obtenerSubCuentaSolicitudCirugia(con, numeroSolicitud);
	}
	
	/**
	 * Método que verifica si se puede abrir la seccion de prescripción diálisis
	 * validando por codigo de institucion y codigo de centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean deboAbrirPrescripcionDialisis(Connection con,int codigoCentroCosto,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoInstitucion",codigoInstitucion);
		return utilidadesDao().deboAbrirPrescripcionDialisis(con, campos);
	}
	
	/**
	 * Método para obtener el centro de costos solicitante / solicitado servicio de la solicitud
	 * @param con
	 * @param parametros
	 * @return 
	 */
	public static InfoDatosInt obtenerCentroCostoSoli(Connection con,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		return utilidadesDao().obtenerCentroCostoSoli(con,parametros);
	}		
	
	/**
	 * Método para consultar el codigo pk de solicitud de cirugia por servicio
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String codigoServicio
	 * @return
	 */
	public static HashMap obtenerSolCirugiaServicio(Connection con,String numeroSolicitud,String codigoServicio)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		parametros.put("servicio",codigoServicio);	
		return utilidadesDao().obtenerSolCirugiaServicio(con,parametros);
	}
	
	/**
	 * Método para obtener el codigo del médico que responde la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoMedicoRespondeSolicitud(Connection con,int numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		return utilidadesDao().obtenerCodigoMedicoRespondeSolicitud(con, campos);
	}
	
	/**
	 * Método para cargar las opciones de manejo de la interconsulta
	 * 
	 * @param con
	 * @param filtroOpciones: Si se desea filtrar por unas opciones específicas entonces se mandan los codigos separados por comas
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerOpcionesManejoInterconsulta(Connection con,String filtroOpciones)
	{
		return utilidadesDao().obtenerOpcionesManejoInterconsulta(con, filtroOpciones);
	}
	
	/**
	 * Método para verificar si existen solicitudes de medicamentos pendientes para despachar
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean existenSolicitudesMedicamentosPendientesDespachar(Connection con,int idCuenta)
	{
		return utilidadesDao().existenSolicitudesMedicamentosPendientesDespachar(con, idCuenta);
	}
	
		
	/**
	 * Método que devuelve el centro de costo que ejecuta parametrizado en la funcionalidad
	 * de "Excepciones Centros de Costo Interconsultas" si este existe
	 * @param con
	 * @param centroCostoSolicita
	 * @param servicio
	 * @param medico
	 * @return
	 */
	public static int obtenerCCEjecutaXExcepCCInter(Connection con, String centroCosto, String servicio, String medico)
	{
		return utilidadesDao().obtenerCCEjecutaXExcepCCInter(con, centroCosto, servicio, medico);
	}
	
	/**
	 * 
	 * @param servicio
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoSolicita
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerCentrosCostoEjecuta(int servicio, int institucion, int centroAtencion, int centroCostoSolicita)
	{
		return utilidadesDao().obtenerCentrosCostoEjecuta(servicio, institucion, centroAtencion, centroCostoSolicita);
	}
	
	/**
	 * 
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoEjecuta
	 * @param centroCostoSolicita
	 * @param servicio
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerProfesionalesEjecutan(int institucion, int centroAtencion, int centroCostoEjecuta, int centroCostoSolicita, int servicio)
	{
		return utilidadesDao().obtenerProfesionalesEjecutan(institucion, centroAtencion, centroCostoEjecuta, centroCostoSolicita, servicio);
	}
	
	/**
	 * Metodo para obtener los convenios y su estado asociado a un numero de solicitud 
	 * @param numSolicitud
	 * @return
	 */
	public static HashMap obtenerConvenioEstadoSolicitud(int numSolicitud)
	{
		return utilidadesDao().obtenerConvenioEstadoSolicitud(numSolicitud);
	}
	
	
	/**
	 * verifica si el usuario tiene registro con la entidad subcontratadas 
	 * @param String codigoEntidadSub
	 * @param String usuario
	 * */
	public static boolean tieneUsuarioEntidadSubcontratada(String codigoEntidadSub,String usuario)
	{
		HashMap parametros = new HashMap();
		parametros.put("entidadSub", codigoEntidadSub);
		parametros.put("usuario", usuario);
		
		return utilidadesDao().tieneUsuarioEntidadSubcontratada(parametros);		
	}
	
	/**
	 * Obtiene las especialidades de un profesional
	 * @param codigoMedico
	 * @return ArrayList<InfoDatosInt>
	 */
	public static ArrayList<InfoDatosInt> obtenerEspecialidadProfesionalEjecutan(int codigoMedico)
	{
		return utilidadesDao().obtenerEspecialidadProfesionalEjecutan(codigoMedico);
	}
	
	/**
	 * Método para obtener el ingreso de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerIngresoXNumeroSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().obtenerIngresoXNumeroSolicitud(con, numeroSolicitud);
	}
}