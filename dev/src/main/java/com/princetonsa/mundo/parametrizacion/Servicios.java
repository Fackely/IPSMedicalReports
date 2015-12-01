/*
 * @(#)Servicios.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Beans.BeanBusquedaServicio;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ServiciosDao;

/**
 * Clase para manejar un conjunto de servicios
 * 
 * @version 1.0 Nov 25, 2003
 */
public class Servicios
{
	private Logger logger = Logger.getLogger(Servicios.class);
	
	/**
	 * Colección con todos los servicios existentes en la aplicación.
	 * El acceso a la colección se hace a través de iteradores (recorrido),
	 * gets (tamaño) y métodos de añadir, elementos que en conjunto
	 * nos permiten tener una colección más segura (Ej. otras clases no
	 * pueden eliminar servicios, agregar objetos diferentes a Servicio, etc)
	 */
	private Collection servicios;
	
	/**
	 * Colección que maneja los nombres y códigos de 
	 * sexo en la fuente de datos (No se carga el nombre
	 * del sexo en la función de cargar debido a que en
	 * ocasiones puede llegar a ser nulo - > cuando el
	 * servicio no esta restringido a hombre o mujer, en
	 * cuyo caso ponemos en el objeto 'Sin Restricción'
	 * y como código -1), de esta manera sabemos al
	 * cargar si fué hombre o mujer y el label a usar
	 */
	private HashMap sexos;

	/**
	 * Colección que maneja los nombres y códigos de 
	 * formularios en la fuente de datos (No se carga el 
	 * nombre del formulario en la función de cargar 
	 * debido a que en ocasiones puede llegar a ser 
	 * nulo - > cuando formulario siguiente, en cuyo caso
	 * cuyo caso ponemos en el objeto 'Sin Formulario'
	 * y como código -1), de esta manera sabemos al
	 * cargar si fué hombre o mujer y el label a usar
	 */
	private HashMap formularios;
	
	/**
	 * Codigo de la institucion
	 */
	private int codigoInstitucion;

	/**
	 * DAO de este objeto, para trabajar con Servicios en
	 * la fuente de datos
	 */
	private ServiciosDao serviciosDao; 
	
	/**
	 * Constructor vació de este objeto, lo único que hace es 
	 * inicializar el acceso a la fuente de datos
	 */
	public Servicios ()
	{
		sexos=new HashMap();
		formularios=new HashMap();
		servicios=new LinkedList();
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( serviciosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			serviciosDao= myFactory.getServiciosDao();
			if( serviciosDao!= null )
				return true;
		}

		return false;
	}
	
	/**
	 * Método que modifica un conjunto de servicios, manejando
	 * transacción de forma atómica (Si desea combinar esta
	 * con otras operaciones de persistencia, utilize 
	 * modificarTransaccional)
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int modificar (Connection con) throws SQLException
	{
		Servicio servicio;
		int numElementos=servicios.size();
		//Si no hay ningún elemento, todo salió bien
		//retornando un número positivo
		if (numElementos==0)
		{
			return 1;
		}
		//Si solo hay un elemento, utilizamos el método
		//atomico de Servicio
		else if (numElementos==1)
		{
			Iterator iterador=servicios.iterator();
			while (iterador.hasNext())
			{
				servicio=(Servicio)iterador.next();
				return servicio.modificar(con);
			}
			 return 0;
		}
		else
		{
			//Cuando hay más de un elemento debemos
			//iterar sobre ellos para realizar todas las
			//inserciones necesarias, sin embargo utilizamos
			//un contador, para saber donde debemos
			//empezar la transacción y terminarla
			Iterator iterador=servicios.iterator();
			int resultados[]=new int[numElementos];
			int i=0;
			while (iterador.hasNext())
			{
				servicio=(Servicio)iterador.next();
				//Cuando i es 0, no se ha abierto la transacción, lo hacemos
				if (i==0)
				{
					resultados[i]=servicio.modificarTransaccional(con, "empezar");
				}
				//Cuando llegamos al último elemento, debemos terminar la transacción
				else if (i==numElementos-1)
				{
					resultados[i]=servicio.modificarTransaccional(con, "finalizar");
				}
				//En cualquier otro caso la transacción continú
				else
				{
					resultados[i]=servicio.modificarTransaccional(con, "continuar");
				}
				i++;
			}
			//Al final revisamos si no se tuvo ningún problema en las inserciones
			//se retorna 1, de lo contrario 0
			if (UtilidadTexto.revisarArregloResp(resultados))
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}

	/**
	 * Método que modifica un conjunto de servicios, pudiendo
	 * combinar esta operación con otras a través del parametro
	 * "estado"
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int modificarTransaccional (Connection con, String estado) throws SQLException
	{
		//Este método se comporta de manera casi igual, luego los
		//comentarios corresponden solo a lo nuevo
		Servicio servicio;
		int numElementos=servicios.size();
		if (numElementos==0)
		{
			return 1;
		}
		else
		{
			Iterator iterador=servicios.iterator();
			int resultados[]=new int[numElementos];
			int i=0;
			while (iterador.hasNext())
			{
				servicio=(Servicio)iterador.next();
				if (i==0&&estado.equals("empezar"))
				{
					//Solo si el estado es empezar y nos encontramos en el primer
					//servicio empezamos la transacción
					resultados[i]=servicio.modificarTransaccional(con, "empezar");
				}
				else if (i==numElementos-1&&estado.equals("finalizar"))
				{
					//Si por el contrario el estado es finalizar y nos encontramos 
					//en el último servicio, terminamos la transacción
					resultados[i]=servicio.modificarTransaccional(con, "finalizar");
				}
				else
				{
					resultados[i]=servicio.modificarTransaccional(con, "continuar");
				}
			}
			if (UtilidadTexto.revisarArregloResp(resultados))
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}

	/**
	 * Este método encapsula el funcionamiento común a los métodos
	 * cargarRestringido y cargarRestringidoServiciosNoUsados su 
	 * objetivo único es evitar la replicación de código. Método privado
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param restriccion Objeto con todas las restricciones que da
	 * el usuario
	 * @param restringirBusquedaNoUsados boolean que indica si
	 * se deben o no buscar con la característica de usada o no
	 * @throws SQLException
	 */
	private void cargarRestringidoComun (Connection con, BeanBusquedaServicio restriccion, boolean restringirBusquedaNoUsados) throws SQLException
	{
		//se reinicia colección
		this.servicios = new ArrayList();
		
		//Primero cargamos las referencias del sexo
		HashMap rs=serviciosDao.cargarSexos(con);
		int numRegistros=Integer.parseInt((String)rs.get("numRegistros"));
		for(int i=0; i<numRegistros; i++)
		{
			sexos.put(Integer.parseInt(rs.get("codigo_"+i)+""), (String)rs.get("nombre_"+i));
		}
		
		
		Collection codigosServiciosYaUsados;
		//En caso de tener que manejar la búsqueda de los no usados
		//(por defecto quedan en no usados), ejecutamos la consulta que
		//corresponde
		if (restringirBusquedaNoUsados)
		{
			codigosServiciosYaUsados=serviciosDao.busquedaServiciosYaUsados(con);
		}
		else
		{
			codigosServiciosYaUsados=new ArrayList();
		}

		//Ahora cargamos la base de los servicios
		rs=serviciosDao.cargarServiciosRestringido(con, restriccion, codigoInstitucion);
		InfoDatosInt sexo, formulario;

		numRegistros=Integer.parseInt((String)rs.get("numRegistros"));
		for(int i=0; i<numRegistros; i++)
		{
			if (rs.get("codigosexo_"+i)!=null && !rs.get("codigosexo_"+i).toString().equals(""))
			{
				int codigoSexo=Integer.parseInt(rs.get("codigosexo_"+i)+"");
				sexo=new InfoDatosInt (codigoSexo, ( (String) sexos.get("" + codigoSexo) )  ); 
			}
			else
			{
				sexo=new InfoDatosInt (-1, "Ambos");
			}

			//logger.info(Integer.parseInt(rs.get("codigo_"+i)+"")+ " "+codigoInstitucion);
			this.formularios = serviciosDao.cargarFormulariosServicio(con, Integer.parseInt(rs.get("codigo_"+i)+""), codigoInstitucion);
			String unidadesUVR=rs.get("unidadesuvr")==null?"0":rs.get("unidadesuvr")+"";

			int codigoEspecialidad=0;
			if(rs.get("codigoespecialidad_"+i)!=null)
			{
				codigoEspecialidad=Integer.parseInt(rs.get("codigoespecialidad_"+i)+"");
			}

			boolean esPos=true;
			if(rs.get("espos_"+i)!=null)
			{
				esPos=UtilidadTexto.getBoolean(rs.get("espos_"+i)+"");
			}

			boolean activo=false;
			if(rs.get("activo_"+i)!=null)
			{
				activo=UtilidadTexto.getBoolean(rs.get("activo_"+i)+"");
			}
			//Se comento la linea que se encuentra acontinuación de este comentario y la que se encuentra dentro del if de nivel_
			//por tarea Xplaner 33243, porque los tipos de datos que resibe InfoDatos son de tipo String.
			
			//int nivel=0;
			String nivel="";
			if(rs.get("nivel_"+i)!=null)
			{
				//nivel=Integer.parseInt(rs.get("nivel_"+i)+"");
				nivel= rs.get("nivel_"+i)+"";
			}

			int portatil=-1;
			if(rs.get("portatilasociado_"+i)!=null)
			{
				portatil=Integer.parseInt(rs.get("portatilasociado_"+i)+"");
			}
		
			Servicio servicio= new Servicio (
					Integer.parseInt(rs.get("codigo_"+i)+""),
					new InfoDatosInt (codigoEspecialidad, (String)rs.get("especialidad_"+i)),
					sexo,
					new InfoDatos((String)rs.get("codigotipo_"+i), (String)rs.get("tipo_"+i)),
					new InfoDatos((String)rs.get("codigonaturaleza_"+i), (String)rs.get("naturaleza_"+i)),
					formularios,
					esPos,
					(String)rs.get("espossubsidiado_"+i),
					activo,
					Double.parseDouble(unidadesUVR),
					new InfoDatos (nivel, "", (String)rs.get("descnivel_"+i)),
					(String)rs.get("realizainstitucion_"+i),
					rs.get("costo_"+i)+"",
					(String)rs.get("requiereinterpretacion_"+i),
					(String)rs.get("requierediagnostico_"+i),
					portatil,
					(String)rs.get("descripcionservicio_"+i),
					(String)rs.get("codigocups_"+i),
					codigoInstitucion,
					(String)rs.get("atencionodontologica_"+i),
					Utilidades.convertirAEntero(rs.get("convencion_"+i)+""),
					Utilidades.convertirAEntero(rs.get("minutosduracion_"+i)+""),
					rs.get("archivo_"+i)+"");
			servicio.cargarCodigosParticulares(con, codigoInstitucion);
			
			if(restringirBusquedaNoUsados)
			{
				if (codigosServiciosYaUsados.contains(servicio.getCodigo()+""))
				{
					servicio.setUsado(true);
				}
				//No debemos tener en cuenta el caso else, porque por defecto
				//el sistema dice que un servicio no fué usado
			}
			
			//Se cargan los datos del servicio
			int codigoGrupo=0;
			if(rs.get("codigogrupo_"+i)!=null)
			{
				codigoGrupo=Integer.parseInt(rs.get("codigogrupo_"+i)+"");
			}
			servicio.setGrupoServicio(new InfoDatosInt(codigoGrupo,(String)rs.get("grupo_"+i)));
			
			
			servicios.add(servicio);
		}

	}

	/**
	 * Método que carga todos los servicios de acuerdo a la restricción
	 * dada, pero siempre dice que el servicio no ha sido usado
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param restriccion Objeto con todas las restricciones que da
	 * el usuario
	 * @throws SQLException
	 */
	public void cargarRestringido (Connection con, BeanBusquedaServicio restriccion) throws SQLException
	{
		this.cargarRestringidoComun(con, restriccion, false);
	}

	/**
	 * Método que carga todos los servicios de acuerdo a la restricción
	 * dada, pero cargando efectivamente el campo que dice si el servicio
	 * ha sido o no usado
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param restriccion Objeto con todas las restricciones que da
	 * el usuario
	 * @throws SQLException
	 */
	public void cargarRestringidoServiciosNoUsados (Connection con, BeanBusquedaServicio restriccion) throws SQLException
	{
		this.cargarRestringidoComun(con, restriccion, true);
	}
	
	/**
	 * Método que carga todos los servicios de este sistema
	 * 
	 * @param con
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public void cargar (Connection con) throws SQLException
	{
		//Primero cargamos las referencias del sexo
		HashMap rs=serviciosDao.cargarSexos(con);
		int numRegistros=Integer.parseInt((String)rs.get("numRegistros"));
		for(int i=0; i<numRegistros; i++)
		{
			sexos.put(Integer.parseInt(rs.get("codigo_"+i)+""), (String)rs.get("nombre_"+i));
		}
		
		//Ahora cargamos la base de los servicios
		rs=UtilidadBD.cargarValueObject(serviciosDao.cargarServicios(con));
		InfoDatosInt sexo, formulario;
		numRegistros=Integer.parseInt((String)rs.get("numRegistros"));
		for(int i=0; i<numRegistros; i++)
		{
			if (rs.get("codigosexo_"+i)!=null && !rs.get("codigosexo_"+i).toString().equals(""))
			{
				int codigoSexo=Integer.parseInt(rs.get("codigosexo_"+i)+"");
				sexo=new InfoDatosInt (codigoSexo, ( (String) sexos.get("" + codigoSexo) )  ); 
			}
			else
			{
				sexo=new InfoDatosInt (-1, "Ambos");
			}
			this.formularios = serviciosDao.cargarFormulariosServicio(con, Integer.parseInt(rs.get("codigo_"+i)+""), this.codigoInstitucion);
			String unidadesUVR=rs.get("unidadesuvr_"+i)==null?"0":rs.get("unidadesuvr_"+i)+"";

			int codigoEspecialidad=0;
			if(rs.get("codigoespecialidad_"+i)!=null && !rs.get("codigoespecialidad_"+i).toString().equals(""))
			{
				codigoEspecialidad=Integer.parseInt(rs.get("codigoespecialidad_"+i)+"");
			}

			boolean esPos=true;
			if(rs.get("espos_"+i)!=null && !rs.get("espos_"+i).toString().equals(""))
			{
				esPos=UtilidadTexto.getBoolean(rs.get("espos_"+i)+"");
			}

			boolean activo=false;
			if(rs.get("activo_"+i)!=null && !rs.get("activo_"+i).toString().equals(""))
			{
				activo=UtilidadTexto.getBoolean(rs.get("activo_"+i)+"");
			}

			int nivel=0;
			if(rs.get("nivel_"+i)!=null && !rs.get("nivel_"+i).toString().equals(""))
			{
				nivel=Integer.parseInt(rs.get("nivel_"+i)+"");
			}
			int portatil=-1;
			if(rs.get("portatilasociado_"+i)!=null && !rs.get("portatilasociado_"+i).toString().equals(""))
			{
				portatil=Integer.parseInt(rs.get("portatilasociado_"+i)+"");
			}

			Servicio servicio= new Servicio (
					Integer.parseInt(rs.get("codigo_"+i)+""),
					new InfoDatosInt (codigoEspecialidad, (String)rs.get("especialidad_"+i)),
					sexo,
					new InfoDatos((String)rs.get("codigotipo_"+i), (String)rs.get("tipo_"+i)),
					new InfoDatos((String)rs.get("codigoNaturaleza_"+i), (String)rs.get("naturaleza_"+i)),
					formularios,
					esPos,
					(String)rs.get("espossubsidiado_"+i),
					activo,
					Double.parseDouble(unidadesUVR),
					new InfoDatos(nivel, "", (String)rs.get("descnivel_"+i)),
					(String)rs.get("realizainstitucion_"+i),
					rs.get("costo_"+i)+"",
					(String)rs.get("requiereinterpretacion_"+i),
					(String)rs.get("requierediagnostico_"+i),
					portatil,
					(String)rs.get("desportatilasociado_"+i),
					(String)rs.get("codigocups_"+i),this.codigoInstitucion,
					(String)rs.get("atencionodontologica_"+i),
					Utilidades.convertirAEntero(rs.get("convencion_"+i)+""),
					Utilidades.convertirAEntero(rs.get("minutosduracion_"+i)+""),
					rs.get("archivo_"+i)+"");
			servicio.cargarCodigosParticulares(con,codigoInstitucion);
			servicios.add(servicio);
		}
	}

	/**
	 * busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups 
	 * @param con
	 * @param codigoAxioma
	 * @param codigoPropietarioCups
	 * @param codigoPropietarioIss
	 * @param codigoPropietarioSoat
	 * @param descripcionCups
	 * @param codigoSexo
	 * @param codigosServiciosInsertados
	 * @return
	 */
	public Collection busquedaAvanzadaServiciosXCodigos(	Connection con, 
															String codigoAxioma,
															String codigoPropietarioCups,
															String codigoPropietarioIss,
															String codigoPropietarioSoat,
															String descripcionCups,
															int codigoSexo,
                                                            int codigoConvenioResponsable,
                                                            int codigoContratoCuenta
                                                            )
	{
		ServiciosDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao();
		Collection coleccion=null;
		try
		{	
				coleccion=UtilidadBD.resultSet2Collection(consulta.busquedaAvanzadaServiciosXCodigos(	con,
																										codigoAxioma,
																										codigoPropietarioCups,
																										codigoPropietarioIss,
																										codigoPropietarioSoat,
																										descripcionCups, 
																										codigoSexo,
                                                                                                        codigoConvenioResponsable,
                                                                                                        codigoContratoCuenta
                                                                                                        ));
				        																						
		}
		catch(Exception e)
		{
			e.printStackTrace();
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Iterador que permite recorrer los servicios sin 
	 * tener acceso a la colección como tal
	 * @return
	 */
	public Iterator getIteradorServicios ()
	{
		return servicios.iterator();
	}
	
	/**
	 * Método que retorna el número de servicios
	 * @return
	 */
	public int getNumServicios()
	{
		return servicios.size();
	}
	
	/**
	 * Método para añadir un servicio a la colección
	 * @param servicio
	 */
	public void anadirServicio (Servicio servicio)
	{
		servicios.add(servicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param automatic
	 * @return
	 * Ademas de cargar el metodo me sirve para verificar que no tenga repetidos en la lista de
	 * automaticos
	 */	
	
	public HashMap cargarServiciosautomatico (Connection con, String automatic) 
	{
		HashMap temp = new HashMap();
		String cadena ="";
		temp = serviciosDao.cargarServiciosautomatico(con, automatic);
		/*
		 * Capturo el numero de registros que tiene el automatico activo,
		 * Recorro los registros y verifico que el codigo no este en la lista de automaticos
		 * 		 
		*/
		for(int i=0; i<Integer.parseInt(temp.get("numRegistros").toString());i++)
		{
			cadena+=temp.get("codigo_"+i).toString()+",";			
			
		}		
		temp.put("codServInsert",cadena);
		
		return temp;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 * Este metodo es para el check box de la funcionalidad
	 * simplemente si quiero que se carge en la lista de automaticos lo chequeo
	 * y si no quiero que se cargue lo deschequeo...obviamente hay que guardar los cambios
	 */
	public boolean actualizarAutomatico (Connection con, HashMap vo)
	{
		return serviciosDao.actualizarAutomatico(con, vo);	
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public static String obtenerCodigoTarifarioServicio (Connection con, int codigoServicio, int tipoTarifario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao().obtenerCodigoTarifarioServicio(con, codigoServicio, tipoTarifario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public static String obtenerCodigoTarifarioServicioConDesc (int codigoServicio, int tipoTarifario)
	{
		Connection con= UtilidadBD.abrirConexion();
		String r= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao().obtenerCodigoTarifarioServicioConDesc(con, codigoServicio, tipoTarifario);
		UtilidadBD.closeConnection(con);
		return r;
	}
	
	/**
	 * 
	 * @param codigoPropietario
	 * @param tipoTarifario
	 * @return
	 */
	public static int obtenerServicioDadoCodigoTarifario(Connection con, String codigoPropietario, int tipoTarifario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao().obtenerServicioDadoCodigoTarifario(con, codigoPropietario, tipoTarifario);
	}
	
	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public static String obtenerNombreServicio(Connection con, int codigoServicio, int tipoTarifario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao().obtenerNombreServicio(con, codigoServicio, tipoTarifario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public static String obtenerNombreServicio(int codigoServicio, int tipoTarifario)
	{
		Connection con= UtilidadBD.abrirConexion();
		String a= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao().obtenerNombreServicio(con, codigoServicio, tipoTarifario);
		UtilidadBD.closeConnection(con);
		return a;
	}

	/**
	 * 
	 * @param codigo
	 * @param codigoInstitucion
	 * @return
	 */
	public static String obtenerNombreServicioTarifarioParametrizado(int codigo,int codigoInstitucion) 
	{
		Connection con= UtilidadBD.abrirConexion();
		int tipoTarifario=Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion));
		if(tipoTarifario<0)
			tipoTarifario=ConstantesBD.codigoTarifarioCups;
		String a= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao().obtenerNombreServicio(con, codigo, tipoTarifario);
		UtilidadBD.closeConnection(con);
		return a;
	}
}
