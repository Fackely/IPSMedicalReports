package com.princetonsa.action.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.actionform.ordenesmedicas.procedimientos.InterpretarProcedimientosForm;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.InterpretarProcedimiento;

public class InterpretarProcedimientosAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(InterpretarProcedimientosAction.class);
	
	/**
	 * Cadena que contiene los indices del mapa.
	 */
	private static final String indicesMapa="solicitud_,fechasolicitud_,horasolicitud_,nombrepaciente_,orden_,procedimiento_,codigoviaingreso_,viaingreso_,codigoestadohc_,estadohc_,servicio_,ccsolicitante_,interpretacion_,adjuntos_,puedointerpretar_,fecharespuesta_,horarespuesta_,diagnosticorespuesta_,codigorespuesta_,cama_,tiposolicitud_,peticion_"; 
	
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{
		Connection con = null;
		try {
			if( form instanceof InterpretarProcedimientosForm)
			{

				con=UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				InterpretarProcedimientosForm forma=(InterpretarProcedimientosForm)form;
				InterpretarProcedimiento mundo=new InterpretarProcedimiento();

				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente= Utilidades.getPersonaBasicaSesion(request.getSession());

				String estado=forma.getEstado();

				logger.info("\n\n ESTADO ======="+estado);

				if (usuario == null) 
				{	
					request.setAttribute("codigoDescripcionError",	"errors.usuario.noCargado");
					return mapping.findForward("paginaError");
				} 
				else if( !UtilidadValidacion.esProfesionalSalud(usuario) )
				{
					if(UtilidadValidacion.estaMedicoInactivo(con,usuario.getCodigoPersona(),usuario.getCodigoInstitucionInt()))
						request.setAttribute("codigoDescripcionError", "errors.profesionalSaludInactivo");
					else
						request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("subMenuOrdenes");
				}
				else if(estado.equals("listadoPaciente"))
				{
					forma.setFiltro("");
					logger.info("VIENE EVOLUCION >>>>>>> "+forma.isVieneEvolucion());
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					ActionForward forward= validacionAccesoPaciente(paciente, request, mapping,usuario);
					if(forward!=null)
					{
						UtilidadBD.closeConnection(con);
						return forward;
					}
					this.accionListarPaciente(con,forma,mundo,paciente,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("listadoRango"))
				{
					accionListarRango(con,forma,mundo,paciente,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("rango"))
				{
					String fechaActual=UtilidadFecha. getFechaActual(con);
					forma.setFechaInicialFiltro(UtilidadFecha.incrementarDiasAFecha(fechaActual, -1, false));
					forma.setFechaFinalFiltro(fechaActual);
					forma.setCentroCostosSolicitanteFiltro("");
					forma.setFiltro("filtroRangoEjecutado");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("filtroSolicitudes");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("interpretar"))
				{

					this.accionInterpretarSolicitud(con,forma,mundo,usuario,paciente);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("mostrarRespuestas"))
				{
					forma.setHistoricoRespuestas(Utilidades.obtenerResultadosRespuestasSolicitudesProcedimientos(con,forma.getNumeroSolicitud()));
					UtilidadBD.closeConnection(con);
					return null;
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
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
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente 
	 */
	private void accionInterpretarSolicitud(Connection con, InterpretarProcedimientosForm forma, InterpretarProcedimiento mundo, UsuarioBasico usuario, PersonaBasica paciente)
	{			
		HashMap tempInterpretacion=new HashMap();		
		String interpretacion=UtilidadCadena.cargarObservaciones(forma.getListaSolicitudes("interpretacion_"+forma.getIndexRegistro())+"","",usuario);
				
		if(mundo.interpretarSolicitud(con,forma.getListaSolicitudes("solicitud_"+forma.getIndexRegistro())+"",interpretacion,usuario.getCodigoPersona()))
		{	
			//Actualiza el estado de la solicitud						
			forma.setListaSolicitudes("codigoestadohc_"+forma.getIndexRegistro(),ConstantesBD.codigoEstadoHCInterpretada+"");								
			forma.setIndicadorExitoOperacion(ConstantesBD.acronimoSi);
			
			int cont = 0;
			for(int i = 0; i < Integer.parseInt(forma.getListaSolicitudes("numRegistros").toString()); i++)
			{
				if(forma.getListaSolicitudes("codigoestadohc_"+i).toString().equals(ConstantesBD.codigoEstadoHCRespondida+""))
				{
					tempInterpretacion.put("solicitud_"+cont,forma.getListaSolicitudes("solicitud_"+i));
					tempInterpretacion.put("ccsolicitante_"+cont,forma.getListaSolicitudes("ccsolicitante_"+i));
					tempInterpretacion.put("fechasolicitud_"+cont,forma.getListaSolicitudes("fechasolicitud_"+i));
					tempInterpretacion.put("horasolicitud_"+cont,forma.getListaSolicitudes("horasolicitud_"+i));
					tempInterpretacion.put("orden_"+cont,forma.getListaSolicitudes("orden_"+i));
					tempInterpretacion.put("servicio_"+cont,forma.getListaSolicitudes("servicio_"+i));
					
					tempInterpretacion.put("procedimiento_"+cont,forma.getListaSolicitudes("procedimiento_"+i));
					tempInterpretacion.put("codigoviaingreso_"+cont,forma.getListaSolicitudes("codigoviaingreso_"+i));
					tempInterpretacion.put("viaingreso_"+cont,forma.getListaSolicitudes("viaingreso_"+i));
					tempInterpretacion.put("nombrepaciente_"+cont,forma.getListaSolicitudes("nombrepaciente_"+i));
					tempInterpretacion.put("codigoestadohc_"+cont,forma.getListaSolicitudes("codigoestadohc_"+i));
					tempInterpretacion.put("estadohc_"+cont,forma.getListaSolicitudes("estadohc_"+i));
					tempInterpretacion.put("cama_"+cont,forma.getListaSolicitudes("cama_"+i));
					tempInterpretacion.put("puedointerpretar_"+cont,forma.getListaSolicitudes("puedointerpretar_"+i));
					tempInterpretacion.put("tiposolicitud_"+cont,forma.getListaSolicitudes("tiposolicitud_"+i));
					tempInterpretacion.put("fecharespuesta_"+cont,forma.getListaSolicitudes("fecharespuesta_"+i));
					tempInterpretacion.put("diagnosticorespuesta_"+cont,forma.getListaSolicitudes("diagnosticorespuesta_"+i));
					tempInterpretacion.put("interpretacion_"+cont,forma.getListaSolicitudes("interpretacion_"+i));
					
					tempInterpretacion.put("codigorespuesta_"+cont,forma.getListaSolicitudes("codigorespuesta_"+i));
					tempInterpretacion.put("horarespuesta_"+cont,forma.getListaSolicitudes("horarespuesta_"+i));
					tempInterpretacion.put("peticion_"+cont,forma.getListaSolicitudes("peticion_"+i));				
					tempInterpretacion.put("adjuntos_"+cont,forma.getListaSolicitudes("adjuntos_"+i));
				
					cont++;
				}
			}			
		
			forma.setListaSolicitudes(new HashMap());
			forma.setListaSolicitudes(tempInterpretacion);
			forma.setListaSolicitudes("numRegistros",cont);		
		}
		else
		{
			forma.setIndicadorExitoOperacion(ConstantesBD.acronimoNo);
			logger.info("Error al actualizar ");
		}
		
				
		/*
		HashMap tempInterpretacion=new HashMap();		
		logger.info("valor del  numRegistros >> "+forma.getListaSolicitudes("numRegistros"));		
		//salvando las interpretaciones de las otras solicitudes
		for(int i=0;i<Integer.parseInt(forma.getListaSolicitudes("numRegistros")+"");i++)
		{
			tempInterpretacion.put(forma.getListaSolicitudes("solicitud_"+i), forma.getListaSolicitudes("interpretacion_"+i));
		}		
		if(forma.getFiltro().equals("filtroRangoEjecutado"))
			this.accionListarRango(con, forma, mundo, paciente, usuario);
		else
			this.accionListarPaciente(con,forma,mundo,paciente,usuario);		
		//poniendo nuevamente las interpretaciones de las otras solicitudes;		
		for(int i=0;i<Integer.parseInt(forma.getListaSolicitudes("numRegistros")+"");i++)
		{
			forma.setListaSolicitudes("interpretacion_"+i,tempInterpretacion.get(forma.getListaSolicitudes("solicitud_"+i)));			
		}				
		logger.info("1valor del  numRegistros >> "+forma.getListaSolicitudes("numRegistros"));
		*/		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 */
	private void accionListarRango(Connection con, InterpretarProcedimientosForm forma, InterpretarProcedimiento mundo, PersonaBasica paciente, UsuarioBasico usuario)
	{
		HashMap vo = generarFiltroUsuario(con,usuario);
		/*
		if(paciente!=null)
		{
			vo.put("paciente", paciente.getCodigoPersona());
		}
		else
		{
			vo.put("paciente", ConstantesBD.codigoNuncaValido+"");
		}
		*/
		vo.put("paciente", ConstantesBD.codigoNuncaValido+"");
		vo.put("fechaInicialFiltro", forma.getFechaInicialFiltro()+"");
		vo.put("fechaFinalFiltro", forma.getFechaFinalFiltro()+"");
		vo.put("centroCostroSolicitanteFiltro", forma.getCentroCostosSolicitanteFiltro()+"");
		vo.put("areaFiltro",forma.getAreaFiltro());
		vo.put("pisoFiltro",forma.getPisoFiltro());
		vo.put("habitacionFiltro", forma.getHabitacionFiltro());
		vo.put("camaFiltro", forma.getCamaFiltro());
		vo.put("cCosto", usuario.getCodigoCentroCosto());
		vo.put("cAtencion", usuario.getCodigoCentroAtencion());
		vo.put("institucion",usuario.getCodigoInstitucionInt());
		vo.put("permitirInterpretarMultiple", ValoresPorDefecto.getPermIntOrdRespMulSinFin(usuario.getCodigoInstitucionInt()));
		forma.setListaSolicitudes(mundo.generarListadoSolicitudesProcedimientosResponder(con, vo));
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 */
	private void accionListarPaciente(Connection con, InterpretarProcedimientosForm forma, InterpretarProcedimiento mundo, PersonaBasica paciente, UsuarioBasico usuario)
	{
		HashMap vo = generarFiltroUsuario(con,usuario);
		vo.put("paciente", paciente.getCodigoPersona());
		vo.put("fechaInicialFiltro", "");
		vo.put("fechaFinalFiltro", "");
		vo.put("centroCostroSolicitanteFiltro", "");
		vo.put("areaFiltro","");
		vo.put("pisoFiltro","");
		vo.put("habitacionFiltro", "");
		vo.put("camaFiltro", "");
		vo.put("cCosto", usuario.getCodigoCentroCosto());
		vo.put("cAtencion", usuario.getCodigoCentroAtencion());
		vo.put("institucion",usuario.getCodigoInstitucionInt());
		vo.put("permitirInterpretarMultiple", ValoresPorDefecto.getPermIntOrdRespMulSinFin(usuario.getCodigoInstitucionInt()));
		
		// restringe el listado de ordenes si viene desde la evolucion, para mostrar
		// solo las que faltan por interpretar y no todas 84889
		if(forma.isVieneEvolucion())
			vo.put("requiereInt", ConstantesBD.acronimoSi);
		
		forma.setListaSolicitudes(mundo.generarListadoSolicitudesProcedimientosResponder(con, vo));
	}


	/**
	 * 
	 * @param con
	 * @param usuario
	 * @return
	 */
	private HashMap generarFiltroUsuario(Connection con, UsuarioBasico usuario)
	{
		
		
		HashMap vo=new HashMap();
		//cargar medico
		Medico m = new Medico();
		m.init(System.getProperty("TIPOBD"));
		try
		{
			m.cargarMedico(con, usuario.getCodigoPersona());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		vo.put("tipoSolicitud", ConstantesBD.codigoTipoSolicitudProcedimiento+"");
		vo.put("centroCostoSolicitante",usuario.getCodigoCentroCosto()+"");
		vo.put("centroCostoIntentaAcceso", usuario.getCodigoCentroCosto()+"");
		vo.put("loginUsurio", usuario.getLoginUsuario()+"");
		
		return vo;
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(InterpretarProcedimientosForm forma)
	{
		String[] indices=indicesMapa.split(",");
		int numReg=Integer.parseInt(forma.getListaSolicitudes("numRegistros")+"");
		forma.setListaSolicitudes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListaSolicitudes(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListaSolicitudes("numRegistros",numReg+"");
	}

	/**
	 * 
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward validacionAccesoPaciente(PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario)
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}
		///Validar que el usuario no se autoatienda
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
		{
			request.setAttribute("codigoDescripcionError", respuesta.getDescripcion());
			return mapping.findForward("paginaError");
		}
					
		
		return null;
	}
}
