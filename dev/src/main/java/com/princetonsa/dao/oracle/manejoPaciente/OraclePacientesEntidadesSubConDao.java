package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.manejoPaciente.PacientesEntidadesSubConDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBasePacientesEntidadesSubCon;


public class OraclePacientesEntidadesSubConDao implements PacientesEntidadesSubConDao
{
	
	//--Atributos
		
	/**
	 * Cadena sentencia sql para insertar registros en la tabla Detalle Servicios Autorizaciones
	 * */
	/**
	 * Cadena sentencia sql para insertar registros en la tabla Detalle Servicios Autorizaciones
	 * */
	private static String strInsertarServiciosAutorizados = "INSERT INTO " +
															"det_serv_autorizados(consecutivo," +
															"					  consecutivo_pac_entidades_sub," +
															" 					  anio_consecutivo_pac_entidades," +
															"					  institucion," +
															"					  servicio," +
															"					  cantidad," +
															"				 	  autorizacion," +
															"					  fecha_autorizacion," +
															"					  responsable," +
															"					  observaciones," +
															"					  usuario_modifica," +
															"					  fecha_modifica," +
															"					  hora_modifica) " +
															"VALUES(seq_det_serv_auto.nextval,?,?,?,?,?,?,?,?,?,?,?,?)";	
	
	
	//--Fin Atributos
	
	//--Metodos
	
	/**
	 * Consulta la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return HashMap datos
	 * */
	public HashMap consultarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.consultarPacientesEntidadesSubcontratadas(con, parametros);
	}
	
	/**
	 * Actualiza la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean actualizarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.actualizarPacientesEntidadesSubcontratadas(con, parametros);
	}
	
	
	/**
	 * Actualiza el estado del registro paciente entidades subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean actualizarEstadoPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.actualizarEstadoPacientesEntidadesSubcontratadas(con, parametros);
	}
	
	
	/**
	 * Inserta informacion en la tabla Pacientes Entidades Subcontratadas 
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String cadena
	 * @return boolean
	 * */
	public boolean insertarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.insertarPacientesEntidadesSubcontratadas(con, parametros);
	}
	
	/**
	 * Consulta la informacion de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param HashMap parametros
	 * @return HashMap datos
	 * */
	public HashMap consultarServiciosAutorizados(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.consultarServiciosAutorizados(con, parametros);
	}
	
	/**
	 * Actuliza los datos de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean actualizarServiciosAutorizados(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.actualizarServiciosAutorizados(con, parametros);
	}
	
	/**
	 * Inserta datos en la tabla Detalle Servicios Autorizados
	 * @param Conenction con
	 * @param HashMap parametros
	 * */
	public boolean insertarServiciosAutorizados(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.insertarServiciosAutorizados(con, parametros, strInsertarServiciosAutorizados);
	}
	
	/**
	 * Elimina Servcios Autorizados 
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public boolean eliminarServiciosAutorizados(Connection con, HashMap parametros)
	{
		return SqlBasePacientesEntidadesSubCon.eliminarServiciosAutorizados(con, parametros);
	}
	
	/**
	 * Método implementado para reversar el registro de entidades subcontratadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public int reversarPacienteEntidadSubcontratada(Connection con,HashMap campos)
	{
		return SqlBasePacientesEntidadesSubCon.reversarPacienteEntidadSubcontratada(con, campos);
	}
	
	//--Fin Metodos	
}