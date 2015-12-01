package com.servinte.axioma.orm.delegate.odontologia.contrato;

import java.util.ArrayList;

import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.PresuFirmasContrato;
import com.servinte.axioma.orm.PresuFirmasContratoHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class PresuFirmasContratoDelegate extends PresuFirmasContratoHome {

	
	/**
	 * Retorna las firmas de un presupuesto odontologico
	 * 
	 * @param codPresoOdonto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PresuFirmasContrato> obtenerFirmasPorPresuOdonto(long codPresoOdonto){
		
		return (ArrayList<PresuFirmasContrato>) sessionFactory.getCurrentSession()
			.createCriteria(PresuFirmasContrato.class, "presu_cotnra")
			
			.createAlias("presu_cotnra.presuContratoOdoImp"			, "presu_cont_odo_imp")
			.createAlias("presu_cont_odo_imp.presupuestoContratado"	, "presu_contra")
			.createAlias("presu_contra.presupuestoOdontologico"		, "presu_odonto")
			
			.add(Restrictions.eq("presu_odonto.codigoPk"			, codPresoOdonto))
			.addOrder(Property.forName("numero").asc())
			.list();
	}
	
	
}
