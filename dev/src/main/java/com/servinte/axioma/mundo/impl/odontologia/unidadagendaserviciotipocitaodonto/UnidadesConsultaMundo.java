
package com.servinte.axioma.mundo.impl.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.ArrayList;
import java.util.List;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.dao.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonDAOFabrica;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaMundo;
import com.servinte.axioma.orm.UnidadesConsulta;

/**
 * Define la l&oacute;gica de negocio relacionada con los objetos {@link UnidadesConsulta}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IUnidadesConsultaMundo
 */

public class UnidadesConsultaMundo implements IUnidadesConsultaMundo{

	
	IUnidadesConsultaDAO unidadesConsultaDAO;
	
	/**
	 * Constructor de la clase
	 */
	public UnidadesConsultaMundo() {
		inicializar();
	}
	
	/**
	 * M&eacute;todo que se encarga de inicializar el objeto DAO encargado de manejar 
	 * la capa de integraci&oacute;n de los objetos {@link UnidadesConsulta}
	 */
	private void inicializar() {
		unidadesConsultaDAO = UnidadAgendaServTipoCitaOdonDAOFabrica.crearUnidadesConsultaDAO();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaMundo#listarUnidadesAgendaPorTipoPorEstado(java.lang.String, boolean)
	 */
	@Override
	public List<UnidadesConsulta> listarUnidadesAgendaPorTipoPorEstado(	String tipoAtencion, boolean estado) {
		
		return unidadesConsultaDAO.listarUnidadesAgendaPorTipoPorEstado(tipoAtencion, estado);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaMundo#listarServiciosPorUnidadAgenda(int)
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorTarifarioPorEstado(int codigoUnidadAgenda, int codigoInstitucion, boolean estado) {

		/*
		 * Se tiene en cuenta lo definido en el par&aacute;metro general del m&oacute;dulo de 
		 * administraci&oacute;n - C&oacute;digo Manual Est&aacute;ndar B&uacute;squeda y Presentaci&oacute;n de Servicios
		 * para realizar la b&uacute;squeda
		 */

		List<DtoUnidadAgendaServCitaOdonto> listaServiciosPorUnidadAgenda = new ArrayList<DtoUnidadAgendaServCitaOdonto>();
		
		int codigoTipoTarifario = ConstantesBD.codigoTarifarioCups;
		
		String codigo = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
		
		if(UtilidadTexto.isNumber(codigo)){
			
			codigoTipoTarifario = Integer.parseInt(codigo);
		}
		
		if(codigoTipoTarifario>0){
			
			listaServiciosPorUnidadAgenda = (ArrayList<DtoUnidadAgendaServCitaOdonto>) unidadesConsultaDAO.listarServiciosPorUnidadAgendaPorEstado(codigoUnidadAgenda, estado);
			
			if (listaServiciosPorUnidadAgenda!=null && listaServiciosPorUnidadAgenda.size()>0){
				
				ArrayList<Integer> codigosServicio = new ArrayList<Integer>();
				ArrayList<Integer> codigosServicioInfo = new ArrayList<Integer>();
				
				for (DtoUnidadAgendaServCitaOdonto dtoUnidadAgendaServCitaOdonto : listaServiciosPorUnidadAgenda) {
					
					 codigosServicio.add(dtoUnidadAgendaServCitaOdonto.getCodigoServicio());
				}
				
				listaServiciosPorUnidadAgenda = unidadesConsultaDAO.obtenerInformacionServiciosPorUnidadAgendaPorTipoTarifario(codigoUnidadAgenda, codigoTipoTarifario, codigosServicio);
				
				for (DtoUnidadAgendaServCitaOdonto dtoUnidadAgendaServCitaOdonto : listaServiciosPorUnidadAgenda) {
					
					codigosServicioInfo.add(dtoUnidadAgendaServCitaOdonto.getCodigoServicio());
				}
				
				codigosServicio.removeAll(codigosServicioInfo);
				
				if(codigosServicio.size()>0){
					
					listaServiciosPorUnidadAgenda.addAll(unidadesConsultaDAO.obtenerInformacionServiciosPorUnidadAgendaPorTipoTarifario(codigoUnidadAgenda, ConstantesBD.codigoTarifarioCups, codigosServicio));
				}
			}

		}else{
			
			listaServiciosPorUnidadAgenda =  unidadesConsultaDAO.listarServiciosPorUnidadAgendaPorTarifarioPorEstado(codigoUnidadAgenda, codigoTipoTarifario, estado);
		}
			
		return listaServiciosPorUnidadAgenda;
	}

	@Override
	public UnidadesConsulta buscarUnidadConsultaId(int codigoUnidadConsulta) {
		return unidadesConsultaDAO.buscarUnidadConsultaId(codigoUnidadConsulta);
	}

	@Override
	public ArrayList<DtoUnidadesConsulta> cargarUnidadesConsultaTipoEspecialidad(
			String tipo, int codigoEspecialidad,int codigoCentroAtencion,boolean filtrarActivas) {
		return UtilidadesConsultaExterna.obtenerUnidadesAgendaXcentrosAtencionXEspecialidad(tipo,codigoEspecialidad,codigoCentroAtencion,filtrarActivas);
	}
}
