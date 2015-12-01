package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultarImpFacAudiDao
{
	
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
	public HashMap consultaFacturasAuditadas (Connection connection, String consulta);
	
	/**
	 * Metodo que actualiza el Log de consulta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios);
	
	/**
	 * Metodo que consulta detalles de factura
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarFactura(Connection con, String consFactura);
		
}