package com.servinte.axioma.orm.delegate.capitacion;


import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.orm.MotivosModifiPresupuesto;
import com.servinte.axioma.orm.MotivosModifiPresupuestoHome;

public class MotivosModificacionPresupuestoDelegate extends MotivosModifiPresupuestoHome{

	/**
	 * @return Lista todos los motivos de modificaion que estan registrados en la base de datos
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultarTodosMotivos(){

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MotivosModifiPresupuesto.class,	"motivosModifiPresupuesto");

		criteria.createAlias("motivosModifiPresupuesto.instituciones"	, "instituciones");
		criteria.createAlias("motivosModifiPresupuesto.usuarios"		, "usuarios");
		
		//ODENAMIENTO POR CODIGO 
		criteria.addOrder(Order.asc("motivosModifiPresupuesto.codigo"));
		
		ProjectionList projectionList = Projections.projectionList(); 
			projectionList.add(Projections.property("motivosModifiPresupuesto.codigoPk")			,"codigoPk");
			projectionList.add(Projections.property("instituciones.codigo")							,"codInstitucion");
			projectionList.add(Projections.property("usuarios.login")								,"loginUsuario");
			projectionList.add(Projections.property("motivosModifiPresupuesto.codigo")				,"codigo");
			projectionList.add(Projections.property("motivosModifiPresupuesto.descripcion")			,"descripcion");
			projectionList.add(Projections.property("motivosModifiPresupuesto.activo")				,"activo");
			projectionList.add(Projections.property("motivosModifiPresupuesto.horaModifica")		,"horaModifica");
			projectionList.add(Projections.property("motivosModifiPresupuesto.fechaModifica")		,"fechaModifica");
		criteria.setProjection(projectionList);

		criteria.setResultTransformer( Transformers.aliasToBean(DtoMotivosModifiPresupuesto.class));
		
		@SuppressWarnings("unchecked")
		ArrayList<DtoMotivosModifiPresupuesto> res = (ArrayList<DtoMotivosModifiPresupuesto>) criteria.list();

		return res;
		
	}




	/**
	 * Metodo encargado de realizar consultas segun el filtro ingresado
	 * @param codigo
	 * @param descripcion
	 * @param Activo
	 * @return Lista con motivos de modificaion segun los filtros ingresdos por el usuario
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultarFiltro(String codigo, String descripcion,Boolean Activo){
		Criteria criteria = getSessionFactory().getCurrentSession()
		.createCriteria(MotivosModifiPresupuesto.class, "motivosModifiPresupuesto");

		
		
		criteria.createAlias("motivosModifiPresupuesto.instituciones"	, "instituciones");
		criteria.createAlias("motivosModifiPresupuesto.usuarios"		, "usuarios");
		
		
		
		
		//SI LA DESCRIPCION ES DIFERENTE DE NULL Y DE VACIO REALIZA LA BUSQUEDA
		if (descripcion!=null && !descripcion.equals("")) {
			criteria.add(Restrictions.like("motivosModifiPresupuesto.descripcion","%"+descripcion+"%"));
		}

		//SI EL CODIGO ES DIFERENTE DE NULL Y DE VACIO REALIZA LA BUSQUEDA
		if (codigo!=null && !codigo.equals("")) {
			criteria.add(Restrictions.eq("motivosModifiPresupuesto.codigo", codigo));
		}

		//VALIDACION DE ACTIVO O INACTIVO, EN LA BASE DE DATOS ESTA EN FORMA DE STRING S - N
		if (Activo!=null ) {
			if (Activo==true) {
				criteria.add(Restrictions.eq("motivosModifiPresupuesto.activo", "S"));
			}else{
				criteria.add(Restrictions.eq("motivosModifiPresupuesto.activo", "N"));
			}

		}
		
		ProjectionList projectionList = Projections.projectionList(); 
			projectionList.add(Projections.property("motivosModifiPresupuesto.codigoPk")					,"codigoPk");
			projectionList.add(Projections.property("instituciones.codigo")							,"codInstitucion");
			projectionList.add(Projections.property("usuarios.login")								,"loginUsuario");
			projectionList.add(Projections.property("motivosModifiPresupuesto.codigo")				,"codigo");
			projectionList.add(Projections.property("motivosModifiPresupuesto.descripcion")			,"descripcion");
			projectionList.add(Projections.property("motivosModifiPresupuesto.activo")				,"activo");
			projectionList.add(Projections.property("motivosModifiPresupuesto.horaModifica")		,"horaModifica");
			projectionList.add(Projections.property("motivosModifiPresupuesto.fechaModifica")		,"fechaModifica");
		criteria.setProjection(projectionList);
	
		criteria.setResultTransformer( Transformers.aliasToBean(DtoMotivosModifiPresupuesto.class));
		
		@SuppressWarnings("unchecked")
		ArrayList<DtoMotivosModifiPresupuesto> res = (ArrayList<DtoMotivosModifiPresupuesto>) criteria.list();

		return res;

	}



	/**
	 * 
	 * Metodo encargado de verificar 
	 * @param motivosModifiPresupuesto
	 * @return Si el motivo se puede eliminar o no
	 */
	public Boolean puedeEliminar(MotivosModifiPresupuesto motivosModifiPresupuesto){
		/**
		 * VALIADACION QUE HACE FALTA POR QUE DIEGO NO A CREADO LA TABLA
		 */

		return false;
	}


	/**
	 * Metodo encargado de eliminar motivos de modificacion de presupuesto
	 * @param elementoAEliminar
	 */
	public void eliminarMotivosModificacionPresupuesto(MotivosModifiPresupuesto elementoAEliminar){
		super.delete(elementoAEliminar);
	}

	/**
	 * Metodo que busca motivos de modificacion por ID
	 * @param id
	 * @return Motivo de modificaion
	 */
	public MotivosModifiPresupuesto buscarPorID(Number id){
		return super.findById((Long) id);
	}


	/**
	 * Metodo que se encarga de buscar o de modificar motivos de modificacion de presupuesto
	 * @param elementoAGuardar
	 */
	public void guardarActualizar(MotivosModifiPresupuesto elementoAGuardar){
		super.attachDirty(elementoAGuardar);
	}
}
