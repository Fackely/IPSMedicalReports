
package com.princetonsa.mundo.antecedentes;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import com.princetonsa.mundo.antecedentes.TipoAntecedenteFamiliar;

import com.princetonsa.dao.AntecedentesFamiliaresDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;


/** Objeto para los antecedentes familiares*/
public class AntecedentesFamiliares
{
	
	/**
		 * Paciente al cual pertence este  antecedente familiar.
		 */
		private PersonaBasica paciente;
	
	/**
	 * Código de la enfermedad familiar en la base de datos
	 */	
	
	/** Observacion por cada enfermedad*/
	private String observaciones;
	
	
	/** Lista con  los antecentes familiares predefinidos. Cada objeto, es de tipo 
	 * TipoAntecedenteFamiliar y contiene el codigo, nombre, observaciones y
	 * parentesco por cada tipo de antecedente familiar
 * @author msilva
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
	 */
	
	private ArrayList tiposAntecedentesFamiliares;
	
	
	/** Lista con  los Otros tipos de antecedentes familiares que han sido ingresado a 
	 * la base de datos. Cada objeto, es de tipo TipoAntecedenteFamiliar y
	 * contiene el codigo, nombre, observaciones y parentesco por cada tipo de
	 * antecedente familiar
	 * @author msilva
	 *
	 * To change this generated comment edit the template variable "typecomment":
	 * Window>Preferences>Java>Templates.
	 * To enable and disable the creation of type comments go to
	 * Window>Preferences>Java>Code Generation.
		 */
	private ArrayList tiposAntecedentesFamiliaresOtros;
	
	
	private AntecedentesFamiliaresDao antecedentesFamiliaresDao;

	/**
	 * Constructora de la clase. Inicializa los atributos.
	 */
	public AntecedentesFamiliares()
	{
			
		this.init(System.getProperty("TIPOBD"));
		this.clean();
	}
	
	public AntecedentesFamiliares(ArrayList  tiposAntecedentesFamiliares,ArrayList  tiposAntecedentesFamiliaresOtros, String observaciones,PersonaBasica paciente)
	{
		if (paciente==null);//Para evitar el warning
		this.init(System.getProperty("TIPOBD"));
		this.tiposAntecedentesFamiliares = tiposAntecedentesFamiliares;
		this.tiposAntecedentesFamiliaresOtros=tiposAntecedentesFamiliaresOtros;
		this.observaciones=observaciones;
		//this.paciente=paciente;
		this.paciente=new PersonaBasica();
		
		
	}
	
	/**
		 * Returns the observaciones Generales a todos los  Antecedentes Familiares.
		 * @return String
		 */
		public String getObservaciones() {
			return observaciones;
		}

		/**
		 * Sets the observaciones Generales a todos los Antecedentes Familiares.
		 * @param observaciones The observaciones to set
		 */
		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}

	/**
		 * Returns the paciente.
		 * @return PersonaBasica
		 */
		public PersonaBasica getPaciente() {
			return paciente;
		}

		/**
		 * Sets the paciente.
		 * @param paciente The paciente to set
		 */
		public void setPaciente(PersonaBasica paciente) {
			this.paciente = paciente;
		}
		
		
	
		/**
		 * Sets the tipoAntecedenteFamiliar.(La lista de antecedentes)
		 * @param tipoAntecedenteFamiliar The tipoAntecedenteFamiliar to set
		 */
		public void setTiposAntecedentesFamiliares(ArrayList tiposAntecedentesFamiliares) {
			this.tiposAntecedentesFamiliares = tiposAntecedentesFamiliares;
		}

		/**
		 * Sets the tipoAntecedenteFamiliarOtro.(La lista de antecedentes)
		 * @param tipoAntecedenteFamiliarOtro The tipoAntecedenteFamiliarOtro to set
		 */
		public void setTiposAntecedentesFamiliaresOtros(ArrayList tiposAntecedentesFamiliaresOtros) {
			this.tiposAntecedentesFamiliaresOtros = tiposAntecedentesFamiliaresOtros;
		
		
		}
	
	
		/**
				 * Retorna el antecedente familiar adicional dado su indice
				 * dentro de la lista.
				 * @param  int, indice
				 * @return	AntecedenteFamiliar, antecedente correspondiente al indice
				 * dado o null de lo contrario
				 */
				public TipoAntecedenteFamiliar getTipoAntecedenteFamiliarOtro(int indice)
				{
					return (TipoAntecedenteFamiliar)this.tiposAntecedentesFamiliaresOtros.get(indice);
				}
	
				/**
				 * Asigna el antecedente familiar adicional al final de la lista
				 * de antecedentes familiares predefinidos
				 * @param TipoAntecedenteFamiliar, antecedente
				 */
				public void setTipoAntecedentefamiliarOtro(TipoAntecedenteFamiliar  antecedente)
				{
					this.tiposAntecedentesFamiliaresOtros.add(antecedente);
				}
	

	/**
					 * Retorna el antecedente familiar predefinido dado su
					 * indice dentro de la lista.
					 * @param  int, indice
					 * @return	AntecedenteFamiliar, antecedente correspondiente al indice
					 * dado o null de lo contrario
					 */
					public TipoAntecedenteFamiliar getTipoAntecedenteFamiliar(int indice)
					{
						return (TipoAntecedenteFamiliar)this.tiposAntecedentesFamiliares.get(indice);
					}
	
					/**
					 * Asigna el antecedente familiar predefinido al final de la lista
					 * de antecedentes familiares predefinidos
					 * @param TipoAntecedenteFamiliar, antecedente
					 */
					public void setTipoAntecedentefamiliar(TipoAntecedenteFamiliar  antecedente)
					{
						this.tiposAntecedentesFamiliares.add(antecedente);
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
			//paciente = new PersonaBasica();
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

			if (myFactory != null) {
				antecedentesFamiliaresDao = myFactory.getAntecedentesFamiliaresDao();
				wasInited = (antecedentesFamiliaresDao != null);
			}

			return wasInited;
		}
	
		public void clean()	
		{
			this.paciente = new PersonaBasica();
			
			this.observaciones = "";
			this.tiposAntecedentesFamiliares=new ArrayList();
			this.tiposAntecedentesFamiliaresOtros=new ArrayList();	
			
				
		}
		
	public boolean cargar(Connection con)throws SQLException
	{
		ResultSetDecorator famGeneral_rs=this.antecedentesFamiliaresDao.cargarFamiliares(con, this.paciente.getCodigoPersona()); 
			
		if (famGeneral_rs.next())
		{
			ArrayList arrayPredefinidos = new ArrayList();
			ArrayList arrayOtros = new ArrayList();
			int codigo;
			String nombre,observaciones,parentesco;
			this.observaciones=famGeneral_rs.getString("observaciones");

			ResultSetDecorator famOtros_rs=this.antecedentesFamiliaresDao.cargarFamiliaresOtros(con,this.paciente.getCodigoPersona());

			while(famOtros_rs.next())
			{
				codigo=famOtros_rs.getInt("codigo");
				nombre=famOtros_rs.getString("nombre");
				observaciones=famOtros_rs.getString("observaciones");
				parentesco=famOtros_rs.getString("parentesco");
				TipoAntecedenteFamiliar	tipoOtro=new TipoAntecedenteFamiliar(codigo,nombre,observaciones,parentesco);
				arrayOtros.add(tipoOtro);
																		
				this.setTiposAntecedentesFamiliaresOtros(arrayOtros);
			}

			ResultSetDecorator famPredefinidos_rs=this.antecedentesFamiliaresDao.cargarFamiliaresTipos(con,this.paciente.getCodigoPersona());
		 	while(famPredefinidos_rs.next())
		 	{
	 			codigo=famPredefinidos_rs.getInt("tipo_enfermedad_familiar");
	 			nombre=famPredefinidos_rs.getString("nombre");
	 			observaciones=famPredefinidos_rs.getString("observaciones");
				parentesco=famPredefinidos_rs.getString("parentesco");
				TipoAntecedenteFamiliar	tipo=new TipoAntecedenteFamiliar(codigo,nombre,observaciones,parentesco);
	 			arrayPredefinidos.add(tipo);
				this.setTiposAntecedentesFamiliares(arrayPredefinidos);
		 	}
			return true;
	 	}
		 return false;	
	}
		
	public  int insertarFamiliares(Connection con)throws SQLException
	{	
		int resp=0;
		resp=this.antecedentesFamiliaresDao.insertarFamiliares(con, paciente.getCodigoPersona(),this.observaciones);
		if (resp==0)
		{
			return 0;	
		}

		for (int i=0; i < this.tiposAntecedentesFamiliares.size(); i++)		
		{ 
			TipoAntecedenteFamiliar  tipoFamiliar  = (TipoAntecedenteFamiliar)this.tiposAntecedentesFamiliares.get(i);
		
			resp=this.antecedentesFamiliaresDao.insertarFamiliaresTipos(con,paciente.getCodigoPersona(),tipoFamiliar.getCodigo(),tipoFamiliar.getObservaciones(),tipoFamiliar.getParentesco());
			if(resp==1)
			if (resp==0)
			{
				return 0;
			}
		}

	   for (int i=0; i < this.tiposAntecedentesFamiliaresOtros.size(); i++)
	   { 
			TipoAntecedenteFamiliar  tipoFamiliarOtro = (TipoAntecedenteFamiliar)this.tiposAntecedentesFamiliaresOtros.get(i);
		   	resp=this.antecedentesFamiliaresDao.insertarFamiliaresOtros(con,paciente.getCodigoPersona(),tipoFamiliarOtro.getCodigo(),tipoFamiliarOtro.getNombre(),tipoFamiliarOtro.getObservaciones(),tipoFamiliarOtro.getParentesco());
			if (resp==0)
			{
				return resp;
			}
		}
		return resp;
	}

	
	public  int  modificar(Connection con)throws SQLException
	{
		int resp=0;
		
		resp=this.antecedentesFamiliaresDao.modificarFamiliares(con,paciente.getCodigoPersona(),this.observaciones);
		if (resp==0)
		{
			return 0;	
		}
		for (int i=0; i < this.tiposAntecedentesFamiliares.size(); i++)
		{ 
			TipoAntecedenteFamiliar  tipoFamiliar  = (TipoAntecedenteFamiliar)this.tiposAntecedentesFamiliares.get(i);
			if(!tipoFamiliar.isIngresadoBD())
			{
				resp=this.antecedentesFamiliaresDao.insertarFamiliaresTipos(con,paciente.getCodigoPersona(),tipoFamiliar.getCodigo(),tipoFamiliar.getObservaciones(),tipoFamiliar.getParentesco());
			}
			if (resp==0)
			{
				return 0;
			}
			else 
			{
				resp=this.antecedentesFamiliaresDao.modificarFamiliaresTipos(con,paciente.getCodigoPersona(),tipoFamiliar.getCodigo(),tipoFamiliar.getObservaciones());
			}
			if(resp==1)
			if (resp==0)
			{
				return 0;
			}
		}
			 
	   for (int i=0; i < this.tiposAntecedentesFamiliaresOtros.size(); i++)
	   { 
			TipoAntecedenteFamiliar  tipoFamiliarOtro = (TipoAntecedenteFamiliar)this.tiposAntecedentesFamiliaresOtros.get(i);

			if(!tipoFamiliarOtro.isIngresadoBD())
			{
				resp=this.antecedentesFamiliaresDao.insertarFamiliaresOtros(con,paciente.getCodigoPersona(),tipoFamiliarOtro.getCodigo(),tipoFamiliarOtro.getNombre(),tipoFamiliarOtro.getObservaciones(),tipoFamiliarOtro.getParentesco());
			}
			if (resp==0)
			{
				return 0;
			}
			else 
			{
				resp=this.antecedentesFamiliaresDao.modificarFamiliaresOtros(con,paciente.getCodigoPersona(),tipoFamiliarOtro.getCodigo(),tipoFamiliarOtro.getObservaciones());
			}
			if (resp==0)
			{
				return resp;
			}
		}
		return resp;
	}
	
	/**
	 * Returns the tiposAntecedentesFamiliares.
	 * @return ArrayList
	 */
	public ArrayList getTiposAntecedentesFamiliares() {
		return this.tiposAntecedentesFamiliares;
	}

	/**
	 * Returns the tiposAntecedentesFamiliaresOtros.
	 * @return ArrayList
	 */
	public ArrayList getTiposAntecedentesFamiliaresOtros() {
		return this.tiposAntecedentesFamiliaresOtros;
	}

}
