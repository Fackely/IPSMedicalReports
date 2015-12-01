package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IHistoricoCapitacionSubcontratadaMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.IHistoricoCapitacionSubcontratadaServicio;

public class HistoricoCapitacionSubcontratadaServicio implements IHistoricoCapitacionSubcontratadaServicio{

	IHistoricoCapitacionSubcontratadaMundo mundo;
	
	
	/**
	 * M�todo constructor de la clase
	 * @author Camilo Gomez
	 */
	public HistoricoCapitacionSubcontratadaServicio(){
		mundo = CapitacionFabricaMundo.crearHistoricoCapitacionSubMundo();

	}
	

	/**
	 * 
	 * Este M�todo se encarga de consultar por el ID las autorizaciones historicas 
	 * de entidades subcontratadas y su respectiva autorizaci�n de capitaci�n
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
		return mundo.obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(dto);
	}

	
}
