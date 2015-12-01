/*
 * 22 de Oct 2005
 * 
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PeticionDao;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadTexto;

/**
 * @author Sebastián Gómez
 *
 * Clase que representa una Petición Qx para las funcionalidades
 * Generar - Consultar - Modificar - Anular Petición
 */
public class Peticion {
	
	/**
	 * DAO para el manejo de Peticiones de Cirugía
	 */
	private PeticionDao peticionDao = null;
	
	//**************ATRIBUTOS **************************************************+
	
	/**
	 * Número de la petición
	 */
	private int numeroPeticion;
	
	/**
	 * Datos del paciente de la petición
	 */
	private InfoDatos paciente;
	
	/**
	 * Datos del tipo de Paciente
	 */
	private InfoDatos tipoPaciente;
	
	/**
	 * Fecha de la petición
	 */
	private String fecha;
	
	/**
	 * Hora de la petición
	 */
	private String hora;
	
	/**
	 * duración aproximada de la cirugía
	 */
	private String duracion;
	
	/**
	 * Fecha de la cirugía
	 */
	private String fechaCirugia;
	
	/**
	 * Datos del médico que solicita
	 */
	private InfoDatos medicoSolicitante;
	
	/**
	 * Login del usuario que realiza la petición
	 */
	private String usuario;
	
	/**
	 * Estado de la petición
	 */
	private int estado;
	
	/**
	 * Requiere UCI?
	 */
	private String requiere_uci;
	
	/**
	 * Número del Pedido
	 */
	private int numeroPedido;
	
	/**
	 * Mapa de los servicios de la petición
	 */
	private HashMap serviciosMap = new HashMap();
	
	/**
	 * Número de registros del mapa serviciosMap
	 */
	private int numServicios;
	
	/** 
	 * Mapa para manejar la consulta general y la consulta
	 * de peticiones por paciente  
	 */
	private HashMap mapaConsultaPeticiones;
	
	
	/**
	 * Mapa de	filtros para las consultas Sql
	 * */
	private HashMap filtrosMap;
	

	
	/**
	 * Número ingreso
	 */
	private String numeroIngreso;
	
	//********CAMPOS PARA LA CONSULTA DE PETICIONES  *********
	/**
	 * Numero del Servicio inicial (consecutivo)
	 */
	private int nroIniServicio;

	/**
	 * Numero del Servicio Final (consecutivo)
	 */
	private int nroFinServicio;
	
	
	/**
	 * Fecha inicial peticion del servicio
	 */
	private String fechaIniPeticion;
	
	/**
	 * Fecha final de peticion del servicio 
	 */
	private String fechaFinPeticion;
	
	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaIniCirugia;

	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaFinCirugia;
	
	/**
	 * Medico que solicita la peticion 
	 */
	private int profesional;
	
	/**
	 * Estado de la peticion
	 */
	private int estadoPeticion;
	

	/**
	 * Variable para almacenar el Centro de atencion.
	 */
    private int centroAtencion; 
    
    /**
     * Nombre del estado de la petición
     */
    private String nombreEstado;
    
    /**
     * Programable
     * */
    private String programable;
	
	//********CAMPOS VINCULADOS CON LA SOLICITUD DE LA PETICIÓN *******
	
	/**
	 * Consecutivo de Órden Médica de la solicitud asociada a la petición
	 */
	private int orden;
	
	/**
	 * Médico que respondió la solicitud 
	 */
	private InfoDatos medicoResponde;
	
	//*********CONSTRUCTORES E INICIALIZADORES*********************************
	
	/**
	 * Constructor
	 *
	 */
	public Peticion()
	{
		
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Se inicializan los datos
	 *
	 */
	public void clean()
	{
		this.numeroPeticion = 0;
		this.paciente = new InfoDatos(0,"");
		this.tipoPaciente = new InfoDatos("","");
		this.fecha = "";
		this.hora = "";
		this.fechaCirugia = "";
		this.medicoSolicitante = new InfoDatos(0,"");
		this.usuario = "";
		this.estado = -1;
		this.requiere_uci = "";
		this.numeroPedido = 0;
		this.serviciosMap = new HashMap();
		this.numServicios = 0;
		
		this.orden = 0;
		this.medicoResponde = new InfoDatos(0,"");
		this.centroAtencion = 0;
		this.nombreEstado = "";
		
		this.numeroIngreso = "";
		this.programable = "";
		this.filtrosMap = new HashMap();
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (peticionDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			peticionDao = myFactory.getPeticionDao();
		}	
	}
	//****************MÉTODOS************************************************
	/**
	 * Método implementado para consultar los servicios de una petición
	 * @param con
	 * @param numeroPeticion
	 * @param indicador : determinar una consulta especica del DAO (debe ser 1)
	 * @param HashMap filtros
	 * @return
	 */
	public void cargarServiciosPeticion(Connection con,int indicador, HashMap filtros)
	{
		this.serviciosMap = peticionDao.cargarServiciosPeticion(con,this.numeroPeticion,indicador,filtros);
		this.numServicios = Integer.parseInt(this.serviciosMap.get("numRegistros")+"");
	}
	
	/**
	 * Metodo para realizar la consulta de todas las peticiones de cirugia del sistema
     * (si el codigoPeticion > 0 entonces restringe busqueda por este criterio)
	 * @param con
	 * @param codigoPaciente
	 * @param HashMap filtros
	 * @return
	 */
	public HashMap cargarDatosGeneralesPeticion2(Connection con, int codigoPaciente, int codigoPeticion,HashMap filtros) 
	{
		return peticionDao.cargarDatosGeneralesPeticion2(con,codigoPaciente, codigoPeticion,filtros);
	}
	
    /**
	 * Metodo para cargar el encabezado del detalle de la peticion de cirugia 
	 * @param con
	 * @param HashMap filtros
	 * @return
	 */
	public HashMap cargarEncabezadoPeticion(Connection con, int nroPeticion,HashMap filtros)
	{
		return peticionDao.cargarEncabezadoPeticion(con,nroPeticion,filtros);
	}
	
	/**
	 * Metodo para cargar los servicios de una peticion  
	 * @param con
	 * @param HashMap filtros
	 * @return
	 */
	public HashMap cargarServiciosPeticionResultados(Connection con, int nroPeticion,HashMap filtros)
	{
		return peticionDao.cargarServiciosPeticion(con,nroPeticion, 0,filtros);
	}
	
	/**
	 * Metodo para cargar los profesionales de la peticion de cirugia 
	 * @param con
	 * @param HashMap filtros
	 * @return
	 */
	public HashMap cargarProfesionalesPeticion(Connection con, int nroPeticion,HashMap filtros)
	{	
		return peticionDao.cargarProfesionalesPeticion(con, nroPeticion,filtros);
	}

	/**
	 * Metodo para cargar los materiales  de una peticion especifica de un paciente determinado
	 * @param con
	 * HashMap filtros
	 * @return
	 */
	public HashMap cargarMaterialesPeticion(Connection con, int nroPeticion,HashMap filtros)
	{	
		return peticionDao.cargarMaterialesPeticion(con, nroPeticion,filtros);
	}

     /**
     * Metodo que carga los materiales especiales PARAMETRIZADOS de una peticion
     * @param con
     * @param codigoPeticion
     * @param HashMap filtros
     * @return
     */
    public HashMap cargarMaterialesEspeciales2(Connection con, String codigoPeticion)
    {
        return peticionDao.cargarMaterialesEspeciales2(con, codigoPeticion);
    }
    
	
	
	/**
	 * Método para conlsutar las peticionnes generales del Cirugias ó de un paciente Especifico.  
	 * @param con
	 * @param paciente
	 * @param filtro : para saber como se debe generar la consulta
	 * @param codigoCuenta --> Para saber si la cuenta está abierta o cerrada se utiliza en preanestesia
	 * 													  Si es igual a -1 se ignora esto
	 * @param codigoCentroAtencion --> Se utiliza en preantestesia para filtrar o no las peticiones de acuerdo a si está
	 * 														abierta o cerrada la cuenta del paciente
	 * @param HashMap filtros
	 * @return
	 */
	
	public HashMap cargarPeticionesCirugias(Connection con,int paciente, int filtro, int codigoCuenta, int codigoCentroAtencion, HashMap filtros)
	{
		return peticionDao.cargarPeticionesCirugias(con, paciente, filtro, codigoCuenta, codigoCentroAtencion,filtros); 
	}	
	
	/**
	 * Metodo para realizar la consulta de peticiones segun parametros 
	 * @param con
	 * @param origen
	 * @param codigoCentroAtencion --> Se utiliza para realizar el filtro de las peticiones por centro de atención en preanestesia,
	 * 																	del usuario en sesión
	 * @return
	 */
	public HashMap consultarPeticiones(Connection con, String origen, int codigoCentroAtencion) 
	{
		return peticionDao.consultarPeticiones(con, this.nroIniServicio, 
													this.nroFinServicio, 
													this.fechaIniPeticion, 
													this.fechaFinPeticion,
													this.fechaIniCirugia, 
													this.fechaFinCirugia, 
													this.profesional, 
													this.estadoPeticion, 
													origen, 
													this.centroAtencion, 
													codigoCentroAtencion,
													this.getProgramable());
	}
	
    
	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @param numeroPeticion
	 * @param numeroPedido
	 * @return
	 */
	public int actualizarPedidoPeticion(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("numeroPedido", this.getNumeroPedido());
		campos.put("numeroPeticion", this.getNumeroPeticion());
		campos.put("usuario", this.getUsuario());
		return peticionDao.actualizarPedidoPeticion(con,campos);
	}
	
	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @return
	 */
	public int insertarPedidoPeticion(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("numeroPedido", this.getNumeroPedido());
		campos.put("numeroPeticion", this.getNumeroPeticion());
		campos.put("usuario", this.getUsuario());
		return peticionDao.insertarPedidoPeticion(con, campos);
	}
    
    /**
    * Metodo que carga los servicios dados los codigos de peticiones
    * @param con
    * @param codigosPeticionesSeparadosPorComas
    * @return
    */
   public HashMap cargarServiciosDadasPeticiones(Connection con, String codigosPeticionesSeparadosPorComas)
   {
       return peticionDao.cargarServiciosDadasPeticiones(con, codigosPeticionesSeparadosPorComas);
   }
    
   /**
    * Metodo que actualiza (fecha Estimada Cx- Duracion Aprox) informacion de la peticion
    * @param con
    * @param fechaEstimadaCirugia
    * @param duracion
    * @param codigoPeticion
    * @param requiereUci
    * @return
    */
    public boolean actualizarFechaDuracionRequiereUciPeticion(Connection con, String fechaEstimadaCirugia, String duracion, String codigoPeticion, String requiereUci)
    {
        return peticionDao.actualizarFechaDuracionRequiereUciPeticion(con, fechaEstimadaCirugia, duracion, codigoPeticion, requiereUci);
    }
    
    /**
     * metodo que carga el encabezaod de la peticion sin restrcciones, a menos de que se especifiquen los
     * codigos de la peticion.
     * @param con
     * @param codigosPeticionesSeparadosPorComas
     * @param HashMap filtros
     * @return
     */
    public Collection cargarEncabezadoPeticionSinRestricciones(Connection con, String codigosPeticionesSeparadosPorComas, HashMap filtros)
    {
        return peticionDao.cargarEncabezadoPeticionSinRestricciones(con, codigosPeticionesSeparadosPorComas,filtros);
    }
    
    
    /**
     * metodo que actualiza el estado de la peticion
     * @param con
     * @param codigoEstado
     * @param codigoPeticion
     * @return
     */
    public boolean actualizarEstadoPeticion(Connection con, int codigoEstado, String codigoPeticion)
    {
        return peticionDao.actualizarEstadoPeticion(con, codigoEstado, codigoPeticion);
    }    
    
    /**
	 * Método que consulta lso apellidos y nombre del paciente de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public static String getApellidosNombresPacientePeticion(Connection con,int codigoPeticion)
	{
		return peticionDao().getApellidosNombresPacientePeticion(con, codigoPeticion);
	}
	
	/**
	 * Método para obtener el dao de forma estática
	 * @return
	 */
	public static PeticionDao peticionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPeticionDao();
	}
    
	//****************GETTERS & SETTERS ***************************************
	/**
	 * @return Returns the duracion.
	 */
	public String getDuracion() {
		return duracion;
	}
	/**
	 * @param duracion The duracion to set.
	 */
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}
	/**
	 * @return Returns the estado.
	 */
	public int getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(int estado) {
		this.estado = estado;
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
	 * @return Returns the fechaCirugia.
	 */
	public String getFechaCirugia() {
		return fechaCirugia;
	}
	/**
	 * @param fechaCirugia The fechaCirugia to set.
	 */
	public void setFechaCirugia(String fechaCirugia) {
		this.fechaCirugia = fechaCirugia;
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
	 * @return Returns the medicoSolicitante.
	 */
	public InfoDatos getMedicoSolicitante() {
		return medicoSolicitante;
	}
	/**
	 * @param medicoSolicitante The medicoSolicitante to set.
	 */
	public void setMedicoSolicitante(InfoDatos medicoSolicitante) {
		this.medicoSolicitante = medicoSolicitante;
	}
	/**
	 * @return Retorna el código del médico solicitante
	 */
	public int getCodigoMedicoSolicitante()
	{
		return medicoSolicitante.getCodigo();
	}
	/**
	 * Asigna un código al médico solicitante
	 * @param codigo
	 */
	public void setCodigoMedicoSolicitante(int codigo)
	{
		this.medicoSolicitante.setCodigo(codigo);
	}
	/**
	 * @return Retorna el nombre del médico solicitante
	 */
	public String getnombreMedicoSolicitante()
	{
		return medicoSolicitante.getNombre();
	}
	/**
	 * Asigna nombre al medico solicitante
	 * @param nombre
	 */
	public void setNombreMedicoSolicitante(String nombre)
	{
		this.medicoSolicitante.setNombre(nombre);
	}
	/**
	 * @return Returns the numeroPedido.
	 */
	public int getNumeroPedido() {
		return numeroPedido;
	}
	/**
	 * @param numeroPedido The numeroPedido to set.
	 */
	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	/**
	 * @return Returns the numeroPeticion.
	 */
	public int getNumeroPeticion() {
		return numeroPeticion;
	}
	/**
	 * @param numeroPeticion The numeroPeticion to set.
	 */
	public void setNumeroPeticion(int numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}
	/**
	 * @return Returns the paciente.
	 */
	public InfoDatos getPaciente() {
		return paciente;
	}
	/**
	 * @param paciente The paciente to set.
	 */
	public void setPaciente(InfoDatos paciente) {
		this.paciente = paciente;
	}
	/**
	 * @return Retorna el código del paciente
	 */
	public int getCodigoPaciente()
	{
		return paciente.getCodigo();
	}
	/**
	 * Asigna el código del paciente
	 * @param codigo
	 */
	public void setCodigoPaciente(int codigo)
	{
		this.paciente.setCodigo(codigo);
	}
	/**
	 * @return Retorna el nombre del paciente
	 */
	public String getNombrePaciente()
	{
		return paciente.getNombre();
	}
	/**
	 * Asigna el nombre del paciente
	 * @param nombre
	 */
	public void setNombrePaciente(String nombre)
	{
		this.paciente.setNombre(nombre);
	}
	/**
	 * @return Returns the requiere_uci.
	 */
	public String getRequiere_uci() {
		return requiere_uci;
	}
	/**
	 * @param requiere_uci The requiere_uci to set.
	 */
	public void setRequiere_uci(String requiere_uci) {
		this.requiere_uci = requiere_uci;
	}
	/**
	 * @return Returns the tipoPaciente.
	 */
	public InfoDatos getTipoPaciente() {
		return tipoPaciente;
	}
	/**
	 * @param tipoPaciente The tipoPaciente to set.
	 */
	public void setTipoPaciente(InfoDatos tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
	/**
	 * @return Retorna el acrónimo del tipo paciente
	 */
	public String getAcronimoTipoPaciente()
	{
		return tipoPaciente.getId();
	}
	/**
	 * Asigna el acrónimo del tipo Paciente
	 * @param acronimo
	 */
	public void setAcronimoTipoPaciente(String acronimo)
	{
		this.tipoPaciente.setId(acronimo);
	}
	/**
	 * @return Retorna el nombre del tipo paciente
	 */
	public String getNombreTipoPaciente()
	{
		return tipoPaciente.getValue();
	}
	/**
	 * Asigna el nombre del tipo de paciente
	 * @param nombre
	 */
	public void setNombreTipoPaciente(String nombre)
	{
		this.tipoPaciente.setValue(nombre);
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
	 * @return Returns the serviciosMap.
	 */
	public HashMap getServiciosMap() {
		return serviciosMap;
	}
	/**
	 * @param serviciosMap The serviciosMap to set.
	 */
	public void setServiciosMap(HashMap serviciosMap) {
		this.serviciosMap = serviciosMap;
	}
	/**
	 * @return Returns the numServicios.
	 */
	public int getNumServicios() {
		return numServicios;
	}
	/**
	 * @param numServicios The numServicios to set.
	 */
	public void setNumServicios(int numServicios) {
		this.numServicios = numServicios;
	}

	/**
	 * @return Retorna mapaConsultaPeticiones.
	 */
	public HashMap getMapaConsultaPeticiones() {
		return mapaConsultaPeticiones;
	}
	/**
	 * @param Asigna mapaConsultaPeticiones.
	 */
	public void setMapaConsultaPeticiones(HashMap mapaConsultaPeticiones) {
		this.mapaConsultaPeticiones = mapaConsultaPeticiones;
	}

	
	/**
	 * @return Returns the medicoResponde.
	 */
	public InfoDatos getMedicoResponde() {
		return medicoResponde;
	}
	/**
	 * @param medicoResponde The medicoResponde to set.
	 */
	public void setMedicoResponde(InfoDatos medicoResponde) {
		this.medicoResponde = medicoResponde;
	}
	
	/**
	 * @return Retorna el código del médico que respondió la solicitud de la peticion
	 */
	public int getCodigoMedicoResponde()
	{
		return medicoResponde.getCodigo();
	}
	/**
	 * Asigna un código al médico que respondió la solicitud
	 * de la petición
	 * @param codigo
	 */
	public void setCodigoMedicoResponde(int codigo)
	{
		this.medicoResponde.setCodigo(codigo);
	}
	/**
	 * @return Retorna el nombre del médico que responde la 
	 * solicitud de la petición
	 */
	public String getnombreMedicoResponde()
	{
		return medicoResponde.getNombre();
	}
	/**
	 * Asigna nombre al medico que respondió la solicitud
	 * de la petición
	 * @param nombre
	 */
	public void setNombreMedicoResponde(String nombre)
	{
		this.medicoResponde.setNombre(nombre);
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
	 * @return Retorna estadoPeticion.
	 */
	public int getEstadoPeticion() {
		return estadoPeticion;
	}
	/**
	 * @param Asigna estadoPeticion.
	 */
	public void setEstadoPeticion(int estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}
	/**
	 * @return Retorna fechaFinCirugia.
	 */
	public String getFechaFinCirugia() {
		return fechaFinCirugia;
	}
	/**
	 * @param Asigna fechaFinCirugia.
	 */
	public void setFechaFinCirugia(String fechaFinCirugia) {
		this.fechaFinCirugia = fechaFinCirugia;
	}
	/**
	 * @return Retorna fechaFinPeticion.
	 */
	public String getFechaFinPeticion() {
		return fechaFinPeticion;
	}
	/**
	 * @param Asigna fechaFinPeticion.
	 */
	public void setFechaFinPeticion(String fechaFinPeticion) {
		this.fechaFinPeticion = fechaFinPeticion;
	}
	/**
	 * @return Retorna fechaIniCirugia.
	 */
	public String getFechaIniCirugia() {
		return fechaIniCirugia;
	}
	/**
	 * @param Asigna fechaIniCirugia.
	 */
	public void setFechaIniCirugia(String fechaIniCirugia) {
		this.fechaIniCirugia = fechaIniCirugia;
	}
	/**
	 * @return Retorna fechaIniPeticion.
	 */
	public String getFechaIniPeticion() {
		return fechaIniPeticion;
	}
	/**
	 * @param Asigna fechaIniPeticion.
	 */
	public void setFechaIniPeticion(String fechaIniPeticion) {
		this.fechaIniPeticion = fechaIniPeticion;
	}
	/**
	 * @return Retorna nroFinServicio.
	 */
	public int getNroFinServicio() {
		return nroFinServicio;
	}
	/**
	 * @param Asigna nroFinServicio.
	 */
	public void setNroFinServicio(int nroFinServicio) {
		this.nroFinServicio = nroFinServicio;
	}
	/**
	 * @return Retorna nroIniServicio.
	 */
	public int getNroIniServicio() {
		return nroIniServicio;
	}
	/**
	 * @param Asigna nroIniServicio.
	 */
	public void setNroIniServicio(int nroIniServicio) {
		this.nroIniServicio = nroIniServicio;
	}
	/**
	 * @return Retorna peticionDao.
	 */
	public PeticionDao getPeticionDao() {
		return peticionDao;
	}
	/**
	 * @param Asigna peticionDao.
	 */
	public void setPeticionDao(PeticionDao peticionDao) {
		this.peticionDao = peticionDao;
	}
	/**
	 * @return Retorna profesional.
	 */
	public int getProfesional() {
		return profesional;
	}
	/**
	 * @param Asigna profesional.
	 */
	public void setProfesional(int profesional) {
		this.profesional = profesional;
	}

	/**
	 * Método para insertar una petición de corugías en la BD a traves de HashMaps
	 * @param con Conexión con la BD
	 * @param mapaPeticionEncabezado HashMap con los datos del encabezado
	 * @param mapaPeticionServicios HashMap con los datos de los servicios
	 * @param mapaPeticionProfesionales HashMap con los datos de los profesionales participantes
	 * @param mapaPeticionMateriales HashMap con los datos de los materiales especiales
	 * @param codigoPersona Persona a la cual se le desea ingresar la petición
	 * @param usuario Usuario del sistema
	 * @param esModificar Booleano que me indica si el método va a Insertar o a Modificar una peticion existente
	 * @param esContinuarTransaccion, boolean que indica si la transaccion ya fue inicializada
	 * @return Número de inserciónes en la BD (posición 0) y codigo de la petición (posición 1)
	 */
	public int[] insertar(Connection con, HashMap mapaPeticionEncabezado, HashMap mapaPeticionServicios, HashMap mapaPeticionProfesionales, HashMap mapaPeticionMateriales, 
			int codigoPersona, int idIngreso, UsuarioBasico usuario, boolean esContinuarTransaccion, boolean esModificar)
	{
		return peticionDao.insertar(con, mapaPeticionEncabezado, mapaPeticionServicios, mapaPeticionProfesionales, mapaPeticionMateriales, codigoPersona, idIngreso, usuario, esContinuarTransaccion, esModificar);
	}

 	/**
 	 * Método para anular la petición de cirugías
 	 * @param con Conexión con la BD
 	 * @param numeroPeticion Petición a modificar
 	 * @param motivoAnulacion Motivo de la anulación
 	 * @param comentario Comentarios de la anulación
 	 * @param loginUsuario Usuario que realiuzó la anulación
 	 * @return Mayor que 0 si la anulación fue correcta
 	 */
	public int anularPeticion(Connection con, int numeroPeticion, int motivoAnulacion, String comentario, String loginUsuario)
	{
		return peticionDao.anularPeticion(con, numeroPeticion, motivoAnulacion, comentario, loginUsuario);
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getNombreEstado() {
		return nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	

	/**
	 * @return the numeroIngreso
	 */
	public String getNumeroIngreso() {
		return numeroIngreso;
	}

	/**
	 * @param numeroIngreso the numeroIngreso to set
	 */
	public void setNumeroIngreso(String numeroIngreso) {
		this.numeroIngreso = numeroIngreso;
	}

	/**
	 * @return the programable
	 */
	public String getProgramable() {
		if(UtilidadTexto.isEmpty("programable"))
			return "";
		
		return programable;
	}

	/**
	 * @param programable the programable to set
	 */
	public void setProgramable(String programable) {
		this.programable = programable;
	}

	/**
	 * @return the filtrosMap
	 */
	public HashMap getFiltrosMap() {		
		return filtrosMap;
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(HashMap filtrosMap) {
		this.filtrosMap = filtrosMap;
	}
	
	/**
	 * @return the filtrosMap
	 */
	public Object getFiltrosMap(String key) {
		return filtrosMap.get(key);
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(String key, Object value) {
		if(this.filtrosMap == null)
			this.filtrosMap = new HashMap();
		
		this.filtrosMap.put(key, value);
	}
	
	/**
 	 * Método para desasociar la petición de la solicitud
 	 * @param con Conexión con la BD
 	 * @param numeroSolicitud
 	 * @return si se pudo actualizar
 	 */
	public boolean desAsociarPeticionSolicitud(Connection con, int numeroSolicitud)
	{
		return peticionDao.desAsociarPeticionSolicitud(con, numeroSolicitud);
	}
	
}
