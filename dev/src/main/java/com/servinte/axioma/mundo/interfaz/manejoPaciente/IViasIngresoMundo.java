package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * Esta clase se encarga de definir los m�todos de 
 * negocio para la entidad Vias de Ingreso
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public interface IViasIngresoMundo {
	
	/**
	 * 
	 * Este M�todo se encarga de buscar todas las vias 
	 * de ingreso registradas en el sistema
	 * 
	 * @return ArrayList<ViasIngreso>
	 * @author, Angela Maria Aguirre
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
	
	
	/**
	 * Busca las vias de ingreso seg�n parametros.
	 * @param parametros
	 * @return ArrayList<DTOEstanciaViaIngCentroCosto>
	 *
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DTOEstanciaViaIngCentroCosto> buscarVias(DTOEstanciaViaIngCentroCosto parametros);

}
