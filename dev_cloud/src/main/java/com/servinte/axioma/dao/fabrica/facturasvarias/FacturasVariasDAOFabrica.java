package com.servinte.axioma.dao.fabrica.facturasvarias;

import com.servinte.axioma.dao.impl.facturasVarias.FacturasVariasDAO;
import com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO;

public class FacturasVariasDAOFabrica {

	public static IFacturasVariasDAO crearFacturasVariasDAO() {
		return new FacturasVariasDAO();
	}

}
