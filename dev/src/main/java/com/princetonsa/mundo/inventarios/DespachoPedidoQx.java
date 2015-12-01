/*
 * Dic 05, 2007
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MaterialesQxDao;
import com.princetonsa.dao.inventarios.DespachoPedidoQxDao;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.MaterialesQx;

/**
 * Clase que implementa los metodos que
 * comunican el WorkFlow de la funcionalidad
 * con la fuente de datos
 * @author Sebastián Gómez R.
 *
 */
public class DespachoPedidoQx 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(DespachoPedidoQx.class);
	
	/**
	 * Interface para el llamado del DAO
	 */
	public static DespachoPedidoQxDao pedidoDao = null;
	
	/**
	 * Constructor
	 *
	 */
	public DespachoPedidoQx()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
     * Método que limpia este objeto
     */
    public void clean()
    {
    }

    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( pedidoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			pedidoDao= myFactory.getDespachoPedidoQxDao();
			if( pedidoDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Método para llamar el DAO de forma estática
	 * @return
	 */
	public static DespachoPedidoQxDao pedidoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDespachoPedidoQxDao();
	}
	
	/**
	 * Método que consulta las peticiones para realizar el despacho de pedidos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarPeticiones(Connection con,String codigoPaciente,String codigoCentroCosto,String codigoCentroCostoUsuario)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoCentroCostoUsuario",codigoCentroCostoUsuario);
		return pedidoDao().consultarPeticiones(con, campos);
	}
	
	/**
	 * Método que carga el detalle de los artículos a despachar de la petición
	 * @param con
	 * @param numeroPeticion
	 * @param usuario
	 * @param listadoPedidos
	 * @return
	 */
	public static ArrayList<HashMap> cargarDetalleArticulosPeticion(Connection con,int numeroPeticion,UsuarioBasico usuario,String listadoPedidos)
	{
		/**
		 * Indices articulos
		 * codigosArt_x, articulo_x, totalPedido_x, unidadMedidaArticulo_x
		 */
		
		//Se consultan los artículos de todos los pedidos de la peticion
		HashMap articulos = pedidoDao().consultaArticulosPedidosPeticion(con, numeroPeticion,usuario.getCodigoCentroCosto(),listadoPedidos);
		ArrayList<HashMap> resultado = new ArrayList<HashMap>();		
		
		int cantidadArticulos = Integer.parseInt(articulos.get("numRegistros").toString());
		
		if(cantidadArticulos>0){
			
			/**
			 * Indices pedidos
			 * codigoPedido_x, urgente_x, estado_x, usuarioSolicitante_x, fechaPedido_x, centroCostoSolicita_x, chequeado_x
			 * articulos_x : (codigoArticulo_y,cantidad_y)
			 */
			//Se consultan los pedidos de la petición con su propio detalle de artículos
			HashMap pedidos = new HashMap();
			
			pedidos = pedidoDao().consultaPedidosPeticion(con, numeroPeticion, usuario.getCodigoCentroCosto(),listadoPedidos);
						
			//Organizacion del listado de artículos (Se añade más información)
			for(int i=0;i<Integer.parseInt(articulos.get("numRegistros").toString());i++)
			{
				int sumaDespacho = 0;
				
				//1) Asignacion de la cantidad de cada articulo x pedido --------------------------------------------------------------------------
				for(int j=0;j<Integer.parseInt(pedidos.get("numRegistros").toString());j++)
				{
					HashMap detallePedido = (HashMap)pedidos.get("articulos_"+j);
					boolean tieneCantidad = false;
					
					
					for(int k=0;k<Integer.parseInt(detallePedido.get("numRegistros").toString());k++)
					{
						if(Integer.parseInt(articulos.get("codigosArt_"+i).toString())==Integer.parseInt(detallePedido.get("codigoArticulo_"+k).toString()))
						{
							tieneCantidad = true;
							articulos.put("cantidadPedido_"+i+"_"+j, detallePedido.get("cantidad_"+k));
							
							sumaDespacho += Integer.parseInt(detallePedido.get("cantidad_"+k).toString());
							
							//Solo aplica cuando se está consultan el resumem del despacho
							articulos.put("lote_"+i, detallePedido.get("lote_"+k));
							articulos.put("fechaVencimiento_"+i, detallePedido.get("fechaVencimiento_"+k));
						}
					}
					
					//Si no había cantidad de articulo para ese pedido se asigna 0
					if(!tieneCantidad)
						articulos.put("cantidadPedido_"+i+"_"+j,"0");
				}
				
				//2) Se verifica si artículo maneja lote y/o fecha Vencimiento---------------------------------------------------------------------------
				articulos.put("manejaLote_"+i,Articulo.articuloManejaLote(con, Integer.parseInt(articulos.get("codigosArt_"+i).toString()), usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				articulos.put("manejaFechaVencimiento_"+i,Articulo.articuloManejaFechaVencimiento(con, Integer.parseInt(articulos.get("codigosArt_"+i).toString()), usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				
				//3) SE añade la información de las existencias del artículo por almacen
				articulos.put("existenciasArticulo_"+i,UtilidadInventarios.getExistenciasXArticulo(Integer.parseInt(articulos.get("codigosArt_"+i).toString()), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt()));
				articulos.put("existenciasAlmacen_"+i, usuario.getCentroCosto()+" ("+usuario.getCentroAtencion()+")");
				
				//4) Se añade el codigo de la farmacia
				articulos.put("codigoAlmacen_"+i, usuario.getCodigoCentroCosto()+"");
				
				//5) A cantidad a despachar se la pasa el mismo total Pedido
				articulos.put("cantidadADespachar_"+i, articulos.get("totalPedido_"+i));
				
				//6) Se asigna la sumatoria de las cantidades despachadas del articulo x todos los pedidos (Aplica solo para el resumen)
				articulos.put("totalDespacho_"+i,sumaDespacho+"");
				
				//7) Se consulta la cantidad del consumo de cada articulo
				articulos.put("cantidadConsumo_"+i, MaterialesQx.consultarCantidadConsumoArticuloPeticion(con, numeroPeticion, Integer.parseInt(articulos.get("codigosArt_"+i).toString())));
			}

			// Mt 5595 Diferencia entre consumo y despacho por artículo
			String esOrden = articulos.get("sol_0")+"";
		
			//Orden de cirugia 
			if(!esOrden.isEmpty()){
						
			   for (int i = 0; i < Integer.parseInt(articulos.get("numRegistros")+ "");i++) {
				   // Se asigna la diferencia entre consumo total y despacho
			     	int consumo = Integer.parseInt(articulos.get("cantidadConsumo_"+ i)+ "");
				    int despacho = Integer.parseInt(articulos.get("cantidadADespachar_" + i) + "");

				    if (consumo < 0) {
					     consumo = 0;
				    }
			   articulos.put("despachoPendiente_" + i, consumo - despacho);
			}

			//Se recupera numero de solicitud y se obtiene el estado del consumo
			MaterialesQxDao materialesDao = null;
			if (materialesDao == null) {
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				materialesDao = myFactory.getMaterialesQxDao();
			}
			Integer numeroSolicitud = Integer.parseInt(articulos.get("sol_0").toString());
			// Se recupera valores del material
			HashMap result = materialesDao.cargarDatosGenerales(con,numeroSolicitud);
			String finalizado = "";
			// valor estado consumo
			finalizado = result.get("finalizado_0") + "";
			// Se agrega finalizado al mapa articulos
			articulos.put("consumoFinalizado_0", finalizado);
     		}
			// Se asigna el resultado
			resultado.add(articulos);
			resultado.add(pedidos);
		}		
		return resultado;
	}
	
}
