
package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoProcesoFacturacionPresupuestoCapitacion;
import com.servinte.axioma.orm.Facturas;

/**
 * Interfaz donde se define el comportamiento del
 * DAO para las funciones de {@link Facturas}
 * 
 * @author Cristhian Murillo
 *
 */
public interface IFacturasDAO 
{

	
	/**
	 * M�todo que se encarga de listar todas als facturas {@link Facturas}
	 * 
	 * @param historicoEncabezado
	 * @author Cristhian Murillo
	 * @return ArrayList<Facturas>
	 */
	public ArrayList<Facturas> listarTodas();
	
	

	/**
	 * M�todo que se encarga de listar todas als facturas {@link Facturas}
	 * 
	 * @param historicoEncabezado
	 * @author Cristhian Murillo
	 * @return ArrayList<DtoFacturasCapitacion>
	 */
	public ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> buscarFacturasPorRangoFecha (DtoProcesoFacturacionPresupuestoCapitacion parametros);
	
	
}