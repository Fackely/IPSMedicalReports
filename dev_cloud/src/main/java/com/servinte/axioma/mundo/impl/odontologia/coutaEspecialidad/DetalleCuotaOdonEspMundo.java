package com.servinte.axioma.mundo.impl.odontologia.coutaEspecialidad;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.servinte.axioma.dao.fabrica.odontologia.cuotaodontologica.CuotaOdontologicaDAOFabrica;
import com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica.IDetalleCuotasOdontoEspsDAO;

import com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad.IDetalleCuotaOdonEspMundo;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;
import com.servinte.axioma.persistencia.UtilidadTransaccion;



/**
 * 
 * @author axioma
 *
 */
public class DetalleCuotaOdonEspMundo  implements IDetalleCuotaOdonEspMundo {

	
	private IDetalleCuotasOdontoEspsDAO cuotasDAO;
	
	
	
	/**
	 * 
	 */
	public DetalleCuotaOdonEspMundo()
	{
		cuotasDAO=CuotaOdontologicaDAOFabrica.crearDetalleCuotaOdon();
	}	
	
	
	@Override
	public DetalleCuotasOdontoEsp buscarxId(Number id) 
	{
		return 	cuotasDAO.buscarxId(id);
		
	}

	
	@Override
	public List<DetalleCuotasOdontoEsp> consultaAvanzadaDetalleCuota(DetalleCuotasOdontoEsp detalle) 
	{
		return cuotasDAO.consultaAvanzadaDetalleCuota(detalle);
	}

	
	
	@Override
	public void eliminar(DetalleCuotasOdontoEsp objeto) 
	{
		cuotasDAO.eliminar(objeto);
	}

	@Override
	public void insertar(DetalleCuotasOdontoEsp objeto) 
	{
			
		cuotasDAO.insertar(objeto);
		
	}
	
	

	@Override
	public void modificar(DetalleCuotasOdontoEsp objeto) 
	{
		
		cuotasDAO.modificar(objeto);
		
	}

	
	
	
	
	@Override
	public void guardarModificaEliminar( ArrayList<DtoDetalleCuotasOdontoEsp> listaDetalles , CuotasOdontEspecialidad dtoCuota ) 
	{
		
		
		
		
		for(DtoDetalleCuotasOdontoEsp dtoDetalle :  listaDetalles )
		{
			if(dtoDetalle.getActivo() && dtoDetalle.getCodigoPk()>0)
			{
				DetalleCuotasOdontoEsp entidad = new DetalleCuotasOdontoEsp();
				
				entidad.setCodigoPk(dtoDetalle.getCodigoPk());
				entidad.setCuotasOdontEspecialidad(dtoCuota);
				entidad.setNroCuotas(dtoDetalle.getNroCuotas());
				entidad.setPorcentaje(dtoDetalle.getPorcentaje());
				entidad.setValor(dtoDetalle.getValor());
				entidad.setTipoCuota(dtoDetalle.getTipoCuota());
				
				modificar(entidad); 
			}
			
			if(!dtoDetalle.getActivo() && dtoDetalle.getCodigoPk()>0 )
			{
				
				DetalleCuotasOdontoEsp entidad = new DetalleCuotasOdontoEsp();
				
				entidad.setCodigoPk(dtoDetalle.getCodigoPk());
				entidad.setCuotasOdontEspecialidad(dtoCuota);
				entidad.setNroCuotas(dtoDetalle.getNroCuotas());
				entidad.setPorcentaje(dtoDetalle.getPorcentaje());
				entidad.setValor(dtoDetalle.getValor());
				entidad.setTipoCuota(dtoDetalle.getTipoCuota());
				
				eliminar(entidad);
			}
			
			if(dtoDetalle.getActivo() && dtoDetalle.getCodigoPk()<0)
			{

				DetalleCuotasOdontoEsp entidad = new DetalleCuotasOdontoEsp();
				
				entidad.setCuotasOdontEspecialidad(dtoCuota);
				entidad.setNroCuotas(dtoDetalle.getNroCuotas());
				entidad.setPorcentaje(dtoDetalle.getPorcentaje());
				entidad.setValor(dtoDetalle.getValor());
				entidad.setTipoCuota(dtoDetalle.getTipoCuota());
				
				insertar(entidad);
				
			}
			
			
		}
		
		
		
		
		
	
		
	}


	

	
	@Override
	public void eliminarDetallesxCuota(CuotasOdontEspecialidad dtoCuota) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		cuotasDAO.eliminarDetallesxCuota(dtoCuota);
		UtilidadTransaccion.getTransaccion().commit();
		
	}
	


}
