package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IGruposServiciosMundo;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.servicio.interfaz.facturacion.IGruposServiciosServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 8/09/2010
 */
public class GruposServiciosServicio implements IGruposServiciosServicio {
	
	IGruposServiciosMundo mundo;
	
	public GruposServiciosServicio(){
		mundo=FacturacionFabricaMundo.crearGruposServiciosMundo();
	}
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GruposServicios> buscarGruposServicioActivos(){
		return mundo.buscarGruposServicioActivos();
	}
	/**
	 * 	
	 * Este M�todo se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GruposServicios> buscarGruposServicio(){
		return mundo.buscarGruposServicio();
	}

	/**
	 * 	
	 * Este Metodo consulta los grupos de servicio
	 * @return List<GrupoServicioDto>
	 * @author ginsotfu
	 *
	 */
	public List<GrupoServicioDto> consultarGruposServicio() throws IPSException {
		return mundo.consultarGruposServicio();
	}

}
