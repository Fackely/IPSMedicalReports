/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.orm.ClaseInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface IClaseInventarioMundo {
	
	/**
	 * 	
	 * Este Método se encarga de consultar las clases de 
	 * inventario en el sistema
	 * 
	 * @return ArrayList<ClaseInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ClaseInventario> buscarClaseInventario();	
	
	/**
	 * Este Método se encarga de obtener las distintas clases de Inventario
	 * que tienen asociado un articulo el cual se encuentra en el cierre de presupuesto
	 * para un nivel de atención para un convenio.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<ClaseInventario>
	 * 
	 */
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
										long consecutivoNivel, String proceso, List<Calendar> meses);
	
	/**
	 * Este Método se encarga de obtener las distintas clases de Inventario
	 * que tienen asociado un articulo el cual se encuentra en el cierre de presupuesto
	 * para un nivel de atención para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<ClaseInventario>
	 * 
	 */
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorContratoPorProceso(int codigoContrato, 
										long consecutivoNivel, String proceso, List<Calendar> meses);

}
