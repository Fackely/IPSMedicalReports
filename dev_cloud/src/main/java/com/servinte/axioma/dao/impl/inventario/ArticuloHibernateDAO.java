package com.servinte.axioma.dao.impl.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.inventario.DtoArticulos;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.delegate.inventario.ArticuloDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IArticuloDAO}.
 * @author Cristhian Murillo
 */
public class ArticuloHibernateDAO implements IArticuloDAO 
{
	
	private ArticuloDelegate articuloDelegate;

	
	/**
	 * Constructor
	 */
	public ArticuloHibernateDAO(){
		articuloDelegate = new ArticuloDelegate();
	}


	@Override
	public Articulo obtenerArticuloPorId(int id) {
		return articuloDelegate.obtenerArticuloPorId(id);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DtoArticulos consultarArticuloPorID(int id){
		return articuloDelegate.consultarArticuloPorID(id);
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
		return articuloDelegate.consultarCodigosArticulosPorTipoCodigo(tipoCodigoMed);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO#buscarArticulosCierrePorNivelPorConvenioPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorConvenioPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return articuloDelegate.buscarArticulosCierrePorNivelPorConvenioPorProceso(
						codigoConvenio, consecutivoNivel, proceso, meses);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO#buscarArticulosCierrePorNivelPorContratoPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorContratoPorProceso(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return articuloDelegate.buscarArticulosCierrePorNivelPorContratoPorProceso(
						codigoContrato, consecutivoNivel, proceso, meses);
	}
	
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente de un articulo
	 * @return DtoArticulos Dto que almacena la información del articulo y su tarifa 
	 * @author, Fabian Becerra
	 */
	public DtoArticulos obtenerTarifaVigenteArticulos(int codigoArticulo,int esquemaTarifario){
		return articuloDelegate.obtenerTarifaVigenteArticulos(codigoArticulo, esquemaTarifario);
	}
	

}
