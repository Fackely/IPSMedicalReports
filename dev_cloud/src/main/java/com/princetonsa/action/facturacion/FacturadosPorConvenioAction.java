package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.FacturadosPorConvenioForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.FacturadosPorConvenio;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class FacturadosPorConvenioAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ConsolidadoFacturacionAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof FacturadosPorConvenioForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				FacturadosPorConvenioForm forma = (FacturadosPorConvenioForm) form;
				FacturadosPorConvenio mundo = new FacturadosPorConvenio();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[FacturadosPorConvenioAction]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, mundo, forma, usuario, mapping);
				}
				else if(estado.equals("recargar"))
				{
					return accionRecargar(con, mundo, forma, usuario, mapping);
				}
				else if(estado.equals("generar"))
				{
					return accionGenerar(con, mundo, forma, usuario, institucion, mapping, request);
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
				logger.error("El form no es compatible con el form de FacturadosPorConvenioForm");
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
	 * Metodo que permite ejecutar e inicializar
	 * la funcionalidad
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, FacturadosPorConvenio mundo, FacturadosPorConvenioForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que carga los contratos segun el convenio
	 * seleccionado
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRecargar(Connection con, FacturadosPorConvenio mundo, FacturadosPorConvenioForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		ArrayList temp = new ArrayList();
		//Cargamos los contratos del convenio seleccionado
		if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
			forma.setContratos(Utilidades.obtenerContratos(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado()), true, true));
		else
			forma.setContratos(temp);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que llama la accion dependiendo
	 * del tipo de salida (Archivo Plano o Impresion)
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param institucion 
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGenerar(Connection con, FacturadosPorConvenio mundo, FacturadosPorConvenioForm forma, UsuarioBasico usuario, InstitucionBasica institucion, ActionMapping mapping, HttpServletRequest request)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			this.generarReporte(con, forma, mundo, usuario, request, mapping);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		
		//Generar Log Tipo Base de Datos
		mundo.insertarLog(con, forma, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que genera el archivo plano de facturados
	 * por convenio en un archivo con extension txt
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 */
	private void generarArchivoPlano(Connection con, FacturadosPorConvenioForm forma, FacturadosPorConvenio mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extTxt = ".txt", extZip = ".zip";
		String convenio = "", contrato = "", encabezadoServicios = "", encabezadoArticulos = "";
		if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
		{
			convenio = Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado()));
			//Reemplazamos los espacios por guion
			convenio = convenio.replaceAll(" ", "-");
		}
		if(UtilidadCadena.noEsVacio(forma.getContratoSeleccionado()))
		{
			contrato = forma.getConvenioSeleccionado();
			//Reemplazamos los espacios por guion
			contrato=contrato.replaceAll(" ", "-");
		}
		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("SerArtFact", convenio+"-"+contrato, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
		
		//Validar que encabezado mandar segun el rompimiento que se va a ejecutar para servicios
		if(forma.getCheckServicios().equals(ConstantesBD.acronimoSi))
		{
			if(forma.getRadioServicios().equals("1"))
				encabezadoServicios = "Grupo Servicio, Vía Ingreso, Cantidad, Valor Total";
			else if (forma.getRadioServicios().equals("2"))
				encabezadoServicios = "Naturaleza Servicio, Vía Ingreso, Cantidad, Valor Total";
			else if (forma.getRadioServicios().equals("3"))
				encabezadoServicios = "Servicio, Código, Vía Ingreso, Cantidad, Valor Total";
			//Llenamos el mapa de servicios
			forma.setFacturadosPorConvenioServicios(mundo.consultarFacturadosPorConvenio(con, forma, true));
		}
		
		//Validar que encabezado mandar segun el rompimiento que se va a ejecutar para articulos
		if(forma.getCheckArticulos().equals(ConstantesBD.acronimoSi))
		{
			if(forma.getRadioArticulos().equals("1"))
				encabezadoArticulos = "Clase Inventario, Vía Ingreso, Cantidad, Valor Total";
			else if (forma.getRadioArticulos().equals("2"))
				encabezadoArticulos = "Naturaleza Artículos, Vía Ingreso, Cantidad, Valor Total";
			else if (forma.getRadioArticulos().equals("3"))
				encabezadoArticulos = "Código, Artículo, Concentración, Forma Farm., Vía Ingreso, Cantidad, Valor Total";
			//Llenamos el mapa de articulos
			forma.setFacturadosPorConvenioArticulos(mundo.consultarFacturadosPorConvenio(con, forma, false));
		}
		
		if(Utilidades.convertirAEntero(forma.getFacturadosPorConvenioServicios("numRegistros")+"") > 0 || Utilidades.convertirAEntero(forma.getFacturadosPorConvenioArticulos("numRegistros")+"") > 0)
		{
			logger.info("===>Servicios: "+forma.getRadioServicios());
			logger.info("===>Articulos: "+forma.getRadioArticulos());
			datosArchivos = mundo.cargarMapaFacturadosPorConvenio(forma.getFacturadosPorConvenioServicios(), forma.getFacturadosPorConvenioArticulos(), "Servicios/Artículos Facturados por Convenio", forma.getFechaInicial(), forma.getFechaFinal(), encabezadoServicios, encabezadoArticulos, convenio, forma.getContratoSeleccionado(), forma.getRadioArticulos(), forma.getRadioServicios());
			String path = ValoresPorDefecto.getReportPath()+"facturacion/";
			boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extTxt);
			if(archivo)
		    {
	    		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extTxt) != ConstantesBD.codigoNuncaValido)
	    		{
	    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+"facturacion/"+nombre+extZip);
	    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "+path+nombre+extTxt+"!!!!!"));
		    		forma.setArchivo(true);
		    		forma.setZip(UtilidadFileUpload.existeArchivo(path, nombre+extZip));
		    		logger.info("SE CREO EL ARCHIVO ZIP");
		    		UtilidadBD.finalizarTransaccion(con);
	    		}
	    		else
	    		{
	    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "+path+nombre+extTxt+"!!!!!"));
	    			forma.setArchivo(true);
			    	forma.setZip(false);
			    	logger.info("NO SE EJECUTO EL ZIP");
			    	UtilidadBD.finalizarTransaccion(con);
	    		}
		    }
		    else
		    {
		    	forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO "+path+nombre+extTxt+"!!!!!"));
		    	forma.setArchivo(false);
		    	forma.setZip(false);
		    	UtilidadBD.abortarTransaccion(con);
		    }
		}
	    else
	    {
	    	forma.setMensaje(new ResultadoBoolean(true,"SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
	    	forma.setArchivo(false);
	    	forma.setZip(false);
	    	UtilidadBD.abortarTransaccion(con);
	    }
		//Reseteamos los mapas con la intencion de que no queden con datos despues de generar el archivo plano
		forma.setFacturadosPorConvenioServicios(new HashMap());
		forma.setFacturadosPorConvenioServicios("numRegistros", "0");
		forma.setFacturadosPorConvenioArticulos(new HashMap());
		forma.setFacturadosPorConvenioArticulos("numRegistros", "0");
	}

	/**
	 * Metodo que genera el reporte de facturados
	 * por convenio en PDF
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 */
	private void generarReporte(Connection con, FacturadosPorConvenioForm forma, FacturadosPorConvenio mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String condiciones = "", condicionesServicios = "", condicionesArticulos = "";
		String newQuery = "";
		String nombreRptDesign = "FacturadosPorConvenio.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,1, "SERVICIOS/ARTÍCULOS FACTURADOS POR CONVENIO");
        
        v = new Vector();
        v.add("PERIODO: "+forma.getFechaInicial()+" - "+forma.getFechaFinal());
        if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
        	v.add("CONVENIO: "+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado())));
        else
        	v.add("CONVENIO: Todos");
        
        if(UtilidadCadena.noEsVacio(forma.getContratoSeleccionado()))
        	v.add("CONTRATO: "+forma.getContratoSeleccionado());
        else
        	v.add("CONTRATO: Todos");
        
        v.add("USUARIO: "+usuario.getLoginUsuario());
        
        //Insertamos el vector con los parametros de consulta
        comp.insertLabelInGridOfMasterPageWithProperties(0,2,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        
		//Condiciones Generales
        condiciones += "f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" AND to_char(f.fecha, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"' ";
        if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
			condiciones += "AND f.convenio = "+forma.getConvenioSeleccionado()+" ";
		if(UtilidadCadena.noEsVacio(forma.getContratoSeleccionado()))
			condiciones += "AND sc.contrato = "+forma.getContratoSeleccionado()+" ";
		
		if(forma.getCheckServicios().equals(ConstantesBD.acronimoSi))
        {
			logger.info("=====>Valor del Radio Servicios: "+forma.getRadioServicios());
	        condicionesServicios = condiciones + "AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" ";
			//El rompimiento es por Grupo de Servicios
        	if(forma.getRadioServicios().equals("1"))
        	{
        		comp.obtenerComponentesDataSet("FacturadosGrupoServicios");
    	        newQuery = comp.obtenerQueryDataSet().replace("1=2", condicionesServicios);
    	        logger.info("=====>Grupo de Servicios: "+newQuery);
    	        comp.modificarQueryDataSet(newQuery);
        	}
        	//El rompimiento es por Naturaleza de Servicios
        	else if(forma.getRadioServicios().equals("2"))
        	{
        		comp.obtenerComponentesDataSet("FacturadosNaturalezaServicios");
    	        newQuery = comp.obtenerQueryDataSet().replace("1=2", condicionesServicios);
    	        logger.info("=====>Naturaleza de Servicios: "+newQuery);
    	        comp.modificarQueryDataSet(newQuery);
        	}
        	//El rompimiento es por Servicios
        	else if(forma.getRadioServicios().equals("3"))
        	{
        		comp.obtenerComponentesDataSet("FacturadosServicios");
    	        newQuery = comp.obtenerQueryDataSet().replace("1=2", condicionesServicios);
    	        logger.info("=====>Servicios: "+newQuery);
    	        comp.modificarQueryDataSet(newQuery);
        	}
        }
		if(forma.getCheckArticulos().equals(ConstantesBD.acronimoSi))
        {
			logger.info("=====>Valor del Radio Articulos: "+forma.getRadioArticulos());
			condicionesArticulos = condiciones + "AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoArticulos+" ";
			//El rompimiento es por Clase de Articulo
        	if(forma.getRadioArticulos().equals("1"))
        	{
        		comp.obtenerComponentesDataSet("FacturadosClaseInventarios");
    	        newQuery = comp.obtenerQueryDataSet().replace("1=2", condicionesArticulos);
    	        logger.info("=====>Clase de Articulos: "+newQuery);
    	        comp.modificarQueryDataSet(newQuery);
        	}
        	//El rompimiento es por Naturaleza de Articulo
        	else if(forma.getRadioArticulos().equals("2"))
        	{
        		comp.obtenerComponentesDataSet("FacturadosNaturalezaArticulos");
    	        newQuery = comp.obtenerQueryDataSet().replace("1=2", condicionesArticulos);
    	        logger.info("=====>Naturaleza de Articulos: "+newQuery);
    	        comp.modificarQueryDataSet(newQuery);
        	}
        	//El rompimiento es por Articulo
        	else if(forma.getRadioArticulos().equals("3"))
        	{
        		comp.obtenerComponentesDataSet("FacturadosArticulos");
    	        newQuery = comp.obtenerQueryDataSet().replace("1=2", condicionesArticulos);
    	        logger.info("=====>Articulos: "+newQuery);
    	        comp.modificarQueryDataSet(newQuery);
        	}
        }
	    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
	}
	
}