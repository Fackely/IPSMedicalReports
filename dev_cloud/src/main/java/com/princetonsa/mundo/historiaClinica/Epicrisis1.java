package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.Epicrisis1Dao;
import com.princetonsa.dto.epicrisis.DtoCirugiaEpicrisis;
import com.princetonsa.dto.epicrisis.DtoEpicrisis1;
import com.princetonsa.dto.epicrisis.DtoEpicrisisSecciones;
import com.princetonsa.dto.epicrisis.DtoEvolucionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoNotasAclaratoriasEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionHospitalizacionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionUrgenciasEpicrisis;
import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;


/**
 * 
 * @author wilson
 *
 */
public class Epicrisis1 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static Epicrisis1Dao eDao;
	
	/**
	 * Maneja los logs del módulo de control de Solicitudes
	 */
	private static Logger logger = Logger.getLogger(Epicrisis1.class);
	
	/**
	 * tamanio de los campos de la tabla epicrisis_solicituudes de tipo clob para oracle y 1000 vc para postgres
	 */
	public static final int maximoContenidoCuadroTexto=4000;
	
	/**
	 * 
	 *
	 */
	public Epicrisis1()  
	{
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			eDao = myFactory.getEpicrisis1Dao();
			wasInited = (eDao != null);
		}
		return wasInited;
	}
	
	/**
	 * obtiene los tipos de evolucion dados los combinados de via ingreso - tipo paciente
	 * @param con
	 * @param viasIngresoTiposPaciente
	 * @return
	 */
	public static HashMap<Object, Object> obtenerTiposEvolucion(Connection con, ArrayList<InfoDatosInt> viasIngresoTiposPaciente )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().obtenerTiposEvolucion(con, viasIngresoTiposPaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @return
	 */
	public static HashMap<Object, Object> cargarCuadroTexto(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, boolean cargarTodas, boolean cargarValoracionesIniciales)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarCuadroTexto(con, cuentas, criteriosBusquedaMap, ingreso, cargarTodas, cargarValoracionesIniciales);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @return
	 */
	public static HashMap<Object, Object> obtenerFechaHoraSolicitudes(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, HashMap<Object, Object> valoracionesInicialesAmostrar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().obtenerFechaHoraSolicitudes(con, cuentas, criteriosBusquedaMap, ingreso, valoracionesInicialesAmostrar);
	}
	
	/**
	 * Metodo que carga el detalle de la epicrisis, es un mapa que contiene los dto de epicrisis dependiendo del tipo de evolucion - solicitud dada,
	 * para optimizar recursos siempre se carga los detalles que esten dentro del rango del paginador (metodologia JIT).
	 * @param con
	 * @param fechasHorasMap
	 * @param indiceInicialPager
	 * @param indiceFinalPager
	 * @return
	 */
	public static HashMap<Object, Object> cargarDetalleEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int indiceInicialPager, int indiceFinalPager, UsuarioBasico usuario, PersonaBasica paciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarDetalleEpicrisis(con, fechasHorasMap, indiceInicialPager, indiceFinalPager, usuario, paciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisis(Connection con, HashMap<Object, Object> cuadroTextoMap, String loginUsuario)
	{
		logger.info("\n******insertarEpicrisis->"+cuadroTextoMap+"\n");
		for(int w=0; w<Integer.parseInt(cuadroTextoMap.get("numRegistros")+""); w++)
		{
			if(Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos
				|| Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales)
			{
				if(existeEpicrisis(con,cuadroTextoMap.get("codigopk_"+w)+"", Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")))
				{
					logger.info("existe evento!!!!!!!!!");
					logger.info("contenido vacio-->"+UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()));
					logger.info("contenido-->"+cuadroTextoMap.get("contenido_"+w)+"");
					if(!UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()))
					{
						if(!modificarEpicrisisEventosAdversos(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
					else 
					{	
						if(!eliminarEpicrisis(con, cuadroTextoMap.get("codigopk_"+w)+"", ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos))
							return false;
					}		
				}
				else
				{	
					if(!UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()))
					{
						if(!insertarEpicrisisEventosAdversos(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
				}	
			}
			else if(Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia
				|| Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta
				|| Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos
				|| Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta
				|| Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
				|| Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp
				|| Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
			{
				if(existeEpicrisis(con,cuadroTextoMap.get("codigopk_"+w)+"", Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")))
				{
					logger.info("existe sol!!!!!!!!!");
					logger.info("contenido vacio-->"+UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()));
					logger.info("contenido-->"+cuadroTextoMap.get("contenido_"+w)+"");
					logger.info("Info Automatica-->"+cuadroTextoMap.get("infoautomatica_"+w));
					
					if(UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()) && UtilidadTexto.isEmpty((cuadroTextoMap.get("infoautomatica_"+w)+"").trim()))
					{	
						if(!eliminarEpicrisis(con, cuadroTextoMap.get("codigopk_"+w)+"", ConstantesBD.codigoTipoEvolucionEpicrisisCirugia))
							return false;
					}	
					else 
					{	
						if(!modificarEpicrisisSolicitudes(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
				}
				else
				{	
					logger.info("Info Automatica-->"+cuadroTextoMap.get("infoautomatica_"+w));
					if(UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()) && UtilidadTexto.isEmpty((cuadroTextoMap.get("infoautomatica_"+w)+"").trim()))
					{
						//nada
					}
					else
					{	
						if(!insertarEpicrisisSolicitudes(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
				}
			}
			else if(Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos)
			{
				logger.info("existe med!!!!!!!!!");
				logger.info("contenido vacio-->"+UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()));
				logger.info("contenido-->"+cuadroTextoMap.get("contenido_"+w)+"");
				
				if(existeEpicrisis(con,cuadroTextoMap.get("codigopk_"+w)+"", Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")))
				{
					if(!UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()))
					{
						if(!modificarEpicrisisAdminMed(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
					else 
					{	
						if(!eliminarEpicrisis(con, cuadroTextoMap.get("codigopk_"+w)+"", ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos))
							return false;
					}	
				}
				else
				{	
					if(!UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()))
					{
						if(!insertarEpicrisisAdminMed(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
				}
			}
			else if(Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
			{
				logger.info("existe evol!!!!!!!!!");
				logger.info("contenido vacio-->"+UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()));
				logger.info("contenido-->"+cuadroTextoMap.get("contenido_"+w)+"");
				
				if(existeEpicrisis(con,cuadroTextoMap.get("codigopk_"+w)+"", Integer.parseInt(cuadroTextoMap.get("codigotipoevolucion_"+w)+"")))
				{
					if(!UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()))
					{
						if(!modificarEpicrisisEvoluciones(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
					else 
					{	
						if(!eliminarEpicrisis(con, cuadroTextoMap.get("codigopk_"+w)+"", ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones))
							return false;
					}	
				}
				else
				{	
					if(!UtilidadTexto.isEmpty((cuadroTextoMap.get("contenido_"+w)+"").trim()))
					{
						if(!insertarEpicrisisEvoluciones(con, cuadroTextoMap, w, loginUsuario))
							return false;
					}	
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param fechasHorasMap
	 * @return
	 */
	public static boolean actualizarIndicativosEnviadosEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, HashMap<Object, Object> detalleMap, int ingreso, UsuarioBasico usuario)
	{
		for(int w=0; w<Integer.parseInt(fechasHorasMap.get("numRegistros")+""); w++)
		{
			if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos
				|| Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales)
			{
				if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadoepicrisis_"+w)+""))
				{	
					actualizarIndicativoEnviadoEpicrisisEventosAdversos(con, fechasHorasMap.get("codigopk_"+w)+"", ConstantesBD.acronimoSi);
				}
			}
			else if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos)
			{
				if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadoepicrisis_"+w)+""))
				{	
					actualizarIndicativoEnviadoEpicrisisProcedimientos(con, fechasHorasMap.get("codigopk_"+w)+"", ConstantesBD.acronimoSi);
				}
			}
			else if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos)
			{
				if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadoepicrisis_"+w)+""))
				{	
					actualizarIndicativoEnviadoEpicrisisAdminMed(con, fechasHorasMap.get("codigopk_"+w)+"", ConstantesBD.acronimoSi);
				}
			}
			else if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia)
			{
				DtoCirugiaEpicrisis dto= (DtoCirugiaEpicrisis) fechasHorasMap.get("DETALLE_"+w);
				
				if(dto!=null)
				{	
					for(int x=0; x<dto.getServicios().size(); x++)
					{	
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadoserviciocxepicrisis_"+dto.getNumeroSolicitud()+"_"+dto.getServicios().get(x).getCodigoServicio())+""))
						{	
							actualizarIndicaticoEnviadoEpicrisisCxServicio(con, dto.getServicios().get(w).getCodigo()+"", ConstantesBD.acronimoSi);
						}
					}
					if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadasalidasala_"+dto.getNumeroSolicitud())+""))
					{
						actualizarIndicaticoEnviadoEpicrisisCxSalidaPaciente(con, dto.getNumeroSolicitud()+"", ConstantesBD.acronimoSi);
					}
					for(int x=0; x<dto.getNotasEnfermeria().size(); x++)
					{	
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadanotaenfermeria_"+dto.getNumeroSolicitud()+"_"+dto.getNotasEnfermeria().get(x).getCodigo())+""))
						{
							actualizarIndicaticoEnviadoEpicrisisCxNotasEnfermeria(con, dto.getNotasEnfermeria().get(w).getCodigo()+"", ConstantesBD.acronimoSi);
						}
					}
					for(int x=0; x<dto.getNotasRecuperacion().size(); x++)
					{	
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadanotarecuperacion_"+dto.getNumeroSolicitud()+"_"+x)+""))
						{
							//se actualizan todas las notas de recuperacion pertenecientes a ese numero solicitud
							actualizarIndicaticoEnviadoEpicrisisCxNotasEnfermeria(con, dto.getNumeroSolicitud()+"", ConstantesBD.acronimoSi);
						}
					}
				}
			}
			else if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta)
			{
				String indiceCheckEnviar="";
				DtoValoracionEpicrisis dto= (DtoValoracionEpicrisis)detalleMap.get("DETALLE_"+w);
				if(dto!=null)
				{
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						DtoEpicrisisSecciones dtoE= new DtoEpicrisisSecciones();
						dtoE.setNumeroSolicitud(Integer.parseInt(fechasHorasMap.get("codigopk_"+w)+""));
						dtoE.setIngreso(ingreso);
						dtoE.setFunParam(dto.getDtoPlantilla().getCodigoFuncionalidad());
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPkFunParamSecFij())|| !UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							 dtoE.setFunParamSeccionFija(ConstantesBD.codigoNuncaValidoDoubleNegativo);
						else
							 dtoE.setFunParamSeccionFija(Double.parseDouble(seccionFija.getCodigoPkFunParamSecFij()));
						
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							dtoE.setPlantillaSecFija(ConstantesBD.codigoNuncaValido);
						else
							dtoE.setPlantillaSecFija(Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
						dtoE.setUsuario(usuario.getLoginUsuario());
						
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadaseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							if(Epicrisis1.existeEpicrisisSeccionesEnviadas(con, dtoE)<=0)
							{
								logger.info("NO EXISTE LA EPICRISIS SECCION ENVIADA!!!");
								Epicrisis1.insertarEpicrisisSecciones(con, dtoE);
							}
							else
							{
								logger.info("EXISTE LA EPICRISIS PARA EL DTO");
							}
						}
					}
					
					if(!UtilidadTexto.isEmpty(dto.getDtoInterpretacion().getInterpretacion()))
					{
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadainterpretacion_"+w)+""))
						{
							actualizarIndicativoEnviadoEpicrisisInterpretacion(con, fechasHorasMap.get("codigopk_"+w)+"", ConstantesBD.acronimoSi);
						}
					}
					
				}
			}
			else if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
					|| Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
			{
				String indiceCheckEnviar="";
				DtoValoracionHospitalizacionEpicrisis dto= (DtoValoracionHospitalizacionEpicrisis)detalleMap.get("DETALLE_"+w);
				if(dto!=null)
				{
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						DtoEpicrisisSecciones dtoE= new DtoEpicrisisSecciones();
						dtoE.setNumeroSolicitud(Integer.parseInt(fechasHorasMap.get("codigopk_"+w)+""));
						dtoE.setIngreso(ingreso);
						logger.info("\n\n ***************************dto.getDtoPlantilla().getCodigoFuncionalidad()->"+dto.getDtoPlantilla().getCodigoFuncionalidad());
						
						dtoE.setFunParam(dto.getDtoPlantilla().getCodigoFuncionalidad());
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPkFunParamSecFij())|| !UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							 dtoE.setFunParamSeccionFija(ConstantesBD.codigoNuncaValidoDoubleNegativo);
						else
							 dtoE.setFunParamSeccionFija(Double.parseDouble(seccionFija.getCodigoPkFunParamSecFij()));
						
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							dtoE.setPlantillaSecFija(ConstantesBD.codigoNuncaValido);
						else
							dtoE.setPlantillaSecFija(Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
						dtoE.setUsuario(usuario.getLoginUsuario());
						
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadaseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							if(Epicrisis1.existeEpicrisisSeccionesEnviadas(con, dtoE)<=0)
							{
								logger.info("NO EXISTE LA EPICRISIS SECCION ENVIADA!!!");
								Epicrisis1.insertarEpicrisisSecciones(con, dtoE);
							}
							else
							{
								logger.info("EXISTE LA EPICRISIS PARA EL DTO");
							}
						}
					}
				}
			}
			
			else if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
			{
				String indiceCheckEnviar="";
				DtoValoracionUrgenciasEpicrisis dto= (DtoValoracionUrgenciasEpicrisis)detalleMap.get("DETALLE_"+w);
				if(dto!=null)
				{
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						DtoEpicrisisSecciones dtoE= new DtoEpicrisisSecciones();
						dtoE.setNumeroSolicitud(Integer.parseInt(fechasHorasMap.get("codigopk_"+w)+""));
						dtoE.setIngreso(ingreso);
						logger.info("\n\n ***************************dto.getDtoPlantilla().getCodigoFuncionalidad()->"+dto.getDtoPlantilla().getCodigoFuncionalidad());
						
						dtoE.setFunParam(dto.getDtoPlantilla().getCodigoFuncionalidad());
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPkFunParamSecFij())|| !UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							 dtoE.setFunParamSeccionFija(ConstantesBD.codigoNuncaValidoDoubleNegativo);
						else
							 dtoE.setFunParamSeccionFija(Double.parseDouble(seccionFija.getCodigoPkFunParamSecFij()));
						
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							dtoE.setPlantillaSecFija(ConstantesBD.codigoNuncaValido);
						else
							dtoE.setPlantillaSecFija(Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
						dtoE.setUsuario(usuario.getLoginUsuario());
						
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadaseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							if(Epicrisis1.existeEpicrisisSeccionesEnviadas(con, dtoE)<=0)
							{
								logger.info("NO EXISTE LA EPICRISIS SECCION ENVIADA!!!");
								Epicrisis1.insertarEpicrisisSecciones(con, dtoE);
							}
							else
							{
								logger.info("EXISTE LA EPICRISIS PARA EL DTO");
							}
						}
					}
				}
			}
			
			else if(Integer.parseInt(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
			{
				String indiceCheckEnviar="";
				DtoEvolucionEpicrisis dto= (DtoEvolucionEpicrisis)detalleMap.get("DETALLE_"+w);
				if(dto!=null)
				{
					for(int x=0; x<dto.getDtoPlantilla().getSeccionesFijas().size();x++)
					{
						//primero verificamos si envio la informacion general
						DtoSeccionFija seccionFija=dto.getDtoPlantilla().getSeccionesFijas().get(x);
						DtoEpicrisisSecciones dtoE= new DtoEpicrisisSecciones();
						dtoE.setEvolucion(Integer.parseInt(fechasHorasMap.get("codigopk_"+w)+""));
						dtoE.setIngreso(ingreso);
						logger.info("\n\n ***************************dto.getDtoPlantilla().getCodigoFuncionalidad()->"+dto.getDtoPlantilla().getCodigoFuncionalidad());
						
						dtoE.setFunParam(dto.getDtoPlantilla().getCodigoFuncionalidad());
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPkFunParamSecFij())|| !UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							 dtoE.setFunParamSeccionFija(ConstantesBD.codigoNuncaValidoDoubleNegativo);
						else
							 dtoE.setFunParamSeccionFija(Double.parseDouble(seccionFija.getCodigoPkFunParamSecFij()));
						
						if(UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
							dtoE.setPlantillaSecFija(ConstantesBD.codigoNuncaValido);
						else
							dtoE.setPlantillaSecFija(Utilidades.convertirAEntero(seccionFija.getCodigoPK()));
						dtoE.setUsuario(usuario.getLoginUsuario());
						
						if(!UtilidadTexto.isEmpty(seccionFija.getCodigoPK()))
						{	
							indiceCheckEnviar=seccionFija.getCodigoPK();
						}
						else
						{
							indiceCheckEnviar=seccionFija.getCodigoPkFunParamSecFij();
						}
						
						if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaComentariosGenerales)
						{
							logger.info("es comentario general   indiceCheckEnviar->"+indiceCheckEnviar+"  seccionFija.getCodigoPK()->"+seccionFija.getCodigoPK()+" seccionFija.getCodigoPkFunParamSecFij()->"+seccionFija.getCodigoPkFunParamSecFij());
							logger.info("fechaHorasMap ya enviada seccion-->"+(fechasHorasMap.get("yaenviadaseccion_"+w+"_"+indiceCheckEnviar)+""));
							logger.info("\n\n\n mapa fechas-->"+fechasHorasMap);
						}
						
						if(UtilidadTexto.getBoolean(fechasHorasMap.get("yaenviadaseccion_"+w+"_"+indiceCheckEnviar)+""))
						{
							if(Epicrisis1.existeEpicrisisSeccionesEnviadas(con, dtoE)<=0)
							{
								logger.info("NO EXISTE LA EPICRISIS SECCION ENVIADA!!!");
								Epicrisis1.insertarEpicrisisSecciones(con, dtoE);
							}
							else
							{
								logger.info("EXISTE LA EPICRISIS PARA EL DTO");
							}
						}
					}
				}
			}
			
			
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().insertarEpicrisisEventosAdversos(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().modificarEpicrisisEventosAdversos(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().insertarEpicrisisSolicitudes(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().modificarEpicrisisSolicitudes(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().insertarEpicrisisAdminMed(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().modificarEpicrisisAdminMed(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().insertarEpicrisisEvoluciones(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().modificarEpicrisisEvoluciones(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param codigoTipoEvolucion
	 * @return
	 */
	public static boolean existeEpicrisis(Connection con, String codigoPk, int codigoTipoEvolucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().existeEpicrisis(con, codigoPk, codigoTipoEvolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisEventosAdversos(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicativoEnviadoEpicrisisEventosAdversos(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisProcedimientos(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicativoEnviadoEpicrisisProcedimientos(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisAdminMed(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicativoEnviadoEpicrisisAdminMed(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxServicio(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicaticoEnviadoEpicrisisCxServicio(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxSalidaPaciente(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicaticoEnviadoEpicrisisCxSalidaPaciente(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxNotasEnfermeria(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param tipoEvolucion
	 * @return
	 */
	public static boolean eliminarEpicrisis(Connection con, String codigoPk, int tipoEvolucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().eliminarEpicrisis(con, codigoPk, tipoEvolucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarEpicrisis(Connection con, DtoEpicrisis1 dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarEpicrisis(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarEncabezadoEpicrisis(Connection con, DtoEpicrisis1 dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().insertarEncabezadoEpicrisis(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean existeEncabezadoEpicrisis(Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().existeEncabezadoEpicrisis(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean existeFinalizacionEpicrisis(Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().existeFinalizacionEpicrisis(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double insertarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().insertarNotaAclaratoria(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarNotaAclaratoria(Connection con, double codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().eliminarNotaAclaratoria(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().modificarNotaAclaratoria(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public static HashMap<Object, Object> cargarNotasAclaratorias (Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarNotasAclaratorias(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static HashMap<Object, Object> cargarMedicoElaboraEpicrisis(Connection con, int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarMedicoElaboraEpicrisis(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<String> obtenerAntecedentesParametrizadosEpicrisis(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().obtenerAntecedentesParametrizadosEpicrisis(con, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double insertarEpicrisisSecciones(Connection con, DtoEpicrisisSecciones dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().insertarEpicrisisSecciones(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double existeEpicrisisSeccionesEnviadas(Connection con, DtoEpicrisisSecciones dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().existeEpicrisisSeccionesEnviadas(con, dto);
	}
	
	/**
	 * metodo que consulta las valoraciones iniciales de hospitalizacion y de urgencias
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static HashMap<Object, Object> cargarValoracionesIniciales(Connection con, Vector<String> cuentasIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarValoracionesIniciales(con, cuentasIngreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentasIngreso
	 * @param usuario
	 * @return
	 */
	public static HashMap<Object, Object> cargarUltimaEvolucion(Connection con, Vector<String> cuentasIngreso, UsuarioBasico usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarUltimaEvolucion(con, cuentasIngreso, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisInterpretacion(Connection con, String codigo, String epicrisisSN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().actualizarIndicativoEnviadoEpicrisisInterpretacion(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param arraySeccionesFijas
	 * @return
	 */
	public static boolean insertarInfoAutomaticaEvolucionEpicrisis(	Connection con, 
																	DtoEvolucion dtoEvolucion, 
																	UsuarioBasico usuario, 
																	PersonaBasica paciente, 
																	DtoPlantilla dtoPlantilla)
	{
		String contenido="";
		for(int w=0; w<dtoPlantilla.getSeccionesFijas().size(); w++)
		{
			DtoSeccionFija seccionFija= dtoPlantilla.getSeccionesFijas().get(w);
			if(seccionFija.isEnviarEpicrisis())
			{
				if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaInformacionGeneral)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionInformacionGeneral(seccionFija, dtoEvolucion);
				}
				else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaDiagnosticos)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionesDx(seccionFija, dtoEvolucion);
				}
				else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaDatosSubjetivos)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionesDatosSubjetivos(seccionFija, dtoEvolucion);
				}
				else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionfijaHallazgosImportantes)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionesHallazgosImportantes(seccionFija, dtoEvolucion);
				}
				else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaAnalisis)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionesAnalisis(seccionFija, dtoEvolucion);
				}
				else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaPlanManejo)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionesPlanManejo(seccionFija, dtoEvolucion);
				}
				else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaConductaSeguir)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionesConductaSeguir(seccionFija, dtoEvolucion);
				}
				else if(seccionFija.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaComentariosGenerales)
				{
					contenido+= Epicrisis1CuadroTexto.armarContenidoEvolucionesComentariosGenerales(seccionFija, dtoEvolucion);
				}
				
				contenido+= Epicrisis1CuadroTexto.armarContenidoSeccionesParametrizablesEvoluciones(seccionFija, dtoEvolucion);
				
				
				logger.info("EPICRISIS contenido-->"+contenido);
				
				DtoEpicrisisSecciones dtoEpicrisis= new DtoEpicrisisSecciones();
				dtoEpicrisis.setEvolucion(Integer.parseInt(dtoEvolucion.getCodigoEvolucion()));
				dtoEpicrisis.setFunParam(dtoPlantilla.getCodigoFuncionalidad());
				if(UtilidadTexto.isEmpty(dtoPlantilla.getSeccionesFijas().get(w).getCodigoPkFunParamSecFij())|| !UtilidadTexto.isEmpty(dtoPlantilla.getSeccionesFijas().get(w).getCodigoPK()))
					dtoEpicrisis.setFunParamSeccionFija(ConstantesBD.codigoNuncaValidoDoubleNegativo);
				else
					dtoEpicrisis.setFunParamSeccionFija(Double.parseDouble(dtoPlantilla.getSeccionesFijas().get(w).getCodigoPkFunParamSecFij()));
				dtoEpicrisis.setIngreso(paciente.getCodigoIngreso());
				dtoEpicrisis.setNumeroSolicitud(ConstantesBD.codigoNuncaValido);
				if(UtilidadTexto.isEmpty(dtoPlantilla.getSeccionesFijas().get(w).getCodigoPK()))
					dtoEpicrisis.setPlantillaSecFija(ConstantesBD.codigoNuncaValido);
				else
					dtoEpicrisis.setPlantillaSecFija(Utilidades.convertirAEntero(dtoPlantilla.getSeccionesFijas().get(w).getCodigoPK()));
				dtoEpicrisis.setUsuario(usuario.getLoginUsuario());
			 
				if(Epicrisis1.insertarEpicrisisSecciones(con, dtoEpicrisis)<=0)
				{
					logger.error("fallo epicrisis evol seccion pos->"+w);
					return false;
				}
			}
		}
		if(!UtilidadTexto.isEmpty(contenido.trim()))
		{
			HashMap<Object, Object> cuadroTextoMap= new HashMap<Object, Object>();
			cuadroTextoMap.put("contenido_0", contenido);
			cuadroTextoMap.put("codigopk_0", dtoEvolucion.getCodigoEvolucion());
			cuadroTextoMap.put("fechabd_0", UtilidadFecha.conversionFormatoFechaABD(dtoEvolucion.getFechaEvolucion()));
			cuadroTextoMap.put("hora_0", dtoEvolucion.getHoraEvolucion());
			cuadroTextoMap.put("numRegistros", 1);
		
			DtoEpicrisis1 dto = new DtoEpicrisis1();
			dto.setFinalizada(false);
			dto.setIngreso(paciente.getCodigoIngreso());
			dto.setUsuarioModifica(usuario.getLoginUsuario());
			
			if(existeEpicrisis(con, dtoEvolucion.getCodigoEvolucion(), ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones))
			{
				logger.info("existe EPICRISIS !!!!!");
				if(!modificarEpicrisisEvoluciones(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			else
			{	
				logger.info("NO existe EPICRISIS !!!!!");
				if(!insertarEpicrisisEvoluciones(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			
			if(Epicrisis1.existeEncabezadoEpicrisis(con, paciente.getCodigoIngreso()))
			{
				logger.info("existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.actualizarEpicrisis(con, dto);
			}
			else
			{	
				logger.info("NO existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.insertarEncabezadoEpicrisis(con, dto);
			}
		}
		return true;
	}

	/**
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static boolean insertarInfoAutomaticaCxEpicrisis(Connection con, String numeroSolicitud, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		logger.info("\n\n************************************************INSERTAR INFO EPICRISIS**************************************************");
		String contenido="";
		String contenidoPerfusion="", contenidoHallazgos="", contenidoPatologia="";
		SolicitudesCx solcx= new SolicitudesCx();
		solcx.cargarEncabezadoSolicitudCx(con, numeroSolicitud);
		HashMap<Object, Object> cuadroTextoMap= new HashMap<Object, Object>();
		cuadroTextoMap.put("contenido_0", contenido);
		cuadroTextoMap.put("codigopk_0", numeroSolicitud);
		cuadroTextoMap.put("fechabd_0", UtilidadFecha.conversionFormatoFechaABD(solcx.getFechaInicialCx()));
		cuadroTextoMap.put("hora_0", solcx.getHoraInicialCx());
		cuadroTextoMap.put("codigotipoevolucion_0", ConstantesBD.codigoTipoEvolucionEpicrisisCirugia);
		cuadroTextoMap.put("numRegistros", 1);
		
		Utilidades.imprimirMapa(cuadroTextoMap);
		
		if(UtilidadTexto.isEmpty(solcx.getHoraInicialCx()) && UtilidadTexto.isEmpty(solcx.getFechaInicialCx()))
			return true;
		
		//cargamos la descripcion qx por acto
		contenido+= HojaQuirurgica.consultarCamposTextoHQx(con,numeroSolicitud+"", ConstantesIntegridadDominio.acronimoTipoInformacionQuirurgica);
		
		if(!contenido.isEmpty())
			contenido= "DESCRIPCION QX POR ACTO: "+contenido;
		
		Vector<String> consecutivosSolCx= SolicitudesCx.obtenerConsecutivoSolCx(con, numeroSolicitud);
		
		logger.info("consecutivosSolCx-->"+consecutivosSolCx);
		
		String contenidoXServicio="";
		for(int w=0; w<consecutivosSolCx.size(); w++)
		{	
			HashMap<Object, Object> mapa= HojaQuirurgica.consultarDescripcionesQx2(con, consecutivosSolCx.get(w));
			for(int x=0; x<Integer.parseInt(mapa.get("numRegistros")+""); x++)
			{	
				contenidoXServicio+=mapa.get("descripcion_"+x)+"\n";
			}	
		}
		
		if(!contenidoXServicio.isEmpty())
		{
			contenido+="DESCRIPCIONES QX POR SERVICIO: \n"+contenidoXServicio;
		}
		
		//luego cargamos los campos perfusion y hallazgos
		contenidoPerfusion=HojaQuirurgica.consultarCamposTextoHQx(con, numeroSolicitud, ConstantesIntegridadDominio.acronimoTipoPerfusion);
		
		if(!UtilidadTexto.isEmpty(contenidoPerfusion))
		{
			contenido+="\nPERFUSION: "+contenidoPerfusion;
		}
		
		contenidoHallazgos=HojaQuirurgica.consultarCamposTextoHQx(con, numeroSolicitud, ConstantesIntegridadDominio.acronimoTipoHallazgos);
		
		if(!UtilidadTexto.isEmpty(contenidoHallazgos))
		{
			contenido+="\nHALLAZGOS: "+contenidoHallazgos;
		}
		
		//luego cargamos la informacion de patologia
		contenidoPatologia= HojaQuirurgica.consultarCamposTextoHQx(con,numeroSolicitud+"", ConstantesIntegridadDominio.acronimoTipoPatologia);
		if(!UtilidadTexto.isEmpty(contenidoPatologia))
		{
			contenido+="\nPATOLOGIA: "+contenidoPatologia;
		}
		
		contenido+="\n\n"+cargarInfoJusNoPos(con, Utilidades.convertirAEntero(numeroSolicitud), false, usuario.getCodigoInstitucionInt(), true, ConstantesBD.codigoNuncaValido/*codigoArticuloServicioOPCIONAL*/);
		
		logger.info("CONTENIDO-->"+contenido);
		cuadroTextoMap.put("contenido_0", contenido);
		
		if(!UtilidadTexto.isEmpty(contenido.trim()))
		{
			DtoEpicrisis1 dto = new DtoEpicrisis1();
			dto.setFinalizada(false);
			dto.setIngreso(paciente.getCodigoIngreso());
			dto.setUsuarioModifica(usuario.getLoginUsuario());
			
			if(existeEpicrisis(con, numeroSolicitud, ConstantesBD.codigoTipoEvolucionEpicrisisCirugia))
			{
				if(!modificarEpicrisisSolicitudes(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			else
			{	
				logger.info("NO existe EPICRISIS !!!!!");
				if(!insertarEpicrisisSolicitudes(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			if(Epicrisis1.existeEncabezadoEpicrisis(con, paciente.getCodigoIngreso()))
			{
				logger.info("existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.actualizarEpicrisis(con, dto);
			}
			else
			{	
				logger.info("NO existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.insertarEncabezadoEpicrisis(con, dto);
			}
		}
		logger.info("************************************************fin INSERTAR INFO EPICRISIS**************************************************\n\n");
		return true;
	}
	
	
	/**
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static boolean insertarInfoAutomaticaJusArticulosEpicrisis(Connection con, String numeroSolicitud, UsuarioBasico usuario, PersonaBasica paciente, String codigoAdmin) 
	{
		logger.info("\n\n************************************************INSERTAR INFO EPICRISIS**************************************************");
		String contenido="";
		HashMap<Object, Object> cuadroTextoMap= new HashMap<Object, Object>();
		cuadroTextoMap.put("contenido_0", contenido);
		cuadroTextoMap.put("codigopk_0", codigoAdmin);
		cuadroTextoMap.put("fechabd_0", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		cuadroTextoMap.put("hora_0", UtilidadFecha.getHoraActual());
		
		cuadroTextoMap.put("codigotipoevolucion_0", ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos);
		cuadroTextoMap.put("numRegistros", 1);
		
		//cargamos la descripcion del articulo
		contenido+= cargarInfoJusNoPos(con, Utilidades.convertirAEntero(numeroSolicitud), true, usuario.getCodigoInstitucionInt(), true, ConstantesBD.codigoNuncaValido/*codigoArticuloServicioOPCIONAL*/);
		
		logger.info("CONTENIDO-->"+contenido);
		cuadroTextoMap.put("contenido_0", contenido);
		
		if(!UtilidadTexto.isEmpty(contenido.trim()))
		{
			DtoEpicrisis1 dto = new DtoEpicrisis1();
			dto.setFinalizada(false);
			dto.setIngreso(paciente.getCodigoIngreso());
			dto.setUsuarioModifica(usuario.getLoginUsuario());
			
			if(existeEpicrisis(con, codigoAdmin, ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos))
			{
				if(!modificarEpicrisisAdminMed(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			else
			{	
				logger.info("NO existe EPICRISIS !!!!!");
				if(!insertarEpicrisisAdminMed(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			if(Epicrisis1.existeEncabezadoEpicrisis(con, paciente.getCodigoIngreso()))
			{
				logger.info("existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.actualizarEpicrisis(con, dto);
			}
			else
			{	
				logger.info("NO existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.insertarEncabezadoEpicrisis(con, dto);
			}
		}
		logger.info("************************************************fin INSERTAR INFO EPICRISIS**************************************************\n\n");
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String cargarInfoJusNoPos(Connection con, int numeroSolicitud, boolean esArticulo, int institucion, boolean cargarDescripcion, int codigoArticuloServicioOPCIONAL)
	{
		String titulo=esArticulo?"ARTICULO":"SERVICIO";
		HashMap<Object, Object> mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarInfoJusNoPos(con, numeroSolicitud, esArticulo, institucion, codigoArticuloServicioOPCIONAL);
		String retorna="";
		for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");w++)
		{
			if(cargarDescripcion)
			{	
				if(w!=0)
					retorna+="\n\n";
				retorna+=mapa.get("descripcion_"+w)+"\nRESUMEN DE HISTORIA CLINICA QUE JUSTIFIQUE EL "+titulo+" NO POS:";
			}	
			retorna+=mapa.get("resumen_his_cli_"+w);
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static boolean insertarInfoAutomaticaJusServiciosEpicrisis(Connection con, String numeroSolicitud, UsuarioBasico usuario, PersonaBasica paciente, int codigoTipoEvolucionEpicrisis, String fecha, String hora) 
	{
		logger.info("\n\n************************************************INSERTAR INFO EPICRISIS**************************************************");
		String contenido="";
		HashMap<Object, Object> cuadroTextoMap= new HashMap<Object, Object>();
		cuadroTextoMap.put("contenido_0", contenido);
		cuadroTextoMap.put("codigopk_0", numeroSolicitud);
		cuadroTextoMap.put("fechabd_0", fecha);
		cuadroTextoMap.put("hora_0", hora);
		cuadroTextoMap.put("codigotipoevolucion_0", codigoTipoEvolucionEpicrisis);
		cuadroTextoMap.put("numRegistros", 1);
		
		Utilidades.imprimirMapa(cuadroTextoMap);
		
		//cargamos la descripcion qx por acto
		contenido+= cargarInfoJusNoPos(con, Utilidades.convertirAEntero(numeroSolicitud), false /*esArticulo*/, usuario.getCodigoInstitucionInt(), true, ConstantesBD.codigoNuncaValido/*codigoArticuloServicioOPCIONAL*/);
		
		logger.info("CONTENIDO-->"+contenido);
		cuadroTextoMap.put("contenido_0", contenido);
		
		if(!UtilidadTexto.isEmpty(contenido.trim()))
		{
			DtoEpicrisis1 dto = new DtoEpicrisis1();
			dto.setFinalizada(false);
			dto.setIngreso(paciente.getCodigoIngreso());
			dto.setUsuarioModifica(usuario.getLoginUsuario());
			
			if(existeEpicrisis(con, numeroSolicitud, ConstantesBD.codigoTipoEvolucionEpicrisisCirugia))
			{
				if(!modificarEpicrisisSolicitudes(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			else
			{	
				logger.info("NO existe EPICRISIS !!!!!");
				if(!insertarEpicrisisSolicitudes(con, cuadroTextoMap, 0/*indice*/, usuario.getLoginUsuario()))
					return false;
			}
			if(Epicrisis1.existeEncabezadoEpicrisis(con, paciente.getCodigoIngreso()))
			{
				logger.info("existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.actualizarEpicrisis(con, dto);
			}
			else
			{	
				logger.info("NO existe ENCABEZADO EPICRISIS !!!!!");
				Epicrisis1.insertarEncabezadoEpicrisis(con, dto);
			}
		}
		logger.info("************************************************fin INSERTAR INFO EPICRISIS**************************************************\n\n");
		return true;
	}
	
	/**
	 * 
	 * @param idIngreso
	 * @return
	 */
	public static DtoEpicrisis1 cargarEpicrisis1(int idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().cargarEpicrisis1(idIngreso);
	}
	
	/**
	 * 
	 * @param valoracionesIniciales
	 * @return
	 */
	public static boolean esValoracionInicialEnEpicrisis(HashMap<Object, Object> valoracionesIniciales)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEpicrisis1Dao().esValoracionInicialEnEpicrisis(valoracionesIniciales);
	}

}
