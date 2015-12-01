package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposAfiliado;

/**
 * Esta clase se encarga de definir los m�todos de 
 * negocio para la entidad  Tipos de Afiliado
 * @author Angela Maria Aguirre
 * @since 30/08/2010
 */
public interface ITiposAfiliadoServicio {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los tipos de 
	 * afiliados registrados en el sistema
	 * 
	 * @return ArrayList<TiposAfiliado>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposAfiliado> obtenerTiposAfiliado();

}
