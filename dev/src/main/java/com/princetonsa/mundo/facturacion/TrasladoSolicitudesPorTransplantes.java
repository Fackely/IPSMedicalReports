package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Listado;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.TrasladoSolicitudesPorTransplantesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.TrasladoSolicitudesPorTransplantesDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.servinte.axioma.fwk.exception.IPSException;





/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class TrasladoSolicitudesPorTransplantes
{

	private static Logger logger = Logger.getLogger(TrasladoSolicitudesPorTransplantes.class);
	
	//indices 
	
	public static final String [] indicesListado = {"capaBusquedaAvanzada0","fechaInicial1","fechaFinal2","servicio3","articuloMed4","cCostoEje5",
													"cCostoSol6","fechaSolicitud7_","numeroOrden8_","servArticulo9_","estadoHisCli10_","cenCosEje11_",
													"cenCosSol12_","cantidad13_","tiposolicitud14_","ingreso15","hidde16_","checkTodos17","descServ18",
													"descArt19","fechaApertura20_","codServArt21_","tipoSol22_","mosMen23_","algCheck24","solicitud25_",
													"centCostoSolicitado26_","autorizacion27_","codigoMedico28_","pool29_","cuenta30_","ingreso31_",
													"tipoCargo32_","centCostoSolicitante33_","centCostPpal34_","codigoMedicoResponde35_","codigoDetalleCargo36_"};
	
	
public static final String [] indicesListadoReceptores = {"idingreso0_","consecutivo1_","fechaIngreso2_","viaIngreso3_","paciente4_",
														  "identificacion5_","centroAtencion6","cuenta7_","codigoPersona8_"};
	
public static final String [] indicesTraslado = {"donante0","receptor1","institucion2","idTraslado3","solicitudTrasladada4","solicitudGenerada5",
												 "usuarioTraslado6"};

	/*-----------------------------------------------------------
	 *       METODOS TRANSLADO SOLICITUDES POR TRANSPLANTES
	 ------------------------------------------------------------*/
	
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static TrasladoSolicitudesPorTransplantesDao trasladoSolicitudesPorTransplantesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoSolicitudesPorTransplantesDao();
	}
	
	

	/**
	 * Metodo encargado de consultar los Servicios o los articulos
	 * de las solicitudes de un paciente.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * ------------------------------
	 * KEY'S DEL MAPA  CRITERIOS
	 * ------------------------------
	 * -- fechaInicial1
	 * -- fechaFinal2
	 * -- servicio3
	 * -- articuloMed4
	 * -- cCostoEje5
	 * -- cCostoSol6
	 * -- ingreso15
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- fechaSolicitud7_
	 * -- numeroOrden8_
	 * -- servArticulo9_
	 * -- estadoHisCli10_
	 * -- cenCosEje11_
	 * -- cenCosSol12_
	 * -- cantidad13_
	 * -- tiposolicitud14_
	 */
	private static HashMap consultarServiciosArticulos (Connection connection, HashMap criterios)
	{
		return trasladoSolicitudesPorTransplantesDao().consultarServiciosArticulos(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar los pacientes receptores
	 * @param connection
	 * @param criterios
	 * ------------------------
	 * KEY'S DEL MAPA CRITERIOS
 	 * ------------------------
 	 * -- centroAtencion6
 	 * @return MAPA
 	 * ----------------------------------------
 	 * KEY'S DEL MAPA QUE RETORNA
 	 * ----------------------------------------
 	 * -- idingreso0_
 	 * -- consecutivo1_
 	 * -- fechaIngreso2_
 	 * -- viaIngreso3_
 	 * -- paciente4
 	 * -- identificacion5_
 	 */
	private static HashMap consultarPacientesReceptores (Connection connection, HashMap criterios)
	{
		return trasladoSolicitudesPorTransplantesDao().consultarPacientesReceptores(connection, criterios);
	}
	
	/**
	 * Metodo encargado de cambia el estado de actualizar
	 * el estado de la facturacion en la tabla det_cargos
	 * @param connection
	 * @param solicitud
	 * @param estado
	 * @return false/true
	 * 
	 */
	public static boolean actualizarEstadoFacturacion (Connection connection, String solicitud, String estado,String codigoDetalleCargo)
	{
		return trasladoSolicitudesPorTransplantesDao().actualizarEstadoFacturacion(connection, solicitud, estado,codigoDetalleCargo);
	}
	
	
	
	/**
	 * Metodo encargado de consultar si existe un translado
	 * @param connection
	 * @param datos
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- donante0
	 * -- receptor1
	 * -- institucion2
	 * @return codigoTranslado (-1 si no existe)
	 */
	private static int  existeTraslado (Connection connection, HashMap datos)
	{
		return trasladoSolicitudesPorTransplantesDao().existeTraslado(connection, datos);
	}
	
	/**
	 * Metodo encargado de identificar si existe o no traslado
	 * @param connection
	 * @param ingDonante
	 * @param ingReceptor
	 * @param institucion
	 * @return
	 */
	public static int  existeTraslado (Connection connection, String ingDonante, String ingReceptor, String institucion)
	{
		HashMap datos = new HashMap ();
		//donante
		datos.put(indicesTraslado[0], ingDonante);
		//receptor
		datos.put(indicesTraslado[1], ingReceptor);
		//institucion
		datos.put(indicesTraslado[2], institucion);
		
		return existeTraslado(connection, datos);
	}
	
	
	/**
	 * Metodo encargado insertar el detalle del traslado de solitides
	 * en la tabla ( det_tras_sol_transplante )
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEYS' DEL MAPA DATOS
	 * ---------------------------
	 * -- idTraslado3
	 * -- solicitudTrasladada4
	 * -- solicitudGenerada5
	 * -- usuarioTraslado6
	 * @return false/true
	 */
	private static boolean insertarDetTraslado (Connection connection, HashMap datos)
	{
		return trasladoSolicitudesPorTransplantesDao().insertarDetTraslado(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de insertar el detalle del traslado
	 * @param connection
	 * @param idTraslado
	 * @param solTrasladada
	 * @param solGenerada
	 * @param usuario
	 * @return
	 */
	public static boolean insertarDetTraslado (Connection connection, String idTraslado, String solTrasladada,String solGenerada,String usuario)
	{
		HashMap datos = new HashMap ();
		//id traslado
		datos.put(indicesTraslado[3], idTraslado);
		//solicitud trasladada
		datos.put(indicesTraslado[4], solTrasladada);
		//solicitud generada
		datos.put(indicesTraslado[5], solGenerada);
		//usuario traslado
		datos.put(indicesTraslado[6], usuario);
		
		return insertarDetTraslado(connection, datos);
	}
	
	
	
	/**
	 * Metodo encargado de insertar los datos del traslado en la tabla ( tras_sol_transplante )
	 * @param connection
	 * @param datos
	 * -----------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------
	 * -- donante0
	 * -- receptor1
	 * -- institucion2
	 * @return false/true
	 */
	private static int insertarEncabTraslado (Connection connection, HashMap datos)
	{
		return trasladoSolicitudesPorTransplantesDao().insertarEncabTraslado(connection, datos);
	}
	
	/**
	 * Metodo encargado de insertar un traslado
	 * @param connection
	 * @param ingDonante
	 * @param ingReceptor
	 * @param institucion
	 * @return
	 */
	public static int insertarEncabTraslado (Connection connection, String ingDonante,String ingReceptor,String institucion)
	{
		HashMap datos = new HashMap ();
		//ingreso donante
		datos.put(indicesTraslado[0], ingDonante );
		//ingreso receptor
		datos.put(indicesTraslado[1], ingReceptor);
		//institucion
		datos.put(indicesTraslado[2], institucion);
		
		return insertarEncabTraslado(connection, datos);
	}
	
	
	
	/**
	 * Metodo encargado de consultar los pacientes receptores
	 * @param connection
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultarPacienteReceptores (Connection connection, String centroAtencion)
	{
		HashMap criterios = new HashMap ();
		criterios.put(indicesListadoReceptores[6], centroAtencion);
		
		return consultarPacientesReceptores(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de buscar la informacion de
	 * servicios y articulos por el numero de ingreso
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public static HashMap busquedaServiciosArticulos (Connection connection,String ingreso)
	{
		HashMap criterios = new HashMap ();
		criterios.put(indicesListado[15], ingreso);
		return trasladoSolicitudesPorTransplantesDao().consultarServiciosArticulos(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de realizar la busqueda avanzada
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static void busquedaAvanzadaServiciosArticulos (Connection connection,TrasladoSolicitudesPorTransplantesForm forma, PersonaBasica paciente)
	{
		HashMap criterios = new HashMap ();
		
		
		//Rango de fechas
		if (UtilidadCadena.noEsVacio(forma.getListadoSolicitudes(indicesListado[1])+"") && UtilidadCadena.noEsVacio(forma.getListadoSolicitudes(indicesListado[2])+""))
		{
			//fecha inicial
			criterios.put(indicesListado[1], forma.getListadoSolicitudes(indicesListado[1]));
			//fecha final
			criterios.put(indicesListado[2], forma.getListadoSolicitudes(indicesListado[2]));
		}
		//servicio
		if (UtilidadCadena.noEsVacio(forma.getListadoSolicitudes(indicesListado[3])+""))
			criterios.put(indicesListado[3], forma.getListadoSolicitudes(indicesListado[3]));
		
		//articulo
		if (UtilidadCadena.noEsVacio(forma.getListadoSolicitudes(indicesListado[4])+""))
			criterios.put(indicesListado[4], forma.getListadoSolicitudes(indicesListado[4]));
		
		//centro costo que ejecuta
		if (UtilidadCadena.noEsVacio(forma.getListadoSolicitudes(indicesListado[5])+"") && !(forma.getListadoSolicitudes(indicesListado[5])+"").equals(ConstantesBD.codigoNuncaValido+""))
			criterios.put(indicesListado[5], forma.getListadoSolicitudes(indicesListado[5]));
		
		//centro costo que solicita
		if (UtilidadCadena.noEsVacio(forma.getListadoSolicitudes(indicesListado[6])+"") &&  !(forma.getListadoSolicitudes(indicesListado[6])+"").equals(ConstantesBD.codigoNuncaValido+""))
			criterios.put(indicesListado[6], forma.getListadoSolicitudes(indicesListado[6]));
		
		//ingreso
		criterios.put(indicesListado[15], paciente.getCodigoIngreso());
		
		String fI = forma.getListadoSolicitudes(indicesListado[1])+"";
		String fF = forma.getListadoSolicitudes(indicesListado[2])+"";
		String serv = forma.getListadoSolicitudes(indicesListado[3])+"";
		String art = forma.getListadoSolicitudes(indicesListado[4])+"";
		String cce = forma.getListadoSolicitudes(indicesListado[5])+"";
		String ccs = forma.getListadoSolicitudes(indicesListado[6])+"";
		String descServ = forma.getListadoSolicitudes(indicesListado[18])+"";
		String descArt = forma.getListadoSolicitudes(indicesListado[19])+"";
		String algchek = forma.getListadoSolicitudes(indicesListado[23])+"";
		
		//se mete la busqueda
		 forma.setListadoSolicitudes(trasladoSolicitudesPorTransplantesDao().consultarServiciosArticulos(connection, criterios));
		//se pone la campa de la bu
		 forma.setListadoSolicitudes(indicesListado[0], true);
		 
		 forma.setListadoSolicitudes(indicesListado[1], fI);
		forma.setListadoSolicitudes(indicesListado[2], fF);
		forma.setListadoSolicitudes(indicesListado[3], serv);
		forma.setListadoSolicitudes(indicesListado[4], art);
		forma.setListadoSolicitudes(indicesListado[5], cce);
		forma.setListadoSolicitudes(indicesListado[6], ccs);
		forma.setListadoSolicitudes(indicesListado[18], descServ);
		forma.setListadoSolicitudes(indicesListado[19], descArt);
		forma.setListadoSolicitudes(indicesListado[23], algchek);
		
	}
	
	
	
	public static ActionForward empezar (Connection connection,PersonaBasica paciente, HttpServletRequest request,ActionMapping mapping, TrasladoSolicitudesPorTransplantesForm forma,UsuarioBasico usuario ) throws SQLException
	{
		
		//se valida que el paciente este cargado den session
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(connection, paciente);
		if(!resp.puedoSeguir)
		{
			
			UtilidadBD.cerrarConexion(connection); 
			return ComunAction.accionSalirCasoError(mapping, request, connection, logger, resp.textoRespuesta, resp.textoRespuesta, true);
		}
		else
			if (!paciente.getTransplante().equals(ConstantesIntegridadDominio.acronimoIndicativoDonante))
			{
				UtilidadBD.cerrarConexion(connection); 
				return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "error.TransplanteNoValido", "error.TransplanteNoValido", true);
			}
			else//se valida que el paciente tenga un ingreso abierto
				if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(connection, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
				{
				
					UtilidadBD.cerrarConexion(connection); 
					return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "errors.paciente.noIngresoSesion", "errors.paciente.noIngresoSesion", true);
				}
				else
				{
				
					//se toma el estado de la cuenta
					int  estadoCuenta=UtilidadesHistoriaClinica.obtenerEstadoCuenta(connection, paciente.getCodigoCuenta()).getCodigo();
					// se valida que el estado de la cuenta sea (Activa,Asociada,Facturada Parcial)
					if (estadoCuenta!=ConstantesBD.codigoEstadoCuentaActiva && estadoCuenta!=ConstantesBD.codigoEstadoCuentaAsociada && estadoCuenta!=ConstantesBD.codigoEstadoCuentaFacturadaParcial)
					{
						UtilidadBD.cerrarConexion(connection); 
						return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "error.paciente.estadoInvalido", "Activa, Asociada o Facturada Parcial", true);
				
					}
				}
		
		forma.setFechaApertura(UtilidadesManejoPaciente.obtenerFechaAperturaIngreso(connection, paciente.getCodigoIngreso()+""));
		
		forma.setListadoSolicitudes(busquedaServiciosArticulos(connection, paciente.getCodigoIngreso()+""));
		iniciar(connection, forma, usuario);
		mostrarMensaje(forma);
		UtilidadBD.cerrarConexion(connection); 
		return mapping.findForward("listado");
	}
	
	
	public static void iniciar (Connection connection,TrasladoSolicitudesPorTransplantesForm forma, UsuarioBasico usuario)
	{
		forma.setListadoSolicitudes(indicesListado[0], false);
		//se obtiene los centros de costo con tipo de area directo
		forma.setCentCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false, 0));
	}
	
	/**
	 * Se encarga del ordenamiento almacenando los criteros
	 * de busqueda.
	 * 
	 * @param forma
	 */
	public static void accionOrdenarMapa(TrasladoSolicitudesPorTransplantesForm forma)
	{
		
		int numReg = Integer.parseInt(forma.getListadoSolicitudes("numRegistros")+"");
		//--------------------------------------------------------------
		//se almacenan los criterios de busqueda y el estado de la capa
		boolean capa = UtilidadTexto.getBoolean(forma.getListadoSolicitudes(indicesListado[0])+"");
		String fI = forma.getListadoSolicitudes(indicesListado[1])+"";
		String fF = forma.getListadoSolicitudes(indicesListado[2])+"";
		String serv = forma.getListadoSolicitudes(indicesListado[3])+"";
		String art = forma.getListadoSolicitudes(indicesListado[4])+"";
		String cce = forma.getListadoSolicitudes(indicesListado[5])+"";
		String ccs = forma.getListadoSolicitudes(indicesListado[6])+"";
		String algCheck = forma.getListadoSolicitudes(indicesListado[24])+"";
		
		forma.setListadoSolicitudes(Listado.ordenarMapa(indicesListado, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoSolicitudes(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		
		forma.setListadoSolicitudes("numRegistros",numReg+"");
		forma.setListadoSolicitudes(indicesListado[0], capa);
		forma.setListadoSolicitudes(indicesListado[1], fI);
		forma.setListadoSolicitudes(indicesListado[2], fF);
		forma.setListadoSolicitudes(indicesListado[3], serv);
		forma.setListadoSolicitudes(indicesListado[4], art);
		forma.setListadoSolicitudes(indicesListado[5], cce);
		forma.setListadoSolicitudes(indicesListado[6], ccs);
		forma.setListadoSolicitudes(indicesListado[24], algCheck);
	}
	
	/**
	 * Se encarga del ordenamiento almacenando los criteros
	 * de busqueda.
	 * 
	 * @param forma
	 */
	public static void accionOrdenarMapaReceptores(TrasladoSolicitudesPorTransplantesForm forma)
	{
		int numReg = Integer.parseInt(forma.getListadoIngresosReceptores("numRegistros")+"");
		//se almacena el centro de atencion
		String centroAtencion = forma.getListadoIngresosReceptores(indicesListadoReceptores[6])+"";
		
		
		forma.setListadoIngresosReceptores(Listado.ordenarMapa(indicesListadoReceptores, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoIngresosReceptores(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoIngresosReceptores("numRegistros", numReg);
		forma.setListadoIngresosReceptores(indicesListadoReceptores[6], centroAtencion);
	}
		
		
	
	/**
	 * Metodo encargado de checkear y descheckear las solicitudes
	 * @param forma
	 */
	public static void checkTodos (TrasladoSolicitudesPorTransplantesForm forma)
	{
		int numReg=Utilidades.convertirAEntero(forma.getListadoSolicitudes("numRegistros")+"");
		String camb="";
		if ((forma.getListadoSolicitudes(indicesListado[17])+"").equals(ConstantesBD.acronimoSi))
		{
			camb=ConstantesBD.acronimoNo;
			forma.setListadoSolicitudes(indicesListado[17], ConstantesBD.acronimoNo);
		}
		else
		{
			camb=ConstantesBD.acronimoSi;
			forma.setListadoSolicitudes(indicesListado[17], ConstantesBD.acronimoSi);
		}
		
		for (int i=0;i<numReg;i++)
			forma.setListadoSolicitudes(indicesListado[16]+i, camb);
		
//		se verifica si se ha chequedao alguno
		if(algunoChequeado(forma))
			forma.setListadoSolicitudes(indicesListado[24], ConstantesBD.acronimoSi);
		else
			forma.setListadoSolicitudes(indicesListado[24], ConstantesBD.acronimoNo);
		
	}
	
	/**
	 * Metodo encargado de checkear o descheckear 
	 * @param forma
	 */
	public static void verificarChecks (TrasladoSolicitudesPorTransplantesForm forma)
	{
		logger.info("\n \n entre a verificarChecks check -->"+forma.getListadoSolicitudes(indicesListado[16]+forma.getIndex()));
		String camb="";
		if ((forma.getListadoSolicitudes(indicesListado[16]+forma.getIndex())+"").equals(ConstantesBD.acronimoSi))
		{
			camb=ConstantesBD.acronimoNo;
			forma.setListadoSolicitudes(indicesListado[16]+forma.getIndex(), ConstantesBD.acronimoNo);
		}
		else
		{
			camb=ConstantesBD.acronimoSi;
			forma.setListadoSolicitudes(indicesListado[16]+forma.getIndex(), ConstantesBD.acronimoSi);
		}
		
		int numReg=Utilidades.convertirAEntero(forma.getListadoSolicitudes("numRegistros")+"");
		String ing = forma.getListadoSolicitudes(indicesListado[8]+forma.getIndex())+"";
		
		
		for (int i=0;i<numReg;i++)
		{
			String ingTmp = forma.getListadoSolicitudes(indicesListado[8]+i)+"";
			if (ing.equals(ingTmp))
				forma.setListadoSolicitudes(indicesListado[16]+i, camb);
		}
		//se verifica si se ha chequedao alguno
		if(algunoChequeado(forma))
			forma.setListadoSolicitudes(indicesListado[24], ConstantesBD.acronimoSi);
		else
			forma.setListadoSolicitudes(indicesListado[24], ConstantesBD.acronimoNo);
		
		logger.info("\n \n sale de verificarChecks check -->"+forma.getListadoSolicitudes(indicesListado[16]+forma.getIndex()));
	}
	
	
	
	public static void mostrarMensaje (TrasladoSolicitudesPorTransplantesForm forma)
	{
		int numReg=Utilidades.convertirAEntero(forma.getListadoSolicitudes("numRegistros")+"");
		
		for (int i=0; i<numReg;i++)
		{
			String orden =forma.getListadoSolicitudes(indicesListado[8]+i)+"";
			if (estaRepetido(orden, forma.getListadoSolicitudes()))
				forma.setListadoSolicitudes(indicesListado[23]+i, ConstantesBD.acronimoSi);
			else
				forma.setListadoSolicitudes(indicesListado[23]+i, ConstantesBD.acronimoNo);
				
		
		}
	}
	
	
	public static boolean estaRepetido (String orden, HashMap mapa)
	{
		int numReg=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
		String orden2="";
		int cant=0;
		for (int i=0; i<numReg;i++)
		{
			orden2 = mapa.get(indicesListado[8]+i)+"";
				if (orden2.equals(orden))
					cant++;
				if (cant>1)
					return true;
		}
		return false;
	}
	
	/**
	 * metodo encargado de verificar si algun servcio se encuentra chechekeado
	 * @param forma
	 * @return
	 */
	public static boolean algunoChequeado(TrasladoSolicitudesPorTransplantesForm forma)
	{
		int numReg=Utilidades.convertirAEntero(forma.getListadoSolicitudes("numRegistros")+"");
		
		for(int i=0;i<numReg;i++)
			if ((forma.getListadoSolicitudes(indicesListado[16]+i)+"").equals(ConstantesBD.acronimoSi))
				return true;
			
		return false;
	}
	
	
	
	public boolean guardar (Connection connection,TrasladoSolicitudesPorTransplantesForm forma,HttpServletRequest request,ActionMapping mapping, UsuarioBasico medico, PersonaBasica paciente) throws SQLException,IPSException
	{
		logger.info("\n\n lsitado solicitudes -->"+forma.getListadoSolicitudes());
		HashMap datos = new HashMap ();
		datos.put("numRegistros", 0);
		int numReg = Utilidades.convertirAEntero(forma.getListadoSolicitudes("numRegistros")+"");
		ArrayList insertados= new ArrayList ();
		PersonaBasica pacienteATrans = new PersonaBasica();
		boolean inserto=true;
		int codigoPersona=Utilidades.convertirAEntero(forma.getListadoIngresosReceptores(indicesListadoReceptores[8]+forma.getIndex())+"");
	//	pacienteATrans.cargar(connection, codigoPersona);
		pacienteATrans.cargarPaciente(connection,  codigoPersona, medico.getCodigoInstitucionInt()+"", medico.getCodigoCentroAtencion()+"");
		logger.info("pacien a trasladar cuenta->"+pacienteATrans.getCodigoCuenta()+" id -->"+pacienteATrans.getNumeroIdentificacionPersona());
		logger.info("\n \n ############ entro 1 ############ numregistros -->"+numReg);
		for (int i=0;i<numReg;i++)
		{
			String solicitud=forma.getListadoSolicitudes(indicesListado[25]+i)+"";
			logger.info("\n \n solicitud -->"+solicitud);
			logger.info("\n\n inserto-->"+inserto+" check-->"+forma.getListadoSolicitudes(indicesListado[16]+i)+" !yaFueInsertado-->"+!yaFueInsertado(insertados, solicitud));
			if (inserto && (forma.getListadoSolicitudes(indicesListado[16]+i)+"").equals(ConstantesBD.acronimoSi) && !yaFueInsertado(insertados, solicitud))
			{
				logger.info("\n \n ############ entro 2 ############");
				datos=sacarSeleccionadosAgrupados(forma.getListadoSolicitudes(), forma.getListadoSolicitudes(indicesListado[25]+i)+"");
				if ((forma.getListadoSolicitudes(indicesListado[32]+i)+"").equals("art"))
				{
					logger.info("\n \n ############ entro 3 ############");
					inserto=flujoInsertarMedicamentos(connection, pacienteATrans.getCodigoCuenta(), Utilidades.convertirAEntero(datos.get(indicesListado[26]+"0")+""), pacienteATrans, request, mapping, datos, medico,paciente,solicitud,forma.getListadoSolicitudes(indicesListado[34]+i)+"");
					if (inserto)
						insertados.add(solicitud);
				}
				else
				{
					logger.info("\n \n ############ entro 4 ############");
					inserto=flujoInsertarServicios(connection, request, mapping, datos, pacienteATrans.getCodigoCuenta(), pacienteATrans, medico,paciente,solicitud);
					if (inserto)
						insertados.add(solicitud);
				}
			}
		}
		
		return inserto;
	}
	
public static boolean yaFueInsertado (ArrayList insertados, String solicitud)	
{
	for (int i=0;i<insertados.size();i++)
		if ((insertados.get(i)+"").equals(solicitud))
			return true;
	
	return false;
}
	
/**
 * Metodo encargado de sacar del mapa los servicios o
 * articulos seleccionados.
 * @param serviciosArticulos
 * @return
 */
public static HashMap sacarSeleccionadosAgrupados (HashMap serviciosArticulos,String solicitud)
{
	HashMap result= new HashMap ();
	result.put("numRegistros", 0);
	int cont =0;
	
	int numReg = Utilidades.convertirAEntero(serviciosArticulos.get("numRegistros")+"");
	
	for (int i=0;i<numReg;i++)
	{
		if ((serviciosArticulos.get(indicesListado[16]+i)+"").equals(ConstantesBD.acronimoSi) && (serviciosArticulos.get(indicesListado[25]+i)+"").equals(solicitud) )
		{
			Listado.copyMapOnIndexMap(serviciosArticulos, result, indicesListado, cont, i);
			cont++;
		}
	}
	
	Listado.quitarNullMapa(result, indicesListado);

	return result;
}
	




	
private  boolean flujoInsertarMedicamentos(	Connection con,
			int codigoCuenta,
			int centCostoSolicitado,
			PersonaBasica paciente,
			HttpServletRequest request,
			ActionMapping mapping,
			HashMap medicamentos,
			UsuarioBasico user,
			PersonaBasica personaOri,
			String solicitudOri,
			String centroCostoPpal) throws SQLException,IPSException
{
	logger.info("\n entre a flujoInsertarMedicamentos medicamentos--> "+medicamentos+" cuenta-->"+codigoCuenta+" cC solicitante --> "+centCostoSolicitado);
	
	int numeroSolicitud=ConstantesBD.codigoNuncaValido;
	boolean inserto=false;
	int codigoDespacho= ConstantesBD.codigoNuncaValido; 
	int codigoTraslado=ConstantesBD.codigoNuncaValido;
	//PRIMERO SE HACE UN INSERT DE UNA SOLICITUD GENERAL
	try
	{
		numeroSolicitud= this.insertarSolicitudBasicaTransaccional(con,codigoCuenta, paciente, user, ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos, centCostoSolicitado,ConstantesBD.codigoNuncaValido);
	
		if(numeroSolicitud<=0)
		{
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			inserto=false;
			//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud b�sica (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
		}
		else
			inserto=true;
	}
	catch(SQLException sqle)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		inserto=false;
		//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud b�sica (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	
	
	//SEGUNDO SE HACE UNA SOLICITUD DE MEDICAMENTOS BASICA
	if (inserto)
		inserto= this.insertarSolicitudMedicamentosTransaccional(con, numeroSolicitud,centCostoSolicitado+"",user,centroCostoPpal);
	if(!inserto)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud de medicamentos b�sica (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	
	//TERCERO SE HACE EL INSERT DEL DETALLE DE LA SOLICITUD DE MEDICAMENTOS
	if (inserto)
		inserto=insertarDetalleSolicitudMedicamentosTransaccional(con,medicamentos, numeroSolicitud, user.getCodigoInstitucionInt());
	if(!inserto)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar el detalle de la solicitud de medicamentos b�sica (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	
	//CUARTO SE HACE EL INSERT DEL DESPACHO BASICO
	if(inserto)
		codigoDespacho=insertarDespachoBasicoTransaccional(con, user, numeroSolicitud);
	if(codigoDespacho<=0)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		inserto=false;
		//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar el despacho b�sico (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	
	//QUINTO SE HACE EL INSERT DEL DETALLE DEL DESPACHO, aca tambien se inserta el valor costo promedio
	if (inserto)
		inserto=insertarDetalleDespachoTransaccional(con, medicamentos, codigoDespacho, user, paciente);
	if(!inserto)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	//	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar el detalle del despacho b�sico (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	
	//SEXTO SE HACE EL CAMBIO DEL ESTADO DE LA SOLICITUD
	if (inserto)
		inserto=this.cambiarEstadoMedicoSolicitudTransaccional(con, numeroSolicitud/*, ""*/);
	if(!inserto)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al cambiar el estado medico de la solicitud (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	
	//SEPTIMO SE TRATA DE GENERAR EL CARGO
	if (inserto)
		inserto=generarInfoSubCuentaCargoMedicamentos(con, user, paciente, numeroSolicitud, medicamentos, codigoCuenta+"");
	if(!inserto)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de generar el cargo (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	else
	{
		//COPIAR LA JUSTICICACION DEL ARTICULO
		///validarInsertarJustificacion(con, user, numeroSolicitud, medicamentos);
	}
	
	//SE INSERTA EL USUARIO QUE HIZO EL CARGO DIRECTO Y SE LE PONE RECARGO -1
	if (inserto)
		inserto=this.insertarInfoCargosDirectosTransaccional(con, numeroSolicitud, user.getLoginUsuario(), -1, -1,"");
	if(!inserto)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de almacenar el usuario que hizo el cargo directo (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
	}
	
	//si todo salio bien entonces
	if (inserto)
		inserto=actualizarEstadoFacturacion(con, medicamentos.get(indicesListado[25]+"0")+"", ConstantesBD.codigoEstadoFacturacionAnulada+"", medicamentos.get(indicesListado[36]+"0")+"");

	if (inserto)
	{
		codigoTraslado=existeTraslado(con, personaOri.getCodigoIngreso()+"", paciente.getCodigoIngreso()+"", user.getCodigoInstitucion());
		if (codigoTraslado<0)
			codigoTraslado=insertarEncabTraslado(con, personaOri.getCodigoIngreso()+"", paciente.getCodigoIngreso()+"", user.getCodigoInstitucion());
			if (codigoTraslado<0)
				inserto=false;
			else
				inserto=insertarDetTraslado(con, codigoTraslado+"", solicitudOri, numeroSolicitud+"", user.getLoginUsuario());
	}
	
	if (inserto)
		this.terminarTransaccion(con);
	else
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	
	return inserto;
	
	}


/**
 * M�todo que termina la transaccion
 * @param con
 * @throws SQLException
 */
public void terminarTransaccion(Connection con) throws SQLException
{
    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
    myFactory.endTransaction(con);
}
		
private int insertarSolicitudBasicaTransaccional(	Connection con, 
		int codigoCuenta,
		PersonaBasica paciente,
		UsuarioBasico user,
		int tipoSolicitud,
		int centroCostoSolicitado,
		int especilidadSolicitada
	   ) throws SQLException
{
	Solicitud objectSolicitud= new Solicitud();
	int numeroSolicitudInsertado=0;
	objectSolicitud.clean();
	objectSolicitud.setFechaSolicitud(UtilidadFecha.getFechaActual());
	objectSolicitud.setHoraSolicitud(UtilidadFecha.getHoraActual());
	objectSolicitud.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
	//objectSolicitud.setNumeroAutorizacion(numeroAutorizacion);
	objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
	objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
	objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
	objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(centroCostoSolicitado));
	
	logger.info("\n\nCuenta A insertar--->"+codigoCuenta+"**********************************************************************************\n");
	objectSolicitud.setCodigoCuenta(codigoCuenta);
	objectSolicitud.setCobrable(true);
	objectSolicitud.setVaAEpicrisis(false);
	objectSolicitud.setUrgente(false);
	//primero lo inserto como pendiente, pero si mas adelante es exitoso el cargo entonces le hago un update a  cargada
	objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCCargoDirecto));
	
	// Cambios Segun Anexo 809
	objectSolicitud.setEspecialidadSolicitadaOrdAmbulatorias(especilidadSolicitada);
	// Fin Cambios Segun Anexo 809
	
	try
	{ 
		numeroSolicitudInsertado=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.inicioTransaccion);
	}
	catch(SQLException sqle)
	{
		logger.warn("Error en la transaccion del insert en la solicitud b�sica");
		return 0;
	}
	return numeroSolicitudInsertado;
}
	
	
/**
 * Metodo que inserta la solcitud de medicamentos b�sica en una transacci�n, recibe como par�metro principal
 * el numeroSolicitud 
 * @param con
 * @param numeroSolicitud
 * @return
 */
private boolean insertarSolicitudMedicamentosTransaccional(Connection con, int numeroSolicitud, String centCostoSolicitado,UsuarioBasico usuario,String centroCostoPpal)
{
	logger.info("\n entre a insertarSolicitudMedicamentosTransaccional -->"+centCostoSolicitado);
    SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
    objetoSolicitudMedicamentos.setNumeroSolicitud(numeroSolicitud);
    objetoSolicitudMedicamentos.setObservacionesGenerales("");
    
    objetoSolicitudMedicamentos.setCentroCostoPrincipal(centroCostoPpal);
    
    logger.info("\n centro de costo principal -->"+objetoSolicitudMedicamentos.getCentroCostoPrincipal());
 
    int resultado=objetoSolicitudMedicamentos.insertarUnicamenteSolMedicamentosTransaccional(con);
    if(resultado<=0)
       return false;
    else
        return true;
}	
	
	
	

/**
 * M�todo que inserta el detallle de la solicitud de medicamentos en una transaccion
 * @param con
 * @param cargosForm
 * @param numeroSolicitud
 * @return
 */
private boolean insertarDetalleSolicitudMedicamentosTransaccional(	Connection con, HashMap medicamentos,int numeroSolicitud, int codigoInstitucion)
{
    SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
    int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
    int resp=ConstantesBD.codigoNuncaValido; 
    
    int numReg = Utilidades.convertirAEntero(medicamentos.get("numRegistros")+"");
    
    for (int i=0; i<numReg; i ++)
    {   
            try
            {
                temporalCodigoArticulo= Integer.parseInt(medicamentos.get(indicesListado[21]+i)+"");
            }
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                return false;
            }
            resp=objetoSolicitudMedicamentos.insertarUnicamenteDetalleSolicitudMedicamentos(con, numeroSolicitud, temporalCodigoArticulo, ValoresPorDefecto.getNumDiasTratamientoMedicamentos(codigoInstitucion), 0);
            if (resp<1)
			{
			    logger.warn("Error en el insert del detalle de solicitud medicamentos del codigo del articulo con indice ="+i );
				return false;
			}
        
    }  
    return true;
}
	

/**
 * Metodo que inserta el despacho b�sico en una transaccion, este metodo retorna el codigo del despacho
 * @param con
 * @param user
 * @param numeroSolicitud
 * @return codigoDespacho
 */
private int insertarDespachoBasicoTransaccional(Connection con, UsuarioBasico user, int numeroSolicitud)
{
    int codigoDespacho=ConstantesBD.codigoNuncaValido;
    DespachoMedicamentos despacho = new DespachoMedicamentos();
    despacho.setUsuario(user.getLoginUsuario());
    despacho.setNumeroSolicitud(numeroSolicitud);
    //este se puso en true para que pudiera generar el cargo, pero en realidad debia ser false, 
    //esto se acordo con Margarita y Nury el 2005-07-01
    despacho.setEsDirecto(true);
    try
    {
        codigoDespacho=despacho.insertarDespachoBasicoUnicamenteTransaccional(con, ConstantesBD.continuarTransaccion);
    }
    catch(SQLException sqle)
    {
        logger.warn("Error en el insert del despacho b�sico" );
        return ConstantesBD.codigoNuncaValido;
    }
    return codigoDespacho;
}
	
/*
private boolean insertarDescAtributosSolicitud(Connection con, int numeroSolicitud, HashMap medicamentos, UsuarioBasico user)
{
    SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
    String temporalNumAutorizacion="";
    int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
    int resultado=ConstantesBD.codigoNuncaValido;
    int codigoAtributoNumeroAutorizacion=UtilidadValidacion.getCodNumeroAutorizacionAtributosSolicitud(con, user.getCodigoInstitucionInt());
    int numReg = Utilidades.convertirAEntero(medicamentos.get("numRegistros")+"");
    
    for (int i=0; i<numReg; i ++)
    {  
     
            temporalNumAutorizacion= medicamentos.get(indicesListado[27]+i)+"";
            temporalCodigoArticulo= Integer.parseInt(medicamentos.get(indicesListado[21]+i)+"");
            try
            {
            	if(temporalNumAutorizacion!=null&&!temporalNumAutorizacion.equals(""))
            	{
	                resultado=objetoSolicitudMedicamentos.insertarUnicamenteAtributoSolicitudMedicamentos(con, numeroSolicitud,temporalCodigoArticulo,  codigoAtributoNumeroAutorizacion, temporalNumAutorizacion);
	                if(resultado<=0)
	 			       return false;
            	}
            }
            catch (Exception e)
            {
                logger.warn("Error insertando la desc_atribuots_solicitud en la solicitud= "+ numeroSolicitud);
                return false;
            }
       
    }    
    return true;
}
*/



private boolean insertarDetalleDespachoTransaccional(	Connection con, 
		HashMap medicamentos,
		int codigoDespacho, 
		UsuarioBasico user, 
		PersonaBasica paciente)
{
	logger.info("\n medicamentos --> "+medicamentos);
	int temporalCodigoArticulo=0, temporalCantidad;
	DespachoMedicamentos despacho= new DespachoMedicamentos();
	int resp=ConstantesBD.codigoNuncaValido;
	boolean insertoBienExcepcionFarmacia=false;
	int numReg = Utilidades.convertirAEntero(medicamentos.get("numRegistros")+"");
	    
	    for (int i=0; i<numReg; i ++)
	    {  
	     
			try
			{
			    temporalCodigoArticulo= Utilidades.convertirAEntero(medicamentos.get(indicesListado[21]+i)+"");
				temporalCantidad=  Utilidades.convertirAEntero(medicamentos.get(indicesListado[13]+i)+"");
				resp=despacho.insertarDetalleDespachoUnicamenteTransaccional(		con, ConstantesBD.continuarTransaccion, 
					temporalCodigoArticulo, 
					temporalCodigoArticulo, 
					codigoDespacho, 
					temporalCantidad, 
					"",
					"",
					"",
					"",
					"",
					"");
				if (resp<1)
				{
					logger.warn("Error en el insert del detalle de despacho con indice ="+i);
					return false;
				}
				
				/*String valorPorDefecto= ValoresPorDefecto.getGenExcepcionesFarmAut(user.getCodigoInstitucionInt());
				if(valorPorDefecto.trim().equals("true"))
				{    
					insertoBienExcepcionFarmacia= despacho.ingresarExcepcionesFarmaciaXConvenio(con,user,paciente,temporalCodigoArticulo);
					if(!insertoBienExcepcionFarmacia)
					{
						logger.warn("Error en la insercion de la expcepcion de farmacia con indice="+i);
						return false;
					}
				}*/	
			}
			catch(NumberFormatException e)
			{
				logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
				return false;
			}
			catch(SQLException sqle)
			{
				logger.warn("Error en el insert del detalle de despocho con indice ="+i +"   error-->"+sqle);
				return false;
			}
		
	}  
	return true; 
}



private boolean cambiarEstadoMedicoSolicitudTransaccional(Connection con, int numeroSolicitud/*, String numeroAutorizacion*/)
{
    int i=0;
    int inserto= ConstantesBD.codigoNuncaValido;
    DespachoMedicamentos despacho= new DespachoMedicamentos();
    despacho.setNumeroSolicitud(numeroSolicitud);
    try
    {
       inserto =despacho.cambiarEstadoMedicoSolicitudTransaccional(con, ConstantesBD.continuarTransaccion, ConstantesBD.codigoEstadoHCCargoDirecto/*, numeroAutorizacion*/); 
    }
    catch(SQLException sqle)
    {
        logger.warn("Error en el insert del cambiar esatdo de la solicitud transaccional con indice ="+i +"   error-->"+sqle);
        return false;
    }
    if (inserto<1)
    {
			return false;
	}
	return true;
}



private boolean generarInfoSubCuentaCargoMedicamentos(Connection con, UsuarioBasico usuario, PersonaBasica paciente, int numeroSolicitud, HashMap medicamentos, String codigoCuenta) throws IPSException
{
    boolean generoCargo=false;
    int codigoArticulo=ConstantesBD.codigoNuncaValido;
    int cantidadArticulo=ConstantesBD.codigoNuncaValido;
    float valorTarifa=ConstantesBD.codigoNuncaValido;
    int centroCostoSolicita=ConstantesBD.codigoNuncaValido;
    
    int numReg = Utilidades.convertirAEntero(medicamentos.get("numRegistros")+"");
    logger.info("\n medicamentos -->"+medicamentos);
    for (int i=0; i<numReg; i ++)
    {  
     
            try
            {
            	 codigoArticulo= Utilidades.convertirAEntero(medicamentos.get(indicesListado[21]+i)+"");
            	 cantidadArticulo=  Utilidades.convertirAEntero(medicamentos.get(indicesListado[13]+i)+"");
            	 centroCostoSolicita = Utilidades.convertirAEntero(medicamentos.get(indicesListado[33]+i)+"");
                //generoCargoExitoso=despacho.generarCargoDespachoMedicamentosDadoValorTarifa(con,usuario, paciente, numeroSolicitud, codigoArticulo, valorTarifa);
        	    
        	    Cargos cargoArticulos= new Cargos();
        	    generoCargo=	cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(		con, 
					        	    																			usuario, 
					        	    																			paciente, 
					        	    																			numeroSolicitud, 
					        	    																			codigoArticulo, 
					        	    																			cantidadArticulo, 
					        	    																			false/*dejarPendiente*/, 
					        	    																			ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos/*codigoTipoSolicitudOPCIONAL*/, 
					        	    																			Utilidades.convertirAEntero(codigoCuenta)/*codigoCuentaOPCIONAL*/, 
					        	    																			centroCostoSolicita, 
					        	    																			valorTarifa /*valorTarifaOPCIONAL*/,
					        	    																			false,"", false /*tarifaNoModificada*/);
        	    if(!generoCargo)
        		{
        			logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud +" y articulo ->"+ codigoArticulo+" y valorUnitario-> "+valorTarifa);
        			return false;
        		}
        	    
        	}
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                return false;
            }
        
    }    
    return true;
}

private boolean validarInsertarJustificacion(Connection con, UsuarioBasico user, int numeroSolicitud, int cantidad, HashMap medicamentos) throws IPSException
{
	
	int codigoArticulo=ConstantesBD.codigoNuncaValido;
	//cargosForm.setJustificacionNoPosMap("numRegistros", 0);
	  int numReg = Utilidades.convertirAEntero(medicamentos.get("numRegistros")+"");
	    
	    for (int i=0; i<numReg; i ++)
	    {      
        
            try
            {
            	 codigoArticulo= Integer.parseInt(medicamentos.get(indicesListado[21]+i)+"");
                /* Se coloca el codigo del convenio en (-1); ya que se ve que nunca es llamado este m�todo
                 * se coloca para no generar error; este cambio aplico por la Tarea 59745 */
            	if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoArticulo, true, false, ConstantesBD.codigoNuncaValido))
        		{
            		double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, codigoArticulo, numeroSolicitud, ConstantesBD.codigoNuncaValido, true);
        			if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoArticulo, cantidad, user.getLoginUsuario(), true, false, Utilidades.convertirAEntero(subcuenta+""),""))
        			{
        				//cargosForm.setJustificacionNoPosMap("mensaje_"+i, "ARTICULO [ "+codigoArticulo+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACION NO POS.");
        			}
        		}
            }
            catch(NumberFormatException e)
            {
                logger.warn("Error no se pudo validar e insertar justificacion pendiente del codigo del articulo "+codigoArticulo+" con indice ="+i +"   error-->"+e);
                return false;
            }
        
    }
	///cargosForm.setJustificacionNoPosMap("numRegistros", cargosForm.getNumeroFilasMapa());
	return true;
}
	private boolean insertarInfoCargosDirectosTransaccional(
			Connection con, 
			int numeroSolicitud, 
			String loginUsuario, 
			int tipoRecargo, 
			int codigoServicioSolicitado, String fechaEjecucion) throws SQLException
	{
		CargosDirectos cargo= new CargosDirectos();
		// Cambios segun Anexo 809 
		cargo.llenarMundoCargoDirecto(numeroSolicitud,loginUsuario,tipoRecargo,codigoServicioSolicitado,"",false,fechaEjecucion);	
	    int resultado=cargo.insertar(con);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}

	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// flujo de cargos directos para servicios
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	private boolean flujoInsertarServicios		(	Connection con, 
			HttpServletRequest request,
			ActionMapping mapping, 
			HashMap servicios,
			int codigoCuenta,
			PersonaBasica paciente,
			UsuarioBasico user,
			PersonaBasica pacienteOri,
			String solicitudOri) throws SQLException
{
		logger.info("\n entre a flujoInsertarServicios paciente a trasladar -->"+paciente.getNumeroIdentificacionPersona()+" paciente original -->"+pacienteOri.getNumeroIdentificacionPersona());
	Solicitud objetoSolicitud = new Solicitud();
	HashMap<String, Object> espSolicitada = new HashMap<String, Object>(); 
	int numeroSolicitud=ConstantesBD.codigoNuncaValido;
	boolean inserto=true;    
	int codigoTraslado=ConstantesBD.codigoNuncaValido;
	int temporalCodigoServicio=ConstantesBD.codigoNuncaValido;
	int temporalCantidadServicio=ConstantesBD.codigoNuncaValido;
	//String temporalNumeroAutorizacion="";
	int temporalCodigoCentroCostoEjecuta=ConstantesBD.codigoNuncaValido;
	int temporalCodigoMedicoResponde=ConstantesBD.codigoNuncaValido;
	int temporalCodigoTipoRecargo= ConstantesBD.codigoNuncaValido;
	int temporalCodigoPool= ConstantesBD.codigoNuncaValido;
	int temporalEspecilidadSolicitada= ConstantesBD.codigoNuncaValido;
	int numReg = Utilidades.convertirAEntero(servicios.get("numRegistros")+"");
	for (int i=0; i<numReg; i ++)
	{    
	
		temporalCodigoServicio= Utilidades.convertirAEntero(servicios.get(indicesListado[21]+i)+"");
		temporalCantidadServicio= Utilidades.convertirAEntero(servicios.get(indicesListado[13]+i)+"");
		temporalCodigoCentroCostoEjecuta= Utilidades.convertirAEntero(servicios.get(indicesListado[26]+i)+"");
		temporalCodigoMedicoResponde= Utilidades.convertirAEntero(servicios.get(indicesListado[35]+i)+"");
		//temporalCodigoTipoRecargo= Integer.parseInt(cargosForm.getServiciosArticulosMap("codigoRecargo_"+i)+"");// preguntar a armando
		temporalCodigoPool= Utilidades.convertirAEntero(servicios.get(indicesListado[29]+i)+"");
		
		// Cambios Segun Anexo 809
		logger.info("numero de solicitud de origen >>>>>>"+solicitudOri);
		espSolicitada = objetoSolicitud.obtenerEspecilidadSolicitada(con, solicitudOri);
		temporalEspecilidadSolicitada = Utilidades.convertirAEntero(espSolicitada.get("especilidadSolicitada")+"");
		logger.info("valor especilidad solicitada despues consulta >>>>"+temporalEspecilidadSolicitada);
		// Fin Cambios Segun Anexo 809
		
		//PRIMERO SE HACE UN INSERT DE UNA SOLICITUD GENERAL
		try
		{ 
			numeroSolicitud= this.insertarSolicitudBasicaTransaccional(con, codigoCuenta, paciente, user, ConstantesBD.codigoTipoSolicitudCargosDirectosServicios, temporalCodigoCentroCostoEjecuta,temporalEspecilidadSolicitada);
	
			logger.info("\n el numero de solicitud ingresado es --> "+numeroSolicitud);
			if(numeroSolicitud<=0)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				inserto=false;
				//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud b�sica (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
			}
			else
				inserto=true;
		}
		catch(SQLException sqle)
		{
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			inserto=false;
			//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud b�sica (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
		}
		logger.info("\n temporal codigo pool -->"+temporalCodigoPool);
		if(temporalCodigoPool>=0)
		{    
			if (inserto)
				inserto = this.actualizarPoolXSolicitud(con, numeroSolicitud, temporalCodigoPool);
			if(!inserto)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				inserto=false;
				//	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar el pool x medico (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
			}
		}    
		
		logger.info("\n se porocede a actualizar el medico responde ");
		// se hace una actulizacion del medico que responde
		try
		{
			if (inserto && temporalCodigoMedicoResponde>0)
			inserto = this.actaulizarMedicoRespondeTransaccional(con, numeroSolicitud, temporalCodigoMedicoResponde);
			if(!inserto)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				inserto=false;
				//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar medico responde (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
			}
		}
		catch(SQLException sqle)
		{
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			inserto=false;
			//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar medico responde (Traslado Solicitudes por Transplantes)", "error.cargosDirectos.medicamentos", true);
		}
		logger.info("\n SE INSERTA EL USUARIO QUE HIZO EL CARGO DIR Y EL TIPO DE RECARGO ");
		//SE INSERTA EL USUARIO QUE HIZO EL CARGO DIR Y EL TIPO DE RECARGO
		if (inserto)
			inserto = this.insertarInfoCargosDirectosTransaccional(con, numeroSolicitud, user.getLoginUsuario(), temporalCodigoTipoRecargo, temporalCodigoServicio,"");
	    if(!inserto)
	    {
	    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	    	inserto=false;
	    	//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el insertar cargos dir (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    }
	    
	    if (inserto)
	    	inserto=this.cambiarEstadoMedicoSolicitudTransaccional(con, numeroSolicitud/*, temporalNumeroAutorizacion*/);
	    if(!inserto)
	    {
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			inserto=false;
			//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al cambiar el estado medico de la solicitud (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
	    }
	    
	    
	    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
	    Cargos cargos= new Cargos();
	    if (inserto)
	    	inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
	    																			user,
	    																			codigoCuenta,
	    																			paciente.getCodigoIngreso(),
	    																			false/*dejarPendiente*/, 
	    																			numeroSolicitud, 
	    																			ConstantesBD.codigoTipoSolicitudCargosDirectosServicios /*codigoTipoSolicitudOPCIONAL*/, 
	    																			ConstantesBD.codigoNuncaValido/*codigoCentroCostoEjecutaOPCIONAL*/, 
	    																			temporalCodigoServicio/*codigoServicioOPCIONAL*/, 
	    																			temporalCantidadServicio/*cantidadServicioOPCIONAL*/, 
	    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
	    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
	    																			/*"" --numeroAutorizacionOPCIONAL,*/
	    																			"" /*esPortatil*/,"");
	}  
	
	if (inserto)
		inserto=actualizarEstadoFacturacion(con, servicios.get(indicesListado[25]+"0")+"", ConstantesBD.codigoEstadoFacturacionAnulada+"",servicios.get(indicesListado[36]+"0")+"" );
	

	if (inserto)
	{
		codigoTraslado=existeTraslado(con, pacienteOri.getCodigoIngreso()+"", paciente.getCodigoIngreso()+"", user.getCodigoInstitucion());
		if (codigoTraslado<0)
			codigoTraslado=insertarEncabTraslado(con, pacienteOri.getCodigoIngreso()+"", paciente.getCodigoIngreso()+"", user.getCodigoInstitucion());
			if (codigoTraslado<0)
				inserto=false;
			else
				inserto=insertarDetTraslado(con, codigoTraslado+"", solicitudOri, numeroSolicitud+"", user.getLoginUsuario());
	}
	
	if (inserto)
		this.terminarTransaccion(con);
	else
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	return inserto;
	}

	/**
	 * M�todo que actualiza el pool x solciitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoPool
	 * @return
	 * @throws SQLException
	 */
	private boolean actualizarPoolXSolicitud(Connection con, int numeroSolicitud, int codigoPool) throws SQLException
	{
	    Solicitud solciitud= new Solicitud();
	    int resultado= solciitud.actualizarPoolSolicitud(con, numeroSolicitud, codigoPool);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
	
	
	
	
   /**
	 * metodo que actualiza el medico que responde la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoMedico
	 * @return
	 * @throws SQLException
	 */
	private boolean actaulizarMedicoRespondeTransaccional(Connection con, int numeroSolicitud, int codigoMedico) throws SQLException
	{
		logger.info("\n entre actaulizarMedicoRespondeTransaccional codigomedico -->"+codigoMedico+" numero solicitud -->"+numeroSolicitud );
		
	    Solicitud objetoSol= new Solicitud();
	    UsuarioBasico medico= new UsuarioBasico();
	    medico.cargarUsuarioBasico(con, codigoMedico);
	    int resultado = objetoSol.actualizarMedicoRespondeTransaccional(con, numeroSolicitud, medico, ConstantesBD.continuarTransaccion);
	    
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
	

}