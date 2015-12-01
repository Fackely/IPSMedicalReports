/*
 * @(#)Citas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004 Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.agenda;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.TipoNumeroId;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ListadoCitasDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para el manejo de conjuntos de citas 
 *
 * @version 1.0, Marzo 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class Citas
{
	/**
	 * Conjunto de citas
	 */
	private ArrayList citas;
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Citas.class);	
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private ListadoCitasDao listadoCitasDao = null; 
	/**
	 * Creadora de la clase
	 * @see java.lang.Object#Object()
	 */
	public Citas()
	{
		this.citas = new ArrayList();
		this.init(System.getProperty("TIPOBD"));
	}
		
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public void init(String tipoBD)
	{
		if( listadoCitasDao == null)
		{
			if(tipoBD==null)
			{
				logger.error("No esta llegando el tipo de base de datos en clase Citas");
				System.exit(1);
			}
			else
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				if (myFactory != null)
					listadoCitasDao = myFactory.getListadoCitasDao();
			}
		}		
	}	

	/**
	 * Adiciona una cita a la lista
	 * @param cita
	 */
	public void addCita(Cita cita)
	{
		this.citas.add(cita);
	}
	
	/**
	 * Retorna la cita dado el indice, si el indice es inválido retorna null
	 * @param indice
	 * @return
	 */
	public Cita getCita(int indice)
	{
		if( indice <0 || indice >= citas.size() )
			return null;
		else
			return (Cita)citas.get(indice);
	}
	
	/**
	 * Retorna el número de citas existentes
	 * @return
	 */
	public int getNumCitas()
	{
		if( citas != null )
			return this.citas.size();
		else
			return 0;
	}
	
	/**
	 * Inicializa el listado de citas
	 *
	 */
	public void resetCitas()
	{
		this.citas = new ArrayList();
	}
	
	/**
	 * Retorna el arreglo con las citas
	 * @return
	 */
	public ArrayList getCitas() 
	{
		return citas;
	}

	/**
	 * Asigna el arreglo con las citas
	 * @param list
	 */
	public void setCitas(ArrayList list) 
	{
		citas = list;
	}
	
	/**
	 * 
	 * @param con
	 * @param codMedico
	 * @param fecha
	 * @param centroAtencion
	 * @return
	 */
	public ResultadoBoolean cargarCitasPorMedico(	Connection con,UsuarioBasico medico, String fecha)
	{
		HashMap campos = new HashMap();
		campos.put("fecha", fecha);
		campos.put("institucion", medico.getCodigoInstitucion());
		campos.put("codigoMedico", medico.getCodigoPersona());
		campos.put("codigoCentroCosto", medico.getCodigoCentroCosto());
		campos.put("codigoCentroAtencion", medico.getCodigoCentroAtencion());
		
		//Se toman las especialidades del médico
		String especialidades = "";
		for(int i=0;i<medico.getEspecialidades().length;i++)
		{
			if(medico.getEspecialidades()[i].isActivo())
			{
				if(!especialidades.equals(""))
					especialidades += ",";
				especialidades += medico.getEspecialidades()[i].getCodigo();
			}
		}
		if(especialidades.equals(""))
			especialidades = ConstantesBD.codigoEspecialidadMedicaNinguna + "";
		campos.put("especialidades", especialidades);
		
		
		HashMap resultados = this.listadoCitasDao.listarCitasPorAtenderMedico(con, campos);
		
		
		int codigoCitaAnterior = 0;
		Cita tempCita = new Cita();
		int contador = 0;
		
		if(resultados.get("numRegistros")!=null && Integer.valueOf(String.valueOf(resultados.get("numRegistros")))!=null){
			for(int i=0;i<Integer.parseInt(resultados.get("numRegistros")+"");i++)
			{
				if(codigoCitaAnterior!=Integer.parseInt(resultados.get("codigoCita_"+i)+""))
				{
					if(codigoCitaAnterior!=0)
					{
						tempCita.setMapaServicios("numRegistros", contador+"");
						logger.info("SE AÑADIÓ LA CITA => "+tempCita.getCodigo());
						this.addCita(tempCita);

					}

					tempCita = new Cita();

					contador = 0;
					codigoCitaAnterior = Integer.parseInt(resultados.get("codigoCita_"+i)+"");

					logger.info("paso por aqui codgio de la cita=> "+codigoCitaAnterior+" en posicion=> "+i);

					tempCita.setCodigo(codigoCitaAnterior);
					tempCita.setCodigoEstadoLiquidacion(resultados.get("codigoEstadoLiquidacion_"+i)+"");
					tempCita.setUnidadConsulta(new InfoDatosInt(Integer.parseInt(resultados.get("codigoUnidadAgenda_"+i)+""), resultados.get("nombreUnidadAgenda_"+i)+""));
					tempCita.setHoraInicio(resultados.get("horaInicioCita_"+i)+"");
					tempCita.setHoraFin(resultados.get("horaFinCita_"+i)+"");
					tempCita.setIdentificacionPaciente(new TipoNumeroId(resultados.get("tipoIdentificacionPaciente_"+i)+"", resultados.get("numeroIdentificacionPaciente_"+i)+""));
					tempCita.setNombreCompletoPaciente(resultados.get("nombreCompletoPaciente_"+i)+"");
					tempCita.setEstadoCita(new InfoDatosInt(Integer.parseInt(resultados.get("codigoEstadoCita_"+i)+""), resultados.get("nombreEstadoCita_"+i)+""));
					tempCita.setCodigoPaciente(Integer.parseInt(resultados.get("codigoPaciente_"+i)+""));
					tempCita.setSexoPaciente((Integer.parseInt(resultados.get("sexo_"+i)+"")));
					tempCita.setCuenta(Integer.parseInt(resultados.get("cuenta_"+i)+""));
					tempCita.setPerteneceAlMedico(UtilidadTexto.getBoolean(resultados.get("perteneceMedico_"+i)+""));
					if(resultados.get("codigomedico_"+i) != null && !resultados.get("codigomedico_"+i).toString().trim().isEmpty()){
						tempCita.setCodigoMedico(new Integer(resultados.get("codigomedico_"+i).toString()));
					}
					else{
						tempCita.setCodigoMedico(ConstantesBD.codigoNuncaValido);
					}
					tempCita.setSolPYP(UtilidadTexto.getBoolean(resultados.get("pyp_"+i)+""));
					tempCita.setCodigoAgenda(Integer.parseInt(resultados.get("codigoAgenda_"+i)+""));
					tempCita.setPrioridad(resultados.get("prioritaria_"+i)+"");
					tempCita.setMostrarCita(resultados.get("mostrarcita_"+i)+"");

				}

				//Se llena la informacion del servicio
				tempCita.setMapaServicios("numeroSolicitud_"+contador, resultados.get("numeroSolicitud_"+i));
				tempCita.setMapaServicios("observaciones_"+contador, resultados.get("observaciones_"+i));
				tempCita.setMapaServicios("codigoServicio_"+contador, resultados.get("codigoServicio_"+i));
				tempCita.setMapaServicios("codigoCentroCosto_"+contador, resultados.get("codigoCentroCosto_"+i));
				tempCita.setMapaServicios("nombreServicio_"+contador, resultados.get("nombreServicio_"+i));
				tempCita.setMapaServicios("estadoServicio_"+contador, resultados.get("estadoServicio_"+i));
				tempCita.setMapaServicios("codigoTipoServicio_"+contador, resultados.get("codigoTipoServicio_"+i));
				tempCita.setMapaServicios("tieneCondiciones_"+contador, resultados.get("tieneCondiciones_"+i));
				tempCita.setMapaServicios("codigoEspecialidad_"+contador, resultados.get("codigoEspecialidad_"+i));
				tempCita.setMapaServicios("mostrarsercita_"+contador, resultados.get("mostrarcita_"+i)+"");
				contador++;
			}
		}
		
		if(resultados.get("numRegistros")!=null &&
				Integer.valueOf(String.valueOf(resultados.get("numRegistros")))!=null	){
			if(Integer.parseInt(resultados.get("numRegistros")+"")>0)
			{
				tempCita.setMapaServicios("numRegistros",contador+"");
				this.addCita(tempCita);
			}
		}
		
		
		return new ResultadoBoolean(true);
	}	
	
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param codMedico
	 * @param fechaInicio
	 * @param fechaFin
	 * @param horaInicio
	 * @param horaFin
	 * @param unidadConsulta
	 * @param estadoLiquidacion
	 * @param consultorio
	 * @param estadosCita
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public ResultadoBoolean cargarCitas(	Connection con,int codPaciente,int codMedico,String fechaInicio, String fechaFin,String horaInicio,String horaFin,int unidadConsulta,String estadoLiquidacion,int consultorio,String [] estadosCita, String centroAtencion,String postO,String tipoOrdenamiento) throws SQLException{
		
		ResultadoCollectionDB resultado = this.listadoCitasDao.listarCitasPorPaciente(con,codPaciente,codMedico,fechaInicio,fechaFin,horaInicio,horaFin,unidadConsulta,estadoLiquidacion,consultorio,estadosCita,centroAtencion,postO,tipoOrdenamiento);
		
		ArrayList listadoCitas = (ArrayList)resultado.getFilasRespuesta();
		int tam = 0;			
		if( listadoCitas != null && (tam = listadoCitas.size()) > 0 )
		{		
			for( int i=0; i < tam; i++ )
			{
				HashMap citaDB = (HashMap)(listadoCitas).get(i);
				Cita tempCita = new Cita();
				
				tempCita.setHoraInicio(citaDB.get("horainicio")+"");
				String nombreMedico=(String)citaDB.get("nombrecompletomedico");
				if(nombreMedico==null)
				{
					nombreMedico="";
				}
				tempCita.setNombreCompletoMedico(nombreMedico);
				tempCita.setUnidadConsulta(new InfoDatosInt((Utilidades.convertirAEntero(citaDB.get("codigounidadconsulta")+"")), (String)citaDB.get("nombreunidadconsulta")));

				tempCita.setNombreEstadoLiquidacion(citaDB.get("nombreestadoliquidacion")+"");
				if(citaDB.get("cuenta")!=null){
					tempCita.setNumeroCuenta((Utilidades.convertirAEntero(citaDB.get("cuenta")+"")));
				}else{
					tempCita.setNumeroCuenta(-1);
				}				
				
				tempCita.setNombreCompletoPaciente(citaDB.get("nombrecompletopersona")+"");
				tempCita.setFecha(citaDB.get("fechainicio")+"");
				tempCita.setCodigo((Utilidades.convertirAEntero(citaDB.get("codigocita")+"")));
				tempCita.setEstadoCita((new InfoDatosInt((Utilidades.convertirAEntero(citaDB.get("codigoestadocita")+"")), (String)citaDB.get("nombreestadocita"))));
				tempCita.setNombreConsultorio(citaDB.get("nombreconsultorio")+"");
				tempCita.setCodigoPaciente((Utilidades.convertirAEntero(citaDB.get("codigopaciente")+"")));
				
				tempCita.setCodigoCentroAtencion(Utilidades.convertirAEntero(citaDB.get("codigocentroatencion")+""));
				tempCita.setNombreCentroAtencion(citaDB.get("nombrecentroatencion")+"");
				Integer codigoMedicoInt=Utilidades.convertirAEntero(citaDB.get("codigomedico")+"");
				if(codigoMedicoInt==null)
				{
					codigoMedicoInt=0;
				}
				tempCita.setCodigoMedico(codigoMedicoInt);
				tempCita.setIdentificacionPaciente(new TipoNumeroId(citaDB.get("tipoid")+"",citaDB.get("numeroid")+""));				
				
				
				//-datos de la cuenta 
				tempCita.setNombreConvenio(citaDB.get("convenio")+"");
				tempCita.setNombreContrato(citaDB.get("contrato")+"");
				tempCita.setNombreClasificacionSocial(citaDB.get("clasificacion_social")+"");
				tempCita.setNombreTipoAfiliado(citaDB.get("tipo_afiliado")+"");
				tempCita.setCodigoEstadoCita( Integer.parseInt(citaDB.get("codigoestadocita")+"") );
				tempCita.setIndiPO(citaDB.get("indpo")+"");
								
				tempCita.setMotivoNoAtencion(citaDB.get("motivo_noatencion")+"");				
				tempCita.setObservaciones(citaDB.get("observacionescancelacion")+"");
				tempCita.setNumeroHistoriaClinica(citaDB.get("numero_historia_clinica")+"");
				
				tempCita.setCodigoConsultorioUsua(citaDB.get("codigo_consultorio_usua")+"");
				tempCita.setRegistroMedico(citaDB.get("registro_medico")+"");
				tempCita.setLoginUsuCita(citaDB.get("login_usu_cita")+"");
				//logger.info("valor del telefono "+citaDB.get("telefono")+"");
				tempCita.setTelefono(citaDB.get("telefono")+"");
				tempCita.setFechaModifica(citaDB.get("fecha_modifica")+"");
				tempCita.setHoraModifica(citaDB.get("hora_modifica")+"");
				tempCita.setNombreUsuario(citaDB.get("usu_cita")+"");
				
				 /**
			     * Inc. 506
			     * @author Diana Ruiz
			     * @since 05/08/2011
			     */
			    
				
				String telefono="";
				
				if(citaDB.get("telefono_fijo")!= null){
					telefono= (citaDB.get("telefono_fijo").toString());
					if(citaDB.get("telefono_celular")!= null){
						telefono=(citaDB.get("telefono_fijo")).toString()+"-"+citaDB.get("telefono_celular");
						if(citaDB.get("telefono")!= null){
							telefono=(citaDB.get("telefono_fijo")).toString()+"-"+citaDB.get("telefono_celular")+"-"+citaDB.get("telefono");						
						}
					}else{
						if(citaDB.get("telefono")!= null)
							telefono=(citaDB.get("telefono_fijo")).toString()+"-"+citaDB.get("telefono");						
					}
				}else { 
					if(citaDB.get("telefono_celular")!= null){
						telefono= (citaDB.get("telefono_celular")).toString();
						if(citaDB.get("telefono")!= null){
							telefono=(citaDB.get("telefono_celular")).toString()+"-"+citaDB.get("telefono");
						}
						
					}else 
						if(citaDB.get("telefono")!= null)
							telefono=(String) citaDB.get("telefono");
				}
				tempCita.setTelefono(telefono);
				//tempCita.setTelefono(citaDB.get("telefono")+"");
								
				this.addCita(tempCita);
			}
			
		}
		return new ResultadoBoolean(true);
			
	}
	

	
	/**
	 * Consulta los servicios asociados a la cita 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap serviciosCita(Connection con, HashMap parametros)
	{
		return this.listadoCitasDao.serviciosCita(con, parametros);		
	}	

	
	public ResultadoBoolean cargarCitas(Connection con, int codPaciente, int idCuenta) throws SQLException{
		
		ResultadoCollectionDB resultado = this.listadoCitasDao.listarCitas(con, codPaciente, idCuenta);
		LinkedList listadoCitas = (LinkedList)resultado.getFilasRespuesta();
		int tam = 0;			
		if( listadoCitas != null && (tam = listadoCitas.size()) > 0 )
		{		
			for( int i=0; i < tam; i++ )
			{
				HashMap citaDB = (HashMap)(listadoCitas).get(i);
				Cita tempCita = new Cita();
				tempCita.setConsecutivoCita(((Integer)citaDB.get("consecutivoordenesmedicas")).intValue());
				tempCita.setHoraInicio(citaDB.get("horainicio")+"");
				String nombreMedico=(String)citaDB.get("nombrecompletomedico");
				if(nombreMedico==null)
				{
					nombreMedico="";
				}
				tempCita.setNombreCompletoMedico(nombreMedico);
				tempCita.setUnidadConsulta(new InfoDatosInt(((Integer)citaDB.get("codigounidadconsulta")).intValue(), (String)citaDB.get("nombreunidadconsulta")));

				tempCita.setNombreCentroAtencion(citaDB.get("nombrecentroatencion")+"");				
				tempCita.setCodigoCentroAtencion(Utilidades.convertirAEntero(citaDB.get("codigocentroatencion")+""));
				tempCita.setNombreEstadoLiquidacion(citaDB.get("nombreestadoliquidacion")+"");
				if(citaDB.get("cuenta")!=null){
					tempCita.setNumeroCuenta(((Integer)citaDB.get("cuenta")).intValue());
				}else{
					tempCita.setNumeroCuenta(-1);
				}
				if(citaDB.get("numerosolicitud")!=null){
					tempCita.setNumeroSolicitud(((Integer)citaDB.get("numerosolicitud")).intValue());
				}else{
					tempCita.setNumeroSolicitud(-1);
				}
				
				tempCita.setNombreCompletoPaciente(citaDB.get("nombrecompletopersona")+"");
				tempCita.setFecha(citaDB.get("fechainicio")+"");
				tempCita.setCodigo(((Integer)citaDB.get("codigocita")).intValue());
				tempCita.setEstadoCita((new InfoDatosInt(((Integer)citaDB.get("codigoestadocita")).intValue(), (String)citaDB.get("nombreestadocita"))));
				tempCita.setNombreConsultorio(citaDB.get("nombreconsultorio")+"");
				tempCita.setCodigoPaciente(((Integer)citaDB.get("codigopaciente")).intValue());
				Integer codigoMedicoInt=(Integer)citaDB.get("codigomedico");
				if(codigoMedicoInt==null)
				{
					codigoMedicoInt=0;
				}
				tempCita.setCodigoMedico(codigoMedicoInt);
				tempCita.setIdentificacionPaciente(new TipoNumeroId(citaDB.get("tipoid")+"",citaDB.get("numeroid")+""));
				
				tempCita.setMotivoNoAtencion(citaDB.get("motivo_noatencion")+"");				
				tempCita.setObservaciones(citaDB.get("observacionescancelacion")+"");
				tempCita.setNumeroHistoriaClinica(citaDB.get("numero_historia_clinica")+"");
				tempCita.setTelefono(citaDB.get("telefono")+"");
				
				this.addCita(tempCita);	
			}
			
		}
		return new ResultadoBoolean(true);			
	}	
	
	/**
	 * Método implementado para obtener le fecha de la primera cita del paciente
	 * en estado asignada o reservada
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String obtenerFechaPrimeraCitaPaciente(Connection con,int codigoPaciente)
	{
		return listadoCitasDao.obtenerFechaPrimeraCitaPaciente(con, codigoPaciente);
	}
}