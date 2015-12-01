/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.servinte.axioma.orm.IncluDctoOdontologico;
import com.servinte.axioma.orm.IncluDctoOdontologicoHome;
import com.servinte.axioma.orm.IncluPresuEncabezado;

/**
 * @author Juan David Ramírez
 * @since Jan 14, 2011
 */
public class IncluDctoOdontologicoDelegate extends IncluDctoOdontologicoHome
{

	/**
	 * Relacionar la solicitud de descuento con las inclusiones
	 * @param dtoDcto Dto con los datos del descuento
	 * @param encabezado Encabezado de la inclusión
	 * @return true en caso de ingresar correctamente, false de lo contrario
	 */
	public boolean relacionarConInclusiones(DtoPresupuestoOdontologicoDescuento dtoDcto, IncluPresuEncabezado encabezado)
	{
		IncluDctoOdontologico descuento=new IncluDctoOdontologico();
		descuento.setIncluPresuEncabezado(encabezado);
		descuento.setPresupuestoDctoOdon(new PresupuestoDctoOdonDelegate().findById(dtoDcto.getCodigo().longValue()));
		
		try{
			this.persist(descuento);
		}
		catch (Exception e) {
			Log4JManager.error("Error relacionado el descuento con la inclusión", e);
			return false;
		}
		
		return true;
	}

	
}
