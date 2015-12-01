package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IViasIngresoDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IViasIngresoMundo;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad Vias de Ingreso
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public class ViasIngresoMundo implements IViasIngresoMundo 
{
	
	/**
	 * Instancia de  IViasIngresoDAO
	 */
	private IViasIngresoDAO dao;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public ViasIngresoMundo(){
		dao = ManejoPacienteDAOFabrica.crearViasIngresoDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar todas las vias 
	 * de ingreso registradas en el sistema
	 * 
	 * @return ArrayList<ViasIngreso>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ViasIngreso> buscarViasIngreso(){
		return dao.buscarViasIngreso();
	}

	/**
	 * Este M&eacute;todo se encarga de buscar vias
	 * de ingreso por c&oacute;digo
	 * @param id
	 * @return ViasIngreso
	 * @author Diana Carolina G
	 */
	@Override
	public ViasIngreso findbyId(int id) {
		return dao.findbyId(id);
	}
	

	@Override
	public ArrayList<DTOEstanciaViaIngCentroCosto> buscarVias(DTOEstanciaViaIngCentroCosto parametros) 
	{
		return dao.buscarVias(parametros);
	}


}
