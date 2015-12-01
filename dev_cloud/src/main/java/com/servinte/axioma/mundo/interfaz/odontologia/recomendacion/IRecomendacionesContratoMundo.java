package com.servinte.axioma.mundo.interfaz.odontologia.recomendacion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.mundo.excepcion.MundoRuntimeExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.VerificacionIngresoUnicosException;
import com.servinte.axioma.orm.RecomendacionesContOdonto;


/**
 * INTEFAZ PARA EL MUNDO DE RECOMENDACIONES CONTRATO
 * ADAPTADOR  PARA COMUNICAR EL MUNDO OTRAS CLASES
 * @author Edgar Carvajal 
 */
public interface IRecomendacionesContratoMundo {
	
	
	
	/**
	 * MENSAJE DE ERROR 
	 * SE PRESENTA CUANDO YA EXISTE UNA RECOMENDACION PERSISTIDA 
	 */
	public static final String error=" Ya existe una Recomendacion ";
	
	
	/**
	 * MENASAJE DE ERROR ELIMIAN RECOMENDACION
	 * SE PRESENTA CUANDO YA EXISTE UNA RECOMENCION EN LA FUNCIONALIDAD DE RECOMENDACIONES ODONTOLOGICAS 
	 */
	public static final String errorEliminar="La recomendacion ya esta siendo utilizada en la funcionalidad de Recomendaciones por Programa Odontológico. ";
	
	
	/**
	 * METODO QUE LISTA TODAS LAS RECOMENDACIONES 
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
	public void guardarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) throws VerificacionIngresoUnicosException ;
	
	/**
	 * METODO QUE ELIMINA LAS RECOMENDACIONES
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 */
	public void eliminarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion)  throws MundoRuntimeExcepcion;
	
	
	/**
	 * METODO QUE MODIFICA UNA RECOMENDACION 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendacion
	 */
	public void modificarRecomendaciones(RecomendacionesContOdonto dtoRecomendacion) throws VerificacionIngresoUnicosException;

	
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
