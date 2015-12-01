package com.servinte.axioma.mundo.fabrica.odontologia.cuotaOdontologica;



import com.servinte.axioma.mundo.impl.odontologia.coutaEspecialidad.CuotasOdontEspecialidadMundo;
import com.servinte.axioma.mundo.impl.odontologia.coutaEspecialidad.DetalleCuotaOdonEspMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad.ICuotasOdontEspecialidadMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad.IDetalleCuotaOdonEspMundo;


/**
 * FABRICA PARA CREAR INSTANCIAS DE CUOTA ODONTOLOGICA
 * @author Edgar Carvajal 
 *
 */
public abstract class CuotaOdontologicaFabrica 
{

	
	/**
	 * CREAR INSTANCIA CUOTAS ODONTOLOGICAS ESPECIALIDAD 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static ICuotasOdontEspecialidadMundo crearCuotasEspecialidad()
	{
		return new CuotasOdontEspecialidadMundo();
	}

	
	/**
	 * CREAR INSTANCIA DETALLES ODONTOLOGIA CUOTAS
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IDetalleCuotaOdonEspMundo crearDetalleCuotaDetalle()
	{
		return new DetalleCuotaOdonEspMundo();
	}
	
	
	
	

}
