package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEvolucionOdontologica;

public interface EvolucionOdontologicaDao
{
	public ArrayList<DtoEvolucionOdontologica> consultarEvolucion(DtoEvolucionOdontologica dto);
	
	public double insertarEvolucionOdon(DtoEvolucionOdontologica dto, Connection con );
	
	/**
	 * Método implementado para cargar la informacion de la evolucion
	 * @param con
	 * @param evolucionOdo
	 */
	public void consultar(Connection con,DtoEvolucionOdontologica evolucionOdo);
}