package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.PacientesEntidadesSubConDao;


/**
 * @author Jose Eduardo Arias Doncel
 * */
public class PacientesEntidadesSubCon
{
	//--Atributos
	
	/***
	 * indice del mapa de Servicios Autorizados 
	 * */
	private static String [] indicesServiciosMap = {"consecutivo_","solicitud_","servicio_","nombreServicio_","cantidad_","autorizacionServicio_","fechaAutorizacion_","horaAutorizacion_","nombreResponsable_","observaciones_","estabd_"};
	
	//----------
	//--Metodos	
	
	/**
	 *  
	 * */
	public static PacientesEntidadesSubConDao PacientesEntidadesSubConDao() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPacientesEntidadesSubConDao();
	}
	
	
	/**
	 * Consulta la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param int Institucion
	 * @param String consecutivo O VACIO
	 * @param String Año Consecutivo O VACIO
	 * @param int codigoPersona o Numero Nunca Valido (-1)
	 * @param String estadoRegistro O VACIO
	 * @param String codigoEntidadSub O VACIO,
	 * @param String codigoConvenio O VACIO
	 * @param String fechaInicialBusqueda O VACIO
	 * @param String fechaFinalBusqueda O VACIO
	 * @param String acronimoEstado O VACIO
	 * @param String loginUsuario O VACIO
	 * @param boolean esListado
	 * */
	public static HashMap consultarPacientesEntidadesSubcontratadas(
																	Connection con,
																	int institucion,
																	String consecutivo,
																	String anioConsecutivo,
																	String codigoPaciente, 
																	String estadoRegistro,
																	String codigoEntidadSub,
																	String codigoConvenio,
																	String fechaInicialBusqueda,
																	String fechaFinalBusqueda,
																	String acronimoEstado,
																	String loginUsuario,
																	boolean esListado )
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("consecutivo",consecutivo);
		parametros.put("anioConsecutivo",anioConsecutivo);
		parametros.put("codigoPaciente",codigoPaciente);
		
		//Parametros de la busqueda Generica
		parametros.put("codigoEntidadSub",codigoEntidadSub);
		parametros.put("codigoConvenio",codigoConvenio);
		parametros.put("fechaInicialBusqueda",UtilidadFecha.conversionFormatoFechaABD(fechaInicialBusqueda));
		parametros.put("fechaFinalBusqueda",UtilidadFecha.conversionFormatoFechaABD(fechaFinalBusqueda));
		parametros.put("acronimoEstado",acronimoEstado);
		parametros.put("loginUsuario",loginUsuario);
		
		parametros.put("estado",estadoRegistro);
		
		if(esListado)
			parametros.put("listado",ConstantesBD.acronimoSi);
		else
			parametros.put("listado",ConstantesBD.acronimoNo);
		
		return PacientesEntidadesSubConDao().consultarPacientesEntidadesSubcontratadas(con, parametros);
	}
	
	/**
	 * Actualiza la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public static boolean actualizarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		return PacientesEntidadesSubConDao().actualizarPacientesEntidadesSubcontratadas(con, parametros);
	}
	
	/**
	 * Actualiza el estado del registro paciente entidades subcontratadas
	 * @param Connection con
	 * @param String estado
	 * @param String consecutivo
	 * @param String anioConsecutivo
	 * @param String observacionesAnulacion
	 * @param String loginUsuario
	 * @return boolean 
	 * */
	public static boolean actualizarEstadoPacientesEntidadesSubcontratadas(Connection con,
			String estado, 
			String consecutivo, 
			String anioConsecutivo,
			String observacionesAnulacion,
			String loginUsuario)
	{
		HashMap parametros = new HashMap();
		parametros.put("estado",estado);
		parametros.put("consecutivo",consecutivo);
		parametros.put("anioConsecutivo",anioConsecutivo);
		parametros.put("observAnulacion",observacionesAnulacion);
		parametros.put("usuarioModifica",loginUsuario);
		parametros.put("fechaModifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica",UtilidadFecha.getHoraActual());
		
		return PacientesEntidadesSubConDao().actualizarEstadoPacientesEntidadesSubcontratadas(con, parametros); 
	}
	
	/**
	 * Inserta informacion en la tabla Pacientes Entidades Subcontratadas 
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String cadena
	 * @return boolean
	 * */
	public static boolean insertarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		return PacientesEntidadesSubConDao().insertarPacientesEntidadesSubcontratadas(con, parametros);
	}
	
	/**
	 * Consulta la informacion de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param int Institucion
	 * @param String consecutivo de Servicios Autorizados o vacia
	 * @param String consecutivo de Pacientes Entidades SubContratadas o ""
	 * @param String Año de consecutivo de Pacientes Entidades SubContratadas o ""
	 * @return HashMap datos
	 * */
	public static HashMap consultarServiciosAutorizados(Connection con,
			int institucion,
			String consecutivoServiciosAuto,
			String consecutivoPacEntidadesSub,
			String anioConsecutivoPacEntidadesSub)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		parametros.put("consecutivo",consecutivoServiciosAuto);
		parametros.put("consecutivoPacEntidadesSub",consecutivoPacEntidadesSub);
		parametros.put("anioConsecutivoPacEntidadesSub",anioConsecutivoPacEntidadesSub);
		
		parametros = PacientesEntidadesSubConDao().consultarServiciosAutorizados(con, parametros);
		parametros.put("INDICES_MAPA",indicesServiciosMap);		
		return parametros;
	}
	
	/**
	 * Actuliza los datos de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public static boolean actualizarServiciosAutorizados(Connection con, HashMap parametros)
	{
		return PacientesEntidadesSubConDao().actualizarServiciosAutorizados(con, parametros);
	}
	
	/**
	 * Inserta datos en la tabla Detalle Servicios Autorizados
	 * @param Conenction con
	 * @param HashMap parametros
	 * */
	public static boolean insertarServiciosAutorizados(Connection con, HashMap parametros)
	{
		return PacientesEntidadesSubConDao().insertarServiciosAutorizados(con, parametros);
	}		
	
	/**
	 * Elimina Servcios Autorizados 
	 * @param Connection con
	 * @param String consecutivoRegServicio
	 * @return boolean 
	 * */
	public static boolean eliminarServiciosAutorizados(Connection con, String consecutivoRegServicio)
	{
		HashMap parametros = new HashMap();
		parametros.put("consecutivo",consecutivoRegServicio);
		return PacientesEntidadesSubConDao().eliminarServiciosAutorizados(con, parametros);
	}
	
	/**
	 * Método implementado para reversar el registro de entidades subcontratadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int reversarPacienteEntidadSubcontratada(Connection con,String consecutivo,String loginUsuario)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivo", consecutivo);
		campos.put("usuario",loginUsuario);
		return PacientesEntidadesSubConDao().reversarPacienteEntidadSubcontratada(con, campos);
	}


	/**
	 * @return the indicesServiciosMap
	 */
	public static String[] getIndicesServiciosMap() {
		return indicesServiciosMap;
	}


	/**
	 * @param indicesServiciosMap the indicesServiciosMap to set
	 */
	public static void setIndicesServiciosMap(String[] indicesServiciosMap) {
		PacientesEntidadesSubCon.indicesServiciosMap = indicesServiciosMap;
	}
	
	
	//--Fin Metodos		
}