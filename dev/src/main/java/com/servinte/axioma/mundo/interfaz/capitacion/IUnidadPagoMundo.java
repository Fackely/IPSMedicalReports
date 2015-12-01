package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.servinte.axioma.orm.UnidadPago;

/**
 * Interface para el acceso a datos de la entidad UnidadPago
 * @author Ricardo Ruiz
 *
 */
public interface IUnidadPagoMundo {
	
	/**
	 * Método utilizado para consultar el valor de unidad de pago para una fecha
	 * @param fecha
	 * @return
	 */
	public ArrayList<UnidadPago> consultarUnidadPagoPorFecha(Date fecha);

}
