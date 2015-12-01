package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.AjustesXInventarioFisicoForm;
import com.princetonsa.actionform.inventarios.ComparativoUltimoConteoForm;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AjustesXInventarioFisico;
import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;
import com.princetonsa.mundo.inventarios.TransaccionesValidasXCC;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * Clase para el manejo del workflow de ajustes por inventario fisico
 * Date: 2008-02-21
 * @author cgarias@princetonsa.com
 */
public class AjustesXInventarioFisicoAction extends Action {
	
	/**
	 * logger 
	 * */
	static Logger logger = Logger.getLogger(AjustesXInventarioFisicoAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{

    		if(response==null);
    		if(form instanceof AjustesXInventarioFisicoForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			AjustesXInventarioFisicoForm forma = (AjustesXInventarioFisicoForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO SECCIONES ---->"+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
    				return this.accionEmpezar(forma, con, mapping, usuario,request);
    			}
    			else if(estado.equals("cargarAlmacenes"))
    			{    			
    				return this.accionAlmacenes(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("cargarClases"))
    			{    			
    				return this.accionCargarClases(forma, con, mapping, usuario);    			
    			}
    			else if(estado.equals("cargarGrupos"))
    			{
    				return this.accionCargarGrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarSubgrupos"))
    			{
    				return this.accionCargarSubgrupos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("adicionarArticulo"))
    			{
    				return this.accionAdicionarArticulo(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("eliminarArticulo"))
    			{
    				return this.accionEliminarArticulo(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("filtrarArticulos"))
    			{
    				return this.accionFiltrarArticulos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("volverFiltro"))
    			{
    				return mapping.findForward("principal");
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO ==> ORDENAR
				 -------------------------------*/
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("articulosFiltrados");
    				}
    				else if(estado.equals("ajustar"))
    				{
    					return this.accionAjustar(forma,request,con,mapping, usuario);
    				}	
    				else if(estado.equals("imprimir"))
    				{
    					return this.generarReporte(con, forma, mapping, request, usuario);
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
    
    private ActionForward accionAlmacenes(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	forma.setAlmacen(ConstantesBD.codigoNuncaValido);
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		forma.setClase(ConstantesBD.codigoNuncaValido);
		forma.setClasesMap(new HashMap());
		forma.setGruposMap(new HashMap());
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setClasesMap("numRegistros", 0);
		forma.setGruposMap("numRegistros", 0);
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setCentrosAtencionMap(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("principal");
	}

	private ActionForward accionAjustar(AjustesXInventarioFisicoForm forma, HttpServletRequest request,Connection con, ActionMapping mapping, UsuarioBasico usuario) {

    	AjustesXInventarioFisico axif = new AjustesXInventarioFisico();
    	int codPrep, tipoAjuste;
    	ActionErrors errores = new ActionErrors();
    	boolean errorValidacion = false;  
    	boolean tipoTrans = false;
    	boolean ajuste = false;
    	int numAjuste=0;
    	int codigoAlmacen=forma.getAlmacen();
    	String codigosAjustados="";
    	int aux=ConstantesBD.codigoNuncaValido;
    	
		axif.setCentroAtencion(forma.getCentroAtencion());
		axif.setAlmacen(forma.getAlmacen());
		axif.setUsuarioModifica(usuario.getLoginUsuario());
		axif.setInstitucion(usuario.getCodigoInstitucionInt());
    	axif.setArticulosFiltradosMap(forma.getArticulosFiltradosMap());

    	for(int x=0; x<Integer.parseInt(forma.getArticulosFiltradosMap().get("numRegistros").toString()); x++)
		{
    		if (forma.getArticulosFiltradosMap().containsKey("activo_"+x) && forma.getArticulosFiltradosMap().get("activo_"+x).toString().equals(ConstantesBD.acronimoSi))
    		{
    			if (Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString())!=0){
    				
    				/*
    				logger.info("------ 3 ");
    				if (Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString())>0){
    					logger.info("------ 4>0 ----"+forma.getGruposClasesValidasEntrada().get("numRegistros"));
    					//Utilidades.imprimirMapa(forma.getGruposClasesValidasEntrada());
    					
    					
    					//for (int g=0; g<Integer.parseInt(forma.getGruposClasesValidasEntrada().get("numRegistros").toString());g++){
    						logger.info("------ 5>0 ");
    						logger.info("Clase: "+forma.getGruposClasesValidasEntrada().get("cod_clase_inventario_"+g)+" = "+forma.getArticulosFiltradosMap().get("clase_"+x));
    						logger.info("Grupo: "+forma.getGruposClasesValidasEntrada().get("cod_grupo_inventario_"+g)+" = "+forma.getArticulosFiltradosMap().get("grupo_"+x));
    						//if (forma.getGruposClasesValidasEntrada().get("cod_clase_inventario_"+g).toString().equals(forma.getArticulosFiltradosMap().get("clase_"+x)+"") && forma.getGruposClasesValidasEntrada().get("cod_grupo_inventario_"+g).toString().equals(forma.getArticulosFiltradosMap().get("grupo_"+x)+"")){
    							logger.info("------ 6>0 ");
    							ajustar = true;
    						//}
    					//}
    				}
    				
    				if (Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString())<0){
    					logger.info("------ 4<0 ----"+forma.getGruposClasesValidasSalida().get("numRegistros"));
    					//Utilidades.imprimirMapa(forma.getGruposClasesValidasSalida());
    					for (int g=0; g<Integer.parseInt(forma.getGruposClasesValidasSalida().get("numRegistros").toString());g++){
    						logger.info("------ 5<0 ");
    						logger.info("indice g: "+g);
    						logger.info("Clase: "+forma.getGruposClasesValidasSalida().get("cod_clase_inventario_"+g)+" = "+forma.getArticulosFiltradosMap().get("clase_"+x));
    						logger.info("Grupo: "+forma.getGruposClasesValidasSalida().get("cod_grupo_inventario_"+g)+" = "+forma.getArticulosFiltradosMap().get("grupo_"+x));
    						if (forma.getGruposClasesValidasSalida().get("cod_clase_inventario_"+g).toString().equals(forma.getArticulosFiltradosMap().get("clase_"+x)+"") && forma.getGruposClasesValidasSalida().get("cod_grupo_inventario_"+g).toString().equals(forma.getArticulosFiltradosMap().get("grupo_"+x)+"")){
    							logger.info("------ 6<0 ");
    							ajustar = true;
    						}
    					}
    				}
    				
    				logger.info("ajustar -- "+ajustar);*/
    				
	    			axif.setCodTransaccionEntrada(forma.getCodTransaccionEntrada());
	    			axif.setCodTransaccionSalida(forma.getCodTransaccionSalida());
	    			axif.setIndexMap(x);
	    	    	
	    	    	int tipoTransaccion1=ConstantesBD.codigoNuncaValido;
	    	    	
	    	    	//	Variables para el ajuste automático
	    	    	
	    	    	if (Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString())>0){
	    	    		if (forma.getCodTransaccionEntrada() == ConstantesBD.codigoNuncaValido){
	    	    			errores.add("conceptoEntrada",new ActionMessage("errors.required","CONCEPTO PARA AJUSTE DE ENTRADA" ));
				            errorValidacion = true;
	    	    		} else {
	    	    			tipoTransaccion1 = forma.getCodTransaccionEntrada();
		    	    		tipoTrans = true;
		    	    		forma.getArticulosFiltradosMap().put("tip_ajuste_"+x, "Entrada");
	    	    		}
	    	    	}	
	    	    	else{
	    	    		if (forma.getCodTransaccionSalida() == ConstantesBD.codigoNuncaValido){
	    	    			errores.add("conceptoSalida",new ActionMessage("errors.required","CONCEPTO PARA AJUSTE DE SALIDA" ));
				            errorValidacion = true;		                      
	    	    		} else {
	    	    			tipoTransaccion1 = forma.getCodTransaccionSalida();
	    	    			forma.getArticulosFiltradosMap().put("tip_ajuste_"+x, "Salida");
	    	    		}
	    	    	}
	    	    	
	    	    	if(!errorValidacion) {
			          
	    	    		forma.getArticulosFiltradosMap().put("desc_ajuste_"+x, axif.consultarDescripcionAjuste(con, tipoTransaccion1));
		    	    	
		    	    	String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
		    	    	
		    	    	
		    	    	ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
		    	    	String consecutivo="";
		    	    	boolean transaccionExitosa=false;
		     
		    	    	
		    	    	int codigoAlmacenConsecutivo=0;
			            
			            if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
			            	codigoAlmacenConsecutivo=ConstantesBD.codigoNuncaValido;        
			            else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
			            	codigoAlmacenConsecutivo=forma.getAlmacen(); 
			            
	
		    	    	consecutivo=consec.obtenerConsecutivoInventario(con,tipoTransaccion1,codigoAlmacenConsecutivo,usuario.getCodigoInstitucionInt())+"";
		    	    	
		    	    	if(Integer.parseInt(consecutivo)==ConstantesBD.codigoNuncaValido)
			            {
			            	errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivoTransAlmacen",UtilidadInventarios.obtenerNombreTipoTransaccion(con,tipoTransaccion1,usuario.getCodigoInstitucionInt()),Utilidades.obtenerNombreCentroCosto(con, codigoAlmacenConsecutivo, usuario.getCodigoInstitucionInt())));
				            errorValidacion = true;		                      
			            }
		    	    	else
		    	    	{	
		    	    		logger.info("@@@@@@@@@@@@@@@@@@2 PASA POR AKI!!!!!!!!!! ");
		    	    		// Regitrar Ajuste
		    	    		
		    	    		aux = axif.registrarAjuste(con, axif);
		    	    		forma.getArticulosFiltradosMap().put("num_ajuste_"+x, aux);
		    	    		codigosAjustados += aux+", ";
		    	    		ajuste = true;
			    	    	
			    	    	int codTransaccion1=UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),tipoTransaccion1,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,codigoAlmacen,true);
						     
			    	    	// Realizar ajuste automático
			    	    	
				            consec.actualizarValorConsecutivoInventarios(con,tipoTransaccion1,codigoAlmacen,usuario.getCodigoInstitucionInt());
		
				        	//NO MANEJA PROVEEDOR DE COMPRA NI PROVEEDOR CATALOGO, SE ENVIA EN BLANCO
				            transaccionExitosa = UtilidadInventarios.insertarDetalleTransaccion(con,codTransaccion1,Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_"+x).toString()),Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString()),forma.getArticulosFiltradosMap().get("costo_promedio_"+x).toString(),forma.getArticulosFiltradosMap().get("lote_"+x).toString(),forma.getArticulosFiltradosMap().get("fecha_vencimiento_"+x).toString(),"","");
				            transaccionExitosa = UtilidadInventarios.generarRegistroCierreTransaccion(con,codTransaccion1+"",usuario.getLoginUsuario(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
				            try
				            {
					            if(Articulo.articuloManejaLote(con, Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_"+x).toString()), usuario.getCodigoInstitucionInt()))
							    	transaccionExitosa = UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(con,Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_"+x).toString()),codigoAlmacen,tipoTrans,Math.abs(Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString())),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion,forma.getArticulosFiltradosMap().get("lote_"+x).toString(),forma.getArticulosFiltradosMap().get("fecha_vencimiento_"+x).toString());        		
					        	else
					        		transaccionExitosa = UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(con,Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_"+x).toString()),codigoAlmacen,tipoTrans,Math.abs(Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString())),usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
				            }
				            catch(SQLException e)
				            {
				            	logger.error("Error al actualizar las existencias del articulo x almacen: "+e);
				            	transaccionExitosa = false;
				            }
		    	    	}  
    				}
	    	    	if(errorValidacion)
		            {
		            	saveErrors(request,errores);	
		            	UtilidadBD.closeConnection(con);
		    	        return mapping.findForward("articulosFiltrados");
		            }
	    	    	axif.finalizarPreparacion(con, codigoAlmacen, Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_"+x).toString()), Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_lote_"+x).toString()));
	    			forma.getArticulosFiltradosMap().put("codigos_ajustados", codigosAjustados);
    			
	    	    	
    			}
    			
    		}
    		if (Integer.parseInt(forma.getArticulosFiltradosMap().get("cant_ajuste_"+x).toString())==0){
    			axif.finalizarPreparacion(con, codigoAlmacen, Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_"+x).toString()), Integer.parseInt(forma.getArticulosFiltradosMap().get("codigo_lote_"+x).toString()));
    		}
		}
    	if(!codigosAjustados.equals("")){
    		UtilidadBD.closeConnection(con);
    		return mapping.findForward("listadoAjustes");
    	}
    	UtilidadBD.closeConnection(con);	
    	return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param con
	 * @return
	 */
    private static boolean insertarPreparacion(ComparativoUltimoConteoForm forma, UsuarioBasico usuario, Connection con)
	{
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
    	pti.setCentroAtencion(forma.getCentroAtencion());
		pti.setAlmacen(forma.getAlmacen());
		pti.setArticulosFiltradosMap(forma.getArticulosFiltradosMap());
		pti.setUsuarioModifica(usuario.getLoginUsuario());
		pti.setFecha_toma(forma.getFecha());
		pti.setHora_toma(forma.getHora());
		pti.setInstitucion(usuario.getCodigoInstitucionInt());
		
		UtilidadBD.closeConnection(con);	
		return pti.confirmarPreparacion(con, pti);
	}
	

	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */

	public static void accionOrdenarMapa(AjustesXInventarioFisicoForm forma)
	{
		
		
		String[] indices = {"descripcion_","codigo_","unidad_medida_","codigo_interfaz_","lote_","existencia_","conteo_","costo_promedio_","cant_ajuste_","cant_conteo_","costo_total_","cod_conteo_","fecha_vencimiento_"};
		
		int numReg = Integer.parseInt(forma.getArticulosFiltradosMap().get("numRegistros").toString());
		
		forma.setArticulosFiltradosMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getArticulosFiltradosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.getArticulosFiltradosMap().put("numRegistros",numReg+"");
		forma.getArticulosFiltradosMap().put("INDICES_MAPA",indices+"");		
		logger.info("############ "+forma.getArticulosFiltradosMap());
		
		logger.info(">>>>>>>>>>><"+forma.getRompimiento()+">>>>>>>>>>>>"+forma.getPatronOrdenar());
	}

   /**
    * 
    * @param forma
    * @param con
    * @param mapping
    * @param usuario
    * @return
    */
	private ActionForward accionFiltrarArticulos(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		AjustesXInventarioFisico axif = new AjustesXInventarioFisico();
		
		axif.setCentroAtencion(forma.getCentroAtencion());
		axif.setAlmacen(forma.getAlmacen());
		axif.setClase(forma.getClase());
		axif.setGrupo(forma.getGrupo());
		axif.setSubgrupo(forma.getSubgrupo());
		axif.setInstitucion(usuario.getCodigoInstitucionInt());
		axif.setCodigosArticulos(forma.getArticulosMap().get("codigosArticulos").toString());
		
		
		logger.info("Almacen Consignación ->"+forma.getAlmacen());
		
		forma.setAlmacenConsignacion(axif.consultarParametroAlmacenConsignacion(con, forma.getAlmacen()));
		
		logger.info("PARAMETRO ALMACEN CONSIGNACION ->"+forma.getAlmacenConsignacion());
		
		HashMap mapa = axif.filtrarArticulos(con, axif);
		HashMap mapa2 = axif.transaccionesValidas(con,axif, forma.getAlmacenConsignacion());
		
		logger.info("------------- "+ValoresPorDefecto.getConceptoParaAjusteEntrada(usuario.getCodigoInstitucionInt(), true));
		logger.info("------------- "+ValoresPorDefecto.getConceptoParaAjusteSalida(usuario.getCodigoInstitucionInt(), true));
		logger.info("------------- "+ValoresPorDefecto.getPermitirModificarConceptosAjuste(usuario.getCodigoInstitucionInt()));
		
		
		
		if(!ValoresPorDefecto.getConceptoParaAjusteEntrada(usuario.getCodigoInstitucionInt(), false).equals("@@")){
			forma.setCodTransaccionEntrada(Integer.parseInt(ValoresPorDefecto.getConceptoParaAjusteEntrada(usuario.getCodigoInstitucionInt(), true)));	
			forma.setNombreTransaccionEntrada(Utilidades.obtenerNombreTransaccionInventario(con, forma.getCodTransaccionEntrada()));
			logger.info("******************** "+forma.getCodTransaccionEntrada());
			forma.setExisteParamTransaccionEntradaXCentroCosto(true);
			
			/*forma.setTransaccionesValidasMap(TransaccionesValidasXCC.generarConsultaStatic(con,usuario.getCodigoInstitucionInt(),forma.getAlmacen(),forma.getCodTransaccionEntrada()));
			//Utilidades.imprimirMapa(forma.getTransaccionesValidasMap());
			for(int x=0; x<Integer.parseInt(forma.getTransaccionesValidasMap("numRegistros")+"");x++){
				if (forma.getTransaccionesValidasMap("cod_tipos_trans_inventario_"+x).toString().equals(forma.getCodTransaccionEntrada()+""))
					forma.setExisteParamTransaccionEntradaXCentroCosto(true);
			}*/
		}	
		
		if(!ValoresPorDefecto.getConceptoParaAjusteSalida(usuario.getCodigoInstitucionInt(), false).equals("@@")){
			forma.setCodTransaccionSalida(Integer.parseInt(ValoresPorDefecto.getConceptoParaAjusteSalida(usuario.getCodigoInstitucionInt(), true)));
			forma.setNombreTransaccionSalida(Utilidades.obtenerNombreTransaccionInventario(con, forma.getCodTransaccionSalida()));
			logger.info("******************** "+forma.getCodTransaccionSalida());
			forma.setExisteParamTransaccionSalidaXCentroCosto(true);
			
			/*
			forma.setTransaccionesValidasMap(TransaccionesValidasXCC.generarConsultaStatic(con,usuario.getCodigoInstitucionInt(),forma.getAlmacen(),forma.getCodTransaccionSalida()));
			//Utilidades.imprimirMapa(forma.getTransaccionesValidasMap());
			for(int x=0; x<Integer.parseInt(forma.getTransaccionesValidasMap("numRegistros")+"");x++){
				if (forma.getTransaccionesValidasMap("cod_tipos_trans_inventario_"+x).toString().equals(forma.getCodTransaccionSalida()+""))
					forma.setExisteParamTransaccionSalidaXCentroCosto(true);
			}*/
		}
			
		forma.setPermitirModificarConceptosAjuste(ValoresPorDefecto.getPermitirModificarConceptosAjuste(usuario.getCodigoInstitucionInt()));
		
		forma.setGruposClasesValidasEntrada(TransaccionesValidasXCC.generarConsultaStatic(con,usuario.getCodigoInstitucionInt(),forma.getAlmacen(),forma.getCodTransaccionEntrada()));
		forma.setGruposClasesValidasSalida(TransaccionesValidasXCC.generarConsultaStatic(con,usuario.getCodigoInstitucionInt(),forma.getAlmacen(),forma.getCodTransaccionSalida()));
		
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		//Utilidades.imprimirMapa(forma.getGruposClasesValidasEntrada());
		//Utilidades.imprimirMapa(forma.getGruposClasesValidasSalida());
		
		forma.setArticulosFiltradosMap(mapa);
		forma.setTransaccionesValidasMap(mapa2);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("articulosFiltrados");
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarArticulo(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		HashMap mapa = forma.getArticulosMap();
		String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
		
		codigosArticulos = codigosArticulos.replaceAll(mapa.get("codigo_"+forma.getIndexMap())+",", "");
		
		eliminarRegistroMapaArticulos(mapa, Integer.parseInt(forma.getIndexMap().toString()));
		
		forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarArticulo(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	
		if(!forma.getDescripcionArticulo().equals("")){
			HashMap mapa = forma.getArticulosMap();
	    	String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
	    	int pos=Integer.parseInt(forma.getArticulosMap().get("numRegistros")+"");
	    	
	     	mapa.put("codigo_"+pos,forma.getCodigoArticulo());
	     	mapa.put("descripcion_"+pos,forma.getDescripcionArticulo());
	    	mapa.put("numRegistros", (pos+1)+"");
	    	
			codigosArticulos += forma.getArticulosMap().get("codigo_"+pos).toString() + ",";
			forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
			
			forma.setDescripcionArticulo("");
		}
		
		UtilidadBD.closeConnection(con);		
        return mapping.findForward("principal");
	}
    
    /**
     * 
     * @param mapa
     * @param pos
     * @return
     */
	private HashMap eliminarRegistroMapaArticulos(HashMap mapa, int pos){
    	int aux=pos+1;
		
    	for(int x=pos; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++)
		{
    		
    		mapa.put("codigo_"+x, mapa.get("codigo_"+aux));
    		mapa.put("descripcion_"+x, mapa.get("descripcion_"+aux));
    		aux++;
		}
    	aux = Integer.parseInt(mapa.get("numRegistros").toString());
    	mapa.remove("codigo_"+aux);
    	mapa.remove("descripcion_"+aux);
    	mapa.put("numRegistros", aux-1);
    	
    	return mapa;
    }
    
    /**
     * 
     * @param mapa
     * @return
     */
	private HashMap crearCadenaConComas(HashMap mapa){
    	String cadena = "";
    	int x;
    	for(x=0; x<Integer.parseInt(mapa.get("numRegistros").toString())-1; x++){
    		cadena += mapa.get("codigo_pk_"+x);
    		cadena += ",";
    	}
    	cadena += mapa.get("codigo_pk_"+x);
    	mapa.put("cadenaCodigos", cadena);
    	return mapa;
    }

	private ActionForward generarReporte(Connection con, AjustesXInventarioFisicoForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
	   	
    	String nombreRptDesign = "AjustesXInventarioFisico.rptdesign";

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        for(int x=0; x<Integer.parseInt(forma.getAlmacenesMap().get("numRegistros")+"");x++)
		{
			if (Integer.parseInt(forma.getAlmacenesMap().get("codigo_"+x).toString()) == forma.getAlmacen())
			{
				comp.insertLabelInGridPpalOfHeader(2,0, "GENERACIÓN AJUSTES AUTOMÁTICOS DE INVENTARIO FÍSICO \n "+usuario.getCentroAtencion()+" - "+forma.getAlmacenesMap().get("nombre_"+x));
			}
		}
        
        comp.insertLabelInGridPpalOfHeader(2,0, "Usuario Responsable: "+usuario.getLoginUsuario());
        
        
 String parame="";
        
        if (forma.getClase()!=ConstantesBD.codigoNuncaValido)
        {
        
        	for(int x=0; x<Integer.parseInt(forma.getClasesMap().get("numRegistros")+"");x++)
    		{
    			if (Integer.parseInt(forma.getClasesMap().get("codigoclaseinventario_"+x).toString()) == forma.getClase())
    			{
    				parame+="Parámetros de generación del listado \n [ Clase: "+forma.getClasesMap().get("nombreclaseinventario_"+x)+" ] ";
    			}
    		}
        	
        	if(forma.getGrupo()!=ConstantesBD.codigoNuncaValido)
        	{
        		for(int x=0; x<Integer.parseInt(forma.getGruposMap().get("numRegistros")+"");x++)
        		{
        			if (Integer.parseInt(forma.getGruposMap().get("codigo_"+x).toString()) == forma.getGrupo())
        			{
        				parame+="- [ Grupo: "+forma.getGruposMap().get("nombre_"+x)+" ] ";
        			}
        		}
        		
        		
        		if(forma.getSubgrupo()!=ConstantesBD.codigoNuncaValido)
        		{
            		for(int x=0; x<Integer.parseInt(forma.getSubgruposMap().get("numRegistros")+"");x++)
            		{
            			if (Integer.parseInt(forma.getSubgruposMap().get("codigo_"+x).toString()) == forma.getSubgrupo())
            			{
            				parame+="- [ Subgrupo: "+forma.getSubgruposMap().get("nombre_"+x)+" ] ";
            			}
            		}
        			
        		}
        	}
        }
        
        
       comp.insertLabelInGridPpalOfHeader(3,0, parame);
       
       comp.obtenerComponentesDataSet("ListadoConteo");
       
       String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", "axif.codigo in ("+forma.getArticulosFiltradosMap().get("codigos_ajustados")+"-1)");
       comp.modificarQueryDataSet(newquery);
       
        logger.info("QUERY >>>>>>>>>>>> \n "+newquery);
        
        comp.modificarQueryDataSet(newquery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
        UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}
	
	private ActionForward accionCargarSubgrupos(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
		pti.setClase(forma.getClase());
		pti.setSubgrupo(forma.getSubgrupo());
		
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		
		forma.setArticulosMap(new HashMap());
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setSubgruposMap(PreparacionTomaInventario.consultarSubgrupos(con, forma.getClase(), forma.getGrupo()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("principal");	
	}


	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarGrupos(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		PreparacionTomaInventario pti = new PreparacionTomaInventario();
		pti.setClase(forma.getClase());
		
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		
		forma.setSubgruposMap(new HashMap());
		forma.setSubgruposMap("numRegistros", 0);
		
		forma.setArticulosMap(new HashMap());
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setGruposMap(PreparacionTomaInventario.consultarGrupos(con, forma.getClase()));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("principal");	
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarClases(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{	
		logger.info(">>>>>>>>>>>>>>>>  "+forma.getAlmacen());
		
		forma.setSubgrupo(ConstantesBD.codigoNuncaValido);
		forma.setGrupo(ConstantesBD.codigoNuncaValido);
		forma.setClase(ConstantesBD.codigoNuncaValido);
		forma.setGruposMap(new HashMap());
		forma.setSubgruposMap(new HashMap());
		forma.setArticulosMap(new HashMap());
		
		forma.setGruposMap("numRegistros", 0);
		forma.setSubgruposMap("numRegistros", 0);
		forma.setArticulosMap("numRegistros", 0);
		forma.setArticulosMap("codigosArticulos", "");
		
		forma.setClasesMap(UtilidadInventarios.cargarInfoClasesInventario(usuario.getCodigoInstitucionInt(), forma.getAlmacen(), ""));
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("principal");	
	}

	/**
     * @param forma
     * @param con
     * @param mapping
	 * @param request 
     * @return
     */
	private ActionForward accionEmpezar(AjustesXInventarioFisicoForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//VALIDAR 
		logger.info("---------->"+Utilidades.convertirAEntero(ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(usuario.getCodigoInstitucionInt())));
		if(Utilidades.convertirAEntero(ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(usuario.getCodigoInstitucionInt()))>0)
		{
			
			forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		else
		{
			return ComunAction.accionSalirCasoError(mapping,request,con, logger, "", "Falta definir el parametro general Conteos Validos Ajustar Inventario", false);
		}
	}
}