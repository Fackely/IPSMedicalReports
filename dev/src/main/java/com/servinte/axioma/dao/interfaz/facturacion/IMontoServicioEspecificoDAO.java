package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IMontoServicioEspecificoDAO{
	
	/** 
	 * Este m?todo se encarga de consultar todos los
	 * servicios espec?ficos relacionados al detalle del monto de cobro
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoServicioEspecifico> obtenerServiciosPorDetalleID(int idDetalle,int codigoTarifario);
	
	/**
	 * 
	 * Este m?todo se encarga de eliminar el registro
	 * de un servicio espec?fico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id);

}
