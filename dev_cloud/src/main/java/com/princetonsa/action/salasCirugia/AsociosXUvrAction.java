package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import com.princetonsa.actionform.salasCirugia.AsociosXUvrForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.AsociosXUvr;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class AsociosXUvrAction extends Action {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger=Logger.getLogger(AsociosXUvrAction.class);
	
	/**
	 * Indices del mapa del maestro por tipo sala
	 */
	String[] indicesTipoSala={"codigoasociouvrtiposala_","codigotipoasocio_","codigotiposala_","codigoasociouvr_","estabd_"};

	/**
	 * Indices del detalle de cada registro
	 */
	String[] indicesDetalle={"codigodetalle_","codigoasocio_","codigotiposervicio_","codigotipoanestesia_","codigoocupacion_","codigoespecialidad_","tipoespecialista_","rango1_","rango2_","codigotipoliquidacion_","valor_","estabd_"};
	
	/**
	 * Indices del mapa de las vigencias por convenio
	 */
	String[] indicesVigencias={"codigoasociouvr_","fechainicial_","fechafinal_","estabd_"};
	
	/**
	 * Metodo excecute del action
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if(form instanceof AsociosXUvrForm)
		{
			AsociosXUvrForm forma=(AsociosXUvrForm)form;
			String estado=forma.getEstado();
			logger.info("Estado-->> "+estado);
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			AsociosXUvr mundo=new AsociosXUvr();
			forma.setMensaje(new ResultadoBoolean(false));
			if(estado==null)
			{
				logger.warn("Estado no valido dentro del flujo de Asocios X Uvr (null)");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("continuar"))
			{
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getAsociosUvrDetalleMap().get("numRegistros").toString()), response, request, "asociosXUvr.jsp",true);
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				
				//UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getCensoCamasMap("numRegistros")+""), response, request, "censoCamas.jsp", false);
				/*if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
					forma.setLinkSiguiente("../asociosXUvr/asociosXUvr.do?estado=continuar");
				response.sendRedirect(forma.getLinkSiguiente());*/
				//response.sendRedirect(forma.getLinkSiguiente());
				//return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getAsociosUvrDetalleMap().get("numRegistros")+""), response, request, "asociosXUvr.jsp",true);
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				return this.accionEmpezar(con,forma,mundo,mapping);
			}
			else if(estado.equals("filtrarXAsocio"))
			{
				return this.accionFiltrarXAsocio(con,forma,mundo,mapping,usuario);
			}
			else if(estado.equals("vigenciasXconvenio"))
			{
				return this.accionVigenciasXConvenio(con,forma,mundo,mapping,usuario,request,response);
			}
			else if(estado.equals("listarDetalle"))
			{
				return this.accionListarDetalle(con,forma,mundo,mapping,usuario);
			}
			else if(estado.equals("guardarXTipoAsocio"))
			{
				return this.accionGuardarXTipoAsocio(con,forma,mundo,mapping,usuario);
			}
			else if(estado.equals("guardarVigencias"))
			{
				return this.accionGuardarVigencias(con,forma,mundo,mapping,usuario);
			}
			else if(estado.equals("guardarTipoAsocioXConvenio"))
			{
				return this.accionGuardarTipoAsocioXConvenio(con,forma,mundo,mapping,usuario);
			}
			else if(estado.equals("guardarDetalle"))
			{
				return this.accionGuardarDetalle(con,forma,mundo,mapping,usuario);
			}
			else if(estado.equals("nuevoTipoAsocio"))
			{
				return this.accionNuevoTipoAsocio(con,forma,mundo,mapping,usuario,request,response);
			}
			else if(estado.equals("nuevoDetalle"))
			{
				return this.accionNuevoDetalle(con, forma, mundo, mapping, usuario, request, response);
			}
			else if(estado.equals("eliminarDetalle"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getAsociosUvrDetalleMap(), forma.getAsociosUvrDetalleEliminadosMap(), forma.getIndiceEliminado(), indicesDetalle, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap("numRegistros")+""), response, request, "asociosXUvr.jsp", forma.getIndiceEliminado()==(Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap("numRegistros")+"")));
			}
			else if(estado.equals("eliminarTipoAsocio"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getAsociosUvrTipoSalaMap(), forma.getAsociosUvrTipoSalaEliminadosMap(), forma.getIndiceEliminado(), indicesTipoSala, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//modificacion por tarea 999
				if ((forma.getAsociosUvrTipoSalaMap("estabd_"+forma.getIndiceEliminado())+"").equals(ConstantesBD.acronimoSi))
					Utilidades.eliminarRegistroMapaGenerico(forma.getAsociosUvrTipoSalaMapClone(), new HashMap(), forma.getIndiceEliminado(), indicesTipoSala, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap("numRegistros")+""), response, request, "listadoXAsocio.jsp", forma.getIndiceEliminado()==(Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap("numRegistros")+"")));
			}
			else if(estado.equals("nuevaVigencia"))
			{
				return this.accionNuevaVigencia(con,forma,mundo,mapping,usuario,request,response);
			}
			else if(estado.equals("eliminarVigencia"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getVigenciasConvenioMap(), forma.getVigenciasConvenioEliminadosMap(), forma.getIndiceEliminado(), indicesVigencias, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Utilidades.convertirAEntero(forma.getVigenciasConvenioMap("numRegistros")+""), response, request, "listadoVigencias.jsp", forma.getIndiceEliminado()==(Utilidades.convertirAEntero(forma.getVigenciasConvenioMap("numRegistros")+"")));
			}
			else if(estado.equals("empezarListarConsultar"))
			{
				forma.reset();
				forma.setEsConsulta(ConstantesBD.acronimoSi);
				return this.accionEmpezar(con,forma,mundo,mapping);
				
			}
			else if(estado.equals("busquedaAvanzada"))
			{
				return this.accionBusquedaAvanzada(con,forma,mundo,mapping,usuario);
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Tipos de Moneda");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de Articulo de Catalogo");
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
	 * Accion empezar, cuando se ingresa a la funcionalidad
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping) 
	{
		forma.setEsquemasTatifarios(Utilidades.obtenerEsquemastarifariosIss(con));
		forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false));
		return mapping.findForward("principal");
	}
	
	/**
	 * Accion cuando se va a escoger el tipo de asocio y el tipo de sala
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrarXAsocio(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		logger.info("\n entro a accionFiltrarXAsocio "+forma.getIndexSeleccionado() +"codigoAsocioXUvr -- > "+forma.getVigenciasConvenioMap() );
		
		forma.setTiposAsocio(Utilidades.obtenerTiposAsocio(con));
		forma.setTiposSala(Utilidades.obtenerTiposSala(con, usuario.getCodigoInstitucionInt(), "true", "false"));
		//////////////////////////////////////////////////////////////////
		//modificado por tarea 35172
		int codigoAsocioXUvr = Utilidades.convertirAEntero(forma.getVigenciasConvenioMap("codigoasociouvr_"+forma.getIndexSeleccionado())+"");
		forma.setAsociosUvrTipoSalaMap(mundo.consultarAsociosXUvrTipoSala(con, forma.getEsquemaTarifario(), forma.getConvenio(),codigoAsocioXUvr));
		///////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////
		//modificado por tarea 1278
		forma.setAsociosUvrTipoSalaMapClone((HashMap)forma.getAsociosUvrTipoSalaMap().clone());
		
		///////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////
		/// se llenan los tipos de liquidacion
		///////////////////////////////////////////////////////////////////////////
		ArrayList<HashMap<String, Object>> datos = new ArrayList<HashMap<String,Object>>();
		HashMap tmp = new HashMap ();
		
			
			//valor
			tmp = new HashMap ();
			tmp.put("codigo", ConstantesBD.codigoTipoLiquidacionSoatUnidades);
			tmp.put("nombre", "Unidades");
			datos.add(tmp);
			
			
			//valor
			tmp = new HashMap ();
			tmp.put("codigo", ConstantesBD.codigoTipoLiquidacionSoatValor);
			tmp.put("nombre", "Valor");	
			datos.add(tmp);
			
			//unidades
			tmp = new HashMap ();
			tmp.put("codigo", ConstantesBD.codigoTipoLiquidacionValorUnidades);
			tmp.put("nombre", "Valor Unidades");	
			datos.add(tmp);
			
			forma.setTiposLiq(datos);
		///////////////////////////////////////////////////////////////////////////////////////////////////
			
		
		return mapping.findForward("listarXAsocio");
	}

	/**
	 * Accion cuando se entra por convenio para escoger la vigencia
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVigenciasXConvenio(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping,UsuarioBasico usuario,HttpServletRequest request,HttpServletResponse response) 
	{
		//forma.reset_pager();
		//forma.setMaxPageItems(Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		forma.setVigenciasConvenioMap(mundo.consultarVigenciasXConvenio(con, forma.getConvenio()));
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getVigenciasConvenioMap().get("numRegistros").toString()), response, request, "listadoVigencias.jsp",false);
		//return mapping.findForward("listadoVigencias");
	}

	/**
	 * Accion que muestra el detalle de la parametrizacion sea por convenio o por esquema tarfario
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionListarDetalle(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String acronimosTipoLiquidacion="'U','V'";
		String acronimosTipoServicio="'"+ConstantesBD.codigoServicioPartosCesarea+"','"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioNoCruentos+"'";
		forma.setTipoAsocio(Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap("codigoasociouvrtiposala_"+forma.getIndex())+""));
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//modificacion por tarea 1420
		//forma.setNombreTipoAsocio(forma.getAsociosUvrTipoSalaMap("nombretipoasocio_"+forma.getIndex())+"");
		if (UtilidadCadena.noEsVacio(forma.getAsociosUvrTipoSalaMap("codigotipoasocio_"+forma.getIndex())+"") && !(forma.getAsociosUvrTipoSalaMap("codigotipoasocio_"+forma.getIndex())+"").equals(ConstantesBD.codigoNuncaValido+""))
			forma.setNombreTipoAsocio(Utilidades.obtenerNombreTipoAsocio(con, Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap("codigotipoasocio_"+forma.getIndex())+""))[0]);
		else
			forma.setNombreTipoAsocio("");
		
		if (UtilidadCadena.noEsVacio(forma.getAsociosUvrTipoSalaMap("codigotiposala_"+forma.getIndex())+"") && !(forma.getAsociosUvrTipoSalaMap("codigotiposala_"+forma.getIndex())+"").equals(ConstantesBD.codigoNuncaValido+""))
			forma.setNombreTipoSala(Utilidades.obtenerNombreTipoSala(con, Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap("codigotiposala_"+forma.getIndex())+"")));
		else
			forma.setNombreTipoSala("");
		//forma.setNombreTipoSala(forma.getAsociosUvrTipoSalaMap("nombretiposala_"+forma.getIndex())+"");
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		forma.setTarifariosOficiales(Utilidades.obtenerTarifariosOficiales(con, ""));
		forma.setTiposServicio(UtilidadesFacturacion.obtenerTiposServicio(con, acronimosTipoServicio, "t"));
		forma.setAsociosUvrDetalleMap(mundo.consultarDetalleAsociosXUvr(con, forma.getCodigoAsocioUvr(), forma.getTipoAsocio(), forma.getEsquemaTarifario(), forma.getConvenio(),forma.getTipoServicio(),forma.getTipoAnestesia(),forma.getOcupacion(),forma.getEspecialidad(),forma.getTipoEspecialista(),forma.getTipoLiquidacion()));
		forma.setOcupacionesMedicas(Utilidades.obtenerOcupacionesMedicas(con));
		forma.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
		forma.setTiposLiquidacion(UtilidadesFacturacion.obtenerTiposLiquidacion(con, acronimosTipoLiquidacion));
		forma.setEspecialidades(Utilidades.obtenerEspecialidadesEnArray(con,ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
		
		
		
		///********************************* ADICIONADO PARA CAPTURAR LA CANTIDAD DEL ESQUEMA SELECCIONADO
		forma.setCantidadesquematarifario(forma.getAsociosUvrTipoSalaMap("cantidadesquematarifario_"+forma.getIndex())+"");
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		//adicionado el 27/08/2008 por tarea 36141
		
		int numRegDet = Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap("numRegistros")+"");
		
		for (int i=0;i<numRegDet;i++)
		{
			if (!UtilidadCadena.noEsVacio(forma.getAsociosUvrDetalleMap("valorunidades_"+i)+""))
				if (Utilidades.convertirADouble(forma.getCantidadesquematarifario())>0)
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, forma.getCantidadesquematarifario());
				else
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, 0);
		}
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		return mapping.findForward("principal");
	}

	/**
	 * Accion guardar que guarda cuando se entra por esquema tarifario
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarXTipoAsocio(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		logger.info("\n entre a accionGuardarXTipoAsocio");
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int numReg=Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap().get("numRegistros")+"");
		int codigoAsocioXUvr = Utilidades.convertirAEntero(forma.getVigenciasConvenioMap("codigoasociouvr_"+forma.getIndexSeleccionado())+"");
		//eliminar
		for(int i=0;i<Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaEliminadosMap().get("numRegistros")+"");i++)
		{
			logger.info("\n entre a eliminar");
			transaccion=mundo.eliminarAsocioXUvrTipoAsocio(con, Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaEliminadosMap().get("codigoasociouvrtiposala_"+i)+""));
		}
		//insertar
		for(int i=0;i<numReg;i++)
		{
			//modificar
			if(forma.getAsociosUvrTipoSalaMap().get("estabd_"+i).equals(ConstantesBD.acronimoSi) && transaccion)
			{
				logger.info("\n verifica  modificacion");
				////////////////////////////////////////////////////////////////
				//adicionado por tarea 1278
				logger.info("\n original"+forma.getAsociosUvrTipoSalaMap("codigotipoasocio_"+i)+" colone "+forma.getAsociosUvrTipoSalaMapClone("codigotipoasocio_"+i));
				//logger.info("\n mapa original "+forma.getAsociosUvrTipoSalaMap());
				//logger.info("\n mapa clone "+forma.getAsociosUvrTipoSalaMapClone());
				
				if (transaccion && !((forma.getAsociosUvrTipoSalaMap("codigotipoasocio_"+i)+"").equals(forma.getAsociosUvrTipoSalaMapClone("codigotipoasocio_"+i)+"")) || 
					!((forma.getAsociosUvrTipoSalaMap("codigotiposala_"+i)+"").equals(forma.getAsociosUvrTipoSalaMapClone("codigotiposala_"+i)+"")) ||
					!((forma.getAsociosUvrTipoSalaMap("liquidarpor_"+i)+"").equals(forma.getAsociosUvrTipoSalaMapClone("liquidarpor_"+i)+"")))
				{
					logger.info("\n entre a modificar");
					transaccion=mundo.modificarAsociosUvrSala(con, forma.getAsociosUvrTipoSalaMap("codigotipoasocio_"+i)+"", forma.getAsociosUvrTipoSalaMap("codigotiposala_"+i)+"", forma.getAsociosUvrTipoSalaMap("codigoasociouvrtiposala_"+i)+"",forma.getAsociosUvrTipoSalaMap("liquidarpor_"+i)+"");
					logger.info("\n sali a modificar");
				}
				////////////////////////////////////////////////////////////////
			}
			else
				if(forma.getAsociosUvrTipoSalaMap().get("estabd_"+i).equals(ConstantesBD.acronimoNo) && transaccion)
				{
					logger.info("\n entre a insertar");
					HashMap<Object, Object> vo=new HashMap<Object, Object>();
					if(forma.getEsquemaTarifario()==ConstantesBD.codigoEsqTarifarioGeneralTodosIss)
						vo.put("esquemageneral", forma.getEsquemaTarifario());
					else
						vo.put("esquemaparticular", forma.getEsquemaTarifario());
					vo.put("convenio", forma.getConvenio());
					vo.put("institucion", usuario.getCodigoInstitucionInt());
					vo.put("usuariomodifica", usuario.getLoginUsuario());
					vo.put("codigotipoasocio", forma.getAsociosUvrTipoSalaMap().get("codigotipoasocio_"+i));
					vo.put("codigotiposala", forma.getAsociosUvrTipoSalaMap().get("codigotiposala_"+i));
					vo.put("liquidarpor", forma.getAsociosUvrTipoSalaMap().get("liquidarpor_"+i));	
					
					if (codigoAsocioXUvr<0)
						transaccion=mundo.insertarAsociosUvrMaestro(con, vo,true);
					else
						transaccion=mundo.insertarAsociosXUvrXTipoAsocio(con, vo, codigoAsocioXUvr);
				}
			
				
					
		}
		
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/// modificado por tarea 35172
		forma.setAsociosUvrTipoSalaMap(mundo.consultarAsociosXUvrTipoSala(con, forma.getEsquemaTarifario(), forma.getConvenio(),codigoAsocioXUvr));
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		forma.setAsociosUvrTipoSalaMapClone((HashMap)forma.getAsociosUvrTipoSalaMap().clone());
		forma.reset_eliminados();
		return mapping.findForward("listarXAsocio");
	}
	
	
	
	
	private ActionForward accionGuardarVigencias(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int numReg=Utilidades.convertirAEntero(forma.getVigenciasConvenioMap().get("numRegistros")+"");
		for(int i=0;i<Utilidades.convertirAEntero(forma.getVigenciasConvenioEliminadosMap().get("numRegistros")+"");i++)
		{
			transaccion=mundo.eliminarAsocioUvrXConvenio(con, Utilidades.convertirAEntero(forma.getVigenciasConvenioEliminadosMap().get("codigoasociouvr_"+i)+""));
		}
		for(int i=0;i<numReg;i++)
		{
			if(forma.getVigenciasConvenioMap().get("estabd_"+i).equals(ConstantesBD.acronimoNo))
			{
				HashMap<Object, Object> vo=new HashMap<Object, Object>();
				vo.put("fechainicial", forma.getVigenciasConvenioMap().get("fechainicial_"+i));
				vo.put("fechafinal", forma.getVigenciasConvenioMap().get("fechafinal_"+i));
				vo.put("convenio", forma.getConvenio());
				vo.put("institucion", usuario.getCodigoInstitucionInt());
				vo.put("usuariomodifica", usuario.getLoginUsuario());
				/*vo.put("codigotipoasocio", forma.getTipoAsocio());
				vo.put("codigotiposala", forma.getTipoSala());*/
				transaccion=mundo.insertarAsociosUvrMaestro(con, vo,false);
			}
			else if(forma.getVigenciasConvenioMap("estabd_"+i).equals(ConstantesBD.acronimoSi))
			{
				HashMap<Object, Object> vo=new HashMap<Object, Object>();
				vo.put("fechainicial", forma.getVigenciasConvenioMap().get("fechainicial_"+i));
				vo.put("fechafinal", forma.getVigenciasConvenioMap().get("fechafinal_"+i));
				vo.put("codigoasociouvr", forma.getVigenciasConvenioMap().get("codigoasociouvr_"+i));
				transaccion=mundo.modificarVigencias(con, vo);
			}
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		forma.setVigenciasConvenioMap(mundo.consultarVigenciasXConvenio(con, forma.getConvenio()));
		return mapping.findForward("listadoVigencias");
	}
	
	private ActionForward accionGuardarTipoAsocioXConvenio(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int numReg=Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap().get("numRegistros")+"");
		for(int i=0;i<numReg;i++)
		{
			if(forma.getAsociosUvrTipoSalaMap().get("estabd_"+i).equals(ConstantesBD.acronimoNo))
			{
				HashMap<Object, Object> vo=new HashMap<Object, Object>();
				vo.put("codigotipoasocio", forma.getAsociosUvrTipoSalaMap().get("codigotipoasocio_"+i));
				vo.put("codigotiposala", forma.getAsociosUvrTipoSalaMap().get("codigotiposala_"+i));
				vo.put("usuariomodifica", usuario.getLoginUsuario());
				transaccion=mundo.insertarAsociosXUvrXTipoAsocio(con, vo, forma.getCodigoAsocioUvr());
			}
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		return mapping.findForward("listarXAsocio");
	}
	
	private ActionForward accionGuardarDetalle(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int codigoDetalle=ConstantesBD.codigoNuncaValido;
		int numReg=Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap().get("numRegistros")+"");
		//eliminar
		for(int i=0;i<Utilidades.convertirAEntero(forma.getAsociosUvrDetalleEliminadosMap().get("numRegistros")+"");i++)
		{
			transaccion=mundo.eliminarDetalle(con, Utilidades.convertirAEntero(forma.getAsociosUvrDetalleEliminadosMap().get("codigodetalle_"+i)+""));
		}
		for(int i=0;i<numReg;i++)
		{
			//insertar
			if(forma.getAsociosUvrDetalleMap().get("estabd_"+i).equals(ConstantesBD.acronimoNo))
			{
				HashMap<Object, Object> vo=new HashMap<Object, Object>();
				vo.put("codigoasociouvrtiposala", forma.getTipoAsocio());
				vo.put("codigotiposervicio", forma.getAsociosUvrDetalleMap("codigotiposervicio_"+i));
				vo.put("codigotipoanestesia", forma.getAsociosUvrDetalleMap("codigotipoanestesia_"+i));
				vo.put("codigoocupacion", forma.getAsociosUvrDetalleMap("codigoocupacion_"+i));
				vo.put("codigoespecialidad", forma.getAsociosUvrDetalleMap("codigoespecialidad_"+i));
				vo.put("codigotipoespecialista", forma.getAsociosUvrDetalleMap("codigotipoespecialista_"+i));
				vo.put("rango1", forma.getAsociosUvrDetalleMap().get("rango1_"+i));
				vo.put("rango2", forma.getAsociosUvrDetalleMap().get("rango2_"+i));
				
				if ((forma.getAsociosUvrDetalleMap("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionValorUnidades+""))
					vo.put("codigotipoliquidacion", ConstantesBD.codigoTipoLiquidacionSoatValor);
				else
					vo.put("codigotipoliquidacion", forma.getAsociosUvrDetalleMap("codigotipoliquidacion_"+i));
				
				vo.put("valor", forma.getAsociosUvrDetalleMap().get("valor_"+i));
				vo.put("usuariomodifica", usuario.getLoginUsuario());
				vo.put("unidades", forma.getAsociosUvrDetalleMap().get("unidades_"+i));
				vo.put("valorunidades", forma.getAsociosUvrDetalleMap().get("valorunidades_"+i));
				
				codigoDetalle=mundo.insertarDetalleAsocioXUvr(con, vo);
				if(codigoDetalle>0)
				{
					HashMap<String, Object> mapa=new HashMap<String, Object>();
			        for(int j=0;j<forma.getTarifariosOficiales().size();j++)
			        {
			        	HashMap mapaTemp=(HashMap)forma.getTarifariosOficiales().get(j);
			        	mapa.put("codigodetalleasociouvr", codigoDetalle);
			        	mapa.put("codigotarifario_"+j, mapaTemp.get("codigotarifario"));
			        	mapa.put("valortarifario_"+j, forma.getAsociosUvrDetalleMap("valortarifario_"+i+"_"+mapaTemp.get("codigotarifario")));
			        	mapa.put("estabd_"+i, forma.getAsociosUvrDetalleMap("estabd_"+i+"_"+mapaTemp.get("codigotarifario")));
			        }
			        mapa.put("numRegistros", forma.getTarifariosOficiales().size()+"");
			        transaccion=mundo.actualizarDetCodigosAsociosXUvr(con, mapa);
				}
			}
			//modificar
			else if(forma.getAsociosUvrDetalleMap().get("estabd_"+i).equals(ConstantesBD.acronimoSi))
			{
				HashMap<Object, Object> vo=new HashMap<Object, Object>();
				vo.put("codigotiposervicio", forma.getAsociosUvrDetalleMap("codigotiposervicio_"+i));
				vo.put("codigotipoanestesia", forma.getAsociosUvrDetalleMap("codigotipoanestesia_"+i));
				vo.put("codigoocupacion", forma.getAsociosUvrDetalleMap("codigoocupacion_"+i));
				vo.put("codigoespecialidad", forma.getAsociosUvrDetalleMap("codigoespecialidad_"+i));
				vo.put("codigotipoespecialista", forma.getAsociosUvrDetalleMap("codigotipoespecialista_"+i));
				vo.put("rango1", forma.getAsociosUvrDetalleMap().get("rango1_"+i));
				vo.put("rango2", forma.getAsociosUvrDetalleMap().get("rango2_"+i));
				logger.info("\n entre a modificar "+forma.getAsociosUvrDetalleMap());
				
				if ((forma.getAsociosUvrDetalleMap("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionSoatUnidades+""))
				{
					logger.info("\n entre  1");
						vo.put("codigotipoliquidacion", ConstantesBD.codigoTipoLiquidacionSoatUnidades);
						vo.put("valor", forma.getAsociosUvrDetalleMap("valor_"+i));
						vo.put("unidades", forma.getAsociosUvrDetalleMap("unidades_"+i));
						vo.put("valorunidades", forma.getAsociosUvrDetalleMap("valorunidades_"+i));
				}
				else
					if ((forma.getAsociosUvrDetalleMap("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionSoatValor+""))
					{
						logger.info("\n\n\n\n ############################## entre  2  i --> "+i);
						vo.put("valor", forma.getAsociosUvrDetalleMap("valor_"+i));
						vo.put("unidades", "");
						vo.put("valorunidades", "");
						
						vo.put("codigotipoliquidacion", ConstantesBD.codigoTipoLiquidacionSoatValor);
					}
					else
						if ((forma.getAsociosUvrDetalleMap("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionValorUnidades +""))
						{
							logger.info("\n entre  2");
							vo.put("valor", "");
							vo.put("unidades", forma.getAsociosUvrDetalleMap("unidades_"+i));
							vo.put("valorunidades", "");
							vo.put("codigotipoliquidacion",  ConstantesBD.codigoTipoLiquidacionSoatValor);
						}
				
				
				vo.put("usuariomodifica", usuario.getLoginUsuario());
				vo.put("codigodetalle", forma.getAsociosUvrDetalleMap("codigodetalle_"+i));
				
				
				transaccion=mundo.modificarDetalle(con, vo);
				HashMap<String, Object> mapa=new HashMap<String, Object>();
		        for(int j=0;j<forma.getTarifariosOficiales().size();j++)
		        {
		        	HashMap mapaTemp=(HashMap)forma.getTarifariosOficiales().get(j);
		        	mapa.put("codigodetalleasociouvr", forma.getAsociosUvrDetalleMap("codigodetalle_"+i));
		        	mapa.put("codigotarifario_"+j, mapaTemp.get("codigotarifario"));
		        	mapa.put("valortarifario_"+j, forma.getAsociosUvrDetalleMap("valortarifario_"+i+"_"+mapaTemp.get("codigotarifario")));
		        	mapa.put("estabd_"+j, forma.getAsociosUvrDetalleMap("estabd_"+i+"_"+mapaTemp.get("codigotarifario")));
		        }
		        mapa.put("numRegistros", forma.getTarifariosOficiales().size()+"");
		        transaccion=mundo.actualizarDetCodigosAsociosXUvr(con, mapa);
			}
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		forma.setAsociosUvrDetalleMap(mundo.consultarDetalleAsociosXUvr(con, forma.getCodigoAsocioUvr(), forma.getTipoAsocio(), forma.getEsquemaTarifario(), forma.getConvenio(),forma.getTipoServicio(),forma.getTipoAnestesia(),forma.getOcupacion(),forma.getEspecialidad(),forma.getTipoEspecialista(),forma.getTipoLiquidacion()));
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		//adicionado el 27/08/2008 por tarea 36141
		
		int numRegDet = Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap("numRegistros")+"");
		
		for (int i=0;i<numRegDet;i++)
		{
			if (!UtilidadCadena.noEsVacio(forma.getAsociosUvrDetalleMap("valorunidades_"+i)+""))
				if (Utilidades.convertirADouble(forma.getCantidadesquematarifario())>0)
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, forma.getCantidadesquematarifario());
				else
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, 0);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		return mapping.findForward("principal");
	}
	
	private ActionForward accionNuevoTipoAsocio(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario,HttpServletRequest request,HttpServletResponse response)throws SQLException 
	{
		int pos=Utilidades.convertirAEntero(forma.getAsociosUvrTipoSalaMap().get("numRegistros")+"");
		forma.setAsociosUvrTipoSalaMap("codigotipoasocio_"+pos,ConstantesBD.codigoNuncaValido);
		forma.setAsociosUvrTipoSalaMap("codigotiposala_"+pos, ConstantesBD.codigoNuncaValido);
		forma.setAsociosUvrTipoSalaMap("liquidarpor_"+pos, "");
		forma.setAsociosUvrTipoSalaMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setAsociosUvrTipoSalaMap("numRegistros", (pos+1));
		UtilidadBD.cerrarConexion(con);
		
		return  UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getAsociosUvrTipoSalaMap().get("numRegistros").toString()), response, request, "listadoXAsocio.jsp",true);
		//return mapping.findForward("listarXAsocio");
	}

	private ActionForward accionNuevoDetalle(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario,HttpServletRequest request, HttpServletResponse response )throws SQLException 
	{
		int pos=Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap().get("numRegistros")+"");
		forma.setAsociosUvrDetalleMap("codigotiposervicio_"+pos,"");
		forma.setAsociosUvrDetalleMap("codigotipoanestesia_"+pos,ConstantesBD.codigoNuncaValido);
		forma.setAsociosUvrDetalleMap("codigoocupacion_"+pos,"");
		forma.setAsociosUvrDetalleMap("codigoespecialidad_"+pos,"");
		forma.setAsociosUvrDetalleMap("codigotipoespecialista_"+pos,"");
		forma.setAsociosUvrDetalleMap("rango1_"+pos,"");
		forma.setAsociosUvrDetalleMap("rango2_"+pos,"");
		forma.setAsociosUvrDetalleMap("codigotipoliquidacion_"+pos,ConstantesBD.codigoNuncaValido);
		forma.setAsociosUvrDetalleMap("valor_"+pos,"");
		forma.setAsociosUvrDetalleMap("estabd_"+pos,ConstantesBD.acronimoNo);
		for(int i=0;i<forma.getTarifariosOficiales().size();i++)
	    {
	    	HashMap mapaTemp=(HashMap)forma.getTarifariosOficiales().get(i);
	    	forma.setAsociosUvrDetalleMap("valortarifario_"+pos+"_"+mapaTemp.get("codigotarifario"),"");
	    	forma.setAsociosUvrDetalleMap("estabd_"+pos+"_"+mapaTemp.get("codigotarifario"),ConstantesBD.acronimoNo);
	    }
		forma.setAsociosUvrDetalleMap("numRegistros", (pos+1));
		UtilidadBD.cerrarConexion(con);
		
//////////////////////////////////////////////////////////////////////////////////////////////
		//adicionado el 27/08/2008 por tarea 36141
		
		int numRegDet = Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap("numRegistros")+"");
		
		for (int i=0;i<numRegDet;i++)
		{
			if (!UtilidadCadena.noEsVacio(forma.getAsociosUvrDetalleMap("valorunidades_"+i)+""))
				if (Utilidades.convertirADouble(forma.getCantidadesquematarifario())>0)
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, forma.getCantidadesquematarifario());
				else
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, 0);
		}
		int maxPageItems=ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt());
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		forma.setOffset(UtilidadSesion.obtenerOffset(maxPageItems, numRegDet, request));
		forma.setCurrentPageNumber(UtilidadSesion.obtenerCurrentPageNumber(maxPageItems, numRegDet));
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),maxPageItems,numRegDet, response, request, "asociosXUvr.jsp",true);
		
		//return mapping.findForward("principal");
	}
	
	private ActionForward accionNuevaVigencia(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario , HttpServletRequest request ,HttpServletResponse response)throws SQLException 
	{
		int pos=Utilidades.convertirAEntero(forma.getVigenciasConvenioMap("numRegistros")+"");
		forma.setVigenciasConvenioMap("fechainicial_"+pos, "");
		forma.setVigenciasConvenioMap("fechafinal_"+pos, "");
		forma.setVigenciasConvenioMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setVigenciasConvenioMap("numRegistros",(pos+1));
		UtilidadBD.cerrarConexion(con);
		
		return 	UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getVigenciasConvenioMap().get("numRegistros").toString()), response, request, "listadoVigencias.jsp",true);
		//return mapping.findForward("listadoVigencias");
	}
	
	private ActionForward accionBusquedaAvanzada(Connection con, AsociosXUvrForm forma, AsociosXUvr mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setAsociosUvrDetalleMap(mundo.consultarDetalleAsociosXUvr(con, forma.getCodigoAsocioUvr(), forma.getTipoAsocio(), forma.getEsquemaTarifario(), forma.getConvenio(),forma.getTipoServicio(),forma.getTipoAnestesia(),forma.getOcupacion(),forma.getEspecialidad(),forma.getTipoEspecialista(),forma.getTipoLiquidacion()));
		
//		//////////////////////////////////////////////////////////////////////////////////////////////
		//adicionado el 27/08/2008 por tarea 36141
		
		int numRegDet = Utilidades.convertirAEntero(forma.getAsociosUvrDetalleMap("numRegistros")+"");
		
		for (int i=0;i<numRegDet;i++)
		{
			if (!UtilidadCadena.noEsVacio(forma.getAsociosUvrDetalleMap("valorunidades_"+i)+""))
				if (Utilidades.convertirADouble(forma.getCantidadesquematarifario())>0)
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, forma.getCantidadesquematarifario());
				else
					forma.setAsociosUvrDetalleMap("valorunidades_"+i, 0);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		return mapping.findForward("principal");
	}

}
