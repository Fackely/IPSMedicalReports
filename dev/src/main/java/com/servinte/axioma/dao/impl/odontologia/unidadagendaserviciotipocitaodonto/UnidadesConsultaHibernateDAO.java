
package com.servinte.axioma.dao.impl.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.orm.delegate.odontologia.unidadAgendaServicioTipoCitaOdonto.UnidadesConsultaDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IUnidadesConsultaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see UnidadesConsultaDelegate
 */


public class UnidadesConsultaHibernateDAO implements IUnidadesConsultaDAO{

	UnidadesConsultaDelegate unidadesConsultaDelegate;
	
	/**
	 * Constructor de la Clase
	 */
	public UnidadesConsultaHibernateDAO() {
		
		unidadesConsultaDelegate = new UnidadesConsultaDelegate();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO#listarUnidadesAgendaPorTipoPorEstado(java.lang.String, boolean)
	 */
	@Override
	public List<UnidadesConsulta> listarUnidadesAgendaPorTipoPorEstado(String tipoAtencion, boolean estado) {

		return unidadesConsultaDelegate.listarUnidadesAgendaPorTipoPorEstado(tipoAtencion, estado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO#obtenerListaServiciosPorUnidadAgenda(int)
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorTarifarioPorEstado(int codigoUnidadAgenda, int codigoTarifario, boolean estado) {
		
		return unidadesConsultaDelegate.listarServiciosPorUnidadAgendaPorTarifarioPorEstado(codigoUnidadAgenda, codigoTarifario, estado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO#listarServiciosPorUnidadAgendaPorEstado(int, boolean)
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorEstado(int codigoUnidadAgenda, boolean estado) {

		return unidadesConsultaDelegate.listarServiciosPorUnidadAgendaPorEstado(codigoUnidadAgenda, estado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO#obtenerInformacionServiciosPorTipoTarifario(int, java.util.ArrayList)
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> obtenerInformacionServiciosPorUnidadAgendaPorTipoTarifario(int codigoUnidadAgenda, int codigoTarifario, ArrayList<Integer> codigosServicio) {
		
		return unidadesConsultaDelegate.obtenerInformacionServiciosPorUnidadAgendaPorTipoTarifario(codigoUnidadAgenda, codigoTarifario, codigosServicio);
	}

	
	
	@Override
	public UnidadesConsulta buscarUnidadConsultaId(int codigoUnidadConsulta) {
			
		UnidadesConsulta unidadConsulta= new UnidadesConsulta();
	
		try {
			
			unidadConsulta=unidadesConsultaDelegate.findById(codigoUnidadConsulta);
			
			if(unidadConsulta!=null){
				
				unidadConsulta.getEspecialidades().getCodigo();
			}
			
			
		} catch (HibernateException e) {
			
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
	
	return unidadConsulta;
	}


	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de las unidades de consulta
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return List
	 * @author Diana Ruiz
	 * @since 23/06/2011
	 * 
	 */
	@Override
	public ArrayList<DtoUnidadesConsulta> listaUnidadesConsulta(int codServicio){
		return unidadesConsultaDelegate.listaUnidadesConsulta(codServicio);
	}

	/**
	 * 
	 * Lista todas las unidades de Consulta
	 * @return ArrayList
	 * @author Cesar Gomez
	 * @since 02/03/2012
	 * 
	 */
	@Override
	public ArrayList<DtoUnidadesConsulta> listaTodoUnidadesConsulta(){
		return unidadesConsultaDelegate.listaTodoUnidadesConsulta();
	}

}
