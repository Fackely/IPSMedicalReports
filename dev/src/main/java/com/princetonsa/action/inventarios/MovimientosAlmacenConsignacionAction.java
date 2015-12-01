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
import com.princetonsa.actionform.inventarios.MovimientosAlmacenConsignacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MovimientosAlmacenConsignacionDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.MovimientosAlmacenConsignacion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Anexo 684
 * Creado el 9 de Octubre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class MovimientosAlmacenConsignacionAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(MovimientosAlmacenConsignacionAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try{

			if (form instanceof MovimientosAlmacenConsignacionForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				MovimientosAlmacenConsignacionForm forma = (MovimientosAlmacenConsignacionForm) form;
				MovimientosAlmacenConsignacion mundo = new MovimientosAlmacenConsignacion();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[MovimientosAlmacenConsignacion]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.setNumIngreso("");
					return accionEmpezar(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("recargar"))
				{
					return accionRecargar(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("buscar"))
				{
					return accionConsultarMovimientosAlmacenConsignacion(con, forma, mundo, usuario, mapping, request, institucion);
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
				logger.error("El form no es compatible con el form de MovimientosAlmacenConsignacionForm");
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
	 * Metodo que permite ejecutar que tipo de salida
	 * fue escogida por el usuario. Indica si el reporte
	 * es en PDF o fue en Archivo Plano
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param institucion
	 * @return ActionForward
	 */
	private ActionForward accionConsultarMovimientosAlmacenConsignacion(
			Connection con, 
			MovimientosAlmacenConsignacionForm forma, 
			MovimientosAlmacenConsignacion mundo, 
			UsuarioBasico usuario, 
			ActionMapping mapping, 
			HttpServletRequest request, 
			InstitucionBasica institucion)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return this.generarReporte(con, forma, mundo, usuario, request, mapping);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			return this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		
		return null;
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
	 * @return ActionForward
	 */
	private ActionForward generarReporte(
			Connection con, 
			MovimientosAlmacenConsignacionForm forma, 
			MovimientosAlmacenConsignacion mundo, 
			UsuarioBasico usuario, 
			HttpServletRequest request, 
			ActionMapping mapping)
	{
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String newQuery ="";
		int bandera = 0;
		
		/*
		 * Validaciones que tipo de codigo se selecciono para imprimir en los reportes
		 */
		if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAxioma))
			codigoAImprimir = ConstantesIntegridadDominio.acronimoAxioma;
		else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoInterfaz))
			codigoAImprimir = ConstantesIntegridadDominio.acronimoInterfaz;
		else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAmbos))
			codigoAImprimir = ConstantesIntegridadDominio.acronimoAmbos;
		
		/*
		 * Validamos que tipo de reporte se esta usando para llamar al DataSet correcto
		 */
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoAlmacenXProveedor))
		{
			/*
			 * Postulamos la bandera en 1 para indicar que el reporte es:
			 * Reporte Detallado Almacén Por Proveedor
			 */
			bandera = 1;
			logger.info("===> Reporte Detallado Almacén Por Proveedor: "+
					ConstantesIntegridadDominio.acronimoTipoReporteDetalladoAlmacenXProveedor);
			//LLamamos al reporte común para todos
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","MovimientosAlmacenConsignacion.rptdesign");
	        //Imprimimos el encabezado del reporte
			armarEncabezado(comp, con, usuario, forma, request, bandera);
			comp.obtenerComponentesDataSet("ReporteDetalladoAlmacenPorProveedor");
			
			newQuery = obtenerConsulta(con, forma, mundo, codigoAImprimir, bandera);
			comp.modificarQueryDataSet(newQuery);
		}
		else
		{
			/*
			 * Postulamos la bandera en 2 para indicar que el reporte es:
			 * Reporte Detallado Paciente Por Proveedor
			 */
			bandera = 2;
			logger.info("===> Reporte Detallado Paciente Por Proveedor: "+
					ConstantesIntegridadDominio.acronimoTipoReporteDetalladoPacienteXProveedor);
			//LLamamos al reporte común para todos
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","MovimientosAlmacenConsignacionPaciente.rptdesign");
	        //Imprimimos el encabezado del reporte
			armarEncabezado(comp, con, usuario, forma, request, bandera);
			comp.obtenerComponentesDataSet("ReporteDetalladoPacientePorProveedor");
			
			newQuery = obtenerConsulta(con, forma, mundo, codigoAImprimir, bandera);
			comp.modificarQueryDataSet(newQuery);
		}

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
	 * Método Movimientos Almacen Consignación Dao
	 * @return MovimientosAlmacenConsignacionDao
	 */
	private MovimientosAlmacenConsignacionDao MovimientosAlmacenConsignacionDao() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenConsignacionDao();
	}
	
	/**
	 * Método obtenerConsulta
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param codigoAImprimir
	 * @param bandera
	 * @return
	 */
	private String obtenerConsulta(
			Connection con, 
			MovimientosAlmacenConsignacionForm forma, 
			MovimientosAlmacenConsignacion mundo, 
			String codigoAImprimir, 
			int bandera) 
	{
		return MovimientosAlmacenConsignacionDao().obtenerConsulta(con, forma, mundo, codigoAImprimir, bandera);
	}

	/**
	 * Método que permite exportar el reporte a un
	 * archivo plano con extensión csv
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return ActionForward
	 */
	private ActionForward generarArchivoPlano(
			Connection con, 
			MovimientosAlmacenConsignacionForm forma, 
			MovimientosAlmacenConsignacion mundo, 
			UsuarioBasico usuario, 
			HttpServletRequest request, 
			ActionMapping mapping, 
			InstitucionBasica institucion)
	{
		logger.info("===> Entré a generar archivo plano");
		System.gc();
		StringBuffer datosArchivos = new StringBuffer();
		String extCsv = ".csv", extZip = ".zip", encabezado = "", periodo = "", nombreReporte = "", nombreArchivo = "" ;
		HashMap tmp = new HashMap();
		
		//Llenamos el mapa con los datos arrojados por la consulta
		tmp = mundo.consultarMovimientos(con, forma, mundo, tmp);
		logger.info("\n el mapa es "+tmp);
		forma.setMovimientosAlmacenConsignacion(tmp);
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		
		/*Según el tipo de reporte, se debe generar un encabezado diferente, un nombre de archivo
		un nombre de reporte y una cargada de datos diferente; ya que todos tienen estructuras distintas*/
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoAlmacenXProveedor))
		{
			logger.info("===> Entré a generar archivo plano para ReporteDetalladoAlmacenXProveedor");
			encabezado = "Nit, Proveedor, Almacén Propiedad, " +
					"Almacén Consignación, Nro. Transacción, " +
					"Cod. Art, Artículo, U. Med, " +
					"Cant., Val. Unid, Valor Total";
			nombreReporte = "Reporte Detallado Almacen Por Proveedor";
			nombreArchivo = "ReporteDetalladoAlmacenXProveedor";
			datosArchivos = mundo.cargarMapaDetalladoAlmacenXProveedor(forma.getMovimientosAlmacenConsignacion(), 
					nombreReporte, encabezado, usuario.getLoginUsuario(), institucion);
		}
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoPacienteXProveedor))
		{
			encabezado = "No. Ingreso, Paciente, Almacén, " +
					"Nro. Transacción, Nit, Proveedor, " +
					"Cod. Art, Artículo, U. Med, Cant., " +
					"Val. Unid, Valot Total";
			nombreReporte = "Reporte Detallado Paciente Por Proveedor";
			nombreArchivo = "ReporteDetalladoPacienteXProveedor";
			datosArchivos = mundo.cargarMapaDetalladoPacienteXProveedor(forma.getMovimientosAlmacenConsignacion(), 
					nombreReporte, encabezado, usuario.getLoginUsuario(), institucion);
		}
		
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
		//String path = "inventarios/ReferenciaContraReferencia/";
		String path = ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt());
		logger.info("====>Path Valor por Defecto: "+path);
		
		//Arma el nombre del reporte
		String nombre = util.TxtFile.armarNombreArchivoConPeriodo(nombreArchivo, periodo, usuario.getLoginUsuario());
		
		//Se genera el documento con la informacion
		logger.info("Mira Aqui Pipe !!! -> Nombre Report: "+nombre);
		logger.info("Mira Aqui Pipe !!! -> Este es el path: "+path);
		logger.info("Mira Aqui Pipe !!! -> Numregistros: "+tmp.get("numRegistros")+"");
		
		if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
		{
			/*
			if (UtilidadTexto.getBoolean(forma.getCriterios(indicesCriterios[3])+""))
			OperacionTrue=util.TxtFile.generarTxt(datosArchivos, nombre, path, extCsv);;
			logger.info("\n se creo ? --> "+OperacionTrue);
			*/
			path = ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt());
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
	    		path = ValoresPorDefecto.getReportPath()+
	    		ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		nombre = util.TxtFile.armarNombreArchivoConPeriodo(nombreArchivo, periodo, usuario.getLoginUsuario());
	    		logger.info("====>Path Definitivo: "+path);
	    		logger.info("====>Nombre Archivo: "+nombre);
	    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extCsv);
				//Validamos si el archivo plano se creo
				if(archivo)
			    {
					//Validamos si el .zip del archivo plano si se ejecuto
					if(BackUpBaseDatos.EjecutarComandoSO("zip  -j "+path+nombre+extZip+" "+path+nombre+extCsv) != ConstantesBD.codigoNuncaValido)
		    		{
						logger.info("====>Se genero el archivo ZIP");
						forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+
								ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt())+nombre+extZip);
			    		forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+
			    				path+nombre+extCsv+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		//Generar Log Tipo Base de Datos
			    		//mundo.insertarLog(con, forma, usuario);
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			logger.info("====>No se genero el archivo ZIP");
		    			forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+
		    					path+nombre+extCsv+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	//Generar Log Tipo Base de Datos
						//mundo.insertarLog(con, forma, usuario);
				    	UtilidadBD.finalizarTransaccion(con);
				    	UtilidadBD.closeConnection(con);
				    	return mapping.findForward("principal");
		    		}
			    }
			    else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true, "INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+
			    			path+nombre+extCsv+"!!!!!"));
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
	 * Metodo que carga el select de almacenes con la información
	 * de centros de costo según el centro de atención seleccionado
	 * y según el tipo de área "SubAlmacén"
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return ActionForward
	 */
	private ActionForward accionRecargar(Connection con, 
			MovimientosAlmacenConsignacionForm forma, 
			ActionMapping mapping, 
			MovimientosAlmacenConsignacion mundo, 
			UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();

		//Llenamos el HashMap con los Almacenes según el Centro de Atención seleccionado
		if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
		{
			//forma.setAlmacenes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), 
			//ConstantesBD.codigoTipoAreaSubalmacen+"", true, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()), false));
			forma.setAlmacenes(Utilidades.obtenerCentrosCostoTipoConsignacion(con, 
					usuario.getCodigoInstitucionInt(), 
					ConstantesBD.codigoTipoAreaSubalmacen+"", 
					true, 
					Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()), 
					false));
		}
		
		else
			forma.setAlmacenes(temp);
		
		//Cargamos el select con todos los proveedores
		logger.info("===> El proveedor es..."+forma.getProveedor());
		if(UtilidadCadena.noEsVacio(forma.getProveedor()))
			forma.setProveedores(mundo.consultarProveedores(con, forma.getProveedor()));
			//forma.setTipoTransaccion(mundo.consultarTransacciones(con, forma.getIndicativoES()));
		else
			forma.setTipoTransaccion(temp);
		
		//Cargamos el select con todos los tipos de transaccion
		if(UtilidadCadena.noEsVacio(forma.getTipoTransaccionSeleccionado()+""))
			forma.setTipoTransaccion(mundo.consultarTransacciones(con, usuario.getCodigoInstitucionInt()));
		else
			forma.setTipoTransaccion(temp);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	

	/**
	 * Metodo que se encarga de cargar los valores
	 * por defecto de la funcionalida. Metodo Inicial
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, 
			MovimientosAlmacenConsignacionForm forma, 
			MovimientosAlmacenConsignacion mundo, 
			UsuarioBasico usuario, 
			ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Cargamos el select con todos los proveedores
		forma.setProveedores(Utilidades.obtenerProveedores(ConstantesBD.codigoNuncaValido));
		//Llamamos al metodo que carga el select de almacen
		this.accionRecargar(con, forma, mapping, mundo, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que genera el encabezado del reporte
	 * @param comp
	 * @param con
	 * @param usuario
	 * @param bandera Indicar si el reporte es por almacén o es por paciente (1 = Almacén, 2 = Paciente)
	 * @return void
	 */
	private void armarEncabezado(
			DesignEngineApi comp, 
			Connection con, 
			UsuarioBasico usuario, 
			MovimientosAlmacenConsignacionForm forma, 
			HttpServletRequest request,
			int bandera)
	{
		String parametros = "";
		//Insertamos la información de la Institución
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        /*
         * Insertamos el título de la funcionalidad en el reporte
         * Está inserción va a depender de la bandera que se haya postulado
         * bandera = 1 : Almacén
         * bandera = 2 : Paciente
         */
        if(bandera == 1)
        {
        	comp.insertLabelInGridPpalOfHeader(1, 1, "Detallado Almacén por Proveedor");
        }
        else if(bandera == 2)
        {
        	comp.insertLabelInGridPpalOfHeader(1, 1, "Detallado Paciente por Proveedor");
        }
        /*
        //Validamos que tipo de reporte se selecciono para ser informado en el cabezote
        String tipoReporte = "";
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoAlmacenXProveedor))
        	tipoReporte = "Tipo Reporte [Detallado Almacén por Proveedor]";
	    else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoPacienteXProveedor))
        	tipoReporte = "Tipo Reporte [Detallado Paciente por Proveedor]";*/

        //Validamos que tipo de articulos se selecciono para ser informado en el cabezote        
        String tipoArticulo = "";
        if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoInterfaz))
        	tipoArticulo = "Tipo Código [Interfaz] ";
        else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAxioma))
        	tipoArticulo = "Tipo Código [Axioma], ";
        else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAmbos))
        	tipoArticulo = "Tipo Código [Interfaz - Axioma] ";

        //Insertamos la información de los parámetros de búsqueda
        parametros = "Centro Atención ["+Utilidades.obtenerNombreCentroAtencion(con, 
        		Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+"], ";
        //Se valida si se filtro por el Almacén
        if(UtilidadCadena.noEsVacio(forma.getAlmacen()))
        	parametros += "Almacén ["+Utilidades.obtenerNombreCentroCosto(con, 
        			Utilidades.convertirAEntero(forma.getAlmacen()), usuario.getCodigoInstitucionInt())+"], ";
        else
        	parametros += "Almacén [Todos], ";
        //Se valida si se filtro por fecha o por tipos de transacción
        if(UtilidadCadena.noEsVacio(forma.getFechaInicial()) && UtilidadCadena.noEsVacio(forma.getFechaFinal()))
        	parametros += "Periodo ["+forma.getFechaInicial()+" - "+forma.getFechaFinal()+"], ";
        
        parametros += tipoArticulo;
        //parametros += tipoReporte;
        
        comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
}