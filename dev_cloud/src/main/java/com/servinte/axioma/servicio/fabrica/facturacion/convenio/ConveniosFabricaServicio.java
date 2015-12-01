package com.servinte.axioma.servicio.fabrica.facturacion.convenio;

import com.servinte.axioma.servicio.impl.facturacion.convenio.ContratoServicio;
import com.servinte.axioma.servicio.impl.facturacion.convenio.ConvenioServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IContratoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;

/**
 * Esta clase se encarga de crear las instancias necesarias del 
 * Servicio para la entidad Convenio
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public abstract class ConveniosFabricaServicio {

	/**
	 * Método que retorna una de instancia de {@link ConvenioServicio }
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IConvenioServicio crearConvenioServicio(){		
		return new ConvenioServicio();		
	}	

	/**
	 * Crea una de instancia de {@link ConvenioServicio }
	 * @author Juan David Ramírez
	 * @return Implementación concreta de IContratoServicio
	 */
	public static IContratoServicio crearContratoServicio(){		
		return new ContratoServicio();		
	}	

}
