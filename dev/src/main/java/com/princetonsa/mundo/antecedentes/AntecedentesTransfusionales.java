package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.AntecedentesTransfusionalesDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de todos los antecedentes transfusionales para el
 * paciente
 *
 * @version 1.0, Septiembre 2, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
@SuppressWarnings("rawtypes")
public class AntecedentesTransfusionales
{
	private Logger logger = Logger.getLogger(AntecedentesTransfusionales.class);
	
	/**
	 * Arreglo con todas las transfusiones realizadas al paciente.
	 */
	private ArrayList transfusiones;
	
	/**
	 * Observaciones generales de antecedentes transfusionales
	 */
	private String observaciones;
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;

	
	/**
	 * Medio para comunicación con la base de datos
	 */
	private AntecedentesTransfusionalesDao antecedentesTransfusionalesDao = null;


	public AntecedentesTransfusionales()
	{
		this.transfusiones = new ArrayList();
		this.observaciones = new String();
		this.fecha = null;
		this.hora = null;
		this.init(System.getProperty("TIPOBD"));
	}

	public AntecedentesTransfusionales(ArrayList transfusiones, String observaciones)
	{
		this.transfusiones = transfusiones;
		this.observaciones = observaciones;
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el objeto de acceso a la base de datos
	 * @param	String, tipoBD. Identificador único de la base de datos que se 
	 * 				está usando.
	 */
	public void init (String tipoBD) 
	{
		if (antecedentesTransfusionalesDao  == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			antecedentesTransfusionalesDao = myFactory.getAntecedentesTransfusionalesDao();
		}
	}

	public void reset()
	{
		this.transfusiones = new ArrayList();
		this.observaciones = new String();
	}
	
	/**
	 * Retorna el arreglo con todas las transfusiones realizadas al paciente.
	 * @return 	ArrayList, transfusiones
	 */
	public ArrayList getTransfusiones()
	{
		return transfusiones;
	}

	/**
	 * Asigna el arreglo con todas las transfusiones realizadas al paciente.
	 * @param ArrayList, transfusiones
	 */
	public void setTransfusiones(ArrayList transfusiones)
	{
		this.transfusiones = transfusiones;
	}
	
	/**
	 * Retorna una transfusion dado su indice dentro del arreglo.
	 * @param	int, indice.
	 * @return	AntecedenteTransfusional, transfusion
	 */
	public AntecedenteTransfusional getTransfusion(int indice)
	{
		return (AntecedenteTransfusional)this.transfusiones.get(indice);
	}

	/**
	 * Adicional una transfusión al final del arreglo
	 * @param AntecedenteTransfusional, antecedente
	 */
	@SuppressWarnings("unchecked")
	public void setTransfusion(AntecedenteTransfusional antecedente)
	{
		this.transfusiones.add(antecedente);
	}
	
	/**
	 * Retorna las observaciones generales de antecedentes transfusionales
	 * @return String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones generales de antecedentes transfusionales
	 * @param String, observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}


	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
		return this.antecedentesTransfusionalesDao.existenAntecedentes(con, codigoPaciente);
	}
	
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		return this.antecedentesTransfusionalesDao.insertarAntecedenteGeneral(con, codigoPaciente);
	}

	public ResultadoBoolean existeAntecedenteTransfusionales(Connection con, int codigoPaciente)
	{
		return this.antecedentesTransfusionalesDao.existenAntecedentesTransfusionales(con, codigoPaciente);
	}

	public ResultadoBoolean insertarAntecedenteTransfusionales(Connection con, int codigoPaciente)
	{
		return this.antecedentesTransfusionalesDao.insertar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean insertarAntecedenteTransfusionalesTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesTransfusionalesDao.insertarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}

	public ResultadoBoolean modificarAntecedenteTransfusionales(Connection con, int codigoPaciente)
	{
		return this.antecedentesTransfusionalesDao.modificar(con, codigoPaciente, this.observaciones);
	}
	
	public ResultadoBoolean modificarAntecedenteTransfusionalesTransaccional(Connection con, int codigoPaciente, String estado) throws Exception
	{
		return this.antecedentesTransfusionalesDao.modificarTransaccional(con, codigoPaciente, this.observaciones, estado);
	}
	
	@SuppressWarnings("unchecked")
	public ResultadoBoolean updateTransaccional(Connection con, int codigoPaciente)  throws Exception
	{
		ResultadoBoolean resultado;
			
		// Verificar si existen otros antecedentes para este paciente, si no, se debe insertar en la tabla general.
		resultado = this.existenAntecedentes(con, codigoPaciente); 
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteGeneral(con, codigoPaciente);
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}

		// Verificar si existen algun registro de antecedentes transfusionales, si no, se debe insertar uno nuevo
		util.UtilidadBD.iniciarTransaccion(con);
		resultado = this.existeAntecedenteTransfusionales(con, codigoPaciente);
		ArrayList filtro=new ArrayList();
		filtro.add(codigoPaciente+"");
		util.UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearAntecedentesTransfusionales,filtro);
		if( !resultado.isTrue() )
		{
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = this.insertarAntecedenteTransfusionales(con, codigoPaciente);
				
				if( !resultado.isTrue() )
					return resultado;
			}
			else
				return resultado;
		}
		else
		if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
		{
			resultado = this.modificarAntecedenteTransfusionales(con, codigoPaciente);
			
			if( !resultado.isTrue() )
				return resultado;
		}
		else
			return resultado;

		// Insertar/Actualizar transfusiones
		int tamT = this.transfusiones.size();
		String estado = ConstantesBD.inicioTransaccion;
		
		for(int i=0; i<tamT; i++)
		{
			if( i!=0  )
				estado = ConstantesBD.continuarTransaccion;
				
			if( i==tamT-1 )
				estado = ConstantesBD.finTransaccion;
				
			AntecedenteTransfusional antecedente = (AntecedenteTransfusional) this.transfusiones.get(i);
				
			resultado = antecedente.existeAntecedente(con, codigoPaciente);
				
			if( resultado.isTrue() )
			{
				resultado = antecedente.modificarTransaccional(con, codigoPaciente, estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;					
			}
			else
			if( resultado.getDescripcion() == null || resultado.getDescripcion().equals("") )
			{
				resultado = antecedente.insertarTransaccional(con, codigoPaciente, estado);

				if( !resultado.isTrue() )
					if( resultado.getDescripcion() != null || !resultado.getDescripcion().equals("") )
						return resultado;				
			}
			else
				return resultado;													
		}	
		return new ResultadoBoolean(true);
	}

	public void cargar(Connection con, int codigoPaciente)
	{
		this.cargarAntecedenteTransfusionales(con, codigoPaciente);
		this.cargarTransfusiones(con, codigoPaciente);
	}	
	
	public void cargarAntecedenteTransfusionales(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesTransfusionalesDao.consultarAntecedentesTransfusionales(con, codigoPaciente);
		
		if( resultado != null )
		{
			try
			{
				if( resultado.next() )
				{
					observaciones = resultado.getString("observaciones");
					
					if(!UtilidadTexto.isEmpty(resultado.getString("fecha"))){
						fecha = UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha"));
						hora = resultado.getString("hora");
					}
				}
			}
			catch(SQLException e)
			{
				logger.warn("No se puede recorrer el ResultSetDecorator con las observaciones generales de los antecedentes transfusionales \n"+e);
			}
		}				
	}
	
	public void cargarTransfusiones(Connection con, int codigoPaciente)
	{
		ResultSetDecorator resultado = this.antecedentesTransfusionalesDao.consultarTransfusiones(con, codigoPaciente);
		
		if( resultado == null )
		{
			this.transfusiones = new ArrayList();
		}
		
		try
		{
			while( resultado.next() )
			{
				AntecedenteTransfusional antecedente = new AntecedenteTransfusional();
				antecedente.setCodigo(resultado.getInt("codigo"));
				antecedente.setComponente(resultado.getString("componente"));
				antecedente.setFecha(resultado.getString("fecha"));
				antecedente.setCausa(resultado.getString("causa"));
				antecedente.setLugar(resultado.getString("lugar"));
				antecedente.setEdad(resultado.getString("edad"));
				antecedente.setDonante(resultado.getString("donante"));
				antecedente.setObservaciones(resultado.getString("observaciones"));
				
				if(!UtilidadTexto.isEmpty(resultado.getString("fecha_ant"))){ 
					antecedente.setFecha_at(UtilidadFecha.conversionFormatoFechaStringDate(resultado.getString("fecha_ant")));
					antecedente.setHora_at(resultado.getString("hora"));
				}
				
				
				this.setTransfusion(antecedente);
			}
		}
		catch(SQLException e)
		{
			logger.warn("No se puede recorrer el ResultSetDecorator donde vienen las transfusiones \n"+e);
			this.transfusiones = null;
		}
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
