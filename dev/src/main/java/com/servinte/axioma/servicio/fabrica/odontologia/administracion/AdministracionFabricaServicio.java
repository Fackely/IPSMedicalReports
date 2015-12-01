package com.servinte.axioma.servicio.fabrica.odontologia.administracion;

import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.servicio.impl.administracion.CentroAtencionServicio;
import com.servinte.axioma.servicio.impl.administracion.CentroCostosServicio;
import com.servinte.axioma.servicio.impl.administracion.EspecialidadServcio;
import com.servinte.axioma.servicio.impl.administracion.EspecialidadesServicio;
import com.servinte.axioma.servicio.impl.administracion.MedicosServicio;
import com.servinte.axioma.servicio.impl.administracion.RolesFuncionalidadesServicio;
import com.servinte.axioma.servicio.impl.administracion.TipoTarjetaClienteServicio;
import com.servinte.axioma.servicio.impl.administracion.TiposIdentificacionServicio;
import com.servinte.axioma.servicio.impl.administracion.UsuariosServicio;
import com.servinte.axioma.servicio.impl.odontologia.administracion.EmisionBonosServicio;
import com.servinte.axioma.servicio.impl.odontologia.administracion.EmisionTarjetaClienteServicio;
import com.servinte.axioma.servicio.impl.tesoreria.IngresosEgresosCajaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.LocalizacionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEmisionBonosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadesServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IRolesFuncionalidadesServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ITipoTarjetaClienteServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.administracion.IEmisionTarjetaClienteServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IIngresosEgresosCajaServicioServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;

/**
 * Fábrica para el submódulo administración 
 * @author Juan David Ramírez
 * @since 06 Septiembre 2010
 */
public abstract class AdministracionFabricaServicio 
{
	
	public AdministracionFabricaServicio() {}
	
	
	/**
	 * Crear instancia de {@link EmisionTarjetaClienteServicio}
	 * @author Juan David Ramírez
	 * @return instancia creada
	 */
	public static final IEmisionTarjetaClienteServicio crearEmisionTarjetaClienteServicio()
	{
		return new EmisionTarjetaClienteServicio();
	}

	/**
	 * Crear instancia de {@link ITipoTarjetaClienteServicio}
	 * @author Juan David Ramírez
	 * @return instancia creada
	 */
	public static final ITipoTarjetaClienteServicio crearTipoTarjetaClienteServicio()
	{
		return new TipoTarjetaClienteServicio();
	}

	

	/**
	 * Retorna la implementacion del servicio de Usuario
	 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
	 * @return
	 */
	public static IUsuariosServicio crearUsuariosServicio()
	{
		return new UsuariosServicio();
	}

	
	/**
	 *	Retorna la implementacion del servicio de centro de Atencion 
	 * @return
	 */
	public static ICentroAtencionServicio crearCentroAtencionServicio()
	{
		return new CentroAtencionServicio();
	}
	
	
	/**
	 * Retorna la implementacion del servicio de Ingresos y Egresos de Caja
	 */
	public static IIngresosEgresosCajaServicioServicio crearIIngresosEgresosCajaServicioServicio()
	{
		return new IngresosEgresosCajaServicio();
	}
	
	
	
	/**
	 * Retorna la implementacion la localizacion
	 */
	public static ILocalizacionServicio crearLocalizacionServicio()
	{
		return new LocalizacionServicio();
	}
	
	
	/**
	 * Retorna la implementacion la RolesFuncionalidades
	 */
	public static IRolesFuncionalidadesServicio crearRolesFuncionalidadesServicio()
	{
		return new RolesFuncionalidadesServicio();
	}
	
	/**
	 * Retorna la implementacion la RolesFuncionalidades
	 */
	public static IMedicosServicio crearMedicosServicio()
	{
		return new MedicosServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEspecialidadesServicio
	 * 
	 * @return IEspecialidadesServicio
	 * @author, Angela Aguirre
	 *
	 */
	public static IEspecialidadesServicio crearEspecialidadesServicio(){
		return new EspecialidadesServicio();
	}
	
	
	/**
	 * Retorna la implementacion del servicio de {@link TiposIdentificacion}
	 * 
	 * @return
	 */
	public static ITiposIdentificacionServicio crearTiposIdentificacionServicio()
	{
		return new TiposIdentificacionServicio();
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IEspecialidadServicio
	 * @return IEspecialidadServicio
	 * 
	 * @author Yennifer Guerrero
	 *
	 */
	public static IEspecialidadServicio crearEspecialidadServicio(){
		return new EspecialidadServcio();
	}

	

	/**
	 * Retorna la implementacion del servicio de {@link ICentroCostosServicio}
	 * @return ICentroCostosServicio
	 */
	public static ICentroCostosServicio crearCentroCostosServicio()
	{
		return new CentroCostosServicio();
	}

	/**
	 * Retorna la implementación del servicio de {@link }
	 * @return ICentroCostosServicio
	 */
	public static IEmisionBonosServicio crearEmisionBonosServicio()
	{
		return new EmisionBonosServicio();
	}
	
	
	
}
