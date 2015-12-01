/**
 * 
 */
package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;
import com.princetonsa.dto.capitacion.DTOPrioridadOcupacionMedica;
import com.princetonsa.dto.capitacion.DTOPrioridadUsuarioEspecifico;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionOcupacionMedicaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadOcupacionMedicaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IPrioridadUsuarioEspecificoDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorizacionUsuarioMundo;
import com.servinte.axioma.orm.CentrosCostoEntidadesSub;
import com.servinte.axioma.orm.NivelAutorOcupMedica;
import com.servinte.axioma.orm.NivelAutorUsuEspec;
import com.servinte.axioma.orm.NivelAutorUsuario;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.OcupacionesMedicas;
import com.servinte.axioma.orm.PrioridadOcupMedica;
import com.servinte.axioma.orm.PrioridadUsuEsp;
import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionUsuarioMundo implements
		INivelAutorizacionUsuarioMundo {
	
	INivelAutorizacionUsuarioDAO dao;
	
	public NivelAutorizacionUsuarioMundo(){
		dao = CapitacionFabricaDAO.crearNivelAutorizacionUsuarioDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return DTONivelAutorizacionUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionUsuario buscarNivelAutorizacionUsuario(int nivelAutorizacionID){
		
		return dao.buscarNivelAutorizacionUsuario(nivelAutorizacionID);
	}


	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionUsuario(NivelAutorUsuario registro){
		return dao.guardarNivelAutorizacionUsuario(registro);
	}



	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionUsuario(NivelAutorUsuario registro){
		return dao.actualizarNivelAutorizacionUsuario(registro);
	}


	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de usuario
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionUsuario(int idNivelAutorizacionUsuario){
		return dao.eliminarNivelAutorizacionUsuario(idNivelAutorizacionUsuario);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * por usuario y sus detalles
	 * 
	 * @return DTONivelAutorizacionUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionUsuario buscarNivelAutorizacionUsuarioDetallado(int nivelAutorizacionID){
		INivelAutorizacionUsuarioEspecificoDAO usuarioEspDAO = 
			CapitacionFabricaDAO.crearNivelAutorizacionUsuarioEspecificoDAO();
		INivelAutorizacionOcupacionMedicaDAO ocupacionMedicaDAO = 
			CapitacionFabricaDAO.crearNivelAutorizacionOcupacionMedicaDAO();
		
		DTOBusquedaNivelAutorizacionUsuario dto = dao.buscarNivelAutorizacionUsuario(nivelAutorizacionID);
		if(dto!=null){
			ArrayList<DTONivelAutorizacionUsuarioEspecifico> listaNivelUsuarioEsp = 
				usuarioEspDAO.buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(dto.getCodigoPk());
			ArrayList<DTONivelAutorizacionOcupacionMedica> listaNivelOcupacionMedica =
				ocupacionMedicaDAO.buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(dto.getCodigoPk());
			
			if(listaNivelUsuarioEsp!=null && listaNivelUsuarioEsp.size()>0){
				IPrioridadUsuarioEspecificoDAO priorUsuEspDAO = 
					CapitacionFabricaDAO.crearPrioridadUsuarioEspecificoDAO();
				ArrayList<DTOPrioridadUsuarioEspecifico> listaPriodidadUsuarioEsp = null;
				for(DTONivelAutorizacionUsuarioEspecifico registro:listaNivelUsuarioEsp){
					listaPriodidadUsuarioEsp = new ArrayList<DTOPrioridadUsuarioEspecifico>();
					listaPriodidadUsuarioEsp = 
						priorUsuEspDAO.buscarPrioridadUsuarioEspecificoPorNivelAutorID(registro.getCodigoPk());
					registro.setListaPriodidadUsuarioEsp(listaPriodidadUsuarioEsp);
				}				
				
				dto.setListaNivelUsuarioEsp(listaNivelUsuarioEsp);
			}
			if(listaNivelOcupacionMedica!=null && listaNivelOcupacionMedica.size()>0){
				IPrioridadOcupacionMedicaDAO prioridadOcuMedicaDAO = CapitacionFabricaDAO.crearPrioridadOcupacionMedicaDAO();
				ArrayList<DTOPrioridadOcupacionMedica> listaPrioridadOcuMedica =null;
					
				for(DTONivelAutorizacionOcupacionMedica registro:listaNivelOcupacionMedica){
					listaPrioridadOcuMedica = new ArrayList<DTOPrioridadOcupacionMedica>();
					listaPrioridadOcuMedica = 
						prioridadOcuMedicaDAO.buscarPrioridadOcupacionMedicaPorNivelAutorID(registro.getCodigoPk());
					registro.setListaPrioridadOcuMedica(listaPrioridadOcuMedica);
				}
				dto.setListaNivelOcupacionMedica(listaNivelOcupacionMedica);
			}
		}		 
		return dto;
		
	}	
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de usuario y sus detalles
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionUsuarioDetallado(DTOBusquedaNivelAutorizacionUsuario dtoNivelAutUsuario){
		INivelAutorizacionUsuarioDAO usuarioDAO = CapitacionFabricaDAO.crearNivelAutorizacionUsuarioDAO();		
		NivelAutorUsuario nivelAutUsuario = new NivelAutorUsuario();
		NivelAutorUsuEspec nivelAutorUsuarioEsp=null;
		NivelAutorOcupMedica nivelAutorOcupacionMedica=null;
		NivelAutorizacion nivelAutorizacion = new NivelAutorizacion();
		boolean nuevo=true;
		boolean procesoExitoso=true;
		IPrioridadOcupacionMedicaDAO prioridadOcuMedica = 
			CapitacionFabricaDAO.crearPrioridadOcupacionMedicaDAO();		
		IPrioridadUsuarioEspecificoDAO prioridadUsuarioEspDAO = 
			CapitacionFabricaDAO.crearPrioridadUsuarioEspecificoDAO();
		
		if(dtoNivelAutUsuario.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
			nivelAutUsuario =
				usuarioDAO.buscarNivelAutorizacionUsuarioPorID(dtoNivelAutUsuario.getCodigoPk());
			nuevo=false;
		}
		
		nivelAutorizacion.setCodigoPk(dtoNivelAutUsuario.getNivelAutorizacionID());
		
		if(dtoNivelAutUsuario.getListaNivelUsuarioEsp()!=null && 
				dtoNivelAutUsuario.getListaNivelUsuarioEsp().size()>0){
			
			HashSet<NivelAutorUsuEspec> setNivelServicioEsp = new HashSet<NivelAutorUsuEspec>(0);
			HashSet<PrioridadUsuEsp> setPrioridadServicioEsp = null;
			PrioridadUsuEsp prioridad = null;
			
			for(DTONivelAutorizacionUsuarioEspecifico registro : dtoNivelAutUsuario.getListaNivelUsuarioEsp()){
				nivelAutorUsuarioEsp= new NivelAutorUsuEspec();
				setPrioridadServicioEsp = new HashSet<PrioridadUsuEsp>();				
				//CentrosCostoEntidadesSub centrosCostoEntidadesSub= new CentrosCostoEntidadesSub();
				
				if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
					nivelAutorUsuarioEsp.setCodigoPk(registro.getCodigoPk());
				}				
				
				Usuarios usuarioEsp = new Usuarios();
				usuarioEsp.setLogin(registro.getUsuarioEspLogin());
				
				nivelAutorUsuarioEsp.setNivelAutorUsuario(nivelAutUsuario);				
				nivelAutorUsuarioEsp.setUsuariosByUsuario(usuarioEsp);
				nivelAutorUsuarioEsp.setFechaRegistro(Calendar.getInstance().getTime());
				nivelAutorUsuarioEsp.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
						.getInstance().getTime()));
				nivelAutorUsuarioEsp.setUsuariosByUsuarioRegistra(dtoNivelAutUsuario.getUsuarios());				
				
				for(DTOPrioridadUsuarioEspecifico prioridadDTO : registro.getListaPriodidadUsuarioEsp()){					
					if(prioridadDTO.isActivo()){
						//centrosCostoEntidadesSub = new CentrosCostoEntidadesSub();
						//centrosCostoEntidadesSub.setConsecutivo(prioridadDTO.getPrioridadID());
						
						prioridad = new PrioridadUsuEsp();
						if(prioridadDTO.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
							prioridad.setCodigoPk(prioridadDTO.getCodigoPk());
						}
						prioridad.setNroPrioridad(prioridadDTO.getNumeroPrioridad());
						prioridad.setNivelAutorUsuEspec(nivelAutorUsuarioEsp);
						setPrioridadServicioEsp.add(prioridad);
					}else{
						if(prioridadDTO.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
							prioridadUsuarioEspDAO.eliminarPrioridadUsuarioEspecifico(prioridadDTO.getCodigoPk());
						}
					}
				}
				nivelAutorUsuarioEsp.setPrioridadUsuEsps(setPrioridadServicioEsp);
				setNivelServicioEsp.add(nivelAutorUsuarioEsp);
			}
			nivelAutUsuario.setNivelAutorUsuEspecs(setNivelServicioEsp);
		}
		
		if(dtoNivelAutUsuario.getListaNivelOcupacionMedica()!= null && 
				dtoNivelAutUsuario.getListaNivelOcupacionMedica().size()>0){
			HashSet<NivelAutorOcupMedica> setNivelOcupacionMedica = new HashSet<NivelAutorOcupMedica>(0);
			HashSet<PrioridadOcupMedica> setPrioridadOcupMedica = null;
			PrioridadOcupMedica prioridad = null;
			//CentrosCostoEntidadesSub centrosCostoEntidadesSub = null;
			
			for(DTONivelAutorizacionOcupacionMedica registro:dtoNivelAutUsuario.getListaNivelOcupacionMedica()){
				nivelAutorOcupacionMedica= new NivelAutorOcupMedica();
				setPrioridadOcupMedica=new HashSet<PrioridadOcupMedica>(0);
				
				if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
					nivelAutorOcupacionMedica.setCodigoPk(registro.getCodigoPk());
				}		
				
				OcupacionesMedicas ocupacionMedica = new OcupacionesMedicas();
				ocupacionMedica.setCodigo(registro.getOcupacionMedicaID());
				
				nivelAutorOcupacionMedica.setNivelAutorUsuario(nivelAutUsuario);
				nivelAutorOcupacionMedica.setOcupacionesMedicas(ocupacionMedica);
				nivelAutorOcupacionMedica.setFechaRegistro(Calendar.getInstance().getTime());
				nivelAutorOcupacionMedica.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
						.getInstance().getTime()));
				nivelAutorOcupacionMedica.setUsuarios(dtoNivelAutUsuario.getUsuarios());
				
				for(DTOPrioridadOcupacionMedica prioridadDTO : registro.getListaPrioridadOcuMedica()){
					if(prioridadDTO.isActivo()){
						prioridad = new PrioridadOcupMedica();
						if(prioridadDTO.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
							prioridad.setCodigoPk(prioridadDTO.getCodigoPk());
						}					
						//centrosCostoEntidadesSub = new CentrosCostoEntidadesSub();
						//centrosCostoEntidadesSub.setConsecutivo(prioridadDTO.getIdPrioridad());
						
						//prioridad.setCentrosCostoEntidadesSub(centrosCostoEntidadesSub);
						prioridad.setNroPrioridad(prioridadDTO.getNumeroPrioridad());
						prioridad.setNivelAutorOcupMedica(nivelAutorOcupacionMedica);
						setPrioridadOcupMedica.add(prioridad);
					}else{
						if(prioridadDTO.getCodigoPk()!=ConstantesBD.codigoNuncaValido){
							prioridadOcuMedica.eliminarPrioridadOcupacionMedica(prioridadDTO.getCodigoPk());
						}
					}
				}
				nivelAutorOcupacionMedica.setPrioridadOcupMedicas(setPrioridadOcupMedica);
				setNivelOcupacionMedica.add(nivelAutorOcupacionMedica);
			}
			nivelAutUsuario.setNivelAutorOcupMedicas(setNivelOcupacionMedica);
		}		
		
		
	
		nivelAutUsuario.setFechaRegistro(Calendar.getInstance().getTime());
		nivelAutUsuario.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));		
		nivelAutUsuario.setUsuarios(dtoNivelAutUsuario.getUsuarios());
		nivelAutUsuario.setNivelAutorizacion(nivelAutorizacion);
		
		if(nuevo){
			procesoExitoso = dao.guardarNivelAutorizacionUsuario(nivelAutUsuario);
		}else{
			procesoExitoso = dao.actualizarNivelAutorizacionUsuario(nivelAutUsuario);
		}
		return procesoExitoso;
	}

}
