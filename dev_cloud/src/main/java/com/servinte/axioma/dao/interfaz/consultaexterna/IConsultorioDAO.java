/**
 * 
 */
package com.servinte.axioma.dao.interfaz.consultaexterna;

import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;

/**
 * @author Juan David Ram�rez
 * @since 22 Diciembre 2010
 */
public interface IConsultorioDAO
{

	/**
	 * Lista los consultorios asociados a los horarios de atenci�n que
	 * concuerden con los par�metros de b�squeda enviados
	 * @param dtoAgenda Dto con los datos de la agenda
	 * @return Lista de consultorios que cumplan las condiciones
	 */
	public ArrayList<DtoConsultorios> listarConsultoriosParaGenerarAgenda(DtoAgendaOdontologica dtoAgenda);

	/**
	 * Lista los consultorios que no se encuentren asociados a los horarios de atenci�n
	 * que concuerden con los par�metros de b�squeda enviados
	 * Los consultorios se asignan en el atributo listConsultoriosXGenerar
	 * @param dtoAgenda DTO con los datos de la agenda
	 */
	public void listarConsultoriosParaGenerarAgendaSinConsultorio(DtoAgendaOdontologica dtoAgenda);

}
