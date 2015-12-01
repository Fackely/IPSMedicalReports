package util.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.UtilidadesConsultaExternaDao;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.HorarioAtencion;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author sgomez
 * Clase que contiene las utilidades del módulo de CONSULTA EXTERNA
 */
public class UtilidadesConsultaExterna 
{
	
	private static Logger logger =Logger.getLogger(UtilidadesConsultaExterna.class);
	
	private static String tipoAtencion;
	private static HashMap<String, Object> datosUnidadAgenda;
	
	static
	{
		tipoAtencion = "";
		datosUnidadAgenda = new HashMap<String, Object>();
	}
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesConsultaExternaDao utilidadesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesConsultaExternaDao();
	}
	
	/**
	 * Cadena que verifica si se deben mostrar otros servicios en la respuesta de citas
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static boolean deboMostrarOtrosServiciosCita(Connection con,String codigoCita)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCita", codigoCita);
		return utilidadesDao().deboMostrarOtrosServiciosCita(con, campos);
	}
	
	/**
	 * Método que consulta los otros servicios que no estan en la cita pero que aplican a su unidad de agenda
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static HashMap<String, Object> consultarOtrosServiciosCita(Connection con,String codigoCita, boolean validarSexoPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCita", codigoCita);
		campos.put("validarSexoPaciente",validarSexoPaciente);
		return utilidadesDao().consultarOtrosServiciosCita(con, campos);
	}
	
	/**
	 * Método que consulta los centros de costo X unidad de agenda
	 * @param con
	 * @param codigoUnidadAgenda
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarCentrosCostoXUnidadAgenda(Connection con,String codigoUnidadAgenda, int centroAtencion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoUnidadAgenda",codigoUnidadAgenda);
		campos.put("codigoCentroAtencion",centroAtencion);
		return utilidadesDao().consultarCentrosCostoXUnidadAgenda(con, campos);
	}
	
	public static int consultarCantidadMaximaCitasControlPostOperatorioConvenio(Connection con, int convenio)
	{
		return utilidadesDao().consultarCantidadMaximaCitasControlPostOperatorioConvenio(con,convenio);
	}
	
	public static int consultarDiasControlPostOperatorioConvenio(Connection con, int convenio)
	{
		return utilidadesDao().consultarDiasControlPostOperatorioConvenio(con,convenio);
	}
	public static HashMap validarControlPostOperatorio(Connection con, int paciente, String fechaCita, int cantCPO, int diasCPO, int especialidad)
	{
		return utilidadesDao().validarControlPostOperatorio(con,paciente, fechaCita,cantCPO,diasCPO,especialidad, new Diagnostico("",ConstantesBD.codigoNuncaValido));
	}
	
	/**
	 * Método implementado para validar el control postoperatorio de urgencias
	 * @param con
	 * @param paciente
	 * @param fechaValoracion
	 * @param codigoIngreso
	 * @param diagnostico
	 * @return
	 */
	public static String validarControlPostOperatorioUrgencias(Connection con,int paciente,String fechaValoracion, int codigoIngreso,Diagnostico diagnostico) throws IPSException
	{
		ArrayList<DtoSubCuentas> subCuentas = UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, codigoIngreso, false, new String[0], false, ""/*subcuenta*/,ConstantesBD.codigoViaIngresoUrgencias);
		int cantidadConsultasControlPostOpera = 0;
		int diasControlPostOpera = 0;
		boolean encontro = false;
		String codigoCirugia = "";
		logger.info("*********INICIO VALIDACION DEL CONTROL POSOPERATORIO**********************");
		for(DtoSubCuentas subCuenta:subCuentas)
			if(!encontro)
			{
				//Se consulta la cantidad máxima de consultas del control post-operatorio
				cantidadConsultasControlPostOpera = consultarCantidadMaximaCitasControlPostOperatorioConvenio(con, subCuenta.getConvenio().getCodigo());
				//SE consulta los dias del control post-operatorio
				diasControlPostOpera = consultarDiasControlPostOperatorioConvenio(con, subCuenta.getConvenio().getCodigo());
				logger.info("cantidadConsultasControlPostOpera: "+cantidadConsultasControlPostOpera);
				logger.info("diasControlPostOpera: "+diasControlPostOpera);
				if(cantidadConsultasControlPostOpera>0 && diasControlPostOpera>0)
				{
					HashMap resultado = utilidadesDao().validarControlPostOperatorio(
						con,
						paciente, 
						fechaValoracion,
						cantidadConsultasControlPostOpera,
						diasControlPostOpera,
						ConstantesBD.codigoNuncaValido, 
						diagnostico);
					
					if(Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
					{
						codigoCirugia = resultado.get("codigopo_0").toString();
						encontro = true;
					}
					
				}
			}
		logger.info("*********FIN VALIDACION DEL CONTROL POSOPERATORIO**********************");
		
		return codigoCirugia;
	}
	
	
	
	/**
	 * Método que consulta los consultorios libres que no se encuentren en los horarios de atencion indicados por el 
	 * dia y la hora inicio y final
	 * */
	public static HashMap consultarConsultoriosLibresHorarioAtencion(
			Connection con,
			int centroAtencion,
			int diaSemana,
			String horaInicio,
			String horaFinal)
	{
		return utilidadesDao().consultarConsultoriosLibresHorarioAtencion(con,centroAtencion,diaSemana,horaInicio,horaFinal);
	}
	
	/**
	 * Método implementado para obtener los codigos propietarios de los servicios asociados a una
	 * cita separados por coma
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerListadoCodigosServiciosCita (int codigoCita,int codigoManual)
	{
		Connection con = UtilidadBD.abrirConexion();
		HashMap campos = new HashMap();
		campos.put("codigoManual",codigoManual);
		campos.put("codigoCita",codigoCita);
		String resultado = utilidadesDao().obtenerListadoCodigosServiciosCita(con, campos);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	
	/**
	 * Metodo encargado de consultar el codigo
	 * medico de la cita.
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public static String obtenerCodigoMedicoCita (Connection connection,String numSol)
	{
		return utilidadesDao().obtenerCodigoMedicoCita(connection, numSol);
	}
	
	/**
	 * Método implementado para obtener la fecha/hora de la cita
	 * Retorna Vector de 3 elementos que contiene:
	 * [0] => fecha
	 * [1] => hora inicio
	 * [2] => hora final
	 * @param con
	 * @param codigoCita
	 * @return
	 * Vector de 3 elementos que contiene:
	 * [0] => fecha
	 * [1] => hora inicio
	 * [2] => hora final
	 */
	public static String[] obtenerFechaHoraCita (Connection con,String codigoCita)
	{
		return utilidadesDao().obtenerFechaHoraCita(con, codigoCita);
	}
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static HashMap centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad)
	{
		return utilidadesDao().centrosAtencionValidosXUsuario(con, usuario, actividad);
	}
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return ArrayList<HashMap>
	 * @author Víctor Gómez
	 */
	public static ArrayList<HashMap<String,Object>> centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad, String tipoAtencion)
	{
		return utilidadesDao().centrosAtencionValidosXUsuario(con, usuario, actividad, tipoAtencion);
	}
	
	/**
	 * Metodo implementado para obtener las unidades de agenda validas para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static HashMap unidadesAgendaXUsuario(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion)
	{
		return utilidadesDao().unidadesAgendaXUsuario(con, usuario, centroAtencion, actividad, tipoAtencion, "");
	}
	
	/**
	 * M&eacute;todo implementado para obtener las unidades de agenda v&aacute;lidas para el usuario.
	 * Tambi&eacute;n se involucra en la consulta, si se envian los c&oacute;digos, los servicios 
	 * que deben estar asociados a las unidades de agenda.
	 * 
	 * @param con
	 * @param usuario
	 * @param centroAtencion
	 * @param actividad
	 * @param tipoAtencion
	 * @param codigosServicios
	 * @return ArrayList<HashMap>
	 * @author Jorge Armando Agudelo
	 */
	public static ArrayList<HashMap> unidadesAgendaXUsuarioArray(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion, String codigosServicios)
	{
		return utilidadesDao().unidadesAgendaXUsuarioArray(con, usuario, centroAtencion, actividad, tipoAtencion, codigosServicios);
	}
	
	/**
	 * Metodo implementado para obtener las unidades de agenda separadas por coma, que esten asociadas a "N" actividades 
	 * @param con
	 * @param usuario
	 * @param actividades
	 * @return
	 */
	public static String unidadesAgendaXUsuario2(Connection con, String usuario, ArrayList actividades, String tipoAtencion)
	{
		String respuesta=new String("");
		String respanterior=new String("");
		
		  for(int i=0; i< actividades.size();i++)
		   {
			   HashMap mapa= new HashMap();
			   mapa=utilidadesDao().unidadesAgendaXUsuario(con, usuario, ConstantesBD.codigoNuncaValido, Utilidades.convertirAEntero(actividades.get(i).toString()), tipoAtencion, "");
			    
			    	if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())>0)
			    	{	
			         String[] aux = mapa.get("todos").toString().split(ConstantesBD.codigoNuncaValido+"");
			    	 if(!respanterior.equals(aux[0]))
			    	  {
			    		 respanterior= aux[0]; 
			    		 respuesta+=depurarUnidadesAgenda(respuesta, aux[0]); 
			    	  } 
			    	}
			     
		   }
		  respuesta=respuesta.substring(0, respuesta.length()-2);
		  logger.info("\n UNIDADES DE AGENDA >> "+respuesta);
	   return respuesta;	
	}
	
	/**
	 * Metodo que valida si una actividad asociada a una unidad de agenda un usuario y un centro de atencion está autorizada
	 * @param con
	 * @param unidadAgenda
	 * @param actividad
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public static boolean esActividadAurtorizada(Connection con, int unidadAgenda, int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen)
	{
		return utilidadesDao().esActividadAurtorizada(con,unidadAgenda, actividad, usuario, centroAtencion, isUniAgenOrAgen);
	}
	
    
	/**
	 * Metodo que valida si una actividad asociada a una unidad de agenda un usuario y un centro de atencion está autorizada
	 * o la Cita Tiene un servicio que esté se encuentre entre los Servicios adicionales del Profesional 
	 * @param con
	 * @param codigoCita
	 * @param unidadAgenda
	 * @param actividad
	 * @param usuario
	 * @param centroAtencion
	 * @param isUniAgenOrAgen
	 * @return
	 */
	public static boolean esActividadAurtorizadaOServAddProf(Connection con,int codigoCita, int unidadAgenda, int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen)
	{
		return utilidadesDao().esActividadAurtorizadaOServAddProf(con,codigoCita,unidadAgenda, actividad, usuario, centroAtencion, isUniAgenOrAgen);
	}
	
	/**
	 * Método para obtener el codigo de la agenda de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static String obtenerCodigoAgendaCita(Connection con,String codigoCita)
	{
		return utilidadesDao().obtenerCodigoAgendaCita(con, codigoCita);
	}
	
	/**
	 * Método que verifica si un profesional de la salud tiene agenda generada
	 * @param con
	 * @param codigoProfesional
	 * @return
	 */
	public static boolean tieneProfesionalAgendaGenerada(Connection con,int codigoProfesional)
	{
		return utilidadesDao().tieneProfesionalAgendaGenerada(con, codigoProfesional);
	}
	
	/**
	 * Método que verifica si el paciente tiene citas con estado NO ASISTIÓ 
	 * que esten entre los días de restricción.
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean pacienteConInasistencia(Connection con,int codigoPaciente,int numeroDias)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("numeroDias", numeroDias);
		return utilidadesDao().pacienteConInasistencia(con, campos);
	}
	
	
	/**
	 * Metodo que elimina los datos repetidos de dos cadenas de numeros separados por comas
	 * @param actual
	 * @param nuevo
	 * @return
	 */
	private static String depurarUnidadesAgenda(String actual, String nuevo)
	{
	  String resp=new String("");	
	  String[] aux1=actual.split(",");
	  String[] aux2=nuevo.split(",");
	  
	  for(int i=0;i < aux1.length;i++)
	    {
		  for(int j=0;j<aux2.length;j++)
		  {
			  if(Utilidades.convertirAEntero(aux1[i]) == Utilidades.convertirAEntero(aux2[j]))
			  {
				  aux2[j]=ConstantesBD.codigoNuncaValido+"";
			  }
		  }
	    }
	  
	  for(int k=0;k<aux2.length;k++)
	  {
		  
		  if(!aux2[k].equals(ConstantesBD.codigoNuncaValido+""))
		  {
			  if(k == aux2.length-1)
			  {
			   resp+=aux2[k];
			  }
			  else{
				   resp+=aux2[k]+", ";   
			  }
		  }
		  
	  }
	  return resp;	
	}

	/**
	 * Metodo utilizado para consultar lo estados de las citas
	 * @param connection
	 * @param codigosEstados
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultaEstadosCita(Connection connection,
			ArrayList<Integer> codigosEstados) {
		return utilidadesDao().consultaEstadosCita(connection, codigosEstados);
	}
	public  static  HashMap consultaMotivosAnulacionCondonacionMulta(Connection con, int codInstitucion)
	{
		return utilidadesDao().consultaMotivosAnulacionCondonacionMulta(con, codInstitucion);	
	}
	
	/**
	 * Verifica si la Institucion Maneja Multas poerImcumplimiento de Citas Según la Institucion y el Modulo
	 * que para este caso es Consulta Externa
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 * 
	 */
	 
	public static ArrayList<HashMap<String, Object>> institucionManejaMultasIncumCitas(Connection con, String cod_institucion, int cod_modulo, String[] parmetros_modulo){
		HashMap parametros = new HashMap(); 
		parametros.put("institucion", cod_institucion);
		parametros.put("modulo", cod_modulo);
		parametros.put("parametrosModulo", parmetros_modulo);
		return utilidadesDao().institucionManejaMultasIncumCitas(con, parametros);
	}
	
	/**
	 * Verifica el estado de las citas del paciente
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 */
	public static ArrayList<HashMap<String, Object>> estadoCitasPaciente(Connection con, int cod_paciente, String fechaIniciControlMultas, boolean filtarConvenio){
		HashMap parametros = new HashMap(); 
		parametros.put("codPaciente", cod_paciente);
		parametros.put("fechaIniControl", fechaIniciControlMultas);
		parametros.put("fitralconvenio", filtarConvenio?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return utilidadesDao().estadoCitasPaciente(con, parametros);
	}
	public static HashMap ObtenerUnidadesAgendaXcentrosAtencion(Connection con,  int conscutivoCentroAtencion, String tipoAtencion, Boolean activas)
	{
		
		return utilidadesDao().UnidadesAgendaXcentrosAtencion(con, conscutivoCentroAtencion, tipoAtencion, activas);
	}
	
	
	public static ArrayList<DtoConsultorios> ObtenerConsultorios(Connection con,  int conscutivoCentroAtencion)
	{
		
		return utilidadesDao().consultoriosXcentrosAtencion(con, conscutivoCentroAtencion);
	}
	
	
	/**
	 * Método para cargar los profesionales que aplican según un unidad de agenda
	 * @param con
	 * @param UnidadAgenda
	 * @param usuario
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>>  obtenerProfesionalesXUnidaAgenda(Connection con, String UnidadAgenda, UsuarioBasico usuario)
	{
		HorarioAtencion horarioAten = new HorarioAtencion();
		ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> arrayProfesionales = new ArrayList<HashMap<String,Object>>();
		
		if(Utilidades.convertirAEntero(UnidadAgenda)!=ConstantesBD.codigoNuncaValido)
		{
			HashMap<String, Object> datosUnidadAgenda = horarioAten.getEspecialidad(con, UnidadAgenda);
			
			
			
			if(!(datosUnidadAgenda.get("profesionales")+"").isEmpty())
			{
			
				if(Utilidades.convertirAEntero(datosUnidadAgenda.get("especialidad").toString()) != ConstantesBD.codigoNuncaValido
						&& datosUnidadAgenda.containsKey("especialidad")
						)
				{				
					if(datosUnidadAgenda.get("profesionales").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
					{
						
						array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, UnidadAgenda);
						arrayProfesionales = UtilidadesAdministracion.obtenerProfesionales(con, 
								usuario.getCodigoInstitucionInt(), 
								Utilidades.convertirAEntero(datosUnidadAgenda.get("especialidad").toString()), 
								false, true, array);
					}else{
						if(datosUnidadAgenda.get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioProcedimiento)
						{
							array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(con, UnidadAgenda);
							arrayProfesionales = UtilidadesAdministracion.obtenerProfesionales(con, 
									usuario.getCodigoInstitucionInt(), 
									ConstantesBD.codigoNuncaValido, 
									false, true, array);
						}else{
							if(datosUnidadAgenda.get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioInterconsulta)
							{
								arrayProfesionales = UtilidadesAdministracion.obtenerProfesionales(con, 
										usuario.getCodigoInstitucionInt(), 
										Utilidades.convertirAEntero(datosUnidadAgenda.get("especialidad").toString()),
										false, true, array);
							}
						}
					}
				}
			}
		}
		
		return arrayProfesionales;
		
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap validacionesBloqueoAtencionCitas(Connection con,UsuarioBasico usuario , int codigoPaciente)
	{
		return utilidadesDao().validacionesBloqueoAtencionCitas(con, usuario, codigoPaciente);
	}
	
	/**
	 * consulta los numeros de solicitud que estan asociados a una cita y que no 
	 * cuenta con una factura, y en caso de contar
	 * con ella su estado no es facturada.
	 * @param con
	 * @param codigoCita
	 * @return numeros de solicitudes
	 */
	public static ArrayList<Integer> consultarSolicitudesSinFacturar (Connection con, int codigoCita)
	{
		return utilidadesDao().consultarSolicitudesSinFacturar(con, codigoCita);
	}
	
	/**
	 * consulta los consutorios 
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static ArrayList<HashMap> consultoriosCentroAtencionTipo(Connection con, int institucion, int centroAtencion)
	{
		return utilidadesDao().consultoriosCentroAtencionTipo(con, institucion, centroAtencion);
	}
	
	/**
	 * Método para obtener el codigo medico de la agenda
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public static  int obtenerCodigoMedicoAgenda(Connection con,int codigoAgenda)
	{
		return utilidadesDao().obtenerCodigoMedicoAgenda(con, codigoAgenda);
	}
	
	/**
	 * metodo que obtiene las actividades autorizadas por una unidad de agenda
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param usuario
	 * @param institucion
	 * @return ArrayList<HashMap>
	 * @author Víctor Hugo Gómez L.
	 */
	public static ArrayList<HashMap> actividadesAutorizadasXUniAgend(Connection con, int centroAtencion, int unidadAgenda, String usuario, int institucion)
	{
		return utilidadesDao().actividadesAutorizadasXUniAgend(con, centroAtencion, unidadAgenda, usuario, institucion);
	}
	
	/**
	 * obtener los motivos cancelacion de un cita
	 * @param con
	 * @param activo
	 * @param tipoCancelacion
	 * @return
	 */
	public static ArrayList<HashMap> obtenerMotivosCancelacion(Connection con, String activo, String tipoCancelacion)
	{
		return utilidadesDao().obtenerMotivosCancelacion(con, activo, tipoCancelacion);
	}
	
	/**
	 * obtener los motivos cancelacion de un cita
	 * @param con
	 * @param activo
	 * @param tipoCancelacion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap> obtenerMotivosCancelacion(String activo, String tipoCancelacion)
	{
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<HashMap> lista=  utilidadesDao().obtenerMotivosCancelacion(con, activo, tipoCancelacion);
		UtilidadBD.closeConnection(con);
		return lista;
	}
	
	/**
	 * Método implementado para verificar 
	 * @param con
	 * @param usuario
	 * @param codigoUnidadAgenda
	 * @return
	 */
	public static boolean validarCentrosCostoUsuarioEnUnidadAgenda(Connection con,UsuarioBasico usuario, int codigoUnidadAgenda)
	{
		return utilidadesDao().validarCentrosCostoUsuarioEnUnidadAgenda(con, usuario, codigoUnidadAgenda);
	}

	/**
	 * @return the tipoAtencion
	 */
	public static String getTipoAtencion() {
		return tipoAtencion;
	}

	/**
	 * @param tipoAtencion the tipoAtencion to set
	 */
	public static void setTipoAtencion(String tipoAtencion) {
		UtilidadesConsultaExterna.tipoAtencion = tipoAtencion;
	}

	/**
	 * @return the datosUnidadAgenda
	 */
	public static HashMap<String, Object> getDatosUnidadAgenda() {
		return datosUnidadAgenda;
	}

	/**
	 * @param datosUnidadAgenda the datosUnidadAgenda to set
	 */
	public static void setDatosUnidadAgenda(
			HashMap<String, Object> datosUnidadAgenda) {
		UtilidadesConsultaExterna.datosUnidadAgenda = datosUnidadAgenda;
	}
	
	/**
	 * Método para obtener los paises permitidos para un usuario según la parametrizacion de la funcinalidad
	 * unidade sde agenda por susuario por centro de atencion
	 * @param con
	 * @param loginUsuario
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(Connection con,String loginUsuario,String tipoAtencion)
	{
		return utilidadesDao().obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(con, loginUsuario,tipoAtencion);
	}


	/**
	 * Método implementado para cargar las ciudades permitidas para un usuario según la parametrizacion
	 * de unidades de agenda x usuario x centro de atencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(Connection con,String loginUsuario,String codigoPais,String tipoAtencion)
	{
		HashMap parametros = new HashMap();
		parametros.put("loginUsuario",loginUsuario);
		parametros.put("codigoPais",codigoPais);
		parametros.put("tipoAtencion",tipoAtencion);
		return utilidadesDao().obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(con, parametros);
	}
	
	/**
	 * Método para obtener los centros de atencion permitios x usuario según la parametrizacion
	 * de unidades de agenda x usuario x centro de atencionn filtrando por ciudad
	 * @param con
	 * @param parametros
	 * @return
	 */
	
	public static ArrayList<HashMap<String, Object>> obtenerCentrosAtencionPermitidosUsuarioXCiudad(Connection con,String codigoPais,String codigoCiudad,String codigoDepto,String loginUsuario,String tipoAtencion)
	{
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("codigoPais", codigoPais);
		parametros.put("codigoCiudad", codigoCiudad);
		parametros.put("codigoDepto", codigoDepto);
		parametros.put("loginUsuario",loginUsuario);
		parametros.put("tipoAtencion", tipoAtencion);
		
		return utilidadesDao().obtenerCentrosAtencionPermitidosUsuarioXCiudad(con, parametros);
	}

	/**
	 * 
	 */
	public static int obtenerCodigoMedicoAgendaXCita(Connection con,int codigoCita)
	{
		return utilidadesDao().obtenerCodigoMedicoAgendaXCita(con,codigoCita);
	}
	
	/**
	 * 
	 */
	public static int obtenerCodigoMedicoRespondeSolicitud(Connection con, int nroSolicitud)
	{
		return utilidadesDao().obtenerCodigoMedicoRespondeSolicitud(con,nroSolicitud);
	}
	
	/**
	 * 
	 */
	public static int obtenerSolicitudXCita(Connection con, int cita)
	{
		return utilidadesDao().obtenerSolicitudXCita(con, cita);
	}
	
	/**
	 * M&eacute;todo que devuelve las unidades de Agenda asociadas a un usuario, 
	 * a un centro de atenci&oacute;n y a un conjunto de servicios
	 * @param con
	 * @param loginUsuario
	 * @param codigoCentroAtencion
	 * @param codigoActividadAutorizadaReservarCitas
	 * @return
	 */
	public static HashMap unidadesAgendaXUsuarioXServicio(Connection con,
			String usuario, int centroAtencion,
			int actividad, String tipoAtencion, String codigosServicios) {
		
		return utilidadesDao().unidadesAgendaXUsuario(con, usuario, centroAtencion, actividad, tipoAtencion, codigosServicios);
	}
	
	/**
	 * Método que verifica las actividades autorizadas para el usuario al listar las citas odontológicas programadas
	 * @param con Comexión con la bD
	 * @param cita Código de la cita
	 * @param actividad Actividad a avalidar
	 * @param usuario Usuario que realiza la consulta
	 * @param centroAtencion Centro de atención del usuario
	 * @return true en caso de tener autorizada la actividad
	 */
	public static boolean esActividadAurtorizadaProgramacionCitaOdo(Connection con, int cita, int actividad, String usuario, int centroAtencion){
		return utilidadesDao().esActividadAurtorizadaProgramacionCitaOdo(con, cita, actividad, usuario, centroAtencion);
	}
	
	public static String esReservaOrdenAmbulatoria(Connection con, int codigoCita) 
	{
		return utilidadesDao().esReservaOrdenAmbulatoria(con, codigoCita);
	}

	/**
	 * 
	 * @param tipo
	 * @param codigoEspecialidad
	 * @param codigoCentroAtencion
	 * @param filtrarActivas
	 * @return
	 */
	public static ArrayList<DtoUnidadesConsulta> obtenerUnidadesAgendaXcentrosAtencionXEspecialidad(String tipo, int codigoEspecialidad, int codigoCentroAtencion,boolean filtrarActivas) 
	{
		ArrayList<DtoUnidadesConsulta> resultado= new ArrayList<DtoUnidadesConsulta>();
		Connection con=UtilidadBD.abrirConexion();
		resultado= utilidadesDao().obtenerUnidadesAgendaXcentrosAtencionXEspecialidad(con, tipo,codigoEspecialidad,codigoCentroAtencion,filtrarActivas);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Método que permite verificar si existe agenda creada para un centro de costo y las unidades de agenda
	 * asociadas a un servicio para una fecha mayor o igual o la fecha pasada por parámetro
	 * @param codigoCentroCostro
	 * @param codigoServicio
	 * @param fecha
	 * @return
	 */
	public static boolean existeAgendaXCentroCostoXServicio(Connection con, int codigoCentroCosto, int codigoServicio, String fecha) 
	{
		return utilidadesDao().existeAgendaXCentroCostoXServicio(con, codigoCentroCosto, codigoServicio, fecha);
	}
}
