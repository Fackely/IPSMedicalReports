/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MaterialesQxDao;
import com.princetonsa.dao.SolicitudMedicamentosDao;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Materiales Quirúrgicos
 */
public class MaterialesQx 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(MaterialesQx.class);
	
	/**
	 * DAO para el manejo de MaterialesQxDao
	 */ 
	private MaterialesQxDao materialesDao=null;
	
	//**************ATRIBUTOS ****************************************
	/**
	 *Código de la orden de cirugia que identifica al Acto quirúrgico
	 */
	private int numeroSolicitud;
	
	/**
	 * fecha del registro materiales Qx. vinculado a un acto Qx.
	 */
	private String fecha;
	
	/**
	 * hora del registro materiales Qx. vinculado a un acto Qx.
	 */
	private String hora;
	
	/**
	 * Datos del centro de costo del registro materiales Qx.
	 * codigo - value
	 */
	private InfoDatos centroCosto;
	

	/**
	 * Datos de la farmacia del registro materiales Qx
	 * codigo - value
	 */
	private InfoDatos farmacia;
	
	/**
	 * Login del usuario que ingresa/modifica el registro de materiales Qx.
	 */
	private String usuario;
	
	/**
	 * Indica si el ingreso de conumo de materiales es por Acto ? S/N
	 */
	private boolean esActo;
	
	/**
	 * Objeto que almacena las cirugias del acto quirúrgico
	 */
	private HashMap cirugias;
	
	/**
	 * Objeto que almacena los artículos del ingreso de materiales Qx.
	 */
	private HashMap articulos;
	
	/**
	 * Variable que verifica si la orden tiene pedidos asociados
	 */
	private boolean tienePedido;
	
	/**
	 * Variable S/N para saber si el consumo se guarda pendiente o terminado
	 */
	private String terminado;
	
	/**
	 * Variable S/N para saber si el consumo se guarda finalizado o no
	 */
	private String finalizado;
	
	/**
	 * Tipo de transacción usada para el pedido de inventarios
	 */
	private String tipoTransaccionPedido;
	
	/**
	 * Código de la institucion
	 */
	private int codigoInstitucion;
	
	/**
	 * Variable que me permite saber si debo generar un pedido x consumo
	 */
	private boolean generarPedidoXConsumo;
	
	private SolicitudMedicamentosDao solicitudMedicamentosDao;
	
	private String entidadSubcontratada;
	//**************DATOS AUXILIARES ************************************
	/**
	 * Concatenación de órdenes que no tienen el pedido Qx de su petición despcahado
	 */
	private String ordenesSinPedidoDespachado;
	//**************CONSTRUCTORES E INICIALIZADORES********************
	/**
	 * Constructor
	 */
	public MaterialesQx() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.numeroSolicitud = -1;
		this.fecha = "";
		this.hora = "";
		this.centroCosto = new InfoDatos(0,"");
		this.farmacia = new InfoDatos(0,"");
		this.usuario = "";
		this.esActo = true;
		this.cirugias = new HashMap();
		this.articulos = new HashMap();
		this.ordenesSinPedidoDespachado = "";
		this.tienePedido = false;
		this.terminado = "";
		this.finalizado = "";
		this.tipoTransaccionPedido = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.generarPedidoXConsumo = false;
	}
	/**
	 * @return the generarPedidoXConsumo
	 */
	public boolean isGenerarPedidoXConsumo() {
		return generarPedidoXConsumo;
	}

	/**
	 * @param generarPedidoXConsumo the generarPedidoXConsumo to set
	 */
	public void setGenerarPedidoXConsumo(boolean generarPedidoXConsumo) {
		this.generarPedidoXConsumo = generarPedidoXConsumo;
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (materialesDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			materialesDao = myFactory.getMaterialesQxDao();
		}	
		
		if(solicitudMedicamentosDao == null)
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			solicitudMedicamentosDao = myFactory.getSolicitudMedicamentosDao();
		}
	}
	
	//*********************************************************************************
	//**************MÉTODOS OPCIÓN PEDIDO QX.******************************************
	//***********************************************************************************

	
	
	//********************************************************************************
	//**************MÉTODOS OPCIÓN INGRESO CONSUMO MATERIALES************************
	//******************************************************************************

	
	
	/**
	 * Método usado para consultar las ordenes de cirugia de una cuenta 
	 * que tengan estado de facturacion 'Pendiente' y estado de historia clínica 'Interpretada'
	 * @param cuenta
	 * @param cargarOrdenesCirugiaStr
	 * @return
	 */
	public HashMap cargarOrdenesCirugia(Connection con,int cuenta)
	{
		HashMap ordenes = materialesDao.cargarOrdenesCirugia(con,cuenta, "materiales");
		
		ordenes = this.prepararDatosMapa(ordenes);
		//se extraen las ordenes que tienen pedido sin despachar ************************
		
		int numRegistros = Integer.parseInt(ordenes.get("numRegistros") + "");
		
		int cont =0;
		this.ordenesSinPedidoDespachado = "";
		for(int i=0;i<numRegistros;i++)
		{
			//se verifica que si la orden tiene pedidos que no se encuentren despachados
			if(UtilidadTexto.getBoolean(ordenes.get("peticion_diferente_"+i)+""))
			{
				if(cont>0)
					this.ordenesSinPedidoDespachado += ",";
				this.ordenesSinPedidoDespachado += ordenes.get("orden_"+i) + "";
				cont ++;
			}
		}
		//********************************************************************************
			
		
		return ordenes;
	}
	
	/**
	 * Método usado para consultar las ordenes de cirugia de una cuenta 
	 * que tengan estado de facturacion 'Pendiente' y estado de historia clínica 'Interpretada'
	 * @param con
	 * @param cuenta
	 * @param funcionalidad "materiales" ó "liquidacion"
	 * @return
	 */
	public HashMap cargarOrdenesCirugia(Connection con,int cuenta,String funcionalidad)
	{
		HashMap ordenes = materialesDao.cargarOrdenesCirugia(con,cuenta, funcionalidad);
		
		ordenes = this.prepararDatosMapa(ordenes);
			
		return ordenes;
	}
	
	/**
	 * Método que realiza uan busqueda avanzada de ordenes de cirugia
	 * que ya estén cargadas
	 * @param con
	 * @param ordenInicial
	 * @param ordenFinal
	 * @param fechaOrdenInicial
	 * @param fechaOrdenFinal
	 * @param fechaCxInicial
	 * @param fechaCxFinal
	 * @param medico
	 * @return
	 */
	public HashMap cargarOrdenesCirugia(Connection con,
			String ordenInicial,String ordenFinal,String fechaOrdenInicial,
			String fechaOrdenFinal,String fechaCxInicial,String fechaCxFinal,int medico,int centroAtencion)
	{
		HashMap ordenes = materialesDao.cargarOrdenesCirugia(con,ordenInicial,ordenFinal,fechaOrdenInicial,fechaOrdenFinal,fechaCxInicial,fechaCxFinal,medico,centroAtencion);
		
		ordenes = this.prepararDatosMapa(ordenes);
		
		return ordenes;
	}
	
	/**
	 * Método implementado para cargar los artículos para Materiales Qx. buscando así:
	 * 1) Se buscan primero si existen articulos ya ingresados en Materiales Qx.
	 * 2) Si no hay, se buscan artículos en el pedido de la orden.
	 * 3) Si no hay, se buscan artículos en la hoja de gastos
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean cargarArticulosMaterialesQx(Connection con)
	{
		String codigosArticulosInsertados = "";
		this.articulos = materialesDao.cargarArticulosMaterialesQx(con,this.numeroSolicitud);
		
		if(this.articulos == null)
			return false;
		else
		{
			//calcular total de cantidades por artículo
			for(int i=0;i<Integer.parseInt(this.articulos.get("numRegistros")+"");i++)
			{	
				//se asigna el total de los consumos
				this.articulos.put("totalConsumos_"+i,Integer.parseInt(this.articulos.get("totalConsAnt_"+i)+"")+Integer.parseInt(this.articulos.get("total_"+i)+""));
				// se asigna la diferencia en pedido y consumo total
				this.articulos.put("difPedCons_"+i,Integer.parseInt(this.articulos.get("totalPedidos_"+i)+"")-Integer.parseInt(this.articulos.get("totalConsumos_"+i)+""));
				//Se asigna el total como historico
				this.articulos.put("totalHistorico_"+i,this.articulos.get("total_"+i));
				
				codigosArticulosInsertados += this.articulos.get("articulo_"+i).toString().split("-")[0]+ ",";
					
			}
			this.articulos.put("codigosArticulosInsertados",codigosArticulosInsertados);
			return true;
		}
	}
	
	/**
	 * Método usado para preparar los datos de cada registro
	 * de un mapa para que tengan la representación ideal en la vista
	 * @param mapa
	 * @return
	 */
	private HashMap prepararDatosMapa(HashMap mapa) 
	{
		int numRegistros=0;
		//se verifica estado del mapa
		if(mapa!=null)
		{
			numRegistros=Integer.parseInt(mapa.get("numRegistros")+"");
			for(int i=0;i<numRegistros;i++)
			{
				//preparación de la hora a 5 caracteres en el campo de fecha_orden
				String[] fechaOrden=(mapa.get("fecha_orden_"+i)+"").split(" ");
				mapa.put("fecha_orden_"+i,fechaOrden[0]+" "+UtilidadFecha.convertirHoraACincoCaracteres(fechaOrden[1]));
			}
		}
		
		return mapa;
	}

	/**
	 * Método usado para consultar los datos generales de un
	 * registro de materiales Qx.
	 * @param con
	 * @return
	 */
	public boolean cargarDatosGenerales(Connection con)
	{
		HashMap resultados = materialesDao.cargarDatosGenerales(con,this.numeroSolicitud);
		int numResultados= Integer.parseInt(resultados.get("numRegistros")+"");
		//se verifica si el registro existe
		if(numResultados>0)
		{
			this.fecha = resultados.get("fecha_0")+"";
			this.hora = UtilidadFecha.convertirHoraACincoCaracteres(resultados.get("hora_0")+"");
			this.setCentroCosto(Integer.parseInt(resultados.get("centro_costo_0")+""));
			this.setNombreCentroCosto(resultados.get("nombre_centro_costo_0")+"");
			this.setFarmacia(Integer.parseInt(resultados.get("farmacia_0")+""));
			this.setNombreFarmacia(resultados.get("nombre_farmacia_0")+"");
			this.usuario = resultados.get("usuario_0")+"";
			this.finalizado = resultados.get("finalizado_0")+"";
			this.esActo = UtilidadTexto.getBoolean(resultados.get("es_acto_0")+"");
			return true;
		}
		else
			return false;
	}
	
	
	/**
	 * Método para consultar las cirugías de un acto quirúgico que harán
	 * parte del registro de los materiales Qx.
	 * @param con
	 * @return
	 */
	public boolean cargarCirugiasPorActo(Connection con)
	{
		this.cirugias = materialesDao.cargarCirugiasPorActo(con,this.numeroSolicitud);
		
		
		if(this.cirugias==null)
			return false;
		else
			return true;
		
	}
	
	/**
	 * Método que inserta la información del encabezado del ingreso de consumo
	 * de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param estado transaccional
	 * @return
	 */
	public int insertarEncabezadoMaterialesQx(Connection con,String estado,UsuarioBasico usuario)
	{
		ArrayList<DtoEntidadSubcontratada> entidades =null;

		String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, this.getFarmacia());
		if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			logger.info("---farma-->"+this.getFarmacia()+"");
			logger.info("---isnt-->"+usuario.getCodigoInstitucionInt()+"");
			entidades = solicitudMedicamentosDao.obtenerEntidadesSubcontratadasCentroCosto(con, this.getFarmacia()+"", usuario.getCodigoInstitucionInt());
			logger.info("ejecutada");
			for(int i=0;i<entidades.size();i++)
				{
					entidadSubcontratada=entidades.get(i).getConsecutivo();
				}
		}
		
		return materialesDao.insertarEncabezadoMaterialesQx(
				con,
				this.numeroSolicitud,
				this.getCentroCosto(),
				this.usuario,
				this.fecha,
				this.hora,
				this.esActo,
				this.finalizado,
				estado,
				this.getFarmacia(),entidadSubcontratada);
	}
	
	
	
	
	/**
	 * Método que modifica la información del encabezado de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param estado
	 * @return
	 */
	public int modificarEncabezadoMaterialesQx(Connection con,String estado,UsuarioBasico usuario)
	{
		ArrayList<DtoEntidadSubcontratada> entidades =null;

		String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, this.getFarmacia());
		if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			String farmacia=this.getFarmacia()+"";
			int inst=usuario.getCodigoInstitucionInt();
			try
			{
				logger.info("--->"+farmacia);
				logger.info("--->"+inst);
				logger.info("--->"+usuario.getCodigoInstitucion());
				entidades = solicitudMedicamentosDao.obtenerEntidadesSubcontratadasCentroCosto(con, farmacia, inst);
				logger.info("ejecutado");
				for(int i=0;i<entidades.size();i++)
					{
						entidadSubcontratada=entidades.get(i).getConsecutivo();
					}
			}
			catch (Exception e) 
			{
				logger.info("ERROR EXTRAÑO",e);
			}
		}
		
		return materialesDao.modificarEncabezadoMaterialesQx(con,this.numeroSolicitud,this.getCentroCosto(),this.usuario,this.fecha,this.hora,this.esActo,this.finalizado,estado,this.getFarmacia(),entidadSubcontratada);
	}
	

	/**
	 * Método que carga los artículos por cirugia de un registro de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroCirugias
	 * @return
	 */
	public boolean cargarArticulosPorCirugia(Connection con)
	{
		String codigosArticulosInsertados = "";
		this.articulos = materialesDao.cargarArticulosPorCirugia(con,
				this.numeroSolicitud,
				Integer.parseInt(this.cirugias.get("numRegistros")+""));
		
		if(this.articulos==null)
			return false;
		else
		{
			
			//calcular total de cantidades por artículo
			for(int i=0;i<Integer.parseInt(this.articulos.get("numRegistros")+"");i++)
			{	
				//se asigna el total de los consumos
				this.articulos.put("totalConsumos_"+i,Integer.parseInt(this.articulos.get("totalConsAnt_"+i)+"")+Integer.parseInt(this.articulos.get("total_"+i)+""));
				// se asigna la diferencia en pedido y consumo total
				this.articulos.put("difPedCons_"+i,Integer.parseInt(this.articulos.get("totalPedidos_"+i)+"")-Integer.parseInt(this.articulos.get("totalConsumos_"+i)+""));
				
				codigosArticulosInsertados += this.articulos.get("articulo_"+i).toString().split("-")[0] + ",";
			}
			this.articulos.put("codigosArticulosInsertados",codigosArticulosInsertados);
			return true;
		}
	}
	
	
	/**
	 * Método que consulta la cantidad de despacho de cada uno de los artículos de la solicitud
	 * @param con
	 * @param mapaArticulos
	 * @param numArticulos
	 * @param porActo 
	 * @param existe 
	 * @return
	 */
	public HashMap consultarCantidadesArticulosDespacho(Connection con,HashMap mapaArticulos,int numArticulos, boolean porActo)
	{
		HashMap mapaDespacho = materialesDao.consultarCantidadesArticulosDespacho(con, numeroSolicitud+"");
		//int total = 0, totalConsumos = 0, difPedCons = 0;
		
		
		//Se actualiza la información de los articulos encontrados con los artículos existentes
		for(int i=0;i<numArticulos;i++)
		{
			//se toma el artículo
			String articulo = mapaArticulos.get("articulo_"+i).toString().split("-")[0];
			
			for(int j=0;j<Integer.parseInt(mapaDespacho.get("numRegistros").toString());j++)
			{
				if(articulo.equals(mapaDespacho.get("codigoArticulo_"+j).toString()))
				{
					
					/**
					//1) Recalcular el total consumo actual
					//Se toma el total que hay hasta ahora
					total = Integer.parseInt(mapaArticulos.get("total_"+i).toString());
					//Se le suma a la cantidad encontrada
					//solo aplica si es por acto
					if(porActo)
						total += Integer.parseInt(mapaDespacho.get("total_"+j).toString()); 
					mapaArticulos.put("total_"+i, total+"");
					
					//2) Recalculo del total consumos
					totalConsumos = Integer.parseInt(mapaArticulos.get("totalConsAnt_"+i).toString()) + total;
					mapaArticulos.put("totalConsumos_"+i, totalConsumos+"");
					
					
					//3) Recálculo de la diferencia de pedidos de consumo
					difPedCons = Integer.parseInt(mapaArticulos.get("totalPedidos_"+i).toString()) - totalConsumos;
					mapaArticulos.put("difPedCons_"+i, difPedCons+"");**/
					
					mapaDespacho.put("seleccionado_"+j, ConstantesBD.acronimoSi);
				}
			}
			
			
		}
		
		//********SE INGRESAN ARTICULOS DEL DESPACHO******************
		
		HashMap mapaTemporal = mapaArticulos;
		int numTemporal = numArticulos;
		String codigosArticulosInsertados = mapaArticulos.get("codigosArticulosInsertados").toString();
		
		logger.info("\n\n\n\n\n\n\n [Mapa Despachos en la Carga]");
		Utilidades.imprimirMapa(mapaDespacho);
		
		
		for(int i=0;i<Integer.parseInt(mapaDespacho.get("numRegistros").toString());i++)
		{
			//si el artículo del despacho no se encontraba entonces se agrega al final del listado
			if(!UtilidadTexto.getBoolean(mapaDespacho.get("seleccionado_"+i)+""))
			{
				mapaTemporal.put("codigoArticulo_"+numTemporal, mapaDespacho.get("codigoArticulo_"+i) );
				mapaTemporal.put("articulo_"+numTemporal,mapaDespacho.get("articulo_"+i));
				mapaTemporal.put("unidadMedida_"+numTemporal,mapaDespacho.get("unidadMedida_"+i));
				mapaTemporal.put("totalPedidos_"+numTemporal,mapaDespacho.get("totalPedidos_"+i));
				mapaTemporal.put("totalConsAnt_"+numTemporal,"0");
				//Solo se postula si es por acto
				if(porActo)
				{
					mapaTemporal.put("total_"+numTemporal,mapaDespacho.get("total_"+i));
					mapaTemporal.put("totalHistorico_"+numTemporal,mapaDespacho.get("total_"+i));
					mapaTemporal.put("totalConsumos_"+numTemporal,mapaDespacho.get("total_"+i));
				}
				else
				{
					mapaTemporal.put("total_"+numTemporal,"0");
					mapaTemporal.put("totalHistorico_"+numTemporal,"0");
					mapaTemporal.put("totalConsumos_"+numTemporal,"0");
				}
				mapaTemporal.put("difPedCons_"+numTemporal,"0");
				mapaTemporal.put("estaBd_"+numTemporal,ConstantesBD.acronimoNo);
				
				codigosArticulosInsertados += mapaTemporal.get("articulo_"+numTemporal).toString().split("-")[0] + ",";
				
				numTemporal++;
			}
		}
		
		mapaTemporal.put("codigosArticulosInsertados",codigosArticulosInsertados);
		mapaTemporal.put("numRegistros",numTemporal+"");
		mapaArticulos = mapaTemporal;
		
		//*************************************************************
		
		return mapaArticulos;
	}
	
	/**
	 * Método para consultar el pedido pendiente de una peticion
	 * @param codigoFarmacia 
	 */
	public HashMap consultarPedidoPendientePeticion(Connection con,String codigoPeticion, int codigoFarmacia)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPeticion", codigoPeticion);
		campos.put("codigoFarmacia", codigoFarmacia);
		return materialesDao.consultarPedidoPendientePeticion(con, campos);
	}
	
	
	/**
	 * Método que retorna de forma estática el dao de Materiales Qx
	 * @return
	 */
	public static MaterialesQxDao materialesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMaterialesQxDao();
	}
	
	/**
	 * Método que consulta los centros de costo de los pedidos existentes de la peticion
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public static HashMap consultarCentrosCostoPedidosPeticion(Connection con,int numeroPeticion)
	{
		return materialesDao().consultarCentrosCostoPedidosPeticion(con, numeroPeticion);
	}
	
	
	
	/**
	 * Método que consulta los artículos de pedidos anteriores de la peticion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap adicionarArticulosPedidosAnterioresPeticion(Connection con,int numeroPeticion,HashMap articulos)
	{
		//Se consultan los articulos de pedidos anteriores de la peticion
		HashMap campos = new HashMap();
		campos.put("codigoPeticion", numeroPeticion+"");
		HashMap mapaPedidosAnteriores = materialesDao.consultarArticulosPedidosAnterioresPeticion(con, campos);
		
		int numArticulos = Integer.parseInt(articulos.get("numRegistros").toString());
		int numArtPedidos = Integer.parseInt(mapaPedidosAnteriores.get("numRegistros").toString());
		boolean encontro = false;
		
		for(int i=0;i<numArticulos;i++)
		{
			encontro = false;
			for(int j=0;j<numArtPedidos;j++)
			{
				//Se verifica que si el articulo existe en el listado se agrega su cantidad
				if(Integer.parseInt(articulos.get("codigoArticulo_"+i).toString())==Integer.parseInt(mapaPedidosAnteriores.get("codigoArticulo_"+j).toString()))
				{
					articulos.put("totalPedidosAnt_"+i,mapaPedidosAnteriores.get("cantidadPedido_"+j).toString());
					mapaPedidosAnteriores.put("eliminado_"+j, ConstantesBD.acronimoSi);
					encontro = true;
				}
			}
			
			//Si no se encontraron pedidos anteriores de ese articulo se agrega 0
			if(!encontro)
				articulos.put("totalPedidosAnt_"+i,"0");
				
		}
		
		//Se agregan los articulos con pedidos anteriores que no estan en el listado actual de articulos
		for(int i=0;i<numArtPedidos;i++)
		{
			if(!UtilidadTexto.getBoolean(mapaPedidosAnteriores.get("eliminado_"+i)+""))
			{
				articulos.put("codigoArticulo_"+numArticulos, mapaPedidosAnteriores.get("codigoArticulo_"+i));
				articulos.put("articulo_"+numArticulos, mapaPedidosAnteriores.get("articulo_"+i));
				articulos.put("unidadMedidaArticulo_"+numArticulos, mapaPedidosAnteriores.get("unidadMedidaArticulo_"+i));
				articulos.put("totalPedidosAnt_"+numArticulos, mapaPedidosAnteriores.get("cantidadPedido_"+i));
				articulos.put("cantidad_"+numArticulos, "0");
				numArticulos++;
			}
		}
		
		articulos.put("numRegistros", numArticulos+"");
		return articulos;
	}
	
	/**
	 * Método que registra el consumo de los articulos
	 * @param con
	 * @return
	 */
	public ActionErrors guardarArticulosConsumo(Connection con,ActionErrors errores)
	{
		HashMap campos = new HashMap();
		campos.put("articulos", this.articulos);
		campos.put("cirugias", this.cirugias);
		campos.put("esActo", this.esActo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		campos.put("terminado", this.terminado);
		campos.put("finalizado", this.finalizado);
		campos.put("numeroSolicitud",this.numeroSolicitud+"");
		logger.info("GUARDAR CONSUMO: CODIGO FARMACIA=>"+this.farmacia.getCodigo()+", TIPO TRANSACCIÓN PEDIDO=> "+this.tipoTransaccionPedido);
		campos.put("codigoFarmacia",this.farmacia.getCodigo()+"");
		campos.put("tipoTransaccionPedido",this.tipoTransaccionPedido+"");
		campos.put("codigoInstitucion",this.codigoInstitucion+"");
		campos.put("generarPedidoXConsumo",this.generarPedidoXConsumo+"");
		campos.put("usuario",this.usuario);
		campos.put("errores",errores);		
		return materialesDao.guardarArticulosConsumo(con, campos);
	}
	
	
	/**
	 * Método que verifica si existe hoja de anestesia finalizada
	 * @param con
	 * @return
	 */
	public boolean existeHojaAnestesiaSinFinalizar(Connection con)
	{
		return materialesDao.existeHojaAnestesiaSinFinalizar(con, this.numeroSolicitud+"");
	}
	
	/**
	 * Método que realiza la reversión de la finalización del consumo
	 * @param con
	 * @param campos
	 * @return
	 */
	public int reversionFinalizacionConsumo(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("usuario",this.usuario);
		campos.put("numeroSolicitud",this.numeroSolicitud+"");
		return materialesDao.reversionFinalizacionConsumo(con, campos);
	}
	
	
	//*****************METODOS PARA EL REGISTRO AUTOMÁTICO DE CONSUMO MATERIALES******************
	/**
	 * Método que verifica si existe consumo de materiales finalizado
	 */
	public static boolean existeConsumoMateriales(Connection con,String numeroSolicitud,boolean finalizado)
	{
		return materialesDao().existeConsumoMateriales(con, numeroSolicitud, finalizado);
	}
	
	/**
	 * Método que inserta la información del encabezado del ingreso de consumo
	 * de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param estado transaccional
	 * @return
	 */
	public static int insertarEncabezadoMaterialesQxAutomatico(Connection con,String numeroSolicitud,UsuarioBasico usuario)
	{
		int resp = 1;
		
		//****************SE VERIFICA SI NO EXISTE PARA INSERTARLO*******************************************************
		if(!existeConsumoMateriales(con, numeroSolicitud, false))
		{
			//******Evaluacion del centro de costo del consumo************************************************
			HashMap centrosCosto = consultarCentrosCostoPedidosPeticion(con, Utilidades.getPeticionSolicitudCx(con, Integer.parseInt(numeroSolicitud)));
			int codigoCentroCosto = usuario.getCodigoCentroCosto();
			//Si ya habián pedidos se toma el centro de costo
			if(Utilidades.convertirAEntero(centrosCosto.get("centroCosto")+"", true)>0)
				codigoCentroCosto = Integer.parseInt(centrosCosto.get("centroCosto").toString());
			//*****************************************************************************************************
			
			UsuarioBasico usu=new UsuarioBasico();
			ArrayList<DtoEntidadSubcontratada> entidades =null;
			SolicitudMedicamentosDao solicitudMedicamentosDao = null ;

			String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, codigoCentroCosto);
			String entidadSub="";
			if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				 entidades = solicitudMedicamentosDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigoCentroCosto+"", usu.getCodigoInstitucionInt());
				 for(int i=0;i<entidades.size();i++)
					{
						 entidadSub=entidades.get(i).getConsecutivo();
					}
			}	
			
			resp = materialesDao().insertarEncabezadoMaterialesQx(
					con,
					Integer.parseInt(numeroSolicitud),
					codigoCentroCosto,
					usuario.getLoginUsuario(),
					UtilidadFecha.getFechaActual(con),
					UtilidadFecha.getHoraActual(con),
					UtilidadTexto.getBoolean(ValoresPorDefecto.getMaterialesPorActo(usuario.getCodigoInstitucionInt())),
					ConstantesBD.acronimoNo,
					ConstantesBD.continuarTransaccion,
					ConstantesBD.codigoNuncaValido,entidadSub);
		}
		return resp;
	}
	
	/**
	 * Método para insertar el consumo de un artículo de modo automático
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarConsumoArticuloAutomatico(Connection con,String numeroSolicitud,int codigoArticulo,int cantidad,String usuario)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("codigoArticulo", codigoArticulo);
		campos.put("cantidad", cantidad);
		campos.put("usuario", usuario);
		return materialesDao().insertarConsumoArticuloAutomatico(con, campos);
	}
	
	
	/**
	 * Método para consultar la cantidad del consumo del articulo de una peticion
	 * Si retorna -1 quiere decir que el artículo no tiene consumo
	 */
	public static int consultarCantidadConsumoArticuloPeticion(Connection con,int codigoPeticion,int codigoArticulo)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPeticion", codigoPeticion);
		campos.put("codigoArticulo", codigoArticulo);
		return materialesDao().consultarCantidadConsumoArticuloPeticion(con, campos);
	}
	
	
	/**
	 * Método para eliminar un registro del listado de articulos del consumo
	 * @param articulos
	 * @param numArticulos
	 * @param posicionArticulo
	 * @param esActo
	 * @param numPaquetes
	 * @param numCirugias
	 * @param cirugias
	 * @return
	 */
	public static HashMap eliminarRegistroListadoArticulosConsumo(HashMap articulos,int numArticulos,int posicionArticulo,boolean esActo,int numPaquetes,int numCirugias,HashMap cirugias)
	{
		for(int i=posicionArticulo;i<(numArticulos-1);i++)
		{
			articulos.put("articulo_"+i, articulos.get("articulo_"+(i+1)));
			articulos.put("unidadMedida_"+i, articulos.get("unidadMedida_"+(i+1)));
			articulos.put("total_"+i, articulos.get("total_"+(i+1)));
			articulos.put("totalConsAnt_"+i, articulos.get("totalConsAnt_"+(i+1)));
			articulos.put("totalConsumos_"+i, articulos.get("totalConsumos_"+(i+1)));
			articulos.put("difPedCons_"+i, articulos.get("difPedCons_"+(i+1)));
			articulos.put("totalPedidos_"+i, articulos.get("totalPedidos_"+(i+1)));
			articulos.put("estaBd_"+i, articulos.get("estaBd_"+(i+1)));
			articulos.put("codigoArticulo_"+i, articulos.get("codigoArticulo_"+(i+1)));
			articulos.put("tipoPosArticulo_"+i, articulos.get("tipoPosArticulo_"+(i+1)));
			articulos.put("yajustifico_"+i, articulos.get("yajustifico_"+(i+1)));
			articulos.put("sol_"+i, articulos.get("sol_"+(i+1)));
			if(esActo)
			{
				articulos.put("totalHistorico_"+i, articulos.get("totalHistorico_"+(i+1)));
				for(int j=0;j<numPaquetes;j++)
					articulos.put("consumoActual"+j+"_"+i, articulos.get("consumoActual"+j+"_"+(i+1)));
			}
			else
			{
				for(int j=0;j<numCirugias;j++)
					articulos.put("consumoActual"+cirugias.get("consecutivo_"+j)+"_"+i, articulos.get("consumoActual"+cirugias.get("consecutivo_"+j)+"_"+(i+1)));
			}
		}
		int numRegistros = numArticulos;
		numRegistros--;
		
		articulos.put("numRegistros", numRegistros+"");
		
		articulos.remove("articulo_"+numRegistros);
		articulos.remove("unidadMedida_"+numRegistros);
		articulos.remove("total_"+numRegistros);
		articulos.remove("totalConsAnt_"+numRegistros);
		articulos.remove("totalConsumos_"+numRegistros);
		articulos.remove("difPedCons_"+numRegistros);
		articulos.remove("totalPedidos_"+numRegistros);
		articulos.remove("estaBd_"+numRegistros);
		articulos.remove("codigoArticulo_"+numRegistros);
		articulos.remove("tipoPosArticulo_"+numRegistros);
		articulos.remove("yajustifico_"+numRegistros);
		articulos.remove("sol_"+numRegistros);
		if(esActo)
		{
			articulos.remove("totalHistorico_"+numRegistros);
			for(int j=0;j<numPaquetes;j++)
				articulos.remove("consumoActual"+j+"_"+numRegistros);
		}
		else
		{
			for(int j=0;j<numCirugias;j++)
				articulos.remove("consumoActual"+cirugias.get("consecutivo_"+j)+"_"+numRegistros);
		}
		
		return articulos;
	}
	
	/**
	 * Método para obtener las farmacias de los pedidos de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public String obtenerFarmaciasPedidosPeticion(Connection con,String codigoPeticion)
	{
		return materialesDao.obtenerFarmaciasPedidosPeticion(con, codigoPeticion);
	}
	//*************GETTERS & SETTERS**********************************************************+
	
	/**
	 * @return Returns the centroCosto.
	 */
	public int getCentroCosto() {
		return centroCosto.getCodigo();
	}
	/**
	 * @param centroCosto The centroCosto to set.
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto.setCodigo(centroCosto);
	}
	
	/**
	 * @return Returns el Nombre centroCosto.
	 */
	public String getNombreCentroCosto() {
		return centroCosto.getValue();
	}
	/**
	 * @param centroCosto The centroCosto to set.
	 */
	public void setNombreCentroCosto(String centroCosto) {
		this.centroCosto.setValue(centroCosto);
	}
	
	/**
	 * @return Returns the farmacia.
	 */
	public int getFarmacia() {
		return farmacia.getCodigo();
	}
	/**
	 * @param farmacia The farmacia to set.
	 */
	public void setFarmacia(int farmacia) {
		this.farmacia.setCodigo(farmacia);
	}
	
	/**
	 * @return Returns el Nombre farmacia.
	 */
	public String getNombreFarmacia() {
		return farmacia.getValue();
	}
	/**
	 * @param farmacia The farmacia to set.
	 */
	public void setNombreFarmacia(String farmacia) {
		this.farmacia.setValue(farmacia);
	}

	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the hora.
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @param hora The hora to set.
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Returns the cirugias.
	 */
	public HashMap getCirugias() {
		return cirugias;
	}
	/**
	 * @param cirugias The cirugias to set.
	 */
	public void setCirugias(HashMap cirugias) {
		this.cirugias = cirugias;
	}
	/**
	 * @return Returns the ordenesSinPedidoDespachado.
	 */
	public String getOrdenesSinPedidoDespachado() {
		return ordenesSinPedidoDespachado;
	}
	/**
	 * @param ordenesSinPedidoDespachado The ordenesSinPedidoDespachado to set.
	 */
	public void setOrdenesSinPedidoDespachado(String ordenesSinPedidoDespachado) {
		this.ordenesSinPedidoDespachado = ordenesSinPedidoDespachado;
	}
	/**
	 * @return Returns the articulos.
	 */
	public HashMap getArticulos() {
		return articulos;
	}
	/**
	 * @param articulos The articulos to set.
	 */
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	/**
	 * @return Returns the esActo.
	 */
	public boolean isEsActo() {
		return esActo;
	}
	/**
	 * @param esActo The esActo to set.
	 */
	public void setEsActo(boolean esActo) {
		this.esActo = esActo;
	}

	/**
	 * @return the tienePedido
	 */
	public boolean isTienePedido() {
		return tienePedido;
	}

	/**
	 * @param tienePedido the tienePedido to set
	 */
	public void setTienePedido(boolean tienePedido) {
		this.tienePedido = tienePedido;
	}

	/**
	 * @return the terminado
	 */
	public String getTerminado() {
		return terminado;
	}

	/**
	 * @param terminado the terminado to set
	 */
	public void setTerminado(String terminado) {
		this.terminado = terminado;
	}

	/**
	 * @return the finalizado
	 */
	public String getFinalizado() {
		return finalizado;
	}

	/**
	 * @param finalizado the finalizado to set
	 */
	public void setFinalizado(String finalizado) {
		this.finalizado = finalizado;
	}

	/**
	 * @return the tipoTransaccionPedido
	 */
	public String getTipoTransaccionPedido() {
		return tipoTransaccionPedido;
	}

	/**
	 * @param tipoTransaccionPedido the tipoTransaccionPedido to set
	 */
	public void setTipoTransaccionPedido(String tipoTransaccionPedido) {
		this.tipoTransaccionPedido = tipoTransaccionPedido;
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

	public String getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	public void setEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}
	
	

	
}
