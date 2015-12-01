package com.servinte.axioma.servicio.impl.tesoreria;

import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ILogTarjetasFinancieraMundo;
import com.servinte.axioma.orm.HistComisionXCentroAten;
import com.servinte.axioma.orm.HistTarjFinancieraComision;
import com.servinte.axioma.orm.HistTarjFinancieraReteica;
import com.servinte.axioma.orm.HistTarjetasFinancieras;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILogTarjetasFinancieraServicio;

/**
 * @author Cristhian Murillo
 */
public class LogTarjetasFinancieraServicio implements ILogTarjetasFinancieraServicio 
{
	
	private ILogTarjetasFinancieraMundo logTarjetasFinancieraMundo;
	
	
	public LogTarjetasFinancieraServicio(){
		logTarjetasFinancieraMundo	=  TesoreriaFabricaMundo.crearLogTarjetasFinancieraMundo();
	}


	@Override
	public void guardarHistComisionXCentroAten(
			HistComisionXCentroAten transientInstance) {
		logTarjetasFinancieraMundo.guardarHistComisionXCentroAten(transientInstance);
	}


	@Override
	public void guardarHistTarjFinancieraReteica(
			HistTarjFinancieraReteica transientInstance) {
		logTarjetasFinancieraMundo.guardarHistTarjFinancieraReteica(transientInstance);
	}


	@Override
	public void guardarHistTarjetasFinancieras(
			HistTarjetasFinancieras transientInstance) {
		logTarjetasFinancieraMundo.guardarHistTarjetasFinancieras(transientInstance);
	}


	@Override
	public void guardarHistTarjFinancieraComision(
			HistTarjFinancieraComision transientInstance) {
		logTarjetasFinancieraMundo.guardarHistTarjFinancieraComision(transientInstance);
	}

	

}
