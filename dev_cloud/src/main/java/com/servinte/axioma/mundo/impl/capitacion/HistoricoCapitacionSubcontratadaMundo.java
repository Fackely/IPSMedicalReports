package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoAutorizacionCapitaSubDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IHistoricoCapitacionSubcontratadaMundo;

public class HistoricoCapitacionSubcontratadaMundo implements IHistoricoCapitacionSubcontratadaMundo{

	IHistoAutorizacionCapitaSubDAO dao;
	
	
	public HistoricoCapitacionSubcontratadaMundo(){
		dao = CapitacionFabricaDAO.crearHistoricoCapitacionSubcontratadaDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones historicas de
	 * entidades subcontratadas y su respectiva autorización de capitación
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
		return dao.obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(dto);
	}
}
