package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IProgramasMundo;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.servicio.interfaz.facturacion.IProgramasServicio;

/**
 * Esta clase se encarga de definir los m&eacute;todos para la entidad Programa
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 12/11/2010
 */
public class ProgramasServicio implements IProgramasServicio {

	IProgramasMundo mundo;

	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ProgramasServicio() {
		mundo = FacturacionFabricaMundo.crearProgramasMundo();
	}

	/**
	 * 
	 * M&eacute;todo encargado de listar los programas registrados en el sistema
	 * 
	 * @return ArrayList<Programas>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Programas> listarProgramas() {
		return mundo.listarProgramas();
	}

}
