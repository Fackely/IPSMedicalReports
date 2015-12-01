/**
 * 
 */
package com.servinte.axioma.action.capitacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.axioma.util.log.Log4JManager;

import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.actionForm.capitacion.ConsultaCargueUsuarioForm;
import com.servinte.axioma.bl.administracion.facade.AdministracionFacade;
import com.servinte.axioma.bl.capitacion.facade.CapitacionFacade;
import com.servinte.axioma.dto.administracion.TipoAfiliadoDto;
import com.servinte.axioma.dto.administracion.TipoIdentificacionDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author jeilones
 * @created 5/10/2012
 *
 */
public class ConsultaCargueUsuarioAction extends DispatchAction{

	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	private static final String KEY_ERROR_CAST="errors.castForm";
	
	private static final String FORWARD_FILTRAR_CARGUES="filtrarCarguesCapitacion";
	private static final String FORWARD_RESULTADO_CARGUES_USUARIOS="resultadoConsultaCarguesUsuario";
	private static final String FORWARD_DETALLE_CARGUES_USUARIOS="detalleCargueUsuario";
	
	public ActionForward filtrarConsultaCargueUsuario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores=new ActionMessages();
		AdministracionFacade administracionFacade=new AdministracionFacade();
		ConsultaCargueUsuarioForm forma=null;
		
		
		try {
			
			if(form instanceof ConsultaCargueUsuarioForm){
				forma = (ConsultaCargueUsuarioForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			forma.setUsuarioCapitadoSeleccionado(new DtoUsuariosCapitados());
			forma.setFiltroUsuarioCapitado(new DtoUsuariosCapitados());
			forma.setListaUsuariosCapitados(new ArrayList<DtoUsuariosCapitados>(0));
			
			List<TipoIdentificacionDto>listaTiposIdentificacionDto=administracionFacade.consultarTiposIdentificacion();
			forma.setTiposIdentificacionDto(listaTiposIdentificacionDto);
			
			List<TipoAfiliadoDto>listaTiposAfiliadoDto=administracionFacade.consultarTiposAfiliados();
			forma.setTiposAfiliadoDto(listaTiposAfiliadoDto);
			
			UsuarioBasico usuarioActual = Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			/*int maxDigitos=20;
			String stringMaxDigitos=ValoresPorDefecto.getNumDigCaptNumIdPac(usuarioActual.getCodigoInstitucionInt());
			if(!UtilidadTexto.isEmpty(stringMaxDigitos)){
				maxDigitos=Integer.parseInt(stringMaxDigitos);
				if(maxDigitos<=0){
					maxDigitos=20;
				}
			}
			
			forma.setMaxDigitosNumIdentificacion(maxDigitos);*/
			
			forma.setNumeroRegistrosPagina(Integer.valueOf(ValoresPorDefecto.getMaxPageItems(usuarioActual.getCodigoInstitucionInt())));
			
		} catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return mapping.findForward(FORWARD_FILTRAR_CARGUES);
	}
	public ActionForward consultarCarguesUsuario(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores=new ActionMessages();
		ConsultaCargueUsuarioForm forma=null;
		try{
			if(form instanceof ConsultaCargueUsuarioForm){
				forma = (ConsultaCargueUsuarioForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			CapitacionFacade capitacionFacade= new CapitacionFacade();
			
			List<DtoUsuariosCapitados> usuariosCapitados=capitacionFacade.consultarCargueUsuarios(true, forma.getFiltroUsuarioCapitado());
			
			forma.setListaUsuariosCapitados(usuariosCapitados);
			
			if(usuariosCapitados.isEmpty()){
				errores.add("", new ActionMessage("errores.ConsultaCargueUsuario.noExisteInfo"));
			}else{
				forma.setNumeroOrdenesPorAutorizar(usuariosCapitados.size());
				if(usuariosCapitados.size()==1){
					forma.setUsuarioCapitadoSeleccionado(usuariosCapitados.get(0));
					
					forma.setPosicionUsuarioCapitado("0");
					
					return consultarDetalleCargue(mapping, forma, request, response);
				}
			}
			
		}catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward(FORWARD_FILTRAR_CARGUES);
		}
		return mapping.findForward(FORWARD_RESULTADO_CARGUES_USUARIOS);
	}

	
	public ActionForward consultarDetalleCargue(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response){
		
		ActionMessages errores=new ActionMessages();
		ConsultaCargueUsuarioForm forma=null;
		try{
			if(form instanceof ConsultaCargueUsuarioForm){
				forma = (ConsultaCargueUsuarioForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			CapitacionFacade capitacionFacade= new CapitacionFacade();
			
			DtoUsuariosCapitados usuarioCapitado=forma.getListaUsuariosCapitados().get(Integer.parseInt(forma.getPosicionUsuarioCapitado()));
			
			forma.setUsuarioCapitadoSeleccionado(usuarioCapitado);
			
			usuarioCapitado=capitacionFacade.consultarDetalleCargue(true, usuarioCapitado);
			
			forma.setUsuarioCapitadoSeleccionado(usuarioCapitado);
		}catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward(FORWARD_RESULTADO_CARGUES_USUARIOS);
		}
		
		return mapping.findForward(FORWARD_DETALLE_CARGUES_USUARIOS);
	}
	
	/**
	 * Metodo encargado de implementar la logica web cuando se realiza ordenamiento por alguna columna
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	public ActionForward actionOrdenar(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			ConsultaCargueUsuarioForm forma=null;
			if(form instanceof ConsultaCargueUsuarioForm){
				forma = (ConsultaCargueUsuarioForm) form;
			}
			else{
				throw new ClassCastException();
			}
			boolean ordenamiento = false;
			if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
				ordenamiento = true;
			}
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(forma.getListaUsuariosCapitados(),sortG);
			
		}
		catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_RESULTADO_CARGUES_USUARIOS);
	}
}
