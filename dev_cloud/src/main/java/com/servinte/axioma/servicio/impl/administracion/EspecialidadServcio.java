package com.servinte.axioma.servicio.impl.administracion;

import java.util.List;

import com.servinte.axioma.mundo.fabrica.administracion.EspecialidadFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IEspecialidadMundo;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;


/**
 * 
 * @author Edgar Carva
 *
 */
public class EspecialidadServcio implements IEspecialidadServicio {

	
	/**
	 * Mundo
	 */
	private IEspecialidadMundo especialidadMundo;
	
	
	/**
	 * CONSTRUTOR
	 */
	public EspecialidadServcio()
	{
		especialidadMundo =EspecialidadFabricaMundo.crearEspecialidadMundo();
	}
	
	
	
	
	
	@Override
	public List<Especialidades> listarEspe(Especialidades dtoEspec) {
		return  especialidadMundo.listarEspe(dtoEspec);
	}





	@Override
	public Especialidades buscarPorId(int codigoPk) {
		return especialidadMundo.buscarPorId(codigoPk);
	}

}
