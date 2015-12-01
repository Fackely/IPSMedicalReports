package com.servinte.axioma.dao.fabrica.ordenes;

import com.servinte.axioma.dao.impl.ordenes.AutorizacionArticuloDespachoHibernateDAO;
import com.servinte.axioma.dao.impl.ordenes.OrdenesAmbulatoriasHibernateDAO;
import com.servinte.axioma.dao.impl.ordenes.RegistroEntregaEntSubHibernateDAO;
import com.servinte.axioma.dao.impl.ordenes.SolDespachoSinAutoHibernateDAO;
import com.servinte.axioma.dao.impl.ordenes.SolicitudesHibernateDAO;
import com.servinte.axioma.dao.impl.ordenes.SolicitudesPosponerHibernateDAO;
import com.servinte.axioma.dao.interfaz.ordenes.IAutorizacionArticuloDespachoDAO;
import com.servinte.axioma.dao.interfaz.ordenes.IOrdenesAmbulatoriasDAO;
import com.servinte.axioma.dao.interfaz.ordenes.IRegistroEntregaEntSubDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolDespachoSinAutoDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesPosponerDAO;

/**
 * Esta clase se encarga de crear las 
 * instancias necesarias del DAO para Ordenes
 * 
 * @author Cristhian Murillo
 *
 */
public abstract class OrdenesFabricaDAO {
		
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * 
	 */
	private OrdenesFabricaDAO(){ }	
	
	
	
	/**
	 * M�todo que retorna una de instancia de {@link AutorizacionArticuloDespachoDAO }
	 * 
	 * @author Cristhian Murillo
	 * @return IAutorizacionArticuloDespachoDAO
	 */
	public static IAutorizacionArticuloDespachoDAO crearAutorizacionArticuloDespachoDAO(){
		return new AutorizacionArticuloDespachoHibernateDAO();				
	}
	
	
	
	/**
	 * M�todo que retorna una de instancia de {@link RegistroEntregaEntSubDAO }
	 * 
	 * @author Cristhian Murillo
	 * @return IRegistroEntregaEntSubDAO
	 */
	public static IRegistroEntregaEntSubDAO crearRegistroEntregaEntSubDAO(){
		return new RegistroEntregaEntSubHibernateDAO();				
	}
	

	
	/**
	 * M�todo que retorna una de instancia de {@link SolicitudesHibernateDAO}
	 * 
	 * @author Cristhian Murillo
	 * @return IsolicitudesDAO
	 */
	public static ISolicitudesDAO crearSolicitudesDAO(){
		return new SolicitudesHibernateDAO();				
	}

	
	
	/**
	 * M�todo que retorna una de instancia de {@link SolDespachoSinAutoHibernateDAO}
	 * 
	 * @author Cristhian Murillo
	 * @return ISolDespachoSinAutoDAO
	 */
	public static ISolDespachoSinAutoDAO crearSolDespachoSinAutoDAO(){
		return new SolDespachoSinAutoHibernateDAO();				
	}
	
	

	/**
	 * M�todo que retorna una de instancia de {@link  SolicitudesPosponerDAO}
	 * 
	 * @author Cristhian Murillo
	 * @return  ISolicitudesPosponerDAO
	 */
	public static ISolicitudesPosponerDAO crearSolicitudesPosponerDAO(){
		return new  SolicitudesPosponerHibernateDAO();				
	}
	
	
	
	/**
	 * M�todo que retorna una de instancia de {@link  OrdenesAmbulatoriasHibernateDAO}
	 * 
	 * @author Fabi�n Becerra
	 * @return  IOrdenesAmbulatoriasDAO
	 */
	public static IOrdenesAmbulatoriasDAO crearOrdenesAmbulatoriasDAO(){
		return new  OrdenesAmbulatoriasHibernateDAO();				
	}
	
	
}
