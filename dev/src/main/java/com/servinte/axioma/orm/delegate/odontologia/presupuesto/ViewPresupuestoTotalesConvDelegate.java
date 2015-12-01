package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoViewPresupuesTotalesConv;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConv;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConvHome;


/**
 * 
 * Esta clase se encarga de
 *
 * @author Yennifer Guerrero
 * @since  07/09/2010
 *
 */
public class ViewPresupuestoTotalesConvDelegate  extends ViewPresupuestoTotalesConvHome{
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener todos los
	 * datos sobre los presupuestos contratados.
	 * @param dto
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	@SuppressWarnings("unchecked")
	public List<ViewPresupuestoTotalesConv> obtenerViewPresupuesto(DtoViewPresupuesTotalesConv dto){
		
		Criteria criteria= sessionFactory.getCurrentSession()
		.createCriteria(ViewPresupuestoTotalesConv.class);
		
		if(dto.getCodigoPresupuesto()>ConstantesBD.codigoNuncaValidoLong)
		{
			criteria.add(Restrictions.eq("id.presupuesto", dto.getCodigoPresupuesto()));
		}
		return criteria.list();
	}
	
	public static void main(String[] args) {
		ViewPresupuestoTotalesConvDelegate x = new ViewPresupuestoTotalesConvDelegate();
		DtoViewPresupuesTotalesConv dto = new DtoViewPresupuesTotalesConv();
		dto.setCodigoPresupuesto(11);
		
		x.obtenerViewPresupuesto(dto);
	}

}
