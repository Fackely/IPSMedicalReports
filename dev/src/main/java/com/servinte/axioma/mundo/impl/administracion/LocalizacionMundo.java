package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IBarriosDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroAtencionDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICiudadesDAO;
import com.servinte.axioma.dao.interfaz.administracion.IInstitucionesDAO;
import com.servinte.axioma.dao.interfaz.administracion.ILocalidadesDAO;
import com.servinte.axioma.dao.interfaz.administracion.IPaisesDAO;
import com.servinte.axioma.dao.interfaz.administracion.IRegionesCoberturaDAO;
import com.servinte.axioma.dto.administracion.DtoBarrio;
import com.servinte.axioma.dto.administracion.DtoCiudad;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.dto.administracion.DtoLocalidad;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ITiposIdentificacionMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;


/**
 * Contiene la l&oacute;gica de Negocio
 * 
 * @author Cristhian Murillo
 * @see ITiposIdentificacionMundo
 */

public class LocalizacionMundo implements ILocalizacionMundo{

	
	private IPaisesDAO paisesDAO;
	private IBarriosDAO barriosDAO;
	private ICiudadesDAO ciudadesDAO;
	private IRegionesCoberturaDAO regionesCoberturaDAO;
	private IInstitucionesDAO institucionesDAO;
	private ICentroAtencionDAO centroAtencionDAO;
	private ILocalidadesDAO localidadesDAO;

	
	public LocalizacionMundo() {
		inicializar();
	}

	private void inicializar() {
		paisesDAO				= AdministracionFabricaDAO.crearPaisesDAO();
		ciudadesDAO				= AdministracionFabricaDAO.crearciCiudadesDAO();
		regionesCoberturaDAO	= AdministracionFabricaDAO.crearregCoberturaDAO();
		institucionesDAO		= AdministracionFabricaDAO.crearInstitucionesDAO();
		centroAtencionDAO		= AdministracionFabricaDAO.crearCentroAtencionDAO();
		barriosDAO				= AdministracionFabricaDAO.crearBarriosDAO();
		localidadesDAO			= AdministracionFabricaDAO.crearLocalidadesDAO();
	}

	
	@Override
	public ArrayList<Ciudades> listarCiudades() {
		return ciudadesDAO.listarCiudades();
	}

	@Override
	public ArrayList<Ciudades> listarCiudadesPorPais(String codigoPais) {
		return ciudadesDAO.listarCiudadesPorPais(codigoPais);
	}
	
	@Override
	public ArrayList<DtoCiudades> listarCiudadesDtoPorPais(String codigoPais)
	{
		return ciudadesDAO.listarCiudadesDtoPorPais(codigoPais);
	}

	@Override
	public ArrayList<Paises> listarPaises() {
		return paisesDAO.listarPaises();
	}
	
	@Override
	public ArrayList<DtoPaises> listarPaisesDto()
	{
		return paisesDAO.listarPaisesDto();
	}

	@Override
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas() {
		return regionesCoberturaDAO.listarRegionesCoberturaActivas();
	}

	@Override
	public ArrayList<Instituciones> listarInstituciones() {
		return institucionesDAO.listarInstituciones();
	}
	
	@Override
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucion(int institucion) {
		return centroAtencionDAO.listarTodosActivosPorInstitucion(institucion);
	}

	@Override
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucionYRegion(int institucion, long codRegion) {
		return centroAtencionDAO.listarTodosActivosPorInstitucionYRegion(institucion, codRegion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYCiudad(long empresaInstitucion, String codigoCiudad, 
			String codigoPais, String codigoDto ){
		return centroAtencionDAO.listarTodosPorEmpresaInstitucionYCiudad(empresaInstitucion, codigoCiudad, codigoPais, codigoDto);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYRegion(long empresaInstitucion, long codigoRegion ){
		return centroAtencionDAO.listarTodosPorEmpresaInstitucionYRegion(empresaInstitucion, codigoRegion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosCentrosAtencion(){
		return centroAtencionDAO.listarTodosCentrosAtencion();
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorCiudad (String codigoCiudad, String codigoPais, String codigoDto ){
		return centroAtencionDAO.listarTodosPorCiudad(codigoCiudad, codigoPais, codigoDto);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucion(long empresaInstitucion){
		return centroAtencionDAO.listarTodosPorEmpresaInstitucion(empresaInstitucion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorRegion(long codigoRegion ){
		return centroAtencionDAO.listarTodosPorRegion(codigoRegion);
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
		return centroAtencionDAO.listarTodosPorPais(codigoPais);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo#listarAllCiudades()
	 */
	@Override
	public List<DtoCiudad> listarAllCiudades() {
		return ciudadesDAO.listarAllCiudades();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo#listarAllBarrios()
	 */
	@Override
	public List<DtoBarrio> listarAllBarrios() {
		return barriosDAO.listarAllBarrios();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo#listarAllLocalidades()
	 */
	@Override
	public List<DtoLocalidad> listarAllLocalidades() {
		return localidadesDAO.listarAllLocalidades();
	}
}
