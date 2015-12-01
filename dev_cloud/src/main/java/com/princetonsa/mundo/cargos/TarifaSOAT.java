/*
 * @(#)TarifaSOAT.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TarifaSOATDao;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Clase para el manejo de una tarifa soat
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Raul@PrincetonSA.com">Raúl Cancino</a>
 */
public class TarifaSOAT 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarifaSOAT.class);
	
	/**
	 * Nombre relativo del archivo de logs para esta funcionalidad
	 */
	private String nombreArchivoLogs;
			
	/**
	 * Codigo de la tarifa SOAT
	 */
	private int codigo;
	
	/**
	 * Grupo de la tarifa SOAT
	 */
	private double grupo;
	
	/**
	 * Valor de la tarifa
	 */	
	private double valorTarifa;
	
	
	private String loginUsuario;
	
	/**
	 * Iva que se debe aplicar a la tarifa
	 */
	private double porcentajeIva;
	
	/**
	 * Esquema tarifario de la tarifa
	 */
	private EsquemaTarifario esquemaTarifario;
	
	/**
	 * Tipo liquidacion de la tarifa
	 */
	private InfoDatosInt tipoLiquidacion;
	
	/**
	 * Servicio asociado a la tarifa 
	 */
	private InfoDatosInt servicio;
	
	/**
	 * Especialidad que corresponde al servicio de esta tarifa
	 */
	private InfoDatosInt especialidad;
	
	/**
	 * Liquidar Asocios Define cuales son los Servicios que deben liquidarse ademas de la tarifa por un valor de asocios
	 * */
	private String liquidarAsocios;
	
	/**
	 * Liquidar Asocios Original
	 * */
	private String liquidarAsociosOriginal;
	
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */	
	private TarifaSOATDao tarifaSOATDao = null;
	
	/**
	 * 
	 */
	private InfoDatosInt tipoLiquidacionOriginal;
	
	/**
	 * 
	 */
	private double valorTarifaOriginal;
	
	/**
	 * 
	 */
	private double valorTarifaConversionMoneda;
	
	/**
	 * 
	 */
	private int index;
	
	/**
	 * Variable alterna para manejar el valor de la tarifa
	 * como String para mostrarla en forma exponencial
	 */
	private String valorTarifaString;
	
	/**
	 * Variable para almacenar la Fecha de Vigencia de la Tarifa
	 */
	private String fechaVigencia;
	
	/** 
	 * Creadora de la clase TarifaSOAT.java
	 */
	public TarifaSOAT()
	{
		this.codigo = 0;
		this.grupo = 0;
		this.valorTarifa = 0.0;
		this.porcentajeIva = 0.0;
		this.esquemaTarifario = new EsquemaTarifario();
		this.tipoLiquidacion = new InfoDatosInt();
		this.servicio = new InfoDatosInt();
		this.nombreArchivoLogs = "";
		this.especialidad = new InfoDatosInt();
		this.init(System.getProperty("TIPOBD"));
		this.loginUsuario="";
		this.valorTarifaOriginal= 0.0;
		this.tipoLiquidacionOriginal = new InfoDatosInt();
		this.valorTarifaConversionMoneda=0.0;
		this.index=ConstantesBD.codigoNuncaValido;
		this.liquidarAsocios = "";
		this.liquidarAsociosOriginal = "";
		this.valorTarifaString = "";
		this.fechaVigencia="";
	}
	
	/**
	 * 
	 * Creadora de la clase TarifaSOAT.java
	 * @param codigo. int, código de la tarifa soat
	 * @param grupo. int, grupo de la tarifa soat
	 * @param valorTarifa. double, valor de la tarifa soat
	 * @param porcentajeIva. pocentaje iva aplicable a la tarifa soat
	 * @param esquemaTarifario. EsquemaTarifario, esquema tarifario correspondiente a la tarifa soat
	 * @param tipoLiquidacion. InfoDatosInt, tipo de liquidación de la tarifa soat
	 * @param servicio. InfoDatosInt, servicio asociado a la tarifa soat
	 * @param nombreArchivoLogs. String, nombre del archivo para los logs de modificación
	 * @param Liquidar Asocios
	 */
	public TarifaSOAT(	int codigo,
									int grupo,
									double valorTarifa,
									double porcentajeIva,
									EsquemaTarifario esquemaTarifario,
									InfoDatosInt tipoLiquidacion,
									InfoDatosInt servicio,
									String liquidarAsocios,
									String nombreArchivoLogs	)
	{
		this.codigo = codigo;
		this.grupo = grupo;
		this.valorTarifa = valorTarifa;
		this.porcentajeIva = porcentajeIva;
		this.esquemaTarifario = esquemaTarifario;
		this.tipoLiquidacion = tipoLiquidacion;
		this.servicio = servicio;
		this.especialidad = new InfoDatosInt();
		this.liquidarAsocios = liquidarAsocios;
		this.nombreArchivoLogs = nombreArchivoLogs;
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
		if( tarifaSOATDao == null)
		{
			if(tipoBD==null)
			{
				logger.error("No esta llegando el tipo de base de datos");
				System.exit(1);
			}
			else
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				if (myFactory != null)
				{
					tarifaSOATDao = myFactory.getTarifaSOATDao();
				}					
			}
		}
	}
	
	
	
	/**
	 * Retorna el nombre relativo del archivo de logs para esta funcionalidad
	 * @return
	 */
	public String getNombreArchivoLogs()
	{
		return nombreArchivoLogs;
	}

	/**
	 * Asigna el nombre relativo del archivo de logs para esta funcionalidad
	 * @param nombreArchivoLogs
	 */
	public void setNombreArchivoLogs(String nombreArchivoLogs)
	{
		this.nombreArchivoLogs = nombreArchivoLogs;
	}

	/**
	 * Retorna el codigo de la tarifa SOAT
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el codigo de la tarifa SOAT
	 * @param i
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el grupo de la tarifa SOAT
	 * @return
	 */
	public double getGrupo()
	{
		return grupo;
	}

	/**
	 * Asigna el grupo de la tarifa SOAT
	 * @param grupo
	 */
	public void setGrupo(double grupo)
	{
		this.grupo = grupo;
	}

	/**
	 * Retorna el valor de la tarifa
	 * @return
	 */
	public double getValorTarifa()
	{
		return valorTarifa;
	}

	/**
	 * Asigna el valor de la tarifa
	 * @param valorTarifa
	 */
	public void setValorTarifa(double valorTarifa)
	{
		this.valorTarifa = valorTarifa;
	}

	/**
	 * Retorna el iva que se debe aplicar a la tarifa
	 * @return
	 */
	public double getPorcentajeIva()
	{
		return porcentajeIva;
	}

	/**
	 * Asigna el iva que se debe aplicar a la tarifa
	 * @param porcentajeIva
	 */
	public void setPorcentajeIva(double porcentajeIva)
	{
		this.porcentajeIva = porcentajeIva;
	}

	/**
	 * Retorna el esquema tarifario de la tarifa
	 * @return
	 */
	public EsquemaTarifario getEsquemaTarifario()
	{
		return esquemaTarifario;
	}

	/**
	 * Asigna el esquema tarifario de la tarifa
	 * @param tarifario
	 */
	public void setEsquemaTarifario(EsquemaTarifario tarifario)
	{
		esquemaTarifario = tarifario;
	}

	/**
	 * Retorna el código del esquema tarifario correspondiente a esta tarifa
	 * @return
	 */
	public int getCodigoEsquemaTarifario()
	{
		return esquemaTarifario.getCodigo();
	}

	/**
	 * Asigna el código del esquema tarifario correspondiente a esta tarifa
	 * @param codigoEsquemaTarifario
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario)
	{
		this.esquemaTarifario.setCodigo(codigoEsquemaTarifario);
	}
	
	/**
	 * Retorna el nombre del esquema tarifario correspondiente a esta tarifa
	 * @return
	 */
	public String getNombreEsquemaTarifario()
	{
		return this.esquemaTarifario.getNombre();
	}

	/**
	 * Asigna el nombre del esquema tarifario correspondiente a esta tarifa
	 * @param nombreEsquemaTarifario
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario)
	{
		this.esquemaTarifario.setNombre(nombreEsquemaTarifario);
	}
	
	/**
	 * Retorna el tipo liquidacion de la tarifa
	 * @return
	 */
	public InfoDatosInt getTipoLiquidacion()
	{
		return tipoLiquidacion;
	}

	/**
	 * Asigna el tipo liquidacion de la tarifa
	 * @param tipoLiquidacion
	 */
	public void setTipoLiquidacion(InfoDatosInt tipoLiquidacion)
	{
		this.tipoLiquidacion = tipoLiquidacion;
	}
	
	/**
	 * Retorna el código del tipo liquidacion de la tarifa
	 * @return
	 */
	public int getCodigoTipoLiquidacion()
	{
		return this.tipoLiquidacion.getCodigo();
	}

	/**
	 * Asigna el código del tipo liquidacion de la tarifa
	 * @param codigoTipoLiquidacion
	 */
	public void setCodigoTipoLiquidacion(int codigoTipoLiquidacion)
	{
		this.tipoLiquidacion.setCodigo(codigoTipoLiquidacion);
	}

	/**
	 * Retorna el nombre del tipo liquidacion de la tarifa
	 * @return
	 */
	public String getNombreTipoLiquidacion()
	{
		return this.tipoLiquidacion.getNombre();
	}

	/**
	 * Asigna el nombre del tipo liquidacion de la tarifa
	 * @param nombreTipoLiquidacion
	 */
	public void setNombreTipoLiquidacion(String nombreTipoLiquidacion)
	{
		this.tipoLiquidacion.setNombre(nombreTipoLiquidacion);
	}

	/**
	 * Retorna el servicio asociado a la tarifa
	 * @return
	 */
	public InfoDatosInt getServicio()
	{
		return servicio;
	}

	/**
	 * Asigna el servicio asociado a la tarifa
	 * @param servicio
	 */
	public void setServicio(InfoDatosInt servicio)
	{
		this.servicio = servicio;
	}

	/**
	 * Retorna el código del servicio que corresponde a esta tarifa
	 * @return
	 */
	public int getCodigoServicio()
	{
		return this.servicio.getCodigo();
	}

	/**
	 * Asigna el código del servicio que corresponde a esta tarifa
	 * @param codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio)
	{
		this.servicio.setCodigo(codigoServicio);
	}

	/**
	 * Retorna el nombre del servicio que corresponde a esta tarifa
	 * @return
	 */
	public String getNombreServicio()
	{
		return this.servicio.getNombre();
	}

	/**
	 * Asigna el nombre del servicio que corresponde a esta tarifa
	 * @param nombreServicio
	 */
	public void setNombreServicio(String nombreServicio)
	{
		this.servicio.setNombre(nombreServicio);
	}	

	/**
	 * Retorna la especialidad que corresponde al servicio de esta tarifa
	 * @return
	 */
	public InfoDatosInt getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * Asigna la especialidad que corresponde al servicio de esta tarifa
	 * @param especialidad
	 */
	public void setEspecialidad(InfoDatosInt especialidad)
	{
		this.especialidad = especialidad;
	}

	/**
	 * Retorna el código de la especialidad que corresponde al servicio de esta tarifa
	 * @return
	 */
	public int getCodigoEspecialidad()
	{
		return this.especialidad.getCodigo();
	}

	/**
	 * Asigna el código de la especialidad que corresponde al servicio de esta tarifa
	 * @param codigoEspecialidad
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad)
	{
		this.especialidad.setCodigo(codigoEspecialidad);
	}

	/**
	 * Retorna el nombre de la especialidad que corresponde al servicio de esta tarifa
	 * @return
	 */
	public String getNombreEspecialidad()
	{
		return this.especialidad.getNombre();
	}

	/**
	 * Asigna el nombre de la especialidad que corresponde al servicio de esta tarifa
	 * @param nombreEspecialidad
	 */
	public void setNombreEspecialidad(String nombreEspecialidad)
	{
		this.especialidad.setNombre(nombreEspecialidad);
	}	

	
	/**
	 * Inserta una tarifa soat validando que no esté previamente definida
	 * @param con. Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean insertarTarifa( Connection con ) throws Exception
	{
		ResultadoBoolean resultado = this.tarifaSOATDao.existeTarifaDefinida(con, this.getCodigoEsquemaTarifario(), this.getCodigoServicio());
		if( !resultado.isTrue() && !UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
		{
			// Por ahora no se usa la transaccional porque no hay mas que una inserción
			// En el momento que se necesite una transaccion se debe cambiar aca y con
			// eso no se afecta nada con los cambios internos del método.
			resultado = this.insertar(con);
			if( resultado.isTrue() )
			{
				return new ResultadoBoolean(true);
			}
			else
			if( !resultado.isTrue() && !UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
			{
				return new ResultadoBoolean(false, "errors.problemasBd");
			}
			else
			{
				return new ResultadoBoolean(false, "error.tarifa.ingreso");
			}				
		}
		else
		if( !resultado.isTrue() && !UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
		{
			return new ResultadoBoolean(false, "errors.problemasBd");
		}
		else
		{
			return new ResultadoBoolean(false, "error.tarifa.definida");
		}
	}
	
	

	/**
	 * Modifica una tarifa dado el codigo
	 * @param con
	 * @return
	 */
	public ResultadoBoolean modificarTarifa(	Connection con, TarifaSOAT original	)
	{
		ResultadoBoolean resultado;
		if( original == null || original.getCodigoEsquemaTarifario() != this.getCodigoEsquemaTarifario() || original.getCodigoServicio() != this.getCodigoServicio()   )
		{
			resultado = this.tarifaSOATDao.existeTarifaDefinida(con, this.getCodigoEsquemaTarifario(), this.getCodigoServicio());		 
		}		
		else
		{
			resultado = new ResultadoBoolean(false);	
		}
		
		if( !resultado.isTrue() && !UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
		{
			// Por ahora no se usa la transaccional porque no hay mas que una inserción
			// En el momento que se necesite una transaccion se debe cambiar aca y con
			// eso no se afecta nada con los cambios internos del método.
		
			this.liquidarAsociosOriginal = original.getLiquidarAsocios();		
			resultado = this.modificar(con);
			if( resultado.isTrue() )
			{
				return new ResultadoBoolean(true);
			}
			else
			if( !resultado.isTrue() && !UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
			{
				return new ResultadoBoolean(false, "errors.problemasBd");
			}
			else
			{
				return new ResultadoBoolean(false, "error.tarifa.modificacion");
			}
		}
		else
		if( !resultado.isTrue() && !UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
		{
			return new ResultadoBoolean(false, "errors.problemasBd");
		}
		else
		{
			return new ResultadoBoolean(false, "error.tarifa.definida");
		}	
	}
	
	/**
	 * Elimina una tarifa dado el codigo
	 * @param con
	 * @return
	 */
	public ResultadoBoolean eliminarTarifa(	Connection con	)
	{
		// Por ahora no se usa la transaccional porque no hay mas que una inserción
		// En el momento que se necesite una transaccion se debe cambiar aca y con
		// eso no se afecta nada con los cambios internos del método.
		ResultadoBoolean resultado = this.eliminar(con);
		
		if( resultado.isTrue() )
		{
			return new ResultadoBoolean(true);
		}
		else
		if( !resultado.isTrue() && !UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
		{
			return new ResultadoBoolean(false, "errors.problemasBd");
		}
		else
		{
			return new ResultadoBoolean(false, "error.tarifa.eliminacion");
		}				
	}
	
	/**
	 * Consulta y carga la tarifa por esquema tarifario y servicio
	 * @param con. Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean consultarTarifa(Connection con, int codigoEsquemaTarifario, int codigoServicio)
	{
		this.setCodigoEsquemaTarifario(codigoEsquemaTarifario);
		this.setCodigoServicio(codigoServicio);

		ResultadoCollectionDB resultadoBusqueda = this.consultarPorEsquemaTarifarioYServicio(con);
		
		return this.cargarDatosBusqueda(resultadoBusqueda);		
	}
	
	/**
	 * Consulta y carga la tarifa por codigo
	 * @param con. Connection, conexión abierta con la fuente de datos
	 * @param codigo. int, código de la tarifa
	 * @return ResultadoBoolean, true si la consulta fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean consultarTarifa(Connection con, int codigo)
	{
		ResultadoCollectionDB resultadoBusqueda = this.tarifaSOATDao.consultar(con, codigo);
		
		return this.cargarDatosBusqueda(resultadoBusqueda);		
	}
	
	/**
	 * Carga en el objeto los datos que se encontraron en la busqueda
	 * @param resultadoBusqueda
	 * @return
	 */
	private ResultadoBoolean cargarDatosBusqueda(ResultadoCollectionDB resultadoBusqueda)
	{
		if( resultadoBusqueda != null )
		{
			if( !resultadoBusqueda.isTrue() )
			{
				return new ResultadoBoolean(false, "error.tarifa.vacia");
			}
			else
			{
				LinkedList tarifas = (LinkedList)resultadoBusqueda.getFilasRespuesta();				
				
				HashMap tarifaTemp = (HashMap)tarifas.get(0);		// Solo deberia haber una fila de respuesta

				this.setCodigo(((Integer)tarifaTemp.get("codigo")).intValue());
				this.setCodigoEsquemaTarifario(((Integer)tarifaTemp.get("codigoesquematarifario")).intValue());
				this.setNombreEsquemaTarifario((String)tarifaTemp.get("nombreesquematarifario"));
				this.setGrupo(((Integer)tarifaTemp.get("grupo")).intValue());
				this.setValorTarifa(((Double)tarifaTemp.get("valortarifa")).doubleValue());
				this.setPorcentajeIva(((Double)tarifaTemp.get("porcentajeiva")).doubleValue());			
				this.setCodigoServicio(((Integer)tarifaTemp.get("codigoservicio")).intValue());
				this.setNombreServicio((String)tarifaTemp.get("nombreservicio"));
				this.setCodigoEspecialidad(((Integer)tarifaTemp.get("codigoespecialidad")).intValue());
				this.setNombreEspecialidad((String)tarifaTemp.get("nombreespecialidad"));	
				this.setLiquidarAsocios((String)tarifaTemp.get("liqasocios"));
				
				if( tarifaTemp.get("codigotipoliquidacion") != null )
				{
					this.setCodigoTipoLiquidacion(((Integer)tarifaTemp.get("codigotipoliquidacion")).intValue());
					this.setNombreTipoLiquidacion((String)tarifaTemp.get("nombretipoliquidacion"));
				}
				else
				{
					this.setCodigoTipoLiquidacion(-1);
					this.setNombreTipoLiquidacion("");
				}			
				
				return new ResultadoBoolean(true);
			}
		}
		else
		{
			return new ResultadoBoolean(false, "errors.problemasBd");
		}
	}

	/**
	 * Inserta una tarifa soat
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#insertar(java.sql.Connection, int, int, int, int, double, double) 
	 */
	public ResultadoBoolean insertar(	Connection con	) throws Exception
	{
		return this.tarifaSOATDao.insertar(	con,				esquemaTarifario.getCodigo(),
																servicio.getCodigo(),
																grupo,
																tipoLiquidacion.getCodigo(),
																valorTarifa,
	 															loginUsuario,
																porcentajeIva,
																tipoLiquidacionOriginal.getCodigo(),
																valorTarifaOriginal,
																liquidarAsocios,
																liquidarAsociosOriginal,
																fechaVigencia);
	}

	/**
	 * Inserta una tarifa soat dentro de una transacción dado el estado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#insertarTransaccional(java.sql.Connection, int, int, int, int, double, double, java.lang.String)
	 */
	public ResultadoBoolean insertarTransacciona(	Connection con,
																					String estado) throws Exception
	{
		return this.tarifaSOATDao.insertarTransaccional(	con,
																						esquemaTarifario.getCodigo(),
																						servicio.getCodigo(),
																						grupo,
																						tipoLiquidacion.getCodigo(),
																						valorTarifa,
																						loginUsuario,
																						porcentajeIva,
																						tipoLiquidacionOriginal.getCodigo(),
																						valorTarifaOriginal,
																						liquidarAsocios,
																						liquidarAsociosOriginal,
																						estado,
																						fechaVigencia) ;
	}

	/**
	 * Modifica una tarifa soat dado su código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#modificar(java.sql.Connection, int, int, int, int, int, double, double)
	 */
	public ResultadoBoolean modificar(	Connection con	)
	{
		return this.tarifaSOATDao.modificar(	con,
																	codigo,
																	esquemaTarifario.getCodigo(),
																	servicio.getCodigo(),
																	grupo,
																	tipoLiquidacion.getCodigo(),
																	valorTarifa,
																	loginUsuario,
																	porcentajeIva,
																	tipoLiquidacionOriginal.getCodigo(),
																	valorTarifaOriginal,
																	liquidarAsocios,
																	liquidarAsociosOriginal,
																	fechaVigencia);
	}

	/**
	 * Modifica una tarifa soat dado su código dentro de una transacción dado el estado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#modificarTransaccional(java.sql.Connection, int, int, int, int, int, double, double, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con,
																						String estado) throws Exception
	{
		return this.tarifaSOATDao.modificarTransaccional(	con,
																							codigo,
																							esquemaTarifario.getCodigo(),
																							servicio.getCodigo(),
																							grupo,
																							tipoLiquidacion.getCodigo(),
																							valorTarifa,
																							loginUsuario,
																							porcentajeIva,
																							tipoLiquidacionOriginal.getCodigo(),
																							valorTarifaOriginal,
																							estado,
																							liquidarAsocios,
																							liquidarAsociosOriginal,
																							fechaVigencia);
	}

	/**
	 * Elimina una tarifa dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con)
	{
	    logger.info("codigo en eliminar->"+codigo);
		return this.tarifaSOATDao.eliminar(con, codigo);
	}

	/**
	 * Consulta una tarifa SOAT dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con)
	{
		return this.tarifaSOATDao.consultar(con, codigo);
	}

	/**
	 * Consulta una tarifa soat dado el código del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaSOATDao#consultar(java.sql.Connection, int, int)
	 */
	private ResultadoCollectionDB consultarPorEsquemaTarifarioYServicio(Connection con	)
	{
		return this.tarifaSOATDao.consultar(con, this.getCodigoEsquemaTarifario(), this.getCodigoServicio());
	}	

	/**
	 * Genera una cadena con los datos de la tarifa
	 * @return
	 */
	public String getCadenaLogTarifa(  )
	{
		String logTarifa = new String();
		
		this.setNombreEsquemaTarifario("Tarifa Servicio SOAT");
		
		logTarifa = "\tEsquema Tarifario ["+this.getCodigoEsquemaTarifario()+"-"+this.getNombreEsquemaTarifario()+"]";
		logTarifa += "\n\tServicio";
		logTarifa += "\n\t\tEspecialidad ["+this.getCodigoEspecialidad()+"-"+this.getNombreEspecialidad()+"]";
		logTarifa += "\n\t\tProcedimiento ["+this.getCodigoServicio()+"-"+this.getNombreServicio()+"]";
		
	
	    if(this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades)
	         this.setNombreTipoLiquidacion("Liquidación por unidades");
	    else if(this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatValor)
	        this.setNombreTipoLiquidacion("Liquidación por valor");
	    else
	    	this.setNombreTipoLiquidacion("Liquidación por grupo");
	   
		logTarifa += "\n\tTipo Liquidación ["+this.getCodigoTipoLiquidacion()+"-"+this.getNombreTipoLiquidacion()+"]";
		if( this.getValorTarifa() != 0.0 )
			logTarifa += "\n\tValor Tarifa ["+this.getValorTarifa()+"]";
		
		if(this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades)
			logTarifa += "\n\tUnidades ["+this.getGrupo()+"]";
		if(this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUvr)
			logTarifa += "\n\tGrupo ["+Utilidades.convertirAEntero(this.getGrupo()+"")+"]";
		
		logTarifa += "\n\t\tLiquidacion Asocio ["+this.liquidarAsocios+"]";
	
		
		logTarifa += "\n\tPorcentaje IVA ["+this.getPorcentajeIva()+"]";
		
		return logTarifa;
	}
	
	/**
	 * Método para cargar una tarifa de SOAT dado el código del servicio
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param servicio Código del servicio 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return true si se cargó correctamente
	 */
	public boolean cargar(Connection con, int servicio, int esquemaTarifario, String fechaVigenciaOPCIONAL)
	{
	    this.servicio.setCodigo(servicio);
		this.esquemaTarifario.setCodigo(esquemaTarifario);
		this.fechaVigencia= fechaVigenciaOPCIONAL;
		return this.cargar(con);
	}
	
	/**
	 * Metodo que consulta todas las tarifas soat por esquema servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param codServicio
	 * @return
	 */
	public HashMap consultarTodasTarifas(Connection con, int esquemaTarifario, String codServicio)
	{
		return this.tarifaSOATDao.consultarTodasTarifas(con, esquemaTarifario, codServicio);
	}
	
	/**
	 * Método para cargar una tarifa de SOAT tomando el código del servicio
	 * y del esquema tarifario de los atributos propios
	 * @param con Conexión con la BD
	 * @return true si se cargó correctamente
	 */
	private boolean cargar(Connection con)
	{
	    if(this.servicio.getCodigo()!=0 && this.esquemaTarifario.getCodigo()!=0)
		{
			Iterator iterador=tarifaSOATDao.cargar(con, this.servicio.getCodigo(), this.esquemaTarifario.getCodigo(), this.fechaVigencia).iterator();
			if(iterador.hasNext())
			{
				HashMap tarifaSOAT=(HashMap)iterador.next();
				this.codigo=Utilidades.convertirAEntero(tarifaSOAT.get("codigo")+"");
				if(tarifaSOAT.get("valor")!=null&&!UtilidadTexto.isEmpty(tarifaSOAT.get("valor").toString()))
				{	
					this.valorTarifaString = UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tarifaSOAT.get("valor")+""));
					this.valorTarifa = Double.parseDouble(tarifaSOAT.get("valor")+"");
					this.valorTarifaOriginal = Double.parseDouble(tarifaSOAT.get("valor")+"");
				}	
				logger.info("antes del iva");
				this.porcentajeIva = Float.parseFloat(tarifaSOAT.get("iva")+""); 
				String liquidacion;
				liquidacion = tarifaSOAT.get("tipo_liquidacion")+"";
				if(liquidacion!=null && !liquidacion.equals("null"))
				{
				    this.tipoLiquidacion.setCodigo(Utilidades.convertirAEntero(tarifaSOAT.get("tipo_liquidacion")+""));
				    this.tipoLiquidacion.setNombre(tarifaSOAT.get("nom_tipo_liquidacion")+"");
				    this.tipoLiquidacionOriginal.setCodigo(Utilidades.convertirAEntero(tarifaSOAT.get("tipo_liquidacion")+""));
				    this.tipoLiquidacionOriginal.setNombre(tarifaSOAT.get("nom_tipo_liquidacion")+"");
				}
				if(tarifaSOAT.get("grupo")!=null&&!UtilidadTexto.isEmpty(tarifaSOAT.get("grupo").toString()))
				{
					this.grupo=Double.parseDouble(tarifaSOAT.get("grupo")+"");
					this.valorTarifaString = UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tarifaSOAT.get("grupo")+""));
				}
				this.servicio.setNombre(tarifaSOAT.get("nombre_servicio")+"");
				this.especialidad.setNombre(tarifaSOAT.get("nombre_especialidad")+"");
				this.especialidad.setCodigo(Utilidades.convertirAEntero(tarifaSOAT.get("codigo_especialidad")+""));
				this.liquidarAsocios = tarifaSOAT.get("liq_asocios")+"";
				
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}
	
	
	/**
	 * 
	 * @param loginUsuario
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public InfoDatosInt getTipoLiquidacionOriginal() {
		return tipoLiquidacionOriginal;
	}
	
	
	/**
	 * 
	 * @param tipoLiquidacionOriginal
	 */
	public void setTipoLiquidacionOriginal(InfoDatosInt tipoLiquidacionOriginal) {
		this.tipoLiquidacionOriginal = tipoLiquidacionOriginal;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public double getValorTarifaOriginal() {
		return valorTarifaOriginal;
	}
	
	
	/**
	 * 
	 * @param valorTarifaOriginal
	 */
	public void setValorTarifaOriginal(double valorTarifaOriginal) {
		this.valorTarifaOriginal = valorTarifaOriginal;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the valorTarifaConversionMoneda
	 */
	public double getValorTarifaConversionMoneda() {
		return valorTarifaConversionMoneda;
	}

	/**
	 * @param valorTarifaConversionMoneda the valorTarifaConversionMoneda to set
	 */
	public void setValorTarifaConversionMoneda(double valorTarifaConversionMoneda) {
		this.valorTarifaConversionMoneda = valorTarifaConversionMoneda;
	}

	/**
	 * @return the liquidarAsocios
	 */
	public String getLiquidarAsocios() {
		return liquidarAsocios;
	}

	/**
	 * @param liquidarAsocios the liquidarAsocios to set
	 */
	public void setLiquidarAsocios(String liquidarAsocios) {
		this.liquidarAsocios = liquidarAsocios;
	}

	/**
	 * @return the liquidarAsociosOriginal
	 */
	public String getLiquidarAsociosOriginal() {
		return liquidarAsociosOriginal;
	}

	/**
	 * @param liquidarAsociosOriginal the liquidarAsociosOriginal to set
	 */
	public void setLiquidarAsociosOriginal(String liquidarAsociosOriginal) {
		this.liquidarAsociosOriginal = liquidarAsociosOriginal;
	}

	/**
	 * @return the valorTarifaString
	 */
	public String getValorTarifaString() {
		return valorTarifaString;
	}

	/**
	 * @param valorTarifaString the valorTarifaString to set
	 */
	public void setValorTarifaString(String valorTarifaString) {
		this.valorTarifaString = valorTarifaString;
	}

	/**
	 * @return the fecha_vigencia
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * @param fecha_vigencia the fecha_vigencia to set
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	
}
