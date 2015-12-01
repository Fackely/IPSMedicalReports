package com.servinte.axioma.dao.impl.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConvenioDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConveniosIngresoPacienteDelegate;


/**
 * Esta clase se encarga de ejecutar los métodos de
 * negocio para la entidad Convenios
 * 
 * @author Edgar Carvajal
 *
 */
public class ConvenioDAO implements IConvenioDAO  {
	
	
	/**
	 * Delegado
	 */
	private ConvenioDelegate delegate;
	
	
	
	/**
	 * Construtor
	 */
	public ConvenioDAO(){
		delegate= new ConvenioDelegate();
	}
	
	
	@Override
	public List<DtoConvenio> listaConveniosOdontologicos(DtoConvenio dto) {
		
		List<DtoConvenio>  listaConvenio = new ArrayList<DtoConvenio>() ;
		
		try{
			//listaConvenio = delegate.listaConveniosOdontologicos();
			// FIXME Arreglar esto cuando llegue edgar carvajal
			listaConvenio = null;
		}
		catch (HibernateException e) {
			
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return listaConvenio;
	}

	
	@Override
	public Convenios buscarxId(Number id) {
		Log4JManager.info("Este metodo es null, no hace nada");
		return null;
	}

	
	@Override
	public void eliminar(Convenios objeto) {
		// TODO Auto-generated method stub
		Log4JManager.info("Este metodo es null, no hace nada");
	}

	
	@Override
	public void insertar(Convenios objeto) {
		// TODO Auto-generated method stub
		Log4JManager.info("Este metodo es null, no hace nada");
	}

	@Override
	public void modificar(Convenios objeto) {
		// TODO Auto-generated method stub
		Log4JManager.info("Este metodo es null, no hace nada");
	}
	
	@Override
	public ArrayList<DtoConvenio> obtenerConveniosManejanMonto(){
		return delegate.obtenerConveniosManejanMonto();
	}
	
	@Override
	public DtoConvenio buscarConvenio(int id){
		return delegate.buscarConvenio(id);
	}

	@Override
	public ArrayList<DtoContrato> listarContratosConvenio(DtoConvenio convenio) {
		return delegate.listarContratosConvenio(convenio);
	}
	
	@Override
	public ArrayList<DtoConvenio> listarConveniosPaciente(int codPaciente, char acronimoEstadoActivo)
	{
		return new ConveniosIngresoPacienteDelegate().obtenerConveniosIngresoPaciente(codPaciente, acronimoEstadoActivo);
	}


	@Override
	public Convenios findById(int id) {
		return delegate.findById(id);
	}


	@Override
	public ArrayList<Convenios> listarConveniosActivosInactivosOdont(
			int codInstitucion) {
		return delegate.listarConveniosActivosInactivosOdont(codInstitucion);
	}


	@Override
	public ArrayList<Convenios> obtenerConvenioDetCargoPorSolicitud(
			int numeroSolicitud, int codInstitucion) {
		return delegate.obtenerConvenioDetCargoPorSolicitud(numeroSolicitud, codInstitucion);
	}


	@Override
	public ArrayList<Convenios> obtenerConveniosPorIngresoPaciente(
			int codInstitucion, int codIngreso) {
		return delegate.obtenerConveniosPorIngresoPaciente(codInstitucion, codIngreso);
	}
	

	@Override
	public ArrayList<Convenios> listarConveniosPorEmpresa(int codigoEmpresa) {
		return delegate.listarConveniosPorEmpresa(codigoEmpresa);
	}


	@Override
	public ArrayList<DTOFacturasConvenios> obtenerValoresFacturadosConvenio(
			DtoReporteValoresFacturadosPorConvenio dto) {
		return delegate.obtenerValoresFacturadosConvenio(dto);
	}


	@Override
	public ArrayList<Convenios> listarConveniosActivosPorInstitucion(int codInstitucion) {
		return delegate.listarConveniosActivosPorInstitucion(codInstitucion);
	}


	@Override
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucion(int codInstitucion) {
		return delegate.listarConveniosCapitadosActivosPorInstitucion(codInstitucion);
	}
	
	@Override
	public ArrayList<Convenios> listarConveniosPorInstitucion(int codInstitucion) {
		return delegate.listarConveniosPorInstitucion(codInstitucion);
	}

	@Override
	public ArrayList<DtoConvenio> listarTodosConveniosCapitadosPorInstitucion(int codInstitucion){
		return delegate.listarTodosConveniosCapitadosPorInstitucion(codInstitucion);
	}
	
	@Override
	public ArrayList<Convenios> listarConveniosCapitadosPorInstitucion(int codInstitucion) {
		return delegate.listarConveniosCapitadosPorInstitucion(codInstitucion);
	}

	@Override
	public ArrayList<Convenios> obtenerConvenioPorUsuarioCapitado(
			int codPaciente) {
		return delegate.obtenerConvenioPorUsuarioCapitado(codPaciente);
	}

	@Override
	public DtoConvenio obtenerTipoContratoConvenio(int codigo) {
		return delegate.obtenerTipoContratoConvenio(codigo);
	}
	
	
	/**
	 * Lista los convenios activos en el sistema
	 *  
	 * @return ArrayList<Convenios>
	 * @author Camilo Gómez
	 */
	public ArrayList<Convenios> listarConveniosActivos(){
		return delegate.listarConveniosActivos();
	}


	@Override
	public ArrayList<Convenios> listarConveniosConParametrizacionPresupuestoPorInstitucionPorCapitacion(
			int codInstitucion, char esCapitacionSubcontratada, Calendar mesAnio) {
		return delegate.listarConveniosConParametrizacionPresupuestoPorInstitucionPorCapitacion(codInstitucion, esCapitacionSubcontratada, mesAnio);
	}


	@Override
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(
			int codInstitucion) {
		return delegate.listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(codInstitucion);
	}


	@Override
	public ArrayList<Convenios> listarConvenios() {
		// 
		return delegate.listarConvenios();
	}

}
