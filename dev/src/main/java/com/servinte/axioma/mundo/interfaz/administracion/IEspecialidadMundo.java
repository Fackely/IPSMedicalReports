package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.List;

import com.servinte.axioma.orm.Especialidades;

public interface IEspecialidadMundo {
	
	
	/**
	 * LISTA ESPECIALIDADES POR TIPO, INSTITUCION 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoEspec
	 * @return
	 */
	public List<Especialidades> listarEspe(Especialidades dtoEspec );
	
	/**
	 * Buscar una especialidad por codigoPK
	 * @param codigo
	 * @return
	 */
	public Especialidades buscarPorId(int codigoPk);

}
