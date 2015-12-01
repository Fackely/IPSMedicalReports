/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.odontologia.facturacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;

/**
 * @author Juan David Ram�rez
 * @since Jan 13, 2011
 */
public interface IDescuentoOdontologicoMundo
{
	/**
	 * Valida si existen descuentos odontol�gicos por atenci�n
	 * @param centroAtencion Centro de atenci�n donde se desean consultar
	 * @return Lista con los descuentos existentes
	 */
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXAtencion(int centroAtencion);

	/**
	 * Valida si existen descuentos odontol�gicos por presupuesto
	 * @param centroAtencion Centro de atenci�n donde se desean consultar
	 * @param fechaVigencia Fecha de vigencia de las promociones
	 * @return Lista con los descuentos vigentes
	 */
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXPresupuesto(int centroAtencion, Date fechaVigencia);

}
