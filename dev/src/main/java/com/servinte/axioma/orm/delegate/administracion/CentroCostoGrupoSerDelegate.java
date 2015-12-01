package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.servinte.axioma.orm.CentroCostoGrupoSer;
import com.servinte.axioma.orm.CentroCostoGrupoSerHome;


/**
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class CentroCostoGrupoSerDelegate extends CentroCostoGrupoSerHome
{
	
	/**
	 * Lista todos los centros costo por grupos de servicio
	 * @return  ArrayList<CentroCostoGrupoSer>
	 * @autor Cristhian Murillo
	*/
	public ArrayList<CentroCostoGrupoSer> listarTodosCentrosCostoPorGrupoServicio()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroCostoGrupoSer.class, "centroCostoGrupoSer");
		ArrayList<CentroCostoGrupoSer> listaCentroCostoGrupoSer = (ArrayList<CentroCostoGrupoSer>) criteria.list();
		return listaCentroCostoGrupoSer;
	}
	
	
	
	/**
	 * Retorna los Centros de costo por el Grupo de Servicio enviado
	 * @param gruposervicio
	 * @return ArrayList<DtoCentroCosto>
	 *
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoCentroCosto> obtenerCentrosCostoPorGrupoServicio(Integer grupoServicio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CentroCostoGrupoSer.class, 	"centroCostoGrupoSer");
		
		criteria.createAlias("centroCostoGrupoSer.centrosCosto"					, "centrosCosto");
		criteria.createAlias("centroCostoGrupoSer.gruposServicios"				, "gruposServicios");
		
		if(grupoServicio != null){
			criteria.add(Restrictions.eq("gruposServicios.codigo"				, grupoServicio )) ;
		}
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("centrosCosto.codigo")				, "codigoCentroCosto")
				.add(Projections.property("centrosCosto.nombre")				, "nombre"))
		.setResultTransformer(Transformers.aliasToBean(DtoCentroCosto.class));
		
		ArrayList<DtoCentroCosto> listaResultado = (ArrayList<DtoCentroCosto>)criteria.list();
		
		return listaResultado;
	}
	
	
	
}
