package com.princetonsa.action.parametrizacion;

import java.io.IOException;
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
import org.axioma.util.log.Log4JManager;

import util.Errores;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.ExcepcionesNaturalezaPacienteForm;
import com.princetonsa.dto.manejoPaciente.DTOExcepcionNaturalezaPaciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.TiposRegimen;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IExcepcionesNaturalezaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.INaturalezaPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposRegimenServicio;


/**
 * Clase usada para controlar los procesos de la
 * funcionalidad Excepci&oacute;n Por Naturaleza del Paciente
 *
 * @version 2.0
 * @author Angela Aguirre
 */
public class ExcepcionesNaturalezaPacienteAction extends Action{
	
	/**
	 * Instancia de la entidad 	IExcepcionNaturalezaPacienteServicio
	 */
	IExcepcionesNaturalezaServicio excepcionServicio = ManejoPacienteServicioFabrica.
		crearExcepcionNaturalezaServicio();
	
	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		if (form instanceof ExcepcionesNaturalezaPacienteForm) {
			
			ExcepcionesNaturalezaPacienteForm forma = (ExcepcionesNaturalezaPacienteForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			UsuarioBasico usuarioSesion = Utilidades.getUsuarioBasicoSesion(request
					.getSession());

			try {
				UtilidadTransaccion.getTransaccion().begin();
				if(estado.equals("empezar_insercion")){
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
	 * las excepciones para naturalezas paciente registradas en el sistema
	 * 
	 * @param ExcepcionesNaturalezaPacienteForm
	 * @param HttpServletRequest
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void buscarRegistros(ExcepcionesNaturalezaPacienteForm forma,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		ArrayList<DTOExcepcionNaturalezaPaciente> listaExcepciones = 
			excepcionServicio.consultarExcepcionNaturalezaDTO();
		
		llenarListas(forma);
		
		if(listaExcepciones!=null && listaExcepciones.size()>0){
			forma.setLongitudListaInical(listaExcepciones.size());
			forma.setListaExcepcionesNaturalezaPaciente(listaExcepciones);			
		}else{
			crearRegistro(forma,request,response);
		}		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de llenar las listas de 
	 * tipos de r&eacute;gimen y naturalezas paciente
	 * 
	 * @param ExcepcionesNaturalezaPacienteForm
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void llenarListas(ExcepcionesNaturalezaPacienteForm forma){
		INaturalezaPacienteServicio naturalezaServicio = 
			ManejoPacienteServicioFabrica.crearNaturalezaPacienteServicio();		
		ITiposRegimenServicio tipoRegimenServicio = 
			ManejoPacienteServicioFabrica.crearTipoRegimenServicio();
		
		forma.setListaNaturalezasPaciente(naturalezaServicio.consultarNaturalezaPacientes());		
		forma.setListaTiposRegimen(tipoRegimenServicio.consultarTiposRegimen());		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de guardar los registros de 
	 * las excepciones de naturalezas de paciente ingresadas y modificadas
	 * por el usuario
	 * 
	 * @param NaturalezaPacienteForm
	 * @param HttpServletRequest
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void guardarRegistros(ExcepcionesNaturalezaPacienteForm forma,
			HttpServletRequest request,HttpServletResponse response )throws Exception{
		
		DTOExcepcionNaturalezaPaciente registroDTO =null;
		ArrayList<Errores> listaErrores=null;
		ActionErrors actionError=new ActionErrors();
		boolean nuevo;
		ExcepcionesNaturaleza excepcion;
		TiposRegimen tipoRegimen;
		NaturalezaPacientes naturaleza;
		INaturalezaPacienteServicio naturalezaSevicio = ManejoPacienteServicioFabrica.crearNaturalezaPacienteServicio();
		ITiposRegimenServicio tiposRegimenServicio = ManejoPacienteServicioFabrica.crearTipoRegimenServicio();
		boolean firstPage=true;
				
		for(int i =0; i<forma.getListaExcepcionesNaturalezaPaciente().size();i++){				
			nuevo=false;	
			registroDTO= forma.getListaExcepcionesNaturalezaPaciente().get(i);
			excepcion = new ExcepcionesNaturaleza();
						
			if(i>=forma.getLongitudListaInical()){
				nuevo=true;
				registroDTO.setActivo(true);
			}else{
				excepcion.setCodigo(registroDTO.getCodigo());
			}			
			
			/*tipoRegimen = new TiposRegimen();
			naturaleza = new NaturalezaPacientes();			
			
			tipoRegimen.setAcronimo(registroDTO.getAcronimoTipoRegimen());
			naturaleza.setCodigo(registroDTO.getCodigoNaturalezaPaciente());*/
			
			tipoRegimen = tiposRegimenServicio.findByID(registroDTO.getAcronimoTipoRegimen());
			naturaleza = naturalezaSevicio.fidByID(registroDTO.getCodigoNaturalezaPaciente());
						
			excepcion.setTiposRegimen(tipoRegimen);
			excepcion.setNaturalezaPacientes(naturaleza);
			excepcion.setActivo(registroDTO.isActivo());
							
			listaErrores=excepcionServicio.validarExcepcionNaturalezaPaciente(excepcion, nuevo);
			if(listaErrores!=null && listaErrores.size()>0){
				for(Errores error : listaErrores){
					actionError.add(error.getMessage(), error.getActionMessage());					
				}
			}
			if(actionError.size()==0){	
				listaErrores=guardarListaRegistros(excepcion, request,nuevo);
				if(listaErrores!=null && listaErrores.size()>0){
					for(Errores error : listaErrores){
						actionError.add(error.getMessage(), error.getActionMessage());						
					}
					buscarRegistros(forma,request,response);
				}
			}				
		}
		if(actionError.size()>0){
			saveErrors(request, actionError);
		}else{
			forma.setMostrarMensaje("resumen");
			buscarRegistros(forma,request,response);
		}		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de guardar y validar los registros
	 * de una naturaleza paciente
	 * 
	 * @param 
	 * @param HttpServletRequest
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ArrayList<Errores> guardarListaRegistros(ExcepcionesNaturaleza excepcion, 
			HttpServletRequest request,boolean nuevo){
		
		ArrayList<Errores> listaErrores=null;
		Usuarios usuario = new Usuarios();
		UsuarioBasico usuarioSesion = Utilidades.getUsuarioBasicoSesion(request
				.getSession());			
		
		usuario.setLogin(usuarioSesion.getLoginUsuario());					
		excepcion.setFecha(Calendar.getInstance().getTime());
		excepcion.setHora(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		excepcion.setUsuarios(usuario);			
			
		listaErrores= excepcionServicio.crearExcepcionNaturaleza(excepcion, nuevo);
		
		return listaErrores;
	}
	
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear un registro vacío para que se pueda
	 * ingresar una nueva excepci&oacute;n de naturaleza de paciente
	 * 
	 * @param ExcepcionesNaturalezaPacienteForm 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void crearRegistro(ExcepcionesNaturalezaPacienteForm forma,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		int codigoNaturalezaPacienteInciial=-1;
		DTOExcepcionNaturalezaPaciente nuevoRegistro= new 
			DTOExcepcionNaturalezaPaciente(codigoNaturalezaPacienteInciial);		
		nuevoRegistro.setActivo(true);		
		if(forma.getListaExcepcionesNaturalezaPaciente()==null){
			forma.setListaExcepcionesNaturalezaPaciente(new ArrayList<DTOExcepcionNaturalezaPaciente>());
		}		
		forma.getListaExcepcionesNaturalezaPaciente().add(nuevoRegistro);
		redireccionar(forma,request,response);
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de eliminar un registro de 
	 * excepci&oacute;n de naturaleza de paciente
	 * 
	 * @param ExcepcionesNaturalezaPacienteForm
	 * @param HttpServletRequest
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistros(ExcepcionesNaturalezaPacienteForm forma,
			HttpServletRequest request, HttpServletResponse response )throws Exception{
		if(forma.getIndex()<forma.getLongitudListaInical()){
			DTOExcepcionNaturalezaPaciente registro = 
				forma.getListaExcepcionesNaturalezaPaciente().get(forma.getIndex());
			
			if(!excepcionServicio.eliminarExcepcionNaturaleza(registro)){
				ActionErrors errores=new ActionErrors();
				errores.add("No se pudo eliminar el registro", 
						new ActionMessage("errores.modManejoPacienteExcepcionNaturaleza.eliminacionFallida"));
				saveErrors(request, errores);				
			}else{
				forma.setMostrarMensaje("resumen");
				buscarRegistros(forma,request,response);
			}
		}else{
			forma.getListaExcepcionesNaturalezaPaciente().remove(forma.getIndex());			
		}		
		redireccionar (forma,request,response);
	}	
	
	/**
     * M&eacute;todo implementado para posicionarse en la &uacute;ltima
     * p&aacutegina del pager.
     * 
     * @param NaturalezaPacienteForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     */
    public void redireccionar (ExcepcionesNaturalezaPacienteForm forma,
    		HttpServletRequest request,HttpServletResponse response)throws Exception{    	
    	UsuarioBasico usuarioSesion = Utilidades.getUsuarioBasicoSesion(request
				.getSession());
    	int registrosPorPagina = ValoresPorDefecto.getMaxPageItemsInt(
				usuarioSesion.getCodigoInstitucionInt());
    	String enlace="excepcionesNaturalezaPaciente.jsp";
    	
    	forma.setOffset(((int)((forma.getListaExcepcionesNaturalezaPaciente().size()-1)/registrosPorPagina))*registrosPorPagina);
        if(request.getParameter("ultimaPage")==null){        	
        	       	
        	if(forma.getListaExcepcionesNaturalezaPaciente().size() > (forma.getOffset()+registrosPorPagina))
               forma.setOffset(((int)(forma.getListaExcepcionesNaturalezaPaciente().size()/registrosPorPagina))*registrosPorPagina);
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
            if(forma.getListaExcepcionesNaturalezaPaciente().size()>(forma.getOffset()+registrosPorPagina)){
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
}
