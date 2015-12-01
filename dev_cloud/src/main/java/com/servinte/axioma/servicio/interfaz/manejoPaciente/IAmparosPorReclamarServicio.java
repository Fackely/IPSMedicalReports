package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;

public interface IAmparosPorReclamarServicio 
{

	public void insertarNuevaReclamacion(DtoReclamacionesAccEveFact amparoXReclamar);

	/**
	 * 
	 * @param codigoFactura
	 * @return 
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
	 */
	public boolean radicarReclamacion(DtoReclamacionesAccEveFact amparoXReclamar);

	/**
	 * 
	 * @param amparoXReclamar
	 */
	public boolean anularReclamacion(DtoReclamacionesAccEveFact amparoXReclamar);

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
