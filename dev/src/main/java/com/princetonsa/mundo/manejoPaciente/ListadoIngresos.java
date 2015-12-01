package com.princetonsa.mundo.manejoPaciente;

/**
 *@author Jhony Alexander Duque A. 
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.InfoDatosString;
import util.Listado;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ListadoIngresosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ListadoIngresosDao;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoEnvioAutorizacion;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
 

public class ListadoIngresos {

	//indices para criterios de busqueda
	public static final String [] indicesCriterios={"centroAtencion","convenio","fechaIngresoInicial",
		"fechaIngresoFinal","fechaSolicitudInicial","fechaSolicitudFinal","estadoSolicitud","estadoIngreso",
		"piso","viaIngreso","estadoCuentaActiva","estadoCuentaAsociada","estadoCuentaActiva",
		"estadoCuentaFacturadaParcial","estadoCuentaFacturada","indicativoOrden","tipoSalida"};
	
	/*
	 * Indices resultados
	 */
	public static final String [] indicesResultados={"centroAtencion","convenio","paciente","tipoNoId",
		"ingreso","viaIngreso","fechaIngreso","estadoIngreso","nroCuenta","indicativo"};
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(ListadoIngresos.class);
	
	
	public static ListadoIngresosDao lisIngresosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao();
	}
	
	
	
	
	//indices listado de ingresos
	public  static String indicesListado [] = {"idIngreso0_","consecutivoIngreso1_","centroAtencion2_","nombreCentroAtencion3_","estadoIngreso4_",
		"fechaAperturaIngreso5_","viaIngreso6_","nombreViaIngreso7_","fechaCierreIngreso8_","numeroCuenta9_",
		"estadoCuenta10_","nombreEstadoCuenta11_"};
	
	public static String indicesEncabezadoDetalle [] ={"ingreso0","consecutivoIngreso1","codigoCentroAtencion2","nombreCentroAtencion3",
													   "fechaHoraIngreso4","numeroAutorizacionAdmision5","idCuenta6","viaIngreso7"};
	
	public static String indicesServiciosArticulos [] = {"solicitud0_","consecutivoOrdenes1_","tipoSolicitud2_","nombreTipoSolicitud3_","estadoHistoriaClinica4_",
														 "nombreEstadoHistClinica5_","centroCostoSolicita6_","nombreCentroCostoSolicita7_",
														 "centroCostoEjecuta8_","nombreCentroCostoEjecuta9_","porcentajeAutorizado10_",
														 "montoAutorizado11_","valorUnitario12_","valorTotal13_","estadoCargo14_","nombreEstadoCargo15_",
														 "fechaSolicitud16_","servArticulo17_","cantidad18_","requiereAutorizacion19_","numeroAutorizacion20_",
														 "codigoDetalleCargo21_","check22_","checkTot23","nombreTipoAsocio24_","servicioCx25_","esMaterialEspecial26_",
														 "codAutorizacion27_","codDetAutorizacion28_","estadoAutorizacion29_","tipoTramite30_","urgente31_","esVigente32_",
														 "fechaVigencia33_","idSubCuenta34_","codigoConvenio35_","fechaAutorizacion36_","horaAutorizacion37_","medicoSol38_",
														 "nombreMedicoSol39_","indMostrarCheck40_","tipoServicio41_"};
	
	
	public static String indicesBusquedaAvanzada [] = {"numeroSolicitud0","servicio1","articulo2","fechaSolicitud3","centroCostoSolicita4",
													   "centroCostoEjecuta5","numeroAutorizacion6","descServ7","descArt8"};
	
	public  static String indicesXRangos [] = {
		"codigo_",
		"paciente_",
		"tiponoid_",
		"ingreso_",
		"viaingreso_",
		"fechaingreso_",
		"estadoingreso_",
		"nrocuenta_"};
	
	
	
	/**
	 * Metodo encargado de consultar el listado de ingresos (cerrados, abiertos)
	 * y cuentas (no importa el estado) de un paciente. 
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- paciente --> Requerido
	 * -- institucion --> Requerido
	 * @return Mapa
	 * -----------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * -----------------------------
	 * idIngreso0_,consecutivoIngreso1_,centroAtencion2_,
	 * nombreCentroAtencion3_,estadoIngreso4_,
	 * fechaAperturaIngreso5_,viaIngreso6_,nombreViaIngreso7_,
	 * fechaCierreIngreso8_,numeroCuenta9_,estadoCuenta10_,
	 * nombreEstadoCuenta11_
	 */
	private static HashMap cargarListadoIngresos (Connection connection, HashMap criterios)
	{
		return lisIngresosDao().cargarListadoIngresos(connection, criterios);
	}
	
	
	
	/**
	 * Metodo encargado de consultar los convenios de un ingreso en 
	 * la tabla sub_cuentas
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * --ingreso --> Requerido
	 * @return ArrayList<HashMap>
	 * ----------------------------------------------
	 * KEY'S DE LOS MAPAS DEL ARRAYLIST DE RESULTADO
	 * ----------------------------------------------
	 * -- codigo
	 * -- nombre
	 * -- contrato
	 */
	public static  ArrayList<HashMap<String, Object>> ObtenerConvenios (Connection connection,HashMap criterios)
	{
		return lisIngresosDao().ObtenerConvenios(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar los convenios de un ingreso en 
	 * la tabla sub_cuentas
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param ingreso
	 * @return ArrayList<HashMap>
	 * ----------------------------------------------
	 * KEY'S DE LOS MAPAS DEL ARRAYLIST DE RESULTADO
	 * ----------------------------------------------
	 * -- codigo
	 * -- nombre
	 * -- contrato
	 */
	public static ArrayList<HashMap<String, Object>> ObtenerConvenios (Connection connection,String ingreso)
	{
		HashMap criterios = new HashMap ();
		criterios.put("ingreso", ingreso);
		
		return ObtenerConvenios(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de servicios
	 * o articulos con el numero de autorizacion.
	 * @author Jhony Alexander Duque A.
	 * @param con 
	 * @param criterios
	 * ---------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------------
	 * -- subCuenta --> Requerido
	 * -- solicitud --> Opcional
	 * -- articulo --> Opcional
	 * -- servicio --> Opcional
	 * -- fechaSolicitud --> Opcional
	 * -- centroCostoSolicita --> Opcional
	 * -- centroCostoEjecuta --> Opcional
	 * --numeroAutorizacion --> Opcional
	 * @return Mapa
	 * --------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------------
	 * solicitud0_,consecutivoOrdenes1_,tipoSolicitud2_,
	 * nombreTipoSolicitud3_,estadoHistoriaClinica4_,
	 * nombreEstadoHistoriaClinica5_,centroCostoSolicita6_,
	 * nombreCentroCostoSolicita7_,centroCostoEjecuta8_,
	 * nombreCentroCostoEjecuta9_,porcentajeAutorizado10_,
	 * montoAutorizado11_,valorUnitario12_,valorTotal13_,
	 * estadoCargo14_,nombreEstadoCargo15_,fechaSolicitud16_,
	 * servArticulo17_,cantidad18_,requiereAutorizacion19_,
	 * numeroAutorizacion20_
	 */
	public static HashMap cargarServiciosArticulos (Connection con, HashMap criterios)
	{
		return lisIngresosDao().cargarServiciosArticulos(con, criterios);
	}
	
	
	/**
	 * Metodo encargado de actualizar el numero de autorizacion 
	 * de un servicio o articulo en la tabla det_cargos
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- numeroAutorizacion --> Requerido
	 * -- codigoDetalleCargo --> Requerido
	 * @return true/false
	 */
	public static  boolean actualizarAutorizacionServicioArticulo (Connection connection, HashMap datos)
	{
		return lisIngresosDao().actualizarAutorizacionServicioArticulo(connection, datos);
	}
	
	/**
	 * Metodo encargado de actualizar el numero de autorizacion 
	 * de la admision
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- numeroAutorizacion --> Requerido
	 * -- subCuenta --> Requerido
	 * @return true/false
	 */
	public static boolean actualizarAutorizacionAdmision (Connection connection, HashMap datos)
	{
		return lisIngresosDao().actualizarAutorizacionAdmision(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de organizar los datos para realizar la busqueda.
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static HashMap cargarServiciosArticulos (Connection connection, ListadoIngresosForm forma)
	{
		logger.info("\n entre a cargarServiciosArticulos convenio -->"+forma.getConvenio()+" busqueda avanzada --> "+forma.getBusquedaAvanzada()+"  ESTADO AUTORIZACION -->"+forma.getEstadoAutorizacion());
		
		HashMap criterios = new HashMap ();
		//subcuenta
		criterios.put("subCuenta", obtenerSubCuenta(forma.getConvenio()));
		
		//indicativo orden
		criterios.put("estadoAutorizacion", forma.getEstadoAutorizacion());
		//ordenes ambulatorias
		criterios.put("ordenesAmbulatorias", forma.isOrdenesAmbulatorias());
		//se toma el id del ingreso
		criterios.put("idIngreso", forma.getEncabezadoDetalle(indicesEncabezadoDetalle[0]));
		
		//solicitud
		if (UtilidadCadena.noEsVacio(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[0])+""))
			criterios.put("solicitud", forma.getBusquedaAvanzada(indicesBusquedaAvanzada[0]));
		//articulo
		if (UtilidadCadena.noEsVacio(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[2])+""))
			criterios.put("articulo", forma.getBusquedaAvanzada(indicesBusquedaAvanzada[2]));
		//servicio
		if (UtilidadCadena.noEsVacio(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[1])+""))
			criterios.put("servicio", forma.getBusquedaAvanzada(indicesBusquedaAvanzada[1]));
		//fechaSolicitud
		if (UtilidadCadena.noEsVacio(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[3])+""))
			criterios.put("fechaSolicitud", forma.getBusquedaAvanzada(indicesBusquedaAvanzada[3]));
		//centroCostoSolicita
		if (UtilidadCadena.noEsVacio(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[4])+"") && !(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[4])+"").equals(ConstantesBD.codigoNuncaValido+""))
			criterios.put("centroCostoSolicita", forma.getBusquedaAvanzada(indicesBusquedaAvanzada[4]));
		//centroCostoEjecuta
		if (UtilidadCadena.noEsVacio(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[5])+"") && !(forma.getBusquedaAvanzada(indicesBusquedaAvanzada[5])+"").equals(ConstantesBD.codigoNuncaValido+""))
			criterios.put("centroCostoEjecuta", forma.getBusquedaAvanzada(indicesBusquedaAvanzada[5]));
		
		logger.info("valor de la sql >> "+criterios);		
		
		HashMap preresultados = cargarServiciosArticulos(connection, criterios);	
		return validarAutorizaciones(preresultados);
		 
	}
	
	/**
	 * metodo encargado se separa la sub_cuenta
	 * de la varibale convenio.
	 * @param convenio convenio@@@@@autorizacion@@@@@subCuenta
	 * @return
	 */
	public static String obtenerSubCuenta(String convenio)
	{
		String temp [] ={};
		try 
		{
			temp=convenio.split(ConstantesBD.separadorSplit);
		}
		catch (Exception e) 
		{
			logger.info("\n problema obteniendo la subcuenta "+e);
			
		}
		
		if (temp.length >= 3 && 
				Utilidades.convertirAEntero(temp[2])>0)
			return temp[2];
		else
			return ConstantesBD.codigoNuncaValido+""; 
		
	}
	
	/**
	 * metodo encargado de separar el convenio
	 * de la varibale convenio.
	 * @param convenio convenio@@@@@autorizacion@@@@@subCuenta
	 * @return
	 */
	public static String obtenerConvenio(String convenio)
	{
		String temp [] ={};
		try 
		{
			temp=convenio.split(ConstantesBD.separadorSplit);
		}
		catch (Exception e) 
		{
			logger.info("\n problema obteniendo el convenio "+e);
			
		}
			return temp[0];
	}
	
	
	
	/**
	 * metodo encargado se separa la autorizacion
	 * de la varibale convenio.
	 * @param convenio convenio@@@@@autorizacion@@@@@subCuenta
	 * @return
	 */
	public static String obtenerAutorizacion(String convenio)
	{
		String temp [] ={};
		try 
		{
			temp=convenio.split(ConstantesBD.separadorSplit);
		}
		catch (Exception e) 
		{
			logger.info("\n problema obteniendo la subcuenta "+e);
			
		}
			return temp[1];
	}
	
	
	/**
	 * Metodo encargado de consultar el listado de ingresos (cerrados, abiertos)
	 * y cuentas (no importa el estado) de un paciente. 
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param paciente
	 * @param institucion
	 * @return Mapa
	 */
	public static HashMap cargarListadoIngresos (Connection connection, int paciente,int institucion )
	{
		HashMap criterios = new HashMap ();
		//codigo del paciente
		criterios.put("paciente", paciente);
		//codigo institucion
		criterios.put("institucion", institucion);
		
		return cargarListadoIngresos(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de ordenar un mapa
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public static HashMap accionOrdenarMapa(HashMap mapaOrdenar,ListadoIngresosForm forma , String [] indices)
	{			
		logger.info("===> Entré a accionOrdenarMapa");
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");	
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		logger.info("===> Voy a salir de accionOrdenarMapa");
		return mapaOrdenar;
	}	
	
	/**
	 * Metodo encargado de cargar el encabezado del detalle
	 * asi como unos select para la busqueda avanzada.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarDetalle (Connection connection, ListadoIngresosForm forma,UsuarioBasico usuario)
	{
		//se formate al pager
		forma.resetPager();
		
		//se formatean los valores del detalle
		forma.resetDetalle();
		
		//se carga el encabezado
		forma.setEncabezadoDetalle(cargarEncabezado(forma.getListadoIngresos(), forma.getIndex()));
		
		//forma.setEncabezadoDetalleOld((HashMap)forma.getEncabezadoDetalle().clone());
		
		//se cargan los convenios
		forma.setConvenios(ObtenerConvenios(connection, forma.getListadoIngresos(indicesListado[0]+forma.getIndex())+""));
		
		if (forma.getConvenios().size()==1)
		{
			HashMap tmp = (HashMap)forma.getConvenios().get(0);
			forma.setConvenio(tmp.get("codigo")+ConstantesBD.separadorSplit+tmp.get("autorizacion")+ConstantesBD.separadorSplit+tmp.get("subCuenta")); 
			forma.resetBusqueda();
			forma.setCuerpoDetalle(cargarServiciosArticulos(connection, forma));									
			forma.setEncabezadoDetalle(indicesEncabezadoDetalle[5], obtenerAutorizacion(forma.getConvenio()));
		}
		
		//se obtiene los centros de costo con tipo de area directo
		forma.setCentCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false, 0));
	
	}
	
	/**
	 * Validaciones de las Autorizaciones
	 * @param HashMap mapaOriginal
	 * */
	public static HashMap validarAutorizaciones(HashMap mapaOriginal)
	{
		int numRegistros = Utilidades.convertirAEntero(mapaOriginal.get("numRegistros").toString());
		boolean esOrdenActiva = false;
				
		for(int i=0; i<numRegistros; i++)
		{				
			esOrdenActiva = Autorizaciones.esOrdenActiva(
					Utilidades.convertirAEntero(mapaOriginal.get(indicesServiciosArticulos[21]+i).toString())>0?Autorizaciones.codInternoAutoSolicSerMed:Autorizaciones.codInternoAutoSolicAmbula,
					mapaOriginal.get(indicesServiciosArticulos[2]+i).toString(), 
					mapaOriginal.get(indicesServiciosArticulos[41]+i).toString(),
					mapaOriginal.get(indicesServiciosArticulos[4]+i).toString());
			
			mapaOriginal.put(indicesServiciosArticulos[40]+i,ConstantesBD.acronimoNo);

			if(mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals("") ||
					mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoNegado) || 
							mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)	|| 
							mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
				mapaOriginal.put(indicesServiciosArticulos[40]+i,ConstantesBD.acronimoSi);
			else if(mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{				
				mapaOriginal.put(indicesServiciosArticulos[40]+i,ConstantesBD.acronimoNo);
			
				if(esOrdenActiva && 
						!Autorizaciones.tieneActivaVigencia(
								mapaOriginal.get("vigencia_"+i).toString(),
								mapaOriginal.get("tipoVigencia_"+i).toString(),
								mapaOriginal.get("fechaFinAutoriza_"+i).toString(),
								mapaOriginal.get(indicesServiciosArticulos[36]+i).toString(),
								mapaOriginal.get(indicesServiciosArticulos[37]+i).toString()))
					mapaOriginal.put(indicesServiciosArticulos[40]+i,ConstantesBD.acronimoSi);																
			}
			
			if(mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoNegado) || 
				mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				InfoDatosString info = Autorizaciones.calcularIncrementoFechaVigencia(
						mapaOriginal.get("vigencia_"+i).toString(),
						mapaOriginal.get("tipoVigencia_"+i).toString(), 
						mapaOriginal.get(indicesServiciosArticulos[36]+i).toString(),
						mapaOriginal.get(indicesServiciosArticulos[37]+i).toString());
				
				mapaOriginal.put(indicesServiciosArticulos[36]+i,info.getCodigo());
				mapaOriginal.put(indicesServiciosArticulos[37]+i,info.getValue());			
			}
			else
			{
				mapaOriginal.put(indicesServiciosArticulos[36]+i,"");
				mapaOriginal.put(indicesServiciosArticulos[37]+i,"");
			}
		}

		return mapaOriginal;		
	}
	
	/**
	 * Metodo encargado de copiar los datos del ingreso
	 * del mapa listado a el mapa encabezado detalle.
	 * @param Datos
	 * @param index
	 * @return
	 */
	public static HashMap cargarEncabezado (HashMap datos,String index)
	{
		HashMap encabezado = new HashMap();
		//id ingreso
		encabezado.put(indicesEncabezadoDetalle[0], datos.get(indicesListado[0]+index));
		//consecutivo ingreso
		encabezado.put(indicesEncabezadoDetalle[1], datos.get(indicesListado[1]+index));
		//codigo centro atencion
		encabezado.put(indicesEncabezadoDetalle[2], datos.get(indicesListado[2]+index));
		//nombre centro atencion
		encabezado.put(indicesEncabezadoDetalle[3], datos.get(indicesListado[3]+index));
		//fecha hora ingreso
		encabezado.put(indicesEncabezadoDetalle[4], datos.get(indicesListado[5]+index));
		//id cuenta
		encabezado.put(indicesEncabezadoDetalle[6], datos.get(indicesListado[9]+index));
		//via ingreso
		encabezado.put(indicesEncabezadoDetalle[7], datos.get(indicesListado[6]+index));
		
		return encabezado;
	}
	
	
	
	public static void guardar (Connection connection, ListadoIngresosForm forma,UsuarioBasico usuario)
	{
		logger.info("\n entre a guardar ");
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		
		//se evalua si cambio la autorizacion de admision
		if (transacction && !obtenerAutorizacion(forma.getConvenio()).equals(forma.getEncabezadoDetalle(indicesEncabezadoDetalle[5])+"") && 
				!obtenerAutorizacion(forma.getConvenio()).equals(forma.getEncabezadoDetalle(indicesEncabezadoDetalle[5])+""))
		{
			logger.info("\n entre a modificar autorizacion de la admision ");
			HashMap datos = new HashMap ();
			//codigo sub_cuenta
			datos.put("subCuenta", obtenerSubCuenta(forma.getConvenio()));
			// numero autorizacion admision
			datos.put("numeroAutorizacion", forma.getEncabezadoDetalle(indicesEncabezadoDetalle[5]));
			
			datos.put("Solicitud", "");
			//servicioArticulo
			
			datos.put("servicioArticulo", "");	

			//autorizacionAnt
				datos.put("autorizacionAnt", obtenerAutorizacion(forma.getConvenio()));	
			//autorizacion
				datos.put("autorizacion", forma.getEncabezadoDetalle(indicesEncabezadoDetalle[5]));
				
			//institucion
				datos.put("institucion", usuario.getCodigoInstitucion());
			//usuarioModifica
				datos.put("usuarioModifica", usuario.getLoginUsuario());
			//asocio
				datos.put("asocio", "");
			
			transacction = actualizarAutorizacionAdmision(connection, datos);
		}
		
		int numReg = Utilidades.convertirAEntero(forma.getCuerpoDetalle("numRegistros")+"");
		//se recoren todos los servicios y/o articulos para identificar 
		//cuales se les modifico el numero de autorizacion
		for (int i=0; i<numReg;i++)
		{
			if (transacction && (forma.getCuerpoDetalle(indicesServiciosArticulos[22]+i)+"").equals(ConstantesBD.acronimoSi) &&
			   !(forma.getCuerpoDetalleOld(indicesServiciosArticulos[20]+i)+"").equals(forma.getCuerpoDetalle(indicesServiciosArticulos[20]+i)+""))
			{
				HashMap datos = new HashMap ();
				//codigo sub_cuenta
				datos.put("codigoDetalleCargo", forma.getCuerpoDetalle(indicesServiciosArticulos[21]+i));
				// numero autorizacion servicio /Articulo
				datos.put("numeroAutorizacion", forma.getCuerpoDetalle(indicesServiciosArticulos[20]+i));
				//Solicitud
				datos.put("Solicitud", forma.getCuerpoDetalle(indicesServiciosArticulos[1]+i));
				//servicioArticulo
				
				if ((forma.getCuerpoDetalle(indicesServiciosArticulos[2]+i)+"").equals(ConstantesBD.codigoTipoSolicitudCirugia+"") && 
					(forma.getCuerpoDetalle(indicesServiciosArticulos[26]+i)+"").equals(ConstantesBD.acronimoNo))
						datos.put("servicioArticulo", forma.getCuerpoDetalle(indicesServiciosArticulos[25]+i));
					else
						datos.put("servicioArticulo", forma.getCuerpoDetalle(indicesServiciosArticulos[17]+i));	

				//autorizacionAnt
					datos.put("autorizacionAnt", forma.getCuerpoDetalleOld(indicesServiciosArticulos[20]+i));	
				//autorizacion
					datos.put("autorizacion", forma.getCuerpoDetalle(indicesServiciosArticulos[20]+i));
					//autorizacion
					datos.put("subCuenta", "");
				//institucion
					datos.put("institucion", usuario.getCodigoInstitucion());
				//usuarioModifica
					datos.put("usuarioModifica", usuario.getLoginUsuario());
				//asocio
					datos.put("asocio", forma.getCuerpoDetalleOld(indicesServiciosArticulos[24]+i));
					
				transacction = actualizarAutorizacionServicioArticulo(connection, datos);
			}
		}
		
		forma.setOperacionTrue(transacction);
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
			String convenio=obtenerConvenio(forma.getConvenio());
			String autoriza=forma.getEncabezadoDetalle(indicesEncabezadoDetalle[5])+"";
			String subCuenta=obtenerSubCuenta(forma.getConvenio());
			forma.resetBusqueda();
			cargarDetalle(connection, forma, usuario);
			forma.setConvenio(convenio+ConstantesBD.separadorSplit+autoriza+ConstantesBD.separadorSplit+subCuenta);
			forma.setEncabezadoDetalle(indicesEncabezadoDetalle[5], obtenerAutorizacion(forma.getConvenio()));
			forma.setCuerpoDetalle(cargarServiciosArticulos(connection, forma));
			forma.setCuerpoDetalleOld((HashMap)forma.getCuerpoDetalle().clone());
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
	}
	
	/**
	 * Método encargado de cargar los pisos del centro de atencion
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void cargarPisosXCentroAtencion(Connection con,ListadoIngresosForm forma, UsuarioBasico usuario){
		String centroDeAtencion = "";
		HashMap pisos = new HashMap();
		/*
		 * Aquí se cargan los pisos por institucion
		 */
		centroDeAtencion=forma.getCriterios("centroAtencion")+"";
		logger.info("===> criterios: "+forma.getCriterios());
		logger.info("===> el centro de atención es: "+centroDeAtencion);
		logger.info("===> Aquí vamos a cargar los pisos por institución");
		pisos=obtenerPisosXInstitucion(con, usuario.getCodigoInstitucionInt(), centroDeAtencion);
		logger.info("===> Aquí están todos los pisos por institución"+pisos);
		forma.setPisos(pisos);
		
	}
	
	/**
	 * Método encargado de cargar los centros de atención
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void cargarCamposSeleccion (Connection con,ListadoIngresosForm forma,UsuarioBasico usuario)
	{
		HashMap viasIngreso = new HashMap();
		HashMap todosConvenios =  new HashMap();
		HashMap estadoSolicitud = new HashMap();
		HashMap pisos = new HashMap();
		String centroDeAtencion = "";
		
		logger.info("===> Voy a cargar los centros de atención");
		logger.info("===> El código del centro de atención es: "+usuario.getCodigoCentroAtencion());
		
		/*
		 * Cargar los centros de atencion
		 */
		/*
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),
				""));
		*/
		forma.setCentrosDeAtencion(obtenerCentrosDeAtencion(con, usuario.getCodigoInstitucionInt()));
		logger.info("===> Centro de Atencion: "+forma.getCentrosDeAtencion());
		
		/*
		 * Postulamos el centro de atencion del usuario en session
		 */
		InfoDatosString ca= new InfoDatosString();
		ca=Utilidades.obtenerInstitucionSirCentroAtencion(con, usuario.getCodigoCentroAtencion());
		
		if (!ca.getCodigo().equals(ConstantesBD.codigoNuncaValido+""))
			forma.setCriterios("centroAtencion",usuario.getCodigoCentroAtencion());
		else if(forma.getCentrosDeAtencion()!=null)
		{
			logger.info("===> Entré al else if centros de atencion diferente de null");
			forma.setCriterios("centroAtencion", usuario.getCodigoCentroAtencion());
		}
		
		/*
		 * Aquí se cargan las vías de ingreso
		 */
		logger.info("===> Voy a cargar las vías de ingreso");
		viasIngreso=this.consultarViaIngreso(con, usuario.getCodigoInstitucionInt());
		
		if(UtilidadCadena.noEsVacio(forma.getViaIngresoSeleccionado()+""))
		{
			viasIngreso=this.consultarViaIngreso(con, usuario.getCodigoInstitucionInt());
			logger.info("===> Entré al if cargando las vías de ingreso");
			forma.setViaIngreso(viasIngreso);
			//forma.setViaIngreso(this.consultarViaIngreso(con, usuario.getCodigoInstitucionInt()));
			//logger.info("===> El mapa viene con estos datos: "+forma.getViaIngreso());
		}
			
		else
		{
			logger.info("===> Entré al else cargando las vías de ingreso");
			forma.setViaIngreso(viasIngreso);
			//logger.info("===> El mapa viene con estos datos: "+forma.getViaIngreso());
		}
			
		/*
		 * Aquí se cargan los convenios
		 */
		//logger.info("===> Aquí vamos a cargar los convenios");
		todosConvenios=obtenerTodosLosConvenios(con);
		//logger.info("===> Aquí están todos los convenios: "+todosConvenios);
		forma.setTodosLosConvenios(todosConvenios);
		
		/*
		 * Aquí se cargan los estados solicitud
		 */
		logger.info("===> Aquí vamos a cargar los estados de solicitud");
		estadoSolicitud=obtenerEstadoSolicitud(con);
		logger.info("===> Aquí están todos los estados de solicitud"+estadoSolicitud);
		forma.setEstadoSolicitud(estadoSolicitud);
		
		/*
		 * Aquí se cargan los pisos por institucion
		 */
		centroDeAtencion=forma.getCriterios("centroAtencion")+"";
		logger.info("===> criterios: "+forma.getCriterios());
		logger.info("===> el centro de atención es: "+centroDeAtencion);
		logger.info("===> Aquí vamos a cargar los pisos por institución");
		pisos=obtenerPisosXInstitucion(con, usuario.getCodigoInstitucionInt(), centroDeAtencion);
		logger.info("===> Aquí están todos los pisos por institución"+pisos);
		forma.setPisos(pisos);
		//forma.setConvenios(ObtenerConvenios(con, forma.getListadoIngresos(indicesListado[0]+forma.getIndex())+""));
		
		/*
		else
		{
			
			if(!(forma.getCriterios("centroAtencion")+"").equals("-2"))
			{
				logger.info("===> Entré al centro de atención diferente a todos '-2'");
				//forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),
				//ConstantesBD.acronimoSi));
				forma.setCriterios("centroAtencion",ConstantesBD.codigoNuncaValido);
			}
			else
			{
				logger.info("===> Los centros de atención son TODOS "+forma.getCriterios("centroAtencion")+"");
				//forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),
				//ConstantesBD.acronimoSi));
			}
			
		}*/
	}
	
	/**
	 * Método encargado de consultar las vías de ingreso
	 * @param con
	 * @return
	 */
	public HashMap consultarViaIngreso(Connection con, int codInstitucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().consultarViaIngreso(con, codInstitucion);
	}
	
	/**
	 * Método encargado de consultar todos los convenios
	 * @param con
	 * @return
	 */
	public static  HashMap obtenerTodosLosConvenios (Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().obtenerTodosLosConvenios(con);
	}
	
	/**
	 * Método encargado de consultar todos los estados solicitud
	 * @param con
	 * @return
	 */
	public static  HashMap obtenerEstadoSolicitud (Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().obtenerEstadoSolicitud(con);
	}
	
	/**
	 * Método encargado de consultar todos los estados solicitud
	 * @param con
	 * @return
	 */
	public static  HashMap obtenerPisosXInstitucion (Connection con, int codInstitucion, String centroDeAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().obtenerPisosXInstitucion(con, codInstitucion, centroDeAtencion);
	}
	
	/**
	 * Método encargado de consultar la actualizacion de autorizaciones medicas por rangos
	 * @param connection
	 * @param criterios
	 * @return HashMap
	 */
	public static HashMap consultaXRangos (Connection con, HashMap criterios)
	{
		logger.info("===> Entré a consultaXRangos ");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().consultaXRangos(con, criterios);
	}
	
	/**
	 * Método encargado de consultar la actualizacion de autorizaciones medicas por rangos para la vista
	 * @param connection
	 * @param criterios
	 * @return HashMap
	 */
	public static HashMap consultaXRangosParaVista (Connection con, HashMap criterios)
	{
		logger.info("===> Entré a consultaXRangos ");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().consultaXRangosParaVista(con, criterios);
	}
	
	
	/**
	 * Método encargado de obtener la consulta para la actualizacion de autorizaciones medicas por rangos
	 * Para la generación del reporte
	 * @param connection
	 * @param criterios
	 * @return String consulta
	 */
	public static String obtenerConsulta (Connection con, HashMap criterios)
	{
		logger.info("===> Entré a consultaXRangosReporte ");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().obtenerConsulta(con, criterios);
	}
	
	/**
	 * Método encargado de obtener los centros de atención
	 * @param Coneection con
	 * @param int codInstitucion
	 * @return HashMap con los resultados de la obtención de los centros de atención
	 */
	public static HashMap obtenerCentrosDeAtencion (Connection con, int codInstitucion)
	{
		logger.info("===> Entré a obtenerCentrosDeAtencion ");
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getListadoIngresosDao().obtenerCentrosDeAtencion(con, codInstitucion);
	}
	
	/**
	 * Metodo encargado de activar el estado generar (Para la generación del reporte en pdf con el birt o el archivo plano)
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @throws SQLException 
	 * @return ActionForward reportepdf/archivoplano
	 */
	public static ActionForward generar(Connection con,ListadoIngresosForm forma,UsuarioBasico usuario, 
			HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion) throws SQLException
	{
		
		if ((forma.getCriterios("tipoSalida")+"").equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return generarReporte(con, usuario, forma, request, mapping);
		else
			return archivoPlano(con, forma, usuario, request, mapping, institucion);
	}
	
	/**
	 * Método encargado de generar reporte en pdf.
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 * @throws SQLException 
	 */
	public static ActionForward generarReporte (Connection connection,UsuarioBasico usuario,ListadoIngresosForm forma, 
			HttpServletRequest request,ActionMapping mapping) throws SQLException
	{
		/*
		 * Se hace una llamada al recolector de basura
		 */
		System.gc();
		
		DesignEngineApi comp;
		HashMap tmp = new HashMap(); 
		String newQuery="";
		tmp.putAll(consultaXRangos(connection, forma.getCriterios()));
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",
				"ActualizacionAutorizacionesMedicasXRangos.rptdesign");
		comp.obtenerComponentesDataSet("XRangos");
		
        /*
         * Imprimimos el encabezado del reporte
         */
		armarEncabezado(comp, connection, usuario, forma, request);
		logger.info("===> Vamos a enviar los datos de la consulta por debajo...");
		newQuery = obtenerConsulta(connection, forma.getCriterios());
        logger.info("===> Consulta en el BIRT Detallado por Tipo Transaccion: "+newQuery);
        
        /*
         * Se modifica el query
         */
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
		
    	UtilidadBD.cerrarConexion(connection);
        return mapping.findForward("principal");
	}
	
	/**
	 * Metodo encargado de organizar el encabezado 
	 * para el birt
	 * @param comp
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 */
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, ListadoIngresosForm forma, 
			HttpServletRequest request)
	{
		//Insertamos la información de la Institución
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(connection,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Insertamos el nombre de la funcionalidad en el reporte 
        comp.insertLabelInGridPpalOfHeader(1,1, "AUTORIZACIONES MÉDICAS POR RANGOS");
        comp.insertLabelInGridPpalOfHeader(3, 0, criteriosOrganizados(connection, forma, usuario));
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	/**
	 * Método encargado de organizar los criterios
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static String criteriosOrganizados (Connection connection, ListadoIngresosForm forma,UsuarioBasico usuario)
	{
		String criterios="";
        
	    if (UtilidadCadena.noEsVacio(forma.getCriterios("centroAtencion")+""))
        {
        	criterios+="Centro Atención: "+Utilidades.obtenerNombreCentroAtencion(connection, 
        				Utilidades.convertirAEntero(forma.getCriterios("centroAtencion")+""));
        }

        return criterios;
	}
	
	/**
	 * Metodo encargado de generar el archivo plano.
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param institucion
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static ActionForward archivoPlano (Connection connection, ListadoIngresosForm forma, UsuarioBasico usuario, 
			HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion) throws SQLException
	{
		
		HashMap tmp = new HashMap();
		tmp.putAll(consultaXRangos(connection, forma.getCriterios()));
		boolean OperacionTrue=false,existeTxt=false;
		int ban=ConstantesBD.codigoNuncaValido;
		
		/*
		 * Se llama al garbage collector
		 */
		System.gc();
		
		/*
		 * Iniciamos Transaccion
		 */
		UtilidadBD.iniciarTransaccion(connection);
		
		/*
		 * Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
		 */
		String path = "manejoPaciente/";
		logger.info("===> Path Valor por Defecto: "+path);
		
		/*
		 * Validamos si el path esta vacio o lleno
		 */
    	if(UtilidadTexto.isEmpty(path))
		{
    		forma.setOperacionTrue(false);
    		forma.setExisteArchivo(false);
    		UtilidadBD.abortarTransaccion(connection);
	    	UtilidadBD.closeConnection(connection);
	    	return ComunAction.accionSalirCasoError(mapping, request, connection, logger, "Inventarios", 
	    			"error.manejoPaciente.rutaNoDefinida", true);
		}
		
		//Arma el nombre del archivo plano
		String nombreReport=CsvFile.armarNombreArchivo("Actualizacion-Autorizaciones-Medicas-Por-Rangos", usuario);
		//Se genera el documento con la informacion
		logger.info("===> Nombre Report: "+nombreReport);
		logger.info("===> Este es el path: "+path);
		logger.info("===> Numregistros: "+tmp.get("numRegistros")+"");
		
		if (Utilidades.convertirAEntero(tmp.get("numRegistros")+"")>0)
		{
			logger.info("===> Archivo plano -->"+cargarMapa(connection, forma, usuario, tmp, institucion));
			OperacionTrue=TxtFile.generarTxt(cargarMapa(connection, forma, usuario, tmp, institucion),nombreReport,
					ValoresPorDefecto.getReportPath()+"manejoPaciente/",".csv");
			logger.info("===> ¿Se creo? --> "+OperacionTrue);
		}
		
		if(Utilidades.convertirAEntero(tmp.get("numRegistros")+"")==0)
		{
			forma.setMensaje(true);
		}
		
		if (OperacionTrue)
		{
			//se genera el archivo en formato Zip
			ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getReportPath()+path+nombreReport+".zip"+" "+
					ValoresPorDefecto.getReportPath()+path+nombreReport+".csv");
			//se ingresa la direccion donde se almaceno el archivo
			forma.setRuta(ValoresPorDefecto.getReportPath()+path+nombreReport+".csv");
			//se ingresa la ruta para poder descargar el archivo
			forma.setUrlArchivo(ValoresPorDefecto.getReportUrl()+path+nombreReport+".zip");
			//se valida si existe el csv
			existeTxt=UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+path, nombreReport+".csv");
			//se valida si existe el zip
			forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(ValoresPorDefecto.getReportPath()+path, nombreReport+".zip"));
									
			if (existeTxt )
				forma.setOperacionTrue(true);
		}
		
		UtilidadBD.cerrarConexion(connection);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo encargado de organizar los datos con ","
	 * para el reporte
	 * @param connection
	 * @param ListadoIngresosForm
	 * @param HashMap datos
	 * @param institucion
	 * @return StringBuffer
	 */
	public static StringBuffer cargarMapa(Connection connection, ListadoIngresosForm forma, 
			UsuarioBasico usuario, HashMap datos, InstitucionBasica institucion)
	{
		StringBuffer cadena = new StringBuffer();
		Vector criterios = new Vector();
		HashMap tmp = new HashMap();
		tmp.putAll(consultaXRangos(connection, forma.getCriterios()));
		//Razon social institucion
		cadena.append(institucion.getRazonSocial()+"\n");
		//Nit
		cadena.append(institucion.getNit()+"\n");
		//Direccion
		cadena.append(institucion.getDireccion()+"\n");
		//Telefono
		cadena.append(institucion.getTelefono()+"\n");
		//Titulo del reporte
		cadena.append("AUTORIZACIONES MÉDICAS POR RANGOS \n\n");  
		cadena.append(criteriosOrganizados(connection, forma, usuario)+"\n");
		
		//Aqui van los titulos de las celdas seguidos en una misma cadena
		logger.info("===> Numregistros: "+tmp.get("numRegistros")+"");
		cadena.append("PACIENTE,TIPO Y NO. ID,INGRESO,VIA ING.,FECHA ING.,ORDEN,SERVICIO/ARTICULO,ASOCIO,CANT.\n");
		String convenio = "";
		
		//Aqui se buscan los registros obtenidos del HasMap y se agregan al archivo plano		
		for(int i=0; i<Utilidades.convertirAEntero(datos.get("numRegistros")+""); i++)
		{
			if(!convenio.equals(datos.get("convenio_"+i)+""))
			{
				logger.info("===> El Convenio Será : "+datos.get("convenio_"+i)+"");
				cadena.append(datos.get("convenio_"+i)+"\n");
			}
			convenio = datos.get("convenio_"+i)+"";
			cadena.append(
					datos.get("paciente_"+i)+","+
					datos.get("tiponoid_"+i)+","+
					datos.get("ingreso_"+i)+","+
					datos.get("viaingreso_"+i)+","+
					datos.get("fechaingreso_"+i)+","+
					datos.get("orden_"+i)+","+
					datos.get("articuloservicio_"+i)+","+
					datos.get("asocio_"+i)+","+
					datos.get("cantidad_"+i)+","+"\n");
		}
		
		return cadena;
	}
	
	
	/**
	 * Método para cargar la autorizacion de una orden ambulatoria o solicitud
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoAutorizacion cargarAutorizacion(Connection con,String codigoDetAutorizacion,int codigoInstitucion)
	{
		DtoAutorizacion autorizacion = new DtoAutorizacion();
		
		HashMap criterios = new HashMap();
		criterios.put("codigoDetAutorizacion",codigoDetAutorizacion);
		criterios.put("codigoInstitucion", codigoInstitucion);
		
		autorizacion = lisIngresosDao().cargarAutorizacion(con, criterios);
		
		return autorizacion;
	}
	
	/**
	 * Método para preperar los datos para enviar
	 * @param con
	 * @param cuerpoDetalle
	 * @param posSolicitud
	 * @param codigoInstitucion
	 * @param encabezadoDetalle 
	 * @return
	 */
	public static DtoAutorizacion prepararDatosEnviar(Connection con,HashMap cuerpoDetalle,int posSolicitud,HashMap encabezadoDetalle,UsuarioBasico usuario)
	{
		DtoAutorizacion autorizacion = new DtoAutorizacion();
		String codigoDetAutorizacion = cuerpoDetalle.get(indicesServiciosArticulos[28]+posSolicitud).toString();
		int codigoViaIngreso = Integer.parseInt(encabezadoDetalle.get(indicesEncabezadoDetalle[7]).toString());
		
		autorizacion = cargarAutorizacion(con, codigoDetAutorizacion, usuario.getCodigoInstitucionInt());
		
		//Si no se ha registrado informacion del anexo técnico y no hay tipo de cobertura se consulta de la sub cuenta
		if(autorizacion.getEstado().equals("")&&autorizacion.getCodigoTipoCobertura()<=0)
		{
			autorizacion.setTipoCobertura(UtilidadesManejoPaciente.obtenerTipoCoberturaSubCuenta(con, cuerpoDetalle.get(indicesServiciosArticulos[34]+posSolicitud).toString()));
			
			if(autorizacion.getCodigoTipoCobertura()>0)
				autorizacion.setCoberturaSaludResponsable(true);
			else
				autorizacion.setCoberturaSaludResponsable(false);
		}
		
		//Si no se ha registro informacion del anexo técnico y no hay origen de atencion se busca consultarla
		if(autorizacion.getEstado().equals("")&&autorizacion.getCodigoOrigenAtencion()<=0)
		{
			String idCuenta = encabezadoDetalle.get(indicesEncabezadoDetalle[6]).toString();
			
			String tipoEvento = Cuenta.obtenerCodigoTipoEventoCuenta(con, idCuenta);
			autorizacion.setOrigenAtencion(lisIngresosDao().obtenerOrigenAtencionXCuenta(con, idCuenta, codigoViaIngreso, tipoEvento));
			
			if(autorizacion.getCodigoOrigenAtencion()>0)
				autorizacion.setOrigenAtencionGuardada(true);
			else
				autorizacion.setOrigenAtencionGuardada(false);
		}
		
		//Si no se han registrado diagnósticos se realiza la validación
		if(autorizacion.getEstado().equals("")&&autorizacion.getDiagnosticos().size()==0)
		{
			int codigoIngreso = Integer.parseInt(encabezadoDetalle.get(indicesEncabezadoDetalle[0]).toString());
			ArrayList<Diagnostico> diagnosticos = UtilidadesHistoriaClinica.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso,false);
			for(Diagnostico diagnostico:diagnosticos)
			{
				DtoDiagAutorizacion diagAutorizacion = new DtoDiagAutorizacion();
				diagAutorizacion.setDiagnostico(diagnostico);
				
				autorizacion.getDiagnosticos().add(diagAutorizacion);
				
			}
			autorizacion.diagnosticosDtoToHashMap();
			
		}
		if(autorizacion.getDiagnosticos().size()==0&&(codigoViaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna||codigoViaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios))
		{
			autorizacion.setPuedoModificarDiagnosticos(true);
		}
		else
		{
			autorizacion.setPuedoModificarDiagnosticos(false);
		}
		/*
		//Si no se ha ingreso un envío se postula uno
		if(autorizacion.getEnvios().size()==0)
		{
			DtoEnvioAutorizacion envio = new DtoEnvioAutorizacion();
			envio.setUsuarioModifica(usuario);
			autorizacion.getEnvios().add(envio);
		}
		*/
		return autorizacion;
		
	}
	
	
}
