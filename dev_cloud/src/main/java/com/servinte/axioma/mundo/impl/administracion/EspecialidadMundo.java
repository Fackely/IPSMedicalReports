package com.servinte.axioma.mundo.impl.administracion;

import java.util.List;

import com.servinte.axioma.dao.fabrica.administracion.especialidad.EspecialidadDAOFabrica;
import com.servinte.axioma.dao.interfaz.administracion.IEspecialidadDao;
import com.servinte.axioma.mundo.interfaz.administracion.IEspecialidadMundo;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class EspecialidadMundo implements IEspecialidadMundo {

	
	IEspecialidadDao especialidadDAO;

	
	/**
	 * 
	 */
	public EspecialidadMundo ()
	{
		especialidadDAO =  EspecialidadDAOFabrica.crearEspecialidad();
	}
	
	
	
	
	@Override
	public List<Especialidades> listarEspe(Especialidades dtoEspec) 
	{
	
		UtilidadTransaccion.getTransaccion().begin();
		List<Especialidades> listaEsp= especialidadDAO.listarEspe(dtoEspec); 
		UtilidadTransaccion.getTransaccion().commit();
		
		return listaEsp;
	}




	@Override
	public Especialidades buscarPorId(int codigoPk) {
		
		return especialidadDAO.buscarxId(codigoPk);
	}
	
	
	
	

}
