/**
 * 
 */
package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluDctoOdontologicoDAO;
import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.delegate.odontologia.presupuesto.IncluDctoOdontologicoDelegate;

/**
 * @author Juan David Ramírez
 * @since Jan 14, 2011
 */
public class IncluDctoOdontologicoDAO implements IIncluDctoOdontologicoDAO
{

	@Override
	public boolean relacionarConInclusiones(
			DtoPresupuestoOdontologicoDescuento dtoDcto,
			IncluPresuEncabezado encabezado)
	{
		IncluDctoOdontologicoDelegate incluDelegate=new IncluDctoOdontologicoDelegate();
		return incluDelegate.relacionarConInclusiones(dtoDcto, encabezado);
	}

}
