package com.princetonsa.dao.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatos;
import util.ResultadoBoolean;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.ordenesmedicas.DtoIncapacidad;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;


/**
 * @author Jairo Gï¿½mez Fecha Octubre de 2009
 */

public interface RegistroIncapacidadesDao {

	public DtoRegistroIncapacidades consultarIncapacidadPorIngreso(Connection connection, int ingreso);
	
	public HashMap consultarTiposIncapacidad(Connection connection);
	
	public String insertarIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario);
	
	public String actualizarIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario);
	
	public String insertarLogIncapacidad(Connection connection, DtoRegistroIncapacidades incapacidad, DtoInfoFechaUsuario fechaUsuario);
	
	public int egresoPosteriorConReversion(Connection connection, DtoInfoFechaUsuario fechaUsuario, int cuenta);
	
	public String actualizarEstadoIncapacidad(Connection connection, String estado, int codigoPK, DtoInfoFechaUsuario fechaHoraUsuarioAnulacion);
	
	public ArrayList<DtoIncapacidad> consultarIncapacidadesPaciente (Connection connection, int codigoPaciente);
	
	public ArrayList<DtoIncapacidad> consultarPacientesIncapacidad(Connection connection, DtoIncapacidad parametros);
	
	public ArrayList<DtoIncapacidad> consultaConveniosIngreso (Connection connection, int codigoPk);
	
	public InfoDatos consultarDiagnosticoValoraciones(Connection connection, int solicitud);
	
	public InfoDatos consultarDiagnosticoEvoluciones(Connection connection, int solicitud);
	
	public String eliminarIncapacidadesInactivasXRegistro(Connection connection, int noIngreso);
	
	public String consultarIncapacidadesCambios(Connection connection, int noIngreso);
	
	/**
	 * Método implementado para activar los registros de incapacidades de un ingreso
	 */
	public ResultadoBoolean activarRegistrosIncapacidades(Connection con,DtoRegistroIncapacidades incapacidad);
	
	/**
	 * Método implementado para verificar si ya fueron facturados todos los servicios de una solicitud
	 */
	public String verificaSolicitudFacturada(Connection connection, int solicitud);
}
