package com.servinte.axioma.servicio.fabrica.odontologia.cuotaOdontologica;

import com.servinte.axioma.servicio.impl.odontologia.cuotaOdontologica.CuotaOdontEspecialidadServicio;
import com.servinte.axioma.servicio.impl.odontologia.cuotaOdontologica.DetalleCuotaOdonEspServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.cuotaOndotologica.ICuotaOdonEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.cuotaOndotologica.IDetalleCuotaOdonEspeServicio;


/**
 * 
 * FABRICA PARA CREAR  COUTAS ODONTOLOGICAS
 * 
 * @author Edgar Carvajal 
 *
 */
public abstract class CuotaOdontologicaServicioFabrica 
{

	
	/**
	 * Construtor private para no genera instancia
	 */
	private CuotaOdontologicaServicioFabrica(){
		
	}
	
	
	/**
	 * CREAR INSTANCIA CUOTA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final  ICuotaOdonEspecialidadServicio crearCuotaOdonEspecialidad()
	{
		return new CuotaOdontEspecialidadServicio();
	} 

	
	
	/**
	 * CREAR INSTANCIA DETALLE CUOTAS
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IDetalleCuotaOdonEspeServicio crearDetalleOdonEspecialidad(){
		return new DetalleCuotaOdonEspServicio();
	}
	
	
}
