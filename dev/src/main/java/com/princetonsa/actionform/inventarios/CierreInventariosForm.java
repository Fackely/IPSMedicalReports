/*
 * Created on 19/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.actionform.inventarios;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.inventarios.UtilidadInventarios;

/**
 * @version 1.0, 19/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class CierreInventariosForm extends ValidatorForm
{

    /**
     * Variable para manejar el estado de la funcionalidad
     */
    private String estado;
    /**
     * Anio del cierre que se esta realizando
     */
    private String anioCierre;
    /**
     * Mes del cierre que se esta realizando
     */
    private String mesCierre;
    /**
     * Observaciones del cierre. 
     */
    private String observacionesCierre;
    /**
     * boolean que indica si el cierre que se esta realizando es de cierre inicial.
     */
    private boolean cierreInicial;
    /**
     * boolean que indica si el cierre que se esta realizando es de cierre final.
     */
    private boolean cierreFinal;
    
    /**
     * Boolean que indica si existe cierre o no.
     */
    private boolean existeCierre;
    
    /**
     * Boolean que indica si existe Cierre Inicial o No.
     */
    private boolean existeCierreInicial;
    
    /**
     * Institucion del usuario en el sistema.
     */
    private int institucion;
    
    /**
     * Variable para manejar el codigo del cierre.
     */
    private String codigoCierre;
    
    
    /**
     * Metodo que resetea todas las variables del bean.
     */
    public void reset()
    {
        this.anioCierre="";
        this.mesCierre="";
        this.observacionesCierre="";
        this.cierreInicial=false;
        this.cierreFinal=false;
        this.existeCierre=false;
        this.existeCierreInicial=false;
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.codigoCierre="";
    }
    
    
    /**
     * metodo que postula los valores iniciales del mes y anio de cierre.
     */
    public void inicializarMesAnioCierre(int institucion)
    {
        int aTemp=UtilidadInventarios.obtenerAnioUltimoCierre(institucion);
        int mTemp=UtilidadInventarios.obtenerMesUltimoCierreAnio(institucion,aTemp);
        if(aTemp==-1&&mTemp==-1)
        {
            String[] fechaTemp=UtilidadFecha.getFechaActual().split("/");
            aTemp=Integer.parseInt(fechaTemp[2]);
            mTemp=Integer.parseInt(fechaTemp[1]);
            if(mTemp==1)
            {
                aTemp=aTemp-1;
                mTemp=12;
            }
            else
            {
                mTemp=mTemp-1;
            }
        }
        else
        {
            if(mTemp==12)
            {
                mTemp=1;
                aTemp=aTemp+1;
            }
            else
            {
                mTemp=mTemp+1;
            }
        }
        this.anioCierre=aTemp+"";
        this.mesCierre=mTemp+"";
    }
    
    /**
     * Metodo para realizar las validaciones de la funcionalidad.
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("generar"))
		{
			if(UtilidadTexto.isEmpty(this.anioCierre) || UtilidadTexto.isEmpty(this.mesCierre))
		    {
		        errores.add("error fecha inalidad", new ActionMessage("error.cierre.fechaCierreInvalido"));
		        return errores;
		    }
		    
			int anioActual,mesActual;
		    String[] fechaTemp=UtilidadFecha.getFechaActual().split("/");
		    anioActual=Integer.parseInt(fechaTemp[2]);
		    mesActual=Integer.parseInt(fechaTemp[1]);
		    if(Integer.parseInt(this.anioCierre)>anioActual)
		    {
		        errores.add("error fecha inalidad", new ActionMessage("error.cierre.fechaCierreInvalido"));
		        this.setExisteCierre(false);
		    	this.setCodigoCierre("");
		        return errores;
		    }
		    else if(Integer.parseInt(this.anioCierre)==anioActual&&Integer.parseInt(this.mesCierre)>=mesActual)
		    {
		        errores.add("error fecha inalidad", new ActionMessage("error.cierre.fechaCierreInvalido"));
		        this.setExisteCierre(false);
		    	this.setCodigoCierre("");
		        return errores;
		    }
			
			if(UtilidadInventarios.existeCierreInventarioParaFecha("01/"+this.getMesCierre()+"/"+this.getAnioCierre(),this.institucion))
		    {
		        this.setExisteCierre(true);
		        this.setCodigoCierre(UtilidadInventarios.obtenerCodigoCierreInventario(this.institucion,this.getMesCierre(),this.getAnioCierre()));
			    return errores;
		    }else
		    {
		    	this.setExisteCierre(false);
		    	this.setCodigoCierre("");
		    }
		}
		if(this.estado.equals("eliminar")||this.estado.equals("reprocesar"))
		{
			if(this.cierreInicial&&!this.cierreFinal)//cierre inicial
		    {
			    if(UtilidadInventarios.existeCierreFinalAnio(this.institucion, this.anioCierre))
		        {
			    	errores.add("YA EXISTE CIERRE FINAL",new ActionMessage("error.cierre.yaExisteCierreFinalAnio",this.anioCierre));
		        }
		    }
			else if(!this.cierreInicial&&this.cierreFinal)//cierre final
		    {
				if(UtilidadInventarios.existeCierreFinalesPosterioresAnio(this.institucion, this.anioCierre))
		        {
		            errores.add("cierres posteriores",new ActionMessage("error.cierre.cierreFinalesPosteriores",this.anioCierre));
		        }
		    }
			else if(!this.cierreFinal&&!this.cierreInicial)//cierre mensual
		    {
				if(mesCierre.equals("12"))//se puede reprocesa como cierre final o no siempre y cuando no exista cierre final, cuando esto se hace con mes 12 toca validar por separado, ya que un cierre final se puede convertir en normal, me imagino que siempre y cuando no existan cierres finales posteriores.
		        {
		        	if(UtilidadInventarios.existeCierreFinalesPosterioresAnio(this.institucion, this.anioCierre))
			        {
			            errores.add("cierres posteriores",new ActionMessage("error.cierre.cierreFinalesPosteriores",this.anioCierre));
			        }
		        }
		        else
		        {
			        if(UtilidadInventarios.existeCierreFinalAnio(this.institucion,this.anioCierre))
			        {
			            errores.add("YA EXISTE CIERRE FINAL",new ActionMessage("error.cierre.yaExisteCierreFinalAnio",this.anioCierre));
			        }
		        }
		    }
		}
		if(this.estado.equals("generar")||this.estado.equals("reprocesar"))
		{
			/*
		    if(UtilidadInventarios.numeroMovimientosInventariosEntreFecha(this.institucion,"01/"+this.mesCierre+"/"+this.anioCierre,"31/"+this.mesCierre+"/"+this.anioCierre)<=0)////VALIDAR QUE EXISTAN MOVIMIENTOS EN EL MES.
		    {
		        errores.add("error fecha sin movimientos", new ActionMessage("error.cierre.noMovimientosFecha",this.mesCierre+"/"+this.anioCierre));
		    }*/
		    if(this.cierreInicial&&!this.cierreFinal)//cierre inicial
		    {
			    if(UtilidadInventarios.existenCierresAnterioresAFecha(this.institucion,"01/"+this.mesCierre+"/"+this.anioCierre))
			    {
			        errores.add("EXISTEN CIERRES ANTERIORES", new ActionMessage("error.cierre.cierresAnteriores"));
			    }
		    }
		    else if(!this.cierreInicial&&this.cierreFinal)//cierre final
		    {
		        if(Integer.parseInt(this.mesCierre)!=12)//solo se puede generar cierre final en diciembre
		        {
		            errores.add("SOLO SE PUEDE GENERAR CIERRE FINAL EN DICIEMBRE",new ActionMessage("error.cierre.cierreFinalSoloDicembre"));
		        }
		        String temp=UtilidadInventarios.existeCierreInicialFecha(this.institucion,this.anioCierre);
		        if(!temp.equals(ConstantesBD.codigoNuncaValido+""))//el cierre inicial es el el mismo anio.
		        {
		            for(int i=Integer.parseInt(temp.split("/")[0])+1;i<12;i++)
		            {
		                if(!UtilidadInventarios.existeCierreInventarioParaFecha("01/"+(i<10?"0"+i:i+"")+"/"+this.anioCierre, this.institucion))
		                {
		                    errores.add("FALTA GENERAR CIERRES MENSUALES",new ActionMessage("error.cierre.faltaGenerarCierreMensualFecha",(i<10?"0"+i:i+""),this.anioCierre));
		                }
		            }
		        }
		        else 
		            {
		            if(!UtilidadInventarios.existeCierreFinalAnio(this.institucion, (Integer.parseInt(this.anioCierre)-1)+""))
			        {
			            errores.add("FALTA GENERAR CIERRES FINALES",new ActionMessage("error.cierre.faltaCienreFinalAnio",(Integer.parseInt(this.anioCierre)-1)+""));
			        }
			        else
			        {
			            for(int i=1;i<12;i++)
			            {
			                if(!UtilidadInventarios.existeCierreInventarioParaFecha("01/"+(i<10?"0"+i:i+"")+"/"+this.anioCierre, this.institucion))
			                {
			                    errores.add("FALTA GENERAR CIERRES MENSUALES",new ActionMessage("error.cierre.faltaGenerarCierreMensualFecha",(i<10?"0"+i:i+""),this.anioCierre));
			                }
			            }
			        }
		        }
		    }
		    else if(!this.cierreFinal&&!this.cierreInicial)//cierre mensual
		    {
		        String temp=UtilidadInventarios.fechaCierreInicial(this.institucion);
		        if(!temp.equals(ConstantesBD.codigoNuncaValido+""))
		        {
		            String aTemp=temp.split("/")[1];
		            String mTemp=temp.split("/")[0];
		            if((Integer.parseInt(this.anioCierre)<Integer.parseInt(aTemp))||((Integer.parseInt(this.anioCierre)==Integer.parseInt(aTemp)&&Integer.parseInt(this.mesCierre)<Integer.parseInt(mTemp))))
		            {
		               	errores.add("EXISTE CIERRE INICIAL SUPERIOR",new ActionMessage("error.cierre.cierreInicialMayorAFecha",mTemp,aTemp));
		            }
		        }
		        if(this.estado.equals("generar")&&!this.mesCierre.equals("12"))
		        {
			        if(UtilidadInventarios.existeCierreFinalAnio(this.institucion,this.anioCierre))
			        {
			            errores.add("YA EXISTE CIERRE FINAL",new ActionMessage("error.cierre.yaExisteCierreFinalAnio",this.anioCierre));
			        }
		        }
		    }
		}
		//FALTA REALIZAR LAS VALIDACIONES PARA ELIMINAR.
		if(this.estado.equals("eliminar"))
		{
		    
		}
		
		if(!errores.isEmpty())
		{
			this.setExisteCierre(false);
	    	this.setCodigoCierre("");
		}
		return errores;
	}
    /**
     * @return Returns the cierreFinal.
     */
    public boolean isCierreFinal()
    {
        return cierreFinal;
    }
    /**
     * @param cierreFinal The cierreFinal to set.
     */
    public void setCierreFinal(boolean cierreFinal)
    {
        this.cierreFinal = cierreFinal;
    }
    /**
     * @return Returns the cierreInicial.
     */
    public boolean isCierreInicial()
    {
        return cierreInicial;
    }
    /**
     * @param cierreInicial The cierreInicial to set.
     */
    public void setCierreInicial(boolean cierreInicial)
    {
        this.cierreInicial = cierreInicial;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado()
    {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
   
    /**
     * @return Returns the observacionesCierre.
     */
    public String getObservacionesCierre()
    {
        return observacionesCierre;
    }
    /**
     * @param observacionesCierre The observacionesCierre to set.
     */
    public void setObservacionesCierre(String observacionesCierre)
    {
        this.observacionesCierre = observacionesCierre;
    }
    /**
     * @return Returns the anioCierre.
     */
    public String getAnioCierre()
    {
        return anioCierre;
    }
    /**
     * @param anioCierre The anioCierre to set.
     */
    public void setAnioCierre(String anioCierre)
    {
        this.anioCierre = anioCierre;
    }
    /**
     * @return Returns the mesCierre.
     */
    public String getMesCierre()
    {
        return mesCierre;
    }
    /**
     * @param mesCierre The mesCierre to set.
     */
    public void setMesCierre(String mesCierre)
    {
        if(mesCierre.length()==1)
            mesCierre="0"+mesCierre;
        this.mesCierre = mesCierre;
    }
    /**
     * @return Returns the existeCierre.
     */
    public boolean isExisteCierre()
    {
        return existeCierre;
    }
    /**
     * @param existeCierre The existeCierre to set.
     */
    public void setExisteCierre(boolean existeCierre)
    {
        this.existeCierre = existeCierre;
    }
    /**
     * @return Returns the existeCierreInicial.
     */
    public boolean isExisteCierreInicial()
    {
        return existeCierreInicial;
    }
    /**
     * @param existeCierreInicial The existeCierreInicial to set.
     */
    public void setExisteCierreInicial(boolean existeCierreInicial)
    {
        this.existeCierreInicial = existeCierreInicial;
    }
    /**
     * @return Returns the institucion.
     */
    public int getInstitucion()
    {
        return institucion;
    }
    /**
     * @param institucion The institucion to set.
     */
    public void setInstitucion(int institucion)
    {
        this.institucion = institucion;
    }


	/**
	 * @return Retorna codigoCierre.
	 */
	public String getCodigoCierre()
	{
		return codigoCierre;
	}


	/**
	 * @param codigoCierre Asigna codigoCierre.
	 */
	public void setCodigoCierre(String codigoCierre)
	{
		this.codigoCierre = codigoCierre;
	}
}
