/**
 * 
 */
package com.servinte.axioma.dao.impl.consultaexterna;

import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.servinte.axioma.dao.interfaz.consultaexterna.IConsultorioDAO;

/**
 * @author Juan David Ramírez
 * @since 22 Diciembre 2010
 */
public class ConsultorioDAO implements IConsultorioDAO
{
	@Override
	public ArrayList<DtoConsultorios> listarConsultoriosParaGenerarAgenda(DtoAgendaOdontologica dtoAgenda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultoriosDao().listarConsultoriosParaGenerarAgenda(dtoAgenda);
	}
	
	@Override
	public void listarConsultoriosParaGenerarAgendaSinConsultorio(DtoAgendaOdontologica dtoAgenda)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultoriosDao().listarConsultoriosParaGenerarAgendaSinConsultorio(dtoAgenda);
	}

}
