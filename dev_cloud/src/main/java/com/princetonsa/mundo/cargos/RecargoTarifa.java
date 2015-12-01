/*
 * @(#)RecargoTarifa.java
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
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RecargoTarifaDao;

import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;

/**
 * Clase para el manejo de un recargo a una tarifa
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Raul@PrincetonSA.com">Raúl Cancino</a>
 */
public class RecargoTarifa
{

	private String nombreConvenio;
	/**
	 * convenio asociado al contrato que cubre el recargo
	 */
	private int convenio;
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RecargoTarifa.class);

	
	/**
	 * Nombre relativo del archivo de logs para esta funcionalidad
	 */
	private String nombreArchivoLogs;
	
	
	/**
	 * Codigo del recargo
	 */
	private int codigo;
	
	/**
	 * Porcentaje de recargo
	 */
	private double porcentaje;
	
	/**
	 * Valor del recargo
	 */
	private double valor;
	
	/**
	 * Servicio asociado al recargo
	 */
	private InfoDatosInt servicio;
	
	/**
	 * Especialidad asociada al recargo
	 */
	private InfoDatosInt especialidad;
	
	/**
	 * Via de ingreso asociada al recargo
	 */
	private InfoDatosInt viaIngreso;
	
	/**
	 * Tipo de recargo
	 */
	private InfoDatosInt tipoRecargo;
	
	/**
	 * Contrato asociado al recargo
	 */
	private Contrato contrato;
	
	private RecargoTarifaDao recargoTarifaDao = null;
	
	
	/**
	 * Creadora de la clase RecargoTarifa.java
	 */
	public RecargoTarifa()
	{
		this.codigo = 0;
		this.porcentaje = 0.0;
		this.valor = 0.0;
		this.contrato = new Contrato();
		this.servicio = new InfoDatosInt();
		this.viaIngreso = new InfoDatosInt();
		this.especialidad = new InfoDatosInt();
		this.tipoRecargo = new InfoDatosInt();
		this.init(System.getProperty("TIPOBD"));			
	}

	/**
	 * Creadora de la clase RecargoTarifa.java
	 * @param codigo, int, código del recargo 
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param contrato. Contrato, contrato para el cual es válid este recargo
	 * @param servicio. InfoDatosInt, servicio asociado esta tarifa, si el código es 0 es para todos
	 * @param viaIngreso. InfoDatosInt, código de la via de ingreso asociado esta tarifa, si el código es 0 es para todas
	 * @param especialidad. InfoDatosInt, especialidad asociado esta tarifa, si el código es 0 es para todas
	 * @param tipoRecargo. InfoDatosInt, tipo de recargo
	 */
	public RecargoTarifa(	int codigo,
										double porcentaje,
										double valor,
										Contrato contrato,
										InfoDatosInt servicio,
										InfoDatosInt viaIngreso,
										InfoDatosInt especialidad,
										InfoDatosInt tipoRecargo)
	{
		this.codigo = codigo;
		this.porcentaje = porcentaje;
		this.valor = valor;
		this.contrato = contrato;
		this.servicio = servicio;
		this.viaIngreso = viaIngreso;
		this.especialidad = especialidad;
		this.tipoRecargo = tipoRecargo;
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
		if( recargoTarifaDao == null)
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
					recargoTarifaDao = myFactory.getRecargoTarifaDao();
				}					
			}
		}
	}
	
	
	
	


	/**
	 * Retorna el codigo del recargo
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el codigo del contrato
	 * @param codigo
	 */
	public void setCodigoContrato(int codigo)
	{
		this.contrato.setCodigo(codigo);
		
	}
	/**
	 * Asigna el codigo del contrato
	 * @param codigo
	 */
	public int getCodigoContrato()
	{
		return this.contrato.getCodigo();
	
	}
	/**
	 * Asigna el numero del contrato
	 * @param codigo
	 */
	public void setNumeroContrato(String codigo)
	{
		this.contrato.setNumeroContrato(codigo);
	}
	/**
		 * Retorna el numero del contrato
		 * @return
		 */
		public String getNumeroContrato()
		{
			return contrato.getNumeroContrato();
		}

		/**
		 * Asigna el codigo del recargo
		 * @param codigo
		 */
		public void setCodigo(int codigo)
		{
			this.codigo = codigo;
		}


	/**
	 * Retorna el porcentaje de recargo
	 * @return
	 */
	public double getPorcentaje()
	{
		return porcentaje;
	}

	/**
	 * Asigna el porcentaje de recargo
	 * @param porcentaje
	 */
	public void setPorcentaje(double porcentaje)
	{
		this.porcentaje = porcentaje;
	}

	/**
	 * Retorna el valor del recargo
	 * @return
	 */
	public double getValor()
	{
		return valor;
	}

	/**
	 * Asigna el alor del recargo
	 * @param valor
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}

	/**
	 * Retorna el servicio asociado al recargo
	 * @return
	 */
	public InfoDatosInt getServicio()
	{
		return servicio;
	}

	/**
	 * Asigna el servicio asociado al recargo
	 * @param servicio 
	 */
	public void setServicio(InfoDatosInt servicio )
	{
		this.servicio = servicio ;
	}
	/**
	 * Retorna el codigo del servicio asociado al recargo
	 * @return
	 */
	public int getCodigoServicio()
	{
		return servicio.getCodigo();
	}

	/**
	 * Asigna el codigo del servicio asociado al recargo
	 * @param servicio 
	 */
	public void setCodigoServicio(int servicio )
	{
		this.servicio.setCodigo(servicio) ;
	}
	/**
	 * Retorna el nombre del servicio asociado al recargo
	 * @return
	 */
	public String getNombreServicio()
	{
		return servicio.getNombre();
	}

	/**
	 * Asigna el nombre del servicio asociado al recargo
	 * @param servicio 
	 */
	public void setNombreServicio(String servicio )
	{
		this.servicio.setNombre(servicio) ;
	}


	/**
	 * Retorna la especialidad asociada al recargo
	 * @return
	 */
	public InfoDatosInt getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * Asigna la especialidad asociada al recargo
	 * @param especialidad
	 */
	public void setEspecialidad(InfoDatosInt especialidad)
	{
		this.especialidad = especialidad;
	}

	/**
	 * Retorna la via de ingreso asociada al recargo
	 * @return
	 */
	public InfoDatosInt getViaIngreso()
	{
		return viaIngreso;
	}

	/**
	 * Asigna la via de ingreso asociada al recargo
	 * @param viaIngreso
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso)
	{
		this.viaIngreso = viaIngreso;
	}

	/**
	 * Retorna el tipo de recargo
	 * @return
	 */
	public InfoDatosInt getTipoRecargo()
	{
		return tipoRecargo;
	}

	/**
	 * Asigna el tipo de recargo
	 * @param tipoRecargo
	 */
	public void setTipoRecargo(InfoDatosInt tipoRecargo)
	{
		this.tipoRecargo = tipoRecargo;
	}

	/**
	 * Retorna el contrato asociado al recargo
	 * @return
	 */
	public Contrato getContrato()
	{
		return contrato;
	}

	/**
	 * Asigna el contrato asociado al recargo
	 * @param contrato
	 */
	public void setContrato(Contrato contrato)
	{
		this.contrato = contrato;
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
	 * Inserta un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#insertar(java.sql.Connection, double, double, int, int, int, int, int)
	 */
	public ResultadoBoolean insertar(	Connection con	)throws SQLException
	{
		return this.recargoTarifaDao.insertar(	con,
																	porcentaje,
																	valor,
																	tipoRecargo.getCodigo(),
																	servicio.getCodigo(),
																	especialidad.getCodigo(),
																	viaIngreso.getCodigo(),
																	contrato.getCodigo());
	}

	/**
	 * Inserta un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#insertarTransaccional(java.sql.Connection, double, double, int, int, int, int, int, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
																					String estado) throws SQLException
	{
		return this.recargoTarifaDao.insertarTransaccional(	con,
																							porcentaje,
																							valor,
																							tipoRecargo.getCodigo(),
																							servicio.getCodigo(),
																							especialidad.getCodigo(),
																							viaIngreso.getCodigo(),
																							contrato.getCodigo(),
																							estado);
	}

	/**
	 * Modifica un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la modificacion fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#modificar(java.sql.Connection, int, double, double, int, int, int, int, int)
	 */
	public ResultadoBoolean modificar(	Connection con, RecargoTarifa original	)throws SQLException
	{
		//validar cambios
		ResultadoBoolean resultado=null;
		if( original == null || original.getCodigoTipoRecargo() != this.getCodigoTipoRecargo() || original.getCodigoServicio() != this.getCodigoServicio() ||
			original.getCodigoEspecialidad()!=this.getCodigoEspecialidad() || original.getCodigoViaIngreso()!=this.getCodigoViaIngreso() || original.getCodigoContrato()!=this.getCodigoContrato())
		{
			if(this.existeRecargo(con)){
				
				resultado = new ResultadoBoolean(false);	
				resultado.setDescripcion(" El recargo para el servicio y la especialidad dada ya está definido.");
			}else
			{
				resultado=new ResultadoBoolean(true);
			}
		}else{
			resultado=new ResultadoBoolean(true);
		}		
		if(resultado.isTrue()){
			this.recargoTarifaDao.modificar(	con,codigo,
			porcentaje,
			valor,
			tipoRecargo.getCodigo(),
			servicio.getCodigo(),
			especialidad.getCodigo(),
			viaIngreso.getCodigo(),
			contrato.getCodigo());
				
		}
		
		return resultado;
		
		

																		
	}

	/**
	 * Modifica un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param estado. String, estado dentro de la transacción
	 * @return ResultadoBoolean, true si la modificación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#modificarTransaccional(java.sql.Connection, int, double, double, int, int, int, int, int, java.lang.String)
	 *
	 **++++++++++++++++++++++++++++++++++++++++++or,
																						int codigoTipoRecargo,
																						int codigoServicio,
																						int codigoEspecialidad,
																						int codigoViaIngreso,
																						int codigoContrato,
																						String estado)
	{
		return this.recargoTarifaDao.modificarTransaccional(	con,
																								codigo,
																								porcentaje,
																								valor,
																								tipoRecargo.getCodigo(),
																								servicio.getCodigo(),
																								especialidad.getCodigo(),
																								viaIngreso.getCodigo(),
																								contrato.getCodigo(),
																								estado	);
	}

	/**
	 * Elimina el recargo 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la eliminación fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#eliminar(java.sql.Connection, int)
	 */
	public ResultadoBoolean eliminar(Connection con)throws SQLException
	{
		return this.recargoTarifaDao.eliminar(con, codigo);
	}

	/**
	 * Consulta el recargo 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si fue exitosa
	 * la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoBoolean consultar(Connection con,boolean conCodigo)throws SQLException
	{
		ResultadoCollectionDB resultado = this.recargoTarifaDao.consultar(con, codigo,conCodigo,this.getCodigoViaIngreso(),this.getCodigoEspecialidad(),this.getCodigoServicio(),this.getCodigoContrato());
		if( !resultado.isTrue() )
			return new ResultadoBoolean(false, resultado.getDescripcion());
		else
		{
			LinkedList listado = (LinkedList)resultado.getFilasRespuesta();
			int tam = 0;			
			if( listado != null && (tam = listado.size()) > 0 )
			{		
				for( int i=0; i < tam; i++ )
				{
					HashMap recargo = (HashMap)(listado).get(i);					
					
					this.setCodigo(((Integer)recargo.get("codigo")).intValue());
					this.setValor(((Double)recargo.get("valor")).doubleValue());
					this.setPorcentaje(((Double)recargo.get("porcentaje")).doubleValue());

					if(recargo.get("codigoespecialidad")!=null){
						this.setCodigoEspecialidad(((Integer)recargo.get("codigoespecialidad")).intValue());
						this.setNombreEspecialidad(recargo.get("nombreespecialidad").toString());						
					}else{
						this.setCodigoEspecialidad(0);
						this.setNombreEspecialidad("Todas");
					}
					logger.info("codigo contrato:"+((Integer)recargo.get("codigocontrato")).intValue());
					this.setCodigoContrato(((Integer)recargo.get("codigocontrato")).intValue());
					this.setNumeroContrato(recargo.get("numerocontrato").toString());
					this.setConvenio(((Integer)recargo.get("codigoconvenio")).intValue());
					this.setNombreConvenio(recargo.get("nombreconvenio").toString());
					if(recargo.get("codigoservicio")!=null){
						this.setCodigoServicio(((Integer)recargo.get("codigoservicio")).intValue());
						this.setNombreServicio(recargo.get("descripcion").toString());
					}else{
						this.setCodigoServicio(0);		
						this.setNombreServicio("Todos");				
					}
					
					this.setCodigoTipoRecargo(((Integer)recargo.get("codigotiporecargo")).intValue());
					
					if(recargo.get("nombreviaingreso")!=null){
						this.setNombreViaIngreso(recargo.get("nombreviaingreso").toString());
						this.setCodigoViaIngreso(((Integer)recargo.get("viaingreso")).intValue());
					}else{
						this.setNombreViaIngreso("Todas");
						this.setCodigoViaIngreso(0);
					}
					
					this.setNombreTipoRecargo(recargo.get("nombretiporecargo").toString());
				
				}
			}
		}
		return new ResultadoBoolean(true);
	
	}
	/**
	 * Retorna la especialidad asociada al recargo
	 * @return
	 */
	public String getNombreEspecialidad()
	{
		return especialidad.getNombre();
	}

	/**
	 * Asigna la especialidad asociada al recargo
	 * @param especialidad
	 */
	public void setNombreEspecialidad(String especialidad)
	{
		this.especialidad.setNombre(especialidad);
	}
	/**
	 * Retorna la especialidad asociada al recargo
	 * @return
	 */
	public int getCodigoEspecialidad()
	{
		return especialidad.getCodigo();
	}

	/**
	 * Asigna la especialidad asociada al recargo
	 * @param especialidad
	 */
	public void setCodigoEspecialidad(int especialidad)
	{
		this.especialidad.setCodigo(especialidad);
	}
	/**
	 * Retorna la via de ingreso asociada al recargo
	 * @return
	 */
	public int getCodigoViaIngreso()
	{
		return viaIngreso.getCodigo();
	}

	/**
	 * Asigna la via de ingreso asociada al recargo
	 * @param viaIngreso
	 */
	public void setCodigoViaIngreso(int viaIngreso)
	{
		this.viaIngreso.setCodigo(viaIngreso);
	}
	/**
	 * Retorna la via de ingreso asociada al recargo
	 * @return
	 */
	public String getNombreViaIngreso()
	{
		return viaIngreso.getNombre();
	}

	/**
	 * Asigna la via de ingreso asociada al recargo
	 * @param viaIngreso
	 */
	public void setNombreViaIngreso(String viaIngreso)
	{
		this.viaIngreso.setNombre(viaIngreso);
	}
	/**
	 * Retorna la via de ingreso asociada al recargo
	 * @return
	 */
	public int getCodigoTipoRecargo()
	{
		return tipoRecargo.getCodigo();
	}

	/**
	 * Asigna la via de ingreso asociada al recargo
	 * @param viaIngreso
	 */
	public void setCodigoTipoRecargo(int tipoRecargo)
	{
		this.tipoRecargo.setCodigo(tipoRecargo);
	}
	/**
	 * Retorna la via de ingreso asociada al recargo
	 * @return
	 */
	public String getNombreTipoRecargo()
	{
		return tipoRecargo.getNombre();
	}

	/**
	 * Asigna la via de ingreso asociada al recargo
	 * @param viaIngreso
	 */
	public void setNombreTipoRecargo(String tipoRecargo)
	{
		this.tipoRecargo.setNombre(tipoRecargo);
	}
	




	public boolean existeRecargo(Connection con) throws SQLException{
		return recargoTarifaDao.existeRecargo(con,
													this.getCodigoTipoRecargo(),
													this.getCodigoServicio(),
													this.getCodigoEspecialidad(),
													this.getCodigoViaIngreso(),
													this.getCodigoContrato());
	}

	

	/**
	 * Returns the recargoTarifaDao.
	 * @return RecargoTarifaDao
	 */
	public RecargoTarifaDao getRecargoTarifaDao()
	{
		return recargoTarifaDao;
	}

	
	/**
	 * Sets the recargoTarifaDao.
	 * @param recargoTarifaDao The recargoTarifaDao to set
	 */
	public void setRecargoTarifaDao(RecargoTarifaDao recargoTarifaDao)
	{
		this.recargoTarifaDao = recargoTarifaDao;
	}

	/**
	 * Returns the convenio.
	 * @return int
	 */
	public int getConvenio()
	{
		return convenio;
	}

	/**
	 * Sets the convenio.
	 * @param convenio The convenio to set
	 */
	public void setConvenio(int convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * Returns the nombreConvenio.
	 * @return String
	 */
	public String getNombreConvenio()
	{
		return nombreConvenio;
	}

	/**
	 * Sets the nombreConvenio.
	 * @param nombreConvenio The nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio)
	{
		this.nombreConvenio = nombreConvenio;
	}

	
	/**
	 * Genera una cadena con los datos del recargo
	 * @return
	 */
	public String getCadenaLogRecargo(  )
	{
		String logTarifa = new String();
		
		logTarifa = "\tContrato [No."+this.getNumeroContrato()+"]";
		logTarifa += "\n\tVía de Ingreso ["+this.getCodigoViaIngreso()+"-"+this.getNombreViaIngreso()+"]";
		logTarifa += "\n\tServicio";
		logTarifa += "\n\t\tEspecialidad ["+this.getCodigoEspecialidad()+"-"+this.getNombreEspecialidad()+"]";
		logTarifa += "\n\t\tProcedimiento ["+this.getCodigoServicio()+"-"+this.getNombreServicio()+"]";
		logTarifa += "\n\tTipo de Recargo ["+this.getCodigoTipoRecargo()+"-"+this.getNombreTipoRecargo()+"]";
		
		if( this.getPorcentaje() != 0.0 )
		{
			logTarifa += "\n\tPorcentaje Excepción ["+this.getPorcentaje()+"]";
		}

		if( this.getValor() != 0.0 )
		{
			logTarifa += "\n\tValor ["+this.getValor()+"]";
		}
		
		return logTarifa;
	}

	/**
	 * Metodo para asignar a una Collection la consulta de Recargo Tarifa.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo del recargo de tarifa.
	 * @return Collection.
	 */
	public Collection consultaUnRecargoTarifa (Connection con, int codigo)
	{
	    recargoTarifaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecargoTarifaDao();
	    Collection coleccion = null;
	    
	    try 
	    {
			coleccion = UtilidadBD.resultSet2Collection(recargoTarifaDao.consultaRecargoTarifa(con, codigo));
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo Recargo Tarifa con el codigo->"+codigo+" "+e.toString());
			coleccion = null;
		}
	    
		return coleccion;
	}
	
	/**
	 * para obtener el codigo del recargo_tarifa actual.
	 * @see SqlBaseRecargoTarifaDao.
	 * @param con Connection con la fuente de datos
	 * @return int (codigo recargo)
	 */  
		public int recargoActual (Connection con)
		{
		    int codigo=0;
		    codigo=recargoTarifaDao.cargarUltimoCodigoSequence(con);
		    return codigo;
		}
}
