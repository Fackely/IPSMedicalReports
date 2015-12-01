package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;
import java.util.List;
import com.servinte.axioma.dao.interfaz.administracion.IEspecialidadDao;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.delegate.administracion.EspecialidadesDelegate;



/**
 * 
 * @author Edgar carvajal
 *
 */
	
public class EspecialidadDao implements IEspecialidadDao {
	
	private EspecialidadesDelegate delegate;
	
	
	public EspecialidadDao(){
		
		delegate = new EspecialidadesDelegate();
	}


	@Override
	public List<Especialidades> listarEspecialidades(String tipoEspecialidad) 
	{
		return delegate.listarEspecialidades(tipoEspecialidad);
	}





	@Override
	public ArrayList<Especialidades> listarEspecialidadesSinCuentasXEspecialidad(
			int institucion) {
		
		return delegate.listarEspecialidadesSinCuentasXEspecialidad(institucion);
	}


	@Override
	public Especialidades buscarxId(Number id) 
	{
		return delegate.findById(id.intValue());
	}


	@Override
	public void eliminar(Especialidades objeto) 
	{
		delegate.delete(objeto);
		
	}


	@Override
	public void insertar(Especialidades objeto) {
		delegate.attachDirty(objeto);	
	}


	
	@Override
	public void modificar(Especialidades objeto) {
		delegate.attachDirty(objeto);
	}


	
	
	@Override
	public List<Especialidades> listarEspe(Especialidades dtoEspec) 
	{
		return delegate.listarEspe(dtoEspec);
	}
	
	
	

	
	

}
