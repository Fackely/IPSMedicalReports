/**
 * 
 */
package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.servinte.axioma.orm.IncluPresuEncabezado;

/**
 * @author Juan David Ram�rez
 * @since Jan 14, 2011
 */
public interface IIncluDctoOdontologicoDAO
{

	/**
	 * Relacionar inclusiones con el encabezado de la inclusi�n
	 * @param dtoDcto DTO con los datos del descuento
	 * @param encabezado Encabezado de la inclusi�n
	 * @return true en caso de ser exitoso, false de lo contrario
	 */
	boolean relacionarConInclusiones(DtoPresupuestoOdontologicoDescuento dtoDcto, IncluPresuEncabezado encabezado);

}
