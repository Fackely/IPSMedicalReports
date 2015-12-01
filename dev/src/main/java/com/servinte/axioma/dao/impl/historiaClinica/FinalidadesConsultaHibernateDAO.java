package com.servinte.axioma.dao.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.historiaClinica.IFinalidadesConsultaDAO;
import com.servinte.axioma.orm.FinalidadesConsulta;
import com.servinte.axioma.orm.delegate.historiaClinica.FinalidadesConsultaDelegate;

public class FinalidadesConsultaHibernateDAO implements IFinalidadesConsultaDAO{
	
	
	FinalidadesConsultaDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public FinalidadesConsultaHibernateDAO(){
		delegate = new FinalidadesConsultaDelegate();		
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
		return delegate.consultarFinalidadesConsulta();
	}

}
