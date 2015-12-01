package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;




/**
 * @author Jhony Alexande Duque A.
 * jduque@princetonsa.com
 */

public interface  ConsultaImpresionAjustesFacturasVariasDao
{
	
	/**
	 * Metodo encargado de consultar los ajustes de facturas varias
	 * Los keys del mapa criterios son:
	 * ---------------------------------
	 * --consecutivo0 --> Opcional
	 * --factura1 --> Opcional
	 * --fechaIni2--> Opcional
	 * --fechaFin3--> Opcional
	 * --institucion4--> Requerido
	 * ----------------------------------
	 * Los key's del mapa resultado
	 * ----------------------------------
	 * codigo0_,consecutivo1_,tipoAjuste2_,
	 * fechaAjuste3_,factura4_,conceptoAjuste5_,
	 * valorAjuste6_,observaciones7_,estado8_,estaBd9_
	 */
	 public HashMap  consultaAjustesFacturasVarias (Connection connection,HashMap criterios, boolean ajusTodos);
	 
	 
		/**
		 * Metodo encargado de generar las clausulas where
		 * de la ocnsulta.
		 * @param criterios
		 * @param ajusTodos
		 * @return
		 */
	public String obtenerWhere (HashMap criterios, boolean ajusTodos);
		
	
}