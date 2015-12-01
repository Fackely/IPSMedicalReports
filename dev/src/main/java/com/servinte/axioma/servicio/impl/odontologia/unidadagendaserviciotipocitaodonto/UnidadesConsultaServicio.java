package com.servinte.axioma.servicio.impl.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.ArrayList;

import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.mundo.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonMundoFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaMundo;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadesConsultaServicio;

/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class UnidadesConsultaServicio implements IUnidadesConsultaServicio{

	
	
	public IUnidadesConsultaMundo unidadMundo;
	
	
	/**
	 * Construtor 
	 */
	public UnidadesConsultaServicio(){
		unidadMundo =UnidadAgendaServTipoCitaOdonMundoFabrica.crearUnidadesConsultaMundo();
		
	}
	
	
	@Override
	public UnidadesConsulta buscarUnidadConsultaId(int codigoUnidadConsulta) {
		return unidadMundo.buscarUnidadConsultaId(codigoUnidadConsulta);
	}


	@Override
	public ArrayList<DtoUnidadesConsulta> cargarUnidadesConsultaTipoEspecialidad(
			String tipo, int codigoEspecialidad,int codigoCentroAtencion,boolean filtrarActivas) {
		return unidadMundo.cargarUnidadesConsultaTipoEspecialidad(tipo,codigoEspecialidad,codigoCentroAtencion,filtrarActivas);
	}
	

}
