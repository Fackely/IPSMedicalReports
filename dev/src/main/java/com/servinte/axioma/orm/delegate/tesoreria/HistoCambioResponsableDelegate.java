package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.tesoreria.DTOHistoCambioResponsable;
import com.servinte.axioma.orm.HistoCambioResponsable;
import com.servinte.axioma.orm.HistoCambioResponsableHome;

/**
 * Esta clase se encarga de ejecutar los procesos
 * de negocio de la entidad Historial Cambio Responsable de
 * un registro Faltante Sobrante  
 * 
 * @author Angela Maria Aguirre
 * @since 19/07/2010
 */
public class HistoCambioResponsableDelegate extends HistoCambioResponsableHome {
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de registrar un historial de 
	 * cambio de responsable para un registro faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return Boolean
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Boolean registrarHistorialFaltanteSobrante(HistoCambioResponsable dto){
		
		boolean save = false;
		try{
			super.persist(dto);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el historial de faltante sobrante: ",e);
		}		
		return save;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar el historial del  cambio de responsable
	 * del registro de faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return ArrayList<DTOHistoCambioResponsable> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOHistoCambioResponsable> consultarHistorialPorDetalleFaltanteSobranteID(DTOHistoCambioResponsable dto){		
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(HistoCambioResponsable.class,"historial")
		.createAlias("historial.detFaltanteSobrante", "detFaltanteSobrante")
		.createAlias("historial.usuariosByResponsable", "usuariosByResponsable")
		.createAlias("historial.usuariosByUsuarioModifica", "usuariosByUsuarioModifica")
		.createAlias("historial.centroAtencion", "centroAtencion")
		.add(Restrictions.eq("detFaltanteSobrante.codigoPk", dto.getDetFaltanteSobrante().getCodigoPk()))
		.setProjection(Projections.projectionList()
				.add(Projections.property("historial.fechaModifica"),"fechaModifica")
				.add(Projections.property("historial.horaModifica"),"horaModifica")				
				.add(Projections.property("historial.motivo"),"motivo")
				.add(Projections.property("usuariosByUsuarioModifica.login"),"loginUsuarioModifica")
				.add(Projections.property("usuariosByResponsable.login"),"loginUsuarioResponsable")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencionDescripcion"))
		.addOrder(Order.desc("historial.fechaModifica"))
		.addOrder(Order.desc("historial.horaModifica"))
		.setResultTransformer(Transformers.aliasToBean(DTOHistoCambioResponsable.class));		
		ArrayList<DTOHistoCambioResponsable> lista=(ArrayList)criteria.list();	
		
		return lista;		
		
	}
}
