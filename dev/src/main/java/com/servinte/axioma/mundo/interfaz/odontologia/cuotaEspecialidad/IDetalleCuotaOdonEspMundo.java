package com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;


/**
 * 
 * @author axioma
 *
 */
public interface IDetalleCuotaOdonEspMundo {
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param objeto
	 */
	public void insertar(DetalleCuotasOdontoEsp objeto);

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param objeto
	 */
	public void modificar(DetalleCuotasOdontoEsp objeto);
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param objeto
	 */
	public void eliminar(DetalleCuotasOdontoEsp objeto);

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param id
	 * @return
	 */
	public DetalleCuotasOdontoEsp buscarxId(Number id);
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param detalle
	 * @return
	 */
	public List<DetalleCuotasOdontoEsp> consultaAvanzadaDetalleCuota(DetalleCuotasOdontoEsp detalle);
	
	
	
	/**
	 * METODO QUE ADMINISTRAR LA LISTA DE DETALLE 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoDetalle
	 */
	public void guardarModificaEliminar(ArrayList<DtoDetalleCuotasOdontoEsp> dtoDetalle , CuotasOdontEspecialidad dtoCuota );
	
	
	/**
	 * ELIMINAR TODOS LOS DETALLES POR CUOTA ODONTOLOGICA
	 * @author Edgar Carvajal Ruiz
	 */
	public void eliminarDetallesxCuota( CuotasOdontEspecialidad dtoCuota);
	

}
