package com.servinte.axioma.servicio.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.historiaClinica.HistoriaClinicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IFinalidadesConsultaMundo;
import com.servinte.axioma.orm.FinalidadesConsulta;
import com.servinte.axioma.servicio.interfaz.historiaClinica.IFinalidadesConsultaServicio;

public class FinalidadesConsultaServicio implements IFinalidadesConsultaServicio{

	IFinalidadesConsultaMundo mundo;
	
	public FinalidadesConsultaServicio(){
		mundo=HistoriaClinicaFabricaMundo.crearFinalidadesConsultaMundo();
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
		return mundo.consultarFinalidadesConsulta();
	}
	
}
