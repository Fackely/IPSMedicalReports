package com.servinte.axioma.dao.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.FinalidadesConsulta;

public interface IFinalidadesConsultaDAO {
	
	/**
	 * Este Método se encarga de consultar las finalidades consulta
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesConsulta>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<FinalidadesConsulta> consultarFinalidadesConsulta();
}

