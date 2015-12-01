/*
 * @(#)AntecedenteAlergias.java
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

import util.InfoDatosBD;

import com.princetonsa.dao.AntecedentesAlergiasDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Clase para el manejo de todos los datos de antecedentes de alergias.
 *
 * @version 1.0, Julio 31, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see com.princetonsa.mundo.antecedentes.CategoriaAlergia
 */
@SuppressWarnings({"rawtypes","deprecation"})
public class AntecedentesAlergias
{
	/**
	 * Paciente al cual pertence este antecedente de alergias.
	 */
	private PersonaBasica paciente;
	
	/**
	 * Lista con todas las categorias de alergias que tiene el paciente. 
	 * Se compone de objetos de tipo "CategoriaAlergia", que contienen el código
	 * de la categoria de alergia en la base de datos, el nombre y una lista con
	 * cada una de las alergias que hagan parte de esa categoria.
	 */
	private ArrayList categoriasAlergias;
	
	/**
	 * Observaciones generales para antecedentes alergias
	 */
	private String observaciones;
	private AntecedentesAlergiasDao antecedentesAlergiasDao;
	
	/**
	 * Constructor que inicializa los atributos de esta clase
	 */
	public AntecedentesAlergias()
	{
		this.init(System.getProperty("TIPOBD"));
		this.clean();				
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

		if (myFactory != null) {
			antecedentesAlergiasDao = myFactory.getAntecedentesAlergiasDao();
			wasInited = (antecedentesAlergiasDao != null);
		}

		return wasInited;
	}
	
	public void clean()	
	{
		paciente = new PersonaBasica();
		categoriasAlergias = new ArrayList();
		observaciones = "";		
	}
	

	public int insertar(Connection con) throws SQLException {
		CategoriaAlergia alergiasCat;
		InfoDatosBD ale;
		int resp = 0;
		resp = this.antecedentesAlergiasDao.insertarAntecedentesAlergias(con, this.paciente.getCodigoPersona(),  this.observaciones);
		if(resp == 0 ){
			return 0;
		}
		for(int i=0; i<this.categoriasAlergias.size(); i++){
			alergiasCat = (CategoriaAlergia)this.categoriasAlergias.get(i);
			for (int j=0; j < alergiasCat.getTiposAlergiasPredefinidas().size(); j++)
			{	
				ale = alergiasCat.getAlergiaPredefinida(j);		
				resp = this.antecedentesAlergiasDao.insertarAlergiasPredef(con, this.paciente.getCodigoPersona(), ale.getCodigo(), ale.getDescripcion());
				if(resp == 0 ){
					return 0;
				}				
			}
			for (int j=0; j < alergiasCat.getTiposAlergiasAdicionales().size(); j++)
			{	
				ale = alergiasCat.getAlergiaAdicional(j);
				this.antecedentesAlergiasDao.insertarAlergiasAdic(con, this.paciente.getCodigoPersona(), alergiasCat.getCodigo(), ale.getCodigo(), ale.getNombre(), ale.getDescripcion());
				if(resp == 0 ){
					return 0;
				}			
								
			}	
		}
		return 1;
	}
		
	@SuppressWarnings("unchecked")
	public boolean cargar(Connection con) throws SQLException {
		ResultSetDecorator rs_antAle = this.antecedentesAlergiasDao.cargarAntecedentesAlergias(con, this.paciente.getCodigoPersona());
		ResultSetDecorator rs_alePredef;
		ResultSetDecorator rs_aleAdic;
		int codigoCategoria;
		int codigoCategoriaAnterior = -2;
		CategoriaAlergia categoriaAlergia = new CategoriaAlergia();
		ArrayList alergias = new ArrayList();
		if(rs_antAle.next()){
			
			this.setObservaciones(rs_antAle.getString("observaciones"));
			rs_alePredef = this.antecedentesAlergiasDao.cargarAlergiasPredef(con, this.paciente.getCodigoPersona());
			rs_aleAdic = this.antecedentesAlergiasDao.cargarAlergiasAdic(con, this.paciente.getCodigoPersona());
			//Cargando en el objeto las alergias predefinidas
			
			while(rs_alePredef.next())
			{
				codigoCategoria = rs_alePredef.getInt("categoria");
				
				if(codigoCategoriaAnterior != codigoCategoria)
				{
					codigoCategoriaAnterior = codigoCategoria;			
					categoriaAlergia = new CategoriaAlergia(codigoCategoria, rs_alePredef.getString("nombreCategoria"), new ArrayList(), new ArrayList());
					
					alergias.add(categoriaAlergia);	
				}
				categoriaAlergia.getTiposAlergiasPredefinidas().add(new InfoDatosBD(rs_alePredef.getInt("tipo"), rs_alePredef.getString("nombreTipo"), rs_alePredef.getString("observaciones") ));
				
			}
			//Cargando en el objeto las alergias adicionales
			codigoCategoriaAnterior = -2;
			boolean categoriaEncontrada = false;
			int indiceAlergia = -1;
			
			while(rs_aleAdic.next())
			{
				codigoCategoria = rs_aleAdic.getInt("categoria");
				
				if(codigoCategoriaAnterior != codigoCategoria)
				{
				codigoCategoriaAnterior = codigoCategoria;
					//Buscando la categoria de la alergia adicional
					for(int i=indiceAlergia+1; i<alergias.size(); i++)
					{
						if(((CategoriaAlergia)alergias.get(i)).getCodigo() == codigoCategoria)
						{
							indiceAlergia = i;
							i= alergias.size();	
							categoriaEncontrada = true;		
						}
					}
					//Si no se encontro la adiciono
					if(!categoriaEncontrada)
					{
						categoriaAlergia = new CategoriaAlergia(codigoCategoria, rs_aleAdic.getString("nombreCategoria"), new ArrayList(), new ArrayList());
						
						alergias.add(categoriaAlergia);
						indiceAlergia = alergias.size()-1; 						
					}
					categoriaEncontrada = false;
				}
				((CategoriaAlergia)alergias.get(indiceAlergia)).getTiposAlergiasAdicionales().add(new InfoDatosBD(rs_aleAdic.getInt("codigo"), rs_aleAdic.getString("nombre"), rs_aleAdic.getString("observaciones") ));
				
			}
			this.setCategoriasAlergias(alergias);
			return true;				
		}
		return false;
	}	
	/**
	 * Tiene en cuenta dos casos: 
	 * 1. Una alergia ya esta en la base de datos y desea modificarla
	 * 2. Una alergia no esta en la base de datos en cuyo caso debe insertarla 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificar(Connection con) throws SQLException 
	{
		CategoriaAlergia categoriaAlergia;
		InfoDatosBD ale;
		int resp = 0;	
		resp = this.antecedentesAlergiasDao.modificarAntecedentesAlergias(con, paciente.getCodigoPersona(),  this.observaciones);
		if(resp == 0)
		{
			return 0;
		}
		for(int i=0; i<this.categoriasAlergias.size(); i++)
		{
			categoriaAlergia = (CategoriaAlergia)this.categoriasAlergias.get(i);			
			for(int j=0; j<categoriaAlergia.getTiposAlergiasPredefinidas().size(); j++)
			{
				ale = categoriaAlergia.getAlergiaPredefinida(j);
				//Ya esta en la bd
				if(ale.getEstaEnBDBoolean())
				{
					resp = this.antecedentesAlergiasDao.modificarAlergiasPredef(con, this.paciente.getCodigoPersona(), ale.getCodigo(), ale.getDescripcion());
					if(resp == 0)
					{
						return 0;
					}						
				}
				//No esta en la bd
				else
				{
					this.antecedentesAlergiasDao.insertarAlergiasPredef(con, this.paciente.getCodigoPersona(),  ale.getCodigo(), ale.getDescripcion());
					if(resp == 0)
					{
						return 0;
					}										
				}
			}
			for(int j=0; j<categoriaAlergia.getTiposAlergiasAdicionales().size(); j++)
			{
				ale = categoriaAlergia.getAlergiaAdicional(j);
				//Ya esta en la bd
				if(ale.getEstaEnBDBoolean())
				{
					this.antecedentesAlergiasDao.modificarAlergiasAdic(con, this.paciente.getCodigoPersona(),  categoriaAlergia.getCodigo(), ale.getCodigo(), ale.getNombre(), ale.getDescripcion());
					if(resp == 0)
					{
						return 0;
					}																		
				}
				//No esta en la bd
				else
				{										
					this.antecedentesAlergiasDao.insertarAlergiasAdic(con, this.paciente.getCodigoPersona(), categoriaAlergia.getCodigo(), ale.getCodigo(), ale.getNombre(), ale.getDescripcion());
					if(resp == 0)
					{
						return 0;
					}																						
				}
			}					
		}
		return 1;
	}	
	/**
	 * Constructor que inicializa los atributos de esta clase con los valores
	 * dados.
	 * @param	PersonaBasica, paciente
	 * @param	ArrayList (CategoriaAlergia), alergias
	 * @param	String, observaciones
	 */
	public AntecedentesAlergias(PersonaBasica paciente, ArrayList categoriasAlergias, String observaciones)
	{
		this.paciente = paciente;
		this.categoriasAlergias = categoriasAlergias;
		this.observaciones = observaciones;
	}
	
	
	/**
	 * Retorna el paciente al cual pertence este antecedente de alergias.
	 * @return 		PersonaBasica, paciente
	 */
	public PersonaBasica getPaciente()
	{
		return paciente;
	}

	/**
	 * Asigna el paciente al cual pertencen este antecedente de alergias.
	 * @param 	PersonaBasica, paciente
	 */
	public void setPaciente(PersonaBasica paciente)
	{
		this.paciente = paciente;
	}

	/**
	 * Retorna la lista con todas las categorias de alergias que tiene el paciente. 
	 * Se compone de objetos de tipo "CategoriaAlergia", que contienen el código
	 * de la categoria de alergia en la base de datos, el nombre y una lista con
	 * cada una de las alergias que hagan parte de esa categoria.
	 * @return 		ArrayList, alergias
	 */
	public ArrayList getCategoriasAlergias()
	{
		return categoriasAlergias;
	}

	/**
	 * Asigna la lista con todas las categorias de alergias que tiene el paciente. 
	 * Se compone de objetos de tipo "CategoriaAlergia", que contienen el código
	 * de la categoria de alergia en la base de datos, el nombre y una lista con
	 * cada una de las alergias que hagan parte de esa categoria.
	 * @param 	ArrayList (CategoriaAlergia), alergias
	 */
	public void setCategoriasAlergias(ArrayList alergias)
	{
		this.categoriasAlergias = alergias;
	}
	
	/**
	 * Retorna una categoria de alergias dado su indice.
	 * @param	int, indice de la alergia
	 * @return	CategoriaAlergia, alergia que corresponde a dicho indice, null si no
	 * 				hay
	 */
	public CategoriaAlergia getCategoriaAlergia(int indice)
	{
		return (CategoriaAlergia)categoriasAlergias.get(indice);
	}
	
	/**
	 * Adiciona una categoria de alergias al final del arreglo.
	 * @param	CategoriaAlergia, categoria de alergia a adicionar
	 */
	@SuppressWarnings("unchecked")
	public void setCategoriaAlergia(CategoriaAlergia categoriaAlergia)
	{
		categoriasAlergias.add(categoriaAlergia);		
	}
	/**
	 * Retorna las observaciones generales para antecedentes alergias
	 * @return 	String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones generales para antecedentes alergias
	 * @param String, observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

}
