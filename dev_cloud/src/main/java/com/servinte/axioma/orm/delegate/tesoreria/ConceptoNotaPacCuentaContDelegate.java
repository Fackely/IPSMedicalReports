package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.orm.ConcNotaPacCuentaCont;
import com.servinte.axioma.orm.ConcNotaPacCuentaContHome;

/**
 * Clase encargada de ejecutar las transacciones del objeto 
 * {@link ConcNotaPacCuentaCont} de la relación conc_nota_pac_cuenta_cont
 * @author diecorqu
 *  
 */
public class ConceptoNotaPacCuentaContDelegate extends
		ConcNotaPacCuentaContHome {

	/**
	 * Método encargado de persistir la entidad ConcNotaPacCuentaCont
	 * @param conceptoNotaPacCuentaCont
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarConceptoNotaPacCuentaCont(ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		boolean save = false;
		try{
			super.persist(conceptoNotaPacCuentaCont);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el concepto con código: " + 
					conceptoNotaPacCuentaCont.getCodigo(),e);
		}		
		return save;
	}

	/**
	 * Método encargado de eliminar un registro de ConcNotaPacCuentaCont
	 * @param conceptoNotaPacCuentaCont
	 */
	public void eliminarConceptoNotaPacCuentaCont(ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		try{
			super.delete(conceptoNotaPacCuentaCont);
		}catch (Exception e) {
			Log4JManager.error("No se pudo eliminar el concepto con código: " + 
					conceptoNotaPacCuentaCont.getCodigo(),e);
		}	
	}

	/**
	 * Método encargado de modificar la entidad ConcNotaPacCuentaCont
	 * @param conceptoNotaPaciente
	 * @return ConcNotaPacCuentaCont
	 */
	public ConcNotaPacCuentaCont modificarConceptoNotaPacCuentaCont(
			ConcNotaPacCuentaCont conceptoNotaPacCuentaCont) {
		ConcNotaPacCuentaCont concepto = null;
		try {
			concepto = super.merge(conceptoNotaPacCuentaCont);
		} catch (Exception e) {
			Log4JManager.error("No se pudo modificar el concepto con código: " + 
					conceptoNotaPacCuentaCont.getCodigo(),e);
			throw new RuntimeException(">>>>>>>>>>>>>>>>>>>>>>HibernateException<<<<<<<<<<<<<<<<<<<<<<<<");
		}
		return concepto;
	}

	/**
	 * Método encargado de buscar un ConcNotaPacCuentaCont por codigo
	 * @param codigo
	 * @return ConcNotaPacCuentaCont
	 */
	public ConcNotaPacCuentaCont findById(long codigo) {
		return super.findById(codigo);
	}

	/**
	 * Método encargado de retornar una lista de ConcNotaPacCuentaCont con los 
	 * Cuentas Conbles Asociadas a un Concepto de Notas de Paciente
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public ArrayList<DtoConcNotaPacCuentaCont> listarConceptoNotaPacCuentaCont(long codigoConceptoNotas) {
		ArrayList<DtoConcNotaPacCuentaCont> dtoConcNotaPacCuentaCont = new ArrayList<DtoConcNotaPacCuentaCont>(); 
		Criteria criteria = sessionFactory.getCurrentSession()
								.createCriteria(ConcNotaPacCuentaCont.class, "conceptoNotaPacCuentaCont").
								createAlias("conceptoNotaPacCuentaCont.conceptoNotaPaciente", "conceptoNotaPaciente").
								add(Restrictions.eq("conceptoNotaPaciente.codigopk", codigoConceptoNotas)).
								addOrder(Property.forName("conceptoNotaPacCuentaCont.codigo").asc());
		
		ProjectionList projectionList = Projections.projectionList(); 
		projectionList.add(Projections.property("conceptoNotaPacCuentaCont.codigo") ,"codigo");
		projectionList.add(Projections.property("conceptoNotaPacCuentaCont.cuentasContables") ,"cuentasContables");
		projectionList.add(Projections.property("conceptoNotaPacCuentaCont.instituciones") ,"instituciones");
		projectionList.add(Projections.property("conceptoNotaPacCuentaCont.conceptoNotaPaciente") ,"conceptoNotaPaciente");
		projectionList.add(Projections.property("conceptoNotaPacCuentaCont.empresasInstitucion") ,"empresasInstitucion");
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConcNotaPacCuentaCont.class));
		
		dtoConcNotaPacCuentaCont = (ArrayList<DtoConcNotaPacCuentaCont>)criteria.list();
		
		for (DtoConcNotaPacCuentaCont concNotaPacCuentaCont2 : dtoConcNotaPacCuentaCont) {
			concNotaPacCuentaCont2.getCuentasContables().getAnioVigencia();
		}
		return dtoConcNotaPacCuentaCont; 
	}
}
