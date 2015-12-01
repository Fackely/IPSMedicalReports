package com.princetonsa.action.facturacion;



import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadFecha;
import util.UtilidadLog;

import com.princetonsa.actionform.facturacion.ContactosEmpresaForm;
import com.princetonsa.dto.facturacion.DtoContactoEmpresa;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ContactosEmpresa;
import com.princetonsa.sort.odontologia.SortContactosEmpresa;


public class ContactosEmpresaAction extends Action   {
	
	private Logger logger = Logger.getLogger(ContactosEmpresaAction.class);
	
	
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
		 		{
						
		
		
		if(form instanceof ContactosEmpresaForm)
						{
							ContactosEmpresaForm forma= (ContactosEmpresaForm)form;
							UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
							logger.info("************************************************************************estado-->"+forma.getEstado());
							logger.info("************************************************************************codEmpresa-->"+forma.getCodEmpresa());
							  
							
						if(forma.getEstado().equals("empezar"))
						{
							
							return accionNuevo(mapping, forma,usuario);
						}
						else if(forma.getEstado().equals("guardar"))
				            {
							return accionGuardar(mapping, forma, usuario);
				            }
						
				        else if(forma.getEstado().equals("mostrarErrores") || forma.getEstado().equals("mostrarErroresModificar"))
				            {
				            				        	
				            }
						
				        else if(forma.getEstado().equals("nuevo"))
				            {
				        	forma.reset();
				        	forma.getContactosEmpresa().setEmpresa(forma.getCodEmpresa());//se puede mejorar
				        	forma.setListContactosEmpresa(ContactosEmpresa.cargar(forma.getContactosEmpresa()));
				        	forma.setEstado("nuevo");
				        	return mapping.findForward("paginaPrincipal");
 					        	}
						
				        else if(forma.getEstado().equals("ordenar"))
				            {    
				        		return this.accionOrdenarContactos(mapping, forma);
				            }
				        else if(forma.getEstado().equals("modificar"))
				            {
				            	forma.setContactosEmpresa((DtoContactoEmpresa)forma.getListContactosEmpresa().get(forma.getPosArray()).clone());
				            	//Creo el log para la modificacion
				            	this.crearLog(forma);
				            	return mapping.findForward("paginaPrincipal");
				            }
				        
				        else if(forma.getEstado().equals("guardarModificar"))
				            {
					        	return accionGuardarModificar(mapping, forma, usuario);
						    }	
				        else if(forma.getEstado().equals("eliminar"))
				            {
			        			return accionEliminar(mapping, forma, usuario);
				            }
				       }
						return null;
				}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping,
			ContactosEmpresaForm forma, UsuarioBasico usuario) {
		DtoContactoEmpresa dtoEliminar = new DtoContactoEmpresa();
		dtoEliminar.setCodigo(forma.getListContactosEmpresa().get(forma.getPosArray()).getCodigo());
		
		
		DtoContactoEmpresa dtoWhere = new DtoContactoEmpresa();
	   	dtoWhere = forma.getListContactosEmpresa().get(forma.getPosArray());
	   	dtoWhere.setEmpresa(forma.getCodEmpresa());
	   	UtilidadLog.generarLog(usuario, dtoWhere, null,  ConstantesBD.tipoRegistroLogEliminacion, ConstantesBD.logContactosEmpresa );		
	   	
	   	
		ContactosEmpresa.eliminar(dtoEliminar);
		return accionNuevo(mapping, forma, usuario);
	}
		
	
	/**
		 * 
		 * @param mapping
		 * @param forma
		 * @param usuario
		 * @return
		 */

	private ActionForward accionGuardar(ActionMapping mapping,
			ContactosEmpresaForm forma, UsuarioBasico usuario) {
		forma.getContactosEmpresa().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getContactosEmpresa().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getContactosEmpresa().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getContactosEmpresa().setInstitucion(usuario.getCodigoInstitucionInt());
		
		logger.info("************************************************************************codEmpresa 2 -->"+forma.getCodEmpresa());
		ContactosEmpresa.guardar(forma.getContactosEmpresa());
		
		//Genero el log para guardar
		String log=forma.getLogContactosOriginal()+"\n====INFORMACION DE CONTACTOS AL GUARDAR===== ";
		log+="\n\n---Contacto ";
		log+="\nCargo: "+forma.getContactosEmpresa().getCargo();
		log+="\nDireccion: "+forma.getContactosEmpresa().getDireccion();
		log+="\nEmail: "+forma.getContactosEmpresa().getEmail();
		log+="\nEmpresa: "+forma.getCodEmpresa();
		log+="\nFecha Modifica: "+forma.getContactosEmpresa().getFechaModifica();
		log+="\nHora Modifica: "+forma.getContactosEmpresa().getHoraModifica();
		log+="\nInstitucion: "+forma.getContactosEmpresa().getInstitucion();
		log+="\nNombre: "+forma.getContactosEmpresa().getNombre();
		log+="\nTelefono: "+forma.getContactosEmpresa().getTelefono();
		log+="\nUsuario Modifica: "+forma.getContactosEmpresa().getUsuarioModifica();
		log+="\nCodigo: "+forma.getContactosEmpresa().getCodigo();
		
		LogsAxioma.enviarLog(ConstantesBD.logEmpresaCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
		//Fin generacion del log para contactos
		
		forma.reset();
		forma.getContactosEmpresa().setEmpresa(forma.getCodEmpresa());
		forma.getContactosEmpresa().setInstitucion(usuario.getCodigoInstitucionInt());
;		forma.setListContactosEmpresa(ContactosEmpresa.cargar(forma.getContactosEmpresa()));
		forma.setEstado("resumen");
		return mapping.findForward("paginaPrincipal");
	}

	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificar(ActionMapping mapping,
			ContactosEmpresaForm forma, UsuarioBasico usuario) {
		forma.getContactosEmpresa().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getContactosEmpresa().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getContactosEmpresa().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getContactosEmpresa().setInstitucion(usuario.getCodigoInstitucionInt());
		ContactosEmpresa.modificar(forma.getContactosEmpresa());
		
		//Genero el log para guardar
		String log=forma.getLogContactosOriginal()+"\n====INFORMACION DE CONTACTOS DESPUES DE LA MODIFICACION===== ";
		log+="\n\n---Contacto ";
		log+="\nCargo: "+forma.getContactosEmpresa().getCargo();
		log+="\nDireccion: "+forma.getContactosEmpresa().getDireccion();
		log+="\nEmail: "+forma.getContactosEmpresa().getEmail();
		log+="\nEmpresa: "+forma.getCodEmpresa();
		log+="\nFecha Modifica: "+forma.getContactosEmpresa().getFechaModifica();
		log+="\nHora Modifica: "+forma.getContactosEmpresa().getHoraModifica();
		log+="\nInstitucion: "+forma.getContactosEmpresa().getInstitucion();
		log+="\nNombre: "+forma.getContactosEmpresa().getNombre();
		log+="\nTelefono: "+forma.getContactosEmpresa().getTelefono();
		log+="\nUsuario Modifica: "+forma.getContactosEmpresa().getUsuarioModifica();
		log+="\nCodigo: "+forma.getContactosEmpresa().getCodigo();
		
		LogsAxioma.enviarLog(ConstantesBD.logEmpresaCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
		//Fin generacion del log para contactos
		
        
		DtoContactoEmpresa dtoNuevo = new DtoContactoEmpresa();
		dtoNuevo= forma.getContactosEmpresa();
		dtoNuevo.setEmpresa(forma.getCodEmpresa());
		
		DtoContactoEmpresa dtoWhere = new DtoContactoEmpresa();
	   	dtoWhere = forma.getListContactosEmpresa().get(forma.getPosArray());
		dtoWhere.setEmpresa(forma.getCodEmpresa());

		forma.setEstado("resumen");
		
		return accionNuevo(mapping, forma, usuario);
	}
			/**
		 * 
		 * @param mapping
		 * @param forma
		 * @param usuario
		 * @return
		 */
		private ActionForward accionNuevo(ActionMapping mapping,
			ContactosEmpresaForm forma, UsuarioBasico usuario) {
		forma.reset();
		forma.getContactosEmpresa().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getContactosEmpresa().setEmpresa(forma.getCodEmpresa());//se puede mejorar
		forma.setListContactosEmpresa(ContactosEmpresa.cargar(forma.getContactosEmpresa()));
		
		
		return mapping.findForward("paginaPrincipal");
	}
		
		/**
		 * 
		 */
		private void crearLog(ContactosEmpresaForm forma)
		{
			String log="\n====INFORMACION DE CONTACTOS ANTES DE LA MODIFICACION===== ";
			log+="\n\n---Contacto ";
			log+="\nCargo: "+forma.getContactosEmpresa().getCargo();
			log+="\nDireccion: "+forma.getContactosEmpresa().getDireccion();
			log+="\nEmail: "+forma.getContactosEmpresa().getEmail();
			log+="\nEmpresa: "+forma.getCodEmpresa();
			log+="\nFecha Modifica: "+forma.getContactosEmpresa().getFechaModifica();
			log+="\nHora Modifica: "+forma.getContactosEmpresa().getHoraModifica();
			log+="\nInstitucion: "+forma.getContactosEmpresa().getInstitucion();
			log+="\nNombre: "+forma.getContactosEmpresa().getNombre();
			log+="\nTelefono: "+forma.getContactosEmpresa().getTelefono();
			log+="\nUsuario Modifica: "+forma.getContactosEmpresa().getUsuarioModifica();
			log+="\nCodigo: "+forma.getContactosEmpresa().getCodigo();
			
			forma.setLogContactosOriginal(log);
		}
		
		/**
		 * 
		 */
		private ActionForward accionOrdenarContactos(ActionMapping mapping,ContactosEmpresaForm forma) 
		{
			SortContactosEmpresa sort= new SortContactosEmpresa();
			sort.setPatronOrdenar(forma.getPatronOrdenar());
			Collections.sort(forma.getListContactosEmpresa(), sort);
			forma.setEstado(forma.getEstadoAnterior());
			return mapping.findForward("paginaPrincipal");
		}
	

}
