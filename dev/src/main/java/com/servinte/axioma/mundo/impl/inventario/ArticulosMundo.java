package com.servinte.axioma.mundo.impl.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.inventario.DtoArticulos;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo;
import com.servinte.axioma.orm.Articulo;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class ArticulosMundo implements IArticulosMundo {
	
	private IArticuloDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public ArticulosMundo(){
		
		dao = InventarioDAOFabrica.crearArticuloDAO();
	}
	
	/**
	 * Metodo de la super clase finById
	 * @param id
	 * @return Articulo
	 */
	public Articulo obtenerArticuloPorId(int id){
		return dao.obtenerArticuloPorId(id);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de obtener los codigos de los articulos por su tipo de codigo
	 * de medicamentos e insumos 
	 * @param tipoCodigoMed tipo de codigo Axioma o Interfaz 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<String> consultarCodigosArticulosPorTipoCodigo(String tipoCodigoMed){
		return dao.consultarCodigosArticulosPorTipoCodigo(tipoCodigoMed);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo#buscarArticulosCierrePorNivelPorConvenioPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorConvenioPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarArticulosCierrePorNivelPorConvenioPorProceso(
						codigoConvenio, consecutivoNivel, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo#buscarArticulosCierrePorNivelPorContratoPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorContratoPorProceso(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarArticulosCierrePorNivelPorContratoPorProceso(
						codigoContrato, consecutivoNivel, proceso, meses);
	}

	/**
	 * Este Método se encarga de consultar la tarifa vigente de un articulo
	 * @return DtoArticulos Dto que almacena la información del articulo y su tarifa 
	 * @author, Fabian Becerra
	 */
	@SuppressWarnings("unchecked")
	public DtoArticulos obtenerTarifaVigenteArticulos(int codigoArticulo,int esquemaTarifario){
		return dao.obtenerTarifaVigenteArticulos(codigoArticulo, esquemaTarifario);
	}
}
