package com.servinte.axioma.mundo.impl.odontologia.coutaEspecialidad;

import java.util.ArrayList;
import java.util.Iterator;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.servinte.axioma.dao.fabrica.odontologia.cuotaodontologica.CuotaOdontologicaDAOFabrica;
import com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica.ICuotasOdontEspecialidadDao;
import com.servinte.axioma.mundo.fabrica.odontologia.cuotaOdontologica.CuotaOdontologicaFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad.ICuotasOdontEspecialidadMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.cuotaEspecialidad.IDetalleCuotaOdonEspMundo;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class CuotasOdontEspecialidadMundo implements ICuotasOdontEspecialidadMundo {
	
	
	
	/**
	 * DAO 
	 */
	private ICuotasOdontEspecialidadDao cuotaOdontoDAO;
	private  IDetalleCuotaOdonEspMundo detalleCuotaMundo;
	
	
	
	/**
	 * CONSTRUTOR 
	 */
	public  CuotasOdontEspecialidadMundo()
	{
		cuotaOdontoDAO = CuotaOdontologicaDAOFabrica.crearCuotaOdontEspecialidad();
		detalleCuotaMundo=CuotaOdontologicaFabrica.crearDetalleCuotaDetalle();
	}


	@Override
	public CuotasOdontEspecialidad buscaxId(int id) 
	{
		UtilidadTransaccion.getTransaccion().begin();	
		CuotasOdontEspecialidad dto=  cuotaOdontoDAO.buscarxId(id);
		
		if(dto!=null)
		{
			dto.getEspecialidades().getNombre();
			dto.getUsuarios();
			dto.getInstituciones().getRazonSocial();
			if( dto.getDetalleCuotasOdontoEsps().size()>0)
			{
				Iterator it = dto.getDetalleCuotasOdontoEsps().iterator();
				while(it.hasNext())
				{	
					DetalleCuotasOdontoEsp detalle=(DetalleCuotasOdontoEsp)  it.next();
					detalle.getPorcentaje();
				}
			}
		}
		
		
		UtilidadTransaccion.getTransaccion().commit();
		
		
		return dto;
	}

	

	@Override
	public CuotasOdontEspecialidad consultarAvanzadaCuotaEspecialidad(CuotasOdontEspecialidad dto) 
	{
		
		CuotasOdontEspecialidad cuota = null;
		try
		{
			UtilidadTransaccion.getTransaccion().begin();
			cuota= cuotaOdontoDAO.consultarAvanzadaCuotaEspecialidad(dto);
			
			if(cuota!=null)
			{
				cuota.getEspecialidades().getNombre();
				cuota.getUsuarios();
				cuota.getInstituciones().getRazonSocial();
			}
			
			UtilidadTransaccion.getTransaccion().commit();  
		}
		catch (Exception e) 
		{
			Log4JManager.info("Error  "+e.getMessage());
			Log4JManager.error("->"+e);
		}
		
		return cuota;
		
	}

	
	

	@Override
	public void eliminar(CuotasOdontEspecialidad dtoCuota) 
	{
		try{
		
		UtilidadTransaccion.getTransaccion().begin();
		cuotaOdontoDAO.eliminar(dtoCuota);
		UtilidadTransaccion.getTransaccion().commit() ;
		
		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		
	}

	
	

	@Override
	public void insertar(CuotasOdontEspecialidad dtoCuota) 
	{
		try{
			
		UtilidadTransaccion.getTransaccion().begin();
		cuotaOdontoDAO.insertar(dtoCuota);
		UtilidadTransaccion.getTransaccion().commit() ;
		
		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
	}

	
	

	@Override
	public void modificar(CuotasOdontEspecialidad dtoCouta) 
	{
		
		try{
		UtilidadTransaccion.getTransaccion().begin();
		cuotaOdontoDAO.modificar(dtoCouta);
		UtilidadTransaccion.getTransaccion().commit() ;
		}
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
	}


	@Override
	public void insertar(CuotasOdontEspecialidad dtoCuota, ArrayList<DtoDetalleCuotasOdontoEsp> dtoDetalle) 
	{
	
		try{
			
			UtilidadTransaccion.getTransaccion().begin();
			cuotaOdontoDAO.insertar(dtoCuota);
			detalleCuotaMundo.guardarModificaEliminar(dtoDetalle, dtoCuota);
			
			dtoCuota.getDetalleCuotasOdontoEsps();
			
			UtilidadTransaccion.getTransaccion().commit() ;
			
			}
			catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
			}
		
		
	}

	
	

}
