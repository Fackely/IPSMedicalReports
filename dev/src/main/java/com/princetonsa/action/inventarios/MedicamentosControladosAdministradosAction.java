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
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.MedicamentosControladosAdministradosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.MedicamentosControladosAdministrados;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;



/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre de 2008
 */

public class MedicamentosControladosAdministradosAction extends Action {
	
	// Objeto para manejar los logs de esta clase
	Logger logger = Logger.getLogger(MedicamentosControladosAdministradosAction.class);

	
	/**	 * Método execute del Action	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {


		Connection con = null;
		try{

			if (form instanceof MedicamentosControladosAdministradosForm) {



				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();

				if(con == null)	{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				MedicamentosControladosAdministradosForm forma = (MedicamentosControladosAdministradosForm) form;
				MedicamentosControladosAdministrados mundo = new MedicamentosControladosAdministrados();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();

				logger.warn("[MedicamentosControladosAdministrados]--->Estado: "+estado);

				if(estado == null) {
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar")) {
					return accionEmpezar(con, mundo, forma, usuario, mapping);
				}

				else if (estado.equals("redireccion")) { 		//ESTADO UTILIZADO PARA EL PAGER			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}

				else if(estado.equals("ordenar")) {				// ordenar los datos del listado
					return accionOrdenar(con, forma, mapping);
				}

				else if(estado.equals("nuevoArticulo")) {		//busqueda generica para el articulo
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				else if(estado.equals("recargar")) {
					this.accionRecargar(con, mundo, forma, usuario, mapping);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}


				else if(estado.equals("buscar")) {
					//direccionamos segun el tipo de salida escogido 
					// si no se escogio nada se muestra el resultado en pantalla
					if(forma.getTipoSalida().equals("")) {
						//cargamos en el mapa el resultado de la busqueda con los criterios enviados
						forma.setFrmListadoBusqueda(mundo.generarResultados(con, forma.getFrmCodigoCentroAtencion(), forma.getFrmViaIngresoSeleccionado(), forma.getFrmCentroCostoSeleccionado(), forma.getFrmConvenioSeleccionado(), forma.getFrmFechaInicial(), forma.getFrmFechaFinal(), forma.getFrmArticulo(), forma.getFrmTipoCodigo()));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listadobus");	
					}

					// si se escogio archivo plano o pdf
					else {
						logger.info("se escogio para pdf o plano");
						return accionImprimir(con, forma, mundo, usuario, mapping, request, institucion, "principal");
					}
				}


				else if(estado.equals("imprimir")) {
					return accionImprimir(con, forma, mundo, usuario, mapping, request, institucion, "listadobus");
				}


				else {
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else {
				logger.error("El form no es compatible con el form de MedicamentosControladosAdministrados");
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

	
	/**	 * Metodo que carga los select con la informacion inicial y resetea la forma
	 * @param con, forma, usuario, mapping
	 * @return	 */
	private ActionForward accionEmpezar(Connection con, MedicamentosControladosAdministrados mundo, MedicamentosControladosAdministradosForm forma, UsuarioBasico usuario, ActionMapping mapping) {
		forma.reset();

		//Cargamos el select con todos los convenios
		forma.setFrmConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		
		//Cargamos el select con todos los centros de atencion
		forma.setFrmCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		
		//Se selecciona el Centro de Atencion de acuerdo a la Sesion
		forma.setFrmCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		
		// Se cargan los datos de la via de Ingreso
		forma.setFrmViaIngreso(Utilidades.obtenerViasIngresoTipoPaciente(con));

		//Cargamos los centros de Costo Inicialmente
		this.accionRecargar(con, mundo, forma, usuario, mapping);
	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	
	/** Metodo que ejecuta la recarga del centro de costo segun el centro de atencion seleccionado
	 * @param con, mundo, forma, usuario, mapping
	 * @return */
	private void accionRecargar(Connection con, MedicamentosControladosAdministrados mundo, MedicamentosControladosAdministradosForm forma, UsuarioBasico usuario, ActionMapping mapping) {

		if(!forma.getFrmCodigoCentroAtencion().equals("") && !forma.getFrmCodigoCentroAtencion().equals("null")) {
		
			//Cargamos el select de centros de costo segun el centro de atencion seleccionado
			String tipoArea = (ConstantesBD.codigoTipoAreaDirecto)+"";
			logger.info("\n====>Centro de Atencion: "+forma.getFrmCodigoCentroAtencion());
			
			//Cargamos el select de centros de costo
			forma.setFrmCentrosCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, 
					true, Utilidades.convertirAEntero(forma.getFrmCodigoCentroAtencion()+""),false));
			logger.info("\n====>Centros de Costo filtrados por el Centro de Atencion: "+forma.getFrmCentrosCosto());
		}
		else {
			
			//Cargamos en el select de centros de costo todos los centros de costo acompañados del centro de atencion
			String tipoArea = (ConstantesBD.codigoTipoAreaDirecto)+"";  //estaban en SubAlmacen
			logger.info("\n====>Centro de Atencion: "+forma.getFrmCodigoCentroAtencion());
			
			forma.setFrmCentrosCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, true, 0,false));
			logger.info("\n====>Todos los Centros de Costo: "+forma.getFrmCentrosCosto());
		}
	}
	
	
	/** Metodo que ejecuta imprime la consulta realizada en un pdf o un archivo plano
	 * segun el tipo de salida seleccionado 
	 * @param (con, forma, mundo, usuario, mapping, request, institucion) 
	 * @return	 */
	private ActionForward accionImprimir(Connection con, MedicamentosControladosAdministradosForm forma, MedicamentosControladosAdministrados mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion, String mandar) {
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			this.generarReporte(con, forma, mundo, usuario, request, mapping);
		else {
			forma.setFrmListadoBusqueda(mundo.generarResultados(con, forma.getFrmCodigoCentroAtencion(), forma.getFrmViaIngresoSeleccionado(), forma.getFrmCentroCostoSeleccionado(), forma.getFrmConvenioSeleccionado(), forma.getFrmFechaInicial(), forma.getFrmFechaFinal(), forma.getFrmArticulo(), forma.getFrmTipoCodigo()));
			this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward(mandar);   //principal o listadobus
	}
	
	
	/** Metodo que permite exportar los datos de la consulta a Archivo Plano con ext. (.csv)
	 * @param (con, forma, mundo, usuario, request, mapping, institucion
	 */
	private ActionForward generarArchivoPlano(Connection con, MedicamentosControladosAdministradosForm forma, MedicamentosControladosAdministrados mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion) {

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
    		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual(con));

    		encabezado = "Usuario, No. Ingreso, Fecha / Hora, Centro Costo, Cod. Med., Medicamento, U. Med., Cant, Convenio, Vía Ingreso, Valor, Paciente, Tipo y # ID\n";
    		nombreReporte = "Medicamentos Controlados Administrados.";

    		datosArchivos = mundo.cargarMapaMedicamentosControladosAdministrados(forma.getFrmListadoBusqueda(), nombreReporte, forma.getFrmFechaInicial(), forma.getFrmFechaFinal(), encabezado, usuario.getLoginUsuario());
    			
    		//Iniciamos Transaccion
    		UtilidadBD.iniciarTransaccion(con);
    		if(Utilidades.convertirAEntero(forma.getFrmListadoBusqueda("numRegistros")+"") > 0) {

    			//Cargamos el path original. La ruta establecida en el web.xml concatenada con la ruta establecida en el parametro general
    			path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());

	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("medicamentosControladosAdministrados", periodo, usuario.getLoginUsuario());

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
	
	
	/** Método que ordena el resultado de la busqueda cuando se muestra en pantalla 
	 * según el criterio de ordenamiento establecido
	 * @param (con, forma, mapping)
	 * @return	 */
	private ActionForward accionOrdenar(Connection con, MedicamentosControladosAdministradosForm forma, ActionMapping mapping) {
		String[] indices={
				"usuario_",
				"numingreso_",
				"fechaingresoadmin_",
				"nomcentrocosto_",
				"codmed_",
				"nomarticulo_",
				"unidadmedida_",
				"cantidadart_",
				"nombreconvenio_",
				"viaingreso_",
				"valortotal_",
				"paciente_",
				"idpaciente_"
			};

		int numReg = Integer.parseInt(forma.getFrmListadoBusqueda("numRegistros")+"");
		forma.setFrmListadoBusqueda(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getFrmListadoBusqueda(), numReg));
		forma.setFrmListadoBusqueda("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadobus");
	}


	/** Metodo que permite crear exporta los resultados de la consulta a PDF
	 * @param con, forma, mundo, usuario, request, mapping	 */
	private ActionForward generarReporte(Connection con, MedicamentosControladosAdministradosForm forma, MedicamentosControladosAdministrados mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		String nombreRptDesign = "MedicamentosControladosAdministrados.rptdesign";
		HashMap<String, Object> CondicionesParametros = new HashMap<String, Object>();
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
        
        comp.insertLabelInGridPpalOfHeader(1, 1, "Medicamentos Controlados Administrados");
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        //Consultamos la condiciones seleccionadas para la consulta de medicamentos controlados administrados
        CondicionesParametros = mundo.consultarMediControAdmin(con, forma.getFrmCodigoCentroAtencion(), forma.getFrmViaIngresoSeleccionado(), forma.getFrmCentroCostoSeleccionado(), forma.getFrmConvenioSeleccionado(), forma.getFrmFechaInicial(), forma.getFrmFechaFinal(), forma.getFrmArticulo(), forma.getFrmTipoCodigo(), usuario.getCodigoInstitucion());
        
        //Insertamos los parametros de búsqueda seleccionados por el usuario
        comp.insertLabelInGridPpalOfHeader(2, 0, CondicionesParametros.get("parametros")+"");

        //cambiamos la consulta que esta en el reporte y el valor del tipo de codigo seleccionado
        
        comp.obtenerComponentesDataSet("MedicamentosControladosAdministrados");
        String newQuery = CondicionesParametros.get("consulta")+"";
        newQuery = newQuery.replace("?", "'"+forma.getFrmTipoCodigo()+"'");
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

}