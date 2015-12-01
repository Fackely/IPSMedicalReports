package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.Programas;

/**
 * Esta clase se encarga de definir los m&eacute;todos para la entidad Programa
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 12/11/2010
 */
public interface IProgramasMundo {

	/**
	 * 
	 * M&eacute;todo encargado de listar los programas registrados en el sistema
	 * 
	 * @return ArrayList<Programas>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Programas> listarProgramas();

}
