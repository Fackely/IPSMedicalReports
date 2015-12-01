package com.servinte.axioma.mundo.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.historiaClinica.HistoriaClinicaFabricaDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.IFinalidadesConsultaDAO;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IFinalidadesConsultaMundo;
import com.servinte.axioma.orm.FinalidadesConsulta;

public class FinalidadesConsultaMundo implements IFinalidadesConsultaMundo{

	
	IFinalidadesConsultaDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public FinalidadesConsultaMundo(){
		dao = HistoriaClinicaFabricaDAO.crearFinalidadesConsultaDAO();
	}
	
	/**
	 * Este Método se encarga de consultar las finalidades consulta
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesConsulta>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<FinalidadesConsulta> consultarFinalidadesConsulta(){
		return dao.consultarFinalidadesConsulta();
	}
}
