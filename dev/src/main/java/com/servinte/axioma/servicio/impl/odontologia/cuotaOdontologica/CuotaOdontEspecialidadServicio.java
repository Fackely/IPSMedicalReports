package com.servinte.axioma.servicio.impl.odontologia.cuotaOdontologica;


import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.servinte.axioma.mundo.fabrica.odontologia.cuotaOdontologica.CuotaOdontologicaFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad.ICuotasOdontEspecialidadMundo;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.servicio.interfaz.odontologia.cuotaOndotologica.ICuotaOdonEspecialidadServicio;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class CuotaOdontEspecialidadServicio implements ICuotaOdonEspecialidadServicio {

	
	
	
	private ICuotasOdontEspecialidadMundo cuotaEspecialidadMundo;
	
	
	/**
	 * 
	 */
	public  CuotaOdontEspecialidadServicio()
	{
		cuotaEspecialidadMundo = CuotaOdontologicaFabrica.crearCuotasEspecialidad();
	}
	
	
	
	@Override
	public CuotasOdontEspecialidad buscaxId(int id) 
	{
		return cuotaEspecialidadMundo.buscaxId(id);
	}

	@Override
	public CuotasOdontEspecialidad consultarAvanzadaCuotaEspecialidad(CuotasOdontEspecialidad dto) 
	{
		return cuotaEspecialidadMundo.consultarAvanzadaCuotaEspecialidad(dto);
	}

	@Override
	public void eliminar(CuotasOdontEspecialidad dtoCuota) {
		cuotaEspecialidadMundo.eliminar(dtoCuota);
		
	}

	@Override
	public void insertar(CuotasOdontEspecialidad dtoCuota) {
		cuotaEspecialidadMundo.insertar(dtoCuota);
		
	}

	@Override
	public void modificar(CuotasOdontEspecialidad dtoCouta) {
		cuotaEspecialidadMundo.modificar(dtoCouta);
	}



	@Override
	public void insertar(CuotasOdontEspecialidad dtoCuota,ArrayList<DtoDetalleCuotasOdontoEsp> dtoDetalle) {
		cuotaEspecialidadMundo.insertar(dtoCuota, dtoDetalle);
	}

}
