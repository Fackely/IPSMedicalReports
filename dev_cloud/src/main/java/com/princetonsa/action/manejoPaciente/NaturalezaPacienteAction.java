package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.Errores;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.NaturalezaPacienteForm;
import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.INaturalezaPacienteServicio;

/**
 * Esta clase se encarga de manejar las solicitudes generales de la
 * entidad Naturaleza Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 11/08/2010
 */
public class NaturalezaPacienteAction extends Action{
	
	/**
	 * Instancia de la entidad 	INaturalezaPacienteServicio
	 */
	INaturalezaPacienteServicio naturalezaServicio = ManejoPacienteServicioFabrica.
	crearNaturalezaPacienteServicio();

	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		if (form instanceof NaturalezaPacienteForm) {
			NaturalezaPacienteForm forma = (NaturalezaPacienteForm) form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			UsuarioBasico usuarioSesion = Utilidades.getUsuarioBasicoSesion(request
					.getSession());

			try {
				UtilidadTransaccion.getTransaccion().begin();
				if(estado.equals("empezar")){
					forma.setOffset(new Integer(0));
					buscarRegistros(forma,request,response);
				}
				if(estado.equals("eliminar")){
					eliminarRegistros(forma,request,response);
				}				
				if(estado.equals("nuevo")){
					crearRegistro(forma,request,response);					
				}
				if(estado.equals("guardar")){
					guardarRegistros(forma, request,response);
				}
				if(estado.equals("ordenar"))
				{
					accionOrdenar(forma, usuarioSesion, mapping);
				}
				UtilidadTransaccion.getTransaccion().commit();
				return mapping.findForward("principal");
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error en la transaccion de Naturaleza Paciente", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar los registros de 
	 * las natrualezas paciente registradas en el sistema
	 * 
	 * @param NaturalezaPacienteForm
	 * @param HttpServletRequest
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void buscarRegistros(NaturalezaPacienteForm forma,
			HttpServletRequest request,HttpServletResponse response )throws Exception{
		
		ArrayList<DTONaturalezaPaciente> listaNaturalezasPaciente = 
			naturalezaServicio.consultarNaturalezaPacienteEliminable();
		
		if(listaNaturalezasPaciente!=null && listaNaturalezasPaciente.size()>0){
			forma.setLongitudListaInical(listaNaturalezasPaciente.size());
			forma.setListaNaturalezasPaciente(listaNaturalezasPaciente);
		}else{
			crearRegistro(forma,request,response);
		}		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de guardar los registros de 
	 * las naturalezas de paciente ingresadas y modificadas
	 * por el usuario
	 * 
	 * @param NaturalezaPacienteForm
	 * @param HttpServletRequest
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void guardarRegistros(NaturalezaPacienteForm forma,
			HttpServletRequest request,HttpServletResponse response )throws Exception{
		
		NaturalezaPacientes nuevoRegistro =null;
		ArrayList<Errores> listaErrores=null;
		ActionErrors actionError=new ActionErrors();
		boolean nuevo=false;
		
		for(int i =0; i<forma.getListaNaturalezasPaciente().size();i++){				
			nuevo=false;	
			nuevoRegistro = new NaturalezaPacientes(); 
			nuevoRegistro= forma.getListaNaturalezasPaciente().get(i);
				
			if(i>=forma.getLongitudListaInical()){
				nuevo=true;
				nuevoRegistro.setActivo(true);
				//MT6471 se agrega codigo para la actualización
				nuevoRegistro.setCodigo(UtilidadBD.obtenerSiguienteValorSecuencia("seq_naturaleza_pacientes"));
			}							
				
			listaErrores=naturalezaServicio.validarNaturalezaPaciente(nuevoRegistro, nuevo);
			if(listaErrores!=null && listaErrores.size()>0){
				for(Errores error : listaErrores){
					actionError.add(error.getMessage(), error.getActionMessage());					
				}
			}
			if(actionError.size()==0){	
				listaErrores=guardarListaRegistros(nuevoRegistro, request,forma,nuevo);
				if(listaErrores!=null && listaErrores.size()>0){
					for(Errores error : listaErrores){
						actionError.add(error.getMessage(), error.getActionMessage());
					}
				}
			}				
		}
		if(actionError.size()>0){
			saveErrors(request, actionError);
		}else{
			forma.setMostrarMensaje("resumen");
			buscarRegistros(forma, request,response);
		}					
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de guardar y validar los registros
	 * de una naturaleza paciente
	 * 
	 * @param NaturalezaPacientes
	 * @param HttpServletRequest
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ArrayList<Errores> guardarListaRegistros(NaturalezaPacientes nuevoRegistro, 
			HttpServletRequest request,NaturalezaPacienteForm forma, boolean nuevo){
		
		ArrayList<Errores> listaErrores=null;
		
		Usuarios usuario = new Usuarios();			
		UsuarioBasico usuarioSesion = Utilidades.getUsuarioBasicoSesion(request
				.getSession());
		usuario.setLogin(usuarioSesion.getLoginUsuario());
		nuevoRegistro.setFecha(Calendar.getInstance().getTime());
		nuevoRegistro.setHora(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		nuevoRegistro.setUsuarios(usuario);			
			
		listaErrores= naturalezaServicio.crearNaturalezaPaciente(
				nuevoRegistro, nuevo);
		
		return listaErrores;
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de eliminar un registro de 
	 * naturaleza de paciente
	 * 
	 * @param NaturalezaPacienteForm
	 * @param HttpServletRequest
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistros(NaturalezaPacienteForm forma,
			HttpServletRequest request,HttpServletResponse response )throws Exception{
		if(forma.getIndex()<forma.getLongitudListaInical()){
			NaturalezaPacientes registro = 
				forma.getListaNaturalezasPaciente().get(forma.getIndex());
			
			if(!naturalezaServicio.eliminarNaturalezaPaciente(registro)){
				ActionErrors errores=new ActionErrors();
					errores.add("No se pudo eliminar", new ActionMessage("errores.modManejoPaciente.eliminacionFallida"));
					saveErrors(request, errores);			
			}else{
				forma.setMostrarMensaje("resumen");
				buscarRegistros(forma, request,response);
			}
		}else{
			forma.getListaNaturalezasPaciente().remove(forma.getIndex());			
		}		
		redireccionar (forma,request, response);
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear un registro vacío para que se pueda
	 * ingresar una nueva naturaleza de paciente
	 * 
	 * @param NaturalezaPacienteForm 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void crearRegistro(NaturalezaPacienteForm forma, HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		DTONaturalezaPaciente nuevoRegistro= new DTONaturalezaPaciente();		
		nuevoRegistro.setNombre("");
		nuevoRegistro.setActivo(true);	
		nuevoRegistro.setPermiteEliminar(true);
		if(forma.getListaNaturalezasPaciente()==null){
			forma.setListaNaturalezasPaciente(new ArrayList<DTONaturalezaPaciente>());
		}		
		forma.getListaNaturalezasPaciente().add(nuevoRegistro);
		redireccionar(forma,request,response);
	}
	
	/**
     * M&eacute;todo implementado para posicionarse en la &uacute;ltima
     * p&aacutegina del pager.
     * 
     * @param NaturalezaPacienteForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     */
    public void redireccionar (NaturalezaPacienteForm forma,
    		HttpServletRequest request,HttpServletResponse response)throws Exception{    	
    	UsuarioBasico usuarioSesion = Utilidades.getUsuarioBasicoSesion(request
				.getSession());
    	int registrosPorPagina = ValoresPorDefecto.getMaxPageItemsInt(
				usuarioSesion.getCodigoInstitucionInt());
    	String enlace="parametrizarNaturalezaPaciente.jsp";
    	
    	forma.setOffset(((int)((forma.getListaNaturalezasPaciente().size()-1)/registrosPorPagina))*registrosPorPagina);
        if(request.getParameter("ultimaPage")==null){        	
        	       	
        	if(forma.getListaNaturalezasPaciente().size() > (forma.getOffset()+registrosPorPagina))
               forma.setOffset(((int)(forma.getListaNaturalezasPaciente().size()/registrosPorPagina))*registrosPorPagina);
            try{
                response.sendRedirect(enlace+"?pager.offset="+forma.getOffset());
            }catch (IOException e){                
                e.printStackTrace();
            }
        }
        else{    
            String ultimaPagina=request.getParameter("ultimaPage");
            String tempOffset="offset=";
            int posOffSet=ultimaPagina.indexOf(tempOffset)+tempOffset.length();
            if(forma.getListaNaturalezasPaciente().size()>(forma.getOffset()+registrosPorPagina)){
            	forma.setOffset(forma.getOffset()+registrosPorPagina);
            }                
            try{
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+forma.getOffset());
            } 
            catch (IOException e){                
                e.printStackTrace();
                throw e;
            }
       }
    }
    
    /**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 */
	public void accionOrdenar(NaturalezaPacienteForm forma, 
			UsuarioBasico usuario, ActionMapping mapping){
		
		boolean ordenamiento = false;		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			ordenamiento = true;
		}		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaNaturalezasPaciente(),sortG);
	} 
   
}

