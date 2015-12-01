package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposAfiliado;

/**
 * Esta clase se encarga de definir los métodos de 
 * negocio para la entidad  Tipos de Afiliado
 * 
 * @author Angela Maria Aguirre
 * @since 30/08/2010
 */
public interface ITiposAfiliadoDAO {
	
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de 
	 * afiliados registrados en el sistema
	 * 
	 * @return ArrayList<TiposAfiliado>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposAfiliado> obtenerTiposAfiliado();

	/**
	 * Metodo que se encarga de obtener la descripcion del
	 * acronimo de tipo Afiliado
	 * 
	 * @param tipoAfiliado
	 * @return String
	 */
	public String obtenerDescripcionTipoAfiliado(Character tipoAfiliado);
}
