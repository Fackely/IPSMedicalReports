package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.orm.ViasIngresoHome;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad Vias de Ingreso
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
@SuppressWarnings("unchecked")
public class ViasIngresoDelegate extends ViasIngresoHome 
{
	
	/**
	 * 
	 * Este Método se encarga de buscar todas las vias 
	 * de ingreso registradas en el sistema
	 * 
	 * @return ArrayList<ViasIngreso>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ViasIngreso> buscarViasIngreso(){
		
		ArrayList<ViasIngreso> listaViasIngreso= (ArrayList<ViasIngreso>)sessionFactory.getCurrentSession()
			.createCriteria(ViasIngreso.class,"viaIngreso")
			.addOrder(Order.asc("viaIngreso.nombre"))
			.list();
		
		return listaViasIngreso;
	}
	
	
	/**
	 * Busca las vias de ingreso según parametros.
	 * @param parametros
	 * @return ArrayList<DTOEstanciaViaIngCentroCosto>
	 *
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DTOEstanciaViaIngCentroCosto> buscarVias(DTOEstanciaViaIngCentroCosto parametros)
	{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(ViasIngreso.class, "viasIngreso");
		
		if(parametros.getViaIngreso() != ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("viasIngreso.codigo", parametros.getViaIngreso()));
		}
		criteria.setProjection((Projections.projectionList()
			.add(Projections.property("viasIngreso.codigo"),"viaIngreso")
			.add(Projections.property("viasIngreso.nombre"),"nombreViaIngreso")));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTOEstanciaViaIngCentroCosto.class));
		
		ArrayList<DTOEstanciaViaIngCentroCosto> listaViasIngreso = (ArrayList<DTOEstanciaViaIngCentroCosto>)criteria.list();
		
		return listaViasIngreso;
	}
	

}
