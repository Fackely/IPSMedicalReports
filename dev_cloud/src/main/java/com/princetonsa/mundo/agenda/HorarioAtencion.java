/*
* @(#)HorarioAtencion.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.mundo.agenda;

import java.io.Serializable;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.actionform.agenda.HorarioAtencionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HorarioAtencionDao;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;

/**
* Esta clase encapsula los atributos y la funcionalidad del
* Horario de Atenciï¿½n.
*
* @version 1.0, Sep 1, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class HorarioAtencion implements Serializable
{
	
	private transient static Logger logger = Logger.getLogger(HorarioAtencion.class);
	
	/** El DAO usado por el objeto <code>HorarioAtencion</code> para acceder a la fuente de datos */
	private static HorarioAtencionDao ihad_dao = null;

	/** Identificador ï¿½nico del horario de atenciï¿½n */
	private int ii_codigo;

	/** Cï¿½digo del consultorio asignado el horario de atenciï¿½n */
	private int ii_consultorio;
	
	private String tipoAtencion="";

	public String getTipoAtencion() {
		return tipoAtencion;
	}

	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}

	/** Cï¿½digo del dï¿½a de la semana asignado al horario de atenciï¿½n
	* 1 - Lunes
	* 2 - Martes
	* 3 - Miï¿½rcoles
	* 4 - Jueves
	* 5 - Viernes
	* 6 - Sï¿½bado
	* 7 - Domingo
	*/
	private int ii_diaSemana;

	/** Tiempo de duraciï¿½n (en minutos) de una consulta en este horario de atenciï¿½n */
	private int ii_duracionConsulta;

	/** Mï¿½ximo nï¿½mero de pacientes que se pueden atender por sesiï¿½n de consulta */
	private int ii_pacientesSesion;

	/** Cï¿½digo de la unidad de Agenda asignada al horario de atenciï¿½n */
	private int ii_unidadAgenda;
	private int codigoCentroAtencion;

	/** Identificaciï¿½n del mï¿½dico asignado al horario de atenciï¿½n */
	private int ii_medico;

	/** Nombre del consultorio asignado al horario de atenciï¿½n */
	private String is_consultorio;

	/** Dï¿½a de la semana asignado al horario de atenciï¿½n */
	private String is_diaSemana;

	/** Hora de finalizaciï¿½n del horario de atenciï¿½n para el dï¿½a de la semana especificado */
	private String is_horaFin;

	/** Hora de inicio del horario de atenciï¿½n para el dï¿½a de la semana especificado */
	private String is_horaInicio;

	/** Nombre completo del mï¿½dico asignado al horario de atenciï¿½n */
	private String is_nombreMedico;

	/** Unidad de consulta asignada al horario de atenciï¿½n */
	private String is_unidadConsulta;
	
    /**
     * Centro de atenciï¿½n que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atenciï¿½n del usuario  
     */
    private int centroAtencion;
    
    /**
     * Nombre del Centro de atenciï¿½n que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atenciï¿½n del usuario
     * se utiliza para la consulta  
     */
    private String nombreCentroAtencion;

    
    //----------------------------------
	//Cambio Funcionalidades Consulta Ext --Anexo 810
	private ArrayList<HashMap<String,Object>> profesionaleSaludUniAgen;
	private String indexCodUniAgen;
	private HashMap<String,Object> datosUnidadAgenda;
	//----------------------------------
    
	/**
	* Construye un horario de atenciï¿½n sin ningï¿½n atributo especificado.
	*/
	public HorarioAtencion()
	{
		clean();
		init();
	}

	/**
	* Dada la identificacion de un horario de atenciï¿½n, carga los datos correspondientes desde la
	* fuente de datos.
	* @param ac_con		Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo	Unidad de consulta a la que serï¿½ asignado el horario de atenciï¿½n
	 * @param institucion 
	*/
	public void cargarHorarioAtencion(Connection ac_con, int ai_codigo, int institucion)throws Exception
	{
		
		HashMap  ldb_horario;	
		/* Obtener los valores de los atributos desde la fuente de datos */
		ldb_horario =  ihad_dao.cargarHorarioAtencion(ac_con, ai_codigo);
			
		/* Validar si se encontrï¿½ el horario de atenciï¿½n especificado */
		if(ldb_horario != null)
		{
			Integer	li_medico;
			String	ls_apellidosMedico;
			String	ls_aux;
			String	ls_nombresMedico;

			/* Establecer el valor de las propiedades del horario de atenciï¿½n */
			
			ii_codigo			= ai_codigo;
			nombreCentroAtencion= ldb_horario.get("nombrecentroatencion")+"";
			ii_consultorio		= ( Utilidades.convertirAEntero(ldb_horario.get("codigoconsultorio") +""));
			ii_diaSemana		= ( Utilidades.convertirAEntero(ldb_horario.get("codigodiasemana") +""));
			ii_duracionConsulta	= ( Utilidades.convertirAEntero(ldb_horario.get("tiemposesion") +""));
			ii_pacientesSesion	= ( Utilidades.convertirAEntero(ldb_horario.get("pacientessesion")+""));
			ii_unidadAgenda	= ( Utilidades.convertirAEntero(ldb_horario.get("codigounidadconsulta") +""));
			centroAtencion      = ( Utilidades.convertirAEntero(ldb_horario.get("codigocentroatencion") +""));
			this.tipoAtencion= ldb_horario.get("tipoatencion")+"";
					
			ArrayList<HashMap<String, Object>> array = new ArrayList<HashMap<String, Object>>();
			this.getProfesionaleSaludUniAgen().clear();
			/*if(ii_unidadAgenda!=ConstantesBD.codigoNuncaValido)
			{
				this.setIndexCodUniAgen(ii_unidadAgenda+"");
				this.setDatosUnidadAgenda(this.getEspecialidad(ac_con, ii_unidadAgenda+""));
				if(Utilidades.convertirAEntero(this.getDatosUnidadAgenda().get("especialidad").toString()) != ConstantesBD.codigoNuncaValido
						&& this.getDatosUnidadAgenda().containsKey("especialidad")
						)
				{
					
					if(this.getDatosUnidadAgenda().get("profesionales").toString().equals(ConstantesIntegridadDominio.acronimoAmbos)
							||(	this.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioProcedimiento))
					{
						//se consultan los codigos del centro de costo asociados a la unidad de consulta
						array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(ac_con, this.getIndexCodUniAgen());
					}
					
					this.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(ac_con, 
							usuario.getCodigoInstitucionInt(), 
							Utilidades.convertirAEntero(this.getDatosUnidadAgenda().get("especialidad").toString()), 
							false, true, array));
				}
			}*/
			
			if(ii_unidadAgenda!=ConstantesBD.codigoNuncaValido)
			{
				this.setIndexCodUniAgen(ii_unidadAgenda+"");
				this.setDatosUnidadAgenda(this.getEspecialidad(ac_con, ii_unidadAgenda+""));
				if(Utilidades.convertirAEntero(this.getDatosUnidadAgenda().get("especialidad").toString()) != ConstantesBD.codigoNuncaValido
						&& this.getDatosUnidadAgenda().containsKey("especialidad")
						)
				{
					if(this.getDatosUnidadAgenda().get("profesionales").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
					{
						
						array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(ac_con, this.getIndexCodUniAgen());
						this.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(ac_con, 
								institucion, 
								Utilidades.convertirAEntero(this.getDatosUnidadAgenda().get("especialidad").toString()), 
								false, true, array));
					}else{
						if(!UtilidadTexto.isEmpty((String)this.getDatosUnidadAgenda().get("profesionales")))
						{
							if(this.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioProcedimiento)
							{
								array = UtilidadesAdministracion.obtenerCodCentroCostoXUnidadConsulta(ac_con, this.getIndexCodUniAgen());
								this.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(ac_con, 
										institucion, 
										ConstantesBD.codigoNuncaValido, 
										false, true, array));
							}else{
								if(this.getDatosUnidadAgenda().get("profesionales").toString().charAt(0) == ConstantesBD.codigoServicioInterconsulta)
								{
									this.setProfesionaleSaludUniAgen(UtilidadesAdministracion.obtenerProfesionales(ac_con, 
											institucion, 
											Utilidades.convertirAEntero(this.getDatosUnidadAgenda().get("especialidad").toString()),
											false, true, array));
								}
							}
						}
					}
				}
			}
			
			
			
			if((ldb_horario.get("nombreconsultorio")+"") != null)
			{
				is_consultorio		= (ldb_horario.get("nombreconsultorio")+"");
				logger.info("is_consultorio : "+is_consultorio);
			}
			else
			{
				is_consultorio = "No asignado";
			}
			
			if((ldb_horario.get("nombrediasemana")+"") != null)
				is_diaSemana =  (ldb_horario.get("nombrediasemana")+"");
			else
				is_diaSemana = "No Asignado";
			if ((ldb_horario.get("nombreunidadconsulta")+"") != null)
				is_unidadConsulta	= (ldb_horario.get("nombreunidadconsulta")+"");
			else
				is_unidadConsulta = "No Asignado";
			if( (ldb_horario.get("nombrecentroatencion")+"")!= null)
				nombreCentroAtencion	= (ldb_horario.get("nombrecentroatencion")+"");
			else
				nombreCentroAtencion="No Asignado";
			if( (li_medico = Utilidades.convertirAEntero(ldb_horario.get("codigomedico")+ " " )) != null )
				ii_medico = li_medico.intValue();
			else
				ii_medico = -1;
			if( (ls_aux = (ldb_horario.get("primerapellidomedico")+"") ) != null)
				ls_apellidosMedico = ls_aux.trim();
			else
				ls_apellidosMedico = "";
			if( (ls_aux = (ldb_horario.get("segundoapellidomedico")+"") ) != null)
				ls_apellidosMedico = (ls_apellidosMedico + " " + ls_aux).trim();
			else
				ls_apellidosMedico ="";
			if( (ls_aux = ldb_horario.get("primernombremedico")+"" ) != null)
				ls_nombresMedico = ls_aux.trim();
			else
				ls_nombresMedico = "";
			if( (ls_aux = (ldb_horario.get("segundonombremedico")+"") ) != null)
				ls_nombresMedico = (ls_nombresMedico + " " + ls_aux).trim();
			else
				ls_nombresMedico ="";
			if(!ls_apellidosMedico.equals("") && !ls_nombresMedico.equals("") )
				ls_apellidosMedico += ", ";
			else if(ls_apellidosMedico.equals("") && ls_nombresMedico.equals("") )
				ls_apellidosMedico = "No asignado";
			
			is_nombreMedico = ls_apellidosMedico + ls_nombresMedico;
			is_horaFin		= (UtilidadFecha.convertirHoraACincoCaracteres(ldb_horario.get("horafin")+""));
			is_horaInicio	= (UtilidadFecha.convertirHoraACincoCaracteres(ldb_horario.get("horainicio")+""));
			
			
		}
		else
		{
			
			clean();
		}
	}

	/** Inicializa los atributos del objeto a valores vacï¿½os (invï¿½lidos) */
	public void clean()
	{
		ii_codigo			= -1;
		ii_consultorio		= -1;
		ii_diaSemana		= -1;
		ii_duracionConsulta	= 0;
		ii_medico			= -1;
		ii_pacientesSesion	= 0;
		ii_unidadAgenda	= -1;

		is_consultorio		= "";
		is_diaSemana		= "";
		is_horaFin			= "";
		is_horaInicio		= "";
		is_nombreMedico		= "";
		is_unidadConsulta	= "";
		nombreCentroAtencion = "";
		codigoCentroAtencion = ConstantesBD.codigoNuncaValido;
		
		// anexo 810
		this.profesionaleSaludUniAgen = new ArrayList<HashMap<String,Object>>() ;
		this.indexCodUniAgen = "";
		this.datosUnidadAgenda = new HashMap<String,Object>();
	}

	/**
	* Elimina este horario de atenciï¿½n de la fuente de datos
	* @param ac_con Conexiï¿½n abierta con una fuente de datos
	* @return Indicador de ï¿½xito de la operaciï¿½n
	*/
	public boolean eliminarHorarioAtencion(Connection ac_con)throws Exception
	{
		boolean lb_resp;

		/* Validar si la eliminaciï¿½n puede realizarse */
		if(ii_codigo > 0)
		{
		    lb_resp = ihad_dao.eliminarHorarioAtencion(ac_con, ii_codigo) == 1;
			if(lb_resp )
				ii_codigo = -1;
		}
		else
			lb_resp = false;

		return lb_resp;
	}

	/**
	* Elimina el horario de atenciï¿½n identificado con el cï¿½digo ai_codigo
	* @param ac_con		Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo	Identificador ï¿½nico del horario de atenciï¿½n a eliminar
	* @return Indicador de ï¿½xito de la operaciï¿½n
	*/
	public static boolean eliminarHorarioAtencion(
		Connection	ac_con,
		int			ai_codigo
	)throws Exception
	{
		boolean lb_resp;

		/* Validar si la eliminaciï¿½n puede realizarse */
		if(ai_codigo > 0)
			lb_resp =
				DaoFactory.getDaoFactory(
					System.getProperty("TIPOBD")
				).getHorarioAtencionDao().eliminarHorarioAtencion(ac_con, ai_codigo) == 1;
		else
			lb_resp = false;

		return lb_resp;
	}

	/**
	* Elimina los horario de atenciï¿½n identificados por los cï¿½digos contenidos en aia_codigos
	* @param ac_con			Conexiï¿½n abierta con una fuente de datos
	* @param aia_codigos	Conjunto de identificadores de horarios de atenciï¿½n a eliminar
	* @return Indicador de ï¿½xito de la operaciï¿½n
	*/
	public static boolean eliminarHorarioAtencion(
		Connection	ac_con,
		int[]		aia_codigos
	)throws Exception
	{
		boolean lb_resp;

		/* Validar si la eliminaciï¿½n puede realizarse */
		if(aia_codigos.length > 0)
		{
			lb_resp =
				DaoFactory.getDaoFactory(
					System.getProperty("TIPOBD")
				).getHorarioAtencionDao().eliminarHorarioAtencion(
					ac_con, aia_codigos
				) == aia_codigos.length;
		}
		else
			lb_resp = false;

		return lb_resp;
	}

	/**
	* Obtiene el cï¿½digo del horario de atenciï¿½n
	* @return Identificador ï¿½nico de este horario de atenciï¿½n
	*/
	public int getCodigo()
	{
		return ii_codigo;
	}

	/**
	* Obtiene el cï¿½digo del consultorio del horario de atenciï¿½n
	* @return Cï¿½digo del consulorio de este horario de atenciï¿½n
	*/
	public int getCodigoConsultorio()
	{
		return ii_consultorio;
	}

	/**
	* Obtiene el cï¿½digo del dï¿½a de la semana del horario de atenciï¿½n
	* @return Cï¿½digo del dï¿½a de la semana de este horario de atenciï¿½n
	*/
	public int getCodigoDiaSemana()
	{
		return ii_diaSemana;
	}

	/**
	* Obtiene el cï¿½digo del mï¿½dico del horario de atencio
	* @return Cï¿½digo del mï¿½dico asignado a este horario de atenciï¿½n
	*/
	public int getCodigoMedico()
	{
		return ii_medico;
	}

	/**
	* Obtiene la unidad de consulta del horario de atenciï¿½n
	* @return Cï¿½digo de la unidad de consulta de este horario de atenciï¿½n
	*/
	public int getCodigoUnidadConsulta()
	{
		return ii_unidadAgenda;
	}

	/**
	* Obtiene el consultorio del horario de atenciï¿½n
	* @return Nombre del consultorio de este horario de atenciï¿½n
	*/
	public String getConsultorio()
	{
		return is_consultorio;
	}

	/**
	* Obtiene el dï¿½a de la semana del horario de atenciï¿½n
	* @return Dï¿½a de la semana al cual fue asignado este horario de atenciï¿½n
	*/
	public String getDiaSemana()
	{
		return is_diaSemana;
	}

	/**
	* Obtiene el tiempo de duraciï¿½n (en minutos) de una consulta en este horario de atenciï¿½n
	* @return Tiempo en minutos que dura un sesiï¿½n (consulta) en este horario de atenciï¿½n
	*/
	public int getDuracionConsulta()
	{
		return ii_duracionConsulta;
	}

	/**
	* Obtiene la hora de finalizaciï¿½n del horario de atenciï¿½n para el dï¿½a de la semana
	* @return Hora de finalizaciï¿½n de este horario de atenciï¿½n
	*/
	public String getHoraFin()
	{
		return is_horaFin;
	}

	/**
	* Obtiene la hora de inicio del horario de atenciï¿½n para el dï¿½a de la semana
	* @return Hora de inicio de este horario de atenciï¿½n
	*/
	public String getHoraInicio()
	{
		return is_horaInicio;
	}

	/**
	* Obtiene el nombre del mï¿½dico asignado al horario de atenciï¿½n
	* @return Nombre del mï¿½dico asignado a este horario de atenciï¿½n
	*/
	public String getMedico()
	{
		return is_nombreMedico;
	}

	/**
	* Obtiene el nï¿½mero mï¿½ximo de pacientes que se pueden atender por sesiï¿½n o consulta
	* @return Nï¿½mero mï¿½ximo de pacientes que se pueden atender por sesiï¿½n o consulta en este
	* horario de atenciï¿½n
	*/
	public int getPacientesSesion()
	{
		return ii_pacientesSesion;
	}

	/**
	* Obtiene la unidad de consulta del horario de atenciï¿½n
	* @return Nombre de la unidad de consulta de este horario de atenciï¿½n
	*/
	public String getUnidadConsulta()
	{
		return is_unidadConsulta;
	}

	/** Inicializa el acceso a bases de datos de este objeto */
	public void init()
	{
		// Obtengo el DAO que encapsula las operaciones de BD de este objeto
		if(ihad_dao == null)
			ihad_dao =
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getHorarioAtencionDao();
	}
	
	
	
	
	/**
	 * 
	 * @param Conneciont con
	 * @param boolean esModificacion
	 * @param HashMap diasSemanaMap
	 * */
	public boolean  interfazTipoInserccionHorario(Connection con,
												  boolean esModificacion,
										  		  HashMap diasSemanaMap,String tipoAtencion, int institucion)
										 		 throws Exception
	{	
		
		String diasadicionados="";
		
		//verifica si existen los datos requeridos
		if(((tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral) && ii_duracionConsulta > 0)
				|| tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica)) 
				&& ii_pacientesSesion	> 0
				&&	ii_unidadAgenda	> 0 && !is_horaFin.equals("")
					&& !is_horaInicio.equals(""))
		{	
		
			logger.info("\n\nentra ok!!");
			//Vetifica que existen dias seleccionados
			if(diasSemanaMap.get("haySeleccion").toString().equals(ConstantesBD.acronimoSi))
			{
				//Recorre el listado los dias de la semana, para verifica cuales fueron seleccionados		
				for(int i = 0; i< Integer.parseInt(diasSemanaMap.get("numRegistros").toString()); i++)
				{
					if(diasSemanaMap.get("seleccionado_"+i).equals(ConstantesBD.acronimoSi))
					{
						if(!esModificacion)
						{			
							ii_codigo = ihad_dao.insertarHorarioAtencion(con,
																		 centroAtencion,  	
																		 ii_unidadAgenda,
																		 ii_consultorio,
																		 Integer.parseInt(diasSemanaMap.get("codigo_"+i).toString()),
																		 ii_medico,
																		 is_horaInicio,
																		 is_horaFin,
																		 ii_duracionConsulta,
																		 ii_pacientesSesion);
							
							if(ii_codigo < 0)							
								return false;
							
							diasadicionados += diasSemanaMap.get("dia_"+i).toString().trim()+",";
						}
						else
						{
							if(ihad_dao.modificarHorarioAtencion(con,
																 centroAtencion,
																 ii_codigo,
																 ii_unidadAgenda,
																 ii_consultorio,
																 Integer.parseInt(diasSemanaMap.get("codigo_"+i).toString()),
																 ii_medico,
																 is_horaInicio,
																 is_horaFin,
																 ii_duracionConsulta,
																 ii_pacientesSesion
																 ) == 1)
							{
								esModificacion = false;
								diasadicionados += diasSemanaMap.get("dia_"+i).toString().trim()+",";
							}
							else{
								return false;
							}
								
						}						
					}
				}	
				
				cargarHorarioAtencion(con, ii_codigo, institucion);
				diasadicionados = diasadicionados.substring(0,diasadicionados.length()-1);
				is_diaSemana = diasadicionados;
				return true;
			}	
			else
			{
				if(!esModificacion)
				{
					ii_codigo = ihad_dao.insertarHorarioAtencion(con,
																 centroAtencion, 
																 ii_unidadAgenda,
																 ii_consultorio,
																 ConstantesBD.codigoNuncaValido,
																 ii_medico,
																 is_horaInicio,
																 is_horaFin,
																 ii_duracionConsulta,
																 ii_pacientesSesion);
					if(ii_codigo > -1)
					{
						cargarHorarioAtencion(con, ii_codigo, institucion);
						return true;
					}
					else				
						return false;						
				}
				else
				{
					if(ihad_dao.modificarHorarioAtencion(con,
														centroAtencion,
														ii_codigo,
														ii_unidadAgenda,
														ii_consultorio,
														ConstantesBD.codigoNuncaValido,
														ii_medico,
														is_horaInicio,
														is_horaFin,
														ii_duracionConsulta,
														ii_pacientesSesion
														) == 1)
					{
						cargarHorarioAtencion(con, ii_codigo, institucion);
						return true;
					}									
				}						
			}
		}
		else
			return false;
			
		return false;				
	}
	
	/**
	* Lista los horarios de atención de la busqueda avanzada
	* @return <code>Collection</code> de los horarios de atencion
	*/
	public static Collection listarHorariosAtencion(Connection con, int codigoInstitucion, String usuario, int centroAtencion, int unidadAgenda, String consultorioAsignado, int consultorio, String profesionalAsignado, int codigoMedico,ArrayList<Integer> diasSeleccionados) {
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("centroAtencion", centroAtencion);
		parametros.put("unidadAgenda", unidadAgenda);
		parametros.put("consultorioAsignado", consultorioAsignado);
		parametros.put("consultorio", consultorio);
		parametros.put("profesionalAsignado", profesionalAsignado);
		parametros.put("profesional", codigoMedico);
		parametros.put("dias", diasSeleccionados);
		parametros.put("usuario", usuario);
		
		ArrayList<HashMap<String, Object>> horarioAtencionList = null;
		HorarioAtencion horarioAtencion = null;
		HashMap<String,Object> horarioAtencionMap = null;
		
		ArrayList<HashMap<String, Object>> codigosHA = ihad_dao.consultarHAAvanzada(con, parametros);
		
		if(codigosHA != null && !codigosHA.isEmpty()){
			
			horarioAtencionList = new ArrayList<HashMap<String,Object>>();
			
			for(int i=0; i<codigosHA.size() ; i++){
				try {
					horarioAtencionMap = codigosHA.get(i); 
					
					horarioAtencion = new HorarioAtencion();
					horarioAtencion.cargarHorarioAtencion(con, ( Utilidades.convertirAEntero(horarioAtencionMap.get("codigo") +" ")), codigoInstitucion);
					
					horarioAtencionMap.put("unidadConsulta", horarioAtencion.getUnidadConsulta());
					horarioAtencionMap.put("consultorio", horarioAtencion.getConsultorio());
					horarioAtencionMap.put("diaSemana", horarioAtencion.getDiaSemana());
					horarioAtencionMap.put("horaInicio", horarioAtencion.getHoraInicio());
					horarioAtencionMap.put("medico", horarioAtencion.getMedico());
					horarioAtencionMap.put("horaFin", horarioAtencion.getHoraFin());
					horarioAtencionMap.put("centroAtencion", horarioAtencion.getNombreCentroAtencion());
					
					horarioAtencionList.add(horarioAtencionMap);
					
				} catch (Exception e) {
					logger.info("listarHorariosAtencion : ",e);
				}
			}
			
		}

		return (Collection) horarioAtencionList;
	}
	
	
	/**
	* Lista todos los horarios de atenciï¿½n
	* @param ac_con		Conexiï¿½n abierta con una fuente de datos
	* @param centroAtencion
	 * @param institucion 
	* @return <code>Collection</code> de los horarios de atenciï¿½n
	*/
	public static Collection listarHorariosAtencion(Connection ac_con, int centroAtencion, String unidadesAgenda, int institucion)throws Exception
	{
		/* Obtener la lista de cï¿½digos de horarios de atenciï¿½n */
		Collection	lc_c;
		Vector		lv_resp;

		lc_c =
			DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")
			).getHorarioAtencionDao().listarCodigosHorarioAtencion(ac_con, centroAtencion, unidadesAgenda, institucion);

		lv_resp = null;

		/* Verificar la existencia del conjunto soluciï¿½n */
		if(lc_c != null && lc_c.size() > 0)
		{
			HashMap	ldb_db;
			Vector		lv_datos;

			lv_datos	= new Vector(lc_c);
			lv_resp		= new Vector();

			/* Iterar sobre el conjunto soluciï¿½n */
			for(int li_i = 0, li_tam = lv_datos.size(); li_i < li_tam; li_i++)
			{
				HorarioAtencion lha_ha;

				/* Obtener un nuevo horario de atenciï¿½n a partir del cï¿½digo actual */
				lha_ha	= new HorarioAtencion();
				ldb_db	= (HashMap)lv_datos.get(li_i);

				lha_ha.cargarHorarioAtencion(ac_con, ( Utilidades.convertirAEntero(ldb_db.get("codigo") +" ")), institucion);
				ldb_db.put("unidadConsulta", lha_ha.getUnidadConsulta());
				ldb_db.put("consultorio", lha_ha.getConsultorio());
				ldb_db.put("diaSemana", lha_ha.getDiaSemana());
				ldb_db.put("horaInicio", lha_ha.getHoraInicio());
				ldb_db.put("medico", lha_ha.getMedico());
				ldb_db.put("horaFin", lha_ha.getHoraFin());
				ldb_db.put("centroAtencion", lha_ha.getNombreCentroAtencion());

				/* Adicionar el horario de atenciï¿½n a la lista de horarios de atenciï¿½n */
				lv_resp.add(ldb_db);
			}
		}

		return (Collection)lv_resp;
	}
	

	
	public static ArrayList<DtoHorarioAtencion> consultarHA(int centroAtencion, int unidadAgenda, String consultorioAsignado, int consultorio, String profesionalAsignado, int codigoMedico,ArrayList<Integer> diasSeleccionados, int tipoReporte) 
	{
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("centroAtencion", centroAtencion);
		parametros.put("unidadAgenda", unidadAgenda);
		parametros.put("consultorioAsignado", consultorioAsignado);
		parametros.put("consultorio", consultorio);
		parametros.put("profesionalAsignado", profesionalAsignado);
		parametros.put("profesional", codigoMedico);
		parametros.put("dias", diasSeleccionados);
		parametros.put("tipoReporte", tipoReporte);
		
		return DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")
		).getHorarioAtencionDao().consultarHA(parametros);
	}
	
	
	/**
	* Establece el cï¿½digo del horario de atenciï¿½n
	* @param ai_codigo Identificador ï¿½nico de este horario de atenciï¿½n
	*/
	public void setCodigo(int ai_codigo)
	{
		ii_codigo = ai_codigo;
	}

	/**
	* Establece el cï¿½digo del consultorio del horario de atenciï¿½n
	* @param ai_consultorio Cï¿½digo del consultorio a asignar a este horario de atenciï¿½n
	*/
	public void setCodigoConsultorio(int ai_consultorio)
	{
		if(ai_consultorio > 0)
			ii_consultorio = ai_consultorio;
	}

	/**
	* Establece el cï¿½digo del dï¿½a de la semana del horario de atenciï¿½n
	* @param ai_diaSemana Cï¿½digo del dï¿½a de la semana a asignar a este horario de atenciï¿½n
	*/
	public void setCodigoDiaSemana(int ai_diaSemana)
	{
		if(ai_diaSemana > 0)
			ii_diaSemana = ai_diaSemana;
	}

	/**
	* Establece el cï¿½digo del mï¿½dico del horario de atencio
	* @param as_codigoMedico Cï¿½digo del mï¿½dico a asignar a este horario de atenciï¿½n
	*/
	public void setCodigoMedico(int ai_codigoMedico)
	{
		if(ai_codigoMedico > 0)
			ii_medico = ai_codigoMedico;
	}
	

	/**
	* Establece el tiempo de duraciï¿½n (en minutos) de una consulta en este horario de atenciï¿½n
	* @param ai_duracionConsulta Tiempo en minutos de una consulta o sesiï¿½n de este horario de
	* atenciï¿½n
	*/
	public void setDuracionConsulta(int ai_duracionConsulta)
	{
		if(ai_duracionConsulta > 0)
			ii_duracionConsulta = ai_duracionConsulta;
	}

	/**
	* Establece la hora de finalizaciï¿½n del horario de atenciï¿½n para el dï¿½a de la semana
	* @param as_horaFin Hora de finalizaciï¿½n de este horario de atenciï¿½n
	*/
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null && validarHora(as_horaFin = as_horaFin.trim(), false) )
			is_horaFin = as_horaFin;
	}

	/**
	* Establece la hora de inicio del horario de atenciï¿½n para el dï¿½a de la semana
	* @param as_horaInicio Hora de inicio de este horario de atenciï¿½n
	*/
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null && validarHora(as_horaInicio = as_horaInicio.trim(), true) )
			is_horaInicio = as_horaInicio;
	}

	/**
	* Establece el nï¿½mero mï¿½ximo de pacientes que se pueden atender por sesiï¿½n de consulta
	* @param ai_pacientesSesion Nï¿½mero mï¿½ximo de pacientes que se pueden asignar a una
	* consulta en este horario de atenciï¿½n
	*/
	public void setPacientesSesion(int ai_pacientesSesion)
	{
		if(ai_pacientesSesion > 0)
			ii_pacientesSesion = ai_pacientesSesion;
	}

	/**
	* Valida si la hora a asignar, ya sea hora de inicio o finalizaciï¿½n es una hora vï¿½lida
	* @param as_hora			Hora a validar
	* @param ab_esHoraInicio	Indica si la hora a validar es una hora de inicio o de finalizaciï¿½n
	* @return true si la hora es vï¿½lida, false de lo contrario
	*/
	private boolean validarHora(String as_hora, boolean ab_esHoraInicio)
	{
		boolean				lb_resp;
		Date				ld_d;
		SimpleDateFormat	lsdf_sdf;

		/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
		lsdf_sdf = new SimpleDateFormat("H:mm");

		/* Exije una interpretaciï¿½n estricta del formato de hora esperado */
		lsdf_sdf.setLenient(false);

		try
		{
			/* Interpretar la hora a validar */
			ld_d = lsdf_sdf.parse(as_hora);

			/* Validar la hora */
			if(ab_esHoraInicio)
			{
				/* La hora de inicio debe ser menor que la hora de finalizaciï¿½n */
				if(is_horaFin.equals("") )
					lb_resp = true;
				else
					lb_resp = ld_d.before(lsdf_sdf.parse(is_horaFin) );
			}
			else
			{
				/* La hora de finalizaciï¿½n debe ser menor que la hora de inicio */
				if(is_horaInicio.equals("") )
					lb_resp = true;
				else
					lb_resp = lsdf_sdf.parse(is_horaInicio).before(ld_d);
			}
		}
		catch(ParseException lpe_e)
		{
			lb_resp = false;
		}

		return lb_resp;
	}

	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return Retorna the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion()
	{
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion)
	{
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the ii_unidadAgenda
	 */
	public int getUnidadAgenda() {
		return ii_unidadAgenda;
	}

	/**
	 * @param ii_unidadAgenda the ii_unidadAgenda to set
	 */
	public void setUnidadAgenda(int ii_unidadAgenda) {
		this.ii_unidadAgenda = ii_unidadAgenda;
	}
	
	/**
	* consultar la Especialidad Unidad de Agenda
	* @param con Conexiï¿½n abierta con una fuente de datos
	* @param HashMap parametros
	* @return HashMap<String,Object>
	*/
	public HashMap<String,Object> getEspecialidad(Connection con, String codigoUniAgen)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigo", codigoUniAgen);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHorarioAtencionDao().getEspecialidad(con, parametros);
	}

	/**
	 * @return the profesionaleSaludUniAgen
	 */
	public ArrayList<HashMap<String, Object>> getProfesionaleSaludUniAgen() {
		return profesionaleSaludUniAgen;
	}

	/**
	 * @param profesionaleSaludUniAgen the profesionaleSaludUniAgen to set
	 */
	public void setProfesionaleSaludUniAgen(
			ArrayList<HashMap<String, Object>> profesionaleSaludUniAgen) {
		this.profesionaleSaludUniAgen = profesionaleSaludUniAgen;
	}

	/**
	 * @return the indexCodUniAgen
	 */
	public String getIndexCodUniAgen() {
		return indexCodUniAgen;
	}

	/**
	 * @param indexCodUniAgen the indexCodUniAgen to set
	 */
	public void setIndexCodUniAgen(String indexCodUniAgen) {
		this.indexCodUniAgen = indexCodUniAgen;
	}

	/**
	 * @return the datosUnidadAgenda
	 */
	public HashMap<String, Object> getDatosUnidadAgenda() {
		return datosUnidadAgenda;
	}

	/**
	 * @param datosUnidadAgenda the datosUnidadAgenda to set
	 */
	public void setDatosUnidadAgenda(HashMap<String, Object> datosUnidadAgenda) {
		this.datosUnidadAgenda = datosUnidadAgenda;
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
	
	public static String generacionXMLparametros(HorarioAtencionForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}

		if(forma.getListaHorarios().size() > 0)
		{
			xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"Centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"cod_centro_atencion"+etiquetaCerrar+forma.getListaHorarios().get(0).getCentroAtencion()+etiquetaAbrir+"/cod_centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"nom_centro_atencion"+etiquetaCerrar+forma.getListaHorarios().get(0).getNomCentroAtencion()+etiquetaAbrir+"/nom_centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"Hora"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"hora_ini_horarios"+etiquetaCerrar+Integer.valueOf(forma.getHoraIniHorarios())+etiquetaAbrir+"/hora_ini_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"min_ini_horarios"+etiquetaCerrar+Integer.valueOf(forma.getMinIniHorarios())+etiquetaAbrir+"/min_ini_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"hora_fin_horarios"+etiquetaCerrar+Integer.valueOf(forma.getHoraFinHorarios())+etiquetaAbrir+"/hora_fin_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"min_fin_horarios"+etiquetaCerrar+Integer.valueOf(forma.getMinFinHorarios())+etiquetaAbrir+"/min_fin_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"multiplo"+etiquetaCerrar+Integer.valueOf(forma.getMultiploMinGenCita())+etiquetaAbrir+"/multiplo"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Hora"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);
		}

		
		return xml.toString();
	}
	
	/**
	 * metodo que retorna un string xml de horarios de atencion
	 * @param forma
	 * @return
	 */
	public static String generacionXMLHorarios(HorarioAtencionForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
		String[] horaInicio = {""};
		String[] horaFinal = {""};
		String horaFinMax="0",mintFinMax="0", horaIni="0", minIni="0", color="";
		int i = 0; 
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}
		
		xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);
		for(DtoHorarioAtencion elem: forma.getListaHorarios())
		{
				horaInicio = elem.getHoraInicio().split(":");
				horaFinal = elem.getHoraFin().split(":");
				try{color=elem.getColorUniAgenda().substring(1,elem.getColorUniAgenda().length());}catch (Exception e) {color="";}
				
				xml.append(etiquetaAbrir+"Horario_atencion"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"codigo_horario"+etiquetaCerrar+elem.getCodigo()+etiquetaAbrir+"/codigo_horario"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"hora_inicio"+etiquetaCerrar+Integer.valueOf(horaInicio[0])+etiquetaAbrir+"/hora_inicio"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"min_inicio"+etiquetaCerrar+Integer.valueOf(horaInicio[1])+etiquetaAbrir+"/min_inicio"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"hora_fin"+etiquetaCerrar+Integer.valueOf(horaFinal[0])+etiquetaAbrir+"/hora_fin"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"min_fin"+etiquetaCerrar+Integer.valueOf(horaFinal[1])+etiquetaAbrir+"/min_fin"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"convencion_profesional"+etiquetaCerrar+elem.getConvencion()+etiquetaAbrir+"/convencion_profesional"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"color_uni_consulta"+etiquetaCerrar+color+etiquetaAbrir+"/color_uni_consulta"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"Consulorio"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"cod_consultorio"+etiquetaCerrar+elem.getConsultorio()+etiquetaAbrir+"/nombre_consultorio"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"nombre_consultorio"+etiquetaCerrar+elem.getNombreConsultorio()+etiquetaAbrir+"/nombre_consultorio"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"/Consultorio"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"Dia"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"cod_dia"+etiquetaCerrar+elem.getDia()+etiquetaAbrir+"/nom_dia"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"nom_dia"+etiquetaCerrar+elem.getNombreDia()+etiquetaAbrir+"/nom_dia"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"/Dia"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"/Horario_atencion"+etiquetaCerrar);
				if(Utilidades.convertirAEntero(horaIni) == 0){
					horaIni= horaInicio[0];
					minIni= horaInicio[1];
				}					
				if(Utilidades.convertirAEntero(horaFinal[0])> Utilidades.convertirAEntero(horaFinMax)){
					horaFinMax = horaFinal[0];
					mintFinMax = horaFinal[1];
				}else{
					if(Utilidades.convertirAEntero(horaFinal[0]) == Utilidades.convertirAEntero(horaFinMax))
						if(Utilidades.convertirAEntero(horaFinal[1]) > Utilidades.convertirAEntero(mintFinMax))
							mintFinMax = horaFinal[1];
				}
			i++;
		}
		xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);
	
		forma.setHoraIniHorarios(horaIni);
		forma.setMinIniHorarios(minIni);
		forma.setHoraFinHorarios(horaFinMax);
		forma.setMinFinHorarios(mintFinMax);
		
		
		return xml.toString();
	}
	
	/**
	 * metodo que retorna un string xml de unidades agenda
	 * @param forma
	 * @return
	 */
	public static String generacionXMLUnidadesAgenda(HorarioAtencionForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
		String color="";
		int i = 0; 
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}
		
		HashMap unidades= new HashMap();
		int m=0;
		int k=0;
		for(DtoHorarioAtencion elem: forma.getListaHorarios())
		{
			unidades.put("numRegistros", k);
			if(m!=0)
			{
				boolean insertar=true;
				int n=Utilidades.convertirAEntero(unidades.get("numRegistros")+"");
				for(int j=0;j<n;j++)
				{
					if((Utilidades.convertirAEntero(unidades.get("codigo_"+j)+"")) == elem.getUnidadConsulta())
						insertar=false;
				}
				if(insertar)
				{
					unidades.put("codigo_"+k, elem.getUnidadConsulta());
					unidades.put("descripcion_"+k, elem.getNombreUniAgenda());
					unidades.put("color_"+k, color);
					k++;
				}
			}
			else{
				unidades.put("codigo_"+k, elem.getUnidadConsulta());
				unidades.put("descripcion_"+k, elem.getNombreUniAgenda());
				unidades.put("color_"+k, color);
				k++;
			}
			m++;
		}
		
		unidades.put("numRegistros", k);
		
		m=Utilidades.convertirAEntero(unidades.get("numRegistros")+"");
		
		xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);
		for(int j=0;j<m;j++)
		{
				try{color=(unidades.get("color_"+j)+"").substring(1,(unidades.get("color_"+j)+"").length());}catch (Exception e) {color="";}
				
				xml.append(etiquetaAbrir+"Unidad_consulta"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"cod_unidad_consulta"+etiquetaCerrar+unidades.get("codigo_"+j)+etiquetaAbrir+"/cod_unidad_consulta"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"nom_unidad_consulta"+etiquetaCerrar+unidades.get("descripcion_"+j)+etiquetaAbrir+"/nom_unidad_consulta"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"color"+etiquetaCerrar+color+etiquetaAbrir+"/color"+etiquetaCerrar);
				xml.append(etiquetaAbrir+"/Unidad_consulta"+etiquetaCerrar);
			i++;
		}
		xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);

		
		return xml.toString();
	}
	
	/**
	 * metodo que retorna un string xml de profesionales
	 * @param forma
	 * @return
	 */
	public static String generacionXMLProfesionales(HorarioAtencionForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
		int i = 0; 
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}
		
		HashMap profesionales= new HashMap();
		int m=0;
		int k=0;
		for(DtoHorarioAtencion elem: forma.getListaHorarios())
		{
			profesionales.put("numRegistros", k);
			if(m!=0)
			{
				boolean insertar=true;
				int n=Utilidades.convertirAEntero(profesionales.get("numRegistros")+"");
				for(int j=0;j<n;j++)
				{
					if((Utilidades.convertirAEntero(profesionales.get("codigo_"+j)+"")) == elem.getCodigoMedico())
						insertar=false;
				}
				if(insertar)
				{
					profesionales.put("codigo_"+k, elem.getCodigoMedico());
					profesionales.put("convencion_"+k, elem.getConvencion());
					profesionales.put("nombre_"+k, elem.getNombreMedico());
					k++;
				}
			}
			else{
				profesionales.put("codigo_"+k, elem.getCodigoMedico());
				profesionales.put("convencion_"+k, elem.getConvencion());
				profesionales.put("nombre_"+k, elem.getNombreMedico());
				k++;
			}
			m++;
		}
	
		m= Utilidades.convertirAEntero(profesionales.get("numRegistros")+"");
	
		
		xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);
		for(int j=0;j<m;j++)
		{
			xml.append(etiquetaAbrir+"Medico"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"cod_medico"+etiquetaCerrar+profesionales.get("codigo_"+j)+etiquetaAbrir+"/cod_medico"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"convencion"+etiquetaCerrar+profesionales.get("convencion_"+j)+etiquetaAbrir+"/convencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"nom_medico"+etiquetaCerrar+profesionales.get("nombre_"+j)+etiquetaAbrir+"/nom_medico"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Medico"+etiquetaCerrar);
			i++;
		}
		xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);
		
		
		return xml.toString();
	}
}