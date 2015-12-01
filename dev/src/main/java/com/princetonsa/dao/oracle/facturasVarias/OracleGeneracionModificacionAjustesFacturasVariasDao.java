package com.princetonsa.dao.oracle.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.GeneracionModificacionAjustesFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseGeneracionModificacionAjustesFacturasVariasDao;



/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class OracleGeneracionModificacionAjustesFacturasVariasDao implements GeneracionModificacionAjustesFacturasVariasDao
{
	/**
	 * Metodo encargado de consultar los ajustes de facturas varias
	 * Los keys del mapa criterios son:
	 * ---------------------------------
	 * --consecutivo0_ --> Opcional
	 * --factura1_ --> Opcional
	 * --fechaIni2_--> Opcional
	 * --fechaFin3_--> Opcional
	 * --institucion4_--> Requerido
	 * ----------------------------------
	 * Los key's del mapa resultado
	 * ----------------------------------
	 * codigo0_,consecutivo1_,tipoAjuste2_,
	 * fechaAjuste3_,factura4_,conceptoAjuste5_,
	 * valorAjuste6_,observaciones7_,estado8_,estaBd9_
	 */
	 public HashMap  consultaAjustesFacturasVarias (Connection connection,HashMap criterios, boolean ajusTodos)
	 {
		 return SqlBaseGeneracionModificacionAjustesFacturasVariasDao.consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
	 }
	 
	 /**
		 * Metodo encargado de consultar  las facturas varias
		 * los key's del mapa criterios son:
		 * ----------------------------------------
		 * --factura1
		 * --fechaIni2
		 * --fechaFin3
		 * --institucion4
		 * --tipoDeudor5
		 * --deudor6
		 * LOS KEYS DEL MAPA RESULTADO:
		 * ---------------------------------------
		 * consecutivo0_,codigoFacVar1_,valorFactura2_
		 * fecha3_,nomEstadoFactura4_,estadoFactura5_
		 * nomDeudor6_,deudor7_,nomConcepto8_,concepto9_
		 * 
		 */
		public  HashMap consultaFacturasVarias (Connection connection,HashMap criterios)
		{
			return SqlBaseGeneracionModificacionAjustesFacturasVariasDao.consultaFacturasVarias(connection, criterios);
		}
		
		 
		 /**
		  * Metodo encargado de ingresar los datos de ajustes
		  * facturas varias
		  * @param connection
		  * @param datos
		  * -------------------------------
		  * Key's del mapa datos
		  * -------------------------------
		  * --consecutivo1_ --> Requerido
		  * --institucion10_ --> Requerido
		  * --tipoAjuste2_ --> Requerido
		  * --fechaAjuste3_ --> Requerido
		  * --factura4_ --> Requerido
		  * --conceptoAjuste5_ --> Requerido
		  * --valorAjuste6_ --> Requerido
		  * --observaciones7_ --> Opcional
		  * --estado8_ --> Requerido
		  * --usuarioModifica11_ --> Requerido
		  * @return
		  */
		 public int insertarAjustesFacturasVarias (Connection connection,HashMap datos)
		 {
			 return SqlBaseGeneracionModificacionAjustesFacturasVariasDao.insertarAjustesFacturasVarias(connection, datos);
		 }
		 
		 	/**
			 * Metodo encargado de modificar un ajuste.
			 * @param connection
			 * @param datos
			 * ---------------------------
			 * KEY'S DEL MAPA DATOS
			 * ---------------------------
			 * --  conceptoAjuste5_
			 * -- fechaAjuste3_
			 * -- valorAjuste6_
			 * -- observaciones7_
			 * -- codigo0_
			 */
			public boolean modificarAjuste (Connection connection, HashMap datos)
			{
				return SqlBaseGeneracionModificacionAjustesFacturasVariasDao.modificarAjuste(connection, datos);
			}
	
}