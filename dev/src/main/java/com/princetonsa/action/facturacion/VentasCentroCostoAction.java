package com.princetonsa.action.facturacion;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.VentasCentroCostoForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.VentasCentroCosto;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class VentasCentroCostoAction extends Action 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(VentasCentroCostoAction.class);
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{	
		if (form instanceof VentasCentroCostoForm) 
		{
			VentasCentroCostoForm forma=(VentasCentroCostoForm) form;
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			ActionErrors errores = new ActionErrors();
			
			VentasCentroCosto mundo = new VentasCentroCosto();
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de VentasCentroCostoAction (null) ");
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
			else if(estado.equals("cargarCentrosCosto"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("imprimir"))
			{
				this.generarReporte(con, forma, mapping, request, usuario, mundo);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("generarArchivo"))
			{
				this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else
			{
				forma.reset(usuario.getCodigoCentroAtencion()+"",usuario.getCodigoInstitucion());
				logger.warn("Estado no valido dentro del flujo de VENTAS X CENTRO COSTO");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}		
		}
		else
		{
			logger.error("El form no es compatible con el form de VentasCentroCostoForm");
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
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarArchivoPlano(Connection con, VentasCentroCostoForm forma, VentasCentroCosto mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		//Llenamos el HashMap con los datos de la consulta de consumos por facturar de pacientes
		forma.setMapaConsultaVentas(mundo.consultarVentasCentroCosto(con, forma));
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extTxt = ".csv", extZip = ".zip" ;
		String periodo = "", encabezadoArchivo = "", centroAtencion;
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		encabezadoArchivo = forma.getFechaInicial()+" al "+forma.getFechaFinal();
		centroAtencion= Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCentroAtencion()));
		String encabezado = "Centro Costo Ejecuta, Cuenta, Valor Total, Descuentos, Ventas Total";
		if(Utilidades.convertirAEntero(forma.getMapaConsultaVentas("numRegistros")+"") > 0)
		{
			//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
			logger.info("====>Path: "+path);
			//Validamos si el path esta vacio o lleno
	    	if(UtilidadTexto.isEmpty(path))
			{
	    		
	    		logger.info("Entrando a sacar error de path >>>>>>"+path);
	    		
	    		forma.setMensaje(new ResultadoBoolean(true, "No se ha establecido la ruta, para el almacenamiento de los reportes del módulo Facturacion en parámetros general."));
	    		forma.setArchivo(false);
		    	forma.setZip(false);
		    	UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Facturación", "error.facturacion.rutaNoDefinida", true);
			}
	    	else
	    	{
	    		//Cargamos el path original. La ruta establcecida en el web.xml concatenada con la ruta establecida en el parametro general
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("ventasXCentroCosto", periodo, usuario.getLoginUsuario());
	    		//Cargamos los datos de la consulta en un StringBuffer para ser impreso en el archivo plano
	    		datosArchivos = mundo.ventasCentroCosto(forma.getMapaConsultaVentas(), "Ventas X Centro de Costo", encabezadoArchivo, encabezado, usuario.getLoginUsuario(), centroAtencion);
	    		logger.info("===>Nombre Archivo: "+nombre);
	    		logger.info("===>Path Final Archivo: "+path);
	    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extTxt);
	    		//Validamos si el archivo si se creo
	    		if(archivo)
			    {
		    		//Validamos si se genero el .zip o no?. Con la intención de indicarle al usuario que el zip no se genero
	    			if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extTxt) != ConstantesBD.codigoNuncaValido)
		    		{
		    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+nombre+extZip);
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
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param mundo
	 * @return
	 */
	private ActionForward generarReporte(Connection con, VentasCentroCostoForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, VentasCentroCosto mundo) 
	{
		String nombreRptDesign = "VentasCentroCosto.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,0, "CENTRO DE ATENCION: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCentroAtencion())));
        comp.insertLabelInGridPpalOfHeader(3,1, "VENTAS POR CENTRO DE COSTO");
        
        //Informacion Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuarioActual.getLoginUsuario());
        
        String condiciones="";
        if(!forma.getCentroCosto().equals(""))
        {
        	condiciones+=" - Centro Costo: "+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getCentroCosto()), usuarioActual.getCodigoInstitucionInt());
        }
        
        comp.insertLabelInGridPpalOfHeader(4,0,"                                              Parametros de Busqueda. ");
        comp.insertLabelInGridPpalOfHeader(4,1,"Fecha Inicial: "+forma.getFechaInicial()+" - Fecha Final "+forma.getFechaFinal() +condiciones );
        
        //SE MODIFICA LA CONSULTA PARA QUE TOME TODOS LOS CODIGOS DE LOS DETALLES CARGO INSERTADOS
        comp.obtenerComponentesDataSet("VentasEmpresa");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        String consultaMod= mundo.cambiarConsulta(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getCentroCosto());
        
        String newQuery=comp.obtenerQueryDataSet().replace("1=1", consultaMod);
        comp.modificarQueryDataSet(newQuery);
        
        logger.info("New >>>>>>>>> "+newQuery);
        logger.info("Mod >>>>>>>>> "+consultaMod);
        
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
        newPathReport += "&centroAtencion="+Utilidades.convertirAEntero(forma.getCentroAtencion());
        
        
       if(!newPathReport.equals(""))
       {
    	   request.setAttribute("isOpenReport", "true");
    	   request.setAttribute("newPathReport", newPathReport);
       }
        
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	
}
