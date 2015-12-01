package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IPersonasDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IPersonas;
import com.servinte.axioma.orm.Personas;

public class PersonasMundo implements IPersonas {

	IPersonasDAO daoPersona;
	
	public PersonasMundo(){
		daoPersona=AdministracionFabricaDAO.crearPersonasDao();
	}
	
	@Override
	public DtoPersonas buscarPersona(String tipoIdentificacion, String numeroIdentificacion)
	{
		return daoPersona.buscarPersona(tipoIdentificacion, numeroIdentificacion);
	}

	@Override
	public DtoPersonas buscarPersona(DtoPersonas dtoPersona) {
		return daoPersona.buscarPersona(dtoPersona);
	}

	@Override
	public Personas attachDirty(DtoPersonas dtoPersona)
	{
		return daoPersona.attachDirty(dtoPersona);
	}
	
	@Override
	public Personas buscarPersonasCompleto(String identificacionBuscar,
			String acronimoTipoIdentificacion) {
		
		Personas personas= new Personas();
		
		try {
			personas=daoPersona.buscarPersonasCompleto(identificacionBuscar, acronimoTipoIdentificacion);
			
		} catch (Exception e) {
			Log4JManager.error(e);
		}
		
		return personas;
	}

	@Override
	public Personas buscarPersonasPorId(int codigoPkPersona) {
		return daoPersona.buscarPersonasPorId(codigoPkPersona);
	}

	
	@Override
	public void attachDirty(Personas instance) {
		daoPersona.attachDirty(instance);	
	}
	
	/**
	 * @author Camilo Gomez
	 */
	public ArrayList<DtoPersonas>buscarPersonasPorNombresApellidos(DtoPersonas dtoPersona){
		return daoPersona.buscarPersonasPorNombresApellidos(dtoPersona);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.IPersonas#buscarPersonasPorFiltro(com.princetonsa.dto.administracion.DtoPersonas)
	 */
	@Override
	public List<DtoPersonas> buscarPersonasPorFiltro(DtoPersonas filtro) {
		return daoPersona.buscarPersonasPorFiltro(filtro);
	}
	
	@Override
	public DtoPersonas buscarPersonaPorCodigo(int codigoPkPersona) {
		daoPersona=AdministracionFabricaDAO.crearPersonasDao();
		return daoPersona.buscarPersonaPorCodigo(codigoPkPersona);
	}
}
