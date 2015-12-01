/**
 * 
 */
package com.servinte.axioma.orm.delegate.capitacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.UtilidadFecha;

import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.CierreNivelAteClasein;
import com.servinte.axioma.orm.CierreNivelAteClaseinHome;
import com.servinte.axioma.orm.ClaseInventario;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class CierreNivelAteClaseinDelegate extends CierreNivelAteClaseinHome 
{


	/**
	 * Lista todos según los parametros
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerCierres(DtoTotalProcesoPresupuestoCapitado parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreNivelAteClasein");
		
		if(parametros.getFecha() != null){
			criteria.add(Restrictions.eq("cierreNivelAteClasein.fechaCierre", parametros.getFecha()));
		}
		if(parametros.getContrato() != null){
			criteria.createAlias("cierreNivelAteClasein.contratos", "contrato",Criteria.INNER_JOIN);
			criteria.add(Restrictions.eq("contrato.codigo", parametros.getContrato()));
		}
		if(parametros.getConvenio() != null){
			criteria.createAlias("contrato.convenios", "convenio",Criteria.INNER_JOIN);
			criteria.add(Restrictions.eq("convenio.codigo", parametros.getConvenio()));
		}
		
		ArrayList<DtoTotalProcesoPresupuestoCapitado> listaResultado = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		listaResultado = (ArrayList<DtoTotalProcesoPresupuestoCapitado>)criteria.list();
		
		return listaResultado;
	}
	

	/**
	 * Obtiene la sumatoria de los valores totales de los articulos de un proceso
	 * para un rango de fechas determinado para un nivel de atención. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ArrayList<Convenios>
	*/
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorProceso(int codigoContrato, long consecutivoNivel, String proceso,
			Date fechaInicio, Date fechaFin){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
		criteria.createAlias("cierreArticulos.articulo", "articulo");
		criteria.createAlias("cierreArticulos.contratos", "contrato");
		criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
		
		criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
		criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
		criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
		criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, fechaInicio, fechaFin));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
				.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
				.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
		
		List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
										.list();
		if(lista.isEmpty()){
			lista.add(new DtoTotalProceso(proceso, new BigDecimal(0)));
		}
		return lista;
	}
	
	
	/**
	 * Obtiene la sumatoria de los valores totales de las clases de inventario de un proceso
	 * para un rango de fechas determinado para un nivel de atención. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param codigoClaseInventario
	 * @param proceso
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ArrayList<Convenios>
	*/
	public List<DtoTotalProceso> obtenerTotalArticulosPorNivelPorClasePorProceso(int codigoContrato, long consecutivoNivel, 
			int codigoClaseInventario, String proceso, Date fechaInicio, Date fechaFin)
	{
		List<DtoTotalProceso> lista = new ArrayList<DtoTotalProceso>();
		
		Criteria criteriaSubGrupo = sessionFactory.getCurrentSession().createCriteria(ClaseInventario.class, "claseInventario");
		criteriaSubGrupo.createAlias("claseInventario.grupoInventarios", "grupos");
		criteriaSubGrupo.createAlias("grupos.subgrupoInventarios", "subGrupos");
		criteriaSubGrupo.add(Restrictions.eq("claseInventario.codigo" , codigoClaseInventario));
		criteriaSubGrupo.setProjection(Projections.projectionList()
				.add(Projections.property("subGrupos.codigo")));
		if(criteriaSubGrupo.list()!= null && !criteriaSubGrupo.list().isEmpty()){
		
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
				criteria.createAlias("cierreArticulos.articulo", "articulo");
				criteria.createAlias("cierreArticulos.contratos", "contrato");
				criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
				
				criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
				criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
				criteria.add(Restrictions.in("articulo.subgrupo" , criteriaSubGrupo.list()));
				criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
				criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, fechaInicio, fechaFin));
				
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
						.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
						.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
				
				lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
		}
		if(lista.isEmpty()){
			lista.add(new DtoTotalProceso(proceso, new BigDecimal(0)));
		}
		return lista;
	}
	
	
	@Override
	public void attachDirty(CierreNivelAteClasein instance) {
		super.attachDirty(instance);
	}
	
	
	
	/**
	 * Elimina la isntancia
	 * @param persistentInstance
	 */
	public void eliminarCierreNivelAteClasein(CierreNivelAteClasein persistentInstance) 
	{
		super.delete(persistentInstance);
	}
	
	
	
	/**
	 * Lista por parametros
	 * 
	 * @param parametros
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<CierreNivelAteClasein>
	 */
	public ArrayList<CierreNivelAteClasein> obtenerCierresCierreNivelAteClasein(DtoTotalProcesoPresupuestoCapitado parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreNivelAteClasein");
		criteria.createAlias("cierreNivelAteClasein.contratos"								, "contrato"						);
		criteria.createAlias("contrato.convenios"											, "convenio"						);
		
		if(parametros.getFecha() != null)
		{
			criteria.add(Restrictions.eq("fechaCierre", parametros.getFecha()));
		}
		if(parametros.getContrato()!=null)
		{
			criteria.add(Restrictions.eq("contrato.codigo", parametros.getContrato().intValue()));
		}
		
		return (ArrayList<CierreNivelAteClasein>)criteria.list();
	}
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProceso(int codigoConvenio, 
							long consecutivoNivel, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
			criteria.createAlias("cierreArticulos.contratos", "contrato");
			criteria.createAlias("cierreArticulos.articulo", "articulo");
			criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
			criteria.createAlias("contrato.convenios", "convenio");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio));
			criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
					.add(Projections.sum("cierreArticulos.cantidad"),"cantidad")
					.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
			
			List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
			if(lista.isEmpty()){
				lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
			}
			DtoTotalProceso totalProceso= new DtoTotalProceso();
			totalProceso=lista.get(0);
			int idMes=mes.get(Calendar.MONTH);
			dtoMesTotal.setNumeroMes(idMes);
			dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
			dtoMesTotal.setCantidadArticulos(totalProceso.getCantidad());
			dtoMesTotal.setValorArticulos(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(int codigoContrato, 
							long consecutivoNivel, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
			criteria.createAlias("cierreArticulos.contratos", "contrato");
			criteria.createAlias("cierreArticulos.articulo", "articulo");
			criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
			criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
					.add(Projections.sum("cierreArticulos.cantidad"),"cantidad")
					.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
			
			List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
			if(lista.isEmpty()){
				lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
			}
			DtoTotalProceso totalProceso= new DtoTotalProceso();
			totalProceso=lista.get(0);
			int idMes=mes.get(Calendar.MONTH);
			dtoMesTotal.setNumeroMes(idMes);
			dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
			dtoMesTotal.setCantidadArticulos(totalProceso.getCantidad());
			dtoMesTotal.setValorArticulos(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param claseInventario
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(int codigoConvenio, 
							long consecutivoNivel, ClaseInventario claseInventario, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			//Primero se consultan las subgrupos de la clase de inventario par luego buscar los cierres que
			//tengan articulos asociados a cada uno de los subgrupos de la clase de inventario
			Criteria criteriaSubGrupo = sessionFactory.getCurrentSession().createCriteria(ClaseInventario.class, "claseInventario");
			criteriaSubGrupo.createAlias("claseInventario.grupoInventarios", "grupos");
			criteriaSubGrupo.createAlias("grupos.subgrupoInventarios", "subGrupos");
			criteriaSubGrupo.add(Restrictions.eq("claseInventario.codigo" , claseInventario.getCodigo()));
			criteriaSubGrupo.setProjection(Projections.projectionList()
					.add(Projections.property("subGrupos.codigo")));
			if(criteriaSubGrupo.list()!= null && !criteriaSubGrupo.list().isEmpty()){
				Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
				criteria.createAlias("cierreArticulos.contratos", "contrato");
				criteria.createAlias("contratos.convenios", "convenio");
				criteria.createAlias("cierreArticulos.articulo", "articulo");
				criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
				
				criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
				criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio));
				criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
				criteria.add(Restrictions.in("articulo.subgrupo" , criteriaSubGrupo.list()));
				criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, inicio.getTime(), fin.getTime()));
				
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
						.add(Projections.sum("cierreArticulos.cantidad"),"cantidad")
						.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
						.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
				
				List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
												.list();
				if(lista.isEmpty()){
					lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
				}
				DtoTotalProceso totalProceso= new DtoTotalProceso();
				totalProceso=lista.get(0);
				int idMes=mes.get(Calendar.MONTH);
				dtoMesTotal.setNumeroMes(idMes);
				dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
				dtoMesTotal.setClaseInventario(claseInventario.getNombre());
				dtoMesTotal.setCantidadArticulos(totalProceso.getCantidad());
				dtoMesTotal.setValorArticulos(totalProceso.getTotalProceso());
				listadoMeses.add(dtoMesTotal);
			}
		}
		return listadoMeses;
	}
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los articulos de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param claseInventario
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(int codigoContrato, 
							long consecutivoNivel, ClaseInventario claseInventario, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			//Primero se consultan las subgrupos de la clase de inventario par luego buscar los cierres que
			//tengan articulos asociados a cada uno de los subgrupos de la clase de inventario
			Criteria criteriaSubGrupo = sessionFactory.getCurrentSession().createCriteria(ClaseInventario.class, "claseInventario");
			criteriaSubGrupo.createAlias("claseInventario.grupoInventarios", "grupos");
			criteriaSubGrupo.createAlias("grupos.subgrupoInventarios", "subGrupos");
			criteriaSubGrupo.add(Restrictions.eq("claseInventario.codigo" , claseInventario.getCodigo()));
			criteriaSubGrupo.setProjection(Projections.projectionList()
					.add(Projections.property("subGrupos.codigo")));
			if(criteriaSubGrupo.list()!= null && !criteriaSubGrupo.list().isEmpty()){
				Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
				criteria.createAlias("cierreArticulos.contratos", "contrato");
				criteria.createAlias("cierreArticulos.articulo", "articulo");
				criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
				
				criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
				criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
				criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
				criteria.add(Restrictions.in("articulo.subgrupo" , criteriaSubGrupo.list()));
				criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, inicio.getTime(), fin.getTime()));
				
				criteria.setProjection(Projections.projectionList()
						.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
						.add(Projections.sum("cierreArticulos.cantidad"),"cantidad")
						.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
						.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
				
				List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
												.list();
				if(lista.isEmpty()){
					lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
				}
				DtoTotalProceso totalProceso= new DtoTotalProceso();
				totalProceso=lista.get(0);
				int idMes=mes.get(Calendar.MONTH);
				dtoMesTotal.setNumeroMes(idMes);
				dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
				dtoMesTotal.setClaseInventario(claseInventario.getNombre());
				dtoMesTotal.setCantidadArticulos(totalProceso.getCantidad());
				dtoMesTotal.setValorArticulos(totalProceso.getTotalProceso());
				listadoMeses.add(dtoMesTotal);
			}
		}
		return listadoMeses;
	}

	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales del articulo de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param articulo
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(int codigoConvenio, 
							long consecutivoNivel, Articulo articulo, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
			criteria.createAlias("cierreArticulos.contratos", "contrato");
			criteria.createAlias("contratos.convenios", "convenio");
			criteria.createAlias("cierreArticulos.articulo", "articulo");
			criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio));
			criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
			criteria.add(Restrictions.eq("articulo.codigo" , articulo.getCodigo()));
			criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
					.add(Projections.sum("cierreArticulos.cantidad"),"cantidad")
					.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
			
			List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
			if(lista.isEmpty()){
				lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
			}
			DtoTotalProceso totalProceso= new DtoTotalProceso();
			totalProceso=lista.get(0);
			int idMes=mes.get(Calendar.MONTH);
			dtoMesTotal.setNumeroMes(idMes);
			dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
			dtoMesTotal.setArticulo(articulo.getDescripcion());
			dtoMesTotal.setCantidadArticulos(totalProceso.getCantidad());
			dtoMesTotal.setValorArticulos(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales del articulo de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param articulo
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(int codigoContrato, 
							long consecutivoNivel, Articulo articulo, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteClasein.class, "cierreArticulos");
			criteria.createAlias("cierreArticulos.contratos", "contrato");
			criteria.createAlias("cierreArticulos.articulo", "articulo");
			criteria.createAlias("articulo.nivelAtencion", "nivelAtencion");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
			criteria.add(Restrictions.eq("cierreArticulos.tipoProceso" , proceso));
			criteria.add(Restrictions.eq("articulo.codigo" , articulo.getCodigo()));
			criteria.add(Restrictions.between("cierreArticulos.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreArticulos.tipoProceso"),"proceso")
					.add(Projections.sum("cierreArticulos.cantidad"),"cantidad")
					.add(Projections.sum("cierreArticulos.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreArticulos.tipoProceso")));
			
			List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
			if(lista.isEmpty()){
				lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
			}
			DtoTotalProceso totalProceso= new DtoTotalProceso();
			totalProceso=lista.get(0);
			int idMes=mes.get(Calendar.MONTH);
			dtoMesTotal.setNumeroMes(idMes);
			dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
			dtoMesTotal.setArticulo(articulo.getDescripcion());
			dtoMesTotal.setCantidadArticulos(totalProceso.getCantidad());
			dtoMesTotal.setValorArticulos(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}

}
