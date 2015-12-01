/*
 * Creado en 2/07/2004
 *
 * Juan David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.actionform.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * @author Juan David Ramírez
 */
public class MontosCobroForm extends ActionForm
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(MontosCobroForm.class);
	
	/**
	 * Se utiliza para la consulta de montos de cobro
	 */
	private Collection consulta;
	
	/**
	 * Al eliminar un registro, este no se debe tener en cuenta para nada
	 * En este string, van a estar todos los registros eliminados
	 */
	private String eliminados;
	
	/**
	 * Manejo de estados se la clase
	 */
	private String estado;
	
	/**
	 * Manejo de todo el contenido del form
	 */
	private HashMap propiedad = new HashMap();
	
	/**
	 * Numero total de inserciones realizadas
	 */
	private int numeroIngresos = 0;
	
	/**
	 * Manejar el orden por columnas
	 */
	private String columna;
	
	/**
	 * Manejar el orden por columnas (Ultima coliumna ordenada)
	 */
	private String columnaAnterior;
	
	/**
	 * Variable donde se almacena el codigo del monto
	 * para el detalle
	 */
	private int codigo;
	
	/**
	 * Variable para guardar el nombre del id del select de la via de ingreso
	 */
	private String idViaIngreso="";
	
	/**
	 * Variable para guardar el valor del value del select de la via de ingreso
	 */
	private String valueViaIngreso="";
	
	/**
	 * Obtener la propiedad con una llave específica
	 * @param key
	 * @return
	 */
	public Object getPropiedad(String key)
	{
		return propiedad.get(key);
	}
	
	/**
	 * Asignar una propiedad con una llave específica
	 * @param key
	 * @param value
	 */
	public void setPropiedad(String key, Object value)
	{
		propiedad.put(key, value);
	}
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		logger.info("valor del estado >> "+estado);
		
		if(estado.equals("resultadoBusqueda") || estado.equals("consultar") || estado.equals("resultadoModificar"))
		{
			return new ActionErrors();			
		}
		/*if(estado.equals("empezar"))
		{
		    ActionErrors errores= new ActionErrors();
		    for(int k=0; k < numeroIngresos; k++)
		    {
		        try
		        {
		            if((propiedad.get("valorRadio_"+k)+"").equals("on"))
		            {
		                if(Integer.parseInt((propiedad.get("valor_"+k)+"")) == 0)
		                {
		                    errores.add("valor_"+k, new ActionMessage("errors.floatMayorQue","El valor en registro "+(k+1), "0"));
		                }
		            }
		        }
		        catch(NumberFormatException e)
		        {
		            errores.add("valor_"+k, new ActionMessage("errors.floatMayorQue","El valor en registro "+(k+1), "0"));
		        }
		    }
		}*/
		if(!estado.equals("empezar") && !estado.equals("adicionar") && !estado.equals("consultar") && !estado.equals("modificar") 
				&& !estado.equals("detalleModificar") && !estado.equals("resultadoModificar"))
		{
			ActionErrors errores= new ActionErrors();
			String temp3= "";
			String[] temp4 = new String[1];
			String[] eliminado = new String[1];
			
			if(!eliminados.equals(""))
			{
				if(eliminado.length>1)
					eliminado=eliminados.split("-");
				else
					eliminado[0]=eliminados;
				
				temp4 = eliminado[0].split("-");
			}
			else			
			{
				eliminado[0]=new String("-1");
				temp4[0]=new String("-1");
			}
			
			logger.info("valor de ingresos >> "+numeroIngresos);
			Utilidades.imprimirMapa(propiedad);
			
			for(int i=0; i<numeroIngresos; i++)
			{
				boolean puedoValidar=true;
				for(int j=0;j<temp4.length;j++)
				{
					if(i==Integer.parseInt(temp4[j]))
					{
						puedoValidar=false;
					}
				}
				if(puedoValidar)
				{
					boolean hayError=false;
					if(Utilidades.convertirAEntero(""+propiedad.get("convenio_"+i))==0)
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "El convenio ";
						else
							temp3 = "El convenio en registro "+(i+1);
							
						errores.add("convenio_"+i, new ActionMessage("errors.required",temp3));
						hayError=true;
					}

					//Cambio de campo de vigencia Inicial en la funcionaldiad de monto de cobro
					if(propiedad.get("fecha_"+i).toString().equals(""))
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "La fecha de vigencia ";
						else
							temp3 = "La fecha de vigencia en registro "+(i+1);
							
						
						errores.add("fecha_"+i, new ActionMessage("errors.required",temp3));
						hayError=true;
					}
					//Fin cambio
					
					if(Utilidades.convertirAEntero((""+propiedad.get("estratoSocial_"+i)).trim())==0)
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "La clasificación socioeconómica ";
						else
							temp3 = "La clasificación socioeconómica en registro "+(i+1);
							
						
						errores.add("estratoSocial_"+i, new ActionMessage("errors.required",temp3));
						hayError=true;
					}
					String temp=String.valueOf(propiedad.get("porcentaje_"+i));
					float porcentaje=0;
					int valor=0;
					if(temp!=null && !temp.equals(""))
					{
						try
						{
							porcentaje=Float.parseFloat(temp);
							if(porcentaje<0 || porcentaje>100)
							{
								if(estado.equals("guardarModificacion"))
									temp3 = "El porcentaje ";
								else
									temp3 = "El porcentaje en registro "+(i+1);
								
								errores.add("porcentaje_"+i, new ActionMessage("errors.range",temp3, "0", "100"));
								hayError=true;
							}
						}
						catch(Exception e)
						{
							if(estado.equals("guardarModificacion"))
								temp3 = "El porcentaje ";
							else
								temp3 = "El porcentaje en registro "+(i+1);
							
							
							errores.add("porcentaje_"+i, new ActionMessage("errors.range",temp3, "0", "100"));
							hayError=true;
						}
					}
					else
					{
						porcentaje=0;
					}
					if(String.valueOf(propiedad.get("tipoAfiliado_"+i)).equals("0"))
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "El Tipo de Afiliado ";
						else
							temp3 = "El Tipo de Afiliado en registro "+(i+1);
						
						errores.add("tipoAfiliado_"+i, new ActionMessage("errors.required",temp3));
						hayError=true;
					}
					if(Utilidades.convertirAEntero(""+propiedad.get("tipoMonto_"+i))==0)
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "El Tipo de Monto ";
						else
							temp3 = "El Tipo de Monto en registro "+(i+1);
						
						errores.add("tipoMonto_"+i, new ActionMessage("errors.required",temp3));
						hayError=true;
					}
					temp=String.valueOf(propiedad.get("valor_"+i));
					logger.info("valor=======> "+temp);
					if(temp.length()>=2)
						logger.info("valor cortado=======>"+temp.substring(temp.length()-2,temp.length()));
					if(temp!=null && !temp.equals(""))
					{
						try
						{
							valor=Integer.parseInt(temp);
							if(valor<0)
							{
								if(estado.equals("guardarModificacion"))
									temp3 = "El Valor ";
								else
									temp3 = "El Valor en registro "+(i+1);
								
								errores.add("valor_"+i, new ActionMessage("errors.MayorQue",temp3, "0"));
								hayError=true;
							}
							else
								//se redondea a la centena
								propiedad.put("valor_"+i,UtilidadCadena.redondearALaCentena(valor)+"");
						}
						catch(Exception e)
						{
							if(estado.equals("guardarModificacion"))
								temp3 = "El Valor ";
							else
								temp3 = "El Valor en registro "+(i+1);
							
							errores.add("valor_"+i, new ActionMessage("errors.floatMayorQue",temp3, "0"));
							hayError=true;
						}
					}
					else
					{
						valor=-1;
					}
					String temp2=String.valueOf(propiedad.get("porcentaje_"+i));
					
					if(temp2!=null&&!temp2.equals(""))
					{
						try
						{
							porcentaje = Float.parseFloat(temp2);
						}
						catch(Exception e)
						{
							porcentaje = -1;
						}
					}
					else
						porcentaje = -1;
					
					if(!temp.equals("") && !temp2.equals(""))
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "modificado ";
						else
							temp3 = " "+(i+1);
						
						errores.add("valor_porcentaje_"+i, new ActionMessage("errors.excluyentesMontosCobro",temp3));
						hayError=true;
						porcentaje=Utilidades.convertirAFloat(temp2);
					}
					
					if(temp.equals("") && temp2.equals(""))
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "Valor ó Porcentaje ";
						else
							temp3 = "Valor ó Porcentaje en Registro "+(i+1);
						
						errores.add("valor_porcentaje_"+i, new ActionMessage("errors.required",temp3));
						hayError=true;
					}
					if(Utilidades.convertirAEntero(""+propiedad.get("viaIngreso_"+i))==0)
					{
						if(estado.equals("guardarModificacion"))
							temp3 = "La Vía de Ingreso ";
						else
							temp3 = "La Vía de Ingreso en registro "+(i+1);
						
						errores.add("viaIngreso_"+i, new ActionMessage("errors.required",temp3));
						hayError=true;
					}
					/*if(propiedad.get("activo_"+i)!=null)
					{
						if(request.getParameter("propiedad(activo_"+i+")")==null)
						{
							setPropiedad("activo_"+i, "off");
						}
						if(!String.valueOf(propiedad.get("activo_"+i)).equals("off"))
						propiedad.put("activo_"+i, "on");
					}
					else
					{
						propiedad.put("activo_"+i, "off");				
					}*/
					if(!hayError)
					{
						Connection con = UtilidadBD.abrirConexion();
						if(UtilidadValidacion.existeMontoEnBD(con, Integer.parseInt(""+propiedad.get("convenio_"+i)), Integer.parseInt(""+propiedad.get("viaIngreso_"+i)), String.valueOf(propiedad.get("tipoAfiliado_"+i)), Integer.parseInt((""+propiedad.get("estratoSocial_"+i))), Integer.parseInt(""+propiedad.get("tipoMonto_"+i)), valor, porcentaje, UtilidadTexto.getBoolean(propiedad.get("activo_"+i)+""),propiedad.get("fecha_"+i)+""))
						{
							if(estado.equals("guardarModificacion"))
								temp3 = " ";
							else
								temp3 = ""+(i+1);
							//Cambio Tarea 99036
							//errores.add("montoRepetido_"+i, new ActionMessage("errors.montoExistente",temp3));
						}
						try
						{
							UtilidadBD.cerrarConexion(con);
						}
						catch (SQLException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			if(!errores.isEmpty())
			{
				numeroIngresos--;
			}
			else
			{
				Vector montosCobro=new Vector(); 
				for(int i=0; i<numeroIngresos; i++)
				{
					boolean puedoValidar=true;
					for(int j=0;j<temp4.length;j++)
					{
						if(i==Integer.parseInt(temp4[j]))
						{
							puedoValidar=false;
						}
					}
					if(puedoValidar)
					{
						String montosCobroStr=new String(); 
						montosCobroStr+=""+propiedad.get("convenio_"+i);
						montosCobroStr+=""+propiedad.get("estratoSocial_"+i);
						String tempo=propiedad.get("porcentaje_"+i)+"";
						if(tempo!=null && !tempo.equals(""))
							montosCobroStr+=""+propiedad.get("porcentaje_"+i);
						else
							montosCobroStr+="-1";
						montosCobroStr+=String.valueOf(propiedad.get("tipoAfiliado_"+i));
						montosCobroStr+=""+propiedad.get("tipoMonto_"+i);
						tempo=propiedad.get("valor_"+i)+"";
						if(tempo!=null && !tempo.equals(""))
							montosCobroStr+=""+propiedad.get("valor_"+i);
						else
							montosCobroStr+="-1";
						montosCobroStr+=""+propiedad.get("viaIngreso_"+i);
						if(montosCobro.contains(montosCobroStr))
						{
							if(estado.equals("guardarModificacion"))
								temp3 = " ";
							else
								temp3 = ""+(i+1);
							//Cambio Tarea 99036
							//errores.add("montoRepetido_"+i, new ActionMessage("errors.montoExistente",temp3));
							break;
						}
						else
						{
							montosCobro.add(montosCobroStr);
						}
					}
				}
				if(!errores.isEmpty())
				{
					numeroIngresos--;
				}
			}
			
			return errores;
		}	
		return null;
	}
	
	/**
	 * Resetear la forma
	 */
	public void reset()
	{
		propiedad=new HashMap();
		setPropiedad("convenio_0","0");
		setPropiedad("viaIngreso_0","0");
		setPropiedad("tipoAfiliado_0","0");
		setPropiedad("estratoSocial_0","0");
		setPropiedad("valor_0","");
		setPropiedad("tipoMonto_0","0");
		setPropiedad("porcentaje_0","");
		setPropiedad("activo_0","0");
		setPropiedad("vigenciaInicial_0","0");
		numeroIngresos=0;
		eliminados="";
		this.codigo = 0;
		this.idViaIngreso="";
		this.valueViaIngreso="";
	}

	/**
	 * Resetear la forma para la búsqueda
	 */
	public void resetBusqueda()
	{
		propiedad=new HashMap();
		numeroIngresos=0;
		eliminados="";
	}

/*
	/**
	 * Resetear solo los checkbox para que me conserve el estado de los selects
	 */
/*
	public void resetChecks()
	{
		setPropiedad("convenio_check",null);
		setPropiedad("viaIngreso_check",null);
		setPropiedad("tipoAfiliado_check",null);
		setPropiedad("estratoSocial_check",null);
		setPropiedad("valor_check",null);
		setPropiedad("tipoMonto_check",null);
		setPropiedad("porcentaje_check",null);
		setPropiedad("activo_check",null);
	}
	*/

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
	 * @return Retorna HashMap con todas las propiedades del formulario.
	 */
	public HashMap getMapPropiedades()
	{
		return propiedad;
	}
	
	/**
	 * @param propiedad Asigna un HashMap con todas las propiedades del formulario.
	 */
	public void setMapPropiedades(HashMap propiedad)
	{
		this.propiedad = propiedad;
	}
	
	/**
	 * @return Retorna numeroIngresos.
	 */
	public int getNumeroIngresos()
	{
		return numeroIngresos;
	}
	
	/**
	 * @param numeroIngresos Asigna numeroIngresos.
	 */
	public void setNumeroIngresos(int numeroIngresos)
	{
		this.numeroIngresos = numeroIngresos;
	}
	
	/**
	 * @return Retorna eliminados.
	 */
	public String getEliminados()
	{
		return eliminados;
	}
	
	/**
	 * @param eliminados Asigna eliminados.
	 */
	public void setEliminados(String eliminados)
	{
		this.eliminados = eliminados;
	}
	
	/**
	 * @return Retorna consulta.
	 */
	public Collection getConsulta()
	{
		return consulta;
	}
	
	/**
	 * @param consulta Asigna consulta.
	 */
	public void setConsulta(Collection consulta)
	{
		this.consulta = consulta;
	}

	/**
	 * @return Retorna columna.
	 */
	public String getColumna()
	{
		return columna;
	}
	/**
	 * Asigna columna
	 * @param columna.
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}
	/**
	 * @return Retorna columnaAnterior.
	 */
	public String getColumnaAnterior()
	{
		return columnaAnterior;
	}
	/**
	 * Asigna columnaAnterior
	 * @param columnaAnterior.
	 */
	public void setColumnaAnterior(String columnaAnterior)
	{
		this.columnaAnterior = columnaAnterior;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getIdViaIngreso() {
		return idViaIngreso;
	}

	public void setIdViaIngreso(String idViaIngreso) {
		this.idViaIngreso = idViaIngreso;
	}

	public String getValueViaIngreso() {
		return valueViaIngreso;
	}

	public void setValueViaIngreso(String valueViaIngreso) {
		this.valueViaIngreso = valueViaIngreso;
	}

	public HashMap getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(HashMap propiedad) {
		this.propiedad = propiedad;
	}
}
