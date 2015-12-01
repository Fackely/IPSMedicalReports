package com.servinte.axioma.mundo.fabrica.odontologia.facturacion.convenio;

import com.servinte.axioma.mundo.impl.facturacion.convenio.ContratoMundo;
import com.servinte.axioma.mundo.impl.facturacion.convenio.ConveniosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.servicio.impl.facturacion.convenio.ConvenioServicio;

/**
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidad ConvenioMundo
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public  abstract class ConveniosFabricaMundo {

	
	/**
	 * Método constructor de la clase
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	private ConveniosFabricaMundo(){
		
	}
	
	/**
	 * Método que crear una instancia de {@link ConveniosMundo }
	 * @author Edgar Carvajal Ruiz
	 */
	public static  IConveniosMundo crearConveniosMundo(){		
		return new ConveniosMundo();		
	}
	
	/**
	 * Crea una de instancia de {@link ConvenioServicio }
	 * @author Juan David Ramírez
	 * @return Implementación concreta de IContratoServicio
	 */
	public static IContratoMundo crearContratoMundo(){		
		return new ContratoMundo();		
	}	

	
}
