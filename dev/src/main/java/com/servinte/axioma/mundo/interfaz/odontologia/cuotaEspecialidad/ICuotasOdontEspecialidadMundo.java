package com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;


/**
 * 
 *INTERFAZ DE COUTAS ESPECIALIDAD MUNDO
 * @author Edgar Carvajal 
 */
public interface ICuotasOdontEspecialidadMundo 
{
	
	
	/**
	 * METODO PARA INSERTAR LAS COUTAS ODONTOLOGICAS 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoCuota
	 */
	public void insertar(CuotasOdontEspecialidad dtoCuota );
	
	
	/**
	 * METODO PARA MODIFCAR LAS COUTAS ODONTOLOGICAS
	 * @author Edgar Carvajal Ruiz
	 * @param dtoCouta
	 */
	public void modificar(CuotasOdontEspecialidad dtoCouta);
	
	
	/**
	 * METODO PARA ELIMINAR COUTAS ODONTOLOGICAS
	 * @author Edgar Carvajal Ruiz
	 * @param dtoCuota
	 */
	public void eliminar(CuotasOdontEspecialidad dtoCuota);
	
	/**
	 * METODO PARA BUSCAR COUTAS ODONTOLOGICAS POR ESPECIALIDAD Y/O POR INSTITUCION
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public CuotasOdontEspecialidad consultarAvanzadaCuotaEspecialidad(CuotasOdontEspecialidad dto);
	
	
	
	/**
	 * METODO QUE BUSCAR UNA CUOTA ODONTOLOGICA POR ID 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public CuotasOdontEspecialidad buscaxId(int id);
	
	
	/**
	 * METODO PARA INSERTAR LAS COUTAS ODONTOLOGICAS + LOS  DETALLES 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoCuota
	 */
	public void insertar(CuotasOdontEspecialidad dtoCuota , ArrayList<DtoDetalleCuotasOdontoEsp> dtoDetalle );
	
	


	
	
	
	
}
