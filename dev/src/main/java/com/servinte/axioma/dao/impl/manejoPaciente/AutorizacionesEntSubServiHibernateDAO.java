package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubServiDAO;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEntSubServiDelegate;

/**
 * Esta clase se encarga de ejecutar la lógica de negocio
 * de la entidad AutorizacionesEntSubServi
 * 
 * @author Angela Maria Aguirre
 * @since 9/12/2010
 */
public class AutorizacionesEntSubServiHibernateDAO implements IAutorizacionesEntSubServiDAO{
	 
	AutorizacionesEntSubServiDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public AutorizacionesEntSubServiHibernateDAO(){
		delegate = new AutorizacionesEntSubServiDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos un registro de
	 * servicio de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntSubServi servicio
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarServicioAutorizacionEntidadSub(AutorizacionesEntSubServi servicio){
		return delegate.guardarServicioAutorizacionEntidadSub(servicio);
	}
	
	
	/**
	 * Este metodo permite modificar un valor para el registro previamente ingresado
	 * @param AutorizacionesEntSubServi servicio
	 * @return
	 * @author Diana Ruiz
	 */
	public boolean modificarServicioAutorizacionEntidadSub(AutorizacionesEntSubServi servicio){
		return delegate.modificarServicioAutorizacionEntidadSub(servicio);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los datos de los servicios
	 * de una autorización de capitación, a través del id de su respectiva
	 * autorización de entidad subcontratada
	 * 
	 * @param long idAutorEntSub
	 * @return ArrayList<DtoServiciosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ArrayList<DtoServiciosAutorizaciones> obtenerDetalleServiciosAutorCapitacion(long idAutorEntSub,String tipoTarifario){
		return delegate.obtenerDetalleServiciosAutorCapitacion(idAutorEntSub,tipoTarifario);
	}
	
	/**
	 * Lista los servicios de la autorizacion por entidad subcontradada
	 * 
	 * @author Fabián Becerra
	 * @param dtoParametros
	 * @return ArrayList<AutorizacionesEntSubServi>
	 */
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return delegate.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
	}

}
