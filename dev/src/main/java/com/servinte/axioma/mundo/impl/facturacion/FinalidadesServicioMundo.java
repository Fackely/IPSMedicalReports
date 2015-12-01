package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IFinalidadesServicioDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IFinalidadesServicioMundo;
import com.servinte.axioma.orm.FinalidadesServicio;

public class FinalidadesServicioMundo implements IFinalidadesServicioMundo{
	
	IFinalidadesServicioDAO dao;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Fabi�n Becerra
	 */
	public FinalidadesServicioMundo(){		
		dao = FacturacionFabricaDAO.crearFinalidadesServicioDAO();
	}	
	
	
	/**
	 * Este M�todo se encarga de consultar las finalidades del servicio
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesServicio>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<FinalidadesServicio> consultarFinalidadesServicio(){
		return dao.consultarFinalidadesServicio();
	}

}
