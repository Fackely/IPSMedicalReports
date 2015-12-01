package com.princetonsa.dao.postgresql.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatos;
import util.ResultadoBoolean;

import com.princetonsa.dao.ordenesmedicas.RegistroIncapacidadesDao;
import com.princetonsa.dao.sqlbase.ordenesmedicas.SqlBaseRegistroIncapacidadDao;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.ordenesmedicas.DtoIncapacidad;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;

/**
 * @author Jairo Gï¿½mez Fecha Septiembre de 2009
 */

public class PostgresqlRegistroIncapacidadesDao implements RegistroIncapacidadesDao {
	public DtoRegistroIncapacidades consultarIncapacidadPorIngreso(Connection connection, int ingreso)
	{
		return SqlBaseRegistroIncapacidadDao.consultarIncapacidadPorIngreso(connection, ingreso);
	}
	
	public HashMap consultarTiposIncapacidad(Connection connection)
	{
		return SqlBaseRegistroIncapacidadDao.consultarTiposIncapacidad(connection);
	}
	
	public String insertarIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario) 
	{
		return SqlBaseRegistroIncapacidadDao.insertarIncapacidad(connection, incapacidad, fechaUsuario);
	}
	
	public String actualizarIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario)
	{
		return SqlBaseRegistroIncapacidadDao.actualizarIncapacidad(connection, incapacidad, fechaUsuario);
	}
	
	public String insertarLogIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario)
	{
		return SqlBaseRegistroIncapacidadDao.insertarLogIncapacidad(connection, incapacidad, fechaUsuario);
	}
	
	public int egresoPosteriorConReversion(Connection connection, DtoInfoFechaUsuario fechaUsuario, int cuenta)
	{
		return SqlBaseRegistroIncapacidadDao.egresoPosteriorConReversion(connection, fechaUsuario, cuenta);
	}
	
	public String actualizarEstadoIncapacidad(Connection connection, String estado, int codigoPK, DtoInfoFechaUsuario fechaHoraUsuarioAnulacion)
	{
		return SqlBaseRegistroIncapacidadDao.actualizarEstadoIncapacidad(connection, estado, codigoPK, fechaHoraUsuarioAnulacion);
	}
	
	public ArrayList<DtoIncapacidad> consultarIncapacidadesPaciente (Connection connection, int codigoPaciente)
	{
		return SqlBaseRegistroIncapacidadDao.consultarIncapacidadesPaciente(connection, codigoPaciente);
	}
	
	public ArrayList<DtoIncapacidad> consultarPacientesIncapacidad(Connection connection, DtoIncapacidad parametros){
		return SqlBaseRegistroIncapacidadDao.consultarPacientesIncapacidad(connection, parametros);
	}
	
	public ArrayList<DtoIncapacidad> consultaConveniosIngreso (Connection connection, int codigoPk){
		return SqlBaseRegistroIncapacidadDao.consultaConveniosIngreso(connection, codigoPk);
	}
	
	public InfoDatos consultarDiagnosticoEvoluciones(Connection connection, int solicitud){
		return SqlBaseRegistroIncapacidadDao.consultarDiagnosticoEvoluciones(connection, solicitud);
	}
	
	public InfoDatos consultarDiagnosticoValoraciones(Connection connection, int solicitud){
		return SqlBaseRegistroIncapacidadDao.consultarDiagnosticoValoraciones(connection, solicitud);
	}
	
	public String eliminarIncapacidadesInactivasXRegistro(Connection connection, int noIngreso) {
		return SqlBaseRegistroIncapacidadDao.eliminarIncapacidadesInactivasXRegistro(connection, noIngreso);
	}
	
	public String consultarIncapacidadesCambios(Connection connection, int noIngreso){
		return SqlBaseRegistroIncapacidadDao.consultarIncapacidadesCambios(connection, noIngreso);
	}
	
	//*********************************************************SEBASTIAN****************************************************************************
	
	/**
	 * Método implementado para activar los registros de incapacidades de un ingreso
	 */
	public ResultadoBoolean activarRegistrosIncapacidades(Connection con,DtoRegistroIncapacidades incapacidad)
	{
		return SqlBaseRegistroIncapacidadDao.activarRegistrosIncapacidades(con, incapacidad);
	}
	
	/**
	 * Método implementado para verificar si ya fueron facturados todos los servicios de una solicitud
	 */
	public String verificaSolicitudFacturada(Connection connection,
			int solicitud) {
		return SqlBaseRegistroIncapacidadDao.verificaSolicitudFacturada(connection, solicitud);
	}
}