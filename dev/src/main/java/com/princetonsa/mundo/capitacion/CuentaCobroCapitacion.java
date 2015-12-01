/*
 * Creado en Jun 13, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import java.lang.Double;
import java.util.*;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.CuentaCobroCapitacionDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

public class CuentaCobroCapitacion
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CuentaCobroCapitacion.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CuentaCobroCapitacionDao  cuentaCobroCapitacionDao = null;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Campo que contiene la fecha inicial para realizar la búsqueda
	 * de los cargos
	 */
	private String fechaInicialGeneracion;
	
	/**
	 * Campo que contiene la fecha final para realizar la búsqueda
	 * de los cargos
	 */
	private String fechaFinalGeneracion;
	
	/**
	 * Campo que guarda las observaciones de la cuenta de cobro
	 */
	private String observacionesCuentaCobro;
	
	  /**
     * Mapa que contiene los cargues consultados de acuerdo a los parámetros
     * de busqueda    
     */
    private HashMap mapaCargues;
    
    /**
     * Campo que guarda el convenio que se selecciona en la búsqueda
     * de cuenta de cobro por convenio
     */
    private int convenioCapitado;
    
    /**
     * Campo que guarda el motivo de anulación
     */
    private String motivoAnulacion;
    
    /**
     * Campo que guarda el número de la cuenta de cobro que
     * se va ha modificar
     */
    private int cuentaCobroModificar;
    
    /**
     * Campo que guarda la fecha inicial en el modificar
     */
    private String fechaInicialModificar;
    
    /**
     * Campo que guarda la fecha final en el modificar
     */
    private String fechaFinalModificar;
    
    /**
     * Campo que guarda las observaciones de la cuenta de cobro  en el modificar
     */
    private String observacionesModificar;
	
//	---------------------------------------------------FIN DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public CuentaCobroCapitacion ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
		this.fechaInicialGeneracion="";
		this.fechaFinalGeneracion="";
		this.observacionesCuentaCobro="";
		this.mapaCargues=new HashMap();
		this.convenioCapitado=-1;
		this.motivoAnulacion="";
		this.fechaInicialModificar="";
		this.fechaFinalModificar="";
		this.observacionesModificar="";
		this.cuentaCobroModificar=0;
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
			cuentaCobroCapitacionDao = myFactory.getCuentaCobroCapitacionDao();
			wasInited = (cuentaCobroCapitacionDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que consulta los cargues que cumplen con los parámetros de búsqueda
	 * ya sea por periodo o por convenio
	 * del paciente
	 * @param con
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	public HashMap consultarCargues(Connection con, String fechaFinal, String fechaInicial, int convenio)
	{
		return cuentaCobroCapitacionDao.consultarCargues(con, fechaFinal, fechaInicial, convenio);
	}
	
	/**
	 * Método que consulta las cuentas de cobro por convenio, de acuerdo a la fecha 
	 * inicial y final
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarCuentasCobroConvenio(Connection con, String fechaInicial, String fechaFinal, int codigoInstitucion)
	{
		return cuentaCobroCapitacionDao.consultarCuentasCobroConvenio(con, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), UtilidadFecha.conversionFormatoFechaABD(fechaFinal), codigoInstitucion);
	}
	
	/**
	 * Método que consulta los contratos_cargue asociados a la cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion 
	 * @return
	 */
	public HashMap consultarDetalleCuentaCobro(Connection con, int numeroCuentaCobro, int institucion)
	{
		return cuentaCobroCapitacionDao.consultarDetalleCuentaCobro(con, numeroCuentaCobro, institucion);
	}
	
	/**
	 * Método que genera la cuenta de cobro de capitación para cada uno
	 * de los cargues seleccionados
	 * @param con
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	public String generarCuentaCobroCapitacion(Connection con, UsuarioBasico usuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		String cuentaCobroGenerada="";
		
		int resp=0, consecutivoCxCCapitacion=0;
				
		if (cuentaCobroCapitacionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CuentaCobroCapitacionDao - generarCuentaCobroCapitacion )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		if (this.mapaCargues != null)
		{
			Vector cuentasConvenioGen=new Vector();
			Vector consecutivosCarteraGen=new Vector();
			
			int numRegistros=Integer.parseInt(this.mapaCargues.get("numRegistros")+"");
			
			for (int i=0; i<numRegistros; i++)
				{
					String seleccionado= (String)this.getMapaCargues("cargue_"+i);
										
					//--------Se verifica si fué seleccionado el cargue ----------//
					if (UtilidadTexto.getBoolean(seleccionado))
					{
						int tipoPago= Integer.parseInt(this.getMapaCargues("tipo_pago_"+i)+"");
						int codigoCargue= Integer.parseInt(this.getMapaCargues("codigo_"+i)+"");
						int codigoConvenio= Integer.parseInt(this.getMapaCargues("codigo_convenio_"+i)+"");
						
						logger.info("\n seleccionado ->"+seleccionado+"\n");
						logger.info("\n codigoCargue ->"+codigoCargue+"\n");
						
						//------------Se verifica que tipo de consecutivo de capitación se debe generar ------------------------//
						if (!cuentasConvenioGen.contains(codigoConvenio))
						{
							if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoCapitacionDiferenteCartera)
							{
								consecutivoCxCCapitacion= Integer.parseInt(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoCuentaCobroCapitacion, usuario.getCodigoInstitucionInt()));								
							}
							else if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoCapitacionUnicoCartera)
							{
								consecutivoCxCCapitacion= Integer.parseInt(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt()));
							}
							else if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoFacturaPaciente)
							{
								int consecutivo=Utilidades.convertirAEntero(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt()));
								//VALIDACION PARA QUE EL CONSECUTIVO NO SE CRUCE CON EL DE FACTURAS.
								boolean consecutivoValido=esConsecutivoValido(ConstantesBD.nombreConsecutivoFacturas,consecutivo,usuario.getCodigoInstitucionInt());
								if(!consecutivoValido)
								{
									int contIntentos=0;
									consecutivo=0;
									while(consecutivo<=0&&contIntentos<100)
									{
										consecutivo=Utilidades.convertirAEntero(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt()));
										contIntentos++;
										consecutivoValido=esConsecutivoValido(ConstantesBD.nombreConsecutivoFacturas,consecutivo,usuario.getCodigoInstitucionInt());
										if(!consecutivoValido)
											consecutivo=0;
									}
								}
								consecutivoCxCCapitacion= consecutivo;
							}
						}
						else
						{
							consecutivoCxCCapitacion=Integer.parseInt(consecutivosCarteraGen.elementAt(cuentasConvenioGen.indexOf(codigoConvenio))+"");
						}

						//---------Se verifica si ya se generó consecutivo para el convenio ----------------//
						if (!cuentasConvenioGen.contains(codigoConvenio))
						{
							cuentasConvenioGen.add(codigoConvenio);
							consecutivosCarteraGen.add(consecutivoCxCCapitacion);
							
							if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoCapitacionDiferenteCartera)
							{
								//---------Se incrementa el consecutivo ---------------------//
								UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoCuentaCobroCapitacion,usuario.getCodigoInstitucionInt(),consecutivoCxCCapitacion+"",ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
							}
							else if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoCapitacionUnicoCartera)
							{
								//---------Se incrementa el consecutivo ---------------------//
								UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoCuentasCobro,usuario.getCodigoInstitucionInt(),consecutivoCxCCapitacion+"",ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
							}							
							else if (Integer.parseInt(ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoTipoConsecutivoFacturaPaciente)
							{
								//---------Se incrementa el consecutivo ---------------------//
								UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoFacturas,usuario.getCodigoInstitucionInt(),consecutivoCxCCapitacion+"",ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
							}

						}
						
						int posicion=cuentasConvenioGen.indexOf(codigoConvenio);
						int consecutivoCartera=Integer.parseInt(consecutivosCarteraGen.elementAt(posicion)+"");

						logger.info("\n consecutivoCartera ->"+consecutivoCartera+"\n");
						//logger.info("\n cuentasConvenioGen ->"+cuentasConvenioGen+"\n");
						//logger.info("\n consecutivosCarteraGen ->"+consecutivosCarteraGen+"\n");
						
						//--------Si el tipo de pago es UPC-------//
						if (tipoPago == Integer.parseInt(ConstantesBD.codigoTipoPagoUpc))
							{
							double valorTotal= Double.parseDouble(this.getMapaCargues("valor_total_"+i)+"");
														
							logger.info("\n valorTotal ->"+valorTotal+"\n");
							
							resp=cuentaCobroCapitacionDao.actualizarContratoCargue (con, codigoCargue, consecutivoCartera, valorTotal, usuario.getCodigoInstitucionInt());
							
							if(!(this.mapaCargues.get("total_usuarios_"+i)+"").equals(this.mapaCargues.get("total_usuarios_old_"+i)+"")||!(this.mapaCargues.get("upc_"+i)+"").equals(this.mapaCargues.get("upc_old_"+i)+""))
							{
								resp=cuentaCobroCapitacionDao.actualizarUpcTotalUserContratoCargue (con, codigoCargue, usuario.getCodigoInstitucionInt(),this.getMapaCargues("total_usuarios_"+i)+"",this.getMapaCargues("upc_"+i)+"",this.getMapaCargues("total_usuarios_old_"+i)+"",this.getMapaCargues("upc_old_"+i)+"",usuario.getLoginUsuario());
							}
							
							
								if (resp <0)
								{
									error=true;
									break;
								}
							}
						else if (tipoPago == Integer.parseInt(ConstantesBD.codigoTipoPagoGrupoEtareo))
						{
							resp=cuentaCobroCapitacionDao.actualizarCargueGrupoEtareo (con, codigoCargue, consecutivoCartera, usuario.getCodigoInstitucionInt());
							
							if (resp <0)
							{
								error=true;
								break;
							}
						}
					}//if cargue seleccionado
					
				}//for
			
			//--------------Se generan las cuentas de cobro de capitación con lo insertado anteriormente ------------------//
			if (!error)
			{
				for (int c=0; c<cuentasConvenioGen.size(); c++)
				{
					int numeroCuentaCobro=Integer.parseInt(consecutivosCarteraGen.elementAt(c)+"");
					int convenioCxC=Integer.parseInt(cuentasConvenioGen.elementAt(c)+"");
					
					//----Se inserta la  cuenta de cobro de capitación con valor inicial cero para despues poder realizar el cálculo de 
					//----del valor inicial de acuerdo a la cuenta de cobro y a la institución---------------------//
					resp=cuentaCobroCapitacionDao.insertarCuentaCobroCapitacion (con, numeroCuentaCobro, convenioCxC, ConstantesBD.codigoEstadoCarteraGenerado, usuario.getLoginUsuario(), 0, this.fechaInicialGeneracion, this.fechaFinalGeneracion, this.observacionesCuentaCobro, usuario.getCodigoInstitucionInt(), UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()), UtilidadFecha.getHoraSegundosActual(), usuario.getCodigoCentroAtencion()+"");
					
					//-------Se obtiene el valor inicial de la cuenta de cobro de capitación ---------//
					double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, numeroCuentaCobro, usuario.getCodigoInstitucionInt());
					
					logger.info("\n cuenta cobro-->"+numeroCuentaCobro+"\n");
					logger.info("\n convenioCxC-->"+convenioCxC+"\n");
					logger.info("\n valorInicialCuenta-->"+valorInicialCuenta+"\n");
					
					//---------------Se actualiza la  cuenta de cobro de capitación con el valor inicial calculado para el número de cuenta de cobro e institución---------------------//
					resp=cuentaCobroCapitacionDao.actualizarCuentaCobroCapitacion (con, numeroCuentaCobro, usuario.getLoginUsuario(), valorInicialCuenta, this.fechaInicialGeneracion, this.fechaFinalGeneracion, this.observacionesCuentaCobro, usuario.getCodigoInstitucionInt());
					
					//resp=cuentaCobroCapitacionDao.insertarCuentaCobroCapitacion (con, numeroCuentaCobro, convenioCxC, ConstantesBD.codigoEstadoCarteraGenerado, usuario.getLoginUsuario(), valorInicialCuenta, this.fechaInicialGeneracion, this.fechaFinalGeneracion, this.observacionesCuentaCobro, usuario.getCodigoInstitucionInt());
					
					//---Se guarda el valor de la cuenta de cobro insertada, se utiliza para mostrar el detalle
					//----de la generación de la cuenta de cobro por convenio -------------//
					if(c==0)
						cuentaCobroGenerada=resp+"";
					else
						cuentaCobroGenerada+=ConstantesBD.separadorSplit+resp;
					
					if (resp <0)
					{
						error=true;
						break;
					}
				}//for
			}//if !error
			
		}//if mapaCargues != null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return "";
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return cuentaCobroGenerada;
	}
	
	/**
	 * Método que inserta la cuenta de cobro de capitación
	 * @param con
	 * @param convenioCxC
	 * @param estadoCartera
	 * @param loginUsuario
	 * @param valorInicialCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param observaciones
	 * @param codigoInstitucionInt
	 * 
	 * @return EL NUMERO DE LA CUENTA DE COBRO INSERTADO
	 * 
	 */
	public int insertarCuentaCobroCapitacion(	Connection con,
												int consecutivoCxCCapitacion,
												int convenioCxC, 
												int estadoCartera, 
												String loginUsuario, 
												double valorInicialCuenta, 
												String fechaInicial, 
												String fechaFinal, 
												String observaciones, 
												int codigoInstitucion,
												String fechaElaboracion,
												String horaElaboracion,
												String centroAtencion)
	{
		return cuentaCobroCapitacionDao.insertarCuentaCobroCapitacion(con, consecutivoCxCCapitacion, convenioCxC, estadoCartera, loginUsuario, valorInicialCuenta, fechaInicial, fechaFinal, observaciones, codigoInstitucion, fechaElaboracion, horaElaboracion, centroAtencion);
	}

	/**
	 * Método que actualiza el contrato cargue con la cuenta de cobro y el valor total
	 * @param con
	 * @param codigoCargue
	 * @param consecutivoCxCCapitacion
	 * @param valorTotal
	 * @param institucion 
	 * @return
	 */
	public int actualizarContratoCargue(Connection con, int codigoCargue, int consecutivoCxCCapitacion, double valorTotal, int institucion)
	{
		return cuentaCobroCapitacionDao.actualizarContratoCargue (con, codigoCargue, consecutivoCxCCapitacion, valorTotal, institucion);
	}
	
	/**
	 * Método que modifica la cuenta de cobro y los cargues asociados a esta
	 * @param con
	 * @param usuario
	 * @param fechaInicialAnt
	 * @param fechaFinalAnt
	 * @param observacionesAnt
	 * @return
	 * @throws SQLException 
	 */
	public boolean modificarCuentaCobroCapitacion(Connection con, UsuarioBasico usuario, String fechaInicialAnt, String fechaFinalAnt, String observacionesAnt) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int resp=0;
				
		if (cuentaCobroCapitacionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CuentaCobroCapitacionDao - modificarCuentaCobroCapitacion )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.mapaCargues != null)
		{
			int numRegistros=Integer.parseInt(this.mapaCargues.get("numRegistros")+"");
			
			//-------------Se liberan todos los cargues mostrados en el listado---------------------------//
			if (cuentaCobroCapitacionDao.liberarCarguesCuentaCobro(con,  this.cuentaCobroModificar, usuario.getCodigoInstitucionInt(), this.fechaFinalModificar)>0)
			{
			for (int i=0; i<numRegistros; i++)
				{
				String seleccionado= (String)this.getMapaCargues("cargue_"+i);
				
				//--------Se verifica si fué seleccionado el cargue ----------//
				if (UtilidadTexto.getBoolean(seleccionado))
				{
					int tipoPago= Integer.parseInt(this.getMapaCargues("tipo_pago_"+i)+"");
					int codigoCargue= Integer.parseInt(this.getMapaCargues("codigo_"+i)+"");
										
					logger.info("\n seleccionado ->"+seleccionado+"\n");
					logger.info("\n codigoCargue ->"+codigoCargue+"\n");
					
					//--------Si el tipo de pago es UPC-------//
					if (tipoPago == Integer.parseInt(ConstantesBD.codigoTipoPagoUpc))
						{
						double valorTotal= Double.parseDouble(this.getMapaCargues("valor_total_"+i)+"");
													
						logger.info("\n valorTotal ->"+valorTotal+"\n");
						
						resp=cuentaCobroCapitacionDao.actualizarContratoCargue (con, codigoCargue, this.cuentaCobroModificar, valorTotal, usuario.getCodigoInstitucionInt());
						
							if (resp <0)
							{
								error=true;
								break;
							}
						}
					else if (tipoPago == Integer.parseInt(ConstantesBD.codigoTipoPagoGrupoEtareo))
					{
						resp=cuentaCobroCapitacionDao.actualizarCargueGrupoEtareo (con, codigoCargue, this.cuentaCobroModificar, usuario.getCodigoInstitucionInt());
						
						if (resp <0)
						{
							error=true;
							break;
						}
					}
					
					//------------------ Se calcula el valor inicial de la cuenta de cobro -------------------//
					double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, this.cuentaCobroModificar, usuario.getCodigoInstitucionInt());
					
					//------------Se actualiza la cuenta de cobro con los nuevos datos ----------------------------//
					resp=cuentaCobroCapitacionDao.actualizarCuentaCobroCapitacion (con, this.cuentaCobroModificar, usuario.getLoginUsuario(), valorInicialCuenta, this.fechaInicialModificar, this.fechaFinalModificar, this.observacionesModificar, usuario.getCodigoInstitucionInt());
					
					//----------------Se genera el log de modificación de la cuenta de cobro --------------------------------//
					//	-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
					StringBuffer log=new StringBuffer();
					log.append("\n===============MODIFICACIÓN DE LA CUENTA DE COBRO DE CAPITACIÓN================");
					log.append("\n NUMERO CUENTA COBRO :"+this.cuentaCobroModificar+"\n");
					
					if (!this.fechaInicialModificar.equals(fechaInicialAnt))
					{
						log.append("\n FECHA INICIAL ANTERIOR :"+fechaInicialAnt);
					    log.append("\n FECHA INICIAL NUEVA :"+this.fechaInicialModificar+"\n");
					}
					
					if (!this.fechaFinalModificar.equals(fechaFinalAnt))
					{
						log.append("\n FECHA FINAL ANTERIOR :"+fechaFinalAnt);
					    log.append("\n FECHA FINAL NUEVA :"+this.fechaFinalModificar+"\n");
					}
					
					if (!this.observacionesModificar.equals(observacionesAnt))
					{
						log.append("\n OBSERVACIONES ANTERIORES :"+observacionesAnt);
					    log.append("\n OBSERVACIONES NUEVAS :"+this.observacionesModificar+"\n");
					}
					
					log.append("\n VALOR INICIAL CUENTA :"+valorInicialCuenta);
					
					log.append("\n==================================================================");
					//-Generar el log 
					LogsAxioma.enviarLog(ConstantesBD.logCuentasCobroCapitacionCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
					
					if (resp <0)
					{
						error=true;
						break;
					}
					
				}//if cargue seleccionado
				}//for
			}//if resp>0
		}//if mapaCargues !=null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}
	
	/**
	 * Método que consulta la información de la cuenta de cobro de acuerdo
	 * al numero de la cuenta de cobro y a la institución
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoInstitucion
	 * @param estadoCartera 
	 * @return
	 */
	public HashMap consultarCuentaCobro(Connection con, int numeroCuentaCobro, int codigoInstitucion, int estadoCartera)
	{
		return cuentaCobroCapitacionDao.consultarCuentaCobro(con, numeroCuentaCobro, codigoInstitucion, estadoCartera);
	}
	
	/**
	 * Método que realiza la busqueda avanzada de las cuentas de cobro de acuerdo
	 * a los parámetros de búsqueda
	 * @param con
	 * @param cuentaCobro
	 * @param cuentaCobroFinal 
	 * @param convenio
	 * @param fechaElaboracion
	 * @param estadoCuentaCobro
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultaAvanzadaCuentaCobro(Connection con, String cuentaCobro, String cuentaCobroFinal, int convenio, String fechaElaboracion, int estadoCuentaCobro, String fechaInicial, String fechaFinal, int codigoInstitucion)
	{
		return cuentaCobroCapitacionDao.consultaAvanzadaCuentaCobro(con, cuentaCobro, cuentaCobroFinal,convenio, fechaElaboracion, estadoCuentaCobro, fechaInicial, fechaFinal, codigoInstitucion);
	}

	/**
	 * Método que realiza la anulación de la cuenta de cobro, modificando su estado y
	 * liberando los cargues asociados a ella (se ponen en null)
	 * @param con
	 * @param cuentaCobro
	 * @param codigoInstitucion
	 * @throws SQLException 
	 */
	public boolean anularCuentaCobro(Connection con, String cuentaCobro, String loginUsuario, int codigoInstitucion) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int resp=0;
				
		if (cuentaCobroCapitacionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CuentaCobroCapitacionDao - anularCuentaCobro )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		resp=cuentaCobroCapitacionDao.anularCuentaCobro (con, cuentaCobro, this.motivoAnulacion, loginUsuario, codigoInstitucion);
		
		if(resp<0)
			error=true;
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}
	
	/**
	 * Método que consulta los cargues que cumplen con los paràmetros en modificar
	 * @param con
	 * @param cuentaCobro
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap consultarCarguesModificar(Connection con, int cuentaCobro, String fechaFinal, int codigoConvenio)
	{
		return cuentaCobroCapitacionDao.consultarCarguesModificar(con, cuentaCobro, fechaFinal, codigoConvenio);
	}
	
	/**
	 * Método que consulta el listado de facturas asociadas a la 
	 * cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarFacturasCxC(Connection con, int cuentaCobro, int codigoInstitucion)
	{
		return cuentaCobroCapitacionDao.consultarFacturasCxC (con, cuentaCobro, codigoInstitucion);
	}
	
	/**
	 * Mètodo que realiza la búsqueda avanzada de las facturas
	 * @param con
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param viaIngreso
	 * @param codigoInstitucion
	 * @param cuentaCobroCapitacion 
	 * @return
	 */
	public HashMap consultaAvanzadaFacturasCxC(Connection con, String numeroFactura, String fechaFactura, int viaIngreso, int codigoInstitucion, String cuentaCobroCapitacion)
	{
		return cuentaCobroCapitacionDao.consultaAvanzadaFacturasCxC (con, numeroFactura, fechaFactura, viaIngreso, codigoInstitucion, cuentaCobroCapitacion);
	}
	
	/**
	 * metodo que actualiza los campos de total_ingresos y diferencia_cuenta de las cuentas de cobro capitadas
	 * @param con
	 * @param numeroCuentaCobro
	 * @param totalIngresos
	 * @return
	 */
	public boolean updateTotalIngresosDiferenciaCuenta(Connection con, String numeroCuentaCobro, String totalIngresos)
	{
		return cuentaCobroCapitacionDao.updateTotalIngresosDiferenciaCuenta(con, numeroCuentaCobro, totalIngresos);
	}
	
	/**
	 * Método que consulta los contratos_cargue asociados al listado
	 * de cuentas de cobro enviado por parámetro, separados por coma
	 * se utiliza en la impresión
	 * @param con
	 * @param listadoCuentasCobro
	 * @return
	 */
	public HashMap consultarDetalleCuentasCobroImpresion(Connection con, String listadoCuentasCobro)
	{
		return cuentaCobroCapitacionDao.consultarDetalleCuentasCobroImpresion (con, listadoCuentasCobro);
	}
	
	/**
	 * Método que consulta el detalle de los cargues de tipo grupo etáreo para mostrarse en la impresión
	 * @param con
	 * @param listadoContratosCargue
	 * @return
	 */
	public HashMap consultarDetalleGrupoEtareo(Connection con, String listadoContratosCargue)
	{
		return cuentaCobroCapitacionDao.consultarDetalleGrupoEtareo(con, listadoContratosCargue);
	}
	
	/**
	 * indica si existe o no un numero de cuenta de cobro dado x institucion
	 * @param numeroCxC
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existeNumeroCxC (String numeroCxC, int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean existe= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaCobroCapitacionDao().existeNumeroCxC(con, numeroCxC, codigoInstitucion);
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return existe;
	}
	
//	-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//
	
	/**
	 * @return Retorna the convenioCapitado.
	 */
	public int getConvenioCapitado()
	{
		return convenioCapitado;
	}

	/**
	 * @param convenioCapitado The convenioCapitado to set.
	 */
	public void setConvenioCapitado(int convenioCapitado)
	{
		this.convenioCapitado = convenioCapitado;
	}

	/**
	 * @return Retorna the fechaFinalGeneracion.
	 */
	public String getFechaFinalGeneracion()
	{
		return fechaFinalGeneracion;
	}

	/**
	 * @param fechaFinalGeneracion The fechaFinalGeneracion to set.
	 */
	public void setFechaFinalGeneracion(String fechaFinalGeneracion)
	{
		this.fechaFinalGeneracion = fechaFinalGeneracion;
	}

	/**
	 * @return Retorna the fechaInicialGeneracion.
	 */
	public String getFechaInicialGeneracion()
	{
		return fechaInicialGeneracion;
	}

	/**
	 * @param fechaInicialGeneracion The fechaInicialGeneracion to set.
	 */
	public void setFechaInicialGeneracion(String fechaInicialGeneracion)
	{
		this.fechaInicialGeneracion = fechaInicialGeneracion;
	}

	/**
	 * @return Retorna the mapaCargues.
	 */
	public HashMap getMapaCargues()
	{
		return mapaCargues;
	}

	/**
	 * @param mapaCargues The mapaCargues to set.
	 */
	public void setMapaCargues(HashMap mapaCargues)
	{
		this.mapaCargues = mapaCargues;
	}
	
	/**
	 * @return Retorna mapaCargues.
	 */
	public Object getMapaCargues(Object key) {
		return mapaCargues.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaCargues(Object key, Object dato) {
		this.mapaCargues.put(key, dato);
	}

	/**
	 * @return Retorna the observacionesCuentaCobro.
	 */
	public String getObservacionesCuentaCobro()
	{
		return observacionesCuentaCobro;
	}

	/**
	 * @param observacionesCuentaCobro The observacionesCuentaCobro to set.
	 */
	public void setObservacionesCuentaCobro(String observacionesCuentaCobro)
	{
		this.observacionesCuentaCobro = observacionesCuentaCobro;
	}

	/**
	 * @return Retorna the motivoAnulacion.
	 */
	public String getMotivoAnulacion()
	{
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion The motivoAnulacion to set.
	 */
	public void setMotivoAnulacion(String motivoAnulacion)
	{
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return Retorna the fechaFinalModificar.
	 */
	public String getFechaFinalModificar()
	{
		return fechaFinalModificar;
	}

	/**
	 * @param fechaFinalModificar The fechaFinalModificar to set.
	 */
	public void setFechaFinalModificar(String fechaFinalModificar)
	{
		this.fechaFinalModificar = fechaFinalModificar;
	}

	/**
	 * @return Retorna the fechaInicialModificar.
	 */
	public String getFechaInicialModificar()
	{
		return fechaInicialModificar;
	}

	/**
	 * @param fechaInicialModificar The fechaInicialModificar to set.
	 */
	public void setFechaInicialModificar(String fechaInicialModificar)
	{
		this.fechaInicialModificar = fechaInicialModificar;
	}

	/**
	 * @return Retorna the observacionesModificar.
	 */
	public String getObservacionesModificar()
	{
		return observacionesModificar;
	}

	/**
	 * @param observacionesModificar The observacionesModificar to set.
	 */
	public void setObservacionesModificar(String observacionesModificar)
	{
		this.observacionesModificar = observacionesModificar;
	}

	/**
	 * @return Retorna the cuentaCobroModificar.
	 */
	public int getCuentaCobroModificar()
	{
		return cuentaCobroModificar;
	}

	/**
	 * @param cuentaCobroModificar The cuentaCobroModificar to set.
	 */
	public void setCuentaCobroModificar(int cuentaCobroModificar)
	{
		this.cuentaCobroModificar = cuentaCobroModificar;
	}

	/**
	 * Consulta las cuentas de cobro dado un string con los codigo.
	 * @param con
	 * @param institucion
	 * @param cuentasCobro
	 * @return
	 */
	public HashMap consultarCuentasCobroConvenio(Connection con, int institucion, String cuentasCobro)
	{
		return cuentaCobroCapitacionDao.consultarCuentasCobroConvenio(con, institucion,cuentasCobro);
	}
	
	/**
	 * Metodo para hacer las posibles validaciones que puede tener el sistema.
	 * @param con
	 * @param valorConsecutivo
	 * @param codigoInstitucionInt
	 * @return
	 */
	private boolean esConsecutivoValido(String nombreConsecutivo,double valorConsecutivo, int institucion) 
	{
		//valida si el consecutivo ya se asigno a otra factura para evitar facturas repetidas.
		if(UtilidadesFacturacion.esConsecutvioAsignadoFactura(institucion,String.valueOf(new Double(valorConsecutivo).longValue())))
		{
			Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, nombreConsecutivo, institucion, valorConsecutivo+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			return false;
		}
		return true;

	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenioCapitado
	 * @return
	 */
	public HashMap consultarNumeroCargue(Connection con, String fechaFinal, String fechaInicial, int convenioCapitado) 
	{
		return cuentaCobroCapitacionDao.consultarNumeroCargue(con, fechaFinal, fechaInicial, convenioCapitado);
	}

}
