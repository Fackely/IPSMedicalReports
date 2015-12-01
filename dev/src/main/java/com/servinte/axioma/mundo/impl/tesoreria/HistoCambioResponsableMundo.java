package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DTOHistoCambioResponsable;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IHistoCambioResponsableDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IHistoCambioResponsableMundo;
import com.servinte.axioma.orm.HistoCambioResponsable;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;

/**
 * Esta clase se encarga de ejecutar los procesos
 * de negocio de la entidad Historial Cambio Responsable de
 * un registro Faltante Sobrante  
 * 
 * @author Angela Maria Aguirre
 * @since 19/07/2010
 */
public class HistoCambioResponsableMundo implements IHistoCambioResponsableMundo  {
	
	/**
	 * Instancia DAO de IHistoCambioResponsableDAO
	 */
	private IHistoCambioResponsableDAO dao;
	
	
	public HistoCambioResponsableMundo(){
		dao = TesoreriaFabricaDAO.crearHistorialCambioResponsableDAO();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de registrar un historial de 
	 * cambio de responsable para un registro faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return Boolean
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Boolean registrarHistorialFaltanteSobrante(HistoCambioResponsable dto){
		return dao.registrarHistorialFaltanteSobrante(dto);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar el historial del  cambio de responsable
	 * del registro de faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return ArrayList<DTOHistoCambioResponsable> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOHistoCambioResponsable> consultarHistorialPorDetalleFaltanteSobranteID(DTOHistoCambioResponsable dto){
		ArrayList<DTOHistoCambioResponsable> lista = dao.consultarHistorialPorDetalleFaltanteSobranteID(dto);
		IUsuariosServicio usuarioServicio = AdministracionFabricaServicio.crearUsuariosServicio();
		DtoUsuarioPersona usuarioResponsable=null;
		DtoUsuarioPersona usuarioModifica=null;
		
		if(lista!=null && lista.size()>0){
			for(DTOHistoCambioResponsable registro : lista){
				DtoUsuarioPersona personaModifica= usuarioServicio.obtenerDtoUsuarioPersona(registro.getLoginUsuarioModifica());
				DtoUsuarioPersona personaResponsable = usuarioServicio.obtenerDtoUsuarioPersona(registro.getLoginUsuarioResponsable());
							
				if(personaResponsable!=null){
					usuarioResponsable = new DtoUsuarioPersona();
					usuarioResponsable.setApellido(personaResponsable.getApellido());						
					usuarioResponsable.setNombre(personaResponsable.getNombre());
					usuarioResponsable.setAcronimoTipoID(personaResponsable.getAcronimoTipoID());
					usuarioResponsable.setNumeroID(personaResponsable.getNumeroID());
				}			
				if(personaModifica!=null){
					usuarioModifica = new DtoUsuarioPersona();
					usuarioModifica.setApellido(personaModifica.getApellido());						
					usuarioModifica.setNombre(personaModifica.getNombre());
					usuarioModifica.setAcronimoTipoID(personaModifica.getAcronimoTipoID());
					usuarioModifica.setNumeroID(personaModifica.getNumeroID());
				}
				registro.setPersonaResponsable(usuarioResponsable);
				registro.setPersonaModifica(usuarioModifica);			
			}
		}
		return lista;
	}

}
