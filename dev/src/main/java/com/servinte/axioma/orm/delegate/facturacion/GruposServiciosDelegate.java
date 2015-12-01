package com.servinte.axioma.orm.delegate.facturacion;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.GruposServiciosHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 8/09/2010
 */
public class GruposServiciosDelegate extends GruposServiciosHome 
{
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<GruposServicios> buscarGruposServicioActivos(){
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(GruposServicios.class)
			.add(Restrictions.eq("activo", true));
		 
		 return (ArrayList<GruposServicios>)criteria.list();
	}
	
	
	
	/**
	 * Este M�todo se encarga de consultar los grupos de servicios.
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Cristhian Murillo
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<GruposServicios> buscarGruposServicio()
	{
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(GruposServicios.class);
		 return (ArrayList<GruposServicios>)criteria.list();
	}
	
	
	/**
	 * Este M�todo se encarga de obtener los distintos grupos de servicios
	 * que tienen asociado un servicio el cual se encuentra en el cierre de presupuesto
	 * para un nivel de atenci�n para un convenio.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<GruposServicios>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
										long consecutivoNivel, String proceso, List<Calendar> meses){
		Calendar fInicio=Calendar.getInstance();
		Calendar fFin=Calendar.getInstance();
		Date fechaInicio;
		Date fechaFin;
		fInicio=(Calendar)meses.get(0).clone();
		fInicio.set(Calendar.DAY_OF_MONTH, fInicio.getActualMinimum(Calendar.DAY_OF_MONTH));
		if(meses.size() > 1){
			fFin=(Calendar)meses.get(meses.size()-1).clone();
			fFin.set(Calendar.DAY_OF_MONTH, fFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		else{
			fFin=(Calendar)meses.get(0).clone();
			fFin.set(Calendar.DAY_OF_MONTH, fFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		fechaInicio=fInicio.getTime();
		fechaFin=fFin.getTime();
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(GruposServicios.class, "gruposServicios");
		 	criteria.createAlias("gruposServicios.servicioses", "servicios")
		 			.createAlias("servicios.cierreNivelAteGruServs", "cierreServicios")
		 			.createAlias("servicios.nivelAtencion", "nivelAtencion")
		 			.createAlias("cierreServicios.contratos", "contratos")
		 			.createAlias("contratos.convenios", "convenio");
		 	criteria.add(Restrictions.eq("convenio.codigo", codigoConvenio))
		 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
		 			.add(Restrictions.eq("cierreServicios.tipoProceso", proceso))
		 			.add(Restrictions.between("cierreServicios.fechaCierre", fechaInicio, fechaFin))
					.addOrder(Order.asc("gruposServicios.descripcion"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 return (ArrayList<GruposServicios>)criteria.list();
	}
	
	
	/**
	 * Este M�todo se encarga de obtener los distintos grupos de servicios
	 * que tienen asociado un servicio el cual se encuentra en el cierre de presupuesto
	 * para un nivel de atenci�n para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<GruposServicios>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorContratoPorProceso(int codigoContrato, 
										long consecutivoNivel, String proceso, List<Calendar> meses){
		Calendar mesInicio=Calendar.getInstance();
		Calendar mesFin=Calendar.getInstance();
		Date fechaInicio;
		Date fechaFin;
		mesInicio=(Calendar)meses.get(0).clone();
		mesInicio.set(Calendar.DAY_OF_MONTH, mesInicio.getActualMinimum(Calendar.DAY_OF_MONTH));
		if(meses.size() > 1){
			mesFin=(Calendar)meses.get(meses.size()-1).clone();
			mesFin.set(Calendar.DAY_OF_MONTH, mesFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		else{
			mesFin=(Calendar)meses.get(0).clone();
			mesFin.set(Calendar.DAY_OF_MONTH, mesFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		fechaInicio=mesInicio.getTime();
		fechaFin=mesFin.getTime();
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(GruposServicios.class, "gruposServicios");
		 	criteria.createAlias("gruposServicios.servicioses", "servicios")
		 			.createAlias("servicios.cierreNivelAteGruServs", "cierreServicios")
		 			.createAlias("servicios.nivelAtencion", "nivelAtencion")
		 			.createAlias("cierreServicios.contratos", "contrato");
		 	criteria.add(Restrictions.eq("contrato.codigo", codigoContrato))
		 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
		 			.add(Restrictions.eq("cierreServicios.tipoProceso", proceso))
		 			.add(Restrictions.between("cierreServicios.fechaCierre", fechaInicio, fechaFin))
					.addOrder(Order.asc("gruposServicios.descripcion"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 return (ArrayList<GruposServicios>)criteria.list();
	}
	
	/**
	 * Este M�todo se encarga de obtener el grupo de servicio 
	 * al cual est� asociado el servicio de la Orden. 
	 *  
	 * @author, Camilo G�mez
	 * 
	 * @param codServicio
	 * @return GruposServicios
	 */	
	public GruposServicios buscarGrupoServicioPorServicio(int codServicio)
	{
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(GruposServicios.class, "gruposServicios");
		 criteria.createAlias("gruposServicios.servicioses", "servicios");
		 criteria.add(Restrictions.eq("servicios.codigo", codServicio))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 return (GruposServicios)criteria.uniqueResult();
	}
	
	/**
	 * Este Metodo consulta los grupos de servicio
	 * @author ginsotfu
	 * 
	 * @param 
	 * @return List<GrupoServicioDto>
	 */	
	@SuppressWarnings("unchecked")
	public List<GrupoServicioDto> consultarGruposServicio() throws BDException{
		List<GrupoServicioDto> listaGruposServicio=new ArrayList<GrupoServicioDto>(0);
		try{
			persistenciaSvc= new PersistenciaSvc();
			listaGruposServicio=(List<GrupoServicioDto>)persistenciaSvc.createNamedQuery("catalogoFacturacion.consultarGruposServicio");
			return listaGruposServicio;
			
			
			}
			catch (Exception e){
				Log4JManager.error(e);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
			}		
	}

}
