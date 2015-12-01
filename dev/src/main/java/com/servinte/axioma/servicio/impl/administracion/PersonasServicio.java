package com.servinte.axioma.servicio.impl.administracion;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.servicio.interfaz.administracion.IPersonasServicio;

/**
 * 
 * @author axioma
 *
 */
public class PersonasServicio  implements IPersonasServicio{
	
	
	/**
	 * 
	 */
	public PersonasServicio(){
	}
	
	@Override
	public DtoPersonas buscarPersona(String tipoIdentificacion, String numeroIdentificacion) {
		return AdministracionFabricaMundo.crearPersonasMundo().buscarPersona(tipoIdentificacion, numeroIdentificacion);
	}

	@Override
	public DtoPersonas buscarPersona(DtoPersonas dtoPersona) {
		return AdministracionFabricaMundo.crearPersonasMundo().buscarPersona(dtoPersona);
	}

	@Override
	public Personas buscarPersonasPorId(int codigoPkPersona) {
		return AdministracionFabricaMundo.crearPersonasMundo().buscarPersonasPorId(codigoPkPersona);
	}

	
	@Override
	public void attachDirty(Personas instance) {
		AdministracionFabricaMundo.crearPersonasMundo().attachDirty(instance);
	}
	
	/**
	 * @author Camilo Gomez
	 */
	public ArrayList<DtoPersonas>buscarPersonasPorNombresApellidos(DtoPersonas dtoPersona){
		return AdministracionFabricaMundo.crearPersonasMundo().buscarPersonasPorNombresApellidos(dtoPersona);
	}
	
}
