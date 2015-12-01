package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionMundo;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionServicio implements INivelAutorizacionServicio {
	
	INivelAutorizacionMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionMundo();
	}

	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrada en el sistema
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DTONivelAutorizacion> buscarNivelAutorizacion(){
		return mundo.buscarNivelAutorizacion();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacion(NivelAutorizacion registro){
		return mundo.guardarNivelAutorizacion(registro);
	}

	
	/**
	 * 
	 * Este M�todo se encarga de consultar nivel de autorizaci�n de un usuario
	 * 
	 * @return ArrayList<DtoValidacionNivelesAutorizacion>
	 * @author, Fabian Becerra
	 *
	 */
	@Override	
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorUsuario(String login){
		return mundo.buscarNivelAutorizacionPorUsuario(login);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar nivel de autorizaci�n por ocupaci�n medica
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabi�n Becerra
	 *
	 */
	@Override
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorOcupacionMedica(int codigoOcupacion){
		return mundo.buscarNivelAutorizacionPorOcupacionMedica(codigoOcupacion);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean actualizarNivelAutorizacion(NivelAutorizacion registro){
		return mundo.actualizarNivelAutorizacion(registro);
	}

	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean eliminarNivelAutorizacion(int idNivelAutorizacion){
		return mundo.eliminarNivelAutorizacion(idNivelAutorizacion);
	}
	
	/**
	 * 
	 * Este m�todo se encarga de verificar si el registro de un nivel de 
	 * autorizaci�n es nuevo para realizar su inserci�n o su actualizaci�n 
	 * 
	 * @param NivelAutorizacion registro
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean actualizarRegistroNivelAutorizacion(NivelAutorizacion registro){
		return mundo.actualizarRegistroNivelAutorizacion(registro);
	}

	/**
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo G�mez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelAutorizacion(){
		return mundo.obtenerNivelAutorizacion();
	}
}
