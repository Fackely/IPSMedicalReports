package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioArticuloDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionMundo;
import com.servinte.axioma.orm.NivelAutorizacion;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionMundo implements INivelAutorizacionMundo {
	
	INivelAutorizacionDAO dao;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionDAO();
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
		ArrayList<DTONivelAutorizacion> listaNivelAutorizacion = dao.buscarNivelAutorizacion();
		INivelAutorizacionServicioArticuloDAO nivelAutorServMedDAO = 
			CapitacionFabricaDAO.crearNivelAutorizacionServicioArticuloDAO();
		INivelAutorizacionUsuarioDAO nivelAutorUsuarioDAO = 
			CapitacionFabricaDAO.crearNivelAutorizacionUsuarioDAO();
		DTOBusquedaNivelAutorizacionServicioArticulo nivelServiciosArticulosDTO = null;
		DTOBusquedaNivelAutorizacionUsuario nivelUsuarioDTO = null;
		boolean permiteEliminar = true;
		
		if(listaNivelAutorizacion!=null && listaNivelAutorizacion.size()>0){
			for(DTONivelAutorizacion registro : listaNivelAutorizacion){
				nivelServiciosArticulosDTO = nivelAutorServMedDAO.
					buscarNivelAutorizacionServicioArticulo(registro.getCodigoPk());
				if(nivelServiciosArticulosDTO!=null){
					permiteEliminar=false;
				}else{
					nivelUsuarioDTO = nivelAutorUsuarioDAO.buscarNivelAutorizacionUsuario(
							registro.getCodigoPk());					
					if(nivelUsuarioDTO!=null){
						permiteEliminar=false;
					}
				}
				registro.setPermiteEliminar(permiteEliminar);
				permiteEliminar=true;
			}
		}		
		return listaNivelAutorizacion;
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
		return dao.guardarNivelAutorizacion(registro);
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
		return dao.buscarNivelAutorizacionPorUsuario(login);
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
		return dao.buscarNivelAutorizacionPorOcupacionMedica(codigoOcupacion);
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
		return dao.actualizarNivelAutorizacion(registro);
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
		return dao.eliminarNivelAutorizacion(idNivelAutorizacion);
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
		boolean save = false;
		
		if(registro.getCodigoPk()>0){
			save = dao.actualizarNivelAutorizacion(registro);
		}else{
			registro.setCodigoPk(0);
			save = dao.guardarNivelAutorizacion(registro);
		}
		return save;
	}
	
	/**
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo G�mez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelAutorizacion(){		
		ArrayList<DTONivelAutorizacion> listaNivelAutorizacion = dao.obtenerNivelesAutorizacion();
		INivelAutorizacionServicioArticuloDAO nivelAutorServMedDAO = 
			CapitacionFabricaDAO.crearNivelAutorizacionServicioArticuloDAO();
		INivelAutorizacionUsuarioDAO nivelAutorUsuarioDAO = 
			CapitacionFabricaDAO.crearNivelAutorizacionUsuarioDAO();
		DTOBusquedaNivelAutorizacionServicioArticulo nivelServiciosArticulosDTO = null;
		DTOBusquedaNivelAutorizacionUsuario nivelUsuarioDTO = null;
		boolean permiteEliminar = true;
		
		if(listaNivelAutorizacion!=null && listaNivelAutorizacion.size()>0){
			for(DTONivelAutorizacion registro : listaNivelAutorizacion){
				nivelServiciosArticulosDTO = nivelAutorServMedDAO.
					buscarNivelAutorizacionServicioArticulo(registro.getCodigoPk());
				if(nivelServiciosArticulosDTO!=null){
					permiteEliminar=false;
				}else{
					nivelUsuarioDTO = nivelAutorUsuarioDAO.buscarNivelAutorizacionUsuario(
							registro.getCodigoPk());					
					if(nivelUsuarioDTO!=null){
						permiteEliminar=false;
					}
				}
				registro.setPermiteEliminar(permiteEliminar);
				permiteEliminar=true;
			}
		}		
		return listaNivelAutorizacion;
	}

}
