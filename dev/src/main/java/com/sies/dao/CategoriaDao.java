/*
 * Created on 4/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao;



import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;
/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CategoriaDao {
	
    /**
     * Metodo que inserta una nueva categoria al sistema
     * @param con
     * @param nombre
     * @param descripcion
     * @param activo
     * @param centroCosto
     * @param color
     * @return
     */
	public int insertarCategoria (Connection con, String nombre, String descripcion, boolean activo, int centroCosto, String color);
	
	/**
	 * Metodo que consulta si el nombre que ingresa ya esta siendo utilizado en la BD
	 * @param con
	 * @param nombre
	 * @return
	 */
	public Collection<HashMap<String, Object>> categoriaUtilizada(Connection con, String nombre);
	
	/**
	 * Metodo que actualiza los datos despues de que se hace una modificación
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param descripcion
	 * @param centroCosto
	 * @param color
	 */
	public void modificar(Connection con, int codigo, String nombre, String descripcion, boolean activo, int centroCosto, String color);
	
	
	/**
	 * Metodo que hace una consulta de todas las categoria que hay en la base de datos
	 * @param con
	 * @param buscarActivas
	 * @param centroCosto
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCategorias(Connection con, boolean buscarActivas, Integer centroCosto);
	
	/**
	 * Metodo que consulta la información de una sola categoría, cuando esta va a 
	 * ser modificada
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultarModificar(Connection con, int codigo);
	
	/**
	 * Metodo que inactiva o activa una categoría, true para activar, false para inactivar
	 * @param con
	 * @param codigo
	 * @param activo
	 * @return
	 */
	public int eliminarCategoria(Connection con, int codigo);
	
	
	/**
	 * Metodo que consulta las enfermeras que se encuentran asignadas en el momento 
	 * a determinada categoría
	 * @param con
	 * @param codigo
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigo, boolean ordenar);
	
	/**
	 * Metodo que consulta las categorías que tiene asociadas una enfermera;
	 * @param con
	 * @param codigo_enfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCategoriasEnfermera(Connection con, int codigo_enfermera);
	
	/**
	 * Metodo que retorna el nombre de la enfermera
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultarEnfermera(Connection con, int codigo);
	
	/**
	 * Metodo que inserta una nueva asignación de personas a categoría
	 * en la tabla categoria_enfermera
	 * @param con
	 * @param codigoPersona
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public int insertarEnfermeraCategoria(Connection con, int codigoPersona,int codigoCategoria, String fechaInicio, String fechaFin);
	
	/**
	 * Metodo que actualiza en campo de fecha de finalización con la fecha actual
	 * de la asignación, cuando una enfermera es asignada a otra categoria
	 * @param con
	 * @param codigoPersona
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @param eliminarTurnos
	 * @param codigoCategoriaOrigen
	 * @return
	 */
	public int actualizarEnfermeraCategoria(Connection con, int codigoPersona, String fechaInicio, String fechaFin, int codigoCategoria, boolean eliminarTurnos, Integer codigoCategoriaOrigen);
	
	
	
	
	
	
	/*****************************Asignar*************************************/
	
	
	
	
	/**
	 * Metodo que consulta las restricciones que se encuentran asignadas en el momento 
	 * a determinada categoria
	 * @param con
	 * @param codigo
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarRestriccionCategoria(Connection con, int codigo);
	
	
	
	
	/**
	 * Metodo que elimina los datos que hay en la tabla categoria_restriccion
	 * @param con
	 * @return
	 */
	public int eliminarTabla (Connection con);
	
	
	/**
	 * Metodo que inserta los registros en la tabla categoria_restriccion
	 * @return
	 */
	public int insertarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoCategoria, int valor);
	
	/**
	 * Metodo que actualiza a la fecha actual la fecha de finalización de la
	 * asociacion de la restriccion a la categoria
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoCategoria
	 * @return
	 */
	public int actualizarRestriccionCategoria(Connection con,  int codigoRestriccion, int codigoCategoria);
	
	/**
	 * Metodo que retorna true y el nombre de la categoria si una enfermera esta o no asociada a una Categoria
	 * en caso de que no, retorna false
	 * @param con
	 * @param codigoProfesional
	 * @return
	 */
	public ResultadoBoolean estaEnfermeraEnCategoria(Connection con, int codigoProfesional);
	
	
	/**
	 * Metodo que retorna un Collection los datos de las enfermeras que cumplen con las
	 * condificiones de una búsqueda avanzada
	 * @param con
	 * @param codigo
	 * @param nombreProfesional
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermerasAvanzada(Connection con, int codigo, String nombreProfesional, int codigoCategoria);
	
	/**
	 * Consulta las personas asociadas a una categoria
	 * @param con
	 * @param codigoCategoria
	 * @param codigoCentroAtencion 
	 * @return Listado de personas
	 */
	public Collection<HashMap<String, Object>> consultarPersonas(Connection con, Integer codigoCategoria, Integer codigoCentroAtencion);

	/**
	 * Método que indica si existe un cuadro de turnos destino
	 * para mover los turnos de las personas
	 * @param connection
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public String existeCuadroDestino(Connection con, int codigoCategoria, String fechaInicio, String fechaFin);

	/**
	 * (Juan David)
	 * Método para consultar si existen turnos generados al momento de cancelar
	 * una sociación
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoPersona
	 * @return
	 */
	public boolean existenTurnosGenerados(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int codigoPersona);

	/**
	 * Método para actualizar la fecha de finalización de una asignación de persona
	 * @param con conexión con la BD
	 * @param codigoPersona Código de la persona que se desea actualizar
	 * @param codigoCategoria Código de la categoría
	 * @param fechaFinAsociacion fecha actual de finalización
	 * @param limite Fecha a la cual se desea actualizar la fecha fin
	 * @return número de registros actualizados en la BD
	 */
	public int actualizarFechaFin(Connection con, int codigoPersona, int codigoCategoria, String fechaFinAsociacion, String limite);

	/**
	 * Método para consultar las categorías en las cuales se puede hacer un
	 * cubrimiento de turno
	 * @param con
	 * @param fecha
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCategoriasDestino(Connection con, String fecha);

	/**
	 * Método para consultar el nombre de una categoría dado esu código
	 * @param con Conexión con la base de datos
	 * @param codigoCategoria Código de la categoría a consultar
	 */
	public Collection<HashMap<String, Object>> consultar(Connection con, int codigoCategoria); 

}
