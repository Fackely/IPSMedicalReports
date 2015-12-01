package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.manejoPaciente.ConsultaEnvioInconsistenciasenBDDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultaEnvioInconsistenciasenBDDao;
import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;

public class OracleConsultaEnvioInconsistenciasenBDDao implements
		ConsultaEnvioInconsistenciasenBDDao {

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
						 "path_archivo ) " +// 8
						 "VALUES (manejopaciente.seq_envio_info_inconsistencias.nextval, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?)"; 
	
	/**
	 * Cadena Sql que realiza la consulta de Convenios activos y con Indicador de Reporte Inconsistencias en Si
	 */
	private static String consultaConveniosSrt="SELECT " +
						  "codigo AS codconvenio, "+
						  "nombre AS nombreconvenio, "+
						  "coalesce(reporte_incon_bd,'"+ConstantesBD.acronimoNo+"') AS  reporteinconbd, " +
						  "activo AS activo "+
						  "FROM convenios " +
						  "WHERE "+ 
						  "activo = 1  AND reporte_incon_bd='"+ConstantesBD.acronimoSi+"' "; 
	
	@Override
	public HashMap consultarIngresosPaciente(Connection con,int codigoInstitucion, int codigoPaciente) {
		
		return SqlBaseConsultaEnvioInconsistenciasenBDDao.consultarIngresosPaciente(con, codigoInstitucion, codigoPaciente);
	}

	@Override
	public DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con,
			HashMap filtros) {
		
		return SqlBaseConsultaEnvioInconsistenciasenBDDao.cargarInformeIncosistencias(con,filtros);
	}

	@Override
	public HashMap insertarEnvioInformeInconsistencias(Connection con,
			HashMap parametros) {
		
		return SqlBaseConsultaEnvioInconsistenciasenBDDao.insertarEnvioInformeInconsistencias( con, parametros, ingresarEnvioInformeIncosist);
	}

	@Override
	public ArrayList<DtoInformeInconsisenBD> getListadoInformeInconsistencias(Connection con, HashMap parametros) {
		
		return SqlBaseConsultaEnvioInconsistenciasenBDDao.listadoInformeInconsistencias(con,parametros);
	}
	
	
	 public  HashMap consultaConvenios(Connection con)
	 {
		 String cadenaConsulta= consultaConveniosSrt;
		 return SqlBaseConsultaEnvioInconsistenciasenBDDao.consultaConvenios(con, cadenaConsulta);
	 }


}
