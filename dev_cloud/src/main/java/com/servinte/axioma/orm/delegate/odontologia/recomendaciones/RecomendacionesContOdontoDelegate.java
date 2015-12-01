package com.servinte.axioma.orm.delegate.odontologia.recomendaciones;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.ibm.icu.math.BigDecimal;
import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.orm.RecomendacionesContOdontoHome;



/**
 * @author Edgar Carvajal 
 */
@SuppressWarnings({"unchecked","unused"})
public class RecomendacionesContOdontoDelegate extends RecomendacionesContOdontoHome
{
	
	
	/**
	 * METODO QUE CARGA LA LISTA DE RECOMENDACIONES 
	 * RECIBE UN DTO RECOMENDACIONES PARA REALIZAR LOS FILTROS 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 * @param institucion
	 * @return
	 */
	public List<RecomendacionesContOdonto> listaRecomendaciones(RecomendacionesContOdonto dtoRecomendaciones , int institucion )
	{
		
		
		Criteria criteriosList= sessionFactory.getCurrentSession().createCriteria(RecomendacionesContOdonto.class);
		
		/*
		 *FILTROS  
		 */
		
		if(dtoRecomendaciones.getCodigoPk()>0)
		{
			criteriosList.add(Restrictions.eq("codigoPk",dtoRecomendaciones.getCodigoPk()));
		}
		
		if(institucion>0)
		{
			criteriosList.add(Restrictions.eq("instituciones.codigo", institucion));
		}
		
		
		if(!UtilidadTexto.isEmpty(dtoRecomendaciones.getDescripcion()))
		{
			criteriosList.add(Restrictions.eq("descripcion", dtoRecomendaciones.getDescripcion()));
		}
		
		
		if(!UtilidadTexto.isEmpty(dtoRecomendaciones.getCodigo()))
		{
			criteriosList.add(Restrictions.eq("codigo",  dtoRecomendaciones.getCodigo()));
		}
		
		
		criteriosList.addOrder(Order.asc("descripcion"));
		criteriosList.list();
		
		
		return (List<RecomendacionesContOdonto> )criteriosList.list();
		
	}
	
	
	
	/**
	 * METODO QUE LISTA LAS RECOMENDACIONES FILTRADAS POR LA INSTITUCION
	 * TAMBIEN RECIBE UN ARRAY DE INT PARA SETTER EL ATRIBUTO YA EXISTE UTILIZADO EN LA BUSQUEDA  
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public List<DtoRecomendaciones> listarRecomendaciones(RecomendacionesContOdonto dtoRecomen, ArrayList<Integer> listaCodigos)
	{
		
		String listaCodigosL=UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(listaCodigos);
		
		String hql= " select " +
					" recomen.codigoPk as codigoPk , ";
					
		if(listaCodigos.size()>0)
		{
			hql+=" case when  recomen.codigoPk in ("+listaCodigosL+") then  true  else  false  end as yaExiste , ";
		}
					
		hql+="recomen.codigo as codigo  ," +
			" recomen.descripcion as descripcion "+ 
			" from RecomendacionesContOdonto  recomen  where instituciones.codigo="+dtoRecomen.getInstituciones().getCodigo()+
			" and  activo='"+ConstantesBD.acronimoSi+"' order by recomen.descripcion asc ";
		
		
		
		Session sess=  sessionFactory.getCurrentSession();
		
		Query query= sess.createQuery(hql);
		
		
		List<DtoRecomendaciones> lista= query.setResultTransformer(Transformers.aliasToBean(DtoRecomendaciones.class)).list();
		
		return lista;
	}
	
	
	
	/**
	 * METODO QUE CARGA LA LISTA DE RECOMENDACIONES 
	 * RECIBE UN DTO RECOMENDACIONES PARA REALIZAR LOS FILTROS 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 * @param institucion
	 * @return
	 */
	public List<RecomendacionesContOdonto> busquedaRecomendaciones(RecomendacionesContOdonto dtoRecomendaciones, boolean aplicaCodigoPK )
	{
		
		
		Criteria criteriosList= sessionFactory.getCurrentSession().createCriteria(RecomendacionesContOdonto.class);
		

		
		if (dtoRecomendaciones.getInstituciones()!=null &&  dtoRecomendaciones.getInstituciones().getCodigo()  >0)
		{
			criteriosList.add(Restrictions.eq("instituciones.codigo", dtoRecomendaciones.getInstituciones().getCodigo()));
		}
		
		
		if(!UtilidadTexto.isEmpty(dtoRecomendaciones.getDescripcion()))
		{
			criteriosList.add(Restrictions.eq("descripcion", dtoRecomendaciones.getDescripcion()));
		}
		
		
		if(!UtilidadTexto.isEmpty(dtoRecomendaciones.getCodigo()))
		{
			criteriosList.add(Restrictions.eq("codigo",  dtoRecomendaciones.getCodigo()));
		}
		
		
		if(aplicaCodigoPK)
		{
			criteriosList.add(Restrictions.ne("codigoPk", dtoRecomendaciones.getCodigoPk()));
		}
		
		
		criteriosList.addOrder(Order.asc("descripcion"));
		List<RecomendacionesContOdonto>  lisRecomen=criteriosList.list();
		
		
		if(lisRecomen!=null &&  lisRecomen.size()>0)
		{	
			for(RecomendacionesContOdonto recomen: lisRecomen )
			{
				if(recomen.getRecomendacionesServProgs()!=null)
				{
					Iterator iterator =  recomen.getRecomendacionesServProgs().iterator();
					
					while(iterator.hasNext())
					{
						Log4JManager.info("aca hay un while que no hace nada ? --> depronto es para forzar el get de algún elemento");
					}
				}
				
			}
		}
		
		return lisRecomen;
		
	}
	
	
	
	
	/**
	 * CARGA LA RECOMENACION POR ID, TAMBIEN CARGA EL SET DE AL RELACION SERVICIO PROGRAMA 
	 * @author Edgar Carvajal Ruiz
	 * @param recomendacion
	 * @return
	 */
	public RecomendacionesContOdonto buscarRecomenacionxId(RecomendacionesContOdonto recomendacion )
	{
		 RecomendacionesContOdonto recomen = findById(recomendacion.getCodigoPk());
		 
		 if(recomen.getRecomendacionesServProgs()!=null)
		 {	
			Iterator iterator = recomen.getRecomendacionesServProgs().iterator(); 
		 }
		
		return recomen;
	}
	
	
	
	
	/**
	 * Retorna las recomendaciones de un presupuesto odontologico
	 * 
	 * @param codPresoOdonto
	 * @return ArrayList<DtoRecomendaciones>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoRecomendaciones> obtenerRecomendacionesPresuOdonto(long codPresoOdonto){
		
		ProjectionList projectionList = Projections.projectionList();
		
		Log4JManager.info("------------------ presupuesto recomendaciones: "+codPresoOdonto);
		
		projectionList.add( Projections.property("recom_cotnra.codigo")	,"codigo")
		.add( Projections.property("recom_cotnra.descripcion")			,"descripcion")
		.add( Projections.property("programas.codigo")					,"pkPrograma")
		.add( Projections.property("programas.codigoPrograma")			,"codigoPrograma")
		.add( Projections.property("programas.nombre")					,"descripcionPrograma");
	
		
		return (ArrayList<DtoRecomendaciones>) sessionFactory.getCurrentSession()
			
			.createCriteria(RecomendacionesContOdonto.class, "recom_cotnra")
			
				.createAlias("recom_cotnra.recomendacionesServProgs"			, "recomendacionesServProgs", Criteria.LEFT_JOIN)
				.createAlias("recomendacionesServProgs.recomSerproSerpros"		, "recomSerproSerpros"		, Criteria.LEFT_JOIN)
				.createAlias("recomSerproSerpros.programas"						, "programas"				, Criteria.LEFT_JOIN)
				.createAlias("programas.presupuestoOdoProgServs"				, "presupuestoOdoProgServs"	, Criteria.LEFT_JOIN)
				.createAlias("presupuestoOdoProgServs.presupuestoOdontologico"	, "presupuestoOdontologico"	, Criteria.LEFT_JOIN)
				
				.add(Restrictions.eq("presupuestoOdontologico.codigoPk"			, codPresoOdonto))
				
				.setProjection(projectionList)
				
				.setResultTransformer(Transformers.aliasToBean(DtoRecomendaciones.class))
			
			.list();
	}
	

}
