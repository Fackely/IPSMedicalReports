/*
 * @(#)CuposExtraAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.agenda;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.agenda.CuposExtraForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.CuposExtra;

/**
 * Clase encargada del control de la funcionalidad de Cupos Extra

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 09 /May/ 2006
 */
public class CuposExtraAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(CuposExtraAction.class);
	boolean esNuevo=false;

	/**
	 * Mï¿½todo excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		
		 Connection con = null;
		 try {

		if(form instanceof CuposExtraForm)
	    {
		    
		    /**Intentamos abrir una conexion con la fuente de datos**/ 
			con = openDBConnection(con); 
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			HttpSession session = request.getSession();
			UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");

			CuposExtra mundo =new CuposExtra();
			CuposExtraForm forma=(CuposExtraForm)form;
			
			String estado = forma.getEstado();
			logger.warn("[CuposExtraAction] estado->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de CuposExtraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
			else if(estado.equals("empezar"))
			{
				forma.reset();
				
				// Obtener centros de atención validos para el usuario
				//UnidadAgendaUsuarioCentro mundoUAUC = new UnidadAgendaUsuarioCentro();
				//forma.setUnidadAgendaMap(mundoUAUC.consultaUAP(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, usuario.getLoginUsuario()));
				forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
				forma.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaCuposExtra));
				forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaCuposExtra,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("inicioBusqueda");
			}
			else if(estado.equals("resultadoBusqueda"))
			{
				return this.accionBusquedaAvanzada(forma, mapping, con, mundo, usuario);
			}
			else if(estado.equals("guardar"))
			{
				return this.accionGuardar(con, mapping, request, forma, mundo, usuario);
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("cambiarCentroAtencion"))
			{
				forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), forma.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaCuposExtra,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("inicioBusqueda");
			}
	    }
		else
		{
			logger.error("El form no es compatible con el form de Formato de Impresion de Factura");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}	


		 }catch (Exception e) {
			   Log4JManager.error(e);
			  }
			  finally{
			   UtilidadBD.closeConnection(con);
			  }
		return null;
	}
	
	/**
	 * Action para realizar la busqueda avanzada de las agendas a las
	 * que se le pretende modificar el numero ed cupos
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada (CuposExtraForm forma, ActionMapping mapping,  Connection con, CuposExtra mundo, UsuarioBasico usuariob) throws SQLException
	{
		// Capturar los centros de atencion si se ha seleccionado la opcion todos
		if (forma.getCentroAtencion()==ConstantesBD.codigoNuncaValido){
			forma.setCentrosAtencion(forma.getCentrosAtencionAutorizados("todos").toString());
		}
		
		// Capturar las unidades de agenda si se ha seleccionado la opcion todos
		if (forma.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido){
			forma.setUnidadesAgenda(forma.getUnidadesAgendaAutorizadas("todos").toString());
		}
		
		String minutosEspera = ValoresPorDefecto.getMinutosEsperaCitaCaduca(usuariob.getCodigoInstitucionInt());
		forma.setMapaCuposExtras(mundo.busquedaAgendaGenerada(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoUnidadConsulta(), forma.getCodigoConsultorio(), forma.getCodigoDiaSemana(), forma.getCodigoMedico(), minutosEspera, forma.getCentroAtencion(), forma.getCentrosAtencion(), forma.getUnidadesAgenda()));
		
		UsuarioBasico usuario= new UsuarioBasico();
		usuario.cargarUsuarioBasico(con, forma.getCodigoMedico());
		forma.setNombreProfesional(usuario.getNombreUsuario());
		if(forma.getNombreProfesional().equals(""))
		{
			forma.setNombreProfesional("No Asignado");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoBusqueda");		
		
	}
	
	/**
	 * Accion para guardar la actualizacion de los cupos extra en la agenda y asi mismo guarda
	 * el log de modificacion de los cupos extras y disponibles de una agenda determinada
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, HttpServletRequest request, CuposExtraForm forma, CuposExtra mundo, UsuarioBasico usuario) throws SQLException
	{
		int numRegistros = Integer.parseInt(forma.getMapaCuposExtras("numRegistros").toString());
		int numeroCupos = 0;
		String numCuposTemp= "";
		String check = "";
		int insercion = 0;
		int actualizacion = 0;
		String horaCita[];
		String hortaTemp = "";
		String fechaCita = "";
		String minutosEspera = "";
		String fechaEvaluar = "";
		minutosEspera = ValoresPorDefecto.getMinutosEsperaCitaCaduca(usuario.getCodigoInstitucionInt());
		
		/**********VALIDAMOS LOS POSIBLES ERRORES EN LA INSERCION POR ERRORES EN EL FORM***********/
		ActionErrors errores=new ActionErrors();
		String check1 = "";
		String temporal = "";
		boolean hayValor = false;
		for(int j = 0 ; j < Integer.parseInt(forma.getMapaCuposExtras("numRegistros").toString()) ; j++)
		{
			check = forma.getMapaCuposExtras("check2_"+j).toString();
			temporal =  forma.getMapaCuposExtras("cuposextra_"+j).toString();
			if(!check.equals("") && !temporal.equals(""))
			{
				hayValor = true;
			}
		}
		if(!hayValor)
		{
			errores.add("Debe chequear", new ActionMessage("error.cuposExtras.debeChequearCampoParaGuardar"));
		}
		
		for(int i = 0 ; i < Integer.parseInt(forma.getMapaCuposExtras("numRegistros").toString()) ; i++)
		{
			check1 = forma.getMapaCuposExtras("check2_"+i).toString();
			//Validamos que si el check es true el valor de los cupos extras debe ser mayor que cero
			if(check1.equals("true"))
			{
				temporal = forma.getMapaCuposExtras("cuposextra_"+i).toString();
				if(!temporal.equals(""))
				{
					if(Integer.parseInt(forma.getMapaCuposExtras("cuposextra_"+i).toString()) == 0)
					{
						errores.add("Cupo Menor o Igual a Cero", new ActionMessage("errors.integerMayorQue","Los Cupos Extras", "Cero(0)"));
					}
				}
				if(temporal.equals(""))
				{
					errores.add("Cupo Menor o Igual a Cero", new ActionMessage("errors.integerMayorQue","Los Cupos Extras", "Cero(0)"));
				}
			}
			temporal = forma.getMapaCuposExtras("cuposextra_"+i)+"";
			if(!temporal.equals(""))
			{
				if(!forma.getMapaCuposExtras("check2_"+i).toString().equals("true"))
				{
					errores.add("Debe chequear", new ActionMessage("error.cuposExtras.debeChequearCampoParaGuardar"));
				}
			}
			//Validamos contra el paramtetro MINUTOS DE ESPERA PARA ASIGNAR CITAS CADUCADAS
			horaCita = forma.getMapaCuposExtras("hora_"+i).toString().split(":");
			hortaTemp = horaCita[0]+":"+horaCita[1];
			fechaCita = forma.getMapaCuposExtras("fecha_"+i).toString();
			
			if(!temporal.equals(""))
			{
				if(Integer.parseInt(forma.getMapaCuposExtras("cuposextra_"+i).toString()) > 0)
				{
					if(check1.equals("true"))
					{
						if(!minutosEspera.equals("") && !minutosEspera.equals("null") && minutosEspera != null)
						{
							String[] serpFechaHora=UtilidadFecha.incrementarMinutosAFechaHora(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), (Integer.parseInt(minutosEspera)*(-1)), false);
							fechaEvaluar = UtilidadFecha.conversionFormatoFechaABD(serpFechaHora[0])+"_"+serpFechaHora[1];
							
							String fechaHoraCita=fechaCita.substring(0, 10)+"_"+hortaTemp;
							
							logger.info("Actual - parametro: "+fechaEvaluar);
							logger.info("Cita: "+fechaHoraCita);
							
							if(fechaHoraCita.compareTo(fechaEvaluar)<0)
								errores.add("despues de minutos de caducidad", new ActionMessage("error.cuposExtras.horaExcedeMinutosCaducidad",horaCita));
							else {
								if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechaCita),UtilidadFecha.getFechaActual()))
									errores.add("fecha anterior a actual", new ActionMessage("error.cuposExtras.fechaSueriorActualNoAplica", "del Sistema"));
							}
							
					}else if(UtilidadFecha.conversionFormatoFechaAAp(fechaCita).compareTo(UtilidadFecha.getFechaActual()) < 0)
							{
								errores.add("fecha anterior a actual", new ActionMessage("error.cuposExtras.fechaSueriorActualNoAplica", "del Sistema"));
							}
						}
						else
						{
							//if(fechaCita.compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())) < 0)
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechaCita), UtilidadFecha.getFechaActual()))
							{
								errores.add("fecha anterior a actual", new ActionMessage("error.cuposExtras.fechaSueriorActualNoAplica", "del Sistema"));
							}
							if(UtilidadFecha.conversionFormatoFechaAAp(fechaCita).compareTo(UtilidadFecha.getFechaActual()) == 0 || UtilidadFecha.conversionFormatoFechaAAp(fechaCita).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())) > 0)
							{
								if(hortaTemp.compareTo(UtilidadFecha.getHoraActual())<0)
								{
									errores.add("hora anterior a actual", new ActionMessage("error.cuposExtras.horaSueriorActualNoAplica", "del Sistema"));
								}
							}
						}
					}
				}
			}
		
		
		if(errores.size()>0)
		{
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("resultadoBusqueda");
		}
		else
		{
			/*********************************************************************************************/
			//Iniciamos la transaccion
			UtilidadBD.iniciarTransaccion(con);
			for(int i = 0 ; i < numRegistros ; i++)
			{
				numCuposTemp = forma.getMapaCuposExtras("cuposextra_"+i).toString();
				if(!numCuposTemp.equals(""))
				{
					numeroCupos = Integer.parseInt(numCuposTemp);
				}
				check = forma.getMapaCuposExtras("check2_"+i).toString();
				if(numeroCupos > 0 && check.equals("true"))
				{
					actualizacion = mundo.actualizarCuposEnAgenda(con, numeroCupos, Integer.parseInt(forma.getMapaCuposExtras("codigoagenda_"+i).toString()));
				}
			}
			//Insertamos los cupos extra en el LOG tipo base de datos
			insercion = mundo.insertarCuposExtra(con, forma.getMapaCuposExtras(), usuario.getLoginUsuario());

			if(actualizacion <= 0 || insercion <= 0)
			{
				forma.setMensajeCulminacion("Proceso No Exitoso. Por favor Verifique");
				//Si es erronea abortamos la transaccion
				UtilidadBD.abortarTransaccion(con);
			}
			else
			{
				
				String minutosEsperaNuevos = ValoresPorDefecto.getMinutosEsperaCitaCaduca(usuario.getCodigoInstitucionInt());
				//Realizamos de nuevo la busqueda para quedar en la pagina actual con los cambios reflejados
				forma.setMapaCuposExtras(mundo.busquedaAgendaGenerada(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoUnidadConsulta(), forma.getCodigoConsultorio(), forma.getCodigoDiaSemana(), forma.getCodigoMedico(), minutosEsperaNuevos, forma.getCentroAtencion(), forma.getCentrosAtencion(), forma.getUnidadesAgenda()));
				UsuarioBasico usuario2= new UsuarioBasico();
				usuario2.cargarUsuarioBasico(con, forma.getCodigoMedico());
				forma.setNombreProfesional(usuario2.getNombreUsuario());
				
				forma.setMensajeExitoso("Proceso realizado con Exito"); 
				
				//De lo contrario finlaizamos la transaccion
				UtilidadBD.finalizarTransaccion(con);
				return mapping.findForward("resultadoBusqueda");

				
			}
			
			//Cerramos la conexion
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resultadoBusqueda");
		}
	}
	
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		{
			return con;
		}
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			return null;
		}
	
		return con;
	}
}
	