/**
 * 
 */
package com.servinte.axioma.dao.impl.odontologia.mantenimiento;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.servinte.axioma.dao.interfaz.odontologia.mantenimiento.IDescuentoOdontologicoDAO;
import com.servinte.axioma.orm.delegate.odontologia.mantenimiento.DescuentosOdonAtenDelegate;
import com.servinte.axioma.orm.delegate.odontologia.mantenimiento.DescuentosOdonDelegate;

/**
 * @author Juan David Ramírez
 * @since Jan 13, 2011
 */
public class DescuentoOdontologicoDAO implements IDescuentoOdontologicoDAO
{

	@Override
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXAtencion(
			int centroAtencion)
	{
		DescuentosOdonAtenDelegate descuentosOdonAtenDelegate=new DescuentosOdonAtenDelegate();
		return descuentosOdonAtenDelegate.validarExistenciaDescuentosOdontologicosXAtencion(centroAtencion);
	}

	@Override
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXPresupuesto(
			int centroAtencion, Date fechaVigencia)
	{
		DescuentosOdonDelegate descuentosOdonDelegate = new DescuentosOdonDelegate();
		return descuentosOdonDelegate.validarExistenciaDescuentosOdontologicosXPresupuesto(centroAtencion, fechaVigencia);
	}
	
}
