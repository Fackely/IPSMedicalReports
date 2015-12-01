package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.facturacion.IFinalidadesServicioDAO;
import com.servinte.axioma.orm.FinalidadesServicio;
import com.servinte.axioma.orm.delegate.facturacion.FinalidadesServicioDelegate;

public class FinalidadesServicioHibernateDAO implements IFinalidadesServicioDAO{
	
	FinalidadesServicioDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * 
	 * @author, Fabián Becerra
	 */
	public FinalidadesServicioHibernateDAO(){
		
		 delegate = new FinalidadesServicioDelegate();
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
		return delegate.consultarFinalidadesServicio();
	}

}
