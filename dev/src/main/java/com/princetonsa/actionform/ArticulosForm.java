/*
 * Created on Nov 28, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.ArticulosAction;
import com.princetonsa.mundo.inventarios.NaturalezaArticulos;
import com.princetonsa.mundo.solicitudes.DocumentosAdjuntos;
import com.servinte.axioma.orm.NivelAtencion;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Juan David Ramírez
 *
 * Princeton S.A.
 */
public class ArticulosForm extends ValidatorForm
{
	private Logger logger = Logger.getLogger(ArticulosForm.class);
	
	/**
	 * Guardar las modificaciones realizadas
	 */
	private HashMap modificaciones;
	/**
	 * Manejo del flujo
	 */
	private String estado;
	
	/**
	 * En esta cadena se almacenarán los valores que fueron ingresados por el usuario
	 * para postularlos en caso de error
	 */
	private String mantenerValores;
	
	/**
	 * Número total de articulos 
	 */
	private int numArticulos;

	/* El código general del servicio se maneja con:
	 * clase:		(4 Dígitos)
	 * grupo:		(4 Dígitos)
	 * subgrupo:	(4 Dígitos)
	 * codigo:		(4 Dígitos) Consecutivo asignado por la base de datos
	 * 
	 * En total es un código de 16 dígitos
	 */
	/**
	 * Clase del artículo.
	 */
	private String clase;
	/**
	 * Grupo del articulo
	 */
	private String grupo;
	/**
	 * Subgrupo del artículo
	 */
	private String subgrupo;
	/**
	 * Código del artículo
	 */
	private String codigo;
	
	/**
	 * Naturaleza del articulo
	 */
	private String naturaleza;
	
	/**
	 * Variables para ordenamiento
	 */
	private String campo="";
	private String ultimaPropiedad="";
	
	private int codModificacion;
	
	private boolean esEsteArticuloUsado;
	
	private String buscarEstado;
	
	/*
	 * El Código del ministerio de salud está compuesto por:
	 * Anatomofarmacológico:	(4 Dígitos)
	 * Principio Activo:		(4 Dígitos)
	 * Forma Farmacéutica		(2 Dígitos)
	 * Concentración:			(1 Dígito)
	 * 
	 * Sólo es requerido cuando la naturaleza es POS,
	 * de lo contrario son campos libres
	 */
	/**
	 * codigo minsalud del artículo.
	 */
	private String minsalud;

	/**
	 * codigo minsalud por componentes del artículo.
	 */
	private String m1;
	private String m2;
	private String m3;
	private String m4;

	/**
	 * Forma farmaceutica del artículo.
	 */
	private String formaFarmaceutica;
	/**
	 * Concentracion del artículo.
	 */
	private String concentracion;
	/**
	 * Unidad de medida del artículo.
	 */
	private String unidadMedida;
	/**
	 * Presentacion del Artículo.
	 */
	private String presentacion;
		
	/**
	 * Descripci�n del art�culo.
	 */
	private String descripcion;

	/**
	 * Estado del art�culo.
	 */
	private boolean estadoArticulo;

	/**
	 * Manejo de hashmap en la busqueda de articulos
	 */
	private HashMap check;
	
	/**
	 * Colleccion en la que quedaran almacenados los resultados de la b�squeda
	 */
	private Collection resultados;
	
	/**
	 * Contiene el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 */
	private String logInfoOriginalArticulo;
	
	
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
	
	/**
	 * 
	 */
	private HashMap infoMedicaMap;
	
	/**
	 * 
	 */
	private HashMap infoMedicaAdjMap;
	
	/**
	 * La variable mostrarInfoMedica representa un boolean que se inicializa en false y cuando la naturaleza sea un medicamento, pasa a ser true
	 */
	private boolean mostrarInfoMedica;
	
	///////////////////////////////////////////////////////////////
	
//	**********DOCUMENTOS ADJUNTOS******************************
	/**
	 * Colecci�n con los nombres generados de los archivos adjuntos 
	 */
	private final Map documentosAdjuntosGenerados = new HashMap(); 
	
	/**
	 * N�mero de documentos adjuntos
	 */
	private int numDocumentosAdjuntos = 1;
	
	/**
	 * codigo de resopuesta
	 */
	private String codigoRespuesta;
	
	//***********************************************************		
	
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
	 * 
	 */
	private String costoPromedioViejo;
	
	/**
	 * Costo donaci�n del articulo.
	 */
	private double costoDonacion;
	
	/**
	 * C�digo Interfaz
	 */
	private String codigoInterfaz;
	
	/**
	 * Indicativo Autom�tico
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
	private String busCodigoInterfaz;
	
	/**
	 * 
	 */
	private String busIndicativoAutomatico;
	
	/**
	 * 
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
	 * Variable para manejar el precio ultima compra viejo
	 */
	private String precioUltimaCompraViejo;
	
	/**
	 *Variable para manejar el precio base venta 
	 */
	private double precioBaseVenta;
	
	/**
	 * Variable para almacenar el precio base venta viejo
	 */
	private String precioBaseVentaViejo;
	
	/**
	 *Variable para manejar el fecha precio base venta 
	 */
	private String fechaPrecioBaseVenta;
	
	/**
	 * 
	 */
	private double precioCompraMasAlta;
	
	private String precioCompraMasAltaViejo;
	
	//Busquedas
	private String busMaximaCantidadMes;

	private String busPorcentajeIva;

	private String busPrecioUltimaCompra;

	private String busPrecioBaseVenta;
	
	private String busPrecioCompraMasAlta;
	
	
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
	
	/**
	 * Variable que maneja el indice de la vias de administracion a eliminar.
	 */
	private int indexViaAdminEliminar;
	
	/////// Inicio Grupos Especiales
	
	/**
	 * 
	 */
	private HashMap grupoEspecialArticulo = new HashMap();
	
	/**
	 * 
	 */
	private int numGrupoEspecialArticulo;
	
	/**
	 * 
	 */
	private int indexGrupoEspecialEliminar;
	
	/////// Fin Grupos Especiales
	
	/**
	 * 
	 */
	private HashMap ubicacionMap;
	
		

	/**
	 * Variable que maneja el indice de la vias de administracion a eliminar.
	 */
	
	//////////////////////////////////////////////////////////////////////////////
	
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
	private int indexUnidosisEliminar;
	
	/**
	 * Seccion Info Medica.
	 */
	private boolean infoMedica;
	
	
	////////Nuevo Sin doc.
	
	private String descripcionAlterna;
	
	
	
	//************ INICIO - Atributos De la secci�n (Codigo �nico de Medicamentos CUM)
	
	/**
	 * Variable para manejar la visibilidad de la secci�n
	 */
	private boolean seccionCum;
	
	/**
	 * Mapa con la informaci�n del Codigo �nico de Medicamento CUM
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
	
	//************ FIN - Atributos De la secci�n (Codigo �nico de Medicamentos CUM)
	
	private String tipoInventario;
	
	
	//--------------------------------------------------------------------
    private ArrayList<String> mensajes = new ArrayList<String>();
    //---------------------------------------------------------------------
	
	/**
	 * Agregado por Anexo 951
	 */
    private String atencionOdon;
    
    
    /**
	 * Atributo donde se almacena el consecutivo del nivel de atenci�n del articulo
	 * agregado en el anexo 1022 v1.51
	 */
    private long nivelAtencion;
    
    /**
	 * Atributo donde se almacena la descripci�n del nivel de atenci�n del articulo
	 */
    private String descripcionNivelAtencion;
    
    
    /**
	 * Atributo donde se almacenan los niveles de atenci�n activos en el sistema
	 */
    private ArrayList<NivelAtencion> nivelesAtencion;
    
	
	/**
	 * @return Retorna clase.
	 */
	public String getClase()
	{
		return clase;
	}
	/**
	 * @return the mensajes
	 */
	public ArrayList<String> getMensajes() {
		return mensajes;
	}
	/**
	 * @param mensajes the mensajes to set
	 */
	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}
	
	public int getSizeMensajes() {
		return mensajes.size();
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
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
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
	 * @return Retorna m1.
	 */
	public String getM1()
	{
		return m1;
	}
	/**
	 * @param m1 Asigna m1.
	 */
	public void setM1(String m1)
	{
		this.m1 = m1;
	}
	/**
	 * @return Retorna m2.
	 */
	public String getM2()
	{
		return m2;
	}
	/**
	 * @param m2 Asigna m2.
	 */
	public void setM2(String m2)
	{
		this.m2 = m2;
	}
	/**
	 * @return Retorna m3.
	 */
	public String getM3()
	{
		return m3;
	}
	/**
	 * @param m3 Asigna m3.
	 */
	public void setM3(String m3)
	{
		this.m3 = m3;
	}
	/**
	 * @return Retorna m4.
	 */
	public String getM4()
	{
		return m4;
	}
	/**
	 * @param m4 Asigna m4.
	 */
	public void setM4(String m4)
	{
		this.m4 = m4;
	}
	/**
	 * @return Retorna mantenerValores.
	 */
	public String getMantenerValores()
	{
		return mantenerValores;
	}
	/**
	 * @param mantenerValores Asigna mantenerValores.
	 */
	public void setMantenerValores(String mantenerValores)
	{
		this.mantenerValores = mantenerValores;
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
	 * @return Retorna numArticulos.
	 */
	public int getNumArticulos()
	{
		return numArticulos;
	}
	/**
	 * @param numArticulos Asigna numArticulos.
	 */
	public void setNumArticulos(int numArticulos)
	{
		this.numArticulos = numArticulos;
	}
	/**
	 * @return Retorna presentacion.
	 */
	public String getPresentacion()
	{
		return presentacion;
	}
	/**
	 * @param presentacion Asigna presentacion.
	 */
	public void setPresentacion(String presentacion)
	{
		this.presentacion = presentacion;
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
	 * M�todo para limpiar los valores de la forma
	 *
	 */
	public void reset()
	{
		logger.info("ESTOY RESETEANDO LA FORMA!!!!!!!!!!!");
		mantenerValores="";
		numArticulos=0;
		clase="";
		grupo="";
		subgrupo="";
		codigo="";
		this.naturaleza="";
		minsalud="";
		m1="";
		m2="";
		m3="";
		m4="";
		formaFarmaceutica="";
		concentracion="";
		unidadMedida="";
		presentacion="";
		descripcion="";
		estadoArticulo=true;
		infoMedica=false;
		check=new HashMap();
		modificaciones=new HashMap();
		this.fechaCreacion=UtilidadFecha.getFechaActual();
		this.stockMinimo=0;
		this.stockMaximo=0;
		this.puntoPedido=0;
		this.cantidadCompra=0;
		this.costoPromedio="0.0";
		this.costoPromedioViejo="";
		this.costoDonacion=0.0;
		this.codigoInterfaz="";
		this.indicativoAutomatico=ConstantesBD.acronimoNo;
		this.indicativoPorCompletar=ConstantesBD.acronimoNo;
		this.categoria=ConstantesBD.codigoNuncaValido;
		this.codModificacion=ConstantesBD.codigoNuncaValido;
		
		this.setEsEsteArticuloUsado(false);
		
		this.buscarEstado="";
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
		this.precioUltimaCompraViejo="";
		this.precioBaseVenta=0;
		this.precioBaseVentaViejo="";
		this.fechaPrecioBaseVenta="";	
		
		this.busMaximaCantidadMes="";
		this.busPorcentajeIva="";
		this.busPrecioUltimaCompra="";
		this.busPrecioBaseVenta="";
		
		this.manejaLotesValorAnt="";
		this.existe="";
		
		this.viasAdminXFormaFarma = new HashMap();
		this.numViasAdmiXFormaFarma=0;
		
		this.grupoEspecialArticulo = new HashMap();
		this.numGrupoEspecialArticulo=0;
		
		this.unidosisXArticulo= new HashMap();
		this.numUnidosisXArticulo=0;
		
		this.indexViaAdminEliminar=0;
		this.indexUnidosisEliminar=0;
		
		
		this.ubicacionMap=new HashMap();
		
		this.tiempoEsp="";
		this.efectoDes="";
		this.efectosSec="";
		this.observaciones="";
		this.bibliografia="";
		
		this.documentosAdjuntosGenerados.clear();
		this.numDocumentosAdjuntos = 0;
		this.codigoRespuesta="";
		
		this.precioCompraMasAlta=0;
		this.precioCompraMasAltaViejo="";
		
		this.descripcionAlterna="";
		this.mostrarInfoMedica = false;
		
		// Inicializaci�n de los atributos para el manejo de la secci�n Codigo �nico de Medicamentos CUM
		this.seccionCum = false;
		this.cumMap = new HashMap();
		
		this.tipoInventario = "";
		this.atencionOdon=ConstantesBD.acronimoNo;
		
		this.nivelAtencion=ConstantesBD.codigoNuncaValido;
		this.nivelesAtencion=new ArrayList<NivelAtencion>();
		this.descripcionNivelAtencion="";
		
	}
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		errores.add(super.validate(mapping, request));
		if(estado.equals("guardar")||estado.equals("guardarModificacion"))
		{
			boolean numero=true;
			// aqui se valida si el valor del factor es un numero
			try 
			{
				Double.parseDouble(this.getCostoPromedio()+"");
			} 
			catch (Exception e)
			{
				numero=false;
				errores.add("descripcion",new ActionMessage("errors.float","El Costo promedio "+this.getCostoPromedio()));
			}
			
			//aqui se valida si cumple con la cantidad de digitos enteros y decimales.			
			if (numero)
			{
				String [] tmp =UtilidadTexto.separarParteEnteraYDecimal(this.getCostoPromedio()+"");
				if (tmp[0].length()>12)
					errores.add("descripcion",new ActionMessage("error.cantidadParteEntera",this.getCostoPromedio(),"12"));
				if (tmp[1].length()>7)
					errores.add("descripcion",new ActionMessage("error.cantidadParteDecimal",this.getCostoPromedio(),"7"));
					
			}
			
			if(clase.equals("0"))
			{
				errores.add("clase", new ActionMessage("errors.required","El campo Clase"));
			}
			if(grupo.equals("0"))
			{
				errores.add("grupo", new ActionMessage("errors.required","El campo Grupo"));
			}
			if(subgrupo.equals("0"))
			{
				errores.add("subgrupo", new ActionMessage("errors.required","El campo Subgrupo"));
			}
			if(descripcion.equals(""))
			{
				errores.add("descripcion", new ActionMessage("errors.required","El campo Descripci�n"));
			}
			if(naturaleza.equals(ConstantesBD.codigoNuncaValido + ""))
			{
				logger.info("===> La Naturaleza Viene en 0, se va a poner mostrarInfoMedica = false");
				errores.add("naturaleza", new ActionMessage("errors.required","El campo Naturaleza"));
				this.setMostrarInfoMedica(false);
			}
			else
			{
				boolean esPos, esMedicamento;
				String[]natTempo=naturaleza.split("-");
				logger.info("===> natTempo [0] = "+natTempo[0].trim());
				if(!UtilidadTexto.isEmpty(natTempo[0]) && 
						natTempo[0].trim().equals(ConstantesBD.acronimoNaturalezaArticuloMedicamentoNoPos.trim())
						)
				{
					logger.info("Entr� a Es No Pos !!!");
					if(this.registroINVIMA.trim().equals(""))
					{
						errores.add("Registro INVIMA", new ActionMessage("errors.required","El campo Registro INVIMA"));
					}
				}
				
				logger.info("natTempo[1] "+natTempo[1]);
				if(UtilidadTexto.getBoolean(natTempo[1]))
				{
					logger.info("===> esPos = True");
					esPos=true;
				}
				else
				{
					logger.info("===> esPos = False");
					esPos=false;
				}
				int posicionesNatTempo = natTempo.length;
				logger.info("===> Voy a mirar las posiciones: "+ posicionesNatTempo);
				
				/*
				 * Validamos las posiciones por que en la modificaci�n solo vienen 2 posiciones
				 */
				if(posicionesNatTempo<3)
				{
					logger.info("===> natTempo[2] es NULL");
				}
				else
				{
					logger.info("�es Medicamento? "+natTempo[2]);
					logger.info("===> Estado = "+this.getEstado());
					if(UtilidadTexto.getBoolean(natTempo[2]))
					{
						esMedicamento = true;
					}
					else
					{
						esMedicamento = false;
					}
					
					logger.info("esPos: "+esPos);
					/* ***************************
					 * INICIO Soluci�n Tarea 62930
					 * ***************************/
					
					  /*
				  	   * Soluci�n de la Tarea 60580
					   * El c�digo min salud para medicamentos NO POS NO es requerido
					   * El c�digo min salud para medicamentos POS SI es requerido
					   * hay que preguntar por el estado para saber si es una modificaci�n o una insercion
					   */
						
						if(this.getEstado().equals("guardarModificacion"))
						{
							logger.info("===> Estamos en una modificaci�n, vamos a validar el min salud entero");
							logger.info("===> El codigo Min salud Es: "+minsalud);
							if(UtilidadTexto.isEmpty(minsalud) && !UtilidadTexto.isEmpty(natTempo[0]) && 
								natTempo[0].trim().equals(ConstantesBD.codigoNaturalezaArticuloMedicamentoPos.trim())
								&&esPos)
							{
								logger.info("===> Entr� al Condicional, Estoy dentro de Medicamentos, se deben de validar los codigos min salud");
								errores.add("Codigo minSalud", new ActionMessage("errors.required","El C�d MINSALUD "));
							}
						}
						
					if(esMedicamento){
						if(this.numUnidosisXArticulo==0)
						{
							errores.add("Unidosis", new ActionMessage("errors.required","El campo Unidosis "));
						}
					}
					//if(esPos)
					if(esPos && esMedicamento)
					{
						
						if(this.getEstado().equals("guardar"))
						{
							logger.info("===> Estamos en una inserci�n, vamos a validar los 4 min salud");
							logger.info("===> Entr� al Condicional, Estoy dentro de Medicamentos, se deben de validar los codigos min salud");
							if(m1.equals(""))
							{
								errores.add("m1", new ActionMessage("errors.required","El campo Anatomofarmacol�gico (C�digo Minsalud)"));
							}
							if(m2.equals(""))
							{
								errores.add("m2", new ActionMessage("errors.required","El campo Principio Activo (C�digo Minsalud)"));
							}
							if(m3.equals(""))
							{
								errores.add("m3", new ActionMessage("errors.required","El campo Forma Farmac�utica (C�digo Minsalud)"));
							}
							if(m4.equals(""))
							{
								errores.add("m4", new ActionMessage("errors.required","El campo Concentraci�n (C�digo Minsalud)"));
							}
							logger.info("===> m1 = "+m1);
							logger.info("===> m2 = "+m2);
							logger.info("===> m3 = "+m3);
							logger.info("===> m4 = "+m4);
						}
						
						
					}
					/* ************************
					 * FIN Soluci�n Tarea 62930
					 * ************************/
					if(esMedicamento)
					{
						logger.info("===> Aqu� hay que validar que NO sea una naturaleza por Art�culos");
						if((!UtilidadTexto.isEmpty(natTempo[0]) 
								&& !(natTempo[0].trim().equals(ConstantesBD.acronimoNaturalezaArticuloMedicamentoNoPos.trim())))
							|| (!UtilidadTexto.isEmpty(natTempo[0]) 
									&& !(natTempo[0].trim().equals(ConstantesBD.acronimoNaturalezaArticuloMedicamentoPos.trim()))))
						{
							/*
							logger.info("===> El codigo Min salud Es: "+minsalud);
							if(minsalud.equals(""))
							{
								logger.info("===> Entr� al Condicional, Estoy dentro de Medicamentos, se deben de validar los codigos min salud");
								errores.add("Codigo minSalud", new ActionMessage("errors.required","El C�d MINSALUD )"));
							}
							*/
							/*
							logger.info("===> Entr� al Condicional, Estoy dentro de Medicamentos, se deben de validar los codigos min salud");
							if(m1.equals(""))
							{
								errores.add("m1", new ActionMessage("errors.required","El campo Anatomofarmacol�gico (C�digo Minsalud)"));
							}
							if(m2.equals(""))
							{
								errores.add("m2", new ActionMessage("errors.required","El campo Principio Activo (C�digo Minsalud)"));
							}
							if(m3.equals(""))
							{
								errores.add("m3", new ActionMessage("errors.required","El campo Forma Farmac�utica (C�digo Minsalud)"));
							}
							if(m4.equals(""))
							{
								errores.add("m4", new ActionMessage("errors.required","El campo Concentraci�n (C�digo Minsalud)"));
							}
							logger.info("===> m1 = "+m1);
							logger.info("===> m2 = "+m2);
							logger.info("===> m3 = "+m3);
							logger.info("===> m4 = "+m4);*/
							logger.info("******************************************");
							logger.info("===> Voy a poner mostrarInfoMedica en true");
							logger.info("******************************************");
							this.setMostrarInfoMedica(true);
						}
						else
						{
							logger.info("===> El Usuario a Seleccionado una Naturaleza diferente a Medicamentos");
						}
					}
				}
				
				
					
					
				/*}
				else
				{*/
					//SE elimina validaci�n por oid=[10567]
					/*if(concentracion.equals(""))
					{
						errores.add("concentracion", new ActionMessage("errors.required","El campo Concentraci�n"));
					}*/
					
				//}
			}
			
			logger.info("NATURALEZA - "+naturaleza);
			
			if(!naturaleza.equals(""))
			{
				String[]natTempo2=naturaleza.split("-");
				
				if(natTempo2.length>2)
				{
					logger.info("natTempo2.length->"+natTempo2.length+"-->("+natTempo2[2]+")");
					
					/*
					 * natTempo2 [2] Me indica si la naturaleza es medicamentos
					 */
					if(natTempo2[2].equals(ConstantesBD.acronimoSi))
					{
						if(formaFarmaceutica.equals("0"))
						{
							logger.info("===> Vamos a agregar true a mostrarInfoMedica");
							this.setMostrarInfoMedica(true);
							logger.info("===> Info M�dica = "+this.isMostrarInfoMedica());
							errores.add("forma", new ActionMessage("errors.required","El campo Forma Farmac�utica"));
						}
					}
					else
					{
						logger.info("===> Como natTempo2 [2] no es igual al acr�nimo SI, Vamos a agregar false a mostrarInfoMedica");
						this.setMostrarInfoMedica(false);
					}
				}
				else if (natTempo2.length==3)
				{
					logger.info("**************************************");
					logger.info("===> La longitud es 3, Vamos a Validar");
					logger.info("**************************************");
					natTempo2=naturaleza.split("-");
					
					if(natTempo2.length>2)
					{
						logger.info("===> natTempo2.length->"+natTempo2.length+"-->("+natTempo2[2]+")");
						
						/*
						 * natTempo2 [2] Me indica si la naturaleza es medicamentos
						 */
						if(natTempo2[2].equals(ConstantesBD.acronimoSi))
						{
							if(formaFarmaceutica.equals("0"))
							{
								logger.info("===> Vamos a agregar true a mostrarInfoMedica");
								this.setMostrarInfoMedica(true);
								logger.info("===> Info M�dica = "+this.isMostrarInfoMedica());
								errores.add("forma", new ActionMessage("errors.required","El campo Forma Farmac�utica"));
							}
						}
						else
						{
							logger.info("===> Como natTempo2 [2] no es igual al acr�nimo SI, Vamos a agregar false a mostrarInfoMedica");
							this.setMostrarInfoMedica(false);
						}
					}
					logger.info("*************************");
					logger.info("===> Fin de la validaci�n");
					logger.info("*************************");
				}
			}
			
			if(categoria<0)
			{
				errores.add("categoria", new ActionMessage("errors.required","El campo Categoria"));
			}
			if(unidadMedida.equals("0"))
			{
				errores.add("unidadMedida", new ActionMessage("errors.required","El campo Unidad de Medida"));
			}
			if(estado.equals("guardar"))//solo si se va a guardar por primera vez aplica la validacion de la fecha
			{
				if(!UtilidadFecha.validarFecha(this.getFechaCreacion()))
				{
					errores.add("fecha Creacion", new ActionMessage("errors.formatoFechaInvalido",this.getFechaCreacion()));
				}
				else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaCreacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
				{
					errores.add("fecha de radicaci�n", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Creacion", "actual"));
				}
			}
			if(stockMaximo<stockMinimo)
			{
			    errores.add("Stock Maximo Mayor Minomo", new ActionMessage("errors.MayorIgualQue", "Stock Maximo", "Stock Minimo"));
			}
			if(puntoPedido>stockMaximo)
			{
			    errores.add("P>SMA", new ActionMessage("errors.MenorIgualQue", "El Punto Pedido","Stock Maximo"));
			}
			else if(puntoPedido<stockMinimo)
			{
			    errores.add("P<SMI", new ActionMessage("errors.MayorIgualQue", "El Punto Pedido","Stock Minimo"));
			}
			
			//
			if (porcentajeIva>100)
			{
				errores.add("Porcentaje Iva mayor a 100", new ActionMessage("errors.MenorIgualQue", "Porcentaje Iva","100"));
			}
			
			if(!fechaPrecioBaseVenta.equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaPrecioBaseVenta()))
				{
					errores.add("fecha Precio Base Venta", new ActionMessage("errors.formatoFechaInvalido","Precio Base Venta", this.getFechaPrecioBaseVenta()));
				}

			}
			if(this.numUnidosisXArticulo>1)
			{
				for(int i=0;i<numUnidosisXArticulo;i++)
				{
					String uTemp=unidosisXArticulo.get("unidad_medida_"+i)+"";
					double cTemp=(unidosisXArticulo.get("cantidad_"+i)+"").trim().equals("")?0:Double.parseDouble(unidosisXArticulo.get("cantidad_"+i)+"");
					
					for(int j=0;j<i;j++)
					{
						double cTemp1=(unidosisXArticulo.get("cantidad_"+j)+"").trim().equals("")?0:Double.parseDouble(unidosisXArticulo.get("cantidad_"+j)+"");
						if(uTemp.trim().equals((unidosisXArticulo.get("unidad_medida_"+j)+"").trim())&&cTemp==cTemp1)
						{
							
							errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","Unidosis "+unidosisXArticulo.get("nombre_"+j)+" Cantidad "+unidosisXArticulo.get("cantidad_"+j)));     
						}
					}
				}
				
			}
			/*
			* Tipo Modificacion: Segun incidencia 6602
			* Autor: Jes�s Dar�o R�os
			* usuario: jesrioro
			* Fecha: 06/03/2013
			* Descripcion: 	para crear y modificar medicamentos
			* 				validar  que  no se  guarde  si  
			* 				no hay al menos  una via de administracion seleccionada
			 */
			if (!naturaleza.equals("-1")) {
				String[] natTempo2 = naturaleza.split("-");
				if (natTempo2[2].equals(ConstantesBD.acronimoSi)) {
					//validar  el estado guardarModificacion
					int num = this.numViasAdmiXFormaFarma;
					String evaluar="N";
					if(num>0){
						for (int i = 0; i < num; i++) {
							String activo = (String) this.viasAdminXFormaFarma.get("activo_" + i);
							if(activo.equals("S")){
								evaluar="S";
							}
						}
							if(!evaluar.equals("S")){
								errores.add("", new ActionMessage("errores.articulos.viaAdministracionActiva",""));
							}
					}else{
						errores.add("", new ActionMessage("errors.required","El campo V�as de Administraci�n "));
					}
	
				}

			}
				

			if(errores.isEmpty())
			{
				logger.info("77777777777777777777777777777777777777777");

				if(naturaleza.split("-")[2].equals(ConstantesBD.acronimoSi)) //81450
					tipoInventario = ConstantesBD.acronimoTipoInventarioMedicamento;
				
				else
					tipoInventario = ConstantesBD.acronimoTipoInventarioElemento;
				
				naturaleza=naturaleza.split("-")[0];
				logger.info("===> No Hay Errores. Naturaleza: " + naturaleza + " Tip Inventario: " + tipoInventario);
			}
			else
			{
				logger.info("===> Si Hay Errores");
				setInfoMedica(false);
			}
		}
		return errores;
	}
	/**
	 * @return Retorna check.
	 */
	public HashMap getChecks()
	{
		return check;
	}
	/**
	 * @param check Asigna check.
	 */
	public void setChecks(HashMap check)
	{
		this.check = check;
	}
	
	/**
	 * Obteber estado del check
	 * @param key
	 * @return
	 */
	public Object getCheck(String key)
	{
		return check.get(key);
	}
	
	/**
	 * Asignar valor del chack
	 * @param key
	 * @param value
	 */
	public void setCheck(String key, Object value)
	{
		check.put(key, value);
	}
	/**
	 * @return Retorna resultados.
	 */
	public Collection getResultados()
	{
		return resultados;
	}
	/**
	 * @param resultados Asigna resultados.
	 */
	public void setResultados(Collection resultados)
	{
		this.resultados = resultados;
	}
	/**
	 * @return Retorna modificaciones.
	 */
	public HashMap getModificaciones()
	{
		return modificaciones;
	}
	/**
	 * @param modificaciones Asigna modificaciones.
	 */
	public void setModificaciones(HashMap modificaciones)
	{
		this.modificaciones = modificaciones;
	}
	/**
	 * @param key llave para retornar el valor que contiene
	 * @return Retorna valor de la modificaci�n.
	 */
	public Object getModificacion(String key)
	{
		return modificaciones.get(key); 
	}
	/**
	 * Asignar un valor a la llave(key) dada
	 * @param key.
	 * @param value.
	 */
	public void setModificacion(String key, Object value)
	{
		this.modificaciones.put(key, value);
	}
    /**
     * @return Returns the logInfoOriginalArticulo.
     */
    public String getLogInfoOriginalArticulo() {
        return logInfoOriginalArticulo;
    }
    /**
     * @param logInfoOriginalArticulo The logInfoOriginalArticulo to set.
     */
    public void setLogInfoOriginalArticulo(String logInfoOriginalArticulo) {
        this.logInfoOriginalArticulo = logInfoOriginalArticulo;
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
     * @return Returns the check.
     */
    public HashMap getCheck()
    {
        return check;
    }
    /**
     * @param check The check to set.
     */
    public void setCheck(HashMap check)
    {
        this.check = check;
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
     * @return Returns the campo.
     */
    public String getCampo()
    {
        return campo;
    }
    /**
     * @param campo The campo to set.
     */
    public void setCampo(String campo)
    {
        this.campo = campo;
    }
    /**
     * @return Returns the ultimaPropiedad.
     */
    public String getUltimaPropiedad()
    {
        return ultimaPropiedad;
    }
    /**
     * @param ultimaPropiedad The ultimaPropiedad to set.
     */
    public void setUltimaPropiedad(String ultimaPropiedad)
    {
        this.ultimaPropiedad = ultimaPropiedad;
    }
    /**
     * @return Returns the codModificacion.
     */
    public int getCodModificacion()
    {
        return codModificacion;
    }
    /**
     * @param codModificacion The codModificacion to set.
     */
    public void setCodModificacion(int codModificacion)
    {
        this.codModificacion = codModificacion;
    } 
    /**
     * @return Returns the buscarEstado.
     */
    public String getBuscarEstado()
    {
        return buscarEstado;
    }
    /**
     * @param buscarEstado The buscarEstado to set.
     */
    public void setBuscarEstado(String buscarEstado)
    {
        this.buscarEstado = buscarEstado;
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
    
    /**
     * 
     * @return
     */
	public String getRegistroINVIMA() {
		return registroINVIMA;
	}
	
	/**
	 * @param registroINVIMA
	 */
	public void setRegistroINVIMA(String registroINVIMA) {
		this.registroINVIMA = registroINVIMA;
	}
	
	/**
	 * 
	 * @return
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

	/**
	 * 
	 * @param key
	 * @return
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
	public int getIndexViaAdminEliminar()
	{
		return indexViaAdminEliminar;
	}
	
	/**
	 * 
	 * @param indexViaAdminEliminar
	 */
	public void setIndexViaAdminEliminar(int indexViaAdminEliminar)
	{
		this.indexViaAdminEliminar = indexViaAdminEliminar;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndexUnidosisEliminar()
	{
		return indexUnidosisEliminar;
	}
	
	/**
	 * 
	 * @param indexUnidosisEliminar
	 */
	public void setIndexUnidosisEliminar(int indexUnidosisEliminar)
	{
		this.indexUnidosisEliminar = indexUnidosisEliminar;
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
	public String getPrecioBaseVentaViejo() {
		return precioBaseVentaViejo;
	}
	
	/**
	 * 
	 * @param precioBaseVentaViejo
	 */
	public void setPrecioBaseVentaViejo(String precioBaseVentaViejo) {
		this.precioBaseVentaViejo = precioBaseVentaViejo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPrecioUltimaCompraViejo() {
		return precioUltimaCompraViejo;
	}
	
	/**
	 * 
	 * @param precioUltimaCompraViejo
	 */
	public void setPrecioUltimaCompraViejo(String precioUltimaCompraViejo) {
		this.precioUltimaCompraViejo = precioUltimaCompraViejo;
	}
	
	/**
	 * @return the ubicacionMap
	 */
	public HashMap getUbicacionMap() {
		return ubicacionMap;
	}
	
	/**
	 * @param ubicacionMap the ubicacionMap to set
	 */
	public void setUbicacionMap(HashMap ubicacionMap) {
		this.ubicacionMap = ubicacionMap;
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
	 * Retorna el nombre generado del documento adjunto
	 * @param key
	 * @return Object
	 */
	public Object getDocumentoAdjuntoGenerado(String key)
	{
		return documentosAdjuntosGenerados.get(key);
	}	
	
	
	/**
	 * Asigna el nombre generado del documento adjunto bajo la llave dada
	 * @param key
	 * @param value
	 */
	public void setDocumentoAdjuntoGenerado(String key, Object value) 
	{
		String val = (String) value;
		
		if (val != null) 
			val = val.trim();

		documentosAdjuntosGenerados.put(key, value);
	}

	/**
	 * @return Returns the documentosAdjuntosGenerados.
	 */
	public Map getDocumentosAdjuntosGenerados() {
		return documentosAdjuntosGenerados;
	}

	/**
	 * @return Returns the numDocumentosAdjuntos.
	 */
	public int getNumDocumentosAdjuntos() {
		return numDocumentosAdjuntos;
	}

	/**
	 * @param numDocumentosAdjuntos The numDocumentosAdjuntos to set.
	 */
	public void setNumDocumentosAdjuntos(int numDocumentosAdjuntos) {
		this.numDocumentosAdjuntos = numDocumentosAdjuntos;
	}
	
	/**
	 * @return Returns the codigoRespuesta.
	 */
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	/**
	 * @param codigoRespuesta The codigoRespuesta to set.
	 */
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getInfoMedicaMap() {
		return infoMedicaMap;
	}
	
	/**
	 * 
	 * @param infoMedicaMap
	 */
	public void setInfoMedicaMap(HashMap infoMedicaMap) {
		this.infoMedicaMap = infoMedicaMap;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getInfoMedicaAdjMap() {
		return infoMedicaAdjMap;
	}
	
	/**
	 * 
	 * @param infoMedicaAdjMap
	 */
	public void setInfoMedicaAdjMap(HashMap infoMedicaAdjMap) {
		this.infoMedicaAdjMap = infoMedicaAdjMap;
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
	public String getPrecioCompraMasAltaViejo() {
		return precioCompraMasAltaViejo;
	}
	
	/**
	 * 
	 * @param precioCompraMasAltaViejo
	 */
	public void setPrecioCompraMasAltaViejo(String precioCompraMasAltaViejo) {
		this.precioCompraMasAltaViejo = precioCompraMasAltaViejo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCostoPromedioViejo() {
		return costoPromedioViejo;
	}
	
	/**
	 * 
	 * @param costoPromedioViejo
	 */
	public void setCostoPromedioViejo(String costoPromedioViejo) {
		this.costoPromedioViejo = costoPromedioViejo;
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
	 * @return
	 */
	public boolean isInfoMedica() {
		return infoMedica;
	}
	
	/**
	 * 
	 * @param infoMedica
	 */
	public void setInfoMedica(boolean infoMedica) {
		this.infoMedica = infoMedica;
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
	 * @return
	 */
	public int getIndexGrupoEspecialEliminar() {
		return indexGrupoEspecialEliminar;
	}
	
	/**
	 * 
	 * @param indexGrupoEspecialEliminar
	 */
	public void setIndexGrupoEspecialEliminar(int indexGrupoEspecialEliminar) {
		this.indexGrupoEspecialEliminar = indexGrupoEspecialEliminar;
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
	 * @return the seccionCum
	 */
	public boolean isSeccionCum() {
		return seccionCum;
	}
	/**
	 * @param seccionCum the seccionCum to set
	 */
	public void setSeccionCum(boolean seccionCum) {
		this.seccionCum = seccionCum;
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
	 * @return the cumMap
	 */
	public Object getCumMap(String llave) {
		return cumMap.get(llave);
	}
	/**
	 * @param cumMap the cumMap to set
	 */
	public void setCumMap(String llave, Object obj) {
		this.cumMap.put(llave, obj);
	}
	
	/**
	 * M�todo setMostrarInfoMedica
	 * @param mostrarInfoMedica
	 */
	public void setMostrarInfoMedica(boolean mostrarInfoMedica) {
		this.mostrarInfoMedica = mostrarInfoMedica;
	}
	
	/**
	 * M�todo isMostrarInfoMedica
	 * @return mostrarInfoMedica (boolean)
	 */
	public boolean isMostrarInfoMedica() {
		return mostrarInfoMedica;
	}
	public void setEsEsteArticuloUsado(boolean esEsteArticuloUsado) {
		this.esEsteArticuloUsado = esEsteArticuloUsado;
	}
	public boolean getEsEsteArticuloUsado() {
		return esEsteArticuloUsado;
	}
	public String getTipoInventario() {
		return tipoInventario;
	}
	public void setTipoInventario(String tipoInventario) {
		this.tipoInventario = tipoInventario;
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
	public void setNivelesAtencion(ArrayList<NivelAtencion> nivelesAtencion) {
		this.nivelesAtencion = nivelesAtencion;
	}
	public ArrayList<NivelAtencion> getNivelesAtencion() {
		return nivelesAtencion;
	}
	public void setDescripcionNivelAtencion(String descripcionNivelAtencion) {
		this.descripcionNivelAtencion = descripcionNivelAtencion;
	}
	public String getDescripcionNivelAtencion() {
		return descripcionNivelAtencion;
	}
	
	
	
}