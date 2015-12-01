package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEnvioInformInconsisenBD;

public interface RegistroEnvioInformInconsisenBDDao {

	public HashMap consultarIngresosPaciente(Connection con, int codigoInstitucion,int codigoPaciente);
	
	public HashMap consultarConveniosResponsables(Connection con, int codigoIngreso);
	
	public DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con, HashMap filtros);
	
	public HashMap cargarTiposInconsistencias(Connection con, int codigoInstitucion);

	public HashMap insertarInformeInconsistencia(Connection con,DtoInformeInconsisenBD informeInconsistencias,HashMap parametros);

	public HashMap modificarInformeInconsistencia(Connection con,DtoInformeInconsisenBD informeInconsistencias, HashMap parametros);

	public HashMap insertarEnvioInformeInconsistencias(Connection con, HashMap parametros);
	
	public ArrayList cargarTiposDocumentos(Connection con);
	
	/**
	   * 
	   * @param con
	   * @param parametros
	   * @return
	   */
	public boolean actualizarPatharchivoIncoBD(Connection con, HashMap parametros);
	
}
