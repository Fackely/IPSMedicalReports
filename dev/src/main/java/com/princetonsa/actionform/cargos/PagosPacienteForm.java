/*
 * Creado en 16/07/2004
 *
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * @author Juan David Ramírez López
 *
 */
public class PagosPacienteForm extends ValidatorForm
{
	/**
	 * Manejar los check de la búsqueda avanzada
	 */
	private HashMap checks;
	
	/**
	 * Propiedad para guardar el nombre de la columna por la cual se quiere ordenar
	 */
	private String ordenxColumna;
	
	/**
	 * Propiedad para guardar el nombre de la columna por la cual se ordenó la ultima vez
	 */
	private String ordenxColumnaAnterior;
	
	/**
	 * Año para el manejo del monto
	 */
	private String anio;
	
	/**
	 * Manejo del código utilizado para la modificación
	 */
	private int codigo;
	
	/**
	 * Manejo de consulta de pagos por paciente
	 */
	private Collection consulta;
	
	/**
	 * Conservar el texto por el cual se hizo la busqueda
	 */
	private String textDiagnostico;
	
	/**
	 * Manejar el estado del flujo
	 */
	private String estado;
	
	/**
	 * Campo entidad
	 */
	private String entidad;
	
	/**
	 * Tipo de monto
	 */
	private int tipoMonto;
	
	/**
	 * Documento de la entidad
	 */
	private String documento;
	
	/**
	 * Fecha del monto
	 */
	private String fecha;
	
	/**
	 * Diagnóstico para el cual se hizo el pago
	 */
	private String diagnostico;
	
	/**
	 * Tipo de cie del diagnóstico
	 */
	private int tipoCie;
	
	/**
	 * Descripcion del pago
	 */
	private String descripcion;
	
	/**
	 * Valor del pago
	 */
	private double valor;
	
	/**
	 * Valor del pago
	 */
	private String valorString;
	
	/**
	 * Origen del pago
	 */
	private int origen;
	
	/**
	 * Usuario que registró el pago
	 */
	private int codigoUsuario;
	
	/**
	 * Manejar el codigoUsuario en la busqueda
	 */
	private String usuario;
	
	/**
	 * Institución para la cual se realizó el pago
	 */
	private int institucion;

	/**
	 * Tipo de regimen ligado al pago
	 */
	private String tipoRegimen;
	
	/**
	 * Método el cual resetea los checkbox del form
	 *
	 */
	public void resetChecks()
	{
		checks=new HashMap();
	}
	public void reset()
	{
		consulta=null;
		entidad="";
		tipoMonto=0;
		documento="";
		fecha="";
		diagnostico="";
		tipoCie=0;
		descripcion="";
		valor=0;
		origen=0;
		codigoUsuario=0;
		usuario="";
		institucion=0;
		textDiagnostico="";
		ordenxColumna="";
		ordenxColumnaAnterior="";
		checks=new HashMap();
		valorString="";
		tipoRegimen="";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("empezar") || estado.equals("consultar") || estado.equals("consultarOtro") || estado.equals("resultado"))
		{
			return null;
		}
		if(estado.equals("guardar") || estado.equals("guardarModificacion"))
		{
			errores=super.validate(mapping, request);
			if(entidad.equals(""))
			{
				errores.add("campoEntidad",new ActionMessage("errors.required","El campo Entidad"));
			}
			if(tipoMonto==0)
			{
				errores.add("campoMonto",new ActionMessage("errors.required","El campo Tipo de Pago"));
			}
			if(fecha.equals(""))
			{
				errores.add("fecha",new ActionMessage("errors.required","La Fecha del Pago"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(fecha))
				{
					errores.add("fecha",new ActionMessage("errors.formatoFechaInvalido",fecha));
				}
			}
			/*
			 * Esta validación se quitó por requerimiento de los clientes
			if(diagnostico.equals(""))
			{
				errores.add("diagnostico",new ActionMessage("errors.required","El Diagnóstico"));
			}
			 * La Validación se remplazó por la siguiente para que no genere problemas en la base de datos
			**/
			if(diagnostico.equals(""))
			{
				diagnostico=null;
			}
			/*
			 * La descripción no se debe validar
			if(descripcion.equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Descripción"));
			}
			 */
			if(documento.equals(""))
			{
				errores.add("documento",new ActionMessage("errors.required","El Documento"));
			}
			if(valorString.equals(""))
			{
				errores.add("valor",new ActionMessage("errors.required","El Valor del Pago"));
			}
			else
			{
				try
				{
					valor=Double.parseDouble(valorString);
					if(valor<=0)
					{
						errores.add("valor",new ActionMessage("errors.floatMayorQue","El Valor del Pago", "0"));
					}
				}
				catch(NumberFormatException e)
				{
					errores.add("valor",new ActionMessage("errors.floatMayorQue","El Valor del Pago", "0"));
				}
			}
		}
		if(!errores.isEmpty())
		{
			estado="corregir";
		}
		return errores;
	}
	
	/**
	 * @return Returna el descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}
	/**
	 * @param Asigna el descripcion.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	/**
	 * @return Returna el diagnostico.
	 */
	public String getDiagnostico()
	{
		return diagnostico;
	}
	/**
	 * @param Asigna el diagnostico.
	 */
	public void setDiagnostico(String diagnostico)
	{
		this.diagnostico = diagnostico;
	}
	/**
	 * @return Returna el documento.
	 */
	public String getDocumento()
	{
		return documento;
	}
	/**
	 * @param Asigna el documento.
	 */
	public void setDocumento(String documento)
	{
		this.documento = documento;
	}
	/**
	 * @return Returna el entidad.
	 */
	public String getEntidad()
	{
		return entidad;
	}
	/**
	 * @param Asigna el entidad.
	 */
	public void setEntidad(String entidad)
	{
		this.entidad = entidad;
	}
	/**
	 * @return Returna el estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param Asigna el estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Returna el fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}
	/**
	 * @param Asigna el fecha.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	/**
	 * @return Returna el institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}
	/**
	 * @param Asigna el institucion.
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}
	/**
	 * @return Returna el origen.
	 */
	public int getOrigen()
	{
		return origen;
	}
	/**
	 * @param Asigna el origen.
	 */
	public void setOrigen(int origen)
	{
		this.origen = origen;
	}
	/**
	 * @return Returna el tipoCie.
	 */
	public int getTipoCie()
	{
		return tipoCie;
	}
	/**
	 * @param Asigna el tipoCie.
	 */
	public void setTipoCie(int tipoCie)
	{
		this.tipoCie = tipoCie;
	}
	/**
	 * @return Returna el tipoMonto.
	 */
	public int getTipoMonto()
	{
		return tipoMonto;
	}
	/**
	 * @param Asigna el tipoMonto.
	 */
	public void setTipoMonto(int tipoMonto)
	{
		this.tipoMonto = tipoMonto;
	}
	/**
	 * @return Returna el codigoUsuario.
	 */
	public int getCodigoUsuario()
	{
		return codigoUsuario;
	}
	/**
	 * @param Asigna el codigoUsuario.
	 */
	public void setCodigoUsuario(int usuario)
	{
		this.codigoUsuario = usuario;
	}
	/**
	 * @return Returna el valor.
	 */
	public double getValor()
	{
		return valor;
	}
	/**
	 * @param Asigna el valor.
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
		this.valorString=valor+"";
	}
	/**
	 * @return Returna el textDiagnostico.
	 */
	public String getTextDiagnostico()
	{
		return textDiagnostico;
	}
	/**
	 * @param Asigna el textDiagnostico.
	 */
	public void setTextDiagnostico(String textDiagnostico)
	{
		this.textDiagnostico = textDiagnostico;
	}
	/**
	 * @return Returna el consulta.
	 */
	public Collection getConsulta()
	{
		return consulta;
	}
	/**
	 * @param Asigna el consulta.
	 */
	public void setConsulta(Collection consulta)
	{
		this.consulta = consulta;
	}
	/**
	 * @return Returna el codigo.
	 */
	public int getCodigo()
	{
		return codigo;
	}
	/**
	 * @param Asigna el codigo.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}
	/**
	 * @return Returna el anio.
	 */
	public String getAnio()
	{
		return anio;
	}
	/**
	 * @param Asigna el anio.
	 */
	public void setAnio(String anio)
	{
		this.anio = anio;
	}
	/**
	 * @return Returna el ordenxColumna.
	 */
	public String getOrdenxColumna()
	{
		return ordenxColumna;
	}
	/**
	 * @param Asigna el ordenxColumna.
	 */
	public void setOrdenxColumna(String ordenxColumna)
	{
		this.ordenxColumna = ordenxColumna;
	}
	/**
	 * @return Returna el ordenxColumnaAnterior.
	 */
	public String getOrdenxColumnaAnterior()
	{
		return ordenxColumnaAnterior;
	}
	/**
	 * @param Asigna el ordenxColumnaAnterior.
	 */
	public void setOrdenxColumnaAnterior(String ordenxColumnaAnterior)
	{
		this.ordenxColumnaAnterior = ordenxColumnaAnterior;
	}
	/**
	 * @return Returna el checks.
	 */
	public HashMap getChecks()
	{
		return checks;
	}
	/**
	 * @param Asigna el checks.
	 */
	public void setChecks(HashMap checks)
	{
		this.checks = checks;
	}
	
	/**
	 * Obtener un check específico
	 * @param key
	 * @return
	 */
	public Object getCheck(String key)
	{
		return checks.get(key);
	}
	
	public void setCheck(String key, Object value)
	{
		checks.put(key, value);
	}
	/**
	 * @return Returna el usuario.
	 */
	public String getUsuario()
	{
		return usuario;
	}
	/**
	 * @param Asigna el usuario.
	 */
	public void setUsuario(String loginUsuario)
	{
		this.usuario = loginUsuario;
	}
	/**
	 * @return Retorna valorString.
	 */
	public String getValorString()
	{
		return valorString;
	}
	/**
	 * @param valorString Asigna valorString.
	 */
	public void setValorString(String valorString)
	{
		this.valorString = valorString;
	}
	/**
	 * @return Retorna tipoRegimen.
	 */
	public String getTipoRegimen()
	{
		return tipoRegimen;
	}
	/**
	 * @param tipoRegimen Asigna tipoRegimen.
	 */
	public void setTipoRegimen(String tipoRegimen)
	{
		this.tipoRegimen = tipoRegimen;
	}
}
