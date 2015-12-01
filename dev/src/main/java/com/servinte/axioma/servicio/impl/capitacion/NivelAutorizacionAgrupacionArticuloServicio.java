package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionAgrupacionArticuloMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionAgrupacionArticuloServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionAgrupacionArticuloServicio implements
		INivelAutorizacionAgrupacionArticuloServicio {
	
	INivelAutorizacionAgrupacionArticuloMundo mundo;
	
	public NivelAutorizacionAgrupacionArticuloServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionAgrupacionArticuloMundo();
	}

	/**
	 * 
	 * Este Método se encarga de insertar un registro de nivel de autorización
	 * de agrupación de artículos
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int insertarMontoAgrupacionArticulo(DTOBusquedaNivelAutorAgrupacionArticulo dto){
		return mundo.insertarMontoAgrupacionArticulo(dto);
	}

	/**
	 * 
	 * Este Método se encarga de eliminar un registro de nivel de autorización
	 * de agrupación de artículos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionAgrupacionArticulo(DTOBusquedaNivelAutorAgrupacionArticulo dto){
		return mundo.eliminarNivelAutorizacionAgrupacionArticulo(dto);
	}

	/**
	 * 
	 * Este Método se encarga de actualizar un registro de autorización
	 * de agrupación de artículos
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int actualizarMontoAgrupacionArticulo(DTOBusquedaNivelAutorAgrupacionArticulo dto){
		return mundo.actualizarMontoAgrupacionArticulo(dto);
	}

	/**
	 * 
	 * Este Método se encarga de buscar un registro de nivel de autorización
	 * de agrupación de artículos por el id del nivel de autorización de
	 * servicios - artículos relacionado
	 * 
	 * @param Connection conn, DTOBusquedaNivelAutorAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> buscarNivelAutorizacionAgrupacionArticulo(
			DTOBusquedaNivelAutorAgrupacionArticulo dto){
		return mundo.buscarNivelAutorizacionAgrupacionArticulo(dto);
	}
}
