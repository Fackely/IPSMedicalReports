package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.List;

import com.servinte.axioma.orm.ViewFinalidadesServ;

public interface IViewFinalidadesServDAO {
	
	/**
	 * M&eacute;todo encargado de obtener las finalidades
	 * de los servicios existentes en el sistema
	 * @param codigoServicio
	 * @return List<ViewFinalidadesServ>
	 * @author Diana Carolina G
	 */
	public List<ViewFinalidadesServ> obtenerViewFinalidadesServ(int codigoServicio);

}
