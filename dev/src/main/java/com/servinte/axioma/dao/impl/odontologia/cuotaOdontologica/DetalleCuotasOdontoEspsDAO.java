package com.servinte.axioma.dao.impl.odontologia.cuotaOdontologica;

import java.util.List;

import com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica.IDetalleCuotasOdontoEspsDAO;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;
import com.servinte.axioma.orm.delegate.odontologia.cuotaOdontologica.DetalleCuotasOdontoEspsDelegate;



/**
 * 
 * @author axioma
 *
 */
public class DetalleCuotasOdontoEspsDAO implements IDetalleCuotasOdontoEspsDAO {

	
	DetalleCuotasOdontoEspsDelegate delegateI;
	
	/**
	 * CONSTRUTOR 
	 */
	public DetalleCuotasOdontoEspsDAO()
	{
		delegateI = new DetalleCuotasOdontoEspsDelegate();	
	}
	
	
	@Override
	public List<DetalleCuotasOdontoEsp> consultaAvanzadaDetalleCuota(DetalleCuotasOdontoEsp detalle) 
	{
		return delegateI.consultaAvanzadaDetalleCuota(detalle);
	}

	
	@Override
	public DetalleCuotasOdontoEsp buscarxId(Number id) 
	{
		return delegateI.findById(id.intValue());
	}

	@Override
	public void eliminar(DetalleCuotasOdontoEsp objeto) 
	{
		delegateI.delete(objeto);
	}

	
	@Override
	public void insertar(DetalleCuotasOdontoEsp objeto) 
	{
		delegateI.persist(objeto);
	}

	
	@Override
	public void modificar(DetalleCuotasOdontoEsp objeto) 
	{
		delegateI.attachDirty(objeto);
	}


	@Override
	public void eliminarDetallesxCuota(CuotasOdontEspecialidad dtoCuota) 
	{
		delegateI.eliminarDetallesxCuota(dtoCuota);
	}

}
