package com.servinte.axioma.orm.delegate.capitacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.ValorizacionPresCapGen;
import com.servinte.axioma.orm.ValorizacionPresCapGenHome;

/**
 * Clase encargada de ejecutar las transacciones del objeto 
 * {@link ValorizacionPresCapGen}
 * @author diecorqu
 *
 */
@SuppressWarnings("unchecked")
public class ValorizacionPresupuestoCapGeneralDelegate extends ValorizacionPresCapGenHome{
	
	/**
	 * Este método retorna la valorizacion para un mes y nivel de atención dado
	 * por codigo
	 * 
	 * @param codigo de valorizacion de presupuesto
	 * @return {@link ValorizacionPresCapGen}
	 */
	public ValorizacionPresCapGen findById(long codValorizacion) {
		return super.findById(codValorizacion);
	}	
	
	/**
	 * Este método retorna la valorizacion para el presupuesto capitado 
	 * dado
	 * 
	 * @param código de la parametrizacion requerida
	 * @return ArrayList<{@link ValorizacionPresCapGen}>
	 */
	public ArrayList<ValorizacionPresCapGen> valoracionPresupuestoCap(long codigoParametrizacion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ValorizacionPresCapGen.class,"valorizacionPresCapGen");
			criteria.createAlias("valorizacionPresCapGen.paramPresupuestosCap", "parametrizacion").
			add(Restrictions.eq("parametrizacion.codigo",codigoParametrizacion));
		return (ArrayList<ValorizacionPresCapGen>)criteria.list();
	}	

	/**
	 * Este método guarda la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param ArrayList<ValorizacionPresCapGen> lista con las valorizaciones para un presupuesto general
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarValorizacionPresupuestoCapitado(ArrayList<ValorizacionPresCapGen> valoracionPresupuesto) {
		boolean save = false;
		try{
			for (ValorizacionPresCapGen valorizacion : valoracionPresupuesto) {
				super.persist(valorizacion);
				save = true;
			}
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo almacenar la valorización del presupuesto ",e);
		}		
		return save;
	}	
	
	/**
	 * Este método modifica la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param ValorizacionPresCapGen valorizacion
	 * @return boolean con el resultado de la operación de guardado
	 */
	public ValorizacionPresCapGen modificarValorizacionPresupuestoCapitado(
			ValorizacionPresCapGen valorizacion) {
		ValorizacionPresCapGen valorizacionModificada = null;
		try{
			valorizacionModificada = super.merge(valorizacion);
		}catch (Exception e) {
			Log4JManager.error("No se pudo almacenar la valorización del presupuesto ",e);
		}		
		return valorizacionModificada;
	}	
	
	/**
	 * Este método elimina la valorizacion de la parametrizacion de presupuesto capitado
	 * 
	 * @param código valorizacion a eliminar
	 * @return boolean con el resultado de la operación
	 */
	public void eliminarValorizacionPresupuestoCapitado(ValorizacionPresCapGen unidadValorizacion) {
		super.delete(unidadValorizacion);
	}	

	/**
	 * Obtiene la valorización general ingresada para un nivel de atención, subgrupo y mes 
	 * 
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param mesAnio
	 * @param grupoClase
	 * @return ArrayList<ValorizacionPresCapGen>
	*/
	public ValorizacionPresCapGen obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(long codigoParametrizacionGeneral, 
			long consecutivoNivel, int mes, String grupoClase) {
		Criteria criteria = sessionFactory.getCurrentSession().
			createCriteria(ValorizacionPresCapGen.class, "valorizacion");
				criteria.createAlias("valorizacion.paramPresupuestosCap", "paramPresupuestoCap");
				criteria.createAlias("valorizacion.nivelAtencion", "nivelAtencion");
				
			criteria.add(Restrictions.eq("paramPresupuestoCap.codigo", codigoParametrizacionGeneral)).
					 add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel)).
					 add(Restrictions.eq("valorizacion.subSeccion", grupoClase)).
					 add(Restrictions.eq("valorizacion.mes", mes));
					
			return (ValorizacionPresCapGen)criteria.uniqueResult();
	}
	
	/**
	 * Verifica si existe una valorización general ingresada para una parametrizacion
	 * de presupuesto de capitación, un nivel de atención y subgrupo determinado
	 * 
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param grupoClase
	 * @return boolean
	 * @author diecorqu
	*/
	public boolean existeValorizacionGeneralxNivelAtencionSubSeccion(long codigoParametrizacionGeneral, 
			long consecutivoNivel, String grupoClase) {
		Criteria criteria = sessionFactory.getCurrentSession().
			createCriteria(ValorizacionPresCapGen.class, "valorizacion");
				criteria.createAlias("valorizacion.paramPresupuestosCap", "paramPresupuestoCap");
				criteria.createAlias("valorizacion.nivelAtencion", "nivelAtencion");
				
			criteria.add(Restrictions.eq("paramPresupuestoCap.codigo", codigoParametrizacionGeneral)).
					 add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel)).
					 add(Restrictions.eq("valorizacion.subSeccion", grupoClase));
			
			ArrayList<ValorizacionPresCapGen> lista = (ArrayList<ValorizacionPresCapGen>)criteria.setMaxResults(1).list();
			return (lista == null || lista.size() == 0) ? false : true;		
	}
	
	/**
	 * Obtiene el total presupuestado de Articulos o Servicios por
	 * nivel de atención de un contrato para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param mesAnio
	 * @param grupoClase
	 * @return ArrayList<Convenios>
	*/
	public DtoTotalProceso obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(int codigoContrato, long consecutivoNivel,
										Calendar mesAnio, String grupoClase)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ValorizacionPresCapGen.class, "valorizacion");
			criteria.createAlias("valorizacion.nivelAtencion", "nivelAtencion");
			criteria.createAlias("valorizacion.paramPresupuestosCap", "presupuesto");
			criteria.createAlias("presupuesto.contratos", "contrato");
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("valorizacion.subSeccion"),"proceso")
					.add(Projections.property("valorizacion.valorGastoSubSeccion"),"totalProceso"));
			
			criteria.add(Restrictions.eq("valorizacion.mes"	, mesAnio.get(Calendar.MONTH)))
					.add(Restrictions.eq("valorizacion.subSeccion"	, grupoClase))
					.add(Restrictions.eq("nivelAtencion.consecutivo"	, consecutivoNivel))
					.add(Restrictions.eq("contrato.codigo" , codigoContrato))
					.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))))
					.setResultTransformer(Transformers.aliasToBean(DtoTotalProceso.class));
			
			
			List<DtoTotalProceso> lista =  criteria.list();
			DtoTotalProceso total = new DtoTotalProceso(grupoClase, new BigDecimal(0));
			if(lista != null && !lista.isEmpty()){
				return lista.get(0);
			}
			return total;
	}
}
