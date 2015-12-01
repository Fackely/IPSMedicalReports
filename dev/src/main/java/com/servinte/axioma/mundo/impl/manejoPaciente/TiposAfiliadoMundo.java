package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposAfiliadoDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposAfiliadoMundo;
import com.servinte.axioma.orm.TiposAfiliado;

/**
 * Esta clase se encarga de ejecutar los m�todos de 
 * negocio para la entidad  Tipos de Afiliado
 * @author Angela Maria Aguirre
 * @since 30/08/2010
 */
public class TiposAfiliadoMundo implements ITiposAfiliadoMundo {
	/**
	 * Instancia de la entidad ITiposAfiliadoDAO
	 */
	ITiposAfiliadoDAO dao;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public TiposAfiliadoMundo(){
		dao = ManejoPacienteDAOFabrica.crearTiposAfiliadoDAO();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los tipos de 
	 * afiliados registrados en el sistema
	 * 
	 * @return ArrayList<TiposAfiliado>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposAfiliado> obtenerTiposAfiliado(){
		return dao.obtenerTiposAfiliado();
	}

	/**
	 * Metodo que se encarga de obtener la descripcion del
	 * acronimo de tipo Afiliado
	 * 
	 * @param tipoAfiliado
	 * @return String
	 */
	public String obtenerDescripcionTipoAfiliado(Character tipoAfiliado){
		return dao.obtenerDescripcionTipoAfiliado(tipoAfiliado);
	}
}
