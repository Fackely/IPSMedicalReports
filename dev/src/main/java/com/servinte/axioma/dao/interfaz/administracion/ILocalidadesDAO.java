package com.servinte.axioma.dao.interfaz.administracion;

import java.util.List;

import com.servinte.axioma.dto.administracion.DtoLocalidad;

/**
 * Interface para los métodos de la clase Localidades
 * 
 * @author Ricardo Ruiz
 *
 */
public interface ILocalidadesDAO {

	/**
	 * Lista todas las Localidades parametrizadas en el sistema
	 * @return La lista de todas las localidades en la estructura DtoLocalidad
	 * @author Ricardo Ruiz
	 */
	public List<DtoLocalidad> listarAllLocalidades();
}
