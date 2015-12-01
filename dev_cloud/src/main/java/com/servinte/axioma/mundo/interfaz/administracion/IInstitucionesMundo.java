package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.impl.administracion.UsuariosMundo;
import com.servinte.axioma.orm.Instituciones;

/**
 * Define el comportamiento
 * 
 * @see UsuariosMundo
 * @author Cristhian Murillo
 */
public interface IInstitucionesMundo {
	
	
	/**
	 * Retorna la Institucion
	 * 
	 * @param codigo
	 * @return {@link Instituciones}
	 *
	 */
	public Instituciones buscarPorCodigo(int codigo);
	
	/**
	 * @return  lista de Instituciones
	 */
	public ArrayList<Instituciones> listarInstituciones();
}
