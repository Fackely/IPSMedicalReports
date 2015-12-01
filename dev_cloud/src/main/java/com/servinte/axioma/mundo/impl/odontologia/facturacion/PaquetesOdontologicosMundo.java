package com.servinte.axioma.mundo.impl.odontologia.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.odontologia.facturacion.PaquetesOdontologicosFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.facturacion.IPaquetesOdontologicosDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.facturacion.IPaquetesOdontologicosMundo;
import com.servinte.axioma.orm.PaquetesOdontologicos;

public class PaquetesOdontologicosMundo implements IPaquetesOdontologicosMundo {
	
	IPaquetesOdontologicosDAO dao;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PaquetesOdontologicosMundo() {
		dao = PaquetesOdontologicosFabricaDAO.crearPaquetesOdontologicosDAO();
	}
	
	@Override
	public ArrayList<PaquetesOdontologicos> listarPaquetesOdontologicos(String codigoPaquete, String descripcionPaquete, int codigoEspecialidad) {
		return dao.listarPaquetesOdontologicos(codigoPaquete, descripcionPaquete, codigoEspecialidad);
	}

}
