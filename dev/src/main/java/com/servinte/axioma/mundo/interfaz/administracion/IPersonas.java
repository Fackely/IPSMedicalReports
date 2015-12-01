package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.orm.Personas;

public interface IPersonas {
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
	 * Buscar una persona por el Id
	 * @param dtoPersona {@link DtoPersonas}
	 * @return {@link DtoPersonas} Cons los datos de la persona buscada, null en caso de no existir
	 */
	public DtoPersonas buscarPersonaPorCodigo(int codigoPkPersona);
	
	/**
	 * Inserta una persona en la Base de Datos
	 * @param dtoPersona {@link DtoPersonas}
	 */
	public Personas attachDirty(DtoPersonas dtoPersona);

	/**
	 * Metodo para buscar una persona por la identificacion y el tipo de identificacion
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return
	 */
	public Personas buscarPersonasCompleto(String identificacionBuscar, String acronimoTipoIdentificacion);

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
	public ArrayList<DtoPersonas> buscarPersonasPorNombresApellidos(DtoPersonas dtoPersona);
	
	
	/**
	 * Busca las personas de acuerdo a tipo de identificación, número de identificación
	 * primer nombre, segundo nombre, primer apellido, segundo apellido
	 * @param dtoPersona
	 * @return
	 * @author Ricardo Ruiz
	 */
	public List<DtoPersonas> buscarPersonasPorFiltro(DtoPersonas filtro);
	
	
}
