/**
 * 
 */
package com.servinte.axioma.dao.interfaz.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.inventario.DtoClaseInventario;
import com.servinte.axioma.orm.ClaseInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface IClaseInventarioDAO {
	
	/**
	 * 	
	 * Este Método se encarga de consultar las clases de inventarios
	 * en el sistema
	 * 
	 * @return ArrayList<ClaseInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ClaseInventario> buscarClaseInventario();
	
	
	 /** 	
	 * Este Método se encarga de obtener la calse inventario partiendo del subgrupo inventario (Tabla: subgrupo_inventario) dado.
	 * El valor de codigoSubgrupoInventario NO ES LA LLAVE PRIMARIA, es el campo subgrupo (único de subgrupo_inventario)
	 * 
	 * @param subgrupo
	 * @return DtoClaseInventario
	 * @author Cristhian Murillo
	 */
	public DtoClaseInventario obtenerClaseInventarioPorSungrupo(int codigoSubgrupoInventario);
	
	
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
