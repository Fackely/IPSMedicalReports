/**
 * 
 */
package com.servinte.axioma.dao.impl.inventario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.inventario.DtoClaseInventario;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.delegate.inventario.ClaseInventarioDelegate;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class ClaseInventarioHibernateDAO implements IClaseInventarioDAO {
	
	ClaseInventarioDelegate delegate = new ClaseInventarioDelegate();
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public ClaseInventarioHibernateDAO(){
		delegate = new ClaseInventarioDelegate();
	}	
	
	/**
	 * 	
	 * Este Método se encarga de consultar las clases de inventarios
	 * en el sistema
	 * 
	 * @return ArrayList<ClaseInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ClaseInventario> buscarClaseInventario(){
		return delegate.buscarClaseInventario();
	}

	
	@Override
	public DtoClaseInventario obtenerClaseInventarioPorSungrupo(int codigoSubgrupoInventario) 
	{
		return delegate.obtenerClaseInventarioPorSungrupo(codigoSubgrupoInventario);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO#buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return delegate.buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(
						codigoConvenio, consecutivoNivel, proceso, meses);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO#buscarClasesInventarioCierrePorNivelPorContratoPorProceso(int, long, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<ClaseInventario> buscarClasesInventarioCierrePorNivelPorContratoPorProceso(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return delegate.buscarClasesInventarioCierrePorNivelPorContratoPorProceso(
						codigoContrato, consecutivoNivel, proceso, meses);
	}

}
