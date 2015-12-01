package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DtoNivelAtencion;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.NivelAtencionHome;

/**
 * @author Cristhian Murillo
 */
public class NivelAtencionDelegate extends NivelAtencionHome
{
	
	
	/**
	 * Este método se encarga de consultar los niveles de atención activos 
	 * en el sistema 
	 * @param 
	 * @return ArrayList<NivelAtencion>
	 */
	@SuppressWarnings({ "unchecked" })
	public  ArrayList<NivelAtencion> obtenerNivelesAtencionActivos(){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "NivelAten");
		
		ProjectionList projection = Projections.projectionList();
		
		criteria.add(Restrictions.eq("NivelAten.activo",true));
		
		projection.add(Projections.property("NivelAten.consecutivo"),"consecutivo");
		projection.add(Projections.property("NivelAten.descripcion"),"descripcion");
		
		criteria.setProjection(Projections.distinct(projection));
		criteria.addOrder( Order.asc("NivelAten.descripcion") );
		criteria.setResultTransformer( Transformers.aliasToBean(NivelAtencion.class));
		ArrayList<NivelAtencion> listadoNivelesAtencion=(ArrayList)criteria.list();
		
		return listadoNivelesAtencion;
	}
	
	/**
	 * Este método se encarga de consultar los niveles de atención en el sistema.
	 *  
	 * @return ArrayList<NivelAtencion>
	 * 
	 * @author Cristhian Murillo
	 */
	@SuppressWarnings("unchecked")
	public  ArrayList<NivelAtencion> obtenerNivelesAtencion(){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
		
		ArrayList<NivelAtencion> listaNivelAtencion = (ArrayList<NivelAtencion>)criteria.list();
		
		return listaNivelAtencion;
	}
	
	
	
	/**
	 * Implementacion del findById.
	 * 
	 * @param id
	 * @return NivelAtencion
	 * 
	 * @author Cristhian Murillo
	 */
	public NivelAtencion findById(long id) {
		return super.findById(id);
	}
	

	/**
	 * Lista los niveles de atención de un contrato y que tengan una
	 * parametrización de presupuesto para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param mesAnio
	 * @return ArrayList<DtoNivelReporte>
	*/
	@SuppressWarnings("unchecked")
	public List<DtoNivelReporte> listarNivelesAtencionConParametrizacionPresupuestoPorContrato(int codigoContrato, Calendar mesAnio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
			criteria.createAlias("nivelAtencion.contratoses", "contrato");
			criteria.createAlias("nivelAtencion.valorizacionPresCapGens", "valorizacion");
			criteria.createAlias("valorizacion.paramPresupuestosCap", "presupuesto");
			criteria.createAlias("presupuesto.contratos", "contrato2");
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("nivelAtencion.consecutivo"),"consecutivo")
					.add(Projections.property("nivelAtencion.descripcion"),"nombre")
					.add(Projections.sum("valorizacion.valorGastoSubSeccion"),"totalPresupuestado")
					.add(Projections.groupProperty("nivelAtencion.consecutivo"))
					.add(Projections.groupProperty("nivelAtencion.descripcion")));
			
			criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato))
					.add(Restrictions.eq("contrato2.codigo" , codigoContrato))
					.add(Restrictions.eq("valorizacion.mes"	, mesAnio.get(Calendar.MONTH)))
					.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))))
					.addOrder(Order.asc("nivelAtencion.descripcion"))
					.setResultTransformer(Transformers.aliasToBean(DtoNivelReporte.class));
			
			
			List<DtoNivelReporte> lista =  criteria.list();
			return lista;
	}

	/**
	 * @author diecorqu
	 * 
	 * Lista los Niveles de servicio por contrato
	 * @param codContrato
	 * @return ArrayList<NivelAtencion>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<NivelAtencion> listarNivelesAtencionContrato(int codContrato)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
		criteria.createAlias("nivelAtencion.contratoses", "contrato").
			add(Restrictions.eq("contrato.codigo", codContrato)).
			addOrder(Property.forName("nivelAtencion.consecutivo").asc());
		
		return (ArrayList<NivelAtencion>)criteria.list();
	}
	
	/**
	 * @author diecorqu
	 * 
	 * Lista los Niveles de atencion asociados a la parametrización general
	 * @param codContrato
	 * @return ArrayList<NivelAtencion>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<NivelAtencion> listarNivelesAtencionParametrizacionPresupuesto(long codParametrizacion)	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
		criteria.createAlias("nivelAtencion.valorizacionPresCapGens", "valorizacion");
		criteria.createAlias("valorizacion.paramPresupuestosCap", "parametrizacion").
			add(Restrictions.eq("parametrizacion.codigo", codParametrizacion));
		criteria.setProjection(Projections.projectionList().
				add(Projections.groupProperty("valorizacion.nivelAtencion")));
		criteria.addOrder(Property.forName("valorizacion.nivelAtencion").asc());
		
		ArrayList<NivelAtencion> nivelesAtencion = (ArrayList<NivelAtencion>)criteria.list();
		
		for (NivelAtencion nivelAtencion : nivelesAtencion) {
			nivelAtencion.getDescripcion();
		}
		
		return nivelesAtencion; 
	}

	
	/**
	 * Lista los niveles de atención de un contrato y que tengan una
	 * parametrización detallada de presupuesto para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param mesAnio
	 * @return ArrayList<DtoNivelAtencion>
	*/
	@SuppressWarnings("unchecked")
	public List<DtoNivelAtencion> listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(int codigoContrato, Calendar mesAnio)
	{
		//Se consultarlos diferentes niveles de atención parametrizados para servicios
		Criteria criteriaServicios = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
			criteriaServicios.createAlias("nivelAtencion.contratoses", "contrato")
					.createAlias("nivelAtencion.detalleValorizacionServs", "detalleValServicios")
					.createAlias("detalleValServicios.paramPresupuestosCap", "presupuesto")
					.createAlias("presupuesto.contratos", "contrato2");
						
			criteriaServicios.add(Restrictions.eq("contrato.codigo" , codigoContrato))
					.add(Restrictions.eq("contrato2.codigo" , codigoContrato))
					.add(Restrictions.eq("detalleValServicios.mes"	, mesAnio.get(Calendar.MONTH)))
					.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))));
			
			ProjectionList projection = Projections.projectionList();		
			projection.add(Projections.property("nivelAtencion.consecutivo"),"consecutivo");
			projection.add(Projections.property("nivelAtencion.descripcion"),"descripcion");
			projection.add(Projections.property("nivelAtencion.codigo"),"codigo");
		
			criteriaServicios.setProjection(Projections.distinct(projection));
			criteriaServicios.addOrder(Order.asc("nivelAtencion.descripcion"));
			criteriaServicios.setResultTransformer(Transformers.aliasToBean(DtoNivelAtencion.class));
			List<DtoNivelAtencion> listaServicios =  criteriaServicios.list();
			
			//Se consultarlos diferentes niveles de atención parametrizados para articulos
			Criteria criteriaArticulos = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
			criteriaArticulos.createAlias("nivelAtencion.contratoses", "contrato")
					.createAlias("nivelAtencion.detalleValorizacionArts", "detalleValArticulos")
					.createAlias("detalleValArticulos.paramPresupuestosCap", "presupuesto")
					.createAlias("presupuesto.contratos", "contrato2");
						
			criteriaArticulos.add(Restrictions.eq("contrato.codigo" , codigoContrato))
					.add(Restrictions.eq("contrato2.codigo" , codigoContrato))
					.add(Restrictions.eq("detalleValArticulos.mes"	, mesAnio.get(Calendar.MONTH)))
					.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))));
			
			ProjectionList projectionArt = Projections.projectionList();		
			projectionArt.add(Projections.property("nivelAtencion.consecutivo"),"consecutivo");
			projectionArt.add(Projections.property("nivelAtencion.descripcion"),"descripcion");
			projectionArt.add(Projections.property("nivelAtencion.codigo"),"codigo");
		
			criteriaArticulos.setProjection(Projections.distinct(projectionArt));
					
			criteriaArticulos.addOrder(Order.asc("nivelAtencion.descripcion"));
			criteriaArticulos.setResultTransformer(Transformers.aliasToBean(DtoNivelAtencion.class));
			List<DtoNivelAtencion> listaArticulos =  criteriaArticulos.list();
			
			List<DtoNivelAtencion> listaNiveles = new ArrayList<DtoNivelAtencion>();
			if(listaServicios != null && !listaServicios.isEmpty()){
				listaNiveles.addAll(listaServicios);
				if(listaArticulos != null && !listaArticulos.isEmpty()){
					for(DtoNivelAtencion nivelArticulos:listaArticulos){
						if(!listaServicios.contains(nivelArticulos)){
							listaNiveles.add(nivelArticulos);
						}
					}
				}
			}
			else{
				if(listaArticulos != null && !listaArticulos.isEmpty()){
					listaNiveles.addAll(listaArticulos);
				}
			}
			Collections.sort(listaNiveles);
			return listaNiveles;
	}
	
	
	/**
	 * Lista los distintos niveles de atención relacionados a servicios involucrados en un cierre
	 * para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorConvenio(int codigoConvenio, String proceso, List<Calendar> meses)
	{
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
		criteria.createAlias("nivelAtencion.servicioses", "servicio")
				.createAlias("servicio.cierreNivelAteGruServs", "cierreGrupoServicios")
				.createAlias("cierreGrupoServicios.contratos", "contrato")
				.createAlias("contrato.convenios", "convenio")
				.add(Restrictions.eq("convenio.codigo", codigoConvenio))
				.add(Restrictions.eq("cierreGrupoServicios.tipoProceso", proceso))
				.add(Restrictions.between("cierreGrupoServicios.fechaCierre", fechaInicio, fechaFin))
				.addOrder(Order.asc("nivelAtencion.descripcion"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (ArrayList<NivelAtencion>)criteria.list();
	}
	
	/**
	 * Lista los distintos niveles de atención relacionados a servicios involucrados en un cierre
	 * para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorContrato(int codigoContrato, String proceso, List<Calendar> meses)
	{
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
		criteria.createAlias("nivelAtencion.servicioses", "servicio")
				.createAlias("servicio.cierreNivelAteGruServs", "cierreGrupoServicios")
				.createAlias("cierreGrupoServicios.contratos", "contrato")
				.add(Restrictions.eq("contrato.codigo", codigoContrato))
				.add(Restrictions.eq("cierreGrupoServicios.tipoProceso", proceso))
				.add(Restrictions.between("cierreGrupoServicios.fechaCierre", fechaInicio, fechaFin))
				.addOrder(Order.asc("nivelAtencion.descripcion"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (ArrayList<NivelAtencion>)criteria.list();
	}
	
	/**
	 * Lista los distintos niveles de atención relacionados a articulos involucrados en un cierre
	 * para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorConvenio(int codigoConvenio, String proceso, List<Calendar> meses)
	{
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
		criteria.createAlias("nivelAtencion.articulos", "articulo")
				.createAlias("articulo.cierreNivelAteClaseins", "cierreClaseInventario")
				.createAlias("cierreClaseInventario.contratos", "contrato")
				.createAlias("contrato.convenios", "convenio")
				.add(Restrictions.eq("convenio.codigo", codigoConvenio))
				.add(Restrictions.eq("cierreClaseInventario.tipoProceso", proceso))
				.add(Restrictions.between("cierreClaseInventario.fechaCierre", fechaInicio, fechaFin))
				.addOrder(Order.asc("nivelAtencion.descripcion"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (ArrayList<NivelAtencion>)criteria.list();
	}
	
	/**
	 * Lista los distintos niveles de atención relacionados a articulos involucrados en un cierre
	 * para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param proceso
	 * @param meses
	 * @return ArrayList<NivelAtencion>
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorContrato(int codigoContrato, String proceso, List<Calendar> meses)
	{
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NivelAtencion.class, "nivelAtencion");
		criteria.createAlias("nivelAtencion.articulos", "articulo")
				.createAlias("articulo.cierreNivelAteClaseins", "cierreClaseInventario")
				.createAlias("cierreClaseInventario.contratos", "contrato")
				.add(Restrictions.eq("contrato.codigo", codigoContrato))
				.add(Restrictions.eq("cierreClaseInventario.tipoProceso", proceso))
				.add(Restrictions.between("cierreClaseInventario.fechaCierre", fechaInicio, fechaFin))
				.addOrder(Order.asc("nivelAtencion.descripcion"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (ArrayList<NivelAtencion>)criteria.list();
	}

}
