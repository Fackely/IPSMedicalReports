package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IUnidadPagoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IUnidadPagoMundo;
import com.servinte.axioma.orm.UnidadPago;

public class UnidadPagoMundo implements IUnidadPagoMundo {

	IUnidadPagoDAO dao;
	
	public UnidadPagoMundo(){
		dao=CapitacionFabricaDAO.crearUnidadPagoDAO();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUnidadPagoMundo#consultarUnidadPagoPorFecha(java.util.Date)
	 */
	@Override
	public ArrayList<UnidadPago> consultarUnidadPagoPorFecha(Date fecha) {
		return dao.consultarUnidadPagoPorFecha(fecha);
	}

}
