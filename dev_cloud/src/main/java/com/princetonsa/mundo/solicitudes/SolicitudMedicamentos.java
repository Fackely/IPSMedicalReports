/*
 * Creado en 2/09/2004
 *
 * Juan David Ramï¿½rez Lï¿½pez
 * Princeton S.A.
 */
package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.ObjetoReferencia;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesJustificacionNoPos;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.medicamentos.SolicitudMedicamentosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudMedicamentosDao;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;


/**
 * @author Juan David Ramï¿½rez Lï¿½pez
 *
 * Princeton S.A.
 */
public class SolicitudMedicamentos extends Solicitud implements Cloneable
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(SolicitudMedicamentos.class);

	/**
	 * Objeto para interactuar con la BD
	 */
	private SolicitudMedicamentosDao solicitudMedicamentosDao;
	
	/**
	 * Statement para almacenar las observaciones generales de la solicitud
	 */
	private String observacionesGenerales;
	
	/**
	 * String con el codigo del centro de costo principal
	 */
	private String centroCostoPrincipal;
	
	private String entidadSubcontratada;
	
	/**
	 * Vector con los artï¿½culos que se va a ingresar en la solicitud
	 */
	@SuppressWarnings("rawtypes")
	private Vector articulos;
	
	/**
	 * El control especial
	 */
	private String controlEspecial;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if(solicitudMedicamentosDao==null)
		{
				DaoFactory myFactory= DaoFactory.getDaoFactory(tipoBD);
				solicitudMedicamentosDao= myFactory.getSolicitudMedicamentosDao();
				if(solicitudMedicamentosDao!=null)
				{
					return true;
				}
		}
		return false;
	}

	/**
	 * 
	 */
	public Object clone()
	{
	    Object clone = null;
	    try
	    {
	        clone = super.clone();
	    } 
	    catch(CloneNotSupportedException e)
	    {
	        // No deberia suceder
	    }
	    return clone;
	}
	
	
	/**
	 * Mï¿½todo para ingresar la solicitud de medicamentos
	 * @param con Connecciï¿½n con la BD
	 * @param request 
	 * @return Mayor que 0 si fue correcta la inserciï¿½n de la solicitud
	 */
	@SuppressWarnings({"unchecked", "rawtypes" })
	public int ingresarSolicitudMedicamentos(Connection con, int codigoInstitucion, UsuarioBasico usuario, PersonaBasica paciente, SolicitudMedicamentosForm forma, String codigoCentroCostoPrincipal, HttpServletRequest request) throws IPSException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    FormatoJustArtNopos fjan=new FormatoJustArtNopos();
	    String codigoOrden="";
	    ArrayList<InfoResponsableCobertura> infoCoberturaMedicamento=null;
	       
		try
		{
			ArrayList<DtoEntidadSubcontratada> entidades =null;

			String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, Utilidades.convertirAEntero(codigoCentroCostoPrincipal))+"";
			if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				 entidades = solicitudMedicamentosDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigoCentroCostoPrincipal, usuario.getCodigoInstitucionInt());
				 for(int i=0;i<entidades.size();i++)
					{
						entidadSubcontratada=entidades.get(i).getConsecutivo();
					}
			}
			
			this.setNumeroSolicitud(this.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion));
			this.setCentroCostoPrincipal(codigoCentroCostoPrincipal);
			
			if(forma.getCheckCE() == null || forma.getCheckCE().equals(""))
				forma.setCheckCE(ConstantesBD.acronimoNo);
			
			solicitudMedicamentosDao.ingresarSolicitudMedicamentos(con, getNumeroSolicitud(), observacionesGenerales, ConstantesBD.continuarTransaccion, centroCostoPrincipal, forma.getCheckCE(), entidadSubcontratada);
			
			//es una orden ambulatoria.
			if(!this.getOrdenAmbulatoria().trim().equals(""))
			{
			    codigoOrden=this.getOrdenAmbulatoria();
				HashMap vo=new HashMap();
				vo.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
				vo.put("numeroSolicitud",this.getNumeroSolicitud()+"");
				vo.put("numeroOrden",codigoOrden+"");
				vo.put("usuario",this.getLoginMedico());
				OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,vo);
				if(this.isSolPYP())
				{
					OrdenesAmbulatorias.actualizarEstadoActividadProgramaPYPPAcienteNumOrdenEstatico(con,codigoOrden,ConstantesBD.codigoEstadoProgramaPYPSolicitado,this.getLoginMedico(),"");
					Utilidades.asignarSolicitudToActividadPYP(con,codigoOrden,this.getNumeroSolicitud()+"");
				}
			}

			infoCoberturaMedicamento=new ArrayList<InfoResponsableCobertura>();
			for(int i=0; i<articulos.size(); i++)
			{
				int cod=((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo();
				ArticuloSolicitudMedicamentos articulo=getArticuloSolicitud(cod);
				
				
				//Segun la validacion medico especialista se valida si se hace o no la solicitud de este medicamento. [justificacion articulos no pos]
			/*	if(forma.getMedicamentos("es_pos_"+articulo.getArticulo()).toString().equals("false"))
				{
					if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(articulo.getArticulo()+"")) )
						{
							if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true))//segun validacion medico especialista queda pendiente=0
								{
								ingresarArticulo(con, ((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo(), null, false, codigoInstitucion);
								}
							else
								logger.info("\n**************************************************************************************\n" +
											"*[NO SE REALIZA SOLICITUD SEGUN VALIDACION MEDICO ESPECIALISTA JUSTIFICACION NO POS]  *  \n" +
											"***************************************************************************************");
						}
				}
				else*/
					ingresarArticulo(con, ((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo(), null, false, codigoInstitucion);
				
//Formato justificacion nopos
				
					if(forma.getMedicamentos("es_pos_"+articulo.getArticulo()).toString().equals("false"))
					{
						
						
						
						/*if(UtilidadTexto.getBoolean(forma.getMedicamentos("insertarJustNP_"+articulo.getArticulo())))
						{*/
							if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(articulo.getArticulo()+"")) )
							{
								
								
									//frecuencia estaba en int
									if(UtilidadValidacion.esMedicamento(articulo.getArticulo())){
										
										//Insercion asocio justificacion solicitudes
										HashMap mapaAsocioJustArt=(HashMap)request.getSession().getAttribute("MAPASOCIOJUSTIFICACION");
										if(mapaAsocioJustArt !=null
												&&forma.getJustificacionMap().containsKey(articulo.getArticulo()+"_sevaasociar")
												&&mapaAsocioJustArt.get(articulo.getArticulo()+"_codigoAsocio")!=null
												&&!UtilidadTexto.isEmpty(mapaAsocioJustArt.get(articulo.getArticulo()+"_codigoAsocio").toString())){
											UtilidadesJustificacionNoPos.insertarAsocioSolicitudJustificacion(con, this.getNumeroSolicitud(), Utilidades.convertirAEntero(mapaAsocioJustArt.get(articulo.getArticulo()+"_codigoAsocio").toString()),articulo.getCantidadSolicitada());
											
										}else{
										
											if(forma.getJustificacionMap().containsKey(articulo.getArticulo()+"_pendiente") && forma.getJustificacionMap().get(articulo.getArticulo()+"_pendiente").equals("1")){
												
												// Guardar Justificacion No Pos Medicamento
												HashMap mapaJustArt=(HashMap)request.getSession().getAttribute(articulo.getArticulo()+"MAPAJUSART");
												if(mapaJustArt==null){
													mapaJustArt=forma.getJustificacionMap();
												}
												if(forma.getJustificacionMap()!=null){
	 												Set<String>keys=forma.getJustificacionMap().keySet();
													for(String key:keys){
														if(!mapaJustArt.containsKey(key)){
															mapaJustArt.put(key,forma.getJustificacionMap().get(key));
														}
													}
												}
												
												mapaJustArt.put(articulo.getArticulo()+"_mapasecciones",request.getSession().getAttribute("MAPASECJUSART"));
												
												fjan.insertarJustificacion(	con,
														getNumeroSolicitud(), 
														ConstantesBD.codigoNuncaValido,
														mapaJustArt,
														forma.getMedicamentosNoPosMap(),
														forma.getMedicamentosPosMap(),
														forma.getSustitutosNoPosMap(),
														forma.getDiagnosticosDefinitivos(),
														articulo.getArticulo(),
														codigoInstitucion, 
														observacionesGenerales, 
														ConstantesBD.continuarTransaccion,
														articulo.getArticulo(), 
														articulo.getObservaciones(), 
														articulo.getDosis(), 
														articulo.getUnidosis(), 
														Integer.parseInt(articulo.getFrecuencia()+""), 
														articulo.getTipoFrecuencia(), 
														articulo.getVia(), 
														articulo.getCantidadSolicitada(), 
														articulo.getDiasTratamiento(),
														usuario.getLoginUsuario()
														);
											}
										}
									} else {
										// Guardar Justificacion No Pos de Articulo diferente a medicamento
										ArrayList<DtoParamJusNoPos> justificacion = new ArrayList<DtoParamJusNoPos>();
										justificacion = (ArrayList<DtoParamJusNoPos>)request.getSession().getAttribute("JUSTIFICACION");
										if(justificacion != null){
											for(int jus=0; jus<justificacion.size(); jus++){
								    			if(justificacion.get(jus).getCodigoArticulo().equals(articulo.getArticulo()+"")){
								    				justificacion.get(jus).setSolicitud(getNumeroSolicitud()+"");
								    				//UtilidadesJustificacionNoPos.guardarJustificacion(con, justificacion.get(jus), ConstantesIntegridadDominio.acronimoInsumo, usuario);
								    				if(!this.getOrdenAmbulatoria().trim().equals("")){
								    					int codigoJustificacion=UtilidadesJustificacionNoPos.consultarCodigoJustificacion(con,codigoInstitucion , ConstantesIntegridadDominio.acronimoInsumo, articulo.getArticulo()+"", 
								    							this.getOrdenAmbulatoria()!=null&&!this.getOrdenAmbulatoria().trim().isEmpty()?this.getOrdenAmbulatoria():null, 
								    									null);
								    					justificacion.get(jus).setCodigoJustificacion(codigoJustificacion+"");
								    					UtilidadesJustificacionNoPos.actualizarJustificacion(con, justificacion.get(jus), ConstantesIntegridadDominio.acronimoInsumo, usuario);
								    				}else{
								    					UtilidadesJustificacionNoPos.guardarJustificacion(con, justificacion.get(jus), ConstantesIntegridadDominio.acronimoInsumo, usuario);
								    				}
								    			}
								    		}
										}
									}
									
											
								if(!this.getOrdenAmbulatoria().trim().equals("")){
									UtilidadJustificacionPendienteArtServ.actualizarSolicitudJusOrdenAmbulatoria(con,Utilidades.convertirAEntero(this.getOrdenAmbulatoria()),articulo.getArticulo(), getNumeroSolicitud(), true);
								}
									
							}
							else
								logger.info("\n\n\n [NO NECESITA JUSTIFICACION NO POS SEGUN VALIDACION CONVENIO] Convenio->"+paciente.getCodigoConvenio()+" - "+Utilidades.obtenerNombreConvenioOriginal(con, paciente.getCodigoConvenio())+" \n\n\n");
						/*}
					else
						logger.info("\n\n\n [NO NECESITA JUSTIFICACION NO POS SEGUN VALIDACION CONVENIO] Convenio->"+paciente.getCodigoConvenio()+" - "+Utilidades.obtenerNombreConvenioOriginal(con, paciente.getCodigoConvenio())+" \n\n\n");*/

				}
//------------------------				
				

//					se ingresan las solicitudes de sub cuenta y el cargo pendiente por articulo
					Cargos cargoArticulos= new Cargos();
	        	    boolean generoCargo=	cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(		con, 
	        	    																										usuario, 
								        	    																			paciente, 
								        	    																			this.getNumeroSolicitud(), 
								        	    																			((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo(), 
								        	    																			((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getCantidadSolicitada()/*cantidadArticulo*/, 
								        	    																			true/*dejarPendiente*/, 
								        	    																			ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
								        	    																			ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
								        	    																			this.getCentroCostoSolicitado().getCodigo(), 
								        	    																			ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
								        	    																			this.isSolPYP(),this.getFechaSolicitud(),false /*tarifaNoModificada*/);
	        	    
	        	    /**Se adiciona a cada articulo la informacion correspondiente de la cobertura para 
	        	     * evaluacion posterior en la autorizacion de Capitacion*/
	        	    DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta=new DtoSolicitudesSubCuenta();
	        	    dtoSolicitudesSubCuenta.getArticulo().setCodigo(((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo()+"");
	        	    dtoSolicitudesSubCuenta.setNumeroSolicitud(this.getNumeroSolicitud()+"");
	        	    dtoSolicitudesSubCuenta.setConsecutivoSolicitud(this.getConsecutivoOrdenesMedicas()+"");
	        	    dtoSolicitudesSubCuenta.setUrgenteSolicitud(this.getUrgente());
	        	    //dtoSolicitudesSubCuenta.setFinalidadSolicitud(this.getfin);
	        	    cargoArticulos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
	        	    infoCoberturaMedicamento.add(cargoArticulos.getInfoResponsableCoberturaGeneral());
	        	    /**------------------------------------------------------------------------------------*/
	        	    if(!generoCargo)
	        		{
	        			logger.warn("Error generando el cargo de la solicitud= "+ this.getNumeroSolicitud() +" y articulo ->"+((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo());
	        			myFactory.abortTransaction(con);
	        			return 0;
	        		}
	        	    
	        	      forma.setCodigoDetalleCargo( Utilidades.convertirAEntero(String.valueOf(cargoArticulos.getDtoDetalleCargo().getCodigoDetalleCargo())));
	        	      forma.setCodigoSubCuentaCargo(Utilidades.convertirAEntero(String.valueOf(cargoArticulos.getDtoDetalleCargo().getCodigoSubcuenta())));
	        	      forma.setCodigoConvenioCargo(cargoArticulos.getDtoDetalleCargo().getCodigoConvenio());
	        	      logger.warn("Datos Cargo  codigo = "+ cargoArticulos.getDtoDetalleCargo().getCodigoDetalleCargo() +" y  subcuenta->"+cargoArticulos.getDtoDetalleCargo().getCodigoSubcuenta());
	        	       
	        	      if(Autorizaciones.actualizarAutorizacionyDetalle(con, codigoOrden, paciente.getCodigoCuenta()+"", forma.getCodigoConvenioCargo()+"", this.getNumeroSolicitud()+"", forma.getCodigoDetalleCargo()+"", forma.getCodigoSubCuentaCargo()+"",cod+"")!=1)
	        	      {
	        	    	   logger.warn("Error generando Actualizando los datos de Autorizacion solicitud= "+ this.getNumeroSolicitud() +" y articulo ->"+((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo());
		        			myFactory.abortTransaction(con);
		        			return 0;
	        	      }
			}
			
			//Se eliminar el atributo en sesion
			//request.removeAttribute("MAPASOCIOJUSTIFICACION");
			request.getSession().removeAttribute("MAPASOCIOJUSTIFICACION");
			//fgfgf
			request.getSession().setAttribute("RESETASOCIOJUSTIFICACION", 1);
			
			
			forma.setInfoCoberturaArticulo(infoCoberturaMedicamento);
				
				
			/*myFactory.endTransaction(con);
			
			// Remover el atributo de JUSTIFICACION  de la sesion
			request.getSession().removeAttribute("JUSTIFICACION");
			
			myFactory.endTransaction(con);
			

				myFactory.abortTransaction(con);
				logger.warn("No hay articulos validos para generar la solicitud de medicamentos. segun validacion profesional de la salud");
				return ConstantesBD.codigoNuncaValido;*/
			
		}
		catch (SQLException e)
		{
			logger.error("Error insertando la solicitud general: "+e);
			return 0;
		}
		return this.getNumeroSolicitud();
	}
	
	
	/**
	 * Metodo que insertar Unicamente Sol MedicamentosTransaccional en el estado continuar transaccion
	 * @param con
	 * @return
	 */
	public int insertarUnicamenteSolMedicamentosTransaccional (Connection con) 
	{
		
		ArrayList<DtoEntidadSubcontratada> entidades =null;
		UsuarioBasico usuario=new UsuarioBasico();
		String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, Utilidades.convertirAEntero(getCentroCostoPrincipal()))+"";
		if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			
			
			
			entidades = solicitudMedicamentosDao.obtenerEntidadesSubcontratadasCentroCosto(con, centroCostoPrincipal, usuario.getCodigoInstitucionInt());
			for(int i=0;i<entidades.size();i++)
			{
				entidadSubcontratada=entidades.get(i).getConsecutivo();
			}
						
		}  
		
	    int resultado=solicitudMedicamentosDao.ingresarSolicitudMedicamentos(con, getNumeroSolicitud(), observacionesGenerales, ConstantesBD.continuarTransaccion, centroCostoPrincipal, controlEspecial ,entidadSubcontratada);
	 	return resultado;
	}
	
	/**
	 * Mï¿½todo para ingresar unicamente el detalle de la solicitud sin Justificacion(),
	 * y con los datos requeridos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public int insertarUnicamenteDetalleSolicitudMedicamentos(Connection con, int numeroSolicitud, int codigoArticulo, String diasTratamiento, int cantidadDespachadaArticulo)
	{
		//campo 7 = temp ke corresponde a la frecuencia
	    int temp=0;
		int resultado=solicitudMedicamentosDao.ingresarDetalleSolicitudTransaccional(con, numeroSolicitud,codigoArticulo, "", "","", temp, "", "", cantidadDespachadaArticulo, ConstantesBD.continuarTransaccion, diasTratamiento);
		return resultado;
	}

	/**
	 * Mï¿½todo para ingresar unicamente el detalle de la solicitud sin Justificacion(),
	 * y con los datos requeridos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	/*
	public int insertarUnicamenteAtributoSolicitudMedicamentos(Connection con, int numeroSolicitud, int codigoArticulo, int atributo, String descripcion)
	{
	    int resultado=solicitudMedicamentosDao.ingresarAtributoTransaccional(con, numeroSolicitud, codigoArticulo, atributo, descripcion, ConstantesBD.continuarTransaccion);
		return resultado;
	}
	*/
	/**
	 * Mï¿½todo para modificar la solicitud de medicamentos
	 * @param con Connecciï¿½n con la BD
	 * @param logModificados
	 * @param forma 
	 * @return Mayor que 0 si fue correcta la inserciï¿½n de la solicitud
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public int modificarSolicitudMedicamentosYSolSubcuenta(Connection con, ObjetoReferencia logModificados, int codigoInstitucion, UsuarioBasico usuario, PersonaBasica paciente, SolicitudMedicamentosForm forma,HttpServletRequest request)
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    FormatoJustArtNopos fjan=new FormatoJustArtNopos();
	    
		try
		{
			/*if(getCentroCostoSolicitado().getCodigo()==ConstantesBD.codigoCentroCostoExternos)
			{
				setNumeroAutorizacion("");
			}*/
			//if(this.actualizarNumeroAutorizacionTransaccional(con, getNumeroAutorizacion(), getNumeroSolicitud(), ConstantesBD.inicioTransaccion).isTrue())
			//{
			//Se abre una sola vez para que haga commit solo al final de la modificacion
			HibernateUtil.beginTransaction();
			
			/*
			 * Se guardan las coberturas encontradas para los diferentes articulos
			 * MT 5880 para ayudar a la MT 6112
			 * */
			List<InfoResponsableCobertura>responsablesCobertura=new ArrayList<InfoResponsableCobertura>(articulos.size());
			
			for(int i=0;i<articulos.size();i++){
				responsablesCobertura.add(null);
			}
			
				if(this.cambiarCentroCostoSolicitado(con, getCentroCostoSolicitado().getCodigo()).isTrue())
				{
					this.actualizarFechaHoraTransaccional(con, getNumeroSolicitud(), UtilidadFecha.conversionFormatoFechaABD(getFechaSolicitud()), getHoraSolicitud(), ConstantesBD.continuarTransaccion);	
					this.actualizarPrioridadUrgenteTransaccional(con, getNumeroSolicitud(), getUrgente(), ConstantesBD.continuarTransaccion);
					solicitudMedicamentosDao.modificarSolicitudMedicamentos(con, getNumeroSolicitud(), observacionesGenerales, ConstantesBD.continuarTransaccion);
					for(int i=0; i<articulos.size(); i++)
					{
						ArticuloSolicitudMedicamentos artSol=((ArticuloSolicitudMedicamentos)articulos.elementAt(i));
						if(!solicitudMedicamentosDao.existeMedicamento(con, getNumeroSolicitud(), artSol.getArticulo()))
						{
							logger.info("\n\n VA HA INSERTAR EL ARTICULO NUEVO--->"+artSol.getArticulo()+" \n");
							
							ingresarArticulo(con, artSol.getArticulo(), logModificados, true, codigoInstitucion);
							
							
//							Formato justificacion nopos
							logger.info("\n\n\n\n [ESPACIO JUSTIFICACION] \n\n\n\n");
							logger.info("\n\n\n [EsPOS] ->"+forma.getMedicamentos("es_pos_"+artSol.getArticulo()).toString());
							logger.info("\n\n\n [Articulo] ->"+artSol.getArticulo());
							
							if(forma.getMedicamentos("es_pos_"+artSol.getArticulo()).toString().equals("false"))
							{
								if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(artSol.getArticulo()+"")) )
								{
									if(forma.getJustificacionMap().get(artSol.getArticulo()+"_pendiente").equals("1"))
									{
										logger.info("\n\n [Inicia la insercion justificacion no pos articulos]\n\n");

										
										//Mt 4568: Insercion asocio justificacion modificacion solicitudes
										HashMap mapaAsocioJustArt=(HashMap)request.getSession().getAttribute("MAPASOCIOJUSTIFICACION");
										if(mapaAsocioJustArt !=null 
												&&forma.getJustificacionMap().containsKey(artSol.getArticulo()+"_sevaasociar")
												&&mapaAsocioJustArt.get(artSol.getArticulo()+"_codigoAsocio")!=null
												&&!UtilidadTexto.isEmpty(mapaAsocioJustArt.get(artSol.getArticulo()+"_codigoAsocio").toString())){
											UtilidadesJustificacionNoPos.insertarAsocioSolicitudJustificacion(con, this.getNumeroSolicitud(), Utilidades.convertirAEntero(mapaAsocioJustArt.get(artSol.getArticulo()+"_codigoAsocio").toString()),artSol.getCantidadSolicitada());
										
										}else{
												
										//frecuencia estaba en int
										
										HashMap mapaJustArt=(HashMap)request.getSession().getAttribute(artSol.getArticulo()+"MAPAJUSART");
										if(mapaJustArt==null){
											mapaJustArt=forma.getJustificacionMap();
										}
										if(forma.getJustificacionMap()!=null){
											Set<String>keys=forma.getJustificacionMap().keySet();
											for(String key:keys){
												if(!mapaJustArt.containsKey(key)){
													mapaJustArt.put(key,forma.getJustificacionMap().get(key));
												}
											}
										}
										
										mapaJustArt.put(artSol.getArticulo()+"_mapasecciones",request.getSession().getAttribute("MAPASECJUSART"));
										
										HashMap mapaMedNoPos=(HashMap) request.getSession().getAttribute(artSol.getArticulo()+"MAPA_MED_NO_POS");
										HashMap mapaMedPos=(HashMap) request.getSession().getAttribute(artSol.getArticulo()+"MAPA_MED_POS");
										HashMap mapaSusNoPos=(HashMap) request.getSession().getAttribute(artSol.getArticulo()+"MAPA_SUS_NO_POS");
										HashMap mapaDiagDef=(HashMap) request.getSession().getAttribute(artSol.getArticulo()+"MAPA_DIAG_DEF");
										HashMap mapaDiagPresuntivos=(HashMap) request.getSession().getAttribute(artSol.getArticulo()+"MAPA_DIAG_PRESUNTIVOS");
										
										int y=fjan.insertarJustificacion(con,
																	getNumeroSolicitud(),
																	ConstantesBD.codigoNuncaValido,
																	mapaJustArt,
																	forma.getMedicamentosNoPosMap(),
																	forma.getMedicamentosPosMap(),
																	forma.getSustitutosNoPosMap(),
																	forma.getDiagnosticosDefinitivos(),
																	artSol.getArticulo(),
																	codigoInstitucion, 
																	observacionesGenerales, 
																	ConstantesBD.continuarTransaccion,
																	artSol.getArticulo(), 
																	artSol.getObservaciones(), 
																	artSol.getDosis(), 
																	artSol.getUnidosis(), 
																	Integer.parseInt(artSol.getFrecuencia()+""), 
																	artSol.getTipoFrecuencia(), 
																	artSol.getVia(), 
																	artSol.getCantidadSolicitada(), 
																	artSol.getDiasTratamiento(),
																	usuario.getLoginUsuario()
																	);
										logger.info("\n\n\n [TERMINA INSERCION JUS NO POS]"+y+"\n\n\n");
										
										if (y<0)
										{
										logger.info("\n\n\n [ABORTAR TRANSACCION ERROR INSERTANDO JUSTIFICACION NO POS] \n\n\n");
										myFactory.abortTransaction(con);
										}
									}	
								}
							}
							else
								logger.info("\n\n\n [NO NECESITA JUSTIFICACION NO POS SEGUN VALIDACION CONVENIO] Convenio->"+paciente.getCodigoConvenio()+" - "+Utilidades.obtenerNombreConvenioOriginal(con, paciente.getCodigoConvenio())+" \n\n\n");
						}	
							
					logger.info("\n\n\n [Termina Proceso justificacion no pos] \n\n\n");		
//		------------------------	
							
							//se ingresan las solicitudes de sub cuenta y el cargo pendiente por articulo
							Cargos cargoArticulos= new Cargos();
			        	    boolean generoCargo=	cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(		con, 
			        	    																										usuario, 
										        	    																			paciente, 
										        	    																			this.getNumeroSolicitud(), 
										        	    																			((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo(), 
										        	    																			((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getCantidadSolicitada() /*cantidadArticulo*/, 
										        	    																			true /*dejarPendiente*/, 
										        	    																			ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
										        	    																			ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
										        	    																			this.getCentroCostoSolicitado().getCodigo(), 
										        	    																			ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
										        	    																			Utilidades.esSolicitudPYP(con, getNumeroSolicitud()),this.getFechaSolicitud(),false /*tarifaNoModificada*/);
			        	    responsablesCobertura.set(i, cargoArticulos.getInfoResponsableCoberturaGeneral());
			        	    
			        	    if(!generoCargo)
			        		{
			        			logger.warn("Error generando el cargo de la solicitud= "+ this.getNumeroSolicitud() +" y articulo ->"+((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo());
			        			myFactory.abortTransaction(con);
			        			return 0;
			        		}
						}
						else
						{
							modificarArticuloYSolSubCuenta(con, artSol.getArticulo(), logModificados, codigoInstitucion);
							
							/*
							 * MT 5880, debe evaluar la cobertura para todos los articulos 
							 * segun DCU 437 v1.3 - PROCESO GENERAL, DE LA VALIDACIÓN DE COBERTURA DE SERVICIOS ARTÍCULOS
							 * incluyendo los que existen desde la creacion de la solicitud como los que se agregan en la modificacion
							 * 
							 * jeilones
							 */
							
							Cargos cargoArticulos= new Cargos();
							cargoArticulos.modificarCoberturaArticulosXConvenioSolicitud(con, 
									usuario, 
									paciente, 
									this.getNumeroSolicitud(),
									ConstantesBD.codigoNuncaValido,
									artSol.getArticulo(), 
									ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
									ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
									Utilidades.esSolicitudPYP(con, getNumeroSolicitud()));
							
							responsablesCobertura.set(i, cargoArticulos.getInfoResponsableCoberturaGeneral());
						}
					}
				}
				else return 0;
			//}
			forma.setInfoCoberturaArticulo((ArrayList<InfoResponsableCobertura>)responsablesCobertura);
			HibernateUtil.endTransaction();
			myFactory.endTransaction(con);
		}
		catch (Exception e)
		{
			logger.error("Error insertando la solicitud general: "+e);
			try
			{
				myFactory.abortTransaction(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error abortando la transacciï¿½n : "+e1);
				return 0;
			}
			HibernateUtil.abortTransaction();
			return 0;
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param numersoSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean existeMedicamentoEnSolicitud( Connection con, int numersoSolicitud, int codigoArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosDao().existeMedicamento(con, numersoSolicitud, codigoArticulo);
	}
	
	
	/**
	 * Mï¿½todo para ingresar los articulos uno por uno
	 * @param codigoArticulo
	 * @param esModificacion
	 * @param logModificados
	 * @param modificado
	 */
	private void ingresarArticulo(Connection con, int codigoArticulo, ObjetoReferencia logInsertados, boolean modificado, int codInstitucion)
	{
		ArticuloSolicitudMedicamentos articulo=getArticuloSolicitud(codigoArticulo);
		if(modificado && logInsertados!=null)
		{
			String log=logInsertados.getStringReferencia() +
			"\n\n*  --- ARTï¿½CULO ADICIONADO [" + codigoArticulo +"] ---" ;
			logInsertados.setStringReferencia(llenarLog(articulo, log, codInstitucion));
		}
		solicitudMedicamentosDao.ingresarDetalleSolicitudTransaccional(con, getNumeroSolicitud(), articulo.getArticulo(), articulo.getObservaciones(), articulo.getDosis(), articulo.getUnidosis(), articulo.getFrecuencia(), articulo.getTipoFrecuencia(), articulo.getVia(), articulo.getCantidadSolicitada(), ConstantesBD.continuarTransaccion, articulo.getDiasTratamiento());
		/*
		if(!articulo.getEsPos().equals("t"))
		{
			int[] codigosJustificacion=Utilidades.buscarCodigosJustificaciones(codInstitucion, false, true);
			for(int i1=0; i1<codigosJustificacion.length;i1++)
			if(articulo.getJustificacion(codigosJustificacion[i1]+"")!=null)
			{
				if(!((String)articulo.getJustificacion(codigosJustificacion[i1]+"")).equals("") && !((String)articulo.getJustificacion(codigosJustificacion[i1]+"")).equals("null"))
				{
					solicitudMedicamentosDao.ingresarAtributoTransaccional(con, getNumeroSolicitud(), articulo.getArticulo(), codigosJustificacion[i1], (String)articulo.getJustificacion(codigosJustificacion[i1]+""), ConstantesBD.continuarTransaccion);
				}
			}
		}
		*/
	}

	/**
	 * Mï¿½todo para modificar los articulos uno por uno
	 * @param con
	 * @param codigoArticulo
	 * @param logModificados
	 */
	@SuppressWarnings("rawtypes")
	private void modificarArticuloYSolSubCuenta(Connection con, int codigoArticulo, ObjetoReferencia logModificados, int codigoInstitucion)
	{
		String log=logModificados.getStringReferencia()+"\n\n*  --- ARTï¿½CULO MODIFICADO [" + codigoArticulo +"] ---" ;
		ArticuloSolicitudMedicamentos articulo=getArticuloSolicitud(codigoArticulo);
		Articulo articuloBD=new Articulo();
		articuloBD.cargarArticulo(con, codigoArticulo);
		if(articulo.getModificado())
		{
			logModificados.setStringReferencia(llenarLog(articulo, log, codigoInstitucion));
		}
		
		logger.info("ENTRA A MODIFICAR ARTICULO---> articulo.getArticulo()->"+articulo.getArticulo()+" articulo.getCantidadSolicitada()->"+articulo.getCantidadSolicitada());
		
		solicitudMedicamentosDao.modificarDetalleSolicitudTransaccional(con, getNumeroSolicitud(), articulo.getArticulo(), articulo.getObservaciones(), articulo.getDosis(), articulo.getFrecuencia(), articulo.getTipoFrecuencia(), articulo.getVia(), articulo.getCantidadSolicitada(), ConstantesBD.continuarTransaccion, articulo.getUnidosis(), articulo.getDiasTratamiento());
		
		//logger.info("\n VA HA ACTUALIZAR LAS CANTIDADES DEL ARTICULO ->"+articulo.getArticulo()+" cantidad->"+articulo.getCantidadSolicitada());
		//en este punto solo debe existir un responsable entonces no es requerido enviar el codigo del convenio
		//YA NO ES NECESARIO POSRQUE SOLO DEBE ACTUALIZAR ES LA CANTIDAD CARGADA (despacho- administracion)
		//Cargos.updateCantidadesCargo(con, articulo.getCantidadSolicitada(), getNumeroSolicitud(), ""/*codigoconvenio*/, articulo.getArticulo(), false);
		
		Vector codigosNombresJustificacion=Utilidades.buscarCodigosNombresJustificaciones(codigoInstitucion, false, true);
		if(!articulo.getEsPos().equals("t"))
		{
			for(int i=0; i<codigosNombresJustificacion.size(); i++)
			{
				Vector atributo=(Vector)codigosNombresJustificacion.elementAt(i);
				String codigoAtributo=atributo.elementAt(0)+"";
				String tempoJustificacion=(String)articulo.getJustificacion(codigoAtributo);
				if(tempoJustificacion!=null)
				{
					if(!tempoJustificacion.equals("") && !tempoJustificacion.equals("null"))
					{
						solicitudMedicamentosDao.modificarAtributoTransaccional(con, getNumeroSolicitud(), articulo.getArticulo(), ((Integer)atributo.elementAt(0)).intValue(), tempoJustificacion, ConstantesBD.continuarTransaccion);
					}
					else
					{
						/*
						 * Si el elemento en la posiciï¿½n 2 es null o lanza la excepcion,
						 * quiere decir que el elemento no es requerido
						 * Los elementos requeridos no deben ser borrados de la BD
						 * unicamnete pueden ser modificados
						 */
						try
						{
							if(atributo.elementAt(2)!=null)
							{
								solicitudMedicamentosDao.borrarAtributoTransaccional(con, getNumeroSolicitud(), articulo.getArticulo(), ((Integer)atributo.elementAt(0)).intValue(), ConstantesBD.continuarTransaccion);
							}
						}
						catch(ArrayIndexOutOfBoundsException e)
						{
							solicitudMedicamentosDao.borrarAtributoTransaccional(con, getNumeroSolicitud(), articulo.getArticulo(), ((Integer)atributo.elementAt(0)).intValue(), ConstantesBD.continuarTransaccion);
						}
					}
				}
			}
		}
	}

	
	/**
	 * Mï¿½todo para llenar el log de modificaciï¿½n
	 * @param articulo
	 * @param log
	 */
	@SuppressWarnings("rawtypes")
	private String llenarLog(ArticuloSolicitudMedicamentos articulo, String log, int codigoInstitucion)
	{
		if(articulo.getModificado())
		{
			log+=articulo.getLogModificacion()+"\n	--- MODIFICACIï¿½N ---";
			log+="\n*  Dosis			" + articulo.getDosis();
			log+="\n*  Via				" + articulo.getVia();
			log+="\n*  Frecuencia			" + articulo.getFrecuencia();
			log+="\n*  Tipo Frecuencia		" + articulo.getTipoFrecuencia();
			log+="\n*  Cantidad Sol.		" + articulo.getCantidadSolicitada();
			log+="\n*  Observaciones		" + articulo.getObservaciones();
	
			if(articulo.getEsPos().equals("f"))
			{
				Vector codigosNombresJustificacion=Utilidades.buscarCodigosNombresJustificaciones(codigoInstitucion, false, true);
				for(int i=0; i<codigosNombresJustificacion.size(); i++)
				{
					Vector atributo=(Vector)codigosNombresJustificacion.elementAt(i);
					String tempoJustificacion=articulo.getJustificacion(atributo.elementAt(0)+"");
					if(tempoJustificacion!=null)
					{
						if(!tempoJustificacion.equals("") && !tempoJustificacion.equals("null"))
						{
							log+=
								"\n*\t"+atributo.elementAt(1)+"\t\t" + tempoJustificacion;
						}
					}
				}
			}
		}
		return log;
	}

	/**
	 * @return Retorna observacionesGenerales.
	 */
	public String getObservacionesGenerales()
	{
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales Asigna observacionesGenerales.
	 */
	public void setObservacionesGenerales(String observacionesGenerales)
	{
		this.observacionesGenerales = observacionesGenerales;
	}
	
	/**
	 * Adicionar la justificaciï¿½n de un articulo
	 * @param justificacion
	 */
	@SuppressWarnings("unchecked")
	public void adicionarArticuloSolicitud(ArticuloSolicitudMedicamentos articulo)
	{
		articulos.add(articulo);
	}
	
	/**
	 * Obtener un medicamento especï¿½fico de la solicitud
	 * @param codigoArticulo
	 * @return Objeto <code>ArticuloSolicitudMedicamentos</code> correspondiente
	 * al cï¿½digo que recibe como parï¿½metro, null si no encuentra el articulo
	 * al cual le corresponde el codigo
	 */
	public ArticuloSolicitudMedicamentos getArticuloSolicitud(int codigoArticulo)
	{
		for(int i=0; i<articulos.size(); i++)
		{
			ArticuloSolicitudMedicamentos tempo=(ArticuloSolicitudMedicamentos)articulos.elementAt(i);
			if(tempo.getArticulo()==codigoArticulo)
				return tempo;
		}
		return null;
	}
	
	/**
	 * Constructor que recibe otra solicitud de medicamentos
	 * @param solicitud
	 */
	public SolicitudMedicamentos(SolicitudMedicamentos solicitud)
	{
		this();
		try
		{
			PropertyUtils.copyProperties(this, solicitud);
		}
		catch (Exception e)
		{
			logger.error("Error Copiando el mundo"+e);
		}
	}
	
	/**
	 * Constructor Vacï¿½o
	 */
	@SuppressWarnings("rawtypes")
	public SolicitudMedicamentos()
	{
		String tipoBD=System.getProperty("TIPOBD");
		super.clean();
		super.init(tipoBD);
		init(tipoBD);
		observacionesGenerales="";
		articulos=new Vector();
	}
	
	/**
	 * Mï¿½todo para cargar la solicitud de Medicamentos
	 * @param con Conecciï¿½n con la BD
	 * @return true si la solicitud se cargï¿½ correctamente
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean cargarSolicitudMedicamentos(Connection con, int codigoInstitucion)
	{
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexiï¿½n (SolicitudMedicamentos)"+e1.toString());
		}
		
		logger.info("NUM SOLICITUD 2-->"+getNumeroSolicitud());
		logger.info("NUM SOLICITUD 3-->"+getNumeroSolicitud());
		try
		{
			this.cargar(con, getNumeroSolicitud());
		}
		catch (SQLException e)
		{
			logger.error("Problemas cargando la solicitud general nï¿½mero: "+getNumeroSolicitud()+": "+e);
			return false;
		}
		logger.info("NUM SOLICITUD 4-->"+getNumeroSolicitud());
		try
		{
			Collection solicitudMedicamentos=solicitudMedicamentosDao.consultarSolicitudMedicamentos(con, getNumeroSolicitud());
			if(solicitudMedicamentos.isEmpty())
			{
				logger.error("No se cargo la solicitud de medicamentos nï¿½mero: "+getNumeroSolicitud()+", probablemente no existe o hay inconsistencias en la BD");
				return false;
			}

			Iterator iterador=solicitudMedicamentos.iterator();
			while(iterador.hasNext())
			{
				HashMap fila=(HashMap)iterador.next();
				this.observacionesGenerales=fila.get("observacionesgenerales")+"";
				this.controlEspecial=fila.get("controlespecial")+"";
			}

			solicitudMedicamentos=solicitudMedicamentosDao.consultarDetalleSolicitudArticulos(con, getNumeroSolicitud(), codigoInstitucion);
			if(solicitudMedicamentos.isEmpty())
			{
				logger.error("No se cargo el detalle de la solicitud de medicamentos nï¿½mero: "+getNumeroSolicitud()+", probablemente no existe o hay inconsistencias en la BD");
				return false;
			}
			else
			{
				iterador=solicitudMedicamentos.iterator();
				this.articulos=new Vector();
				while(iterador.hasNext())
				{
					HashMap fila=(HashMap)iterador.next();
					ArticuloSolicitudMedicamentos articulo=new ArticuloSolicitudMedicamentos();
					articulo.setArticulo(Integer.parseInt(fila.get("articulo")+""));
					String tempo;
					
					int[]codigosInstituciones=Utilidades.buscarCodigosJustificaciones(con, codigoInstitucion, false, true);
					for(int i=0; i<codigosInstituciones.length;i++)
					{
						tempo=(fila.get("just"+codigosInstituciones[i])==null)?"":(fila.get("just"+codigosInstituciones[i])+"");
						articulo.setJustificacion(codigosInstituciones[i]+"", tempo.equals("null")?"":tempo);
					}
					tempo=(fila.get("observaciones")==null)?"":(fila.get("observaciones")+"");
					articulo.setObservaciones(tempo.equals("null")?"":tempo);
					articulo.setDosis(fila.get("dosis")+"");
					articulo.setUnidosis(fila.get("unidosis")+"");
					articulo.setFrecuencia(Integer.parseInt(fila.get("frecuencia")+""));
					//articulo.setFrecuencia(fila.get("frecuencia")+"");
					articulo.setTipoFrecuencia(fila.get("tipofrecuencia")+"");
					articulo.setVia(fila.get("via")+"");
					
					if(UtilidadTexto.isEmpty(fila.get("cantidad")+"")){
						articulo.setCantidadSolicitada(0);
					}else{
						articulo.setCantidadSolicitada(Integer.parseInt(fila.get("cantidad")+"")); 	
					}
					
					articulo.setDiasTratamiento(fila.get("diastratamiento").toString());
					tempo=fila.get("espos")+"";
					articulo.setEsPos(tempo.equals("null")?"":tempo);
					//tempo=(fila.get("despachototal")==null)?"0":(fila.get("despachototal")+"");
					articulo.setDespachoTotal(Utilidades.convertirAEntero(fila.get("despachototal")+"")>0?Utilidades.convertirAEntero(fila.get("despachototal")+""):0);
					tempo=String.valueOf(fila.get("artprincipal"));
					boolean temp=UtilidadTexto.isEmpty(tempo)?false:true;
					articulo.setEsSustituto(temp);
					if(temp)
							articulo.setArtPrincipal(Integer.parseInt(tempo));
					tempo=String.valueOf(fila.get("essuspendido"));
					if(tempo.equals("true") || tempo.equals("t") || tempo.equals("1"))
					{
						articulo.setSuspendido(true);
					}
					tempo=String.valueOf(fila.get("motivosuspension"));
					articulo.setMotivoSuspension(tempo.equals("null")?"":tempo);
					tempo=(fila.get("totalpaciente")==null)?"":(fila.get("totalpaciente")+"");
					articulo.setTotalDespachadoPaciente(tempo.equals("null")?"":tempo);
					tempo=(fila.get("totalfarmacia")==null)?"":(fila.get("totalfarmacia")+"");
					articulo.setTotalDespachadoFarmacia(tempo.equals("null")?"":tempo);
					
					//hermorhu - MT6628
					articulo.setEsMedicamento(fila.get("esmedicamento").toString());
					
					if(	(UtilidadTexto.isEmpty(fila.get("cantidad")+"")) 
						&& (UtilidadTexto.isEmpty(fila.get("unidosis")+""))	){
						// En este caso no se tiene en cuenta el articulo para mostrarlo
					}else{
						this.articulos.add(articulo);
					}
				}
			}
			return true;
		}
		catch(Exception e)
		{
			logger.error("Problemas cargando el detalle de la solicitud de medicamentos nï¿½mero: "+getNumeroSolicitud()+": "+e);
			return false;
		}
	}
	
	/**
	 * Mï¿½todo que carga los insumos retornando una coleccion
	 * @param con Connecciï¿½n con la BD 
	 * @param numeroSolicitud Numero de solicitud a la cual pertenecen los insumos
	 * @return Collection con la lista de los insumos de la solicitud
	 */
	@SuppressWarnings("rawtypes")
	public Collection cargarInsumos(Connection con)
	{
		return solicitudMedicamentosDao.consultarInsumos(con, getNumeroSolicitud());
	}
	
	/**
	 * Metodo la consulta de los insumos pedidos por
	 * la farmacia.
	 * @param con, Connection con la fuente de datos. 
	 * @see com.princetonsa.dao.sqlbase.SqlBaseSolicitudMedicamentosDao#cargaInsumosFarmacia(java.sql.Connection)
	 */
	@SuppressWarnings("rawtypes")
	public Collection cargaInsumosFarmacia (Connection con, int numeroSolicitud)
	{
	    solicitudMedicamentosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosDao();
		Collection col= null;
		try 
		    {
		      col = solicitudMedicamentosDao.consultarInsumosFarmacia(con, numeroSolicitud);
			} 
		    catch (Exception e) 
		    {
				logger.warn("Error cargaInsumosFarmacia [ mundo SolicitudMedicamentos ]->"+e.toString());
				col = null;
			}
		    
		return col;
	}

	
	/**
	 * 
	 * @param con Conecciï¿½n con la BD
	 * @param codigosEliminados String con los codigos eliminados separados por "-"
	 * @param logEliminados Llenar el log de los elementos eliminados
	 * @param codigoInstitucion Cï¿½digo de la institucion del usuario
	 * @return numero de elementos eliminados
	 */
	public int eliminarArticulosYSolSubCuenta(Connection con, String codigosEliminados, ObjetoReferencia logEliminados, int codigoInstitucion) throws IPSException
	{
		if(codigosEliminados!=null)
		{
			String[] codigosEliminadosStr=codigosEliminados.split("-");
			int codigosEliminadosInt[]=new int[codigosEliminadosStr.length];
			for(int i=0;i<codigosEliminadosStr.length;i++)
			{
				codigosEliminadosInt[i]=Integer.parseInt(codigosEliminadosStr[i]);
			}
			
			logger.info("\n\n\n\n\n\n [eliminarArticulosYSolSubCuenta] Tamaï¿½o ->"+codigosEliminadosInt.length+" \n\n\n\n\n\n ");
			logger.info("\n\n\n\n\n\n [eliminarArticulosYSolSubCuenta] Valor Ultima Posicion ->"+codigosEliminadosInt[codigosEliminadosInt.length-1]+" \n\n\n\n\n\n ");
			eliminarArticulosYSolSubCuentas(con, codigosEliminadosInt, logEliminados, codigoInstitucion);
			
			return 0;
		}
		return 0;
	}
	
	/**
	 * Metodo para eliminar artï¿½culos de una solicitud
	 * @param con conecciï¿½n con la base de datos
	 * @param codigosArticulosAEliminar Array con los cï¿½digos de los articulos
	 * de la solicitud que se desean eliminar 
	 * @param codigoInstitucion Codigo de la institucion del usuario
	 * @return el numero de elementos eliminados
	 */
	@SuppressWarnings("rawtypes")
	public int eliminarArticulosYSolSubCuentas(Connection con, int codigosArticulosAEliminar[], ObjetoReferencia log, int codigoInstitucion) throws IPSException
	{
		String logEliminados=log.getStringReferencia();
		int eliminados=0;
		if(codigosArticulosAEliminar.length>0)
		{
			for(int i=0; i<codigosArticulosAEliminar.length;i++)
			{
				if(solicitudMedicamentosDao.existeMedicamento(con, getNumeroSolicitud(), codigosArticulosAEliminar[i]))
				{
				DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta= new DtoSolicitudesSubCuenta();
				dtoSolicitudesSubCuenta.setArticulo(new InfoDatosString(codigosArticulosAEliminar[i]+""));
				dtoSolicitudesSubCuenta.setNumeroSolicitud(getNumeroSolicitud()+"");
				
				//en este punto no puede estar distribuido entonces solo debe existir un responsable  y no es requerida la subcuenta
				double codigoDetalleCargox= Cargos.obtenerCodigoDetalleCargoXArticulo(con, "" /*subcuenta*/, getNumeroSolicitud(), codigosArticulosAEliminar[i], ConstantesBD.acronimoNo/*facturado*/);
				
				if(solicitudMedicamentosDao.eliminarArticulo(con, getNumeroSolicitud(), codigosArticulosAEliminar[i])>0 
					&& Cargos.eliminarDetalleCargoYErroresYArtConsumoXCodigoDetalleCargo(con, codigoDetalleCargox)
					&& Solicitud.eliminarSolicitudSubCuenta(con, dtoSolicitudesSubCuenta))
				{
					Articulo articulo=new Articulo();
					articulo.cargarArticulo(con, codigosArticulosAEliminar[i]);
					ArticuloSolicitudMedicamentos articuloSolicitud=this.getArticuloSolicitud(codigosArticulosAEliminar[i]);
					logEliminados+= "\n\n*  --- ARTï¿½CULO ELIMINADO [" + codigosArticulosAEliminar[i] +"] ---" +
									"\n*  Nombre Artï¿½culo		" + articulo.getDescripcion() +
									"\n*  Concentraciï¿½n		" + articulo.getConcentracion() +
									"\n*  Forma Farmacï¿½utica		" + articulo.getNomFormaFarmaceutica() +
									"\n*  Unidad de Medida		" + articulo.getNomUnidadMedida() +
									"\n*  Naturaleza			" + articulo.getNomNaturaleza() ;
					
					if(!UtilidadTexto.getBoolean(articulo.getEsPos()))
					{
						Vector codigosNombresJustificacion=Utilidades.buscarCodigosNombresJustificaciones(codigoInstitucion, false, true);
						for(int i1=0;i1<codigosNombresJustificacion.size();i1++)
						{
							Vector atributo=(Vector)codigosNombresJustificacion.elementAt(i1);
							try
							{
								String atributoJustificacion=articuloSolicitud.getJustificacion(atributo.elementAt(0)+"")+"";
								if(!atributoJustificacion.equals("") && !atributoJustificacion.equals("null"))
								{
									logEliminados+=
											"\n*\t\t" + atributo.elementAt(1)+"\t\t"+atributoJustificacion;
								}
							}
							catch(Exception e)
							{
								logger.error("No inserto los atributos de Justificacion ");
							}
						}
					}
					try
					{
						if(!articuloSolicitud.getObservaciones().equals(""))
							logEliminados+= "\n*  Observaciones		" + articuloSolicitud.getObservaciones();
					}
					catch(Exception e)
					{
						logger.error("No inserto las observaciones de Justificacion ");
					}
					log.setStringReferencia(logEliminados);
					eliminados++;
				}
				else
				{
					logger.error("No se eliminï¿½ el articulo "+codigosArticulosAEliminar[i]);
				}
			}
			}
		}
		else
		{
			return eliminados;
		}
		return 0;
	}
	
	/**
	 * Mï¿½todo para suspender artï¿½culos de una solicitud
	 * @param con Conecciï¿½n con la base de datos
	 * @param codigosArticulosASuspender Vector con los cï¿½digos y
	 * motivos de suspensiï¿½n de los artï¿½culos a suspender
	 * separados por la cadena "@@@@" --> codigo@@@@motivo
	 * @param codigoMedico Cï¿½digo del mï¿½dico que realizï¿½ la suspensiï¿½n
	 * @return boolean con el resultado de la inserciï¿½n en la BD de
	 * los artï¿½culos suspendidos
	 */
	public boolean suspenderMedicamento(Connection con, String codigosArticulosASuspender[], int codigoMedico, ObjetoReferencia log)
	{
		String logSuspendidos=log.getStringReferencia();
		boolean resultado=true;
		for(int i=0; i<codigosArticulosASuspender.length; i++)
		{
			String[] tempo=codigosArticulosASuspender[i].split("@@@@");
			if(!solicitudMedicamentosDao.suspenderMedicamento(con, getNumeroSolicitud(), Integer.parseInt(tempo[0]), tempo[1], codigoMedico))
			{
				resultado=false;
			}
			else
			{
				Articulo articulo=new Articulo();
				articulo.cargarArticulo(con, Integer.parseInt(tempo[0]));
				logSuspendidos+= "\n\n*  --- ARTï¿½CULO SUSPENDIDO [" + tempo[0] +"] ---" +
				"\n*  Nombre Artï¿½culo		" + articulo.getDescripcion() +" " +
				"\n*  Motivo Suspensiï¿½n		" + tempo[2] +" " ;
			}
		}
		log.setStringReferencia(logSuspendidos);
		return resultado;
	}

	/**
	 * Mï¿½todo para suspender artï¿½culos de una solicitud
	 * @param con Conecciï¿½n con la base de datos
	 * @param codigosArticulosASuspender Vector con los cï¿½digos y
	 * motivos de suspensiï¿½n de los artï¿½culos a suspender
	 * separados por la cadena "@@@@" --> codigo@@@@motivo
	 * @param codigoMedico Cï¿½digo del mï¿½dico que realizï¿½ la suspensiï¿½n
	 * @return boolean con el resultado de la inserciï¿½n en la BD de
	 * los artï¿½culos suspendidos
	 */
	public boolean modificarMotivosSuspension(Connection con, String codigosArticulosAModificarMotivo[])
	{
		boolean resultado=true;
		for(int i=0; i<codigosArticulosAModificarMotivo.length; i++)
		{
			String[] tempo=codigosArticulosAModificarMotivo[i].split("@@@@");
			if(!solicitudMedicamentosDao.modificarMotivoSuspension(con, getNumeroSolicitud(), Integer.parseInt(tempo[0]), tempo[1]))
			{
				resultado=false;
			}
		}
		return resultado;
	}

	/**
	 * Metodo para consultar todas las solicitudes de medicamentos que se han
	 * realizado para un paciente espec&iacute;fico.
	 * @param con, conexion a la BD.
	 * @param cuenta, codigo de la cuenta del paciente que se desea consultar (Si esta llega en 0 no se hace el filtrado)
	 * @param centroCosto Centro de costo del paciente (Si este llega en 0 no se hace el filtrado)
	 * @return Collection, Resultado de la consulta, todas las solicitudes del pacientes.
	 */
	@SuppressWarnings("rawtypes")
	public Collection consultarSolicitudesMedicamento(Connection con, int cuenta, String fechaInicialFiltro, String fechaFinalFiltro, int areaFiltro, int pisoFiltro, int habitacionFiltro, int camaFiltro)
	{
		HashMap<String, Object> criteriosBusquedaMap= new HashMap<String, Object>();
		criteriosBusquedaMap.put("cuenta", cuenta);
		criteriosBusquedaMap.put("fechaInicialFiltro", fechaInicialFiltro);
		criteriosBusquedaMap.put("fechaFinalFiltro", fechaFinalFiltro);
		criteriosBusquedaMap.put("areaFiltro", areaFiltro);
		criteriosBusquedaMap.put("pisoFiltro", pisoFiltro);
		criteriosBusquedaMap.put("habitacionFiltro", habitacionFiltro);
		criteriosBusquedaMap.put("camaFiltro", camaFiltro);
		return solicitudMedicamentosDao.consultarSolicitudesMedicamento(con,criteriosBusquedaMap);
	}
	
	/**
	 * Metodo para consultar todas las solicitudes de medicamentos que se han
	 * realizado para un paciente espec&iacute;fico, y que tienen administracion.
	 * @param con, conexion a la BD.
	 * @param cuenta, codigo de la cuenta del paciente que se desea consultar
	 * @return Collection, Resultado de la consulta, todas las solicitudes del pacientes.
	 */
	@SuppressWarnings("rawtypes")
	public Collection consultarSolicitudesMedicamentoConAdministracion(Connection con, int cuenta)
	{
		return solicitudMedicamentosDao.consultarSolicitudesMedicamentoConAdministracion(con,cuenta);
	}
	/**
	 * @return Retorna el articulos.
	 */
	@SuppressWarnings("rawtypes")
	public Vector getArticulos()
	{
		return articulos;
	}
	/**
	 * @param articulos Asigna el articulos.
	 */
	@SuppressWarnings("rawtypes")
	public void setArticulos(Vector articulos)
	{
		this.articulos = articulos;
	}
	
	/**
	 * Mï¿½todo para resetear los artï¿½culos del mundo de <code>SolicitudMedicamentos</code>
	 */
	@SuppressWarnings("rawtypes")
	public void resetMedicamentos()
	{
		observacionesGenerales="";
		articulos=new Vector();
	}

	/**
	 * Mï¿½todo para resetear completamente el mundo de <code>SolicitudMedicamentos</code>
	 */
	@SuppressWarnings("rawtypes")
	public void resetCompleto()
	{
		super.clean();
		observacionesGenerales="";
		articulos=new Vector();
	}

	/**
	 * Mï¿½todo para actualizar cantidades solicitadas
	 * de una solicitud de medicamentos
	 * @param con Conexiï¿½n con la BD
	 * @param cantidades Vector con elementos tipo Vector
	 * en la posiciï¿½n (0) debe estar el cï¿½digo del articulo
	 * a modificar y en pa posiciï¿½n (1) la cantidad ingresada 
	 * @param numeroSolicitud Solicitud a modificar
	 * @return int con el nï¿½mero de elementos actualizados
	 * -1 en caso de algï¿½n error
	 */
	@SuppressWarnings("rawtypes")
	public int actualizarCantidades(Connection con, Vector cantidades, int numeroSolicitud)
	{
		int contadorRegistros=0;
		for(int i=0;i<cantidades.size();i++)
		{
			Vector tempo=(Vector)cantidades.elementAt(i);
			int codigoArticulo=((Integer)tempo.elementAt(0)).intValue();
			int cantidad=((Integer)tempo.elementAt(1)).intValue();
			if(solicitudMedicamentosDao.actualizarCantidades(con, codigoArticulo, cantidad, numeroSolicitud)<0)
			{
				return -1;
			}
			else
			{
				contadorRegistros++;
			}
		}
		return contadorRegistros;
	}

	/**
	 * Listado de los medicamentos de todas las solicitudes del paciente
	 * @param con
	 * @param cuentaPaciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Collection consultarListadoMedicamentosPaciente(Connection con, int cuentaPaciente)
	{
		return solicitudMedicamentosDao.consultarListadoMedicamentosPaciente(con, cuentaPaciente);
	}

	public int conveniosPlanEspecial(Connection con, int codigoIngreso) 
	{
		return solicitudMedicamentosDao.conveniosPlanEspecial(con, codigoIngreso);
	}
	
	
	/**
	 * Consulta las solicitudes Historicas
	 * @param Connection con 
	 * @param boolean esUltimaSolicitud
	 * @param String codigoPaciente
	 * @param String codigoIngreso
	 * @param String codigoViaIngreso
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap consultarHistorialSolicitudes(
			Connection con,
			boolean esUltimaSolicitud,
			String codigoPaciente,
			String codigoIngreso,
			String codigoViaIngreso,
			String codigoAlmacen,
			int institucion)
	{
		HashMap parametros = new HashMap();
		
		parametros.put("esUltimaSolicitud", esUltimaSolicitud?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("codigoPaciente", codigoPaciente);
		parametros.put("codigoIngreso", codigoIngreso);
		parametros.put("codigoViaIngreso", codigoViaIngreso);		
		parametros.put("codigoAlmacen", codigoAlmacen);
		parametros.put("institucion", institucion);
		
		return solicitudMedicamentosDao.consultarHistorialSolicitudes(con, parametros);
	}

	/**
	 * @return the centroCostoPrincipal
	 */
	public String getCentroCostoPrincipal() {
		return centroCostoPrincipal;
	}

	public String getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	public void setEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}

	/**
	 * @param centroCostoPrincipal the centroCostoPrincipal to set
	 */
	public void setCentroCostoPrincipal(String centroCostoPrincipal) {
		this.centroCostoPrincipal = centroCostoPrincipal;
	}

	public String getControlEspecial() {
		return controlEspecial;
	}

	public void setControlEspecial(String controlEspecial) {
		this.controlEspecial = controlEspecial;
	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articuloPrincipal
	 * @param articuloEquivalente
	 */
	public static boolean insertarAticulosEquivalenteSolicitud(Connection con,int numeroSolicitud, int articuloEquivalente, int articuloPrincipal) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosDao().insertarAticulosEquivalenteSolicitud(con, numeroSolicitud, articuloEquivalente,articuloPrincipal);
	}
	
	
	
	/**
	 * Este m&eacute;todo se encarga de buscar los art&iacute;culos
	 * asociados a una solicitud de medicamentos
	 * @param conn
	 * @param dto
	 * @return ArrayList<DtoArticulos>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoArticulos> buscarArticulosXSolicitudMedicamentos(Connection conn,
			int numeroSolicitud){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosDao().buscarArticulosXSolicitudMedicamentos(conn, numeroSolicitud);
	} 
	
	
	/**
	 * @param conn
	 * @param codigoMedico
	 * @return Datos del medico que anula
	 * @throws SQLException
	 */
	public String consultarDatosMedicoAnulacion(Connection conn,Integer codigoMedico) throws SQLException{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosDao().consultarDatosMedicoAnulacion(conn, codigoMedico);
	}
	
	
}