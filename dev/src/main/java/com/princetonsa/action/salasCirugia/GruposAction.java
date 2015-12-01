/*
 * @(#)GruposAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
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
import util.InfoDatosInt;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import com.princetonsa.actionform.salasCirugia.GruposForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.Grupos;

/**
 *   Action, controla todas las opciones dentro de grupos, 
 *   incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Sep 07, 2005
 * @author wrios
 */
public class GruposAction extends Action
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GruposAction.class);

	/**
	 * 
	 */
	private String [] indices={"codigoPKGrupo_","codigoEsquemaTarifario_","nombreEsquemaTarifario_","grupo_","codigoAsocio_","acronimoTipoServicio_","nombreTipoServicio_","descTipoServicio_","codigoTipoLiquidacion_","nombreTipoLiquidacion_","unidades_","valor_","convenio_","tipoServicio_","nomTipoServicio_","estaBD_","esEliminada_","estaSiendoUtilizadaEnOtraFunc_"};
	
	/**
	 * 
	 */
	private String [] indicesVigencias={"fechainicial_","fechafinal_","codigogrupo_","estabd_"};
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof GruposForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				GruposForm forma =(GruposForm)form;
				String estado=forma.getEstado(); 
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				logger.warn("ESTADO GRUPOS==========="+estado);
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de la GRUPOS (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(forma,mapping, con);
				}
				else if (estado.equals("empezarIngresarModificarEliminar"))
				{
					return this.accionEmpezarIngresarModificarEliminar(forma,mapping, usuario.getCodigoInstitucionInt(), con);
				}
				else if(estado.equals("ingresarNuevoElementoMapa"))
				{
					return this.accionIngresarNuevoElementoMapa(forma, mapping, con);
				}
				else if(estado.equals("eliminarElementoMapa"))
				{
					return this.accionEliminarElementoMapa(forma, mapping, con);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(forma,mapping,request,usuario,con);
				}
				else if(estado.equals("listarConsultar"))
				{
					return this.accionListarConsultar(forma,mapping,con,usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("listarConsultarXEsquema"))
				{
					return this.accionListarConsultarXEsquema(forma,mapping,con,usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("listarConsultarXConvenio"))
				{
					return this.accionListarConsultarXConvenio(forma,mapping,con);
				}
				else if(estado.equals("ordenar"))
				{
					return this.accionOrdenar(forma,mapping,request,con);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					return this.accionBusquedaAvanzada(forma, mapping, con, usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("listarVigencias"))
				{
					return this.accionListarVigencias(con,mapping,forma);
				}
				else if(estado.equals("nuevaVigencia"))
				{
					return this.accionNuevaVigencia(con,forma,mapping);
				}
				else if(estado.equals("guardarVigencia"))
				{
					return this.accionGuardarVigencia(con,forma,mapping,usuario);
				}
				else if(estado.equals("eliminarVigencia"))
				{
					UtilidadBD.closeConnection(con);
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaVigencias(), forma.getMapaVigenciasEliminado(), forma.getIndiceEliminado(), indicesVigencias, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Utilidades.convertirAEntero(forma.getMapaVigencias("numRegistros")+""), response, request, "listadoVigencias.jsp", forma.getIndiceEliminado()==(Utilidades.convertirAEntero(forma.getMapaVigencias("numRegistros")+"")));
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de grupos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	

	private ActionForward accionListarConsultarXConvenio(GruposForm forma, ActionMapping mapping, Connection con)throws SQLException 
	{
		Grupos mundo=new Grupos();
		forma.setMapaVigencias(mundo.listarVigenciasConvenio(con, Utilidades.convertirAEntero(forma.getConvenio())));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaConsultaVigencias");
	}


	private ActionForward accionListarConsultarXEsquema(GruposForm forma, ActionMapping mapping, Connection con, int codigoInstitucion) 
	{
		try
		{
			forma.setEstado("listarConsultarXEsquema");
			Grupos mundo= new Grupos();
			
			if(!forma.getCodigoEsquemaTarifario().equals(""))
			{
				forma.setConsecutivoEsquemaTarifario(Integer.parseInt(forma.getCodigoEsquemaTarifario().split(ConstantesBD.separadorSplit )[0]));
				forma.setEsquemaTarifarioGeneral(UtilidadTexto.getBoolean(forma.getCodigoEsquemaTarifario().split(ConstantesBD.separadorSplit)[1]));
			}
			
			mundo.setEsquemaTarifario(new InfoDatosInt(forma.getConsecutivoEsquemaTarifario(), ""));
			forma.setMapaGrupos(mundo.listadoGrupos(con, codigoInstitucion, forma.getConsecutivoEsquemaTarifario(), Utilidades.convertirAEntero(forma.getMapaVigencias("codigogrupo_"+forma.getIndexMapa())+""),forma.isEsquemaTarifarioGeneral()));
			forma.setTarifariosOficiales(mundo.listarTarifariosOficiales(con));
			forma.setTiposServicio(mundo.listarTiposServicio(con));
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//modificacion por tarea 3384
			int numReg=Utilidades.convertirAEntero(forma.getMapaGrupos("numRegistros")+"");
			for (int i=0;i<numReg;i++)
			forma.setMapaGrupos("valor_"+i, UtilidadTexto.formatearValores(forma.getMapaGrupos("valor_"+i)+"", "000.00"));
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapping.findForward("paginaListar");
	}


		/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param forma GruposForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "grupos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	GruposForm forma, 
																ActionMapping mapping,
																Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		forma.resetMapa();
		forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",true));
		forma.setEstado("empezar");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarIngresarModificarEliminar.
	 * 
	 * @param forma GruposForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param int codigoInstitucion
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "grupos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarIngresarModificarEliminar(	GruposForm forma, 
																									ActionMapping mapping,
																									int codigoInstitucion,
																									Connection con) throws SQLException
	{
		forma.setEstado("empezarIngresarModificarEliminar");
		Grupos mundo= new Grupos();		
		
		if(UtilidadCadena.noEsVacio(forma.getCodigoEsquemaTarifario()))
		{
			logger.info("\n esquema tarifario --> "+forma.getCodigoEsquemaTarifario());
			forma.setConsecutivoEsquemaTarifario(Integer.parseInt(forma.getCodigoEsquemaTarifario().split(ConstantesBD.separadorSplit)[0]));
			forma.setEsquemaTarifarioGeneral(UtilidadTexto.getBoolean(forma.getCodigoEsquemaTarifario().split(ConstantesBD.separadorSplit)[1]));
		}
		
		//System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n valor select : " + forma.getCodigoEsquemaTarifario() + "\n\n\n\n\n\n\n");
		mundo.setEsquemaTarifario(new InfoDatosInt(forma.getConsecutivoEsquemaTarifario(), ""));
		mundo.setConvenio(forma.getConvenio());
		
		forma.setMapaGrupos(mundo.listadoGrupos(con, codigoInstitucion, forma.getConsecutivoEsquemaTarifario(), forma.getCodigoPKGrupo(), forma.isEsquemaTarifarioGeneral()));
		
		forma.setTiposServicio(mundo.listarTiposServicio(con));
		forma.setTarifariosOficiales(mundo.listarTarifariosOficiales(con));
	
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//modificacion por tarea 3384
		int numReg=Utilidades.convertirAEntero(forma.getMapaGrupos("numRegistros")+"");
		for (int i=0;i<numReg;i++)
		forma.setMapaGrupos("valor_"+i, UtilidadTexto.formatearValores(forma.getMapaGrupos("valor_"+i)+"", "000.00"));
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * ingresarNuevoElementoMapa.
	 * 
	 * @param GruposForm forma
	 * 				para pre-llenar datos si es necesario
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal 
	 * @throws SQLException
	 */
	private ActionForward accionIngresarNuevoElementoMapa(		GruposForm forma, 
	        																					ActionMapping mapping,
	        																					Connection con
																						  ) throws Exception
	{
		/////////////////////////////////////////////////////////////////////////////////////////////
		//Modificacion por tarea 36648
		//se inicializa la variable de verificacion
		forma.setOperacionTrue(false);
		////////////////////////////////////////////////////////////////////////////////////////////
		
		int pos=Utilidades.convertirAEntero(forma.getMapaGrupos("numRegistros")+"");
	    forma.setMapaGrupos("codigoPKGrupo_"+pos,"-1");
	    forma.setMapaGrupos("codigoEsquemaTarifario_"+pos,"-1");
	    forma.setMapaGrupos("nombreEsquemaTarifario_"+pos,"Seleccione");
	    forma.setMapaGrupos("grupo_"+pos,"");
	    forma.setMapaGrupos("codigoAsocio_"+pos,"-1");
	    forma.setMapaGrupos("acronimoTipoServicio_"+pos,"");
	    forma.setMapaGrupos("nombreTipoServicio_"+pos,"Seleccione");
	    forma.setMapaGrupos("descTipoServicio_"+pos,"");
	    forma.setMapaGrupos("codigoTipoLiquidacion_"+pos,"-1");
	    forma.setMapaGrupos("nombreTipoLiquidacion_"+pos,"Seleccione");
	    forma.setMapaGrupos("unidades_"+pos,"");
	    forma.setMapaGrupos("valor_"+pos,"");
	    forma.setMapaGrupos("estaBD_"+pos,"f");
	    forma.setMapaGrupos("esEliminada_"+pos,"f");
	    forma.setMapaGrupos("estaSiendoUtilizadaEnOtraFunc_"+pos,"f");
	    forma.setMapaGrupos("tipoServicio_"+pos,"");
	    for(int i=0;i<forma.getTarifariosOficiales().size();i++)
	    {
	    	HashMap mapaTemp=(HashMap)forma.getTarifariosOficiales().get(i);
	    	forma.setMapaGrupos("valorTarifario_"+pos+"_"+mapaTemp.get("codigo"),"");
	    	forma.setMapaGrupos("estabd_"+pos+"_"+mapaTemp.get("codigo"),ConstantesBD.acronimoNo+"");
	    }
	    forma.setMapaGrupos("numRegistros",pos+1);
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * eliminarElementoMapa.
	 * 
	 * @param GruposForm forma, 
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "grupos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEliminarElementoMapa(		GruposForm forma, 
	    																				ActionMapping mapping,
																						Connection con
																				) throws Exception
	{
	    forma.setMapaGrupos("esEliminada_"+forma.getIndexMapa(),"t");
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardar.
	 * 
	 * @param GruposForm forma
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward 
	 * @throws SQLException
	 */
	private ActionForward accionGuardar (	GruposForm forma,
																ActionMapping mapping,
																HttpServletRequest request, 
																UsuarioBasico usuario,
																Connection con) throws SQLException
	{
	    
	    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    //Modificacion por tarea 36648
	     // this.accionResumen(forma, mapping, usuario.getCodigoInstitucionInt(), con);
		 forma.setOperacionTrue(actualizarGuardarNuevosXETEliminarViejosBDTransaccional(forma,usuario,con));
	    forma.setEstado("empezarIngresarModificarEliminar");
	    return this.accionEmpezarIngresarModificarEliminar(forma,mapping, usuario.getCodigoInstitucionInt(), con);
	    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    
	}
	
	/**
	 * Método transaccional que comparando el HashMap Original y el modificado,
	 * entonces inserta en BD los que en el hash Map tienen como key del
	 * estaBD=  y esEliminada= los nuevos, y los que han sido eliminados que
	 * estan en BD  estaBD=  y esEliminada=, por otra parte modifica los datos 
	 * pasandolos a un nuevo mapa que contiene solo la info modificada para enviar 
	 * a la BD. 
	 * 
	 * @param GruposForm forma
	 * @param con
	 * @throws SQLException
	 * 
	 * @return true cuando todo salió bien 
	 */
	private boolean actualizarGuardarNuevosXETEliminarViejosBDTransaccional(	GruposForm forma,
	        																										UsuarioBasico usuario,
	        																										Connection con) throws SQLException
	{
		Grupos mundo= new Grupos();
	    mundo.setEsquemaTarifario(new InfoDatosInt(forma.getConsecutivoEsquemaTarifario(), ""));
	    boolean insertados=false;
	    int codigoGrupo=0;
	    boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
	    
	    for(int k=0; k<forma.getNumeroRealFilasMapa(); k++)
	    {
	        /*PRIMERA PARTE PARA LA ELIMINACIÓN DE DATOS QUE ESTAN EN BD*/
	    	if(enTransaccion)
	    	{
		         if(UtilidadTexto.getBoolean(forma.getMapaGrupos("esEliminada_"+k)+""))
		         {
		        	 if(UtilidadTexto.getBoolean(forma.getMapaGrupos("estaBD_"+k)+""))
		             {
		                 insertados=false;
		                 mundo.setCodigoPKGrupo(Integer.parseInt(forma.getMapaGrupos("codigoPKGrupo_"+k)+""));
		                 if(mundo.eliminarCodigosGruposTotal(con,cargarValueObject(forma,Utilidades.convertirAEntero(forma.getMapaGrupos("codigoPKGrupo_"+k)+""),k)));
	                     	insertados=mundo.eliminarGrupoTransaccional(con, ConstantesBD.continuarTransaccion);
	                     if(!insertados)
	                 	     enTransaccion=false;
	                 	else
	                 	{
	                         generarLog(forma, k, usuario, false);
	                         enTransaccion=true;
	                 	}
		             }
		         }
	    	}
	    	else
	    	{
	    		k=forma.getNumeroRealFilasMapa();
	    	}
	    }
	    for(int k=0;k<forma.getNumeroRealFilasMapa();k++)
	    {
	    	if(!UtilidadTexto.getBoolean(forma.getMapaGrupos("esEliminada_"+k)+""))
	         {
	        	if(UtilidadTexto.getBoolean(forma.getMapaGrupos("estaBD_"+k)+""))
	            {
	        		if(existeModificacionRegistro(con,forma,mundo,usuario,k))
	        		{
				         String tempoCodPKGrupo=forma.getMapaGrupos("codigoPKGrupo_"+k)+"";
				         if(tempoCodPKGrupo!=null && !tempoCodPKGrupo.trim().equals("") && !tempoCodPKGrupo.equals("null") && !tempoCodPKGrupo.equals("-1"))
				         { 
					         mundo.setCodigoPKGrupo(Integer.parseInt(forma.getMapaGrupos("codigoPKGrupo_"+k)+""));
					         mundo.setGrupo(Integer.parseInt(forma.getMapaGrupos("grupo_"+k)+""));
					         mundo.setAsocio(new InfoDatosInt(Integer.parseInt(forma.getMapaGrupos("codigoAsocio_"+k)+""), ""));
					         mundo.setEsquemaTarifario(new InfoDatosInt(forma.getConsecutivoEsquemaTarifario(), ""));
					         mundo.setEsquemaGeneral(forma.isEsquemaTarifarioGeneral());
					         mundo.setTipoLiquidacion(new InfoDatosInt(Integer.parseInt(forma.getMapaGrupos("codigoTipoLiquidacion_"+k)+"")));
					         mundo.setUnidades(forma.getMapaGrupos("unidades_"+k)+"");
					         if(mundo.getUnidades().trim().equals(""))
					             mundo.setUnidades("0");
					         mundo.setValor(Double.parseDouble(forma.getMapaGrupos("valor_"+k)+""));
					         mundo.setConvenio(forma.getConvenio());
					         mundo.setTipoServicio(forma.getMapaGrupos("tipoServicio_"+k)+"");
					         mundo.setLiquidarPor(forma.getMapaGrupos("liquidarpor_"+k)+"");
					         insertados=mundo.modificarGrupoTransaccional(con, ConstantesBD.continuarTransaccion);
					         if(!insertados)
					            enTransaccion=false;
					         else
					         {
					        	enTransaccion=true;
					            generarLog(forma, k, usuario, true);
					         }
					         if(enTransaccion)
					         {
						         enTransaccion=mundo.actualizarDetCodigosGrupos(con,cargarValueObject(forma,Utilidades.convertirAEntero(tempoCodPKGrupo),k));
					         }
				         }
	        		}
	        		
	            }
	         }
	    }
	    
	    logger.info("Termine modificaciones");
		for(int k=0; k<forma.getNumeroRealFilasMapa(); k++)
		{      
			if(enTransaccion)
			{
		       /*TERCERA PARTE PARA LA INSERCIÓN DE LOS NUEVOS DATOS*/
		         if(!UtilidadTexto.getBoolean(forma.getMapaGrupos("esEliminada_"+k)+""))
		         {
		        	if(!UtilidadTexto.getBoolean(forma.getMapaGrupos("estaBD_"+k)+""))
		            {
			            insertados=false;
		                mundo.setGrupo(Integer.parseInt(forma.getMapaGrupos("grupo_"+k)+""));
				        mundo.setAsocio(new InfoDatosInt(Integer.parseInt(forma.getMapaGrupos("codigoAsocio_"+k)+""), ""));
				        mundo.setEsquemaTarifario(new InfoDatosInt(forma.getConsecutivoEsquemaTarifario(), ""));
				        mundo.setEsquemaGeneral(forma.isEsquemaTarifarioGeneral());
				        mundo.setTipoLiquidacion(new InfoDatosInt(Integer.parseInt(forma.getMapaGrupos("codigoTipoLiquidacion_"+k)+"")));
				        mundo.setUnidades(forma.getMapaGrupos("unidades_"+k)+"");
				        if(mundo.getUnidades().trim().equals(""))
				            mundo.setUnidades("0");
				        mundo.setValor(Double.parseDouble(forma.getMapaGrupos("valor_"+k)+""));
				        mundo.setConvenio(forma.getConvenio());
				        mundo.setTipoServicio(forma.getMapaGrupos("tipoServicio_"+k)+"");
				        mundo.setLiquidarPor(forma.getMapaGrupos("liquidarpor_"+k)+"");
		                codigoGrupo=mundo.insertarXesquemaTarifario(con,usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), ConstantesBD.continuarTransaccion,forma.getCodigoPKGrupo());
		                if(codigoGrupo==0)
		                	enTransaccion=false;
		                else
		                	enTransaccion=true;
		                 
		                if(enTransaccion)
		                	 enTransaccion=mundo.actualizarDetCodigosGrupos(con,cargarValueObject(forma,codigoGrupo,k));
		             }
		         }
			}
			else
			{
				k=forma.getNumeroRealFilasMapa();
			}
	    }
	    logger.info("Termine insercion ");
	    // SI LA TRANSACCIÓN YA FUÉ INICIADA ENTONCES QUE LA FINALICE
		if(enTransaccion)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
	    return enTransaccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario 
	 * @param mundo 
	 * @param indice
	 * @return
	 */
	private boolean existeModificacionRegistro(Connection con, GruposForm forma, Grupos mundo, UsuarioBasico usuario, int pos) 
	{
		String tempoCodPKGrupo=forma.getMapaGrupos("codigoPKGrupo_"+pos)+"";
		HashMap mapa=(HashMap)forma.getMapaGrupos().clone();
		HashMap mapaTemp=mundo.listadoGruposLLave(con,tempoCodPKGrupo );
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					//generarLog
					return true;
				}
			}
		}
		for(int i=0;i<forma.getTarifariosOficiales().size();i++)
		{
        	HashMap mapaTempTar=(HashMap)forma.getTarifariosOficiales().get(i);
			if(!((mapaTemp.get("valorTarifario_0_"+mapaTempTar.get("codigo"))+"").trim().equals((mapa.get("valorTarifario_"+pos+"_"+mapaTempTar.get("codigo"))+"").trim())))
			{
				//generarLog
				return true;
			}
		}
		return false;
	}

	private HashMap<String,Object> cargarValueObject(GruposForm forma, int codigoGrupo,int indice) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
        for(int i=0;i<forma.getTarifariosOficiales().size();i++)
        {
        	HashMap mapaTemp=(HashMap)forma.getTarifariosOficiales().get(i);
        	mapa.put("codigoPKGrupo", codigoGrupo);
        	mapa.put("codigotarifario_"+i, mapaTemp.get("codigo"));
        	mapa.put("valorTarifario_"+i, forma.getMapaGrupos("valorTarifario_"+indice+"_"+mapaTemp.get("codigo")));
        	mapa.put("estabd_"+i, forma.getMapaGrupos("estabd_"+indice+"_"+mapaTemp.get("codigo")));
        }
        mapa.put("numRegistros", forma.getTarifariosOficiales().size()+"");
		return mapa;
	}

	/**
	 * Método que genera los Logs de Modificación y borrado 
	 * @param forma
	 * @param indexKeyCodigoMapaMod
	 * @param usuario
	 * @param esModificacion
	 */
	private void generarLog(GruposForm forma,  int indexKey, UsuarioBasico usuario, boolean esModificacion)
	{
	    //en construccion
	    String log;
	        
	    /*
	    if(esModificacion)
	    {    
	        log="\n            =========INFORMACION ORIGINAL GRUPOS QUIRURGICOS======== " +
			"\n*  Grupo [" +forma.getMapaGruposNoModificado("grupo_"+indexKey) +"] "+
			"\n*  Código Asocio ["+forma.getMapaGruposNoModificado("codigoAsocio_"+indexKey)+"] " +
			"\n*  Código Esquema Tarifario ["+forma.getCodigoEsquemaTarifario()+"] " +
			"\n*  Código Cups ["+forma.getMapaGruposNoModificado("codigoCups_"+indexKey)+"] " +
			"\n*  Código Soat ["+forma.getMapaGruposNoModificado("codigoSoat_"+indexKey)+"] " +
			"\n*  Código Tipo Liquidación ["+forma.getMapaGruposNoModificado("codigoTipoLiquidacion_"+indexKey)+"] " +
			"\n*  Unidades ["+forma.getMapaGruposNoModificado("unidades_"+indexKey)+"] " +
			"\n*  Valor ["+forma.getMapaGruposNoModificado("valor_"+indexKey)+"] " +
			"\n*  Activo ["+forma.getMapaGruposNoModificado("activo_"+indexKey)+"] " +
			"\n*  Institucion ["+usuario.getCodigoInstitucion()+"] " +
			""  ;
			
		    log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION GRUPOS QUIRURGICOS===== " +
		    "\n*  Grupo [" +forma.getMapaGruposAux("grupo_"+indexKey) +"] "+
			"\n*  Código Asocio ["+forma.getMapaGruposAux("codigoAsocio_"+indexKey)+"] " +
			"\n*  Código Esquema Tarifario ["+forma.getCodigoEsquemaTarifario()+"] " +
			"\n*  Código Cups ["+forma.getMapaGruposAux("codigoCups_"+indexKey)+"] " +
			"\n*  Código Soat ["+forma.getMapaGruposAux("codigoSoat_"+indexKey)+"] " +
			"\n*  Código Tipo Liquidación ["+forma.getMapaGruposAux("codigoTipoLiquidacion_"+indexKey)+"] " +
			"\n*  Unidades ["+forma.getMapaGruposAux("unidades_"+indexKey)+"] " +
			"\n*  Valor ["+forma.getMapaGruposAux("valor_"+indexKey)+"] " +
			"\n*  Activo ["+forma.getMapaGruposAux("activo_"+indexKey)+"] " +
			"\n*  Institucion ["+usuario.getCodigoInstitucion()+"] " +
			""  ;
			log+="\n========================================================\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logGruposCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario()); 
	    }
	    else
	    {
	        log="\n            =========INFORMACION ORIGINAL GRUPOS QUIRURGICOS======== " +
			"\n*  Grupo [" +forma.getMapaGruposNoModificado("grupo_"+indexKey) +"] "+
			"\n*  Código Asocio ["+forma.getMapaGruposNoModificado("codigoAsocio_"+indexKey)+"] " +
			"\n*  Código Esquema Tarifario ["+forma.getCodigoEsquemaTarifario()+"] " +
			"\n*  Código Cups ["+forma.getMapaGruposNoModificado("codigoCups_"+indexKey)+"] " +
			"\n*  Código Soat ["+forma.getMapaGruposNoModificado("codigoSoat_"+indexKey)+"] " +
			"\n*  Código Tipo Liquidación ["+forma.getMapaGruposNoModificado("codigoTipoLiquidacion_"+indexKey)+"] " +
			"\n*  Unidades ["+forma.getMapaGruposNoModificado("unidades_"+indexKey)+"] " +
			"\n*  Valor ["+forma.getMapaGruposNoModificado("valor_"+indexKey)+"] " +
			"\n*  Activo ["+forma.getMapaGruposNoModificado("activo_"+indexKey)+"] " +
			"\n*  Institucion ["+usuario.getCodigoInstitucion()+"] " +
			""  ;
	        log+="\n========================================> FUE ELIMINADO\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logGruposCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuario.getLoginUsuario());
	    }
	    */
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * 
	 * @param forma GruposForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param int codigoInstitucion
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "resumenGrupos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	GruposForm forma, 
																ActionMapping mapping,
																int codigoInstitucion,
																Connection con) throws SQLException
	{
	    int codigoEsqTar= forma.getConsecutivoEsquemaTarifario();
	    boolean esEsquemaGeneral = forma.isEsquemaTarifarioGeneral();
	    //forma.reset();
	    forma.resetMapa();
	    forma.setConsecutivoEsquemaTarifario(codigoEsqTar);
	    forma.setEsquemaTarifarioGeneral(esEsquemaGeneral);
		Grupos mundo= new Grupos();
		mundo.setEsquemaTarifario(new InfoDatosInt(forma.getConsecutivoEsquemaTarifario(), ""));
		//forma.setMapaGrupos(mundo.listadoMapaGrupos(con, codigoInstitucion ));
		forma.setMapaGrupos(mundo.listadoGrupos(con, codigoInstitucion, forma.getConsecutivoEsquemaTarifario(), forma.getCodigoPKGrupo(),forma.isEsquemaTarifarioGeneral()));
		

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//modificacion por tarea 3384
		int numReg=Utilidades.convertirAEntero(forma.getMapaGrupos("numRegistros")+"");
		for (int i=0;i<numReg;i++)
		forma.setMapaGrupos("valor_"+i, UtilidadTexto.formatearValores(forma.getMapaGrupos("valor_"+i)+"", "000.00"));
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaResumen");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarConsultar
	 * @param forma GruposForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoGrupos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarConsultar(	GruposForm forma, 
																		ActionMapping mapping,
																		Connection con, int codigoInstitucion) throws SQLException 
	{
	    forma.reset();
	    forma.resetMapa();
	    forma.setConvenios(Utilidades.obtenerConvenios(con, "","2",false,"",false));
		forma.setEstado("listarConsultar");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaListar");
	}	
	
	/**
	 * Accion ordenar de la consulta de grupos
	 * @param gruposForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenar(		GruposForm gruposForm,
																ActionMapping mapping,
																HttpServletRequest request, 
																Connection con) throws SQLException
    {
	    try
	    {
	        gruposForm.setCol(Listado.ordenarColumna(new ArrayList(gruposForm.getCol()),gruposForm.getUltimaPropiedad(),gruposForm.getColumna()));
	        gruposForm.setUltimaPropiedad(gruposForm.getColumna());
	        UtilidadBD.cerrarConexion(con);
	        return mapping.findForward("paginaListar")	;
	    }
		catch(Exception e)
		{
			logger.warn("Error en el listado de grupos ");
			gruposForm.reset();
			gruposForm.resetMapa();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado Grupos");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");		
		}
    }	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param forma GruposForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "listadoGrupos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	GruposForm forma, 
																				ActionMapping mapping, 
																				Connection con, 
																				int codigoInstitucion) throws SQLException
	{
	    Grupos mundo= new Grupos();
	    String forward="paginaListar";
	   
		forma.resetMapa();
	    forma.setMapaGrupos(mundo.busquedaAvanzadaGrupos(con, Utilidades.convertirAEntero(forma.getGrupo()), forma.getCodigoAsocio(), forma.getCodigoTipoLiquidacion(), codigoInstitucion, forma.getTipoServicio(),forma.getConsecutivoEsquemaTarifario(),forma.getConvenio(),UtilidadFecha.conversionFormatoFechaABD(forma.getMapaVigencias("fechainicial_"+forma.getIndexMapa())+""),UtilidadFecha.conversionFormatoFechaABD(forma.getMapaVigencias("fechafinal_"+forma.getIndexMapa())+""),forma.getLiquidarPor(),forma.getCups()));
	    forma.setTiposServicio(mundo.listarTiposServicio(con));
		forma.setTarifariosOficiales(mundo.listarTarifariosOficiales(con));
	    
	    UtilidadBD.cerrarConexion(con);
	    
	    if (forma.getFuncionalidad().equals("ingresarModificarEsqTar"))
	    	forward="paginaPrincipal";
	    	
		return mapping.findForward(forward);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionListarVigencias(Connection con,ActionMapping mapping, GruposForm forma) throws SQLException
	{
		forma.setEstado("listarVigencias");
		Grupos mundo=new Grupos();
		mundo.setConvenio(forma.getConvenio());
		forma.setMapaVigencias(mundo.listarVigenciasConvenio(con, Utilidades.convertirAEntero(forma.getConvenio())));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaVigencias");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionNuevaVigencia(Connection con, GruposForm forma, ActionMapping mapping) 
	{
		int pos=Utilidades.convertirAEntero(forma.getMapaVigencias("numRegistros")+"");
		forma.setMapaVigencias("fechainicial_"+pos,"");
		forma.setMapaVigencias("fechafinal_"+pos,"");
		forma.setMapaVigencias("codigogrupo_"+pos,ConstantesBD.codigoNuncaValido);
		forma.setMapaVigencias("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setMapaVigencias("numRegistros",(pos+1)+"");
		return mapping.findForward("listaVigencias");
	}
	
	private ActionForward accionGuardarVigencia(Connection con, GruposForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		Grupos mundo=new Grupos();
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int codigo=ConstantesBD.codigoNuncaValido;
		int numReg=Utilidades.convertirAEntero(forma.getMapaVigencias("numRegistros")+"");
		//ELIMINAR
		for(int i=0;i<Integer.parseInt(forma.getMapaVigenciasEliminado("numRegistros")+"");i++)
		{
			logger.info("\nELIMINANDO...");
			transaccion=mundo.eliminarGrupoMaestro(con, Utilidades.convertirAEntero(forma.getConvenio()), Utilidades.convertirAEntero(forma.getMapaVigenciasEliminado("codigogrupo_"+i)+""));
		}
		for(int i=0;i<numReg;i++)
		{
			//MODIFICAR
			if((forma.getMapaVigencias("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				logger.info("\nMODIFICANDO...");
				HashMap vo=new HashMap();
				vo.put("fechainicial", forma.getMapaVigencias("fechainicial_"+i));
				vo.put("fechafinal", forma.getMapaVigencias("fechafinal_"+i));
				vo.put("convenio", forma.getConvenio());
				vo.put("codigogrupo", forma.getMapaVigencias("codigogrupo_"+i));
				transaccion=mundo.modificarVigenciasConvenio(con, vo);
			}
			//INSERTAR
			else if((forma.getMapaVigencias("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				logger.info("\nINSERTANDO...");
				codigo=mundo.insertarVigenciaGrupo(con, forma.getConsecutivoEsquemaTarifario(), forma.getConvenio(), forma.getMapaVigencias("fechainicial_"+i)+"", forma.getMapaVigencias("fechafinal_"+i)+"", usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(),forma.isEsquemaTarifarioGeneral());
			}
		}
		if(transaccion||codigo>0)
		{
			UtilidadBD.finalizarTransaccion(con);
			logger.info("\nOPERACION REALIZADA CON EXITO!!!");
		}
		else 
		{
			UtilidadBD.abortarTransaccion(con);
			logger.info("\nNO SE GUARDARON CAMBIOS!!!");
		}
		forma.setMapaVigencias(mundo.listarVigenciasConvenio(con, Utilidades.convertirAEntero(forma.getConvenio())));
		return mapping.findForward("listaVigencias");
	}

}
