package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
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
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.seccionesparametrizables.TecnicaAnestesiaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.TecnicaAnestesia;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.princetonsa.mundo.solicitudes.Solicitud;

/**
 * 
 * @author wilson
 *
 */
public class TecnicaAnestesiaAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(TecnicaAnestesiaAction.class);
    
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
    		if(form instanceof TecnicaAnestesiaForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			TecnicaAnestesiaForm forma =(TecnicaAnestesiaForm)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en Tecnica Anestesia es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de TECNICA ANESTESIA (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarTecnicaAnestesia") || estado.equals("resumen"))
    			{
    				return this.accionEmpezarTecnicaAnestesia(request, forma, mapping, con, usuario);
    			}
    			else if(estado.equals("adicionarTecnicaAnestesia"))
    			{
    				return this.accionAdicionarTecnicaAnestesia(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("eliminarTecnicaAnestesia"))
    			{
    				return this.accionEliminarTecnicaAnestesia(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarTecnicaAnestesia"))
    			{
    				return this.accionGuardarTecnicaAnestesia(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("empezarTecnicaAnestesiaGeneralRegional") || estado.equals("resumenGeneralRegional"))
    			{
    				return this.accionEmpezarTecnicaAnestesiaGeneralRegional(forma, request, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarTecnicaAnestesiaGeneralRegional"))
    			{
    				return this.accionGuardarTecnicaAnestesiaGeneralRegional(forma, request,mapping, con, usuario);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de TECNICA ANESTESIA");
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
	private ActionForward accionGuardarTecnicaAnestesiaGeneralRegional(TecnicaAnestesiaForm forma, HttpServletRequest request,ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		logger.info("\n*************************************************************************************************************");
		logger.info("MAPA tecnica---->"+forma.getMapaTecAnestesia());
		logger.info("*************************************************************************************************************\n");
		
		UtilidadBD.iniciarTransaccion(con);
		TecnicaAnestesia.insertarTecnicaAnestesiaGeneralRegional(con, forma.getNumeroSolicitud(), forma.getMapaTecAnestesia());
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		
		forma.setEstado("resumenGeneralRegional");
		return this.accionEmpezarTecnicaAnestesiaGeneralRegional(forma,request, mapping, con, usuario);
	}

	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionGuardarTecnicaAnestesia(TecnicaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		boolean exitoso=false;
		
		exitoso=TecnicaAnestesia.eliminarTecnicaAnestesia(con, forma.getNumeroSolicitud());
		
		if(exitoso)
		{
			for(int w=0; w<Integer.parseInt(forma.getMapaTecAnestesia("numRegistros")+"");w++)
			{
				exitoso=TecnicaAnestesia.insertarTecnicaAnestesia(	con, 
															forma.getNumeroSolicitud(), 
															Integer.parseInt(forma.getMapaTecAnestesia("tipo_anestesia_"+w)+""), 
															Integer.parseInt( forma.getMapaTecAnestesia("tipo_anestesia_inst_cc_"+w)+""), 
															usuario.getLoginUsuario());
				if(!exitoso)
				{
					UtilidadBD.abortarTransaccion(con);
					logger.error("no inseto el detalle de la tecnica de anes -->"+forma.getMapaTagTecnicaAnestesia("descripcion_"+w));
				}
			}
		}
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		if(exitoso)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
			
		forma.setEstado("resumen");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tecnicaAnestesia");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarTecnicaAnestesia(HttpServletRequest request,TecnicaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.resetTecnicaAnestesia();
		inicializarParametrosRequest(forma,request);
		forma.setMapaTecAnestesia(TecnicaAnestesia.cargarTecnicaAnestesiaSolicitud(con, forma.getNumeroSolicitud()));
		validacionMostrarInfo(forma,forma.getMapaTecAnestesia());		
		
		//segun duvian en esta tecnica de anestesia se tiene que mandar el centro costo que solicita
		forma.setCentroCosto(Solicitud.obtenerCodigoCentroCostoSolicitado(con, forma.getNumeroSolicitud()+""));
		
		Vector<String> tiposAnestInstCCNoMostrar= new Vector<String>();
		
		for(int w=0; w<Integer.parseInt(forma.getMapaTecAnestesia().get("numRegistros")+""); w++)
		{
			tiposAnestInstCCNoMostrar.add(forma.getMapaTecAnestesia().get("tipo_anestesia_inst_cc_"+w)+"");
		}
		
		forma.setMapaTagTecnicaAnestesia(TecnicaAnestesia.obtenerTecnicaAnestesia(con, forma.getNumeroSolicitud(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt(), tiposAnestInstCCNoMostrar));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tecnicaAnestesia");
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarTecnicaAnestesia(TecnicaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		String indices[]= {"tipo_anestesia_inst_cc_", "tipo_anestesia_", "descripcion_", "acronimo_"};
		int numRegistros= Integer.parseInt(forma.getMapaTecAnestesia("numRegistros")+"");
		int numRegistrosTag= Integer.parseInt(forma.getMapaTagTecnicaAnestesia("numRegistros")+"");
		
		for(int w=0; w<indices.length; w++)
		{	
			forma.setMapaTecAnestesia(indices[w]+numRegistros, forma.getMapaTagTecnicaAnestesia(indices[w]+forma.getIndex()));
			forma.getMapaTagTecnicaAnestesia().remove(indices[w]+forma.getIndex());
		}
		
		for(int w=forma.getIndex(); w<numRegistrosTag; w++)
		{
			for(int x=0; x<indices.length; x++)
			{	
				forma.setMapaTagTecnicaAnestesia(indices[x]+w, forma.getMapaTagTecnicaAnestesia(indices[x]+(w+1)));
			}	
		}
		
		forma.setMapaTecAnestesia("numRegistros", numRegistros+1);
		forma.setMapaTagTecnicaAnestesia("numRegistros", numRegistrosTag-1);
		forma.setIndex(0);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tecnicaAnestesia");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarTecnicaAnestesia(TecnicaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		String indices[]= {"tipo_anestesia_inst_cc_", "tipo_anestesia_", "descripcion_", "acronimo_"};
		int numRegistros= Integer.parseInt(forma.getMapaTecAnestesia("numRegistros")+"");
		int numRegistrosTag= Integer.parseInt(forma.getMapaTagTecnicaAnestesia("numRegistros")+"");
	
		logger.info("numRegistros->"+numRegistros+" numRegistrosTag-->"+numRegistrosTag+" indice-->"+forma.getIndexEliminado());
		
		for(int w=0; w<indices.length; w++)
		{	
			forma.setMapaTagTecnicaAnestesia(indices[w]+numRegistrosTag, forma.getMapaTecAnestesia(indices[w]+forma.getIndexEliminado()));
			logger.info("seteamos al tag-->"+ forma.getMapaTecAnestesia(indices[w]+forma.getIndexEliminado()));
			forma.getMapaTecAnestesia().remove(indices[w]+forma.getIndexEliminado());
		}
		
		logger.info("tag final->"+forma.getMapaTagTecnicaAnestesia());
		
		for(int w=forma.getIndexEliminado(); w<numRegistros; w++)
		{
			for(int x=0; x<indices.length; x++)
			{	
				forma.setMapaTecAnestesia(indices[x]+w, forma.getMapaTecAnestesia(indices[x]+(w+1)));
			}	
		}
		
		logger.info("mapa final->"+forma.getMapaTecAnestesia());
		
		forma.setMapaTecAnestesia("numRegistros", numRegistros-1);
		forma.setMapaTagTecnicaAnestesia("numRegistros", numRegistrosTag+1);
		
		forma.setIndex(0);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tecnicaAnestesia");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarTecnicaAnestesiaGeneralRegional(TecnicaAnestesiaForm forma, HttpServletRequest request,ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.resetTecnicaGeneralRegional();
		if(!forma.getEstado().equals("resumenGeneralRegional"))
			inicializarParametrosRequest(forma,request);	
		
		//Consulta de los tipos de técnicas de anestesia general que tienen opciones para la institución
		forma.setListadoTecAnestesiaOpcionesGral (TecnicaAnestesia.consultarTipoParametrizado(con, usuario.getCodigoInstitucionInt(), 1));
		
		//Consulta de los tipos de técnicas de anestesia general sin opciones para la institución
		forma.setListadoTecAnestesiaGral (TecnicaAnestesia.consultarTipoParametrizado(con, usuario.getCodigoInstitucionInt(), 2));
		
		//Consulta de los tipos de técnicas de anestesia general sin opciones para la institución
		forma.setListadoTecAnestesiaRegional (TecnicaAnestesia.consultarTipoParametrizado(con, usuario.getCodigoInstitucionInt(), 3));
		
		//Se carga la información de la sección técnica de anestesia
		
		//logger.info("\n numsol->"+forma.getNumeroSolicitud());
		forma.setMapaTecAnestesia( TecnicaAnestesia.cargarTecnicaAnestesiaGeneralRegional(con, forma.getNumeroSolicitud()));
		
		//Validacion para mostrar informacion 
		if(forma.getMapaTecAnestesia().isEmpty() 
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay informacion tecnica anestesia");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
			
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tecnicaAnestesiaGeneralRegional");
	}

	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param TecnicaAnestesiaForm forma 
	 * */
	private void validacionMostrarInfo(TecnicaAnestesiaForm forma,HashMap mapa)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((mapa.isEmpty() || (mapa.containsKey("numRegistros") && mapa.get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay información Tecnica de Anestesia ");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{
			logger.info("no paso validaciones mostrar informacion. Tecnica de Anestesia. mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
			return;	
		}		
	}
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param TecnicaAnestesiaForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(TecnicaAnestesiaForm forma,HttpServletRequest request)
	{
		if (!(request.getParameter("esSinEncabezado")+"").equals("") && !(request.getParameter("esSinEncabezado")+"").equals("null"))
			forma.setEsSinEncabezado(UtilidadTexto.getBoolean(request.getParameter("esSinEncabezado")+"")+"");
		else
			forma.setEsSinEncabezado(ConstantesBD.valorFalseLargoString);
		
		if (!(request.getParameter("mostrarDatosInfoActivo")+"").equals("") && !(request.getParameter("mostrarDatosInfoActivo")+"").equals("null"))
			forma.setMostrarDatosInfoActivo(UtilidadTexto.getBoolean(request.getParameter("mostrarDatosInfoActivo")+""));
		else
			forma.setMostrarDatosInfoActivo(false);		
				
		if (!(request.getParameter("ocultarMenu")+"").equals("") && !(request.getParameter("ocultarMenu")+"").equals("null"))
			forma.setOcultarMenu(UtilidadTexto.getBoolean(request.getParameter("ocultarMenu")+""));
		else
			forma.setOcultarMenu(false);
	}
}