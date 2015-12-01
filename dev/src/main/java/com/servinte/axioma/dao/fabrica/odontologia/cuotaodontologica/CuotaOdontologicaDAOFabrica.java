package com.servinte.axioma.dao.fabrica.odontologia.cuotaodontologica;

import com.servinte.axioma.dao.impl.odontologia.cuotaOdontologica.CuotasOdonEspecialidadDAO;
import com.servinte.axioma.dao.impl.odontologia.cuotaOdontologica.DetalleCuotasOdontoEspsDAO;
import com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica.ICuotasOdontEspecialidadDao;
import com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica.IDetalleCuotasOdontoEspsDAO;


/**
 * 
 * FABRICA PARA CREAR INSTANCIAS DE CUOTAS ODONTOLOGICAS
 * @author Edgar Carvajal 
 *
 */
public abstract class CuotaOdontologicaDAOFabrica 
{
	
	/**
	 * METODO PARA CREAR INSTANCIAS DE CUOTAS ODONTOLOGICAS
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final ICuotasOdontEspecialidadDao crearCuotaOdontEspecialidad()
	{
		return new CuotasOdonEspecialidadDAO();
	}
	
	/**
	 * METODO  PARA CREAR INSTANCIAS DE DETALLES CUOTAS ODONTOLOGICAS
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IDetalleCuotasOdontoEspsDAO crearDetalleCuotaOdon()
	{
		return new DetalleCuotasOdontoEspsDAO();
	}
	
	 
	
	

}
