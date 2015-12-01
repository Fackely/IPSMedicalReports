package com.servinte.axioma.dao.fabrica.facturacion.convenio;

import com.servinte.axioma.dao.impl.facturacion.convenio.ContratoDAO;
import com.servinte.axioma.dao.impl.facturacion.convenio.ConvenioDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;

/**
 * Esta clase se encarga de crear las instancias necesarias
 * del DAO para la entidad Convenio
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public abstract class ConvenioFabricaDAO {
		
	/**
	 * 
	 * Método constructor de la clase
	 * 
	 */
	private ConvenioFabricaDAO(){		
	}	
	
	/**
	 * Método que retorna una de instancia de {@link ConvenioDAO }
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IConvenioDAO crearConvenioDAO(){
		return new ConvenioDAO();				
	}
	
	/**
	 * Crea una de instancia de {@link ConvenioDAO }
	 * @author Juan David Ramírez
	 * @return Implementación concreta de IContratoServicio
	 */
	public static IContratoDAO crearContratoDAO(){		
		return new ContratoDAO();		
	}	


}
