package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposMontoMundo;
import com.servinte.axioma.orm.TiposMonto;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposMontoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Tipos de Monto
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class TiposMontoServicio implements ITiposMontoServicio {
	/**
	 * Instancia de la entidad ITiposMontoMundo
	 */
	ITiposMontoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public TiposMontoServicio(){
		mundo = FacturacionFabricaMundo.crearTiposMontoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de montos
	 * de cobro manejados 
	 * 
	 * @return  ArrayList<TiposMonto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMonto> consultarTiposMonto(){
		return mundo.consultarTiposMonto();
	}

}
