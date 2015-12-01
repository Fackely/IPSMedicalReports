/**
 * 
 */
package com.servinte.axioma.mundo.impl.odontologia.facturacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.servinte.axioma.dao.fabrica.odontologia.MantenimientoOdontologiaFabricaDao;
import com.servinte.axioma.dao.interfaz.odontologia.mantenimiento.IDescuentoOdontologicoDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.facturacion.IDescuentoOdontologicoMundo;

/**
 * @author Juan David Ramírez
 * @since Jan 13, 2011
 */
public class DescuentoOdontologicoMundo implements IDescuentoOdontologicoMundo
{

	@Override
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXAtencion(
			int centroAtencion)
	{
		IDescuentoOdontologicoDAO descuentoOdontologicoDAO = MantenimientoOdontologiaFabricaDao.crearDescuentoOdontologicoDAO();
		return descuentoOdontologicoDAO.validarExistenciaDescuentosOdontologicosXAtencion(centroAtencion);
	}

	@Override
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXPresupuesto(
			int centroAtencion, Date fechaVigencia)
	{
		IDescuentoOdontologicoDAO descuentoOdontologicoDAO = MantenimientoOdontologiaFabricaDao.crearDescuentoOdontologicoDAO();
		return descuentoOdontologicoDAO.validarExistenciaDescuentosOdontologicosXPresupuesto(centroAtencion, fechaVigencia);
	}

}
