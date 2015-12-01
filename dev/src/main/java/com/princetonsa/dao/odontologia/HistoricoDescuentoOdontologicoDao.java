package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologicoAtencion;

public interface HistoricoDescuentoOdontologicoDao {

	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoHistoricoDescuentoOdontologico dtoNuevo, DtoHistoricoDescuentoOdontologico dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoHistoricoDescuentoOdontologico> cargar(DtoHistoricoDescuentoOdontologico dtoWhere);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoHistoricoDescuentoOdontologico dto) ;
	
	
	/***
	 * 
	 * 
	 */
	public  boolean modificarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dtoNuevo, DtoHistoricoDescuentoOdontologicoAtencion dtoWhere) ;
	
	
	/**
	 * 
	 * 
	 */
	
	public  ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> cargarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dtoWhere);
	
	/***
	 * 
	 * 
	 * 
	 */
	
	public  double guardarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dto) ;
	
	
	
}
