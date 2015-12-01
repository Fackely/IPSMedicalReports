package com.servinte.axioma.mundo.impl.tesoreria;

import com.servinte.axioma.dao.impl.tesoreria.HistComisionXCentroAtenHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.HistTarjFinancieraComisionHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.HistTarjFinancieraReteicaHibernateDAO;
import com.servinte.axioma.dao.impl.tesoreria.HistTarjetasFinancierasHibernateDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ILogTarjetasFinancieraMundo;
import com.servinte.axioma.orm.HistComisionXCentroAten;
import com.servinte.axioma.orm.HistTarjFinancieraComision;
import com.servinte.axioma.orm.HistTarjFinancieraReteica;
import com.servinte.axioma.orm.HistTarjetasFinancieras;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con LogTarjetasFinancieraMundo
 * 
 * @author Cristhian Murillo
 * @see ILogTarjetasFinancieraMundo
 */
public class LogTarjetasFinancieraMundo implements ILogTarjetasFinancieraMundo {

	
	private HistComisionXCentroAtenHibernateDAO 	histComisionXCcentroAtenHibernateDAO;
	private HistTarjetasFinancierasHibernateDAO		histTarjetasFinancierasHibernateDAO;
	private HistTarjFinancieraComisionHibernateDAO 	histTarjFinancieraComisionHibernateDAO;
	private HistTarjFinancieraReteicaHibernateDAO 	histTarjFinancieraReteicaHibernateDAO;
	
	 
	/**
	 * Constructor de la clase
	 */
	public LogTarjetasFinancieraMundo() {
		inicializar();
	}
	
	
	/**
	 * M&eacute;todo que inicializa un objeto que implementa la interfaz IMovimientosCajaDAO
	 */
	private void inicializar() 
	{
		histComisionXCcentroAtenHibernateDAO 	= new HistComisionXCentroAtenHibernateDAO();
		histTarjetasFinancierasHibernateDAO		= new HistTarjetasFinancierasHibernateDAO();
		histTarjFinancieraComisionHibernateDAO	= new HistTarjFinancieraComisionHibernateDAO();
		histTarjFinancieraReteicaHibernateDAO	= new HistTarjFinancieraReteicaHibernateDAO();
	}


	@Override
	public void guardarHistComisionXCentroAten(
			HistComisionXCentroAten transientInstance) {
		histComisionXCcentroAtenHibernateDAO.guardarHistComisionXCentroAten(transientInstance);
	}


	@Override
	public void guardarHistTarjFinancieraReteica(
			HistTarjFinancieraReteica transientInstance) {
		histTarjFinancieraReteicaHibernateDAO.guardarHistTarjFinancieraReteica(transientInstance);
	}


	@Override
	public void guardarHistTarjFinancieraComision(
			HistTarjFinancieraComision transientInstance) {
		histTarjFinancieraComisionHibernateDAO.guardarHistTarjFinancieraComision(transientInstance);
	}


	@Override
	public void guardarHistTarjetasFinancieras(
			HistTarjetasFinancieras transientInstance) {
		histTarjetasFinancierasHibernateDAO.guardarHistTarjetasFinancieras(transientInstance);
	}

}
