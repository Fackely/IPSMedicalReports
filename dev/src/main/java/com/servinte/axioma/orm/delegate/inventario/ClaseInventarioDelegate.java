/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.inventario.DtoClaseInventario;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.CierreNivelAteClasein;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.ClaseInventarioHome;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class ClaseInventarioDelegate extends ClaseInventarioHome 
{
	private IPersistenciaSvc persistenciaSvc;
	/**
	 * 	
	 * Este M�todo se encarga de consultar las clases de inventarios
	 * en el sistema
	 * 
	 * @return ArrayList<ClaseInventario>
	 * @author, Angela Maria Aguirre
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ClaseInventario> buscarClaseInventario()
	{
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ClaseInventario.class);
		 
		 ArrayList<ClaseInventario> listaClaseInventarios = new ArrayList<ClaseInventario>();
		 listaClaseInventarios = (ArrayList<ClaseInventario>)criteria.list();
		 
		 return listaClaseInventarios;
	} 
	
	
	 /** 	
	 * Este M�todo se encarga de obtener la calse inventario partiendo del subgrupo inventario (Tabla: subgrupo_inventario) dado.
	 * El valor de codigoSubgrupoInventario NO ES LA LLAVE PRIMARIA, es el campo subgrupo (�nico de subgrupo_inventario)
	 * 
	 * @param subgrupo
	 * @return DtoClaseInventario
	 * @author Cristhian Murillo
	 */
	public DtoClaseInventario obtenerClaseInventarioPorSungrupo(int codigoSubgrupoInventario)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ClaseInventario.class, "claseInventario");
		
		criteria.createAlias("claseInventario.instituciones"		, "instituciones"		, Criteria.INNER_JOIN);
		criteria.createAlias("claseInventario.grupoInventarios"		, "grupoInventarios"	, Criteria.INNER_JOIN);
		criteria.createAlias("grupoInventarios.subgrupoInventarios"	, "subgrupoInventarios"	, Criteria.INNER_JOIN);
		
		criteria.add(Restrictions.eq("subgrupoInventarios.codigo", codigoSubgrupoInventario));
		 
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("claseInventario.codigo")						,"codigo")
				.add(Projections.property("instituciones.codigo")						,"institucion")								
				.add(Projections.property("claseInventario.nombre")        				,"nombre")
		)); 
		 
		criteria.setResultTransformer(Transformers.aliasToBean(DtoClaseInventario.class));
					
		return (DtoClaseInventario)criteria.uniqueResult();
	} 
	
	/**
	 * Este M�todo se encarga de obtener las distintas clases de Inventario
	 * que tienen asociado un articulo el cual se encuentra en el cierre de presupuesto
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
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
										long consecutivoNivel, String proceso, List<Calendar> meses){
		
		ArrayList<ClaseInventario> clasesInventarios = new ArrayList<ClaseInventario>();
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
		
		Criteria criteriaSubGrupos = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
	 	criteriaSubGrupos.createAlias("cierreArticulos.articulo", "articulo")
	 			.createAlias("articulo.nivelAtencion", "nivelAtencion")
	 			.createAlias("cierreArticulos.contratos", "contratos")
	 			.createAlias("contratos.convenios", "convenio");
	 	criteriaSubGrupos.add(Restrictions.eq("convenio.codigo", codigoConvenio))
	 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
	 			.add(Restrictions.eq("cierreArticulos.tipoProceso", proceso))
	 			.add(Restrictions.between("cierreArticulos.fechaCierre", fechaInicio, fechaFin));
	 	
	 	ProjectionList projection = Projections.projectionList();
	 			projection.add(Projections.property("articulo.subgrupo"));
	 	
	 	criteriaSubGrupos.setProjection(Projections.distinct(projection));
	
		
	 	if(criteriaSubGrupos.list()!= null && !criteriaSubGrupos.list().isEmpty()){
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ClaseInventario.class, "claseInventario");
			criteria.createAlias("claseInventario.grupoInventarios", "grupos");
			criteria.createAlias("grupos.subgrupoInventarios", "subGrupos");
			criteria.add(Restrictions.in("subGrupos.codigo", criteriaSubGrupos.list()));
			
			criteria.addOrder(Order.asc("claseInventario.nombre"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			clasesInventarios=(ArrayList<ClaseInventario>)criteria.list();
	 	}
	 	
	 	return clasesInventarios;
	}
	
	/**
	 * Este M�todo se encarga de obtener las distintas clases de Inventario
	 * que tienen asociado un articulo el cual se encuentra en el cierre de presupuesto
	 * para un nivel de atenci�n para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<GruposServicios>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorContratoPorProceso(int codigoContrato, 
										long consecutivoNivel, String proceso, List<Calendar> meses){
		
		ArrayList<ClaseInventario> clasesInventarios = new ArrayList<ClaseInventario>();
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
		
		Criteria criteriaSubGrupos = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
	 	criteriaSubGrupos.createAlias("cierreArticulos.articulo", "articulo")
	 			.createAlias("articulo.nivelAtencion", "nivelAtencion")
	 			.createAlias("cierreArticulos.contratos", "contratos");
	 	criteriaSubGrupos.add(Restrictions.eq("contratos.codigo", codigoContrato))
	 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
	 			.add(Restrictions.eq("cierreArticulos.tipoProceso", proceso))
	 			.add(Restrictions.between("cierreArticulos.fechaCierre", fechaInicio, fechaFin));
	 	
	 	ProjectionList projection = Projections.projectionList();
	 			projection.add(Projections.property("articulo.subgrupo"));
	 	
	 	criteriaSubGrupos.setProjection(Projections.distinct(projection));
	
		
	 	if(criteriaSubGrupos.list()!= null && !criteriaSubGrupos.list().isEmpty()){
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ClaseInventario.class, "claseInventario");
			criteria.createAlias("claseInventario.grupoInventarios", "grupos");
			criteria.createAlias("grupos.subgrupoInventarios", "subGrupos");
			criteria.add(Restrictions.in("subGrupos.codigo", criteriaSubGrupos.list()));
			
			criteria.addOrder(Order.asc("claseInventario.nombre"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			clasesInventarios=(ArrayList<ClaseInventario>)criteria.list();
	 	}
	 	
	 	return clasesInventarios;
	}
	
	/**
	 * Este Método se encarga de consultar la lista de clase inventarios
	 * 
	 * @author ginsotfu
	 *
	 * @param 
	 * @return List<ClaseInventarioDto>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<ClaseInventarioDto> consultarClaseInventario() throws BDException{
		List<ClaseInventarioDto> listaClasesInventario=new ArrayList<ClaseInventarioDto>(0);
		try{
			persistenciaSvc= new PersistenciaSvc();
			listaClasesInventario=(List<ClaseInventarioDto>)persistenciaSvc.createNamedQuery("catalogoInventario.consultarClaseInventario");
			return listaClasesInventario;

			}
			catch (Exception e){
				Log4JManager.error(e);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
			}		
	}
}
