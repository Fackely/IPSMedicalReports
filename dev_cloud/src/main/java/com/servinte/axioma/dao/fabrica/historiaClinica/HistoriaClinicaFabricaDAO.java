package com.servinte.axioma.dao.fabrica.historiaClinica;

import com.servinte.axioma.dao.impl.historiaClinica.AntecedentesHibernateDAO;
import com.servinte.axioma.dao.impl.historiaClinica.AntecedentesVariosHibernateDAO;
import com.servinte.axioma.dao.impl.historiaClinica.CausasExternasHibernateDAO;
import com.servinte.axioma.dao.impl.historiaClinica.FinalidadesConsultaHibernateDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.IAntecedentesDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.IAntecedentesVariosDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.ICausasExternasDAO;
import com.servinte.axioma.dao.interfaz.historiaClinica.IFinalidadesConsultaDAO;


/**
 * 
 * @author Cristhian Murillo
 *
 */
public class HistoriaClinicaFabricaDAO {
	
	public HistoriaClinicaFabricaDAO() {
		
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad ICausasExternasDAO
	 * 
	 * @return CausasExternasHibernateDAO
	 * @author, Fabián Becerra
	 *
	 */
	public static ICausasExternasDAO crearCausasExternasDAO(){
		return new CausasExternasHibernateDAO();
	}
	

	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IFinalidadesConsultaDAO
	 * 
	 * @return FinalidadesConsultaHibernateDAO
	 * @author, Fabián Becerra
	 *
	 */
	public static IFinalidadesConsultaDAO crearFinalidadesConsultaDAO(){
		return new FinalidadesConsultaHibernateDAO();
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad IAntecedentesVariosDAO
	 * 
	 * @return AntecedentesVariosHibernateDAO
	 * @author, Cristhian Murillo
	 *
	 */
	public static IAntecedentesVariosDAO crearAntecedentesVariosDAO(){
		return new AntecedentesVariosHibernateDAO();
	}
	
	

	/**
	 * 
	 * Este Método se encarga de crear una instancia para la entidad IAntecedentesVariosDAO
	 * 
	 * @return AntecedentesHibernateDAO
	 * @author, Cristhian Murillo
	 *
	 */
	public static IAntecedentesDAO crearAntecedentesDAO(){
		return new AntecedentesHibernateDAO();
	}
	

}
