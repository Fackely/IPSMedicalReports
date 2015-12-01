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
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.PacientesPorFacturarForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.PacientesPorFacturar;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class PacientesPorFacturarAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(PacientesPorFacturarAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof PacientesPorFacturarForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				PacientesPorFacturarForm forma = (PacientesPorFacturarForm) form;
				PacientesPorFacturar mundo = new PacientesPorFacturar();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[PacientesPorFacturarAction]--->Estado: "+estado);

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
					return accionGenerar(con, mundo, forma, usuario, mapping, request, institucion);
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
				logger.error("El form no es compatible con el form de PacientesPorFacturarForm");
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
	 * Metodo que llama internamente al metodo según el tipo
	 * de salida escogido (Archivo Plano, Impresión)
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param institucion 
	 * @return
	 */
	private ActionForward accionGenerar(Connection con, PacientesPorFacturar mundo, PacientesPorFacturarForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return this.generarReporte(con, forma, mundo, usuario, request, mapping);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			return this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		
		return null;
	}

	/**
	 * Metodo que ejecuta el tipo de salida en formato
	 * archivo plano (CSV)
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 */
	private ActionForward generarArchivoPlano(Connection con, PacientesPorFacturarForm forma, PacientesPorFacturar mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		//Llenamos el HashMap con los datos de la consulta de consumos por facturar de pacientes
		forma.setConsumosPorFacturar(mundo.consultarConsumosPorFacturar(con, forma));
		StringBuffer datosArchivos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);
		String extTxt = ".csv", extZip = ".zip" ;
		String periodo = "", encabezadoArchivo = "";
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		encabezadoArchivo = forma.getFechaInicial()+" al "+forma.getFechaFinal();
		String encabezado = "Ingreso, Fecha Ingreso, Fecha Egreso, ID, Paciente, Usuario, Valor";
		if(Utilidades.convertirAEntero(forma.getConsumosPorFacturar("numRegistros")+"") > 0)
		{
			//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
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
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("pacientesPorFacturar", periodo, usuario.getLoginUsuario());
	    		//Cargamos los datos de la consulta en un StringBuffer para ser impreso en el archivo plano
	    		datosArchivos = mundo.consumosPorFacturar(forma.getConsumosPorFacturar(), "Pacientes Por Facturar", encabezadoArchivo, encabezado, usuario.getLoginUsuario());
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
	 * Metodo que ejecuta el tipo de salida en formato PDF
	 * mediante la herramienta de impresión de reportes Birt
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 */
	private ActionForward generarReporte(Connection con, PacientesPorFacturarForm forma, PacientesPorFacturar mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "ConsumosPacientesPorFacturar.rptdesign";
		String condicionesUno = "", condicionesDos = "", condicionesComunes = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
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
        comp.insertLabelInGridPpalOfHeader(1, 1, "PACIENTES POR FACTURAR");
        
        //Párametros de Búsqueda
        String parametros = "";
        v = new Vector();
        parametros +="CENTRO ATENCIÓN: ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+"] ";
        
        if(UtilidadCadena.noEsVacio(forma.getViaIngreso()))
        	parametros +="VÍA INGRESO: ["+Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(forma.getViaIngreso()))+"] ";
        else
        	parametros +="VÍA INGRESO: [Todos] ";
        
        parametros +="PERIODO: ["+forma.getFechaInicial()+" - "+forma.getFechaFinal()+"] ";
        
        if(!forma.getTipoConvenioSeleccionado().equals(""))
        	parametros +="TIPO CONVENIO: ["+Utilidades.obtenerDescripcionTipoConvenio(con, forma.getTipoConvenioSeleccionado(), usuario.getCodigoInstitucionInt())+"] ";
        
        if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
        	parametros += "CONVENIO: ["+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado()))+"] ";
        else
        	parametros +="CONVENIO: [Todos] ";
        
        if(forma.getTipoEgreso().equals(ConstantesIntegridadDominio.acronimoAmbos))
        	parametros +="TIPO EGRESO: ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAmbos)+"] ";
        else if(forma.getTipoEgreso().equals(ConstantesIntegridadDominio.acronimoIndicativoSinEgreso))
        	parametros +="TIPO EGRESO: ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoIndicativoSinEgreso)+"] ";
        else if(forma.getTipoEgreso().equals(ConstantesIntegridadDominio.acronimoIndicativoConEgreso))
        	parametros +="TIPO EGRESO: ["+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoIndicativoConEgreso)+"] ";
        
        //Informacion Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        //Insertamos el vector con los parametros de consulta
        comp.insertLabelInGridPpalOfHeader(2,0,parametros);
        //comp.insertLabelInGridOfMasterPageWithProperties(0,2,parametros,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        
        comp.obtenerComponentesDataSet("PacientesPorFacturar");
        
        //Filtros Iniciales primera parte del SELECT UNION
        //Modificado por la Tarea 38233
        //condicionesUno = "dc.facturado = '"+ConstantesBD.acronimoNo+"' AND dc.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+", "+ConstantesBD.codigoTipoSolicitudCirugia+") AND dc.servicio_cx IS NULL ";
        condicionesUno = "dc.facturado = '"+ConstantesBD.acronimoNo+"' AND dc.servicio_cx IS NULL AND dc.estado<>"+ConstantesBD.codigoEstadoFPendiente+" ";
        
        //Filtros Iniciales segunda parte del SELECT UNION
        //Modificado por la Tarea 38233


        //condicionesDos = "dc.facturado = '"+ConstantesBD.acronimoNo+"' AND dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND ta.tipos_servicio = 'M' AND sc.indi_tarifa_consumo_materiales) = '"+ConstantesBD.acronimoSi+"' ";

        condicionesDos = "dc.facturado = '"+ConstantesBD.acronimoNo+"' AND dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" ";
        
        //Filtros condiciones seleccionadas en la funcionalidad. Primeros los campos requeridos que no requiere validar, luego los no requeridos
        condicionesComunes = "AND i.centro_atencion = "+forma.getCodigoCentroAtencion()+" ";
        
        //Se adiciona validacion para convertir la fecha con (TO_DATE)
        //Se concatena a la fecha final la hora final del dia para que tome los registros en consultas de un mismo dia precisamente por el to_date
        String horaFinalDia="23:59";
        condicionesComunes += "AND i.fecha_ingreso BETWEEN TO_DATE('"+ forma.getFechaInicial()+"','DD/MM/YYYY') AND TO_DATE('"+forma.getFechaFinal()+" "+horaFinalDia+"','DD/MM/YYYY HH24:MI') ";
        //condicionesComunes += "AND i.fecha_ingreso BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"' ";        
        
        
        if(!forma.getTipoConvenioSeleccionado().equals(""))        
        	condicionesComunes += "AND con.tipo_convenio = '"+forma.getTipoConvenioSeleccionado()+"' ";
        
        //Filtramos por Tipo Egreso. Si es con egreso es True, sino se manda False, sino es ambos no se filtra por ello
		if(forma.getTipoEgreso().equals(ConstantesIntegridadDominio.acronimoIndicativoConEgreso))
			condicionesComunes += (System.getProperty("TIPOBD").equals("ORACLE")?"AND getexisteegreso(cu.id) = 1 ":"AND getexisteegreso(cu.id) = true ");
		else if(forma.getTipoEgreso().equals(ConstantesIntegridadDominio.acronimoIndicativoSinEgreso))
			condicionesComunes += (System.getProperty("TIPOBD").equals("ORACLE")?"AND getexisteegreso(cu.id) = 0 ":"AND getexisteegreso(cu.id) = false ");
		//Filtramos por la Vía de Ingreso. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(forma.getViaIngreso()))
			condicionesComunes += "AND cu.via_ingreso = "+forma.getViaIngreso()+" ";
		//Filtramos por el Convenio. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
			condicionesComunes += "AND con.codigo = "+forma.getConvenioSeleccionado()+" ";
		
		//Agregamos las condiciones generales a cada uno de las condiciones particulares de cada SELECT de la consulta UNION
		condicionesUno = condicionesUno+condicionesComunes;
		condicionesDos = condicionesDos+condicionesComunes;
		
        String newQuery = comp.obtenerQueryDataSet().replace("1=2", condicionesUno);
        comp.modificarQueryDataSet(newQuery);
        newQuery = comp.obtenerQueryDataSet().replace("1=3", condicionesDos);
        logger.info("=====>Consulta en el BIRT con Condiciones: "+newQuery);
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
	 * Método que permite cargar el select de convenios
	 * según el tipo de convenio seleccionado
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRecargar(Connection con, PacientesPorFacturar mundo, PacientesPorFacturarForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		logger.info("====>Tipo Convenio Seleccionado: "+forma.getTipoConvenioSeleccionado());
		//Cargamos el select con todos los convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false, forma.getTipoConvenioSeleccionado()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que inicializa la funcionalidad, carga la información inicial
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, PacientesPorFacturar mundo, PacientesPorFacturarForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Cargamos el select con todas las vias ingreso
		forma.setViasIngresos(Utilidades.obtenerViasIngreso(con,""));
		//Cargamos el select con todos los tipo de convenio
		forma.setTiposConvenio(Utilidades.obtenerTiposConvenio(con, usuario.getCodigoInstitucion()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}