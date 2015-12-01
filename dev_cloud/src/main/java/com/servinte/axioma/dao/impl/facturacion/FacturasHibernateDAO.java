
package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoProcesoFacturacionPresupuestoCapitacion;
import com.servinte.axioma.dao.interfaz.facturacion.IFacturasDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoricoEncabezadoDAO;
import com.servinte.axioma.orm.Facturas;
import com.servinte.axioma.orm.delegate.facturacion.FacturasDelegate;

/**
 * Implementación de la interfaz {@link IHistoricoEncabezadoDAO}
 * 
 * @author Cristhian Murillo
 *
 */
public class FacturasHibernateDAO implements IFacturasDAO{

	
	private FacturasDelegate facturasDelegate;

	
	/**
	 * Constructor de la Clase
	 */
	public FacturasHibernateDAO() 
	{
		facturasDelegate =  new FacturasDelegate();
	}
	
	
	@Override
	public ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> buscarFacturasPorRangoFecha (DtoProcesoFacturacionPresupuestoCapitacion parametros)
	{
		return facturasDelegate.buscarFacturasPorRangoFecha(parametros);
	}


	@Override
	public ArrayList<Facturas> listarTodas() {
		return facturasDelegate.listarTodas();
	}

	
}
