package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IGruposServiciosDAO;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.interfaz.facturacion.IGruposServiciosMundo;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.delegate.facturacion.GruposServiciosDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 8/09/2010
 */
public class GruposServiciosMundo implements IGruposServiciosMundo {
	
	IGruposServiciosDAO dao;
	
	public GruposServiciosMundo(){
		dao=FacturacionFabricaDAO.crearGruposServiciosDAO();
	}
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GruposServicios> buscarGruposServicioActivos(){
		return dao.buscarGruposServicioActivos();
	}

	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GruposServicios> buscarGruposServicio(){
		return dao.buscarGruposServicio();
	}
	@Override
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorConvenioPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarGruposServicioCierrePorNivelPorConvenioPorProceso(
						codigoConvenio, consecutivoNivel, proceso, meses);
	}

	@Override
	public ArrayList<GruposServicios> buscarGruposServicioCierrePorNivelPorContratoPorProceso(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarGruposServicioCierrePorNivelPorContratoPorProceso(
						codigoContrato, consecutivoNivel, proceso, meses);
	}
	
	/**
	 * Este M�todo se encarga de obtener el grupo de servicio 
	 * al cual est� asociado el servicio de la Orden. 
	 *  
	 * @author, Camilo G�mez
	 * 
	 * @param codServicio
	 * @return GruposServicios
	 */	
	public GruposServicios buscarGrupoServicioPorServicio(int codServicio){
		return dao.buscarGrupoServicioPorServicio(codServicio);
	}
	
	/**
	 * Este Metodo se encarga de consultar los grupos de servicio
	 * @author ginsotfu
	 * 
	 * @param 
	 * @return List<GrupoServicioDto>
	 */	
	public List<GrupoServicioDto> consultarGruposServicio() throws IPSException {
		List<GrupoServicioDto> listaGruposServicio=null;
		try{
			HibernateUtil.beginTransaction();
			GruposServiciosDelegate delegate = new GruposServiciosDelegate ();
			listaGruposServicio=delegate.consultarGruposServicio();
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaGruposServicio;
	}
}
