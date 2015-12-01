package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IProgramasDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IProgramasMundo;
import com.servinte.axioma.orm.Programas;

/**
 * Esta clase se encarga de definir los m&eacute;todos para la entidad Programa
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 12/11/2010
 */
public class ProgramasMundo implements IProgramasMundo {

	IProgramasDAO dao;

	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ProgramasMundo() {
		dao = FacturacionFabricaDAO.crearProgramasDAO();
	}

	/**
	 * 
	 * M&eacute;todo encargado de listar los programas registrados en el sistema
	 * 
	 * @return ArrayList<Programas>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Programas> listarProgramas() {
		return dao.listarProgramas();
	}

}
