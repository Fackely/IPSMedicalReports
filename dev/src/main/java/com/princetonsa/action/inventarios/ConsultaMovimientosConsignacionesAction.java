package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.ConsultaMovimientosConsignacionesForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsultaMovimientosConsignaciones;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultaMovimientosConsignacionesAction extends Action 
{

	
	/**
	 * 
	 */
	Logger logger = Logger.getLogger(ConsultaMovimientosConsignacionesAction.class);
	
	/**
	 * 
	 */
	private String[] indices={"nitproveedor_","descproveedor_","codigoarticulo_","codigointerfaz_","descarticulo_","unidadmedida_","cantidadentrada_","costopromedio_","cantidadsalida_","cantidadentrada_","valortotal_"};
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{

			if (form instanceof ConsultaMovimientosConsignacionesForm) 
			{
				ConsultaMovimientosConsignacionesForm forma=(ConsultaMovimientosConsignacionesForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				ActionErrors errores = new ActionErrors();

				ConsultaMovimientosConsignaciones mundo = new ConsultaMovimientosConsignaciones();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultaMovimientosConsignacionesAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarAlmacenes"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					forma.setMapaListadoMovimientos(mundo.consultarMovimientosConsignaciones(con, forma.getCentroAtencion(), forma.getAlmacen(), forma.getProveedor(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getTipoCodigo()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("ordernar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("imprimir"))
				{
					this.generarReporte(con, forma, mapping, request, usuario, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("generarArchivo"))
				{
					this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaMovimientosConsignacionesForm");
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
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param mundo
	 * @return
	 */
	private ActionForward generarReporte(Connection con, ConsultaMovimientosConsignacionesForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, ConsultaMovimientosConsignaciones mundo) 
	{
		String nombreRptDesign = "ConsultaMovimientosConsignaciones.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        comp.insertLabelInGridPpalOfHeader(2,1, "       CONSULTA MOVIMIENTOS CONSIGNACIONES");
        
        String condiciones="";
        
        String periodo= forma.getFechaInicial()+" al "+forma.getFechaFinal();
        
        condiciones= " - Centro Atencion: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCentroAtencion()));
        
        if(!forma.getAlmacen().equals(""))
        {
        	condiciones+=" - Centro Costo: "+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getAlmacen()), usuarioActual.getCodigoInstitucionInt());
        }
        if(!forma.getProveedor().equals(""))
        {
        	condiciones+=" - Proveedor: "+forma.getProveedor();
        }
        comp.insertLabelInGridPpalOfHeader(3,1,"                           Parametros ");
        comp.insertLabelInGridPpalOfHeader(4,1," Periodo: "+periodo +condiciones );
        
        //SE MODIFICA LA CONSULTA PARA QUE TOME TODOS LOS CODIGOS DE LOS DETALLES CARGO INSERTADOS
        comp.obtenerComponentesDataSet("MovimientosConsignaciones");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        String consultaMod= mundo.cambiarConsulta(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getAlmacen(), forma.getProveedor(), forma.getCentroAtencion());
        
        String newQuery=comp.obtenerQueryDataSet() + consultaMod;
        comp.modificarQueryDataSet(newQuery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
       //newPathReport += "&centroAtencion="+Utilidades.convertirAEntero(forma.getCentroAtencion());
        
       if(!newPathReport.equals(""))
       {
    	   request.setAttribute("isOpenReport", "true");
    	   request.setAttribute("newPathReport", newPathReport);
       }
        
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarArchivoPlano(Connection con, ConsultaMovimientosConsignacionesForm forma, ConsultaMovimientosConsignaciones mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		//Llenamos el HashMap con los datos de la consulta de consumos por facturar de pacientes
		forma.setMapaListadoMovimientos(mundo.consultarMovimientosConsignaciones(con, forma.getCentroAtencion(), forma.getAlmacen(), forma.getProveedor(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getTipoCodigo()));
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extTxt = ".csv", extZip = ".zip" ;
		String periodo = "", encabezadoArchivo = "", centroAtencion;
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		encabezadoArchivo = forma.getFechaInicial()+" al "+forma.getFechaFinal();
		centroAtencion= Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCentroAtencion()));
		String encabezado = "Nit, Proveedor, Cod. Art., Articulo, U. Medida, Cantidad, Costo, Valor Total";
		if(Utilidades.convertirAEntero(forma.getMapaListadoMovimientos("numRegistros")+"") > 0)
		{
			//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt());
			logger.info("====>Path: "+path);
			//Validamos si el path esta vacio o lleno
	    	if(UtilidadTexto.isEmpty(path))
			{
	    		forma.setMensaje(new ResultadoBoolean(true, ""));
	    		forma.setArchivo(false);
		    	forma.setZip(false);
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Facturación", "error.facturacion.rutaNoDefinida", true);
			}
	    	else
	    	{
	    		//Cargamos el path original. La ruta establcecida en el web.xml concatenada con la ruta establecida en el parametro general
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("consultaMovimientosConsignaciones", periodo, usuario.getLoginUsuario());
	    		//Cargamos los datos de la consulta en un StringBuffer para ser impreso en el archivo plano
	    		datosArchivos = mundo.movimientosConsignaciones(forma.getMapaListadoMovimientos(), "Consulta Movimientos Consignaciones", encabezadoArchivo, encabezado, usuario.getLoginUsuario(), centroAtencion);
	    		logger.info("===>Nombre Archivo: "+nombre);
	    		logger.info("===>Path Final Archivo: "+path);
	    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extTxt);
	    		//Validamos si el archivo si se creo
	    		if(archivo)
			    {
		    		//Validamos si se genero el .zip o no?. Con la intención de indicarle al usuario que el zip no se genero
	    			if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extTxt) != ConstantesBD.codigoNuncaValido)
		    		{
		    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt())+nombre+extZip);
		    			logger.info("===>Path Archivo: "+forma.getPathArchivoTxt());
			    		forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extTxt+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extTxt+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	UtilidadBD.finalizarTransaccion(con);
				    	UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
		    		}
			    }
	    		else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+path+nombre+extTxt+"!!!!!"));
			    	forma.setArchivo(false);
			    	forma.setZip(false);
			    	UtilidadBD.abortarTransaccion(con);
			    	UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
			    }
	    	}
	    }
	    else
	    {
	    	forma.setMensaje(new ResultadoBoolean(true,"SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
	    	forma.setArchivo(false);
	    	forma.setZip(false);
	    	UtilidadBD.abortarTransaccion(con);
	    	UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
	    }
	}
	
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(ConsultaMovimientosConsignacionesForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getMapaListadoMovimientos().get("numRegistros")+"");
		forma.setMapaListadoMovimientos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaListadoMovimientos(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaListadoMovimientos("numRegistros",numReg+"");
	}
	
	
	
	
	
}
