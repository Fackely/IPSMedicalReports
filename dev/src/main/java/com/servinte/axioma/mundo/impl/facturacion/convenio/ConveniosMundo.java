package com.servinte.axioma.mundo.impl.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import util.ValoresPorDefecto;

import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.servinte.axioma.dao.fabrica.facturacion.convenio.ConvenioFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.orm.Convenios;


/**
 * Mundo Convenio
 * Clase en donde se lleva todo la logica de negocio de convenios
 * @author Edgar Carvajal
 *
 */
public class ConveniosMundo  implements IConveniosMundo{

	
	/**
	 * Interfaz DAO
	 */
	private IConvenioDAO convenioDAO;
	
	
	/**
	 * Construtor
	 */
	public ConveniosMundo(){
		this.setConvenioDAO(ConvenioFabricaDAO.crearConvenioDAO());
	}
	
	
	@Override
	public List<DtoConvenio> listaConveniosOdontologicos(DtoConvenio dto) {
		return this.getConvenioDAO().listaConveniosOdontologicos(dto);
	}

	
	public void setConvenioDAO(IConvenioDAO convenioDAO) {
		this.convenioDAO = convenioDAO;
	}

	
	public IConvenioDAO getConvenioDAO() {
		return convenioDAO;
	}
	
	
	@Override
	public ArrayList<DtoConvenio> obtenerConveniosManejanMonto(){
		ArrayList<DtoConvenio> listaConvenios = 
			convenioDAO.obtenerConveniosManejanMonto();
		return listaConvenios;
	}	
	
	
	@Override
	public DtoConvenio buscarConvenio(int id){
		return convenioDAO.buscarConvenio(id);
	}

	
	@Override
	public ArrayList<DtoContrato> listarContratosConvenio(DtoConvenio convenio) {
		return convenioDAO.listarContratosConvenio(convenio);
	}

	
	@Override
	public ArrayList<Convenios> cargarConveniosParametrizadosPorDefecto() 
	{
		/*
			 No se recomienda de seguido el uso de este método ya que realiza doble consulta en la base de datos
			 Al consultar los convenios parametrizados por defecto en ValoresPorDefecto y luego volverlos a consultar
			 para convertirlos en una entidad del orm.
		 */
		ArrayList<HashMap<String, Object>> arrayConveniosParametrizadosPorDefecto = ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();
		
		if(arrayConveniosParametrizadosPorDefecto.size() > 0)
		{
			ArrayList<Convenios> listaConveniosPorDefecto = new ArrayList<Convenios>();
			
			for (HashMap<String, Object> hashMapConvenio : arrayConveniosParametrizadosPorDefecto) 
			{
				Convenios convenios;
				convenios = new Convenios();
				
				String codigoConvenio = hashMapConvenio.get("codigoConvenio").toString();
				convenios = convenioDAO.findById(Integer.parseInt(codigoConvenio));
				
				if(convenios != null){
					listaConveniosPorDefecto.add(convenios);
				}
			}
			
			return listaConveniosPorDefecto;
		}
		
		return null;
	}
	

	@Override
	public ArrayList<DtoConvenio> listarConveniosPaciente(int codPaciente, char acronimoEstadoActivo)
	{
		return convenioDAO.listarConveniosPaciente(codPaciente, acronimoEstadoActivo);
	}

	
	@Override
	public Convenios findById(int id) {
		return convenioDAO.findById(id);
	}

	
	@Override
	public ArrayList<Convenios> listarConveniosActivosInactivosOdont(
			int codInstitucion) {
		return convenioDAO.listarConveniosActivosInactivosOdont(codInstitucion);
	}

	
	@Override
	public ArrayList<Convenios> obtenerConvenioDetCargoPorSolicitud(int numeroSolicitud, int codInstitucion) {
		return convenioDAO.obtenerConvenioDetCargoPorSolicitud(numeroSolicitud, codInstitucion);
	}


	@Override
	public ArrayList<Convenios> obtenerConveniosPorIngresoPaciente(
			int codInstitucion, int codIngreso) {
		return convenioDAO.obtenerConveniosPorIngresoPaciente(codInstitucion, codIngreso);
	}

	
	@Override
	public ArrayList<Convenios> listarConveniosPorEmpresa(int codigoEmpresa) {
		return convenioDAO.listarConveniosPorEmpresa(codigoEmpresa);
	}


	@Override
	public ArrayList<DTOFacturasConvenios> obtenerValoresFacturadosConvenio(
			DtoReporteValoresFacturadosPorConvenio dto) {
		return convenioDAO.obtenerValoresFacturadosConvenio(dto);
	}


	@Override
	public ArrayList<Convenios> listarConveniosActivosPorInstitucion(int codInstitucion) {
		return convenioDAO.listarConveniosActivosPorInstitucion(codInstitucion);
	}


	@Override
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucion(int codInstitucion) {
		return convenioDAO.listarConveniosCapitadosActivosPorInstitucion(codInstitucion);
	}


	@Override
	public ArrayList<Convenios> obtenerConvenioPorUsuarioCapitado(
			int codPaciente) {
		return convenioDAO.obtenerConvenioPorUsuarioCapitado(codPaciente);
	}


	@Override
	public DtoConvenio obtenerTipoContratoConvenio(int codigo) {
		return convenioDAO.obtenerTipoContratoConvenio(codigo);
	}
	
	
	@Override
	public ArrayList<Convenios> listarConveniosPorInstitucion(int codInstitucion) {
		return convenioDAO.listarConveniosPorInstitucion(codInstitucion);
	}


	@Override
	public ArrayList<Convenios> listarConveniosCapitadosPorInstitucion(int codInstitucion) {
		return convenioDAO.listarConveniosCapitadosPorInstitucion(codInstitucion);
	}
	
	/**
	 * Lista los convenios activos en el sistema
	 *  
	 * @return ArrayList<Convenios>
	 * @author Camilo Gómez
	 */
	public ArrayList<Convenios> listarConveniosActivos(){
		return convenioDAO.listarConveniosActivos();
	}
	/**
	 * Lista los convenios activos en el sistema
	 *  
	 * @return ArrayList<Convenios>
	 * @author Camilo Gómez
	 */
	public ArrayList<Convenios> listarConvenios(){
		return convenioDAO.listarConvenios();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo#listarConveniosConParametrizacionPresupuestoPorInstitucionPorpitacion(int, char)
	 */
	@Override
	public ArrayList<Convenios> listarConveniosConParametrizacionPresupuestoPorInstitucionPorCapitacion(
			int codInstitucion, char esCapitacionSubcontratada, Calendar mesAnio) {
		return convenioDAO.listarConveniosConParametrizacionPresupuestoPorInstitucionPorCapitacion(codInstitucion, esCapitacionSubcontratada, mesAnio);
	}


	@Override
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(
			int codInstitucion) {
		return convenioDAO.listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(codInstitucion);
	}
}
