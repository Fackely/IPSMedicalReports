package com.servinte.axioma.mundo.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.historiaClinica.HistoriaClinicaFabricaDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.IAntecedentesVariosDAO;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAntecedentesVariosMundo;
import com.servinte.axioma.orm.AntecedentesVarios;

/**
 * 
 * @author Cristhian Murillo
 *
 */
public class AntecedentesVariosMundo implements IAntecedentesVariosMundo{
	
	
	IAntecedentesVariosDAO antecedentesVariosDAO;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Cristhian Murillo
	 */
	public AntecedentesVariosMundo(){
		antecedentesVariosDAO = HistoriaClinicaFabricaDAO.crearAntecedentesVariosDAO();
	}

	
	@Override
	public ArrayList<AntecedentesVarios> obtenerAntecedentesVariosPaciente(Integer codPaciente) 
	{
		return antecedentesVariosDAO.obtenerAntecedentesVariosPaciente(codPaciente);
	}	
	

}
