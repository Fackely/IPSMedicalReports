/*
 * Enero 15, 2008
 */
package com.princetonsa.mundo.cargos;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosDirectosDao;
import com.princetonsa.dto.cargos.DtoCargoDirecto;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.cargos.DtoDiagnosticosCargoDirectoHC;
import com.princetonsa.dto.ordenes.DtoProcedimiento;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;


/**
 * 
 * @author Sebastián Gómez R.
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Cargos Directos
 *
 */
public class CargosDirectos 
{
	/**
	 * Logger de la clase
	 */
	private static Logger logger = Logger.getLogger(CargosDirectos.class);
	
	/**
	 * Dao de Cargos Directos
	 */
	private CargosDirectosDao cargoDirectoDao = null;
	
	/**
	 * DTO el cargo directo
	 */
	private ArrayList<DtoCargoDirecto> cargoDirecto = new ArrayList<DtoCargoDirecto>();
	
	/**
	 * DTO el cargo directo de hitoria clinica
	 */
	private DtoCargoDirectoHC cargoDirectoHC = new DtoCargoDirectoHC();
	
	//**********************************************************************************************
	//****************INICIALIZADORES & CONSTRUCTORES***********************************************
	/**
	 * Constructor
	 */
	public CargosDirectos()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.cargoDirecto = new ArrayList<DtoCargoDirecto>();
		this.cargoDirectoHC = new DtoCargoDirectoHC();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (cargoDirectoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cargoDirectoDao = myFactory.getCargosDirectosDao();
		}	
	}
	//************************************************************************************************
	//************************MÉTODOS ****************************************************************
	/**
	 * Método implementado para insertar regitros de cargos directos incluyendo su información de historia clínica
	 * Se envía un arreglo de DTO's se cargos directos y un DTO de informacion de historia clinica de cargo directo
	 * - Para que se inserte la información de historia clinica se debe llenar el atributo cargoDirectoHC y su atributo cargado = true
	 * - Para que se inserte información propia de cargos directos basta que se añada un DTO de cargo directo al arreglo cargoDirecto
	 * 
	 * - Se puede ingresar solo informacion de historia clinica o solamente informacion de cargo directo o ambas
	 * @param con
	 * @return
	 */
	public int insertar(Connection con)
	{
		logger.info("\n\n Especialidad Seleccionada en  mundoCargos INSERTAR >>"+cargoDirectoHC.getEspecialidadProfesional());
		return cargoDirectoDao.insertar(con, cargoDirecto, cargoDirectoHC);
	}
	
	
	//Anexo 550.*************************************************************************************
	/**
	 * Construye el dto de Cargos Directos HC 
	 * Connection con
	 * @param String fechaSolicitud
	 * @param String horaSolicitud
	 * @param HashMap diagnosticos
	 * @param int pos
	 * */
	public  void llenarMundoCargoDirectoHC(
			Connection con,
			UsuarioBasico usuario,			
			int servicio,			
			String tipoServicio,
			HashMap diagnosticos,
			int pos)
	{		
		//Objeto para la información de los diagnosticos
		DtoDiagnosticosCargoDirectoHC dtoDiagnosticosHC;
		//Objeto para la informacion del cargo directo HC
		DtoCargoDirectoHC dtoCargoDirectoHC = new DtoCargoDirectoHC();
		String [] vector;
		int numRegistros = 0;		
		
		//Inicializa la información del Dto de Cargos Directos Historia Clinica
		dtoCargoDirectoHC.setFechaSolicitud(diagnosticos.get("fechaCP_"+pos).toString());
		dtoCargoDirectoHC.setHoraSolicitud(UtilidadFecha.getHoraActual());		
		dtoCargoDirectoHC.setServicio(new InfoDatosInt(servicio,""));
		dtoCargoDirectoHC.setTipoServicio(new InfoDatos(tipoServicio,""));
		
		if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"") && 
				!diagnosticos.get("codigoCausaExterna_"+pos).toString().equals(""))
			dtoCargoDirectoHC.setCausaExterna(new InfoDatosInt(Integer.parseInt(diagnosticos.get("codigoCausaExterna_"+pos).toString()),""));
		
		dtoCargoDirectoHC.setManejaRips(UtilidadTexto.getBoolean(ValoresPorDefecto.getEntidadManejaRips(usuario.getCodigoInstitucionInt())));
		
		if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"") && 
				!diagnosticos.get("codigoFinalidadConsulta_"+pos).toString().equals(""))
			dtoCargoDirectoHC.setFinalidadConsulta(new InfoDatos(diagnosticos.get("codigoFinalidadConsulta_"+pos).toString(),""));			
			
		if(tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"") && 
				!diagnosticos.get("codigoFinalidadProcedimiento_"+pos).toString().equals(""))
			dtoCargoDirectoHC.setFinalidadProcedimiento(new InfoDatosInt(Integer.parseInt(diagnosticos.get("codigoFinalidadProcedimiento_"+pos).toString()),""));
		
		dtoCargoDirectoHC.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		dtoCargoDirectoHC.setUsuarioModifica(new InfoDatos(usuario.getLoginUsuario(),""));
		dtoCargoDirectoHC.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dtoCargoDirectoHC.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoCargoDirectoHC.setTipo(ConstantesIntegridadDominio.acronimoNormal);	
		
		//Carga el Diagnostico Principal
		if(!diagnosticos.get("dxPrincipal_"+pos).toString().equals(""))
		{
			dtoDiagnosticosHC = new DtoDiagnosticosCargoDirectoHC();
			vector =  diagnosticos.get("dxPrincipal_"+pos).toString().split(ConstantesBD.separadorSplit);
			dtoDiagnosticosHC.setPrincipal(true);
			dtoDiagnosticosHC.setComplicacion(false);
			dtoDiagnosticosHC.setDiagnostico(new Diagnostico(vector[0],Integer.parseInt(vector[1])));
			
			//Tipo de Diagnostico Principal
			if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"") && 
				diagnosticos.containsKey("codigoTipoDiagnosticoPrincipal_"+pos) && 
					!diagnosticos.get("codigoTipoDiagnosticoPrincipal_"+pos).toString().equals(""))			
				dtoDiagnosticosHC.setTipoDiagnostico(new InfoDatos(diagnosticos.get("codigoTipoDiagnosticoPrincipal_"+pos).toString(),""));
					
			dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagnosticosHC);
		}
		
		numRegistros = 0;
		//Carga los Diagnosticos Relacionados
		if(!diagnosticos.get("numDiagnosticosDefinitivos_"+pos).toString().equals("") && 
				!diagnosticos.get("numDiagnosticosDefinitivos_"+pos).toString().equals("0"))
		{
			numRegistros = Integer.parseInt(diagnosticos.get("numDiagnosticosDefinitivos_"+pos).toString());
			for(int i = 0 ; i < numRegistros; i++)
			{
				//Si esta seleccionado el diagnostico				
				if(UtilidadTexto.getBoolean(diagnosticos.get(pos+"_checkbox_"+i).toString()))
				{					
					dtoDiagnosticosHC = new DtoDiagnosticosCargoDirectoHC();
					vector =  diagnosticos.get(pos+"_"+i+"").toString().split(ConstantesBD.separadorSplit);
					dtoDiagnosticosHC.setPrincipal(false);
					dtoDiagnosticosHC.setComplicacion(false);
					dtoDiagnosticosHC.setDiagnostico(new Diagnostico(vector[0],Integer.parseInt(vector[1])));
					dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagnosticosHC);
				}
			}
		}		
		
		//Servicios de Tipo Procedimiento
		if(tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
		{			
			//Carga el diagnostico de Complicación
			if(!diagnosticos.get("dxComplicacion_"+pos).toString().equals(""))
			{
				dtoDiagnosticosHC = new DtoDiagnosticosCargoDirectoHC();
				vector =  diagnosticos.get("dxComplicacion_"+pos).toString().split(ConstantesBD.separadorSplit);
				dtoDiagnosticosHC.setPrincipal(false);
				dtoDiagnosticosHC.setComplicacion(true);
				dtoDiagnosticosHC.setDiagnostico(new Diagnostico(vector[0],Integer.parseInt(vector[1])));					
				dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagnosticosHC);
			}
		}
		
		//Retorna la información
		dtoCargoDirectoHC.setCargado(true);
		this.cargoDirectoHC =  dtoCargoDirectoHC;		
	}
	
	/**
	 * Método para cargar la información e historia clínica de un cargo directo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoCargoDirectoHC consultarInformacionHC(Connection con,String numeroSolicitud)
	{
		return cargoDirectoDao.consultarInformacionHC(con, numeroSolicitud);
	}
	//***********************************************************************************************
	
	//***********************************************************************************************
	//*************************GETTERS & SETTERS*****************************************************
	//***********************************************************************************************
	/**
	 * @return the cargoDirecto
	 */
	public ArrayList<DtoCargoDirecto> getCargoDirecto() {
		return cargoDirecto;
	}
	/**
	 * @param cargoDirecto the cargoDirecto to set
	 */
	public void setCargoDirecto(ArrayList<DtoCargoDirecto> cargoDirecto) {
		this.cargoDirecto = cargoDirecto;
	}
	/**
	 * @return the cargoDirectoHC
	 */
	public DtoCargoDirectoHC getCargoDirectoHC() {
		return cargoDirectoHC;
	}
	/**
	 * @param cargoDirectoHC the cargoDirectoHC to set
	 */
	public void setCargoDirectoHC(DtoCargoDirectoHC cargoDirectoHC) {
		logger.info("\n\n Especialidad Seleccionada en cargoDirectoHC >>"+cargoDirectoHC.getEspecialidadProfesional());
		this.cargoDirectoHC = cargoDirectoHC;
	}
	/**
	 * Método implementado para llenar el mundo de cargo directo y lo añade al arreglo de cargos directos
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @param tipoRecargo
	 * @param codigoServicioSolicitado
	 * @param codigoDatosHC
	 * @param afectaInventarios 
	 * @param fechaEjecucion 
	 */
	public void llenarMundoCargoDirecto(int numeroSolicitud, String loginUsuario, int tipoRecargo, int codigoServicioSolicitado, String codigoDatosHC, boolean afectaInventarios, String fechaEjecucion) 
	{
		DtoCargoDirecto dtoCargoDirecto = new DtoCargoDirecto();
		dtoCargoDirecto.setNumeroSolicitud(numeroSolicitud+"");
		dtoCargoDirecto.setLoginUsuario(loginUsuario);
		dtoCargoDirecto.setCodigoTipoRecargo(tipoRecargo);
		dtoCargoDirecto.setCodigoServicioSolicitado(codigoServicioSolicitado);
		dtoCargoDirecto.setCodigoDatosHC(codigoDatosHC);
		dtoCargoDirecto.setAfectaInventarios(afectaInventarios);
		dtoCargoDirecto.setFechaEjecucion(fechaEjecucion);
		
		//Se añade un nuevo elemento del cargo directo
		this.cargoDirecto.add(dtoCargoDirecto);
		
	}
	
	/**
	 * M&eacute;todo encargado de buscar los servicios asociados a las 
	 * solicitudes generadas de cargos directos de sevicios
	 * @author Diana Carolina G
	 */
	public DtoProcedimiento buscarServiciosCargosDirectos(Connection con, int numeroSolicitud, int codigoTarifario)
	{
		return DaoFactory.getDaoFactory(
	  			   System.getProperty("TIPOBD")).getCargosDirectosDao().buscarServiciosCargosDirectos(con, 
	  					   numeroSolicitud, codigoTarifario);
		
	}
}
