package com.servinte.axioma.mundo.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.historiaClinica.HistoriaClinicaFabricaDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.IAntecedentesDAO;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAntecedentesMundo;
import com.servinte.axioma.orm.Antecedentes;

/**
 * 
 * @author Cristhian Murillo
 *
 */
public class AntecedentesMundo implements IAntecedentesMundo{
	
	
	IAntecedentesDAO antecedentesDAO;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Cristhian Murillo
	 */
	public AntecedentesMundo(){
		antecedentesDAO = HistoriaClinicaFabricaDAO.crearAntecedentesDAO();
	}

	
	@Override
	public ArrayList<Antecedentes> listarAntecedentes() 
	{
		return antecedentesDAO.listarAntecedentes();
	}
	

}
