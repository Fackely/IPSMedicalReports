package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.RecibosCajaId;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface ICentroAtencionDAO {

	
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
	 * Este m&eacute;todo se encarga de retornar el c&oacute;digo de los centros 
	 * de atenci&oacute;n en los que se han dado ingresos.
	 * @param ingresos
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionIngresos (List<Integer> ingresos);
	
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
	 * Carga el centro de atencion por su id
	 * @param id
	 * @return CentroAtencion
	 */
	public CentroAtencion findById(int id);
	
	
	/**
	 * Retorna los centros de atenci&oacute;n en los cuales
	 * se registraron los presupuestos odontol&oacute; contratados  
	 * @param presupuesto
	 * @return listaCentros
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionPresupuestos (List<Long> presupuesto);
	
	
	/**
	 * M&eacute;todo que retorna los centros de atenci&oacute;n en los 
	 * cuales quedan asociados los recibos de caja.
	 * @param numeroRC
	 * @return ArrayList<DtoCentrosAtencion>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionRecibosCaja (List<RecibosCajaId> numeroRC);

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
	
	/**
	 * @return todos los centros de atencion activos
	 */
	public ArrayList<CentroAtencion> listarActivos();

	/**
	 * Este m&eacute;todo se encarga de retornar el listado con
	 * todos los centros de atenci&oacute;n activos asociados al usuario 
	 * 
	 * @return lista Centros Atencion - listado de todos los centros de atenci&oacute;n.
	 * @author Diecorqu
	 */
	public ArrayList<CentroAtencion> listarCentrosAtencionActivosUsuario(String login);

}
