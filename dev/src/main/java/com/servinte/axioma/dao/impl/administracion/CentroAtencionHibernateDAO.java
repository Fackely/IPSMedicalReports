package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dao.interfaz.administracion.ICentroAtencionDAO;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.delegate.administracion.CentroAtencionDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ICentroAtencionDAO}.
 * 
 * @author Cristhian Murillo
 * @see CentroAtencionDelegate.
 */

public class CentroAtencionHibernateDAO implements ICentroAtencionDAO{

	
	private CentroAtencionDelegate delegate = new CentroAtencionDelegate();

	
	@Override
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucion(int institucion) {
		return	delegate.listarTodosActivosPorInstitucion(institucion);
	}
	

	/**
	 * 	Retorna los activos de acuerdo a una insitucion y a una region
	 */
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucionYRegion(int institucion, long codRegion){
		return delegate.listarTodosActivosPorInstitucionYRegion(institucion, codRegion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosCentrosAtencion(){
		return delegate.listarTodosCentrosAtencion();
	}
	
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYCiudad(long empresaInstitucion, String codigoCiudad, 
			String codigoPais, String codigoDto ){
		return delegate.listarTodosPorEmpresaInstitucionYCiudad(empresaInstitucion, codigoCiudad, codigoPais, codigoDto);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYRegion(long empresaInstitucion, long codigoRegion ){
		return delegate.listarTodosPorEmpresaInstitucionYRegion(empresaInstitucion, codigoRegion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionIngresos (List<Integer> ingresos){
		return delegate.obtenerCentrosAtencionIngresos(ingresos);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorCiudad (String codigoCiudad, String codigoPais, String codigoDto ){
		return delegate.listarTodosPorCiudad(codigoCiudad, codigoPais, codigoDto);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucion(long empresaInstitucion){
		return delegate.listarTodosPorEmpresaInstitucion(empresaInstitucion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorRegion(long codigoRegion ){
		return delegate.listarTodosPorRegion(codigoRegion);
	}


	@Override
	public CentroAtencion findById(int id) {
		return delegate.findById(id);
	}


	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionPresupuestos(
			List<Long> presupuesto) {
		return delegate.obtenerCentrosAtencionPresupuestos(presupuesto);
	}


	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionRecibosCaja(
			List<RecibosCajaId> numeroRC) {
		return delegate.obtenerCentrosAtencionRecibosCaja(numeroRC);
	}

	/**
	 * M&eacute;todo encargado de obtener el listado de los centros de 
	 * atenci&oacute;n pertenecientes a un pa%iacute; determinado.
	 * 
	 * @param codigoPais
	 * @return listaCentro
	 *
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorPais (String codigoPais) {
		return delegate.listarTodosPorPais(codigoPais);
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.administracion.ICentroAtencionDAO#listarActivos()
	 */
	public ArrayList<CentroAtencion> listarActivos(){
		return delegate.listarActivos();
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.ICentroAtencionDAO#listarCentrosAtencionActivosUsuario(java.lang.String)
	 */
	@Override
	public ArrayList<CentroAtencion> listarCentrosAtencionActivosUsuario(
			String login) {
		return delegate.listarCentrosAtencionActivosUsuario(login);
	}
}
