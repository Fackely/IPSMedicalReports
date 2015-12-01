/**
 * 
 */
package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluDctoOdontologicoDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluDctoOdontologicoMundo;
import com.servinte.axioma.orm.IncluPresuEncabezado;

/**
 * @author Juan David Ramírez
 * @since Jan 14, 2011
 */
public class IncluDctoOdontologicoMundo implements IIncluDctoOdontologicoMundo
{

	@Override
	public boolean relacionarConInclusiones(
			DtoPresupuestoOdontologicoDescuento dtoDcto,
			IncluPresuEncabezado encabezado)
	{
		IIncluDctoOdontologicoDAO incluDctoOdontologicoDAO=PresupuestoFabricaDAO.crearIncluDctoOdontologicoDAO();
		return incluDctoOdontologicoDAO.relacionarConInclusiones(dtoDcto, encabezado);
	}

}
