package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;

public interface ConsultaEnvioInconsistenciasenBDDao {

	public HashMap consultarIngresosPaciente(Connection con, int codigoInstitucion,int codigoPaciente);

	public DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con,HashMap filtros);

	public HashMap insertarEnvioInformeInconsistencias(Connection con,HashMap parametros);

	public ArrayList<DtoInformeInconsisenBD> getListadoInformeInconsistencias(Connection con, HashMap parametros);

	public HashMap consultaConvenios(Connection con);


}
