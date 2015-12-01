/*
 * Creado May 11, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DetalleCoberturaAction
 * com.princetonsa.action.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.action.facturacion;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.DetalleCoberturaForm;
import com.princetonsa.actionform.odontologia.ProgramasOdontologicosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.DetalleCobertura;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
public class DetalleCoberturaAction extends Action
{
	/**
	 * 
	 */
	private Logger logger=Logger.getLogger(DetalleCoberturaAction.class);

	/**
	 * 
	 */
	private String[] indicesCobertura={"codigo_","codigocobertura_","desccobertura_","institucion_","viaingreso_","nomviaingreso_","natpaciente_","nomnatpaciente_","usuario_","fecha_","hora_","tiporegistro_","tipopaciente_","nomtipopaciente_","tipospaciente_","tipocobertura_"};

	/**
	 * 
	 */
	private String[] indicesAgrupacionArticulos={"codigo_","codigodetallecob_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","requiereautorizacion_","semanasmincotizacion_","cantidad_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesArticulos={"codigo_","codigodetallecob_","codigoArticulo_","descripcionArticulo_","requiereautorizacion_","semanasmincotizacion_","cantidad_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesAgrupacionServicio={"codigo_","codigodetallecob_","tipopos_","tipopossubsidiado_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","requiereautorizacion_","semanasmincotizacion_","cantidad_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesServicios={"codigo_","codigodetallecob_","codigoServicio_","descripcionServicio_","requiereautorizacion_","semanasmincotizacion_","cantidad_","tiporegistro_"};
	
	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof DetalleCoberturaForm)
			{
				DetalleCoberturaForm forma=(DetalleCoberturaForm) form;
				DetalleCobertura mundo=new DetalleCobertura();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				con=UtilidadBD.abrirConexion();
				String estado=forma.getEstado();
				ActionErrors errores = new  ActionErrors();

				logger.info("ESTADO -->"+estado);

				forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));

				if(estado.equals("empezar"))
				{
					forma.reset();
					return this.accionEmpezar(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("nuevaCobertura"))
				{
					int pos = Integer.parseInt(forma.getCoberturas("numRegistros").toString());
					Utilidades.nuevoRegistroMapaGenerico(forma.getCoberturas(),indicesCobertura,"numRegistros","tiporegistro_","MEM");
					forma.setCoberturas("tipospaciente_"+pos, new ArrayList<HashMap<String, Object>>());
					forma.setCoberturas("tipocobertura_"+pos,ConstantesIntegridadDominio.acronimoTipoCoberturaGeneral);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getCoberturas("numRegistros").toString()), response, request, "detalleCobertura.jsp",true);
				}
				else if(estado.equals("eliminarCobertura"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getCoberturas(),forma.getCoberturasEliminados(),forma.getPosEliminar(),indicesCobertura,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getCoberturas("numRegistros").toString()), response, request, "detalleCobertura.jsp",forma.getPosEliminar()==Integer.parseInt(forma.getCoberturas("numRegistros").toString()));
				}
				else if(estado.equals("guardarCobertura"))
				{
					return this.accionGuardarCobertura(con, forma, mundo, usuario, mapping,errores, request);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					try
					{
						response.sendRedirect(forma.getLinkSiguiente());
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					return null;
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("detalleCobertura"))
				{
					forma.setOperacionTrue(false);
					this.accionDetalleCobertura(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");
				}


				//estado de la composicion
				else if(estado.equals("nuevaAgrupacion"))
				{
					forma.setOperacionTrue(false);
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionArticulos(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");

				}
				else if(estado.equals("eliminarAgrupacion"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionArticulos(),forma.getAgrupacionArticulosEliminados(),forma.getPosEliminar(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");	
				}

				else if(estado.equals("nuevoArticulo"))
				{
					String[] indicesTemp={"codigo_","codigodetallecob_","requiereautorizacion_","semanasmincotizacion_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");
				}
				else if(estado.equals("eliminarArticulo"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getArticulos(),forma.getArticulosEliminados(),forma.getPosEliminar(),indicesArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");	
				}
				else if(estado.equals("nuevaAgrupacionServicio"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionServicios(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");

				}
				else if(estado.equals("eliminarAgrupacionServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionServicios(),forma.getAgrupacionServiciosEliminados(),forma.getPosEliminar(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");	
				}
				else if(estado.equals("nuevoServicio"))
				{
					String[] indicesTemp={"codigo_","codigodetallecob_","requiereautorizacion_","semanasmincotizacion_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getServicios(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");
				}
				else if(estado.equals("eliminarServicio"))
				{
					Utilidades.imprimirMapa(forma.getServicios());
					Utilidades.eliminarRegistroMapaGenerico(forma.getServicios(),forma.getServiciosEliminados(),forma.getPosEliminar(),indicesServicios,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");	
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					//consultar lo que se guardo.
					this.accionDetalleCobertura(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCobertura");
				}
				else if(estado.equals("consultarTipoPaciente"))
				{
					this.accionConsultarTipoPaciente(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//Agregado por anexo 895
				else if (estado.equals("verificarTipoCoberturaOdon"))
				{
					this.accionVerificarTipoCoberturaOdon(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("eliminarCoberturaProgramas"))
				{
					this.accionEliminarCoberturaProgramas(con, forma, mundo, usuario);
					return mapping.findForward("detalleCobertura");
				}

				else if (estado.equals("cargarDetallePrograma"))
				{
					this.accionCargarDetallePrograma(con, forma, mundo, usuario);
					return mapping.findForward("detalleProgramas");
				}

				else if (estado.equals("eliminarCoberturaServicios"))
				{
					this.accionEliminarCoberturaServicios(con, forma, mundo, usuario);
					return mapping.findForward("detalleCobertura");
				}

				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}


			}
			else
			{
				logger.error("Error con la forma (Revisar StrutsConfig)");
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
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private void accionCargarDetallePrograma(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario)
	{
		double codigoPrograma=forma.getListadoCoberturaProgramas().get(forma.getPosCoberturaProgramas()).getPrograma();
		String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		forma.setListadoDetalleProgramas(ProgramasOdontologicos.cargarDetallePrograma(codigoPrograma,tmpBusquedaServicios));
	}
	
	private void accionEliminarCoberturaProgramas(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario)
	{
		double codigoCoberturaAEliminar=forma.getListadoCoberturaProgramas().get(forma.getPosCoberturaProgramas()).getCodigo();
		mundo.eliminarCoberturaProgramas(con,codigoCoberturaAEliminar);
		forma.setListadoCoberturaProgramas(mundo.consultarCoberturaProgramas(con,forma.getCoberturaProgramas()));
		forma.resetCoberturaProgramas();
	}
	
	
	private void accionEliminarCoberturaServicios(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario)
	{
		double codigoCoberturaAEliminar=forma.getListadoCoberturaServicios().get(forma.getPosCoberturaServicios()).getCodigo();
		mundo.eliminarCoberturaServicios(con,codigoCoberturaAEliminar);
		forma.setListadoCoberturaServicios(mundo.consultarCoberturaServicios(con,forma.getCoberturaServicios()));
		forma.resetCoberturaServicios();
	}
	
	/**
	 * Agregado por anexo 895
	 * 
	 */
	private void accionVerificarTipoCoberturaOdon (Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario) 
	{
		String tipoCobertura=forma.getCoberturas().get("codigocobertura_"+forma.getPosCobertura())+"";
		String[] arrayTipoCobertura=tipoCobertura.split(ConstantesBD.separadorSplit);
		logger.info("el valor del splitteado------->"+arrayTipoCobertura[1]);
		
		if (arrayTipoCobertura[1].equals(ConstantesIntegridadDominio.acronimoTipoCoberturaOdontologico))
			forma.getCoberturas().put("tipocobertura_"+forma.getPosCobertura(),ConstantesIntegridadDominio.acronimoTipoCoberturaOdontologico);
		else 
			forma.getCoberturas().put("tipocobertura_"+forma.getPosCobertura(),ConstantesIntegridadDominio.acronimoTipoCoberturaGeneral);
		
	}

	/**
	 * Consultar tipos de pacientes según la vía de ingreso
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarTipoPaciente(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario) {
		//	se carga el tipo de paciente
		forma.setCoberturas("tipospaciente_"+forma.getPosMapa(),UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con,forma.getCoberturas("viaingreso_"+forma.getPosMapa()).toString()));
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionArticulos(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarArticulos(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionServicios(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarServicios(con,forma,mundo,usuario,transaccion);
		//Agregado pro Anexo 945
		if(transaccion)
			transaccion=this.accionGuardarProgramas(con,forma,mundo,usuario,transaccion);
		if (transaccion)
			transaccion=this.accionGuardarServiciosNoIncluidosEnPLanTratamiento(con, forma, mundo, usuario, transaccion);
		
		if(transaccion)
		{
			forma.setOperacionTrue(true);
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}	
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarServiciosNoIncluidosEnPLanTratamiento(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, boolean transaccion)
	{
		transaccion=true;
		forma.getCoberturaServicios().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getCoberturaServicios().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.getCoberturaServicios().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getCoberturaServicios().setRequiereAutorizacion(ConstantesBD.acronimoNo);
		//Si hay un programa para insertar
		if (forma.getCoberturaServicios().getCodigoServicio()>0)
		{
			transaccion=mundo.insertarCoberturaServicios(con,forma.getCoberturaServicios());
			forma.setListadoCoberturaServicios(mundo.consultarCoberturaServicios(con,forma.getCoberturaServicios()));
			if (transaccion)
			{
				forma.resetCoberturaServicios();
			}
		}
		
		return transaccion;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarProgramas(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, boolean transaccion)
	{
		transaccion=true;
		forma.getCoberturaProgramas().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getCoberturaProgramas().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.getCoberturaProgramas().setHoraModifica(UtilidadFecha.getHoraActual());
		//Si hay un programa para insertar
		if (forma.getCoberturaProgramas().getPrograma()>0)
		{
			transaccion=mundo.insertarCoberturaProgramas(con,forma.getCoberturaProgramas());
			forma.setListadoCoberturaProgramas(mundo.consultarCoberturaProgramas(con,forma.getCoberturaProgramas()));
			if (transaccion)
				forma.resetCoberturaProgramas();
		}
		
		return transaccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarServicios(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, boolean transaccion)
	{
		for(int i=0;i<Integer.parseInt(forma.getServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarServicios(con,forma.getServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleCoberturaCodigo,indicesServicios);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getServicios(),mundo.consultarServiciosLLave(con, forma.getServicios("codigo_"+i)+""),i,usuario,indicesServicios))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getServicios("codigo_"+i)+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("requiereautorizacion", forma.getServicios("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getServicios("semanasmincotizacion_"+i));
				vo.put("cantidad", forma.getServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarServicios(con, vo);
			}
			//insertar
			else if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigodetallecob", forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("requiereautorizacion", forma.getServicios("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getServicios("semanasmincotizacion_"+i));
				vo.put("cantidad", forma.getServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarServicios(con, vo);
			}
			
		}
		return transaccion;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarAgrupacionServicios(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionServicios(con,forma.getAgrupacionServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleCoberturaCodigo,indicesAgrupacionServicio);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionServicios(),mundo.consultarAgrupacionServiciosLLave(con, forma.getAgrupacionServicios("codigo_"+i)+""),i,usuario,indicesAgrupacionServicio))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionServicios("codigo_"+i)+"");
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("tipopossubsidiado", forma.getAgrupacionServicios("tipopossubsidiado_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("cantidad", forma.getAgrupacionServicios("cantidad_"+i));
				vo.put("requiereautorizacion", forma.getAgrupacionServicios("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getAgrupacionServicios("semanasmincotizacion_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionServicios(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigodetallecob", forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("tipopossubsidiado", forma.getAgrupacionServicios("tipopossubsidiado_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("cantidad", forma.getAgrupacionServicios("cantidad_"+i));
				vo.put("requiereautorizacion", forma.getAgrupacionServicios("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getAgrupacionServicios("semanasmincotizacion_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarAgrupacionServicios(con, vo);
			}
		}
		return transaccion;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarArticulos(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarArticulos(con,forma.getArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleCoberturaCodigo,indicesArticulos);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getArticulos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getArticulos(),mundo.consultarArticulosLLave(con, forma.getArticulos("codigo_"+i)+""),i,usuario,indicesArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getArticulos("codigo_"+i)+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("cantidad", forma.getArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("requiereautorizacion", forma.getArticulos("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getArticulos("semanasmincotizacion_"+i));
				transaccion=mundo.modificarArticulos(con, vo);
			}
			//insertar
			else if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigodetallecob", forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("cantidad", forma.getArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("requiereautorizacion", forma.getArticulos("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getArticulos("semanasmincotizacion_"+i));
				transaccion=mundo.insertarArticulos(con, vo);
			}
			
		}
		return transaccion;
	}

	/**
	 * 
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarAgrupacionArticulos(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionArticulos(con,forma.getAgrupacionArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleCoberturaCodigo,indicesAgrupacionArticulos);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulos("numRegistros")+"");i++)
		{
			if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionArticulos(),mundo.consultarAgrupacionArticulosLLave(con, forma.getAgrupacionArticulos("codigo_"+i)+""),i,usuario,indicesAgrupacionArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionArticulos("codigo_"+i)+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("requiereautorizacion", forma.getAgrupacionArticulos("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getAgrupacionArticulos("semanasmincotizacion_"+i));
				vo.put("cantidad", forma.getAgrupacionArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionArticulos(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigodetallecob", forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("requiereautorizacion", forma.getAgrupacionArticulos("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getAgrupacionArticulos("semanasmincotizacion_"+i));
				vo.put("cantidad", forma.getAgrupacionArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarAgrupacionArticulos(con, vo);
			}
		}
		return transaccion;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 */
	private void accionDetalleCobertura(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario)
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+""));
		
		int n=Utilidades.convertirAEntero(forma.getCoberturas("numRegistros")+"");
		for(int i=0;i<n;i++){
			if(i==forma.getIndexSeleccionado()){
				forma.setPosi(i);	
			}
			
		}
		
		//Cambios por anexo 945
		if (forma.getCoberturas("tipocobertura_"+forma.getIndexSeleccionado()).toString().equals(ConstantesIntegridadDominio.acronimoTipoCoberturaOdontologico))
		{
			if (ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
			{
				forma.setTipoCobertura(ConstantesBD.acronimoSi);
				//Si se manejan programas odontologicos, entonces seteo el dto de cobertura de programas y servicios
				forma.getCoberturaProgramas().setCodigoDetalleCob(Utilidades.convertirADouble(forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+""));
				forma.getCoberturaServicios().setCodigoDetalleCob(Utilidades.convertirADouble(forma.getCoberturas("codigo_"+forma.getIndexSeleccionado())+""));
				
				forma.setListadoCoberturaProgramas(mundo.consultarCoberturaProgramas(con,forma.getCoberturaProgramas()));
				forma.setListadoCoberturaServicios(mundo.consultarCoberturaServicios(con,forma.getCoberturaServicios()));
			}
			else 
			{
				if (ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
					forma.setTipoCobertura(ConstantesBD.acronimoNo);
			}
		}
		else
		{
			forma.setTipoCobertura(ConstantesBD.acronimoNo);
		}
			//forma.setTipoCobertura(ConstantesBD.acronimoSi);
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(DetalleCoberturaForm forma)
	{
		int n=Utilidades.convertirAEntero(forma.getCoberturas("numRegistros")+"");
		
		for(int i=0;i<n;i++)
		{
			if((forma.getCoberturas("nomnatpaciente_"+i)+"").equals(""))
				forma.setCoberturas("nomnatpaciente_"+i, "todas");
			if((forma.getCoberturas("nomtipopaciente_"+i)+"").equals(""))
				forma.setCoberturas("nomtipopaciente_"+i, "todas");
			if((forma.getCoberturas("nomviaingreso_"+i)+"").equals(""))
				forma.setCoberturas("nomviaingreso_"+i, "todas");
		}
		
		int numReg=Utilidades.convertirAEntero(forma.getCoberturas("numRegistros")+"");
		forma.setCoberturas(Listado.ordenarMapa(indicesCobertura,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getCoberturas(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setCoberturas("numRegistros",numReg+"");
		
		logger.info("\n\nel mapa despues del cambio ex:"+forma.getCoberturas());
		
		for(int i=0;i<n;i++)
		{
			if((forma.getCoberturas("nomnatpaciente_"+i)+"").equals("todas"))
				forma.setCoberturas("nomnatpaciente_"+i, "");
			if((forma.getCoberturas("nomtipopaciente_"+i)+"").equals("todas"))
				forma.setCoberturas("nomtipopaciente_"+i, "");
			if((forma.getCoberturas("nomviaingreso_"+i)+"").equals("todas"))
				forma.setCoberturas("nomviaingreso_"+i, "");
		}		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarCobertura(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, ActionMapping mapping, ActionErrors errores, HttpServletRequest request)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getCoberturasEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarDetalleCobertura(con,forma.getCoberturasEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getCoberturasEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleCoberturaCodigo,indicesCobertura);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getCoberturas("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getCoberturas("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getCoberturas(),mundo.consultarDetalleCoberturaLLave(con, forma.getCoberturas("codigo_"+i)+""),i,usuario,indicesCobertura))
			{
				
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getCoberturas("codigo_"+i)+"");
				
				//Modificado por Anexo 895
				String tipoCobertura=forma.getCoberturas("codigocobertura_"+i)+"";
				String[] arrayTipoCobertura=tipoCobertura.split(ConstantesBD.separadorSplit);
				vo.put("codigoCobertura", arrayTipoCobertura[0]);
				
				logger.info("el codigo de cobertura------->"+forma.getCoberturas("codigo_"+i).toString());
				
				if (forma.getCoberturas("codigo_"+i).toString().equals(""))
				{
					errores.add("", new ActionMessage("errors.notEspecific", " Debe existir la cobertura para el registro "+(i+1)));
					saveErrors(request, errores);
				}
				
				vo.put("institucion", forma.getCoberturas("institucion_"+i));
				vo.put("viaIngreso", forma.getCoberturas("viaingreso_"+i));
				vo.put("natPaciente", forma.getCoberturas("natpaciente_"+i));
				vo.put("tipoPaciente", forma.getCoberturas("tipopaciente_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				
				if (!forma.getCoberturas("codigo_"+i).toString().equals(" "))
					transaccion=mundo.modificarDetalleCobertura(con, vo);
			}
			//insertar
			else if((forma.getCoberturas("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				
				//Modificado por Anexo 895
				String tipoCobertura=forma.getCoberturas("codigocobertura_"+i)+"";
				String[] arrayTipoCobertura=tipoCobertura.split(ConstantesBD.separadorSplit);
				vo.put("codigoCobertura", arrayTipoCobertura[0]);
				
				logger.info("el codigo de cobertura------->"+forma.getCoberturas("codigocobertura_"+i).toString());
				
				if (forma.getCoberturas("codigocobertura_"+i).toString().equals(" "+ConstantesBD.separadorSplit+" "))
				{
					errores.add("", new ActionMessage("errors.notEspecific", " Debe existir la cobertura para el registro "+(i+1)));
					saveErrors(request, errores);
				}
				
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("viaIngreso", forma.getCoberturas("viaingreso_"+i));
				vo.put("natPaciente", forma.getCoberturas("natpaciente_"+i));
				vo.put("tipoPaciente", forma.getCoberturas("tipopaciente_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				
				if (!forma.getCoberturas("codigocobertura_"+i).toString().equals(" "+ConstantesBD.separadorSplit+" "))
					transaccion=mundo.insertarDetalleCobertura(con, vo);
			}
		}
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			for(int i=0;i<Integer.parseInt(forma.getCoberturas("numRegistros")+"");i++)
			{
				forma.setCoberturas("tiporegistro_"+i,"BD");
			}
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}	
		
		
		//consultar nuevamente
		forma.reset();
		
		if (transaccion)
			forma.setOperacionTrue(true);
		
		return this.accionEmpezar(con,forma,mundo,usuario,mapping);
		//return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, DetalleCoberturaForm forma, DetalleCobertura mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		//incializar la informacion de los select
		forma.setSelCoberturas(mundo.obtenerListadoCoberturasInstitucion(con,usuario.getCodigoInstitucionInt()));
		forma.setSelViasIngreso((Vector<InfoDatosString>)Utilidades.obtenerViasIngreso(con,true));
		forma.setSelNaturalezaPaciente(Utilidades.obtenerNaturalezasPaciente(con,"",ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
		
		forma.setCoberturas(mundo.obtenerListadoCoberturas(con,usuario.getCodigoInstitucionInt()));
		
		for(int i=0;i<Integer.parseInt(forma.getCoberturas("numRegistros").toString());i++)
			forma.setCoberturas("tipospaciente_"+i, UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getCoberturas("viaingreso_"+i).toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @param mapaTemp
	 * @param pos
	 * @param usuario
	 * @param indices 
	 * @return
	 */
	private boolean existeModificacion(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices)
	{
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logDetalleCoberturaCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}
}
