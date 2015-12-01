package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.orm.MotivosModifiPresupuesto;

public interface IMotivosModificacionPresupuestoDAO extends IBaseDAO <MotivosModifiPresupuesto> {
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#insertar(java.lang.Object)
	 */
	public void insertar(MotivosModifiPresupuesto objeto) ;

	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#modificar(java.lang.Object)
	 */
	public void modificar(MotivosModifiPresupuesto objeto);

	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#eliminar(java.lang.Object)
	 */
	public void eliminar(MotivosModifiPresupuesto objeto);

	
	/**
	 * @see com.servinte.axioma.dao.interfaz.IBaseDAO#buscarxId(java.lang.Number)
	 */
	public MotivosModifiPresupuesto buscarxId(Number id) ;
	

	/**
	 * @return lista con todos los motivos de modificacion de presupuesto
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultarTodosMotivosModificacion();
	
	/**
	 * @param codigo
	 * @param descripcion
	 * @param Activo
	 * @return lista de motivos de modificacion segun los filtros seleccionados
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultaFiltro(String codigo, String descripcion,Boolean Activo);
	
	/**
	 * @param motivosModifiPresupuesto
	 * @return Si se puede eliminar o no el motivo de modificacion seleccionado
	 */
	public Boolean puedeEliminar(MotivosModifiPresupuesto motivosModifiPresupuesto);
}
