package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaPaquetesFacturadosDao 
{

	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoPaquete
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	HashMap consultarPaquetesFacturados(Connection con, String codigoConvenio, String codigoPaquete, String fechaInicial, String fechaFinal);
	
	/**
	 * 
	 * @param con
	 * @param detalleFactura
	 * @return
	 */
	HashMap consultarDetallePaquetes(Connection con, String detalleFactura);
	
	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	HashMap consultarAsociosCirugia(Connection con, String solicitud, String servicioCx, String codDetFactura);

	
	
}
