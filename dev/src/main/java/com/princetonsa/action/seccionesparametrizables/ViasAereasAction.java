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

import com.princetonsa.actionform.seccionesparametrizables.ViasAereasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.parametrizacion.ViasAereas;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author wilson
 *
 */
public class ViasAereasAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(ViasAereasAction.class);
    
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
    		if(form instanceof ViasAereasForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			ViasAereasForm forma =(ViasAereasForm)form;
    			String estado=forma.getEstado();


    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en VIAS AEREAS es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de VIAS AEREAS (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar") || estado.equals("resumen"))
    			{
    				return this.accionEmpezar(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("nuevo"))
    			{
    				return this.accionNuevo(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("modificar"))
    			{
    				return this.accionModificar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("accionConsultarDetalle"))
    			{
    				return this.accionConsultarDetalle(forma,request, mapping, con, usuario);    			
    			}    			
    			else if(estado.equals("continuar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("viasAereas");
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
    				logger.warn("Estado no valido dentro del flujo de VIAS AEREAS");
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
	private ActionForward accionEmpezar(ViasAereasForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset1();
		
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		forma.setMapaListado(ViasAereas.obtenerListadoViasAereas(con, forma.getNumeroSolicitud()));
		validacionMostrarInfo(forma,forma.getMapaListado());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoViasAereas");
	}
	
	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param InfoGeneralHAForm forma 
	 * */
	private void validacionMostrarInfo(ViasAereasForm forma,HashMap mapa)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((mapa.isEmpty() || (mapa.containsKey("numRegistros") && mapa.get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay información Vias Aereas ");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{			
			logger.info("no paso validaciones mostrar informacion (Vias Aereas). mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);		
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
	private ActionForward accionNuevo(ViasAereasForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		HashMap<Object, Object> listado= (HashMap)forma.getMapaListado().clone();
		forma.reset();
		forma.setMapaListado(listado);
		forma.setMapaTiposDispositivo(ViasAereas.cargarTiposDispositivosCCInst(con, forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("viasAereas");
	}
	
	/** 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificar(ViasAereasForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setCodigoViaAerea(Integer.parseInt(forma.getMapaListado("codigo_via_aerea_ha_"+forma.getIndex())+""));
		forma.setEstaBD(true);
		forma.setFecha(forma.getMapaListado("fecha_"+forma.getIndex())+"");
		forma.setHora(forma.getMapaListado("hora_"+forma.getIndex())+"");
		forma.setTipoDispositivo(Integer.parseInt(forma.getMapaListado("tipo_dispositivo_"+forma.getIndex())+""));
		forma.setMapaTiposDispositivo(ViasAereas.cargarTiposDispositivosCCInst(con, forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		forma.setMapa(ViasAereas.cargarViaAereaHojaAnestesia(con, forma.getCodigoViaAerea()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("viasAereas");
	}
	
	
	/** 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultarDetalle(ViasAereasForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{		
		forma.reset1();
		
		if(request != null)
			inicializarParametrosRequest(forma,request);
		
		forma.setMapa(ViasAereas.cargarViaAereaCompleta(con, forma.getNumeroSolicitud()));
		validacionMostrarInfo(forma,forma.getMapa());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCompleto");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ViasAereasForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) throws IPSException 
	{
		if(!forma.getEstaBD())
		{
			UtilidadBD.iniciarTransaccion(con);
			insertarEncabezadoDetalle(forma, mapping, con, usuario);
			//en este punto insertamos los anestesiologos
			HojaAnestesia hojaAnestesia= new HojaAnestesia();
			hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
			UtilidadBD.finalizarTransaccion(con);
			forma.setEstado("resumen");
			return this.accionEmpezar(forma,null, mapping, con, usuario);
		}
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			ViasAereas.eliminarDetalle(con, forma.getCodigoViaAerea(), ConstantesBD.codigoNuncaValido /*articulo*/);
			ViasAereas.eliminar(con, forma.getCodigoViaAerea());
			insertarEncabezadoDetalle(forma, mapping, con, usuario);
			//en este punto insertamos los anestesiologos
			HojaAnestesia hojaAnestesia= new HojaAnestesia();
			hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
			UtilidadBD.finalizarTransaccion(con);
			forma.setEstado("resumen");
			return this.accionEmpezar(forma,null, mapping, con, usuario);
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
	private void insertarEncabezadoDetalle(ViasAereasForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) throws IPSException
	{
		HashMap<Object, Object> mapaEncabezado= new HashMap<Object, Object>();
		mapaEncabezado.put("numerosolicitud", forma.getNumeroSolicitud());
		mapaEncabezado.put("tipodispositivo", forma.getTipoDispositivo());
		mapaEncabezado.put("fecha", forma.getFecha());
		mapaEncabezado.put("hora", forma.getHora());
		mapaEncabezado.put("institucion", usuario.getCodigoInstitucionInt());
		mapaEncabezado.put("usuariomodifica", usuario.getLoginUsuario());
		int codigoEncabezado=ViasAereas.insertar(con, mapaEncabezado);
		
		for(int w=0; w<Integer.parseInt(forma.getMapa("numRegistros")+"");w++)
		{
			if(!UtilidadTexto.getBoolean(forma.getMapa("fueeliminadoarticulo_"+w)+""))
    		{
				HashMap<Object, Object> mapaDetalle= new HashMap<Object, Object>();
				mapaDetalle.put("viaaerea", codigoEncabezado);
				mapaDetalle.put("articulo", forma.getMapa("codigoarticulo_"+w));
				mapaDetalle.put("cantidad", forma.getMapa("cantidad_"+w));
				mapaDetalle.put("viainserciondisp", forma.getMapa("viainsercion_"+w));
				mapaDetalle.put("generaconsumo", forma.getMapa("generaconsumo_"+w));
				mapaDetalle.put("codigodetmateqx", forma.getMapa("codigodetmateqx_"+w)+"");
				if(ViasAereas.insertarDetalle(con, mapaDetalle))
				{
					//%%%%%%%% FORMATO JUSTIFICACION NO POS %%%%%%%%
					FormatoJustArtNopos fjan=new FormatoJustArtNopos();
					
					if(forma.getMapa("tipoposarticulo_"+w).toString().equals("NOPOS") || forma.getMapa("tipoposarticulo_"+w).toString().equals("f"))
					{
						if(forma.getJustificacionMap().get(forma.getMapa("codigoarticulo_"+w)+"_pendiente").equals("0"))
						{
							
							double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, Integer.parseInt(forma.getMapa("codigoarticulo_"+w).toString()), forma.getNumeroSolicitud(), ConstantesBD.codigoNuncaValido, true);
							
							UtilidadJustificacionPendienteArtServ.insertarJusNP(con, forma.getNumeroSolicitud(), Integer.parseInt(forma.getMapa("codigoarticulo_"+w).toString()), Integer.parseInt(forma.getMapa("cantidad_"+w).toString()), usuario.getLoginUsuario(), true, false, Utilidades.convertirAEntero(subcuenta+""),"");	
						}
						else
							if(forma.getJustificacionMap().get(forma.getMapa("codigoarticulo_"+w)+"_pendiente").equals("1")){

								//frecuencia estaba en 0 int
								
								fjan.insertarJustificacion(	con,
															forma.getNumeroSolicitud(),
															ConstantesBD.codigoNuncaValido,
															forma.getJustificacionMap(),
															forma.getMedicamentosNoPosMap(),
															forma.getMedicamentosPosMap(),
															forma.getSustitutosNoPosMap(),
															forma.getDiagnosticosDefinitivos(),
															Integer.parseInt(forma.getMapa("codigoarticulo_"+w).toString()),
															usuario.getCodigoInstitucionInt(), 
															"", 
															ConstantesBD.continuarTransaccion,
															Integer.parseInt(forma.getMapa("codigoarticulo_"+w).toString()), 
															"", 
															"0", 
															"", 
															0, 
															"", 
															"", 
															Integer.parseInt(forma.getMapa("cantidad_"+w).toString()), 
															"0",
															usuario.getLoginUsuario()
															);
						}
					}
					//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
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
	private ActionForward accionEliminar(ViasAereasForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setCodigoViaAerea(Integer.parseInt(forma.getMapaListado("codigo_via_aerea_ha_"+forma.getIndex())+""));
		UtilidadBD.iniciarTransaccion(con);
		ViasAereas.eliminarDetalle(con, forma.getCodigoViaAerea(), ConstantesBD.codigoNuncaValido /*articulo*/);
		ViasAereas.eliminar(con, forma.getCodigoViaAerea());
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		return this.accionEmpezar(forma,null, mapping, con, usuario);
	}
	
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param ViasAereasForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(ViasAereasForm forma,HttpServletRequest request)
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
}