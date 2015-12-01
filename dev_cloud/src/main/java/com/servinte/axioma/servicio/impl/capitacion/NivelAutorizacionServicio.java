package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionMundo;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionServicio implements INivelAutorizacionServicio {
	
	INivelAutorizacionMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorizacionMundo();
	}

	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
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
	 * Este Método se encarga de guardar los niveles de autorización
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
	 * Este Método se encarga de consultar nivel de autorización de un usuario
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
	 * Este Método se encarga de consultar nivel de autorización por ocupación medica
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabián Becerra
	 *
	 */
	@Override
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorOcupacionMedica(int codigoOcupacion){
		return mundo.buscarNivelAutorizacionPorOcupacionMedica(codigoOcupacion);
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
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
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización
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
	 * Este método se encarga de verificar si el registro de un nivel de 
	 * autorización es nuevo para realizar su inserción o su actualización 
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
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo Gómez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelAutorizacion(){
		return mundo.obtenerNivelAutorizacion();
	}
}
