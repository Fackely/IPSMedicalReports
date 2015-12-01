package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;


import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.Especialidades;

public interface IEspecialidadDao extends IBaseDAO<Especialidades> {

	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param tipoEspecialidad
	 * @return
	 */
	public  List<Especialidades>  listarEspecialidades (String tipoEspecialidad);

	
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param institucion
	 * @return
	 */
	public ArrayList<Especialidades> listarEspecialidadesSinCuentasXEspecialidad(int institucion);
	
	
	
	
	/**
	 * LISTA ESPECIALIDADES POR TIPO, INSTITUCION 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoEspec
	 * @return
	 */
	public List<Especialidades> listarEspe(Especialidades dtoEspec );
	
}
