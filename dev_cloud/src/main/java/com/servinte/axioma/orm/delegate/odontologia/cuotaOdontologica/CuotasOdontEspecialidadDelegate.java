package com.servinte.axioma.orm.delegate.odontologia.cuotaOdontologica;

import java.util.Iterator;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.CuotasOdontEspecialidadHome;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public class CuotasOdontEspecialidadDelegate extends CuotasOdontEspecialidadHome {
	
	
	
	
	
	/**
	 * METODO QUE  RETORNA UN OBJETO CUOTAS ONDONTOLOGICAS DEPENDIENDO DE LA INFORMACION QUE CONTENGA EL DTO  
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public  CuotasOdontEspecialidad consultarAvanzadaCuotaEspecialidad(  CuotasOdontEspecialidad dto){
		
		Criteria criterio = null;
		
		CuotasOdontEspecialidad cuotasOdonto =accionCargaCoutaEspecialidad(criterio , dto);
		
	
		
		/*
		 * ITERACION PARA CARGAR EL DETALLE
		 */
		if(cuotasOdonto!=null && cuotasOdonto.getDetalleCuotasOdontoEsps()!=null)
		{
			Iterator it = cuotasOdonto.getDetalleCuotasOdontoEsps().iterator();
			
			while(it.hasNext())
			{
				DetalleCuotasOdontoEsp dtoDetalle = (DetalleCuotasOdontoEsp)it.next();
			}
			
		}
		
	
		return  cuotasOdonto;
	}

	
	
	
	
	
	
	
	
	/**
	 * METODO EN DONDE SE HACER EL FILTRADO DE BUSQUEDAD
	 * @author Edgar Carvajal Ruiz
	 * @param criterio
	 * @param dto
	 */
	private CuotasOdontEspecialidad accionCargaCoutaEspecialidad( 	Criteria criterio , CuotasOdontEspecialidad dto) 
	{
		
		
		criterio = sessionFactory.getCurrentSession().createCriteria(CuotasOdontEspecialidad.class);
		

		
		
		if( dto.getEspecialidades()!=null && dto.getEspecialidades().getCodigo()>0)
		{
			criterio.add(Restrictions.eq("especialidades.codigo", dto.getEspecialidades().getCodigo() ) );
		}
		
		
		
		if(dto.getInstituciones()!=null && dto.getInstituciones().getCodigo()>0)
		{
			criterio.add(Restrictions.eq("instituciones.codigo", dto.getInstituciones().getCodigo()));
		}
		
		
		
		if(dto.getCodigoPk()>0)
		{
			criterio.add(Restrictions.eq("codigoPk", dto.getCodigoPk()));
		}
		
		
		
		CuotasOdontEspecialidad  cuota= new CuotasOdontEspecialidad() ;
		
		
		try
		{
			cuota= (CuotasOdontEspecialidad) criterio.uniqueResult();
		}
		catch(Exception e)
		{
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return  cuota;
	}
	

}
