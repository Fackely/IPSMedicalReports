package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dao.interfaz.facturacion.IGruposServiciosDAO;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.delegate.facturacion.GruposServiciosDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 8/09/2010
 */
public class GruposServiciosHibernateDAO implements IGruposServiciosDAO {
	
	GruposServiciosDelegate delegate;
	
	public GruposServiciosHibernateDAO(){
		delegate = new GruposServiciosDelegate();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GruposServicios> buscarGruposServicioActivos(){
		return delegate.buscarGruposServicioActivos();
	}
	
	
	@Override
	public ArrayList<GruposServicios> buscarGruposServicio() {
		return delegate.buscarGruposServicio();
	}

	@Override
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorConvenioPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return delegate.buscarGruposServicioCierrePorNivelPorConvenioPorProceso(
							codigoConvenio, consecutivoNivel, proceso, meses);
	}

	@Override
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorContratoPorProceso(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return delegate.buscarGruposServicioCierrePorNivelPorContratoPorProceso(
						codigoContrato, consecutivoNivel, proceso, meses);
	}

	/**
	 * Este Método se encarga de obtener el grupo de servicio 
	 * al cual está asociado el servicio de la Orden. 
	 *  
	 * @author, Camilo Gómez
	 * 
	 * @param codServicio
	 * @return GruposServicios
	 */	
	public GruposServicios buscarGrupoServicioPorServicio(int codServicio){
		return delegate.buscarGrupoServicioPorServicio(codServicio);
	}
}
