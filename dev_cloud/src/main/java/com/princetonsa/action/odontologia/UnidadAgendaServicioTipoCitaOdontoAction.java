
package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.odontologia.UnidadAgendaServicioTipoCitaOdontoForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadAgendaServTipoCitaOdontoServicio;

/**
 * Action que gestiona lo relacionado con el anexo:
 * 
 * 1119 - Unidad de Agenda-Servicio X Tipo de Cita Odontol&oacute;gica
 * 
 * @author Jorge Armando Agudelo Quintero 
 */

public class UnidadAgendaServicioTipoCitaOdontoAction extends Action{

	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {

		if(form instanceof UnidadAgendaServicioTipoCitaOdontoForm)
		{	
			
			UnidadAgendaServicioTipoCitaOdontoForm forma = (UnidadAgendaServicioTipoCitaOdontoForm) form;
			
			String estado = forma.getEstado();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado.equals("iniciarProceso")){
				
				forma.reset();
				
				UtilidadTransaccion.getTransaccion().begin();

				obtenerListaUnidadAgendaOdontologica (forma);
				obtenerListadoUnidAgenServCitaOdonRegistrados (forma);
				listarTiposCitaDisponibles(forma);
				
				UtilidadTransaccion.getTransaccion().commit();
				
				return mapping.findForward("principal");
				
			}else if(estado.equals("seleccionTipoCita")){
				
				String acronimoTipoCitaOdontologica  = request.getParameter("acronimoTipoCita");
				
				seleccionarTipoCitaOdontologica(forma,	acronimoTipoCitaOdontologica);
				
				return mapping.findForward("detRegistroUnidAgenServCitaOdonto");
				
			}else if(estado.equals("seleccionUnidadAgenda")){
				
				if(forma.getUnidadAgenda()!=null){
					
					UtilidadTransaccion.getTransaccion().begin();
						
					obtenerListaServiciosPorUnidadAgenda (forma, usuario.getCodigoInstitucionInt());
					
					UtilidadTransaccion.getTransaccion().commit();
					
				}else{
					
					forma.setListaServiciosOdontologicos(null);
					forma.setNoActivaListaServiciosOdon(true);
				}
				
				return mapping.findForward("principal");
				
			}else if(estado.equals("guardar")){

				eliminarUnidAgenServCitaOdonRegistrada(forma);
				
				if(forma.getServicioOdontologico()!=null){
					
					if(guardarRegistroUnidadAgendaPorServicioCitaOdonto (forma, usuario.getLoginUsuario())){
						
						forma.inicializarNuevoRegistro();
					}
				}
				
				listarTiposCitaDisponibles(forma);
				
				return mapping.findForward("principal");
			
			}else if (estado.equals("eliminar")){
				
				if(forma.getPosicionRegistroAEliminar()!=ConstantesBD.codigoNuncaValido){
					
					DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto = forma.getListaUnidAgenServCitaOdonRegistrados().get(forma.getPosicionRegistroAEliminar());
					
					unidadAgendaServCitaOdonto.setEliminarRegistro(true);
					forma.inicializarNuevoRegistro();
					listarTiposCitaDisponibles(forma);
				}
				
				return mapping.findForward("detRegistroUnidAgenServCitaOdonto");
			
			}else if (estado.equals("limpiarRegistro")){
				
				forma.inicializarNuevoRegistro();
				
				return mapping.findForward("detRegistroUnidAgenServCitaOdonto");
				
			}else if(estado.equals("volver")){
				
				forma.reset();
				
				return mapping.findForward("volver");
				
			}else if (estado.equals("habilitar")){
				
				/*
				 * En este caso la posición a eliminar es utilizada para volver a habilitar el registro
				 * que se queria eliminar.
				 */
				if(forma.getPosicionRegistroAEliminar()!=ConstantesBD.codigoNuncaValido){
					
					DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto = forma.getListaUnidAgenServCitaOdonRegistrados().get(forma.getPosicionRegistroAEliminar());
				
					unidadAgendaServCitaOdonto.setEliminarRegistro(false);
					forma.inicializarNuevoRegistro();
					listarTiposCitaDisponibles(forma);
				}
				
				return mapping.findForward("detRegistroUnidAgenServCitaOdonto");
			}
		}
		
		return mapping.findForward("volver");
	}

	/**
	 * M&eacute;todo que activa o desactiva las listas de selecci&oacute;n y limpia los valores 
	 * seleccionados previamente en cada una de ellas.
	 * 
	 * @param forma
	 * @param acronimoTipoCitaOdontologica
	 */
	private void seleccionarTipoCitaOdontologica(UnidadAgendaServicioTipoCitaOdontoForm forma,	String acronimoTipoCitaOdontologica) {
		
		if(!UtilidadTexto.isEmpty(acronimoTipoCitaOdontologica) && !UtilidadTexto.isNumber(acronimoTipoCitaOdontologica)){
			
			forma.setTiposCitaOdontologicaHelper(acronimoTipoCitaOdontologica);
			forma.setNoActivaListaUnidadAgenda(false);

		}else{
			
			forma.setTiposCitaOdontologicaHelper("");
			forma.setNoActivaListaUnidadAgenda(true);
		}

		forma.setUnidadAgendaHelper(ConstantesBD.codigoNuncaValido);
		forma.setServicioOdontologicoHelper(ConstantesBD.codigoNuncaValido);
		forma.setNoActivaListaServiciosOdon(true);
	}

	
	
	/**
	 * M&eacute;todo que obtiene un listado con las Unidades de Agenda de tipo
	 * Odontol&oacute;gica activas disponibles para la parametrizaci&oacute;n.
	 * @param forma
	 */
	private void obtenerListaUnidadAgendaOdontologica(UnidadAgendaServicioTipoCitaOdontoForm forma) {
		
		IUnidadAgendaServTipoCitaOdontoServicio unidadAgendaServCitaOdontoServicio = UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadAgendaServCitaOdontoServicio();

		List<UnidadesConsulta> listaUnidadAgendaOdontologica = unidadAgendaServCitaOdontoServicio.listarUnidadesAgendaPorTipoPorEstado(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, true);
		
		forma.setListaUnidadAgendaOdontologica((ArrayList<UnidadesConsulta>) listaUnidadAgendaOdontologica);
	}
	
	
	/**
	 * 
	 * M&eacute;todo que se encarga de eliminar o agregar al listado de tipos de cita odontol&oacute;gica 
	 * las que pueden ser registrados. Ordena alfab&eacute;ticamente los registros existentes de la parametrizaci&oacute;n
	 * 
	 * @param listaTiposCita
	 * @param listaUnidAgenServCitaOdonRegistrados
	 */
	private void listarTiposCitaDisponibles(UnidadAgendaServicioTipoCitaOdontoForm forma) {
	
		ArrayList<DtoIntegridadDominio> listaTiposCitaOdontologica = obtenerListaTiposCita();
		
		ArrayList<DtoIntegridadDominio> listaRegistrosTipoCita = new ArrayList<DtoIntegridadDominio>();
			
		ArrayList<DtoUnidadAgendaServCitaOdonto> listaUnidAgenServCitaOdonRegistrados = new ArrayList<DtoUnidadAgendaServCitaOdonto>();
		
		for (DtoIntegridadDominio tipoCita : listaTiposCitaOdontologica) {
			
			for (DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto : forma.getListaUnidAgenServCitaOdonRegistrados()) {
				
				if(unidadAgendaServCitaOdonto.getAcronimoTipoCita().equals(tipoCita.getAcronimo())){
					
					unidadAgendaServCitaOdonto.setDescripcionTipoCita(tipoCita.getDescripcion());
					
					listaUnidAgenServCitaOdonRegistrados .add(unidadAgendaServCitaOdonto);
					
					if(!unidadAgendaServCitaOdonto.isEliminarRegistro()){
						
						listaRegistrosTipoCita.add(tipoCita);
					}
				}
			}
		}
		
		listaTiposCitaOdontologica.removeAll(listaRegistrosTipoCita);
		
		forma.setListaUnidAgenServCitaOdonRegistrados(listaUnidAgenServCitaOdonRegistrados);
		forma.setListaTiposCitaOdontologica(listaTiposCitaOdontologica);
	}

	/**
	 * M&eacute;todo que obtiene un listado con los tipos de cita odontol&oacute;gicas
	 * disponibles para la parametrizaci&oacute;n.
	 * 
	 * @param forma
	 * @return 
	 */
	private ArrayList<DtoIntegridadDominio> obtenerListaTiposCita(){

		// se genere un vector con las etiquetas a mostrar
		String[] listaConstantesStr = {   
				ConstantesIntegridadDominio.acronimoPrioritaria,
				ConstantesIntegridadDominio.acronimoAuditoria,
				ConstantesIntegridadDominio.acronimoControlCitaOdon,
				ConstantesIntegridadDominio.acronimoRevaloracion,
				ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial
		};

		// se hace una conexion para la utilidad de integridad dominio
		Connection con = UtilidadBD.abrirConexion(); 
		List<DtoIntegridadDominio> listaConstantes = Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantesStr, true);
		
		ArrayList<DtoIntegridadDominio> listaTiposCitaOdontologica = new ArrayList<DtoIntegridadDominio>(listaConstantes);
		
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			e.printStackTrace();
			Log4JManager.info("Problema cerrando la conexion - obtenerListadoTiposCita");
		}
		
		return listaTiposCitaOdontologica;
	}
	
	/**
	 * M&eacute;todo que obtiene un listado con las Unidades de Agenda - Servicio por Tipo Cita Odontol&oacute;ca
	 * que ya fueron registradas en el sistema
	 * 
	 * @param forma
	 */
	private void obtenerListadoUnidAgenServCitaOdonRegistrados(UnidadAgendaServicioTipoCitaOdontoForm forma) {
		
		IUnidadAgendaServTipoCitaOdontoServicio unidadAgendaServCitaOdontoServicio = UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadAgendaServCitaOdontoServicio();
		
		forma.setListaUnidAgenServCitaOdonRegistrados((ArrayList<DtoUnidadAgendaServCitaOdonto>) unidadAgendaServCitaOdontoServicio.obtenerListadoUnidAgenServCitaOdonRegistrados());
		
	}
	
	/**
	 * M&eacute;todo que elimina un registro de Unidades de Agenda - Servicio por Tipo Cita Odontol&oacute;ca
	 * 
	 * @param listaUnidAgenServCitaOdonRegistrados
	 */
	private void eliminarUnidAgenServCitaOdonRegistrada(UnidadAgendaServicioTipoCitaOdontoForm forma) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		ArrayList<DtoUnidadAgendaServCitaOdonto> listaRegistrosEliminar = new ArrayList<DtoUnidadAgendaServCitaOdonto>();
		
		IUnidadAgendaServTipoCitaOdontoServicio unidadAgendaServCitaOdontoServicio = UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadAgendaServCitaOdontoServicio();
		
		for (DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto : forma.getListaUnidAgenServCitaOdonRegistrados()) {
			
			if(unidadAgendaServCitaOdonto.isEliminarRegistro()){
				
				if(unidadAgendaServCitaOdontoServicio.eliminarUnidAgenServCitaOdonRegistrada(unidadAgendaServCitaOdonto.getCodigoRegistro())){
					
					listaRegistrosEliminar.add(unidadAgendaServCitaOdonto);
				}
			}
		}
		
		if(listaRegistrosEliminar.size()>0){
			
			forma.setEstado("exitoso");
			forma.getListaUnidAgenServCitaOdonRegistrados().removeAll(listaRegistrosEliminar);
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	/**
	 * M&eacute;todo que obtiene un listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 */
	private void obtenerListaServiciosPorUnidadAgenda(UnidadAgendaServicioTipoCitaOdontoForm forma, int codigoInstitucion) {
		
		IUnidadAgendaServTipoCitaOdontoServicio unidadAgendaServCitaOdontoServicio = UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadAgendaServCitaOdontoServicio();

		List<DtoUnidadAgendaServCitaOdonto> listaServiciosOdontologicos = unidadAgendaServCitaOdontoServicio.listarServiciosPorUnidadAgendaPorTarifarioPorEstado(forma.getUnidadAgenda().getCodigo(), codigoInstitucion,  true);
		
		forma.setListaServiciosOdontologicos((ArrayList<DtoUnidadAgendaServCitaOdonto>) listaServiciosOdontologicos);
		
		forma.setNoActivaListaServiciosOdon(false);
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar el registro de Unidad de Agenda - Servicio por Tipo de Cita Odontol&oacute;gica
	 * 
	 * @param dtoUnidadAgendaServCitaOdonto
	 * @return boolean indicando que se realiz&oacute; el registro satisfactoriamente
	 */
	private boolean guardarRegistroUnidadAgendaPorServicioCitaOdonto(UnidadAgendaServicioTipoCitaOdontoForm forma, String loginUsuario) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		boolean guardar = false;
		
		IUnidadAgendaServTipoCitaOdontoServicio unidadAgendaServCitaOdontoServicio = UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadAgendaServCitaOdontoServicio();
		
		DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto = forma.getServicioOdontologico();
		IUsuariosServicio usuariosServicio = getUsuariosServicio ();
		
		unidadAgendaServCitaOdonto.setUsuario(usuariosServicio.buscarPorLogin(loginUsuario));
		unidadAgendaServCitaOdonto.setAcronimoTipoCita(forma.getTiposCitaOdontologicaHelper());
		unidadAgendaServCitaOdonto.setDescripcionTipoCita(forma.getTiposCitaOdontologica().getDescripcion());
		
		long codigo = unidadAgendaServCitaOdontoServicio.guardarRegistroUnidadAgendaPorServicioCitaOdonto(unidadAgendaServCitaOdonto);
		
		if(codigo > 0){
			
			forma.getServicioOdontologico().setCodigoRegistro(codigo);
			
			forma.getListaUnidAgenServCitaOdonRegistrados().add(forma.getServicioOdontologico());
			
			UtilidadTransaccion.getTransaccion().commit();
			
			guardar = true;
		}

		if(guardar){
			
			forma.setEstado("exitoso");
			
		}else{
			
			forma.setEstado("noExitoso");
		}
		
		return guardar;
	}
	
	/**
	 * M&eacute;todo que se encarga de devolver una instancia del objeto que implementa la interfaz
	 * IUsuariosServicio
	 *
	 * @return IUsuariosServicio
	 */
	public static IUsuariosServicio getUsuariosServicio() {
		return AdministracionFabricaServicio.crearUsuariosServicio();
	}
}
