package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.servinte.axioma.orm.UnidadPago;

/**
 * Interface para el acceso a datos de la entidad UnidadPago
 * @author Ricardo Ruiz
 *
 */
public interface IUnidadPagoDAO {

	/**
	 * Método utilizado para consultar el valor de unidad de pago para una fecha
	 * @param fecha
	 * @return
	 */
	public ArrayList<UnidadPago> consultarUnidadPagoPorFecha(Date fecha);
}
