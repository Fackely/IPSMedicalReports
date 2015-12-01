/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.odontologia.presupuesto;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.servinte.axioma.orm.IncluPresuEncabezado;

/**
 * @author Juan David Ram�rez
 * @since Jan 14, 2011
 */
public interface IIncluDctoOdontologicoMundo
{

	/**
	 * Relacionar inclusiones con el encabezado de la inclusi�n
	 * @param dtoDcto
	 * @param encabezado
	 */
	public boolean relacionarConInclusiones(DtoPresupuestoOdontologicoDescuento dtoDcto, IncluPresuEncabezado encabezado);

}
