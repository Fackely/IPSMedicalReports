package com.princetonsa.dao.oracle.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.glosas.ConsultarImprimirGlosasSinRespuestaDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConsultarImprimirGlosasSinRespuestaDao;
import com.princetonsa.dto.glosas.DtoGlosa;


public class OracleConsultarImprimirGlosasSinRespuestaDao implements
		ConsultarImprimirGlosasSinRespuestaDao {

	/**
	 * Consulta para Actualizar la tabla con descripcion del archivo plano glosas sin respuesta
	 */
	private static String guardarLog="INSERT INTO log_glosas_sin_resp (" +
										"codigo," +
										"fecha_grabacion," +
										"hora_grabacion," +
										"usuario," +
										"reporte," +
										"tipo_salida," +
										"criterios, " +
										"ruta) " +
										"VALUES(seq_log_glosas_sin_resp.nextval," +
										"CURRENT_DATE," +
										""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
										"?," +
										"?," +
										"?," +
									 	"?," +
										"?) ";
	
	
	@Override
	public ArrayList<DtoGlosa> consultarListadoGlosas(Connection con, String filtroConvenio, String filtroContrato, String fechaInicial, String fechaFinal, String indicativo, String consecutivoFactura) {
		// TODO Auto-generated method stub
		return SqlBaseConsultarImprimirGlosasSinRespuestaDao.consultarListadoGlosas(con,filtroConvenio,filtroContrato,fechaInicial,fechaFinal, indicativo,consecutivoFactura);
	}

	@Override
	public String cadenaConsultaGlosasSinResp(HashMap parametros) {
		// TODO Auto-generated method stub
		return SqlBaseConsultarImprimirGlosasSinRespuestaDao.cadenaConsultaGlosasSinResp( parametros);
	}

	@Override
	public boolean guardar(Connection con, HashMap criterios) {
		// TODO Auto-generated method stub
		return SqlBaseConsultarImprimirGlosasSinRespuestaDao.guardarLog(con, criterios, guardarLog);
	}

	
}
