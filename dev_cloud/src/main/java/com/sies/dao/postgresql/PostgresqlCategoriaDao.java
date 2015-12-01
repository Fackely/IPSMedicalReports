/*
 * Created on 4/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.sies.dao.sqlbase.SqlBaseCategoriaDao;
import com.sies.dao.CategoriaDao;

/**
 * @author karenth
 * Modificado por Juan David Ramírez L.
 */
public class PostgresqlCategoriaDao implements CategoriaDao{
	
	public int insertarCategoria(Connection con, String nombre, String descripcion, boolean activo, int centroCosto, String color) {
		return  SqlBaseCategoriaDao.insertarCategoria(con,nombre, descripcion, activo, centroCosto, color);
	}
		
	public  Collection<HashMap<String, Object>> categoriaUtilizada(Connection con, String nombre)
	{
		return SqlBaseCategoriaDao.categoriaUtilizada(con, nombre);
	}
	
	
	public Collection<HashMap<String, Object>> consultarCategorias(Connection con, boolean buscarActivas, Integer centroCosto)
	{
		return SqlBaseCategoriaDao.consultarCategorias(con, buscarActivas, centroCosto);
	}
	
	public ResultSet consultarModificar(Connection con, int codigo)
	{
		return SqlBaseCategoriaDao.consultarModificar(con, codigo);
	}
	
	public void modificar(Connection con, int codigo, String nombre,String descripcion, boolean activo, int centroCosto, String color)
	{
		SqlBaseCategoriaDao.modificar(con, codigo, nombre,descripcion, activo, centroCosto, color);
	}
	
	
	public int eliminarCategoria(Connection con, int codigo)
	{
		return SqlBaseCategoriaDao.eliminarCategoria(con, codigo);
	}


	public Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigo, boolean ordenar)
	{
		return SqlBaseCategoriaDao.consultarEnfermeraCategoria(con, codigo, ordenar);
	}

	/**
	 * Metodo que consulta las categorias que tiene asociadas una enfermera;
	 */
	public Collection<HashMap<String, Object>> consultarCategoriasEnfermera(Connection con, int codigo_enfermera)
	{
		return SqlBaseCategoriaDao.consultarCategoriasEnfermera(con, codigo_enfermera);
	}
	
	public ResultSet consultarEnfermera(Connection con, int codigo)
	{
	    return SqlBaseCategoriaDao.consultarEnfermera(con, codigo);
	}

	

	public int insertarEnfermeraCategoria(Connection con, int codigoPersona, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return SqlBaseCategoriaDao.insertarEnfermeraCategoria(con, codigoPersona, codigoCategoria, fechaInicio, fechaFin);
	}
	
	
	public int actualizarEnfermeraCategoria(Connection con, int codigoPersona, String fechaInicio, String fechaFin, int codigoCategoria, boolean eliminarTurnos, Integer codigoCategoriaOrigen)
	{
		return SqlBaseCategoriaDao.actualizarEnfermeraCategoria(con, codigoPersona, fechaInicio, fechaFin, codigoCategoria, eliminarTurnos, codigoCategoriaOrigen);
	}
	
	

	
	/*****************Asignar restricciones a Categoria*****************/
	
	
	/**
	 * Metodo que permite la consulta de las restricciones que se encuentran asociadas una 
	 * determinada categoria
	 */
	public Collection<HashMap<String, Object>> consultarRestriccionCategoria(Connection con, int codigo)
	{
		return SqlBaseCategoriaDao.consultarRestriccionCategoria(con, codigo);
	}
	
	
	/**
	 * Método que elimina todos los registros de la tabla categoria_restriccion
	 */
	public int eliminarTabla(Connection con)
	{
	    return SqlBaseCategoriaDao.eliminarTabla(con);
	}
	
	/**
	 * Método que actualiza la fecha de finalización de la asociación de una restriccion dada,
	 * a una categoria dada 
	 */
	public int actualizarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoCategoria)
	{
	    return SqlBaseCategoriaDao.actualizarRestriccionCategoria(con, codigoRestriccion, codigoCategoria);
	}
	
	
	
	/**
	 * Metodo que inserta los registros en la tabla de categoria_restriccion
	 */
	public int insertarRestriccionCategoria(Connection con, int codigoRestriccion, int codigoCategoria, int valor)
	{
	    return SqlBaseCategoriaDao.insertarRestriccionCategoria(con,codigoRestriccion,codigoCategoria, valor);
	}
	
	/**
	 * Metodo que retorna true y el nombre de la categoria si una enfermera esta o no asociada a una Categoria
	 * en caso de que no, retorna false
	 */
	public ResultadoBoolean estaEnfermeraEnCategoria(Connection con, int codigoProfesional)
	{
		return SqlBaseCategoriaDao.estaEnfermeraEnCategoria(con, codigoProfesional);
	}
	
	/**
	 * Metodo que retorna un Collection los datos de las enfermeras que cumplen con las
	 * condificiones de una búsqueda avanzada
	 * @param con
	 * @param codigo
	 * @param nombreProfesional
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermerasAvanzada(Connection con, int codigo, String nombreProfesional, int codigoCategoria)
	{
		return SqlBaseCategoriaDao.consultarEnfermerasAvanzada(con, codigo, nombreProfesional, codigoCategoria);
	}
	
	/**
	 * Consulta las personas asociadas a una categoria
	 * @param con
	 * @return Listado de personas
	 */
	public Collection<HashMap<String, Object>> consultarPersonas(Connection con, Integer codigoCategoria, Integer codigoCentroAtencion)
	{
		return SqlBaseCategoriaDao.consultarPersonas(con, codigoCategoria, codigoCentroAtencion);
	}

	/**
	 * Método que indica si existe un cuadro de turnos destino
	 * para mover los turnos de las personas
	 * @param connection
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public String existeCuadroDestino(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return SqlBaseCategoriaDao.existeCuadroDestino(con, codigoCategoria, fechaInicio, fechaFin);
	}
	
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
	public boolean existenTurnosGenerados(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int codigoPersona)
	{
		return SqlBaseCategoriaDao.existenTurnosGenerados(con, codigoCategoria, fechaInicio, fechaFin, codigoPersona);
	}

	/**
	 * Método para actualizar la fecha de finalización de una asignación de persona
	 * @param con conexión con la BD
	 * @param codigoPersona Código de la persona que se desea actualizar
	 * @param codigoCategoria Código de la categoría
	 * @param fechaFinAsociacion fecha actual de finalización
	 * @param limite Fecha a la cual se desea actualizar la fecha fin
	 * @return número de registros actualizados en la BD
	 */
	public int actualizarFechaFin(Connection con, int codigoPersona, int codigoCategoria, String fechaFinAsociacion, String limite)
	{
		return SqlBaseCategoriaDao.actualizarFechaFin(con, codigoPersona, codigoCategoria, fechaFinAsociacion, limite);
	}

	/**
	 * Método para consultar las categorías en las cuales se puede hacer un
	 * cubrimiento de turno
	 * @param con
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCategoriasDestino(Connection con, String fecha)
	{
		return SqlBaseCategoriaDao.consultarCategoriasDestino(con, fecha);
	}

	@Override
	public Collection<HashMap<String, Object>> consultar(Connection con, int codigoCategoria)
	{
		return SqlBaseCategoriaDao.consultar(con, codigoCategoria);
	}

}
