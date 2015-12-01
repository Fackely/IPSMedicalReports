package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.Listado;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.RegistroEventosAdversosForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.RegistroEventosAdversos;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;


/**
 * Clase para el manejo del workflow de RegistroEventosAdversosAction
 * Date: 2008-05-09
 * @author lgchavez@princetonsa.com
 */
public class RegistroEventosAdversosAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(RegistroEventosAdversosAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try {
    		if(response==null);
    		if(form instanceof RegistroEventosAdversosForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			RegistroEventosAdversosForm forma = (RegistroEventosAdversosForm)form;

    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO REGISTRO EVENTOS ADVERSOS---->"+estado+"\n\n");
    			ActionErrors errores = new ActionErrors();
    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			forma.setMensaje(new ResultadoBoolean(false));



    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
    				//Validaciones

    				logger.info("********VALIDACION PACIENTE EN VALORACIONES*******************");
    				//Se realiza la validacion del paciente
    				RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);

    				logger.info("respuesta validacion : "+resp.puedoSeguir+", "+resp.textoRespuesta);
    				logger.info("codigo ingreso=> "+ paciente.getCodigoIngreso());
    				logger.info("********FIN VALIDACION PACIENTE EN VALORACIONES*******************");
    				//Se valida si el paciente tiene cuenta abierta
    				if(!resp.puedoSeguir)
    				{
    					//Se verifica si el paciente tiene cuentas abiertas en otros centros de atencion
    					//consultan el nombre del centro de atencion de alguna cuenta activa que tenga el paciente
    					String nomCentroAtencion = Utilidades.getNomCentroAtencionIngresoAbierto(con,paciente.getCodigoPersona()+"");
    					if(nomCentroAtencion.equals(""))
    						errores.add("error validacion paciente",new ActionMessage(resp.textoRespuesta));
    					else
    						errores.add("Paciente con ingreso abierto en otro centro de atencion",new ActionMessage("errores.paciente.ingresoAbiertoCentroAtencion",nomCentroAtencion));

    					saveErrors(request,errores);
    					UtilidadBD.cerrarConexion(con);
    					return mapping.findForward("error");
    				}
    				else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
    				{
    					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "no ingreso abierto", "errors.paciente.noIngreso", true);
    				}
    				///Validación de autoatencion
    				ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
    				if(respuesta.isTrue())
    					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);



    				if(!errores.isEmpty())
    				{
    					saveErrors(request,errores);	
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("error");	
    				}
    				else{
    					return this.accionEmpezar(forma, con, mapping, usuario, paciente);
    				}



    			}
    			else if (estado.equals("redireccion"))
    			{
    				UtilidadBD.closeConnection(con);
    				response.sendRedirect(forma.getLinkSiguiente());
    				return null;
    			}
    			else
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("empezar");
    				}
    				else
    					if(estado.equals("mostrarNuevo"))
    					{
    						return this.accionMostrarNuevo(forma, con, mapping, usuario, paciente);
    					}
    					else
    						if(estado.equals("cargarEventos"))
    						{
    							return this.accionCargarEventos(forma, con, mapping, usuario, paciente);
    						}
    						else
    							if(estado.equals("guardarNuevo"))
    							{
    								return this.accionGuardar(request, forma, con, mapping, usuario, paciente);
    							}    
    							else
    								if(estado.equals("cargarDetalle"))
    								{
    									return this.accionCargarDetalle(forma, con, mapping, usuario, paciente);
    								}
    								else
    									if(estado.equals("modificar"))
    									{
    										return this.accionModificar(request, forma, con, mapping, usuario, paciente);
    									}
    		}
    		return null;
    	} catch (Exception e) {
    		Log4JManager.error(e);
    		return null;
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
     }
    
    
	/**
     * 
     * @param request 
	 * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionModificar(HttpServletRequest request, RegistroEventosAdversosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
		
    	forma.setFiltro("codigomod", forma.getCodigo());
    	forma.setFiltro("usuario", usuario.getLoginUsuario());
    	RegistroEventosAdversos rea=new RegistroEventosAdversos();
		
    	if (forma.getFiltro().containsKey("observaciones") && !forma.getFiltro().get("observaciones").toString().trim().equals(""))
    	{
    	String observaciones="\n" +
		""+forma.getFiltro().get("observaciones")+"\n";
		forma.setFiltro("observaciones", observaciones);
    	}
    	
		int y=rea.modificar(con,forma.getFiltro());
    	forma.setCapa(0);
    	
    	ActionErrors errores = new ActionErrors();
		
		if (y>0)
		{
			forma.setMensaje(new ResultadoBoolean(true,"Modificación Realizada con Exito"));
		}
		else
		{
			errores.add("", new ActionMessage("errors.notEspecific", "ERROR EN LA MODIFICACION "));
		}
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return this.accionEmpezar(forma, con, mapping, usuario, paciente);
		}
	
    	
    	
    	return this.accionEmpezar(forma, con, mapping, usuario, paciente);
    }
    
    
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionCargarDetalle(RegistroEventosAdversosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente)
    {
		
		forma.setCapa(2);
		RegistroEventosAdversos rea=new RegistroEventosAdversos();
		rea.setCodigorea(forma.getCodigo()+"");
		forma.setFiltro(rea.consultar(con, rea));
		logger.info("\n\n\n\n registro del evento seleccionado >>>>"+forma.getFiltro());
		
		forma.setFiltro("fechar_0", UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltro().get("fechahorar_0").toString().split(" / ")[0]));
		forma.setFiltro("horar_0", forma.getFiltro().get("fechahorar_0").toString().split(" / ")[1]);
		
		//Concatenar todas las N Observaciones del registro catastrofico con el fin de mostrarlas todas en un solo textArea
		//Sino hay observaciones se muestra el campo en la vista vacio y no generara error
		String observacionesGenerales = "";
		
		UsuarioBasico usu=new UsuarioBasico();
		for(int i=0; i<Utilidades.convertirAEntero(forma.getFiltro("numRegistros")+""); i++)
		{
			
			if (!(forma.getFiltro("login_"+i)+"").equals(""))
			{
			try {
				usu.cargarUsuarioBasico(forma.getFiltro("login_"+i)+"");
			} catch (SQLException e) {
				logger.info("[EventosAdversos] -> Error Cargando usuario"+forma.getFiltro("login_"+i)+"");
				e.printStackTrace();
			}
			observacionesGenerales += forma.getFiltro("observaciones_"+i)+usu.getInformacionGeneralPersonalSalud()+"\n\n";
			}
			
		}
		forma.setFiltro("observacionesGenerales", observacionesGenerales);
		logger.info("\n====>Observaciones Generales: "+observacionesGenerales);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");
    	
	}

    
    /**
     * 
     * @param request 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionGuardar(HttpServletRequest request, RegistroEventosAdversosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
		forma.setFiltro("ingreso",paciente.getCodigoIngreso());
		forma.setFiltro("usuario",usuario.getLoginUsuario() );
		forma.setFiltro("institucion", usuario.getCodigoInstitucion());
		forma.setFiltro("paciente", paciente.getCodigoPersona());
		forma.setFiltro("centroCosto", paciente.getCodigoArea());
		
		
		if (forma.getFiltro().containsKey("observaciones") && !forma.getFiltro().get("observaciones").toString().trim().equals(""))
		{
		String observaciones="\n" +
							""+forma.getFiltro().get("observaciones")+"\n" ;
		forma.setFiltro("observaciones", observaciones);
		}
		
		RegistroEventosAdversos eca=new RegistroEventosAdversos();
    	int y=eca.guardarEvento(con,forma.getFiltro());
		ActionErrors errores = new ActionErrors();
		
		if (y>0)
		{
			forma.setMensaje(new ResultadoBoolean(true,"Inserción Realizada con Exito"));
		}
		else
		{
			errores.add("", new ActionMessage("errors.notEspecific", "ERROR EN LA INSERCION"));
		}
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return this.accionEmpezar(forma, con, mapping, usuario, paciente);
		}
		return this.accionEmpezar(forma, con, mapping, usuario, paciente);
    	}






	private ActionForward accionCargarEventos(RegistroEventosAdversosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
    	
		for (int i=0;i<Integer.parseInt(forma.getEventos().get("numRegistros").toString());i++)
		{
			if (forma.getEventos().get("tipo_"+i).toString().equals(""+forma.getFiltro().get("tipoEvento").toString()))
			{
				forma.getEventos().put("es_"+i,1);
			}
			else
			{
				forma.getEventos().put("es_"+i, 0);
			}
		}
		
		return mapping.findForward("empezar");
	}



	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
	private ActionForward accionMostrarNuevo(RegistroEventosAdversosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
		forma.reset1();
		
		EventosAdversos mundo = new EventosAdversos();
		mundo.setInstitucion(usuario.getCodigoInstitucionInt()+"");
		forma.setEventos(mundo.consultar(con, mundo));
		forma.setCapa(1);
		
		for (int i=0;i<Integer.parseInt(forma.getEventos().get("numRegistros").toString());i++)
		{
			if (forma.getEventos().get("tipo_"+i).toString().equals(""+forma.getFiltro().get("tipoEvento"))){
				forma.getEventos().put("es_"+i,1);
			}
			else
			{
				forma.getEventos().put("es_"+i, 0);
			}
		}
		
		forma.setFiltro("fechar", UtilidadFecha.getFechaActual());
		forma.setFiltro("horar", UtilidadFecha.getHoraActual());
		
		return mapping.findForward("empezar");
	}

    
    /**
	 * metodo encargado del ordenamiento del mapa ingresos
	 * @param forma
	 */

	public static void accionOrdenarMapa(RegistroEventosAdversosForm forma)
	{
		String [] indicesMap = {	"codigoevento_",
				"nombreevento_",
				"fechahorar_",
				"tipoevento_",
				"gestionado_" ,
				"codigo_",
				};

		
		int numReg = Integer.parseInt(forma.getIngresosMap().get("numRegistros").toString());
		forma.setIngresosMap(Listado.ordenarMapa(indicesMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.getIngresosMap().put("numRegistros",numReg);
		forma.getIngresosMap().put("INDICES_MAPA",indicesMap);
	}
    
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
	 * @param paciente 
     * @return
     */
	private ActionForward accionEmpezar(RegistroEventosAdversosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		forma.reset();
		forma.setFechaingreso(paciente.getFechaIngreso());
		RegistroEventosAdversos rea=new RegistroEventosAdversos();
		rea.setIngreso(paciente.getCodigoIngreso()+"");
		forma.setIngresosMap(rea.consultar(con, rea));
		logger.info("\n\n\n\n registros de eventos mapa >>>>"+forma.getIngresosMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");
	}	


}
