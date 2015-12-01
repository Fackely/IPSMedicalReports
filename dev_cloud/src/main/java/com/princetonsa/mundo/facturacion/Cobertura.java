package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.facturacion.InfoCobertura;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CoberturaDao;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com  
 * */

public class Cobertura
{
	
	/**
	 * Objeto para almacenar los logs  
	 */
	private static Logger logger = Logger.getLogger(Cobertura.class); 
	
	//--------------------Atributos
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static CoberturaDao coberturaDao;
	
	/**
	 * Codigo de la coberuta
	 * */
	private String codigoCobertura;
	
	/**
	 * Codigo original de la cobertura para su modificacion
	 * */
	private String codigoCoberturaOld;
	
	/**
	 * Codigo de la institucion
	 * */
	private int institucion;
	
	/**
	 * Descripcion de la coberuta 
	 * */
	private String descripcionCobertura;
	
	/**
	 * Observaciones de la cobertura
	 * */
	private String observacionCobertura;
	
	/**
	 * Estado de la cobertura Activo o Inactivo
	 * */
	private String estadoCobertura;
	
	/**
	 * Usuario quien modifico por ultima vez o creo la cobertura
	 * */
	private String usuarioModifica;
	
	/**
	 * Fecha de ultima modificacion o creacion de la cobertura
	 * */
	private String fechaModifica;
	
	/**
	 * Hora de ultima modificacion o creacion de la cobertura
	 * */
	private String horaModifica;
	
	//--------------------Metodos
	
	private String tipoCobertura;
	
	
	
	/**
	 * Resetea todos los atributos de la clase 
	 * */
	public void reset()
	{
		this.codigoCobertura = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.descripcionCobertura = "";
		this.observacionCobertura = "";
		this.estadoCobertura = "ACTI";
	    this.usuarioModifica="";
	    this.fechaModifica="";
	    this.horaModifica="";	
	    this.tipoCobertura = "";
	}
	
	
	
	/**
	 * Constructor de la Clase
	 * */
	public Cobertura()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));		
	}
	
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (coberturaDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			coberturaDao = myFactory.getCoberturaDao();
		}	
	}
	
	
	/**
	 * @return DaoFactory
	 * */
	public static CoberturaDao coberturaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCoberturaDao();
	}
	
	
	
	/**
	 * Insertar un registro de coberturas
	 * @param con
	 * @param Cobertura cobertura
	 * */
	public boolean insertarCobertura(Connection con, Cobertura cobertura)
	{
		return coberturaDao.insertarCobertura(con, cobertura);		
	}
	
	
	
	/**
	 * Modifica una cobertura registrada
	 * @param con
	 * @param Cobertura cobertura
	 * */
	public boolean modificarCobertura(Connection con, Cobertura cobertura)
	{
		return coberturaDao.modificarCobertura(con, cobertura);
	}
	
	
	
	public String getTipoCobertura() {
		return tipoCobertura;
	}



	public void setTipoCobertura(String tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}



	/**
	 * Elimina una cobertura registrada
	 * @param con
	 * @param Cobertura cobertura
	 * */	
	public boolean eliminarCobertura(Connection con, String codigoCobertura, int institucion)
	{
		return coberturaDao.eliminarCobertura(con, codigoCobertura, institucion);
	}
	
	
	
	/**
	 * Consulta basica de coberturas por keys
	 * @param con
	 * @param codigoCobertura
	 * @param institucion
	 * */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCoberturaBasica(Connection con, String codigoCobertura, int institucion)
	{
		return coberturaDao.consultaCoberturaBasica(con, codigoCobertura, institucion);		 		
	}	
	
	
	/**
	 * Consulta avanzada de coberturas por cada uno de los campos
	 * @param con
	 * @param condicion 
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaCoberturaAvanzada(Connection con,HashMap condicion)
	{
		HashMap temp = new HashMap();
		temp = coberturaDao().consultaCoberturaAvanzada(con, condicion);
		
		for(int i=0; i<Integer.parseInt(temp.get("numRegistros").toString());i++)
		{
			if(coberturaDao().verificarDependencia(con,temp.get("codigo_"+i).toString().trim()))			
				temp.put("depende_"+i,ConstantesBD.acronimoSi);			
			else			
				temp.put("depende_"+i,ConstantesBD.acronimoNo);			
			
			temp.put("codigoOld_"+i,temp.get("codigo_"+i).toString().trim());
			
			
		}	
		
		return temp;				
	}	
	
	
	
	/**
	 * Método implementado para validar la cobertura del servicio de una entidad subcontratada
	 * @param con
	 * @param codigoEntidad
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @param fecha
	 * @return
	 */
	public static InfoCobertura validacionCoberturaServicioEntidadSubDadaEntidad(Connection con,String codigoEntidad,int codigoViaIngreso,String tipoPaciente,int codigoServicio,int codigoNaturalezaPaciente,int codigoInstitucion,String fecha) throws IPSException
	{
		InfoCobertura infoCobertura = new InfoCobertura();
		//Se busca el contrato vigente segun la entidad subcontratada
		DtoContratoEntidadSub contrato = CargosEntidadesSubcontratadas.obtenerContratoVigenteEntidadSubcontratada(con, codigoEntidad, fecha);
		
		if(!contrato.getConsecutivo().equals(""))
		{
			infoCobertura = validacionCoberturaServicioEntidadSub(con, Integer.parseInt(contrato.getConsecutivo()), codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
		}
		else
		{
			infoCobertura.setIncluido(false);
			infoCobertura.setExiste(false);
		}
		return infoCobertura;
	}
	
	public static InfoCobertura validacionCoberturaArticuloEntidadSubDadaEntidad(Connection con,String codigoEntidad,int codigoViaIngreso,String tipoPaciente,int codigoArticulo,int codigoNaturalezaPaciente,int codigoInstitucion,String fecha) throws IPSException
	{
		InfoCobertura infoCobertura = new InfoCobertura();
		//Se busca el contrato vigente segun la entidad subcontratada
		DtoContratoEntidadSub contrato = CargosEntidadesSubcontratadas.obtenerContratoVigenteEntidadSubcontratada(con, codigoEntidad, fecha);
		
		if(!contrato.getConsecutivo().equals(""))
		{
			infoCobertura = validacionCoberturaArticuloEntidadSub(con, Integer.parseInt(contrato.getConsecutivo()), codigoViaIngreso, tipoPaciente, codigoArticulo, codigoNaturalezaPaciente, codigoInstitucion);
		}
		else
		{
			infoCobertura.setIncluido(false);
			infoCobertura.setExiste(false);
		}
		return infoCobertura;
	}
	
	/**
	 * metodo estatico que evalua la cobertura o no de un servicio para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaServicioEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso,String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion ) throws IPSException
	{
		return coberturaDao().validacionCoberturaServicioEntidadSub(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
	}
	
	/**
	 * metodo estatico que evalua la cobertura o no de un servicio para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaServicio(Connection con, long codigoContrato, int codigoViaIngreso,String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion ) throws IPSException
	{
		return coberturaDao().validacionCoberturaServicio(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
	}

	/**
	 * metodo estatico que evalua la cobertura o no de un servicio para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaServicio(long codigoContrato, int codigoViaIngreso,String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion ) throws IPSException
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoCobertura info= coberturaDao().validacionCoberturaServicio(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoServicio, codigoNaturalezaPaciente, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	/**
	 * metodo estatico que evalua la cobertura o no de un servicio para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaPrograma(int codigoContrato, int codigoViaIngreso,String tipoPaciente, Double codigoPrograma,  int codigoNaturalezaPaciente, int codigoInstitucion ) throws IPSException
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoCobertura info= coberturaDao().validacionCoberturaPrograma(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoPrograma, codigoNaturalezaPaciente, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	
	/**
	 * metodo estatico que evalua la cobertura o no de un ARTICULO para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaArticuloEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion ) throws IPSException
	{
		return coberturaDao().validacionCoberturaArticuloEntidadSub(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoArticulo, codigoNaturalezaPaciente, codigoInstitucion);
	}
	
	/**
	 * metodo estatico que evalua la cobertura o no de un ARTICULO para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaArticulo(Connection con, int codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion ) throws IPSException
	{
		return coberturaDao().validacionCoberturaArticulo(con, codigoContrato, codigoViaIngreso, tipoPaciente, codigoArticulo, codigoNaturalezaPaciente, codigoInstitucion);
	}
	
	/**
	 *  metodo estatico que evalua la cobertura de un servicio para un responsable - via ingreso - codigo servicio - institucion, 
	 * devuelve true si lo cubre.
	 * 
	 * @param con
	 * @param dtoSubCuenta
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaServicioDadoResponsable(Connection con, DtoSubCuentas dtoSubCuenta, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoInstitucion) throws IPSException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		
		try {
			if(!Contrato.manejaCobertura(con, dtoSubCuenta.getContrato()))
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(false);
				infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
				infoCobertura.setCantidad(ConstantesBD.codigoNuncaValido);
				return infoCobertura;
			}
			else
			{	
				infoCobertura= validacionCoberturaServicio(	con, 
															dtoSubCuenta.getContrato(), 
															codigoViaIngreso,
															tipoPaciente,
															codigoServicio, 
															dtoSubCuenta.getNaturalezaPaciente(), 
															codigoInstitucion);
				//VERIFICAMOS QUE LA BUSQUEDA DE COBERTURA EXISTA Y LA INCLUYA
				if(infoCobertura.incluido()&&infoCobertura.existe())
				{
					//VERIFICAMOS QUE NO SOBREPASE LAS SEMANAS DE COTIZACION
					if(dtoSubCuenta.getSemanasCotizacion()>= infoCobertura.getSemanasMinimasCotizacion())
					{
						//CUMPLE CON LA VALIDACION DE LAS SEMANAS Y LO CUBRE
						return infoCobertura;
					}
					else
					{
						infoCobertura.setIncluido(false);
						infoCobertura.setRequiereAutorizacion(false);
						infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
					}
						
				}
				else if(!dtoSubCuenta.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
					//de lo contrario entonces no existe detalle y es requerida la autorizacion
					infoCobertura.setRequiereAutorizacion(true);
				
				return infoCobertura;
			}
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 *  metodo estatico que evalua la cobertura de un articulo para un responsable - via ingreso - codigo servicio - institucion, 
	 * devuelve true si lo cubre.
	 * 
	 * @param con
	 * @param dtoSubCuenta
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoCobertura validacionCoberturaArticuloDadoResponsable(Connection con, DtoSubCuentas dtoSubCuenta, int codigoViaIngreso,String tipoPaciente, int codigoArticulo, int codigoInstitucion) throws IPSException
	{
		
		InfoCobertura infoCobertura= new InfoCobertura();
		
		try{
			if(!Contrato.manejaCobertura(con, dtoSubCuenta.getContrato()))
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(false);
				infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
				infoCobertura.setCantidad(ConstantesBD.codigoNuncaValido);
				return infoCobertura;
			}
			else
			{	
				infoCobertura= validacionCoberturaArticulo(	con, 
														dtoSubCuenta.getContrato(), 
														codigoViaIngreso, 
														tipoPaciente,
														codigoArticulo, 
														dtoSubCuenta.getNaturalezaPaciente(), 
														codigoInstitucion);
				//VERIFICAMOS QUE LA BUSQUEDA DE COBERTURA EXISTA Y LA INCLUYA 
				if(infoCobertura.existe() && infoCobertura.incluido())
				{
					//VERIFICAMOS QUE NO SOBREPASE LAS SEMANAS DE COTIZACION
					if(dtoSubCuenta.getSemanasCotizacion()>= infoCobertura.getSemanasMinimasCotizacion())
					{
						//CUMPLE CON LA VALIDACION DE LAS SEMANAS Y LO CUBRE
						return infoCobertura;
					}
					else
					{
						infoCobertura.setIncluido(false);
						infoCobertura.setRequiereAutorizacion(false);
						infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
					}
				}
				return infoCobertura;
			}
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * metodo estatico que evalua la cobertura de un servicio para una cuenta - via ingreso - tipo paciente - codigo servicio - institucion, 
	 * devuelve un objeto que tiene encapsulado el DtoSubCuenta y la InfoCobertura que contiene los siguientes atributos:
	 * 
	 * InfoCobertura------------------>
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT
	 *  
	 * @param con
	 * @param idIngreso
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param pyp (para saber si se deben filtrar solo los convenios de pyp)
	 * @return
	 */
	public static InfoResponsableCobertura validacionCoberturaServicio(Connection con, String idIngreso, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoInstitucion,boolean pyp, String subCuentaCoberturaOPCIONAL) throws IPSException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		DtoSubCuentas dtoSubCuenta= new DtoSubCuentas();
		
		try {
			//PRIMERO CARGAMOS LA INFORMACION DE LA CUENTA,
			
			//OBTENEMOS LOS RESPONSABLES DE LA CUENTA
			ArrayList<DtoSubCuentas> dtoSubCuentasVector= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, Integer.parseInt(idIngreso),false,new String[0],pyp, subCuentaCoberturaOPCIONAL /*subCuenta*/,codigoViaIngreso);
			//ITERAMOS LOS N RESPONSABLES
			for(int w=0; w<dtoSubCuentasVector.size(); w++)
			{
				dtoSubCuenta=dtoSubCuentasVector.get(w);
				
				//SI EL CONTRATO NO MANEJA COBERTURA ENTONCES LA COBERTURA ES COMPLETA
				if(!Contrato.manejaCobertura(con, dtoSubCuenta.getContrato()))
				{
					infoCobertura.setExiste(true);
					infoCobertura.setIncluido(true);
					infoCobertura.setRequiereAutorizacion(false);
					infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
					infoCobertura.setCantidad(ConstantesBD.codigoNuncaValido);
					return new InfoResponsableCobertura(dtoSubCuenta, infoCobertura);
				}
				else
				{	
					infoCobertura= validacionCoberturaServicio(	con, 
																dtoSubCuenta.getContrato(), 
																codigoViaIngreso,
																tipoPaciente,
																codigoServicio, 
																dtoSubCuenta.getNaturalezaPaciente(), 
																codigoInstitucion);
					
					logger.info("dtoSubCuenta.getCodigoTipoRegimen()? "+dtoSubCuenta.getCodigoTipoRegimen());
					//VERIFICAMOS QUE LA BUSQUEDA DE COBERTURA EXISTA Y LA INCLUYA
					if(infoCobertura.incluido()&&infoCobertura.existe())
					{
						//VERIFICAMOS QUE NO SOBREPASE LAS SEMANAS DE COTIZACION O SI EL CONVENIO ES DE TIPO ATENCION ODONTOLOGICA
						if(dtoSubCuenta.getSemanasCotizacion()>= infoCobertura.getSemanasMinimasCotizacion() || Convenio.obtenerTipoAtencion(dtoSubCuenta.getConvenio().getCodigo()).equals(ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico))
						{
							//CUMPLE CON LA VALIDACION DE LAS SEMANAS Y LO CUBRE
							return new InfoResponsableCobertura(dtoSubCuenta, infoCobertura);
						}
						else
						{
							infoCobertura.setIncluido(false);
							infoCobertura.setRequiereAutorizacion(false);
							infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
						}
					}
					else if(!dtoSubCuenta.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
						infoCobertura.setRequiereAutorizacion(true);
					
				}	
			}
			//SI LLEGA A ESTE PUNTO ENTONCES ENVIAMOS EL DE ULTIMA PRIORIDAD ES DECIR EL ULTIMO QUE SE CARGO
			return new InfoResponsableCobertura(dtoSubCuenta, infoCobertura);
				
		}catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param contrato
	 * @param codigoNaturalezaPaciente
	 * @return
	 */
	public static boolean cubreServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoInstitucion, int contrato, int codigoNaturalezaPaciente) throws IPSException{
		InfoCobertura infoCobertura= new InfoCobertura();
		DtoSubCuentas dtoSubCuenta= new DtoSubCuentas();
		
		// SI EL CONTRATO NO MANEJA COBERTURA ENTONCES LA COBERTURA ES COMPLETA
		if(!Contrato.manejaCobertura(con, contrato))
			return true;
			
		else
		{	
			infoCobertura= validacionCoberturaServicio(	con, 
														contrato, 
														codigoViaIngreso,
														tipoPaciente,
														codigoServicio, 
														codigoNaturalezaPaciente, 
														codigoInstitucion);
			
			//VERIFICAMOS QUE LA BUSQUEDA DE COBERTURA EXISTA Y LA INCLUYA 
			if(infoCobertura.existe() && infoCobertura.incluido())
			{
				return true;
				/*
				//VERIFICAMOS QUE NO SOBREPASE LAS SEMANAS DE COTIZACION
				if(dtoSubCuenta.getSemanasCotizacion()>= infoCobertura.getSemanasMinimasCotizacion())
				{
					//CUMPLE CON LA VALIDACION DE LAS SEMANAS Y LO CUBRE
					return true;
				}
				else
				{
					logger.info("no lo cubre");
				}*/
			}
		}	
		return false;
	}
	
	/**
	 * metodo estatico que evalua la cobertura de un articulo para una cuenta - via ingreso - codigo servicio - institucion, 
	 * devuelve un objeto que tiene encapsulado el DtoSubCuenta y la InfoCobertura que contiene los siguientes atributos:
	 * 
	 * InfoCobertura------------------>
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT
	 *  
	 * @param con
	 * @param idIngreso
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param pyp (evalua cobertura solo para convenios pyp)
	 * @return
	 */
	public static InfoResponsableCobertura validacionCoberturaArticulo(Connection con, String idIngreso, int codigoViaIngreso, String tipoPaciente, int codigoArticulo, int codigoInstitucion,boolean pyp) throws IPSException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		DtoSubCuentas dtoSubCuenta= new DtoSubCuentas();
		
		try {
			//OBTENEMOS LOS RESPONSABLES DE LA CUENTA
			ArrayList<DtoSubCuentas> dtoSubCuentasVector= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, Integer.parseInt(idIngreso),false,new String[0],pyp, "" /*subCuenta*/,codigoViaIngreso);
			
			//ITERAMOS LOS N RESPONSABLES
			for(int w=0; w<dtoSubCuentasVector.size(); w++)
			{
				//EVALUAMOS QUE LOS RESPONSABLES NO ESTEN FACTURADOS
				dtoSubCuenta=dtoSubCuentasVector.get(w);
				
				//SI EL CONTRATO NO MANEJA COBERTURA ENTONCES LA COBERTURA ES COMPLETA
				if(!Contrato.manejaCobertura(con, dtoSubCuenta.getContrato()))
				{
					infoCobertura.setExiste(true);
					infoCobertura.setIncluido(true);
					infoCobertura.setRequiereAutorizacion(false);
					infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
					infoCobertura.setCantidad(ConstantesBD.codigoNuncaValido);
					return new InfoResponsableCobertura(dtoSubCuenta, infoCobertura);
				}
				else
				{	
					infoCobertura= validacionCoberturaArticulo ( con,
																dtoSubCuenta.getContrato(), 
																codigoViaIngreso,
																tipoPaciente,
																codigoArticulo, 
																dtoSubCuenta.getNaturalezaPaciente(), 
																codigoInstitucion);
					//VERIFICAMOS QUE LA BUSQUEDA DE COBERTURA EXISTA Y LA INCLUYA 
					if(infoCobertura.existe() && infoCobertura.incluido())
					{
						//VERIFICAMOS QUE NO SOBREPASE LAS SEMANAS DE COTIZACION
						if(dtoSubCuenta.getSemanasCotizacion()>= infoCobertura.getSemanasMinimasCotizacion())
						{
							//CUMPLE CON LA VALIDACION DE LAS SEMANAS Y LO CUBRE
							return new InfoResponsableCobertura(dtoSubCuenta, infoCobertura);
						}
						else
						{
							infoCobertura.setIncluido(false);
							infoCobertura.setRequiereAutorizacion(false);
							infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
						}
					}
				}	
			}
			//SI LLEGA A ESTE PUNTO ENTONCES ENVIAMOS EL DE ULTIMA PRIORIDAD ES DECIR EL ULTIMO QUE SE CARGO
			return new InfoResponsableCobertura(dtoSubCuenta, infoCobertura);
				
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}	
	}
	
	/**
	 * para las solicitudes que no tienen asignado un servicio se debe asignar esta solicitud al responsable
	 * de prioridad #1 y en la cobertura dejar la informacion en false (acordado con Margarita el 2007-07-04)
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static InfoResponsableCobertura validacionCoberturaNoAsignadoServicio(Connection con, String idIngreso,boolean pyp) throws IPSException
	{
		InfoCobertura infoCobertura= new InfoCobertura();
		DtoSubCuentas dtoSubCuenta= new DtoSubCuentas();
		
		//OBTENEMOS LOS RESPONSABLES DE LA CUENTA
		ArrayList<DtoSubCuentas> dtoSubCuentasVector= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, Integer.parseInt(idIngreso),false,new String[0],pyp, "" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Vía de ingreso*/);
		//OBTENEMOS EL RESPONSABLE DE PRIORIDAD #1
		if(dtoSubCuentasVector.size()>0)
		{
			dtoSubCuenta=dtoSubCuentasVector.get(0);
			dtoSubCuenta.loggerSubcuenta();
			
			if(!Contrato.manejaCobertura(con, dtoSubCuenta.getContrato()))
			{
				infoCobertura.setExiste(true);
				infoCobertura.setIncluido(true);
				infoCobertura.setRequiereAutorizacion(false);
				infoCobertura.setSemanasMinimasCotizacion(ConstantesBD.codigoNuncaValido);
				infoCobertura.setCantidad(ConstantesBD.codigoNuncaValido);
			}
		}
		
		return new InfoResponsableCobertura(dtoSubCuenta, infoCobertura);
	}
	
	//--------------------Getters And Setters

	
	public String getCodigoCobertura() {
		return codigoCobertura;
	}

	public void setCodigoCobertura(String codigoCobertura) {
		this.codigoCobertura = codigoCobertura;
	}

	public String getDescripcionCobertura() {
		return descripcionCobertura;
	}

	public void setDescripcionCobertura(String descripcionCobertura) {
		this.descripcionCobertura = descripcionCobertura;
	}

	public String getEstadoCobertura() {
		return estadoCobertura;
	}

	public void setEstadoCobertura(String estadoCobertura) {
		this.estadoCobertura = estadoCobertura;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getObservacionCobertura() {
		return observacionCobertura;
	}

	public void setObservacionCobertura(String observacionCobertura) {
		this.observacionCobertura = observacionCobertura;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the codigoCoberturaOld
	 */
	public String getCodigoCoberturaOld() {
		return codigoCoberturaOld;
	}

	/**
	 * @param codigoCoberturaOld the codigoCoberturaOld to set
	 */
	public void setCodigoCoberturaOld(String codigoCoberturaOld) {
		this.codigoCoberturaOld = codigoCoberturaOld;
	}	
}