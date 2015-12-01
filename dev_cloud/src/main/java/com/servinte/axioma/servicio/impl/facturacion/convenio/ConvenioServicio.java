package com.servinte.axioma.servicio.impl.facturacion.convenio;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.servinte.axioma.mundo.fabrica.odontologia.facturacion.convenio.ConveniosFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;


/**
 * Esta clase se encarga de ejecutar los métodos de
 * negocio para la entidad Convenios
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class ConvenioServicio implements IConvenioServicio {

	/**
	 * Interfaz Convenio Mundo
	 */
	private IConveniosMundo convenioMundo;
	
	/**
	 *Construtor 
	 */
	public ConvenioServicio(){
		this.convenioMundo=ConveniosFabricaMundo.crearConveniosMundo();
	}

	public void setConvenioMundo(IConveniosMundo convenioMundo) {
		this.convenioMundo = convenioMundo;
	}

	public IConveniosMundo getConvenioMundo() {
		return convenioMundo;
	}

	@Override
	public List<DtoConvenio> listaConveniosOdontologicos(DtoConvenio dto) {
		return convenioMundo.listaConveniosOdontologicos(dto);
	}
	
	
	@Override
	public ArrayList<DtoConvenio> obtenerConveniosManejanMonto(){
		return convenioMundo.obtenerConveniosManejanMonto();
	}
	
	
	@Override
	public DtoConvenio buscarConvenio(int id){
		return convenioMundo.buscarConvenio(id);
	}

	
	@Override
	public ArrayList<DtoContrato> listarContratosConvenio(DtoConvenio convenio) {
		return convenioMundo.listarContratosConvenio(convenio);
	}
	

	@Override
	public ArrayList<Convenios> cargarConveniosParametrizadosPorDefecto() {
		return convenioMundo.cargarConveniosParametrizadosPorDefecto();
	}


	@Override
	public ArrayList<DtoConvenio> listarConveniosPaciente(int codPaciente,
			char acronimoEstadoActivo)
	{
		return convenioMundo.listarConveniosPaciente(codPaciente, acronimoEstadoActivo);
	}
	

	@Override
	public ArrayList<Convenios> listarConveniosActivosInactivosOdont(
			int codInstitucion) {
		return convenioMundo.listarConveniosActivosInactivosOdont(codInstitucion);
	}
	
	
	@Override
	public ArrayList<Convenios> obtenerConvenioDetCargoPorSolicitud(int numeroSolicitud, int codInstitucion) {
		return convenioMundo.obtenerConvenioDetCargoPorSolicitud(numeroSolicitud, codInstitucion);
	}

	@Override
	public Convenios findById(int id){
		return convenioMundo.findById(id);
	}
	

	@Override
	public ArrayList<Convenios> obtenerConveniosPorIngresoPaciente(
			int codInstitucion, int codIngreso) {
		return convenioMundo.obtenerConveniosPorIngresoPaciente(codInstitucion, codIngreso);
	}

	
	@Override
	public ArrayList<Convenios> listarConveniosPorEmpresa(int codigoEmpresa) {
		return convenioMundo.listarConveniosPorEmpresa(codigoEmpresa);
	}


	@Override
	public ArrayList<DTOFacturasConvenios> obtenerValoresFacturadosConvenio(
			DtoReporteValoresFacturadosPorConvenio dto) {
		return convenioMundo.obtenerValoresFacturadosConvenio(dto);
	}

	
	@Override
	public ArrayList<Convenios> listarConveniosActivosPorInstitucion(int codInstitucion) {
		return convenioMundo.listarConveniosActivosPorInstitucion(codInstitucion);
	}

	
	@Override
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucion(int codInstitucion) {
		return convenioMundo.listarConveniosCapitadosActivosPorInstitucion(codInstitucion);
	}

}
