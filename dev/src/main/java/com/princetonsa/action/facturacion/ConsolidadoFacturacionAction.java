package com.princetonsa.action.facturacion;

import java.sql.Connection;
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

import com.princetonsa.actionform.facturacion.ConsolidadoFacturacionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsolidadoFacturacion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class ConsolidadoFacturacionAction extends Action
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
			if (form instanceof ConsolidadoFacturacionForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsolidadoFacturacionForm forma = (ConsolidadoFacturacionForm) form;
				ConsolidadoFacturacion mundo = new ConsolidadoFacturacion();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[ConsolidadoFacturacionAction]--->Estado: "+estado);

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
				logger.error("El form no es compatible con el form de ConsolidadoFacturacionForm");
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
	private ActionForward accionEmpezar(Connection con, ConsolidadoFacturacion mundo, ConsolidadoFacturacionForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		//Cargamos el select con todas las vias ingreso
		forma.setViasIngresos(Utilidades.obtenerViasIngreso(con,""));
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
	private ActionForward accionRecargar(Connection con, ConsolidadoFacturacion mundo, ConsolidadoFacturacionForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		//Cargamos los contratos del convenio seleccionado
		if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
			forma.setContratos(Utilidades.obtenerContratos(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado()), true, true));
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
	private ActionForward accionGenerar(Connection con, ConsolidadoFacturacion mundo, ConsolidadoFacturacionForm forma, UsuarioBasico usuario, InstitucionBasica institucion, ActionMapping mapping, HttpServletRequest request)
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
	 * Metodo que genera el archivo plano de consolidado
	 * de facturacion en un archivo con extension txt
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 */
	private void generarArchivoPlano(Connection con, ConsolidadoFacturacionForm forma, ConsolidadoFacturacion mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		forma.setConsolidadoFacturacion(mundo.consultarConsolidadoFacturacion(con, forma));
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extTxt = ".txt", extZip = ".zip" ;
		String periodo = "", encabezadoArchivo = "";
		
		//Validamos si la consulta se realizo por fecha o por factura para armar el nombre del archivo plano
		if(UtilidadCadena.noEsVacio(forma.getFechaInicial()) && UtilidadCadena.noEsVacio(forma.getFechaFinal()))
		{
			periodo = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"-"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
			encabezadoArchivo = forma.getFechaInicial()+" al "+forma.getFechaFinal();
		}
		if(UtilidadCadena.noEsVacio(forma.getFacturaInicial()) && UtilidadCadena.noEsVacio(forma.getFacturaFinal()))
		{
			periodo = forma.getFacturaInicial()+"-"+forma.getFacturaFinal();
			encabezadoArchivo = forma.getFacturaInicial()+" al "+forma.getFacturaFinal();
		}
		
		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("consolidadoFacturacion", "", periodo);
		String encabezado = "Factura, Fecha, Vía Ingreso, Paciente, Documento, Estrato, Ficha, Valor Convenio, Valor Paciente, Valor Total";
		
		if(Utilidades.convertirAEntero(forma.getConsolidadoFacturacion("numRegistros")+"") > 0)
		{
			datosArchivos = mundo.cargarMapaConsolidadoFacturacion(forma.getConsolidadoFacturacion(), "Consolidado de Facturación", encabezadoArchivo, encabezado);
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
		    		UtilidadBD.finalizarTransaccion(con);
	    		}
	    		else
	    		{
	    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "+path+nombre+extTxt+"!!!!!"));
	    			forma.setArchivo(true);
			    	forma.setZip(false);
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
	}

	/**
	 * Metodo que genera el reporte de consolidado
	 * de facturacion en PDF
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 */
	private void generarReporte(Connection con, ConsolidadoFacturacionForm forma, ConsolidadoFacturacion mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "ConsolidadoFacturacion.rptdesign";
		String condiciones = "", parametros = "";
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
        comp.insertLabelInGridPpalOfHeader(1,1, "CONSOLIDADO DE FACTURACIÓN");
        
        if(UtilidadCadena.noEsVacio(forma.getFechaInicial()) && UtilidadCadena.noEsVacio(forma.getFechaFinal()))
        	parametros = "Periodo ["+forma.getFechaInicial()+" - "+forma.getFechaFinal()+"], ";
        else if(UtilidadCadena.noEsVacio(forma.getFacturaInicial()) && UtilidadCadena.noEsVacio(forma.getFacturaFinal()))
        	parametros = "Rango Facturas ["+forma.getFacturaInicial()+" - "+forma.getFacturaFinal()+"], ";
        
        if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
        	parametros += "Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado()))+"], ";
        else
        	parametros += "Convenio [Todos], ";
        
        if(UtilidadCadena.noEsVacio(forma.getContratoSeleccionado()))
        	parametros += "Contrato ["+forma.getContratoSeleccionado()+"], ";
        else
        	parametros += "Contrato [Todos], ";
        
        if(UtilidadCadena.noEsVacio(forma.getViaIngreso()))
        	parametros += "Vía Ingreso ["+Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(forma.getViaIngreso()))+"]";
        else
        	parametros += "Vía Ingreso [Todos]";
        
        //Insertamos los parametros de búsqueda seleccionados por el usuario
        comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        comp.obtenerComponentesDataSet("ConsolidadoFacturacion");
        
        condiciones= ConsolidadoFacturacion.obtenerCondiciones(forma.getFacturaInicial(), forma.getFacturaFinal(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getViaIngreso(), forma.getConvenioSeleccionado(), forma.getContratoSeleccionado());
        
        String newQuery = comp.obtenerQueryDataSet().split("1=2")[0]+condiciones;
        logger.info("\n=====>Consulta en el BIRT con Condiciones: "+newQuery);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
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