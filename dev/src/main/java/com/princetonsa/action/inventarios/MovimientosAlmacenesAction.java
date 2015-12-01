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
import com.princetonsa.actionform.inventarios.MovimientosAlmacenesForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.MovimientosAlmacenes;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jaramillo
 */

public class MovimientosAlmacenesAction extends Action 
{
										
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(MovimientosAlmacenesAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try
		{
			if (form instanceof MovimientosAlmacenesForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				MovimientosAlmacenesForm forma = (MovimientosAlmacenesForm) form;
				MovimientosAlmacenes mundo = new MovimientosAlmacenes();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[MovimientosAlmacenes]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("recargar"))
				{
					return accionRecargar(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("buscar"))
				{
					return accionConsultarMovimientosAlmacenes(con, forma, mundo, usuario, mapping, request, institucion);
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
				logger.error("El form no es compatible con el form de MovimientosAlmacenesForm");
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
	 * @return
	 */
	private ActionForward accionConsultarMovimientosAlmacenes(Connection con, MovimientosAlmacenesForm forma, MovimientosAlmacenes mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
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
	 * @return
	 */
	private ActionForward generarReporte(Connection con, MovimientosAlmacenesForm forma, MovimientosAlmacenes mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String codigoTransacciones = mundo.tiposTransaccionesEscogidas(con, forma);
		
		//Validaciones que tipo de codigo se selecciono para imprimir en los reportes
		if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAxioma))
			codigoAImprimir = "1";
		else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoInterfaz))
			codigoAImprimir = "2";
		else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAmbos))
			codigoAImprimir = "3";
		
		//LLamamos al reporte común para todos
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","MovimientosAlmacen.rptdesign");
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, con, usuario, forma, request);
		
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion))
			comp.obtenerComponentesDataSet("MovimientoDetalladoTransaccion");
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion))
			comp.obtenerComponentesDataSet("MovimientoDetalladoProveedor");
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion))
			comp.obtenerComponentesDataSet("TotalizadoAlmacen");
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen))
			comp.obtenerComponentesDataSet("EntradasSalidas");
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo))
			comp.obtenerComponentesDataSet("EntradasSalidasArticulo");
		
		String newQuery = mundo.obtenerSqlReporte(forma.getCodigoCentroAtencion(), forma.getAlmacen(), forma.getIndicativoES(), codigoTransacciones, forma.getTransaccionInicial(), forma.getTransaccionFinal(), forma.getTipoReporte(), codigoAImprimir);
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
        //Generar Log Tipo Base de Datos
		mundo.insertarLog(con, forma, usuario);
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
	private ActionForward generarArchivoPlano(Connection con, MovimientosAlmacenesForm forma, MovimientosAlmacenes mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		StringBuffer datosArchivos = new StringBuffer();
		String extCsv = ".csv", extZip = ".zip", encabezado = "", periodo = "", nombreReporte = "", nombreArchivo = "" ;
		//Llenamos el mapa con los datos arrojados por la consulta
		forma.setMovimientosAlmacenes(mundo.consultarMovimientos(con, forma));
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		/*Según el tipo de reporte, se debe generar un encabezado diferente, un nombre de archivo
		un nombre de reporte y una cargada de datos diferente; ya que todos tienen estructuras distintas*/
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion))
		{
			encabezado = "Número Trans., Fecha, Código, Descripción, Unidad de Medida, Cantidad, Valor Unid., Valor Total";
			nombreReporte = "Detallado por Tipo de Transacción";
			nombreArchivo = "detalladoPorTipoTransaccion";
			datosArchivos = mundo.cargarMapaDetalladoTipoTransaccion(forma.getMovimientosAlmacenes(), nombreReporte, encabezado, usuario.getLoginUsuario());
		}
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion))
		{
			encabezado = "Almacén, Almacén Destino, Número Trans., Fecha, Usuario, Producto, Descripción, Cantidad, Valor Unid., Valor Total";
			nombreReporte = "Detallado Proveedor por Tipo de Transacción";
			nombreArchivo = "detalladoProveedorPorTipoTransaccion";
			datosArchivos = mundo.cargarMapaDetalladoProveedorTipoTransaccion(forma.getMovimientosAlmacenes(), nombreReporte, encabezado, usuario.getLoginUsuario());
		}
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion))
		{
			encabezado = "Número Trans., Fecha, Proveedor, Razón Social, Usuario, Cantidad, Valor Unid., Valor Total";
			nombreReporte = "Totalizado por Almacén y Tipo de Transacción";
			nombreArchivo = "totalizadoPorAlmacenTipoTransaccion";
			datosArchivos = mundo.cargarMapaTotalizadoAlmacenTipoTransaccion(forma.getMovimientosAlmacenes(), nombreReporte, encabezado, usuario.getLoginUsuario());
		}
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen))
		{
			encabezado = "Número Trans., Fecha, Entradas, Valor, Salidas, Valor";
			nombreReporte = "Entradas y Salidas Totalizadas por Almacén";
			nombreArchivo = "entradasSalidasTotalizadasAlmacen";
			datosArchivos = mundo.cargarMapaEntradasSalidasTotalizadas(forma.getMovimientosAlmacenes(), nombreReporte, encabezado, usuario.getLoginUsuario());
		}
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo))
		{
			encabezado = "Código, Descripción, Unidad de Medida, Entradas, Salidas, Nuevo Saldo, Valor Unitario, Valor Total";
			nombreReporte = "Entradas y Salidas por Artículo";
			nombreArchivo = "entradasSalidasArticulo";
			datosArchivos = mundo.cargarMapaEntradasSalidasArticulo(forma.getMovimientosAlmacenes(), nombreReporte, encabezado, usuario.getLoginUsuario());
		}
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(con);
		if(Utilidades.convertirAEntero(forma.getMovimientosAlmacenes("numRegistros")+"") > 0)
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
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo(nombreArchivo, periodo, usuario.getLoginUsuario());
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
						forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuario.getCodigoInstitucionInt())+nombre+extZip);
			    		forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		//Generar Log Tipo Base de Datos
			    		mundo.insertarLog(con, forma, usuario);
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			logger.info("====>No se genero el archivo ZIP");
		    			forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	//Generar Log Tipo Base de Datos
						mundo.insertarLog(con, forma, usuario);
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
	 * Metodo que carga el select de almacenes con la información
	 * de centros de costo según el centro de atención seleccionado
	 * y según el tipo de área "SubAlmacén"
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionRecargar(Connection con, MovimientosAlmacenesForm forma, ActionMapping mapping, MovimientosAlmacenes mundo, UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();

		//Llenamos el HashMap con los Almacenes según el Centro de Atención seleccionado
		if(UtilidadCadena.noEsVacio(forma.getCodigoCentroAtencion()))
			forma.setAlmacenes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaSubalmacen+"", true, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()), false));
		else
			forma.setAlmacenes(temp);
		
		//Cargamos el select con todos los tipos de transaccion
		if(UtilidadCadena.noEsVacio(forma.getIndicativoES()))
			forma.setTipoTransaccion(mundo.consultarTransacciones(con, forma.getIndicativoES()));
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
	private ActionForward accionEmpezar(Connection con, MovimientosAlmacenesForm forma, MovimientosAlmacenes mundo, UsuarioBasico usuario, ActionMapping mapping)
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
	 * Funcion que genera el encabezado del reporte
	 * @param comp
	 * @param con
	 * @param usuario
	 * @return
	 */
	private void armarEncabezado(DesignEngineApi comp, Connection con, UsuarioBasico usuario, MovimientosAlmacenesForm forma, HttpServletRequest request)
	{
		String parametros = "";
		//Insertamos la información de la Institución
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 2, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        if(Utilidades.convertirAEntero(institucion.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit()+" - "+institucion.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Insertamos el nombre de la funcionalidad en el reporte 
        comp.insertLabelInGridPpalOfHeader(1, 1, "MOVIMIENTOS ALMACENES");
        
        //Validamos que tipo de reporte se selecciono para ser informado en el cabezote
        String tipoReporte = "";
        if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion))
        	tipoReporte = "TIPO REPORTE ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion)+"]";
	    else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion))
        	tipoReporte = "TIPO REPORTE ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion)+"]";
	    else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion))
        	tipoReporte = "TIPO REPORTE ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion)+"]";
	    else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen))
        	tipoReporte = "TIPO REPORTE ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen)+"]";
	    else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo))
        	tipoReporte = "TIPO REPORTE ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo)+"]";
        
        //Validamos que tipo de articulos se selecciono para ser informado en el cabezote        
        String tipoArticulo = "";
        if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoInterfaz))
        	tipoArticulo = "TIPO CÓDIGO [Interfaz],   ";
        else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAxioma))
        	tipoArticulo = "TIPO CÓDIGO [Axioma],   ";
        else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAmbos))
        	tipoArticulo = "TIPO CÓDIGO [Interfaz - Axioma],   ";

        //Insertamos la información de los parámetros de búsqueda
        parametros = "CENTRO ATENCIÓN ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+"],   ";
        //Se valida si se filtro por el Almacén
        if(UtilidadCadena.noEsVacio(forma.getAlmacen()))
        	parametros += "ALMACÉN ["+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getAlmacen()), usuario.getCodigoInstitucionInt())+"],   ";
        else
        	parametros += "ALMACÉN [Todos],   ";
        //Se valida si se filtro por fecha o por tipos de transacción
        if(UtilidadCadena.noEsVacio(forma.getFechaInicial()) && UtilidadCadena.noEsVacio(forma.getFechaFinal()))
        	parametros += "PERIODO ["+forma.getFechaInicial()+" - "+forma.getFechaFinal()+"],   ";
        else if(UtilidadCadena.noEsVacio(forma.getTransaccionInicial()) && UtilidadCadena.noEsVacio(forma.getTransaccionFinal()))
        	parametros += "RANGO DE TRANSACCIONES ["+forma.getTransaccionInicial()+" - "+forma.getTransaccionFinal()+"],   ";
              
        
        parametros += tipoArticulo;
        parametros += tipoReporte;
        
       // logger.info("\n ######### el valor de  indicativo es -->"+forma.getIndicativoES());
        if (forma.getIndicativoES().equals(ConstantesBD.codigoTipoConceptoEntradaInv+""))
        	parametros+=" INDICATIVO E/S [ENTRADAS] ";
        else
        	if (forma.getIndicativoES().equals(ConstantesBD.codigoTipoConceptoSalidaInv+""))
            	parametros+=" INDICATIVO E/S [SALIDAS] ";
        
        //logger.info("\n ######### el valor de  getTipoTransaccionSeleccionado es -->"+forma.getTipoTransaccionSeleccionado().length);
        String tipTrans="   TIPOS TRANSACCION [ ";
        
        for (int i=0;i<forma.getTipoTransaccionSeleccionado().length;i++)
        {
        	 // logger.info("\n ######### el valor de  getTipoTransaccionSeleccionado es -->"+forma.getTipoTransaccionSeleccionado()[i]+"el nombre -->"+Utilidades.obtenerNombreTransaccionInventario(con, Utilidades.obtenerConsecutivoDeTransaccionInv(forma.getTipoTransaccionSeleccionado()[i])));
        	if (i==0)
        		tipTrans=tipTrans+Utilidades.obtenerNombreTransaccionInventario(con,  Utilidades.obtenerConsecutivoDeTransaccionInv(forma.getTipoTransaccionSeleccionado()[i]));
        	else
        		if (i>0)
        			tipTrans=tipTrans+", "+Utilidades.obtenerNombreTransaccionInventario(con,Utilidades.obtenerConsecutivoDeTransaccionInv(forma.getTipoTransaccionSeleccionado()[i]));
        }
       
        tipTrans+=" ]";
        
        parametros+=tipTrans;
        
        //logger.info("\n parametros --> "+parametros); 
        
        comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
}