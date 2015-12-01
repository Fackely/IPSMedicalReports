package com.princetonsa.action.inventarios;

import java.sql.Connection;
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

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.ConsultaCostoArticulosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsultaCostoArticulos;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class ConsultaCostoArticulosAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ConsultaCostoArticulosAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try
		{

			if (form instanceof ConsultaCostoArticulosForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaCostoArticulosForm forma = (ConsultaCostoArticulosForm) form;
				ConsultaCostoArticulos mundo = new ConsultaCostoArticulos();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[ConsultaCostoArticulos]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, mundo, mapping, usuario, institucion);
				}
				else if(estado.equals("recargar"))
				{
					return accionRecargar(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("generar"))
				{
					return accionConsultarCostoArticulos(con, forma, mundo, usuario, mapping, request, institucion);
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
				logger.error("El form no es compatible con el form de ConsultaCostoArticulosForm");
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
	 * Método que permite ejecutar que tipo de salida
	 * fue escogida por el usuario. Indica si el reporte
	 * es en PDF o fue en Archivo Plano
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param institucion
	 * @return
	 */
	private ActionForward accionConsultarCostoArticulos(Connection con, ConsultaCostoArticulosForm forma, ConsultaCostoArticulos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return this.generarReporte(con, forma, mundo, usuario, request, mapping);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			return this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("principal");
	}

	/**
	 * Metodo que permite exportar el reporte a un
	 * archivo plano con extensión csv
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarArchivoPlano(Connection con, ConsultaCostoArticulosForm forma, ConsultaCostoArticulos mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		StringBuffer datosArchivos = new StringBuffer();
		String extCsv = ".csv", extZip = ".zip", encabezado = "", periodo = "", nombreReporte = "";
		//Llenamos el mapa con los datos arrojados por la consulta
		forma.setConsultaCostoArticulos(mundo.consultarCostoArticulos(con, forma));
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		encabezado = "Cód. Art., Artículo, U. Med., Almacén, Costo Promedio";
		nombreReporte = "Consulta Costo Artículos";
		datosArchivos = mundo.cargarMapaCostoArticulos(forma.getConsultaCostoArticulos(), nombreReporte, encabezado, usuario.getLoginUsuario());
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(con);
		if(Utilidades.convertirAEntero(forma.getConsultaCostoArticulos("numRegistros")+"") > 0)
		{
			//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt());
			logger.info("====>Path Valor por Defecto: "+path);
			//Validamos si el path esta vacio o lleno
	    	if(UtilidadTexto.isEmpty(path))
			{
	    		forma.setMensaje(new ResultadoBoolean(true, ""));
	    		forma.setArchivo(false);
		    	forma.setZip(false);
		    	UtilidadBD.abortarTransaccion(con);
		    	UtilidadBD.closeConnection(con);
		    	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Inventarios", "error.inventarios.rutaNoDefinida", true);
			}
			else
			{
				//Cargamos el path original. La ruta establecida en el web.xml concatenada con la ruta establecida en el parametro general
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("consultaCostoArticulos", periodo, usuario.getLoginUsuario());
	    		logger.info("====>Path Definitivo: "+path);
	    		logger.info("====>Nombre Archivo: "+nombre);
	    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extCsv);
				//Validamos si el archivo plano se creo
				if(archivo)
			    {
					//Validamos si el .zip del archivo plano si se ejecuto
					if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extCsv) != ConstantesBD.codigoNuncaValido)
		    		{
						logger.info("===>Se genero el archivo ZIP");
						forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt())+nombre+extZip);
			    		forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			logger.info("===>No se genero el archivo ZIP");
		    			forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	UtilidadBD.finalizarTransaccion(con);
				    	UtilidadBD.closeConnection(con);
				    	return mapping.findForward("principal");
		    		}
			    }
			    else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true, "INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+path+nombre+extCsv+"!!!!!"));
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
	    	forma.setMensaje(new ResultadoBoolean(true, "SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
	    	forma.setArchivo(false);
	    	forma.setZip(false);
	    	UtilidadBD.finalizarTransaccion(con);
	    	return mapping.findForward("principal");
	    }
	}

	/**
	 * Metodo que permite exportar el reporte en
	 * formato PDF utilizando la herramienta BIRT 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward generarReporte(Connection con, ConsultaCostoArticulosForm forma, ConsultaCostoArticulos mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "ConsultaCostoArticulos.rptdesign", condiciones = "", parametros = "", codigoAImprimir = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Insertamos el título del reporte
        comp.insertLabelInGridPpalOfHeader(1, 1, "CONSULTA COSTO ARTÍCULOS");
        
        //Parámetros de Búsqueda
        //****************************INICIO IMPRESIÓN PARAMETROS DE BÚSQUEDA*********************************
        parametros = "Centro Atención ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+"], ";
        //Se valida si se filtro por el Almacén
        if(UtilidadCadena.noEsVacio(forma.getAlmacen()))
        	parametros += "Almacén ["+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getAlmacen()), usuario.getCodigoInstitucionInt())+"], ";
        else
        	parametros += "Almacén [Todos], ";
        //Se valida si se filtro por la Clase de Inventario
        if(UtilidadCadena.noEsVacio(forma.getClase()))
        	parametros += "Clase Inventario ["+forma.getDescripcionClase()+"], ";
        else
        	parametros += "Clase Inventario [Todos], ";
        //Se valida si se filtro por el Grupo
        if(UtilidadCadena.noEsVacio(forma.getGrupo()))
        	parametros += "Grupo ["+forma.getDescripcionGrupo()+"], ";
        else
        	parametros += "Grupo [Todos], ";
        //Se valida si se filtro por el Subgrupo
        if(UtilidadCadena.noEsVacio(forma.getSubGrupo()))
        	parametros += "SubGrupo ["+forma.getDescripcionSubGrupo()+"], ";
        else
        	parametros += "SubGrupo [Todos], ";
        //Se valida si se filtro por el Subgrupo
        if(!UtilidadCadena.noEsVacio(forma.getCodArticulo()))
        	parametros += "Artículo [Todos], ";
        //Se valida que tipo de Código se escogio para la búsqueda
        if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoInterfaz))
        {
        	parametros += "Tipo Código Artículo [Interfaz]";
        	codigoAImprimir = "2";
        }
        else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAxioma))
        {
        	parametros += "Tipo Código Artículo [Axioma]";
        	codigoAImprimir = "1";
        }
        else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAmbos))
        {
        	parametros += "Tipo Código Artículo [Axioma - Interfaz]";
        	codigoAImprimir = "3";
        }
        //****************************FIN IMPRESIÓN PARAMETROS DE BÚSQUEDA***********************************
        comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        //Consultamos la condiciones seleccionadas para la consulta de movimiento deudor
        condiciones = mundo.consultarCondicionesCostoArticulos(con, forma);
        
        //Obtenemos el DataSet y lo modificamos
        comp.obtenerComponentesDataSet("ConsultaCostoArticulos");
        String newQuery = comp.obtenerQueryDataSet().replace("4", codigoAImprimir);
        comp.modificarQueryDataSet(newQuery);
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("=====>Consulta en el BIRT: "+newQuery);
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
	 * Método utilizado para inicializar los
	 * párametros de la funcionalidad y cargar
	 * la página de búsqueda inicial
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param mapping
	 * @param usuario
	 * @param institucion
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ConsultaCostoArticulosForm forma, ConsultaCostoArticulos mundo, ActionMapping mapping, UsuarioBasico usuario, InstitucionBasica institucion)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Llamamos al metodo que carga el select de almacen
		this.accionRecargar(con, forma, mapping, mundo, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que carga el select de almacenes con la información
	 * de centros de costo según el centro de atención seleccionado
	 * y según el tipo de área "SubAlmacén"
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 */
	private ActionForward accionRecargar(Connection con, ConsultaCostoArticulosForm forma, ActionMapping mapping, ConsultaCostoArticulos mundo, UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();
		if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
			forma.setAlmacenes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaSubalmacen+"", true, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()), false));
		else
			forma.setAlmacenes(temp);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}