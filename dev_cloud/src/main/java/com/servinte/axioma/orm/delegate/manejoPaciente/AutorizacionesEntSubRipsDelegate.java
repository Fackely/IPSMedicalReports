package com.servinte.axioma.orm.delegate.manejoPaciente;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionesEntSubRips;
import com.servinte.axioma.orm.AutorizacionesEntSubRips;
import com.servinte.axioma.orm.AutorizacionesEntSubRipsHome;

public class AutorizacionesEntSubRipsDelegate extends AutorizacionesEntSubRipsHome{

	/**
	 * Consulta si la autorización enviada como parámetro ya ha sido procesada
	 * con éxito por la funcionalidad Rips Entidades Subcontratadas
	 * @param consecutivoAutorizacionEntidadSub
	 * @return DtoAutorizacionesEntSubRips
	 * 
	 * @author Fabián Becerra
	 */
	public DtoAutorizacionesEntSubRips obtenerAutorizacionEntSubRipsPorEntSub(long consecutivoAutorizacionEntidadSub){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntSubRips.class, "autorizacionesEntSubRips");
		
		criteria.createAlias("autorizacionesEntSubRips.autorizacionesEntidadesSub"		, "autorizacionesEntidadesSub");
		criteria.createAlias("autorizacionesEntSubRips.usuarios"						, "usuario"				);
		
		
		// Filtros de Búsqueda -----------------------------------------------------------------------------------------------------------
		
		criteria.add(Restrictions.eq("autorizacionesEntidadesSub.consecutivo"	, consecutivoAutorizacionEntidadSub));
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("autorizacionesEntSubRips.fecha")			,"fechaProceso");
			projectionList.add(Projections.property("autorizacionesEntSubRips.hora")			,"horaProceso");
			projectionList.add(Projections.property("usuario.login")							,"loginUsuarioProceso");
			
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoAutorizacionesEntSubRips.class));
		
		DtoAutorizacionesEntSubRips autorizacion= new DtoAutorizacionesEntSubRips();
		autorizacion = (DtoAutorizacionesEntSubRips) criteria.uniqueResult();
		
		return autorizacion;
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro autorizacion entidad subcontratada que fue procesada
	 * por la funcionalidad procesar rips entidades subcontratadas
	 * 
	 * @param autorEntSubRips Autorizacion de Entidad Subcontratada Procesada por Rips
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarAutorizacionEntSubRips(AutorizacionesEntSubRips autorEntSubRips){
		boolean save = true;					
		try{
			super.attachDirty(autorEntSubRips);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar/actualizar el registro de " +
					"autorizaciones entidades sub rips: ",e);
		}				
		return save;				
	}
	
	
}
