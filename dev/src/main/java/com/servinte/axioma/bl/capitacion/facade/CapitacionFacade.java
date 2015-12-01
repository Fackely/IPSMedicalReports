package com.servinte.axioma.bl.capitacion.facade;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.bl.capitacion.impl.CargueUsuariosMundo;
import com.servinte.axioma.bl.capitacion.impl.CatalogoCapitacionMundo;
import com.servinte.axioma.bl.capitacion.impl.NivelAutorizacionMundo;
import com.servinte.axioma.bl.capitacion.interfaz.ICargueUsuariosMundo;
import com.servinte.axioma.bl.capitacion.interfaz.ICatalogoCapitacionMundo;
import com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo;
import com.servinte.axioma.dto.capitacion.NivelAtencionDto;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase Fachada que provee todos los servicios de l�gica de negocio del m�dulo de
 * Capitaci�n a todos los Action de la Capa Web
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class CapitacionFacade {

	/**
	 * Servicio encargado de obtener los niveles de atenci�n parametrizados
	 * en el sistema
	 * 
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<NivelAtencionDto> consultarNivelesAtencion() throws IPSException{
		ICatalogoCapitacionMundo catalogoCapitacionMundo= new CatalogoCapitacionMundo();
		return catalogoCapitacionMundo.consultarNivelesAtencion();
	}

	
	/**
	 * M�todo encargado de consultar los niveles de Autorizaci�n
	 * parametrizados para el usuario
	 * 
	 * @param loginUsuario
	 * @param codigoPersonaUsuario
	 * @param tipoAutorizacion
	 * @param isActivo
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<NivelAutorizacionDto> consultarNivelesAutorizacionUsuario(String loginUsuario, int codigoPersonaUsuario, String tipoAutorizacion, boolean isActivo) throws IPSException{
		INivelAutorizacionMundo nivelAutorizacionMundo = new NivelAutorizacionMundo();
		return nivelAutorizacionMundo.consultarNivelesAutorizacionUsuario(loginUsuario, codigoPersonaUsuario, tipoAutorizacion, isActivo, true);
	}

	
	/**
	 * Metodo que verifica si existe parametrizaci�n de niveles por art�culos (DCU 1006) Versus
	 * los Niveles de Autorizaci�n del usuario que aplican para la v�a de Ingreso de la orden y 
	 * que apliquen para al menos uno de los medicamentos o insumos   
	 * 
	 * @param medicamentosInsumosOrden
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeNivelAutorizacionMedicamentoInsumo(List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosOrden, List<Integer> nivelesAutorizacionUsuario) throws IPSException{
		INivelAutorizacionMundo nivelAutorizacionMundo= new NivelAutorizacionMundo();
		return nivelAutorizacionMundo.existeNivelAutorizacionMedicamentoInsumo(medicamentosInsumosOrden, nivelesAutorizacionUsuario);
	}

	
	/**
	 * Metodo que verifica si existe parametrizaci�n de niveles por servicios (DCU 1006) Versus
	 * los Niveles de Autorizaci�n del usuario que aplican para la v�a de Ingreso de la orden y 
	 * que apliquen para al menos uno de los servicios
	 * 
	 * @param serviciosOrden
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeNivelAutorizacionServicio(List<ServicioAutorizacionOrdenDto> serviciosOrden, List<Integer> nivelesAutorizacionUsuario) throws IPSException{
		INivelAutorizacionMundo nivelAutorizacionMundo= new NivelAutorizacionMundo();
		return nivelAutorizacionMundo.existeNivelAutorizacionServicio(serviciosOrden, nivelesAutorizacionUsuario);
	}

	
	
	
	/**
	 * Consulta los cargue de usuarios que coincida con los filtros de busqueda
	 * 
	 * @param requiereTransaccion
	 * @param filtrosUsuario
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 4/10/2012
	 */
	public List<DtoUsuariosCapitados> consultarCargueUsuarios(boolean requiereTransaccion,DtoUsuariosCapitados filtrosUsuario) throws IPSException{
		ICargueUsuariosMundo cargueUsuariosMundo=new CargueUsuariosMundo();
		return cargueUsuariosMundo.consultarCargueUsuarios(requiereTransaccion, filtrosUsuario);
	}
	
	/**
	 * Consulta la informacion detallada de un usuario capitado
	 * 
	 * @param requiereTransaccion
	 * @param usuarioCapitado
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 5/10/2012
	 */
	public DtoUsuariosCapitados consultarDetalleCargue(boolean requiereTransaccion,DtoUsuariosCapitados usuarioCapitado) throws IPSException{
		ICargueUsuariosMundo cargueUsuariosMundo=new CargueUsuariosMundo();
		return cargueUsuariosMundo.consultarDetalleUsuario(requiereTransaccion, usuarioCapitado);
	}
}