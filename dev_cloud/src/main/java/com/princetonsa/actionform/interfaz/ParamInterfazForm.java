/*
 * Creado el 27/04/2006
 * Juan David Ramírez López
 */
package com.princetonsa.actionform.interfaz;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;

public class ParamInterfazForm extends ValidatorForm
{

	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Manejo de estados del flujo
	 */
	private String estado;
	
	/**
	 * Tipo de interfaz para el cual se va a hacer el ingreso de la información
	 */
	private int tipoInterfaz;
	
	/**
	 * Manejo de la descripción de la parametrización
	 */
	private String descripcion;
	
	/**
	 * Manejo de los consecutivos de cada parametrización
	 */
	private int consecutivo;

	/**
	 * Manejo de los campos parametrizados en funcionalidad Parametrización Campos Interfaz
	 */
	private HashMap camposInterfaz;
	
	/**
	 * Colección para manejar los tipos de indicativo sio no existe
	 */
	private Collection tiposIndicativoExiste;

	/**
	 * Colección para manejar los tipos de tamaño
	 */
	private Collection tiposTamanio;
	
	/**
	 * Manejar las opciones de selección de los campos parametrizados
	 */
	private Collection opcionesSelector;
	
	/**
	 * Manejar la parametrización de las opciones de los selectores por campo
	 */
	private Collection selectores;
	
	/**
	 * Manejo de los tipos de Interfaz (Facturación - Anulación)
	 */
	private Collection tiposInterfaz;
	
	/**
	 * Listado de los registros de interfdaz antes de ver el detalle
	 */
	private Collection listadoRegistrosInterfaz;
	
	/**
	 * Código del registro para consultar el detalle
	 */
	private int codigoRegistro;
	
	/**
	 * Manejo tipos de registro
	 */
	private Collection tiposRegistro;
	
	/**
	 * Tipo de registro
	 */
	private int tipoRegistro;
	
	/**
	 * Códigos de los tipos en los cuales se puede ingresar una descripcion
	 */
	private Collection tiposLibres;
	
	/**
	 * Validador de datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equalsIgnoreCase("guardar") || estado.equalsIgnoreCase("guardarModificacion"))
		{
			int numeroRegistros=Integer.parseInt(camposInterfaz.get("numRegistros")+"");
			for(int i=0; i<numeroRegistros; i++)
			{
				int campo=Integer.parseInt(camposInterfaz.get("codigo_tipo_campo_"+i)+"");
				String nombreCampo=(String)camposInterfaz.get("tipo_campo_"+i);
				switch(campo)
				{
					case ConstantesBD.codigoTipoCampoSecuencia:
						//No se hace nada
					break;
					case ConstantesBD.codigoTipoCampoLibre:
						//No se hace nada
					break;
/*
					case ConstantesBD.codigoTipoCampoLibre:
					break;
					case ConstantesBD.codigoTipoCampoPrefijoFactura:
					break;
					case ConstantesBD.codigoTipoCampoFehcaAnio:
					break;
					case ConstantesBD.codigoTipoCampoFechaMes:
					break;
					case ConstantesBD.codigoTipoCampoFechaDia:
					break;
					case ConstantesBD.codigoTipoCampoFechaHora:
					break;
					case ConstantesBD.codigoTipoCampoFechaDDMMAAAA:
					break;
					case ConstantesBD.codigoTipoCampoFehcaAAAAMMDD:
					break;
					case ConstantesBD.codigoTipoCampoFehcaHoraAAAAMMDDMMSS:
					break;
					case ConstantesBD.codigoTipoCampoFechaHoraDDMMAAAAMMSS:
					break;
					case ConstantesBD.codigoTipoCampoValores:
					break;
*/
					case ConstantesBD.codigoTipoCampoCuentasContables:
					case ConstantesBD.codigoTipoCampoTipoMovimiento:
/*
					case ConstantesBD.codigoTipoCampoNumeroIdSistema:
					break;
*/
					case ConstantesBD.codigoTipoCampoCentroCostoSistema:
						int tipoSelector=Integer.parseInt(camposInterfaz.get("select_secundario_"+i)+"");
						if(tipoSelector==0)
						{
							errores.add("campo_sec_"+i, new ActionMessage("errors.required","Valor asocio para el campo \""+nombreCampo+"\""));
						}
					break;
/*
					case ConstantesBD.codigoTipoCampoCampoDetalleSistema:
					break;
					case ConstantesBD.codigoTipoCampoUsuario:
					break;
*/
					default:
						tipoSelector=Integer.parseInt(camposInterfaz.get("select_principal_"+i)+"");
						if(tipoSelector==0)
						{
							errores.add("campo_"+i, new ActionMessage("errors.required","Valor para el campo \""+nombreCampo+"\""));
						}
					break;
				}
				int requerido=Integer.parseInt(camposInterfaz.get("existe_"+i)+"");
				if(requerido==0)
				{
					errores.add("requerido_"+i, new ActionMessage("errors.required","Indicativo requerido para el campo \""+nombreCampo+"\""));
				}
				else if(requerido==ConstantesBD.codIndicativoSiNoExisteDefault)
				{
					if(!UtilidadCadena.noEsVacio(camposInterfaz.get("text_existe_"+i)+""))
					{
						errores.add("text_requerido_"+i, new ActionMessage("errors.required","Valor Asociado Ind. Requerido (Default) para el campo \""+nombreCampo+"\""));
					}
				}
				int tamanio=Integer.parseInt(camposInterfaz.get("tamanio_"+i)+"");
				if(tamanio==0)
				{
					errores.add("tamanio_"+i, new ActionMessage("errors.required","Tamaño para el campo \" "+nombreCampo+"\""));
				}
				else if(tamanio==ConstantesBD.codTamanioFijo || tamanio==ConstantesBD.codTamanioMenorQue)
				{
					String texto=camposInterfaz.get("text_tamanio_"+i)+"";
					if(UtilidadCadena.noEsVacio(texto))
					{
						try{
							Integer.parseInt(texto);
						}
						catch (NumberFormatException e)
						{
							errores.add("text_tamanio_"+i, new ActionMessage("errors.integerMayorQue","Valor Asociado Tamaño para el campo \""+nombreCampo+"\"", "0"));
						}
						
					}
					else
					{
						String error="";
						if(tamanio==ConstantesBD.codTamanioFijo)
						{
							error="Fijo";
						}
						else
						{
							error="Menor Que";
						}
						errores.add("text_tamanio_"+i, new ActionMessage("errors.required","Valor Asociado Tamaño ("+error+") para el campo \""+nombreCampo+"\""));
					}
				}
			}
		}
		return errores;
	}

	/**
	 * Método para resetear la clase
	 * @param estado
	 * @param camposInterfaz
	 */
	public void reset()
	{
		this.camposInterfaz = new HashMap();
		this.tiposIndicativoExiste=null;
		this.tiposTamanio=null;
		this.opcionesSelector=null;
		this.selectores=null;
		this.tiposInterfaz=null;
		this.tipoInterfaz=0;
		this.descripcion="";
		this.consecutivo=0;
		this.tiposRegistro=null;
		this.tipoRegistro=0;
		this.tiposLibres=null;
	}

	/**
	 * @return Retorna camposInterfaz.
	 */
	public HashMap getCamposInterfaz()
	{
		return camposInterfaz;
	}

	/**
	 * @param camposInterfaz Asigna camposInterfaz.
	 */
	public void setCamposInterfaz(HashMap camposInterfaz)
	{
		this.camposInterfaz = camposInterfaz;
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
	 * @return Retorna opcionesSelector.
	 */
	public Collection getOpcionesSelector()
	{
		return opcionesSelector;
	}

	/**
	 * @param opcionesSelector Asigna opcionesSelector.
	 */
	public void setOpcionesSelector(Collection opcionesSelector)
	{
		this.opcionesSelector = opcionesSelector;
	}

	/**
	 * @return Retorna selectores.
	 */
	public Collection getSelectores()
	{
		return selectores;
	}

	/**
	 * @param selectores Asigna selectores.
	 */
	public void setSelectores(Collection selectores)
	{
		this.selectores = selectores;
	}

	/**
	 * @return Retorna tipoInterfaz.
	 */
	public int getTipoInterfaz()
	{
		return tipoInterfaz;
	}

	/**
	 * @param tipoInterfaz Asigna tipoInterfaz.
	 */
	public void setTipoInterfaz(int tipoInterfaz)
	{
		this.tipoInterfaz = tipoInterfaz;
	}

	/**
	 * @return Retorna tiposIndicativoExiste.
	 */
	public Collection getTiposIndicativoExiste()
	{
		return tiposIndicativoExiste;
	}

	/**
	 * @param tiposIndicativoExiste Asigna tiposIndicativoExiste.
	 */
	public void setTiposIndicativoExiste(Collection tiposIndicativoExiste)
	{
		this.tiposIndicativoExiste = tiposIndicativoExiste;
	}

	/**
	 * @return Retorna tiposInterfaz.
	 */
	public Collection getTiposInterfaz()
	{
		return tiposInterfaz;
	}

	/**
	 * @param tiposInterfaz Asigna tiposInterfaz.
	 */
	public void setTiposInterfaz(Collection tiposInterfaz)
	{
		this.tiposInterfaz = tiposInterfaz;
	}

	/**
	 * @return Retorna tiposTamanio.
	 */
	public Collection getTiposTamanio()
	{
		return tiposTamanio;
	}

	/**
	 * @param tiposTamanio Asigna tiposTamanio.
	 */
	public void setTiposTamanio(Collection tiposTamanio)
	{
		this.tiposTamanio = tiposTamanio;
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
	 * @return Retorna consecutivo.
	 */
	public int getConsecutivo()
	{
		return consecutivo;
	}

	/**
	 * @param consecutivo Asigna consecutivo.
	 */
	public void setConsecutivo(int consecutivo)
	{
		this.consecutivo = consecutivo;
	}

	/**
	 * @return Retorna listadoRegistrosInterfaz.
	 */
	public Collection getListadoRegistrosInterfaz()
	{
		return listadoRegistrosInterfaz;
	}

	/**
	 * @param listadoRegistrosInterfaz Asigna listadoRegistrosInterfaz.
	 */
	public void setListadoRegistrosInterfaz(Collection listadoRegistrosInterfaz)
	{
		this.listadoRegistrosInterfaz = listadoRegistrosInterfaz;
	}

	/**
	 * @return Retorna codigoRegistro.
	 */
	public int getCodigoRegistro()
	{
		return codigoRegistro;
	}

	/**
	 * @param codigoRegistro Asigna codigoRegistro.
	 */
	public void setCodigoRegistro(int codigoRegistro)
	{
		this.codigoRegistro = codigoRegistro;
	}

	/**
	 * @return Retorna tiposRegistro.
	 */
	public Collection getTiposRegistro()
	{
		return tiposRegistro;
	}

	/**
	 * @param tiposRegistro Asigna tiposRegistro.
	 */
	public void setTiposRegistro(Collection tiposRegistro)
	{
		this.tiposRegistro = tiposRegistro;
	}

	/**
	 * @return Retorna tipoRegistro.
	 */
	public int getTipoRegistro()
	{
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro Asigna tipoRegistro.
	 */
	public void setTipoRegistro(int tipoRegistro)
	{
		this.tipoRegistro = tipoRegistro;
	}

	/**
	 * @return Retorna tiposLibres.
	 */
	public Collection getTiposLibres()
	{
		return tiposLibres;
	}

	/**
	 * @param tiposLibres Asigna tiposLibres.
	 */
	public void setTiposLibres(Collection tiposLibres)
	{
		this.tiposLibres = tiposLibres;
	}


}
