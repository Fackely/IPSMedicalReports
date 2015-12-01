package com.servinte.axioma.servicio.interfaz.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.orm.FinalidadesConsulta;

public interface IFinalidadesConsultaServicio {
	
	/**
	 * Este M�todo se encarga de consultar las finalidades consulta
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesConsulta>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<FinalidadesConsulta> consultarFinalidadesConsulta();

}
