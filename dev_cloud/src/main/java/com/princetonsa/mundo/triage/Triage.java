/*
 * Created on Feb 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.triage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TriageDao;
import com.princetonsa.dto.historiaClinica.DtoClasificacionesTriage;
import com.princetonsa.dto.historiaClinica.DtoTriage;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * @author sebastián gómez rivillas
 *
 * Gestiona el mundo de la funcionalidad triage
 */
public class Triage 
{
	//********************** ATRIBUTOS **************************
	
	private static TriageDao triageDao = null;
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Triage.class);
	
	/**
	 * Mapa para insertar los datos del triage
	 */
	HashMap datosTriage = new HashMap();
	/**
	 * <code>consecutivo</code> del triage (parte entera)
	 */
	private String consecutivo;
	/**
	 * <code>consecutivo_fecha</code> del triage (parte de la fecha)
	 */
	private String consecutivo_fecha;
	/**
	 * <code>fecha</code> del sistema
	 */
	private String fecha;
	/**
	 * <code>hora</code> del sistema
	 */
	private String hora;
	/**
	 * <code>primer_nombre</code> del paciente
	 */
	private String primer_nombre;
	/**
	 * <code>segundo_nombre</code> del paciente
	 */
	private String segundo_nombre;
	/**
	 * <code>primer_apellido</code> del paciente
	 */
	private String primer_apellido;
	/**
	 * <code>segundo_apellido</code> del paciente
	 */
	private String segundo_apellido;
	
	/**
	 * <code>fecha_nacimiento</code> del paciente
	 */
	private String fecha_nacimiento;
	/**
	 * <code>tipo_id</code>, acronimo y nombre del tipo de identificacion
	 */
	private InfoDatos tipo_id;
	/**
	 * <code>numero_id</code>, numero de identificacion
	 */
	private String numero_id;
	/**
	 * <code>convenio</code>, codigo de convenio y descripcion
	 */
	private InfoDatos convenio;
	/**
	 *  <code>otro_conveio</code> otro convenio (opcional)
	 */
	private String otro_convenio;
	/**
	 * <code>accidenteTrabajo</code>
	 */
	private String accidenteTrabajo;
	
	
	/**
	 * 
	 */
	private int codigoClasificacionTriage;
	
	/**
	 * 
	 */
	private String descripcionClasificacionTriage;
	/**
	 * <code>arpAfiliado</code>
	 */
	private InfoDatos arpAfiliado;
	/**
	 * <code>tipo_afiliado</code> acrónimo y descripción de afiliado
	 */
	private InfoDatos tipo_afiliado;
	/**
	 * <code>id_cotizante</code>, identificación cotizante en caso de que sea beneficiario
	 */
	private String id_cotizante;
	/**
	 * <code>motivo_consulta</code>
	 */
	private String motivo_consulta;
	/**
	 * <code>categorias_triage</code> , codigo y descripción de la categoría dle triage
	 */
	private InfoDatos categorias_triage;
	/**
	 *  <code>antecedentes</code>
	 */
	private String antecedentes;
	/**
	 * <code>destino</code>, codigo y descirpción del destino del paciente
	 */
	private InfoDatos destino;
	/**
	 *<code>observaciones_generales</code>
	 */
	private String observaciones_generales;
	/**
	 * <code>login</code> del usuario
	 */
	private String login;
	/**
	 *  <code>usuario</code>, profesional de la salud, Registro / especialidades
	 */
	private String usuario;
	/** SIGNOS VITALES DEL TRIAGE*/
	/**
	 * Mapa para almacenar los signos vitales
	 */
	private HashMap signosVitales = new HashMap();
	
	/**
	 * Número de signos vitales
	 */
	private int numSignos;
	/**
	 * Colección de <code>triage</code>
	 */
	private Collection triage;
	
	private int codigoClasificacion;
	private int codigoDestino;
	private String admision;
	private int codigoSala;
	private String colornombre;
	private String signosintoma;
	private String nombresala;
	private String nombreCentroAtencion;
	private boolean existeAdmision;
	private String noRespondeLlamado;
	//*********************CONSTRUCTORES E INICIALIZADOES*****************************************************
	/**
	 * Constructor usado para otros
	 */
	public Triage(){
			this.clean();
			this.init(System.getProperty("TIPOBD"));
		}
	/**
	 * Limpia los datos
	 */
	public void clean()
	{
			this.datosTriage = new HashMap();
			this.setConsecutivo("");
			this.setConsecutivo_fecha("");
			this.setFecha("");
			this.setHora("");
			this.setPrimer_nombre("");
			this.setSegundo_nombre("");
			this.setPrimer_apellido("");
			this.setSegundo_apellido("");
			this.setFecha_nacimiento("");
			this.setTipo_id(new InfoDatos("",""));
			this.setNumero_id("");
			this.setConvenio(new InfoDatos(0,""));
			this.setOtro_convenio("");
			this.setAccidenteTrabajo("");
			this.codigoClasificacionTriage=ConstantesBD.codigoNuncaValido;
			this.descripcionClasificacionTriage="";
			this.setArpAfiliado(new InfoDatos(0,""));
			this.setTipo_afiliado(new InfoDatos("",""));
			this.setId_cotizante("");
			this.setMotivo_consulta("");
			this.setCategorias_triage(new InfoDatos(0,""));
			this.setAntecedentes("");
			this.setDestino(new InfoDatos(0,""));
			this.setObservaciones_generales("");
			this.setLogin("");
			this.setUsuario("");
			//SIGNOS VITALES
			this.setSignosVitales(new HashMap());
			this.setNumSignos(0);
			this.setNombreCentroAtencion("");
			
		}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (triageDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			triageDao = myFactory.getTriageDao();
		}
		
		
		
	}
	
	/**
	 * Método para obtener el DAO del Triage
	 * @return
	 */
	public static TriageDao triageDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTriageDao();
	}
	//*********************METODOS*****************************************************
	
	
	/**
	 * @return Returns the antecedentes.
	 */
	public String getAntecedentes() {
		return antecedentes;
	}
	/**
	 * @param antecedentes The antecedentes to set.
	 */
	public void setAntecedentes(String antecedentes) {
		this.antecedentes = antecedentes;
	}
	/**
	 * @return Returns the categorias_triage.
	 */
	public InfoDatos getCategorias_triage() {
		return categorias_triage;
	}
	/**
	 * @param categorias_triage The categorias_triage to set.
	 */
	public void setCategorias_triage(InfoDatos categorias_triage) {
		this.categorias_triage = categorias_triage;
	}
	/**
	 * @return Returns the consecutivo.
	 */
	public String getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return Returns the consecutivo_fecha.
	 */
	public String getConsecutivo_fecha() {
		return consecutivo_fecha;
	}
	/**
	 * @param consecutivo_fecha The consecutivo_fecha to set.
	 */
	public void setConsecutivo_fecha(String consecutivo_fecha) {
		this.consecutivo_fecha = consecutivo_fecha;
	}
	/**
	 * @return Returns the convenio.
	 */
	public InfoDatos getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(InfoDatos convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the destino.
	 */
	public InfoDatos getDestino() {
		return destino;
	}
	/**
	 * @param destino The destino to set.
	 */
	public void setDestino(InfoDatos destino) {
		this.destino = destino;
	}
	
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the fecha_nacimiento.
	 */
	public String getFecha_nacimiento() {
		return fecha_nacimiento;
	}
	/**
	 * @param fecha_nacimiento The fecha_nacimiento to set.
	 */
	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}
	/**
	 * @return Returns the hora.
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @param hora The hora to set.
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	/**
	 * @return Returns the id_cotizante.
	 */
	public String getId_cotizante() {
		return id_cotizante;
	}
	/**
	 * @param id_cotizante The id_cotizante to set.
	 */
	public void setId_cotizante(String id_cotizante) {
		this.id_cotizante = id_cotizante;
	}
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return Returns the motivo_consulta.
	 */
	public String getMotivo_consulta() {
		return motivo_consulta;
	}
	/**
	 * @param motivo_consulta The motivo_consulta to set.
	 */
	public void setMotivo_consulta(String motivo_consulta) {
		this.motivo_consulta = motivo_consulta;
	}
	/**
	 * @return Returns the numero_id.
	 */
	public String getNumero_id() {
		return numero_id;
	}
	/**
	 * @param numero_id The numero_id to set.
	 */
	public void setNumero_id(String numero_id) {
		this.numero_id = numero_id;
	}
	/**
	 * @return Returns the observaciones_generales.
	 */
	public String getObservaciones_generales() {
		return observaciones_generales;
	}
	/**
	 * @param observaciones_generales The observaciones_generales to set.
	 */
	public void setObservaciones_generales(String observaciones_generales) {
		this.observaciones_generales = observaciones_generales;
	}
	
	/**
	 * @return Returns the primer_apellido.
	 */
	public String getPrimer_apellido() {
		return primer_apellido;
	}
	/**
	 * @param primer_apellido The primer_apellido to set.
	 */
	public void setPrimer_apellido(String primer_apellido) {
		this.primer_apellido = primer_apellido;
	}
	/**
	 * @return Returns the primer_nombre.
	 */
	public String getPrimer_nombre() {
		return primer_nombre;
	}
	/**
	 * @param primer_nombre The primer_nombre to set.
	 */
	public void setPrimer_nombre(String primer_nombre) {
		this.primer_nombre = primer_nombre;
	}
	/**
	 * @return Returns the segundo_apellido.
	 */
	public String getSegundo_apellido() {
		return segundo_apellido;
	}
	/**
	 * @param segundo_apellido The segundo_apellido to set.
	 */
	public void setSegundo_apellido(String segundo_apellido) {
		this.segundo_apellido = segundo_apellido;
	}
	/**
	 * @return Returns the segundo_nombre.
	 */
	public String getSegundo_nombre() {
		return segundo_nombre;
	}
	/**
	 * @param segundo_nombre The segundo_nombre to set.
	 */
	public void setSegundo_nombre(String segundo_nombre) {
		this.segundo_nombre = segundo_nombre;
	}
	
	/**
	 * @return Returns the tipo_afiliado.
	 */
	public InfoDatos getTipo_afiliado() {
		return tipo_afiliado;
	}
	/**
	 * @param tipo_afiliado The tipo_afiliado to set.
	 */
	public void setTipo_afiliado(InfoDatos tipo_afiliado) {
		this.tipo_afiliado = tipo_afiliado;
	}
	/**
	 * @return Returns the tipo_id.
	 */
	public InfoDatos getTipo_id() {
		return tipo_id;
	}
	/**
	 * @param tipo_id The tipo_id to set.
	 */
	public void setTipo_id(InfoDatos tipo_id) {
		this.tipo_id = tipo_id;
	}
	/**
	 * @return Returns the triage.
	 */
	public Collection getTriage() {
		return triage;
	}
	/**
	 * @param triage The triage to set.
	 */
	public void setTriage(Collection triage) {
		this.triage = triage;
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
	 * @return Returns the otro_convenio.
	 */
	public String getOtro_convenio() {
		return otro_convenio;
	}
	/**
	 * @param otro_convenio The otro_convenio to set.
	 */
	public void setOtro_convenio(String otro_convenio) {
		this.otro_convenio = otro_convenio;
	}
	
	
	//******************METODOS DE OPERACION *******************************************
	/**
	 * Función usada para cargar el reporte del triage
	 * @param con
	 * @param fechainicial
	 * @param fechafinal
	 * @param usuario
	 * @param responsable
	 * @param centroAtencion
	 * @return
	 */
	public Collection reporteTriage(Connection con, String fechainicial, String fechafinal, String usuario, String responsable,int centroAtencion){
			return triageDao.reporteTriage(con,fechainicial,fechafinal,usuario,responsable,centroAtencion);
		}
	/**
	 * Función usada para actualizar un registro triage en el cual se le añade solamente una
	 * nuava observación general
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @param observacion
	 * @return
	 */
	public int actualizarTriage(Connection con,String consecutivo,String consecutivo_fecha,String observacion, String noRespondioLLamado){
			
			return triageDao.actualizarTriage(con,consecutivo,consecutivo_fecha,observacion, noRespondioLLamado);
		}
	/**
	 * Función qeu obtiene el detalle de un triage específico
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @return
	 */
	public Triage cargarTriage(Connection con,String consecutivo,String consecutivo_fecha)
	{
		try
		{
			this.triage=triageDao.cargarTriage(con,consecutivo,consecutivo_fecha);
			if(	this.triage == null)
				return null;
			else
			{
				
				Iterator iterador=this.triage.iterator();
				
				Triage tri=new Triage();
				
				if(iterador.hasNext())
				{
					HashMap TriageBD=(HashMap)iterador.next();
					tri.setConsecutivo(TriageBD.get("consecutivo")+"");
					tri.setConsecutivo_fecha(TriageBD.get("consecutivo_fecha")+"");
					tri.setFecha(TriageBD.get("fecha")+"");
					tri.setHora(TriageBD.get("hora")+"");
					tri.setPrimer_nombre(TriageBD.get("primer_nombre")+"");
					tri.setSegundo_nombre(TriageBD.get("segundo_nombre")+"");
					tri.setPrimer_apellido(TriageBD.get("primer_apellido")+"");
					tri.setSegundo_apellido(TriageBD.get("segundo_apellido")+"");
					
					tri.setFecha_nacimiento(TriageBD.get("fecha_nacimiento")+"");
					tri.setTipo_id(new InfoDatos(TriageBD.get("acro_tipo_identificacion")+"",TriageBD.get("tipo_identificacion")+""));
					tri.setNumero_id(TriageBD.get("numero_identificacion")+"");
					tri.setConvenio(new InfoDatos(0,TriageBD.get("convenio")==null?"":TriageBD.get("convenio")+""));
					tri.setOtro_convenio(TriageBD.get("otro_convenio")==null?"":TriageBD.get("otro_convenio")+"");
					tri.setTipo_afiliado(new InfoDatos("",TriageBD.get("tipo_afiliado")==null?"":TriageBD.get("tipo_afiliado")+""));
					tri.setId_cotizante(TriageBD.get("id_cotizante")+"");
					tri.setMotivo_consulta(TriageBD.get("motivo_consulta")+"");
					tri.setCategorias_triage(new InfoDatos(Integer.parseInt(TriageBD.get("numero_triage")+""),TriageBD.get("categoria_triage")+""));
					tri.setAntecedentes(TriageBD.get("antecedentes")+"");
					tri.setDestino(new InfoDatos(0,TriageBD.get("destino")+""));
					tri.setObservaciones_generales(TriageBD.get("observaciones_generales")+"");
					tri.setLogin(TriageBD.get("login")+"");
					tri.setUsuario(TriageBD.get("usuario")+"");
					
					tri.setColornombre(TriageBD.get("colornombre")+"");
					tri.setSignosintoma(TriageBD.get("signosintoma")+"");
					tri.setNombresala(TriageBD.get("nombresala")+"");
					tri.setExisteAdmision(UtilidadValidacion.existeAdmisionParaTriage(con, consecutivo, consecutivo_fecha, Integer.parseInt(TriageBD.get("institucion")+"")));
					tri.setNoRespondeLlamado(TriageBD.get("no_responde_llamado")+"");
					
					///Se cargan los signos vitales
					tri.setSignosVitales(triageDao.cargarSignosVitalesTriage(con,tri.getConsecutivo(),tri.getConsecutivo_fecha()));
					tri.setNumSignos(Integer.parseInt(tri.getSignosVitales("numRegistros").toString()));
					
					tri.setAccidenteTrabajo(TriageBD.get("accidente_trabajo").toString());
					tri.setCodigoArpAfiliado(TriageBD.get("codigo_convenio_arp").toString().equals("")?0:Integer.parseInt(TriageBD.get("codigo_convenio_arp").toString()));
					tri.setNombreArpAfiliado(TriageBD.get("nombre_convenio_arp").toString());
					
				}

				return tri;
			}
		}
		catch(Exception e)
		{
			logger.warn("Error en la consulta de un detalle de triage en el Mundo Triage " +e.toString());
			return null;
		}

	}
	/**
	 * Función usada para cargar todos los triage del sistema
	 * @param con
	 * @return
	 */
	public Collection cargarTriage(Connection con)
	{
		return triageDao.resumenTriage(con,"");
	}
	
	/**
	 * Metodo para buscar un triage por medio de busqueda avanzada
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCalificacion
	 * @param codigoDestino
	 * @param admision
	 * @param codigoSala
	 * @param centroAtencion
	 * @return
	 */
	public Collection buscarTriage(Connection con, String fechaInicial, String fechaFinal, int codigoCalificacion, int codigoDestino, String admision, int codigoSala, int centroAtencion )
	{
		return triageDao.buscarTriage(con, fechaInicial, fechaFinal, codigoCalificacion, codigoDestino, admision, codigoSala, centroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoIdentificacionPersona
	 * @param numeroIdentificacionPersona
	 * @return
	 */
	public Collection buscarTriage(Connection con,
			String tipoIdentificacionPersona, String numeroIdentificacionPersona) {
		return triageDao.buscarTriage(con, tipoIdentificacionPersona,numeroIdentificacionPersona);

	}
	
	
	/**
	 * Consulta e triage recién insertado para mostrarlo como resumen
	 * @param con
	 * @return
	 */
	public void resumenTriage(Connection con,String consecutivo){
			
			try
			{	
				
					this.triage=triageDao.resumenTriage(con,consecutivo);
					Iterator iterador=this.triage.iterator();
					
					if(iterador.hasNext())
					{
						
						HashMap TriageBD=(HashMap)iterador.next();
						
						this.setConsecutivo(TriageBD.get("consecutivo")+"");
						this.setConsecutivo_fecha(TriageBD.get("consecutivo_fecha")+"");
						this.setFecha(TriageBD.get("fecha")+"");
						this.setHora(TriageBD.get("hora")+"");
						this.setPrimer_nombre(TriageBD.get("primer_nombre")+"");
						this.setSegundo_nombre(TriageBD.get("segundo_nombre")+"");
						this.setPrimer_apellido(TriageBD.get("primer_apellido")+"");
						this.setSegundo_apellido(TriageBD.get("segundo_apellido")+"");
						this.setFecha_nacimiento(TriageBD.get("fecha_nacimiento")+"");
						this.setTipo_id(new InfoDatos(TriageBD.get("acro_tipo_identificacion")+"",TriageBD.get("tipo_identificacion")+""));
						this.setNumero_id(TriageBD.get("numero_identificacion")+"");
						this.setConvenio(new InfoDatos(0,TriageBD.get("convenio")==null?"":TriageBD.get("convenio")+""));
						this.setOtro_convenio(TriageBD.get("otro_convenio")==null?"":TriageBD.get("otro_convenio")+"");
						this.setTipo_afiliado(new InfoDatos("",TriageBD.get("tipo_afiliado")==null?"":TriageBD.get("tipo_afiliado")+""));
						this.setId_cotizante(TriageBD.get("id_cotizante")+"");
						this.setMotivo_consulta(TriageBD.get("motivo_consulta")+"");
						this.setCategorias_triage(new InfoDatos(Integer.parseInt(TriageBD.get("numero_triage")+""),TriageBD.get("categoria_triage")+""));
						this.setAntecedentes(TriageBD.get("antecedentes")+"");
						this.setDestino(new InfoDatos(0,TriageBD.get("destino")+""));
						this.setObservaciones_generales(TriageBD.get("observaciones_generales")+"");
						this.setLogin(TriageBD.get("login")+"");
						this.setUsuario(TriageBD.get("usuario")+"");
						
						this.setNombresala(TriageBD.get("sala")+"");
						this.setSignosintoma(TriageBD.get("signo_sintoma")+"");
						this.setColornombre(TriageBD.get("colornombre")+"");
						this.setNombreCentroAtencion(TriageBD.get("centro_atencion").toString());
						
						//Se cargan los signos vitales
						this.setSignosVitales(triageDao.cargarSignosVitalesTriage(con,this.consecutivo,this.consecutivo_fecha));
						this.setNumSignos(Integer.parseInt(this.getSignosVitales("numRegistros").toString()));
						
						this.setAccidenteTrabajo(TriageBD.get("accidente_trabajo").toString());
						this.setCodigoArpAfiliado(TriageBD.get("codigo_convenio_arp").toString().equals("")?0:Integer.parseInt(TriageBD.get("codigo_convenio_arp").toString()));
						this.setNombreArpAfiliado(TriageBD.get("nombre_convenio_arp").toString());
						this.setCodigoClasificacionTriage(Utilidades.convertirAEntero(TriageBD.get("codigoclastriage")+""));
						this.setDescripcionClasificacionTriage(TriageBD.get("descclastriage")+"");
					}

				
			}
			catch(Exception e)
			{
				logger.warn("Error en la consulta de un resumen triage en el Mundo Triage " +e.toString());
				
			}
		}
	/**
	 * Función usada para hacer unsa inserción de Triage
	 * @param con
	 * @param tri
	 * @param institucion
	 * @return
	 */
	public int insertarTriage(Connection con)
	{
		int bandera=1; //usada para verificar que la transacción haya sido exitosa (0 fallido, 1 éxito)
		
		int revision=triageDao.insertarTriage(con,this.datosTriage,ConstantesBD.continuarTransaccion);
		
		if(revision==1)
		{
			String consec=this.getDatosTriage("consecutivo").toString();
			
			//INSERCIÓN DE LOS SIGNOS VITALES
			int revision2 = 0;
			for(int i=0;i<this.numSignos;i++)
			{
				revision2 = triageDao.insertarSignosVitalesTriage(
					con,
					Integer.parseInt(this.getSignosVitales("codigo_"+i).toString()),
					consec,
					this.getSignosVitales("valor_"+i).toString(),ConstantesBD.continuarTransaccion);
				if(revision2<=0)
					bandera = 0;
			}
			
		}
			
		else
		{
			bandera=0;
			
				
		}
		
		return bandera;
	}
	
	/**
	 * Método implementado para actualizar el estado del paciente que estuvo
	 * registrado para Triage cambiando su estado a atendido e ingresando su consecutivo triage
	 * respectivo
	 * @param con
	 * @param codigo
	 * @param consecutivoTriage
	 * @return
	 */
	public int actualizarPacienteParaTriage(Connection con,String codigo,String consecutivoTriage)
	{
		return triageDao.actualizarPacienteParaTriage(con,codigo,consecutivoTriage);
	}
	
	/**
	 * 
	 * @param codigoCuenta
	 */
	public static DtoTriage consultarInfoResumenTriagePorCuenta(int codigoCuenta)
	{
		return triageDao().consultarInfoResumenTriagePorCuenta(codigoCuenta);
	}
	
	/**
	 * Método que consulta los datos del triage
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoTriage obtenerDatosTriage(Connection con,String consecutivoTriage,String consecutivoFechaTriage)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivoTriage", consecutivoTriage);
		campos.put("consecutivoFechaTriage", consecutivoFechaTriage);
		return triageDao().obtenerDatosTriage(con, campos);
	}
	
	/**
	 * @return Returns the admision.
	 */
	public String getAdmision()
	{
		return admision;
	}
	/**
	 * @param admision The admision to set.
	 */
	public void setAdmision(String admision)
	{
		this.admision=admision;
	}
	/**
	 * @return Returns the codigoClasificacion.
	 */
	public int getCodigoClasificacion()
	{
		return codigoClasificacion;
	}
	/**
	 * @param codigoClasificacion The codigoClasificacion to set.
	 */
	public void setCodigoClasificacion(int codigoClasificacion)
	{
		this.codigoClasificacion=codigoClasificacion;
	}
	/**
	 * @return Returns the codigoDestino.
	 */
	public int getCodigoDestino()
	{
		return codigoDestino;
	}
	/**
	 * @param codigoDestino The codigoDestino to set.
	 */
	public void setCodigoDestino(int codigoDestino)
	{
		this.codigoDestino=codigoDestino;
	}
	/**
	 * @return Returns the codigoSala.
	 */
	public int getCodigoSala()
	{
		return codigoSala;
	}
	/**
	 * @param codigoSala The codigoSala to set.
	 */
	public void setCodigoSala(int codigoSala)
	{
		this.codigoSala=codigoSala;
	}
	/**
	 * @return Returns the colornombre.
	 */
	public String getColornombre()
	{
		return colornombre;
	}
	/**
	 * @param colornombre The colornombre to set.
	 */
	public void setColornombre(String colornombre)
	{
		this.colornombre=colornombre;
	}
	/**
	 * @return Returns the signosintoma.
	 */
	public String getSignosintoma()
	{
		return signosintoma;
	}
	/**
	 * @param signosintoma The signosintoma to set.
	 */
	public void setSignosintoma(String signosintoma)
	{
		this.signosintoma=signosintoma;
	}
	/**
	 * @return Returns the nombresala.
	 */
	public String getNombresala()
	{
		return nombresala;
	}
	/**
	 * @param nombresala The nombresala to set.
	 */
	public void setNombresala(String nombresala)
	{
		this.nombresala=nombresala;
	}
	/**
	 * @return Returns the existeAdmision.
	 */
	public boolean isExisteAdmision()
	{
		return existeAdmision;
	}
	/**
	 * @param existeAdmision The existeAdmision to set.
	 */
	public void setExisteAdmision(boolean existeAdmision)
	{
		this.existeAdmision=existeAdmision;
	}
	/**
	 * @return Returns the datosTriage.
	 */
	public HashMap getDatosTriage() {
		return datosTriage;
	}
	/**
	 * @param datosTriage The datosTriage to set.
	 */
	public void setDatosTriage(HashMap datosTriage) {
		this.datosTriage = datosTriage;
	}
	/**
	 * @return Retorna un elemento del mapa datosTriage.
	 */
	public Object getDatosTriage(String key) {
		return datosTriage.get(key);
	}
	/**
	 * @param asigna un elemento al mapa datosTriage .
	 */
	public void setDatosTriage(String key,Object obj) {
		this.datosTriage.put(key,obj);
	}
	/**
	 * @return Returns the numSignos.
	 */
	public int getNumSignos() {
		return numSignos;
	}
	/**
	 * @param numSignos The numSignos to set.
	 */
	public void setNumSignos(int numSignos) {
		this.numSignos = numSignos;
	}
	/**
	 * @return Returns the signosVitales.
	 */
	public HashMap getSignosVitales() {
		return signosVitales;
	}
	/**
	 * @param signosVitales The signosVitales to set.
	 */
	public void setSignosVitales(HashMap signosVitales) {
		this.signosVitales = signosVitales;
	}
	/**
	 * @return Retorna un elemento del mapa signosVitales.
	 */
	public Object getSignosVitales(String key) {
		return signosVitales.get(key);
	}
	/**
	 * @param signosVitales The signosVitales to set.
	 */
	public void setSignosVitales(String key,Object obj) 
	{
		this.signosVitales.put(key,obj);
	}
	/**
	 * @return Returns the noRespondeLlamado.
	 */
	public String getNoRespondeLlamado()
	{
		return noRespondeLlamado;
	}
	/**
	 * @param noRespondeLlamado The noRespondeLlamado to set.
	 */
	public void setNoRespondeLlamado(String noRespondeLlamado)
	{
		this.noRespondeLlamado=noRespondeLlamado;
	}
	/**
	 * @return Returns the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}
	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the arpAfiliado
	 */
	public InfoDatos getArpAfiliado() {
		return arpAfiliado;
	}
	/**
	 * @param arpAfiliado the arpAfiliado to set
	 */
	public void setArpAfiliado(InfoDatos arpAfiliado) {
		this.arpAfiliado = arpAfiliado;
	}
	/**
	 * @return retorna el codigo del arp afiliado
	 */
	public int getCodigoArpAfiliado() {
		return arpAfiliado.getCodigo();
	}
	/**
	 * @param Asigna codigo del arpAfiliado 
	 */
	public void setCodigoArpAfiliado(int codigo) {
		this.arpAfiliado.setCodigo(codigo);
	}
	/**
	 * @return retorna el nombre del arp afiliado
	 */
	public String getNombreArpAfiliado() {
		return arpAfiliado.getNombre();
	}
	/**
	 * @param Asigna nombre del arpAfiliado 
	 */
	public void setNombreArpAfiliado(String nombre) {
		this.arpAfiliado.setValue(nombre);
	}
	/**
	 * @return the accidenteTrabajo
	 */
	public String getAccidenteTrabajo() {
		return accidenteTrabajo;
	}
	/**
	 * @param accidenteTrabajo the accidenteTrabajo to set
	 */
	public void setAccidenteTrabajo(String accidenteTrabajo) {
		this.accidenteTrabajo = accidenteTrabajo;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ArrayList<DtoClasificacionesTriage> consultarClasificacionesTriage() 
	{
		return triageDao().consultarClasificacionesTriage();
	}
	public int getCodigoClasificacionTriage() {
		return codigoClasificacionTriage;
	}
	public void setCodigoClasificacionTriage(int codigoClasificacionTriage) {
		this.codigoClasificacionTriage = codigoClasificacionTriage;
	}
	public String getDescripcionClasificacionTriage() {
		return descripcionClasificacionTriage;
	}
	public void setDescripcionClasificacionTriage(
			String descripcionClasificacionTriage) {
		this.descripcionClasificacionTriage = descripcionClasificacionTriage;
	}
	
}
