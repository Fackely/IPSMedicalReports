package com.servinte.axioma.servicio.interfaz.odontologia.recomendacion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.servicio.excepcion.ServicioException;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public interface IRecomendacionesContOdontoServicio 
{
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 * @return
	 */
	public List<RecomendacionesContOdonto> obtenerRecomendacionesContrato(RecomendacionesContOdonto dtoRecomendaciones, int institucion );
	
	
	/**
	 * METODO QUE GUARDAR LAS RECOMENDACIONES
	 * RECIBE UN DTO RECOMENDACIONES
	 * VALIDA SI YA PERSISTE UNA RECOMENDACION Y EXITE RETORNA UN EXCEPTION PARA MOSTRARLA A NIVEL DE PRESENTACION    
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 */
	public void guardarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) throws ServicioException;
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 * @throws ServicioException 
	 */
	public void eliminarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) throws ServicioException;
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 * @throws ServicioException
	 */
	public void modificarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion)  throws ServicioException;;
	
	
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
	 * Retorna las recomendaciones de un presupuesto odontologico
	 * 
	 * @param codPresoOdonto
	 * @return ArrayList<DtoRecomendaciones>
	 */
	public ArrayList<DtoRecomendaciones> obtenerRecomendacionesPresuOdonto(long codPresoOdonto);
	

}
