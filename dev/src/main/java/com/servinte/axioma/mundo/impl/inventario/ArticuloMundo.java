package com.servinte.axioma.mundo.impl.inventario;

import java.sql.Connection;
import java.util.Collection;

import util.UtilidadBD;

import com.princetonsa.dao.ArticulosDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Articulo;
import com.servinte.axioma.mundo.interfaz.inventario.IArticuloMundo;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class ArticuloMundo implements IArticuloMundo {
	
	/**
	 * Comunicacion con la BD
	 */
	private ArticulosDao articulosDao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public ArticuloMundo(){
		
		articulosDao = DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")).getArticulosDao();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los artículos
	 * registrados en el sistema
	 * @param Connection con,String[] criteriosBusqueda, Articulo articulo
	 * @return Collection
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Collection realizarBusqueda(String[] criteriosBusqueda, Articulo articulo){
		Connection con = UtilidadBD.abrirConexion();
		Collection collection =  articulosDao.buscarArticulos(con,criteriosBusqueda,articulo.getClase(),
				articulo.getGrupo(),articulo.getSubgrupo(),articulo.getCodigo(),articulo.getDescripcion(),
				articulo.getNaturaleza(),articulo.getMinsalud(),articulo.getFormaFarmaceutica(),
				articulo.getUnidadMedida(),articulo.getConcentracion(), articulo.getEstadoArticulo());
		
		UtilidadBD.closeConnection(con);		
		
		return collection;
	}

}
