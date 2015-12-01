package com.princetonsa.mundo.ordenesmedicas.solicitudes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ProcedimientoDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.solicitudes.DocumentosAdjuntos;
import com.princetonsa.mundo.solicitudes.Solicitud;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Clase para el manejo de un procedimiento
 *
 * @version 1.0, Febrero 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:juandavid@PrincetonSA.com">Juan David Ramirez</a>
 */
public class Procedimiento
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Procedimiento.class);	

	/**
	 * Codigo pk 
	 * */
	private int codigoPkResSolProc;
	
	
	/**
	 * N�mero de solicitud del procedimiento
	 */
	private int numeroSolicitud;

	
	private int centroCostoSolicitado;

	/**
	 * Fecha en que se ejecut� el procedimiento
	 */
	private String fechaEjecucion;
	
	/**
	 * Fecha en la que se grab� la respuesta
	 */
	private String fechaGrabacionRespuesta;

	/**
	 * Hora en la que se grab� la respuesta
	 */
	private String horaGrabacionRespuesta;

	/**
	 * Modificar el n�mero de autorizacion
	 */
	private String numeroAutorizacion;

	/**
	 * Resultados del procedimiento
	 */
	private String resultados;
	
	/**
	 * Observaciones del procedimiento
	 */
	private String observaciones;
	
	/**
	 * Tipo de recargo del procedimiento
	 */
	private InfoDatosInt tipoRecargo;
	
	/**
	 * Comentario adicional de la historia cl�nica
	 */
	private String comentarioHistoriaClinica;
	
	/**
	 * Documentos adjuntos
	 */
	private DocumentosAdjuntos documentosAdjuntos;
	
	/**
	 * Interfaz para acceder la fuente de datos
	 */
	private ProcedimientoDao procedimientoDao = null;
	
	/**
	 * Campo utilizado para responder solicitudes de otros procedimientos hechas a centros de costo externos
	 */
	private String respuestaOtros;
	
	/**
	 * Manejo de solicitudes multiples
	 */
	private boolean multiple;
	
	/**
	 * Frecuencia en la solicitud multiple
	 */
	private float frecuencia;
	
	/**
	 * Tipo de frecuencia de la solcitud
	 */
	private InfoDatosInt tipoFrecuencia;
	
	/**
	 * Objeto que almacena los diagn�sticos de la respuesta del 
	 * procedimiento
	 */
	private HashMap diagnosticos  = new HashMap();
	
	/**
	 * Variable que indica el n�mero de diagn�sticos
	 * relacionados de la instituci�n
	 */
	private int numDiagnosticos;
	
	/**
	 * Cadena para saber si un procimiento es pos o no
	 */
	private String esPos;
	
	
	
	/**
	 * Creadora vacia de la clase
	 * @see java.lang.Object#Object()
	 */
	public Procedimiento()
	{
		this.fechaEjecucion = new String();
		this.resultados = new String();
		this.observaciones = new String();
		this.tipoRecargo = new InfoDatosInt();
		this.documentosAdjuntos = new DocumentosAdjuntos();
		this.init(System.getProperty("TIPOBD"));
		this.multiple=false;
		this.frecuencia=-1;
		this.tipoFrecuencia=new InfoDatosInt(0);
		//atributos de diagn�sticos
		this.diagnosticos = new HashMap();
		this.numDiagnosticos = 0;
		this.esPos="";
	}
	
	public Procedimiento(String fechaEjecucion, String resultados, String observaciones, InfoDatosInt tipoRecargo, String comentarioHistoriaClinica)
	{
		this.fechaEjecucion = fechaEjecucion;
		this.resultados = resultados;
		this.observaciones = observaciones;
		this.tipoRecargo = tipoRecargo;
		this.init(System.getProperty("TIPOBD"));
		this.comentarioHistoriaClinica = comentarioHistoriaClinica;
		this.multiple=false;
		this.frecuencia=-1;
		this.tipoFrecuencia=new InfoDatosInt(0);
		this.esPos=esPos;
	}
	
	
	/**
	 * @return Returns the esPos.
	 */
	public String getEsPos()
	{
		return esPos;
	}

	/**
	 * @param esPos The esPos to set.
	 */
	public void setEsPos(String esPos)
	{
		this.esPos=esPos;
	}

	/**
	 * @return Returns the diagnosticos.
	 */
	public HashMap getDiagnosticos() {
		return diagnosticos;
	}
	/**
	 * @param diagnosticos The diagnosticos to set.
	 */
	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	/**
	 * @return Returns the numDiagnosticos.
	 */
	public int getNumDiagnosticos() {
		return numDiagnosticos;
	}
	/**
	 * @param numDiagnosticos The numDiagnosticos to set.
	 */
	public void setNumDiagnosticos(int numDiagnosticos) {
		this.numDiagnosticos = numDiagnosticos;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public void init(String tipoBD)
	{
		if( procedimientoDao == null )
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

			if (myFactory != null)
			procedimientoDao = myFactory.getProcedimientoDao();
		}		
	}
	
	/**
	 * M�todo para retornar el DAO de procedimiento dao
	 * @return
	 */
	public static ProcedimientoDao procedimientoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcedimientoDao();
	}

	
	/**
	 * Retorna la fecha en que se ejecut� el procedimiento
	 * @return String
	 */
	public String getFechaEjecucion()
	{
		return fechaEjecucion;
	}

	/**
	 * Asigna la fecha en que se ejecut� el procedimiento
	 * @param fechaEjecucion The fechaEjecucion to set
	 */
	public void setFechaEjecucion(String fechaEjecucion)
	{
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * Retorna los resultados del procedimiento
	 * @return String
	 */
	public String getResultados()
	{
		return resultados;
	}

	/**
	 * Asigna los resultados del procedimiento
	 * @param resultados The resultados to set
	 */
	public void setResultados(String resultados)
	{
		this.resultados = resultados;
	}

	/**
	 * Retorna las observaciones del procedimiento
	 * @return String
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones del procedimiento
	 * @param observaciones The observaciones to set
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * Retorna el tipo de recargo del procedimiento
	 * @return InfoDatosInt
	 */
	public InfoDatosInt getTipoRecargo()
	{
		return tipoRecargo;
	}

	/**
	 * Asigna el tipo de recargo del procedimiento
	 * @param tipoRecargo The tipoRecargo to set
	 */
	public void setTipoRecargo(InfoDatosInt tipoRecargo)
	{
		this.tipoRecargo = tipoRecargo;
	}
	
	/**
	 * retorna el comentario adicional de la historia cl�nica
	 * @return String
	 */
	public String getComentarioHistoriaClinica() {
		return comentarioHistoriaClinica;
	}

	/**
	 * Asigna el comentario adicional de la historia cl�nica
	 * @param string
	 */
	public void setComentarioHistoriaClinica(String string) {
		comentarioHistoriaClinica = string;
	}


	/**
	 * Retorna los documentos adjuntos
	 * @return DocumentosAdjuntos
	 */
	public DocumentosAdjuntos getDocumentosAdjuntos()
	{
		return documentosAdjuntos;
	}

	/**
	 * Asigna los documentos adjuntos
	 * @param documentosAdjuntos The documentosAdjuntos to set
	 */
	public void setDocumentosAdjuntos(DocumentosAdjuntos documentosAdjuntos)
	{
		this.documentosAdjuntos = documentosAdjuntos;
	}
	
	/**
	 * Inserta dentro de una transacci�n un nuevo procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return ResultadoBoolean
	 */
	private ResultadoBoolean insertarProcedimientoTransaccional(	Connection con,
																					int numeroSolicitud,
																					String estado)
	{
		return this.procedimientoDao.insertarTransaccional(con, numeroSolicitud, this.fechaEjecucion, this.resultados, this.observaciones, this.tipoRecargo.getCodigo(), this.comentarioHistoriaClinica, estado);																					
	}
	
	/**
	 * M�todo est�tico que inserta la respuesta de un procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaEjecucion
	 * @param resultados
	 * @param observaciones
	 * @param tipoRecargo
	 * @param comentarioHistoriaClinica
	 * @param estado
	 * @return
	 */
	public static int insertarProcedimientoTransaccional(Connection con,int numeroSolicitud,String fechaEjecucion,String resultados,String observaciones,int tipoRecargo,String comentarioHistoriaClinica,String estado)
	{
		ResultadoBoolean resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcedimientoDao().insertarTransaccional(con, numeroSolicitud, fechaEjecucion, resultados, observaciones, tipoRecargo, comentarioHistoriaClinica, estado);
		int resp = 0;
		if(resultado.isTrue())
			resp = 1;
		return resp;
	}
	
	/**
	 * Modifica dentro de una transacci�n los datos del procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param estado
	 * @return ResultadoBoolean
	 */
	private ResultadoBoolean modificarProcedimientoTransaccional(Connection con,
																					int numeroSolicitud,
																					String estado)
	{
		return this.procedimientoDao.modificarTransaccional(con, numeroSolicitud, this.resultados, this.observaciones, this.comentarioHistoriaClinica, estado);
	}
	
	/**
	 * Inserta o modifica dependiendo del caso que aplique un procedimiento con
	 * todos sus datos.
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 * @throws Exception
	 */
	public ResultadoBoolean insertarModificar(Connection con, int numeroSolicitud, UsuarioBasico user, PersonaBasica persona ) throws Exception
	{
		ResultadoBoolean existeProc = this.procedimientoDao.existeProcedimiento(con, numeroSolicitud);
		this.setNumeroSolicitud(numeroSolicitud);
		
		// se verifica si la respuesta del procedimiento no se ha insertado
		if( !existeProc.isTrue() && !UtilidadCadena.noEsVacio(existeProc.getDescripcion()) )	// Insertar
		{
			//inserci�n de la respuesta del procedimiento
			ResultadoBoolean resultadoInsercion = this.insertarProcedimientoTransaccional(con, numeroSolicitud, ConstantesBD.inicioTransaccion );
			if( resultadoInsercion.isTrue() )
			{
				//actualizaci�n del n�mero de autorizacion----------------------------------------------------------------------
				/*if(this.getNumeroAutorizacion()!=null)
				{
					Solicitud solicitud = new Solicitud();
					ResultadoBoolean resultado = solicitud.actualizarNumeroAutorizacionTransaccional(con, this.getNumeroAutorizacion(), numeroSolicitud, ConstantesBD.continuarTransaccion);
					if(!resultado.isTrue())
					{
						logger.warn("No se actualiz� el n�mero de autorizaci�n en Valoracion Interconsulta: "+resultado.getDescripcion());
						return resultado;
					}
				}
				*/
				
				//inserci�n de los documentos adjuntos -----------------------------------------------------------------------------------
				resultadoInsercion = this.documentosAdjuntos.insertarEliminarDocumentosAdjuntosTransaccional(con, numeroSolicitud, ConstantesBD.continuarTransaccion);
				if( resultadoInsercion.isTrue() )
				{
				    Solicitud sol=new Solicitud();
				    //se actualiza el pool del m�dico--------------------------------------------------------------
				    ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(),user.getCodigoPersona());
					if(array.size()==1)
					{
						sol.actualizarPoolSolicitud(con,numeroSolicitud,Integer.parseInt(array.get(0)+""));
					}
					//se actualiza el m�dico que responde --------------------------------------------------------------------------
				    sol.actualizarMedicoRespondeTransaccional(con, numeroSolicitud, user, ConstantesBD.continuarTransaccion);
				    //se insertan los diagn�stico -------------------------------------------------------------------------------
				    resultadoInsercion = this.insertarDiagnosticos(con,false);
				    
				    if(resultadoInsercion.isTrue())
				    {
					    //-------------------------------------------------------------------------------------------------------
					    //se genera el cargo--------------------------------------------------------------------------------------
						resultadoInsercion = this.generarInfoSubCuentaYCargoServicio(con, numeroSolicitud, user, persona); 
						UtilidadBD.finalizarTransaccion(con);
						return resultadoInsercion;
				    }
				    else
				    	return resultadoInsercion;
				}
				else
					return resultadoInsercion;
			}
			else
				return resultadoInsercion;
		}
		else
		if( !existeProc.isTrue() && UtilidadCadena.noEsVacio(existeProc.getDescripcion()) ) // Error en la consuta de existe
		{
			return existeProc;
		}
		// se verifica si la respuesta del procideimeinto ya existe (quiere decir que ya es para modificar)
		else if( existeProc.isTrue() ) // Modificar
		{
			//modificacion de la respuesta del procedimiento---------------------------------------------------------------------------------
			ResultadoBoolean resultadoModificacion = this.modificarProcedimientoTransaccional(con, numeroSolicitud, ConstantesBD.inicioTransaccion);
			if( resultadoModificacion.isTrue() )
			{
				//inserci�n de los documentos adjuntos--------------------------------------------------------------------------------------
				resultadoModificacion = this.documentosAdjuntos.insertarEliminarDocumentosAdjuntosTransaccional(con, numeroSolicitud, ConstantesBD.continuarTransaccion);
				if(!resultadoModificacion.isTrue()&& UtilidadCadena.noEsVacio(resultadoModificacion.getDescripcion()))
				{
					logger.error("No se modific� el procedimietno al tratar de insertar documentos adjuntos");
					return resultadoModificacion;
				}
				
				/*if(this.getNumeroAutorizacion()!=null)
				{
					Solicitud solicitud = new Solicitud();
					//actualizaci�n del numero de autorizacion------------------------------------------------------------------------------------
					ResultadoBoolean resultado = solicitud.actualizarNumeroAutorizacionTransaccional(con, numeroAutorizacion, numeroSolicitud, ConstantesBD.finTransaccion);
					if(resultado.isTrue())
					{
						//modificacion de los diagn�sticos---------------------------------------------------------
						resultado = this.insertarDiagnosticos(con,true);
					}
					else
					{
						logger.warn("No se actualiz� el n�mero de autorizaci�n en Valoracion Interconsulta: "+resultado.getDescripcion());
						return resultado;
					}
				}
				*/
				return resultadoModificacion;
			}
			else
				return resultadoModificacion;			
		}			
		return new ResultadoBoolean(false);
	}

	/**
	 * M�todo implementado para insertar los diagn�sticos del
	 * procedimiento
	 * @param con
	 * @param esModificacion
	 * @return
	 */
	private ResultadoBoolean insertarDiagnosticos(Connection con, boolean esModificacion) 
	{
		ResultadoBoolean resp = new ResultadoBoolean(true);
		String auxS0 = "";
		int contador = 0;
		
		//Se verifica si es modificacion, para eliminar los registros actuales
		if(esModificacion)
			resp = this.procedimientoDao.eliminarDiagnosticos(con,this.numeroSolicitud,ConstantesBD.continuarTransaccion);
		
		if(resp.isTrue())
		{
			//INSERCI�N DEL DIAGN�STICO PRINCIPAL ******************************************
			auxS0 = this.diagnosticos.get("acronimoPrincipal") + "";
			//se verifica si se insert� diagn�stico principal
			if(!auxS0.equals("")&&!auxS0.equals("null"))
			{
				resp = this.procedimientoDao.insertarDiagnostico(
					con,
					this.numeroSolicitud,
					this.diagnosticos.get("acronimoPrincipal")+"",
					Integer.parseInt(this.diagnosticos.get("tipoCiePrincipal")+""),
					true,false,0,ConstantesBD.continuarTransaccion);
				
				if(!resp.isTrue())
					return resp;
			}
			
			//INSERCI�N DEL DIAGN�STICO DE COMPLICACI�N ***************************************
			auxS0 = this.diagnosticos.get("acronimoComplicacion") + "";
			//se verifica si se insert� diagn�stico de complicaci�n
			if(!auxS0.equals("")&&!auxS0.equals("null"))
			{
				resp = this.procedimientoDao.insertarDiagnostico(
					con,
					this.numeroSolicitud,
					this.diagnosticos.get("acronimoComplicacion")+"",
					Integer.parseInt(this.diagnosticos.get("tipoCieComplicacion")+""),
					false,true,0,ConstantesBD.continuarTransaccion);
				
				if(!resp.isTrue())
					return resp;
			}
			
			//INSERCI�N DE LOS DIAGN�STICOS RELACIONADOS *******************************************
			//iteraci�n de los diagn�sticos
			for(int i=0;i<this.numDiagnosticos;i++)
			{
				//se verifica si el diagn�stico fue chequeado
				if(UtilidadTexto.getBoolean(this.diagnosticos.get("checkRel_"+i)+""))
				{
					contador ++;
					
					resp = this.procedimientoDao.insertarDiagnostico(
							con,
							this.numeroSolicitud,
							this.diagnosticos.get("acronimoRel_"+i)+"",
							Integer.parseInt(this.diagnosticos.get("tipoCieRel_"+i)+""),
							false,false,contador,ConstantesBD.continuarTransaccion);
					
					if(!resp.isTrue())
						//se finaliza ciclo
						i = this.numDiagnosticos;
				}
			}
		}
		return resp;
	}

	public ResultadoBoolean cargar(Connection con,
			int numeroSolicitud)
	{
	    return this.cargar(con, numeroSolicitud, false);
	}
	
	/**
	 * M�todo para obtener los codigos de las respuestas de la solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCirugia
	 * @return
	 */
	public static ArrayList<String> obtenerCodigosRespuestas(Connection con,String numeroSolicitud,String codigoCirugia)
	{
		return procedimientoDao().obtenerCodigosRespuestas(con, numeroSolicitud, codigoCirugia);
	}
	
	/**
	 * Carga el procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @return ResultadoBoolean
	 */
	public ResultadoBoolean cargar(Connection con,
														int numeroSolicitud, boolean esSolicitud)
	{
		ResultadoCollectionDB procedimiento = this.procedimientoDao.cargar(con, numeroSolicitud);
		this.numeroSolicitud = numeroSolicitud;
		boolean temp;
		
		if( !procedimiento.isTrue() )
			return new ResultadoBoolean(false, procedimiento.getDescripcion());
		else
		{
			//this.cargarNumeroAutorizacion(con,numeroSolicitud);
			ArrayList listadoDocs = (ArrayList)procedimiento.getFilasRespuesta();
			int tam = listadoDocs.size();
			if( listadoDocs != null && tam > 0 )
			{		
				HashMap resultado = (HashMap)(listadoDocs).get(0);
				this.codigoPkResSolProc = Utilidades.convertirAEntero(resultado.get("codigoressolproc").toString());
				this.fechaEjecucion = UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(resultado.get("fechaejecucion")));
				this.fechaGrabacionRespuesta=UtilidadFecha.conversionFormatoFechaAAp(String.valueOf(resultado.get("fechagrabacionrespuesta")));
				this.horaGrabacionRespuesta=String.valueOf(resultado.get("horagrabacionrespuesta")) ;
				this.resultados = (String)resultado.get("resultados");
				this.tipoRecargo.setCodigo(Integer.valueOf(String.valueOf(resultado.get("codigotiporecargo"))));
				this.tipoRecargo.setNombre((String)resultado.get("nombretiporecargo"));
				this.observaciones = (String)resultado.get("observaciones");
				this.comentarioHistoriaClinica = (String)resultado.get("comentariohistoriaclinica").toString();
				temp=UtilidadTexto.getBoolean(resultado.get("espos")+"");
				if(temp)
				{
					this.esPos="SI";
				}
				else
				{
					this.esPos="NO";
				}
				this.documentosAdjuntos = new DocumentosAdjuntos();
				ResultadoBoolean resp=this.documentosAdjuntos.cargarDocumentosAdjuntos(con, numeroSolicitud, esSolicitud, "");
				if (!resp.isTrue()&&resp.getDescripcion()!=null&&!resp.getDescripcion().equals(""))
				{
				    //No carga bien si sale en false y hay alguna descripci�n
				    return resp;
				}
				else
				{
					//**SE CARGAN LOS DIAGN�STICOS (SI HAY)************************
					this.cargarDiagnosticos(con);
					//****************************************************
				    resp.setResultado(true);
				    return resp;
				}
			}
			else
			{
				logger.warn("No encontr� ninguna solicitud para el n�mero : "+numeroSolicitud);
				return new ResultadoBoolean(false, "No encontr� nada para este n�mero de solicitud");
			}
		}
	}
	
	/**
	 * M�todo implementado para cargar los diagn�sticos de un procedimiento
	 * @param con
	 */
	private void cargarDiagnosticos(Connection con) 
	{
		int numRegistros = 0;
		int numDiag = 0;
		String auxS0 = "", auxS1 = "";
		this.diagnosticos = new HashMap();
		HashMap mapaAux = this.procedimientoDao.cargarDiagnosticosXSolicitud(con,this.numeroSolicitud);
		numRegistros = Integer.parseInt(mapaAux.get("numRegistros")+"");
		
		for(int i=0;i<numRegistros;i++)
		{
			//se toman los indicativos de tipo diagn�stico
			auxS0 = mapaAux.get("principal_"+i) + "";
			auxS1 = mapaAux.get("complicacion_"+i) + "";
			
			//DIAGN�STICO PRINCIPAL
			if(UtilidadTexto.getBoolean(auxS0)&&!UtilidadTexto.getBoolean(auxS1))
			{
				this.diagnosticos.put("acronimoPrincipal",mapaAux.get("acronimo_"+i));
				this.diagnosticos.put("tipoCiePrincipal",mapaAux.get("tipo_cie_"+i));
				this.diagnosticos.put("nombrePrincipal",mapaAux.get("nombre_"+i));
				this.diagnosticos.put("numeroPrincipal",mapaAux.get("numero_"+i));
			}
			//DIAGN�STICO COMPLICACION
			else if(!UtilidadTexto.getBoolean(auxS0)&&UtilidadTexto.getBoolean(auxS1))
			{
				this.diagnosticos.put("acronimoComplicacion",mapaAux.get("acronimo_"+i));
				this.diagnosticos.put("tipoCieComplicacion",mapaAux.get("tipo_cie_"+i));
				this.diagnosticos.put("nombreComplicacion",mapaAux.get("nombre_"+i));
				this.diagnosticos.put("numeroComplicacion",mapaAux.get("numero_"+i));
			}
			//DIAGN�STICOS RELACIONADOS
			else if(!UtilidadTexto.getBoolean(auxS0)&&!UtilidadTexto.getBoolean(auxS1))
			{
				this.diagnosticos.put("acronimoRel_"+numDiag,mapaAux.get("acronimo_"+i));
				this.diagnosticos.put("tipoCieRel_"+numDiag,mapaAux.get("tipo_cie_"+i));
				this.diagnosticos.put("nombreRel_"+numDiag,mapaAux.get("nombre_"+i));
				this.diagnosticos.put("numeroRel_"+numDiag,mapaAux.get("numero_"+i));
				numDiag ++;
			}
		}
		this.numDiagnosticos = numDiag;
		this.diagnosticos.put("numRegistros",numRegistros+"");
		
	}

	/**
	 * M�todo para generar cargos, por ahora solo cambia los estados
	 * @param con
	 * @return ResultadoBoolean
	 */
	protected ResultadoBoolean generarInfoSubCuentaYCargoServicio(Connection con, int numeroSolicitud, UsuarioBasico user, PersonaBasica persona) throws Exception
	{
		Solicitud solicitud = new Solicitud();
		try
		{
			solicitud.cargar(con, this.getNumeroSolicitud());
		}
		catch(SQLException e)
		{
			logger.warn("Error cargando el centro de costo solicitado: "+e.getMessage());
		}
		ResultadoBoolean resultado=new ResultadoBoolean(true);
		if(solicitud.getCentroCostoSolicitado().getCodigo()==0)
		{
			solicitud.setNumeroSolicitud(this.getNumeroSolicitud());
			resultado=solicitud.cambiarCentroCostoSolicitado(con, this.getCentroCostoSolicitado());
		}
		
		if(resultado.isTrue())
		{
			//Cargo cargo=new Cargo();
			//if (estado.equals(ConstantesBD.inicioTransaccion))
			//{
				//cargo.generarCargoTransaccional(con,numeroSolicitud,this.getCentroCostoSolicitado(),persona.getCodigoContrato(),persona.getEstaContratoVencido(),persona.getCodigoUltimaViaIngreso(),ConstantesBD.codigoTipoSolicitudProcedimiento,user.getLoginUsuario(),1, 0, ConstantesBD.codigoServicioNoDefinido, false,"",/*<-aqui van las observaciones cuando se requieran*/  estado, false/*utilizarValorTarifaOpcional*/, -1/*valorTarifaOpcional*/);
				//return solicitud.cambiarEstadosSolicitudTransaccional(con, numeroSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.continuarTransaccion);
			//}
			//else if (estado.equals(ConstantesBD.continuarTransaccion))
			//{
				//cargo.generarCargoTransaccional(con,numeroSolicitud,this.getCentroCostoSolicitado(),persona.getCodigoContrato(),persona.getEstaContratoVencido(),persona.getCodigoUltimaViaIngreso(),ConstantesBD.codigoTipoSolicitudProcedimiento,user.getLoginUsuario(),1, 0, ConstantesBD.codigoServicioNoDefinido, false,"",/*<-aqui van las observaciones cuando se requieran*/  estado, false/*utilizarValorTarifaOpcional*/, -1/*valorTarifaOpcional*/);
				//return solicitud.cambiarEstadosSolicitudTransaccional(con, numeroSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, estado);
			//}
			//else
			//{
				//cargo.generarCargoTransaccional(con,numeroSolicitud,this.getCentroCostoSolicitado(),persona.getCodigoContrato(),persona.getEstaContratoVencido(),persona.getCodigoUltimaViaIngreso(),ConstantesBD.codigoTipoSolicitudProcedimiento,user.getLoginUsuario(),1, 0, ConstantesBD.codigoServicioNoDefinido, false,"",/*<-aqui van las observaciones cuando se requieran*/  ConstantesBD.continuarTransaccion, false/*utilizarValorTarifaOpcional*/, -1/*valorTarifaOpcional*/);
				//return solicitud.cambiarEstadosSolicitudTransaccional(con, numeroSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, estado);
			//}
			
			
			//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
		    																	user, 
		    																	persona, 
		    																	false/*dejarPendiente*/, 
		    																	numeroSolicitud, 
		    																	ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
		    																	persona.getCodigoCuenta(), 
		    																	this.getCentroCostoSolicitado()/*codigoCentroCostoEjecutaOPCIONAL*/, 
		    																	ConstantesBD.codigoNuncaValido/*codigoServicioOPCIONAL*/, 
		    																	1/*cantidadServicioOPCIONAL*/, 
		    																	ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/, 
		    																	ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
		    																	/*"" -- numeroAutorizacionOPCIONAL*/
		    																	""/*esPortatil*/,false,this.fechaEjecucion,"");
		
		    return solicitud.cambiarEstadosSolicitudTransaccional(con, numeroSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.continuarTransaccion);
		}
		else
		{
			logger.error("Error cambiando el centro de costo solicitado: "+ resultado.getDescripcion());
			return resultado;
		}
	}

	/**
	 * Retorna el numero de autorizacion de la solicitud del procedimiento
	 * @return String con el numero de autorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * Asigna el numero de autorizacion de la solicitud del procedimiento
	 * @param numeroAutorizacion
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	
	/*
	public ResultadoBoolean cargarNumeroAutorizacion(Connection con, int numeroSolicitud)
	{
		Solicitud solicitud= new Solicitud();
		
		ResultadoCollectionDB resultadoCargarNumeroAutorizacion = solicitud.cargarNumeroAutorizacion(con, numeroSolicitud);
		try{
			
			if(resultadoCargarNumeroAutorizacion.isTrue())
			{
				Collection fila=resultadoCargarNumeroAutorizacion.getFilasRespuesta();
				Iterator iterator=fila.iterator();
				while(iterator.hasNext())
				{
					HashMap res=(HashMap)iterator.next();
					this.setNumeroAutorizacion((String)res.get("numeroautorizacion"));
				}
			}
			else
			{
				logger.warn("Error cargando el Numero de Autorizacion en ValoracionInterconsulta: "+resultadoCargarNumeroAutorizacion.getDescripcion());
				return new ResultadoBoolean(resultadoCargarNumeroAutorizacion.isTrue(), resultadoCargarNumeroAutorizacion.getDescripcion());
			}

		}
		catch(Exception e)
		{
			logger.warn("Problema en ValoracionInterconsulta tratando de cargar el numero de autorizaci�n: "+e);
			return new ResultadoBoolean(false,"Problema en ValoracionInterconsulta tratando de cargar el numero de autorizaci�n: "+e);
		}
		return new ResultadoBoolean(resultadoCargarNumeroAutorizacion.isTrue(), resultadoCargarNumeroAutorizacion.getDescripcion());
	}
	*/
	/**
	 * Retorna la respuesta de solicitudes de otros procedimientos hechas a centros de costo externos
	 * @return
	 */
	public String getRespuestaOtros() {
		return respuestaOtros;
	}

	/**
	 * Asigna la respuesta de solicitudes de otros procedimientos hechas a centros de costo externos
	 * @param string
	 */
	public void setRespuestaOtros(String string) {
		respuestaOtros = string;
	}
	
	/**
	 * Carga la respuesta para centros de costo externos cuando se seleccion� un
	 * procedimietno no parametrizado (campo otro)
	 * @param con
	 * @return
	 */
	public ResultadoBoolean cargarOtros(Connection con)
	{
		ResultadoCollectionDB resultadoCargar = procedimientoDao.cargarOtros(con, this.getNumeroSolicitud());
		if(resultadoCargar.isTrue())
		{
			Iterator iterador=resultadoCargar.getFilasRespuesta().iterator();
			HashMap fila=(HashMap)iterador.next();
			this.setRespuestaOtros((String)fila.get("resultadosotros"));
			return new ResultadoBoolean(true);
		}
		else
		{
			logger.warn("Error ValoracionInterconsulta: "+resultadoCargar.getDescripcion());
			return new ResultadoBoolean(false, "Error ValoracionInterconsulta: "+resultadoCargar.getDescripcion());
		}
	}

	/**
	 * Inserta una respuesta para centros de costo externos cuando se seleccion� un
	 * procedimietno no parametrizado (campo otro)
	 * @param con
	 * @param numeroSolicitud
	 * @param respuestaOtros
	 * @return
	 */
	public ResultadoBoolean insertarOtros(Connection con)
	{
		ResultadoBoolean resultadoInsertar = procedimientoDao.insertarOtros(con, this.numeroSolicitud, this.respuestaOtros);
		if(!resultadoInsertar.isTrue())
		{
			logger.warn("Error insertando otros procedimientos: "+resultadoInsertar.getDescripcion());
			return resultadoInsertar;
		}
		else
		{
			resultadoInsertar=this.generarCargoOtros(con);
			if(!resultadoInsertar.isTrue())
			{
				logger.warn("Error cambiando estados para otros procedimientos: "+resultadoInsertar.getDescripcion());
			}
			return resultadoInsertar;
		}
	}

	protected ResultadoBoolean generarCargoOtros(Connection con)
	{
		Solicitud solicitud=new Solicitud();
		return solicitud.cambiarEstadosSolicitud(con, this.getNumeroSolicitud(), ConstantesBD.codigoEstadoFExterno, ConstantesBD.codigoEstadoHCRespondida);
	}

	
	
	public  String cargarExtraProcedimiento(Connection con, int numeroSolicitud)
    {
		return procedimientoDao.cargarExtraProcedimiento(con, numeroSolicitud);
    }
	
	/**
	 * Retorna el n�mero de solicitud del procedimiento
	 * @return
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * asigna el n�mero de solicitud del procedimiento
	 * @param i
	 */
	public void setNumeroSolicitud(int i) {
		numeroSolicitud = i;
	}

	/**
	 * @return
	 */
	public int getCentroCostoSolicitado() {
		return centroCostoSolicitado;
	}

	/**
	 * @param i
	 */
	public void setCentroCostoSolicitado(int i) {
		centroCostoSolicitado = i;
	}

	/**
	 *M�todo que devuelve el comentario de la historia 
	 *cl�nica en formato Html
	 * @return
	 */
	public String getComentarioHistoriaClinicaHtml()
	{
	    return UtilidadTexto.observacionAHTML(this.comentarioHistoriaClinica);
	}
	
	/**
	 *M�todo que devuelve los resultados en formato 
	 *Html
	 * @return
	 */
	public String getResultadosHtml()
	{
	    return UtilidadTexto.observacionAHTML(this.resultados);
	}
	
	/**
	 *M�todo que devuelve las observaciones en formato 
	 *Html
	 * @return
	 */
	public String getObservacionesHtml()
	{
	    return UtilidadTexto.observacionAHTML(this.observaciones);
	}
    /**
     * @return Returns the fechaGrabacionRespuesta.
     */
    public String getFechaGrabacionRespuesta()
    {
        return fechaGrabacionRespuesta;
    }
    /**
     * @param fechaGrabacionRespuesta The fechaGrabacionRespuesta to set.
     */
    public void setFechaGrabacionRespuesta(String fechaGrabacionRespuesta)
    {
        this.fechaGrabacionRespuesta = fechaGrabacionRespuesta;
    }
    /**
     * @return Returns the horaGrabacionRespuesta.
     */
    public String getHoraGrabacionRespuesta()
    {
        return horaGrabacionRespuesta;
    }
    /**
     * @param horaGrabacionRespuesta The horaGrabacionRespuesta to set.
     */
    public void setHoraGrabacionRespuesta(String horaGrabacionRespuesta)
    {
        this.horaGrabacionRespuesta = horaGrabacionRespuesta;
    }

	/**
	 * @return Retorna multiple.
	 */
	public boolean getMultiple()
	{
		return multiple;
	}

	/**
	 * @param multiple Asigna multiple.
	 */
	public void setMultiple(boolean multiple)
	{
		this.multiple = multiple;
	}

	/**
	 * @return Retorna frecuencia.
	 */
	public float getFrecuencia()
	{
		return frecuencia;
	}

	/**
	 * @param frecuencia Asigna frecuencia.
	 */
	public void setFrecuencia(float frecuencia)
	{
		this.frecuencia = frecuencia;
	}

	/**
	 * @return Retorna tipoFrecuencia.
	 */
	public InfoDatosInt getTipoFrecuencia()
	{
		return tipoFrecuencia;
	}

	/**
	 * @param tipoFrecuencia Asigna tipoFrecuencia.
	 */
	public void setTipoFrecuencia(InfoDatosInt tipoFrecuencia)
	{
		this.tipoFrecuencia = tipoFrecuencia;
	}

	/**
	 * @return the codigoPkResSolProc
	 */
	public int getCodigoPkResSolProc() {
		return codigoPkResSolProc;
	}

	/**
	 * @param codigoPkResSolProc the codigoPkResSolProc to set
	 */
	public void setCodigoPkResSolProc(int codigoPkResSolProc) {
		this.codigoPkResSolProc = codigoPkResSolProc;
	}
}
