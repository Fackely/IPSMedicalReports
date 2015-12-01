package com.princetonsa.dao.cartera;

import java.sql.Connection;
import java.util.HashMap;



/**
 * @author Jhony Alexande Duque A.
 * jduque@princetonsa.com
 */

public interface RecaudoCarteraEventoDao 
{

	/**
	 * Metodo encargado de consultar el reporte 
	 * Resumido por tipo de convenio.
	 * @param criterios
	 * -----------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoConvenio3 --> Opcional
	 * -- convenio4 --> Opcional
	 * -- tipoSalida5 --> Requerido
	 * -- institucion6 --> Requerido
	 * ----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------------
	 * tipoConvenio0_, tipoDoc1_,documento2_,
	 * fechaRecaudo3_,valorRecaudo4_
	 */
	public HashMap consultaReporteResumidoXTipoConvenio (Connection connection, HashMap criterios);
	
	
	/**
	 * Metodo encargado de consultar el reporte 
	 * Resumido por convenio.
	 * @param criterios
	 * -----------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoConvenio3 --> Opcional
	 * -- convenio4 --> Opcional
	 * -- tipoSalida5 --> Requerido
	 * -- institucion6 --> Requerido
	 * ----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------------
	 * tipoConvenio0_, tipoDoc1_,documento2_,
	 * fechaRecaudo3_,valorRecaudo4_,convenio5_
	 */
	public HashMap consultarReporteResumidoXConvenio (Connection connection, HashMap criterios);
	
	/**
	 * Metodo encargado de insertar un log cuango
	 * se genera un reporte
	 * -----------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------------------
	 * -- reporte7 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoSalida5 --> Requerido
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoConvenio3 --> Requerido
	 * -- convenio4 --> Requerido
	 * -- nombreReporte8 --> Requerido
	 * -- institucion6 --> Requerido
	 * -- usuario9 --> Requerido
	 */
	public boolean insertarLog (Connection connection,HashMap criterios);
	
	/**
	 * Metodo encargado de de consultar los datos del reporte
	 * detallado por factura
	 * @param criterios
	 * @param connection
	 * -----------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoConvenio3 --> Opcional
	 * -- convenio4 --> Opcional
	 * -- tipoSalida5 --> Requerido
	 * -- institucion6 --> Requerido
	 * ----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------------
	 * tipoConvenio0_, tipoDoc1_,documento2_,
	 * fechaRecaudo3_,valorRecaudo4_,convenio5_
	 * factura6_,fechaFactura7_
	 * @return
	 */
	public HashMap consultarReporteDetalladoXFactura (Connection connection, HashMap criterios);
	
	
	
}