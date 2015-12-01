package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
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

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.PacientesHospitalizadosForm;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.PacientesHospitalizados;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IDiagnosticosMundo;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class PacientesHospitalizadosAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(PacientesHospitalizadosAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof PacientesHospitalizadosForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				PacientesHospitalizadosForm forma = (PacientesHospitalizadosForm) form;
				PacientesHospitalizados mundo = new PacientesHospitalizados();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado = forma.getEstado();
				logger.warn("[PacientesHospitalizados]--->Estado: "+estado);

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
					if(!forma.getCodigoCentroAtencion().equals("") && !forma.getCodigoCentroAtencion().equals("null"))
					{
						//Cargamos el select de centros de costo segun el centro de atencion seleccionado
						String tipoArea = (ConstantesBD.codigoTipoAreaDirecto)+"";
						mundo.separarDatos(forma);
						forma.setCentroCosto(mundo.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, forma.getTipo(), forma.getCodigoCentroAtencion()));
						forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()+"")));
						//Llenamos el centro de atencion, el convenio y centro de costo con el que esta seleccionado con el fin de que no le haga reset
						forma.setCodigoCentroAtencion(forma.getCodigoCentroAtencion()+ConstantesBD.separadorSplit+forma.getNombreCentroAtencion()+"");
						forma.setConvenioSeleccionado(forma.getConvenioSeleccionado()+ConstantesBD.separadorSplit+forma.getNombreConvenioSeleccionado()+"");
						forma.setCodigoCentroCosto(forma.getCodigoCentroCosto()+ConstantesBD.separadorSplit+forma.getNombreCentroCosto()+"");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					return this.accionConsultarPacientesHospitalizados(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("volverPrincipal"))
				{
					return this.accionEmpezar(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("imprimir"))
				{
					return this.accionImprimirPacientesHospitalizados(con, forma, mundo, usuario, request, mapping);
				}
				if (estado.equals("cargarPaciente"))
				{
					return this.cargarPaciente(con, forma, mundo, usuario, paciente, request, mapping);
				}
				else if(estado.equals("ordenar"))
				{
					return this.ordenarListado(con, forma, mapping);
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
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
				logger.error("El form no es compatible con el form de PacientesHospitalizadosForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			return mapping.findForward("paginaError");
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	
	}
	
	/**
	 * Metodo que permite ordenar por un patron dado el criterio seleccionado
	 * @param con 
	 * @param forma
	 * @param mapping 
	 * @return
	 */
	private ActionForward ordenarListado(Connection con, PacientesHospitalizadosForm forma, ActionMapping mapping)
	{
		String[] indices={
			        		"numerocuenta_",
			        		"ingreso_",
							"numeroingreso_",
			        		"fechaingreso_",
			        		"horaingreso_",
			        		"codigopaciente_",
			        		"nombrepaciente_",
			        		"idpaciente_",
			        		"sexopaciente_",
			        		"fechanacimiento_",
			        		"centrocosto_",
			        		"responsable_",
			        		"fechacentrocosto_",
			        		"horacentrocosto_",
			        		"diagnostico_",
			        		"convenio_",
			        		"numerocama_",
			        		"egreso_"
        				};
		int numReg = Integer.parseInt(forma.getBusquedaPacientesHospitalizados("numRegistros")+"");
		forma.setBusquedaPacientesHospitalizados(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getBusquedaPacientesHospitalizados(), numReg));
		forma.setBusquedaPacientesHospitalizados("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * Metodo que carga el paciente en sesion
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente 
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarPaciente(Connection con, PacientesHospitalizadosForm forma, PacientesHospitalizados mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getBusquedaPacientesHospitalizados("codigopaciente_"+forma.getPosicion())+""));
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}
	
	/**
	 * Metodo que permite imprimir en PDF los resultados de la busqueda
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionImprimirPacientesHospitalizados(Connection con, PacientesHospitalizadosForm forma, PacientesHospitalizados mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "PacientesHospitalizados.rptdesign";
		String condiciones = "", parametros = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,1, "PACIENTES HOSPITALIZADOS");
        
        //Par�metros de B�squeda
        //****************************INICIO IMPRESI�N PARAMETROS DE B�SQUEDA*********************************
        parametros = "Centro Atenci�n ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+"], ";
        parametros += "Periodo ["+forma.getFechaInicial()+" - "+forma.getFechaFinal()+"], ";
        //Se valida si se filtro por el Centro de Costo
        if(UtilidadCadena.noEsVacio(forma.getCodigoCentroCosto()))
        	parametros += "Centro de Costo ["+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getCodigoCentroCosto()), usuario.getCodigoInstitucionInt())+"], ";
        else
        	parametros += "Centro de Costo [Todos], ";
        //Se valida si se filtro por la Convenio
        if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
        	parametros += "Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado()))+"], ";
        else
        	parametros += "Convenio [Todos], ";
        //Insertamos el usuario que genero el reporte
        parametros += "Usuario ["+usuario.getLoginUsuario()+"]";
        //****************************FIN IMPRESI�N PARAMETROS DE B�SQUEDA***********************************
        comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
        
        comp.obtenerComponentesDataSet("PacientesHospitalizados");
        
        //Filtramos la consulta por la via de ingreso "Hospitalizacion"
        condiciones += "cu.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" "; 
        //Filtramos la consulta por el estado del ingreso el cual debe estar "Abierto"
        condiciones += "AND i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' ";
        //Filtramos la consulta por el estado de la cuenta el cual debe estar "Cuenta Activa", "Facturada Parcial" y "Asociada"
        condiciones += "AND cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+", "+ConstantesBD.codigoEstadoCuentaAsociada+") ";
        //Filtramos la consulta por el centro de atencion. Como es requerido no se valida
		condiciones += "AND cc.centro_atencion = "+forma.getCodigoCentroAtencion()+" ";
		//Validamos la consulta por el tipo de paciente. Necesario validar
		if(!forma.getTipo().equals("") && !forma.getTipo().equals("null"))
		{
			if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoAmbos))
				condiciones += "AND cu.tipo_paciente IN ('"+ConstantesBD.tipoPacienteHospitalizado+"', '"+ConstantesBD.tipoPacienteCirugiaAmbulatoria+"') ";
			else
				condiciones += "AND cu.tipo_paciente = '"+forma.getTipo()+"' ";
		}
		//Filtramos la consulta por el centro de costo. Necesario validar
		if(!forma.getCodigoCentroCosto().equals("") && !forma.getCodigoCentroCosto().equals("null"))
			condiciones += "AND cu.area = "+forma.getCodigoCentroCosto()+" ";
		//Filtramos la consulta por el convenio seleccionado. Necesario validar
		if(!forma.getConvenioSeleccionado().equals("") && !forma.getConvenioSeleccionado().equals("null"))
			condiciones += "AND "+forma.getConvenioSeleccionado()+" IN (SELECT convenio FROM sub_cuentas WHERE sub_cuentas.ingreso = cu.id_ingreso)  ";
		//Filtramos la consulta por el piso seleccionado. Necesario validar
		if(!forma.getPisoSeleccionado().equals("") && !forma.getPisoSeleccionado().equals("null"))
			condiciones += "AND hab.piso = "+forma.getPisoSeleccionado()+" ";
		//Filtramos la consulta por el egreso. Como es requerido no se valida 
		if(forma.getIndicativoEgreso().equals(ConstantesIntegridadDominio.acronimoIndicativoConEgreso))
			condiciones += "AND getexisteegreso(cu.id) = true ";
		else if(forma.getIndicativoEgreso().equals(ConstantesIntegridadDominio.acronimoIndicativoSinEgreso))
			condiciones += "AND getexisteegreso(cu.id) = false ";
		//Filtramos la consulta por el rango de Fecha de Ingreso. Como es requerido no se valida
		condiciones += "AND i.fecha_ingreso BETWEEN to_date('"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"','yyyy-MM-dd') AND to_date('"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+" 23:59','yyyy-MM-dd HH24:MI') ";
        //Adicionamos a la consulta la clausula de ordenamiento inicial
		condiciones += "ORDER BY LOWER (getnombrepersona(i.codigo_paciente)) ";
		
        //String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
		
		String newQuery = "SELECT cu.id AS numerocuenta,"+
	                              "i.id AS ingreso,"+
	                              "i.consecutivo AS numeroingreso,"+
	                              "to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechaingreso,"+
	                              "i.hora_ingreso AS horaingreso,"+
	                              "i.codigo_paciente AS codigopaciente,"+
	                              "LOWER (getnombrepersona(i.codigo_paciente)) AS nombrepaciente,"+
	                              "getidpaciente(i.codigo_paciente) AS idpaciente,"+
	                              "CASE WHEN getsexopaciente(i.codigo_paciente) = 1 THEN 'M' ELSE 'F' END AS s,"+
	                              "getedadmesdiasimpleres(current_date, to_date(getfechanacimientopaciente(i.codigo_paciente), 'DD-MM-YYYY')) AS fechanacimiento,"+
	                              "cu.area AS codcentrocosto,"+
	                              "LOWER (getnomcentrocosto(cu.area)) AS centrocosto,"+
	                              "to_char(ah.fecha_admision, 'DD/MM/YYYY') AS fechacentrocosto,"+
	                              (System.getProperty("TIPOBD").equals("ORACLE")?"ah.hora_admision AS horacentrocosto,":"to_char(ah.hora_admision, 'HH24:MM') AS horacentrocosto,") +
	                              "LOWER (getultdiagpac(i.id)) AS diagnostico,"+
	                              "LOWER (getnombreconvenioresponsable(getCodigoConvenioXIngreso(i.id), i.id))	AS convenio,"+
	                              "getcamacuenta(cu.id, cu.via_ingreso) AS numerocama, " +
	                              (System.getProperty("TIPOBD").equals("ORACLE")?"CASE WHEN getexisteegreso(cu.id)=1 THEN 'CE' ELSE 'SE' END AS egreso ":"CASE WHEN getexisteegreso(cu.id) THEN 'CE' ELSE 'SE' END AS egreso ")+ 							
	                              "FROM "+
	                              "cuentas cu "+
	                              "INNER JOIN ingresos i ON (cu.id_ingreso = i.id) "+
	                              "INNER JOIN admisiones_hospi ah ON (cu.id = ah.cuenta) "+
	                              "INNER JOIN centros_costo cc ON (cu.area = cc.codigo) "+
	                              "LEFT OUTER JOIN camas1 cam ON (ah.cama = cam.codigo) "+
	                              "LEFT OUTER JOIN habitaciones hab ON (cam.habitacion = hab.codigo) "+
	                              "WHERE "+
	                              "1=2";
		
		newQuery = newQuery.replace("1=2", condiciones);
        logger.info("===>Consulta en el BIRT con Condiciones: "+newQuery);
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
		return mapping.findForward("listado");
	}

	/**
	 * Metodo 
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarPacientesHospitalizados(Connection con, PacientesHospitalizadosForm forma, PacientesHospitalizados mundo, UsuarioBasico usuario, ActionMapping mapping) throws IPSException
	{
		ArrayList<DtoSubCuentas> temp = new ArrayList<DtoSubCuentas>();
		forma.setBusquedaPacientesHospitalizados(mundo.consultarPacientesHospitalizados(con, forma));
		IDiagnosticosMundo diagnosticosMundo=ManejoPacienteFabricaMundo.crearDiagnosticosMundo();
		DtoDiagnostico parametros=new DtoDiagnostico();
		
		/*Recorremos el numero de registros del mapa con el fin de
		obtener los convenios particulares para consultar sus respectivos responsables*/ 
		for(int i=0; i<Utilidades.convertirAEntero(forma.getBusquedaPacientesHospitalizados("numRegistros")+""); i++)
		{
			temp = UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, Utilidades.convertirAEntero(forma.getBusquedaPacientesHospitalizados("ingreso_"+i)+""), true, new String[0], false, "" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Via de ingreso*/); 
			for(int j=0; j<temp.size(); j++)
			{
				String tempo = "";
				
				if(temp.size() > 1)
					tempo = ((DtoSubCuentas)temp.get(j)).getConvenio().getNombre()+", ";
				else
					tempo = ((DtoSubCuentas)temp.get(j)).getConvenio().getNombre();
				
				forma.setBusquedaPacientesHospitalizados("responsable_"+i, tempo);
			}
			//logger.info("====>Responsables "+i+": "+forma.getBusquedaPacientesHospitalizados("responsable_"+i));
			
			//Mantis 1176----Se elimina consulta del diagnostico con funcion (getultdiagpac(i.id)) del SqlBasePacientesHospitalizados
			//y se utiliza nueva consulta
			parametros=new DtoDiagnostico();
            parametros.setIdCuenta(Utilidades.convertirAEntero(forma.getBusquedaPacientesHospitalizados("numerocuenta_"+i).toString()));
            parametros.setIdIngreso(Utilidades.convertirAEntero(forma.getBusquedaPacientesHospitalizados("ingreso_"+i).toString()));
        	parametros=diagnosticosMundo.ultimoDiagnostico(parametros);
			forma.setBusquedaPacientesHospitalizados("diagnostico_"+i, parametros.getNombreCompletoDiagnostico());
		}
		
		HibernateUtil.endTransaction();
		return mapping.findForward("listado");
	}

	/**
	 * Metodo que ejecuta y carga los select de la busqueda
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param usuario
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, PacientesHospitalizadosForm forma, PacientesHospitalizados mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+ConstantesBD.separadorSplit+usuario.getCentroAtencion()+"");
		//Cargamos el select con todos los convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		String tipoArea = (ConstantesBD.codigoTipoAreaDirecto)+"";
		//Separamos el codigo y el nombre para realizar la consulta de centros de costo y de pisos
		mundo.separarDatos(forma);
		//Cargamos el select de centros de costo
		forma.setCentroCosto(mundo.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, forma.getTipo(), forma.getCodigoCentroAtencion()));
		//Cargamos el select de pisos
		forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()+"")));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+ConstantesBD.separadorSplit+usuario.getCentroAtencion()+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}