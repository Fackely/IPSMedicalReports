package com.servinte.axioma.mundo.interfaz.administracion;

import com.servinte.axioma.orm.Barrios;

/**
 * Interface para los métodos de la clase Barrios
 * 
 * @author Ricardo Ruiz
 *
 */
public interface IBarriosMundo {
	
	/**
	 * Método que permite obetener el Barrio a partir de el codigoBarrio
	 * @param codigoBarrio
	 * @param codigoCiudad
	 * @param codigoDepartamento
	 * @param codigoPais
	 * @return
	 */
	public Barrios findByCodigoBarrio(String codigoBarrio, String codigoCiudad, String codigoDepartamento, String codigoPais);

}
