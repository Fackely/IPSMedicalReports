package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO;
import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.orm.MotivosModifiPresupuesto;
import com.servinte.axioma.orm.delegate.capitacion.MotivosModificacionPresupuestoDelegate;


public class MotivosModificacionPresupuestoDAO implements IMotivosModificacionPresupuestoDAO{
	
	/**
	 *Delegate con metodos de perisistencia 
	 */
	private MotivosModificacionPresupuestoDelegate motivosModificacionPresupuestoDelegate = new  MotivosModificacionPresupuestoDelegate();
	
	/**
	 *Constructor 
	 */
	public MotivosModificacionPresupuestoDAO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO#insertar(com.servinte.axioma.orm.MotivosModifiPresupuesto)
	 */
	@Override
	public void insertar(MotivosModifiPresupuesto objeto) {
		motivosModificacionPresupuestoDelegate.guardarActualizar(objeto);
		
	}

	/**
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO#modificar(com.servinte.axioma.orm.MotivosModifiPresupuesto)
	 */
	@Override
	public void modificar(MotivosModifiPresupuesto objeto) {
		motivosModificacionPresupuestoDelegate.guardarActualizar(objeto);
		
	}

	/**
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO#eliminar(com.servinte.axioma.orm.MotivosModifiPresupuesto)
	 */
	@Override
	public void eliminar(MotivosModifiPresupuesto objeto) {
		motivosModificacionPresupuestoDelegate.eliminarMotivosModificacionPresupuesto(objeto);
		
	}

	/**
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO#buscarxId(java.lang.Number)
	 */
	@Override
	public MotivosModifiPresupuesto buscarxId(Number id) {
		// TODO Auto-generated method stub
		return motivosModificacionPresupuestoDelegate.buscarPorID(id);
	}
	

	/**
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO#consultarTodosMotivosModificacion()
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultarTodosMotivosModificacion(){
		return motivosModificacionPresupuestoDelegate.consultarTodosMotivos();
	}
	
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO#consultaFiltro(java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultaFiltro(String codigo, String descripcion,Boolean Activo){
		return motivosModificacionPresupuestoDelegate.consultarFiltro(codigo, descripcion, Activo);
	}
	
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO#puedeEliminar(com.servinte.axioma.orm.MotivosModifiPresupuesto)
	 */
	public Boolean puedeEliminar(MotivosModifiPresupuesto motivosModifiPresupuesto){
		return motivosModificacionPresupuestoDelegate.puedeEliminar(motivosModifiPresupuesto);
	}

}
