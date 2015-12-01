/*
 * Mayo 25, 2007
 */
package util.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.UtilidadesManejoPacienteDao;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoInfoAmparosReclamacion;
import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.manejoPaciente.DtoResponsablePaciente;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurips;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurpro;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * 
 * @author sgomez
 * Clase que contiene las utilidades del módulo de MANEJO PACIENTE
 */
public class UtilidadesManejoPaciente 
{
	

	
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesManejoPacienteDao utilidadesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesManejoPacienteDao();
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarReservaCama(Connection con, String codigoPaciente, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesManejoPacienteDao().consultarReservarCama(con,codigoPaciente, codigoInstitucion);
	}
	
	/**
	 * 
	 * Método que consulta las camas del sistema segun el filtro que se haya tomado a través de los parametros
	 * Nota * El único parametro requerido es institucion, los demás pueden estar nulos a vacíos
	 * @param con
	 * @param institucion
	 * @param codigoSexo
	 * @param fechaNacimiento
	 * @param codigoEstado
	 * @param codigoCentroCosto
	 * @param codigoCentroAtencion
	 * @param viaIngreso
	 * @param horaMovimiento 
	 * @param fechaMovimiento 
	 * @param asignableAdmision 
	 * @return
	 */
	public static HashMap obtenerCamas(Connection con, String institucion,String sexo,String fechaNacimiento,String codigoEstadoCamaDisponible,String codigoCentroCosto,String codigoCentroAtencion,String viaIngreso, String fechaMovimiento, String horaMovimiento,String piso,String tipoPaciente, String asignableAdmision, String paciente, String codigoEstadoCamaReservada, String acronimoEstadoActivo)
	{
		HashMap campos = new HashMap();		
		campos.put("estado",codigoEstadoCamaDisponible);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoCentroAtencion",codigoCentroAtencion);		
		campos.put("viaIngreso",viaIngreso);
		campos.put("tipoPaciente",tipoPaciente);
		campos.put("sexo",sexo);
		campos.put("fechaNacimiento",fechaNacimiento);
		campos.put("institucion",institucion);
		campos.put("fechaMovimiento", fechaMovimiento);
		campos.put("horaMovimiento", horaMovimiento);
		campos.put("piso",piso);
		campos.put("tipoPaciente", tipoPaciente);
		campos.put("asignableAdmision", asignableAdmision);
		campos.put("paciente", paciente);
		campos.put("codigoEstadoCamaReservada", codigoEstadoCamaReservada);
		campos.put("acronimoEstadoActivo", acronimoEstadoActivo);	
		
		return utilidadesDao().obtenerCamas(con, campos);
	}
	
	/**
	 * Método que inserta la reserva de la cama segun el filtro que se haya tomado a través de los parametros
	 * @param con
	 * @param codigo
	 * @param codigoCentroAtencion
	 * @param codigoPaciente
	 * @param codigoCama
	 * @param fechaOcupacion
	 * @param institucion
	 * @param usuariomodifica
	 * @return
	 */
	public static int insertarReservarCamas(Connection con, String codigoPaciente, String codigoCama, String fechaOcupacion, String institucion, String usuarioModifica, String nombresPaciente, String tipoIdentific, String numeroIdentific, String codCentroAtencionSel,String centroAtencionSel, String cama, String codCentroAtencionRes, String centroAtencionRes, String observaciones )
	{
		HashMap mapa= new HashMap();
		mapa.put("codigoPaciente", codigoPaciente);
		mapa.put("codigoCama", codigoCama);
		mapa.put("fechaOcupacion", fechaOcupacion);
		mapa.put("institucion", institucion);
		mapa.put("usuarioModifica", usuarioModifica);
		
		mapa.put("nombresPaciente", nombresPaciente);
		mapa.put("tipoIdentificacion", tipoIdentific);
		mapa.put("numeroIdentificacion", numeroIdentific);
		mapa.put("codCentroAtencionSel", codCentroAtencionSel);
		mapa.put("centroAtencionSel", centroAtencionSel);
		mapa.put("cama", cama);
		mapa.put("codCentroAtencionRes", codCentroAtencionRes);
		mapa.put("centroAtencionRes", centroAtencionRes);
		mapa.put("observaciones",observaciones);
		return utilidadesDao().insertarReservarCamas(con, mapa);
	}
	
	
	/**
	 * Método que modifica la reserva de la cama segun el filtro que se haya tomado a través de los parametros
	 * @param con
	 * @param codigo
	 * @param codigoPaciente
	 * @param codigoCama
	 * @param fechaOcupacion
	 * @param usuariomodifica
	 * @param nombresPaciente
	 * @param tipoIdentific
	 * @param numeroIdentific
	 * @param codCentroAtencionSel
	 * @param centroAtencionSel
	 * @param cama
	 * @param codCentroAtencionRes
	 * @param centroAtencionRes
	 * @return
	 */
	public static boolean modificarReservarCamas(Connection con, String codigo,String codigoPaciente, String codigoCama, String estado,String fechaOcupacion,String usuariomodifica, String nombresPaciente, String tipoIdentific, String numeroIdentific, String codCentroAtencionSel, String centroAtencionSel, String cama, String codCentroAtencionRes, String centroAtencionRes,String observaciones)
	{
		HashMap mapa= new HashMap();
		mapa.put("codigo", codigo);
		mapa.put("codigoCama", codigoCama);
		mapa.put("codigoPaciente", codigoPaciente);
		mapa.put("fechaOcupacion", fechaOcupacion);
		mapa.put("usuarioModifica", usuariomodifica);
		
		mapa.put("nombresPaciente", nombresPaciente);
		mapa.put("tipoIdentificacion", tipoIdentific);
		mapa.put("numeroIdentificacion", numeroIdentific);
		mapa.put("codCentroAtencionSel", codCentroAtencionSel);
		mapa.put("centroAtencionSel", centroAtencionSel);
		mapa.put("cama", cama);
		mapa.put("estado",estado);
		mapa.put("codCentroAtencionRes", codCentroAtencionRes);
		mapa.put("centroAtencionRes", centroAtencionRes);
		mapa.put("observaciones",observaciones);
		return utilidadesDao().modificarReservarCamas(con, mapa);
	}
	
	
	/**
	 * Método para obtener la hora de ingreso de una cuenta dependiendo de la via de ingreso
	 */
	public static String getHoraIngreso(Connection con,int idCuenta,int viaIngreso)
	{
		String hora = "";
		
		switch(viaIngreso)
		{
			case ConstantesBD.codigoViaIngresoUrgencias:
				hora = Utilidades.obtenerHoraAdmisionUrg(con, idCuenta);
			break;
			case ConstantesBD.codigoViaIngresoHospitalizacion:
				hora = Utilidades.obtenerHoraAdmisionHosp(con, idCuenta);
			break;
			case ConstantesBD.codigoViaIngresoConsultaExterna:
			case ConstantesBD.codigoViaIngresoAmbulatorios:
				hora = UtilidadValidacion.obtenerHoraAperturaCuenta(con, idCuenta);
			break;
		}
		
		return hora;
	}
	
	/**
	 * Método implementado para obtener los origenes de la admision
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerOrigenesAdmision(Connection con)
	{
		return utilidadesDao().obtenerOrigenesAdmision(con);
	}
	
	/**
	 * Método que consulta los datos de la cama reservada de un paciente
	 * @param con
	 * @param codigoPaciente
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultarCamaReservada(Connection con,String codigoPaciente,String centroAtencion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("centroAtencion", centroAtencion);
		return utilidadesDao().consultarCamaReservada(con, campos);
	}
	
	/**
	 * Método que libera una cama reserva de un paciente y le cambia su estado
	 * @param con
	 * @param codigoReserva
	 * @param codigoCama
	 * @param estado 
	 * @return
	 */
	public static boolean liberarReservaCama(Connection con,String codigoReserva,String codigoCama, String estado)
	{
		HashMap campos = new HashMap();
		campos.put("codigoReserva", codigoReserva);
		campos.put("codigoCama", codigoCama);
		campos.put("estado", estado);
		return utilidadesDao().liberarReservaCama(con, campos);
	}
	
	/**
	 * Método que consulta las etnias activas del sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEtnias(Connection con)
	{
		return utilidadesDao().obtenerEtnias(con);
	}
	
	/**
	 * Método que consulta las religiones parametrizadas por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerReligiones(Connection con,int institucion)
	{
		return utilidadesDao().obtenerReligiones(con, institucion);
	}
	
	/**
	 * Método que consulta los estudios parametrizados del sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstudios(Connection con)
	{
		return utilidadesDao().obtenerEstudios(con);
	}
	
	/**
	 * Método que consulta los convenios de un paciente, que están asociados a la estructura convenios_paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultarConveniosPaciente(Connection con,String codigoPaciente)
	{
		return utilidadesDao().consultarConveniosPaciente(con, codigoPaciente);
	}
	
	
	/**
	 * Método que consulta los tipos de paciente definidos por vía de ingreso
	 * @param con
	 * @param String codigoViaingreso
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposPacienteXViaIngreso(Connection con,String codigoViaIngreso)
	{
		return obtenerTiposPacienteXViaIngreso(con,codigoViaIngreso,"");
	}
	
	
	/**
	 * Método que consulta los tipos de paciente definidos por vía de ingreso
	 * @param con
	 * @param String codigoViaingreso
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposPacienteXViaIngreso(Connection con,String codigoViaIngreso,String tipoPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("tipoPaciente", tipoPaciente);		
		return utilidadesDao().obtenerTiposPacienteXViaIngreso(con, campos);
	}
	
	/**
	 * Método implementado para cargar el responsable del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoResponsablePaciente cargarResponsablePaciente(Connection con,String tipoIdentificacion,String numeroIdentificacion)
	{
		HashMap campos = new HashMap();
		campos.put("tipoIdentificacion", tipoIdentificacion);
		campos.put("numeroIdentificacion", numeroIdentificacion);
		return utilidadesDao().cargarResponsablePaciente(con, campos);
	}
	
	/**
	 * Método que obtiene los profesiones de la salud por institucion mas otros filtros
	 * @param con
	 * @param institucion
	 * @param validarActivos
	 * @param validarOcupacion
	 * @param centroCosto
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,int institucion,boolean validarActivos,boolean validarOcupacion,String centroCosto,String codigoMedicosInsertados)
	{
		HashMap campos = new HashMap();
		campos.put("institucion", institucion+"");
		campos.put("activo",validarActivos+"");
		campos.put("ocupacion",validarOcupacion+"");
		campos.put("centroCosto",centroCosto);
		campos.put("codigosInsertados",codigoMedicosInsertados);		
		return utilidadesDao().obtenerProfesionales(con, campos);
	}
	
	/**
	 * Método que obtiene los profesiones de la salud por institucion mas otros filtros
	 * @param con
	 * @param institucion
	 * @param validarActivos
	 * @param validarOcupacion
	 * @param centroCosto
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,int institucion,boolean validarActivos,boolean validarOcupacion,String centroCosto,String codigoMedicosInsertados, String especialidades)
	{
		HashMap campos = new HashMap();
		campos.put("institucion", institucion+"");
		campos.put("activo",validarActivos+"");
		campos.put("ocupacion",validarOcupacion+"");
		campos.put("centroCosto",centroCosto);
		campos.put("codigosInsertados",codigoMedicosInsertados);
		campos.put("especialidades",especialidades);
		return utilidadesDao().obtenerProfesionales(con, campos);
	}
	
	
	/**
	 * Método que verifica si es requerido ingresar al documento de garantía
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoTipoPaciente
	 * @param codigoTipoIdentificacion
	 * @param institucion
	 * @return
	 */
	public static boolean esRequeridoDocumentoGarantia(Connection con,int codigoViaIngreso,String codigoTipoPaciente,String codigoTipoIdentificacion,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoViaIngreso", codigoViaIngreso+"");
		campos.put("codigoTipoPaciente", codigoTipoPaciente);
		campos.put("codigoTipoIdentificacion", codigoTipoIdentificacion);
		campos.put("institucion", institucion+"");
		return utilidadesDao().esRequeridoDocumentoGarantia(con, campos);
	}
	
	/**
	 * Método que verifica si es requerido el deudor
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esRequeridoDeudor(Connection con,String codigoTipoRegimen,String codigoTipoIdentificacion,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoTipoRegimen", codigoTipoRegimen);
		campos.put("codigoTipoIdentificacion", codigoTipoIdentificacion);
		campos.put("institucion", institucion+"");
		return utilidadesDao().esRequeridoDeudor(con, campos);
	}
	
	/**
	 * Metodo que me devuelve un arraylist de Hashmap
	 * con la informacion de los estados de la cama.
	 * Los key's del HashMap son: codigo y nombre
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstadosCama (Connection connection) 
	{
		return utilidadesDao().obtenerEstadosCama(connection);
	}
	
	
	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de Costo.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion.
	 * @author Jhony Alexander Duque A.
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido; para buscar en todos los centos de atencion= 0
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion)
	{
		return utilidadesDao().obtenerCentrosCosto(con, institucion, tiposArea, todos, centroAtencion);
	}
	
	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de atencion.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion, viaingtipopac.
	 * @author Jhony Alexander Duque A.
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido para todos = 0
	 * @param manejanCamas --> Opcional
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion, String manejanCamas)
	{
		return  utilidadesDao().obtenerCentrosCosto(con, institucion, tiposArea, todos, centroAtencion, manejanCamas);
	}
		
	
	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de Costo.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion.
	 * @author Jhony Alexander Duque A.
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido; para buscar en todos los centos de atencion= 0
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosCostoViaingreso(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion)
	{
		return utilidadesDao().obtenerCentrosCostoViaingreso(con, institucion, tiposArea, todos, centroAtencion);
	}
	
	
	
	/**
	 * Metodo que retona un Arraylist de HashMap con los
	 * Centros de Atencion.
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosAtencion(Connection con, int codigoInstitucion,String filtrarInsSirc) 
	{
		return utilidadesDao().obtenerCentrosAtencion(con, codigoInstitucion,filtrarInsSirc);
	}
	
	
	/**
	 * 
	 * 
	 */
	public static ArrayList<DtoCentrosAtencion> obtenerCentrosAtencion(DtoCentrosAtencion dto) 
	{
		return utilidadesDao().obtenerCentrosAtencion(dto);
	}
	
	
	/**
	 * Metodo que retorna los pisos filtrandolos
	 * por institucion o centro de atencion que son
	 * los valores que pueden ir en el hashmap.
	 * los key's en el hashmap parametros deben llevar los
	 * siguientes nombres --> institucion y/o centroatencion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPisos (Connection connection, HashMap parametros)
	{
		return utilidadesDao().obtenerPisos(connection, parametros);
	}
	
	/**
	 * Metodo que retorna los pisos filtrandolos
	 * por institucion o centro de atencion
	 * @param connection
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPisos (Connection connection, int institucion,int centroAtencion)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",institucion);
		campos.put("centroatencion",centroAtencion);
		return utilidadesDao().obtenerPisos(connection, campos);
	}
	
	
	/**
	 * Metodo que carga un paciente.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param usuarioActual
	 * @param paciente
	 * @param request
	 */
	public static  void cargarPaciente (Connection connection, UsuarioBasico usuarioActual,PersonaBasica paciente, HttpServletRequest request)
	{
		
		ObservableBD observable = (ObservableBD)request.getSession().getServletContext().getAttribute("observable");
		try 
		{
			paciente.cargar(connection, paciente.getCodigoPersona());
			paciente.cargarPaciente2(connection, paciente.getCodigoPersona(), usuarioActual.getCodigoInstitucion(), usuarioActual.getCodigoCentroAtencion()+"");
		} 
		catch (Exception e) 
		{
			System.out.print("Error cargando el paciente: "+e);
		}
		observable.addObserver(paciente);
		UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
	}
	
	/**
	 * Método para cargar el paciente x ingreso
	 * @param connection
	 * @param usuarioActual
	 * @param paciente
	 * @param request
	 * @param idIngreso
	 */
	public static void cargarPacienteXIngreso(Connection connection, UsuarioBasico usuarioActual,PersonaBasica paciente, HttpServletRequest request,String idIngreso)
	{
		ObservableBD observable = (ObservableBD)request.getSession().getServletContext().getAttribute("observable");
		try 
		{
			paciente.cargar(connection, paciente.getCodigoPersona());
			paciente.cargarPacienteXingreso(connection, idIngreso, usuarioActual.getCodigoInstitucion(), usuarioActual.getCodigoCentroAtencion()+"");
			
		} 
		catch (Exception e) 
		{
			System.out.print("Error cargando el paciente: "+e);
		}
		observable.addObserver(paciente);
		UtilidadSesion.notificarCambiosObserverXIngreso(idIngreso,paciente.getCodigoPersona(),request.getSession().getServletContext());
	}
	
	
	/**
	 * Devuelve 
	 * @param String fechaMayor
	 * @param String horaMayor
	 * @param String fechaMayor
	 * @param String horaMayor 
	 * */	
	public static String calcularTiempo(String fechaMayor, String horaMayor, String fechaMenor, String horaMenor)
	{
		GregorianCalendar calendario=new GregorianCalendar();
		
		String horasSirc="";
		String minutosSirc="";		
		
		long fechaInMillisMayor;
		long fechaInMillisMenor;		
		long segundos;
		long minutos;
		long horas;				
		
		calendario.set(Integer.parseInt(fechaMayor.split("-")[0]),Integer.parseInt(fechaMayor.split("-")[1]),Integer.parseInt(fechaMayor.split("-")[2]),Integer.parseInt(horaMayor.split(":")[0]),Integer.parseInt(horaMayor.split(":")[1]));		
		fechaInMillisMayor = calendario.getTimeInMillis();		
		calendario.set(Integer.parseInt(fechaMenor.split("-")[0]),Integer.parseInt(fechaMenor.split("-")[1]),Integer.parseInt(fechaMenor.split("-")[2]),Integer.parseInt(horaMenor.split(":")[0]),Integer.parseInt(horaMenor.split(":")[1]));	
		fechaInMillisMenor = calendario.getTimeInMillis();
		
		segundos = (fechaInMillisMayor - fechaInMillisMenor) / 1000;		
		horas = segundos / 3600 ;
		segundos -= horas*3600; // se calcula de nuevo debido a la perdida de decimales		
		minutos  = segundos / 60;
				
		if(horas<10)
			horasSirc = "0";		
		if(minutos<10)
			minutosSirc = "0";		
		
		return horasSirc+""+horas+":"+minutosSirc+""+minutos;	
	}
	
	/**
	 * Método que consulta las habitaciones por institucion y centro de atencion
	 * el campo piso es opcional, si no se quiere filtrar por piso se manda 0
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerHabitaciones(Connection con,int institucion,int centroAtencion,int piso)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("institucion", institucion);
		campos.put("centroAtencion", centroAtencion);
		campos.put("piso", piso);
		return utilidadesDao().obtenerHabitaciones(con, campos);
	}
	
	/**
	 * Método para consultar los tipos de habitaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposHabitaciones(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",institucion);
		return utilidadesDao().obtenerTiposHabitaciones(con, campos);
	}
	
	
	/**
	 * Método que consulta el tipo paciente de una cuenta
	 * @param con
	 * @param idcuenta
	 * @return
	 */
	public static InfoDatosString obtenerTipoPacienteCuenta(Connection con,String idCuenta)
	{
		HashMap campos = new HashMap();
		campos.put("idCuenta",idCuenta);
		return utilidadesDao().obtenerTipoPacienteCuenta(con, campos);
	}
	
	/**
	 * Método que consulta el tipo paciente de una cuenta
	 * @param con
	 * @param idcuenta
	 * @return
	 */
	public static InfoDatosString obtenerTipoPacienteCuenta(String idCuenta)
	{
		Connection con=UtilidadBD.abrirConexion();
		HashMap campos = new HashMap();
		campos.put("idCuenta",idCuenta);
		InfoDatosString info=  utilidadesDao().obtenerTipoPacienteCuenta(con, campos);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	/**
	 * Método que consulta el tipo paciente de una solicitud
	 * @param con
	 * @param nSolicitud
	 * @return
	 */
	public static InfoDatosString obtenerTipoPacienteSolicitud(Connection con,String nSolicitud) throws IPSException
	{
		HashMap campos = new HashMap();
		campos.put("nSolicitud",nSolicitud);
		return utilidadesDao().obtenerTipoPacienteSolicitud(con, campos);
	}
	
	
	
	
	/**
	 * Método que consulta el id de la cuenta de un paciente que pertenezca a un ingreso abierto,
	 * una via de ingreso especifica, un centro de atencion especifico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String getIdCuentaIngresoAbiertoPaciente(Connection con,int codigoPaciente,int codigoViaIngreso,int codigoCentroAtencion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente+"");
		campos.put("codigoViaIngreso",codigoViaIngreso+"");
		campos.put("codigoCentroAtencion",codigoCentroAtencion+"");
		return utilidadesDao().getIdCuentaIngresoAbiertoPaciente(con, campos);
	}
	
	/**
	 * Método que consulta el centro de atencion de la cuenta del ingreso abierto del paciente.
	 * 
	 * Nota* Se envian como parámetros el codgio del pacietne y las vias de ingreso que se desean filtrar
	 * se pasan separadas por comas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int getCentroAtencionCuentaIngresoAbiertoPaciente(Connection con,int codigoPaciente,String viasIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente+"");
		campos.put("viasIngreso",viasIngreso);
		return utilidadesDao().getCentroAtencionCuentaIngresoAbiertoPaciente(con, campos);
	}
	
	/**
	 * Método que realiza la isnerción del log de cambio de estado de camas
	 * @param con
	 * @param codigoCama
	 * @param codigoEstadoOriginal
	 * @param codigoEstadoNuevo
	 * @param fecha
	 * @param hora
	 * @param usuario
	 * @param idCuenta
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static int insertarLogCambioEstadoCama(Connection con,int codigoCama, int codigoEstadoOriginal,int codigoEstadoNuevo, String fecha, String hora,String usuario,String idCuenta,int codigoInstitucion,int codigoCentroAtencion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCama",codigoCama+"");
		campos.put("codigoEstadoOriginal",codigoEstadoOriginal+"");
		campos.put("codigoEstadoNuevo",codigoEstadoNuevo+"");
		campos.put("fecha",fecha);
		campos.put("hora",hora);
		campos.put("usuario",usuario);
		campos.put("idCuenta",idCuenta);
		campos.put("codigoInstitucion",codigoInstitucion+"");
		campos.put("codigoCentroAtencion",codigoCentroAtencion+"");
		return utilidadesDao().insertarLogCambioEstadoCama(con, campos);
	}
	
	/**
	 * Metodo adicionado por Jhony Alexander Duque A.
	 * Metodo encargado de consultar Datos de la cama.
	 * Este Metodo recibe un hashmap con dos parametros
	 * -------------------------------------
	 *      KEY'S DEL HASHMAP PARAMETROS
	 * -------------------------------------
	 * --codigoCama --> Requerido
	 * -->institucion --> Requerido
	 * Devuelve un Mapa con los siguientes Key's
	 *  habitacion,tipoHabitacion,numeroCama,
	 *  nombreCentroCosto,codigoCama,piso,
	 *  tipoUsuario, centroAtencion
	 *  estadoCama,nombreEstadoCama
	 * @param connection
	 * @param parametros
	 * @return mapa
	 */
	public static HashMap obtenerDatosCama (Connection connection, HashMap parametros)
	{
		return utilidadesDao().obtenerDatosCama(connection, parametros);
	}
	
	/**
	 * Método que consulta las camas por habitación
	 * @param con
	 * @param codigoHabitacion
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerCamasXHabitacion(Connection con,String codigoHabitacion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoHabitacion",codigoHabitacion);
		return utilidadesDao().obtenerCamasXHabitacion(con, campos);
	}
	
	/**
	 * Método implementado para consultar las entidades subcontratadas parametrizadas
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEntidadesSubcontratadas(Connection con,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		return utilidadesDao().obtenerEntidadesSubcontratadas(con, campos);
	}
	
	
	/**
	 * Método implementado para consultar las entidades subcontratadas parametrizadas a partir de la institucion  y una fecha dada
	 * para la validacion de vigencias. 
	 * 
	 * Key's consecutivo,codigo,institucion,descripcion
	 * @param con
	 * @param codigoInstitucion
	 * @param String fecha o vacio
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEntidadesSubcontratadas(Connection con,int codigoInstitucion,String fecha)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("fecha",fecha);
		return utilidadesDao().obtenerEntidadesSubcontratadas(con, campos);
	}
	
	/**
	 * Método implementado para consultar los usuarios que han registrado ingresos en la estructura de Pacientes entidades subcontratadas 
	 * 
	 * Key's login,nombre
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerUsuariosEntidadesSubcontratadas(Connection con,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",codigoInstitucion);		
		return utilidadesDao().obtenerUsuariosEntidadesSubcontratadas(con, campos);
	}
	
	
	/**
	 * Método uqe consultar el valor de un parámetro general de los planos entidades subcontratadas 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String getValorParametroGeneralPlanosEntidadSubcontratada(Connection con,int seccion,String campo,int codigoInstitucion,int codigoConvenio,int codigoViaIngreso,int codigoCentroAtencion)
	{
		HashMap campos = new HashMap();
		campos.put("seccion",seccion);
		campos.put("campo",campo);
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoConvenio",codigoConvenio);
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("codigoCentroAtencion",codigoCentroAtencion);
		return utilidadesDao().getValorParametroGeneralPlanosEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Metodo Adicionado por Jhony Alexander Duque A.
	 * 11/01/2008
	 * Metodo encargado de obtener las secciones de parametros
	 * entidades subcontratadas.
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerSeccionesParametrosEntidadesSubcontratadas(Connection con)
	{
		return utilidadesDao().obtenerSeccionesParametrosEntidadesSubcontratadas(con);
	}
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * 15/01/2008
	 * Metodo encargado de consultar los tipos de 
	 * monto de cobro pudiendo filtar por difentes
	 * campos
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 *  -- activo --> Opcional 
	 *  -- convenio --> Opcional 
	 *  -- viaIngreso --> Opcional 
	 *  -- tipoAfiliado --> Opcional 
	 * @return ArrayList<HashMap<String,Objet>>
	 * --------------------------------------------------------
	 * 	KEY'S DEL MAPA DENTRO DEL ARRAY QUE DEVUELVE EL METODO
	 * --------------------------------------------------------
	 * -- codigo
	 * -- convenio
	 * -- viaIngreso
	 * -- tipoAfiliado
	 * -- estratoSocial
	 * -- tipoMonto
	 * -- valor
	 * -- porcentaje
	 * -- activo
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMontosCobro (Connection connection,HashMap criterios)
	{
		return utilidadesDao().obtenerMontosCobro(connection, criterios);
	}
	
	/**
	 * Adicionado por Julio Hernández
	 * 24/09/2009
	 * Metodo encargado de consultar los tipos de 
	 * monto de cobro pudiendo filtar por difentes
	 * campos
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 *  -- activo --> Opcional 
	 *  -- convenio --> Opcional 
	 *  -- viaIngreso --> Opcional 
	 *  -- tipoAfiliado --> Opcional 
	 * @return ArrayList<HashMap<String,Objet>>
	 * --------------------------------------------------------
	 * 	KEY'S DEL MAPA DENTRO DEL ARRAY QUE DEVUELVE EL METODO
	 * --------------------------------------------------------
	 * -- codigo
	 * -- convenio
	 * -- viaIngreso
	 * -- tipoAfiliado
	 * -- estratoSocial
	 * -- tipoMonto
	 * -- valor
	 * -- porcentaje
	 * -- activo
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMontosCobroVigentes (Connection connection,HashMap criterios)
	{
		return utilidadesDao().obtenerMontosCobroVigentes(connection, criterios);
	}
	
	
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * 15/01/2008
	 * Metodo encargado de consultar todos los tipos de 
	 * paciente
	 * @param connection
	 * @return ArrayList<HashMap<String, Object>> TiposPaciente
	 * ----------------------------------------------------
	 * KEY'S DEL HASHMAP DEL ARRAYLIST DE TIPOS PACIENTE
	 * ----------------------------------------------------
	 * -- acronimo
	 * -- nombre
	 */
	public  static ArrayList<HashMap<String, Object>>ObtenerTiposPaciente (Connection connection, int viaIngreso)
	{
		return utilidadesDao().ObtenerTiposPaciente(connection,viaIngreso);
	}
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * 16/01/2008
	 * Metodo encargado de consultar las areas de ingreso
	 * por via de ingreso.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------------
	 * 			KEY'S DEL HASHMAP CRITERIOS
	 * -----------------------------------------------
	 *  -- institucion --> Requerido
	 *  -- viaIngreso --> Opcional
	 *  -- centroCosto --> Opcional
	 *  -- centroAtencion --> Requerido
	 * @return rrayList<HashMap<String, Object>> areaIngreso
	 * ----------------------------------------------------
	 * KEY'S DEL HASHMAP DENTRO DEL ARRAY AREA INGRESO
	 * ----------------------------------------------------
	 * -- codigo
	 * -- nombre
	 * -- centroCosto
	 * -- viaIngreso
	 */
	public static ArrayList<HashMap<String, Object>>ObtenerAreaIngreso (Connection connection,HashMap criterios)
	{
		return utilidadesDao().ObtenerAreaIngreso(connection, criterios);
	}
	
	/**
	 * Método implementado para obtener los constratos vigentes de un usuario capitado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerContratosVigentesUsuarioCapitado(Connection con,int codigoPaciente,int codigoConvenio)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoConvenio",codigoConvenio);
		return utilidadesDao().obtenerContratosVigentesUsuarioCapitado(con, campos);
	}
	
	/**
	 * Método implementado para obtener los constratos vigentes de un usuario capitado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerContratosUsuarioCapitado(Connection con,int codigoPaciente,int codigoConvenio)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoConvenio",codigoConvenio);
		return utilidadesDao().obtenerContratosUsuarioCapitado(con, campos);
	}
	
	/**
	 * Método para obtener la descripcion de un tipo cie
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerDescripcionTipoCie(Connection con,int codigo)
	{
		return utilidadesDao().obtenerDescripcionTipoCie(con, codigo);
	}



	public static int obtenerCamaAdmision(Connection con, int idCuenta) {
		return utilidadesDao().obtenerCamaAdmision(con, idCuenta);
	}
	
	/**
	 * Método para consultar los estados de la cuenta
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstadosCuenta(Connection con)
	{
		return utilidadesDao().obtenerEstadosCuenta(con);
	}
	
	/**
	 * Método implmentado para listar los tipos de monitoreo
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposMonitoreo(Connection con,int codigoInstitucion)
	{
		return utilidadesDao().obtenerTiposMonitoreo(con, codigoInstitucion);
	}
	
	/**
	 * Método implementado para obtener el codigo del ingreso si un paciente tiene un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return codigoIngreso
	 */
	public static int tienePreingresoPendiente(Connection con, int codigoInstitucion, int codigoPaciente)
	{
		return utilidadesDao().tienePreingresoPendiente(con, codigoInstitucion, codigoPaciente);
	}
	
	/**
	 * Método que retorna si un ingreso tiene o no, un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean ingresoConPreingresoPendiente(Connection con,int codigoInstitucion, int codigoIngreso)
	{
		return utilidadesDao().ingresoConPreingresoPendiente(con, codigoInstitucion, codigoIngreso);
	}
	
	/**
	 * Método implementado para obtener el codigo del ingreso si un paciente tiene un preingreso pendiente en un centro de atencion.
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return codigoIngreso
	 */
	public static int tienePreingresoPendienteXCentroAtencion(Connection con, int codigoInstitucion, int centroAtencion, int codigoPaciente)
	{
		return utilidadesDao().tienePreingresoPendienteXCentroAtencion(con, codigoInstitucion, centroAtencion, codigoPaciente);
	}
	
	/**
	 * Método implementado para obtener el origen de admision de una cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerOrigenAdmisionCuenta(Connection con,int codigoCuenta)
	{
		return utilidadesDao().obtenerOrigenAdmisionCuenta(con, codigoCuenta);
	}
	
	/**
	 * Método implementado para actualizar el estado del preingreso
	 * @param con
	 * @param ingreso
	 * @param estado
	 * @return
	 */
	public static boolean actualizarEstadoPreingreso(Connection con, int ingreso, String estado, String usuario)
	{
		return utilidadesDao().actualizarEstadoPreingreso(con, ingreso, estado, usuario);
	}
	
	/**
	 * Método implementado para consultar si el ingreso tuvo un cierre manual
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean tieneIngresoManual(Connection con, int ingreso)
	{
		return utilidadesDao().tieneIngresoManual(con, ingreso);
	}
	
	/**
	 * Método implementado para consultar el reingreso de un ingreso; si no tiene retorna (-1)
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int obtenerReingreso(Connection con, int ingreso)
	{
		return utilidadesDao().obtenerReingreso(con, ingreso);
	}
	
	/**
	 * Método implementado para obtener los ingresosCerradosPendientesXFacturar
	 * (si el ingreso se encuentra en estado cerrado con indicativo de cierre manual y cuenta en estado activa, asociada o facturacion parcial)
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String ingresosCerradosPendientesXFacturar(Connection con, int paciente)
	{
		return utilidadesDao().ingresosCerradosPendientesXFacturar(con, paciente);
	}
	
	
	/**
	 * Metodo encargado de consultar la fecha de apertura de la cuenta.
	 * @param connection
	 * @param cuenta
	 * @return fecha apertura
	 */
	public static  String obtenerFechaAperturaCuenta (Connection connection, String cuenta)
	{
		return utilidadesDao().obtenerFechaAperturaCuenta(connection, cuenta);
	}
	
	/**
	 * Metodo encargado de consultar la fecha de apertura de la cuenta.
	 * @param connection
	 * @param cuenta
	 * @return fecha apertura
	 */
	public static  String obtenerFechaAperturaCuenta (String cuenta)
	{
		Connection connection= UtilidadBD.abrirConexion();
		String w= utilidadesDao().obtenerFechaAperturaCuenta(connection, cuenta);
		UtilidadBD.closeConnection(connection);
		return w;
	}
	
	/**
	 * Método para verificar si es requerido la verificación de derechos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esRequeridaVerificacionDerechos(Connection  con,int codigoViaIngreso,String codigoTipoRegimen)
	{
		HashMap campos = new HashMap();
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("codigoTipoRegimen",codigoTipoRegimen);
		return utilidadesDao().esRequeridaVerificacionDerechos(con, campos);
		
	}
	
	/**
	 * Método que verifica si existe verificación de derechos por la subcuenta
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean existeVerificacionDerechosSubcuenta(Connection con,String subCuenta)
	{
		return utilidadesDao().existeVerificacionDerechosSubcuenta(con, subCuenta);
	}
	
	/**
	 * Método paara verificar si existe un ingreso cuidado especial activo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean existeIngresoCuidadoEspecialActivo(Connection con,int codigoIngreso,String indicativo)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",codigoIngreso);
		campos.put("indicador",indicativo);
		return utilidadesDao().existeIngresoCuidadoEspecialActivo(con, campos);
		
	}
	
	/**
	 * Método que verifica si el ingreso es de control postoperatorio
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean esIngresoControlPostOperatorioCx(Connection con,String codigoIngreso)
	{
		return utilidadesDao().esIngresoControlPostOperatorioCx(con, codigoIngreso);
	}
	
	/**
	 * Metodo encargado de consultar la fecha de apertura del ingreso
	 * @param connection
	 * @param ingreso
	 * @return fecha apertura
	 */
	public static  String obtenerFechaAperturaIngreso (Connection connection, String ingreso)
	{
		return utilidadesDao().obtenerFechaAperturaIngreso(connection, ingreso);
	}
	
	/**
	 * Método implementado para obtener el codigo del tipo de monitoreo del ingreso a cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial(Connection con,int idIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		return utilidadesDao().obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial(con, campos); 
	}
	
	/**
	 * Método que actualiza el tipo de monitoreo de un ingresi cuidado especial
	 * @param con
	 * @param codigoCentroCosto 
	 * @param campos
	 * @return
	 */
	public static int actualizarTipoMonitoreoIngresoCuidadoEspecial(Connection con,int idIngreso,int codigoTipoMonitoreo,int codigoCentroCosto, String usuario)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		campos.put("codigoTipoMonitoreo",codigoTipoMonitoreo);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("usuario",usuario);
		return utilidadesDao().actualizarTipoMonitoreoIngresoCuidadoEspecial(con, campos);
	}
	
	/**
	 * Método implementado para reversar un ingreso cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean reversarIngresoCuidadoEspecial(Connection con,int idIngreso,String usuario)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		campos.put("usuario",usuario);
		return utilidadesDao().reversarIngresoCuidadoEspecial(con, campos);
	}
	
	/**
	 * Método implementado para obtener los tipos de reporte
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposReporte(Connection con,int codigoFuncionalidad,String parametrizable)
	{
		HashMap campos = new HashMap();
		campos.put("codigoFuncionalidad", codigoFuncionalidad);
		campos.put("parametrizable", parametrizable);
		return utilidadesDao().obtenerTiposReporte(con, campos);
	}
	
	/**
	 * Método implementado para insertar el log de reportes
	 * @param con
	 * @param codigoTipoReporte
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public static int insertarLogReportes(Connection con,int codigoTipoReporte,String loginUsuario,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoTipoReporte", codigoTipoReporte);
		campos.put("loginUsuario", loginUsuario);
		campos.put("codigoInstitucion", codigoInstitucion);
		return utilidadesDao().insertarLogReportes(con, campos);
	}
	
	/**
	 * Carga todas las cuentas del paciente
	 * @param Connection con
	 * @param String codigoPaciente
	 * */
	public static ArrayList getCuentasPaciente(Connection con,String codigoPaciente)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPaciente",codigoPaciente);
		return utilidadesDao().cuentasPaciente(con,parametros);
	}
	
	/**
	 * Metodo encargado de consultar y devolver las cuentas de un ongreso
	 * separadas por comas. 
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public static String obtenerCuentasXIngreso (Connection connection, String ingreso)
	{
		return utilidadesDao().obtenerCuentasXIngreso(connection, ingreso);
	}

	/**
	 * Inserta el log de las autorizaciones ingresadas al paciente
	 * @param Connection con
	 * @param String autorizacion
	 * @param String numeroId
	 * @param String tipoId
	 * @param UsuarioBasico usuario
	 * @param String funcionalidad
	 * */
	public static boolean insertarLogAutorizacionIngresoEvento(
			Connection con,
			String autorizacion,
			String codigoPersona,
			String numeroId, 
			String tipoId,
			UsuarioBasico usuario,
			String funcionalidad)
	{
		HashMap parametros = new HashMap();
		parametros.put("autorizacion",autorizacion);
		parametros.put("codigoPersona",codigoPersona);
		parametros.put("numeroid",numeroId);
		parametros.put("tipoid",tipoId);		
		parametros.put("institucion",usuario.getCodigoInstitucion());
		parametros.put("funcionalidad",funcionalidad);
		parametros.put("usuario",usuario.getLoginUsuario());
		parametros.put("fecha",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("hora",UtilidadFecha.getHoraActual());
		
		return utilidadesDao().insertarLogAutorizacionIngresoEvento(con,parametros);		
	}
	
	/**
	 * Método para obtener el identificador del centro de costo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerIdentificadorCentroCosto(Connection con,int codigo)
	{
		return utilidadesDao().obtenerIdentificadorCentroCosto(con, codigo);
	}
	
	/**
	 * Método para obtener el número de carnet del ingreso del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static String obtenerNroCarnetIngresoPaciente(Connection con,int codigoIngreso)
	{
		return utilidadesDao().obtenerNroCarnetIngresoPaciente(con, codigoIngreso);
	}
	
	/**
	 * Método para consultar el codigo paciente de un ingreso
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static int obtenerCodigoPacienteXIngreso(Connection con,int codigoIngreso)
	{
		return utilidadesDao().obtenerCodigoPacienteXIngreso(con, codigoIngreso);
	}
	
	/**
	 * Método que verifica si está el centro de costo asociado a un tipo de monitoreo específico
	 * @param con
	 * @param centroCosto
	 * @param tipoMonitoreo
	 * @return
	 */
	public static boolean estaCentroCostoEnTipoMonitoreo(Connection con,int centroCosto,int tipoMonitoreo)
	{
		return utilidadesDao().estaCentroCostoEnTipoMonitoreo(con, centroCosto, tipoMonitoreo);
	}
	
	/**
	 * Metodo encargado de consultar si un registro de sol_cirugia_por_servicio se encuentra
	 * en la tabla de informacion_parto
	 * @param connection
	 * @param codigoSolCxServ
	 * @return consecutivo informacion parto (-1 si no encontro nada)
	 */
	public static int obtenerConsecutivoPartoXcodigoSolCxServ (Connection connection, String codigoSolCxServ )
	{
		return utilidadesDao().obtenerConsecutivoPartoXcodigoSolCxServ(connection, codigoSolCxServ);
	}

	/**
	 * Método para obtener el codigo del medico tratante
	 * @param con
	 * @param ingreso
	 */
	public static int obtenerCodigoMedicoTratante(Connection con, int ingreso) 
	{
		return utilidadesDao().obtenerCodigoMedicoTratante(con, ingreso);
	}
	
	/**
	 * Metodo encargado de consultar algunos datos del ingreso.
	 * @param connection
	 * @param ingreso
	 * @return HashMap
	 * 
	 */
	public static HashMap obtenerDatosIngreso (Connection connection, String ingreso,String institucion)
	{
		return utilidadesDao().obtenerDatosIngreso(connection, ingreso, institucion);
	}
	
	/**
	 * Metodo encargado de consultar el codigo de interfaz segun la via de ingreso y el tipo de paciente.
	 * @param connection
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public static String obtenerCodInterfazXViaIngresoTipoPac(Connection connection, int viaIngreso, String tipoPaciente)
	{
		return utilidadesDao().obtenerCodInterfazXViaIngresoTipoPac(connection, viaIngreso, tipoPaciente);
	}
	
	/**
	 * Metodo encargado de consultar el codigo de interfaz segun la cuenta.
	 * @param connection
	 * @param cuenta
	 * @return
	 */
	public static String obtenerCodInterfazXCuenta(Connection connection, String cuenta)
	{
		return utilidadesDao().obtenerCodInterfazXCuenta(connection, cuenta);
	}
	/**
	 * Método implementado para cargar los tipos de servicios solicitados
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerTiposServicioSolicitados(Connection con,int codigoInstitucion,boolean activo)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("activo",activo);
		return utilidadesDao().obtenerTiposServicioSolicitados(con, campos);
	}
	
	/**
	 * Método que consulta el tipo de cobertura de una sub cuenta
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerTipoCoberturaSubCuenta(Connection con,String idSubCuenta)
	{
		return utilidadesDao().obtenerTipoCoberturaSubCuenta(con, idSubCuenta);
	}
	
	/**
	 * Método para obtener las coberturas salud por tipo de regimen de una sub_cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCoberturasSaludXTipoRegimenSubCuenta(Connection con,String idSubCuenta,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("idSubCuenta", idSubCuenta);
		campos.put("codigoInstitucion",codigoInstitucion);		
		return utilidadesDao().obtenerCoberturasSaludXTipoRegimenSubCuenta(con, campos);
	}
		
	/**
	 * Método para obtener las coberturas de salud x el tipo de régimen
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCoberturasSaludXTipoRegimen(Connection con,String codigoTipoRegimen,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoTipoRegimen", codigoTipoRegimen);
		campos.put("codigoInstitucion",codigoInstitucion);
		Utilidades.imprimirMapa(campos);
		return utilidadesDao().obtenerCoberturasSaludXTipoRegimen(con, campos);
	}
	
	/**
	 * Método para cargar las entidades del envío
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoConvenio
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarEntidadesEnvio(Connection con,int codigoInstitucion,int codigoConvenio)
	{
		ArrayList<HashMap<String, Object>> entidades = new ArrayList<HashMap<String,Object>>();
		
		//Se consultan las empresas de dirección territorial
		ArrayList<HashMap<String, Object>> empresas = Utilidades.obtenerEmpresas(con, codigoInstitucion, true);
		for(HashMap elemento:empresas)
		{
			HashMap<String,Object> nuevaEntidad = new HashMap<String, Object>();
			nuevaEntidad.put("codigo",elemento.get("codigo"));
			nuevaEntidad.put("descripcion",elemento.get("descripcion"));
			nuevaEntidad.put("esEmpresa",ConstantesBD.acronimoSi);
			entidades.add(nuevaEntidad);
		}
		
		
		//Se consultan el convenio que aplica
		String nombreConvenio = Utilidades.obtenerNombreConvenioOriginal(con, codigoConvenio);
		HashMap<String, Object> nuevaEntidad = new HashMap<String, Object>();
		nuevaEntidad.put("codigo", codigoConvenio);
		nuevaEntidad.put("descripcion",nombreConvenio);
		nuevaEntidad.put("esEmpresa",ConstantesBD.acronimoNo);
		entidades.add(nuevaEntidad);
		
		return entidades;
		
	}
	
	
	/**
	 * Obtiene los convenios por ingreso
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<HashMap<String,Object>> obtenerConveniosXIngreso(Connection con,int codigoIngreso,String codigoSubcuenta)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",codigoIngreso);
		parametros.put("codigoSubcuenta",codigoSubcuenta);
		
		return utilidadesDao().obtenerConveniosXIngreso(con, parametros);	
	}
	
	/**
	 *  consula la informacion de los profesionales de la salud que solicitan ordenes (solicitudes). la busqueda puede ser por
	 *  numero de solicitud o codigopk de det_cargos, si es mas de un codigo, van separados por comas 
	 * */
	public static ArrayList<HashMap<String,Object>> obtenerProfSolicitaSolicitudes(Connection con,String codigoEvaluar,boolean esDetCargos)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoEvaluar", codigoEvaluar);
		parametros.put("esDetCargos", esDetCargos?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return utilidadesDao().obtenerProfSolicitaSolicitudes(con, parametros); 
	}
	
	/**
	 * Método para obtener los convenios del ingreso y su informacion de autorizacion asociada
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoAutorizacion> obtenerConveniosIngresoAutorizacionesAdmision(Connection con,int codigoIngreso,int codigoCuenta,boolean filtrarSinCuenta)
	{
		HashMap<String,Object> campos = new HashMap<String, Object>();
		campos.put("codigoIngreso", codigoIngreso);
		campos.put("codigoCuenta", codigoCuenta);
		campos.put("filtrarSinCuenta", filtrarSinCuenta);
		return utilidadesDao().obtenerConveniosIngresoAutorizacionesAdmision(con, campos);
	}
	
	/**
	 * Método para obtener el estado de la autorizacion de admisión de una subcuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoAutorizacion> obtenerAutorizacionesAdmisionSubCuenta(Connection con,long idSubCuenta)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("idSubCuenta",idSubCuenta);
		return utilidadesDao().obtenerAutorizacionesAdmisionSubCuenta(con, campos);
	}
	
	/**
	 * Método para obtener el tipo de entidad del centro de costo que ejecuta
	 * @param con
	 * @param codigoCentroCosto
	 * @return
	 */
	public static String obtenerTipoEntidadEjecutaCentroCosto(Connection con,int codigoCentroCosto)
	{
		return utilidadesDao().obtenerTipoEntidadEjecutaCentroCosto(con, codigoCentroCosto);
	}
	
	/**
	 * Método para cargar la descripcion de la entidaad subcontratad de la autorizacion de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultarDescripcionEntidadSubcontratadaAutorizacionSolicitud(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().consultarDescripcionEntidadSubcontratadaAutorizacionSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param paciente
	 * @return
	 */
	public static ArrayList<Paciente>  obtenerDatosPaciente(Paciente paciente)
		{
		return utilidadesDao().obtenerDatosPaciente(paciente);
     	}
	
	/**
	 * Método para cargar los datos básicos de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static DtoCuentas consultarDatosCuenta(Connection con,BigDecimal idCuenta)
	{
		return utilidadesDao().consultarDatosCuenta(con, idCuenta);
	}
	
	
	/**
	 * Método para consultar el numero de autorizacion de ingreso
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerAutorizacionIngresoXFactura(Connection con,String codigoFactura)
	{
		return utilidadesDao().obtenerAutorizacionIngresoXFactura(con, codigoFactura);
	}
	
	/**
	 * Método para verificar si un paciente tiene un ingreso abierto sin importar a cual centro de atención hace parte
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean tienePacienteIngresoAbierto(Connection con,BigDecimal codigoPaciente)
	{
		return utilidadesDao().tienePacienteIngresoAbierto(con, codigoPaciente);
	}
	
	/**
	 * 
	 * Metodo para obtener El centro de atencion duenio del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static int obtenerCentroAtencionDuenioPaciente(int codigoPaciente)
	{
		Connection con= UtilidadBD.abrirConexion();
		int retorna= utilidadesDao().obtenerCentroAtencionDuenioPaciente(con, codigoPaciente);
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	



	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static DtoInfoAmparosReclamacion consultarInformacionAmparosReclamacion(Connection con, String ingreso)
	{
		return utilidadesDao().consultarInformacionAmparosReclamacion(con, ingreso);
	}
	
	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static ArrayList<DtoIngresos> consultarIngresosConNotasAdministrativas(
			int codigoPersona) {
		return utilidadesDao().consultarInformacionAmparosReclamacion(codigoPersona);
	}


	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static ArrayList<DtoIngresos> consultarIngresosRegistrosEventosYAccidentes(
			int codigoPersona) {
		return utilidadesDao().consultarIngresosRegistrosEventosYAccidentes(codigoPersona);
	}



	public static ArrayList<DtoFactura> consultarFacturasIngresosConveniosAseguradoras(
			int ingreso) {
		return utilidadesDao().consultarFacturasIngresosConveniosAseguradoras(ingreso);
	}
	
	/**
	 * 
	 */
	public static String facturaTieneReclamacionRadicadaPrevia(int codigo) 
	{
		return utilidadesDao().facturaTieneReclamacionRadicadaPrevia(codigo);
	}



	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public static DtoCertAtenMedicaFurips cargarCetificacionMedicaFurips(
			int ingreso) {
		return utilidadesDao().cargarCetificacionMedicaFurips(ingreso);
	}



	public static DtoCertAtenMedicaFurpro cargarCetificacionMedicaFurpro(
			int ingreso) {
		return utilidadesDao().cargarCetificacionMedicaFurpro(ingreso);
	}



	public static HashMap<String, String> obtenerUltimosSignosVitalesRE(
			Connection con, int idCuenta) {
		return utilidadesDao().obtenerUltimosSignosVitalesRE(con,idCuenta);
	}
}
