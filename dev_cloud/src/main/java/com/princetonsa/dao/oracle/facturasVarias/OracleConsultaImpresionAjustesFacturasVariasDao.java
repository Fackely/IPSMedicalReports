package com.princetonsa.dao.oracle.facturasVarias;


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.ConsultaImpresionAjustesFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseConsultaImpresionAjustesFacturasVariasDao;



/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class OracleConsultaImpresionAjustesFacturasVariasDao implements ConsultaImpresionAjustesFacturasVariasDao
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
	 public HashMap  consultaAjustesFacturasVarias (Connection connection,HashMap criterios, boolean ajusTodos)
	 {
		 return SqlBaseConsultaImpresionAjustesFacturasVariasDao.consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
	 }
	
	/**
	 * Metodo encargado de generar las clausulas where
	 * de la ocnsulta.
	 * @param criterios
	 * @param ajusTodos
	 * @return
	 */
	public String obtenerWhere (HashMap criterios, boolean ajusTodos)
	{
		return SqlBaseConsultaImpresionAjustesFacturasVariasDao.obtenerWhere(criterios, ajusTodos);
	}
}