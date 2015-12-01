/**
 * 
 */
package com.servinte.axioma.mundo.impl.consultaexterna;

import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.servinte.axioma.dao.fabrica.consultaexterna.ConsultaExternaFabricaDAO;
import com.servinte.axioma.dao.interfaz.consultaexterna.IConsultorioDAO;
import com.servinte.axioma.mundo.interfaz.consultaexterna.IConsultoriosMundo;

/**
 * @author Juan David Ramírez
 * @since 22 Diciembre 2010
 *
 */
public class ConsultoriosMundo implements IConsultoriosMundo
{

	@Override
	public ArrayList<DtoConsultorios> listarConsultoriosParaGenerarAgenda(DtoAgendaOdontologica dtoAgenda)
	{
		IConsultorioDAO consultorioDAO=ConsultaExternaFabricaDAO.crearConsultorioDAO();
		return consultorioDAO.listarConsultoriosParaGenerarAgenda(dtoAgenda);
	}

	@Override
	public void listarConsultoriosParaGenerarAgendaSinConsultorio(DtoAgendaOdontologica dtoAgenda)
	{
		IConsultorioDAO consultorioDAO=ConsultaExternaFabricaDAO.crearConsultorioDAO();
		consultorioDAO.listarConsultoriosParaGenerarAgendaSinConsultorio(dtoAgenda);
		
	}

}
