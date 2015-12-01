package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;

public interface DescuentoOdontologicoDao {

	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoDescuentosOdontologicos dtoNuevo, DtoDescuentosOdontologicos dtoWhere);
	
	 /** 
	 * @param 
	 * @return
	 */
	public boolean modificarAtencion(DtoDescuentoOdontologicoAtencion dtoNuevo, DtoDescuentoOdontologicoAtencion dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoDescuentosOdontologicos> cargar(DtoDescuentosOdontologicos dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoDescuentoOdontologicoAtencion> cargarAtencion(DtoDescuentoOdontologicoAtencion dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoDescuentosOdontologicos dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoDescuentosOdontologicos dto) ;
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardarAtencion(DtoDescuentoOdontologicoAtencion dto) ;
	
	/**
	 * 
	 * 
	 * 
	 */
	
	
	
	public  boolean existeCruceFechas(DtoDescuentosOdontologicos  dto, double codigoPkNotIn , int centroAtencion);
	
	
	/***
	 * 
	 * 
	 */
	
	public  boolean eliminarAtencion(DtoDescuentoOdontologicoAtencion dtoWhere) ;
	
	/**
	 * 
	 * 
	 * 
	 */
	
}
