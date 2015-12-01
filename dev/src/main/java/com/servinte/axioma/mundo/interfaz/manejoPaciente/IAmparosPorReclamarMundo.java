/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;

/**
 * @author axioma
 *
 */
public interface IAmparosPorReclamarMundo {

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
	 * @param amparoXReclamar
	 * @return
	 */
	boolean radicarReclamacion(DtoReclamacionesAccEveFact amparoXReclamar);

	/**
	 * 
	 * @param amparoXReclamar
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
