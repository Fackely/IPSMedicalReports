package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.capitacion.ParametrizarNivelAutorizacionForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;

/**
 * Esta clase se encarga de procesar las solicitudes de 
 * la parametrizaci�n de los niveles de autorizaci�n
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class ParametrizarNivelAutorizacionAction extends Action {
	
	/**
	 * 
	 * Este M�todo se encarga de ejecutar las acciones sobre las p�ginas
	 * de parametrizaci�n de niveles de autorizaci�n
	 * 
	 * 
	 * @param  ActionMapping mapping, HttpServletRequest request,
	 *         HttpServletResponse response
	 * @return ActionForward     
	 * @author Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		
		ActionForward forward=null;
		if (form instanceof ParametrizarNivelAutorizacionForm) {
			ParametrizarNivelAutorizacionForm forma = (ParametrizarNivelAutorizacionForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			forma.setMensaje("");
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			forma.setNumeroRegistrosPagina(Integer.valueOf(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			UtilidadTransaccion.getTransaccion().begin();
			try{
				if(estado.equals("empezar")){
					forma.reset();					
					llenarListas(forma);					
					forward= empezar(forma, mapping);
				}else{
					if(estado.equals("nuevo")){
						forward= nuevoRegistro(forma, mapping);
					}else{
						if(estado.equals("eliminar")){
							forward= eliminarRegistro(forma, mapping);
						}else{
							if(estado.equals("guardar")){
								forward= guardarRegistro(forma, mapping,request,usuario);
							}else if(forma.getEstado().equals("asignarPropiedad")){
								forward= null;
							}
						}
					}
				}
				UtilidadTransaccion.getTransaccion().commit();
			}catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error en la parametrizaci�n del Nivel de Autorizaci�n", e);
			}
		}
		return forward;
	}
	
	

	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @param ParametrizarNivelAutorizacionForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward empezar(ParametrizarNivelAutorizacionForm forma,ActionMapping mapping){
		INivelAutorizacionServicio autorizacionServicio = 
			CapitacionFabricaServicio.crearNivelAutorizacionServicio();
		
		ArrayList<DTONivelAutorizacion> listaNivelAutorizacion = autorizacionServicio.obtenerNivelAutorizacion();
		
		if(listaNivelAutorizacion!=null && listaNivelAutorizacion.size()>0){
			forma.setListaNivelAutorizacion(listaNivelAutorizacion);
			forma.setNumeroTotalRegistros(listaNivelAutorizacion.size());
		}else{
			forma.setListaNivelAutorizacion(null);
			forma.setMensaje("sinDatos");
			forma.setNumeroTotalRegistros(0);
		}
		return mapping.findForward("principal");	
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los listados necesarios
	 * para la p�gina de parametrizaci�n de Niveles de Autorizaci�n
	 * 
	 * @param ParametrizarNivelAutorizacionForm
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void llenarListas(ParametrizarNivelAutorizacionForm forma){
		
		IViasIngresoServicio viaIngresoServicio = 
				ManejoPacienteServicioFabrica.crearViasIngresoServicio();		
		
		forma.setListaViasIngreso(viaIngresoServicio.buscarViasIngreso());
		
		Connection conn=UtilidadBD.abrirConexion();
		
		String[] listadoIntegridadDOminio=new String[]{ConstantesIntegridadDominio.acronimoTipoAutorizacionManual,
				ConstantesIntegridadDominio.acronimoTipoAutorizacionAuto};
		
		ArrayList<DtoIntegridadDominio> listaTiposAutorizacion=Utilidades.
		generarListadoConstantesIntegridadDominio(conn, listadoIntegridadDOminio, false);
				
		UtilidadBD.closeConnection(conn);	
				
		forma.setListaTiposAutorizacion(listaTiposAutorizacion);		
	}
	
	/**
	 * 
	 * Este M�todo se encarga de crear un nuevo registro de nivel
	 * de autorizaci�n
	 * 
	 * @return ActionForward
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward nuevoRegistro(ParametrizarNivelAutorizacionForm forma, ActionMapping mapping){
		DTONivelAutorizacion nuevoRegistro = new DTONivelAutorizacion();
		
		nuevoRegistro.setPermiteEliminar(true);
		nuevoRegistro.setActivo(true);
		
		if(forma.getListaNivelAutorizacion()==null){
			forma.setListaNivelAutorizacion(new ArrayList<DTONivelAutorizacion>());
		}
		
		forma.getListaNivelAutorizacion().add(nuevoRegistro);
		
		return mapping.findForward("principal");	
		
	}			
	
	
	/**
	 * 
	 * Este M�todo se encarga de guardar un registro de nivel
	 * de autorizaci�n
	 * 
	 * @return ActionForward 
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward guardarRegistro(ParametrizarNivelAutorizacionForm forma, 
			ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioSesion){
		INivelAutorizacionServicio autorizacionServicio = 
			CapitacionFabricaServicio.crearNivelAutorizacionServicio();
		NivelAutorizacion nivel = null;
		
		if(forma.getListaNivelAutorizacion()!=null && forma.getListaNivelAutorizacion().size()>0){
			ActionErrors errores = validarRegistroUnico(forma.getListaNivelAutorizacion());
			if(errores.isEmpty()){
				for(DTONivelAutorizacion registro : forma.getListaNivelAutorizacion()){
					
					if(registro.isValorModificado()){
						IUsuariosServicio usuarioServicio = AdministracionFabricaServicio.crearUsuariosServicio();
						IViasIngresoServicio viaIngresoServicio = ManejoPacienteServicioFabrica.crearViasIngresoServicio();						
						nivel = new NivelAutorizacion();
						
						ViasIngreso viaIngreso = viaIngresoServicio.findbyId(registro.getViasIngresoPK());						
						Usuarios usuario = usuarioServicio.buscarPorLogin(usuarioSesion.getLoginUsuario());
						
						nivel.setUsuarios(usuario);
						nivel.setViasIngreso(viaIngreso);			
						nivel.setCodigoPk(registro.getCodigoPk());						
						nivel.setFechaRegistro(Calendar.getInstance().getTime());
						nivel.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
								.getInstance().getTime()));
						nivel.setActivo(registro.isActivo());
						nivel.setDescripcion(registro.getDescripcion());
						nivel.setTipoAutorizacion(registro.getTipoAutorizacionAcronimo());
						
						autorizacionServicio.actualizarRegistroNivelAutorizacion(nivel);
					}					
				}
				forma.setMensaje("exitoso");
				return empezar(forma, mapping);
			}else{
				saveErrors(request, errores);
			}						
		}		
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de nivel
	 * de autorizaci�n
	 * 
	 * @return ActionForward 
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward eliminarRegistro(ParametrizarNivelAutorizacionForm forma, ActionMapping mapping){
		INivelAutorizacionServicio autorizacionServicio = 
			CapitacionFabricaServicio.crearNivelAutorizacionServicio();
		
		int indiceEliminar = forma.getIndice();
		int i=0;
		
		for(DTONivelAutorizacion registro : forma.getListaNivelAutorizacion()){
			if(i == indiceEliminar){
				if(registro.getCodigoPk()>0){
					autorizacionServicio.eliminarNivelAutorizacion(registro.getCodigoPk());
					forma.setMensaje("exitoso");
					return empezar(forma, mapping);
				}else{
					forma.getListaNivelAutorizacion().remove(indiceEliminar);
				}
				forma.setMensaje("exitoso");				
				break;
			}
			i++;
		}		
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * Este M�todo se encarga de Validar que no exita otro registro
	 * igual al que se est� tratando de guardar
	 * 
	 * @param ArrayList<DTONivelAutorizacion> lista
	 * @return ActionErrors
	 *   
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionErrors validarRegistroUnico(ArrayList<DTONivelAutorizacion> lista){		
		ActionErrors errores = new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ParametrizarNivelAutorizacionForm");
		int indice=0;		
		for(DTONivelAutorizacion registroValidar :lista){
			int j=0; 
			for(DTONivelAutorizacion registro :lista){
				if(j>indice){
					if(registroValidar.equals(registro)){
						errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("parametrizarNivelAutorizacionForm.registroRepetido",j+1)));
					}
				}
				j++;
			}
			indice++;
		}		
		return errores;
	}

}
