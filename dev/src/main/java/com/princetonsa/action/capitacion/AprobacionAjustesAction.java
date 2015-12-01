/*
 * Creado el Jun 30, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.capitacion.AprobacionAjustesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.AprobacionAjustes;
import com.princetonsa.pdf.AprobacionAjustesPdf;

public class AprobacionAjustesAction extends Action {
	
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(AprobacionAjustesAction.class);
	
	
	/**
	 * Funcion que maneja la navegacion por la funcionalidad.
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof AprobacionAjustesForm)
		{
			
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			} 
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			AprobacionAjustesForm forma = (AprobacionAjustesForm) form;
			String estado=forma.getEstado();
			logger.warn("AprobacionAjustesAction el Estado [" + estado + "]\n\n");
			
			 
			if(estado.equals("aprobacion")) 	 //-----Cuado se entra por Aprobar
			{	
				return accionEmpezar(con, mapping, forma, usuario, true);
			} 
			else if (estado.equals("empezar"))   //-----Estado Neutral PARA COMENZAR si setear la variable  "SoloConsulta"
			{								     //-----Para devolverse de la pagina de detalle	
				return accionEmpezar(con, mapping, forma, usuario, false); 
			} 
			else if (estado.equals("buscar")) 	   //-----Cuando se ejecuta la consulta.
			{	
				return accionBuscar(con, mapping, forma, usuario, request); 
			} 
			else if (estado.equals("redireccion")) //-- Estado para mantener los datos del pager
			{			    
			    UtilidadBD.cerrarConexion(con);
			    response.sendRedirect(forma.getLinkSiguiente());
			    return null;
			}
			else if (estado.equals("detalleAjuste")) //-- Estado para mostrar el detalle de algun ajuste
			{			    
				return accionDetalleAjuste(con, mapping, forma, usuario, "detalleAjuste"); 
			}
			else if (estado.equals("detalleCargue")) //-- Estado para mostrar el detalle del cuenta de Cobro.
			{			    
				return accionDetalleCargue(con, mapping, forma, usuario); 
			}
			else if (estado.equals("aprobarAjuste")) //-- Estado para Aprobar el ajuste Seleccionado.
			{			    
				return accionAprobarAjuste(con, mapping, forma, usuario); 
			}
			else if (estado.equals("imprimirAjuste")) //-- Estado para Aprobar el ajuste Seleccionado.
			{			    
				return accionImprimirAjuste(con, mapping, forma, usuario, request); 
			}
			
			
			//-- ---> Estados para la funcionalidad CONSULTA/IMPRESION AJUSTES. 
			else if (estado.equals("consultar")) 
			{			    
				return accionBusquedaAjustes(con, mapping, forma, usuario, true); 
			}
			else if (estado.equals("consultarNoReset")) 
			{			    
				return accionBusquedaAjustes(con, mapping, forma, usuario, false); 
			}
			else if (estado.equals("buscarAjustes")) 		  //-Realizar la busqueda con los datos ingresados en la busqueda Avanzada
			{			    
				return accionRealizarBusquedaAjustes(con, mapping, forma, usuario); 
			}
			else if (estado.equals("detalleAjusteConsulta"))  		  //-- Realizar la busqueda del detalle del ajuste. para la funcionalidad de Consulta de Ajustes.
			{			    
				return accionDetalleAjusteConsulta(con, mapping, forma, usuario); 
			}
			else if (estado.equals("imprimirAjusteConsulta"))  		  //-- Generar el PDf con la inforamcion de un ajuste especifico.
			{			    
				return accionImprimirAjusteConsulta(con, mapping, forma, usuario, request); 
			}
			else if (estado.equals("imprimirListadoAjusteConsulta"))  //-- Realizar la busqueda del detalle del ajuste. para la funcionalidad de Consulta de Ajustes.
			{			    
				return accionImprimirListadoAjusteConsulta(con, mapping, forma, usuario, request); 
			}
			else if (estado.equals("ordenar"))  //-- Realizar el Ordenamientos del Listado de Ajustes Segun la Busqueda  Avanzada.
			{			    
				return accionOrdenarListadoAjustes(con, mapping, forma, usuario, request); 
			}
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
	    else
	    {
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
	 * Metodo para realizar el Ordenamientos del Listado de Ajustes Segun la Busqueda  Avanzada.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionOrdenarListadoAjustes(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		String[] indices = {"cod_tipo_ajuste_", "tipo_ajuste_", "consecutivo_", "estado_ajuste_", "codigo_estado_ajuste_",
							"fecha_ajuste_", "fecha_elaboracion_", "usuario_elaboracion_", "valor_total_",
							"codigo_contrato_", "cuenta_cobro_", "convenio_", "codigo_ajustes_", "fecha_aprobacion_", "usuario_aprobacion_"};

		int nroReg = 0;
		if (UtilidadCadena.noEsVacio(forma.getMapaAjuste("numRegistros")+""))
		{
			nroReg = Integer.parseInt( forma.getMapaAjuste("numRegistros") + "");
		}

		forma.getMapaAjuste().putAll(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),
									            forma.getMapaAjuste(), nroReg));

		forma.setMapaAjuste("numRegistros", nroReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listadoBusqueda");	
	}

	/**
	 * Metodo para generar el reporte del detalle de un ajuste especifico. Para la Funcionalidad de   
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirAjuste(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
		
		//-- Adicionar Al Mapa Los Convenios.
		HashMap mapa = new HashMap();
		
		//-- Para cargar el detalle de un ajuste.
		mapa.put("nroConsulta", "15");
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		
		
		
		mapa.put("nroCodigoAjuste","" + forma.getMapaAjuste("d_codigo_ajuste_0"));    //-- Colocar el Id del Ajuste a Buscar.
		
		
		//-- Consultar.
		mapa = mundo.cargarInformacion(con, mapa);
		mapa.remove("numRegistros");
		forma.getMapaAjuste().putAll(mapa);
		
		
		//---------------------------------------------------
		//---  Consultar la Información de los cargues. -----
		//---------------------------------------------------
		
		//-- Para cargar el detalle de un ajuste.
		mapa.clear();
		mapa.put("nroConsulta", "16"); 
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());

		//-- Colocar el Id del cuenta de cobro.	
		mapa.put("nroCodigoAjuste","" + forma.getMapaAjuste("nroCodigoAjuste"));
		//-- mapa.put("nroCuentaCobro","" + forma.getCodigoCuentaCobroSel());
		
		//-- Consultar. Todos los cargues asociados a esa cuenta de cobro.
		forma.setMapaCargue( mundo.cargarInformacion(con, mapa) );


		//------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------
		//------------------------------- Enviar al Reporte ---------------------------------- 
		
		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Detalle_Ajuste (Capitacion) -" + r.nextInt()  +".pdf";
   
        AprobacionAjustesPdf.pdfDetalleAjusteConsulta(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request);	
        
        UtilidadBD.cerrarConexion(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Detalle Ajuste Cartera Capitación");
	    return mapping.findForward("abrirPdf");	      
	}



	/**
	 * Metodo para generar el PDF con el listado de ajustes generados a partir de una consulta.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirListadoAjusteConsulta(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Listados_Ajustes_Capitacion" + r.nextInt()  +".pdf";
   
        AprobacionAjustesPdf.pdfListadoAjusteConsulta(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request);	
        
        UtilidadBD.cerrarConexion(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Detalle Ajuste Cartera Capitación");
	    return mapping.findForward("abrirPdf");	      
	}

	/**
	 * Metodo para Imprimir el Detalle del Ajuste desde la Funcionalidad Consultar/Imprimir ajustes Capitados   
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirAjusteConsulta(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Detalle_Ajuste_Capitacion_" + r.nextInt()  +".pdf";
   
        AprobacionAjustesPdf.pdfDetalleAjusteConsulta(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request);	
        
        UtilidadBD.cerrarConexion(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Detalle Ajuste Cartera Capitación");
	    return mapping.findForward("abrirPdf");	      
	  }


	/**
	 * Realizar la busqueda del detalle del ajuste. para la funcionalidad de Consulta de Ajustes.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalleAjusteConsulta(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario) throws SQLException
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
		
		HashMap mapa = new HashMap();
		
		//-- Para cargar el detalle de un ajuste.
		mapa.put("nroConsulta", "15");
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mapa.put("nroCodigoAjuste","" + forma.getMapaAjuste("nroCodigoAjuste"));    //-- Colocar el Id del Ajuste a Buscar.
		
		
		//-- Consultar.
		mapa = mundo.cargarInformacion(con, mapa);
		mapa.remove("numRegistros");
		forma.getMapaAjuste().putAll(mapa);
		
		//-- Consultar si los cargues tiene información asociada al ajuste.
		mapa.clear();
		mapa.put("nroConsulta", "10");
		mapa.put("codigoAjuste", forma.getMapaAjuste("nroCodigoAjuste"));
		mapa = mundo.cargarInformacion(con, mapa);
		
		if ( UtilidadCadena.noEsVacio(mapa.get("numRegistros")+"") && (Integer.parseInt(mapa.get("numRegistros")+"") > 0) && (Integer.parseInt(mapa.get("cantidad_cargues_0")+"") > 0) ) 
		{
			forma.setMapaAjuste("HayCarguesAjustados","si");
		}
		else 
		{
			forma.setMapaAjuste("HayCarguesAjustados","no");
		}	
		
		//---------------------------------------------------
		//---  Consultar la Información de los cargues. -----
		//---------------------------------------------------
		
		//-- Para cargar el detalle de un ajuste.
		mapa.put("nroConsulta", "16"); 
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());

		//-- Colocar el Id del cuenta de cobro.	
		mapa.put("nroCodigoAjuste","" + forma.getMapaAjuste("nroCodigoAjuste"));
		
		//-- Consultar. Todos los cargues asociados a esa cuenta de cobro.
		forma.setMapaCargue( mundo.cargarInformacion(con, mapa) );
		

		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleAjusteConsulta");
	}


	/**
	 * Metodo para Consultar con los parametros de busqueda insertados.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionRealizarBusquedaAjustes(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario) throws SQLException
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
		

		//-- Parametro de Consulta.
		//-- HashMap mapa = new HashMap(); mapa.put("nroConsulta", "7"); 
		//-- forma.setMapa( mundo.cargarInformacion(con, mapa) );
		
		//-- Colocar los parametros de busqueda en el mapa		
		HashMap mapa = new HashMap();
		mapa.put("nroConsulta", "7");
		
		//-- Cargar La información de la busqueda avanzada en el MAPA.
		mapa.put("codigoInstitucion", ""+usuario.getCodigoInstitucionInt());

		//-- Colocar la informacion en el MAPA.
		mapa.put("tipoAjuste", ""+forma.getTipoAjuste());
		mapa.put("estadoAjuste", ""+forma.getEstadoAjuste());
		mapa.put("nroAjusteIni", ""+forma.getNroAjuste());
		mapa.put("nroAjusteFin", ""+forma.getNroAjusteFinal());
		mapa.put("fechaIniElab", ""+forma.getFechaAjuste());
		mapa.put("fechaFinElab", ""+forma.getFechaAprobacionAjuste());		
		mapa.put("cuentaCobro", ""+forma.getNroCuentaCobro());
		mapa.put("convenio", ""+forma.getNroConvenio());
		
		//-- Cargar la informacion en el Mapa.
		forma.setMapaAjuste(  mundo.cargarInformacion(con, mapa) );
		
		//-- Retornar la pagina del listado.
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listadoBusqueda");
	}




	/**
	 * Metodo para Retornar la pagina de la busqueda Avanzada
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param resetear 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBusquedaAjustes(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, boolean resetear) throws SQLException
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 

		//-- Este es para no borrar la informacion de consulta en dado caso que se devuelvan
		if ( resetear )
		{	
			
			//-- Limpiar el informacion del Form
			forma.reset();
		}

		//-- Consultar los tipos de Ajustes (Debito y Credito).
		HashMap mapa = new HashMap(); mapa.put("nroConsulta", "0"); 
		forma.setMapa( mundo.cargarInformacion(con, mapa) );
		
		//-- Consultar los estados de los ajustes.		
		mapa.clear();
		mapa.put("nroConsulta", "5");
		HashMap mp = mundo.cargarInformacion(con, mapa);
		forma.getMapa().put("numRegistrosTa",mp.get("numRegistros"));
		mp.remove("numRegistros");  
		forma.getMapa().putAll(mp);
		
		//-- Consultar Los Convenios
		mp.clear(); 
		mapa.put("nroConsulta", "1");
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mapa.put("vigentes", "false");
		
		mp = mundo.cargarInformacion(con, mapa);
		forma.getMapa().put("numRegConvenios", mp.get("numRegistros"));
		mp.remove("numRegistros");
		forma.getMapa().putAll(mp);
		
		//-- Consultar los tipos de conceptos parametrizados en el sistema (SOLO CAPITADOS y TODOS)
		mp.clear(); 
		mapa.put("nroConsulta", "6");
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mp = mundo.cargarInformacion(con, mapa);
		forma.getMapa().put("numRegConceptos", mp.get("numRegistros"));
		mp.remove("numRegistros");
		forma.getMapa().putAll(mp);
		
		
		
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principalBusqueda");
	}


	/**
	 * Metodo para Aprobar Un Ajuste Especifico.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */

	private ActionForward accionAprobarAjuste(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario) throws SQLException 
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
		
		//-- Crear el mapa de los Parametros. 
		HashMap mapa = new HashMap();
		
		//-- Insertar La Informacion para Inserción
		mapa.put("codigoAjuste","" + forma.getMapaAjuste("d_codigo_ajuste_0") );
		mapa.put("valorAjuste","" + forma.getMapaAjuste("d_valor_total_0") );
		mapa.put("codigoCuentaCobro","" + forma.getMapaAjuste("d_cuenta_cobro_0") );
		mapa.put("codigoTipoAjuste","" + forma.getMapaAjuste("d_cod_tipo_ajuste_0") ); //-Codigo del tipo del ajuste.
		

		mapa.put("usuario","" + usuario.getLoginUsuario());
		mapa.put("institucion","" + usuario.getCodigoInstitucionInt());
		mapa.put("fechaAprobacion","" + forma.getFechaAprobacionAjuste());
		
		//-- Consultar. Todos los cargues asociados a esa cuenta de cobro.
		int resp = mundo.aprobarAjuste(con, mapa);

		//-- Determinar si el ajuste se aprobo correctamente.
		if (resp > 0) { forma.setMapaAjuste("ajusteAprobado", "si"); }
		else		  {	forma.setMapaAjuste("ajusteAprobado", "no"); }
		
		//--   
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("ajusteAprobado");
	}


	/**
	 * Metodo para consultar los cargues de una cuenta de Cobro Especifica.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalleCargue(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario) throws SQLException
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
		
		//-- Adicionar Al Mapa Los Convenios.
		HashMap mapa = new HashMap();
		
		//-- Para cargar el detalle de un ajuste.
		mapa.put("nroConsulta", "4"); 
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());

		//-- Colocar el Id del cuenta de cobro.	
		mapa.put("nroCodigoAjuste","" + forma.getMapaAjuste("nroCodigoAjuste"));
		mapa.put("nroCuentaCobro","" + forma.getCodigoCuentaCobroSel());
		
		//-- Consultar. Todos los cargues asociados a esa cuenta de cobro.
		forma.setMapaCargue( mundo.cargarInformacion(con, mapa) );
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleCargue");
	}


	/**
	 * Metodo para mostrar la informacion de un ajuste especifico.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalleAjuste(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, String destino) throws SQLException
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
		
		//-- Adicionar Al Mapa Los Convenios.
		HashMap mapa = new HashMap();
		
		//-- Cargar La fecha Inicial de Cierre.
		HashMap mp = new HashMap();
		mp.put("codigoInstitucion",usuario.getCodigoInstitucionInt());
		mp.put("nroConsulta","9");
		
		mp = mundo.cargarInformacion(con, mp);
		if ( UtilidadCadena.noEsVacio(mp.get("fecha_cierre_0")+"") ) 
		{
			forma.setFechaCierreInicial(mp.get("fecha_cierre_0")+""); 
		}
		
		//-- Para cargar el detalle de un ajuste.
		mapa.put("nroConsulta", "3"); 
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());

		//-- Colocar el Id del Ajuste a Buscar.	
		mapa.put("nroCodigoAjuste","" + forma.getMapaAjuste("nroCodigoAjuste"));
		
		//-- Consultar.
		mapa = mundo.cargarInformacion(con, mapa);
		mapa.remove("numRegistros");
		forma.getMapaAjuste().putAll(mapa);
		
		//-- Consultar si los cargues tiene información asociada al ajuste.
		mapa.clear();
		mapa.put("nroConsulta", "10");
		mapa.put("codigoAjuste", forma.getMapaAjuste("nroCodigoAjuste"));
		mapa = mundo.cargarInformacion(con, mapa);
		
		if ( UtilidadCadena.noEsVacio(mapa.get("numRegistros")+"") && (Integer.parseInt(mapa.get("numRegistros")+"") > 0) && (Integer.parseInt(mapa.get("cantidad_cargues_0")+"") > 0) ) 
		{
			forma.setMapaAjuste("HayCarguesAjustados","si");
		}
		else 
		{
			forma.setMapaAjuste("HayCarguesAjustados","no");
		}	
		
		
		
		//-- Verificar si los ajustes credito de cada cargue individual
		//-- no sobrepasan el valor total de cada cargue 
		mapa.clear();
		mapa.put("nroConsulta", "11");
		mapa.put("codigoAjuste", forma.getMapaAjuste("nroCodigoAjuste"));
		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mapa = mundo.cargarInformacion(con, mapa);
		
		if ( UtilidadCadena.noEsVacio(mapa.get("numRegistros")+"") && (Integer.parseInt(mapa.get("numRegistros")+"") > 0) && (Integer.parseInt(mapa.get("cantidad_0")+"") > 0) ) 
		{
			forma.setMapaAjuste("ErrorAjusteCredito","si");
		}
		else 
		{
			forma.setMapaAjuste("ErrorAjusteCredito","no");
		}	
		
		
		UtilidadBD.cerrarConexion(con);
		
		//-- Para direccionar a la pagina de busqueda en caso de que solo 
		//-- se encuentre un solo registro.
		if ( UtilidadCadena.vNull(forma.getMapaAjuste("numRegistros")+"", "1") )
		{
			return mapping.findForward("principal");
		}
		else
		{
			return mapping.findForward(destino);		
		}
	}


	/**
	 * Metodo que retorna la el listado de Ajustes, dados unos parametros de consulta. 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBuscar(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
		
		//-- Colocar el numero de la consulta en el mapa. (2 --> Busqueda Avanzada)
		HashMap mapa = new HashMap(); mapa.put("nroConsulta", "2"); 
		
		//-- Cargar La información de la busqueda avanzada en el MAPA.
		mapa.put("codigoInstitucion", ""+usuario.getCodigoInstitucionInt());

		mapa.put("tipoAjuste", ""+forma.getTipoAjuste());
		mapa.put("nroAjuste", ""+forma.getNroAjuste());
		mapa.put("fechaAjuste", ""+forma.getFechaAjuste());
		mapa.put("cuentaCobro", ""+forma.getNroCuentaCobro());
		mapa.put("convenio", ""+forma.getNroConvenio());
		
		//-- Consultar la informacion con los parametros de consulta.
		forma.setMapaAjuste( mundo.cargarInformacion(con, mapa) );
		
		//-- Para Sacar el Mensaje de Busqueda Avanzada. 
		mapa.clear();
		if ( forma.getTipoAjuste() != 0 ) 
		{
			//-Verificar que el ajuste esta pero con otro estado.
			mapa.put("nroConsulta", "8");
			
			mapa.put("codigoInstitucion", ""+usuario.getCodigoInstitucionInt());
			mapa.put("tipoAjuste", ""+forma.getTipoAjuste());
			mapa.put("nroAjuste", ""+forma.getNroAjuste());
		
			HashMap mp = mundo.cargarInformacion(con, mapa);
			if ( UtilidadCadena.noEsVacio(""+mp.get("numRegistros")) && (Integer.parseInt(""+mp.get("numRegistros")) == 1) )
			{
				forma.getMapaAjuste().put("aux_estado_ajuste", mp.get("aux_estado_ajuste_0") );
			}
			else
			{
				//--- Para indicar el numero y tipo de ajuste, que se mandaron
				//--- como parametro de busqueda no estan registrados en la BD.
				forma.getMapaAjuste().put("aux_estado_ajuste", "");
			}
		}
		else 
		{
			//--- Para indicar el numero y tipo de ajuste, que se mandaron 
			//--- como parametro de busqueda no estan registrados en la BD.
			forma.getMapaAjuste().put("aux_estado_ajuste", "");			
		}
		
	
		 
		//-- Verificar si es un solo registro mostrar el detalle del Ajuste
		//-- de lo contrario mostrar el listado de ajustes.
		 if ( UtilidadCadena.noEsVacio(forma.getMapaAjuste("numRegistros")+"") && (Integer.parseInt(forma.getMapaAjuste("numRegistros")+"") == 1)  ) 
		 {
			 forma.setMapaAjuste("nroCodigoAjuste", forma.getMapaAjuste("codigo_0")+"" );
			 forma.setCodigoCuentaCobroSel( Integer.parseInt(forma.getMapaAjuste("cuenta_cobro_0")+"") );
			 
			 
			 return accionDetalleAjuste(con, mapping, forma, usuario, "principal"); 
		 }
		 else
		 {			 
			 UtilidadBD.cerrarConexion(con);
			 return mapping.findForward("listado");
		 }
	}
	
	/**
	 * Metodo para empezar en la funcionalidad.  
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param resetear 
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, AprobacionAjustesForm forma, UsuarioBasico usuario, boolean resetear) throws SQLException 
	{
		AprobacionAjustes mundo = new AprobacionAjustes(); 
	
		if ( resetear )
		{
			//-- Limpiar el informacion del Form
			forma.reset();
		}	
		
		//-- Colocar el numero de la consulta en el mapa.
		HashMap mapa = new HashMap(); mapa.put("nroConsulta", "0"); 
		
		//-- Consultar los tipos de Ajustes (Debito y Credito).
		forma.setMapa( mundo.cargarInformacion(con, mapa) );
		
		//-- Adicionar Al Mapa Los Convenios.
		HashMap mp = new HashMap();
		mapa.put("nroConsulta", "1");
		mapa.put("vigentes", "true");

		mapa.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mp = mundo.cargarInformacion(con, mapa);
		forma.getMapa().put("numRegConvenios", mp.get("numRegistros"));
		mp.remove("numRegistros");
		forma.getMapa().putAll(mp);
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
}



/*		DesignEngineApi comp;        
comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"capitacion/","listadoAjustes.rptdesign");
comp.insertImageHeaderOfMasterPage(0, 0, ValoresPorDefecto.getDirectorioImagenes()+"logo_clinica_sana_login.gif");
Vector v=new Vector();

//-- Obtener la informacion de la Empresa.
ParametrizacionInstitucion ins=new ParametrizacionInstitucion();
ins.cargar(con, usuario.getCodigoInstitucionInt());

v.add("                 "); v.add("" + ins.getRazonSocial() ); 			
v.add("                 "); v.add(ins.getDescripcionTipoIdentificacion()+" "+ins.getNit());		 
v.add("                 "); v.add("Dirección: " + ins.getDireccion());		 
v.add("                 "); v.add("Telefono: " + ins.getTelefono());		 

boolean entro = false; int nroFilas = 4;

//-Si se hizo la busqueda por fechas insertarlas en el reporte.
if ( !forma.getFechaIniCirugia().trim().equals("") || !forma.getFechaFinCirugia().trim().equals("") )
{
	v.add("                 ");				v.add("                 ");
	v.add("Parametros de Busqueda  ");  	v.add("                 ");
	v.add("Fecha Inicial Cirugía:  "); v.add( forma.getFechaIniCirugia() ); 
	v.add("Fecha Final   Cirugía:  "); v.add( forma.getFechaFinCirugia() );  
	entro = true; nroFilas+=4;
}
if ( !forma.getFechaIniPeticion().trim().equals("") || !forma.getFechaFinPeticion().trim().equals("") )
{
	if (!entro) 
	{
    	v.add("                 ");				v.add("                 ");
		v.add("Parametros de Busqueda  ");  	v.add("                 ");
    	nroFilas+=2;
	}
	v.add("Fecha Inicial Petición:  "); v.add( forma.getFechaIniPeticion() ); 
	v.add("Fecha Final	 Petición:  "); v.add( forma.getFechaFinPeticion() );
	nroFilas+=2;
}

//-En la columna 1 Fila 0 del Grid de Encabezado se insertara una tabla de 2 columna y 3 Filas 
comp.insertGridHeaderOfMasterPage(0, 1, 2, nroFilas);
comp.insertLabelInGridOfMasterPage(0, 1, v);


//-----Barrer el Mapa e insertarlo en un vector.
//HashMap mp = forma.getMapaPeticiones();

int nroReg = 0;
//-Colocar los cirujanos
//if ( UtilidadCadena.noEsVacio( mp.get("numRegistros") +"" ) ) { nroReg = Integer.parseInt( mp.get("numRegistros") +"" );	}


String columnas [] = {"codigo_peticion_", "paciente_", "fecha_cirugia_", "hora_cirugia_",  "serviciof_",
					  "cirujanof_", "tipo_anestesia_", "anestesiologof_", "observacionesf_", "empresa_"};

//-Insertar la informacion en el Mapa del Reporte.
//comp.insertarMapaEnReporte(columnas, forma.getMapaPeticiones(), 1);        

//--Insertar EL Nombre Del Usuario Que Imprimio El Reporte. 
comp.insertarUsuarioImprimio(usuario.getNombreUsuario());

//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
request.setAttribute("isOpenReport", "true");
request.setAttribute("newPathReport", newPathReport);
comp.updateJDBCParameters(newPathReport);

UtilidadBD.cerrarConexion(con);
return mapping.findForward("listadoBusqueda");*/
