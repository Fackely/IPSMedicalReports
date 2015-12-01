/**
 * 
 */
package com.servinte.axioma.mundo.impl.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.mundo.interfaz.inventario.IClaseInventarioMundo;
import com.servinte.axioma.orm.ClaseInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class ClaseInventarioMundo implements IClaseInventarioMundo{
	
	IClaseInventarioDAO dao;
	
	public ClaseInventarioMundo(){
		dao = InventarioDAOFabrica.crearClaseInventarioDAO();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar las clases de 
	 * inventario en el sistema
	 * 
	 * @return ArrayList<ClaseInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ClaseInventario> buscarClaseInventario(){
		return dao.buscarClaseInventario();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.inventario.IClaseInventarioMundo#buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(
						codigoConvenio, consecutivoNivel, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.inventario.IClaseInventarioMundo#buscarClasesInventarioCierrePorNivelPorContratoPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorContratoPorProceso(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarClasesInventarioCierrePorNivelPorContratoPorProceso(
						codigoContrato, consecutivoNivel, proceso, meses);
	}


}
