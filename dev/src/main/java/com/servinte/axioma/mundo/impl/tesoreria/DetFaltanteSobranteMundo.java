package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.Set;

import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetFaltanteSobranteDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IHistoCambioResponsableDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetFaltanteSobranteMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.orm.HistoCambioResponsable;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos de
 * negocio para la entidad  detalle faltante sobrante
 * 
 * @author Angela Maria Aguirre
 * @since 22/07/2010
 */
public class DetFaltanteSobranteMundo implements IDetFaltanteSobranteMundo {

	/**
	 * Instancia DAO IDetFaltanteSobranteDAO
	 */
	IDetFaltanteSobranteDAO detFaltanteDAO;
	
	public DetFaltanteSobranteMundo(){
		detFaltanteDAO = TesoreriaFabricaDAO.crearDetFaltanteSobranteDAO();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consular por ID los datos del detalle de un
	 * registro faltante sobrante
	 * 
	 * @param DetFaltanteSobrante
	 * @return DetFaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public DetFaltanteSobrante consultarRegistroDetFaltanteSobrantePorID(
			DetFaltanteSobrante registro) {
		return detFaltanteDAO.consultarRegistroDetFaltanteSobrantePorID(registro);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el detalle
	 * de un faltante sobrante
	 * 
	 * @param DetFaltanteSobrante
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean actualizarDetFaltanteSobrante(DetFaltanteSobrante registro){
		return detFaltanteDAO.actualizarDetFaltanteSobrante(registro);
	}
		
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el responsable del
	 * detalle de un registro de faltante sobrante y guardar su hist&oacute;rico
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */	
	@Override
	public boolean actualizarResponsableDetFaltanteSobrante(DTOCambioResponsableDetFaltanteSobrante dto) {		
		IHistoCambioResponsableDAO histoDAO = TesoreriaFabricaDAO.crearHistorialCambioResponsableDAO();
		IDetFaltanteSobranteDAO detalleDAO= TesoreriaFabricaDAO.crearDetFaltanteSobranteDAO();		
		DetFaltanteSobrante detalle = new DetFaltanteSobrante();
		
		detalle.setCodigoPk(dto.getIdDetalleFaltanteSobrante());		
		detalle = detalleDAO.consultarRegistroDetFaltanteSobrantePorID(detalle);		
		
		boolean procesoExitoso=false;
			
		if(detalle!=null){
						
			HistoCambioResponsable historial = new HistoCambioResponsable();
			Usuarios usuarioModifica = new Usuarios();
			Usuarios usuarioResponsable = new Usuarios();
			Usuarios nuevoResponsable = new Usuarios();
			CentroAtencion centroAtencion = new CentroAtencion();
				
			usuarioModifica.setLogin(dto.getLoginUsuarioModifica());
			usuarioResponsable.setLogin(detalle.getUsuarios().getLogin());
			centroAtencion.setConsecutivo(dto.getConsecutivoCA());
			
			historial.setDetFaltanteSobrante(detalle);
			historial.setUsuariosByUsuarioModifica(usuarioModifica);
			historial.setUsuariosByResponsable(usuarioResponsable);
			historial.setFechaModifica(dto.getFechaProceso());
			historial.setHoraModifica(dto.getHoraProceso());	
			historial.setMotivo(dto.getMotivo());
			historial.setCentroAtencion(centroAtencion);
			
			histoDAO.registrarHistorialFaltanteSobrante(historial);
							
			nuevoResponsable.setLogin(dto.getLoginUsuarioResponsable());				
			detalle.setUsuarios(nuevoResponsable);
				
			procesoExitoso = detalleDAO.actualizarDetFaltanteSobrante(detalle);
		}
		
		return procesoExitoso;
		
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar los datos necesarios
	 * para el cambio del responsable del detalle de faltante/sobrante
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante, con los datos necesarios para realizar la consulta  
	 * @return List<DTOCambioResponsableDetFaltanteSobrante>, con los datos obtenidos en la consulta
	 * @author Angela Aguirre
	 */
	@Override
	public ArrayList<DTOCambioResponsableDetFaltanteSobrante> busquedaDetFaltanteSobrante(DTOCambioResponsableDetFaltanteSobrante dto){
		ArrayList<DTOCambioResponsableDetFaltanteSobrante> listaDetFaltanteSobrante =
			detFaltanteDAO.busquedaDetFaltanteSobrante(dto);
		
		if(listaDetFaltanteSobrante!=null && listaDetFaltanteSobrante.size()>0){
			completarDatosFaltanteSobrante(listaDetFaltanteSobrante);
		}
		
		return listaDetFaltanteSobrante;		
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de buscar los datos faltantes
	 * de la consulta de los registros de faltantes sobrantes
	 * 
	 * @param ArrayList<DTOCambioResponsableDetFaltanteSobrante>
	 * @return ArrayList<DTOCambioResponsableDetFaltanteSobrante>
	 * @author, Angela Maria Aguirre
	 *
	 */	
	private ArrayList<DTOCambioResponsableDetFaltanteSobrante> completarDatosFaltanteSobrante(
			ArrayList<DTOCambioResponsableDetFaltanteSobrante> lista) {
		
		IUsuariosServicio usuarioServicio = AdministracionFabricaServicio.crearUsuariosServicio();  
		
		for(DTOCambioResponsableDetFaltanteSobrante registro : lista){
			DtoUsuarioPersona persona = new DtoUsuarioPersona();
			DtoUsuarioPersona usuarioResponsable = new DtoUsuarioPersona();
			
			persona = usuarioServicio.obtenerDtoUsuarioPersona(registro.getLoginUsuarioResponsable());
			
			if(persona!=null){
				usuarioResponsable.setApellido(persona.getApellido());						
				usuarioResponsable.setNombre(persona.getNombre());
				usuarioResponsable.setAcronimoTipoID(persona.getAcronimoTipoID());
				usuarioResponsable.setNumeroID(persona.getNumeroID());
			}			
			
			registro.setUsuarioResponsable(usuarioResponsable);
			
			if(registro.getTipoDiferencia().equals(
					ConstantesIntegridadDominio.acronimoDiferenciaFaltante)){
					
				registro.setTipoDiferencia((String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoDiferenciaFaltante));
					
			}else if(registro.getTipoDiferencia().equals(
					ConstantesIntegridadDominio.acronimoDiferenciaSobrante)){
					
				registro.setTipoDiferencia((String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoDiferenciaSobrante));
			}					
			
			if(registro.getEstadoFaltanteSobrante().equals(ConstantesIntegridadDominio.acronimoEstadoFaltanteSobranteGenerado)){
				registro.setEstadoFaltanteSobrante((String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoEstadoGenerado));
			}else{
				registro.setEstadoFaltanteSobrante((String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoEstadoFaltanteSobranteConciliado));
			}
			
			if(registro.getContabilizado()!=null){				
				registro.setDescripcionContabilizado(UtilidadTexto.imprimirSiNo(
						registro.getContabilizado().toString()));					
			}			
		}
		
		return lista;
	}

	
	/**
	 * Devuelve el total de los detalles de los Faltantes / Sobrantes asociados a las Entregas a 
	 * Transportadora de Valores.
	 * 
	 * @param setIdDetalleFaltanteSobrante
	 * @return Valor total de las diferencias asociadas a las entregas a Transportadora de valores 
	 * realizadas en un turno espec&iacute;fico
	 */
	public double obtenerTotalDiferenciaEfectivoEntregasTransportadora (Set<Long> setIdDetalleFaltanteSobrante){
		return detFaltanteDAO.obtenerTotalDiferenciaEfectivoEntregasTransportadora(setIdDetalleFaltanteSobrante);
	}

}
