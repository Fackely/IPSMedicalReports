package com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica;

import java.util.List;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;


/**
 * 
 * @author axioma
 *
 */
public interface IDetalleCuotasOdontoEspsDAO extends IBaseDAO<DetalleCuotasOdontoEsp> 
{
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param detalle
	 * @return
	 */
	public List<DetalleCuotasOdontoEsp> consultaAvanzadaDetalleCuota(DetalleCuotasOdontoEsp detalle);
	
	
	
	

	/**
	 * ELIMINAR TODOS LOS DETALLES POR CUOTA ODONTOLOGICA
	 * @author Edgar Carvajal Ruiz
	 */
	public void eliminarDetallesxCuota( CuotasOdontEspecialidad dtoCuota);
		

}
