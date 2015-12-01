package com.princetonsa.dao.postgresql.glosas;

import java.sql.Connection;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.glosas.ConsultarImpFacAudiDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConsultarImpFacAudiDao;

public class PostgresqlConsultarImpFacAudiDao implements ConsultarImpFacAudiDao
{
	/**
	 * Consulta para Actualizar la tabla con descripcion del archivo plano
	 */
	private static String guardarLog="INSERT INTO log_consulta_aud_glosa (" +
										"codigo," +
										"fecha_grabacion," +
										"hora_grabacion," +
										"usuario," +
										"reporte," +
										"tipo_salida," +
										"criterios, " +
										"ruta) " +
										"VALUES(nextval('seq_log_consulta_aud_glosa')," +
										"CURRENT_DATE," +
										""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
										"?," +
										"?," +
										"?," +
										"?," +
										"?) ";
	
	
	/**
	 * Metodo encargado de consultar las facturas auditadas
	 * @param connection
	 * @param consulta --> consulta a realizar
	 * @return HashMap
	 * -----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------
	 * codigoGlosa_,fechaAuditoriaBd_,
	 * fechaAuditoria_,nombreGonvenio_,
	 * preGlosa_, valorPreGlosa_,
	 * fechaElaboracionBd_,fechaElaboracion_,
	 * consecutivoFactura_, nombrePaciente_,
	 * valorTotal_
	 */
	public HashMap consultaFacturasAuditadas (Connection connection, String consulta)
	{
		return SqlBaseConsultarImpFacAudiDao.consultaFacturasAuditadas(connection, consulta);
	}
	
	/**
	 * Metodo que actualiza el Log de consulta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios)
	{
		return SqlBaseConsultarImpFacAudiDao.guardar(con, criterios, guardarLog);
	}
	
	/**
	 * Metodo que consulta detalles de factura
	 */
	public HashMap consultarFactura(Connection con, String consFactura)
	{
		return SqlBaseConsultarImpFacAudiDao.consultarFactura(con, consFactura);
	}
}