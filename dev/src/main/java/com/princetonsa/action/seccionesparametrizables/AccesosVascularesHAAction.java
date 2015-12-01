package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
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
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.seccionesparametrizables.AccesosVascularesHAForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.parametrizacion.AccesosVascularesHA;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author wilson
 *
 */
public class AccesosVascularesHAAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(AccesosVascularesHAAction.class);
    
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
    		if(form instanceof AccesosVascularesHAForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			AccesosVascularesHAForm forma =(AccesosVascularesHAForm)form;
    			String estado=forma.getEstado();


    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en ACCESOS VASCULARES es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de ACCESOS VASCULARES (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar") || estado.equals("resumen"))
    			{
    				this.accionEmpezar(forma,request, mapping, con, usuario);
    				return mapping.findForward("listadoAccesosVascularesHA");
    			}
    			else if(estado.equals("resumenCompleto"))
    			{
    				this.accionEmpezar(forma,request, mapping, con, usuario);
    				return mapping.findForward("resumenCompleto");
    			}
    			else if(estado.equals("nuevo"))
    			{
    				return this.accionNuevo(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("modificar"))
    			{
    				return this.accionModificar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("continuar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("accesosVascularesHA");
    			}
    			else if(estado.equals("cambieTipoAccesoVascular"))
    			{
    				return this.accionCambieTipoAccesoVascular(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardar(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("eliminar"))
    			{
    				return this.accionEliminar(forma, mapping, con, usuario);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de ACCESOS VASCULARES");
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
     * @param request
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private void accionEmpezar(AccesosVascularesHAForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{		
		if(request != null)
			inicializarParametrosRequest(forma,request);
				
		forma.setMapaListado(AccesosVascularesHA.obtenerListadoAccesosVasculares(con, forma.getNumeroSolicitud()));
		validacionMostrarInfo(forma,forma.getMapaListado());
		UtilidadBD.closeConnection(con);		
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(AccesosVascularesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		HashMap<Object, Object> listado= (HashMap)forma.getMapaListado().clone();
		forma.reset();
		forma.setMapaListado(listado);
		forma.setMapaTiposAccesosVasculares(AccesosVascularesHA.cargarTiposAccesoVascularCCInst(con, forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("accesosVascularesHA");
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificar(AccesosVascularesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setCodigoPKAccesoVAscularHA(Double.parseDouble(forma.getMapaListado("cod_acc_vascular_hoja_anes_"+forma.getIndex())+""));
		forma.setEstaBD(true);
		
		forma.setArticulo(Integer.parseInt(forma.getMapaListado("articulo_"+forma.getIndex())+""));
		forma.setDescArticulo(forma.getMapaListado("descarticulo_"+forma.getIndex())+"");
		forma.setEspos(forma.getMapaListado("espos_"+forma.getIndex())+"");
		forma.setCantidadArticulo(Integer.parseInt(forma.getMapaListado("cantidad_"+forma.getIndex())+""));
		forma.setFecha(forma.getMapaListado("fecha_"+forma.getIndex())+"");
		forma.setHora(forma.getMapaListado("hora_"+forma.getIndex())+"");
		forma.setLocalizacion(Integer.parseInt(forma.getMapaListado("localizacion_"+forma.getIndex())+""));
		forma.setTipoAccesoVascular(Integer.parseInt(forma.getMapaListado("tipoaccesovascular_"+forma.getIndex())+""));
		forma.setGeneroConsumo(UtilidadTexto.getBoolean(forma.getMapaListado("generoconsumo_"+forma.getIndex())+""));
		forma.setGeneroOrden(UtilidadTexto.getBoolean(forma.getMapaListado("generoorden_"+forma.getIndex())+""));
		forma.setCodigoDetMateQx(Utilidades.convertirAEntero(forma.getMapaListado("codigodetmateqx_"+forma.getIndex())+"")+"");
		
		forma.setMapaLocalizacionesMap(AccesosVascularesHA.cargarLocalizacionesXTipoAcceso(con, forma.getTipoAccesoVascular(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		forma.setMapaTiposAccesosVasculares(AccesosVascularesHA.cargarTiposAccesoVascularCCInst(con, forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		
		logger.info("genero consumo-->"+forma.getGeneroConsumo());
		logger.info("genero orden-->"+forma.getGeneroOrden());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("accesosVascularesHA");
	}
    
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(AccesosVascularesHAForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) throws IPSException 
	{
		HashMap<Object, Object> mapaInsercion= new HashMap<Object, Object>();
		mapaInsercion.put("numerosolicitud", forma.getNumeroSolicitud());
		mapaInsercion.put("fecha", forma.getFecha());
		mapaInsercion.put("hora", forma.getHora());
		mapaInsercion.put("tipoaccesovascular", forma.getTipoAccesoVascular());
		mapaInsercion.put("articulo", forma.getArticulo());
		mapaInsercion.put("cantidad", forma.getCantidadArticulo());
		mapaInsercion.put("localizacion", forma.getLocalizacion());
		mapaInsercion.put("usuariomodifica", usuario.getLoginUsuario());
		mapaInsercion.put("codigo", forma.getCodigoPKAccesoVAscularHA());
		mapaInsercion.put("generoconsumo", ConstantesBD.acronimoNo);
		mapaInsercion.put("generoorden", ConstantesBD.acronimoNo);
		mapaInsercion.put("codigodetmateqx",forma.getCodigoDetMateQx());
		
		UtilidadBD.iniciarTransaccion(con);
		
		boolean inserto=false;
		if(!forma.getEstaBD())
		{
			mapaInsercion.put("codigodetmateqx",ConstantesBD.codigoNuncaValido);
			inserto=AccesosVascularesHA.insertar(con, mapaInsercion)>0;
			formatoJustificacionNoPos(con, forma, usuario);				
		}
		else
		{
			inserto=AccesosVascularesHA.modificar(con, mapaInsercion);
			formatoJustificacionNoPos(con, forma, usuario);
		}
		
		if(!inserto)
			UtilidadBD.abortarTransaccion(con);
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		
		///probar la generacion de la orden
		/*UtilidadBD.iniciarTransaccion(con);
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		if(!AccesosVascularesHA.generarOrdenProcedimientosAccesosVasculares(con, forma.getNumeroSolicitud(), usuario, paciente))
			UtilidadBD.abortarTransaccion(con);
		else
			UtilidadBD.finalizarTransaccion(con);*/
		///fin prueba generacion de la orden
		
		forma.setEstado("resumen");
		this.accionEmpezar(forma,null, mapping, con, usuario);
		return mapping.findForward("listadoAccesosVascularesHA");
	}
	
	
	
	public void formatoJustificacionNoPos(Connection con, AccesosVascularesHAForm forma, UsuarioBasico usuario) throws IPSException
	{
		//%%%%%%%% FORMATO JUSTIFICACION NO POS %%%%%%%%
		int numRegistros = Integer.parseInt(forma.getMapaTiposAccesosVasculares().get("numRegistros").toString());
		FormatoJustArtNopos fjan=new FormatoJustArtNopos();
		
		for(int w = 0; w < numRegistros; w++ )
		{
			if(forma.getEspos().toString().equals("NOPOS"))
			{
				if(forma.getJustificacionMap().get(forma.getArticulo()+"_pendiente").equals("0"))
				{
					double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, forma.getArticulo(), forma.getNumeroSolicitud(), ConstantesBD.codigoNuncaValido, true);
					
					UtilidadJustificacionPendienteArtServ.insertarJusNP(con, forma.getNumeroSolicitud(), forma.getArticulo(), forma.getCantidadArticulo(), usuario.getLoginUsuario(), true, false, Utilidades.convertirAEntero(subcuenta+""),"");	
				}
				else
					if(forma.getJustificacionMap().get(forma.getArticulo()+"_pendiente").equals("1")){
						//la frecuencia estaba en 0
						fjan.insertarJustificacion(	con,
													forma.getNumeroSolicitud(),
													ConstantesBD.codigoNuncaValido,
													forma.getJustificacionMap(),
													forma.getMedicamentosNoPosMap(),
													forma.getMedicamentosPosMap(),
													forma.getSustitutosNoPosMap(),
													forma.getDiagnosticosDefinitivos(),
													forma.getArticulo(),
													usuario.getCodigoInstitucionInt(), 
													"", 
													ConstantesBD.continuarTransaccion,
													forma.getArticulo(), 
													"", 
													"0", 
													"", 
													0, 
													"", 
													"", 
													forma.getCantidadArticulo(), 
													"0",
													usuario.getLoginUsuario()
													);
				}
			}
		}
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(AccesosVascularesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setCodigoPKAccesoVAscularHA(Integer.parseInt(forma.getMapaListado("cod_acc_vascular_hoja_anes_"+forma.getIndex())+""));
		UtilidadBD.iniciarTransaccion(con);
		AccesosVascularesHA.eliminar(con, forma.getCodigoPKAccesoVAscularHA());
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		this.accionEmpezar(forma,null, mapping, con, usuario);
		return mapping.findForward("listadoAccesosVascularesHA");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCambieTipoAccesoVascular(AccesosVascularesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		//si entra a este flujo entonces fue que cambio el tipo de acceso vascular para lo cual se tiene que cargar la localizacion y logicamente resetear el articulo para que cargue uno que este ligado a la parametrizacion
		forma.setArticulo(ConstantesBD.codigoNuncaValido);
		forma.setDescArticulo("");
		forma.setCantidadArticulo(1);
		forma.setMapaLocalizacionesMap(AccesosVascularesHA.cargarLocalizacionesXTipoAcceso(con, forma.getTipoAccesoVascular(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("accesosVascularesHA");
	}
	
	
	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param InfoGeneralHAForm forma 
	 * */
	private void validacionMostrarInfo(AccesosVascularesHAForm forma,HashMap mapa)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((mapa.isEmpty() || (mapa.containsKey("numRegistros") && mapa.get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay información Accesos Vasculares");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{			
			logger.info("no paso validaciones mostrar informacion (Accesos Vasculares). mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);		
		}		
	}
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param AccesosVascularesHAForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(AccesosVascularesHAForm forma,HttpServletRequest request)
	{
		logger.info("valor del encabezado >> "+forma.getEsSinEncabezado());
		
		if (!(request.getParameter("esSinEncabezado")+"").equals("") && !(request.getParameter("esSinEncabezado")+"").equals("null"))
			forma.setEsSinEncabezado(request.getParameter("esSinEncabezado")+"");
		else if (!UtilidadTexto.getBoolean(forma.getEsSinEncabezado().toString()))
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
}