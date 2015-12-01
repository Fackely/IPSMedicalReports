/*
 * @(#)InformacionParametrizable.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.mundo.informacionParametrizable;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.InformacionParametrizableDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase que permite manejar información Parametrizable de forma generica
 * (En cuatro sencillos pasos se puede agregar la capacidad de manejar 
 * campos parametrizables a una funcionalidad en particular)
 *
 * @version 1.0, Oct 29, 2003
 */
public class InformacionParametrizable
{
	/**
	 * Código en la tabla a la cual va asociada esta información parametrizable 
	 */
	private int codigoTabla;
	
	/**
	 * Código de la funcionalidad parametrizable
	 */
	private int codigoFuncionalidadParametrizable;
	
	/**
	 * Colección de secciones para la funcionalidad definida con el atributo
	 * codigoFuncionalidadParametrizable
	 */
	private Collection seccionesParametrizables;
	
	/**
	 * Atributo privado para acceder a la funcionalidad de persistencia propia 
	 * de este objeto
	 */
	private static InformacionParametrizableDao informacionParametrizableDao;

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
			informacionParametrizableDao = myFactory.getInformacionParametrizableDao();
			wasInited = (informacionParametrizableDao != null);
		}

		return wasInited;

	}
	
	/**
	 * Constructor de este objeto
	 *
	 */
	public InformacionParametrizable()
	{
		clean();
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Método que limpia este objeto
	 *
	 */
	public void clean ()
	{
		codigoTabla=0;
		codigoFuncionalidadParametrizable=0;
		seccionesParametrizables=new LinkedList();
	}
	
	/**
	 * Método que inserta secciones y nuevos campos parametrizados por un médico
	 * en particular
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapa Mapa con los datos viejos y nuevos
	 * @param medico Médico que esta parametrizando (Agregando nuevos datos)
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarNuevosCamposParametrizados (Connection con, HashMap mapa, UsuarioBasico medico) throws SQLException
	{
		if (con==null||medico==null||mapa==null)
		{
			return false;
		}
		int numeroCampos = 0, numeroCamposBD = 0, i;
		numeroCampos = Integer.parseInt((String) mapa.get("numeroCampos"));
		numeroCamposBD = Integer.parseInt((String) mapa.get("numeroCamposBD"));

		int orden, codigo, codigoTipo, codigoSeccion;
		boolean activo; 
		
		String strTemporal="", nombre;
		if (numeroCampos == 1)
		{
			orden = Integer.parseInt((String)mapa.get("orden_1"));
			strTemporal = (String) mapa.get("activo_1");
			if (strTemporal == null)
			{
				activo=false;
			}
			else if (strTemporal.equals("true")||strTemporal.equals("t"))
			{
				activo=true;
			}
			else
			{
				activo=false;
			}
			if (numeroCamposBD==1)
			{
				int centroCosto = 0;
				if(Integer.parseInt(mapa.get("alcance_1")+"")==ConstantesBD.codigoAlcanceCentroCosto)
				{
				    if(mapa.containsKey("centrocosto_1" ))
					{
						if((mapa.get("centrocosto_1")+"").equals("undefined") || (mapa.get("centrocosto_1")+"").equals("null") || (mapa.get("centrocosto_1")+"").equals(""))
						{
							centroCosto =  medico.getCodigoCentroCosto();
						}
						else
						{
							centroCosto = Integer.parseInt(mapa.get("centrocosto_1")+"");
						}
					}
					else
					{
						centroCosto =  medico.getCodigoCentroCosto();
					}
				}
				else
				{
					centroCosto =  medico.getCodigoCentroCosto();
				}
				//Solo hay que actualizar
				codigo=Integer.parseInt((String)mapa.get("codigo_1"));
				if (informacionParametrizableDao.actualizarCampoParametrizado(con, activo, codigo, Integer.parseInt((String)mapa.get("alcance_1")), centroCosto)>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				 nombre=(String) mapa.get("nombre_1");
				 codigoTipo=Integer.parseInt((String)mapa.get("codigoTipo_1"));
				 codigoSeccion=Integer.parseInt((String)mapa.get("codigoSeccion_1"));
				 int centroCosto = 0;
				 if(Integer.parseInt(mapa.get("alcance_1")+"")==ConstantesBD.codigoAlcanceCentroCosto)
				 {
					if(mapa.containsKey("centrocosto_1" ))
					{
						if((mapa.get("centrocosto_1")+"").equals("undefined") || (mapa.get("centrocosto_1")+"").equals("null") || (mapa.get("centrocosto_1")+"").equals(""))
						{
							centroCosto =  medico.getCodigoCentroCosto();
						}
						else
						{
							centroCosto = Integer.parseInt(mapa.get("centrocosto_1")+"");
						}
					}
					else
					{
						centroCosto =  medico.getCodigoCentroCosto();
					}
				 }
				 else
				 {
					 centroCosto =  medico.getCodigoCentroCosto();
				 }
				 if (informacionParametrizableDao.insertarCampoParametrizado(con, orden, codigoSeccion, codigoTipo, nombre, medico.getCodigoPersona(),  activo, centroCosto, medico.getCodigoInstitucion(), Integer.parseInt((String)mapa.get("alcance_1")))>0)
				 {
				 	return true;
				 }
				 else
				 {
				 	return false;
				 }
			}
		}
		else
		{
			for (i = 1 ; i <= numeroCamposBD ; i++)
			{
			    int alcance=Integer.parseInt((String)mapa.get("alcance_" + i));
				orden=Integer.parseInt((String)mapa.get("orden_" + i));
				strTemporal=(String) mapa.get("activo_" + i);
				int centroCosto = 0;
				if(alcance == ConstantesBD.codigoAlcanceCentroCosto)
				{
				    if(mapa.containsKey("centrocosto_"+i ))
					{
						if((mapa.get("centrocosto_" + i)+"").equals("undefined") || (mapa.get("centrocosto_"+i)+"").equals("null") || (mapa.get("centrocosto_"+i)+"").equals(""))
						{
							centroCosto =  medico.getCodigoCentroCosto();
						}
						else
						{
							centroCosto = Integer.parseInt(mapa.get("centrocosto_"+i)+"");
						}
					}
					else
					{
						centroCosto =  medico.getCodigoCentroCosto();
					}
				}
				else
				{
					centroCosto =  medico.getCodigoCentroCosto();
				}
				
				if (strTemporal==null)
				{
					activo=false;
				}
				else if (strTemporal.equals("true")||strTemporal.equals("t"))
				{
					activo=true;
				}
				else
				{
					activo=false;
				}
				codigo=Integer.parseInt((String)mapa.get("codigo_"+i));
				
				
				if (i==1)
				{
					informacionParametrizableDao.actualizarCampoParametrizadoTransaccional(con, activo, codigo, alcance, centroCosto, "empezar");
				}
				else if (i==numeroCampos)
				{
					informacionParametrizableDao.actualizarCampoParametrizadoTransaccional(con, activo, codigo, alcance, centroCosto, "finalizar");
				}
				else
				{
					informacionParametrizableDao.actualizarCampoParametrizadoTransaccional(con, activo, codigo, alcance, centroCosto, "continuar");
				}
				
			}
			
			for (i = numeroCamposBD+1 ; i <= numeroCampos ; i++)
			{
			    int alcance=Integer.parseInt((String)mapa.get("alcance_" + i));
			    
			    //@todo: Super Cambio. Parte 1!!
				orden=i;
				
				strTemporal=(String) mapa.get("activo_" + i);
				if (strTemporal==null)
				{
					activo=false;
				}
				else if (strTemporal.equals("true")||strTemporal.equals("t"))
				{
					activo=true;
				}
				else
				{
					activo=false;
				}
				nombre = (String) mapa.get("nombre_" + i);
				codigoTipo = Integer.parseInt((String)mapa.get("codigoTipo_" + i));
				codigoSeccion = Integer.parseInt((String)mapa.get("codigoSeccion_" + i));
				int centroCosto = 0; 
				if(mapa.containsKey("centrocosto_" + i))
				{
					if((mapa.get("centrocosto_"+i)+"").equals("undefined") || (mapa.get("centrocosto_"+i)+"").equals("null") || (mapa.get("centrocosto_"+i)+"").equals(""))
					{
						centroCosto =  medico.getCodigoCentroCosto();
					}
					else
					{
						centroCosto = Integer.parseInt(mapa.get("centrocosto_"+i)+"");
					}
				}
				else
				{
					centroCosto =  medico.getCodigoCentroCosto();
				}
				if (i==1)
				{
					informacionParametrizableDao.insertarCampoParametrizadoTransaccional(con, orden, codigoSeccion, codigoTipo, nombre, medico.getCodigoPersona(), activo, centroCosto, medico.getCodigoInstitucion(), alcance, "empezar");
				}
				else if (i==numeroCampos)
				{
					informacionParametrizableDao.insertarCampoParametrizadoTransaccional(con, orden, codigoSeccion, codigoTipo, nombre, medico.getCodigoPersona(), activo, centroCosto, medico.getCodigoInstitucion(), alcance, "finalizar");
				}
				else
				{
					informacionParametrizableDao.insertarCampoParametrizadoTransaccional(con, orden, codigoSeccion, codigoTipo, nombre, medico.getCodigoPersona(), activo, centroCosto, medico.getCodigoInstitucion(), alcance,  "continuar");
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Método que carga en el objeto las secciones y campos que PUEDEN ser
	 * llenados por un médico en particular. Estos datos quedan llenos en un mapa
	 * que da el usuario (En general una clase Action)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapa Mapa en que se quieren guardar los datos que pueden ser
	 * llenados por un médico en particular
	 * @param medico Médico para el cual se le desean buscar sus datos básicos
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarCamposAnteriores (Connection con, HashMap mapa, UsuarioBasico medico) throws SQLException
	{
		if (con==null||medico==null||mapa==null)
		{
			return false;
		}
		int i=0;

		ResultSetDecorator rs=informacionParametrizableDao.buscarCamposParametrizadosPreviamente(con, medico.getCodigoPersona(), medico.getCodigoInstitucionInt());
		while (rs.next())
		{
			i++;
			mapa.put("nombre_" + i, rs.getString("nombre"));
			mapa.put("tipo_" + i, rs.getString("tipo"));
			mapa.put("codigoTipo_" + i, rs.getString("codigoTipo"));
			mapa.put("funcionalidad_" + i, rs.getString("funcionalidad"));
			mapa.put("codigoFuncionalidad_" + i, rs.getString("codigoFuncionalidad"));
			mapa.put("seccion_" + i, rs.getString("seccion"));
			mapa.put("codigoSeccion_" + i, rs.getString("codigoSeccion"));
			mapa.put("orden_" + i, rs.getString("orden"));
			mapa.put("activo_" + i, rs.getBoolean("activo") + "");
			mapa.put("centrocosto_" + i, rs.getString("centrocosto") + "");
			mapa.put("codigo_" + i, rs.getString("codigo"));
			mapa.put("alcance_"+i, rs.getString("codigoAlcanceCampo"));
		}
		
		mapa.put("numeroCampos", ""+i);
		mapa.put("numeroCamposBD", ""+i );

		if (i==0)
		{
			return false;
		}
		else
		{
			return true;
		}

	}
	
	/**
	 * Método que recibe un mapa con la información llenada desde un Action en
	 * un HashMap y llena esta Información Parametrizable con todas las secciones
	 * que contengan al menos uno de los campos parametrizables llenos. (Solo
	 * intenta capturar los campos definidos por el médico para esta funcionalidad) 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapa Mapa con los datos llenos desde el Action
	 * @param medico Médico que quiere llenar los datos parametrizables
	 * @return
	 * @throws SQLException
	 */
	public boolean inicializarParaInsercion(Connection con, HashMap mapa, UsuarioBasico medico,int codigoCentroCosto) throws SQLException
	{
		//Primero intentamos inicializar el codigo de la tabla
		if (mapa==null||mapa.get("codigoTabla")==null||mapa.get("codigoFuncionalidadParametrizable")==null||medico==null)
		{
			return false;
		}
		
		//Ahora revisamos si es un paramétro entero
		try
		{
			codigoTabla=Integer.parseInt(    ((String) mapa.get("codigoTabla"))      );
			codigoFuncionalidadParametrizable=Integer.parseInt(    ((String) mapa.get("codigoFuncionalidadParametrizable"))      );
		}
		catch (Exception e)
		{
			return false;
		}
		
		//Llegados a este punto, tenemos un dato válido
		//y empezamos a construir la estructura del objeto basandonos 
		//en este dato
		
		ResultSetDecorator rs=informacionParametrizableDao.buscarSeccionesYCamposDadaFuncionalidad(con, codigoFuncionalidadParametrizable, medico.getCodigoPersona(), codigoCentroCosto, medico.getCodigoInstitucion());
		int codigoSeccionAnterior=0, codigoSeccionActual, ordenCampo;
		String elementoEnMapa;
		SeccionParametrizable seccionAnterior=new SeccionParametrizable();
		while (rs.next())
		{
			codigoSeccionActual=rs.getInt("codigoSeccion");
			//Lo inicializo dentro del while para manejar referencias diferentes
			//cuando es una nueva sección
			SeccionParametrizable seccionTemp;
			
			if (codigoSeccionActual!=codigoSeccionAnterior)
			{
				//Debemos crear una nueva sección
				seccionTemp=new SeccionParametrizable(codigoSeccionActual, rs.getString("seccion"));
				//y agregarla a la colección
				agregarSeccionParametrizable(seccionTemp);
			}
			else
			{
				seccionTemp=seccionAnterior;
			}
			codigoSeccionAnterior=codigoSeccionActual;
			//Nuestra nueva seccionAnterior es seccionTemp
			seccionAnterior=seccionTemp;
			
			//Ahora empezamos el trabajo con los campos
			//Primero creamos el campo dinamico


			ordenCampo=rs.getInt("ordenCampo");
			elementoEnMapa=(String)mapa.get( codigoSeccionActual + "-" + ordenCampo);
			if (elementoEnMapa!=null&&!elementoEnMapa.equals(""))
			{
				CampoParametrizable campoTemp=new CampoParametrizable (rs.getInt("codigoCampo"), ordenCampo, rs.getString("campo"), elementoEnMapa);
				seccionTemp.agregarCampoParametrizable(campoTemp);
			}

		}
		limpiarSecciones();
		//Si no me he salido antes es porque todo salio bien
		return true;
	}
	
	/**
	 * Método que permite insertar todas las secciones de esta información
	 * parametrizable, permitiendo recibir el estado de la transacción
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción a manejar
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarTransaccional (Connection con, String estado) throws SQLException
	{
		Iterator iterador=seccionesParametrizables.iterator();
		int numeroSecciones=seccionesParametrizables.size();
		int i=1, j=1, numeroCampos;
		boolean yaEmpeze=false;
		while (iterador.hasNext())
		{
			SeccionParametrizable sec=(SeccionParametrizable)iterador.next();
			Iterator iterador2=sec.getIteradorCamposParametrizables();
			numeroCampos=sec.getNumeroCamposParametrizables();
			j=1;

			while (iterador2.hasNext())
			{
				InfoDatosInt campo=(InfoDatosInt) iterador2.next();
				
				if (estado.equals("empezar")&&!yaEmpeze)
				{
					yaEmpeze=true;
					informacionParametrizableDao.insertarLlenadoCampoTransaccional(con, codigoTabla, campo.getCodigo(), campo.getDescripcion(), "empezar");
				}
				else if (estado.equals("finalizar")&&i==numeroSecciones&&j==numeroCampos)
				{
					informacionParametrizableDao.insertarLlenadoCampoTransaccional(con, codigoTabla, campo.getCodigo(), campo.getDescripcion(), "finalizar");
				}
				else
				{
					informacionParametrizableDao.insertarLlenadoCampoTransaccional(con, codigoTabla, campo.getCodigo(), campo.getDescripcion(), "continuar");
				}
				j++;
			}
			
			i++;
		}
		
		if (numeroSecciones==0)
		{
			//Si el número de secciones es 0 quiere decir que no había ningún campo
			//(Al utilizar el método de limpiar nos aseguramos que no haya secciones
			//sin campos. En las pruebas hay una sección con este caso).
			//Si el estado es continuar no hay nada más que hacer, en caso de empezar
			//y finalizar si hay un par de cosas por hacer
			if (estado.equals("empezar"))
			{
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			}
			else if (estado.equals("finalizar"))
			{
			    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
				return true;
			}
			
		}
		
		return true;
	}
	
	/**
	 * Método que retorna un iterador para las secciones parametrizables
	 * @return
	 */
	public Iterator getIteradorSeccionesParametrizables()
	{
		return seccionesParametrizables.iterator();
	}

	/**
	 * Método privado que añade una nueva sección a esta información
	 * parametrizable
	 * 
	 * @param seccion
	 */
	private void agregarSeccionParametrizable(SeccionParametrizable seccion)
	{
		seccionesParametrizables.add(seccion);
	}
	
	/**
	 * Método que retorna el número de secciones parametrizables
	 * 
	 * @return
	 */
	public int getNumeroSeccionesParametrizables()
	{
		return seccionesParametrizables.size();
	}
	
	/**
	 * Método que se encarga de limpiar las secciones que no tengan
	 * campos dentro de sí  (al inicializar, se busca en la BD los campos 
	 * que pudo llenar el médico, sin embargo todos estos NO se insertan,
	 * solo los que haya llenado el médico). 
	 *
	 */
	private void limpiarSecciones()
	{
		Iterator iterador=seccionesParametrizables.iterator();
		LinkedList aEliminar=new LinkedList();
		
		while (iterador.hasNext())
		{
			SeccionParametrizable sec=(SeccionParametrizable) iterador.next();
			if (sec.getNumeroCamposParametrizables()==0)
			{
				aEliminar.add(sec);
			}
		}
		
		iterador=aEliminar.iterator();
		while (iterador.hasNext())
		{
			SeccionParametrizable sec=(SeccionParametrizable) iterador.next();
			seccionesParametrizables.remove(sec);
		}
	}
}
