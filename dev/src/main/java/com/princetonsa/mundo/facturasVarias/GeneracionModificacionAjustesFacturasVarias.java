package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;
import com.princetonsa.actionform.facturasVarias.GeneracionModificacionAjustesFacturasVariasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.GeneracionModificacionAjustesFacturasVariasDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class GeneracionModificacionAjustesFacturasVarias
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		public static Logger logger = Logger.getLogger(GeneracionModificacionAjustesFacturasVarias.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static GeneracionModificacionAjustesFacturasVariasDao generacionModificacionAjustesFacturasVariasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionModificacionAjustesFacturasVariasDao();
	}
	
	
	
	/*------------------------------------------------------------------------------------------------------
	 * INDICES PARA EL MANEJO DE LOS KEYS EN LOS MAPAS
	 * ----------------------------------------------------------------------------------------------------*/

	/**
	 * Indices de la consulta de ajustes de facturas varias
	 */
	public static String [] indicesAjustesFacturasVarias = {"codigo0_","consecutivo1_","tipoAjuste2_","fechaAjuste3_","factura4_","conceptoAjuste5_",
									   "valorAjuste6_","observaciones7_","estado8_","estaBd9_","institucion10_","usuarioModifica11_","deudor12_",
									   "nomConcepto13_","cosecFac14_","anioConsecutivo15_"};
	
	
	/**
	 * Indices del mapa Criterios para la consulta avanzada de ajustes
	 * de facturas varias
	 */
	public static String [] indicesCriteriosBusqueda= {"consecutivo0","factura1","fechaIni2","fechaFin3","institucion4","tipoDeudor5","deudor6",
													   "codigoFacVar7","codigoAjus8","nomDeu9","tipoIdent10","numIdent11"};
	
	/**
	 * 
	 */
	public static String [] indicesFacturasVarias = {"codigo0_","codigoFacVar1_","nomEstadoFactura2_","fechaAjuste3_","valorFactura4_",
													 "estadoFactura5_","nomDeudor6_","deudor7_","estado8_","concepto9_","institucion10_",
													 "saldoFactura11_","fecha12_","consecutivo13_","nomConcepto14_","fechaAprobacion15_",
													 "numIdent16_"};

	/*---------------------------------------------------------------------------------------------------------------------*/
	
	
	
	
	/**
	 * Metodo encargado de consultar los ajustes de facturas varias
	 * Los keys del mapa criterios son:
	 * ---------------------------------
	 * --consecutivo0_ --> Opcional
	 * --factura1_ --> Opcional
	 * --fechaIni2_--> Opcional
	 * --fechaFin3_--> Opcional
	 * --institucion4_--> Requerido
	 * ----------------------------------
	 * Los key's del mapa resultado
	 * ----------------------------------
	 * codigo0_,consecutivo1_,tipoAjuste2_,
	 * fechaAjuste3_,factura4_,conceptoAjuste5_,
	 * valorAjuste6_,observaciones7_,estado8_,estaBd9_
	 */
	 public static HashMap  consultaAjustesFacturasVarias (Connection connection,HashMap criterios, boolean ajusTodos)
	 {
		 return generacionModificacionAjustesFacturasVariasDao().consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
	 }
	 
	 
	 /**
		 * Metodo encargado de consultar  las facturas varias
		 * los key's del mapa criterios son:
		 * ----------------------------------------
		 * --factura1
		 * --fechaIni2
		 * --fechaFin3
		 * --institucion4
		 * --tipoDeudor5
		 * --deudor6
		 * LOS KEYS DEL MAPA RESULTADO:
		 * ---------------------------------------
		 * consecutivo0_,codigoFacVar1_,valorFactura2_
		 * fecha3_,nomEstadoFactura4_,estadoFactura5_
		 * nomDeudor6_,deudor7_,nomConcepto8_,concepto9_
		 * 
		 */
		public static HashMap consultaFacturasVarias (Connection connection,HashMap criterios)
		{
			return generacionModificacionAjustesFacturasVariasDao().consultaFacturasVarias(connection, criterios);
		}
		
		 /**
		  * Metodo encargado de ingresar los datos de ajustes
		  * facturas varias
		  * @param connection
		  * @param datos
		  * -------------------------------
		  * Key's del mapa datos
		  * -------------------------------
		  * --consecutivo1_ --> Requerido
		  * --institucion10_ --> Requerido
		  * --tipoAjuste2_ --> Requerido
		  * --fechaAjuste3_ --> Requerido
		  * --factura4_ --> Requerido
		  * --conceptoAjuste5_ --> Requerido
		  * --valorAjuste6_ --> Requerido
		  * --observaciones7_ --> Opcional
		  * --estado8_ --> Requerido
		  * --usuarioModifica11_ --> Requerido
		  * @return
		  */
		 public static int insertarAjustesFacturasVarias (Connection connection,HashMap datos)
		 {
			 return generacionModificacionAjustesFacturasVariasDao().insertarAjustesFacturasVarias(connection, datos);
		 }
	
		 		 
			/**
			 * Metodo encargado de modificar un ajuste.
			 * @param connection
			 * @param datos
			 * ---------------------------
			 * KEY'S DEL MAPA DATOS
			 * ---------------------------
			 * --  conceptoAjuste5_
			 * -- fechaAjuste3_
			 * -- valorAjuste6_
			 * -- observaciones7_
			 * -- codigo0_
			 */
			public static boolean modificarAjuste (Connection connection, HashMap datos)
			{
				return generacionModificacionAjustesFacturasVariasDao().modificarAjuste(connection, datos);
			}
		 
		 /**
		  * Metodo encargado de consultar los datos de la factura varia
		  * @param connection
		  * @param usuario
		  * @param codFacvar
		  * @return
		  */
		 public static  HashMap cargarFactura (Connection connection,UsuarioBasico usuario, String consecutivo)
		 {
			 logger.info("\n entre a cargarFactura codFacvar -->"+consecutivo); 
			 
			 HashMap criterios = new HashMap ();
			 criterios.put(indicesCriteriosBusqueda[4], usuario.getCodigoInstitucion());
			 if (UtilidadCadena.noEsVacio(consecutivo) && !consecutivo.equals(ConstantesBD.codigoNuncaValido+""))
			 {
				 	criterios.put(indicesCriteriosBusqueda[1], consecutivo);
			 		criterios = consultaFacturasVarias(connection, criterios); 
			 }
			 
			 return criterios;
			 
		 }
		 
		 /**
		  * Metodo encargado de consultar los datos de los ajustes de la factura
		  * varia.
		  * @param connection
		  * @param usuario
		  * @param codAjusFacVar
		  * @return
		  */
		 public static HashMap cargarAjustesFacVar (Connection connection,UsuarioBasico usuario,String codAjusFacVar, boolean ajusTodos)
		 {

			 logger.info("\n entre a cargarAjustesFacVar codAjusFacVar -->"+codAjusFacVar); 
			 
			 HashMap criterios = new HashMap ();
			 criterios.put(indicesCriteriosBusqueda[4], usuario.getCodigoInstitucion());
			 criterios.put(indicesCriteriosBusqueda[8], codAjusFacVar);
			 return consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
			 
		 }
		 
		 
		 /**
			 * Metodo Encargado de identificar cual es nombre del consecutivo a manejar.
			 * @param usuario
			 * @return
			 */
			public static String obtenerNombreConsecutivoAjustesFacturasVarias(UsuarioBasico usuario, String tipoAjuste)
			{
				String consecutivoManejar = "";
				
				//se verifica el tipo de ajuste a manejar el ajuste para saber cual es el consecutivo que maneja
				if (tipoAjuste.equals(ConstantesIntegridadDominio.acronimoCredito))
					consecutivoManejar = ConstantesBD.nombreConsecutivoAjustesCreditoFacturasVarias;
				
				
				if (tipoAjuste.equals(ConstantesIntegridadDominio.acronimoDebito))
					consecutivoManejar = ConstantesBD.nombreConsecutivoAjustesDebitoFacturasVarias;
				
				return consecutivoManejar;
			}
			
	/**
	 * Metodo encargado de iniciar la vista de facturas varias
	 * @param connection
	 * @param forma
	 * @param usuario
	 */		
	public static void initGenModAjustesFacturasVarias (Connection connection, GeneracionModificacionAjustesFacturasVariasForm forma, UsuarioBasico usuario)
	{
		logger.info("\n initGenModAjustesFacturasVarias ");
		
		//1)se carga la informacion de la factura Varia
		forma.setMapaInfoFac(GeneracionModificacionAjustesFacturasVarias.cargarFactura(connection, usuario, forma.getListado(indicesAjustesFacturasVarias[14]+forma.getIndex())+""));
	
		//2)se carga la informacion de ajustes a la factura si existe
		if (!(forma.getListado(indicesFacturasVarias[0]+forma.getIndex())+"").equals("") && !(forma.getListado(indicesFacturasVarias[0]+forma.getIndex())+"").equals("null"))
		{
			forma.setMapaAjustes(GeneracionModificacionAjustesFacturasVarias.cargarAjustesFacVar(connection, usuario, forma.getListado(indicesFacturasVarias[0]+forma.getIndex())+"", true));
			//se saca una copia de los ajustes
			forma.setMapaAjustesClone((HashMap)forma.getMapaAjustes().clone());
		}
		//se carga el concepto
		cargarConcepto(connection, forma, usuario);
		//logger.info("\n los conceptos en initGenModAjustesFacturasVarias --> "+forma.getConceptosFacVar());
		
	}
	
	public static void cargarAjuste (Connection connection, GeneracionModificacionAjustesFacturasVariasForm forma, UsuarioBasico usuario, String codigoAjuste)
	{
		//1)se carga la informacion de la factura Varia
		forma.setMapaInfoFac(GeneracionModificacionAjustesFacturasVarias.cargarFactura(connection, usuario, forma.getMapaInfoFac(indicesFacturasVarias[13]+"0")+""));
		
		forma.setMapaAjustes(GeneracionModificacionAjustesFacturasVarias.cargarAjustesFacVar(connection, usuario, codigoAjuste, true));
		//se saca una copia de los ajustes
		forma.setMapaAjustesClone((HashMap)forma.getMapaAjustes().clone());
	
		//se carga el concepto
		cargarConcepto(connection, forma, usuario);
		//logger.info("\n los conceptos en initGenModAjustesFacturasVarias --> "+forma.getConceptosFacVar());
		
	}
	
	
	
	/**
	 * Metodo en cargado de cargar los diferentes tipos de ajustes
	 */
	public static void cargarConcepto (Connection connection, GeneracionModificacionAjustesFacturasVariasForm forma,UsuarioBasico usuario)
	{

		HashMap parametros = new HashMap ();
		//---------se cargan los parametros por los cuales se va a buscar-----------------
		parametros.put("institucion", usuario.getCodigoInstitucion());
		parametros.put("tipoCartera", ConstantesBD.codigoTipoCarteraTodos+","+ConstantesBD.codigoTipoCarteraFacturasVarias);
		forma.setMapaAjustes(indicesAjustesFacturasVarias[3]+"0", UtilidadFecha.getFechaActual());
		if (!(forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals("") && !(forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals("null"))
			if ((forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals(ConstantesIntegridadDominio.acronimoCredito))
				parametros.put("naturaleza", ConstantesBD.codigoConceptosCarteraCredito);
			else
				if ((forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"").equals(ConstantesIntegridadDominio.acronimoDebito))
					parametros.put("naturaleza", ConstantesBD.codigoConceptosCarteraDebito);
		//--------------------------------------------------------------------------------
		// se carga la informacion del concepto de ajuste
		forma.setConceptosFacVar(UtilidadesFacturacion.obtenerConseptosFacturasVarias(connection, parametros));
		//logger.info("\n los conceptos  en cargarConcepto --> "+forma.getConceptosFacVar());
	}
	
	
	
	/**
	 * Metodo encargado de  consultar los datos de la tabla
	 * ajustes facturas varias
	 * @param connection
	 * @param criterios
	 * @param usuario
	 * @return
	 */
	public static HashMap consultarAjustesFacturasVarias (Connection connection, HashMap criterios, UsuarioBasico usuario, boolean ajusTodos)
	{	
		
	

		criterios.put(indicesCriteriosBusqueda[4], usuario.getCodigoInstitucion());
	
		return consultaAjustesFacturasVarias(connection, criterios, ajusTodos);
		
		
	}
	
	/**
	 * Metodo encargado de guardar los datos 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward guardar (Connection connection, GeneracionModificacionAjustesFacturasVariasForm forma, ActionMapping mapping,UsuarioBasico usuario )
	{
		logger.info("\n************* entre a guardar ****************");
		String nomConsec="",consecutivo="",anioConsecutivo="";
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		int codigo=ConstantesBD.codigoNuncaValido;
		logger.info("----->INICIANDO TRANSACCION ....");
		
		
		//modificar
		if (forma.getMapaAjustes().containsKey(indicesAjustesFacturasVarias[9]+"0") && (forma.getMapaAjustes(indicesAjustesFacturasVarias[9]+"0")+"").equals(ConstantesBD.acronimoSi))
		{
			codigo = Utilidades.convertirAEntero(forma.getMapaAjustes(indicesAjustesFacturasVarias[0]+"0")+"");
			//logger.info("\n entre a modificar  ");
			if(existeModificacion(forma.getMapaAjustes(), forma.getMapaAjustesClone()).equals(ConstantesBD.acronimoSi))
				transacction=organizaDatos(connection, forma.getMapaAjustes(), usuario);
			if (transacction)
			{
				String [] indices={indicesAjustesFacturasVarias[1],indicesAjustesFacturasVarias[2],indicesAjustesFacturasVarias[3],indicesAjustesFacturasVarias[4],
						indicesAjustesFacturasVarias[13],indicesAjustesFacturasVarias[6],indicesAjustesFacturasVarias[7],indicesAjustesFacturasVarias[8]};
				
				if (!(forma.getMapaAjustes(indicesAjustesFacturasVarias[6]+"0")+"").equals(forma.getMapaAjustesClone(indicesAjustesFacturasVarias[6]+"0")+""))
					Utilidades.generarLogGenerico(forma.getMapaAjustes(), forma.getMapaAjustesClone(), usuario.getLoginUsuario(), false, 0, ConstantesBD.logGeneracionModificacionAjustesFacVariasCodigo, indices);
			}
		}
		else//insertar
			if (!forma.getMapaAjustes().containsKey(indicesAjustesFacturasVarias[9]+"0") || !(forma.getMapaAjustes(indicesAjustesFacturasVarias[9]+"0")+"").equals(ConstantesBD.acronimoSi))
			{
				nomConsec=obtenerNombreConsecutivoAjustesFacturasVarias(usuario, forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"");
				consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(nomConsec, usuario.getCodigoInstitucionInt());
				anioConsecutivo=UtilidadBD.obtenerAnioConsecutivo(nomConsec,usuario.getCodigoInstitucionInt(), consecutivo);
				//logger.info("\n entre a insertar ");
				codigo=insertar(connection, forma, usuario,anioConsecutivo,consecutivo);
				
				if (codigo>0)
					transacction=true;
			}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);
			//Solo cuando se inserta
			if (!forma.getMapaAjustes().containsKey(indicesAjustesFacturasVarias[9]+"0") || !(forma.getMapaAjustes(indicesAjustesFacturasVarias[9]+"0")+"").equals(ConstantesBD.acronimoSi))
			{
				Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,  nomConsec, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
			cargarAjuste(connection, forma, usuario, codigo+"");
			logger.info("----->TRANSACCION AL 100% ....");
			forma.setOperacionTrue(true);
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
			//Solo cuando se inserta
			if (!forma.getMapaAjustes().containsKey(indicesAjustesFacturasVarias[9]+"0") || !(forma.getMapaAjustes(indicesAjustesFacturasVarias[9]+"0")+"").equals(ConstantesBD.acronimoSi))
			{
				Connection con=UtilidadBD.abrirConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,  nomConsec, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				UtilidadBD.closeConnection(con);
			}
			
		}
			
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("principal");
		
	}
	
	
	public static int insertar (Connection connection,GeneracionModificacionAjustesFacturasVariasForm forma ,UsuarioBasico usuario,String anioConsecutivo, String consecutivo)
	{
		logger.info("\n entro a insertar");
		
		HashMap datos = new HashMap ();
		//----se obtiene el valor del consecutivo, dependiendo de si es un ajuste debito o credito----------------------
	
		 
		logger.info("\n el valor del consecutivo -->"+consecutivo);
		//-------------------------------------------------------------------------------------------------------------
		
		//----se carga el mapa con los valores insertar----------------------------------------------------------------
		
		//consecutivo
		datos.put(indicesAjustesFacturasVarias[1], consecutivo);
		//anio consecutivo
		datos.put(indicesAjustesFacturasVarias[15], anioConsecutivo);
		//institucion
		datos.put(indicesAjustesFacturasVarias[10], usuario.getCodigoInstitucion());
		//tipo de ajuste
		datos.put(indicesAjustesFacturasVarias[2], forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0"));
		//facha ajuste
		datos.put(indicesAjustesFacturasVarias[3], forma.getMapaAjustes(indicesAjustesFacturasVarias[3]+"0"));
		//factura
		datos.put(indicesAjustesFacturasVarias[4], forma.getMapaInfoFac(indicesFacturasVarias[1]+"0"));
		//concepto ajuste
		datos.put(indicesAjustesFacturasVarias[5], forma.getMapaAjustes(indicesAjustesFacturasVarias[5]+"0"));
		//valor ajuste
		datos.put(indicesAjustesFacturasVarias[6], forma.getMapaAjustes(indicesAjustesFacturasVarias[6]+"0"));
		//observaciones
		datos.put(indicesAjustesFacturasVarias[7], forma.getMapaAjustes(indicesAjustesFacturasVarias[7]+"0"));
		//estado
		datos.put(indicesAjustesFacturasVarias[8], ConstantesIntegridadDominio.acronimoEstadoPendiente);
		//usuario modifica
		datos.put(indicesAjustesFacturasVarias[11], usuario.getLoginUsuario());
		
		return insertarAjustesFacturasVarias(connection, datos);
		
	}
	
	/**
	 * Metodo encargado de verificar si 
	 * existe modificacion 
	 * @param datos
	 * @param datosClone
	 * @return
	 */
	public static String existeModificacion (HashMap datos, HashMap datosClone)
	{
		//logger.info("\n entre a exisis datos--> "+datos+" datosClone --> "+datosClone);
		String result=ConstantesBD.acronimoNo;
		//conceptoAjuste5_
		if (!(datos.get(indicesAjustesFacturasVarias[5]+"0")+"").equals(datosClone.get(indicesAjustesFacturasVarias[5]+"0")+""))
			result = ConstantesBD.acronimoSi; 
		else //fechaAjuste3_
			if (!(datos.get(indicesAjustesFacturasVarias[3]+"0")+"").equals(datosClone.get(indicesAjustesFacturasVarias[3]+"0")+""))
				result = ConstantesBD.acronimoSi;
			else //valorAjuste6_
				if (!(datos.get(indicesAjustesFacturasVarias[6]+"0")+"").equals(datosClone.get(indicesAjustesFacturasVarias[6]+"0")+""))
					result = ConstantesBD.acronimoSi;
				else //observaciones7_
					if (!(datos.get(indicesAjustesFacturasVarias[7]+"0")+"").equals(datosClone.get(indicesAjustesFacturasVarias[7]+"0")+""))
						result = ConstantesBD.acronimoSi;
		
		return result;
	}
	
	
	/**
	 * Metodo encargado de organizar los datos
	 * para almecenar la modificacion
	 * @param connection
	 * @param datosModificados
	 * @param usuario
	 * @return
	 */
	public static boolean organizaDatos (Connection connection, HashMap datosModificados,UsuarioBasico usuario)
	{
		HashMap datos = new HashMap (); 
		//conceptoAjuste5_
		datos.put(indicesAjustesFacturasVarias[5], datosModificados.get(indicesAjustesFacturasVarias[5]+"0"));
		//fechaAjuste3_
		datos.put(indicesAjustesFacturasVarias[3], datosModificados.get(indicesAjustesFacturasVarias[3]+"0"));
		//valorAjuste6_
		datos.put(indicesAjustesFacturasVarias[6], datosModificados.get(indicesAjustesFacturasVarias[6]+"0"));
		//observaciones7_
		datos.put(indicesAjustesFacturasVarias[7], datosModificados.get(indicesAjustesFacturasVarias[7]+"0"));
		//usuarioModifica11_
		datos.put(indicesAjustesFacturasVarias[11], usuario.getLoginUsuario());
		//codigo0_
		datos.put(indicesAjustesFacturasVarias[0], datosModificados.get(indicesAjustesFacturasVarias[0]+"0"));
		
		return modificarAjuste(connection, datos); 
	}
	
	
	
	/**
	 *  Ordena Un Mapa HashMap a partir del patron de ordenamiento
	 *  @param HashMap mapaOrdenar
	 *  @param String patronOrdenar
	 *  @param String ultimoPatron
	 *  @return Mapa Ordenado
	 **/
	public HashMap accionOrdenarMapa(HashMap mapaOrdenar,GeneracionModificacionAjustesFacturasVariasForm forma)
	{			
		
		
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");		
		mapaOrdenar = (Listado.ordenarMapa(indicesAjustesFacturasVarias,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
	
		
		return mapaOrdenar;
	}	
	
}