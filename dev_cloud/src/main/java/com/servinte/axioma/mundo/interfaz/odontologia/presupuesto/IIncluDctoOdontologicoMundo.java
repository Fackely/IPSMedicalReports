/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.odontologia.presupuesto;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.servinte.axioma.orm.IncluPresuEncabezado;

/**
 * @author Juan David Ramírez
 * @since Jan 14, 2011
 */
public interface IIncluDctoOdontologicoMundo
{

	/**
	 * Relacionar inclusiones con el encabezado de la inclusión
	 * @param dtoDcto
	 * @param encabezado
	 */
	public boolean relacionarConInclusiones(DtoPresupuestoOdontologicoDescuento dtoDcto, IncluPresuEncabezado encabezado);

}
