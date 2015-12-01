package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion;
import com.servinte.axioma.dto.capitacion.DtoLogParamPresupCap;
import com.servinte.axioma.dto.capitacion.DtoParamPresupCap;
import com.servinte.axioma.orm.LogParamPresupuestoCap;
import com.servinte.axioma.orm.LogParamPresupuestoCapHome;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Clase encargada de ejecutar las transacciones del objeto {@link LogParamPresupuestoCap}
 * @author diecorqu
 *
 */
@SuppressWarnings("unchecked")
public class LogParametrizacionPresupuestoCapDelegate extends LogParamPresupuestoCapHome {

	/**
	 * Este m�todo retorna el log de parametrizaci�n por id
	 * 
	 * @param codigo Log
	 * @return LogParamPresupuestoCap
	 */
	public LogParamPresupuestoCap findById(long codigoLog) {
		LogParamPresupuestoCap logParametrizacion = null;
		logParametrizacion = super.findById(codigoLog);
		logParametrizacion.getContratos().getNumeroContrato();
		logParametrizacion.getConvenios().getNombre();
		logParametrizacion.getUsuarios().getPersonas().getPrimerNombre();
		logParametrizacion.getMotivosModifiPresupuesto().getDescripcion();
		return logParametrizacion;
	}	

	/**
	 * Este m�todo guarda el log para la parametrizacion del presupuesto
	 * 
	 * @param Log Parametrizacion del presupuesto
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean guardarLogParametrizacionPresupuesto(LogParamPresupuestoCap logParametrizacion) {
		boolean save = false;
		try {
			super.persist(logParametrizacion);
			save = true;
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo almacenar el log para la parametrizacion del presupuesto ",e);
		}		
		return save;
	}	
	
	/**
	 * Este m�todo verifica si existe log para la parametrizaci�n del presupuesto
	 * de una parametrizaci�n dada
	 * 
	 * @param codigo de la parametrizaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeLogParametrizacionPresupuesto(long codParametrizacion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(LogParamPresupuestoCap.class,"logParametrizacion");
		criteria.createAlias("logParametrizacion.paramPresupuestosCap", "parametrizacion").
			add(Restrictions.eq("parametrizacion.codigo", codParametrizacion));
		return (criteria.setMaxResults(1).list() == null || criteria.setMaxResults(1).list().size() == 0) ? false : true;
	}
	
	/**
	 * Este m�todo verifica si existe log con un motivo de modificaci�n asociado
	 * 
	 * @param codigo motivo de modificaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeLogParametrizacionMotivoModificacion(long codMotivoModificacion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(LogParamPresupuestoCap.class,"logParametrizacion");
		criteria.createAlias("logParametrizacion.motivosModifiPresupuesto", "motivosModificacion").
			add(Restrictions.eq("motivosModificacion.codigoPk", codMotivoModificacion));
		return (criteria.setMaxResults(1).list() == null || criteria.setMaxResults(1).list().size() == 0) ? false : true;
	}
	
	/**
	 * Este m�todo obtiene Log para la parametrizaci�n del presupuesto
	 * de una parametrizaci�n dada
	 * 
	 * @param codigo de la parametrizaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public LogParamPresupuestoCap obtenerLogParametrizacionPresupuesto(long codParametrizacion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(LogParamPresupuestoCap.class,"logParametrizacion");
		criteria.createAlias("logParametrizacion.paramPresupuestosCap", "parametrizacion").
			add(Restrictions.eq("parametrizacion.codigo", codParametrizacion));
		return (LogParamPresupuestoCap)criteria.uniqueResult();
	}
	
	/**
	 * Este m�todo obtiene la lista de resultados para la consulta de logs de parametrizaci�n
	 * del presupuesto entre fechas, convenio, contrato y a�o de vigencia 
	 * 
	 * @param DtoLogBusquedaParametrizacion Dto con los datos requeridos para la consulta
	 * @return ArrayList<DtoLogParamPresupCap> lista con resultados
	 */
	public ArrayList<DtoLogParamPresupCap> listarResultadosBusquedaLogParametrizacionPresupuesto(
			DtoLogBusquedaParametrizacion dtoBusqueda) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(LogParamPresupuestoCap.class,"logParametrizacion").
			createAlias("logParametrizacion.paramPresupuestosCap", "parametrizacion").
			createAlias("parametrizacion.contratos", "contrato").
			createAlias("contrato.convenios", "convenio");
			
		if (dtoBusqueda.getCodigoConvenio() != ConstantesBD.codigoNuncaValidoLong) {
			criteria.add(Restrictions.eq("convenio.codigo", dtoBusqueda.getCodigoConvenio()));
		}
		if (dtoBusqueda.getCodigoContrato() != ConstantesBD.codigoNuncaValido) {
			criteria.add(Restrictions.eq("contrato.codigo", dtoBusqueda.getCodigoContrato()));
		}
		if (!String.valueOf(ConstantesBD.codigoNuncaValido).equals(dtoBusqueda.getFechaVigencia())) {
			criteria.add(Restrictions.eq("parametrizacion.anioVigencia", dtoBusqueda.getFechaVigencia()));
		}
		criteria.add(Restrictions.between("logParametrizacion.fechaModificacion", 
										  dtoBusqueda.getFechaInicial(), 
										  dtoBusqueda.getFechaFinal())).
				 addOrder(Property.forName("fechaModificacion").desc()).
				 addOrder(Property.forName("horaModificacion").desc());
		
		ProjectionList projectionList = Projections.projectionList(); 
		projectionList.add(Projections.property("logParametrizacion.codigo") ,"codigoLog");
		projectionList.add(Projections.property("logParametrizacion.anioVigencia") ,"anioVigencia");
		projectionList.add(Projections.property("logParametrizacion.fechaModificacion") ,"fechaModificacion");
		projectionList.add(Projections.property("logParametrizacion.horaModificacion") ,"horaModificacion");
		projectionList.add(Projections.property("logParametrizacion.contratos") ,"contratos");
		projectionList.add(Projections.property("logParametrizacion.convenios") ,"convenios");
		projectionList.add(Projections.property("logParametrizacion.usuarios") ,"usuarios");
		projectionList.add(Projections.property("logParametrizacion.motivosModifiPresupuesto") ,"motivosModificacionPresupuesto");
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoLogParamPresupCap.class));
		
		ArrayList<DtoLogParamPresupCap> listaDtoLogs = (ArrayList<DtoLogParamPresupCap>)criteria.list();
		
		for (DtoLogParamPresupCap logParamPresupuestoCap : listaDtoLogs) {
			logParamPresupuestoCap.getConvenios().getNombre();
			logParamPresupuestoCap.getContratos().getNumeroContrato();
			logParamPresupuestoCap.getUsuarios().getPersonas().getPrimerNombre();
			logParamPresupuestoCap.getMotivosModificacionPresupuesto().getDescripcion();
		}
		return listaDtoLogs;
	}	
	
	/**
	 * Este m�todo modifica el Log para la parametrizaci�n del presupuesto
	 * 
	 * @param LogParamPresupuestoCap Log Parametrizaci�n Presupuesto
	 * @return LogParamPresupuestoCap modificado
	 */
	public LogParamPresupuestoCap modificarLogParametrizacionPresupuesto(LogParamPresupuestoCap log) {
		return super.merge(log);
	}	
	
	/**
	 * Este m�todo elimina el Log para la parametrizaci�n del presupuesto
	 * 
	 * @param c�digo Log a eliminar
	 * @return boolean con el resultado de la operaci�n de eliminado
	 */
	public void eliminarLogParametrizacionPresupuesto(LogParamPresupuestoCap log) {
		super.delete(log);
	}	
}
