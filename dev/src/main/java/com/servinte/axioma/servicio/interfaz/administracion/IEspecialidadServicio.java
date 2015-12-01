package com.servinte.axioma.servicio.interfaz.administracion;

import java.util.List;

import com.servinte.axioma.orm.Especialidades;


/**
 *  
 * @author Edgar Carvajal
 *
 */
public interface IEspecialidadServicio {
	
	
	
	
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
