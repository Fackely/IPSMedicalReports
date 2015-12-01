package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ResultadoBoolean;
import util.Utilidades;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.PlantillaComponenteDto;


public class ParametrizacionComponentesForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String estado;
	
	private String componente; 
	
	private HashMap<String, Object> mapaConsultaSecciones;
	
	private HashMap<String, Object> mapaConsultaEscalas;
	
	private HashMap<String, Object> mapaConsultaCampos;
	
	private HashMap<String, Object> mapaEscalas;
	
	private HashMap<String, Object> mapaSecciones;
	
	private HashMap<String, Object> mapaCampos;
	
	private HashMap<String, Object> mapaOpciones;
	
	private HashMap<String, Object> mapaDetalleSeccion;
	
	private HashMap<String, Object> mapaDetalleEscala;
	
	private HashMap<String, Object> mapaDetalleCampo;
	
	private HashMap<String, Object> mapaConsultaSubseccion;
	
	private HashMap<String, Object> mapaConsultaCamposSeccion;
	
	private HashMap<String, Object> mapaSignosVitales;
	
	private HashMap<String, Object> mapaCodigosSeccion;
	
	private HashMap<String, Object> mapaOrdenSeccion;
	
	private HashMap<String, Object> mapaOrdenEscala;
	
	private HashMap<String, Object> mapaOrdenCampos;
	
	private String seccion;
	
	private int indiceSeccion;
	
	private String campo;
	
	private int indiceCampo;
	
	////
	private int indiceSeccionEliminar;
	
	private int indiceEscalaEliminar;
	
	private int indiceCampoEliminar;
	
	private int indiceOpcionesEliminar;
	
	
	private String escala;
	
	private int indiceEscala;
	
	private String indexNivel;
	
	private String indexSeccionNivel2;
	
	private String indexCampo;
	
	private ResultadoBoolean mostrarMensaje;
	
	
	//////
	private HashMap<String, Object> mapaEscalaModificar;
	
	private HashMap<String, Object> mapaSeccionesExistentes;
	
	private HashMap<String, Object> mapaCamposExistentes;
	
	private HashMap formulaMap;
	
	private String formulaComprobar;
	
	private HashMap mapaCamposFormula;
	
	
	private String codigoSeccion="";
	
	
	///////////Anexo 728.
	
	private HashMap mapaSeccionesAsociadas;
	
	private String seccionesAsociadas;
	
	private HashMap mapaSeccionesGeneradas;
	
	private int numSeccionesGeneradas;
	
	private String valoresAsociar;
	
	private HashMap mapaValoresGenerados;
	
	private int numValoresGenerados;
	
	private String indexOpciones;
	
	private HashMap mapaSeccionesPrevioAsocio;
	
	private HashMap mapaValoresPrevioAsocio;	
	
	
	//*****************************************************
	private DtoComponente componentePreview;
	
	private String indexOpcion;
	
	private boolean primeraVezModificarOpciones;
	
	private boolean primeraVezModificarValores;
	
	private int valorIndexPrimeraVez;
	
	/**
	 * Atributo almacena la plantilla seleccionada
	 */
	private String graficaCurvas;
	
	/**
	 * Atributo con la lista de las Curvas posibles a escoger para el componente
	 */
	private List<CurvaCrecimientoParametrizabDto> listaGraficaCurvas = new ArrayList<CurvaCrecimientoParametrizabDto>();
	
	/**
	 * Atributo con la lista de las curvas seleccionadas para el componente
	 */
	private List<PlantillaComponenteDto> listaGraficaCurvasSeleccionadas = new ArrayList<PlantillaComponenteDto>();
	
	/**
	 * Atributo con el index (listaGraficaCurvasSeleccionadas) de la Curva eliminada del Componente 
	 */
	private String indexCurvaEliminada;
	
	/**
	 * Atributo con la lista de curvas eliminadas del componente
	 */
	private List<PlantillaComponenteDto> listaGraficaCurvasEliminadas = new ArrayList<PlantillaComponenteDto>();
	
	/**
	 * Atributo activa cuando se guarda
	 */
	private boolean procesoExitoso = false;
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.componente="";
		this.seccion="";
		this.indiceSeccion=ConstantesBD.codigoNuncaValido;
		this.campo="";
		this.indiceCampo=ConstantesBD.codigoNuncaValido;
		this.escala="";
		this.indiceEscala=ConstantesBD.codigoNuncaValido;
		this.mapaConsultaSecciones=new HashMap<String, Object>();
		this.mapaConsultaSecciones.put("numRegistros", "0");
		this.mapaConsultaEscalas=new HashMap<String, Object>();
		this.mapaConsultaEscalas.put("numRegistros", "0");
		this.mapaConsultaCampos=new HashMap<String, Object>();
		this.mapaConsultaCampos.put("numRegistros", "0");
		this.mapaEscalas=new HashMap<String, Object>();
		this.mapaEscalas.put("numRegistros", "0");
		this.mapaSecciones=new HashMap<String, Object>();
		this.mapaSecciones.put("numRegistros", "0");
		this.mapaCampos=new HashMap<String, Object>();
		this.mapaCampos.put("numRegistros", "0");
		this.mapaOpciones=new HashMap<String, Object>();
		this.mapaOpciones.put("numRegistros", "0");
		this.mapaDetalleSeccion=new HashMap<String, Object>();
		this.mapaDetalleSeccion.put("numRegistros", "0");
		this.mapaDetalleEscala=new HashMap<String, Object>();
		this.mapaDetalleEscala.put("numRegistros", "0");
		this.mapaDetalleCampo=new HashMap<String, Object>();
		this.mapaDetalleCampo.put("numRegistros", "0");
		this.mapaConsultaSubseccion=new HashMap<String, Object>();
		this.mapaConsultaSecciones.put("numRegitros", "0");
		this.mapaConsultaCamposSeccion=new HashMap<String, Object>();
		this.mapaConsultaCamposSeccion.put("numRegistros", "0");
		this.mapaSignosVitales=new HashMap<String, Object>();
		this.mapaSignosVitales.put("numRegistros", "0");
		this.mapaCodigosSeccion= new HashMap<String, Object>();
		this.mapaCodigosSeccion.put("numRegistros", "0");
		this.mapaOrdenCampos=new HashMap<String, Object>();
		this.mapaOrdenCampos.put("numRegistros", "0");
		this.mapaOrdenEscala=new HashMap<String, Object>();
		this.mapaOrdenEscala.put("numRegistros", "0");
		this.mapaOrdenSeccion=new HashMap<String, Object>();
		this.mapaOrdenSeccion.put("numRegistros", "0");
		
		///////
		this.mapaEscalaModificar=new HashMap<String, Object>();
		this.mapaEscalaModificar.put("numRegistros", "0");
		this.mapaSeccionesExistentes=new HashMap<String, Object>();
		this.mapaSeccionesExistentes.put("numRegistros", "0");
		this.mapaCamposExistentes=new HashMap<String, Object>();
		this.mapaCamposExistentes.put("numRegistros", "0");
		
		this.formulaMap= new HashMap();
		this.formulaMap.put("numRegistros", "0");
		this.mapaCamposFormula= new HashMap();
		this.mapaCamposFormula.put("numRegistros", "0");
		this.formulaComprobar="";
		
		this.indexNivel="";
		this.indexSeccionNivel2="";
		this.indexCampo="";
		this.indiceSeccionEliminar=ConstantesBD.codigoNuncaValido;
		this.indiceEscalaEliminar=ConstantesBD.codigoNuncaValido;
		this.indiceCampoEliminar=ConstantesBD.codigoNuncaValido;
		this.indiceOpcionesEliminar=ConstantesBD.codigoNuncaValido;
		
		//////Anexo 728.
		this.mapaSeccionesAsociadas = new HashMap();
		this.mapaSeccionesAsociadas.put("numRegistros", "0");
		this.seccionesAsociadas="";
		this.mapaSeccionesGeneradas = new HashMap();
		this.mapaSeccionesGeneradas.put("numRegistros", "0");
		this.numSeccionesGeneradas=0;
		this.valoresAsociar="";
		this.mapaValoresGenerados = new HashMap();
		this.mapaValoresGenerados.put("numRegistros", "0");
		this.numValoresGenerados=0;
		this.indexOpciones="";
		this.mapaSeccionesPrevioAsocio = new HashMap();
		this.mapaSeccionesPrevioAsocio.put("numRegistros", "0");
		this.mapaValoresPrevioAsocio = new HashMap();
		this.mapaValoresPrevioAsocio.put("numRegistros", "0");
		
		//*****************************************************
		this.componentePreview = new DtoComponente();
		
		this.primeraVezModificarOpciones=false;
		this.primeraVezModificarValores=false;
		this.valorIndexPrimeraVez=ConstantesBD.codigoNuncaValido;
		
		this.graficaCurvas = "";
		this.indexCurvaEliminada = "";
		this.procesoExitoso = false;
	}

	
	public void resetCurvasCrecimiento(){
		this.listaGraficaCurvas = new ArrayList<CurvaCrecimientoParametrizabDto>();
		this.listaGraficaCurvasSeleccionadas = new ArrayList<PlantillaComponenteDto>();
		this.listaGraficaCurvasEliminadas = new ArrayList<PlantillaComponenteDto>();
	}
	
	/**
	 * 
	 *
	 */
	public void resetSeccion()
	{
		this.mapaSecciones=new HashMap<String, Object>();
		this.mapaSecciones.put("numRegistros", "0");
		this.mapaCampos=new HashMap<String, Object>();
		this.mapaCampos.put("numRegistros", "0");
		this.mapaEscalas= new HashMap<String, Object>();
		this.mapaEscalas.put("numRegistros", "0");
		
	}
	
	/**
	 * 
	 *
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje=new ResultadoBoolean(false,"");
	}
	
	public void resetProcesoExitoso(){
		this.procesoExitoso = false;
	}
	
	/**
	 * 
	 */
	public void resetComponentePreview()
	{
		this.componentePreview = new DtoComponente();
	}
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardarCampo")||this.estado.equals("modificarCampo"))
		{
			
			int numRegOpciones= Utilidades.convertirAEntero(this.mapaOpciones.get("numRegistros")+"");
			
			int numRegCampos= Utilidades.convertirAEntero(this.mapaCampos.get("numRegistros")+"");
			
			for(int i=0;i<numRegCampos;i++)
			{
			
				if((this.mapaCampos.get("codigocampo_"+i)+"").equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El codigo del Campo del registro "+(i+1)));
				}
				else
				{
					
					//Verifica que no existan codigos Repetidos entre si
					for(int j=0; j<numRegCampos; j++)
					{
						if((this.mapaCampos.get("codigocampo_"+i)+"").equals(this.mapaCampos.get("codigocampo_"+j)+"") && 
								i!=j)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El Codigo del campo del registro "+(i+1)+" se encuentra repetido con el codigo del campo del registro "+(j+1)+". Verificar los Nuevos Registros."));
							
						}
						if((this.mapaCampos.get("nombrecampo_"+i)+"").equals(this.mapaCampos.get("nombrecampo_"+j)+"") && 
								i!=j)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El Nombre del campo del registro "+(i+1)+" se encuentra repetido con el nombre del campo del registro "+(j+1)+". Verificar los Nuevos Registros."));
							
						}
					}
					
				}
				if((this.mapaCampos.get("nombrecampo_"+i)+"").equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El nombre del Campo del registro "+(i+1)));
				}
				if((this.mapaCampos.get("etiqueta_"+i)+"").equals("")&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoTextoPredeterminado+"")))
				{
					errores.add("codigo", new ActionMessage("errors.required","La etiqueta del campo del registro "+(i+1)));
				}
				if((this.mapaCampos.get("tipocampo_"+i)+"").equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El tipo de Campo del registro "+(i+1)));
				}
				if((this.mapaCampos.get("tamanocampo_"+i)+"").equals("")&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoCaracter+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoAreaTexto+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoEntero+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoDecimal+"")))
				{
					errores.add("codigo", new ActionMessage("errors.required","El tamaño del Campo del registro "+(i+1)));
				}
				if((this.mapaCampos.get("signocampo_"+i)+"").equals("")&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoEntero+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoDecimal+"")))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Signo del Campo del registro "+(i+1)));
				}
				if((this.mapaCampos.get("valorminimo_"+i)+"").equals("")&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoEntero+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoDecimal+"")))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Valor minimo del registro "+(i+1)));
				}
				if((this.mapaCampos.get("valormaximo_"+i)+"").equals("")&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoEntero+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoDecimal+"")))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Valor maximo del registro "+(i+1)));
				}
				else
				{
					if(Utilidades.convertirADouble(this.mapaCampos.get("valormaximo_"+i)+"")<Utilidades.convertirADouble(this.mapaCampos.get("valorminimo_"+i)+""))
					{
						errores.add("codigo", new ActionMessage("errors.notEspecific","El Valor maximo del registro "+(i+1)+" debe ser mayor al valor minimo"));
					}
					if(!this.mapaCampos.get("tipocampo_"+i).equals(ConstantesCamposParametrizables.tipoCampoCaracter+"") && !(this.mapaCampos.get("valorpredeterminado_"+i)+"").equals("")&&(Utilidades.convertirADouble(this.mapaCampos.get("valorpredeterminado_"+i)+"")>Utilidades.convertirADouble(this.mapaCampos.get("valormaximo_"+i)+"")||Utilidades.convertirADouble(this.mapaCampos.get("valorpredeterminado_"+i)+"")<Utilidades.convertirADouble(this.mapaCampos.get("valorminimo_"+i)+"")))
					{
						errores.add("codigo", new ActionMessage("errors.notEspecific","El Valor predeterminado del registro "+(i+1)+" debe estar entre el valor minimo y el valor maximo."));
					}
				}
				if((this.mapaCampos.get("unidadcampo_"+i)+"").equals("")&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoEntero+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoDecimal+"")))
				{
					errores.add("codigo", new ActionMessage("errors.required","La unidad del registro "+(i+1)));
				}
				if((this.mapaCampos.get("numerodecimales_"+i)+"").equals("")&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoNumericoDecimal+"")))
				{
					errores.add("codigo", new ActionMessage("errors.required","El numero de decimales del registro "+(i+1)));
				}
				if((this.mapaCampos.get("unicofila_"+i)+"").equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El campo unico x fila del registro "+(i+1)));
				}
				if((this.mapaCampos.get("columnasocupadas_"+i)+"").equals("")&&(this.mapaCampos.get("unicofila_"+i)+"").equals(ConstantesBD.acronimoNo))
				{
					errores.add("codigo", new ActionMessage("errors.required","El campo columnas ocupadas del registro "+(i+1)));
					
				}
				else
				{
					if((Utilidades.convertirAEntero(this.mapaCampos.get("columnasocupadas_"+i)+"")==0))
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","El numero de Columnas para el Registro Nro. "+(i+1)+" debe ser mayor a cero "));
					}
					if((Utilidades.convertirAEntero(this.getMapaCampos("columnasocupadas_"+i)+"")>(Utilidades.convertirAEntero(this.getMapaDetalleSeccion("columnaseccion_0")+""))))
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","El numero de columnas a ocupar del registro "+(i+1)+" excede el numero de columas ("+this.getMapaDetalleSeccion("columnaseccion_0")+") de la seccion, por favor verifique."));
					}
				}
				if((numRegOpciones<1)&&((this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoChequeo+"")||(this.mapaCampos.get("tipocampo_"+i)+"").equals(ConstantesCamposParametrizables.tipoCampoSeleccion+"")))
				{
					errores.add("codigo", new ActionMessage("errors.notEspecific","Se debe adicionar un valor como minimo en el campo opciones."));
				}
				for(int k=0;k<numRegOpciones;k++)
				{
					if((this.mapaOpciones.get("opcion_"+k)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","La opcion de la Seccion opciones del registro "+(k+1)));
					}
					if((this.mapaOpciones.get("valor_"+k)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","La valor de la Seccion opciones del registro "+(k+1)));
					}
				}
			}	
				
		}
		if(this.estado.equals("guardarEscala"))
		{
			
			int numRegEscala= Utilidades.convertirAEntero(this.mapaEscalas.get("numRegistros")+"");
			
			for(int i=0;i<numRegEscala;i++)
			{
				if((this.mapaEscalas.get("escala_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El campo escala del registro "+(i+1)));
				}
				else
				{
					
					//Verifica que no existan codigos Repetidos entre si
					for(int j=0; j<numRegEscala; j++)
					{
						if((this.mapaEscalas.get("escala_"+i)+"").equals(this.mapaEscalas.get("escala_"+j)+"") && 
								i!=j)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El Registro Nro. "+(i+1)+" se encuentra repetido con el Registro Nro. "+(j+1)+". Verificar los Nuevos Registros."));
							
						}
					}
					
				}
			}
			
		}
		if(this.estado.equals("guardarSeccion")||this.estado.equals("modificarSeccion"))
		{
			
			int numRegSeccion= Utilidades.convertirAEntero(this.mapaSecciones.get("numRegistros")+"");
			
			for(int i=0;i<numRegSeccion;i++)
			{
				
				if(!(this.mapaSecciones.get("edadinicial_"+i)+"").trim().equals("")&&(this.mapaSecciones.get("edadfinal_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La edad final del registro "+(i+1)));
				}
				if(!(this.mapaSecciones.get("edadfinal_"+i)+"").trim().equals("")&&(this.mapaSecciones.get("edadinicial_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La edad inicial del registro "+(i+1)));
				}
				else if(Utilidades.convertirAEntero(this.mapaSecciones.get("edadinicial_"+i)+"")>(Utilidades.convertirAEntero(this.mapaSecciones.get("edadfinal_"+i)+"")))
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","La edad final en dias  del Registro Nro. "+(i+1)+" debe ser mayor o igual que la edad inicial en dias. Verificar los Nuevos Registros."));
				}
				if((this.mapaSecciones.get("codigoseccion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El codigo de la seccion del registro "+(i+1)));
				}
				else
				{
					//Verifica que no existan codigos Repetidos entre si
					for(int j=0; j<numRegSeccion; j++)
					{
						if((this.mapaSecciones.get("codigoseccion_"+i)+"").equals(this.mapaSecciones.get("codigoseccion_"+j)+"") && 
								i!=j)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El Codigo de la seccion del registro "+(i+1)+" se encuentra repetido con el codigo de la seccion del registro "+(j+1)+". Verificar los Nuevos Registros."));
							
						}
						if((this.mapaSecciones.get("nombreseccion_"+i)+"").equals(this.mapaSecciones.get("nombreseccion_"+j)+"") && 
								i!=j)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El Nombre de la seccion del registro "+(i+1)+" se encuentra repetido con el nombre de la seccion del registro "+(j+1)+". Verificar los Nuevos Registros."));
							
						}
					}
					
				}
				if((this.mapaSecciones.get("columnaseccion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El campo columna del registro "+(i+1)));
				}
				else
				{
					if((Utilidades.convertirAEntero(this.mapaSecciones.get("columnaseccion_"+i)+"")==0))
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","El numero de Columnas para el Registro Nro. "+(i+1)+" debe ser mayor a cero "));
					}
				}
				
			}	
			
		}
		if(estado.equals("guardarSignos"))
		{
			for(int i=0; i<Utilidades.convertirAEntero(this.getMapaSignosVitales("numRegistros")+"");i++)
			{
				
				if((this.getMapaSignosVitales().get("ordensigno_"+i)+"").equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Campo orden del registro "+this.getMapaSignosVitales("nombresigno_"+i)));
				}
				else
				{
					
					//Verifica que no existan codigos Repetidos entre si
					for(int j=0; j<Utilidades.convertirAEntero(this.getMapaSignosVitales("numRegistros")+""); j++)
					{
						if((this.getMapaSignosVitales().get("ordensigno_"+i)+"").equals(this.getMapaSignosVitales().get("ordensigno_"+j)+"") && 
								i!=j)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","El campo orden del registro "+this.getMapaSignosVitales("nombresigno_"+i)+" se encuentra repetido con el codigo del campo del registro "+this.getMapaSignosVitales("nombresigno_"+j)+". Favor verificar."));
							
						}
					}
					
				}
			}
		}
		return errores;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getComponente() {
		return componente;
	}

	/**
	 * 
	 * @param componente
	 */
	public void setComponente(String componente) {
		this.componente = componente;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaEscalas() {
		return mapaEscalas;
	}

	/**
	 * 
	 * @param mapaEscalas
	 */
	public void setMapaEscalas(HashMap<String, Object> mapaEscalas) {
		this.mapaEscalas = mapaEscalas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaEscalas(String key) {
		return mapaEscalas.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaEscalas(String key,Object value) {
		this.mapaEscalas.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaCampos() {
		return mapaConsultaCampos;
	}

	/**
	 * 
	 * @param mapaConsultaCampos
	 */
	public void setMapaConsultaCampos(HashMap<String, Object> mapaConsultaCampos) {
		this.mapaConsultaCampos = mapaConsultaCampos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaCampos(String key) {
		return mapaConsultaCampos.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaCampos(String key,Object value) {
		this.mapaConsultaCampos.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaEscalas() {
		return mapaConsultaEscalas;
	}

	/**
	 * 
	 * @param mapaConsultaEscalas
	 */
	public void setMapaConsultaEscalas(HashMap<String, Object> mapaConsultaEscalas) {
		this.mapaConsultaEscalas = mapaConsultaEscalas;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaEscalas(String key) {
		return mapaConsultaEscalas.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaEscalas(String key,Object value) {
		this.mapaConsultaEscalas.put(key, value);
	}
	

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaSecciones() {
		return mapaConsultaSecciones;
	}

	/**
	 * 
	 * @param mapaConsultaSecciones
	 */
	public void setMapaConsultaSecciones(
			HashMap<String, Object> mapaConsultaSecciones) {
		this.mapaConsultaSecciones = mapaConsultaSecciones;
	}

	
	public Object getMapaConsultaSecciones(String key) {
		return mapaConsultaSecciones.get(key);
	}

	
	public void setMapaConsultaSecciones(String key,Object value) {
		this.mapaConsultaSecciones.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaSecciones() {
		return mapaSecciones;
	}

	/**
	 * 
	 * @param mapaSecciones
	 */
	public void setMapaSecciones(HashMap<String, Object> mapaSecciones) {
		this.mapaSecciones = mapaSecciones;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSecciones(String key) {
		return mapaSecciones.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSecciones(String key,Object value) {
		this.mapaSecciones.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaCampos() {
		return mapaCampos;
	}

	/**
	 * 
	 * @param mapaCampos
	 */
	public void setMapaCampos(HashMap<String, Object> mapaCampos) {
		this.mapaCampos = mapaCampos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaCampos(String key) {
		return mapaCampos.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaCampos(String key,Object value) {
		this.mapaCampos.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaOpciones() {
		return mapaOpciones;
	}

	/**
	 * 
	 * @param mapaOpciones
	 */
	public void setMapaOpciones(HashMap<String, Object> mapaOpciones) {
		this.mapaOpciones = mapaOpciones;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaOpciones(String key) {
		return mapaOpciones.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaOpciones(String key,Object value) {
		this.mapaOpciones.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * 
	 * @param seccion
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * 
	 * @return
	 */
	public String getCampo() {
		return campo;
	}

	/**
	 * 
	 * @param campo
	 */
	public void setCampo(String campo) {
		this.campo = campo;
	}

	/**
	 * 
	 * @return
	 */
	public String getEscala() {
		return escala;
	}

	/**
	 * 
	 * @param escala
	 */
	public void setEscala(String escala) {
		this.escala = escala;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceCampo() {
		return indiceCampo;
	}

	/**
	 * 
	 * @param indiceCampo
	 */
	public void setIndiceCampo(int indiceCampo) {
		this.indiceCampo = indiceCampo;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceEscala() {
		return indiceEscala;
	}

	/**
	 * 
	 * @param indiceEscala
	 */
	public void setIndiceEscala(int indiceEscala) {
		this.indiceEscala = indiceEscala;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceSeccion() {
		return indiceSeccion;
	}

	/**
	 * 
	 * @param indiceSeccion
	 */
	public void setIndiceSeccion(int indiceSeccion) {
		this.indiceSeccion = indiceSeccion;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaDetalleCampo() {
		return mapaDetalleCampo;
	}

	/**
	 * 
	 * @param mapaDetalleCampo
	 */
	public void setMapaDetalleCampo(HashMap<String, Object> mapaDetalleCampo) {
		this.mapaDetalleCampo = mapaDetalleCampo;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleCampo(String key) {
		return mapaDetalleCampo.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaDetalleCampo(String key,Object value) {
		this.mapaDetalleCampo.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaDetalleEscala() {
		return mapaDetalleEscala;
	}

	/**
	 * 
	 * @param mapaDetalleEscala
	 */
	public void setMapaDetalleEscala(HashMap<String, Object> mapaDetalleEscala) {
		this.mapaDetalleEscala = mapaDetalleEscala;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleEscala(String key) {
		return mapaDetalleEscala.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaDetalleEscala(String key,Object value) {
		this.mapaDetalleEscala.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaDetalleSeccion() {
		return mapaDetalleSeccion;
	}

	/**
	 * 
	 * @param mapaDetalleSeccion
	 */
	public void setMapaDetalleSeccion(HashMap<String, Object> mapaDetalleSeccion) {
		this.mapaDetalleSeccion = mapaDetalleSeccion;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleSeccion(String key) {
		return mapaDetalleSeccion.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaDetalleSeccion(String key,Object value) {
		this.mapaDetalleSeccion.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaCamposSeccion() {
		return mapaConsultaCamposSeccion;
	}

	/**
	 * 
	 * @param mapaConsultaCamposSeccion
	 */
	public void setMapaConsultaCamposSeccion(
			HashMap<String, Object> mapaConsultaCamposSeccion) {
		this.mapaConsultaCamposSeccion = mapaConsultaCamposSeccion;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaCamposSeccion(String key) {
		return mapaConsultaCamposSeccion.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaCamposSeccion(String key,Object value) {
		this.mapaConsultaCamposSeccion.put(key, value);
	}
	
	
	

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaSubseccion() {
		return mapaConsultaSubseccion;
	}

	/**
	 * 
	 * @param mapaConsultaSubseccion
	 */
	public void setMapaConsultaSubseccion(
			HashMap<String, Object> mapaConsultaSubseccion) {
		this.mapaConsultaSubseccion = mapaConsultaSubseccion;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaSignosVitales() {
		return mapaSignosVitales;
	}

	/**
	 * 
	 * @param mapaSignosVitales
	 */
	public void setMapaSignosVitales(HashMap<String, Object> mapaSignosVitales) {
		this.mapaSignosVitales = mapaSignosVitales;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSignosVitales(String key) {
		return mapaSignosVitales.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSignosVitales(String key,Object value) {
		this.mapaSignosVitales.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaCodigosSeccion() {
		return mapaCodigosSeccion;
	}

	/**
	 * 
	 * @param mapaCodigosSeccion
	 */
	public void setMapaCodigosSeccion(HashMap<String, Object> mapaCodigosSeccion) {
		this.mapaCodigosSeccion = mapaCodigosSeccion;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaOrdenCampos() {
		return mapaOrdenCampos;
	}

	/**
	 * 
	 * @param mapaOrdenCampos
	 */
	public void setMapaOrdenCampos(HashMap<String, Object> mapaOrdenCampos) {
		this.mapaOrdenCampos = mapaOrdenCampos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */	
	public Object getMapaOrdenCampos(String key) {
		return mapaOrdenCampos.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaOrdenCampos(String key,Object value) {
		this.mapaOrdenCampos.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaOrdenEscala() {
		return mapaOrdenEscala;
	}

	/**
	 * 
	 * @param mapaOrdenEscala
	 */
	public void setMapaOrdenEscala(HashMap<String, Object> mapaOrdenEscala) {
		this.mapaOrdenEscala = mapaOrdenEscala;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */	
	public Object getMapaOrdenEscala(String key) {
		return mapaOrdenEscala.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaOrdenEscala(String key,Object value) {
		this.mapaOrdenEscala.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaOrdenSeccion() {
		return mapaOrdenSeccion;
	}

	/**
	 * 
	 * @param mapaOrdenSeccion
	 */
	public void setMapaOrdenSeccion(HashMap<String, Object> mapaOrdenSeccion) {
		this.mapaOrdenSeccion = mapaOrdenSeccion;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaOrdenSeccion(String key) {
		return mapaOrdenSeccion.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setMapaOrdenSeccion(String key,Object value) {
		this.mapaOrdenSeccion.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexNivel() {
		return indexNivel;
	}

	/**
	 * 
	 * @param indexNivel
	 */
	public void setIndexNivel(String indexNivel) {
		this.indexNivel = indexNivel;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexSeccionNivel2() {
		return indexSeccionNivel2;
	}

	/**
	 * 
	 * @param indexSeccionNivel2
	 */
	public void setIndexSeccionNivel2(String indexSeccionNivel2) {
		this.indexSeccionNivel2 = indexSeccionNivel2;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexCampo() {
		return indexCampo;
	}

	/**
	 * 
	 * @param indexCampo
	 */
	public void setIndexCampo(String indexCampo) {
		this.indexCampo = indexCampo;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceSeccionEliminar() {
		return indiceSeccionEliminar;
	}

	/**
	 * 
	 * @param indiceSeccionEliminar
	 */
	public void setIndiceSeccionEliminar(int indiceSeccionEliminar) {
		this.indiceSeccionEliminar = indiceSeccionEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaEscalaModificar() {
		return mapaEscalaModificar;
	}

	/**
	 * 
	 * @param mapaEscalaModificar
	 */
	public void setMapaEscalaModificar(HashMap<String, Object> mapaEscalaModificar) {
		this.mapaEscalaModificar = mapaEscalaModificar;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */	
	public Object getMapaEscalaModificar(String key) {
		return mapaEscalaModificar.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaEscalaModificar(String key,Object value) {
		this.mapaEscalaModificar.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaSeccionesExistentes() {
		return mapaSeccionesExistentes;
	}

	/**
	 * 
	 * @param mapaSeccionesExistentes
	 */
	public void setMapaSeccionesExistentes(
			HashMap<String, Object> mapaSeccionesExistentes) {
		this.mapaSeccionesExistentes = mapaSeccionesExistentes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSeccionesExistentes(String key) {
		return mapaSeccionesExistentes.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSeccionesExistentes(String key,Object value) {
		this.mapaSeccionesExistentes.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceEscalaEliminar() {
		return indiceEscalaEliminar;
	}

	/**
	 * 
	 * @param indiceEscalaEliminar
	 */
	public void setIndiceEscalaEliminar(int indiceEscalaEliminar) {
		this.indiceEscalaEliminar = indiceEscalaEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceCampoEliminar() {
		return indiceCampoEliminar;
	}

	/**
	 * 
	 * @param indiceCampoEliminar
	 */
	public void setIndiceCampoEliminar(int indiceCampoEliminar) {
		this.indiceCampoEliminar = indiceCampoEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceOpcionesEliminar() {
		return indiceOpcionesEliminar;
	}

	/**
	 * 
	 * @param indiceOpcionesEliminar
	 */
	public void setIndiceOpcionesEliminar(int indiceOpcionesEliminar) {
		this.indiceOpcionesEliminar = indiceOpcionesEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaCamposExistentes() {
		return mapaCamposExistentes;
	}

	/**
	 * 
	 * @param mapaCamposExistentes
	 */
	public void setMapaCamposExistentes(HashMap<String, Object> mapaCamposExistentes) {
		this.mapaCamposExistentes = mapaCamposExistentes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaCamposExistentes(String key) {
		return mapaCamposExistentes.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaCamposExistentes(String key,Object value) {
		this.mapaCamposExistentes.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getFormulaMap() {
		return formulaMap;
	}

	/**
	 * 
	 * @param formulaMap
	 */
	public void setFormulaMap(HashMap formulaMap) {
		this.formulaMap = formulaMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getFormulaMap(String key){
		return formulaMap.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setFormulaMap(String key,Object value) {
		this.formulaMap.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public String getFormulaComprobar() {
		return formulaComprobar;
	}

	/**
	 * 
	 * @param formulaComprobar
	 */
	public void setFormulaComprobar(String formulaComprobar) {
		this.formulaComprobar = formulaComprobar;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaCamposFormula() {
		return mapaCamposFormula;
	}

	/**
	 * 
	 * @param mapaCamposFormula
	 */
	public void setMapaCamposFormula(HashMap mapaCamposFormula) {
		this.mapaCamposFormula = mapaCamposFormula;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaCamposFormula(String key){
		return mapaCamposFormula.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaCamposFormula(String key,Object value) {
		this.mapaCamposFormula.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	/**
	 * 
	 * @param codigoSeccion
	 */
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaSeccionesAsociadas() {
		return mapaSeccionesAsociadas;
	}

	/**
	 * 
	 * @param mapaSeccionesAsociadas
	 */
	public void setMapaSeccionesAsociadas(HashMap mapaSeccionesAsociadas) {
		this.mapaSeccionesAsociadas = mapaSeccionesAsociadas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSeccionesAsociadas(String key){
		return mapaSeccionesAsociadas.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSeccionesAsociadas(String key,Object value) {
		this.mapaSeccionesAsociadas.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getSeccionesAsociadas() {
		return seccionesAsociadas;
	}

	/**
	 * 
	 * @param seccionesAsociadas
	 */
	public void setSeccionesAsociadas(String seccionesAsociadas) {
		this.seccionesAsociadas = seccionesAsociadas;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaSeccionesGeneradas() {
		return mapaSeccionesGeneradas;
	}

	/**
	 * 
	 * @param mapaSeccionesGeneradas
	 */
	public void setMapaSeccionesGeneradas(HashMap mapaSeccionesGeneradas) {
		this.mapaSeccionesGeneradas = mapaSeccionesGeneradas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSeccionesGeneradas(String key){
		return mapaSeccionesGeneradas.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSeccionesGeneradas(String key,Object value) {
		this.mapaSeccionesGeneradas.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public int getNumSeccionesGeneradas() {
		return numSeccionesGeneradas;
	}

	/**
	 * 
	 * @param numSeccionesGeneradas
	 */
	public void setNumSeccionesGeneradas(int numSeccionesGeneradas) {
		this.numSeccionesGeneradas = numSeccionesGeneradas;
	}

	/**
	 * 
	 * @return
	 */
	public String getValoresAsociar() {
		return valoresAsociar;
	}

	/**
	 * 
	 * @param valoresAsociar
	 */
	public void setValoresAsociar(String valoresAsociar) {
		this.valoresAsociar = valoresAsociar;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaValoresGenerados() {
		return mapaValoresGenerados;
	}

	/**
	 * 
	 * @param mapaValoresGenerados
	 */
	public void setMapaValoresGenerados(HashMap mapaValoresGenerados) {
		this.mapaValoresGenerados = mapaValoresGenerados;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaValoresGenerados(String key) {
		return mapaValoresGenerados.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaValoresGenerados(String key, Object value) {
		this.mapaValoresGenerados.put(key, value);
	}
		
	/**
	 * 
	 * @return
	 */
	public int getNumValoresGenerados() {
		return numValoresGenerados;
	}

	/**
	 * 
	 * @param numValoresGenerados
	 */
	public void setNumValoresGenerados(int numValoresGenerados) {
		this.numValoresGenerados = numValoresGenerados;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexOpciones() {
		return indexOpciones;
	}

	/**
	 * 
	 * @param indexOpciones
	 */
	public void setIndexOpciones(String indexOpciones) {
		this.indexOpciones = indexOpciones;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaSeccionesPrevioAsocio() {
		return mapaSeccionesPrevioAsocio;
	}

	/**
	 * 
	 * @param mapaSeccionesPrevioAsocio
	 */
	public void setMapaSeccionesPrevioAsocio(HashMap mapaSeccionesPrevioAsocio) {
		this.mapaSeccionesPrevioAsocio = mapaSeccionesPrevioAsocio;
	}
	
	/**
	 * 	
	 * @param key
	 * @return
	 */
	public Object getMapaSeccionesPrevioAsocio(String key) {
		return mapaSeccionesPrevioAsocio.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSeccionesPrevioAsocio(String key, Object value) {
		this.mapaSeccionesPrevioAsocio.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaValoresPrevioAsocio() {
		return mapaValoresPrevioAsocio;
	}

	/**
	 * 
	 * @param mapaValoresPrevioAsocio
	 */
	public void setMapaValoresPrevioAsocio(HashMap mapaValoresPrevioAsocio) {
		this.mapaValoresPrevioAsocio = mapaValoresPrevioAsocio;
	}
	
	/**
	 * 	
	 * @param key
	 * @return
	 */
	public Object getMapaValoresPrevioAsocio(String key) {
		return mapaValoresPrevioAsocio.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaValoresPrevioAsocio(String key, Object value) {
		this.mapaValoresPrevioAsocio.put(key, value);
	}


	/**
	 * @return the componentePreview
	 */
	public DtoComponente getComponentePreview() {
		return componentePreview;
	}


	/**
	 * @param componentePreview the componentePreview to set
	 */
	public void setComponentePreview(DtoComponente componentePreview) {
		this.componentePreview = componentePreview;
	}


	public boolean isPrimeraVezModificarOpciones() {
		return primeraVezModificarOpciones;
	}


	public void setPrimeraVezModificarOpciones(boolean primeraVezModificarOpciones) {
		this.primeraVezModificarOpciones = primeraVezModificarOpciones;
	}


	public boolean isPrimeraVezModificarValores() {
		return primeraVezModificarValores;
	}


	public void setPrimeraVezModificarValores(boolean primeraVezModificarValores) {
		this.primeraVezModificarValores = primeraVezModificarValores;
	}


	public int getValorIndexPrimeraVez() {
		return valorIndexPrimeraVez;
	}


	public void setValorIndexPrimeraVez(int valorIndexPrimeraVez) {
		this.valorIndexPrimeraVez = valorIndexPrimeraVez;
	}
	
	/**
	 * @return the graficaCurva
	 */
	public String getGraficaCurvas() {
		return graficaCurvas;
	}
	
	/**
	 * @param graficaCurva the graficaCurva to set
	 */
	public void setGraficaCurvas(String graficaCurvas) {
		this.graficaCurvas = graficaCurvas;
	}
	
	/**
	 * @return the listaGraficaCurvas
	 */
	public List<CurvaCrecimientoParametrizabDto> getListaGraficaCurvas() {
		return listaGraficaCurvas;
	}

	/**
	 * @param listaGraficaCurvas the listaGraficaCurvas to set
	 */
	public void setListaGraficaCurvas(
			List<CurvaCrecimientoParametrizabDto> listaGraficaCurvas) {
		this.listaGraficaCurvas = listaGraficaCurvas;
	}

	/**
	 * @return the listaGraficaCurvasSeleccionadas
	 */
	public List<PlantillaComponenteDto> getListaGraficaCurvasSeleccionadas() {
		return listaGraficaCurvasSeleccionadas;
	}

	/**
	 * @param listaGraficaCurvasSeleccionadas the listaGraficaCurvasSeleccionadas to set
	 */
	public void setListaGraficaCurvasSeleccionadas(
			List<PlantillaComponenteDto> listaGraficaCurvasSeleccionadas) {
		this.listaGraficaCurvasSeleccionadas = listaGraficaCurvasSeleccionadas;
	}

	/**
	 * @return the indexCurvaEliminada
	 */
	public String getIndexCurvaEliminada() {
		return indexCurvaEliminada;
	}

	/**
	 * @param indexCurvaEliminada the indexCurvaEliminada to set
	 */
	public void setIndexCurvaEliminada(String indexCurvaEliminada) {
		this.indexCurvaEliminada = indexCurvaEliminada;
	}

	/**
	 * @return the listaGraficaCurvasEliminadas
	 */
	public List<PlantillaComponenteDto> getListaGraficaCurvasEliminadas() {
		return listaGraficaCurvasEliminadas;
	}

	/**
	 * @param listaGraficaCurvasEliminadas the listaGraficaCurvasEliminadas to set
	 */
	public void setListaGraficaCurvasEliminadas(
			List<PlantillaComponenteDto> listaGraficaCurvasEliminadas) {
		this.listaGraficaCurvasEliminadas = listaGraficaCurvasEliminadas;
	}

	/**
	 * @return the procesoExitoso
	 */
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}

	/**
	 * @param procesoExitoso the procesoExitoso to set
	 */
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}
	
}
