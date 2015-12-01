package com.princetonsa.action.facturacion;

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
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ConsumosPorFacturarPacientesHospitalizadosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsumosPorFacturarPacientesHospitalizados;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre de 2008
 */

public class ConsumosPorFacturarPacientesHospitalizadosAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ConsumosPorFacturarPacientesHospitalizadosAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

		Connection con = null;
		try{
			if (form instanceof ConsumosPorFacturarPacientesHospitalizadosForm) {


				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();

				if(con == null)	{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsumosPorFacturarPacientesHospitalizadosForm forma = (ConsumosPorFacturarPacientesHospitalizadosForm) form;
				ConsumosPorFacturarPacientesHospitalizados mundo = new ConsumosPorFacturarPacientesHospitalizados();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[ConsumosFacturados]--->Estado: "+estado);

				if(estado == null) {
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar")) {
					return accionEmpezar(con, forma, usuario, mapping);
				}

				else if(estado.equals("imprimir")) {
					return accionImprimir(con, forma, mundo, usuario, mapping, request, institucion);
				}
				else {
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else {
				logger.error("El form no es compatible con el form de ConsumosPorFacturarPacientesHospitalizados");
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
	 * Metodo que ejecuta la consulta de ConsumosPorFacturarPacientesHospitalizados segun el tipo de Salida seleccionada
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param institucion 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ConsumosPorFacturarPacientesHospitalizadosForm forma, ConsumosPorFacturarPacientesHospitalizados mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
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

	private ActionForward generarArchivoPlano(Connection con, ConsumosPorFacturarPacientesHospitalizadosForm forma, ConsumosPorFacturarPacientesHospitalizados mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion) {

		StringBuffer datosArchivos = new StringBuffer();
		String extCsv = ".csv", extZip = ".zip", encabezado = "", periodo = "", nombreReporte = "";

		String path = ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
		logger.info("====>Valor Path Para Reportes por Defecto: "+path);

		
    	if(UtilidadTexto.isEmpty(path)) {
    		forma.setMensaje(new ResultadoBoolean(true, ""));
    		forma.setArchivo(false);
	    	forma.setZip(false);
	    	UtilidadBD.abortarTransaccion(con);
	    	UtilidadBD.closeConnection(con);
	    	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Facturación", "error.facturacion.rutaNoDefinida", true);
		}

    	//si el path de reportes esta definido en la administracion
    	else {

    		//Llenamos el mapa con los datos arrojados por la consulta
    		forma.setConsumosPorFacturarPacientesHospitalizados(mundo.generarArchivoPlano(con, forma));
    		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual(con));
    		encabezado = "Ingreso, Fecha Ingreso, Id, Paciente, Piso/Habitación/Cama, Valor Consumos\n";
    		nombreReporte = "Consumos Por Facturar Pacientes Hospitalizados";

    		datosArchivos = mundo.cargarMapaConsumosPorFacturarPacientesHospitalizados(forma.getConsumosPorFacturarPacientesHospitalizados(), nombreReporte, forma.getFechaInicial(), forma.getFechaFinal(),encabezado, usuario.getLoginUsuario());
    		
    		//Iniciamos Transaccion
    		UtilidadBD.iniciarTransaccion(con);
    		if(Utilidades.convertirAEntero(forma.getConsumosPorFacturarPacientesHospitalizados("numRegistros")+"") > 0) {

    			//Cargamos el path original. La ruta establecida en el web.xml concatenada con la ruta establecida en el parametro general
    			path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());

	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("consumosPorFacturarPacientesHospitalizados", periodo, usuario.getLoginUsuario());

	    		logger.info("===>Path Definitivo: " + path);
	    		logger.info("===>Nombre Archivo: " + nombre);
	    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extCsv);
    			
				//Validamos si el archivo plano se creo
				if(archivo) {
					//Validamos si el .zip del archivo plano si se ejecuto
					if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extCsv) != ConstantesBD.codigoNuncaValido) {
						logger.info("===>Se genero el archivo ZIP");
						forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt())+nombre+extZip);
		    			forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));

						forma.setArchivo(true);
						forma.setZip(true);
						UtilidadBD.finalizarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					else {
						logger.info("===>No se genero el archivo ZIP");
						forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
						forma.setArchivo(true);
						forma.setZip(false);
						UtilidadBD.finalizarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
				}
				else {
					forma.setMensaje(new ResultadoBoolean(true, "INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+path+nombre+extCsv+"!!!!!"));
					forma.setArchivo(false);
					forma.setZip(false);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
    		}
    		else { // no se encontraron registros
    			
    			forma.setMensaje(new ResultadoBoolean(true, "SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
    			forma.setArchivo(false);
    			forma.setZip(false);
    			UtilidadBD.finalizarTransaccion(con);
    			return mapping.findForward("principal");
    		}
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
	private ActionForward generarReporte(Connection con, ConsumosPorFacturarPacientesHospitalizadosForm forma, ConsumosPorFacturarPacientesHospitalizados mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "ConsumosPorFacturarPacientesHospitalizados.rptdesign";
		HashMap<String, Object> CondicionesParametros = new HashMap<String, Object>();
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
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        comp.insertLabelInGridPpalOfHeader(1, 1, "CONSUMOS POR FACTURAR PACIENTES HOSPITALIZADOS");
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        //Consultamos la condiciones seleccionadas para la consulta de movimiento deudor
        CondicionesParametros = mundo.consultarCondicionesConsumosPacientesHospitalizados(con, forma);
        
        //Insertamos los parametros de búsqueda seleccionados por el usuario
        comp.insertLabelInGridPpalOfHeader(2, 0, CondicionesParametros.get("parametros")+"");
        
        comp.obtenerComponentesDataSet("ConsumosPacientesHospitalizados");
        String newQuery = CondicionesParametros.get("consulta")+"";
        logger.info("===>Consulta en el BIRT con Condiciones: "+newQuery);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals("")) {
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
	private ActionForward accionEmpezar(Connection con, ConsumosPorFacturarPacientesHospitalizadosForm forma, UsuarioBasico usuario, ActionMapping mapping) {
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