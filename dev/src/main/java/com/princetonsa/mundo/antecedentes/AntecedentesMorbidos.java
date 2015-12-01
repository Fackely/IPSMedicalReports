/*
 * @(#)AntecedentesMorbidos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.AntecedentesMorbidosDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Clase para el manejo de todos los antecedentes mórbidos de todos los tipos
 * del paciente.
 *
 * @version 1.0, Agosto 1, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see com.princetonsa.mundo.antecedentes.AntecedenteMorbidoMedico
 * @see com.princetonsa.mundo.antecedentes.AntecedenteMorbidoQuirurgico
 * @see com.princetonsa.mundo.PersonaBasica
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class AntecedentesMorbidos
{
	private Logger logger = Logger.getLogger(AntecedentesMorbidos.class);
	
	/**
	 * Paciente al cual pertence este antecedente morbido
	 */
	private PersonaBasica paciente;
	
	/**
	 * Lista con los antecedentes mórbidos médicos predefinidos en la base de
	 * datos. Cada elemento es de tipo "AntecedenteMorbidoMedico".
	 */
	private ArrayList antecedentesMorbidosMedicosPredefinidos;

	/**
	 * Lista con los antecedentes mórbidos médicos no predefinidos en la base de
	 * datos. Cada elemento es de tipo "AntecedenteMorbidoMedico".
	 */	
	private ArrayList antecedentesMorbidosMedicosAdicionales;
	
	/**
	 * Lista con todos los antecedentes mórbidos quirurgicos del paciente. Cada
	 * elemento es de tipo "AntecedenteMorbidoQuirurgico"
	 */
	private ArrayList  antecedentesMorbidosQuirurgicos;
	
	/**
	 * Observaciones generales de los antecedentes mórbidos
	 */
	private String observaciones;
	
	/** antecedentesMorbidosDao * */
	private AntecedentesMorbidosDao antecedentesMorbidosDao;
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;
	
	
	
	/**
	 * Constructora de la calse. Inicializa todos los atributos.
	 */
	public AntecedentesMorbidos()
	{
		this.paciente = new PersonaBasica();
		this.antecedentesMorbidosMedicosPredefinidos = new ArrayList();
		this.antecedentesMorbidosMedicosAdicionales = new ArrayList();
		this.antecedentesMorbidosQuirurgicos = new ArrayList();
		this.observaciones = "";
		this.fecha = null;
		this.hora = null;
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Constructora de la calse. Inicializa todos los atributos con los valores
	 * dados
	 * @param PersonaBasica, paciente
	 * @param ArrayList, antecedentesMorbidosPredefinidos
	 * @param ArrayList, antecedentesMorbidosAnteriores
	 * @param ArrayList, antecedentesMorbidosQuirurgicos
	 * @param String, observaciones
	 */
	public AntecedentesMorbidos(PersonaBasica paciente, ArrayList antecedentesMorbidosMedicosPredefinidos, ArrayList antecedentesMorbidosMedicosAdicionales, ArrayList antecedentesMorbidosQuirurgicos, String observaciones)
	{
		this.paciente=paciente;
		//No podemos utilizar el viejo, así que creamos uno nuevo
		//No se elimino el parametro para mantener la estructura original
		this.paciente = new PersonaBasica();
		this.antecedentesMorbidosMedicosPredefinidos = antecedentesMorbidosMedicosPredefinidos;
		this.antecedentesMorbidosMedicosAdicionales = antecedentesMorbidosMedicosAdicionales;
		this.antecedentesMorbidosQuirurgicos = antecedentesMorbidosQuirurgicos;
		this.observaciones = observaciones;
		this.init(System.getProperty("TIPOBD"));
	}
	
	public void init (String tipoBD) 
	{
		if (antecedentesMorbidosDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedentesMorbidosDao = myFactory.getAntecedentesMorbidosDao();
		}
	}

	public void reset()
	{
		this.antecedentesMorbidosMedicosPredefinidos = new ArrayList();
		this.antecedentesMorbidosMedicosAdicionales = new ArrayList();
		this.antecedentesMorbidosQuirurgicos = new ArrayList();
		this.observaciones = "";	
		this.fecha = null;
		this.hora = null;
	}
	
	/**
	 * Retorna el paciente al cual pertence este antecedente morbido
	 * @return PersonaBasica, paciente
	 */
	public PersonaBasica getPaciente()
	{
		return paciente;
	}

	/**
	 * Asigna el paciente al cual pertence este antecedente morbido
	 * @param PersonaBasica, paciente
	 */
	public void setPaciente(PersonaBasica paciente)
	{
		this.paciente = paciente;
	}

	/**
	 * Retorna la lista con los antecedentes mórbidos médicos predefinidos en la
	 * base de datos. Cada elemento es de tipo "AntecedenteMorbidoMedico".
	 * @return ArrayList, antecedentes mórbidos predefinidos
	 */
	public ArrayList getAntecedentesMorbidosMedicosPredefinidos()
	{
		return antecedentesMorbidosMedicosPredefinidos;
	}

	/**
	 * Asigna la lista con los antecedentes mórbidos médicos predefinidos en la
	 * base de datos. Cada elemento es de tipo "AntecedenteMorbidoMedico".
	 * @param ArrayList, antecedentes mórbidos predefinidos
	 */
	public void setAntecedentesMorbidosMedicosPredefinidos(ArrayList antecedentesMorbidosPredefinidos)
	{
		this.antecedentesMorbidosMedicosPredefinidos =
			antecedentesMorbidosPredefinidos;
	}

	/**
	 * Retorna el antecedente mórbido médico predefinido dado su indice dentro
	 * de la lista.
	 * @param  int, indice
	 * @return	AntecedenteMorbidoMedico, antecedente correspondiente al indice
	 * 				dado o null de lo contrario
	 */
	public AntecedenteMorbidoMedico getAntecedenteMorbidoMedicoPredefinido(int indice)
	{
		return (AntecedenteMorbidoMedico)this.antecedentesMorbidosMedicosPredefinidos.get(indice);
	}
	
	/**
	 * Asigna el antecedente mórbido mèdico predefinido al final de la lista de
	 * antecedentes mórbidos predefinidos
	 * @param AntecedenteMorbidoMedico, antecedente
	 */
	public void setAntecedenteMorbidoMedicoPredefinido(AntecedenteMorbidoMedico antecedente)
	{
		this.antecedentesMorbidosMedicosPredefinidos.add(antecedente);
	}
	
	/**
	 * Retorna la lista con los antecedentes mórbidos médicos no predefinidos en
	 * la base de datos. Cada elemento es de tipo "AntecedenteMorbidoMedico".
	 * @return ArrayList, antecedentes morbidos adicionales
	 */
	public ArrayList getAntecedentesMorbidosMedicosAdicionales()
	{
		return antecedentesMorbidosMedicosAdicionales;
	}

	/**
	 * Asigna la lista con los antecedentes mórbidos médicos no predefinidos en
	 * la base de datos. Cada elemento es de tipo "AntecedenteMorbidoMedico".
	 * @param ArrayList, antecedentes morbidos medicos adicionales	 
	 */
	public void setAntecedentesMorbidosMedicosAdicionales(ArrayList antecedentesMorbidosMedicosAdicionales)
	{
		this.antecedentesMorbidosMedicosAdicionales =
			antecedentesMorbidosMedicosAdicionales;
	}

	/**
	 * Retorna el antecedente mórbido médico adicional dado su indice dentro de
	 * la lista.
	 * @param  int, indice
	 * @return	AntecedenteMorbidoMedico, antecedente correspondiente al indice
	 * 				dado o null de lo contrario
	 */
	public AntecedenteMorbidoMedico getAntecedenteMorbidoMedicoAdicional(int indice)
	{
		return (AntecedenteMorbidoMedico)this.antecedentesMorbidosMedicosAdicionales.get(indice);
	}
	
	/**
	 * Asigna el antecedente mórbido médico adiconal al final de la lista de
	 * antecedentes mórbidos adicionales
	 * @param AntecedenteMorbidoMedico, antecedente
	 */
	public void setAntecedenteMorbidoMedicoAdicional(AntecedenteMorbidoMedico antecedente)
	{
		this.antecedentesMorbidosMedicosAdicionales.add(antecedente);
	}

	/**
	 * Retorna la lista con todos los antecedentes mórbidos quirurgicos del
	 * paciente. Cada elemento es de tipo "AntecedenteMorbidoQuirurgico"
	 * @return ArrayList, antecedentes mórbidos quirurgicos
	 */
	public ArrayList getAntecedentesMorbidosQuirurgicos()
	{
		return antecedentesMorbidosQuirurgicos;
	}

	/**
	 * Asigna la lista con todos los antecedentes mórbidos quirurgicos del
	 * paciente. Cada elemento es de tipo "AntecedenteMorbidoQuirurgico"
	 * @param ArrayList, antecedentes morbidos quirurgicos
	 */
	public void setAntecedentesMorbidosQuirurgicos(ArrayList antecedentesMorbidosQuirurgicos)
	{
		this.antecedentesMorbidosQuirurgicos = antecedentesMorbidosQuirurgicos;
	}

	/**
	 * Retorna el antecedente mórbido quirurgico adicional dado su indice dentro
	 * de la lista.
	 * @param  int, indice
	 * @return	AntecedenteMorbidoQuirurgico, antecedente correspondiente al
	 * indice dado o null de lo contrario
	 */
	public AntecedenteMorbidoQuirurgico getAntecedenteMorbidoQuirurgico(int indice)
	{
		return (AntecedenteMorbidoQuirurgico)this.antecedentesMorbidosQuirurgicos.get(indice);
	}
	
	/**
	 * Asigna el antecedente mórbido quirurgico al final de la lista de
	 * antecedentes mórbidos adicionales
	 * @param AntecedenteMorbidoQuirurgico, antecedente
	 */
	public void setAntecedenteMorbidoQuirurgico(AntecedenteMorbidoQuirurgico antecedente)
	{
		this.antecedentesMorbidosQuirurgicos.add(antecedente);
	}

	/**
	 * Retorna las observaciones generales de los antecedentes mórbidos
	 * @return String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones generales de los antecedentes mórbidos
	 * @param String, observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}
	
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
		return this.antecedentesMorbidosDao.existenAntecedentes(con, codigoPaciente);
	}
	
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		return this.antecedentesMorbidosDao.insertarAntecedenteGeneral(con, codigoPaciente);
	}

	public ResultadoBoolean existenAntecedentesMorbidos(Connection con, int codigoPaciente)
	{
		return this.antecedentesMorbidosDao.existenAntecedentesMorbidos(con, codigoPaciente);
	}

	public ResultadoBoolean insertarAntecedenteMorbido(Connection con, int codigoPaciente)
	{
		return this.antecedentesMorbidosDao.insertar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean insertarAntecedenteMorbidoTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesMorbidosDao.insertarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}

	public ResultadoBoolean modificarAntecedenteMorbido(Connection con, int codigoPaciente)
	{
		return this.antecedentesMorbidosDao.modificar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean modificarAntecedenteMorbidoTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesMorbidosDao.modificarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}
	
	public void cargarMorbidos(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesMorbidosDao.consultarAntecedentesMorbidos(con, codigoPaciente);
		
		if( resultado != null )
		{
			try
			{
				if( resultado.next() )
				{
					observaciones = resultado.getString("observaciones");
					
					if(!UtilidadTexto.isEmpty(resultado.getString("hora"))){
						hora = resultado.getString("hora");
					}
					
					Log4JManager.info(resultado.getString("fecha")+"");
					
					if(!UtilidadTexto.isEmpty(resultado.getString("fecha"))){
						fecha = UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")+"");
					}
				}
			}
			catch(SQLException e)
			{
				logger.warn("No se puede recorrer el ResultSetDecorator con las observaciones generales de los antecedentes mórbidos \n"+e);
			}
		}		
	}
	
	
	public ArrayList cargarMorbidosMedicosPredefinidos(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesMorbidosDao.cargarMorbidosMedicosPredefinidos(con, codigoPaciente);

		if( resultado == null )
		{
			this.antecedentesMorbidosMedicosPredefinidos = new ArrayList();
			return antecedentesMorbidosMedicosPredefinidos;
		}
		
		try
		{
			while( resultado.next() )
			{
				AntecedenteMorbidoMedico antecedente = new AntecedenteMorbidoMedico();
				antecedente.setCodigo(resultado.getInt("tipoAntecedente"));
				antecedente.setNombre(resultado.getString("nombreAntecedente"));
				antecedente.setFechaInicio(resultado.getString("fechaInicio"));
				antecedente.setTratamiento(resultado.getString("tratamiento"));
				antecedente.setRestriccionDietaria(resultado.getString("restriccionDietaria"));
				antecedente.setObservaciones(resultado.getString("observaciones"));
				
				if(!UtilidadTexto.isEmpty(resultado.getString("fecha")+"")){
					antecedente.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")+""));
					antecedente.setHora(resultado.getString("hora"));
				}
				
				this.setAntecedenteMorbidoMedicoPredefinido(antecedente);
			}
		}
		catch(SQLException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los antecedentes mórbidos médicos predefinidos \n"+e);
			return null;
		}
		catch(NullPointerException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los antecedentes mórbidos médicos predefinidos \n"+e);
			return null;			
		}
		
		return this.antecedentesMorbidosMedicosPredefinidos;
	}
	
	
	public ArrayList cargarMorbidosMedicosAdicionales(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesMorbidosDao.cargarMorbidosMedicosOtros(con, codigoPaciente);
		
		if( resultado == null )
		{
			this.antecedentesMorbidosMedicosAdicionales = new ArrayList();
			return antecedentesMorbidosMedicosAdicionales;
		}
		
		try
		{
			while( resultado.next() )
			{
				AntecedenteMorbidoMedico antecedente = new AntecedenteMorbidoMedico();
				antecedente.setCodigo(resultado.getInt("codigo"));
				antecedente.setNombre(resultado.getString("nombre"));
				antecedente.setFechaInicio(resultado.getString("fechaInicio"));
				antecedente.setTratamiento(resultado.getString("tratamiento"));
				antecedente.setRestriccionDietaria(resultado.getString("restriccionDietaria"));
				antecedente.setObservaciones(resultado.getString("observaciones"));
				
				if(!UtilidadTexto.isEmpty(resultado.getString("fecha")+"")){
					antecedente.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")+""));
					antecedente.setHora(resultado.getString("hora"));
				}
				
				this.setAntecedenteMorbidoMedicoAdicional(antecedente);
			}
		}
		catch(SQLException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los antecedentes mórbidos médicos otros \n"+e);
			return null;
		}
		
		return this.antecedentesMorbidosMedicosAdicionales;
	}

	public ArrayList cargarMorbidosQuirurgicos(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesMorbidosDao.cargarMorbidosQuirurgicos(con, codigoPaciente);
		
		if( resultado == null )
		{
			this.antecedentesMorbidosQuirurgicos = new ArrayList();
			return antecedentesMorbidosQuirurgicos;
		}
		
		try
		{
			while( resultado.next() )
			{
				AntecedenteMorbidoQuirurgico antecedente = new AntecedenteMorbidoQuirurgico();
				antecedente.setCodigo(resultado.getInt("codigo"));
				antecedente.setNombre(resultado.getString("nombre"));
				antecedente.setFecha(resultado.getString("fecha"));
				antecedente.setCausa(resultado.getString("causa"));
				antecedente.setComplicaciones(resultado.getString("complicaciones"));
				antecedente.setRecomendaciones(resultado.getString("recomendaciones"));
				antecedente.setObservaciones(resultado.getString("observaciones"));
				
				if(!UtilidadTexto.isEmpty(resultado.getString("fecha")+"")){
					antecedente.setFecha_ant((UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha")+"")));
					antecedente.setHora_ant((resultado.getString("hora")+""));
				}
				
				this.setAntecedenteMorbidoQuirurgico(antecedente);
			}
		}
		catch(SQLException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen los antecedentes mórbidos quirúrgicos \n"+e);
			return null;
		}
		
		return this.antecedentesMorbidosQuirurgicos;
	}
	
	public void cargar(Connection con)
	{
		this.cargarMorbidos(con, paciente.getCodigoPersona());
		this.cargarMorbidosMedicosPredefinidos(con, paciente.getCodigoPersona());
		this.cargarMorbidosMedicosAdicionales(con, paciente.getCodigoPersona());
		this.cargarMorbidosQuirurgicos(con, paciente.getCodigoPersona());
	}	
	
	public ResultadoBoolean updateTransaccional(Connection con) throws Exception
	{
		ResultadoBoolean resultado;
			
		// Verificar si existen otros antecedentes para este paciente, si no, se debe insertar en la tabla general.
		resultado = this.existenAntecedentes(con, paciente.getCodigoPersona()); 
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteGeneral(con, paciente.getCodigoPersona());
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}
		
		// Verificar si existen algun registro de antecedentes morbidos, si no, se debe insertar uno nuevo
		resultado = this.existenAntecedentesMorbidos(con, paciente.getCodigoPersona());
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteMorbido(con, paciente.getCodigoPersona());
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}
		else
		if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
		{
			resultado = this.modificarAntecedenteMorbido(con, paciente.getCodigoPersona());
			
			if( !resultado.isTrue() )
				return resultado;
		}
		else
			return resultado;
		
		String estado = ConstantesBD.inicioTransaccion;
		// Insertar morbidos medicos predefinidos
		int tamMMP = this.antecedentesMorbidosMedicosPredefinidos.size();
		int tamMMO = this.antecedentesMorbidosMedicosAdicionales.size();
		int tamMQ = this.antecedentesMorbidosQuirurgicos.size();
		
		for(int i=0; i<tamMMP; i++)
		{
			if( i!= 0 )
				estado = ConstantesBD.continuarTransaccion;

			if( i==tamMMP-1 && tamMMO == 0 && tamMQ == 0 )
				estado = ConstantesBD.finTransaccion;				
				
			AntecedenteMorbidoMedico antecedente = (AntecedenteMorbidoMedico) this.antecedentesMorbidosMedicosPredefinidos.get(i);
			
			resultado = antecedente.existeAntecedentePredefinido(con, paciente.getCodigoPersona());
			
			logger.debug("Existe antecedente predefinido = "+resultado.isTrue());
			
			if( resultado.isTrue() )
			{
				resultado = antecedente.modificarPredefinidoTransaccional(con, paciente.getCodigoPersona(), estado);
				
				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = antecedente.insertarTransaccionalPredefinido(con, paciente.getCodigoPersona(), estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
				return resultado;										
		}		

		// Insertar morbidos medicos otros
		if( tamMMP == 0 )
			estado = ConstantesBD.inicioTransaccion;
		
		for(int i=0; i<tamMMO; i++)
		{
			if( i!= 0 )
				estado = ConstantesBD.continuarTransaccion;

			if( i==tamMMO-1 && tamMQ == 0 )
				estado = ConstantesBD.finTransaccion;				
				
			AntecedenteMorbidoMedico antecedente = (AntecedenteMorbidoMedico) this.antecedentesMorbidosMedicosAdicionales.get(i);
			
			resultado = antecedente.existeAntecedenteOtro(con, paciente.getCodigoPersona());
			
			logger.debug("Existe antecedente otro ("+paciente.getCodigoTipoIdentificacionPersona()+", "+paciente.getNumeroIdentificacionPersona()+", "+antecedente.getCodigo()+") = "+resultado.isTrue());
			
			if( resultado.isTrue() )
			{
				resultado = antecedente.modificarOtroTransaccional(con, paciente.getCodigoPersona(), estado);
				
				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = antecedente.insertarTransaccionalOtro(con, paciente.getCodigoPersona(), estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;
			}
			else
				return resultado;										
		}		
		
		// Insertar morbidos quirurgicos
		if( tamMMP == 0 && tamMMO == 0 )
			estado = ConstantesBD.inicioTransaccion;
			
		for(int i=0; i<tamMQ; i++)
		{
			if( i!=0  )
				estado = ConstantesBD.continuarTransaccion;
				
			if( i==tamMQ-1 )
				estado = ConstantesBD.finTransaccion;
				
			AntecedenteMorbidoQuirurgico antecedente = (AntecedenteMorbidoQuirurgico) this.antecedentesMorbidosQuirurgicos.get(i);
				
			resultado = antecedente.existeAntecedente(con, paciente.getCodigoPersona());
				
			logger.debug("Existe antecedente quirurgico = "+resultado.isTrue());
			logger.debug("El estado es = "+estado);
				
			if( resultado.isTrue() )
			{
				resultado = antecedente.modificarTransaccional(con, paciente.getCodigoPersona(), estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;					
			}
			else
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = antecedente.insertarTransaccional(con, paciente.getCodigoPersona(), estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;				
			}
			else
				return resultado;													
		}
		return new ResultadoBoolean(true);
	}

	/**
	 * @return valor de fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha el fecha para asignar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return valor de hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora el hora para asignar
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
}
