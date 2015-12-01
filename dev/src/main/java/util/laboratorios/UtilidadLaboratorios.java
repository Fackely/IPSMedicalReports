/*
 * Created on 16/11/2006
 * 
 * @author <a href="mailto:wilson@hotmail.com">Wilson Rios</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package util.laboratorios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.laboratorio.UtilidadLaboratoriosDao;

/**
 * Clase que contiene las diferentes utilidades de laboratorios
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios/a>
 */
public class UtilidadLaboratorios 
{
	/**
	 * Metodo que retorna el DaoFactory de la funcionalidad
	 * @return
	 */
	private static UtilidadLaboratoriosDao utilidadDao()  
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadLaboratoriosDao();
	}
	
	/**
	 * Metodo que actualiza las existencias de un articulo en un almacen 
	 * @param codArticulo
	 * @param codAlmacen
	 * @param nuevaExistencia
	 * @param institucion
	 * @return
	 */
	public static boolean validaCargo()
	{
		//@todo implementar el metodo
	    return true;
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param codigoEstadoHCTomaDeMuestra
	 * @param fechaCambio
	 * @param horaCambio
	 * @param loginUsuario
	 * @param estadoHistoriaClinica
	 */
	public static boolean pasarSolicitudATomaMuestras(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario,int estadoHistoriaClinica)
	{
		return utilidadDao().pasarSolicitudATomaMuestras(con,numeroSolicitud,fechaCambio,horaCambio,loginUsuario,estadoHistoriaClinica);
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @param codigoEstadoHCTomaDeMuestra
	 * @param fechaCambio
	 * @param horaCambio
	 * @param loginUsuario
	 */
	public static boolean pasarSolicitudAEnProceso(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario)
	{
		return utilidadDao().pasarSolicitudAEnProceso(con,numeroSolicitud,fechaCambio,horaCambio,loginUsuario);
	}
	
	/**
	 * Método implementado para consultar los registros de la tabla de interfaz laboratorios que no se hayan leido
	 * @param con
	 * @return
	 */
	public static HashMap consultarInterfazLaboratorios(Connection con )
	{
		return utilidadDao().consultarInterfazLaboratorios(con);
	}
	
	/**
	 * Método implementado para actualizar a leidos los registros que no se hayan leido
	 * de la tabla interfaz_laboratorio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static int actualizarLeidoInterfazLaboratorios( Connection con,String consecutivo)
	{
		return utilidadDao().actualizarLeidoInterfazLaboratorios(con,consecutivo);
	}
	
	/**
	 * Método que inserta una respuesta de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param resultados
	 * @param tipoRecargo
	 * @param observaciones
	 * @param comentario
	 * @param pathPDF
	 * @param codigoMedico
	 * @param loginUsuario 
	 * @param infoAd 
	 * @param finalizar
	 * @return
	 */
	public static boolean insertarRespuestaProcedimientos(Connection con,String numeroSolicitud,String fecha,String hora,String resultados,int tipoRecargo,String observaciones,String comentario,String pathPDF,String codigoMedico,String loginUsuario, HashMap infoAd, boolean finalizar)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("fechaEjecucion",fecha);
		campos.put("horaEjecucion",hora);
		campos.put("resultados",resultados);
		campos.put("tipoRecargo",tipoRecargo+"");
		campos.put("observaciones",observaciones);
		campos.put("comentario",comentario);
		campos.put("pathPDF",pathPDF);
		campos.put("codigoMedico",codigoMedico);
		boolean resp =  utilidadDao().insertarRespuestaProcedimientos(con,campos,finalizar);
		
		if(Utilidades.esSolicitudPYP(con,Integer.parseInt(numeroSolicitud))&&resp)
		{
			String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,Integer.parseInt(numeroSolicitud));
			Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPEjecutado,loginUsuario,"");
			Utilidades.actualizarAcumuladoPYP(con,numeroSolicitud,infoAd.get("centro_atencion").toString());
		}
		
		
		return resp;
	}
	
	/**
	 * Método que valida el tiempo de reproceso
	 * Si es True es válido
	 * Si es Falso se superó el tiempo de reproceso
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaReproceso
	 * @param horaReproceso
	 * @return
	 */
	public static boolean validarTiempoReproceso(Connection con,String numeroSolicitud,String fechaReproceso,String horaReproceso)
	{
		return utilidadDao().validarTiempoReproceso(con,numeroSolicitud,fechaReproceso,horaReproceso);
	}
	
	/**
	 * Método para obtener informacion adicional de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap getInformacionAdicionalSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadDao().getInformacionAdicionalSolicitud(con,numeroSolicitud); 
	}
	
	/**
	 * Método implementado para insertar el log de inconsistencias
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @param fecha
	 * @param hora
	 * @param descripcion
	 * @param consecutivo
	 * @return
	 */
	public static int insertarLogInconsistencias(Connection con,String numeroSolicitud,int estado,String fecha,String hora,String descripcion,String consecutivo)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("estado",estado+"");
		campos.put("fecha",fecha);
		campos.put("hora",hora);
		campos.put("descripcion",descripcion);
		campos.put("consecutivo",consecutivo);
		return utilidadDao().insertarLogInconsistencias(con,campos);
	}
	
	/**
	 * Método para obtener el codigo de laboratorio del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerCodigoLaboratorioServicio(Connection con,int codigoServicio)
	{
		return utilidadDao().obtenerCodigoLaboratorioServicio(con, codigoServicio);
	}
	

}
