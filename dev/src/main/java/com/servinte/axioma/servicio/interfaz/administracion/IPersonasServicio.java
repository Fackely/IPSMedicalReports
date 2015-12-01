package com.servinte.axioma.servicio.interfaz.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.orm.Personas;

/**
 * 
 * @author axioma
 *
 */
public interface IPersonasServicio {

	/**
	 * Buscar una persona por el tipo y número de identificación
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return {@link DtoPersonas} Cons los datos de la persona buscada, null en caso de no existir
	 */
	public DtoPersonas buscarPersona(String tipoIdentificacion, String numeroIdentificacion);

	/**
	 * Buscar una persona por el tipo y número de identificación
	 * @param dtoPersona {@link DtoPersonas}
	 * @return {@link DtoPersonas} Cons los datos de la persona buscada, null en caso de no existir
	 */
	public DtoPersonas buscarPersona(DtoPersonas dtoPersona);
	
	
	/**
	 * Buscar personas por id
	 * @param codigoPkPersona
	 * @return
	 */
	public Personas buscarPersonasPorId(int codigoPkPersona);
	
	
	/**
	 * attachDirty
	 * @param instance
	 */
	public void attachDirty(Personas instance);
	
	/**
	 * 
	 * @param dtoPersona
	 * @return
	 * @author Camilo Gomez
	 */
	public ArrayList<DtoPersonas>buscarPersonasPorNombresApellidos(DtoPersonas dtoPersona);
	
}
