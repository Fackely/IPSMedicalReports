package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IHistoricoIngEstanciaSubcontratadaMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.IHistoricoIngEstanciaSubcontratadaServicio;

public class HistoricoIngEstanciaSubcontratadaServicio implements IHistoricoIngEstanciaSubcontratadaServicio {
	
	
	IHistoricoIngEstanciaSubcontratadaMundo mundo;
	
	
	/**
	 * Método constructor de la clase
	 * @author Camilo Gomez
	 */
	public HistoricoIngEstanciaSubcontratadaServicio(){
		mundo = CapitacionFabricaMundo.crearHistoricoIngEstanciaSubMundo();

	}
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones historicas 
	 * de Ingreso Estancia
	 * 
	 * @param DTOAutorEntidadSubcontratadaCapitacion dto
	 * @return ArrayList<DTOAutorEntidadSubcontratadaCapitacion>
	 * @author, Camilo Gomez
	 *
	 */
	public ArrayList<DTOAutorizacionIngresoEstancia> obtenerHistoricoAutorizEntSubIngEstanciaPorID(
			DTOAutorizacionIngresoEstancia dto){
		return mundo.obtenerHistoricoAutorizEntSubIngEstanciaPorID(dto);
	}

}
