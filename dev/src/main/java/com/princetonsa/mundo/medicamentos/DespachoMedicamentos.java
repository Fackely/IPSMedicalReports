
/*
 * @(#)DespachoMedicamentos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.medicamentos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.DespachoMedicamentosDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.GeneracionExcepcionesFarmacia;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * Clase para el manejo de despacho de medicamentos
 * @version 1.0, Agosto 31, 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lï¿½pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rï¿½os</a>
 */
@SuppressWarnings("unchecked")
public class DespachoMedicamentos 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static DespachoMedicamentosDao despachoDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(DespachoMedicamentos.class);

	/**
	 * Nï¿½mero de la solicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * Fecha de la solicitud
	 */
	private String fechaSolicitud;

	/**
	 * Hora de solicitud
	 */
	private String horaSoliciutd;
	
	/**
	 * medico que solicita
	 */
	private String medicoSolicitante;
	
	/**
	 * nï¿½mero de autorizaciï¿½n
	 */
	//private String numeroAutorizacion;
	
	/**
	 * Cantidad parcial del despacho que se ha realizado en la farmacia
	 */
	private int despachoParcial;
	
	/**
	 * Estado mï¿½dico de la solicitud
	 */
	private String estadoMedico;
	
	/**
	 * Observaciones Generales de la solicitud de medicamentos
	 */
	private String observacionesGenerales;
	
	/**
	 * Boolean que indica si el despacho es o no directo
	 */
	private boolean esDirecto;
	
	/**
	 * login del usuario
	 */
	private String usuario;
	
	/**
	 * Cï¿½digo del artï¿½culo que va ha ser sustituto
	 */
	private int numeroSustituto;
	
	/**
	 * Cï¿½digo del artï¿½culo que es original
	 */
	private int numeroOriginal;
	
	/**
	 * Consecutivo de orden mï¿½dica
	 */
	private int orden;
	
	/**
	 * 
	 */
	private String nombrePersonaRecibe;
	
	/**
	 * codigo centro de costo
	 */
	private int codigoFarmacia;
	
	
	/**
	 * codigo centro costo solicitado para el pedido
	 */
	private Integer codigoCentroCostoSolicitado;
	
	/**
	 * nombre centro costo solicitado para el pedido
	 */
	private String nombreCentroCostoSolicitado;
	
	
	
	
	/**
	 * resetea los datos pertinentes
	 */
	public void reset()
	{
		this.numeroSolicitud=0;	
		this.fechaSolicitud="";
		this.horaSoliciutd="";
		this.medicoSolicitante="";
		//this.numeroAutorizacion="";
		this.despachoParcial=-1;
		this.estadoMedico="";
		this.observacionesGenerales="";
		this.esDirecto=false;
		this.usuario="";
		this.numeroSustituto=0;
		this.numeroOriginal=0;
		this.orden=0;
		this.nombrePersonaRecibe="";
		this.codigoFarmacia = ConstantesBD.codigoNuncaValido;
		this.codigoCentroCostoSolicitado = ConstantesBD.codigoNuncaValido;
		this.nombreCentroCostoSolicitado = "";
	}	
	
	
	//constructor de la clase
	public DespachoMedicamentos(int numeroSolicitud)
	{
		this.numeroSolicitud=numeroSolicitud;
		this.init (System.getProperty("TIPOBD"));
	}	
	
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parï¿½metros
	 */		
	public DespachoMedicamentos()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}

	
	/**
	 * Mï¿½todo que obtiene todos los resultados 
	 * de las solicitudes internas de medicamentos
	 * filtradas decuerdo al centro de costo del usuario 
	 * para mostrarlos en el listado
	 * 
	 * keys{codigoPersona, codigoCentroCostoUser, areaFiltro, camaFiltro, habitacionFiltro, pisoFiltro, filtrarPorCantidad}
	 * 
	 * @param con
	 * @return
	 */
	public Collection listadoSolicitudesMedicamentos(Connection con, int codigoCentroCostoUser, int codigoPersona, int institucion, int areaFiltro, int camaFiltro, int habitacionFiltro, int pisoFiltro)
	{
		despachoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao();
		HashMap<String, Object> mapa= new HashMap<String, Object>();
		mapa.put("codigoPersona", codigoPersona);
		mapa.put("codigoCentroCostoUser", codigoCentroCostoUser);
		mapa.put("areaFiltro", areaFiltro);
		mapa.put("camaFiltro", camaFiltro);
		mapa.put("habitacionFiltro", habitacionFiltro);
		mapa.put("pisoFiltro", pisoFiltro);
		mapa.put("institucion", institucion+"");
		
		Collection coleccion=null;
		try
		{
			boolean filtrarPorCantidad=!UtilidadTexto.getBoolean(ValoresPorDefecto.getIngresoCantidadFarmacia(institucion));
			mapa.put("filtrarPorCantidad", filtrarPorCantidad);
			coleccion=UtilidadBD.resultSet2Collection(despachoDao.listadoSolicitudesMedicamentos(con,mapa));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho medicamentos " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	
	
	/**
	 * Carga los registros existentes de Insumos en la BD.
	 * @param con, Connection, conexion con la fuente de datos.
	 * @param numeroSolicitud, int numero de la solicitud.
	 * @return, collection
	 */
	public Collection infoInsumos(Connection con, int numeroSolicitud)
	{
	    despachoDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao();
	    Collection coleccion=null;
	    try
	    {
	        coleccion=UtilidadBD.resultSet2Collection(despachoDao.infoInsumos(con,numeroSolicitud));  
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error mundo despacho medicamentos " +e.toString());
			coleccion=null;  
	    }
	    return coleccion;
	}
	
	
	/**
	 * Cuando se aï¿½ade un sustituto entonces se hace un UNION a la consulta principa
	 * y entonces me devuelve el resultado de esta nueva consulta
	 * @param con
	 * @param numeroSolicitud
	 * @param coleccionNumerosSustitutos
	 * @param codigoInstitucion intituciï¿½n del usuario
	 * @return
	 * @throws SQLException
	 */
	public Collection consultaGenerada (Connection con, int numeroSolicitud, Collection coleccionNumerosSustitutos, int codigoInstitucion, int codigoArticulo) throws SQLException
	{
		despachoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(despachoDao.consultaGenerada(con,numeroSolicitud,coleccionNumerosSustitutos, codigoInstitucion, codigoArticulo));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho medicamentos " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}	/**
	 * Cuando se añade un sustituto se deben buscar lso equivalentes de los med. substituidos
	 * @param con
	 * @param numeroSolicitud
	 * @param coleccionNumerosSustitutos
	 * @param codigoInstitucion intitución del usuario
	 * @return
	 * @throws SQLException
	 */
	public Collection consultaEquivalentes (Connection con, int codSus, int codPrin) throws SQLException
	{
		despachoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao();
		Collection coleccion=null; 
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(despachoDao.consultaEquivalentes(con,codSus,codPrin));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho medicamentos " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * Mï¿½todo para cargar los datos bï¿½sicos de la solicitud
	 * @param con conexiï¿½n
	 * @param codigoInstitucion Institucion del usuario
	 * @param  empresa
	 * @return
	 * @throws SQLException
	 */
	public boolean infoSolicitud(Connection con, int numeroSolicitud, int codigoInstitucion, int codigoArticulo) throws SQLException
	{
	   ResultSetDecorator rs=despachoDao.infoSolicitud(con,numeroSolicitud, false, codigoInstitucion, codigoArticulo);
	   if (rs.next())
	   {
		    this.numeroSolicitud=rs.getInt("numeroSolicitud");
		    this.fechaSolicitud=rs.getString("fechaSolicitud");
		    this.horaSoliciutd=rs.getString("horaSolicitud");
		    this.medicoSolicitante=rs.getString("medicoSolicitante");
		    //this.numeroAutorizacion=rs.getString("numeroAutorizacion");
		    this.estadoMedico=rs.getString("estadoMedico");
		    this.observacionesGenerales=rs.getString("observacionesGenerales");
			this.orden=rs.getInt("orden");
		    
		    return true;
		    
	   }
		else
		{
			return false;
		}
		
	}
	
	/**
	 * Mï¿½todo que obtiene todos los resultados 
	 * de los artï¿½culos de las solicitudes de medicamentos
	 * @param con
	 * @param codigoInstitucion institucion del usuario
	 * @return
	 */
	public Collection listadoMedicamentos(Connection con, int numeroSolicitud, int codigoInstitucion, int codigoArticulo)
	{
		despachoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao();
		Collection coleccion=new ArrayList();
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(despachoDao.infoSolicitud(con,numeroSolicitud,true, codigoInstitucion, codigoArticulo));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho medicamentos " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeDespachocConEntregaDirPac(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao().existeDespachocConEntregaDirPac(con, numeroSolicitud);
	}
	
	
	/**
	 * Mï¿½todo que Inserta la info bï¿½sica y el detalle del despacho,
	 * Tablas (despacho, detalle_despachos)
	 * @param con una conexion abierta con una fuente de datos
	 * @param cantidadesSolicitadas 
	 * @param actualizarPyP 
	 * @return nï¿½mero de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public int insertarDespachoBasicoYDetalle(	Connection con, 
												Collection datosCollection, 
												int tamanoCollectionOriginal, 
												int datosCodigosArticulos[],
												Vector cantidadesSolicitadas, 
												int datosInsumosArticulo[],
												int datosInsumosCantidad[],
												int codigoEstadoMedico,
												/*String numeroAutorizacion,*/
												boolean esDirecto,
												boolean generarCargo,
												UsuarioBasico user,
												PersonaBasica paciente, String estado,
												String mezclaOrdenDieta, 
												boolean actualizarPyP,
												String radioFinal,
												HashMap despachoMap,
												String nombrePersonaRecibe,
												HashMap<String, String> sustitutos) throws IPSException  
	{
		int  codigoDespacho=0;
		int limiteFinal=tamanoCollectionOriginal>datosCodigosArticulos.length?datosCodigosArticulos.length:tamanoCollectionOriginal;
		int resp2[]=new int[datosCollection.size()];
		int resp3[]=new int[limiteFinal];
		//int resp3[]=new int[tamanoCollectionOriginal];	
		int resp4[]=new int[datosInsumosArticulo.length];
		CargosEntidadesSubcontratadas cargosEntidadesSubcontratadas = new CargosEntidadesSubcontratadas();
		
		logger.info("error->1 cantidad:"+datosInsumosArticulo.length);
		
		//Iniciamos la transacciï¿½n, si el estado es empezar
		boolean inicioTrans;
		
		if (estado.equals(ConstantesBD.inicioTransaccion))
		{
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		}
		else
		{
			inicioTrans=true;
		}
		
		logger.info("error->2");
		
		/*if(esDirecto)
		{
			if(radioFinal.equals("radioFinal"))
			{	
				//se cambia para el caso en que todas sean cero, si genera cargo el estado queda en Cargada o pendiente
				this.cambiarEstadoFacturacionSolicitud(con, ConstantesBD.codigoEstadoFAnulada, numeroSolicitud);
			}	
		}*/
		
		logger.info("error->3");
		
		/*
		 * Insertar cantidades solicitadas
		 */
		SolicitudMedicamentos solicitud=new SolicitudMedicamentos();
		solicitud.actualizarCantidades(con, cantidadesSolicitadas, numeroSolicitud);
		
		if(!mezclaOrdenDieta.trim().equals(""))
		{	
			//se actualiza la informacion de las cantidades para el caso de mezclas
			//y las cantidades de la justificacion no pos en caso de que exista
			for(int wil=0; wil<datosCodigosArticulos.length-1; wil++)
		    {
				if(!Utilidades.actualizarCantidadArticuloSolMedicamentos(con, numeroSolicitud+"", datosCodigosArticulos[wil]+"", despachoMap.get("parcial_"+(wil+1)).toString()))
				{
					UtilidadBD.abortarTransaccion(con);
					logger.warn("error en actualiza la informacion de las cantidades para el caso de mezclas");
					return -1;
				}
				try
				{
					FormatoJustArtNopos.actualizarCantidadJustificacionRespMezcla(con, Utilidades.convertirAEntero(despachoMap.get("parcial_"+(wil+1)).toString()), numeroSolicitud,  Utilidades.convertirAEntero(datosCodigosArticulos[wil]+"") );
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
		    }
		}	
		
		logger.info("error->4");
		
		if (!inicioTrans||codigoDespacho<0  )
		{
			/*
			 * Se aborta la traqnsacciï¿½n porque no se
			 * pudieron modificar las cantidades solicitadas
			 */
			UtilidadBD.abortarTransaccion(con);
			logger.warn("no se pudieron modificar las cantidades solicitadas");
			return -1;
		}
		
		logger.info("error->5");
		codigoDespacho=despachoDao.insertarDespachoBasico(con,usuario,esDirecto,numeroSolicitud, nombrePersonaRecibe);
		logger.info("---->"+codigoDespacho);
		if (!inicioTrans||codigoDespacho<1  )
		{
			UtilidadBD.abortarTransaccion(con);
			logger.warn("error insertando el desspacho basico");
			return -1;
		}
		else
		{
			int i=0;
			Iterator it=datosCollection.iterator();
			int datosParticulares[];
			//int x=tamanoCollectionOriginal;
			int x=limiteFinal;
			
			logger.info("error->6---------"+datosCollection.size());
			/**En este ciclo se insertan los medicamentos sustitutos*/
			while (it.hasNext())
			{
				//logger.info("error->6.1--------->"+((int[])it.next()));
			    datosParticulares=(int[])it.next();
			    String tipoDespacho="", almacenConsignacion="", proveedorCompra="", proveedorCatalogo="";
				if(despachoMap.containsKey("tipodespacho_"+(x+1)))
					tipoDespacho=despachoMap.get("tipodespacho_"+(x+1)).toString();
				if(despachoMap.containsKey("almacenConsignacion_"+(x+1)))
					almacenConsignacion=despachoMap.get("almacenConsignacion_"+(x+1)).toString();
				if(despachoMap.containsKey("proveedorCompra_"+(x+1)))
					proveedorCompra=despachoMap.get("proveedorCompra_"+(x+1)).toString();
				if(despachoMap.containsKey("proveedorCatalogo_"+(x+1)))
					proveedorCatalogo=despachoMap.get("proveedorCatalogo_"+(x+1)).toString();
			    
				resp2[i]=despachoDao.insertarDetalleDespacho(con,datosParticulares[0],datosParticulares[1], Integer.parseInt(despachoMap.get("parcial_"+(x+1)).toString()), codigoDespacho, despachoMap.get("lote_"+(x+1)).toString(), despachoMap.get("fechaVencimientoLote_"+(x+1)).toString(), tipoDespacho, almacenConsignacion, proveedorCompra, proveedorCatalogo,0);
				
				logger.info("ARITUCLO SUSTITUTOS -->"+datosParticulares[0]+" DEL ARTICULO PRINCIPAL "+datosParticulares[1]);
				//POR SOLICITUD DE VERSALLES Y HABLANDO CON MARGARITA, SE DECIDE AGREGAR LOS ARTICULOS PRINCIPALES A LA SOLICITUD, PARA QUE SE VEAN REFLEJADOS EN EL ESTADO DE LA CUENTA Y EN LA FACTURACION.
				//TODO
				
				
				
				
				
				
				
				
				
				
//				GENERACION DEL CARGO POR ARTICULO SEGï¿½N EL ANEXO 799
				//boolean existeMedicamentoEnSolicitud = SolicitudMedicamentos.existeMedicamentoEnSolicitud(con, numeroSolicitud, datosParticulares[0]);
				SolicitudMedicamentos.insertarAticulosEquivalenteSolicitud(con,numeroSolicitud, datosParticulares[0], datosParticulares[1]);
				if(resp2[i] > 0 && Utilidades.convertirAEntero(despachoMap.get("parcial_"+(x+1))+"") > 0)
				{
					cargosEntidadesSubcontratadas.generarCargoArticulo(con, codigoFarmacia, datosParticulares[0], datosParticulares[1], numeroSolicitud+"", "", fechaSolicitud, horaSoliciutd, true, user,"","");
				}
				
				logger.info("INSERTO DETALLE DESPACHO ARTICULO-->"+datosParticulares[0]);
				//se anaden los insumos que se seleccionaron desde el despacho de medicamentos y que no hacen parte de la solicitud como tal
				//if(!existeMedicamentoEnSolicitud)
				{
					
					logger.info("\n\n VA HA INSERTAR EL ARTICULO NUEVO EN LA SOL - SUBCUENTA--->"+datosParticulares[0]+" \n");
					//se ingresan las solicitudes de sub cuenta y el cargo pendiente por articulo
					Cargos cargoArticulos= new Cargos();
					
					int numeroResponsables= cargoArticulos.obtenerNumeroResponsablesSolicitudCargo(con, numeroSolicitud, datosParticulares[0], false);
					
					if(numeroResponsables<=0)
					{	
						boolean generoCargo=	cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(		con, 
		        	    																										user, 
									        	    																			paciente, 
									        	    																			numeroSolicitud, 
									        	    																			datosParticulares[0] /*codigoArticulo*/, 
									        	    																			datosParticulares[1]/*cantidadArticulo*/, 
									        	    																			true/*dejarPendiente*/, 
									        	    																			ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
									        	    																			ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
									        	    																			ConstantesBD.codigoNuncaValido, 
									        	    																			ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
									        	    																			Utilidades.esSolicitudPYP(con, numeroSolicitud),"", false /*tarifaNoModificada*/);
		        	    if(!generoCargo)
		        		{
		        	    	UtilidadBD.abortarTransaccion(con);
		        			logger.warn("Error generando el cargo de la solicitud= "+ this.getNumeroSolicitud() +" y articulo ->"+datosParticulares[0]);
		        			return -1;
		        		}
		        	    else {
		        	    	//*************************** Guardar Justificaciï¿½n Pendiente
							if(despachoDao.validarNOPOS(con, numeroSolicitud, datosParticulares[0],true))
							{
								logger.info("Entro a guardar la justificaciï¿½n pendiente / Subcuenta: "+Utilidades.convertirAEntero(cargoArticulos.getDtoDetalleCargo().getCodigoSubcuenta()+""));
								UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, datosParticulares[0], Utilidades.convertirAEntero(despachoMap.get("parcial_"+(x+1))+""), user.getLoginUsuario(), true, false, Utilidades.convertirAEntero(cargoArticulos.getDtoDetalleCargo().getCodigoSubcuenta()+""),ConstantesIntegridadDominio.acronimoMedicamento);
								//despachoDao.insertarJusNP(con, numeroSolicitud, datosInsumosArticulo[y], user.getLoginUsuario());
							}
							//***********************************************************
		        	    }
					}
				}
				
				
				
				
				
				
				
				
				
				
				
//				GENERACION DEL CARGO POR ARTICULO SEGï¿½N EL ANEXO 799
				if(resp2[i] > 0 && Utilidades.convertirAEntero(despachoMap.get("parcial_"+(x+1))+"") > 0);
				{
					cargosEntidadesSubcontratadas.generarCargoArticulo(con, codigoFarmacia, datosParticulares[0], datosParticulares[1], numeroSolicitud+"", "", fechaSolicitud, horaSoliciutd, false, user,"","");
				}
				
				//inserta las excepciones de farmacia para ese artï¿½culo, centro_costo_solicita, convenio
				
				if(codigoEstadoMedico==ConstantesBD.codigoEstadoHCDespachada)
				{
					/// XPLANNER 37023 SEPTIEMBRE 1/08
				    /*if(ValoresPorDefecto.getGenExcepcionesFarmAut(user.getCodigoInstitucionInt()).trim().equals("true") || ValoresPorDefecto.getGenExcepcionesFarmAut(user.getCodigoInstitucionInt()).trim().equals("t"))
				    {    
					    boolean insertoBienExcepcionFarmacia= this.ingresarExcepcionesFarmaciaXConvenio(con,user,paciente,datosParticulares[0]);
						if(!insertoBienExcepcionFarmacia)
						{
							UtilidadBD.abortarTransaccion(con);
							logger.warn("error inserta las excepciones de farmacia para ese artï¿½culo, centro_costo_solicita, convenio");
							return -1;
						}
				    }*/	
				}	
				x++;
				i++;
			}
			
			logger.info("error->7");
			
			for (i=0;i<resp2.length;i++)
			{
				if (resp2[i]<1)
				{
					UtilidadBD.abortarTransaccion(con);
					logger.warn("error");
					return -1;
				}
			}
			
			logger.info("error->8");
			/**En este ciclo se insertan los medicamentos originales, por esta razï¿½n articuloSustituto y articuloPrincipal son iguales**/
			for(int w=0; w<limiteFinal; w++)
			{
				String tipoDespacho="", almacenConsignacion="", proveedorCompra="", proveedorCatalogo="";
				if(despachoMap.containsKey("tipodespacho_"+(w+1)))
					tipoDespacho=despachoMap.get("tipodespacho_"+(w+1)).toString();
				if(despachoMap.containsKey("almacenConsignacion_"+(w+1)))
					almacenConsignacion=despachoMap.get("almacenConsignacion_"+(w+1)).toString();
				if(despachoMap.containsKey("proveedorCompra_"+(w+1)))
					proveedorCompra=despachoMap.get("proveedorCompra_"+(w+1)).toString();
				if(despachoMap.containsKey("proveedorCatalogo_"+(w+1)))
					proveedorCatalogo=despachoMap.get("proveedorCatalogo_"+(w+1)).toString();
				
				//TODO
				resp3[w]=despachoDao.insertarDetalleDespacho(con,datosCodigosArticulos[w],Utilidades.convertirAEntero(sustitutos.get(datosCodigosArticulos[w]+"")), Integer.parseInt(despachoMap.get("parcial_"+(w+1)).toString()),codigoDespacho, despachoMap.get("lote_"+(w+1)).toString(), despachoMap.get("fechaVencimientoLote_"+(w+1)).toString(), tipoDespacho, almacenConsignacion, proveedorCompra, proveedorCatalogo,Utilidades.convertirADouble(despachoMap.get("saldodosisdespachada_"+(w+1))+""));
				
//				GENERACION DEL CARGO POR ARTICULO SEGï¿½N EL ANEXO 799
				if(resp3[w] > 0 && Utilidades.convertirAEntero(despachoMap.get("parcial_"+(w+1))+"") > 0)
				{
					try {
						logger.info("fechaSolicitud"+fechaSolicitud+", Hora Solicitud"+horaSoliciutd);
						cargosEntidadesSubcontratadas.generarCargoArticulo(con, codigoFarmacia, datosCodigosArticulos[w], Utilidades.convertirAEntero(sustitutos.get(datosCodigosArticulos[w]+"")), numeroSolicitud+"", "", fechaSolicitud, horaSoliciutd, false, user,"", "");
					} catch (Exception e) {
						logger.info("ERROR SQL OJO:::::::");
						e.printStackTrace();
					}
				}
				//inserta las excepciones de farmacia para ese artï¿½culo, centro_costo_solicita, convenio
				
				/// XPLANNER 37023 SEPTIEMBRE 1/08
				/*
				if(ValoresPorDefecto.getGenExcepcionesFarmAut(user.getCodigoInstitucionInt()).trim().equals("true") || ValoresPorDefecto.getGenExcepcionesFarmAut(user.getCodigoInstitucionInt()).trim().equals("t"))
			    { 
					if(codigoEstadoMedico==ConstantesBD.codigoEstadoHCDespachada)
					{    
						boolean insertoBienExcepcionFarmacia= this.ingresarExcepcionesFarmaciaXConvenio(con,user,paciente,datosCodigosArticulos[w]);
						if(!insertoBienExcepcionFarmacia)
						{
							UtilidadBD.abortarTransaccion(con);
							logger.warn("error las excepciones de farmacia");
							return -1;
						}
					}
			    }*/	
			}
			
			logger.info("error->9");
			
			for (i=0;i<resp3.length;i++)
			{
				if (resp3[i]<1)
				{
					UtilidadBD.abortarTransaccion(con);
					logger.warn("error");
					return -1;
				}
			}
			
			logger.info("error->10 despachoMap-->"+despachoMap);
			
			for(int y=0;y<datosInsumosArticulo.length; y++)
			{
				logger.info("y->"+y);
				String tipoDespacho="", almacenConsignacion="", proveedorCompra="", proveedorCatalogo="";
				if(despachoMap.containsKey("tipodespachoInsumo_"+y))
					tipoDespacho=despachoMap.get("tipodespachoInsumo_"+y).toString();
				if(despachoMap.containsKey("almacenConsignacionInsumo_"+y))
					almacenConsignacion=despachoMap.get("almacenConsignacionInsumo_"+y).toString();
				if(despachoMap.containsKey("proveedorCompraInsumo_"+y))
					proveedorCompra=despachoMap.get("proveedorCompraInsumo_"+y).toString();
				if(despachoMap.containsKey("proveedorCatalogoInsumo_"+y))
					proveedorCatalogo=despachoMap.get("proveedorCatalogoInsumo_"+y).toString();
				

				logger.info("CANTIDAD -->"+datosInsumosCantidad[y]);
				resp4[y]=despachoDao.insertarDetalleDespacho(con,datosInsumosArticulo[y], datosInsumosArticulo[y],datosInsumosCantidad[y],codigoDespacho, despachoMap.get("loteInsumo_"+y).toString(), despachoMap.get("fechaVencimientoLoteInsumo_"+y).toString(), tipoDespacho, almacenConsignacion, proveedorCompra, proveedorCatalogo,0);
				
//				GENERACION DEL CARGO POR ARTICULO SEGï¿½N EL ANEXO 799
				boolean existeMedicamentoEnSolicitud = SolicitudMedicamentos.existeMedicamentoEnSolicitud(con, numeroSolicitud, datosInsumosArticulo[y]); 
				if(resp4[y] > 0 && Utilidades.convertirAEntero(despachoMap.get("parcial_"+(y+1))+"") > 0)
				{
					cargosEntidadesSubcontratadas.generarCargoArticulo(con, codigoFarmacia, datosInsumosArticulo[y], datosInsumosArticulo[y], numeroSolicitud+"", "", fechaSolicitud, horaSoliciutd, existeMedicamentoEnSolicitud?false:true, user,"", "");
				}
				
				logger.info("INSERTO DETALLE DESPACHO ARTICULO-->"+datosInsumosArticulo[y]);
				//se anaden los insumos que se seleccionaron desde el despacho de medicamentos y que no hacen parte de la solicitud como tal
				if(!existeMedicamentoEnSolicitud)
				{
					
					logger.info("\n\n VA HA INSERTAR EL ARTICULO NUEVO EN LA SOL - SUBCUENTA--->"+datosInsumosArticulo[y]+" \n");
					//se ingresan las solicitudes de sub cuenta y el cargo pendiente por articulo
					Cargos cargoArticulos= new Cargos();
					
					int numeroResponsables= cargoArticulos.obtenerNumeroResponsablesSolicitudCargo(con, numeroSolicitud, datosInsumosArticulo[y], false);
					
					if(numeroResponsables<=0)
					{	
						boolean generoCargo=	cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(		con, 
		        	    																										user, 
									        	    																			paciente, 
									        	    																			numeroSolicitud, 
									        	    																			datosInsumosArticulo[y] /*codigoArticulo*/, 
									        	    																			datosInsumosCantidad[y]/*cantidadArticulo*/, 
									        	    																			true/*dejarPendiente*/, 
									        	    																			ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
									        	    																			ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
									        	    																			ConstantesBD.codigoNuncaValido, 
									        	    																			ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
									        	    																			Utilidades.esSolicitudPYP(con, numeroSolicitud),"", false /*tarifaNoModificada*/);
		        	    if(!generoCargo)
		        		{
		        	    	UtilidadBD.abortarTransaccion(con);
		        			logger.warn("Error generando el cargo de la solicitud= "+ this.getNumeroSolicitud() +" y articulo ->"+datosInsumosArticulo[y]);
		        			return -1;
		        		}
		        	    else {
		        	    	//*************************** Guardar Justificaciï¿½n Pendiente
							if(despachoDao.validarNOPOS(con, numeroSolicitud, datosInsumosArticulo[y],false))
							{
								logger.info("Entro a guardar la justificaciï¿½n pendiente / Subcuenta: "+Utilidades.convertirAEntero(cargoArticulos.getDtoDetalleCargo().getCodigoSubcuenta()+""));
								UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, datosInsumosArticulo[y], datosInsumosCantidad[y], user.getLoginUsuario(), true, false, Utilidades.convertirAEntero(cargoArticulos.getDtoDetalleCargo().getCodigoSubcuenta()+""),ConstantesIntegridadDominio.acronimoInsumo);
								//despachoDao.insertarJusNP(con, numeroSolicitud, datosInsumosArticulo[y], user.getLoginUsuario());
							}
							//***********************************************************
		        	    }
					}
				}
			}
			
			for (i=0;i<resp4.length;i++)
			{
				if (resp4[i]<1)
				{
					UtilidadBD.abortarTransaccion(con);
					logger.warn("error");
					return -1;
				}
			}
		}
		
		logger.info("error-11");
		
		int resp5=0;
		//si es diferente entonces hacer el cambio 
		resp5=despachoDao.cambiarEstadoMedicoSolicitudYNumAutorizacion(con,codigoEstadoMedico,/*numeroAutorizacion,*/numeroSolicitud);
		if(resp5<1)
		{
			UtilidadBD.abortarTransaccion(con);
			logger.warn("error cambiar estado medico y num autorizacion");
			return -1;
		}
		
		logger.info("error->12");
		
		if(generarCargo)
		{
			/*boolean esMezclasDesdeDespacho=false;
			if(!mezclaOrdenDieta.trim().equals(""))
				esMezclasDesdeDespacho=true;*/
			
			Cargos cargos= new Cargos();
			
			boolean generoCargoExitoso= cargos.recalcularCargoArticuloXSolicitudDespachoMedicamentos(con, numeroSolicitud, user,"");
			//generarCargoDespachoMedicamentos(con,user,paciente,numeroSolicitud, esMezclasDesdeDespacho);
			if(!generoCargoExitoso)
			{
				logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud);
				UtilidadBD.abortarTransaccion(con);
				return -1;
			}
		}
		
		logger.info("error->13");
		
		if(actualizarPyP)
		{
			if(Utilidades.esSolicitudPYP(con, numeroSolicitud))
			{
				//primero se actualiza la actividad a estado ejecutado
				String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,numeroSolicitud);
				Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPEjecutado,user.getLoginUsuario(),"");
				
				//segundo se actualiza el acumulado de la actividad 
				if(Utilidades.actualizarAcumuladoPYP(con, numeroSolicitud+"", user.getCodigoCentroAtencion()+"")<=0)
				{
					logger.warn("Error actualizando el acumulado PYP de la solicitud= "+ numeroSolicitud);
					UtilidadBD.abortarTransaccion(con);
					return -1;
				}
			}
		}
		
		logger.info("error->14");
		/*Solicitud mundoSolicitud= new Solicitud();
		int validarIn= mundoSolicitud.actualizarMedicoRespondeTransaccional(con, this.getNumeroSolicitud(), user, ConstantesBD.continuarTransaccion);
		
		if(validarIn<=0)
		{
			logger.warn("Error actualizando el mï¿½dico responde de la solicitud= "+ numeroSolicitud);
			abortTransaction(con);
			return -1;
		}*/
		logger.info("error->15");
		
		if (estado.equals(ConstantesBD.finTransaccion))
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		//endTransaction(con);
		
		return codigoDespacho;
	}
	
	
	/**
	 * Mï¿½todo que ingresa las excepciones de farmacia por convenio para cada uno de los artï¿½culos
	 * que tienen parametrizaciï¿½n en dicha tabla
	 * @param con
	 * @param user
	 * @param paciente
	 * @param codigoArticulo
	 * @return
	 */
	public boolean ingresarExcepcionesFarmaciaXConvenio(Connection con, UsuarioBasico user, PersonaBasica paciente, int codigoArticulo)
	{
	    GeneracionExcepcionesFarmacia objeto= new GeneracionExcepcionesFarmacia();
	    try
	    {
	        ResultSetDecorator rs= objeto.consultaExcepcionesFarmacia(con , paciente.getCodigoConvenio(), UtilidadValidacion.getCodigoCentroCostoSolicitanteXNumeroSolicitud(con, this.getNumeroSolicitud()), codigoArticulo,false,true);
		    
		    if (rs.next())
			{
		        objeto.setNumeroSolicitud(this.getNumeroSolicitud());
		        objeto.setCodigoArticulo(codigoArticulo);
		        objeto.setPorcentajeNoCubierto(rs.getDouble("no_cubreEF"));
		        
		        objeto.insertarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.continuarTransaccion, user.getLoginUsuario() );
			}
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error mundo  ingresarExcepcionesFarmaciaXConvenio" +e.toString());
			return false;
	    }
	    return true;
	}
	
	/**
	 * Mï¿½todo que ingresa las excepciones de farmacia por convenio para cada uno de los artï¿½culos
	 * que tienen parametrizaciï¿½n en dicha tabla
	 * @param con
	 * @param user
	 * @param paciente
	 * @param codigoArticulo
	 * @return
	 */
	public boolean ingresarExcepcionesFarmaciaXConvenioSinPaciente(Connection con, UsuarioBasico user, int codigoConvenio,int codigoCentroCostoSolicitante, int codigoArticulo)
	{
	    GeneracionExcepcionesFarmacia objeto= new GeneracionExcepcionesFarmacia();
	    try
	    {
	        ResultSetDecorator rs= objeto.consultaExcepcionesFarmacia(con , codigoConvenio, codigoCentroCostoSolicitante, codigoArticulo,false,true);
		    
		    if (rs.next())
			{
		        objeto.setNumeroSolicitud(this.getNumeroSolicitud());
		        objeto.setCodigoArticulo(codigoArticulo);
		        objeto.setPorcentajeNoCubierto(rs.getDouble("no_cubreEF"));
		        
		        objeto.insertarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.continuarTransaccion, user.getLoginUsuario() );
			}
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error mundo  ingresarExcepcionesFarmaciaXConvenio" +e.toString());
			return false;
	    }
	    return true;
	}
	
	 /**
	   * Mï¿½todo que permite insertar unicamente el despacho basico en una transaccion retornando el codigo del despacho 
	   * Recibe como parï¿½metro el nivel de transaccionalidad
	   * @param con
	   * @param estado
	   * @return
	   * @throws SQLException
	   */	
	  public int insertarDespachoBasicoUnicamenteTransaccional (Connection con, String estado) throws SQLException
	  {
	      int codigoDespacho=0;
	      DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	      if (estado.equals(ConstantesBD.inicioTransaccion))
	      {
	          if (!myFactory.beginTransaction(con))
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      try
	      {
	          codigoDespacho=despachoDao.insertarDespachoBasico(con,usuario,esDirecto,numeroSolicitud,"");
	          if (codigoDespacho<=0)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	          throw e;
	      }
	      
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return codigoDespacho;
	  }
	
	  /**
	   * Mï¿½todo que permite insertar unicamente el despacho basico en una transaccion retornando el codigo del despacho 
	   * Recibe como parï¿½metro el nivel de transaccionalidad
	   * @param con
	   * @param estado
	   * @return
	   * @throws SQLException
	   */	
	  public int insertarDetalleDespachoUnicamenteTransaccional (	Connection con, String estado, int articulo, int articuloPPal,
	          																						int codigoDespacho, int cantidad,
	          																						String lote,
	          																						String fechaVencimiento,
	          																						String tipoDespacho,
	          																						String almacenConsignacion,
	          																						String proveedorCompra,
	          																						String proveedorCatalogo) throws SQLException
	  {
	      int numeroInsert=0;
	      DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	      if (estado.equals(ConstantesBD.inicioTransaccion))
	      {
	          if (!myFactory.beginTransaction(con))
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      try
	      {
	          numeroInsert=despachoDao.insertarDetalleDespacho(con, articulo, articuloPPal, cantidad, codigoDespacho, lote, fechaVencimiento, tipoDespacho, almacenConsignacion, proveedorCompra, proveedorCatalogo,0 );
	          
	          if (numeroInsert<=0)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	          throw e;
	      }
	      
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return numeroInsert;
	  }  
	  
	/**
	 * Consulta los articulos (Principales - Sustitutos) que ya han sido insertados
	 * para restringir la bï¿½squeda en el tag
	 * @param con
	 * @return
	 */
	public  Collection articulosInsertados(Connection con, int numeroSolicitud)
	{
		Collection coleccion=null;
		try
		{
			coleccion= despachoDao.articulosInsertados(con,numeroSolicitud);
			return coleccion;
		}
		catch(Exception e)
		{
			logger.warn("Error mundo despacho Medicamentos " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	 /**
	  * metodo que cambia elestado medico de la solicitud  en una transacion
	  * @param con
	  * @param estado
	  * @param codigoEstadoMedico
	  * @return
	  * @throws SQLException
	  */
	  public int cambiarEstadoMedicoSolicitudTransaccional (	Connection con, String estado, int codigoEstadoMedico/*, String numAutorizacion*/) throws SQLException
	  {
	      int numeroInsert=0;
	      DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	      if (estado.equals(ConstantesBD.inicioTransaccion))
	      {
	          if (!myFactory.beginTransaction(con))
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      try
	      {
	          numeroInsert=despachoDao.cambiarEstadoMedicoSolicitudYNumAutorizacion(con,codigoEstadoMedico,/*numAutorizacion,*/numeroSolicitud);
	          
	          if (numeroInsert<=0)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	          throw e;
	      }
	      
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return numeroInsert;
	  }  
	
    /**
	 * Mï¿½todo que one la solicitu en estado anulada cuando no se genera ningun cargo
	 * @param con
	 * @param estadoFacturacion
	 * @param numeroSolicitud
	 * @return
	 */
	public int cambiarEstadoFacturacionSolicitud(Connection con, int estadoFacturacion, int numeroSolicitud)
	{
		return despachoDao.cambiarEstadoFacturacionSolicitud(con, estadoFacturacion, numeroSolicitud);
	}
	  
	  
	/**
	 * Mï¿½todo que elimina un sustituto con despacho total 0 (NOTA: pero que ya esta almacenado en BD )
	 */
	public  int eliminarSustitutoConDespachoCero(Connection con, int articulo, int numeroSolicitud)
	{
		int resp=0;
		resp=despachoDao.eliminarSustitutoConDespachoCero(con,articulo,numeroSolicitud);
		return resp;
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaciï¿½n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
			if (myFactory != null)
			{
				despachoDao = myFactory.getDespachoMedicamentosDao();
				wasInited = (despachoDao != null);
			}
			return wasInited;
	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean dosisConsistentesSolicitud(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao().dosisConsistentesSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the fechaSolicitud.
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	/**
	 * @param fechaSolicitud The fechaSolicitud to set.
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	/**
	 * @return Returns the horaSoliciutd.
	 */
	public String getHoraSoliciutd() {
		return horaSoliciutd;
	}
	/**
	 * @param horaSoliciutd The horaSoliciutd to set.
	 */
	public void setHoraSoliciutd(String horaSoliciutd) {
		this.horaSoliciutd = horaSoliciutd;
	}
	/**
	 * @return Returns the medicoSolicitante.
	 */
	public String getMedicoSolicitante() {
		return medicoSolicitante;
	}
	/**
	 * @param medicoSolicitante The medicoSolicitante to set.
	 */
	public void setMedicoSolicitante(String medicoSolicitante) {
		this.medicoSolicitante = medicoSolicitante;
	}
	/**
	 * @return Returns the numeroAutorizacion.
	 */
	/*
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * @param numeroAutorizacion The numeroAutorizacion to set.
	 */
	/*
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	*/
	/**
	 * @return Returns the despachoParcial.
	 */
	public int getDespachoParcial() {
		return despachoParcial;
	}
	/**
	 * @param despachoParcial The despachoParcial to set.
	 */
	public void setDespachoParcial(int despachoParcial) {
		this.despachoParcial = despachoParcial;
	}
	
	/**
	 * @return Returns the estadoMedico.
	 */
	public String getEstadoMedico() {
		return estadoMedico;
	}
	/**
	 * @param estadoMedico The estadoMedico to set.
	 */
	public void setEstadoMedico(String estadoMedico) {
		this.estadoMedico = estadoMedico;
	}
	
	/**
	 * @return Returns the observacionesGenerales.
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales The observacionesGenerales to set.
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	
	/**
	 * @return Returns the esDirecto.
	 */
	public boolean getEsDirecto() {
		return esDirecto;
	}
	/**
	 * @param esDirecto The esDirecto to set.
	 */
	public void setEsDirecto(boolean esDirecto) {
		this.esDirecto = esDirecto;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @return Returns the numeroOriginal.
	 */
	public int getNumeroOriginal() {
		return numeroOriginal;
	}
	/**
	 * @param numeroOriginal The numeroOriginal to set.
	 */
	public void setNumeroOriginal(int numeroOriginal) {
		this.numeroOriginal = numeroOriginal;
	}
	/**
	 * @return Returns the numeroSustituto.
	 */
	public int getNumeroSustituto() {
		return numeroSustituto;
	}
	/**
	 * @param numeroSustituto The numeroSustituto to set.
	 */
	public void setNumeroSustituto(int numeroSustituto) {
		this.numeroSustituto = numeroSustituto;
	}
    /**
     * @return Returns the orden.
     */
    public int getOrden() {
        return orden;
    }
    /**
     * @param orden The orden to set.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

	/**
	 * @return the nombrePersonaRecibe
	 */
	public String getNombrePersonaRecibe() {
		return nombrePersonaRecibe;
	}

	/**
	 * @param nombrePersonaRecibe the nombrePersonaRecibe to set
	 */
	public void setNombrePersonaRecibe(String nombrePersonaRecibe) {
		this.nombrePersonaRecibe = nombrePersonaRecibe;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param dosis
	 * @param sumarDosis
	 * @return
	 */
	public boolean actualizarDosisSobrantes(Connection con, int codigoCentroCosto, int codigoArticulo,String unidadmedida, double dosis, boolean sumarDosis) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao().actualizarDosisSobrantes(con, codigoCentroCosto,codigoArticulo,unidadmedida,dosis,sumarDosis);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param unidadMedida
	 * @return
	 */
	public static double obtenerDosisSaldoArticulo(Connection con, int codigoCentroCosto,String codigoArticulo,String unidadMedida)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoMedicamentosDao().obtenerDosisSaldoArticulo(con, codigoCentroCosto,codigoArticulo,unidadMedida);
	}

	/**
	 * @return the codigoFarmacia
	 */
	public int getCodigoFarmacia() {
		return codigoFarmacia;
	}

	/**
	 * @param codigoFarmacia the codigoFarmacia to set
	 */
	public void setCodigoFarmacia(int codigoFarmacia) {
		this.codigoFarmacia = codigoFarmacia;
	}


	public static DespachoMedicamentosDao getDespachoDao() {
		return despachoDao;
	}


	public static void setDespachoDao(DespachoMedicamentosDao despachoDao) {
		DespachoMedicamentos.despachoDao = despachoDao;
	}


	public Integer getCodigoCentroCostoSolicitado() {
		return codigoCentroCostoSolicitado;
	}


	public void setCodigoCentroCostoSolicitado(Integer codigoCentroCostoSolicitado) {
		this.codigoCentroCostoSolicitado = codigoCentroCostoSolicitado;
	}


	public String getNombreCentroCostoSolicitado() {
		return nombreCentroCostoSolicitado;
	}


	public void setNombreCentroCostoSolicitado(String nombreCentroCostoSolicitado) {
		this.nombreCentroCostoSolicitado = nombreCentroCostoSolicitado;
	}
	
}
