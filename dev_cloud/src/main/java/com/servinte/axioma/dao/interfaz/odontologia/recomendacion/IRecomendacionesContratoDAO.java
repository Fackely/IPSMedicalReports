package com.servinte.axioma.dao.interfaz.odontologia.recomendacion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.orm.RecomendacionesContOdonto;

/**
 * INTERFAZ QUE DEFINE LOS METODOS 
 * 
 * @author Edgar Carvajal 
 *
 */
public interface IRecomendacionesContratoDAO 
{
	

	/**
	 * TODO MODIFICAR
	 * LISTA TODAS LAS RECOMENACIONES 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 * @param institucion
	 * @return
	 */
	public List<RecomendacionesContOdonto> listaRecomendaciones(RecomendacionesContOdonto dtoRecomendaciones , int institucion );

	
	
	/**
	 * METODO QUE LISTA LAS RECOMENDACIONES FILTRADAS POR LA INSTITUCION
	 * TAMBIEN RECIBE UN ARRAY DE INT PARA SETTER EL ATRIBUTO YA EXISTE UTILIZADO EN LA BUSQUEDA  
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomen
	 * @param listaCodigos
	 * @return
	 */
	public List<DtoRecomendaciones> listarRecomendaciones(RecomendacionesContOdonto dtoRecomen, ArrayList<Integer> listaCodigos);
	
	
	/**
	 * METODO QUE GUARDAR LAS RECOMENDACIONES
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 */
	public void guardarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion);
		
	/**
	 * METODO QUE ELIMINA LAS RECOMENDACIONES
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 */
	public void eliminarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion);
		
	
	/**
	 * METODO QUE MODIFICA UNA RECOMENDACION 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 */
	public void modificarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion); 
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 * @param aplicaCodigoPK
	 * @return
	 */
	public List<RecomendacionesContOdonto> busquedaRecomendaciones(RecomendacionesContOdonto dtoRecomendaciones, boolean aplicaCodigoPK );

	
	
	/**
	 * CARGA LA RECOMENACION POR ID, TAMBIEN CARGA EL SET DE AL RELACION SERVICIO PROGRAMA 
	 * @author Edgar Carvajal Ruiz
	 * @param recomendacion
	 * @return
	 */
	public RecomendacionesContOdonto buscarRecomenacionxId(RecomendacionesContOdonto recomendacion );
	
	
	
	/**
	 * Retorna las recomendaciones de un presupuesto odontologico
	 * 
	 * @param codPresoOdonto
	 * @return ArrayList<DtoRecomendaciones>
	 */
	public ArrayList<DtoRecomendaciones> obtenerRecomendacionesPresuOdonto(long codPresoOdonto);
	
}
