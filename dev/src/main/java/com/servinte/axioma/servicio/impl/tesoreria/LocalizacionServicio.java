package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;
import com.servinte.axioma.servicio.interfaz.tesoreria.IIngresosEgresosCajaServicioServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link IIngresosEgresosCajaServicioServicio}
 * 
 * @author Cristhian Murillo
 *
 */
public class LocalizacionServicio  implements ILocalizacionServicio{

	private ILocalizacionMundo localizacionMundo;
	
	
	public LocalizacionServicio()
	{
		localizacionMundo	= AdministracionFabricaMundo.crearloILocalizacionMundo();
	}
	
	
	@Override
	public ArrayList<Ciudades> listarCiudades() {
		return localizacionMundo.listarCiudades();
	}
	
	public ArrayList<DtoPaises> listarPaisesDto()
	{
		return localizacionMundo.listarPaisesDto();
	}


	@Override
	public ArrayList<Ciudades> listarCiudadesPorPais(String codigoPais) {
		return localizacionMundo.listarCiudadesPorPais(codigoPais);
	}
	
	@Override
	public ArrayList<DtoCiudades> listarCiudadesDtoPorPais(String codigoPais)
	{
		return localizacionMundo.listarCiudadesDtoPorPais(codigoPais);
	}


	@Override
	public ArrayList<Paises> listarPaises() {
		return localizacionMundo.listarPaises();
	}


	@Override
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas() {
		return localizacionMundo.listarRegionesCoberturaActivas();
	}


	@Override
	public ArrayList<Instituciones> listarInstituciones() {
		return localizacionMundo.listarInstituciones();
	}



	@Override
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucion(int institucion) {
		return localizacionMundo.listarTodosActivosPorInstitucion(institucion);
	}
	
	
	@Override
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucionYRegion(int institucion, long codRegion){
		return localizacionMundo.listarTodosActivosPorInstitucionYRegion(institucion, codRegion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYCiudad(long empresaInstitucion, String codigoCiudad, 
			String codigoPais, String codigoDto ){
		return localizacionMundo.listarTodosPorEmpresaInstitucionYCiudad(empresaInstitucion, codigoCiudad, codigoPais, codigoDto);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYRegion(long empresaInstitucion, long codigoRegion ){
		return localizacionMundo.listarTodosPorEmpresaInstitucionYRegion(empresaInstitucion, codigoRegion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosCentrosAtencion(){
		return localizacionMundo.listarTodosCentrosAtencion();
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorCiudad (String codigoCiudad, String codigoPais, String codigoDto ){
		return localizacionMundo.listarTodosPorCiudad(codigoCiudad, codigoPais, codigoDto);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucion(long empresaInstitucion){
		return localizacionMundo.listarTodosPorEmpresaInstitucion(empresaInstitucion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarTodosPorRegion(long codigoRegion ){
		return localizacionMundo.listarTodosPorRegion(codigoRegion);
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
		return localizacionMundo.listarTodosPorPais(codigoPais);
	}
}
