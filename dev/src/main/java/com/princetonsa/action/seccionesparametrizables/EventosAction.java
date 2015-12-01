package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
import java.util.HashMap;

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

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.seccionesparametrizables.EventosForm;
import com.princetonsa.dto.salas.DtoEventos;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.Eventos;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class EventosAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(EventosAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try {
    		if (response==null); 
    		if(form instanceof EventosForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			EventosForm forma =(EventosForm)form;
    			boolean validacionCapitacion=false;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en EVENTOS es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de EVENTOS (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarEventos") || estado.equals("resumen"))
    			{
    				return this.accionEmpezarEventos(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardarEventos(forma, mapping, con, usuario, request,validacionCapitacion);
    			}
    			else if(estado.equals("nuevoEventoNReg"))
    			{
    				return this.accionNuevoEventoNReg(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("modificarEventoNReg"))
    			{
    				return this.accionModificarEventoNReg(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("eliminarEventoNReg"))
    			{
    				return this.accionEliminarEventoNReg(forma, mapping, con, usuario);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de EVENTOS");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
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
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezarEventos(EventosForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		forma.setMapa(Eventos.cargarEventoHojaAnestesia(con, forma.getNumeroSolicitud(), forma.getCodigoEventoInstCC(), forma.getCodigoEvento()));
		forma.setDtoEvento((DtoEventos)forma.getMapa("DTOEVENTO"));
		
		if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaOtros)
		{
			if(Integer.parseInt(forma.getMapa("numRegistros").toString()) <= 0)
				forma.getMapa().put("poseeInformacion",ConstantesBD.acronimoNo);
			else
				forma.getMapa().put("poseeInformacion",ConstantesBD.acronimoSi);			
			
			postularFechasHorasActuales(forma);
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("otrosEventos");
		}
		else if(!forma.getDtoEvento().getNRegistros())
		{
			llenarForm1Reg(forma, con, usuario);
			
			if(UtilidadTexto.isEmpty(forma.getFechaInicialActualizada()))
				forma.getMapa().put("poseeInformacion",ConstantesBD.acronimoNo);
			else
				forma.getMapa().put("poseeInformacion",ConstantesBD.acronimoSi);			
			
			if(forma.getDtoEvento().getCodigo()!=ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx)
				postularFechasHorasActuales(forma);
			
			ActionForward forward=validacionesIngresoSala(forma, request, mapping, con);
			if(forward!=null)
				return forward;
			UtilidadBD.closeConnection(con);
			return mapping.findForward("eventos");
		}
		else 
		{			
			if(Integer.parseInt(forma.getMapa("numRegistros").toString()) <= 0)
				forma.getMapa().put("poseeInformacion",ConstantesBD.acronimoNo);
			else
				forma.getMapa().put("poseeInformacion",ConstantesBD.acronimoSi);			
			
			postularFechasHorasActuales(forma);
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("eventoNregistros");
		}
	}

	/**
	 * 
	 * @param forma
	 */
	private void postularFechasHorasActuales(EventosForm forma) 
	{
		if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaInicialActualizada()))
			forma.setFechaInicialActualizada(UtilidadFecha.getFechaActual());
		if(!UtilidadFecha.validacionHora(forma.getHoraInicialActualizada()).puedoSeguir)
			forma.setHoraInicialActualizada(UtilidadFecha.getHoraActual());
		
		if(forma.getDtoEvento().getLlevaFechaFin())
		{	
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaFinalActualizada()))
				forma.setFechaFinalActualizada(UtilidadFecha.getFechaActual());
			if(!UtilidadFecha.validacionHora(forma.getHoraFinalActualizada()).puedoSeguir)
				forma.setHoraFinalActualizada(UtilidadFecha.getHoraActual());
		}	
	}

	/**
	 * 
	 * @param forma
	 */
	private void llenarForm1Reg(EventosForm forma, Connection con, UsuarioBasico usuario) 
	{
		if( Utilidades.convertirAEntero(forma.getMapa("numRegistros").toString())==1)
		{
			forma.setFechaInicialActualizada(forma.getMapa("fechainicial_0").toString());
			forma.setFechaFinalActualizada(forma.getMapa("fechafinal_0").toString());
			forma.setHoraInicialActualizada(forma.getMapa("horainicial_0").toString());
			forma.setHoraFinalActualizada(forma.getMapa("horafinal_0").toString());
			forma.setEstaBD( UtilidadTexto.getBoolean(forma.getMapa("estabd_0").toString()));
			forma.setCodigoEventoHojaAnestesia( Integer.parseInt(forma.getMapa("codigoeventohojaanestesia_0")+""));
			forma.setGraficarActualizado(forma.getMapa("graficar_0")+"");
			
			if(forma.getCodigoEvento()==ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx)
			{
				if(UtilidadTexto.isEmpty(forma.getFechaInicialActualizada())
					&& UtilidadTexto.isEmpty(forma.getFechaFinalActualizada())
					&& UtilidadTexto.isEmpty(forma.getHoraInicialActualizada())
					&& UtilidadTexto.isEmpty(forma.getHoraFinalActualizada()))
				{	
					HojaAnestesia hojaAnes= new HojaAnestesia();
					HashMap<Object, Object> mapa= hojaAnes.consultarAnestesiologos(con, forma.getNumeroSolicitud()+"", "", "", usuario, false);
					if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")>0)
					{
						forma.setPuedoModificarFechaHoraCx(false);
					}
				}	
			}	
		}
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarEventos(EventosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request, boolean validacionCapitacion) 
	{
		ActionForward forward=validacionesIngresoSala(forma, request, mapping, con);
		if(forward!=null)
			return forward;
		
		UtilidadBD.iniciarTransaccion(con);
		HashMap<Object, Object> mapaEvento= llenarMapaInsercion(forma, usuario);
		
		logger.info("\n ESTA EN BD-->"+forma.getEstaBD()+"\n");
		
		///lo primero es verificar si existe insercion en la hoja de anestesia
		
		if(!UtilidadTexto.getBoolean(HojaQuirurgica.existeHojaAnestesia(con, forma.getNumeroSolicitud()+"")))
		{	
			HojaAnestesia mundo= new HojaAnestesia();
			mundo.crearHojaAnestesiaBasica(
					 con,
					 ConstantesBD.acronimoNo,
					 forma.getNumeroSolicitud()+"",
					 usuario.getCodigoInstitucionInt(),
					 "",
					 usuario.getCodigoPersona()+"",
					 validacionCapitacion
					 );
		}
		
		if(!forma.getEstaBD())
		{	
			if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia)
			{
				Eventos.actualizarInicioFinHojaAnestesia(con, forma.getFechaInicialActualizada(), forma.getHoraInicialActualizada(), forma.getFechaFinalActualizada(), forma.getHoraFinalActualizada(), forma.getNumeroSolicitud());
			}
			else if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx)
			{
				Eventos.actualizarInicioFinCx(con, forma.getFechaInicialActualizada(), forma.getHoraInicialActualizada(), forma.getFechaFinalActualizada(), forma.getHoraFinalActualizada(), forma.getNumeroSolicitud());
			}
			else
			{
				Eventos.insertar(con, mapaEvento);
			}
		}	
		else
		{	
			if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia)
			{
				Eventos.actualizarInicioFinHojaAnestesia(con, forma.getFechaInicialActualizada(), forma.getHoraInicialActualizada(), forma.getFechaFinalActualizada(), forma.getHoraFinalActualizada(), forma.getNumeroSolicitud());
			}
			else if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx)
			{
				Eventos.actualizarInicioFinCx(con, forma.getFechaInicialActualizada(), forma.getHoraInicialActualizada(), forma.getFechaFinalActualizada(), forma.getHoraFinalActualizada(), forma.getNumeroSolicitud());
			}
			else
			{
				Eventos.modificar(con, mapaEvento);
			}
		}
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		return this.accionEmpezarEventos(forma,null, mapping, con, usuario);
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	private HashMap<Object, Object> llenarMapaInsercion(EventosForm forma, UsuarioBasico usuario) 
	{
		HashMap<Object, Object> mapaEvento= new HashMap<Object, Object>();
		mapaEvento.put("numerosolicitud", forma.getNumeroSolicitud());
		mapaEvento.put("codigoeventoinstcc", forma.getCodigoEventoInstCC());
		mapaEvento.put("fechainicial", forma.getFechaInicialActualizada());
		mapaEvento.put("horainicial", forma.getHoraInicialActualizada());
		mapaEvento.put("fechafinal", forma.getFechaFinalActualizada());
		mapaEvento.put("horafinal", forma.getHoraFinalActualizada());
		
		if(!forma.getEstaBD())
			mapaEvento.put("consecutivootroseventos", Integer.parseInt(forma.getMapa("numRegistros")+"")+1);
		else
			mapaEvento.put("consecutivootroseventos", forma.getIndex()+1);
		mapaEvento.put("descotroseventos", forma.getNombreOtroEvento());
		
		mapaEvento.put("graficar", forma.getGraficarActualizado());
		mapaEvento.put("loginusuario", usuario.getLoginUsuario());
		mapaEvento.put("codigoeventohojaanestesia", forma.getCodigoEventoHojaAnestesia());
		
		mapaEvento.put("numRegistros", Integer.parseInt(forma.getMapa("numRegistros")+"")+1);
		
		return mapaEvento;
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoEventoNReg(EventosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.resetNuevo();
		forma.setEstaBD(false);
		UtilidadBD.closeConnection(con);
		
		postularFechasHorasActuales(forma);
		
		if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaOtros)
			return mapping.findForward("otrosEventos");
		else
			return mapping.findForward("eventoNregistros");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarEventoNReg(EventosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		llenarCamposModificar(forma, usuario);
		UtilidadBD.closeConnection(con);
		if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaOtros)
			return mapping.findForward("otrosEventos");
		else
			return mapping.findForward("eventoNregistros");
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private void llenarCamposModificar(EventosForm forma, UsuarioBasico usuario) 
	{
		forma.setFechaInicialActualizada(forma.getMapa("fechainicial_"+forma.getIndex())+"");
		forma.setHoraInicialActualizada(forma.getMapa("horainicial_"+forma.getIndex())+"");
		forma.setFechaFinalActualizada(forma.getMapa("fechafinal_"+forma.getIndex())+"");
		forma.setHoraFinalActualizada(forma.getMapa("horafinal_"+forma.getIndex())+"");
		forma.setGraficarActualizado(forma.getMapa("graficar_"+forma.getIndex())+"");
		forma.setCodigoEventoHojaAnestesia(Integer.parseInt( forma.getMapa("codigoeventohojaanestesia_"+forma.getIndex())+""));
		forma.setNombreOtroEvento(forma.getMapa("descotroseventos_"+forma.getIndex())+"");
		forma.setEstaBD(true);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarEventoNReg(EventosForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		if(forma.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaOtros)
			Eventos.actualizarConsecutivoOtrosEventos(con, Integer.parseInt(forma.getMapa("codigoeventohojaanestesia_"+forma.getIndex())+""));
		Eventos.eliminar(con, Integer.parseInt(forma.getMapa("codigoeventohojaanestesia_"+forma.getIndex())+""));
		forma.setEstado("resumen");
		return this.accionEmpezarEventos(forma,null, mapping, con, usuario);
	}
	
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param EventosForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(EventosForm forma,HttpServletRequest request)
	{
		if (!(request.getParameter("esSinEncabezado")+"").equals("") && !(request.getParameter("esSinEncabezado")+"").equals("null"))
			forma.setEsSinEncabezado(request.getParameter("esSinEncabezado")+"");
		else
			forma.setEsSinEncabezado(ConstantesBD.acronimoNo);		
		
		if (!(request.getParameter("mostrarDatosInfoActivo")+"").equals("") && !(request.getParameter("mostrarDatosInfoActivo")+"").equals("null"))
			forma.setMostrarDatosInfoActivo(UtilidadTexto.getBoolean(request.getParameter("mostrarDatosInfoActivo")+""));
		else
			forma.setMostrarDatosInfoActivo(false);
		
		if (!(request.getParameter("ocultarMenu")+"").equals("") && !(request.getParameter("ocultarMenu")+"").equals("null"))
			forma.setOcultarMenu(UtilidadTexto.getBoolean(request.getParameter("ocultarMenu")+""));
		else
			forma.setOcultarMenu(false);
	}	
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward validacionesIngresoSala(EventosForm forma, HttpServletRequest request, ActionMapping mapping, Connection con)
	{
		ActionErrors errores = new ActionErrors();
		logger.info("\n codigo evento hoja anes->"+forma.getCodigoEvento()+"  ->"+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+"\n");
		if(forma.getCodigoEvento()==ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx)
		{
			logger.info("entra");
			//la fecha/hora ingreso - egreso sala es requerido
			SolicitudesCx solicitudCx = new SolicitudesCx();		
			solicitudCx.cargarEncabezadoSolicitudCx(forma.getNumeroSolicitud()+"");
			
			if(UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala())
				|| UtilidadTexto.isEmpty(solicitudCx.getHoraIngresoSala())
				)
			{
				logger.info("debe ser error!!!!");
				errores.add("", new ActionMessage("errors.required", "La fecha/hora ingreso sala"));
	        	saveErrors(request, errores);
	        	UtilidadBD.closeConnection(con);
				return mapping.findForward("eventos");
			}
		}
		
		return null;
	}
}