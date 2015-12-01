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
	 * M�todo constructor de la clase
	 * @author, Fabi�n Becerra
	 */
	public FinalidadesConsultaMundo(){
		dao = HistoriaClinicaFabricaDAO.crearFinalidadesConsultaDAO();
	}
	
	/**
	 * Este M�todo se encarga de consultar las finalidades consulta
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesConsulta>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<FinalidadesConsulta> consultarFinalidadesConsulta(){
		return dao.consultarFinalidadesConsulta();
	}
}
