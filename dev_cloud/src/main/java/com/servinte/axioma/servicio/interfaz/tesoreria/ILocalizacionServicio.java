package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;


/**
 * Servicio que le delega al negocio las operaciones relacionados con 
 * los ingresos y los egresos que tiene una caja 
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.servicio.impl.tesoreria.IIngresosEgresosCajaServicioServicio
 */
public interface ILocalizacionServicio {
	
	
	
	/* LOCALIZACION */
	/**
	 * Lista todos los Paises
	 * @return
	 */
	public ArrayList<Paises> listarPaises ();
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoPaises> listarPaisesDto();
	/**
	 * Lista todas las Ciudades
	 * @return
	 */
	public ArrayList<Ciudades> listarCiudades ();
	
	/**
	 * Lista todas las Ciudades de un país
	 * @return
	 */
	public ArrayList<Ciudades> listarCiudadesPorPais (String codigoPais);
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoCiudades> listarCiudadesDtoPorPais(String codigoPais);
	
	/**
	 * Lista todas las Regiones de Cobertura
	 * @return
	 */
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas();
	
	/**
	 * Lista todas las Instituciones
	 * @return
	 */
	public ArrayList<Instituciones> listarInstituciones ();
	
	
	/**
	 * 	Retorna los activos de acuerdo a una insitucion
	 */
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucion(int institucion);
	
	
	/**
	 * 	Retorna los activos de acuerdo a una insitucion y a una region
	 */
	public ArrayList<CentroAtencion> listarTodosActivosPorInstitucionYRegion(int institucion, long codRegion);
	
	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una empresa institucion y una ciudad espec&iacute;fica.
	 * @param empresaInstitucion
	 * @param codigoCiudad
	 * @param codigoPais
	 * @param codigoDto
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYCiudad(long empresaInstitucion, String codigoCiudad, 
			String codigoPais, String codigoDto );
	
	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una empresa institucion y una regi&oacute; espec&iacute;fica.
	 * @param empresaInstitucion
	 * @param codigoRegion
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucionYRegion(long empresaInstitucion, long codigoRegion );
	
	/**
	 * Este m&eacute;todo se encarga de retornar el listado con
	 * todos los centros de atenci&oacute;n existentes en el sistema.
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosCentrosAtencion();
	
	/**
	 * Este m&eacute;todo se encarga de obtner el listado de los centros de 
	 * atenci&oacute;n pertenecientes a una determinada ciudad.
	 * @param codigoCiudad
	 * @param codigoPais
	 * @param codigoDto
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorCiudad (String codigoCiudad, String codigoPais, String codigoDto );
	
	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una empresa instituci&oacute;n espec&iacute;fica.
	 * @param empresaInstitucion
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorEmpresaInstitucion(long empresaInstitucion);
	
	/**
	 * Retorna todos los centros de atenci&oacute;n del sistema de 
	 * acuerdo a una regi&oacute; espec&iacute;fica.
	 * @param codigoRegion
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorRegion(long codigoRegion );

	/**
	 * M&eacute;todo encargado de obtener el listado de los centros de 
	 * atenci&oacute;n pertenecientes a un pa%iacute; determinado.
	 * 
	 * @param codigoPais
	 * @return listaCentro
	 *
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DtoCentrosAtencion> listarTodosPorPais (String codigoPais);

}
