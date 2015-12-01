package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroAtencionDAO;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.RecibosCajaId;


public class CentroAtencionMundo implements ICentroAtencionMundo
{
	
	ICentroAtencionDAO dao;
	
	
	public CentroAtencionMundo() {
		dao = AdministracionFabricaDAO.crearCentroAtencionDAO();
	}

	
	@Override
	public CentroAtencion buscarPorCodigo(int codigo)
	{
		return dao.findById(codigo);
	}
	

	@Override
	public ArrayList<DtoCentrosAtencion> listarCentrosAtencion(boolean activo)
	{
		DtoCentrosAtencion dtoCentrosAtencion=new DtoCentrosAtencion();
		dtoCentrosAtencion.setActivo(activo);
		ArrayList<DtoCentrosAtencion> tmpLista=UtilidadesManejoPaciente.obtenerCentrosAtencion(dtoCentrosAtencion);

		return tmpLista;
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarCentrosAtencion(DtoCentrosAtencion dtoCentrosAtencion)
	{
		ArrayList<DtoCentrosAtencion> tmpLista=UtilidadesManejoPaciente.obtenerCentrosAtencion(dtoCentrosAtencion);
		return tmpLista;
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionIngresos (List<Integer> ingresos){
		return dao.obtenerCentrosAtencionIngresos(ingresos);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorCiudad (String codigoCiudad, String codigoPais, String codigoDto ){
		return dao.listarTodosPorCiudad(codigoCiudad, codigoPais, codigoDto);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucion(long empresaInstitucion){
		return dao.listarTodosPorEmpresaInstitucion(empresaInstitucion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorRegion(long codigoRegion ){
		return dao.listarTodosPorRegion(codigoRegion);
	}

	@Override
	public CentroAtencion findById(int id) {
		return dao.findById(id);
	}


	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionPresupuestos(
			List<Long> presupuesto) {
		return dao.obtenerCentrosAtencionPresupuestos(presupuesto);
	}


	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionRecibosCaja(
			List<RecibosCajaId> numeroRC) {
		return dao.obtenerCentrosAtencionRecibosCaja(numeroRC);
	}


	/**
	 * @see com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo#listarActivos()
	 */
	public ArrayList<CentroAtencion> listarActivos(){
		return dao.listarActivos();
		
	}

	/**
	 * Este m&eacute;todo se encarga de retornar el listado con
	 * todos los centros de atenci&oacute;n existentes en el sistema. 
	 * 
	 * @return listaCentro listado de todos los centros de atenci&oacute;n.
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosCentrosAtencion(){
		return dao.listarTodosCentrosAtencion();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo#listarCentrosAtencionActivosUsuario(java.lang.String)
	 */
	@Override
	public ArrayList<CentroAtencion> listarCentrosAtencionActivosUsuario(
			String login) {
		return dao.listarCentrosAtencionActivosUsuario(login);
	}
}
