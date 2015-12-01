/*
 * @(#)Articulo.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.Answer;
import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;


import com.princetonsa.dao.ArticuloDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.solicitudes.DocumentosAdjuntos;

/**
 * Esta clase encapsula los atributos y la funcionalidad necesarias para manejar un artículo de farmacia de un hospital.
 *
 * @version 1.0, Oct 29, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Articulo {

	private Logger logger=Logger.getLogger(Articulo.class);
	/**
	 * Código del artículo
	 */
	private String codigo;

	/**
	 * Clase del artículo.
	 */
	private String clase;
	/**
	 * Grupo del articulo
	 */
	private String grupo;

	/**
	 *Subgrupo del artículo
	 */
	private String subgrupo;
		
	/**
	 * Naturaleza del articulo
	 */
	private String naturaleza;
	
	/**
	 * Forma farmaceutica del artículo.
	 */
	private String formaFarmaceutica;
	/**
	 * Concentracion del artículo.
	 */
	private String concentracion="";
	
	/**
	 * Unidad de medida del artículo.
	 */
	private String unidadMedida;
	/**
	 * Nombre del artículo.
	 */
	private String minsalud;

	/**
	 * Descripción del artículo.
	 */
	private String descripcion;

	/**
	 * Nombre de la naturaleza
	 */
	private String nomNaturaleza;
	/**
	 * Nombre de la forma farmaceutica
	 */
	private String nomFormaFarmaceutica;
	/**
	 * nombre Unidad de Medida
	 */
	private String nomUnidadMedida;
	
	/**
	 * Neturaleza del artículo POS o NO_POS
	 */
	private String esPos;
	/**
	 * Estado del artículo.
	 */
	private boolean estadoArticulo;
	
	
	private static ArticuloDao articuloDao = null;

	////////////////CAMPOS NECESARIOS PARA EL MANEJO DE INVENTARIOS
	
	/**
	 * Fecha de Creacion del articulo
	 */
	private String fechaCreacion;
	
	/**
	 * Stock Minimo del Articulo.
	 */
	private int stockMinimo;
	
	/**
	 * Stock maximo del articulo
	 */
	private int stockMaximo;
	
	/**
	 * Punto de pedido del articulo
	 */
	private int puntoPedido;
	
	/**
	 * Maneja la cantidad pormedio de compra del articulo.
	 */
	private int cantidadCompra;
	
	/**
	 * Costo promedio del articulo.
	 */
	private String costoPromedio;
	
	/**
	 * Costo donación del articulo.
	 */
	private double costoDonacion;
	
	/**
	 * Código Interfaz
	 */
	private String codigoInterfaz;
	
	/**
	 * Indicativo Automático
	 */
	private String indicativoAutomatico;
	
	/**
	 * Indicativo por completar
	 */
	private String indicativoPorCompletar;
	
	/**
	 * Categoria del articulo(Normal/Especial).
	 */
	private int categoria;
	
	/**
	 * Usuario que crea el articulo.
	 */
	private String usuario;
	
	/**
	 * Variable para la hora de creacion del registro
	 */
	private String horaCreacion;
	
	/**
	 * Stock Minimo del Articulo.
	 */
	private String busStockMinimo;
	
	/**
	 * Stock maximo del articulo
	 */
	private String busStockMaximo;
	
	/**
	 * Punto de pedido del articulo
	 */
	private String busPuntoPedido;
	
	/**
	 * Maneja la cantidad pormedio de compra del articulo.
	 */
	private String busCantidadCompra;
	
	/**
	 * Costo promedio del articulo.
	 */
	private String busCostoPromedio;
	
	/**
	 * 
	 */
	private String busPrecioCompraMasAlta;
	
	/**
	 * Código Interfaz
	 */
	private String busCodigoInterfaz;
	
	/**
	 * Indicativo Automático
	 */
	private String busIndicativoAutomatico;
	
	/**
	 * Indicativo por completar
	 */
	private String busIndicativoPorCompletar;
	
	/**
	 * Variable para manejar el registro INVIMA.
	 */

	private String registroINVIMA;
	
	/**
	 * Variable para manejar la cantidad maxima mes
	 */
	private double maximaCantidadMes;
	
	/**
	 * Variable para manejar la multidosis
	 */
	private String multidosis;
	
	/**
	 * Variable para establecer si maneja lotes
	 */
	private String manejaLotes;
	
	/**
	 * Variable para establecer si maneja fecha_vencimiento
	 */
	private String manejaFechaVencimiento;
	
	/**
	 *Variable para manejar el porcentaje iva 
	 */
	private double porcentajeIva;
	
	/**
	 *Variable para manejar el precio ultima compra 
	 */
	private double precioUltimaCompra;
	
	/**
	 * 
	 */
	private double precioCompraMasAlta;
	
	/**
	 *Variable para manejar el precio base venta 
	 */
	private double precioBaseVenta;
	
	/**
	 *Variable para manejar el fecha precio base venta 
	 */
	private String fechaPrecioBaseVenta;
	
	//Busquedas
	private String busMaximaCantidadMes;

	private String busPorcentajeIva;

	private String busPrecioUltimaCompra;

	private String busPrecioBaseVenta;
	
	
	/**
	 * Variable para saber si manejaba lotes o no
	 */
	private String manejaLotesValorAnt;
	
	/**
	 * Variable para saber si hay existencias del Articulo en AlmacenXLote
	 */
	private String existe;
	
	/**
	 *Grilla Vias Administracion Por Forma Farmaceutica 
	 */
	private HashMap viasAdminXFormaFarma = new HashMap();	
	
	/**
	 * Numero de Registros en Grilla Vias de Administracion Por Forma Farmaceutica
	 */
	private int numViasAdmiXFormaFarma;
	
	//////////////////////////////////////////////////////////////////////////////
	
	///////////Campos para la nueva informacion medica//////////////
	
	/**
	 * 
	 */
	private String tiempoEsp;
	
	/**
	 * 
	 */
	private String efectoDes;
	
	/**
	 * 
	 */
	private String efectosSec;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private String bibliografia;
	
	DocumentosAdjuntos documentosAdjuntos= new DocumentosAdjuntos();
	
	///////////////////////////////////////////////////////////////
	
	
	/**
	 *Grilla  Unidosis Articulo
	 */
	private HashMap unidosisXArticulo = new HashMap();	
	
	/**
	 * Numero de Registros en Grilla  Unidosis Articulo
	 */
	private int numUnidosisXArticulo;
	
	/**
	 * 
	 */
	private HashMap grupoEspecialArticulo = new HashMap();
	
	/**
	 * 
	 */
	private int numGrupoEspecialArticulo;
	
	//////Nuevo sn doc
	
	private String descripcionAlterna;
	
	
	private String esMedicamento;
	
	private String atencionOdon;
	
	/**
	 * Atributo que almacena el consecutivo del nivel de atención del articulo 
	 */
	private long nivelAtencion;
	
	/**
	 *Variable para manejar la institucion 
	 */
	//private int institucion;
	
	/**
	 * Mapa con la información del Codigo único de Medicamento CUM
	 * Contiene las siguientes llaves:
	 * - numero_expediente
	 * - cons_present_comercial
	 * - presentacion_comercial
	 * - clasificacion_atc
	 * - registro_art
	 * - vigencia
	 * - roles_x_producto
	 * - titular
	 * - fabricante
	 * - importador
	 */
	private HashMap cumMap;
	
	/**
	 * Constructora de la clase <code>Articulo</code>.
	 */
	public Articulo () {
		this.init(System.getProperty("TIPOBD"));
		this.clean();
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) {

		if (articuloDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			articuloDao = myFactory.getArticuloDao();
		}

	}
	
	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del artículo.
	 */
	public void clean () {
			this.setClase("");
			this.setCodigo("");
			this.setConcentracion("");
			this.setUnidadMedida("");
			this.setSubgrupo("");
			this.setMinsalud("");
			this.setDescripcion("");
			this.setNaturaleza("");
			this.setGrupo("");
			this.setFormaFarmaceutica("");
			this.setEstadoArticulo(false);
			this.fechaCreacion="";
			this.usuario="";
			this.horaCreacion="";
			this.stockMinimo=0;
			this.stockMaximo=0;
			this.puntoPedido=0;
			this.cantidadCompra=0;
			this.costoPromedio="0.0";
			this.costoDonacion=0.0;
			this.codigoInterfaz="";
			this.indicativoAutomatico="";
			this.indicativoPorCompletar="";
			this.categoria=ConstantesBD.codigoNuncaValido;
			this.busStockMinimo="";
			this.busStockMaximo="";
			this.busPuntoPedido="";
			this.busCantidadCompra="";
			this.busCostoPromedio="";
			this.busPrecioCompraMasAlta="";
			this.busCodigoInterfaz="";
			this.busIndicativoAutomatico="";
			this.busIndicativoPorCompletar="";
			this.registroINVIMA="";
			
			this.maximaCantidadMes=0;
			this.multidosis="N";
			this.manejaLotes="N";
			this.manejaFechaVencimiento="N";
			this.porcentajeIva=0;
			this.precioUltimaCompra=0;
			this.precioBaseVenta=0;
			this.precioCompraMasAlta=0;
			this.fechaPrecioBaseVenta="";	
			
			this.busMaximaCantidadMes="";
			this.busPorcentajeIva="";
			this.busPrecioUltimaCompra="";
			this.busPrecioBaseVenta="";
			
			this.manejaLotesValorAnt="";
			this.existe="";
			
			this.viasAdminXFormaFarma = new HashMap();
			this.numViasAdmiXFormaFarma=0;
			
			/////
			
			this.unidosisXArticulo= new HashMap();
			this.numUnidosisXArticulo=0;
			
			this.descripcionAlterna="";
			
			this.cumMap=new HashMap();
			
			this.esMedicamento="";
			this.atencionOdon="";
			
			this.setNivelAtencion(ConstantesBD.codigoNuncaValidoLong);

	}

	/**
	 * Inserta un artículo en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return numero de camas insertadas
	 */
	public int insertarArticulo(Connection con, int institucion, HashMap mapaViaAdm, HashMap mapaUnidosis, HashMap mapaGrupoEspecial)
	{
		logger.info("\n\n\n**********************LA ATENCION ODON------>"+this.atencionOdon);
		logger.info("fechaPrecioBaseVenta mundo-->"+this.fechaPrecioBaseVenta);
		return articuloDao.insertarArticulo(con,						//1 
											subgrupo, 					//2
		        							naturaleza,					//3
		        							formaFarmaceutica, 			//4
		        							concentracion, 				//5
		        							unidadMedida, 				//6
		        							estadoArticulo,				//7
		        							minsalud,					//8
		        							descripcion,				//9
		        							this.fechaCreacion,			//10
		        							this.usuario,				//11
		        							this.horaCreacion,			//12
		        							this.stockMinimo,			//13
		        							this.stockMaximo,			//14
		        							this.puntoPedido,			//15
		        							this.cantidadCompra,		//16	
		        							this.costoPromedio,			//17
		        							this.costoDonacion,			//18
		        							this.codigoInterfaz,		//19
		        							this.indicativoAutomatico,	//20	
		        							this.indicativoPorCompletar,//21
		        							this.categoria,				//22
		        							this.registroINVIMA,		//23
		        							 this.maximaCantidadMes,	//24	
		        							 this.multidosis,			//25
		        							 this.manejaLotes,			//26
		        							 this.manejaFechaVencimiento,//27
		        							 this.porcentajeIva,		//28
		        							 this.precioUltimaCompra,	//29	
		        							 this.precioBaseVenta,		//30
		        							 this.precioCompraMasAlta,	//31
		        							 this.descripcionAlterna,	//32	
		        							 this.fechaPrecioBaseVenta,	//33
		        							 institucion,				//34
		        							 mapaViaAdm,				//35
		        							 mapaUnidosis,				//36
		        							 mapaGrupoEspecial,			//37
		        							 this.cumMap,				//38
		        							 this.atencionOdon,			//39
		        							 this.nivelAtencion //40
		        							 
		        							 

	    
	    );
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param articulo
	 * @return
	 */
	public boolean insertarInformMed(Connection con, int codArticulo, Articulo articulo)
	{
	    return articuloDao.insertarInformMed(con, codArticulo, articulo);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public boolean insertarDocumentosAdjuntos(Connection con, int codigoArticulo)
	{
		try 
		{
			return this.documentosAdjuntos.insertarEliminarDocumentosAdjuntosTransaccionalIM(con, codigoArticulo, ConstantesBD.continuarTransaccion).isTrue();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Adición sebastián
	 * Método usado para insertar un atributo de la justificación de un artículo
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo
	 * @param descripcion
	 * @return
	 */
	public int insertarAtributoJustificacion(Connection con,int numeroSolicitud,int articulo,int atributo,String descripcion)
	{
		return articuloDao.insertarAtributoJustificacion(con,numeroSolicitud,articulo,atributo,descripcion);
	}
	

	/**
	 * Dada la identificacion de una cama, establece las propiedades de un objeto <code>Articulo</code>
	 * en los valores correspondientes.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoArticulo el código del articulo a insertar
	 */
	public void cargarArticulo(Connection con, int codigoArticulo)
	{
		Answer a=articuloDao.cargarArticulo(con, codigoArticulo);
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM=articuloDao.consultaInfoMArticulo(con, codigoArticulo);
		ResultSetDecorator rs = a.getResultSet();
		ResultSetDecorator rsDescripciones;
		this.clean();
		try
		{
			if (rs.next())
			{
				String tempo;
				int tempoInt;
				double tempoDbl;  
				
				setClase(rs.getString("clase").trim());
				setGrupo(rs.getString("grupo").trim());
				setSubgrupo(rs.getString("subgrupo").trim());
				setNaturaleza(rs.getString("naturaleza").trim());
				tempo=rs.getString("forma_farmaceutica");
				if(tempo!=null)
				{
					setFormaFarmaceutica(tempo);
				}
				else
				{
					setFormaFarmaceutica("");
				}
				tempo=rs.getString("concentracion");
				if(tempo!=null)
				{
					setConcentracion(tempo);
				}
				else
				{
					setConcentracion("");
				}
				tempo=rs.getString("unidad_medida");
				if(tempo!=null)
				{
					setUnidadMedida(tempo);
				} 
				else
				{
					setUnidadMedida("");
				}
				tempo=rs.getString("minsalud");
				if(tempo!=null)
				{
					setMinsalud(tempo);
				}
				else
				{
					setMinsalud("");
				}
				setDescripcion(rs.getString("descripcion").trim());
				setEstadoArticulo(rs.getBoolean("estado"));
				setCodigo(rs.getString("codigo").trim());
				setFechaCreacion(rs.getString("fecha"));
				setUsuario(rs.getString("usuario"));
				setCategoria(rs.getInt("categoria"));
				setStockMinimo(rs.getInt("stockminimo"));
				setStockMaximo(rs.getInt("stockmaximo"));
				setPuntoPedido(rs.getInt("puntopedido"));
				setCantidadCompra(rs.getInt("cantidadcompra"));
				setCostoPromedio(rs.getString("costopromedio"));
				setCostoDonacion(rs.getDouble("costodonacion"));
				setCodigoInterfaz(rs.getString("codigointerfaz"));
				setIndicativoAutomatico(rs.getString("indicativoautomatico"));
				setIndicativoPorCompletar(rs.getString("indicativoporcompletar"));
				setRegistroINVIMA(rs.getString("registroinvima"));
				setEsMedicamento(rs.getString("es_medicamento"));
				setAtencionOdon(rs.getString("atencionodontologica"));
				
				tempoInt =rs.getInt("maxima_cantidad_mes");
				if(tempoInt==-1)//en la consulta agregar un case para ver si es nulo
				{
					setMaximaCantidadMes(0);
				}
				else
				{
					setMaximaCantidadMes(rs.getInt("maxima_cantidad_mes"));
				}

				setMultidosis(rs.getString("multidosis"));
				setManejaLotes(rs.getString("maneja_lotes"));

				//Guarda si manejaba lotes o no
				setManejaLotesValorAnt(rs.getString("maneja_lotes"));
				
				setManejaFechaVencimiento(rs.getString("maneja_fecha_vencimiento"));
				setPorcentajeIva(rs.getDouble("porcentaje_iva"));
				
				tempoDbl =rs.getDouble("precio_ultima_compra");
				if(tempoDbl==-1)//en la consulta agregar un case para ver si es nulo
				{
					setPrecioUltimaCompra(0);
				}
				else
				{
					setPrecioUltimaCompra(tempoDbl);
				}
				
				tempoDbl =rs.getDouble("precio_base_venta");
				if(tempoDbl==-1)//en la consulta agregar un case para ver si es nulo
				{
					setPrecioBaseVenta(0);
				}
				else
				{
					setPrecioBaseVenta(tempoDbl);
				}
				tempoDbl =rs.getDouble("precio_compra_mas_alta");
				if(tempoDbl==-1)//en la consulta agregar un case para ver si es nulo
				{
					setPrecioCompraMasAlta(0);
				}
				else
				{
					setPrecioCompraMasAlta(tempoDbl);
				}
				
				//
				tempo=rs.getString("fecha_precio_base_venta");
				if(tempo!=null)
				{
					setFechaPrecioBaseVenta(tempo);
				}
				else
				{
					setFechaPrecioBaseVenta("");
				}
				tempo=rs.getString("descripcionalterna");
				if(tempo!=null)
				{
					setDescripcionAlterna(tempo);
				}
				else
				{
					setDescripcionAlterna("");
				}
				
				Long temp=rs.getLong("nivelatencion");
				if(temp!=null)
				{
					setNivelAtencion(temp.longValue());
				}
				else
				{
					setNivelAtencion(ConstantesBD.codigoNuncaValidoLong);
				}
				
				// Seccion Codigo único de Medicamento CUM
				HashMap cumMapAux = new HashMap();
				cumMapAux.put("numero_expediente", rs.getString("numero_expediente"));
				cumMapAux.put("cons_present_comercial", rs.getString("cons_present_comercial"));
				cumMapAux.put("presentacion_comercial", rs.getString("presentacion_comercial"));
				cumMapAux.put("clasificacion_atc", rs.getString("clasificacion_atc"));
				cumMapAux.put("registro_art", rs.getString("registro_art"));
				cumMapAux.put("vigencia", rs.getString("vigencia"));
				cumMapAux.put("roles_x_producto", rs.getString("roles_x_producto"));
				cumMapAux.put("titular", rs.getString("titular"));
				cumMapAux.put("fabricante", rs.getString("fabricante"));
				cumMapAux.put("importador", rs.getString("importador"));
				setCumMap(cumMapAux);
				
				Utilidades.imprimirMapa(cumMapAux);
				
				///
				setExiste(rs.getString("existe"));
				///
				//
			}
			//-----Carga datos Informacion medica Articulo-----//
			
			if(Integer.parseInt(mapaM.get("numRegistros").toString())>0)
			{
				setTiempoEsp(mapaM.get("tiempoRespEsperado_0").toString());
				setEfectoDes(mapaM.get("efectoDeseado_0").toString());
				setEfectosSec(mapaM.get("efectoSecundario_0").toString());
				setObservaciones(mapaM.get("observaciones_0").toString());
				setBibliografia(mapaM.get("bibliografia_0").toString());
			}
			
			//-------------------Fin---------------------------//
			rsDescripciones=articuloDao.cargarArticuloDescripcion(con,codigoArticulo);
			if(rsDescripciones.next())
			{
				setNomFormaFarmaceutica(rsDescripciones.getString("formaFarmaceutica"));
				setNomNaturaleza(rsDescripciones.getString("naturaleza"));
				setNomUnidadMedida(rsDescripciones.getString("unidadMedida"));
				setEsPos((rsDescripciones.getString("es_pos")+"").equals("null")?"":rsDescripciones.getString("es_pos"));
			}
			//Cargar Grilla de Vias de Administracion
			this.setViasAdminXFormaFarma(articuloDao.cargarViasAdminArticulo(con, codigoArticulo) );
			this.numViasAdmiXFormaFarma=Integer.parseInt(this.getViasAdminXFormaFarma("numRegistros")+"");
			
			this.setUnidosisXArticulo(articuloDao.cargarUnidosisArticulo(con, codigoArticulo));
			this.numUnidosisXArticulo=Integer.parseInt(this.getUnidosisXArticulo("numRegistros")+"");
			
			this.setGrupoEspecialArticulo(articuloDao.cargarGrupoEspecialesArticulo(con, codigoArticulo));
			this.numGrupoEspecialArticulo=Utilidades.convertirAEntero(this.getGrupoEspecialArticulo("numRegistros")+"");

		}
		catch (SQLException e)
		{
			logger.error("Error cargando el artículo "+e);
		}
	}
	
	/**
	 * Consulta y Carga La informacion Medica de una Articulo Seleccionado
	 */
	public static HashMap<String, Object> cargarInfoMArticulo(Connection con, int codigoArticulo)
	{
		return getArticuloDao().consultaInfoMArticulo(con, codigoArticulo);
	}
	
	/**
	 * Método encargado de consultar los 4 códigos minSalud para un articulo tipo medicamento
	 * @author Felipe Pérez Granda
	 * @param con Conexión 
	 * @param codigoArticulo El código del artículo
	 * @return HashMap<String,Object>
	 */
	public static HashMap<String,Object> cargarMinSalud(Connection con, int codigoArticulo)
	{
		return getArticuloDao().cargarMinSalud(con, codigoArticulo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static HashMap<String, Object> cargarInfoMAdjArticulo(Connection con, int codigoArticulo)
	{
		return getArticuloDao().consultaInfoMAdjArticulo(con, codigoArticulo);
	}

	/**
	 * Modifica una cama en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCama el código de la cama que desea modificar
	 * @return numero de camas modificadas
	 */
	public int modificarArticulo(Connection con, String codigoArticulo,int institucion, boolean manejoLoteDeSaN, HashMap mapaViaAdm, HashMap mapaUnidosis, HashMap mapaGrupoEspecial, String usuario)
	{
		return articuloDao.modificarArticulo(con, Integer.parseInt(codigoArticulo), descripcion, naturaleza, minsalud, formaFarmaceutica, concentracion, unidadMedida, estadoArticulo,this.stockMinimo,this.stockMaximo,this.puntoPedido,this.cantidadCompra,this.costoPromedio,this.codigoInterfaz,this.indicativoAutomatico,this.indicativoPorCompletar,this.categoria,this.registroINVIMA, this.maximaCantidadMes,this.multidosis, this.manejaLotes, this.manejaFechaVencimiento, this.porcentajeIva, this.precioUltimaCompra, this.precioBaseVenta, this.precioCompraMasAlta, this.descripcionAlterna, this.fechaPrecioBaseVenta, institucion, manejoLoteDeSaN, mapaViaAdm, mapaUnidosis, mapaGrupoEspecial, usuario, this.cumMap, this.nivelAtencion);		
	}
	
	/**
	 * 
	 */
	public int modificarInfoMArticulo(Connection con, String codigoArticulo)
	{
		return articuloDao.modificarInfoMArticulo(con, Integer.parseInt(codigoArticulo), tiempoEsp, efectoDes, efectosSec, observaciones, bibliografia);		
	}
	
	/**
	 * Método encargado de modificar la información medica de un artículo cuya naturaleza sea Medicamento
	 * @param con
	 * @param codigo
	 * @param tiempoEsp
	 * @param efectoDes
	 * @param efectosSec
	 * @param observaciones
	 * @param bibliografia
	 * @return true/false boolean
	 */
	public boolean modificarInfoMedicaArticulo(Connection con, String codigoArticulo)
	{
		return articuloDao.modificarInfoMedicaArticulo(con, Integer.parseInt(codigoArticulo), tiempoEsp, efectoDes, efectosSec, observaciones, bibliografia);
	}
	
	/**
	 * Metodo para eliminar un documento adjunto de un articulo info medica
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAdjIM(Connection con, String codigo)
	{
		return articuloDao.eliminarAdjIM(con, codigo);
	}

	/**
	 * Obtener el codigo del último artículo modificado
	 * @param con
	 * @return
	 */
	public int getUltimoModificado(Connection con)
	{
		return articuloDao.codigoUltimoArticulo(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param buscarEstado
	 * @param institucion 
	 * @return
	 */
	public Collection buscar(Connection con, boolean buscarEstado, int institucion)
	{
		return articuloDao.buscar(con, getClase(), getGrupo(), getSubgrupo(), getCodigo(), getDescripcion(), getNaturaleza(), getMinsalud(), getFormaFarmaceutica(), getConcentracion(), getUnidadMedida(), getEstadoArticulo(), buscarEstado,getBusStockMinimo(),getBusStockMaximo(),getBusPuntoPedido(),getBusCostoPromedio(), getBusPrecioCompraMasAlta(), getBusCodigoInterfaz(),getBusIndicativoAutomatico(),getBusIndicativoPorCompletar(),getBusCantidadCompra(),getCategoria(),getFechaCreacion(),getRegistroINVIMA(), getBusMaximaCantidadMes(),getMultidosis(),getManejaLotes(),getManejaFechaVencimiento(),getBusPorcentajeIva(),getBusPrecioUltimaCompra(),getBusPrecioBaseVenta(),getFechaPrecioBaseVenta(), institucion, getDescripcionAlterna(),getNivelAtencion());
		
		
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public boolean insertarViasAdminArticulo(Connection con, HashMap mapaViaAdmArt)
	{
		return articuloDao.insertarViasAdminArticulo(con,mapaViaAdmArt);
	}	

	/**
	 * 
	 * @param con
	 * @param valor
	 * @param codigo
	 * @param tarifa
	 * @return
	 */
	public boolean modificarTarifas(Connection con, double valor, int codigo, String tarifa)
	{
		return articuloDao.modificarTarifas(con,valor,codigo,tarifa);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarViasAdminArticulo(Connection con,int codigo)
	{
		return articuloDao.cargarViasAdminArticulo(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public static HashMap consultarUbicacion(Connection con, Articulo a)
	{
		return articuloDao.consultarUbicacion(con, a);
		
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaViaAdmArt
	 * @return
	 */
	public  boolean actualizarViasAdminArticulo(Connection con, HashMap mapaViaAdmArt) 
	{
		return articuloDao.actualizarViasAdminArticulo(con, mapaViaAdmArt);
	}		
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarUnidosisArticulo(Connection con,int codigo)
	{
		return articuloDao.cargarUnidosisArticulo(con, codigo);
	}
	
	
	/**
	 * @return Retorna articuloDao.
	 */
	public static ArticuloDao getArticuloDao()
	{
		return articuloDao;
	}
	/**
	 * @param articuloDao Asigna articuloDao.
	 */
	public static void setArticuloDao(ArticuloDao articuloDao)
	{
		Articulo.articuloDao = articuloDao;
	}
	/**
	 * @return Retorna clase.
	 */
	public String getClase()
	{
		return clase;
	}
	/**
	 * @param clase Asigna clase.
	 */
	public void setClase(String clase)
	{
		this.clase = clase;
	}
	/**
	 * @return Retorna codigo.
	 */
	public String getCodigo()
	{
		return codigo;
	}
	/**
	 * @param codigo Asigna codigo.
	 */
	public void setCodigo(String codigo)
	{
		this.codigo = codigo;
	}
	/**
	 * @return Retorna concentracion.
	 */
	public String getConcentracion()
	{
		return concentracion;
	}
	/**
	 * @param concentracion Asigna concentracion.
	 */
	public void setConcentracion(String concentracion)
	{
		this.concentracion = concentracion;
	}
	/**
	 * @return Retorna descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}
	/**
	 * @param descripcion Asigna descripcion.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	/**
	 * @return Retorna estadoArticulo.
	 */
	public boolean getEstadoArticulo()
	{
		return estadoArticulo;
	}
	/**
	 * @param estadoArticulo Asigna estadoArticulo.
	 */
	public void setEstadoArticulo(boolean estadoArticulo)
	{
		this.estadoArticulo = estadoArticulo;
	}
	/**
	 * @return Retorna formaFarmaceutica.
	 */
	public String getFormaFarmaceutica()
	{
		return formaFarmaceutica;
	}
	/**
	 * @param formaFarmaceutica Asigna formaFarmaceutica.
	 */
	public void setFormaFarmaceutica(String formaFarmaceutica)
	{
		this.formaFarmaceutica = formaFarmaceutica;
	}
	/**
	 * @return Retorna grupo.
	 */
	public String getGrupo()
	{
		return grupo;
	}
	/**
	 * @param grupo Asigna grupo.
	 */
	public void setGrupo(String grupo)
	{
		this.grupo = grupo;
	}
	/**
	 * @return Retorna minsalud.
	 */
	public String getMinsalud()
	{
		return minsalud;
	}
	/**
	 * @param minsalud Asigna minsalud.
	 */
	public void setMinsalud(String minsalud)
	{
		this.minsalud = minsalud;
	}
	/**
	 * @return Retorna naturaleza.
	 */
	public String getNaturaleza()
	{
		return naturaleza;
	}
	/**
	 * @param naturaleza Asigna naturaleza.
	 */
	public void setNaturaleza(String naturaleza)
	{
		this.naturaleza = naturaleza;
	}
	/**
	 * @return Retorna subgrupo.
	 */
	public String getSubgrupo()
	{
		return subgrupo;
	}
	/**
	 * @param subgrupo Asigna subgrupo.
	 */
	public void setSubgrupo(String subgrupo)
	{
		this.subgrupo = subgrupo;
	}
	/**
	 * @return Retorna unidadMedida.
	 */
	public String getUnidadMedida()
	{
		return unidadMedida;
	}
	/**
	 * @param unidadMedida Asigna unidadMedida.
	 */
	public void setUnidadMedida(String unidadMedida)
	{
		this.unidadMedida = unidadMedida;
	}
	/**
	 * @return Retorna el nomFormaFarmaceutica.
	 */
	public String getNomFormaFarmaceutica() {
		return nomFormaFarmaceutica;
	}
	/**
	 * @param nomFormaFarmaceutica Asigna el nomFormaFarmaceutica.
	 */
	public void setNomFormaFarmaceutica(String nomFormaFarmaceutica) {
		this.nomFormaFarmaceutica = nomFormaFarmaceutica;
	}
	/**
	 * @return Retorna el nomNaturalez.
	 */
	public String getNomNaturaleza() {
		return nomNaturaleza;
	}
	/**
	 * @param nomNaturalez Asigna el nomNaturalez.
	 */
	public void setNomNaturaleza(String nomNaturalez) {
		this.nomNaturaleza = nomNaturalez;
	}
	/**
	 * @return Retorna el nomUnidadMedida.
	 */
	public String getNomUnidadMedida() {
		return nomUnidadMedida;
	}
	/**
	 * @param nomUnidadMedida Asigna el nomUnidadMedida.
	 */
	public void setNomUnidadMedida(String nomUnidadMedida) {
		this.nomUnidadMedida = nomUnidadMedida;
	}
	
	/**
	 * Método que devuelve un string con todo lo necesario del medicamento
	 * para presentar en la vista
	 * 1-ACETAMINOFEN-Medicamento Pos-R0887A3498-CAPSULAS-500-MG
	 * @return String con los atributos del medicamento
	 */
	public String getStringMedicamentoCompleto()
	{
		return 
			getCodigo()+"-"+
			getDescripcion()+"-"+
			getNomNaturaleza()+"-"+
			getMinsalud()+"-"+
			getNomFormaFarmaceutica()+"-"+
			getConcentracion()+"-"+
			getNomUnidadMedida()+"-"+
			getEsPos();
	}

	/**
	 * Método que devuelve un Vector con todo lo necesario del medicamento
	 * para presentar en la vista
	 * @return Vector con los atributos del medicamento
	 */
	public Vector getVectorMedicamentoCompleto()
	{
		Vector vector = new Vector();
		vector.add(new Integer(getCodigo()));
		vector.add(getDescripcion());
		vector.add(getMinsalud());
		vector.add(getNomFormaFarmaceutica());
		vector.add(getConcentracion());
		vector.add(getNomUnidadMedida());
		vector.add(getEsPos());
		return vector; 
	}

	/**
	 * @return Retorna esPos.
	 */
	public String getEsPos()
	{
		return esPos;
	}
	/**
	 * @param esPos Asigna esPos.
	 */
	public void setEsPos(String esPos)
	{
		this.esPos = esPos;
	}
    /**
     * @return Returns the cantidadCompra.
     */
    public int getCantidadCompra()
    {
        return cantidadCompra;
    }
    /**
     * @param cantidadCompra The cantidadCompra to set.
     */
    public void setCantidadCompra(int cantidadCompra)
    {
        this.cantidadCompra = cantidadCompra;
    }
    /**
     * @return Returns the categoria.
     */
    public int getCategoria()
    {
        return categoria;
    }
    /**
     * @param categoria The categoria to set.
     */
    public void setCategoria(int categoria)
    {
        this.categoria = categoria;
    }
    /**
     * @return Returns the costoPromedio.
     */
    public String getCostoPromedio()
    {
        return costoPromedio;
    }
    /**
     * @param costoPromedio The costoPromedio to set.
     */
    public void setCostoPromedio(String costoPromedio)
    {
        this.costoPromedio = costoPromedio;
    }
    /**
     * @return Returns the fechaCreacion.
     */
    public String getFechaCreacion()
    {
        return fechaCreacion;
    }
    /**
     * @param fechaCreacion The fechaCreacion to set.
     */
    public void setFechaCreacion(String fechaCreacion)
    {
        this.fechaCreacion = fechaCreacion;
    }
    /**
     * @return Returns the puntoPedido.
     */
    public int getPuntoPedido()
    {
        return puntoPedido;
    }
    /**
     * @param puntoPedido The puntoPedido to set.
     */
    public void setPuntoPedido(int puntoPedido)
    {
        this.puntoPedido = puntoPedido;
    }
    /**
     * @return Returns the stockMaximo.
     */
    public int getStockMaximo()
    {
        return stockMaximo;
    }
    /**
     * @param stockMaximo The stockMaximo to set.
     */
    public void setStockMaximo(int stockMaximo)
    {
        this.stockMaximo = stockMaximo;
    }
    /**
     * @return Returns the stockMinimo.
     */
    public int getStockMinimo()
    {
        return stockMinimo;
    }
    /**
     * @param stockMinimo The stockMinimo to set.
     */
    public void setStockMinimo(int stockMinimo)
    {
        this.stockMinimo = stockMinimo;
    }
    /**
     * @return Returns the usuario.
     */
    public String getUsuario()
    {
        return usuario;
    }
    /**
     * @param usuario The usuario to set.
     */
    public void setUsuario(String usuario)
    {
        this.usuario = usuario;
    }

    /**
     * @return Returns the busCantidadCompra.
     */
    public String getBusCantidadCompra()
    {
        return busCantidadCompra;
    }
    /**
     * @param busCantidadCompra The busCantidadCompra to set.
     */
    public void setBusCantidadCompra(String busCantidadCompra)
    {
        this.busCantidadCompra = busCantidadCompra;
    }
    /**
     * @return Returns the busCostoPromedio.
     */
    public String getBusCostoPromedio()
    {
        return busCostoPromedio;
    }
    /**
     * @param busCostoPromedio The busCostoPromedio to set.
     */
    public void setBusCostoPromedio(String busCostoPromedio)
    {
        this.busCostoPromedio = busCostoPromedio;
    }
    /**
     * @return Returns the busPuntoPedido.
     */
    public String getBusPuntoPedido()
    {
        return busPuntoPedido;
    }
    /**
     * @param busPuntoPedido The busPuntoPedido to set.
     */
    public void setBusPuntoPedido(String busPuntoPedido)
    {
        this.busPuntoPedido = busPuntoPedido;
    }
    /**
     * @return Returns the busStockMaximo.
     */
    public String getBusStockMaximo()
    {
        return busStockMaximo;
    }
    /**
     * @param busStockMaximo The busStockMaximo to set.
     */
    public void setBusStockMaximo(String busStockMaximo)
    {
        this.busStockMaximo = busStockMaximo;
    }
    /**
     * @return Returns the busStockMinimo.
     */
    public String getBusStockMinimo()
    {
        return busStockMinimo;
    }
    /**
     * @param busStockMinimo The busStockMinimo to set.
     */
    public void setBusStockMinimo(String busStockMinimo)
    {
        this.busStockMinimo = busStockMinimo;
    }

	public String getRegistroINVIMA() {
		return registroINVIMA;
	}

	public void setRegistroINVIMA(String registroINVIMA) {
		this.registroINVIMA = registroINVIMA;
	}

	/**
	 * Metodo que indica si un articulo maneja lote
	 * @param con
	 * @param codigoArticulo
	 */
	public static boolean articuloManejaLote(Connection con, int codigoArticulo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().articuloManejaLote(con,codigoArticulo,institucion);
	}
	
	/**
	 * Metodo que indica si un articulo maneja lote
	 * @param con
	 * @param codigoArticulo
	 */
	public static boolean articuloManejaLote( int codigoArticulo,int institucion)
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().articuloManejaLote(con,codigoArticulo,institucion);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * Metodo que indica si un articulo maneja fecha de vencimiento
	 * @param con
	 * @param codigoArticulo
	 */
	public static boolean articuloManejaFechaVencimiento(Connection con, int codigoArticulo,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().articuloManejaFechaVencimiento(con,codigoArticulo,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerCodigoInterfazNaturalezaArticulo(Connection con, int codigoArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().obtenerCodigoInterfazNaturalezaArticulo(con, codigoArticulo);
	}
	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean esMedicamento(int codigoArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().esMedicamento(codigoArticulo);
	}
	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean esPos(int codigoArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().esPos(codigoArticulo);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public String getFechaPrecioBaseVenta() {
		return fechaPrecioBaseVenta;
	}
	
	/**
	 * 
	 * @param fechaPrecioBaseVenta
	 */
	public void setFechaPrecioBaseVenta(String fechaPrecioBaseVenta) {
		this.fechaPrecioBaseVenta = fechaPrecioBaseVenta;
	}

	/**
	 * 
	 * @return
	 */
	public String getManejaFechaVencimiento() {
		return manejaFechaVencimiento;
	}
	
	/**
	 * 
	 * @param manejaFechaVencimiento
	 */
	public void setManejaFechaVencimiento(String manejaFechaVencimiento) {
		this.manejaFechaVencimiento = manejaFechaVencimiento;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getManejaLotes() {
		return manejaLotes;
	}
	
	/**
	 * 
	 * @param manejaLotes
	 */
	public void setManejaLotes(String manejaLotes) {
		this.manejaLotes = manejaLotes;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMaximaCantidadMes() {
		return maximaCantidadMes;
	}
	
	/**
	 * 
	 * @param maximaCantidadMes
	 */
	public void setMaximaCantidadMes(double maximaCantidadMes) {
		this.maximaCantidadMes = maximaCantidadMes;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMultidosis() {
		return multidosis;
	}
	
	/**
	 * 
	 * @param multidosis
	 */
	public void setMultidosis(String multidosis) {
		this.multidosis = multidosis;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPorcentajeIva() {
		return porcentajeIva;
	}
	
	/**
	 * 
	 * @param porcentajeIva
	 */
	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPrecioBaseVenta() {
		return precioBaseVenta;
	}
	
	/**
	 * 
	 * @param precioBaseVenta
	 */
	public void setPrecioBaseVenta(double precioBaseVenta) {
		this.precioBaseVenta = precioBaseVenta;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPrecioUltimaCompra() {
		return precioUltimaCompra;
	}
	
	/**
	 * 
	 * @param precioUltimaCompra
	 */
	public void setPrecioUltimaCompra(double precioUltimaCompra) {
		this.precioUltimaCompra = precioUltimaCompra;
	}

	/**
	 * 
	 * @return
	 */
	public String getBusPorcentajeIva() {
		return busPorcentajeIva;
	}
	
	/**
	 * 
	 * @param busPorcentajeIva
	 */
	public void setBusPorcentajeIva(String busPorcentajeIva) {
		this.busPorcentajeIva = busPorcentajeIva;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBusPrecioBaseVenta() {
		return busPrecioBaseVenta;
	}
	
	/**
	 * 
	 * @param busPrecioBaseVenta
	 */
	public void setBusPrecioBaseVenta(String busPrecioBaseVenta) {
		this.busPrecioBaseVenta = busPrecioBaseVenta;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBusPrecioUltimaCompra() {
		return busPrecioUltimaCompra;
	}
	
	/**
	 * 
	 * @param busPrecioUltimaCompra
	 */
	public void setBusPrecioUltimaCompra(String busPrecioUltimaCompra) {
		this.busPrecioUltimaCompra = busPrecioUltimaCompra;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBusMaximaCantidadMes() {
		return busMaximaCantidadMes;
	}
	
	/**
	 * 
	 * @param busMaximaCantidadMes
	 */
	public void setBusMaximaCantidadMes(String busMaximaCantidadMes) {
		this.busMaximaCantidadMes = busMaximaCantidadMes;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getManejaLotesValorAnt() {
		return manejaLotesValorAnt;
	}
	
	/**
	 * 
	 * @param manejaLotesValorAnt
	 */
	public void setManejaLotesValorAnt(String manejaLotesValorAnt) {
		this.manejaLotesValorAnt = manejaLotesValorAnt;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getExiste() {
		return existe;
	}
	
	/**
	 * 
	 * @param existe
	 */
	public void setExiste(String existe) {
		this.existe = existe;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumViasAdmiXFormaFarma() {
		return numViasAdmiXFormaFarma;
	}
	
	/**
	 * 
	 * @param numViasAdmiXFormaFarma
	 */
	public void setNumViasAdmiXFormaFarma(int numViasAdmiXFormaFarma) {
		this.numViasAdmiXFormaFarma = numViasAdmiXFormaFarma;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getViasAdminXFormaFarma() {
		return viasAdminXFormaFarma;
	}
	
	/**
	 * 
	 * @param viasAdminXFormaFarma
	 */
	public void setViasAdminXFormaFarma(HashMap viasAdminXFormaFarma) {
		this.viasAdminXFormaFarma = viasAdminXFormaFarma;
	}		
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getViasAdminXFormaFarma(String key) {
		return viasAdminXFormaFarma.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setViasAdminXFormaFarma(String key,Object obj) {
		this.viasAdminXFormaFarma.put(key,obj);
	}

	/**
	 * 
	 * @return
	 */
	public int getNumUnidosisXArticulo() {
		return numUnidosisXArticulo;
	}
	
	/**
	 * 
	 * @param numUnidosisXArticulo
	 */
	public void setNumUnidosisXArticulo(int numUnidosisXArticulo) {
		this.numUnidosisXArticulo = numUnidosisXArticulo;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getUnidosisXArticulo() {
		return unidosisXArticulo;
	}
	
	/**
	 * 
	 * @param unidosisXArticulo
	 */
	public void setUnidosisXArticulo(HashMap unidosisXArticulo) {
		this.unidosisXArticulo = unidosisXArticulo;
	}		
	
	

	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	public Object getUnidosisXArticulo(String key) {
		return unidosisXArticulo.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setUnidosisXArticulo(String key,Object obj) {
		this.unidosisXArticulo.put(key,obj);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}
	
	/**
	 * 
	 * @param codigoInterfaz
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getCostoDonacion() {
		return costoDonacion;
	}
	
	/**
	 * 
	 * @param costoDonacion
	 */
	public void setCostoDonacion(double costoDonacion) {
		this.costoDonacion = costoDonacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getIndicativoAutomatico() {
		return indicativoAutomatico;
	}
	
	/**
	 * 
	 * @param indicativoAutomatico
	 */
	public void setIndicativoAutomatico(String indicativoAutomatico) {
		this.indicativoAutomatico = indicativoAutomatico;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getIndicativoPorCompletar() {
		return indicativoPorCompletar;
	}
	
	/**
	 * 
	 * @param indicativoPorCompletar
	 */
	public void setIndicativoPorCompletar(String indicativoPorCompletar) {
		this.indicativoPorCompletar = indicativoPorCompletar;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBusCodigoInterfaz() {
		return busCodigoInterfaz;
	}
	
	/**
	 * 
	 * @param busCodigoInterfaz
	 */
	public void setBusCodigoInterfaz(String busCodigoInterfaz) {
		this.busCodigoInterfaz = busCodigoInterfaz;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBusIndicativoAutomatico() {
		return busIndicativoAutomatico;
	}
	
	/**
	 * 
	 * @param busIndicativoAutomatico
	 */
	public void setBusIndicativoAutomatico(String busIndicativoAutomatico) {
		this.busIndicativoAutomatico = busIndicativoAutomatico;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBusIndicativoPorCompletar() {
		return busIndicativoPorCompletar;
	}
	
	/**
	 * 
	 * @param busIndicativoPorCompletar
	 */
	public void setBusIndicativoPorCompletar(String busIndicativoPorCompletar) {
		this.busIndicativoPorCompletar = busIndicativoPorCompletar;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHoraCreacion() {
		return horaCreacion;
	}
	
	/**
	 * 
	 * @param horaCreacion
	 */
	public void setHoraCreacion(String horaCreacion) {
		this.horaCreacion = horaCreacion;
	}


	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean esArticuloMultidosis(Connection con, int codigoArticulo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().esArticuloMultidosis(con,codigoArticulo);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBibliografia() {
		return bibliografia;
	}
	
	/**
	 * 
	 * @param bibliografia
	 */
	public void setBibliografia(String bibliografia) {
		this.bibliografia = bibliografia;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEfectoDes() {
		return efectoDes;
	}
	
	/**
	 * 
	 * @param efectoDes
	 */
	public void setEfectoDes(String efectoDes) {
		this.efectoDes = efectoDes;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEfectosSec() {
		return efectosSec;
	}
	
	/**
	 * 
	 * @param efectosSec
	 */
	public void setEfectosSec(String efectosSec) {
		this.efectosSec = efectosSec;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}
	
	/**
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTiempoEsp() {
		return tiempoEsp;
	}
	
	/**
	 * 
	 * @param tiempoEsp
	 */
	public void setTiempoEsp(String tiempoEsp) {
		this.tiempoEsp = tiempoEsp;
	}
	
	/**
	 * @return Returns the documentosAdjuntos.
	 */
	public DocumentosAdjuntos getDocumentosAdjuntos() {
		return documentosAdjuntos;
	}

	/**
	 * @param documentosAdjuntos The documentosAdjuntos to set.
	 */
	public void setDocumentosAdjuntos(DocumentosAdjuntos documentosAdjuntos) {
		this.documentosAdjuntos = documentosAdjuntos;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPrecioCompraMasAlta() {
		return precioCompraMasAlta;
	}
	
	/**
	 * 
	 * @param precioCompraMasAlta
	 */
	public void setPrecioCompraMasAlta(double precioCompraMasAlta) {
		this.precioCompraMasAlta = precioCompraMasAlta;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getBusPrecioCompraMasAlta() {
		return busPrecioCompraMasAlta;
	}
	
	/**
	 * 
	 * @param busPrecioCompraMasAlta
	 */
	public void setBusPrecioCompraMasAlta(String busPrecioCompraMasAlta) {
		this.busPrecioCompraMasAlta = busPrecioCompraMasAlta;
	}
	
	/**
	 * 
	 * @param con
	 * @param codModificacion
	 * @return
	 */
	public HashMap cargarGrupoEspecialesArticulo(Connection con, int codigoArticulo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().cargarGrupoEspecialesArticulo(con,codigoArticulo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codModificacion
	 * @return
	 */
	public String cargarDescripcionNivelAtencion(Connection con, int codigoArticulo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticuloDao().cargarDescripcionNivelAtencion(con,codigoArticulo);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getGrupoEspecialArticulo() {
		return grupoEspecialArticulo;
	}
	
	/**
	 * 
	 * @param grupoEspecialArticulo
	 */
	public void setGrupoEspecialArticulo(HashMap grupoEspecialArticulo) {
		this.grupoEspecialArticulo = grupoEspecialArticulo;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumGrupoEspecialArticulo() {
		return numGrupoEspecialArticulo;
	}
	
	/**
	 * 
	 * @param numGrupoEspecialArticulo
	 */
	public void setNumGrupoEspecialArticulo(int numGrupoEspecialArticulo) {
		this.numGrupoEspecialArticulo = numGrupoEspecialArticulo;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getGrupoEspecialArticulo(String key) {
		return grupoEspecialArticulo.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setGrupoEspecialArticulo(String key,Object obj) {
		this.grupoEspecialArticulo.put(key,obj);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescripcionAlterna() {
		return descripcionAlterna;
	}
	
	/**
	 * 
	 * @param descripcionAlterna
	 */
	public void setDescripcionAlterna(String descripcionAlterna) {
		this.descripcionAlterna = descripcionAlterna;
	}

	/**
	 * @return the cumMap
	 */
	public HashMap getCumMap() {
		return cumMap;
	}

	/**
	 * @param cumMap the cumMap to set
	 */
	public void setCumMap(HashMap cumMap) {
		this.cumMap = cumMap;
	}

	/**
	 * @return the esMedicamento
	 */
	public String getEsMedicamento() {
		return esMedicamento;
	}

	/**
	 * @param esMedicamento the esMedicamento to set
	 */
	public void setEsMedicamento(String esMedicamento) {
		this.esMedicamento = esMedicamento;
	}

	public String getAtencionOdon() {
		return atencionOdon;
	}

	public void setAtencionOdon(String atencionOdon) {
		this.atencionOdon = atencionOdon;
	}

	public void setNivelAtencion(
			long nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	public long getNivelAtencion() {
		return nivelAtencion;
	}


	
}
