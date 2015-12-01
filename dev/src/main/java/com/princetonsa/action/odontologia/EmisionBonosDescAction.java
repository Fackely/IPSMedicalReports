package com.princetonsa.action.odontologia;






import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoPacienteBonoPresupuesto;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.odontologia.EmisionBonosDescForm;
import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.EmisionBonosDesc;
import com.princetonsa.sort.odontologia.SortEmisionBonosDesc;
import com.princetonsa.sort.odontologia.SortGenerico;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public class EmisionBonosDescAction extends Action  {
	private Logger logger = Logger.getLogger(EmisionBonosDescAction.class);
     

	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
		 	{
		
				MessageResources mensaje=getResources(request);
				mensaje.getMessage("button.save");
				if(form instanceof EmisionBonosDescForm)
				{
					ActionErrors errores = new  ActionErrors();
				EmisionBonosDescForm forma = (EmisionBonosDescForm)form;	
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				

				/**
				 * UTILIZA PROGRAMAS O SERVICIOS
				 */
				forma.setUtilizaProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));

				logger.info("ESTADO--------------------------------------->"+forma.getEstado());
		

				if(forma.getEstado().equals("volver"))
			    	{
			    		return accionVolver(mapping, forma, usuario);
			    	}
		
				if(forma.getEstado().equals("empezar"))
		    	{
		    		return accionVolver(mapping, forma, usuario);
		    	}
			
				if(forma.getEstado().equals("empezarConsulta"))
				{
					return accionVolver(mapping, forma, usuario);
				}
	
				
				if(forma.getEstado().equals("cargar"))
				{
					return accionCargar(mapping, forma, usuario);
				}
				
				if(forma.getEstado().equals("nuevo"))
				{
					return mapping.findForward("paginaPrincipal");
				}
				
				if(forma.getEstado().equals("guardar"))
				{
					return accionGuardar(mapping, forma, usuario);
				}
				
				if(forma.getEstado().equals("modificar"))
				{
					forma.setDtoEmisionBonosDesc((DtoEmisionBonosDesc)forma.getListaEmisionBonosDesc().get(forma.getPosArray()).clone());
					
					if( EmisionBonosDesc.existeSerialSubCuentas(forma.getDtoEmisionBonosDesc().getSerialInicial(), forma.getDtoEmisionBonosDesc().getSerialFinal(), forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().getCodigo()))
					{
						errores.add("", new ActionMessage("errors.notEspecific", " No Se Permite La Modificación, El Bono Esta Asociado A Un Paciente"));
						saveErrors(request, errores);
					}
					return mapping.findForward("paginaPrincipal");
				}
				
				if(forma.getEstado().equals("guardarModificar"))
				{
					return accionGuardarModificar(mapping, forma, usuario);
				}
				if(forma.getEstado().equals("eliminar"))
				{
					return accionEliminar(mapping, forma, usuario);
				}
				if(forma.getEstado().equals("ordenar"))
		        {    
		            	return accionOrdenar(mapping,  forma);
		        }
				
				if(forma.getEstado().equals("empezarConsulta"))
				{
		          	return mapping.findForward("paginaPrincipalConsultar");
		       }
				if(forma.getEstado().equals("consultaDetalle"))
				{
					return accionConsultaAvanzada(forma, usuario, mapping);
				}
				if(forma.getEstado().equals("busquedaAvanzada"))
				{
					return accionBusquedaAvanzada(forma, usuario, mapping, errores, request);
				}
				if(forma.getEstado().equals("ordenarPacientes"))
				{
					return accionOrdenarListaPaciente(mapping, forma, usuario);
				}
				if(forma.getEstado().equals("mostrarErrores") || forma.getEstado().equals("mostrarErroresModificar"))
	                      	return mapping.findForward("paginaPrincipal");
			
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
	private ActionForward accionOrdenarListaPaciente(ActionMapping mapping,	EmisionBonosDescForm forma, UsuarioBasico usuario) {
		
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaPaciente() ,sortG);
		
		return mapping.findForward("detEmision");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBusquedaAvanzada(EmisionBonosDescForm forma, UsuarioBasico usuario, ActionMapping mapping, ActionErrors errores, HttpServletRequest request) {
		
		
		
	int contador=0;
		
		if(forma.getDtoInfoPaciente().getBono().doubleValue()<=0)
		{
			contador++;
		}
		if(UtilidadTexto.isEmpty(forma.getDtoInfoPaciente().getPrimerApellido() ))
		{
			contador++;
		}
		if(UtilidadTexto.isEmpty(forma.getDtoInfoPaciente().getSegundoApellido() ))
		{
			contador++;
		}
		if(UtilidadTexto.isEmpty(forma.getDtoInfoPaciente().getPrimerNombre()))
		{
			contador++;
		}
		if(UtilidadTexto.isEmpty(forma.getDtoInfoPaciente().getSegundoApellido()))
		{
			contador++;
		}
		
		if(contador==5)
		{
			errores.add("", new ActionMessage("errors.notEspecific", "Deberá ingresar al menos un criterio para que se inicie el proceso de Búqueda avanzada"));
			saveErrors(request, errores);
			forma.setEstado("errorBusqueda");
			return mapping.findForward("detEmision");
		}
		
		/**
		 * CRITERIOS DE BUSQUEDA 
		 */
		forma.getDtoInfoPaciente().setCodigoConvenio(forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().getCodigo());
		forma.getDtoInfoPaciente().setCodigoPrograma(forma.getDtoEmisionBonosDesc().getPrograma().getCodigo());
		forma.getDtoInfoPaciente().setCodigoServcio(forma.getDtoEmisionBonosDesc().getServicio().getCodigo());
		forma.getDtoInfoPaciente().setCodigoPkBono(forma.getDtoEmisionBonosDesc().getCodigo());
		
		if( forma.getUtilizaProgramas().equals(ConstantesBD.acronimoSi))
		{
			forma.setListaPaciente(EmisionBonosDesc.consultaEmisionBonos(forma.getDtoInfoPaciente(), Boolean.TRUE ));
		}
		else
		{
			forma.setListaPaciente(EmisionBonosDesc.consultaEmisionBonos(forma.getDtoInfoPaciente(), Boolean.FALSE)) ;
		}
		
		return mapping.findForward("detEmision");
	}


	


	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultaAvanzada(EmisionBonosDescForm forma, UsuarioBasico usuario,ActionMapping mapping) 
	{
		
		logger.info("1-------------");
		
		forma.resetBusqueda();	
		logger.info("2-------------"+forma.getDtoInfoPaciente().getBono());
		
		
		forma.setDtoEmisionBonosDesc((DtoEmisionBonosDesc)forma.getListaEmisionBonosDesc().get(forma.getPosArray()).clone());
	
		InfoPacienteBonoPresupuesto dtoConsulta= new InfoPacienteBonoPresupuesto();
		dtoConsulta.setCodigoConvenio(forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().getCodigo());
		dtoConsulta.setCodigoPrograma(forma.getDtoEmisionBonosDesc().getPrograma().getCodigo());
		dtoConsulta.setCodigoServcio(forma.getDtoEmisionBonosDesc().getServicio().getCodigo());
		dtoConsulta.setCodigoPkBono(forma.getDtoEmisionBonosDesc().getCodigo());
		
		if( forma.getUtilizaProgramas().equals(ConstantesBD.acronimoSi))
		{
			forma.setListaPaciente(EmisionBonosDesc.consultaEmisionBonos(dtoConsulta, Boolean.TRUE) );
		}
		else
		{
			forma.setListaPaciente(EmisionBonosDesc.consultaEmisionBonos(dtoConsulta,Boolean.FALSE ));
		}
		logger.info("3-------------"+forma.getDtoInfoPaciente().getBono());
		return mapping.findForward("detEmision");
	}

/**
 * 
 * @param mapping
 * @param forma
 * @param usuario
 * @return
 */
	private ActionForward accionEliminar(ActionMapping mapping,
			EmisionBonosDescForm forma, UsuarioBasico usuario) {
		forma.getDtoEmisionBonosDesc().setCodigo(forma.getListaEmisionBonosDesc().get(forma.getPosArray()).getCodigo());
		
			
		
			EmisionBonosDesc.eliminar(forma.getDtoEmisionBonosDesc());
			DtoEmisionBonosDesc dtoWhere =  new DtoEmisionBonosDesc();
			
			
			dtoWhere =forma.getListaEmisionBonosDesc().get(forma.getPosArray());
			UtilidadLog.generarLog(usuario, dtoWhere, null, ConstantesBD.tipoRegistroLogEliminacion , ConstantesBD.logEmisionBonosDescuentosOdontologicos);
			forma.reset();
			forma.setEstado("resumen");
			forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().setCodigo(forma.getCodigoConvenio());
			forma.setListaEmisionBonosDesc(EmisionBonosDesc.cargar(forma.getDtoEmisionBonosDesc()));
		
		
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
			EmisionBonosDescForm forma, UsuarioBasico usuario) {
		forma.getDtoEmisionBonosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoEmisionBonosDesc().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoEmisionBonosDesc().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoEmisionBonosDesc().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoEmisionBonosDesc().setCodigo(forma.getListaEmisionBonosDesc().get(forma.getPosArray()).getCodigo());	
		EmisionBonosDesc.modificar(forma.getDtoEmisionBonosDesc());
		DtoEmisionBonosDesc dtoWhere =  new DtoEmisionBonosDesc();
		dtoWhere =forma.getListaEmisionBonosDesc().get(forma.getPosArray());
		DtoEmisionBonosDesc dtoNuevo =  new DtoEmisionBonosDesc();
		dtoNuevo =forma.getDtoEmisionBonosDesc();
		
		UtilidadLog.generarLog(usuario, dtoWhere, dtoNuevo, ConstantesBD.tipoRegistroLogModificacion , ConstantesBD.logEmisionBonosDescuentosOdontologicos);
		
		forma.reset();
		forma.setEstado("resumen");
		forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().setCodigo(forma.getCodigoConvenio());
		forma.setListaEmisionBonosDesc(EmisionBonosDesc.cargar(forma.getDtoEmisionBonosDesc()));
		return mapping.findForward("paginaPrincipal");
	}

/**
 * 
 * @param mapping
 * @param forma
 * @param usuario
 * @return
 */
	private ActionForward accionGuardar(ActionMapping mapping,
			EmisionBonosDescForm forma, UsuarioBasico usuario) {
		logger.info("Guardar ---------------------------------------------------");
		logger.info("estado---"+forma.getEstado());
		forma.getDtoEmisionBonosDesc().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoEmisionBonosDesc().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoEmisionBonosDesc().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoEmisionBonosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		logger.info("Codigo en la Action:---------------->"+forma.getDtoEmisionBonosDesc().getPrograma().getCodigo());
		EmisionBonosDesc.guardar(forma.getDtoEmisionBonosDesc());
		forma.reset();
		forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().setCodigo(forma.getCodigoConvenio());
		forma.setListaEmisionBonosDesc(EmisionBonosDesc.cargar(forma.getDtoEmisionBonosDesc()));
		
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
	private ActionForward accionCargar(ActionMapping mapping,
			EmisionBonosDescForm forma, UsuarioBasico usuario) {
		logger.info("estado---"+forma.getEstado());
		forma.reset();
		forma.setEstado("cargar");
		forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().setCodigo(forma.getCodigoConvenio());
		forma.getDtoEmisionBonosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().setNombre(Utilidades.obtenerNombreConvenioOriginal(forma.getCodigoConvenio()));
	  	forma.setListaEmisionBonosDesc(EmisionBonosDesc.cargar(forma.getDtoEmisionBonosDesc()));
	  	if(!UtilidadTexto.isEmpty(forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().getNombre()))
	  	{
		forma.setTmpNombreConvenio(forma.getDtoEmisionBonosDesc().getConvenioPatrocinador().getNombre());
	  	}
		return mapping.findForward("paginaPrincipal");
	}

/**
 * 
 * @param mapping
 * @param forma
 * @param usuario
 * @return
 */
	private ActionForward accionVolver(ActionMapping mapping,
			EmisionBonosDescForm forma, UsuarioBasico usuario) {
		forma.reset();
		forma.setTmpNombreConvenio("");
		forma.setEstado("empezar");
		forma.setListConvenios(Utilidades.obtenerConvenios("", ""/*tipoContrato*/, true/*verificarVencimientoContrato*/, ""/*fechaReferencia*/, true/*activo*/,ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico, ConstantesBD.acronimoSi, ""/*Maneja Promociones*/));
		forma.getDtoEmisionBonosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping, EmisionBonosDescForm forma) 
	{
		Collections.sort(forma.getListaEmisionBonosDesc(), new SortEmisionBonosDesc (forma.getPatronOrdenar()));
		forma.setEstado("cargar");
		return mapping.findForward("paginaPrincipal");
	}

}
