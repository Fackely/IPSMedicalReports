/*
 * Creado el 27-dic-2005
 * por Joan López
 */
package com.princetonsa.action.inventarios;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

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

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;
import util.pdf.PdfReports;

import com.princetonsa.actionform.inventarios.KardexForm;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.Kardex;
import com.princetonsa.pdf.KardexPdf;

/**
 * @author Joan López
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class KardexAction extends Action 
{
	/**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(KardexAction.class);
    /**
	 * Método execute del action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Connection con = null;
		try{

			if(form instanceof KardexForm)
			{
				KardexForm forma=(KardexForm)form;
				Kardex mundo = new Kardex();
				HttpSession sesion = request.getSession();		
				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(sesion);
				String estado=forma.getEstado();
				logger.warn("[KardexAction] --> "+estado);			

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de KardexAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezarBusqueda"))
				{
					forma.reset();	    
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaBusqueda");
				}
				else if(estado.equals("generarArchivoPlano"))
				{
					return this.accionGenerarArchivoPlano(con, forma, mundo, usuario, request, mapping, sesion);
				}
				else if(estado.equals("generarBusqueda"))
				{	 
					return this.validacionesCierre(usuario, forma, request, mapping, con, mundo);
				}
				else if(estado.equals("kardexGeneral"))
				{	
					forma.setPorLote(false);
					this.accionGenerarCalculosValidacionesKardex(con,forma,mundo,usuario,request);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaDetalleArticulos");
				}
				else if(estado.equals("porLote"))
				{
					forma.setPorLote(true);
					forma.setCodigoArticulo(Integer.parseInt(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""));
					this.accionEjecutarBusquedaArticulosLote(con,forma,mundo);
					if(Integer.parseInt(forma.getMapaArticulos("numRegistros")+"")==1)
					{ 
						forma.setIndexSeleccionado(0);
						forma.setLote(forma.getMapaArticulos("lote_"+forma.getIndexSeleccionado())+"");
						forma.setFechaVencimiento(forma.getMapaArticulos("fechavencimiento_"+forma.getIndexSeleccionado()+"")+"");
						this.accionGenerarCalculosValidacionesKardex(con,forma,mundo,usuario,request);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaDetalleArticulos");
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaListadoArticulosLote");			    
				}
				else if(estado.equals("detalleKardexPorLote"))
				{
					forma.setPorLote(true);
					forma.setLote(forma.getMapaArticulos("lote_"+forma.getIndexSeleccionado())+"");
					forma.setFechaVencimiento(forma.getMapaArticulos("fechavencimiento_"+forma.getIndexSeleccionado()+"")+"");
					this.accionGenerarCalculosValidacionesKardex(con,forma,mundo,usuario,request);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaDetalleArticulos");
				}
				else if(estado.equals("volverABusqueda"))
				{				
					forma.setPorLote(false);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaBusqueda");
				}
				else if(estado.equals("volverAListado"))
				{					
					forma.setPorLote(false);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaListadoArticulos");
				}
				else if(estado.equals("volverAListadoLote"))
				{					
					forma.setPorLote(true);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaListadoArticulosLote");
				}
				else if(estado.equals("imprimirDetalle"))
				{
					this.accionImprimirDetalle(forma,request,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("abrirPdf");
				}
				else if(estado.equals("imprimirListado"))
				{
					this.accionImprimirListado(forma,mundo,request,usuario,con);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("abrirPdf");
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
				logger.error("El form no es compatible con el form de KardexForm");
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
	
	public ActionForward validacionesCierre(UsuarioBasico usuario, KardexForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, Kardex mundo)
	{
		ActionErrors errors= new ActionErrors();
		String fechaCierreInicial=UtilidadInventarios.fechaCierreInicial(usuario.getCodigoInstitucionInt());
		String[] mesAnio=fechaCierreInicial.split("/");
		int anioCierreInicial=Utilidades.convertirAEntero(mesAnio[1]);
		int anioSeleccionado=Utilidades.convertirAEntero(forma.getFechaInicial().split("/")[2]);
		int mesSeleccionado=Utilidades.convertirAEntero(forma.getFechaInicial().split("/")[1]);
		String aniosSinCierre="", mesesSinCierre="";
		
		int mesCierreInicial=Utilidades.convertirAEntero(mesAnio[0]);
		//VALIDACIONES CIERRES AÑOS ANTERIORES
		if(anioCierreInicial<anioSeleccionado)
		{
			for(int i=anioCierreInicial;i<anioSeleccionado;i++)
			{
				if(!UtilidadInventarios.existeCierreFinalAnio(usuario.getCodigoInstitucionInt(), i+""))
				{
					aniosSinCierre+=i+" ";
				}
			}
			mesCierreInicial=1;//comienza a verificar desde enero del año actual los cierres
		}
		
		//VALIDACIONES CIERRES MESES ANTERIORES
		for(int i=mesCierreInicial;i<mesSeleccionado;i++)
		{
			String codigoCierre=UtilidadInventarios.obtenerCodigoCierreInventario(usuario.getCodigoInstitucionInt(), i+"", anioSeleccionado+"");
			if(codigoCierre.equals(ConstantesBD.codigoNuncaValido+""))
			{
				mesesSinCierre+=UtilidadFecha.obtenerNombreMes(i)+" ";
			}
		}
		
		if(!UtilidadTexto.isEmpty(aniosSinCierre))
		{
			errors.add("Existen años sin cierre",new ActionMessage("error.inventarios.noExisteCierreAnual",aniosSinCierre.trim()));
			saveErrors(request, errors);
			return mapping.findForward("paginaBusqueda");
		}
		else if(!UtilidadTexto.isEmpty(mesesSinCierre))
		{
			errors.add("Existen meses sin cierre",new ActionMessage("error.inventarios.noExisteCierreMensual",mesesSinCierre.trim()));
			saveErrors(request, errors);
			return mapping.findForward("paginaBusqueda");
		}else
		{
			forma.setPorLote(false);
			
			try {
				this.accionEjecutarBusquedaAvanzada(con,forma,mundo);                
		        if(Integer.parseInt(forma.getMapaArticulos("numRegistros")+"")==1)
		        {
		        	logger.info("numero registros 1!!!");
		            if(Articulo.articuloManejaLote(con,Integer.parseInt(forma.getMapaArticulos("cod_articulo_0")+""), usuario.getCodigoInstitucionInt()))
		            {
		            	logger.info("\n\nforward->paginaListadoArticulos");
		                
							UtilidadBD.cerrarConexion(con);
						
					    return mapping.findForward("paginaListadoArticulos");
		            }
		            forma.setIndexSeleccionado(0);
		            this.accionGenerarCalculosValidacionesKardex(con,forma,mundo,usuario,request);
		            UtilidadBD.cerrarConexion(con);
		            logger.info("\n\nforward->paginaDetalleArticulos");
		            return mapping.findForward("paginaDetalleArticulos");
		        }
		        Utilidades.obtenerNombreArticulo(con,-1);
		        UtilidadBD.cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    return mapping.findForward("paginaListadoArticulos");
		}
		
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo 
	 * @param request
	 * @param usuario
	 * @param con 
	 */
	private void accionImprimirListado(KardexForm forma, Kardex mundo, HttpServletRequest request, UsuarioBasico usuario, Connection con) 
	{
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrarListadoKardex" + r.nextInt()  +".pdf";
    	String filename=ValoresPorDefecto.getFilePath() + nombreArchivo;
    	PdfReports report = new PdfReports();
    	//PdfReports report = new PdfReports("",true);
    	KardexPdf.generarEncabezado(report,usuario,forma, request);
		report.openReport(filename);
		for(int i=0;i<Integer.parseInt(forma.getMapaArticulos("numRegistros")+"");i++)
         {
            forma.setIndexSeleccionado(i);
			forma.setLote(forma.getMapaArticulos("lote_"+forma.getIndexSeleccionado())+"");
			forma.setFechaVencimiento(forma.getMapaArticulos("fechavencimiento_"+forma.getIndexSeleccionado()+"")+"");
            this.accionGenerarCalculosValidacionesKardex(con,forma,mundo,usuario,request);
     		report=KardexPdf.generarReporteUnArticulo(report,forma,forma.getIndexSeleccionado());
         }
		report.closeReport(); 
   	    request.setAttribute("nombreArchivo", nombreArchivo);
   	    request.setAttribute("nombreVentana", "Consulta Kardex");
	}

	/**
	 * 
	 * @param forma
	 * @param usuario 
	 * @param request 
	 */
	private void accionImprimirDetalle(KardexForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
    	String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrarKardex" + r.nextInt()  +".pdf";
    	KardexPdf.pdfKardexDetalle(ValoresPorDefecto.getFilePath() + nombreArchivo,forma,usuario, request);
   	    request.setAttribute("nombreArchivo", nombreArchivo);
   	    request.setAttribute("nombreVentana", "Consulta Kardex");
	}
	
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 */
	private void accionGenerarCalculosValidacionesKardex(Connection con, KardexForm forma, Kardex mundo, UsuarioBasico usuario, HttpServletRequest request) 
	{
		this.generarCalculoCantidadAnual(con, forma, usuario, mundo);
    	this.accionValidaciones(con,forma,mundo,usuario,request);
    	this.generarCalculoMovimientoCantidadArticulos(con, forma,  mundo);
    	forma.setMapaArticulos("calculoCosto",UtilidadInventarios.obtenerCostoUnitarioKardex(usuario.getCodigoInstitucionInt(),Integer.parseInt(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getFechaInicial()));          
    	double calculoValorTotal=(Double.parseDouble(forma.getMapaArticulos("calculoCantidad")+""))*(Double.parseDouble(forma.getMapaArticulos("calculoCosto")+""));
    	forma.setMapaArticulos("calculoValorTotalInicial",calculoValorTotal+"");
		HashMap mapa = new HashMap();
		if(!forma.isPorLote())
			mapa=mundo.ejecutarConsultaDetalleArticulos(con,forma.getCodAlmacen(),(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getFechaInicial(),forma.getFechaFinal());
		else
			mapa=mundo.ejecutarConsultaDetalleArticulosLote(con,forma.getCodAlmacen(),(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getFechaInicial(),forma.getFechaFinal(),forma.getLote(),forma.getFechaVencimiento());
		mapa.putAll(mundo.generarCalculoCostoUnitarioArticulos(mapa,forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+"",usuario.getCodigoInstitucionInt()));
		mapa.putAll(mundo.generarCalculoCantidadArticulos(mapa,(forma.getMapaArticulos("calculoCantidad")+"")));
		mapa.putAll(mundo.generarCalculoValorSaldoArticulos(mapa));
		forma.setMapaArticulos("detalle_"+forma.getIndexSeleccionado(), mapa);	
		/**saldo final-> la información que se muestra corresponde a la misma mostrada en el ultimo movimiento 
		 *del kardex generadoen los campos CANT SALDO,COSTO UNIT,VR SALDO, si no se relacionaron movimientos se muestra
		 *la misma información generada para los campos SALDO INICIAL */
		if(Integer.parseInt(mapa.get("numRegistros")+"")>0)
		{
		    int pos=(Integer.parseInt(mapa.get("numRegistros")+""))-1;
			forma.setMapaArticulos("calculoCostoFinal",mapa.get("costo_unitario_dos_"+pos));
			forma.setMapaArticulos("calculoCantidadFinal",mapa.get("cantidad_saldo_"+pos));
			forma.setMapaArticulos("calculoValorTotalFinal",mapa.get("valor_saldo_"+pos));
		}
		else
		{
		    forma.setMapaArticulos("calculoCantidadFinal",forma.getMapaArticulos("calculoCantidad"));
		    forma.setMapaArticulos("calculoCostoFinal",forma.getMapaArticulos("calculoCosto"));			
			forma.setMapaArticulos("calculoValorTotalFinal",forma.getMapaArticulos("calculoValorTotalInicial"));
		}
	}
	/**
	 * metodo para realizar las validaciones para
	 * generar el kardex
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 */
	private boolean accionValidaciones(Connection con, KardexForm forma, Kardex mundo, UsuarioBasico usuario, HttpServletRequest request) 
	{
		boolean cumpleValidacion=true;		
		String temp=UtilidadInventarios.existeCierreInicialFecha(usuario.getCodigoInstitucionInt(),forma.getFechaInicial().split("/")[2]);
        if(!temp.equals(ConstantesBD.codigoNuncaValido+""))//el cierre inicial es el el mismo anio.
        {
        	 for(int i=Integer.parseInt(temp.split("/")[0])+1;i<Integer.parseInt(forma.getFechaInicial().split("/")[1]);i++)
             {
                 	this.generarCalculos(con, forma, usuario,mundo,i);
             }
        }
        else 
        {
	            for(int i=1;i<Integer.parseInt(forma.getFechaInicial().split("/")[1]);i++)
	            {
	                	this.generarCalculos(con, forma, usuario,mundo,i);
	            }
        }
        return cumpleValidacion;
	}
	/**
	 * metodo para generar el calculo de
	 * la cantidad
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mundo
	 * @param mes
	 */
	private void generarCalculos(Connection con,KardexForm forma,UsuarioBasico usuario, Kardex mundo,int mes)
	{
		HashMap mapa=new HashMap();
		//forma.setMapaArticulos("calculoCantidad","0");
		int calculoCantidad=forma.getMapaArticulos().containsKey("calculoCantidad")?Integer.parseInt(forma.getMapaArticulos().get("calculoCantidad")+""):0;
		
		String codigoCierre=UtilidadInventarios.obtenerCodigoCierreInventario(usuario.getCodigoInstitucionInt(),mes+"",forma.getFechaInicial().split("/")[2]);
		if(!forma.isPorLote())
			mapa=mundo.ejecutarConsultaDetalleCierres(con,codigoCierre,Integer.parseInt(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getCodAlmacen());
		else
			mapa=mundo.ejecutarConsultaDetalleCierresLote(con,codigoCierre,Integer.parseInt(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getCodAlmacen(),forma.getLote(),forma.getFechaVencimiento());
		if(mapa!=null&&!mapa.isEmpty())
		{
			for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
			{
				calculoCantidad=calculoCantidad+(Integer.parseInt(mapa.get("cantidad_total_entradas_"+k)+"")-Integer.parseInt(mapa.get("cantidad_total_salidas_"+k)+""));								
			}
			forma.setMapaArticulos("calculoCantidad",calculoCantidad+"");
		}
		else
		{
			logger.warn("NO EXISTE INFORMACION DE CIERRE PARA EL ARTICULO ["+forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+"-"+forma.getMapaArticulos("desc_articulo_"+forma.getIndexSeleccionado())+"]");
		}
	}
	private void generarCalculoCantidadAnual(Connection con,KardexForm forma,UsuarioBasico usuario, Kardex mundo)
	{		
		logger.info("1");
		int calculoCantidad=0;		
		HashMap mapaAnual=new HashMap();
		forma.setMapaArticulos("calculoCantidad","0");
		String codigoCierreAnual=UtilidadInventarios.obtenerCodigoCierreInventario(usuario.getCodigoInstitucionInt(),12+"",(Integer.parseInt(forma.getFechaInicial().split("/")[2])-1)+"");
		if(!forma.isPorLote())
			mapaAnual=mundo.ejecutarConsultaDetalleCierres(con,codigoCierreAnual,Integer.parseInt(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getCodAlmacen());
		else
			mapaAnual=mundo.ejecutarConsultaDetalleCierresLote(con,codigoCierreAnual,Integer.parseInt(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getCodAlmacen(),forma.getLote(),forma.getFechaVencimiento());
		if(mapaAnual!=null&&!mapaAnual.isEmpty())
		{
			for(int k=0;k<Integer.parseInt(mapaAnual.get("numRegistros")+"");k++)
			{
				calculoCantidad=calculoCantidad+(Integer.parseInt(mapaAnual.get("cantidad_total_entradas_anio_"+k)+"")-Integer.parseInt(mapaAnual.get("cantidad_total_salidas_anio_"+k)+""));				
			}
			forma.setMapaArticulos("calculoCantidad",calculoCantidad+"");
		}				
	}
	/**
	 * metodo para calcular la catidad de los 
	 * movimientos realizados a un articulo, que
	 * esta fuera del cierre
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void generarCalculoMovimientoCantidadArticulos(Connection con,KardexForm forma,Kardex mundo)
	{
		HashMap mapa=new HashMap();
		String rangoFinal="";
		//forma.setMapaArticulos("calculoCantidad","0");
		int calculoCantidad=forma.getMapaArticulos().containsKey("calculoCantidad")?Integer.parseInt(forma.getMapaArticulos().get("calculoCantidad")+""):0;
		int dia=forma.getFechaInicial().split("/")[0].equals("01")?ConstantesBD.codigoNuncaValido:Integer.parseInt(forma.getFechaInicial().split("/")[0])-1;
		rangoFinal=dia<=9?"0"+dia+forma.getFechaInicial().substring(2):dia+forma.getFechaInicial().substring(2);
		if(dia!=ConstantesBD.codigoNuncaValido)
		{
			if(!forma.isPorLote())
				mapa=mundo.consultarUltimosMovimientosArticulo(con,"01"+forma.getFechaInicial().substring(2),rangoFinal,(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getCodAlmacen()+"");
			else
				mapa=mundo.consultarUltimosMovimientosArticuloLote(con,"01"+forma.getFechaInicial().substring(2),rangoFinal,(forma.getMapaArticulos("cod_articulo_"+forma.getIndexSeleccionado())+""),forma.getCodAlmacen()+"",forma.getLote(),forma.getFechaVencimiento());
			if(mapa!=null&&!mapa.isEmpty())
			{
				for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
				{
					calculoCantidad=calculoCantidad+(Integer.parseInt(mapa.get("cantidad_entrada_"+k)+"")-Integer.parseInt(mapa.get("cantidad_salida_"+k)+""));
				}
				forma.setMapaArticulos("calculoCantidad",calculoCantidad+"");
			}
		}
	}
	/**
	 * metodo para ejecutar la busqueda de articulos
	 * segun los parametros elegidos
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionEjecutarBusquedaAvanzada(Connection con, KardexForm forma, Kardex mundo) 
	{
		HashMap mapa=new HashMap();		
		mapa.put("centroAtencion", forma.getCentroAtencion());
		mapa.put("codigoAlmacen",forma.getCodAlmacen()+"");
		mapa.put("codigoClase",forma.getClase());
		mapa.put("codigoGrupo",forma.getGrupo());
		mapa.put("codigoSubGrupo",forma.getSubgrupo());
		mapa.put("codigoArticulo",forma.getArticulo());
		mapa.put("fechaInicial",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()+""));
		mapa.put("fechaFinal",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal()+""));		
		forma.setMapaArticulos(mundo.ejecutarBusquedaAvanzada(con, mapa));
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionEjecutarBusquedaArticulosLote(Connection con, KardexForm forma, Kardex mundo)
	{
		HashMap mapa=new HashMap();		
		mapa.put("centroAtencion", forma.getCentroAtencion());
		mapa.put("codigoAlmacen",forma.getCodAlmacen()+"");
		mapa.put("codigoArticulo",forma.getCodigoArticulo()+"");
		mapa.put("fechaInicial",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()+""));
		mapa.put("fechaFinal",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal()+""));		
		forma.setMapaArticulos(mundo.accionEjecutarBusquedaArticulosLote(con, mapa));
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	public ActionForward accionGenerarArchivoPlano(Connection con, KardexForm forma, Kardex mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, HttpSession sesion)
	{
		forma.setPorLote(false);
		//this.accionEjecutarBusquedaAvanzada(con,forma,mundo);
		HashMap mapaRestric=new HashMap();		
		mapaRestric.put("centroAtencion", forma.getCentroAtencion());
		mapaRestric.put("codigoAlmacen",forma.getCodAlmacen()+"");
		mapaRestric.put("codigoClase",forma.getClase());
		mapaRestric.put("codigoGrupo",forma.getGrupo());
		mapaRestric.put("codigoSubGrupo",forma.getSubgrupo());
		mapaRestric.put("codigoArticulo",forma.getArticulo());
		mapaRestric.put("fechaInicial",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()+""));
		mapaRestric.put("fechaFinal",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal()+""));		
		
		HashMap mapaListadoArticulos=mundo.ejecutarBusquedaAvanzada(con, mapaRestric);
		forma.setMapaArticulos((HashMap)mapaListadoArticulos.clone());
		
		String texto="";
		
		for(int w=0; w<Integer.parseInt(mapaListadoArticulos.get("numRegistros").toString()); w++)
		{
			forma.setIndexSeleccionado(w);
			this.accionGenerarCalculosValidacionesKardex(con,forma,mundo,usuario,request);
			
			if(w==0)
			{	
				texto+=	"===================================================== ARCHIVO PLANO KARDEX ===================================================================== \n" +
									"NOMBRE DEL ALMACEN : "+forma.getNomAlmacen()+" \n" +
									"CENTRO DE ATENCION : "+forma.getNomCentroAtencion()+" \n"+
									"FECHA INICIAL      : "+forma.getFechaInicial()+" \n" +
									"FECHA FINAL        : "+forma.getFechaFinal()+" \n" +
									"\n\n";
			}	
			
			texto+=	"*********************************************************************************************************************************************************\n" +
								"ARTICULO        : "+forma.getMapaArticulos("cod_articulo_"+w)+" "+forma.getMapaArticulos("desc_articulo_"+w)+" \n" +
								"U. MEDIDA       : "+forma.getMapaArticulos("unidad_medida_"+w)+"\n" +
								"SALDO INICIAL A : "+forma.getFechaInicial()+" Cantidad Saldo : "+forma.getMapaArticulos("calculoCantidad")+" Costo Unitario : "+UtilidadTexto.formatearValores(forma.getMapaArticulos("calculoCosto")+"")+" Vr.Saldo : "+UtilidadTexto.formatearValores(forma.getMapaArticulos("calculoValorTotalInicial")+"")+"\n\n";
			
			HashMap mapaDetalleArticulo=new HashMap();
			mapaDetalleArticulo=(HashMap)forma.getMapaArticulos("detalle_"+w);
			
			for(int x=0; x<Integer.parseInt(mapaDetalleArticulo.get("numRegistros")+""); x++)
			{
				if(x==0)
					texto+="| TRANSACCION | DOC | F.ELABORA | F.ATENCION | COSTO UNIT. | CANT.ENTR | VR.ENTR | CANT.SAL | VR. SAL | CANT.SAL | VR.SAL | CANT.SALDO | COSTO.UNIT | VR.SALDO | \n\n";
				
				texto+=	"| "+mapaDetalleArticulo.get("cod_transaccion_"+x)+" "+mapaDetalleArticulo.get("desc_transaccion_"+x)+
										" | "+mapaDetalleArticulo.get("documento_"+x)+
										" | "+mapaDetalleArticulo.get("fecha_elaboracion_"+x)+
										" | "+mapaDetalleArticulo.get("fecha_atencion_"+x)+
										" | "+UtilidadTexto.formatearValores(mapaDetalleArticulo.get("costo_unitario_"+x)+"")+
										" | "+mapaDetalleArticulo.get("cantidad_entrada_"+x)+
										" | "+UtilidadTexto.formatearValores(mapaDetalleArticulo.get("valor_entrada_"+x)+"")+
										" | "+mapaDetalleArticulo.get("cantidad_salida_"+x)+
										" | "+UtilidadTexto.formatearValores(mapaDetalleArticulo.get("valor_salida_"+x)+"")+
										" | "+mapaDetalleArticulo.get("cantidad_saldo_"+x)+
										" | "+UtilidadTexto.formatearValores(mapaDetalleArticulo.get("costo_unitario_dos_"+x)+"")+
										" | "+UtilidadTexto.formatearValores(mapaDetalleArticulo.get("valor_saldo_"+x)+"")+"\n\n";
				
			}
			
			texto+=	"SALDO FINAL A : "+forma.getFechaFinal()+" Cantidad Saldo Final : "+forma.getMapaArticulos("calculoCantidadFinal")+" Costo Unitario Final : "+UtilidadTexto.formatearValores(forma.getMapaArticulos("calculoCostoFinal")+"")+ " Vr.Saldo : "+UtilidadTexto.formatearValores(forma.getMapaArticulos("calculoValorTotalFinal")+"")+" \n\n";
									
		}
		logger.info(texto);
		forma.setArchivoGenerado(guardarArchivo(forma.getNomAlmacen(), forma.getFechaInicial(), forma.getFechaFinal(), texto, sesion));
		forma.setEstado("archivoPlanoGenerado");
		UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaBusqueda");
	}

	/**
	 * 
	 * @param nombreAlmacen
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param texto
	 * @return
	 */
	private String guardarArchivo(String nombreAlmacen, String fechaInicial, String fechaFinal, String texto, HttpSession sesion)
	{
		String folder= "kardex";
		String ruta=sesion.getServletContext().getRealPath(System.getProperty("file.separator"))+"archivosPlanos"+System.getProperty("file.separator");
		logger.info("path->"+ruta);
		//String ruta="/home/wilson/contextos/axioma/tmp/";
		String archivo = folder + System.getProperty("file.separator") + nombreAlmacen.trim() + "_FI_"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"_FF_"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"_FGen_"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"_"+UtilidadFecha.getHoraActual()+".txt";
		archivo=archivo.trim();
		archivo=archivo.replace(" ", "_");
		logger.info("archivo--->"+archivo);
		try 
		{
			File directorio = new File(ruta, folder);

			if (!directorio.isDirectory() && !directorio.exists()) 
			{
				if (!directorio.mkdirs()) 
				{
					logger.error("Error creando el directorio "+ folder);
					return "Error en la creando el directorio en "+ruta;
				}
			}
			
			FileWriter archivoLog = new FileWriter(ruta	+ archivo, true);
			archivoLog.write(texto);
			archivoLog.close();
			
			if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+(ruta+archivo).replace(".txt", ".zip")+" "+(ruta+archivo)) != ConstantesBD.codigoNuncaValido)
    		{
				return ".."+System.getProperty("file.separator")+"archivosPlanos"+System.getProperty("file.separator")+ (archivo.replace(".txt", ".zip"));
    		}
			return ".."+System.getProperty("file.separator")+"archivosPlanos"+System.getProperty("file.separator")+ archivo;
		} 
		catch (Exception e) 
		{
			logger.error("Error en creacion archivo");
			return "Error I/O creando el archivo en "+ruta;
		}
	} 
	
}