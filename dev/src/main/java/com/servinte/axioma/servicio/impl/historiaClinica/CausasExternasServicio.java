package com.servinte.axioma.servicio.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.historiaClinica.HistoriaClinicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.ICausasExternasMundo;
import com.servinte.axioma.orm.CausasExternas;
import com.servinte.axioma.servicio.interfaz.historiaClinica.ICausasExternasServicio;

public class CausasExternasServicio implements ICausasExternasServicio{
	
	ICausasExternasMundo mundo;
	
	public CausasExternasServicio(){
		mundo=HistoriaClinicaFabricaMundo.crearCausasExternasMundo();
	}

	/**
	 * Este Método se encarga de consultar las causas externas
	 * registradas en el sistema
	 * 
	 * @return ArrayList<CausasExternas>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<CausasExternas> consultarCausasExternas(){
		return mundo.consultarCausasExternas();
	}

}
