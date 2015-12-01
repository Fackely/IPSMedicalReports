package com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica;

import com.princetonsa.dto.odontologia.DtoInicioAtencionCita;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.fabrica.odontologia.agendaOdontologica.AgendaOdontologicaFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IUsuarioDAO;
import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaMundo;
import com.servinte.axioma.orm.CitasOdontologicas;
import com.servinte.axioma.orm.CitasOdontologicasHome;
import com.servinte.axioma.orm.InicioAtencionCita;
import com.servinte.axioma.orm.Usuarios;

public class InicioAtencionCitaMundo implements IInicioAtencionCitaMundo{

	private IInicioAtencionCitaDAO dao;
	
	
	public InicioAtencionCitaMundo() {
		dao = AgendaOdontologicaFabricaDAO.crearInicioAtencionCitaDAO();
	}
	/**
	 * 
	 * Este Método se encarga de guardar un registro de
	 * inicio atención de citas odontológicas
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean guardarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto){
		
		InicioAtencionCita iac=new InicioAtencionCita();
		
		iac.setFechaInicio(dto.getFechaInicioAtencion());
		iac.setFechaModificacion(dto.getFechaModificacion());
		iac.setHoraInicio(dto.getHoraInicioAtencion());
		iac.setHoraModificacion(dto.getHoraModificacion());
		
		ICitaOdontologicaDAO citaOdontologicaDAO= AgendaOdontologicaFabricaDAO.crearAgendaOdontologicaDAO();
		CitasOdontologicas cita=citaOdontologicaDAO.buscarCitaOdontologicas(dto.getCodigoCita());
		iac.setCitasOdontologicas(cita);
			
		IUsuarioDAO	usuariosDAO = AdministracionFabricaDAO.crearUsuarioDAO();
		Usuarios  usuario = usuariosDAO.buscarPorLogin(dto.getLoginUsuario());
		iac.setUsuarios(usuario);
		
		return dao.guardarInicioAtencionCitaOdonto(iac);
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar un registro de
	 * inicio atención de citas odontológicas
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean actualizarInicioAtencionCitaOdonto(DtoInicioAtencionCita dto){
		
		InicioAtencionCita iac=new InicioAtencionCita();
		iac.setCodigoCita(dto.getCodigoCita());
		iac.setFechaInicio(dto.getFechaInicioAtencion());
		iac.setFechaModificacion(dto.getFechaModificacion());
		iac.setHoraInicio(dto.getHoraInicioAtencion());
		iac.setHoraModificacion(dto.getHoraModificacion());
		
		CitasOdontologicasHome c= new CitasOdontologicasHome();
		CitasOdontologicas cita=c.findById(dto.getCodigoCita());
		iac.setCitasOdontologicas(cita);
			
		IUsuarioDAO	usuariosDAO = AdministracionFabricaDAO.crearUsuarioDAO();
		Usuarios  usuario = usuariosDAO.buscarPorLogin(dto.getLoginUsuario());
		iac.setUsuarios(usuario);
		
		return dao.actualizarInicioAtencionCitaOdonto(iac);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * inicio atención de citas odontológicas por su id
	 * 
	 * @param 
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public InicioAtencionCita buscarRegistroInicioAtencionCitaOdontoPorID(long codigoCita){
		return dao.buscarRegistroInicioAtencionCitaOdontoPorID(codigoCita);
	}
}
