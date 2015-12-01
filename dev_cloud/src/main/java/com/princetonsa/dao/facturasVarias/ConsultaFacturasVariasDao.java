package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaFacturasVariasDao 
{

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param factura
	 * @param estadosFactura
	 * @param tipoDeudor
	 * @param deudor
	 * @return
	 */
	HashMap BusquedaFacturas(Connection con, String fechaInicial, String fechaFinal, String factura, String estadosFactura, String tipoDeudor, String deudor);
	
	
	/**
	 * 
	 * @param con
	 * @param factura
	 * @param tipoDeudor
	 * @return
	 */
	HashMap<String, Object> consultaDetalleFactura(Connection con, int factura, String tipoDeudor);

}
