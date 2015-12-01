/**
 * 
 */
package com.servinte.axioma.orm.delegate.capitacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.UtilidadFecha;

import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.CierreNivelAteGruServ;
import com.servinte.axioma.orm.CierreNivelAteGruServHome;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.ReferenciasServicio;
import com.servinte.axioma.orm.Servicios;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class CierreNivelAteGruServDelegate extends CierreNivelAteGruServHome 
{


	/**
	 * Lista todos según los parametros
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerCierres(DtoTotalProcesoPresupuestoCapitado parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreNivelAteGruServ");
		
		if(parametros.getFecha() != null){
			criteria.add(Restrictions.eq("cierreNivelAteGruServ.fechaCierre", parametros.getFecha()));
		}
		if(parametros.getContrato() != null){
			criteria.createAlias("cierreNivelAteGruServ.contratos", "contrato",Criteria.INNER_JOIN);
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
	 * Obtiene la sumatoria de los valores totales de los servicios de un proceso
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
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorProceso(int codigoContrato, long consecutivoNivel, String proceso,
			Date fechaInicio, Date fechaFin){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
		criteria.createAlias("cierreServicios.contratos", "contrato");
		criteria.createAlias("cierreServicios.servicios", "servicio");
		criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
		
		criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
		criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
		criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
		criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, fechaInicio, fechaFin));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
				.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
				.add(Projections.groupProperty("cierreServicios.tipoProceso")));
		
		List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
										.list();
		if(lista.isEmpty()){
			lista.add(new DtoTotalProceso(proceso, new BigDecimal(0)));
		}
		return lista;
	}
	
	
	/**
	 * Obtiene la sumatoria de los valores totales de los grupos de servicios de un proceso
	 * para un rango de fechas determinado para un nivel de atención. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param codigoGrupoServicio
	 * @param proceso
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ArrayList<Convenios>
	*/
	public List<DtoTotalProceso> obtenerTotalServiciosPorNivelPorGrupoPorProceso(int codigoContrato, long consecutivoNivel, 
			int codigoGrupoServicio, String proceso, Date fechaInicio, Date fechaFin)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
			criteria.createAlias("cierreServicios.servicios", "servicio");
			criteria.createAlias("cierreServicios.contratos", "contrato");
			criteria.createAlias("servicio.gruposServicios", "grupoServicio");
			criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
			
			criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
			criteria.add(Restrictions.eq("grupoServicio.codigo" , codigoGrupoServicio));
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));			
			criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, fechaInicio, fechaFin));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
					.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreServicios.tipoProceso")));
			
			List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
			if(lista.isEmpty()){
				lista.add(new DtoTotalProceso(proceso, new BigDecimal(0)));
			}
			return lista;
	}
	
	@Override
	public void attachDirty(CierreNivelAteGruServ instance) {
		super.attachDirty(instance);
	}
	
	
	
	/**
	 * Elimina la isntancia
	 * @param persistentInstance
	 */
	public void eliminarCierreNivelAteGruServ(CierreNivelAteGruServ persistentInstance) 
	{
		super.delete(persistentInstance);
	}
	
	
	
	/**
	 * Lista por parametros
	 * 
	 * @param parametros
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<CierreNivelAteGruServ>
	 */
	public ArrayList<CierreNivelAteGruServ> obtenerCierresCierreNivelAteGruServ(DtoTotalProcesoPresupuestoCapitado parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreNivelAteGruServ");
		criteria.createAlias("cierreNivelAteGruServ.contratos"								, "contrato"						);
		criteria.createAlias("contrato.convenios"											, "convenio"						);
		
		if(parametros.getFecha() != null){
			criteria.add(Restrictions.eq("fechaCierre", parametros.getFecha()));
		}
		if(parametros.getContrato()!=null)
		{
			criteria.add(Restrictions.eq("contrato.codigo", parametros.getContrato().intValue()));
		}
		
		return (ArrayList<CierreNivelAteGruServ>)criteria.list();
	}
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
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
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProceso(int codigoConvenio, 
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
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
			criteria.createAlias("cierreServicios.contratos", "contrato");
			criteria.createAlias("cierreServicios.servicios", "servicio");
			criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
			criteria.createAlias("contrato.convenios", "convenio");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio));
			criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
					.add(Projections.sum("cierreServicios.cantidad"),"cantidad")
					.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreServicios.tipoProceso")));
			
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
			dtoMesTotal.setCantidadServicios(totalProceso.getCantidad());
			dtoMesTotal.setValorServicios(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
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
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(int codigoContrato, 
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
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
			criteria.createAlias("cierreServicios.contratos", "contrato");
			criteria.createAlias("cierreServicios.servicios", "servicio");
			criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
			criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
					.add(Projections.sum("cierreServicios.cantidad"),"cantidad")
					.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreServicios.tipoProceso")));
			
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
			dtoMesTotal.setCantidadServicios(totalProceso.getCantidad());
			dtoMesTotal.setValorServicios(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}

	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los grupos de servicios de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param grupoServicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(
							int codigoConvenio, long consecutivoNivel, GruposServicios grupoServicio, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
			criteria.createAlias("cierreServicios.contratos", "contrato");
			criteria.createAlias("cierreServicios.servicios", "servicio");
			criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
			criteria.createAlias("servicio.gruposServicios", "grupoServicio");
			criteria.createAlias("contrato.convenios", "convenio");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("grupoServicio.codigo" , grupoServicio.getCodigo()));
			criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio));
			criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
					.add(Projections.sum("cierreServicios.cantidad"),"cantidad")
					.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreServicios.tipoProceso")));
			
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
			dtoMesTotal.setGrupoServicio(grupoServicio.getDescripcion());
			dtoMesTotal.setCantidadServicios(totalProceso.getCantidad());
			dtoMesTotal.setValorServicios(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}
	
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los grupos de servicios de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param grupoServicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(int codigoContrato, 
							long consecutivoNivel, GruposServicios grupoServicio, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
			criteria.createAlias("cierreServicios.contratos", "contrato");
			criteria.createAlias("cierreServicios.servicios", "servicio");
			criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
			criteria.createAlias("servicio.gruposServicios", "grupoServicio");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("grupoServicio.codigo" , grupoServicio.getCodigo()));
			criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
			criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
					.add(Projections.sum("cierreServicios.cantidad"),"cantidad")
					.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreServicios.tipoProceso")));
			
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
			dtoMesTotal.setGrupoServicio(grupoServicio.getDescripcion());
			dtoMesTotal.setCantidadServicios(totalProceso.getCantidad());
			dtoMesTotal.setValorServicios(totalProceso.getTotalProceso());
			listadoMeses.add(dtoMesTotal);
		}
		return listadoMeses;
	}
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes para un convenio. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param servicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(
							int codigoConvenio, long consecutivoNivel, Servicios servicio, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
			criteria.createAlias("cierreServicios.contratos", "contrato");
			criteria.createAlias("cierreServicios.servicios", "servicio");
			criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
			criteria.createAlias("contrato.convenios", "convenio");
			
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("servicio.codigo" , servicio.getCodigo()));
			criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio));
			criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
					.add(Projections.sum("cierreServicios.cantidad"),"cantidad")
					.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreServicios.tipoProceso")));
			
			List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
			if(lista.isEmpty()){
				lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
			}
			DtoTotalProceso totalProceso= new DtoTotalProceso();
			totalProceso=lista.get(0);
			
			if(servicio.getReferenciasServicios() != null && !servicio.getReferenciasServicios().isEmpty()){
				String nombreServicio="";
				for(Iterator<ReferenciasServicio> nombre=servicio.getReferenciasServicios().iterator(); nombre.hasNext();){
					ReferenciasServicio referenciaServicio=nombre.next();
					nombreServicio=referenciaServicio.getDescripcion();
					break;
				}
				
				int idMes=mes.get(Calendar.MONTH);
				dtoMesTotal.setNumeroMes(idMes);
				dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
				dtoMesTotal.setServicio(nombreServicio);
				dtoMesTotal.setCantidadServicios(totalProceso.getCantidad());
				dtoMesTotal.setValorServicios(totalProceso.getTotalProceso());
				listadoMeses.add(dtoMesTotal);
			}
		}
		return listadoMeses;
	}
	
	/**
	 * Obtiene el consolidado por cada mes de la sumatoria de los valores totales de los servicios de un proceso
	 * para un nivel de atención para cada mes para un contrato. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param servicio
	 * @param proceso
	 * @param List<Calendar> meses
	 * @return ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>
	*/
	@SuppressWarnings("static-access")
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(
							int codigoContrato, long consecutivoNivel, Servicios servicio, String proceso, List<Calendar> meses){
		
		ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listadoMeses = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		UtilidadFecha utilidadFecha = new UtilidadFecha();
		
		for(Calendar mes:meses){
			DtoMesesTotalServiciosArticulosValorizadosPorConvenio dtoMesTotal = new DtoMesesTotalServiciosArticulosValorizadosPorConvenio();
			Calendar inicio=Calendar.getInstance();
			Calendar fin=Calendar.getInstance();
			//Se setea la fecha de cada mes obteniendo el primer y último día de cada mes
			inicio.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(mes.get(Calendar.YEAR), mes.get(Calendar.MONTH),mes.getActualMaximum(Calendar.DAY_OF_MONTH));
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CierreNivelAteGruServ.class, "cierreServicios");
			criteria.createAlias("cierreServicios.contratos", "contrato");
			criteria.createAlias("cierreServicios.servicios", "servicio");
			criteria.createAlias("servicio.nivelAtencion", "nivelAtencion");
						
			criteria.add(Restrictions.eq("nivelAtencion.consecutivo" , consecutivoNivel));
			criteria.add(Restrictions.eq("servicio.codigo" , servicio.getCodigo()));
			criteria.add(Restrictions.eq("contrato.codigo" , codigoContrato));
			criteria.add(Restrictions.eq("cierreServicios.tipoProceso" , proceso));
			criteria.add(Restrictions.between("cierreServicios.fechaCierre"	, inicio.getTime(), fin.getTime()));
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("cierreServicios.tipoProceso"),"proceso")
					.add(Projections.sum("cierreServicios.cantidad"),"cantidad")
					.add(Projections.sum("cierreServicios.valorAcumulado"),"totalProceso")
					.add(Projections.groupProperty("cierreServicios.tipoProceso")));
			
			List<DtoTotalProceso> lista = criteria.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class))
											.list();
			if(lista.isEmpty()){
				lista.add(new DtoTotalProceso(proceso, 0, new BigDecimal(0)));
			}
			DtoTotalProceso totalProceso= new DtoTotalProceso();
			totalProceso=lista.get(0);
			
			if(servicio.getReferenciasServicios() != null && !servicio.getReferenciasServicios().isEmpty()){
				String nombreServicio="";
				for(Iterator<ReferenciasServicio> nombre=servicio.getReferenciasServicios().iterator(); nombre.hasNext();){
					ReferenciasServicio referenciaServicio=nombre.next();
					nombreServicio=referenciaServicio.getDescripcion();
					break;
				}
				
				int idMes=mes.get(Calendar.MONTH);
				dtoMesTotal.setNumeroMes(idMes);
				dtoMesTotal.setNombreMes(utilidadFecha.obtenerNombreMesProperties(idMes));
				dtoMesTotal.setServicio(nombreServicio);
				dtoMesTotal.setCantidadServicios(totalProceso.getCantidad());
				dtoMesTotal.setValorServicios(totalProceso.getTotalProceso());
				listadoMeses.add(dtoMesTotal);
			}
		}
		return listadoMeses;
	}
}
