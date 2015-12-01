package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAmparosPorReclamarDao;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaReclamaciones;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAmparosPorReclamarMundo;

public class AmparosPorReclamarMundo implements IAmparosPorReclamarMundo 
{
	IAmparosPorReclamarDao amparosPorReclamarDao;

	public AmparosPorReclamarMundo()
	{
		amparosPorReclamarDao=ManejoPacienteDAOFabrica.crearAmparosPorReclamarDao();
	}

	@Override
	public void insertarNuevaReclamacion(
			DtoReclamacionesAccEveFact amparoXReclamar) {
		amparosPorReclamarDao.insertarNuevaReclamacion(amparoXReclamar);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesFactura(int codigoFactura) {
		return amparosPorReclamarDao.consultarReclamacionesFactura(codigoFactura);
	}

	@Override
	public DtoReclamacionesAccEveFact consultarReclamacion(int codigoPk) {
		return amparosPorReclamarDao.consultarReclamacion(codigoPk);
	}
	
	@Override
	public boolean radicarReclamacion(DtoReclamacionesAccEveFact amparoXReclamar) {
		return amparosPorReclamarDao.radicarReclamacion(amparoXReclamar);
	}

	@Override
	public boolean anularReclamacion(DtoReclamacionesAccEveFact amparoXReclamar) {
		return amparosPorReclamarDao.anularReclamacion(amparoXReclamar);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesEventoCatastrofico(int codigo,boolean todas) 
	{
		return amparosPorReclamarDao.consultarReclamacionesEventoCatastrofico(codigo,todas);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesAccidenteTransito(int codigo,boolean todas) 
	{
		return amparosPorReclamarDao.consultarReclamacionesAccidenteTransito(codigo,todas);
	}

	@Override
	public ArrayList<DtoReclamacionesAccEveFact> consultarReclamacionesBusquedaAvanzada(
			DtoFiltroBusquedaAvanzadaReclamaciones filtro) {
		return amparosPorReclamarDao.consultarReclamacionesBusquedaAvanzada(filtro);
	}
}
