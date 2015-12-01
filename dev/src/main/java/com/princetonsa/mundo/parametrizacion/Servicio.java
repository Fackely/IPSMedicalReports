/*
 * @(#)Servicio.java
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
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ServiciosDao;
import com.princetonsa.dao.sqlbase.SqlBaseServiciosDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;

/**
 * Clase en la que se maneja toda la información / funcionalidad
 * correspondiente a un servicio. Cuando el código de uno de los
 * datos del servicio corresponde a un entero se maneja un tipo
 * InfoDatosInt, cuando sea un acrónimo (texto) se utiliza 
 * InfoDatos.  Este método no tiene su propio objeto Dao, porque
 * comparte muchas características con Servicios y para evitar
 * inicializar varias veces este Dao, una por elemento (De todas
 * maneras si el usuario lo desea utilizar StandAlone, esta clase
 * lo inicializa)
 * 
 * @version 1.0 Nov 25, 2003
 */
public class Servicio
{

	Logger logger=Logger.getLogger(Servicio.class);
	/**
	 * En esta variable se almacena el código del 
	 * Servicio en la fuente de datos 
	 */
	private int codigo;
	
	/**
	 * Objeto en que se guarda toda la información de 
	 * la especialidad a la que pertenece este servicio
	 */
	private InfoDatosInt especialidad;
	
	/**
	 * Objeto en que se guarda toda la información del 
	 * sexo sobre el que se restringe este servicio
	 * (-1 en el código en caso de no existir restricción)
	 */
	private InfoDatosInt restriccionSexo;
	
	/**
	 * Objeto en que se guarda toda la información del
	 * tipo de este servicio
	 */
	private InfoDatos tipoServicio;
	
	/**
	 * Objeto en que se guarda toda la información de
	 * la naturaleza de este servicio
	 */
	private InfoDatos naturalezaServicio;

	
	/**
	 * Objeto en que se guarda la información del 
	 * formulario
	 */
	private HashMap<String,Object> formulario = new HashMap<String, Object>();
	
	/**
	 * nivel
	 */
	private InfoDatos nivel;
	
	/**
	 * Objeto que almacena la información de Cups,
	 * en caso de no existir cups para este servicio,
	 * tiene como acronimo ""
	 */
	private InfoDatos informacionCups;
	
	/**
	 * Objeto que almacena la información de ISS,
	 * en caso de no existir cups para este servicio,
	 * tiene como acronimo ""
	 */
	private InfoDatos informacionISS;
	
	/**
	 * Objeto que almacena la información de Soat
	 * en caso de no existir cups para este servicio,
	 * tiene como acronimo ""
	 */
	private InfoDatos informacionSoat;
	
	/**
	 * Objeto que almacena la informacion de Sonria
	 * en caso de no existir Cups para este servicio tiene como acronimo ""
	 */
	private InfoDatos informacionSonria;
	
	/**
	 * String donde se almacenan las unidades SOAT
	 * (Número flotante con dos decimales) 
	 */
	private String unidadesSoat;

	/**
	 * Flotante donde se guarda el valor real de las
	 * unidades SOAT para consultas numéricas
	 */
	private double unidadesSoatFloat;

	/**
	 * Este atributo me dice si este servicio es POS o
	 * no
	 */
	private boolean esPos;
	
	/**
	 * 
	 */
	private String posSubsidiado;
	
	/**
	 * Este atributo me dice si este servicio es POS o
	 * no
	 */
	private boolean activo;
	
	/**
	 * Unidades Uvr del servicio
	 */
	private double unidadesUvr;
	
	/**
	 * Grupo del servicio
	 */
	private InfoDatosInt grupoServicio;
	

	/**
	 * Este atributo me dice si este servicio ha sido usado o
	 * no
	 */
	private boolean usado;
	
	/**
	 * Boolean que nos indica que el elemento previamente tuvo
	 * unidades Soat. Sirve para el caso particular en que se desea
	 * actualizar el registro de Soat; antes existía alguno de los
	 * datos de Soat y ahora no existe ninguno
	 */
	private boolean centinelaSoat;
	
	/**
	 * Boolean que nos indica que el elemento previamente tuvo
	 * unidades Iss.. Sirve para el caso particular en que se desea
	 * actualizar el registro de Iss; antes existía alguno de los
	 * datos de Iss y ahora no existe ninguno
	 */
	private boolean centinelaIss;
	
	/**
	 * DAO de este objeto, para trabajar con Servicio en
	 * la fuente de datos
	 */
	private ServiciosDao servicioDao; 
	
	
	
	private String realizaInstitucion;
	private String costo;
	private String requiereInterpretacion;
	private String requiereDiagnostico;
	
	private HashMap tarifariosMap;
	
	/**
	 * Codigo para los servicios portatiles
	 * Adicion señalada en el documento Anexo 591
	 */
	private String desServicioPortatil;
	
	private int codigoServicioPortatil;
	
	/**
	 * Codigo Cups adicionado por la Tarea 38289
	 */
	private String codigoCups;
	
	/**
	 * Codigo de la institucion
	 */
	private int codigoInstitucion;
	
	/**
	 * Elementos Anexo 868 Odontología
	 */
	private String atencionOdonlogica;
	
	private int convencion;
	
	private int minutosDuracion;
	
	private String archivo;
	
	/**
	 * Constructor de este objeto y única manera de establecer la
	 * información del objeto (Por seguridad NO hay metodos set)
	 * 
	 * @param especialidad especialidad que restringe este servicio
	 * @param restriccionSexo sexo que restring este servicio
	 * @param tipoServicio Tipo de este servicio
	 * @param naturalezaServicio Naturaleza de este servicio
	 * @param esPos 
	 */
	
	public Servicio (int codigo)
	{
		this.clean();
		
		this.codigo=codigo;
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Método para obtener el DAO de servicios de manera estática
	 * @return
	 */
	public static ServiciosDao servicioDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao();
	}
	
	/**
	 * Método para obtener el arreglo de los formularios para asignarle al servicio
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<DtoPlantillaServDiag> obtenerArregloFormularios(Connection con,int codigoInstitucion)
	{
		return servicioDao().obtenerArregloFormularios(con, codigoInstitucion);
	}
	
	
	
	
	public Servicio (int codigo, InfoDatosInt especialidad, InfoDatosInt restriccionSexo, InfoDatos tipoServicio, InfoDatos naturalezaServicio, HashMap<String,Object> formulario, boolean esPos,String posSubsidiado, boolean activo, double unidadesUvr, InfoDatos nivel, String realizaInstitucion, String costo, String requiereInterpretacion, String requiereDiagnostico, int codigoServicioPortatil, String desServicioPortatil, String codigoCups,int codigoInstitucion,String atencionOdontologica, int convencion, int minutosDuracion, String archivo)
	{
		this.clean();
		this.codigo=codigo;
		this.especialidad=especialidad;
		this.restriccionSexo=restriccionSexo;
		this.tipoServicio=tipoServicio;
		this.naturalezaServicio=naturalezaServicio;
		this.formulario=formulario;
		this.nivel= nivel;
		this.esPos=esPos;
		this.posSubsidiado=posSubsidiado;
		this.activo=activo;
		this.unidadesUvr=unidadesUvr;
		this.realizaInstitucion=realizaInstitucion;
		this.costo=costo;
		this.requiereInterpretacion=requiereInterpretacion;
		this.requiereDiagnostico = requiereDiagnostico;
		this.desServicioPortatil = desServicioPortatil;
		this.codigoServicioPortatil = codigoServicioPortatil;
		this.codigoCups = codigoCups;
		this.codigoInstitucion = codigoInstitucion;
		this.atencionOdonlogica=atencionOdontologica;
		this.convencion=convencion;
		this.minutosDuracion=minutosDuracion;
		this.archivo=archivo;
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Método que limpia este servicio
	 *
	 */
	public void clean ()
	{
		codigo=-1;
		this.especialidad=new InfoDatosInt();
		this.restriccionSexo=new InfoDatosInt();
		this.tipoServicio=new InfoDatos();
		this.naturalezaServicio=new InfoDatos();
		this.formulario=new HashMap<String, Object>();
		this.nivel= new InfoDatos();
		this.esPos=true;
		this.posSubsidiado=ConstantesBD.acronimoNo+"";
		this.usado=false;
		this.informacionCups=new InfoDatos();
		this.informacionISS=new InfoDatos();
		this.informacionSoat=new InfoDatos();
		this.informacionSonria=new InfoDatos(); 
		
		this.unidadesSoat="0.0";
		this.unidadesSoatFloat=0.0;
		this.unidadesUvr=0.0;
		this.grupoServicio = new InfoDatosInt();
		this.centinelaSoat=false;
		this.centinelaIss=false;
		
		this.realizaInstitucion="";
		this.requiereInterpretacion="";
		this.requiereDiagnostico = "";
		
		this.tarifariosMap= new HashMap();
		
		this.desServicioPortatil = "";
		this.codigoServicioPortatil = ConstantesBD.codigoNuncaValido;
		this.codigoCups = "";
		
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		
		this.atencionOdonlogica="";
		this.convencion=ConstantesBD.codigoNuncaValido;
		this.minutosDuracion=ConstantesBD.codigoNuncaValido;
		this.archivo="";
	}
	
	/**
	 * Método que carga los códigos particulas que tenga este 
	 * servicio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public boolean cargarCodigosParticulares (Connection con, int codInstitucion) throws SQLException
	{
		if (servicioDao==null)
		{
			return false;
		}
		int codigoTipoTarifario=0;
		//String tarifario= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codInstitucion);
		HashMap rs=servicioDao.cargarCodigosParticulares(con, this.codigo);
		/*if(tarifario.equals(""))
		{	
			codigoTipoTarifario=ConstantesBD.codigoTarifarioCups;
		}else
		{
			codigoTipoTarifario=Utilidades.convertirAEntero(tarifario);
		}*/
		
		int numRegistros=Integer.parseInt((String)rs.get("numRegistros"));
		//Utilidades.imprimirMapa(rs);
		for(int i=0; i<numRegistros; i++)
		{
			codigoTipoTarifario=Integer.parseInt(rs.get("tipotarifario_"+i)+"");
			
			if(codigoTipoTarifario==ConstantesBD.codigoTarifarioCups)
			{
			 this.informacionCups.setNombre((String)rs.get("nombre_"+i));
			 this.informacionCups.setDescripcion((String)rs.get("descripcion_"+i));
			}
			if(codigoTipoTarifario==ConstantesBD.codigoTarifarioISS)
			{
				this.informacionISS.setNombre((String)rs.get("nombre_"+i));
				this.informacionISS.setDescripcion((String)rs.get("descripcion_"+i));
			}
			if(codigoTipoTarifario==ConstantesBD.codigoTarifarioSonria)
			{
				this.informacionSonria.setNombre((String)rs.get("nombre_"+i));
				this.informacionSonria.setDescripcion((String)rs.get("descripcion_"+i));
			}
			if(codigoTipoTarifario==ConstantesBD.codigoTarifarioSoat)
			{
				this.informacionSoat.setNombre((String)rs.get("nombre_"+i));
				this.informacionSoat.setDescripcion((String)rs.get("descripcion_"+i));
				double unidades=0;
				if(rs.get("unidades_"+i)!=null && !rs.get("unidades_"+i).toString().equals(""))
				{
					unidades=Double.parseDouble(rs.get("unidades_"+i).toString());
				}
				this.setUnidadesSoat(unidades);
			}	
			
		}
		return true;
	}

	/**
	 * Método que carga las unidades UVR que tenga un
	 * servicio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public boolean consultaUnidadesUvr (Connection con) throws SQLException
	{
		if (servicioDao==null)
		{
			return false;
		}		
		ResultSetDecorator rs=servicioDao.consultaUnidadesUvr(con, this.codigo);
		while (rs.next())
		{
			this.unidadesUvr=rs.getInt("unidadesUvr");
		}
		return true;
	}
	/**
	 * Método que inserta un servicio (es transaccional de manera atómica),
	 * si desea combinarlo con otras operaciones de persistencia utilize 
	 * insertarTransaccional
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int insertar (Connection con) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		//Se asume que todo salio bien a menos que se demuestre lo contrario, ya
		//que algunas de las operaciones a verificar no siempre son ejecutadas
		int resp1=1, resp2=1, resp3=1, resp4=1;
		if (servicioDao==null)
		{
			return -1;
		}
		//Iniciamos la transacción
		boolean inicioTrans=myFactory.beginTransaction(con);
		
		//Insertamos el servicio con sus datos básicos (sin información
		//sobre cups, iss o soat)
		
		this.codigo=servicioDao.insertarServicio(con, especialidad.getCodigo(), tipoServicio.getAcronimo(), naturalezaServicio.getAcronimo(), restriccionSexo.getCodigo(),  formulario, esPos,posSubsidiado, activo, unidadesUvr, grupoServicio.getCodigo(), nivel.getId(), requiereInterpretacion, costo, realizaInstitucion, requiereDiagnostico, codigoServicioPortatil,codigoInstitucion, atencionOdonlogica,convencion,minutosDuracion);

		//Si la inserción salio bien, el método nos debió retornar el código
		//del servicio, código que de ser válido
		if (this.codigo>0)
		{
			if (this.tieneInformacionCups())
			{
				resp1=servicioDao.insertarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioCups, informacionCups.getNombre().trim(), informacionCups.getDescripcion());
			}
		
			if (this.tieneInformacionISS())
			{
				resp2=servicioDao.insertarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioISS, informacionISS.getNombre().trim(), informacionISS.getDescripcion());
			}

			if (this.tieneInformacionSoat())
			{
				resp3=servicioDao.insertarCodigoParticularSoat(con, this.codigo, informacionSoat.getNombre().trim(), this.unidadesSoatFloat, informacionSoat.getDescripcion());
			}
			
			if(this.tieneInformacionSonria())
			{
				resp3=servicioDao.insertarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioSonria, informacionSonria.getNombre().trim(), informacionSoat.getDescripcion());
			}
			
			for(int i=0;i<Utilidades.convertirAEntero(this.tarifariosMap.get("numRegistros")+"",false);i++)
			{
				if(!UtilidadTexto.isEmpty(tarifariosMap.get("codigopropietario_"+i)+"")&&!UtilidadTexto.isEmpty(tarifariosMap.get("descripcion_"+i)+""))
				{
					if((tarifariosMap.get("tiporegistro_"+i)+"").trim().equals("MEM"))
						resp2=servicioDao.insertarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
					else
						resp2=servicioDao.modificarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
				}
			}

		}

		//Acá revisamos si todo salio bien terminamos la transacción,
		//si no hacemos un rollback
		if (!inicioTrans||resp1<1||resp2<1||resp3<1||resp4<1||this.codigo<1)
		{
			this.codigo=0;
			myFactory.abortTransaction(con);
		}
		else
		{
			myFactory.endTransaction(con);
		}

		return this.codigo;
		
	}

	/**
	 * Método que inserta un servicio, recibiendo el estado de la transacción
	 * para poder combinarlo con otras operaciones de persistencia
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Define el lugar dentro de una transacción más grande que
	 * ocupa esta operación (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		//Se asume que todo salio bien a menos que se demuestre lo contrario, ya
		//que algunas de las operaciones a verificar no siempre son ejecutadas
		int resp1=1, resp2=1, resp3=1;
		if (servicioDao==null)
		{
			return -1;
		}
		boolean inicioTrans;
		
		//Iniciamos la transacción en caso de tener estado empezar
		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
		
		//Insertamos el servicio con sus datos básicos (sin información
		//sobre cups, iss o soat)
		this.codigo=servicioDao.insertarServicio(con, especialidad.getCodigo(), tipoServicio.getAcronimo(), naturalezaServicio.getAcronimo(), restriccionSexo.getCodigo(), formulario, esPos,posSubsidiado, activo, unidadesUvr,grupoServicio.getCodigo(), nivel.getId(), realizaInstitucion, costo, requiereInterpretacion, requiereDiagnostico, codigoServicioPortatil,codigoInstitucion,atencionOdonlogica,convencion,minutosDuracion);
		
		//Si la inserción salio bien, el método nos debió retornar el código
		//del servicio, código que de ser válido
		if (this.codigo>0)
		{
			if (this.tieneInformacionCups())
			{
				resp1=servicioDao.insertarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioCups, informacionCups.getNombre(), informacionCups.getDescripcion());
			}
		
			if (this.tieneInformacionISS())
			{
				resp2=servicioDao.insertarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioISS, informacionISS.getNombre(), informacionISS.getDescripcion());
			}

			if (this.tieneInformacionSoat())
			{
				resp3=servicioDao.insertarCodigoParticularSoat(con, this.codigo, informacionSoat.getNombre(), this.unidadesSoatFloat, informacionSoat.getDescripcion());
			}
			for(int i=0;i<Utilidades.convertirAEntero(this.tarifariosMap.get("numRegistros")+"",false);i++)
			{
				if((tarifariosMap.get("tiporegistro_"+i)+"").trim().equals("MEM"))
					resp2=servicioDao.insertarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
				else
					resp2=servicioDao.modificarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
			}
		}

		//Acá revisamos si algo salio mal, en cuyo caso abortamos
		// la transacción
		if (!inicioTrans||resp1<1||resp2<1||resp3<1||this.codigo<1)
		{
			this.codigo=0;
			myFactory.abortTransaction(con);
		}
		//En caso contrario y solo si el estado es finalizar, terminamos
		//la transacción
		else if (estado.equals("finalizar"))
		{
			myFactory.endTransaction(con);
		}
		
		return this.codigo;
		
	}

	/**
	 * Método que modifica un servicio (es transaccional de manera atómica),
	 * si desea combinarlo con otras operaciones de persistencia utilize 
	 * modificarTransaccional
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int modificar (Connection con) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp0=1, resp1=1, resp2=1, resp3=1;
		
		if (servicioDao==null)
		{
			return -1;
		}

		//Iniciamos la transacción
		boolean inicioTrans=myFactory.beginTransaction(con);

		//Modificamos el servicio con sus datos básicos (sin tocar
		//información sobre cups, iss o soat)
		
		resp0=servicioDao.modificarServicio(con, this.codigo, this.especialidad.getCodigo(), this.tipoServicio.getAcronimo(), this.naturalezaServicio.getAcronimo(), this.restriccionSexo.getCodigo(),  this.formulario, this.esPos,this.posSubsidiado, this.activo, this.unidadesUvr,this.grupoServicio.getCodigo(), this.nivel.getId(), this.requiereInterpretacion, this.costo, this.realizaInstitucion, this.requiereDiagnostico, this.codigoServicioPortatil,this.codigoInstitucion, this.atencionOdonlogica, this.convencion, this.minutosDuracion);
		//Para cada uno de los códigos particulares, si el dato esta
		//lleno se inserta
		if (this.tieneInformacionCups())
		{
			resp1=servicioDao.modificarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioCups, this.informacionCups.getNombre(), this.informacionCups.getDescripcion());
		}
		
		if (this.tieneInformacionISS())
		{
			resp2=servicioDao.modificarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioISS, this.informacionISS.getNombre(), this.informacionISS.getDescripcion());
		}

		if (this.tieneInformacionSoat())
		{
			resp3=servicioDao.modificarCodigoParticularSoat(con, this.codigo, this.informacionSoat.getNombre(), this.unidadesSoatFloat, this.informacionSoat.getDescripcion());
		}

		int numeroElementosTarifariosMap=Utilidades.convertirAEntero(this.tarifariosMap.get("numRegistros")+"",false);
		for(int i=0;i<numeroElementosTarifariosMap;i++)
		{
			if(UtilidadCadena.noEsVacio(tarifariosMap.get("codigopropietario_"+i)+""))
			{
				if((tarifariosMap.get("tiporegistro_"+i)+"").trim().equals("MEM"))
					resp2=servicioDao.insertarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
				else
					resp2=servicioDao.modificarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
			}
		}
		//Acá revisamos si todo salio bien terminamos la transacción,
		//si no hacemos un rollback
		if (!inicioTrans||resp0<1||resp1<1||resp2<1||resp3<1)
		{
			resp1=0;
			myFactory.abortTransaction(con);
		}
		else
		{
			myFactory.endTransaction(con);
		}
		return resp1;
	}

	/**
	 * Método que modifica un servicio, recibiendo el estado de la transacción
	 * para poder combinarlo con otras operaciones de persistencia
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Define el lugar dentro de una transacción más grande que
	 * ocupa esta operación (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int modificarTransaccional (Connection con, String estado) throws SQLException
	{
		
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp0=1, resp1=1, resp2=1, resp3=1;
		
		if (servicioDao==null)
		{
			return -1;
		}

		boolean inicioTrans;
		
		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}

		resp0=servicioDao.modificarServicio(con, this.codigo, this.especialidad.getCodigo(), this.tipoServicio.getAcronimo(), this.naturalezaServicio.getAcronimo(), this.restriccionSexo.getCodigo(),  this.formulario, this.esPos,this.posSubsidiado, this.activo, this.unidadesUvr, this.grupoServicio.getCodigo(), this.nivel.getId(), this.realizaInstitucion, this.costo, this.requiereInterpretacion, this.requiereDiagnostico, this.codigoServicioPortatil,this.codigoInstitucion,this.atencionOdonlogica,this.convencion,this.minutosDuracion);
		if (this.tieneInformacionCups())
		{
			resp1=servicioDao.modificarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioCups, this.informacionCups.getNombre().trim(), this.informacionCups.getDescripcion());
		}
		
		if (this.tieneInformacionISS())
		{
			resp2=servicioDao.modificarCodigoParticular(con, this.codigo, ConstantesBD.codigoTarifarioISS, this.informacionISS.getNombre().trim(), this.informacionISS.getDescripcion());
		}

		if (this.tieneInformacionSoat())
		{
			resp3=servicioDao.modificarCodigoParticularSoat(con, this.codigo, this.informacionSoat.getNombre().trim(), this.unidadesSoatFloat, this.informacionSoat.getDescripcion());
		}
	
		for(int i=0;i<Utilidades.convertirAEntero(this.tarifariosMap.get("numRegistros")+"",false);i++)
		{
			if((tarifariosMap.get("tiporegistro_"+i)+"").trim().equals("MEM"))
				resp2=servicioDao.insertarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
			else
				resp2=servicioDao.modificarCodigoParticular(con, this.codigo,Utilidades.convertirAEntero(tarifariosMap.get("codigo_"+i)+"",false),tarifariosMap.get("codigopropietario_"+i)+"", tarifariosMap.get("descripcion_"+i)+"");
		}
		if (!inicioTrans||resp0<1||resp1<1||resp2<1||resp3<1)
		{
			resp1=0;
			myFactory.abortTransaction(con);
		}
		else if (estado.equals("finalizar"))
		{
			myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Método que revisa si este servicio tiene
	 * información de Cups
	 * @return
	 */
	public boolean tieneInformacionCups ()
	{
		if (informacionCups.getNombre()!=null&&!informacionCups.getNombre().equals(""))
		{
			return true;
		}
		else if(informacionCups.getDescripcion()!=null&&!informacionCups.getDescripcion().equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Método que revisa si este servicio tiene
	 * información de ISS
	 * @return
	 */
	public boolean tieneInformacionISS ()
	{
		if (informacionISS.getNombre()!=null&&!informacionISS.getNombre().equals(""))
		{
			return true;
		}
		else if (informacionISS.getDescripcion()!=null&&!informacionISS.getDescripcion().equals(""))
		{
			return true;
		}
		else if (this.centinelaIss==true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Método que revisa si este servicio tiene
	 * información de Cups
	 * @return
	 */
	public boolean tieneInformacionSoat ()
	{
		if (informacionSoat.getNombre()!=null&&!informacionSoat.getNombre().equals(""))
		{
			return true;
		}
		else if (informacionSoat.getDescripcion()!=null&&!informacionSoat.getDescripcion().equals(""))
		{
			return true;
		}
		else if (this.unidadesSoatFloat>0)
		{
			return true;
		}
		else if (this.centinelaSoat==true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public boolean tieneInformacionSonria ()
	{
		if (informacionSonria.getNombre()!=null&&!informacionSonria.getNombre().equals(""))
		{
			return true;
		}
		else if (informacionSonria.getDescripcion()!=null&&!informacionSonria.getDescripcion().equals(""))
		{
			return true;
		}
		else 
		{
			return false;
		}
		
	}
	
	
	/**
	 * @return
	 */
	public InfoDatosInt getEspecialidad()
	{
		return especialidad;
	}

	
	/**
	 * @return
	 */
	public InfoDatos getNaturalezaServicio()
	{
		return naturalezaServicio;
	}

	/**
	 * @return
	 */
	public InfoDatosInt getRestriccionSexo()
	{
		return restriccionSexo;
	}

	/**
	 * @return
	 */
	public InfoDatos getTipoServicio()
	{
		return tipoServicio;
	}

	/**
	 * @return
	 */
	public boolean isEsPos()
	{
		return esPos;
	}

	/**
	 * @return
	 */
	public InfoDatos getInformacionCups()
	{
		return informacionCups;
	}

	/**
	 * @return
	 */
	public InfoDatos getInformacionISS()
	{
		return informacionISS;
	}

	/**
	 * @return
	 */
	public InfoDatos getInformacionSoat()
	{
		return informacionSoat;
	}

	/**
	 * @param datos
	 */
	public void setInformacionCups(InfoDatos datos)
	{
		informacionCups = datos;
	}

	/**
	 * @param datos
	 */
	public void setInformacionISS(InfoDatos datos)
	{
		informacionISS = datos;
	}

	/**
	 * @param datos
	 */
	public void setInformacionSoat(InfoDatos datos)
	{
		informacionSoat = datos;
	}

	/**
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	
	
	/**
	 * @return
	 */
	public InfoDatos getNivel()
	{
		return nivel;
	}
	
	/**
	 * @return
	 */
	public boolean getActivo()
	{
		return activo;
	}

	/**
	 * @param b
	 */
	public void setActivo(boolean b)
	{
		activo = b;
	}

	/**
	 * Método que permite establecer el texto de las unidades
	 * Soat con las dos cifras requeridas, cortandolo en ese punto
	 * (No realiza aproximación)
	 * 
	 * @param unidadesSoatFloat
	 */
	private void setOldUnidadesSoat (double unidadesSoatFloat)
	{
		String temporal="" + unidadesSoatFloat;
		//Las unidades Float siempre se guardan
		this.unidadesSoatFloat=unidadesSoatFloat;
		int indiceInicial, indiceFinal;
		indiceInicial=temporal.indexOf(".");
		
		//Si se encontró un punto, se debe hacer todo el proceso
		if (indiceInicial>=0)
		{
				
			//Asumimos, en peor de los casos que hay menos de dos 
			//cifras, si probamos lo contrario, modificamos
			indiceFinal=temporal.length();
			if (indiceInicial+3<=indiceFinal)
			{
				indiceFinal=indiceInicial+3;
			}
			this.unidadesSoat=temporal.substring(0, indiceFinal);
		}
		else
		{
			//No se encontró un punto, copiamos directo
			this.unidadesSoat="" + unidadesSoatFloat;
		}
	}
	
	/**
	 * Método que permite establecer el texto de las unidades
	 * Soat con las dos cifras requeridas (Aproximando)
	 * 
	 * @param unidadesSoatFloat
	 */
	public void setUnidadesSoat (double unidadesSoatFloat)
	{
		//Las unidades Float siempre se guardan
		this.unidadesSoatFloat=unidadesSoatFloat;
		
		this.unidadesSoat="" + UtilidadTexto.getDoubleConFormatoEspecifico(unidadesSoatFloat, "##0.00");
	}

	/**
	 * Método get para obtener el número de unidades Soat
	 * en String (para la restricción de cifras)
	 * @return
	 */
	public String getUnidadesSoat ()
	{
		return unidadesSoat;
	}

	/**
	 * Método get para obtener el número de unidades Soat
	 * en double
	 * @return
	 */	
	public double getUnidadesSoatFloat()
	{
		return unidadesSoatFloat;
	}

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( servicioDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			servicioDao= myFactory.getServiciosDao();
			if( servicioDao!= null )
				return true;
		}

		return false;
	}
	

	/**
	 * @return
	 */
	public boolean getUsado() {
		return usado;
	}

	/**
	 * @param b
	 */
	public void setUsado(boolean b) {
		usado = b;
	}

	public double getUnidadesUvr() {
		return unidadesUvr;
	}
	public boolean getCentinelaSoat() {
		return centinelaSoat;
	}

	
	public void setCentinelaSoat(boolean centinelaSoat) {
		this.centinelaSoat = centinelaSoat;
	}
	public boolean isCentinelaIss() {
		return centinelaIss;
	}
	public void setCentinelaIss(boolean centinelaIss) {
		this.centinelaIss = centinelaIss;
	}
	
	/**
	 * @return Returns the grupoServicio.
	 */
	public InfoDatosInt getGrupoServicio() {
		return grupoServicio;
	}
	/**
	 * @param grupoServicio The grupoServicio to set.
	 */
	public void setGrupoServicio(InfoDatosInt grupoServicio) {
		this.grupoServicio = grupoServicio;
	}
	
		
	/**
	 * 
	 * @return
	 */
	public String getRealizaInstitucion() {
		return realizaInstitucion;
	}
	
	/**
	 * 
	 * @param realizaInstitucion
	 */
	public void setRealizaInstitucion(String realizaInstitucion) {
		this.realizaInstitucion = realizaInstitucion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRequiereInterpretacion() {
		return requiereInterpretacion;
	}
	
	/**
	 * 
	 * @param requiereInterpretacion
	 */
	public void setRequiereInterpretacion(String requiereInterpretacion) {
		this.requiereInterpretacion = requiereInterpretacion;
	}




	public HashMap getTarifariosMap() {
		return tarifariosMap;
	}




	public void setTarifariosMap(HashMap tarifariosMap) {
		this.tarifariosMap = tarifariosMap;
	}




	public static HashMap consultarTarifarios(Connection con, int codigoServicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosDao().consultarTarifarios(con, codigoServicio);
	}
	
	/**
	 * Método para cargar los formularios del servicvio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap<String, Object> cargarFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion)
	{
		return servicioDao().cargarFormulariosServicio(con, codigoServicio, codigoInstitucion);
	}
	
	


	/**
	 * Obtener minutos duracion servicio
	 * @param codigoServicio
	 * @param con
	 * @return
	 */
	public static int  obtenerMinutosDuracionServicio(int codigoServicio, Connection con) {
		return  servicioDao().obtenerMinutosDuracionServicio(codigoServicio, con); 
	}
	
	/**
	 * Método para cargar los formularios del servicvio ArrayList
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<DtoPlantillaServDiag> cargarFormulariosServicioArray(Connection con,int codigoServicio,int codigoInstitucion)
	{
		HashMap parametros = new HashMap();
		DtoPlantillaServDiag dto ;
		ArrayList<DtoPlantillaServDiag> array = new ArrayList<DtoPlantillaServDiag>();		
		parametros = servicioDao().cargarFormulariosServicio(con, codigoServicio, codigoInstitucion);
		
		for(int i=0; i<Utilidades.convertirAEntero(parametros.get("numRegistros").toString()); i++)
		{
			dto = new DtoPlantillaServDiag();
			dto.setCodigoDiagnostico(Utilidades.convertirAEntero(parametros.get("consecutivo_"+i).toString()));
			dto.setCodigoPkPlantilla(Utilidades.convertirAEntero(parametros.get("codigoPkPlantilla_"+i).toString()));
			dto.setCodigoPlantilla(Utilidades.convertirAEntero(parametros.get("codigoPlantilla_"+i).toString()));
			dto.setDescripcionPlantilla(parametros.get("nombrePlantilla_"+i).toString());
			dto.setCodigoDiagnostico(Utilidades.convertirAEntero(parametros.get("codigoDiagnostico_"+i).toString()));
			dto.setDescripcionDiagnostico(parametros.get("nombreDiagnostico_"+i).toString());
			
			array.add(dto);
		}
		
		return array;
	}
	
	
	/**
	 * Método para cargar el numero de formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static int getNumeroFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion)
	{
		return servicioDao().getNumeroFormulariosServicio(con,codigoServicio,codigoInstitucion);
	}
	
	
	public String getCosto() {
		return costo;
	}
	
	public void setCosto(String costo) {
		this.costo = costo;
	}

	/**
	 * @return the requiereDiagnostico
	 */
	public String getRequiereDiagnostico() {
		return requiereDiagnostico;
	}


	/**
	 * @param requiereDiagnostico the requiereDiagnostico to set
	 */
	public void setRequiereDiagnostico(String requiereDiagnostico) {
		this.requiereDiagnostico = requiereDiagnostico;
	}


	/**
	 * @return
	 */
	public int getCodigoServicioPortatil() {
		return codigoServicioPortatil;
	}


	/**
	 * @param codigoServicioPortatil
	 */
	public void setCodigoServicioPortatil(int codigoServicioPortatil) {
		this.codigoServicioPortatil = codigoServicioPortatil;
	}


	/**
	 * @return
	 */
	public String getDesServicioPortatil() {
		return desServicioPortatil;
	}

	/**
	 * @param desServicioPortatil
	 */
	public void setDesServicioPortatil(String desServicioPortatil) {
		this.desServicioPortatil = desServicioPortatil;
	}



	public String getPosSubsidiado() {
		return posSubsidiado;
	}



	public void setPosSubsidiado(String posSubsidiado) {
		this.posSubsidiado = posSubsidiado;
	}



	/**
	 * @return the codigoCups
	 */
	public String getCodigoCups() {
		return codigoCups;
	}



	/**
	 * @param codigoCups the codigoCups to set
	 */
	public void setCodigoCups(String codigoCups) {
		this.codigoCups = codigoCups;
	}

	/**
	 * @return the formulario
	 */
	public HashMap<String, Object> getFormulario() {
		return formulario;
	}

	/**
	 * @param formulario the formulario to set
	 */
	public void setFormulario(HashMap<String, Object> formulario) {
		this.formulario = formulario;
	}

	public String getAtencionOdonlogica() {
		return atencionOdonlogica;
	}

	public void setAtencionOdonlogica(String atencionOdonlogica) {
		this.atencionOdonlogica = atencionOdonlogica;
	}

	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	public int getMinutosDuracion() {
		return minutosDuracion;
	}

	public void setMinutosDuracion(int minutosDuracion) {
		this.minutosDuracion = minutosDuracion;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public InfoDatos getInformacionSonria() {
		return informacionSonria;
	}

	public void setInformacionSonria(InfoDatos informacionSonria) {
		this.informacionSonria = informacionSonria;
	}
}
