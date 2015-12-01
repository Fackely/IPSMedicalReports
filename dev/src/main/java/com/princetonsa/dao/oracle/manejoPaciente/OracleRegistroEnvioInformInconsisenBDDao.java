package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.manejoPaciente.RegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;

public class OracleRegistroEnvioInformInconsisenBDDao implements
		RegistroEnvioInformInconsisenBDDao {

	/**
	 * Cadena Sql que Inserta un envio de Informe de Inconsistencias en BD
	 */
	public static String ingresarEnvioInformeIncosist="INSERT INTO envio_info_inconsistencias " +
						 "(codigo_pk, " +
						 "informe_inconsist, " + //1
						 "fecha_envio, " +//2
						 "hora_envio, " +//3
						 "usuario_envio, " +//4
						 "convenio_envio, " +//5
						 "empresa_envio, " +//6
						 "medio_envio," + // 7
						 "path_archivo ) " +//8
						 "VALUES (manejopaciente.seq_envio_info_inconsistencias.nextval, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?)";
	
	/**
	 * Cadena Sql que Inserta la variable de inconsistencia en la BD
	 */
	public static String ingresarVariablesIncorrectasInformeInconsist="INSERT INTO info_incon_var_incorrecta " +
						"(codigo_pk, " + //1
						"informe_inconsistencias, " +//2
						"tipo_variable, " +//3
						"valor ) " +//4
						"VALUES (manejopaciente.seq_info_incon_var_incorrecta.nextval, ?, ?, ?)";
	
	
	@Override
public HashMap consultarIngresosPaciente(Connection con,int codigoInstitucion, int codigoPaciente) {
		
		return SqlBaseRegistroEnvioInformInconsisenBDDao.consultarIngresosPaciente(con, codigoInstitucion, codigoPaciente);
	}

	@Override
public HashMap consultarConveniosResponsables(Connection con, int codigoIngreso) {
		
		return  SqlBaseRegistroEnvioInformInconsisenBDDao.consultarConveniosResponsables(con, codigoIngreso);
	}

	@Override
	public DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con,HashMap filtros) {
		
		return  SqlBaseRegistroEnvioInformInconsisenBDDao.cargarInformeIncosistencias(con, filtros);
	}

	@Override
	public HashMap cargarTiposInconsistencias(Connection con,int codigoInstitucion) {
		
		return  SqlBaseRegistroEnvioInformInconsisenBDDao.cargarTiposInconsistencias(con, codigoInstitucion);
	}

	@Override
	public HashMap insertarInformeInconsistencia(Connection con,DtoInformeInconsisenBD informeInconsistencias,HashMap parametros) {
		
		return  SqlBaseRegistroEnvioInformInconsisenBDDao.insertarInformeInconsistencia(con,informeInconsistencias,parametros,ingresarEnvioInformeIncosist, ingresarVariablesIncorrectasInformeInconsist);
	}

	@Override
	public HashMap modificarInformeInconsistencia(Connection con,
			DtoInformeInconsisenBD informeInconsistencias, HashMap parametros) {
		
		return SqlBaseRegistroEnvioInformInconsisenBDDao.modificarInformeInconsistencia(con,informeInconsistencias,parametros,ingresarEnvioInformeIncosist, ingresarVariablesIncorrectasInformeInconsist);
	}

	@Override
	public HashMap insertarEnvioInformeInconsistencias(Connection con,HashMap parametros) {
		
		return  SqlBaseRegistroEnvioInformInconsisenBDDao.insertarEnvioInformeInconsistencias(con,parametros,ingresarEnvioInformeIncosist);
	}

	@Override
	public ArrayList cargarTiposDocumentos(Connection con) {
		
		return SqlBaseRegistroEnvioInformInconsisenBDDao.cargarTiposDocumentos(con);
	}

	/**
	   * 
	   * @param con
	   * @param parametros
	   * @return
	   */
	public boolean actualizarPatharchivoIncoBD(Connection con, HashMap parametros)
	{
		return SqlBaseRegistroEnvioInformInconsisenBDDao.actualizarPatharchivoIncoBD(con, parametros);
	}
	
}
