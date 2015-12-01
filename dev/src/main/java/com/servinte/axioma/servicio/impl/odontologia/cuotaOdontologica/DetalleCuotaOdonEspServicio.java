package com.servinte.axioma.servicio.impl.odontologia.cuotaOdontologica;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.servinte.axioma.mundo.fabrica.odontologia.cuotaOdontologica.CuotaOdontologicaFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad.IDetalleCuotaOdonEspMundo;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;
import com.servinte.axioma.servicio.interfaz.odontologia.cuotaOndotologica.IDetalleCuotaOdonEspeServicio;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class DetalleCuotaOdonEspServicio implements IDetalleCuotaOdonEspeServicio {

	/**
	 * 
	 */
	private IDetalleCuotaOdonEspMundo detalleCuotaOdon;
	
	
	/**
	 * 
	 */
	public DetalleCuotaOdonEspServicio(){
		detalleCuotaOdon = CuotaOdontologicaFabrica.crearDetalleCuotaDetalle();
	}
	
	
	@Override
	public DetalleCuotasOdontoEsp buscarxId(Number id) 
	{
		return detalleCuotaOdon.buscarxId(id.intValue());
	}

	
	
	@Override
	public List<DetalleCuotasOdontoEsp> consultaAvanzadaDetalleCuota(DetalleCuotasOdontoEsp detalle) 
	{
		
		return detalleCuotaOdon.consultaAvanzadaDetalleCuota(detalle);
	}

	
	
	@Override
	public void eliminar(DetalleCuotasOdontoEsp objeto) 
	{
		detalleCuotaOdon.eliminar(objeto);
	}

	
	
	@Override
	public void guardarModificaEliminar(ArrayList<DtoDetalleCuotasOdontoEsp> dtoDetalle,CuotasOdontEspecialidad dtoCuota) 
	{
		
		
		detalleCuotaOdon.guardarModificaEliminar(dtoDetalle, dtoCuota);
		
	}

	
	
	@Override
	public void insertar(DetalleCuotasOdontoEsp objeto) 
	{
		detalleCuotaOdon.insertar(objeto);
	}

	
	
	
	@Override
	public void modificar(DetalleCuotasOdontoEsp objeto) 
	{
		detalleCuotaOdon.modificar(objeto);
	}


	
	@Override
	public void eliminarDetallesxCuota(CuotasOdontEspecialidad dtoCuota) 
	{
		detalleCuotaOdon.eliminarDetallesxCuota(dtoCuota);
	}


	
	

}
