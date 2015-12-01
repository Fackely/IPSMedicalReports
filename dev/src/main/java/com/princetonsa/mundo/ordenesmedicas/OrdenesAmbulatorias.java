/*
 * 
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 * 
 */
package com.princetonsa.mundo.ordenesmedicas;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesJustificacionNoPos;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ordenesmedicas.OrdenesAmbulatoriasDao;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.ordenes.InfoArticuloOrdenAmbulatoriaDto;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DtoValidacionGeneracionAutorizacionCapitada;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IValidacionGeneracionAutorizacionCapitadaMundo;

/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 * 
 */
public class OrdenesAmbulatorias 
{
	/**
	 * Para hacer logs de este mundo
	 */	
	private static Logger logger = Logger.getLogger(OrdenesAmbulatorias.class);
	
	/*  desarrollo justificacion no pos */
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos
	 */
	@SuppressWarnings("rawtypes")
	private HashMap justificacionMap=new HashMap();
	
	/**
	 * Mapa medicamento pos
	 */
	@SuppressWarnings("rawtypes")
	private HashMap medicamentosPosMap=new HashMap();
	
	/**
	 * Mapa medicamento no pos
	 */
	@SuppressWarnings("rawtypes")
	private HashMap medicamentosNoPosMap=new HashMap();
	
	/**
	 * Mapa medicamento sustituto no pos
	 */
	@SuppressWarnings("rawtypes")
	private HashMap sustitutosNoPosMap=new HashMap();
	
	/**
	 * Mapa diagnosticos definitivos
	 */
	@SuppressWarnings("rawtypes")
	private HashMap diagnosticosDefinitivos=new HashMap();
	
	//-------*------
	
	/**
	 * 
	 */
	private OrdenesAmbulatoriasDao objetoDao;
	
	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap ordenes;
	
	/**
	 * 
	 */
	private String tipoOrden;
	
	/**
	 * 
	 */
	private String centroAtencion;
	
	/**
	 * 
	 */
	private String profesional;
	
	/**
	 * 
	 */
	private String especialidad;
	
	/**
	 * 
	 */
	private String numeroOrden;
	
	/**
	 * 
	 */
	private boolean urgente;
	
	/**
	 * 
	 */
	private String fechaOrden;
	
	/**
	 * 
	 */
	private String hora;
	/**
	 * 
	 */
	private String servicio;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * 
	 */
	private String cantidad;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private boolean consultaExterna;
	
	/**
	 * 
	 */
	private String centroCostoSolicita;
	
	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap articulos;

	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String loginUsuario;
	
	/**
	 * 
	 */
	private String codigoPaciente;
	
	/**
	 * Indicativo que informa si la orden se genero como pyp.
	 */
	private boolean pyp;
	
	/**
	 * 
	 */
	private String estadoOrden;
	
	/**
	 * 
	 */
	private String numeroSolicitud;
	
	/**
	 * 
	 */
	private String fechaConfirma;
	
	/**
	 * 
	 */
	private String horaConfirma;
	
	/**
	 * 
	 */
	private String usuarioConfirma;

	/**
	 * 
	 */
	private String finalidadServicio;
	
	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap servicios;

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap justificacionesServicios;
	
	/**
	 * 
	 * */
	private String otros; 
	
	/**
	 * 
	 */
	private Vector<String> consecutivosOrdenesInsertadas;
	
	/**
	 * 
	 * */
	private int idIngresoPaciente;
	
	/**
	 * 
	 */
	private int cuentaPaciente;
	
	private String controlEspecial;
	
	private int codigoCitaAsociada;
	
	private ArrayList<DtoValidacionGeneracionAutorizacionCapitada> dtoValidaciones = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
	/**
	 * Mensajes desde el properties	
	 */
	private static MessageResources mensajes = MessageResources.getMessageResources("com.servinte.mensajes.ordenes.OrdenesAmbulatoriasForm");	
	
	/**
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void reset() 
	{
		this.numeroSolicitud="";
		this.fechaConfirma="";
		this.horaConfirma="";
		this.usuarioConfirma="";
		this.tipoOrden="";
		this.centroAtencion="";
		this.profesional="";
		this.especialidad="";
		this.numeroOrden="";
		this.urgente=false;
		this.fechaOrden="";
		this.hora="";
		this.tipoServicio="";
		this.cantidad="";
		this.observaciones="";
		this.consultaExterna=false;
		this.centroCostoSolicita="";
		this.servicio="";
		this.ordenes=new HashMap();
		this.ordenes.put("numRegistros","0");
		this.articulos=new HashMap();
		this.articulos.put("numRegistros","0");
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.loginUsuario="";
		this.codigoPaciente="";
		this.pyp=false;
		this.estadoOrden="";
		this.finalidadServicio="";
		this.servicios=new HashMap();
		this.servicios.put("numRegistros","0");
		this.otros = "";
		this.consecutivosOrdenesInsertadas= new Vector<String>();
		this.idIngresoPaciente = ConstantesBD.codigoNuncaValido;
		this.cuentaPaciente = ConstantesBD.codigoNuncaValido;
		this.controlEspecial="";
		this.codigoCitaAsociada=ConstantesBD.codigoNuncaValido;
		this.dtoValidaciones=new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
		
	}
	


	/**
	 * @return the cuentaPaciente
	 */
	public int getCuentaPaciente() {
		return cuentaPaciente;
	}



	/**
	 * @param cuentaPaciente the cuentaPaciente to set
	 */
	public void setCuentaPaciente(int cuentaPaciente) {
		this.cuentaPaciente = cuentaPaciente;
	}



	public String getControlEspecial() {
		return controlEspecial;
	}



	public void setControlEspecial(String controlEspecial) {
		this.controlEspecial = controlEspecial;
	}



	public OrdenesAmbulatorias()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param property
	 */
	private boolean init(String tipoBD) 
	{
		if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este 
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getOrdenesAmbulatoriasDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getOrdenes() {
		return ordenes;
	}

	@SuppressWarnings("rawtypes")
	public void setOrdenes(HashMap ordenes) {
		this.ordenes = ordenes;
	}

	/**
	 * Metodo que realiza las consultas de las ordenes ambulatorias de un paciente, se envia el codigo del estado de las solicitudes
	 * si se envia -1, realiza la consulta de ordenes en cualquiere estado.
	 * @param con
	 * @param codigoPersona
	 * @param idIngreso 
	 * @param codigoInstitucion
	 * @param i
	 */
	public void consultarOrdenesAmbulatoriasPaciente(Connection con, int codigoPersona, String institucion, int estado, int idIngreso) 
	{
		this.ordenes=objetoDao.consultarOrdenesAmbulatoriasPaciente(con,codigoPersona,institucion,estado, idIngreso);
		//logger.info("\n\nORDENEEEEESSSS->>"+ordenes);
	}

	/**
	 * Metodo que realiza las consultas de las ordenes ambulatorias de un paciente, se envia el codigo del estado de las solicitudes
	 * si se envia -1, realiza la consulta de ordenes en cualquiere estado.
	 * @param con
	 * @param codigoPersona
	 * @param codigoInstitucion
	 * @param i
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarOrdenesAmbulatoriasPacientePrincipal(Connection con, int codigoPersona, String institucion, int estado) 
	{
		return objetoDao.consultarOrdenesAmbulatoriasPaciente(con,codigoPersona,institucion,estado, ConstantesBD.codigoNuncaValido);
	}
	
	/**
	 * 
	 * @param con
	 * @param articulosMap
	 * @param codigoOrden
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean updateOrdenAmbulatoriaArticulos(Connection con, HashMap articulosMap, String codigoOrden)
	{
		return objetoDao.updateOrdenAmbulatoriaArticulos(con, articulosMap, codigoOrden);
	}
	
	/**
	 * Metodo que realiza las consultas de las ordenes ambulatorias x codigo
	 * @param con
	 * @param codigoPersona
	 * @param codigoInstitucion
	 * @param i
	 */
	public void consultarOrdenesAmbulatoriasXCodigoOrden(Connection con, String codigoOrden , String institucion) 
	{
		this.ordenes=objetoDao.consultarOrdenesAmbulatoriasXCodigoOrden(con,codigoOrden,institucion);
	}
	
	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public boolean isConsultaExterna() {
		return consultaExterna;
	}

	public void setConsultaExterna(boolean consultaExterna) {
		this.consultaExterna = consultaExterna;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getFechaOrden() {
		return fechaOrden;
	}

	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	public String getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public OrdenesAmbulatoriasDao getObjetoDao() {
		return objetoDao;
	}

	public void setObjetoDao(OrdenesAmbulatoriasDao objetoDao) {
		this.objetoDao = objetoDao;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getProfesional() {
		return profesional;
	}

	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getTipoOrden() {
		return tipoOrden;
	}

	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public boolean isUrgente() {
		return urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap getArticulos() {
		return articulos;
	}

	@SuppressWarnings("rawtypes")
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	
	public Object getArticulos(String key) {
		return articulos.get(key);
	}

	@SuppressWarnings("unchecked")
	public void setArticulos(String key,Object value) {
		this.articulos.put(key,value);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 */
	public void cargarDetalleOrdenArticulos(Connection con, String codigoOrden) 
	{
		this.setArticulos(objetoDao.cargarDetalleOrdenArticulos(con,codigoOrden));
	}

	/**
	 * 
	 * @param con
	 * @param string
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDetalleOrdenArticulosValidacion(Connection con, String codigoOrden) 
	{
		return objetoDao.cargarDetalleOrdenArticulos(con,codigoOrden);
	}
	
	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * 
	 * @param con
	 * @param esTransaccional 
	 * @param usuario 
	 * @param paciente 
	 * @param request 
	 * @return codigo de la orden
	 * @throws IPSException 
	 */
	@SuppressWarnings({"unchecked", "rawtypes" })
	public String[] guardarOrdenAmbulatoria(Connection con, boolean esTransaccional, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionErrors errores) throws IPSException 
	{		
		String codigoOrden=ConstantesBD.codigoNuncaValido+"";
		List<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionGeneracionAutorizacionCapitada = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
		List<AutorizacionCapitacionDto> listaAutorizacionesCapitacion= new ArrayList<AutorizacionCapitacionDto>();
		String codIngreso ="";
		int numOrden = 0;
		int contratoConvenio = ConstantesBD.codigoNuncaValido;
		int convenio = ConstantesBD.codigoNuncaValido;
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();

		if(Integer.parseInt(this.tipoOrden)==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)
		{
			int numServ=Integer.parseInt(this.servicios.get("numRegistros")+"");
			boolean transaccion=true;
			
			if(numServ <= 0)
			{
				this.numeroOrden=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ordenes_ambulatorias_base")+"";
				
				HashMap mapaInfoGeneral=this.cargarMapaGeneralNuevaOrdenAmbulatoria();
				transaccion=objetoDao.guardarInformacionGeneralAmbulatorios(con,mapaInfoGeneral);
				codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,this.numeroOrden,this.institucion);
				
				if(esTransaccional)
				{
					if(transaccion)
						UtilidadBD.finalizarTransaccion(con);
					else
						UtilidadBD.abortarTransaccion(con);
				}
			}
			
			 
			 /*
			 * MT: 3796, 3458, 2756 y 3888
			 * Cambio: DCU 307 V1.62
			 * Diana Ruiz
			 */
				
			/*Verifico lo definido en el parametro 'Vía Ingreso para Validaciones de Ordenes Ambulatorias y Peticiones'*/
			String viaIngresoPrametro= ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
			String tipoPaciente = ValoresPorDefecto.getTipoPacienteValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
			String consecutivoOrden="";
		  	//String tipoPaciente=listaTiposPaciente.get(0).get("codigoTipoPaciente").toString();*/
		  	String pyp="";
			for(int i=0;i<numServ;i++)
			{
				String cubierto = ConstantesBD.acronimoNo;
				if(!UtilidadTexto.getBoolean(this.servicios.get("fueEliminadoServicio_"+i)+""))
				{
					//---------Se incrementa el consecutivo ---------------------//
					transaccion=true;
					
					if(esTransaccional){
						transaccion = UtilidadBD.iniciarTransaccion(con);
					}
					
					
					Integer codServicio 	= null;
					String descServicio 	= null;
					Integer cantServicio 	= null;
					Integer ordenAutorizar 	= null;
					
					if(transaccion)
					{
						numOrden=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ordenes_ambulatorias_base");
						ordenAutorizar = numOrden;
						
						this.numeroOrden=String.valueOf(numOrden);
						this.consecutivosOrdenesInsertadas.add(this.numeroOrden);
						
						this.urgente=UtilidadTexto.getBoolean(this.servicios.get("urgente_"+i)+"");
						HashMap mapaInfoGeneral=this.cargarMapaGeneralNuevaOrdenAmbulatoria();
						pyp = mapaInfoGeneral.get("pyp")+"";
						transaccion=objetoDao.guardarInformacionGeneralAmbulatorios(con,mapaInfoGeneral);
						codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,String.valueOf(numOrden),this.institucion);
						HashMap detalle=new HashMap();

						/*Obtener el ingreso del paciente */
					  	codIngreso= mapaInfoGeneral.get("ingreso")+"";
					  	
					  	consecutivoOrden=mapaInfoGeneral.get("consecutivo")+"";
						/*Envio la información al proceso de cobertura */
						InfoResponsableCobertura infoResponsableCobertura= 
	    	        		Cobertura.validacionCoberturaServicio(con, codIngreso,
	    	        				Utilidades.convertirAEntero(viaIngresoPrametro), tipoPaciente, 
	    	        				Utilidades.convertirAEntero(this.servicios.get("codigo_"+i)+""), usuario.getCodigoInstitucionInt(), false, "");
						
						/*Verifico si el servicio se encuentra cubierto por el convenio responsable */
						if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe())	{
							cubierto=ConstantesBD.acronimoSi;
						}
							
						
						contratoConvenio=infoResponsableCobertura.getDtoSubCuenta().getContrato();
						convenio = infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();

						detalle.put("codigo",codigoOrden);
						detalle.put("servicio",this.servicios.get("codigo_"+i)+"");
						detalle.put("finalidad",this.servicios.get("finalidad_"+i)+"");
						detalle.put("cantidad",this.servicios.get("cantidad_"+i)+"");
						/*Adiciono la información de cobertura para cada servicio*/ 
						detalle.put("contrato_convenio_",contratoConvenio);
						detalle.put("cubierto_",cubierto);
						transaccion=objetoDao.guardarInformacionDetalleOrdenAmbulatorioServicio(con,detalle);
						
						logger.info("valor del servicio >> "+this.servicios);
						//SI EL INDICADOR DE JUSTIFICAR ES VERDADERO SE HACE EL INGRESO DE LA JUSTIFICACIÓN NO POS
						if (this.servicios.containsKey("justificar_"+i) && 
								UtilidadTexto.getBoolean(this.servicios.get("justificar_"+i)+"")){
		                	//cxForm.getServiciosMap().put(i+"_servicio", cxForm.getServiciosMap("codigoServicio_"+i));
							FormatoJustServNopos.ingresarJustificacion(
		                		con,
		                		usuario.getCodigoInstitucionInt(), 
		                		usuario.getLoginUsuario(), 
		                		(HashMap) //this.justificacionesServicios,
		                		this.justificacionesServicios.get(this.servicios.get("codigo_"+i).toString()+"_mapajustificacion"),
		                		ConstantesBD.codigoNuncaValido,
		                		Utilidades.convertirAEntero(codigoOrden),
		                		Integer.parseInt(this.servicios.get("codigo_"+i).toString()),
		                		usuario.getCodigoPersona());
		                		//cxForm.setJustificar(cxForm.getServiciosMap("justificar_"+i).toString());
		                }
						codServicio = new Integer(Utilidades.convertirAEntero(this.servicios.get("codigo_"+i).toString()));
						descServicio = this.servicios.get("descripcionServicio_"+i).toString();
						cantServicio = new Integer(Utilidades.convertirAEntero(this.servicios.get("cantidad_"+i).toString()));
					}
					
					
					if(esTransaccional)
					{
						if(transaccion){
							UtilidadBD.finalizarTransaccion(con);
							
							if(ordenAutorizar!=null && codServicio!=null && descServicio!=null && cantServicio!=null)
							{
								//TODO
								/*
								 * Cambios en autorizaciones donde se permite generar en una misma autorización 
								 * n ordenes ambulatorias
								 * MT  
								 */
								DtoValidacionGeneracionAutorizacionCapitada dtoValidacionGeneraAutorizacion = new DtoValidacionGeneracionAutorizacionCapitada();
								dtoValidacionGeneraAutorizacion.setOrdenAmbulatoria(Integer.parseInt(codigoOrden));
								dtoValidacionGeneraAutorizacion.setConsecutivoOrden(consecutivoOrden);
								
								DtoServicios servicio = new DtoServicios();
								servicio.setCodigoServicio(codServicio);
								servicio.setDescripcionServicio(descServicio);
								servicio.setCantidad(cantServicio);
								servicio.setServicioCubierto(cubierto);
								servicio.setUrgente(urgente);
								dtoValidacionGeneraAutorizacion.getServicios().add(servicio);
								dtoValidacionGeneraAutorizacion.setContratoConvenioResponsable(contratoConvenio);
								dtoValidacionGeneraAutorizacion.setConvenio(convenio);
								DtoDiagnostico dtoDiagnostico=new DtoDiagnostico();	
								dtoDiagnostico=this.consultarDiagnosticoOrdenAmbulatoria(con, Utilidades.convertirAEntero(this.getNumeroOrden()));
								dtoValidacionGeneraAutorizacion.setTipoOrden(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria);
								dtoValidacionGeneraAutorizacion.setPaciente(paciente);		
								dtoValidacionGeneraAutorizacion.getCentrosCostoSolicitante().setCodigo(Utilidades.convertirAEntero(this.getCentroCostoSolicita()));
								dtoValidacionGeneraAutorizacion.setAcronimoDiagnostico(dtoDiagnostico.getAcronimoDiagnostico());
								dtoValidacionGeneraAutorizacion.setCodIngreso(Integer.parseInt(codIngreso));
								if (!pyp.isEmpty()){
									if (Boolean.parseBoolean(pyp)){
										dtoValidacionGeneraAutorizacion.setPyp(true);
									}else {
										dtoValidacionGeneraAutorizacion.setPyp(false);
										servicio.setAcronimoTipoServicio(this.getServicios().get("tipoServicio_"+i).toString());
										servicio.setCodigoEspecialidad(Integer.parseInt(this.getServicios().get("codigoespecialidad_"+i).toString()));
										servicio.setCodigoGrupoServicio(Integer.parseInt(this.getServicios().get("gruposervicio_"+i).toString()));
										if (this.getServicios().get("codigoCups_"+i) != null){
											servicio.setCodigoPropietarioServicio(this.getServicios().get("codigoCups_"+i).toString());
										}
									}
								}
								listaValidacionGeneracionAutorizacionCapitada.add(dtoValidacionGeneraAutorizacion);
							}
						}
						else
							UtilidadBD.abortarTransaccion(con);
					}
				}
			}
			/*
			 * Envio el listado de las ordenes a autorizar al proceso de validación de generación de autorización capitada
			 */
			try {
				listaAutorizacionesCapitacion = generarAutorizacionCapitacion(paciente, con, usuario, request, listaValidacionGeneracionAutorizacionCapitada);
			} catch (IPSException ipse) {
				errores.add("", new ActionMessage(String.valueOf(ipse.getErrorCode())));
				Log4JManager.error(ipse.getMessage(), ipse);
			}
			
			
		}
		else if(Integer.parseInt(this.tipoOrden)==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos)
		{			
			boolean transaccion=true;
			
			if(esTransaccional)
				transaccion = UtilidadBD.iniciarTransaccion(con);
			if(transaccion)
			{
				//manejo del consecutivo.
				numOrden=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ordenes_ambulatorias_base");
				this.numeroOrden=String.valueOf(numOrden);
				this.consecutivosOrdenesInsertadas.add(this.numeroOrden);	
				
				HashMap mapaInfoGeneral=this.cargarMapaGeneralNuevaOrdenAmbulatoria();
				transaccion=objetoDao.guardarInformacionGeneralAmbulatorios(con,mapaInfoGeneral);
				codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,String.valueOf(numOrden),this.institucion);
				
				/*
				 * MT: 3796, 3458, 2756 y 3888
				 * Cambio: DCU 307 V1.62
				 * Diana Ruiz
				 */
				
				
				/*Verifico lo definido en el parametro 'Vía Ingreso para Validaciones de Ordenes Ambulatorias y Peticiones'*/
				String viaIngresoPrametro= ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
							
				/*Obtener el tipo de paciente según la vía de Ingreso */
				ArrayList<HashMap<String,Object>> listaTiposPaciente=new ArrayList<HashMap<String,Object>>();
			  	listaTiposPaciente = UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, viaIngresoPrametro);
			  	String tipoPaciente=listaTiposPaciente.get(0).get("codigoTipoPaciente").toString();
				
				/*Obtener el ingreso del paciente */
			  	codIngreso= mapaInfoGeneral.get("ingreso")+"";
				this.articulos.put("codOrden",codigoOrden);
				int numArti=Integer.parseInt(this.articulos.get("numRegistros")+"");
								
				for(int i=0;i<numArti;i++){
					String cubierto = ConstantesBD.acronimoNo;
				/*Envio la información al proceso de cobertura */
					InfoResponsableCobertura infoResponsableCobertura = new InfoResponsableCobertura(); 
					infoResponsableCobertura = Cobertura.validacionCoberturaArticulo(con, codIngreso, Utilidades.convertirAEntero(viaIngresoPrametro), 
							tipoPaciente, Utilidades.convertirAEntero(this.articulos.get("articulo_"+i)+""),
							usuario.getCodigoInstitucionInt(), false);
										
					/*Verifico si el servicio se encuentra cubierto por el convenio responsable */
					if(infoResponsableCobertura.getInfoCobertura().incluido()&& infoResponsableCobertura.getInfoCobertura().existe()){
						cubierto=ConstantesBD.acronimoSi;
					}
				
					
					contratoConvenio=infoResponsableCobertura.getDtoSubCuenta().getContrato();
					convenio = infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
										
					this.articulos.put("contrato_convenio_"+i,contratoConvenio);
					this.articulos.put("cubierto_"+i,cubierto);		
				}
				transaccion=objetoDao.guardarInformacionDetalleOrdenAmbulatorioArticulo(con,this.articulos);
				
				// ************* GUARDAR JUSTIFICACIONES NO POS *****************
				FormatoJustArtNopos fjan=new FormatoJustArtNopos();
				int numReg=Utilidades.convertirAEntero(articulos.get("numRegistros")==null||(articulos.get("numRegistros")+"").equals("")?"0":articulos.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					
					
					this.articulos.get("articulo_"+i);
					
					
					HashMap mapaAsocioJustArt=(HashMap)request.getSession().getAttribute("MAPASOCIOJUSTIFICACION");
					if(mapaAsocioJustArt !=null
							&&this.justificacionMap.containsKey(articulos.get("articulo_"+i)+"_sevaasociar")
							&&mapaAsocioJustArt.get(articulos.get("articulo_"+i)+"_codigoAsocio")!=null
							&&mapaAsocioJustArt.get(articulos.get("articulo_"+i)+"_codigoAsocio")!=null
							&&!UtilidadTexto.isEmpty(mapaAsocioJustArt.get(this.articulos.get("articulo_"+i)+"_codigoAsocio").toString())){
							UtilidadesJustificacionNoPos.insertarAsocioOrdenJustificacion(con, Integer.parseInt(codigoOrden), Utilidades.convertirAEntero(mapaAsocioJustArt.get(this.articulos.get("articulo_"+i)+"_codigoAsocio").toString()),Utilidades.convertirAEntero(this.articulos.get("cantidad_"+i)+""));
					}else{
					
						if(!UtilidadTexto.getBoolean(articulos.get("fueEliminadoArticulo_"+i)+""))
						{
							logger.info("valor de articulos >> "+articulos);
							if(articulos.containsKey("tipoPosArticulo_"+i) && 
									articulos.get("tipoPosArticulo_"+i).equals("NOPOS"))
							{
								if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(this.articulos.get("articulo_"+i)+"")) )
								{
									logger.info("JUSTIFICACION NO POS MAP \n "+this.justificacionMap);
									
									if(UtilidadTexto.getBoolean(articulos.get("medicamento_"+i)+"")){
										if(this.justificacionMap.get(this.articulos.get("articulo_"+i)+"_pendiente").equals("1"))
										{
											fjan.insertarJustificacion(	con,
																		ConstantesBD.codigoNuncaValido,
																		Utilidades.convertirAEntero(codigoOrden),
																		(HashMap) //this.justificacionMap,
																		this.justificacionMap.get(this.articulos.get("articulo_"+i).toString()+"_mapajustificacion"),
																		this.medicamentosNoPosMap,
																		this.medicamentosPosMap,
																		this.sustitutosNoPosMap,
																		this.diagnosticosDefinitivos,
																		Utilidades.convertirAEntero(this.articulos.get("articulo_"+i)+""),
																		usuario.getCodigoInstitucionInt(), 
																		this.articulos.get("observaciones_"+i)+"", 
																		ConstantesBD.continuarTransaccion,
																		Utilidades.convertirAEntero(this.articulos.get("articulo_"+i)+""), 
																		this.articulos.get("observaciones_"+i)+"", 
																		this.articulos.get("dosis_"+i)+"", 
																		this.articulos.get("unidosis_"+i)+"", 
																		Utilidades.convertirAEntero(this.articulos.get("frecuencia_"+i)+""), 
																		this.articulos.get("tipofrecuencia_"+i)+"", 
																		this.articulos.get("via_"+i)+"", 
																		Utilidades.convertirADouble(this.articulos.get("cantidad_"+i)+""), 
																		this.articulos.get("duraciontratamiento_"+i)+"", 
																		usuario.getLoginUsuario());
										}
									} else {
										// Guardar Justificacion No Pos de Articulo diferente a medicamento
										ArrayList<DtoParamJusNoPos> justificacion = new ArrayList<DtoParamJusNoPos>();
										justificacion = (ArrayList<DtoParamJusNoPos>)request.getSession().getAttribute("JUSTIFICACION");
										for(int jus=0; jus<justificacion.size(); jus++){
							    			if(justificacion.get(jus).getCodigoArticulo().equals(this.articulos.get("articulo_"+i)+"")){
							    				logger.info("CODIGO ORDEN AMBULATORIA: "+codigoOrden);
							    				justificacion.get(jus).setOrdenAmbulatoria(codigoOrden);
							    				UtilidadesJustificacionNoPos.guardarJustificacion(con, justificacion.get(jus), ConstantesIntegridadDominio.acronimoInsumo, usuario);
							    			}
							    		}
									}
								}
							}
						}
					}
					request.getSession().setAttribute("RESETASOCIOJUSTIFICACION", 1);
					
				}
				// ************* FIN GUARDAR JUSTIFICACIONES NO POS *************
			}
			if(esTransaccional)
			{
				if(transaccion){
					UtilidadBD.finalizarTransaccion(con);
					
					//TODO
					DtoValidacionGeneracionAutorizacionCapitada dtoValidacionGeneraAutorizacion = new DtoValidacionGeneracionAutorizacionCapitada();
					dtoValidacionGeneraAutorizacion.setOrdenAmbulatoria(Integer.parseInt(codigoOrden));
					dtoValidacionGeneraAutorizacion.setConsecutivoOrden(numOrden+"");
					dtoValidacionGeneraAutorizacion.setContratoConvenioResponsable(contratoConvenio);
					dtoValidacionGeneraAutorizacion.setConvenio(convenio);
					DtoDiagnostico dtoDiagnostico=new DtoDiagnostico();	
					dtoDiagnostico=this.consultarDiagnosticoOrdenAmbulatoria(con, Utilidades.convertirAEntero(this.getNumeroOrden()));
					dtoValidacionGeneraAutorizacion.setTipoOrden(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria);
					dtoValidacionGeneraAutorizacion.setPaciente(paciente);		
					dtoValidacionGeneraAutorizacion.getCentrosCostoSolicitante().setCodigo(Utilidades.convertirAEntero(this.getCentroCostoSolicita()));
					dtoValidacionGeneraAutorizacion.setAcronimoDiagnostico(dtoDiagnostico.getAcronimoDiagnostico());
					dtoValidacionGeneraAutorizacion.setCodIngreso(Integer.parseInt(codIngreso));
					dtoValidacionGeneraAutorizacion.setOrdenMedicamentoUrgente(this.urgente);					
					listaValidacionGeneracionAutorizacionCapitada.add(dtoValidacionGeneraAutorizacion);
					
					// Se hace llamado al proceso de Generación Capitacion DCU-966*/
					listaAutorizacionesCapitacion = generarAutorizacionCapitacion(paciente, con, usuario, request, listaValidacionGeneracionAutorizacionCapitada);
					
					
				} else{
					UtilidadBD.abortarTransaccion(con);
				}
			}
			
			request.removeAttribute("JUSTIFICACION");
		}
		if(listaAutorizacionesCapitacion!=null && !listaAutorizacionesCapitacion.isEmpty()){//Se adiciona mensaje para los servicio que no se autorizaron
			manejoPacienteFacade.obtenerMensajesError(listaAutorizacionesCapitacion, errores);
		}
		String[] a={"", ""};
		a[0]=codigoOrden;
		a[1]=this.numeroOrden;
		return a;
	}

	/**
	 * 
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap cargarMapaGeneralNuevaOrdenAmbulatoria() 
	{
		HashMap mapa=new HashMap();
		mapa.put("consecutivo",this.numeroOrden);
		mapa.put("institucion",this.institucion+"");
		mapa.put("paciente",this.codigoPaciente);
		mapa.put("tipoOrden",this.tipoOrden);
		mapa.put("pyp",this.pyp);
		mapa.put("urgente",this.urgente);
		mapa.put("centroAtencion",this.centroAtencion);
		mapa.put("usuario",this.loginUsuario);
		mapa.put("especialidad",this.especialidad);
		mapa.put("fecha",this.fechaOrden);
		mapa.put("hora",this.hora);
		mapa.put("observaciones",this.observaciones);
		mapa.put("fechaConfirmacion","");
		mapa.put("horaConfirmacion","");
		mapa.put("usuarioConfirma","");
		mapa.put("estado",this.estadoOrden);
		mapa.put("consultaExterna",this.consultaExterna+"");
		mapa.put("centroCostoSolicita", this.centroCostoSolicita);
		mapa.put("otros", this.otros);		
		mapa.put("ingreso", this.idIngresoPaciente+"");
		mapa.put("cuenta", this.cuentaPaciente+"");
		mapa.put("controlEspecial", this.controlEspecial);
		mapa.put("citaAsociada", this.codigoCitaAsociada);
		logger.info("\n\nCONTROLLL ESPECIAAAL--------------->"+mapa.get("controlEspecial"));
		return mapa;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public boolean isPyp() {
		return pyp;
	}

	public void setPyp(boolean pyp) {
		this.pyp = pyp;
	}

	public String getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(String estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public String getFechaConfirma() {
		return fechaConfirma;
	}

	public void setFechaConfirma(String fechaConfirma) {
		this.fechaConfirma = fechaConfirma;
	}

	public String getHoraConfirma() {
		return horaConfirma;
	}

	public void setHoraConfirma(String horaConfirma) {
		this.horaConfirma = horaConfirma;
	}

	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public String getUsuarioConfirma() {
		return usuarioConfirma;
	}

	public void setUsuarioConfirma(String usuarioConfirma) {
		this.usuarioConfirma = usuarioConfirma;
	}

	public String getFinalidadServicio() {
		return finalidadServicio;
	}

	public void setFinalidadServicio(String finalidadServicio) {
		this.finalidadServicio = finalidadServicio;
	}

	public boolean responderOrdenAmbulatorio(Connection con, @SuppressWarnings("rawtypes") HashMap vo) 
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		enTransaccion=objetoDao.ingresarResultado(con,vo);
		enTransaccion=actualizarEstadoOrdenAmbulatoria(con,vo.get("codigo")+"",ConstantesBD.codigoEstadoOrdenAmbulatoriaRespondida);
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		return enTransaccion;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean anularOrden(Connection con, HashMap vo) 
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		enTransaccion=objetoDao.anularOrden(con,vo);
		enTransaccion=actualizarEstadoOrdenAmbulatoria(con,vo.get("codigo")+"",ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada);
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		return enTransaccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @param codigoEstadoOrdenAmbulatoriaRespondida
	 * @return
	 */
	public static boolean actualizarEstadoOrdenAmbulatoria(Connection con, String codigo, int estado) 
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return myFactoryLocal.getOrdenesAmbulatoriasDao().actualizarEstadoOrdenAmbulatoria(con,codigo,estado);
	}


	/**
	 * 
	 * @param con
	 * @param usuario 
	 * @param comentario 
	 * @param string
	 * @param codigoEstadoProgramaPYPCancelado
	 */
	public void actualizarEstadoActividadProgramaPYPPAcienteNumOrden(Connection con, String codOrden, String estado, String usuario, String comentario) 
	{
		objetoDao.actualizarEstadoActividadProgramaPYPPAcienteNumOrden(con,codOrden,estado,usuario,comentario);
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario 
	 * @param comentario 
	 * @param string
	 * @param codigoEstadoProgramaPYPCancelado
	 */
	public static void actualizarEstadoActividadProgramaPYPPAcienteNumOrdenEstatico(Connection con, String codOrden, String estado, String usuario, String comentario) 
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		myFactoryLocal.getOrdenesAmbulatoriasDao().actualizarEstadoActividadProgramaPYPPAcienteNumOrden(con,codOrden,estado,usuario,comentario);
	}
	
	
	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int actualizarSolicitudEnOrdenAmbulatoria(Connection con,HashMap campos)
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return myFactoryLocal.getOrdenesAmbulatoriasDao().actualizarSolicitudEnOrdenAmbulatoria(con,campos);
	}
	
	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria para la reserva de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int confirmarReservaCitaEnOrdenAmbulatoria(Connection con,HashMap campos)
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return myFactoryLocal.getOrdenesAmbulatoriasDao().confirmarReservaCitaEnOrdenAmbulatoria(con,campos);
	}
	
	
	

	@SuppressWarnings("rawtypes")
	public HashMap getServicios() {
		return servicios;
	}

	@SuppressWarnings("rawtypes")
	public void setServicios(HashMap servicios) {
		this.servicios = servicios;
	}

	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @param institucion
	 * Formato: urgente SeparadoSplit observaciones SeparadoSplit centro_atencion_solicita SeparadoSplit usuario_solicita SeparadoSplit especialidad_solicita SeparadoSplit servicio SeparadoSplit finalidad 
	 */
	public static String obtenerInfoServicioProcOrdenAmbulatoria(Connection con, String ordenAmbulatoria, int institucion) 
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return myFactoryLocal.getOrdenesAmbulatoriasDao().obtenerInfoServicioProcOrdenAmbulatoria(con,ordenAmbulatoria, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int confirmarOrdenAmbulatoria(Connection con, HashMap campos) 
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return myFactoryLocal.getOrdenesAmbulatoriasDao().confirmarOrdenAmbulatoria(con,campos);
	}
	
	/**
	 * Método implementado para ingresar informacion de referencia de consulta
	 * externa a la orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int ingresarInformacionReferenciaExterna(Connection con,@SuppressWarnings("rawtypes") HashMap campos)
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return myFactoryLocal.getOrdenesAmbulatoriasDao().ingresarInformacionReferenciaExterna(con,campos);
	}

	/**
	 * 
	 * @param con
	 * @param numeroOrden
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServiciosOrdenAmbulatoria(Connection con, String numeroOrden, int institucion)
	{
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenesAmbulatoriasDao().consultarServiciosOrdenAmbulatoria(con,numeroOrden, institucion);
	}

	/**
	 * @return the centroCostoSolicita
	 */
	public String getCentroCostoSolicita() {
		return centroCostoSolicita;
	}

	/**
	 * @param centroCostoSolicita the centroCostoSolicita to set
	 */
	public void setCentroCostoSolicita(String centroCostoSolicita) {
		this.centroCostoSolicita = centroCostoSolicita;
	}

	/**
	 * @return the justificacionesServicios
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getJustificacionesServicios() {
		return justificacionesServicios;
	}

	/**
	 * @param justificacionesServicios the justificacionesServicios to set
	 */
	@SuppressWarnings("rawtypes")
	public void setJustificacionesServicios(HashMap justificacionesServicios) {
		this.justificacionesServicios = justificacionesServicios;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoOA
	 * @return
	 * @throws SQLException
	 */
	public static int obtenerCentroCostoSolicitante(Connection con, String codigoOA) throws SQLException
	{
		return Utilidades.convertirAEntero(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenesAmbulatoriasDao().consultarCentroCostoSolicitante(con,codigoOA).split(ConstantesBD.separadorSplit)[0]/*me toco hacer esto porque lo hicieron mal desde el sql*/);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoOA
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerCentroCostoSolicitanteDesc(Connection con, String codigoOA) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenesAmbulatoriasDao().consultarCentroCostoSolicitante(con,codigoOA);
	} 

	/**
	 * @return the otros
	 */
	public String getOtros() {
		return otros;
	}

	/**
	 * @param otros the otros to set
	 */
	public void setOtros(String otros) {
		this.otros = otros;
	}

	/**
	 * @return the consecutivosOrdenesInsertadas
	 */
	public Vector<String> getConsecutivosOrdenesInsertadas() {
		return consecutivosOrdenesInsertadas;
	}

	/**
	 * @param consecutivosOrdenesInsertadas the consecutivosOrdenesInsertadas to set
	 */
	public void setConsecutivosOrdenesInsertadas(
			Vector<String> consecutivosOrdenesInsertadas) {
		this.consecutivosOrdenesInsertadas = consecutivosOrdenesInsertadas;
	}

	/**
	 * @return the justificacionMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getJustificacionMap() {
		return justificacionMap;
	}

	/**
	 * @return the medicamentosPosMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getMedicamentosPosMap() {
		return medicamentosPosMap;
	}

	/**
	 * @return the medicamentosNoPosMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getMedicamentosNoPosMap() {
		return medicamentosNoPosMap;
	}

	/**
	 * @return the sustitutosNoPosMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getSustitutosNoPosMap() {
		return sustitutosNoPosMap;
	}

	/**
	 * @return the diagnosticosDefinitivos
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	@SuppressWarnings("rawtypes")
	public void setJustificacionMap(HashMap justificacionMap) {
		this.justificacionMap = justificacionMap;
	}

	/**
	 * @param medicamentosPosMap the medicamentosPosMap to set
	 */
	@SuppressWarnings("rawtypes")
	public void setMedicamentosPosMap(HashMap medicamentosPosMap) {
		this.medicamentosPosMap = medicamentosPosMap;
	}

	/**
	 * @param medicamentosNoPosMap the medicamentosNoPosMap to set
	 */
	@SuppressWarnings("rawtypes")
	public void setMedicamentosNoPosMap(HashMap medicamentosNoPosMap) {
		this.medicamentosNoPosMap = medicamentosNoPosMap;
	}

	/**
	 * @param sustitutosNoPosMap the sustitutosNoPosMap to set
	 */
	@SuppressWarnings("rawtypes")
	public void setSustitutosNoPosMap(HashMap sustitutosNoPosMap) {
		this.sustitutosNoPosMap = sustitutosNoPosMap;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	@SuppressWarnings("rawtypes")
	public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	public int getIdIngresoPaciente() {
		return idIngresoPaciente;
	}

	public void setIdIngresoPaciente(int idIngresoPaciente) {
		this.idIngresoPaciente = idIngresoPaciente;
	}
	
	/**
	 * Metodo para consultar los centros de costo asociados al grupo de servicio
	 * @param criterios
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCentrosCostoXUnidadAgendaServ(Connection con, int codigoServicio)
	{
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenesAmbulatoriasDao().consultaCentrosCostoXUnidadAgendaServ(con, codigoServicio);
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap consultarCodigoOrdenAmb(String consecutivo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenesAmbulatoriasDao().consultarCodigoOrdenAmb(consecutivo);
	}



	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public int otenerUltimaCuentaPacienteValidaParaOrden(Connection con,int codigoPersona) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenesAmbulatoriasDao().otenerUltimaCuentaPacienteValidaParaOrden(con,codigoPersona);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public static DtoDiagnostico consultarDiagnosticoOrden(Connection con,int codigoOrden) 
	{
		DaoFactory myFactoryLocal = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return myFactoryLocal.getOrdenesAmbulatoriasDao().consultarDiagnosticoOrden(con,codigoOrden);
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar el 
	 * diagn&oacute;stico asociado a la orden ambulatoria
	 * @param Connection con, int codigoOrden
	 * @return DtoDiagnostico
	 * @author Diana Carolina G
	 */
	public static DtoDiagnostico consultarDiagnosticoOrdenAmbulatoria(Connection con, int codigoOrdenAmbulatoria){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenesAmbulatoriasDao().consultarDiagnosticoOrdenAmbulatoria(con, codigoOrdenAmbulatoria);
	}
	
	/**
	 * Método que consulta los resultados asociados a las órdenes ambulatorias.
	 * 
	 * @param con
	 * @param numeroOrden
	 * 
	 * @return resultado
	 */
	public String consultarResultadoOrdenesAmbulatorias(Connection con, String numeroOrden) 
	{
		return objetoDao.consultarResultadoOrdenesAmbulatorias(con,numeroOrden);
	}
	
	
	/**
	 * Método que se encarga de setear los datos correspondientes al dtoValidacionGeneracionAutorizacionCapitadaMundo
	 * para la generación de la autorización de capitación para Ordenes Ambulatorias 
	 * RQF 02-0025
	 * 
	 * @author Camilo Gómez
	 * @param paciente
	 * @param con
	 * @param usuario
	 * @param request
	 */
	public List<AutorizacionCapitacionDto> generarAutorizacionCapitacion(PersonaBasica paciente,Connection con,
			UsuarioBasico usuario,HttpServletRequest request, List<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionGeneracionAutorizacionCapitada) throws IPSException
	{
		IValidacionGeneracionAutorizacionCapitadaMundo validacionGeneracionAutorizacionCapitadaMundo=ManejoPacienteFabricaMundo.crearValidacionGeneracionAutorizacionCapitadaMundo();
		List<AutorizacionCapitacionDto> listaAutorizacionesCapitacion = new ArrayList<AutorizacionCapitacionDto>();
		
		if(Integer.parseInt(this.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos)
		{//ARTICULOS
			DtoArticulos dtoArticulos				=new DtoArticulos();
			ArrayList<DtoArticulos>listaArticulos	=new ArrayList<DtoArticulos>();
			IArticuloDAO articuloDao = InventarioDAOFabrica.crearArticuloDAO();
			for(int i=0;i<Utilidades.convertirAEntero(this.getArticulos("numRegistros").toString());i++)
			{
				if(!UtilidadTexto.getBoolean(this.getArticulos("fueEliminadoArticulo_"+i))){
					dtoArticulos=new DtoArticulos();
					dtoArticulos.setCodigoArticulo(Utilidades.convertirAEntero(this.getArticulos("articulo_"+i).toString()));
					dtoArticulos.setDescripcionArticulo(this.getArticulos().get("descripcionArt_"+i).toString());
					dtoArticulos.setCantidadArticulo(Utilidades.convertirAEntero(this.getArticulos().get("cantidad_"+i).toString()));
					dtoArticulos.setArticuloCubierto(this.getArticulos().get("cubierto_"+i).toString());
					dtoArticulos.setCodNaturaleza(Utilidades.convertirAEntero(this.getArticulos().get("codnaturaleza_"+i).toString()));
					dtoArticulos.setClaseArticulo(Utilidades.convertirAEntero(this.getArticulos().get("codclaseinventario_"+i).toString()));
					dtoArticulos.setCodGrupoArticulo(Utilidades.convertirAEntero(this.getArticulos().get("codgrupo_"+i).toString()));
					dtoArticulos.setCodigoSubGrupoArticulo(Utilidades.convertirAEntero(this.getArticulos().get("codsubgrupo_"+i).toString()));

					/* MT 3873 */
				
					if (this.getArticulos().get("unidadMedidaArticulo_"+i)!= null)
						dtoArticulos.setUnidad_medida(this.getArticulos().get("unidadMedidaArticulo_"+i).toString());					
				
					dtoArticulos.setDosis(this.getArticulos().get("dosis_"+i).toString());
				
					int tipoFrec=Utilidades.convertirAEntero(this.getArticulos().get("tipofrecuencia_"+i).toString());
					if(tipoFrec==ConstantesBD.codigoTipoFrecuenciaHoras)
						dtoArticulos.setTipoFrecuencia(ConstantesBD.nombreTipoFrecuenciaHoras);
					else if(tipoFrec==ConstantesBD.codigoTipoFrecuenciaMinutos)
						dtoArticulos.setTipoFrecuencia(ConstantesBD.nombreTipoFrecuenciaMinutos);
					else 
						dtoArticulos.setTipoFrecuencia(ConstantesBD.nombreTipoFrecuenciaDias);
					dtoArticulos.setFrecuencia(Utilidades.convertirAEntero(this.getArticulos().get("frecuencia_"+i).toString()));
					dtoArticulos.setVia(Utilidades.obtenerNombreViaAdministracion(con,Utilidades.convertirAEntero(this.getArticulos().get("via_"+i).toString())));
					dtoArticulos.setDiasTratamiento(Utilidades.convertirALong(this.getArticulos().get("duraciontratamiento_"+i).toString()));
				
					boolean esMedicamento=Boolean.parseBoolean(this.getArticulos().get("medicamento_"+i).toString());
					if(esMedicamento){
						dtoArticulos.setEsMedicamento(ConstantesBD.acronimoSiChar);
					}else{
						dtoArticulos.setEsMedicamento(ConstantesBD.acronimoNoChar);
					}
					DtoArticulos dtoArticuloDao = articuloDao.consultarArticuloPorID(dtoArticulos.getCodigoArticulo());
					dtoArticulos.setNaturalezaArticulo(dtoArticuloDao.getNaturalezaArticulo());
					dtoArticulos.setNombreNaturaleza(dtoArticuloDao.getNombreNaturaleza());
					listaArticulos.add(dtoArticulos);
				}	
			}
			listaValidacionGeneracionAutorizacionCapitada.get(0).setArticulos(listaArticulos);
		}
		try {
			//Se hace llamado al proceso de Generación Capitacion DCU-966
			//TODO
			listaAutorizacionesCapitacion = validacionGeneracionAutorizacionCapitadaMundo.generarValidacionGeneracionAutorizacionCapitada
					(listaValidacionGeneracionAutorizacionCapitada, usuario);
		} catch (IPSException ipse) {
			Log4JManager.error(ipse.getMessage(), ipse);
			throw ipse;
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaAutorizacionesCapitacion;
	}



	/**
	 * @return the dtoValidaciones
	 */
	public ArrayList<DtoValidacionGeneracionAutorizacionCapitada> getDtoValidaciones() {
		return dtoValidaciones;
	}



	/**
	 * @param dtoValidaciones the dtoValidaciones to set
	 */
	public void setDtoValidaciones(
			ArrayList<DtoValidacionGeneracionAutorizacionCapitada> dtoValidaciones) {
		this.dtoValidaciones = dtoValidaciones;
	}



	/**
	 * @return the codigoCitaAsociada
	 */
	public int getCodigoCitaAsociada() {
		return codigoCitaAsociada;
	}



	/**
	 * @param codigoCitaAsociada the codigoCitaAsociada to set
	 */
	public void setCodigoCitaAsociada(int codigoCitaAsociada) {
		this.codigoCitaAsociada = codigoCitaAsociada;
	}


	/**
	 * Metodo de negocio que retorna Lista de {@link InfoArticuloOrdenAmbulatoriaDto} con los detalles de los articulos segun MT 3851 (DCU 307 v 2.21).
	 * @param con
	 * @param codigoOrden
	 * @param esMedicamento
	 * @param codigoInstitucion
	 * @author javrammo
	 * @return
	 * @throws Exception
	 */
	public List<InfoArticuloOrdenAmbulatoriaDto> detalleToolTipArticulosOrdenAmbulatoria(Connection con, int codigoOrden, boolean esMedicamento, int codigoInstitucion) throws IPSException 
	{		
		
		try {
			//Consulta el parametro Código Manual Estándar Búsqueda y Presentación de Artículos
			boolean mostrarCodigoInterfaz = false;
			if(ConstantesIntegridadDominio.acronimoInterfaz.equals(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(codigoInstitucion))){
				mostrarCodigoInterfaz = true;
			}
			List<InfoArticuloOrdenAmbulatoriaDto> detalleToolTipArticulosOrdenAmbulatoria = objetoDao.detalleToolTipArticulosOrdenAmbulatoria(con,codigoOrden, esMedicamento);
			
			/**
			 * MT 6867
			 * @author javrammo
			 * Se agrega orden ascendentemente por la descripción del medicamento asociado a la orden ambulatoria.
			 * por BD no se puedo ya que el ordenamiento es diferente.
			 */
			SortGenerico ordenamiento = new SortGenerico("Descripion", true);
			Collections.sort(detalleToolTipArticulosOrdenAmbulatoria, ordenamiento);			
			/**
			 * fin MT 6867
			 */			
			
			if(detalleToolTipArticulosOrdenAmbulatoria!= null && !detalleToolTipArticulosOrdenAmbulatoria.isEmpty()){			
				for (InfoArticuloOrdenAmbulatoriaDto infoArticuloOrdenAmbulatoriaDto : detalleToolTipArticulosOrdenAmbulatoria) {
					if(mostrarCodigoInterfaz && !UtilidadTexto.isEmpty(infoArticuloOrdenAmbulatoriaDto.getCodigoInterfaz())){
						infoArticuloOrdenAmbulatoriaDto.setCodigoTooltip(infoArticuloOrdenAmbulatoriaDto.getCodigoInterfaz());
					}
					else{
						infoArticuloOrdenAmbulatoriaDto.setCodigoTooltip(infoArticuloOrdenAmbulatoriaDto.getCodigoAxioma());
					}
					
					//Se arma la descripcion del tooltip según DCU 307 v 2.21
					StringBuffer descripcionTooltip = new StringBuffer("");
					descripcionTooltip.append(infoArticuloOrdenAmbulatoriaDto.getCodigoTooltip());
					descripcionTooltip.append(" - " +  infoArticuloOrdenAmbulatoriaDto.getDescripion());				
					if(esMedicamento){
						descripcionTooltip.append(infoArticuloOrdenAmbulatoriaDto.getConcentracion() != null ? (" " +mensajes.getMessage("ordenesAmbulatorias.tooltip.concentracion")+infoArticuloOrdenAmbulatoriaDto.getConcentracion()) : "");
						descripcionTooltip.append(" " +mensajes.getMessage("ordenesAmbulatorias.tooltip.formafarmaceutica")+infoArticuloOrdenAmbulatoriaDto.getFormaFarmaceutica());
						descripcionTooltip.append(" " +mensajes.getMessage("ordenesAmbulatorias.tooltip.unidadMedida") +infoArticuloOrdenAmbulatoriaDto.getUnidadDeMedida());
					}				
					infoArticuloOrdenAmbulatoriaDto.setDescripcionCompletaTooltip(descripcionTooltip.toString().toUpperCase());				
				}			
			}
					
			return detalleToolTipArticulosOrdenAmbulatoria;
		}catch (IPSException e) {
			Log4JManager.error(e.getMessage(),e);
			throw e;
		} 
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}

	}
	
}
