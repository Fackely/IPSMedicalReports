package com.servinte.axioma.dao.interfaz.administracion;

import java.util.List;

import com.servinte.axioma.dto.administracion.DtoBarrio;
import com.servinte.axioma.orm.Barrios;

/**
 * Interface para los métodos de la clase Barrios
 * 
 * @author Ricardo Ruiz
 *
 */
public interface IBarriosDAO {
	
	/**
	 * Método que permite obetener el Barrio a partir de el codigoBarrio
	 * @param codigoBarrio
	 * @param codigoCiudad
	 * @param codigoDepartamento
	 * @param codigoPais
	 * @author Ricardo Ruiz
	 * @return
	 */
	public Barrios findByCodigoBarrio(String codigoBarrio, String codigoCiudad, String codigoDepartamento, String codigoPais);
	
	/**
	 * Lista todos los Barrios parametrizados en el sistema
	 * @return La lista de todos los barrios en la estructura DtoBarrio
	 * @author Ricardo Ruiz
	 */
	public List<DtoBarrio> listarAllBarrios();

}
