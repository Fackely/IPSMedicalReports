package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.List;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.servinte.axioma.orm.EstanciaViaIngCentroCosto;

public interface IEstanciaViaIngCentroCostoMundo {

	
	
	/**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(EstanciaViaIngCentroCosto objeto);

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(EstanciaViaIngCentroCosto objeto);
	
		
	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( EstanciaViaIngCentroCosto objeto);
		
		
	

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public EstanciaViaIngCentroCosto buscarxId(Number id);
	
	
	/**
	 * 
	 * @param dtoEstancia
	 */
	public void insertarEstancia(DTOEstanciaViaIngCentroCosto dtoEstancia);
	
	/**
	 * 
	 * @param idEntidadSubContratada
	 * @return
	 */
	public List<DTOEstanciaViaIngCentroCosto> listarEstanciasxEntidadesSubContratadas(Long idEntidadSubContratada);
	
	
	
	
	/**
	 * 
	 * @param listDTO
	 */
	public void modificarDTOEstanciaViaIngCentroCosto(List<DTOEstanciaViaIngCentroCosto> listDTO);
	
	/**
	 * 
	 * @param viaIngreso
	 * @param centroCosto
	 * @param listaEstancia
	 * @return
	 */
	public boolean validarExistencia(int viaIngreso, int centroCosto, List<DTOEstanciaViaIngCentroCosto> listaEstancia);
	
	
	

}
