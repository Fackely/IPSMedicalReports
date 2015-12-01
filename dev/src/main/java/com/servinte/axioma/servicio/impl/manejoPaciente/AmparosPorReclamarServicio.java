/**
 * 
 */
package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAmparosPorReclamarMundo;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAmparosPorReclamarServicio;

/**
 * @author axioma
 *
 */
public class AmparosPorReclamarServicio implements IAmparosPorReclamarServicio 
{
	IAmparosPorReclamarMundo AmparosPorReclamarMundo;

	/**
	 * 
	 */
	public AmparosPorReclamarServicio() 
	{
		AmparosPorReclamarMundo = ManejoPacienteFabricaMundo.crearAmparosPorReclamarMundo();
	}

	@Override
	public void insertarNuevaReclamacion(
			DtoReclamacionesAccEveFact amparoXReclamar) {
		AmparosPorReclamarMundo.insertarNuevaReclamacion(amparoXReclamar);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesFactura(int codigoFactura) {
		return AmparosPorReclamarMundo.consultarReclamacionesFactura(codigoFactura);
	}

	@Override
	public DtoReclamacionesAccEveFact consultarReclamacion(int codigoPk) {
		return AmparosPorReclamarMundo.consultarReclamacion(codigoPk);
	}

	@Override
	public boolean radicarReclamacion(DtoReclamacionesAccEveFact amparoXReclamar) {
		return AmparosPorReclamarMundo.radicarReclamacion(amparoXReclamar);
	}

	@Override
	public boolean anularReclamacion(DtoReclamacionesAccEveFact amparoXReclamar) {
		return AmparosPorReclamarMundo.anularReclamacion(amparoXReclamar);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesEventoCatastrofico(
			int codigo,boolean todas) {
		return AmparosPorReclamarMundo.consultarReclamacionesEventoCatastrofico(codigo,todas);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesAccidenteTransito(
			int codigo,boolean todas) {
		return AmparosPorReclamarMundo.consultarReclamacionesAccidenteTransito(codigo,todas);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesBusquedaAvanzada(
			DtoFiltroBusquedaAvanzadaReclamaciones filtro) {
		return AmparosPorReclamarMundo.consultarReclamacionesBusquedaAvanzada(filtro);
	}
}
