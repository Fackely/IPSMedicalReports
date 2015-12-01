/**
 * 
 */
package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;

/**
 * @author axioma
 *
 */
public interface IAmparosPorReclamarDao {

	void insertarNuevaReclamacion(DtoReclamacionesAccEveFact amparoXReclamar);

	/**
	 * 
	 * @param codigoFactura
	 */
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesFactura(int codigoFactura);

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public DtoReclamacionesAccEveFact consultarReclamacion(int codigoPk);

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	boolean radicarReclamacion(DtoReclamacionesAccEveFact amparoXReclamar);

	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	boolean anularReclamacion(DtoReclamacionesAccEveFact amparoXReclamar);

	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesEventoCatastrofico(int codigo,boolean todas);

	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesAccidenteTransito(int codigo,boolean todas);

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesBusquedaAvanzada(DtoFiltroBusquedaAvanzadaReclamaciones filtro);

}
