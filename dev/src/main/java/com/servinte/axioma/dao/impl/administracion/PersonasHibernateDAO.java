package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import util.ConstantesBD;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.dao.interfaz.administracion.IPersonasDAO;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.TiposPersonas;
import com.sies.hibernate.delegate.PersonasDelegate;

/**
 * Acceso a la base de datos a traves de Hibernate para
 * la entidad de personas
 * @author Juan David Ramírez
 * @since 20 Septiembre 2010
 * @version 1.0.0
 */
public class PersonasHibernateDAO implements IPersonasDAO 
{

	PersonasDelegate personas;
	
	
	/**
	 * Constructor de la clase
	 */
	public PersonasHibernateDAO() {
		personas=new PersonasDelegate();
	}
	
	
	
	@Override
	public DtoPersonas buscarPersona(String tipoIdentificacion,	String numeroIdentificacion) 
	{
		DtoPersonas persona=new DtoPersonas();
		persona.setTipoIdentificacion(tipoIdentificacion);
		persona.setNumeroIdentificacion(numeroIdentificacion);
		return personas.buscarPersona(persona);
	}

	
	@Override
	public DtoPersonas buscarPersona(DtoPersonas dtoPersona) {
		return personas.buscarPersona(dtoPersona);
	}
	
	@Override
	public DtoPersonas buscarPersonaPorCodigo(int codigoPkPersona) {
		return personas.buscarPersonaPorCodigo(codigoPkPersona);
	}
	
	/**
	 * Método para buscar una persona por la identificación y el tipo de identificación
	 * @return
	 */
	@Override
	public Personas buscarPersonasCompleto(String identificacionBuscar, String acronimoTipoIdentificacion){
		
		Personas persona= new Personas();
		try {
			persona=personas.buscarPersona(identificacionBuscar, acronimoTipoIdentificacion);
		} catch (HibernateException  e) {
			Log4JManager.error(e);
		}
		return persona;
	}
	
	
	@Override
	public Personas attachDirty(DtoPersonas dtoPersona)
	{
		Personas personaOrm=new Personas();
		TiposIdentificacion tipoId=new TiposIdentificacion();
		tipoId.setAcronimo(dtoPersona.getTipoIdentificacion());
		personaOrm.setTiposIdentificacion(tipoId);
		personaOrm.setNumeroIdentificacion(dtoPersona.getNumeroIdentificacion());
		personaOrm.setPrimerNombre(dtoPersona.getPrimerNombre());
		personaOrm.setSegundoNombre(dtoPersona.getSegundoNombre());
		personaOrm.setPrimerApellido(dtoPersona.getPrimerApellido());
		personaOrm.setSegundoApellido(dtoPersona.getSegundoApellido());
		personaOrm.setIndicativoInterfaz(ConstantesBD.acronimoNo);
		TiposPersonas tipoPersona=new TiposPersonas();
		tipoPersona.setCodigo(ConstantesBD.codigoTipoPersonaNatural);
		personaOrm.setTiposPersonas(tipoPersona);
		try
		{
			personas.attachDirty(personaOrm);
		}
		catch (Exception e) {
			Log4JManager.error("Error ingresando los datos de la persona", e);
			return null;
		}
		return personaOrm;
	}


	@Override
	public Personas buscarPersonasPorId(int codigoPkPersona) {
	
		Personas persona =new Personas();
		try {
		
			persona=personas.findById(codigoPkPersona);
		} catch (Exception e) {
			
		}
		return persona;
	}

	
	@Override
	public void attachDirty(Personas instance) {
		personas.attachDirty(instance);
	}

	/**
	 * @author Camilo Gomez
	 */
	public ArrayList<DtoPersonas>buscarPersonasPorNombresApellidos(DtoPersonas dtoPersona){
		return personas.buscarPersonasPorNombresApellidos(dtoPersona);
	}



	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.IPersonasDAO#buscarPersonasPorFiltro(com.princetonsa.dto.administracion.DtoPersonas)
	 */
	@Override
	public List<DtoPersonas> buscarPersonasPorFiltro(DtoPersonas filtro) {
		return personas.buscarPersonasPorFiltro(filtro);
	}
	
}
