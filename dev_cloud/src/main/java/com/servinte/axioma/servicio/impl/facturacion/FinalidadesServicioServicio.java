package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IFinalidadesServicioMundo;
import com.servinte.axioma.orm.FinalidadesServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IFinalidadesServicioServicio;

public class FinalidadesServicioServicio implements IFinalidadesServicioServicio{
	
	
	IFinalidadesServicioMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public FinalidadesServicioServicio(){
		mundo = FacturacionFabricaMundo.crearFinalidadesServicioMundo();
	}	
	
	/**
	 * Este Método se encarga de consultar las finalidades del servicio
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesServicio>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<FinalidadesServicio> consultarFinalidadesServicio(){
		return mundo.consultarFinalidadesServicio();
	}

}
