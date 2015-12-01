package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.orm.MotivosModifiPresupuesto;

public interface IMotivosModificacionPresupuestoMundo {
	
	/**
	 * @return lista con todos los motivos de modificacion de presupuestos
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultarTodosMotivosModificacion();
	
	/**
	 * Guardar y modificar los motivos de modificacion
	 * @param usuario
	 * @param lista
	 */
	public void guardarModificarMotivosModificacion(UsuarioBasico usuario,ArrayList<DtoMotivosModifiPresupuesto> lista);
	
	/**
	 * Metodo que se encarga de eliminar motivos de modificacion
	 * @param listaAEliminar
	 */
	public void eliminarMotivosModificacion(ArrayList<Long> listaAEliminar);
	
	/**
	 * @param codigo
	 * @param descripcion
	 * @param Activo
	 * @return motivos de modificacion consultados por filtro de busqueda
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultaFiltro(String codigo, String descripcion,Boolean Activo);
	
	/**
	 * @param motivosModifiPresupuesto
	 * @return Si puede eliminar o no el registro
	 */
	public Boolean puedeEliminar(MotivosModifiPresupuesto motivosModifiPresupuesto);

}
