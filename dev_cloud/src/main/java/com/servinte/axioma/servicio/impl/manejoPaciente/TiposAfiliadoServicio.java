package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposAfiliadoMundo;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposAfiliadoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad  Tipos de Afiliado
 * 
 * @author Angela Maria Aguirre
 * @since 30/08/2010
 */
public class TiposAfiliadoServicio implements ITiposAfiliadoServicio {
	
	/**
	 * Intancia de la clase ITiposAfiliadoMundo
	 */
	ITiposAfiliadoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public TiposAfiliadoServicio(){
		mundo = ManejoPacienteFabricaMundo.crearTiposAfiliadoMundo();
	}
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de 
	 * afiliados registrados en el sistema
	 * 
	 * @return ArrayList<TiposAfiliado>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposAfiliado> obtenerTiposAfiliado(){
		return mundo.obtenerTiposAfiliado();
	}	

}
