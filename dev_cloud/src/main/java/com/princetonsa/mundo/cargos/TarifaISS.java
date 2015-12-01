/*
 * @(#)TarifaISS.java
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
import com.princetonsa.dao.TarifaISSDao;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Clase para el manejo de una tarifa iss
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TarifaISS
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarifaISS.class);
	
	/**
	 * Código de la tarifa en el sistema
	 */
	private int codigo;
	
	/**
	 * Esquema tarifario correspondiente a esta tarifa
	 */
	private EsquemaTarifario esquemaTarifario;
	
	/**
	 * Servicio que corresponde a esta tarifa
	 */
	private InfoDatosInt servicio;
	
	/**
	 * Especialidad que corresponde al servicio de esta tarifa
	 */
	private InfoDatosInt especialidad;
	
	/**
	 * Valor de la tarifa del servicio que corresponde a esta tarifa
	 */
	private double valorTarifa;
	
	private String loginUsuario;
	
	/**
	 * Porcentaje del iva aplicable a el servicio que corresponde a esta tarifa
	 */
	private double porcentajeIva;
	
	/**
	 * Cód - nombre del tipo de liquidación
	 */
	private InfoDatosInt tipoLiquidacion;

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */	
	private TarifaISSDao tarifaISSDao = null;
	
	/**
	 * Unidades UVR
	 */
	private double unidades;
	
	/**
	 * String liquidarAsocios
	 * */
	private String liquidarAsocios;
	
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
	 * */
	private String liquidarAsociosOriginal; 
	
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
	 * Creadora de la clase
	 */	
	public TarifaISS()
	{
		this.codigo = 0;
		this.valorTarifa = 0.0;
		this.valorTarifaOriginal = 0.0;
		this.porcentajeIva = 0.0;
		this.tipoLiquidacion=new InfoDatosInt();
		this.tipoLiquidacionOriginal=new InfoDatosInt();
		this.esquemaTarifario = new EsquemaTarifario();
		this.servicio = new InfoDatosInt();
		this.especialidad = new InfoDatosInt();
		this.init(System.getProperty("TIPOBD"));	
		this.unidades = 0.0;
		this.loginUsuario="";
		this.valorTarifaConversionMoneda= 0.0;
		this.index=ConstantesBD.codigoNuncaValido;
		this.liquidarAsocios = "";
		this.liquidarAsociosOriginal = "";
		this.valorTarifaString = "";
		this.fechaVigencia="";
	}
	
	/** 
	 * Creadora de la clase TarifaISS.java
	 * @param codigo. int, codigo de la tarifa en el sistema
	 * @param valorTarifa. float, valor en pesos de la tarifa
	 * @param porcentajeIva. float, porcentaje del iva
	 * @param esquemaTarifario. EsquemaTarifario, esquema tarifario
	 * @param servicio. InfoDatosInt, codigo-nombre del servicio al que corresponde esta tarifa
	 */
	public TarifaISS(	int codigo, 
								int valorTarifa, 
								double porcentajeIva, 
								EsquemaTarifario esquemaTarifario, 
								InfoDatosInt servicio,
								InfoDatosInt tipoLiquidacion,
								String liquidarAsocios)
	{
		this.codigo = codigo;
		this.valorTarifa = valorTarifa;
		this.porcentajeIva = porcentajeIva;
		this.esquemaTarifario = esquemaTarifario;
		this.servicio = servicio;
		this.especialidad = new InfoDatosInt();
		this.tipoLiquidacion= tipoLiquidacion;
		this.liquidarAsocios = liquidarAsocios;
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
		if( tarifaISSDao == null)
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
					tarifaISSDao = myFactory.getTarifaISSDao();
				}					
			}
		}
	}
	
	
	/**
	 * Retorna el código de la tarifa en el sistema
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el código de la tarifa en el sistema
	 * @param codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna el esquema tarifario correspondiente a esta tarifa
	 * @return
	 */
	public EsquemaTarifario getEsquemaTarifario()
	{
		return esquemaTarifario;
	}

	/**
	 * Asigna el esquema tarifario correspondiente a esta tarifa
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
	 * Retorna el servicio que corresponde a esta tarifa
	 * @return
	 */
	public InfoDatosInt getServicio()
	{
		return servicio;
	}

	/**
	 * Asigna el servicio que corresponde a esta tarifa
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
	 * Retorna el valor de la tarifa del servicio que corresponde a esta tarifa
	 * @return
	 */
	public double getValorTarifa()
	{
		return valorTarifa;
	}

	/**
	 * Asigna el valor de la tarifa del servicio que corresponde a esta tarifa
	 * @param d
	 */
	public void setValorTarifa(double valorTarifa)
	{
		this.valorTarifa = valorTarifa;
	}

	/**
	 * Retorna el porcentaje del iva aplicable a el servicio que corresponde a esta tarifa
	 * @return
	 */
	public double getPorcentajeIva()
	{
		return porcentajeIva;
	}

	/**
	 * Asigna el porcentaje del iva aplicable a el servicio que corresponde a esta tarifa
	 * @param porcentajeIva
	 */
	public void setPorcentajeIva(double porcentajeIva)
	{
		this.porcentajeIva = porcentajeIva;
	}

	/**
	 * Inserta una tarifa iss validando que no esté previamente definida
	 * @param con. Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean insertarTarifa( Connection con )
	{
		ResultadoBoolean resultado = this.tarifaISSDao.existeTarifaDefinida(con, this.getCodigoEsquemaTarifario(), this.getCodigoServicio());
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
	public ResultadoBoolean modificarTarifa(	Connection con, TarifaISS original	)
	{
		ResultadoBoolean resultado;
						
		if( original == null || original.getCodigoEsquemaTarifario() != this.getCodigoEsquemaTarifario() || original.getCodigoServicio() != this.getCodigoServicio() )
		{
			resultado = this.tarifaISSDao.existeTarifaDefinida(con, this.getCodigoEsquemaTarifario(), this.getCodigoServicio());		 
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
		ResultadoCollectionDB resultadoBusqueda = this.tarifaISSDao.consultar(con, codigo);
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
				if(tarifaTemp.get("valortarifa")!=null)
					this.setValorTarifa(((Double)tarifaTemp.get("valortarifa")).doubleValue());
				this.setPorcentajeIva(((Double)tarifaTemp.get("porcentajeiva")).doubleValue());			
				this.setCodigoServicio(((Integer)tarifaTemp.get("codigoservicio")).intValue());
				this.setNombreServicio((String)tarifaTemp.get("nombreservicio"));	
				this.setCodigoEspecialidad(((Integer)tarifaTemp.get("codigoespecialidad")).intValue());
				this.setNombreEspecialidad((String)tarifaTemp.get("nombreespecialidad"));
				this.setLiquidarAsocios((String)tarifaTemp.get("liqasocios"));
				if(tarifaTemp.get("unidades")!=null)
					this.setUnidades(Double.parseDouble(tarifaTemp.get("unidades")+""));
				return new ResultadoBoolean(true);
			}
		}
		else
		{
			return new ResultadoBoolean(false, "errors.problemasBd");
		}
	}


	
	/**
	 * Inserta una tarifa iss
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean insertar(	Connection con	)
	{
		return this.tarifaISSDao.insertar(	con, 
															this.esquemaTarifario.getCodigo(),
															this.servicio.getCodigo(),
															Utilidades.convertirADouble(valorTarifaString),
															loginUsuario,
															porcentajeIva,
															this.tipoLiquidacion.getCodigo(),
															this.unidades,
															this.liquidarAsocios,
															this.tipoLiquidacionOriginal.getCodigo(),
															this.valorTarifaOriginal,
															this.liquidarAsociosOriginal,
															this.fechaVigencia);
	}

	/*	----------------------Metodo que no se utiliza----------------------
	/**
	 * Inserta una tarifa iss dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	/*private ResultadoBoolean insertarTransaccional(	Connection con,
																					String estado	)
	{
		return this.tarifaISSDao.insertarTransaccional(	con,
																				this.esquemaTarifario.getCodigo(),
																				this.servicio.getCodigo(),
																				valorTarifa,
																				porcentajeIva,
																				estado);		
	}*/
															
	/**
	 * Modifica una tarifa iss dado su código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean modificar(	Connection con	)
	{
		return this.tarifaISSDao.modificar(	con,
																codigo,
																this.esquemaTarifario.getCodigo(),
																this.servicio.getCodigo(),
																Utilidades.convertirADouble(valorTarifaString),
																loginUsuario,
																porcentajeIva,
																this.tipoLiquidacion.getCodigo(),
																this.unidades,																
																this.tipoLiquidacionOriginal.getCodigo(),
																this.valorTarifaOriginal,
																this.liquidarAsocios,
																this.liquidarAsociosOriginal,
																this.fechaVigencia);	
	}
	
	/*	----------------------Metodo que no se utiliza----------------------
	/**
	 * Modifica una tarifa iss dado su código dentro de una transacción dado su estado.
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	/*
	private ResultadoBoolean modificarTransaccional(	Connection con,
																						String estado	)
	{
		return this.tarifaISSDao.modificarTransaccional(	con,
																					codigo,
																					this.esquemaTarifario.getCodigo(),
																					this.servicio.getCodigo(),
																					valorTarifa,
																					porcentajeIva,
																					estado);		
	}*/
	
	/**
	 * Elimina una tarifa dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	private ResultadoBoolean eliminar(	Connection con	)
	{
	    return this.tarifaISSDao.eliminar(con, codigo);		
	}

	/*
	/**
	 * Consulta una tarifa iss dado el código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 */
	/*	----------------------Metodo que no se utiliza----------------------
	private ResultadoCollectionDB consultarPorCodigo(	Connection con	)
	{
		return this.tarifaISSDao.consultar(con, codigo);
	}*/
	
	/**
	 * Consulta una tarifa iss dado el código del esquema tarifario y el codigo del servicio
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoEsquemaTarifario. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.TarifaISSDao#consultar(java.sql.Connection, int, int)
	 */
	private ResultadoCollectionDB consultarPorEsquemaTarifarioYServicio(Connection con	)
	{
		return this.tarifaISSDao.consultar(con, this.getCodigoEsquemaTarifario(), this.getCodigoServicio());
	}	
	
	/**
	 * Genera una cadena con los datos de la tarifa
	 * @return
	 */
	public String getCadenaLogTarifa(  )
	{
		String logTarifa = new String();
		
		if(this.getCodigoEsquemaTarifario()==10)
		     this.setNombreEsquemaTarifario("Tarifa Servicio ISS");
		else
		     this.setNombreEsquemaTarifario("Tarifa Servicio Particular");
		
		logTarifa = "\tEsquema Tarifario ["+this.getCodigoEsquemaTarifario()+"-"+this.getNombreEsquemaTarifario()+"]";
		logTarifa += "\n\tServicio";
		logTarifa += "\n\t\tEspecialidad ["+this.getCodigoEspecialidad()+"-"+this.getNombreEspecialidad()+"]";
		logTarifa += "\n\t\tProcedimiento ["+this.getCodigoServicio()+"-"+this.getNombreServicio()+"]";
		
		if(this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades)
	         this.setNombreTipoLiquidacion("Liquidación por unidades");
	    else if(this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatValor)
	        this.setNombreTipoLiquidacion("Liquidación por valor");
	    else
	    	this.setNombreTipoLiquidacion("Liquidación por UVR");
		
		logTarifa += "\n\tTipo Liquidación ["+this.getCodigoTipoLiquidacion()+"-"+this.getNombreTipoLiquidacion()+"]";
				
		if( this.getValorTarifa() != 0.0 )
			logTarifa += "\n\tValor Tarifa ["+this.getValorTarifa()+"]";
		
		if(this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades||
		   this.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUvr)
			logTarifa += "\n\tUnidades ["+this.getUnidades()+"]";
		
		logTarifa += "\n\tPorcentaje IVA ["+this.getPorcentajeIva()+"]";
		logTarifa += "\n\tLiquidacion Tarifas ["+this.getLiquidarAsocios()+"]";
		
		return logTarifa;
	}

	/**
	 * Retorna el código del tipo de liquidación
	 * @return
	 */
	public int getCodigoTipoLiquidacion()
	{
		return this.tipoLiquidacion.getCodigo();
	}

	/**
	 * Asigna el código del tipo de liquidacion
	 * @param codigoTipoLiquidacion
	 */
	public void setCodigoTipoLiquidacion(int codigoTipoLiquidacion)
	{
		this.tipoLiquidacion.setCodigo(codigoTipoLiquidacion);
	}

	/**
	 * Retorna el nombre del tipo de liquidacion 
	 * @return
	 */
	public String getNombreTipoLiquidacion()
	{
		return this.tipoLiquidacion.getNombre();
	}

	/**
	 * Asigna el nombre del tipo de Liquidacion
	 * @param nombreTipoLiquidacion
	 */
	public void setNombreTipoLiquidacion(String nombreTipoLiquidacion)
	{
		this.tipoLiquidacion.setNombre(nombreTipoLiquidacion);
	}	
	
	/**
	 * Método para cargar una tarifa de ISS dado el código del servicio
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
	 * Método para cargar una tarifa de ISS tomando el código del servicio
	 * y del esquema tarifario de los atributos propios
	 * @param con Conexión con la BD
	 * @return true si se cargó correctamente
	 */
	private boolean cargar(Connection con)
	{
		if(this.servicio.getCodigo()!=0 && this.esquemaTarifario.getCodigo()!=0)
		{
			Iterator iterador=tarifaISSDao.cargar(con, this.servicio.getCodigo(), this.esquemaTarifario.getCodigo(), this.fechaVigencia).iterator();
			if(iterador.hasNext())
			{
				HashMap tarifaISS=(HashMap)iterador.next();
				this.codigo=Integer.parseInt(tarifaISS.get("codigo")+"");
				
				logger.info("===>Valor HashMap: "+tarifaISS.get("valor"));
				
				if(!UtilidadTexto.isEmpty(tarifaISS.get("valor")+""))
				{
					this.valorTarifaString = UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tarifaISS.get("valor")+""));
					logger.info("===>Valor Tarifa String: "+this.valorTarifaString);

					this.valorTarifa = Double.parseDouble(tarifaISS.get("valor")+"");
					logger.info("===>Valor Tarifa: "+this.valorTarifa);
					
					this.valorTarifaOriginal = Double.parseDouble(tarifaISS.get("valor")+"");
					logger.info("===>Valor Tarifa Original: "+this.valorTarifaOriginal);
				}
				this.porcentajeIva=Utilidades.convertirADouble(tarifaISS.get("iva")+"",true);
				this.tipoLiquidacion.setCodigo(Integer.parseInt(tarifaISS.get("tipo_liquidacion")+""));
				this.tipoLiquidacionOriginal.setCodigo(Integer.parseInt(tarifaISS.get("tipo_liquidacion")+""));
				this.tipoLiquidacion.setNombre(tarifaISS.get("nom_tipo_liquidacion")+"");
				this.tipoLiquidacionOriginal.setNombre(tarifaISS.get("nom_tipo_liquidacion")+"");
				this.servicio.setNombre(tarifaISS.get("nombre_servicio")+"");
				this.especialidad.setNombre(tarifaISS.get("nombre_especialidad")+"");
				this.especialidad.setCodigo(Integer.parseInt(tarifaISS.get("codigo_especialidad")+""));
				this.liquidarAsocios = (String)tarifaISS.get("liqasocios");
				this.liquidarAsociosOriginal = (String)tarifaISS.get("liqasocios");

				logger.info("===>Unidades HashMap: "+tarifaISS.get("unidades"));
				
				if(!UtilidadTexto.isEmpty(tarifaISS.get("unidades")+""))
				{
					this.unidades = Double.parseDouble(tarifaISS.get("unidades")+"");
					if(!UtilidadCadena.noEsVacio(this.valorTarifaString)) {
						this.valorTarifaString = UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(tarifaISS.get("unidades")+""));
						logger.info("===>1 Valor Tarifa String: "+this.valorTarifaString);
					}
					logger.info("===>2 Valor Tarifa String: "+this.valorTarifaString);
				}
				logger.info("===>Valor Tarifa String: "+this.valorTarifaString);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metodo que consulta todas las tarifas por esquema tarifario y servicio
	 * @param con
	 * @param esquemaTarifario
	 * @param codServicio
	 * @return
	 */
	public HashMap consultaTodasTarifas(Connection con, int esquemaTarifario, String codServicio, String tipoCodigoTarifario)
	{
		return this.tarifaISSDao.consultaTodasTarifas(con, esquemaTarifario, codServicio, tipoCodigoTarifario);
	}

	/**
	 * @return Returns the unidades.
	 */
	public double getUnidades() {
		return unidades;
	}
	/**
	 * @param unidades The unidades to set.
	 */
	public void setUnidades(double unidades) {
		this.unidades = unidades;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public InfoDatosInt getTipoLiquidacionOriginal() {
		return tipoLiquidacionOriginal;
	}

	public void setTipoLiquidacionOriginal(InfoDatosInt tipoLiquidacionOriginal) {
		this.tipoLiquidacionOriginal = tipoLiquidacionOriginal;
	}

	public double getValorTarifaOriginal() {
		return valorTarifaOriginal;
	}

	public void setValorTarifaOriginal(double valorTarifaOriginal) {
		this.valorTarifaOriginal = valorTarifaOriginal;
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
	public void setFechaVigencia(String fecha_vigencia) {
		this.fechaVigencia = fecha_vigencia;
	}

}
