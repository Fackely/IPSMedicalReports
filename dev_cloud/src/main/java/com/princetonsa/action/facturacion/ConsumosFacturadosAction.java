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

import com.princetonsa.actionform.facturacion.ConsumosFacturadosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsumosFacturados;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo.
 * Fecha: Mayo de 2008
 */

public class ConsumosFacturadosAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ConsumosFacturadosAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof ConsumosFacturadosForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsumosFacturadosForm forma = (ConsumosFacturadosForm) form;
				ConsumosFacturados mundo = new ConsumosFacturados();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[ConsumosFacturados]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, usuario, mapping);
				}
				else if(estado.equals("buscar"))
				{
					return accionBuscar(con, forma, mundo, usuario, mapping, request, institucion);
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
				logger.error("El form no es compatible con el form de ConsumosFacturados");
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
	 * Metodo que ejecuta la consulta de Consumos Facturados segun el tipo de Salida seleccionada
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param institucion 
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, ConsumosFacturadosForm forma, ConsumosFacturados mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			this.generarReporte(con, forma, mundo, usuario, request, mapping);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		
		UtilidadBD.closeConnection(con);			    	
    	return mapping.findForward("principal");
	}

	/**
	 * Metodo que permite exportar los datos de la consulta a Archivo Plano con ext. (.csv)
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 */
	private void generarArchivoPlano(Connection con, ConsumosFacturadosForm forma, ConsumosFacturados mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		forma.setConsumosFacturados(mundo.generarArchivoPlano(con, forma));
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extCsv = ".csv", extZip = ".zip" ;
		String periodo = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"-"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"-"+usuario.getLoginUsuario();
		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("consumosFacturados", "", periodo);
		String encabezado = "Factura, Ingreso, Fecha/Hora Factura, Id, Paciente, H.C., Convenio, Valor Factura";
		
		if(Utilidades.convertirAEntero(forma.getConsumosFacturados("numRegistros")+"") > 0)
		{
			datosArchivos = mundo.cargarMapaConsumosFacturados(forma.getConsumosFacturados(), "Consumos Facturados", periodo, encabezado);
			String path = ValoresPorDefecto.getReportPath()+"facturacion/";
			boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extCsv);
			if(archivo)
		    {
	    		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extCsv) != ConstantesBD.codigoNuncaValido)
	    		{
	    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+"facturacion/"+nombre+extZip);
		    		forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "+path+nombre+extCsv+"!!!!!"));
		    		forma.setArchivo(true);
		    		forma.setZip(UtilidadFileUpload.existeArchivo(path, nombre+extZip));
		    		UtilidadBD.finalizarTransaccion(con);
	    		}
	    		else
	    		{
	    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA "+path+nombre+extCsv+"!!!!!"));
	    			forma.setArchivo(true);
			    	forma.setZip(false);
			    	UtilidadBD.finalizarTransaccion(con);
	    		}
		    }
		    else
		    {
		    	forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO "+path+nombre+extCsv+"!!!!!"));
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
	    	UtilidadBD.finalizarTransaccion(con);
	    }
	}

	/**
	 * Metodo que permite crear exporta los resultados de la consulta a PDF
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param usuario
	 * @param request
	 * @param mapping
	 */
	private ActionForward generarReporte(Connection con, ConsumosFacturadosForm forma, ConsumosFacturados mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "ConsumosFacturados.rptdesign";
		String condiciones = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
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
        comp.insertLabelInGridPpalOfHeader(1,0, "CONSUMOS FACTURADOS POR RANGOS DE FECHAS");
        
        String filtro = "";
        
        filtro += "[centro atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion())).toLowerCase();
        
        
        if(!forma.getConvenioSeleccionado().equals("") && !forma.getConvenioSeleccionado().equals("null"))
        {
        	filtro += "   convenio: "+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado())).toLowerCase();
        	
        	
        }
        else
        {
        	filtro += "   convenio:  todos";
        	
        }
        comp.obtenerComponentesDataSet("ConsumosFacturadosRompimiento");
        
        filtro += "   periodo facturas: "+forma.getFechaInicial()+" - "+forma.getFechaFinal();
        
        
        if(UtilidadCadena.noEsVacio(forma.getMontoBaseInicial()))
        	filtro += "   monto base: "+forma.getMontoBaseInicial()+" - "+forma.getMontoBaseFinal();
        else
        	filtro += "   monto base: ninguno";
        
        if(UtilidadCadena.noEsVacio(forma.getTope()))
        	filtro +=  "   n° filas reporte: "+forma.getTope();
        else
        	filtro += "   n° filas reporte: ninguno";
        
        filtro += "   usuario: "+usuario.getLoginUsuario()+"]";
        comp.insertLabelInGridPpalOfHeader(2,0, filtro);
        
        condiciones= ConsumosFacturados.obtenerCondiciones(forma.getCodigoCentroAtencion(), forma.getFechaInicial(), forma.getFechaFinal(), forma.getConvenioSeleccionado(), forma.getMontoBaseInicial(), forma.getMontoBaseFinal(), forma.getTope());
        
        String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("\n===>Consulta en el BIRT con Condiciones: "+newQuery);
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
        UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que carga los select con la informacion inicial y resetea la forma
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ConsumosFacturadosForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Cargamos el select con todos los convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}