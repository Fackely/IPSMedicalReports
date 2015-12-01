/*
 * Creado May 17, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * CoberturasConvenioAction
 * com.princetonsa.action.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.CoberturasConvenioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CoberturasConvenio;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 17, 2007
 */
public class CoberturasConvenioAction extends Action
{
	/**
	 * 
	 */
	private Logger logger=Logger.getLogger(CoberturasConvenioAction.class);
	
	/**
	 * 
	 */
	private String[] indicesCobertura={"codigocobertura_","desccobertura_","prioridad_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesExcepciones={"codigo_","viaingreso_","nomviaingreso_","tipopaciente_","nomtipopaciente_","natpaciente_","nomnatpaciente_","tiporegistro_","tipospaciente_"};	

	/**
	 * 
	 */
	private String[] indicesAgrupacionArticulos={"codigo_","codigoexcepcion_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","requiereautorizacion_","semanasmincotizacion_","cantidad_","presfactura_","incluido_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesArticulos={"codigo_","codigoexcepcion_","codigoArticulo_","descripcionArticulo_","requiereautorizacion_","semanasmincotizacion_","cantidad_","presfactura_","incluido_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesAgrupacionServicio={"codigo_","codigoexcepcion_","tipopos_","tipopossubsidiado_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","requiereautorizacion_","semanasmincotizacion_","cantidad_","incluido_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesServicios={"codigo_","codigoexcepcion_","codigoServicio_","descripcionServicio_","requiereautorizacion_","semanasmincotizacion_","cantidad_","incluido_","tiporegistro_"};

	
	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof CoberturasConvenioForm)
			{
				CoberturasConvenioForm forma=(CoberturasConvenioForm) form;
				CoberturasConvenio mundo=new CoberturasConvenio();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				con=UtilidadBD.abrirConexion();
				String estado=forma.getEstado();

				logger.info("ESTADO -->"+estado);

				forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));

				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setConvenios(mundo.obtenerConvenios(con));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				if(estado.equals("empezarConsulta"))
				{
					forma.reset();
					forma.setConvenios(mundo.obtenerConvenios(con));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarContratos"))
				{
					forma.setContrato(ConstantesBD.codigoNuncaValido);

					if(!forma.getCodigoConvenioStr().toString().equals(ConstantesBD.codigoNuncaValido+"")){
						//Cambio por anexo 945 para obtener el tipo de atencion del convenio
						forma.setTipoAtencionConvenio(forma.getCodigoConvenioStr().split(ConstantesBD.separadorSplit)[1]);
						forma.setCodigoConvenio(Utilidades.convertirAEntero(forma.getCodigoConvenioStr().split(ConstantesBD.separadorSplit)[0]));
						//Fin Cambio
					}

					forma.setContratos(Utilidades.obtenerContratos(con,forma.getCodigoConvenio(),true,false));
					if(forma.getContratos().size()==1)
					{
						forma.setContrato(Integer.parseInt(((HashMap)(forma.getContratos().get(0))).get("codigo")+""));
						return this.accionCargarInfo(con,forma,mundo,usuario,mapping,true);
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarInfo"))
				{
					return this.accionCargarInfo(con,forma,mundo,usuario,mapping,true);
				}
				else if(estado.equals("nuevaCobertura"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getCoberturas(),indicesCobertura,"numRegistros","tiporegistro_","MEM");
					int numRegistros=Utilidades.convertirAEntero(forma.getCoberturas().get("numRegistros")+"");
					forma.setCoberturas("prioridad_"+(numRegistros-1),numRegistros);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarCobertura"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getCoberturas(),forma.getCoberturasEliminados(),forma.getPosEliminar(),indicesCobertura,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevaExcepcion"))
				{
					int pos = Integer.parseInt(forma.getExcepciones("numRegistros").toString());
					Utilidades.nuevoRegistroMapaGenerico(forma.getExcepciones(),indicesExcepciones,"numRegistros","tiporegistro_","MEM");
					forma.setExcepciones("tipospaciente_"+pos, new ArrayList<HashMap<String, Object>>());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarExcepcion"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getExcepciones(),forma.getExcepcionesEliminados(),forma.getPosEliminar(),indicesExcepciones,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//seccion para excepcion
				else if(estado.equals("guardarCoberturaConvenio"))
				{
					return this.accionGuardarCobertura(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("detalleExcepcion"))
				{
					forma.setMensaje("");
					return this.accionDetalleExcepcion(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("nuevaAgrupacion"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionArticulos(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","MEM");
					forma.setAgrupacionArticulos("incluido_"+(Utilidades.convertirAEntero(forma.getAgrupacionArticulos("numRegistros")+"")-1), ConstantesBD.acronimoSi);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("eliminarAgrupacion"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionArticulos(),forma.getAgrupacionArticulosEliminados(),forma.getPosEliminar(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}

				else if(estado.equals("nuevoArticulo"))
				{
					//				String[] indicesTemp={"codigo_","codigoexcepcion_","requiereautorizacion_","semanasmincotizacion_","cantidad_","presfactura_","incluido_","tiporegistro_"};
					//				Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					//				
					//				forma.setArticulos("incluido_"+(Utilidades.convertirAEntero(forma.getArticulos("numRegistros")+"")), ConstantesBD.acronimoSi);
					//				Utilidades.imprimirMapa(forma.getArticulos());
					//				
					//				UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("volverDetalle"))
				{

					String[] indicesTemp={"codigo_","codigoexcepcion_","requiereautorizacion_","semanasmincotizacion_","cantidad_","presfactura_","incluido_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");

					forma.setArticulos("incluido_"+(Utilidades.convertirAEntero(forma.getArticulos("numRegistros")+"")), ConstantesBD.acronimoSi);
					Utilidades.imprimirMapa(forma.getArticulos());

					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}

				else if(estado.equals("eliminarArticulo"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getArticulos(),forma.getArticulosEliminados(),forma.getPosEliminar(),indicesArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("nuevaAgrupacionServicio"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionServicios(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","MEM");
					forma.setAgrupacionServicios("incluido_"+(Utilidades.convertirAEntero(forma.getAgrupacionServicios("numRegistros")+"")-1), ConstantesBD.acronimoSi);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("eliminarAgrupacionServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionServicios(),forma.getAgrupacionServiciosEliminados(),forma.getPosEliminar(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("nuevoServicio"))
				{
					String[] indicesTemp={"codigo_","codigoexcepcion_","requiereautorizacion_","semanasmincotizacion_","cantidad_","presfactura_","incluido_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getServicios(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					forma.setServicios("incluido_"+(Utilidades.convertirAEntero(forma.getServicios("numRegistros")+"")-1), ConstantesBD.acronimoSi);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getServicios(),forma.getServiciosEliminados(),forma.getPosEliminar(),indicesServicios,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					//consultar lo que se guardo.
					return this.accionDetalleExcepcion(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("consultarTipoPaciente"))
				{
					this.accionConsultarTipoPaciente(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//Agragado por Anexo 945
				else if (estado.equals("cargarDetallePrograma"))
				{
					this.accionCargarDetallePrograma(con, forma, mundo, usuario);
					return mapping.findForward("detalleProgramas");
				}
				else if (estado.equals("eliminarExcepcionProgramas"))
				{
					this.accionEliminarExcepcionProgramas(con, forma, mundo, usuario);
					return mapping.findForward("detalle");
				}
				//Fin Anexo 945
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
	 * Consultar tipos de pacientes según la vía de ingreso
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarTipoPaciente(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario) 
	{
		logger.info("MAPA EXCEPCIONES=> "+forma.getExcepciones());
		//se carga el tipo de paciente
		forma.setExcepciones("tipospaciente_"+forma.getPosMapa(),UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con,forma.getExcepciones("viaingreso_"+forma.getPosMapa()).toString()));
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario)
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
		if(transaccion)
			transaccion=this.accionGuardarProgramas(con,forma,mundo,usuario,transaccion);
		if(transaccion)
		{
			forma.setMensaje("Proceso Realizado con Éxito!");
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje("Problemas en el Proceso");
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
	private boolean accionGuardarServicios(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		for(int i=0;i<Integer.parseInt(forma.getServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarServicios(con,forma.getServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesServicios);
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
				vo.put("presfactura", forma.getServicios("presfactura_"+i));
				vo.put("incluido", forma.getServicios("incluido_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarServicios(con, vo);
			}
			//insertar
			else if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("contrato", forma.getContrato());
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("requiereautorizacion", forma.getServicios("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getServicios("semanasmincotizacion_"+i));
				vo.put("cantidad", forma.getServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("presfactura", forma.getServicios("presfactura_"+i));
				vo.put("incluido", forma.getServicios("incluido_"+i));
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
	private boolean accionGuardarProgramas(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		if (forma.getExcepcionCobConvenio().getPrograma()>0)
		{
			forma.getExcepcionCobConvenio().setCodigoExcepcion(Utilidades.convertirADouble(forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"") );
			forma.getExcepcionCobConvenio().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			forma.getExcepcionCobConvenio().setUsuarioModifica(usuario.getLoginUsuario());
			forma.getExcepcionCobConvenio().setHoraModifica(UtilidadFecha.getHoraActual());
			transaccion=mundo.insertarProgExcepcionConvenio(con,forma.getExcepcionCobConvenio());
			
			if (transaccion)
				forma.resetExcepcion();
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
	private boolean accionGuardarAgrupacionServicios(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
//		eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionServicios(con,forma.getAgrupacionServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesAgrupacionServicio);
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
				vo.put("incluido", forma.getAgrupacionServicios("incluido_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionServicios(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("contrato", forma.getContrato());
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("tipopossubsidiado", forma.getAgrupacionServicios("tipopossubsidiado_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("cantidad", forma.getAgrupacionServicios("cantidad_"+i));
				vo.put("requiereautorizacion", forma.getAgrupacionServicios("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getAgrupacionServicios("semanasmincotizacion_"+i));
				vo.put("incluido", forma.getAgrupacionServicios("incluido_"+i));
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
	private boolean accionGuardarArticulos(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar  
		for(int i=0;i<Integer.parseInt(forma.getArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarArticulos(con,forma.getArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesArticulos);
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
				vo.put("incluido", forma.getArticulos("incluido_"+i));
				vo.put("presfactura", forma.getArticulos("presfactura_"+i));
				transaccion=mundo.modificarArticulos(con, vo);
			}
			//insertar
			else if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("cantidad", forma.getArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("requiereautorizacion", forma.getArticulos("requiereautorizacion_"+i));
				vo.put("semanasmincotizacion", forma.getArticulos("semanasmincotizacion_"+i));
				vo.put("incluido", forma.getArticulos("incluido_"+i));
				vo.put("presfactura", forma.getArticulos("presfactura_"+i));
				transaccion=mundo.insertarArticulos(con, vo);
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
	private boolean accionGuardarAgrupacionArticulos(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionArticulos(con,forma.getAgrupacionArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesAgrupacionArticulos);
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
				vo.put("presfactura", forma.getAgrupacionArticulos("presfactura_"+i));
				vo.put("incluido", forma.getAgrupacionArticulos("incluido_"+i));
				vo.put("cantidad", forma.getAgrupacionArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionArticulos(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("contrato", forma.getContrato());
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("presfactura", forma.getAgrupacionArticulos("presfactura_"+i));
				vo.put("incluido", forma.getAgrupacionArticulos("incluido_"+i));
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
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleExcepcion(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.resetMapasEliminacion();
		
		
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		//Agregado por Anexo 945
		forma.getExcepcionCobConvenio().setCodigoExcepcion(Utilidades.convertirADouble(forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setListadoExcepcionCobConvenio(mundo.consultaProgExcepcionConvenio(con, forma.getExcepcionCobConvenio()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
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
	private ActionForward accionGuardarCobertura(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//Aguardo los logs tarea 135730 tanto para excepciones y coebrturas
		for (int i=0; i<Utilidades.convertirAEntero(forma.getCoberturas().get("numRegistros")+"");i++)
			Utilidades.generarLogGenerico(forma.getCoberturas(), forma.getCoberturasOriginales(), usuario.getLoginUsuario(), false, i,ConstantesBD.logCoberturaConvenioCodigo,indicesCobertura);
		
		
		for (int i=0; i<Utilidades.convertirAEntero(forma.getExcepciones().get("numRegistros")+"");i++)
			Utilidades.generarLogGenerico(forma.getExcepciones(), forma.getExcepcionesOriginales(), usuario.getLoginUsuario(), false, i,ConstantesBD.logCoberturaConvenioCodigo,indicesExcepciones);
		
		
		if(transaccion)
			transaccion=this.accionGuardarCoberturas(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarExcepciones(con,forma,mundo,usuario,transaccion);
		if(transaccion)
		{
			forma.setMensaje("Proceso Realizado con Éxito!");
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje("Problemas en el Proceso");
			UtilidadBD.abortarTransaccion(con);
		}	
		return this.accionCargarInfo(con,forma,mundo,usuario,mapping,false);
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
	private boolean accionGuardarExcepciones(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getExcepcionesEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminareExepciones(con,forma.getExcepcionesEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getExcepcionesEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesExcepciones);
				transaccion=true;
			}
		}
		logger.info("MAPA EXCEPCIONES=> "+forma.getExcepciones());
		for(int i=0;i<Integer.parseInt(forma.getExcepciones("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getExcepciones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getExcepciones(),mundo.consultarExcepcionLLave(con, forma.getExcepciones("codigo_"+i)+""),i,usuario,indicesExcepciones))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getExcepciones("codigo_"+i)+"");
				vo.put("viaIngreso", forma.getExcepciones("viaingreso_"+i));
				vo.put("tipoPaciente", forma.getExcepciones("tipopaciente_"+i));
				vo.put("natPaciente", forma.getExcepciones("natpaciente_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarExcepcion(con, vo);
			}
			//insertar
			else if((forma.getExcepciones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getExcepciones("codigo_"+i)+"");
				vo.put("codigoContrato", forma.getContrato());
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("viaIngreso", forma.getExcepciones("viaingreso_"+i));
				vo.put("tipoPaciente", forma.getExcepciones("tipopaciente_"+i));
				vo.put("natPaciente", forma.getExcepciones("natpaciente_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarExcepcion(con, vo);
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
	private boolean accionGuardarCoberturas(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getCoberturasEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarCobertura(con,forma.getCoberturasEliminados().get("codigocobertura_"+i)+"",forma.getContrato(),usuario.getCodigoInstitucionInt()))
			{
				Utilidades.generarLogGenerico(forma.getCoberturasEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesCobertura);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getCoberturas("numRegistros")+"");i++)
		{
			//	modificar
			HashMap mapaTemp=mundo.consultarCoberturaLLave(con, forma.getCoberturas("codigocobertura_"+i)+"",forma.getContrato(),usuario.getCodigoInstitucionInt());
			
			if((forma.getCoberturas("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getCoberturas(),mapaTemp,i,usuario,indicesCobertura))
			{
				HashMap vo=new HashMap();
				vo.put("codigoCobertura", forma.getCoberturas("codigocobertura_"+i));
				vo.put("prioridad", forma.getCoberturas("prioridad_"+i)); 
				vo.put("codigoContrato", forma.getContrato()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarCobertura(con, vo);
			}
			if((forma.getCoberturas("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoCobertura", forma.getCoberturas("codigocobertura_"+i));
				vo.put("prioridad", forma.getCoberturas("prioridad_"+i)); 
				vo.put("codigoContrato", forma.getContrato()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarCobertura(con, vo);
			}
		}
		return transaccion;
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(CoberturasConvenioForm forma)
	{
		int numReg=Integer.parseInt(forma.getExcepciones("numRegistros")+"");
		forma.setExcepciones(Listado.ordenarMapa(indicesExcepciones,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getExcepciones(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setExcepciones("numRegistros",numReg+"");
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
	private ActionForward accionCargarInfo(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario, ActionMapping mapping, Boolean borrarMensaje)
	{	
		if(borrarMensaje)
			forma.setMensaje("");
		
		//incializar la informacion de los select
		//Se agregan cambios segun anexo 945 para cuando el tipo de atencion de convenio es odontologico o general
		if (forma.getTipoAtencionConvenio().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico))
		{
			forma.resetSelects();
			forma.setSelCoberturas(Utilidades.obtenerListadoCoberturasXTipo(con,usuario.getCodigoInstitucionInt(),ConstantesIntegridadDominio.acronimoTipoCoberturaOdontologico));
			InfoDatosString viaIngOdon=new InfoDatosString();
			InfoDatosString tipoPaciente=new InfoDatosString();
			//Agrego consulta Externa como unica opcion
			viaIngOdon.setCodigo(ConstantesBD.codigoViaIngresoConsultaExterna+"");
			viaIngOdon.setNombre("Consulta Externa");
			forma.getSelViasIngreso().add(viaIngOdon);
			//Fin de consulta externa
			
		}
		else if(forma.getTipoAtencionConvenio().equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioMedicoGeneral))
		{
			forma.resetSelects();
			forma.setSelCoberturas(Utilidades.obtenerListadoCoberturasInstitucion(con,usuario.getCodigoInstitucionInt()));
			forma.setSelViasIngreso((Vector<InfoDatosString>)Utilidades.obtenerViasIngreso(con,true));
			forma.setSelNaturalezaPaciente(Utilidades.obtenerNaturalezasPaciente(con,"",ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
		}
		
		
		forma.setCoberturas(new HashMap());
		forma.setCoberturas(mundo.obtenerListadoCoberturas(con,usuario.getCodigoInstitucionInt(),forma.getContrato()));
		forma.setExcepciones(new HashMap());
		forma.setExcepciones(mundo.obtenerListadoExcepciones(con,usuario.getCodigoInstitucionInt(),forma.getContrato()));

		for(int i=0;i<Integer.parseInt(forma.getExcepciones("numRegistros").toString());i++)
			forma.setExcepciones("tipospaciente_"+i, UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getExcepciones("viaingreso_"+i).toString()));
		
		//Tarea 135730 --Busco las excepciones originales y coberturas originales (cargados)
		forma.setCoberturasOriginales(forma.getCoberturas());
		forma.setExcepcionesOriginales(forma.getExcepciones());
		
		
		forma.setCoberturasEliminados(new HashMap());
		forma.getCoberturasEliminados().put("numRegistros", "0");
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
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logCoberturaConvenioCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private void accionCargarDetallePrograma(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario)
	{
		double codigoPrograma=forma.getListadoExcepcionCobConvenio().get(forma.getPosExcepcion()).getPrograma();
		String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		forma.setListadoDetalleProgramas(ProgramasOdontologicos.cargarDetallePrograma(codigoPrograma, tmpBusquedaServicios));
	}
	
	private void accionEliminarExcepcionProgramas(Connection con, CoberturasConvenioForm forma, CoberturasConvenio mundo, UsuarioBasico usuario)
	{
		double codigoExepcionAEliminar=forma.getListadoExcepcionCobConvenio().get(forma.getPosExcepcion()).getCodigo();
		mundo.eliminarProgExcepcionConvenio(con,codigoExepcionAEliminar);
		forma.setListadoExcepcionCobConvenio(mundo.consultaProgExcepcionConvenio(con,forma.getExcepcionCobConvenio()));
		forma.resetExcepcion();
	}
}
