package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoAutorizacionCapitaSubDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoricoIngEstanciaSubcontratadaDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IHistoricoCapitacionSubcontratadaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IHistoricoIngEstanciaSubcontratadaMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.IHistoricoCapitacionSubcontratadaServicio;

public class HistoricoIngEstanciaSubcontratadaMundo implements IHistoricoIngEstanciaSubcontratadaMundo{

	
	IHistoricoIngEstanciaSubcontratadaDAO dao;
	
	
	public HistoricoIngEstanciaSubcontratadaMundo(){
		dao = CapitacionFabricaDAO.crearHistoricoIngEstanciaSubcontratadaDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones historicas de
	 * Ingreso Estancia
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorizacionIngresoEstancia> obtenerHistoricoAutorizEntSubIngEstanciaPorID(
			DTOAutorizacionIngresoEstancia dto){
		return dao.obtenerHistoricoAutorizEntiSubIngEstanciaPorID(dto);
	}
	
	
}
