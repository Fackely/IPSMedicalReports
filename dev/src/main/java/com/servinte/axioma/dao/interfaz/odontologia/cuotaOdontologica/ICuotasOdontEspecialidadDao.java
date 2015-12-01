package com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;



/**
 * 
 * @author Edgar Carvajal 
 *
 */
public interface ICuotasOdontEspecialidadDao extends IBaseDAO<CuotasOdontEspecialidad>
{
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO QUE BUSQUEDA POR ESPECIALIDA E INSTITUCION 
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public  CuotasOdontEspecialidad consultarAvanzadaCuotaEspecialidad(CuotasOdontEspecialidad dto);
		

}
