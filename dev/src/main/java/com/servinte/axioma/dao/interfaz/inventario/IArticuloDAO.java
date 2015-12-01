package com.servinte.axioma.dao.interfaz.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.inventario.DtoArticulos;
import com.servinte.axioma.orm.Articulo;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IArticuloDAO
 * 
 * @author Cristhian Murillo
 */
public interface IArticuloDAO 
{
	
	/**
	 * Metodo de la super clase finById
	 * @param id
	 * @return Articulo
	 */
	public Articulo obtenerArticuloPorId(int id);
	
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DtoArticulos consultarArticuloPorID(int id);
	
	/**
	 * 
	 * Este Método se encarga de obtener los codigos de los articulos por su tipo de codigo
	 * de medicamentos e insumos 
	 * @param tipoCodigoMed tipo de codigo Axioma o Interfaz 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<String> consultarCodigosArticulosPorTipoCodigo(String tipoCodigoMed);
	
	
	/**
	 * Este Método se encarga de obtener los distintos articulos
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un convenio.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Articulo>
	 * 
	 */
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
										long consecutivoNivel, String proceso, List<Calendar> meses);
	
	/**
	 * Este Método se encarga de obtener los distintos articulos
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Articulo>
	 * 
	 */
	public ArrayList<Articulo> buscarArticulosCierrePorNivelPorContratoPorProceso(int codigoContrato, 
										long consecutivoNivel, String proceso, List<Calendar> meses);
	
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente de un articulo
	 * @return DtoArticulos Dto que almacena la información del articulo y su tarifa 
	 * @author, Fabian Becerra
	 */
	public DtoArticulos obtenerTarifaVigenteArticulos(int codigoArticulo,int esquemaTarifario);
	
}
