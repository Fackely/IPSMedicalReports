package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntidadesFinancierasMundo;
import com.servinte.axioma.servicio.interfaz.tesoreria.IEntidadesFinancierasServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link IEntidadesFinancierasServicio}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class EntidadesFinancierasServicio  implements IEntidadesFinancierasServicio{

	private IEntidadesFinancierasMundo entidadesFinancierasMundo;
	
	public EntidadesFinancierasServicio() {
		entidadesFinancierasMundo =  TesoreriaFabricaMundo.crearEntidadesFinancierasMundo();
	}

	@Override
	public ArrayList<DtoEntidadesFinancieras> consultarEntidadesFinancieras(boolean todas) {
		
		return entidadesFinancierasMundo.consultarEntidadesFinancieras(todas);
	}

	@Override
	public List<DtoEntidadesFinancieras> obtenerEntidadesPorInstitucion(int codigoInstitucion, boolean activo) {
		
		return entidadesFinancierasMundo.obtenerEntidadesPorInstitucion(codigoInstitucion, activo);
	}
	
}
