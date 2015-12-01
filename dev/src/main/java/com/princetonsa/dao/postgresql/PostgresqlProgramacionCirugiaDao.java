/*
 * Creado el 18-nov-2005
 * por Julian Montoya
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.ProgramacionCirugiaDao;
import com.princetonsa.dao.sqlbase.SqlBaseProgramacionCirugia;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class PostgresqlProgramacionCirugiaDao implements ProgramacionCirugiaDao
{

	/**
	 * Metodo para retornar la información de peticion de cirugia 
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarInformacionPeticion(Connection con, int nroPeticion, int tipoConsulta)
	{
		return SqlBaseProgramacionCirugia.cargarInformacionPeticion(con, nroPeticion, tipoConsulta);
	}
	
	/**
	 * Metodo para retornar la información de las cirugias asociadas... 
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarInformacionCirugias(Connection con, int nroPeticion)
	{
		return SqlBaseProgramacionCirugia.cargarInformacionCirugias(con, nroPeticion);
	}
	
	 /**
	 * Metodo para retornar la información de las salas 
	 * @param con
	 * @return
	 */
	 public Collection cargarSalas(Connection con)
	 {
	  	return SqlBaseProgramacionCirugia.cargarSalas(con);
	 }


	/**
	 * @param codigoPersona 
	 * @param con
	 * @return
	 */
	 public Collection cargarListadoPeticionesPaciente(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta)
	 {
	  	return SqlBaseProgramacionCirugia.cargarListadoPeticionesPaciente(con, codigoPersona, centroAtencion, codigoCuenta);
	 }

	 
	 /**
 	   * Consultar los servicios asociadas a las peticiones...
	   * @param con
	   * @param codigoPersona
	   * @return
	   */
	public Collection cargarInformacionServiciosPeticion(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta)
	{
	  	return SqlBaseProgramacionCirugia.cargarInformacionServiciosPeticion(con, codigoPersona, centroAtencion, codigoCuenta);
	}
	
	/**
 	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public Collection cargarListadoPeticionesBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
			  										  String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
													  String fechaFinCirugia, int profesionalSolicita)
	{
		return SqlBaseProgramacionCirugia.cargarListadoPeticionesBusqueda(con, nroIniServicio, nroFinServicio,
				  														  fechaIniPeticion, fechaFinPeticion, fechaIniCirugia, 
																		  fechaFinCirugia, profesionalSolicita);
	}

	/**
	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda. Para la funcionalidad de consulta de cirugias. 
	 * @param con
	 * @return
	 */
	public HashMap cargarListadoConsultaPeticionesBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
			  										  String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
													  String fechaFinCirugia, int profesionalSolicita, int centroAtencion)
	{
		StringBuffer consulta = new StringBuffer();
		consulta.append(
				  "SELECT  "+
				  "pqx.codigo as codigo_peticion, "+ 
				  "getNombrePersona(pqx.paciente) as paciente, "+ 
				  "CASE WHEN to_char(pqx.fecha_cirugia, 'YYYY-MM-DD') IS NULL THEN '' ELSE to_char(pqx.fecha_cirugia, 'YYYY-MM-DD') END AS fecha_cirugia, "+ 
				  "CASE WHEN to_char(psqx.hora_inicio, 'HH24:MI') IS NULL THEN '' ELSE to_char(psqx.hora_inicio, 'HH24:MI') END AS hora_cirugia, "+
				  "CASE WHEN tp.descripcion IS NULL THEN '' ELSE tp.descripcion END AS tipo_anestesia, "+  
				  "coalesce(getempresapeticionqx(pqx.codigo),'') as empresa, "+ 
				  "s.descripcion || ' -- ' || getnomcentrocosto(pqx.centro_atencion) as sala "+ 
				  "FROM peticion_qx pqx "+	
				  "INNER JOIN programacion_salas_qx psqx ON ( psqx.peticion = pqx.codigo ) "+ 
				  "INNER JOIN salas s ON ( psqx.sala = s.consecutivo ) "+  
				  "LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo ) "+ 
				  "LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia ) "+ 
				  "WHERE pqx.estado_peticion IN ("+ConstantesBD.codigoEstadoPeticionProgramada+","+ConstantesBD.codigoEstadoPeticionReprogramada+") " +
				  "AND   pqx.programable = '"+ConstantesBD.acronimoSi+"' ");		

		
		
		return SqlBaseProgramacionCirugia.cargarListadoConsultaPeticionesBusqueda(con, nroIniServicio, nroFinServicio,
				  														  fechaIniPeticion, fechaFinPeticion, fechaIniCirugia, 
																		  fechaFinCirugia, profesionalSolicita, centroAtencion, consulta);
	}
	
	
	/**
	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda. Para la funcionalidad de consulta de cirugias.
	 * @param con
	 * @return
	 */
	public Collection accionResultadoBusquedaAvanzadaProgramadas(Connection con, int nroIniServicio, int nroFinServicio,
			  										  String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
													  String fechaFinCirugia, int profesionalSolicita)
	{
		return SqlBaseProgramacionCirugia.accionResultadoBusquedaAvanzadaProgramadas(con, nroIniServicio, nroFinServicio,
																					fechaIniPeticion, fechaFinPeticion, fechaIniCirugia, 
																					fechaFinCirugia, profesionalSolicita);
	}

	
	
 	/**
 	 * Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
 	 * 
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */public Collection cargarInformacionServiciosBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
														 String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
														 String fechaFinCirugia, int profesionalSolicita)
	{
		return SqlBaseProgramacionCirugia.cargarInformacionServiciosBusqueda(con, nroIniServicio, nroFinServicio,	
																		     fechaIniPeticion, fechaFinPeticion, fechaIniCirugia, 
																		     fechaFinCirugia, profesionalSolicita);
	}  

		/**
		 *  Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
		 * @param con
		 * @param nroIniServicio
		 * @param nroFinServicio
		 * @param fechaIniPeticion
		 * @param fechaFinPeticion
		 * @param fechaIniCirugia
		 * @param fechaFinCirugia
		 * @param profesionalSolicita
		 * @return
		 */
		public HashMap cargarListadoConsultaServiciosBusqueda(Connection con, int nroIniServicio, int nroFinServicio,
																String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
															    String fechaFinCirugia, int profesionalSolicita)
		{
			return SqlBaseProgramacionCirugia.cargarListadoConsultaServiciosBusqueda(con, nroIniServicio, nroFinServicio,	
																					 fechaIniPeticion, fechaFinPeticion, fechaIniCirugia, 
																					 fechaFinCirugia, profesionalSolicita);
		}  
	 
	 	/**
	 	 * Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
		 * @param con
		 * @return
		 */
		public Collection cargarInformacionServiciosBusquedaProgramadas(Connection con, int nroIniServicio, int nroFinServicio,
															 String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, 
															 String fechaFinCirugia, int profesionalSolicita)
		{
			return SqlBaseProgramacionCirugia.cargarInformacionServiciosBusquedaProgramadas(con, nroIniServicio, nroFinServicio,	
																			     fechaIniPeticion, fechaFinPeticion, fechaIniCirugia, 
																			     fechaFinCirugia, profesionalSolicita);
		}

		/**
	 * Metodo para insertar la programacion de una sala a una hora determinada
	 * @param con
	 * @param numeroPeticion
	 * @param fechaProgramacion
	 * @param nroSala
	 * @param horaInicioProgramacion
	 * @param horaFinProgramacion
	 * @param codigoUsuario
	 * @return
	 */
	
	public int insertarProgamacion(Connection con, int numeroPeticion, String fechaProgramacion, int nroSala, String horaInicioProgramacion, String horaFinProgramacion, String loginUsuario, int operacion)
	{
		return SqlBaseProgramacionCirugia.insertarProgamacion(con, numeroPeticion, fechaProgramacion, nroSala, horaInicioProgramacion, horaFinProgramacion, loginUsuario, operacion);
	}
	
	/**
	 *  Metodo para cargar la programacion regsitrada de cada sala
	 * @param con
	 * @param fechaProgramacion
	 * @return
	 */
	public Collection cargarProgramacionSalas(Connection con, String fechaProgramacion)
	{
		StringBuffer consulta = new StringBuffer();
		consulta.append("  SELECT * from (SELECT peticion as peticion, sala as sala, to_char(hora_inicio,'HH24:MI') as hora_inicio," +
						"						 to_char(hora_fin,'HH24:MI') as hora_fin "+  
						"						 FROM programacion_salas_qx pqx " +
						" 							  WHERE to_char(fecha_cirugia, 'YYYY-MM-DD') = ? " +
						"  UNION ALL  " +
						"  SELECT -1 as peticion, codigo_sala as sala, to_char(rango_inicial,'HH24:MI') as hora_inicio, to_char(rango_final,'HH24:MI') as hora_fin " +
						"		  FROM disponibilidad_salas ) tabla ORDER BY peticion DESC ");
		
		
		return SqlBaseProgramacionCirugia.cargarProgramacionSalas(con, fechaProgramacion, consulta.toString());
	}
	

	/**
	 * Metodo para cargar listado de las peticiones de programadas y reprogramadas
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarListadoPeticionesProgramadas(Connection con, int centroAtencion)
	{
		return SqlBaseProgramacionCirugia.cargarListadoPeticionesProgramadas(con, centroAtencion);
	}
	
	
	/**
	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda en la Reprogramacion de Cirugias   
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param estadoPeticion
	 * @param profesional
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @return
	 */
	public Collection cargarListadoPeticionesBusquedaReprogramacion(Connection con, int nroIniServicio, int nroFinServicio, String estadoPeticion,
																	  int profesional, String fechaIniCirugia, String fechaFinCirugia)
	{
		return SqlBaseProgramacionCirugia.cargarListadoPeticionesBusquedaReprogramacion(con, nroIniServicio, nroFinServicio, estadoPeticion, profesional,
																							 fechaIniCirugia, fechaFinCirugia);
	}
	
	/**
	 * Metodo para listar los servicions de las peticiones de acuerdo a unos parametros de busqueda en la Reprogramacion de Cirugias   
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param estadoPeticion
	 * @param profesional
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @return
	 */
	public Collection cargarInformacionServiciosPeticionReprogramacion(Connection con, int nroIniServicio, int nroFinServicio, String estadoPeticion,
			  																		   int profesional, String fechaIniCirugia, String fechaFinCirugia)
	{
		return SqlBaseProgramacionCirugia.cargarInformacionServiciosPeticionReprogramacion(con, nroIniServicio, nroFinServicio, estadoPeticion, profesional,
										 													 fechaIniCirugia, fechaFinCirugia);
	}

	/**
	 * Metodo para dejar disponible la peticion de cirugia de nuevo y eliminar la programacion de la misma
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public int eliminarProgramacion(Connection con, int numeroPeticion)
	{
		return SqlBaseProgramacionCirugia.eliminarProgramacion(con, numeroPeticion);
	}
	
	
	/**
	 * Metodo para cargar el codigo de la persona.
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public Collection cargarCodigoPersona(Connection con, int nroPeticion)
	{
		return SqlBaseProgramacionCirugia.cargarCodigoPersona(con, nroPeticion);
	}
	
	/**
	 * Metodo para cargar listado de las peticiones para la funcionalidad Consulta de programacaion de Cirugias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap cargarListadoPeticiones(Connection con, int codigoPersona)
	{
		return SqlBaseProgramacionCirugia.cargarListadoPeticiones(con, codigoPersona);
	}

	/**
	 * Metodo para cargar listado de servicios para la funcionalidad Consulta de programacaion de Cirugias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap cargarServiciosPeticion(Connection con, int codigoPersona)
	{
		return SqlBaseProgramacionCirugia.cargarServiciosPeticion(con, codigoPersona);
	}

	/**
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public HashMap consultaProgramacionSalasQx(Connection con, int numeroPeticion) {
		return SqlBaseProgramacionCirugia.consultaProgramacionSalasQx(con,numeroPeticion);
	}
	
	public int insertarCancelacionProgramacionSalasQx(Connection con, HashMap rep){
		return SqlBaseProgramacionCirugia.insertarCancelacionProgramacionSalasQx(con,rep);
	}

}
