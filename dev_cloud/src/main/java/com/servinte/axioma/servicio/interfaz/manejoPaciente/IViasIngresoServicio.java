package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.ViasIngreso;

/**
 * Esta clase se encarga de definir los métodos de 
 * negocio para la entidad Vias de Ingreso
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public interface IViasIngresoServicio {
	
	/**
	 * 
	 * Este Método se encarga de buscar todas las vias 
	 * de ingreso registradas en el sistema
	 * 
	 * @return ArrayList<ViasIngreso>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ViasIngreso> buscarViasIngreso();
	
	
	/**
	 * Este M&eacute;todo se encarga de buscar vias
	 * de ingreso por c&oacute;digo
	 * @param id
	 * @return ViasIngreso
	 * @author Diana Carolina G
	 */
	public ViasIngreso findbyId(int id);


}
