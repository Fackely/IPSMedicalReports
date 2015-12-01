package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTONivelAutorServMedic;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.NivelAutorAgrServHome;
import com.servinte.axioma.orm.NivelAutorServMedic;

public class NivelAutorServMedicDelegate extends
		NivelAutorAgrServHome{
	
	/**
	 * 
	 * Este Método se encarga de consultar el nivel de autorización por un servicio especifico
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorServicioEspecifico(int codigoNivelAutorizacion,int codigoServicio){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorServMedic.class,"nivelAutorServMedic");
		
		criteria.createAlias("nivelAutorServMedic.nivelAutorServicios", "nivelAutorServicio")
				.createAlias("nivelAutorServicio.servicios", "servicio")
		 		.createAlias("nivelAutorServMedic.nivelAutorizacion", "nivelAutorizacion");
		
		
		criteria.add(Restrictions.eq("nivelAutorizacion.codigoPk", codigoNivelAutorizacion));
		criteria.add(Restrictions.eq("servicio.codigo", codigoServicio));
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("nivelAutorServMedic.codigoPk"),"codigoPk")
			.add(Projections.property("nivelAutorizacion.codigoPk"),"nivelAutorizacion")
		);
		
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorServMedic.class));
		
		ArrayList<DTONivelAutorServMedic> lista = 
			(ArrayList<DTONivelAutorServMedic>)criteria.list();
				
		return lista;
	}

	
	/**
	 * 
	 * Este Método se encarga de consultar el nivel de autorización por agrupación de servicios
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionServicios(DtoServicios servicio, int codigoNivelAutorizacion){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorServMedic.class,"nivelAutorServMedic");
		
		criteria.createAlias("nivelAutorServMedic.nivelAutorAgrServs", "nivelAutorAgrServ")
				.createAlias("nivelAutorServMedic.nivelAutorizacion", "nivelAutorizacion")
				.createAlias("nivelAutorAgrServs.especialidades", "especialidad", Criteria.LEFT_JOIN)
				.createAlias("nivelAutorAgrServs.tiposServicio", "tipoServicio", Criteria.LEFT_JOIN)
				.createAlias("nivelAutorAgrServs.gruposServicios", "grupoServicio", Criteria.LEFT_JOIN);
		
		
		criteria.add(Restrictions.eq("nivelAutorizacion.codigoPk", codigoNivelAutorizacion));
		
		Disjunction disjunction = Restrictions.disjunction();
		
		/*//POR ESPECIALIDAD Se quitan campos por tarea 33245 xplanner 2010
		disjunction.add(Restrictions.eq("especialidad.codigo",servicio.getCodigoEspecialidad()));
		
		//POR TIPO SERVICIO
		disjunction.add(Restrictions.eq("tipoServicio.acronimo",servicio.getAcronimoTipoServicio()));
		
		//POR GRUPO SERVICIO
		disjunction.add(Restrictions.eq("grupoServicio.codigo",servicio.getCodigoGrupoServicio()));*/
		
		criteria.add(disjunction);
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
			.add(Projections.property("nivelAutorServMedic.codigoPk"),"codigoPk")
			.add(Projections.property("nivelAutorizacion.codigoPk"),"nivelAutorizacion")
			.add(Projections.property("especialidad.codigo"),"codigoEspecialidadServicio")
			.add(Projections.property("tipoServicio.acronimo"),"acronimoTipoServicio")
			.add(Projections.property("grupoServicio.codigo"),"codigoGrupoServicio"))
		);
		
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorServMedic.class));
		
		ArrayList<DTONivelAutorServMedic> lista = 
			(ArrayList<DTONivelAutorServMedic>)criteria.list();
				
		return lista;
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el nivel de autorización por un articulo especifico
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorArticuloEspecifico(int codigoNivelAutorizacion,int codigoArticulo){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorServMedic.class,"nivelAutorServMedic");
		
		criteria.createAlias("nivelAutorServMedic.nivelAutorArticulos", "nivelAutorArticulo")
				.createAlias("nivelAutorServMedic.nivelAutorizacion", "nivelAutorizacion");
		
		criteria.add(Restrictions.eq("nivelAutorizacion.codigoPk", codigoNivelAutorizacion));
		criteria.add(Restrictions.eq("nivelAutorArticulo.articulo", codigoArticulo));
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("nivelAutorServMedic.codigoPk"),"codigoPk")
			.add(Projections.property("nivelAutorizacion.codigoPk"),"nivelAutorizacion")
		);
		
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorServMedic.class));
		
		ArrayList<DTONivelAutorServMedic> lista = 
			(ArrayList<DTONivelAutorServMedic>)criteria.list();
				
		return lista;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar el nivel de autorización por agrupacion de articulos
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionMedicamentos(Articulo articulo, int codigoNivelAutorizacion,GrupoInventario grupoArticulo){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorServMedic.class,"nivelAutorServMedic");
		
		criteria.createAlias("nivelAutorServMedic.nivelAutorAgrArts", "nivelAutorAgrArt")
				.createAlias("nivelAutorServMedic.nivelAutorizacion", "nivelAutorizacion")
				.createAlias("nivelAutorAgrArt.grupoInventario", "grupoInventario", Criteria.LEFT_JOIN)
				.createAlias("nivelAutorAgrArt.claseInventario", "claseInventario", Criteria.LEFT_JOIN)
				.createAlias("nivelAutorAgrArt.naturalezaArticulo", "naturalezaArticulo", Criteria.LEFT_JOIN)
				;
		
		criteria.add(Restrictions.eq("nivelAutorizacion.codigoPk", codigoNivelAutorizacion));
		Disjunction disjunction = Restrictions.disjunction();
		
		/*//POR SUBGRUPO Se quitan campos por tarea 33245 xplanner 2010
		disjunction.add(Restrictions.eq("nivelAutorAgrArt.subgrupoInventario",articulo.getSubgrupo()));
		
		//POR GRUPO
		Conjunction conjunctionGrupoInventario = Restrictions.conjunction();
		conjunctionGrupoInventario.add(Restrictions.eq("grupoInventario.id.codigo", grupoArticulo.getId().getCodigo()));
		conjunctionGrupoInventario.add(Restrictions.eq("grupoInventario.id.clase", grupoArticulo.getId().getClase()));
		disjunction.add(conjunctionGrupoInventario);
		
		//POR CLASE
		disjunction.add(Restrictions.eq("claseInventario.codigo",grupoArticulo.getId().getClase()));
		
		//POR NATURALEZA
		Conjunction conjunctionNaturaleza = Restrictions.conjunction();
		conjunctionNaturaleza.add(Restrictions.eq("naturalezaArticulo.id.acronimo", articulo.getNaturalezaArticulo().getId().getAcronimo()));
		conjunctionNaturaleza.add(Restrictions.eq("naturalezaArticulo.id.institucion", articulo.getNaturalezaArticulo().getId().getInstitucion()));
		disjunction.add(conjunctionNaturaleza);*/
		
		criteria.add(disjunction);
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
			.add(Projections.property("nivelAutorServMedic.codigoPk"),"codigoPk")
			.add(Projections.property("nivelAutorizacion.codigoPk"),"nivelAutorizacion")
			.add(Projections.property("nivelAutorAgrArt.subgrupoInventario"),"subgrupoArticulo")
			.add(Projections.property("grupoInventario.id.codigo"),"codigoGrupoIdArticulo")
			.add(Projections.property("grupoInventario.id.clase"),"claseGrupoIdArticulo")
			.add(Projections.property("claseInventario.codigo"),"claseArticulo")
			.add(Projections.property("naturalezaArticulo.id.acronimo"),"acronimoNaturalezaId")
			.add(Projections.property("naturalezaArticulo.id.institucion"),"institucionNaturalezaId"))
		);
		
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorServMedic.class));
		
		ArrayList<DTONivelAutorServMedic> lista = 
			(ArrayList<DTONivelAutorServMedic>)criteria.list();
				
		return lista;
	}
	
	
	

}
