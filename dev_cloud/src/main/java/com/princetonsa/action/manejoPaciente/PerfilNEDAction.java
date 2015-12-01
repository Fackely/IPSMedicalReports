package com.princetonsa.action.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.PerfilNEDForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.manejoPaciente.DtoCamposPerfilNed;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.manejoPaciente.PerfilNED;

/**
 * 
 * @author axioma
 *
 */
public class PerfilNEDAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(PisosAction.class);
		
	/**
	 * Metodo execute del Action
	 */
	public ActionForward execute(   ActionMapping mapping,
	        						ActionForm form,
	        						HttpServletRequest request,
	        						HttpServletResponse response ) throws Exception
	{
	        					    
		if (response==null);
	    if(form instanceof PerfilNEDForm)
	    {
	        UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
	        PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
	        PerfilNEDForm forma =(PerfilNEDForm)form;
	        logger.warn("\n\n\nEl estado en PERFILNED es------->"+forma.getEstado()+"\n");
	        
	        ActionForward validaciones= validarPaciente(mapping, request, paciente);
	        if(validaciones!=null)
	        	return validaciones;
	        
	        if(forma.getEstado() == null)
			{
				forma.reset(); 
				logger.error("Estado no valido dentro del flujo de PERFIL NED (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
	        else if(forma.getEstado().equals("empezar") || forma.getEstado().equals("resumen"))
	        {
	        	return accionEmpezar(mapping, forma, paciente, usuario, request);
	        }
	        else if(forma.getEstado().equals("empezarConsulta"))
	        {
	        	return accionEmpezarConsulta(mapping, forma, paciente, usuario, request);
	        }
	        else if(forma.getEstado().equals("continuar"))
	        {
	        	return mapping.findForward("paginaPrincipal");
	        }	
	        else if(forma.getEstado().equals("guardar"))
	        {
	        	return accionGuardar(mapping, forma, paciente, usuario, request);
	        }
	        else
	        {
	        	forma.reset(); 
				logger.error("Estado no valido dentro del flujo de PERFIL NED ("+forma.getEstado()+") ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
	        }
	    }
		return null;  
	}

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward validarPaciente(	ActionMapping mapping,
											HttpServletRequest request, 
											PersonaBasica paciente) 
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		return null;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(	ActionMapping mapping,
											PerfilNEDForm forma,
											PersonaBasica paciente,
											UsuarioBasico usuario,
											HttpServletRequest request) 
	{
		forma.reset();
		
		BigDecimal codigoEscala;
		forma.setPerfilNed(PerfilNED.cargarPerfilNEDXPaciente(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));
		
		if(forma.getPerfilNed().getCodigoPk()>0)
		{
			//si existe debemos cargar el codigo de la escala perteneciente al paciente
			codigoEscala= new BigDecimal(forma.getPerfilNed().getEscala());
			forma.setEscala(Plantillas.cargarEscalaPerfilNed(codigoEscala, forma.getPerfilNed().getCodigoPk()));
		}
		else
		{
			if( Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt()))<=0)
			{
				ActionErrors errores = new ActionErrors(); 
	        	errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "Escala Perfil Paciente NED"));
	            saveErrors(request, errores);
	            return mapping.findForward("paginaErroresActionErrors");
			}
			//de lo contrario seteamos el del valor x defecto
			codigoEscala= new BigDecimal(Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt()))); 
			forma.setEscala(Plantillas.cargarEscalaPerfilNed(codigoEscala, ConstantesBD.codigoNuncaValidoDoubleNegativo));
		}
		
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarConsulta(ActionMapping mapping,
												PerfilNEDForm forma, 
												PersonaBasica paciente, 
												UsuarioBasico usuario,
												HttpServletRequest request) 
	{
		forma.reset();
		BigDecimal codigoEscala;
		forma.setPerfilNed(PerfilNED.cargarPerfilNEDXPaciente(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));
		
		logger.info("valor del valor total del perfil ned: "+forma.getPerfilNed().getValorTotal());
		logger.info("valor de la escala factor de prediccion: "+forma.getPerfilNed().getEscalaFactorPrediccion());
		
		if(forma.getPerfilNed().getCodigoPk()>0)
		{
			//si existe debemos cargar el codigo de la escala perteneciente al paciente
			codigoEscala= new BigDecimal(forma.getPerfilNed().getEscala());
			forma.setEscala(Plantillas.cargarEscalaPerfilNed(codigoEscala, forma.getPerfilNed().getCodigoPk()));
			
			logger.info("total de la escala: "+forma.getEscala().getTotalEscala());
			logger.info("factor de prediccion de la escala: "+forma.getEscala().getFactorPrediccion().getNombre());
			
			forma.getEscala().setTotalEscala(forma.getPerfilNed().getValorTotal());
			forma.getEscala().setNombreFactorPrediccion(PerfilNED.cargarTotalesFactoresPrediccion(forma.getPerfilNed().getCodigoPk()).getNombre());
		}
		else
		{
			ActionErrors errores = new ActionErrors(); 
        	errores.add("", new ActionMessage("errors.notEspecific", "Paciente NO tiene perfil Ned asociado, por favor revisar"));
            saveErrors(request, errores);
            return mapping.findForward("paginaErroresActionErrors");
		}
		
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,
										PerfilNEDForm forma,
										PersonaBasica paciente,
										UsuarioBasico usuario,
										HttpServletRequest request) 
	{
		//en este punto debemos setear los valores que tenemos cargados en la escala al dtoPerfilNED
		DtoPerfilNed dto= new DtoPerfilNed();
		llenarEncabezado(forma, paciente, usuario, dto);
		ArrayList<DtoCamposPerfilNed> arrayCampos= new ArrayList<DtoCamposPerfilNed>();
		llenarDetalle(forma, usuario, arrayCampos);
		
		if(forma.getPerfilNed().getCodigoPk()<=0)
		{	
			ActionForward forwardError= accionGuardarNuevo(dto, arrayCampos, mapping, request);
			if(forwardError!=null)
				return forwardError; 
		}
		else
		{
			ActionForward forwardError= accionModificar(forma, dto, arrayCampos, mapping, request);
			if(forwardError!=null)
				return forwardError;
		}
		
		forma.setEstado("resumen");
		return accionEmpezar(mapping, forma, paciente, usuario, request);
	}
	
	/**
	 * 
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param dto
	 */
	private void llenarEncabezado(PerfilNEDForm forma, PersonaBasica paciente,
								UsuarioBasico usuario, DtoPerfilNed dto) 
	{
		dto.setCodigoPaciente(paciente.getCodigoPersona());
		if(forma.getPerfilNed().getCodigoPk()>0)
		{
			dto.setCodigoPk(forma.getPerfilNed().getCodigoPk());
		}
		else
		{
			dto.setDatosfechaUsuarioRegistro(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		}
		
		dto.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dto.setEscala(Utilidades.convertirADouble(forma.getEscala().getCodigoPK()));
		dto.setEscalaFactorPrediccion(Utilidades.convertirADouble(forma.getEscala().getCodigoFactorPrediccion()));
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		dto.setValorTotal(forma.getEscala().getTotalEscala());
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param arrayCampos
	 */
	private void llenarDetalle(PerfilNEDForm forma, UsuarioBasico usuario,
			ArrayList<DtoCamposPerfilNed> arrayCampos) 
	{
		for(DtoSeccionParametrizable seccion:forma.getEscala().getSecciones())
		{
			if(seccion.isVisible())
			{
				//logger.info("numero campos de la seccion "+seccion.getDescripcion()+">> "+seccion.getCampos().size());
				for(DtoCampoParametrizable campo:seccion.getCampos())
				{	
					if(campo.isMostrar())
					{
						DtoCamposPerfilNed dtoArray= new DtoCamposPerfilNed();
						dtoArray.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
						dtoArray.setEscalaCampoSeccion(Utilidades.convertirADouble(campo.getConsecutivoParametrizacion()));
						
						logger.info("\n\n\n campo.getCodigoPK()--->"+campo.getConsecutivoParametrizacion()+"\n\n");
						
						dtoArray.setObservaciones(campo.getObservaciones());
						if(UtilidadTexto.isEmpty(campo.getValor()))
						{	
							dtoArray.setValor(null);
						}
						else
						{
							dtoArray.setValor(new BigDecimal( Utilidades.convertirADouble(campo.getValor())));
						}
						arrayCampos.add(dtoArray);
					}
				}	
			}
		}
	}

	
	/**
	 * 
	 * @param dto
	 * @param arrayCampos
	 * @return
	 */
	private ActionForward accionGuardarNuevo(DtoPerfilNed dto,
											ArrayList<DtoCamposPerfilNed> arrayCampos,
											ActionMapping mapping,
											HttpServletRequest request) 
	{
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		double codigoPerfilNED= PerfilNED.guardar(con,dto);
		
		if(codigoPerfilNED<=0)
		{
			logger.error("NO INSERTA ENCABEZADO");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			////hacer error
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
		}
		
		for(DtoCamposPerfilNed dtoCampo: arrayCampos)
		{
			dtoCampo.setCodigoPerfilNed(codigoPerfilNED);
			if(PerfilNED.guardarCampo(con, dtoCampo)<=0)
			{
				logger.error("NO INSERTA CAMPO ");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				////hacer error
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}
		}	
		
		logger.info("inserta 100%%%");
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return null;
	}
	
	/**
	 * 
	 * @param dto
	 * @param arrayCampos
	 * @return
	 */
	private ActionForward accionModificar(	PerfilNEDForm forma,
											DtoPerfilNed dto,
											ArrayList<DtoCamposPerfilNed> arrayCampos,
											ActionMapping mapping,
											HttpServletRequest request) 
	{
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		DtoPerfilNed dtoWhere = new DtoPerfilNed();
		dtoWhere.setCodigoPk(dto.getCodigoPk());
		
		if(!PerfilNED.modificar(con, dto, dtoWhere))
		{
			logger.error("NO MODIFICA ENCABEZADO");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			////hacer error
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
		}
		
		for(DtoCamposPerfilNed dtoCampo: arrayCampos)
		{
			DtoCamposPerfilNed dtoWhereDetalle= new DtoCamposPerfilNed();
			dtoWhereDetalle.setCodigoPerfilNed(forma.getPerfilNed().getCodigoPk());
			dtoWhereDetalle.setEscalaCampoSeccion(dtoCampo.getEscalaCampoSeccion());
			
			if(!PerfilNED.modificarCampos(con, dtoCampo, dtoWhereDetalle))
			{
				logger.error("NO INSERTA CAMPO ");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				////hacer  errors.problemasBd error
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
			}
		}	
		
		logger.info("modifica 100%%%");
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return null;
	}
}
