package com.servinte.axioma.mundo.impl.facturacion;

import java.util.List;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IViewFinalidadesServDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IViewFinalidadesServMundo;
import com.servinte.axioma.orm.ViewFinalidadesServ;

public class ViewFinalidadesServMundo implements IViewFinalidadesServMundo{
	
	private IViewFinalidadesServDAO dao;
	
	
	public ViewFinalidadesServMundo(){
		dao = FacturacionFabricaDAO.crearViewFinalidadesServDAO();
	}

	@Override
	public List<ViewFinalidadesServ> obtenerViewFinalidadesServ(
			int codigoServicio) {
		return dao.obtenerViewFinalidadesServ(codigoServicio);
	}

	
	
	

}
