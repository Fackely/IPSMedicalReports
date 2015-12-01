/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.inventario.DtoArticulos;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.ArticuloHome;

/**
 * @author Cristhian Murillo
 *
 */
public class ArticuloDelegate extends ArticuloHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return Articulo
	 */
	public Articulo obtenerArticuloPorId(int id) {
		Articulo articulo = super.findById(id);
		 
		return articulo;
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DtoArticulos consultarArticuloPorID(int id){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Articulo.class, "articulo");
		criteria.createAlias("articulo.naturalezaArticulo","naturaleza");
		criteria.createAlias("articulo.instituciones","institucion");
		criteria.createAlias("articulo.nivelAtencion","nivel",Criteria.LEFT_JOIN);
				
		criteria.add(Restrictions.eq("articulo.codigo",id));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("articulo.subgrupo"),"codigoSubGrupoArticulo");
		projectionList.add(Projections.property("naturaleza.id.acronimo"),"naturalezaArticulo");
		projectionList.add(Projections.property("naturaleza.nombre"),"nombreNaturaleza");
		projectionList.add(Projections.property("institucion.codigo"),"codigoInstitucion");
		projectionList.add(Projections.property("nivel.consecutivo"),"nivelAtencion");
		
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulos.class));
		DtoArticulos dto = (DtoArticulos)criteria.uniqueResult();
	
		return dto;
	
		
	}	
	
	/**
	 * 
	 * Este Método se encarga de obtener los codigos de los articulos por su tipo de codigo
	 * de medicamentos e insumos 
	 * @param tipoCodigoMed tipo de codigo Axioma o Interfaz 
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> consultarCodigosArticulosPorTipoCodigo(String tipoCodigoMed){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Articulo.class, "articulo");
		criteria.createAlias("articulo.instituciones","institucion");
		
		ProjectionList projectionList = Projections.projectionList();
		if(tipoCodigoMed.equals(ConstantesIntegridadDominio.acronimoAxioma)){
			projectionList.add(Projections.property("articulo.codigo"),"codigoArticulo");
		}else
			if(tipoCodigoMed.equals(ConstantesIntegridadDominio.acronimoInterfaz)){
				projectionList.add(Projections.property("articulo.codigoInterfaz"),"codigoInterfaz");
			}
		
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulos.class));
		ArrayList<DtoArticulos> listaCodigosArticulo = (ArrayList<DtoArticulos>)criteria.list();
	
		ArrayList<String> lista=new ArrayList<String>();
		if(tipoCodigoMed.equals(ConstantesIntegridadDominio.acronimoAxioma)){
			for(DtoArticulos articulo:listaCodigosArticulo){
				lista.add(articulo.getCodigoArticulo().toString());
			}
		}else if(tipoCodigoMed.equals(ConstantesIntegridadDominio.acronimoInterfaz)){
			for(DtoArticulos articulo:listaCodigosArticulo){
				if(!UtilidadTexto.isEmpty(articulo.getCodigoInterfaz()))
					lista.add(articulo.getCodigoInterfaz().trim());
			}
		}
		
		
		return lista;
	
		
	}
	
	/**
	 * Este Método se encarga de obtener los distintos articulos
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un convenio.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Articulo>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
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
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Articulo.class, "articulo");
	 	criteria.createAlias("articulo.cierreNivelAteClaseins", "cierreArticulos")
	 			.createAlias("articulo.nivelAtencion", "nivelAtencion")
	 			.createAlias("cierreArticulos.contratos", "contratos")
	 			.createAlias("contratos.convenios", "convenio");
	 	criteria.add(Restrictions.eq("convenio.codigo", codigoConvenio))
	 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
	 			.add(Restrictions.eq("cierreArticulos.tipoProceso", proceso))
	 			.add(Restrictions.between("cierreArticulos.fechaCierre", fechaInicio, fechaFin));
	 	criteria.addOrder(Order.asc("articulo.descripcion"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
	 	return (ArrayList<Articulo>)criteria.list();
	}
	
	/**
	 * Este Método se encarga de obtener los distintos articulos
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Articulo>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorContratoPorProceso(int codigoContrato, 
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
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Articulo.class, "articulo");
	 	criteria.createAlias("articulo.cierreNivelAteClaseins", "cierreArticulos")
	 			.createAlias("articulo.nivelAtencion", "nivelAtencion")
	 			.createAlias("cierreArticulos.contratos", "contrato");
	 	criteria.add(Restrictions.eq("contrato.codigo", codigoContrato))
	 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
	 			.add(Restrictions.eq("cierreArticulos.tipoProceso", proceso))
	 			.add(Restrictions.between("cierreArticulos.fechaCierre", fechaInicio, fechaFin));
	 	criteria.addOrder(Order.asc("articulo.descripcion"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
	 	return (ArrayList<Articulo>)criteria.list();
	}
	
	
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente de un articulo
	 * @return DtoArticulos Dto que almacena la información del articulo y su tarifa 
	 * @author, Fabian Becerra
	 */
	@SuppressWarnings("unchecked")
	public DtoArticulos obtenerTarifaVigenteArticulos(int codigoArticulo,int esquemaTarifario){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Articulo.class,"articulo");
		
		criteria.createAlias("articulo.tarifasInventarios", "tarifasInventario")
				.createAlias("tarifasInventario.esquemasTarifarios", "esquemaTarifario")
		 		;
		
		if(codigoArticulo!=ConstantesBD.codigoNuncaValido)
			criteria.add(Restrictions.eq("articulo.codigo", codigoArticulo));
		
		criteria.add(Restrictions.eq("esquemaTarifario.codigo", esquemaTarifario));
		
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.le("tarifasInventario.fechaVigencia", UtilidadFecha.getFechaActualTipoBD()));
		disjunction.add(Restrictions.isNull("tarifasInventario.fechaVigencia"));
		criteria.add(disjunction);
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
			.add(Projections.property("articulo.codigo"),"codigoArticulo")
			.add(Projections.property("articulo.descripcion"),"descripcionArticulo")
			.add(Projections.property("tarifasInventario.valorTarifa"),"valorTarifa")
			.add(Projections.property("tarifasInventario.fechaVigencia"),"fechaVigenciaTarifa")
			//MT6749 se agrega campos
			.add(Projections.property("tarifasInventario.fechaModifica"),"fechaModifica")
			.add(Projections.property("tarifasInventario.horaModifica"),"horaModifica")
		));
		
		//MT6749 se agrega ordenamiento para que me presente la ultima tarifa.
		criteria.addOrder( Order.desc("tarifasInventario.fechaVigencia") );
		criteria.addOrder( Order.desc("tarifasInventario.fechaModifica") );
		criteria.addOrder( Order.desc("tarifasInventario.horaModifica") );
				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulos.class));
		
		ArrayList<DtoArticulos>  listaArticulos = 
			(ArrayList<DtoArticulos> )criteria.list();
		
		//---- OBTENER LA TARIFA VIGENTE
		if(Utilidades.isEmpty(listaArticulos)){
			return null;
		}
		else{
			//--Se toma la ultima fecha de vigencia antes de la actual  
			if(listaArticulos.size()==1)//Si solo existe un registro lo devuelve
			{
				return listaArticulos.get(0);
			}
			else
			{
				if(listaArticulos.get(0).getFechaVigenciaTarifa()==null)//No devuelve la tarifa nula si existen mas registros
				{
					return listaArticulos.get(1);
			}
				else
				{
					return listaArticulos.get(0);
				}
			}
		}
	}
}
