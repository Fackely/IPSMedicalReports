package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.EntidadesSubcontratadasHome;

/**
 * Esta clase se encarga de ejecutar los procesos
 * de negocio de la entidad EntidadesSubcontratadas
 *
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class EntidadesSubcontratadasDelegate extends EntidadesSubcontratadasHome 
{
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para el centro de costo
	 * de la funcionalidad CentroCosto por Unidades Subcontratadas.
	 * Si se le envia como centro de costo un codigo invalido se listaran todas las entidades subcontratadas activas
	 * 
	 * @param codCentroCosto
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Cristhian Murillo
	 * @modified jeilones
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo (int codCentroCosto)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntidadesSubcontratadas.class, "entidadesSubcontratadas");
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.activo"				, ConstantesBD.acronimoSi));
		
		if(codCentroCosto > 0)
		{
			criteria.createAlias("entidadesSubcontratadas.centrosCostoEntidadesSubs"	, "centrosCostoEntidadesSubs", Criteria.LEFT_JOIN);
			criteria.createAlias("centrosCostoEntidadesSubs.centrosCosto"	, "centrosCosto");
			
			criteria.add(Restrictions.eq("centrosCosto.codigo"				, codCentroCosto));
			criteria.add(Restrictions.eq("centrosCosto.esActivo"			, true));
		}
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.distinct(Projections.projectionList()
				.add( Projections.groupProperty("entidadesSubcontratadas.codigoPk")						, "codigoPk")));
				projectionList.add(Projections.groupProperty("entidadesSubcontratadas.razonSocial")		, "razonSocial");
				projectionList.add(Projections.groupProperty("entidadesSubcontratadas.direccion")		, "direccion");
				projectionList.add(Projections.groupProperty("entidadesSubcontratadas.telefono")			, "telefono");
				
		if(codCentroCosto > 0)
		{
			projectionList.add(Projections.groupProperty("centrosCostoEntidadesSubs.nroPrioridad")	, "nroPrioridad");
		}		
				
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoEntidadSubcontratada.class));
		
		criteria.addOrder(Order.asc("entidadesSubcontratadas.razonSocial"));
		
		return (ArrayList<DtoEntidadSubcontratada>) criteria.list();
	}
	
	
	
	/**
	 * Implementación del método findById
	 * @param id
	 * @return EntidadesSubcontratadas
	 */
	public EntidadesSubcontratadas obtenerEntidadesSubcontratadasporId(long id) {
		return super.findById(id);
	}
	
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para el centro de costo que tengan contrato vigente
	 * de la funcionalidad CentroCosto por Unidades Subcontratadas.
	 * Si se le envia como centro de costo un codigo invalido se listaran todas las entidades subcontratadas activas con contrato vigente.
	 * 
	 * @param codCentroCosto
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivoContratoVigente (int codCentroCosto)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntidadesSubcontratadas.class, "entidadesSubcontratadas");
		
		criteria.createAlias("entidadesSubcontratadas.centrosCostoEntidadesSubs"	, "centrosCostoEntidadesSubs");
		criteria.createAlias("entidadesSubcontratadas.contratosEntidadesSubs"		, "contratosEntidadesSubs");
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.activo"				, ConstantesBD.acronimoSi));
		
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		criteria.add(Restrictions.gt("contratosEntidadesSubs.fechaFinal"	, fechaActual));
		criteria.add(Restrictions.lt("contratosEntidadesSubs.fechaInicial"	, fechaActual));
		
		if(codCentroCosto > 0)
		{
			criteria.createAlias("centrosCostoEntidadesSubs.centrosCosto"	, "centrosCosto");
			criteria.add(Restrictions.eq("centrosCosto.codigo"				, codCentroCosto));
			criteria.add(Restrictions.eq("centrosCosto.esActivo"			, true));
		}
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.distinct(Projections.projectionList().add( Projections.property("entidadesSubcontratadas.codigoPk")		, "codigoPk")));
			projectionList.add(Projections.property("entidadesSubcontratadas.razonSocial")									, "razonSocial");
			projectionList.add(Projections.property("entidadesSubcontratadas.direccion")									, "direccion");
			projectionList.add(Projections.property("entidadesSubcontratadas.telefono")										, "telefono");
			projectionList.add(Projections.property("centrosCostoEntidadesSubs.nroPrioridad")								, "nroPrioridad");
			projectionList.add(Projections.property("contratosEntidadesSubs.consecutivo")									, "contratoEntSub");
			projectionList.add(Projections.property("contratosEntidadesSubs.tipoTarifa")									, "tipotarifa");
			
			
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.codigoPk"));
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.razonSocial"));
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.direccion"));
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.telefono"));
			projectionList.add( Projections.groupProperty("centrosCostoEntidadesSubs.nroPrioridad"));
			projectionList.add( Projections.groupProperty("contratosEntidadesSubs.consecutivo"));
			projectionList.add( Projections.groupProperty("contratosEntidadesSubs.tipoTarifa"));
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoEntidadSubcontratada.class));
		
		criteria.addOrder(Order.asc("centrosCostoEntidadesSubs.nroPrioridad"));
		
		return (ArrayList<DtoEntidadSubcontratada>) criteria.list();
	}
	
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para la vía de ingreso dada (Según anexo 538)
	 * 
	 * @param parametros
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXViaIngreso (DtoEntidadSubcontratada parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntidadesSubcontratadas.class, "entidadesSubcontratadas");
		
		criteria.createAlias("entidadesSubcontratadas.estanciaViaIngCentroCostos"	, "estanciaViaIngCentroCostos");
		criteria.createAlias("estanciaViaIngCentroCostos.viasIngreso"				, "viasIngreso");
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.activo"		, ConstantesBD.acronimoSi));
		criteria.add(Restrictions.eq("viasIngreso.codigo"					, parametros.getViaIngreso()));
		
		if(parametros.isPermiteEstanciaPaciente()){
			criteria.createAlias("entidadesSubcontratadas.contratosEntidadesSubs", "contrato");			
			
			criteria.add(Restrictions.eq("entidadesSubcontratadas.permiteEstanciaPacientes", ConstantesBD.acronimoSi));
			criteria.add(Restrictions.le("contrato.fechaInicial",UtilidadFecha.getFechaActualTipoBD()));
			criteria.add(Restrictions.ge("contrato.fechaFinal",UtilidadFecha.getFechaActualTipoBD()));		
		}
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.distinct(Projections.projectionList().add( 
							   Projections.property("entidadesSubcontratadas.codigoPk")			, "codigoPk")));
			projectionList.add(Projections.property("entidadesSubcontratadas.razonSocial")		, "razonSocial");
			projectionList.add(Projections.property("entidadesSubcontratadas.direccion")		, "direccion");
			projectionList.add(Projections.property("entidadesSubcontratadas.telefono")			, "telefono");
			projectionList.add(Projections.property("viasIngreso.codigo")						, "viaIngreso");
			
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.codigoPk"));
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.razonSocial"));
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.direccion"));
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.telefono"));
			projectionList.add( Projections.groupProperty("viasIngreso.codigo"));
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoEntidadSubcontratada.class));
		
		return (ArrayList<DtoEntidadSubcontratada>) criteria.list();
	}
	
	/**
	 * Retorna las EntidadesSubcontratadas activas en el sistema
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Fabián Becerra
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubActivasEnSistema()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntidadesSubcontratadas.class, "entidadesSubcontratadas");
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.activo"				, ConstantesBD.acronimoSi));
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.distinct(Projections.projectionList().add( Projections.property("entidadesSubcontratadas.codigoPk")		, "codigoPk")));
			projectionList.add(Projections.property("entidadesSubcontratadas.razonSocial")									, "razonSocial");
			projectionList.add(Projections.property("entidadesSubcontratadas.direccion")									, "direccion");
			projectionList.add(Projections.property("entidadesSubcontratadas.telefono")										, "telefono");
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoEntidadSubcontratada.class));
		
		criteria.addOrder(Order.asc("entidadesSubcontratadas.razonSocial"));
		
		return (ArrayList<DtoEntidadSubcontratada>) criteria.list();
	}
	
	
	
	
	/**
	 * Retorna la EntidadesSubcontratadas por si llave primaria
	 * 
	 * @param codigoPk
	 * @return DtoEntidadSubcontratada
	 * 
	 * @author Cristhian Murillo
	 */
	public DtoEntidadSubcontratada listarEntidadesSubXId (Long codigoPk)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntidadesSubcontratadas.class, "entidadesSubcontratadas");
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.codigoPk"				, codigoPk));
		criteria.createAlias("entidadesSubcontratadas.centrosCostoEntidadesSubs"	, "centrosCostoEntidadesSubs");
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.distinct(Projections.projectionList()
				.add( Projections.property("entidadesSubcontratadas.codigoPk")						, "codigoPk")));
				projectionList.add(Projections.property("entidadesSubcontratadas.razonSocial")		, "razonSocial");
				projectionList.add(Projections.property("entidadesSubcontratadas.direccion")		, "direccion");
				projectionList.add(Projections.property("entidadesSubcontratadas.telefono")			, "telefono");
				projectionList.add(Projections.property("centrosCostoEntidadesSubs.nroPrioridad")	, "nroPrioridad");
				
				projectionList.add( Projections.groupProperty("entidadesSubcontratadas.codigoPk"));
				projectionList.add( Projections.groupProperty("entidadesSubcontratadas.razonSocial"));
				projectionList.add( Projections.groupProperty("entidadesSubcontratadas.direccion"));
				projectionList.add( Projections.groupProperty("entidadesSubcontratadas.telefono"));
				projectionList.add( Projections.groupProperty("centrosCostoEntidadesSubs.nroPrioridad"));
				
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoEntidadSubcontratada.class));
		
		ArrayList<DtoEntidadSubcontratada> listaEntidadesConPrioridad = new ArrayList<DtoEntidadSubcontratada>();
		listaEntidadesConPrioridad = (ArrayList<DtoEntidadSubcontratada>)criteria.list();
		
		return listaEntidadesConPrioridad.get(0);
	}
	
	
}
